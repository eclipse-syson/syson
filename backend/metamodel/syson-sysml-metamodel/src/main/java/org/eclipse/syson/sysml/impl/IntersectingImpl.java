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
import org.eclipse.syson.sysml.Intersecting;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Intersecting</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.IntersectingImpl#getIntersectingType <em>Intersecting Type</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.IntersectingImpl#getTypeIntersected <em>Type Intersected</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IntersectingImpl extends RelationshipImpl implements Intersecting {
    /**
     * The cached value of the '{@link #getIntersectingType() <em>Intersecting Type</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIntersectingType()
     * @generated
     * @ordered
     */
    protected Type intersectingType;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected IntersectingImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getIntersecting();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Type getIntersectingType() {
        if (intersectingType != null && intersectingType.eIsProxy()) {
            InternalEObject oldIntersectingType = (InternalEObject)intersectingType;
            intersectingType = (Type)eResolveProxy(oldIntersectingType);
            if (intersectingType != oldIntersectingType) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.INTERSECTING__INTERSECTING_TYPE, oldIntersectingType, intersectingType));
            }
        }
        return intersectingType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Type basicGetIntersectingType() {
        return intersectingType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setIntersectingType(Type newIntersectingType) {
        Type oldIntersectingType = intersectingType;
        intersectingType = newIntersectingType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.INTERSECTING__INTERSECTING_TYPE, oldIntersectingType, intersectingType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Type getTypeIntersected() {
        Type typeIntersected = basicGetTypeIntersected();
        return typeIntersected != null && typeIntersected.eIsProxy() ? (Type)eResolveProxy((InternalEObject)typeIntersected) : typeIntersected;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Type basicGetTypeIntersected() {
        // TODO: implement this method to return the 'Type Intersected' reference
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
            case SysmlPackage.INTERSECTING__INTERSECTING_TYPE:
                if (resolve) return getIntersectingType();
                return basicGetIntersectingType();
            case SysmlPackage.INTERSECTING__TYPE_INTERSECTED:
                if (resolve) return getTypeIntersected();
                return basicGetTypeIntersected();
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
            case SysmlPackage.INTERSECTING__INTERSECTING_TYPE:
                setIntersectingType((Type)newValue);
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
            case SysmlPackage.INTERSECTING__INTERSECTING_TYPE:
                setIntersectingType((Type)null);
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
            case SysmlPackage.INTERSECTING__INTERSECTING_TYPE:
                return intersectingType != null;
            case SysmlPackage.INTERSECTING__TYPE_INTERSECTED:
                return basicGetTypeIntersected() != null;
        }
        return super.eIsSet(featureID);
    }

} //IntersectingImpl
