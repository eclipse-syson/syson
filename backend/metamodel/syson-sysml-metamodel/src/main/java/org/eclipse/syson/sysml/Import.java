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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Import</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.Import#isIsImportAll <em>Is Import All</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Import#isIsRecursive <em>Is Recursive</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Import#getVisibility <em>Visibility</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Import#getImportedElement <em>Imported Element</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Import#getImportOwningNamespace <em>Import Owning Namespace</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getImport()
 * @model abstract="true"
 * @generated
 */
public interface Import extends Relationship {
    /**
     * Returns the value of the '<em><b>Imported Element</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @return the value of the '<em>Imported Element</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getImport_ImportedElement()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Element getImportedElement();

    /**
     * Returns the value of the '<em><b>Import Owning Namespace</b></em>' reference. It is bidirectional and its
     * opposite is '{@link org.eclipse.syson.sysml.Namespace#getOwnedImport <em>Owned Import</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Import Owning Namespace</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getImport_ImportOwningNamespace()
     * @see org.eclipse.syson.sysml.Namespace#getOwnedImport
     * @model opposite="ownedImport" required="true" transient="true" changeable="false" volatile="true" derived="true"
     *        ordered="false" annotation="redefines" annotation="subsets"
     * @generated
     */
    Namespace getImportOwningNamespace();

    /**
     * Returns the value of the '<em><b>Is Import All</b></em>' attribute. The default value is <code>"false"</code>.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Is Import All</em>' attribute.
     * @see #setIsImportAll(boolean)
     * @see org.eclipse.syson.sysml.SysmlPackage#getImport_IsImportAll()
     * @model default="false" required="true" ordered="false"
     * @generated
     */
    boolean isIsImportAll();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Import#isIsImportAll <em>Is Import All</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Is Import All</em>' attribute.
     * @see #isIsImportAll()
     * @generated
     */
    void setIsImportAll(boolean value);

    /**
     * Returns the value of the '<em><b>Is Recursive</b></em>' attribute. The default value is <code>"false"</code>.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Is Recursive</em>' attribute.
     * @see #setIsRecursive(boolean)
     * @see org.eclipse.syson.sysml.SysmlPackage#getImport_IsRecursive()
     * @model default="false" required="true" ordered="false"
     * @generated
     */
    boolean isIsRecursive();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Import#isIsRecursive <em>Is Recursive</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Is Recursive</em>' attribute.
     * @see #isIsRecursive()
     * @generated
     */
    void setIsRecursive(boolean value);

    /**
     * Returns the value of the '<em><b>Visibility</b></em>' attribute. The default value is <code>"public"</code>. The
     * literals are from the enumeration {@link org.eclipse.syson.sysml.VisibilityKind}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Visibility</em>' attribute.
     * @see org.eclipse.syson.sysml.VisibilityKind
     * @see #setVisibility(VisibilityKind)
     * @see org.eclipse.syson.sysml.SysmlPackage#getImport_Visibility()
     * @model default="public" required="true" ordered="false"
     * @generated
     */
    VisibilityKind getVisibility();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Import#getVisibility <em>Visibility</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
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
     * @model excludedMany="true" excludedOrdered="false"
     * @generated
     */
    EList<Membership> importedMemberships(EList<Namespace> excluded);

} // Import
