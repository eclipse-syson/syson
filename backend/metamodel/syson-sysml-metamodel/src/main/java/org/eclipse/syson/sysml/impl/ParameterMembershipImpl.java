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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Parameter Membership</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.ParameterMembershipImpl#getOwnedMemberParameter <em>Owned Member
 * Parameter</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ParameterMembershipImpl extends FeatureMembershipImpl implements ParameterMembership {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected ParameterMembershipImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getParameterMembership();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature getOwnedMemberParameter() {
        Feature ownedMemberParameter = this.basicGetOwnedMemberParameter();
        return ownedMemberParameter != null && ownedMemberParameter.eIsProxy() ? (Feature) this.eResolveProxy((InternalEObject) ownedMemberParameter) : ownedMemberParameter;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Feature basicGetOwnedMemberParameter() {
        return this.getOwnedRelatedElement().stream()
                .filter(Feature.class::isInstance)
                .map(Feature.class::cast)
                .findFirst()
                .orElse(null);
    }

    /**
     * <!-- begin-user-doc -->Return the required value of the direction of the ownedMemberParameter. By default, this
     * is in. body: FeatureDirectionKind::_'in'<!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public FeatureDirectionKind parameterDirection() {
        var memberParameter = this.getOwnedMemberParameter();
        if (memberParameter != null) {
            return memberParameter.getDirection();
        }
        return FeatureDirectionKind.IN;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.PARAMETER_MEMBERSHIP__OWNED_MEMBER_PARAMETER:
                if (resolve) {
                    return this.getOwnedMemberParameter();
                }
                return this.basicGetOwnedMemberParameter();
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
            case SysmlPackage.PARAMETER_MEMBERSHIP__OWNED_MEMBER_PARAMETER:
                return this.basicGetOwnedMemberParameter() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
        switch (operationID) {
            case SysmlPackage.PARAMETER_MEMBERSHIP___PARAMETER_DIRECTION:
                return this.parameterDirection();
        }
        return super.eInvoke(operationID, arguments);
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Feature getOwnedMemberFeature() {
        return this.getOwnedMemberParameter();
    }

} // ParameterMembershipImpl
