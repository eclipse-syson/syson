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


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Trigger Invocation Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.TriggerInvocationExpression#getKind <em>Kind</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getTriggerInvocationExpression()
 * @model
 * @generated
 */
public interface TriggerInvocationExpression extends InvocationExpression {
    /**
     * Returns the value of the '<em><b>Kind</b></em>' attribute.
     * The literals are from the enumeration {@link org.eclipse.syson.sysml.TriggerKind}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Kind</em>' attribute.
     * @see org.eclipse.syson.sysml.TriggerKind
     * @see #setKind(TriggerKind)
     * @see org.eclipse.syson.sysml.SysmlPackage#getTriggerInvocationExpression_Kind()
     * @model required="true" ordered="false"
     * @generated
     */
    TriggerKind getKind();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.TriggerInvocationExpression#getKind <em>Kind</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Kind</em>' attribute.
     * @see org.eclipse.syson.sysml.TriggerKind
     * @see #getKind()
     * @generated
     */
    void setKind(TriggerKind value);

} // TriggerInvocationExpression
