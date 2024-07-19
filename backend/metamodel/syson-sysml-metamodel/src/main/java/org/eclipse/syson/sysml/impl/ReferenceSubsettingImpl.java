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
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Reference Subsetting</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.ReferenceSubsettingImpl#getReferencedFeature <em>Referenced
 * Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ReferenceSubsettingImpl#getReferencingFeature <em>Referencing
 * Feature</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ReferenceSubsettingImpl extends SubsettingImpl implements ReferenceSubsetting {
    /**
     * The cached value of the '{@link #getReferencedFeature() <em>Referenced Feature</em>}' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getReferencedFeature()
     * @generated
     * @ordered
     */
    protected Feature referencedFeature;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected ReferenceSubsettingImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getReferenceSubsetting();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature getReferencedFeature() {
        if (this.referencedFeature != null && this.referencedFeature.eIsProxy()) {
            InternalEObject oldReferencedFeature = (InternalEObject) this.referencedFeature;
            this.referencedFeature = (Feature) this.eResolveProxy(oldReferencedFeature);
            if (this.referencedFeature != oldReferencedFeature) {
                if (this.eNotificationRequired())
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.REFERENCE_SUBSETTING__REFERENCED_FEATURE, oldReferencedFeature, this.referencedFeature));
            }
        }
        return this.referencedFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Feature basicGetReferencedFeature() {
        return this.referencedFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setReferencedFeature(Feature newReferencedFeature) {
        Feature oldReferencedFeature = this.referencedFeature;
        this.referencedFeature = newReferencedFeature;
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.REFERENCE_SUBSETTING__REFERENCED_FEATURE, oldReferencedFeature, this.referencedFeature));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature getReferencingFeature() {
        Feature referencingFeature = this.basicGetReferencingFeature();
        return referencingFeature != null && referencingFeature.eIsProxy() ? (Feature) this.eResolveProxy((InternalEObject) referencingFeature) : referencingFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Feature basicGetReferencingFeature() {
        return this.getOwningFeature();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.REFERENCE_SUBSETTING__REFERENCED_FEATURE:
                if (resolve)
                    return this.getReferencedFeature();
                return this.basicGetReferencedFeature();
            case SysmlPackage.REFERENCE_SUBSETTING__REFERENCING_FEATURE:
                if (resolve)
                    return this.getReferencingFeature();
                return this.basicGetReferencingFeature();
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
            case SysmlPackage.REFERENCE_SUBSETTING__REFERENCED_FEATURE:
                this.setReferencedFeature((Feature) newValue);
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
            case SysmlPackage.REFERENCE_SUBSETTING__REFERENCED_FEATURE:
                this.setReferencedFeature((Feature) null);
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
            case SysmlPackage.REFERENCE_SUBSETTING__REFERENCED_FEATURE:
                return this.referencedFeature != null;
            case SysmlPackage.REFERENCE_SUBSETTING__REFERENCING_FEATURE:
                return this.basicGetReferencingFeature() != null;
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
        return this.getReferencedFeature();
    }

    /**
     * <!-- begin-user-doc --> Redefines setter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void setSubsettedFeature(Feature newSubsettedFeature) {
        this.setReferencedFeature(newSubsettedFeature);
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Feature getOwningFeature() {
        Element owner = this.getOwningRelatedElement();
        if (owner instanceof Feature feature) {
            return feature;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Feature getSubsettingFeature() {
        return this.getReferencingFeature();
    }
} // ReferenceSubsettingImpl
