/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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

import org.eclipse.emf.common.util.EList;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.ViewDeletionRequest;
import org.eclipse.sirius.components.diagrams.elements.NodeElementProps;
import org.eclipse.sirius.components.diagrams.renderer.DiagramRenderingCache;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.components.view.emf.diagram.api.IViewDiagramDescriptionSearchService;
import org.eclipse.syson.sysml.ConnectionUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.helper.EMFUtils;
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

    private final IViewDiagramDescriptionSearchService viewDiagramDescriptionSearchService;

    private final IDiagramIdProvider diagramIdProvider;

    private final IFeedbackMessageService feedbackMessageService;

    public DiagramQueryElementService(IObjectSearchService objectSearchService, IViewDiagramDescriptionSearchService viewDiagramDescriptionSearchService, IDiagramIdProvider diagramIdProvider,
            IFeedbackMessageService feedbackMessageService) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.viewDiagramDescriptionSearchService = Objects.requireNonNull(viewDiagramDescriptionSearchService);
        this.diagramIdProvider = Objects.requireNonNull(diagramIdProvider);
        this.feedbackMessageService = feedbackMessageService;
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

    /**
     * Get the parent ID of the given {@link Node}.
     *
     * @param diagramContext
     *            the {@link DiagramContext} retrieved from the Variable Manager.
     * @param node
     *            the given {@link Node}.
     * @return the parent ID of the given {@link Node}.
     */
    public String getGraphicalParentId(DiagramContext diagramContext, Node node) {
        String parentId = null;
        var parent = new NodeFinder(diagramContext.diagram()).getParent(node);
        if (parent instanceof Node parentNode) {
            parentId = parentNode.getId();
        } else if (parent instanceof Diagram diagram) {
            parentId = diagram.getId();
        } else {
            parentId = diagramContext.diagram().getId();
        }
        return parentId;
    }

    /**
     * Get the semantic element of the graphical parent of the given node. If located at the root of the diagram, return the semantic element of the diagram (most likely the {@link ViewUsage)}.
     *
     * @param node
     *         the node for which we want to find the semantic element corresponding to its graphical parent node
     * @param editingContext
     *         the editing context
     * @param contextDiagram
     *         the diagram
     * @return an optional element
     */
    public Optional<Element> getGraphicalSemanticParent(Node node, IEditingContext editingContext, Diagram contextDiagram) {
        var parent = new NodeFinder(contextDiagram).getParent(node);
        Optional<Object> result = Optional.empty();
        if (parent instanceof Node parentNode) {
            result = this.objectSearchService.getObject(editingContext, parentNode.getTargetObjectId());
        } else if (parent instanceof Diagram diagram) {
            result = this.objectSearchService.getObject(editingContext, diagram.getTargetObjectId());
        }
        return result.filter(Element.class::isInstance).map(Element.class::cast);
    }

    /**
     * Check if a {@link ConnectionUsage} edge should be displayed between the given source and target.
     *
     * <p>If the {@link ConnectionUsage} use feature chain to reference inherited elements, a special computation should check that the edge only connected inherited feature and not "real" objects. To
     * do that, it will only select elements displayed "inside" the expected parent.</p>
     *
     * <p>For example, for the feature chain "part1.port1" where "port1" is not directly owned by "part1", this method will only select target/source node displayed nested a "part1" node or at its
     * border.
     * </p>
     *
     * @param self
     *         the connection usage
     * @param sourceNode
     *         the graphical source element
     * @param targetNode
     *         the graphical target element
     * @param cacheRendering
     *         the current DiagramRenderingCache
     * @param editingContext
     *         the editing context
     * @return {@code true} if the edge should be rendered
     */
    public boolean shouldRenderConnectionUsageEdge(ConnectionUsage self, org.eclipse.sirius.components.representations.Element sourceNode,
            org.eclipse.sirius.components.representations.Element targetNode, DiagramRenderingCache cacheRendering, IEditingContext editingContext) {
        Feature sourceFeature = self.getSourceFeature();
        if (this.isValidConnectionEnd(sourceFeature, sourceNode, cacheRendering, editingContext)) {
            // Handle binary connection for now
            EList<Feature> targetFeature = self.getTargetFeature();
            if (!targetFeature.isEmpty()) {
                return this.isValidConnectionEnd(targetFeature.get(0), targetNode, cacheRendering, editingContext);
            }
        }

        return false;
    }

    /**
     * Generate the NodeDescription Id of the given {@link Element} in the context of the given {@link Diagram}.
     *
     * @param element
     *            the given {@link Element}.
     * @param diagram
     *            the given {@link Diagram}.
     * @param editingContext
     *            the {@link IEditingContext} retrieved from the Variable Manager.
     * @return an Optional NodeDescription Id of the given {@link Element} in the context of the given {@link Diagram}
     *         if it exists, an empty Optional otherwise.
     */
    public Optional<String> getNodeDescriptionId(Element element, Diagram diagram, IEditingContext editingContext) {
        var optViewDD = this.viewDiagramDescriptionSearchService.findById(editingContext, diagram.getDescriptionId());
        if (optViewDD.isPresent()) {
            return EMFUtils.allContainedObjectOfType(optViewDD.get(), org.eclipse.sirius.components.view.diagram.NodeDescription.class)
                    .filter(nodeDesc -> nodeDesc.getName().equals("GV Node " + element.eClass().getName()))
                    .map(nodeDesc -> this.diagramIdProvider.getId(nodeDesc))
                    .findFirst();
        }
        return Optional.empty();
    }

    /**
     * Registers an information-level message to be displayed to the end-user after the tool has executed.
     *
     * @param self
     *            any object, it is not used by the service.
     * @param message
     *            the message to display to the end-user.
     * @return self.
     */
    public Object infoMessage(Object self, String message) {
        this.feedbackMessageService.addFeedbackMessage(new Message(message, MessageLevel.INFO));
        return self;
    }

    /**
     * Checks if the given node represents the given feature. This especially handle can of feature chain that target inherited member. In such case, the two last segments of the chain should be used
     * to find the suitable node.
     *
     * @param endFeature
     *         the end feature of a {@link org.eclipse.syson.sysml.ConnectorAsUsage}
     * @param nodeToTest
     *         the node to test
     * @param cacheRendering
     *         the current cache rendering
     * @param editingContext
     *         the editing context
     * @return true if the given node is valid source/target for the given node
     */
    private boolean isValidConnectionEnd(Feature endFeature, org.eclipse.sirius.components.representations.Element nodeToTest, DiagramRenderingCache cacheRendering,
            IEditingContext editingContext) {
        boolean isValidSource = false;
        if (endFeature != null && endFeature.getChainingFeature().size() > 1) {
            EList<Feature> chainingFeatures = endFeature.getChainingFeature();
            // Gets the previous chain of the last segment and check if the target node is displayed in it.
            Feature lastChain = chainingFeatures.get(chainingFeatures.size() - 1);
            Feature beforeLastChain = chainingFeatures.get(chainingFeatures.size() - 2);
            // If the target is owned by the previous chaining feature it means it is not an inherited feature, this is the only case where the connection can be displayed between root diagram element
            // Otherwise, the graphical parent should match the previous chaining feature
            boolean allowRootElement = beforeLastChain.getOwnedFeature().contains(lastChain);
            isValidSource = this.isDisplayIn(beforeLastChain, nodeToTest, cacheRendering, editingContext, allowRootElement);

        } else if (nodeToTest.getProps() instanceof NodeElementProps nodeElementProps) {
            isValidSource = this.objectSearchService.getObject(editingContext, nodeElementProps.getTargetObjectId()).map(sourceElement -> sourceElement == endFeature).orElse(false);
        }
        return isValidSource;
    }


    private boolean isDisplayIn(Feature expectedSemanticParent, org.eclipse.sirius.components.representations.Element node, DiagramRenderingCache cacheRendering, IEditingContext editingContext, boolean allowRootElement) {
        if (node.getProps() instanceof NodeElementProps sourceNodeProps) {
            return cacheRendering.getParent(sourceNodeProps.getId())
                    .map(org.eclipse.sirius.components.representations.Element::getProps)
                    .filter(NodeElementProps.class::isInstance)
                    .map(NodeElementProps.class::cast)
                    .map(NodeElementProps::getTargetObjectId)
                    .flatMap(id -> this.objectSearchService.getObject(editingContext, id))
                    .map(semanticParent -> semanticParent == expectedSemanticParent)
                    .orElse(allowRootElement);
        }
        return false;
    }
}
