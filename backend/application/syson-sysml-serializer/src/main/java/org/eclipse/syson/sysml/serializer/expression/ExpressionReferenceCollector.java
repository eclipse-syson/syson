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
package org.eclipse.syson.sysml.serializer.expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.SerializationMode;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.model.ModelReference;
import org.eclipse.syson.sysml.serializer.model.ReferenceTargets;
import org.eclipse.syson.sysml.serializer.model.SafeEmfAccess;
import org.eclipse.syson.sysml.serializer.model.StoredModel;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceNameMatcher;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.tokens.ReferenceNameSyntax;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Expression;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureChainExpression;
import org.omg.sysml.lang.sysml.FeatureChaining;
import org.omg.sysml.lang.sysml.FeatureReferenceExpression;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Matches resolved and dangling expression references to their rendered spellings.
 *
 * <p>
 * References are collected from the EMF containment tree and then matched to the final text. This keeps reference
 * tracing independent from the syntax used to assemble the text. The same matching updates parsed spellings
 * after model references change, for both text and HTML output.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class ExpressionReferenceCollector {

    private static final String MEMBER_ELEMENT_REFERENCE = "memberElement";

    private final Function<Expression, String> expressionText;

    private final ReferenceResolver referenceResolver;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ReferenceTargets referenceTargets;

    private Map<String, List<TextOutput.Reference>> indexedReferencesByName;

    private List<ReferenceRename> qualifiedIndexedRenames = List.of();

    private boolean hasReferenceRenames;

    /**
     * Creates a collector for one run.
     *
     * @param expressionText
     *            renders an expression exactly as it is written, so references are matched against the same text
     * @param referenceResolver
     *            the run's reference resolver
     * @param context
     *            the run's context
     */
    public ExpressionReferenceCollector(final Function<Expression, String> expressionText,
            final ReferenceResolver referenceResolver,
            final SerializationContext context) {
        this.expressionText = expressionText;
        this.referenceResolver = referenceResolver;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.referenceTargets = context.modelQueries().referenceTargets();
    }

    /**
     * Returns the references of an expression subtree visible in the expression's rendered text, including references
     * whose dangling spelling was resolved elsewhere in the model.
     *
     * @param expression
     *            the expression
     * @return an immutable list in text order, possibly empty
     */
    public List<TextOutput.Reference> collect(final Expression expression) {
        final List<TextOutput.Reference> references = new ArrayList<>();
        final String text = this.expressionText.apply(expression);
        this.collectExpressionReferencesInSubtree(expression, text, references);
        this.collectReferencesInSubtree(expression, text, references);
        this.addVisibleIndexedReferences(references, text);
        return this.scopedOccurrences(expression, text, references);
    }

    /**
     * Returns the references of {@code element}'s subtree visible in caller-provided text.
     *
     * @param element
     *            the element whose subtree owns the references
     * @param text
     *            the rendered text
     * @return an immutable list in text order, possibly empty
     */
    public List<TextOutput.Reference> collect(final Element element,
            final String text) {
        return this.collect(element, text, null);
    }

    /**
     * Returns the references of {@code element}'s subtree visible in {@code text}, plus {@code fallbackTarget}: under
     * its reference name when visible, otherwise under the whole text.
     *
     * @param element
     *            the element whose subtree owns the references
     * @param text
     *            the rendered text
     * @param fallbackTarget
     *            an extra target known to the caller, or {@code null}
     * @return an immutable list in text order, possibly empty
     */
    public List<TextOutput.Reference> collect(final Element element, final String text,
            final EObject fallbackTarget) {
        final List<TextOutput.Reference> candidates = new ArrayList<>();
        this.collectExpressionReferencesInSubtree(element, text, candidates);
        this.collectReferencesInSubtree(element, text, candidates);
        final List<TextOutput.Reference> fallbackReferences = new ArrayList<>();
        if (fallbackTarget != null) {
            if (fallbackTarget instanceof final Element target) {
                this.addVisibleAtomicReferences(fallbackReferences, text,
                        this.context.nameRenderer().referenceName(target),
                        target);
            }
            if (fallbackReferences.isEmpty()) {
                fallbackReferences.addAll(this.atomicReferenceOccurrences(text, fallbackTarget));
            }
        }
        candidates.addAll(fallbackReferences);
        this.addVisibleIndexedReferences(candidates, text);
        return this.withFallbackTargets(text, this.scopedOccurrences(element, text, candidates), fallbackReferences);
    }

    /** Restores caller-known targets in gaps or unresolved occurrences without replacing scoped model bindings. */
    private List<TextOutput.Reference> withFallbackTargets(final String text, final List<TextOutput.Reference> occurrences,
            final List<TextOutput.Reference> fallbackReferences) {
        if (fallbackReferences.isEmpty()) {
            return occurrences;
        }
        final NavigableMap<Integer, TextOutput.Reference> indexed = this.indexedOccurrences(text, occurrences);
        for (final Map.Entry<Integer, TextOutput.Reference> fallback : this.indexedOccurrences(text,
                this.visibleOccurrences(text, fallbackReferences)).entrySet()) {
            this.mergeFallback(indexed, fallback.getKey(), fallback.getValue());
        }
        return List.copyOf(indexed.values());
    }

    /** Associates each occurrence with its position in the rendered text, keeping repeated names distinct. */
    private NavigableMap<Integer, TextOutput.Reference> indexedOccurrences(final String text, final List<TextOutput.Reference> occurrences) {
        final NavigableMap<Integer, TextOutput.Reference> indexed = new TreeMap<>();
        int offset = 0;
        for (final TextOutput.Reference occurrence : occurrences) {
            final ReferenceMatch match = this.referenceMatch(text, occurrence.name(), offset);
            if (match.start() >= 0) {
                indexed.put(match.start(), new TextOutput.Reference(match.name(), occurrence.target()));
                offset = match.start() + match.name().length();
            }
        }
        return indexed;
    }

    /** Fills an unbound occurrence over the same text range, or adds a fallback that overlaps no scoped occurrence. */
    private void mergeFallback(final NavigableMap<Integer, TextOutput.Reference> occurrences, final int start,
            final TextOutput.Reference fallback) {
        final Map.Entry<Integer, TextOutput.Reference> previous = occurrences.floorEntry(start);
        if (previous != null && start < previous.getKey() + previous.getValue().name().length()) {
            if (previous.getKey() == start && previous.getValue().name().length() == fallback.name().length()
                    && previous.getValue().target() == null && fallback.target() != null) {
                occurrences.put(start, new TextOutput.Reference(previous.getValue().name(), fallback.target()));
            }
        } else {
            final Integer next = occurrences.ceilingKey(start);
            if (next == null || start + fallback.name().length() <= next) {
                occurrences.put(start, fallback);
            }
        }
    }

    /**
     * Matches each rendered path against its own expression's references before using the broader candidate pool.
     * Otherwise the two {@code a} occurrences in {@code x.a + y.a} lose their distinct targets when pooled by name.
     */
    private List<TextOutput.Reference> scopedOccurrences(final Element element, final String text,
            final List<TextOutput.Reference> candidates) {
        final List<ExpressionSpan> spans = new ArrayList<>();
        this.collectExpressionSpans(element, text, 0, spans);
        final List<TextOutput.Reference> occurrences = new ArrayList<>();
        int offset = 0;
        for (final ExpressionSpan span : spans) {
            occurrences.addAll(this.visibleOccurrences(text.substring(offset, span.start()), candidates));
            final String pathText = text.substring(span.start(), span.end());
            final List<TextOutput.Reference> pathReferences = new ArrayList<>();
            this.collectExpressionReferencesInSubtree(span.expression(), pathText, pathReferences);
            this.collectReferencesInSubtree(span.expression(), pathText, pathReferences);
            if (span.expression() instanceof final FeatureChainExpression chain) {
                occurrences.addAll(this.chainOccurrences(chain, pathText, pathReferences));
            } else {
                occurrences.addAll(this.visibleOccurrences(pathText, pathReferences));
            }
            offset = span.end();
        }
        occurrences.addAll(this.visibleOccurrences(text.substring(offset), candidates));
        return List.copyOf(occurrences);
    }

    /** Finds non-overlapping path expressions in containment and text order, skipping parser expression wrappers. */
    private int collectExpressionSpans(final Element element, final String text, final int from,
            final List<ExpressionSpan> spans) {
        if (element == null) {
            return from;
        }
        int offset = from;
        final ExpressionSpan span = this.pathSpan(element, text, from);
        if (span != null) {
            spans.add(span);
            offset = span.end();
        } else {
            for (final Relationship relationship : this.navigation.ownedRelationships(element)) {
                for (final Element related : this.navigation.ownedRelatedElements(relationship)) {
                    offset = this.collectExpressionSpans(related, text, offset, spans);
                }
            }
        }
        return offset;
    }

    /** Locates one rendered path, leaving expression wrappers to their owned expressions. */
    private ExpressionSpan pathSpan(final Element element, final String text, final int from) {
        ExpressionSpan span = null;
        if (element instanceof FeatureChainExpression
                || element instanceof FeatureReferenceExpression && this.navigation.firstOwnedExpression(element) == null) {
            final Expression expression = (Expression) element;
            final String path = this.expressionText.apply(expression);
            int start = -1;
            if (!path.isBlank()) {
                start = ReferenceTextScanner.firstReferenceIndex(text, path, from);
            }
            if (start >= 0) {
                span = new ExpressionSpan(start, start + path.length(), expression);
            }
        }
        return span;
    }

    /**
     * Binds the source and appended segments in path order, even when both spell the same name, as in {@code a.a}.
     * A source or segment whose spelling is not found in the rendered path keeps no binding of its own: the text around
     * it is matched against the broader candidates instead.
     */
    private List<TextOutput.Reference> chainOccurrences(final FeatureChainExpression chain, final String text,
            final List<TextOutput.Reference> candidates) {
        final List<TextOutput.Reference> occurrences = new ArrayList<>();
        int offset = 0;
        for (final Expression source : this.navigation.operands(chain)) {
            final String sourceText = this.expressionText.apply(source);
            int start = -1;
            if (!sourceText.isBlank()) {
                start = ReferenceTextScanner.firstReferenceIndex(text, sourceText, offset);
            }
            if (start >= 0) {
                occurrences.addAll(this.visibleOccurrences(text.substring(offset, start), candidates));
                occurrences.addAll(this.collect(source));
                offset = start + sourceText.length();
            }
        }
        for (final ModelReference segment : this.chainSegments(chain)) {
            final ReferenceMatch match = this.referenceMatch(text, this.referenceResolver.referenceName(segment.name()), offset);
            if (match.start() >= 0) {
                occurrences.addAll(this.visibleOccurrences(text.substring(offset, match.start()), candidates));
                occurrences.addAll(this.atomicReferenceOccurrences(match.name(), segment.element()));
                offset = match.start() + match.name().length();
            }
        }
        occurrences.addAll(this.visibleOccurrences(text.substring(offset), candidates));
        return List.copyOf(occurrences);
    }

    /** Reads only this chain's appended targets, excluding the value expressions that render its source. */
    private List<ModelReference> chainSegments(final FeatureChainExpression chain) {
        final List<ModelReference> segments = new ArrayList<>();
        for (final Relationship relationship : this.navigation.ownedRelationships(chain)) {
            if (this.navigation.isMetaclass(relationship, SysMLPackage.eINSTANCE.getMembership())) {
                segments.addAll(this.referenceTargets.referenceTargets(relationship, MEMBER_ELEMENT_REFERENCE,
                        this.context.danglingReferenceIndex(), this.context.nameRenderer()::referenceName));
            }
            for (final Element related : this.navigation.ownedRelatedElements(relationship)) {
                if (related instanceof Feature && this.navigation.featureValues(related).isEmpty()) {
                    segments.addAll(this.context.modelQueries().nestedTargets().chainedFeatureReferencesInSubtree(related,
                            this.context.danglingReferenceIndex(), this.context.nameRenderer()::referenceName));
                }
            }
        }
        return segments;
    }

    /**
     * Replaces parser spellings made stale by dangling references resolved after parsing with the targets' current
     * reference names. Renames from {@code element}'s subtree claim occurrences first, then unambiguous qualified
     * renames from the whole model; each replaces only the first unclaimed whole-word occurrence of the original text.
     *
     * @param element
     *            the element whose subtree owns the references
     * @param text
     *            text containing parser spellings
     * @return the updated text, or {@code text} itself when nothing was resolved
     */
    public String updatedText(final Element element, final String text) {
        String updated = text;
        if (this.context.hasResolvedDanglingTargets()) {
            this.indexReferencesAndRenames();
            if (this.hasReferenceRenames) {
                updated = this.updatedTextWithRenames(element, text);
            }
        }
        return updated;
    }

    private String updatedTextWithRenames(final Element element,
            final String text) {
        final List<ReferenceRename> renames = new ArrayList<>();
        this.collectReferenceRenamesInSubtree(element, renames);
        renames.addAll(this.qualifiedIndexedRenames);
        return ReferenceTextScanner.replaceFirstReferences(text, renames);
    }

    private void collectReferenceRenamesInSubtree(final Element element,
            final List<ReferenceRename> renames) {
        this.collectReferenceRenames(element, renames);
        for (final Relationship relationship : this.navigation.ownedRelationships(element)) {
            this.collectReferenceRenames(relationship, renames);
            for (final Element related : this.navigation
                    .ownedRelatedElements(relationship)) {
                this.collectReferenceRenamesInSubtree(related, renames);
            }
        }
    }

    /**
     * Splits a reference spelling on {@code ::} and {@code .} outside quotes into an immutable segment list, binding
     * only the final segment to the target. Separators are not part of the segments.
     *
     * @param text
     *            the reference spelling, or {@code null} for an empty list
     * @param target
     *            the target of the last segment, or {@code null}
     * @return one reference per segment
     */
    public static List<TextOutput.Reference> atomicReferences(final String text,
            final EObject target) {
        final List<String> segments = ReferenceTextScanner.referenceSegments(text);
        final List<TextOutput.Reference> references = new ArrayList<>();
        for (int index = 0; index < segments.size(); index++) {
            EObject segmentTarget = null;
            if (index == segments.size() - 1) {
                segmentTarget = target;
            }
            references.add(new TextOutput.Reference(segments.get(index),
                    segmentTarget));
        }
        return List.copyOf(references);
    }

    /**
     * Adds resolved dangling references whose name appears as a token of {@code text}, for references the subtree
     * walk cannot reach (for example ones held by elements outside it).
     */
    private void addVisibleIndexedReferences(
            final List<TextOutput.Reference> references, final String text) {
        final Map<String, List<TextOutput.Reference>> indexedReferences = this
                .indexReferencesAndRenames();
        for (final String name : ReferenceTextScanner.referenceTokens(text)) {
            final List<TextOutput.Reference> matches = indexedReferences
                    .get(name);
            if (matches != null && !matches.isEmpty()) {
                references.add(matches.getFirst());
            }
        }
    }

    /**
     * Initializes reference and rename caches together on first use; subsequent calls return the cached index.
     */
    private Map<String, List<TextOutput.Reference>> indexReferencesAndRenames() {
        if (this.indexedReferencesByName == null) {
            final Map<String, List<TextOutput.Reference>> references = new HashMap<>();
            final Map<String, Element> bindings = new HashMap<>();
            final List<ReferenceRename> qualifiedRenames = new ArrayList<>();
            for (final EObject referenceContext : this.context
                    .danglingReferenceIndex().contexts()) {
                this.collectIndexedReferences(referenceContext, references, bindings);
                final List<ReferenceRename> contextRenames = new ArrayList<>();
                this.collectReferenceRenames(referenceContext, contextRenames);
                this.hasReferenceRenames = this.hasReferenceRenames
                        || !contextRenames.isEmpty();
                contextRenames.stream()
                        .filter(rename -> rename.original()
                                .contains(SysMLTokens.NAMESPACE_SEPARATOR))
                        .forEach(qualifiedRenames::add);
            }
            // Certify each spelling once; repeated occurrences must not rescan all of its model-wide candidates.
            references.replaceAll((name, candidates) -> unambiguousIndexedReferences(candidates));
            this.indexedReferencesByName = references;
            this.qualifiedIndexedRenames = unambiguousQualifiedRenames(qualifiedRenames, bindings);
        }
        return this.indexedReferencesByName;
    }

    /** Collapses a nonempty index bucket only when every candidate denotes the same object. */
    private static List<TextOutput.Reference> unambiguousIndexedReferences(final List<TextOutput.Reference> candidates) {
        final TextOutput.Reference first = candidates.getFirst();
        if (candidates.stream().allMatch(candidate -> candidate.target() == first.target())) {
            return List.of(first);
        }
        return List.of();
    }

    /**
     * Keeps model-wide fallback renames only when every recorded use of the spelling identifies the same target and
     * every replacement agrees. Bindings include unchanged references: a relative spelling such as {@code Q::A}
     * can name different elements in different namespaces even when only one of those elements was renamed.
     * A null binding denotes an unresolved or ambiguous spelling and prevents a model-wide replacement.
     */
    private static List<ReferenceRename> unambiguousQualifiedRenames(final List<ReferenceRename> qualifiedRenames,
            final Map<String, Element> bindings) {
        return qualifiedRenames.stream()
                .filter(rename -> bindings.get(ReferenceNameMatcher.normalized(rename.original())) != null)
                .collect(Collectors.groupingBy(
                        ReferenceRename::original,
                        LinkedHashMap::new,
                        Collectors.toList()))
                .values().stream()
                .filter(sameSpellingRenames -> sameSpellingRenames.stream()
                        .map(ReferenceRename::replacement).distinct().count() == 1)
                .map(sameSpellingRenames -> sameSpellingRenames.get(0)).toList();
    }

    private void collectIndexedReferences(final EObject referenceContext,
            final Map<String, List<TextOutput.Reference>> references, final Map<String, Element> bindings) {
        for (final String referenceName : this.context.danglingReferenceIndex()
                .referenceNames(referenceContext)) {
            final EStructuralFeature feature = referenceContext.eClass()
                    .getEStructuralFeature(referenceName);
            if (!(feature instanceof final EReference reference)) {
                continue;
            }
            final List<Element> targets = this.resolvedTargets(referenceContext, reference);
            targets.forEach(
                    target -> this.addIndexedReference(references, target));
            final List<String> spellings = this.context.danglingReferenceIndex().targets(referenceContext, referenceName);
            for (int index = 0; index < spellings.size(); index++) {
                final String spelling = ReferenceNameMatcher.normalized(this.referenceResolver.referenceName(spellings.get(index)));
                Element target = null;
                if (targets.size() == spellings.size()) {
                    target = targets.get(index);
                }
                if (!bindings.containsKey(spelling)) {
                    bindings.put(spelling, target);
                } else if (bindings.get(spelling) != target) {
                    bindings.put(spelling, null);
                }
            }
        }
    }

    /**
     * Records a rename for each resolved dangling reference of {@code referenceContext} whose parser spelling no
     * longer matches the target's name. Spellings confirmed by the reference path resolver in round-trip mode are
     * aliases, not stale names, and are kept, as are the spellings of a reference whose targets were only partly
     * linked. Feature chain segments are skipped: the chain rendering already names each segment after its current
     * target, and a textual rename of a segment could rewrite another segment with the same spelling.
     */
    private void collectReferenceRenames(final EObject referenceContext,
            final List<ReferenceRename> renames) {
        if (this.isFeatureChainSegment(referenceContext)) {
            return;
        }
        for (final String referenceName : this.context.danglingReferenceIndex()
                .referenceNames(referenceContext)) {
            final EStructuralFeature feature = referenceContext.eClass()
                    .getEStructuralFeature(referenceName);
            if (!(feature instanceof final EReference reference)) {
                continue;
            }
            final List<Element> targets = this.resolvedTargets(referenceContext,
                    reference);
            final List<String> originalNames = this.context
                    .danglingReferenceIndex()
                    .targets(referenceContext, referenceName);
            // Linking installs only successful targets: with a partial list, positions no longer pair spellings and
            // targets, and keeping a spelling is safer than renaming it after the wrong target.
            if (targets.size() != originalNames.size()) {
                continue;
            }
            for (int index = 0; index < targets.size(); index++) {
                if (this.shouldRenameReference(referenceContext, reference, originalNames.get(index), targets.get(index))) {
                    this.addReferenceRename(renames, originalNames.get(index), targets.get(index));
                }
            }
        }
    }

    /** A linker-confirmed alias is stable; otherwise the current target and lexical context decide. */
    private boolean shouldRenameReference(final EObject referenceContext, final EReference reference, final String original, final Element target) {
        final boolean confirmedAlias = this.context.options().mode() == SerializationMode.ROUND_TRIP
                && !this.context.referencePathResolver().resolve(referenceContext, reference, original, target).isEmpty();
        return !confirmedAlias && referenceContext instanceof final Element contextElement
                && !this.referenceTargets.parsedNameDesignates(contextElement, reference.getName(), original, target);
    }

    /** Tells whether a reference context holds one segment of a feature chain, {@code a.b} or {@code src.b}. */
    private boolean isFeatureChainSegment(final EObject referenceContext) {
        return referenceContext instanceof FeatureChaining
                || referenceContext instanceof final Relationship relationship
                        && this.navigation.isMetaclass(relationship, SysMLPackage.eINSTANCE.getMembership())
                        && relationship.getOwningRelatedElement() instanceof FeatureChainExpression;
    }

    /** Returns the installed Element targets of a reference, proxies included, without resolving them. */
    private List<Element> resolvedTargets(final EObject referenceContext,
            final EReference reference) {
        return SafeEmfAccess.unresolvedValues(referenceContext, reference).stream()
                .filter(Element.class::isInstance)
                .map(Element.class::cast)
                .toList();
    }

    /** Records a spelling already proven stale against its target, including its segment-quoted variant. */
    private void addReferenceRename(final List<ReferenceRename> renames,
            final String originalName, final Element target) {
        final String renderedOriginal = this.referenceResolver.referenceName(originalName);
        final String renderedTarget = this.referenceResolver.referenceName(
                this.context.nameRenderer().referenceName(target));
        if (renderedOriginal.isBlank() || renderedTarget.isBlank() || renderedOriginal.equals(renderedTarget)) {
            return;
        }
        renames.add(new ReferenceRename(renderedOriginal, renderedTarget));
        final String quotedQualifiedName = this.referenceResolver.quoteQualifiedNameSegments(originalName);
        if (!quotedQualifiedName.equals(renderedOriginal)) {
            renames.add(
                    new ReferenceRename(quotedQualifiedName, renderedTarget));
        }
    }

    private void addIndexedReference(
            final Map<String, List<TextOutput.Reference>> references,
            final Element target) {
        final String name = this.context.nameRenderer().referenceName(target);
        for (final TextOutput.Reference reference : this
                .atomicReferenceOccurrences(name, target)) {
            references.computeIfAbsent(reference.name(),
                    ignored -> new ArrayList<>()).add(reference);
        }
    }

    /**
     * Splits one reference spelling into segment occurrences; see
     * {@link #atomicReferenceOccurrences(String, EObject, EObject, EReference)}.
     *
     * @param text
     *            the reference spelling
     * @param target
     *            the referenced object, or {@code null}
     * @return one occurrence per segment
     */
    public List<TextOutput.Reference> atomicReferenceOccurrences(
            final String text, final EObject target) {
        return this.atomicReferenceOccurrences(text, target, null, null);
    }

    /**
     * Splits one reference spelling into segment occurrences and assigns each segment a target, so every segment of a
     * qualified name links to its own element. Segment targets come from the caller's reference path resolver when it
     * answers for every segment; otherwise the target is placed on the segment spelling its name and the preceding
     * segments are matched against its owning namespaces, stopping at the first mismatch.
     *
     * @param text
     *            the reference spelling
     * @param target
     *            the terminal target, or {@code null}
     * @param referenceContext
     *            the object holding the reference, or {@code null}
     * @param reference
     *            the EMF reference holding {@code target}, or {@code null}
     * @return one occurrence per segment; unmatched segments have a {@code null} target
     */
    public List<TextOutput.Reference> atomicReferenceOccurrences(
            final String text, final EObject target,
            final EObject referenceContext, final EReference reference) {
        // The conjugation prefix of ~P::Pt is not part of the first segment's name.
        final List<String> segments = ReferenceTextScanner.referenceSegments(ReferenceNameSyntax.withoutConjugation(text));
        if (target == null || segments.size() <= 1
                || !(target instanceof final Element element)) {
            return atomicReferences(text, target);
        }
        final List<EObject> resolvedSegmentTargets =
                this.resolvedSegmentTargets(segments, referenceContext,
                        reference, text, target);
        List<EObject> segmentTargets = resolvedSegmentTargets;
        if (segmentTargets.isEmpty()) {
            segmentTargets = this.ownershipSegmentTargets(segments, element);
        }
        return references(segments, segmentTargets);
    }

    /**
     * Aligns {@code element} and its owning namespaces with the segments spelling their names. A conjugated port
     * definition is spelled after the port definition owning it ({@code ~P::Pt}), so the names are matched from there.
     */
    private List<EObject> ownershipSegmentTargets(
            final List<String> segments, final Element element) {
        final Element namedElement = Objects.requireNonNullElse(StoredModel.originalPortDefinition(element), element);
        final String referenceName = this.context.nameRenderer()
                .referenceName(namedElement);
        final String declaredName = this.context.nameRenderer()
                .declaredName(namedElement);
        int targetIndex = segments.size() - 1;
        for (int index = segments.size() - 1; index >= 0; index--) {
            if (matchesTargetName(segments.get(index), referenceName,
                    declaredName)) {
                targetIndex = index;
                break;
            }
        }
        final List<EObject> targets = new ArrayList<>(
                Collections.nCopies(segments.size(), null));
        targets.set(targetIndex, element);
        if (this.matchesElementName(segments.get(targetIndex), namedElement)) {
            Namespace owner = StoredModel.owningNamespace(namedElement);
            for (int index = targetIndex - 1;
                    index >= 0 && owner != null; index--) {
                if (!this.matchesElementName(segments.get(index), owner)) {
                    break;
                }
                targets.set(index, owner);
                owner = StoredModel.owningNamespace(owner);
            }
        }
        return targets;
    }

    /**
     * Returns the resolver's per-segment targets aligned with {@code segments}, or an empty list when its answer does
     * not cover every segment; a leading {@code $} segment has no target.
     */
    private List<EObject> resolvedSegmentTargets(
            final List<String> segments, final EObject referenceContext,
            final EReference reference, final String text,
            final EObject terminalTarget) {
        List<EObject> alignedTargets = List.of();
        if (referenceContext != null && reference != null) {
            final List<EObject> targets = this.context.referencePathResolver()
                    .resolve(referenceContext, reference, text,
                            terminalTarget);
            int rootMarkerOffset = 0;
            if (!segments.isEmpty() && SysMLTokens.ROOT_NAMESPACE_MARKER
                    .equals(segments.get(0))) {
                // The absolute-path marker occupies a text segment but has no model target.
                rootMarkerOffset = 1;
            }
            if (targets.size() == segments.size() - rootMarkerOffset) {
                alignedTargets = new ArrayList<>(
                        Collections.nCopies(segments.size(), null));
                for (int index = 0; index < targets.size(); index++) {
                    alignedTargets.set(index + rootMarkerOffset, targets.get(index));
                }
            }
        }
        return alignedTargets;
    }

    private static List<TextOutput.Reference> references(
            final List<String> segments, final List<EObject> targets) {
        final List<TextOutput.Reference> references = new ArrayList<>();
        for (int index = 0; index < segments.size(); index++) {
            references.add(new TextOutput.Reference(segments.get(index),
                    targets.get(index)));
        }
        return List.copyOf(references);
    }

    private boolean matchesElementName(final String segment,
            final Element element) {
        return matchesTargetName(segment,
                this.context.nameRenderer().referenceName(element),
                this.context.nameRenderer().declaredName(element));
    }

    private static boolean matchesTargetName(final String segment,
            final String referenceName, final String declaredName) {
        return !referenceName.isBlank()
                && ReferenceNameMatcher.equivalent(segment, referenceName)
                || !declaredName.isBlank()
                        && ReferenceNameMatcher.equivalent(segment,
                                declaredName);
    }

    /** Collects the visible references held directly by each element and relationship of the owned subtree. */
    private void collectReferencesInSubtree(final Element element, final String text,
            final List<TextOutput.Reference> references) {
        if (element == null) {
            return;
        }
        final List<Relationship> ownedRelationships = this.navigation.ownedRelationships(element);
        this.collectDirectReferences(element, text, references);
        for (final Relationship relationship : ownedRelationships) {
            this.collectDirectReferences(relationship, text, references);
            for (final Element related : this.navigation
                    .ownedRelatedElements(relationship)) {
                this.collectReferencesInSubtree(related, text, references);
            }
        }
    }

    /**
     * Collects the references expressions carry through relationships: feature reference paths, membership member
     * elements, relationship targets and their dangling spellings.
     */
    private void collectExpressionReferencesInSubtree(final Element element,
            final String text,
            final List<TextOutput.Reference> references) {
        if (element == null) {
            return;
        }
        if (element instanceof final FeatureReferenceExpression referenceExpression) {
            this.addVisibleFeatureReferencePath(referenceExpression, text, references);
        }
        for (final Relationship relationship : List
                .copyOf(element.getOwnedRelationship())) {
            if (this.navigation.isMetaclass(relationship,
                    SysMLPackage.eINSTANCE.getMembership())
                    || this.navigation.isMetaclass(relationship,
                            SysMLPackage.eINSTANCE.getOwningMembership())) {
                this.referenceTargets
                        .referenceTargets(relationship,
                                MEMBER_ELEMENT_REFERENCE,
                                this.context.danglingReferenceIndex(),
                                this.context.nameRenderer()::referenceName)
                        .forEach(target -> this.addVisibleAtomicReferences(
                                references, text,
                                this.referenceResolver.referenceName(target.name()),
                                target.element()));
            }
            for (final Element target : relationshipTargets(relationship)) {
                this.addVisibleAtomicReferences(references, text,
                        this.context.nameRenderer().referenceName(target),
                        target);
            }
            this.context.danglingReferenceIndex().targets(relationship)
                    .forEach(target -> this.addVisibleAtomicReferences(
                            references, text,
                            this.referenceResolver.referenceName(target), null));
            for (final Element related : this.navigation
                    .ownedRelatedElements(relationship)) {
                this.collectExpressionReferencesInSubtree(related, text, references);
            }
        }
    }

    /**
     * Collects the visible references held by one element: its feature reference path, its relationship targets and
     * every non-containment, non-derived EMF reference, dangling spellings included.
     */
    private void collectDirectReferences(final Element element,
            final String text,
            final List<TextOutput.Reference> references) {
        if (element instanceof final FeatureReferenceExpression referenceExpression) {
            this.addVisibleFeatureReferencePath(referenceExpression, text, references);
        }
        if (element instanceof final Relationship relationship) {
            relationshipTargets(relationship)
                    .forEach(target -> this.addVisibleAtomicReferences(
                            references, text,
                            this.visibleReferenceName(text, target),
                            target));
            this.context.danglingReferenceIndex().targets(relationship)
                    .forEach(target -> this.addVisibleAtomicReferences(
                            references, text, this.referenceResolver.referenceName(target),
                            null));
        }
        for (final EReference reference : element.eClass()
                .getEAllReferences()) {
            if (reference.isContainment() || reference.isContainer()
                    || reference.isDerived()) {
                continue;
            }
            this.referenceTargets
                    .referenceTargets(element, reference.getName(),
                            this.context.danglingReferenceIndex(),
                            target -> this.visibleReferenceName(text, target))
                    .forEach(target -> this.addVisibleAtomicReferences(
                            references, text,
                            this.referenceResolver.referenceName(target.name()),
                            target.element(), element, reference));
        }
    }

    /** Adds the endpoint path of a feature reference expression, linked to its referenced feature, when visible. */
    private void addVisibleFeatureReferencePath(final FeatureReferenceExpression referenceExpression,
            final String text, final List<TextOutput.Reference> references) {
        final String path = this.referenceResolver.endpointPath(referenceExpression);
        if (!path.isBlank()) {
            this.addVisibleAtomicReferences(references, text, path,
                    this.referenceTargets.firstDirectRelationshipTargetElement(referenceExpression,
                            SysMLPackage.eINSTANCE.getReferenceSubsetting()));
        }
    }

    private static List<Element> relationshipTargets(
            final Relationship relationship) {
        return StoredModel.targets(relationship);
    }

    /**
     * Returns the target's reference name only when the target may appear in {@code text} (it is a proxy, unnamed, or
     * its declared name occurs), which avoids computing names for unrelated targets.
     */
    private String visibleReferenceName(final String text,
            final Element target) {
        final String declaredName = target.getDeclaredName();
        String name = "";
        if (target instanceof final InternalEObject internal
                && internal.eIsProxy()
                || declaredName == null
                || this.referenceMatch(text,
                        this.referenceResolver.referenceName(declaredName), 0)
                        .start() >= 0) {
            name = this.context.nameRenderer().referenceName(target);
        }
        return name;
    }

    private void addVisibleAtomicReferences(
            final List<TextOutput.Reference> references,
            final String renderedText, final String referenceName,
            final EObject target) {
        this.addVisibleAtomicReferences(references, renderedText,
                referenceName, target, null, null);
    }

    private void addVisibleAtomicReferences(
            final List<TextOutput.Reference> references,
            final String renderedText, final String referenceName,
            final EObject target, final EObject referenceContext,
            final EReference reference) {
        if (this.referenceMatch(renderedText, referenceName, 0).start() >= 0) {
            references.addAll(this.atomicReferenceOccurrences(referenceName,
                    target, referenceContext, reference));
        }
    }

    /**
     * Matches candidates against {@code text} from left to right without overlap and returns the occurrences found.
     * At the same position a longer spelling wins, then a resolved target over an unresolved one; a qualified
     * candidate whose full spelling is absent may match by its last segment. Matching is lexical, not token-aware.
     *
     * @param text
     *            the rendered text
     * @param candidates
     *            the candidate references, in any order
     * @return an immutable list in text order
     */
    public List<TextOutput.Reference> visibleOccurrences(final String text,
            final List<TextOutput.Reference> candidates) {
        final List<TextOutput.Reference> available = distinctNames(
                resolveQuotedTargets(candidates));
        final List<TextOutput.Reference> occurrences = new ArrayList<>();
        int offset = 0;
        while (offset < text.length()) {
            TextOutput.Reference next = null;
            ReferenceMatch nextMatch = null;
            for (final TextOutput.Reference candidate : available) {
                final ReferenceMatch match = this.referenceMatch(text,
                        candidate.name(), offset);
                if (this.isPreferredOccurrence(match, candidate, nextMatch, next)) {
                    next = candidate;
                    nextMatch = match;
                }
            }
            if (next == null || nextMatch == null) {
                break;
            }
            occurrences.add(
                    new TextOutput.Reference(nextMatch.name(), next.target()));
            offset = nextMatch.start() + nextMatch.name().length();
        }
        return List.copyOf(occurrences);
    }

    /**
     * Gives an unresolved quoted candidate a target only when every resolved candidate with the same unquoted
     * spelling agrees on its identity. An ambiguous spelling remains unresolved.
     *
     * @param candidates
     *            the candidate references
     * @return the candidates in the same order
     */
    public static List<TextOutput.Reference> resolveQuotedTargets(
            final List<TextOutput.Reference> candidates) {
        final Map<String, EObject> targetsByName = new HashMap<>();
        for (final TextOutput.Reference candidate : candidates) {
            if (candidate.target() != null) {
                final String name = ReferenceNameMatcher.normalized(candidate.name());
                if (!targetsByName.containsKey(name)) {
                    targetsByName.put(name, candidate.target());
                } else if (targetsByName.get(name) != candidate.target()) {
                    targetsByName.put(name, null);
                }
            }
        }
        return candidates.stream().map(candidate -> {
            if (candidate.target() != null || !isQuoted(candidate.name())) {
                return candidate;
            }
            final EObject target = targetsByName.get(
                    ReferenceNameMatcher.normalized(candidate.name()));
            TextOutput.Reference resolved = candidate;
            if (target != null) {
                resolved = new TextOutput.Reference(candidate.name(), target);
            }
            return resolved;
        }).toList();
    }

    /** Keeps one candidate per spelling, preferring a resolved target, in first-seen order. */
    private static List<TextOutput.Reference> distinctNames(
            final List<TextOutput.Reference> candidates) {
        final Map<String, TextOutput.Reference> byName =
                new LinkedHashMap<>();
        for (final TextOutput.Reference candidate : candidates) {
            final TextOutput.Reference existing = byName.putIfAbsent(
                    candidate.name(), candidate);
            if (existing != null && existing.target() == null
                    && candidate.target() != null) {
                byName.put(candidate.name(), candidate);
            }
        }
        return List.copyOf(byName.values());
    }

    private static boolean isQuoted(final String name) {
        return name.length() > 1
                && name.startsWith(SysMLTokens.UNRESTRICTED_NAME_DELIMITER)
                && name.endsWith(SysMLTokens.UNRESTRICTED_NAME_DELIMITER);
    }

    /** Tells whether a candidate match comes first in the text, or wins at the same position. */
    private boolean isPreferredOccurrence(final ReferenceMatch candidateMatch,
            final TextOutput.Reference candidate,
            final ReferenceMatch currentMatch,
            final TextOutput.Reference current) {
        boolean result = candidateMatch.start() >= 0;
        if (result && currentMatch != null
                && candidateMatch.start() >= currentMatch.start()) {
            result = preferredAtSamePosition(candidateMatch, candidate,
                    currentMatch, current);
        }
        return result;
    }

    private static boolean preferredAtSamePosition(
            final ReferenceMatch candidateMatch,
            final TextOutput.Reference candidate,
            final ReferenceMatch currentMatch,
            final TextOutput.Reference current) {
        boolean preferred = false;
        if (candidateMatch.start() == currentMatch.start()) {
            final int lengthDifference = candidateMatch.name().length()
                    - currentMatch.name().length();
            preferred = lengthDifference > 0;
            if (lengthDifference == 0) {
                preferred = current != null && current.target() == null
                        && candidate.target() != null;
            }
        }
        return preferred;
    }

    /** Finds {@code name} from {@code offset}, falling back to its last segment; a negative start means none. */
    private ReferenceMatch referenceMatch(final String text, final String name,
            final int offset) {
        ReferenceMatch match = new ReferenceMatch(-1, "");
        if (name != null && !name.isBlank()) {
            final int start = text.indexOf(name, offset);
            final String leaf = leafAfterLastSeparator(name);
            if (start >= 0) {
                match = new ReferenceMatch(start, name);
            } else if (leaf != null) {
                match = new ReferenceMatch(text.indexOf(leaf, offset), leaf);
            }
        }
        return match;
    }

    /** Returns what follows the last {@code ::} or {@code .} of a spelling, or {@code null} when it has none. */
    private static String leafAfterLastSeparator(final String name) {
        final String segment = ReferenceTextScanner.lastReferenceSegment(name);
        String leaf = null;
        if (!segment.equals(name)) {
            leaf = segment;
        }
        return leaf;
    }

    /** Position and spelling of a candidate occurrence in rendered text. */
    private record ReferenceMatch(int start, String name) {
    }

    /** The text range belonging to one path expression, whose targets are matched within that range only. */
    private record ExpressionSpan(int start, int end, Expression expression) {
    }
}
