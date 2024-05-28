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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Allocation Definition</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.AllocationDefinition#getAllocation <em>Allocation</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getAllocationDefinition()
 * @model
 * @generated
 */
public interface AllocationDefinition extends ConnectionDefinition {
    /**
     * Returns the value of the '<em><b>Allocation</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.AllocationUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Allocation</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getAllocationDefinition_Allocation()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<AllocationUsage> getAllocation();

} // AllocationDefinition
