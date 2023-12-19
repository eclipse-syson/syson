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
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Feature Typing</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureTypingImpl#getOwningFeature <em>Owning Feature</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureTypingImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureTypingImpl#getTypedFeature <em>Typed Feature</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FeatureTypingImpl extends SpecializationImpl implements FeatureTyping {
    /**
     * The cached value of the '{@link #getType() <em>Type</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getType()
     * @generated
     * @ordered
     */
    protected Type type;

    /**
     * The cached value of the '{@link #getTypedFeature() <em>Typed Feature</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTypedFeature()
     * @generated
     * @ordered
     */
    protected Feature typedFeature;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected FeatureTypingImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getFeatureTyping();
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
    public Type getType() {
        if (type != null && type.eIsProxy()) {
            InternalEObject oldType = (InternalEObject)type;
            type = (Type)eResolveProxy(oldType);
            if (type != oldType) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.FEATURE_TYPING__TYPE, oldType, type));
            }
        }
        return type;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Type basicGetType() {
        return type;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setType(Type newType) {
        Type oldType = type;
        type = newType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE_TYPING__TYPE, oldType, type));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Feature getTypedFeature() {
        if (typedFeature != null && typedFeature.eIsProxy()) {
            InternalEObject oldTypedFeature = (InternalEObject)typedFeature;
            typedFeature = (Feature)eResolveProxy(oldTypedFeature);
            if (typedFeature != oldTypedFeature) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.FEATURE_TYPING__TYPED_FEATURE, oldTypedFeature, typedFeature));
            }
        }
        return typedFeature;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Feature basicGetTypedFeature() {
        return typedFeature;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setTypedFeature(Feature newTypedFeature) {
        Feature oldTypedFeature = typedFeature;
        typedFeature = newTypedFeature;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE_TYPING__TYPED_FEATURE, oldTypedFeature, typedFeature));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.FEATURE_TYPING__OWNING_FEATURE:
                if (resolve) return getOwningFeature();
                return basicGetOwningFeature();
            case SysmlPackage.FEATURE_TYPING__TYPE:
                if (resolve) return getType();
                return basicGetType();
            case SysmlPackage.FEATURE_TYPING__TYPED_FEATURE:
                if (resolve) return getTypedFeature();
                return basicGetTypedFeature();
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
            case SysmlPackage.FEATURE_TYPING__TYPE:
                setType((Type)newValue);
                return;
            case SysmlPackage.FEATURE_TYPING__TYPED_FEATURE:
                setTypedFeature((Feature)newValue);
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
            case SysmlPackage.FEATURE_TYPING__TYPE:
                setType((Type)null);
                return;
            case SysmlPackage.FEATURE_TYPING__TYPED_FEATURE:
                setTypedFeature((Feature)null);
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
            case SysmlPackage.FEATURE_TYPING__OWNING_FEATURE:
                return basicGetOwningFeature() != null;
            case SysmlPackage.FEATURE_TYPING__TYPE:
                return type != null;
            case SysmlPackage.FEATURE_TYPING__TYPED_FEATURE:
                return typedFeature != null;
        }
        return super.eIsSet(featureID);
    }

} //FeatureTypingImpl
