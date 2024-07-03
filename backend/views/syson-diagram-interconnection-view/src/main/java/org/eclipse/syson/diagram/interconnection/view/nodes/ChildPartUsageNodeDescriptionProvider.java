/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.DropNodeTool;
import org.eclipse.sirius.components.view.diagram.EdgeTool;
import org.eclipse.sirius.components.view.diagram.InsideLabelDescription;
import org.eclipse.sirius.components.view.diagram.InsideLabelPosition;
import org.eclipse.sirius.components.view.diagram.InsideLabelStyle;
import org.eclipse.sirius.components.view.diagram.LabelTextAlign;
import org.eclipse.sirius.components.view.diagram.ListLayoutStrategyDescription;
import org.eclipse.sirius.components.view.diagram.NodeContainmentKind;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.sirius.components.view.diagram.UserResizableDirection;
import org.eclipse.syson.diagram.common.view.nodes.AbstractNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.services.description.ToolDescriptionService;
import org.eclipse.syson.diagram.common.view.tools.CompartmentNodeToolProvider;
import org.eclipse.syson.diagram.interconnection.view.tools.ChildrenPartUsageCompartmentNodeToolProvider;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the child part usage node description.
 *
 * @author arichard
 */
public class ChildPartUsageNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    public static final String NAME = "IV Node ChildPartUsage";

    private final ToolDescriptionService toolDescriptionService;

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public ChildPartUsageNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
        this.toolDescriptionService = new ToolDescriptionService(descriptionNameGenerator);
    }

    @Override
    public NodeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getPartUsage());
        return this.diagramBuilderHelper.newNodeDescription()
                .childrenLayoutStrategy(this.diagramBuilderHelper.newListLayoutStrategyDescription().areChildNodesDraggableExpression("false").build())
                .collapsible(true)
                .defaultHeightExpression(ViewConstants.DEFAULT_CONTAINER_NODE_HEIGHT)
                .defaultWidthExpression("150")
                .domainType(domainType)
                .insideLabel(this.createInsideLabelDescription())
                .name(NAME)
                .semanticCandidatesExpression(AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getUsage_NestedPart().getName())
                .style(this.createChildPartUsageNodeStyle())
                .userResizable(UserResizableDirection.BOTH)
                .synchronizationPolicy(SynchronizationPolicy.UNSYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optChildPartUsageNodeDescription = cache.getNodeDescription(ChildPartUsageNodeDescriptionProvider.NAME);
        var optPortUsageBorderNodeDescription = cache.getNodeDescription(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage()));
        var optDocCompartmentNodeDescription = cache
                .getNodeDescription(this.descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getDocumentation(), SysmlPackage.eINSTANCE.getElement_Documentation()));
        var optAttributesCompartmentNodeDescription = cache
                .getNodeDescription(this.descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute()));
        var optCompartmentFreeFormNodeDescription = cache
                .getNodeDescription(this.descriptionNameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPart()));

        NodeDescription nodeDescription = optChildPartUsageNodeDescription.get();
        nodeDescription.getReusedChildNodeDescriptions().add(optDocCompartmentNodeDescription.get());
        nodeDescription.getReusedChildNodeDescriptions().add(optAttributesCompartmentNodeDescription.get());
        nodeDescription.getReusedChildNodeDescriptions().add(optCompartmentFreeFormNodeDescription.get());
        nodeDescription.getReusedBorderNodeDescriptions().add(optPortUsageBorderNodeDescription.get());
        nodeDescription.setPalette(this.createNodePalette(cache));

        List<NodeDescription> growableNodes = List.of(optCompartmentFreeFormNodeDescription.get());
        ListLayoutStrategyDescription layoutStrategy = this.diagramBuilderHelper.newListLayoutStrategyDescription()
                .areChildNodesDraggableExpression("false")
                .growableNodes(growableNodes.toArray(NodeDescription[]::new))
                .build();
        nodeDescription.setChildrenLayoutStrategy(layoutStrategy);
    }

    private InsideLabelDescription createInsideLabelDescription() {
        return this.diagramBuilderHelper.newInsideLabelDescription()
                .labelExpression(AQLUtils.getSelfServiceCallExpression("getContainerLabel"))
                .position(InsideLabelPosition.TOP_CENTER)
                .style(this.createInsideLabelStyle())
                .textAlign(LabelTextAlign.CENTER)
                .build();
    }

    private InsideLabelStyle createInsideLabelStyle() {
        return this.diagramBuilderHelper.newInsideLabelStyle()
                .borderSize(0)
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
                .background(this.colorProvider.getColor(ViewConstants.DEFAULT_BACKGROUND_COLOR))
                .build();
    }

    private NodePalette createNodePalette(IViewDiagramElementFinder cache) {
        var changeContext = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("deleteFromModel"));

        var deleteTool = this.diagramBuilderHelper.newDeleteTool()
                .name("Delete from Model")
                .body(changeContext.build());

        var callEditService = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("directEdit", "newLabel"));

        var editTool = this.diagramBuilderHelper.newLabelEditTool()
                .name("Edit")
                .initialDirectEditLabelExpression(AQLUtils.getSelfServiceCallExpression("getDefaultInitialDirectEditLabel"))
                .body(callEditService.build());

        return this.diagramBuilderHelper.newNodePalette()
                .deleteTool(deleteTool.build())
                .dropNodeTool(this.createDropFromDiagramTool(cache))
                .labelEditTool(editTool.build())
                .toolSections(this.createNodeToolSection(cache),
                        this.toolDescriptionService.addElementsNodeToolSection(true),
                        this.defaultToolsFactory.createDefaultHideRevealNodeToolSection())
                .build();
    }

    private NodeToolSection createNodeToolSection(IViewDiagramElementFinder cache) {
        List<NodeTool> nodeTools = new ArrayList<>();
        CompartmentNodeToolProvider attributeTool = new CompartmentNodeToolProvider(SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), this.descriptionNameGenerator);
        nodeTools.add(attributeTool.create(null));
        CompartmentNodeToolProvider docTool = new CompartmentNodeToolProvider(SysmlPackage.eINSTANCE.getElement_Documentation(), this.descriptionNameGenerator);
        nodeTools.add(docTool.create(null));
        ChildrenPartUsageCompartmentNodeToolProvider childPartTool = new ChildrenPartUsageCompartmentNodeToolProvider();
        nodeTools.add(childPartTool.create(null));
        NodeDescription portNodeDescription = cache.getNodeDescription(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage())).get();
        nodeTools.add(this.toolDescriptionService.createNodeTool(portNodeDescription, SysmlPackage.eINSTANCE.getPortUsage(), NodeContainmentKind.BORDER_NODE));
        nodeTools.add(this.toolDescriptionService.createNodeToolWithDirection(portNodeDescription, SysmlPackage.eINSTANCE.getPortUsage(), NodeContainmentKind.BORDER_NODE, FeatureDirectionKind.IN));
        nodeTools.add(this.toolDescriptionService.createNodeToolWithDirection(portNodeDescription, SysmlPackage.eINSTANCE.getPortUsage(), NodeContainmentKind.BORDER_NODE, FeatureDirectionKind.INOUT));
        nodeTools.add(this.toolDescriptionService.createNodeToolWithDirection(portNodeDescription, SysmlPackage.eINSTANCE.getPortUsage(), NodeContainmentKind.BORDER_NODE, FeatureDirectionKind.OUT));

        return this.diagramBuilderHelper.newNodeToolSection()
                .name("Create")
                .nodeTools(nodeTools.toArray(NodeTool[]::new))
                .edgeTools(this.getEdgeTools(cache).toArray(EdgeTool[]::new))
                .build();
    }

    private List<EdgeTool> getEdgeTools(IViewDiagramElementFinder cache) {
        return List.of();
    }

    private DropNodeTool createDropFromDiagramTool(IViewDiagramElementFinder cache) {
        var acceptedNodeTypes = new ArrayList<NodeDescription>();

        var optPortUsageBorderNodeDescription = cache.getNodeDescription(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage()));
        var optChildPartUsageNodeDescription = cache.getNodeDescription(ChildPartUsageNodeDescriptionProvider.NAME);
        var optFirstLevelChildPartUsageNodeDescription = cache.getNodeDescription(FirstLevelChildPartUsageNodeDescriptionProvider.NAME);

        acceptedNodeTypes.add(optPortUsageBorderNodeDescription.get());
        acceptedNodeTypes.add(optChildPartUsageNodeDescription.get());
        acceptedNodeTypes.add(optFirstLevelChildPartUsageNodeDescription.get());

        var dropElementFromDiagram = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression("droppedElement", "dropElementFromDiagram", List.of("droppedNode", "targetElement", "targetNode", "editingContext", "diagramContext",
                        "convertedNodes")));

        return this.diagramBuilderHelper.newDropNodeTool()
                .name("Drop from Diagram")
                .acceptedNodeTypes(acceptedNodeTypes.toArray(NodeDescription[]::new))
                .body(dropElementFromDiagram.build())
                .build();
    }
}
