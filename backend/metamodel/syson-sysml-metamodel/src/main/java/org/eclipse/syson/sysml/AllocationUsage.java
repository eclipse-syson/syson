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
 * A representation of the model object '<em><b>Allocation Usage</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.AllocationUsage#getAllocationDefinition <em>Allocation Definition</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getAllocationUsage()
 * @model
 * @generated
 */
public interface AllocationUsage extends ConnectionUsage {
    /**
     * Returns the value of the '<em><b>Allocation Definition</b></em>' reference list.
     * The list contents are of type {@link org.eclipse.syson.sysml.AllocationDefinition}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Allocation Definition</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getAllocationUsage_AllocationDefinition()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<AllocationDefinition> getAllocationDefinition();

} // AllocationUsage
