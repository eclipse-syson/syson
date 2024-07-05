/*******************************************************************************
 * Copyright (c) 2023, 2024 Obeo.
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
package org.eclipse.syson.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.ListLayoutStrategy;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.ViewCreationRequest;
import org.eclipse.sirius.components.diagrams.ViewDeletionRequest;
import org.eclipse.sirius.components.diagrams.components.NodeContainmentKind;
import org.eclipse.sirius.components.diagrams.components.NodeIdProvider;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.helper.EMFUtils;

/**
 * Tool-related Java services used by SysON representations.
 *
 * @author arichard
 */
public class ToolService {

    protected final IObjectService objectService;

    protected final IRepresentationDescriptionSearchService representationDescriptionSearchService;

    protected final IFeedbackMessageService feedbackMessageService;

    private final DeleteService deleteService;

    public ToolService(IObjectService objectService, IRepresentationDescriptionSearchService representationDescriptionSearchService, IFeedbackMessageService feedbackMessageService) {
        this.objectService = Objects.requireNonNull(objectService);
        this.representationDescriptionSearchService = Objects.requireNonNull(representationDescriptionSearchService);
        this.feedbackMessageService = Objects.requireNonNull(feedbackMessageService);
        this.deleteService = new DeleteService();
    }

    /**
     * Called by "Drop tool" from General View diagram.
     *
     * @param element
     *            the {@link Element} to drop to a General View diagram.
     * @param editingContext
     *            the {@link IEditingContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param diagramContext
     *            the {@link IDiagramContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param selectedNode
     *            the selected node on which the element has been dropped (may be null if the tool has been called from
     *            the diagram). It corresponds to a variable accessible from the variable manager.
     * @param convertedNodes
     *            the map of all existing node descriptions in the DiagramDescription of this Diagram. It corresponds to
     *            a variable accessible from the variable manager.
     * @return the input {@link Element}.
     */
    public Element dropElementFromExplorer(Element element, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        var elementToDrop = Optional.ofNullable(element);
        if (element instanceof Membership membership) {
            elementToDrop = membership.getOwnedRelatedElement().stream().findFirst();
        }
        if (elementToDrop.isPresent()) {
            this.createView(elementToDrop.get(), editingContext, diagramContext, selectedNode, convertedNodes);
        }
        return element;
    }

    /**
     * Called by "Drop Node tool" from General View diagram.
     *
     * @param droppedElement
     *            the dropped {@link Element}.
     * @param droppedNode
     *            the dropped {@link Node}.
     * @param targetElement
     *            the new semantic container.
     * @param targetElement
     *            the new graphical container.
     * @param editingContext
     *            the {@link IEditingContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param diagramContext
     *            the {@link IDiagramContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param convertedNodes
     *            the map of all existing node descriptions in the DiagramDescription of this Diagram. It corresponds to
     *            a variable accessible from the variable manager.
     * @return the input {@link Element}.
     */
    public Element dropElementFromDiagram(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext, IDiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        this.moveElement(droppedElement, droppedNode, targetElement, targetNode, editingContext, diagramContext, convertedNodes);
        return droppedElement;
    }

    /**
     * Get the parent node of the given {@link Element}. This could be a {@link Node} or a {@link Diagram}.
     *
     * @param element
     *            the given {@link Element}.
     * @param node
     *            the {@link Node} corresponding to the given {@link Element}.
     * @param diagramContext
     *            the diagram context in which to find the parent node.
     * @return a {@link Node} or a {@link Diagram}.
     */
    public Object getParentNode(Element element, Node node, IDiagramContext diagramContext) {
        Object parentNode = null;
        Diagram diagram = diagramContext.getDiagram();
        List<Node> nodes = diagram.getNodes();
        if (nodes.contains(node)) {
            parentNode = diagram;
        } else {
            parentNode = nodes.stream()
                    .map(subNode -> this.getParentNode(node, subNode))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }
        return parentNode;
    }

    protected Node getParentNode(Node node, Node nodeContainer) {
        List<Node> nodes = nodeContainer.getChildNodes();
        if (nodes.contains(node)) {
            return nodeContainer;
        }
        return nodes.stream()
                .map(subNode -> this.getParentNode(node, subNode))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    protected List<Node> getChildNodes(IDiagramContext diagramContext, Object selectedNode) {
        var childNodes = new ArrayList<Node>();
        if (selectedNode instanceof Node node) {
            childNodes.addAll(node.getChildNodes());
            if (node.getChildrenLayoutStrategy() instanceof ListLayoutStrategy) {
                // childNodes are compartments, so also add childNodes of childNodes
                node.getChildNodes().stream().forEach(cn -> childNodes.addAll(cn.getChildNodes()));
            }
        } else {
            var diagram = diagramContext.getDiagram();
            childNodes.addAll(diagram.getNodes());
        }
        return childNodes;
    }

    protected boolean isPresent(Element element, List<Node> nodes) {
        return nodes.stream().anyMatch(node -> node.getTargetObjectId().equals(this.objectService.getId(element)));
    }

    protected ViewCreationRequest createView(Element element, IEditingContext editingContext, IDiagramContext diagramContext, Object selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        return this.createView(element, editingContext, diagramContext, selectedNode, convertedNodes, NodeContainmentKind.CHILD_NODE);
    }

    protected ViewCreationRequest createView(Element element, IEditingContext editingContext, IDiagramContext diagramContext, Object selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes, NodeContainmentKind nodeKind) {
        var parentElementId = this.getParentElementId(selectedNode, diagramContext);
        var optDescriptionId = this.getChildNodeDescriptionIdForRendering(element, editingContext, diagramContext, selectedNode, convertedNodes);
        if (optDescriptionId.isPresent()) {
            return this.createView(element, parentElementId, optDescriptionId.get(), diagramContext, nodeKind);
        } else {
            return null;
        }
    }

    protected ViewCreationRequest createView(Element element, String parentElementId, String descriptionId, IDiagramContext diagramContext, NodeContainmentKind nodeKind) {
        var request = ViewCreationRequest.newViewCreationRequest()
                .containmentKind(nodeKind)
                .descriptionId(descriptionId)
                .parentElementId(parentElementId)
                .targetObjectId(this.objectService.getId(element))
                .build();
        diagramContext.getViewCreationRequests().add(request);
        return request;
    }

    /**
     * Returns the identifier of {@code graphicalElement}'s parent.
     * <p>
     * This method supports both {@link Node} and {@link ViewCreationRequest}.
     * </p>
     *
     * @param graphicalElement
     *            the graphical element to search the parent id
     * @param diagramContext
     *            the diagram context
     * @return the identifier of {@code graphicalElement}'s parent
     */
    protected String getParentElementId(Object graphicalElement, IDiagramContext diagramContext) {
        final String parentElementId;
        if (graphicalElement instanceof Node node) {
            parentElementId = node.getId();
        } else if (graphicalElement instanceof ViewCreationRequest viewCreationRequest) {
            parentElementId = new NodeIdProvider().getNodeId(viewCreationRequest.getParentElementId(),
                    viewCreationRequest.getDescriptionId(),
                    NodeContainmentKind.CHILD_NODE,
                    viewCreationRequest.getTargetObjectId());
        } else {
            parentElementId = diagramContext.getDiagram().getId();
        }
        return parentElementId;
    }

    /**
     * Returns the description id that can be used to render {@code element} in {@code parent}.
     * <p>
     * This method supports both {@link Node} and {@link ViewCreationRequest} as parent. This method returns the
     * description id of a top-level element if the provided {@code parent} isn't a {@link Node} or a
     * {@link ViewCreationRequest}.
     * </p>
     *
     * @param element
     *            the element to render
     * @param editingContext
     *            the editing context
     * @param diagramContext
     *            the diagram context
     * @param parent
     *            the parent ({@link Node} or {@link ViewCreationRequest})
     * @param convertedNodes
     *            the converted nodes
     * @return the description id
     */
    protected Optional<String> getChildNodeDescriptionIdForRendering(Element element, IEditingContext editingContext, IDiagramContext diagramContext, Object parent,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        NodeDescriptionService nodeDescriptionService = new NodeDescriptionService();
        List<NodeDescription> candidates = new ArrayList<>();
        final Object parentObject;

        if (parent instanceof Node node) {
            NodeDescription parentNodeDescription = convertedNodes.values().stream()
                    .filter(nodeDescription -> Objects.equals(nodeDescription.getId(), node.getDescriptionId()))
                    .findFirst()
                    .orElse(null);
            parentObject = this.objectService.getObject(editingContext, node.getTargetObjectId()).orElse(null);
            candidates = nodeDescriptionService.getChildNodeDescriptionsForRendering(element, parentObject, List.of(parentNodeDescription), convertedNodes);
        } else if (parent instanceof ViewCreationRequest viewCreationRequest && viewCreationRequest.getDescriptionId() != null) {
            NodeDescription parentNodeDescription = convertedNodes.values().stream()
                    .filter(nodeDescription -> Objects.equals(nodeDescription.getId(), viewCreationRequest.getDescriptionId()))
                    .findFirst()
                    .orElse(null);
            parentObject = this.objectService.getObject(editingContext, viewCreationRequest.getTargetObjectId()).orElse(null);
            candidates = nodeDescriptionService.getChildNodeDescriptionsForRendering(element, parentObject, List.of(parentNodeDescription), convertedNodes);
        } else {
            var diagramDescription = this.representationDescriptionSearchService.findById(editingContext, diagramContext.getDiagram().getDescriptionId());
            parentObject = this.objectService.getObject(editingContext, diagramContext.getDiagram().getTargetObjectId()).orElse(null);
            candidates = diagramDescription
                    .filter(org.eclipse.sirius.components.diagrams.description.DiagramDescription.class::isInstance)
                    .map(org.eclipse.sirius.components.diagrams.description.DiagramDescription.class::cast)
                    .map(org.eclipse.sirius.components.diagrams.description.DiagramDescription::getNodeDescriptions)
                    .orElse(List.of())
                    .stream()
                    .filter(nodeDescription -> nodeDescriptionService.canNodeDescriptionRenderElement(nodeDescription, element, parentObject))
                    .toList();
        }

        return candidates.stream()
                .map(NodeDescription::getId)
                .findFirst();
    }

    protected void moveElement(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext, IDiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        Optional<Membership> membership = this.getMembership(droppedElement, targetElement);
        if (membership.isPresent()) {
            targetElement.getOwnedRelationship().add(membership.get());
        } else {
            return;
        }
        this.createView(droppedElement, editingContext, diagramContext, targetNode, convertedNodes);
        diagramContext.getViewDeletionRequests().add(ViewDeletionRequest.newViewDeletionRequest().elementId(droppedNode.getId()).build());
    }

    /**
     * Returns the correct membership to store the droppedElement inside the targetElement one.
     *
     * @param droppedElement
     *            the element that has been dropped
     * @param targetElement
     *            the element inside which the drop has been performed
     * @return
     */
    private Optional<Membership> getMembership(Element droppedElement, Element targetElement) {
        Optional<Membership> result = Optional.empty();
        if (droppedElement.eContainer() instanceof Membership currentMembership) {
            if (targetElement instanceof Package) {
                // the expected membership should be an OwningMembership
                if (currentMembership instanceof FeatureMembership) {
                    var owningMemberhip = SysmlFactory.eINSTANCE.createOwningMembership();
                    owningMemberhip.getOwnedRelatedElement().add(droppedElement);
                    this.deleteService.deleteFromModel(currentMembership);
                    result = Optional.of(owningMemberhip);
                } else {
                    result = Optional.of(currentMembership);
                }
            } else {
                // the expected membership should be a FeatureMembership
                if (currentMembership instanceof FeatureMembership) {
                    result = Optional.of(currentMembership);
                } else {
                    var featureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
                    featureMembership.getOwnedRelatedElement().add(droppedElement);
                    this.deleteService.deleteFromModel(currentMembership);
                    result = Optional.of(featureMembership);
                }
            }
        }
        return result;
    }

    protected Optional<org.eclipse.sirius.components.view.diagram.NodeDescription> getViewNodeDescription(String descriptionId, DiagramDescription diagramDescription,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        return EMFUtils.eAllContentStreamWithSelf(diagramDescription)
                .filter(org.eclipse.sirius.components.view.diagram.NodeDescription.class::isInstance)
                .map(org.eclipse.sirius.components.view.diagram.NodeDescription.class::cast)
                .filter(nodeDesc -> {
                    NodeDescription convertedNodeDesc = convertedNodes.get(nodeDesc);
                    return convertedNodeDesc != null && descriptionId.equals(convertedNodeDesc.getId());
                })
                .findFirst();
    }
}
