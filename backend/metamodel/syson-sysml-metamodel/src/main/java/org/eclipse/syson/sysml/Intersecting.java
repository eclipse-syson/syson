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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Intersecting</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.Intersecting#getIntersectingType <em>Intersecting Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Intersecting#getTypeIntersected <em>Type Intersected</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getIntersecting()
 * @model
 * @generated
 */
public interface Intersecting extends Relationship {
    /**
     * Returns the value of the '<em><b>Intersecting Type</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @return the value of the '<em>Intersecting Type</em>' reference.
     * @see #setIntersectingType(Type)
     * @see org.eclipse.syson.sysml.SysmlPackage#getIntersecting_IntersectingType()
     * @model required="true" ordered="false"
     * @generated
     */
    Type getIntersectingType();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Intersecting#getIntersectingType <em>Intersecting
     * Type</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Intersecting Type</em>' reference.
     * @see #getIntersectingType()
     * @generated
     */
    void setIntersectingType(Type value);

    /**
     * Returns the value of the '<em><b>Type Intersected</b></em>' reference. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.Type#getOwnedIntersecting <em>Owned Intersecting</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Type Intersected</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getIntersecting_TypeIntersected()
     * @see org.eclipse.syson.sysml.Type#getOwnedIntersecting
     * @model opposite="ownedIntersecting" required="true" transient="true" changeable="false" volatile="true"
     *        derived="true" ordered="false" annotation="redefines" annotation="subsets"
     * @generated
     */
    Type getTypeIntersected();

} // Intersecting
