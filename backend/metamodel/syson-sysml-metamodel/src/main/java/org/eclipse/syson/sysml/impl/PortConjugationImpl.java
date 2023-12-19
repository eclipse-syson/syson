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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.syson.sysml.ConjugatedPortDefinition;
import org.eclipse.syson.sysml.PortConjugation;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Port Conjugation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.PortConjugationImpl#getConjugatedPortDefinition <em>Conjugated Port Definition</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.PortConjugationImpl#getOriginalPortDefinition <em>Original Port Definition</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PortConjugationImpl extends ConjugationImpl implements PortConjugation {
    /**
     * The cached value of the '{@link #getOriginalPortDefinition() <em>Original Port Definition</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOriginalPortDefinition()
     * @generated
     * @ordered
     */
    protected PortDefinition originalPortDefinition;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected PortConjugationImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getPortConjugation();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ConjugatedPortDefinition getConjugatedPortDefinition() {
        ConjugatedPortDefinition conjugatedPortDefinition = basicGetConjugatedPortDefinition();
        return conjugatedPortDefinition != null && conjugatedPortDefinition.eIsProxy() ? (ConjugatedPortDefinition)eResolveProxy((InternalEObject)conjugatedPortDefinition) : conjugatedPortDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ConjugatedPortDefinition basicGetConjugatedPortDefinition() {
        // TODO: implement this method to return the 'Conjugated Port Definition' reference
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
    public PortDefinition getOriginalPortDefinition() {
        if (originalPortDefinition != null && originalPortDefinition.eIsProxy()) {
            InternalEObject oldOriginalPortDefinition = (InternalEObject)originalPortDefinition;
            originalPortDefinition = (PortDefinition)eResolveProxy(oldOriginalPortDefinition);
            if (originalPortDefinition != oldOriginalPortDefinition) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.PORT_CONJUGATION__ORIGINAL_PORT_DEFINITION, oldOriginalPortDefinition, originalPortDefinition));
            }
        }
        return originalPortDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PortDefinition basicGetOriginalPortDefinition() {
        return originalPortDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setOriginalPortDefinition(PortDefinition newOriginalPortDefinition) {
        PortDefinition oldOriginalPortDefinition = originalPortDefinition;
        originalPortDefinition = newOriginalPortDefinition;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.PORT_CONJUGATION__ORIGINAL_PORT_DEFINITION, oldOriginalPortDefinition, originalPortDefinition));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.PORT_CONJUGATION__CONJUGATED_PORT_DEFINITION:
                if (resolve) return getConjugatedPortDefinition();
                return basicGetConjugatedPortDefinition();
            case SysmlPackage.PORT_CONJUGATION__ORIGINAL_PORT_DEFINITION:
                if (resolve) return getOriginalPortDefinition();
                return basicGetOriginalPortDefinition();
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
            case SysmlPackage.PORT_CONJUGATION__ORIGINAL_PORT_DEFINITION:
                setOriginalPortDefinition((PortDefinition)newValue);
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
            case SysmlPackage.PORT_CONJUGATION__ORIGINAL_PORT_DEFINITION:
                setOriginalPortDefinition((PortDefinition)null);
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
            case SysmlPackage.PORT_CONJUGATION__CONJUGATED_PORT_DEFINITION:
                return basicGetConjugatedPortDefinition() != null;
            case SysmlPackage.PORT_CONJUGATION__ORIGINAL_PORT_DEFINITION:
                return originalPortDefinition != null;
        }
        return super.eIsSet(featureID);
    }

} //PortConjugationImpl
