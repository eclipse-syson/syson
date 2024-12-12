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
import org.eclipse.syson.tree.explorer.view.services.api.ISysONExplorerFilterService;
import org.eclipse.syson.tree.explorer.view.services.api.ISysONExplorerFragment;

/**
 * The directory containing standard libraries displayed in the explorer.
 *
 * @author gdaniel
 */
public class LibrariesDirectory implements ISysONExplorerFragment {

    private final String id = UUID.nameUUIDFromBytes("SysON_Libraries_Directory".getBytes()).toString();

    private final ISysONExplorerFilterService filterService;

    private final String label;

    public LibrariesDirectory(String label, ISysONExplorerFilterService filterService) {
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
        boolean result = !activeFilterIds.contains(SysONTreeFilterProvider.HIDE_KERML_STANDARD_LIBRARIES_TREE_FILTER_ID)
                || !activeFilterIds.contains(SysONTreeFilterProvider.HIDE_SYSML_STANDARD_LIBRARIES_TREE_FILTER_ID);
        if (!result) {
            // Check if the user libraries directory contains children, we don't want to display the libraries directory
            // if it only contains empty directories.
            UserLibrariesDirectory userLibrariesDirectory = new UserLibrariesDirectory("User Libraries", this.filterService);
            result = !activeFilterIds.contains(SysONTreeFilterProvider.HIDE_USER_LIBRARIES_TREE_FILTER_ID)
                    && userLibrariesDirectory.hasChildren(editingContext, expandedIds, activeFilterIds);
        }
        return result;
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
        if (!activeFilterIds.contains(SysONTreeFilterProvider.HIDE_USER_LIBRARIES_TREE_FILTER_ID)) {
            UserLibrariesDirectory userLibrariesDirectory = new UserLibrariesDirectory("User libraries", this.filterService);
            if (userLibrariesDirectory.hasChildren(editingContext, expandedIds, activeFilterIds)) {
                // Add the user libraries directory only if it contains children.
                result.add(userLibrariesDirectory);
            }
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
