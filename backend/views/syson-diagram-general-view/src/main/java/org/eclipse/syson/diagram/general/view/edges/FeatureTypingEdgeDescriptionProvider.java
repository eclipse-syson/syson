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
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the {@link FeatureTyping} edge description.
 *
 * @author arichard
 */
public class FeatureTypingEdgeDescriptionProvider extends AbstractEdgeDescriptionProvider {

    public static final String NAME = "GV Edge FeatureTyping";

    public FeatureTypingEdgeDescriptionProvider(IColorProvider colorProvider) {
        super(colorProvider);
    }

    @Override
    public EdgeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getFeatureTyping());
        return this.diagramBuilderHelper.newEdgeDescription()
                .domainType(domainType)
                .isDomainBasedEdge(true)
                .labelExpression("")
                .name(NAME)
                .semanticCandidatesExpression("aql:self.getAllReachable(" + domainType + ")")
                .sourceNodesExpression(AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getFeatureTyping_TypedFeature().getName())
                .style(this.createEdgeStyle())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .targetNodesExpression(AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getFeatureTyping_Type().getName())
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optEdgeDescription = cache.getEdgeDescription(NAME);
        EdgeDescription edgeDescription = optEdgeDescription.get();
        diagramDescription.getEdgeDescriptions().add(edgeDescription);

        GeneralViewDiagramDescriptionProvider.USAGES.forEach(usage -> {
            var optNodeDescription = cache.getNodeDescription(GVDescriptionNameGenerator.getNodeName(usage));
            edgeDescription.getSourceNodeDescriptions().add(optNodeDescription.get());
        });

        GeneralViewDiagramDescriptionProvider.DEFINITIONS.forEach(definition -> {
            var optNodeDescription = cache.getNodeDescription(GVDescriptionNameGenerator.getNodeName(definition));
            edgeDescription.getTargetNodeDescriptions().add(optNodeDescription.get());
        });

        edgeDescription.setPalette(this.createEdgePalette(List.of(this.createSourceReconnectTool(), this.createTargetReconnectTool())));
    }

    private EdgeStyle createEdgeStyle() {
        return this.diagramBuilderHelper.newEdgeStyle()
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_EDGE_COLOR))
                .edgeWidth(1)
                .lineStyle(LineStyle.SOLID)
                .sourceArrowStyle(ArrowStyle.NONE)
                .targetArrowStyle(ArrowStyle.CLOSED_ARROW_WITH_DOTS)
                .build();
    }

    private SourceEdgeEndReconnectionTool createSourceReconnectTool() {
        var builder = this.diagramBuilderHelper.newSourceEdgeEndReconnectionTool();

        var unsetOldTypedFeature = this.viewBuilderHelper.newUnsetValue()
                .featureName(SysmlPackage.eINSTANCE.getFeatureTyping_TypedFeature().getName())
                .elementExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_SOURCE);

        var setNewTypedFeature = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getFeatureTyping_TypedFeature().getName())
                .valueExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_TARGET);

        var unsetOldSpecific = this.viewBuilderHelper.newUnsetValue()
                .featureName(SysmlPackage.eINSTANCE.getSpecialization_Specific().getName())
                .elementExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_SOURCE);

        var setNewSpecific = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSpecialization_Specific().getName())
                .valueExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_TARGET);

        var setNewContainer = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getElement_OwnedRelationship().getName())
                .valueExpression(AQLConstants.AQL + AQLConstants.EDGE_SEMANTIC_ELEMENT);

        var changeContextNewContainer = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_TARGET)
                .children(setNewContainer.build());

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + AQLConstants.EDGE_SEMANTIC_ELEMENT)
                .children(unsetOldTypedFeature.build(), unsetOldSpecific.build(), setNewTypedFeature.build(), setNewSpecific.build(), changeContextNewContainer.build());

        return builder
                .name("Reconnect Source")
                .body(body.build())
                .build();
    }

    private TargetEdgeEndReconnectionTool createTargetReconnectTool() {
        var builder = this.diagramBuilderHelper.newTargetEdgeEndReconnectionTool();

        var unsetOldType = this.viewBuilderHelper.newUnsetValue()
                .featureName(SysmlPackage.eINSTANCE.getFeatureTyping_Type().getName())
                .elementExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_SOURCE);

        var setNewType = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getFeatureTyping_Type().getName())
                .valueExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_TARGET);

        var unsetOldGeneral = this.viewBuilderHelper.newUnsetValue()
                .featureName(SysmlPackage.eINSTANCE.getSpecialization_General().getName())
                .elementExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_SOURCE);

        var setNewGeneral = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSpecialization_General().getName())
                .valueExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_TARGET);

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + AQLConstants.EDGE_SEMANTIC_ELEMENT)
                .children(unsetOldType.build(), unsetOldGeneral.build(), setNewType.build(), setNewGeneral.build());

        return builder
                .name("Reconnect Target")
                .preconditionExpression(AQLConstants.AQL + AQLConstants.EDGE_SEMANTIC_ELEMENT + ".checkFeatureTypingEdgeReconnectionTarget(" + AQLConstants.SEMANTIC_RECONNECTION_TARGET + ")")
                .body(body.build())
                .build();
    }
}
