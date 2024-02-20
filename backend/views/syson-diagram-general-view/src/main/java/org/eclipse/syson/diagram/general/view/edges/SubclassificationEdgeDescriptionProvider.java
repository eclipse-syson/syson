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
import org.eclipse.syson.diagram.general.view.nodes.PackageNodeDescriptionProvider;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the {@link Subclassification} edge description.
 *
 * @author arichard
 */
public class SubclassificationEdgeDescriptionProvider extends AbstractEdgeDescriptionProvider {

    public static final String NAME = "GV Edge Subclassification";

    public SubclassificationEdgeDescriptionProvider(IColorProvider colorProvider) {
        super(colorProvider);
    }

    @Override
    public EdgeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getSubclassification());
        return this.diagramBuilderHelper.newEdgeDescription()
                .domainType(domainType)
                .isDomainBasedEdge(true)
                .labelExpression("")
                .name(NAME)
                .semanticCandidatesExpression("aql:self.getAllReachable(" + domainType + ")")
                .sourceNodesExpression(AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getSubclassification_Subclassifier().getName())
                .style(this.createEdgeStyle())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .targetNodesExpression(AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getSubclassification_Superclassifier().getName())
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optEdgeDescription = cache.getEdgeDescription(NAME);
        var optAttributeDefinitionNodeDescription = cache.getNodeDescription(GVDescriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getAttributeDefinition()));
        var optEnumerationDefinitionNodeDescription = cache.getNodeDescription(GVDescriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getEnumerationDefinition()));
        var optInterfaceDefinitionNodeDescription = cache.getNodeDescription(GVDescriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getInterfaceDefinition()));
        var optItemDefinitionNodeDescription = cache.getNodeDescription(GVDescriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getItemDefinition()));
        var optMetadataDefinitionNodeDescription = cache.getNodeDescription(GVDescriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getMetadataDefinition()));
        var optPackageNodeDescription = cache.getNodeDescription(PackageNodeDescriptionProvider.NAME);
        var optPartDefinitionNodeDescription = cache.getNodeDescription(GVDescriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartDefinition()));
        var optPortDefinitionNodeDescription = cache.getNodeDescription(GVDescriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPortDefinition()));

        EdgeDescription edgeDescription = optEdgeDescription.get();
        diagramDescription.getEdgeDescriptions().add(edgeDescription);
        edgeDescription.getSourceNodeDescriptions().add(optAttributeDefinitionNodeDescription.get());
        edgeDescription.getTargetNodeDescriptions().add(optAttributeDefinitionNodeDescription.get());
        edgeDescription.getSourceNodeDescriptions().add(optEnumerationDefinitionNodeDescription.get());
        edgeDescription.getTargetNodeDescriptions().add(optEnumerationDefinitionNodeDescription.get());
        edgeDescription.getSourceNodeDescriptions().add(optInterfaceDefinitionNodeDescription.get());
        edgeDescription.getTargetNodeDescriptions().add(optInterfaceDefinitionNodeDescription.get());
        edgeDescription.getSourceNodeDescriptions().add(optItemDefinitionNodeDescription.get());
        edgeDescription.getTargetNodeDescriptions().add(optItemDefinitionNodeDescription.get());
        edgeDescription.getSourceNodeDescriptions().add(optMetadataDefinitionNodeDescription.get());
        edgeDescription.getTargetNodeDescriptions().add(optMetadataDefinitionNodeDescription.get());
        edgeDescription.getSourceNodeDescriptions().add(optPackageNodeDescription.get());
        edgeDescription.getTargetNodeDescriptions().add(optPackageNodeDescription.get());
        edgeDescription.getSourceNodeDescriptions().add(optPartDefinitionNodeDescription.get());
        edgeDescription.getTargetNodeDescriptions().add(optPartDefinitionNodeDescription.get());
        edgeDescription.getSourceNodeDescriptions().add(optPortDefinitionNodeDescription.get());
        edgeDescription.getTargetNodeDescriptions().add(optPortDefinitionNodeDescription.get());

        edgeDescription.setPalette(this.createEdgePalette(List.of(this.createSourceReconnectTool(), this.createTargetReconnectTool())));
    }

    private EdgeStyle createEdgeStyle() {
        return this.diagramBuilderHelper.newEdgeStyle()
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_EDGE_COLOR))
                .edgeWidth(1)
                .lineStyle(LineStyle.SOLID)
                .sourceArrowStyle(ArrowStyle.NONE)
                .targetArrowStyle(ArrowStyle.INPUT_CLOSED_ARROW)
                .build();
    }

    private SourceEdgeEndReconnectionTool createSourceReconnectTool() {
        var builder = this.diagramBuilderHelper.newSourceEdgeEndReconnectionTool();

        var unsetOldSubclassifier = this.viewBuilderHelper.newUnsetValue()
                .featureName(SysmlPackage.eINSTANCE.getSubclassification_Subclassifier().getName())
                .elementExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_SOURCE);

        var setNewSubclassifier = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSubclassification_Subclassifier().getName())
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
                .children(unsetOldSubclassifier.build(), unsetOldSpecific.build(), setNewSubclassifier.build(), setNewSpecific.build(), changeContextNewContainer.build());

        return builder
                .name("Reconnect Source")
                .body(body.build())
                .build();
    }

    private TargetEdgeEndReconnectionTool createTargetReconnectTool() {
        var builder = this.diagramBuilderHelper.newTargetEdgeEndReconnectionTool();

        var unsetOldSuperclassifier = this.viewBuilderHelper.newUnsetValue()
                .featureName(SysmlPackage.eINSTANCE.getSubclassification_Superclassifier().getName())
                .elementExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_SOURCE);

        var setNewSuperclassifier = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSubclassification_Superclassifier().getName())
                .valueExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_TARGET);

        var unsetOldGeneral = this.viewBuilderHelper.newUnsetValue()
                .featureName(SysmlPackage.eINSTANCE.getSpecialization_General().getName())
                .elementExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_SOURCE);

        var setNewGeneral = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getSpecialization_General().getName())
                .valueExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_TARGET);

        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + AQLConstants.EDGE_SEMANTIC_ELEMENT)
                .children(unsetOldSuperclassifier.build(), unsetOldGeneral.build(), setNewSuperclassifier.build(), setNewGeneral.build());

        return builder
                .name("Reconnect Target")
                .body(body.build())
                .build();
    }
}
