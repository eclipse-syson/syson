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

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Redefinition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.RedefinitionImpl#getRedefinedFeature <em>Redefined Feature</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.RedefinitionImpl#getRedefiningFeature <em>Redefining Feature</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RedefinitionImpl extends SubsettingImpl implements Redefinition {
    /**
     * The cached value of the '{@link #getRedefinedFeature() <em>Redefined Feature</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRedefinedFeature()
     * @generated
     * @ordered
     */
    protected Feature redefinedFeature;

    /**
     * The cached value of the '{@link #getRedefiningFeature() <em>Redefining Feature</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRedefiningFeature()
     * @generated
     * @ordered
     */
    protected Feature redefiningFeature;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RedefinitionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getRedefinition();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Feature getRedefinedFeature() {
        if (redefinedFeature != null && redefinedFeature.eIsProxy()) {
            InternalEObject oldRedefinedFeature = (InternalEObject)redefinedFeature;
            redefinedFeature = (Feature)eResolveProxy(oldRedefinedFeature);
            if (redefinedFeature != oldRedefinedFeature) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.REDEFINITION__REDEFINED_FEATURE, oldRedefinedFeature, redefinedFeature));
            }
        }
        return redefinedFeature;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Feature basicGetRedefinedFeature() {
        return redefinedFeature;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setRedefinedFeature(Feature newRedefinedFeature) {
        Feature oldRedefinedFeature = redefinedFeature;
        redefinedFeature = newRedefinedFeature;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.REDEFINITION__REDEFINED_FEATURE, oldRedefinedFeature, redefinedFeature));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Feature getRedefiningFeature() {
        if (redefiningFeature != null && redefiningFeature.eIsProxy()) {
            InternalEObject oldRedefiningFeature = (InternalEObject)redefiningFeature;
            redefiningFeature = (Feature)eResolveProxy(oldRedefiningFeature);
            if (redefiningFeature != oldRedefiningFeature) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.REDEFINITION__REDEFINING_FEATURE, oldRedefiningFeature, redefiningFeature));
            }
        }
        return redefiningFeature;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Feature basicGetRedefiningFeature() {
        return redefiningFeature;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setRedefiningFeature(Feature newRedefiningFeature) {
        Feature oldRedefiningFeature = redefiningFeature;
        redefiningFeature = newRedefiningFeature;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.REDEFINITION__REDEFINING_FEATURE, oldRedefiningFeature, redefiningFeature));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.REDEFINITION__REDEFINED_FEATURE:
                if (resolve) return getRedefinedFeature();
                return basicGetRedefinedFeature();
            case SysmlPackage.REDEFINITION__REDEFINING_FEATURE:
                if (resolve) return getRedefiningFeature();
                return basicGetRedefiningFeature();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case SysmlPackage.REDEFINITION__REDEFINED_FEATURE:
                setRedefinedFeature((Feature)newValue);
                return;
            case SysmlPackage.REDEFINITION__REDEFINING_FEATURE:
                setRedefiningFeature((Feature)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case SysmlPackage.REDEFINITION__REDEFINED_FEATURE:
                setRedefinedFeature((Feature)null);
                return;
            case SysmlPackage.REDEFINITION__REDEFINING_FEATURE:
                setRedefiningFeature((Feature)null);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.REDEFINITION__REDEFINED_FEATURE:
                return redefinedFeature != null;
            case SysmlPackage.REDEFINITION__REDEFINING_FEATURE:
                return redefiningFeature != null;
        }
        return super.eIsSet(featureID);
    }

} //RedefinitionImpl
