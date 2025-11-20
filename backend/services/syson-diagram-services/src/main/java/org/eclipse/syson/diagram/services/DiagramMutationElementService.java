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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramDescriptionService;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramQueryService;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.ViewCreationRequest;
import org.eclipse.sirius.components.diagrams.components.NodeContainmentKind;
import org.eclipse.sirius.components.diagrams.components.NodeIdProvider;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.diagrams.description.SynchronizationPolicy;
import org.eclipse.sirius.components.diagrams.events.HideDiagramElementEvent;
import org.eclipse.syson.model.services.ModelMutationElementService;
import org.eclipse.syson.services.ElementInitializerSwitch;
import org.eclipse.syson.services.NodeDescriptionService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expose;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.metamodel.services.MetamodelMutationElementService;
import org.springframework.stereotype.Service;

/**
 * Element-related services doing mutations in diagrams and models.
 *
 * @author arichard
 */
@Service
public class DiagramMutationElementService {

    private static final String STATE_TRANSITION_COMPARTMENT_NAME = "state transition";

    private final IIdentityService identityService;

    private final IObjectSearchService objectSearchService;

    private final IRepresentationDescriptionSearchService representationDescriptionSearchService;

    private final IDiagramDescriptionService diagramDescriptionService;

    private final IDiagramQueryService diagramQueryService;

    private final DiagramQueryElementService diagramQueryElementService;

    private final ModelMutationElementService modelMutationElementService;

    private final MetamodelMutationElementService metamodelMutationElementService;

    private final UtilService utilService;

    private final NodeDescriptionService nodeDescriptionService;

    public DiagramMutationElementService(IIdentityService identityService, IObjectSearchService objectSearchService, IRepresentationDescriptionSearchService representationDescriptionSearchService,
            IDiagramDescriptionService diagramDescriptionService, IDiagramQueryService diagramQueryService, DiagramQueryElementService diagramQueryElementService,
            ModelMutationElementService modelMutationElementService) {
        this.identityService = Objects.requireNonNull(identityService);
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.representationDescriptionSearchService = Objects.requireNonNull(representationDescriptionSearchService);
        this.diagramDescriptionService = Objects.requireNonNull(diagramDescriptionService);
        this.diagramQueryService = Objects.requireNonNull(diagramQueryService);
        this.diagramQueryElementService = Objects.requireNonNull(diagramQueryElementService);
        this.modelMutationElementService = Objects.requireNonNull(modelMutationElementService);
        this.metamodelMutationElementService = new MetamodelMutationElementService();
        this.utilService = new UtilService();
        this.nodeDescriptionService = new NodeDescriptionService(this.objectSearchService);
    }

    /**
     * Allows to expose the given {@link Element} in a new {@link ViewUsage} that will be type by the a ViewDefinition
     * (represented by its qualified name). The given {@link Element} is also removed from the exposed elements of the
     * existing {@link ViewUsage} associated to the given {@code selectedNode}. The new {@link ViewUsage} will be
     * exposed in the existing {@link ViewUsage}.
     *
     * @param element
     *            the {@link Element} to expose in a new ViewUsage.
     * @param newViewDefinition
     *            the ViewDefinition (represented by its qualified name) that will be the type of the new ViewUsage.
     * @param editingContext
     *            the {@link IEditingContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param diagramContext
     *            the {@link DiagramContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param convertedNodes
     *            the map of all existing node descriptions in the DiagramDescription of this Diagram. It corresponds to
     *            a variable accessible from the variable manager.
     * @return
     */
    public Element viewNodeAs(Element element, String newViewDefinition, IEditingContext editingContext, DiagramContext diagramContext, Node selectedNode) {
        var existingViewUsage = this.diagramQueryElementService.getViewUsage(editingContext, diagramContext, selectedNode);
        if (existingViewUsage != null) {
            Element viewUsageContainer = existingViewUsage.getOwner();
            // 1 - create a new ViewUsage in the viewUsageContainer, typed by the ViewDefinition corresponding to the
            // newViewDefinition
            var newViewUsage = SysmlFactory.eINSTANCE.createViewUsage();
            var newViewUsageMembership = this.metamodelMutationElementService.createMembership(viewUsageContainer);
            newViewUsageMembership.getOwnedRelatedElement().add(newViewUsage);
            new ElementInitializerSwitch().doSwitch(newViewUsage);
            this.modelMutationElementService.setAsView(newViewUsage, newViewDefinition);

            // 2 - move the element and its children from the existingViewUsage to new newViewUsage
            var exposed = existingViewUsage.getOwnedImport().stream()
                    .filter(Expose.class::isInstance)
                    .map(Expose.class::cast)
                    .toList();
            this.moveExposedElements(element, exposed, newViewUsage);

            // 3 - expose the new ViewUsage in the existingViewUsage
            if (!existingViewUsage.getExposedElement().contains(newViewUsage)) {
                var membershipExpose = SysmlFactory.eINSTANCE.createMembershipExpose();
                membershipExpose.setImportedMembership(newViewUsage.getOwningMembership());
                existingViewUsage.getOwnedRelationship().add(membershipExpose);
            }
            // 4 - expose the element and its sub elements previously exposed in the existingViewUsage in the
            // newViewUsage
            var membershipExpose = SysmlFactory.eINSTANCE.createMembershipExpose();
            membershipExpose.setImportedMembership(element.getOwningMembership());
            newViewUsage.getOwnedRelationship().add(membershipExpose);
        }

        return element;
    }

    private void moveExposedElements(Element element, List<Expose> exposed, ViewUsage newViewUsage) {
        var exposedCopy = new ArrayList<>(exposed);
        for (Expose expose : exposedCopy) {
            Element importedElement = expose.getImportedElement();
            if (EMFUtils.isAncestor(element, importedElement)) {
                newViewUsage.getOwnedRelationship().add(expose);
            }
        }
    }

    public ViewCreationRequest createView(Element element, IEditingContext editingContext, DiagramContext diagramContext, Object selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        return this.createView(element, editingContext, diagramContext, selectedNode, convertedNodes, NodeContainmentKind.CHILD_NODE);
    }

    private ViewCreationRequest createView(Element element, IEditingContext editingContext, DiagramContext diagramContext, Object selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes, NodeContainmentKind nodeKind) {
        var optDescriptionId = this.getChildNodeDescriptionIdForRendering(element, editingContext, diagramContext, selectedNode, convertedNodes);
        if (optDescriptionId.isPresent()) {
            var parentElementId = this.getParentElementId(selectedNode, diagramContext);
            return this.createView(element, parentElementId, optDescriptionId.get(), editingContext, diagramContext, nodeKind);
        } else {
            return null;
        }
    }

    public ViewCreationRequest createView(Element element, String parentElementId, String descriptionId, IEditingContext editingContext, DiagramContext diagramContext,
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
    public Optional<String> getChildNodeDescriptionIdForRendering(Element element, IEditingContext editingContext, DiagramContext diagramContext, Object parent,
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
    private String getParentElementId(Object graphicalElement, DiagramContext diagramContext) {
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
     * Called by "New State" tool from StateTransition View StateUsage node.
     *
     * @param parentState
     *            The parent {@link StateDefinition} or {@link StateUsage}
     * @param editingContext
     *            the {@link IEditingContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param diagramContext
     *            the {@link DiagramContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param selectedNode
     *            the selected node on which the tool has been called. It corresponds to a variable accessible from the
     *            variable manager.
     * @param convertedNodes
     *            the map of all existing node descriptions in the DiagramDescription of this Diagram. It corresponds to
     *            a variable accessible from the variable manager.
     * @param isParallel
     *            whether or not the created State is set as parallel.
     * @param isExhibit
     *            Whether or not the created State is exhibited or not.
     * @return the created {@link StateUsage}.
     */
    public StateUsage createChildState(Element parentState, IEditingContext editingContext, DiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes, boolean isParallel, boolean isExhibit) {
        StateUsage childState = this.utilService.createChildState(parentState, isParallel, isExhibit);
        if (selectedNode.getInsideLabel().getText().equals(STATE_TRANSITION_COMPARTMENT_NAME)) {
            this.createView(childState, editingContext, diagramContext, selectedNode, convertedNodes);
        } else {
            selectedNode.getChildNodes().stream().filter(child -> child.getInsideLabel().getText().equals(STATE_TRANSITION_COMPARTMENT_NAME)).findFirst()
                    .ifPresent(compartmentNode -> {
                        this.createView(childState, editingContext, diagramContext, compartmentNode, convertedNodes);
                    });
        }

        return childState;
    }

    /**
     * For the given {@link Element} and its associated selected {@link Node}, reveal all hidden linked nodes and hide
     * selected {@link Node} compartments.
     *
     * @param element
     *            the current context of the service.
     * @param selectedNode
     *            the selectedNode corresponding to the given Element
     * @param editingContext
     *            the given {@link IEditingContext} in which this service has been called.
     * @param diagramContext
     *            the given {@link DiagramContext}.
     * @return the given {@link Element}.
     */
    public Element showContentAsTree(Element element, Node selectedNode, IEditingContext editingContext, DiagramContext diagramContext) {
        List<Node> linkedNodes = new ArrayList<>();
        var diagram = diagramContext.diagram();
        var nodeId = selectedNode.getId();

        Set<String> childNodesIds = selectedNode.getChildNodes().stream().map(Node::getId).collect(Collectors.toSet());
        diagramContext.diagramEvents().add(new HideDiagramElementEvent(childNodesIds, true));

        diagram.getEdges().forEach(edge -> {
            if (Objects.equals(edge.getSourceId(), nodeId)) {
                this.diagramQueryService.findNodeById(diagram, edge.getTargetId()).ifPresent(linkedNodes::add);
            }
        });
        Set<String> linkedNodesIds = linkedNodes.stream().map(Node::getId).collect(Collectors.toSet());
        diagramContext.diagramEvents().add(new HideDiagramElementEvent(linkedNodesIds, false));

        return element;
    }

    /**
     * For the given {@link Element} and its associated selected {@link Node}, hide all hidden linked nodes and reveal
     * selected {@link Node} children that are not empty.
     *
     * @param element
     *            the current context of the service.
     * @param selectedNode
     *            the selectedNode corresponding to the given Element
     * @param editingContext
     *            the given {@link IEditingContext} in which this service has been called.
     * @param diagramContext
     *            the given {@link DiagramContext}.
     * @return the given {@link Element}.
     */
    public Element showContentAsNested(Element element, Node selectedNode, IEditingContext editingContext, DiagramContext diagramContext) {
        List<Node> linkedNodes = new ArrayList<>();
        var diagram = diagramContext.diagram();
        var nodeId = selectedNode.getId();

        Set<String> notEmptyChildNodesIds = selectedNode.getChildNodes().stream().filter(n -> !n.getChildNodes().isEmpty()).map(Node::getId).collect(Collectors.toSet());
        diagramContext.diagramEvents().add(new HideDiagramElementEvent(notEmptyChildNodesIds, false));

        diagram.getEdges().forEach(edge -> {
            if (Objects.equals(edge.getSourceId(), nodeId)) {
                this.diagramQueryService.findNodeById(diagram, edge.getTargetId()).ifPresent(linkedNodes::add);
            }
        });
        Set<String> linkedNodesIds = linkedNodes.stream().map(Node::getId).collect(Collectors.toSet());
        diagramContext.diagramEvents().add(new HideDiagramElementEvent(linkedNodesIds, true));

        return element;
    }
}
