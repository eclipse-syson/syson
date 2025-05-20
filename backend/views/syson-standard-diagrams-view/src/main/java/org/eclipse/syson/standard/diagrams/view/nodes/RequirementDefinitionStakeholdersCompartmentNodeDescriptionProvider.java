/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.standard.diagrams.view.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.view.builder.IViewDiagramElementFinder;
import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.INodeToolProvider;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.syson.diagram.common.view.nodes.AbstractCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.tools.StakeholdersCompartmentNodeToolProvider;
import org.eclipse.syson.sysml.RequirementDefinition;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.springframework.util.Assert;

/**
 * {@link RequirementDefinitionActorsCompartmentNodeDescriptionProvider} specialization for
 * {@link RequirementDefinition#getStakeholderParameter()}.
 *
 * @author flatombe
 */
public class RequirementDefinitionStakeholdersCompartmentNodeDescriptionProvider extends AbstractCompartmentNodeDescriptionProvider {

    public RequirementDefinitionStakeholdersCompartmentNodeDescriptionProvider(EClass eClass, EReference eReference, IColorProvider colorProvider, IDescriptionNameGenerator descriptionNameGenerator) {
        super(eClass, eReference, colorProvider, descriptionNameGenerator);
        Assert.isTrue(Stream.concat(Stream.of(eClass), eClass.getEAllSuperTypes().stream()).anyMatch(eClassOrSuper -> eClassOrSuper == SysmlPackage.eINSTANCE.getRequirementDefinition()),
                () -> "'%s' is not a sub-type of RequirementDefinition".formatted(eClass.getName()));
        Assert.isTrue(
                eReference.getEType() instanceof EClass &&
                        Stream.concat(Stream.of((EClass) eReference.getEType()), ((EClass) eReference.getEType()).getEAllSuperTypes().stream())
                                .anyMatch(eReferenceTypeOrSuper -> eReferenceTypeOrSuper == SysmlPackage.eINSTANCE.getPartUsage()),
                () -> "'%s' type is not a sub-type of PartUsage".formatted(eReference.getName()));
    }

    @Override
    protected String getCustomCompartmentLabel() {
        return "stakeholders";
    }

    @Override
    protected List<NodeDescription> getDroppableNodes(IViewDiagramElementFinder cache) {
        final List<NodeDescription> droppableNodeDescriptions = new ArrayList<>();

        cache.getNodeDescription(
                this.getDescriptionNameGenerator().getCompartmentItemName(SysmlPackage.eINSTANCE.getConcernDefinition(), SysmlPackage.eINSTANCE.getRequirementDefinition_StakeholderParameter()))
                .ifPresent(droppableNodeDescriptions::add);
        cache.getNodeDescription(this.getDescriptionNameGenerator().getCompartmentItemName(SysmlPackage.eINSTANCE.getConcernUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_StakeholderParameter()))
                .ifPresent(droppableNodeDescriptions::add);
        cache.getNodeDescription(
                this.getDescriptionNameGenerator().getCompartmentItemName(SysmlPackage.eINSTANCE.getRequirementDefinition(), SysmlPackage.eINSTANCE.getRequirementDefinition_StakeholderParameter()))
                .ifPresent(droppableNodeDescriptions::add);
        cache.getNodeDescription(
                this.getDescriptionNameGenerator().getCompartmentItemName(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_StakeholderParameter()))
                .ifPresent(droppableNodeDescriptions::add);
        cache.getNodeDescription(
                this.getDescriptionNameGenerator().getCompartmentItemName(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), SysmlPackage.eINSTANCE.getRequirementUsage_StakeholderParameter()))
                .ifPresent(droppableNodeDescriptions::add);

        return droppableNodeDescriptions;
    }

    @Override
    protected List<INodeToolProvider> getItemCreationToolProviders() {
        List<INodeToolProvider> creationToolProviders = new ArrayList<>();
        creationToolProviders.add(new StakeholdersCompartmentNodeToolProvider());
        return creationToolProviders;
    }
}
