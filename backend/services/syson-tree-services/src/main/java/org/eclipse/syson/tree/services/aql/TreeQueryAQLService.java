/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.tree.services.aql;

import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.trees.TreeItem;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.RepresentationMetadata;
import org.eclipse.syson.tree.explorer.services.api.ISysONExplorerService;

/**
 * Entry point for all tree-related services doing queries in trees and called by AQL expressions in tree descriptions.
 *
 * @author arichard
 */
public class TreeQueryAQLService {

    private final ISysONExplorerService sysonExplorerService;

    public TreeQueryAQLService(ISysONExplorerService sysonExplorerService) {
        this.sysonExplorerService = Objects.requireNonNull(sysonExplorerService);
    }

    public String getTreeItemId(Object self) {
        return this.sysonExplorerService.getTreeItemId(self);
    }

    public String getKind(Object self) {
        return this.sysonExplorerService.getKind(self);
    }

    public String getLabel(Object self) {
        return this.sysonExplorerService.getLabel(self);
    }

    public boolean isEditable(Object self) {
        return this.sysonExplorerService.isEditable(self);
    }

    public boolean isDeletable(Object self) {
        return this.sysonExplorerService.isDeletable(self);
    }

    public boolean isSelectable(Object self) {
        return this.sysonExplorerService.isSelectable(self);
    }

    public List<String> getImageURL(Object self) {
        return this.sysonExplorerService.getImageURL(self);
    }

    public Object getTreeItemObject(String treeItemId, IEditingContext editingContext) {
        return this.sysonExplorerService.getTreeItemObject(treeItemId, editingContext);
    }

    public Object getParent(Object self, String treeItemId, IEditingContext editingContext) {
        return this.sysonExplorerService.getParent(self, treeItemId, editingContext);
    }

    public boolean hasChildren(Object self, IEditingContext editingContext, List<RepresentationMetadata> existingRepresentations, List<String> expandedIds, List<String> activeFilterIds) {
        return this.sysonExplorerService.hasChildren(self, editingContext, existingRepresentations, expandedIds, activeFilterIds);
    }

    public List<Object> getChildren(Object self, IEditingContext editingContext, List<RepresentationMetadata> existingRepresentations, List<String> expandedIds, List<String> activeFilterIds) {
        return this.sysonExplorerService.getChildren(self, editingContext, existingRepresentations, expandedIds, activeFilterIds);
    }

    public boolean canExpandAll(TreeItem treeItem, IEditingContext editingContext) {
        return this.sysonExplorerService.canExpandAll(treeItem, editingContext);
    }

    public boolean canCreateNewObjectsFromText(Object self) {
        return this.sysonExplorerService.canCreateNewObjectsFromText(self);
    }

    public List<Object> getElements(IEditingContext editingContext, List<String> activeFilterIds) {
        return this.sysonExplorerService.getElements(editingContext, activeFilterIds);
    }

    public String getType(Object self) {
        return this.sysonExplorerService.getType(self);
    }

    public String getShortName(Object self) {
        return this.sysonExplorerService.getShortName(self);
    }

    public String getReadOnlyTag(Object self) {
        return this.sysonExplorerService.getReadOnlyTag(self);
    }

    public String getLibraryLabel(Object self) {
        return this.sysonExplorerService.getLibraryLabel(self);
    }
}
