/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.standard.diagrams.view.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.diagrams.description.EdgeDescription;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.EdgeTool;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.OutsideLabelDescription;
import org.eclipse.sirius.components.view.diagram.OutsideLabelPosition;
import org.eclipse.sirius.web.application.editingcontext.EditingContext;
import org.eclipse.syson.diagram.common.view.nodes.AbstractPortUsageBorderNodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to create the port usage border node description for general view diagram.
 *
 * @author frouene
 */
public class PortUsageBorderNodeDescriptionProvider extends AbstractPortUsageBorderNodeDescriptionProvider {

    public PortUsageBorderNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator nameGenerator) {
        super(colorProvider, nameGenerator);
    }

    @Override
    protected String getSemanticCandidatesExpression() {
        return AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getUsage_NestedPort().getName();
    }

    @Override
    protected OutsideLabelDescription createOutsideLabelDescription() {
        return this.diagramBuilderHelper.newOutsideLabelDescription()
                .labelExpression(AQLUtils.getSelfServiceCallExpression("getBorderNodeUsageLabel"))
                .position(OutsideLabelPosition.BOTTOM_CENTER)
                .style(this.createOutsideLabelStyle())
                .build();
    }

    @Override
    protected String getName() {
        return this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage());
    }

    @Override
    protected NodePalette createNodePalette(IViewDiagramElementFinder cache, NodeDescription nodeDescription) {
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
                .labelEditTool(editTool.build())
                .toolSections(this.defaultToolsFactory.createDefaultHideRevealNodeToolSection())
                .edgeTools(this.getEdgeTools(cache, nodeDescription).toArray(EdgeTool[]::new))
                .build();
    }

    @Override
    protected List<EdgeTool> getEdgeTools(IViewDiagramElementFinder cache, NodeDescription nodeDescription) {
        List<EdgeTool> edgeTools = new ArrayList<>();
        if (cache.getNodeDescription(this.getName()).isPresent()) {
            edgeTools.add(this.createBindingConnectorAsUsageEdgeTool(List.of(nodeDescription)));
            edgeTools.add(this.createInterfaceUsageEdgeTool(List.of(nodeDescription)));
            edgeTools.add(this.createFlowUsageEdgeTool(List.of(nodeDescription)));
            cache.getNodeDescription(this.descriptionNameGenerator.getInheritedBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage()))
                    .ifPresent(inheritedPortBorderNode -> {
                        edgeTools.add(this.createBindingConnectorAsUsageToInheritedPortUsageEdgeTool(List.of(inheritedPortBorderNode)));
                        edgeTools.add(this.createInterfaceUsageToInheritedPortUsageEdgeTool(List.of(inheritedPortBorderNode)));
                        edgeTools.add(this.createFlowUsageEdgeToInheritedPortUsageTool(List.of(inheritedPortBorderNode)));
                    });
        }
        return edgeTools;
    }

    private EdgeTool createBindingConnectorAsUsageEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression(EdgeDescription.SEMANTIC_EDGE_SOURCE, "createBindingConnectorAsUsage", EdgeDescription.SEMANTIC_EDGE_TARGET));

        return builder
                .name(this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getBindingConnectorAsUsage()) + " (bind)")
                .iconURLsExpression(this.getToolIconURLsExpression(SysmlPackage.eINSTANCE.getBindingConnectorAsUsage().getName()))
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    private EdgeTool createInterfaceUsageEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression(EdgeDescription.SEMANTIC_EDGE_SOURCE, "createInterfaceUsage", EdgeDescription.SEMANTIC_EDGE_TARGET));

        return builder
                .name(this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getInterfaceUsage()) + " (connect)")
                .iconURLsExpression(this.getToolIconURLsExpression(SysmlPackage.eINSTANCE.getInterfaceUsage().getName()))
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    private EdgeTool createFlowUsageEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression(EdgeDescription.SEMANTIC_EDGE_SOURCE, "createFlowUsage", EdgeDescription.SEMANTIC_EDGE_TARGET));

        return builder
                .name(this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getFlowUsage()) + " (flow)")
                .iconURLsExpression(this.getToolIconURLsExpression(SysmlPackage.eINSTANCE.getFlowUsage().getName()))
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    private EdgeTool createBindingConnectorAsUsageToInheritedPortUsageEdgeTool(List<NodeDescription> targetElementDescriptions) {
        return this.diagramBuilderHelper.newEdgeTool()
                .name(REDEFINE_PORT_PREFIX_TOOL_NAME + this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getBindingConnectorAsUsage()) + " (bind)")
                .iconURLsExpression(this.getToolIconURLsExpression(SysmlPackage.eINSTANCE.getBindingConnectorAsUsage().getName()))
                .body(this.viewBuilderHelper.newChangeContext()
                        .expression(AQLUtils.getServiceCallExpression(EdgeDescription.SEMANTIC_EDGE_TARGET, "redefineInheritedPort", List.of(
                                EdgeDescription.EDGE_TARGET, EditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT)))
                        .children(this.viewBuilderHelper.newChangeContext()
                                .expression(AQLUtils.getServiceCallExpression(EdgeDescription.SEMANTIC_EDGE_SOURCE, "createBindingConnectorAsUsage", AQLConstants.SELF))
                                .build())
                        .build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    private EdgeTool createInterfaceUsageToInheritedPortUsageEdgeTool(List<NodeDescription> targetElementDescriptions) {
        return this.diagramBuilderHelper.newEdgeTool()
                .name(REDEFINE_PORT_PREFIX_TOOL_NAME + this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getInterfaceUsage()) + " (connect)")
                .iconURLsExpression(this.getToolIconURLsExpression(SysmlPackage.eINSTANCE.getInterfaceUsage().getName()))
                .body(this.viewBuilderHelper.newChangeContext()
                        .expression(AQLUtils.getServiceCallExpression(EdgeDescription.SEMANTIC_EDGE_TARGET, "redefineInheritedPort", List.of(
                                EdgeDescription.EDGE_TARGET, EditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT)))
                        .children(this.viewBuilderHelper.newChangeContext()
                                .expression(AQLUtils.getServiceCallExpression(EdgeDescription.SEMANTIC_EDGE_SOURCE, "createInterfaceUsage", AQLConstants.SELF))
                                .build())
                        .build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    private EdgeTool createFlowUsageEdgeToInheritedPortUsageTool(List<NodeDescription> targetElementDescriptions) {
        return this.diagramBuilderHelper.newEdgeTool()
                .name(REDEFINE_PORT_PREFIX_TOOL_NAME + this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getFlowUsage()) + " (flow)")
                .iconURLsExpression(this.getToolIconURLsExpression(SysmlPackage.eINSTANCE.getFlowUsage().getName()))
                .body(this.viewBuilderHelper.newChangeContext()
                        .expression(AQLUtils.getServiceCallExpression(EdgeDescription.SEMANTIC_EDGE_TARGET, "redefineInheritedPort", List.of(
                                EdgeDescription.EDGE_TARGET, EditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT)))
                        .children(this.viewBuilderHelper.newChangeContext()
                                .expression(AQLUtils.getServiceCallExpression(EdgeDescription.SEMANTIC_EDGE_SOURCE, "createFlowUsage", AQLConstants.SELF))
                                .build())
                        .build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }
}
