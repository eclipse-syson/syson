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
package org.eclipse.syson.diagram.statetransition.view.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.syson.diagram.common.view.nodes.AbstractUsageNodeDescriptionProvider;
import org.eclipse.syson.diagram.statetransition.view.StateTransitionViewDiagramDescriptionProvider;
import org.eclipse.syson.diagram.statetransition.view.services.StateTransitionViewNodeToolSectionSwitch;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Node description provider for all SysMLv2 Usage elements in the StateTransition View diagram.
 *
 * @author adieumegard
 */
public class UsageNodeDescriptionProvider extends AbstractUsageNodeDescriptionProvider {

    public UsageNodeDescriptionProvider(EClass eClass, IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(eClass, colorProvider, descriptionNameGenerator);
    }

    @Override
    protected List<NodeDescription> getReusedChildren(IViewDiagramElementFinder cache) {
        var reusedChildren = new ArrayList<NodeDescription>();

        StateTransitionViewDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.forEach((type, listItems) -> {
            if (type.equals(this.eClass)) {
                listItems.forEach(eReference -> {
                    // list compartment
                    cache.getNodeDescription(this.nameGenerator.getCompartmentName(type, eReference)).ifPresent(reusedChildren::add);
                });
            }
        });
        if (this.eClass.equals(SysmlPackage.eINSTANCE.getStateUsage())) {
            cache.getNodeDescription(this.nameGenerator.getFreeFormCompartmentName(this.eClass, SysmlPackage.eINSTANCE.getUsage_NestedState())).ifPresent(reusedChildren::add);
        }
        return reusedChildren;
    }

    @Override
    protected List<NodeDescription> getAllNodeDescriptions(IViewDiagramElementFinder cache) {
        var allNodes = new ArrayList<NodeDescription>();

        StateTransitionViewDiagramDescriptionProvider.DEFINITIONS.forEach(definition -> cache.getNodeDescription(this.nameGenerator.getNodeName(definition)).ifPresent(allNodes::add));
        StateTransitionViewDiagramDescriptionProvider.USAGES.forEach(usage -> cache.getNodeDescription(this.nameGenerator.getNodeName(usage)).ifPresent(allNodes::add));
        cache.getNodeDescription(this.nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage())).ifPresent(allNodes::add);
        return allNodes;
    }

    @Override
    protected List<NodeToolSection> getToolSections(NodeDescription nodeDescription, IViewDiagramElementFinder cache) {
        StateTransitionViewNodeToolSectionSwitch toolSectionSwitch = new StateTransitionViewNodeToolSectionSwitch(this.getAllNodeDescriptions(cache), this.nameGenerator);
        return toolSectionSwitch.doSwitch(this.eClass);
    }
}
