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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Occurrence Definition</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.OccurrenceDefinition#isIsIndividual <em>Is Individual</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.OccurrenceDefinition#getLifeClass <em>Life Class</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getOccurrenceDefinition()
 * @model
 * @generated
 */
public interface OccurrenceDefinition extends Definition, org.eclipse.syson.sysml.Class {
    /**
     * Returns the value of the '<em><b>Is Individual</b></em>' attribute. The default value is <code>"false"</code>.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Is Individual</em>' attribute.
     * @see #setIsIndividual(boolean)
     * @see org.eclipse.syson.sysml.SysmlPackage#getOccurrenceDefinition_IsIndividual()
     * @model default="false" required="true" ordered="false"
     * @generated
     */
    boolean isIsIndividual();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.OccurrenceDefinition#isIsIndividual <em>Is
     * Individual</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Is Individual</em>' attribute.
     * @see #isIsIndividual()
     * @generated
     */
    void setIsIndividual(boolean value);

    /**
     * Returns the value of the '<em><b>Life Class</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Life Class</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getOccurrenceDefinition_LifeClass()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    LifeClass getLifeClass();

} // OccurrenceDefinition
