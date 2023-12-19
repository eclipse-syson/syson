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
 * A representation of the model object '<em><b>Textual Representation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.TextualRepresentation#getBody <em>Body</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.TextualRepresentation#getLanguage <em>Language</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.TextualRepresentation#getRepresentedElement <em>Represented Element</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getTextualRepresentation()
 * @model
 * @generated
 */
public interface TextualRepresentation extends AnnotatingElement {
    /**
     * Returns the value of the '<em><b>Body</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Body</em>' attribute.
     * @see #setBody(String)
     * @see org.eclipse.syson.sysml.SysmlPackage#getTextualRepresentation_Body()
     * @model required="true" ordered="false"
     * @generated
     */
    String getBody();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.TextualRepresentation#getBody <em>Body</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Body</em>' attribute.
     * @see #getBody()
     * @generated
     */
    void setBody(String value);

    /**
     * Returns the value of the '<em><b>Language</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Language</em>' attribute.
     * @see #setLanguage(String)
     * @see org.eclipse.syson.sysml.SysmlPackage#getTextualRepresentation_Language()
     * @model required="true" ordered="false"
     * @generated
     */
    String getLanguage();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.TextualRepresentation#getLanguage <em>Language</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Language</em>' attribute.
     * @see #getLanguage()
     * @generated
     */
    void setLanguage(String value);

    /**
     * Returns the value of the '<em><b>Represented Element</b></em>' reference.
     * It is bidirectional and its opposite is '{@link org.eclipse.syson.sysml.Element#getTextualRepresentation <em>Textual Representation</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Represented Element</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getTextualRepresentation_RepresentedElement()
     * @see org.eclipse.syson.sysml.Element#getTextualRepresentation
     * @model opposite="textualRepresentation" required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Element getRepresentedElement();

} // TextualRepresentation
