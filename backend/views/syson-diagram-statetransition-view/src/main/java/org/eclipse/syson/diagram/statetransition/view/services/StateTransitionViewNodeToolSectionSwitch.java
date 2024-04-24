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
import java.util.Optional;

import org.eclipse.sirius.components.view.builder.generated.DiagramBuilders;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.syson.diagram.common.view.tools.CompartmentNodeToolProvider;
import org.eclipse.syson.diagram.statetransition.view.STVDescriptionNameGenerator;
import org.eclipse.syson.diagram.statetransition.view.StateTransitionViewDiagramDescriptionProvider;
import org.eclipse.syson.diagram.statetransition.view.tools.StateUsageCompartmentNodeToolProvider;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.util.SysmlEClassSwitch;

/**
 * Switch retrieving the list of NodeToolSections for each SysMLv2 concept represented in the State Transition View diagram.

 * @author adieumegard
 */
public class StateTransitionViewNodeToolSectionSwitch extends SysmlEClassSwitch<Void> {

    private final DiagramBuilders diagramBuilderHelper;

    private final List<NodeToolSection> nodeToolSections;

    private final STVDescriptionNameGenerator nameGenerator = new STVDescriptionNameGenerator();

    public StateTransitionViewNodeToolSectionSwitch() {
        this.diagramBuilderHelper = new DiagramBuilders();
        this.nodeToolSections = new ArrayList<>();
    }

    public List<NodeToolSection> getNodeToolSections() {
        return this.nodeToolSections;
    }

    @Override
    public Void caseDefinition(Definition object) {
        this.createToolsForCompartmentItems(object);        
        return super.caseDefinition(object);
    }
    
    @Override
    public Void caseStateDefinition(StateDefinition object) {
        List<NodeTool> nodeTools = new ArrayList<>();
        nodeTools.add(new StateUsageCompartmentNodeToolProvider(true).create(null));
        nodeTools.add(new StateUsageCompartmentNodeToolProvider(false).create(null));
        addToolsToSection(nodeTools, "Create");
        return super.caseStateDefinition(object);
    }

    @Override
    public Void caseUsage(Usage object) {
        this.createToolsForCompartmentItems(object);
        return super.caseUsage(object);
    }

    /**
     * Add {@code nodeTools} tools to the tools section named {@code sectionName}. Creates the tool section if needed.
     * 
     * @param nodeTools
     *            The tools to add
     * @param sectionName
     *            The name of the section
     */
    private void addToolsToSection(List<NodeTool> nodeTools, String sectionName) {
        Optional<NodeToolSection> createToolSection = this.nodeToolSections.stream()
                .filter(toolSection -> toolSection.getName().equals(sectionName))
                .findFirst();
        if (createToolSection.isPresent()) {
            createToolSection.get().getNodeTools().addAll(nodeTools);
        } else {
            NodeToolSection toolSection = this.diagramBuilderHelper.newNodeToolSection()
                    .name(sectionName)
                    .nodeTools(nodeTools.toArray(NodeTool[]::new))
                    .build();
            this.nodeToolSections.add(toolSection);
        }
    }

    private void createToolsForCompartmentItems(Element object) {
        List<NodeTool> compartmentNodeTools = new ArrayList<>();
        StateTransitionViewDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.forEach((compartmentEClass, listItems) -> {
            if (compartmentEClass.equals(object.eClass())) {
                listItems.forEach(eReference -> {
                    CompartmentNodeToolProvider provider = new CompartmentNodeToolProvider(eReference, this.nameGenerator);
                    compartmentNodeTools.add(provider.create(null));
                });
            }
        });
        addToolsToSection(compartmentNodeTools, "Create");
    }
}
