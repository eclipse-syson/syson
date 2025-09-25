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

import java.util.Objects;
import java.util.Optional;

import org.eclipse.sirius.components.collaborative.trees.api.IInitialDirectEditTreeItemLabelProvider;
import org.eclipse.sirius.components.collaborative.trees.dto.InitialDirectEditElementLabelInput;
import org.eclipse.sirius.components.collaborative.trees.dto.InitialDirectEditElementLabelSuccessPayload;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.core.api.labels.StyledString;
import org.eclipse.sirius.components.trees.Tree;
import org.eclipse.sirius.components.trees.TreeItem;
import org.eclipse.sirius.web.application.views.explorer.services.ExplorerDescriptionProvider;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.tree.explorer.view.SysONTreeViewDescriptionProvider;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * Used to compute the initial label to display when the direct edit of a tree item of the SysON Explorer is triggered
 * on the frontend.
 *
 * @author arichard
 */
@Service
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SysONExplorerInitialDirectEditTreeItemLabelProvider implements IInitialDirectEditTreeItemLabelProvider {

    private final SysONTreeViewDescriptionProvider sysONTreeViewDescriptionProvider;

    private final IObjectSearchService objectSearchService;

    public SysONExplorerInitialDirectEditTreeItemLabelProvider(SysONTreeViewDescriptionProvider sysONTreeViewDescriptionProvider, IObjectSearchService objectSearchService) {
        this.sysONTreeViewDescriptionProvider = Objects.requireNonNull(sysONTreeViewDescriptionProvider);
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
    }

    @Override
    public boolean canHandle(Tree tree) {
        return tree.getId().startsWith(ExplorerDescriptionProvider.PREFIX)
                && Objects.equals(tree.getDescriptionId(), this.sysONTreeViewDescriptionProvider.getDescriptionId());
    }

    @Override
    public IPayload handle(IEditingContext editingContext, Tree tree, InitialDirectEditElementLabelInput input) {
        String initialLabel = tree.getChildren().stream()
                .map(treeItems -> this.searchById(treeItems, input.treeItemId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(TreeItem::getLabel)
                .map(StyledString::toString)
                .findFirst()
                .orElse("");

        var optViewUsage = this.objectSearchService.getObject(editingContext, input.treeItemId()).filter(ViewUsage.class::isInstance).map(ViewUsage.class::cast);
        if (optViewUsage.isPresent()) {
            if (initialLabel.endsWith("[GeneralView]")) {
                initialLabel = this.replaceLast(initialLabel, " [GeneralView]", "");
            } else if (initialLabel.endsWith("[InterconnectionView]")) {
                initialLabel = this.replaceLast(initialLabel, " [InterconnectionView]", "");
            } else if (initialLabel.endsWith("[ActionFlowView]")) {
                initialLabel = this.replaceLast(initialLabel, " [ActionFlowView]", "");
            } else if (initialLabel.endsWith("[StateTransitionView]")) {
                initialLabel = this.replaceLast(initialLabel, " [StateTransitionView]", "");
            }
        }

        return new InitialDirectEditElementLabelSuccessPayload(input.id(), initialLabel);
    }

    private Optional<TreeItem> searchById(TreeItem treeItem, String id) {
        Optional<TreeItem> optionalTreeItem = Optional.empty();
        if (treeItem.getId().equals(id)) {
            optionalTreeItem = Optional.of(treeItem);
        }
        if (optionalTreeItem.isEmpty() && treeItem.isHasChildren()) {
            optionalTreeItem = treeItem.getChildren().stream()
                    .map(treeItems -> this.searchById(treeItems, id))
                    .filter(Optional::isPresent)
                    .map(Optional::get).findFirst();
        }
        return optionalTreeItem;
    }

    String replaceLast(String string, String stringToReplace, String replacement) {
        int index = string.lastIndexOf(stringToReplace);
        if (index == -1) {
            return string;
        }
        return string.substring(0, index) + replacement + string.substring(index + stringToReplace.length());
    }
}
