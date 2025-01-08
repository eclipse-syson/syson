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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.RenderingUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.ViewRenderingMembership;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>View Rendering Membership</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.ViewRenderingMembershipImpl#getOwnedRendering <em>Owned Rendering</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ViewRenderingMembershipImpl#getReferencedRendering <em>Referenced
 * Rendering</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ViewRenderingMembershipImpl extends FeatureMembershipImpl implements ViewRenderingMembership {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected ViewRenderingMembershipImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getViewRenderingMembership();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public RenderingUsage getOwnedRendering() {
        RenderingUsage ownedRendering = this.basicGetOwnedRendering();
        return ownedRendering != null && ownedRendering.eIsProxy() ? (RenderingUsage) this.eResolveProxy((InternalEObject) ownedRendering) : ownedRendering;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public RenderingUsage basicGetOwnedRendering() {
        // TODO: implement this method to return the 'Owned Rendering' reference
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
    public RenderingUsage getReferencedRendering() {
        RenderingUsage referencedRendering = this.basicGetReferencedRendering();
        return referencedRendering != null && referencedRendering.eIsProxy() ? (RenderingUsage) this.eResolveProxy((InternalEObject) referencedRendering) : referencedRendering;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public RenderingUsage basicGetReferencedRendering() {
        // TODO: implement this method to return the 'Referenced Rendering' reference
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
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.VIEW_RENDERING_MEMBERSHIP__OWNED_RENDERING:
                if (resolve) {
                    return this.getOwnedRendering();
                }
                return this.basicGetOwnedRendering();
            case SysmlPackage.VIEW_RENDERING_MEMBERSHIP__REFERENCED_RENDERING:
                if (resolve) {
                    return this.getReferencedRendering();
                }
                return this.basicGetReferencedRendering();
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
            case SysmlPackage.VIEW_RENDERING_MEMBERSHIP__OWNED_RENDERING:
                return this.basicGetOwnedRendering() != null;
            case SysmlPackage.VIEW_RENDERING_MEMBERSHIP__REFERENCED_RENDERING:
                return this.basicGetReferencedRendering() != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Feature getOwnedMemberFeature() {
        return this.getOwnedRendering();
    }

} // ViewRenderingMembershipImpl
