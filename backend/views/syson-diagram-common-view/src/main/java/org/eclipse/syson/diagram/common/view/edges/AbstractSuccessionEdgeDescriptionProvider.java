/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
import org.eclipse.sirius.components.view.diagram.LineStyle;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the Succession edge description.
 *
 * @author Jerome Gout
 */
public abstract class AbstractSuccessionEdgeDescriptionProvider extends AbstractEdgeDescriptionProvider {

    protected final IDescriptionNameGenerator descriptionNameGenerator;

    public AbstractSuccessionEdgeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.descriptionNameGenerator = descriptionNameGenerator;
    }

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
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getSuccessionAsUsage());
        return this.diagramBuilderHelper.newEdgeDescription()
                .domainType(domainType)
                .isDomainBasedEdge(true)
                .centerLabelExpression(AQLUtils.getSelfServiceCallExpression("getSuccessionLabel"))
                .name(this.descriptionNameGenerator.getEdgeName(SysmlPackage.eINSTANCE.getSuccession()))
                .semanticCandidatesExpression(AQLUtils.getSelfServiceCallExpression("getAllReachable", domainType))
                .style(this.createEdgeStyle())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .sourceNodesExpression("aql:self.source")
                .targetNodesExpression("aql:self.target")
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        cache.getEdgeDescription(this.descriptionNameGenerator.getEdgeName(SysmlPackage.eINSTANCE.getSuccession())).ifPresent(ed -> {
            diagramDescription.getEdgeDescriptions().add(ed);
            ed.getSourceNodeDescriptions().addAll(this.getSourceNodes(cache));
            ed.getTargetNodeDescriptions().addAll(this.getTargetNodes(cache));
            ed.setPalette(this.createEdgePalette());
        });
    }

    @Override
    protected ChangeContextBuilder getSourceReconnectToolBody() {
        var params = List.of(AQLConstants.SEMANTIC_RECONNECTION_SOURCE, AQLConstants.SEMANTIC_RECONNECTION_TARGET);
        return this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression(AQLConstants.EDGE_SEMANTIC_ELEMENT, "reconnectSourceSuccessionEdge", params));
    }

    @Override
    protected ChangeContextBuilder getTargetReconnectToolBody() {
        var params = List.of(AQLConstants.SEMANTIC_RECONNECTION_SOURCE, AQLConstants.SEMANTIC_RECONNECTION_TARGET);
        return this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression(AQLConstants.EDGE_SEMANTIC_ELEMENT, "reconnectTargetSuccessionEdge", params));
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
}
