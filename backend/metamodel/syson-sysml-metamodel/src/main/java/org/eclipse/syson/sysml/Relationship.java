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
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Relationship</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.Relationship#isIsImplied <em>Is Implied</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Relationship#getOwnedRelatedElement <em>Owned Related Element</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Relationship#getOwningRelatedElement <em>Owning Related Element</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Relationship#getRelatedElement <em>Related Element</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Relationship#getSource <em>Source</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Relationship#getTarget <em>Target</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getRelationship()
 * @model abstract="true"
 * @generated
 */
public interface Relationship extends Element {
    /**
     * Returns the value of the '<em><b>Is Implied</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Is Implied</em>' attribute.
     * @see #setIsImplied(boolean)
     * @see org.eclipse.syson.sysml.SysmlPackage#getRelationship_IsImplied()
     * @model default="false" required="true" ordered="false"
     * @generated
     */
    boolean isIsImplied();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Relationship#isIsImplied <em>Is Implied</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Is Implied</em>' attribute.
     * @see #isIsImplied()
     * @generated
     */
    void setIsImplied(boolean value);

    /**
     * Returns the value of the '<em><b>Owned Related Element</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.Element}.
     * It is bidirectional and its opposite is '{@link org.eclipse.syson.sysml.Element#getOwningRelationship <em>Owning Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Owned Related Element</em>' containment reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getRelationship_OwnedRelatedElement()
     * @see org.eclipse.syson.sysml.Element#getOwningRelationship
     * @model opposite="owningRelationship" containment="true"
     * @generated
     */
    EList<Element> getOwnedRelatedElement();

    /**
     * Returns the value of the '<em><b>Owning Related Element</b></em>' container reference.
     * It is bidirectional and its opposite is '{@link org.eclipse.syson.sysml.Element#getOwnedRelationship <em>Owned Relationship</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Owning Related Element</em>' container reference.
     * @see #setOwningRelatedElement(Element)
     * @see org.eclipse.syson.sysml.SysmlPackage#getRelationship_OwningRelatedElement()
     * @see org.eclipse.syson.sysml.Element#getOwnedRelationship
     * @model opposite="ownedRelationship" transient="false" ordered="false"
     * @generated
     */
    Element getOwningRelatedElement();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Relationship#getOwningRelatedElement <em>Owning Related Element</em>}' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Owning Related Element</em>' container reference.
     * @see #getOwningRelatedElement()
     * @generated
     */
    void setOwningRelatedElement(Element value);

    /**
     * Returns the value of the '<em><b>Related Element</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.Element}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Related Element</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getRelationship_RelatedElement()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Element> getRelatedElement();

    /**
     * Returns the value of the '<em><b>Source</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.Element}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Source</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getRelationship_Source()
     * @model
     * @generated
     */
    EList<Element> getSource();

    /**
     * Returns the value of the '<em><b>Target</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.Element}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Target</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getRelationship_Target()
     * @model
     * @generated
     */
    EList<Element> getTarget();

} // Relationship
