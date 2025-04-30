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
package org.eclipse.syson.diagram.general.view.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.nodes.AbstractFakeNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.StatesCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

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
        this.addReusableCompartments(cache, nameGenerator, childrenNodes);
        this.addReusableBorderedNode(cache, nameGenerator, childrenNodes);
        return childrenNodes;
    }

    private void addReusableCompartments(IViewDiagramElementFinder cache, GVDescriptionNameGenerator nameGenerator, ArrayList<NodeDescription> childrenNodes) {
        // don't forget to add custom compartments
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_ActorParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_StakeholderParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getRequirementDefinition(), SysmlPackage.eINSTANCE.getRequirementDefinition_SubjectParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getRequirementDefinition(), SysmlPackage.eINSTANCE.getRequirementDefinition_ActorParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getRequirementDefinition(), SysmlPackage.eINSTANCE.getRequirementDefinition_StakeholderParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getConcernUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getConcernUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_ActorParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getConcernUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_StakeholderParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getConcernDefinition(), SysmlPackage.eINSTANCE.getRequirementDefinition_SubjectParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getConcernDefinition(), SysmlPackage.eINSTANCE.getRequirementDefinition_ActorParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getConcernDefinition(), SysmlPackage.eINSTANCE.getRequirementDefinition_StakeholderParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_ActorParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_StakeholderParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_SubjectParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getCaseDefinition(), SysmlPackage.eINSTANCE.getCaseDefinition_SubjectParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_ObjectiveRequirement()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getCaseDefinition(), SysmlPackage.eINSTANCE.getCaseDefinition_ObjectiveRequirement()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_ActorParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getCaseDefinition(), SysmlPackage.eINSTANCE.getCaseDefinition_ActorParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getAllocationDefinition(), SysmlPackage.eINSTANCE.getConnectionDefinition_ConnectionEnd()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getStateDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedState()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getExhibitStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getActionDefinition(), SysmlPackage.eINSTANCE.getBehavior_Parameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getStateDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedState()) + StatesCompartmentNodeDescriptionProvider.STATES_NAME)
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()) + StatesCompartmentNodeDescriptionProvider.STATES_NAME)
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getExhibitStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()) + StatesCompartmentNodeDescriptionProvider.STATES_NAME)
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getStateDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedState()) + StatesCompartmentNodeDescriptionProvider.EXHIBIT_STATES_NAME)
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()) + StatesCompartmentNodeDescriptionProvider.EXHIBIT_STATES_NAME)
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getExhibitStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()) + StatesCompartmentNodeDescriptionProvider.EXHIBIT_STATES_NAME)
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getActionUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAction()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getActionDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getPerformActionUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAction()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getStateDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedState()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getExhibitStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()))
                .ifPresent(childrenNodes::add);

        this.addCustomCompartmentsForParts(cache, nameGenerator, childrenNodes);
    }

    private void addCustomCompartmentsForParts(IViewDiagramElementFinder cache, IDescriptionNameGenerator nameGenerator, List<NodeDescription> childrenNodes) {
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()) + StatesCompartmentNodeDescriptionProvider.STATES_NAME)
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()) + StatesCompartmentNodeDescriptionProvider.EXHIBIT_STATES_NAME)
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getPartDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedState()) + StatesCompartmentNodeDescriptionProvider.STATES_NAME)
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getPartDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedState()) + StatesCompartmentNodeDescriptionProvider.EXHIBIT_STATES_NAME)
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAction()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getPartDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getPartDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedState()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getStateDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedState()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getExhibitStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAction()) + PerformActionsCompartmentNodeDescriptionProvider.PERFORM_ACTIONS_COMPARTMENT_NAME)
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getPartDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction()) + PerformActionsCompartmentNodeDescriptionProvider.PERFORM_ACTIONS_COMPARTMENT_NAME)
                .ifPresent(childrenNodes::add);
    }

    private void addReusableBorderedNode(IViewDiagramElementFinder cache, GVDescriptionNameGenerator nameGenerator, ArrayList<NodeDescription> childrenNodes) {
        cache.getNodeDescription(nameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(nameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage()))
                .ifPresent(childrenNodes::add);
    }
}
