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
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Reference Subsetting</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.ReferenceSubsettingImpl#getReferencedFeature <em>Referenced Feature</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ReferenceSubsettingImpl#getReferencingFeature <em>Referencing Feature</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ReferenceSubsettingImpl extends SubsettingImpl implements ReferenceSubsetting {
    /**
     * The cached value of the '{@link #getReferencedFeature() <em>Referenced Feature</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getReferencedFeature()
     * @generated
     * @ordered
     */
    protected Feature referencedFeature;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ReferenceSubsettingImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getReferenceSubsetting();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Feature getReferencedFeature() {
        if (referencedFeature != null && referencedFeature.eIsProxy()) {
            InternalEObject oldReferencedFeature = (InternalEObject)referencedFeature;
            referencedFeature = (Feature)eResolveProxy(oldReferencedFeature);
            if (referencedFeature != oldReferencedFeature) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.REFERENCE_SUBSETTING__REFERENCED_FEATURE, oldReferencedFeature, referencedFeature));
            }
        }
        return referencedFeature;
    }
    
    /**
     * referencedFeature : Feature {redefines subsettedFeature}.
     * 
     * The Feature that is referenced by the referencingFeature of this ReferenceSubsetting.
     * 
     * @generated NOT
     */
    @Override
    public Feature getSubsettedFeature() {
        return this.getReferencedFeature();
    }

    
    /**
     * referencedFeature : Feature {redefines subsettedFeature}.
     * 
     * The Feature that is referenced by the referencingFeature of this ReferenceSubsetting.
     * Setter.
     * 
     * @generated NOT
     */
    @Override
    public void setSubsettedFeature(Feature newSubsettedFeature) {
        super.setSubsettedFeature(newSubsettedFeature);
        this.setReferencedFeature(newSubsettedFeature);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Feature basicGetReferencedFeature() {
        return referencedFeature;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setReferencedFeature(Feature newReferencedFeature) {
        Feature oldReferencedFeature = referencedFeature;
        referencedFeature = newReferencedFeature;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.REFERENCE_SUBSETTING__REFERENCED_FEATURE, oldReferencedFeature, referencedFeature));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Feature getReferencingFeature() {
        Feature referencingFeature = basicGetReferencingFeature();
        return referencingFeature != null && referencingFeature.eIsProxy() ? (Feature)eResolveProxy((InternalEObject)referencingFeature) : referencingFeature;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Feature basicGetReferencingFeature() {
        // TODO: implement this method to return the 'Referencing Feature' reference
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
            case SysmlPackage.REFERENCE_SUBSETTING__REFERENCED_FEATURE:
                if (resolve) return getReferencedFeature();
                return basicGetReferencedFeature();
            case SysmlPackage.REFERENCE_SUBSETTING__REFERENCING_FEATURE:
                if (resolve) return getReferencingFeature();
                return basicGetReferencingFeature();
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
            case SysmlPackage.REFERENCE_SUBSETTING__REFERENCED_FEATURE:
                setReferencedFeature((Feature)newValue);
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
            case SysmlPackage.REFERENCE_SUBSETTING__REFERENCED_FEATURE:
                setReferencedFeature((Feature)null);
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
            case SysmlPackage.REFERENCE_SUBSETTING__REFERENCED_FEATURE:
                return referencedFeature != null;
            case SysmlPackage.REFERENCE_SUBSETTING__REFERENCING_FEATURE:
                return basicGetReferencingFeature() != null;
        }
        return super.eIsSet(featureID);
    }

} //ReferenceSubsettingImpl
