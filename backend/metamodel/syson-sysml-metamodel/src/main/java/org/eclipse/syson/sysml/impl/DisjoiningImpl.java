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
import org.eclipse.syson.sysml.Disjoining;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Disjoining</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.DisjoiningImpl#getDisjoiningType <em>Disjoining Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DisjoiningImpl#getOwningType <em>Owning Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DisjoiningImpl#getTypeDisjoined <em>Type Disjoined</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DisjoiningImpl extends RelationshipImpl implements Disjoining {
    /**
     * The cached value of the '{@link #getDisjoiningType() <em>Disjoining Type</em>}' reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @see #getDisjoiningType()
     * @generated
     * @ordered
     */
    protected Type disjoiningType;

    /**
     * The cached value of the '{@link #getTypeDisjoined() <em>Type Disjoined</em>}' reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getTypeDisjoined()
     * @generated
     * @ordered
     */
    protected Type typeDisjoined;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected DisjoiningImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getDisjoining();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Type getDisjoiningType() {
        if (this.disjoiningType != null && this.disjoiningType.eIsProxy()) {
            InternalEObject oldDisjoiningType = (InternalEObject) this.disjoiningType;
            this.disjoiningType = (Type) this.eResolveProxy(oldDisjoiningType);
            if (this.disjoiningType != oldDisjoiningType) {
                if (this.eNotificationRequired())
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.DISJOINING__DISJOINING_TYPE, oldDisjoiningType, this.disjoiningType));
            }
        }
        return this.disjoiningType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Type basicGetDisjoiningType() {
        return this.disjoiningType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setDisjoiningType(Type newDisjoiningType) {
        Type oldDisjoiningType = this.disjoiningType;
        this.disjoiningType = newDisjoiningType;
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.DISJOINING__DISJOINING_TYPE, oldDisjoiningType, this.disjoiningType));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Type getOwningType() {
        Type owningType = this.basicGetOwningType();
        return owningType != null && owningType.eIsProxy() ? (Type) this.eResolveProxy((InternalEObject) owningType) : owningType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Type basicGetOwningType() {
        // TODO: implement this method to return the 'Owning Type' reference
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
    public Type getTypeDisjoined() {
        if (this.typeDisjoined != null && this.typeDisjoined.eIsProxy()) {
            InternalEObject oldTypeDisjoined = (InternalEObject) this.typeDisjoined;
            this.typeDisjoined = (Type) this.eResolveProxy(oldTypeDisjoined);
            if (this.typeDisjoined != oldTypeDisjoined) {
                if (this.eNotificationRequired())
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.DISJOINING__TYPE_DISJOINED, oldTypeDisjoined, this.typeDisjoined));
            }
        }
        return this.typeDisjoined;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Type basicGetTypeDisjoined() {
        return this.typeDisjoined;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setTypeDisjoined(Type newTypeDisjoined) {
        Type oldTypeDisjoined = this.typeDisjoined;
        this.typeDisjoined = newTypeDisjoined;
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.DISJOINING__TYPE_DISJOINED, oldTypeDisjoined, this.typeDisjoined));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.DISJOINING__DISJOINING_TYPE:
                if (resolve)
                    return this.getDisjoiningType();
                return this.basicGetDisjoiningType();
            case SysmlPackage.DISJOINING__OWNING_TYPE:
                if (resolve)
                    return this.getOwningType();
                return this.basicGetOwningType();
            case SysmlPackage.DISJOINING__TYPE_DISJOINED:
                if (resolve)
                    return this.getTypeDisjoined();
                return this.basicGetTypeDisjoined();
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
            case SysmlPackage.DISJOINING__DISJOINING_TYPE:
                this.setDisjoiningType((Type) newValue);
                return;
            case SysmlPackage.DISJOINING__TYPE_DISJOINED:
                this.setTypeDisjoined((Type) newValue);
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
            case SysmlPackage.DISJOINING__DISJOINING_TYPE:
                this.setDisjoiningType((Type) null);
                return;
            case SysmlPackage.DISJOINING__TYPE_DISJOINED:
                this.setTypeDisjoined((Type) null);
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
            case SysmlPackage.DISJOINING__DISJOINING_TYPE:
                return this.disjoiningType != null;
            case SysmlPackage.DISJOINING__OWNING_TYPE:
                return this.basicGetOwningType() != null;
            case SysmlPackage.DISJOINING__TYPE_DISJOINED:
                return this.typeDisjoined != null;
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
        EList<Element> targets = new EObjectEList.Unsettable<>(Element.class, this, SysmlPackage.DISJOINING__TARGET) {
            @Override
            public boolean addAll(Collection<? extends Element> collection) {
                if (collection != null) {
                    Iterator<? extends Element> iterator = collection.iterator();
                    if (iterator.hasNext()) {
                        Element next = iterator.next();
                        if (next instanceof Type type) {
                            DisjoiningImpl.this.setDisjoiningType(type);
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
        Type disjoiningType = this.getDisjoiningType();
        if (disjoiningType != null) {
            targets.add(disjoiningType);
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
        EList<Element> sources = new EObjectEList.Unsettable<>(Element.class, this, SysmlPackage.DISJOINING__SOURCE) {
            @Override
            public boolean addAll(Collection<? extends Element> collection) {
                if (collection != null) {
                    Iterator<? extends Element> iterator = collection.iterator();
                    if (iterator.hasNext()) {
                        Element next = iterator.next();
                        if (next instanceof Type type) {
                            DisjoiningImpl.this.setTypeDisjoined(type);
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
        Type typeDisjoined = this.getTypeDisjoined();
        if (typeDisjoined != null) {
            sources.add(typeDisjoined);
        }
        return sources;
    }

} // DisjoiningImpl
