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
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureInverting;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Feature Inverting</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureInvertingImpl#getFeatureInverted <em>Feature Inverted</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureInvertingImpl#getInvertingFeature <em>Inverting Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureInvertingImpl#getOwningFeature <em>Owning Feature</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FeatureInvertingImpl extends RelationshipImpl implements FeatureInverting {
    /**
     * The cached value of the '{@link #getFeatureInverted() <em>Feature Inverted</em>}' reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @see #getFeatureInverted()
     * @generated
     * @ordered
     */
    protected Feature featureInverted;

    /**
     * The cached value of the '{@link #getInvertingFeature() <em>Inverting Feature</em>}' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getInvertingFeature()
     * @generated
     * @ordered
     */
    protected Feature invertingFeature;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected FeatureInvertingImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getFeatureInverting();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature getFeatureInverted() {
        if (this.featureInverted != null && this.featureInverted.eIsProxy()) {
            InternalEObject oldFeatureInverted = (InternalEObject) this.featureInverted;
            this.featureInverted = (Feature) this.eResolveProxy(oldFeatureInverted);
            if (this.featureInverted != oldFeatureInverted) {
                if (this.eNotificationRequired()) {
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.FEATURE_INVERTING__FEATURE_INVERTED, oldFeatureInverted, this.featureInverted));
                }
            }
        }
        return this.featureInverted;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Feature basicGetFeatureInverted() {
        return this.featureInverted;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setFeatureInverted(Feature newFeatureInverted) {
        Feature oldFeatureInverted = this.featureInverted;
        this.featureInverted = newFeatureInverted;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE_INVERTING__FEATURE_INVERTED, oldFeatureInverted, this.featureInverted));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature getInvertingFeature() {
        if (this.invertingFeature != null && this.invertingFeature.eIsProxy()) {
            InternalEObject oldInvertingFeature = (InternalEObject) this.invertingFeature;
            this.invertingFeature = (Feature) this.eResolveProxy(oldInvertingFeature);
            if (this.invertingFeature != oldInvertingFeature) {
                if (this.eNotificationRequired()) {
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.FEATURE_INVERTING__INVERTING_FEATURE, oldInvertingFeature, this.invertingFeature));
                }
            }
        }
        return this.invertingFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Feature basicGetInvertingFeature() {
        return this.invertingFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setInvertingFeature(Feature newInvertingFeature) {
        Feature oldInvertingFeature = this.invertingFeature;
        this.invertingFeature = newInvertingFeature;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE_INVERTING__INVERTING_FEATURE, oldInvertingFeature, this.invertingFeature));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature getOwningFeature() {
        Feature owningFeature = this.basicGetOwningFeature();
        return owningFeature != null && owningFeature.eIsProxy() ? (Feature) this.eResolveProxy((InternalEObject) owningFeature) : owningFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Feature basicGetOwningFeature() {
        // TODO: implement this method to return the 'Owning Feature' reference
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
            case SysmlPackage.FEATURE_INVERTING__FEATURE_INVERTED:
                if (resolve) {
                    return this.getFeatureInverted();
                }
                return this.basicGetFeatureInverted();
            case SysmlPackage.FEATURE_INVERTING__INVERTING_FEATURE:
                if (resolve) {
                    return this.getInvertingFeature();
                }
                return this.basicGetInvertingFeature();
            case SysmlPackage.FEATURE_INVERTING__OWNING_FEATURE:
                if (resolve) {
                    return this.getOwningFeature();
                }
                return this.basicGetOwningFeature();
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
            case SysmlPackage.FEATURE_INVERTING__FEATURE_INVERTED:
                this.setFeatureInverted((Feature) newValue);
                return;
            case SysmlPackage.FEATURE_INVERTING__INVERTING_FEATURE:
                this.setInvertingFeature((Feature) newValue);
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
            case SysmlPackage.FEATURE_INVERTING__FEATURE_INVERTED:
                this.setFeatureInverted((Feature) null);
                return;
            case SysmlPackage.FEATURE_INVERTING__INVERTING_FEATURE:
                this.setInvertingFeature((Feature) null);
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
            case SysmlPackage.FEATURE_INVERTING__FEATURE_INVERTED:
                return this.featureInverted != null;
            case SysmlPackage.FEATURE_INVERTING__INVERTING_FEATURE:
                return this.invertingFeature != null;
            case SysmlPackage.FEATURE_INVERTING__OWNING_FEATURE:
                return this.basicGetOwningFeature() != null;
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
        EList<Element> sources = new EObjectEList.Unsettable<>(Element.class, this, SysmlPackage.RELATIONSHIP__SOURCE) {
            @Override
            public boolean addAll(Collection<? extends Element> collection) {
                if (collection != null) {
                    Iterator<? extends Element> iterator = collection.iterator();
                    if (iterator.hasNext()) {
                        Element next = iterator.next();
                        if (next instanceof Feature feature) {
                            FeatureInvertingImpl.this.setFeatureInverted(feature);
                            return true;
                        }
                    }
                }
                return false;
            }
        };
        Feature featureInverted = this.getFeatureInverted();
        if (featureInverted != null) {
            sources.add(featureInverted);
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
        EList<Element> targets = new EObjectEList.Unsettable<>(Element.class, this, SysmlPackage.RELATIONSHIP__TARGET) {
            @Override
            public boolean addAll(Collection<? extends Element> collection) {
                if (collection != null) {
                    Iterator<? extends Element> iterator = collection.iterator();
                    if (iterator.hasNext()) {
                        Element next = iterator.next();
                        if (next instanceof Feature feature) {
                            FeatureInvertingImpl.this.setInvertingFeature(feature);
                            return true;
                        }
                    }
                }
                return false;
            }
        };
        Feature invertingFeature = this.getInvertingFeature();
        if (invertingFeature != null) {
            targets.add(invertingFeature);
        }
        return targets;
    }

} // FeatureInvertingImpl
