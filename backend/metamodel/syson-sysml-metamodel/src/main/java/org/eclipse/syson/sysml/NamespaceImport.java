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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Namespace Import</b></em>'. <!-- end-user-doc
 * -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.NamespaceImport#getImportedNamespace <em>Imported Namespace</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getNamespaceImport()
 * @model
 * @generated
 */
public interface NamespaceImport extends Import {
    /**
     * Returns the value of the '<em><b>Imported Namespace</b></em>' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Imported Namespace</em>' reference.
     * @see #setImportedNamespace(Namespace)
     * @see org.eclipse.syson.sysml.SysmlPackage#getNamespaceImport_ImportedNamespace()
     * @model required="true" ordered="false" annotation="redefines"
     * @generated
     */
    Namespace getImportedNamespace();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.NamespaceImport#getImportedNamespace <em>Imported
     * Namespace</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Imported Namespace</em>' reference.
     * @see #getImportedNamespace()
     * @generated
     */
    void setImportedNamespace(Namespace value);

} // NamespaceImport
