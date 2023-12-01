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


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>While Loop Action Usage</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.WhileLoopActionUsage#getUntilArgument <em>Until Argument</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.WhileLoopActionUsage#getWhileArgument <em>While Argument</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getWhileLoopActionUsage()
 * @model
 * @generated
 */
public interface WhileLoopActionUsage extends LoopActionUsage {
    /**
     * Returns the value of the '<em><b>Until Argument</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Until Argument</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getWhileLoopActionUsage_UntilArgument()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Expression getUntilArgument();

    /**
     * Returns the value of the '<em><b>While Argument</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>While Argument</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getWhileLoopActionUsage_WhileArgument()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Expression getWhileArgument();

} // WhileLoopActionUsage
