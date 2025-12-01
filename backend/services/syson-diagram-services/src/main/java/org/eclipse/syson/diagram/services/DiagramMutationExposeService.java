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

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.ListLayoutStrategy;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.diagrams.components.NodeContainmentKind;
import org.eclipse.sirius.components.diagrams.components.NodeIdProvider;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.diagrams.description.SynchronizationPolicy;
import org.eclipse.sirius.components.diagrams.events.HideDiagramElementEvent;
import org.eclipse.syson.model.services.ModelQueryElementService;
import org.eclipse.syson.services.DeleteService;
import org.eclipse.syson.services.NodeDescriptionService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.services.api.SiriusWebCoreServices;
import org.eclipse.syson.services.api.ViewDefinitionKind;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Comment;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Documentation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expose;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.TextualRepresentation;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.util.NodeFinder;
import org.springframework.stereotype.Service;

/**
 * Expose-related services doing mutations in diagrams and models.
 *
 * @author arichard
 */
@Service
public class DiagramMutationExposeService {

    private final SiriusWebCoreServices siriusWebCoreServices;

    private final DiagramMutationElementService diagramMutationElementService;

    private final DiagramQueryElementService diagramQueryElementService;

    private final ModelQueryElementService modelQueryElementService;

    private final DeleteService deleteService;

    private final UtilService utilService;

    private final NodeDescriptionService nodeDescriptionService;

    public DiagramMutationExposeService(SiriusWebCoreServices siriusWebCoreServices, DiagramMutationElementService diagramMutationElementService, DiagramQueryElementService diagramQueryElementService,
            ModelQueryElementService modelQueryElementService) {
        this.siriusWebCoreServices = Objects.requireNonNull(siriusWebCoreServices);
        this.diagramMutationElementService = Objects.requireNonNull(diagramMutationElementService);
        this.diagramQueryElementService = Objects.requireNonNull(diagramQueryElementService);
        this.modelQueryElementService = Objects.requireNonNull(modelQueryElementService);
        this.deleteService = new DeleteService();
        this.utilService = new UtilService();
        this.nodeDescriptionService = new NodeDescriptionService(siriusWebCoreServices.objectSearchService());
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
                parentElement = this.siriusWebCoreServices.objectSearchService().getObject(editingContext, diagramContext.diagram().getTargetObjectId())
                        .filter(Element.class::isInstance)
                        .map(Element.class::cast)
                        .orElse(null);
            } else {
                parentElement = this.siriusWebCoreServices.objectSearchService().getObject(editingContext, selectedNode.getTargetObjectId())
                        .filter(Element.class::isInstance)
                        .map(Element.class::cast)
                        .orElse(null);
            }
            this.handleUnsynchronizedElement(element, parentElement, editingContext, diagramContext, selectedNode, convertedNodes);
        } else {
            var viewUsage = this.diagramQueryElementService.getViewUsage(editingContext, diagramContext, selectedNode);
            if (viewUsage != null && !viewUsage.getExposedElement().contains(element)) {
                // if it is the General View, we want to hide tree elements if a compartment containing the same
                // element is displayed or it is displayed as border node
                if (selectedNode != null && ViewDefinitionKind.isGeneralView(this.utilService.getViewDefinitionKind(element, List.of(), editingContext))) {
                    this.hideNodeIfVisibleCompartmentCouldHostTheFutureNode(element, editingContext, diagramContext, selectedNode, convertedNodes);
                    this.hideNodeIfBorderNodeCouldHostTheFutureNode(element, editingContext, diagramContext, selectedNode, convertedNodes);
                    this.hideNodeIfNestedIsDefault(element, editingContext, diagramContext, selectedNode, convertedNodes);
                }
                var membershipExpose = SysmlFactory.eINSTANCE.createMembershipExpose();
                membershipExpose.setImportedMembership(element.getOwningMembership());
                viewUsage.getOwnedRelationship().add(membershipExpose);
            }
        }
        return element;
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
    public Element addToExposedElements(Element element, boolean recursive, IEditingContext editingContext, DiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        var viewUsage = this.diagramQueryElementService.getViewUsage(editingContext, diagramContext, selectedNode);
        if (viewUsage != null) {
            var childElements = this.getChildElementsToRender(element, recursive);
            for (Element childElement : childElements) {
                if (this.utilService.isUnsynchronized(childElement)) {
                    this.handleUnsynchronizedElement(childElement, element, editingContext, diagramContext, selectedNode, convertedNodes);
                } else if (this.modelQueryElementService.isExposable(element) && !Objects.equals(viewUsage, childElement) && !(childElement instanceof ViewUsage)
                        && !viewUsage.getExposedElement().contains(childElement)) {
                    var membershipExpose = SysmlFactory.eINSTANCE.createMembershipExpose();
                    membershipExpose.setImportedMembership(childElement.getOwningMembership());
                    viewUsage.getOwnedRelationship().add(membershipExpose);
                    if (recursive) {
                        membershipExpose.setIsRecursive(true);
                    }
                }
            }
        }
        return element;
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
        var optDiagramTargetObject = this.siriusWebCoreServices.objectSearchService().getObject(editingContext, diagramContext.diagram().getTargetObjectId());
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

    /**
     * Returns the elements contained by {@code parentElement} that should be rendered.
     * <p>
     * This method is typically used by
     * {@link #addToExposedElements(Element, IEditingContext, DiagramContext, Node, Map, boolean)} to navigate the model
     * and find the elements to display.
     * </p>
     *
     * @param parentElement
     *            the parent element
     * @param recursive
     *            the method should search for unsynchronized elements recursively
     * @return the list of contained elements that should be rendered
     */
    private Set<Element> getChildElementsToRender(Element parentElement, boolean recursive) {
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
                childElementsToRender.removeIf(elt -> !this.utilService.isUnsynchronized(elt));
                contents.addAll(childElementsToRender);
            });
        }
        return contents;
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
    private void handleUnsynchronizedElement(Element element, Element parentElement, IEditingContext editingContext, DiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (selectedNode == null) {
            this.diagramMutationElementService.createView(element, editingContext, diagramContext, selectedNode, convertedNodes);
        } else if (element instanceof Documentation && (parentElement instanceof Package || parentElement instanceof NamespaceImport || parentElement instanceof ViewUsage)) {
            var parentNode = new NodeFinder(diagramContext.diagram()).getParent(selectedNode);
            this.diagramMutationElementService.createView(element, editingContext, diagramContext, parentNode, convertedNodes);
        } else if (element instanceof Comment && !(element instanceof Documentation)) {
            var parentNode = new NodeFinder(diagramContext.diagram()).getParent(selectedNode);
            this.diagramMutationElementService.createView(element, editingContext, diagramContext, parentNode, convertedNodes);
        } else if (element instanceof TextualRepresentation) {
            var parentNode = new NodeFinder(diagramContext.diagram()).getParent(selectedNode);
            this.diagramMutationElementService.createView(element, editingContext, diagramContext, parentNode, convertedNodes);
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
                            this.diagramMutationElementService.createView(element, compartmentNode.getId(), candidate.getId(), editingContext, diagramContext, NodeContainmentKind.CHILD_NODE);
                        }
                    }
                }
            } else {
                // The parent doesn't have compartments, we want to add elements directly inside it if possible.
                // This is for example the case with Package elements.
                this.diagramMutationElementService.getChildNodeDescriptionIdForRendering(element, editingContext, diagramContext, selectedNode, convertedNodes)
                        .ifPresent(descriptionId -> {
                            this.diagramMutationElementService.createView(element, editingContext, diagramContext, selectedNode, convertedNodes);
                        });
            }
        }
    }

    private void removeFromExposedElements(Node currentNode, IEditingContext editingContext, List<Expose> exposed) {
        List<Node> childNodes = currentNode.getChildNodes();
        for (Node childNode : childNodes) {
            this.siriusWebCoreServices.objectSearchService().getObject(editingContext, childNode.getTargetObjectId()).ifPresent(childElt -> {
                this.deleteExpose(exposed, childElt);
            });
            this.removeFromExposedElements(childNode, editingContext, exposed);
        }
    }

    private void deleteExpose(List<Expose> exposed, Object exposedElement) {
        for (Expose expose : exposed) {
            if (Objects.equals(exposedElement, expose.getImportedElement())) {
                this.deleteService.deleteFromModel(expose);
            }
        }
    }

    private void hideNodeIfVisibleCompartmentCouldHostTheFutureNode(Element element, IEditingContext editingContext, DiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        // 1- get visible compartments of selected node
        // 2- get visible compartments that could host the future node
        // 3- if a visible compartment could host the future node, then hide the tree node
        List<Node> visibleCompartments = selectedNode.getChildNodes().stream().filter(n -> n.getState().equals(ViewModifier.Normal)).toList();
        boolean visibleCompartmentsThatCouldHostTheFutureNode = false;
        for (Node visibleCompartment : visibleCompartments) {
            Optional<String> childNodeDescriptionIdForRendering = this.diagramMutationElementService.getChildNodeDescriptionIdForRendering(element, editingContext, diagramContext, visibleCompartment,
                    convertedNodes);
            if (childNodeDescriptionIdForRendering.isPresent()) {
                visibleCompartmentsThatCouldHostTheFutureNode = true;
                break;
            }
        }
        if (visibleCompartmentsThatCouldHostTheFutureNode) {
            var parentId = this.diagramQueryElementService.getGraphicalParentId(diagramContext, selectedNode);
            var descriptionId = this.diagramQueryElementService.getNodeDescriptionId(element, diagramContext.diagram(), editingContext);
            if (parentId != null && descriptionId.isPresent()) {
                var nodeId = new NodeIdProvider().getNodeId(parentId,
                        descriptionId.get(),
                        NodeContainmentKind.CHILD_NODE,
                        this.siriusWebCoreServices.identityService().getId(element));
                diagramContext.diagramEvents().add(new HideDiagramElementEvent(Set.of(nodeId), true));
            }
        }
    }

    private void hideNodeIfBorderNodeCouldHostTheFutureNode(Element element, IEditingContext editingContext, DiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        // 1- get border nodes descriptions that could host the future node
        // 3- if a border nodes description could host the future node, then hide the tree node
        boolean borderNodeDecriptionCouldHostTheFutureNode = false;
        var childNodeDescriptionIdForRendering = this.diagramMutationElementService.getChildNodeDescriptionIdForRendering(element, editingContext, diagramContext, selectedNode,
                convertedNodes);
        // Child nodes are composed of compartment nodes and border nodes but compartment nodes are not appropriate
        // candidates, so only border nodes are remaining.
        if (childNodeDescriptionIdForRendering.isPresent()) {
            borderNodeDecriptionCouldHostTheFutureNode = true;
        }
        if (borderNodeDecriptionCouldHostTheFutureNode) {
            var parentId = this.diagramQueryElementService.getGraphicalParentId(diagramContext, selectedNode);
            var descriptionId = this.diagramQueryElementService.getNodeDescriptionId(element, diagramContext.diagram(), editingContext);
            if (parentId != null && descriptionId.isPresent()) {
                var nodeId = new NodeIdProvider().getNodeId(parentId,
                        descriptionId.get(),
                        NodeContainmentKind.CHILD_NODE,
                        this.siriusWebCoreServices.identityService().getId(element));
                diagramContext.diagramEvents().add(new HideDiagramElementEvent(Set.of(nodeId), true));
            }
        }
    }

    private void hideNodeIfNestedIsDefault(Element element, IEditingContext editingContext, DiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (element instanceof AttributeUsage || element instanceof PortUsage || element instanceof ItemUsage) {
            var parentId = this.diagramQueryElementService.getGraphicalParentId(diagramContext, selectedNode);
            var descriptionId = this.diagramQueryElementService.getNodeDescriptionId(element, diagramContext.diagram(), editingContext);
            if (parentId != null && descriptionId.isPresent()) {
                var nodeId = new NodeIdProvider().getNodeId(parentId,
                        descriptionId.get(),
                        NodeContainmentKind.CHILD_NODE,
                        this.siriusWebCoreServices.identityService().getId(element));
                boolean hide = true;
                if (this.modelQueryElementService.isExposed(element, this.diagramQueryElementService.getViewUsage(editingContext, diagramContext, selectedNode))) {
                    hide = false;
                }
                diagramContext.diagramEvents().add(new HideDiagramElementEvent(Set.of(nodeId), hide));
            }
        }
    }
}
