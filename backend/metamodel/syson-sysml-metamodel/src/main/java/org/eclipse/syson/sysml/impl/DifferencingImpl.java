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
import org.eclipse.syson.sysml.Differencing;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Differencing</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.DifferencingImpl#getDifferencingType <em>Differencing Type</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DifferencingImpl#getTypeDifferenced <em>Type Differenced</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DifferencingImpl extends RelationshipImpl implements Differencing {
    /**
     * The cached value of the '{@link #getDifferencingType() <em>Differencing Type</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDifferencingType()
     * @generated
     * @ordered
     */
    protected Type differencingType;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DifferencingImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getDifferencing();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Type getDifferencingType() {
        if (differencingType != null && differencingType.eIsProxy()) {
            InternalEObject oldDifferencingType = (InternalEObject)differencingType;
            differencingType = (Type)eResolveProxy(oldDifferencingType);
            if (differencingType != oldDifferencingType) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.DIFFERENCING__DIFFERENCING_TYPE, oldDifferencingType, differencingType));
            }
        }
        return differencingType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Type basicGetDifferencingType() {
        return differencingType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setDifferencingType(Type newDifferencingType) {
        Type oldDifferencingType = differencingType;
        differencingType = newDifferencingType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.DIFFERENCING__DIFFERENCING_TYPE, oldDifferencingType, differencingType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Type getTypeDifferenced() {
        Type typeDifferenced = basicGetTypeDifferenced();
        return typeDifferenced != null && typeDifferenced.eIsProxy() ? (Type)eResolveProxy((InternalEObject)typeDifferenced) : typeDifferenced;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Type basicGetTypeDifferenced() {
        // TODO: implement this method to return the 'Type Differenced' reference
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
            case SysmlPackage.DIFFERENCING__DIFFERENCING_TYPE:
                if (resolve) return getDifferencingType();
                return basicGetDifferencingType();
            case SysmlPackage.DIFFERENCING__TYPE_DIFFERENCED:
                if (resolve) return getTypeDifferenced();
                return basicGetTypeDifferenced();
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
            case SysmlPackage.DIFFERENCING__DIFFERENCING_TYPE:
                setDifferencingType((Type)newValue);
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
            case SysmlPackage.DIFFERENCING__DIFFERENCING_TYPE:
                setDifferencingType((Type)null);
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
            case SysmlPackage.DIFFERENCING__DIFFERENCING_TYPE:
                return differencingType != null;
            case SysmlPackage.DIFFERENCING__TYPE_DIFFERENCED:
                return basicGetTypeDifferenced() != null;
        }
        return super.eIsSet(featureID);
    }

} //DifferencingImpl
