/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
import org.eclipse.syson.diagram.common.view.services.description.ToolConstants;
import org.eclipse.syson.diagram.common.view.tools.AcceptActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.AcceptActionPayloadNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.AcceptActionPortUsageReceiverToolNodeProvider;
import org.eclipse.syson.diagram.common.view.tools.ActionFlowCompartmentNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ActorCompartmentNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.AssignmentActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.CompartmentNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.DecisionActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.DoneActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ExhibitStateWithReferenceNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ForkActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.JoinActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.MergeActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ObjectiveRequirementCompartmentNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ObjectiveRequirementWithBaseRequirementCompartmentNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.PartUsageFeatureTypingNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.PartUsageSubsettingNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.PerformActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ReferencingPerformActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.SetAsCompositeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.SetAsRefToolProvider;
import org.eclipse.syson.diagram.common.view.tools.StakeholdersCompartmentNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.StartActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.StateSubactionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.StateTransitionCompartmentNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.SubjectCompartmentNodeToolProvider;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.AssignmentActionUsage;
import org.eclipse.syson.sysml.CaseDefinition;
import org.eclipse.syson.sysml.CaseUsage;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.InterfaceDefinition;
import org.eclipse.syson.sysml.ItemDefinition;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.OccurrenceDefinition;
import org.eclipse.syson.sysml.OccurrenceUsage;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PerformActionUsage;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.RequirementDefinition;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateSubactionKind;
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

    private final IViewDiagramElementFinder cache;

    private final List<NodeDescription> allNodeDescriptions;

    private final UtilService utilService;

    public GeneralViewNodeToolSectionSwitch(IViewDiagramElementFinder cache, List<NodeDescription> allNodeDescriptions) {
        super(new GVDescriptionNameGenerator());
        this.cache = Objects.requireNonNull(cache);
        this.allNodeDescriptions = Objects.requireNonNull(allNodeDescriptions);
        this.utilService = new UtilService();
    }

    @Override
    public List<NodeToolSection> caseAcceptActionUsage(AcceptActionUsage object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.createPayloadNodeTool(SysmlPackage.eINSTANCE.getItemDefinition()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.createPayloadNodeTool(SysmlPackage.eINSTANCE.getPartDefinition()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.createPortUsageAsReceiverNodeTool());
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsCompositeToolProvider().create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsRefToolProvider().create(this.cache));
        sections.add(this.toolDescriptionService.relatedElementsNodeToolSection(true));
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    public List<NodeToolSection> caseActionUsage(ActionUsage object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getAttributeUsage()), SysmlPackage.eINSTANCE.getAttributeUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getPartUsage()), SysmlPackage.eINSTANCE.getPartUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage(), null, FeatureDirectionKind.IN));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage(), null, FeatureDirectionKind.INOUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage(), null, FeatureDirectionKind.OUT));
        this.createToolsForCompartmentItem(SysmlPackage.eINSTANCE.getUsage_NestedItem(), sections, this.cache);
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new StartActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new DoneActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new JoinActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new ForkActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new MergeActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new DecisionActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new AcceptActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new AssignmentActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new ReferencingPerformActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new PerformActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new ActionFlowCompartmentNodeToolProvider().create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsCompositeToolProvider().create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsRefToolProvider().create(this.cache));
        sections.add(this.toolDescriptionService.relatedElementsNodeToolSection(true));
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    public List<NodeToolSection> caseActionDefinition(ActionDefinition object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.IN));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.INOUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.OUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new StartActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new DoneActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new JoinActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new ForkActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new MergeActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new DecisionActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new AcceptActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new AssignmentActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new ReferencingPerformActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new PerformActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache));
        if (!(object instanceof StateDefinition)) {
            // StateDefinition has its own "action" creation tools
            this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new ActionFlowCompartmentNodeToolProvider().create(this.cache));
        }
        sections.add(this.toolDescriptionService.relatedElementsNodeToolSection(true));
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    public List<NodeToolSection> caseAssignmentActionUsage(AssignmentActionUsage object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsCompositeToolProvider().create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsRefToolProvider().create(this.cache));
        sections.add(this.toolDescriptionService.relatedElementsNodeToolSection(true));
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    public List<NodeToolSection> caseCaseDefinition(CaseDefinition object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.createNewSubjectNodeTool());
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.createNewActorNodeTool());
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.REQUIREMENTS, this.createRequirementUsageAsObjectiveRequirementNodeTool());
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.REQUIREMENTS, this.creatRequirementUsageAsObjectiveWithBaseRequirementNodeTool());
        this.createToolsForCompartmentItems(object, sections, this.cache);
        sections.add(this.toolDescriptionService.relatedElementsNodeToolSection(true));
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    public List<NodeToolSection> caseCaseUsage(CaseUsage object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getAttributeUsage()), SysmlPackage.eINSTANCE.getAttributeUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.IN));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.INOUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.OUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getPartUsage()), SysmlPackage.eINSTANCE.getPartUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage(), null, FeatureDirectionKind.IN));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage(), null, FeatureDirectionKind.INOUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage(), null, FeatureDirectionKind.OUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.createNewSubjectNodeTool());
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.createNewActorNodeTool());
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.REQUIREMENTS, this.createRequirementUsageAsObjectiveRequirementNodeTool());
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.REQUIREMENTS, this.creatRequirementUsageAsObjectiveWithBaseRequirementNodeTool());
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsCompositeToolProvider().create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsRefToolProvider().create(this.cache));
        this.createToolsForCompartmentItems(object, sections, this.cache);
        sections.add(this.toolDescriptionService.relatedElementsNodeToolSection(true));
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    public List<NodeToolSection> caseConstraintUsage(ConstraintUsage object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getAttributeUsage()), SysmlPackage.eINSTANCE.getAttributeUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.IN));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.INOUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.OUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getPartUsage()), SysmlPackage.eINSTANCE.getPartUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage(), null, FeatureDirectionKind.IN));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage(), null, FeatureDirectionKind.INOUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage(), null, FeatureDirectionKind.OUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsCompositeToolProvider().create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsRefToolProvider().create(this.cache));
        this.createToolsForCompartmentItems(object, sections, this.cache);
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    public List<NodeToolSection> caseDefinition(Definition object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.createToolsForCompartmentItems(object, sections, this.cache);
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    public List<NodeToolSection> caseInterfaceDefinition(InterfaceDefinition object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.createToolsForCompartmentItems(object, sections, this.cache);
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    public List<NodeToolSection> caseItemDefinition(ItemDefinition object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.IN));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.INOUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.OUT));
        this.createToolsForCompartmentItems(object, sections, this.cache);
        sections.add(this.toolDescriptionService.relatedElementsNodeToolSection(true));
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    public List<NodeToolSection> caseItemUsage(ItemUsage object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.IN));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.INOUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.OUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getPartUsage()), SysmlPackage.eINSTANCE.getPartUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage(), null, FeatureDirectionKind.IN));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage(), null, FeatureDirectionKind.INOUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage(), null, FeatureDirectionKind.OUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsCompositeToolProvider().create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsRefToolProvider().create(this.cache));
        this.createToolsForCompartmentItems(object, sections, this.cache);
        sections.add(this.toolDescriptionService.relatedElementsNodeToolSection(true));
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    public List<NodeToolSection> caseOccurrenceDefinition(OccurrenceDefinition object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getPartUsage()), SysmlPackage.eINSTANCE.getPartUsage()));
        this.createToolsForCompartmentItems(object, sections, this.cache);
        sections.add(this.toolDescriptionService.relatedElementsNodeToolSection(true));
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    public List<NodeToolSection> caseOccurrenceUsage(OccurrenceUsage object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.createToolsForCompartmentItems(object, sections, this.cache);
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsCompositeToolProvider().create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsRefToolProvider().create(this.cache));
        sections.add(this.toolDescriptionService.relatedElementsNodeToolSection(true));
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    public List<NodeToolSection> casePartDefinition(PartDefinition object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();

        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.IN));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.INOUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.OUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getPartUsage()), SysmlPackage.eINSTANCE.getPartUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new StartActionNodeToolProvider(SysmlPackage.eINSTANCE.getPartDefinition(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new DoneActionNodeToolProvider(SysmlPackage.eINSTANCE.getPartDefinition(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new JoinActionNodeToolProvider(SysmlPackage.eINSTANCE.getPartDefinition(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new ForkActionNodeToolProvider(SysmlPackage.eINSTANCE.getPartDefinition(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new MergeActionNodeToolProvider(SysmlPackage.eINSTANCE.getPartDefinition(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new DecisionActionNodeToolProvider(SysmlPackage.eINSTANCE.getPartDefinition(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new AcceptActionNodeToolProvider(SysmlPackage.eINSTANCE.getPartDefinition(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new ActionFlowCompartmentNodeToolProvider().create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new AssignmentActionNodeToolProvider(SysmlPackage.eINSTANCE.getPartDefinition(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new ReferencingPerformActionNodeToolProvider(SysmlPackage.eINSTANCE.getPartDefinition(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new PerformActionNodeToolProvider(SysmlPackage.eINSTANCE.getPartDefinition(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new ExhibitStateWithReferenceNodeToolProvider(this.descriptionNameGenerator).create(this.cache));
        this.createToolsForCompartmentItems(object, sections, this.cache);
        this.toolDescriptionService.getNodeToolSection(sections, ToolConstants.BEHAVIOR).ifPresent(nts -> {
            // Remove New Action tool, we use a custom New Action tool from ActionFlowCompartmentNodeToolProvider
            nts.getNodeTools().removeIf(nodeTool -> Objects.equals(nodeTool.getName(), "New Action"));
        });
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new ActionFlowCompartmentNodeToolProvider().create(this.cache));
        this.toolDescriptionService.getNodeToolSection(sections, ToolConstants.BEHAVIOR).ifPresent(nts -> {
            // Remove New Action tool, we use a custom New Action tool from ActionFlowCompartmentNodeToolProvider
            nts.getNodeTools().removeIf(nodeTool -> Objects.equals(nodeTool.getName(), "New State"));
        });
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateTransitionCompartmentNodeToolProvider(false, false).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateTransitionCompartmentNodeToolProvider(true, false).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateTransitionCompartmentNodeToolProvider(false, true).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateTransitionCompartmentNodeToolProvider(true, true).create(this.cache));
        sections.add(this.toolDescriptionService.relatedElementsNodeToolSection(true));
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    public List<NodeToolSection> casePartUsage(PartUsage object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.IN));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.INOUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.OUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getPartUsage()), SysmlPackage.eINSTANCE.getPartUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new StartActionNodeToolProvider(SysmlPackage.eINSTANCE.getPartUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new DoneActionNodeToolProvider(SysmlPackage.eINSTANCE.getPartUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new JoinActionNodeToolProvider(SysmlPackage.eINSTANCE.getPartUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new ForkActionNodeToolProvider(SysmlPackage.eINSTANCE.getPartUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new MergeActionNodeToolProvider(SysmlPackage.eINSTANCE.getPartUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new DecisionActionNodeToolProvider(SysmlPackage.eINSTANCE.getPartUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new AcceptActionNodeToolProvider(SysmlPackage.eINSTANCE.getPartUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new ActionFlowCompartmentNodeToolProvider().create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new AssignmentActionNodeToolProvider(SysmlPackage.eINSTANCE.getPartUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new ReferencingPerformActionNodeToolProvider(SysmlPackage.eINSTANCE.getPartUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new PerformActionNodeToolProvider(SysmlPackage.eINSTANCE.getPartUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new ExhibitStateWithReferenceNodeToolProvider(this.descriptionNameGenerator).create(this.cache));
        this.createToolsForCompartmentItems(object, sections, this.cache);
        this.toolDescriptionService.getNodeToolSection(sections, ToolConstants.BEHAVIOR).ifPresent(nts -> {
            // Remove New Action tool, we use a custom New Action tool from ActionFlowCompartmentNodeToolProvider
            nts.getNodeTools().removeIf(nodeTool -> Objects.equals(nodeTool.getName(), "New Action"));
        });
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new ActionFlowCompartmentNodeToolProvider().create(this.cache));
        this.toolDescriptionService.getNodeToolSection(sections, ToolConstants.BEHAVIOR).ifPresent(nts -> {
            // Remove New Action tool, we use a custom New Action tool from ActionFlowCompartmentNodeToolProvider
            nts.getNodeTools().removeIf(nodeTool -> Objects.equals(nodeTool.getName(), "New State"));
        });
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateTransitionCompartmentNodeToolProvider(false, false).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateTransitionCompartmentNodeToolProvider(true, false).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateTransitionCompartmentNodeToolProvider(false, true).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateTransitionCompartmentNodeToolProvider(true, true).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new PartUsageSubsettingNodeToolProvider(this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.getNodeToolSection(sections, ToolConstants.STRUCTURE).ifPresent(nts -> {
            this.createPartUsageFeatureTypingToolNode(object, nts);
        });
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsCompositeToolProvider().create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsRefToolProvider().create(this.cache));
        sections.add(this.toolDescriptionService.relatedElementsNodeToolSection(true));
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    public List<NodeToolSection> casePerformActionUsage(PerformActionUsage object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new StartActionNodeToolProvider(SysmlPackage.eINSTANCE.getPerformActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new DoneActionNodeToolProvider(SysmlPackage.eINSTANCE.getPerformActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new JoinActionNodeToolProvider(SysmlPackage.eINSTANCE.getPerformActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new ForkActionNodeToolProvider(SysmlPackage.eINSTANCE.getPerformActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new MergeActionNodeToolProvider(SysmlPackage.eINSTANCE.getPerformActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new DecisionActionNodeToolProvider(SysmlPackage.eINSTANCE.getPerformActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new AcceptActionNodeToolProvider(SysmlPackage.eINSTANCE.getPerformActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new ActionFlowCompartmentNodeToolProvider().create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new AssignmentActionNodeToolProvider(SysmlPackage.eINSTANCE.getPerformActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new ReferencingPerformActionNodeToolProvider(SysmlPackage.eINSTANCE.getPerformActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new PerformActionNodeToolProvider(SysmlPackage.eINSTANCE.getPerformActionUsage(), this.descriptionNameGenerator).create(this.cache));
        this.createToolsForCompartmentItem(SysmlPackage.eINSTANCE.getUsage_NestedItem(), sections, this.cache);
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsCompositeToolProvider().create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsRefToolProvider().create(this.cache));
        sections.add(this.toolDescriptionService.relatedElementsNodeToolSection(true));
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    public List<NodeToolSection> casePortDefinition(PortDefinition object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getPartUsage()), SysmlPackage.eINSTANCE.getPartUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.IN));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.INOUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.OUT));
        this.createToolsForCompartmentItems(object, sections, this.cache);
        sections.add(this.toolDescriptionService.relatedElementsNodeToolSection(true));
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    public List<NodeToolSection> casePortUsage(PortUsage object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.IN));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.INOUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.OUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getPartUsage()), SysmlPackage.eINSTANCE.getPartUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage(), null, FeatureDirectionKind.IN));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage(), null, FeatureDirectionKind.INOUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage(), null, FeatureDirectionKind.OUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsCompositeToolProvider().create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsRefToolProvider().create(this.cache));
        this.createToolsForCompartmentItems(object, sections, this.cache);
        sections.add(this.toolDescriptionService.relatedElementsNodeToolSection(true));
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    public List<NodeToolSection> caseRequirementUsage(RequirementUsage object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.IN));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.INOUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.OUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getPartUsage()), SysmlPackage.eINSTANCE.getPartUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage(), null, FeatureDirectionKind.IN));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage(), null, FeatureDirectionKind.INOUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage(), null, FeatureDirectionKind.OUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.REQUIREMENTS,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getRequirementUsage()), SysmlPackage.eINSTANCE.getRequirementUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.createNewSubjectNodeTool());
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.createNewActorNodeTool());
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.createNewStakeholderNodeTool());
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsCompositeToolProvider().create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsRefToolProvider().create(this.cache));
        this.createToolsForCompartmentItems(object, sections, this.cache);
        sections.add(this.toolDescriptionService.relatedElementsNodeToolSection(true));
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    public List<NodeToolSection> caseRequirementDefinition(RequirementDefinition object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.createNewSubjectNodeTool());
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.createNewActorNodeTool());
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.createNewStakeholderNodeTool());
        this.createToolsForCompartmentItems(object, sections, this.cache);
        sections.add(this.toolDescriptionService.relatedElementsNodeToolSection(true));
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    public List<NodeToolSection> caseStateDefinition(StateDefinition object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateTransitionCompartmentNodeToolProvider(false, false).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateTransitionCompartmentNodeToolProvider(true, false).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateTransitionCompartmentNodeToolProvider(false, true).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateTransitionCompartmentNodeToolProvider(true, true).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new ExhibitStateWithReferenceNodeToolProvider(this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateSubactionNodeToolProvider(StateSubactionKind.ENTRY, true).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateSubactionNodeToolProvider(StateSubactionKind.ENTRY, false).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateSubactionNodeToolProvider(StateSubactionKind.DO, true).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateSubactionNodeToolProvider(StateSubactionKind.DO, false).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateSubactionNodeToolProvider(StateSubactionKind.EXIT, true).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateSubactionNodeToolProvider(StateSubactionKind.EXIT, false).create(this.cache));
        sections.add(this.toolDescriptionService.relatedElementsNodeToolSection(true));
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    public List<NodeToolSection> caseStateUsage(StateUsage object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateTransitionCompartmentNodeToolProvider(false, false).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateTransitionCompartmentNodeToolProvider(true, false).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateTransitionCompartmentNodeToolProvider(false, true).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateTransitionCompartmentNodeToolProvider(true, true).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new ExhibitStateWithReferenceNodeToolProvider(this.descriptionNameGenerator).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateSubactionNodeToolProvider(StateSubactionKind.ENTRY, true).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateSubactionNodeToolProvider(StateSubactionKind.ENTRY, false).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateSubactionNodeToolProvider(StateSubactionKind.DO, true).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateSubactionNodeToolProvider(StateSubactionKind.DO, false).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateSubactionNodeToolProvider(StateSubactionKind.EXIT, true).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new StateSubactionNodeToolProvider(StateSubactionKind.EXIT, false).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsCompositeToolProvider().create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsRefToolProvider().create(this.cache));
        sections.add(this.toolDescriptionService.relatedElementsNodeToolSection(true));
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    public List<NodeToolSection> caseUsage(Usage object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsCompositeToolProvider().create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsRefToolProvider().create(this.cache));
        this.createToolsForCompartmentItems(object, sections, this.cache);
        sections.add(this.toolDescriptionService.relatedElementsNodeToolSection(true));
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    public List<NodeToolSection> caseUseCaseDefinition(UseCaseDefinition object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.createNewSubjectNodeTool());
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.createNewActorNodeTool());
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.REQUIREMENTS, this.createRequirementUsageAsObjectiveRequirementNodeTool());
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.REQUIREMENTS, this.creatRequirementUsageAsObjectiveWithBaseRequirementNodeTool());
        this.createToolsForCompartmentItems(object, sections, this.cache);
        sections.add(this.toolDescriptionService.relatedElementsNodeToolSection(true));
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    public List<NodeToolSection> caseUseCaseUsage(UseCaseUsage object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getAttributeUsage()), SysmlPackage.eINSTANCE.getAttributeUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.IN));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.INOUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getItemUsage()), SysmlPackage.eINSTANCE.getItemUsage(), null, FeatureDirectionKind.OUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getPartUsage()), SysmlPackage.eINSTANCE.getPartUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE,
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage(), null, FeatureDirectionKind.IN));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage(), null, FeatureDirectionKind.INOUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.toolDescriptionService
                .createNodeToolWithDirection(this.getNodeDescription(SysmlPackage.eINSTANCE.getPortUsage()), SysmlPackage.eINSTANCE.getPortUsage(), null, FeatureDirectionKind.OUT));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.createNewSubjectNodeTool());
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.createNewActorNodeTool());
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.REQUIREMENTS, this.createRequirementUsageAsObjectiveRequirementNodeTool());
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.REQUIREMENTS, this.creatRequirementUsageAsObjectiveWithBaseRequirementNodeTool());
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsCompositeToolProvider().create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsRefToolProvider().create(this.cache));
        this.createToolsForCompartmentItems(object, sections, this.cache);
        sections.add(this.toolDescriptionService.relatedElementsNodeToolSection(true));
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    protected List<EReference> getElementCompartmentReferences(Element element) {
        List<EReference> compartmentRefs = new ArrayList<>();
        List<EReference> refs = GeneralViewDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.get(element.eClass());
        if (refs != null) {
            compartmentRefs.addAll(refs);
        }
        compartmentRefs.removeIf(ref -> Objects.equals(ref, SysmlPackage.eINSTANCE.getElement_Documentation()));
        return compartmentRefs;
    }

    @Override
    protected List<NodeDescription> getAllNodeDescriptions() {
        return this.allNodeDescriptions;
    }

    @Override
    protected void createToolsForCompartmentItem(EReference eReference, List<NodeToolSection> sections, IViewDiagramElementFinder finder) {
        var eType = eReference.getEType();
        var toolSectionName = this.toolDescriptionService.getToolSectionName(eType);
        var provider = new CompartmentNodeToolProvider(eReference, this.descriptionNameGenerator);
        this.toolDescriptionService.addNodeTool(sections, toolSectionName, provider.create(finder));
        if (GeneralViewDiagramDescriptionProvider.DIRECTIONAL_ELEMENTS.contains(eReference.getEType())) {
            // the element inside the compartment is a directional element, we need to add 3 more tools for each
            // direction
            this.toolDescriptionService.addNodeTool(sections, toolSectionName, new CompartmentNodeToolProvider(eReference, this.descriptionNameGenerator, FeatureDirectionKind.IN).create(finder));
            this.toolDescriptionService.addNodeTool(sections, toolSectionName,
                    new CompartmentNodeToolProvider(eReference, this.descriptionNameGenerator, FeatureDirectionKind.INOUT).create(finder));
            this.toolDescriptionService.addNodeTool(sections, toolSectionName, new CompartmentNodeToolProvider(eReference, this.descriptionNameGenerator, FeatureDirectionKind.OUT).create(finder));
        }
    }

    private NodeTool createNewSubjectNodeTool() {
        var subjectCompartmentNodeToolProvider = new SubjectCompartmentNodeToolProvider();
        return subjectCompartmentNodeToolProvider.create(this.cache);
    }

    private NodeTool createNewActorNodeTool() {
        var actorsCompartmentNodeToolProvider = new ActorCompartmentNodeToolProvider();
        return actorsCompartmentNodeToolProvider.create(this.cache);
    }

    private NodeTool createNewStakeholderNodeTool() {
        var stakeholdersCompartmentNodeToolProvider = new StakeholdersCompartmentNodeToolProvider();
        return stakeholdersCompartmentNodeToolProvider.create(this.cache);
    }

    private NodeTool createRequirementUsageAsObjectiveRequirementNodeTool() {
        var objectiveRequirementCompartmentNodeToolProvider = new ObjectiveRequirementCompartmentNodeToolProvider();
        return objectiveRequirementCompartmentNodeToolProvider.create(this.cache);
    }

    private NodeTool creatRequirementUsageAsObjectiveWithBaseRequirementNodeTool() {
        var objectiveRequirementWithBaseRequirementCompartmentNodeToolProvider = new ObjectiveRequirementWithBaseRequirementCompartmentNodeToolProvider();
        return objectiveRequirementWithBaseRequirementCompartmentNodeToolProvider.create(this.cache);
    }

    private NodeTool createPayloadNodeTool(EClass payloadType) {
        var payloadNodeToolProvider = new AcceptActionPayloadNodeToolProvider(payloadType, this.descriptionNameGenerator);
        return payloadNodeToolProvider.create(this.cache);
    }

    private NodeTool createPortUsageAsReceiverNodeTool() {
        var newPortAsReceiverToolProvider = new AcceptActionPortUsageReceiverToolNodeProvider();
        return newPortAsReceiverToolProvider.create(this.cache);
    }

    private void createPartUsageFeatureTypingToolNode(PartUsage object, NodeToolSection createSection) {
        var definitionNodeName = this.descriptionNameGenerator.getNodeName(this.utilService.getPartDefinitionEClassFrom(object));
        this.allNodeDescriptions.stream()
                .filter(nodeDesc -> Objects.equals(nodeDesc.getName(), definitionNodeName))
                .findFirst()
                .ifPresent(nodeDesc -> createSection.getNodeTools().add(new PartUsageFeatureTypingNodeToolProvider(nodeDesc, this.descriptionNameGenerator).create(this.cache)));
    }
}
