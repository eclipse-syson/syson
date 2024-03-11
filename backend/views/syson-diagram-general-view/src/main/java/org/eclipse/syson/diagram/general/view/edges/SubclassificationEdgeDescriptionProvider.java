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
package org.eclipse.syson.diagram.general.view.edges;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.edges.AbstractSubclassificationEdgeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Used to create the {@link Subclassification} edge description in the General View diagram.
 *
 * @author arichard
 */
public class SubclassificationEdgeDescriptionProvider extends AbstractSubclassificationEdgeDescriptionProvider {

    public SubclassificationEdgeDescriptionProvider(IColorProvider colorProvider) {
        super(colorProvider);
    }

    @Override
    protected String getName() {
        return "GV Edge Subclassification";
    }

    private List<NodeDescription> getSourceAndTargetNodes(IViewDiagramElementFinder cache) {
        var nameGenerator = new GVDescriptionNameGenerator();
        var sourcesAndTargets = new ArrayList<NodeDescription>();

        cache.getNodeDescription(nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionDefinition())).ifPresent(sourcesAndTargets::add);
        cache.getNodeDescription(nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getAttributeDefinition())).ifPresent(sourcesAndTargets::add);
        cache.getNodeDescription(nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getEnumerationDefinition())).ifPresent(sourcesAndTargets::add);
        cache.getNodeDescription(nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getInterfaceDefinition())).ifPresent(sourcesAndTargets::add);
        cache.getNodeDescription(nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getItemDefinition())).ifPresent(sourcesAndTargets::add);
        cache.getNodeDescription(nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getMetadataDefinition())).ifPresent(sourcesAndTargets::add);
        cache.getNodeDescription(nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage())).ifPresent(sourcesAndTargets::add);
        cache.getNodeDescription(nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartDefinition())).ifPresent(sourcesAndTargets::add);
        cache.getNodeDescription(nameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPortDefinition())).ifPresent(sourcesAndTargets::add);

        return sourcesAndTargets;
    }

    @Override
    protected List<NodeDescription> getSourceNodes(IViewDiagramElementFinder cache) {
        return this.getSourceAndTargetNodes(cache);
    }

    @Override
    protected List<NodeDescription> getTargetNodes(IViewDiagramElementFinder cache) {
        return this.getSourceAndTargetNodes(cache);
    }
}
