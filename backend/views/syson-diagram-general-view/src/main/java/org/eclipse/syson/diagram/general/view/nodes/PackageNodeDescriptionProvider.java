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
package org.eclipse.syson.diagram.general.view.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.FreeFormLayoutStrategyDescriptionBuilder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.DropNodeTool;
import org.eclipse.sirius.components.view.diagram.NodeContainmentKind;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.diagram.general.view.AQLConstants;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.diagram.general.view.SysMLMetamodelHelper;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Used to create the part definition node description.
 *
 * @author arichard
 */
public class PackageNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    public static final String NAME = "GV Node PackageDefinition";

    public PackageNodeDescriptionProvider(IColorProvider colorProvider) {
        super(colorProvider);
    }

    @Override
    public NodeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getPackage());
        return this.diagramBuilderHelper.newNodeDescription()
                .childrenLayoutStrategy(new FreeFormLayoutStrategyDescriptionBuilder().build())
                .defaultHeightExpression("300")
                .defaultWidthExpression("300")
                .domainType(domainType)
                .labelExpression("aql:self.getContainerLabel()")
                .name(NAME)
                .semanticCandidatesExpression("aql:self.getAllReachable(" + domainType + ")")
                .style(this.createPackageNodeStyle())
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

        if (optPartUsageNodeDescription.isPresent()) {
            NodeDescription packageNodeDescription = optPackageNodeDescription.get();
            diagramDescription.getNodeDescriptions().add(packageNodeDescription);
            packageNodeDescription.getReusedChildNodeDescriptions().add(optAttributeDefinitionNodeDescription.get());
            packageNodeDescription.getReusedChildNodeDescriptions().add(optAttributeUsageNodeDescription.get());
            packageNodeDescription.getReusedChildNodeDescriptions().add(optEnumerationDefinitionNodeDescription.get());
            packageNodeDescription.getReusedChildNodeDescriptions().add(optInterfaceDefinitionNodeDescription.get());
            packageNodeDescription.getReusedChildNodeDescriptions().add(optInterfaceUsageNodeDescription.get());
            packageNodeDescription.getReusedChildNodeDescriptions().add(optItemDefinitionNodeDescription.get());
            packageNodeDescription.getReusedChildNodeDescriptions().add(optItemUsageNodeDescription.get());
            packageNodeDescription.getReusedChildNodeDescriptions().add(optMetadataDefinitionNodeDescription.get());
            packageNodeDescription.getReusedChildNodeDescriptions().add(packageNodeDescription);
            packageNodeDescription.getReusedChildNodeDescriptions().add(optPartDefinitionNodeDescription.get());
            packageNodeDescription.getReusedChildNodeDescriptions().add(optPartUsageNodeDescription.get());
            packageNodeDescription.getReusedChildNodeDescriptions().add(optPortDefinitionNodeDescription.get());
            packageNodeDescription.getReusedChildNodeDescriptions().add(optPortUsageNodeDescription.get());
            packageNodeDescription.setPalette(this.createNodePalette(packageNodeDescription, cache, dependencyTargetNodeDescriptions));
        }
    }

    protected NodeStyleDescription createPackageNodeStyle() {
        return this.diagramBuilderHelper.newRectangularNodeStyleDescription()
                .borderColor(this.colorProvider.getColor(GeneralViewDiagramDescriptionProvider.DEFAULT_BORDER_COLOR))
                .borderRadius(0)
                .color(this.colorProvider.getColor(GeneralViewDiagramDescriptionProvider.DEFAULT_BACKGROUND_COLOR))
                .displayHeaderSeparator(false)
                .labelColor(this.colorProvider.getColor(GeneralViewDiagramDescriptionProvider.DEFAULT_LABEL_COLOR))
                .showIcon(true)
                .withHeader(true)
                .build();
    }

    private NodePalette createNodePalette(NodeDescription nodeDescription, IViewDiagramElementFinder cache, List<NodeDescription> allNodeDescriptions) {
        var changeContext = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self.deleteFromModel()");

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
                .dropNodeTool(this.createDropFromDiagramTool(cache))
                .toolSections(this.createNodeToolSection(cache), this.addElementsToolSection(cache))
                .edgeTools(this.createDependencyEdgeTool(allNodeDescriptions))
                .build();
    }

    private DropNodeTool createDropFromDiagramTool(IViewDiagramElementFinder cache) {
        var acceptedNodeTypes = new ArrayList<NodeDescription>();

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

        acceptedNodeTypes.add(optAttributeDefinitionNodeDescription.get());
        acceptedNodeTypes.add(optAttributeUsageNodeDescription.get());
        acceptedNodeTypes.add(optEnumerationDefinitionNodeDescription.get());
        acceptedNodeTypes.add(optInterfaceDefinitionNodeDescription.get());
        acceptedNodeTypes.add(optInterfaceUsageNodeDescription.get());
        acceptedNodeTypes.add(optItemDefinitionNodeDescription.get());
        acceptedNodeTypes.add(optItemUsageNodeDescription.get());
        acceptedNodeTypes.add(optMetadataDefinitionNodeDescription.get());
        acceptedNodeTypes.add(optPackageNodeDescription.get());
        acceptedNodeTypes.add(optPartDefinitionNodeDescription.get());
        acceptedNodeTypes.add(optPartUsageNodeDescription.get());
        acceptedNodeTypes.add(optPortDefinitionNodeDescription.get());
        acceptedNodeTypes.add(optPortUsageNodeDescription.get());

        var dropElementFromDiagram = this.viewBuilderHelper.newChangeContext()
                .expression("aql:droppedElement.dropElementFromDiagram(droppedNode, targetElement, targetNode, editingContext, diagramContext, convertedNodes)");

        return this.diagramBuilderHelper.newDropNodeTool()
                .name("Drop from Diagram")
                .acceptedNodeTypes(acceptedNodeTypes.toArray(new NodeDescription[acceptedNodeTypes.size()]))
                .body(dropElementFromDiagram.build()).build();
    }

    private NodeToolSection createNodeToolSection(IViewDiagramElementFinder cache) {
        return this.diagramBuilderHelper.newNodeToolSection()
                .name("Create")
                .nodeTools(this.createNodeTool(cache.getNodeDescription(AttributeDefinitionNodeDescriptionProvider.NAME).get(), SysmlPackage.eINSTANCE.getAttributeDefinition()),
                           this.createNodeTool(cache.getNodeDescription(AttributeUsageNodeDescriptionProvider.NAME).get(), SysmlPackage.eINSTANCE.getAttributeUsage()),
                           this.createNodeTool(cache.getNodeDescription(EnumerationDefinitionNodeDescriptionProvider.NAME).get(), SysmlPackage.eINSTANCE.getEnumerationDefinition()),
                           this.createNodeTool(cache.getNodeDescription(InterfaceDefinitionNodeDescriptionProvider.NAME).get(), SysmlPackage.eINSTANCE.getInterfaceDefinition()),
                           this.createNodeTool(cache.getNodeDescription(InterfaceUsageNodeDescriptionProvider.NAME).get(), SysmlPackage.eINSTANCE.getInterfaceUsage()),
                           this.createNodeTool(cache.getNodeDescription(ItemDefinitionNodeDescriptionProvider.NAME).get(), SysmlPackage.eINSTANCE.getItemDefinition()),
                           this.createNodeTool(cache.getNodeDescription(ItemUsageNodeDescriptionProvider.NAME).get(), SysmlPackage.eINSTANCE.getItemUsage()),
                           this.createNodeTool(cache.getNodeDescription(MetadataDefinitionNodeDescriptionProvider.NAME).get(), SysmlPackage.eINSTANCE.getMetadataDefinition()),
                           this.createNodeTool(cache.getNodeDescription(PackageNodeDescriptionProvider.NAME).get(), SysmlPackage.eINSTANCE.getPackage()),
                           this.createNodeTool(cache.getNodeDescription(PartDefinitionNodeDescriptionProvider.NAME).get(), SysmlPackage.eINSTANCE.getPartDefinition()),
                           this.createNodeTool(cache.getNodeDescription(PartUsageNodeDescriptionProvider.NAME).get(), SysmlPackage.eINSTANCE.getPartUsage()),
                           this.createNodeTool(cache.getNodeDescription(PortDefinitionNodeDescriptionProvider.NAME).get(), SysmlPackage.eINSTANCE.getPortDefinition()),
                           this.createNodeTool(cache.getNodeDescription(PortUsageNodeDescriptionProvider.NAME).get(), SysmlPackage.eINSTANCE.getPortUsage()))
                .build();
    }

    private NodeTool createNodeTool(NodeDescription nodeDescription, EClass eClass) {
        var builder = this.diagramBuilderHelper.newNodeTool();

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
                .parentViewExpression("aql:selectedNode")
                .semanticElementExpression("aql:newInstance")
                .variableName("newInstanceView");

        var changeContexMembership = this.viewBuilderHelper.newChangeContext()
                .expression("aql:newOwningMembership")
                .children(createEClassInstance.build(), createView.build());

        var createMembership = this.viewBuilderHelper.newCreateInstance()
                .typeName(SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getOwningMembership()))
                .referenceName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .variableName("newOwningMembership")
                .children(changeContexMembership.build());

        return builder
                .name(eClass.getName())
                .body(createMembership.build())
                .build();
    }

    private NodeToolSection addElementsToolSection(IViewDiagramElementFinder cache) {
        return this.diagramBuilderHelper.newNodeToolSection()
                .name("Add")
                .nodeTools(this.addExistingElementsTool())
                .build();
    }

    private NodeTool addExistingElementsTool() {
        var builder = this.diagramBuilderHelper.newNodeTool();

        var addExistingelements = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self.addExistingElements(editingContext, diagramContext, selectedNode, convertedNodes)");

        return builder
                .name("Add existing elements")
                .body(addExistingelements.build())
                .build();
    }
}
