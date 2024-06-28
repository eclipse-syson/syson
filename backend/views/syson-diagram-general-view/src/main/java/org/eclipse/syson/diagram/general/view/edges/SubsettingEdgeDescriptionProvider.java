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
package org.eclipse.syson.diagram.general.view.edges;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.edges.AbstractSubsettingEdgeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.services.description.ReferencingPerformActionUsageNodeDescriptionService;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.Subsetting;

/**
 * Used to create the {@link Subsetting} edge description in the General View diagram.
 *
 * @author arichard
 */
public class SubsettingEdgeDescriptionProvider extends AbstractSubsettingEdgeDescriptionProvider {

    public SubsettingEdgeDescriptionProvider(IColorProvider colorProvider) {
        super(colorProvider);
    }

    @Override
    protected String getName() {
        return "GV Edge Subsetting";
    }

    private List<NodeDescription> getSourceAndTargetNodes(IViewDiagramElementFinder cache) {
        var nameGenerator = new GVDescriptionNameGenerator();
        var sourcesAndTargets = new ArrayList<NodeDescription>();

        GeneralViewDiagramDescriptionProvider.USAGES.forEach(usage -> {
            cache.getNodeDescription(nameGenerator.getNodeName(usage)).ifPresent(sourcesAndTargets::add);
        });
        // since referencing perform action is not part of regular usages, we should add it manually
        cache.getNodeDescription(nameGenerator.getNodeName(ReferencingPerformActionUsageNodeDescriptionService.REFERENCING_PERFORM_ACTION_NAME)).ifPresent(sourcesAndTargets::add);
        return sourcesAndTargets;
    }

    @Override
    protected List<NodeDescription> getSourceNodes(IViewDiagramElementFinder cache) {
        return this.getSourceAndTargetNodes(cache);
    }

    @Override
    protected List<NodeDescription> getTargetNodes(IViewDiagramElementFinder cache) {
        return this.getSourceAndTargetNodes(cache);
    }
}
