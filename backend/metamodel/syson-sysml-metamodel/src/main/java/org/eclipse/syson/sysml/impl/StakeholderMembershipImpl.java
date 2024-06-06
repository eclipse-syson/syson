/*******************************************************************************
* Copyright (c) 2023, 2024 Obeo.
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
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.StakeholderMembership;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Stakeholder Membership</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.StakeholderMembershipImpl#getOwnedStakeholderParameter <em>Owned Stakeholder
 * Parameter</em>}</li>
 * </ul>
 *
 * @generated
 */
public class StakeholderMembershipImpl extends ParameterMembershipImpl implements StakeholderMembership {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected StakeholderMembershipImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getStakeholderMembership();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public PartUsage getOwnedStakeholderParameter() {
        PartUsage ownedStakeholderParameter = this.basicGetOwnedStakeholderParameter();
        return ownedStakeholderParameter != null && ownedStakeholderParameter.eIsProxy() ? (PartUsage) this.eResolveProxy((InternalEObject) ownedStakeholderParameter) : ownedStakeholderParameter;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public PartUsage basicGetOwnedStakeholderParameter() {
        // TODO: implement this method to return the 'Owned Stakeholder Parameter' reference
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
            case SysmlPackage.STAKEHOLDER_MEMBERSHIP__OWNED_STAKEHOLDER_PARAMETER:
                if (resolve) {
                    return this.getOwnedStakeholderParameter();
                }
                return this.basicGetOwnedStakeholderParameter();
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
            case SysmlPackage.STAKEHOLDER_MEMBERSHIP__OWNED_STAKEHOLDER_PARAMETER:
                return this.basicGetOwnedStakeholderParameter() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Feature getOwnedMemberParameter() {
        return this.getOwnedStakeholderParameter();
    }

} // StakeholderMembershipImpl
