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

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectEList;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Intersecting;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Intersecting</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.IntersectingImpl#getIntersectingType <em>Intersecting Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.IntersectingImpl#getTypeIntersected <em>Type Intersected</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IntersectingImpl extends RelationshipImpl implements Intersecting {
    /**
     * The cached value of the '{@link #getIntersectingType() <em>Intersecting Type</em>}' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getIntersectingType()
     * @generated
     * @ordered
     */
    protected Type intersectingType;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected IntersectingImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getIntersecting();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Type getIntersectingType() {
        if (this.intersectingType != null && this.intersectingType.eIsProxy()) {
            InternalEObject oldIntersectingType = (InternalEObject) this.intersectingType;
            this.intersectingType = (Type) this.eResolveProxy(oldIntersectingType);
            if (this.intersectingType != oldIntersectingType) {
                if (this.eNotificationRequired())
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.INTERSECTING__INTERSECTING_TYPE, oldIntersectingType, this.intersectingType));
            }
        }
        return this.intersectingType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Type basicGetIntersectingType() {
        return this.intersectingType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIntersectingType(Type newIntersectingType) {
        Type oldIntersectingType = this.intersectingType;
        this.intersectingType = newIntersectingType;
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.INTERSECTING__INTERSECTING_TYPE, oldIntersectingType, this.intersectingType));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Type getTypeIntersected() {
        Type typeIntersected = this.basicGetTypeIntersected();
        return typeIntersected != null && typeIntersected.eIsProxy() ? (Type) this.eResolveProxy((InternalEObject) typeIntersected) : typeIntersected;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Type basicGetTypeIntersected() {
        // TODO: implement this method to return the 'Type Intersected' reference
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
            case SysmlPackage.INTERSECTING__INTERSECTING_TYPE:
                if (resolve)
                    return this.getIntersectingType();
                return this.basicGetIntersectingType();
            case SysmlPackage.INTERSECTING__TYPE_INTERSECTED:
                if (resolve)
                    return this.getTypeIntersected();
                return this.basicGetTypeIntersected();
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
            case SysmlPackage.INTERSECTING__INTERSECTING_TYPE:
                this.setIntersectingType((Type) newValue);
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
            case SysmlPackage.INTERSECTING__INTERSECTING_TYPE:
                this.setIntersectingType((Type) null);
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
            case SysmlPackage.INTERSECTING__INTERSECTING_TYPE:
                return this.intersectingType != null;
            case SysmlPackage.INTERSECTING__TYPE_INTERSECTED:
                return this.basicGetTypeIntersected() != null;
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
        EList<Element> targets = new EObjectEList.Unsettable<>(Element.class, this, SysmlPackage.INTERSECTING__TARGET) {
            @Override
            public boolean addAll(Collection<? extends Element> collection) {
                if (collection != null) {
                    Iterator<? extends Element> iterator = collection.iterator();
                    if (iterator.hasNext()) {
                        Element next = iterator.next();
                        if (next instanceof Type type) {
                            IntersectingImpl.this.setIntersectingType(type);
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
        Type intersectingType = this.getIntersectingType();
        if (intersectingType != null) {
            targets.add(intersectingType);
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
        EList<Element> sources = new EObjectEList.Unsettable<>(Element.class, this, SysmlPackage.INTERSECTING__SOURCE) {
            @Override
            public boolean addAll(Collection<? extends Element> collection) {
                return false;
            }

            @Override
            protected void dispatchNotification(Notification notification) {
            }
        };
        Type typeIntersected = this.getTypeIntersected();
        if (typeIntersected != null) {
            sources.add(typeIntersected);
        }
        return sources;
    }

} // IntersectingImpl
