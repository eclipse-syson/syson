/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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
import java.util.stream.Stream;

import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.ViewCreationRequest;
import org.eclipse.sirius.components.diagrams.ViewDeletionRequest;
import org.eclipse.sirius.components.diagrams.components.NodeContainmentKind;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Tool-related Java services used by SysON representations.
 *
 * @author arichard
 */
public class ToolService {

    private final IObjectService objectService;

    private final IRepresentationDescriptionSearchService representationDescriptionSearchService;

    public ToolService(IObjectService objectService, IRepresentationDescriptionSearchService representationDescriptionSearchService) {
        this.objectService = Objects.requireNonNull(objectService);
        this.representationDescriptionSearchService = Objects.requireNonNull(representationDescriptionSearchService);
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
    public Element dropElementFromDiagram(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext, IDiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        this.moveElement(droppedElement, droppedNode, targetElement, targetNode, editingContext, diagramContext, convertedNodes);
        return droppedElement;
    }

    protected List<Node> getChildNodes(IDiagramContext diagramContext, Node selectedNode) {
        var childNodes = new ArrayList<Node>();
        if (selectedNode != null) {
            childNodes.addAll(selectedNode.getChildNodes());
        } else {
            var diagram = diagramContext.getDiagram();
            childNodes.addAll(diagram.getNodes());
        }
        return childNodes;
    }

    protected boolean isPresent(Element element, List<Node> nodes) {
        return nodes.stream().anyMatch(node -> node.getTargetObjectId().equals(this.objectService.getId(element)));
    }

    protected void createView(Element element, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        var parentElementId = this.getParentElementId(diagramContext, selectedNode);
        var descriptionId = this.getDescriptionId(element, editingContext, diagramContext, selectedNode, convertedNodes);

        if (descriptionId.isPresent()) {
            var request = ViewCreationRequest.newViewCreationRequest().containmentKind(NodeContainmentKind.CHILD_NODE).descriptionId(descriptionId.get()).parentElementId(parentElementId)
                    .targetObjectId(this.objectService.getId(element)).build();
            diagramContext.getViewCreationRequests().add(request);
        }
    }

    protected String getParentElementId(IDiagramContext diagramContext, Node selectedNode) {
        if (selectedNode != null) {
            return selectedNode.getId();
        }
        return diagramContext.getDiagram().getId();
    }

    protected Optional<String> getDescriptionId(Element element, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        // The NodeDescription must be a child of the Parent Node/Diagram
        var generalViewDescription = this.representationDescriptionSearchService.findById(editingContext, diagramContext.getDiagram().getDescriptionId());

        final var childNodeDescriptions = new ArrayList<>();

        if (selectedNode == null) {
            childNodeDescriptions.addAll(generalViewDescription.filter(org.eclipse.sirius.components.diagrams.description.DiagramDescription.class::isInstance)
                    .map(org.eclipse.sirius.components.diagrams.description.DiagramDescription.class::cast)
                    .map(org.eclipse.sirius.components.diagrams.description.DiagramDescription::getNodeDescriptions).orElse(List.of()));
        } else {
            childNodeDescriptions.addAll(convertedNodes.values().stream().filter(nodeDesc -> nodeDesc.getId().equals(selectedNode.getDescriptionId())).flatMap(nodeDesc -> Stream
                    .concat(nodeDesc.getChildNodeDescriptions().stream(), convertedNodes.values().stream().filter(convNode -> nodeDesc.getReusedChildNodeDescriptionIds().contains(convNode.getId()))))
                    .toList());
        }

        var domainType = SysMLMetamodelHelper.buildQualifiedName(element.eClass());

        return convertedNodes.keySet().stream().filter(viewNodeDesc -> viewNodeDesc.getDomainType().equals(domainType)).map(viewNodeDesc -> convertedNodes.get(viewNodeDesc))
                .filter(nodeDesc -> childNodeDescriptions.contains(nodeDesc)).map(NodeDescription::getId).findFirst();
    }

    protected void moveElement(Element droppedElement, Node droppedNode, Element targetElement, Node targetNode, IEditingContext editingContext, IDiagramContext diagramContext,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        if (droppedElement.eContainer() instanceof Membership membership) {
            targetElement.getOwnedRelationship().add(membership);
        } else {
            return;
        }
        this.createView(droppedElement, editingContext, diagramContext, targetNode, convertedNodes);
        diagramContext.getViewDeletionRequests().add(ViewDeletionRequest.newViewDeletionRequest().elementId(droppedNode.getId()).build());
    }
}
