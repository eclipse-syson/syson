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
package org.eclipse.syson.diagram.general.view.nodes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.nodes.AbstractCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.tools.SubjectCompartmentNodeToolProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;

/**
 * UseCase usage Subject Compartment node description.
 *
 * @author Jerome Gout
 */
public class UseCaseUsageSubjectCompartmentNodeDescriptionProvider extends AbstractCompartmentNodeDescriptionProvider {

    public UseCaseUsageSubjectCompartmentNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator nameGenerator) {
        super(SysmlPackage.eINSTANCE.getUseCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_SubjectParameter(), colorProvider, nameGenerator);
    }

    @Override
    protected String getCustomCompartmentLabel() {
        return "subject";
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        List<NodeDescription> droppableNodes = new ArrayList<>();
        cache.getNodeDescription(this.descriptionNameGenerator.getCompartmentItemName(SysmlPackage.eINSTANCE.getUseCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_SubjectParameter()))
                .ifPresent(droppableNodes::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getCompartmentItemName(SysmlPackage.eINSTANCE.getUseCaseDefinition(), SysmlPackage.eINSTANCE.getCaseDefinition_SubjectParameter()))
                .ifPresent(droppableNodes::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getCompartmentItemName(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter()))
                .ifPresent(droppableNodes::add);
        cache.getNodeDescription(this.descriptionNameGenerator.getCompartmentItemName(SysmlPackage.eINSTANCE.getRequirementDefinition(), SysmlPackage.eINSTANCE.getRequirementDefinition_SubjectParameter()))
                .ifPresent(droppableNodes::add);
        return droppableNodes;
    }

    @Override
    protected String getDropElementFromDiagramExpression() {
        return "aql:droppedElement.dropSubjectFromDiagram(droppedNode, targetElement, targetNode, editingContext, diagramContext, convertedNodes)";
    }

    @Override
    protected INodeToolProvider getItemCreationToolProvider() {
        return new SubjectCompartmentNodeToolProvider();
    }
}
