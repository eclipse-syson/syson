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
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.StateSubactionKind;
import org.eclipse.syson.sysml.StateSubactionMembership;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>State Subaction Membership</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.StateSubactionMembershipImpl#getKind <em>Kind</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.StateSubactionMembershipImpl#getAction <em>Action</em>}</li>
 * </ul>
 *
 * @generated
 */
public class StateSubactionMembershipImpl extends FeatureMembershipImpl implements StateSubactionMembership {
    /**
     * The default value of the '{@link #getKind() <em>Kind</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @see #getKind()
     * @generated
     * @ordered
     */
    protected static final StateSubactionKind KIND_EDEFAULT = StateSubactionKind.DO;

    /**
     * The cached value of the '{@link #getKind() <em>Kind</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @see #getKind()
     * @generated
     * @ordered
     */
    protected StateSubactionKind kind = KIND_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected StateSubactionMembershipImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getStateSubactionMembership();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ActionUsage getAction() {
        ActionUsage action = this.basicGetAction();
        return action != null && action.eIsProxy() ? (ActionUsage) this.eResolveProxy((InternalEObject) action) : action;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public ActionUsage basicGetAction() {
        // TODO: implement this method to return the 'Action' reference
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
    public StateSubactionKind getKind() {
        return this.kind;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setKind(StateSubactionKind newKind) {
        StateSubactionKind oldKind = this.kind;
        this.kind = newKind == null ? KIND_EDEFAULT : newKind;
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.STATE_SUBACTION_MEMBERSHIP__KIND, oldKind, this.kind));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.STATE_SUBACTION_MEMBERSHIP__KIND:
                return this.getKind();
            case SysmlPackage.STATE_SUBACTION_MEMBERSHIP__ACTION:
                if (resolve)
                    return this.getAction();
                return this.basicGetAction();
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
            case SysmlPackage.STATE_SUBACTION_MEMBERSHIP__KIND:
                this.setKind((StateSubactionKind) newValue);
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
            case SysmlPackage.STATE_SUBACTION_MEMBERSHIP__KIND:
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
            case SysmlPackage.STATE_SUBACTION_MEMBERSHIP__KIND:
                return this.kind != KIND_EDEFAULT;
            case SysmlPackage.STATE_SUBACTION_MEMBERSHIP__ACTION:
                return this.basicGetAction() != null;
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

} // StateSubactionMembershipImpl
