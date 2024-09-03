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
package org.eclipse.syson.diagram.general.view.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.nodes.AbstractPackageNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.tools.ToolSectionDescription;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Used to create the package node description in the General View diagram.
 *
 * @author arichard
 */
public class PackageNodeDescriptionProvider extends AbstractPackageNodeDescriptionProvider {

    public PackageNodeDescriptionProvider(IColorProvider colorProvider) {
        super(colorProvider, new GVDescriptionNameGenerator());
    }

    @Override
    protected List<NodeDescription> getReusedChildren(IViewDiagramElementFinder cache) {
        var reusedChildren = new ArrayList<NodeDescription>();

        GeneralViewDiagramDescriptionProvider.DEFINITIONS.forEach(definition -> cache.getNodeDescription(this.nameGenerator.getNodeName(definition)).ifPresent(reusedChildren::add));
        GeneralViewDiagramDescriptionProvider.USAGES.forEach(usage -> cache.getNodeDescription(this.nameGenerator.getNodeName(usage)).ifPresent(reusedChildren::add));
        GeneralViewDiagramDescriptionProvider.ANNOTATINGS.forEach(annotating -> cache.getNodeDescription(this.nameGenerator.getNodeName(annotating)).ifPresent(reusedChildren::add));
        cache.getNodeDescription(this.nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage())).ifPresent(reusedChildren::add);
        return reusedChildren;
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        var droppableNodes = new ArrayList<NodeDescription>();

        GeneralViewDiagramDescriptionProvider.DEFINITIONS.forEach(definition -> cache.getNodeDescription(this.nameGenerator.getNodeName(definition)).ifPresent(droppableNodes::add));
        GeneralViewDiagramDescriptionProvider.USAGES.forEach(usage -> cache.getNodeDescription(this.nameGenerator.getNodeName(usage)).ifPresent(droppableNodes::add));
        GeneralViewDiagramDescriptionProvider.ANNOTATINGS.forEach(annotating -> cache.getNodeDescription(this.nameGenerator.getNodeName(annotating)).ifPresent(droppableNodes::add));
        cache.getNodeDescription(this.nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage())).ifPresent(droppableNodes::add);

        return droppableNodes;
    }

    @Override
    protected List<NodeDescription> getAllNodeDescriptions(IViewDiagramElementFinder cache) {
        var allNodes = new ArrayList<NodeDescription>();

        GeneralViewDiagramDescriptionProvider.DEFINITIONS.forEach(definition -> cache.getNodeDescription(this.nameGenerator.getNodeName(definition)).ifPresent(allNodes::add));
        GeneralViewDiagramDescriptionProvider.USAGES.forEach(usage -> cache.getNodeDescription(this.nameGenerator.getNodeName(usage)).ifPresent(allNodes::add));
        cache.getNodeDescription(this.nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage())).ifPresent(allNodes::add);
        return allNodes;
    }

    @Override
    protected List<ToolSectionDescription> getToolSections() {
        return GeneralViewDiagramDescriptionProvider.TOOL_SECTIONS;
    }
}
