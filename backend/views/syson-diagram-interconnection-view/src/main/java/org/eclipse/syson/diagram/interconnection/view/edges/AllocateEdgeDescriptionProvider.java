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
package org.eclipse.syson.diagram.interconnection.view.edges;

import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.edges.AbstractAllocateEdgeDescriptionProvider;
import org.eclipse.syson.diagram.interconnection.view.IVDescriptionNameGenerator;
import org.eclipse.syson.diagram.interconnection.view.nodes.RootPortUsageBorderNodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Used to create the Allocate edge description inside the Interconnection View diagram.
 *
 * @author Jerome Gout
 */
public class AllocateEdgeDescriptionProvider extends AbstractAllocateEdgeDescriptionProvider {

    private final IVDescriptionNameGenerator descriptionNameGenerator;

    public AllocateEdgeDescriptionProvider(IColorProvider colorProvider, IVDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider);
        this.descriptionNameGenerator = Objects.requireNonNull(descriptionNameGenerator);
    }

    @Override
    protected String getName() {
        return this.descriptionNameGenerator.getEdgeName("Allocate");
    }

    @Override
    protected List<NodeDescription> getSourceNodes(IViewDiagramElementFinder cache) {
        return this.getSourceAndTarget(cache);
    }

    @Override
    protected List<NodeDescription> getTargetNodes(IViewDiagramElementFinder cache) {
        return this.getSourceAndTarget(cache);
    }

    private List<NodeDescription> getSourceAndTarget(IViewDiagramElementFinder cache) {
        var rootPortUsage = cache.getNodeDescription(RootPortUsageBorderNodeDescriptionProvider.NAME).get();
        var portUsage = cache.getNodeDescription(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage())).get();
        var firstLevelChildPartUsage = cache.getNodeDescription(this.descriptionNameGenerator.getFirstLevelNodeName(SysmlPackage.eINSTANCE.getPartUsage())).get();
        var childPartUsage = cache.getNodeDescription(this.descriptionNameGenerator.getChildNodeName(SysmlPackage.eINSTANCE.getPartUsage())).get();

        return List.of(firstLevelChildPartUsage, childPartUsage, rootPortUsage, portUsage);
    }
}
