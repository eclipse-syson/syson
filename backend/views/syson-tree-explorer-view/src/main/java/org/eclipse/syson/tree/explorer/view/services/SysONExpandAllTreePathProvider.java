/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.sirius.components.collaborative.trees.api.IExpandAllTreePathProvider;
import org.eclipse.sirius.components.collaborative.trees.dto.ExpandAllTreePathInput;
import org.eclipse.sirius.components.collaborative.trees.dto.ExpandAllTreePathSuccessPayload;
import org.eclipse.sirius.components.collaborative.trees.dto.TreePath;
import org.eclipse.sirius.components.collaborative.trees.services.api.ITreeNavigationService;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IURLParser;
import org.eclipse.sirius.components.trees.Tree;
import org.eclipse.sirius.components.trees.renderer.TreeRenderer;
import org.eclipse.sirius.web.application.UUIDParser;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.services.api.IRepresentationMetadataSearchService;
import org.eclipse.syson.tree.explorer.services.api.ISysONExplorerService;
import org.eclipse.syson.tree.explorer.view.SysONTreeViewDescriptionProvider;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;

/**
 * Computes Explorer expand/collapse paths without copying all previously visited IDs for each item.
 *
 * @author arichard
 */
@Service
public class SysONExpandAllTreePathProvider implements IExpandAllTreePathProvider {

    // shortcut: mirrors Sirius Web's 100-level limit, revisit when its generic traversal becomes linear.
    private static final int MAX_DEPTH_INCREASE = 100;

    private final ISysONExplorerService explorerService;

    private final SysONTreeViewDescriptionProvider descriptionProvider;

    private final ITreeNavigationService navigationService;

    private final IURLParser urlParser;

    private final IRepresentationMetadataSearchService representationMetadataSearchService;

    /**
     * Creates the path provider using the Explorer's existing services.
     *
     * @param explorerService the composed Explorer service, including application delegates
     * @param descriptionProvider the SysON Explorer description provider
     * @param navigationService the tree navigation service
     * @param urlParser the tree URI parser
     * @param representationMetadataSearchService the representation metadata search service
     */
    public SysONExpandAllTreePathProvider(ISysONExplorerService explorerService, SysONTreeViewDescriptionProvider descriptionProvider,
            ITreeNavigationService navigationService, IURLParser urlParser, IRepresentationMetadataSearchService representationMetadataSearchService) {
        this.explorerService = Objects.requireNonNull(explorerService);
        this.descriptionProvider = Objects.requireNonNull(descriptionProvider);
        this.navigationService = Objects.requireNonNull(navigationService);
        this.urlParser = Objects.requireNonNull(urlParser);
        this.representationMetadataSearchService = Objects.requireNonNull(representationMetadataSearchService);
    }

    /**
     * Restricts this optimization to SysON's Explorer description.
     *
     * @param tree the tree being queried
     * @return whether this is the SysON Explorer
     */
    @Override
    public boolean canHandle(Tree tree) {
        return Objects.equals(tree.getDescriptionId(), this.descriptionProvider.getDescriptionId());
    }

    /**
     * Traverses the filtered subtree in depth-first order, retaining Sirius Web's depth limit and path semantics.
     *
     * @param editingContext the current editing context
     * @param tree the current Explorer tree
     * @param input the selected subtree
     * @return the IDs to expand or collapse and the maximum depth
     */
    @Override
    public ExpandAllTreePathSuccessPayload handle(IEditingContext editingContext, Tree tree, ExpandAllTreePathInput input) {
        var parameters = this.urlParser.getParameterValues(tree.getId());
        List<String> filters = Optional.ofNullable(parameters.get(TreeRenderer.ACTIVE_FILTER_IDS))
                .flatMap(values -> values.stream().findFirst())
                .map(this.urlParser::getParameterEntries)
                .orElse(List.of());
        var representations = new UUIDParser().parse(editingContext.getId())
                .map(id -> this.representationMetadataSearchService.findAllRepresentationMetadataBySemanticData(AggregateReference.to(id)))
                .orElse(List.of());
        int initialDepth = this.navigationService.getAncestors(editingContext, tree, input.treeItemId()).size();
        int maxDepth = initialDepth;
        var pending = new ArrayDeque<PendingItem>();
        pending.push(new PendingItem(this.explorerService.getTreeItemObject(input.treeItemId(), editingContext), input.treeItemId(), initialDepth));
        var expandedIds = new ArrayList<String>();
        var visitedIds = new HashSet<String>();
        while (!pending.isEmpty()) {
            var item = pending.pop();
            maxDepth = Math.max(maxDepth, item.depth());
            if (item.depth() - initialDepth >= MAX_DEPTH_INCREASE) {
                continue;
            }
            if (visitedIds.add(item.id())) {
                expandedIds.add(item.id());
            }
            if (item.object() == null) {
                maxDepth = Math.max(maxDepth, item.depth() + 1);
            } else if (this.explorerService.hasChildren(item.object(), editingContext, representations, expandedIds, filters)) {
                var children = this.explorerService.getChildren(item.object(), editingContext, representations, expandedIds, filters);
                for (int index = children.size() - 1; index >= 0; index--) {
                    var child = children.get(index);
                    pending.push(new PendingItem(child, this.explorerService.getTreeItemId(child), item.depth() + 1));
                }
            }
        }
        return new ExpandAllTreePathSuccessPayload(input.id(), new TreePath(expandedIds, maxDepth));
    }

    /**
     * An item awaiting traversal, retaining the child object instead of looking it up again by ID.
     *
     * @param object the semantic object or synthetic directory
     * @param id the tree item ID
     * @param depth the item's absolute depth
     */
    private record PendingItem(Object object, String id, int depth) {
    }
}
