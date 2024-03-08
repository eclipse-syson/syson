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
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.FreeFormLayoutStrategyDescriptionBuilder;
import org.eclipse.sirius.components.view.builder.generated.NodeToolSectionBuilder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.DropNodeTool;
import org.eclipse.sirius.components.view.diagram.EdgeTool;
import org.eclipse.sirius.components.view.diagram.NodeContainmentKind;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.diagram.general.view.services.GeneralViewEdgeToolSwitch;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysmlcustomnodes.SysMLCustomnodesFactory;
import org.eclipse.syson.sysmlcustomnodes.SysMLPackageNodeStyleDescription;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.DescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the package node description.
 *
 * @author arichard
 */
public class PackageNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    public static final String NAME = "GV Node Package";

    public PackageNodeDescriptionProvider(IColorProvider colorProvider) {
        super(colorProvider);
    }

    @Override
    public NodeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getPackage());
        return this.diagramBuilderHelper.newNodeDescription()
                .collapsible(true)
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
        var optPackageNodeDescription = cache.getNodeDescription(PackageNodeDescriptionProvider.NAME);
        NodeDescription packageNodeDescription = optPackageNodeDescription.get();
        diagramDescription.getNodeDescriptions().add(packageNodeDescription);

        var allTargetNodeDescriptions = new ArrayList<NodeDescription>();

        GeneralViewDiagramDescriptionProvider.DEFINITIONS.forEach(definition -> {
            var optNodeDescription = cache.getNodeDescription(GVDescriptionNameGenerator.getNodeName(definition));
            allTargetNodeDescriptions.add(optNodeDescription.get());
            packageNodeDescription.getReusedChildNodeDescriptions().add(optNodeDescription.get());
        });

        GeneralViewDiagramDescriptionProvider.USAGES.forEach(usage -> {
            var optNodeDescription = cache.getNodeDescription(GVDescriptionNameGenerator.getNodeName(usage));
            allTargetNodeDescriptions.add(optNodeDescription.get());
            packageNodeDescription.getReusedChildNodeDescriptions().add(optNodeDescription.get());
        });

        allTargetNodeDescriptions.add(packageNodeDescription);
        packageNodeDescription.getReusedChildNodeDescriptions().add(packageNodeDescription);
        packageNodeDescription.setPalette(this.createNodePalette(packageNodeDescription, cache, allTargetNodeDescriptions));
    }

    protected NodeStyleDescription createPackageNodeStyle() {
        SysMLPackageNodeStyleDescription nodeStyleDescription = SysMLCustomnodesFactory.eINSTANCE.createSysMLPackageNodeStyleDescription();
        nodeStyleDescription.setBorderColor(this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR));
        nodeStyleDescription.setBorderRadius(0);
        nodeStyleDescription.setColor(this.colorProvider.getColor(ViewConstants.DEFAULT_BACKGROUND_COLOR));
        nodeStyleDescription.setLabelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR));
        nodeStyleDescription.setShowIcon(true);
        return nodeStyleDescription;
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

        var edgeTools = new ArrayList<EdgeTool>();
        edgeTools.addAll(this.getEdgeTools(nodeDescription, allNodeDescriptions));

        return this.diagramBuilderHelper.newNodePalette()
                .deleteTool(deleteTool.build())
                .labelEditTool(editTool.build())
                .dropNodeTool(this.createDropFromDiagramTool(cache))
                .toolSections(this.createToolSections(cache))
                .edgeTools(edgeTools.toArray(EdgeTool[]::new))
                .build();
    }

    private List<EdgeTool> getEdgeTools(NodeDescription nodeDescription, List<NodeDescription> allNodeDescriptions) {
        GeneralViewEdgeToolSwitch edgeToolSwitch = new GeneralViewEdgeToolSwitch(nodeDescription, allNodeDescriptions);
        edgeToolSwitch.doSwitch(SysmlPackage.eINSTANCE.getPackage());
        return edgeToolSwitch.getEdgeTools();
    }

    private DropNodeTool createDropFromDiagramTool(IViewDiagramElementFinder cache) {
        var acceptedNodeTypes = new ArrayList<NodeDescription>();

        GeneralViewDiagramDescriptionProvider.DEFINITIONS.forEach(definition -> {
            var optNodeDescription = cache.getNodeDescription(GVDescriptionNameGenerator.getNodeName(definition));
            acceptedNodeTypes.add(optNodeDescription.get());
        });

        GeneralViewDiagramDescriptionProvider.USAGES.forEach(usage -> {
            var optNodeDescription = cache.getNodeDescription(GVDescriptionNameGenerator.getNodeName(usage));
            acceptedNodeTypes.add(optNodeDescription.get());
        });

        var optPackageNodeDescription = cache.getNodeDescription(PackageNodeDescriptionProvider.NAME);
        acceptedNodeTypes.add(optPackageNodeDescription.get());

        var dropElementFromDiagram = this.viewBuilderHelper.newChangeContext()
                .expression("aql:droppedElement.dropElementFromDiagram(droppedNode, targetElement, targetNode, editingContext, diagramContext, convertedNodes)");

        return this.diagramBuilderHelper.newDropNodeTool()
                .name("Drop from Diagram")
                .acceptedNodeTypes(acceptedNodeTypes.toArray(NodeDescription[]::new))
                .body(dropElementFromDiagram.build())
                .build();
    }

    private NodeTool createNodeTool(NodeDescription nodeDescription, EClass eClass) {
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

        return this.diagramBuilderHelper.newNodeTool()
                .name(DescriptionNameGenerator.getCreationToolName(eClass))
                .iconURLsExpression("/icons/full/obj16/" + eClass.getName() + ".svg")
                .body(createMembership.build())
                .build();
    }

    private NodeToolSection addElementsToolSection() {
        return this.diagramBuilderHelper.newNodeToolSection()
                .name("Add")
                .nodeTools(this.addExistingElementsTool())
                .build();
    }

    private NodeTool addExistingElementsTool() {
        var addExistingelements = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self.addExistingElements(editingContext, diagramContext, selectedNode, convertedNodes)");

        return this.diagramBuilderHelper.newNodeTool()
                .name("Add existing elements")
                .iconURLsExpression("/icons/AddExistingElements.svg")
                .body(addExistingelements.build())
                .build();
    }
    
    private NodeToolSection[] createToolSections(IViewDiagramElementFinder cache) {
        var sections = new ArrayList<NodeToolSection>();
        
        GeneralViewDiagramDescriptionProvider.TOOL_SECTIONS.forEach((sectionName, elements) -> {
            NodeToolSectionBuilder sectionBuilder = this.diagramBuilderHelper.newNodeToolSection()
                    .name(sectionName)
                    .nodeTools(this.createElementsOfToolSection(cache, elements));
            sections.add(sectionBuilder.build());
        });
        
        sections.add(this.addElementsToolSection());
        
        return sections.toArray(NodeToolSection[]::new);
    }
    
    private NodeTool[] createElementsOfToolSection(IViewDiagramElementFinder cache, List<EClass> elements) {
        var nodeTools = new ArrayList<NodeTool>();

        elements.forEach(definition -> {
            nodeTools.add(this.createNodeTool(cache.getNodeDescription(GVDescriptionNameGenerator.getNodeName(definition)).get(), definition));
        });

        nodeTools.sort((nt1, nt2) -> nt1.getName().compareTo(nt2.getName()));

        return nodeTools.toArray(NodeTool[]::new);
    }
}
