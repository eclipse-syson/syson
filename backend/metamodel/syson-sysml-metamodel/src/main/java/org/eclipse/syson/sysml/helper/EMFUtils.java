/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
package org.eclipse.syson.sysml.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utils class to browse or edit EMF model.
 *
 * @author Arthur Daussy
 */
public class EMFUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(EMFUtils.class);

    /**
     * Gets the ancestor of given type starting from a specific element.
     *
     * @param object
     *            an {@link EObject}
     * @param type
     *            expected type
     * @param <T>
     *            expected type
     * @return a expected typed ancestor or <code>null</code>;
     */
    private static <T extends EObject> T getAncestor(Class<T> type, EObject object) {
        if (object == null) {
            return null;
        }
        final T result;
        if (type.isInstance(object)) {
            result = (T) object;
        } else {
            result = getAncestor(type, object.eContainer());
        }
        return result;
    }

    /**
     * Gets all Settings targeting the given source element with the given {@link EReference}
     *
     * @param source
     *            the source element
     * @param targetingFeature
     *            the feature that link the searched elements to the source
     * @return a {@link Collection} of {@link Setting}
     */
    public static Collection<Setting> getInverse(EObject source, EReference targetingFeature) {
        return source.eAdapters().stream()
                .filter(ECrossReferenceAdapter.class::isInstance)
                .map(ECrossReferenceAdapter.class::cast)
                .map(crossRef -> crossRef.getInverseReferences(source, targetingFeature, false))
                .findFirst().orElseGet(() -> {
                    LOGGER.warn("Unable to find an ECrossReference on " + source);
                    return List.of();
                });

    }
    
    /**
     * Gets all Settings targeting the given source element.
     *
     * @param source
     *            the source element
     * @return a {@link Collection} of {@link Setting}
     */
    public static Collection<Setting> getInverse(EObject source) {
        return source.eAdapters().stream()
                .filter(ECrossReferenceAdapter.class::isInstance)
                .map(ECrossReferenceAdapter.class::cast)
                .map(crossRef -> crossRef.getInverseReferences(source))
                .findFirst()
                .orElseGet(() -> {
                    LOGGER.warn("Unable to find an ECrossReference on " + source);
                    return List.of();
                });

    }

    /**
     * Gets the first adapter of a given type installed on a Notifier.
     *
     * @param notifier
     *         notifier on which the adapter is installed
     * @param adapterType
     *         the type of the desired adapter
     * @param <T>
     *         the type of the desired adapter
     * @return an optional {@link Notifier}
     */
    public static <T extends Adapter> Optional<T> getAdapter(Notifier notifier, Class<T> adapterType) {
        if (notifier != null) {
            return notifier.eAdapters().stream()
                    .filter(adapterType::isInstance)
                    .map(adapterType::cast)
                    .findFirst();
        }
        return Optional.empty();
    }

    /**
     * Gets a stream composed from the object itself and all its content.
     *
     * @param o
     *            an {@link EObject}
     * @return a stream
     */
    public static Stream<EObject> eAllContentStreamWithSelf(EObject o) {
        if (o == null) {
            return Stream.empty();
        }
        return Stream.concat(Stream.of(o), StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(o.eAllContents(), Spliterator.NONNULL), false));
    }

    /**
     * Gets a stream composed from the object itself and all its content.
     *
     * @param r
     *            a resource
     * @return a stream
     */
    public static Stream<Notifier> eAllContentStreamWithSelf(Resource r) {
        if (r == null) {
            return Stream.empty();
        }
        return Stream.concat(Stream.of(r), StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(r.getAllContents(), Spliterator.NONNULL), false));
    }

    /**
     * Gets a stream composed from the object itself and all its content.
     *
     * @param n
     *            a notifier
     * @return a stream
     */
    public static Stream<Notifier> eAllContentStreamWithSelf(Notifier n) {
        Stream<Notifier> result = Stream.empty();
        if (n instanceof Resource resource) {
            result = eAllContentStreamWithSelf(resource);
        } else if (n instanceof EObject eObject) {
            result = eAllContentStreamWithSelf(eObject).map(Notifier.class::cast);
        } else if (n instanceof ResourceSet resourceSet) {
            result = eAllContentStreamWithSelf(resourceSet);
        }
        return result;
    }

    /**
     * Gets a stream composed from the object itself and all its content.
     *
     * @param rs
     *            a resource set
     * @return a stream
     */
    public static Stream<Notifier> eAllContentStreamWithSelf(ResourceSet rs) {
        if (rs == null) {
            return Stream.empty();
        }
        return Stream.concat(Stream.of(rs), StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(rs.getAllContents(), Spliterator.NONNULL), false));
    }

    /**
     * Gets all objects contained in the given notifier with the given type.
     * <p>
     * <i>If self if of the expected type then it belong to the returned stream </i>
     * </p>
     *
     * @param self
     *            a {@link Notifier} (EObject, Resource or ResourceSet)
     * @param type
     *            the type of the element in the returned stream
     * @return a stream
     * @param <T>
     *            type of element in the returned stream
     */
    public static <T extends EObject> Stream<T> allContainedObjectOfType(Notifier self, Class<T> type) {
        final Stream<T> result;
        if (self instanceof EObject eObject) {
            result = eAllContentStreamWithSelf(eObject).filter(e -> type.isInstance(e)).map(e -> type.cast(e));
        } else if (self instanceof Resource resource) {
            result = eAllContentStreamWithSelf(resource).filter(e -> type.isInstance(e)).map(e -> type.cast(e));
        } else if (self instanceof ResourceSet resourceSet) {
            result = eAllContentStreamWithSelf(resourceSet).filter(e -> type.isInstance(e))
                    .map(e -> type.cast(e));
        } else {
            result = Stream.empty();
        }
        return result;
    }

    /**
     * Returns {@code true} if {@code parent} is an ancestor of {@code eObject}.
     * <p>
     * This method includes {@code eObject} as an ancestor of itself. This means that this method will always return
     * {@code true} if {@code parent == eObject}.
     * </p>
     *
     * @param parent
     *            the parent EObject to check
     * @param eObject
     *            the EObject to check
     * @return {@code true} if {@code parent} is an ancestor of {@code eObject}
     */
    public static boolean isAncestor(EObject parent, EObject eObject) {
        return !getAncestors(EObject.class, eObject, ancestor -> Objects.equals(ancestor, parent)).isEmpty();
    }

    /**
     * Gets the first ancestor from the given object which match the predicated and has the expected type.
     *
     * @param <T>
     *            the expected type
     * @param type
     *            the expected type
     * @param object
     *            the source object
     * @param ancestorPredicate
     *            an optional {@link Predicate}
     * @return an ancestor or <code>null</code>
     */
    public static <T extends EObject> List<T> getAncestors(Class<T> type, EObject object, Predicate<EObject> ancestorPredicate) {
        var current = object;
        List<T> results = new ArrayList<>();
        while (current != null) {
            if (type.isInstance(current) && (ancestorPredicate == null || ancestorPredicate.test(current))) {
                results.add((T) current);
            }
            current = current.eContainer();
        }
        return results;
    }

    /**
     * Gets the first ancestor containing the given object with the expected type. An optional predicate can be
     * specified to add more constraint on the searched ancestor..
     *
     * @param <T>
     *            the expected type of the ancestor
     * @param type
     *            the expected type of the ancestor
     * @param object
     *            the source object
     * @param ancestorPredicate
     *            an optional predicate than can be used to add another constraint on the searched ancestor (set to
     *            <code>null</code> if the type constraint is enough)
     * @return a matching ancestor or {@link Optional#empty()}
     */
    public static <T extends EObject> Optional<T> getFirstAncestor(Class<T> type, EObject object, Predicate<EObject> ancestorPredicate) {
        var current = object;
        List<T> results = new ArrayList<>();
        while (current != null) {
            if (type.isInstance(current) && (ancestorPredicate == null || ancestorPredicate.test(current))) {
                return Optional.of((T) current);
            }
            current = current.eContainer();
        }
        return Optional.empty();
    }

    /**
     * Gets all the ancestors of the given object which have the expected type.
     *
     * @param <T>
     *            the expected type
     * @param type
     *            the expected type
     * @param object
     *            the source object
     * @return a list of ancestors
     */
    private static <T extends EObject> List<T> getAncestors(Class<T> type, EObject object) {
        return getAncestors(type, object, null);
    }

    /**
     * Gets the first common ancestor from both given objects with the expected type.
     *
     * @param <T>
     *            the expected ancestor type
     * @param expectedType
     *            the expected ancestor type
     * @param e1
     *            the first {@link EObject}
     * @param e2
     *            the last {@link EObject}
     * @return an ancestor or <code>null</code>
     */
    public static <T extends EObject> T getLeastCommonContainer(Class<T> expectedType, EObject e1, EObject e2) {

        List<T> e1Ancestors = getAncestors(expectedType, e1);
        List<T> e2CommonAncestors = getAncestors(expectedType, e2, container -> e1Ancestors.contains(container));
        if (e2CommonAncestors.isEmpty()) {
            return null;
        } else {
            return e2CommonAncestors.get(0);
        }
    }

    /**
     * Resolve all non derived references in the given {@link ResourceSet}.
     *
     * @param rs
     *            the given {@link ResourceSet}.
     */
    public static void resolveAllNonDerived(ResourceSet rs) {
        List<Resource> resources = rs.getResources();
        for (int i = 0; i < resources.size(); i++) {
            resolveAllNonDerived(resources.get(i));
        }
    }

    private static void resolveAllNonDerived(Resource resource) {
        resource.getContents().forEach(eObject -> resolveAllNonDerived(eObject));
    }

    private static void resolveAllNonDerived(EObject eObject) {
        resolveNonDerivedCrossReferences(eObject);
        for (Iterator<EObject> i = eObject.eAllContents(); i.hasNext();) {
            EObject childEObject = i.next();
            resolveNonDerivedCrossReferences(childEObject);
        }
    }

    private static void resolveNonDerivedCrossReferences(EObject eObject) {
        eObject.eClass().getEAllReferences().stream().filter(e -> !e.isDerived() && !e.isContainment()).forEach(ref -> eObject.eGet(ref, true));
    }
}
