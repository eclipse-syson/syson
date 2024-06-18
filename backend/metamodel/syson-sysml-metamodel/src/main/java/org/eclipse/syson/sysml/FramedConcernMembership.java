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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Framed Concern Membership</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.FramedConcernMembership#getOwnedConcern <em>Owned Concern</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.FramedConcernMembership#getReferencedConcern <em>Referenced Concern</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getFramedConcernMembership()
 * @model
 * @generated
 */
public interface FramedConcernMembership extends RequirementConstraintMembership {
    /**
     * Returns the value of the '<em><b>Owned Concern</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Concern</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFramedConcernMembership_OwnedConcern()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     *        annotation="redefines"
     * @generated
     */
    ConcernUsage getOwnedConcern();

    /**
     * Returns the value of the '<em><b>Referenced Concern</b></em>' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Referenced Concern</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFramedConcernMembership_ReferencedConcern()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     *        annotation="redefines"
     * @generated
     */
    ConcernUsage getReferencedConcern();

} // FramedConcernMembership
