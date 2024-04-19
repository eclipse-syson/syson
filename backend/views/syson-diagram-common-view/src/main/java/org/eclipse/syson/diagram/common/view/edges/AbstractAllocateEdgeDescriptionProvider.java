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
package org.eclipse.syson.diagram.common.view.edges;

import java.util.List;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.ChangeContextBuilder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.ArrowStyle;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.EdgeDescription;
import org.eclipse.sirius.components.view.diagram.EdgeStyle;
import org.eclipse.sirius.components.view.diagram.LabelEditTool;
import org.eclipse.sirius.components.view.diagram.LineStyle;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the Allocate edge description.
 *
 * @author Jerome Gout
 */
public abstract class AbstractAllocateEdgeDescriptionProvider extends AbstractEdgeDescriptionProvider {

    public AbstractAllocateEdgeDescriptionProvider(IColorProvider colorProvider) {
        super(colorProvider);
    }

    /**
     * Implementers should provide the actual name of this {@link EdgeDescription}.
     *
     * @return the name of the edge description
     */
    protected abstract String getName();

    /**
     * Implementers should provide the list of {@link NodeDescription} that can be a source of this
     * {@link EdgeDescription}.
     *
     * @param cache
     *            the cache used to retrieve node descriptions.
     * @return the list of {@link NodeDescription} that can be a source of this {@link EdgeDescription}.
     */
    protected abstract List<NodeDescription> getSourceNodes(IViewDiagramElementFinder cache);

    /**
     * Implementers should provide the list of {@link NodeDescription} that can be a target of this
     * {@link EdgeDescription}.
     *
     * @param cache
     *            the cache used to retrieve node descriptions.
     * @return the list of {@link NodeDescription} that can be a target of this {@link EdgeDescription}.
     */
    protected abstract List<NodeDescription> getTargetNodes(IViewDiagramElementFinder cache);

    @Override
    public EdgeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getAllocationUsage());
        return this.diagramBuilderHelper.newEdgeDescription()
                .domainType(domainType)
                .isDomainBasedEdge(true)
                .centerLabelExpression("allocate")
                .name(this.getName())
                .semanticCandidatesExpression(AQLConstants.AQL_SELF + ".getAllReachableAllocateEdges()")
                .sourceNodesExpression(AQLConstants.AQL_SELF + ".getSourceAllocateEdge()")
                .style(this.createEdgeStyle())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .targetNodesExpression(AQLConstants.AQL_SELF + ".getTargetAllocateEdge()")
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optEdgeDescription = cache.getEdgeDescription(this.getName());
        EdgeDescription edgeDescription = optEdgeDescription.get();
        diagramDescription.getEdgeDescriptions().add(edgeDescription);

        edgeDescription.getSourceNodeDescriptions().addAll(this.getSourceNodes(cache));
        edgeDescription.getTargetNodeDescriptions().addAll(this.getTargetNodes(cache));

        edgeDescription.setPalette(this.createEdgePalette());
    }

    private EdgeStyle createEdgeStyle() {
        return this.diagramBuilderHelper.newEdgeStyle()
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_EDGE_COLOR))
                .edgeWidth(1)
                .lineStyle(LineStyle.SOLID)
                .sourceArrowStyle(ArrowStyle.NONE)
                .targetArrowStyle(ArrowStyle.INPUT_ARROW)
                .build();
    }

    @Override
    protected ChangeContextBuilder getSourceReconnectToolBody() {
        var unsetOldSource = this.viewBuilderHelper.newUnsetValue()
                .featureName(SysmlPackage.eINSTANCE.getDependency_Client().getName())
                .elementExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_SOURCE);

        var setNewSource = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getDependency_Client().getName())
                .valueExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_TARGET);

        return this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + AQLConstants.EDGE_SEMANTIC_ELEMENT)
                .children(unsetOldSource.build(), setNewSource.build());
    }

    @Override
    protected ChangeContextBuilder getTargetReconnectToolBody() {
        var unsetOldSource = this.viewBuilderHelper.newUnsetValue()
                .featureName(SysmlPackage.eINSTANCE.getDependency_Supplier().getName())
                .elementExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_SOURCE);

        var setNewSource = this.viewBuilderHelper.newSetValue()
                .featureName(SysmlPackage.eINSTANCE.getDependency_Supplier().getName())
                .valueExpression(AQLConstants.AQL + AQLConstants.SEMANTIC_RECONNECTION_TARGET);

        return this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + AQLConstants.EDGE_SEMANTIC_ELEMENT)
                .children(unsetOldSource.build(), setNewSource.build());
    }

    @Override
    protected LabelEditTool getEdgeEditTool() {
        var callEditService = this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL_SELF + ".directEditNameOff(newLabel)");

        return this.diagramBuilderHelper.newLabelEditTool()
                .name("Edit")
                .initialDirectEditLabelExpression(AQLConstants.AQL_SELF + ".getDefaultInitialDirectEditLabel()")
                .body(callEditService.build()).build();
    }
}
