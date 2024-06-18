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
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Feature Typing</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureTypingImpl#getOwningFeature <em>Owning Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureTypingImpl#getType <em>Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureTypingImpl#getTypedFeature <em>Typed Feature</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FeatureTypingImpl extends SpecializationImpl implements FeatureTyping {
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
     * The cached value of the '{@link #getTypedFeature() <em>Typed Feature</em>}' reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getTypedFeature()
     * @generated
     * @ordered
     */
    protected Feature typedFeature;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected FeatureTypingImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getFeatureTyping();
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
        Element owningRelElement = this.getOwningRelatedElement();
        if (owningRelElement instanceof Feature owningFeature) {
            return owningFeature;
        }
        return null;
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
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.FEATURE_TYPING__TYPE, oldType, this.type));
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
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE_TYPING__TYPE, oldType, this.type));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature getTypedFeature() {
        if (this.typedFeature != null && this.typedFeature.eIsProxy()) {
            InternalEObject oldTypedFeature = (InternalEObject) this.typedFeature;
            this.typedFeature = (Feature) this.eResolveProxy(oldTypedFeature);
            if (this.typedFeature != oldTypedFeature) {
                if (this.eNotificationRequired()) {
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.FEATURE_TYPING__TYPED_FEATURE, oldTypedFeature, this.typedFeature));
                }
            }
        }
        return this.typedFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Feature basicGetTypedFeature() {
        return this.typedFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setTypedFeature(Feature newTypedFeature) {
        Feature oldTypedFeature = this.typedFeature;
        this.typedFeature = newTypedFeature;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE_TYPING__TYPED_FEATURE, oldTypedFeature, this.typedFeature));
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
            case SysmlPackage.FEATURE_TYPING__OWNING_FEATURE:
                if (resolve) {
                    return this.getOwningFeature();
                }
                return this.basicGetOwningFeature();
            case SysmlPackage.FEATURE_TYPING__TYPE:
                if (resolve) {
                    return this.getType();
                }
                return this.basicGetType();
            case SysmlPackage.FEATURE_TYPING__TYPED_FEATURE:
                if (resolve) {
                    return this.getTypedFeature();
                }
                return this.basicGetTypedFeature();
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
            case SysmlPackage.FEATURE_TYPING__TYPE:
                this.setType((Type) newValue);
                return;
            case SysmlPackage.FEATURE_TYPING__TYPED_FEATURE:
                this.setTypedFeature((Feature) newValue);
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
            case SysmlPackage.FEATURE_TYPING__TYPE:
                this.setType((Type) null);
                return;
            case SysmlPackage.FEATURE_TYPING__TYPED_FEATURE:
                this.setTypedFeature((Feature) null);
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
            case SysmlPackage.FEATURE_TYPING__OWNING_FEATURE:
                return this.basicGetOwningFeature() != null;
            case SysmlPackage.FEATURE_TYPING__TYPE:
                return this.type != null;
            case SysmlPackage.FEATURE_TYPING__TYPED_FEATURE:
                return this.typedFeature != null;
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
        return this.getType();
    }

    /**
     * <!-- begin-user-doc --> Redefines setter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void setGeneral(Type newGeneral) {
        this.setType(newGeneral);
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Type getSpecific() {
        return this.getTypedFeature();
    }

    /**
     * <!-- begin-user-doc --> Redefines setter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void setSpecific(Type newSpecific) {
        if (newSpecific instanceof Feature newSpecificFeature) {
            this.setTypedFeature(newSpecificFeature);
        }
    }

} // FeatureTypingImpl
