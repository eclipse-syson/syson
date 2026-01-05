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

import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.core.api.IEditingContext;
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
import org.eclipse.syson.diagram.common.view.DescriptionFinder;
import org.eclipse.syson.diagram.common.view.edges.AbstractEdgeDescriptionProvider;
import org.eclipse.syson.diagram.services.aql.DiagramMutationAQLService;
import org.eclipse.syson.diagram.services.aql.DiagramQueryAQLService;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to describe an {@link org.eclipse.syson.sysml.ConnectionUsage} edge between two {@link org.eclipse.syson.sysml.Usage}.
 *
 * @author Arthur Daussy
 */
public class ConnectionUsageEdgeDescriptionProvider extends AbstractEdgeDescriptionProvider {

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public ConnectionUsageEdgeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optEdgeDescription = cache.getEdgeDescription(this.getName());
        if (optEdgeDescription.isPresent()) {
            EdgeDescription edgeDescription = optEdgeDescription.get();
            diagramDescription.getEdgeDescriptions().add(edgeDescription);

            List<NodeDescription> sourceNodes = this.getSourceNodes(cache);
            List<NodeDescription> targetNodes = this.getTargetNodes(cache);

            edgeDescription.getSourceDescriptions().addAll(sourceNodes);

            edgeDescription.getTargetDescriptions().addAll(targetNodes);

            edgeDescription.setPalette(this.createEdgePalette(cache));
        }
    }

    @Override
    protected LabelEditTool getEdgeEditTool() {
        var callEditService = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of1(DiagramMutationAQLService::editEdgeCenterLabel).aqlSelf("newLabel"));

        return this.diagramBuilderHelper.newLabelEditTool()
                .name("Edit")
                .initialDirectEditLabelExpression(ServiceMethod.of0(DiagramQueryAQLService::getDefaultInitialDirectEditLabel).aqlSelf())
                .body(callEditService.build())
                .build();
    }

    @Override
    public EdgeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getConnectionUsage());
        return this.diagramBuilderHelper.newEdgeDescription()
                .domainType(domainType)
                .isDomainBasedEdge(true)
                .centerLabelExpression(ServiceMethod.of0(DiagramQueryAQLService::getEdgeLabel).aqlSelf())
                .name(this.getName())
                .semanticCandidatesExpression(AQLUtils.getSelfServiceCallExpression("getAllReachable", domainType))
                .sourceExpression("aql:self.getSource()")
                .style(this.createEdgeStyle())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .targetExpression("aql:self.getTarget()")
                .preconditionExpression(ServiceMethod.of4(DiagramQueryAQLService::shouldRenderConnectionUsageEdge)
                        .aqlSelf(org.eclipse.sirius.components.diagrams.description.EdgeDescription.GRAPHICAL_EDGE_SOURCE,
                                org.eclipse.sirius.components.diagrams.description.EdgeDescription.GRAPHICAL_EDGE_TARGET,
                                org.eclipse.sirius.components.diagrams.description.DiagramDescription.CACHE,
                                IEditingContext.EDITING_CONTEXT)
                        // Needs this to avoid instantiation on inheriting concept
                        + "and self.oclIsTypeOf(sysml::ConnectionUsage)")
                .build();
    }

    @Override
    protected ChangeContextBuilder getSourceReconnectToolBody() {
        return this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of5(DiagramMutationAQLService::reconnectSource).aql(
                                AQLConstants.EDGE_SEMANTIC_ELEMENT,
                                AQLConstants.SEMANTIC_RECONNECTION_TARGET,
                                AQLConstants.RECONNECTION_TARGET_VIEW,
                                AQLConstants.OTHER_END,
                                IEditingContext.EDITING_CONTEXT,
                                AQLConstants.DIAGRAM
                        ));
    }

    @Override
    protected ChangeContextBuilder getTargetReconnectToolBody() {
        return this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of5(DiagramMutationAQLService::reconnectTarget).aql(
                        AQLConstants.EDGE_SEMANTIC_ELEMENT,
                        AQLConstants.SEMANTIC_RECONNECTION_TARGET,
                        AQLConstants.OTHER_END,
                        AQLConstants.RECONNECTION_TARGET_VIEW,
                        IEditingContext.EDITING_CONTEXT,
                        AQLConstants.DIAGRAM
                ));
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

    private String getName() {
        return this.descriptionNameGenerator.getEdgeName(SysmlPackage.eINSTANCE.getConnectionUsage());
    }

    private List<NodeDescription> getSourceNodes(IViewDiagramElementFinder cache) {
        return new DescriptionFinder(this.descriptionNameGenerator).getConnectableNodeDescriptions(cache.getNodeDescriptions(), SysmlPackage.eINSTANCE.getUsage());
    }

    private List<NodeDescription> getTargetNodes(IViewDiagramElementFinder cache) {
        return new DescriptionFinder(this.descriptionNameGenerator).getConnectableNodeDescriptions(cache.getNodeDescriptions(), SysmlPackage.eINSTANCE.getUsage());
    }
}
