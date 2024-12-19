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
package org.eclipse.syson.diagram.statetransition.view.nodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.diagram.common.view.nodes.AbstractPackageNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.tools.ExhibitStateWithReferenceNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.NamespaceImportNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ToolSectionDescription;
import org.eclipse.syson.diagram.statetransition.view.StateTransitionViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to create the package node description in State Transition View diagram.
 *
 * @author arichard
 */
public class PackageNodeDescriptionProvider extends AbstractPackageNodeDescriptionProvider {

    public PackageNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider, descriptionNameGenerator);
    }

    @Override
    protected List<NodeDescription> getReusedChildren(IViewDiagramElementFinder cache) {
        var reusedChildren = new ArrayList<NodeDescription>();

        StateTransitionViewDiagramDescriptionProvider.DEFINITIONS.forEach(definition -> cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(definition)).ifPresent(reusedChildren::add));
        StateTransitionViewDiagramDescriptionProvider.USAGES.forEach(usage -> cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(usage)).ifPresent(reusedChildren::add));
        StateTransitionViewDiagramDescriptionProvider.ANNOTATINGS.forEach(usage -> cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(usage)).ifPresent(reusedChildren::add));
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage())).ifPresent(reusedChildren::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getNamespaceImport())).ifPresent(reusedChildren::add);
        return reusedChildren;
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        var droppableNodes = new ArrayList<NodeDescription>();

        StateTransitionViewDiagramDescriptionProvider.DEFINITIONS.forEach(definition -> cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(definition)).ifPresent(droppableNodes::add));
        StateTransitionViewDiagramDescriptionProvider.USAGES.forEach(usage -> cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(usage)).ifPresent(droppableNodes::add));
        StateTransitionViewDiagramDescriptionProvider.ANNOTATINGS.forEach(usage -> cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(usage)).ifPresent(droppableNodes::add));
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage())).ifPresent(droppableNodes::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getNamespaceImport())).ifPresent(droppableNodes::add);
        return droppableNodes;
    }

    @Override
    protected List<NodeDescription> getAllNodeDescriptions(IViewDiagramElementFinder cache) {
        var allNodes = new ArrayList<NodeDescription>();

        StateTransitionViewDiagramDescriptionProvider.DEFINITIONS.forEach(definition -> cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(definition)).ifPresent(allNodes::add));
        StateTransitionViewDiagramDescriptionProvider.USAGES.forEach(usage -> cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(usage)).ifPresent(allNodes::add));
        StateTransitionViewDiagramDescriptionProvider.ANNOTATINGS.forEach(usage -> cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(usage)).ifPresent(allNodes::add));
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage())).ifPresent(allNodes::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getNamespaceImport())).ifPresent(allNodes::add);
        return allNodes;
    }

    @Override
    protected List<ToolSectionDescription> getToolSections() {
        List<EClass> stateTransitionElementsSV = StateTransitionViewDiagramDescriptionProvider.BEHAVIOR_TOOL_SECTION.elements();
        List<EClass> stateTransitionElementsPackage = new ArrayList<>(stateTransitionElementsSV);
        stateTransitionElementsPackage.add(SysmlPackage.eINSTANCE.getComment());
        return List.of(
                new ToolSectionDescription(StateTransitionViewDiagramDescriptionProvider.BEHAVIOR_TOOL_SECTION.name(), Collections.unmodifiableList(stateTransitionElementsPackage)));
    }

    @Override
    protected List<NodeTool> addCustomTools(IViewDiagramElementFinder cache, String sectionName) {
        var nodeTools = new ArrayList<NodeTool>();
        if (StateTransitionViewDiagramDescriptionProvider.BEHAVIOR_TOOL_SECTION.name().equals(sectionName)) {
            nodeTools.add(new ExhibitStateWithReferenceNodeToolProvider(this.descriptionNameGenerator).create(cache));
            NodeDescription nodeDescription = cache.getNodeDescription(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getNamespaceImport())).orElse(null);
            nodeTools.add(new NamespaceImportNodeToolProvider(nodeDescription, this.descriptionNameGenerator).create(cache));
        }
        return nodeTools;
    }
}
