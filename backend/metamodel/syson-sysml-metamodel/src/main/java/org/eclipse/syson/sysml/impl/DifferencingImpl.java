/*******************************************************************************
* Copyright (c) 2023, 2025 Obeo.
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

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectEList;
import org.eclipse.syson.sysml.Differencing;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Differencing</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.DifferencingImpl#getDifferencingType <em>Differencing Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DifferencingImpl#getTypeDifferenced <em>Type Differenced</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DifferencingImpl extends RelationshipImpl implements Differencing {
    /**
     * The cached value of the '{@link #getDifferencingType() <em>Differencing Type</em>}' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getDifferencingType()
     * @generated
     * @ordered
     */
    protected Type differencingType;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected DifferencingImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getDifferencing();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Type getDifferencingType() {
        if (this.differencingType != null && this.differencingType.eIsProxy()) {
            InternalEObject oldDifferencingType = (InternalEObject) this.differencingType;
            this.differencingType = (Type) this.eResolveProxy(oldDifferencingType);
            if (this.differencingType != oldDifferencingType) {
                if (this.eNotificationRequired()) {
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.DIFFERENCING__DIFFERENCING_TYPE, oldDifferencingType, this.differencingType));
                }
            }
        }
        return this.differencingType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Type basicGetDifferencingType() {
        return this.differencingType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setDifferencingType(Type newDifferencingType) {
        Type oldDifferencingType = this.differencingType;
        this.differencingType = newDifferencingType;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.DIFFERENCING__DIFFERENCING_TYPE, oldDifferencingType, this.differencingType));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Type getTypeDifferenced() {
        Type typeDifferenced = this.basicGetTypeDifferenced();
        return typeDifferenced != null && typeDifferenced.eIsProxy() ? (Type) this.eResolveProxy((InternalEObject) typeDifferenced) : typeDifferenced;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Type basicGetTypeDifferenced() {
        // TODO: implement this method to return the 'Type Differenced' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.DIFFERENCING__DIFFERENCING_TYPE:
                if (resolve) {
                    return this.getDifferencingType();
                }
                return this.basicGetDifferencingType();
            case SysmlPackage.DIFFERENCING__TYPE_DIFFERENCED:
                if (resolve) {
                    return this.getTypeDifferenced();
                }
                return this.basicGetTypeDifferenced();
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
            case SysmlPackage.DIFFERENCING__DIFFERENCING_TYPE:
                this.setDifferencingType((Type) newValue);
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
            case SysmlPackage.DIFFERENCING__DIFFERENCING_TYPE:
                this.setDifferencingType((Type) null);
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
            case SysmlPackage.DIFFERENCING__DIFFERENCING_TYPE:
                return this.differencingType != null;
            case SysmlPackage.DIFFERENCING__TYPE_DIFFERENCED:
                return this.basicGetTypeDifferenced() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Element> getTarget() {
        EList<Element> targets = new EObjectEList.Unsettable<>(Element.class, this, SysmlPackage.DIFFERENCING__TARGET) {
            @Override
            public boolean addAll(Collection<? extends Element> collection) {
                if (collection != null) {
                    Iterator<? extends Element> iterator = collection.iterator();
                    if (iterator.hasNext()) {
                        Element next = iterator.next();
                        if (next instanceof Type type) {
                            DifferencingImpl.this.setDifferencingType(type);
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            protected void dispatchNotification(Notification notification) {
            }
        };
        Type differencingType = this.getDifferencingType();
        if (differencingType != null) {
            targets.add(differencingType);
        }
        return targets;
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Element> getSource() {
        EList<Element> sources = new EObjectEList.Unsettable<>(Element.class, this, SysmlPackage.DIFFERENCING__SOURCE) {
            @Override
            public boolean addAll(Collection<? extends Element> collection) {
                return false;
            }

            @Override
            protected void dispatchNotification(Notification notification) {
            }
        };
        Type typeDifferenced = this.getTypeDifferenced();
        if (typeDifferenced != null) {
            sources.add(typeDifferenced);
        }
        return sources;
    }

} // DifferencingImpl
