/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
package org.eclipse.syson.diagram.common.view.services;

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
 * Switch allowing to filter some candidates to be considered as inherited node items or not.
 *
 * @author arichard
 */
public class InheritedCompartmentItemFilterSwitch extends SysmlSwitch<Boolean> {

    private final EReference eReference;

    public InheritedCompartmentItemFilterSwitch(EReference eReference) {
        this.eReference = Objects.requireNonNull(eReference);
    }

    @Override
    public Boolean defaultCase(EObject object) {
        return false;
    }

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

    @Override
    public Boolean caseExhibitStateUsage(ExhibitStateUsage object) {
        Boolean shouldKeep = Boolean.FALSE;
        // Add this behavior parameter check for each caseXXXUsage.
        // In this case, we want to display inherited parameters (directed feature) but not all features with the same
        // type.
        if (this.shouldConsiderParameter(object)) {
            shouldKeep = this.isInheritedParameter(object);
        } else if (this.shouldConsiderExhibitState(object)) {
            shouldKeep = this.isInheritedState(object);
        } else {
            shouldKeep = super.caseExhibitStateUsage(object);
        }
        return shouldKeep;
    }

    @Override
    public Boolean caseFeature(Feature object) {
        // Add this behavior parameter check for each caseXXXUsage.
        // In this case, we want to display inherited parameters (directed feature) but not all features with the same
        // type.
        if (this.shouldConsiderParameter(object)) {
            return this.isInheritedParameter(object);
        }
        return this.eReference.getEType().equals(object.eClass());
    }

    @Override
    public Boolean casePartUsage(PartUsage object) {
        // Add this behavior parameter check for each caseXXXUsage.
        // In this case, we want to display inherited parameters (directed feature) but not all features with the same
        // type.
        if (this.shouldConsiderParameter(object)) {
            return this.isInheritedParameter(object);
        }
        EClassifier eType = this.eReference.getEType();
        EClass eClass = object.eClass();
        return eType.equals(eClass) || (eType instanceof EClass eTypeEClass && eTypeEClass.isSuperTypeOf(eClass));
    }

    @Override
    public Boolean casePerformActionUsage(PerformActionUsage object) {
        Boolean shouldKeep = Boolean.FALSE;
        // Add this behavior parameter check for each caseXXXUsage.
        // In this case, we want to display inherited parameters (directed feature) but not all features with the same
        // type.
        if (this.shouldConsiderParameter(object)) {
            shouldKeep = this.isInheritedParameter(object);
        } else if (this.shouldConsiderPerformUsage(object)) {
            shouldKeep = this.isInheritedAction(object);
        } else {
            shouldKeep = super.casePerformActionUsage(object);
        }
        return shouldKeep;
    }

    @Override
    public Boolean caseReferenceUsage(ReferenceUsage object) {
        // Add this behavior parameter check for each caseXXXUsage.
        // In this case, we want to display inherited parameters (directed feature) but not all features with the same
        // type.
        if (this.shouldConsiderParameter(object)) {
            return this.isInheritedParameter(object);
        }

        EClassifier eType = this.eReference.getEType();
        EClass eClass = object.eClass();
        return eType.equals(eClass) || (eType instanceof EClass eTypeEClass && eTypeEClass.isSuperTypeOf(eClass));
    }

    private boolean shouldConsiderParameter(Feature feature) {
        Type owningType = feature.getOwningType();
        return (owningType instanceof Behavior && this.eReference.equals(SysmlPackage.eINSTANCE.getBehavior_Parameter()))
                || (owningType instanceof Step && this.eReference.equals(SysmlPackage.eINSTANCE.getStep_Parameter()));
    }

    private boolean isInheritedParameter(Feature feature) {
        List<Feature> featureParameter = switch (feature.getOwner()) {
            case Behavior behavior -> behavior.getParameter();
            case Step step -> step.getParameter();
            default -> List.of();
        };
        return featureParameter.contains(feature);
    }

    private boolean shouldConsiderExhibitState(Feature feature) {
        if (!(feature instanceof ExhibitStateUsage)) {
            return false;
        }

        EClassifier eType = this.eReference.getEType();
        EClass eClass = feature.eClass();
        return eType.equals(eClass) || (eType instanceof EClass eTypeEClass && eTypeEClass.isSuperTypeOf(eClass));
    }

    private boolean isInheritedState(Feature feature) {
        List<StateUsage> featureState = switch (feature.getOwner()) {
            case Definition definition -> definition.getOwnedState();
            case Usage usage -> usage.getNestedState();
            default -> List.of();
        };
        return featureState.contains(feature);
    }

    private boolean shouldConsiderPerformUsage(Feature feature) {
        if (!(feature instanceof PerformActionUsage)) {
            return false;
        }

        EClassifier eType = this.eReference.getEType();
        EClass eClass = feature.eClass();
        return eType.equals(eClass) || (eType instanceof EClass eTypeEClass && eTypeEClass.isSuperTypeOf(eClass));
    }

    private boolean isInheritedAction(Feature feature) {
        List< ActionUsage> featureActions = switch (feature.getOwner()) {
            case Definition definition -> definition.getOwnedAction();
            case Usage usage -> usage.getNestedAction();
            default -> List.of();
        };
        return featureActions.contains(feature);
    }
}
