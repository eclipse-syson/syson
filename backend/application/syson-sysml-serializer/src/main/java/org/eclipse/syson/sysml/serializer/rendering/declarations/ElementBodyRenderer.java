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
package org.eclipse.syson.sysml.serializer.rendering.declarations;

import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.syson.sysml.serializer.ElementDispatch;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.model.ElementSyntaxQueries;
import org.eclipse.syson.sysml.serializer.model.ModelFlags;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.syntax.CommentRenderer;
import org.eclipse.syson.sysml.serializer.syntax.ParserFragments;
import org.eclipse.syson.sysml.serializer.syntax.SourceSyntax;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Comment;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Emits element and statement bodies, preserving comments and relationship sibling context.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class ElementBodyRenderer {

    private static final String PARSER_MEMBER_SUFFIX = "Member";

    private static final Set<String> UNTERMINATED_STATEMENT_FRAGMENTS = Set.of(
            ParserFragments.DEFAULT_TARGET_SUCCESSION,
            ParserFragments.EFFECT_BEHAVIOR_USAGE,
            ParserFragments.GUARDED_TARGET_SUCCESSION,
            ParserFragments.PERFORMED_ACTION_USAGE,
            ParserFragments.TARGET_SUCCESSION,
            ParserFragments.TRANSITION_SUCCESSION,
            ParserFragments.TRANSITION_SUCCESSION_MEMBER,
            ParserFragments.TRIGGER_ACTION,
            ParserFragments.TRIGGER_ACTION_MEMBER);

    private static final Set<String> UNTERMINATED_DECLARATION_FRAGMENTS = Set.of(
            ParserFragments.BODY_PARAMETER,
            ParserFragments.FOR_VARIABLE_DECLARATION,
            ParserFragments.FOR_VARIABLE_DECLARATION_MEMBER,
            ParserFragments.MULTIPLICITY_SOURCE_END,
            ParserFragments.OWNED_CROSS_FEATURE_MEMBER,
            ParserFragments.OWNED_CROSS_MULTIPLICITY,
            ParserFragments.OWNED_CROSS_MULTIPLICITY_MEMBER,
            ParserFragments.PAYLOAD_FEATURE,
            ParserFragments.PAYLOAD_FEATURE_MEMBER,
            ParserFragments.PAYLOAD_PARAMETER,
            ParserFragments.PAYLOAD_PARAMETER_MEMBER);

    private final ElementSyntaxQueries syntax;

    private final CommentRenderer comments;

    private final ElementDispatch dispatch;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ModelFlags flags;

    /**
     * Creates body rendering that chooses delimiters, preserves member order and delegates recursive member rendering.
     *
     * @param syntax
     *            decides whether a declaration needs a body block or a statement terminator
     * @param comments
     *            writes source notes associated with bodies
     * @param dispatch
     *            renders body elements and relationships through the session
     * @param context
     *            supplies the run's output and model queries
     */
    public ElementBodyRenderer(final ElementSyntaxQueries syntax,
            final CommentRenderer comments,
            final ElementDispatch dispatch,
            final SerializationContext context) {
        this.syntax = syntax;
        this.comments = comments;
        this.dispatch = dispatch;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.flags = context.modelQueries().flags();
    }

    /**
     * Writes <code>{ ... }</code> with every body relationship of {@code element}, its trailing comments, and {@code parallel} before
     * the brace for parallel states. Comments attached to generated, unrendered members are written after the closing
     * brace so they are not lost. Does not end the line.
     */
    public void serializeBody(final Element element) {
        final TextOutput output = this.context.output();
        output.space();
        if (this.flags.isParallel(element)) {
            output.token(SysMLTokens.PARALLEL_KEYWORD).space();
        }
        output.beginBlock();
        final List<Relationship> relationships = this.navigation
                .bodyRelationships(element);
        this.serializeNamespaceBody(element, relationships);
        this.comments.serializeTrailingComments(element);
        output.endBlock();
        this.serializeGeneratedMemberComments(relationships);
    }

    /**
     * Ends a declaration whose renderer selected the body members itself, because ends or payloads are already written
     * inline: writes <code>{ ... }</code> with those members and the trailing comments of {@code element} when there are
     * members, trailing comments or a body block recorded by the parser, otherwise {@code ;}. Always ends the line.
     */
    public void serializeSelectedBodyOrStatementEnd(final Element element,
            final List<Relationship> relationships) {
        final TextOutput output = this.context.output();
        final boolean hasMembers = relationships.stream().anyMatch(this::hasBodyMember);
        if (!hasMembers && !this.syntax.hasStatementBody(element)) {
            this.writeStatementTerminator(element);
            return;
        }
        output.space().beginBlock();
        relationships.forEach(this.dispatch::serializeRelationship);
        this.comments.serializeTrailingComments(element);
        output.endBlock().newline();
    }

    /**
     * Tells whether {@code relationship} is written in the body: it owns no element (an import or alias) or owns
     * something other than metadata promoted to a {@code #Keyword} of the declaration.
     */
    private boolean hasBodyMember(final Relationship relationship) {
        final List<Element> relatedElements = this.navigation.ownedRelatedElements(relationship);
        return relatedElements.isEmpty()
                || !relatedElements.stream().allMatch(this.syntax::isPromotedUserKeywordMetadata);
    }

    /**
     * Ends a declaration whose grammar allows only comments and metadata in its body (optionally also explicit feature
     * bindings): writes that restricted body when one exists, otherwise {@code ;}. Root fragments of parser rules that have
     * no terminator end with a line break only.
     */
    public void serializeStatementEnd(final Element element,
            final boolean includeFeatureBindings) {
        if (this.syntax.hasStatementBody(element, includeFeatureBindings)) {
            this.serializeStatementBody(element, includeFeatureBindings);
            this.context.output().newline();
        } else {
            this.writeStatementTerminator(element);
        }
    }

    private void writeStatementTerminator(final Element element) {
        final TextOutput output = this.context.output();
        if (!this.isUnterminatedStatementFragment(element)) {
            output.token(SysMLTokens.STATEMENT_TERMINATOR);
        }
        output.newline();
    }

    /**
     * Tells whether {@code element} is the root of a parser-entry fragment whose grammar rule ends without {@code ;}
     * (successions, trigger actions, effect behaviors). For {@code ...Member} rules the owned element is treated as the root
     * too.
     */
    private boolean isUnterminatedStatementFragment(final Element element) {
        final String entryPoint = this.context.options().parserEntryPoint();
        return UNTERMINATED_STATEMENT_FRAGMENTS.contains(entryPoint)
                && (SourceSyntax.isRootParserEntry(this.context.options(), element, entryPoint)
                        || entryPoint.endsWith(PARSER_MEMBER_SUFFIX)
                                && SourceSyntax.isParserEntryChild(this.context.options(), element,
                                        entryPoint));
    }

    /**
     * Writes the restricted statement body: comments, non-keyword metadata and, when requested, explicit feature
     * bindings. All other members are left to the declaration header that already rendered them.
     */
    private void serializeStatementBody(final Element element,
            final boolean includeFeatureBindings) {
        final TextOutput output = this.context.output();
        output.space().beginBlock();
        for (final Relationship relationship : this.navigation
                .bodyRelationships(element)) {
            for (final Element related : this.navigation
                    .ownedRelatedElements(relationship)) {
                if (this.syntax.isNestedEndReference(element, related)) {
                    continue;
                }
                if (related instanceof Comment
                        || this.syntax.isStatementBodyMetadata(related)
                        || includeFeatureBindings
                                && this.syntax.isExplicitFeatureBinding(related)) {
                    this.dispatch.serializeElement(related);
                }
            }
        }
        this.comments.serializeTrailingComments(element);
        output.endBlock();
    }

    /**
     * Writes <code>{ ... }</code> while skipping relationships whose members are all plain {@code Feature}s (generated ends or
     * parameters) or given elements, and relationships of the given metaclasses. Relationships that own no element,
     * such as imports and aliases, are kept. Does not end the line.
     */
    public void serializeBodyExcludingImplicitFeatures(final Element element,
            final List<Element> additionalSkippedElements,
            final List<EClass> skippedRelationshipClasses) {
        final TextOutput output = this.context.output();
        output.space().beginBlock();
        final List<Relationship> relationships = this.navigation
                .bodyRelationships(element);
        for (int index = 0; index < relationships.size(); index++) {
            final Relationship relationship = relationships.get(index);
            if (skippedRelationshipClasses.contains(relationship.eClass())
                    || this.syntax.isDeclarationTargetMembership(element, relationship)
                    || this.ownsOnlySkippedElements(relationship, additionalSkippedElements)) {
                continue;
            }
            // Skipped relationships retain their positions in the sibling context used by succession rendering.
            this.dispatch.serializeRelationship(relationship, relationships,
                    index);
        }
        this.comments.serializeTrailingComments(element);
        output.endBlock();
    }

    /**
     * Tells whether every element owned by {@code relationship} is a plain {@code Feature} or one of the
     * {@code additionalSkippedElements}. A relationship that owns nothing, such as an import or an alias, is user-written
     * body content and is never skipped.
     */
    private boolean ownsOnlySkippedElements(final Relationship relationship,
            final List<Element> additionalSkippedElements) {
        final List<Element> relatedElements = this.navigation.ownedRelatedElements(relationship);
        return !relatedElements.isEmpty() && relatedElements.stream()
                .allMatch(related -> this.navigation.isMetaclass(related, SysMLPackage.eINSTANCE.getFeature())
                        || additionalSkippedElements.contains(related));
    }

    /**
     * Writes the body members of {@code element} without braces. End features skip their nested reference usages, which
     * the end declaration already renders.
     */
    public void serializeNamespaceBody(final Element element) {
        final List<Relationship> relationships = this.navigation
                .bodyRelationships(element);
        this.serializeNamespaceBody(element, relationships);
    }

    /**
     * Dispatches each body relationship with its sibling list and index, which succession and transition rendering use
     * to find neighbouring members.
     */
    private void serializeNamespaceBody(final Element element,
            final List<Relationship> relationships) {
        for (int index = 0; index < relationships.size(); index++) {
            final Relationship relationship = relationships.get(index);
            final List<Element> relatedElements = this.navigation
                    .ownedRelatedElements(relationship);
            if (!relatedElements.isEmpty() && relatedElements.stream().allMatch(
                    related -> this.syntax.isNestedEndReference(element, related))) {
                continue;
            }
            this.dispatch.serializeRelationship(relationship, relationships,
                    index);
        }
    }

    /**
     * Writes the comments attached to empty port conjugations and, recursively, to the members of generated elements
     * among {@code relationships}; those produce no declaration that could carry the comments.
     */
    private void serializeGeneratedMemberComments(final List<Relationship> relationships) {
        for (final Relationship relationship : relationships) {
            final List<Element> relatedElements = this.navigation.ownedRelatedElements(relationship);
            if (relatedElements.isEmpty() && this.navigation.isMetaclass(relationship,
                    SysMLPackage.eINSTANCE.getPortConjugation())) {
                this.comments.serializeLeadingComments(relationship);
            }
            for (final Element related : relatedElements) {
                if (this.syntax.isAnonymousGeneratedElement(related)) {
                    this.serializeGeneratedMemberComments(this.navigation.bodyRelationships(related));
                }
            }
        }
    }

    /**
     * Ends a declaration with <code>{ ... }</code> when it has a serializable body (or a recorded source block, or is {@code parallel},
     * except for ends), otherwise with {@code ;}. Root fragments of declaration rules that have no terminator, such as
     * payload or for-variable declarations, end with a line break only. Always ends the output line.
     */
    public void renderBodyOrStatementEnd(final Element element) {
        if (this.syntax.shouldSerializeBlockBody(element)) {
            this.serializeBody(element);
            this.context.output().newline();
        } else if (this.isUnterminatedDeclarationFragment(element)) {
            this.context.output().newline();
        } else {
            this.context.output().token(SysMLTokens.STATEMENT_TERMINATOR)
                    .newline();
        }
    }

    /**
     * Tells whether {@code element} is, or is directly owned by, the root of a parser-entry fragment whose declaration
     * rule has no {@code ;} terminator.
     */
    private boolean isUnterminatedDeclarationFragment(final Element element) {
        final String entryPoint = this.context.options().parserEntryPoint();
        return UNTERMINATED_DECLARATION_FRAGMENTS.contains(entryPoint)
                && (SourceSyntax.isRootParserEntry(this.context.options(), element, entryPoint)
                        || SourceSyntax.isParserEntryChild(this.context.options(), element, entryPoint));
    }

}
