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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.syson.sysml.Classifier;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Subclassification</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.SubclassificationImpl#getOwningClassifier <em>Owning Classifier</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.SubclassificationImpl#getSubclassifier <em>Subclassifier</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.SubclassificationImpl#getSuperclassifier <em>Superclassifier</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SubclassificationImpl extends SpecializationImpl implements Subclassification {
    /**
     * The cached value of the '{@link #getSubclassifier() <em>Subclassifier</em>}' reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getSubclassifier()
     * @generated
     * @ordered
     */
    protected Classifier subclassifier;

    /**
     * The cached value of the '{@link #getSuperclassifier() <em>Superclassifier</em>}' reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @see #getSuperclassifier()
     * @generated
     * @ordered
     */
    protected Classifier superclassifier;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected SubclassificationImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getSubclassification();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Classifier getOwningClassifier() {
        Classifier owningClassifier = this.basicGetOwningClassifier();
        return owningClassifier != null && owningClassifier.eIsProxy() ? (Classifier) this.eResolveProxy((InternalEObject) owningClassifier) : owningClassifier;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Classifier basicGetOwningClassifier() {
        return this.getSubclassifier();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Classifier getSubclassifier() {
        if (this.subclassifier != null && this.subclassifier.eIsProxy()) {
            InternalEObject oldSubclassifier = (InternalEObject) this.subclassifier;
            this.subclassifier = (Classifier) this.eResolveProxy(oldSubclassifier);
            if (this.subclassifier != oldSubclassifier) {
                if (this.eNotificationRequired()) {
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.SUBCLASSIFICATION__SUBCLASSIFIER, oldSubclassifier, this.subclassifier));
                }
            }
        }
        return this.subclassifier;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Classifier basicGetSubclassifier() {
        return this.subclassifier;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setSubclassifier(Classifier newSubclassifier) {
        Classifier oldSubclassifier = this.subclassifier;
        this.subclassifier = newSubclassifier;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.SUBCLASSIFICATION__SUBCLASSIFIER, oldSubclassifier, this.subclassifier));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Classifier getSuperclassifier() {
        if (this.superclassifier != null && this.superclassifier.eIsProxy()) {
            InternalEObject oldSuperclassifier = (InternalEObject) this.superclassifier;
            this.superclassifier = (Classifier) this.eResolveProxy(oldSuperclassifier);
            if (this.superclassifier != oldSuperclassifier) {
                if (this.eNotificationRequired()) {
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.SUBCLASSIFICATION__SUPERCLASSIFIER, oldSuperclassifier, this.superclassifier));
                }
            }
        }
        return this.superclassifier;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Classifier basicGetSuperclassifier() {
        return this.superclassifier;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setSuperclassifier(Classifier newSuperclassifier) {
        Classifier oldSuperclassifier = this.superclassifier;
        this.superclassifier = newSuperclassifier;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.SUBCLASSIFICATION__SUPERCLASSIFIER, oldSuperclassifier, this.superclassifier));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.SUBCLASSIFICATION__OWNING_CLASSIFIER:
                if (resolve) {
                    return this.getOwningClassifier();
                }
                return this.basicGetOwningClassifier();
            case SysmlPackage.SUBCLASSIFICATION__SUBCLASSIFIER:
                if (resolve) {
                    return this.getSubclassifier();
                }
                return this.basicGetSubclassifier();
            case SysmlPackage.SUBCLASSIFICATION__SUPERCLASSIFIER:
                if (resolve) {
                    return this.getSuperclassifier();
                }
                return this.basicGetSuperclassifier();
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
            case SysmlPackage.SUBCLASSIFICATION__SUBCLASSIFIER:
                this.setSubclassifier((Classifier) newValue);
                return;
            case SysmlPackage.SUBCLASSIFICATION__SUPERCLASSIFIER:
                this.setSuperclassifier((Classifier) newValue);
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
            case SysmlPackage.SUBCLASSIFICATION__SUBCLASSIFIER:
                this.setSubclassifier((Classifier) null);
                return;
            case SysmlPackage.SUBCLASSIFICATION__SUPERCLASSIFIER:
                this.setSuperclassifier((Classifier) null);
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
            case SysmlPackage.SUBCLASSIFICATION__OWNING_CLASSIFIER:
                return this.basicGetOwningClassifier() != null;
            case SysmlPackage.SUBCLASSIFICATION__SUBCLASSIFIER:
                return this.subclassifier != null;
            case SysmlPackage.SUBCLASSIFICATION__SUPERCLASSIFIER:
                return this.superclassifier != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Type getOwningType() {
        return this.getOwningClassifier();
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Type getSpecific() {
        return this.getSubclassifier();
    }

    /**
     * <!-- begin-user-doc --> Redefines setter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void setSpecific(Type newSpecific) {
        if (newSpecific instanceof Classifier newSpecificClassifier) {
            this.setSubclassifier(newSpecificClassifier);
        }
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Type getGeneral() {
        return this.getSuperclassifier();
    }

    /**
     * <!-- begin-user-doc --> Redefines setter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void setGeneral(Type newGeneral) {
        if (newGeneral instanceof Classifier newGeneralClassifier) {
            this.setSuperclassifier(newGeneralClassifier);
        }
    }

} // SubclassificationImpl
