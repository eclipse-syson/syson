/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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
import java.util.Set;

import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramDescriptionService;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.diagrams.IDiagramElement;
import org.eclipse.sirius.components.diagrams.ListLayoutStrategy;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.ViewCreationRequest;
import org.eclipse.sirius.components.diagrams.ViewDeletionRequest;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.diagrams.components.NodeContainmentKind;
import org.eclipse.sirius.components.diagrams.components.NodeIdProvider;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.diagrams.description.SynchronizationPolicy;
import org.eclipse.sirius.components.diagrams.events.HideDiagramElementEvent;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.syson.services.api.ISysMLMoveElementService;
import org.eclipse.syson.sysml.AnnotatingElement;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expose;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.helper.EMFUtils;

/**
 * Tool-related Java services used by SysON representations.
 *
 * @author arichard
 */
public class ToolService {

    protected final IIdentityService identityService;

    protected final IObjectSearchService objectSearchService;

    protected final IRepresentationDescriptionSearchService representationDescriptionSearchService;

    protected final IDiagramDescriptionService diagramDescriptionService;

    protected final IFeedbackMessageService feedbackMessageService;

    protected final ISysMLMoveElementService moveService;

    protected final DeleteService deleteService;

    protected final UtilService utilService;

    protected final NodeDescriptionService nodeDescriptionService;

    public ToolService(IIdentityService identityService, IObjectSearchService objectSearchService, IRepresentationDescriptionSearchService representationDescriptionSearchService,
            IDiagramDescriptionService diagramDescriptionService, IFeedbackMessageService feedbackMessageService, ISysMLMoveElementService moveService) {
        this.identityService = Objects.requireNonNull(identityService);
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.representationDescriptionSearchService = Objects.requireNonNull(representationDescriptionSearchService);
        this.diagramDescriptionService = Objects.requireNonNull(diagramDescriptionService);
        this.feedbackMessageService = Objects.requireNonNull(feedbackMessageService);
        this.moveService = Objects.requireNonNull(moveService);
        this.deleteService = new DeleteService();
        this.utilService = new UtilService();
        this.nodeDescriptionService = new NodeDescriptionService(objectSearchService);
    }

    /**
     * Get the parent node of the given {@link Element}. This could be a {@link Node} or a {@link Diagram}.
     *
     * @param element
     *            the given {@link Element}.
     * @param selectedNode
     *            the {@link Node} corresponding to the given {@link Element}, could be <code>null</code>.
     * @param selectedEdge
     *            the {@link Edge} corresponding to the given {@link Element}, could be <code>null</code>.
     * @param diagramContext
     *            the diagram context in which to find the parent node.
     * @return a {@link Node} or a {@link Diagram}.
     */
    public Object getParentNode(Element element, Node selectedNode, Edge selectedEdge, IDiagramContext diagramContext) {
        Object parentNode = null;
        if (selectedEdge != null) {
            Node sourceNode = this.getSourceNode(selectedEdge, diagramContext.getDiagram());
            if (sourceNode != null) {
                parentNode = this.getParentNode(element, sourceNode, diagramContext);
            } else {
                parentNode = this.getParentNode(element, selectedEdge, diagramContext);
            }
        } else {
            parentNode = this.getParentNode(element, selectedNode, diagramContext);
        }
        return parentNode;
    }

    /**
     * Get the parent node of the given {@link Element}. This could be a {@link Node} or a {@link Diagram}.
     *
     * @param element
     *            the given {@link Element}.
     * @param diagramElement
     *            the {@link IDiagramElement} corresponding to the given {@link Element}.
     * @param diagramContext
     *            the diagram context in which to find the parent node.
     * @return a {@link Node} or a {@link Diagram}.
     */
    public Object getParentNode(Element element, IDiagramElement diagramElement, IDiagramContext diagramContext) {
        Object parentNode = null;
        var diagram = diagramContext.getDiagram();
        var nodes = diagram.getNodes();
        var edges = diagram.getEdges();
        if (nodes.contains(diagramElement)) {
            parentNode = diagram;
        } else if (edges.contains(diagramElement)) {
            parentNode = diagram;
        } else {
            parentNode = nodes.stream()
                    .map(subNode -> this.getParentNode(diagramElement, subNode))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }
        return parentNode;
    }

    /**
     * Remove the given Element from the exposedElements reference of the {@link ViewUsage} that is the target of the
     * given {@link IDiagramContext}. Also removes potential children that are sub-nodes of the given selectedNode
     * corresponding to the given Element.
     *
     * @param element
     *            the current context of the service.
     * @param selectedNode
     *            the selectedNode corresponding to the given Element
     * @param editingContext
     *            the given {@link IEditingContext} in which this service has been called.
     * @param diagramContext
     *            the given {@link IDiagramContext}.
     * @return always <code>true</code>.
     */
    public boolean removeFromExposedElements(Element element, Node selectedNode, IEditingContext editingContext, IDiagramContext diagramContext) {
        var optDiagramTargetObject = this.objectSearchService.getObject(editingContext, diagramContext.getDiagram().getTargetObjectId());
        if (optDiagramTargetObject.isPresent()) {
            var diagramTargetObject = optDiagramTargetObject.get();
            if (diagramTargetObject instanceof ViewUsage viewUsage) {
                var exposed = viewUsage.getOwnedImport().stream()
                        .filter(Expose.class::isInstance)
                        .map(Expose.class::cast)
                        .toList();
                this.deleteExpose(exposed, element);
                // remove potential children that are sub-nodes of the given selectedNode
                this.removeFromExposedElements(selectedNode, editingContext, exposed);
            }
        }
        return true;
    }

    protected Node getParentNode(IDiagramElement diagramElement, Node nodeContainer) {
        List<Node> nodes = nodeContainer.getChildNodes();
        if (nodes.contains(diagramElement)) {
            return nodeContainer;
        }
        return nodes.stream()
                .map(subNode -> this.getParentNode(diagramElement, subNode))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    protected List<Node> getChildNodes(IDiagramContext diagramContext, Object selectedNode) {
        var childNodes = new ArrayList<Node>();
        if (selectedNode instanceof Node node) {
            childNodes.addAll(node.getChildNodes());
            if (node.getStyle().getChildrenLayoutStrategy() instanceof ListLayoutStrategy) {
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
        return nodes.stream().anyMatch(node -> node.getTargetObjectId().equals(this.identityService.getId(element)));
    }


    protected ViewCreationRequest createView(Element element, IEditingContext editingContext, IDiagramContext diagramContext, Object selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        return this.createView(element, editingContext, diagramContext, selectedNode, convertedNodes, NodeContainmentKind.CHILD_NODE);
    }

    protected ViewCreationRequest createView(Element element, IEditingContext editingContext, IDiagramContext diagramContext, Object selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes, NodeContainmentKind nodeKind) {
        var optDescriptionId = this.getChildNodeDescriptionIdForRendering(element, editingContext, diagramContext, selectedNode, convertedNodes);
        if (optDescriptionId.isPresent()) {
            var parentElementId = this.getParentElementId(selectedNode, diagramContext);
            return this.createView(element, parentElementId, optDescriptionId.get(), editingContext, diagramContext, nodeKind);
        } else {
            return null;
        }
    }

    protected ViewCreationRequest createView(Element element, String parentElementId, String descriptionId, IEditingContext editingContext, IDiagramContext diagramContext,
            NodeContainmentKind nodeKind) {
        ViewCreationRequest request = null;
        var diagramDescription = this.representationDescriptionSearchService.findById(editingContext, diagramContext.getDiagram().getDescriptionId())
                .filter(org.eclipse.sirius.components.diagrams.description.DiagramDescription.class::isInstance)
                .map(org.eclipse.sirius.components.diagrams.description.DiagramDescription.class::cast);
        var nodeDescription = this.diagramDescriptionService.findDiagramElementDescriptionById(diagramDescription.get(), descriptionId)
                .filter(NodeDescription.class::isInstance)
                .map(NodeDescription.class::cast)
                .filter(nd -> Objects.equals(nd.getSynchronizationPolicy(), SynchronizationPolicy.UNSYNCHRONIZED));
        if (nodeDescription.isPresent()) {
            request = ViewCreationRequest.newViewCreationRequest()
                    .containmentKind(nodeKind)
                    .descriptionId(descriptionId)
                    .parentElementId(parentElementId)
                    .targetObjectId(this.identityService.getId(element))
                    .build();
            diagramContext.getViewCreationRequests().add(request);
        }
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
        List<NodeDescription> candidates = new ArrayList<>();
        final Object parentObject;

        if (parent instanceof Node node) {
            NodeDescription parentNodeDescription = convertedNodes.values().stream()
                    .filter(nodeDescription -> Objects.equals(nodeDescription.getId(), node.getDescriptionId()))
                    .findFirst()
                    .orElse(null);
            parentObject = this.objectSearchService.getObject(editingContext, node.getTargetObjectId()).orElse(null);
            candidates = this.nodeDescriptionService.getChildNodeDescriptionsForRendering(element, parentObject, List.of(parentNodeDescription), convertedNodes, editingContext, diagramContext);
        } else if (parent instanceof ViewCreationRequest viewCreationRequest && viewCreationRequest.getDescriptionId() != null) {
            NodeDescription parentNodeDescription = convertedNodes.values().stream()
                    .filter(nodeDescription -> Objects.equals(nodeDescription.getId(), viewCreationRequest.getDescriptionId()))
                    .findFirst()
                    .orElse(null);
            parentObject = this.objectSearchService.getObject(editingContext, viewCreationRequest.getTargetObjectId()).orElse(null);
            candidates = this.nodeDescriptionService.getChildNodeDescriptionsForRendering(element, parentObject, List.of(parentNodeDescription), convertedNodes, editingContext, diagramContext);
        } else {
            var diagramDescription = this.representationDescriptionSearchService.findById(editingContext, diagramContext.getDiagram().getDescriptionId());
            parentObject = this.objectSearchService.getObject(editingContext, diagramContext.getDiagram().getTargetObjectId()).orElse(null);
            candidates = diagramDescription
                    .filter(org.eclipse.sirius.components.diagrams.description.DiagramDescription.class::isInstance)
                    .map(org.eclipse.sirius.components.diagrams.description.DiagramDescription.class::cast)
                    .map(org.eclipse.sirius.components.diagrams.description.DiagramDescription::getNodeDescriptions)
                    .orElse(List.of())
                    .stream()
                    .filter(nodeDescription -> this.nodeDescriptionService.canNodeDescriptionRenderElement(nodeDescription, element, parentObject, editingContext, diagramContext))
                    .toList();
        }

        return candidates.stream()
                .map(NodeDescription::getId)
                .findFirst();
    }

    protected void moveElement(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext, IDiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        this.moveService.moveSemanticElement(droppedElement, targetElement);
        ViewCreationRequest droppedElementViewCreationRequest = this.createView(droppedElement, editingContext, diagramContext, targetNode, convertedNodes);
        this.moveSubNodes(droppedElementViewCreationRequest, droppedNode, diagramContext);
        diagramContext.getViewDeletionRequests().add(ViewDeletionRequest.newViewDeletionRequest().elementId(droppedNode.getId()).build());
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

    /**
     * Moves the sub-nodes of the provided {@code parentNode} inside the graphical element created by
     * {@code parentViewCreationRequest}.
     * <p>
     * This method moves both child nodes and border nodes, but doesn't move edges, which are synchronized. This method
     * preserves the visibility of existing nodes: a hidden node in {@code parentNode} will still be hidden when
     * rendered in {@code parentViewCreationRequest}.
     * </p>
     * <p>
     * This method is typically used as part of graphical drag & drop, to ensure that the content of the dropped element
     * are re-rendered in their new container.
     * </p>
     *
     * @param parentViewCreationRequest
     *            the creation request for the new parent of the nodes
     * @param parentNode
     *            the existing node to move the content from
     * @param diagramContext
     *            the diagram context
     */
    protected void moveSubNodes(ViewCreationRequest parentViewCreationRequest, Node parentNode, IDiagramContext diagramContext) {
        for (Node childNode : parentNode.getChildNodes()) {
            ViewCreationRequest childViewCreationRequest = ViewCreationRequest.newViewCreationRequest()
                    .containmentKind(NodeContainmentKind.CHILD_NODE)
                    .descriptionId(childNode.getDescriptionId())
                    .parentElementId(this.getParentElementId(parentViewCreationRequest, diagramContext))
                    .targetObjectId(childNode.getTargetObjectId())
                    .build();
            diagramContext.getViewCreationRequests().add(childViewCreationRequest);
            this.moveSubNodes(childViewCreationRequest, childNode, diagramContext);
            if (childNode.getModifiers().contains(ViewModifier.Hidden)) {
                // Hide the new element if it was hidden before the drop, we want the new elements to look like the
                // dropped ones. We can't use DiagramServices here because we don't have access to the elements to hide
                // (only their IDs, they haven't been rendered yet).
                diagramContext.getDiagramEvents().add(new HideDiagramElementEvent(Set.of(this.getParentElementId(childViewCreationRequest, diagramContext)), true));
            }
        }
        for (Node borderNode : parentNode.getBorderNodes()) {
            ViewCreationRequest childViewCreationRequest = ViewCreationRequest.newViewCreationRequest()
                    .containmentKind(NodeContainmentKind.BORDER_NODE)
                    .descriptionId(borderNode.getDescriptionId())
                    .parentElementId(this.getParentElementId(parentViewCreationRequest, diagramContext))
                    .targetObjectId(borderNode.getTargetObjectId())
                    .build();
            diagramContext.getViewCreationRequests().add(childViewCreationRequest);
            this.moveSubNodes(childViewCreationRequest, borderNode, diagramContext);
            if (borderNode.getModifiers().contains(ViewModifier.Hidden)) {
                diagramContext.getDiagramEvents().add(new HideDiagramElementEvent(Set.of(this.getParentElementId(childViewCreationRequest, diagramContext)), true));
            }
        }
    }

    /**
     * Get the source node of the given {@link Edge}.
     *
     * @param edge
     *            the given {@link Edge}.
     * @param diagram
     *            the diagram containing the {@link Edge}.
     * @return the source node of the given {@link Edge} if found, <code>null</code> otherwise.
     */
    protected Node getSourceNode(Edge edge, Diagram diagram) {
        Node sourceNode = null;
        String sourceId = edge.getSourceId();
        for (Node node : diagram.getNodes()) {
            if (Objects.equals(sourceId, node.getId())) {
                sourceNode = node;
                break;
            }
            sourceNode = this.getSourceNode(sourceId, node);
            if (sourceNode != null) {
                break;
            }
        }
        return sourceNode;
    }

    protected Node getSourceNode(String sourceId, Node node) {
        Node sourceNode = null;
        for (Node childNode : node.getChildNodes()) {
            if (Objects.equals(sourceId, childNode.getId())) {
                sourceNode = childNode;
                break;
            }
            sourceNode = this.getSourceNode(sourceId, childNode);
            if (sourceNode != null) {
                break;
            }
        }
        if (sourceNode == null) {
            for (Node borderNode : node.getBorderNodes()) {
                if (Objects.equals(sourceId, borderNode.getId())) {
                    sourceNode = borderNode;
                    break;
                }
                sourceNode = this.getSourceNode(sourceId, borderNode);
                if (sourceNode != null) {
                    break;
                }
            }
        }
        return sourceNode;
    }

    protected boolean isExposed(Element element, ViewUsage viewUsage) {
        return viewUsage.getExposedElement().contains(element);
    }

    /**
     * Check if the given {@link Element} is represented by an unsynchronized node.
     *
     * @param element
     *            the given {@link Element}
     * @return <code>true</code> if the given {@link Element} is represented by an unsynchronized node,
     *         <code>false</code> otherwise.
     */
    protected boolean isUnsynchronized(Element element) {
        boolean isUnsynchronized = false;
        if (Objects.equals(element, this.utilService.retrieveStandardStartAction(element))) {
            isUnsynchronized = true;
        } else if (Objects.equals(element, this.utilService.retrieveStandardDoneAction(element))) {
            isUnsynchronized = true;
        } else if (element instanceof NamespaceImport) {
            isUnsynchronized = true;
        } else if (element instanceof AnnotatingElement) {
            isUnsynchronized = true;
        }
        return isUnsynchronized;
    }

    protected void removeFromExposedElements(Node currentNode, IEditingContext editingContext, List<Expose> exposed) {
        List<Node> childNodes = currentNode.getChildNodes();
        for (Node childNode : childNodes) {
            this.objectSearchService.getObject(editingContext, childNode.getTargetObjectId()).ifPresent(childElt -> {
                this.deleteExpose(exposed, childElt);
            });
            this.removeFromExposedElements(childNode, editingContext, exposed);
        }
    }

    protected void deleteExpose(List<Expose> exposed, Object exposedElement) {
        for (Expose expose : exposed) {
            if (Objects.equals(exposedElement, expose.getImportedElement())) {
                this.deleteService.deleteFromModel(expose);
            }
        }
    }
}
