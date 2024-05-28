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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Use Case Usage</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.UseCaseUsage#getIncludedUseCase <em>Included Use Case</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.UseCaseUsage#getUseCaseDefinition <em>Use Case Definition</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getUseCaseUsage()
 * @model
 * @generated
 */
public interface UseCaseUsage extends CaseUsage {
    /**
     * Returns the value of the '<em><b>Included Use Case</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.UseCaseUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Included Use Case</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUseCaseUsage_IncludedUseCase()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<UseCaseUsage> getIncludedUseCase();

    /**
     * Returns the value of the '<em><b>Use Case Definition</b></em>' reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Use Case Definition</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUseCaseUsage_UseCaseDefinition()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    UseCaseDefinition getUseCaseDefinition();

} // UseCaseUsage
