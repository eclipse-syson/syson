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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Case Usage</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.CaseUsage#getActorParameter <em>Actor Parameter</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.CaseUsage#getCaseDefinition <em>Case Definition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.CaseUsage#getObjectiveRequirement <em>Objective Requirement</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.CaseUsage#getSubjectParameter <em>Subject Parameter</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getCaseUsage()
 * @model
 * @generated
 */
public interface CaseUsage extends CalculationUsage {
    /**
     * Returns the value of the '<em><b>Actor Parameter</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.PartUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Actor Parameter</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getCaseUsage_ActorParameter()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<PartUsage> getActorParameter();

    /**
     * Returns the value of the '<em><b>Case Definition</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @return the value of the '<em>Case Definition</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getCaseUsage_CaseDefinition()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    CaseDefinition getCaseDefinition();

    /**
     * Returns the value of the '<em><b>Objective Requirement</b></em>' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Objective Requirement</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getCaseUsage_ObjectiveRequirement()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    RequirementUsage getObjectiveRequirement();

    /**
     * Returns the value of the '<em><b>Subject Parameter</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @return the value of the '<em>Subject Parameter</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getCaseUsage_SubjectParameter()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Usage getSubjectParameter();

} // CaseUsage
