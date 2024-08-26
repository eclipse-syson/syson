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
package org.eclipse.syson.diagram.common.view.services;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramService;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramServices;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramService;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
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
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.ListLayoutStrategyDescription;
import org.eclipse.sirius.components.view.emf.IViewRepresentationDescriptionSearchService;
import org.eclipse.syson.diagram.common.view.nodes.AbstractActionsCompartmentNodeDescriptionProvider;
import org.eclipse.syson.services.DeleteService;
import org.eclipse.syson.services.ElementInitializerSwitch;
import org.eclipse.syson.services.NodeDescriptionService;
import org.eclipse.syson.services.ToolService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.ActorMembership;
import org.eclipse.syson.sysml.ConstraintDefinition;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.ObjectiveMembership;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.RequirementConstraintKind;
import org.eclipse.syson.sysml.RequirementDefinition;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateSubactionKind;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.SubjectMembership;
import org.eclipse.syson.sysml.Succession;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.UseCaseDefinition;
import org.eclipse.syson.sysml.UseCaseUsage;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.util.SysMLMetamodelHelper;
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

    private final Logger logger = LoggerFactory.getLogger(ViewToolService.class);

    private final DeleteService deleteService;

    private final UtilService utilService;

    private final NodeDescriptionService nodeDescriptionService;

    public ViewToolService(IObjectService objectService, IRepresentationDescriptionSearchService representationDescriptionSearchService,
            IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService, IFeedbackMessageService feedbackMessageService) {
        super(objectService, representationDescriptionSearchService, feedbackMessageService);
        this.viewRepresentationDescriptionSearchService = Objects.requireNonNull(viewRepresentationDescriptionSearchService);
        this.elementInitializerSwitch = new ElementInitializerSwitch();
        this.deleteService = new DeleteService();
        this.utilService = new UtilService();
        this.nodeDescriptionService = new NodeDescriptionService();
    }

    /**
     * Add the nodes representing {@code parentElement}'s children that are not present in the diagram or the
     * {@code parentViewCreationRequest}.
     * <p>
     * This method operates on {@link ViewCreationRequest}, meaning that it can create views for elements inside nodes
     * not yet present on the diagram. See
     * {@link #addExistingElements(Element, IEditingContext, IDiagramContext, Node, Map, boolean)} to add elements
     * inside existing nodes.
     * </p>
     *
     * @param parentElement
     *            the {@link Element} to add in the diagram
     * @param editingContext
     *            the editing context
     * @param diagramContext
     *            the diagram context
     * @param parentViewCreationRequest
     *            the creation request of the parent element (not yet rendered on the diagram)
     * @param convertedNodes
     *            the converted nodes
     * @param recursive
     *            whether the tool should add elements recursively or not
     * @return the created element
     */
    public Element addExistingElements(Element parentElement, IEditingContext editingContext, IDiagramContext diagramContext, ViewCreationRequest parentViewCreationRequest,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes, boolean recursive) {

        List<? extends Element> childElementsToRender = this.getChildElementsToRender(parentElement);

        var diagramDescription = this.viewRepresentationDescriptionSearchService.findById(editingContext, diagramContext.getDiagram().getDescriptionId());
        DiagramDescription representationDescription = (DiagramDescription) diagramDescription.get();


        final org.eclipse.sirius.components.view.diagram.NodeDescription parentNodeDescription;
        if (parentViewCreationRequest != null) {
            parentNodeDescription = this.getViewNodeDescription(parentViewCreationRequest.getDescriptionId(), representationDescription, convertedNodes)
                    .orElse(null);
        } else {
            parentNodeDescription = null;
        }

        for (Element childElement : childElementsToRender) {
            List<ViewCreationRequest> creationRequests = new ArrayList<>();
            boolean hasRenderedSynchronizedElement = false;
            if (parentViewCreationRequest == null) {
                creationRequests.add(this.createView(childElement, editingContext, diagramContext, (Node) null, convertedNodes));
            } else {
                if (parentNodeDescription.getChildrenLayoutStrategy() instanceof ListLayoutStrategyDescription) {
                    // The parent node has compartments, we want to add elements inside them if possible.
                    List<org.eclipse.sirius.components.view.diagram.NodeDescription> allChildren = Stream
                            .concat(parentNodeDescription.getChildrenDescriptions().stream(), parentNodeDescription.getReusedChildNodeDescriptions().stream()).toList();
                    for (org.eclipse.sirius.components.view.diagram.NodeDescription compartmentNodeDescription : allChildren) {
                        // We can't use the method getChildNodeDescriptionIdForRendering here because we can't access
                        // the parent node (it hasn't been created yet),
                        // nor the parent ViewCreationRequest because the compartment is synchronized and thus it is not
                        // created by a request.
                        List<NodeDescription> candidates = this.nodeDescriptionService.getChildNodeDescriptionsForRendering(childElement,
                                childElement.getOwner(), List.of(convertedNodes.get(compartmentNodeDescription)), convertedNodes);
                        if (!candidates.isEmpty()) {
                            String parentElementId = this.getParentElementId(parentViewCreationRequest,
                                    diagramContext);
                            String compartmentNodeId = new NodeIdProvider().getNodeId(parentElementId,
                                    convertedNodes.get(compartmentNodeDescription).getId(),
                                    NodeContainmentKind.CHILD_NODE,
                                    // The compartment has the same target object as its parent.
                                    parentViewCreationRequest.getTargetObjectId());
                            for (NodeDescription candidate : candidates) {
                                // Ignore synchronized nodes, this avoids unnecessary recursions that could create
                                // rendering issues when attempting to add an element as a child of a synchronized
                                // element. This is especially the case when creating views for synchronized elements
                                // inside a list compartment.
                                if (candidate.getSynchronizationPolicy().equals(SynchronizationPolicy.SYNCHRONIZED)) {
                                    hasRenderedSynchronizedElement = true;
                                } else {
                                    creationRequests.add(this.createView(childElement, compartmentNodeId, candidate.getId(), diagramContext, NodeContainmentKind.CHILD_NODE));
                                }
                            }
                        }
                    }
                } else {
                    // The parent doesn't have compartments, we want to add elements directly inside it if possible.
                    // This is for example the case with Package elements.
                    this.getChildNodeDescriptionIdForRendering(childElement, editingContext, diagramContext, parentViewCreationRequest, convertedNodes)
                            .ifPresent(descriptionId -> {
                                creationRequests.add(this.createView(childElement, editingContext, diagramContext, parentViewCreationRequest, convertedNodes));
                            });
                }
            }

            if (!creationRequests.isEmpty()) {
                if (recursive) {
                    creationRequests.forEach(creationRequest -> {
                        this.addExistingElements(childElement, editingContext, diagramContext, creationRequest, convertedNodes, recursive);
                    });
                }
            } else if (!hasRenderedSynchronizedElement) {
                // No element have been created for the current child, we try to find a parent that can represent it.
                // This is for example the case with usages semantically contained in other usages but graphically
                // displayed next to them.
                this.addElementInParent(childElement, parentViewCreationRequest.getParentElementId(), editingContext, diagramContext, convertedNodes, recursive);
            }
        }
        return parentElement;
    }

    /**
     * Add the nodes representing {@code parentElement}'s children that are not present in the diagram or the
     * {@code selectedNode}.
     * <p>
     * This method operates on {@link Node}, meaning that it can create views for elements inside nodes already present
     * on the diagram. See
     * {@link #addExistingElements(Element, IEditingContext, IDiagramContext, ViewCreationRequest, Map, boolean)} to add
     * elements on nodes that will be created by the next render.
     * </p>
     *
     * @param parentElement
     *            the {@link Element} to add in the diagram
     * @param editingContext
     *            the editing context
     * @param diagramContext
     *            the diagram context
     * @param selectedNode
     *            the creation request of the parent element (not yet rendered on the diagram)
     * @param convertedNodes
     *            the converted nodes
     * @param recursive
     *            whether the tool should add elements recursively or not
     * @return the created element
     */
    public Element addExistingElements(Element parentElement, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes, boolean recursive) {
        final List<? extends Element> childElementsToRender = this.getChildElementsToRender(parentElement);
        boolean hasRenderedSynchronizedElement = false;

        for (Element childElement : childElementsToRender) {
            List<ViewCreationRequest> creationRequests = new ArrayList<>();
            if (selectedNode == null) {
                creationRequests.add(this.createView(childElement, editingContext, diagramContext, selectedNode, convertedNodes));
            } else {
                if (selectedNode.getChildrenLayoutStrategy() instanceof ListLayoutStrategy) {
                    for (Node compartmentNode : selectedNode.getChildNodes()) {

                        NodeDescription compartmentNodeDescription = convertedNodes.values().stream()
                                .filter(nd -> Objects.equals(nd.getId(), compartmentNode.getDescriptionId()))
                                .findFirst()
                                .orElse(null);
                        List<NodeDescription> candidates = this.nodeDescriptionService.getChildNodeDescriptionsForRendering(childElement, childElement.getOwner(), List.of(compartmentNodeDescription),
                                convertedNodes);
                        for (NodeDescription candidate : candidates) {
                            // Ignore synchronized nodes, this avoids unnecessary recursions that could create
                            // rendering issues when attempting to add an element as a child of a synchronized
                            // element. This is especially the case when creating views for synchronized elements
                            // inside a list compartment.
                            if (candidate.getSynchronizationPolicy().equals(SynchronizationPolicy.SYNCHRONIZED)) {
                                hasRenderedSynchronizedElement = true;
                            } else {
                                creationRequests.add(this.createView(childElement, compartmentNode.getId(), candidate.getId(), diagramContext, NodeContainmentKind.CHILD_NODE));
                            }
                        }
                    }
                } else {
                    // The parent doesn't have compartments, we want to add elements directly inside it if
                    // possible.
                    // This is for example the case with Package elements.
                    this.getChildNodeDescriptionIdForRendering(childElement, editingContext, diagramContext, selectedNode, convertedNodes)
                            .ifPresent(descriptionId -> {
                                creationRequests.add(this.createView(childElement, editingContext, diagramContext, selectedNode, convertedNodes));
                            });
                }
            }
            if (!creationRequests.isEmpty()) {
                if (recursive) {
                    creationRequests.forEach(creationRequest -> {
                        this.addExistingElements(childElement, editingContext, diagramContext, creationRequest, convertedNodes, recursive);
                    });
                }
            } else if (!hasRenderedSynchronizedElement) {
                // Render in parent if the element hasn't been rendered by a creation request nor a synchronized node.
                final String parentElementId;
                if (selectedNode != null) {
                    Object parentObject = new NodeFinder(diagramContext.getDiagram()).getParent(selectedNode);
                    if (parentObject instanceof Node parentNode) {
                        parentElementId = parentNode.getId();
                    } else {
                        parentElementId = null;
                    }
                } else {
                    parentElementId = null;
                }
                this.addElementInParent(childElement, parentElementId, editingContext, diagramContext, convertedNodes, recursive);
            }
        }
        return parentElement;
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
        if (selectedNode != null) {
            optTargetElement = this.objectService.getObject(editingContext, selectedNode.getTargetObjectId());
        } else {
            optTargetElement = this.objectService.getObject(editingContext, diagramContext.getDiagram().getTargetObjectId());
        }
        if (optTargetElement.isPresent() && optTargetElement.get() instanceof Element targetElement) {
            // Check if the element we attempt to drop is in the ancestors of the target element. If it is the case we
            // want to prevent the drop.
            if (EMFUtils.isAncestor(element, targetElement)) {
                String errorMessage = MessageFormat.format("Cannot drop {0} in {1}: {0} is a parent of {1}", element.getName(), targetElement.getName());
                this.logger.warn(errorMessage);
                this.feedbackMessageService.addFeedbackMessage(new Message(errorMessage, MessageLevel.WARNING));
            } else {
                var optElementToDrop = Optional.ofNullable(element);
                if (element instanceof Membership membership) {
                    optElementToDrop = membership.getOwnedRelatedElement().stream().findFirst();
                }
                optElementToDrop.ifPresent(elementToDrop -> {
                    ViewCreationRequest parentViewCreationRequest = this.createView(elementToDrop, editingContext, diagramContext, selectedNode, convertedNodes);
                    Optional<Node> parentNodeOnDiagram = new NodeFinder(diagramContext.getDiagram())
                            .getOneNodeMatching(diagramNode -> Objects.equals(diagramNode.getId(), this.getParentElementId(parentViewCreationRequest, diagramContext)));
                    if (parentNodeOnDiagram.isPresent()) {
                        // The node already exist on the diagram, we don't need to create it.
                        // It is easier to check it in this order (first create the ViewCreationRequest then remove it)
                        // because we need the ViewCreationRequest anyways to check if the node is on the diagram.
                        diagramContext.getViewCreationRequests().remove(parentViewCreationRequest);
                        if (parentNodeOnDiagram.get().getModifiers().contains(ViewModifier.Hidden)) {
                            // The node exists on the diagram but is hidden, we can't create a new view representing it,
                            // but we reveal it.
                            IDiagramService diagramService = new DiagramService(diagramContext);
                            new DiagramServices().reveal(diagramService, List.of(parentNodeOnDiagram.get()));
                        } else {
                            String errorMessage = MessageFormat.format("The element {0} is already visible in its parent {1}", element.getName(), targetElement.getName());
                            this.logger.warn(errorMessage);
                            this.feedbackMessageService.addFeedbackMessage(new Message(errorMessage, MessageLevel.WARNING));
                        }
                    } else {
                        this.hideCompartments(parentViewCreationRequest, editingContext, diagramContext, convertedNodes);
                    }
                });
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
        final Element result;
        // Check if the element we attempt to drop is in the ancestors of the target element. If it is the case we want
        // to prevent the drop.
        if (EMFUtils.isAncestor(droppedElement, targetElement)) {
            String errorMessage = MessageFormat.format("Cannot drop {0} in {1}: {0} is a parent of {1}", droppedElement.getName(), targetElement.getName());
            this.logger.warn(errorMessage);
            this.feedbackMessageService.addFeedbackMessage(new Message(errorMessage, MessageLevel.WARNING));
            // Null prevents the drop and makes Sirius Web reset the position of the dragged element.
            result = null;
        } else {
            this.moveElement(droppedElement, droppedNode, targetElement, targetNode, editingContext, diagramContext, convertedNodes);
            result = droppedElement;
        }
        return result;
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
    private void hideCompartments(ViewCreationRequest parentViewCreationRequest, IEditingContext editingContext, IDiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        var diagramDescription = this.viewRepresentationDescriptionSearchService.findById(editingContext, diagramContext.getDiagram().getDescriptionId());
        if (diagramDescription.isPresent()) {
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
     * {@link #addExistingElements(Element, IEditingContext, IDiagramContext, Node, Map, boolean)} to navigate the model
     * and find the elements to display.
     * </p>
     *
     * @param parentElement
     *            the parent element
     * @return the list of contained elements that should be rendered
     */
    private List<? extends Element> getChildElementsToRender(Element parentElement) {
        final List<? extends Element> childElements;
        if (parentElement instanceof Usage usage) {
            childElements = usage.getNestedUsage();
        } else if (parentElement instanceof Definition definition) {
            childElements = definition.getOwnedUsage();
        } else if (parentElement instanceof Namespace np) {
            childElements = np.getOwnedMember();
        } else {
            childElements = List.of();
        }
        return childElements;
    }

    /**
     * Creates a view representing the provided {@code element} in its closest parent.
     * <p>
     * This method looks for the provided {@code parentElementId} in both the {@link ViewCreationRequest} and the
     * existing nodes in the diagram. If the parent cannot be found the element is added on the diagram.
     * </p>
     *
     * @param element
     *            the element to create a view from
     * @param parentElementId
     *            the identifier of the parent that should contain the created view
     * @param editingContext
     *            the editing context
     * @param diagramContext
     *            the diagram context
     * @param convertedNodes
     *            the converted nodes
     * @param recursive
     *            whether the creation is recursive
     * @return the element
     */
    private Element addElementInParent(Element element, String parentElementId, IEditingContext editingContext, IDiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes, boolean recursive) {
        diagramContext.getViewCreationRequests().stream()
                .filter(vc -> Objects.equals(this.getParentElementId(vc, diagramContext), parentElementId))
                .findFirst()
                .ifPresentOrElse(vc -> {
                    var req = this.createView(element, editingContext, diagramContext, vc, convertedNodes);
                    if (recursive) {
                        this.addExistingElements(element, editingContext, diagramContext, req, convertedNodes, recursive);
                    }
                }, () -> {
                    new NodeFinder(diagramContext.getDiagram()).getOneNodeMatching(n -> Objects.equals(n.getId(), parentElementId))
                            .ifPresentOrElse(n -> {
                                var req = this.createView(element, editingContext, diagramContext, n, convertedNodes);
                                if (recursive) {
                                    this.addExistingElements(element, editingContext, diagramContext, req, convertedNodes, recursive);
                                }
                            }, () -> {
                                var req = this.createView(element, editingContext, diagramContext, (Node) null, convertedNodes);
                                if (recursive) {
                                    this.addExistingElements(element, editingContext, diagramContext, req, convertedNodes, recursive);
                                }
                            });
                });
        return element;
    }

    /**
     * Check if a tool that will create an instance of the given type should be available for a diagram associated to
     * the given {@link Namespace}.
     *
     * @param ns
     *            the given {@link Namespace}.
     * @param type
     *            the given type.
     * @return <code>true</code> if the tool should be available, <code>false</code> otherwise.
     */
    public boolean toolShouldBeAvailable(Namespace ns, String type) {
        boolean toolShouldBeAvailable = false;
        EClass domainClass = SysMLMetamodelHelper.toEClass(type);
        if (ns instanceof Package) {
            toolShouldBeAvailable = true;
        } else if (ns instanceof Usage && !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass)) {
            toolShouldBeAvailable = true;
        } else if (ns instanceof Definition && !SysmlPackage.eINSTANCE.getDefinition().isSuperTypeOf(domainClass)) {
            toolShouldBeAvailable = true;
        }
        return toolShouldBeAvailable;
    }

    public Usage becomeNestedUsage(Usage usage, Element newContainer) {
        if (this.getOwnerHierarchy(newContainer).contains(usage) || Objects.equals(newContainer, usage)) {
            String message = MessageFormat.format("Cannot change the owner of {0}, this would create a containment cycle", String.valueOf(usage.getName()));
            this.feedbackMessageService.addFeedbackMessage(new Message(message, MessageLevel.WARNING));
            this.logger.warn(message);
        } else {
            this.changeOwner(usage, newContainer);
            usage.setIsComposite(true);
        }
        return usage;
    }

    private Element changeOwner(Element element, Element newContainer) {
        var eContainer = element.eContainer();
        if (eContainer instanceof FeatureMembership featureMembership) {
            newContainer.getOwnedRelationship().add(featureMembership);
        } else if (eContainer instanceof OwningMembership owningMembership) {
            var newFeatureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
            // Set the container of newFeatureMembership first to make sure features owned by
            // element aren't lost when changing its container.
            newContainer.getOwnedRelationship().add(newFeatureMembership);
            newFeatureMembership.getOwnedRelatedElement().add(element);
            this.deleteService.deleteFromModel(owningMembership);
        }
        return element;
    }

    private List<Element> getOwnerHierarchy(Element element) {
        List<Element> ownerHierarchy = new ArrayList<>();
        Element currentElement = element;
        while (currentElement.getOwner() != null) {
            ownerHierarchy.add(currentElement.getOwner());
            currentElement = currentElement.getOwner();
        }
        return ownerHierarchy;
    }

    public RequirementUsage becomeObjectiveRequirement(RequirementUsage requirement, Element newContainer) {
        if (newContainer instanceof UseCaseUsage || newContainer instanceof UseCaseDefinition) {
            if (ViewCreateService.isEmptyObjectiveRequirement(newContainer)) {
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

    public PartUsage addAsNestedPart(PartDefinition partDefinition, PartUsage partUsage) {
        var eContainer = partUsage.eContainer();
        if (eContainer instanceof FeatureMembership featureMembership) {
            partDefinition.getOwnedRelationship().add(featureMembership);
        } else if (eContainer instanceof OwningMembership owningMembership) {
            var newFeatureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
            newFeatureMembership.getOwnedRelatedElement().add(partUsage);
            partDefinition.getOwnedRelationship().add(newFeatureMembership);
            this.deleteService.deleteFromModel(owningMembership);
        }
        return partUsage;
    }

    public Element dropSubjectFromDiagram(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext, IDiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (targetElement instanceof RequirementUsage
                || targetElement instanceof RequirementDefinition
                || targetElement instanceof UseCaseUsage
                || targetElement instanceof UseCaseDefinition) {
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
        if (targetElement instanceof UseCaseUsage
                || targetElement instanceof UseCaseDefinition) {
            if (ViewCreateService.isEmptyObjectiveRequirement(targetElement)) {
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

    private void moveContraintInRequirementConstraintCompartment(ConstraintUsage droppedConstraint, Element requirement, RequirementConstraintKind kind) {
        var oldMembership = droppedConstraint.eContainer();
        var membership = SysmlFactory.eINSTANCE.createRequirementConstraintMembership();
        membership.getOwnedRelatedElement().add(droppedConstraint);
        membership.setKind(kind);
        requirement.getOwnedRelationship().add(membership);
        if (oldMembership instanceof OwningMembership owningMembership) {
            this.deleteService.deleteFromModel(owningMembership);
        }
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
            result = this.changeOwner(otherEnd, newSource);
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

    private Package getClosestContainingPackageFrom(Element element) {
        var owner = element.eContainer();
        while (!(owner instanceof Package) && owner != null) {
            owner = owner.eContainer();
        }
        return (Package) owner;
    }

    /**
     * Create a new graphical view for an element inside a compartment given its label.
     *
     * @param childElement
     *            the semantic object for which the view is created.
     * @param compartmentName
     *            the label of the compartment in which the view should be created.
     * @param selectedNode
     *            the {@link Node} where the tool was triggered. It can be an element or the compartment itself.
     * @param editingContext
     *            the {@link IEditingContext} of the tool
     * @param diagramContext
     *            the {@link IDiagramContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param convertedNodes
     *            the map of all existing node descriptions in the DiagramDescription of this Diagram. It corresponds to
     *            a variable accessible from the variable manager.
     */
    public Element createViewInFreeFormCompartment(Element childElement, String compartmentName, Node selectedNode,
            IEditingContext editingContext,
            IDiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {

        Node parentNode = null;

        if (selectedNode.getInsideLabel() != null && Objects.equals(selectedNode.getInsideLabel().getText(), compartmentName)) {
            parentNode = selectedNode;
        } else {
            parentNode = selectedNode.getChildNodes().stream()
                    .filter(child -> child.getInsideLabel() != null)
                    .filter(child -> Objects.equals(child.getInsideLabel().getText(), compartmentName))
                .findFirst()
                .orElse(null);
        }

        if (parentNode != null) {
            this.createView(childElement, editingContext, diagramContext, parentNode, convertedNodes);
        }

        return childElement;
    }

    /**
     * Create a new TransitionUsage and set it as the child of the parent of the source {@link Feature}. Sets its source
     * and target.
     *
     * @param sourceUsage
     *            the {@link Feature} used as a source for the transition
     * @param targetUsage
     *            the {@link Feature} used as a target for the transition
     * @return the given source {@link Feature}.
     */
    public Feature createTransitionUsage(Feature sourceUsage, Feature targetUsage) {
        // Check source and target have the same parent
        Element sourceParentElement = sourceUsage.getOwner();
        Element targetParentElement = targetUsage.getOwner();
        if (sourceParentElement != targetParentElement || this.utilService.isParallelState(sourceParentElement)) {
            // Should probably not be here as the transition creation should not be allowed.
            return sourceUsage;
        }
        // Create transition usage and add it to the parent element
        // sourceParentElement <>-> FeatureMembership -> RelatedElement = TransitionUsage
        TransitionUsage newTransitionUsage = SysmlFactory.eINSTANCE.createTransitionUsage();
        var featureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        featureMembership.getOwnedRelatedElement().add(newTransitionUsage);
        sourceParentElement.getOwnedRelationship().add(featureMembership);

        // Create EndFeature
        // TransitionUsage <>-> Membership -> MemberElement = sourceAction
        var sourceMembership = SysmlFactory.eINSTANCE.createMembership();
        newTransitionUsage.getOwnedRelationship().add(sourceMembership);
        sourceMembership.setMemberElement(sourceUsage);

        // Create Succession
        // TransitionUsage <>-> FeatureMembership -> RelatedElement = succession
        Succession succession = SysmlFactory.eINSTANCE.createSuccession();
        this.elementInitializerSwitch.doSwitch(succession);
        var successionFeatureMembership = SysmlFactory.eINSTANCE.createFeatureMembership();
        successionFeatureMembership.getOwnedRelatedElement().add(succession);
        newTransitionUsage.getOwnedRelationship().add(successionFeatureMembership);

        // Set Succession Source and Target Features
        succession.getOwnedRelationship().add(this.createConnectorEndFeatureMembership(sourceUsage));
        succession.getOwnedRelationship().add(this.createConnectorEndFeatureMembership(targetUsage));
        this.elementInitializerSwitch.doSwitch(newTransitionUsage);

        return sourceUsage;
    }

    /**
     * <>-> EndFeatureMembership -> RelatedElement = Feature <>-> ReferenceSubsetting -> ReferencedFeature = feature
     *
     * @param feature
     *            The feature to reference subset
     * @return
     */
    private EndFeatureMembership createConnectorEndFeatureMembership(Feature feature) {
        var successionSourceEndFeatureMembership = SysmlFactory.eINSTANCE.createEndFeatureMembership();
        var successionSourceEndFeatureFeature = SysmlFactory.eINSTANCE.createFeature();
        successionSourceEndFeatureMembership.getOwnedRelatedElement().add(successionSourceEndFeatureFeature);

        var successionSourceRefSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        successionSourceRefSubsetting.setReferencedFeature(feature);
        successionSourceEndFeatureFeature.getOwnedRelationship().add(successionSourceRefSubsetting);
        return successionSourceEndFeatureMembership;
    }

    /**
     * Create a new {@link ActionUsage} as a child of {@code parentState}. The {@code actionKind} string is matched
     * against possible {@link StateSubactionKind} values to set the new {@link ActionUsage} of the correct kind.
     *
     * @param parentState
     *            The state onto which the action is added
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
     * @param actionKind
     *            The value against which the {@link StateSubactionKind} of the {@link ActionUsage} membership is set.
     * @return
     */
    public ActionUsage createOwnedAction(Element parentState, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes, String actionKind) {
        ActionUsage childAction = this.utilService.createChildAction(parentState, actionKind);

        if (childAction != null) {
            if (diagramContext.getDiagram().getLabel().equals("General View")) {
                this.createView(childAction, editingContext, diagramContext, diagramContext.getDiagram(), convertedNodes);
            } else if (selectedNode.getInsideLabel().getText().equals(AbstractActionsCompartmentNodeDescriptionProvider.ACTIONS_COMPARTMENT_LABEL)) {
                this.createView(childAction, editingContext, diagramContext, selectedNode, convertedNodes);
            } else {
                selectedNode.getChildNodes().stream().filter(child -> child.getInsideLabel().getText().equals(AbstractActionsCompartmentNodeDescriptionProvider.ACTIONS_COMPARTMENT_LABEL)).findFirst()
                        .ifPresent(compartmentNode -> {
                            this.createView(childAction, editingContext, diagramContext, compartmentNode, convertedNodes);
                        });
            }
        }

        return childAction;
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
        if (self instanceof StateUsage state && selectedNode instanceof Node node) {
            var realParentNode = node.getChildNodes().stream().filter(childNode -> childNode.getInsideLabel().getText().contains(STATE_TRANSITION_COMPARTMENT_NAME)).findFirst();
            if (realParentNode.isPresent()) {
                return realParentNode.get();
            }
        }
        return selectedNode;
    }
}
