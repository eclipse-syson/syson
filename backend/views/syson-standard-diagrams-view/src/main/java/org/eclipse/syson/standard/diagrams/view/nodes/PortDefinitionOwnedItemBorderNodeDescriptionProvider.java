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

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.nodes.AbstractItemUsageBorderNodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to create the border node description for PortDefinition getDefinition_OwnedItem.
 *
 * @author mcharfadi
 */
public class PortDefinitionOwnedItemBorderNodeDescriptionProvider extends AbstractItemUsageBorderNodeDescriptionProvider {

    public PortDefinitionOwnedItemBorderNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(SysmlPackage.eINSTANCE.getDefinition_OwnedItem(), colorProvider, descriptionNameGenerator);
    }

    @Override
    protected String getName() {
        return this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage(), this.eReference);
    }

    // We filter so elements that extends Item like Parts are not returned
    @Override
    protected String getSemanticCandidatesExpression() {
        return "aql:self.ownedItem->select(e | e.oclIsTypeOf(sysml::ItemUsage))";
    }

    @Override
    protected List<NodeDescription> getBindingConectorAsUsageToolTarget(IViewDiagramElementFinder cache) {
        var nodeDescriptions = new ArrayList<NodeDescription>();
        cache.getNodeDescription(this.getName()).ifPresent(nodeDescriptions::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage())).ifPresent(nodeDescriptions::add);
        return nodeDescriptions;
    }

    @Override
    protected List<NodeDescription> getFlowConnectionToolTargets(IViewDiagramElementFinder cache) {
        var nodes = new ArrayList<NodeDescription>();
        cache.getNodeDescription(this.getName()).ifPresent(nodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort())).ifPresent(nodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedPort())).ifPresent(nodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage())).ifPresent(nodes::add);
        return nodes;
    }

}
