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
package org.eclipse.syson.diagram.common.view.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.diagram.EdgeTool;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodePalette;
import org.eclipse.sirius.components.view.diagram.SynchronizationPolicy;
import org.eclipse.sirius.components.view.diagram.UserResizableDirection;
import org.eclipse.syson.diagram.common.view.DescriptionFinder;
import org.eclipse.syson.diagram.common.view.services.ViewEdgeToolService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.ServiceMethod;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Used to create the starting node description of a state.
 *
 * @author Jerome Gout
 */
public class StartStateNodeDescriptionProvider extends AbstractNodeDescriptionProvider {

    public static final String START_STATE_NAME = "StartState";

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public StartStateNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    @Override
    public NodeDescription create() {
        String domainType = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getStateUsage());
        return this.diagramBuilderHelper.newNodeDescription()
                .collapsible(false)
                .domainType(domainType)
                .defaultWidthExpression("28")
                .defaultHeightExpression("28")
                .name(this.descriptionNameGenerator.getNodeName(START_STATE_NAME))
                .semanticCandidatesExpression(ServiceMethod.of0(UtilService::retrieveStandardStartState).aqlSelf())
                .style(this.createImageNodeStyleDescription("images/start_action.svg"))
                .userResizable(UserResizableDirection.NONE)
                .synchronizationPolicy(SynchronizationPolicy.UNSYNCHRONIZED)
                .build();
    }

    @Override
    public void link(DiagramDescription diagramDescription, IViewDiagramElementFinder cache) {
        // this nodeDescription has not been added to the diagramDescription children but to the fakeNodeDescription
        // children instead
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(START_STATE_NAME)).ifPresent(nodeDescription -> {
            nodeDescription.setPalette(this.createNodePalette(cache));
        });
    }

    private NodePalette createNodePalette(IViewDiagramElementFinder cache) {
        var edgeTools = new ArrayList<EdgeTool>(this.getEdgeTools(cache));

        return this.diagramBuilderHelper.newNodePalette()
                .edgeTools(edgeTools.toArray(EdgeTool[]::new))
                .quickAccessTools(this.getDeleteFromDiagramTool())
                .toolSections(this.defaultToolsFactory.createDefaultHideRevealNodeToolSection())
                .build();
    }

    private List<EdgeTool> getEdgeTools(IViewDiagramElementFinder cache) {
        var targetElementDescriptions = this.getStartTargetDescriptions(cache);
        var edgeToolService = new ViewEdgeToolService(this.viewBuilderHelper, this.diagramBuilderHelper, cache.getNodeDescriptions(), this.descriptionNameGenerator);
        var connectionTool = edgeToolService.createConnectionUsageEdgeTool(new DescriptionFinder(this.descriptionNameGenerator).getConnectableNodeDescriptions(cache.getNodeDescriptions(), SysmlPackage.eINSTANCE.getStateUsage()));
        var transitionTool = edgeToolService.createTransitionUsageEdgeTool(SysmlPackage.eINSTANCE.getTransitionUsage(), targetElementDescriptions);
        return List.of(transitionTool, connectionTool);
    }

    private List<NodeDescription> getStartTargetDescriptions(IViewDiagramElementFinder cache) {
        var targets = new ArrayList<NodeDescription>();
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getStateUsage())).ifPresent(targets::add);
        return targets;
    }
}
