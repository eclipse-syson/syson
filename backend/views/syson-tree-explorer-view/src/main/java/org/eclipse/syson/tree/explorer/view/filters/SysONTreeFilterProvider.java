/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.syson.tree.explorer.view.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.sirius.components.collaborative.trees.api.ITreeFilterProvider;
import org.eclipse.sirius.components.collaborative.trees.api.TreeFilter;
import org.eclipse.sirius.components.trees.description.TreeDescription;
import org.springframework.stereotype.Service;

/**
 * Specific tree filter provider for SysON.
 *
 * @author arichard
 */
@Service
public class SysONTreeFilterProvider implements ITreeFilterProvider {

    public static final String HIDE_MEMBERSHIPS_TREE_ITEM_FILTER_ID = UUID.nameUUIDFromBytes("SysONTreeItemMembershipsFilter".getBytes()).toString();

    public static final String HIDE_KERML_STANDARD_LIBRARIES_TREE_FILTER_ID = UUID.nameUUIDFromBytes("SysONTreeKerMLStandardLibrariesFilter".getBytes()).toString();

    public static final String HIDE_SYSML_STANDARD_LIBRARIES_TREE_FILTER_ID = UUID.nameUUIDFromBytes("SysONTreeSysMLStandardLibrariesFilter".getBytes()).toString();

    public static final String HIDE_ROOT_NAMESPACES_ID = UUID.nameUUIDFromBytes("SysONTreeRootNamespacesFilter".getBytes()).toString();

    @Override
    public List<TreeFilter> get(String editingContextId, TreeDescription treeDescription, String representationId) {
        List<TreeFilter> filters = new ArrayList<>();
        filters.add(new TreeFilter(HIDE_MEMBERSHIPS_TREE_ITEM_FILTER_ID, "Hide Memberships", true));
        filters.add(new TreeFilter(HIDE_KERML_STANDARD_LIBRARIES_TREE_FILTER_ID, "Hide KerML Standard Libraries", false));
        filters.add(new TreeFilter(HIDE_SYSML_STANDARD_LIBRARIES_TREE_FILTER_ID, "Hide SysML Standard Libraries", false));
        filters.add(new TreeFilter(HIDE_ROOT_NAMESPACES_ID, "Hide Root Namespaces", true));
        return filters;
    }
}
