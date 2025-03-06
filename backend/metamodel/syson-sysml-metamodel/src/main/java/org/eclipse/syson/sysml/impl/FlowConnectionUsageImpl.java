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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
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
import org.eclipse.syson.sysml.OccurrenceDefinition;
import org.eclipse.syson.sysml.OccurrenceUsage;
import org.eclipse.syson.sysml.PortionKind;
import org.eclipse.syson.sysml.Step;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Flow Connection Usage</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.FlowConnectionUsageImpl#isIsIndividual <em>Is Individual</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FlowConnectionUsageImpl#getPortionKind <em>Portion Kind</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FlowConnectionUsageImpl#getIndividualDefinition <em>Individual
 * Definition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FlowConnectionUsageImpl#getOccurrenceDefinition <em>Occurrence
 * Definition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FlowConnectionUsageImpl#getBehavior <em>Behavior</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FlowConnectionUsageImpl#getParameter <em>Parameter</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FlowConnectionUsageImpl#getActionDefinition <em>Action Definition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FlowConnectionUsageImpl#getInteraction <em>Interaction</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FlowConnectionUsageImpl#getItemFeature <em>Item Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FlowConnectionUsageImpl#getItemFlowEnd <em>Item Flow End</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FlowConnectionUsageImpl#getItemType <em>Item Type</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FlowConnectionUsageImpl#getSourceOutputFeature <em>Source Output
 * Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FlowConnectionUsageImpl#getTargetInputFeature <em>Target Input
 * Feature</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.FlowConnectionUsageImpl#getFlowConnectionDefinition <em>Flow Connection
 * Definition</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FlowConnectionUsageImpl extends ConnectorAsUsageImpl implements FlowConnectionUsage {
    /**
     * The default value of the '{@link #isIsIndividual() <em>Is Individual</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #isIsIndividual()
     * @generated
     * @ordered
     */
    protected static final boolean IS_INDIVIDUAL_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsIndividual() <em>Is Individual</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #isIsIndividual()
     * @generated
     * @ordered
     */
    protected boolean isIndividual = IS_INDIVIDUAL_EDEFAULT;

    /**
     * The default value of the '{@link #getPortionKind() <em>Portion Kind</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getPortionKind()
     * @generated
     * @ordered
     */
    protected static final PortionKind PORTION_KIND_EDEFAULT = PortionKind.SNAPSHOT;

    /**
     * The cached value of the '{@link #getPortionKind() <em>Portion Kind</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getPortionKind()
     * @generated
     * @ordered
     */
    protected PortionKind portionKind = PORTION_KIND_EDEFAULT;

    /**
     * This is true if the Portion Kind attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    protected boolean portionKindESet;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected FlowConnectionUsageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getFlowConnectionUsage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean isIsIndividual() {
        return this.isIndividual;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIsIndividual(boolean newIsIndividual) {
        boolean oldIsIndividual = this.isIndividual;
        this.isIndividual = newIsIndividual;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FLOW_CONNECTION_USAGE__IS_INDIVIDUAL, oldIsIndividual, this.isIndividual));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public PortionKind getPortionKind() {
        return this.portionKind;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setPortionKind(PortionKind newPortionKind) {
        PortionKind oldPortionKind = this.portionKind;
        this.portionKind = newPortionKind == null ? PORTION_KIND_EDEFAULT : newPortionKind;
        boolean oldPortionKindESet = this.portionKindESet;
        this.portionKindESet = true;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.FLOW_CONNECTION_USAGE__PORTION_KIND, oldPortionKind, this.portionKind, !oldPortionKindESet));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void unsetPortionKind() {
        PortionKind oldPortionKind = this.portionKind;
        boolean oldPortionKindESet = this.portionKindESet;
        this.portionKind = PORTION_KIND_EDEFAULT;
        this.portionKindESet = false;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.UNSET, SysmlPackage.FLOW_CONNECTION_USAGE__PORTION_KIND, oldPortionKind, PORTION_KIND_EDEFAULT, oldPortionKindESet));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean isSetPortionKind() {
        return this.portionKindESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public OccurrenceDefinition getIndividualDefinition() {
        OccurrenceDefinition individualDefinition = this.basicGetIndividualDefinition();
        return individualDefinition != null && individualDefinition.eIsProxy() ? (OccurrenceDefinition) this.eResolveProxy((InternalEObject) individualDefinition) : individualDefinition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public OccurrenceDefinition basicGetIndividualDefinition() {
        return this.getOccurrenceDefinition().stream()
                .filter(OccurrenceDefinition.class::isInstance)
                .map(OccurrenceDefinition.class::cast)
                .filter(OccurrenceDefinition::isIsIndividual)
                .findFirst()
                .orElse(null);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<org.eclipse.syson.sysml.Class> getOccurrenceDefinition() {
        List<org.eclipse.syson.sysml.Class> occurrenceDefinitions = new ArrayList<>();
        this.getType().stream()
                .filter(org.eclipse.syson.sysml.Class.class::isInstance)
                .map(org.eclipse.syson.sysml.Class.class::cast)
                .forEach(occurrenceDefinitions::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getOccurrenceUsage_OccurrenceDefinition(), occurrenceDefinitions.size(), occurrenceDefinitions.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Behavior> getBehavior() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getStep_Behavior(), data.size(), data.toArray());
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
     * @generated NOT
     */
    public ItemFeature basicGetItemFeature() {
        return this.getOwnedFeature().stream()
                .filter(ItemFeature.class::isInstance)
                .map(ItemFeature.class::cast)
                .findFirst()
                .orElse(null);
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
     * @generated NOT
     */
    @Override
    public EList<Interaction> getFlowConnectionDefinition() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getFlowConnectionUsage_FlowConnectionDefinition(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Expression argument(int i) {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Feature inputParameter(int i) {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<Feature> inputParameters() {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        return null;
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
            case SysmlPackage.FLOW_CONNECTION_USAGE__IS_INDIVIDUAL:
                return this.isIsIndividual();
            case SysmlPackage.FLOW_CONNECTION_USAGE__PORTION_KIND:
                return this.getPortionKind();
            case SysmlPackage.FLOW_CONNECTION_USAGE__INDIVIDUAL_DEFINITION:
                if (resolve) {
                    return this.getIndividualDefinition();
                }
                return this.basicGetIndividualDefinition();
            case SysmlPackage.FLOW_CONNECTION_USAGE__OCCURRENCE_DEFINITION:
                return this.getOccurrenceDefinition();
            case SysmlPackage.FLOW_CONNECTION_USAGE__BEHAVIOR:
                return this.getBehavior();
            case SysmlPackage.FLOW_CONNECTION_USAGE__PARAMETER:
                return this.getParameter();
            case SysmlPackage.FLOW_CONNECTION_USAGE__ACTION_DEFINITION:
                return this.getActionDefinition();
            case SysmlPackage.FLOW_CONNECTION_USAGE__INTERACTION:
                return this.getInteraction();
            case SysmlPackage.FLOW_CONNECTION_USAGE__ITEM_FEATURE:
                if (resolve) {
                    return this.getItemFeature();
                }
                return this.basicGetItemFeature();
            case SysmlPackage.FLOW_CONNECTION_USAGE__ITEM_FLOW_END:
                return this.getItemFlowEnd();
            case SysmlPackage.FLOW_CONNECTION_USAGE__ITEM_TYPE:
                return this.getItemType();
            case SysmlPackage.FLOW_CONNECTION_USAGE__SOURCE_OUTPUT_FEATURE:
                if (resolve) {
                    return this.getSourceOutputFeature();
                }
                return this.basicGetSourceOutputFeature();
            case SysmlPackage.FLOW_CONNECTION_USAGE__TARGET_INPUT_FEATURE:
                if (resolve) {
                    return this.getTargetInputFeature();
                }
                return this.basicGetTargetInputFeature();
            case SysmlPackage.FLOW_CONNECTION_USAGE__FLOW_CONNECTION_DEFINITION:
                return this.getFlowConnectionDefinition();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case SysmlPackage.FLOW_CONNECTION_USAGE__IS_INDIVIDUAL:
                this.setIsIndividual((Boolean) newValue);
                return;
            case SysmlPackage.FLOW_CONNECTION_USAGE__PORTION_KIND:
                this.setPortionKind((PortionKind) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case SysmlPackage.FLOW_CONNECTION_USAGE__IS_INDIVIDUAL:
                this.setIsIndividual(IS_INDIVIDUAL_EDEFAULT);
                return;
            case SysmlPackage.FLOW_CONNECTION_USAGE__PORTION_KIND:
                this.unsetPortionKind();
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.FLOW_CONNECTION_USAGE__IS_INDIVIDUAL:
                return this.isIndividual != IS_INDIVIDUAL_EDEFAULT;
            case SysmlPackage.FLOW_CONNECTION_USAGE__PORTION_KIND:
                return this.isSetPortionKind();
            case SysmlPackage.FLOW_CONNECTION_USAGE__INDIVIDUAL_DEFINITION:
                return this.basicGetIndividualDefinition() != null;
            case SysmlPackage.FLOW_CONNECTION_USAGE__OCCURRENCE_DEFINITION:
                return !this.getOccurrenceDefinition().isEmpty();
            case SysmlPackage.FLOW_CONNECTION_USAGE__BEHAVIOR:
                return !this.getBehavior().isEmpty();
            case SysmlPackage.FLOW_CONNECTION_USAGE__PARAMETER:
                return !this.getParameter().isEmpty();
            case SysmlPackage.FLOW_CONNECTION_USAGE__ACTION_DEFINITION:
                return !this.getActionDefinition().isEmpty();
            case SysmlPackage.FLOW_CONNECTION_USAGE__INTERACTION:
                return !this.getInteraction().isEmpty();
            case SysmlPackage.FLOW_CONNECTION_USAGE__ITEM_FEATURE:
                return this.basicGetItemFeature() != null;
            case SysmlPackage.FLOW_CONNECTION_USAGE__ITEM_FLOW_END:
                return !this.getItemFlowEnd().isEmpty();
            case SysmlPackage.FLOW_CONNECTION_USAGE__ITEM_TYPE:
                return !this.getItemType().isEmpty();
            case SysmlPackage.FLOW_CONNECTION_USAGE__SOURCE_OUTPUT_FEATURE:
                return this.basicGetSourceOutputFeature() != null;
            case SysmlPackage.FLOW_CONNECTION_USAGE__TARGET_INPUT_FEATURE:
                return this.basicGetTargetInputFeature() != null;
            case SysmlPackage.FLOW_CONNECTION_USAGE__FLOW_CONNECTION_DEFINITION:
                return !this.getFlowConnectionDefinition().isEmpty();
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
        if (baseClass == OccurrenceUsage.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.FLOW_CONNECTION_USAGE__IS_INDIVIDUAL:
                    return SysmlPackage.OCCURRENCE_USAGE__IS_INDIVIDUAL;
                case SysmlPackage.FLOW_CONNECTION_USAGE__PORTION_KIND:
                    return SysmlPackage.OCCURRENCE_USAGE__PORTION_KIND;
                case SysmlPackage.FLOW_CONNECTION_USAGE__INDIVIDUAL_DEFINITION:
                    return SysmlPackage.OCCURRENCE_USAGE__INDIVIDUAL_DEFINITION;
                case SysmlPackage.FLOW_CONNECTION_USAGE__OCCURRENCE_DEFINITION:
                    return SysmlPackage.OCCURRENCE_USAGE__OCCURRENCE_DEFINITION;
                default:
                    return -1;
            }
        }
        if (baseClass == Step.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.FLOW_CONNECTION_USAGE__BEHAVIOR:
                    return SysmlPackage.STEP__BEHAVIOR;
                case SysmlPackage.FLOW_CONNECTION_USAGE__PARAMETER:
                    return SysmlPackage.STEP__PARAMETER;
                default:
                    return -1;
            }
        }
        if (baseClass == ActionUsage.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.FLOW_CONNECTION_USAGE__ACTION_DEFINITION:
                    return SysmlPackage.ACTION_USAGE__ACTION_DEFINITION;
                default:
                    return -1;
            }
        }
        if (baseClass == ItemFlow.class) {
            switch (derivedFeatureID) {
                case SysmlPackage.FLOW_CONNECTION_USAGE__INTERACTION:
                    return SysmlPackage.ITEM_FLOW__INTERACTION;
                case SysmlPackage.FLOW_CONNECTION_USAGE__ITEM_FEATURE:
                    return SysmlPackage.ITEM_FLOW__ITEM_FEATURE;
                case SysmlPackage.FLOW_CONNECTION_USAGE__ITEM_FLOW_END:
                    return SysmlPackage.ITEM_FLOW__ITEM_FLOW_END;
                case SysmlPackage.FLOW_CONNECTION_USAGE__ITEM_TYPE:
                    return SysmlPackage.ITEM_FLOW__ITEM_TYPE;
                case SysmlPackage.FLOW_CONNECTION_USAGE__SOURCE_OUTPUT_FEATURE:
                    return SysmlPackage.ITEM_FLOW__SOURCE_OUTPUT_FEATURE;
                case SysmlPackage.FLOW_CONNECTION_USAGE__TARGET_INPUT_FEATURE:
                    return SysmlPackage.ITEM_FLOW__TARGET_INPUT_FEATURE;
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
        if (baseClass == OccurrenceUsage.class) {
            switch (baseFeatureID) {
                case SysmlPackage.OCCURRENCE_USAGE__IS_INDIVIDUAL:
                    return SysmlPackage.FLOW_CONNECTION_USAGE__IS_INDIVIDUAL;
                case SysmlPackage.OCCURRENCE_USAGE__PORTION_KIND:
                    return SysmlPackage.FLOW_CONNECTION_USAGE__PORTION_KIND;
                case SysmlPackage.OCCURRENCE_USAGE__INDIVIDUAL_DEFINITION:
                    return SysmlPackage.FLOW_CONNECTION_USAGE__INDIVIDUAL_DEFINITION;
                case SysmlPackage.OCCURRENCE_USAGE__OCCURRENCE_DEFINITION:
                    return SysmlPackage.FLOW_CONNECTION_USAGE__OCCURRENCE_DEFINITION;
                default:
                    return -1;
            }
        }
        if (baseClass == Step.class) {
            switch (baseFeatureID) {
                case SysmlPackage.STEP__BEHAVIOR:
                    return SysmlPackage.FLOW_CONNECTION_USAGE__BEHAVIOR;
                case SysmlPackage.STEP__PARAMETER:
                    return SysmlPackage.FLOW_CONNECTION_USAGE__PARAMETER;
                default:
                    return -1;
            }
        }
        if (baseClass == ActionUsage.class) {
            switch (baseFeatureID) {
                case SysmlPackage.ACTION_USAGE__ACTION_DEFINITION:
                    return SysmlPackage.FLOW_CONNECTION_USAGE__ACTION_DEFINITION;
                default:
                    return -1;
            }
        }
        if (baseClass == ItemFlow.class) {
            switch (baseFeatureID) {
                case SysmlPackage.ITEM_FLOW__INTERACTION:
                    return SysmlPackage.FLOW_CONNECTION_USAGE__INTERACTION;
                case SysmlPackage.ITEM_FLOW__ITEM_FEATURE:
                    return SysmlPackage.FLOW_CONNECTION_USAGE__ITEM_FEATURE;
                case SysmlPackage.ITEM_FLOW__ITEM_FLOW_END:
                    return SysmlPackage.FLOW_CONNECTION_USAGE__ITEM_FLOW_END;
                case SysmlPackage.ITEM_FLOW__ITEM_TYPE:
                    return SysmlPackage.FLOW_CONNECTION_USAGE__ITEM_TYPE;
                case SysmlPackage.ITEM_FLOW__SOURCE_OUTPUT_FEATURE:
                    return SysmlPackage.FLOW_CONNECTION_USAGE__SOURCE_OUTPUT_FEATURE;
                case SysmlPackage.ITEM_FLOW__TARGET_INPUT_FEATURE:
                    return SysmlPackage.FLOW_CONNECTION_USAGE__TARGET_INPUT_FEATURE;
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
    public int eDerivedOperationID(int baseOperationID, Class<?> baseClass) {
        if (baseClass == OccurrenceUsage.class) {
            switch (baseOperationID) {
                default:
                    return -1;
            }
        }
        if (baseClass == Step.class) {
            switch (baseOperationID) {
                default:
                    return -1;
            }
        }
        if (baseClass == ActionUsage.class) {
            switch (baseOperationID) {
                case SysmlPackage.ACTION_USAGE___ARGUMENT__INT:
                    return SysmlPackage.FLOW_CONNECTION_USAGE___ARGUMENT__INT;
                case SysmlPackage.ACTION_USAGE___INPUT_PARAMETER__INT:
                    return SysmlPackage.FLOW_CONNECTION_USAGE___INPUT_PARAMETER__INT;
                case SysmlPackage.ACTION_USAGE___INPUT_PARAMETERS:
                    return SysmlPackage.FLOW_CONNECTION_USAGE___INPUT_PARAMETERS;
                case SysmlPackage.ACTION_USAGE___IS_SUBACTION_USAGE:
                    return SysmlPackage.FLOW_CONNECTION_USAGE___IS_SUBACTION_USAGE;
                default:
                    return -1;
            }
        }
        if (baseClass == ItemFlow.class) {
            switch (baseOperationID) {
                default:
                    return -1;
            }
        }
        return super.eDerivedOperationID(baseOperationID, baseClass);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
        switch (operationID) {
            case SysmlPackage.FLOW_CONNECTION_USAGE___ARGUMENT__INT:
                return this.argument((Integer) arguments.get(0));
            case SysmlPackage.FLOW_CONNECTION_USAGE___INPUT_PARAMETER__INT:
                return this.inputParameter((Integer) arguments.get(0));
            case SysmlPackage.FLOW_CONNECTION_USAGE___INPUT_PARAMETERS:
                return this.inputParameters();
            case SysmlPackage.FLOW_CONNECTION_USAGE___IS_SUBACTION_USAGE:
                return this.isSubactionUsage();
        }
        return super.eInvoke(operationID, arguments);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String toString() {
        if (this.eIsProxy()) {
            return super.toString();
        }

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (isIndividual: ");
        result.append(this.isIndividual);
        result.append(", portionKind: ");
        if (this.portionKindESet) {
            result.append(this.portionKind);
        } else {
            result.append("<unset>");
        }
        result.append(')');
        return result.toString();
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Behavior> getActionDefinition() {
        EList<Behavior> actionDefinitions = new BasicEList<>();
        EList<Interaction> flowConnectionDefinition = this.getFlowConnectionDefinition();
        if (flowConnectionDefinition != null) {
            actionDefinitions.addAll(flowConnectionDefinition);
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getActionUsage_ActionDefinition(), actionDefinitions.size(), actionDefinitions.toArray());
    }

    /**
     * <!-- begin-user-doc --> Redefines getter generated from eAnnotation <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Interaction> getInteraction() {
        EList<Interaction> interactions = new BasicEList<>();
        EList<Interaction> flowConnectionDefinition = this.getFlowConnectionDefinition();
        if (flowConnectionDefinition != null) {
            interactions.addAll(flowConnectionDefinition);
        }
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getItemFlow_Interaction(), interactions.size(), interactions.toArray());
    }

} // FlowConnectionUsageImpl
