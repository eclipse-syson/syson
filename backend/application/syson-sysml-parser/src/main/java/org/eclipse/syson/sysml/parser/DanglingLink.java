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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * Describes one unresolved reference produced while building a parsed model.
 *
 * @param context
 *            the object that owns the unresolved reference
 * @param reference
 *            the unresolved structural reference
 * @param eClass
 *            the expected target type
 * @param targets
 *            the textual qualified names to resolve during linking
 */
public record DanglingLink(EObject context, EReference reference, EClass eClass, List<String> targets) {

}
