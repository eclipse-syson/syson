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
package org.eclipse.syson.diagram.general.view.edges;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.ArrowStyle;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.EdgeDescription;
import org.eclipse.sirius.components.view.diagram.EdgeStyle;
import org.eclipse.sirius.components.view.diagram.LineStyle;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.diagram.common.view.edges.AbstractEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the edge description between part usages and their nested part usages.
 *
 * @author arichard
 */
public class PartUsageNestedPartEdgeDescriptionProvider extends AbstractEdgeDescriptionProvider {

    public static final String NAME = "GV Edge PartUsage Nested PartUsage";

    public PartUsageNestedPartEdgeDescriptionProvider(IColorProvider colorProvider) {
        super(colorProvider);
    }

    @Override
    public EdgeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getPartUsage());
        return this.diagramBuilderHelper.newEdgeDescription()
                .domainType(domainType)
                .isDomainBasedEdge(false)
                .labelExpression("")
                .name(NAME)
                .sourceNodesExpression(AQLConstants.AQL_SELF)
                .style(this.createEdgeStyle())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .targetNodesExpression(AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getUsage_NestedPart().getName())
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var nameGenerator = new GVDescriptionNameGenerator();
        var optEdgeDescription = cache.getEdgeDescription(NAME);
        var optPartUsageNodeDescription = cache.getNodeDescription(nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()));

        EdgeDescription edgeDescription = optEdgeDescription.get();
        diagramDescription.getEdgeDescriptions().add(edgeDescription);
        edgeDescription.getSourceNodeDescriptions().add(optPartUsageNodeDescription.get());
        edgeDescription.getTargetNodeDescriptions().add(optPartUsageNodeDescription.get());
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
}
