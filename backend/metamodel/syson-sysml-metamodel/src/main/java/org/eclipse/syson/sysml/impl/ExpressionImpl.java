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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Function;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.ExpressionImpl#isIsModelLevelEvaluable <em>Is Model Level Evaluable</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ExpressionImpl#getFunction <em>Function</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ExpressionImpl#getResult <em>Result</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ExpressionImpl extends StepImpl implements Expression {
    /**
     * The default value of the '{@link #isIsModelLevelEvaluable() <em>Is Model Level Evaluable</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsModelLevelEvaluable()
     * @generated
     * @ordered
     */
    protected static final boolean IS_MODEL_LEVEL_EVALUABLE_EDEFAULT = false;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ExpressionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getExpression();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Function getFunction() {
        Function function = basicGetFunction();
        return function != null && function.eIsProxy() ? (Function)eResolveProxy((InternalEObject)function) : function;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Function basicGetFunction() {
        // TODO: implement this method to return the 'Function' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public boolean isIsModelLevelEvaluable() {
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Feature getResult() {
        Feature result = basicGetResult();
        return result != null && result.eIsProxy() ? (Feature)eResolveProxy((InternalEObject)result) : result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Feature basicGetResult() {
        // TODO: implement this method to return the 'Result' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public boolean checkCondition(Element target) {
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Element> evaluate(Element target) {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public boolean modelLevelEvaluable(EList<Feature> visited) {
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.EXPRESSION__IS_MODEL_LEVEL_EVALUABLE:
                return isIsModelLevelEvaluable();
            case SysmlPackage.EXPRESSION__FUNCTION:
                if (resolve) return getFunction();
                return basicGetFunction();
            case SysmlPackage.EXPRESSION__RESULT:
                if (resolve) return getResult();
                return basicGetResult();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.EXPRESSION__IS_MODEL_LEVEL_EVALUABLE:
                return isIsModelLevelEvaluable() != IS_MODEL_LEVEL_EVALUABLE_EDEFAULT;
            case SysmlPackage.EXPRESSION__FUNCTION:
                return basicGetFunction() != null;
            case SysmlPackage.EXPRESSION__RESULT:
                return basicGetResult() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
        switch (operationID) {
            case SysmlPackage.EXPRESSION___CHECK_CONDITION__ELEMENT:
                return checkCondition((Element)arguments.get(0));
            case SysmlPackage.EXPRESSION___EVALUATE__ELEMENT:
                return evaluate((Element)arguments.get(0));
            case SysmlPackage.EXPRESSION___MODEL_LEVEL_EVALUABLE__ELIST:
                return modelLevelEvaluable((EList<Feature>)arguments.get(0));
        }
        return super.eInvoke(operationID, arguments);
    }

} //ExpressionImpl
