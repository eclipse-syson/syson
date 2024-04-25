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

import java.util.Optional;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.LiteralExpression;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Feature Value</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureValueImpl#isIsDefault <em>Is Default</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureValueImpl#isIsInitial <em>Is Initial</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureValueImpl#getFeatureWithValue <em>Feature With Value</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FeatureValueImpl#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FeatureValueImpl extends OwningMembershipImpl implements FeatureValue {
    /**
     * The default value of the '{@link #isIsDefault() <em>Is Default</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsDefault()
     * @generated
     * @ordered
     */
    protected static final boolean IS_DEFAULT_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsDefault() <em>Is Default</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsDefault()
     * @generated
     * @ordered
     */
    protected boolean isDefault = IS_DEFAULT_EDEFAULT;

    /**
     * The default value of the '{@link #isIsInitial() <em>Is Initial</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsInitial()
     * @generated
     * @ordered
     */
    protected static final boolean IS_INITIAL_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsInitial() <em>Is Initial</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsInitial()
     * @generated
     * @ordered
     */
    protected boolean isInitial = IS_INITIAL_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected FeatureValueImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getFeatureValue();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Feature getFeatureWithValue() {
        Feature featureWithValue = basicGetFeatureWithValue();
        return featureWithValue != null && featureWithValue.eIsProxy() ? (Feature)eResolveProxy((InternalEObject)featureWithValue) : featureWithValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Feature basicGetFeatureWithValue() {
        // TODO: implement this method to return the 'Feature With Value' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isIsDefault() {
        return isDefault;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setIsDefault(boolean newIsDefault) {
        boolean oldIsDefault = isDefault;
        isDefault = newIsDefault;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE_VALUE__IS_DEFAULT, oldIsDefault, isDefault));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isIsInitial() {
        return isInitial;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setIsInitial(boolean newIsInitial) {
        boolean oldIsInitial = isInitial;
        isInitial = newIsInitial;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FEATURE_VALUE__IS_INITIAL, oldIsInitial, isInitial));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getValue() {
        Expression value = basicGetValue();
        return value != null && value.eIsProxy() ? (Expression)eResolveProxy((InternalEObject)value) : value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.FEATURE_VALUE__IS_DEFAULT:
                return isIsDefault();
            case SysmlPackage.FEATURE_VALUE__IS_INITIAL:
                return isIsInitial();
            case SysmlPackage.FEATURE_VALUE__FEATURE_WITH_VALUE:
                if (resolve) return getFeatureWithValue();
                return basicGetFeatureWithValue();
            case SysmlPackage.FEATURE_VALUE__VALUE:
                if (resolve) return getValue();
                return basicGetValue();
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
            case SysmlPackage.FEATURE_VALUE__IS_DEFAULT:
                setIsDefault((Boolean)newValue);
                return;
            case SysmlPackage.FEATURE_VALUE__IS_INITIAL:
                setIsInitial((Boolean)newValue);
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
            case SysmlPackage.FEATURE_VALUE__IS_DEFAULT:
                setIsDefault(IS_DEFAULT_EDEFAULT);
                return;
            case SysmlPackage.FEATURE_VALUE__IS_INITIAL:
                setIsInitial(IS_INITIAL_EDEFAULT);
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
            case SysmlPackage.FEATURE_VALUE__IS_DEFAULT:
                return isDefault != IS_DEFAULT_EDEFAULT;
            case SysmlPackage.FEATURE_VALUE__IS_INITIAL:
                return isInitial != IS_INITIAL_EDEFAULT;
            case SysmlPackage.FEATURE_VALUE__FEATURE_WITH_VALUE:
                return basicGetFeatureWithValue() != null;
            case SysmlPackage.FEATURE_VALUE__VALUE:
                return basicGetValue() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (isDefault: ");
        result.append(isDefault);
        result.append(", isInitial: ");
        result.append(isInitial);
        result.append(')');
        return result.toString();
    }

} //FeatureValueImpl
