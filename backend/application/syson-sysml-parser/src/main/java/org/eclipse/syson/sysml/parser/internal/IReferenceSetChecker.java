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
package org.eclipse.syson.sysml.parser.internal;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * Determines whether a reference has been explicitly set.
 *
 * @author Arthur Daussy
 */
@FunctionalInterface
public interface IReferenceSetChecker {

    /**
     * Tests whether the given reference has been explicitly set on an object.
     *
     * @param object
     *         the object holding the reference
     * @param reference
     *         the reference to test
     * @return {@code true} if the reference has been explicitly set
     */
    boolean isSet(EObject object, EReference reference);
}
