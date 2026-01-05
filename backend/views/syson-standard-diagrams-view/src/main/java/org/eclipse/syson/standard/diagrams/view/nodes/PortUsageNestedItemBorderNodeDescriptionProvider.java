/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.nodes.AbstractItemUsageBorderNodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLConstants;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Used to create the border node description for PortUsage getUsage_NestedItem.
 *
 * @author mcharfadi
 */
public class PortUsageNestedItemBorderNodeDescriptionProvider extends AbstractItemUsageBorderNodeDescriptionProvider {

    public PortUsageNestedItemBorderNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(SysmlPackage.eINSTANCE.getDefinition_OwnedItem(), colorProvider, descriptionNameGenerator);
    }

    @Override
    protected String getName() {
        return this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getUsage_NestedItem());
    }

    // We filter so elements that extends Item like Parts are not returned
    @Override
    protected String getSemanticCandidatesExpression() {
        var itemUsage = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getItemUsage());
        return AQLConstants.AQL_SELF + "." + SysmlPackage.eINSTANCE.getUsage_NestedItem().getName() + "->select(e | e.oclIsTypeOf(" + itemUsage + "))";

    }

    @Override
    protected List<NodeDescription> getBindingConectorAsUsageToolTarget(IViewDiagramElementFinder cache) {
        var nodeDescriptions = new ArrayList<NodeDescription>();
        cache.getNodeDescription(this.getName()).ifPresent(nodeDescriptions::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getBehavior_Parameter())).ifPresent(nodeDescriptions::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedItem())).ifPresent(nodeDescriptions::add);
        return nodeDescriptions;
    }

    @Override
    protected List<NodeDescription> getFlowConnectionToolTargets(IViewDiagramElementFinder cache) {
        var nodes = new ArrayList<NodeDescription>();
        cache.getNodeDescription(this.getName()).ifPresent(nodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort())).ifPresent(nodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedPort())).ifPresent(nodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getBehavior_Parameter())).ifPresent(nodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedItem())).ifPresent(nodes::add);
        return nodes;
    }

}
