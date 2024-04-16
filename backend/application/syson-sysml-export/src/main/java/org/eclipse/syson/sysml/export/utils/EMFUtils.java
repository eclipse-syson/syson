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
package org.eclipse.syson.sysml.export.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.eclipse.emf.ecore.EObject;

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
