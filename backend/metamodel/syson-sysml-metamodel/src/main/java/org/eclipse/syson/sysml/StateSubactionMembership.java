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
 * A representation of the model object '<em><b>State Subaction Membership</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.StateSubactionMembership#getKind <em>Kind</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.StateSubactionMembership#getAction <em>Action</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getStateSubactionMembership()
 * @model
 * @generated
 */
public interface StateSubactionMembership extends FeatureMembership {
    /**
     * Returns the value of the '<em><b>Action</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Action</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getStateSubactionMembership_Action()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    ActionUsage getAction();

    /**
     * Returns the value of the '<em><b>Kind</b></em>' attribute.
     * The literals are from the enumeration {@link org.eclipse.syson.sysml.StateSubactionKind}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Kind</em>' attribute.
     * @see org.eclipse.syson.sysml.StateSubactionKind
     * @see #setKind(StateSubactionKind)
     * @see org.eclipse.syson.sysml.SysmlPackage#getStateSubactionMembership_Kind()
     * @model required="true" ordered="false"
     * @generated
     */
    StateSubactionKind getKind();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.StateSubactionMembership#getKind <em>Kind</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Kind</em>' attribute.
     * @see org.eclipse.syson.sysml.StateSubactionKind
     * @see #getKind()
     * @generated
     */
    void setKind(StateSubactionKind value);

} // StateSubactionMembership
