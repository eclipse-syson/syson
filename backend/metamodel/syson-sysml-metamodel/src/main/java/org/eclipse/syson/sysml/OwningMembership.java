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
 * A representation of the model object '<em><b>Owning Membership</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.OwningMembership#getOwnedMemberElementId <em>Owned Member Element Id</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.OwningMembership#getOwnedMemberName <em>Owned Member Name</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.OwningMembership#getOwnedMemberShortName <em>Owned Member Short Name</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.OwningMembership#getOwnedMemberElement <em>Owned Member Element</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getOwningMembership()
 * @model
 * @generated
 */
public interface OwningMembership extends Membership {
    /**
     * Returns the value of the '<em><b>Owned Member Element</b></em>' reference.
     * It is bidirectional and its opposite is '{@link org.eclipse.syson.sysml.Element#getOwningMembership <em>Owning Membership</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Owned Member Element</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getOwningMembership_OwnedMemberElement()
     * @see org.eclipse.syson.sysml.Element#getOwningMembership
     * @model opposite="owningMembership" required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Element getOwnedMemberElement();

    /**
     * Returns the value of the '<em><b>Owned Member Element Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Owned Member Element Id</em>' attribute.
     * @see org.eclipse.syson.sysml.SysmlPackage#getOwningMembership_OwnedMemberElementId()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    String getOwnedMemberElementId();

    /**
     * Returns the value of the '<em><b>Owned Member Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Owned Member Name</em>' attribute.
     * @see org.eclipse.syson.sysml.SysmlPackage#getOwningMembership_OwnedMemberName()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    String getOwnedMemberName();

    /**
     * Returns the value of the '<em><b>Owned Member Short Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Owned Member Short Name</em>' attribute.
     * @see org.eclipse.syson.sysml.SysmlPackage#getOwningMembership_OwnedMemberShortName()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    String getOwnedMemberShortName();

} // OwningMembership
