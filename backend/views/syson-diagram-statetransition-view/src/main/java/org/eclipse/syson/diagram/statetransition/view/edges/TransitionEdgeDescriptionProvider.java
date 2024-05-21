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
package org.eclipse.syson.diagram.statetransition.view.edges;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EOperation;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.generated.ChangeContextBuilder;
import org.eclipse.sirius.components.view.builder.generated.DiagramBuilders;
import org.eclipse.sirius.components.view.builder.generated.ViewBuilders;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.ArrowStyle;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.EdgeDescription;
import org.eclipse.sirius.components.view.diagram.EdgeStyle;
import org.eclipse.sirius.components.view.diagram.LabelEditTool;
import org.eclipse.sirius.components.view.diagram.LineStyle;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.syson.diagram.common.view.edges.AbstractEdgeDescriptionProvider;
import org.eclipse.syson.diagram.statetransition.view.StateTransitionViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.BindingConnectorAsUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;
import org.eclipse.syson.util.ViewConstants;

/**
 * Used to create the {@link BindingConnectorAsUsage} edge description.
 *
 * @author adieumegard
 */
public class TransitionEdgeDescriptionProvider extends AbstractEdgeDescriptionProvider {

    public static final String NAME = "ST Edge TransitionUsage";

    private final ViewBuilders viewBuilderHelper = new ViewBuilders();

    private final DiagramBuilders diagramBuilderHelper = new DiagramBuilders();

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public TransitionEdgeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    @Override
    public EdgeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getTransitionUsage());
        return this.diagramBuilderHelper.newEdgeDescription()
                .domainType(domainType)
                .isDomainBasedEdge(true)
                .centerLabelExpression("aql:self.getTransitionLabel()")
                .name(NAME)
                .semanticCandidatesExpression("aql:self.getAllReachable(" + domainType + ")")
                .sourceNodesExpression("aql:self.source")
                .style(this.createEdgeStyle())
                .synchronizationPolicy(SynchronizationPolicy.SYNCHRONIZED)
                .targetNodesExpression("aql:self.target")
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        var optEdgeDescription = cache.getEdgeDescription(NAME);
        List<NodeDescription> optNodeDescriptions = new ArrayList<>();
        StateTransitionViewDiagramDescriptionProvider.DEFINITIONS.forEach(eClass -> {
            cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(eClass)).ifPresent(optNodeDescriptions::add);
        });
        StateTransitionViewDiagramDescriptionProvider.USAGES.forEach(eClass -> {
            cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(eClass)).ifPresent(optNodeDescriptions::add);
        });

        if (optEdgeDescription.isPresent() && !optNodeDescriptions.isEmpty()) {
            EdgeDescription edgeDescription = optEdgeDescription.get();
            diagramDescription.getEdgeDescriptions().add(edgeDescription);
            edgeDescription.getSourceNodeDescriptions().addAll(optNodeDescriptions);
            edgeDescription.getTargetNodeDescriptions().addAll(optNodeDescriptions);
            edgeDescription.setPalette(this.createEdgePalette());
        }
    }

    @Override
    protected ChangeContextBuilder getSourceReconnectToolBody() {
        return this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + AQLConstants.EDGE_SEMANTIC_ELEMENT + ".setTransitionSource(" + AQLConstants.SEMANTIC_RECONNECTION_TARGET + ")");
    }

    @Override
    protected ChangeContextBuilder getTargetReconnectToolBody() {
        return this.viewBuilderHelper.newChangeContext()
                .expression(AQLConstants.AQL + AQLConstants.EDGE_SEMANTIC_ELEMENT + ".setTransitionTarget(" + AQLConstants.SEMANTIC_RECONNECTION_TARGET + ")");
    }

    @Override
    protected String getSourceReconnectToolPreconditionExpression() {
        return AQLConstants.AQL + AQLConstants.SEMANTIC_OTHER_END + ".checkTransitionEdgeTarget(" + AQLConstants.SEMANTIC_RECONNECTION_TARGET + ")";
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
        var changeContext = this.viewBuilderHelper.newChangeContext()
                .expression("aql:self.editTransitionEdgeLabel(newLabel)")
                .build();

        return this.diagramBuilderHelper.newLabelEditTool()
                .name("Edit TransitionUsage Label")
                .body(changeContext)
                .build();
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
}
