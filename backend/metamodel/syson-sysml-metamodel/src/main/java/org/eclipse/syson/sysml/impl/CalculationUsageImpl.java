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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.CalculationUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Function;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Calculation Usage</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.CalculationUsageImpl#isIsModelLevelEvaluable <em>Is Model Level
 * Evaluable</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.CalculationUsageImpl#getFunction <em>Function</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.CalculationUsageImpl#getResult <em>Result</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.CalculationUsageImpl#getCalculationDefinition <em>Calculation
 * Definition</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CalculationUsageImpl extends ActionUsageImpl implements CalculationUsage {
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
    protected CalculationUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getCalculationUsage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Function getFunction() {
        Function function = this.basicGetFunction();
        return function != null && function.eIsProxy() ? (Function) this.eResolveProxy((InternalEObject) function) : function;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Function basicGetFunction() {
        // TODO: implement this method to return the 'Function' reference
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
     * @generated
     */
    @Override
    public Function getCalculationDefinition() {
        Function calculationDefinition = this.basicGetCalculationDefinition();
        return calculationDefinition != null && calculationDefinition.eIsProxy() ? (Function) this.eResolveProxy((InternalEObject) calculationDefinition) : calculationDefinition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Function basicGetCalculationDefinition() {
        // TODO: implement this method to return the 'Calculation Definition' reference
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
    public boolean checkCondition(Element target) {
        return false;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<Element> evaluate(Element target) {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean modelLevelEvaluable(EList<Feature> visited) {
        return false;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.CALCULATION_USAGE__IS_MODEL_LEVEL_EVALUABLE:
                return this.isIsModelLevelEvaluable();
            case SysmlPackage.CALCULATION_USAGE__FUNCTION:
                if (resolve)
                    return this.getFunction();
                return this.basicGetFunction();
            case SysmlPackage.CALCULATION_USAGE__RESULT:
                if (resolve)
                    return this.getResult();
                return this.basicGetResult();
            case SysmlPackage.CALCULATION_USAGE__CALCULATION_DEFINITION:
                if (resolve)
                    return this.getCalculationDefinition();
                return this.basicGetCalculationDefinition();
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
            case SysmlPackage.CALCULATION_USAGE__IS_MODEL_LEVEL_EVALUABLE:
                return this.isIsModelLevelEvaluable() != IS_MODEL_LEVEL_EVALUABLE_EDEFAULT;
            case SysmlPackage.CALCULATION_USAGE__FUNCTION:
                return this.basicGetFunction() != null;
            case SysmlPackage.CALCULATION_USAGE__RESULT:
                return this.basicGetResult() != null;
            case SysmlPackage.CALCULATION_USAGE__CALCULATION_DEFINITION:
                return this.basicGetCalculationDefinition() != null;
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
        if (baseClass == Expression.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.CALCULATION_USAGE__IS_MODEL_LEVEL_EVALUABLE:
                    return SysmlPackage.EXPRESSION__IS_MODEL_LEVEL_EVALUABLE;
                case SysmlPackage.CALCULATION_USAGE__FUNCTION:
                    return SysmlPackage.EXPRESSION__FUNCTION;
                case SysmlPackage.CALCULATION_USAGE__RESULT:
                    return SysmlPackage.EXPRESSION__RESULT;
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
        if (baseClass == Expression.class) {
            switch (baseFeatureID) {
                case SysmlPackage.EXPRESSION__IS_MODEL_LEVEL_EVALUABLE:
                    return SysmlPackage.CALCULATION_USAGE__IS_MODEL_LEVEL_EVALUABLE;
                case SysmlPackage.EXPRESSION__FUNCTION:
                    return SysmlPackage.CALCULATION_USAGE__FUNCTION;
                case SysmlPackage.EXPRESSION__RESULT:
                    return SysmlPackage.CALCULATION_USAGE__RESULT;
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
    public int eDerivedOperationID(int baseOperationID, Class<?> baseClass) {
        if (baseClass == Expression.class) {
            switch (baseOperationID) {
                case SysmlPackage.EXPRESSION___CHECK_CONDITION__ELEMENT:
                    return SysmlPackage.CALCULATION_USAGE___CHECK_CONDITION__ELEMENT;
                case SysmlPackage.EXPRESSION___EVALUATE__ELEMENT:
                    return SysmlPackage.CALCULATION_USAGE___EVALUATE__ELEMENT;
                case SysmlPackage.EXPRESSION___MODEL_LEVEL_EVALUABLE__ELIST:
                    return SysmlPackage.CALCULATION_USAGE___MODEL_LEVEL_EVALUABLE__ELIST;
                default:
                    return -1;
            }
        }
        return super.eDerivedOperationID(baseOperationID, baseClass);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
        switch (operationID) {
            case SysmlPackage.CALCULATION_USAGE___CHECK_CONDITION__ELEMENT:
                return this.checkCondition((Element) arguments.get(0));
            case SysmlPackage.CALCULATION_USAGE___EVALUATE__ELEMENT:
                return this.evaluate((Element) arguments.get(0));
            case SysmlPackage.CALCULATION_USAGE___MODEL_LEVEL_EVALUABLE__ELIST:
                return this.modelLevelEvaluable((EList<Feature>) arguments.get(0));
        }
        return super.eInvoke(operationID, arguments);
    }

} // CalculationUsageImpl
