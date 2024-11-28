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
package org.eclipse.syson.tree.explorer.view.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.syson.tree.explorer.view.filters.SysONTreeFilterProvider;
import org.eclipse.syson.tree.explorer.view.services.api.ISysONExplorerFragment;
import org.eclipse.syson.tree.explorer.view.services.api.ISysONExplorerFilterService;

/**
 * The directory containing standard libraries displayed in the explorer.
 *
 * @author gdaniel
 */
public class StandardLibraryDirectory implements ISysONExplorerFragment {

    private final String id = UUID.nameUUIDFromBytes("STD".getBytes()).toString();

    private final ISysONExplorerFilterService filterService;

    private final String label;

    public StandardLibraryDirectory(String label, ISysONExplorerFilterService filterService) {
        this.label = Objects.requireNonNull(label);
        this.filterService = Objects.requireNonNull(filterService);
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    @Override
    public List<String> getIconURL() {
        return List.of("icons/LibraryResource.svg");
    }

    @Override
    public boolean hasChildren(IEditingContext editingContext, List<String> expandedIds, List<String> activeFilterIds) {
        return !activeFilterIds.contains(SysONTreeFilterProvider.HIDE_KERML_STANDARD_LIBRARIES_TREE_FILTER_ID)
                || !activeFilterIds.contains(SysONTreeFilterProvider.HIDE_SYSML_STANDARD_LIBRARIES_TREE_FILTER_ID);
    }

    @Override
    public List<Object> getChildren(IEditingContext editingContext, List<String> expandedIds, List<String> activeFilterIds) {
        List<Object> result = new ArrayList<>();
        if (!activeFilterIds.contains(SysONTreeFilterProvider.HIDE_KERML_STANDARD_LIBRARIES_TREE_FILTER_ID)) {
            result.add(new KerMLStandardLibraryDirectory(this.filterService));
        }
        if (!activeFilterIds.contains(SysONTreeFilterProvider.HIDE_SYSML_STANDARD_LIBRARIES_TREE_FILTER_ID)) {
            result.add(new SysMLStandardLibraryDirectory(this.filterService));
        }
        return result;
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public boolean isDeletable() {
        return false;
    }

    @Override
    public boolean isSelectable() {
        return true;
    }
}
