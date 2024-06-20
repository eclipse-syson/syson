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

import java.util.Collection;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.syson.sysml.Dependency;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Dependency</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.DependencyImpl#getClient <em>Client</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DependencyImpl#getSupplier <em>Supplier</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DependencyImpl extends RelationshipImpl implements Dependency {
    /**
     * The cached value of the '{@link #getClient() <em>Client</em>}' reference list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getClient()
     * @generated
     * @ordered
     */
    protected EList<Element> client;

    /**
     * The cached value of the '{@link #getSupplier() <em>Supplier</em>}' reference list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getSupplier()
     * @generated
     * @ordered
     */
    protected EList<Element> supplier;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected DependencyImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getDependency();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<Element> getClient() {
        if (this.client == null) {
            this.client = new EObjectResolvingEList<>(Element.class, this, SysmlPackage.DEPENDENCY__CLIENT);
        }
        return this.client;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<Element> getSupplier() {
        if (this.supplier == null) {
            this.supplier = new EObjectResolvingEList<>(Element.class, this, SysmlPackage.DEPENDENCY__SUPPLIER);
        }
        return this.supplier;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.DEPENDENCY__CLIENT:
                return this.getClient();
            case SysmlPackage.DEPENDENCY__SUPPLIER:
                return this.getSupplier();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case SysmlPackage.DEPENDENCY__CLIENT:
                this.getClient().clear();
                this.getClient().addAll((Collection<? extends Element>) newValue);
                return;
            case SysmlPackage.DEPENDENCY__SUPPLIER:
                this.getSupplier().clear();
                this.getSupplier().addAll((Collection<? extends Element>) newValue);
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
            case SysmlPackage.DEPENDENCY__CLIENT:
                this.getClient().clear();
                return;
            case SysmlPackage.DEPENDENCY__SUPPLIER:
                this.getSupplier().clear();
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
            case SysmlPackage.DEPENDENCY__CLIENT:
                return this.client != null && !this.client.isEmpty();
            case SysmlPackage.DEPENDENCY__SUPPLIER:
                return this.supplier != null && !this.supplier.isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Element> getSource() {
        return this.getClient();
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Element> getTarget() {
        return this.getSupplier();
    }

} // DependencyImpl
