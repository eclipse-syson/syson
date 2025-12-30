/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
package org.eclipse.syson.tree.explorer.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.collaborative.trees.api.ITreeFilterProvider;
import org.eclipse.sirius.components.collaborative.trees.api.TreeFilter;
import org.eclipse.sirius.components.trees.description.TreeDescription;
import org.eclipse.syson.tree.explorer.filters.SysONTreeFilterConstants;
import org.springframework.stereotype.Service;

/**
 * Specific tree filter provider for SysON.
 *
 * @author arichard
 */
@Service
public class SysONTreeFilterProvider implements ITreeFilterProvider {

    @Override
    public List<TreeFilter> get(String editingContextId, TreeDescription treeDescription) {
        List<TreeFilter> filters = new ArrayList<>();
        if (SysONExplorerTreeDescriptionProvider.SYSON_EXPLORER.equals(treeDescription.getLabel())) {
            filters.add(new TreeFilter(SysONTreeFilterConstants.HIDE_MEMBERSHIPS_TREE_ITEM_FILTER_ID, "Hide Memberships", true));
            filters.add(new TreeFilter(SysONTreeFilterConstants.HIDE_KERML_STANDARD_LIBRARIES_TREE_FILTER_ID, "Hide KerML Standard Libraries", false));
            filters.add(new TreeFilter(SysONTreeFilterConstants.HIDE_SYSML_STANDARD_LIBRARIES_TREE_FILTER_ID, "Hide SysML Standard Libraries", false));
            filters.add(new TreeFilter(SysONTreeFilterConstants.HIDE_USER_LIBRARIES_TREE_FILTER_ID, "Hide User Libraries", false));
            filters.add(new TreeFilter(SysONTreeFilterConstants.HIDE_ROOT_NAMESPACES_ID, "Hide Root Namespaces", true));
            filters.add(new TreeFilter(SysONTreeFilterConstants.HIDE_EXPOSE_ELEMENTS_TREE_ITEM_FILTER_ID, "Hide Expose Elements", true));
        }
        return filters;
    }
}
