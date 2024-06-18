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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Featuring</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.Featuring#getFeature <em>Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Featuring#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getFeaturing()
 * @model abstract="true"
 * @generated
 */
public interface Featuring extends Relationship {
    /**
     * Returns the value of the '<em><b>Feature</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Feature</em>' reference.
     * @see #setFeature(Feature)
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeaturing_Feature()
     * @model required="true" ordered="false" annotation="subsets"
     * @generated
     */
    Feature getFeature();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Featuring#getFeature <em>Feature</em>}' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Feature</em>' reference.
     * @see #getFeature()
     * @generated
     */
    void setFeature(Feature value);

    /**
     * Returns the value of the '<em><b>Type</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Type</em>' reference.
     * @see #setType(Type)
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeaturing_Type()
     * @model required="true" ordered="false" annotation="subsets"
     * @generated
     */
    Type getType();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Featuring#getType <em>Type</em>}' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Type</em>' reference.
     * @see #getType()
     * @generated
     */
    void setType(Type value);

} // Featuring
