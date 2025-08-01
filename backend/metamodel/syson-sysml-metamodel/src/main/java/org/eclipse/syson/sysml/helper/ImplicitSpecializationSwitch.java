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
import java.util.Optional;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.*;
import org.eclipse.syson.sysml.util.ILibraryNamespaceProvider;
import org.eclipse.syson.sysml.util.SysmlSwitch;

/**
 * Switch allowing to compute implicit specializations for a given Element.
 *
 * @author arichard
 */
public class ImplicitSpecializationSwitch extends SysmlSwitch<List<Specialization>> {

    private final ImplicitSpecializationAccumulator implicitSpecializations;

    private final ILibraryNamespaceProvider namespaceProvider;

    public ImplicitSpecializationSwitch(List<Specialization> existingSpecializations, ILibraryNamespaceProvider namespaceProvider) {
        this.namespaceProvider = namespaceProvider;
        this.implicitSpecializations = ImplicitSpecializationAccumulator.fromExistingSpecialization(existingSpecializations);
    }

    @Override
    public List<Specialization> doSwitch(EObject eObject) {
        super.doSwitch(eObject);
        return this.implicitSpecializations.getSpecializations();
    }

    @Override
    public List<Specialization> caseAcceptActionUsage(AcceptActionUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            String implicitElement = null;
            var owningType = object.getOwningType();
            if (object.isIsComposite() && object.isSubactionUsage() && !object.isTriggerAction()) {
                // A composite AcceptActionUsage that is a subaction usage, but is not the triggerAction of a
                // TransitionUsage, must directly or indirectly specialize the
                // ActionUsageActions::Action::acceptSubactions from the Systems Model Library.
                implicitElement = "Actions::Action::acceptSubactions";
            } else if (!object.isTriggerAction()) {
                // An AcceptActionUsage that is not the triggerAction of a TransitionUsage must directly or indirectly
                // specialize the ActionUsage Actions::acceptActions from the Systems Model Library.
                implicitElement = "Actions::acceptActions";
            } else if (object.isTriggerAction()) {
                // An AcceptActionUsage that is the triggerAction of TransitionUsage must directly or indirectly
                // specialize the ActionUsage Actions::TransitionAction::accepter from the Systems Model Library.
                implicitElement = "Actions::TransitionAction::accepter";
            }
            if (implicitElement != null) {
                var implicitSubsetting = this.implicitSubsetting(object, implicitElement);
                if (implicitSubsetting != null) {
                    this.implicitSpecializations.add(implicitSubsetting);
                }
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseReferenceUsage(ReferenceUsage referenceUsage) {

        Type owningType = referenceUsage.getOwningType();
        if (owningType instanceof SuccessionAsUsage successionAsUsage) {
            this.implicitSpecializations.addAll(this.handleReferenceUsageInSuccessionAsUsage(referenceUsage, successionAsUsage));
        }

        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseActionDefinition(ActionDefinition object) {
        if (!this.implicitSpecializations.hasSubclassification()) {
            var implicitSubclassification = this.implicitSubclassification(object, "Actions::Action");
            if (implicitSubclassification != null) {
                this.implicitSpecializations.add(implicitSubclassification);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseActionUsage(ActionUsage object) {
        var implicitSubsettingElement = "Actions::actions";
        // A composite ActionUsage whose owningType is PartDefinition or PartUsage must directly or indirectly
        // specialize the ActionUsage Parts::Part::ownedActions from the Systems Model Library.
        var owningType = object.getOwningType();
        var owningFeatureMembership = object.getOwningFeatureMembership();
        if (owningFeatureMembership instanceof StateSubactionMembership ssm && !this.implicitSpecializations.hasRedefinition()) {
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
                this.implicitSpecializations.add(implicitRedefinition);
            }
        } else if (object.isIsComposite() && (owningType instanceof PartDefinition || owningType instanceof PartUsage)) {
            implicitSubsettingElement = "Parts::Part::ownedActions";
        } else if (object.isIsComposite() && object.isSubactionUsage()) {
            // A composite ActionUsage that is a subaction usage must directly or indirectly specialize the ActionUsage
            // Actions::Action::subactions from the Systems Model Library.
            implicitSubsettingElement = "Actions::Action::subactions";
        }
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseAllocationDefinition(AllocationDefinition object) {
        if (!this.implicitSpecializations.hasSubclassification()) {
            var implicitSubclassification = this.implicitSubclassification(object, "Allocations::Allocation");
            if (implicitSubclassification != null) {
                this.implicitSpecializations.add(implicitSubclassification);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseAllocationUsage(AllocationUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsetting = this.implicitSubsetting(object, "Allocations::allocations");
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseAnalysisCaseDefinition(AnalysisCaseDefinition object) {
        if (!this.implicitSpecializations.hasSubclassification()) {
            var implicitSubclassification = this.implicitSubclassification(object, "AnalysisCases::AnalysisCase");
            if (implicitSubclassification != null) {
                this.implicitSpecializations.add(implicitSubclassification);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseAnalysisCaseUsage(AnalysisCaseUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
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
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseAssertConstraintUsage(AssertConstraintUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsettingElement = "Constraints::assertedConstraints";
            // If a AssertConstraintUsage is negated, then it must directly or indirectly specialize the ConstraintUsage
            // Constraints::negatedConstraints. Otherwise, it must directly or indirectly specialize the ConstraintUsage
            // Constraints::assertedConstraints.
            if (object.isIsNegated()) {
                implicitSubsettingElement = "Constraints::negatedConstraints";
            }
            var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseAssignmentActionUsage(AssignmentActionUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsettingElement = "Actions::assignmentActions";
            // A composite AssignmentActionUsage that is a subaction usage must directly or indirectly specialize the
            // ActionUsage Actions::Action::assignments from the Systems Model Library.
            if (object.isIsComposite() && object.isSubactionUsage()) {
                implicitSubsettingElement = "Actions::Action::assignments";
            }
            var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseAttributeUsage(AttributeUsage object) {
        if (this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsetting = this.implicitSubsetting(object, "Base::dataValues");
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseCalculationDefinition(CalculationDefinition object) {
        if (!this.implicitSpecializations.hasSubclassification()) {
            var implicitSubclassification = this.implicitSubclassification(object, "Calculations::Calculation");
            if (implicitSubclassification != null) {
                this.implicitSpecializations.add(implicitSubclassification);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseCalculationUsage(CalculationUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsettingElement = "Calculations::calculations";
            var owningType = object.getOwningType();
            if (owningType instanceof CalculationDefinition || owningType instanceof CalculationUsage) {
                implicitSubsettingElement = "Calculations::Calculation::subcalculations";
            }
            var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseCaseDefinition(CaseDefinition object) {
        if (!this.implicitSpecializations.hasSubclassification()) {
            var implicitSubclassification = this.implicitSubclassification(object, "Cases::Case");
            if (implicitSubclassification != null) {
                this.implicitSpecializations.add(implicitSubclassification);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseCaseUsage(CaseUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsettingElement = "Cases::cases";
            // A composite CaseUsage whose owningType is a CaseDefinition or CaseUsage must directly or indirectly
            // specialize the CaseUsage Cases::Case::subcases.
            var owningType = object.getOwningType();
            if (owningType instanceof CaseDefinition || owningType instanceof CaseUsage) {
                implicitSubsettingElement = "Cases::Case::subcases";
            }
            var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseConcernDefinition(ConcernDefinition object) {
        if (!this.implicitSpecializations.hasSubclassification()) {
            var implicitSubclassification = this.implicitSubclassification(object, "Requirements::ConcernCheck");
            if (implicitSubclassification != null) {
                this.implicitSpecializations.add(implicitSubclassification);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseConcernUsage(ConcernUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsettingElement = "Requirements::concernChecks";
            // If a ConcernUsage is owned via a FramedConcernMembership, then it must directly or indirectly specialize
            // the ConcernUsage Requirements::RequirementCheck::concerns from the Systems Model Library.
            var owningFeatureMembership = object.getOwningFeatureMembership();
            if (owningFeatureMembership instanceof FramedConcernMembership) {
                implicitSubsettingElement = "Requirements::RequirementCheck::concerns";
            }
            var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseConnectionDefinition(ConnectionDefinition object) {
        if (!this.implicitSpecializations.hasSubclassification()) {
            var implicitSubclassificationElement = "Connections::Connection";
            // A binary ConnectionDefinition must directly or indirectly specialize the ConnectionDefinition
            // Connections::BinaryConnection from the Systems Model Library.
            if (object.getOwnedEndFeature().size() == 2) {
                implicitSubclassificationElement = "Connections::BinaryConnection";
            }
            var implicitSubclassification = this.implicitSubclassification(object, implicitSubclassificationElement);
            if (implicitSubclassification != null) {
                this.implicitSpecializations.add(implicitSubclassification);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseConnectionUsage(ConnectionUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsettingElement = "Connections::connections";
            // A binary ConnectionUsage must directly or indirectly specialize the ConnectionUsage
            // Connections::binaryConnections from the Systems Model Library.
            if (object.getOwnedEndFeature().size() == 2) {
                implicitSubsettingElement = "Connections::binaryConnections";
            }
            var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }

        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseConstraintDefinition(ConstraintDefinition object) {
        if (!this.implicitSpecializations.hasSubclassification()) {
            var implicitSubclassification = this.implicitSubclassification(object, "Constraints::ConstraintCheck");
            if (implicitSubclassification != null) {
                this.implicitSpecializations.add(implicitSubclassification);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseConstraintUsage(ConstraintUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsettingElement = "Constraints::constraintChecks";
            // A ConstraintUsage whose owningType is an ItemDefinition or ItemUsage must directly or indirectly
            // specialize the ConstraintUsage Items::Item::checkedConstraints.
            var owningType = object.getOwningType();
            if (owningType instanceof ItemDefinition || owningType instanceof ItemUsage) {
                implicitSubsettingElement = "Items::Item::checkedConstraints";
            }
            var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
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
                    this.implicitSpecializations.add(implicitSubsetting);
                }
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseControlNode(ControlNode object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsetting = this.implicitSubsetting(object, "Action::Action::controls");
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseDecisionNode(DecisionNode object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsetting = this.implicitSubsetting(object, "Actions::Action::decisions");
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseEventOccurrenceUsage(EventOccurrenceUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            // If an EventOccurrenceUsage has an owningType that is an OccurrenceDefinition or OccurrenceUsage, then it
            // must directly or indirectly specialize the Feature Occurrences::Occurrence::timeEnclosedOccurrences.
            var owningType = object.getOwningType();
            if (owningType instanceof OccurrenceDefinition || owningType instanceof OccurrenceUsage) {
                var implicitSubsetting = this.implicitSubsetting(object, "Occurrences::Occurrence::timeEnclosedOccurrences");
                if (implicitSubsetting != null) {
                    this.implicitSpecializations.add(implicitSubsetting);
                }
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseExhibitStateUsage(ExhibitStateUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            // If an ExhibitStateUsage has an owningType that is a PartDefinition or PartUsage, then it must directly or
            // indirectly specialize the StateUsage Parts::Part::exhibitedStates.
            var owningType = object.getOwningType();
            if (owningType instanceof PartDefinition || owningType instanceof PartUsage) {
                var implicitSubsetting = this.implicitSubsetting(object, "Parts::Part::exhibitedStates");
                if (implicitSubsetting != null) {
                    this.implicitSpecializations.add(implicitSubsetting);
                }
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseFeature(Feature object) {
        if (!this.implicitSpecializations.hasRedefinition()) {
            this.implicitSpecializations.addAll(this.handleImplicitParameterRedefinition(object));
        }
        // The specification states that "If the Feature has chainingFeatures, then the union also includes the types of
        // the last chainingFeature".We need to implement this here in order to make inherited Feature resolvable.
        // If we only implement this in "getType" of the Feature implementation the general mechanism of name resolution
        // that relies on "getMembership" would fail.
        // Normally the implicit typing would be present in the model (either with an implicit inheritance
        // or implicit typing). But since in SysON we choose to add those "implicit" element virtually when the derived
        // feature are called, we needed to find a place where this 'implicit' typing would impact both Feature.getType
        // and Namespace.getMembership. implementations.

        EList<Feature> chainingFeature = object.getChainingFeature();
        if (!chainingFeature.isEmpty()) {
            Feature lastFeature = chainingFeature.get(chainingFeature.size() - 1);
            for (Type type : lastFeature.getType()) {
                FeatureTyping featureTyping = SysmlFactory.eINSTANCE.createFeatureTyping();
                this.implicitSpecializations.add(featureTyping);
                featureTyping.setType(type);
                featureTyping.setTypedFeature(object);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseFlowDefinition(FlowDefinition object) {
        if (!this.implicitSpecializations.hasSubclassification()) {
            var implicitSubclassification = this.implicitSubclassification(object, "Connections::MessageConnection");
            if (implicitSubclassification != null) {
                this.implicitSpecializations.add(implicitSubclassification);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseFlowUsage(FlowUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsettingElement = "Connections::flowConnections";
            // If a FlowUsage has no ownedEndFeatures, then it must directly or indirectly specialize the base
            // FlowUsage Connections::messageConnections from the Systems Library model.
            if (object.getOwnedEndFeature().isEmpty()) {
                implicitSubsettingElement = "Connections::messageConnections";
            }
            var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseForkNode(ForkNode object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsetting = this.implicitSubsetting(object, "Actions::Action::forks");
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseForLoopActionUsage(ForLoopActionUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsettingElement = "Actions::forLoopActions";
            // A composite ForLoopActionUsage that is a subaction usage must directly or indirectly specialize the
            // ActionUsage Actions::Action::forLoops from the Systems Model Library.
            if (object.isIsComposite() && object.isSubactionUsage()) {
                implicitSubsettingElement = "Actions::Action::forLoops";
            }
            var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseIfActionUsage(IfActionUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsettingElement = "Actions::ifThenActions";
            if (object.getElseAction() != null) {
                // A IfActionUsage must directly or indirectly specialize the ActionUsage Actions::ifThenActions from
                // the Systems Model Library. If it has an elseAction, then it must directly or indirectly specialize
                // Actions::ifThenElseActions
                implicitSubsettingElement = "Actions::ifThenElseActions";
            } else if (object.isIsComposite() && object.isSubactionUsage()) {
                // A composite IfActionUsage that is a subaction usage must directly or indirectly specialize the
                // ActionUsage Actions::Action::ifSubactions from the Systems Model Library.
                implicitSubsettingElement = "Actions::Action::ifSubactions";
            }
            var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseIncludeUseCaseUsage(IncludeUseCaseUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            // A IncludeUseCaseUsage whose owningType is a UseCaseDefinition or UseCaseUsage must directly or indirectly
            // specialize the UseCaseUsage UseCases::UseCase::includedUseCases from the Systems Model Library.
            var owningType = object.getOwningType();
            if (owningType instanceof UseCaseDefinition || owningType instanceof UseCaseUsage) {
                var implicitSubsetting = this.implicitSubsetting(object, "UseCases::UseCase::includedUseCases");
                if (implicitSubsetting != null) {
                    this.implicitSpecializations.add(implicitSubsetting);
                }
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseInterfaceDefinition(InterfaceDefinition object) {
        if (!this.implicitSpecializations.hasSubclassification()) {
            var implicitSubclassificationElement = "Interfaces::Interface";
            // A binary InterfaceDefinition must directly or indirectly specialize the InterfaceDefinition
            // Interfaces::BinaryInterface from the Systems Model Library.
            if (object.getOwnedEndFeature().size() == 2) {
                implicitSubclassificationElement = "Interfaces::BinaryInterface";
            }
            var implicitSubclassification = this.implicitSubclassification(object, implicitSubclassificationElement);
            if (implicitSubclassification != null) {
                this.implicitSpecializations.add(implicitSubclassification);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseInterfaceUsage(InterfaceUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsettingElement = "Interfaces::interfaces";
            // A binary InterfaceUsage must directly or indirectly specialize the InterfaceUsage
            // Interfaces::binaryInterfaces from the Systems Model Library.
            if (object.getOwnedEndFeature().size() == 2) {
                implicitSubsettingElement = "Interfaces::binaryInterfaces";
            }
            var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseItemDefinition(ItemDefinition object) {
        if (!this.implicitSpecializations.hasSubclassification()) {
            var implicitSubclassification = this.implicitSubclassification(object, "Items::Item");
            if (implicitSubclassification != null) {
                this.implicitSpecializations.add(implicitSubclassification);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseItemUsage(ItemUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsetting = this.implicitSubsetting(object, "Items::items");
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseJoinNode(JoinNode object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsetting = this.implicitSubsetting(object, "Actions::Action::join");
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseMergeNode(MergeNode object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsetting = this.implicitSubsetting(object, "Actions::Action::merges");
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseMetadataDefinition(MetadataDefinition object) {
        if (!this.implicitSpecializations.hasSubclassification()) {
            var implicitSubclassification = this.implicitSubclassification(object, "Metadata::MetadataItem");
            if (implicitSubclassification != null) {
                this.implicitSpecializations.add(implicitSubclassification);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseMetadataUsage(MetadataUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsetting = this.implicitSubsetting(object, "Metadata::metadataItems");
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseMultiplicity(Multiplicity object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsetting = this.implicitSubsetting(object, "Base::naturals");
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseOccurrenceDefinition(OccurrenceDefinition object) {
        if (!this.implicitSpecializations.hasSubclassification()) {
            var implicitSubclassification = this.implicitSubclassification(object, "Occurrences::Occurrence");
            if (implicitSubclassification != null) {
                this.implicitSpecializations.add(implicitSubclassification);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseOccurrenceUsage(OccurrenceUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsetting = this.implicitSubsetting(object, "Occurrences::occurrences");
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> casePartDefinition(PartDefinition object) {
        if (!this.implicitSpecializations.hasSubclassification()) {
            var implicitSubclassification = this.implicitSubclassification(object, "Parts::Part");
            if (implicitSubclassification != null) {
                this.implicitSpecializations.add(implicitSubclassification);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> casePartUsage(PartUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsettingElement = "Parts::parts";
            var owningType = object.getOwningType();
            if (object.getOwningFeatureMembership() instanceof ActorMembership) {
                // If a PartUsage is owned via an ActorMembership, then it must directly or indirectly specialize either
                // Requirements::RequirementCheck::actors (if its owningType is a RequirementDefinition or
                // RequirementUsage or Cases::Case::actors (otherwise).
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
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> casePerformActionUsage(PerformActionUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            // If a PerformActionUsage has an owningType that is a PartDefinition or PartUsage, then it must directly or
            // indirectly specialize the ActionUsage Parts::Part::performedActions.
            var owningType = object.getOwningType();
            if (owningType instanceof PartDefinition || owningType instanceof PartUsage) {
                var implicitSubsetting = this.implicitSubsetting(object, "Parts::Part::performedActions");
                if (implicitSubsetting != null) {
                    this.implicitSpecializations.add(implicitSubsetting);
                }
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> casePortDefinition(PortDefinition object) {
        if (!this.implicitSpecializations.hasSubclassification()) {
            var implicitSubclassification = this.implicitSubclassification(object, "Ports::Port");
            if (implicitSubclassification != null) {
                this.implicitSpecializations.add(implicitSubclassification);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> casePortUsage(PortUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsettingElement = "Ports::ports";
            // A composite PortUsage with an owningType that is a PortDefinition or PortUsage must directly or
            // indirectly specialize the PortUsage Ports::Port::subports from the Systems Model Library.
            var owningType = object.getOwningType();
            if (object.isIsComposite() && (owningType instanceof PortDefinition || owningType instanceof PortUsage)) {
                implicitSubsettingElement = "Ports::Port::subports";
            }
            var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseRenderingDefinition(RenderingDefinition object) {
        if (!this.implicitSpecializations.hasSubclassification()) {
            var implicitSubclassification = this.implicitSubclassification(object, "Views::Rendering");
            if (implicitSubclassification != null) {
                this.implicitSpecializations.add(implicitSubclassification);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseRenderingUsage(RenderingUsage object) {
        var owningFeatureMembership = object.getOwningFeatureMembership();
        if (owningFeatureMembership instanceof ViewRenderingMembership && !(this.implicitSpecializations.hasRedefinition())) {
            // A RenderingUsage whose owningFeatureMembership is a ViewRenderingMembership must redefine the
            // RenderingUsage Views::View::viewRendering
            var implicitRedefinitionElement = "Views::View::viewRendering";
            var implicitRedefinition = this.implicitRedefinition(object, implicitRedefinitionElement);
            if (implicitRedefinition != null) {
                this.implicitSpecializations.add(implicitRedefinition);
            }
        }
        if (this.implicitSpecializations.getSpecializations().isEmpty()) {
            if (!this.implicitSpecializations.hasSubSetting()) {
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
                    this.implicitSpecializations.add(implicitSubsetting);
                }
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseRequirementDefinition(RequirementDefinition object) {
        if (!this.implicitSpecializations.hasSubclassification()) {
            var implicitSubclassification = this.implicitSubclassification(object, "Requirements::RequirementCheck");
            if (implicitSubclassification != null) {
                this.implicitSpecializations.add(implicitSubclassification);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseRequirementUsage(RequirementUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsettingElement = "Requirements::requirementChecks";
            var owningType = object.getOwningType();
            var owningFeatureMembership = object.getOwningFeatureMembership();
            if (object.isIsComposite() && (owningType instanceof RequirementDefinition || owningType instanceof RequirementUsage)) {
                // A composite RequirementUsage whose owningType is a RequirementDefinition or RequirementUsage must
                // directly or indirectly specialize the RequirementUsage
                // Requirements::RequirementCheck::subrequirements from the Systems Model Library.
                implicitSubsettingElement = "Requirements::RequirementCheck::subrequirements";
            } else if (owningFeatureMembership instanceof RequirementVerificationMembership) {
                // RequirementUsage whose owningFeatureMembership is a RequirementVerificationMembership must directly
                // or indirectly specialize the RequirementUsage
                // VerificationCases::VerificationCase::obj::requirementVerifications.
                implicitSubsettingElement = "VerificationCases::VerificationCase::obj::requirementVerifications";
            }
            var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }

        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseSendActionUsage(SendActionUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsettingElement = "Actions::sendActions";
            // A composite SendActionUsage that is a subaction must directly or indirectly specialize the ActionUsage
            // Actions::Action::sendSubactions from the Systems Model Library.
            if (object.isIsComposite() && object.isSubactionUsage()) {
                implicitSubsettingElement = "Actions::Action::acceptSubactions";
            }
            var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseStateDefinition(StateDefinition object) {
        if (!this.implicitSpecializations.hasSubclassification()) {
            var implicitSubclassification = this.implicitSubclassification(object, "States::StateAction");
            if (implicitSubclassification != null) {
                this.implicitSpecializations.add(implicitSubclassification);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseStateUsage(StateUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsettingElement = "States::stateActions";
            // A StateUsage that is a substate usage with a non-parallel owning StateDefinition or StateUsage must
            // directly or indirectly specialize the StateUsage States::StateAction::exclusiveStates from the Systems
            // Model Library.
            if (object.isSubstateUsage(false)) {
                implicitSubsettingElement = "States::StateAction::exclusiveStates";
            } else if (object.isSubstateUsage(true)) {
                implicitSubsettingElement = "States::StateAction::substates";
            }
            var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseSuccessionFlowUsage(SuccessionFlowUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsetting = this.implicitSubsetting(object, "Flows::successionFlows");
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseTransitionUsage(TransitionUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsettingElement = "Actions::transitionActions";
            // A composite TransitionUsage whose owningType is a ActionDefinition or ActionUsage, but not a
            // StateDefinition or StateUsage, must directly or indirectly specialize the ActionUsage
            // Actions::Action::decisionTransitions from the Systems Model Library. A composite TransitionUsage whose
            // owningType is a StateDefinition or StateUsage must directly or indirectly specialize the ActionUsage
            // States::State::stateTransitions from the Systems Model Library.
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
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseTriggerInvocationExpression(TriggerInvocationExpression object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            // A TriggerInvocationExpression must directly or indirectly specialize one of the Functions TriggerWhen,
            // TriggerAt or TriggerAfter, from the Kernel Semantic Library Triggers package, depending on whether its
            // kind is when, at or after, respectively.
            var implicitSubsettingElement = "Triggers::TriggerAfter";
            if (object.getKind() == TriggerKind.WHEN) {
                implicitSubsettingElement = "Triggers::TriggerWhen";
            } else if (object.getKind() == TriggerKind.AT) {
                implicitSubsettingElement = "Triggers::TriggerAt";
            }
            var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseType(Type object) {
        // Describe limitation with use of external metadata
        List<MetadataUsage> metadataUsage = object.getOwnedMembership().stream()
                .map(Membership::getMemberElement)
                .filter(MetadataUsage.class::isInstance)
                .map(MetadataUsage.class::cast)
                .toList();

        // Avoid any computation if no metadata usage is defined
        if (!metadataUsage.isEmpty()) {
            /*
             * This code gets the MetadaUsage contained inside the object.
             */
            Type semanticMedatadaMetaclass = this.namespaceProvider.getNamespaceFromLibrary("Metaobjects::SemanticMetadata", Type.class);
            List<Type> baseTypes = metadataUsage.stream()
                    .filter(MetadataUsage::isSemantic)
                    .flatMap(mu -> this.getBaseTypes(mu).stream())
                    .toList();

            boolean isAnnotatedTypeAUsage = object instanceof Usage;
            boolean isAnnotatedTypeADefinition = object instanceof Definition;

            for (Type baseType : baseTypes) {
                if (isAnnotatedTypeADefinition) {
                    Definition annotatedDefinition = (Definition) object;
                    if (baseType instanceof Classifier baseClassifier) {
                        this.implicitSpecializations.add(this.implicitSubclassification(annotatedDefinition, baseClassifier));
                    } else if (baseType instanceof Feature baseFeature) {
                        baseFeature.getType().stream()
                                .filter(Classifier.class::isInstance)
                                .map(Classifier.class::cast)
                                .forEach(baseClassifier -> {
                                    this.implicitSpecializations.add(this.implicitSubclassification(annotatedDefinition, baseClassifier));
                                });
                    }
                } else if (isAnnotatedTypeAUsage) {
                    Usage annotatedUsage = (Usage) object;
                    if (baseType instanceof Feature basefeature) {
                        this.implicitSpecializations.add(this.implicitReferenceSubsetting(annotatedUsage, basefeature));
                    }
                }
            }
        }

        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseUseCaseDefinition(UseCaseDefinition object) {
        if (!this.implicitSpecializations.hasSubclassification()) {
            var implicitSubclassification = this.implicitSubclassification(object, "UseCases::UseCase");
            if (implicitSubclassification != null) {
                this.implicitSpecializations.add(implicitSubclassification);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseUseCaseUsage(UseCaseUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsettingElement = "UseCases::useCases";
            // A composite UseCaseUsage whose owningType is a UseCaseDefinition or UseCaseUsage must specialize the
            // UseCaseUsage UseCases::UseCase::subUseCases from the Systems Model Library.
            var owningType = object.getOwningType();
            if (object.isIsComposite() && (owningType instanceof UseCaseDefinition || owningType instanceof UseCaseUsage)) {
                implicitSubsettingElement = "UseCases::UseCase::subUseCases";
            }
            var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }

        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseVerificationCaseDefinition(VerificationCaseDefinition object) {
        if (!this.implicitSpecializations.hasSubclassification()) {
            var implicitSubclassification = this.implicitSubclassification(object, "VerificationCases::VerificationCase");
            if (implicitSubclassification != null) {
                this.implicitSpecializations.add(implicitSubclassification);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseVerificationCaseUsage(VerificationCaseUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsettingElement = "VerificationCases::verificationCases";
            // If it is composite and owned by a VerificationCaseDefinition or VerificationCaseUsage, then it must
            // specialize VerificationCaseUsage VerificationCases::VerificationCase::subVerificationCases.
            var owningType = object.getOwningType();
            if (object.isIsComposite() && (owningType instanceof VerificationCaseDefinition || owningType instanceof VerificationCaseUsage)) {
                implicitSubsettingElement = "VerificationCases::VerificationCase::subVerificationCases";
            }
            var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseViewDefinition(ViewDefinition object) {
        if (!this.implicitSpecializations.hasSubclassification()) {
            var implicitSubclassification = this.implicitSubclassification(object, "Views::View");
            if (implicitSubclassification != null) {
                this.implicitSpecializations.add(implicitSubclassification);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseViewpointDefinition(ViewpointDefinition object) {
        if (!this.implicitSpecializations.hasSubclassification()) {
            var implicitSubclassification = this.implicitSubclassification(object, "Views::Viewpoint");
            if (implicitSubclassification != null) {
                this.implicitSpecializations.add(implicitSubclassification);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseViewpointUsage(ViewpointUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsettingElement = "Views::viewpoints";
            // A composite ViewpointUsage whose owningType is a ViewDefinition or ViewUsage must directly or indirectly
            // specialize the ViewpointUsage Views::View::viewpointSatisfactions from the Systems Model Library.
            var owningType = object.getOwningType();
            if (object.isIsComposite() && (owningType instanceof ViewDefinition || owningType instanceof ViewUsage)) {
                implicitSubsettingElement = "Views::View::viewpointSatisfactions";
            }
            var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseViewUsage(ViewUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsettingElement = "Views::views";
            // A ViewUsage whose owningType is a ViewDefinition or ViewUsage must specialize the ViewUsage
            // Views::View::subviews from the Systems Library Model.
            var owningType = object.getOwningType();
            if (owningType instanceof ViewDefinition || owningType instanceof ViewUsage) {
                implicitSubsettingElement = "Views::View::subviews";
            }
            var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    @Override
    public List<Specialization> caseWhileLoopActionUsage(WhileLoopActionUsage object) {
        if (!this.implicitSpecializations.hasSubSetting()) {
            var implicitSubsettingElement = "Actions::whileLoopActions";
            // A composite WhileLoopActionUsage that is a subaction usage must directly or indirectly specialize the
            // ActionUsage Actions::Action::whileLoops from the Systems Model Library.
            if (object.isIsComposite() && object.isSubactionUsage()) {
                implicitSubsettingElement = "Actions::Action::whileLoops";
            }
            var implicitSubsetting = this.implicitSubsetting(object, implicitSubsettingElement);
            if (implicitSubsetting != null) {
                this.implicitSpecializations.add(implicitSubsetting);
            }
        }
        // Return null to iterate on other abstract EClass cases
        return null;
    }

    private List<Type> getBaseTypes(MetadataUsage metadaUsage) {
        String baseTypeFeatureQn = "Metaobjects::SemanticMetadata::baseType";
        Feature baseTypeFeature = this.namespaceProvider.getNamespaceFromLibrary(baseTypeFeatureQn, Feature.class);
        Expression valueExpression = metadaUsage.getMetadataDefinition().getOwnedFeature().stream()
                .filter(f -> f.supertypes(true).contains(baseTypeFeature))
                .findFirst()
                .map(this::getValue)
                .orElse(null);

        if (valueExpression instanceof OperatorExpression opExpression && "meta".equals(opExpression.getOperator())) {
            return opExpression.getParameter().stream()
                    .map(this::getValue)
                    .filter(MetadataAccessExpression.class::isInstance)
                    .map(MetadataAccessExpression.class::cast)
                    .map(MetadataAccessExpression::getReferencedElement)
                    .filter(Type.class::isInstance)
                    .map(Type.class::cast)
                    .toList();
        } else {
            return List.of();
        }
    }

    private Expression getValue(Feature f) {
        return f.getOwnedMembership().stream()
                .filter(FeatureValue.class::isInstance)
                .map(FeatureValue.class::cast)
                .findFirst()
                .map(FeatureValue::getValue)
                .orElse(null);
    }

    private Redefinition implicitRedefinition(Feature feature, String implicitRedefinedFeatureQualifiedName) {
        var implicitFeature = this.namespaceProvider.getNamespaceFromLibrary(implicitRedefinedFeatureQualifiedName, Feature.class);
        if (implicitFeature != null) {
            return this.implicitRedefinition(feature, implicitFeature);
        }
        return null;
    }

    private Redefinition implicitRedefinition(Feature redefiningFeature, Feature redefinedFeature) {
        var redefinition = SysmlFactory.eINSTANCE.createRedefinition();
        redefinition.setDeclaredName("redefines (implicit)");
        redefinition.setIsImplied(true);
        redefinition.setRedefiningFeature(redefiningFeature);
        redefinition.setRedefinedFeature(redefinedFeature);
        return redefinition;
    }

    private Subclassification implicitSubclassification(Classifier classifier, String implicitSuperclassifierQualifiedName) {
        var implicitClassifier = this.namespaceProvider.getNamespaceFromLibrary(implicitSuperclassifierQualifiedName, Classifier.class);
        if (implicitClassifier != null) {
            return this.implicitSubclassification(classifier, implicitClassifier);
        }
        return null;
    }

    private Subclassification implicitSubclassification(Classifier classifier, Classifier implicitClassifier) {
        var subclassification = SysmlFactory.eINSTANCE.createSubclassification();
        subclassification.setDeclaredName("subclasses (implicit)");
        subclassification.setIsImplied(true);
        subclassification.setSubclassifier(classifier);
        subclassification.setSuperclassifier(implicitClassifier);
        return subclassification;
    }

    private Subsetting implicitSubsetting(Feature feature, String implicitSubsettedFeatureQualifiedName) {
        var implicitFeature = this.namespaceProvider.getNamespaceFromLibrary(implicitSubsettedFeatureQualifiedName, Feature.class);
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
        var implicitType = this.namespaceProvider.getNamespaceFromLibrary(implicitTypeQualifiedName, Type.class);
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

    private <T extends Relationship> List<T> getOwnedRelations(java.lang.Class<T> type, Element parent) {
        return parent.getOwnedRelationship().stream()
                .filter(type::isInstance)
                .map(type::cast)
                .toList();
    }

    private Feature computeSourceFeature(SuccessionAsUsage successionAsUsage) {
        Namespace owningNamespace = successionAsUsage.getOwningNamespace();
        if (owningNamespace instanceof TransitionUsage transitionUsage && transitionUsage.getSuccession() == successionAsUsage) {
            return transitionUsage.getSource();
        }
        if (owningNamespace != null) {
            EList<Membership> ownedMemberships = owningNamespace.getOwnedMembership();
            int index = ownedMemberships.indexOf(successionAsUsage.getOwningMembership());
            if (index > 0) {
                ListIterator<Membership> iterator = ownedMemberships.subList(0, index).listIterator(index);
                while (iterator.hasPrevious()) {
                    Membership previous = iterator.previous();
                    if (previous.getMemberElement() instanceof Feature feature && this.isValidSourceOrTargetFeaturForSuccession(feature)) {
                        return feature;
                    }
                }
            }
        }
        return null;
    }

    private Feature computeTargetFeature(SuccessionAsUsage successionAsUsage) {
        Namespace owningNamespace = successionAsUsage.getOwningNamespace();
        if (owningNamespace instanceof TransitionUsage transitionUsage && transitionUsage.getSuccession() == successionAsUsage) {
            return transitionUsage.getTarget();
        } else if (owningNamespace != null) {
            EList<Membership> ownedMemberships = owningNamespace.getOwnedMembership();
            int index = ownedMemberships.indexOf(successionAsUsage.getOwningMembership());
            if (index > 0 && ownedMemberships.size() > index) {
                ListIterator<Membership> iterator = ownedMemberships.subList(index + 1, ownedMemberships.size()).listIterator();
                while (iterator.hasNext()) {
                    Membership next = iterator.next();
                    if (next.getMemberElement() instanceof Feature feature && this.isValidSourceOrTargetFeaturForSuccession(feature)) {
                        return feature;
                    }
                }
            }
        }
        return null;
    }

    private boolean isValidSourceOrTargetFeaturForSuccession(Feature feature) {
        return !(feature instanceof Connector || feature instanceof TransitionUsage);
    }

    /**
     * Handle the creation of implicit redefinitions for feature that are parameters of their owner.
     * <p>
     * This method implements KerML 7.4.7.2 and 7.4.7.3, and ensures that a parameter implicitly redefines the
     * corresponding parameters of its owner's specializations that are {@link Behavior} or {@link Step}.
     * </p>
     *
     * @param feature
     *            the feature to redefine
     * @return the list of {@link Redefinition} for the provided {@code feature}
     */
    private List<Redefinition> handleImplicitParameterRedefinition(Feature feature) {
        List<Redefinition> implicitRedefinitions = new ArrayList<>();
        if (feature.getOwner() instanceof Step stepOwner) {
            implicitRedefinitions = this.handleImplicitParameterRedefinition(feature, stepOwner);
        } else if (feature.getOwner() instanceof Behavior behaviorOwner) {
            implicitRedefinitions = this.handleImplicitParameterRedefinition(feature, behaviorOwner);
        }
        return implicitRedefinitions;
    }

    private List<Redefinition> handleImplicitParameterRedefinition(Feature feature, Step owner) {
        List<Redefinition> implicitRedefinitions = new ArrayList<>();
        int parameterIndex = owner.getParameter().indexOf(feature);
        if (parameterIndex >= 0) {
            implicitRedefinitions = owner.getOwnedSpecialization().stream()
                    .map(Specialization::getGeneral)
                    .filter(type -> type instanceof Behavior || type instanceof Step)
                    .map(type -> this.createImplicitParameterRedefinition(feature, type, parameterIndex))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
        }
        return implicitRedefinitions;
    }

    private List<Redefinition> handleImplicitParameterRedefinition(Feature feature, Behavior owner) {
        List<Redefinition> implicitRedefinitions = new ArrayList<>();
        int parameterIndex = owner.getParameter().indexOf(feature);
        if (parameterIndex >= 0) {
            implicitRedefinitions = owner.getOwnedSubclassification().stream()
                    .map(Subclassification::getSuperclassifier)
                    .filter(Behavior.class::isInstance)
                    .map(classifier -> this.createImplicitParameterRedefinition(feature, classifier, parameterIndex))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
        }
        return implicitRedefinitions;
    }

    /**
     * Creates an implicit redefinition from {@code feature} to the {@code index} parameter of {@code type}.
     * <p>
     * This method creates the redefinition if:
     * <ul>
     * <li>The provided {@code type} contains parameters</li>
     * <li>The {@code index} parameter of {@code type} exists</li>
     * <li>The {@code index} parameter of {@code type} has the same direction as the provided {@code feature}.</li>
     * </ul>
     * </p>
     *
     * @param feature
     *            the feature redefining the parameter
     * @param type
     *            the type containing the redefined parameter
     * @param index
     *            the index of the redefined parameter in {@code type}
     * @return
     */
    private Optional<Redefinition> createImplicitParameterRedefinition(Feature feature, Type type, int index) {
        Optional<Redefinition> result = Optional.empty();
        List<Feature> parameters = new ArrayList<>();
        if (type instanceof Behavior behavior) {
            parameters = behavior.getParameter();
        } else if (type instanceof Step step) {
            parameters = step.getParameter();
        }
        if (parameters.size() > index) {
            if (parameters.get(index).getDirection() == feature.getDirection()) {
                result = Optional.ofNullable(this.implicitRedefinition(feature, parameters.get(index)));
            }
        }
        return result;
    }
}
