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
import org.eclipse.syson.sysml.AssertConstraintUsage;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Invariant;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Assert Constraint Usage</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.AssertConstraintUsageImpl#isIsNegated <em>Is Negated</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.AssertConstraintUsageImpl#getAssertedConstraint <em>Asserted
 * Constraint</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AssertConstraintUsageImpl extends ConstraintUsageImpl implements AssertConstraintUsage {
    /**
     * The default value of the '{@link #isIsNegated() <em>Is Negated</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsNegated()
     * @generated
     * @ordered
     */
    protected static final boolean IS_NEGATED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsNegated() <em>Is Negated</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsNegated()
     * @generated
     * @ordered
     */
    protected boolean isNegated = IS_NEGATED_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected AssertConstraintUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getAssertConstraintUsage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean isIsNegated() {
        return this.isNegated;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIsNegated(boolean newIsNegated) {
        boolean oldIsNegated = this.isNegated;
        this.isNegated = newIsNegated;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.ASSERT_CONSTRAINT_USAGE__IS_NEGATED, oldIsNegated, this.isNegated));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ConstraintUsage getAssertedConstraint() {
        ConstraintUsage assertedConstraint = this.basicGetAssertedConstraint();
        return assertedConstraint != null && assertedConstraint.eIsProxy() ? (ConstraintUsage) this.eResolveProxy((InternalEObject) assertedConstraint) : assertedConstraint;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public ConstraintUsage basicGetAssertedConstraint() {
        // TODO: implement this method to return the 'Asserted Constraint' reference
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
            case SysmlPackage.ASSERT_CONSTRAINT_USAGE__IS_NEGATED:
                return this.isIsNegated();
            case SysmlPackage.ASSERT_CONSTRAINT_USAGE__ASSERTED_CONSTRAINT:
                if (resolve) {
                    return this.getAssertedConstraint();
                }
                return this.basicGetAssertedConstraint();
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
            case SysmlPackage.ASSERT_CONSTRAINT_USAGE__IS_NEGATED:
                this.setIsNegated((Boolean) newValue);
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
            case SysmlPackage.ASSERT_CONSTRAINT_USAGE__IS_NEGATED:
                this.setIsNegated(IS_NEGATED_EDEFAULT);
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
            case SysmlPackage.ASSERT_CONSTRAINT_USAGE__IS_NEGATED:
                return this.isNegated != IS_NEGATED_EDEFAULT;
            case SysmlPackage.ASSERT_CONSTRAINT_USAGE__ASSERTED_CONSTRAINT:
                return this.basicGetAssertedConstraint() != null;
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
        if (baseClass == Invariant.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.ASSERT_CONSTRAINT_USAGE__IS_NEGATED:
                    return SysmlPackage.INVARIANT__IS_NEGATED;
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
        if (baseClass == Invariant.class) {
            switch (baseFeatureID) {
                case SysmlPackage.INVARIANT__IS_NEGATED:
                    return SysmlPackage.ASSERT_CONSTRAINT_USAGE__IS_NEGATED;
                default:
                    return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
        result.append(" (isNegated: ");
        result.append(this.isNegated);
        result.append(')');
        return result.toString();
    }

} // AssertConstraintUsageImpl
