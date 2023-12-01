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
import org.eclipse.sirius.components.view.diagram.EdgeStyle;
import org.eclipse.sirius.components.view.diagram.LineStyle;
import org.eclipse.sirius.components.view.diagram.SourceEdgeEndReconnectionTool;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.sirius.components.view.diagram.TargetEdgeEndReconnectionTool;
import org.eclipse.syson.diagram.general.view.AQLConstants;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.diagram.general.view.SysMLMetamodelHelper;
import org.eclipse.syson.diagram.general.view.nodes.AttributeUsageNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.InterfaceUsageNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.ItemUsageNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.PartUsageNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.nodes.PortUsageNodeDescriptionProvider;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Used to create the {@link Redefinition} edge description.
 *
 * @author arichard
 */
public class RedefinitionEdgeDescriptionProvider extends AbstractEdgeDescriptionProvider {

    public static final String NAME = "GV Edge Redefinition";

    public RedefinitionEdgeDescriptionProvider(IColorProvider colorProvider) {
        super(colorProvider);
    }

    @Override
    public EdgeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getRedefinition());
        return this.diagramBuilderHelper.newEdgeDescription()
                .domainType(domainType)
                .isDomainBasedEdge(true)
                .labelExpression("")
                .name(NAME)
                .semanticCandidatesExpression("aql:self.getAllReachable(" + domainType + ")")
                .sourceNodesExpression("aql:self." + SysmlPackage.eINSTANCE.getRedefinition_RedefiningFeature().getName())
                .style(this.createEdgeStyle())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .targetNodesExpression("aql:self." + SysmlPackage.eINSTANCE.getRedefinition_RedefinedFeature().getName())
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optEdgeDescription = cache.getEdgeDescription(NAME);
        var optAttributeUsageNodeDescription = cache.getNodeDescription(AttributeUsageNodeDescriptionProvider.NAME);
        var optInterfaceUsageNodeDescription = cache.getNodeDescription(InterfaceUsageNodeDescriptionProvider.NAME);
        var optItemUsageNodeDescription = cache.getNodeDescription(ItemUsageNodeDescriptionProvider.NAME);
        var optPartUsageNodeDescription = cache.getNodeDescription(PartUsageNodeDescriptionProvider.NAME);
        var optPortUsageNodeDescription = cache.getNodeDescription(PortUsageNodeDescriptionProvider.NAME);

        if (optEdgeDescription.isPresent()) {
            EdgeDescription edgeDescription = optEdgeDescription.get();
            diagramDescription.getEdgeDescriptions().add(edgeDescription);
            edgeDescription.getSourceNodeDescriptions().add(optAttributeUsageNodeDescription.get());
            edgeDescription.getTargetNodeDescriptions().add(optAttributeUsageNodeDescription.get());
            edgeDescription.getSourceNodeDescriptions().add(optInterfaceUsageNodeDescription.get());
            edgeDescription.getTargetNodeDescriptions().add(optInterfaceUsageNodeDescription.get());
            edgeDescription.getSourceNodeDescriptions().add(optItemUsageNodeDescription.get());
            edgeDescription.getTargetNodeDescriptions().add(optItemUsageNodeDescription.get());
            edgeDescription.getSourceNodeDescriptions().add(optPartUsageNodeDescription.get());
            edgeDescription.getTargetNodeDescriptions().add(optPartUsageNodeDescription.get());
            edgeDescription.getSourceNodeDescriptions().add(optPortUsageNodeDescription.get());
            edgeDescription.getTargetNodeDescriptions().add(optPortUsageNodeDescription.get());

            edgeDescription.setPalette(this.createEdgePalette(List.of(this.createSourceReconnectTool(), this.createTargetReconnectTool())));
        }
    }

    private EdgeStyle createEdgeStyle() {
        return this.diagramBuilderHelper.newEdgeStyle()
                .color(this.colorProvider.getColor(GeneralViewDiagramDescriptionProvider.DEFAULT_EDGE_COLOR))
                .edgeWidth(1)
                .lineStyle(LineStyle.SOLID)
                .sourceArrowStyle(ArrowStyle.NONE)
                .targetArrowStyle(ArrowStyle.CLOSED_ARROW_WITH_VERTICAL_BAR)
                .build();
    }

    private SourceEdgeEndReconnectionTool createSourceReconnectTool() {
        var builder = this.diagramBuilderHelper.newSourceEdgeEndReconnectionTool();

        var unsetOldRedefiningFeature = this.viewBuilderHelper.newUnsetValue()
                .featureName(SysmlPackage.eINSTANCE.getRedefinition_RedefiningFeature().getName())
                .elementExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_SOURCE);

        var setNewRedefiningFeature = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getRedefinition_RedefiningFeature().getName())
                .valueExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_TARGET);

        var unsetOldSubsettingFeature = this.viewBuilderHelper.newUnsetValue()
                .featureName(SysmlPackage.eINSTANCE.getSubsetting_SubsettingFeature().getName())
                .elementExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_SOURCE);

        var setNewSubsettingFeature = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSubsetting_SubsettingFeature().getName())
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
                .children(unsetOldRedefiningFeature.build(), unsetOldSubsettingFeature.build(), unsetOldSpecific.build(), setNewRedefiningFeature.build(), setNewSubsettingFeature.build(),
                        setNewSpecific.build(), changeContextNewContainer.build());

        return builder
                .name("Reconnect Source")
                .body(body.build())
                .build();
    }

    private TargetEdgeEndReconnectionTool createTargetReconnectTool() {
        var builder = this.diagramBuilderHelper.newTargetEdgeEndReconnectionTool();

        var unsetOldRedefinedFeature = this.viewBuilderHelper.newUnsetValue()
                .featureName(SysmlPackage.eINSTANCE.getRedefinition_RedefinedFeature().getName())
                .elementExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_SOURCE);

        var setNewRedefinedFeature = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getRedefinition_RedefinedFeature().getName())
                .valueExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_TARGET);

        var unsetOldSubsettedFeature = this.viewBuilderHelper.newUnsetValue()
                .featureName(SysmlPackage.eINSTANCE.getSubsetting_SubsettedFeature().getName())
                .elementExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_SOURCE);

        var setNewSubsettedFeature = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSubsetting_SubsettedFeature().getName())
                .valueExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_TARGET);

        var unsetOldGeneral = this.viewBuilderHelper.newUnsetValue()
                .featureName(SysmlPackage.eINSTANCE.getSpecialization_General().getName())
                .elementExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_SOURCE);

        var setNewGeneral = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSpecialization_General().getName())
                .valueExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_TARGET);

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + AQLConstants.EDGE_SEMANTIC_ELEMENT)
                .children(unsetOldRedefinedFeature.build(), unsetOldSubsettedFeature.build(), unsetOldGeneral.build(), setNewRedefinedFeature.build(), setNewSubsettedFeature.build(),
                        setNewGeneral.build());

        return builder
                .name("Reconnect Target")
                .body(body.build())
                .build();
    }
}
