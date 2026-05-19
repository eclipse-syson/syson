/*******************************************************************************
* Copyright (c) 2023, 2026 Obeo.
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
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.ResultExpressionMembership;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Result Expression Membership</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.ResultExpressionMembershipImpl#getOwnedResultExpression <em>Owned Result
 * Expression</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ResultExpressionMembershipImpl extends FeatureMembershipImpl implements ResultExpressionMembership {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected ResultExpressionMembershipImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getResultExpressionMembership();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Expression getOwnedResultExpression() {
        Expression ownedResultExpression = this.basicGetOwnedResultExpression();
        return ownedResultExpression != null && ownedResultExpression.eIsProxy() ? (Expression) this.eResolveProxy((InternalEObject) ownedResultExpression) : ownedResultExpression;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Expression basicGetOwnedResultExpression() {
        return this.getOwnedRelatedElement().stream()
                .filter(Expression.class::isInstance)
                .map(Expression.class::cast)
                .findFirst()
                .orElse(null);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.RESULT_EXPRESSION_MEMBERSHIP__OWNED_RESULT_EXPRESSION:
                if (resolve) {
                    return this.getOwnedResultExpression();
                }
                return this.basicGetOwnedResultExpression();
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
            case SysmlPackage.RESULT_EXPRESSION_MEMBERSHIP__OWNED_RESULT_EXPRESSION:
                return this.basicGetOwnedResultExpression() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Feature getOwnedMemberFeature() {
        return this.getOwnedResultExpression();
    }

} // ResultExpressionMembershipImpl
