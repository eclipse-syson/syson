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
package org.eclipse.syson.diagram.common.view.services;

import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.RequirementConstraintKind;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.SysmlPackage;
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
    public Boolean caseFeature(Feature object) {
        return this.eReference.getEType().equals(object.eClass());
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
    public Boolean casePartUsage(PartUsage object) {
        EClassifier eType = this.eReference.getEType();
        EClass eClass = object.eClass();
        return eType.equals(eClass) || (eType instanceof EClass eTypeEClass && eTypeEClass.isSuperTypeOf(eClass));
    }

    @Override
    public Boolean caseReferenceUsage(ReferenceUsage object) {
        EClassifier eType = this.eReference.getEType();
        EClass eClass = object.eClass();
        return eType.equals(eClass) || (eType instanceof EClass eTypeEClass && eTypeEClass.isSuperTypeOf(eClass));
    }
}
