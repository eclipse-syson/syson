/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.nodes.AbstractCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.tools.SubjectCompartmentNodeToolProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.AQLUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.springframework.util.Assert;

/**
 * Requirement Subject Compartment node description.
 *
 * @author Jerome Gout
 */
public class RequirementUsageSubjectCompartmentNodeDescriptionProvider extends AbstractCompartmentNodeDescriptionProvider {

    public RequirementUsageSubjectCompartmentNodeDescriptionProvider(IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        this(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter(), colorProvider, descriptionNameGenerator);
    }

    public RequirementUsageSubjectCompartmentNodeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(eClass, eReference, colorProvider, descriptionNameGenerator);
        Assert.isTrue(Stream.concat(Stream.of(eClass), eClass.getEAllSuperTypes().stream()).anyMatch(eClassOrSuper -> eClassOrSuper == SysmlPackage.eINSTANCE.getRequirementUsage()),
                () -> "'%s' is not a sub-type of RequirementUsage".formatted(eClass.getName()));
    }

    @Override
    protected String getCustomCompartmentLabel() {
        return "subject";
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        List<NodeDescription> droppableNodes = new ArrayList<>();
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentItemName(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter()))
                .ifPresent(droppableNodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentItemName(SysmlPackage.eINSTANCE.getRequirementDefinition(), SysmlPackage.eINSTANCE.getRequirementDefinition_SubjectParameter()))
                .ifPresent(droppableNodes::add);
        cache.getNodeDescription(
                this.getDescriptionNameGenerator().getCompartmentItemName(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_SubjectParameter()))
                .ifPresent(droppableNodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentItemName(SysmlPackage.eINSTANCE.getCaseUsage(), SysmlPackage.eINSTANCE.getCaseUsage_SubjectParameter()))
                .ifPresent(droppableNodes::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentItemName(SysmlPackage.eINSTANCE.getCaseDefinition(), SysmlPackage.eINSTANCE.getCaseDefinition_SubjectParameter()))
                .ifPresent(droppableNodes::add);
        return droppableNodes;
    }

    @Override
    protected String getDropElementFromDiagramExpression() {
        return AQLUtils.getServiceCallExpression("droppedElement", "dropSubjectFromDiagram",
                List.of("droppedNode", "targetElement", "targetNode", IEditingContext.EDITING_CONTEXT, IDiagramContext.DIAGRAM_CONTEXT, "convertedNodes"));
    }

    @Override
    protected List<INodeToolProvider> getItemCreationToolProviders() {
        List<INodeToolProvider> creationToolProviders = new ArrayList<>();
        creationToolProviders.add(new SubjectCompartmentNodeToolProvider());
        return creationToolProviders;
    }
}
