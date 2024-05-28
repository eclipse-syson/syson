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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.ViewpointDefinition;
import org.eclipse.syson.sysml.ViewpointUsage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Viewpoint Usage</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.ViewpointUsageImpl#getViewpointDefinition <em>Viewpoint Definition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ViewpointUsageImpl#getViewpointStakeholder <em>Viewpoint
 * Stakeholder</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ViewpointUsageImpl extends RequirementUsageImpl implements ViewpointUsage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected ViewpointUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getViewpointUsage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ViewpointDefinition getViewpointDefinition() {
        ViewpointDefinition viewpointDefinition = this.basicGetViewpointDefinition();
        return viewpointDefinition != null && viewpointDefinition.eIsProxy() ? (ViewpointDefinition) this.eResolveProxy((InternalEObject) viewpointDefinition) : viewpointDefinition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public ViewpointDefinition basicGetViewpointDefinition() {
        // TODO: implement this method to return the 'Viewpoint Definition' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<PartUsage> getViewpointStakeholder() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getViewpointUsage_ViewpointStakeholder(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.VIEWPOINT_USAGE__VIEWPOINT_DEFINITION:
                if (resolve)
                    return this.getViewpointDefinition();
                return this.basicGetViewpointDefinition();
            case SysmlPackage.VIEWPOINT_USAGE__VIEWPOINT_STAKEHOLDER:
                return this.getViewpointStakeholder();
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
            case SysmlPackage.VIEWPOINT_USAGE__VIEWPOINT_DEFINITION:
                return this.basicGetViewpointDefinition() != null;
            case SysmlPackage.VIEWPOINT_USAGE__VIEWPOINT_STAKEHOLDER:
                return !this.getViewpointStakeholder().isEmpty();
        }
        return super.eIsSet(featureID);
    }

} // ViewpointUsageImpl
