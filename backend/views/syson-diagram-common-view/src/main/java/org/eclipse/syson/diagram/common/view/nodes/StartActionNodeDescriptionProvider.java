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
package org.eclipse.syson.diagram.common.view.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramService;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.diagrams.description.EdgeDescription;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.EdgeTool;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.sirius.components.view.diagram.UserResizableDirection;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Used to create the starting node description of an Action.
 *
 * @author Jerome Gout
 */
public class StartActionNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    public static final String START_ACTION_NAME = "StartAction";

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public StartActionNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    @Override
    public NodeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getActionUsage());
        return this.diagramBuilderHelper.newNodeDescription()
                .collapsible(false)
                .domainType(domainType)
                .defaultWidthExpression("28")
                .defaultHeightExpression("28")
                .name(this.descriptionNameGenerator.getNodeName(START_ACTION_NAME))
                .semanticCandidatesExpression(AQLUtils.getSelfServiceCallExpression("retrieveStandardStartAction"))
                .style(this.createImageNodeStyleDescription("images/start_action.svg"))
                .userResizable(UserResizableDirection.NONE)
                .synchronizationPolicy(SynchronizationPolicy.UNSYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        // this nodeDescription has not been added to the diagramDescription children but to the fakeNodeDescription
        // children instead
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(START_ACTION_NAME)).ifPresent(nodeDescription -> {
            nodeDescription.setPalette(this.createNodePalette(cache));
        });
    }

    private NodePalette createNodePalette(IViewDiagramElementFinder cache) {
        var edgeTools = new ArrayList<EdgeTool>();
        edgeTools.addAll(this.getEdgeTools(cache));

        return this.diagramBuilderHelper.newNodePalette()
                .edgeTools(edgeTools.toArray(EdgeTool[]::new))
                .quickAccessTools(this.getDeleteFromDiagramTool())
                .toolSections(this.defaultToolsFactory.createDefaultHideRevealNodeToolSection())
                .build();
    }

    private List<EdgeTool> getEdgeTools(IViewDiagramElementFinder cache) {
        var targetElementDescriptions = this.getStartTargetDescriptions(cache);

        var builder = this.diagramBuilderHelper.newEdgeTool();
        var params = List.of(EdgeDescription.SEMANTIC_EDGE_TARGET, EdgeDescription.EDGE_SOURCE, EdgeDescription.EDGE_TARGET, IEditingContext.EDITING_CONTEXT, IDiagramService.DIAGRAM_SERVICES);
        var body = this.viewBuilderHelper.newChangeContext()
                .expression(AQLUtils.getServiceCallExpression(EdgeDescription.SEMANTIC_EDGE_SOURCE, "createSuccessionEdge", params));

        var createStartEdgeTool = builder.name(this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getSuccession()))
                .iconURLsExpression("/icons/full/obj16/Succession.svg")
                .body(body.build())
                .targetElementDescriptions(targetElementDescriptions.toArray(NodeDescription[]::new))
                .build();

        return List.of(createStartEdgeTool);
    }

    private List<NodeDescription> getStartTargetDescriptions(IViewDiagramElementFinder cache) {
        var targets = new ArrayList<NodeDescription>();
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage())).ifPresent(targets::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getAcceptActionUsage())).ifPresent(targets::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getAssignmentActionUsage())).ifPresent(targets::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPerformActionUsage())).ifPresent(targets::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(JoinActionNodeDescriptionProvider.JOIN_ACTION_NAME)).ifPresent(targets::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(ForkActionNodeDescriptionProvider.FORK_ACTION_NAME)).ifPresent(targets::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(MergeActionNodeDescriptionProvider.MERGE_ACTION_NAME)).ifPresent(targets::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(DecisionActionNodeDescriptionProvider.DECISION_ACTION_NAME)).ifPresent(targets::add);
        return targets;
    }
}
