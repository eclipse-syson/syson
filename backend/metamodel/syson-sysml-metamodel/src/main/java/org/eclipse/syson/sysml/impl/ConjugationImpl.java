/**
 * Copyright (c) 2023 Obeo.
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
import org.eclipse.syson.sysml.Conjugation;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Conjugation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.ConjugationImpl#getConjugatedType <em>Conjugated Type</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ConjugationImpl#getOriginalType <em>Original Type</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ConjugationImpl#getOwningType <em>Owning Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConjugationImpl extends RelationshipImpl implements Conjugation {
    /**
     * The cached value of the '{@link #getConjugatedType() <em>Conjugated Type</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConjugatedType()
     * @generated
     * @ordered
     */
    protected Type conjugatedType;

    /**
     * The cached value of the '{@link #getOriginalType() <em>Original Type</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOriginalType()
     * @generated
     * @ordered
     */
    protected Type originalType;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ConjugationImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getConjugation();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Type getConjugatedType() {
        if (conjugatedType != null && conjugatedType.eIsProxy()) {
            InternalEObject oldConjugatedType = (InternalEObject)conjugatedType;
            conjugatedType = (Type)eResolveProxy(oldConjugatedType);
            if (conjugatedType != oldConjugatedType) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.CONJUGATION__CONJUGATED_TYPE, oldConjugatedType, conjugatedType));
            }
        }
        return conjugatedType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Type basicGetConjugatedType() {
        return conjugatedType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setConjugatedType(Type newConjugatedType) {
        Type oldConjugatedType = conjugatedType;
        conjugatedType = newConjugatedType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.CONJUGATION__CONJUGATED_TYPE, oldConjugatedType, conjugatedType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Type getOriginalType() {
        if (originalType != null && originalType.eIsProxy()) {
            InternalEObject oldOriginalType = (InternalEObject)originalType;
            originalType = (Type)eResolveProxy(oldOriginalType);
            if (originalType != oldOriginalType) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.CONJUGATION__ORIGINAL_TYPE, oldOriginalType, originalType));
            }
        }
        return originalType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Type basicGetOriginalType() {
        return originalType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setOriginalType(Type newOriginalType) {
        Type oldOriginalType = originalType;
        originalType = newOriginalType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.CONJUGATION__ORIGINAL_TYPE, oldOriginalType, originalType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Type getOwningType() {
        Type owningType = basicGetOwningType();
        return owningType != null && owningType.eIsProxy() ? (Type)eResolveProxy((InternalEObject)owningType) : owningType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Type basicGetOwningType() {
        // TODO: implement this method to return the 'Owning Type' reference
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
            case SysmlPackage.CONJUGATION__CONJUGATED_TYPE:
                if (resolve) return getConjugatedType();
                return basicGetConjugatedType();
            case SysmlPackage.CONJUGATION__ORIGINAL_TYPE:
                if (resolve) return getOriginalType();
                return basicGetOriginalType();
            case SysmlPackage.CONJUGATION__OWNING_TYPE:
                if (resolve) return getOwningType();
                return basicGetOwningType();
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
            case SysmlPackage.CONJUGATION__CONJUGATED_TYPE:
                setConjugatedType((Type)newValue);
                return;
            case SysmlPackage.CONJUGATION__ORIGINAL_TYPE:
                setOriginalType((Type)newValue);
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
            case SysmlPackage.CONJUGATION__CONJUGATED_TYPE:
                setConjugatedType((Type)null);
                return;
            case SysmlPackage.CONJUGATION__ORIGINAL_TYPE:
                setOriginalType((Type)null);
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
            case SysmlPackage.CONJUGATION__CONJUGATED_TYPE:
                return conjugatedType != null;
            case SysmlPackage.CONJUGATION__ORIGINAL_TYPE:
                return originalType != null;
            case SysmlPackage.CONJUGATION__OWNING_TYPE:
                return basicGetOwningType() != null;
        }
        return super.eIsSet(featureID);
    }

} //ConjugationImpl
