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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Occurrence Usage</b></em>'. <!-- end-user-doc
 * -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.OccurrenceUsage#isIsIndividual <em>Is Individual</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.OccurrenceUsage#getPortionKind <em>Portion Kind</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.OccurrenceUsage#getIndividualDefinition <em>Individual Definition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.OccurrenceUsage#getOccurrenceDefinition <em>Occurrence Definition</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getOccurrenceUsage()
 * @model
 * @generated
 */
public interface OccurrenceUsage extends Usage {
    /**
     * Returns the value of the '<em><b>Individual Definition</b></em>' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Individual Definition</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getOccurrenceUsage_IndividualDefinition()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    OccurrenceDefinition getIndividualDefinition();

    /**
     * Returns the value of the '<em><b>Is Individual</b></em>' attribute. The default value is <code>"false"</code>.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Is Individual</em>' attribute.
     * @see #setIsIndividual(boolean)
     * @see org.eclipse.syson.sysml.SysmlPackage#getOccurrenceUsage_IsIndividual()
     * @model default="false" required="true" ordered="false"
     * @generated
     */
    boolean isIsIndividual();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.OccurrenceUsage#isIsIndividual <em>Is Individual</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Is Individual</em>' attribute.
     * @see #isIsIndividual()
     * @generated
     */
    void setIsIndividual(boolean value);

    /**
     * Returns the value of the '<em><b>Occurrence Definition</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Class}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Occurrence Definition</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getOccurrenceUsage_OccurrenceDefinition()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<org.eclipse.syson.sysml.Class> getOccurrenceDefinition();

    /**
     * Returns the value of the '<em><b>Portion Kind</b></em>' attribute. The literals are from the enumeration
     * {@link org.eclipse.syson.sysml.PortionKind}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Portion Kind</em>' attribute.
     * @see org.eclipse.syson.sysml.PortionKind
     * @see #setPortionKind(PortionKind)
     * @see org.eclipse.syson.sysml.SysmlPackage#getOccurrenceUsage_PortionKind()
     * @model ordered="false"
     * @generated
     */
    PortionKind getPortionKind();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.OccurrenceUsage#getPortionKind <em>Portion Kind</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Portion Kind</em>' attribute.
     * @see org.eclipse.syson.sysml.PortionKind
     * @see #getPortionKind()
     * @generated
     */
    void setPortionKind(PortionKind value);

} // OccurrenceUsage
