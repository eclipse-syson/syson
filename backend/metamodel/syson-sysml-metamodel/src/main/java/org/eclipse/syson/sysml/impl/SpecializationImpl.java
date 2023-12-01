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
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Specialization</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.SpecializationImpl#getGeneral <em>General</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.SpecializationImpl#getOwningType <em>Owning Type</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.SpecializationImpl#getSpecific <em>Specific</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SpecializationImpl extends RelationshipImpl implements Specialization {
    /**
     * The cached value of the '{@link #getGeneral() <em>General</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getGeneral()
     * @generated
     * @ordered
     */
    protected Type general;

    /**
     * The cached value of the '{@link #getSpecific() <em>Specific</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSpecific()
     * @generated
     * @ordered
     */
    protected Type specific;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SpecializationImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getSpecialization();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Type getGeneral() {
        if (general != null && general.eIsProxy()) {
            InternalEObject oldGeneral = (InternalEObject)general;
            general = (Type)eResolveProxy(oldGeneral);
            if (general != oldGeneral) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.SPECIALIZATION__GENERAL, oldGeneral, general));
            }
        }
        return general;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Type basicGetGeneral() {
        return general;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setGeneral(Type newGeneral) {
        Type oldGeneral = general;
        general = newGeneral;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.SPECIALIZATION__GENERAL, oldGeneral, general));
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
    public Type getSpecific() {
        if (specific != null && specific.eIsProxy()) {
            InternalEObject oldSpecific = (InternalEObject)specific;
            specific = (Type)eResolveProxy(oldSpecific);
            if (specific != oldSpecific) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.SPECIALIZATION__SPECIFIC, oldSpecific, specific));
            }
        }
        return specific;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Type basicGetSpecific() {
        return specific;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setSpecific(Type newSpecific) {
        Type oldSpecific = specific;
        specific = newSpecific;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.SPECIALIZATION__SPECIFIC, oldSpecific, specific));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.SPECIALIZATION__GENERAL:
                if (resolve) return getGeneral();
                return basicGetGeneral();
            case SysmlPackage.SPECIALIZATION__OWNING_TYPE:
                if (resolve) return getOwningType();
                return basicGetOwningType();
            case SysmlPackage.SPECIALIZATION__SPECIFIC:
                if (resolve) return getSpecific();
                return basicGetSpecific();
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
            case SysmlPackage.SPECIALIZATION__GENERAL:
                setGeneral((Type)newValue);
                return;
            case SysmlPackage.SPECIALIZATION__SPECIFIC:
                setSpecific((Type)newValue);
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
            case SysmlPackage.SPECIALIZATION__GENERAL:
                setGeneral((Type)null);
                return;
            case SysmlPackage.SPECIALIZATION__SPECIFIC:
                setSpecific((Type)null);
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
            case SysmlPackage.SPECIALIZATION__GENERAL:
                return general != null;
            case SysmlPackage.SPECIALIZATION__OWNING_TYPE:
                return basicGetOwningType() != null;
            case SysmlPackage.SPECIALIZATION__SPECIFIC:
                return specific != null;
        }
        return super.eIsSet(featureID);
    }

} //SpecializationImpl
