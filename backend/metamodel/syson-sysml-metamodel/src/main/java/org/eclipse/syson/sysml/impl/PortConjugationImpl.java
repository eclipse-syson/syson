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
import org.eclipse.syson.sysml.Type;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Port Conjugation</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.PortConjugationImpl#getConjugatedPortDefinition <em>Conjugated Port
 * Definition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.PortConjugationImpl#getOriginalPortDefinition <em>Original Port
 * Definition</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PortConjugationImpl extends ConjugationImpl implements PortConjugation {
    /**
     * The cached value of the '{@link #getOriginalPortDefinition() <em>Original Port Definition</em>}' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getOriginalPortDefinition()
     * @generated
     * @ordered
     */
    protected PortDefinition originalPortDefinition;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected PortConjugationImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getPortConjugation();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ConjugatedPortDefinition getConjugatedPortDefinition() {
        ConjugatedPortDefinition conjugatedPortDefinition = this.basicGetConjugatedPortDefinition();
        return conjugatedPortDefinition != null && conjugatedPortDefinition.eIsProxy() ? (ConjugatedPortDefinition) this.eResolveProxy((InternalEObject) conjugatedPortDefinition)
                : conjugatedPortDefinition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public ConjugatedPortDefinition basicGetConjugatedPortDefinition() {
        // TODO: implement this method to return the 'Conjugated Port Definition' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public PortDefinition getOriginalPortDefinition() {
        if (this.originalPortDefinition != null && this.originalPortDefinition.eIsProxy()) {
            InternalEObject oldOriginalPortDefinition = (InternalEObject) this.originalPortDefinition;
            this.originalPortDefinition = (PortDefinition) this.eResolveProxy(oldOriginalPortDefinition);
            if (this.originalPortDefinition != oldOriginalPortDefinition) {
                if (this.eNotificationRequired()) {
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.PORT_CONJUGATION__ORIGINAL_PORT_DEFINITION, oldOriginalPortDefinition, this.originalPortDefinition));
                }
            }
        }
        return this.originalPortDefinition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public PortDefinition basicGetOriginalPortDefinition() {
        return this.originalPortDefinition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setOriginalPortDefinition(PortDefinition newOriginalPortDefinition) {
        PortDefinition oldOriginalPortDefinition = this.originalPortDefinition;
        this.originalPortDefinition = newOriginalPortDefinition;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.PORT_CONJUGATION__ORIGINAL_PORT_DEFINITION, oldOriginalPortDefinition, this.originalPortDefinition));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.PORT_CONJUGATION__CONJUGATED_PORT_DEFINITION:
                if (resolve) {
                    return this.getConjugatedPortDefinition();
                }
                return this.basicGetConjugatedPortDefinition();
            case SysmlPackage.PORT_CONJUGATION__ORIGINAL_PORT_DEFINITION:
                if (resolve) {
                    return this.getOriginalPortDefinition();
                }
                return this.basicGetOriginalPortDefinition();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case SysmlPackage.PORT_CONJUGATION__ORIGINAL_PORT_DEFINITION:
                this.setOriginalPortDefinition((PortDefinition) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case SysmlPackage.PORT_CONJUGATION__ORIGINAL_PORT_DEFINITION:
                this.setOriginalPortDefinition((PortDefinition) null);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.PORT_CONJUGATION__CONJUGATED_PORT_DEFINITION:
                return this.basicGetConjugatedPortDefinition() != null;
            case SysmlPackage.PORT_CONJUGATION__ORIGINAL_PORT_DEFINITION:
                return this.originalPortDefinition != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Type getOwningType() {
        return this.getConjugatedPortDefinition();
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Type getOriginalType() {
        return this.getOriginalPortDefinition();
    }

    /**
     * <!-- begin-user-doc --> Redefines setter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void setOriginalType(Type newOriginalType) {
        if (newOriginalType instanceof PortDefinition newOriginalTypePortDefinition) {
            this.setOriginalPortDefinition(newOriginalTypePortDefinition);
        }
    }

} // PortConjugationImpl
