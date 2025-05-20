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
package org.eclipse.syson.standard.diagrams.view.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.nodes.AbstractFakeNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.InterconnectionCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.StatesCompartmentNodeDescriptionProvider;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.standard.diagrams.view.SDVDiagramDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Fake Node Description allowing to store other Node Descriptions that will be reused by other Node Descriptions.
 * Typical use is for compartment Nodes.
 *
 * @author arichard
 */
public class FakeNodeDescriptionProvider extends AbstractFakeNodeDescriptionProvider {

    public FakeNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(colorProvider, descriptionNameGenerator);
    }

    @Override
    protected String getName() {
        return SDVDescriptionNameGenerator.PREFIX + " Node Fake";
    }

    @Override
    protected List<NodeDescription> getChildrenDescription(IViewDiagramElementFinder cache) {
        var childrenNodes = new ArrayList<NodeDescription>();

        SDVDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.forEach((type, listItems) -> {
            listItems.forEach(eReference -> cache.getNodeDescription(this.descriptionNameGenerator.getCompartmentName(type, eReference)).ifPresent(childrenNodes::add));
        });
        this.addReusableCustomNodes(cache, childrenNodes);
        this.addReusableCompartments(cache, this.descriptionNameGenerator, childrenNodes);
        this.addReusableBorderedNode(cache, this.descriptionNameGenerator, childrenNodes);
        return childrenNodes;
    }

    private void addReusableCompartments(IViewDiagramElementFinder cache, IDescriptionNameGenerator descriptionNameGenerator, List<NodeDescription> childrenNodes) {
        // don't forget to add custom compartments
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_ActorParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_StakeholderParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getRequirementDefinition(), SysmlPackage.eINSTANCE.getRequirementDefinition_SubjectParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getRequirementDefinition(), SysmlPackage.eINSTANCE.getRequirementDefinition_ActorParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getRequirementDefinition(), SysmlPackage.eINSTANCE.getRequirementDefinition_StakeholderParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getConcernUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getConcernUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_ActorParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getConcernUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_StakeholderParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getConcernDefinition(), SysmlPackage.eINSTANCE.getRequirementDefinition_SubjectParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getConcernDefinition(), SysmlPackage.eINSTANCE.getRequirementDefinition_ActorParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getConcernDefinition(), SysmlPackage.eINSTANCE.getRequirementDefinition_StakeholderParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_ActorParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_StakeholderParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_SubjectParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getCaseDefinition(), SysmlPackage.eINSTANCE.getCaseDefinition_SubjectParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_ObjectiveRequirement()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getCaseDefinition(), SysmlPackage.eINSTANCE.getCaseDefinition_ObjectiveRequirement()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_ActorParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getCaseDefinition(), SysmlPackage.eINSTANCE.getCaseDefinition_ActorParameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getAllocationDefinition(), SysmlPackage.eINSTANCE.getConnectionDefinition_ConnectionEnd()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getStateDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedState()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getExhibitStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getActionDefinition(), SysmlPackage.eINSTANCE.getBehavior_Parameter()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getStateDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedState()) + StatesCompartmentNodeDescriptionProvider.STATES_NAME)
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()) + StatesCompartmentNodeDescriptionProvider.STATES_NAME)
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getExhibitStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()) + StatesCompartmentNodeDescriptionProvider.STATES_NAME)
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getStateDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedState()) + StatesCompartmentNodeDescriptionProvider.EXHIBIT_STATES_NAME)
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()) + StatesCompartmentNodeDescriptionProvider.EXHIBIT_STATES_NAME)
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getExhibitStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()) + StatesCompartmentNodeDescriptionProvider.EXHIBIT_STATES_NAME)
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getActionUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAction()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getActionDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getActionUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAction())
                + PerformActionsCompartmentNodeDescriptionProvider.PERFORM_ACTIONS_COMPARTMENT_NAME)
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getActionDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction())
                + PerformActionsCompartmentNodeDescriptionProvider.PERFORM_ACTIONS_COMPARTMENT_NAME)
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getPerformActionUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAction()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getStateDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedState()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getExhibitStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()))
                .ifPresent(childrenNodes::add);

        this.addCustomCompartmentsForParts(cache, descriptionNameGenerator, childrenNodes);
    }

    private void addCustomCompartmentsForParts(IViewDiagramElementFinder cache, IDescriptionNameGenerator descriptionNameGenerator, List<NodeDescription> childrenNodes) {
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()) + StatesCompartmentNodeDescriptionProvider.STATES_NAME)
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()) + StatesCompartmentNodeDescriptionProvider.EXHIBIT_STATES_NAME)
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getPartDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedState()) + StatesCompartmentNodeDescriptionProvider.STATES_NAME)
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getPartDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedState()) + StatesCompartmentNodeDescriptionProvider.EXHIBIT_STATES_NAME)
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAction()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getPartDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getPartDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedState()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getStateDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedState()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getExhibitStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAction()) + PerformActionsCompartmentNodeDescriptionProvider.PERFORM_ACTIONS_COMPARTMENT_NAME)
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getPartDefinition(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction()) + PerformActionsCompartmentNodeDescriptionProvider.PERFORM_ACTIONS_COMPARTMENT_NAME)
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getFreeFormCompartmentName(InterconnectionCompartmentNodeDescriptionProvider.COMPARTMENT_NAME)).ifPresent(childrenNodes::add);
    }

    private void addReusableBorderedNode(IViewDiagramElementFinder cache, IDescriptionNameGenerator descriptionNameGenerator, List<NodeDescription> childrenNodes) {
        cache.getNodeDescription(descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage()))
                .ifPresent(childrenNodes::add);
        cache.getNodeDescription(descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage()))
                .ifPresent(childrenNodes::add);
    }
}
