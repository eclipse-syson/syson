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
package org.eclipse.syson.diagram.interconnection.view.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.diagrams.CollapsingState;
import org.eclipse.sirius.components.diagrams.FreeFormLayoutStrategy;
import org.eclipse.sirius.components.diagrams.InsideLabel;
import org.eclipse.sirius.components.diagrams.InsideLabelLocation;
import org.eclipse.sirius.components.diagrams.LabelOverflowStrategy;
import org.eclipse.sirius.components.diagrams.LabelStyle;
import org.eclipse.sirius.components.diagrams.LineStyle;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.Position;
import org.eclipse.sirius.components.diagrams.RectangularNodeStyle;
import org.eclipse.sirius.components.diagrams.Size;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.diagrams.components.NodeContainmentKind;
import org.eclipse.sirius.components.diagrams.components.NodeIdProvider;
import org.eclipse.sirius.components.diagrams.description.NodeDescription;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.emf.IViewRepresentationDescriptionSearchService;
import org.eclipse.syson.diagram.common.view.services.ViewToolService;
import org.eclipse.syson.diagram.interconnection.view.IVDescriptionNameGenerator;
import org.eclipse.syson.diagram.interconnection.view.InterconnectionViewForUsageDiagramDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.nodes.ChildPartUsageNodeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.nodes.ChildrenPartUsageCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.nodes.FirstLevelChildPartUsageNodeDescriptionProvider;
import org.eclipse.syson.services.ElementInitializerSwitch;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Tool-related Java services used by the {@link InterconnectionViewForUsageDiagramDescriptionProvider}.
 *
 * @author arichard
 */
public class InterconnectionViewToolService extends ViewToolService {

    private final ElementInitializerSwitch elementInitializerSwitch;

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public InterconnectionViewToolService(IObjectService objectService, IRepresentationDescriptionSearchService representationDescriptionSearchService,
            IViewRepresentationDescriptionSearchService viewRepresentationDescriptionSearchService) {
        super(objectService, representationDescriptionSearchService, viewRepresentationDescriptionSearchService);
        this.elementInitializerSwitch = new ElementInitializerSwitch();
        this.descriptionNameGenerator = new IVDescriptionNameGenerator();
    }

    /**
     * Called by "New Part" tool from Interconnection View Child PartUsage node.
     *
     * @param partUsage
     *            the {@link PartUsage} corresponding to the target object on which the tool has been called.
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
     * @return the created PartUsage
     */
    public PartUsage createChildPart(PartUsage partUsage, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        FeatureMembership membership = SysmlFactory.eINSTANCE.createFeatureMembership();
        partUsage.getOwnedRelationship().add(membership);
        PartUsage childPart = SysmlFactory.eINSTANCE.createPartUsage();
        membership.getOwnedRelatedElement().add(childPart);
        this.elementInitializerSwitch.doSwitch(childPart);
        // get the children part usage compartment
        Node parentNode = null;
        Optional<org.eclipse.sirius.components.view.diagram.NodeDescription> nodeChildPartUsage = convertedNodes.keySet().stream()
                .filter(n -> FirstLevelChildPartUsageNodeDescriptionProvider.NAME.equals(n.getName())).findFirst();
        if (nodeChildPartUsage.isPresent()) {
            NodeDescription nodeDescription = convertedNodes.get(nodeChildPartUsage.get());
            if (nodeDescription.getId().equals(selectedNode.getDescriptionId())) {
                parentNode = selectedNode.getChildNodes().get(1);
            }
        }
        if (parentNode == null) {
            nodeChildPartUsage = convertedNodes.keySet().stream()
                    .filter(n -> ChildPartUsageNodeDescriptionProvider.NAME.equals(n.getName())).findFirst();
            if (nodeChildPartUsage.isPresent()) {
                NodeDescription nodeDescription = convertedNodes.get(nodeChildPartUsage.get());
                if (nodeDescription.getId().equals(selectedNode.getDescriptionId())) {
                    parentNode = selectedNode.getChildNodes().get(1);
                }
            }
        }
        if (parentNode == null) {
            parentNode = selectedNode;
        }
        if (parentNode != null) {
            this.createView(childPart, editingContext, diagramContext, parentNode, convertedNodes);
        }
        return childPart;
    }

    /**
     * Called by "Add existing elements" tool from Interconnection View Usage node. Add nodes that are not present in
     * selectedNode (i.e. a PartUsage).
     *
     * @param usage
     *            the {@link Usage} corresponding to the target object of the Diagram or the {@link Node} Package on
     *            which the tool has been called.
     * @param editingContext
     *            the {@link IEditingContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param diagramContext
     *            the {@link IDiagramContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param selectedNode
     *            the selected node on which the tool has been called (may be null if the tool has been called from the
     *            diagram). It corresponds to a variable accessible from the variable manager.
     * @param convertedNodes
     *            the map of all existing node descriptions in the DiagramDescription of this Diagram. It corresponds to
     *            a variable accessible from the variable manager.
     * @param recursive
     *            whether the tool should add existing elements recursively or not.
     * @return the input {@link Usage}.
     */
    public Usage addExistingElements(Usage usage, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes, boolean recursive) {
        var nestedParts = usage.getNestedPart();
        var diagramDescription = this.viewRepresentationDescriptionSearchService.findById(editingContext, diagramContext.getDiagram().getDescriptionId());
        DiagramDescription representationDescription = (DiagramDescription) diagramDescription.get();

        Node childrenPartUsageCompartmentNode = null;
        Optional<org.eclipse.sirius.components.view.diagram.NodeDescription> nodeChildPartUsage = convertedNodes.keySet().stream()
                .filter(n -> FirstLevelChildPartUsageNodeDescriptionProvider.NAME.equals(n.getName())).findFirst();
        if (nodeChildPartUsage.isPresent()) {
            NodeDescription nodeDescription = convertedNodes.get(nodeChildPartUsage.get());
            if (nodeDescription.getId().equals(selectedNode.getDescriptionId())) {
                childrenPartUsageCompartmentNode = selectedNode.getChildNodes().get(1);
            }
        }
        if (childrenPartUsageCompartmentNode == null) {
            nodeChildPartUsage = convertedNodes.keySet().stream()
                    .filter(n -> ChildPartUsageNodeDescriptionProvider.NAME.equals(n.getName())).findFirst();
            if (nodeChildPartUsage.isPresent()) {
                NodeDescription nodeDescription = convertedNodes.get(nodeChildPartUsage.get());
                if (nodeDescription.getId().equals(selectedNode.getDescriptionId())) {
                    childrenPartUsageCompartmentNode = selectedNode.getChildNodes().get(1);
                }
            }
        }
        if (childrenPartUsageCompartmentNode == null) {
            childrenPartUsageCompartmentNode = selectedNode;
        }
        var parentNode = childrenPartUsageCompartmentNode;
        nestedParts.stream().filter(subPartUsage -> !this.isPresent(subPartUsage, this.getChildNodes(diagramContext, parentNode))).forEach(subPartUsage -> {
            this.createView(subPartUsage, editingContext, diagramContext, parentNode, convertedNodes);
            if (recursive) {
                Node fakeNode = this.createFakeNode(subPartUsage, parentNode, diagramContext, representationDescription, convertedNodes);
                this.addExistingSubElements(subPartUsage, editingContext, diagramContext, fakeNode, representationDescription, convertedNodes);
            }
        });
        return usage;
    }

    /**
     * Called by "Add existing elements" tool from Interconnection View Definition node. Add nodes that are not present
     * in selectedNode (i.e. a PartDefinition).
     *
     * @param definition
     *            the {@link Definition} corresponding to the target object of the Diagram or the {@link Node} Package
     *            on which the tool has been called.
     * @param editingContext
     *            the {@link IEditingContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param diagramContext
     *            the {@link IDiagramContext} of the tool. It corresponds to a variable accessible from the variable
     *            manager.
     * @param selectedNode
     *            the selected node on which the tool has been called (may be null if the tool has been called from the
     *            diagram). It corresponds to a variable accessible from the variable manager.
     * @param convertedNodes
     *            the map of all existing node descriptions in the DiagramDescription of this Diagram. It corresponds to
     *            a variable accessible from the variable manager.
     * @param recursive
     *            whether the tool should add existing elements recursively or not.
     * @return the input {@link Definition}.
     */
    public Definition addExistingElements(Definition definition, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes, boolean recursive) {
        var ownedParts = definition.getOwnedPart();
        var diagramDescription = this.viewRepresentationDescriptionSearchService.findById(editingContext, diagramContext.getDiagram().getDescriptionId());
        DiagramDescription representationDescription = (DiagramDescription) diagramDescription.get();
        var parentNode = selectedNode;
        ownedParts.stream().filter(subPartUsage -> !this.isPresent(subPartUsage, this.getChildNodes(diagramContext, parentNode))).forEach(subPartUsage -> {
            this.createView(subPartUsage, editingContext, diagramContext, parentNode, convertedNodes);
            if (recursive) {
                Node fakeNode = this.createFakeNode(subPartUsage, parentNode, diagramContext, representationDescription, convertedNodes);
                this.addExistingSubElements(subPartUsage, editingContext, diagramContext, fakeNode, representationDescription, convertedNodes);
            }
        });
        return definition;
    }

    @Override
    protected Node createFakeNode(EObject semanticElement, Object parentNode, IDiagramContext diagramContext, DiagramDescription diagramDescription,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        Node fakeNode = super.createFakeNode(semanticElement, parentNode, diagramContext, diagramDescription, convertedNodes);
        // create the compartments (attributes and children part usage)
        Optional<org.eclipse.sirius.components.view.diagram.NodeDescription> attributesCompartment = convertedNodes.keySet().stream()
                .filter(n -> this.descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute()).equals(n.getName()))
                .findFirst();
        NodeDescription attributesCompartmentNodeDescription = convertedNodes.get(attributesCompartment.get());
        Node fakeAttributesCompartmentNode = this.createFakeCompartmentNode(semanticElement, fakeNode, attributesCompartmentNodeDescription);
        fakeNode.getChildNodes().add(fakeAttributesCompartmentNode);
        Optional<org.eclipse.sirius.components.view.diagram.NodeDescription> childrenPartUsagesCompartment = convertedNodes.keySet().stream()
                .filter(n -> ChildrenPartUsageCompartmentNodeDescriptionProvider.NAME.equals(n.getName())).findFirst();
        NodeDescription childrenPartUsagesCompartmentNodeDescription = convertedNodes.get(childrenPartUsagesCompartment.get());
        Node fakeChildrenPartUsageCompartmentNode = this.createFakeCompartmentNode(semanticElement, fakeNode, childrenPartUsagesCompartmentNodeDescription);
        fakeNode.getChildNodes().add(fakeChildrenPartUsageCompartmentNode);
        return fakeNode;
    }

    private PartUsage addExistingSubElements(PartUsage partUsage, IEditingContext editingContext, IDiagramContext diagramContext, Node selectedNode, DiagramDescription diagramDescription,
            Map<org.eclipse.sirius.components.view.diagram.NodeDescription, NodeDescription> convertedNodes) {
        var nestedParts = partUsage.getNestedPart();
        Node childrenPartUsageCompartmentNode = null;
        Optional<org.eclipse.sirius.components.view.diagram.NodeDescription> nodeChildPartUsage = convertedNodes.keySet().stream()
                .filter(n -> FirstLevelChildPartUsageNodeDescriptionProvider.NAME.equals(n.getName())).findFirst();
        if (nodeChildPartUsage.isPresent()) {
            NodeDescription nodeDescription = convertedNodes.get(nodeChildPartUsage.get());
            if (nodeDescription.getId().equals(selectedNode.getDescriptionId())) {
                childrenPartUsageCompartmentNode = selectedNode.getChildNodes().get(1);
            }
        }
        if (childrenPartUsageCompartmentNode == null) {
            nodeChildPartUsage = convertedNodes.keySet().stream()
                    .filter(n -> ChildPartUsageNodeDescriptionProvider.NAME.equals(n.getName())).findFirst();
            if (nodeChildPartUsage.isPresent()) {
                NodeDescription nodeDescription = convertedNodes.get(nodeChildPartUsage.get());
                if (nodeDescription.getId().equals(selectedNode.getDescriptionId())) {
                    childrenPartUsageCompartmentNode = selectedNode.getChildNodes().get(1);
                }
            }
        }
        if (childrenPartUsageCompartmentNode == null) {
            childrenPartUsageCompartmentNode = selectedNode;
        }
        var parentNode = childrenPartUsageCompartmentNode;
        nestedParts.stream().forEach(subPartUsage -> {
            this.createView(subPartUsage, editingContext, diagramContext, parentNode, convertedNodes);
            Node fakeNode = this.createFakeNode(subPartUsage, parentNode, diagramContext, diagramDescription, convertedNodes);
            this.addExistingSubElements(subPartUsage, editingContext, diagramContext, fakeNode, diagramDescription, convertedNodes);
        });
        return partUsage;
    }

    protected Node createFakeCompartmentNode(EObject semanticElement, Node parentNode, NodeDescription nodeDescription) {
        String targetObjectId = this.objectService.getId(semanticElement);
        String parentElementId = parentNode.getId();

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

        var nodeStyle = RectangularNodeStyle.newRectangularNodeStyle().color("").borderColor("").borderStyle(LineStyle.Solid).build();

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
}
