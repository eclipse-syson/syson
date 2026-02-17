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

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ChangeContextBuilder;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.ArrowStyle;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.EdgeDescription;
import org.eclipse.sirius.components.view.diagram.EdgeStyle;
import org.eclipse.sirius.components.view.diagram.LabelEditTool;
import org.eclipse.sirius.components.view.diagram.LineStyle;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.diagram.services.aql.DiagramMutationAQLService;
import org.eclipse.syson.diagram.services.aql.DiagramQueryAQLService;
import org.eclipse.syson.model.services.aql.ModelQueryAQLService;
import org.eclipse.syson.sysml.InterfaceUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the {@link InterfaceUsage} edge description.
 *
 * @author frouene
 */
public abstract class AbstractInterfaceUsageEdgeDescriptionProvider extends AbstractEdgeDescriptionProvider {

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    public AbstractInterfaceUsageEdgeDescriptionProvider(IColorProvider colorProvider) {
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
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getInterfaceUsage());
        return this.diagramBuilderHelper.newEdgeDescription()
                .centerLabelExpression(ServiceMethod.of0(DiagramQueryAQLService::getEdgeLabel).aqlSelf())
                .domainType(domainType)
                .isDomainBasedEdge(true)
                .name(this.getName())
                .preconditionExpression(ServiceMethod.of4(DiagramQueryAQLService::shouldRenderConnectorEdge)
                        .aqlSelf(org.eclipse.sirius.components.diagrams.description.EdgeDescription.GRAPHICAL_EDGE_SOURCE,
                                org.eclipse.sirius.components.diagrams.description.EdgeDescription.GRAPHICAL_EDGE_TARGET,
                                org.eclipse.sirius.components.diagrams.description.DiagramDescription.CACHE,
                                IEditingContext.EDITING_CONTEXT)
                        // Needs this to avoid instantiation on inheriting concept
                        + "and self.oclIsTypeOf(" + domainType + ")")
                .semanticCandidatesExpression("aql:self.getAllReachable(" + domainType + ")")
                .sourceExpression(ServiceMethod.of0(ModelQueryAQLService::getConnectorSource).aqlSelf())
                .style(this.createEdgeStyle())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .targetExpression(ServiceMethod.of0(ModelQueryAQLService::getConnectorTarget).aqlSelf())
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
        var callEditService = this.viewBuilderHelper.newChangeContext()
                .expression(ServiceMethod.of1(DiagramMutationAQLService::editEdgeCenterLabel).aqlSelf("newLabel"));

        return this.diagramBuilderHelper.newLabelEditTool()
                .name("Edit")
                .initialDirectEditLabelExpression(ServiceMethod.of0(DiagramQueryAQLService::getDefaultInitialDirectEditLabel).aqlSelf())
                .body(callEditService.build())
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
}
