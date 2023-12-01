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


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Objective Membership</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.ObjectiveMembership#getOwnedObjectiveRequirement <em>Owned Objective Requirement</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getObjectiveMembership()
 * @model
 * @generated
 */
public interface ObjectiveMembership extends FeatureMembership {
    /**
     * Returns the value of the '<em><b>Owned Objective Requirement</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Owned Objective Requirement</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getObjectiveMembership_OwnedObjectiveRequirement()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    RequirementUsage getOwnedObjectiveRequirement();

} // ObjectiveMembership
