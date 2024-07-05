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
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.syson.diagram.common.view.services.AbstractViewNodeToolSectionSwitch;
import org.eclipse.syson.diagram.common.view.services.description.ToolDescriptionService;
import org.eclipse.syson.diagram.common.view.tools.CompartmentNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.StateTransitionActionCompartmentToolProvider;
import org.eclipse.syson.diagram.common.view.tools.StateTransitionCompartmentNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.StateTransitionToggleExhibitStateToolProvider;
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

    private final List<NodeDescription> allNodeDescriptions;

    private final ToolDescriptionService toolDescriptionService;

    public StateTransitionViewNodeToolSectionSwitch(List<NodeDescription> allNodeDescriptions, IDescriptionNameGenerator descriptionNameGenerator) {
        super(descriptionNameGenerator);
        this.allNodeDescriptions = Objects.requireNonNull(allNodeDescriptions);
        toolDescriptionService = new ToolDescriptionService(descriptionNameGenerator);
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
            compartmentNodeTools.add(provider.create(null));
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
                new StateTransitionCompartmentNodeToolProvider(false, false).create(null),
                new StateTransitionCompartmentNodeToolProvider(true, false).create(null),
                new StateTransitionCompartmentNodeToolProvider(false, true).create(null),
                new StateTransitionCompartmentNodeToolProvider(true, true).create(null),
                new StateTransitionActionCompartmentToolProvider(SysmlPackage.eINSTANCE.getStateDefinition_EntryAction()).create(null),
                new StateTransitionActionCompartmentToolProvider(SysmlPackage.eINSTANCE.getStateDefinition_DoAction()).create(null),
                new StateTransitionActionCompartmentToolProvider(SysmlPackage.eINSTANCE.getStateDefinition_ExitAction()).create(null),
                new CompartmentNodeToolProvider(SysmlPackage.eINSTANCE.getElement_Documentation(), this.descriptionNameGenerator).create(null));
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
                new StateTransitionActionCompartmentToolProvider(SysmlPackage.eINSTANCE.getStateUsage_ExitAction()).create(null),
                new CompartmentNodeToolProvider(SysmlPackage.eINSTANCE.getElement_Documentation(), this.descriptionNameGenerator).create(null));
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
}
