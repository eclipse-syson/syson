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
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.IfActionUsage;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>If Action Usage</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.IfActionUsageImpl#getElseAction <em>Else Action</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.IfActionUsageImpl#getIfArgument <em>If Argument</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.IfActionUsageImpl#getThenAction <em>Then Action</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IfActionUsageImpl extends ActionUsageImpl implements IfActionUsage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected IfActionUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getIfActionUsage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ActionUsage getElseAction() {
        ActionUsage elseAction = this.basicGetElseAction();
        return elseAction != null && elseAction.eIsProxy() ? (ActionUsage) this.eResolveProxy((InternalEObject) elseAction) : elseAction;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public ActionUsage basicGetElseAction() {
        // TODO: implement this method to return the 'Else Action' reference
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
    public Expression getIfArgument() {
        Expression ifArgument = this.basicGetIfArgument();
        return ifArgument != null && ifArgument.eIsProxy() ? (Expression) this.eResolveProxy((InternalEObject) ifArgument) : ifArgument;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Expression basicGetIfArgument() {
        // TODO: implement this method to return the 'If Argument' reference
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
    public ActionUsage getThenAction() {
        ActionUsage thenAction = this.basicGetThenAction();
        return thenAction != null && thenAction.eIsProxy() ? (ActionUsage) this.eResolveProxy((InternalEObject) thenAction) : thenAction;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public ActionUsage basicGetThenAction() {
        // TODO: implement this method to return the 'Then Action' reference
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
            case SysmlPackage.IF_ACTION_USAGE__ELSE_ACTION:
                if (resolve) {
                    return this.getElseAction();
                }
                return this.basicGetElseAction();
            case SysmlPackage.IF_ACTION_USAGE__IF_ARGUMENT:
                if (resolve) {
                    return this.getIfArgument();
                }
                return this.basicGetIfArgument();
            case SysmlPackage.IF_ACTION_USAGE__THEN_ACTION:
                if (resolve) {
                    return this.getThenAction();
                }
                return this.basicGetThenAction();
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
            case SysmlPackage.IF_ACTION_USAGE__ELSE_ACTION:
                return this.basicGetElseAction() != null;
            case SysmlPackage.IF_ACTION_USAGE__IF_ARGUMENT:
                return this.basicGetIfArgument() != null;
            case SysmlPackage.IF_ACTION_USAGE__THEN_ACTION:
                return this.basicGetThenAction() != null;
        }
        return super.eIsSet(featureID);
    }

} // IfActionUsageImpl
