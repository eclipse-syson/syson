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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Enumeration Definition</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.EnumerationDefinition#getEnumeratedValue <em>Enumerated Value</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getEnumerationDefinition()
 * @model
 * @generated
 */
public interface EnumerationDefinition extends AttributeDefinition {
    /**
     * Returns the value of the '<em><b>Enumerated Value</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.EnumerationUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Enumerated Value</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getEnumerationDefinition_EnumeratedValue()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<EnumerationUsage> getEnumeratedValue();

} // EnumerationDefinition
