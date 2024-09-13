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
package org.eclipse.syson.diagram.statetransition.view.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.syson.diagram.common.view.services.AbstractViewNodeToolSectionSwitch;
import org.eclipse.syson.diagram.common.view.services.description.ToolDescriptionService;
import org.eclipse.syson.diagram.common.view.tools.CompartmentNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.ExhibitStateWithReferenceNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.SetAsCompositeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.SetAsRefToolProvider;
import org.eclipse.syson.diagram.common.view.tools.StateTransitionActionCompartmentToolProvider;
import org.eclipse.syson.diagram.common.view.tools.StateTransitionCompartmentNodeToolProvider;
import org.eclipse.syson.diagram.statetransition.view.StateTransitionViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Switch retrieving the list of NodeToolSections for each SysMLv2 concept represented in the State Transition View
 * diagram.
 *
 * @author adieumegard
 */
public class StateTransitionViewNodeToolSectionSwitch extends AbstractViewNodeToolSectionSwitch {

    private final IViewDiagramElementFinder cache;

    private final List<NodeDescription> allNodeDescriptions;

    private final ToolDescriptionService toolDescriptionService;

    public StateTransitionViewNodeToolSectionSwitch(IViewDiagramElementFinder cache, List<NodeDescription> allNodeDescriptions, IDescriptionNameGenerator descriptionNameGenerator) {
        super(descriptionNameGenerator);
        this.cache = Objects.requireNonNull(cache);
        this.allNodeDescriptions = Objects.requireNonNull(allNodeDescriptions);
        this.toolDescriptionService = new ToolDescriptionService(descriptionNameGenerator);
    }

    @Override
    protected List<EReference> getElementCompartmentReferences(Element element) {
        List<EReference> compartmentRefs = new ArrayList<>();
        List<EReference> refs = StateTransitionViewDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.get(element.eClass());
        if (refs != null) {
            compartmentRefs.addAll(refs);
        }
        refs = StateTransitionViewDiagramDescriptionProvider.COMPARTMENTS_WITH_MERGED_LIST_ITEMS.get(element.eClass());
        if (refs != null) {
            compartmentRefs.addAll(refs);
        }
        return compartmentRefs;
    }

    @Override
    protected List<NodeTool> createToolsForCompartmentItems(Element object) {
        List<NodeTool> compartmentNodeTools = new ArrayList<>();
        this.getElementCompartmentReferences(object).forEach(eReference -> {
            CompartmentNodeToolProvider provider = new CompartmentNodeToolProvider(eReference, this.descriptionNameGenerator);
            compartmentNodeTools.add(provider.create(this.cache));
        });
        return compartmentNodeTools;
    }

    @Override
    protected List<NodeDescription> getAllNodeDescriptions() {
        return this.allNodeDescriptions;
    }

    @Override
    public List<NodeToolSection> caseDefinition(Definition object) {
        var createSection = this.toolDescriptionService.buildCreateSection();
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection);
    }

    @Override
    public List<NodeToolSection> caseStateDefinition(StateDefinition object) {
        var createSection = this.toolDescriptionService.buildCreateSection(
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getComment()), SysmlPackage.eINSTANCE.getComment(),
                        SysmlPackage.eINSTANCE.getOwningMembership(), null),
                new StateTransitionCompartmentNodeToolProvider(false, false).create(this.cache),
                new StateTransitionCompartmentNodeToolProvider(true, false).create(this.cache),
                new StateTransitionCompartmentNodeToolProvider(false, true).create(this.cache),
                new StateTransitionCompartmentNodeToolProvider(true, true).create(this.cache),
                new StateTransitionActionCompartmentToolProvider(SysmlPackage.eINSTANCE.getStateDefinition_EntryAction()).create(this.cache),
                new StateTransitionActionCompartmentToolProvider(SysmlPackage.eINSTANCE.getStateDefinition_DoAction()).create(this.cache),
                new StateTransitionActionCompartmentToolProvider(SysmlPackage.eINSTANCE.getStateDefinition_ExitAction()).create(this.cache),
                new ExhibitStateWithReferenceNodeToolProvider(this.descriptionNameGenerator).create(this.cache),
                new CompartmentNodeToolProvider(SysmlPackage.eINSTANCE.getElement_Documentation(), this.descriptionNameGenerator).create(this.cache));
        return List.of(createSection, this.toolDescriptionService.addElementsNodeToolSection(true));
    }

    @Override
    public List<NodeToolSection> caseStateUsage(StateUsage object) {
        var createSection = this.toolDescriptionService.buildCreateSection(
                this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getComment()), SysmlPackage.eINSTANCE.getComment(),
                        SysmlPackage.eINSTANCE.getOwningMembership(), null),
                new StateTransitionCompartmentNodeToolProvider(false, false).create(this.cache),
                new StateTransitionCompartmentNodeToolProvider(true, false).create(this.cache),
                new StateTransitionCompartmentNodeToolProvider(false, true).create(this.cache),
                new StateTransitionCompartmentNodeToolProvider(true, true).create(this.cache),
                new StateTransitionActionCompartmentToolProvider(SysmlPackage.eINSTANCE.getStateUsage_EntryAction()).create(this.cache),
                new StateTransitionActionCompartmentToolProvider(SysmlPackage.eINSTANCE.getStateUsage_DoAction()).create(this.cache),
                new StateTransitionActionCompartmentToolProvider(SysmlPackage.eINSTANCE.getStateUsage_ExitAction()).create(this.cache),
                new ExhibitStateWithReferenceNodeToolProvider(this.descriptionNameGenerator).create(this.cache),
                new CompartmentNodeToolProvider(SysmlPackage.eINSTANCE.getElement_Documentation(), this.descriptionNameGenerator).create(this.cache));
        var editSection = this.toolDescriptionService.buildEditSection(
                new SetAsCompositeToolProvider().create(this.cache),
                new SetAsRefToolProvider().create(this.cache));
        return List.of(createSection, editSection, this.toolDescriptionService.addElementsNodeToolSection(true));
    }

    @Override
    public List<NodeToolSection> caseUsage(Usage object) {
        var createSection = this.toolDescriptionService.buildCreateSection(this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getComment()),
                SysmlPackage.eINSTANCE.getComment(), SysmlPackage.eINSTANCE.getOwningMembership(), null));
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        var editSection = this.toolDescriptionService.buildEditSection(
                new SetAsCompositeToolProvider().create(this.cache),
                new SetAsRefToolProvider().create(this.cache));
        return List.of(createSection, editSection, this.toolDescriptionService.addElementsNodeToolSection(true));
    }
}
