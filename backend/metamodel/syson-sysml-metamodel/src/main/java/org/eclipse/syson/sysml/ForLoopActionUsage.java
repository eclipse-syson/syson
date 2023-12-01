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
 * A representation of the model object '<em><b>For Loop Action Usage</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.ForLoopActionUsage#getLoopVariable <em>Loop Variable</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.ForLoopActionUsage#getSeqArgument <em>Seq Argument</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getForLoopActionUsage()
 * @model
 * @generated
 */
public interface ForLoopActionUsage extends LoopActionUsage {
    /**
     * Returns the value of the '<em><b>Loop Variable</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Loop Variable</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getForLoopActionUsage_LoopVariable()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    ReferenceUsage getLoopVariable();

    /**
     * Returns the value of the '<em><b>Seq Argument</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Seq Argument</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getForLoopActionUsage_SeqArgument()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Expression getSeqArgument();

} // ForLoopActionUsage
