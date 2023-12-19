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
import org.eclipse.syson.sysml.FeatureInverting;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Feature Inverting</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureInvertingImpl#getFeatureInverted <em>Feature Inverted</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureInvertingImpl#getInvertingFeature <em>Inverting Feature</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureInvertingImpl#getOwningFeature <em>Owning Feature</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FeatureInvertingImpl extends RelationshipImpl implements FeatureInverting {
    /**
     * The cached value of the '{@link #getFeatureInverted() <em>Feature Inverted</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFeatureInverted()
     * @generated
     * @ordered
     */
    protected Feature featureInverted;

    /**
     * The cached value of the '{@link #getInvertingFeature() <em>Inverting Feature</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInvertingFeature()
     * @generated
     * @ordered
     */
    protected Feature invertingFeature;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected FeatureInvertingImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getFeatureInverting();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Feature getFeatureInverted() {
        if (featureInverted != null && featureInverted.eIsProxy()) {
            InternalEObject oldFeatureInverted = (InternalEObject)featureInverted;
            featureInverted = (Feature)eResolveProxy(oldFeatureInverted);
            if (featureInverted != oldFeatureInverted) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.FEATURE_INVERTING__FEATURE_INVERTED, oldFeatureInverted, featureInverted));
            }
        }
        return featureInverted;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Feature basicGetFeatureInverted() {
        return featureInverted;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setFeatureInverted(Feature newFeatureInverted) {
        Feature oldFeatureInverted = featureInverted;
        featureInverted = newFeatureInverted;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE_INVERTING__FEATURE_INVERTED, oldFeatureInverted, featureInverted));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Feature getInvertingFeature() {
        if (invertingFeature != null && invertingFeature.eIsProxy()) {
            InternalEObject oldInvertingFeature = (InternalEObject)invertingFeature;
            invertingFeature = (Feature)eResolveProxy(oldInvertingFeature);
            if (invertingFeature != oldInvertingFeature) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.FEATURE_INVERTING__INVERTING_FEATURE, oldInvertingFeature, invertingFeature));
            }
        }
        return invertingFeature;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Feature basicGetInvertingFeature() {
        return invertingFeature;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setInvertingFeature(Feature newInvertingFeature) {
        Feature oldInvertingFeature = invertingFeature;
        invertingFeature = newInvertingFeature;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE_INVERTING__INVERTING_FEATURE, oldInvertingFeature, invertingFeature));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Feature getOwningFeature() {
        Feature owningFeature = basicGetOwningFeature();
        return owningFeature != null && owningFeature.eIsProxy() ? (Feature)eResolveProxy((InternalEObject)owningFeature) : owningFeature;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Feature basicGetOwningFeature() {
        // TODO: implement this method to return the 'Owning Feature' reference
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
            case SysmlPackage.FEATURE_INVERTING__FEATURE_INVERTED:
                if (resolve) return getFeatureInverted();
                return basicGetFeatureInverted();
            case SysmlPackage.FEATURE_INVERTING__INVERTING_FEATURE:
                if (resolve) return getInvertingFeature();
                return basicGetInvertingFeature();
            case SysmlPackage.FEATURE_INVERTING__OWNING_FEATURE:
                if (resolve) return getOwningFeature();
                return basicGetOwningFeature();
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
            case SysmlPackage.FEATURE_INVERTING__FEATURE_INVERTED:
                setFeatureInverted((Feature)newValue);
                return;
            case SysmlPackage.FEATURE_INVERTING__INVERTING_FEATURE:
                setInvertingFeature((Feature)newValue);
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
            case SysmlPackage.FEATURE_INVERTING__FEATURE_INVERTED:
                setFeatureInverted((Feature)null);
                return;
            case SysmlPackage.FEATURE_INVERTING__INVERTING_FEATURE:
                setInvertingFeature((Feature)null);
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
            case SysmlPackage.FEATURE_INVERTING__FEATURE_INVERTED:
                return featureInverted != null;
            case SysmlPackage.FEATURE_INVERTING__INVERTING_FEATURE:
                return invertingFeature != null;
            case SysmlPackage.FEATURE_INVERTING__OWNING_FEATURE:
                return basicGetOwningFeature() != null;
        }
        return super.eIsSet(featureID);
    }

} //FeatureInvertingImpl
