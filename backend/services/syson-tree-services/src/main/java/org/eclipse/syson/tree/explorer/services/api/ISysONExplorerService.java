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
package org.eclipse.syson.tree.explorer.services.api;

import java.util.List;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.trees.TreeItem;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.RepresentationMetadata;

/**
 * Interface of the service used by the SysON explorer.
 *
 * @author gdaniel
 */
public interface ISysONExplorerService {

    String getTreeItemId(Object self);

    String getTreeItemTooltip(Object self);

    String getKind(Object self);

    String getLabel(Object self);

    boolean isEditable(Object self);

    boolean isDeletable(Object self);

    boolean isSelectable(Object self);

    List<String> getImageURL(Object self);

    Object getTreeItemObject(String treeItemId, IEditingContext editingContext);

    Object getParent(Object self, String treeItemId, IEditingContext editingContext);

    boolean hasChildren(Object self, IEditingContext editingContext, List<RepresentationMetadata> existingRepresentations, List<String> expandedIds, List<String> activeFilterIds);

    List<Object> getChildren(Object self, IEditingContext editingContext, List<RepresentationMetadata> existingRepresentations, List<String> expandedIds, List<String> activeFilterIds);

    boolean canExpandAll(TreeItem treeItem, IEditingContext editingContext);

    boolean canCreateNewObjectsFromText(Object self);

    List<Object> getElements(IEditingContext editingContext, List<String> activeFilterIds);

    String getType(Object self);

    String getShortName(Object self);

    String getReadOnlyTag(Object self);

    String getLibraryLabel(Object self);

    /**
     * NoOp implementation.
     *
     * @author Arthur Daussy
     */
    class NoOp implements ISysONExplorerService {

        @Override
        public String getTreeItemId(Object self) {
            return "";
        }

        @Override
        public String getTreeItemTooltip(Object self) {
            return "";
        }

        @Override
        public String getKind(Object self) {
            return "";
        }

        @Override
        public String getLabel(Object self) {
            return "";
        }

        @Override
        public boolean isEditable(Object self) {
            return false;
        }

        @Override
        public boolean isDeletable(Object self) {
            return false;
        }

        @Override
        public boolean isSelectable(Object self) {
            return false;
        }

        @Override
        public List<String> getImageURL(Object self) {
            return List.of();
        }

        @Override
        public Object getTreeItemObject(String treeItemId, org.eclipse.sirius.components.core.api.IEditingContext editingContext) {
            return null;
        }

        @Override
        public Object getParent(Object self, String treeItemId, org.eclipse.sirius.components.core.api.IEditingContext editingContext) {
            return null;
        }

        @Override
        public boolean hasChildren(Object self, org.eclipse.sirius.components.core.api.IEditingContext editingContext, List<RepresentationMetadata> existingRepresentations, List<String> expandedIds,
                List<String> activeFilterIds) {
            return false;
        }

        @Override
        public List<Object> getChildren(Object self, org.eclipse.sirius.components.core.api.IEditingContext editingContext, List<RepresentationMetadata> existingRepresentations,
                List<String> expandedIds, List<String> activeFilterIds) {
            return List.of();
        }

        @Override
        public boolean canExpandAll(TreeItem treeItem, org.eclipse.sirius.components.core.api.IEditingContext editingContext) {
            return false;
        }

        @Override
        public boolean canCreateNewObjectsFromText(Object self) {
            return false;
        }

        @Override
        public List<Object> getElements(org.eclipse.sirius.components.core.api.IEditingContext editingContext, List<String> activeFilterIds) {
            return List.of();
        }

        @Override
        public String getType(Object self) {
            return "";
        }

        @Override
        public String getShortName(Object self) {
            return "";
        }

        @Override
        public String getReadOnlyTag(Object self) {
            return "";
        }

        @Override
        public String getLibraryLabel(Object self) {
            return "";
        }
    }

}
