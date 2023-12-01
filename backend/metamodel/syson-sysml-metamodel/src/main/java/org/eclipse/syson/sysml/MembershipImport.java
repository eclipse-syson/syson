/**
 * Copyright (c) 2023 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.syson.sysml;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Membership Import</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.MembershipImport#getImportedMembership <em>Imported Membership</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getMembershipImport()
 * @model
 * @generated
 */
public interface MembershipImport extends Import {
    /**
     * Returns the value of the '<em><b>Imported Membership</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Imported Membership</em>' reference.
     * @see #setImportedMembership(Membership)
     * @see org.eclipse.syson.sysml.SysmlPackage#getMembershipImport_ImportedMembership()
     * @model required="true" ordered="false"
     * @generated
     */
    Membership getImportedMembership();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.MembershipImport#getImportedMembership <em>Imported Membership</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Imported Membership</em>' reference.
     * @see #getImportedMembership()
     * @generated
     */
    void setImportedMembership(Membership value);

} // MembershipImport
