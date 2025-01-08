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

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.Association;
import org.eclipse.syson.sysml.Behavior;
import org.eclipse.syson.sysml.Classifier;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Interaction;
import org.eclipse.syson.sysml.ItemFeature;
import org.eclipse.syson.sysml.ItemFlow;
import org.eclipse.syson.sysml.ItemFlowEnd;
import org.eclipse.syson.sysml.Step;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Item Flow</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.ItemFlowImpl#getBehavior <em>Behavior</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ItemFlowImpl#getParameter <em>Parameter</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ItemFlowImpl#getInteraction <em>Interaction</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ItemFlowImpl#getItemFeature <em>Item Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ItemFlowImpl#getItemFlowEnd <em>Item Flow End</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ItemFlowImpl#getItemType <em>Item Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ItemFlowImpl#getSourceOutputFeature <em>Source Output Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.ItemFlowImpl#getTargetInputFeature <em>Target Input Feature</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ItemFlowImpl extends ConnectorImpl implements ItemFlow {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected ItemFlowImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getItemFlow();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Feature> getParameter() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getStep_Parameter(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Interaction> getInteraction() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getItemFlow_Interaction(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ItemFeature getItemFeature() {
        ItemFeature itemFeature = this.basicGetItemFeature();
        return itemFeature != null && itemFeature.eIsProxy() ? (ItemFeature) this.eResolveProxy((InternalEObject) itemFeature) : itemFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public ItemFeature basicGetItemFeature() {
        // TODO: implement this method to return the 'Item Feature' reference
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
    public EList<ItemFlowEnd> getItemFlowEnd() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getItemFlow_ItemFlowEnd(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Classifier> getItemType() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getItemFlow_ItemType(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature getSourceOutputFeature() {
        Feature sourceOutputFeature = this.basicGetSourceOutputFeature();
        return sourceOutputFeature != null && sourceOutputFeature.eIsProxy() ? (Feature) this.eResolveProxy((InternalEObject) sourceOutputFeature) : sourceOutputFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Feature basicGetSourceOutputFeature() {
        // TODO: implement this method to return the 'Source Output Feature' reference
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
    public Feature getTargetInputFeature() {
        Feature targetInputFeature = this.basicGetTargetInputFeature();
        return targetInputFeature != null && targetInputFeature.eIsProxy() ? (Feature) this.eResolveProxy((InternalEObject) targetInputFeature) : targetInputFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public Feature basicGetTargetInputFeature() {
        // TODO: implement this method to return the 'Target Input Feature' reference
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
            case SysmlPackage.ITEM_FLOW__BEHAVIOR:
                return this.getBehavior();
            case SysmlPackage.ITEM_FLOW__PARAMETER:
                return this.getParameter();
            case SysmlPackage.ITEM_FLOW__INTERACTION:
                return this.getInteraction();
            case SysmlPackage.ITEM_FLOW__ITEM_FEATURE:
                if (resolve) {
                    return this.getItemFeature();
                }
                return this.basicGetItemFeature();
            case SysmlPackage.ITEM_FLOW__ITEM_FLOW_END:
                return this.getItemFlowEnd();
            case SysmlPackage.ITEM_FLOW__ITEM_TYPE:
                return this.getItemType();
            case SysmlPackage.ITEM_FLOW__SOURCE_OUTPUT_FEATURE:
                if (resolve) {
                    return this.getSourceOutputFeature();
                }
                return this.basicGetSourceOutputFeature();
            case SysmlPackage.ITEM_FLOW__TARGET_INPUT_FEATURE:
                if (resolve) {
                    return this.getTargetInputFeature();
                }
                return this.basicGetTargetInputFeature();
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
            case SysmlPackage.ITEM_FLOW__BEHAVIOR:
                return !this.getBehavior().isEmpty();
            case SysmlPackage.ITEM_FLOW__PARAMETER:
                return !this.getParameter().isEmpty();
            case SysmlPackage.ITEM_FLOW__INTERACTION:
                return !this.getInteraction().isEmpty();
            case SysmlPackage.ITEM_FLOW__ITEM_FEATURE:
                return this.basicGetItemFeature() != null;
            case SysmlPackage.ITEM_FLOW__ITEM_FLOW_END:
                return !this.getItemFlowEnd().isEmpty();
            case SysmlPackage.ITEM_FLOW__ITEM_TYPE:
                return !this.getItemType().isEmpty();
            case SysmlPackage.ITEM_FLOW__SOURCE_OUTPUT_FEATURE:
                return this.basicGetSourceOutputFeature() != null;
            case SysmlPackage.ITEM_FLOW__TARGET_INPUT_FEATURE:
                return this.basicGetTargetInputFeature() != null;
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
                case SysmlPackage.ITEM_FLOW__BEHAVIOR:
                    return SysmlPackage.STEP__BEHAVIOR;
                case SysmlPackage.ITEM_FLOW__PARAMETER:
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
                    return SysmlPackage.ITEM_FLOW__BEHAVIOR;
                case SysmlPackage.STEP__PARAMETER:
                    return SysmlPackage.ITEM_FLOW__PARAMETER;
                default:
                    return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Association> getAssociation() {
        EList<Association> associations = new BasicEList<>();
        EList<Interaction> interaction = this.getInteraction();
        if (interaction != null) {
            associations.addAll(interaction);
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getConnector_Association(), associations.size(), associations.toArray());
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Behavior> getBehavior() {
        EList<Behavior> behaviors = new BasicEList<>();
        EList<Interaction> interaction = this.getInteraction();
        if (interaction != null) {
            behaviors.addAll(interaction);
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getStep_Behavior(), behaviors.size(), behaviors.toArray());
    }

} // ItemFlowImpl
