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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.MetadataAccessExpression;
import org.eclipse.syson.sysml.MetadataFeature;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Metadata Access Expression</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.MetadataAccessExpressionImpl#getReferencedElement <em>Referenced
 * Element</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MetadataAccessExpressionImpl extends ExpressionImpl implements MetadataAccessExpression {
    /**
     * The cached value of the '{@link #getReferencedElement() <em>Referenced Element</em>}' reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getReferencedElement()
     * @generated
     * @ordered
     */
    protected Element referencedElement;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected MetadataAccessExpressionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getMetadataAccessExpression();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Element getReferencedElement() {
        if (this.referencedElement != null && this.referencedElement.eIsProxy()) {
            InternalEObject oldReferencedElement = (InternalEObject) this.referencedElement;
            this.referencedElement = (Element) this.eResolveProxy(oldReferencedElement);
            if (this.referencedElement != oldReferencedElement) {
                if (this.eNotificationRequired())
                    this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SysmlPackage.METADATA_ACCESS_EXPRESSION__REFERENCED_ELEMENT, oldReferencedElement, this.referencedElement));
            }
        }
        return this.referencedElement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Element basicGetReferencedElement() {
        return this.referencedElement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setReferencedElement(Element newReferencedElement) {
        Element oldReferencedElement = this.referencedElement;
        this.referencedElement = newReferencedElement;
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.METADATA_ACCESS_EXPRESSION__REFERENCED_ELEMENT, oldReferencedElement, this.referencedElement));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public MetadataFeature metaclassFeature() {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.METADATA_ACCESS_EXPRESSION__REFERENCED_ELEMENT:
                if (resolve)
                    return this.getReferencedElement();
                return this.basicGetReferencedElement();
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
            case SysmlPackage.METADATA_ACCESS_EXPRESSION__REFERENCED_ELEMENT:
                this.setReferencedElement((Element) newValue);
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
            case SysmlPackage.METADATA_ACCESS_EXPRESSION__REFERENCED_ELEMENT:
                this.setReferencedElement((Element) null);
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
            case SysmlPackage.METADATA_ACCESS_EXPRESSION__REFERENCED_ELEMENT:
                return this.referencedElement != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
        switch (operationID) {
            case SysmlPackage.METADATA_ACCESS_EXPRESSION___METACLASS_FEATURE:
                return this.metaclassFeature();
        }
        return super.eInvoke(operationID, arguments);
    }

} // MetadataAccessExpressionImpl
