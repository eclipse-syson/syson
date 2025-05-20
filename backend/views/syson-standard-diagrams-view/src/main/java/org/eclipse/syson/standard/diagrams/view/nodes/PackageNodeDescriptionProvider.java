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
package org.eclipse.syson.standard.diagrams.view.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.diagram.common.view.nodes.AbstractPackageNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.tools.ExhibitStateWithReferenceNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.NamespaceImportNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ToolSectionDescription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.standard.diagrams.view.SDVDiagramDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Used to create the package node description in the General View diagram.
 *
 * @author arichard
 */
public class PackageNodeDescriptionProvider extends AbstractPackageNodeDescriptionProvider {

    public PackageNodeDescriptionProvider(IColorProvider colorProvider) {
        super(colorProvider, new SDVDescriptionNameGenerator());
    }

    @Override
    protected List<NodeDescription> getReusedChildren(IViewDiagramElementFinder cache) {
        var reusedChildren = new ArrayList<NodeDescription>();

        SDVDiagramDescriptionProvider.DEFINITIONS.forEach(definition -> cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(definition)).ifPresent(reusedChildren::add));
        SDVDiagramDescriptionProvider.USAGES.forEach(usage -> cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(usage)).ifPresent(reusedChildren::add));
        SDVDiagramDescriptionProvider.ANNOTATINGS.forEach(annotating -> cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(annotating)).ifPresent(reusedChildren::add));
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage())).ifPresent(reusedChildren::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getNamespaceImport())).ifPresent(reusedChildren::add);
        return reusedChildren;
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        var droppableNodes = new ArrayList<NodeDescription>();

        SDVDiagramDescriptionProvider.DEFINITIONS.forEach(definition -> cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(definition)).ifPresent(droppableNodes::add));
        SDVDiagramDescriptionProvider.USAGES.forEach(usage -> cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(usage)).ifPresent(droppableNodes::add));
        SDVDiagramDescriptionProvider.ANNOTATINGS.forEach(annotating -> cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(annotating)).ifPresent(droppableNodes::add));
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage())).ifPresent(droppableNodes::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getNamespaceImport())).ifPresent(droppableNodes::add);

        return droppableNodes;
    }

    @Override
    protected List<NodeDescription> getAllNodeDescriptions(IViewDiagramElementFinder cache) {
        var allNodes = new ArrayList<NodeDescription>();

        SDVDiagramDescriptionProvider.DEFINITIONS.forEach(definition -> cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(definition)).ifPresent(allNodes::add));
        SDVDiagramDescriptionProvider.USAGES.forEach(usage -> cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(usage)).ifPresent(allNodes::add));
        SDVDiagramDescriptionProvider.ANNOTATINGS.forEach(annotating -> cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(annotating)).ifPresent(allNodes::add));
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage())).ifPresent(allNodes::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getNamespaceImport())).ifPresent(allNodes::add);
        return allNodes;
    }

    @Override
    protected List<ToolSectionDescription> getToolSections() {
        return List.of(
                SDVDiagramDescriptionProvider.REQUIREMENTS_TOOL_SECTION,
                SDVDiagramDescriptionProvider.STRUCTURE_TOOL_SECTION,
                SDVDiagramDescriptionProvider.BEHAVIOR_TOOL_SECTION,
                SDVDiagramDescriptionProvider.ANALYSIS_TOOL_SECTION,
                SDVDiagramDescriptionProvider.EXTENSION_TOOL_SECTION);
    }

    @Override
    protected List<NodeTool> addCustomTools(IViewDiagramElementFinder cache, String sectionName) {
        var nodeTools = new ArrayList<NodeTool>();
        if (SDVDiagramDescriptionProvider.BEHAVIOR_TOOL_SECTION.name().equals(sectionName)) {
            nodeTools.add(new ExhibitStateWithReferenceNodeToolProvider(this.descriptionNameGenerator).create(cache));
        } else if (SDVDiagramDescriptionProvider.STRUCTURE_TOOL_SECTION.name().equals(sectionName)) {
            NodeDescription nodeDescription = cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getNamespaceImport())).orElse(null);
            nodeTools.add(new NamespaceImportNodeToolProvider(nodeDescription, this.descriptionNameGenerator).create(cache));
        }
        return nodeTools;
    }
}
