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
 * An index entry representing a {@link org.eclipse.syson.sysml.Namespace}.
 *
 * <p>
 * This record contains relevant data related to a SysML Namespace (e.g. name, short name, qualified name), as well as technical information used by Sirius Web to present index entries to the end user
 * (label, icons, type).
 * </p>
 * <p>
 * This class contains {@link INestedIndexEntry}, which are additional objects serialized as sub-fields of this index entry, which allows to access information related to elements connected to the
 * namespace (e.g. {@code owner.name} to access the name of the owner, or {@code ownedElement.name} to access the name of the owned elements).
 * </p>
 *
 * @param editingContextId the identifier of the editing context containing the namespace
 * @param id the identifier of the namespace
 * @param type the name of the concrete SysML type of the namespace
 * @param label the label of the namespace
 * @param iconURLs the URLs of the icons of the namespace
 * @param name the name of the namespace
 * @param shortName the short name of the namespace
 * @param qualifiedName the qualified name of the namespace
 * @param owner the nested entry holding information related to the namespace's owner
 * @param ownedElement the nested entries holding information related to the namespace owned elements
 *
 * @author gdaniel
 */
public record NamespaceIndexEntry(
        String editingContextId,
        String id,
        String type,
        String label,
        List<String> iconURLs,
        String name,
        String shortName,
        String qualifiedName,
        INestedIndexEntry owner,
        List<INestedIndexEntry> ownedElement
) implements IIndexEntry {
}
