/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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

import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.diagrams.description.EdgeDescription;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.ConditionalNodeStyle;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.EdgeTool;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.NodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.OutsideLabelDescription;
import org.eclipse.sirius.components.view.diagram.OutsideLabelPosition;
import org.eclipse.sirius.components.view.diagram.OutsideLabelStyle;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.sirius.components.view.diagram.UserResizableDirection;
import org.eclipse.syson.diagram.common.view.nodes.AbstractNodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the item usage border node description.
 *
 * @author gdaniel
 */
public class ItemUsageBorderNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public ItemUsageBorderNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    @Override
    public NodeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getItemUsage());
        return this.diagramBuilderHelper.newNodeDescription()
                .defaultHeightExpression("10")
                .defaultWidthExpression("10")
                .domainType(domainType)
                .outsideLabels(this.createOutsideLabelDescription())
                .name(this.getName())
                .semanticCandidatesExpression(AQLUtils.getSelfServiceCallExpression("getBorderNodesItems"))
                .style(this.createItemUnsetNodeStyle())
                .conditionalStyles(this.createItemUsageConditionalNodeStyles().toArray(ConditionalNodeStyle[]::new))
                .userResizable(UserResizableDirection.NONE)
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optItemUsageBorderNodeDescription = cache.getNodeDescription(this.getName());

        NodeDescription nodeDescription = optItemUsageBorderNodeDescription.get();
        nodeDescription.setPalette(this.createNodePalette(cache, nodeDescription));
    }

    public String getName() {
        return this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage());
    }

    private NodeStyleDescription createItemUnsetNodeStyle() {
        return this.diagramBuilderHelper.newRectangularNodeStyleDescription()
                .borderColor(this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR))
                .borderRadius(0)
                .background(this.colorProvider.getColor(ViewConstants.DEFAULT_BACKGROUND_COLOR))
                .build();
    }

    private OutsideLabelDescription createOutsideLabelDescription() {
        return this.diagramBuilderHelper.newOutsideLabelDescription()
                .labelExpression(AQLUtils.getSelfServiceCallExpression("getBorderNodeUsageLabel"))
                .position(OutsideLabelPosition.BOTTOM_CENTER)
                .style(this.createOutsideLabelStyle())
                .build();
    }

    private OutsideLabelStyle createOutsideLabelStyle() {
        return this.diagramBuilderHelper.newOutsideLabelStyle()
                .bold(false)
                .borderSize(0)
                .fontSize(12)
                .italic(false)
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIconExpression("aql:false")
                .strikeThrough(false)
                .underline(false)
                .build();
    }

    private List<ConditionalNodeStyle> createItemUsageConditionalNodeStyles() {
        var borderColor = this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR);
        return List.of(
                this.diagramBuilderHelper.newConditionalNodeStyle()
                        .condition(AQLUtils.getSelfServiceCallExpression("isInFeature"))
                        .style(this.createImageNodeStyleDescription("/images/feature_in.svg", borderColor, true))
                        .build(),
                this.diagramBuilderHelper.newConditionalNodeStyle()
                        .condition(AQLUtils.getSelfServiceCallExpression("isOutFeature"))
                        .style(this.createImageNodeStyleDescription("/images/feature_out.svg", borderColor, true))
                        .build(),
                this.diagramBuilderHelper.newConditionalNodeStyle()
                        .condition(AQLUtils.getSelfServiceCallExpression("isInOutFeature"))
                        .style(this.createImageNodeStyleDescription("/images/feature_inout.svg", borderColor, true))
                        .build()
        );
    }

    private NodePalette createNodePalette(IViewDiagramElementFinder cache, NodeDescription nodeDescription) {
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

        NodeDescription portBorderNodeDescription = cache.getNodeDescription(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage())).get();
        NodeDescription rootPortBorderNode = cache.getNodeDescription(RootPortUsageBorderNodeDescriptionProvider.NAME).get();

        return this.diagramBuilderHelper.newNodePalette()
                .deleteTool(deleteTool.build())
                .labelEditTool(editTool.build())
                .toolSections(this.defaultToolsFactory.createDefaultHideRevealNodeToolSection())
                .edgeTools(
                        this.createBindingConnectorAsUsageEdgeTool(List.of(nodeDescription)),
                        this.createFlowConnectionUsageEdgeTool(List.of(nodeDescription, portBorderNodeDescription, rootPortBorderNode)))
                .build();
    }

    private EdgeTool createBindingConnectorAsUsageEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression(EdgeDescription.SEMANTIC_EDGE_SOURCE, "createBindingConnectorAsUsage", EdgeDescription.SEMANTIC_EDGE_TARGET));

        return builder
                .name(this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getBindingConnectorAsUsage()) + " (bind)")
                .iconURLsExpression("/icons/full/obj16/" + SysmlPackage.eINSTANCE.getBindingConnectorAsUsage().getName() + ".svg")
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    private EdgeTool createFlowConnectionUsageEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression(EdgeDescription.SEMANTIC_EDGE_SOURCE, "createFlowConnectionUsage", EdgeDescription.SEMANTIC_EDGE_TARGET));

        return builder
                .name(this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getFlowConnectionUsage()) + " (flow)")
                .iconURLsExpression("/icons/full/obj16/" + SysmlPackage.eINSTANCE.getFlowConnectionUsage().getName() + ".svg")
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }
}
