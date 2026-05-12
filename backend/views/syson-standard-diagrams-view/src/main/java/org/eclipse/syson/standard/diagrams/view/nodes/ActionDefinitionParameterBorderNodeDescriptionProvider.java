/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Used to create border node description for ActionDefinition getBehavior_Parameter.
 *
 * @author Arthur Daussy
 */
public class ActionDefinitionParameterBorderNodeDescriptionProvider extends AbstractItemUsageBorderNodeDescriptionProvider {

    public ActionDefinitionParameterBorderNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(SysmlPackage.eINSTANCE.getBehavior_Parameter(), colorProvider, descriptionNameGenerator);
    }

    @Override
    protected String getName() {
        return this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getBehavior_Parameter());
    }

    @Override
    protected String getSemanticCandidatesExpression() {
        return "aql:self.parameter";
    }

    @Override
    protected List<NodeDescription> getBindingConectorAsUsageToolTarget(IViewDiagramElementFinder cache) {
        var nodeDescriptions = new ArrayList<NodeDescription>();
        cache.getNodeDescription(this.getName()).ifPresent(nodeDescriptions::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedItem())).ifPresent(nodeDescriptions::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getUsage_NestedItem())).ifPresent(nodeDescriptions::add);
        return nodeDescriptions;
    }

}
