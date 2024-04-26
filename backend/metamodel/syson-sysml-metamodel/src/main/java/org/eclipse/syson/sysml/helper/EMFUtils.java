/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * Utils class to browse or edit EMF model.
 *
 * @author Arthur Daussy
 */
public class EMFUtils {

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
     * Gets a stream composed from the object itself and all its content.
     *
     * @param o
     *          an {@link EObject}
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
     *          a resource
     * @return a stream
     */
    public static Stream<Notifier> eAllContentSteamWithSelf(Resource r) {
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
     *          a notifier
     * @return a stream
     */
    public static Stream<Notifier> eAllContentSteamWithSelf(Notifier n) {
        Stream<Notifier> result = Stream.empty();
        if (n instanceof Resource resource) {
            result = eAllContentSteamWithSelf(resource);
        } else if (n instanceof EObject eObject) {
            result = eAllContentStreamWithSelf(eObject).map(Notifier.class::cast);
        } else if (n instanceof ResourceSet resourceSet) {
            result = eAllContentSteamWithSelf(resourceSet);
        }
        return result;
    }

    /**
     * Gets a stream composed from the object itself and all its content.
     *
     * @param rs
     *           a resource set
     * @return a stream
     */
    public static Stream<Notifier> eAllContentSteamWithSelf(ResourceSet rs) {
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
     *             a {@link Notifier} (EObject, Resource or ResourceSet)
     * @param type
     *             the type of the element in the returned stream
     * @return a stream
     * @param <T>
     *            type of element in the returned stream
     */
    public static <T extends EObject> Stream<T> allContainedObjectOfType(Notifier self, Class<T> type) {
        final Stream<T> result;
        if (self instanceof EObject) {
            result = eAllContentStreamWithSelf((EObject) self).filter(e -> type.isInstance(e)).map(e -> type.cast(e));
        } else if (self instanceof Resource) {
            result = eAllContentSteamWithSelf((Resource) self).filter(e -> type.isInstance(e)).map(e -> type.cast(e));
        } else if (self instanceof ResourceSet) {
            result = eAllContentSteamWithSelf((ResourceSet) self).filter(e -> type.isInstance(e))
                    .map(e -> type.cast(e));
        } else {
            result = Stream.empty();
        }
        return result;
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

}
