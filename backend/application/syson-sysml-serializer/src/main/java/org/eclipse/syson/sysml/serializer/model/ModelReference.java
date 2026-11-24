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

import org.omg.sysml.lang.sysml.Element;

/**
 * A textual reference name and its resolved or proxy target, as returned by {@link ReferenceTargets}. The target is
 * {@code null} only when the name comes from a parser dangling link.
 *
 * @param name
 *            the reference text to write
 * @param element
 *            the referenced element, or {@code null} for an unresolved parser reference
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public record ModelReference(String name, Element element) {
}
