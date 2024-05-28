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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Stakeholder Membership</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.StakeholderMembership#getOwnedStakeholderParameter <em>Owned Stakeholder
 * Parameter</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getStakeholderMembership()
 * @model
 * @generated
 */
public interface StakeholderMembership extends ParameterMembership {
    /**
     * Returns the value of the '<em><b>Owned Stakeholder Parameter</b></em>' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Owned Stakeholder Parameter</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getStakeholderMembership_OwnedStakeholderParameter()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    PartUsage getOwnedStakeholderParameter();

} // StakeholderMembership
