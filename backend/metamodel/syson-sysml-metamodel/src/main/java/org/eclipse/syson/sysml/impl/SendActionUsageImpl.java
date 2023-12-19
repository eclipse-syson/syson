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
package org.eclipse.syson.sysml.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.SendActionUsage;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Send Action Usage</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.SendActionUsageImpl#getPayloadArgument <em>Payload Argument</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.SendActionUsageImpl#getReceiverArgument <em>Receiver Argument</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.SendActionUsageImpl#getSenderArgument <em>Sender Argument</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SendActionUsageImpl extends ActionUsageImpl implements SendActionUsage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SendActionUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getSendActionUsage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getPayloadArgument() {
        Expression payloadArgument = basicGetPayloadArgument();
        return payloadArgument != null && payloadArgument.eIsProxy() ? (Expression)eResolveProxy((InternalEObject)payloadArgument) : payloadArgument;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Expression basicGetPayloadArgument() {
        // TODO: implement this method to return the 'Payload Argument' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getReceiverArgument() {
        Expression receiverArgument = basicGetReceiverArgument();
        return receiverArgument != null && receiverArgument.eIsProxy() ? (Expression)eResolveProxy((InternalEObject)receiverArgument) : receiverArgument;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Expression basicGetReceiverArgument() {
        // TODO: implement this method to return the 'Receiver Argument' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression getSenderArgument() {
        Expression senderArgument = basicGetSenderArgument();
        return senderArgument != null && senderArgument.eIsProxy() ? (Expression)eResolveProxy((InternalEObject)senderArgument) : senderArgument;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Expression basicGetSenderArgument() {
        // TODO: implement this method to return the 'Sender Argument' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.SEND_ACTION_USAGE__PAYLOAD_ARGUMENT:
                if (resolve) return getPayloadArgument();
                return basicGetPayloadArgument();
            case SysmlPackage.SEND_ACTION_USAGE__RECEIVER_ARGUMENT:
                if (resolve) return getReceiverArgument();
                return basicGetReceiverArgument();
            case SysmlPackage.SEND_ACTION_USAGE__SENDER_ARGUMENT:
                if (resolve) return getSenderArgument();
                return basicGetSenderArgument();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.SEND_ACTION_USAGE__PAYLOAD_ARGUMENT:
                return basicGetPayloadArgument() != null;
            case SysmlPackage.SEND_ACTION_USAGE__RECEIVER_ARGUMENT:
                return basicGetReceiverArgument() != null;
            case SysmlPackage.SEND_ACTION_USAGE__SENDER_ARGUMENT:
                return basicGetSenderArgument() != null;
        }
        return super.eIsSet(featureID);
    }

} //SendActionUsageImpl
