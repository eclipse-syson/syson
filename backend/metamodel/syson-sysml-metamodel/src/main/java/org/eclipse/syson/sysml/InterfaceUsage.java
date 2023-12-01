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
 * A representation of the model object '<em><b>Interface Usage</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.InterfaceUsage#getInterfaceDefinition <em>Interface Definition</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getInterfaceUsage()
 * @model
 * @generated
 */
public interface InterfaceUsage extends ConnectionUsage {
    /**
     * Returns the value of the '<em><b>Interface Definition</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.InterfaceDefinition}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Interface Definition</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getInterfaceUsage_InterfaceDefinition()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    EList<InterfaceDefinition> getInterfaceDefinition();

} // InterfaceUsage
