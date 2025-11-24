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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.ViewDeletionRequest;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.util.NodeFinder;
import org.springframework.stereotype.Service;

/**
 * Element-related services doing queries in diagrams.
 *
 * @author arichard
 */
@Service
public class DiagramQueryElementService {

    private final IObjectSearchService objectSearchService;

    private final UtilService utilService;

    public DiagramQueryElementService(IObjectSearchService objectSearchService) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.utilService = new UtilService();
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
    public ViewUsage getViewUsage(IEditingContext editingContext, DiagramContext diagramContext, Node node) {
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
            optViewUsage = this.getParentViewUsage(node, editingContext, diagramContext);
        }
        return optViewUsage.orElse(null);
    }

    private Optional<ViewUsage> getParentViewUsage(Node node, IEditingContext editingContext, DiagramContext diagramContext) {
        Optional<ViewUsage> optViewUsage = Optional.empty();
        if (optViewUsage.isEmpty()) {
            var parentGraphicalObj = new NodeFinder(diagramContext.diagram()).getParent(node);
            if (parentGraphicalObj instanceof Node parentNode) {
                optViewUsage = this.objectSearchService.getObject(editingContext, parentNode.getTargetObjectId())
                        .filter(ViewUsage.class::isInstance)
                        .map(ViewUsage.class::cast);
                if (optViewUsage.isEmpty()) {
                    return this.getParentViewUsage(parentNode, editingContext, diagramContext);
                }
            } else if (parentGraphicalObj instanceof Diagram diagram) {
                optViewUsage = this.objectSearchService.getObject(editingContext, diagram.getTargetObjectId())
                        .filter(ViewUsage.class::isInstance)
                        .map(ViewUsage.class::cast);
            }
        }
        return optViewUsage;
    }

    /**
     * Check if the diagram associated to the given {@link DiagramContext} contains nodes.
     *
     * @param editingContext
     *            the {@link IEditingContext} retrieved from the Variable Manager.
     * @param diagramContext
     *            the {@link DiagramContext} retrieved from the Variable Manager.
     * @param previousDiagram
     *            the previous {@link Diagram} retrieved from the Variable Manager.
     * @return the given {@link Element} if the diagram is empty, <code>null</code> otherwise.
     */
    public boolean isDiagramEmpty(IEditingContext editingContext, DiagramContext diagramContext, Diagram previousDiagram, int exposedElements) {
        boolean emptyDiagram = false;
        if (previousDiagram != null && diagramContext != null && exposedElements == 0) {
            List<Node> previousNodes = previousDiagram.getNodes();
            var viewCreationRequests = diagramContext.viewCreationRequests();
            var viewDeletionRequests = diagramContext.viewDeletionRequests();

            if (viewCreationRequests.isEmpty() && this.previousNodesOnlyContainsEmptyDiagramImageNode(editingContext, previousNodes)) {
                emptyDiagram = true;
            } else if (viewDeletionRequests.isEmpty() && this.previousNodesOnlyContainsMissingElements(editingContext, previousNodes)) {
                // Undo on a synchronized node
                emptyDiagram = true;
            } else if (!viewDeletionRequests.isEmpty() && this.previousNodesOnlyViewDeletionRequests(previousNodes, viewDeletionRequests)) {
                // Undo on an unsynchronized node
                emptyDiagram = true;
            }
        } else {
            emptyDiagram = true;
        }
        return emptyDiagram;
    }

    private boolean previousNodesOnlyContainsEmptyDiagramImageNode(IEditingContext editingContext, List<Node> previousNodes) {
        return previousNodes.isEmpty() || (previousNodes.size() == 1 && previousNodes.stream()
                .anyMatch(
                        node -> Objects.equals("siriusComponents://nodeDescription?sourceKind=view&sourceId=8dcd14b0-6259-3193-ad2c-743f394c68e4&sourceElementId=024ab54f-ab81-3d02-8abd-f65af641e6c6",
                                node.getDescriptionId())));
    }

    private boolean previousNodesOnlyContainsMissingElements(IEditingContext editingContext, List<Node> previousNodes) {
        for (Node node : previousNodes) {
            var targetObjectId = node.getTargetObjectId();
            var optObject = this.objectSearchService.getObject(editingContext, targetObjectId)
                    .filter(Element.class::isInstance)
                    .map(Element.class::cast);
            if (optObject.isPresent()) {
                return false;
            }
        }
        return true;
    }

    private boolean previousNodesOnlyViewDeletionRequests(List<Node> previousNodes, List<ViewDeletionRequest> viewDeletionRequests) {
        var nodesIds = previousNodes.stream().map(Node::getId).toList();
        for (ViewDeletionRequest viewDeletionRequest : viewDeletionRequests) {
            if (!nodesIds.contains(viewDeletionRequest.getElementId())) {
                return false;
            }
        }
        return true;
    }
}
