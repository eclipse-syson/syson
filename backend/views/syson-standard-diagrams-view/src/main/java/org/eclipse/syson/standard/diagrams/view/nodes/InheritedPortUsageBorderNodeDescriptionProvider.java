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

import org.eclipse.emf.ecore.EReference;
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
 * Used to create the inherited port usage border node description.
 *
 * @author frouene
 */
public class InheritedPortUsageBorderNodeDescriptionProvider extends AbstractPortUsageBorderNodeDescriptionProvider {

    public static final String REDEFINE_INHERITED_PORT_SERVICE = "redefineInheritedPort";

    protected static final String REDEFINE_PORTS_PREFIX_TOOL_NAME = "Redefine Ports And ";

    protected static final String REDEFINED_TARGET = "redefinedTarget";

    public InheritedPortUsageBorderNodeDescriptionProvider(EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator nameGenerator) {
        super(eReference, colorProvider, nameGenerator);
    }

    @Override
    protected String getSemanticCandidatesExpression() {
        return AQLUtils.getSelfServiceCallExpression("getInheritedCompartmentItems", "'" + this.eReference.getName() + "'");
    }

    @Override
    protected OutsideLabelDescription createOutsideLabelDescription() {
        return this.diagramBuilderHelper.newOutsideLabelDescription()
                .labelExpression(AQLConstants.AQL + "'^' + self.getBorderNodeUsageLabel()")
                .position(OutsideLabelPosition.BOTTOM_CENTER)
                .style(this.createOutsideLabelStyle())
                .build();
    }

    @Override
    protected String getName() {
        return this.descriptionNameGenerator.getInheritedBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage(), this.eReference);
    }

    @Override
    protected NodePalette createNodePalette(IViewDiagramElementFinder cache, NodeDescription nodeDescription) {
        return this.diagramBuilderHelper.newNodePalette()
                .toolSections(this.defaultToolsFactory.createDefaultHideRevealNodeToolSection())
                .edgeTools(this.getEdgeTools(cache, nodeDescription).toArray(EdgeTool[]::new))
                .build();
    }

    @Override
    protected List<EdgeTool> getEdgeTools(IViewDiagramElementFinder cache, NodeDescription nodeDescription) {
        List<EdgeTool> edgeTools = new ArrayList<>();
        var optInheritedNestedPort = cache
                .getNodeDescription(this.descriptionNameGenerator.getInheritedBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort()));
        var optInheritedOwnedPort = cache
                .getNodeDescription(this.descriptionNameGenerator.getInheritedBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedPort()));
        if (optInheritedNestedPort.isPresent() && optInheritedOwnedPort.isPresent()) {
            var inheritedNestedPort = optInheritedNestedPort.get();
            var inheritedOwnedPort = optInheritedOwnedPort.get();
            edgeTools.add(this.createBindingConnectorAsUsageToInheritedPortUsageEdgeTool(List.of(inheritedNestedPort, inheritedOwnedPort)));
            edgeTools.add(this.createInterfaceUsageToInheritedPortUsageEdgeTool(List.of(inheritedNestedPort, inheritedOwnedPort)));
            edgeTools.add(this.createFlowUsageEdgeToInheritedPortUsageTool(List.of(inheritedNestedPort, inheritedOwnedPort)));
        }
        var optNestedPort = cache.getNodeDescription(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort()));
        var optOwnedPort = cache.getNodeDescription(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedPort()));
        if (optNestedPort.isPresent() && optOwnedPort.isPresent()) {
            var nestedPort = optNestedPort.get();
            var ownedPort = optOwnedPort.get();
            edgeTools.add(this.createBindingConnectorAsUsageToPortUsageEdgeTool(List.of(nestedPort, ownedPort)));
            edgeTools.add(this.createInterfaceUsageToPortUsageEdgeTool(List.of(nestedPort, ownedPort)));
            edgeTools.add(this.createFlowUsageEdgeToPortUsageTool(List.of(nestedPort, ownedPort)));
        }
        return edgeTools;
    }

    private EdgeTool createBindingConnectorAsUsageToPortUsageEdgeTool(List<NodeDescription> targetElementDescriptions) {
        return this.diagramBuilderHelper.newEdgeTool()
                .name(REDEFINE_PORT_PREFIX_TOOL_NAME + this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getBindingConnectorAsUsage()) + " (bind)")
                .iconURLsExpression(this.getToolIconURLsExpression(SysmlPackage.eINSTANCE.getBindingConnectorAsUsage().getName()))
                .body(this.viewBuilderHelper.newChangeContext()
                        .expression(AQLUtils.getServiceCallExpression(EdgeDescription.SEMANTIC_EDGE_SOURCE, REDEFINE_INHERITED_PORT_SERVICE, List.of(
                                EdgeDescription.EDGE_SOURCE, EditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT)))
                        .children(this.viewBuilderHelper.newChangeContext()
                                .expression(AQLUtils.getSelfServiceCallExpression("createBindingConnectorAsUsage", EdgeDescription.SEMANTIC_EDGE_TARGET))
                                .build())
                        .build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    private EdgeTool createInterfaceUsageToPortUsageEdgeTool(List<NodeDescription> targetElementDescriptions) {
        return this.diagramBuilderHelper.newEdgeTool()
                .name(REDEFINE_PORT_PREFIX_TOOL_NAME + this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getInterfaceUsage()) + " (connect)")
                .iconURLsExpression(this.getToolIconURLsExpression(SysmlPackage.eINSTANCE.getInterfaceUsage().getName()))
                .body(this.viewBuilderHelper.newChangeContext()
                        .expression(AQLUtils.getServiceCallExpression(EdgeDescription.SEMANTIC_EDGE_SOURCE, REDEFINE_INHERITED_PORT_SERVICE, List.of(
                                EdgeDescription.EDGE_SOURCE, EditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT)))
                        .children(this.viewBuilderHelper.newChangeContext()
                                .expression(AQLUtils.getSelfServiceCallExpression("createInterfaceUsage", EdgeDescription.SEMANTIC_EDGE_TARGET))
                                .build())
                        .build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    private EdgeTool createFlowUsageEdgeToPortUsageTool(List<NodeDescription> targetElementDescriptions) {
        return this.diagramBuilderHelper.newEdgeTool()
                .name(REDEFINE_PORT_PREFIX_TOOL_NAME + this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getFlowUsage()) + " (flow)")
                .iconURLsExpression(this.getToolIconURLsExpression(SysmlPackage.eINSTANCE.getFlowUsage().getName()))
                .body(this.viewBuilderHelper.newChangeContext()
                        .expression(AQLUtils.getServiceCallExpression(EdgeDescription.SEMANTIC_EDGE_SOURCE, REDEFINE_INHERITED_PORT_SERVICE, List.of(
                                EdgeDescription.EDGE_SOURCE, EditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT)))
                        .children(this.viewBuilderHelper.newChangeContext()
                                .expression(AQLUtils.getSelfServiceCallExpression("createFlowUsage", EdgeDescription.SEMANTIC_EDGE_TARGET))
                                .build())
                        .build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    private EdgeTool createBindingConnectorAsUsageToInheritedPortUsageEdgeTool(List<NodeDescription> targetElementDescriptions) {
        return this.diagramBuilderHelper.newEdgeTool()
                .name(REDEFINE_PORTS_PREFIX_TOOL_NAME + this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getBindingConnectorAsUsage()) + " (bind)")
                .iconURLsExpression(this.getToolIconURLsExpression(SysmlPackage.eINSTANCE.getBindingConnectorAsUsage().getName()))
                .body(this.viewBuilderHelper.newChangeContext()
                        .expression(AQLUtils.getServiceCallExpression(EdgeDescription.SEMANTIC_EDGE_TARGET, REDEFINE_INHERITED_PORT_SERVICE, List.of(
                                EdgeDescription.EDGE_TARGET, EditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT)))
                        .children(this.viewBuilderHelper.newLet()
                                .variableName(REDEFINED_TARGET)
                                .valueExpression(AQLConstants.AQL_SELF)
                                .children(this.viewBuilderHelper.newChangeContext()
                                        .expression(AQLUtils.getServiceCallExpression(EdgeDescription.SEMANTIC_EDGE_SOURCE, REDEFINE_INHERITED_PORT_SERVICE, List.of(
                                                EdgeDescription.EDGE_SOURCE, EditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT)))
                                        .children(this.viewBuilderHelper.newChangeContext()
                                                .expression(AQLUtils.getSelfServiceCallExpression("createBindingConnectorAsUsage", REDEFINED_TARGET))
                                                .build())
                                        .build())
                                .build())
                        .build()
                )
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    private EdgeTool createInterfaceUsageToInheritedPortUsageEdgeTool(List<NodeDescription> targetElementDescriptions) {
        return this.diagramBuilderHelper.newEdgeTool()
                .name(REDEFINE_PORTS_PREFIX_TOOL_NAME + this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getInterfaceUsage()) + " (connect)")
                .iconURLsExpression(this.getToolIconURLsExpression(SysmlPackage.eINSTANCE.getInterfaceUsage().getName()))
                .body(this.viewBuilderHelper.newChangeContext()
                        .expression(AQLUtils.getServiceCallExpression(EdgeDescription.SEMANTIC_EDGE_TARGET, REDEFINE_INHERITED_PORT_SERVICE, List.of(
                                EdgeDescription.EDGE_TARGET, EditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT)))
                        .children(this.viewBuilderHelper.newLet()
                                .variableName(REDEFINED_TARGET)
                                .valueExpression(AQLConstants.AQL_SELF)
                                .children(this.viewBuilderHelper.newChangeContext()
                                        .expression(AQLUtils.getServiceCallExpression(EdgeDescription.SEMANTIC_EDGE_SOURCE, REDEFINE_INHERITED_PORT_SERVICE, List.of(
                                                EdgeDescription.EDGE_SOURCE, EditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT)))
                                        .children(this.viewBuilderHelper.newChangeContext()
                                                .expression(AQLUtils.getSelfServiceCallExpression("createInterfaceUsage", REDEFINED_TARGET))
                                                .build())
                                        .build())
                                .build())
                        .build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    private EdgeTool createFlowUsageEdgeToInheritedPortUsageTool(List<NodeDescription> targetElementDescriptions) {
        return this.diagramBuilderHelper.newEdgeTool()
                .name(REDEFINE_PORTS_PREFIX_TOOL_NAME + this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getFlowUsage()) + " (flow)")
                .iconURLsExpression(this.getToolIconURLsExpression(SysmlPackage.eINSTANCE.getFlowUsage().getName()))
                .body(this.viewBuilderHelper.newChangeContext()
                        .expression(AQLUtils.getServiceCallExpression(EdgeDescription.SEMANTIC_EDGE_TARGET, REDEFINE_INHERITED_PORT_SERVICE, List.of(
                                EdgeDescription.EDGE_TARGET, EditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT)))
                        .children(this.viewBuilderHelper.newLet()
                                .variableName(REDEFINED_TARGET)
                                .valueExpression(AQLConstants.AQL_SELF)
                                .children(this.viewBuilderHelper.newChangeContext()
                                        .expression(AQLUtils.getServiceCallExpression(EdgeDescription.SEMANTIC_EDGE_SOURCE, REDEFINE_INHERITED_PORT_SERVICE, List.of(
                                                EdgeDescription.EDGE_SOURCE, EditingContext.EDITING_CONTEXT, DiagramContext.DIAGRAM_CONTEXT)))
                                        .children(this.viewBuilderHelper.newChangeContext()
                                                .expression(AQLUtils.getSelfServiceCallExpression("createFlowUsage", REDEFINED_TARGET))
                                                .build())
                                        .build())
                                .build())
                        .build()
                )
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }
}
