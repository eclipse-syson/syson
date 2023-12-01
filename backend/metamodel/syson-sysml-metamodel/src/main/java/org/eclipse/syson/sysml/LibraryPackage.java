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
 * A representation of the model object '<em><b>Library Package</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.LibraryPackage#isIsStandard <em>Is Standard</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getLibraryPackage()
 * @model
 * @generated
 */
public interface LibraryPackage extends org.eclipse.syson.sysml.Package {
    /**
     * Returns the value of the '<em><b>Is Standard</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Is Standard</em>' attribute.
     * @see #setIsStandard(boolean)
     * @see org.eclipse.syson.sysml.SysmlPackage#getLibraryPackage_IsStandard()
     * @model default="false" required="true" ordered="false"
     * @generated
     */
    boolean isIsStandard();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.LibraryPackage#isIsStandard <em>Is Standard</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Is Standard</em>' attribute.
     * @see #isIsStandard()
     * @generated
     */
    void setIsStandard(boolean value);

} // LibraryPackage
