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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Requirement Verification Membership</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.RequirementVerificationMembership#getOwnedRequirement <em>Owned
 * Requirement</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.RequirementVerificationMembership#getVerifiedRequirement <em>Verified
 * Requirement</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getRequirementVerificationMembership()
 * @model
 * @generated
 */
public interface RequirementVerificationMembership extends RequirementConstraintMembership {
    /**
     * Returns the value of the '<em><b>Owned Requirement</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @return the value of the '<em>Owned Requirement</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getRequirementVerificationMembership_OwnedRequirement()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    RequirementUsage getOwnedRequirement();

    /**
     * Returns the value of the '<em><b>Verified Requirement</b></em>' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Verified Requirement</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getRequirementVerificationMembership_VerifiedRequirement()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     *        annotation="redefines"
     * @generated
     */
    RequirementUsage getVerifiedRequirement();

} // RequirementVerificationMembership
