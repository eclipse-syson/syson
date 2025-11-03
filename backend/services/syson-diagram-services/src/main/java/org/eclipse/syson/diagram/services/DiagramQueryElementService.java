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
package org.eclipse.syson.diagram.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.syson.sysml.ViewUsage;
import org.springframework.stereotype.Service;

/**
 * Element-related services doing queries in diagrams.
 *
 * @author arichard
 */
@Service
public class DiagramQueryElementService {

    private final IObjectSearchService objectSearchService;

    public DiagramQueryElementService(IObjectSearchService objectSearchService) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
    }

    /**
     * Search and retrieve the ViewUsage corresponding to the parent Node/diagram of the given Node.
     *
     * @param editingContext
     *            the {@link IEditingContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param diagramContext
     *            the {@link DiagramContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param node
     *            the selected node on which the element has been dropped (may be null if the tool has been called from
     *            the diagram). It corresponds to a variable accessible from the variable manager.
     * @return an Optional ViewUsage if found, an empty Optional otherwise.
     */
    protected ViewUsage getViewUsage(IEditingContext editingContext, DiagramContext diagramContext, Node node) {
        Optional<ViewUsage> optViewUsage = Optional.empty();
        if (node == null) {
            optViewUsage = this.objectSearchService.getObject(editingContext, diagramContext.diagram().getTargetObjectId())
                    .filter(ViewUsage.class::isInstance)
                    .map(ViewUsage.class::cast);
        } else {
            optViewUsage = this.objectSearchService.getObject(editingContext, node.getTargetObjectId())
                    .filter(ViewUsage.class::isInstance)
                    .map(ViewUsage.class::cast);
        }
        if (optViewUsage.isEmpty()) {
            List<Node> rootNodes = diagramContext.diagram().getNodes();
            for (Node rootNode : rootNodes) {
                if (Objects.equals(rootNode, node)) {
                    optViewUsage = this.objectSearchService.getObject(editingContext, diagramContext.diagram().getTargetObjectId())
                            .filter(ViewUsage.class::isInstance)
                            .map(ViewUsage.class::cast);
                    break;
                }
            }
        }
        if (optViewUsage.isEmpty()) {
            List<Node> rootNodes = diagramContext.diagram().getNodes();
            List<Node> allSubNodes = this.getAllSubNodes(rootNodes);
            for (Node subNode : allSubNodes) {
                if (subNode.getChildNodes().contains(node)) {
                    var vu = this.getViewUsage(editingContext, diagramContext, subNode);
                    if (vu != null) {
                        optViewUsage = Optional.of(vu);
                        break;
                    }
                }
            }
        }
        if (optViewUsage.isEmpty()) {
            optViewUsage = this.objectSearchService.getObject(editingContext, diagramContext.diagram().getTargetObjectId())
                    .filter(ViewUsage.class::isInstance)
                    .map(ViewUsage.class::cast);
        }
        return optViewUsage.orElse(null);
    }

    /**
     * Get all sub nodes (nod border nodes, only child nodes) of the given list of nodes.
     *
     * @param nodes
     *            the given list of nodes.
     * @return all sub nodes of the given list of nodes.
     */
    private List<Node> getAllSubNodes(List<Node> nodes) {
        var allSubNodes = new LinkedList<Node>();
        for (Node node : nodes) {
            var children = new LinkedList<Node>();
            children.addAll(node.getChildNodes());
            allSubNodes.addAll(this.getAllSubNodes(children));
        }
        return allSubNodes;
    }
}
