/*******************************************************************************
 * Copyright (c) 2023, 2026 Obeo.
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
package org.eclipse.syson.diagram.services.utils;

import java.util.Objects;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.diagram.services.api.IDiagramLabelService;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.ActorMembership;
import org.eclipse.syson.sysml.AllocationDefinition;
import org.eclipse.syson.sysml.AllocationUsage;
import org.eclipse.syson.sysml.AssignmentActionUsage;
import org.eclipse.syson.sysml.AttributeDefinition;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.CaseDefinition;
import org.eclipse.syson.sysml.CaseUsage;
import org.eclipse.syson.sysml.Comment;
import org.eclipse.syson.sysml.ConcernDefinition;
import org.eclipse.syson.sysml.ConcernUsage;
import org.eclipse.syson.sysml.ConstraintDefinition;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Documentation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EnumerationDefinition;
import org.eclipse.syson.sysml.ExhibitStateUsage;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureReferenceExpression;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.InterfaceDefinition;
import org.eclipse.syson.sysml.InterfaceUsage;
import org.eclipse.syson.sysml.ItemDefinition;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.LiteralExpression;
import org.eclipse.syson.sysml.MetadataDefinition;
import org.eclipse.syson.sysml.MultiplicityRange;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.OccurrenceDefinition;
import org.eclipse.syson.sysml.OccurrenceUsage;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PerformActionUsage;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.RequirementDefinition;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SatisfyRequirementUsage;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.SubjectMembership;
import org.eclipse.syson.sysml.TextualRepresentation;
import org.eclipse.syson.sysml.TriggerInvocationExpression;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.UseCaseDefinition;
import org.eclipse.syson.sysml.UseCaseUsage;
import org.eclipse.syson.sysml.VariantMembership;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.VisibilityKind;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.eclipse.syson.sysml.util.SysmlSwitch;

/**
 * Switch returning the label of some container nodes in the diagrams.
 *
 * @author arichard
 */
public class MultiLineLabelSwitch extends SysmlSwitch<String> {

    private final IDiagramLabelService labelService;

    public MultiLineLabelSwitch(IDiagramLabelService labelService) {
        this.labelService = Objects.requireNonNull(labelService);
    }

    @Override
    public String caseElement(Element object) {
        StringBuilder label = new StringBuilder();
        label.append(this.labelService.getIdentificationLabel(object));
        return label.toString();
    }

    @Override
    public String caseAcceptActionUsage(AcceptActionUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append(this.reference(object))
                .append("accept action")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getReferenceSubsettingLabel(object))
                .append(this.labelService.getSubsettingLabel(object))
                .append(this.labelService.getValueLabel(object))
                .append(LabelConstants.CR)
                .append(this.acceptActionUsagePayloadDetails(object))
                .append(this.acceptActionUsageReceiverDetails(object));
        return label.toString();
    }

    @Override
    public String caseActionDefinition(ActionDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("action def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String caseActionUsage(ActionUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append(this.reference(object))
                .append("action")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getReferenceSubsettingLabel(object))
                .append(this.labelService.getSubsettingLabel(object))
                .append(this.labelService.getValueLabel(object));
        return label.toString();
    }

    @Override
    public String caseAllocationDefinition(AllocationDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("allocation def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String caseAllocationUsage(AllocationUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append(this.reference(object))
                .append("allocation")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getReferenceSubsettingLabel(object))
                .append(this.labelService.getSubsettingLabel(object))
                .append(this.labelService.getValueLabel(object));
        return label.toString();
    }

    @Override
    public String caseAttributeDefinition(AttributeDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("attribute def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String caseAttributeUsage(AttributeUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("attribute")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getReferenceSubsettingLabel(object))
                .append(this.labelService.getSubsettingLabel(object))
                .append(this.labelService.getValueLabel(object));
        return label.toString();
    }

    @Override
    public String caseAssignmentActionUsage(AssignmentActionUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append(this.reference(object))
                .append("assign")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.assignmentActionUsageDetails(object));
        return label.toString();
    }

    @Override
    public String caseCaseDefinition(CaseDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("case def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String caseCaseUsage(CaseUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append(this.reference(object))
                .append("case")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getReferenceSubsettingLabel(object))
                .append(this.labelService.getSubsettingLabel(object))
                .append(this.labelService.getValueLabel(object));
        return label.toString();
    }

    @Override
    public String caseComment(Comment object) {
        StringBuilder label = new StringBuilder();
        label
                .append(LabelConstants.OPEN_QUOTE)
                .append("comment")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR);
        String declaredName = object.getDeclaredName();
        if (declaredName != null && !declaredName.isEmpty()) {
            label
                    .append(this.caseElement(object))
                    .append(LabelConstants.CR);
        }
        String body = object.getBody();
        if (body != null) {
            label
                    .append(LabelConstants.CR)
                    .append(object.getBody());
        }
        return label.toString();
    }

    @Override
    public String caseConcernDefinition(ConcernDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("concern def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String caseConcernUsage(ConcernUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append(this.reference(object))
                .append("concern")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getReferenceSubsettingLabel(object))
                .append(this.labelService.getSubsettingLabel(object))
                .append(this.labelService.getValueLabel(object));
        return label.toString();
    }

    @Override
    public String caseConstraintDefinition(ConstraintDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("constraint def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String caseConstraintUsage(ConstraintUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append(this.reference(object))
                .append("constraint")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getReferenceSubsettingLabel(object))
                .append(this.labelService.getSubsettingLabel(object))
                .append(this.labelService.getValueLabel(object));
        return label.toString();
    }

    @Override
    public String caseDocumentation(Documentation object) {
        StringBuilder label = new StringBuilder();
        label
                .append(LabelConstants.OPEN_QUOTE)
                .append("doc")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR);
        String declaredName = object.getDeclaredName();
        if (declaredName != null && !declaredName.isEmpty()) {
            label
                    .append(this.caseElement(object))
                    .append(LabelConstants.CR);
        }
        String body = object.getBody();
        if (body != null) {
            label
                    .append(LabelConstants.CR)
                    .append(object.getBody());
        }
        return label.toString();
    }

    @Override
    public String caseTextualRepresentation(TextualRepresentation object) {
        StringBuilder label = new StringBuilder();
        label
                .append(LabelConstants.OPEN_QUOTE)
                .append("rep")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR);
        String declaredName = object.getDeclaredName();
        if (declaredName != null && !declaredName.isEmpty()) {
            label
                    .append(this.caseElement(object))
                    .append(LabelConstants.CR);
        }

        String language = object.getLanguage();

        if (language != null) {
            label.append(LabelConstants.CR)
                    .append("language = ")
                    .append(language);
        }

        String body = object.getBody();
        if (body != null) {
            label
                    .append(LabelConstants.CR)
                    .append(object.getBody());
        }
        return label.toString();
    }

    @Override
    public String caseEnumerationDefinition(EnumerationDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("enumeration def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String caseExhibitStateUsage(ExhibitStateUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(this.getIsParallel(object.isIsParallel()))
                .append(LabelConstants.OPEN_QUOTE)
                .append(this.reference(object))
                .append("exhibit state")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getReferenceSubsettingLabel(object))
                .append(this.labelService.getSubsettingLabel(object))
                .append(this.labelService.getValueLabel(object));
        return label.toString();
    }

    @Override
    public String caseInterfaceDefinition(InterfaceDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("interface def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String caseInterfaceUsage(InterfaceUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append(this.reference(object))
                .append("interface")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getReferenceSubsettingLabel(object))
                .append(this.labelService.getSubsettingLabel(object))
                .append(this.labelService.getValueLabel(object));
        return label.toString();
    }

    @Override
    public String caseItemDefinition(ItemDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("item def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String caseNamespaceImport(NamespaceImport object) {
        Namespace importedNamespace = object.getImportedNamespace();
        StringBuilder builder = new StringBuilder();
        if (importedNamespace != null) {
            if (object.getVisibility() != VisibilityKind.PUBLIC) {
                builder.append(LabelConstants.OPEN_QUOTE);
                builder.append(object.getVisibility().getLiteral());
                builder.append(LabelConstants.CLOSE_QUOTE);
                builder.append(LabelConstants.CR);
            }
            builder.append(this.caseElement(importedNamespace));
        }
        return builder.toString();
    }

    @Override
    public String caseItemUsage(ItemUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append(this.reference(object))
                .append("item")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getReferenceSubsettingLabel(object))
                .append(this.labelService.getSubsettingLabel(object))
                .append(this.labelService.getValueLabel(object));
        return label.toString();
    }

    @Override
    public String caseMetadataDefinition(MetadataDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("metadata def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String caseOccurrenceDefinition(OccurrenceDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(this.individual(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("occurrence def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String caseOccurrenceUsage(OccurrenceUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(this.individual(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append(this.reference(object))
                .append("occurrence")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getReferenceSubsettingLabel(object))
                .append(this.labelService.getSubsettingLabel(object))
                .append(this.labelService.getValueLabel(object));
        return label.toString();
    }

    @Override
    public String casePackage(Package object) {
        StringBuilder label = new StringBuilder();
        label.append(this.caseElement(object));
        return label.toString();
    }

    @Override
    public String casePartDefinition(PartDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("part def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String casePartUsage(PartUsage object) {
        StringBuilder label = new StringBuilder();
        if (!(object.getOwningMembership() instanceof ActorMembership)) {
            // The label shouldn't contain abstract, ref, or part if the part represents an actor.
            label
                    .append(this.getBasicNamePrefix(object))
                    .append(LabelConstants.OPEN_QUOTE)
                    .append(this.reference(object))
                    .append("part")
                    .append(LabelConstants.CLOSE_QUOTE)
                    .append(LabelConstants.CR);
        }
        label
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getReferenceSubsettingLabel(object))
                .append(this.labelService.getSubsettingLabel(object))
                .append(this.labelService.getValueLabel(object));
        return label.toString();
    }

    @Override
    public String casePerformActionUsage(PerformActionUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append(this.reference(object))
                .append(this.getPerformActionUsageTag(object))
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR);
        String name = object.getPerformedAction().getName();
        if (name == null) {
            name = "";
        }
        label.append(name);
        return label.toString();
    }

    @Override
    public String casePortDefinition(PortDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("port def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String casePortUsage(PortUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append(this.reference(object))
                .append("port")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getReferenceSubsettingLabel(object))
                .append(this.labelService.getSubsettingLabel(object))
                .append(this.labelService.getValueLabel(object));
        return label.toString();
    }

    @Override
    public String caseReferenceUsage(ReferenceUsage object) {
        var conceptName = "reference";
        var owner = object.getOwner();
        var owningMembership = object.getOwningMembership();
        if (owner instanceof ActionUsage || owner instanceof ActionDefinition) {
            conceptName = "parameter";
        } else if (owningMembership instanceof SubjectMembership) {
            conceptName = "subject";
        }
        StringBuilder label = new StringBuilder();
        if (!(owningMembership instanceof SubjectMembership)) {
            // The label shouldn't contain abstract, ref, or part if the part represents an actor.
            label
                    .append(this.getBasicNamePrefix(object));
        }
        label
                .append(LabelConstants.OPEN_QUOTE)
                .append(this.reference(object))
                .append(conceptName)
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getReferenceSubsettingLabel(object))
                .append(this.labelService.getSubsettingLabel(object))
                .append(this.labelService.getValueLabel(object));
        return label.toString();
    }

    @Override
    public String caseRequirementDefinition(RequirementDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("requirement def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String caseRequirementUsage(RequirementUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append(this.reference(object))
                .append("requirement")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getReferenceSubsettingLabel(object))
                .append(this.labelService.getSubsettingLabel(object))
                .append(this.labelService.getValueLabel(object));
        return label.toString();
    }

    @Override
    public String caseSatisfyRequirementUsage(SatisfyRequirementUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append(this.reference(object))
                .append("satisfy requirement")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getReferenceSubsettingLabel(object))
                .append(this.labelService.getSubsettingLabel(object))
                .append(this.labelService.getValueLabel(object));
        return label.toString();
    }

    @Override
    public String caseUseCaseDefinition(UseCaseDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append("use case def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String caseUseCaseUsage(UseCaseUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append(this.reference(object))
                .append("use case")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getReferenceSubsettingLabel(object))
                .append(this.labelService.getSubsettingLabel(object))
                .append(this.labelService.getValueLabel(object));
        return label.toString();
    }

    @Override
    public String caseStateDefinition(StateDefinition object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(this.getIsParallel(object.isIsParallel()))
                .append(LabelConstants.OPEN_QUOTE)
                .append("state def")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.labelService.getSubclassificationLabel(object));
        return label.toString();
    }

    @Override
    public String caseStateUsage(StateUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(this.getIsParallel(object.isIsParallel()))
                .append(LabelConstants.OPEN_QUOTE)
                .append(this.reference(object))
                .append("state")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.CR)
                .append(this.caseElement(object))
                .append(this.multiplicityRange(object))
                .append(this.labelService.getTypingLabel(object))
                .append(this.labelService.getRedefinitionLabel(object))
                .append(this.labelService.getReferenceSubsettingLabel(object))
                .append(this.labelService.getSubsettingLabel(object))
                .append(this.labelService.getValueLabel(object));
        return label.toString();
    }

    @Override
    public String caseViewUsage(ViewUsage object) {
        StringBuilder label = new StringBuilder();
        label
                .append(this.getBasicNamePrefix(object))
                .append(LabelConstants.OPEN_QUOTE)
                .append(this.reference(object))
                .append("view")
                .append(LabelConstants.CLOSE_QUOTE)
                .append(LabelConstants.SPACE)
                .append(this.caseElement(object))
                .append(this.labelService.getTypingLabel(object));
        return label.toString();
    }

    /**
     * Builds the string value for Parallel StateUsage or StateDefinition.
     *
     * @param isParallel
     *         Whether the State is parallel or not.
     */
    private StringBuilder getIsParallel(boolean isParallel) {
        StringBuilder parallel = new StringBuilder();
        if (isParallel) {
            parallel
                    .append(LabelConstants.OPEN_QUOTE)
                    .append("parallel")
                    .append(LabelConstants.CLOSE_QUOTE)
                    .append(LabelConstants.CR);
        }
        return parallel;
    }

    private String getBasicNamePrefix(Type object) {
        StringBuilder label = new StringBuilder();
        if (object instanceof Usage usage) {
            label.append(this.variationUsage(usage));
        } else if (object instanceof Definition definition) {
            label.append(this.variationDefinition(definition));
        }
        label.append(this.variantElement(object))
                .append(this.abstractType(object));
        return label.toString();
    }

    private String abstractType(Type type) {
        StringBuilder label = new StringBuilder();
        if (type.isIsAbstract()) {
            label
                    .append(LabelConstants.OPEN_QUOTE)
                    .append(LabelConstants.ABSTRACT)
                    .append(LabelConstants.CLOSE_QUOTE)
                    .append(LabelConstants.CR);
        }
        return label.toString();
    }

    private String variationDefinition(Definition definition) {
        StringBuilder label = new StringBuilder();
        if (definition.isIsVariation()) {
            label
                    .append(LabelConstants.OPEN_QUOTE)
                    .append(LabelConstants.VARIATION)
                    .append(LabelConstants.CLOSE_QUOTE)
                    .append(LabelConstants.CR);
        }
        return label.toString();
    }

    private String variationUsage(Usage usage) {
        StringBuilder label = new StringBuilder();
        if (usage.isIsVariation()) {
            label
                    .append(LabelConstants.OPEN_QUOTE)
                    .append(LabelConstants.VARIATION)
                    .append(LabelConstants.CLOSE_QUOTE)
                    .append(LabelConstants.CR);
        }
        return label.toString();
    }

    private String variantElement(Element elt) {
        StringBuilder label = new StringBuilder();
        EObject membership = elt.getOwningMembership();
        if (membership != null) {
            EObject parent = membership.eContainer();
            boolean hasVariationParent = (parent instanceof Definition && ((Definition) parent).isIsVariation()) | (parent instanceof Usage && ((Usage) parent).isIsVariation());
            if (membership instanceof VariantMembership | hasVariationParent) {
                label
                        .append(LabelConstants.OPEN_QUOTE)
                        .append(LabelConstants.VARIANT)
                        .append(LabelConstants.CLOSE_QUOTE)
                        .append(LabelConstants.CR);
            }
        }
        return label.toString();
    }

    private String multiplicityRange(Usage usage) {
        StringBuilder label = new StringBuilder();
        var optMultiplicityRange = usage.getOwnedRelationship().stream()
                .filter(OwningMembership.class::isInstance)
                .map(OwningMembership.class::cast)
                .flatMap(m -> m.getOwnedRelatedElement().stream())
                .filter(MultiplicityRange.class::isInstance)
                .map(MultiplicityRange.class::cast)
                .findFirst();
        if (optMultiplicityRange.isPresent()) {
            var range = optMultiplicityRange.get();
            String firstBound = null;
            String secondBound = null;
            var bounds = range.getOwnedRelationship().stream()
                    .filter(OwningMembership.class::isInstance)
                    .map(OwningMembership.class::cast)
                    .flatMap(m -> m.getOwnedRelatedElement().stream())
                    .filter(LiteralExpression.class::isInstance)
                    .map(LiteralExpression.class::cast)
                    .toList();
            if (bounds.size() == 1) {
                firstBound = this.labelService.getSysmlTextualRepresentation(bounds.get(0), false);
            } else if (bounds.size() == 2) {
                firstBound = this.labelService.getSysmlTextualRepresentation(bounds.get(0), false);
                secondBound = this.labelService.getSysmlTextualRepresentation(bounds.get(1), false);
            }
            label.append(LabelConstants.OPEN_BRACKET);
            if (firstBound != null) {
                label.append(firstBound);
            }
            if (secondBound != null) {
                label.append("..");
                label.append(secondBound);
            }
            label.append(LabelConstants.CLOSE_BRACKET);
            label.append(LabelConstants.SPACE);
        }
        return label.toString();
    }

    private String reference(Usage usage) {
        String result = "";
        if (usage.isIsReference()) {
            result = LabelConstants.REF + LabelConstants.SPACE;
        }
        return result;
    }

    private String individual(OccurrenceDefinition occurrenceDefinition) {
        return this.individual(occurrenceDefinition.isIsIndividual());
    }

    private String individual(OccurrenceUsage occurrenceUsage) {
        return this.individual(occurrenceUsage.isIsIndividual());
    }

    private String individual(boolean isIndividual) {
        StringBuilder label = new StringBuilder();
        if (isIndividual) {
            label
                    .append(LabelConstants.OPEN_QUOTE)
                    .append("individual")
                    .append(LabelConstants.CLOSE_QUOTE)
                    .append(LabelConstants.CR);
        }
        return label.toString();
    }

    private String assignmentActionUsageDetails(AssignmentActionUsage aau) {
        StringBuilder label = new StringBuilder();
        if (aau.getReferent() != null) {
            label
                    .append(aau.getReferent().getName())
                    .append(LabelConstants.SPACE)
                    .append(":=")
                    .append(LabelConstants.SPACE)
                    .append(this.getAssignmentValue(aau.getValueExpression()));
        }
        return label.toString();
    }

    private String getAssignmentValue(Expression expression) {
        StringBuilder label = new StringBuilder();
        if (expression != null) {
            // At the moment, we do not have the service to retrieve the textual representation of an
            // expression. This code should be enhanced to cover all kind of expression.
            var value = this.labelService.getSysmlTextualRepresentation(expression, false);
            if (value != null) {
                label.append(value);
            }
        }
        return label.toString();
    }

    private String getPerformActionUsageTag(PerformActionUsage pau) {
        if (Objects.equals(pau.getPerformedAction(), pau)) {
            return "perform action";
        }
        return "perform";
    }

    private String acceptActionUsagePayloadDetails(AcceptActionUsage aau) {
        StringBuilder label = new StringBuilder();
        this.addAcceptActionUsagePayloadParameter(label, aau);
        this.addAcceptActionUsagePayloadArgument(label, aau);
        return label.toString();
    }

    private void addAcceptActionUsagePayloadParameter(StringBuilder label, AcceptActionUsage aau) {
        var payloadParameter = aau.getPayloadParameter();
        if (payloadParameter != null) {
            var payloadName = payloadParameter.getDeclaredName();
            var payloadTypeName = payloadParameter.getOwnedRelationship().stream()
                    .filter(FeatureTyping.class::isInstance)
                    .map(FeatureTyping.class::cast)
                    .map(ft -> ft.getType())
                    .filter(type -> type != null)
                    .map(t -> t.getDeclaredName())
                    .findFirst()
                    .orElse(null);

            if (payloadTypeName != null) {
                label
                        .append(LabelConstants.OPEN_QUOTE)
                        .append("accept")
                        .append(LabelConstants.CLOSE_QUOTE);
                if (payloadName != null) {
                    label
                            .append(LabelConstants.SPACE)
                            .append(payloadName);
                }
                if (payloadName != null) {
                    label.append(LabelConstants.COLON);
                }
                label
                        .append(LabelConstants.SPACE)
                        .append(payloadTypeName);
                label.append(LabelConstants.CR);
            }
        }
    }

    private void addAcceptActionUsagePayloadArgument(StringBuilder label, AcceptActionUsage aau) {
        var payloadArgument = aau.getPayloadArgument();
        if (payloadArgument instanceof TriggerInvocationExpression tie) {
            Expression expression = tie.getOwnedRelationship().stream()
                    .filter(FeatureMembership.class::isInstance)
                    .map(FeatureMembership.class::cast)
                    .flatMap(pm -> pm.getOwnedRelatedElement().stream())
                    .filter(Expression.class::isInstance)
                    .map(Expression.class::cast)
                    .findFirst()
                    .orElse(null);
            if (expression != null && expression.getDeclaredName() != null) {
                label
                        .append(tie.getKind().toString())
                        .append(LabelConstants.SPACE)
                        // At the moment, we do not have the service to retrieve the textual representation of an
                        // expression.
                        .append(expression.getDeclaredName());
            }
        }
    }

    private String acceptActionUsageReceiverDetails(AcceptActionUsage aau) {
        StringBuilder label = new StringBuilder();
        Expression receiverArgument = aau.getReceiverArgument();
        if (receiverArgument instanceof FeatureReferenceExpression fre) {
            var referent = fre.getReferent();
            if (referent != null) {
                var referentName = referent.getName();
                if (referentName != null) {
                    label
                            .append(LabelConstants.OPEN_QUOTE)
                            .append("via")
                            .append(LabelConstants.CLOSE_QUOTE)
                            .append(LabelConstants.SPACE)
                            .append(referentName)
                            .append(LabelConstants.CR);
                }
            }
        }
        return label.toString();
    }
}
