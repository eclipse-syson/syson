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
package org.eclipse.syson.sysml.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.syson.sysml.ConjugatedPortDefinition;
import org.eclipse.syson.sysml.ConjugatedPortTyping;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Conjugated Port Typing</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.ConjugatedPortTypingImpl#getConjugatedPortDefinition <em>Conjugated Port Definition</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.ConjugatedPortTypingImpl#getPortDefinition <em>Port Definition</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConjugatedPortTypingImpl extends FeatureTypingImpl implements ConjugatedPortTyping {
    /**
     * The cached value of the '{@link #getConjugatedPortDefinition() <em>Conjugated Port Definition</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConjugatedPortDefinition()
     * @generated
     * @ordered
     */
    protected ConjugatedPortDefinition conjugatedPortDefinition;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ConjugatedPortTypingImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getConjugatedPortTyping();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ConjugatedPortDefinition getConjugatedPortDefinition() {
        if (conjugatedPortDefinition != null && conjugatedPortDefinition.eIsProxy()) {
            InternalEObject oldConjugatedPortDefinition = (InternalEObject)conjugatedPortDefinition;
            conjugatedPortDefinition = (ConjugatedPortDefinition)eResolveProxy(oldConjugatedPortDefinition);
            if (conjugatedPortDefinition != oldConjugatedPortDefinition) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.CONJUGATED_PORT_TYPING__CONJUGATED_PORT_DEFINITION, oldConjugatedPortDefinition, conjugatedPortDefinition));
            }
        }
        return conjugatedPortDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ConjugatedPortDefinition basicGetConjugatedPortDefinition() {
        return conjugatedPortDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setConjugatedPortDefinition(ConjugatedPortDefinition newConjugatedPortDefinition) {
        ConjugatedPortDefinition oldConjugatedPortDefinition = conjugatedPortDefinition;
        conjugatedPortDefinition = newConjugatedPortDefinition;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.CONJUGATED_PORT_TYPING__CONJUGATED_PORT_DEFINITION, oldConjugatedPortDefinition, conjugatedPortDefinition));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public PortDefinition getPortDefinition() {
        PortDefinition portDefinition = basicGetPortDefinition();
        return portDefinition != null && portDefinition.eIsProxy() ? (PortDefinition)eResolveProxy((InternalEObject)portDefinition) : portDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PortDefinition basicGetPortDefinition() {
        // TODO: implement this method to return the 'Port Definition' reference
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
            case SysmlPackage.CONJUGATED_PORT_TYPING__CONJUGATED_PORT_DEFINITION:
                if (resolve) return getConjugatedPortDefinition();
                return basicGetConjugatedPortDefinition();
            case SysmlPackage.CONJUGATED_PORT_TYPING__PORT_DEFINITION:
                if (resolve) return getPortDefinition();
                return basicGetPortDefinition();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case SysmlPackage.CONJUGATED_PORT_TYPING__CONJUGATED_PORT_DEFINITION:
                setConjugatedPortDefinition((ConjugatedPortDefinition)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case SysmlPackage.CONJUGATED_PORT_TYPING__CONJUGATED_PORT_DEFINITION:
                setConjugatedPortDefinition((ConjugatedPortDefinition)null);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.CONJUGATED_PORT_TYPING__CONJUGATED_PORT_DEFINITION:
                return conjugatedPortDefinition != null;
            case SysmlPackage.CONJUGATED_PORT_TYPING__PORT_DEFINITION:
                return basicGetPortDefinition() != null;
        }
        return super.eIsSet(featureID);
    }

} //ConjugatedPortTypingImpl
