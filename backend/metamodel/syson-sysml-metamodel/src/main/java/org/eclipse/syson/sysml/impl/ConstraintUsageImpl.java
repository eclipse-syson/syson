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
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.Behavior;
import org.eclipse.syson.sysml.BooleanExpression;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Function;
import org.eclipse.syson.sysml.Predicate;
import org.eclipse.syson.sysml.Step;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Constraint Usage</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.ConstraintUsageImpl#getBehavior <em>Behavior</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ConstraintUsageImpl#getParameter <em>Parameter</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ConstraintUsageImpl#isIsModelLevelEvaluable <em>Is Model Level Evaluable</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ConstraintUsageImpl#getFunction <em>Function</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ConstraintUsageImpl#getResult <em>Result</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ConstraintUsageImpl#getPredicate <em>Predicate</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ConstraintUsageImpl#getConstraintDefinition <em>Constraint Definition</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConstraintUsageImpl extends OccurrenceUsageImpl implements ConstraintUsage {
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
    protected ConstraintUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getConstraintUsage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Behavior> getBehavior() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getStep_Behavior(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Feature> getParameter() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getStep_Parameter(), data.size(), data.toArray());
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
     * @generated
     */
    @Override
    public Predicate getPredicate() {
        Predicate predicate = basicGetPredicate();
        return predicate != null && predicate.eIsProxy() ? (Predicate)eResolveProxy((InternalEObject)predicate) : predicate;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Predicate basicGetPredicate() {
        // TODO: implement this method to return the 'Predicate' reference
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
    public Predicate getConstraintDefinition() {
        Predicate constraintDefinition = basicGetConstraintDefinition();
        return constraintDefinition != null && constraintDefinition.eIsProxy() ? (Predicate)eResolveProxy((InternalEObject)constraintDefinition) : constraintDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Predicate basicGetConstraintDefinition() {
        // TODO: implement this method to return the 'Constraint Definition' reference
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
            case SysmlPackage.CONSTRAINT_USAGE__BEHAVIOR:
                return getBehavior();
            case SysmlPackage.CONSTRAINT_USAGE__PARAMETER:
                return getParameter();
            case SysmlPackage.CONSTRAINT_USAGE__IS_MODEL_LEVEL_EVALUABLE:
                return isIsModelLevelEvaluable();
            case SysmlPackage.CONSTRAINT_USAGE__FUNCTION:
                if (resolve) return getFunction();
                return basicGetFunction();
            case SysmlPackage.CONSTRAINT_USAGE__RESULT:
                if (resolve) return getResult();
                return basicGetResult();
            case SysmlPackage.CONSTRAINT_USAGE__PREDICATE:
                if (resolve) return getPredicate();
                return basicGetPredicate();
            case SysmlPackage.CONSTRAINT_USAGE__CONSTRAINT_DEFINITION:
                if (resolve) return getConstraintDefinition();
                return basicGetConstraintDefinition();
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
            case SysmlPackage.CONSTRAINT_USAGE__BEHAVIOR:
                return !getBehavior().isEmpty();
            case SysmlPackage.CONSTRAINT_USAGE__PARAMETER:
                return !getParameter().isEmpty();
            case SysmlPackage.CONSTRAINT_USAGE__IS_MODEL_LEVEL_EVALUABLE:
                return isIsModelLevelEvaluable() != IS_MODEL_LEVEL_EVALUABLE_EDEFAULT;
            case SysmlPackage.CONSTRAINT_USAGE__FUNCTION:
                return basicGetFunction() != null;
            case SysmlPackage.CONSTRAINT_USAGE__RESULT:
                return basicGetResult() != null;
            case SysmlPackage.CONSTRAINT_USAGE__PREDICATE:
                return basicGetPredicate() != null;
            case SysmlPackage.CONSTRAINT_USAGE__CONSTRAINT_DEFINITION:
                return basicGetConstraintDefinition() != null;
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
        if (baseClass == Step.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.CONSTRAINT_USAGE__BEHAVIOR: return SysmlPackage.STEP__BEHAVIOR;
                case SysmlPackage.CONSTRAINT_USAGE__PARAMETER: return SysmlPackage.STEP__PARAMETER;
                default: return -1;
            }
        }
        if (baseClass == Expression.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.CONSTRAINT_USAGE__IS_MODEL_LEVEL_EVALUABLE: return SysmlPackage.EXPRESSION__IS_MODEL_LEVEL_EVALUABLE;
                case SysmlPackage.CONSTRAINT_USAGE__FUNCTION: return SysmlPackage.EXPRESSION__FUNCTION;
                case SysmlPackage.CONSTRAINT_USAGE__RESULT: return SysmlPackage.EXPRESSION__RESULT;
                default: return -1;
            }
        }
        if (baseClass == BooleanExpression.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.CONSTRAINT_USAGE__PREDICATE: return SysmlPackage.BOOLEAN_EXPRESSION__PREDICATE;
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
        if (baseClass == Step.class) {
            switch (baseFeatureID) {
                case SysmlPackage.STEP__BEHAVIOR: return SysmlPackage.CONSTRAINT_USAGE__BEHAVIOR;
                case SysmlPackage.STEP__PARAMETER: return SysmlPackage.CONSTRAINT_USAGE__PARAMETER;
                default: return -1;
            }
        }
        if (baseClass == Expression.class) {
            switch (baseFeatureID) {
                case SysmlPackage.EXPRESSION__IS_MODEL_LEVEL_EVALUABLE: return SysmlPackage.CONSTRAINT_USAGE__IS_MODEL_LEVEL_EVALUABLE;
                case SysmlPackage.EXPRESSION__FUNCTION: return SysmlPackage.CONSTRAINT_USAGE__FUNCTION;
                case SysmlPackage.EXPRESSION__RESULT: return SysmlPackage.CONSTRAINT_USAGE__RESULT;
                default: return -1;
            }
        }
        if (baseClass == BooleanExpression.class) {
            switch (baseFeatureID) {
                case SysmlPackage.BOOLEAN_EXPRESSION__PREDICATE: return SysmlPackage.CONSTRAINT_USAGE__PREDICATE;
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
    public int eDerivedOperationID(int baseOperationID, Class<?> baseClass) {
        if (baseClass == Step.class) {
            switch (baseOperationID) {
                default: return -1;
            }
        }
        if (baseClass == Expression.class) {
            switch (baseOperationID) {
                case SysmlPackage.EXPRESSION___CHECK_CONDITION__ELEMENT: return SysmlPackage.CONSTRAINT_USAGE___CHECK_CONDITION__ELEMENT;
                case SysmlPackage.EXPRESSION___EVALUATE__ELEMENT: return SysmlPackage.CONSTRAINT_USAGE___EVALUATE__ELEMENT;
                case SysmlPackage.EXPRESSION___MODEL_LEVEL_EVALUABLE__ELIST: return SysmlPackage.CONSTRAINT_USAGE___MODEL_LEVEL_EVALUABLE__ELIST;
                default: return -1;
            }
        }
        if (baseClass == BooleanExpression.class) {
            switch (baseOperationID) {
                default: return -1;
            }
        }
        return super.eDerivedOperationID(baseOperationID, baseClass);
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
            case SysmlPackage.CONSTRAINT_USAGE___CHECK_CONDITION__ELEMENT:
                return checkCondition((Element)arguments.get(0));
            case SysmlPackage.CONSTRAINT_USAGE___EVALUATE__ELEMENT:
                return evaluate((Element)arguments.get(0));
            case SysmlPackage.CONSTRAINT_USAGE___MODEL_LEVEL_EVALUABLE__ELIST:
                return modelLevelEvaluable((EList<Feature>)arguments.get(0));
        }
        return super.eInvoke(operationID, arguments);
    }

} //ConstraintUsageImpl
