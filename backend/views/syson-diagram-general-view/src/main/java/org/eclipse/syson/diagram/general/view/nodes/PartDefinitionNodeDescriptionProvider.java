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
package org.eclipse.syson.diagram.general.view.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.diagrams.description.EdgeDescription;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.EdgeTool;
import org.eclipse.sirius.components.view.diagram.NodeContainmentKind;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the part definition node description.
 *
 * @author arichard
 */
public class PartDefinitionNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    public static final String NAME = "GV Node PartDefinition";

    public PartDefinitionNodeDescriptionProvider(IColorProvider colorProvider) {
        super(colorProvider);
    }

    @Override
    public NodeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getPartDefinition());
        return this.diagramBuilderHelper.newNodeDescription()
                .childrenLayoutStrategy(this.diagramBuilderHelper.newListLayoutStrategyDescription().areChildNodesDraggableExpression("false").build())
                .collapsible(true)
                .defaultHeightExpression(ViewConstants.DEFAULT_CONTAINER_NODE_HEIGHT)
                .defaultWidthExpression(ViewConstants.DEFAULT_NODE_WIDTH)
                .domainType(domainType)
                .labelExpression("aql:self.getContainerLabel()")
                .name(NAME)
                .semanticCandidatesExpression("aql:self.getAllReachable(" + domainType + ")")
                .style(this.createDefinitionNodeStyle())
                .userResizable(true)
                .synchronizationPolicy(SynchronizationPolicy.UNSYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var dependencyTargetNodeDescriptions = new ArrayList<NodeDescription>();

        var optAttributeDefinitionNodeDescription = cache.getNodeDescription(AttributeDefinitionNodeDescriptionProvider.NAME);
        var optAttributeUsageNodeDescription = cache.getNodeDescription(AttributeUsageNodeDescriptionProvider.NAME);
        var optEnumerationDefinitionNodeDescription = cache.getNodeDescription(EnumerationDefinitionNodeDescriptionProvider.NAME);
        var optInterfaceDefinitionNodeDescription = cache.getNodeDescription(InterfaceDefinitionNodeDescriptionProvider.NAME);
        var optInterfaceUsageNodeDescription = cache.getNodeDescription(InterfaceUsageNodeDescriptionProvider.NAME);
        var optItemDefinitionNodeDescription = cache.getNodeDescription(ItemDefinitionNodeDescriptionProvider.NAME);
        var optItemUsageNodeDescription = cache.getNodeDescription(ItemUsageNodeDescriptionProvider.NAME);
        var optMetadataDefinitionNodeDescription = cache.getNodeDescription(MetadataDefinitionNodeDescriptionProvider.NAME);
        var optPackageNodeDescription = cache.getNodeDescription(PackageNodeDescriptionProvider.NAME);
        var optPartDefinitionNodeDescription = cache.getNodeDescription(PartDefinitionNodeDescriptionProvider.NAME);
        var optPartUsageNodeDescription = cache.getNodeDescription(PartUsageNodeDescriptionProvider.NAME);
        var optPortDefinitionNodeDescription = cache.getNodeDescription(PortDefinitionNodeDescriptionProvider.NAME);
        var optPortUsageNodeDescription = cache.getNodeDescription(PortUsageNodeDescriptionProvider.NAME);

        var optDefinitionAttributesCompartmentNodeDescription = cache.getNodeDescription(DefinitionAttributesCompartmentNodeDescriptionProvider.NAME);
        var optDefinitionPortsCompartmentNodeDescription = cache.getNodeDescription(DefinitionPortsCompartmentNodeDescriptionProvider.NAME);

        dependencyTargetNodeDescriptions.add(optAttributeDefinitionNodeDescription.get());
        dependencyTargetNodeDescriptions.add(optAttributeUsageNodeDescription.get());
        dependencyTargetNodeDescriptions.add(optEnumerationDefinitionNodeDescription.get());
        dependencyTargetNodeDescriptions.add(optInterfaceDefinitionNodeDescription.get());
        dependencyTargetNodeDescriptions.add(optInterfaceUsageNodeDescription.get());
        dependencyTargetNodeDescriptions.add(optItemDefinitionNodeDescription.get());
        dependencyTargetNodeDescriptions.add(optItemUsageNodeDescription.get());
        dependencyTargetNodeDescriptions.add(optMetadataDefinitionNodeDescription.get());
        dependencyTargetNodeDescriptions.add(optPackageNodeDescription.get());
        dependencyTargetNodeDescriptions.add(optPartDefinitionNodeDescription.get());
        dependencyTargetNodeDescriptions.add(optPartUsageNodeDescription.get());
        dependencyTargetNodeDescriptions.add(optPortDefinitionNodeDescription.get());
        dependencyTargetNodeDescriptions.add(optPortUsageNodeDescription.get());

        NodeDescription nodeDescription = optPartDefinitionNodeDescription.get();
        diagramDescription.getNodeDescriptions().add(nodeDescription);
        nodeDescription.getReusedChildNodeDescriptions().add(optDefinitionAttributesCompartmentNodeDescription.get());
        nodeDescription.getReusedChildNodeDescriptions().add(optDefinitionPortsCompartmentNodeDescription.get());
        nodeDescription.setPalette(this.createNodePalette(nodeDescription, dependencyTargetNodeDescriptions));
    }

    private NodePalette createNodePalette(NodeDescription nodeDescription, List<NodeDescription> allNodeDescriptions) {
        var changeContext = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF + ".deleteFromModel()");

        var deleteTool = this.diagramBuilderHelper.newDeleteTool()
                .name("Delete from Model")
                .body(changeContext.build());

        var callEditService = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF + ".directEdit(newLabel)");

        var editTool = this.diagramBuilderHelper.newLabelEditTool()
                .name("Edit")
                .initialDirectEditLabelExpression(AQLConstants.AQL_SELF + ".getDefaultInitialDirectEditLabel()")
                .body(callEditService.build());

        return this.diagramBuilderHelper.newNodePalette()
                .deleteTool(deleteTool.build())
                .labelEditTool(editTool.build())
                .edgeTools(this.createDependencyEdgeTool(allNodeDescriptions),
                        this.createSubclassificationEdgeTool(allNodeDescriptions.stream().filter(nodeDesc -> NAME.equals(nodeDesc.getName())).toList()),
                        this.createAddAsNestedPartEdgeTool(allNodeDescriptions.stream().filter(nodeDesc -> PartUsageNodeDescriptionProvider.NAME.equals(nodeDesc.getName())).toList()))
                .toolSections(
                        this.createElementsToolSection(allNodeDescriptions.stream().filter(nodeDesc -> PartUsageNodeDescriptionProvider.NAME.equals(nodeDesc.getName())).findFirst().get(),
                                allNodeDescriptions.stream().filter(nodeDesc -> ItemUsageNodeDescriptionProvider.NAME.equals(nodeDesc.getName())).findFirst().get()),
                        this.addElementsToolSection())
                .build();
    }

    private NodeToolSection createElementsToolSection(NodeDescription partUsageNodeDescription, NodeDescription itemUsageNodeDescription) {
        return this.diagramBuilderHelper.newNodeToolSection()
                .name("Create")
                .nodeTools(this.createNestedUsageNodeTool(partUsageNodeDescription, SysmlPackage.eINSTANCE.getPartUsage()),
                        this.createNestedUsageNodeTool(itemUsageNodeDescription, SysmlPackage.eINSTANCE.getItemUsage()))
                .build();
    }

    private NodeTool createNestedUsageNodeTool(NodeDescription nodeDescription, EClass eClass) {
        var setValue = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getElement_DeclaredName().getName())
                .valueExpression(eClass.getName());

        var changeContextNewInstance = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newInstance")
                .children(setValue.build());

        var createEClassInstance = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(eClass))
                .referenceName(SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement().getName())
                .variableName("newInstance")
                .children(changeContextNewInstance.build());

        var createView = this.diagramBuilderHelper.newCreateView()
                .containmentKind(NodeContainmentKind.CHILD_NODE)
                .elementDescription(nodeDescription)
                .parentViewExpression("aql:self.getParentNode(selectedNode, diagramContext)")
                .semanticElementExpression("aql:newInstance")
                .variableName("newInstanceView");

        var changeContexMembership = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newFeatureMembership")
                .children(createEClassInstance.build(), createView.build());

        var createMembership = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getFeatureMembership()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newFeatureMembership")
                .children(changeContexMembership.build());

        return this.diagramBuilderHelper.newNodeTool()
                .name("New nested " + eClass.getName())
                .iconURLsExpression("/icons/full/obj16/" + eClass.getName() + ".svg")
                .body(createMembership.build())
                .build();
    }

    private EdgeTool createAddAsNestedPartEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var callService = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE + ".addAsNestedPart(" + EdgeDescription.SEMANTIC_EDGE_TARGET + ")");

        return builder
                .name("Add Part Usage as nested " + SysmlPackage.eINSTANCE.getPartUsage().getName())
                .iconURLsExpression("/icons/full/obj16/" + SysmlPackage.eINSTANCE.getMembership().getName() + ".svg")
                .body(callService.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(new NodeDescription[targetElementDescriptions.size()]))
                .build();
    }

    private NodeToolSection addElementsToolSection() {
        return this.diagramBuilderHelper.newNodeToolSection()
                .name("Add")
                .nodeTools(this.addExistingNestedPartsTool())
                .build();
    }

    private NodeTool addExistingNestedPartsTool() {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var addExistingelements = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self.addExistingElements(editingContext, diagramContext, selectedNode, convertedNodes)");

        return builder
                .name("Add existing nested elements")
                .iconURLsExpression("/icons/AddExistingElements.svg")
                .body(addExistingelements.build())
                .build();
    }
}
