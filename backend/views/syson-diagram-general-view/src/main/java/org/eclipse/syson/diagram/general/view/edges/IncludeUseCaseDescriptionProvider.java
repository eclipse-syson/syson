/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.view.ChangeContextBuilder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.ArrowStyle;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.DiagramElementDescription;
import org.eclipse.sirius.components.view.diagram.EdgeDescription;
import org.eclipse.sirius.components.view.diagram.EdgeStyle;
import org.eclipse.sirius.components.view.diagram.LineStyle;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.diagram.common.view.edges.AbstractEdgeDescriptionProvider;
import org.eclipse.syson.sysml.IncludeUseCaseUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Edge for {@link IncludeUseCaseUsage}.
 *
 * @author Arthur Daussy
 */
public class IncludeUseCaseDescriptionProvider extends AbstractEdgeDescriptionProvider {

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public IncludeUseCaseDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.descriptionNameGenerator = descriptionNameGenerator;
    }

    @Override
    public EdgeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getIncludeUseCaseUsage());
        return this.diagramBuilderHelper.newEdgeDescription()
                .domainType(domainType)
                .isDomainBasedEdge(true)
                .centerLabelExpression(LabelConstants.OPEN_QUOTE + "include" + LabelConstants.CLOSE_QUOTE)
                .name(this.getName())
                .semanticCandidatesExpression(AQLUtils.getSelfServiceCallExpression("getAllReachable", domainType))
                .sourceExpression("aql:self.owningUsage")
                .style(this.createEdgeStyle())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .targetExpression("aql:self.useCaseIncluded")
                .build();
    }

    private String getName() {
        return this.descriptionNameGenerator.getEdgeName(SysmlPackage.eINSTANCE.getIncludeUseCaseUsage());
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optEdgeDescription = cache.getEdgeDescription(this.getName());
        if (optEdgeDescription.isPresent()) {
            EdgeDescription edgeDescription = optEdgeDescription.get();
            diagramDescription.getEdgeDescriptions().add(edgeDescription);

            edgeDescription.getSourceDescriptions().addAll(this.getSourceTargetNodes(cache));
            edgeDescription.getTargetDescriptions().addAll(this.getSourceTargetNodes(cache));

            edgeDescription.setPalette(this.createEdgePalette(cache));
        }
    }

    private Collection<? extends DiagramElementDescription> getSourceTargetNodes(IViewDiagramElementFinder cache) {
        List<NodeDescription> nodes = new ArrayList<>();
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getUseCaseUsage())).ifPresent(nodes::add);
        return nodes;
    }

    private EdgeStyle createEdgeStyle() {
        return this.diagramBuilderHelper.newEdgeStyle()
                .borderSize(0)
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_EDGE_COLOR))
                .edgeWidth(1)
                .lineStyle(LineStyle.SOLID)
                .sourceArrowStyle(ArrowStyle.NONE)
                .targetArrowStyle(ArrowStyle.INPUT_FILL_CLOSED_ARROW)
                .build();
    }

    @Override
    protected ChangeContextBuilder getSourceReconnectToolBody() {
        var params = List.of(AQLConstants.SEMANTIC_RECONNECTION_TARGET);
        return this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression(AQLConstants.EDGE_SEMANTIC_ELEMENT, "reconnectSourceIncludeUseCaseUsage", params));
    }

    @Override
    protected ChangeContextBuilder getTargetReconnectToolBody() {
        var params = List.of(AQLConstants.SEMANTIC_RECONNECTION_TARGET);
        return this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression(AQLConstants.EDGE_SEMANTIC_ELEMENT, "reconnectTargetIncludeUseCaseUsage", params));
    }

}
