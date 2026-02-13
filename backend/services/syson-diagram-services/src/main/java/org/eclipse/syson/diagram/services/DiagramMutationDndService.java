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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramService;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramServices;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramService;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.ViewCreationRequest;
import org.eclipse.sirius.components.diagrams.ViewDeletionRequest;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.diagrams.components.NodeContainmentKind;
import org.eclipse.sirius.components.diagrams.components.NodeIdProvider;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.diagrams.events.HideDiagramElementEvent;
import org.eclipse.sirius.components.emf.services.EObjectIDManager;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.emf.IViewRepresentationDescriptionSearchService;
import org.eclipse.syson.model.services.ModelQueryElementService;
import org.eclipse.syson.services.DeleteService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.services.api.SiriusWebCoreServices;
import org.eclipse.syson.sysml.CaseDefinition;
import org.eclipse.syson.sysml.CaseUsage;
import org.eclipse.syson.sysml.ConstraintDefinition;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.RequirementConstraintKind;
import org.eclipse.syson.sysml.RequirementDefinition;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SubjectMembership;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.util.NodeFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Dnd-related services doing mutations in diagrams and models.
 *
 * @author arichard
 */
@Service
public class DiagramMutationDndService {

    private final Logger logger = LoggerFactory.getLogger(DiagramMutationDndService.class);

    private final SiriusWebCoreServices siriusWebCoreServices;

    private final IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService;

    private final DiagramMutationMoveService diagramMutationMoveService;

    private final DiagramMutationElementService diagramMutationElementService;

    private final DiagramMutationExposeService diagramMutationExposeService;

    private final DiagramQueryElementService diagramQueryElementService;

    private final ModelQueryElementService modelQueryElementService;

    private final DeleteService deleteService;

    private final UtilService utilService;

    public DiagramMutationDndService(SiriusWebCoreServices siriusWebCoreServices, IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService,
            DiagramMutationMoveService diagramMutationMoveService, DiagramMutationElementService diagramMutationElementService, DiagramMutationExposeService diagramMutationExposeService,
            ModelQueryElementService modelQueryElementService, DiagramQueryElementService diagramQueryElementService) {
        this.siriusWebCoreServices = Objects.requireNonNull(siriusWebCoreServices);
        this.viewRepresentationDescriptionSearchService = Objects.requireNonNull(viewRepresentationDescriptionSearchService);
        this.diagramMutationMoveService = Objects.requireNonNull(diagramMutationMoveService);
        this.diagramMutationElementService = Objects.requireNonNull(diagramMutationElementService);
        this.diagramMutationExposeService = Objects.requireNonNull(diagramMutationExposeService);
        this.diagramQueryElementService = Objects.requireNonNull(diagramQueryElementService);
        this.modelQueryElementService = Objects.requireNonNull(modelQueryElementService);
        this.deleteService = new DeleteService();
        this.utilService = new UtilService();
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
     *            the {@link DiagramContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param selectedNode
     *            the selected node on which the element has been dropped (may be null if the tool has been called from
     *            the diagram). It corresponds to a variable accessible from the variable manager.
     * @param convertedNodes
     *            the map of all existing node descriptions in the DiagramDescription of this Diagram. It corresponds to
     *            a variable accessible from the variable manager.
     * @return the input {@link Element}.
     */
    public Element dropElementFromExplorer(Element element, IEditingContext editingContext, DiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        Optional<Object> optTargetElement;
        Optional<org.eclipse.sirius.components.view.diagram.NodeDescription> optNodeDescription = Optional.empty();
        if (selectedNode != null) {
            optTargetElement = this.siriusWebCoreServices.objectSearchService().getObject(editingContext, selectedNode.getTargetObjectId());
            optNodeDescription = convertedNodes.entrySet().stream().filter(entry -> entry.getValue().getId().equals(selectedNode.getDescriptionId())).map(Entry::getKey).findFirst();
        } else {
            optTargetElement = this.siriusWebCoreServices.objectSearchService().getObject(editingContext, diagramContext.diagram().getTargetObjectId());
        }
        if (optNodeDescription.isPresent() && optNodeDescription.get().getName().contains("EmptyDiagram")) {
            // The element is dropped on the information box displayed on an empty diagram. This box is visible only if
            // the diagram is empty, so we want to actually perform the drop on the diagram itself.
            return this.dropElementFromExplorer(element, editingContext, diagramContext, null, convertedNodes);
        } else if (optTargetElement.isPresent() && optTargetElement.get() instanceof Element targetElement) {
            // Check if the element we attempt to drop is in the ancestors of the target element and we attempt to drop
            // it on anything else than the diagram background. If it is the case we want to prevent the drop.
            if (EMFUtils.isAncestor(element, targetElement) && selectedNode != null) {
                this.logAncestorError(element, targetElement);
            } else {
                var optElementToDrop = Optional.ofNullable(element);
                if (this.utilService.isStandardDoneAction(element) || this.utilService.isStandardStartAction(element)) {
                    // Special case of "start" and "done" action.
                    // Those elements are represented in the diagram using a membership instead of the "real" element
                    optElementToDrop = Optional.of(element);
                } else if (element instanceof Membership membership) {
                    optElementToDrop = membership.getOwnedRelatedElement().stream().findFirst();
                }
                if (optElementToDrop.isPresent()) {
                    this.dropElementFromExplorerInTarget(optElementToDrop.get(), targetElement, editingContext, diagramContext, selectedNode, convertedNodes);
                }
            }
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
     * @param targetNode
     *            the new graphical container.
     * @param editingContext
     *            the {@link IEditingContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param diagramContext
     *            the {@link DiagramContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param convertedNodes
     *            the map of all existing node descriptions in the DiagramDescription of this Diagram. It corresponds to
     *            a variable accessible from the variable manager.
     * @return the input {@link Element}.
     */
    public Element dropElementFromDiagram(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext, DiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        final Element result;
        final Element realTargetElement;
        // If the target element is a ViewUsage, then we must drop the dropped element in its owner.
        if (targetElement instanceof ViewUsage viewUsage) {
            realTargetElement = viewUsage.getOwner();
        } else {
            realTargetElement = targetElement;
        }
        // Check if the element we attempt to drop is in the ancestors of the target element. If it is the case we want
        // to prevent the drop.
        if (EMFUtils.isAncestor(droppedElement, realTargetElement)) {
            this.logAncestorError(droppedElement, realTargetElement);
            // Null prevents the drop and makes Sirius Web reset the position of the dragged element.
            result = null;
        } else if (droppedNode.isBorderNode()) {
            // Null prevents the drop of border node when we only want to move them along the border of their parent
            result = null;
        } else {
            this.diagramMutationMoveService.moveElement(droppedElement, droppedNode, realTargetElement, targetNode, editingContext, diagramContext, convertedNodes);
            result = droppedElement;
        }
        return result;
    }

    public Element dropSubjectFromDiagram(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext, DiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (targetElement instanceof RequirementUsage
                || targetElement instanceof RequirementDefinition
                || targetElement instanceof CaseUsage
                || targetElement instanceof CaseDefinition) {
            boolean noExistingSubject = targetElement.getOwnedRelationship().stream()
                    .filter(SubjectMembership.class::isInstance)
                    .map(SubjectMembership.class::cast)
                    .findFirst().isEmpty();
            if (noExistingSubject) {
                this.diagramMutationMoveService.moveElement(droppedElement, droppedNode, targetElement, targetNode, editingContext, diagramContext, convertedNodes);
            }
        }
        return droppedElement;
    }

    public Element dropObjectiveRequirementFromDiagram(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext, DiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (targetElement instanceof CaseUsage
                || targetElement instanceof CaseDefinition) {
            if (this.utilService.isEmptyObjectiveRequirement(targetElement)) {
                this.diagramMutationMoveService.moveElement(droppedElement, droppedNode, targetElement, targetNode, editingContext, diagramContext, convertedNodes);
            }
        }
        return droppedElement;
    }

    public Element dropElementFromDiagramInRequirementAssumeConstraintCompartment(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext,
            DiagramContext diagramContext, Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (droppedElement instanceof ConstraintUsage droppedConstraint && (targetElement instanceof RequirementUsage || targetElement instanceof RequirementDefinition)) {
            this.diagramMutationMoveService.moveConstraintInRequirementConstraintCompartment(droppedConstraint, targetElement, RequirementConstraintKind.ASSUMPTION);
            this.diagramMutationElementService.createView(droppedElement, editingContext, diagramContext, targetNode, convertedNodes);
            diagramContext.viewDeletionRequests().add(ViewDeletionRequest.newViewDeletionRequest().elementId(droppedNode.getId()).build());
        }
        return droppedElement;
    }

    public Element dropElementFromDiagramInRequirementRequireConstraintCompartment(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext,
            DiagramContext diagramContext, Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (droppedElement instanceof ConstraintUsage droppedConstraint && (targetElement instanceof RequirementUsage || targetElement instanceof RequirementDefinition)) {
            this.diagramMutationMoveService.moveConstraintInRequirementConstraintCompartment(droppedConstraint, targetElement, RequirementConstraintKind.REQUIREMENT);
            this.diagramMutationElementService.createView(droppedElement, editingContext, diagramContext, targetNode, convertedNodes);
            diagramContext.viewDeletionRequests().add(ViewDeletionRequest.newViewDeletionRequest().elementId(droppedNode.getId()).build());
        }
        return droppedElement;
    }

    public Element dropElementFromDiagramInConstraintCompartment(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext,
            DiagramContext diagramContext, Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (droppedElement instanceof ConstraintUsage droppedConstraint) {
            if (targetElement instanceof ConstraintUsage || targetElement instanceof ConstraintDefinition) {
                var oldMembership = droppedConstraint.eContainer();
                var membership = SysmlFactory.eINSTANCE.createFeatureMembership();
                membership.getOwnedRelatedElement().add(droppedConstraint);
                targetElement.getOwnedRelationship().add(membership);
                if (oldMembership instanceof OwningMembership owningMembership) {
                    this.deleteService.deleteFromModel(owningMembership);
                }
                this.diagramMutationElementService.createView(droppedElement, editingContext, diagramContext, targetNode, convertedNodes);
                diagramContext.viewDeletionRequests().add(ViewDeletionRequest.newViewDeletionRequest().elementId(droppedNode.getId()).build());
            }
        }
        return droppedElement;
    }

    /**
     * Drops the provided {@code sourceElement} from the explorer on the given {@code selectedNode} on the diagram.
     * <p>
     * This method may perform graphical actions (e.g. create a view to represent the dropped element) as well as
     * semantic actions (e.g. set a feature typing). Note that the provided {@code targetElement} should be the semantic
     * element represented by {@code selectedNode}.
     * </p>
     * <p>
     * This method doesn't perform a drop of an element on the diagram to another element on the diagram. See
     * {@link #dropElementFromDiagram(Element, Node, Element, Node, IEditingContext, DiagramContext, Map)} for
     * in-diagram drag and drop.
     * </p>
     *
     * @param sourceElement
     *            the source element to drop
     * @param targetElement
     *            the target of the drop
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
    private void dropElementFromExplorerInTarget(Element sourceElement, Element targetElement, IEditingContext editingContext, DiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (sourceElement instanceof Definition definition && targetElement instanceof Usage usage && !(targetElement instanceof ViewUsage)) {
            // Dropping a definition on a usage types the usage with the definition. It doesn't create a new node on the
            // diagram.
            this.utilService.setFeatureTyping(usage, definition);
        } else if (this.modelQueryElementService.isExposable(sourceElement)) {
            if (this.modelQueryElementService.isExposed(sourceElement, this.diagramQueryElementService.getViewUsage(editingContext, diagramContext, selectedNode))) {
                var parentId = this.diagramQueryElementService.getGraphicalParentId(diagramContext, selectedNode);
                var descriptionId = this.diagramQueryElementService.getNodeDescriptionId(sourceElement, diagramContext.diagram(), editingContext);
                var nodeId = new NodeIdProvider().getNodeId(parentId,
                        descriptionId.get(),
                        NodeContainmentKind.CHILD_NODE,
                        this.siriusWebCoreServices.identityService().getId(sourceElement));
                this.diagramQueryElementService.findNodeById(diagramContext.diagram(), nodeId).ifPresent(node -> {
                    if (node.getState().equals(ViewModifier.Hidden)) {
                        diagramContext.diagramEvents().add(new HideDiagramElementEvent(Set.of(nodeId), false));
                    } else {
                        this.logAlreadyVisibleMessage(sourceElement, targetElement);
                    }
                });
            } else {
                Node newSelectedNode = selectedNode;
                if (selectedNode == null) {
                    // try to get the graphical node corresponding to the semantic parent
                    var parentId = new EObjectIDManager().findId(sourceElement.getOwner());
                    var optParentNode = diagramContext.diagram().getNodes().stream().filter(n -> parentId.isPresent() && Objects.equals(n.getTargetObjectId(), parentId.get())).findFirst();
                    if (optParentNode.isPresent()) {
                        newSelectedNode = optParentNode.get();
                    }
                }
                this.diagramMutationExposeService.expose(sourceElement, editingContext, diagramContext, newSelectedNode, convertedNodes);
            }
        } else {
            ViewCreationRequest parentViewCreationRequest = this.diagramMutationElementService.createView(sourceElement, editingContext, diagramContext, selectedNode, convertedNodes);
            Optional<Node> parentNodeOnDiagram = new NodeFinder(diagramContext.diagram())
                    .getOneNodeMatching(diagramNode -> Objects.equals(diagramNode.getId(), this.getParentElementId(parentViewCreationRequest, diagramContext)));
            if (parentNodeOnDiagram.isPresent()) {
                // The node already exist on the diagram, we don't need to create it.
                // It is easier to check it in this order (first create the ViewCreationRequest then remove
                // it) because we need the ViewCreationRequest anyways to check if the node is on the diagram.
                diagramContext.viewCreationRequests().remove(parentViewCreationRequest);
                if (parentNodeOnDiagram.get().getState().equals(ViewModifier.Hidden)) {
                    // The node exists on the diagram but is hidden, we can't create a new view representing
                    // it, but we reveal it.
                    IDiagramService diagramService = new DiagramService(diagramContext);
                    new DiagramServices().reveal(diagramService, List.of(parentNodeOnDiagram.get()));
                } else {
                    this.logAlreadyVisibleMessage(sourceElement, targetElement);
                }
            } else if (parentViewCreationRequest != null) {
                // The node doesn't exist on the diagram, it will be created with the ViewCreationRequest, we want to
                // make sure its compartment won't be visible after the drop.
                this.hideCompartments(parentViewCreationRequest, editingContext, diagramContext, convertedNodes);
            }
        }
    }

    private void logAlreadyVisibleMessage(Element element, Element parent) {
        String errorMessage = MessageFormat.format("The element {0} is already visible in its parent {1}", element.getName(), parent.getName());
        this.logger.warn(errorMessage);
        this.siriusWebCoreServices.feedbackMessageService().addFeedbackMessage(new Message(errorMessage, MessageLevel.WARNING));
    }

    /**
     * Hides the compartments of the {@link Node} that will be created by the provided
     * {@code parentViewCreationRequest}.
     * <p>
     * This method is typically called as part of drag & drop operations, where compartments may need to be hidden after
     * a drop action is performed.
     * </p>
     *
     * @param parentViewCreationRequest
     *            the creation request for the Node to hide the compartment from
     * @param editingContext
     *            the editing context
     * @param diagramContext
     *            the diagram context
     * @param convertedNodes
     *            the converted nodes
     */
    private void hideCompartments(ViewCreationRequest parentViewCreationRequest, IEditingContext editingContext, DiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        var diagramDescription = this.viewRepresentationDescriptionSearchService.findById(editingContext, diagramContext.diagram().getDescriptionId());
        if (diagramDescription.isPresent() && parentViewCreationRequest != null) {
            DiagramDescription representationDescription = (DiagramDescription) diagramDescription.get();
            String parentId = this.getParentElementId(parentViewCreationRequest, diagramContext);
            this.getViewNodeDescription(parentViewCreationRequest.getDescriptionId(), representationDescription, convertedNodes).ifPresent(parentNodeDescription -> {
                List<org.eclipse.sirius.components.view.diagram.NodeDescription> allChildren = Stream
                        .concat(parentNodeDescription.getChildrenDescriptions().stream(), parentNodeDescription.getReusedChildNodeDescriptions().stream()).toList();
                Set<String> childrenIdsToHide = new HashSet<>();
                for (org.eclipse.sirius.components.view.diagram.NodeDescription childNodeDescription : allChildren) {
                    // Create a dummy ViewCreationRequest to find out which ID will be given to the
                    // compartment of the dropped node.
                    ViewCreationRequest childViewCreationRequest = ViewCreationRequest.newViewCreationRequest()
                            .parentElementId(parentId)
                            .containmentKind(NodeContainmentKind.CHILD_NODE)
                            .descriptionId(convertedNodes.get(childNodeDescription).getId())
                            .targetObjectId(parentViewCreationRequest.getTargetObjectId())
                            .build();
                    childrenIdsToHide.add(this.getParentElementId(childViewCreationRequest, diagramContext));
                }
                // We can't use DiagramService here because the elements to hide aren't yet on the
                // diagram.
                diagramContext.diagramEvents().add(new HideDiagramElementEvent(childrenIdsToHide, true));
            });
        }
    }

    private Optional<org.eclipse.sirius.components.view.diagram.NodeDescription> getViewNodeDescription(String descriptionId, DiagramDescription diagramDescription,
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
     * Logs a message indicating an issue in the hierarchy of {@code parent} and {@code child}.
     * <p>
     * This method logs a message in the console as well as to the end user.
     * </p>
     *
     * @param parent
     *            the parent in the hierarchy
     * @param child
     *            the child in the hierarchy
     */
    private void logAncestorError(Element parent, Element child) {
        final String errorMessage;
        if (parent == child) {
            errorMessage = MessageFormat.format("Cannot drop {0} on itself", parent.getName());
        } else {
            errorMessage = MessageFormat.format("Cannot drop {0} on {1}: {0} is a parent of {1}", parent.getName(), child.getName());
        }
        this.logger.warn(errorMessage);
        this.siriusWebCoreServices.feedbackMessageService().addFeedbackMessage(new Message(errorMessage, MessageLevel.WARNING));
    }
}
