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
import org.eclipse.syson.diagram.common.view.nodes.AbstractPackageNodeDescriptionProvider;
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

        StateTransitionViewDiagramDescriptionProvider.DEFINITIONS.forEach(definition -> cache.getNodeDescription(this.nameGenerator.getNodeName(definition)).ifPresent(reusedChildren::add));
        StateTransitionViewDiagramDescriptionProvider.USAGES.forEach(usage -> cache.getNodeDescription(this.nameGenerator.getNodeName(usage)).ifPresent(reusedChildren::add));
        StateTransitionViewDiagramDescriptionProvider.ANNOTATINGS.forEach(usage -> cache.getNodeDescription(this.nameGenerator.getNodeName(usage)).ifPresent(reusedChildren::add));
        cache.getNodeDescription(this.nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage())).ifPresent(reusedChildren::add);
        return reusedChildren;
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        var droppableNodes = new ArrayList<NodeDescription>();

        StateTransitionViewDiagramDescriptionProvider.DEFINITIONS.forEach(definition -> cache.getNodeDescription(this.nameGenerator.getNodeName(definition)).ifPresent(droppableNodes::add));
        StateTransitionViewDiagramDescriptionProvider.USAGES.forEach(usage -> cache.getNodeDescription(this.nameGenerator.getNodeName(usage)).ifPresent(droppableNodes::add));
        StateTransitionViewDiagramDescriptionProvider.ANNOTATINGS.forEach(usage -> cache.getNodeDescription(this.nameGenerator.getNodeName(usage)).ifPresent(droppableNodes::add));
        cache.getNodeDescription(this.nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage())).ifPresent(droppableNodes::add);

        return droppableNodes;
    }

    @Override
    protected List<NodeDescription> getAllNodeDescriptions(IViewDiagramElementFinder cache) {
        var allNodes = new ArrayList<NodeDescription>();

        StateTransitionViewDiagramDescriptionProvider.DEFINITIONS.forEach(definition -> cache.getNodeDescription(this.nameGenerator.getNodeName(definition)).ifPresent(allNodes::add));
        StateTransitionViewDiagramDescriptionProvider.USAGES.forEach(usage -> cache.getNodeDescription(this.nameGenerator.getNodeName(usage)).ifPresent(allNodes::add));
        StateTransitionViewDiagramDescriptionProvider.ANNOTATINGS.forEach(usage -> cache.getNodeDescription(this.nameGenerator.getNodeName(usage)).ifPresent(allNodes::add));
        cache.getNodeDescription(this.nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage())).ifPresent(allNodes::add);
        return allNodes;
    }

    @Override
    protected List<ToolSectionDescription> getToolSections() {
        List<EClass> stateTransitionElementsSV = StateTransitionViewDiagramDescriptionProvider.STATE_TRANSITION_TOOL_SECTIONS.elements();
        List<EClass> stateTransitionElementsPackage = new ArrayList<>(stateTransitionElementsSV);
        stateTransitionElementsPackage.add(SysmlPackage.eINSTANCE.getComment());
        return List.of(
                new ToolSectionDescription(StateTransitionViewDiagramDescriptionProvider.STATE_TRANSITION_TOOL_SECTIONS.name(), Collections.unmodifiableList(stateTransitionElementsPackage)));
    }
}
