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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.RenderingUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.ViewDefinition;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.ViewpointUsage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>View Definition</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.ViewDefinitionImpl#getSatisfiedViewpoint <em>Satisfied Viewpoint</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ViewDefinitionImpl#getView <em>View</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ViewDefinitionImpl#getViewCondition <em>View Condition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ViewDefinitionImpl#getViewRendering <em>View Rendering</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ViewDefinitionImpl extends PartDefinitionImpl implements ViewDefinition {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected ViewDefinitionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getViewDefinition();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<ViewpointUsage> getSatisfiedViewpoint() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getViewDefinition_SatisfiedViewpoint(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<ViewUsage> getView() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getViewDefinition_View(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Expression> getViewCondition() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getViewDefinition_ViewCondition(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public RenderingUsage getViewRendering() {
        RenderingUsage viewRendering = this.basicGetViewRendering();
        return viewRendering != null && viewRendering.eIsProxy() ? (RenderingUsage) this.eResolveProxy((InternalEObject) viewRendering) : viewRendering;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public RenderingUsage basicGetViewRendering() {
        // TODO: implement this method to return the 'View Rendering' reference
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
            case SysmlPackage.VIEW_DEFINITION__SATISFIED_VIEWPOINT:
                return this.getSatisfiedViewpoint();
            case SysmlPackage.VIEW_DEFINITION__VIEW:
                return this.getView();
            case SysmlPackage.VIEW_DEFINITION__VIEW_CONDITION:
                return this.getViewCondition();
            case SysmlPackage.VIEW_DEFINITION__VIEW_RENDERING:
                if (resolve) {
                    return this.getViewRendering();
                }
                return this.basicGetViewRendering();
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
            case SysmlPackage.VIEW_DEFINITION__SATISFIED_VIEWPOINT:
                return !this.getSatisfiedViewpoint().isEmpty();
            case SysmlPackage.VIEW_DEFINITION__VIEW:
                return !this.getView().isEmpty();
            case SysmlPackage.VIEW_DEFINITION__VIEW_CONDITION:
                return !this.getViewCondition().isEmpty();
            case SysmlPackage.VIEW_DEFINITION__VIEW_RENDERING:
                return this.basicGetViewRendering() != null;
        }
        return super.eIsSet(featureID);
    }

} // ViewDefinitionImpl
