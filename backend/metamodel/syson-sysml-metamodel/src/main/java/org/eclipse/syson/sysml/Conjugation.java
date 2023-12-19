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
 * A representation of the model object '<em><b>Conjugation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.Conjugation#getConjugatedType <em>Conjugated Type</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Conjugation#getOriginalType <em>Original Type</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Conjugation#getOwningType <em>Owning Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getConjugation()
 * @model
 * @generated
 */
public interface Conjugation extends Relationship {
    /**
     * Returns the value of the '<em><b>Conjugated Type</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Conjugated Type</em>' reference.
     * @see #setConjugatedType(Type)
     * @see org.eclipse.syson.sysml.SysmlPackage#getConjugation_ConjugatedType()
     * @model required="true" ordered="false"
     * @generated
     */
    Type getConjugatedType();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Conjugation#getConjugatedType <em>Conjugated Type</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Conjugated Type</em>' reference.
     * @see #getConjugatedType()
     * @generated
     */
    void setConjugatedType(Type value);

    /**
     * Returns the value of the '<em><b>Original Type</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Original Type</em>' reference.
     * @see #setOriginalType(Type)
     * @see org.eclipse.syson.sysml.SysmlPackage#getConjugation_OriginalType()
     * @model required="true" ordered="false"
     * @generated
     */
    Type getOriginalType();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Conjugation#getOriginalType <em>Original Type</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Original Type</em>' reference.
     * @see #getOriginalType()
     * @generated
     */
    void setOriginalType(Type value);

    /**
     * Returns the value of the '<em><b>Owning Type</b></em>' reference.
     * It is bidirectional and its opposite is '{@link org.eclipse.syson.sysml.Type#getOwnedConjugator <em>Owned Conjugator</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Owning Type</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getConjugation_OwningType()
     * @see org.eclipse.syson.sysml.Type#getOwnedConjugator
     * @model opposite="ownedConjugator" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Type getOwningType();

} // Conjugation
