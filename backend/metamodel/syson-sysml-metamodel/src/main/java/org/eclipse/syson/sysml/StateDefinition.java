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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>State Definition</b></em>'. <!-- end-user-doc
 * -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.StateDefinition#isIsParallel <em>Is Parallel</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.StateDefinition#getDoAction <em>Do Action</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.StateDefinition#getEntryAction <em>Entry Action</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.StateDefinition#getExitAction <em>Exit Action</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.StateDefinition#getState <em>State</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getStateDefinition()
 * @model
 * @generated
 */
public interface StateDefinition extends ActionDefinition {
    /**
     * Returns the value of the '<em><b>Do Action</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Do Action</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getStateDefinition_DoAction()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    ActionUsage getDoAction();

    /**
     * Returns the value of the '<em><b>Entry Action</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Entry Action</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getStateDefinition_EntryAction()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    ActionUsage getEntryAction();

    /**
     * Returns the value of the '<em><b>Exit Action</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Exit Action</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getStateDefinition_ExitAction()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    ActionUsage getExitAction();

    /**
     * Returns the value of the '<em><b>Is Parallel</b></em>' attribute. The default value is <code>"false"</code>. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Is Parallel</em>' attribute.
     * @see #setIsParallel(boolean)
     * @see org.eclipse.syson.sysml.SysmlPackage#getStateDefinition_IsParallel()
     * @model default="false" required="true" ordered="false"
     * @generated
     */
    boolean isIsParallel();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.StateDefinition#isIsParallel <em>Is Parallel</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Is Parallel</em>' attribute.
     * @see #isIsParallel()
     * @generated
     */
    void setIsParallel(boolean value);

    /**
     * Returns the value of the '<em><b>State</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.StateUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>State</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getStateDefinition_State()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<StateUsage> getState();

} // StateDefinition
