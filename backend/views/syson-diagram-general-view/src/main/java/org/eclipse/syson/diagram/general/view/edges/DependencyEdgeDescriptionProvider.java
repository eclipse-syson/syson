/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.diagram.common.view.edges.AbstractDependencyEdgeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.services.description.ReferencingPerformActionUsageNodeDescriptionService;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.diagram.general.view.services.GeneralViewNodeToolsWithoutSectionSwitch;
import org.eclipse.syson.sysml.Dependency;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to create the {@link Dependency} edge description inside the General View diagram.
 *
 * @author arichard
 */
public class DependencyEdgeDescriptionProvider extends AbstractDependencyEdgeDescriptionProvider {

    public DependencyEdgeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider, descriptionNameGenerator);
    }

    @Override
    protected String getName() {
        return "GV Edge Dependency";
    }

    @Override
    protected List<NodeDescription> getSourceNodes(IViewDiagramElementFinder cache) {
        return this.getSourceAndTarget(cache);
    }

    @Override
    protected List<NodeDescription> getTargetNodes(IViewDiagramElementFinder cache) {
        return this.getSourceAndTarget(cache);
    }

    @Override
    protected List<NodeTool> getToolsWithoutSection(IViewDiagramElementFinder cache) {
        GeneralViewNodeToolsWithoutSectionSwitch toolsWithoutSectionSwitch = new GeneralViewNodeToolsWithoutSectionSwitch(cache, this.getAllNodeDescriptions(cache));
        return toolsWithoutSectionSwitch.doSwitch(SysmlPackage.eINSTANCE.getDependency());
    }

    protected List<NodeDescription> getAllNodeDescriptions(IViewDiagramElementFinder cache) {
        var allNodes = new ArrayList<NodeDescription>();
        GeneralViewDiagramDescriptionProvider.DEFINITIONS.forEach(definition -> cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(definition)).ifPresent(allNodes::add));
        GeneralViewDiagramDescriptionProvider.USAGES.forEach(usage -> cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(usage)).ifPresent(allNodes::add));
        GeneralViewDiagramDescriptionProvider.ANNOTATINGS.forEach(usage -> cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(usage)).ifPresent(allNodes::add));
        return allNodes;
    }

    private List<NodeDescription> getSourceAndTarget(IViewDiagramElementFinder cache) {
        var nameGenerator = new GVDescriptionNameGenerator();
        var sourcesAndTargets = new ArrayList<NodeDescription>();
        GeneralViewDiagramDescriptionProvider.DEFINITIONS.forEach(definition -> {
            cache.getNodeDescription(nameGenerator.getNodeName(definition)).ifPresent(sourcesAndTargets::add);
        });
        GeneralViewDiagramDescriptionProvider.USAGES.forEach(definition -> {
            cache.getNodeDescription(nameGenerator.getNodeName(definition)).ifPresent(sourcesAndTargets::add);
        });
        cache.getNodeDescription(nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage())).ifPresent(sourcesAndTargets::add);
        // since referencing perform action is not part of regular usages, we should add it manually
        cache.getNodeDescription(nameGenerator.getNodeName(ReferencingPerformActionUsageNodeDescriptionService.REFERENCING_PERFORM_ACTION_NAME)).ifPresent(sourcesAndTargets::add);
        return sourcesAndTargets;
    }
}
