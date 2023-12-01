/**
 * Copyright (c) 2023 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 */
package org.eclipse.syson.sysml.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Behavior;
import org.eclipse.syson.sysml.Classifier;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FlowConnectionUsage;
import org.eclipse.syson.sysml.Interaction;
import org.eclipse.syson.sysml.ItemFeature;
import org.eclipse.syson.sysml.ItemFlow;
import org.eclipse.syson.sysml.ItemFlowEnd;
import org.eclipse.syson.sysml.Step;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Flow Connection Usage</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.FlowConnectionUsageImpl#getBehavior <em>Behavior</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FlowConnectionUsageImpl#getParameter <em>Parameter</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FlowConnectionUsageImpl#getActionDefinition <em>Action Definition</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FlowConnectionUsageImpl#getInteraction <em>Interaction</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FlowConnectionUsageImpl#getItemFeature <em>Item Feature</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FlowConnectionUsageImpl#getItemFlowEnd <em>Item Flow End</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FlowConnectionUsageImpl#getItemType <em>Item Type</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FlowConnectionUsageImpl#getSourceOutputFeature <em>Source Output Feature</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FlowConnectionUsageImpl#getTargetInputFeature <em>Target Input Feature</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.FlowConnectionUsageImpl#getFlowConnectionDefinition <em>Flow Connection Definition</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FlowConnectionUsageImpl extends ConnectionUsageImpl implements FlowConnectionUsage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected FlowConnectionUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getFlowConnectionUsage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Behavior> getBehavior() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getStep_Behavior(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Feature> getParameter() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getStep_Parameter(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Behavior> getActionDefinition() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getActionUsage_ActionDefinition(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Interaction> getInteraction() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getItemFlow_Interaction(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ItemFeature getItemFeature() {
        ItemFeature itemFeature = basicGetItemFeature();
        return itemFeature != null && itemFeature.eIsProxy() ? (ItemFeature)eResolveProxy((InternalEObject)itemFeature) : itemFeature;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ItemFeature basicGetItemFeature() {
        // TODO: implement this method to return the 'Item Feature' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<ItemFlowEnd> getItemFlowEnd() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getItemFlow_ItemFlowEnd(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Classifier> getItemType() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getItemFlow_ItemType(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Feature getSourceOutputFeature() {
        Feature sourceOutputFeature = basicGetSourceOutputFeature();
        return sourceOutputFeature != null && sourceOutputFeature.eIsProxy() ? (Feature)eResolveProxy((InternalEObject)sourceOutputFeature) : sourceOutputFeature;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Feature basicGetSourceOutputFeature() {
        // TODO: implement this method to return the 'Source Output Feature' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Feature getTargetInputFeature() {
        Feature targetInputFeature = basicGetTargetInputFeature();
        return targetInputFeature != null && targetInputFeature.eIsProxy() ? (Feature)eResolveProxy((InternalEObject)targetInputFeature) : targetInputFeature;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Feature basicGetTargetInputFeature() {
        // TODO: implement this method to return the 'Target Input Feature' reference
        // -> do not perform proxy resolution
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Interaction> getFlowConnectionDefinition() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getFlowConnectionUsage_FlowConnectionDefinition(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Expression argument(int i) {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Feature inputParameter(int i) {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EList<Feature> inputParameters() {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public boolean isSubactionUsage() {
        return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.FLOW_CONNECTION_USAGE__BEHAVIOR:
                return getBehavior();
            case SysmlPackage.FLOW_CONNECTION_USAGE__PARAMETER:
                return getParameter();
            case SysmlPackage.FLOW_CONNECTION_USAGE__ACTION_DEFINITION:
                return getActionDefinition();
            case SysmlPackage.FLOW_CONNECTION_USAGE__INTERACTION:
                return getInteraction();
            case SysmlPackage.FLOW_CONNECTION_USAGE__ITEM_FEATURE:
                if (resolve) return getItemFeature();
                return basicGetItemFeature();
            case SysmlPackage.FLOW_CONNECTION_USAGE__ITEM_FLOW_END:
                return getItemFlowEnd();
            case SysmlPackage.FLOW_CONNECTION_USAGE__ITEM_TYPE:
                return getItemType();
            case SysmlPackage.FLOW_CONNECTION_USAGE__SOURCE_OUTPUT_FEATURE:
                if (resolve) return getSourceOutputFeature();
                return basicGetSourceOutputFeature();
            case SysmlPackage.FLOW_CONNECTION_USAGE__TARGET_INPUT_FEATURE:
                if (resolve) return getTargetInputFeature();
                return basicGetTargetInputFeature();
            case SysmlPackage.FLOW_CONNECTION_USAGE__FLOW_CONNECTION_DEFINITION:
                return getFlowConnectionDefinition();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.FLOW_CONNECTION_USAGE__BEHAVIOR:
                return !getBehavior().isEmpty();
            case SysmlPackage.FLOW_CONNECTION_USAGE__PARAMETER:
                return !getParameter().isEmpty();
            case SysmlPackage.FLOW_CONNECTION_USAGE__ACTION_DEFINITION:
                return !getActionDefinition().isEmpty();
            case SysmlPackage.FLOW_CONNECTION_USAGE__INTERACTION:
                return !getInteraction().isEmpty();
            case SysmlPackage.FLOW_CONNECTION_USAGE__ITEM_FEATURE:
                return basicGetItemFeature() != null;
            case SysmlPackage.FLOW_CONNECTION_USAGE__ITEM_FLOW_END:
                return !getItemFlowEnd().isEmpty();
            case SysmlPackage.FLOW_CONNECTION_USAGE__ITEM_TYPE:
                return !getItemType().isEmpty();
            case SysmlPackage.FLOW_CONNECTION_USAGE__SOURCE_OUTPUT_FEATURE:
                return basicGetSourceOutputFeature() != null;
            case SysmlPackage.FLOW_CONNECTION_USAGE__TARGET_INPUT_FEATURE:
                return basicGetTargetInputFeature() != null;
            case SysmlPackage.FLOW_CONNECTION_USAGE__FLOW_CONNECTION_DEFINITION:
                return !getFlowConnectionDefinition().isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
        if (baseClass == Step.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.FLOW_CONNECTION_USAGE__BEHAVIOR: return SysmlPackage.STEP__BEHAVIOR;
                case SysmlPackage.FLOW_CONNECTION_USAGE__PARAMETER: return SysmlPackage.STEP__PARAMETER;
                default: return -1;
            }
        }
        if (baseClass == ActionUsage.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.FLOW_CONNECTION_USAGE__ACTION_DEFINITION: return SysmlPackage.ACTION_USAGE__ACTION_DEFINITION;
                default: return -1;
            }
        }
        if (baseClass == ItemFlow.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.FLOW_CONNECTION_USAGE__INTERACTION: return SysmlPackage.ITEM_FLOW__INTERACTION;
                case SysmlPackage.FLOW_CONNECTION_USAGE__ITEM_FEATURE: return SysmlPackage.ITEM_FLOW__ITEM_FEATURE;
                case SysmlPackage.FLOW_CONNECTION_USAGE__ITEM_FLOW_END: return SysmlPackage.ITEM_FLOW__ITEM_FLOW_END;
                case SysmlPackage.FLOW_CONNECTION_USAGE__ITEM_TYPE: return SysmlPackage.ITEM_FLOW__ITEM_TYPE;
                case SysmlPackage.FLOW_CONNECTION_USAGE__SOURCE_OUTPUT_FEATURE: return SysmlPackage.ITEM_FLOW__SOURCE_OUTPUT_FEATURE;
                case SysmlPackage.FLOW_CONNECTION_USAGE__TARGET_INPUT_FEATURE: return SysmlPackage.ITEM_FLOW__TARGET_INPUT_FEATURE;
                default: return -1;
            }
        }
        return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
        if (baseClass == Step.class) {
            switch (baseFeatureID) {
                case SysmlPackage.STEP__BEHAVIOR: return SysmlPackage.FLOW_CONNECTION_USAGE__BEHAVIOR;
                case SysmlPackage.STEP__PARAMETER: return SysmlPackage.FLOW_CONNECTION_USAGE__PARAMETER;
                default: return -1;
            }
        }
        if (baseClass == ActionUsage.class) {
            switch (baseFeatureID) {
                case SysmlPackage.ACTION_USAGE__ACTION_DEFINITION: return SysmlPackage.FLOW_CONNECTION_USAGE__ACTION_DEFINITION;
                default: return -1;
            }
        }
        if (baseClass == ItemFlow.class) {
            switch (baseFeatureID) {
                case SysmlPackage.ITEM_FLOW__INTERACTION: return SysmlPackage.FLOW_CONNECTION_USAGE__INTERACTION;
                case SysmlPackage.ITEM_FLOW__ITEM_FEATURE: return SysmlPackage.FLOW_CONNECTION_USAGE__ITEM_FEATURE;
                case SysmlPackage.ITEM_FLOW__ITEM_FLOW_END: return SysmlPackage.FLOW_CONNECTION_USAGE__ITEM_FLOW_END;
                case SysmlPackage.ITEM_FLOW__ITEM_TYPE: return SysmlPackage.FLOW_CONNECTION_USAGE__ITEM_TYPE;
                case SysmlPackage.ITEM_FLOW__SOURCE_OUTPUT_FEATURE: return SysmlPackage.FLOW_CONNECTION_USAGE__SOURCE_OUTPUT_FEATURE;
                case SysmlPackage.ITEM_FLOW__TARGET_INPUT_FEATURE: return SysmlPackage.FLOW_CONNECTION_USAGE__TARGET_INPUT_FEATURE;
                default: return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int eDerivedOperationID(int baseOperationID, Class<?> baseClass) {
        if (baseClass == Step.class) {
            switch (baseOperationID) {
                default: return -1;
            }
        }
        if (baseClass == ActionUsage.class) {
            switch (baseOperationID) {
                case SysmlPackage.ACTION_USAGE___ARGUMENT__INT: return SysmlPackage.FLOW_CONNECTION_USAGE___ARGUMENT__INT;
                case SysmlPackage.ACTION_USAGE___INPUT_PARAMETER__INT: return SysmlPackage.FLOW_CONNECTION_USAGE___INPUT_PARAMETER__INT;
                case SysmlPackage.ACTION_USAGE___INPUT_PARAMETERS: return SysmlPackage.FLOW_CONNECTION_USAGE___INPUT_PARAMETERS;
                case SysmlPackage.ACTION_USAGE___IS_SUBACTION_USAGE: return SysmlPackage.FLOW_CONNECTION_USAGE___IS_SUBACTION_USAGE;
                default: return -1;
            }
        }
        if (baseClass == ItemFlow.class) {
            switch (baseOperationID) {
                default: return -1;
            }
        }
        return super.eDerivedOperationID(baseOperationID, baseClass);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
        switch (operationID) {
            case SysmlPackage.FLOW_CONNECTION_USAGE___ARGUMENT__INT:
                return argument((Integer)arguments.get(0));
            case SysmlPackage.FLOW_CONNECTION_USAGE___INPUT_PARAMETER__INT:
                return inputParameter((Integer)arguments.get(0));
            case SysmlPackage.FLOW_CONNECTION_USAGE___INPUT_PARAMETERS:
                return inputParameters();
            case SysmlPackage.FLOW_CONNECTION_USAGE___IS_SUBACTION_USAGE:
                return isSubactionUsage();
        }
        return super.eInvoke(operationID, arguments);
    }

} //FlowConnectionUsageImpl
