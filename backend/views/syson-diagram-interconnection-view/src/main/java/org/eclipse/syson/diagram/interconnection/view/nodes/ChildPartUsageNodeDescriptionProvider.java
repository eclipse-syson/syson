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
package org.eclipse.syson.diagram.interconnection.view.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.DropNodeTool;
import org.eclipse.sirius.components.view.diagram.InsideLabelDescription;
import org.eclipse.sirius.components.view.diagram.InsideLabelPosition;
import org.eclipse.sirius.components.view.diagram.InsideLabelStyle;
import org.eclipse.sirius.components.view.diagram.NodeContainmentKind;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.diagram.common.view.nodes.AbstractNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.tools.CompartmentNodeToolProvider;
import org.eclipse.syson.diagram.interconnection.view.tools.ChildrenPartUsageCompartmentNodeToolProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.DescriptionNameGenerator;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the part definition node description.
 *
 * @author arichard
 */
public class ChildPartUsageNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    public static final String NAME = "IV Node ChildPartUsage";

    private final IDescriptionNameGenerator nameGenerator;

    public ChildPartUsageNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator nameGenerator) {
        super(colorProvider);
        this.nameGenerator = Objects.requireNonNull(nameGenerator);
    }

    @Override
    public NodeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getPartUsage());
        return this.diagramBuilderHelper.newNodeDescription()
                .childrenLayoutStrategy(this.diagramBuilderHelper.newListLayoutStrategyDescription().areChildNodesDraggableExpression("false").build())
                .defaultHeightExpression("100")
                .defaultWidthExpression("150")
                .domainType(domainType)
                .insideLabel(this.createInsideLabelDescription())
                .name(NAME)
                .semanticCandidatesExpression(AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getUsage_NestedPart().getName())
                .style(this.createChildPartUsageNodeStyle())
                .userResizable(true)
                .synchronizationPolicy(SynchronizationPolicy.UNSYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optChildPartUsageNodeDescription = cache.getNodeDescription(ChildPartUsageNodeDescriptionProvider.NAME);
        var optPortUsageBorderNodeDescription = cache.getNodeDescription(PortUsageBorderNodeDescriptionProvider.NAME);
        var optAttributesCompartmentNodeDescription = cache.getNodeDescription(this.nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute()));
        var optCompartmentFreeFormNodeDescription = cache.getNodeDescription(ChildrenPartUsageCompartmentNodeDescriptionProvider.NAME);

        NodeDescription nodeDescription = optChildPartUsageNodeDescription.get();
        nodeDescription.getReusedChildNodeDescriptions().add(optAttributesCompartmentNodeDescription.get());
        nodeDescription.getReusedChildNodeDescriptions().add(optCompartmentFreeFormNodeDescription.get());
        nodeDescription.getReusedBorderNodeDescriptions().add(optPortUsageBorderNodeDescription.get());
        nodeDescription.setPalette(this.createNodePalette(cache));
    }

    private InsideLabelDescription createInsideLabelDescription() {
        return this.diagramBuilderHelper.newInsideLabelDescription()
                .labelExpression(AQLConstants.AQL_SELF + ".getContainerLabel()")
                .position(InsideLabelPosition.TOP_CENTER)
                .style(this.createInsideLabelStyle())
                .build();
    }

    private InsideLabelStyle createInsideLabelStyle() {
        return this.diagramBuilderHelper.newInsideLabelStyle()
                .displayHeaderSeparator(true)
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIcon(true)
                .withHeader(true)
                .build();
    }

    private NodeStyleDescription createChildPartUsageNodeStyle() {
        return this.diagramBuilderHelper.newRectangularNodeStyleDescription()
                .borderColor(this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR))
                .borderRadius(10)
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_BACKGROUND_COLOR))
                .build();
    }

    private NodePalette createNodePalette(IViewDiagramElementFinder cache) {
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
                .dropNodeTool(this.createDropFromDiagramTool(cache))
                .labelEditTool(editTool.build())
                .toolSections(this.createNodeToolSection(cache), this.addElementsToolSection())
                .build();
    }

    private NodeToolSection createNodeToolSection(IViewDiagramElementFinder cache) {
        List<NodeTool> nodeTools = new ArrayList<>();
        CompartmentNodeToolProvider attributeTool = new CompartmentNodeToolProvider(SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), this.nameGenerator);
        nodeTools.add(attributeTool.create(null));
        ChildrenPartUsageCompartmentNodeToolProvider childPartTool = new ChildrenPartUsageCompartmentNodeToolProvider();
        nodeTools.add(childPartTool.create(null));
        NodeTool portBorderNodeTool = this.createNodeTool(cache.getNodeDescription(PortUsageBorderNodeDescriptionProvider.NAME).get(), SysmlPackage.eINSTANCE.getPortUsage(), NodeContainmentKind.BORDER_NODE);
        nodeTools.add(portBorderNodeTool);

        return this.diagramBuilderHelper.newNodeToolSection()
                .name("Create")
                .nodeTools(nodeTools.toArray(NodeTool[]::new))
                .build();
    }

    private NodeTool createNodeTool(NodeDescription nodeDescription, EClass eClass, NodeContainmentKind nodeKind) {
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
                .containmentKind(nodeKind)
                .elementDescription(nodeDescription)
                .parentViewExpression("aql:selectedNode")
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

        return builder
                .name(new DescriptionNameGenerator("").getCreationToolName(eClass))
                .iconURLsExpression("/icons/full/obj16/" + eClass.getName() + ".svg")
                .body(createMembership.build())
                .build();
    }

    private DropNodeTool createDropFromDiagramTool(IViewDiagramElementFinder cache) {
        var acceptedNodeTypes = new ArrayList<NodeDescription>();

        var optPortUsageBorderNodeDescription = cache.getNodeDescription(PortUsageBorderNodeDescriptionProvider.NAME);
        var optChildPartUsageNodeDescription = cache.getNodeDescription(ChildPartUsageNodeDescriptionProvider.NAME);

        acceptedNodeTypes.add(optPortUsageBorderNodeDescription.get());
        acceptedNodeTypes.add(optChildPartUsageNodeDescription.get());

        var dropElementFromDiagram = this.viewBuilderHelper.newChangeContext()
                .expression("aql:droppedElement.dropElementFromDiagram(droppedNode, targetElement, targetNode, editingContext, diagramContext, convertedNodes)");

        return this.diagramBuilderHelper.newDropNodeTool()
                .name("Drop from Diagram")
                .acceptedNodeTypes(acceptedNodeTypes.toArray(NodeDescription[]::new))
                .body(dropElementFromDiagram.build())
                .build();
    }
}
