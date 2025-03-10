/*******************************************************************************
* Copyright (c) 2023, 2025 Obeo.
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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Membership</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.Membership#getMemberElementId <em>Member Element Id</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Membership#getMemberName <em>Member Name</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Membership#getMemberShortName <em>Member Short Name</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Membership#getVisibility <em>Visibility</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Membership#getMemberElement <em>Member Element</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Membership#getMembershipOwningNamespace <em>Membership Owning Namespace</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getMembership()
 * @model
 * @generated
 */
public interface Membership extends Relationship {
    /**
     * Returns the value of the '<em><b>Member Element</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @return the value of the '<em>Member Element</em>' reference.
     * @see #setMemberElement(Element)
     * @see org.eclipse.syson.sysml.SysmlPackage#getMembership_MemberElement()
     * @model required="true" ordered="false"
     * @generated
     */
    Element getMemberElement();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Membership#getMemberElement <em>Member Element</em>}'
     * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Member Element</em>' reference.
     * @see #getMemberElement()
     * @generated
     */
    void setMemberElement(Element value);

    /**
     * Returns the value of the '<em><b>Member Element Id</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @return the value of the '<em>Member Element Id</em>' attribute.
     * @see org.eclipse.syson.sysml.SysmlPackage#getMembership_MemberElementId()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    String getMemberElementId();

    /**
     * Returns the value of the '<em><b>Member Name</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Member Name</em>' attribute.
     * @see #setMemberName(String)
     * @see org.eclipse.syson.sysml.SysmlPackage#getMembership_MemberName()
     * @model ordered="false"
     * @generated
     */
    String getMemberName();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Membership#getMemberName <em>Member Name</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Member Name</em>' attribute.
     * @see #getMemberName()
     * @generated
     */
    void setMemberName(String value);

    /**
     * Returns the value of the '<em><b>Membership Owning Namespace</b></em>' reference. It is bidirectional and its
     * opposite is '{@link org.eclipse.syson.sysml.Namespace#getOwnedMembership <em>Owned Membership</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Membership Owning Namespace</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getMembership_MembershipOwningNamespace()
     * @see org.eclipse.syson.sysml.Namespace#getOwnedMembership
     * @model opposite="ownedMembership" required="true" transient="true" changeable="false" volatile="true"
     *        derived="true" ordered="false"
     * @generated
     */
    Namespace getMembershipOwningNamespace();

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model ordered="false"
     * @generated
     */
    EList<Feature> allRedefinedFeatures();

    /**
     * Returns the value of the '<em><b>Member Short Name</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @return the value of the '<em>Member Short Name</em>' attribute.
     * @see #setMemberShortName(String)
     * @see org.eclipse.syson.sysml.SysmlPackage#getMembership_MemberShortName()
     * @model ordered="false"
     * @generated
     */
    String getMemberShortName();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Membership#getMemberShortName <em>Member Short Name</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Member Short Name</em>' attribute.
     * @see #getMemberShortName()
     * @generated
     */
    void setMemberShortName(String value);

    /**
     * Returns the value of the '<em><b>Visibility</b></em>' attribute. The default value is <code>"public"</code>. The
     * literals are from the enumeration {@link org.eclipse.syson.sysml.VisibilityKind}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Visibility</em>' attribute.
     * @see org.eclipse.syson.sysml.VisibilityKind
     * @see #setVisibility(VisibilityKind)
     * @see org.eclipse.syson.sysml.SysmlPackage#getMembership_Visibility()
     * @model default="public" required="true" ordered="false"
     * @generated
     */
    VisibilityKind getVisibility();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Membership#getVisibility <em>Visibility</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Visibility</em>' attribute.
     * @see org.eclipse.syson.sysml.VisibilityKind
     * @see #getVisibility()
     * @generated
     */
    void setVisibility(VisibilityKind value);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model required="true" ordered="false" otherRequired="true" otherOrdered="false"
     * @generated
     */
    boolean isDistinguishableFrom(Membership other);

} // Membership
