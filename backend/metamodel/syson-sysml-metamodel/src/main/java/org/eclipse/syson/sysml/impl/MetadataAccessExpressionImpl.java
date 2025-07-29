/*******************************************************************************
* Copyright (c) 2023, 2025 Obeo.
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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.Membership;
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
        Element referencedElement = this.basicGetReferencedElement();
        return referencedElement != null && referencedElement.eIsProxy() ? (Element) this.eResolveProxy((InternalEObject) referencedElement) : referencedElement;
    }

    /**
     * <!-- begin-user-doc -->The referencedElement of a MetadataAccessExpression is the memberElement of its first
     * ownedMembership that is not a FeatureMembership <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public Element basicGetReferencedElement() {
        return this.getOwnedMembership().stream()
                .filter(f -> !(f instanceof FeatureMembership))
                .map(Membership::getMemberElement)
                .findFirst()
                .orElse(null);
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
                if (resolve) {
                    return this.getReferencedElement();
                }
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
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.METADATA_ACCESS_EXPRESSION__REFERENCED_ELEMENT:
                return this.basicGetReferencedElement() != null;
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
