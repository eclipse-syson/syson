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
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.TypeFeaturing;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Type Featuring</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.TypeFeaturingImpl#getFeatureOfType <em>Feature Of Type</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TypeFeaturingImpl#getFeaturingType <em>Featuring Type</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.TypeFeaturingImpl#getOwningFeatureOfType <em>Owning Feature Of Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TypeFeaturingImpl extends FeaturingImpl implements TypeFeaturing {
    /**
     * The cached value of the '{@link #getFeatureOfType() <em>Feature Of Type</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFeatureOfType()
     * @generated
     * @ordered
     */
    protected Feature featureOfType;

    /**
     * The cached value of the '{@link #getFeaturingType() <em>Featuring Type</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFeaturingType()
     * @generated
     * @ordered
     */
    protected Type featuringType;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TypeFeaturingImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getTypeFeaturing();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Feature getFeatureOfType() {
        if (featureOfType != null && featureOfType.eIsProxy()) {
            InternalEObject oldFeatureOfType = (InternalEObject)featureOfType;
            featureOfType = (Feature)eResolveProxy(oldFeatureOfType);
            if (featureOfType != oldFeatureOfType) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.TYPE_FEATURING__FEATURE_OF_TYPE, oldFeatureOfType, featureOfType));
            }
        }
        return featureOfType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Feature basicGetFeatureOfType() {
        return featureOfType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setFeatureOfType(Feature newFeatureOfType) {
        Feature oldFeatureOfType = featureOfType;
        featureOfType = newFeatureOfType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.TYPE_FEATURING__FEATURE_OF_TYPE, oldFeatureOfType, featureOfType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Type getFeaturingType() {
        if (featuringType != null && featuringType.eIsProxy()) {
            InternalEObject oldFeaturingType = (InternalEObject)featuringType;
            featuringType = (Type)eResolveProxy(oldFeaturingType);
            if (featuringType != oldFeaturingType) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.TYPE_FEATURING__FEATURING_TYPE, oldFeaturingType, featuringType));
            }
        }
        return featuringType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Type basicGetFeaturingType() {
        return featuringType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setFeaturingType(Type newFeaturingType) {
        Type oldFeaturingType = featuringType;
        featuringType = newFeaturingType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.TYPE_FEATURING__FEATURING_TYPE, oldFeaturingType, featuringType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Feature getOwningFeatureOfType() {
        Feature owningFeatureOfType = basicGetOwningFeatureOfType();
        return owningFeatureOfType != null && owningFeatureOfType.eIsProxy() ? (Feature)eResolveProxy((InternalEObject)owningFeatureOfType) : owningFeatureOfType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Feature basicGetOwningFeatureOfType() {
        // TODO: implement this method to return the 'Owning Feature Of Type' reference
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
            case SysmlPackage.TYPE_FEATURING__FEATURE_OF_TYPE:
                if (resolve) return getFeatureOfType();
                return basicGetFeatureOfType();
            case SysmlPackage.TYPE_FEATURING__FEATURING_TYPE:
                if (resolve) return getFeaturingType();
                return basicGetFeaturingType();
            case SysmlPackage.TYPE_FEATURING__OWNING_FEATURE_OF_TYPE:
                if (resolve) return getOwningFeatureOfType();
                return basicGetOwningFeatureOfType();
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
            case SysmlPackage.TYPE_FEATURING__FEATURE_OF_TYPE:
                setFeatureOfType((Feature)newValue);
                return;
            case SysmlPackage.TYPE_FEATURING__FEATURING_TYPE:
                setFeaturingType((Type)newValue);
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
            case SysmlPackage.TYPE_FEATURING__FEATURE_OF_TYPE:
                setFeatureOfType((Feature)null);
                return;
            case SysmlPackage.TYPE_FEATURING__FEATURING_TYPE:
                setFeaturingType((Type)null);
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
            case SysmlPackage.TYPE_FEATURING__FEATURE_OF_TYPE:
                return featureOfType != null;
            case SysmlPackage.TYPE_FEATURING__FEATURING_TYPE:
                return featuringType != null;
            case SysmlPackage.TYPE_FEATURING__OWNING_FEATURE_OF_TYPE:
                return basicGetOwningFeatureOfType() != null;
        }
        return super.eIsSet(featureID);
    }

} //TypeFeaturingImpl
