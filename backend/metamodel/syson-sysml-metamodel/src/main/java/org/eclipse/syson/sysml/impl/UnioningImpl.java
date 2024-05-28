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
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Unioning;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Unioning</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.UnioningImpl#getTypeUnioned <em>Type Unioned</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.UnioningImpl#getUnioningType <em>Unioning Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class UnioningImpl extends RelationshipImpl implements Unioning {
    /**
     * The cached value of the '{@link #getUnioningType() <em>Unioning Type</em>}' reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getUnioningType()
     * @generated
     * @ordered
     */
    protected Type unioningType;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected UnioningImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getUnioning();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Type getTypeUnioned() {
        Type typeUnioned = this.basicGetTypeUnioned();
        return typeUnioned != null && typeUnioned.eIsProxy() ? (Type) this.eResolveProxy((InternalEObject) typeUnioned) : typeUnioned;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Type basicGetTypeUnioned() {
        // TODO: implement this method to return the 'Type Unioned' reference
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
    public Type getUnioningType() {
        if (this.unioningType != null && this.unioningType.eIsProxy()) {
            InternalEObject oldUnioningType = (InternalEObject) this.unioningType;
            this.unioningType = (Type) this.eResolveProxy(oldUnioningType);
            if (this.unioningType != oldUnioningType) {
                if (this.eNotificationRequired())
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.UNIONING__UNIONING_TYPE, oldUnioningType, this.unioningType));
            }
        }
        return this.unioningType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Type basicGetUnioningType() {
        return this.unioningType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setUnioningType(Type newUnioningType) {
        Type oldUnioningType = this.unioningType;
        this.unioningType = newUnioningType;
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.UNIONING__UNIONING_TYPE, oldUnioningType, this.unioningType));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.UNIONING__TYPE_UNIONED:
                if (resolve)
                    return this.getTypeUnioned();
                return this.basicGetTypeUnioned();
            case SysmlPackage.UNIONING__UNIONING_TYPE:
                if (resolve)
                    return this.getUnioningType();
                return this.basicGetUnioningType();
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
            case SysmlPackage.UNIONING__UNIONING_TYPE:
                this.setUnioningType((Type) newValue);
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
            case SysmlPackage.UNIONING__UNIONING_TYPE:
                this.setUnioningType((Type) null);
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
            case SysmlPackage.UNIONING__TYPE_UNIONED:
                return this.basicGetTypeUnioned() != null;
            case SysmlPackage.UNIONING__UNIONING_TYPE:
                return this.unioningType != null;
        }
        return super.eIsSet(featureID);
    }

} // UnioningImpl
