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
import org.eclipse.syson.sysml.FeatureChaining;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Feature Chaining</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureChainingImpl#getChainingFeature <em>Chaining Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureChainingImpl#getFeatureChained <em>Feature Chained</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FeatureChainingImpl extends RelationshipImpl implements FeatureChaining {
    /**
     * The cached value of the '{@link #getChainingFeature() <em>Chaining Feature</em>}' reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @see #getChainingFeature()
     * @generated
     * @ordered
     */
    protected Feature chainingFeature;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected FeatureChainingImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getFeatureChaining();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature getChainingFeature() {
        if (this.chainingFeature != null && this.chainingFeature.eIsProxy()) {
            InternalEObject oldChainingFeature = (InternalEObject) this.chainingFeature;
            this.chainingFeature = (Feature) this.eResolveProxy(oldChainingFeature);
            if (this.chainingFeature != oldChainingFeature) {
                if (this.eNotificationRequired()) {
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.FEATURE_CHAINING__CHAINING_FEATURE, oldChainingFeature, this.chainingFeature));
                }
            }
        }
        return this.chainingFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Feature basicGetChainingFeature() {
        return this.chainingFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setChainingFeature(Feature newChainingFeature) {
        Feature oldChainingFeature = this.chainingFeature;
        this.chainingFeature = newChainingFeature;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE_CHAINING__CHAINING_FEATURE, oldChainingFeature, this.chainingFeature));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature getFeatureChained() {
        Feature featureChained = this.basicGetFeatureChained();
        return featureChained != null && featureChained.eIsProxy() ? (Feature) this.eResolveProxy((InternalEObject) featureChained) : featureChained;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Feature basicGetFeatureChained() {
        if (this.getOwningRelatedElement() instanceof Feature feature) {
            return feature;
        }
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
            case SysmlPackage.FEATURE_CHAINING__CHAINING_FEATURE:
                if (resolve) {
                    return this.getChainingFeature();
                }
                return this.basicGetChainingFeature();
            case SysmlPackage.FEATURE_CHAINING__FEATURE_CHAINED:
                if (resolve) {
                    return this.getFeatureChained();
                }
                return this.basicGetFeatureChained();
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
            case SysmlPackage.FEATURE_CHAINING__CHAINING_FEATURE:
                this.setChainingFeature((Feature) newValue);
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
            case SysmlPackage.FEATURE_CHAINING__CHAINING_FEATURE:
                this.setChainingFeature((Feature) null);
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
            case SysmlPackage.FEATURE_CHAINING__CHAINING_FEATURE:
                return this.chainingFeature != null;
            case SysmlPackage.FEATURE_CHAINING__FEATURE_CHAINED:
                return this.basicGetFeatureChained() != null;
        }
        return super.eIsSet(featureID);
    }

} // FeatureChainingImpl
