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
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Specialization</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.SpecializationImpl#getGeneral <em>General</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.SpecializationImpl#getOwningType <em>Owning Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.SpecializationImpl#getSpecific <em>Specific</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SpecializationImpl extends RelationshipImpl implements Specialization {
    /**
     * The cached value of the '{@link #getGeneral() <em>General</em>}' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getGeneral()
     * @generated
     * @ordered
     */
    protected Type general;

    /**
     * The cached value of the '{@link #getSpecific() <em>Specific</em>}' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getSpecific()
     * @generated
     * @ordered
     */
    protected Type specific;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected SpecializationImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getSpecialization();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Type getGeneral() {
        if (this.general != null && this.general.eIsProxy()) {
            InternalEObject oldGeneral = (InternalEObject) this.general;
            this.general = (Type) this.eResolveProxy(oldGeneral);
            if (this.general != oldGeneral) {
                if (this.eNotificationRequired())
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.SPECIALIZATION__GENERAL, oldGeneral, this.general));
            }
        }
        return this.general;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Type basicGetGeneral() {
        return this.general;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setGeneral(Type newGeneral) {
        Type oldGeneral = this.general;
        this.general = newGeneral;
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.SPECIALIZATION__GENERAL, oldGeneral, this.general));
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
    public Type getSpecific() {
        if (this.specific != null && this.specific.eIsProxy()) {
            InternalEObject oldSpecific = (InternalEObject) this.specific;
            this.specific = (Type) this.eResolveProxy(oldSpecific);
            if (this.specific != oldSpecific) {
                if (this.eNotificationRequired())
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.SPECIALIZATION__SPECIFIC, oldSpecific, this.specific));
            }
        }
        return this.specific;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Type basicGetSpecific() {
        return this.specific;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setSpecific(Type newSpecific) {
        Type oldSpecific = this.specific;
        this.specific = newSpecific;
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.SPECIALIZATION__SPECIFIC, oldSpecific, this.specific));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.SPECIALIZATION__GENERAL:
                if (resolve)
                    return this.getGeneral();
                return this.basicGetGeneral();
            case SysmlPackage.SPECIALIZATION__OWNING_TYPE:
                if (resolve)
                    return this.getOwningType();
                return this.basicGetOwningType();
            case SysmlPackage.SPECIALIZATION__SPECIFIC:
                if (resolve)
                    return this.getSpecific();
                return this.basicGetSpecific();
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
            case SysmlPackage.SPECIALIZATION__GENERAL:
                this.setGeneral((Type) newValue);
                return;
            case SysmlPackage.SPECIALIZATION__SPECIFIC:
                this.setSpecific((Type) newValue);
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
            case SysmlPackage.SPECIALIZATION__GENERAL:
                this.setGeneral((Type) null);
                return;
            case SysmlPackage.SPECIALIZATION__SPECIFIC:
                this.setSpecific((Type) null);
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
            case SysmlPackage.SPECIALIZATION__GENERAL:
                return this.general != null;
            case SysmlPackage.SPECIALIZATION__OWNING_TYPE:
                return this.basicGetOwningType() != null;
            case SysmlPackage.SPECIALIZATION__SPECIFIC:
                return this.specific != null;
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
        EList<Element> targets = new EObjectEList.Unsettable<>(Element.class, this, SysmlPackage.SPECIALIZATION__TARGET) {
            @Override
            public boolean addAll(Collection<? extends Element> collection) {
                if (collection != null) {
                    Iterator<? extends Element> iterator = collection.iterator();
                    if (iterator.hasNext()) {
                        Element next = iterator.next();
                        if (next instanceof Type type) {
                            SpecializationImpl.this.setGeneral(type);
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
        Type general = this.getGeneral();
        if (general != null) {
            targets.add(general);
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
        EList<Element> sources = new EObjectEList.Unsettable<>(Element.class, this, SysmlPackage.SPECIALIZATION__SOURCE) {
            @Override
            public boolean addAll(Collection<? extends Element> collection) {
                if (collection != null) {
                    Iterator<? extends Element> iterator = collection.iterator();
                    if (iterator.hasNext()) {
                        Element next = iterator.next();
                        if (next instanceof Type type) {
                            SpecializationImpl.this.setSpecific(type);
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
        Type specific = this.getSpecific();
        if (specific != null) {
            sources.add(specific);
        }
        return sources;
    }

} // SpecializationImpl
