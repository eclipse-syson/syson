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
package org.eclipse.syson.diagram.actionflow.view.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.syson.diagram.actionflow.view.AFVDescriptionNameGenerator;
import org.eclipse.syson.diagram.actionflow.view.ActionFlowViewDiagramDescriptionProvider;
import org.eclipse.syson.diagram.common.view.services.AbstractViewNodeToolSectionSwitch;
import org.eclipse.syson.diagram.common.view.services.description.ToolConstants;
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
import org.eclipse.syson.diagram.common.view.tools.PerformActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ReferencingPerformActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.SetAsCompositeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.SetAsRefToolProvider;
import org.eclipse.syson.diagram.common.view.tools.StartActionNodeToolProvider;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.AssignmentActionUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.PerformActionUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * Switch retrieving the list of NodeToolSections for each SysMLv2 concept represented in the Action Flow View diagram.
 *
 * @author Jerome Gout
 */
public class ActionFlowViewNodeToolSectionSwitch extends AbstractViewNodeToolSectionSwitch {

    private final List<NodeDescription> allNodeDescriptions;

    private final IViewDiagramElementFinder cache;

    public ActionFlowViewNodeToolSectionSwitch(IViewDiagramElementFinder cache, List<NodeDescription> allNodeDescriptions) {
        super(new AFVDescriptionNameGenerator());
        this.cache = Objects.requireNonNull(cache);
        this.allNodeDescriptions = Objects.requireNonNull(allNodeDescriptions);
    }

    @Override
    public List<NodeToolSection> caseAcceptActionUsage(AcceptActionUsage object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.createPayloadNodeTool(SysmlPackage.eINSTANCE.getItemDefinition()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.createPayloadNodeTool(SysmlPackage.eINSTANCE.getPartDefinition()));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, this.createPortUsageAsReceiverNodeTool());
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsCompositeToolProvider().create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.STRUCTURE, new SetAsRefToolProvider().create(this.cache));
        this.createToolsForCompartmentItems(object, sections, this.cache);
        sections.add(this.toolDescriptionService.relatedElementsNodeToolSection(true));
        this.toolDescriptionService.removeEmptyNodeToolSections(sections);
        return sections;
    }

    @Override
    public List<NodeToolSection> caseActionUsage(ActionUsage object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
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
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new ActionFlowCompartmentNodeToolProvider().create(this.cache));
        this.createToolsForCompartmentItem(SysmlPackage.eINSTANCE.getDefinition_OwnedItem(), sections, this.cache);
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
    public List<NodeToolSection> caseDefinition(Definition object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.createToolsForCompartmentItems(object, sections, this.cache);
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
    protected void createToolsForCompartmentItem(EReference eReference, List<NodeToolSection> sections, IViewDiagramElementFinder finder) {
        var eType = eReference.getEType();
        var toolSectionName = this.toolDescriptionService.getToolSectionName(eType);
        var provider = new CompartmentNodeToolProvider(eReference, this.descriptionNameGenerator);
        this.toolDescriptionService.addNodeTool(sections, toolSectionName, provider.create(finder));
        if (ActionFlowViewDiagramDescriptionProvider.DIRECTIONAL_ELEMENTS.contains(eReference.getEType())) {
            // the element inside the compartment is a directional element, we need to add 3 more tools for each
            // direction
            this.toolDescriptionService.addNodeTool(sections, toolSectionName, new CompartmentNodeToolProvider(eReference, this.descriptionNameGenerator, FeatureDirectionKind.IN).create(finder));
            this.toolDescriptionService.addNodeTool(sections, toolSectionName,
                    new CompartmentNodeToolProvider(eReference, this.descriptionNameGenerator, FeatureDirectionKind.INOUT).create(finder));
            this.toolDescriptionService.addNodeTool(sections, toolSectionName, new CompartmentNodeToolProvider(eReference, this.descriptionNameGenerator, FeatureDirectionKind.OUT).create(finder));
        }
    }

    @Override
    protected List<EReference> getElementCompartmentReferences(Element element) {
        List<EReference> compartmentRefs = new ArrayList<>();
        List<EReference> refs = ActionFlowViewDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.get(element.eClass());
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

    private NodeTool createPayloadNodeTool(EClass payloadType) {
        var payloadNodeToolProvider = new AcceptActionPayloadNodeToolProvider(payloadType, new AFVDescriptionNameGenerator());
        return payloadNodeToolProvider.create(this.cache);
    }

    private NodeTool createPortUsageAsReceiverNodeTool() {
        var newPortAsReceiverToolProvider = new AcceptActionPortUsageReceiverToolNodeProvider();
        return newPortAsReceiverToolProvider.create(this.cache);
    }
}
