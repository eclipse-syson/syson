/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.tree.explorer.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.trees.TreeItem;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.RepresentationMetadata;
import org.eclipse.syson.tree.explorer.services.api.ISysONDefaultExplorerService;
import org.eclipse.syson.tree.explorer.services.api.ISysONExplorerService;
import org.eclipse.syson.tree.explorer.services.api.ISysONExplorerServiceDelegate;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link ISysONExplorerService} which delegates to {@link ISysONExplorerServiceDelegate}.
 * <p>
 * {@link ISysONDefaultExplorerService} is used as fallback if there is no {@link ISysONExplorerServiceDelegate} to
 * delegate to.
 * </p>
 *
 * @author gdaniel
 */
@Service
public class ComposedSysONExplorerService implements ISysONExplorerService {

    private final List<ISysONExplorerServiceDelegate> explorerServiceDelegate;

    private final ISysONDefaultExplorerService defaultExplorerService;

    public ComposedSysONExplorerService(List<ISysONExplorerServiceDelegate> explorerServiceDelegate, ISysONDefaultExplorerService defaultExplorerService) {
        this.explorerServiceDelegate = Objects.requireNonNull(explorerServiceDelegate);
        this.defaultExplorerService = Objects.requireNonNull(defaultExplorerService);
    }

    @Override
    public String getTreeItemId(Object self) {
        return this.getDelegate(self)
                .map(delegate -> delegate.getTreeItemId(self))
                .orElseGet(() -> this.defaultExplorerService.getTreeItemId(self));
    }

    @Override
    public String getKind(Object self) {
        return this.getDelegate(self)
                .map(delegate -> delegate.getKind(self))
                .orElseGet(() -> this.defaultExplorerService.getKind(self));
    }

    @Override
    public String getLabel(Object self) {
        return this.getDelegate(self)
                .map(delegate -> delegate.getLabel(self))
                .orElseGet(() -> this.defaultExplorerService.getLabel(self));
    }

    @Override
    public boolean isEditable(Object self) {
        return this.getDelegate(self)
                .map(delegate -> delegate.isEditable(self))
                .orElseGet(() -> this.defaultExplorerService.isEditable(self));
    }

    @Override
    public boolean isDeletable(Object self) {
        return this.getDelegate(self)
                .map(delegate -> delegate.isDeletable(self))
                .orElseGet(() -> this.defaultExplorerService.isDeletable(self));
    }

    @Override
    public boolean isSelectable(Object self) {
        return this.getDelegate(self)
                .map(delegate -> delegate.isSelectable(self))
                .orElseGet(() -> this.defaultExplorerService.isSelectable(self));
    }

    @Override
    public List<String> getImageURL(Object self) {
        return this.getDelegate(self)
                .map(delegate -> delegate.getImageURL(self))
                .orElseGet(() -> this.defaultExplorerService.getImageURL(self));
    }

    @Override
    public Object getTreeItemObject(String treeItemId, IEditingContext editingContext) {
        return this.getDelegate(editingContext)
                .map(delegate -> delegate.getTreeItemObject(treeItemId, editingContext))
                .orElseGet(() -> this.defaultExplorerService.getTreeItemObject(treeItemId, editingContext));
    }

    @Override
    public Object getParent(Object self, String treeItemId, IEditingContext editingContext) {
        return this.getDelegate(self)
                .map(delegate -> delegate.getParent(self, treeItemId, editingContext))
                .orElseGet(() -> this.defaultExplorerService.getParent(self, treeItemId, editingContext));
    }

    @Override
    public boolean hasChildren(Object self, IEditingContext editingContext, List<RepresentationMetadata> existingRepresentations, List<String> expandedIds, List<String> activeFilterIds) {
        return this.getDelegate(self)
                .map(delegate -> delegate.hasChildren(self, editingContext, existingRepresentations, expandedIds, activeFilterIds))
                .orElseGet(() -> this.defaultExplorerService.hasChildren(self, editingContext, existingRepresentations, expandedIds, activeFilterIds));
    }

    @Override
    public List<Object> getChildren(Object self, IEditingContext editingContext, List<RepresentationMetadata> existingRepresentations, List<String> expandedIds, List<String> activeFilterIds) {
        return this.getDelegate(self)
                .map(delegate -> delegate.getChildren(self, editingContext, existingRepresentations, expandedIds, activeFilterIds))
                .orElseGet(() -> this.defaultExplorerService.getChildren(self, editingContext, existingRepresentations, expandedIds, activeFilterIds));
    }

    @Override
    public boolean canExpandAll(TreeItem treeItem, IEditingContext editingContext) {
        return this.getDelegate(editingContext)
                .map(delegate -> delegate.canExpandAll(treeItem, editingContext))
                .orElseGet(() -> this.defaultExplorerService.canExpandAll(treeItem, editingContext));
    }

    @Override
    public boolean canCreateNewObjectsFromText(Object self) {
        return this.getDelegate(self)
                .map(delegate -> delegate.canCreateNewObjectsFromText(self))
                .orElseGet(() -> this.defaultExplorerService.canCreateNewObjectsFromText(self));
    }

    @Override
    public List<Object> getElements(IEditingContext editingContext, List<String> activeFilterIds) {
        return this.getDelegate(editingContext)
                .map(delegate -> delegate.getElements(editingContext, activeFilterIds))
                .orElseGet(() -> this.defaultExplorerService.getElements(editingContext, activeFilterIds));
    }

    @Override
    public String getType(Object self) {
        return this.getDelegate(self)
                .map(delegate -> delegate.getType(self))
                .orElseGet(() -> this.defaultExplorerService.getType(self));
    }

    @Override
    public String getShortName(Object self) {
        return this.getDelegate(self)
                .map(delegate -> delegate.getShortName(self))
                .orElseGet(() -> this.defaultExplorerService.getShortName(self));
    }

    @Override
    public String getReadOnlyTag(Object self) {
        return this.getDelegate(self)
                .map(delegate -> delegate.getReadOnlyTag(self))
                .orElseGet(() -> this.defaultExplorerService.getReadOnlyTag(self));
    }

    @Override
    public String getLibraryLabel(Object self) {
        return this.getDelegate(self)
                .map(delegate -> delegate.getLibraryLabel(self))
                .orElseGet(() -> this.defaultExplorerService.getLibraryLabel(self));
    }


    private Optional<ISysONExplorerServiceDelegate> getDelegate(Object object) {
        return this.explorerServiceDelegate.stream()
                .filter(delegate -> delegate.canHandle(object))
                .findFirst();
    }

}
