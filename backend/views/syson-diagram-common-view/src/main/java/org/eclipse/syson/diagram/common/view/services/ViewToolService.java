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
package org.eclipse.syson.diagram.common.view.services;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramService;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramServices;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramDescriptionService;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramService;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.ListLayoutStrategy;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.ViewCreationRequest;
import org.eclipse.sirius.components.diagrams.ViewDeletionRequest;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.diagrams.components.NodeContainmentKind;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.diagrams.description.SynchronizationPolicy;
import org.eclipse.sirius.components.diagrams.events.HideDiagramElementEvent;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.emf.IViewRepresentationDescriptionSearchService;
import org.eclipse.syson.services.ElementInitializerSwitch;
import org.eclipse.syson.services.ToolService;
import org.eclipse.syson.services.api.ISysMLMoveElementService;
import org.eclipse.syson.services.api.ViewDefinitionKind;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.ActorMembership;
import org.eclipse.syson.sysml.CaseDefinition;
import org.eclipse.syson.sysml.CaseUsage;
import org.eclipse.syson.sysml.Comment;
import org.eclipse.syson.sysml.ConstraintDefinition;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Documentation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.ObjectiveMembership;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.RequirementConstraintKind;
import org.eclipse.syson.sysml.RequirementDefinition;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.SubjectMembership;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TextualRepresentation;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.UseCaseDefinition;
import org.eclipse.syson.sysml.UseCaseUsage;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tool-related Java services used by all diagrams.
 *
 * @author arichard
 */
public class ViewToolService extends ToolService {

    private static final String STATE_TRANSITION_COMPARTMENT_NAME = "state transition";

    protected final IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService;

    protected final ElementInitializerSwitch elementInitializerSwitch;

    protected final Logger logger = LoggerFactory.getLogger(ViewToolService.class);

    public ViewToolService(IIdentityService identityService, IObjectSearchService objectSearchService, IRepresentationDescriptionSearchService representationDescriptionSearchService,
            IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService, IDiagramDescriptionService diagramDescriptionService,
            IFeedbackMessageService feedbackMessageService, ISysMLMoveElementService moveService) {
        super(identityService, objectSearchService, representationDescriptionSearchService, diagramDescriptionService, feedbackMessageService, moveService);
        this.viewRepresentationDescriptionSearchService = Objects.requireNonNull(viewRepresentationDescriptionSearchService);
        this.elementInitializerSwitch = new ElementInitializerSwitch();
    }

    /**
     * For the given element, search its ViewUsage (if this service has been called from the diagram background it will
     * be the ViewUsage itself), then get its container and add all container's children to the exposed elements of the
     * ViewUsage.
     *
     * @param element
     *            the given {@link Element}.
     * @param recursive
     *            if the process should add elements recursively.
     * @param editingContext
     *            the {@link IEditingContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param diagramContext
     *            the {@link IDiagramContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param selectedNode
     *            the selected node on which the service has been called (may be null if the tool has been called from
     *            the diagram). It corresponds to a variable accessible from the variable manager.
     * @param convertedNodes
     *            the map of all existing node descriptions in the DiagramDescription of this Diagram. It corresponds to
     *            a variable accessible from the variable manager.
     * @return the given {@link Element}.
     */
    public Element addToExposedElements(Element element, boolean recursive, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        var viewUsage = this.getViewUsage(editingContext, diagramContext, selectedNode);
        if (viewUsage != null) {
            var childElements = this.getChildElementsToRender(element, recursive);
            for (Element childElement : childElements) {
                if (this.isUnsynchronized(childElement)) {
                    this.handleUnsynchronizedElement(childElement, element, editingContext, diagramContext, selectedNode, convertedNodes);
                } else if (!Objects.equals(viewUsage, childElement) && !(childElement instanceof ViewUsage) && !this.isExposed(childElement, viewUsage)) {
                    var membershipExpose = SysmlFactory.eINSTANCE.createMembershipExpose();
                    membershipExpose.setImportedMembership(childElement.getOwningMembership());
                    viewUsage.getOwnedRelationship().add(membershipExpose);
                    if (childElement instanceof Package || recursive) {
                        membershipExpose.setIsRecursive(true);
                    }
                }
            }
        }
        return element;
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
     *            the {@link IDiagramContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param selectedNode
     *            the selected node on which the service has been called (may be null if the tool has been called from
     *            the diagram). It corresponds to a variable accessible from the variable manager.
     * @param convertedNodes
     *            the map of all existing node descriptions in the DiagramDescription of this Diagram. It corresponds to
     *            a variable accessible from the variable manager.
     * @return the given {@link Element}.
     */
    public Element expose(Element element, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (this.isUnsynchronized(element)) {
            final Element parentElement;
            if (selectedNode == null) {
                parentElement = this.objectSearchService.getObject(editingContext, diagramContext.getDiagram().getTargetObjectId())
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
        Optional<Object> optTargetElement;
        Optional<org.eclipse.sirius.components.view.diagram.NodeDescription> optNodeDescription = Optional.empty();
        if (selectedNode != null) {
            optTargetElement = this.objectSearchService.getObject(editingContext, selectedNode.getTargetObjectId());
            optNodeDescription = convertedNodes.entrySet().stream().filter(entry -> entry.getValue().getId().equals(selectedNode.getDescriptionId())).map(Entry::getKey).findFirst();
        } else {
            optTargetElement = this.objectSearchService.getObject(editingContext, diagramContext.getDiagram().getTargetObjectId());
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
     *            the {@link IDiagramContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param convertedNodes
     *            the map of all existing node descriptions in the DiagramDescription of this Diagram. It corresponds to
     *            a variable accessible from the variable manager.
     * @return the input {@link Element}.
     */
    public Element dropElementFromDiagram(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext, IDiagramContext diagramContext,
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
        } else {
            this.moveElement(droppedElement, droppedNode, realTargetElement, targetNode, editingContext, diagramContext, convertedNodes);
            result = droppedElement;
        }
        return result;
    }

    /**
     * Check if a tool that will create an instance of the given type should be available for a diagram associated to
     * the given {@link Element}.
     *
     * @param element
     *            the given {@link Element}.
     * @param newElementType
     *            the given type.
     * @return <code>true</code> if the tool should be available, <code>false</code> otherwise.
     */
    public boolean toolShouldBeAvailable(Element element, IEditingContext editingContext, IDiagramContext diagramContext, EClass newElementType) {
        ViewDefinitionKind viewDefinitionKind = this.utilService.getViewDefinitionKind(element, List.of(), editingContext, diagramContext);
        var elt = this.utilService.getViewUsageOwner(element);

        return switch (viewDefinitionKind) {
            case INTERCONNECTION_VIEW -> this.toolShouldBeAvailableOnInterconnectionView(elt, newElementType);
            case ACTION_FLOW_VIEW -> this.toolShouldBeAvailableOnActionFlowView(elt, newElementType);
            case STATE_TRANSITION_VIEW -> this.toolShouldBeAvailableOnStateTransitionView(elt, newElementType);
            default -> this.toolShouldBeAvailableOnGeneralView(elt, newElementType);
        };
    }

    private boolean toolShouldBeAvailableOnGeneralView(Element element, EClass domainClass) {
        boolean toolShouldBeAvailable = false;
        if (element instanceof Package) {
            toolShouldBeAvailable = true;
        } else if (element instanceof Usage && !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass)) {
            toolShouldBeAvailable = true;
        } else if (element instanceof Definition && !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass)) {
            toolShouldBeAvailable = true;
        }
        return toolShouldBeAvailable;
    }

    private boolean toolShouldBeAvailableOnInterconnectionView(Element element, EClass domainClass) {
        boolean toolShouldBeAvailable = false;
        if (element instanceof Package) {
            toolShouldBeAvailable = true;
        } else if (element instanceof Usage && !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass)) {
            toolShouldBeAvailable = true;
        } else if (element instanceof Definition && !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass)) {
            toolShouldBeAvailable = true;
        }
        return toolShouldBeAvailable;
    }

    private boolean toolShouldBeAvailableOnActionFlowView(Element element, EClass domainClass) {
        boolean toolShouldBeAvailable = false;
        if (element instanceof ActionUsage) {
            toolShouldBeAvailable = !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass);
        } else if (element instanceof ActionDefinition) {
            toolShouldBeAvailable = !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass);
        } else {
            if (SysmlPackage.eINSTANCE.getActionUsage().equals(domainClass) || SysmlPackage.eINSTANCE.getActionUsage().isSuperTypeOf(domainClass)) {
                toolShouldBeAvailable = true;
            } else if (SysmlPackage.eINSTANCE.getActionDefinition().equals(domainClass) || SysmlPackage.eINSTANCE.getActionDefinition().isSuperTypeOf(domainClass)) {
                toolShouldBeAvailable = true;
            }
        }
        return toolShouldBeAvailable;
    }

    private boolean toolShouldBeAvailableOnStateTransitionView(Element element, EClass domainClass) {
        boolean toolShouldBeAvailable = false;
        if (element instanceof StateUsage) {
            toolShouldBeAvailable = !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass);
        } else if (element instanceof StateDefinition) {
            toolShouldBeAvailable = !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass);
        } else {
            if (SysmlPackage.eINSTANCE.getStateUsage().equals(domainClass) || SysmlPackage.eINSTANCE.getStateUsage().isSuperTypeOf(domainClass)) {
                toolShouldBeAvailable = true;
            } else if (SysmlPackage.eINSTANCE.getStateDefinition().equals(domainClass) || SysmlPackage.eINSTANCE.getStateDefinition().isSuperTypeOf(domainClass)) {
                toolShouldBeAvailable = true;
            }
        }
        return toolShouldBeAvailable;
    }

    public Usage becomeNestedUsage(Usage usage, Element newContainer) {
        if (this.getOwnerHierarchy(newContainer).contains(usage) || Objects.equals(newContainer, usage)) {
            String message = MessageFormat.format("Cannot change the owner of {0}, this would create a containment cycle", String.valueOf(usage.getName()));
            this.feedbackMessageService.addFeedbackMessage(new Message(message, MessageLevel.WARNING));
            this.logger.warn(message);
        } else {
            this.moveService.moveSemanticElement(usage, newContainer);
            usage.setIsComposite(true);
        }
        return usage;
    }

    public RequirementUsage becomeObjectiveRequirement(RequirementUsage requirement, Element newContainer) {
        if (newContainer instanceof UseCaseUsage || newContainer instanceof UseCaseDefinition) {
            if (this.utilService.isEmptyObjectiveRequirement(newContainer)) {
                var eContainer = requirement.eContainer();
                if (eContainer instanceof ObjectiveMembership objectiveMembership) {
                    // requirement is already an objective of another usecaseXXX
                    newContainer.getOwnedRelationship().add(objectiveMembership);
                } else if (eContainer instanceof OwningMembership owningMembership) {
                    var newObjectiveMembership = SysmlFactory.eINSTANCE.createObjectiveMembership();
                    newObjectiveMembership.getOwnedRelatedElement().add(requirement);
                    newContainer.getOwnedRelationship().add(newObjectiveMembership);
                    this.deleteService.deleteFromModel(owningMembership);
                }
            }
        }
        return requirement;
    }

    public Element dropSubjectFromDiagram(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext, IDiagramContext diagramContext,
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
                this.moveElement(droppedElement, droppedNode, targetElement, targetNode, editingContext, diagramContext, convertedNodes);
            }
        }
        return droppedElement;
    }

    public Element dropObjectiveRequirementFromDiagram(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext, IDiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (targetElement instanceof CaseUsage
                || targetElement instanceof CaseDefinition) {
            if (this.utilService.isEmptyObjectiveRequirement(targetElement)) {
                this.moveElement(droppedElement, droppedNode, targetElement, targetNode, editingContext, diagramContext, convertedNodes);
            }
        }
        return droppedElement;
    }

    public Element dropElementFromDiagramInRequirementAssumeConstraintCompartment(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext,
            IDiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (droppedElement instanceof ConstraintUsage droppedConstraint && (targetElement instanceof RequirementUsage || targetElement instanceof RequirementDefinition)) {
            this.moveContraintInRequirementConstraintCompartment(droppedConstraint, targetElement, RequirementConstraintKind.ASSUMPTION);
            this.createView(droppedElement, editingContext, diagramContext, targetNode, convertedNodes);
            diagramContext.getViewDeletionRequests().add(ViewDeletionRequest.newViewDeletionRequest().elementId(droppedNode.getId()).build());
        }
        return droppedElement;
    }

    public Element dropElementFromDiagramInRequirementRequireConstraintCompartment(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext,
            IDiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (droppedElement instanceof ConstraintUsage droppedConstraint && (targetElement instanceof RequirementUsage || targetElement instanceof RequirementDefinition)) {
            this.moveContraintInRequirementConstraintCompartment(droppedConstraint, targetElement, RequirementConstraintKind.REQUIREMENT);
            this.createView(droppedElement, editingContext, diagramContext, targetNode, convertedNodes);
            diagramContext.getViewDeletionRequests().add(ViewDeletionRequest.newViewDeletionRequest().elementId(droppedNode.getId()).build());
        }
        return droppedElement;
    }

    public Element dropElementFromDiagramInConstraintCompartment(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext,
            IDiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (droppedElement instanceof ConstraintUsage droppedConstraint) {
            if (targetElement instanceof ConstraintUsage || targetElement instanceof ConstraintDefinition) {
                var oldMembership = droppedConstraint.eContainer();
                var membership = SysmlFactory.eINSTANCE.createFeatureMembership();
                membership.getOwnedRelatedElement().add(droppedConstraint);
                targetElement.getOwnedRelationship().add(membership);
                if (oldMembership instanceof OwningMembership owningMembership) {
                    this.deleteService.deleteFromModel(owningMembership);
                }
                this.createView(droppedElement, editingContext, diagramContext, targetNode, convertedNodes);
                diagramContext.getViewDeletionRequests().add(ViewDeletionRequest.newViewDeletionRequest().elementId(droppedNode.getId()).build());
            }
        }
        return droppedElement;
    }

    /**
     * Reconnects the source of a nested actor edge.
     * <p>
     * The source of this edge is either an UseCase or a Requirement, and can only be reconnected to UseCase or
     * Requirements.
     * </p>
     *
     * @param self
     *            the current UseCase or Requirement
     * @param newSource
     *            the new UseCase or Requirement
     * @param otherEnd
     *            the Actor connected to the UseCase or Requirement
     * @return the Actor
     */
    public Element reconnectSourceNestedActorEdge(Element self, Element newSource, Element otherEnd) {
        if (newSource instanceof UseCaseUsage || newSource instanceof UseCaseDefinition
                || newSource instanceof RequirementUsage || newSource instanceof RequirementDefinition) {
            if (otherEnd.getOwningMembership() instanceof ActorMembership actorMembership) {
                newSource.getOwnedRelationship().add(actorMembership);
            } else {
                // This is an error, an Actor should always be contained in an ActorMembership.
                String errorMessage = "Cannot reconnect the Actor, it is not owned by an " + ActorMembership.class.getSimpleName();
                this.logger.error(errorMessage);
                this.feedbackMessageService.addFeedbackMessage(new Message(errorMessage, MessageLevel.ERROR));
            }
        } else {
            String errorMessage = "Cannot reconnect an Actor to a non-UseCase, non-Requirement element";
            this.logger.warn(errorMessage);
            this.feedbackMessageService.addFeedbackMessage(new Message(errorMessage, MessageLevel.WARNING));
        }
        return otherEnd;
    }

    public Element reconnnectSourceCompositionEdge(Element self, Element newSource, Element otherEnd) {
        Element result = otherEnd;
        if (this.getOwnerHierarchy(newSource).contains(otherEnd) || Objects.equals(newSource, otherEnd)) {
            String message = MessageFormat.format("Cannot change the owner of {0}, this would create a containment cycle", String.valueOf(otherEnd.getName()));
            this.feedbackMessageService.addFeedbackMessage(new Message(message, MessageLevel.WARNING));
            this.logger.warn(message);
        } else {
            this.moveService.moveSemanticElement(otherEnd, newSource);
            result = otherEnd;
        }
        return result;
    }

    public Element reconnnectTargetCompositionEdge(Element self, Element oldTarget, Element newTarget, Element otherEnd) {
        if (this.getOwnerHierarchy(otherEnd).contains(newTarget) || Objects.equals(otherEnd, newTarget)) {
            String message = MessageFormat.format("Cannot change the owner of {0}, this would create a containment cycle", String.valueOf(otherEnd.getName()));
            this.feedbackMessageService.addFeedbackMessage(new Message(message, MessageLevel.WARNING));
            this.logger.warn(message);
        } else {
            var oldContainer = oldTarget.eContainer();
            if (newTarget instanceof Usage && oldContainer instanceof FeatureMembership featureMembership) {
                // Move the old target to the innermost package
                // We don't use moveToClosestContainingPackage here because we are moving oldTarget in the closest
                // containing package and updating newTarget at the same time.
                var pack = this.getClosestContainingPackageFrom(self);
                if (pack != null) {
                    var owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
                    pack.getOwnedRelationship().add(owningMembership);
                    owningMembership.getOwnedRelatedElement().add(oldTarget);
                    // reuse feature membership of the previous nested
                    var oldMembership = newTarget.eContainer();
                    featureMembership.getOwnedRelatedElement().add(newTarget);
                    if (oldMembership instanceof OwningMembership oldOwningMembership) {
                        this.deleteService.deleteFromModel(oldOwningMembership);
                    }
                }
            }
        }
        return self;
    }

    /**
     * Reconnects the annotation to the annotated element target.
     * <p>
     * The target of this edge can be all elements except a Documentation or a Comment.
     * </p>
     *
     * @param self
     *            the current annotating element
     * @param newTarget
     *            the new Annotated element
     * @return the annotating element
     */
    public Element reconnnectTargetAnnotatedEdge(Element self, Element newTarget) {
        if (!(newTarget instanceof Comment) && !(newTarget instanceof Documentation)) {
            this.moveService.moveSemanticElement(self, newTarget);
        }
        return self;
    }

    /**
     * Moves the provided {@code usage} in the closest containing package.
     *
     * @param usage
     *            the usage to move
     * @return the moved element
     */
    public Element moveToClosestContainingPackage(Usage usage) {
        var pack = this.getClosestContainingPackageFrom(usage);
        if (pack != null) {
            var oldMembership = usage.eContainer();
            var owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
            pack.getOwnedRelationship().add(owningMembership);
            owningMembership.getOwnedRelatedElement().add(usage);
            if (oldMembership instanceof OwningMembership oldOwningMembership) {
                this.deleteService.deleteFromModel(oldOwningMembership);
            }
        }
        return usage;
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
     *            the {@link IDiagramContext} of the tool. It corresponds to a variable accessible from the variable
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
    public StateUsage createChildState(Element parentState, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
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
     * Return the real parent {@link Node} given the current object and the selectedNode.
     *
     * @param self
     *            the current object.
     * @param selectedNode
     *            the selectedNode (can be a {@link Node} or null (in case of a {@link Diagram}).
     * @return the real parent {@link Node} given the current object and the selectedNode.
     */
    public Object getParentViewExpression(Object self, Object selectedNode) {
        if (self instanceof StateUsage && selectedNode instanceof Node node) {
            var realParentNode = node.getChildNodes().stream().filter(childNode -> childNode.getInsideLabel().getText().contains(STATE_TRANSITION_COMPARTMENT_NAME)).findFirst();
            if (realParentNode.isPresent()) {
                return realParentNode.get();
            }
        }
        return selectedNode;
    }

    /**
     * Service to retrieve the root elements of the selection dialog of the NamespaceImport creation tool.
     *
     * @param editingContext
     *            the editing context
     * @return the list of resources that contain at least one {@link Package}
     */
    public List<Resource> getNamespaceImportSelectionDialogElements(IEditingContext editingContext) {
        var optionalResourceSet = Optional.of(editingContext)
                .filter(IEMFEditingContext.class::isInstance)
                .map(IEMFEditingContext.class::cast)
                .map(IEMFEditingContext::getDomain)
                .map(EditingDomain::getResourceSet);
        var resources = optionalResourceSet.map(resourceSet -> resourceSet.getResources().stream()
                .filter(resource -> this.containsDirectlyOrIndirectlyInstancesOf(resource, SysmlPackage.eINSTANCE.getPackage()))
                .toList())
                .orElseGet(ArrayList::new);
        return resources.stream().sorted((r1, r2) -> this.getResourceName(r1).compareTo(this.getResourceName(r2))).toList();
    }

    /**
     * Service to retrieve the children of a given element in the selection dialog of the NamespaceImport creation tool.
     *
     * @param self
     *            an element of the tree
     * @return the list of {@link Package} element found under the given root element.
     */
    public List<Package> getNamespaceImportSelectionDialogChildren(Object self) {
        List<Package> result = new ArrayList<>();
        if (self instanceof Resource resource) {
            resource.getContents().stream()
                    .filter(Element.class::isInstance)
                    .map(Element.class::cast)
                    .forEach(element -> result.addAll(this.findClosestPackageInChildren(element)));
        } else if (self instanceof Package packageElement) {
            packageElement.getOwnedRelationship().stream()
                    .filter(Membership.class::isInstance)
                    .map(Membership.class::cast)
                    .forEach(membership -> result.addAll(this.findClosestPackageInChildren(membership)));
        }
        return result;
    }

    /**
     * Provides the root elements in the tree of the selection dialog for the StakeholderParameter creation tool.
     *
     * @param editingContext
     *            the (non-{@code null}) {@link IEditingContext}.
     * @return the (non-{@code null}) {@link List} of all {@link Resource} that contain at least one {@link PartUsage}.
     */
    public List<Resource> getStakeholderSelectionDialogElements(IEditingContext editingContext) {
        return this.getAllResourcesWithInstancesOf(editingContext, SysmlPackage.eINSTANCE.getPartUsage());
    }

    /**
     * Provides the children of element in the tree of the selection dialog for the StakeholderParameter creation tool.
     *
     * @param selectionDialogTreeElement
     *            a (non-{@code null}) selection dialog tree element.
     * @return the (non-{@code null}) {@link List} of all children that contain (possibly indirectly) or are
     *         {@link PartUsage}.
     */
    public List<? extends Object> getStakeholderSelectionDialogChildren(Object selectionDialogTreeElement) {
        return this.getChildrenWithInstancesOf(selectionDialogTreeElement, SysmlPackage.eINSTANCE.getPartUsage());
    }

    /**
     * Provides the root elements in the tree of the selection dialog for the SubjectParameter creation tool.
     *
     * @param editingContext
     *            the (non-{@code null}) {@link IEditingContext}.
     * @return the (non-{@code null}) {@link List} of all {@link Resource} that contain at least one {@link Type}.
     */
    public List<Resource> getSubjectSelectionDialogElements(IEditingContext editingContext) {
        return this.getAllResourcesWithInstancesOf(editingContext, SysmlPackage.eINSTANCE.getUsage());
    }

    /**
     * Provides the children of element in the tree of the selection dialog for the SubjectParameter creation tool.
     *
     * @param selectionDialogTreeElement
     *            a (non-{@code null}) selection dialog tree element.
     * @return the (non-{@code null}) {@link List} of all children that contain (possibly indirectly) or are
     *         {@link Usage}.
     */
    public List<? extends Object> getSubjectSelectionDialogChildren(Object selectionDialogTreeElement) {
        return this.getChildrenWithInstancesOf(selectionDialogTreeElement, SysmlPackage.eINSTANCE.getUsage());
    }

    /**
     * Provides the root elements in the tree of the selection dialog for the ActorParameter creation tool.
     *
     * @param editingContext
     *            the (non-{@code null}) {@link IEditingContext}.
     * @return the (non-{@code null}) {@link List} of all {@link Resource} that contain at least one {@link PartUsage}.
     */
    public List<Resource> getActorSelectionDialogElements(IEditingContext editingContext) {
        return this.getAllResourcesWithInstancesOf(editingContext, SysmlPackage.eINSTANCE.getPartUsage());
    }

    /**
     * Provides the children of element in the tree of the selection dialog for the ActorParameter creation tool.
     *
     * @param selectionDialogTreeElement
     *            a (non-{@code null}) selection dialog tree element.
     * @return the (non-{@code null}) {@link List} of all children that contain (possibly indirectly) or are
     *         {@link PartUsage}.
     */
    public List<? extends Object> getActorSelectionDialogChildren(Object selectionDialogTreeElement) {
        return this.getChildrenWithInstancesOf(selectionDialogTreeElement, SysmlPackage.eINSTANCE.getPartUsage());
    }

    /**
     * Provides the root elements in the tree of the selection dialog for presenting all existing ActionUsage.
     *
     * @param editingContext
     *            the (non-{@code null}) {@link IEditingContext}.
     * @return the (non-{@code null}) {@link List} of all {@link Resource} that contain at least one {@link ActionUsage}.
     */
    public List<Resource> getActionReferenceSelectionDialogElements(IEditingContext editingContext) {
        return this.getAllResourcesWithInstancesOf(editingContext, SysmlPackage.eINSTANCE.getActionUsage());
    }

    /**
     * Provides the children of element in the tree of the selection dialog for presenting all existing ActionUsage.
     *
     * @param selectionDialogTreeElement
     *            a (non-{@code null}) selection dialog tree element.
     * @return the (non-{@code null}) {@link List} of all children that contain (possibly indirectly) an {@link ActionUsage}.
     */
    public List<? extends Object> getActionReferenceSelectionDialogChildren(Object selectionDialogTreeElement) {
        return this.getChildrenWithInstancesOf(selectionDialogTreeElement, SysmlPackage.eINSTANCE.getActionUsage());
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
     *            the {@link IDiagramContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param selectedNode
     *            the selected node on which the service has been called (may be null if the tool has been called from
     *            the diagram). It corresponds to a variable accessible from the variable manager.
     * @param convertedNodes
     *            the map of all existing node descriptions in the DiagramDescription of this Diagram. It corresponds to
     *            a variable accessible from the variable manager.
     */
    protected void handleUnsynchronizedElement(Element element, Element parentElement, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (selectedNode == null) {
            this.createView(element, editingContext, diagramContext, selectedNode, convertedNodes);
        } else if (element instanceof Documentation && (parentElement instanceof Package || parentElement instanceof NamespaceImport || parentElement instanceof ViewUsage)) {
            var parentNode = new NodeFinder(diagramContext.getDiagram()).getParent(selectedNode);
            this.createView(element, editingContext, diagramContext, parentNode, convertedNodes);
        } else if (element instanceof Comment && !(element instanceof Documentation)) {
            var parentNode = new NodeFinder(diagramContext.getDiagram()).getParent(selectedNode);
            this.createView(element, editingContext, diagramContext, parentNode, convertedNodes);
        } else if (element instanceof TextualRepresentation) {
            var parentNode = new NodeFinder(diagramContext.getDiagram()).getParent(selectedNode);
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
     * Search and retrieve the ViewUsage corresponding to the parent Node/diagram of the given Node.
     *
     * @param editingContext
     *            the {@link IEditingContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param diagramContext
     *            the {@link IDiagramContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param node
     *            the selected node on which the element has been dropped (may be null if the tool has been called from
     *            the diagram). It corresponds to a variable accessible from the variable manager.
     * @return an Optional ViewUsage if found, an empty Optional otherwise.
     */
    protected ViewUsage getViewUsage(IEditingContext editingContext, IDiagramContext diagramContext, Node node) {
        Optional<ViewUsage> optViewUsage = Optional.empty();
        if (node == null) {
            optViewUsage = this.objectSearchService.getObject(editingContext, diagramContext.getDiagram().getTargetObjectId())
                    .filter(ViewUsage.class::isInstance)
                    .map(ViewUsage.class::cast);
        } else {
            optViewUsage = this.objectSearchService.getObject(editingContext, node.getTargetObjectId())
                    .filter(ViewUsage.class::isInstance)
                    .map(ViewUsage.class::cast);
        }
        if (optViewUsage.isEmpty()) {
            List<Node> rootNodes = diagramContext.getDiagram().getNodes();
            for (Node rootNode : rootNodes) {
                if (Objects.equals(rootNode, node)) {
                    optViewUsage = this.objectSearchService.getObject(editingContext, diagramContext.getDiagram().getTargetObjectId())
                            .filter(ViewUsage.class::isInstance)
                            .map(ViewUsage.class::cast);
                    break;
                }
            }
        }
        if (optViewUsage.isEmpty()) {
            List<Node> rootNodes = diagramContext.getDiagram().getNodes();
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
            optViewUsage = this.objectSearchService.getObject(editingContext, diagramContext.getDiagram().getTargetObjectId())
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

    /**
     * Drops the provided {@code sourceElement} from the explorer on the given {@code selectedNode} on the diagram.
     * <p>
     * This method may perform graphical actions (e.g. create a view to represent the dropped element) as well as
     * semantic actions (e.g. set a feature typing). Note that the provided {@code targetElement} should be the semantic
     * element represented by {@code selectedNode}.
     * </p>
     * <p>
     * This method doesn't perform a drop of an element on the diagram to another element on the diagram. See
     * {@link #dropElementFromDiagram(Element, Node, Element, Node, IEditingContext, IDiagramContext, Map)} for
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
     *            the {@link IDiagramContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param selectedNode
     *            the selected node on which the service has been called (may be null if the tool has been called from
     *            the diagram). It corresponds to a variable accessible from the variable manager.
     * @param convertedNodes
     *            the map of all existing node descriptions in the DiagramDescription of this Diagram. It corresponds to
     *            a variable accessible from the variable manager.
     */
    protected void dropElementFromExplorerInTarget(Element sourceElement, Element targetElement, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (sourceElement instanceof Definition definition && targetElement instanceof Usage usage && !(targetElement instanceof ViewUsage)) {
            // Dropping a definition on a usage types the usage with the definition. It doesn't create a new node on the
            // diagram.
            this.utilService.setFeatureTyping(usage, definition);
        } else if (this.isExposable(sourceElement)) {
            this.expose(sourceElement, editingContext, diagramContext, selectedNode, convertedNodes);
        } else {
            ViewCreationRequest parentViewCreationRequest = this.createView(sourceElement, editingContext, diagramContext, selectedNode, convertedNodes);
            Optional<Node> parentNodeOnDiagram = new NodeFinder(diagramContext.getDiagram())
                    .getOneNodeMatching(diagramNode -> Objects.equals(diagramNode.getId(), this.getParentElementId(parentViewCreationRequest, diagramContext)));
            if (parentNodeOnDiagram.isPresent()) {
                // The node already exist on the diagram, we don't need to create it.
                // It is easier to check it in this order (first create the ViewCreationRequest then remove
                // it) because we need the ViewCreationRequest anyways to check if the node is on the diagram.
                diagramContext.getViewCreationRequests().remove(parentViewCreationRequest);
                if (parentNodeOnDiagram.get().getModifiers().contains(ViewModifier.Hidden)) {
                    // The node exists on the diagram but is hidden, we can't create a new view representing
                    // it, but we reveal it.
                    IDiagramService diagramService = new DiagramService(diagramContext);
                    new DiagramServices().reveal(diagramService, List.of(parentNodeOnDiagram.get()));
                } else {
                    String errorMessage = MessageFormat.format("The element {0} is already visible in its parent {1}", sourceElement.getName(), targetElement.getName());
                    this.logger.warn(errorMessage);
                    this.feedbackMessageService.addFeedbackMessage(new Message(errorMessage, MessageLevel.WARNING));
                }
            } else if (parentViewCreationRequest != null) {
                // The node doesn't exist on the diagram, it will be created with the ViewCreationRequest, we want to
                // make sure its compartment won't be visible after the drop.
                this.hideCompartments(parentViewCreationRequest, editingContext, diagramContext, convertedNodes);
            }
        }
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
    protected void hideCompartments(ViewCreationRequest parentViewCreationRequest, IEditingContext editingContext, IDiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        var diagramDescription = this.viewRepresentationDescriptionSearchService.findById(editingContext, diagramContext.getDiagram().getDescriptionId());
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
                diagramContext.getDiagramEvents().add(new HideDiagramElementEvent(childrenIdsToHide, true));
            });
        }
    }

    /**
     * Returns the elements contained by {@code parentElement} that should be rendered.
     * <p>
     * This method is typically used by
     * {@link #addToExposedElements(Element, IEditingContext, IDiagramContext, Node, Map, boolean)} to navigate the
     * model and find the elements to display.
     * </p>
     *
     * @param parentElement
     *            the parent element
     * @param recursive
     *            the method should search for unsynchronized elements recursively
     * @return the list of contained elements that should be rendered
     */
    protected Set<Element> getChildElementsToRender(Element parentElement, boolean recursive) {
        var contents = new LinkedHashSet<Element>();
        Set<Element> childElements = new LinkedHashSet<>();
        if (parentElement instanceof ActionUsage || parentElement instanceof PartUsage) {
            // ActionUsage and PartUsage can contain Membership referencing actions from the standard library (start and
            // done). We want to retrieve these membership as part of the child elements to render (e.g. to display them
            // as part of an addExistingElement service).
            Usage usage = (Usage) parentElement;
            childElements.addAll(usage.getNestedUsage());
            childElements.addAll(this.utilService.collectSuccessionSourceAndTarget(usage));
        } else if (parentElement instanceof ActionDefinition || parentElement instanceof PartDefinition) {
            Definition definition = (Definition) parentElement;
            childElements.addAll(definition.getOwnedUsage());
            childElements.addAll(this.utilService.collectSuccessionSourceAndTarget(definition));
        } else if (parentElement instanceof Usage usage) {
            childElements.addAll(usage.getNestedUsage());
        } else if (parentElement instanceof Definition definition) {
            childElements.addAll(definition.getOwnedUsage());
        } else if (parentElement instanceof Namespace np) {
            childElements.addAll(np.getOwnedMember());
            childElements.addAll(np.getOwnedImport());
        }
        contents.addAll(childElements);
        if (recursive) {
            childElements.forEach(child -> {
                Set<Element> childElementsToRender = this.getChildElementsToRender(child, true);
                childElementsToRender.removeIf(elt -> !this.isUnsynchronized(elt));
                contents.addAll(childElementsToRender);
            });
        }
        return contents;
    }

    protected List<Resource> getAllResourcesWithInstancesOf(IEditingContext editingContext, EClassifier eClassifier) {
        Objects.requireNonNull(editingContext);

        var optResourceSet = Optional.of(editingContext)
                .filter(IEMFEditingContext.class::isInstance)
                .map(IEMFEditingContext.class::cast)
                .map(IEMFEditingContext::getDomain)
                .map(EditingDomain::getResourceSet);
        var resourcesContainingPartUsage = optResourceSet.map(resourceSet -> resourceSet.getResources().stream()
                .filter(resource -> this.containsDirectlyOrIndirectlyInstancesOf(resource, eClassifier))
                .toList())
                .orElseGet(ArrayList::new);
        return resourcesContainingPartUsage.stream().sorted(Comparator.comparing(r -> this.getResourceName(r))).toList();
    }

    protected List<? extends Object> getChildrenWithInstancesOf(Object selectionDialogTreeElement, EClassifier eClassifier) {
        Objects.requireNonNull(selectionDialogTreeElement);

        final List<? extends Object> result;

        if (selectionDialogTreeElement instanceof Resource resource) {
            result = resource.getContents().stream()
                    .filter(content -> eClassifier.isInstance(content) || this.containsDirectlyOrIndirectlyInstancesOf(content, eClassifier)).toList();
        } else if (selectionDialogTreeElement instanceof Element sysmlElement) {
            return sysmlElement.getOwnedRelationship().stream()
                    .filter(Membership.class::isInstance)
                    .map(Membership.class::cast)
                    .map(Membership::getOwnedRelatedElement).flatMap(List::stream)
                    .filter(content -> eClassifier.isInstance(content) || this.containsDirectlyOrIndirectlyInstancesOf(content, eClassifier)).toList();
        } else {
            result = new ArrayList<>();
        }

        return result;
    }

    /**
     * Check is the given element can be added to the exposed elements of a ViewUsage.
     *
     * @param element
     *            the given {@link Element}.
     * @return <code>true</code> if the given element can be added to the exposed elements of a ViewUsage,
     *         <code>false</code> otherwise.
     */
    protected boolean isExposable(Element element) {
        boolean isExposable = false;
        var declaredName = element.getDeclaredName();
        if (this.utilService.isStandardDoneAction(element) || this.utilService.isStandardStartAction(element)) {
            isExposable = false;
        } else {
            isExposable = declaredName != null && !declaredName.isBlank();
        }
        return isExposable;
    }

    protected boolean containsDirectlyOrIndirectlyInstancesOf(Resource resource, EClassifier eClassifier) {
        boolean found = false;
        final Iterator<EObject> allContents = resource.getAllContents();
        while (!found && allContents.hasNext()) {
            found = eClassifier.isInstance(allContents.next());
        }
        return found;
    }

    protected boolean containsDirectlyOrIndirectlyInstancesOf(EObject eObject, EClassifier eClassifier) {
        boolean found = false;
        final Iterator<EObject> allContents = eObject.eAllContents();
        while (!found && allContents.hasNext()) {
            found = eClassifier.isInstance(allContents.next());
        }
        return found;
    }

    protected String getResourceName(Resource resource) {
        return resource.eAdapters().stream()
                .filter(ResourceMetadataAdapter.class::isInstance)
                .map(ResourceMetadataAdapter.class::cast)
                .findFirst()
                .map(ResourceMetadataAdapter::getName)
                .orElse(resource.getURI().lastSegment());
    }

    protected List<Package> findClosestPackageInChildren(Element element) {
        var result = new ArrayList<Package>();
        if (element instanceof Package packageElement) {
            result.add(packageElement);
        } else if (element instanceof Membership membership) {
            membership.getOwnedRelatedElement()
                    .forEach(child -> result.addAll(this.findClosestPackageInChildren(child)));
        } else {
            element.getOwnedRelationship().stream()
                    .filter(Membership.class::isInstance)
                    .map(Membership.class::cast)
                    .forEach(membership -> result.addAll(this.findClosestPackageInChildren(membership)));
        }
        return result;
    }

    protected Package getClosestContainingPackageFrom(Element element) {
        var owner = element.eContainer();
        while (!(owner instanceof Package) && owner != null) {
            owner = owner.eContainer();
        }
        return (Package) owner;
    }

    protected void moveContraintInRequirementConstraintCompartment(ConstraintUsage droppedConstraint, Element requirement, RequirementConstraintKind kind) {
        var oldMembership = droppedConstraint.eContainer();
        var membership = SysmlFactory.eINSTANCE.createRequirementConstraintMembership();
        membership.getOwnedRelatedElement().add(droppedConstraint);
        membership.setKind(kind);
        requirement.getOwnedRelationship().add(membership);
        if (oldMembership instanceof OwningMembership owningMembership) {
            this.deleteService.deleteFromModel(owningMembership);
        }
    }

    protected List<Element> getOwnerHierarchy(Element element) {
        List<Element> ownerHierarchy = new ArrayList<>();
        Element currentElement = element;
        while (currentElement.getOwner() != null) {
            ownerHierarchy.add(currentElement.getOwner());
            currentElement = currentElement.getOwner();
        }
        return ownerHierarchy;
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
    protected void logAncestorError(Element parent, Element child) {
        final String errorMessage;
        if (parent == child) {
            errorMessage = MessageFormat.format("Cannot drop {0} on itself", parent.getName());
        } else {
            errorMessage = MessageFormat.format("Cannot drop {0} on {1}: {0} is a parent of {1}", parent.getName(), child.getName());
        }
        this.logger.warn(errorMessage);
        this.feedbackMessageService.addFeedbackMessage(new Message(errorMessage, MessageLevel.WARNING));
    }
}
