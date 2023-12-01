/**
 * Copyright (c) 2023 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.syson.sysml.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.SubjectMembership;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Subject Membership</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.SubjectMembershipImpl#getOwnedSubjectParameter <em>Owned Subject Parameter</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SubjectMembershipImpl extends ParameterMembershipImpl implements SubjectMembership {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SubjectMembershipImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getSubjectMembership();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Usage getOwnedSubjectParameter() {
        Usage ownedSubjectParameter = basicGetOwnedSubjectParameter();
        return ownedSubjectParameter != null && ownedSubjectParameter.eIsProxy() ? (Usage)eResolveProxy((InternalEObject)ownedSubjectParameter) : ownedSubjectParameter;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Usage basicGetOwnedSubjectParameter() {
        // TODO: implement this method to return the 'Owned Subject Parameter' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.SUBJECT_MEMBERSHIP__OWNED_SUBJECT_PARAMETER:
                if (resolve) return getOwnedSubjectParameter();
                return basicGetOwnedSubjectParameter();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.SUBJECT_MEMBERSHIP__OWNED_SUBJECT_PARAMETER:
                return basicGetOwnedSubjectParameter() != null;
        }
        return super.eIsSet(featureID);
    }

} //SubjectMembershipImpl
