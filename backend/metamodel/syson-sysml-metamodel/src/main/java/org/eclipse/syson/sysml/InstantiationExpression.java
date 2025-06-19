/**
 * Copyright (c) 2023, 2025 Obeo.
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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Instantiation Expression</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.InstantiationExpression#getArgument <em>Argument</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.InstantiationExpression#getInstantiatedType <em>Instantiated Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getInstantiationExpression()
 * @model abstract="true"
 * @generated
 */
public interface InstantiationExpression extends Expression {
    /**
     * Returns the value of the '<em><b>Argument</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Expression}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Argument</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getInstantiationExpression_Argument()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    EList<Expression> getArgument();

    /**
     * Returns the value of the '<em><b>Instantiated Type</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @return the value of the '<em>Instantiated Type</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getInstantiationExpression_InstantiatedType()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     *        annotation="subsets"
     * @generated
     */
    Type getInstantiatedType();

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model ordered="false"
     * @generated
     */
    Type instantiatedType();

} // InstantiationExpression
