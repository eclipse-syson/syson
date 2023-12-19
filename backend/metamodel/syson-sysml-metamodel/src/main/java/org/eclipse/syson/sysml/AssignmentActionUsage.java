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
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Assignment Action Usage</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.AssignmentActionUsage#getReferent <em>Referent</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.AssignmentActionUsage#getTargetArgument <em>Target Argument</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.AssignmentActionUsage#getValueExpression <em>Value Expression</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getAssignmentActionUsage()
 * @model
 * @generated
 */
public interface AssignmentActionUsage extends ActionUsage {
    /**
     * Returns the value of the '<em><b>Referent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Referent</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getAssignmentActionUsage_Referent()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Feature getReferent();

    /**
     * Returns the value of the '<em><b>Target Argument</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Target Argument</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getAssignmentActionUsage_TargetArgument()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Expression getTargetArgument();

    /**
     * Returns the value of the '<em><b>Value Expression</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value Expression</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getAssignmentActionUsage_ValueExpression()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Expression getValueExpression();

} // AssignmentActionUsage
