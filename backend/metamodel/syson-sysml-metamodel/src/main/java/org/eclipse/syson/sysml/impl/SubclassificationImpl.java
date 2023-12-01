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
import org.eclipse.syson.sysml.Classifier;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Subclassification</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.SubclassificationImpl#getOwningClassifier <em>Owning Classifier</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.SubclassificationImpl#getSubclassifier <em>Subclassifier</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.SubclassificationImpl#getSuperclassifier <em>Superclassifier</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SubclassificationImpl extends SpecializationImpl implements Subclassification {
    /**
     * The cached value of the '{@link #getSubclassifier() <em>Subclassifier</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSubclassifier()
     * @generated
     * @ordered
     */
    protected Classifier subclassifier;

    /**
     * The cached value of the '{@link #getSuperclassifier() <em>Superclassifier</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSuperclassifier()
     * @generated
     * @ordered
     */
    protected Classifier superclassifier;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SubclassificationImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getSubclassification();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Classifier getOwningClassifier() {
        Classifier owningClassifier = basicGetOwningClassifier();
        return owningClassifier != null && owningClassifier.eIsProxy() ? (Classifier)eResolveProxy((InternalEObject)owningClassifier) : owningClassifier;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    public Classifier basicGetOwningClassifier() {
        return getSubclassifier();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Classifier getSubclassifier() {
        if (subclassifier != null && subclassifier.eIsProxy()) {
            InternalEObject oldSubclassifier = (InternalEObject)subclassifier;
            subclassifier = (Classifier)eResolveProxy(oldSubclassifier);
            if (subclassifier != oldSubclassifier) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.SUBCLASSIFICATION__SUBCLASSIFIER, oldSubclassifier, subclassifier));
            }
        }
        return subclassifier;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Classifier basicGetSubclassifier() {
        return subclassifier;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setSubclassifier(Classifier newSubclassifier) {
        Classifier oldSubclassifier = subclassifier;
        subclassifier = newSubclassifier;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.SUBCLASSIFICATION__SUBCLASSIFIER, oldSubclassifier, subclassifier));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Classifier getSuperclassifier() {
        if (superclassifier != null && superclassifier.eIsProxy()) {
            InternalEObject oldSuperclassifier = (InternalEObject)superclassifier;
            superclassifier = (Classifier)eResolveProxy(oldSuperclassifier);
            if (superclassifier != oldSuperclassifier) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.SUBCLASSIFICATION__SUPERCLASSIFIER, oldSuperclassifier, superclassifier));
            }
        }
        return superclassifier;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Classifier basicGetSuperclassifier() {
        return superclassifier;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setSuperclassifier(Classifier newSuperclassifier) {
        Classifier oldSuperclassifier = superclassifier;
        superclassifier = newSuperclassifier;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.SUBCLASSIFICATION__SUPERCLASSIFIER, oldSuperclassifier, superclassifier));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.SUBCLASSIFICATION__OWNING_CLASSIFIER:
                if (resolve) return getOwningClassifier();
                return basicGetOwningClassifier();
            case SysmlPackage.SUBCLASSIFICATION__SUBCLASSIFIER:
                if (resolve) return getSubclassifier();
                return basicGetSubclassifier();
            case SysmlPackage.SUBCLASSIFICATION__SUPERCLASSIFIER:
                if (resolve) return getSuperclassifier();
                return basicGetSuperclassifier();
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
            case SysmlPackage.SUBCLASSIFICATION__SUBCLASSIFIER:
                setSubclassifier((Classifier)newValue);
                return;
            case SysmlPackage.SUBCLASSIFICATION__SUPERCLASSIFIER:
                setSuperclassifier((Classifier)newValue);
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
            case SysmlPackage.SUBCLASSIFICATION__SUBCLASSIFIER:
                setSubclassifier((Classifier)null);
                return;
            case SysmlPackage.SUBCLASSIFICATION__SUPERCLASSIFIER:
                setSuperclassifier((Classifier)null);
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
            case SysmlPackage.SUBCLASSIFICATION__OWNING_CLASSIFIER:
                return basicGetOwningClassifier() != null;
            case SysmlPackage.SUBCLASSIFICATION__SUBCLASSIFIER:
                return subclassifier != null;
            case SysmlPackage.SUBCLASSIFICATION__SUPERCLASSIFIER:
                return superclassifier != null;
        }
        return super.eIsSet(featureID);
    }

} //SubclassificationImpl
