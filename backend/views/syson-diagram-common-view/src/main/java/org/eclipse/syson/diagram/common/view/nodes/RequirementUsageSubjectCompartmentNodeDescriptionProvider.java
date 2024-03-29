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
package org.eclipse.syson.diagram.common.view.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.tools.RequirementSubjectCompartmentNodeToolProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * Requirement Subject Compartment node description.
 *
 * @author Jerome Gout
 */
public class RequirementUsageSubjectCompartmentNodeDescriptionProvider extends AbstractCompartmentNodeDescriptionProvider {

    public RequirementUsageSubjectCompartmentNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator nameGenerator) {
        super(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter(), colorProvider, nameGenerator);
    }

    @Override
    protected String getCustomCompartmentLabel() {
        return "subject";
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        List<NodeDescription> droppableNodes = new ArrayList<>();
        cache.getNodeDescription(this.nameGenerator.getCompartmentItemName(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter()))
                .ifPresent(droppableNodes::add);
        cache.getNodeDescription(this.nameGenerator.getCompartmentItemName(SysmlPackage.eINSTANCE.getRequirementDefinition(), SysmlPackage.eINSTANCE.getRequirementDefinition_SubjectParameter()))
                .ifPresent(droppableNodes::add);
        return droppableNodes;
    }

    @Override
    protected String getDropElementFromDiagramExpression() {
        return "aql:droppedElement.dropRequirementSubjectFromDiagram(droppedNode, targetElement, targetNode, editingContext, diagramContext, convertedNodes)";
    }

    @Override
    protected INodeToolProvider getItemCreationToolProvider() {
        return new RequirementSubjectCompartmentNodeToolProvider();
    }
}
