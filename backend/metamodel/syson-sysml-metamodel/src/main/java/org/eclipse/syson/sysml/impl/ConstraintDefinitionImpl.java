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
import org.eclipse.syson.sysml.Behavior;
import org.eclipse.syson.sysml.ConstraintDefinition;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Function;
import org.eclipse.syson.sysml.Predicate;
import org.eclipse.syson.sysml.Step;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Constraint Definition</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.ConstraintDefinitionImpl#getParameter <em>Parameter</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConstraintDefinitionImpl#getStep <em>Step</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConstraintDefinitionImpl#isIsModelLevelEvaluable <em>Is Model Level
 * Evaluable</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConstraintDefinitionImpl#getExpression <em>Expression</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ConstraintDefinitionImpl#getResult <em>Result</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConstraintDefinitionImpl extends OccurrenceDefinitionImpl implements ConstraintDefinition {
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
    protected ConstraintDefinitionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getConstraintDefinition();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Feature> getParameter() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getBehavior_Parameter(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Step> getStep() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getBehavior_Step(), data.size(), data.toArray());
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
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.CONSTRAINT_DEFINITION__PARAMETER:
                return this.getParameter();
            case SysmlPackage.CONSTRAINT_DEFINITION__STEP:
                return this.getStep();
            case SysmlPackage.CONSTRAINT_DEFINITION__IS_MODEL_LEVEL_EVALUABLE:
                return this.isIsModelLevelEvaluable();
            case SysmlPackage.CONSTRAINT_DEFINITION__EXPRESSION:
                return this.getExpression();
            case SysmlPackage.CONSTRAINT_DEFINITION__RESULT:
                if (resolve)
                    return this.getResult();
                return this.basicGetResult();
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
            case SysmlPackage.CONSTRAINT_DEFINITION__PARAMETER:
                return !this.getParameter().isEmpty();
            case SysmlPackage.CONSTRAINT_DEFINITION__STEP:
                return !this.getStep().isEmpty();
            case SysmlPackage.CONSTRAINT_DEFINITION__IS_MODEL_LEVEL_EVALUABLE:
                return this.isIsModelLevelEvaluable() != IS_MODEL_LEVEL_EVALUABLE_EDEFAULT;
            case SysmlPackage.CONSTRAINT_DEFINITION__EXPRESSION:
                return !this.getExpression().isEmpty();
            case SysmlPackage.CONSTRAINT_DEFINITION__RESULT:
                return this.basicGetResult() != null;
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
        if (baseClass == Behavior.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.CONSTRAINT_DEFINITION__PARAMETER:
                    return SysmlPackage.BEHAVIOR__PARAMETER;
                case SysmlPackage.CONSTRAINT_DEFINITION__STEP:
                    return SysmlPackage.BEHAVIOR__STEP;
                default:
                    return -1;
            }
        }
        if (baseClass == Function.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.CONSTRAINT_DEFINITION__IS_MODEL_LEVEL_EVALUABLE:
                    return SysmlPackage.FUNCTION__IS_MODEL_LEVEL_EVALUABLE;
                case SysmlPackage.CONSTRAINT_DEFINITION__EXPRESSION:
                    return SysmlPackage.FUNCTION__EXPRESSION;
                case SysmlPackage.CONSTRAINT_DEFINITION__RESULT:
                    return SysmlPackage.FUNCTION__RESULT;
                default:
                    return -1;
            }
        }
        if (baseClass == Predicate.class) {
            switch (derivedFeatureID) {
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
        if (baseClass == Behavior.class) {
            switch (baseFeatureID) {
                case SysmlPackage.BEHAVIOR__PARAMETER:
                    return SysmlPackage.CONSTRAINT_DEFINITION__PARAMETER;
                case SysmlPackage.BEHAVIOR__STEP:
                    return SysmlPackage.CONSTRAINT_DEFINITION__STEP;
                default:
                    return -1;
            }
        }
        if (baseClass == Function.class) {
            switch (baseFeatureID) {
                case SysmlPackage.FUNCTION__IS_MODEL_LEVEL_EVALUABLE:
                    return SysmlPackage.CONSTRAINT_DEFINITION__IS_MODEL_LEVEL_EVALUABLE;
                case SysmlPackage.FUNCTION__EXPRESSION:
                    return SysmlPackage.CONSTRAINT_DEFINITION__EXPRESSION;
                case SysmlPackage.FUNCTION__RESULT:
                    return SysmlPackage.CONSTRAINT_DEFINITION__RESULT;
                default:
                    return -1;
            }
        }
        if (baseClass == Predicate.class) {
            switch (baseFeatureID) {
                default:
                    return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
    }

} // ConstraintDefinitionImpl
