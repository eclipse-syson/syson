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
import org.eclipse.syson.diagram.common.view.tools.PerformActionNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ReferencingPerformActionNodeToolProvider;
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

    private final ToolDescriptionService toolDescriptionService;

    public ActionFlowViewNodeToolSectionSwitch(IViewDiagramElementFinder cache, List<NodeDescription> allNodeDescriptions) {
        super(new AFVDescriptionNameGenerator());
        this.cache = Objects.requireNonNull(cache);
        this.allNodeDescriptions = Objects.requireNonNull(allNodeDescriptions);
        this.toolDescriptionService = new ToolDescriptionService(this.descriptionNameGenerator);
    }

    @Override
    protected List<EReference> getElementCompartmentReferences(Element element) {
        List<EReference> refs = ActionFlowViewDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.get(element.eClass());
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
        var createSection = this.toolDescriptionService.buildCreateSection(new StartActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new DoneActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new JoinActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new ForkActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new MergeActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new DecisionActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new AcceptActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new ActionFlowCompartmentNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new CompartmentNodeToolProvider(SysmlPackage.eINSTANCE.getElement_Documentation(), this.descriptionNameGenerator).create(this.cache),
                new AssignmentActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new ReferencingPerformActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache),
                new PerformActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.descriptionNameGenerator).create(this.cache));
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItem(SysmlPackage.eINSTANCE.getUsage_NestedItem()));
        return List.of(createSection, this.toolDescriptionService.addElementsNodeToolSection(true));
    }

    @Override
    public List<NodeToolSection> caseActionDefinition(ActionDefinition object) {
        var createSection = this.toolDescriptionService.buildCreateSection(
                new StartActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache),
                new DoneActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache),
                new JoinActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache),
                new ForkActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache),
                new MergeActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache),
                new DecisionActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache),
                new ActionFlowCompartmentNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache),
                new CompartmentNodeToolProvider(SysmlPackage.eINSTANCE.getElement_Documentation(), this.descriptionNameGenerator).create(this.cache),
                new AcceptActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache),
                new AssignmentActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache),
                new ReferencingPerformActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache),
                new PerformActionNodeToolProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.descriptionNameGenerator).create(this.cache));
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItem(SysmlPackage.eINSTANCE.getDefinition_OwnedItem()));
        return List.of(createSection, this.toolDescriptionService.addElementsNodeToolSection(true));
    }

    @Override
    public List<NodeToolSection> caseAssignmentActionUsage(AssignmentActionUsage object) {
        // Define here the set of node tools that should be added to the Assignment action palette,
        // such as "Set assigned element" and "Set value".
        return List.of(this.toolDescriptionService.addElementsNodeToolSection(true));
    }

    @Override
    public List<NodeToolSection> caseDefinition(Definition object) {
        var createSection = this.toolDescriptionService.buildCreateSection();
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection);
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
    public List<NodeToolSection> caseUsage(Usage object) {
        var createSection = this.toolDescriptionService.buildCreateSection();
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.toolDescriptionService.addElementsNodeToolSection(true));
    }

    @Override
    protected List<NodeTool> createToolsForCompartmentItem(EReference eReference) {
        List<NodeTool> compartmentNodeTools = new ArrayList<>();
        compartmentNodeTools.add(new CompartmentNodeToolProvider(eReference, this.descriptionNameGenerator).create(this.cache));
        if (ActionFlowViewDiagramDescriptionProvider.DIRECTIONAL_ELEMENTS.contains(eReference.getEType())) {
            // the element inside the compartment is a directional element, we need to add 3 more tools for each
            // direction
            compartmentNodeTools.add(new CompartmentNodeToolProvider(eReference, this.descriptionNameGenerator, FeatureDirectionKind.IN).create(this.cache));
            compartmentNodeTools.add(new CompartmentNodeToolProvider(eReference, this.descriptionNameGenerator, FeatureDirectionKind.INOUT).create(this.cache));
            compartmentNodeTools.add(new CompartmentNodeToolProvider(eReference, this.descriptionNameGenerator, FeatureDirectionKind.OUT).create(this.cache));
        }
        return compartmentNodeTools;
    }

    private NodeTool createPayloadNodeTool(EClass payloadType) {
        var payloadNodeToolProvider = new AcceptActionPayloadNodeToolProvider(payloadType, new AFVDescriptionNameGenerator());
        return payloadNodeToolProvider.create(null);
    }

    private NodeTool createPortUsageAsReceiverNodeTool() {
        var newPortAsReceiverToolProvider = new AcceptActionPortUsageReceiverToolNodeProvider();
        return newPortAsReceiverToolProvider.create(null);
    }
}
