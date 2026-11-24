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

import java.util.Objects;

import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.model.NestedTargetSearch;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.ReferenceSubsetting;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Renders include use case usages.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class IncludeUseCaseRenderer {

    private final DeclarationRenderer declarations;

    private final ReferenceResolver referenceResolver;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final SerializationContext context;

    private final NestedTargetSearch nestedTargets;

    /**
     * Creates rendering for anonymous referenced includes and declared {@code include use case} usages.
     *
     * @param declarations
     *            writes occurrence prefixes, declaration clauses and body delimiters
     * @param referenceResolver
     *            supplies declared names and the referenced use-case name
     * @param referenceWriter
     *            emits included use-case targets with their reference metadata
     * @param context
     *            supplies the run's output, options and nested-reference queries
     */
    public IncludeUseCaseRenderer(final DeclarationRenderer declarations,
            final ReferenceResolver referenceResolver,
            final ExpressionAndReferenceWriter referenceWriter,
            final SerializationContext context) {
        this.declarations = declarations;
        this.referenceResolver = referenceResolver;
        this.referenceWriter = referenceWriter;
        this.context = context;
        this.nestedTargets = context.modelQueries().nestedTargets();
    }

    /**
     * Writes an {@code include} usage in one of its two grammar forms: {@code include target;} for an anonymous usage that
     * references an existing use case, or {@code include use case name ...;} for a declared one.
     */
    public void serializeIncludeUseCaseUsage(final Element includeUseCaseUsage) {
        this.writePrefix(includeUseCaseUsage);
        final IncludeUseCasePlan plan = this.createPlan(includeUseCaseUsage);
        if (plan.anonymousReference()) {
            this.writeReference(includeUseCaseUsage, plan);
        } else {
            this.writeDeclaration(includeUseCaseUsage, plan.name());
        }
        this.declarations.renderBodyOrStatementEnd(includeUseCaseUsage);
    }

    /**
     * Writes the {@code occurrenceUsagePrefix} of the grammar rule {@code includeUseCaseUsage}, then {@code include}: direction,
     * prefix modifiers, the optional {@code ref}, {@code individual}, the portion kind and user keywords.
     */
    private void writePrefix(final Element includeUseCaseUsage) {
        this.declarations.serializeOccurrenceUsagePrefix(includeUseCaseUsage);
        this.declarations.serializeUserKeywords(includeUseCaseUsage);
        this.context.output().token(SysMLTokens.INCLUDE_KEYWORD);
    }

    /**
     * Chooses the referenced use case of an anonymous include: a feature chain when implicit reference keywords may be
     * elided, otherwise the reference subsetting target as a reference name (it may need qualification to resolve from
     * the include), then the target's declared name, falling back to the chain.
     */
    private IncludeUseCasePlan createPlan(final Element includeUseCaseUsage) {
        final String name = this.referenceResolver.declaredName(includeUseCaseUsage);
        final String chainedTarget = this.nestedTargets.firstChainedFeatureName(includeUseCaseUsage,
                ReferenceSubsetting.class, this.context.danglingReferenceIndex());
        String target = this.referenceResolver.firstDirectRelationshipTarget(
                includeUseCaseUsage,
                SysMLPackage.eINSTANCE.getReferenceSubsetting());
        final Element targetElement = this.referenceResolver.firstDirectRelationshipTargetElement(includeUseCaseUsage,
                SysMLPackage.eINSTANCE.getReferenceSubsetting());
        if (name.isBlank() && this.context.options().implicitReferenceKeywordElision()) {
            if (!chainedTarget.isBlank()) {
                target = chainedTarget;
            } else if (targetElement != null && target.isBlank()) {
                target = this.referenceResolver.declaredName(targetElement);
            }
        }
        if (target.isBlank()) {
            target = chainedTarget;
        }
        return new IncludeUseCasePlan(name, target,
                Objects.requireNonNullElse(targetElement, includeUseCaseUsage),
                name.isBlank() && !target.isBlank());
    }

    /**
     * Writes the {@code include target featureSpecializationPart [= value]} form that subsets an existing use case; the
     * reference subsetting is the target itself, so only the other specializations and the multiplicity follow it.
     */
    private void writeReference(final Element includeUseCaseUsage,
            final IncludeUseCasePlan plan) {
        this.referenceWriter.writeReferenceText(this.context.output().space(), plan.target(),
                plan.referenceContext());
        this.declarations.serializeSpecializations(includeUseCaseUsage, true, false);
        this.declarations.serializeFeatureValues(includeUseCaseUsage);
    }

    /** Writes the {@code include use case name specializations [= value]} form that declares the included use case. */
    private void writeDeclaration(final Element includeUseCaseUsage,
            final String name) {
        this.context.output().space().token(SysMLTokens.USE_KEYWORD).space()
                .token(SysMLTokens.CASE_KEYWORD);
        this.declarations.renderOptionalName(name);
        this.declarations.serializeSpecializations(includeUseCaseUsage, true);
        this.declarations.serializeFeatureValues(includeUseCaseUsage);
    }

    /** Values collected before rendering an included use case. */
    private record IncludeUseCasePlan(String name, String target,
            Element referenceContext, boolean anonymousReference) {
    }
}
