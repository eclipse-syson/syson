/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.util;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.sysml.Element;

/**
 * Simple implementation of a proxy.
 *
 * @author Arthur Daussy
 */
public record NamedProxy(Element context, EReference ref, String nameToResolve) {
}
