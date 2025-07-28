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
package org.eclipse.syson.tree.explorer.view.services;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.eclipse.sirius.components.collaborative.trees.api.ITreePathProvider;
import org.eclipse.sirius.components.collaborative.trees.dto.TreePath;
import org.eclipse.sirius.components.collaborative.trees.dto.TreePathInput;
import org.eclipse.sirius.components.collaborative.trees.dto.TreePathSuccessPayload;
import org.eclipse.sirius.components.collaborative.trees.services.api.ITreeNavigationService;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.core.api.IURLParser;
import org.eclipse.sirius.components.representations.VariableManager;
import org.eclipse.sirius.components.trees.Tree;
import org.eclipse.sirius.components.trees.description.TreeDescription;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.tree.explorer.view.SysONExplorerTreeDescriptionProvider;
import org.eclipse.syson.tree.explorer.view.filters.SysONTreeFilterProvider;
import org.eclipse.syson.tree.explorer.view.services.api.ISysONExplorerFilterService;
import org.springframework.stereotype.Service;

/**
 * ITreePathProvider implementation for the SysON Explorer tree. This version handles the SysON Explorer filters.
 * 
 * @author arichard
 */
@Service
public class SysONExplorerTreePathProvider implements ITreePathProvider {

    private final ITreeNavigationService treeNavigationService;

    private final IURLParser urlParser;

    private final ISysONExplorerFilterService filterService;

    private final IRepresentationDescriptionSearchService representationDescriptionSearchService;

    private final UtilService utilService = new UtilService();

    public SysONExplorerTreePathProvider(ITreeNavigationService treeNavigationService, IURLParser urlParser, ISysONExplorerFilterService filterService,
            IRepresentationDescriptionSearchService representationDescriptionSearchService) {
        this.treeNavigationService = Objects.requireNonNull(treeNavigationService);
        this.urlParser = Objects.requireNonNull(urlParser);
        this.filterService = Objects.requireNonNull(filterService);
        this.representationDescriptionSearchService = Objects.requireNonNull(representationDescriptionSearchService);
    }

    @Override
    public boolean canHandle(Tree tree) {
        if (tree != null) {
            var sysonExplorerId = UUID.nameUUIDFromBytes(SysONExplorerTreeDescriptionProvider.SYSON_EXPLORER.getBytes()).toString();
            var parameters = this.urlParser.getParameterValues(tree.getDescriptionId());
            var sourceId = parameters.get("sourceId").get(0);
            return Objects.equals(sysonExplorerId, sourceId);
        }
        return false;
    }

    @Override
    public IPayload handle(IEditingContext editingContext, Tree tree, TreePathInput input) {
        int maxDepth = 0;
        Set<String> allAncestors = new LinkedHashSet<>();
        for (String selectionEntryId : input.selectionEntryIds()) {
            var itemAncestors = this.treeNavigationService.getAncestors(editingContext, tree, selectionEntryId);
            var optTreeDescription = this.getTreeDescription(editingContext, tree.getDescriptionId());
            if (!itemAncestors.isEmpty() && optTreeDescription.isPresent()) {
                var parameters = this.urlParser.getParameterValues(tree.getId());
                var activeFilterIdsParam = parameters.get("activeFilterIds").get(0);
                var activeFilterIds = this.urlParser.getParameterEntries(activeFilterIdsParam);
                var itemAncestorsObjects = new ArrayList<Object>();
                for (String itemAncestor : itemAncestors) {
                    this.getTreeItemObject(editingContext, optTreeDescription.get(), tree, itemAncestor).ifPresent(itemAncestorsObjects::add);
                }
                var filteredItemAncestorsIds = new ArrayList<String>();
                var filteredItemAncestorsObjects = this.applyFilters(itemAncestorsObjects, activeFilterIds);
                for (Object filteredItemAncestorsObject : filteredItemAncestorsObjects) {
                    this.getItemId(editingContext, optTreeDescription.get(), tree, filteredItemAncestorsObject).ifPresent(filteredItemAncestorsIds::add);
                }
                allAncestors.addAll(filteredItemAncestorsIds);
                maxDepth = Math.max(maxDepth, filteredItemAncestorsIds.size());
            }
        }
        return new TreePathSuccessPayload(input.id(), new TreePath(allAncestors.stream().toList(), maxDepth));
    }

    private Optional<String> getItemId(IEditingContext editingContext, TreeDescription treeDescription, Tree tree, Object object) {
        if (treeDescription != null) {
            var variableManager = new VariableManager();
            variableManager.put(VariableManager.SELF, object);
            return Optional.of(treeDescription.getTreeItemIdProvider().apply(variableManager));
        }
        return Optional.empty();
    }

    private Optional<Object> getTreeItemObject(IEditingContext editingContext, TreeDescription treeDescription, Tree tree, String id) {
        if (treeDescription != null) {
            var variableManager = new VariableManager();
            variableManager.put(IEditingContext.EDITING_CONTEXT, editingContext);
            variableManager.put(TreeDescription.ID, id);
            return Optional.ofNullable(treeDescription.getTreeItemObjectProvider().apply(variableManager));
        }
        return Optional.empty();
    }

    private Optional<TreeDescription> getTreeDescription(IEditingContext editingContext, String descriptionId) {
        return this.representationDescriptionSearchService.findById(editingContext, descriptionId)
                .filter(TreeDescription.class::isInstance)
                .map(TreeDescription.class::cast);
    }

    private List<Object> applyFilters(List<Object> elements, List<String> activeFilterIds) {
        var alteredElements = new ArrayList<Object>(elements);
        if (activeFilterIds.contains(SysONTreeFilterProvider.HIDE_MEMBERSHIPS_TREE_ITEM_FILTER_ID)) {
            alteredElements.removeIf(Membership.class::isInstance);
        }
        if (activeFilterIds.contains(SysONTreeFilterProvider.HIDE_KERML_STANDARD_LIBRARIES_TREE_FILTER_ID)) {
            alteredElements.removeIf(this.filterService::isKerMLStandardLibrary);
        }
        if (activeFilterIds.contains(SysONTreeFilterProvider.HIDE_SYSML_STANDARD_LIBRARIES_TREE_FILTER_ID)) {
            alteredElements.removeIf(this.filterService::isSysMLStandardLibrary);
        }
        if (activeFilterIds.contains(SysONTreeFilterProvider.HIDE_USER_LIBRARIES_TREE_FILTER_ID)) {
            alteredElements.removeIf(this.filterService::isUserLibrary);
        }
        if (activeFilterIds.contains(SysONTreeFilterProvider.HIDE_ROOT_NAMESPACES_ID)) {
            alteredElements.removeIf(e -> e instanceof Namespace ns && this.utilService.isRootNamespace(ns));
        }
        return alteredElements;
    }
}
