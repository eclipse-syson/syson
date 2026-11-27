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
package org.eclipse.syson.sysml.serializer.rendering.actions;

import java.util.List;
import java.util.stream.Stream;

import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.ElementDispatch;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.SerializationMode;
import org.eclipse.syson.sysml.serializer.SerializationOptions;
import org.eclipse.syson.sysml.serializer.model.ElementSyntaxQueries;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.model.NestedTargetSearch;
import org.eclipse.syson.sysml.serializer.model.ParserAdapterQueries;
import org.eclipse.syson.sysml.serializer.model.StoredModel;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.rendering.declarations.DeclarationRenderer;
import org.eclipse.syson.sysml.serializer.syntax.ParserFragments;
import org.eclipse.syson.sysml.serializer.syntax.SourceSyntax;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.AssignmentActionUsage;
import org.omg.sysml.lang.sysml.Comment;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Expression;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.ReferenceUsage;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.SuccessionAsUsage;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.lang.sysml.TransitionFeatureKind;
import org.omg.sysml.lang.sysml.TransitionFeatureMembership;
import org.omg.sysml.lang.sysml.TransitionUsage;

/**
 * Writes transition usages in their state and action body forms:
 * {@code transition t first s accept e if g do a then t2;}, the target-only {@code accept e then t2;} of a state body,
 * guarded successions {@code if g then b;} of action bodies and the default branch {@code else b;} of a decision. The
 * trigger, guard and effect are recovered from the kind of each {@code TransitionFeatureMembership}, and the target
 * from the owned succession.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class TransitionRenderer {

    private final DeclarationRenderer declarations;

    private final ElementDispatch dispatch;

    private final ReferenceResolver referenceResolver;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ParserAdapterQueries adapters;

    private final NestedTargetSearch nestedTargets;

    private final ActionRenderer actionRenderer;

    private final SuccessionEndpointRenderer endpointRenderer;

    /**
     * Creates the renderer.
     *
     * @param declarations
     *            writes specializations, bodies and statement ends
     * @param dispatch
     *            writes the members of a simple transition
     * @param referenceResolver
     *            resolves declared and referenced names
     * @param referenceWriter
     *            writes expressions and references with traceability metadata
     * @param syntax
     *            syntax queries on elements
     * @param context
     *            the serialization context
     * @param actionRenderer
     *            provides the inline texts of trigger and effect actions
     */
    public TransitionRenderer(final DeclarationRenderer declarations,
            final ElementDispatch dispatch,
            final ReferenceResolver referenceResolver,
            final ExpressionAndReferenceWriter referenceWriter,
            final ElementSyntaxQueries syntax,
            final SerializationContext context,
            final ActionRenderer actionRenderer) {
        this.declarations = declarations;
        this.dispatch = dispatch;
        this.referenceResolver = referenceResolver;
        this.referenceWriter = referenceWriter;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.adapters = context.modelQueries().adapters();
        this.nestedTargets = context.modelQueries().nestedTargets();
        this.actionRenderer = actionRenderer;
        this.endpointRenderer = new SuccessionEndpointRenderer(referenceResolver, referenceWriter, syntax, context);
    }

    /**
     * Writes a transition statement: declaration, source, then either the default-target form {@code else target;} or
     * trigger, guard, effect and {@code then target;}. Callers check {@link #canSerializeTransitionUsage(Element)}
     * first.
     *
     * @param transitionUsage
     *            the transition usage
     */
    public void serializeTransitionUsage(final Element transitionUsage) {
        final TransitionParts parts = this.transitionParts(transitionUsage);
        final ParserFragment fragment = this.parserFragment(transitionUsage);
        final TextOutput output = this.context.output();
        this.writeTransitionDeclaration(transitionUsage, parts, fragment);
        this.writeTransitionSource(transitionUsage, parts);
        if (this.mustRenderDefaultTargetSuccession(transitionUsage, parts)) {
            output.token(SysMLTokens.ELSE_KEYWORD).space();
            this.referenceWriter.writeReferencePath(output, parts.target(), parts.targetElement());
        } else {
            this.writeTransitionTriggerGuardAndEffect(parts);
            output.space().token(SysMLTokens.THEN_KEYWORD);
            this.writeTransitionTarget(transitionUsage, parts, fragment);
        }
        this.declarations.serializeStatementEnd(transitionUsage);
    }

    /**
     * Tells whether the element is a transition this renderer can write as a transition statement: it needs a target
     * and either behavior (default-target branch, trigger or guard) or a declaration that the shorthand cannot omit (a
     * source in a state body, or a name with a source). Other transitions are written by the generic or
     * simple-transition paths.
     *
     * @param element
     *            the candidate element
     * @return {@code true} when {@link #serializeTransitionUsage(Element)} applies
     */
    public boolean canSerializeTransitionUsage(final Element element) {
        if (!(element instanceof TransitionUsage)) {
            return false;
        }
        final TransitionParts parts = this.transitionParts(element);
        final boolean behaviorSyntax = this.mustRenderDefaultTargetSuccession(element, parts)
                || !parts.trigger().isBlank() || !parts.guard().isBlank();
        final boolean declarationSyntax = this.mustRenderStateTransitionKeyword(element, parts.source())
                || !parts.name().isBlank() && !parts.source().isBlank();
        return !parts.target().isBlank() && (behaviorSyntax || declarationSyntax);
    }

    /**
     * Tells whether the element is an unnamed transition that only wraps a succession, as created for a plain
     * {@code then b;} in an action body.
     *
     * @param element
     *            the candidate element
     * @return {@code true} when {@link #serializeSimpleTransitionUsage(Element)} applies
     */
    public boolean canSerializeSimpleTransitionUsage(final Element element) {
        return element instanceof TransitionUsage
                && this.referenceResolver.declaredName(element).isBlank()
                && this.navigation.bodyRelationships(element).stream()
                        .flatMap(relationship -> this.navigation.ownedRelatedElements(relationship).stream())
                        .anyMatch(SuccessionAsUsage.class::isInstance);
    }

    /**
     * Writes a simple transition through its members (the succession writes {@code then b;}), unless the source used
     * the {@code transition} keyword, in which case the full transition statement is written.
     *
     * @param transitionUsage
     *            the transition usage
     */
    public void serializeSimpleTransitionUsage(final Element transitionUsage) {
        if (this.adapters.hasTextAdapter(transitionUsage, ITextAdapter.EXPLICIT_TRANSITION_KEYWORD)) {
            this.serializeTransitionUsage(transitionUsage);
            return;
        }
        for (final Relationship relationship : this.navigation.bodyRelationships(transitionUsage)) {
            this.dispatch.serializeRelationship(relationship);
        }
    }

    /**
     * Reads every clause of a transition, omitting the implicit decision source for both dispatch and output: name, source, trigger, guard, effect
     * and target, each with the element it is traced to.
     */
    private TransitionParts transitionParts(final Element transitionUsage) {
        final List<Element> transitionFeatures = this.transitionFeatureElements(transitionUsage).toList();
        final Element triggerElement = this.transitionTriggerElement(transitionFeatures);
        String trigger = "";
        if (triggerElement != null) {
            trigger = this.actionRenderer.acceptActionText(triggerElement);
        }
        final Element effectElement = this.transitionEffectElement(transitionFeatures);
        final TransitionParts parts = new TransitionParts(this.referenceResolver.declaredName(transitionUsage),
                this.transitionSource(transitionUsage), this.transitionSourceElement(transitionUsage),
                trigger, triggerElement,
                this.transitionGuard(transitionFeatures), this.firstGuardExpression(transitionFeatures),
                effectElement,
                this.transitionTarget(transitionUsage), this.transitionTargetElement(transitionUsage));
        if (this.shouldOmitImplicitActionSource(transitionUsage, parts)) {
            return parts.withoutSource();
        }
        return parts;
    }

    /** Identifies the standalone parser fragment, if any, whose grammar rule the transition is serialized for. */
    private ParserFragment parserFragment(final Element transitionUsage) {
        final SerializationOptions options = this.context.options();
        final boolean targetTransitionMember = SourceSyntax.isParserEntryChild(options, transitionUsage,
                ParserFragments.TARGET_TRANSITION_USAGE_MEMBER);
        final boolean targetTransitionRoot = SourceSyntax.isRootParserEntry(options, transitionUsage,
                ParserFragments.TARGET_TRANSITION_USAGE);
        final boolean guardedSuccession = SourceSyntax.isRootParserEntry(options, transitionUsage,
                ParserFragments.GUARDED_SUCCESSION)
                || SourceSyntax.isParserEntryChild(options, transitionUsage, ParserFragments.GUARDED_SUCCESSION_MEMBER);
        return new ParserFragment(targetTransitionMember, targetTransitionRoot, guardedSuccession);
    }

    /**
     * Writes the declaration prefix: {@code succession ...} for a guarded-succession fragment, {@code transition name}
     * for a target-transition member, {@code transition|succession name} with its {@code UsageDeclaration}
     * specializations for a named transition, and a bare
     * {@code transition} when the source wrote it or a state-body transition with a source needs it.
     */
    private void writeTransitionDeclaration(final Element transitionUsage, final TransitionParts parts,
            final ParserFragment fragment) {
        final TextOutput output = this.context.output();
        if (fragment.guardedSuccession()) {
            output.token(SysMLTokens.SUCCESSION_KEYWORD);
            this.declarations.serializeSpecializations(transitionUsage, true);
            output.space();
        } else if (fragment.targetTransitionMember()) {
            output.token(SysMLTokens.TRANSITION_KEYWORD).space().token(parts.name()).space();
        } else if (!parts.name().isBlank()) {
            output.token(this.transitionDeclarationKeyword(transitionUsage, parts)).space()
                    .token(parts.name());
            this.declarations.serializeSpecializations(transitionUsage, true);
            output.space();
        } else if (this.adapters.hasTextAdapter(transitionUsage, ITextAdapter.EXPLICIT_TRANSITION_KEYWORD)
                || this.mustRenderStateTransitionKeyword(transitionUsage, parts.source())) {
            output.token(SysMLTokens.TRANSITION_KEYWORD).space();
        }
    }

    /**
     * Writes the source state, preceded by {@code first} when required (see
     * {@link #shouldRenderTransitionFirstKeyword(Element)}), traced to the source element when it is resolved.
     */
    private void writeTransitionSource(final Element transitionUsage, final TransitionParts parts) {
        if (parts.source().isBlank()) {
            return;
        }
        final TextOutput output = this.context.output();
        if (this.shouldRenderTransitionFirstKeyword(transitionUsage)) {
            output.token(SysMLTokens.FIRST_KEYWORD).space();
        }
        if (parts.sourceElement() == null) {
            this.referenceWriter.writeReferenceText(output, parts.source(), transitionUsage);
        } else {
            this.referenceWriter.writeReferencePath(output, parts.source(), parts.sourceElement());
        }
        output.space();
    }

    /**
     * Tells whether {@code first} precedes the source. When the source used the {@code transition} keyword, the parser
     * marker says whether {@code first} was omitted; otherwise {@code first} is only elided for unnamed state-body
     * transitions under reference keyword elision.
     */
    private boolean shouldRenderTransitionFirstKeyword(final Element transitionUsage) {
        if (this.adapters.hasTextAdapter(transitionUsage, ITextAdapter.EXPLICIT_TRANSITION_KEYWORD)) {
            return !this.adapters.hasTextAdapter(transitionUsage, ITextAdapter.TRANSITION_FIRST_KEYWORD_OMITTED);
        }
        return !this.context.options().implicitReferenceKeywordElision()
                || !this.isStateBodyTransition(transitionUsage)
                || !this.referenceResolver.declaredName(transitionUsage).isBlank();
    }

    /**
     * Writes the {@code accept ...} trigger, {@code if guard} and {@code do effect} clauses that are present, in
     * grammar order.
     */
    private void writeTransitionTriggerGuardAndEffect(final TransitionParts parts) {
        final TextOutput output = this.context.output();
        if (!parts.trigger().isBlank()) {
            this.referenceWriter.writeReferenceText(output, parts.trigger(), parts.triggerElement());
            this.declarations.serializeTrailingComments(parts.triggerElement());
        }
        if (!parts.guard().isBlank()) {
            output.space().token(SysMLTokens.IF_KEYWORD).space();
            this.referenceWriter.writeExpression(output, parts.guardExpression());
        }
        if (parts.effectElement() != null) {
            output.space().token(SysMLTokens.DO_KEYWORD).space();
            this.serializeEffectBehaviorUsage(parts.effectElement());
        }
    }

    /**
     * Writes the target after {@code then}. Parser succession fragments write the target end with its multiplicity (and
     * {@code name ::> ref} for a target-transition root); otherwise the target path is written, traced to the target
     * element when it is resolved.
     */
    private void writeTransitionTarget(final Element transitionUsage, final TransitionParts parts,
            final ParserFragment fragment) {
        final TextOutput output = this.context.output();
        final Element targetElement = parts.targetElement();
        if (targetElement == null) {
            this.referenceWriter.writeReferenceText(output.space(), parts.target(), transitionUsage);
        } else if (fragment.isSuccessionFragment()) {
            this.endpointRenderer.writeMultiplicity(targetElement);
            output.space();
            if (fragment.targetTransitionRoot()) {
                this.writeParserTargetTransitionTarget(targetElement);
            } else {
                this.endpointRenderer.write(targetElement);
            }
        } else {
            this.referenceWriter.writeReferencePath(output.space(), parts.target(), targetElement);
        }
    }

    /**
     * Writes the target end of a target-transition fragment as {@code name ::> reference}, or whichever part is
     * present.
     */
    private void writeParserTargetTransitionTarget(final Element targetElement) {
        final TextOutput output = this.context.output();
        final String name = this.referenceResolver.declaredName(targetElement);
        final String reference = this.referenceResolver.firstDirectRelationshipTarget(
                targetElement, SysMLPackage.eINSTANCE.getReferenceSubsetting());
        output.token(name);
        if (!reference.isBlank()) {
            if (!name.isBlank()) {
                output.space().token(SysMLTokens.REFERENCE_SUBSETTING_OPERATOR).space();
            }
            this.referenceWriter.writeReferencePath(output, reference,
                    this.referenceResolver.firstDirectRelationshipTargetElement(targetElement,
                            SysMLPackage.eINSTANCE.getReferenceSubsetting()));
        }
    }

    /**
     * Returns the keyword of a named transition: {@code succession} when the source wrote it or when the transition is
     * an action-body succession inferred structurally, otherwise {@code transition}.
     */
    private String transitionDeclarationKeyword(final Element transitionUsage, final TransitionParts parts) {
        String keyword = SysMLTokens.TRANSITION_KEYWORD;
        // shortcut: infer action-body successions structurally, remove when the model distinguishes named transitions.
        if (this.adapters.hasTextAdapter(transitionUsage, ITextAdapter.SUCCESSION_KEYWORD)
                || this.isImplicitActionBodySuccession(transitionUsage, parts)) {
            keyword = SysMLTokens.SUCCESSION_KEYWORD;
        }
        return keyword;
    }

    /**
     * Tells whether a transition looks like an action-body succession: owned by an action, with only a source, and with
     * an input parameter.
     */
    private boolean isImplicitActionBodySuccession(final Element transitionUsage, final TransitionParts parts) {
        final boolean sourceOnly = !parts.source().isBlank() && parts.trigger().isBlank()
                && parts.effectElement() == null;
        return sourceOnly && this.isActionBodyTransition(transitionUsage)
                && this.hasTransitionInputParameter(transitionUsage);
    }

    /** Tells whether a state-body transition with a source must be introduced by {@code transition}. */
    private boolean mustRenderStateTransitionKeyword(final Element transitionUsage, final String source) {
        return this.isStateBodyTransition(transitionUsage) && !source.isBlank();
    }

    /**
     * Tells whether the transition is written as the default branch {@code else target;}
     * ({@code DefaultTargetSuccession}): an action-target or default-target succession fragment without behavior, a
     * parser marker for the default form, or an inferred default branch (see
     * {@link #isImplicitDefaultTargetSuccession(Element, TransitionParts)}).
     */
    private boolean mustRenderDefaultTargetSuccession(final Element transitionUsage, final TransitionParts parts) {
        return !parts.hasBehavior() && (this.isParserTargetSuccession(transitionUsage)
                || this.adapters.hasTextAdapter(transitionUsage, ITextAdapter.DEFAULT_TARGET_SUCCESSION)
                || this.isImplicitDefaultTargetSuccession(transitionUsage, parts));
    }

    /** Tells whether the transition is the root of an action-target or default-target succession fragment. */
    private boolean isParserTargetSuccession(final Element transitionUsage) {
        return SourceSyntax.isRootParserEntry(this.context.options(), transitionUsage,
                ParserFragments.ACTION_TARGET_SUCCESSION)
                || SourceSyntax.isRootParserEntry(this.context.options(), transitionUsage,
                        ParserFragments.DEFAULT_TARGET_SUCCESSION);
    }

    /**
     * Infers a default {@code else} branch from structure when no marker exists: a target-only transition with just its
     * succession, placed right after a guarded transition.
     */
    private boolean isImplicitDefaultTargetSuccession(final Element transitionUsage, final TransitionParts parts) {
        // shortcut: infer target-only default branches, remove when the model identifies a decision node's default branch.
        return parts.isTargetOnly()
                && this.transitionFeatureElements(transitionUsage).findAny().isEmpty()
                && this.transitionSuccessionUsages(transitionUsage).count() == 1
                && this.followsGuardedTransition(transitionUsage);
    }

    /**
     * Tells whether the previous non-comment member of the owner is a guarded transition, as in a decision's
     * {@code if g then a; else b;}.
     */
    private boolean followsGuardedTransition(final Element transitionUsage) {
        final Relationship previous = this.previousSemanticSibling(transitionUsage);
        return previous != null && previous.getOwnedRelatedElement().stream()
                .anyMatch(sibling -> sibling instanceof TransitionUsage
                        && !this.transitionGuard(this.transitionFeatureElements(sibling).toList()).isBlank());
    }

    /**
     * Returns the owning relationship of the previous non-comment member of the transition's owner, or {@code null}.
     */
    private Relationship previousSemanticSibling(final Element transitionUsage) {
        final Relationship owningRelationship = transitionUsage.getOwningRelationship();
        Relationship previous = null;
        if (owningRelationship != null && owningRelationship.getOwningRelatedElement() != null) {
            final List<Relationship> siblings = this.navigation
                    .ownedRelationships(owningRelationship.getOwningRelatedElement());
            for (int index = siblings.indexOf(owningRelationship) - 1; index >= 0 && previous == null; index--) {
                final Relationship candidate = siblings.get(index);
                if (this.navigation.ownedRelatedElements(candidate).stream()
                        .anyMatch(sibling -> !(sibling instanceof Comment))) {
                    previous = candidate;
                }
            }
        }
        return previous;
    }

    /** Tells whether the transition is a member of an action definition or usage. */
    private boolean isActionBodyTransition(final Element transitionUsage) {
        final Element owner = this.transitionOwner(transitionUsage);
        return this.navigation.isMetaclass(owner, SysMLPackage.eINSTANCE.getActionDefinition())
                || this.navigation.isMetaclass(owner, SysMLPackage.eINSTANCE.getActionUsage());
    }

    /** Tells whether the transition is a member of a state definition, state usage or exhibit state usage. */
    private boolean isStateBodyTransition(final Element transitionUsage) {
        final Element owner = this.transitionOwner(transitionUsage);
        return this.navigation.isMetaclass(owner, SysMLPackage.eINSTANCE.getStateDefinition())
                || this.navigation.isMetaclass(owner, SysMLPackage.eINSTANCE.getStateUsage())
                || this.navigation.isMetaclass(owner, SysMLPackage.eINSTANCE.getExhibitStateUsage());
    }

    /** Returns the element owning the transition's membership, or {@code null} for a detached transition. */
    private Element transitionOwner(final Element transitionUsage) {
        Element owner = null;
        if (transitionUsage.eContainer() instanceof final Relationship membership
                && membership.eContainer() instanceof final Element membershipOwner) {
            owner = membershipOwner;
        }
        return owner;
    }

    /** Tells whether the transition owns a reference parameter, which action-body successions carry. */
    private boolean hasTransitionInputParameter(final Element transitionUsage) {
        return this.navigation.ownedRelationships(transitionUsage).stream()
                .filter(relationship -> this.navigation.isMetaclass(relationship,
                        SysMLPackage.eINSTANCE.getParameterMembership()))
                .flatMap(relationship -> this.navigation.ownedRelatedElements(relationship).stream())
                .anyMatch(ReferenceUsage.class::isInstance);
    }

    /**
     * Returns the source text ({@code TransitionSourceMember}): the parser spelling of the source membership while it
     * still designates the source, else (outside round-trip mode, with reference keyword elision) the resolved source
     * name, else the first feature chain owned by the transition. Empty when there is no source.
     */
    private String transitionSource(final Element transitionUsage) {
        String source = this.navigation.ownedRelationships(transitionUsage).stream()
                .filter(this::isTransitionSourceMembership)
                .map(relationship -> this.context.modelQueries().referenceTargets().currentParsedTargetName(relationship,
                        this.context.danglingReferenceIndex()))
                .filter(name -> !name.isBlank())
                .findFirst().map(this.referenceResolver::referenceName).orElse("");
        if (source.isBlank()
                && this.context.options().mode() != SerializationMode.ROUND_TRIP
                && this.context.options().implicitReferenceKeywordElision()) {
            source = this.navigation.ownedRelationships(transitionUsage).stream()
                    .filter(this::isTransitionSourceMembership)
                    .flatMap(relationship -> StoredModel.targets(relationship).stream())
                    .findFirst().map(this.context.nameRenderer()::referenceName)
                    .orElse("");
        }
        if (source.isBlank()) {
            source = this.transitionSourceFromOwnedFeatureChain(transitionUsage);
        }
        return source;
    }

    /** Returns the resolved source element of the transition, or {@code null}. */
    private Element transitionSourceElement(final Element transitionUsage) {
        return this.navigation.ownedRelationships(transitionUsage).stream()
                .filter(this::isTransitionSourceMembership)
                .flatMap(relationship -> StoredModel.targets(relationship).stream())
                .findFirst().orElse(null);
    }

    /** Tells whether the relationship is the plain membership that references the transition source. */
    private boolean isTransitionSourceMembership(final Relationship relationship) {
        return this.navigation.isMetaclass(relationship, SysMLPackage.eINSTANCE.getMembership());
    }

    /**
     * Tells whether the source is implied and must be omitted: an unnamed action-body transition leaving a decision
     * node, written {@code if g then a;} inside the decision's successions ({@code GuardedTargetSuccession}).
     */
    private boolean shouldOmitImplicitActionSource(final Element transitionUsage, final TransitionParts parts) {
        return this.context.options().implicitReferenceKeywordElision()
                && parts.name().isBlank()
                && this.isActionBodyTransition(transitionUsage)
                && this.navigation.isMetaclass(parts.sourceElement(), SysMLPackage.eINSTANCE.getDecisionNode());
    }

    /** Returns the first feature chain owned by the transition, used as the source when it is written as a chain. */
    private String transitionSourceFromOwnedFeatureChain(final Element transitionUsage) {
        return this.navigation.ownedRelationships(transitionUsage).stream()
                .filter(relationship -> this.navigation.isMetaclass(relationship,
                        SysMLPackage.eINSTANCE.getOwningMembership()))
                .flatMap(relationship -> this.navigation.ownedRelatedElements(relationship).stream())
                .filter(Feature.class::isInstance)
                .map(feature -> String.join(SysMLTokens.PATH_SEPARATOR,
                        this.nestedTargets.directFeatureChainNames(feature,
                                this.context.danglingReferenceIndex())))
                .filter(text -> !text.isBlank()).findFirst()
                .map(this.referenceResolver::referenceName).orElse("");
    }

    /** Returns the text of the first guard expression with text, or empty. */
    private String transitionGuard(final List<Element> transitionFeatures) {
        return this.guardExpressions(transitionFeatures).map(this.referenceWriter::expressionText)
                .filter(text -> !text.isBlank()).findFirst().orElse("");
    }

    /** Returns the first guard expression, kept to trace the written guard. */
    private Expression firstGuardExpression(final List<Element> transitionFeatures) {
        return this.guardExpressions(transitionFeatures).findFirst().orElse(null);
    }

    /** Returns the expressions owned through {@code if} (guard) transition feature memberships, in source order. */
    private Stream<Expression> guardExpressions(final List<Element> transitionFeatures) {
        return transitionFeatures.stream()
                .filter(element -> isTransitionFeatureOfKind(element, TransitionFeatureKind.GUARD))
                .filter(Expression.class::isInstance)
                .map(Expression.class::cast);
    }

    /** Returns the first accept action among the trigger features, or {@code null}. */
    private Element transitionTriggerElement(final List<Element> transitionFeatures) {
        return transitionFeatures.stream()
                .filter(element -> isTransitionFeatureOfKind(element, TransitionFeatureKind.TRIGGER))
                .filter(element -> this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getAcceptActionUsage()))
                .findFirst().orElse(null);
    }

    /** Returns the first effect action that is a perform or has an inline text, or {@code null}. */
    private Element transitionEffectElement(final List<Element> transitionFeatures) {
        return transitionFeatures.stream()
                .filter(element -> isTransitionFeatureOfKind(element, TransitionFeatureKind.EFFECT))
                .filter(element -> this.isPerformEffect(element) || !this.effectActionText(element).isBlank())
                .findFirst().orElse(null);
    }

    private boolean isPerformEffect(final Element element) {
        return this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getPerformActionUsage());
    }

    /**
     * Tells which transition clause (trigger, guard or effect) a feature belongs to. The membership kind is the only
     * reliable source: an accept action, for instance, can be either the trigger or the {@code do} effect.
     */
    private static boolean isTransitionFeatureOfKind(final Element feature, final TransitionFeatureKind kind) {
        return feature.getOwningRelationship() instanceof final TransitionFeatureMembership membership
                && membership.getKind() == kind;
    }

    /** Returns the trigger, guard and effect features of the transition, in source order. */
    private Stream<Element> transitionFeatureElements(final Element transitionUsage) {
        return this.navigation.ownedRelationships(transitionUsage).stream()
                .filter(TransitionFeatureMembership.class::isInstance)
                .flatMap(relationship -> this.navigation.ownedRelatedElements(relationship).stream());
    }

    /**
     * Writes the effect after {@code do} (grammar rule {@code EffectBehaviorUsage}), for a transition or a standalone
     * {@code EffectBehaviorMember}. A perform effect is written as its {@code PerformActionUsageDeclaration} with its
     * optional body; a send, accept or assignment effect as its inline text followed by its body members. Nothing
     * is written for any other element.
     *
     * @param effectElement
     *            the effect action
     */
    public void serializeEffectBehaviorUsage(final Element effectElement) {
        if (this.isPerformEffect(effectElement)) {
            this.actionRenderer.serializePerformedPerformAction(effectElement, false);
            return;
        }
        this.referenceWriter.writeReferenceText(this.context.output(), this.effectActionText(effectElement),
                effectElement);
        if (this.actionRenderer.hasInlineActionBody(effectElement)) {
            this.actionRenderer.serializeInlineActionBody(effectElement);
        }
    }

    /**
     * Returns the inline text of a send, accept or assignment effect, with its {@code action name} declaration, as for
     * a state subaction. Empty for anything else, including {@code null}.
     */
    private String effectActionText(final Element element) {
        String effect = "";
        if (this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getSendActionUsage())
                || this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getAcceptActionUsage())
                || element instanceof AssignmentActionUsage) {
            effect = this.actionRenderer.stateSubactionText(element);
        }
        return effect;
    }

    /** Returns the target path of the first owned succession with a target, or empty. */
    private String transitionTarget(final Element transitionUsage) {
        return this.transitionSuccessionUsages(transitionUsage)
                .map(this::successionTarget).filter(target -> !target.isBlank())
                .findFirst().orElse("");
    }

    /** Returns the target end of the first owned succession with two ends, or {@code null}. */
    private Element transitionTargetElement(final Element transitionUsage) {
        return this.transitionSuccessionUsages(transitionUsage)
                .map(this.navigation::referenceEnds).filter(ends -> ends.size() >= 2)
                .map(ends -> ends.get(1)).findFirst().orElse(null);
    }

    /** Returns the successions owned by the transition, which carry its target. */
    private Stream<SuccessionAsUsage> transitionSuccessionUsages(final Element transitionUsage) {
        return this.navigation.ownedRelationships(transitionUsage).stream()
                .flatMap(relationship -> this.navigation.ownedRelatedElements(relationship).stream())
                .filter(SuccessionAsUsage.class::isInstance)
                .map(SuccessionAsUsage.class::cast);
    }

    /** Returns the endpoint path of the succession's second end, or empty when it has fewer than two ends. */
    private String successionTarget(final SuccessionAsUsage successionAsUsage) {
        final List<Element> ends = this.navigation.referenceEnds(successionAsUsage);
        String target = "";
        if (ends.size() >= 2) {
            target = this.referenceResolver.endpointPath(ends.get(1));
        }
        return target;
    }

    /**
     * The clauses of a transition ({@code TransitionUsage} grammar), each text paired with the element it is traced
     * to.
     */
    private record TransitionParts(String name, String source, Element sourceElement,
            String trigger, Element triggerElement, String guard, Expression guardExpression,
            Element effectElement, String target, Element targetElement) {

        /** Returns the same parts with an implied source, which is then not written. */
        TransitionParts withoutSource() {
            return new TransitionParts(this.name, "", this.sourceElement, this.trigger, this.triggerElement,
                    this.guard, this.guardExpression, this.effectElement, this.target,
                    this.targetElement);
        }

        boolean hasBehavior() {
            return !this.trigger.isBlank() || !this.guard.isBlank() || this.effectElement != null;
        }

        /** Tells whether only a target is present: no name, source, trigger, guard or effect. */
        boolean isTargetOnly() {
            return this.name.isBlank() && this.source.isBlank() && !this.hasBehavior() && !this.target.isBlank();
        }
    }

    /**
     * The standalone parser fragment a transition is serialized for: a {@code TargetTransitionUsage} member or root, or
     * a {@code GuardedSuccession}. Their grammars spell the declaration and target differently.
     */
    private record ParserFragment(boolean targetTransitionMember, boolean targetTransitionRoot,
            boolean guardedSuccession) {

        /** Tells whether the target is written as a succession end, with its multiplicity. */
        boolean isSuccessionFragment() {
            return this.guardedSuccession || this.targetTransitionMember || this.targetTransitionRoot;
        }
    }
}
