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
package org.eclipse.syson.diagram.interconnection.view.edges;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.edges.AbstractSuccessionEdgeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.DecisionActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.DoneActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.ForkActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.JoinActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.MergeActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.StartActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.services.description.ReferencingPerformActionUsageNodeDescriptionService;
import org.eclipse.syson.diagram.interconnection.view.IVDescriptionNameGenerator;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Used to create a Succession Edge provider in General View diagram.
 *
 * @author Jerome Gout
 */
public class SuccessionEdgeDescriptionProvider extends AbstractSuccessionEdgeDescriptionProvider {

    public SuccessionEdgeDescriptionProvider(IColorProvider colorProvider) {
        super(colorProvider, new IVDescriptionNameGenerator());
    }

    @Override
    protected List<NodeDescription> getSourceNodes(IViewDiagramElementFinder cache) {
        List<NodeDescription> sources = new ArrayList<>();
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage())).ifPresent(sources::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getAcceptActionUsage())).ifPresent(sources::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPerformActionUsage())).ifPresent(sources::add);
        // the start node can be the source of a succession
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(StartActionNodeDescriptionProvider.START_ACTION_NAME)).ifPresent(sources::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(JoinActionNodeDescriptionProvider.JOIN_ACTION_NAME)).ifPresent(sources::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(ForkActionNodeDescriptionProvider.FORK_ACTION_NAME)).ifPresent(sources::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(MergeActionNodeDescriptionProvider.MERGE_ACTION_NAME)).ifPresent(sources::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(DecisionActionNodeDescriptionProvider.DECISION_ACTION_NAME)).ifPresent(sources::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(ReferencingPerformActionUsageNodeDescriptionService.REFERENCING_PERFORM_ACTION_NAME)).ifPresent(sources::add);
        return sources;
    }

    @Override
    protected List<NodeDescription> getTargetNodes(IViewDiagramElementFinder cache) {
        List<NodeDescription> targets = new ArrayList<>();
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage())).ifPresent(targets::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getAcceptActionUsage())).ifPresent(targets::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPerformActionUsage())).ifPresent(targets::add);
        // the done node can be the target of a succession
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(DoneActionNodeDescriptionProvider.DONE_ACTION_NAME)).ifPresent(targets::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(JoinActionNodeDescriptionProvider.JOIN_ACTION_NAME)).ifPresent(targets::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(ForkActionNodeDescriptionProvider.FORK_ACTION_NAME)).ifPresent(targets::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(MergeActionNodeDescriptionProvider.MERGE_ACTION_NAME)).ifPresent(targets::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(DecisionActionNodeDescriptionProvider.DECISION_ACTION_NAME)).ifPresent(targets::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(ReferencingPerformActionUsageNodeDescriptionService.REFERENCING_PERFORM_ACTION_NAME)).ifPresent(targets::add);
        return targets;
    }
}
