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
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Subsetting</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.SubsettingImpl#getOwningFeature <em>Owning Feature</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.SubsettingImpl#getSubsettedFeature <em>Subsetted Feature</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.SubsettingImpl#getSubsettingFeature <em>Subsetting Feature</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SubsettingImpl extends SpecializationImpl implements Subsetting {
    /**
     * The cached value of the '{@link #getSubsettedFeature() <em>Subsetted Feature</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSubsettedFeature()
     * @generated
     * @ordered
     */
    protected Feature subsettedFeature;

    /**
     * The cached value of the '{@link #getSubsettingFeature() <em>Subsetting Feature</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSubsettingFeature()
     * @generated
     * @ordered
     */
    protected Feature subsettingFeature;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SubsettingImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getSubsetting();
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
    public Feature getSubsettedFeature() {
        if (subsettedFeature != null && subsettedFeature.eIsProxy()) {
            InternalEObject oldSubsettedFeature = (InternalEObject)subsettedFeature;
            subsettedFeature = (Feature)eResolveProxy(oldSubsettedFeature);
            if (subsettedFeature != oldSubsettedFeature) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.SUBSETTING__SUBSETTED_FEATURE, oldSubsettedFeature, subsettedFeature));
            }
        }
        return subsettedFeature;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Feature basicGetSubsettedFeature() {
        return subsettedFeature;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setSubsettedFeature(Feature newSubsettedFeature) {
        Feature oldSubsettedFeature = subsettedFeature;
        subsettedFeature = newSubsettedFeature;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.SUBSETTING__SUBSETTED_FEATURE, oldSubsettedFeature, subsettedFeature));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Feature getSubsettingFeature() {
        if (subsettingFeature != null && subsettingFeature.eIsProxy()) {
            InternalEObject oldSubsettingFeature = (InternalEObject)subsettingFeature;
            subsettingFeature = (Feature)eResolveProxy(oldSubsettingFeature);
            if (subsettingFeature != oldSubsettingFeature) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.SUBSETTING__SUBSETTING_FEATURE, oldSubsettingFeature, subsettingFeature));
            }
        }
        return subsettingFeature;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Feature basicGetSubsettingFeature() {
        return subsettingFeature;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setSubsettingFeature(Feature newSubsettingFeature) {
        Feature oldSubsettingFeature = subsettingFeature;
        subsettingFeature = newSubsettingFeature;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.SUBSETTING__SUBSETTING_FEATURE, oldSubsettingFeature, subsettingFeature));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.SUBSETTING__OWNING_FEATURE:
                if (resolve) return getOwningFeature();
                return basicGetOwningFeature();
            case SysmlPackage.SUBSETTING__SUBSETTED_FEATURE:
                if (resolve) return getSubsettedFeature();
                return basicGetSubsettedFeature();
            case SysmlPackage.SUBSETTING__SUBSETTING_FEATURE:
                if (resolve) return getSubsettingFeature();
                return basicGetSubsettingFeature();
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
            case SysmlPackage.SUBSETTING__SUBSETTED_FEATURE:
                setSubsettedFeature((Feature)newValue);
                return;
            case SysmlPackage.SUBSETTING__SUBSETTING_FEATURE:
                setSubsettingFeature((Feature)newValue);
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
            case SysmlPackage.SUBSETTING__SUBSETTED_FEATURE:
                setSubsettedFeature((Feature)null);
                return;
            case SysmlPackage.SUBSETTING__SUBSETTING_FEATURE:
                setSubsettingFeature((Feature)null);
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
            case SysmlPackage.SUBSETTING__OWNING_FEATURE:
                return basicGetOwningFeature() != null;
            case SysmlPackage.SUBSETTING__SUBSETTED_FEATURE:
                return subsettedFeature != null;
            case SysmlPackage.SUBSETTING__SUBSETTING_FEATURE:
                return subsettingFeature != null;
        }
        return super.eIsSet(featureID);
    }

} //SubsettingImpl
