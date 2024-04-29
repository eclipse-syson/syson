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
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.Element#getAliasIds <em>Alias Ids</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Element#getDeclaredName <em>Declared Name</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Element#getDeclaredShortName <em>Declared Short Name</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Element#getElementId <em>Element Id</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Element#isIsImpliedIncluded <em>Is Implied Included</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Element#isIsLibraryElement <em>Is Library Element</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Element#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Element#getQualifiedName <em>Qualified Name</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Element#getShortName <em>Short Name</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Element#getDocumentation <em>Documentation</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Element#getOwnedAnnotation <em>Owned Annotation</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Element#getOwnedElement <em>Owned Element</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Element#getOwnedRelationship <em>Owned Relationship</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Element#getOwner <em>Owner</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Element#getOwningMembership <em>Owning Membership</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Element#getOwningNamespace <em>Owning Namespace</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Element#getOwningRelationship <em>Owning Relationship</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Element#getTextualRepresentation <em>Textual Representation</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getElement()
 * @model abstract="true"
 * @generated
 */
public interface Element extends EObject {
    /**
     * Returns the value of the '<em><b>Alias Ids</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Alias Ids</em>' attribute list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getElement_AliasIds()
     * @model
     * @generated
     */
    EList<String> getAliasIds();

    /**
     * Returns the value of the '<em><b>Declared Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Declared Name</em>' attribute.
     * @see #setDeclaredName(String)
     * @see org.eclipse.syson.sysml.SysmlPackage#getElement_DeclaredName()
     * @model ordered="false"
     * @generated
     */
    String getDeclaredName();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Element#getDeclaredName <em>Declared Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Declared Name</em>' attribute.
     * @see #getDeclaredName()
     * @generated
     */
    void setDeclaredName(String value);

    /**
     * Returns the value of the '<em><b>Declared Short Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Declared Short Name</em>' attribute.
     * @see #setDeclaredShortName(String)
     * @see org.eclipse.syson.sysml.SysmlPackage#getElement_DeclaredShortName()
     * @model ordered="false"
     * @generated
     */
    String getDeclaredShortName();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Element#getDeclaredShortName <em>Declared Short Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Declared Short Name</em>' attribute.
     * @see #getDeclaredShortName()
     * @generated
     */
    void setDeclaredShortName(String value);

    /**
     * Returns the value of the '<em><b>Documentation</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.Documentation}.
     * It is bidirectional and its opposite is '{@link org.eclipse.syson.sysml.Documentation#getDocumentedElement <em>Documented Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Documentation</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getElement_Documentation()
     * @see org.eclipse.syson.sysml.Documentation#getDocumentedElement
     * @model opposite="documentedElement" transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Documentation> getDocumentation();

    /**
     * Returns the value of the '<em><b>Element Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Element Id</em>' attribute.
     * @see #setElementId(String)
     * @see org.eclipse.syson.sysml.SysmlPackage#getElement_ElementId()
     * @model id="true" required="true" ordered="false"
     * @generated
     */
    String getElementId();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Element#getElementId <em>Element Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Element Id</em>' attribute.
     * @see #getElementId()
     * @generated
     */
    void setElementId(String value);

    /**
     * Returns the value of the '<em><b>Is Implied Included</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Is Implied Included</em>' attribute.
     * @see #setIsImpliedIncluded(boolean)
     * @see org.eclipse.syson.sysml.SysmlPackage#getElement_IsImpliedIncluded()
     * @model default="false" required="true" ordered="false"
     * @generated
     */
    boolean isIsImpliedIncluded();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Element#isIsImpliedIncluded <em>Is Implied Included</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Is Implied Included</em>' attribute.
     * @see #isIsImpliedIncluded()
     * @generated
     */
    void setIsImpliedIncluded(boolean value);

    /**
     * Returns the value of the '<em><b>Is Library Element</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Is Library Element</em>' attribute.
     * @see org.eclipse.syson.sysml.SysmlPackage#getElement_IsLibraryElement()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    boolean isIsLibraryElement();

    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see org.eclipse.syson.sysml.SysmlPackage#getElement_Name()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    String getName();

    /**
     * Returns the value of the '<em><b>Owned Annotation</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.Annotation}.
     * It is bidirectional and its opposite is '{@link org.eclipse.syson.sysml.Annotation#getOwningAnnotatedElement <em>Owning Annotated Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Owned Annotation</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getElement_OwnedAnnotation()
     * @see org.eclipse.syson.sysml.Annotation#getOwningAnnotatedElement
     * @model opposite="owningAnnotatedElement" transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Annotation> getOwnedAnnotation();

    /**
     * Returns the value of the '<em><b>Owned Element</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.Element}.
     * It is bidirectional and its opposite is '{@link org.eclipse.syson.sysml.Element#getOwner <em>Owner</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Owned Element</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getElement_OwnedElement()
     * @see org.eclipse.syson.sysml.Element#getOwner
     * @model opposite="owner" transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Element> getOwnedElement();

    /**
     * Returns the value of the '<em><b>Owned Relationship</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.Relationship}.
     * It is bidirectional and its opposite is '{@link org.eclipse.syson.sysml.Relationship#getOwningRelatedElement <em>Owning Related Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Owned Relationship</em>' containment reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getElement_OwnedRelationship()
     * @see org.eclipse.syson.sysml.Relationship#getOwningRelatedElement
     * @model opposite="owningRelatedElement" containment="true"
     * @generated
     */
    EList<Relationship> getOwnedRelationship();

    /**
     * Returns the value of the '<em><b>Owner</b></em>' reference.
     * It is bidirectional and its opposite is '{@link org.eclipse.syson.sysml.Element#getOwnedElement <em>Owned Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Owner</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getElement_Owner()
     * @see org.eclipse.syson.sysml.Element#getOwnedElement
     * @model opposite="ownedElement" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Element getOwner();

    /**
     * Returns the value of the '<em><b>Owning Membership</b></em>' reference.
     * It is bidirectional and its opposite is '{@link org.eclipse.syson.sysml.OwningMembership#getOwnedMemberElement <em>Owned Member Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Owning Membership</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getElement_OwningMembership()
     * @see org.eclipse.syson.sysml.OwningMembership#getOwnedMemberElement
     * @model opposite="ownedMemberElement" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    OwningMembership getOwningMembership();

    /**
     * Returns the value of the '<em><b>Owning Namespace</b></em>' reference.
     * It is bidirectional and its opposite is '{@link org.eclipse.syson.sysml.Namespace#getOwnedMember <em>Owned Member</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Owning Namespace</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getElement_OwningNamespace()
     * @see org.eclipse.syson.sysml.Namespace#getOwnedMember
     * @model opposite="ownedMember" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Namespace getOwningNamespace();

    /**
     * Returns the value of the '<em><b>Owning Relationship</b></em>' container reference.
     * It is bidirectional and its opposite is '{@link org.eclipse.syson.sysml.Relationship#getOwnedRelatedElement <em>Owned Related Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Owning Relationship</em>' container reference.
     * @see #setOwningRelationship(Relationship)
     * @see org.eclipse.syson.sysml.SysmlPackage#getElement_OwningRelationship()
     * @see org.eclipse.syson.sysml.Relationship#getOwnedRelatedElement
     * @model opposite="ownedRelatedElement" transient="false" ordered="false"
     * @generated
     */
    Relationship getOwningRelationship();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Element#getOwningRelationship <em>Owning Relationship</em>}' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Owning Relationship</em>' container reference.
     * @see #getOwningRelationship()
     * @generated
     */
    void setOwningRelationship(Relationship value);

    /**
     * Returns the value of the '<em><b>Qualified Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Qualified Name</em>' attribute.
     * @see org.eclipse.syson.sysml.SysmlPackage#getElement_QualifiedName()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    String getQualifiedName();

    /**
     * Returns the value of the '<em><b>Short Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Short Name</em>' attribute.
     * @see org.eclipse.syson.sysml.SysmlPackage#getElement_ShortName()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    String getShortName();

    /**
     * Returns the value of the '<em><b>Textual Representation</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.TextualRepresentation}.
     * It is bidirectional and its opposite is '{@link org.eclipse.syson.sysml.TextualRepresentation#getRepresentedElement <em>Represented Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Textual Representation</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getElement_TextualRepresentation()
     * @see org.eclipse.syson.sysml.TextualRepresentation#getRepresentedElement
     * @model opposite="representedElement" transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<TextualRepresentation> getTextualRepresentation();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model ordered="false"
     * @generated
     */
    String effectiveName();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model ordered="false"
     * @generated
     */
    String effectiveShortName();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model ordered="false"
     * @generated
     */
    String escapedName();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model ordered="false"
     * @generated
     */
    Namespace libraryNamespace();

} // Element
