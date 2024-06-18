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

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Result Expression Membership</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.ResultExpressionMembership#getOwnedResultExpression <em>Owned Result
 * Expression</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getResultExpressionMembership()
 * @model
 * @generated
 */
public interface ResultExpressionMembership extends FeatureMembership {
    /**
     * Returns the value of the '<em><b>Owned Result Expression</b></em>' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Owned Result Expression</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getResultExpressionMembership_OwnedResultExpression()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     *        annotation="redefines"
     * @generated
     */
    Expression getOwnedResultExpression();

} // ResultExpressionMembership
