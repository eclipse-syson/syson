/*******************************************************************************
* Copyright (c) 2023, 2025 Obeo.
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
package org.eclipse.syson.sysml.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.RequirementVerificationMembership;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Requirement Verification Membership</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.RequirementVerificationMembershipImpl#getOwnedRequirement <em>Owned
 * Requirement</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.RequirementVerificationMembershipImpl#getVerifiedRequirement <em>Verified
 * Requirement</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RequirementVerificationMembershipImpl extends RequirementConstraintMembershipImpl implements RequirementVerificationMembership {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected RequirementVerificationMembershipImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getRequirementVerificationMembership();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public RequirementUsage getOwnedRequirement() {
        RequirementUsage ownedRequirement = this.basicGetOwnedRequirement();
        return ownedRequirement != null && ownedRequirement.eIsProxy() ? (RequirementUsage) this.eResolveProxy((InternalEObject) ownedRequirement) : ownedRequirement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public RequirementUsage basicGetOwnedRequirement() {
        // TODO: implement this method to return the 'Owned Requirement' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public RequirementUsage getVerifiedRequirement() {
        RequirementUsage verifiedRequirement = this.basicGetVerifiedRequirement();
        return verifiedRequirement != null && verifiedRequirement.eIsProxy() ? (RequirementUsage) this.eResolveProxy((InternalEObject) verifiedRequirement) : verifiedRequirement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public RequirementUsage basicGetVerifiedRequirement() {
        // TODO: implement this method to return the 'Verified Requirement' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.REQUIREMENT_VERIFICATION_MEMBERSHIP__OWNED_REQUIREMENT:
                if (resolve) {
                    return this.getOwnedRequirement();
                }
                return this.basicGetOwnedRequirement();
            case SysmlPackage.REQUIREMENT_VERIFICATION_MEMBERSHIP__VERIFIED_REQUIREMENT:
                if (resolve) {
                    return this.getVerifiedRequirement();
                }
                return this.basicGetVerifiedRequirement();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.REQUIREMENT_VERIFICATION_MEMBERSHIP__OWNED_REQUIREMENT:
                return this.basicGetOwnedRequirement() != null;
            case SysmlPackage.REQUIREMENT_VERIFICATION_MEMBERSHIP__VERIFIED_REQUIREMENT:
                return this.basicGetVerifiedRequirement() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public ConstraintUsage getOwnedConstraint() {
        return this.getOwnedRequirement();
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public ConstraintUsage getReferencedConstraint() {
        return this.getVerifiedRequirement();
    }

} // RequirementVerificationMembershipImpl
