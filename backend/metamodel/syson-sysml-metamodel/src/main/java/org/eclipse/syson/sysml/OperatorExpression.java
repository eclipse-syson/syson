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
 * A representation of the model object '<em><b>Operator Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.OperatorExpression#getOperator <em>Operator</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.OperatorExpression#getOperand <em>Operand</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getOperatorExpression()
 * @model
 * @generated
 */
public interface OperatorExpression extends InvocationExpression {
    /**
     * Returns the value of the '<em><b>Operand</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.Expression}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Operand</em>' containment reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getOperatorExpression_Operand()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Expression> getOperand();

    /**
     * Returns the value of the '<em><b>Operator</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Operator</em>' attribute.
     * @see #setOperator(String)
     * @see org.eclipse.syson.sysml.SysmlPackage#getOperatorExpression_Operator()
     * @model required="true" ordered="false"
     * @generated
     */
    String getOperator();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.OperatorExpression#getOperator <em>Operator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Operator</em>' attribute.
     * @see #getOperator()
     * @generated
     */
    void setOperator(String value);

} // OperatorExpression
