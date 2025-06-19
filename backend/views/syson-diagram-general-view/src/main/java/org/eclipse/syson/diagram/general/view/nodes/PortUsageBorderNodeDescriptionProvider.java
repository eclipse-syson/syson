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
package org.eclipse.syson.diagram.general.view.nodes;

import java.util.List;

import org.eclipse.sirius.components.diagrams.description.EdgeDescription;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.EdgeTool;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
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
    protected List<EdgeTool> getEdgeTools(IViewDiagramElementFinder cache, NodeDescription nodeDescription) {
        NodeDescription portBorderNode = cache.getNodeDescription(this.nameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage())).get();
        return List.of(this.createBindingConnectorAsUsageEdgeTool(List.of(nodeDescription, portBorderNode)),
                this.createInterfaceUsageEdgeTool(List.of(nodeDescription, portBorderNode)),
                this.createFlowUsageEdgeTool(List.of(nodeDescription, portBorderNode)));
    }

    private EdgeTool createBindingConnectorAsUsageEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression(EdgeDescription.SEMANTIC_EDGE_SOURCE, "createBindingConnectorAsUsage", EdgeDescription.SEMANTIC_EDGE_TARGET));

        return builder
                .name(this.nameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getBindingConnectorAsUsage()) + " (bind)")
                .iconURLsExpression("/icons/full/obj16/" + SysmlPackage.eINSTANCE.getBindingConnectorAsUsage().getName() + ".svg")
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    private EdgeTool createInterfaceUsageEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression(EdgeDescription.SEMANTIC_EDGE_SOURCE, "createInterfaceUsage", EdgeDescription.SEMANTIC_EDGE_TARGET));

        return builder
                .name(this.nameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getInterfaceUsage()) + " (connect)")
                .iconURLsExpression("/icons/full/obj16/" + SysmlPackage.eINSTANCE.getInterfaceUsage().getName() + ".svg")
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

    private EdgeTool createFlowUsageEdgeTool(List<NodeDescription> targetElementDescriptions) {
        var builder = this.diagramBuilderHelper.newEdgeTool();

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression(EdgeDescription.SEMANTIC_EDGE_SOURCE, "createFlowUsage", EdgeDescription.SEMANTIC_EDGE_TARGET));

        return builder
                .name(this.nameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getFlowUsage()) + " (flow)")
                .iconURLsExpression("/icons/full/obj16/" + SysmlPackage.eINSTANCE.getFlowUsage().getName() + ".svg")
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();
    }

}
