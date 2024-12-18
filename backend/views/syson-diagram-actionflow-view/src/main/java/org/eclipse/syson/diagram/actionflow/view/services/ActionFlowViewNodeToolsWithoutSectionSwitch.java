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

import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.syson.diagram.actionflow.view.AFVDescriptionNameGenerator;
import org.eclipse.syson.diagram.common.view.services.AbstractViewNodeToolsWithoutSectionSwitch;
import org.eclipse.syson.diagram.common.view.services.description.ToolDescriptionService;
import org.eclipse.syson.diagram.common.view.tools.CompartmentNodeToolProvider;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * Switch retrieving the list of NodeTools (without ToolSection) for each SysMLv2 concept represented in the Action Flow
 * View diagram.
 *
 * @author arichard
 */
public class ActionFlowViewNodeToolsWithoutSectionSwitch extends AbstractViewNodeToolsWithoutSectionSwitch {

    private final List<NodeDescription> allNodeDescriptions;

    private final IViewDiagramElementFinder cache;

    private final ToolDescriptionService toolDescriptionService;

    public ActionFlowViewNodeToolsWithoutSectionSwitch(IViewDiagramElementFinder cache, List<NodeDescription> allNodeDescriptions) {
        super(new AFVDescriptionNameGenerator());
        this.cache = Objects.requireNonNull(cache);
        this.allNodeDescriptions = Objects.requireNonNull(allNodeDescriptions);
        this.toolDescriptionService = new ToolDescriptionService(this.descriptionNameGenerator);
    }

    @Override
    protected List<NodeDescription> getAllNodeDescriptions() {
        return this.allNodeDescriptions;
    }

    @Override
    public List<NodeTool> caseDefinition(Definition object) {
        var commentNodeTool = this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getComment()),
                SysmlPackage.eINSTANCE.getComment(), SysmlPackage.eINSTANCE.getOwningMembership(), null);
        var documentationNodeTool = new CompartmentNodeToolProvider(SysmlPackage.eINSTANCE.getElement_Documentation(), this.descriptionNameGenerator).create(this.cache);
        return List.of(commentNodeTool, documentationNodeTool);
    }

    @Override
    public List<NodeTool> caseUsage(Usage object) {
        var commentNodeTool = this.toolDescriptionService.createNodeTool(this.getNodeDescription(SysmlPackage.eINSTANCE.getComment()),
                SysmlPackage.eINSTANCE.getComment(), SysmlPackage.eINSTANCE.getOwningMembership(), null);
        var documentationNodeTool = new CompartmentNodeToolProvider(SysmlPackage.eINSTANCE.getElement_Documentation(), this.descriptionNameGenerator).create(this.cache);
        return List.of(commentNodeTool, documentationNodeTool);
    }
}
