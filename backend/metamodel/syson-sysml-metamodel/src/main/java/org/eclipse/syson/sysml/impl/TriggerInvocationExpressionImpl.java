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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TriggerInvocationExpression;
import org.eclipse.syson.sysml.TriggerKind;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Trigger Invocation Expression</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.TriggerInvocationExpressionImpl#getKind <em>Kind</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TriggerInvocationExpressionImpl extends InvocationExpressionImpl implements TriggerInvocationExpression {
    /**
     * The default value of the '{@link #getKind() <em>Kind</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @see #getKind()
     * @generated
     * @ordered
     */
    protected static final TriggerKind KIND_EDEFAULT = TriggerKind.AFTER;

    /**
     * The cached value of the '{@link #getKind() <em>Kind</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @see #getKind()
     * @generated
     * @ordered
     */
    protected TriggerKind kind = KIND_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected TriggerInvocationExpressionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getTriggerInvocationExpression();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public TriggerKind getKind() {
        return this.kind;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setKind(TriggerKind newKind) {
        TriggerKind oldKind = this.kind;
        this.kind = newKind == null ? KIND_EDEFAULT : newKind;
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.TRIGGER_INVOCATION_EXPRESSION__KIND, oldKind, this.kind));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.TRIGGER_INVOCATION_EXPRESSION__KIND:
                return this.getKind();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case SysmlPackage.TRIGGER_INVOCATION_EXPRESSION__KIND:
                this.setKind((TriggerKind) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case SysmlPackage.TRIGGER_INVOCATION_EXPRESSION__KIND:
                this.setKind(KIND_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.TRIGGER_INVOCATION_EXPRESSION__KIND:
                return this.kind != KIND_EDEFAULT;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String toString() {
        if (this.eIsProxy())
            return super.toString();

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (kind: ");
        result.append(this.kind);
        result.append(')');
        return result.toString();
    }

} // TriggerInvocationExpressionImpl
