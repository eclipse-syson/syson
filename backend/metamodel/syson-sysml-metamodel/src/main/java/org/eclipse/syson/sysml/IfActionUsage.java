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
 * A representation of the model object '<em><b>If Action Usage</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.IfActionUsage#getElseAction <em>Else Action</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.IfActionUsage#getIfArgument <em>If Argument</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.IfActionUsage#getThenAction <em>Then Action</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getIfActionUsage()
 * @model
 * @generated
 */
public interface IfActionUsage extends ActionUsage {
    /**
     * Returns the value of the '<em><b>Else Action</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Else Action</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getIfActionUsage_ElseAction()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    ActionUsage getElseAction();

    /**
     * Returns the value of the '<em><b>If Argument</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>If Argument</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getIfActionUsage_IfArgument()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Expression getIfArgument();

    /**
     * Returns the value of the '<em><b>Then Action</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Then Action</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getIfActionUsage_ThenAction()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    ActionUsage getThenAction();

} // IfActionUsage
