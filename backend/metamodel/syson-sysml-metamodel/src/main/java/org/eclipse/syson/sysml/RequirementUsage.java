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
 * A representation of the model object '<em><b>Requirement Usage</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.RequirementUsage#getReqId <em>Req Id</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.RequirementUsage#getText <em>Text</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.RequirementUsage#getActorParameter <em>Actor Parameter</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.RequirementUsage#getAssumedConstraint <em>Assumed Constraint</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.RequirementUsage#getFramedConcern <em>Framed Concern</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.RequirementUsage#getRequiredConstraint <em>Required Constraint</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.RequirementUsage#getRequirementDefinition <em>Requirement Definition</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.RequirementUsage#getStakeholderParameter <em>Stakeholder Parameter</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.RequirementUsage#getSubjectParameter <em>Subject Parameter</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getRequirementUsage()
 * @model
 * @generated
 */
public interface RequirementUsage extends ConstraintUsage {
    /**
     * Returns the value of the '<em><b>Actor Parameter</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.PartUsage}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Actor Parameter</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getRequirementUsage_ActorParameter()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<PartUsage> getActorParameter();

    /**
     * Returns the value of the '<em><b>Assumed Constraint</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.ConstraintUsage}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Assumed Constraint</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getRequirementUsage_AssumedConstraint()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<ConstraintUsage> getAssumedConstraint();

    /**
     * Returns the value of the '<em><b>Framed Concern</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.ConcernUsage}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Framed Concern</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getRequirementUsage_FramedConcern()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<ConcernUsage> getFramedConcern();

    /**
     * Returns the value of the '<em><b>Req Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Req Id</em>' attribute.
     * @see #setReqId(String)
     * @see org.eclipse.syson.sysml.SysmlPackage#getRequirementUsage_ReqId()
     * @model ordered="false"
     * @generated
     */
    String getReqId();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.RequirementUsage#getReqId <em>Req Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Req Id</em>' attribute.
     * @see #getReqId()
     * @generated
     */
    void setReqId(String value);

    /**
     * Returns the value of the '<em><b>Required Constraint</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.ConstraintUsage}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Required Constraint</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getRequirementUsage_RequiredConstraint()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<ConstraintUsage> getRequiredConstraint();

    /**
     * Returns the value of the '<em><b>Requirement Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Requirement Definition</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getRequirementUsage_RequirementDefinition()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    RequirementDefinition getRequirementDefinition();

    /**
     * Returns the value of the '<em><b>Stakeholder Parameter</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.PartUsage}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Stakeholder Parameter</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getRequirementUsage_StakeholderParameter()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<PartUsage> getStakeholderParameter();

    /**
     * Returns the value of the '<em><b>Subject Parameter</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Subject Parameter</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getRequirementUsage_SubjectParameter()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Usage getSubjectParameter();

    /**
     * Returns the value of the '<em><b>Text</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Text</em>' attribute list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getRequirementUsage_Text()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    EList<String> getText();

} // RequirementUsage
