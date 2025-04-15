/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.diagram.statetransition.view.edges;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.diagram.common.view.edges.AbstractTransitionEdgeDescriptionProvider;
import org.eclipse.syson.diagram.statetransition.view.StateTransitionViewDiagramDescriptionProvider;
import org.eclipse.syson.diagram.statetransition.view.services.StateTransitionViewNodeToolsWithoutSectionSwitch;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to create the {@link TransitionUsage} edge description.
 *
 * @author arichard
 */
public class TransitionEdgeDescriptionProvider extends AbstractTransitionEdgeDescriptionProvider {

    public TransitionEdgeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider, descriptionNameGenerator);
    }

    @Override
    protected List<NodeTool> getToolsWithoutSection(IViewDiagramElementFinder cache) {
        StateTransitionViewNodeToolsWithoutSectionSwitch toolsWithoutSectionSwitch = new StateTransitionViewNodeToolsWithoutSectionSwitch(cache, this.getAllNodeDescriptions(cache));
        return toolsWithoutSectionSwitch.doSwitch(SysmlPackage.eINSTANCE.getTransitionUsage());
    }

    protected List<NodeDescription> getAllNodeDescriptions(IViewDiagramElementFinder cache) {
        var allNodes = new ArrayList<NodeDescription>();
        StateTransitionViewDiagramDescriptionProvider.DEFINITIONS.forEach(definition -> cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(definition)).ifPresent(allNodes::add));
        StateTransitionViewDiagramDescriptionProvider.USAGES.forEach(usage -> cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(usage)).ifPresent(allNodes::add));
        StateTransitionViewDiagramDescriptionProvider.ANNOTATINGS.forEach(usage -> cache.getNodeDescription(this.getDescriptionNameGenerator().getNodeName(usage)).ifPresent(allNodes::add));
        return allNodes;
    }
}
