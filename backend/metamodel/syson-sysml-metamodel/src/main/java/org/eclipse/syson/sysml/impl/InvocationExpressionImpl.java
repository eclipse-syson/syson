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
import java.util.Objects;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.InvocationExpression;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;

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
        List<Expression> arguments = new ArrayList<>();
        Type instantiatedType = this.instantiatedType();
        if (instantiatedType != null) {
            instantiatedType.getInput().stream()
                    .map(inp -> {
                        return this.getOwnedFeature().stream()
                                .filter(ow -> ow.redefines(inp))
                                .map(Feature::getValuation)
                                .filter(Objects::nonNull)
                                .map(FeatureValue::getValue)
                                .toList();
                    })
                    .flatMap(List::stream)
                    .forEach(arguments::add);;
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getInvocationExpression_Argument(), arguments.size(), arguments.toArray());
    }

    /**
     * @generated NOT
     */
    protected Type instantiatedType() {
        var members = this.getOwnedMembership().stream()
                .filter(m -> !(m instanceof FeatureMembership))
                .map(Membership::getMemberElement)
                .toList();
        if (!members.isEmpty() && members.get(0) instanceof Type t) {
            return t;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<Expression> getOperand() {
        // TODO: implement this method to return the 'Operand' containment reference list
        // Ensure that you remove @generated or mark it @generated NOT
        // The list is expected to implement org.eclipse.emf.ecore.util.InternalEList and
        // org.eclipse.emf.ecore.EStructuralFeature.Setting
        // so it's likely that an appropriate subclass of org.eclipse.emf.ecore.util.EcoreEList should be used.
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case SysmlPackage.INVOCATION_EXPRESSION__OPERAND:
                return ((InternalEList<?>) this.getOperand()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
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
                return !this.getOperand().isEmpty();
        }
        return super.eIsSet(featureID);
    }

} // InvocationExpressionImpl
