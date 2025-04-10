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
package org.eclipse.syson.diagram.general.view.edges;

import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.edges.AbstractInterfaceUsageEdgeDescriptionProvider;
import org.eclipse.syson.sysml.InterfaceUsage;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to describe an {@link InterfaceUsage} edge between two {@link PortUsage}.
 *
 * @author frouene
 */
public class InterfaceUsageEdgeDescriptionProvider extends AbstractInterfaceUsageEdgeDescriptionProvider {

    private final IDescriptionNameGenerator descriptionNameGenerator;

    public InterfaceUsageEdgeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    public String getName() {
        return this.descriptionNameGenerator.getEdgeName(SysmlPackage.eINSTANCE.getInterfaceUsage());
    }

    @Override
    protected List<NodeDescription> getSourceNodes(IViewDiagramElementFinder cache) {
        var optPortUsageBorderNodeDescription = cache.getNodeDescription(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage()));
        return optPortUsageBorderNodeDescription.map(List::of).orElseGet(List::of);
    }

    @Override
    protected List<NodeDescription> getTargetNodes(IViewDiagramElementFinder cache) {
        var optPortUsageBorderNodeDescription = cache.getNodeDescription(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage()));
        return optPortUsageBorderNodeDescription.map(List::of).orElseGet(List::of);
    }

}
