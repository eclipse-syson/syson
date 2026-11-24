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
package org.eclipse.syson.sysml.serializer.specialization;

import java.util.List;

import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.SerializationMode;
import org.eclipse.syson.sysml.serializer.expression.ExpressionRenderer;
import org.eclipse.syson.sysml.serializer.model.ElementSyntaxQueries;
import org.eclipse.syson.sysml.serializer.model.ModelFlags;
import org.eclipse.syson.sysml.serializer.model.MultiplicityText;
import org.eclipse.syson.sysml.serializer.model.ParserAdapterQueries;
import org.eclipse.syson.sysml.serializer.model.SpecializationTargetNames;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.specialization.SpecializationClausePlanner.SpecializationClause;
import org.eclipse.syson.sysml.serializer.specialization.SpecializationFacts.ClauseKind;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;

/**
 * Writes the specialization part of a feature or definition declaration ({@code featureSpecializationPart} and
 * {@code subclassificationPart}): typing, subclassification, subsetting, redefinition, reference subsetting, cross
 * subsetting, multiplicity and the {@code ordered}/{@code nonunique} modifiers.
 * <p>
 * The grammar accepts these clauses in any order, with the {@code multiplicityPart} either first or after at least one
 * specialization. {@link #serializeSpecializations(Element, boolean, boolean)} writes them in source order, grouped
 * into clauses by {@link SpecializationClausePlanner}; individual clauses are formatted by
 * {@link SpecializationClauseRenderer}.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class SpecializationRenderer {

    private final ExpressionRenderer expressions;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final SerializationContext context;

    private final ModelFlags flags;

    private final ParserAdapterQueries adapters;

    private final MultiplicityText multiplicities;

    private final SpecializationTargetNames specializationTargets;

    private final SpecializationClausePlanner clausePlanner;

    private final SpecializationClauseRenderer clauseRenderer;

    private final SpecializationFactsCollector factsCollector;

    /**
     * Creates a renderer writing to the output of the given context.
     *
     * @param referenceResolver
     *            resolves the target names written in clauses
     * @param expressions
     *            renders multiplicity bounds and other expressions
     * @param referenceWriter
     *            writes reference text with HTML target metadata
     * @param syntax
     *            syntax queries used to decide when a multiplicity is rendered elsewhere (in a body)
     * @param context
     *            the serialization context whose output receives the clauses
     */
    public SpecializationRenderer(final ReferenceResolver referenceResolver,
            final ExpressionRenderer expressions,
            final ExpressionAndReferenceWriter referenceWriter,
            final ElementSyntaxQueries syntax,
            final SerializationContext context) {
        this.expressions = expressions;
        this.referenceWriter = referenceWriter;
        this.context = context;
        this.flags = context.modelQueries().flags();
        this.adapters = context.modelQueries().adapters();
        this.multiplicities = context.modelQueries().multiplicities();
        this.specializationTargets = context.modelQueries().specializationTargets();
        this.clausePlanner = new SpecializationClausePlanner(this.adapters);
        this.clauseRenderer = new SpecializationClauseRenderer(referenceResolver, expressions, referenceWriter,
                context);
        this.factsCollector = new SpecializationFactsCollector(expressions, syntax, context);
    }

    /**
     * Writes every specialization clause of {@code element}, including reference and cross subsetting.
     *
     * @param element
     *            the element whose declaration is being written
     * @param includeMultiplicity
     *            whether multiplicity and {@code ordered} belong to this suffix; callers pass {@code false} when they
     *            write them at another position of the declaration
     * @see #serializeSpecializations(Element, boolean, boolean)
     */
    public void serializeSpecializations(final Element element, final boolean includeMultiplicity) {
        this.serializeSpecializations(element, includeMultiplicity, true);
    }

    /**
     * Writes the specialization suffix of {@code element} after its name, in source order.
     * <p>
     * The grammar accepts the {@code featureSpecialization} clauses in any order, so they are written in the order of
     * the element's explicit relationships, which is the source order for parsed text: the reparsed model then keeps
     * the same relationship order. Consecutive targets of one kind share a clause as the source did (see
     * {@link SpecializationClausePlanner}). The multiplicity is written at its own position, followed by the
     * {@code ordered}/{@code nonunique} modifiers of the same {@code multiplicityPart}. Each clause is written with a
     * leading space; nothing is written when the element has no specialization, multiplicity or late modifier.
     * </p>
     *
     * @param element
     *            the element whose declaration is being written
     * @param includeMultiplicity
     *            whether multiplicity and {@code ordered} belong to this suffix
     * @param includeReferenceSpecializations
     *            whether {@code ::>} / {@code =>} clauses belong to this suffix; callers such as {@code frame} and
     *            {@code render} memberships pass {@code false} because the reference target is already written as the
     *            membership's own reference
     */
    public void serializeSpecializations(final Element element,
            final boolean includeMultiplicity,
            final boolean includeReferenceSpecializations) {
        final SpecializationFacts facts = this.factsCollector.collect(element);
        final List<SpecializationClause> clauses = this.clausePlanner.clauses(element, facts.items());
        final int modifierSlot = this.clausePlanner.modifierSlot(element, clauses);
        if (modifierSlot < 0) {
            this.writeOrderedAndLateNonUnique(element, includeMultiplicity);
        }
        ClauseKind previousKind = null;
        for (int index = 0; index < clauses.size(); index++) {
            final SpecializationClause clause = clauses.get(index);
            if (clause.kind() == ClauseKind.MULTIPLICITY) {
                if (includeMultiplicity) {
                    this.writeMultiplicity(element, facts, previousKind == ClauseKind.REDEFINITION);
                }
            } else if (includeReferenceSpecializations || !clause.kind().singleTarget()) {
                this.clauseRenderer.writeClause(element, clause, facts.rawMultiplicity());
                previousKind = clause.kind();
            }
            if (index == modifierSlot) {
                this.writeOrderedAndLateNonUnique(element, includeMultiplicity);
            }
        }
    }

    /**
     * Writes the specialization of a declaration whose prefix already consumed part of it, such as a metadata body
     * usage that starts with the redefined feature it binds ({@code ref annotatedElement :> x : T;}). Only the first
     * non-empty clause family is written, in the priority crossed ({@code =>}), referenced ({@code ::>}), subsetted
     * ({@code :>}), redefined ({@code :>>}); the typing follows. Multiplicity and modifiers are left to the caller.
     *
     * @param element
     *            the metadata body usage
     * @param redefinedFeatures
     *            the redefinitions not already written by the caller as the usage prefix
     * @param referencedFeatures
     *            the reference subsetting targets
     */
    public void serializePrefixedSpecializations(final Element element,
            final List<String> redefinedFeatures,
            final List<String> referencedFeatures) {
        final List<String> subsettedFeatures = this.specializationTargets.subsettedFeatureNames(element,
                this.context.danglingReferenceIndex(), this.context.nameRenderer()::referenceName);
        final List<String> crossedFeatures = this.specializationTargets.crossedFeatureNames(element,
                this.context.danglingReferenceIndex(), this.context.nameRenderer()::referenceName);
        if (!crossedFeatures.isEmpty()) {
            this.clauseRenderer.writeCrossSubsettingSymbol(element, crossedFeatures);
        } else if (!referencedFeatures.isEmpty()) {
            this.clauseRenderer.writeReferenceSubsettingSymbol(element, referencedFeatures);
        } else if (!subsettedFeatures.isEmpty()) {
            this.clauseRenderer.writeSubsettingSymbol(element, subsettedFeatures);
        } else if (!redefinedFeatures.isEmpty()) {
            final List<List<String>> groups = this.specializationTargets.redefinedFeatureNameGroups(element,
                    this.context.danglingReferenceIndex(), this.context.nameRenderer()::referenceName)
                    .stream().filter(redefinedFeatures::containsAll).toList();
            this.clauseRenderer.writeRedefinition(this.context.output(), element, redefinedFeatures, groups,
                    this.multiplicities.multiplicityText(element, this.expressions::expressionText));
        }
        final List<String> featureTypes = this.specializationTargets.featureTypeNames(element,
                this.context.danglingReferenceIndex(), this.context.nameRenderer()::referenceName);
        if (!featureTypes.isEmpty()) {
            this.clauseRenderer.writeTyping(element, featureTypes);
        }
    }

    /**
     * Returns normalized redefinition text ({@code :>> a :>> b}) for callers that embed it in a larger reference text,
     * such as compact action syntax. Source keyword spellings are not reproduced.
     *
     * @param redefinedFeatures
     *            the redefined feature names, in order
     * @return one {@code :>>} clause per name separated by spaces, or an empty string for an empty list
     */
    public String redefinitionText(final List<String> redefinedFeatures) {
        return this.clauseRenderer.redefinitionText(redefinedFeatures);
    }

    /**
     * Writes the redefinition clause of {@code element} for the given targets, reproducing the source keyword or symbol
     * and clause grouping, with a leading space.
     *
     * @param output
     *            the output receiving the clause
     * @param element
     *            the redefining element
     * @param redefinedFeatures
     *            the redefined feature names to write
     */
    public void writeRedefinition(final TextOutput output, final Element element,
            final List<String> redefinedFeatures) {
        this.clauseRenderer.writeRedefinition(output, element, redefinedFeatures);
    }

    /**
     * Writes an early {@code nonunique} right after the declared name, as in {@code part p nonunique : P}, when the
     * element has no multiplicity and the parser did not mark the modifier as late. Complements
     * {@link #serializeLateNonUnique(Element)}: at most one of the two writes the keyword.
     *
     * @param element
     *            the declared feature
     */
    public void serializeSuffixModifiers(final Element element) {
        if (this.flags.isNonUnique(element)
                && !this.adapters.hasTextAdapter(element, ITextAdapter.LATE_NONUNIQUE)
                && this.multiplicities.multiplicityText(element, this.expressions::expressionText).isBlank()) {
            this.context.output().space().token(SysMLTokens.NONUNIQUE_KEYWORD);
        }
    }

    /**
     * Writes {@code nonunique} at its late position (after the multiplicity or specialization clauses) when the element
     * is non-unique and either has a multiplicity or was marked late by the parser. Complements
     * {@link #serializeSuffixModifiers(Element)}.
     *
     * @param element
     *            the declared feature
     */
    public void serializeLateNonUnique(final Element element) {
        if (this.flags.isNonUnique(element)
                && (this.adapters.hasTextAdapter(element, ITextAdapter.LATE_NONUNIQUE)
                        || !this.multiplicities.multiplicityText(element, this.expressions::expressionText)
                                .isBlank())) {
            this.context.output().space().token(SysMLTokens.NONUNIQUE_KEYWORD);
        }
    }

    /**
     * Writes the facts' multiplicity, when there is one to emit. Normalized output separates it with a space
     * ({@code : T [1]}); other modes attach it to the preceding token, the conventional {@code : T[1]} spelling, except
     * after a redefinition ({@code :>> p [1]}).
     */
    private void writeMultiplicity(final Element element, final SpecializationFacts facts,
            final boolean afterRedefinition) {
        if (!facts.multiplicity().isBlank()) {
            final TextOutput output = this.context.output();
            if (afterRedefinition || this.context.options().mode() == SerializationMode.NORMALIZED) {
                output.space();
            }
            this.referenceWriter.writeReferenceText(output, facts.multiplicity(), element);
        }
    }

    /**
     * Writes {@code ordered} and a late {@code nonunique} in the order the source used: {@code nonunique ordered} by
     * default, {@code ordered nonunique} when the parser marked it. {@code ordered} belongs to the multiplicity part,
     * so it is written only when the caller includes the multiplicity.
     */
    private void writeOrderedAndLateNonUnique(final Element element, final boolean includeMultiplicity) {
        final boolean ordered = includeMultiplicity && this.flags.isOrdered(element);
        final boolean nonUniqueAfterOrdered = ordered
                && this.adapters.hasTextAdapter(element, ITextAdapter.NONUNIQUE_AFTER_ORDERED);
        if (!nonUniqueAfterOrdered) {
            this.serializeLateNonUnique(element);
        }
        if (ordered) {
            this.context.output().space().token(SysMLTokens.ORDERED_KEYWORD);
        }
        if (nonUniqueAfterOrdered) {
            this.serializeLateNonUnique(element);
        }
    }
}
