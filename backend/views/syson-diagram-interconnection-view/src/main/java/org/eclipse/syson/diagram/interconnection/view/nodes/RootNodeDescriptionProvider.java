/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
import org.eclipse.sirius.components.view.diagram.ConditionalNodeStyle;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.DropNodeTool;
import org.eclipse.sirius.components.view.diagram.HeaderSeparatorDisplayMode;
import org.eclipse.sirius.components.view.diagram.InsideLabelDescription;
import org.eclipse.sirius.components.view.diagram.InsideLabelPosition;
import org.eclipse.sirius.components.view.diagram.InsideLabelStyle;
import org.eclipse.sirius.components.view.diagram.LabelTextAlign;
import org.eclipse.sirius.components.view.diagram.NodeContainmentKind;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.sirius.components.view.diagram.UserResizableDirection;
import org.eclipse.syson.diagram.common.view.nodes.AbstractNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.services.description.ToolDescriptionService;
import org.eclipse.syson.diagram.interconnection.view.IVDescriptionNameGenerator;
import org.eclipse.syson.diagram.interconnection.view.InterconnectionViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the root node description of the Interconnection View.
 *
 * @author arichard
 */
public class RootNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    private final ToolDescriptionService toolDescriptionService;

    private final IVDescriptionNameGenerator descriptionNameGenerator;

    public RootNodeDescriptionProvider(IColorProvider colorProvider, IVDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
        this.toolDescriptionService = new ToolDescriptionService(descriptionNameGenerator);
    }

    @Override
    public NodeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getElement());
        return this.diagramBuilderHelper.newNodeDescription()
                .defaultHeightExpression("400")
                .defaultWidthExpression("700")
                .domainType(domainType)
                .insideLabel(this.createInsideLabelDescription())
                .name(this.getName())
                .semanticCandidatesExpression(AQLConstants.AQL_SELF + ".owner")
                .style(this.createPartDefinitionNodeStyle())
                .conditionalStyles(this.createPartUsageConditionalNodeStyle())
                .userResizable(UserResizableDirection.BOTH)
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optRootDefinitionNodeDescription = cache.getNodeDescription(this.getName());
        var optFirstLevelChildPartUsageNodeDescription = cache.getNodeDescription(this.descriptionNameGenerator.getFirstLevelNodeName(SysmlPackage.eINSTANCE.getPartUsage()));
        var optFirstLevelChildActionUsageNodeDescription = cache.getNodeDescription(this.descriptionNameGenerator.getFirstLevelNodeName(SysmlPackage.eINSTANCE.getActionUsage()));
        var optRootPortUsageBorderNodeDescription = cache.getNodeDescription(RootPortUsageBorderNodeDescriptionProvider.NAME);
        var reusedChildren = new ArrayList<NodeDescription>();
        InterconnectionViewDiagramDescriptionProvider.ANNOTATINGS.forEach(annotating -> cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(annotating)).ifPresent(reusedChildren::add));

        NodeDescription nodeDescription = optRootDefinitionNodeDescription.get();
        diagramDescription.getNodeDescriptions().add(nodeDescription);
        nodeDescription.getChildrenDescriptions().add(optFirstLevelChildPartUsageNodeDescription.get());
        nodeDescription.getChildrenDescriptions().add(optFirstLevelChildActionUsageNodeDescription.get());
        nodeDescription.getBorderNodesDescriptions().add(optRootPortUsageBorderNodeDescription.get());
        nodeDescription.getReusedChildNodeDescriptions().addAll(reusedChildren);
        nodeDescription.setPalette(this.createNodePalette(cache));
    }

    public String getName() {
        return this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getElement());
    }

    private InsideLabelDescription createInsideLabelDescription() {
        return this.diagramBuilderHelper.newInsideLabelDescription()
                .labelExpression(AQLConstants.AQL_SELF + ".getContainerLabel()")
                .position(InsideLabelPosition.TOP_CENTER)
                .style(this.createInsideLabelStyle())
                .textAlign(LabelTextAlign.CENTER)
                .build();
    }

    private InsideLabelStyle createInsideLabelStyle() {
        return this.diagramBuilderHelper.newInsideLabelStyle()
                .borderSize(0)
                .headerSeparatorDisplayMode(HeaderSeparatorDisplayMode.NEVER)
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIconExpression(AQLUtils.getSelfServiceCallExpression("showIcon"))
                .withHeader(true)
                .build();
    }

    private NodeStyleDescription createPartDefinitionNodeStyle() {
        return this.diagramBuilderHelper.newRectangularNodeStyleDescription()
                .borderColor(this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR))
                .borderRadius(0)
                .background(this.colorProvider.getColor(ViewConstants.DEFAULT_BACKGROUND_COLOR))
                .childrenLayoutStrategy(this.diagramBuilderHelper.newFreeFormLayoutStrategyDescription().build())
                .build();
    }

    private ConditionalNodeStyle createPartUsageConditionalNodeStyle() {
        return this.diagramBuilderHelper.newConditionalNodeStyle()
                .condition(AQLConstants.AQL_SELF + ".oclIsKindOf(" + SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getPartUsage()) + ")")
                .style(this.diagramBuilderHelper.newRectangularNodeStyleDescription()
                        .borderColor(this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR))
                        .borderRadius(10)
                        .background(this.colorProvider.getColor(ViewConstants.DEFAULT_BACKGROUND_COLOR))
                        .build()
                )
                .build();
    }

    private NodePalette createNodePalette(IViewDiagramElementFinder cache) {
        var callEditService = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("directEditNode", "newLabel"));

        var editTool = this.diagramBuilderHelper.newLabelEditTool()
                .name("Edit")
                .initialDirectEditLabelExpression(AQLConstants.AQL_SELF + ".getDefaultInitialDirectEditLabel()")
                .body(callEditService.build());

        return this.diagramBuilderHelper.newNodePalette()
                .labelEditTool(editTool.build())
                .dropNodeTool(this.createDropFromDiagramTool(cache))
                .toolSections(this.createNodeToolSections(cache).toArray(NodeToolSection[]::new))
                .build();
    }

    private List<NodeToolSection> createNodeToolSections(IViewDiagramElementFinder cache) {
        List<NodeToolSection> nodeToolSections = new ArrayList<>();
        var structureSection = this.toolDescriptionService.buildStructureSection();
        var behaviorSection = this.toolDescriptionService.buildBehaviorSection();
        var relatedElementsNodeToolSection = this.toolDescriptionService.relatedElementsNodeToolSection(true);

        cache.getNodeDescription(this.descriptionNameGenerator.getFirstLevelNodeName(SysmlPackage.eINSTANCE.getPartUsage())).ifPresent(partND -> {
            structureSection.getNodeTools().add(this.toolDescriptionService.createNodeTool(partND, SysmlPackage.eINSTANCE.getPartUsage(), NodeContainmentKind.CHILD_NODE));
        });
        cache.getNodeDescription(this.descriptionNameGenerator.getFirstLevelNodeName(SysmlPackage.eINSTANCE.getActionUsage())).ifPresent(actionND -> {
            behaviorSection.getNodeTools().add(this.toolDescriptionService.createNodeTool(actionND, SysmlPackage.eINSTANCE.getActionUsage(), NodeContainmentKind.CHILD_NODE));
        });
        cache.getNodeDescription(RootPortUsageBorderNodeDescriptionProvider.NAME).ifPresent(portND -> {
            structureSection.getNodeTools().add(this.toolDescriptionService.createNodeTool(portND, SysmlPackage.eINSTANCE.getPortUsage(), NodeContainmentKind.BORDER_NODE));
            structureSection.getNodeTools()
                    .add(this.toolDescriptionService.createNodeToolWithDirection(portND, SysmlPackage.eINSTANCE.getPortUsage(), NodeContainmentKind.BORDER_NODE, FeatureDirectionKind.IN));
            structureSection.getNodeTools()
                    .add(this.toolDescriptionService.createNodeToolWithDirection(portND, SysmlPackage.eINSTANCE.getPortUsage(), NodeContainmentKind.BORDER_NODE, FeatureDirectionKind.INOUT));
            structureSection.getNodeTools()
                    .add(this.toolDescriptionService.createNodeToolWithDirection(portND, SysmlPackage.eINSTANCE.getPortUsage(), NodeContainmentKind.BORDER_NODE, FeatureDirectionKind.OUT));

        });

        nodeToolSections.add(structureSection);
        nodeToolSections.add(behaviorSection);
        nodeToolSections.add(relatedElementsNodeToolSection);
        return nodeToolSections;
    }

    private DropNodeTool createDropFromDiagramTool(IViewDiagramElementFinder cache) {
        var acceptedNodeTypes = new ArrayList<NodeDescription>();

        var optPortUsageBorderNodeDescription = cache.getNodeDescription(RootPortUsageBorderNodeDescriptionProvider.NAME);
        var optChildPartUsageNodeDescription = cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()));
        var optFirstLevelChildPartUsageNodeDescription = cache.getNodeDescription(this.descriptionNameGenerator.getFirstLevelNodeName(SysmlPackage.eINSTANCE.getPartUsage()));

        acceptedNodeTypes.add(optPortUsageBorderNodeDescription.get());
        acceptedNodeTypes.add(optChildPartUsageNodeDescription.get());
        acceptedNodeTypes.add(optFirstLevelChildPartUsageNodeDescription.get());
        InterconnectionViewDiagramDescriptionProvider.ANNOTATINGS
                .forEach(annotating -> cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(annotating)).ifPresent(acceptedNodeTypes::add));

        var dropElementFromDiagram = this.viewBuilderHelper.newChangeContext()
                .expression("aql:droppedElement.dropElementFromDiagram(droppedNode, targetElement, targetNode, editingContext, diagramContext, convertedNodes)");

        return this.diagramBuilderHelper.newDropNodeTool()
                .name("Drop from Diagram")
                .acceptedNodeTypes(acceptedNodeTypes.toArray(NodeDescription[]::new))
                .body(dropElementFromDiagram.build())
                .build();
    }
}
