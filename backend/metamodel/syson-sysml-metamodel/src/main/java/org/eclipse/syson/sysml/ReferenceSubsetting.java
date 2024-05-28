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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Reference Subsetting</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.ReferenceSubsetting#getReferencedFeature <em>Referenced Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.ReferenceSubsetting#getReferencingFeature <em>Referencing Feature</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getReferenceSubsetting()
 * @model
 * @generated
 */
public interface ReferenceSubsetting extends Subsetting {
    /**
     * Returns the value of the '<em><b>Referenced Feature</b></em>' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Referenced Feature</em>' reference.
     * @see #setReferencedFeature(Feature)
     * @see org.eclipse.syson.sysml.SysmlPackage#getReferenceSubsetting_ReferencedFeature()
     * @model required="true" ordered="false"
     * @generated
     */
    Feature getReferencedFeature();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.ReferenceSubsetting#getReferencedFeature <em>Referenced
     * Feature</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Referenced Feature</em>' reference.
     * @see #getReferencedFeature()
     * @generated
     */
    void setReferencedFeature(Feature value);

    /**
     * Returns the value of the '<em><b>Referencing Feature</b></em>' reference. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.Feature#getOwnedReferenceSubsetting <em>Owned Reference Subsetting</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Referencing Feature</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getReferenceSubsetting_ReferencingFeature()
     * @see org.eclipse.syson.sysml.Feature#getOwnedReferenceSubsetting
     * @model opposite="ownedReferenceSubsetting" required="true" transient="true" changeable="false" volatile="true"
     *        derived="true" ordered="false"
     * @generated
     */
    Feature getReferencingFeature();

} // ReferenceSubsetting
