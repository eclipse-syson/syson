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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Namespace</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.Namespace#getImportedMembership <em>Imported Membership</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Namespace#getMember <em>Member</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Namespace#getMembership <em>Membership</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Namespace#getOwnedImport <em>Owned Import</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Namespace#getOwnedMember <em>Owned Member</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Namespace#getOwnedMembership <em>Owned Membership</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getNamespace()
 * @model
 * @generated
 */
public interface Namespace extends Element {
    /**
     * Returns the value of the '<em><b>Imported Membership</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Membership}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Imported Membership</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getNamespace_ImportedMembership()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<Membership> getImportedMembership();

    /**
     * Returns the value of the '<em><b>Member</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Element}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Member</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getNamespace_Member()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Element> getMember();

    /**
     * Returns the value of the '<em><b>Membership</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Membership}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Membership</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getNamespace_Membership()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Membership> getMembership();

    /**
     * Returns the value of the '<em><b>Owned Import</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Import}. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.Import#getImportOwningNamespace <em>Import Owning Namespace</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Import</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getNamespace_OwnedImport()
     * @see org.eclipse.syson.sysml.Import#getImportOwningNamespace
     * @model opposite="importOwningNamespace" transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Import> getOwnedImport();

    /**
     * Returns the value of the '<em><b>Owned Member</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Element}. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.Element#getOwningNamespace <em>Owning Namespace</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Member</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getNamespace_OwnedMember()
     * @see org.eclipse.syson.sysml.Element#getOwningNamespace
     * @model opposite="owningNamespace" transient="true" changeable="false" volatile="true" derived="true"
     *        annotation="subsets"
     * @generated
     */
    EList<Element> getOwnedMember();

    /**
     * Returns the value of the '<em><b>Owned Membership</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Membership}. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.Membership#getMembershipOwningNamespace <em>Membership Owning Namespace</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Membership</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getNamespace_OwnedMembership()
     * @see org.eclipse.syson.sysml.Membership#getMembershipOwningNamespace
     * @model opposite="membershipOwningNamespace" transient="true" changeable="false" volatile="true" derived="true"
     *        annotation="subsets"
     * @generated
     */
    EList<Membership> getOwnedMembership();

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model excludedMany="true" excludedOrdered="false"
     * @generated
     */
    EList<Membership> importedMemberships(EList<Namespace> excluded);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model ordered="false" visibilityOrdered="false" excludedMany="true" excludedOrdered="false"
     * @generated
     */
    EList<Membership> membershipsOfVisibility(VisibilityKind visibility, EList<Namespace> excluded);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model ordered="false" elementRequired="true" elementOrdered="false"
     * @generated
     */
    EList<String> namesOf(Element element);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model ordered="false" qualifiedNameRequired="true" qualifiedNameOrdered="false"
     * @generated
     */
    String qualificationOf(String qualifiedName);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model ordered="false" qualifiedNameRequired="true" qualifiedNameOrdered="false"
     * @generated
     */
    Membership resolve(String qualifiedName);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model ordered="false" qualifiedNameRequired="true" qualifiedNameOrdered="false"
     * @generated
     */
    Membership resolveGlobal(String qualifiedName);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model ordered="false" nameRequired="true" nameOrdered="false"
     * @generated
     */
    Membership resolveLocal(String name);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model ordered="false" nameRequired="true" nameOrdered="false"
     * @generated
     */
    Membership resolveVisible(String name);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model required="true" ordered="false" qualifiedNameRequired="true" qualifiedNameOrdered="false"
     * @generated
     */
    String unqualifiedNameOf(String qualifiedName);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model required="true" ordered="false" memRequired="true" memOrdered="false"
     * @generated
     */
    VisibilityKind visibilityOf(Membership mem);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model excludedMany="true" excludedOrdered="false" isRecursiveRequired="true" isRecursiveOrdered="false"
     *        includeAllRequired="true" includeAllOrdered="false"
     * @generated
     */
    EList<Membership> visibleMemberships(EList<Namespace> excluded, boolean isRecursive, boolean includeAll);

} // Namespace
