/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
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
import org.eclipse.sirius.components.view.diagram.LabelEditTool;
import org.eclipse.sirius.components.view.diagram.LineStyle;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.diagram.common.view.nodes.DoneActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.StartActionNodeDescriptionProvider;
import org.eclipse.syson.sysml.BindingConnectorAsUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the {@link BindingConnectorAsUsage} edge description.
 *
 * @author adieumegard
 */
public class TransitionEdgeDescriptionProvider extends AbstractEdgeDescriptionProvider {

    public static final String NAME = "Edge TransitionUsage";

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public TransitionEdgeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    protected List<NodeDescription> getSourceNodes(IViewDiagramElementFinder cache) {
        return this.getAllActionOrStateUsage(cache, false, true);
    }

    protected List<NodeDescription> getTargetNodes(IViewDiagramElementFinder cache) {
        return this.getAllActionOrStateUsage(cache, true, false);
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        cache.getEdgeDescription(this.getEdgeDescriptionName()).ifPresent(ed -> {
            diagramDescription.getEdgeDescriptions().add(ed);
            ed.getSourceDescriptions().addAll(this.getSourceNodes(cache));
            ed.getTargetDescriptions().addAll(this.getTargetNodes(cache));
            ed.setPalette(this.createEdgePalette());
        });
    }

    @Override
    public EdgeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getTransitionUsage());
        return this.getDiagramBuilderHelper().newEdgeDescription()
                .domainType(domainType)
                .isDomainBasedEdge(true)
                .centerLabelExpression(AQLUtils.getSelfServiceCallExpression("getTransitionLabel"))
                .name(this.getEdgeDescriptionName())
                .semanticCandidatesExpression(AQLUtils.getSelfServiceCallExpression("getAllReachable", domainType))
                .sourceExpression(AQLConstants.AQL_SELF + ".source")
                .style(this.createDefaultEdgeStyle())
                .conditionalStyles(this.createStateConditionalStyle())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .targetExpression(AQLConstants.AQL_SELF + ".target")
                .build();
    }

    private ConditionalEdgeStyle createStateConditionalStyle() {
        return this.diagramBuilderHelper.newConditionalEdgeStyle()
                .borderSize(0)
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_EDGE_COLOR))
                .edgeWidth(1)
                .lineStyle(LineStyle.SOLID)
                .sourceArrowStyle(ArrowStyle.NONE)
                .targetArrowStyle(ArrowStyle.INPUT_ARROW)
                .condition(AQLUtils.getSelfServiceCallExpression("isTransitionUsageForState"))
                .build();
    }

    protected String getEdgeDescriptionName() {
        return this.getDescriptionNameGenerator().getDiagramPrefix() + NAME;
    }

    @Override
    protected ChangeContextBuilder getSourceReconnectToolBody() {
        return this.getViewBuilderHelper().newChangeContext()
                .expression(AQLUtils.getServiceCallExpression(AQLConstants.EDGE_SEMANTIC_ELEMENT, "reconnectSourceTransitionEdge", AQLConstants.SEMANTIC_RECONNECTION_TARGET));
    }

    @Override
    protected ChangeContextBuilder getTargetReconnectToolBody() {
        return this.getViewBuilderHelper().newChangeContext()
                .expression(AQLUtils.getServiceCallExpression(AQLConstants.EDGE_SEMANTIC_ELEMENT, "reconnectTargetTransitionEdge", AQLConstants.SEMANTIC_RECONNECTION_TARGET));
    }

    @Override
    protected String getSourceReconnectToolPreconditionExpression() {
        return AQLUtils.getServiceCallExpression(AQLConstants.SEMANTIC_OTHER_END, "checkTransitionEdgeTarget", AQLConstants.SEMANTIC_RECONNECTION_TARGET);
    }

    @Override
    protected String getTargetReconnectToolPreconditionExpression() {
        return this.getSourceReconnectToolPreconditionExpression();
    }

    /**
     * Label edit tool setting the attributes of an {@linkplain EOperation} based on the provided input. Will allow to
     * set the {@linkplain EOperation} dataType, parameters, and cardinality values. <br/>
     * Relies on {@code org.eclipse.emf.ecoretools.design.service.DesignServices.performEdit(EAttribute, String)} or
     * {@code org.eclipse.emf.ecoretools.design.service.DesignServices.performEdit(EOperation, String)}.
     *
     * @return The LabelEditTool for the TransitionEdge
     */
    @Override
    protected LabelEditTool getEdgeEditTool() {
        var changeContext = this.getViewBuilderHelper().newChangeContext()
                .expression(AQLUtils.getSelfServiceCallExpression("directEditTransitionEdgeLabel", "newLabel"))
                .build();

        return this.getDiagramBuilderHelper().newLabelEditTool()
                .name("Edit TransitionUsage Label")
                .initialDirectEditLabelExpression(AQLUtils.getSelfServiceCallExpression("getTransitionLabel", "false"))
                .body(changeContext)
                .build();
    }

    private EdgeStyle createDefaultEdgeStyle() {
        return this.diagramBuilderHelper.newEdgeStyle()
                .borderSize(0)
                .color(this.colorProvider.getColor(ViewConstants.DEFAULT_EDGE_COLOR))
                .edgeWidth(1)
                .lineStyle(LineStyle.DASH)
                .sourceArrowStyle(ArrowStyle.NONE)
                .targetArrowStyle(ArrowStyle.INPUT_ARROW)
                .build();
    }

    protected ViewBuilders getViewBuilderHelper() {
        return this.viewBuilderHelper;
    }

    protected DiagramBuilders getDiagramBuilderHelper() {
        return this.diagramBuilderHelper;
    }

    protected IDescriptionNameGenerator getDescriptionNameGenerator() {
        return this.descriptionNameGenerator;
    }

    private List<NodeDescription> getAllActionOrStateUsage(IViewDiagramElementFinder cache, boolean excudeStart, boolean excludeDone) {
        return cache.getNodeDescriptions().stream().filter(this::isActionOrStateUsage)
                .filter(n -> !excudeStart || !this.isStartNode(n))
                .filter(n -> !excludeDone || !this.isDoneNode(n))
                .toList();
    }

    private boolean isStartNode(NodeDescription n) {
        return this.getDescriptionNameGenerator().getNodeName(StartActionNodeDescriptionProvider.START_ACTION_NAME).equals(n.getName());
    }

    private boolean isDoneNode(NodeDescription n) {
        return this.getDescriptionNameGenerator().getNodeName(DoneActionNodeDescriptionProvider.DONE_ACTION_NAME).equals(n.getName());
    }

    private boolean isActionOrStateUsage(NodeDescription nodeDescription) {
        EClass targetEClass = SysMLMetamodelHelper.toEClass(nodeDescription.getDomainType());
        return targetEClass != null && (SysmlPackage.eINSTANCE.getActionUsage().isSuperTypeOf(targetEClass) || SysmlPackage.eINSTANCE.getStateUsage().isSuperTypeOf(targetEClass))
                && !(nodeDescription.getStyle() instanceof IconLabelNodeStyleDescription);
    }
}
