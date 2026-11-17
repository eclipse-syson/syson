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

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.sysml.parser.internal.IReferenceSetChecker;

/**
 * Checks reference settings from the qualified names recorded by the parser, with a fallback to the unresolved EMF value.
 *
 * @author ylussaud
 */
public class MapBasedReferenceSetChecker implements IReferenceSetChecker {

    private final Map<EObject, Map<EReference, Set<QualifiedName>>> qualifiedNames;

    public MapBasedReferenceSetChecker(Map<EObject, Map<EReference, Set<QualifiedName>>> qualifiedNames) {
        this.qualifiedNames = Objects.requireNonNull(qualifiedNames);
    }

    @Override
    public boolean isSet(EObject object, EReference reference) {
        Map<EReference, Set<QualifiedName>> references = this.qualifiedNames.get(object);
        if (references != null) {
            Set<QualifiedName> names = references.get(reference);
            if (names != null && !names.isEmpty()) {
                return true;
            }
        }
        return object.eGet(reference, false) != null;
    }
}
