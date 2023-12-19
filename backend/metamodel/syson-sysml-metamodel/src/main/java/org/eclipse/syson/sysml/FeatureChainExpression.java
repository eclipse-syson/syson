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
 * A representation of the model object '<em><b>Feature Chain Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.FeatureChainExpression#getTargetFeature <em>Target Feature</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getFeatureChainExpression()
 * @model
 * @generated
 */
public interface FeatureChainExpression extends OperatorExpression {
    /**
     * Returns the value of the '<em><b>Target Feature</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Target Feature</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getFeatureChainExpression_TargetFeature()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Feature getTargetFeature();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model ordered="false"
     * @generated
     */
    Feature sourceTargetFeature();

} // FeatureChainExpression
