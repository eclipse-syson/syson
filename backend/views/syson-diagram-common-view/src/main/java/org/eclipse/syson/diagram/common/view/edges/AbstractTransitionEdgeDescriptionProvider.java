/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.diagram.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.view.ChangeContextBuilder;
import org.eclipse.sirius.components.view.builder.generated.view.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.ArrowStyle;
import org.eclipse.sirius.components.view.diagram.ConditionalEdgeStyle;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.EdgeDescription;
import org.eclipse.sirius.components.view.diagram.EdgeStyle;
import org.eclipse.sirius.components.view.diagram.IconLabelNodeStyleDescription;
import org.eclipse.sirius.components.view.diagram.LineStyle;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.diagram.common.view.nodes.DoneActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.StartActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.services.ViewEdgeService;
import org.eclipse.syson.diagram.services.aql.DiagramQueryAQLService;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the {@link TransitionUsage} edge description.
 *
 * @author adieumegard
 */
public abstract class AbstractTransitionEdgeDescriptionProvider extends AbstractEdgeDescriptionProvider {

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public AbstractTransitionEdgeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    @Override
    public EdgeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getTransitionUsage());
        return this.getDiagramBuilderHelper().newEdgeDescription()
                .domainType(domainType)
                .isDomainBasedEdge(true)
                .centerLabelExpression(ServiceMethod.of0(DiagramQueryAQLService::getTransitionLabel).aqlSelf())
                .name(this.getEdgeDescriptionName())
                .preconditionExpression(ServiceMethod.of2(ViewEdgeService::isInSameGraphicalContainer).aql(org.eclipse.sirius.components.diagrams.description.EdgeDescription.GRAPHICAL_EDGE_SOURCE,
                        org.eclipse.sirius.components.diagrams.description.EdgeDescription.GRAPHICAL_EDGE_TARGET, org.eclipse.sirius.components.diagrams.description.DiagramDescription.CACHE))
                .semanticCandidatesExpression(AQLUtils.getSelfServiceCallExpression("getAllReachable", domainType))
                .sourceExpression(AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getTransitionUsage_Source().getName())
                .style(this.createDefaultEdgeStyle())
                .conditionalStyles(this.createStateConditionalStyle())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .targetExpression(AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getTransitionUsage_Target().getName())
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        cache.getEdgeDescription(this.getEdgeDescriptionName()).ifPresent(ed -> {
            diagramDescription.getEdgeDescriptions().add(ed);
            ed.getSourceDescriptions().addAll(this.getSourceNodes(cache));
            ed.getTargetDescriptions().addAll(this.getTargetNodes(cache));
            ed.setPalette(this.createEdgePalette(cache));
        });
    }

    @Override
    protected ChangeContextBuilder getSourceReconnectToolBody() {
        return this.getViewBuilderHelper().newChangeContext()
                .expression(ServiceMethod.of1(ViewEdgeService::reconnectSourceTransitionEdge).aql(AQLConstants.EDGE_SEMANTIC_ELEMENT, AQLConstants.SEMANTIC_RECONNECTION_TARGET));
    }

    @Override
    protected ChangeContextBuilder getTargetReconnectToolBody() {
        return this.getViewBuilderHelper().newChangeContext()
                .expression(ServiceMethod.of1(ViewEdgeService::reconnectTargetTransitionEdge).aql(AQLConstants.EDGE_SEMANTIC_ELEMENT, AQLConstants.SEMANTIC_RECONNECTION_TARGET));
    }

    @Override
    protected String getSourceReconnectToolPreconditionExpression() {
        return ServiceMethod.of1(ViewEdgeService::checkTransitionEdgeTarget).aql(AQLConstants.SEMANTIC_OTHER_END, AQLConstants.SEMANTIC_RECONNECTION_TARGET);
    }

    @Override
    protected String getTargetReconnectToolPreconditionExpression() {
        return this.getSourceReconnectToolPreconditionExpression();
    }

    /**
     * Implementers should provide the list of {@link NodeTool} (without ToolSection) for this {@link EdgeDescription}.
     *
     * @param cache
     *            the cache used to retrieve node descriptions.
     * @return the list of {@link NodeTool} of this edge.
     */
    @Override
    protected List<NodeTool> getToolsWithoutSection(IViewDiagramElementFinder cache) {
        return new ArrayList<>();
    };

    protected ViewBuilders getViewBuilderHelper() {
        return this.viewBuilderHelper;
    }

    protected DiagramBuilders getDiagramBuilderHelper() {
        return this.diagramBuilderHelper;
    }

    protected IDescriptionNameGenerator getDescriptionNameGenerator() {
        return this.descriptionNameGenerator;
    }

    protected String getEdgeDescriptionName() {
        return this.descriptionNameGenerator.getEdgeName(SysmlPackage.eINSTANCE.getTransitionUsage());
    }

    protected List<NodeDescription> getSourceNodes(IViewDiagramElementFinder cache) {
        return this.getAllActionOrStateUsage(cache, false, true);
    }

    protected List<NodeDescription> getTargetNodes(IViewDiagramElementFinder cache) {
        return this.getAllActionOrStateUsage(cache, true, false);
    }

    protected EdgeStyle createDefaultEdgeStyle() {
        return this.diagramBuilderHelper.newEdgeStyle()
                .borderSize(0)
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_EDGE_COLOR))
                .edgeWidth(1)
                .lineStyle(LineStyle.DASH)
                .sourceArrowStyle(ArrowStyle.NONE)
                .targetArrowStyle(ArrowStyle.INPUT_ARROW)
                .build();
    }

    protected ConditionalEdgeStyle createStateConditionalStyle() {
        return this.diagramBuilderHelper.newConditionalEdgeStyle()
                .borderSize(0)
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_EDGE_COLOR))
                .edgeWidth(1)
                .lineStyle(LineStyle.SOLID)
                .sourceArrowStyle(ArrowStyle.NONE)
                .targetArrowStyle(ArrowStyle.INPUT_ARROW)
                .condition(ServiceMethod.of0(ViewEdgeService::isTransitionUsageForState).aqlSelf())
                .build();
    }

    protected List<NodeDescription> getAllActionOrStateUsage(IViewDiagramElementFinder cache, boolean excudeStart, boolean excludeDone) {
        return cache.getNodeDescriptions().stream().filter(this::isActionOrStateUsage)
                .filter(n -> !excudeStart || !this.isStartNode(n))
                .filter(n -> !excludeDone || !this.isDoneNode(n))
                .toList();
    }

    protected boolean isStartNode(NodeDescription n) {
        return Objects.equals(this.getDescriptionNameGenerator().getNodeName(StartActionNodeDescriptionProvider.START_ACTION_NAME), n.getName());
    }

    protected boolean isDoneNode(NodeDescription n) {
        return Objects.equals(this.getDescriptionNameGenerator().getNodeName(DoneActionNodeDescriptionProvider.DONE_ACTION_NAME), n.getName());
    }

    protected boolean isActionOrStateUsage(NodeDescription nodeDescription) {
        EClass targetEClass = SysMLMetamodelHelper.toEClass(nodeDescription.getDomainType());
        return targetEClass != null && (SysmlPackage.eINSTANCE.getActionUsage().isSuperTypeOf(targetEClass) || SysmlPackage.eINSTANCE.getStateUsage().isSuperTypeOf(targetEClass))
                && !(nodeDescription.getStyle() instanceof IconLabelNodeStyleDescription);
    }
}
