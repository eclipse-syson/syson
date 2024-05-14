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
import org.eclipse.syson.diagram.actionflow.view.AFVDescriptionNameGenerator;
import org.eclipse.syson.diagram.actionflow.view.ActionFlowViewDiagramDescriptionProvider;
import org.eclipse.syson.diagram.actionflow.view.services.ActionFlowViewNodeToolSectionSwitch;
import org.eclipse.syson.diagram.common.view.nodes.AbstractDefinitionNodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Node description provider for all SysMLv2 Definitions elements in the Action Flow View diagram.
 *
 * @author arichard
 */
public class DefinitionNodeDescriptionProvider extends AbstractDefinitionNodeDescriptionProvider {

    public DefinitionNodeDescriptionProvider(EClass eClass, IColorProvider colorProvider) {
        super(eClass, colorProvider, new AFVDescriptionNameGenerator());
    }

    @Override
    protected List<NodeDescription> getReusedChildren(IViewDiagramElementFinder cache) {
        var reusedChildren = new ArrayList<NodeDescription>();

        ActionFlowViewDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.forEach((type, listItems) -> {
            if (type.equals(this.eClass)) {
                listItems.forEach(eReference -> {
                    // list compartment
                    cache.getNodeDescription(this.nameGenerator.getCompartmentName(type, eReference)).ifPresent(reusedChildren::add);
                    // free form compartment
                    cache.getNodeDescription(this.nameGenerator.getFreeFormCompartmentName(this.eClass, eReference)).ifPresent(reusedChildren::add);
                });
            }
        });
        return reusedChildren;
    }

    @Override
    protected List<NodeDescription> getAllNodeDescriptions(IViewDiagramElementFinder cache) {
        var allNodes = new ArrayList<NodeDescription>();

        ActionFlowViewDiagramDescriptionProvider.DEFINITIONS.forEach(definition -> cache.getNodeDescription(this.nameGenerator.getNodeName(definition)).ifPresent(allNodes::add));
        ActionFlowViewDiagramDescriptionProvider.USAGES.forEach(usage -> cache.getNodeDescription(this.nameGenerator.getNodeName(usage)).ifPresent(allNodes::add));
        cache.getNodeDescription(this.nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage())).ifPresent(allNodes::add);
        return allNodes;
    }

    @Override
    protected List<NodeToolSection> getToolSections(NodeDescription nodeDescription, IViewDiagramElementFinder cache) {
        ActionFlowViewNodeToolSectionSwitch toolSectionSwitch = new ActionFlowViewNodeToolSectionSwitch(this.getAllNodeDescriptions(cache));
        return toolSectionSwitch.doSwitch(this.eClass);
    }
}
