/*******************************************************************************
 * Copyright (c) 2026 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.syson.sysml.serializer.rendering.relationships;

import java.util.List;

import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.ElementDispatch;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.SerializationMode;
import org.eclipse.syson.sysml.serializer.model.ElementSyntaxQueries;
import org.eclipse.syson.sysml.serializer.model.ModelFlags;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.model.ModelReference;
import org.eclipse.syson.sysml.serializer.model.ParserAdapterQueries;
import org.eclipse.syson.sysml.serializer.model.ReferenceTargets;
import org.eclipse.syson.sysml.serializer.model.StoredModel;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.rendering.declarations.DeclarationRenderer;
import org.eclipse.syson.sysml.serializer.syntax.ParserFragments;
import org.eclipse.syson.sysml.serializer.syntax.SourceSyntax;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.ActionDefinition;
import org.omg.sysml.lang.sysml.ActionUsage;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.SuccessionAsUsage;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.lang.sysml.TransitionUsage;

/**
 * Writes memberships without a membership keyword: owning memberships with their visibility and members, and the
 * non-owning forms {@code first target;} (grammar {@code initialNodeMember}) and {@code alias X for Y;} (grammar
 * {@code aliasMember}).
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class MembershipRenderer {

    private static final String MEMBER_ELEMENT_REFERENCE = "memberElement";

    private final DeclarationRenderer declarations;

    private final ElementDispatch dispatch;

    private final ReferenceResolver referenceResolver;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final ElementSyntaxQueries syntax;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ModelFlags flags;

    private final ParserAdapterQueries adapters;

    private final ReferenceTargets referenceTargets;

    /**
     * Creates the renderer.
     *
     * @param declarations
     *            writes comments, visibility and body ends
     * @param dispatch
     *            writes owned members and alias bodies
     * @param referenceResolver
     *            resolves names and endpoint paths
     * @param referenceWriter
     *            writes references with traceability metadata
     * @param syntax
     *            detects generated members and statement bodies
     * @param context
     *            the serialization context
     */
    public MembershipRenderer(final DeclarationRenderer declarations,
            final ElementDispatch dispatch,
            final ReferenceResolver referenceResolver,
            final ExpressionAndReferenceWriter referenceWriter,
            final ElementSyntaxQueries syntax,
            final SerializationContext context) {
        this.declarations = declarations;
        this.dispatch = dispatch;
        this.referenceResolver = referenceResolver;
        this.referenceWriter = referenceWriter;
        this.syntax = syntax;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.flags = context.modelQueries().flags();
        this.adapters = context.modelQueries().adapters();
        this.referenceTargets = context.modelQueries().referenceTargets();
    }

    /**
     * Writes a membership that has no keyword-specific syntax. Non-owning memberships become {@code first target;}
     * (action bodies), {@code first start;} or {@code alias ... for target;}; owning memberships write their visibility
     * and dispatch the owned members. {@code siblings} and {@code membershipIndex} locate the membership in its owner's
     * body, which is needed to tell a {@code first} membership from a plain one by looking at the successions that
     * follow it.
     *
     * @param membership
     *            the membership
     * @param siblings
     *            the body relationships of the owner, in order
     * @param membershipIndex
     *            the index of {@code membership} in {@code siblings}
     */
    void serializeMembership(final Membership membership, final List<Relationship> siblings,
            final int membershipIndex) {
        this.declarations.serializeLeadingComments(membership);
        final List<Element> relatedElements = this.navigation.ownedRelatedElements(membership);
        if (this.serializeFirstMembership(membership, relatedElements, siblings, membershipIndex)
                || this.serializeAliasMembership(membership)) {
            return;
        }
        this.declarations.serializeMembershipVisibility(membership);
        this.serializeMembershipElements(membership, relatedElements);
        this.declarations.serializeTrailingComments(membership);
    }

    /**
     * Writes the action-body forms of a non-owning membership ({@code first target;} or {@code first start;}) and
     * returns true, or returns false with nothing written. The source {@code first} marker is honored first in
     * round-trip mode; each later form is only tried when the previous one did not apply.
     */
    private boolean serializeFirstMembership(final Membership membership, final List<Element> relatedElements,
            final List<Relationship> siblings, final int membershipIndex) {
        if (this.context.options().mode() == SerializationMode.ROUND_TRIP
                && this.adapters.hasTextAdapter(membership, ITextAdapter.FIRST_MEMBERSHIP)
                && relatedElements.isEmpty() && this.isUnnamed(membership)) {
            final String target = this.firstMembershipTarget(membership);
            if (!target.isBlank()) {
                this.writeFirstMembershipTarget(membership, target);
                this.declarations.serializeTrailingComments(membership);
                return true;
            }
        }
        return this.serializeParserInitialNodeMember(membership, relatedElements)
                || this.serializeImplicitStartMembership(membership, relatedElements, siblings, membershipIndex)
                || this.serializeFirstMembershipTarget(membership, relatedElements, siblings, membershipIndex);
    }

    /**
     * For the {@code initialNodeMember} parser fragment, writes {@code first target;} from the parser spelling, since
     * the target is not resolved in a fragment parse.
     */
    private boolean serializeParserInitialNodeMember(final Membership membership,
            final List<Element> relatedElements) {
        if (!relatedElements.isEmpty() || !SourceSyntax.isRootParserEntry(this.context.options(), membership,
                ParserFragments.INITIAL_NODE_MEMBER)) {
            return false;
        }
        final String target = this.context.danglingReferenceIndex().targets(membership).stream()
                .findFirst().orElse("");
        return this.writeFirstMembershipTargetIfPresent(membership, target);
    }

    /** Writes {@code first start;} when {@link #canSerializeImplicitStartMembership} accepts the membership. */
    private boolean serializeImplicitStartMembership(final Membership membership,
            final List<Element> relatedElements, final List<Relationship> siblings, final int membershipIndex) {
        final boolean implicitStart = this.canSerializeImplicitStartMembership(membership, relatedElements, siblings,
                membershipIndex);
        if (implicitStart) {
            this.context.output().token(SysMLTokens.FIRST_KEYWORD).space()
                    .token(SysMLTokens.START_FEATURE_NAME)
                    .token(SysMLTokens.STATEMENT_TERMINATOR).newline();
        }
        return implicitStart;
    }

    /**
     * Tells whether an unresolved membership to {@code start} must be written as {@code first start;}. It is skipped
     * when the next sibling is a succession with a statement body that renders the start itself, except in use cases.
     */
    private boolean canSerializeImplicitStartMembership(final Membership membership,
            final List<Element> relatedElements, final List<Relationship> siblings, final int membershipIndex) {
        final boolean unresolvedStart = relatedElements.isEmpty()
                && this.context.danglingReferenceIndex().targets(membership).stream()
                        .anyMatch(SysMLTokens.START_FEATURE_NAME::equals);
        if (!unresolvedStart) {
            return false;
        }
        final boolean startRenderedBySuccession = this.hasLaterSuccession(siblings, membershipIndex)
                && this.nextSiblingHasStatementSuccession(siblings, membershipIndex)
                && !this.navigation.isMetaclass(this.navigation.containingElement(membership),
                        SysMLPackage.eINSTANCE.getUseCaseUsage());
        return !startRenderedBySuccession;
    }

    /**
     * Writes an unnamed non-owning membership as {@code first target;} when it was marked as such, is followed by a
     * succession, or sits directly in an action whose member it references. Returns false with nothing written
     * otherwise.
     */
    private boolean serializeFirstMembershipTarget(final Membership membership, final List<Element> relatedElements,
            final List<Relationship> siblings, final int membershipIndex) {
        if (!relatedElements.isEmpty() || !this.isUnnamed(membership)) {
            return false;
        }
        final boolean firstMembership = this.adapters.hasTextAdapter(membership, ITextAdapter.FIRST_MEMBERSHIP)
                || this.hasLaterSuccession(siblings, membershipIndex)
                || this.isStandaloneActionFirstMembership(membership);
        return firstMembership
                && this.writeFirstMembershipTargetIfPresent(membership, this.firstMembershipTarget(membership));
    }

    /**
     * Returns the written name of the membership's member element, preferring the parser spelling, or an empty string.
     */
    private String firstMembershipTarget(final Membership membership) {
        return this.referenceTargets.referenceTargets(membership, MEMBER_ELEMENT_REFERENCE,
                this.context.danglingReferenceIndex(), this.context.nameRenderer()::referenceName).stream()
                .map(ModelReference::name)
                .findFirst().orElse("");
    }

    /**
     * Matches an unnamed membership of a feature directly in an action (not a transition), which only the
     * {@code first} syntax can express.
     */
    private boolean isStandaloneActionFirstMembership(final Membership membership) {
        final Element owner = this.navigation.containingElement(membership);
        final boolean actionOwner = (owner instanceof ActionDefinition || owner instanceof ActionUsage)
                && !(owner instanceof TransitionUsage);
        final boolean featureMember = StoredModel.memberElement(membership) instanceof Feature
                || this.context.danglingReferenceIndex().hasTargetOfType(membership,
                        SysMLPackage.eINSTANCE.getMembership_MemberElement(), SysMLPackage.eINSTANCE.getFeature());
        return actionOwner && this.isUnnamed(membership) && featureMember;
    }

    private boolean isUnnamed(final Membership membership) {
        return this.flags.memberName(membership).isBlank() && this.flags.memberShortName(membership).isBlank();
    }

    /** Writes {@code first target;} and returns true, or returns false with nothing written for a blank target. */
    private boolean writeFirstMembershipTargetIfPresent(final Membership membership, final String target) {
        if (target.isBlank()) {
            return false;
        }
        this.writeFirstMembershipTarget(membership, target);
        return true;
    }

    private void writeFirstMembershipTarget(final Membership membership, final String target) {
        this.declarations.serializeMembershipVisibility(membership);
        this.context.output().token(SysMLTokens.FIRST_KEYWORD).space();
        this.referenceWriter.writeReferenceText(this.context.output(), target, membership)
                .token(SysMLTokens.STATEMENT_TERMINATOR).newline();
    }

    /**
     * Tells whether any later sibling owns a succession, which makes a preceding non-owning membership its
     * {@code first} source.
     */
    private boolean hasLaterSuccession(final List<Relationship> siblings, final int membershipIndex) {
        return siblings.subList(membershipIndex + 1, siblings.size()).stream()
                .flatMap(sibling -> this.navigation.ownedRelatedElements(sibling).stream())
                .anyMatch(SuccessionAsUsage.class::isInstance);
    }

    /** Tells whether the next sibling owns a succession that has a statement body. */
    private boolean nextSiblingHasStatementSuccession(final List<Relationship> siblings, final int membershipIndex) {
        return membershipIndex + 1 < siblings.size()
                && this.navigation.ownedRelatedElements(siblings.get(membershipIndex + 1)).stream()
                        .filter(SuccessionAsUsage.class::isInstance)
                        .map(SuccessionAsUsage.class::cast)
                        .anyMatch(this.syntax::hasStatementBody);
    }

    /**
     * Writes {@code alias <short> name for target} (with body or {@code ;}) for a plain non-owning membership with a
     * target and a name, or for the alias parser rule even when unnamed. Returns false with nothing written otherwise.
     */
    private boolean serializeAliasMembership(final Membership membership) {
        final boolean parserAliasMember = SourceSyntax.isRootParserEntry(this.context.options(), membership,
                ParserFragments.ALIAS_MEMBER);
        if (!parserAliasMember && !this.navigation.isMetaclass(membership, SysMLPackage.eINSTANCE.getMembership())) {
            return false;
        }
        final String aliasTarget = this.referenceTargets.aliasTargetName(membership,
                this.context.danglingReferenceIndex());
        final String aliasName = this.flags.memberName(membership);
        final boolean serializable = !aliasTarget.isBlank() && (!aliasName.isBlank() || parserAliasMember);
        if (serializable) {
            this.writeAliasDeclaration(membership, this.renderedAliasName(membership, aliasName),
                    this.renderedAliasTarget(membership, aliasTarget));
            this.writeAliasBody(membership);
        }
        return serializable;
    }

    /** Returns the alias name, keeping the source's quoted spelling when recorded. */
    private String renderedAliasName(final Membership membership, final String aliasName) {
        return this.referenceResolver.escapedName(aliasName, this.adapters.hasTextAdapter(membership, ITextAdapter.QUOTED_NAME));
    }

    /** Returns the alias target, keeping the source quoting of its last segment when recorded. */
    private String renderedAliasTarget(final Membership membership, final String aliasTarget) {
        if (this.adapters.hasTextAdapter(membership, ITextAdapter.QUOTED_ALIAS_TARGET)) {
            return this.referenceResolver.referenceName(
                    this.referenceResolver.quoteFinalQualifiedNameSegment(aliasTarget));
        }
        return this.referenceResolver.referenceName(aliasTarget);
    }

    /** Writes {@code [visibility] alias [<short>] [name] for target}, without the body. */
    private void writeAliasDeclaration(final Membership membership, final String aliasName,
            final String aliasTarget) {
        final TextOutput output = this.context.output();
        this.declarations.serializeMembershipVisibility(membership);
        output.token(SysMLTokens.ALIAS_KEYWORD).space();
        final String aliasShortName = this.flags.memberShortName(membership);
        if (!aliasShortName.isBlank()) {
            output.token(SysMLTokens.SHORT_NAME_START)
                    .token(this.referenceResolver.referenceName(aliasShortName))
                    .token(SysMLTokens.SHORT_NAME_END).space();
        }
        output.token(aliasName).space().token(SysMLTokens.FOR_KEYWORD).space();
        this.referenceWriter.writeReferencePath(output, aliasTarget,
                this.referenceTargets.firstRelationshipTargetElement(membership));
    }

    /**
     * Writes the alias's comments and metadata in braces, or {@code ;} when it has none and the source used no braces.
     */
    private void writeAliasBody(final Membership membership) {
        final TextOutput output = this.context.output();
        final List<Element> aliasBody = this.navigation.ownedRelationships(membership).stream()
                .flatMap(relationship -> this.navigation.ownedRelatedElements(relationship).stream())
                .filter(related -> !this.syntax.isAnonymousGeneratedElement(related))
                .toList();
        if (aliasBody.isEmpty() && !this.adapters.hasTextAdapter(membership, ITextAdapter.BODY_BLOCK)) {
            output.token(SysMLTokens.STATEMENT_TERMINATOR).newline();
            return;
        }
        output.space().beginBlock();
        aliasBody.forEach(this.dispatch::serializeElement);
        output.endBlock().newline();
    }

    /**
     * Dispatches the owned members, skipping generated ones, except a reference usage that references a target; the
     * payload parameter fragment dispatches generated members too.
     */
    private void serializeMembershipElements(final Membership membership, final List<Element> relatedElements) {
        final boolean payloadParameterFragment = SourceSyntax.isRootParserEntry(this.context.options(), membership,
                ParserFragments.PAYLOAD_PARAMETER_MEMBER);
        for (final Element related : relatedElements) {
            if (payloadParameterFragment || !this.syntax.isAnonymousGeneratedElement(related)) {
                this.dispatch.serializeElement(related);
            } else if (this.navigation.isMetaclass(related, SysMLPackage.eINSTANCE.getReferenceUsage())) {
                this.serializeAnonymousReferenceUsage(related);
            }
        }
    }

    /**
     * Writes an unnamed reference usage that only references another feature ({@code ref ::> target { ... }}). It
     * has no clause the generated-element test looks for, yet the user wrote it: its declaration keeps the
     * {@code ref} keyword, the multiplicity and the body, after the notes the parser attached to it (the forced
     * declaration does not write them). Writes nothing when it references nothing.
     */
    private void serializeAnonymousReferenceUsage(final Element referenceUsage) {
        if (!this.referenceResolver.directReferencedElementTarget(referenceUsage).isBlank()) {
            this.declarations.serializeLeadingComments(referenceUsage);
            this.dispatch.serializeDeclaration(referenceUsage);
        }
    }
}
