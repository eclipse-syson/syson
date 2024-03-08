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
package org.eclipse.syson.application.configuration;

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

    @Override
    public List<TreeFilter> get(String editingContextId, TreeDescription treeDescription, String representationId) {
        List<TreeFilter> filters = new ArrayList<>();
        filters.add(new TreeFilter(HIDE_MEMBERSHIPS_TREE_ITEM_FILTER_ID, "Hide Memberships", true));
        return filters;
    }
}
