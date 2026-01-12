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
 * An index entry representing a nested {@link org.eclipse.syson.sysml.Specialization}.
 *
 * <p>
 * This record contains a {@link NestedElementIndexEntry} which allows to access information related to the {@code general} element of the specialization. Note that this nested element is not
 * recursive, preventing infinite entry nesting when converting elements into index entries. See {@link INestedIndexEntry} for more information.
 * </p>
 *
 * @param id the identifier of the specialization
 * @param type the name of the concrete SysML type of the specialization
 * @param label the label of the specialization
 * @param name the name of the specialization
 * @param shortName the short name of the specialization
 * @param qualifiedName the qualified name of the specialization
 *
 * @author gdaniel
 */
public record NestedSpecializationIndexEntry(
        String id,
        String type,
        String label,
        String name,
        String shortName,
        String qualifiedName,
        NestedElementIndexEntry general
) implements INestedIndexEntry {
}
