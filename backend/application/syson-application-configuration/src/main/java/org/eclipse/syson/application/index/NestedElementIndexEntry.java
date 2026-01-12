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
package org.eclipse.syson.application.index;

/**
 * An index entry representing a nested {@link org.eclipse.syson.sysml.Element}.
 *
 * <p>
 * This record does not contain recursive {@link INestedIndexEntry} in order to avoid infinite entry nesting when converting elements into index entries.
 * See {@link INestedIndexEntry} for more information.
 * </p>
 *
 * @param id the identifier of the element
 * @param type the name of the concrete SysML type of the element
 * @param label the label of the element
 * @param name the name of the element
 * @param shortName the short name of the element
 * @param qualifiedName the qualified name of the element
 *
 * @author gdaniel
 */
public record NestedElementIndexEntry(
        String id,
        String type,
        String label,
        String name,
        String shortName,
        String qualifiedName
) implements INestedIndexEntry {
}
