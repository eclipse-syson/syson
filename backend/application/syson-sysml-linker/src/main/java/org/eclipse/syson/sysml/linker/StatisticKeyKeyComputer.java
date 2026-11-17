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
package org.eclipse.syson.sysml.linker;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * Computes stable keys used to aggregate link-resolution statistics by EMF containment topology and reference.
 * <p>
 * A key identifies the type of the object holding the reference, up to five of its containing types, and the name of
 * the reference being resolved. It intentionally does not use element names or identifiers so that equivalent link
 * locations in different models share the same statistics entry.
 * </p>
 *
 * @author Arthur Daussy
 */
public class StatisticKeyKeyComputer {

    /**
     * Computes the statistics key for the given object and reference.
     * <p>
     * When an object is provided, its key portion consists of up to five containing {@link org.eclipse.emf.ecore.EClass
     * EClass} names, ordered from the outermost retained container to the immediate container, followed by the object's
     * own {@link org.eclipse.emf.ecore.EClass EClass} name. When a reference is provided, its name is appended after a
     * semicolon.
     * </p>
     *
     * @param eObj
     *            the object holding the reference, or <code>null</code> to omit the containment topology
     * @param reference
     *            the reference represented by the key, or <code>null</code> to omit the reference name
     * @return the statistics key, or an empty string when both arguments are <code>null</code>
     */
    public String computeStatKey(EObject eObj, EReference reference) {
        StringBuilder res = new StringBuilder();

        if (eObj != null) {
            EObject firstContainer = eObj.eContainer();
            EObject secondContainer = getContainer(firstContainer);
            EObject thirdContainer = getContainer(secondContainer);
            EObject fourthContainer = getContainer(thirdContainer);
            EObject fifthContainer = getContainer(fourthContainer);
            appendContainerType(res, fifthContainer);
            appendContainerType(res, fourthContainer);
            appendContainerType(res, thirdContainer);
            appendContainerType(res, secondContainer);
            appendContainerType(res, firstContainer);
            res.append(eObj.eClass().getName());
        }
        if (reference != null) {
            res.append(";");
            res.append(reference.getName());
        }

        return res.toString();
    }

    /**
     * Gets the container used to compute a statistic key.
     * 
     * @param eObject
     *            the {@link EObject}
     * @return the container used to compute a statistic key
     */
    private EObject getContainer(EObject eObject) {
        EObject result = null;
        if (eObject != null) {
            result = eObject.eContainer();
        }
        return result;
    }

    /**
     * Appends a container type to a statistic key.
     * 
     * @param builder
     *            the {@link StringBuilder}
     * @param container
     *            the container {@link EObject}
     */
    private void appendContainerType(StringBuilder builder, EObject container) {
        if (container != null) {
            builder.append(container.eClass().getName());
            builder.append(".");
        }
    }
}
