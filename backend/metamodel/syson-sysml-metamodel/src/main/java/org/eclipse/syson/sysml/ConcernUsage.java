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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Concern Usage</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.ConcernUsage#getConcernDefinition <em>Concern Definition</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getConcernUsage()
 * @model
 * @generated
 */
public interface ConcernUsage extends RequirementUsage {
    /**
     * Returns the value of the '<em><b>Concern Definition</b></em>' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Concern Definition</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getConcernUsage_ConcernDefinition()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false" annotation="redefines"
     * @generated
     */
    ConcernDefinition getConcernDefinition();

} // ConcernUsage
