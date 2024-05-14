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
import org.eclipse.syson.diagram.common.view.nodes.AbstractNodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the port usage border node description.
 *
 * @author arichard
 */
public class PortUsageBorderNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    private final IDescriptionNameGenerator nameGenerator;

    public PortUsageBorderNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator nameGenerator) {
        super(colorProvider);
        this.nameGenerator = Objects.requireNonNull(nameGenerator);
    }

    @Override
    public NodeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getPortUsage());
        return this.diagramBuilderHelper.newNodeDescription()
                .defaultHeightExpression("10")
                .defaultWidthExpression("10")
                .domainType(domainType)
                .outsideLabels(this.createOutsideLabelDescription())
                .name(this.getName())
                .semanticCandidatesExpression(AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getUsage_NestedPort().getName())
                // Default style if no conditional style can be applied
                .style(this.createImageNodeStyleDescription("images/PortUsage_In.svg"))
                .conditionalStyles(this.createPortUsageConditionalNodeStyles().toArray(ConditionalNodeStyle[]::new))
                .userResizable(true)
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optPortUsageBorderNodeDescription = cache.getNodeDescription(this.getName());

        NodeDescription nodeDescription = optPortUsageBorderNodeDescription.get();
        nodeDescription.setPalette(this.createNodePalette(cache, nodeDescription));
    }

    public String getName() {
        return this.nameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage());
    }

    private OutsideLabelDescription createOutsideLabelDescription() {
        return this.diagramBuilderHelper.newOutsideLabelDescription()
                .labelExpression(AQLConstants.AQL_SELF + ".getBorderNodePortUsageLabel()")
                .position(OutsideLabelPosition.BOTTOM_CENTER)
                .style(this.createOutsideLabelStyle())
                .build();
    }

    private OutsideLabelStyle createOutsideLabelStyle() {
        return this.diagramBuilderHelper.newOutsideLabelStyle()
                .bold(false)
                .fontSize(12)
                .italic(false)
                .labelColor(this.colorProvider.getColor(ViewConstants.DEFAULT_LABEL_COLOR))
                .showIcon(false)
                .strikeThrough(false)
                .underline(false)
                .build();
    }

    private List<ConditionalNodeStyle> createPortUsageConditionalNodeStyles() {
        return List.of(
                this.diagramBuilderHelper.newConditionalNodeStyle()
                        .condition("aql:self.isInPort()")
                        .style(this.createImageNodeStyleDescription("/images/PortUsage_In.svg"))
                .build(),
                this.diagramBuilderHelper.newConditionalNodeStyle()
                        .condition("aql:self.isOutPort()")
                        .style(this.createImageNodeStyleDescription("/images/PortUsage_Out.svg"))
                        .build(),
                this.diagramBuilderHelper.newConditionalNodeStyle()
                        .condition("aql:self.isInOutPort()")
                        .style(this.createImageNodeStyleDescription("/images/PortUsage_InOut.svg"))
                        .build()
        );
    }

    private NodeStyleDescription createImageNodeStyleDescription(String imagePath) {
        return this.diagramBuilderHelper.newImageNodeStyleDescription()
                .borderColor(this.colorProvider.getColor(ViewConstants.DEFAULT_BORDER_COLOR))
                .borderRadius(0)
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_BACKGROUND_COLOR))
                .positionDependentRotation(true)
                .shape(imagePath)
                .build();
    }

    private NodePalette createNodePalette(IViewDiagramElementFinder cache, NodeDescription nodeDescription) {
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

        NodeDescription rootPortBorderNode = cache.getNodeDescription(RootPortUsageBorderNodeDescriptionProvider.NAME).get();

        return this.diagramBuilderHelper.newNodePalette()
                .deleteTool(deleteTool.build())
                .labelEditTool(editTool.build())
                .toolSections(this.defaultToolsFactory.createDefaultHideRevealNodeToolSection())
                .edgeTools(
                        this.createBindingConnectorAsUsageEdgeTool(List.of(nodeDescription, rootPortBorderNode)),
                        this.createInterfaceUsageEdgeTool(List.of(nodeDescription, rootPortBorderNode)))
                .build();
    }

    private EdgeTool createBindingConnectorAsUsageEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE + ".createBindingConnectorAsUsage(" + EdgeDescription.SEMANTIC_EDGE_TARGET + ")");

        return builder
                .name(this.nameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getBindingConnectorAsUsage()))
                .iconURLsExpression("/icons/full/obj16/" + SysmlPackage.eINSTANCE.getBindingConnectorAsUsage().getName() + ".svg")
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    private EdgeTool createInterfaceUsageEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + EdgeDescription.SEMANTIC_EDGE_SOURCE + ".createInterfaceUsage(" + EdgeDescription.SEMANTIC_EDGE_TARGET + ")");

        return builder
                .name(this.nameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getInterfaceUsage()))
                .iconURLsExpression("/icons/full/obj16/" + SysmlPackage.eINSTANCE.getInterfaceUsage().getName() + ".svg")
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }
}
