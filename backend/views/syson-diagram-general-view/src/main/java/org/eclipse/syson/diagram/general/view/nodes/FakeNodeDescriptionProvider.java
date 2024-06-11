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
package org.eclipse.syson.diagram.general.view.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.nodes.AbstractFakeNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * Fake Node Description allowing to store other Node Descriptions that will be reused by other Node Descriptions.
 * Typical use is for compartment Nodes.
 *
 * @author arichard
 */
public class FakeNodeDescriptionProvider extends AbstractFakeNodeDescriptionProvider {

    public FakeNodeDescriptionProvider(IColorProvider colorProvider) {
        super(colorProvider);
    }

    @Override
    protected String getName() {
        return "GV Node Fake";
    }

    @Override
    protected List<NodeDescription> getChildrenDescription(IViewDiagramElementFinder cache) {
        var nameGenerator = new GVDescriptionNameGenerator();
        var childrenNodes = new ArrayList<NodeDescription>();

        GeneralViewDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.forEach((type, listItems) -> {
            listItems.forEach(eReference -> cache.getNodeDescription(nameGenerator.getCompartmentName(type, eReference)).ifPresent(childrenNodes::add));
        });
        GeneralViewDiagramDescriptionProvider.COMPARTMENTS_WITH_MERGED_LIST_ITEMS.forEach((type, listItems) -> {
            listItems.forEach(eReference -> cache.getNodeDescription(nameGenerator.getCompartmentName(type, eReference)).ifPresent(childrenNodes::add));
        });
        // don't forget to add custom compartments
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getRequirementDefinition(), SysmlPackage.eINSTANCE.getRequirementDefinition_SubjectParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getUseCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_SubjectParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getUseCaseDefinition(), SysmlPackage.eINSTANCE.getCaseDefinition_SubjectParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getUseCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_ObjectiveRequirement()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getUseCaseDefinition(), SysmlPackage.eINSTANCE.getCaseDefinition_ObjectiveRequirement()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getAllocationDefinition(), SysmlPackage.eINSTANCE.getConnectionDefinition_ConnectionEnd()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getStateDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedState()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getActionUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAction()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getActionDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction()))
                .ifPresent(childrenNodes::add);
        return childrenNodes;
    }
}
