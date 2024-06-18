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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Differencing</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.Differencing#getDifferencingType <em>Differencing Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Differencing#getTypeDifferenced <em>Type Differenced</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getDifferencing()
 * @model
 * @generated
 */
public interface Differencing extends Relationship {
    /**
     * Returns the value of the '<em><b>Differencing Type</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @return the value of the '<em>Differencing Type</em>' reference.
     * @see #setDifferencingType(Type)
     * @see org.eclipse.syson.sysml.SysmlPackage#getDifferencing_DifferencingType()
     * @model required="true" ordered="false"
     * @generated
     */
    Type getDifferencingType();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Differencing#getDifferencingType <em>Differencing
     * Type</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Differencing Type</em>' reference.
     * @see #getDifferencingType()
     * @generated
     */
    void setDifferencingType(Type value);

    /**
     * Returns the value of the '<em><b>Type Differenced</b></em>' reference. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.Type#getOwnedDifferencing <em>Owned Differencing</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Type Differenced</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDifferencing_TypeDifferenced()
     * @see org.eclipse.syson.sysml.Type#getOwnedDifferencing
     * @model opposite="ownedDifferencing" required="true" transient="true" changeable="false" volatile="true"
     *        derived="true" ordered="false" annotation="redefines" annotation="subsets"
     * @generated
     */
    Type getTypeDifferenced();

} // Differencing
