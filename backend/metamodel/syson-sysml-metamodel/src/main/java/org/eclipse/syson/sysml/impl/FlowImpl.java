/**
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
 */
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
import org.eclipse.syson.sysml.Flow;
import org.eclipse.syson.sysml.FlowEnd;
import org.eclipse.syson.sysml.Interaction;
import org.eclipse.syson.sysml.PayloadFeature;
import org.eclipse.syson.sysml.Step;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Flow</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.FlowImpl#getBehavior <em>Behavior</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FlowImpl#getParameter <em>Parameter</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FlowImpl#getFlowEnd <em>Flow End</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FlowImpl#getInteraction <em>Interaction</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FlowImpl#getPayloadFeature <em>Payload Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FlowImpl#getPayloadType <em>Payload Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FlowImpl#getSourceOutputFeature <em>Source Output Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FlowImpl#getTargetInputFeature <em>Target Input Feature</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FlowImpl extends ConnectorImpl implements Flow {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected FlowImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getFlow();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
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

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Feature> getParameter() {
        List<Feature> data = this.getOwnedFeature().stream()
                .filter(this::isParameter)
                .toList();
        // The list should contain the inherited parameters, it is not the case for now.
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getStep_Parameter(), data.size(), data.toArray());
    }

    /**
     * @generated NOT
     */
    private boolean isParameter(Feature feature) {
        Type owningType = feature.getOwningType();
        return (owningType instanceof Behavior || owningType instanceof Step) && feature.getDirection() != null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<FlowEnd> getFlowEnd() {
        List<FlowEnd> flowEnds = new ArrayList<>();
        this.getConnectorEnd().stream()
                .filter(FlowEnd.class::isInstance)
                .map(FlowEnd.class::cast)
                .forEach(flowEnds::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getFlow_FlowEnd(), flowEnds.size(), flowEnds.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Interaction> getInteraction() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getFlow_Interaction(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public PayloadFeature getPayloadFeature() {
        PayloadFeature payloadFeature = this.basicGetPayloadFeature();
        return payloadFeature != null && payloadFeature.eIsProxy() ? (PayloadFeature) this.eResolveProxy((InternalEObject) payloadFeature) : payloadFeature;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public PayloadFeature basicGetPayloadFeature() {
        return this.getOwnedFeature().stream()
                .filter(PayloadFeature.class::isInstance)
                .map(PayloadFeature.class::cast)
                .findFirst()
                .orElse(null);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Classifier> getPayloadType() {
        List<Classifier> payloadTypes = new ArrayList<>();
        PayloadFeature payloadFeature = this.getPayloadFeature();
        if (payloadFeature != null) {
            payloadFeature.getType().stream()
                    .filter(Classifier.class::isInstance)
                    .map(Classifier.class::cast)
                    .forEach(payloadTypes::add);
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getFlow_PayloadType(), payloadTypes.size(), payloadTypes.toArray());
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
     * @generated NOT
     */
    public Feature basicGetSourceOutputFeature() {
        return this.getConnectorEnd().stream()
                .map(Feature::getOwnedFeature)
                .flatMap(List::stream)
                .findFirst()
                .orElse(null);
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
     * @generated NOT
     */
    public Feature basicGetTargetInputFeature() {
        EList<Feature> ends = this.getConnectorEnd();
        if (ends.size() < 2 || ends.get(1).getOwnedFeature().isEmpty()) {
            return null;
        } else {
            return ends.get(1).getOwnedFeature().get(0);
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.FLOW__BEHAVIOR:
                return this.getBehavior();
            case SysmlPackage.FLOW__PARAMETER:
                return this.getParameter();
            case SysmlPackage.FLOW__FLOW_END:
                return this.getFlowEnd();
            case SysmlPackage.FLOW__INTERACTION:
                return this.getInteraction();
            case SysmlPackage.FLOW__PAYLOAD_FEATURE:
                if (resolve) {
                    return this.getPayloadFeature();
                }
                return this.basicGetPayloadFeature();
            case SysmlPackage.FLOW__PAYLOAD_TYPE:
                return this.getPayloadType();
            case SysmlPackage.FLOW__SOURCE_OUTPUT_FEATURE:
                if (resolve) {
                    return this.getSourceOutputFeature();
                }
                return this.basicGetSourceOutputFeature();
            case SysmlPackage.FLOW__TARGET_INPUT_FEATURE:
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
            case SysmlPackage.FLOW__BEHAVIOR:
                return !this.getBehavior().isEmpty();
            case SysmlPackage.FLOW__PARAMETER:
                return !this.getParameter().isEmpty();
            case SysmlPackage.FLOW__FLOW_END:
                return !this.getFlowEnd().isEmpty();
            case SysmlPackage.FLOW__INTERACTION:
                return !this.getInteraction().isEmpty();
            case SysmlPackage.FLOW__PAYLOAD_FEATURE:
                return this.basicGetPayloadFeature() != null;
            case SysmlPackage.FLOW__PAYLOAD_TYPE:
                return !this.getPayloadType().isEmpty();
            case SysmlPackage.FLOW__SOURCE_OUTPUT_FEATURE:
                return this.basicGetSourceOutputFeature() != null;
            case SysmlPackage.FLOW__TARGET_INPUT_FEATURE:
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
                case SysmlPackage.FLOW__BEHAVIOR:
                    return SysmlPackage.STEP__BEHAVIOR;
                case SysmlPackage.FLOW__PARAMETER:
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
                    return SysmlPackage.FLOW__BEHAVIOR;
                case SysmlPackage.STEP__PARAMETER:
                    return SysmlPackage.FLOW__PARAMETER;
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
} // FlowImpl
