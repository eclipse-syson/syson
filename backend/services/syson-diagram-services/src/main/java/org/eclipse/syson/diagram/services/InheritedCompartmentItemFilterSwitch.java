/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
package org.eclipse.syson.diagram.services;

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Behavior;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.ExhibitStateUsage;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PerformActionUsage;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.RequirementConstraintKind;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.Step;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.util.SysmlSwitch;

/**
 * Filters inherited compartment items according to the semantics of a compartment reference.
 *
 * @author arichard
 */
public class InheritedCompartmentItemFilterSwitch extends SysmlSwitch<Boolean> {

    private final EReference eReference;

    /**
     * Creates the filter for the given compartment reference.
     *
     * @param eReference
     *            the compartment reference used to decide whether an inherited feature should be kept
     */
    public InheritedCompartmentItemFilterSwitch(EReference eReference) {
        this.eReference = Objects.requireNonNull(eReference);
    }

    /**
     * Returns {@code false} for unsupported semantic objects.
     *
     * @param object
     *            the semantic object to inspect
     * @return always {@code false}
     */
    @Override
    public Boolean defaultCase(EObject object) {
        return false;
    }

    /**
     * Filters inherited requirement constraints according to the targeted compartment kind.
     *
     * @param object
     *            the inherited constraint usage
     * @return {@code true} when the constraint belongs to the requested compartment
     */
    @Override
    public Boolean caseConstraintUsage(ConstraintUsage object) {
        boolean keep = true;
        OwningMembership owningMembership = object.getOwningMembership();
        if (owningMembership instanceof RequirementConstraintMembership constraintMembership) {
            RequirementConstraintKind constraintKind = constraintMembership.getKind();
            if (SysmlPackage.eINSTANCE.getRequirementDefinition_AssumedConstraint().equals(this.eReference)) {
                keep = RequirementConstraintKind.ASSUMPTION == constraintKind;
            } else if (SysmlPackage.eINSTANCE.getRequirementDefinition_RequiredConstraint().equals(this.eReference)) {
                keep = RequirementConstraintKind.REQUIREMENT == constraintKind;
            } else if (SysmlPackage.eINSTANCE.getRequirementUsage_AssumedConstraint().equals(this.eReference)) {
                keep = RequirementConstraintKind.ASSUMPTION == constraintKind;
            } else if (SysmlPackage.eINSTANCE.getRequirementUsage_RequiredConstraint().equals(this.eReference)) {
                keep = RequirementConstraintKind.REQUIREMENT == constraintKind;
            }
        }
        return keep && this.caseFeature(object);
    }

    /**
     * Filters inherited exhibit states, including the parameter-compartment special case.
     *
     * @param object
     *            the inherited exhibit state
     * @return {@code true} when the feature should be shown in the target compartment
     */
    @Override
    public Boolean caseExhibitStateUsage(ExhibitStateUsage object) {
        final Boolean result;
        if (this.shouldConsiderParameter(object)) {
            result = this.isInheritedParameter(object);
        } else if (this.shouldConsiderExhibitState(object)) {
            result = this.isInheritedState(object);
        } else {
            result = super.caseExhibitStateUsage(object);
        }
        return result;
    }

    /**
     * Filters inherited features against the target compartment type.
     *
     * @param object
     *            the inherited feature
     * @return {@code true} when the feature matches the compartment
     */
    @Override
    public Boolean caseFeature(Feature object) {
        if (this.shouldConsiderParameter(object)) {
            return this.isInheritedParameter(object);
        }
        return this.eReference.getEType().equals(object.eClass());
    }

    /**
     * Filters inherited part usages against the target compartment type.
     *
     * @param object
     *            the inherited part usage
     * @return {@code true} when the part usage matches the compartment
     */
    @Override
    public Boolean casePartUsage(PartUsage object) {
        if (this.shouldConsiderParameter(object)) {
            return this.isInheritedParameter(object);
        }
        EClassifier eType = this.eReference.getEType();
        EClass eClass = object.eClass();
        return eType.equals(eClass) || (eType instanceof EClass eTypeEClass && eTypeEClass.isSuperTypeOf(eClass));
    }

    /**
     * Filters inherited perform actions, including the action-compartment special case.
     *
     * @param object
     *            the inherited perform action usage
     * @return {@code true} when the feature should be shown in the target compartment
     */
    @Override
    public Boolean casePerformActionUsage(PerformActionUsage object) {
        final Boolean result;
        if (this.shouldConsiderParameter(object)) {
            result = this.isInheritedParameter(object);
        } else if (this.shouldConsiderPerformUsage(object)) {
            result = this.isInheritedAction(object);
        } else {
            result = super.casePerformActionUsage(object);
        }
        return result;
    }

    /**
     * Filters inherited reference usages against the target compartment type.
     *
     * @param object
     *            the inherited reference usage
     * @return {@code true} when the reference usage matches the compartment
     */
    @Override
    public Boolean caseReferenceUsage(ReferenceUsage object) {
        if (this.shouldConsiderParameter(object)) {
            return this.isInheritedParameter(object);
        }

        EClassifier eType = this.eReference.getEType();
        EClass eClass = object.eClass();
        return eType.equals(eClass) || (eType instanceof EClass eTypeEClass && eTypeEClass.isSuperTypeOf(eClass));
    }

    /**
     * Indicates whether the feature should be evaluated as a behavior or step parameter.
     *
     * @param feature
     *            the feature to inspect
     * @return {@code true} when the target compartment is a parameter compartment for the feature owner
     */
    private boolean shouldConsiderParameter(Feature feature) {
        Type owningType = feature.getOwningType();
        return (owningType instanceof Behavior && this.eReference.equals(SysmlPackage.eINSTANCE.getBehavior_Parameter()))
                || (owningType instanceof Step && this.eReference.equals(SysmlPackage.eINSTANCE.getStep_Parameter()));
    }

    /**
     * Indicates whether the feature is inherited through a behavior or step parameter list.
     *
     * @param feature
     *            the feature to inspect
     * @return {@code true} when the feature is an inherited parameter
     */
    private boolean isInheritedParameter(Feature feature) {
        List<Feature> featureParameter = switch (feature.getOwner()) {
            case Behavior behavior -> behavior.getParameter();
            case Step step -> step.getParameter();
            default -> List.of();
        };
        return featureParameter.contains(feature);
    }

    /**
     * Indicates whether the feature should be evaluated as an exhibit-state compartment item.
     *
     * @param feature
     *            the feature to inspect
     * @return {@code true} when the feature matches an exhibit-state compartment
     */
    private boolean shouldConsiderExhibitState(Feature feature) {
        if (!(feature instanceof ExhibitStateUsage)) {
            return false;
        }

        EClassifier eType = this.eReference.getEType();
        EClass eClass = feature.eClass();
        return eType.equals(eClass) || (eType instanceof EClass eTypeEClass && eTypeEClass.isSuperTypeOf(eClass));
    }

    /**
     * Indicates whether the feature is inherited through the owner's state list.
     *
     * @param feature
     *            the feature to inspect
     * @return {@code true} when the feature is an inherited state
     */
    private boolean isInheritedState(Feature feature) {
        List<StateUsage> featureState = switch (feature.getOwner()) {
            case Definition definition -> definition.getOwnedState();
            case Usage usage -> usage.getNestedState();
            default -> List.of();
        };
        return featureState.contains(feature);
    }

    /**
     * Indicates whether the feature should be evaluated as a perform-action compartment item.
     *
     * @param feature
     *            the feature to inspect
     * @return {@code true} when the feature matches a perform-action compartment
     */
    private boolean shouldConsiderPerformUsage(Feature feature) {
        if (!(feature instanceof PerformActionUsage)) {
            return false;
        }

        EClassifier eType = this.eReference.getEType();
        EClass eClass = feature.eClass();
        return eType.equals(eClass) || (eType instanceof EClass eTypeEClass && eTypeEClass.isSuperTypeOf(eClass));
    }

    /**
     * Indicates whether the feature is inherited through the owner's action list.
     *
     * @param feature
     *            the feature to inspect
     * @return {@code true} when the feature is an inherited action
     */
    private boolean isInheritedAction(Feature feature) {
        List<ActionUsage> featureActions = switch (feature.getOwner()) {
            case Definition definition -> definition.getOwnedAction();
            case Usage usage -> usage.getNestedAction();
            default -> List.of();
        };
        return featureActions.contains(feature);
    }
}
