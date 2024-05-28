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
import org.eclipse.syson.sysml.CalculationDefinition;
import org.eclipse.syson.sysml.CalculationUsage;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Function;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Calculation Definition</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.CalculationDefinitionImpl#isIsModelLevelEvaluable <em>Is Model Level
 * Evaluable</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.CalculationDefinitionImpl#getExpression <em>Expression</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.CalculationDefinitionImpl#getResult <em>Result</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.CalculationDefinitionImpl#getCalculation <em>Calculation</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CalculationDefinitionImpl extends ActionDefinitionImpl implements CalculationDefinition {
    /**
     * The default value of the '{@link #isIsModelLevelEvaluable() <em>Is Model Level Evaluable</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #isIsModelLevelEvaluable()
     * @generated
     * @ordered
     */
    protected static final boolean IS_MODEL_LEVEL_EVALUABLE_EDEFAULT = false;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected CalculationDefinitionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getCalculationDefinition();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Expression> getExpression() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getFunction_Expression(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean isIsModelLevelEvaluable() {
        return false;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature getResult() {
        Feature result = this.basicGetResult();
        return result != null && result.eIsProxy() ? (Feature) this.eResolveProxy((InternalEObject) result) : result;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Feature basicGetResult() {
        // TODO: implement this method to return the 'Result' reference
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
    public EList<CalculationUsage> getCalculation() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getCalculationDefinition_Calculation(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.CALCULATION_DEFINITION__IS_MODEL_LEVEL_EVALUABLE:
                return this.isIsModelLevelEvaluable();
            case SysmlPackage.CALCULATION_DEFINITION__EXPRESSION:
                return this.getExpression();
            case SysmlPackage.CALCULATION_DEFINITION__RESULT:
                if (resolve)
                    return this.getResult();
                return this.basicGetResult();
            case SysmlPackage.CALCULATION_DEFINITION__CALCULATION:
                return this.getCalculation();
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
            case SysmlPackage.CALCULATION_DEFINITION__IS_MODEL_LEVEL_EVALUABLE:
                return this.isIsModelLevelEvaluable() != IS_MODEL_LEVEL_EVALUABLE_EDEFAULT;
            case SysmlPackage.CALCULATION_DEFINITION__EXPRESSION:
                return !this.getExpression().isEmpty();
            case SysmlPackage.CALCULATION_DEFINITION__RESULT:
                return this.basicGetResult() != null;
            case SysmlPackage.CALCULATION_DEFINITION__CALCULATION:
                return !this.getCalculation().isEmpty();
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
        if (baseClass == Function.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.CALCULATION_DEFINITION__IS_MODEL_LEVEL_EVALUABLE:
                    return SysmlPackage.FUNCTION__IS_MODEL_LEVEL_EVALUABLE;
                case SysmlPackage.CALCULATION_DEFINITION__EXPRESSION:
                    return SysmlPackage.FUNCTION__EXPRESSION;
                case SysmlPackage.CALCULATION_DEFINITION__RESULT:
                    return SysmlPackage.FUNCTION__RESULT;
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
        if (baseClass == Function.class) {
            switch (baseFeatureID) {
                case SysmlPackage.FUNCTION__IS_MODEL_LEVEL_EVALUABLE:
                    return SysmlPackage.CALCULATION_DEFINITION__IS_MODEL_LEVEL_EVALUABLE;
                case SysmlPackage.FUNCTION__EXPRESSION:
                    return SysmlPackage.CALCULATION_DEFINITION__EXPRESSION;
                case SysmlPackage.FUNCTION__RESULT:
                    return SysmlPackage.CALCULATION_DEFINITION__RESULT;
                default:
                    return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
    }

} // CalculationDefinitionImpl
