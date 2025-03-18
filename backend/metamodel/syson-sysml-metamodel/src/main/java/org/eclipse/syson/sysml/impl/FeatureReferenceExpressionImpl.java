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
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureReferenceExpression;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Feature Reference Expression</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureReferenceExpressionImpl#getReferent <em>Referent</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FeatureReferenceExpressionImpl extends ExpressionImpl implements FeatureReferenceExpression {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected FeatureReferenceExpressionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getFeatureReferenceExpression();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature getReferent() {
        Feature referent = this.basicGetReferent();
        return referent != null && referent.eIsProxy() ? (Feature) this.eResolveProxy((InternalEObject) referent) : referent;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Feature basicGetReferent() {
        return this.getOwnedMembership().stream()
                .filter(mem -> !(mem instanceof ParameterMembership))
                .map(Membership::getMemberElement)
                .filter(el -> el != null)
                .filter(Feature.class::isInstance)
                .map(Feature.class::cast)
                .findFirst()
                .orElse(null);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Feature getResult() {
        /**
         * <pre>
         * Shortcut to implement the description found in 8.3.4.8.4 FeatureReferenceExpression:
         *  "A FeatureReferenceExpression is an Expression whose result is bound to a referent Feature."
         *
         * The specification would normally requires that:
         *   1. This element contains a ResultExpressionMembership (since FeaturReferenceMembership inherits that constraint from Expression)
         *   2. Then a BindingConnector is added between the referent and the resource (see checkFeatureReferenceExpressionBindingConnector)
         *   3. Then that bind connector would in some way make the Feature owned by the ResultExpressionMembership redefine the referent feature
         *
         * At the moment we do not understand properly how to implement the link between the BindingConnector and the implicit computation of the redefinition.
         * </pre>
         */
        return this.getReferent();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.FEATURE_REFERENCE_EXPRESSION__REFERENT:
                if (resolve) {
                    return this.getReferent();
                }
                return this.basicGetReferent();
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
            case SysmlPackage.FEATURE_REFERENCE_EXPRESSION__REFERENT:
                return this.basicGetReferent() != null;
        }
        return super.eIsSet(featureID);
    }

} // FeatureReferenceExpressionImpl
