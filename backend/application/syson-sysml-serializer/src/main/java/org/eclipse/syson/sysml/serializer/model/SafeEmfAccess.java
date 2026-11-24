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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * Isolates defensive access to generated EMF getters.
 *
 * <p>
 * A partially resolved or dynamically initialized model can make a generated getter fail with different
 * {@link RuntimeException} subtypes. Serialization historically treats those failures as absent optional model data.
 * Keeping that boundary here avoids duplicating broad catches throughout renderers. The failure actually met on parsed
 * models is a {@link NullPointerException} raised by the derived feature-chaining logic of the metamodel implementation
 * when a chain is unresolved (for instance during {@code Namespace.resolve}).
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class SafeEmfAccess {

    private static final String REDEFINES = "redefines";

    private static final String SUBSETS = "subsets";

    private static final Map<EClass, Map<EReference, List<EReference>>> SPECIALIZATIONS = new ConcurrentHashMap<>();

    private SafeEmfAccess() {
        // Utility class.
    }

    /**
     * Evaluates a model access, treating any {@link RuntimeException} as missing data. A {@code null} result is
     * returned as-is: only failures fall back. Do not wrap logic whose exceptions signal serializer bugs, since they
     * would be silently hidden.
     *
     * @param access
     *            the model read to perform
     * @param fallback
     *            the value standing for absent data
     * @return the access result, or {@code fallback} if it failed
     */
    @SuppressWarnings("checkstyle:IllegalCatch")
    public static <T> T get(final Supplier<T> access, final T fallback) {
        try {
            return access.get();
        } catch (final RuntimeException exception) {
            // Generated EMF accessors do not expose a narrower failure contract.
            return fallback;
        }
    }

    /**
     * Reads the current values of a reference without resolving proxies and without letting the SysML logic compute
     * them, so that reading never modifies the model.
     * <p>
     * Only stored features are read. The metamodel redefines many references in subclasses, such as
     * {@code Relationship::target} by {@code Specialization::general} and then {@code FeatureTyping::type}, and its
     * implementation computes the redefined one as a union through resolving getters; such a reference is read through
     * the stored references that redefine or subset it. A containment, such as {@code Relationship::ownedRelatedElement},
     * is always read directly. A derived reference that only subsets a containment, such as the owned member element of
     * an owning membership, is read from that containment, even through intermediate redefinitions. Generated code
     * ignores the resolve flag of {@code eGet} for many-valued references, and iterating a resolving list resolves its
     * proxies and replaces its entries; lists are therefore read through their non-resolving view. A failed read gives
     * no value.
     * </p>
     *
     * @param object
     *            the object holding the reference
     * @param reference
     *            the reference to read
     * @return the referenced objects, proxies included, in list order and without duplicates; a single value as a
     *         one-element list
     */
    public static List<?> unresolvedValues(final EObject object, final EReference reference) {
        if (object == null || reference == null) {
            return List.of();
        }
        final Set<Object> values = Collections.newSetFromMap(new IdentityHashMap<>());
        final List<Object> ordered = new ArrayList<>();
        collectValues(object, reference, values, ordered);
        return ordered;
    }

    private static void collectValues(final EObject object, final EReference reference, final Set<Object> values, final List<Object> ordered) {
        final List<EReference> specializations = specializations(object.eClass(), reference);
        // A many-valued reference with specializations is a computed union whose getter must not be called, unless it
        // is a containment: a containment list is always the stored one.
        final boolean readable = specializations.isEmpty() || !reference.isMany() || reference.isContainment();
        if (isStored(reference) && readable) {
            storedValues(object, reference).stream()
                    .filter(values::add)
                    .forEach(ordered::add);
        }
        for (final EReference specialization : specializations) {
            collectValues(object, specialization, values, ordered);
        }
        if (!isStored(reference) && specializations.isEmpty()) {
            collectContainedValues(object, reference, values, ordered);
        }
    }

    /** Reads a derived reference that only subsets containments from those containments, keeping its type. */
    private static void collectContainedValues(final EObject object, final EReference reference, final Set<Object> values, final List<Object> ordered) {
        for (final EReference containment : generalContainments(reference)) {
            storedValues(object, containment).stream()
                    .filter(reference.getEReferenceType()::isInstance)
                    .filter(values::add)
                    .forEach(ordered::add);
        }
    }

    private static List<?> storedValues(final EObject object, final EReference reference) {
        final Object value = get(() -> object.eGet(reference, false), null);
        List<?> values = List.of();
        if (value instanceof final InternalEList<?> list) {
            values = list.basicList();
        } else if (value instanceof final List<?> list) {
            values = list;
        } else if (value != null) {
            values = List.of(value);
        }
        return values;
    }

    private static boolean isStored(final EReference reference) {
        return !reference.isDerived() && !reference.isVolatile() && !reference.isTransient();
    }

    /** Lists the references of a metaclass that directly redefine or subset {@code reference}; a metaclass never changes. */
    private static List<EReference> specializations(final EClass eClass, final EReference reference) {
        return SPECIALIZATIONS.computeIfAbsent(eClass, ignored -> new ConcurrentHashMap<>())
                .computeIfAbsent(reference, ignored -> eClass.getEAllReferences().stream()
                        .filter(candidate -> candidate != reference && generals(candidate).contains(reference))
                        .toList());
    }

    /**
     * Lists the containments that {@code reference} redefines or subsets, directly or through intermediate derived
     * references, as {@code FeatureMembership::ownedMemberFeature} reaches {@code Relationship::ownedRelatedElement}
     * through {@code OwningMembership::ownedMemberElement}. A stored reference that is not a containment, such as
     * {@code Relationship::target}, leads to no containment, so its own generals are not searched.
     */
    private static List<EReference> generalContainments(final EReference reference) {
        final List<EReference> containments = new ArrayList<>();
        for (final EReference general : generals(reference)) {
            if (general.isContainment()) {
                containments.add(general);
            } else if (!isStored(general)) {
                containments.addAll(generalContainments(general));
            }
        }
        return containments;
    }

    private static List<EReference> generals(final EReference reference) {
        final List<EReference> generals = new ArrayList<>();
        for (final EAnnotation annotation : reference.getEAnnotations()) {
            if (REDEFINES.equals(annotation.getSource()) || SUBSETS.equals(annotation.getSource())) {
                annotation.getReferences().stream()
                        .filter(EReference.class::isInstance)
                        .map(EReference.class::cast)
                        .forEach(generals::add);
            }
        }
        return generals;
    }
}
