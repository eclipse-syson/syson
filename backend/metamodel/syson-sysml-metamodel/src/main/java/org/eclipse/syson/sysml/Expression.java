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
package org.eclipse.syson.sysml;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.Expression#isIsModelLevelEvaluable <em>Is Model Level Evaluable</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Expression#getFunction <em>Function</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Expression#getResult <em>Result</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getExpression()
 * @model
 * @generated
 */
public interface Expression extends Step {
    /**
     * Returns the value of the '<em><b>Function</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Function</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getExpression_Function()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Function getFunction();

    /**
     * Returns the value of the '<em><b>Is Model Level Evaluable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Is Model Level Evaluable</em>' attribute.
     * @see org.eclipse.syson.sysml.SysmlPackage#getExpression_IsModelLevelEvaluable()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    boolean isIsModelLevelEvaluable();

    /**
     * Returns the value of the '<em><b>Result</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Result</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getExpression_Result()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Feature getResult();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model required="true" ordered="false" targetRequired="true" targetOrdered="false"
     * @generated
     */
    boolean checkCondition(Element target);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model unique="false" targetRequired="true" targetOrdered="false"
     * @generated
     */
    EList<Element> evaluate(Element target);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model required="true" ordered="false" visitedMany="true" visitedOrdered="false"
     * @generated
     */
    boolean modelLevelEvaluable(EList<Feature> visited);

} // Expression
