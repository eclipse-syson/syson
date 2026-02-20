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

import java.text.MessageFormat;
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
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IReadOnlyObjectPredicate;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.ViewCreationRequest;
import org.eclipse.sirius.components.diagrams.components.NodeContainmentKind;
import org.eclipse.sirius.components.diagrams.components.NodeIdProvider;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.diagrams.description.SynchronizationPolicy;
import org.eclipse.sirius.components.diagrams.events.HideDiagramElementEvent;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.model.services.ModelMutationElementService;
import org.eclipse.syson.services.NodeDescriptionService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.services.api.SiriusWebCoreServices;
import org.eclipse.syson.sysml.BindingConnectorAsUsage;
import org.eclipse.syson.sysml.ConnectionUsage;
import org.eclipse.syson.sysml.Connector;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.Expose;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FlowUsage;
import org.eclipse.syson.sysml.InterfaceUsage;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SatisfyRequirementUsage;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.metamodel.services.ElementInitializerSwitch;
import org.eclipse.syson.sysml.metamodel.services.MetamodelMutationElementService;
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;
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

    private final IFeedbackMessageService feedbackMessageService;

    private final IReadOnlyObjectPredicate readOnlyObjectPredicate;

    private final MetamodelMutationElementService metamodelMutationElementService;

    private final UtilService utilService;

    private final NodeDescriptionService nodeDescriptionService;

    private final MetamodelQueryElementService metamodelQueryElementService;

    public DiagramMutationElementService(SiriusWebCoreServices coreServices,
            IDiagramDescriptionService diagramDescriptionService, IDiagramQueryService diagramQueryService, IReadOnlyObjectPredicate readOnlyObjectPredicate,
            DiagramQueryElementService diagramQueryElementService,
            ModelMutationElementService modelMutationElementService) {
        this.identityService = Objects.requireNonNull(coreServices.identityService());
        this.objectSearchService = Objects.requireNonNull(coreServices.objectSearchService());
        this.representationDescriptionSearchService = Objects.requireNonNull(coreServices.representationDescriptionSearchService());
        this.readOnlyObjectPredicate = Objects.requireNonNull(readOnlyObjectPredicate);
        this.feedbackMessageService = Objects.requireNonNull(coreServices.feedbackMessageService());
        this.diagramDescriptionService = Objects.requireNonNull(diagramDescriptionService);
        this.diagramQueryService = Objects.requireNonNull(diagramQueryService);
        this.diagramQueryElementService = Objects.requireNonNull(diagramQueryElementService);
        this.modelMutationElementService = Objects.requireNonNull(modelMutationElementService);
        this.metamodelMutationElementService = new MetamodelMutationElementService();
        this.metamodelQueryElementService = new MetamodelQueryElementService();
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
     * @return the new {@link ViewUsage} or the given {@link Element} if its existing associated {@link ViewUsage} has
     *         not been found.
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
            var elementInitializerSwitch = new ElementInitializerSwitch();
            elementInitializerSwitch.doSwitch(newViewUsage);
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
                elementInitializerSwitch.doSwitch(membershipExpose);
            }
            // 4 - expose the element and its sub elements previously exposed in the existingViewUsage in the
            // newViewUsage
            var membershipExpose = SysmlFactory.eINSTANCE.createMembershipExpose();
            membershipExpose.setImportedMembership(element.getOwningMembership());
            newViewUsage.getOwnedRelationship().add(membershipExpose);
            elementInitializerSwitch.doSwitch(membershipExpose);

            return newViewUsage;
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

        if (parent instanceof Node node) {
            var parentNodeDescription = convertedNodes.values().stream()
                    .filter(nodeDescription -> Objects.equals(nodeDescription.getId(), node.getDescriptionId()))
                    .findFirst()
                    .orElse(null);
            var parentObject = this.objectSearchService.getObject(editingContext, node.getTargetObjectId()).orElse(null);
            candidates = this.nodeDescriptionService.getChildNodeDescriptionsForRendering(element, parentObject, List.of(parentNodeDescription), convertedNodes, editingContext, diagramContext);
        } else if (parent instanceof ViewCreationRequest viewCreationRequest && viewCreationRequest.getDescriptionId() != null) {
            var parentNodeDescription = convertedNodes.values().stream()
                    .filter(nodeDescription -> Objects.equals(nodeDescription.getId(), viewCreationRequest.getDescriptionId()))
                    .findFirst()
                    .orElse(null);
            var parentObject = this.objectSearchService.getObject(editingContext, viewCreationRequest.getTargetObjectId()).orElse(null);
            candidates = this.nodeDescriptionService.getChildNodeDescriptionsForRendering(element, parentObject, List.of(parentNodeDescription), convertedNodes, editingContext, diagramContext);
        } else {
            var diagramDescription = this.representationDescriptionSearchService.findById(editingContext, diagramContext.diagram().getDescriptionId());
            var parentObject = this.objectSearchService.getObject(editingContext, diagramContext.diagram().getTargetObjectId()).orElse(null);
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
    public Optional<String> getBorderNodeDescriptionIdForRendering(Element element, IEditingContext editingContext, DiagramContext diagramContext, Object parent,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        List<NodeDescription> candidates = new ArrayList<>();

        if (parent instanceof Node node) {
            NodeDescription parentNodeDescription = convertedNodes.values().stream()
                    .filter(nodeDescription -> Objects.equals(nodeDescription.getId(), node.getDescriptionId()))
                    .findFirst()
                    .orElse(null);
            var parentObject = this.objectSearchService.getObject(editingContext, node.getTargetObjectId()).orElse(null);
            candidates = this.nodeDescriptionService.getBorderNodeDescriptionsForRendering(element, parentObject, List.of(parentNodeDescription), convertedNodes, editingContext, diagramContext);
        } else if (parent instanceof ViewCreationRequest viewCreationRequest && viewCreationRequest.getDescriptionId() != null) {
            NodeDescription parentNodeDescription = convertedNodes.values().stream()
                    .filter(nodeDescription -> Objects.equals(nodeDescription.getId(), viewCreationRequest.getDescriptionId()))
                    .findFirst()
                    .orElse(null);
            var parentObject = this.objectSearchService.getObject(editingContext, viewCreationRequest.getTargetObjectId()).orElse(null);
            candidates = this.nodeDescriptionService.getBorderNodeDescriptionsForRendering(element, parentObject, List.of(parentNodeDescription), convertedNodes, editingContext, diagramContext);
        } else {
            var diagramDescription = this.representationDescriptionSearchService.findById(editingContext, diagramContext.diagram().getDescriptionId());
            var parentObject = this.objectSearchService.getObject(editingContext, diagramContext.diagram().getTargetObjectId()).orElse(null);
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

    /**
     * Set a new source {@link Feature} for the given {@link Connector}. Note that it might also move the
     * {@link Connector} to a new container to match creation rules.
     *
     * @param connector
     *            the given {@link Connector}.
     * @param newSource
     *            the new target {@link Feature}.
     * @param sourceNode
     *            new source node of the edge
     * @param targetNode
     *            target node of the edge
     * @param editingContext
     *            the editing context
     * @param diagram
     *            the context diagram
     * @return the given {@link Connector}.
     */
    public Connector reconnectSource(Connector connector, Feature newSource, Node sourceNode, Node targetNode, IEditingContext editingContext, Diagram diagram) {
        Optional<Feature> optOldTarget = this.metamodelQueryElementService.getConnectorTarget(connector).stream().findFirst();
        if (optOldTarget.isEmpty()) {
            // Invalid model for reconnection
            this.feedbackMessageService.addFeedbackMessage(new Message("Invalid connector: missing target", MessageLevel.WARNING));
            return connector;
        }

        Feature oldTarget = optOldTarget.get();

        // Recompute the best container
        this.getConnectorContainer(sourceNode, targetNode, newSource, oldTarget, editingContext, diagram).ifPresent(newContainer -> {
            if (newContainer != connector.getOwner()) {
                // Move to the new container and notify the user
                newContainer.getOwnedRelationship().add(connector.getOwningRelationship());
                if (connector instanceof PartUsage) {
                    connector.setIsComposite(newContainer instanceof Type);
                }
                this.feedbackMessageService.addFeedbackMessage(new Message("The connection has been moved to a new owner " + newContainer.getQualifiedName(), MessageLevel.INFO));
            }
        });

        // Recompute both target and source chain
        List<EndFeatureMembership> endFeatureMemberships = connector.getOwnedFeatureMembership().stream()
                .filter(EndFeatureMembership.class::isInstance)
                .map(EndFeatureMembership.class::cast)
                .toList();
        connector.getOwnedRelationship().removeAll(endFeatureMemberships);

        Element newSourceContainer = null;
        Element oldTargetContainer = null;

        if (sourceNode != null && sourceNode.isBorderNode()) {
            newSourceContainer = this.diagramQueryElementService.getGraphicalSemanticParent(sourceNode, editingContext, diagram).orElse(null);
        } else {
            newSourceContainer = newSource.getOwner();
        }
        if (targetNode != null && targetNode.isBorderNode()) {
            oldTargetContainer = this.diagramQueryElementService.getGraphicalSemanticParent(targetNode, editingContext, diagram).orElse(null);
        } else {
            oldTargetContainer = oldTarget.getOwner();
        }

        this.metamodelMutationElementService.setConnectorEnds(connector, newSource, oldTarget, newSourceContainer, oldTargetContainer, connector.getOwner());

        return connector;
    }

    /**
     * Set a new target {@link Feature} for the given {@link Connector}. Note that it might also move the
     * {@link Connector} to a new container to match creation rules.
     *
     * @param connector
     *            the given {@link Connector}.
     * @param newTarget
     *            the new target {@link Element}.
     * @param sourceNode
     *            source node of the edge
     * @param targetNode
     *            new target node of the edge
     * @param editingContext
     *            the editing context
     * @param diagram
     *            the context diagram
     * @return the given {@link Connector}.
     */
    public Connector reconnectTarget(Connector connector, Feature newTarget, Node sourceNode, Node targetNode, IEditingContext editingContext, Diagram diagram) {
        Feature source = this.metamodelQueryElementService.getConnectorSource(connector);
        if (source == null) {
            // Invalid model for reconnection
            this.feedbackMessageService.addFeedbackMessage(new Message("Invalid connector: missing source", MessageLevel.WARNING));
            return connector;
        }

        // Recompute the best container
        this.getConnectorContainer(sourceNode, targetNode, source, newTarget, editingContext, diagram).ifPresent(newContainer -> {
            if (newContainer != connector.getOwner()) {
                // Move to the new container  and notify the user
                newContainer.getOwnedRelationship().add(connector.getOwningRelationship());
                if (connector instanceof PartUsage) {
                    connector.setIsComposite(newContainer instanceof Type);
                }
                this.feedbackMessageService.addFeedbackMessage(new Message("The connection has been moved to a new owner " + newContainer.getQualifiedName(), MessageLevel.INFO));
            }
        });

        // Recompute both target and source chain
        List<EndFeatureMembership> endFeatureMemberships = connector.getOwnedFeatureMembership().stream()
                .filter(EndFeatureMembership.class::isInstance)
                .map(EndFeatureMembership.class::cast)
                .toList();
        connector.getOwnedRelationship().removeAll(endFeatureMemberships);

        Element sourceContainer = null;
        Element newTargetContainer = null;

        if (sourceNode != null && sourceNode.isBorderNode()) {
            sourceContainer = this.diagramQueryElementService.getGraphicalSemanticParent(sourceNode, editingContext, diagram).orElse(null);
        } else {
            sourceContainer = source.getOwner();
        }
        if (targetNode != null && targetNode.isBorderNode()) {
            newTargetContainer = this.diagramQueryElementService.getGraphicalSemanticParent(targetNode, editingContext, diagram).orElse(null);
        } else {
            newTargetContainer = newTarget.getOwner();
        }

        this.metamodelMutationElementService.setConnectorEnds(connector, source, newTarget, sourceContainer, newTargetContainer, connector.getOwner());

        return connector;
    }

    /**
     * Set a new source {@link Element} for the given {@link SatisfyRequirementUsage}.
     *
     * @param sru
     *            the given {@link SatisfyRequirementUsage}.
     * @param newSource
     *            the new source {@link Element}.
     * @param sourceNode
     *            new source node of the edge
     * @param targetNode
     *            target node of the edge
     * @param editingContext
     *            the editing context
     * @param diagram
     *            the context diagram
     * @return the given {@link SatisfyRequirementUsage}.
     */
    public SatisfyRequirementUsage reconnectSatisfyRequirementSource(SatisfyRequirementUsage sru, Element newSource) {
        if (newSource instanceof Feature newFeature) {
            var subjectParameter = sru.getSubjectParameter();
            if (subjectParameter != null) {
                subjectParameter.getOwnedSubsetting().stream()
                        .findFirst()
                        .ifPresent(s -> s.setSubsettedFeature(newFeature));
            } else {
                var newSubjectMembership = SysmlFactory.eINSTANCE.createSubjectMembership();
                sru.getOwnedRelationship().add(newSubjectMembership);
                this.metamodelMutationElementService.initialize(newSubjectMembership);

                var newReferenceUsage = SysmlFactory.eINSTANCE.createReferenceUsage();
                newSubjectMembership.getOwnedRelatedElement().add(newReferenceUsage);
                this.metamodelMutationElementService.initialize(newReferenceUsage);
                this.utilService.setSubsetting(newReferenceUsage, newFeature);
            }
        } else {
            this.feedbackMessageService.addFeedbackMessage(new Message("The satisfy source must be a Feature", MessageLevel.WARNING));
        }
        return sru;
    }

    /**
     * Set a new target {@link Element} for the given {@link SatisfyRequirementUsage}.
     *
     * @param sru
     *            the given {@link SatisfyRequirementUsage}.
     * @param newTarget
     *            the new target {@link Element}.
     * @return the given {@link SatisfyRequirementUsage}.
     */
    public SatisfyRequirementUsage reconnectSatisfyRequirementTarget(SatisfyRequirementUsage sru, Element newTarget) {
        if (newTarget instanceof RequirementUsage newReqUsage) {
            var ownedReferenceSubsetting = sru.getOwnedReferenceSubsetting();
            if (ownedReferenceSubsetting != null) {
                ownedReferenceSubsetting.setReferencedFeature(newReqUsage);
            } else {
                var newReferenceSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
                sru.getOwnedRelationship().add(newReferenceSubsetting);
                // link the new ReferenceSubsetting to the given existing RequirementUsage
                newReferenceSubsetting.setReferencedFeature(newReqUsage);
                this.metamodelMutationElementService.initialize(newReferenceSubsetting);
            }
        } else {
            this.feedbackMessageService.addFeedbackMessage(new Message("The satisfy target must be a RequirementUsage", MessageLevel.WARNING));
        }
        return sru;
    }

    /**
     * Creates a {@link BindingConnectorAsUsage}.
     *
     * @param source
     *            the source feature
     * @param target
     *            the target feature
     * @param sourceNode
     *            the graphical source node
     * @param targetNode
     *            the graphical target node
     * @param editingContext
     *            the current {@link IEditingContext}
     * @param diagramContext
     *            the {@link DiagramContext}
     * @return a new {@link BindingConnectorAsUsage}
     */
    public BindingConnectorAsUsage createBindingConnectorAsUsage(Feature source, Feature target, Node sourceNode, Node targetNode, IEditingContext editingContext, DiagramContext diagramContext) {
        final Element edgeSourceContainer;
        final Element edgeTargetContainer;

        if (sourceNode != null) {
            edgeSourceContainer = this.diagramQueryElementService.getGraphicalSemanticParent(sourceNode, editingContext, diagramContext.diagram()).orElse(null);
        } else {
            edgeSourceContainer = null;
        }
        if (targetNode != null) {
            edgeTargetContainer = this.diagramQueryElementService.getGraphicalSemanticParent(targetNode, editingContext, diagramContext.diagram()).orElse(null);
        } else {
            edgeTargetContainer = null;
        }

        return this.getConnectorContainer(sourceNode, targetNode, source, target, editingContext, diagramContext.diagram())
                .map(connectorContainer -> this.metamodelMutationElementService.createBindingConnectorAsUsage(source, target, edgeSourceContainer, edgeTargetContainer, connectorContainer))
                .orElseGet(() -> {
                    this.feedbackMessageService.addFeedbackMessage(
                            new Message(
                                    MessageFormat.format("Unable to find a suitable owner for this BindingConnector in {0} containment tree.", source.getQualifiedName()),
                                    MessageLevel.WARNING));
                    return null;
                });
    }

    /**
     * Creates a {@link FlowUsage}.
     *
     * @param source
     *         the source feature
     * @param target
     *         the target feature
     * @param sourceNode
     *         the graphical source node
     * @param targetNode
     *         the graphical target node
     * @param editingContext
     *         the current {@link IEditingContext}
     * @param diagramContext
     *         the {@link DiagramContext}
     * @return a new {@link FlowUsage}
     */
    public FlowUsage createFlowUsage(Feature source, Feature target, Node sourceNode, Node targetNode, IEditingContext editingContext, DiagramContext diagramContext) {
        final Element edgeSourceContainer;
        final Element edgeTargetContainer;

        if (sourceNode != null) {
            edgeSourceContainer = this.diagramQueryElementService.getGraphicalSemanticParent(sourceNode, editingContext, diagramContext.diagram()).orElse(null);
        } else {
            edgeSourceContainer = null;
        }
        if (targetNode != null) {
            edgeTargetContainer = this.diagramQueryElementService.getGraphicalSemanticParent(targetNode, editingContext, diagramContext.diagram()).orElse(null);
        } else {
            edgeTargetContainer = null;
        }

        return this.getConnectorContainer(sourceNode, targetNode, source, target, editingContext, diagramContext.diagram())
                .map(connectorContainer -> this.metamodelMutationElementService.createFlowUsage(source, target, edgeSourceContainer, edgeTargetContainer, connectorContainer)).orElseGet(() -> {
                    this.feedbackMessageService.addFeedbackMessage(
                            new Message(
                                    MessageFormat.format("Unable to find a suitable owner for this FlowUsage in {0} containment tree.", source.getQualifiedName()),
                                    MessageLevel.WARNING));
                    return null;
                });
    }

    /**
     * Creates a {@link InterfaceUsage}.
     *
     * @param sourcePort
     *         the source port
     * @param targetPort
     *         the target port
     * @param sourceNode
     *         the graphical source node
     * @param targetNode
     *         the graphical target node
     * @param editingContext
     *         the current {@link IEditingContext}
     * @param diagramContext
     *         the {@link DiagramContext}
     * @return a new {@link InterfaceUsage}
     */
    public InterfaceUsage createInterfaceUsage(PortUsage sourcePort, PortUsage targetPort, Node sourceNode, Node targetNode, IEditingContext editingContext, DiagramContext diagramContext) {
        final Element edgeSourceContainer;
        final Element edgeTargetContainer;

        if (sourceNode != null) {
            edgeSourceContainer = this.diagramQueryElementService.getGraphicalSemanticParent(sourceNode, editingContext, diagramContext.diagram()).orElse(null);
        } else {
            edgeSourceContainer = null;
        }
        if (targetNode != null) {
            edgeTargetContainer = this.diagramQueryElementService.getGraphicalSemanticParent(targetNode, editingContext, diagramContext.diagram()).orElse(null);
        } else {
            edgeTargetContainer = null;
        }

        return this.getConnectorContainer(sourceNode, targetNode, sourcePort, targetPort, editingContext, diagramContext.diagram())
                .map(connectorContainer -> this.metamodelMutationElementService.createInterfaceUsage(sourcePort, targetPort, edgeSourceContainer, edgeTargetContainer, connectorContainer))
                .orElseGet(() -> {
                    this.feedbackMessageService.addFeedbackMessage(
                            new Message(
                                    MessageFormat.format("Unable to find a suitable owner for this InterfaceUsage in {0} containment tree.", sourcePort.getQualifiedName()),
                                    MessageLevel.WARNING));
                    return null;
                });
    }

    /**
     * Creates a {@link ConnectionUsage}.
     *
     * @param connectionSource
     *         the source usage
     * @param connectionTarget
     *         the target usage
     * @param sourceNode
     *         the graphical source node
     * @param targetNode
     *         the graphical target node
     * @param editingContext
     *         the current {@link IEditingContext}
     * @param diagramContext
     *         the {@link DiagramContext}
     * @return a new {@link ConnectionUsage}
     */
    public ConnectionUsage createConnectionUsage(Usage connectionSource, Usage connectionTarget, Node sourceNode, Node targetNode, IEditingContext editingContext, DiagramContext diagramContext) {
        final Element edgeSourceContainer;
        final Element edgeTargetContainer;

        if (sourceNode != null) {
            edgeSourceContainer = this.diagramQueryElementService.getGraphicalSemanticParent(sourceNode, editingContext, diagramContext.diagram()).orElse(null);
        } else {
            edgeSourceContainer = null;
        }
        if (targetNode != null) {
            edgeTargetContainer = this.diagramQueryElementService.getGraphicalSemanticParent(targetNode, editingContext, diagramContext.diagram()).orElse(null);
        } else {
            edgeTargetContainer = null;
        }

        return this.getConnectorContainer(sourceNode, targetNode, connectionSource, connectionTarget, editingContext, diagramContext.diagram())
                .map(connectorContainer -> this.metamodelMutationElementService.createConnectionUsage(connectionSource, connectionTarget, edgeSourceContainer, edgeTargetContainer, connectorContainer))
                .orElseGet(() -> {
                    this.feedbackMessageService.addFeedbackMessage(
                            new Message(
                                    MessageFormat.format("Unable to find a suitable owner for this ConnectionUsage in {0} containment tree.", connectionSource.getQualifiedName()),
                                    MessageLevel.WARNING));
                    return null;
                });
    }

    private Optional<Namespace> getConnectorContainer(Node sourceNode, Node targetNode, Element source, Element target, IEditingContext editingContext, Diagram diagram) {
        Optional<Namespace> namespaceOwner = Optional.empty();
        if (sourceNode != null && targetNode != null) {
            // First use the graphical node to be able to handle inherited elements that would not be displayed in their owner
            Element sourceParentGraphicalElement = this.diagramQueryElementService.getGraphicalSemanticParent(sourceNode, editingContext, diagram)
                    .filter(e -> !(e instanceof ViewUsage))
                    .orElse(source); // If not found the node is located at the root of the diagram, use the semantic element alone
            Element targetParentGraphicalElement = this.diagramQueryElementService.getGraphicalSemanticParent(targetNode, editingContext, diagram)
                    .filter(e -> !(e instanceof ViewUsage))
                    .orElse(target); // If not found the node is located at the root of the diagram, use the semantic element alone
            if (sourceParentGraphicalElement != null && targetParentGraphicalElement != null) {
                namespaceOwner = this.metamodelQueryElementService.getCommonOwnerAncestor(sourceParentGraphicalElement, targetParentGraphicalElement, Namespace.class,
                        owner -> !this.readOnlyObjectPredicate.test(owner));
            }
        }
        return namespaceOwner;
    }
}
