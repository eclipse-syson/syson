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
package org.eclipse.syson.diagram.common.view.services.description;

import org.eclipse.emf.ecore.EObject;
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
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.OccurrenceDefinition;
import org.eclipse.syson.sysml.OccurrenceUsage;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PerformActionUsage;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.RequirementDefinition;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.UseCaseDefinition;
import org.eclipse.syson.sysml.UseCaseUsage;
import org.eclipse.syson.util.SysmlEClassSwitch;

/**
 * Get the name of the Tool Section given the SysMLv2 class.
 *
 * @author arichard
 */
public class NodeToolSectionNameSwitch extends SysmlEClassSwitch<String> {

    @Override
    public String defaultCase(EObject object) {
        return ToolConstants.STRUCTURE;
    }

    @Override
    public String caseAcceptActionUsage(AcceptActionUsage object) {
        return ToolConstants.BEHAVIOR;
    }

    @Override
    public String caseActionDefinition(ActionDefinition object) {
        return ToolConstants.BEHAVIOR;
    }

    @Override
    public String caseActionUsage(ActionUsage object) {
        return ToolConstants.BEHAVIOR;
    }

    @Override
    public String caseAllocationDefinition(AllocationDefinition object) {
        return ToolConstants.BEHAVIOR;
    }

    @Override
    public String caseAllocationUsage(AllocationUsage object) {
        return ToolConstants.BEHAVIOR;
    }

    @Override
    public String caseAssignmentActionUsage(AssignmentActionUsage object) {
        return ToolConstants.BEHAVIOR;
    }

    @Override
    public String caseAttributeDefinition(AttributeDefinition object) {
        return ToolConstants.STRUCTURE;
    }

    @Override
    public String caseAttributeUsage(AttributeUsage object) {
        return ToolConstants.STRUCTURE;
    }

    @Override
    public String caseCaseDefinition(CaseDefinition object) {
        return ToolConstants.ANALYSIS;
    }

    @Override
    public String caseCaseUsage(CaseUsage object) {
        return ToolConstants.ANALYSIS;
    }

    @Override
    public String caseConstraintDefinition(ConstraintDefinition object) {
        return ToolConstants.REQUIREMENTS;
    }

    @Override
    public String caseConstraintUsage(ConstraintUsage object) {
        return ToolConstants.REQUIREMENTS;
    }

    @Override
    public String caseEnumerationDefinition(EnumerationDefinition object) {
        return ToolConstants.STRUCTURE;
    }

    @Override
    public String caseEnumerationUsage(EnumerationUsage object) {
        return ToolConstants.STRUCTURE;
    }

    @Override
    public String caseExhibitStateUsage(ExhibitStateUsage object) {
        return ToolConstants.BEHAVIOR;
    }

    @Override
    public String caseInterfaceDefinition(InterfaceDefinition object) {
        return ToolConstants.STRUCTURE;
    }

    @Override
    public String caseInterfaceUsage(InterfaceUsage object) {
        return ToolConstants.STRUCTURE;
    }

    @Override
    public String caseItemDefinition(ItemDefinition object) {
        return ToolConstants.STRUCTURE;
    }

    @Override
    public String caseItemUsage(ItemUsage object) {
        return ToolConstants.STRUCTURE;
    }

    @Override
    public String caseMetadataDefinition(MetadataDefinition object) {
        return ToolConstants.EXTENSION;
    }

    @Override
    public String caseMetadataUsage(MetadataUsage object) {
        return ToolConstants.EXTENSION;
    }

    @Override
    public String caseNamespaceImport(NamespaceImport object) {
        return ToolConstants.STRUCTURE;
    }

    @Override
    public String caseOccurrenceDefinition(OccurrenceDefinition object) {
        return ToolConstants.BEHAVIOR;
    }

    @Override
    public String caseOccurrenceUsage(OccurrenceUsage object) {
        return ToolConstants.BEHAVIOR;
    }

    @Override
    public String casePackage(Package object) {
        return ToolConstants.STRUCTURE;
    }

    @Override
    public String casePartDefinition(PartDefinition object) {
        return ToolConstants.STRUCTURE;
    }

    @Override
    public String casePartUsage(PartUsage object) {
        return ToolConstants.STRUCTURE;
    }

    @Override
    public String casePerformActionUsage(PerformActionUsage object) {
        return ToolConstants.BEHAVIOR;
    }

    @Override
    public String casePortDefinition(PortDefinition object) {
        return ToolConstants.STRUCTURE;
    }

    @Override
    public String casePortUsage(PortUsage object) {
        return ToolConstants.STRUCTURE;
    }

    @Override
    public String caseReferenceUsage(ReferenceUsage object) {
        return ToolConstants.STRUCTURE;
    }

    @Override
    public String caseRequirementDefinition(RequirementDefinition object) {
        return ToolConstants.REQUIREMENTS;
    }

    @Override
    public String caseRequirementUsage(RequirementUsage object) {
        return ToolConstants.REQUIREMENTS;
    }

    @Override
    public String caseStateDefinition(StateDefinition object) {
        return ToolConstants.BEHAVIOR;
    }

    @Override
    public String caseStateUsage(StateUsage object) {
        return ToolConstants.BEHAVIOR;
    }

    @Override
    public String caseUseCaseDefinition(UseCaseDefinition object) {
        return ToolConstants.ANALYSIS;
    }

    @Override
    public String caseUseCaseUsage(UseCaseUsage object) {
        return ToolConstants.ANALYSIS;
    }
}
