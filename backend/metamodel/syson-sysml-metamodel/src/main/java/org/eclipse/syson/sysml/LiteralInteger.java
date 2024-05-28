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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Literal Integer</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.LiteralInteger#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getLiteralInteger()
 * @model
 * @generated
 */
public interface LiteralInteger extends LiteralExpression {
    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Value</em>' attribute.
     * @see #setValue(int)
     * @see org.eclipse.syson.sysml.SysmlPackage#getLiteralInteger_Value()
     * @model required="true" ordered="false"
     * @generated
     */
    int getValue();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.LiteralInteger#getValue <em>Value</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Value</em>' attribute.
     * @see #getValue()
     * @generated
     */
    void setValue(int value);

} // LiteralInteger
