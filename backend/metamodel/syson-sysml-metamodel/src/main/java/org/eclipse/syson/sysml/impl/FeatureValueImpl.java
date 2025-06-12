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
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Feature Value</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureValueImpl#isIsDefault <em>Is Default</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureValueImpl#isIsInitial <em>Is Initial</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureValueImpl#getFeatureWithValue <em>Feature With Value</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FeatureValueImpl#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FeatureValueImpl extends OwningMembershipImpl implements FeatureValue {
    /**
     * The default value of the '{@link #isIsDefault() <em>Is Default</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsDefault()
     * @generated
     * @ordered
     */
    protected static final boolean IS_DEFAULT_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsDefault() <em>Is Default</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsDefault()
     * @generated
     * @ordered
     */
    protected boolean isDefault = IS_DEFAULT_EDEFAULT;

    /**
     * The default value of the '{@link #isIsInitial() <em>Is Initial</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsInitial()
     * @generated
     * @ordered
     */
    protected static final boolean IS_INITIAL_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsInitial() <em>Is Initial</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsInitial()
     * @generated
     * @ordered
     */
    protected boolean isInitial = IS_INITIAL_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected FeatureValueImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getFeatureValue();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature getFeatureWithValue() {
        Feature featureWithValue = this.basicGetFeatureWithValue();
        return featureWithValue != null && featureWithValue.eIsProxy() ? (Feature) this.eResolveProxy((InternalEObject) featureWithValue) : featureWithValue;
    }

    /**
     * <!-- begin-user-doc -->The Feature to be provided a value. - {subsets membershipOwningNamespace} <!--
     * end-user-doc -->
     *
     * @generated NOT
     */
    public Feature basicGetFeatureWithValue() {
        Namespace membershipOwningName = this.getMembershipOwningNamespace();
        if (membershipOwningName instanceof Feature featureNamespace) {
            return featureNamespace;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean isIsDefault() {
        return this.isDefault;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIsDefault(boolean newIsDefault) {
        boolean oldIsDefault = this.isDefault;
        this.isDefault = newIsDefault;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE_VALUE__IS_DEFAULT, oldIsDefault, this.isDefault));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean isIsInitial() {
        return this.isInitial;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIsInitial(boolean newIsInitial) {
        boolean oldIsInitial = this.isInitial;
        this.isInitial = newIsInitial;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE_VALUE__IS_INITIAL, oldIsInitial, this.isInitial));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Expression getValue() {
        Expression value = this.basicGetValue();
        return value != null && value.eIsProxy() ? (Expression) this.eResolveProxy((InternalEObject) value) : value;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Expression basicGetValue() {
        return this.getOwnedRelatedElement().stream()
                .filter(Expression.class::isInstance)
                .map(Expression.class::cast)
                .findFirst()
                .orElse(null);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.FEATURE_VALUE__IS_DEFAULT:
                return this.isIsDefault();
            case SysmlPackage.FEATURE_VALUE__IS_INITIAL:
                return this.isIsInitial();
            case SysmlPackage.FEATURE_VALUE__FEATURE_WITH_VALUE:
                if (resolve) {
                    return this.getFeatureWithValue();
                }
                return this.basicGetFeatureWithValue();
            case SysmlPackage.FEATURE_VALUE__VALUE:
                if (resolve) {
                    return this.getValue();
                }
                return this.basicGetValue();
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
            case SysmlPackage.FEATURE_VALUE__IS_DEFAULT:
                this.setIsDefault((Boolean) newValue);
                return;
            case SysmlPackage.FEATURE_VALUE__IS_INITIAL:
                this.setIsInitial((Boolean) newValue);
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
            case SysmlPackage.FEATURE_VALUE__IS_DEFAULT:
                this.setIsDefault(IS_DEFAULT_EDEFAULT);
                return;
            case SysmlPackage.FEATURE_VALUE__IS_INITIAL:
                this.setIsInitial(IS_INITIAL_EDEFAULT);
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
            case SysmlPackage.FEATURE_VALUE__IS_DEFAULT:
                return this.isDefault != IS_DEFAULT_EDEFAULT;
            case SysmlPackage.FEATURE_VALUE__IS_INITIAL:
                return this.isInitial != IS_INITIAL_EDEFAULT;
            case SysmlPackage.FEATURE_VALUE__FEATURE_WITH_VALUE:
                return this.basicGetFeatureWithValue() != null;
            case SysmlPackage.FEATURE_VALUE__VALUE:
                return this.basicGetValue() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String toString() {
        if (this.eIsProxy()) {
            return super.toString();
        }

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (isDefault: ");
        result.append(this.isDefault);
        result.append(", isInitial: ");
        result.append(this.isInitial);
        result.append(')');
        return result.toString();
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Element getOwnedMemberElement() {
        return this.getValue();
    }

} // FeatureValueImpl
