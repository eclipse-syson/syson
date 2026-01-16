/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
package org.eclipse.syson.standard.diagrams.view.edges;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.view.ChangeContextBuilder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.ArrowStyle;
import org.eclipse.sirius.components.view.diagram.EdgeStyle;
import org.eclipse.sirius.components.view.diagram.LineStyle;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.edges.AbstractSubsettingEdgeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.SDVDiagramDescriptionProvider;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the {@link ReferenceSubsetting} edge description in the standard diagrams.
 *
 * @author arichard
 */
public class ReferenceSubsettingEdgeDescriptionProvider extends AbstractSubsettingEdgeDescriptionProvider {

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public ReferenceSubsettingEdgeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(SysmlPackage.eINSTANCE.getReferenceSubsetting(), colorProvider);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    @Override
    protected String getName() {
        return this.descriptionNameGenerator.getEdgeName(SysmlPackage.eINSTANCE.getReferenceSubsetting());
    }

    @Override
    protected String getSourceExpression() {
        return AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getReferenceSubsetting_ReferencingFeature().getName();
    }

    @Override
    protected String getTargetExpression() {
        return AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getReferenceSubsetting_ReferencedFeature().getName();
    }

    @Override
    protected List<NodeDescription> getSourceNodes(IViewDiagramElementFinder cache) {
        return this.getSourceAndTargetNodes(cache);
    }

    @Override
    protected List<NodeDescription> getTargetNodes(IViewDiagramElementFinder cache) {
        return this.getSourceAndTargetNodes(cache);
    }

    @Override
    protected ChangeContextBuilder getSourceReconnectToolBody() {
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

        return this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + AQLConstants.EDGE_SEMANTIC_ELEMENT)
                .children(unsetOldSubsettingFeature.build(), unsetOldSpecific.build(), setNewSubsettingFeature.build(),
                        setNewSpecific.build(), changeContextNewContainer.build());
    }

    @Override
    protected ChangeContextBuilder getTargetReconnectToolBody() {
        var unsetOldReferencedFeature = this.viewBuilderHelper.newUnsetValue()
                .featureName(SysmlPackage.eINSTANCE.getReferenceSubsetting_ReferencedFeature().getName())
                .elementExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_SOURCE);

        var setNewReferencedFeature = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getReferenceSubsetting_ReferencedFeature().getName())
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

        return this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + AQLConstants.EDGE_SEMANTIC_ELEMENT)
                .children(unsetOldReferencedFeature.build(), unsetOldSubsettedFeature.build(), unsetOldGeneral.build(), setNewReferencedFeature.build(), setNewSubsettedFeature.build(),
                        setNewGeneral.build());
    }

    @Override
    protected EdgeStyle createEdgeStyle() {
        return this.diagramBuilderHelper.newEdgeStyle()
                .borderSize(0)
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_EDGE_COLOR))
                .edgeWidth(1)
                .lineStyle(LineStyle.SOLID)
                .sourceArrowStyle(ArrowStyle.NONE)
                .targetArrowStyle(ArrowStyle.CLOSED_ARROW_WITH4_DOTS)
                .build();
    }

    private List<NodeDescription> getSourceAndTargetNodes(IViewDiagramElementFinder cache) {
        var sourcesAndTargets = new ArrayList<NodeDescription>();

        SDVDiagramDescriptionProvider.USAGES.forEach(usage -> {
            cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(usage)).ifPresent(sourcesAndTargets::add);
        });
        return sourcesAndTargets;
    }
}
