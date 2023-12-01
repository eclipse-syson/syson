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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dependency</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.Dependency#getClient <em>Client</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.Dependency#getSupplier <em>Supplier</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getDependency()
 * @model
 * @generated
 */
public interface Dependency extends Relationship {
    /**
     * Returns the value of the '<em><b>Client</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.Element}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Client</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDependency_Client()
     * @model required="true"
     * @generated
     */
    EList<Element> getClient();

    /**
     * Returns the value of the '<em><b>Supplier</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.Element}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Supplier</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDependency_Supplier()
     * @model required="true"
     * @generated
     */
    EList<Element> getSupplier();

} // Dependency
