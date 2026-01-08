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

import java.util.List;

import org.eclipse.sirius.web.application.index.services.api.IIndexEntry;

/**
 * TODO
 *
 * @author gdaniel
 */
public record TypeIndexEntry(
        String editingContextId,
        String id,
        String type,
        String label,
        List<String> iconURLs,
        String name,
        String shortName,
        String qualifiedName,
        INestedIndexEntry owner,
        List<INestedIndexEntry> ownedSpecialization,
        List<INestedIndexEntry> ownedElement
) implements IIndexEntry {
}
