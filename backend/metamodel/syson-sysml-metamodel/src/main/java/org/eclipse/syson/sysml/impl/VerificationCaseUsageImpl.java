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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.VerificationCaseDefinition;
import org.eclipse.syson.sysml.VerificationCaseUsage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Verification Case Usage</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.VerificationCaseUsageImpl#getVerificationCaseDefinition <em>Verification Case
 * Definition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.VerificationCaseUsageImpl#getVerifiedRequirement <em>Verified
 * Requirement</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VerificationCaseUsageImpl extends CaseUsageImpl implements VerificationCaseUsage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected VerificationCaseUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getVerificationCaseUsage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public VerificationCaseDefinition getVerificationCaseDefinition() {
        VerificationCaseDefinition verificationCaseDefinition = this.basicGetVerificationCaseDefinition();
        return verificationCaseDefinition != null && verificationCaseDefinition.eIsProxy() ? (VerificationCaseDefinition) this.eResolveProxy((InternalEObject) verificationCaseDefinition)
                : verificationCaseDefinition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public VerificationCaseDefinition basicGetVerificationCaseDefinition() {
        // TODO: implement this method to return the 'Verification Case Definition' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<RequirementUsage> getVerifiedRequirement() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getVerificationCaseUsage_VerifiedRequirement(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.VERIFICATION_CASE_USAGE__VERIFICATION_CASE_DEFINITION:
                if (resolve) {
                    return this.getVerificationCaseDefinition();
                }
                return this.basicGetVerificationCaseDefinition();
            case SysmlPackage.VERIFICATION_CASE_USAGE__VERIFIED_REQUIREMENT:
                return this.getVerifiedRequirement();
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
            case SysmlPackage.VERIFICATION_CASE_USAGE__VERIFICATION_CASE_DEFINITION:
                return this.basicGetVerificationCaseDefinition() != null;
            case SysmlPackage.VERIFICATION_CASE_USAGE__VERIFIED_REQUIREMENT:
                return !this.getVerifiedRequirement().isEmpty();
        }
        return super.eIsSet(featureID);
    }

} // VerificationCaseUsageImpl
