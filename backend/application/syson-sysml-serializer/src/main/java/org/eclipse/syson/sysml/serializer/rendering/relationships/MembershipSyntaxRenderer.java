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
import java.util.Objects;

import org.eclipse.syson.sysml.serializer.ElementDispatch;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.model.ElementSyntaxQueries;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.model.ModelReference;
import org.eclipse.syson.sysml.serializer.model.NestedTargetSearch;
import org.eclipse.syson.sysml.serializer.model.ReferenceTargets;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.rendering.actions.ActionRenderer;
import org.eclipse.syson.sysml.serializer.rendering.declarations.DeclarationRenderer;
import org.eclipse.syson.sysml.serializer.syntax.SourceSyntax;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.EnumerationUsage;
import org.omg.sysml.lang.sysml.Expression;
import org.omg.sysml.lang.sysml.PerformActionUsage;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.StateSubactionMembership;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Writes keyword-introduced memberships ({@code subject}, {@code actor}, {@code stakeholder}, {@code objective},
 * {@code frame}, {@code variant}, {@code filter}, {@code render}, {@code return}, state subactions) and annotations.
 * Each form has a declaration variant ({@code keyword declaration}) and, where the grammar allows it, a reference
 * variant ({@code keyword target;}) for a member that only references an existing feature.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class MembershipSyntaxRenderer {

    private static final String ANNOTATED_ELEMENT_REFERENCE = "annotatedElement";

    private final ActionRenderer actionRenderer;

    private final DeclarationRenderer declarations;

    private final ElementDispatch dispatch;

    private final ReferenceResolver referenceResolver;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final ElementSyntaxQueries syntax;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ReferenceTargets referenceTargets;

    private final NestedTargetSearch nestedTargets;

    /**
     * Creates the renderer.
     *
     * @param actionRenderer
     *            writes the compact text of state subactions
     * @param declarations
     *            writes comments, visibility, specializations and bodies
     * @param dispatch
     *            writes the declarations of owned members
     * @param referenceResolver
     *            resolves names, endpoint paths and referenced targets
     * @param referenceWriter
     *            writes references and expressions with traceability metadata
     * @param syntax
     *            detects generated members and explicit bodies
     * @param context
     *            the serialization context
     */
    public MembershipSyntaxRenderer(final ActionRenderer actionRenderer,
            final DeclarationRenderer declarations,
            final ElementDispatch dispatch,
            final ReferenceResolver referenceResolver,
            final ExpressionAndReferenceWriter referenceWriter,
            final ElementSyntaxQueries syntax,
            final SerializationContext context) {
        this.actionRenderer = actionRenderer;
        this.declarations = declarations;
        this.dispatch = dispatch;
        this.referenceResolver = referenceResolver;
        this.referenceWriter = referenceWriter;
        this.syntax = syntax;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.referenceTargets = context.modelQueries().referenceTargets();
        this.nestedTargets = context.modelQueries().nestedTargets();
    }

    /**
     * Writes an annotation that owns its annotating element. A single unnamed, bodiless metadata usage becomes the
     * {@code #Type} prefix form; other owned metadata is dispatched as a full declaration. Otherwise only the first
     * annotated element is written, as a reference path line.
     *
     * @param annotation
     *            the annotation relationship
     */
    void serializeAnnotation(final Relationship annotation) {
        final List<Element> annotatingMetadata = this.navigation.ownedRelatedElements(annotation).stream()
                .filter(element -> this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getMetadataUsage()))
                .toList();
        if (annotatingMetadata.size() == 1) {
            this.serializeAnnotatingMetadata(annotatingMetadata.get(0));
            return;
        }
        final List<ModelReference> annotatedElements = this.referenceTargets.referenceTargets(annotation,
                ANNOTATED_ELEMENT_REFERENCE, this.context.danglingReferenceIndex(),
                this.context.nameRenderer()::referenceName);
        if (!annotatedElements.isEmpty()) {
            final ModelReference annotatedElement = annotatedElements.get(0);
            this.referenceWriter.writeReferencePath(this.context.output(),
                    this.referenceResolver.referenceName(annotatedElement.name()), annotatedElement.element())
                    .newline();
        }
    }

    /** Writes {@code #Type} for an unnamed, bodiless, typed metadata usage, otherwise its full declaration. */
    private void serializeAnnotatingMetadata(final Element metadataUsage) {
        final String metadataType = this.syntax.metadataTypeName(metadataUsage);
        final boolean prefixForm = !this.referenceResolver.hasIdentification(metadataUsage)
                && !metadataType.isBlank()
                && this.navigation.bodyRelationships(metadataUsage).isEmpty();
        if (prefixForm) {
            final TextOutput output = this.context.output().token(SysMLTokens.METADATA_KEYWORD_PREFIX);
            this.referenceWriter.writeReferenceText(output, metadataType, metadataUsage).newline();
        } else {
            this.dispatch.serializeElement(metadataUsage);
        }
    }

    /**
     * Writes a {@code frame} member (grammar {@code framedConcernMember}): {@code frame target ...;} when the concern
     * references an existing one, otherwise {@code frame} followed by the full concern declaration.
     *
     * @param membership
     *            the framed concern membership
     */
    void serializeFramedConcernMembership(final Relationship membership) {
        final Element framedConcern = this.navigation.ownedRelatedElements(membership).stream()
                .findFirst().orElse(null);
        String target = "";
        if (framedConcern != null) {
            target = this.referenceResolver.directReferencedElementTarget(framedConcern);
        }
        if (target.isBlank()) {
            this.declarations.serializePrefixedMembership(SysMLTokens.FRAME_KEYWORD, membership);
            return;
        }
        this.declarations.serializeLeadingComments(membership);
        final TextOutput output = this.context.output().token(SysMLTokens.FRAME_KEYWORD).space();
        final Element targetElement = this.referenceResolver.directReferencedElementTargetElement(framedConcern);
        if (targetElement != null) {
            this.referenceWriter.writeReferencePath(output, target, targetElement);
        } else {
            this.referenceWriter.writeReferenceText(output, target, framedConcern);
        }
        this.declarations.serializeSpecializations(framedConcern, true, false);
        this.writeBlockBodyOrTerminator(framedConcern);
    }

    /**
     * Writes an {@code entry}, {@code do} or {@code exit} state subaction (grammar {@code entryActionMember} and
     * siblings). Model-built perform actions with a body get the full {@code action ...} declaration; other perform
     * actions get their {@code PerformActionUsageDeclaration} and regular action body; send, accept and assignment
     * subactions are written inline. The empty entry placeholder is written as {@code entry ;}, matching the spelling
     * of the reference corpus. The notes of the membership surround the subaction.
     *
     * @param membership
     *            the state subaction membership
     */
    void serializeStateSubactionMembership(final StateSubactionMembership membership) {
        this.declarations.serializeLeadingComments(membership);
        this.writeStateSubaction(membership);
        this.declarations.serializeTrailingComments(membership);
    }

    /** Writes the {@code entry}, {@code do} or {@code exit} subaction of a state subaction membership, then ends the line. */
    private void writeStateSubaction(final StateSubactionMembership membership) {
        final TextOutput output = this.context.output();
        this.declarations.serializeMembershipVisibility(membership);
        output.token(membership.getKind().getName());
        final Element action = this.navigation.ownedRelatedElements(membership).stream().findFirst().orElse(null);
        if (action == null) {
            output.token(SysMLTokens.STATEMENT_TERMINATOR).newline();
        } else if (this.navigation.isEmptyStateEntryAction(action)) {
            output.space().token(SysMLTokens.STATEMENT_TERMINATOR).newline();
        } else if (this.requiresFullActionDeclaration(action)) {
            output.space();
            this.declarations.serializeMembershipFeature(SysMLTokens.ACTION_KEYWORD, action);
        } else if (this.navigation.isMetaclass(action, SysMLPackage.eINSTANCE.getPerformActionUsage())) {
            output.space();
            this.actionRenderer.serializePerformedPerformAction(action, true);
        } else {
            final String actionText = this.actionRenderer.stateSubactionText(action);
            this.referenceWriter.writeReferenceText(output.space(), actionText, action);
            if (this.actionRenderer.hasInlineActionBody(action)) {
                this.actionRenderer.serializeInlineActionBody(action);
            } else {
                output.token(SysMLTokens.STATEMENT_TERMINATOR);
            }
            output.newline();
        }
    }

    /** Tells whether a subaction must use the full declaration: a model-built perform action that has a block body. */
    private boolean requiresFullActionDeclaration(final Element action) {
        return action instanceof PerformActionUsage && !SourceSyntax.hasTextAdapter(action)
                && this.syntax.shouldSerializeBlockBody(action);
    }

    /**
     * Writes {@code return} followed by the dispatched result parameter declaration (grammar
     * {@code returnParameterMember}); generated members are skipped.
     *
     * @param membership
     *            the return parameter membership
     */
    void serializeReturnParameterMembership(final Relationship membership) {
        this.declarations.serializeLeadingComments(membership);
        this.declarations.serializeMembershipVisibility(membership);
        this.context.output().token(SysMLTokens.RETURN_KEYWORD).space();
        this.navigation.ownedRelatedElements(membership).stream()
                .filter(related -> !this.syntax.isAnonymousGeneratedElement(related))
                .forEach(this.dispatch::serializeElement);
    }

    /**
     * Writes a keyword-introduced case feature ({@code subject}, {@code actor}, {@code stakeholder},
     * {@code objective}). A declared member is written as {@code keyword declaration}; a generated member that only
     * references another feature becomes <code>keyword ::&gt; target;</code>, or {@code keyword;} when no target is
     * known.
     *
     * @param keyword
     *            the membership keyword
     * @param membership
     *            the case feature membership
     */
    void serializeCaseFeatureMembership(final String keyword, final Relationship membership) {
        this.declarations.serializeLeadingComments(membership);
        final List<Element> relatedElements = this.navigation.ownedRelatedElements(membership);
        if (relatedElements.stream().allMatch(this.syntax::isAnonymousGeneratedElement)) {
            this.serializeReferencedCaseFeature(keyword, membership, relatedElements);
        } else {
            for (final Element related : relatedElements) {
                if (!this.syntax.isAnonymousGeneratedElement(related)) {
                    this.declarations.serializeMembershipVisibility(membership);
                    this.declarations.serializeMembershipFeature(keyword, related);
                }
            }
        }
        this.declarations.serializeTrailingComments(membership);
    }

    /**
     * Writes the reference form of a case feature, taking the target from the generated member's endpoint path or,
     * when unresolved, from the first parser spelling in the membership subtree.
     */
    private void serializeReferencedCaseFeature(final String keyword, final Relationship membership,
            final List<Element> relatedElements) {
        this.declarations.serializeMembershipVisibility(membership);
        final TextOutput output = this.context.output();
        final String target = relatedElements.stream()
                .map(this.referenceResolver::endpointPath)
                .filter(path -> !path.isBlank())
                .findFirst()
                .orElseGet(() -> this.nestedTargets.firstDanglingTarget(membership,
                        this.context.danglingReferenceIndex()));
        if (target.isBlank()) {
            output.token(keyword).token(SysMLTokens.STATEMENT_TERMINATOR).newline();
            return;
        }
        final Element referenceContext = relatedElements.stream()
                .filter(related -> !this.referenceResolver.endpointPath(related).isBlank())
                .map(this.referenceResolver::directReferencedElementTargetElement)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseGet(() -> Objects.requireNonNullElse(
                        this.referenceTargets.firstRelationshipTargetElement(membership), membership));
        output.token(keyword).space().token(SysMLTokens.REFERENCE_SUBSETTING_OPERATOR).space();
        this.referenceWriter.writeReferenceText(output, target, referenceContext)
                .token(SysMLTokens.STATEMENT_TERMINATOR).newline();
    }

    /**
     * Writes a variant member (grammar {@code variantUsageMember}). Enumeration values are dispatched without
     * {@code variant}, since {@code enum def} bodies declare them directly; declared variants get {@code variant}
     * before their declaration; referenced variants become {@code variant target;}.
     *
     * @param membership
     *            the variant membership
     */
    void serializeVariantMembership(final Relationship membership) {
        final List<Element> relatedElements = this.navigation.ownedRelatedElements(membership);
        if (relatedElements.stream().anyMatch(EnumerationUsage.class::isInstance)) {
            this.declarations.serializeLeadingComments(membership);
            relatedElements.forEach(this.dispatch::serializeElement);
        } else if (relatedElements.size() == 1 && this.isReferencedVariant(relatedElements.get(0))) {
            this.declarations.serializeLeadingComments(membership);
            this.serializeReferencedVariant(membership, relatedElements);
        } else if (relatedElements.stream().anyMatch(related -> !this.syntax.isAnonymousGeneratedElement(related))) {
            this.declarations.serializePrefixedMembership(SysMLTokens.VARIANT_KEYWORD, membership);
        } else {
            this.declarations.serializeLeadingComments(membership);
            this.serializeReferencedVariant(membership, relatedElements);
        }
    }

    /** A variant reference may have a body, but it cannot declare its own identification or value. */
    private boolean isReferencedVariant(final Element element) {
        final boolean identified = !this.referenceResolver.declaredName(element).isBlank()
                || !this.context.nameRenderer().declaredShortName(element).isBlank();
        return this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getReferenceUsage()) && !identified
                && this.navigation.featureValues(element).isEmpty() && !this.referenceSubsettingTarget(element).isBlank();
    }

    /**
     * Writes {@code variant target} with an optional body for a variant that only references an existing usage.
     * Writes nothing when no target is known.
     */
    private void serializeReferencedVariant(final Relationship membership, final List<Element> relatedElements) {
        final Element referencedVariant = relatedElements.stream()
                .filter(related -> this.navigation.isMetaclass(related, SysMLPackage.eINSTANCE.getReferenceUsage()))
                .findFirst().orElse(null);
        final String target;
        Element referenceContext = membership;
        if (referencedVariant == null) {
            target = this.syntax.referencedMembershipTarget(membership);
        } else {
            target = this.referenceSubsettingTarget(referencedVariant);
            referenceContext = Objects.requireNonNullElse(this.referenceResolver.firstDirectRelationshipTargetElement(
                    referencedVariant, SysMLPackage.eINSTANCE.getReferenceSubsetting()), membership);
        }
        if (target.isBlank()) {
            return;
        }
        this.declarations.serializeMembershipVisibility(membership);
        final TextOutput output = this.context.output().token(SysMLTokens.VARIANT_KEYWORD).space();
        this.referenceWriter.writeReferenceText(output, target, referenceContext);
        if (referencedVariant == null) {
            output.token(SysMLTokens.STATEMENT_TERMINATOR).newline();
        } else {
            this.declarations.serializeSpecializations(referencedVariant, true, false);
            this.writeBlockBodyOrTerminator(referencedVariant);
        }
    }

    /**
     * Writes {@code filter condition;} (grammar {@code elementFilterMember}) for the first owned expression of a
     * package or view filter membership.
     *
     * @param membership
     *            the element filter membership
     */
    void serializeElementFilterMembership(final Relationship membership) {
        this.declarations.serializeLeadingComments(membership);
        this.navigation.ownedRelatedElements(membership).stream()
                .filter(Expression.class::isInstance)
                .map(Expression.class::cast)
                .findFirst()
                .ifPresent(condition -> {
                    this.declarations.serializeMembershipVisibility(membership);
                    final TextOutput output = this.context.output().token(SysMLTokens.FILTER_KEYWORD).space();
                    this.referenceWriter.writeExpression(output, condition)
                            .token(SysMLTokens.STATEMENT_TERMINATOR).newline();
                });
    }

    /**
     * Writes a {@code render} member of a view (grammar {@code viewRenderingMember}): {@code render} followed by the
     * declaration of the rendering, or {@code render target ...} for an anonymous rendering that references one by
     * name or by feature chain. A rendering is never dropped: without a target it keeps its declaration form.
     *
     * @param membership
     *            the view rendering membership
     */
    void serializeViewRenderingMembership(final Relationship membership) {
        this.declarations.serializeLeadingComments(membership);
        final Element rendering = this.navigation.ownedRelatedElements(membership).stream()
                .filter(related -> this.navigation.isMetaclass(related, SysMLPackage.eINSTANCE.getRenderingUsage()))
                .findFirst().orElse(null);
        if (rendering == null) {
            return;
        }
        this.declarations.serializeMembershipVisibility(membership);
        final TextOutput output = this.context.output().token(SysMLTokens.RENDER_KEYWORD).space();
        final String target = this.referenceResolver.directReferencedElementTarget(rendering);
        if (target.isBlank() || !this.referenceResolver.declaredName(rendering).isBlank()) {
            this.dispatch.serializeElement(rendering);
        } else {
            this.referenceWriter.writeReferenceText(output, target, rendering);
            this.declarations.serializeSpecializations(rendering, true, false);
            this.declarations.renderBodyOrStatementEnd(rendering);
        }
    }

    /** Ends a reference-form member with its explicit block body, or with {@code ;}. */
    private void writeBlockBodyOrTerminator(final Element member) {
        final TextOutput output = this.context.output();
        if (this.syntax.shouldSerializeBlockBody(member)) {
            this.declarations.serializeBody(member);
            output.newline();
        } else {
            output.token(SysMLTokens.STATEMENT_TERMINATOR).newline();
        }
    }

    private String referenceSubsettingTarget(final Element element) {
        return this.referenceResolver.directReferencedElementTarget(element);
    }
}
