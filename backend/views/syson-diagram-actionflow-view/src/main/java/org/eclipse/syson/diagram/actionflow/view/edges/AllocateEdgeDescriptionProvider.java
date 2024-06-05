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
package org.eclipse.syson.diagram.actionflow.view.edges;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.actionflow.view.AFVDescriptionNameGenerator;
import org.eclipse.syson.diagram.actionflow.view.ActionFlowViewDiagramDescriptionProvider;
import org.eclipse.syson.diagram.common.view.edges.AbstractAllocateEdgeDescriptionProvider;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to create the Allocate edge description inside the Action Flow View diagram.
 *
 * @author Jerome Gout
 */
public class AllocateEdgeDescriptionProvider extends AbstractAllocateEdgeDescriptionProvider {

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public AllocateEdgeDescriptionProvider(IColorProvider colorProvider) {
        super(colorProvider);
        this.descriptionNameGenerator = new AFVDescriptionNameGenerator();
    }

    @Override
    protected String getName() {
        return this.descriptionNameGenerator.getEdgeName("Allocate");
    }

    @Override
    protected List<NodeDescription> getSourceNodes(IViewDiagramElementFinder cache) {
        return this.getSourceAndTarget(cache);
    }

    @Override
    protected List<NodeDescription> getTargetNodes(IViewDiagramElementFinder cache) {
        return this.getSourceAndTarget(cache);
    }

    private List<NodeDescription> getSourceAndTarget(IViewDiagramElementFinder cache) {
        var sourcesAndTargets = new ArrayList<NodeDescription>();
        ActionFlowViewDiagramDescriptionProvider.USAGES.forEach(usage -> {
            cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(usage)).ifPresent(sourcesAndTargets::add);
        });
        return sourcesAndTargets;
    }

}
