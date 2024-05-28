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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Step;
import org.eclipse.syson.sysml.Succession;
import org.eclipse.syson.sysml.SuccessionAsUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Succession As Usage</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.SuccessionAsUsageImpl#getEffectStep <em>Effect Step</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.SuccessionAsUsageImpl#getGuardExpression <em>Guard Expression</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.SuccessionAsUsageImpl#getTransitionStep <em>Transition Step</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.SuccessionAsUsageImpl#getTriggerStep <em>Trigger Step</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SuccessionAsUsageImpl extends ConnectorAsUsageImpl implements SuccessionAsUsage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected SuccessionAsUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getSuccessionAsUsage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Step> getEffectStep() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getSuccession_EffectStep(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Expression> getGuardExpression() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getSuccession_GuardExpression(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Step getTransitionStep() {
        Step transitionStep = this.basicGetTransitionStep();
        return transitionStep != null && transitionStep.eIsProxy() ? (Step) this.eResolveProxy((InternalEObject) transitionStep) : transitionStep;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Step basicGetTransitionStep() {
        // TODO: implement this method to return the 'Transition Step' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Step> getTriggerStep() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getSuccession_TriggerStep(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.SUCCESSION_AS_USAGE__EFFECT_STEP:
                return this.getEffectStep();
            case SysmlPackage.SUCCESSION_AS_USAGE__GUARD_EXPRESSION:
                return this.getGuardExpression();
            case SysmlPackage.SUCCESSION_AS_USAGE__TRANSITION_STEP:
                if (resolve)
                    return this.getTransitionStep();
                return this.basicGetTransitionStep();
            case SysmlPackage.SUCCESSION_AS_USAGE__TRIGGER_STEP:
                return this.getTriggerStep();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.SUCCESSION_AS_USAGE__EFFECT_STEP:
                return !this.getEffectStep().isEmpty();
            case SysmlPackage.SUCCESSION_AS_USAGE__GUARD_EXPRESSION:
                return !this.getGuardExpression().isEmpty();
            case SysmlPackage.SUCCESSION_AS_USAGE__TRANSITION_STEP:
                return this.basicGetTransitionStep() != null;
            case SysmlPackage.SUCCESSION_AS_USAGE__TRIGGER_STEP:
                return !this.getTriggerStep().isEmpty();
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
        if (baseClass == Succession.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.SUCCESSION_AS_USAGE__EFFECT_STEP:
                    return SysmlPackage.SUCCESSION__EFFECT_STEP;
                case SysmlPackage.SUCCESSION_AS_USAGE__GUARD_EXPRESSION:
                    return SysmlPackage.SUCCESSION__GUARD_EXPRESSION;
                case SysmlPackage.SUCCESSION_AS_USAGE__TRANSITION_STEP:
                    return SysmlPackage.SUCCESSION__TRANSITION_STEP;
                case SysmlPackage.SUCCESSION_AS_USAGE__TRIGGER_STEP:
                    return SysmlPackage.SUCCESSION__TRIGGER_STEP;
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
        if (baseClass == Succession.class) {
            switch (baseFeatureID) {
                case SysmlPackage.SUCCESSION__EFFECT_STEP:
                    return SysmlPackage.SUCCESSION_AS_USAGE__EFFECT_STEP;
                case SysmlPackage.SUCCESSION__GUARD_EXPRESSION:
                    return SysmlPackage.SUCCESSION_AS_USAGE__GUARD_EXPRESSION;
                case SysmlPackage.SUCCESSION__TRANSITION_STEP:
                    return SysmlPackage.SUCCESSION_AS_USAGE__TRANSITION_STEP;
                case SysmlPackage.SUCCESSION__TRIGGER_STEP:
                    return SysmlPackage.SUCCESSION_AS_USAGE__TRIGGER_STEP;
                default:
                    return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
    }

} // SuccessionAsUsageImpl
