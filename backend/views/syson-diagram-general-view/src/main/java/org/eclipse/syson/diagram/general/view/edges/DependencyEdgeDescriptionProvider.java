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

import java.util.List;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.ArrowStyle;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.EdgeDescription;
import org.eclipse.sirius.components.view.diagram.EdgeStyle;
import org.eclipse.sirius.components.view.diagram.LineStyle;
import org.eclipse.sirius.components.view.diagram.SourceEdgeEndReconnectionTool;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.sirius.components.view.diagram.TargetEdgeEndReconnectionTool;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.PackageNodeDescriptionProvider;
import org.eclipse.syson.sysml.Dependency;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the {@link Dependency} edge description.
 *
 * @author arichard
 */
public class DependencyEdgeDescriptionProvider extends AbstractEdgeDescriptionProvider {

    public static final String NAME = "GV Edge Dependency";

    public DependencyEdgeDescriptionProvider(IColorProvider colorProvider) {
        super(colorProvider);
    }

    @Override
    public EdgeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getDependency());
        return this.diagramBuilderHelper.newEdgeDescription()
                .domainType(domainType)
                .isDomainBasedEdge(true)
                .labelExpression("")
                .name(NAME)
                .semanticCandidatesExpression("aql:self.getAllReachable(" + domainType + ")")
                .sourceNodesExpression(AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getDependency_Client().getName())
                .style(this.createEdgeStyle())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .targetNodesExpression(AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getDependency_Supplier().getName())
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optEdgeDescription = cache.getEdgeDescription(NAME);
        EdgeDescription edgeDescription = optEdgeDescription.get();
        diagramDescription.getEdgeDescriptions().add(edgeDescription);

        GeneralViewDiagramDescriptionProvider.DEFINITIONS.forEach(definition -> {
            var optNodeDescription = cache.getNodeDescription(GVDescriptionNameGenerator.getNodeName(definition));
            edgeDescription.getSourceNodeDescriptions().add(optNodeDescription.get());
            edgeDescription.getTargetNodeDescriptions().add(optNodeDescription.get());
        });

        GeneralViewDiagramDescriptionProvider.USAGES.forEach(usage -> {
            var optNodeDescription = cache.getNodeDescription(GVDescriptionNameGenerator.getNodeName(usage));
            edgeDescription.getSourceNodeDescriptions().add(optNodeDescription.get());
            edgeDescription.getTargetNodeDescriptions().add(optNodeDescription.get());
        });

        var optPackageNodeDescription = cache.getNodeDescription(PackageNodeDescriptionProvider.NAME);
        edgeDescription.getSourceNodeDescriptions().add(optPackageNodeDescription.get());
        edgeDescription.getTargetNodeDescriptions().add(optPackageNodeDescription.get());

        edgeDescription.setPalette(this.createEdgePalette(List.of(this.createSourceReconnectTool(),
                this.createTargetReconnectTool())));
    }

    private EdgeStyle createEdgeStyle() {
        return this.diagramBuilderHelper.newEdgeStyle()
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_EDGE_COLOR))
                .edgeWidth(1)
                .lineStyle(LineStyle.DASH)
                .sourceArrowStyle(ArrowStyle.NONE)
                .targetArrowStyle(ArrowStyle.INPUT_ARROW)
                .build();
    }

    private SourceEdgeEndReconnectionTool createSourceReconnectTool() {
        var builder = this.diagramBuilderHelper.newSourceEdgeEndReconnectionTool();

        var unsetOldSource = this.viewBuilderHelper.newUnsetValue()
                .featureName(SysmlPackage.eINSTANCE.getDependency_Client().getName())
                .elementExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_SOURCE);

        var setNewSource = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getDependency_Client().getName())
                .valueExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_TARGET);

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + AQLConstants.EDGE_SEMANTIC_ELEMENT)
                .children(unsetOldSource.build(), setNewSource.build());

        return builder
                .name("Reconnect Source")
                .body(body.build())
                .build();
    }

    private TargetEdgeEndReconnectionTool createTargetReconnectTool() {
        var builder = this.diagramBuilderHelper.newTargetEdgeEndReconnectionTool();

        var unsetOldSource = this.viewBuilderHelper.newUnsetValue()
                .featureName(SysmlPackage.eINSTANCE.getDependency_Supplier().getName())
                .elementExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_SOURCE);

        var setNewSource = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getDependency_Supplier().getName())
                .valueExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_TARGET);

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + AQLConstants.EDGE_SEMANTIC_ELEMENT)
                .children(unsetOldSource.build(), setNewSource.build());

        return builder
                .name("Reconnect Target")
                .body(body.build())
                .build();
    }
}
