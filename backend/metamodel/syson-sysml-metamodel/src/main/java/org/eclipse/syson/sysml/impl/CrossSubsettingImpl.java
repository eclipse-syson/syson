/**
 * Copyright (c) 2025 Obeo.
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
import org.eclipse.syson.sysml.CrossSubsetting;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Cross Subsetting</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.CrossSubsettingImpl#getCrossedFeature <em>Crossed Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.CrossSubsettingImpl#getCrossingFeature <em>Crossing Feature</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CrossSubsettingImpl extends SubsettingImpl implements CrossSubsetting {
    /**
     * The cached value of the '{@link #getCrossedFeature() <em>Crossed Feature</em>}' reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @see #getCrossedFeature()
     * @generated
     * @ordered
     */
    protected Feature crossedFeature;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected CrossSubsettingImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getCrossSubsetting();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature getCrossedFeature() {
        if (this.crossedFeature != null && this.crossedFeature.eIsProxy()) {
            InternalEObject oldCrossedFeature = (InternalEObject) this.crossedFeature;
            this.crossedFeature = (Feature) this.eResolveProxy(oldCrossedFeature);
            if (this.crossedFeature != oldCrossedFeature) {
                if (this.eNotificationRequired()) {
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.CROSS_SUBSETTING__CROSSED_FEATURE, oldCrossedFeature, this.crossedFeature));
                }
            }
        }
        return this.crossedFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Feature basicGetCrossedFeature() {
        return this.crossedFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setCrossedFeature(Feature newCrossedFeature) {
        Feature oldCrossedFeature = this.crossedFeature;
        this.crossedFeature = newCrossedFeature;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.CROSS_SUBSETTING__CROSSED_FEATURE, oldCrossedFeature, this.crossedFeature));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature getCrossingFeature() {
        Feature crossingFeature = this.basicGetCrossingFeature();
        return crossingFeature != null && crossingFeature.eIsProxy() ? (Feature) this.eResolveProxy((InternalEObject) crossingFeature) : crossingFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Feature basicGetCrossingFeature() {
        Element owningRel = this.getOwningRelatedElement();
        if (owningRel instanceof Feature feature) {
            return feature;
        }
        return null;
    }

    /**
     * generated NOT
     */
    @Override
    public Feature getSubsettedFeature() {
        return this.getCrossedFeature();
    }

    /**
     * generated NOT
     */
    @Override
    public Feature getOwningFeature() {
        return this.getCrossingFeature();
    }

    /**
     * generated NOT
     */
    @Override
    public Feature getSubsettingFeature() {
        return this.getCrossingFeature();
    }

    /**
     * generated NOT
     */
    @Override
    public void setSubsettingFeature(Feature newSubsettingFeature) {
        // Nothing to do here as this reference is redefined by the crossingFeature derived reference
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.CROSS_SUBSETTING__CROSSED_FEATURE:
                if (resolve) {
                    return this.getCrossedFeature();
                }
                return this.basicGetCrossedFeature();
            case SysmlPackage.CROSS_SUBSETTING__CROSSING_FEATURE:
                if (resolve) {
                    return this.getCrossingFeature();
                }
                return this.basicGetCrossingFeature();
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
            case SysmlPackage.CROSS_SUBSETTING__CROSSED_FEATURE:
                this.setCrossedFeature((Feature) newValue);
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
            case SysmlPackage.CROSS_SUBSETTING__CROSSED_FEATURE:
                this.setCrossedFeature((Feature) null);
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
            case SysmlPackage.CROSS_SUBSETTING__CROSSED_FEATURE:
                return this.crossedFeature != null;
            case SysmlPackage.CROSS_SUBSETTING__CROSSING_FEATURE:
                return this.basicGetCrossingFeature() != null;
        }
        return super.eIsSet(featureID);
    }

} // CrossSubsettingImpl
