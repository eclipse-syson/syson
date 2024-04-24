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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.RequirementConstraintKind;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.util.SysmlSwitch;

/**
 * Test.
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
}
