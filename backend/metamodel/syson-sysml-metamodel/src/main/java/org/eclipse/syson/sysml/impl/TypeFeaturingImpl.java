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
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.TypeFeaturing;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Type Featuring</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.TypeFeaturingImpl#getFeatureOfType <em>Feature Of Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.TypeFeaturingImpl#getFeaturingType <em>Featuring Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.TypeFeaturingImpl#getOwningFeatureOfType <em>Owning Feature Of
 * Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TypeFeaturingImpl extends RelationshipImpl implements TypeFeaturing {
    /**
     * The cached value of the '{@link #getFeatureOfType() <em>Feature Of Type</em>}' reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getFeatureOfType()
     * @generated
     * @ordered
     */
    protected Feature featureOfType;

    /**
     * The cached value of the '{@link #getFeaturingType() <em>Featuring Type</em>}' reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getFeaturingType()
     * @generated
     * @ordered
     */
    protected Type featuringType;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected TypeFeaturingImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getTypeFeaturing();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature getFeatureOfType() {
        if (this.featureOfType != null && this.featureOfType.eIsProxy()) {
            InternalEObject oldFeatureOfType = (InternalEObject) this.featureOfType;
            this.featureOfType = (Feature) this.eResolveProxy(oldFeatureOfType);
            if (this.featureOfType != oldFeatureOfType) {
                if (this.eNotificationRequired()) {
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.TYPE_FEATURING__FEATURE_OF_TYPE, oldFeatureOfType, this.featureOfType));
                }
            }
        }
        return this.featureOfType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Feature basicGetFeatureOfType() {
        return this.featureOfType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setFeatureOfType(Feature newFeatureOfType) {
        Feature oldFeatureOfType = this.featureOfType;
        this.featureOfType = newFeatureOfType;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.TYPE_FEATURING__FEATURE_OF_TYPE, oldFeatureOfType, this.featureOfType));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Type getFeaturingType() {
        if (this.featuringType != null && this.featuringType.eIsProxy()) {
            InternalEObject oldFeaturingType = (InternalEObject) this.featuringType;
            this.featuringType = (Type) this.eResolveProxy(oldFeaturingType);
            if (this.featuringType != oldFeaturingType) {
                if (this.eNotificationRequired()) {
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.TYPE_FEATURING__FEATURING_TYPE, oldFeaturingType, this.featuringType));
                }
            }
        }
        return this.featuringType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Type basicGetFeaturingType() {
        return this.featuringType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setFeaturingType(Type newFeaturingType) {
        Type oldFeaturingType = this.featuringType;
        this.featuringType = newFeaturingType;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.TYPE_FEATURING__FEATURING_TYPE, oldFeaturingType, this.featuringType));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature getOwningFeatureOfType() {
        Feature owningFeatureOfType = this.basicGetOwningFeatureOfType();
        return owningFeatureOfType != null && owningFeatureOfType.eIsProxy() ? (Feature) this.eResolveProxy((InternalEObject) owningFeatureOfType) : owningFeatureOfType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Feature basicGetOwningFeatureOfType() {
        // TODO: implement this method to return the 'Owning Feature Of Type' reference
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
            case SysmlPackage.TYPE_FEATURING__FEATURE_OF_TYPE:
                if (resolve) {
                    return this.getFeatureOfType();
                }
                return this.basicGetFeatureOfType();
            case SysmlPackage.TYPE_FEATURING__FEATURING_TYPE:
                if (resolve) {
                    return this.getFeaturingType();
                }
                return this.basicGetFeaturingType();
            case SysmlPackage.TYPE_FEATURING__OWNING_FEATURE_OF_TYPE:
                if (resolve) {
                    return this.getOwningFeatureOfType();
                }
                return this.basicGetOwningFeatureOfType();
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
            case SysmlPackage.TYPE_FEATURING__FEATURE_OF_TYPE:
                this.setFeatureOfType((Feature) newValue);
                return;
            case SysmlPackage.TYPE_FEATURING__FEATURING_TYPE:
                this.setFeaturingType((Type) newValue);
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
            case SysmlPackage.TYPE_FEATURING__FEATURE_OF_TYPE:
                this.setFeatureOfType((Feature) null);
                return;
            case SysmlPackage.TYPE_FEATURING__FEATURING_TYPE:
                this.setFeaturingType((Type) null);
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
            case SysmlPackage.TYPE_FEATURING__FEATURE_OF_TYPE:
                return this.featureOfType != null;
            case SysmlPackage.TYPE_FEATURING__FEATURING_TYPE:
                return this.featuringType != null;
            case SysmlPackage.TYPE_FEATURING__OWNING_FEATURE_OF_TYPE:
                return this.basicGetOwningFeatureOfType() != null;
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
        EList<Element> sources = new EObjectEList.Unsettable<>(Element.class, this, SysmlPackage.TYPE_FEATURING__SOURCE) {
            @Override
            public boolean addAll(Collection<? extends Element> collection) {
                if (collection != null) {
                    Iterator<? extends Element> iterator = collection.iterator();
                    if (iterator.hasNext()) {
                        Element next = iterator.next();
                        if (next instanceof Feature feature) {
                            TypeFeaturingImpl.this.setFeatureOfType(feature);
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
        Feature featureOfType = this.getFeatureOfType();
        if (featureOfType != null) {
            sources.add(featureOfType);
        }
        return sources;
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Element> getTarget() {
        EList<Element> targets = new EObjectEList.Unsettable<>(Element.class, this, SysmlPackage.TYPE_FEATURING__TARGET) {
            @Override
            public boolean addAll(Collection<? extends Element> collection) {
                if (collection != null) {
                    Iterator<? extends Element> iterator = collection.iterator();
                    if (iterator.hasNext()) {
                        Element next = iterator.next();
                        if (next instanceof Type type) {
                            TypeFeaturingImpl.this.setFeaturingType(type);
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
        Type featuringType = this.getFeaturingType();
        if (featuringType != null) {
            targets.add(featuringType);
        }
        return targets;
    }
} // TypeFeaturingImpl
