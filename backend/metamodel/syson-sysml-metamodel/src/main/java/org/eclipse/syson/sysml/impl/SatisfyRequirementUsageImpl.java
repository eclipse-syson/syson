/*******************************************************************************
* Copyright (c) 2023, 2026 Obeo.
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

import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.syson.sysml.AssertConstraintUsage;
import org.eclipse.syson.sysml.BindingConnector;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Invariant;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SatisfyRequirementUsage;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Satisfy Requirement Usage</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.SatisfyRequirementUsageImpl#isIsNegated <em>Is Negated</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.SatisfyRequirementUsageImpl#getAssertedConstraint <em>Asserted
 * Constraint</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.SatisfyRequirementUsageImpl#getSatisfiedRequirement <em>Satisfied
 * Requirement</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.SatisfyRequirementUsageImpl#getSatisfyingFeature <em>Satisfying
 * Feature</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SatisfyRequirementUsageImpl extends RequirementUsageImpl implements SatisfyRequirementUsage {
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
    protected SatisfyRequirementUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getSatisfyRequirementUsage();
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
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.SATISFY_REQUIREMENT_USAGE__IS_NEGATED, oldIsNegated, this.isNegated));
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
     * @generated NOT
     *
     */
    public ConstraintUsage basicGetAssertedConstraint() {
        return this.getSatisfiedRequirement();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public RequirementUsage getSatisfiedRequirement() {
        RequirementUsage satisfiedRequirement = this.basicGetSatisfiedRequirement();
        return satisfiedRequirement != null && satisfiedRequirement.eIsProxy() ? (RequirementUsage) this.eResolveProxy((InternalEObject) satisfiedRequirement) : satisfiedRequirement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public RequirementUsage basicGetSatisfiedRequirement() {
        RequirementUsage satisfiedRequirement = null;
        var referencedFeatureTarget = this.referencedFeatureTarget();
        if (referencedFeatureTarget == null) {
            satisfiedRequirement = this;
        } else if (referencedFeatureTarget instanceof RequirementUsage requirementUsage) {
            satisfiedRequirement = requirementUsage;
        }
        return satisfiedRequirement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature getSatisfyingFeature() {
        Feature satisfyingFeature = this.basicGetSatisfyingFeature();
        return satisfyingFeature != null && satisfyingFeature.eIsProxy() ? (Feature) this.eResolveProxy((InternalEObject) satisfyingFeature) : satisfyingFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Feature basicGetSatisfyingFeature() {
        Feature satisfyingFeature = null;

        // custom implementation, not compliant with deriveSatisfyRequirementUsageSatisfyingFeature
        // because deriveSatisfyRequirementUsageSatisfyingFeature does not use subjectMembership
        // https://issues.omg.org/issues/SYSML21-12 also shows there is a problem with this
        var subjectParameter = this.getSubjectParameter();
        if (subjectParameter != null) {
            satisfyingFeature = subjectParameter.getOwnedSubsetting().stream()
                    .findFirst()
                    .map(Subsetting::getSubsettedFeature)
                    .orElse(null);
        }
        // fallback to default implementation
        if (satisfyingFeature == null) {
            List<BindingConnector> bindings = this.getOwnedMember().stream()
                    .filter(BindingConnector.class::isInstance)
                    .map(BindingConnector.class::cast)
                    .filter(b -> b.getRelatedElement().contains(this.getSubjectParameter()))
                    .toList();

            if (bindings.isEmpty() || bindings.get(0).getRelatedElement().stream().anyMatch(r -> r != this.getSubjectParameter())) {
                satisfyingFeature = null;
            } else {
                satisfyingFeature = bindings.get(0).getRelatedElement().stream()
                        .filter(Feature.class::isInstance)
                        .map(Feature.class::cast)
                        .filter(r -> r != this.getSubjectParameter())
                        .findAny()
                        .orElse(null);
            }
        }
        return satisfyingFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.SATISFY_REQUIREMENT_USAGE__IS_NEGATED:
                return this.isIsNegated();
            case SysmlPackage.SATISFY_REQUIREMENT_USAGE__ASSERTED_CONSTRAINT:
                if (resolve) {
                    return this.getAssertedConstraint();
                }
                return this.basicGetAssertedConstraint();
            case SysmlPackage.SATISFY_REQUIREMENT_USAGE__SATISFIED_REQUIREMENT:
                if (resolve) {
                    return this.getSatisfiedRequirement();
                }
                return this.basicGetSatisfiedRequirement();
            case SysmlPackage.SATISFY_REQUIREMENT_USAGE__SATISFYING_FEATURE:
                if (resolve) {
                    return this.getSatisfyingFeature();
                }
                return this.basicGetSatisfyingFeature();
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
            case SysmlPackage.SATISFY_REQUIREMENT_USAGE__IS_NEGATED:
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
            case SysmlPackage.SATISFY_REQUIREMENT_USAGE__IS_NEGATED:
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
            case SysmlPackage.SATISFY_REQUIREMENT_USAGE__IS_NEGATED:
                return this.isNegated != IS_NEGATED_EDEFAULT;
            case SysmlPackage.SATISFY_REQUIREMENT_USAGE__ASSERTED_CONSTRAINT:
                return this.basicGetAssertedConstraint() != null;
            case SysmlPackage.SATISFY_REQUIREMENT_USAGE__SATISFIED_REQUIREMENT:
                return this.basicGetSatisfiedRequirement() != null;
            case SysmlPackage.SATISFY_REQUIREMENT_USAGE__SATISFYING_FEATURE:
                return this.basicGetSatisfyingFeature() != null;
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
                case SysmlPackage.SATISFY_REQUIREMENT_USAGE__IS_NEGATED:
                    return SysmlPackage.INVARIANT__IS_NEGATED;
                default:
                    return -1;
            }
        }
        if (baseClass == AssertConstraintUsage.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.SATISFY_REQUIREMENT_USAGE__ASSERTED_CONSTRAINT:
                    return SysmlPackage.ASSERT_CONSTRAINT_USAGE__ASSERTED_CONSTRAINT;
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
                    return SysmlPackage.SATISFY_REQUIREMENT_USAGE__IS_NEGATED;
                default:
                    return -1;
            }
        }
        if (baseClass == AssertConstraintUsage.class) {
            switch (baseFeatureID) {
                case SysmlPackage.ASSERT_CONSTRAINT_USAGE__ASSERTED_CONSTRAINT:
                    return SysmlPackage.SATISFY_REQUIREMENT_USAGE__ASSERTED_CONSTRAINT;
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

} // SatisfyRequirementUsageImpl
