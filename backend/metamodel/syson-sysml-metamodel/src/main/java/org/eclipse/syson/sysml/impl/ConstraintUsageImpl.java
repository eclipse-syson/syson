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
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.Predicate;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.Step;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Constraint Usage</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.ConstraintUsageImpl#getBehavior <em>Behavior</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConstraintUsageImpl#getParameter <em>Parameter</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConstraintUsageImpl#isIsModelLevelEvaluable <em>Is Model Level
 * Evaluable</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConstraintUsageImpl#getFunction <em>Function</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConstraintUsageImpl#getResult <em>Result</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConstraintUsageImpl#getPredicate <em>Predicate</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConstraintUsageImpl#getConstraintDefinition <em>Constraint
 * Definition</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConstraintUsageImpl extends OccurrenceUsageImpl implements ConstraintUsage {
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
    protected ConstraintUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getConstraintUsage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Behavior> getBehavior() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getStep_Behavior(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Feature> getParameter() {
        List<Feature> features = this.getOwnedRelationship().stream()
                .filter(ParameterMembership.class::isInstance)
                .map(ParameterMembership.class::cast)
                .flatMap(or -> or.getOwnedRelatedElement().stream())
                .filter(Feature.class::isInstance)
                .map(Feature.class::cast)
                .toList();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getStep_Parameter(), features.size(), features.toArray());
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
    public Predicate getPredicate() {
        Predicate predicate = this.basicGetPredicate();
        return predicate != null && predicate.eIsProxy() ? (Predicate) this.eResolveProxy((InternalEObject) predicate) : predicate;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Predicate basicGetPredicate() {
        return this.getConstraintDefinition();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Predicate getConstraintDefinition() {
        Predicate constraintDefinition = this.basicGetConstraintDefinition();
        return constraintDefinition != null && constraintDefinition.eIsProxy() ? (Predicate) this.eResolveProxy((InternalEObject) constraintDefinition) : constraintDefinition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Predicate basicGetConstraintDefinition() {
        // TODO: implement this method to return the 'Constraint Definition' reference
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
            case SysmlPackage.CONSTRAINT_USAGE__BEHAVIOR:
                return this.getBehavior();
            case SysmlPackage.CONSTRAINT_USAGE__PARAMETER:
                return this.getParameter();
            case SysmlPackage.CONSTRAINT_USAGE__IS_MODEL_LEVEL_EVALUABLE:
                return this.isIsModelLevelEvaluable();
            case SysmlPackage.CONSTRAINT_USAGE__FUNCTION:
                if (resolve) {
                    return this.getFunction();
                }
                return this.basicGetFunction();
            case SysmlPackage.CONSTRAINT_USAGE__RESULT:
                if (resolve) {
                    return this.getResult();
                }
                return this.basicGetResult();
            case SysmlPackage.CONSTRAINT_USAGE__PREDICATE:
                if (resolve) {
                    return this.getPredicate();
                }
                return this.basicGetPredicate();
            case SysmlPackage.CONSTRAINT_USAGE__CONSTRAINT_DEFINITION:
                if (resolve) {
                    return this.getConstraintDefinition();
                }
                return this.basicGetConstraintDefinition();
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
            case SysmlPackage.CONSTRAINT_USAGE__BEHAVIOR:
                return !this.getBehavior().isEmpty();
            case SysmlPackage.CONSTRAINT_USAGE__PARAMETER:
                return !this.getParameter().isEmpty();
            case SysmlPackage.CONSTRAINT_USAGE__IS_MODEL_LEVEL_EVALUABLE:
                return this.isIsModelLevelEvaluable() != IS_MODEL_LEVEL_EVALUABLE_EDEFAULT;
            case SysmlPackage.CONSTRAINT_USAGE__FUNCTION:
                return this.basicGetFunction() != null;
            case SysmlPackage.CONSTRAINT_USAGE__RESULT:
                return this.basicGetResult() != null;
            case SysmlPackage.CONSTRAINT_USAGE__PREDICATE:
                return this.basicGetPredicate() != null;
            case SysmlPackage.CONSTRAINT_USAGE__CONSTRAINT_DEFINITION:
                return this.basicGetConstraintDefinition() != null;
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
        if (baseClass == Step.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.CONSTRAINT_USAGE__BEHAVIOR:
                    return SysmlPackage.STEP__BEHAVIOR;
                case SysmlPackage.CONSTRAINT_USAGE__PARAMETER:
                    return SysmlPackage.STEP__PARAMETER;
                default:
                    return -1;
            }
        }
        if (baseClass == Expression.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.CONSTRAINT_USAGE__IS_MODEL_LEVEL_EVALUABLE:
                    return SysmlPackage.EXPRESSION__IS_MODEL_LEVEL_EVALUABLE;
                case SysmlPackage.CONSTRAINT_USAGE__FUNCTION:
                    return SysmlPackage.EXPRESSION__FUNCTION;
                case SysmlPackage.CONSTRAINT_USAGE__RESULT:
                    return SysmlPackage.EXPRESSION__RESULT;
                default:
                    return -1;
            }
        }
        if (baseClass == BooleanExpression.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.CONSTRAINT_USAGE__PREDICATE:
                    return SysmlPackage.BOOLEAN_EXPRESSION__PREDICATE;
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
        if (baseClass == Step.class) {
            switch (baseFeatureID) {
                case SysmlPackage.STEP__BEHAVIOR:
                    return SysmlPackage.CONSTRAINT_USAGE__BEHAVIOR;
                case SysmlPackage.STEP__PARAMETER:
                    return SysmlPackage.CONSTRAINT_USAGE__PARAMETER;
                default:
                    return -1;
            }
        }
        if (baseClass == Expression.class) {
            switch (baseFeatureID) {
                case SysmlPackage.EXPRESSION__IS_MODEL_LEVEL_EVALUABLE:
                    return SysmlPackage.CONSTRAINT_USAGE__IS_MODEL_LEVEL_EVALUABLE;
                case SysmlPackage.EXPRESSION__FUNCTION:
                    return SysmlPackage.CONSTRAINT_USAGE__FUNCTION;
                case SysmlPackage.EXPRESSION__RESULT:
                    return SysmlPackage.CONSTRAINT_USAGE__RESULT;
                default:
                    return -1;
            }
        }
        if (baseClass == BooleanExpression.class) {
            switch (baseFeatureID) {
                case SysmlPackage.BOOLEAN_EXPRESSION__PREDICATE:
                    return SysmlPackage.CONSTRAINT_USAGE__PREDICATE;
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
        if (baseClass == Step.class) {
            switch (baseOperationID) {
                default:
                    return -1;
            }
        }
        if (baseClass == Expression.class) {
            switch (baseOperationID) {
                case SysmlPackage.EXPRESSION___CHECK_CONDITION__ELEMENT:
                    return SysmlPackage.CONSTRAINT_USAGE___CHECK_CONDITION__ELEMENT;
                case SysmlPackage.EXPRESSION___EVALUATE__ELEMENT:
                    return SysmlPackage.CONSTRAINT_USAGE___EVALUATE__ELEMENT;
                case SysmlPackage.EXPRESSION___MODEL_LEVEL_EVALUABLE__ELIST:
                    return SysmlPackage.CONSTRAINT_USAGE___MODEL_LEVEL_EVALUABLE__ELIST;
                default:
                    return -1;
            }
        }
        if (baseClass == BooleanExpression.class) {
            switch (baseOperationID) {
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
            case SysmlPackage.CONSTRAINT_USAGE___CHECK_CONDITION__ELEMENT:
                return this.checkCondition((Element) arguments.get(0));
            case SysmlPackage.CONSTRAINT_USAGE___EVALUATE__ELEMENT:
                return this.evaluate((Element) arguments.get(0));
            case SysmlPackage.CONSTRAINT_USAGE___MODEL_LEVEL_EVALUABLE__ELIST:
                return this.modelLevelEvaluable((EList<Feature>) arguments.get(0));
        }
        return super.eInvoke(operationID, arguments);
    }

    /**
     * @generated NOT
     */
    @Override
    public Feature namingFeature() {
        OwningMembership owMembership = this.getOwningMembership();
        if (owMembership instanceof RequirementConstraintMembership && this.getOwnedReferenceSubsetting() != null) {
            return this.getOwnedReferenceSubsetting().getReferencedFeature();
        }
        return super.namingFeature();
    }

} // ConstraintUsageImpl
