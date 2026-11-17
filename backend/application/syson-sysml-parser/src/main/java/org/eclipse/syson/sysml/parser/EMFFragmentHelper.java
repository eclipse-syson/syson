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
package org.eclipse.syson.sysml.parser;

import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.util.SegmentSequence;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;

/**
 * Utility methods for the containment-based EMF URI fragments used by the parser and linker tests.
 *
 * @author Arthur Daussy
 */
public final class EMFFragmentHelper {

    /**
     * Hidden constructor.
     */
    private EMFFragmentHelper() {
        // Prevent instantiation.
    }

    /**
     * Gets a stable containment fragment for the given {@link EObject}.
     *
     * @param eObject
     *            the object to locate
     * @return the containment fragment of the object
     */
    public static String getURIFragment(EObject eObject) {
        String result;

        InternalEObject internalEObject = (InternalEObject) eObject;
        if (internalEObject.eInternalContainer() == null) {
            result = "/";
        } else {
            SegmentSequence.Builder builder = SegmentSequence.newBuilder("/");
            for (InternalEObject container = internalEObject.eInternalContainer(); container != null; container = internalEObject.eInternalContainer()) {
                builder.append(container.eURIFragmentSegment(internalEObject.eContainingFeature(), internalEObject));
                internalEObject = container;
            }

            builder.append("");
            builder.reverse();
            result = builder.toSegmentSequence().toString();
        }

        return result;
    }

    /**
     * Finds an {@link EObject} from a root object and a containment fragment.
     *
     * @param root
     *            the root object
     * @param fragment
     *            the containment fragment
     * @return the object if the fragment can be resolved
     */
    public static Optional<EObject> findEObject(EObject root, String fragment) {
        Optional<EObject> result;

        if ("/".equals(fragment)) {
            result = Optional.of(root);
        } else if (fragment == null || !fragment.startsWith("/")) {
            result = Optional.empty();
        } else {
            EObject current = root;
            for (String segment : fragment.substring(1).split("/")) {
                Optional<EObject> next = getChild(current, segment);
                if (next.isEmpty()) {
                    current = null;
                    break;
                }
                current = next.get();
            }
            result = Optional.ofNullable(current);
        }

        return result;
    }

    /**
     * Gets the child object identified by one fragment segment.
     *
     * @param current
     *            the current object
     * @param segment
     *            the fragment segment
     * @return the child if the segment can be resolved
     */
    private static Optional<EObject> getChild(EObject current, String segment) {
        Optional<EObject> result = Optional.empty();

        String[] referenceAndIndex = segment.split("\\.");
        if (referenceAndIndex.length == 2 && referenceAndIndex[0].startsWith("@")) {
            EReference reference = (EReference) current.eClass().getEStructuralFeature(referenceAndIndex[0].substring(1));
            if (reference != null) {
                try {
                    int index = Integer.parseInt(referenceAndIndex[1]);
                    result = getReferencedObject(current, reference, index);
                } catch (NumberFormatException exception) {
                    result = Optional.empty();
                }
            }
        }

        return result;
    }

    /**
     * Gets the referenced object for the given reference and index.
     *
     * @param current
     *            the current object
     * @param reference
     *            the reference to navigate
     * @param index
     *            the value index
     * @return the referenced object if any
     */
    private static Optional<EObject> getReferencedObject(EObject current, EReference reference, int index) {
        Optional<EObject> result = Optional.empty();

        if (reference.isMany()) {
            List<?> values = (List<?>) current.eGet(reference);
            if (index >= 0 && index < values.size()) {
                result = Optional.ofNullable((EObject) values.get(index));
            }
        } else if (index == 0) {
            result = Optional.ofNullable((EObject) current.eGet(reference));
        }

        return result;
    }
}
