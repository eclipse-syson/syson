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
 * A representation of the model object '<em><b>Subsetting</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.Subsetting#getOwningFeature <em>Owning Feature</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Subsetting#getSubsettedFeature <em>Subsetted Feature</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Subsetting#getSubsettingFeature <em>Subsetting Feature</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getSubsetting()
 * @model
 * @generated
 */
public interface Subsetting extends Specialization {
    /**
     * Returns the value of the '<em><b>Owning Feature</b></em>' reference.
     * It is bidirectional and its opposite is '{@link org.eclipse.syson.sysml.Feature#getOwnedSubsetting <em>Owned Subsetting</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Owning Feature</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getSubsetting_OwningFeature()
     * @see org.eclipse.syson.sysml.Feature#getOwnedSubsetting
     * @model opposite="ownedSubsetting" required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Feature getOwningFeature();

    /**
     * Returns the value of the '<em><b>Subsetted Feature</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Subsetted Feature</em>' reference.
     * @see #setSubsettedFeature(Feature)
     * @see org.eclipse.syson.sysml.SysmlPackage#getSubsetting_SubsettedFeature()
     * @model required="true" ordered="false"
     * @generated
     */
    Feature getSubsettedFeature();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Subsetting#getSubsettedFeature <em>Subsetted Feature</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Subsetted Feature</em>' reference.
     * @see #getSubsettedFeature()
     * @generated
     */
    void setSubsettedFeature(Feature value);

    /**
     * Returns the value of the '<em><b>Subsetting Feature</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Subsetting Feature</em>' reference.
     * @see #setSubsettingFeature(Feature)
     * @see org.eclipse.syson.sysml.SysmlPackage#getSubsetting_SubsettingFeature()
     * @model required="true" ordered="false"
     * @generated
     */
    Feature getSubsettingFeature();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Subsetting#getSubsettingFeature <em>Subsetting Feature</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Subsetting Feature</em>' reference.
     * @see #getSubsettingFeature()
     * @generated
     */
    void setSubsettingFeature(Feature value);

} // Subsetting
