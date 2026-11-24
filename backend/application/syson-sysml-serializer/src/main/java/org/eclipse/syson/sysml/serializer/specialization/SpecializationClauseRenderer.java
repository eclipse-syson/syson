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
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.expression.ExpressionRenderer;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.model.MultiplicityText;
import org.eclipse.syson.sysml.serializer.model.ParserAdapterQueries;
import org.eclipse.syson.sysml.serializer.model.SpecializationTargetNames;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.specialization.SpecializationClausePlanner.SpecializationClause;
import org.eclipse.syson.sysml.serializer.specialization.SpecializationFacts.SourceItem;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.FeatureTyping;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Formats and writes one specialization clause at a time (typing, subclassification, subsetting, redefinition, reference
 * and cross subsetting) for {@link SpecializationRenderer}, which decides their order.
 * <p>
 * Each clause reproduces the source spelling recorded by parser markers: keyword or symbol ({@code subsets} vs
 * {@code :>}, {@code redefines} vs {@code :>>}, {@code defined by} vs {@code :}, {@code references} vs {@code ::>},
 * {@code crosses} vs {@code =>}) and the split of targets into repeated clauses ({@code :> a :> b} vs {@code :> a, b}).
 * Without markers the normalized symbol form is used. Every write starts with a space. The {@code ...Symbol} writers
 * always use the symbol, for declaration forms whose caller does not keep the source spelling.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
final class SpecializationClauseRenderer {

    private static final String CROSSED_FEATURE = SysMLPackage.Literals.CROSS_SUBSETTING__CROSSED_FEATURE.getName();

    private static final String GENERAL = SysMLPackage.Literals.SPECIALIZATION__GENERAL.getName();

    private static final String REDEFINED_FEATURE = SysMLPackage.Literals.REDEFINITION__REDEFINED_FEATURE.getName();

    private static final String REFERENCED_FEATURE = SysMLPackage.Literals.REFERENCE_SUBSETTING__REFERENCED_FEATURE
            .getName();

    private static final String SUBSETTED_FEATURE = SysMLPackage.Literals.SUBSETTING__SUBSETTED_FEATURE.getName();

    private static final String SUPERCLASSIFIER = SysMLPackage.Literals.SUBCLASSIFICATION__SUPERCLASSIFIER.getName();

    private final ReferenceResolver referenceResolver;

    private final ExpressionRenderer expressions;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ParserAdapterQueries adapters;

    private final MultiplicityText multiplicities;

    private final SpecializationTargetNames specializationTargets;

    /**
     * Creates a clause renderer writing to the output of the given context.
     *
     * @param referenceResolver
     *            resolves the target names written in clauses
     * @param expressions
     *            renders multiplicity bounds, used to strip multiplicity text from redefinition names
     * @param referenceWriter
     *            writes reference text with HTML target metadata
     * @param context
     *            the serialization context whose output receives the clauses
     */
    SpecializationClauseRenderer(final ReferenceResolver referenceResolver,
            final ExpressionRenderer expressions,
            final ExpressionAndReferenceWriter referenceWriter,
            final SerializationContext context) {
        this.referenceResolver = referenceResolver;
        this.expressions = expressions;
        this.referenceWriter = referenceWriter;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.adapters = context.modelQueries().adapters();
        this.multiplicities = context.modelQueries().multiplicities();
        this.specializationTargets = context.modelQueries().specializationTargets();
    }

    /** Writes {@code :> targets}, linking only the subsetted features. */
    void writeSubsettingSymbol(final Element element, final List<String> subsettedFeatures) {
        this.writeLinkedClause(SysMLTokens.SUBSETTING_OPERATOR,
                this.referenceResolver.referenceNames(subsettedFeatures), element,
                SysMLPackage.eINSTANCE.getSubsetting(), SUBSETTED_FEATURE);
    }

    /** Writes one {@code ::> target} clause per referenced feature, linking them; the grammar takes one target each. */
    void writeReferenceSubsettingSymbol(final Element element, final List<String> referencedFeatures) {
        for (final String target : referencedFeatures) {
            this.writeLinkedClause(SysMLTokens.REFERENCE_SUBSETTING_OPERATOR, this.referenceResolver.referenceName(target),
                    element, SysMLPackage.eINSTANCE.getReferenceSubsetting(), REFERENCED_FEATURE);
        }
    }

    /** Writes one {@code => target} clause per crossed feature, linking them; the grammar takes one target each. */
    void writeCrossSubsettingSymbol(final Element element, final List<String> crossedFeatures) {
        for (final String target : crossedFeatures) {
            this.writeLinkedClause(SysMLTokens.CROSSING_OPERATOR, this.referenceResolver.referenceName(target),
                    element, SysMLPackage.eINSTANCE.getCrossSubsetting(), CROSSED_FEATURE);
        }
    }

    /** Writes {@code operator targetText}, linking the targets of {@code element}'s relationships of one metaclass. */
    private void writeLinkedClause(final String operator, final String targetText, final Element element,
            final EClass relationshipClass, final String... targetReferenceNames) {
        final TextOutput output = this.context.output().space().token(operator).space();
        this.referenceWriter.writeRelationshipReferences(output, targetText, element, relationshipClass,
                targetReferenceNames);
    }

    /**
     * Returns normalized redefinition text ({@code :>> a :>> b}), one clause per name, ignoring source keyword spelling
     * and grouping. Used where a redefinition is embedded in another reference text, such as compact action syntax.
     *
     * @param redefinedFeatures
     *            the redefined feature names, in order
     * @return the clauses separated by spaces, or an empty string for an empty list
     */
    String redefinitionText(final List<String> redefinedFeatures) {
        return this.redefinitionClauses(SysMLTokens.REDEFINITION_OPERATOR,
                redefinedFeatures.stream().map(List::of).toList());
    }

    /**
     * Writes the redefinition clause of {@code element} for the given targets, using the element's own source grouping
     * and multiplicity to keep the source spelling.
     */
    void writeRedefinition(final TextOutput output, final Element element, final List<String> redefinedFeatures) {
        this.writeRedefinition(output, element, redefinedFeatures,
                this.specializationTargets.redefinedFeatureNameGroups(element,
                        this.context.danglingReferenceIndex(),
                        this.context.nameRenderer()::referenceName),
                this.multiplicities.multiplicityText(element, this.expressions::expressionText));
    }

    /**
     * Writes the redefinition clause, reproducing {@code redefines a, b}, {@code :>> a :>> b} or {@code :>> a, b} as in
     * the source. A multiplicity suffix that leaked into a captured target name (e.g. {@code :>> p[1]}) is stripped
     * because the multiplicity is written by the caller.
     *
     * @param redefinedFeatures
     *            the redefined feature names, flattened in source order
     * @param groups
     *            the same names grouped per source {@code Redefinition} relationship
     * @param multiplicity
     *            the element's raw multiplicity text, used only to strip it from target names
     */
    void writeRedefinition(final TextOutput output, final Element element, final List<String> redefinedFeatures,
            final List<List<String>> groups, final String multiplicity) {
        output.space();
        this.referenceWriter.writeRelationshipReferences(output,
                this.redefinitionClauseText(element, redefinedFeatures, groups, multiplicity), element,
                SysMLPackage.eINSTANCE.getRedefinition(), REDEFINED_FEATURE, GENERAL);
    }

    /**
     * Writes one clause planned by {@link SpecializationClausePlanner}: its operator, as keyword or symbol, then its
     * targets separated by {@code , }. Redefinition targets lose a multiplicity suffix the parser may have captured
     * with them, since the multiplicity is written at its own position.
     *
     * @param element
     *            the declared element owning the clause relationships
     * @param clause
     *            the clause to write; never a multiplicity clause
     * @param rawMultiplicity
     *            the element's raw multiplicity text, used only to strip it from redefinition targets
     */
    void writeClause(final Element element, final SpecializationClause clause, final String rawMultiplicity) {
        final List<String> names = clause.names();
        switch (clause.kind()) {
            case TYPING -> this.writeTypingClause(clause, names);
            case SUBCLASSIFICATION -> this.writeLinkedClause(
                    spelling(clause, SysMLTokens.SPECIALIZES_KEYWORD, SysMLTokens.SUBSETTING_OPERATOR),
                    this.referenceResolver.referenceNames(names), element,
                    SysMLPackage.eINSTANCE.getSubclassification(), SUPERCLASSIFIER, GENERAL);
            case SUBSETTING -> this.writeLinkedClause(
                    spelling(clause, SysMLTokens.SUBSETS_KEYWORD, SysMLTokens.SUBSETTING_OPERATOR),
                    this.referenceResolver.referenceNames(names), element,
                    SysMLPackage.eINSTANCE.getSubsetting(), SUBSETTED_FEATURE, GENERAL);
            case REDEFINITION -> this.writeLinkedClause(
                    spelling(clause, SysMLTokens.REDEFINES_KEYWORD, SysMLTokens.REDEFINITION_OPERATOR),
                    this.referenceResolver.referenceNames(this.withoutMultiplicitySuffix(names, rawMultiplicity)),
                    element, SysMLPackage.eINSTANCE.getRedefinition(), REDEFINED_FEATURE, GENERAL);
            case REFERENCE_SUBSETTING -> this.writeUnlinkedClause(
                    spelling(clause, SysMLTokens.REFERENCES_KEYWORD, SysMLTokens.REFERENCE_SUBSETTING_OPERATOR),
                    names.get(0), element);
            case CROSS_SUBSETTING -> this.writeUnlinkedClause(
                    spelling(clause, SysMLTokens.CROSSES_KEYWORD, SysMLTokens.CROSSING_OPERATOR),
                    names.get(0), element);
            default -> {
                // MULTIPLICITY is written by SpecializationRenderer at its own position, so it is never passed here.
            }
        }
    }

    private static String spelling(final SpecializationClause clause, final String keyword, final String symbol) {
        if (clause.keyword()) {
            return keyword;
        }
        return symbol;
    }

    /** Writes {@code : types} or {@code defined by types}, linking the types of the clause's typings. */
    private void writeTypingClause(final SpecializationClause clause, final List<String> featureTypes) {
        final TextOutput output = this.context.output().space();
        if (clause.keyword()) {
            output.token(SysMLTokens.DEFINED_KEYWORD).space().token(SysMLTokens.BY_KEYWORD).space();
        } else {
            output.token(SysMLTokens.TYPE_SEPARATOR).space();
        }
        this.referenceWriter.writeTypingReferences(output, this.referenceResolver.referenceNames(featureTypes),
                clause.items().stream().map(SourceItem::relationship).toList());
    }

    /** Writes {@code operator target}, linking the target only through the expression-reference occurrences. */
    private void writeUnlinkedClause(final String operator, final String target, final Element element) {
        final TextOutput output = this.context.output().space().token(operator).space();
        this.referenceWriter.writeReferenceText(output, this.referenceResolver.referenceName(target), element);
    }

    /**
     * Writes the {@code typings} clause. When the parser marked typing clause starts, the source split is reproduced
     * ({@code : A, B : C} vs {@code : A, B, C}, mixing {@code defined by} as written); otherwise one {@code :} or
     * {@code defined by} clause lists all types. Callers only call it for typed elements.
     *
     * @param featureTypes
     *            the type names, in source order
     */
    void writeTyping(final Element element, final List<String> featureTypes) {
        final TextOutput output = this.context.output();
        final List<Relationship> typings = this.navigation.ownedRelationships(element)
                .stream().filter(FeatureTyping.class::isInstance).toList();
        if (this.hasTypingClauseMarkers(typings, featureTypes)) {
            this.referenceWriter.writeTypingReferences(output, this.capturedTypingText(typings, featureTypes),
                    element);
        } else {
            if (this.adapters.hasTextAdapter(element, ITextAdapter.DEFINED_BY)) {
                output.space().token(SysMLTokens.DEFINED_KEYWORD).space().token(SysMLTokens.BY_KEYWORD).space();
            } else {
                output.space().token(SysMLTokens.TYPE_SEPARATOR).space();
            }
            this.referenceWriter.writeTypingReferences(output, this.referenceResolver.referenceNames(featureTypes),
                    element);
        }
    }

    /**
     * Tells whether the captured typing clause markers can be trusted: one name per typing relationship and at least
     * one clause-start marker.
     */
    private boolean hasTypingClauseMarkers(final List<Relationship> typings, final List<String> featureTypes) {
        return typings.size() == featureTypes.size()
                && typings.stream().anyMatch(typing -> this.adapters.hasTextAdapter(typing,
                        ITextAdapter.TYPING_CLAUSE_START));
    }

    /**
     * Rebuilds the typing text clause by clause: a typing marked as a clause start opens a new {@code :} or
     * {@code defined by} clause, any other continues the current one after {@code , }.
     */
    private String capturedTypingText(final List<Relationship> typings, final List<String> featureTypes) {
        final StringBuilder text = new StringBuilder();
        for (int index = 0; index < typings.size(); index++) {
            final Relationship typing = typings.get(index);
            if (index > 0 && !this.adapters.hasTextAdapter(typing, ITextAdapter.TYPING_CLAUSE_START)) {
                text.append(SysMLTokens.ARGUMENT_SEPARATOR);
            } else if (this.adapters.hasTextAdapter(typing, ITextAdapter.DEFINED_BY)) {
                text.append(SysMLTokens.DEFINED_BY_INFIX);
            } else {
                text.append(SysMLTokens.TYPE_SEPARATOR_INFIX);
            }
            text.append(this.referenceResolver.referenceName(featureTypes.get(index)));
        }
        return text.toString();
    }

    /** Counts the occurrences of a parser marker on the element, i.e. how many clauses the source spelled that way. */
    private static long markerCount(final Element element, final ITextAdapter marker) {
        return element.eAdapters().stream().filter(adapter -> adapter == marker).count();
    }

    /**
     * Builds the full redefinition clause text, operator included. With a {@code redefines} marker, repeated markers
     * give one clause per source group, otherwise one comma list; the symbol form uses one {@code :>>} clause per group
     * (or per name when groups are unknown) unless {@link #mergesRedefinitionGroups(Element, List)}.
     */
    private String redefinitionClauseText(final Element element, final List<String> redefinedFeatures,
            final List<List<String>> groups, final String multiplicity) {
        final String text;
        if (this.adapters.hasTextAdapter(element, ITextAdapter.REDEFINES)) {
            final List<String> names = this.withoutMultiplicitySuffix(redefinedFeatures, multiplicity);
            if (markerCount(element, ITextAdapter.REDEFINES) > 1 && flattened(groups).equals(names)) {
                text = this.redefinitionClauses(SysMLTokens.REDEFINES_KEYWORD, groups);
            } else {
                text = this.redefinitionClauses(SysMLTokens.REDEFINES_KEYWORD, List.of(names));
            }
        } else if (groups.isEmpty()) {
            text = this.redefinitionText(this.withoutMultiplicitySuffix(redefinedFeatures, multiplicity));
        } else {
            final List<List<String>> strippedGroups = groups.stream()
                    .map(group -> this.withoutMultiplicitySuffix(group, multiplicity)).toList();
            if (this.mergesRedefinitionGroups(element, strippedGroups)) {
                text = this.redefinitionClauses(SysMLTokens.REDEFINITION_OPERATOR, List.of(flattened(strippedGroups)));
            } else {
                text = this.redefinitionClauses(SysMLTokens.REDEFINITION_OPERATOR, strippedGroups);
            }
        }
        return text;
    }

    /**
     * Tells whether single-target redefinitions share the one symbol clause recorded by the parser. Qualified names
     * and end-feature flags do not identify source clause boundaries.
     */
    private boolean mergesRedefinitionGroups(final Element element, final List<List<String>> groups) {
        return groups.size() > 1
                && groups.stream().allMatch(group -> group.size() == 1)
                && markerCount(element, ITextAdapter.REDEFINES_SYMBOL) == 1;
    }

    /** Formats one {@code operator a, b} clause per group, the clauses separated by spaces. */
    private String redefinitionClauses(final String operator, final List<List<String>> groups) {
        return groups.stream()
                .map(group -> operator + SysMLTokens.SPACE + this.referenceResolver.referenceNames(group))
                .collect(Collectors.joining(SysMLTokens.SPACE));
    }

    private static List<String> flattened(final List<List<String>> groups) {
        return groups.stream().flatMap(List::stream).toList();
    }

    private List<String> withoutMultiplicitySuffix(final List<String> names, final String multiplicity) {
        if (multiplicity.isBlank()) {
            return names;
        }
        return names.stream().map(name -> this.withoutMultiplicitySuffix(name, multiplicity)).toList();
    }

    /**
     * Strips from a captured target name the multiplicity and {@code ordered}/{@code nonunique} text that the parser
     * may have kept with it, since the caller writes them itself. The given multiplicity is removed when it ends the
     * name; otherwise the last bracket pair is removed. Surrounding whitespace is trimmed.
     */
    private String withoutMultiplicitySuffix(final String name, final String multiplicity) {
        int end = endBeforeTrailingWhitespace(name, name.length());
        final int start = firstNonWhitespaceIndex(name, end);
        end = endBeforeTrailingModifier(name, start, end);
        final int multiplicityStart = end - multiplicity.length();
        if (multiplicityStart >= start && name.regionMatches(multiplicityStart, multiplicity, 0, multiplicity.length())) {
            end = endBeforeTrailingWhitespace(name, multiplicityStart);
        } else {
            end = endBeforeTrailingWhitespace(name, endBeforeTrailingBracketExpression(name, start, end));
        }
        end = endBeforeTrailingModifier(name, start, end);
        return name.substring(start, end);
    }

    private static int firstNonWhitespaceIndex(final String text, final int end) {
        int start = 0;
        while (start < end && Character.isWhitespace(text.charAt(start))) {
            start++;
        }
        return start;
    }

    private static int endBeforeTrailingWhitespace(final String text, final int end) {
        int trimmedEnd = end;
        while (trimmedEnd > 0 && Character.isWhitespace(text.charAt(trimmedEnd - 1))) {
            trimmedEnd--;
        }
        return trimmedEnd;
    }

    /**
     * Returns the end index before one trailing {@code ordered} or {@code nonunique} word and the whitespace before it,
     * or {@code end} when there is none.
     */
    private static int endBeforeTrailingModifier(final String text, final int start, final int end) {
        int newEnd = endBeforeTrailingWord(text, start, end, SysMLTokens.ORDERED_KEYWORD);
        if (newEnd == end) {
            newEnd = endBeforeTrailingWord(text, start, end, SysMLTokens.NONUNIQUE_KEYWORD);
        }
        return newEnd;
    }

    /** Returns the end index before {@code word} when it ends the text as a whole word, or {@code end} otherwise. */
    private static int endBeforeTrailingWord(final String text, final int start, final int end, final String word) {
        final int wordStart = end - word.length();
        final boolean endsWithWord = wordStart >= start
                && text.regionMatches(wordStart, word, 0, word.length())
                && (wordStart == start || Character.isWhitespace(text.charAt(wordStart - 1)));
        if (endsWithWord) {
            return endBeforeTrailingWhitespace(text, wordStart);
        }
        return end;
    }

    /** Returns the end index before a trailing {@code [...]} expression, or {@code end} when the text has none. */
    private static int endBeforeTrailingBracketExpression(final String text, final int start, final int end) {
        int result = end;
        if (end > start && text.charAt(end - 1) == ']') {
            final int bracketStart = text.lastIndexOf('[', end - 2);
            if (bracketStart >= start) {
                result = bracketStart;
            }
        }
        return result;
    }
}
