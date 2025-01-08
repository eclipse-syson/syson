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
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Subsetting</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.SubsettingImpl#getOwningFeature <em>Owning Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.SubsettingImpl#getSubsettedFeature <em>Subsetted Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.SubsettingImpl#getSubsettingFeature <em>Subsetting Feature</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SubsettingImpl extends SpecializationImpl implements Subsetting {
    /**
     * The cached value of the '{@link #getSubsettedFeature() <em>Subsetted Feature</em>}' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getSubsettedFeature()
     * @generated
     * @ordered
     */
    protected Feature subsettedFeature;

    /**
     * The cached value of the '{@link #getSubsettingFeature() <em>Subsetting Feature</em>}' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getSubsettingFeature()
     * @generated
     * @ordered
     */
    protected Feature subsettingFeature;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected SubsettingImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getSubsetting();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature getOwningFeature() {
        Feature owningFeature = this.basicGetOwningFeature();
        return owningFeature != null && owningFeature.eIsProxy() ? (Feature) this.eResolveProxy((InternalEObject) owningFeature) : owningFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Feature basicGetOwningFeature() {
        Element owningRel = this.getOwningRelatedElement();
        if (owningRel instanceof Feature feature) {
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
    public Feature getSubsettedFeature() {
        if (this.subsettedFeature != null && this.subsettedFeature.eIsProxy()) {
            InternalEObject oldSubsettedFeature = (InternalEObject) this.subsettedFeature;
            this.subsettedFeature = (Feature) this.eResolveProxy(oldSubsettedFeature);
            if (this.subsettedFeature != oldSubsettedFeature) {
                if (this.eNotificationRequired()) {
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.SUBSETTING__SUBSETTED_FEATURE, oldSubsettedFeature, this.subsettedFeature));
                }
            }
        }
        return this.subsettedFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Feature basicGetSubsettedFeature() {
        return this.subsettedFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setSubsettedFeature(Feature newSubsettedFeature) {
        Feature oldSubsettedFeature = this.subsettedFeature;
        this.subsettedFeature = newSubsettedFeature;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.SUBSETTING__SUBSETTED_FEATURE, oldSubsettedFeature, this.subsettedFeature));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature getSubsettingFeature() {
        if (this.subsettingFeature != null && this.subsettingFeature.eIsProxy()) {
            InternalEObject oldSubsettingFeature = (InternalEObject) this.subsettingFeature;
            this.subsettingFeature = (Feature) this.eResolveProxy(oldSubsettingFeature);
            if (this.subsettingFeature != oldSubsettingFeature) {
                if (this.eNotificationRequired()) {
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.SUBSETTING__SUBSETTING_FEATURE, oldSubsettingFeature, this.subsettingFeature));
                }
            }
        }
        return this.subsettingFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Feature basicGetSubsettingFeature() {
        return this.subsettingFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setSubsettingFeature(Feature newSubsettingFeature) {
        Feature oldSubsettingFeature = this.subsettingFeature;
        this.subsettingFeature = newSubsettingFeature;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.SUBSETTING__SUBSETTING_FEATURE, oldSubsettingFeature, this.subsettingFeature));
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
            case SysmlPackage.SUBSETTING__OWNING_FEATURE:
                if (resolve) {
                    return this.getOwningFeature();
                }
                return this.basicGetOwningFeature();
            case SysmlPackage.SUBSETTING__SUBSETTED_FEATURE:
                if (resolve) {
                    return this.getSubsettedFeature();
                }
                return this.basicGetSubsettedFeature();
            case SysmlPackage.SUBSETTING__SUBSETTING_FEATURE:
                if (resolve) {
                    return this.getSubsettingFeature();
                }
                return this.basicGetSubsettingFeature();
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
            case SysmlPackage.SUBSETTING__SUBSETTED_FEATURE:
                this.setSubsettedFeature((Feature) newValue);
                return;
            case SysmlPackage.SUBSETTING__SUBSETTING_FEATURE:
                this.setSubsettingFeature((Feature) newValue);
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
            case SysmlPackage.SUBSETTING__SUBSETTED_FEATURE:
                this.setSubsettedFeature((Feature) null);
                return;
            case SysmlPackage.SUBSETTING__SUBSETTING_FEATURE:
                this.setSubsettingFeature((Feature) null);
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
            case SysmlPackage.SUBSETTING__OWNING_FEATURE:
                return this.basicGetOwningFeature() != null;
            case SysmlPackage.SUBSETTING__SUBSETTED_FEATURE:
                return this.subsettedFeature != null;
            case SysmlPackage.SUBSETTING__SUBSETTING_FEATURE:
                return this.subsettingFeature != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Type getOwningType() {
        return this.getOwningFeature();
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Type getGeneral() {
        return this.getSubsettedFeature();
    }

    /**
     * <!-- begin-user-doc --> Redefines setter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void setGeneral(Type newGeneral) {
        if (newGeneral instanceof Feature newGeneralFeature) {
            this.setSubsettedFeature(newGeneralFeature);
        }
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Type getSpecific() {
        return this.getSubsettingFeature();
    }

    /**
     * <!-- begin-user-doc --> Redefines setter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void setSpecific(Type newSpecific) {
        if (newSpecific instanceof Feature newSpecificFeature) {
            this.setSubsettingFeature(newSpecificFeature);
        }
    }

} // SubsettingImpl
