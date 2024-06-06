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
import org.eclipse.syson.sysml.ConcernUsage;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.FramedConcernMembership;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Framed Concern Membership</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.FramedConcernMembershipImpl#getOwnedConcern <em>Owned Concern</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FramedConcernMembershipImpl#getReferencedConcern <em>Referenced
 * Concern</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FramedConcernMembershipImpl extends RequirementConstraintMembershipImpl implements FramedConcernMembership {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected FramedConcernMembershipImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getFramedConcernMembership();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ConcernUsage getOwnedConcern() {
        ConcernUsage ownedConcern = this.basicGetOwnedConcern();
        return ownedConcern != null && ownedConcern.eIsProxy() ? (ConcernUsage) this.eResolveProxy((InternalEObject) ownedConcern) : ownedConcern;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public ConcernUsage basicGetOwnedConcern() {
        // TODO: implement this method to return the 'Owned Concern' reference
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
    public ConcernUsage getReferencedConcern() {
        ConcernUsage referencedConcern = this.basicGetReferencedConcern();
        return referencedConcern != null && referencedConcern.eIsProxy() ? (ConcernUsage) this.eResolveProxy((InternalEObject) referencedConcern) : referencedConcern;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public ConcernUsage basicGetReferencedConcern() {
        // TODO: implement this method to return the 'Referenced Concern' reference
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
            case SysmlPackage.FRAMED_CONCERN_MEMBERSHIP__OWNED_CONCERN:
                if (resolve) {
                    return this.getOwnedConcern();
                }
                return this.basicGetOwnedConcern();
            case SysmlPackage.FRAMED_CONCERN_MEMBERSHIP__REFERENCED_CONCERN:
                if (resolve) {
                    return this.getReferencedConcern();
                }
                return this.basicGetReferencedConcern();
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
            case SysmlPackage.FRAMED_CONCERN_MEMBERSHIP__OWNED_CONCERN:
                return this.basicGetOwnedConcern() != null;
            case SysmlPackage.FRAMED_CONCERN_MEMBERSHIP__REFERENCED_CONCERN:
                return this.basicGetReferencedConcern() != null;
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
        return this.getOwnedConcern();
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public ConstraintUsage getReferencedConstraint() {
        return this.getReferencedConcern();
    }

} // FramedConcernMembershipImpl
