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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.model.ParserAdapterQueries;
import org.eclipse.syson.sysml.serializer.specialization.SpecializationFacts.ClauseKind;
import org.eclipse.syson.sysml.serializer.specialization.SpecializationFacts.SourceItem;
import org.omg.sysml.lang.sysml.Element;

/**
 * Splits the source-ordered specialization items of an element into the clauses of {@code featureSpecializationPart}
 * / {@code subclassificationPart}, and finds where its {@code multiplicityPart} modifiers go.
 * <p>
 * Consecutive items of one kind form one comma clause ({@code :> a, b}), except reference and cross subsettings whose
 * grammar rules take a single target. The parser records the keyword or symbol of each non-typing clause as an
 * element marker (attached in reverse source order): the i-th source marker of a kind gives the spelling of the i-th
 * clause of that kind, and the number of markers tells how many clauses the source wrote, so {@code :> a :> b} is not
 * merged into {@code :> a, b}. Typing clauses carry their own clause-start and {@code defined by} markers. Without markers (model
 * built or edited outside the parser) each run is one clause written with the symbol.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
final class SpecializationClausePlanner {

    private final ParserAdapterQueries adapters;

    /**
     * Creates a planner.
     *
     * @param adapters
     *            parser markers recording the source spelling
     */
    SpecializationClausePlanner(final ParserAdapterQueries adapters) {
        this.adapters = adapters;
    }

    /**
     * Groups the items into clauses, in source order. A multiplicity item is its own clause.
     *
     * @param element
     *            the declared element, carrying the clause keyword markers
     * @param items
     *            the specialization items in source order
     * @return the clauses, in source order
     */
    List<SpecializationClause> clauses(final Element element, final List<SourceItem> items) {
        if (items.size() <= 1) {
            return this.singleClauseOrEmpty(element, items);
        }
        final Map<ClauseKind, Deque<Boolean>> spellings = sourceSpellings(element);
        final boolean typingMarkers = items.stream().anyMatch(item -> item.kind() == ClauseKind.TYPING
                && this.adapters.hasTextAdapter(item.relationship(), ITextAdapter.TYPING_CLAUSE_START));
        final boolean[] splitStarts = splitStarts(items, extraSplits(items, spellings));
        final List<SpecializationClause> clauses = new ArrayList<>();
        List<SourceItem> current = new ArrayList<>();
        boolean keyword = false;
        ClauseKind previousKind = null;
        for (int index = 0; index < items.size(); index++) {
            final SourceItem item = items.get(index);
            final ClauseKind kind = item.kind();
            if (this.startsClause(item, previousKind, typingMarkers, splitStarts[index])) {
                if (!current.isEmpty()) {
                    clauses.add(new SpecializationClause(previousKind, keyword, List.copyOf(current)));
                }
                current = new ArrayList<>();
                keyword = this.isKeywordSpelling(element, item, typingMarkers, spellings);
            }
            current.add(item);
            previousKind = kind;
        }
        if (!current.isEmpty()) {
            clauses.add(new SpecializationClause(previousKind, keyword, List.copyOf(current)));
        }
        return List.copyOf(clauses);
    }

    private boolean startsClause(final SourceItem item, final ClauseKind previousKind, final boolean typingMarkers, final boolean splitStart) {
        final ClauseKind kind = item.kind();
        boolean startsClause = kind != previousKind || kind.singleTarget() || kind == ClauseKind.MULTIPLICITY;
        if (!startsClause && kind == ClauseKind.TYPING) {
            startsClause = typingMarkers
                    && this.adapters.hasTextAdapter(item.relationship(), ITextAdapter.TYPING_CLAUSE_START);
        } else if (!startsClause) {
            startsClause = splitStart;
        }
        return startsClause;
    }

    /** Plans an empty or single-item suffix without counting runs or allocating split maps. */
    private List<SpecializationClause> singleClauseOrEmpty(final Element element, final List<SourceItem> items) {
        if (items.isEmpty()) {
            return List.of();
        }
        final SourceItem item = items.get(0);
        final boolean typingMarkers = item.kind() == ClauseKind.TYPING
                && this.adapters.hasTextAdapter(item.relationship(), ITextAdapter.TYPING_CLAUSE_START);
        final boolean keyword = this.isKeywordSpelling(element, item, typingMarkers, sourceSpellings(element));
        return List.of(new SpecializationClause(item.kind(), keyword, List.copyOf(items)));
    }

    /**
     * Returns the index of the clause after which the {@code ordered} / {@code nonunique} modifiers are written, or
     * {@code -1} to write them before every clause.
     * <p>
     * The modifiers belong to {@code multiplicityPart}, so they follow the multiplicity when there is one. Otherwise
     * the parser's late-modifier marker tells whether the part started with a specialization (first alternative of
     * {@code featureSpecializationPart}); the clause markers attached before it tell how many non-typing clauses
     * preceded the modifiers. Without that marker the modifiers lead, as in {@code part p nonunique ordered : P}.
     * </p>
     *
     * @param element
     *            the declared element
     * @param clauses
     *            its clauses, as returned by {@link #clauses(Element, List)}
     * @return the clause index, from {@code -1} to {@code clauses.size() - 1}
     */
    int modifierSlot(final Element element, final List<SpecializationClause> clauses) {
        int slot = -1;
        final int multiplicityIndex = indexOfKind(clauses, ClauseKind.MULTIPLICITY);
        if (multiplicityIndex >= 0) {
            slot = multiplicityIndex;
        } else if (this.adapters.hasTextAdapter(element, ITextAdapter.LATE_NONUNIQUE) && !clauses.isEmpty()) {
            slot = slotAfterNonTypingClauses(clauses, clauseMarkersBeforeLateModifiers(element));
        }
        return slot;
    }

    /**
     * Returns the index of the last clause before the non-typing clause that follows {@code count} non-typing clauses,
     * or the last index when there is no such clause. Typing clauses carry no element marker, so those that follow the
     * counted clauses are kept before the modifiers.
     */
    private static int slotAfterNonTypingClauses(final List<SpecializationClause> clauses, final int count) {
        int nonTypingClauses = 0;
        for (int index = 0; index < clauses.size(); index++) {
            final boolean nonTyping = clauses.get(index).kind() != ClauseKind.TYPING;
            if (nonTyping && nonTypingClauses == count && index > 0) {
                return index - 1;
            }
            if (nonTyping) {
                nonTypingClauses++;
            }
        }
        return clauses.size() - 1;
    }

    /**
     * Tells whether the clause that {@code item} opens is spelled with its keyword ({@code defined by},
     * {@code specializes}, {@code subsets}, {@code redefines}, {@code references}, {@code crosses}) rather than its
     * symbol. Non-typing clauses consume the next recorded spelling of their kind, so mixed spellings such as
     * {@code subsets a :> c} are kept clause by clause.
     */
    private boolean isKeywordSpelling(final Element element, final SourceItem item, final boolean typingMarkers,
            final Map<ClauseKind, Deque<Boolean>> spellings) {
        final boolean keyword;
        if (item.kind() == ClauseKind.TYPING) {
            EObject marked = element;
            if (typingMarkers) {
                marked = item.relationship();
            }
            keyword = this.adapters.hasTextAdapter(marked, ITextAdapter.DEFINED_BY);
        } else {
            final Deque<Boolean> kindSpellings = spellings.get(item.kind());
            keyword = kindSpellings != null && !kindSpellings.isEmpty() && kindSpellings.poll();
        }
        return keyword;
    }

    /**
     * Counts, per kind, how many runs of consecutive items the source split into further clauses: the recorded clause
     * count minus the number of runs, bounded by the number of items that could open a clause.
     */
    private static Map<ClauseKind, Integer> extraSplits(final List<SourceItem> items,
            final Map<ClauseKind, Deque<Boolean>> spellings) {
        final Map<ClauseKind, Integer> itemCounts = new EnumMap<>(ClauseKind.class);
        final Map<ClauseKind, Integer> runCounts = new EnumMap<>(ClauseKind.class);
        ClauseKind previousKind = null;
        for (final SourceItem item : items) {
            itemCounts.merge(item.kind(), 1, Integer::sum);
            if (item.kind() != previousKind) {
                runCounts.merge(item.kind(), 1, Integer::sum);
            }
            previousKind = item.kind();
        }
        final Map<ClauseKind, Integer> splits = new EnumMap<>(ClauseKind.class);
        spellings.forEach((kind, kindSpellings) -> {
            final int runs = runCounts.getOrDefault(kind, 0);
            final int available = itemCounts.getOrDefault(kind, 0) - runs;
            splits.put(kind, Math.max(0, Math.min(kindSpellings.size() - runs, available)));
        });
        return splits;
    }

    /**
     * Marks the items that open one of the extra clauses inside a run of one kind. The source position of these
     * splits is not recorded, so they are placed at the end of the runs: {@code subsets a, b subsets c} rather than
     * {@code subsets a subsets b, c}, the spelling of the standard library ({@code subsets nestedReference, parameter
     * subsets Metadata::metadataItems}).
     */
    private static boolean[] splitStarts(final List<SourceItem> items, final Map<ClauseKind, Integer> extraSplits) {
        final boolean[] starts = new boolean[items.size()];
        for (int index = items.size() - 1; index > 0; index--) {
            final ClauseKind kind = items.get(index).kind();
            if (kind == items.get(index - 1).kind() && extraSplits.getOrDefault(kind, 0) > 0) {
                extraSplits.merge(kind, -1, Integer::sum);
                starts[index] = true;
            }
        }
        return starts;
    }

    /** Returns the index of the first clause of the given kind, or {@code -1}. */
    private static int indexOfKind(final List<SpecializationClause> clauses, final ClauseKind kind) {
        for (int index = 0; index < clauses.size(); index++) {
            if (clauses.get(index).kind() == kind) {
                return index;
            }
        }
        return -1;
    }

    /** Collects, per clause kind, the keyword ({@code true}) or symbol ({@code false}) spelling of each source clause. */
    private static Map<ClauseKind, Deque<Boolean>> sourceSpellings(final Element element) {
        final Map<ClauseKind, Deque<Boolean>> spellings = new EnumMap<>(ClauseKind.class);
        for (final Adapter adapter : element.eAdapters()) {
            final ClauseMarker marker = ClauseMarker.of(adapter);
            if (marker != null) {
                // Markers are attached in reverse source order.
                spellings.computeIfAbsent(marker.kind(), kind -> new ArrayDeque<>()).addFirst(marker.keyword());
            }
        }
        return spellings;
    }

    /**
     * Counts the non-typing clause markers of the clauses written before the late-modifier marker. The parser
     * attaches an element's markers in reverse source order, so these are the markers that follow it.
     */
    private static int clauseMarkersBeforeLateModifiers(final Element element) {
        int count = 0;
        boolean afterLateModifiers = false;
        for (final Adapter adapter : element.eAdapters()) {
            if (adapter == ITextAdapter.LATE_NONUNIQUE) {
                afterLateModifiers = true;
            } else if (afterLateModifiers && ClauseMarker.of(adapter) != null) {
                count++;
            }
        }
        return count;
    }

    /**
     * One clause written in the source, as one or more consecutive items.
     *
     * @param kind
     *            the clause kind
     * @param keyword
     *            whether the clause is spelled with its keyword rather than its symbol
     * @param items
     *            the items listed in the clause
     */
    record SpecializationClause(ClauseKind kind, boolean keyword, List<SourceItem> items) {

        /**
         * Returns the target names listed in the clause.
         *
         * @return the names, in source order
         */
        List<String> names() {
            return this.items.stream().flatMap(item -> item.names().stream()).toList();
        }
    }

    /**
     * Parser marker recording the spelling of one non-typing clause.
     *
     * @param kind
     *            the clause kind
     * @param keyword
     *            whether the clause used the keyword
     */
    private record ClauseMarker(ClauseKind kind, boolean keyword) {

        private static final Map<Adapter, ClauseMarker> MARKERS = Map.ofEntries(
                Map.entry(ITextAdapter.SPECIALIZES, new ClauseMarker(ClauseKind.SUBCLASSIFICATION, true)),
                Map.entry(ITextAdapter.SPECIALIZES_SYMBOL, new ClauseMarker(ClauseKind.SUBCLASSIFICATION, false)),
                Map.entry(ITextAdapter.SUBSETS, new ClauseMarker(ClauseKind.SUBSETTING, true)),
                Map.entry(ITextAdapter.SUBSETS_SYMBOL, new ClauseMarker(ClauseKind.SUBSETTING, false)),
                Map.entry(ITextAdapter.REDEFINES, new ClauseMarker(ClauseKind.REDEFINITION, true)),
                Map.entry(ITextAdapter.REDEFINES_SYMBOL, new ClauseMarker(ClauseKind.REDEFINITION, false)),
                Map.entry(ITextAdapter.REFERENCES, new ClauseMarker(ClauseKind.REFERENCE_SUBSETTING, true)),
                Map.entry(ITextAdapter.REFERENCES_SYMBOL, new ClauseMarker(ClauseKind.REFERENCE_SUBSETTING, false)),
                Map.entry(ITextAdapter.CROSSES, new ClauseMarker(ClauseKind.CROSS_SUBSETTING, true)),
                Map.entry(ITextAdapter.CROSSES_SYMBOL, new ClauseMarker(ClauseKind.CROSS_SUBSETTING, false)));

        /** Returns the clause marker an adapter stands for, or {@code null} for any other adapter. */
        static ClauseMarker of(final Adapter adapter) {
            return MARKERS.get(adapter);
        }
    }
}
