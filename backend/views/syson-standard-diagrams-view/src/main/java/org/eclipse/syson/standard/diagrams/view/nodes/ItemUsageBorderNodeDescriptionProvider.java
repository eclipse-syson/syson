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
package org.eclipse.syson.standard.diagrams.view.nodes;

import java.util.List;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.nodes.AbstractItemUsageBorderNodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to create the item usage border node description.
 *
 * @author Arthur Daussy
 */
public class ItemUsageBorderNodeDescriptionProvider extends AbstractItemUsageBorderNodeDescriptionProvider {

    public ItemUsageBorderNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider, descriptionNameGenerator);
    }

    @Override
    protected List<NodeDescription> getBindingConectorAsUsageToolTarget(IViewDiagramElementFinder cache) {
        return List.of(cache.getNodeDescription(this.getName()).get());
    }

    @Override
    protected List<NodeDescription> getFlowConnectionToolTargets(IViewDiagramElementFinder cache) {
        NodeDescription nodeDescription = cache.getNodeDescription(this.getName()).get();
        NodeDescription portBorderNodeDescription = cache.getNodeDescription(this.getDescriptionNameGenerator().getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage())).get();
        return List.of(nodeDescription, portBorderNodeDescription);
    }


}
