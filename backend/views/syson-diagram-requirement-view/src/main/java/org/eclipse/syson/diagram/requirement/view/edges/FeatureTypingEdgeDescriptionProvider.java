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
package org.eclipse.syson.diagram.requirement.view.edges;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.edges.AbstractFeatureTypingEdgeDescriptionProvider;
import org.eclipse.syson.diagram.requirement.view.RVDescriptionNameGenerator;
import org.eclipse.syson.diagram.requirement.view.RequirementViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.FeatureTyping;

/**
 * Used to create the {@link FeatureTyping} edge description in the Requirement View diagram.
 *
 * @author Jerome Gout
 */
public class FeatureTypingEdgeDescriptionProvider extends AbstractFeatureTypingEdgeDescriptionProvider {

    public FeatureTypingEdgeDescriptionProvider(IColorProvider colorProvider) {
        super(colorProvider);
    }

    @Override
    protected String getName() {
        return "RV Edge FeatureTyping";
    }

    @Override
    protected List<NodeDescription> getSourceNodes(IViewDiagramElementFinder cache) {
        var nameGenerator = new RVDescriptionNameGenerator();
        var sources = new ArrayList<NodeDescription>();
        RequirementViewDiagramDescriptionProvider.USAGES.forEach(usage -> {
            cache.getNodeDescription(nameGenerator.getNodeName(usage)).ifPresent(sources::add);
        });
        return sources;
    }

    @Override
    protected List<NodeDescription> getTargetNodes(IViewDiagramElementFinder cache) {
        var nameGenerator = new RVDescriptionNameGenerator();
        var targets = new ArrayList<NodeDescription>();
        RequirementViewDiagramDescriptionProvider.DEFINITIONS.forEach(usage -> {
            cache.getNodeDescription(nameGenerator.getNodeName(usage)).ifPresent(targets::add);
        });
        return targets;
    }
}
