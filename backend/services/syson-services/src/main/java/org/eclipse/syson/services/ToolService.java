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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
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
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.diagrams.components.NodeContainmentKind;
import org.eclipse.sirius.components.diagrams.components.NodeIdProvider;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.diagrams.description.SynchronizationPolicy;
import org.eclipse.sirius.components.diagrams.events.HideDiagramElementEvent;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.syson.services.api.ISysMLMoveElementService;
import org.eclipse.syson.sysml.Comment;
import org.eclipse.syson.sysml.Documentation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expose;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.TextualRepresentation;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.util.NodeFinder;

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
     * For the given element, search its ViewUsage (if this service has been called from the diagram background it will
     * be the ViewUsage itself), and add the given element to the exposed elements of this ViewUsage.
     *
     * @param element
     *            the given {@link Element}.
     * @param editingContext
     *            the {@link IEditingContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param diagramContext
     *            the {@link DiagramContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param selectedNode
     *            the selected node on which the service has been called (may be null if the tool has been called from
     *            the diagram). It corresponds to a variable accessible from the variable manager.
     * @param convertedNodes
     *            the map of all existing node descriptions in the DiagramDescription of this Diagram. It corresponds to
     *            a variable accessible from the variable manager.
     * @return the given {@link Element}.
     */
    public Element expose(Element element, IEditingContext editingContext, DiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (this.utilService.isUnsynchronized(element)) {
            final Element parentElement;
            if (selectedNode == null) {
                parentElement = this.objectSearchService.getObject(editingContext, diagramContext.diagram().getTargetObjectId())
                        .filter(Element.class::isInstance)
                        .map(Element.class::cast)
                        .orElse(null);
            } else {
                parentElement = this.objectSearchService.getObject(editingContext, selectedNode.getTargetObjectId())
                        .filter(Element.class::isInstance)
                        .map(Element.class::cast)
                        .orElse(null);
            }
            this.handleUnsynchronizedElement(element, parentElement, editingContext, diagramContext, selectedNode, convertedNodes);
        } else {
            var viewUsage = this.getViewUsage(editingContext, diagramContext, selectedNode);
            if (viewUsage != null && !this.isExposed(element, viewUsage)) {
                var membershipExpose = SysmlFactory.eINSTANCE.createMembershipExpose();
                membershipExpose.setImportedMembership(element.getOwningMembership());
                viewUsage.getOwnedRelationship().add(membershipExpose);
                if (element instanceof Package) {
                    membershipExpose.setIsRecursive(true);
                }
            }
        }
        return element;
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
    public Object getParentNode(Element element, Node selectedNode, Edge selectedEdge, DiagramContext diagramContext) {
        Object parentNode = null;
        if (selectedEdge != null) {
            Node sourceNode = this.getSourceNode(selectedEdge, diagramContext.diagram());
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
    public Object getParentNode(Element element, IDiagramElement diagramElement, DiagramContext diagramContext) {
        Object parentNode = null;
        var diagram = diagramContext.diagram();
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
     * Handle unsynchronized nodes.
     *
     * @param element
     *            the given {@link Element}.
     * @param parentElement
     *            the parent element of the given {@link Element}.
     * @param editingContext
     *            the {@link IEditingContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param diagramContext
     *            the {@link DiagramContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param selectedNode
     *            the selected node on which the service has been called (may be null if the tool has been called from
     *            the diagram). It corresponds to a variable accessible from the variable manager.
     * @param convertedNodes
     *            the map of all existing node descriptions in the DiagramDescription of this Diagram. It corresponds to
     *            a variable accessible from the variable manager.
     */
    protected void handleUnsynchronizedElement(Element element, Element parentElement, IEditingContext editingContext, DiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (selectedNode == null) {
            this.createView(element, editingContext, diagramContext, selectedNode, convertedNodes);
        } else if (element instanceof Documentation && (parentElement instanceof Package || parentElement instanceof NamespaceImport || parentElement instanceof ViewUsage)) {
            var parentNode = new NodeFinder(diagramContext.diagram()).getParent(selectedNode);
            this.createView(element, editingContext, diagramContext, parentNode, convertedNodes);
        } else if (element instanceof Comment && !(element instanceof Documentation)) {
            var parentNode = new NodeFinder(diagramContext.diagram()).getParent(selectedNode);
            this.createView(element, editingContext, diagramContext, parentNode, convertedNodes);
        } else if (element instanceof TextualRepresentation) {
            var parentNode = new NodeFinder(diagramContext.diagram()).getParent(selectedNode);
            this.createView(element, editingContext, diagramContext, parentNode, convertedNodes);
        } else {
            if (selectedNode.getStyle().getChildrenLayoutStrategy() instanceof ListLayoutStrategy) {
                for (Node compartmentNode : selectedNode.getChildNodes()) {
                    var compartmentNodeDescription = convertedNodes.values().stream()
                            .filter(nd -> Objects.equals(nd.getId(), compartmentNode.getDescriptionId()))
                            .findFirst()
                            .orElse(null);
                    var candidates = this.nodeDescriptionService.getChildNodeDescriptionsForRendering(element, parentElement, List.of(compartmentNodeDescription), convertedNodes, editingContext,
                            diagramContext);
                    for (NodeDescription candidate : candidates) {
                        if (candidate.getSynchronizationPolicy().equals(SynchronizationPolicy.UNSYNCHRONIZED)) {
                            this.createView(element, compartmentNode.getId(), candidate.getId(), editingContext, diagramContext, NodeContainmentKind.CHILD_NODE);
                        }
                    }
                }
            } else {
                // The parent doesn't have compartments, we want to add elements directly inside it if possible.
                // This is for example the case with Package elements.
                this.getChildNodeDescriptionIdForRendering(element, editingContext, diagramContext, selectedNode, convertedNodes)
                        .ifPresent(descriptionId -> {
                            this.createView(element, editingContext, diagramContext, selectedNode, convertedNodes);
                        });
            }
        }
    }

    /**
     * Remove the given Element from the exposedElements reference of the {@link ViewUsage} that is the target of the
     * given {@link DiagramContext}. Also removes potential children that are sub-nodes of the given selectedNode
     * corresponding to the given Element.
     *
     * @param element
     *            the current context of the service.
     * @param selectedNode
     *            the selectedNode corresponding to the given Element
     * @param editingContext
     *            the given {@link IEditingContext} in which this service has been called.
     * @param diagramContext
     *            the given {@link DiagramContext}.
     * @return always <code>true</code>.
     */
    public boolean removeFromExposedElements(Element element, Node selectedNode, IEditingContext editingContext, DiagramContext diagramContext) {
        var optDiagramTargetObject = this.objectSearchService.getObject(editingContext, diagramContext.diagram().getTargetObjectId());
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

    protected List<Node> getChildNodes(DiagramContext diagramContext, Object selectedNode) {
        var childNodes = new ArrayList<Node>();
        if (selectedNode instanceof Node node) {
            childNodes.addAll(node.getChildNodes());
            if (node.getStyle().getChildrenLayoutStrategy() instanceof ListLayoutStrategy) {
                // childNodes are compartments, so also add childNodes of childNodes
                node.getChildNodes().stream().forEach(cn -> childNodes.addAll(cn.getChildNodes()));
            }
        } else {
            var diagram = diagramContext.diagram();
            childNodes.addAll(diagram.getNodes());
        }
        return childNodes;
    }

    protected boolean isPresent(Element element, List<Node> nodes) {
        return nodes.stream().anyMatch(node -> node.getTargetObjectId().equals(this.identityService.getId(element)));
    }


    protected ViewCreationRequest createView(Element element, IEditingContext editingContext, DiagramContext diagramContext, Object selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        return this.createView(element, editingContext, diagramContext, selectedNode, convertedNodes, NodeContainmentKind.CHILD_NODE);
    }

    protected ViewCreationRequest createView(Element element, IEditingContext editingContext, DiagramContext diagramContext, Object selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes, NodeContainmentKind nodeKind) {
        var optDescriptionId = this.getChildNodeDescriptionIdForRendering(element, editingContext, diagramContext, selectedNode, convertedNodes);
        if (optDescriptionId.isPresent()) {
            var parentElementId = this.getParentElementId(selectedNode, diagramContext);
            return this.createView(element, parentElementId, optDescriptionId.get(), editingContext, diagramContext, nodeKind);
        } else {
            return null;
        }
    }

    protected ViewCreationRequest createView(Element element, String parentElementId, String descriptionId, IEditingContext editingContext, DiagramContext diagramContext,
            NodeContainmentKind nodeKind) {
        ViewCreationRequest request = null;
        var diagramDescription = this.representationDescriptionSearchService.findById(editingContext, diagramContext.diagram().getDescriptionId())
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
            diagramContext.viewCreationRequests().add(request);
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
    protected String getParentElementId(Object graphicalElement, DiagramContext diagramContext) {
        final String parentElementId;
        if (graphicalElement instanceof Node node) {
            parentElementId = node.getId();
        } else if (graphicalElement instanceof ViewCreationRequest viewCreationRequest) {
            parentElementId = new NodeIdProvider().getNodeId(viewCreationRequest.getParentElementId(),
                    viewCreationRequest.getDescriptionId(),
                    NodeContainmentKind.CHILD_NODE,
                    viewCreationRequest.getTargetObjectId());
        } else {
            parentElementId = diagramContext.diagram().getId();
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
    protected Optional<String> getChildNodeDescriptionIdForRendering(Element element, IEditingContext editingContext, DiagramContext diagramContext, Object parent,
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
            var diagramDescription = this.representationDescriptionSearchService.findById(editingContext, diagramContext.diagram().getDescriptionId());
            parentObject = this.objectSearchService.getObject(editingContext, diagramContext.diagram().getTargetObjectId()).orElse(null);
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
    protected List<Node> getAllSubNodes(List<Node> nodes) {
        var allSubNodes = new LinkedList<Node>();
        for (Node node : nodes) {
            var children = new LinkedList<Node>();
            children.addAll(node.getChildNodes());
            allSubNodes.addAll(this.getAllSubNodes(children));
        }
        return allSubNodes;
    }

    protected void moveElement(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext, DiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        this.moveService.moveSemanticElement(droppedElement, targetElement);
        ViewUsage viewUsage = this.getViewUsage(editingContext, diagramContext, droppedNode);
        if (viewUsage != null) {
            this.removeFromExposedElements(droppedElement, droppedNode, editingContext, diagramContext);
        }
        this.expose(droppedElement, editingContext, diagramContext, targetNode, convertedNodes);
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
    protected void moveSubNodes(ViewCreationRequest parentViewCreationRequest, Node parentNode, DiagramContext diagramContext) {
        for (Node childNode : parentNode.getChildNodes()) {
            ViewCreationRequest childViewCreationRequest = ViewCreationRequest.newViewCreationRequest()
                    .containmentKind(NodeContainmentKind.CHILD_NODE)
                    .descriptionId(childNode.getDescriptionId())
                    .parentElementId(this.getParentElementId(parentViewCreationRequest, diagramContext))
                    .targetObjectId(childNode.getTargetObjectId())
                    .build();
            diagramContext.viewCreationRequests().add(childViewCreationRequest);
            this.moveSubNodes(childViewCreationRequest, childNode, diagramContext);
            if (childNode.getModifiers().contains(ViewModifier.Hidden)) {
                // Hide the new element if it was hidden before the drop, we want the new elements to look like the
                // dropped ones. We can't use DiagramServices here because we don't have access to the elements to hide
                // (only their IDs, they haven't been rendered yet).
                diagramContext.diagramEvents().add(new HideDiagramElementEvent(Set.of(this.getParentElementId(childViewCreationRequest, diagramContext)), true));
            }
        }
        for (Node borderNode : parentNode.getBorderNodes()) {
            ViewCreationRequest childViewCreationRequest = ViewCreationRequest.newViewCreationRequest()
                    .containmentKind(NodeContainmentKind.BORDER_NODE)
                    .descriptionId(borderNode.getDescriptionId())
                    .parentElementId(this.getParentElementId(parentViewCreationRequest, diagramContext))
                    .targetObjectId(borderNode.getTargetObjectId())
                    .build();
            diagramContext.viewCreationRequests().add(childViewCreationRequest);
            this.moveSubNodes(childViewCreationRequest, borderNode, diagramContext);
            if (borderNode.getModifiers().contains(ViewModifier.Hidden)) {
                diagramContext.diagramEvents().add(new HideDiagramElementEvent(Set.of(this.getParentElementId(childViewCreationRequest, diagramContext)), true));
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
