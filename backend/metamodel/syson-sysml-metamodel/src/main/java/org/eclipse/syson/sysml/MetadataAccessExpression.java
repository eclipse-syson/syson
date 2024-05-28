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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Metadata Access Expression</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.MetadataAccessExpression#getReferencedElement <em>Referenced Element</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getMetadataAccessExpression()
 * @model
 * @generated
 */
public interface MetadataAccessExpression extends Expression {
    /**
     * Returns the value of the '<em><b>Referenced Element</b></em>' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Referenced Element</em>' reference.
     * @see #setReferencedElement(Element)
     * @see org.eclipse.syson.sysml.SysmlPackage#getMetadataAccessExpression_ReferencedElement()
     * @model required="true" ordered="false"
     * @generated
     */
    Element getReferencedElement();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.MetadataAccessExpression#getReferencedElement
     * <em>Referenced Element</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Referenced Element</em>' reference.
     * @see #getReferencedElement()
     * @generated
     */
    void setReferencedElement(Element value);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model required="true" ordered="false"
     * @generated
     */
    MetadataFeature metaclassFeature();

} // MetadataAccessExpression
