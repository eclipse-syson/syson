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
import org.eclipse.syson.sysml.AssertConstraintUsage;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Invariant;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SatisfyRequirementUsage;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Satisfy Requirement Usage</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.SatisfyRequirementUsageImpl#isIsNegated <em>Is Negated</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.SatisfyRequirementUsageImpl#getAssertedConstraint <em>Asserted Constraint</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.SatisfyRequirementUsageImpl#getSatisfiedRequirement <em>Satisfied Requirement</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.SatisfyRequirementUsageImpl#getSatisfyingFeature <em>Satisfying Feature</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SatisfyRequirementUsageImpl extends RequirementUsageImpl implements SatisfyRequirementUsage {
    /**
     * The default value of the '{@link #isIsNegated() <em>Is Negated</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsNegated()
     * @generated
     * @ordered
     */
    protected static final boolean IS_NEGATED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsNegated() <em>Is Negated</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsNegated()
     * @generated
     * @ordered
     */
    protected boolean isNegated = IS_NEGATED_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SatisfyRequirementUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getSatisfyRequirementUsage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isIsNegated() {
        return isNegated;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setIsNegated(boolean newIsNegated) {
        boolean oldIsNegated = isNegated;
        isNegated = newIsNegated;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.SATISFY_REQUIREMENT_USAGE__IS_NEGATED, oldIsNegated, isNegated));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ConstraintUsage getAssertedConstraint() {
        ConstraintUsage assertedConstraint = basicGetAssertedConstraint();
        return assertedConstraint != null && assertedConstraint.eIsProxy() ? (ConstraintUsage)eResolveProxy((InternalEObject)assertedConstraint) : assertedConstraint;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ConstraintUsage basicGetAssertedConstraint() {
        // TODO: implement this method to return the 'Asserted Constraint' reference
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
    public RequirementUsage getSatisfiedRequirement() {
        RequirementUsage satisfiedRequirement = basicGetSatisfiedRequirement();
        return satisfiedRequirement != null && satisfiedRequirement.eIsProxy() ? (RequirementUsage)eResolveProxy((InternalEObject)satisfiedRequirement) : satisfiedRequirement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RequirementUsage basicGetSatisfiedRequirement() {
        // TODO: implement this method to return the 'Satisfied Requirement' reference
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
    public Feature getSatisfyingFeature() {
        Feature satisfyingFeature = basicGetSatisfyingFeature();
        return satisfyingFeature != null && satisfyingFeature.eIsProxy() ? (Feature)eResolveProxy((InternalEObject)satisfyingFeature) : satisfyingFeature;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Feature basicGetSatisfyingFeature() {
        // TODO: implement this method to return the 'Satisfying Feature' reference
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
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.SATISFY_REQUIREMENT_USAGE__IS_NEGATED:
                return isIsNegated();
            case SysmlPackage.SATISFY_REQUIREMENT_USAGE__ASSERTED_CONSTRAINT:
                if (resolve) return getAssertedConstraint();
                return basicGetAssertedConstraint();
            case SysmlPackage.SATISFY_REQUIREMENT_USAGE__SATISFIED_REQUIREMENT:
                if (resolve) return getSatisfiedRequirement();
                return basicGetSatisfiedRequirement();
            case SysmlPackage.SATISFY_REQUIREMENT_USAGE__SATISFYING_FEATURE:
                if (resolve) return getSatisfyingFeature();
                return basicGetSatisfyingFeature();
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
            case SysmlPackage.SATISFY_REQUIREMENT_USAGE__IS_NEGATED:
                setIsNegated((Boolean)newValue);
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
            case SysmlPackage.SATISFY_REQUIREMENT_USAGE__IS_NEGATED:
                setIsNegated(IS_NEGATED_EDEFAULT);
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
            case SysmlPackage.SATISFY_REQUIREMENT_USAGE__IS_NEGATED:
                return isNegated != IS_NEGATED_EDEFAULT;
            case SysmlPackage.SATISFY_REQUIREMENT_USAGE__ASSERTED_CONSTRAINT:
                return basicGetAssertedConstraint() != null;
            case SysmlPackage.SATISFY_REQUIREMENT_USAGE__SATISFIED_REQUIREMENT:
                return basicGetSatisfiedRequirement() != null;
            case SysmlPackage.SATISFY_REQUIREMENT_USAGE__SATISFYING_FEATURE:
                return basicGetSatisfyingFeature() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
        if (baseClass == Invariant.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.SATISFY_REQUIREMENT_USAGE__IS_NEGATED: return SysmlPackage.INVARIANT__IS_NEGATED;
                default: return -1;
            }
        }
        if (baseClass == AssertConstraintUsage.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.SATISFY_REQUIREMENT_USAGE__ASSERTED_CONSTRAINT: return SysmlPackage.ASSERT_CONSTRAINT_USAGE__ASSERTED_CONSTRAINT;
                default: return -1;
            }
        }
        return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
        if (baseClass == Invariant.class) {
            switch (baseFeatureID) {
                case SysmlPackage.INVARIANT__IS_NEGATED: return SysmlPackage.SATISFY_REQUIREMENT_USAGE__IS_NEGATED;
                default: return -1;
            }
        }
        if (baseClass == AssertConstraintUsage.class) {
            switch (baseFeatureID) {
                case SysmlPackage.ASSERT_CONSTRAINT_USAGE__ASSERTED_CONSTRAINT: return SysmlPackage.SATISFY_REQUIREMENT_USAGE__ASSERTED_CONSTRAINT;
                default: return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
        result.append(" (isNegated: ");
        result.append(isNegated);
        result.append(')');
        return result.toString();
    }

} //SatisfyRequirementUsageImpl
