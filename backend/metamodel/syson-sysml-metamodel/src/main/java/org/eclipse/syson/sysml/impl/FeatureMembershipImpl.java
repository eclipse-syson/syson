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
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.Featuring;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Feature Membership</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureMembershipImpl#getFeature <em>Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureMembershipImpl#getType <em>Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureMembershipImpl#getOwnedMemberFeature <em>Owned Member
 * Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureMembershipImpl#getOwningType <em>Owning Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FeatureMembershipImpl extends OwningMembershipImpl implements FeatureMembership {
    /**
     * The cached value of the '{@link #getFeature() <em>Feature</em>}' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getFeature()
     * @generated
     * @ordered
     */
    protected Feature feature;

    /**
     * The cached value of the '{@link #getType() <em>Type</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @see #getType()
     * @generated
     * @ordered
     */
    protected Type type;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected FeatureMembershipImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getFeatureMembership();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Feature basicGetFeature() {
        return this.getFeature();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void setFeature(Feature newFeature) {
        // It redefines "feature" feature from Featuring
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Type getType() {
        if (this.type != null && this.type.eIsProxy()) {
            InternalEObject oldType = (InternalEObject) this.type;
            this.type = (Type) this.eResolveProxy(oldType);
            if (this.type != oldType) {
                if (this.eNotificationRequired()) {
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.FEATURE_MEMBERSHIP__TYPE, oldType, this.type));
                }
            }
        }
        return this.type;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Type basicGetType() {
        Type owningType = this.getOwningType();
        if (owningType != null) {
            return owningType;
        }
        return this.type;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setType(Type newType) {
        Type oldType = this.type;
        this.type = newType;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE_MEMBERSHIP__TYPE, oldType, this.type));
        }
    }

    /**
     * <!-- begin-user-doc --> The Feature that this FeatureMembership relates to its owningType, making it an
     * ownedFeature of the owningType. <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature getOwnedMemberFeature() {
        Feature ownedMemberFeature = this.basicGetOwnedMemberFeature();
        return ownedMemberFeature != null && ownedMemberFeature.eIsProxy() ? (Feature) this.eResolveProxy((InternalEObject) ownedMemberFeature) : ownedMemberFeature;
    }

    /**
     * <!-- begin-user-doc --> The Feature that this FeatureMembership relates to its owningType, making it an
     * ownedFeature of the owningType. <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Feature basicGetOwnedMemberFeature() {
        return this.getOwnedRelatedElement().stream()
                .filter(Feature.class::isInstance)
                .map(Feature.class::cast)
                .findFirst()
                .orElse(null);
    }

    /**
     * <!-- begin-user-doc --> The Type that owns this FeatureMembership. <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Type getOwningType() {
        Type owningType = this.basicGetOwningType();
        return owningType != null && owningType.eIsProxy() ? (Type) this.eResolveProxy((InternalEObject) owningType) : owningType;
    }

    /**
     * <!-- begin-user-doc --> The Type that owns this FeatureMembership. <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Type basicGetOwningType() {
        InternalEObject container = this.eContainer;
        if (container instanceof Type ty) {
            return ty;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public String getMemberName() {
        return this.getOwnedMemberName();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public String getMemberShortName() {
        return this.getOwnedMemberShortName();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.FEATURE_MEMBERSHIP__FEATURE:
                if (resolve) {
                    return this.getFeature();
                }
                return this.basicGetFeature();
            case SysmlPackage.FEATURE_MEMBERSHIP__TYPE:
                if (resolve) {
                    return this.getType();
                }
                return this.basicGetType();
            case SysmlPackage.FEATURE_MEMBERSHIP__OWNED_MEMBER_FEATURE:
                if (resolve) {
                    return this.getOwnedMemberFeature();
                }
                return this.basicGetOwnedMemberFeature();
            case SysmlPackage.FEATURE_MEMBERSHIP__OWNING_TYPE:
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
            case SysmlPackage.FEATURE_MEMBERSHIP__FEATURE:
                this.setFeature((Feature) newValue);
                return;
            case SysmlPackage.FEATURE_MEMBERSHIP__TYPE:
                this.setType((Type) newValue);
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
            case SysmlPackage.FEATURE_MEMBERSHIP__FEATURE:
                this.setFeature((Feature) null);
                return;
            case SysmlPackage.FEATURE_MEMBERSHIP__TYPE:
                this.setType((Type) null);
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
            case SysmlPackage.FEATURE_MEMBERSHIP__FEATURE:
                return this.feature != null;
            case SysmlPackage.FEATURE_MEMBERSHIP__TYPE:
                return this.type != null;
            case SysmlPackage.FEATURE_MEMBERSHIP__OWNED_MEMBER_FEATURE:
                return this.basicGetOwnedMemberFeature() != null;
            case SysmlPackage.FEATURE_MEMBERSHIP__OWNING_TYPE:
                return this.basicGetOwningType() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
        if (baseClass == Featuring.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.FEATURE_MEMBERSHIP__FEATURE:
                    return SysmlPackage.FEATURING__FEATURE;
                case SysmlPackage.FEATURE_MEMBERSHIP__TYPE:
                    return SysmlPackage.FEATURING__TYPE;
                default:
                    return -1;
            }
        }
        return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
        if (baseClass == Featuring.class) {
            switch (baseFeatureID) {
                case SysmlPackage.FEATURING__FEATURE:
                    return SysmlPackage.FEATURE_MEMBERSHIP__FEATURE;
                case SysmlPackage.FEATURING__TYPE:
                    return SysmlPackage.FEATURE_MEMBERSHIP__TYPE;
                default:
                    return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Element getOwnedMemberElement() {
        return this.getOwnedMemberFeature();
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Namespace getMembershipOwningNamespace() {
        return this.getOwningType();
    }

     /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
     @Override
     public Feature getFeature() {
         return this.getOwnedMemberFeature();
     }

} // FeatureMembershipImpl
