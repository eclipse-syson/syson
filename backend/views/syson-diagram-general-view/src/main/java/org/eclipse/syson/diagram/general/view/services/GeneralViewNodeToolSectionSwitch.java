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
package org.eclipse.syson.diagram.general.view.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.syson.diagram.common.view.services.AbstractViewNodeToolSectionSwitch;
import org.eclipse.syson.diagram.common.view.services.description.ToolDescriptionService;
import org.eclipse.syson.diagram.common.view.tools.AcceptActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.AcceptActionPayloadNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.AcceptActionPortUsageReceiverToolNodeProvider;
import org.eclipse.syson.diagram.common.view.tools.ActionFlowCompartmentNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.AssignmentActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.CompartmentNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.DecisionActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.DoneActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ForkActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.JoinActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.MergeActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ObjectiveRequirementCompartmentNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.PerformActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ReferencingPerformActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.StartActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.StateTransitionActionCompartmentToolProvider;
import org.eclipse.syson.diagram.common.view.tools.StateTransitionCompartmentNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.StateTransitionToggleExhibitStateToolProvider;
import org.eclipse.syson.diagram.common.view.tools.SubjectCompartmentNodeToolProvider;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.AssignmentActionUsage;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.InterfaceDefinition;
import org.eclipse.syson.sysml.ItemDefinition;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.OccurrenceDefinition;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PerformActionUsage;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.RequirementDefinition;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.UseCaseDefinition;
import org.eclipse.syson.sysml.UseCaseUsage;

/**
 * Switch retrieving the list of NodeToolSections for each SysMLv2 concept represented in the General View diagram.
 *
 * @author arichard
 */
public class GeneralViewNodeToolSectionSwitch extends AbstractViewNodeToolSectionSwitch {

    private final List<NodeDescription> allNodeDescriptions;

    private final IViewDiagramElementFinder cache;

    private final ToolDescriptionService toolDescriptionService;

    public GeneralViewNodeToolSectionSwitch(IViewDiagramElementFinder cache, List<NodeDescription> allNodeDescriptions) {
        super(new GVDescriptionNameGenerator());
        this.cache = Objects.requireNonNull(cache);
        this.allNodeDescriptions = Objects.requireNonNull(allNodeDescriptions);
        this.toolDescriptionService = new ToolDescriptionService(this.descriptionNameGenerator);
    }

    @Override
    protected List<EReference> getElementCompartmentReferences(Element element) {
        List<EReference> refs = GeneralViewDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.get(element.eClass());
        if (refs != null) {
            return refs;
        } else {
            return List.of();
        }
    }

    @Override
    protected List<NodeDescription> getAllNodeDescriptions() {
        return this.allNodeDescriptions;
    }

    @Override
    public List<NodeToolSection> caseAcceptActionUsage(AcceptActionUsage object) {
        var createSection = this.toolDescriptionService.buildCreateSection(
                this.createPayloadNodeTool(SysmlPackage.eINSTANCE.getItemDefinition()),
                this.createPayloadNodeTool(SysmlPackage.eINSTANCE.getPartDefinition()),
                this.createPortUsageAsReceiverNodeTool());
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.toolDescriptionService.addElementsNodeToolSection(true));
    }

    @Override
    public List<NodeToolSection> caseActionUsage(ActionUsage object) {
        var createSection = this.toolDescriptionService.buildCreateSection(
                new StartActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new DoneActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new JoinActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new ForkActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new MergeActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new DecisionActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getAttributeUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPartUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPortUsage()),
                new AcceptActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new CompartmentNodeToolProvider(SysmlPackage.eINSTANCE.getElement_Documentation(), this.descriptionNameGenerator).create(this.cache),
                new AssignmentActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new ReferencingPerformActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new PerformActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache));
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItem(SysmlPackage.eINSTANCE.getUsage_NestedItem()));
        createSection.getNodeTools().add(new ActionFlowCompartmentNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(null));
        return List.of(createSection, this.toolDescriptionService.addElementsNodeToolSection(true));
    }

    @Override
    public List<NodeToolSection> caseActionDefinition(ActionDefinition object) {
        var createSection = this.toolDescriptionService.buildCreateSection(new StartActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache));
        createSection.getNodeTools().add(new DoneActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache));
        createSection.getNodeTools().add(new JoinActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache));
        createSection.getNodeTools().add(new ForkActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache));
        createSection.getNodeTools().add(new MergeActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache));
        createSection.getNodeTools().add(new DecisionActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache));
        createSection.getNodeTools().add(new AcceptActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache));
        createSection.getNodeTools().add(new AssignmentActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache));
        createSection.getNodeTools().add(new ReferencingPerformActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache));
        createSection.getNodeTools().add(new PerformActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache));

        if (!(object instanceof StateDefinition)) {
            // StateDefinition has its own "action" creation tools
            createSection.getNodeTools().add(new ActionFlowCompartmentNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(null));
        }
        createSection.getNodeTools().add(new CompartmentNodeToolProvider(SysmlPackage.eINSTANCE.getElement_Documentation(), this.descriptionNameGenerator).create(null));
        return List.of(createSection, this.toolDescriptionService.addElementsNodeToolSection(true));
    }

    @Override
    public List<NodeToolSection> caseAssignmentActionUsage(AssignmentActionUsage object) {
        // Define here the set of node tools that should be added to the Assignment action palette,
        // such as "Set assigned element" and "Set value".
        return List.of(this.toolDescriptionService.addElementsNodeToolSection(true));
    }

    @Override
    public List<NodeToolSection> caseConstraintUsage(ConstraintUsage object) {
        var createSection = this.toolDescriptionService.buildCreateSection(
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getAttributeUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getItemUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPartUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPortUsage()));
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.toolDescriptionService.addElementsNodeToolSection(true));
    }

    @Override
    public List<NodeToolSection> caseDefinition(Definition object) {
        var createSection = this.toolDescriptionService.buildCreateSection();
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection);
    }

    @Override
    public List<NodeToolSection> caseInterfaceDefinition(InterfaceDefinition object) {
        var createSection = this.toolDescriptionService.buildCreateSection();
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection);
    }

    @Override
    public List<NodeToolSection> caseItemDefinition(ItemDefinition object) {
        var createSection = this.toolDescriptionService.buildCreateSection(this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getItemUsage()));
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.toolDescriptionService.addElementsNodeToolSection(true));
    }

    @Override
    public List<NodeToolSection> caseItemUsage(ItemUsage object) {
        var createSection = this.toolDescriptionService.buildCreateSection(this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getItemUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPartUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPortUsage()));
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.toolDescriptionService.addElementsNodeToolSection(true));
    }

    @Override
    public List<NodeToolSection> caseOccurrenceDefinition(OccurrenceDefinition object) {
        var createSection = this.toolDescriptionService.buildCreateSection(this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPartUsage()));
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.toolDescriptionService.addElementsNodeToolSection(true));
    }

    @Override
    public List<NodeToolSection> casePartDefinition(PartDefinition object) {
        var createSection = this.createPartDefinitionElementsToolSection();
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.toolDescriptionService.addElementsNodeToolSection(true));
    }

    @Override
    public List<NodeToolSection> casePartUsage(PartUsage object) {
        var createSection = this.createPartUsageElementsToolSection();
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.toolDescriptionService.addElementsNodeToolSection(true));
    }

    @Override
    public List<NodeToolSection> casePerformActionUsage(PerformActionUsage object) {
        var createSection = this.toolDescriptionService.buildCreateSection(
                new StartActionNodeToolProvider(SysmlPackage.eINSTANCE.getPerformActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new DoneActionNodeToolProvider(SysmlPackage.eINSTANCE.getPerformActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new JoinActionNodeToolProvider(SysmlPackage.eINSTANCE.getPerformActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new ForkActionNodeToolProvider(SysmlPackage.eINSTANCE.getPerformActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new MergeActionNodeToolProvider(SysmlPackage.eINSTANCE.getPerformActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new DecisionActionNodeToolProvider(SysmlPackage.eINSTANCE.getPerformActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new AcceptActionNodeToolProvider(SysmlPackage.eINSTANCE.getPerformActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new ActionFlowCompartmentNodeToolProvider(SysmlPackage.eINSTANCE.getPerformActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new CompartmentNodeToolProvider(SysmlPackage.eINSTANCE.getElement_Documentation(), this.descriptionNameGenerator).create(this.cache),
                new AssignmentActionNodeToolProvider(SysmlPackage.eINSTANCE.getPerformActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new ReferencingPerformActionNodeToolProvider(SysmlPackage.eINSTANCE.getPerformActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new PerformActionNodeToolProvider(SysmlPackage.eINSTANCE.getPerformActionUsage(), this.descriptionNameGenerator).create(this.cache));
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItem(SysmlPackage.eINSTANCE.getUsage_NestedItem()));
        return List.of(createSection, this.toolDescriptionService.addElementsNodeToolSection(true));
    }

    @Override
    public List<NodeToolSection> casePortUsage(PortUsage object) {
        var createSection = this.toolDescriptionService.buildCreateSection(this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPortUsage()));
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.toolDescriptionService.addElementsNodeToolSection(true));
    }

    @Override
    public List<NodeToolSection> caseRequirementUsage(RequirementUsage object) {
        var createSection = this.toolDescriptionService.buildCreateSection(
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getItemUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPartUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPortUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getRequirementUsage()),
                this.createPartUsageAsSubjectNodeTool());
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.toolDescriptionService.addElementsNodeToolSection(true));
    }

    @Override
    public List<NodeToolSection> caseRequirementDefinition(RequirementDefinition object) {
        var createSection = this.toolDescriptionService.buildCreateSection(this.createPartUsageAsSubjectNodeTool());
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.toolDescriptionService.addElementsNodeToolSection(true));
    }

    @Override
    public List<NodeToolSection> caseStateDefinition(StateDefinition object) {
        var createSection = this.toolDescriptionService.buildCreateSection(
                new StateTransitionCompartmentNodeToolProvider(false, false).create(null),
                new StateTransitionCompartmentNodeToolProvider(true, false).create(null),
                new StateTransitionCompartmentNodeToolProvider(false, true).create(null),
                new StateTransitionCompartmentNodeToolProvider(true, true).create(null),
                new StateTransitionActionCompartmentToolProvider(SysmlPackage.eINSTANCE.getStateDefinition_EntryAction()).create(null),
                new StateTransitionActionCompartmentToolProvider(SysmlPackage.eINSTANCE.getStateDefinition_DoAction()).create(null),
                new StateTransitionActionCompartmentToolProvider(SysmlPackage.eINSTANCE.getStateDefinition_ExitAction()).create(null));
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.toolDescriptionService.addElementsNodeToolSection(true));
    }

    @Override
    public List<NodeToolSection> caseStateUsage(StateUsage object) {
        var createSection = this.toolDescriptionService.buildCreateSection(
                new StateTransitionCompartmentNodeToolProvider(false, false).create(null),
                new StateTransitionCompartmentNodeToolProvider(true, false).create(null),
                new StateTransitionCompartmentNodeToolProvider(false, true).create(null),
                new StateTransitionCompartmentNodeToolProvider(true, true).create(null),
                new StateTransitionActionCompartmentToolProvider(SysmlPackage.eINSTANCE.getStateUsage_EntryAction()).create(null),
                new StateTransitionActionCompartmentToolProvider(SysmlPackage.eINSTANCE.getStateUsage_DoAction()).create(null),
                new StateTransitionActionCompartmentToolProvider(SysmlPackage.eINSTANCE.getStateUsage_ExitAction()).create(null));
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        var editSection = this.toolDescriptionService.buildEditSection(
                new StateTransitionToggleExhibitStateToolProvider(true).create(null),
                new StateTransitionToggleExhibitStateToolProvider(false).create(null));
        return List.of(createSection, editSection, this.toolDescriptionService.addElementsNodeToolSection(true));
    }

    @Override
    public List<NodeToolSection> caseUsage(Usage object) {
        var createSection = this.toolDescriptionService.buildCreateSection();
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.toolDescriptionService.addElementsNodeToolSection(true));
    }

    @Override
    public List<NodeToolSection> caseUseCaseDefinition(UseCaseDefinition object) {
        var createSection = this.toolDescriptionService.buildCreateSection(this.createPartUsageAsSubjectNodeTool(), this.createRequirementUsageAsObjectiveRequirementNodeTool());
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.toolDescriptionService.addElementsNodeToolSection(true));
    }

    @Override
    public List<NodeToolSection> caseUseCaseUsage(UseCaseUsage object) {
        var createSection = this.toolDescriptionService.buildCreateSection(
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getAttributeUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getItemUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPartUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPortUsage()),
                this.createPartUsageAsSubjectNodeTool(),
                this.createRequirementUsageAsObjectiveRequirementNodeTool());
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.toolDescriptionService.addElementsNodeToolSection(true));
    }

    private NodeTool createPartUsageAsSubjectNodeTool() {
        var subjectCompartmentNodeToolProvider = new SubjectCompartmentNodeToolProvider();
        return subjectCompartmentNodeToolProvider.create(null);
    }

    private NodeTool createRequirementUsageAsObjectiveRequirementNodeTool() {
        var objectiveRequirementCompartmentNodeToolProvider = new ObjectiveRequirementCompartmentNodeToolProvider();
        return objectiveRequirementCompartmentNodeToolProvider.create(null);
    }

    private NodeToolSection createPartDefinitionElementsToolSection() {
        return this.toolDescriptionService.buildCreateSection(
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPartUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getItemUsage()));
    }

    private NodeToolSection createPartUsageElementsToolSection() {
        return this.toolDescriptionService.buildCreateSection(
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getItemUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPartUsage()));
    }

    private NodeTool createPayloadNodeTool(EClass payloadType) {
        var payloadNodeToolProvider = new AcceptActionPayloadNodeToolProvider(payloadType, new GVDescriptionNameGenerator());
        return payloadNodeToolProvider.create(null);
    }

    private NodeTool createPortUsageAsReceiverNodeTool() {
        var newPortAsReceiverToolProvider = new AcceptActionPortUsageReceiverToolNodeProvider();
        return newPortAsReceiverToolProvider.create(null);
    }

    @Override
    protected List<NodeTool> createToolsForCompartmentItem(EReference eReference) {
        List<NodeTool> compartmentNodeTools = new ArrayList<>();
        compartmentNodeTools.add(new CompartmentNodeToolProvider(eReference, this.descriptionNameGenerator).create(this.cache));
        if (GeneralViewDiagramDescriptionProvider.DIRECTIONAL_ELEMENTS.contains(eReference.getEType())) {
            // the element inside the compartment is a directional element, we need to add 3 more tools for each direction
            compartmentNodeTools.add(new CompartmentNodeToolProvider(eReference, this.descriptionNameGenerator, FeatureDirectionKind.IN).create(this.cache));
            compartmentNodeTools.add(new CompartmentNodeToolProvider(eReference, this.descriptionNameGenerator, FeatureDirectionKind.INOUT).create(this.cache));
            compartmentNodeTools.add(new CompartmentNodeToolProvider(eReference, this.descriptionNameGenerator, FeatureDirectionKind.OUT).create(this.cache));
        }
        return compartmentNodeTools;
    }
}
