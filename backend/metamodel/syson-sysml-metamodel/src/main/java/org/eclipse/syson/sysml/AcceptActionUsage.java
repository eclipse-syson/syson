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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Accept Action Usage</b></em>'. <!-- end-user-doc
 * -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.AcceptActionUsage#getPayloadArgument <em>Payload Argument</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.AcceptActionUsage#getPayloadParameter <em>Payload Parameter</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.AcceptActionUsage#getReceiverArgument <em>Receiver Argument</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getAcceptActionUsage()
 * @model
 * @generated
 */
public interface AcceptActionUsage extends ActionUsage {
    /**
     * Returns the value of the '<em><b>Payload Argument</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @return the value of the '<em>Payload Argument</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getAcceptActionUsage_PayloadArgument()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Expression getPayloadArgument();

    /**
     * Returns the value of the '<em><b>Payload Parameter</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @return the value of the '<em>Payload Parameter</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getAcceptActionUsage_PayloadParameter()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    ReferenceUsage getPayloadParameter();

    /**
     * Returns the value of the '<em><b>Receiver Argument</b></em>' reference. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @return the value of the '<em>Receiver Argument</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getAcceptActionUsage_ReceiverArgument()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    Expression getReceiverArgument();

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model kind="operation" required="true" ordered="false"
     * @generated
     */
    boolean isTriggerAction();

} // AcceptActionUsage
