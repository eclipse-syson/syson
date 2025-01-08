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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Featuring;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Featuring</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.FeaturingImpl#getFeature <em>Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeaturingImpl#getType <em>Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class FeaturingImpl extends RelationshipImpl implements Featuring {
    /**
     * The cached value of the '{@link #getFeature() <em>Feature</em>}' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getFeature()
     * @generated
     * @ordered
     */
    protected Feature feature;

    /**
     * The cached value of the '{@link #getType() <em>Type</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @see #getType()
     * @generated
     * @ordered
     */
    protected Type type;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected FeaturingImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getFeaturing();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature getFeature() {
        if (this.feature != null && this.feature.eIsProxy()) {
            InternalEObject oldFeature = (InternalEObject) this.feature;
            this.feature = (Feature) this.eResolveProxy(oldFeature);
            if (this.feature != oldFeature) {
                if (this.eNotificationRequired()) {
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.FEATURING__FEATURE, oldFeature, this.feature));
                }
            }
        }
        return this.feature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Feature basicGetFeature() {
        return this.feature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setFeature(Feature newFeature) {
        Feature oldFeature = this.feature;
        this.feature = newFeature;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURING__FEATURE, oldFeature, this.feature));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Type getType() {
        if (this.type != null && this.type.eIsProxy()) {
            InternalEObject oldType = (InternalEObject) this.type;
            this.type = (Type) this.eResolveProxy(oldType);
            if (this.type != oldType) {
                if (this.eNotificationRequired()) {
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.FEATURING__TYPE, oldType, this.type));
                }
            }
        }
        return this.type;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Type basicGetType() {
        return this.type;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setType(Type newType) {
        Type oldType = this.type;
        this.type = newType;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURING__TYPE, oldType, this.type));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.FEATURING__FEATURE:
                if (resolve) {
                    return this.getFeature();
                }
                return this.basicGetFeature();
            case SysmlPackage.FEATURING__TYPE:
                if (resolve) {
                    return this.getType();
                }
                return this.basicGetType();
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
            case SysmlPackage.FEATURING__FEATURE:
                this.setFeature((Feature) newValue);
                return;
            case SysmlPackage.FEATURING__TYPE:
                this.setType((Type) newValue);
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
            case SysmlPackage.FEATURING__FEATURE:
                this.setFeature((Feature) null);
                return;
            case SysmlPackage.FEATURING__TYPE:
                this.setType((Type) null);
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
            case SysmlPackage.FEATURING__FEATURE:
                return this.feature != null;
            case SysmlPackage.FEATURING__TYPE:
                return this.type != null;
        }
        return super.eIsSet(featureID);
    }

} // FeaturingImpl
