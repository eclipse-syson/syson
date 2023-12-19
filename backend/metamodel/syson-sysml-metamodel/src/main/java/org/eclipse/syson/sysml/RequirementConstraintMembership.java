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
 * A representation of the model object '<em><b>Requirement Constraint Membership</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.RequirementConstraintMembership#getKind <em>Kind</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.RequirementConstraintMembership#getOwnedConstraint <em>Owned Constraint</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.RequirementConstraintMembership#getReferencedConstraint <em>Referenced Constraint</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getRequirementConstraintMembership()
 * @model
 * @generated
 */
public interface RequirementConstraintMembership extends FeatureMembership {
    /**
     * Returns the value of the '<em><b>Kind</b></em>' attribute.
     * The literals are from the enumeration {@link org.eclipse.syson.sysml.RequirementConstraintKind}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Kind</em>' attribute.
     * @see org.eclipse.syson.sysml.RequirementConstraintKind
     * @see #setKind(RequirementConstraintKind)
     * @see org.eclipse.syson.sysml.SysmlPackage#getRequirementConstraintMembership_Kind()
     * @model required="true" ordered="false"
     * @generated
     */
    RequirementConstraintKind getKind();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.RequirementConstraintMembership#getKind <em>Kind</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Kind</em>' attribute.
     * @see org.eclipse.syson.sysml.RequirementConstraintKind
     * @see #getKind()
     * @generated
     */
    void setKind(RequirementConstraintKind value);

    /**
     * Returns the value of the '<em><b>Owned Constraint</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Owned Constraint</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getRequirementConstraintMembership_OwnedConstraint()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    ConstraintUsage getOwnedConstraint();

    /**
     * Returns the value of the '<em><b>Referenced Constraint</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Referenced Constraint</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getRequirementConstraintMembership_ReferencedConstraint()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    ConstraintUsage getReferencedConstraint();

} // RequirementConstraintMembership
