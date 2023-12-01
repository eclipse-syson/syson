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
 * A representation of the model object '<em><b>State Usage</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.StateUsage#isIsParallel <em>Is Parallel</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.StateUsage#getDoAction <em>Do Action</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.StateUsage#getEntryAction <em>Entry Action</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.StateUsage#getExitAction <em>Exit Action</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.StateUsage#getStateDefinition <em>State Definition</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getStateUsage()
 * @model
 * @generated
 */
public interface StateUsage extends ActionUsage {
    /**
     * Returns the value of the '<em><b>Do Action</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Do Action</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getStateUsage_DoAction()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    ActionUsage getDoAction();

    /**
     * Returns the value of the '<em><b>Entry Action</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Entry Action</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getStateUsage_EntryAction()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    ActionUsage getEntryAction();

    /**
     * Returns the value of the '<em><b>Exit Action</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Exit Action</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getStateUsage_ExitAction()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    ActionUsage getExitAction();

    /**
     * Returns the value of the '<em><b>Is Parallel</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Is Parallel</em>' attribute.
     * @see #setIsParallel(boolean)
     * @see org.eclipse.syson.sysml.SysmlPackage#getStateUsage_IsParallel()
     * @model default="false" required="true" ordered="false"
     * @generated
     */
    boolean isIsParallel();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.StateUsage#isIsParallel <em>Is Parallel</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Is Parallel</em>' attribute.
     * @see #isIsParallel()
     * @generated
     */
    void setIsParallel(boolean value);

    /**
     * Returns the value of the '<em><b>State Definition</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.Behavior}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>State Definition</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getStateUsage_StateDefinition()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Behavior> getStateDefinition();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model required="true" ordered="false" isParallelRequired="true" isParallelOrdered="false"
     * @generated
     */
    boolean isSubstateUsage(boolean isParallel);

} // StateUsage
