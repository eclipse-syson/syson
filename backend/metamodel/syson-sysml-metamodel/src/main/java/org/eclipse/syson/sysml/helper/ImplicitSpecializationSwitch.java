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
package org.eclipse.syson.sysml.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.ActorMembership;
import org.eclipse.syson.sysml.AllocationDefinition;
import org.eclipse.syson.sysml.AllocationUsage;
import org.eclipse.syson.sysml.AnalysisCaseDefinition;
import org.eclipse.syson.sysml.AnalysisCaseUsage;
import org.eclipse.syson.sysml.AssertConstraintUsage;
import org.eclipse.syson.sysml.AssignmentActionUsage;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.CalculationDefinition;
import org.eclipse.syson.sysml.CalculationUsage;
import org.eclipse.syson.sysml.CaseDefinition;
import org.eclipse.syson.sysml.CaseUsage;
import org.eclipse.syson.sysml.Classifier;
import org.eclipse.syson.sysml.ConcernDefinition;
import org.eclipse.syson.sysml.ConcernUsage;
import org.eclipse.syson.sysml.ConnectionDefinition;
import org.eclipse.syson.sysml.ConnectionUsage;
import org.eclipse.syson.sysml.ConstraintDefinition;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.ControlNode;
import org.eclipse.syson.sysml.DecisionNode;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.EventOccurrenceUsage;
import org.eclipse.syson.sysml.ExhibitStateUsage;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.FlowConnectionDefinition;
import org.eclipse.syson.sysml.FlowConnectionUsage;
import org.eclipse.syson.sysml.ForLoopActionUsage;
import org.eclipse.syson.sysml.ForkNode;
import org.eclipse.syson.sysml.FramedConcernMembership;
import org.eclipse.syson.sysml.IfActionUsage;
import org.eclipse.syson.sysml.IncludeUseCaseUsage;
import org.eclipse.syson.sysml.InterfaceDefinition;
import org.eclipse.syson.sysml.InterfaceUsage;
import org.eclipse.syson.sysml.ItemDefinition;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.JoinNode;
import org.eclipse.syson.sysml.LifeClass;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.MergeNode;
import org.eclipse.syson.sysml.MetadataDefinition;
import org.eclipse.syson.sysml.MetadataUsage;
import org.eclipse.syson.sysml.Multiplicity;
import org.eclipse.syson.sysml.OccurrenceDefinition;
import org.eclipse.syson.sysml.OccurrenceUsage;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PerformActionUsage;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.RenderingDefinition;
import org.eclipse.syson.sysml.RenderingUsage;
import org.eclipse.syson.sysml.RequirementConstraintKind;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.RequirementDefinition;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.RequirementVerificationMembership;
import org.eclipse.syson.sysml.SendActionUsage;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.StakeholderMembership;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateSubactionKind;
import org.eclipse.syson.sysml.StateSubactionMembership;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.SuccessionAsUsage;
import org.eclipse.syson.sysml.SuccessionFlowConnectionUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.TriggerInvocationExpression;
import org.eclipse.syson.sysml.TriggerKind;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.UseCaseDefinition;
import org.eclipse.syson.sysml.UseCaseUsage;
import org.eclipse.syson.sysml.VerificationCaseDefinition;
import org.eclipse.syson.sysml.VerificationCaseUsage;
import org.eclipse.syson.sysml.ViewDefinition;
import org.eclipse.syson.sysml.ViewRenderingMembership;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.ViewpointDefinition;
import org.eclipse.syson.sysml.ViewpointUsage;
import org.eclipse.syson.sysml.WhileLoopActionUsage;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.eclipse.syson.sysml.util.SysmlSwitch;

/**
 * Switch allowing to compute implicit specializations for a given Element.
 *
 * @author arichard
 */
public class ImplicitSpecializationSwitch extends SysmlSwitch<List<Specialization>> {

    private final List<Specialization> existingSpecializations;

    private final ElementUtil elementUtil;

    public ImplicitSpecializationSwitch(List<Specialization> existingSpecializations) {
        this.existingSpecializations = Objects.requireNonNull(existingSpecializations);
        this.elementUtil = new ElementUtil();
    }

    @Override
    public List<Specialization> defaultCase(EObject object) {
        return List.of();
    }

    @Override
    public List<Specialization> caseAcceptActionUsage(AcceptActionUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        String implicitElement = null;
        var owningType = object.getOwningType();
        if (object.isIsComposite() && object.isSubactionUsage() && !object.isTriggerAction()) {
            // A composite AcceptActionUsage that is a subaction usage, but is not the triggerAction of a
            // TransitionUsage, must directly or indirectly specialize the ActionUsageActions::Action::acceptSubactions
            // from the Systems Model Library.
            implicitElement = "Actions::Action::acceptSubactions";
        } else if (!object.isTriggerAction()) {
            // An AcceptActionUsage that is not the triggerAction of a TransitionUsage must directly or indirectly
            // specialize the ActionUsage Actions::acceptActions from the Systems Model Library.
            implicitElement = "Actions::acceptActions";
        } else if (object.isTriggerAction()) {
            // An AcceptActionUsage that is the triggerAction of TransitionUsage must directly or indirectly specialize
            // the ActionUsage Actions::TransitionAction::accepter from the Systems Model Library.
            implicitElement = "Actions::TransitionAction::accepter";
        }
        if (implicitElement != null) {
            var implicitSubsetting = this.implicitSubsetting(object, implicitElement);
            if (implicitSubsetting != null) {
                implicitSpecializations.add(implicitSubsetting);
            }
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseReferenceUsage(ReferenceUsage referenceUsage) {
        final List<Specialization> result;
        Type owningType = referenceUsage.getOwningType();
        if (owningType instanceof SuccessionAsUsage successionAsUsage) {
            result = this.handleReferenceUsageInSuccessionAsUsage(referenceUsage, successionAsUsage);
        } else {
            result = List.of();
        }
        if (!result.isEmpty()) {
            return result;
        }
        // If not found check for super type cases
        return super.caseReferenceUsage(referenceUsage);
    }

    @Override
    public List<Specialization> caseActionDefinition(ActionDefinition object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubclassification(object)) {
            return implicitSpecializations;
        }
        var implicitSubclassification = this.implicitSubclassification(object, "Actions::Action");
        if (implicitSubclassification != null) {
            implicitSpecializations.add(implicitSubclassification);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseActionUsage(ActionUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        var implicitSubsettingElement = "Actions::actions";
        // A composite ActionUsage whose owningType is PartDefinition or PartUsage must directly or indirectly
        // specialize the ActionUsage Parts::Part::ownedActions from the Systems Model Library.
        var owningType = object.getOwningType();
        var owningFeatureMembership = object.getOwningFeatureMembership();
        if (owningFeatureMembership instanceof StateSubactionMembership ssm && !this.hasRedefinition(object)) {
            // An ActionUsage that is the entry, do, or exit Action of a StateDefinition or StateUsage must redefine the
            // entryAction, doAction, or exitAction feature, respectively, of the StateDefinition States::StateAction
            // from the Systems Model Library.
            var implicitRedefinitionElement = "States::StateAction::exitAction";
            var kind = ssm.getKind();
            if (kind == StateSubactionKind.ENTRY) {
                implicitRedefinitionElement = "States::StateAction::entryAction";
            } else if (kind == StateSubactionKind.DO) {
                implicitRedefinitionElement = "States::StateAction::doAction";
            }
            var implicitRedefinition = this.implicitRedefinition(object, implicitRedefinitionElement);
            if (implicitRedefinition != null) {
                implicitSpecializations.add(implicitRedefinition);
            }
        } else if (object.isIsComposite() && (owningType instanceof PartDefinition || owningType instanceof PartUsage)) {
            implicitSubsettingElement = "Parts::Part::ownedActions";
        } else if (object.isIsComposite() && object.isSubactionUsage()) {
            // A composite ActionUsage that is a subaction usage must directly or indirectly specialize the ActionUsage
            // Actions::Action::subactions from the Systems Model Library.
            implicitSubsettingElement = "Actions::Action::subactions";
        }
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseAllocationDefinition(AllocationDefinition object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubclassification(object)) {
            return implicitSpecializations;
        }
        var implicitSubclassification = this.implicitSubclassification(object, "Allocations::Allocation");
        if (implicitSubclassification != null) {
            implicitSpecializations.add(implicitSubclassification);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseAllocationUsage(AllocationUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsetting = this.implicitSubsetting(object, "Allocations::allocations");
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseAnalysisCaseDefinition(AnalysisCaseDefinition object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubclassification(object)) {
            return implicitSpecializations;
        }
        var implicitSubclassification = this.implicitSubclassification(object, "AnalysisCases::AnalysisCase");
        if (implicitSubclassification != null) {
            implicitSpecializations.add(implicitSubclassification);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseAnalysisCaseUsage(AnalysisCaseUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsettingElement = "AnalysisCases::analysisCases";
        // A composite AnalysisCaseUsage whose owningType is an AnalysisCaseDefinition or AnalysisCaseUsage must
        // specialize the AnalysisCaseUsage AnalysisCases::AnalysisCase::subAnalysisCases from the Systems Model
        // Library.
        var owningType = object.getOwningType();
        if (owningType instanceof AnalysisCaseDefinition || owningType instanceof AnalysisCaseUsage) {
            implicitSubsettingElement = "AnalysisCases::AnalysisCase::subAnalysisCases";
        }
        var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseAssertConstraintUsage(AssertConstraintUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsettingElement = "Constraints::assertedConstraints";
        // If a AssertConstraintUsage is negated, then it must directly or indirectly specialize the ConstraintUsage
        // Constraints::negatedConstraints. Otherwise, it must directly or indirectly specialize the ConstraintUsage
        // Constraints::assertedConstraints.
        if (object.isIsNegated()) {
            implicitSubsettingElement = "Constraints::negatedConstraints";
        }
        var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseAssignmentActionUsage(AssignmentActionUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsettingElement = "Actions::assignmentActions";
        // A composite AssignmentActionUsage that is a subaction usage must directly or indirectly specialize the
        // ActionUsage Actions::Action::assignments from the Systems Model Library.
        if (object.isIsComposite() && object.isSubactionUsage()) {
            implicitSubsettingElement = "Actions::Action::assignments";
        }
        var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseAttributeUsage(AttributeUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsetting = this.implicitSubsetting(object, "Base::dataValues");
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseCalculationDefinition(CalculationDefinition object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubclassification(object)) {
            return implicitSpecializations;
        }
        var implicitSubclassification = this.implicitSubclassification(object, "Calculations::Calculation");
        if (implicitSubclassification != null) {
            implicitSpecializations.add(implicitSubclassification);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseCalculationUsage(CalculationUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsettingElement = "Calculations::calculations";
        var owningType = object.getOwningType();
        if (owningType instanceof CalculationDefinition || owningType instanceof CalculationUsage) {
            implicitSubsettingElement = "Calculations::Calculation::subcalculations";
        }
        var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseCaseDefinition(CaseDefinition object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubclassification(object)) {
            return implicitSpecializations;
        }
        var implicitSubclassification = this.implicitSubclassification(object, "Cases::Case");
        if (implicitSubclassification != null) {
            implicitSpecializations.add(implicitSubclassification);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseCaseUsage(CaseUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsettingElement = "Cases::cases";
        // A composite CaseUsage whose owningType is a CaseDefinition or CaseUsage must directly or indirectly
        // specialize the CaseUsage Cases::Case::subcases.
        var owningType = object.getOwningType();
        if (owningType instanceof CaseDefinition || owningType instanceof CaseUsage) {
            implicitSubsettingElement = "Cases::Case::subcases";
        }
        var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseConcernDefinition(ConcernDefinition object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubclassification(object)) {
            return implicitSpecializations;
        }
        var implicitSubclassification = this.implicitSubclassification(object, "Requirements::ConcernCheck");
        if (implicitSubclassification != null) {
            implicitSpecializations.add(implicitSubclassification);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseConcernUsage(ConcernUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsettingElement = "Requirements::concernChecks";
        // If a ConcernUsage is owned via a FramedConcernMembership, then it must directly or indirectly specialize the
        // ConcernUsage Requirements::RequirementCheck::concerns from the Systems Model Library.
        var owningFeatureMembership = object.getOwningFeatureMembership();
        if (owningFeatureMembership instanceof FramedConcernMembership) {
            implicitSubsettingElement = "Requirements::RequirementCheck::concerns";
        }
        var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseConnectionDefinition(ConnectionDefinition object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubclassification(object)) {
            return implicitSpecializations;
        }
        var implicitSubclassificationElement = "Connections::Connection";
        // A binary ConnectionDefinition must directly or indirectly specialize the ConnectionDefinition
        // Connections::BinaryConnection from the Systems Model Library.
        if (object.getOwnedEndFeature().size() == 2) {
            implicitSubclassificationElement = "Connections::BinaryConnection";
        }
        var implicitSubclassification = this.implicitSubclassification(object, implicitSubclassificationElement);
        if (implicitSubclassification != null) {
            implicitSpecializations.add(implicitSubclassification);
        }
        return implicitSpecializations;

    }

    @Override
    public List<Specialization> caseConnectionUsage(ConnectionUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsettingElement = "Connections::connections";
        // A binary ConnectionUsage must directly or indirectly specialize the ConnectionUsage
        // Connections::binaryConnections from the Systems Model Library.
        if (object.getOwnedEndFeature().size() == 2) {
            implicitSubsettingElement = "Connections::binaryConnections";
        }
        var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseConstraintDefinition(ConstraintDefinition object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubclassification(object)) {
            return implicitSpecializations;
        }
        var implicitSubclassification = this.implicitSubclassification(object, "Constraints::ConstraintCheck");
        if (implicitSubclassification != null) {
            implicitSpecializations.add(implicitSubclassification);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseConstraintUsage(ConstraintUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsettingElement = "Constraints::constraintChecks";
        // A ConstraintUsage whose owningType is an ItemDefinition or ItemUsage must directly or indirectly specialize
        // the ConstraintUsage Items::Item::checkedConstraints.
        var owningType = object.getOwningType();
        if (owningType instanceof ItemDefinition || owningType instanceof ItemUsage) {
            implicitSubsettingElement = "Items::Item::checkedConstraints";
        }
        var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        // A ConstraintUsage whose owningFeatureMembership is a RequirementConstraintMembership must directly or
        // indirectly specialize on the ConstraintUsages assumptions or constraints from the onstraintDefinition
        // Requirements::RequirementCheck in the Systems Model Library, depending on whether the kind of the
        // RequirementConstraintMembership is assumption or requirement, respectively.
        var owningFeatureMembership = object.getOwningFeatureMembership();
        if (owningFeatureMembership instanceof RequirementConstraintMembership rcm) {
            var kind = rcm.getKind();
            if (kind == RequirementConstraintKind.ASSUMPTION) {
                implicitSubsettingElement = "Requirements::RequirementCheck::assumptions";
            } else {
                implicitSubsettingElement = "Requirements::RequirementCheck::constraints";
            }
            implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
            if (implicitSubsetting != null) {
                implicitSpecializations.add(implicitSubsetting);
            }
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseControlNode(ControlNode object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsetting = this.implicitSubsetting(object, "Action::Action::controls");
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseDecisionNode(DecisionNode object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsetting = this.implicitSubsetting(object, "Actions::Action::decisions");
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseEventOccurrenceUsage(EventOccurrenceUsage object) {
        if (this.hasSubsetting(object)) {
            return List.of();
        }
        // If an EventOccurrenceUsage has an owningType that is an OccurrenceDefinition or OccurrenceUsage, then it must
        // directly or indirectly specialize the Feature Occurrences::Occurrence::timeEnclosedOccurrences.
        var owningType = object.getOwningType();
        if (owningType instanceof OccurrenceDefinition || owningType instanceof OccurrenceUsage) {
            var implicitSubsetting = this.implicitSubsetting(object, "Occurrences::Occurrence::timeEnclosedOccurrences");
            if (implicitSubsetting != null) {
                return List.of(implicitSubsetting);
            }
        }
        return super.caseEventOccurrenceUsage(object);
    }

    @Override
    public List<Specialization> caseExhibitStateUsage(ExhibitStateUsage object) {
        if (this.hasSubsetting(object)) {
            return List.of();
        }
        // If an ExhibitStateUsage has an owningType that is a PartDefinition or PartUsage, then it must directly or
        // indirectly specialize the StateUsage Parts::Part::exhibitedStates.
        var owningType = object.getOwningType();
        if (owningType instanceof PartDefinition || owningType instanceof PartUsage) {
            var implicitSubsetting = this.implicitSubsetting(object, "Parts::Part::exhibitedStates");
            if (implicitSubsetting != null) {
                return List.of(implicitSubsetting);
            }
        }
        return super.caseExhibitStateUsage(object);
    }

    @Override
    public List<Specialization> caseFlowConnectionDefinition(FlowConnectionDefinition object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubclassification(object)) {
            return implicitSpecializations;
        }
        var implicitSubclassification = this.implicitSubclassification(object, "Connections::MessageConnection");
        if (implicitSubclassification != null) {
            implicitSpecializations.add(implicitSubclassification);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseFlowConnectionUsage(FlowConnectionUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsettingElement = "Connections::flowConnections";
        // If a FlowConnectionUsage has no ownedEndFeatures, then it must directly or indirectly specialize the base
        // FlowConnectionUsage Connections::messageConnections from the Systems Library model.
        if (object.getOwnedEndFeature().isEmpty()) {
            implicitSubsettingElement = "Connections::messageConnections";
        }
        var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseForkNode(ForkNode object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsetting = this.implicitSubsetting(object, "Actions::Action::forks");
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseForLoopActionUsage(ForLoopActionUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsettingElement = "Actions::forLoopActions";
        // A composite ForLoopActionUsage that is a subaction usage must directly or indirectly specialize the
        // ActionUsage Actions::Action::forLoops from the Systems Model Library.
        if (object.isIsComposite() && object.isSubactionUsage()) {
            implicitSubsettingElement = "Actions::Action::forLoops";
        }
        var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseIfActionUsage(IfActionUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsettingElement = "Actions::ifThenActions";
        if (object.getElseAction() != null) {
            // A IfActionUsage must directly or indirectly specialize the ActionUsage Actions::ifThenActions from the
            // Systems Model Library. If it has an elseAction, then it must directly or indirectly specialize
            // Actions::ifThenElseActions
            implicitSubsettingElement = "Actions::ifThenElseActions";
        } else if (object.isIsComposite() && object.isSubactionUsage()) {
            // A composite IfActionUsage that is a subaction usage must directly or indirectly specialize the
            // ActionUsage Actions::Action::ifSubactions from the Systems Model Library.
            implicitSubsettingElement = "Actions::Action::ifSubactions";
        }
        var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseIncludeUseCaseUsage(IncludeUseCaseUsage object) {
        if (this.hasSubsetting(object)) {
            return List.of();
        }
        // A IncludeUseCaseUsage whose owningType is a UseCaseDefinition or UseCaseUsage must directly or indirectly
        // specialize the UseCaseUsage UseCases::UseCase::includedUseCases from the Systems Model Library.
        var owningType = object.getOwningType();
        if (owningType instanceof UseCaseDefinition || owningType instanceof UseCaseUsage) {
            var implicitSubsetting = this.implicitSubsetting(object, "UseCases::UseCase::includedUseCases");
            if (implicitSubsetting != null) {
                return List.of(implicitSubsetting);
            }
        }
        return super.caseIncludeUseCaseUsage(object);
    }

    @Override
    public List<Specialization> caseInterfaceDefinition(InterfaceDefinition object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubclassification(object)) {
            return implicitSpecializations;
        }
        var implicitSubclassificationElement = "Interfaces::Interface";
        // A binary InterfaceDefinition must directly or indirectly specialize the InterfaceDefinition
        // Interfaces::BinaryInterface from the Systems Model Library.
        if (object.getOwnedEndFeature().size() == 2) {
            implicitSubclassificationElement = "Interfaces::BinaryInterface";
        }
        var implicitSubclassification = this.implicitSubclassification(object, implicitSubclassificationElement);
        if (implicitSubclassification != null) {
            implicitSpecializations.add(implicitSubclassification);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseInterfaceUsage(InterfaceUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsettingElement = "Interfaces::interfaces";
        // A binary InterfaceUsage must directly or indirectly specialize the InterfaceUsage
        // Interfaces::binaryInterfaces from the Systems Model Library.
        if (object.getOwnedEndFeature().size() == 2) {
            implicitSubsettingElement = "Interfaces::binaryInterfaces";
        }
        var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseItemDefinition(ItemDefinition object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubclassification(object)) {
            return implicitSpecializations;
        }
        var implicitSubclassification = this.implicitSubclassification(object, "Items::Item");
        if (implicitSubclassification != null) {
            implicitSpecializations.add(implicitSubclassification);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseItemUsage(ItemUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsetting = this.implicitSubsetting(object, "Items::items");
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseJoinNode(JoinNode object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsetting = this.implicitSubsetting(object, "Actions::Action::join");
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseLifeClass(LifeClass object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubclassification(object)) {
            return implicitSpecializations;
        }
        var implicitSubclassification = this.implicitSubclassification(object, "Occurrences::Life");
        if (implicitSubclassification != null) {
            implicitSpecializations.add(implicitSubclassification);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseMergeNode(MergeNode object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsetting = this.implicitSubsetting(object, "Actions::Action::merges");
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseMetadataDefinition(MetadataDefinition object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubclassification(object)) {
            return implicitSpecializations;
        }
        var implicitSubclassification = this.implicitSubclassification(object, "Metadata::MetadataItem");
        if (implicitSubclassification != null) {
            implicitSpecializations.add(implicitSubclassification);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseMetadataUsage(MetadataUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsetting = this.implicitSubsetting(object, "Metadata::metadataItems");
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseMultiplicity(Multiplicity object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsetting = this.implicitSubsetting(object, "Base::naturals");
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseOccurrenceDefinition(OccurrenceDefinition object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubclassification(object)) {
            return implicitSpecializations;
        }
        var implicitSubclassification = this.implicitSubclassification(object, "Occurrences::Occurrence");
        if (implicitSubclassification != null) {
            implicitSpecializations.add(implicitSubclassification);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseOccurrenceUsage(OccurrenceUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsetting = this.implicitSubsetting(object, "Occurrences::occurrences");
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> casePartDefinition(PartDefinition object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubclassification(object)) {
            return implicitSpecializations;
        }
        var implicitSubclassification = this.implicitSubclassification(object, "Parts::Part");
        if (implicitSubclassification != null) {
            implicitSpecializations.add(implicitSubclassification);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> casePartUsage(PartUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsettingElement = "Parts::parts";
        var owningType = object.getOwningType();
        if (object.getOwningFeatureMembership() instanceof ActorMembership) {
            // If a PartUsage is owned via an ActorMembership, then it must directly or indirectly specialize either
            // Requirements::RequirementCheck::actors (if its owningType is a RequirementDefinition or RequirementUsage
            // or Cases::Case::actors (otherwise).
            if (owningType instanceof RequirementDefinition || owningType instanceof RequirementUsage) {
                implicitSubsettingElement = "Requirements::RequirementCheck::actors";
            } else {
                implicitSubsettingElement = "Cases::Case::actors";
            }
        } else if (object.getOwningFeatureMembership() instanceof StakeholderMembership) {
            // If a PartUsage is owned via a StakeholderMembership, then it must directly or indirectly specialize
            // either Requirements::RequirementCheck::stakeholders.
            implicitSubsettingElement = "Requirements::RequirementCheck::stakeholders";
        } else if (object.isIsComposite() && (owningType instanceof ItemDefinition || owningType instanceof ItemUsage)) {
            // A composite PartUsage whose owningType is a ItemDefinition or ItemUsage must directly or indirectly
            // specialize the PartUsage Items::Item::subparts from the Systems Model Library.
            implicitSubsettingElement = "Items::Item::subparts";
        }
        var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> casePerformActionUsage(PerformActionUsage object) {
        if (this.hasSubsetting(object)) {
            return List.of();
        }
        // If a PerformActionUsage has an owningType that is a PartDefinition or PartUsage, then it must directly or
        // indirectly specialize the ActionUsage Parts::Part::performedActions.
        var owningType = object.getOwningType();
        if (owningType instanceof PartDefinition || owningType instanceof PartUsage) {
            var implicitSubsetting = this.implicitSubsetting(object, "Parts::Part::performedActions");
            if (implicitSubsetting != null) {
                return List.of(implicitSubsetting);
            }
        }
        return super.casePerformActionUsage(object);
    }

    @Override
    public List<Specialization> casePortDefinition(PortDefinition object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubclassification(object)) {
            return implicitSpecializations;
        }
        var implicitSubclassification = this.implicitSubclassification(object, "Ports::Port");
        if (implicitSubclassification != null) {
            implicitSpecializations.add(implicitSubclassification);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> casePortUsage(PortUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsettingElement = "Ports::ports";
        // A composite PortUsage with an owningType that is a PortDefinition or PortUsage must directly or
        // indirectly specialize the PortUsage Ports::Port::subports from the Systems Model Library.
        var owningType = object.getOwningType();
        if (object.isIsComposite() && (owningType instanceof PortDefinition || owningType instanceof PortUsage)) {
            implicitSubsettingElement = "Ports::Port::subports";
        }
        var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseRenderingDefinition(RenderingDefinition object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubclassification(object)) {
            return implicitSpecializations;
        }
        var implicitSubclassification = this.implicitSubclassification(object, "Views::Rendering");
        if (implicitSubclassification != null) {
            implicitSpecializations.add(implicitSubclassification);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseRenderingUsage(RenderingUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        var owningFeatureMembership = object.getOwningFeatureMembership();
        if (owningFeatureMembership instanceof ViewRenderingMembership && !(this.hasRedefinition(object))) {
            // A RenderingUsage whose owningFeatureMembership is a ViewRenderingMembership must redefine the
            // RenderingUsage Views::View::viewRendering
            var implicitRedefinitionElement = "Views::View::viewRendering";
            var implicitRedefinition = this.implicitRedefinition(object, implicitRedefinitionElement);
            if (implicitRedefinition != null) {
                implicitSpecializations.add(implicitRedefinition);
            }
        }
        if (implicitSpecializations.isEmpty()) {
            if (this.hasSubsetting(object)) {
                return implicitSpecializations;
            }
            var implicitSubsettingElement = "Views::renderings";
            var owningType = object.getOwningType();
            if (object.isIsComposite() && (owningType instanceof RenderingDefinition || owningType instanceof RenderingUsage)) {
                // A RenderingUsage whose owningType is a RenderingDefinition or RenderingUsage must directly or
                // indirectly specialize the RenderingUsage Views::Rendering::subrenderings from the Systems Model
                // Library.
                implicitSubsettingElement = "Views::Rendering::subrenderings";
            }
            var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
            if (implicitSubsetting != null) {
                implicitSpecializations.add(implicitSubsetting);
            }
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseRequirementDefinition(RequirementDefinition object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubclassification(object)) {
            return implicitSpecializations;
        }
        var implicitSubclassification = this.implicitSubclassification(object, "Requirements::RequirementCheck");
        if (implicitSubclassification != null) {
            implicitSpecializations.add(implicitSubclassification);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseRequirementUsage(RequirementUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsettingElement = "Requirements::requirementChecks";
        var owningType = object.getOwningType();
        var owningFeatureMembership = object.getOwningFeatureMembership();
        if (object.isIsComposite() && (owningType instanceof RequirementDefinition || owningType instanceof RequirementUsage)) {
            // A composite RequirementUsage whose owningType is a RequirementDefinition or RequirementUsage must
            // directly or indirectly specialize the RequirementUsage Requirements::RequirementCheck::subrequirements
            // from the Systems Model Library.
            implicitSubsettingElement = "Requirements::RequirementCheck::subrequirements";
        } else if (owningFeatureMembership instanceof RequirementVerificationMembership) {
            // RequirementUsage whose owningFeatureMembership is a RequirementVerificationMembership must directly or
            // indirectly specialize the RequirementUsage
            // VerificationCases::VerificationCase::obj::requirementVerifications.
            implicitSubsettingElement = "VerificationCases::VerificationCase::obj::requirementVerifications";
        }
        var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseSendActionUsage(SendActionUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsettingElement = "Actions::sendActions";
        // A composite SendActionUsage that is a subaction must directly or indirectly specialize the ActionUsage
        // Actions::Action::sendSubactions from the Systems Model Library.
        if (object.isIsComposite() && object.isSubactionUsage()) {
            implicitSubsettingElement = "Actions::Action::acceptSubactions";
        }
        var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseStateDefinition(StateDefinition object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubclassification(object)) {
            return implicitSpecializations;
        }
        var implicitSubclassification = this.implicitSubclassification(object, "States::StateAction");
        if (implicitSubclassification != null) {
            implicitSpecializations.add(implicitSubclassification);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseStateUsage(StateUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsettingElement = "States::stateActions";
        // A StateUsage that is a substate usage with a non-parallel owning StateDefinition or StateUsage must directly
        // or indirectly specialize the StateUsage States::StateAction::exclusiveStates from the Systems Model Library.
        if (object.isSubstateUsage(false)) {
            implicitSubsettingElement = "States::StateAction::exclusiveStates";
        } else if (object.isSubstateUsage(true)) {
            implicitSubsettingElement = "States::StateAction::substates";
        }
        var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseSuccessionFlowConnectionUsage(SuccessionFlowConnectionUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsetting = this.implicitSubsetting(object, "Connections::successionFlowConnections");
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseTransitionUsage(TransitionUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsettingElement = "Actions::transitionActions";
        // A composite TransitionUsage whose owningType is a ActionDefinition or ActionUsage, but not a StateDefinition
        // or StateUsage, must directly or indirectly specialize the ActionUsage Actions::Action::decisionTransitions
        // from the Systems Model Library.
        // A composite TransitionUsage whose owningType is a StateDefinition or StateUsage must directly or indirectly
        // specialize the ActionUsage States::State::stateTransitions from the Systems Model Library.
        var owningType = object.getOwningType();
        if (object.isIsComposite() && (owningType instanceof ActionDefinition || owningType instanceof ActionUsage)) {
            if (!(owningType instanceof StateDefinition || owningType instanceof StateUsage)) {
                implicitSubsettingElement = "Actions::Action::decisionTransitions";
            } else {
                implicitSubsettingElement = "States::State::stateTransitions";
            }
        }
        var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseTriggerInvocationExpression(TriggerInvocationExpression object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        // A TriggerInvocationExpression must directly or indirectly specialize one of the Functions TriggerWhen,
        // TriggerAt or TriggerAfter, from the Kernel Semantic Library Triggers package, depending on whether its kind
        // is when, at or after, respectively.
        var implicitSubsettingElement = "Triggers::TriggerAfter";
        if (object.getKind() == TriggerKind.WHEN) {
            implicitSubsettingElement = "Triggers::TriggerWhen";
        } else if (object.getKind() == TriggerKind.AT) {
            implicitSubsettingElement = "Triggers::TriggerAt";
        }
        var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseUseCaseDefinition(UseCaseDefinition object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubclassification(object)) {
            return implicitSpecializations;
        }
        var implicitSubclassification = this.implicitSubclassification(object, "UseCases::UseCase");
        if (implicitSubclassification != null) {
            implicitSpecializations.add(implicitSubclassification);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseUseCaseUsage(UseCaseUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsettingElement = "UseCases::useCases";
        // A composite UseCaseUsage whose owningType is a UseCaseDefinition or UseCaseUsage must specialize the
        // UseCaseUsage UseCases::UseCase::subUseCases from the Systems Model Library.
        var owningType = object.getOwningType();
        if (object.isIsComposite() && (owningType instanceof UseCaseDefinition || owningType instanceof UseCaseUsage)) {
            implicitSubsettingElement = "UseCases::UseCase::subUseCases";
        }
        var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseVerificationCaseDefinition(VerificationCaseDefinition object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubclassification(object)) {
            return implicitSpecializations;
        }
        var implicitSubclassification = this.implicitSubclassification(object, "VerificationCases::VerificationCase");
        if (implicitSubclassification != null) {
            implicitSpecializations.add(implicitSubclassification);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseVerificationCaseUsage(VerificationCaseUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsettingElement = "VerificationCases::verificationCases";
        // If it is composite and owned by a VerificationCaseDefinition or VerificationCaseUsage, then it must
        // specialize VerificationCaseUsage VerificationCases::VerificationCase::subVerificationCases.
        var owningType = object.getOwningType();
        if (object.isIsComposite() && (owningType instanceof VerificationCaseDefinition || owningType instanceof VerificationCaseUsage)) {
            implicitSubsettingElement = "VerificationCases::VerificationCase::subVerificationCases";
        }
        var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseViewDefinition(ViewDefinition object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubclassification(object)) {
            return implicitSpecializations;
        }
        var implicitSubclassification = this.implicitSubclassification(object, "Views::View");
        if (implicitSubclassification != null) {
            implicitSpecializations.add(implicitSubclassification);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseViewpointDefinition(ViewpointDefinition object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubclassification(object)) {
            return implicitSpecializations;
        }
        var implicitSubclassification = this.implicitSubclassification(object, "Views::Viewpoint");
        if (implicitSubclassification != null) {
            implicitSpecializations.add(implicitSubclassification);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseViewpointUsage(ViewpointUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsettingElement = "Views::viewpoints";
        // A composite ViewpointUsage whose owningType is a ViewDefinition or ViewUsage must directly or indirectly
        // specialize the ViewpointUsage Views::View::viewpointSatisfactions from the Systems Model Library.
        var owningType = object.getOwningType();
        if (object.isIsComposite() && (owningType instanceof ViewDefinition || owningType instanceof ViewUsage)) {
            implicitSubsettingElement = "Views::View::viewpointSatisfactions";
        }
        var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseViewUsage(ViewUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsettingElement = "Views::views";
        // A ViewUsage whose owningType is a ViewDefinition or ViewUsage must specialize the ViewUsage
        // Views::View::subviews from the Systems Library Model.
        var owningType = object.getOwningType();
        if (owningType instanceof ViewDefinition || owningType instanceof ViewUsage) {
            implicitSubsettingElement = "Views::View::subviews";
        }
        var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    @Override
    public List<Specialization> caseWhileLoopActionUsage(WhileLoopActionUsage object) {
        List<Specialization> implicitSpecializations = new ArrayList<>();
        if (this.hasSubsetting(object)) {
            return implicitSpecializations;
        }
        var implicitSubsettingElement = "Actions::whileLoopActions";
        // A composite WhileLoopActionUsage that is a subaction usage must directly or indirectly specialize the
        // ActionUsage Actions::Action::whileLoops from the Systems Model Library.
        if (object.isIsComposite() && object.isSubactionUsage()) {
            implicitSubsettingElement = "Actions::Action::whileLoops";
        }
        var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
        if (implicitSubsetting != null) {
            implicitSpecializations.add(implicitSubsetting);
        }
        return implicitSpecializations;
    }

    private Redefinition implicitRedefinition(Feature feature, String implicitRedefinedFeatureQualifiedName) {
        var implicitFeature = this.elementUtil.findByNameAndType(feature, implicitRedefinedFeatureQualifiedName, Feature.class);
        if (implicitFeature != null) {
            var redefinition = SysmlFactory.eINSTANCE.createRedefinition();
            redefinition.setDeclaredName("redefines (implicit)");
            redefinition.setIsImplied(true);
            redefinition.setRedefiningFeature(feature);
            redefinition.setRedefinedFeature(implicitFeature);
            return redefinition;
        }
        return null;
    }

    private Subclassification implicitSubclassification(Classifier classifier, String implicitSuperclassifierQualifiedName) {
        var implicitClassifier = this.elementUtil.findByNameAndType(classifier, implicitSuperclassifierQualifiedName, Classifier.class);
        if (implicitClassifier != null) {
            var subclassification = SysmlFactory.eINSTANCE.createSubclassification();
            subclassification.setDeclaredName("specializes (implicit)");
            subclassification.setIsImplied(true);
            subclassification.setSubclassifier(classifier);
            subclassification.setSuperclassifier(implicitClassifier);
            return subclassification;
        }
        return null;
    }

    private Subsetting implicitSubsetting(Feature feature, String implicitSubsettedFeatureQualifiedName) {
        var implicitFeature = this.elementUtil.findByNameAndType(feature, implicitSubsettedFeatureQualifiedName, Feature.class);
        if (implicitFeature != null) {
            return this.implicitSubsetting(feature, implicitFeature);
        }
        return null;
    }

    private Subsetting implicitSubsetting(Feature feature, Feature implicitFeature) {
        var subsetting = SysmlFactory.eINSTANCE.createSubsetting();
        subsetting.setDeclaredName("subsets (implicit)");
        subsetting.setIsImplied(true);
        subsetting.setSubsettingFeature(feature);
        subsetting.setSubsettedFeature(implicitFeature);
        return subsetting;
    }

    private Subsetting implicitReferenceSubsetting(Feature feature, Feature implicitFeature) {
        var subsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
        subsetting.setDeclaredName("subsets (implicit)");
        subsetting.setIsImplied(true);
        subsetting.setSubsettingFeature(feature);
        subsetting.setSubsettedFeature(implicitFeature);
        return subsetting;
    }

    private FeatureTyping implicitTyping(Feature feature, String implicitTypeQualifiedName) {
        var implicitType = this.elementUtil.findByNameAndType(feature, implicitTypeQualifiedName, Type.class);
        if (implicitType != null) {
            var featureTyping = SysmlFactory.eINSTANCE.createFeatureTyping();
            featureTyping.setDeclaredName("typed by (implicit)");
            featureTyping.setIsImplied(true);
            featureTyping.setTypedFeature(feature);
            featureTyping.setType(implicitType);
            return featureTyping;
        }
        return null;
    }

    /**
     * Handle special case of {@link ReferenceUsage} inside the {@link EndFeatureMembership} of a
     * {@link SuccessionAsUsage}.
     *
     * <p>
     * The first two ReferenceUsage of a SuccessionAsUsage point to the source and target. If those ReferenceUsages do
     * not explicitly define a {@link ReferenceSubsetting} then a {@link ReferenceSubsetting} is computed from the
     * previous and next feature.
     * </p>
     *
     * @param referenceUsage
     *            the {@link ReferenceUsage} that might need modificationU
     * @param parentSuccessionAsUsage
     *            the parent element of the given {@link ReferenceUsage}
     * @return a list of {@link Specialization}
     */
    private List<Specialization> handleReferenceUsageInSuccessionAsUsage(ReferenceUsage referenceUsage, SuccessionAsUsage parentSuccessionAsUsage) {
        final List<Specialization> result;
        // At this moment we only handle the case of implicit source since we haven't found a
        // case where the target is implicit
        int index = parentSuccessionAsUsage.getOwnedFeature().indexOf(referenceUsage);
        boolean hasNoExpliciteSubsetting = this.getOwnedRelations(ReferenceSubsetting.class, referenceUsage).isEmpty();
        if (index == 0 && this.getOwnedRelations(ReferenceSubsetting.class, referenceUsage).isEmpty()) {
            // Source feature
            // Add a reference ReferenceSubsetting to the previous feature
            Feature sourceFeature = this.computeSourceFeature(parentSuccessionAsUsage);
            result = List.of(this.implicitReferenceSubsetting(referenceUsage, sourceFeature));
        } else if (index == 1 && hasNoExpliciteSubsetting) {
            // Target feature
            // Add a reference ReferenceSubsetting to the next feature
            Feature targetFeature = this.computeTargetFeature(parentSuccessionAsUsage);
            result = List.of(this.implicitReferenceSubsetting(referenceUsage, targetFeature));
        } else {
            result = List.of();
        }
        return result;
    }

    private <T extends Relationship> List<T> getOwnedRelations(Class<T> type, Element parent) {
        return parent.getOwnedRelationship().stream()
                .filter(type::isInstance)
                .map(type::cast)
                .toList();
    }

    private Feature computeSourceFeature(SuccessionAsUsage successionAsUsage) {
        Type owningType = successionAsUsage.getOwningType();
        if (owningType instanceof TransitionUsage transitionUsage && transitionUsage.getSuccession() == successionAsUsage) {
            return transitionUsage.getSource();
        }
        if (owningType != null) {
            EList<Membership> ownedMemberships = owningType.getOwnedMembership();
            int index = ownedMemberships.indexOf(successionAsUsage.getOwningMembership());
            if (index > 0) {
                ListIterator<Membership> iterator = ownedMemberships.subList(0, index).listIterator(index);
                while (iterator.hasPrevious()) {
                    Membership previous = iterator.previous();
                    if (previous.getMemberElement() instanceof Feature feature) {
                        return feature;
                    }
                }
            }
        }
        return null;
    }

    private Feature computeTargetFeature(SuccessionAsUsage successionAsUsage) {
        Type owningType = successionAsUsage.getOwningType();
        if (owningType instanceof TransitionUsage transitionUsage && transitionUsage.getSuccession() == successionAsUsage) {
            return transitionUsage.getTarget();
        } else if (owningType != null) {
            EList<Membership> ownedMemberships = owningType.getOwnedMembership();
            int index = ownedMemberships.indexOf(successionAsUsage.getOwningMembership());
            if (index > 0 && ownedMemberships.size() > index) {
                ListIterator<Membership> iterator = ownedMemberships.subList(index + 1, ownedMemberships.size()).listIterator();
                while (iterator.hasNext()) {
                    Membership next = iterator.next();
                    if (next.getMemberElement() instanceof Feature feature) {
                        return feature;
                    }
                }
            }
        }
        return null;
    }

    private boolean hasRedefinition(Element element) {
        return this.existingSpecializations.stream().anyMatch(Redefinition.class::isInstance);
    }

    private boolean hasSubclassification(Element element) {
        return this.existingSpecializations.stream().anyMatch(Subclassification.class::isInstance);
    }

    private boolean hasSubsetting(Element element) {
        return this.existingSpecializations.stream().anyMatch(spe -> spe instanceof Subsetting && !(spe instanceof Redefinition));
    }

    private boolean hasFeatureTyping(Element element) {
        return this.existingSpecializations.stream().anyMatch(FeatureTyping.class::isInstance);
    }
}
