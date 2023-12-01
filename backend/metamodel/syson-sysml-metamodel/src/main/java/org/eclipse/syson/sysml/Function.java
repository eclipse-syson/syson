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
package org.eclipse.syson.sysml;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Function</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.Function#isIsModelLevelEvaluable <em>Is Model Level Evaluable</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Function#getExpression <em>Expression</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Function#getResult <em>Result</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getFunction()
 * @model
 * @generated
 */
public interface Function extends Behavior {
    /**
     * Returns the value of the '<em><b>Expression</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.Expression}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Expression</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFunction_Expression()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    EList<Expression> getExpression();

    /**
     * Returns the value of the '<em><b>Is Model Level Evaluable</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Is Model Level Evaluable</em>' attribute.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFunction_IsModelLevelEvaluable()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    boolean isIsModelLevelEvaluable();

    /**
     * Returns the value of the '<em><b>Result</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Result</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFunction_Result()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Feature getResult();

} // Function
