/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.diagram.interconnection.view.edges;

import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.edges.AbstractFlowUsageEdgeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.nodes.RootPortUsageBorderNodeDescriptionProvider;
import org.eclipse.syson.sysml.FlowUsage;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to describe an {@link FlowUsage} edge between two {@link PortUsage}/{@link ItemUsage}.
 *
 * @author arichard
 */
public class FlowUsageEdgeDescriptionProvider extends AbstractFlowUsageEdgeDescriptionProvider {

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public FlowUsageEdgeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    @Override
    public String getName() {
        return this.descriptionNameGenerator.getEdgeName(SysmlPackage.eINSTANCE.getFlowUsage());
    }

    @Override
    protected List<NodeDescription> getSourceNodes(IViewDiagramElementFinder cache) {
        var optRootPortUsageBorderNodeDescription = cache.getNodeDescription(RootPortUsageBorderNodeDescriptionProvider.NAME);
        var optPortUsageBorderNodeDescription = cache.getNodeDescription(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage()));
        var optItemUsageBorderNodeDescription = cache.getNodeDescription(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage()));
        if (optRootPortUsageBorderNodeDescription.isPresent() && optPortUsageBorderNodeDescription.isPresent() && optItemUsageBorderNodeDescription.isPresent()) {
            return List.of(optRootPortUsageBorderNodeDescription.get(), optPortUsageBorderNodeDescription.get(), optItemUsageBorderNodeDescription.get());
        }
        return List.of();
    }

    @Override
    protected List<NodeDescription> getTargetNodes(IViewDiagramElementFinder cache) {
        var optRootPortUsageBorderNodeDescription = cache.getNodeDescription(RootPortUsageBorderNodeDescriptionProvider.NAME);
        var optPortUsageBorderNodeDescription = cache.getNodeDescription(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage()));
        var optItemUsageBorderNodeDescription = cache.getNodeDescription(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage()));
        if (optRootPortUsageBorderNodeDescription.isPresent() && optPortUsageBorderNodeDescription.isPresent() && optItemUsageBorderNodeDescription.isPresent()) {
            return List.of(optRootPortUsageBorderNodeDescription.get(), optPortUsageBorderNodeDescription.get(), optItemUsageBorderNodeDescription.get());
        }
        return List.of();
    }
}
