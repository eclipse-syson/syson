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
package org.eclipse.syson.sysml.serializer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.sysml.parser.DanglingLink;

/**
 * Lookup table for unresolved reference names captured during parsing.
 *
 * <p>
 * The OMG model does not always contain proxy objects for unresolved references: the new parser records them separately
 * as dangling links. This service keeps serializers independent from the parser result shape while still allowing
 * round-trip serialization to emit the original unresolved names.
 * </p>
 * <p>
 * Entries are keyed by model object identity and by the name of the unresolved {@link EReference}. The index is
 * immutable once built; the retained links are only re-read to detect references that were resolved after parsing.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class DanglingReferenceIndex {

    private final Map<EObject, Map<String, List<String>>> targetsByObjectAndReference;

    /** Every spelling of each object in the parser's link order, across its references. */
    private final Map<EObject, List<String>> targetsByObject;

    private final List<DanglingLink> danglingLinks;

    private DanglingReferenceIndex(final Map<EObject, Map<String, List<String>>> targetsByObjectAndReference,
            final Map<EObject, List<String>> targetsByObject, final List<DanglingLink> danglingLinks) {
        this.targetsByObjectAndReference = freezeOwnedIndex(targetsByObjectAndReference);
        targetsByObject.replaceAll((context, targets) -> Collections.unmodifiableList(targets));
        this.targetsByObject = Collections.unmodifiableMap(targetsByObject);
        this.danglingLinks = danglingLinks;
    }

    /** Wraps the exclusively owned index in place; callers must not retain mutable aliases. */
    private static Map<EObject, Map<String, List<String>>> freezeOwnedIndex(final Map<EObject, Map<String, List<String>>> mutableIndex) {
        for (final Map.Entry<EObject, Map<String, List<String>>> contextEntry : mutableIndex.entrySet()) {
            final Map<String, List<String>> references = contextEntry.getValue();
            references.replaceAll((name, targets) -> Collections.unmodifiableList(targets));
            contextEntry.setValue(Collections.unmodifiableMap(references));
        }
        return Collections.unmodifiableMap(mutableIndex);
    }

    /**
     * Returns an index without any retained reference, used when the model does not come from the parser or when a
     * lookup must ignore parser spellings.
     */
    public static DanglingReferenceIndex empty() {
        return new DanglingReferenceIndex(Collections.emptyMap(), new IdentityHashMap<>(), List.of());
    }

    /**
     * Builds the index from the parser's dangling links; a {@code null} or empty set gives {@link #empty()}. A
     * {@code null} link is ignored. Links missing a context, reference or target list are not indexed by name, but are
     * still retained for {@link #hasTargetOfType} and {@link #hasResolvedTargets()}.
     */
    public static DanglingReferenceIndex from(final Set<DanglingLink> danglingLinks) {
        if (danglingLinks == null || danglingLinks.isEmpty()) {
            return empty();
        }
        // Parser links belong to specific model instances, even when their names are equal.
        // Reference names keep the parser's link order so that callers taking the first target are deterministic.
        final Map<EObject, Map<String, List<String>>> index = new IdentityHashMap<>();
        final Map<EObject, List<String>> targetsByObject = new IdentityHashMap<>();
        final List<DanglingLink> retainedLinks = danglingLinks.stream().filter(Objects::nonNull).map(DanglingReferenceIndex::snapshot).toList();
        for (final DanglingLink link : retainedLinks) {
            if (link.context() != null && link.reference() != null && link.targets() != null) {
                index.computeIfAbsent(link.context(), key -> new LinkedHashMap<>())
                        .computeIfAbsent(link.reference().getName(), key -> new ArrayList<>())
                        .addAll(link.targets());
                targetsByObject.computeIfAbsent(link.context(), key -> new ArrayList<>()).addAll(link.targets());
            }
        }
        return new DanglingReferenceIndex(index, targetsByObject, retainedLinks);
    }

    /**
     * Copies a link's target list so later changes to the caller's list cannot make name and type queries disagree.
     * The context and reference stay shared: {@link #hasResolvedTargets()} must observe later model changes.
     */
    private static DanglingLink snapshot(final DanglingLink link) {
        List<String> targets = null;
        if (link.targets() != null) {
            targets = Collections.unmodifiableList(new ArrayList<>(link.targets()));
        }
        return new DanglingLink(link.context(), link.reference(), link.eClass(), targets);
    }

    /**
     * Reports whether the link's reference now holds a non-proxy value, meaning the model was linked or edited after
     * parsing and the retained spelling may be stale. The probe has no side effect: proxies are neither resolved nor
     * replaced.
     */
    private static boolean hasResolvedTarget(final DanglingLink link) {
        return SafeEmfAccess.unresolvedValues(link.context(), link.reference()).stream()
                .anyMatch(target -> target instanceof final EObject object && !object.eIsProxy());
    }

    /**
     * Returns the parser spellings recorded for one reference of one object, in the parser's link order, so callers
     * taking the first spelling are deterministic; an empty immutable list when nothing was recorded or the context
     * or reference name is missing.
     */
    public List<String> targets(final EObject context, final String referenceName) {
        return this.targetsByObjectAndReference.getOrDefault(context, Collections.emptyMap()).getOrDefault(referenceName, List.of());
    }

    /**
     * Returns every parser spelling recorded for the object, whatever the reference, for callers that only need some
     * name for an unresolved target. Immutable, ordered as the parser reported the links.
     */
    public List<String> targets(final EObject context) {
        return this.targetsByObject.getOrDefault(context, List.of());
    }

    /**
     * Reports whether the parser left an unresolved reference expecting exactly {@code targetType}, which tells what
     * kind of element the source named even though no target is installed. Context, reference and metaclass are
     * matched by identity, without subtype matching; a {@code null} context or reference matches nothing, not the
     * incomplete links that lack one.
     */
    public boolean hasTargetOfType(final EObject context, final EReference reference, final EClass targetType) {
        if (context == null || reference == null) {
            return false;
        }
        return this.danglingLinks.stream()
                .filter(link -> link.context() == context && link.reference() == reference)
                .anyMatch(link -> link.eClass() == targetType && link.targets() != null && !link.targets().isEmpty());
    }

    /** Returns the objects owning at least one indexed unresolved reference, as an immutable unordered set. */
    public Set<EObject> contexts() {
        return Set.copyOf(this.targetsByObjectAndReference.keySet());
    }

    /** Returns the names of the object's references that have indexed spellings, as an immutable unordered set. */
    public Set<String> referenceNames(final EObject context) {
        return Set.copyOf(this.targetsByObjectAndReference.getOrDefault(context, Collections.emptyMap()).keySet());
    }

    /**
     * Reports whether any retained link now has a resolved value in the model. When none has, no unresolved
     * reference can have been renamed by linking, so callers can skip rename detection. The model is probed on each
     * call; callers are expected to cache the answer for a run.
     */
    public boolean hasResolvedTargets() {
        return this.danglingLinks.stream().anyMatch(DanglingReferenceIndex::hasResolvedTarget);
    }

}
