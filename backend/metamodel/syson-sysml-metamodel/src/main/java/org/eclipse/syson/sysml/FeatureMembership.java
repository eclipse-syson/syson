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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Feature Membership</b></em>'. <!-- end-user-doc
 * -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.FeatureMembership#getOwnedMemberFeature <em>Owned Member Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.FeatureMembership#getOwningType <em>Owning Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getFeatureMembership()
 * @model
 * @generated
 */
public interface FeatureMembership extends OwningMembership, Featuring {
    /**
     * Returns the value of the '<em><b>Owned Member Feature</b></em>' reference. It is bidirectional and its opposite
     * is '{@link org.eclipse.syson.sysml.Feature#getOwningFeatureMembership <em>Owning Feature Membership</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Member Feature</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeatureMembership_OwnedMemberFeature()
     * @see org.eclipse.syson.sysml.Feature#getOwningFeatureMembership
     * @model opposite="owningFeatureMembership" required="true" transient="true" changeable="false" volatile="true"
     *        derived="true" ordered="false"
     * @generated
     */
    Feature getOwnedMemberFeature();

    /**
     * Returns the value of the '<em><b>Owning Type</b></em>' reference. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.Type#getOwnedFeatureMembership <em>Owned Feature Membership</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owning Type</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeatureMembership_OwningType()
     * @see org.eclipse.syson.sysml.Type#getOwnedFeatureMembership
     * @model opposite="ownedFeatureMembership" required="true" transient="true" changeable="false" volatile="true"
     *        derived="true" ordered="false"
     * @generated
     */
    Type getOwningType();

} // FeatureMembership
