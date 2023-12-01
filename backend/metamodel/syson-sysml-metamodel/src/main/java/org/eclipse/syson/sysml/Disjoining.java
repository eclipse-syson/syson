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
 * A representation of the model object '<em><b>Disjoining</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.Disjoining#getDisjoiningType <em>Disjoining Type</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Disjoining#getOwningType <em>Owning Type</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Disjoining#getTypeDisjoined <em>Type Disjoined</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getDisjoining()
 * @model
 * @generated
 */
public interface Disjoining extends Relationship {
    /**
     * Returns the value of the '<em><b>Disjoining Type</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Disjoining Type</em>' reference.
     * @see #setDisjoiningType(Type)
     * @see org.eclipse.syson.sysml.SysmlPackage#getDisjoining_DisjoiningType()
     * @model required="true" ordered="false"
     * @generated
     */
    Type getDisjoiningType();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Disjoining#getDisjoiningType <em>Disjoining Type</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Disjoining Type</em>' reference.
     * @see #getDisjoiningType()
     * @generated
     */
    void setDisjoiningType(Type value);

    /**
     * Returns the value of the '<em><b>Owning Type</b></em>' reference.
     * It is bidirectional and its opposite is '{@link org.eclipse.syson.sysml.Type#getOwnedDisjoining <em>Owned Disjoining</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Owning Type</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDisjoining_OwningType()
     * @see org.eclipse.syson.sysml.Type#getOwnedDisjoining
     * @model opposite="ownedDisjoining" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Type getOwningType();

    /**
     * Returns the value of the '<em><b>Type Disjoined</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type Disjoined</em>' reference.
     * @see #setTypeDisjoined(Type)
     * @see org.eclipse.syson.sysml.SysmlPackage#getDisjoining_TypeDisjoined()
     * @model required="true" ordered="false"
     * @generated
     */
    Type getTypeDisjoined();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Disjoining#getTypeDisjoined <em>Type Disjoined</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type Disjoined</em>' reference.
     * @see #getTypeDisjoined()
     * @generated
     */
    void setTypeDisjoined(Type value);

} // Disjoining
