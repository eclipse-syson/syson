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
import org.eclipse.syson.sysml.Disjoining;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Disjoining</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.DisjoiningImpl#getDisjoiningType <em>Disjoining Type</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DisjoiningImpl#getOwningType <em>Owning Type</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DisjoiningImpl#getTypeDisjoined <em>Type Disjoined</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DisjoiningImpl extends RelationshipImpl implements Disjoining {
    /**
     * The cached value of the '{@link #getDisjoiningType() <em>Disjoining Type</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDisjoiningType()
     * @generated
     * @ordered
     */
    protected Type disjoiningType;

    /**
     * The cached value of the '{@link #getTypeDisjoined() <em>Type Disjoined</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTypeDisjoined()
     * @generated
     * @ordered
     */
    protected Type typeDisjoined;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DisjoiningImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getDisjoining();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Type getDisjoiningType() {
        if (disjoiningType != null && disjoiningType.eIsProxy()) {
            InternalEObject oldDisjoiningType = (InternalEObject)disjoiningType;
            disjoiningType = (Type)eResolveProxy(oldDisjoiningType);
            if (disjoiningType != oldDisjoiningType) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.DISJOINING__DISJOINING_TYPE, oldDisjoiningType, disjoiningType));
            }
        }
        return disjoiningType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Type basicGetDisjoiningType() {
        return disjoiningType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setDisjoiningType(Type newDisjoiningType) {
        Type oldDisjoiningType = disjoiningType;
        disjoiningType = newDisjoiningType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.DISJOINING__DISJOINING_TYPE, oldDisjoiningType, disjoiningType));
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
    public Type getTypeDisjoined() {
        if (typeDisjoined != null && typeDisjoined.eIsProxy()) {
            InternalEObject oldTypeDisjoined = (InternalEObject)typeDisjoined;
            typeDisjoined = (Type)eResolveProxy(oldTypeDisjoined);
            if (typeDisjoined != oldTypeDisjoined) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.DISJOINING__TYPE_DISJOINED, oldTypeDisjoined, typeDisjoined));
            }
        }
        return typeDisjoined;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Type basicGetTypeDisjoined() {
        return typeDisjoined;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setTypeDisjoined(Type newTypeDisjoined) {
        Type oldTypeDisjoined = typeDisjoined;
        typeDisjoined = newTypeDisjoined;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.DISJOINING__TYPE_DISJOINED, oldTypeDisjoined, typeDisjoined));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.DISJOINING__DISJOINING_TYPE:
                if (resolve) return getDisjoiningType();
                return basicGetDisjoiningType();
            case SysmlPackage.DISJOINING__OWNING_TYPE:
                if (resolve) return getOwningType();
                return basicGetOwningType();
            case SysmlPackage.DISJOINING__TYPE_DISJOINED:
                if (resolve) return getTypeDisjoined();
                return basicGetTypeDisjoined();
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
            case SysmlPackage.DISJOINING__DISJOINING_TYPE:
                setDisjoiningType((Type)newValue);
                return;
            case SysmlPackage.DISJOINING__TYPE_DISJOINED:
                setTypeDisjoined((Type)newValue);
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
            case SysmlPackage.DISJOINING__DISJOINING_TYPE:
                setDisjoiningType((Type)null);
                return;
            case SysmlPackage.DISJOINING__TYPE_DISJOINED:
                setTypeDisjoined((Type)null);
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
            case SysmlPackage.DISJOINING__DISJOINING_TYPE:
                return disjoiningType != null;
            case SysmlPackage.DISJOINING__OWNING_TYPE:
                return basicGetOwningType() != null;
            case SysmlPackage.DISJOINING__TYPE_DISJOINED:
                return typeDisjoined != null;
        }
        return super.eIsSet(featureID);
    }

} //DisjoiningImpl
