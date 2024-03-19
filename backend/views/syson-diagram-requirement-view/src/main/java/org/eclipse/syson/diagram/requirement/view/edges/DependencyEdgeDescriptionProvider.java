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
import org.eclipse.syson.diagram.common.view.edges.AbstractDependencyEdgeDescriptionProvider;
import org.eclipse.syson.diagram.requirement.view.RVDescriptionNameGenerator;
import org.eclipse.syson.diagram.requirement.view.RequirementViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.Dependency;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Used to create the {@link Dependency} edge description inside the Requirement View diagram.
 *
 * @author Jerome Gout
 */
public class DependencyEdgeDescriptionProvider extends AbstractDependencyEdgeDescriptionProvider {

    public DependencyEdgeDescriptionProvider(IColorProvider colorProvider) {
        super(colorProvider);
    }

    @Override
    protected String getName() {
        return "RV Edge Dependency";
    }

    private List<NodeDescription> getSourceAndTarget(IViewDiagramElementFinder cache) {
        var nameGenerator = new RVDescriptionNameGenerator();
        var sourcesAndTargets = new ArrayList<NodeDescription>();

        RequirementViewDiagramDescriptionProvider.DEFINITIONS.forEach(definition -> {
            cache.getNodeDescription(nameGenerator.getNodeName(definition)).ifPresent(sourcesAndTargets::add);
        });
        RequirementViewDiagramDescriptionProvider.USAGES.forEach(definition -> {
            cache.getNodeDescription(nameGenerator.getNodeName(definition)).ifPresent(sourcesAndTargets::add);
        });
        cache.getNodeDescription(nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage())).ifPresent(sourcesAndTargets::add);
        return sourcesAndTargets;
    }

    @Override
    protected List<NodeDescription> getSourceNodes(IViewDiagramElementFinder cache) {
        return this.getSourceAndTarget(cache);
    }

    @Override
    protected List<NodeDescription> getTargetNodes(IViewDiagramElementFinder cache) {
        return this.getSourceAndTarget(cache);
    }
}
