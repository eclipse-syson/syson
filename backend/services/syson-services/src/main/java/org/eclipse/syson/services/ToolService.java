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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.diagrams.CollapsingState;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.FreeFormLayoutStrategy;
import org.eclipse.sirius.components.diagrams.InsideLabel;
import org.eclipse.sirius.components.diagrams.InsideLabelLocation;
import org.eclipse.sirius.components.diagrams.LabelOverflowStrategy;
import org.eclipse.sirius.components.diagrams.LabelStyle;
import org.eclipse.sirius.components.diagrams.LineStyle;
import org.eclipse.sirius.components.diagrams.ListLayoutStrategy;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.Position;
import org.eclipse.sirius.components.diagrams.RectangularNodeStyle;
import org.eclipse.sirius.components.diagrams.Size;
import org.eclipse.sirius.components.diagrams.ViewCreationRequest;
import org.eclipse.sirius.components.diagrams.ViewDeletionRequest;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.diagrams.components.NodeContainmentKind;
import org.eclipse.sirius.components.diagrams.components.NodeIdProvider;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tool-related Java services used by SysON representations.
 *
 * @author arichard
 */
public class ToolService {

    protected final IObjectService objectService;

    protected final IRepresentationDescriptionSearchService representationDescriptionSearchService;

    protected final IFeedbackMessageService feedbackMessageService;

    private final Logger logger = LoggerFactory.getLogger(ToolService.class);

    public ToolService(IObjectService objectService, IRepresentationDescriptionSearchService representationDescriptionSearchService, IFeedbackMessageService feedbackMessageService) {
        this.objectService = Objects.requireNonNull(objectService);
        this.representationDescriptionSearchService = Objects.requireNonNull(representationDescriptionSearchService);
        this.feedbackMessageService = Objects.requireNonNull(feedbackMessageService);
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

    protected void createView(Element element, IEditingContext editingContext, IDiagramContext diagramContext, Object selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        this.createView(element, editingContext, diagramContext, selectedNode, convertedNodes, NodeContainmentKind.CHILD_NODE);
    }

    protected void createView(Element element, IEditingContext editingContext, IDiagramContext diagramContext, Object selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes, NodeContainmentKind nodeKind) {
        var parentElementId = this.getParentElementId(diagramContext, selectedNode);
        var descriptionId = this.getDescriptionId(element, editingContext, diagramContext, selectedNode, convertedNodes);

        if (descriptionId.isPresent()) {
            var request = ViewCreationRequest.newViewCreationRequest()
                    .containmentKind(nodeKind)
                    .descriptionId(descriptionId.get())
                    .parentElementId(parentElementId)
                    .targetObjectId(this.objectService.getId(element))
                    .build();
            diagramContext.getViewCreationRequests().add(request);
        }
    }

    protected String getParentElementId(IDiagramContext diagramContext, Object selectedNode) {
        if (selectedNode instanceof Node node) {
            return node.getId();
        }
        return diagramContext.getDiagram().getId();
    }

    protected Optional<String> getDescriptionId(Element element, IEditingContext editingContext, IDiagramContext diagramContext, Object selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        // The NodeDescription must be a child of the Parent Node/Diagram
        var diagramDescription = this.representationDescriptionSearchService.findById(editingContext, diagramContext.getDiagram().getDescriptionId());

        final var childNodeDescriptions = new ArrayList<>();

        if (selectedNode instanceof Node node) {
            childNodeDescriptions.addAll(convertedNodes.values().stream().filter(nodeDesc -> nodeDesc.getId().equals(node.getDescriptionId())).flatMap(nodeDesc -> Stream
                    .concat(nodeDesc.getChildNodeDescriptions().stream(), convertedNodes.values().stream().filter(convNode -> nodeDesc.getReusedChildNodeDescriptionIds().contains(convNode.getId()))))
                    .toList());
        } else {
            childNodeDescriptions.addAll(diagramDescription.filter(org.eclipse.sirius.components.diagrams.description.DiagramDescription.class::isInstance)
                    .map(org.eclipse.sirius.components.diagrams.description.DiagramDescription.class::cast)
                    .map(org.eclipse.sirius.components.diagrams.description.DiagramDescription::getNodeDescriptions).orElse(List.of()));
        }

        var domainType = SysMLMetamodelHelper.buildQualifiedName(element.eClass());

        return convertedNodes.keySet().stream()
                .filter(viewNodeDesc -> viewNodeDesc.getDomainType().equals(domainType))
                .map(viewNodeDesc -> convertedNodes.get(viewNodeDesc))
                .filter(nodeDesc -> childNodeDescriptions.contains(nodeDesc))
                .map(NodeDescription::getId)
                .findFirst();
    }

    protected void moveElement(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext, IDiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (droppedElement.eContainer() instanceof Membership membership) {
            targetElement.getOwnedRelationship().add(membership);
        } else {
            return;
        }
        this.createView(droppedElement, editingContext, diagramContext, targetNode, convertedNodes, NodeContainmentKind.CHILD_NODE);
        diagramContext.getViewDeletionRequests().add(ViewDeletionRequest.newViewDeletionRequest().elementId(droppedNode.getId()).build());
    }

    protected Node createFakeNode(EObject semanticElement, Object parentNode, IDiagramContext diagramContext, DiagramDescription diagramDescription,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        String targetObjectId = this.objectService.getId(semanticElement);
        String parentElementId = null;
        org.eclipse.sirius.components.view.diagram.NodeDescription childrenType = null;

        if (parentNode == null || parentNode instanceof Diagram) {
            parentElementId = diagramContext.getDiagram().getId();
            childrenType = this.getChildrenNodeDescriptionsOfType(diagramDescription, null, semanticElement.eClass());
        } else if (parentNode instanceof Node pNode) {
            parentElementId = pNode.getId();
            org.eclipse.sirius.components.view.diagram.NodeDescription targetNodeDescription = this.getViewNodeDescription(pNode.getDescriptionId(), diagramDescription, convertedNodes).orElse(null);
            childrenType = this.getChildrenNodeDescriptionsOfType(diagramDescription, targetNodeDescription, semanticElement.eClass());
        }

        NodeDescription nodeDescription = convertedNodes.get(childrenType);
        if (nodeDescription == null) {
            return null;
        }
        var targetObjectKind = this.objectService.getKind(semanticElement);
        var targetObjectLabel = this.objectService.getLabel(semanticElement);
        String nodeId = new NodeIdProvider().getNodeId(parentElementId, nodeDescription.getId().toString(), NodeContainmentKind.CHILD_NODE, targetObjectId);

        var labelStyle = LabelStyle.newLabelStyle()
                .color("")
                .fontSize(14)
                .iconURL(List.of())
                .build();

        var insideLabel = InsideLabel.newLabel("")
                .insideLabelLocation(InsideLabelLocation.TOP_CENTER)
                .isHeader(false)
                .overflowStrategy(LabelOverflowStrategy.NONE)
                .style(labelStyle)
                .text("")
                .build();

        var nodeStyle = RectangularNodeStyle.newRectangularNodeStyle().background("").borderColor("").borderStyle(LineStyle.Solid).build();

        return Node.newNode(nodeId)
                .type("")
                .targetObjectId(targetObjectId)
                .targetObjectKind(targetObjectKind)
                .targetObjectLabel(targetObjectLabel)
                .descriptionId(nodeDescription.getId())
                .borderNode(false)
                .modifiers(Set.of())
                .state(ViewModifier.Normal)
                .collapsingState(CollapsingState.EXPANDED)
                .insideLabel(insideLabel)
                .style(nodeStyle)
                .childrenLayoutStrategy(new FreeFormLayoutStrategy())
                .position(Position.UNDEFINED)
                .size(Size.UNDEFINED)
                .userResizable(true)
                .borderNodes(new ArrayList<>())
                .childNodes(new ArrayList<>())
                .customizedProperties(Set.of())
                .build();
    }

    /**
     * Checks if the given {@link Node} can have a child built upon the given element.
     *
     * @param element
     *            an {@link Element} to add as a child of the given node.
     * @param parentNode
     *            a {@link Node} in which the given {@link Element} is about to be added.
     * @param diagramDescription
     *            the diagram description
     * @param convertedNodes
     *            the map of all existing node descriptions in the DiagramDescription of this Diagram. It corresponds to
     *            a variable accessible from the variable manager.
     * @return <code>true</code> if the given element can be added as a child inside the given node and
     *         <code>false</code> otherwise.
     */
    protected boolean isCompliantAsChild(Element element, Node parentNode, DiagramDescription diagramDescription,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        var optNodeDescription = this.getViewNodeDescription(parentNode.getDescriptionId(), diagramDescription, convertedNodes);
        if (optNodeDescription.isPresent()) {
            var nodeDescription = optNodeDescription.get();
            List<org.eclipse.sirius.components.view.diagram.NodeDescription> descriptions = new ArrayList<>();

            descriptions.addAll(nodeDescription.getChildrenDescriptions());
            descriptions.addAll(nodeDescription.getBorderNodesDescriptions());
            descriptions.addAll(nodeDescription.getReusedBorderNodeDescriptions());
            descriptions.addAll(nodeDescription.getReusedChildNodeDescriptions());

            return descriptions.stream()
                    .distinct()
                    .filter(nd -> this.isCompliant(SysMLMetamodelHelper.toEClass(nd.getDomainType()), element.eClass()))
                    .count() > 0;
        }
        return false;
    }

    private org.eclipse.sirius.components.view.diagram.NodeDescription getChildrenNodeDescriptionsOfType(DiagramDescription diagramDescription,
            org.eclipse.sirius.components.view.diagram.NodeDescription nodeDescription, EClass eClass) {
        final List<org.eclipse.sirius.components.view.diagram.NodeDescription> descriptions = new ArrayList<>();
        final String parentName;
        if (nodeDescription == null) {
            parentName = diagramDescription.getName();
            descriptions.addAll(diagramDescription.getNodeDescriptions());
        } else {
            parentName = nodeDescription.getName();
            descriptions.addAll(nodeDescription.getChildrenDescriptions());
            descriptions.addAll(nodeDescription.getBorderNodesDescriptions());
            descriptions.addAll(nodeDescription.getReusedBorderNodeDescriptions());
            descriptions.addAll(nodeDescription.getReusedChildNodeDescriptions());
        }

        List<org.eclipse.sirius.components.view.diagram.NodeDescription> candidates = descriptions.stream()
                .distinct()
                .filter(c -> this.isCompliant(SysMLMetamodelHelper.toEClass(c.getDomainType()), eClass))
                .sorted(Comparator.comparingInt(n -> -1 * this.computeDistanceToElement(SysMLMetamodelHelper.toEClass(n.getDomainType()))))
                .toList();
        if (candidates.isEmpty()) {
            this.logger.error("No candidate for children of type {} on {}", eClass.getName(), parentName);
            return null;
        } else {
            Optional<org.eclipse.sirius.components.view.diagram.NodeDescription> perfectCandidate = candidates.stream().filter(c -> SysMLMetamodelHelper.toEClass(c.getDomainType()) == eClass)
                    .findFirst();
            org.eclipse.sirius.components.view.diagram.NodeDescription byDefault = null;
            if (perfectCandidate.isPresent()) {
                byDefault = perfectCandidate.get();
            } else {
                byDefault = candidates.get(0);
                if (candidates.size() > 1) {
                    this.logger.info("More than one candidate for children of type {} on {}. By default use the more specific type {}", eClass.getName(), parentName, byDefault.getName());
                }
            }
            return byDefault;
        }
    }

    private int computeDistanceToElement(EClassifier source) {
        return this.computeDistanceToElement(source, 0);
    }

    private int computeDistanceToElement(EClassifier source, int current) {
        if (source == SysmlPackage.eINSTANCE.getElement()) {
            return current;
        } else {
            int distance = Integer.MAX_VALUE;
            if (source instanceof EClass) {
                EClass sourceEClass = (EClass) source;
                for (EClass superType : sourceEClass.getESuperTypes()) {
                    distance = Math.min(distance, this.computeDistanceToElement(superType, current + 1));
                }
            }
            return distance;
        }
    }

    private boolean isCompliant(EClassifier expected, EClass toTest) {
        return toTest == expected || toTest.getEAllSuperTypes().contains(expected);
    }

    private Optional<org.eclipse.sirius.components.view.diagram.NodeDescription> getViewNodeDescription(String descriptionId, DiagramDescription diagramDescription,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        return this.eAllContentStreamWithSelf(diagramDescription)
                .filter(org.eclipse.sirius.components.view.diagram.NodeDescription.class::isInstance)
                .map(org.eclipse.sirius.components.view.diagram.NodeDescription.class::cast)
                .filter(nodeDesc -> {
                    NodeDescription convertedNodeDesc = convertedNodes.get(nodeDesc);
                    return convertedNodeDesc != null && descriptionId.equals(convertedNodeDesc.getId());
                })
                .findFirst();
    }

    private Stream<EObject> eAllContentStreamWithSelf(EObject o) {
        if (o == null) {
            return Stream.empty();
        }
        return Stream.concat(Stream.of(o), StreamSupport.stream(Spliterators.spliteratorUnknownSize(o.eAllContents(), Spliterator.NONNULL), false));
    }
}
