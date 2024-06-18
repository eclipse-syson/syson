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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Specialization</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.Specialization#getGeneral <em>General</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Specialization#getOwningType <em>Owning Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Specialization#getSpecific <em>Specific</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getSpecialization()
 * @model
 * @generated
 */
public interface Specialization extends Relationship {
    /**
     * Returns the value of the '<em><b>General</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>General</em>' reference.
     * @see #setGeneral(Type)
     * @see org.eclipse.syson.sysml.SysmlPackage#getSpecialization_General()
     * @model required="true" ordered="false" annotation="redefines"
     * @generated
     */
    Type getGeneral();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Specialization#getGeneral <em>General</em>}' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>General</em>' reference.
     * @see #getGeneral()
     * @generated
     */
    void setGeneral(Type value);

    /**
     * Returns the value of the '<em><b>Owning Type</b></em>' reference. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.Type#getOwnedSpecialization <em>Owned Specialization</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owning Type</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getSpecialization_OwningType()
     * @see org.eclipse.syson.sysml.Type#getOwnedSpecialization
     * @model opposite="ownedSpecialization" transient="true" changeable="false" volatile="true" derived="true"
     *        ordered="false" annotation="subsets"
     * @generated
     */
    Type getOwningType();

    /**
     * Returns the value of the '<em><b>Specific</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Specific</em>' reference.
     * @see #setSpecific(Type)
     * @see org.eclipse.syson.sysml.SysmlPackage#getSpecialization_Specific()
     * @model required="true" ordered="false" annotation="redefines"
     * @generated
     */
    Type getSpecific();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Specialization#getSpecific <em>Specific</em>}' reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Specific</em>' reference.
     * @see #getSpecific()
     * @generated
     */
    void setSpecific(Type value);

} // Specialization
