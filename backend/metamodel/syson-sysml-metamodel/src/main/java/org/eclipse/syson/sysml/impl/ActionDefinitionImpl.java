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
import org.eclipse.syson.sysml.Step;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Action Definition</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.ActionDefinitionImpl#getParameter <em>Parameter</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ActionDefinitionImpl#getStep <em>Step</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ActionDefinitionImpl#getAction <em>Action</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ActionDefinitionImpl extends OccurrenceDefinitionImpl implements ActionDefinition {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected ActionDefinitionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getActionDefinition();
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
            case SysmlPackage.ACTION_DEFINITION__PARAMETER:
                return this.getParameter();
            case SysmlPackage.ACTION_DEFINITION__STEP:
                return this.getStep();
            case SysmlPackage.ACTION_DEFINITION__ACTION:
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
            case SysmlPackage.ACTION_DEFINITION__PARAMETER:
                return !this.getParameter().isEmpty();
            case SysmlPackage.ACTION_DEFINITION__STEP:
                return !this.getStep().isEmpty();
            case SysmlPackage.ACTION_DEFINITION__ACTION:
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
                case SysmlPackage.ACTION_DEFINITION__PARAMETER:
                    return SysmlPackage.BEHAVIOR__PARAMETER;
                case SysmlPackage.ACTION_DEFINITION__STEP:
                    return SysmlPackage.BEHAVIOR__STEP;
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
                    return SysmlPackage.ACTION_DEFINITION__PARAMETER;
                case SysmlPackage.BEHAVIOR__STEP:
                    return SysmlPackage.ACTION_DEFINITION__STEP;
                default:
                    return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
    }

} // ActionDefinitionImpl
