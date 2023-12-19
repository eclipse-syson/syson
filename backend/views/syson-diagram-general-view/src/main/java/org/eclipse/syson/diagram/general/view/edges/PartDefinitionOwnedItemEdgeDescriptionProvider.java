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
package org.eclipse.syson.diagram.general.view.edges;

import java.util.List;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.ArrowStyle;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.EdgeDescription;
import org.eclipse.sirius.components.view.diagram.EdgePalette;
import org.eclipse.sirius.components.view.diagram.EdgeReconnectionTool;
import org.eclipse.sirius.components.view.diagram.EdgeStyle;
import org.eclipse.sirius.components.view.diagram.LineStyle;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.diagram.general.view.nodes.ItemUsageNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.PartDefinitionNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.PartUsageNodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the edge description between part definition and their nested part usages.
 *
 * @author arichard
 */
public class PartDefinitionOwnedItemEdgeDescriptionProvider extends AbstractEdgeDescriptionProvider {

    public static final String NAME = "GV Edge PartDefinition OwnedItem";

    public PartDefinitionOwnedItemEdgeDescriptionProvider(IColorProvider colorProvider) {
        super(colorProvider);
    }

    @Override
    public EdgeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getItemUsage());
        return this.diagramBuilderHelper.newEdgeDescription()
                .domainType(domainType)
                .isDomainBasedEdge(false)
                .labelExpression(AQLConstants.AQL + org.eclipse.sirius.components.diagrams.description.EdgeDescription.SEMANTIC_EDGE_TARGET + ".getMultiplicityLabel()")
                .name(NAME)
                .sourceNodesExpression(AQLConstants.AQL_SELF)
                .style(this.createEdgeStyle())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .targetNodesExpression(AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getDefinition_OwnedItem().getName())
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optEdgeDescription = cache.getEdgeDescription(NAME);
        var optPartDefinitionNodeDescription = cache.getNodeDescription(PartDefinitionNodeDescriptionProvider.NAME);
        var optPartUsageNodeDescription = cache.getNodeDescription(PartUsageNodeDescriptionProvider.NAME);
        var optItemUsageNodeDescription = cache.getNodeDescription(ItemUsageNodeDescriptionProvider.NAME);

        EdgeDescription edgeDescription = optEdgeDescription.get();
        diagramDescription.getEdgeDescriptions().add(edgeDescription);
        edgeDescription.getSourceNodeDescriptions().add(optPartDefinitionNodeDescription.get());
        edgeDescription.getTargetNodeDescriptions().add(optPartUsageNodeDescription.get());
        edgeDescription.getTargetNodeDescriptions().add(optItemUsageNodeDescription.get());

        edgeDescription.setPalette(this.createEdgePalette(List.of()));
    }

    private EdgeStyle createEdgeStyle() {
        return this.diagramBuilderHelper.newEdgeStyle()
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_EDGE_COLOR))
                .edgeWidth(1)
                .lineStyle(LineStyle.SOLID)
                .sourceArrowStyle(ArrowStyle.FILL_DIAMOND)
                .targetArrowStyle(ArrowStyle.NONE)
                .build();
    }

    @Override
    protected EdgePalette createEdgePalette(List<EdgeReconnectionTool> edgeReconnectionTools) {
        var changeContext = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self.deleteFromModel()");

        var deleteTool = this.diagramBuilderHelper.newDeleteTool()
                .name("Delete from Model")
                .body(changeContext.build());

        var callEditService = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + org.eclipse.sirius.components.diagrams.description.EdgeDescription.SEMANTIC_EDGE_TARGET + ".editMultiplicityRangeCenterLabel(newLabel)");

        var centerLabelEditTool = this.diagramBuilderHelper.newLabelEditTool()
                .name("Center Label Edit Tool")
                .initialDirectEditLabelExpression(
                        AQLConstants.AQL + org.eclipse.sirius.components.diagrams.description.EdgeDescription.SEMANTIC_EDGE_TARGET + ".getMultiplicityRangeInitialDirectEditLabel()")
                .body(callEditService.build());

        return this.diagramBuilderHelper
                .newEdgePalette()
                .deleteTool(deleteTool.build())
                .centerLabelEditTool(centerLabelEditTool.build())
                .edgeReconnectionTools(edgeReconnectionTools.toArray(new EdgeReconnectionTool[edgeReconnectionTools.size()]))
                .build();
    }
}
