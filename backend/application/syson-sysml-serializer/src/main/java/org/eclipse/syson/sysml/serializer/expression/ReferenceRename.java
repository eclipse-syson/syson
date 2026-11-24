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
package org.eclipse.syson.sysml.serializer.expression;

/**
 * Existing parser spelling and replacement derived from its EMF target.
 *
 * @param original
 *            the parser spelling found in the text
 * @param replacement
 *            the target's current reference name
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
record ReferenceRename(String original, String replacement) {
}
