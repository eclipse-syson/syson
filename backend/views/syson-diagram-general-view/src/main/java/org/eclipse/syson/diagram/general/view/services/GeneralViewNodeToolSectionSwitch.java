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

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.syson.diagram.common.view.services.AbstractViewNodeToolSectionSwitch;
import org.eclipse.syson.diagram.common.view.tools.AcceptActionPayloadNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.AcceptActionPortUsageReceiverToolNodeProvider;
import org.eclipse.syson.diagram.common.view.tools.ActionFlowCompartmentNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.CompartmentNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.DoneActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ForkActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.JoinActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ObjectiveRequirementCompartmentNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.StartActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.SubjectCompartmentNodeToolProvider;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.InterfaceDefinition;
import org.eclipse.syson.sysml.ItemDefinition;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.OccurrenceDefinition;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.RequirementDefinition;
import org.eclipse.syson.sysml.RequirementUsage;
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

    public GeneralViewNodeToolSectionSwitch(IViewDiagramElementFinder cache, List<NodeDescription> allNodeDescriptions) {
        super(new GVDescriptionNameGenerator());
        this.cache = Objects.requireNonNull(cache);
        this.allNodeDescriptions = Objects.requireNonNull(allNodeDescriptions);
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
        var createSection = this.buildCreateSection(
                this.createPayloadNodeTool(SysmlPackage.eINSTANCE.getItemDefinition()),
                this.createPayloadNodeTool(SysmlPackage.eINSTANCE.getPartDefinition()),
                this.createPortUsageAsReceiverNodeTool());
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> caseActionUsage(ActionUsage object) {
        var createSection = this.buildCreateSection(
                new StartActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new DoneActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new JoinActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new ForkActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getAttributeUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPartUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPortUsage()));
        createSection.getNodeTools().add(new CompartmentNodeToolProvider(SysmlPackage.eINSTANCE.getUsage_NestedItem(), this.descriptionNameGenerator).create(null));
        createSection.getNodeTools().add(new ActionFlowCompartmentNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(null));
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> caseActionDefinition(ActionDefinition object) {
        var createSection = this.buildCreateSection(new StartActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache));
        createSection.getNodeTools().add(new DoneActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache));
        createSection.getNodeTools().add(new JoinActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache));
        createSection.getNodeTools().add(new ForkActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache));
        createSection.getNodeTools().add(new ActionFlowCompartmentNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(null));
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> caseConstraintUsage(ConstraintUsage object) {
        var createSection = this.buildCreateSection(
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getAttributeUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getItemUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPartUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPortUsage()));
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> caseDefinition(Definition object) {
        var createSection = this.buildCreateSection();
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection);
    }

    @Override
    public List<NodeToolSection> caseInterfaceDefinition(InterfaceDefinition object) {
        var createSection = this.buildCreateSection();
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection);
    }

    @Override
    public List<NodeToolSection> caseItemDefinition(ItemDefinition object) {
        var createSection = this.buildCreateSection(this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getItemUsage()));
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> caseItemUsage(ItemUsage object) {
        var createSection = this.buildCreateSection(this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getItemUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPartUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPortUsage()));
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> caseOccurrenceDefinition(OccurrenceDefinition object) {
        var createSection = this.buildCreateSection(this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPartUsage()));
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> casePartDefinition(PartDefinition object) {
        var createSection = this.createPartDefinitionElementsToolSection();
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> casePartUsage(PartUsage object) {
        var createSection = this.createPartUsageElementsToolSection();
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> casePortUsage(PortUsage object) {
        var createSection = this.buildCreateSection(this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPortUsage()));
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> caseRequirementUsage(RequirementUsage object) {
        var createSection = this.buildCreateSection(
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getItemUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPartUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPortUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getRequirementUsage()),
                this.createPartUsageAsSubjectNodeTool());
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> caseRequirementDefinition(RequirementDefinition object) {
        var createSection = this.buildCreateSection(this.createPartUsageAsSubjectNodeTool());
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> caseUsage(Usage object) {
        var createSection = this.buildCreateSection();
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> caseUseCaseDefinition(UseCaseDefinition object) {
        var createSection = this.buildCreateSection(this.createPartUsageAsSubjectNodeTool(), this.createRequirementUsageAsObjectiveRequirementNodeTool());
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> caseUseCaseUsage(UseCaseUsage object) {
        var createSection = this.buildCreateSection(
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getAttributeUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getItemUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPartUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPortUsage()),
                this.createPartUsageAsSubjectNodeTool(),
                this.createRequirementUsageAsObjectiveRequirementNodeTool());
        return List.of(createSection, this.addElementsToolSection());
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
        return this.buildCreateSection(
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getPartUsage()),
                this.createNestedUsageNodeTool(SysmlPackage.eINSTANCE.getItemUsage()));
    }

    private NodeToolSection createPartUsageElementsToolSection() {
        return this.buildCreateSection(
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
}
