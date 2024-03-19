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
package org.eclipse.syson.diagram.requirement.view.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.sirius.components.view.builder.generated.DiagramBuilders;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.diagram.NodeToolSection;
import org.eclipse.syson.diagram.common.view.tools.CompartmentNodeToolProvider;
import org.eclipse.syson.diagram.requirement.view.RVDescriptionNameGenerator;
import org.eclipse.syson.diagram.requirement.view.RequirementViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysmlEClassSwitch;

/**
 * Switch retrieving the list of NodeToolSections for each SysMLv2 concept represented in the Requirement View diagram.

 * @author Jerome Gout
 */
public class RequirementViewNodeToolSectionSwitch extends SysmlEClassSwitch<Void> {

    private final DiagramBuilders diagramBuilderHelper;

    private final List<NodeToolSection> nodeToolSections;

    private final IDescriptionNameGenerator nameGenerator = new RVDescriptionNameGenerator();

    public RequirementViewNodeToolSectionSwitch() {
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
    public Void caseUsage(Usage object) {
        this.createToolsForCompartmentItems(object);
        return super.caseUsage(object);
    }

    private void createToolsForCompartmentItems(Element object) {
        List<NodeTool> compartmentNodeTools = new ArrayList<>();
        RequirementViewDiagramDescriptionProvider.COMPARTMENTS_WITH_LIST_ITEMS.forEach((compartmentEClass, listItems) -> {
            if (compartmentEClass.equals(object.eClass())) {
                listItems.forEach(eReference -> {
                    CompartmentNodeToolProvider provider = new CompartmentNodeToolProvider(eReference.getEType(), this.nameGenerator);
                    compartmentNodeTools.add(provider.create(null));
                });
            }
        });
        Optional<NodeToolSection> createToolSection = this.nodeToolSections.stream()
                .filter(toolSection -> toolSection.getName().equals("Create"))
                .findFirst();
        if (createToolSection.isPresent()) {
            createToolSection.get().getNodeTools().addAll(compartmentNodeTools);
        } else {
            NodeToolSection toolSection = this.diagramBuilderHelper.newNodeToolSection()
                    .name("Create")
                    .nodeTools(compartmentNodeTools.toArray(NodeTool[]::new))
                    .build();
            this.nodeToolSections.add(toolSection);
        }
    }
}
