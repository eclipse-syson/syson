/**
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
 */
package org.eclipse.syson.sysml.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.InstantiationExpression;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Instantiation Expression</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.InstantiationExpressionImpl#getArgument <em>Argument</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.InstantiationExpressionImpl#getInstantiatedType <em>Instantiated
 * Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class InstantiationExpressionImpl extends ExpressionImpl implements InstantiationExpression {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected InstantiationExpressionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getInstantiationExpression();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Expression> getArgument() {
        // The derivation of argument is given in the concrete subclasses of InstantiationExpression.
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Type getInstantiatedType() {
        Type instantiatedType = this.basicGetInstantiatedType();
        return instantiatedType != null && instantiatedType.eIsProxy() ? (Type) this.eResolveProxy((InternalEObject) instantiatedType) : instantiatedType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Type basicGetInstantiatedType() {
        return this.instantiatedType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Type instantiatedType() {
        List<Element> members = this.getOwnedMembership().stream().filter(m -> !(m instanceof FeatureMembership)).map(Membership::getMemberElement).toList();
        if (members.isEmpty() || !(members.get(0) instanceof Type)) {
            return null;
        }
        return (Type) members.get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.INSTANTIATION_EXPRESSION__ARGUMENT:
                return this.getArgument();
            case SysmlPackage.INSTANTIATION_EXPRESSION__INSTANTIATED_TYPE:
                if (resolve) {
                    return this.getInstantiatedType();
                }
                return this.basicGetInstantiatedType();
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
            case SysmlPackage.INSTANTIATION_EXPRESSION__ARGUMENT:
                return !this.getArgument().isEmpty();
            case SysmlPackage.INSTANTIATION_EXPRESSION__INSTANTIATED_TYPE:
                return this.basicGetInstantiatedType() != null;
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
            case SysmlPackage.INSTANTIATION_EXPRESSION___INSTANTIATED_TYPE:
                return this.instantiatedType();
        }
        return super.eInvoke(operationID, arguments);
    }

} // InstantiationExpressionImpl
