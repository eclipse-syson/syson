/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
import org.eclipse.sirius.components.view.builder.generated.view.ChangeContextBuilder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.ArrowStyle;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.EdgeDescription;
import org.eclipse.sirius.components.view.diagram.EdgeStyle;
import org.eclipse.sirius.components.view.diagram.LabelEditTool;
import org.eclipse.sirius.components.view.diagram.LineStyle;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.diagram.common.view.services.ViewEdgeService;
import org.eclipse.syson.sysml.BindingConnectorAsUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the {@link BindingConnectorAsUsage} edge description.
 *
 * @author Arthur Daussy
 */
public abstract class AbstractFeatureValueEdgeDescriptionProvider extends AbstractEdgeDescriptionProvider {

    public AbstractFeatureValueEdgeDescriptionProvider(IColorProvider colorProvider) {
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
     *         the cache used to retrieve node descriptions.
     * @return the list of {@link NodeDescription} that can be a source of this {@link EdgeDescription}.
     */
    protected abstract List<NodeDescription> getSourceNodes(IViewDiagramElementFinder cache);

    /**
     * Implementers should provide the list of {@link NodeDescription} that can be a target of this
     * {@link EdgeDescription}.
     *
     * @param cache
     *         the cache used to retrieve node descriptions.
     * @return the list of {@link NodeDescription} that can be a target of this {@link EdgeDescription}.
     */
    protected abstract List<NodeDescription> getTargetNodes(IViewDiagramElementFinder cache);

    @Override
    public EdgeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getFeatureValue());
        return this.diagramBuilderHelper.newEdgeDescription()
                .domainType(domainType)
                .isDomainBasedEdge(true)
                .centerLabelExpression("=")
                .name(this.getName())
                .semanticCandidatesExpression(AQLUtils.getSelfServiceCallExpression("getAllReachable", domainType))
                .sourceExpression(AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getFeatureValue_FeatureWithValue().getName())
                .style(this.createEdgeStyle())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .targetExpression(ServiceMethod.of0(ViewEdgeService::getFeatureValueTarget).aqlSelf())
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optEdgeDescription = cache.getEdgeDescription(this.getName());
        if (optEdgeDescription.isPresent()) {
            EdgeDescription edgeDescription = optEdgeDescription.get();
            diagramDescription.getEdgeDescriptions().add(edgeDescription);

            edgeDescription.getSourceDescriptions().addAll(this.getSourceNodes(cache));
            edgeDescription.getTargetDescriptions().addAll(this.getTargetNodes(cache));

            edgeDescription.setPalette(this.createEdgePalette(cache));
        }
    }

    private EdgeStyle createEdgeStyle() {
        return this.diagramBuilderHelper.newEdgeStyle()
                .borderSize(0)
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_EDGE_COLOR))
                .edgeWidth(1)
                .lineStyle(LineStyle.SOLID)
                .sourceArrowStyle(ArrowStyle.NONE)
                .targetArrowStyle(ArrowStyle.NONE)
                .build();
    }

    @Override
    protected LabelEditTool getEdgeEditTool() {
        return null;
    }

    @Override
    protected ChangeContextBuilder getSourceReconnectToolBody() {
        return this.viewBuilderHelper.newChangeContext()
                .expression("aql:'Unable to reconnect source from a FeatureValue implicit binding (= {0})'.sendMessageWithTextualRepresentation('WARNING',edgeSemanticElement)");
    }

    @Override
    protected ChangeContextBuilder getTargetReconnectToolBody() {
        return this.viewBuilderHelper.newChangeContext()
                .expression("aql:'Unable to reconnect target from a FeatureValue implicit binding (= {0})'.sendMessageWithTextualRepresentation('WARNING',edgeSemanticElement)");
    }


}
