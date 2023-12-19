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
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Calculation Usage</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.CalculationUsage#getCalculationDefinition <em>Calculation Definition</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getCalculationUsage()
 * @model
 * @generated
 */
public interface CalculationUsage extends ActionUsage, Expression {
    /**
     * Returns the value of the '<em><b>Calculation Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Calculation Definition</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getCalculationUsage_CalculationDefinition()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    Function getCalculationDefinition();

} // CalculationUsage
