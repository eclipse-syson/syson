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
package org.eclipse.syson.diagram.general.view.edges;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.edges.AbstractSuccessionEdgeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.DoneActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.StartActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;

/**
 * Used to create a Succession Edge provider in General View diagram.
 *
 * @author Jerome Gout
 */
public class SuccessionEdgeDescriptionProvider extends AbstractSuccessionEdgeDescriptionProvider {

    public SuccessionEdgeDescriptionProvider(IColorProvider colorProvider) {
        super(colorProvider, new GVDescriptionNameGenerator());
    }

    private List<NodeDescription> getAllUsages(IViewDiagramElementFinder cache) {
        var sourcesAndTargets = new ArrayList<NodeDescription>();

        GeneralViewDiagramDescriptionProvider.USAGES.forEach(usage -> {
            cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(usage)).ifPresent(sourcesAndTargets::add);
        });

        return sourcesAndTargets;
    }

    @Override
    protected List<NodeDescription> getSourceNodes(IViewDiagramElementFinder cache) {
        var sources = this.getAllUsages(cache);
        // the start node can be the source of a succession
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(StartActionNodeDescriptionProvider.START_ACTION_NAME)).ifPresent(sources::add);
        return sources;
    }

    @Override
    protected List<NodeDescription> getTargetNodes(IViewDiagramElementFinder cache) {
        var targets = this.getAllUsages(cache);
        // the done node can be the target of a succession
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(DoneActionNodeDescriptionProvider.DONE_ACTION_NAME)).ifPresent(targets::add);
        return targets;
    }
}
