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
package org.eclipse.syson.diagram.actionflow.view.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.syson.diagram.actionflow.view.ActionFlowViewDiagramDescriptionProvider;
import org.eclipse.syson.diagram.actionflow.view.services.ActionFlowViewNodeToolSectionSwitch;
import org.eclipse.syson.diagram.common.view.nodes.AbstractUsageNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.DoneActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.ForkActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.JoinActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.MergeActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.StartActionNodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Node description provider for all SysMLv2 Usage elements in the Action Flow View diagram.
 *
 * @author arichard
 */
public class UsageNodeDescriptionProvider extends AbstractUsageNodeDescriptionProvider {

    public UsageNodeDescriptionProvider(EClass eClass, IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(eClass, colorProvider, descriptionNameGenerator);
    }

    @Override
    protected List<NodeDescription> getReusedChildren(IViewDiagramElementFinder cache) {
        var reusedChildren = new ArrayList<NodeDescription>();

        ActionFlowViewDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.forEach((type, listItems) -> {
            if (type.equals(this.eClass)) {
                listItems.forEach(eReference -> {
                    // list compartment
                    cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentName(type, eReference)).ifPresent(reusedChildren::add);
                    // free form compartment
                    cache.getNodeDescription(this.getDescriptionNameGenerator().getFreeFormCompartmentName(this.eClass, eReference)).ifPresent(reusedChildren::add);
                });
            }
        });
        return reusedChildren;
    }

    @Override
    protected List<NodeDescription> getAllNodeDescriptions(IViewDiagramElementFinder cache) {
        var allNodes = new ArrayList<NodeDescription>();
        ActionFlowViewDiagramDescriptionProvider.DEFINITIONS.forEach(definition -> cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(definition)).ifPresent(allNodes::add));
        ActionFlowViewDiagramDescriptionProvider.USAGES.forEach(usage -> cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(usage)).ifPresent(allNodes::add));
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(SysmlPackage.eINSTANCE.getPackage())).ifPresent(allNodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(StartActionNodeDescriptionProvider.START_ACTION_NAME)).ifPresent(allNodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(DoneActionNodeDescriptionProvider.DONE_ACTION_NAME)).ifPresent(allNodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(JoinActionNodeDescriptionProvider.JOIN_ACTION_NAME)).ifPresent(allNodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(ForkActionNodeDescriptionProvider.FORK_ACTION_NAME)).ifPresent(allNodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(MergeActionNodeDescriptionProvider.MERGE_ACTION_NAME)).ifPresent(allNodes::add);
        return allNodes;
    }

    @Override
    protected List<NodeToolSection> getToolSections(NodeDescription nodeDescription, IViewDiagramElementFinder cache) {
        ActionFlowViewNodeToolSectionSwitch toolSectionSwitch = new ActionFlowViewNodeToolSectionSwitch(cache, this.getAllNodeDescriptions(cache));
        return toolSectionSwitch.doSwitch(this.eClass);
    }
}
