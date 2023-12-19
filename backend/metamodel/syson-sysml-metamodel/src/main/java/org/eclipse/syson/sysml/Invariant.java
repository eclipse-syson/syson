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
 * A representation of the model object '<em><b>Invariant</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.Invariant#isIsNegated <em>Is Negated</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getInvariant()
 * @model
 * @generated
 */
public interface Invariant extends BooleanExpression {
    /**
     * Returns the value of the '<em><b>Is Negated</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Is Negated</em>' attribute.
     * @see #setIsNegated(boolean)
     * @see org.eclipse.syson.sysml.SysmlPackage#getInvariant_IsNegated()
     * @model default="false" required="true" ordered="false"
     * @generated
     */
    boolean isIsNegated();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Invariant#isIsNegated <em>Is Negated</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Is Negated</em>' attribute.
     * @see #isIsNegated()
     * @generated
     */
    void setIsNegated(boolean value);

} // Invariant
