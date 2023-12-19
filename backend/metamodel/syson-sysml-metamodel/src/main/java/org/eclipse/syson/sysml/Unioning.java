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
 * A representation of the model object '<em><b>Unioning</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.Unioning#getTypeUnioned <em>Type Unioned</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Unioning#getUnioningType <em>Unioning Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getUnioning()
 * @model
 * @generated
 */
public interface Unioning extends Relationship {
    /**
     * Returns the value of the '<em><b>Type Unioned</b></em>' reference.
     * It is bidirectional and its opposite is '{@link org.eclipse.syson.sysml.Type#getOwnedUnioning <em>Owned Unioning</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type Unioned</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUnioning_TypeUnioned()
     * @see org.eclipse.syson.sysml.Type#getOwnedUnioning
     * @model opposite="ownedUnioning" required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Type getTypeUnioned();

    /**
     * Returns the value of the '<em><b>Unioning Type</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Unioning Type</em>' reference.
     * @see #setUnioningType(Type)
     * @see org.eclipse.syson.sysml.SysmlPackage#getUnioning_UnioningType()
     * @model required="true" ordered="false"
     * @generated
     */
    Type getUnioningType();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Unioning#getUnioningType <em>Unioning Type</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Unioning Type</em>' reference.
     * @see #getUnioningType()
     * @generated
     */
    void setUnioningType(Type value);

} // Unioning
