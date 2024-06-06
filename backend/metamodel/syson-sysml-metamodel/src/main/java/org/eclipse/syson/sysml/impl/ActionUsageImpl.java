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
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Behavior;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.Step;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Action Usage</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.ActionUsageImpl#getBehavior <em>Behavior</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ActionUsageImpl#getParameter <em>Parameter</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ActionUsageImpl#getActionDefinition <em>Action Definition</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ActionUsageImpl extends OccurrenceUsageImpl implements ActionUsage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected ActionUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getActionUsage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Feature> getParameter() {
        List<Feature> features = this.getOwnedRelationship().stream()
                .filter(ParameterMembership.class::isInstance)
                .map(ParameterMembership.class::cast)
                .flatMap(or -> or.getOwnedRelatedElement().stream())
                .filter(Feature.class::isInstance)
                .map(Feature.class::cast)
                .toList();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getStep_Parameter(), features.size(), features.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Behavior> getActionDefinition() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getActionUsage_ActionDefinition(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Expression argument(int i) {
        var features = this.inputParameters();
        if (features.size() > i) {
            return features.get(i).getOwnedRelationship().stream()
                    .filter(FeatureValue.class::isInstance)
                    .map(FeatureValue.class::cast)
                    .map(FeatureValue::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public Feature inputParameter(int i) {
        var parameters = this.inputParameters();
        if (parameters.size() > i) {
            return parameters.get(i);
        }
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Feature> inputParameters() {
        var parameters = this.getParameter().stream()
                .filter(f -> (f.getDirection() == FeatureDirectionKind.IN || f.getDirection() == FeatureDirectionKind.INOUT))
                .toList();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getStep_Parameter(), parameters.size(), parameters.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean isSubactionUsage() {
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
            case SysmlPackage.ACTION_USAGE__BEHAVIOR:
                return this.getBehavior();
            case SysmlPackage.ACTION_USAGE__PARAMETER:
                return this.getParameter();
            case SysmlPackage.ACTION_USAGE__ACTION_DEFINITION:
                return this.getActionDefinition();
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
            case SysmlPackage.ACTION_USAGE__BEHAVIOR:
                return !this.getBehavior().isEmpty();
            case SysmlPackage.ACTION_USAGE__PARAMETER:
                return !this.getParameter().isEmpty();
            case SysmlPackage.ACTION_USAGE__ACTION_DEFINITION:
                return !this.getActionDefinition().isEmpty();
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
        if (baseClass == Step.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.ACTION_USAGE__BEHAVIOR:
                    return SysmlPackage.STEP__BEHAVIOR;
                case SysmlPackage.ACTION_USAGE__PARAMETER:
                    return SysmlPackage.STEP__PARAMETER;
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
        if (baseClass == Step.class) {
            switch (baseFeatureID) {
                case SysmlPackage.STEP__BEHAVIOR:
                    return SysmlPackage.ACTION_USAGE__BEHAVIOR;
                case SysmlPackage.STEP__PARAMETER:
                    return SysmlPackage.ACTION_USAGE__PARAMETER;
                default:
                    return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
        switch (operationID) {
            case SysmlPackage.ACTION_USAGE___ARGUMENT__INT:
                return this.argument((Integer) arguments.get(0));
            case SysmlPackage.ACTION_USAGE___INPUT_PARAMETER__INT:
                return this.inputParameter((Integer) arguments.get(0));
            case SysmlPackage.ACTION_USAGE___INPUT_PARAMETERS:
                return this.inputParameters();
            case SysmlPackage.ACTION_USAGE___IS_SUBACTION_USAGE:
                return this.isSubactionUsage();
        }
        return super.eInvoke(operationID, arguments);
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Behavior> getBehavior() {
        EList<Behavior> behaviors = new BasicEList<>();
        EList<Behavior> actionDefinition = this.getActionDefinition();
        if (actionDefinition != null) {
            behaviors.addAll(actionDefinition);
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getStep_Behavior(), behaviors.size(), behaviors.toArray());
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<org.eclipse.syson.sysml.Class> getOccurrenceDefinition() {
        EList<org.eclipse.syson.sysml.Class> occurrenceDefinitions = new BasicEList<>();
        EList<Behavior> actionDefinition = this.getActionDefinition();
        if (actionDefinition != null) {
            occurrenceDefinitions.addAll(actionDefinition);
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getOccurrenceUsage_OccurrenceDefinition(), occurrenceDefinitions.size(), occurrenceDefinitions.toArray());
    }

} // ActionUsageImpl
