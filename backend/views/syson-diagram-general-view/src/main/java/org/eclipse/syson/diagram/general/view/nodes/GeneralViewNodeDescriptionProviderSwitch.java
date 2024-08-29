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

import org.eclipse.sirius.components.view.builder.providers.IColorProvider;
import org.eclipse.sirius.components.view.builder.providers.INodeDescriptionProvider;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.AllocationDefinition;
import org.eclipse.syson.sysml.AllocationUsage;
import org.eclipse.syson.sysml.AssignmentActionUsage;
import org.eclipse.syson.sysml.AttributeDefinition;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.CaseDefinition;
import org.eclipse.syson.sysml.CaseUsage;
import org.eclipse.syson.sysml.ConstraintDefinition;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.EnumerationDefinition;
import org.eclipse.syson.sysml.EnumerationUsage;
import org.eclipse.syson.sysml.ExhibitStateUsage;
import org.eclipse.syson.sysml.InterfaceDefinition;
import org.eclipse.syson.sysml.InterfaceUsage;
import org.eclipse.syson.sysml.ItemDefinition;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.MetadataDefinition;
import org.eclipse.syson.sysml.MetadataUsage;
import org.eclipse.syson.sysml.OccurrenceDefinition;
import org.eclipse.syson.sysml.OccurrenceUsage;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PerformActionUsage;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.RequirementDefinition;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.UseCaseDefinition;
import org.eclipse.syson.sysml.UseCaseUsage;
import org.eclipse.syson.util.SysmlEClassSwitch;

/**
 * Switch retrieving the list of NodeDescriptionProvider for each SysMLv2 concept represented in the General View
 * diagram.
 *
 * @author Jerome Gout
 */
public class GeneralViewNodeDescriptionProviderSwitch extends SysmlEClassSwitch<INodeDescriptionProvider> {

    private final IColorProvider colorProvider;

    public GeneralViewNodeDescriptionProviderSwitch(IColorProvider colorProvider) {
        this.colorProvider = colorProvider;
    }

    @Override
    public INodeDescriptionProvider caseAcceptActionUsage(AcceptActionUsage object) {
        return new UsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getAcceptActionUsage(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseActionDefinition(ActionDefinition object) {
        return new DefinitionNodeDescriptionProvider(SysmlPackage.eINSTANCE.getActionDefinition(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseActionUsage(ActionUsage object) {
        return new UsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getActionUsage(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseAllocationDefinition(AllocationDefinition object) {
        return new DefinitionNodeDescriptionProvider(SysmlPackage.eINSTANCE.getAllocationDefinition(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseAllocationUsage(AllocationUsage object) {
        return new AllocationUsageNodeDescriptionProvider(this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseAttributeDefinition(AttributeDefinition object) {
        return new DefinitionNodeDescriptionProvider(SysmlPackage.eINSTANCE.getAttributeDefinition(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseAttributeUsage(AttributeUsage object) {
        return new UsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getAttributeUsage(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseAssignmentActionUsage(AssignmentActionUsage object) {
        return new UsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getAssignmentActionUsage(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseCaseDefinition(CaseDefinition object) {
        return new DefinitionNodeDescriptionProvider(SysmlPackage.eINSTANCE.getCaseDefinition(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseCaseUsage(CaseUsage object) {
        return new UsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getCaseUsage(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseConstraintDefinition(ConstraintDefinition object) {
        return new DefinitionNodeDescriptionProvider(SysmlPackage.eINSTANCE.getConstraintDefinition(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseConstraintUsage(ConstraintUsage object) {
        return new UsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getConstraintUsage(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseEnumerationDefinition(EnumerationDefinition object) {
        return new DefinitionNodeDescriptionProvider(SysmlPackage.eINSTANCE.getEnumerationDefinition(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseEnumerationUsage(EnumerationUsage object) {
        return new UsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getEnumerationUsage(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseExhibitStateUsage(ExhibitStateUsage object) {
        return new UsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getExhibitStateUsage(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseInterfaceDefinition(InterfaceDefinition object) {
        return new DefinitionNodeDescriptionProvider(SysmlPackage.eINSTANCE.getInterfaceDefinition(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseInterfaceUsage(InterfaceUsage object) {
        return new UsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getInterfaceUsage(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseItemDefinition(ItemDefinition object) {
        return new DefinitionNodeDescriptionProvider(SysmlPackage.eINSTANCE.getItemDefinition(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseItemUsage(ItemUsage object) {
        return new UsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getItemUsage(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseMetadataDefinition(MetadataDefinition object) {
        return new DefinitionNodeDescriptionProvider(SysmlPackage.eINSTANCE.getMetadataDefinition(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseMetadataUsage(MetadataUsage object) {
        return new UsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getMetadataUsage(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseOccurrenceDefinition(OccurrenceDefinition object) {
        return new DefinitionNodeDescriptionProvider(SysmlPackage.eINSTANCE.getOccurrenceDefinition(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseOccurrenceUsage(OccurrenceUsage object) {
        return new UsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getOccurrenceUsage(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider casePackage(Package object) {
        return new PackageNodeDescriptionProvider(this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider casePartDefinition(PartDefinition object) {
        return new DefinitionNodeDescriptionProvider(SysmlPackage.eINSTANCE.getPartDefinition(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider casePartUsage(PartUsage object) {
        return new UsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getPartUsage(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider casePerformActionUsage(PerformActionUsage object) {
        return new UsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getPerformActionUsage(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider casePortDefinition(PortDefinition object) {
        return new DefinitionNodeDescriptionProvider(SysmlPackage.eINSTANCE.getPortDefinition(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider casePortUsage(PortUsage object) {
        return new UsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getPortUsage(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseRequirementDefinition(RequirementDefinition object) {
        return new DefinitionNodeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementDefinition(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseRequirementUsage(RequirementUsage object) {
        return new UsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getRequirementUsage(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseStateDefinition(StateDefinition object) {
        return new DefinitionNodeDescriptionProvider(SysmlPackage.eINSTANCE.getStateDefinition(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseStateUsage(StateUsage object) {
        return new UsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getStateUsage(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseUseCaseDefinition(UseCaseDefinition object) {
        return new DefinitionNodeDescriptionProvider(SysmlPackage.eINSTANCE.getUseCaseDefinition(), this.colorProvider);
    }

    @Override
    public INodeDescriptionProvider caseUseCaseUsage(UseCaseUsage object) {
        return new UsageNodeDescriptionProvider(SysmlPackage.eINSTANCE.getUseCaseUsage(), this.colorProvider);
    }

}
