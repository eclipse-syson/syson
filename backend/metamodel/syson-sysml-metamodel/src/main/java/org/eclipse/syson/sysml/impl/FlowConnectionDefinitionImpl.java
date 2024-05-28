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
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Behavior;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FlowConnectionDefinition;
import org.eclipse.syson.sysml.Interaction;
import org.eclipse.syson.sysml.Step;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Flow Connection Definition</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.FlowConnectionDefinitionImpl#getParameter <em>Parameter</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FlowConnectionDefinitionImpl#getStep <em>Step</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FlowConnectionDefinitionImpl#getAction <em>Action</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FlowConnectionDefinitionImpl extends ConnectionDefinitionImpl implements FlowConnectionDefinition {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected FlowConnectionDefinitionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getFlowConnectionDefinition();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Feature> getParameter() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getBehavior_Parameter(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Step> getStep() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getBehavior_Step(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<ActionUsage> getAction() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getActionDefinition_Action(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.FLOW_CONNECTION_DEFINITION__PARAMETER:
                return this.getParameter();
            case SysmlPackage.FLOW_CONNECTION_DEFINITION__STEP:
                return this.getStep();
            case SysmlPackage.FLOW_CONNECTION_DEFINITION__ACTION:
                return this.getAction();
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
            case SysmlPackage.FLOW_CONNECTION_DEFINITION__PARAMETER:
                return !this.getParameter().isEmpty();
            case SysmlPackage.FLOW_CONNECTION_DEFINITION__STEP:
                return !this.getStep().isEmpty();
            case SysmlPackage.FLOW_CONNECTION_DEFINITION__ACTION:
                return !this.getAction().isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
        if (baseClass == Behavior.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.FLOW_CONNECTION_DEFINITION__PARAMETER:
                    return SysmlPackage.BEHAVIOR__PARAMETER;
                case SysmlPackage.FLOW_CONNECTION_DEFINITION__STEP:
                    return SysmlPackage.BEHAVIOR__STEP;
                default:
                    return -1;
            }
        }
        if (baseClass == ActionDefinition.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.FLOW_CONNECTION_DEFINITION__ACTION:
                    return SysmlPackage.ACTION_DEFINITION__ACTION;
                default:
                    return -1;
            }
        }
        if (baseClass == Interaction.class) {
            switch (derivedFeatureID) {
                default:
                    return -1;
            }
        }
        return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
        if (baseClass == Behavior.class) {
            switch (baseFeatureID) {
                case SysmlPackage.BEHAVIOR__PARAMETER:
                    return SysmlPackage.FLOW_CONNECTION_DEFINITION__PARAMETER;
                case SysmlPackage.BEHAVIOR__STEP:
                    return SysmlPackage.FLOW_CONNECTION_DEFINITION__STEP;
                default:
                    return -1;
            }
        }
        if (baseClass == ActionDefinition.class) {
            switch (baseFeatureID) {
                case SysmlPackage.ACTION_DEFINITION__ACTION:
                    return SysmlPackage.FLOW_CONNECTION_DEFINITION__ACTION;
                default:
                    return -1;
            }
        }
        if (baseClass == Interaction.class) {
            switch (baseFeatureID) {
                default:
                    return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
    }

} // FlowConnectionDefinitionImpl
