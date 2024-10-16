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

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.InvocationExpression;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Invocation Expression</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.InvocationExpressionImpl#getArgument <em>Argument</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.InvocationExpressionImpl#getOperand <em>Operand</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InvocationExpressionImpl extends ExpressionImpl implements InvocationExpression {
    /**
     * The cached value of the '{@link #getOperand() <em>Operand</em>}' reference list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getOperand()
     * @generated
     * @ordered
     */
    protected EList<Expression> operand;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected InvocationExpressionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getInvocationExpression();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Expression> getArgument() {
        List<Expression> arguments = this.getOwnedFeature().stream()
                .filter(f -> f.getDirection() == FeatureDirectionKind.IN)
                .map(Feature::getValuation)
                .filter(Objects::nonNull)
                .map(v -> v.getValue())
                .filter(Objects::nonNull)
                .toList();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getInvocationExpression_Argument(), arguments.size(), arguments.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<Expression> getOperand() {
        if (this.operand == null) {
            this.operand = new EObjectResolvingEList<>(Expression.class, this, SysmlPackage.INVOCATION_EXPRESSION__OPERAND);
        }
        return this.operand;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.INVOCATION_EXPRESSION__ARGUMENT:
                return this.getArgument();
            case SysmlPackage.INVOCATION_EXPRESSION__OPERAND:
                return this.getOperand();
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
            case SysmlPackage.INVOCATION_EXPRESSION__ARGUMENT:
                return !this.getArgument().isEmpty();
            case SysmlPackage.INVOCATION_EXPRESSION__OPERAND:
                return this.operand != null && !this.operand.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} // InvocationExpressionImpl
