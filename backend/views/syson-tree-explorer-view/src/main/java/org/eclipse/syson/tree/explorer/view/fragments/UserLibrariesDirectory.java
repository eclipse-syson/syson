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

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.web.application.editingcontext.EditingContext;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.eclipse.syson.tree.explorer.view.services.api.ISysONExplorerFilterService;
import org.eclipse.syson.tree.explorer.view.services.api.ISysONExplorerFragment;

/**
 * The directory containing referenced libraries displayed in the explorer.
 *
 * @author gdaniel
 */
public class UserLibrariesDirectory implements ISysONExplorerFragment {

    private final String id = UUID.nameUUIDFromBytes("SysON_User_Libraries_Directory".getBytes()).toString();

    private String label;

    private final ISysONExplorerFilterService filterService;

    public UserLibrariesDirectory(String label, ISysONExplorerFilterService filterService) {
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
        boolean hasChildren = false;
        if (editingContext instanceof EditingContext siriusWebEditingContext) {
            hasChildren = this.filterService.applyFilters(siriusWebEditingContext.getDomain().getResourceSet().getResources(), activeFilterIds).stream()
                    .filter(Resource.class::isInstance)
                    .map(Resource.class::cast)
                    .anyMatch(resource -> ElementUtil.isImported(resource) && !new UtilService().getLibraries(resource, false).isEmpty());
        }
        return hasChildren;
    }

    @Override
    public List<Object> getChildren(IEditingContext editingContext, List<String> expandedIds, List<String> activeFilterIds) {
        List<Object> result = new ArrayList<>();
        if (editingContext instanceof EditingContext siriusWebEditingContext) {
            this.filterService.applyFilters(siriusWebEditingContext.getDomain().getResourceSet().getResources(), activeFilterIds).stream()
                    .filter(Resource.class::isInstance)
                    .map(Resource.class::cast)
                    .filter(resource -> ElementUtil.isImported(resource) && !new UtilService().getLibraries(resource, false).isEmpty())
                    .forEach(result::add);
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
