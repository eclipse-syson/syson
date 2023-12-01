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
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.WhileLoopActionUsage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>While Loop Action Usage</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.WhileLoopActionUsageImpl#getUntilArgument <em>Until Argument</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.WhileLoopActionUsageImpl#getWhileArgument <em>While Argument</em>}</li>
 * </ul>
 *
 * @generated
 */
public class WhileLoopActionUsageImpl extends LoopActionUsageImpl implements WhileLoopActionUsage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected WhileLoopActionUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getWhileLoopActionUsage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getUntilArgument() {
        Expression untilArgument = basicGetUntilArgument();
        return untilArgument != null && untilArgument.eIsProxy() ? (Expression)eResolveProxy((InternalEObject)untilArgument) : untilArgument;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Expression basicGetUntilArgument() {
        // TODO: implement this method to return the 'Until Argument' reference
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
    public Expression getWhileArgument() {
        Expression whileArgument = basicGetWhileArgument();
        return whileArgument != null && whileArgument.eIsProxy() ? (Expression)eResolveProxy((InternalEObject)whileArgument) : whileArgument;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Expression basicGetWhileArgument() {
        // TODO: implement this method to return the 'While Argument' reference
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
            case SysmlPackage.WHILE_LOOP_ACTION_USAGE__UNTIL_ARGUMENT:
                if (resolve) return getUntilArgument();
                return basicGetUntilArgument();
            case SysmlPackage.WHILE_LOOP_ACTION_USAGE__WHILE_ARGUMENT:
                if (resolve) return getWhileArgument();
                return basicGetWhileArgument();
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
            case SysmlPackage.WHILE_LOOP_ACTION_USAGE__UNTIL_ARGUMENT:
                return basicGetUntilArgument() != null;
            case SysmlPackage.WHILE_LOOP_ACTION_USAGE__WHILE_ARGUMENT:
                return basicGetWhileArgument() != null;
        }
        return super.eIsSet(featureID);
    }

} //WhileLoopActionUsageImpl
