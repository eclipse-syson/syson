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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Satisfy Requirement Usage</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.SatisfyRequirementUsage#getSatisfiedRequirement <em>Satisfied
 * Requirement</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.SatisfyRequirementUsage#getSatisfyingFeature <em>Satisfying Feature</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getSatisfyRequirementUsage()
 * @model
 * @generated
 */
public interface SatisfyRequirementUsage extends RequirementUsage, AssertConstraintUsage {
    /**
     * Returns the value of the '<em><b>Satisfied Requirement</b></em>' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Satisfied Requirement</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getSatisfyRequirementUsage_SatisfiedRequirement()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     *        annotation="redefines"
     * @generated
     */
    RequirementUsage getSatisfiedRequirement();

    /**
     * Returns the value of the '<em><b>Satisfying Feature</b></em>' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Satisfying Feature</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getSatisfyRequirementUsage_SatisfyingFeature()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Feature getSatisfyingFeature();

} // SatisfyRequirementUsage
