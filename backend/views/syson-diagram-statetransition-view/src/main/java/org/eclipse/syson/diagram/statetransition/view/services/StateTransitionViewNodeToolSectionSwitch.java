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
import org.eclipse.syson.diagram.common.view.tools.CompartmentNodeToolProvider;
import org.eclipse.syson.diagram.common.view.tools.StateTransitionActionToolProvider;
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

    private final List<NodeDescription> allNodeDescriptions;

    public StateTransitionViewNodeToolSectionSwitch(List<NodeDescription> allNodeDescriptions, IDescriptionNameGenerator descriptionNameGenerator) {
        super(descriptionNameGenerator);
        this.allNodeDescriptions = Objects.requireNonNull(allNodeDescriptions);
    }

    @Override
    protected List<EReference> getElementCompartmentReferences(Element element) {
        List<EReference> refs = StateTransitionViewDiagramDescriptionProvider.COMPARTMENTS_WITH_MERGED_LIST_ITEMS.get(element.eClass());
        if (refs != null) {
            return refs;
        } else {
            return List.of();
        }
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
        var createSection = this.buildCreateSection();
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection);
    }

    @Override
    public List<NodeToolSection> caseStateDefinition(StateDefinition object) {
        var createSection = this.buildCreateSection(
                new StateTransitionCompartmentNodeToolProvider(true).create(null),
                new StateTransitionCompartmentNodeToolProvider(false).create(null),
                new StateTransitionActionToolProvider(SysmlPackage.eINSTANCE.getStateDefinition_EntryAction()).create(null),
                new StateTransitionActionToolProvider(SysmlPackage.eINSTANCE.getStateDefinition_DoAction()).create(null),
                new StateTransitionActionToolProvider(SysmlPackage.eINSTANCE.getStateDefinition_ExitAction()).create(null));
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> caseStateUsage(StateUsage object) {
        var createSection = this.buildCreateSection(
                new StateTransitionCompartmentNodeToolProvider(true).create(null),
                new StateTransitionCompartmentNodeToolProvider(false).create(null),
                new StateTransitionActionToolProvider(SysmlPackage.eINSTANCE.getStateUsage_EntryAction()).create(null),
                new StateTransitionActionToolProvider(SysmlPackage.eINSTANCE.getStateUsage_DoAction()).create(null),
                new StateTransitionActionToolProvider(SysmlPackage.eINSTANCE.getStateUsage_ExitAction()).create(null));
        return List.of(createSection, this.addElementsToolSection());
    }

    @Override
    public List<NodeToolSection> caseUsage(Usage object) {
        var createSection = this.buildCreateSection();
        createSection.getNodeTools().addAll(this.createToolsForCompartmentItems(object));
        return List.of(createSection, this.addElementsToolSection());
    }
}
