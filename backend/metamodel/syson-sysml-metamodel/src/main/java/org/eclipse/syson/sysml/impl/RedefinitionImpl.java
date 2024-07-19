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
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Redefinition</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.RedefinitionImpl#getRedefinedFeature <em>Redefined Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.RedefinitionImpl#getRedefiningFeature <em>Redefining Feature</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RedefinitionImpl extends SubsettingImpl implements Redefinition {
    /**
     * The cached value of the '{@link #getRedefinedFeature() <em>Redefined Feature</em>}' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getRedefinedFeature()
     * @generated
     * @ordered
     */
    protected Feature redefinedFeature;

    /**
     * The cached value of the '{@link #getRedefiningFeature() <em>Redefining Feature</em>}' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getRedefiningFeature()
     * @generated
     * @ordered
     */
    protected Feature redefiningFeature;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected RedefinitionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getRedefinition();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature getRedefinedFeature() {
        if (this.redefinedFeature != null && this.redefinedFeature.eIsProxy()) {
            InternalEObject oldRedefinedFeature = (InternalEObject) this.redefinedFeature;
            this.redefinedFeature = (Feature) this.eResolveProxy(oldRedefinedFeature);
            if (this.redefinedFeature != oldRedefinedFeature) {
                if (this.eNotificationRequired())
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.REDEFINITION__REDEFINED_FEATURE, oldRedefinedFeature, this.redefinedFeature));
            }
        }
        return this.redefinedFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Feature basicGetRedefinedFeature() {
        return this.redefinedFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setRedefinedFeature(Feature newRedefinedFeature) {
        Feature oldRedefinedFeature = this.redefinedFeature;
        this.redefinedFeature = newRedefinedFeature;
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.REDEFINITION__REDEFINED_FEATURE, oldRedefinedFeature, this.redefinedFeature));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature getRedefiningFeature() {
        if (this.redefiningFeature != null && this.redefiningFeature.eIsProxy()) {
            InternalEObject oldRedefiningFeature = (InternalEObject) this.redefiningFeature;
            this.redefiningFeature = (Feature) this.eResolveProxy(oldRedefiningFeature);
            if (this.redefiningFeature != oldRedefiningFeature) {
                if (this.eNotificationRequired())
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.REDEFINITION__REDEFINING_FEATURE, oldRedefiningFeature, this.redefiningFeature));
            }
        }
        return this.redefiningFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Feature basicGetRedefiningFeature() {
        return this.redefiningFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setRedefiningFeature(Feature newRedefiningFeature) {
        Feature oldRedefiningFeature = this.redefiningFeature;
        this.redefiningFeature = newRedefiningFeature;
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.REDEFINITION__REDEFINING_FEATURE, oldRedefiningFeature, this.redefiningFeature));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.REDEFINITION__REDEFINED_FEATURE:
                if (resolve)
                    return this.getRedefinedFeature();
                return this.basicGetRedefinedFeature();
            case SysmlPackage.REDEFINITION__REDEFINING_FEATURE:
                if (resolve)
                    return this.getRedefiningFeature();
                return this.basicGetRedefiningFeature();
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
            case SysmlPackage.REDEFINITION__REDEFINED_FEATURE:
                this.setRedefinedFeature((Feature) newValue);
                return;
            case SysmlPackage.REDEFINITION__REDEFINING_FEATURE:
                this.setRedefiningFeature((Feature) newValue);
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
            case SysmlPackage.REDEFINITION__REDEFINED_FEATURE:
                this.setRedefinedFeature((Feature) null);
                return;
            case SysmlPackage.REDEFINITION__REDEFINING_FEATURE:
                this.setRedefiningFeature((Feature) null);
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
            case SysmlPackage.REDEFINITION__REDEFINED_FEATURE:
                return this.redefinedFeature != null;
            case SysmlPackage.REDEFINITION__REDEFINING_FEATURE:
                return this.redefiningFeature != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Feature getSubsettedFeature() {
        return this.getRedefinedFeature();
    }

    /**
     * <!-- begin-user-doc --> Redefines setter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void setSubsettedFeature(Feature newSubsettedFeature) {
        this.setRedefinedFeature(newSubsettedFeature);
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Feature getSubsettingFeature() {
        return this.getRedefiningFeature();
    }

    /**
     * <!-- begin-user-doc --> Redefines setter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void setSubsettingFeature(Feature newSubsettingFeature) {
        this.setRedefiningFeature(newSubsettingFeature);
    }

} // RedefinitionImpl
