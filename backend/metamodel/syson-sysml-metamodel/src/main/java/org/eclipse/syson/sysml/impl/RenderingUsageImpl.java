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
import org.eclipse.syson.sysml.RenderingDefinition;
import org.eclipse.syson.sysml.RenderingUsage;
import org.eclipse.syson.sysml.SysmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Rendering Usage</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.RenderingUsageImpl#getRenderingDefinition <em>Rendering Definition</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RenderingUsageImpl extends PartUsageImpl implements RenderingUsage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected RenderingUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getRenderingUsage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public RenderingDefinition getRenderingDefinition() {
        RenderingDefinition renderingDefinition = this.basicGetRenderingDefinition();
        return renderingDefinition != null && renderingDefinition.eIsProxy() ? (RenderingDefinition) this.eResolveProxy((InternalEObject) renderingDefinition) : renderingDefinition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public RenderingDefinition basicGetRenderingDefinition() {
        // TODO: implement this method to return the 'Rendering Definition' reference
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
            case SysmlPackage.RENDERING_USAGE__RENDERING_DEFINITION:
                if (resolve)
                    return this.getRenderingDefinition();
                return this.basicGetRenderingDefinition();
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
            case SysmlPackage.RENDERING_USAGE__RENDERING_DEFINITION:
                return this.basicGetRenderingDefinition() != null;
        }
        return super.eIsSet(featureID);
    }

} // RenderingUsageImpl
