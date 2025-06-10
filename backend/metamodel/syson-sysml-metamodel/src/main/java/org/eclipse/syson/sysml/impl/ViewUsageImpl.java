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
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.RenderingUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.ViewDefinition;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.ViewpointUsage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>View Usage</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.ViewUsageImpl#getExposedElement <em>Exposed Element</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ViewUsageImpl#getSatisfiedViewpoint <em>Satisfied Viewpoint</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ViewUsageImpl#getViewCondition <em>View Condition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ViewUsageImpl#getViewDefinition <em>View Definition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ViewUsageImpl#getViewRendering <em>View Rendering</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ViewUsageImpl extends PartUsageImpl implements ViewUsage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected ViewUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getViewUsage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Element> getExposedElement() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getViewUsage_ExposedElement(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<ViewpointUsage> getSatisfiedViewpoint() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getViewUsage_SatisfiedViewpoint(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Expression> getViewCondition() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getViewUsage_ViewCondition(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ViewDefinition getViewDefinition() {
        ViewDefinition viewDefinition = this.basicGetViewDefinition();
        return viewDefinition != null && viewDefinition.eIsProxy() ? (ViewDefinition) this.eResolveProxy((InternalEObject) viewDefinition) : viewDefinition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public ViewDefinition basicGetViewDefinition() {
        // TODO: implement this method to return the 'View Definition' reference
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
     * @generated NOT
     */
    @Override
    public boolean includeAsExposed(Element element) {
        return false;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.VIEW_USAGE__EXPOSED_ELEMENT:
                return this.getExposedElement();
            case SysmlPackage.VIEW_USAGE__SATISFIED_VIEWPOINT:
                return this.getSatisfiedViewpoint();
            case SysmlPackage.VIEW_USAGE__VIEW_CONDITION:
                return this.getViewCondition();
            case SysmlPackage.VIEW_USAGE__VIEW_DEFINITION:
                if (resolve) {
                    return this.getViewDefinition();
                }
                return this.basicGetViewDefinition();
            case SysmlPackage.VIEW_USAGE__VIEW_RENDERING:
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
            case SysmlPackage.VIEW_USAGE__EXPOSED_ELEMENT:
                return !this.getExposedElement().isEmpty();
            case SysmlPackage.VIEW_USAGE__SATISFIED_VIEWPOINT:
                return !this.getSatisfiedViewpoint().isEmpty();
            case SysmlPackage.VIEW_USAGE__VIEW_CONDITION:
                return !this.getViewCondition().isEmpty();
            case SysmlPackage.VIEW_USAGE__VIEW_DEFINITION:
                return this.basicGetViewDefinition() != null;
            case SysmlPackage.VIEW_USAGE__VIEW_RENDERING:
                return this.basicGetViewRendering() != null;
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
            case SysmlPackage.VIEW_USAGE___INCLUDE_AS_EXPOSED__ELEMENT:
                return this.includeAsExposed((Element) arguments.get(0));
        }
        return super.eInvoke(operationID, arguments);
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<PartDefinition> getPartDefinition() {
        EList<PartDefinition> partDefinitions = new BasicEList<>();
        ViewDefinition viewDefinition = this.getViewDefinition();
        if (viewDefinition != null) {
            partDefinitions.add(viewDefinition);
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getPartUsage_PartDefinition(), partDefinitions.size(), partDefinitions.toArray());
    }

} // ViewUsageImpl
