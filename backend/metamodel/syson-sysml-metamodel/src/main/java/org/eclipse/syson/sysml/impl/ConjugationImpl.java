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
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.Conjugation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Conjugation</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.ConjugationImpl#getConjugatedType <em>Conjugated Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConjugationImpl#getOriginalType <em>Original Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConjugationImpl#getOwningType <em>Owning Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConjugationImpl extends RelationshipImpl implements Conjugation {
    /**
     * The cached value of the '{@link #getConjugatedType() <em>Conjugated Type</em>}' reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @see #getConjugatedType()
     * @generated
     * @ordered
     */
    protected Type conjugatedType;

    /**
     * The cached value of the '{@link #getOriginalType() <em>Original Type</em>}' reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getOriginalType()
     * @generated
     * @ordered
     */
    protected Type originalType;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected ConjugationImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getConjugation();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Type getConjugatedType() {
        if (this.conjugatedType != null && this.conjugatedType.eIsProxy()) {
            InternalEObject oldConjugatedType = (InternalEObject) this.conjugatedType;
            this.conjugatedType = (Type) this.eResolveProxy(oldConjugatedType);
            if (this.conjugatedType != oldConjugatedType) {
                if (this.eNotificationRequired()) {
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.CONJUGATION__CONJUGATED_TYPE, oldConjugatedType, this.conjugatedType));
                }
            }
        }
        return this.conjugatedType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Type basicGetConjugatedType() {
        return this.conjugatedType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setConjugatedType(Type newConjugatedType) {
        Type oldConjugatedType = this.conjugatedType;
        this.conjugatedType = newConjugatedType;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.CONJUGATION__CONJUGATED_TYPE, oldConjugatedType, this.conjugatedType));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Type getOriginalType() {
        if (this.originalType != null && this.originalType.eIsProxy()) {
            InternalEObject oldOriginalType = (InternalEObject) this.originalType;
            this.originalType = (Type) this.eResolveProxy(oldOriginalType);
            if (this.originalType != oldOriginalType) {
                if (this.eNotificationRequired()) {
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.CONJUGATION__ORIGINAL_TYPE, oldOriginalType, this.originalType));
                }
            }
        }
        return this.originalType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Type basicGetOriginalType() {
        return this.originalType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setOriginalType(Type newOriginalType) {
        Type oldOriginalType = this.originalType;
        this.originalType = newOriginalType;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.CONJUGATION__ORIGINAL_TYPE, oldOriginalType, this.originalType));
        }
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
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.CONJUGATION__CONJUGATED_TYPE:
                if (resolve) {
                    return this.getConjugatedType();
                }
                return this.basicGetConjugatedType();
            case SysmlPackage.CONJUGATION__ORIGINAL_TYPE:
                if (resolve) {
                    return this.getOriginalType();
                }
                return this.basicGetOriginalType();
            case SysmlPackage.CONJUGATION__OWNING_TYPE:
                if (resolve) {
                    return this.getOwningType();
                }
                return this.basicGetOwningType();
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
            case SysmlPackage.CONJUGATION__CONJUGATED_TYPE:
                this.setConjugatedType((Type) newValue);
                return;
            case SysmlPackage.CONJUGATION__ORIGINAL_TYPE:
                this.setOriginalType((Type) newValue);
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
            case SysmlPackage.CONJUGATION__CONJUGATED_TYPE:
                this.setConjugatedType((Type) null);
                return;
            case SysmlPackage.CONJUGATION__ORIGINAL_TYPE:
                this.setOriginalType((Type) null);
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
            case SysmlPackage.CONJUGATION__CONJUGATED_TYPE:
                return this.conjugatedType != null;
            case SysmlPackage.CONJUGATION__ORIGINAL_TYPE:
                return this.originalType != null;
            case SysmlPackage.CONJUGATION__OWNING_TYPE:
                return this.basicGetOwningType() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Element> getSource() {
        EList<Element> sources = new BasicEList<>();
        Type conjugatedType = this.getConjugatedType();
        if (conjugatedType != null) {
            sources.add(conjugatedType);
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getRelationship_Source(), sources.size(), sources.toArray());
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Element> getTarget() {
        EList<Element> targets = new BasicEList<>();
        Type originalType = this.getOriginalType();
        if (originalType != null) {
            targets.add(originalType);
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getRelationship_Target(), targets.size(), targets.toArray());
    }

} // ConjugationImpl
