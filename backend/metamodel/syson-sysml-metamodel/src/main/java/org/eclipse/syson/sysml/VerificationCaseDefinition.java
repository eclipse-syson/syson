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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Verification Case Definition</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.VerificationCaseDefinition#getVerifiedRequirement <em>Verified
 * Requirement</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getVerificationCaseDefinition()
 * @model
 * @generated
 */
public interface VerificationCaseDefinition extends CaseDefinition {
    /**
     * Returns the value of the '<em><b>Verified Requirement</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.RequirementUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Verified Requirement</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getVerificationCaseDefinition_VerifiedRequirement()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<RequirementUsage> getVerifiedRequirement();

} // VerificationCaseDefinition
