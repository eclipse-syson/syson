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
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.syson.diagram.common.view.services.AbstractViewNodeToolSectionSwitch;
import org.eclipse.syson.diagram.common.view.services.description.ToolConstants;
import org.eclipse.syson.diagram.common.view.tools.ExhibitStateWithReferenceNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.SetAsCompositeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.SetAsRefToolProvider;
import org.eclipse.syson.diagram.common.view.tools.StateTransitionActionCompartmentToolProvider;
import org.eclipse.syson.diagram.common.view.tools.StateTransitionCompartmentNodeToolProvider;
import org.eclipse.syson.diagram.statetransition.view.STVDescriptionNameGenerator;
import org.eclipse.syson.diagram.statetransition.view.StateTransitionViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * Switch retrieving the list of NodeToolSections for each SysMLv2 concept represented in the State Transition View
 * diagram.
 *
 * @author adieumegard
 */
public class StateTransitionViewNodeToolSectionSwitch extends AbstractViewNodeToolSectionSwitch {

    private final IViewDiagramElementFinder cache;

    private final List<NodeDescription> allNodeDescriptions;

    public StateTransitionViewNodeToolSectionSwitch(IViewDiagramElementFinder cache, List<NodeDescription> allNodeDescriptions) {
        super(new STVDescriptionNameGenerator());
        this.cache = Objects.requireNonNull(cache);
        this.allNodeDescriptions = Objects.requireNonNull(allNodeDescriptions);
    }

    @Override
    public List<NodeToolSection> caseDefinition(Definition object) {
        var sections = this.toolDescriptionService.createDefaultNodeToolSections();
        this.createToolsForCompartmentItems(object, sections, this.cache);
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
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new StateTransitionActionCompartmentToolProvider(SysmlPackage.eINSTANCE.getStateDefinition_EntryAction()).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new StateTransitionActionCompartmentToolProvider(SysmlPackage.eINSTANCE.getStateDefinition_DoAction()).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new StateTransitionActionCompartmentToolProvider(SysmlPackage.eINSTANCE.getStateDefinition_ExitAction()).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new ExhibitStateWithReferenceNodeToolProvider(this.descriptionNameGenerator).create(this.cache));
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
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new StateTransitionActionCompartmentToolProvider(SysmlPackage.eINSTANCE.getStateDefinition_EntryAction()).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new StateTransitionActionCompartmentToolProvider(SysmlPackage.eINSTANCE.getStateDefinition_DoAction()).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR,
                new StateTransitionActionCompartmentToolProvider(SysmlPackage.eINSTANCE.getStateDefinition_ExitAction()).create(this.cache));
        this.toolDescriptionService.addNodeTool(sections, ToolConstants.BEHAVIOR, new ExhibitStateWithReferenceNodeToolProvider(this.descriptionNameGenerator).create(this.cache));
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
        compartmentRefs.removeIf(ref -> Objects.equals(ref, SysmlPackage.eINSTANCE.getElement_Documentation()));
        return compartmentRefs;
    }

    @Override
    protected List<NodeDescription> getAllNodeDescriptions() {
        return this.allNodeDescriptions;
    }
}
