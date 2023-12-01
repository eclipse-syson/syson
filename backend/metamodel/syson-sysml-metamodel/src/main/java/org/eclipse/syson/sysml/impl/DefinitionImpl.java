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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreEList;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.AllocationUsage;
import org.eclipse.syson.sysml.AnalysisCaseUsage;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.CalculationUsage;
import org.eclipse.syson.sysml.CaseUsage;
import org.eclipse.syson.sysml.ConcernUsage;
import org.eclipse.syson.sysml.ConnectorAsUsage;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.EnumerationUsage;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FlowConnectionUsage;
import org.eclipse.syson.sysml.InterfaceUsage;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.MetadataUsage;
import org.eclipse.syson.sysml.OccurrenceUsage;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.RenderingUsage;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.UseCaseUsage;
import org.eclipse.syson.sysml.VariantMembership;
import org.eclipse.syson.sysml.VerificationCaseUsage;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.ViewpointUsage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Definition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#isIsVariation <em>Is Variation</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getDirectedUsage <em>Directed Usage</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedAction <em>Owned Action</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedAllocation <em>Owned Allocation</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedAnalysisCase <em>Owned Analysis Case</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedAttribute <em>Owned Attribute</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedCalculation <em>Owned Calculation</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedCase <em>Owned Case</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedConcern <em>Owned Concern</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedConnection <em>Owned Connection</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedConstraint <em>Owned Constraint</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedEnumeration <em>Owned Enumeration</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedFlow <em>Owned Flow</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedInterface <em>Owned Interface</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedItem <em>Owned Item</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedMetadata <em>Owned Metadata</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedOccurrence <em>Owned Occurrence</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedPart <em>Owned Part</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedPort <em>Owned Port</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedReference <em>Owned Reference</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedRendering <em>Owned Rendering</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedRequirement <em>Owned Requirement</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedState <em>Owned State</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedTransition <em>Owned Transition</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedUsage <em>Owned Usage</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedUseCase <em>Owned Use Case</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedVerificationCase <em>Owned Verification Case</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedView <em>Owned View</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedViewpoint <em>Owned Viewpoint</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getUsage <em>Usage</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getVariant <em>Variant</em>}</li>
 *   <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getVariantMembership <em>Variant Membership</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DefinitionImpl extends ClassifierImpl implements Definition {
    /**
     * The default value of the '{@link #isIsVariation() <em>Is Variation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsVariation()
     * @generated
     * @ordered
     */
    protected static final boolean IS_VARIATION_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsVariation() <em>Is Variation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isIsVariation()
     * @generated
     * @ordered
     */
    protected boolean isVariation = IS_VARIATION_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DefinitionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getDefinition();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Usage> getDirectedUsage() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_DirectedUsage(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean isIsVariation() {
        return isVariation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setIsVariation(boolean newIsVariation) {
        boolean oldIsVariation = isVariation;
        isVariation = newIsVariation;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.DEFINITION__IS_VARIATION, oldIsVariation, isVariation));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<ActionUsage> getOwnedAction() {
        List<ActionUsage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedAction(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<AllocationUsage> getOwnedAllocation() {
        List<AllocationUsage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedAllocation(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<AnalysisCaseUsage> getOwnedAnalysisCase() {
        List<AnalysisCaseUsage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedAnalysisCase(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<AttributeUsage> getOwnedAttribute() {
        List<AttributeUsage> attributes = new ArrayList<>();
        this.getOwnedRelationship().stream()
            .filter(FeatureMembership.class::isInstance)
            .map(FeatureMembership.class::cast)
            .flatMap(fm -> fm.getOwnedRelatedElement().stream())
            .filter(AttributeUsage.class::isInstance)
            .map(AttributeUsage.class::cast)
            .forEach(attributes::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(), attributes.size(), attributes.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<CalculationUsage> getOwnedCalculation() {
        List<CalculationUsage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedCalculation(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<CaseUsage> getOwnedCase() {
        List<CaseUsage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedCase(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<ConcernUsage> getOwnedConcern() {
        List<ConcernUsage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedConcern(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<ConnectorAsUsage> getOwnedConnection() {
        List<ConnectorAsUsage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedConnection(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<ConstraintUsage> getOwnedConstraint() {
        List<ConstraintUsage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedConstraint(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<EnumerationUsage> getOwnedEnumeration() {
        List<EnumerationUsage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedEnumeration(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<FlowConnectionUsage> getOwnedFlow() {
        List<FlowConnectionUsage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedFlow(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<InterfaceUsage> getOwnedInterface() {
        List<InterfaceUsage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedInterface(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<ItemUsage> getOwnedItem() {
        List<ItemUsage> ownedItems = new ArrayList<>();
        this.getOwnedRelationship().stream()
            .filter(FeatureMembership.class::isInstance)
            .map(FeatureMembership.class::cast)
            .flatMap(fm -> fm.getOwnedRelatedElement().stream())
            .filter(ItemUsage.class::isInstance)
            .map(ItemUsage.class::cast)
            .forEach(ownedItems::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedItem(), ownedItems.size(), ownedItems.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<MetadataUsage> getOwnedMetadata() {
        List<MetadataUsage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedMetadata(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<OccurrenceUsage> getOwnedOccurrence() {
        List<OccurrenceUsage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedOccurrence(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<PartUsage> getOwnedPart() {
        List<PartUsage> omnedParts = new ArrayList<>();
        this.getOwnedRelationship().stream()
            .filter(FeatureMembership.class::isInstance)
            .map(FeatureMembership.class::cast)
            .flatMap(fm -> fm.getOwnedRelatedElement().stream())
            .filter(PartUsage.class::isInstance)
            .map(PartUsage.class::cast)
            .forEach(omnedParts::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedPart(), omnedParts.size(), omnedParts.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<PortUsage> getOwnedPort() {
        List<PortUsage> ownedPorts = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .flatMap(fm -> fm.getOwnedRelatedElement().stream())
                .filter(PortUsage.class::isInstance)
                .map(PortUsage.class::cast)
                .forEach(ownedPorts::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedPort(), ownedPorts.size(), ownedPorts.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<ReferenceUsage> getOwnedReference() {
        List<ReferenceUsage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedReference(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<RenderingUsage> getOwnedRendering() {
        List<RenderingUsage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedRendering(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<RequirementUsage> getOwnedRequirement() {
        List<RequirementUsage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedRequirement(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<StateUsage> getOwnedState() {
        List<StateUsage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedState(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<TransitionUsage> getOwnedTransition() {
        List<TransitionUsage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedTransition(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Usage> getOwnedUsage() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedUsage(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<UseCaseUsage> getOwnedUseCase() {
        List<UseCaseUsage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedUseCase(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<VerificationCaseUsage> getOwnedVerificationCase() {
        List<VerificationCaseUsage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedVerificationCase(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<ViewUsage> getOwnedView() {
        List<ViewUsage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedView(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<ViewpointUsage> getOwnedViewpoint() {
        List<ViewpointUsage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedViewpoint(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Usage> getUsage() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_Usage(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<Usage> getVariant() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_Variant(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EList<VariantMembership> getVariantMembership() {
        List<VariantMembership> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_VariantMembership(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.DEFINITION__IS_VARIATION:
                return isIsVariation();
            case SysmlPackage.DEFINITION__DIRECTED_USAGE:
                return getDirectedUsage();
            case SysmlPackage.DEFINITION__OWNED_ACTION:
                return getOwnedAction();
            case SysmlPackage.DEFINITION__OWNED_ALLOCATION:
                return getOwnedAllocation();
            case SysmlPackage.DEFINITION__OWNED_ANALYSIS_CASE:
                return getOwnedAnalysisCase();
            case SysmlPackage.DEFINITION__OWNED_ATTRIBUTE:
                return getOwnedAttribute();
            case SysmlPackage.DEFINITION__OWNED_CALCULATION:
                return getOwnedCalculation();
            case SysmlPackage.DEFINITION__OWNED_CASE:
                return getOwnedCase();
            case SysmlPackage.DEFINITION__OWNED_CONCERN:
                return getOwnedConcern();
            case SysmlPackage.DEFINITION__OWNED_CONNECTION:
                return getOwnedConnection();
            case SysmlPackage.DEFINITION__OWNED_CONSTRAINT:
                return getOwnedConstraint();
            case SysmlPackage.DEFINITION__OWNED_ENUMERATION:
                return getOwnedEnumeration();
            case SysmlPackage.DEFINITION__OWNED_FLOW:
                return getOwnedFlow();
            case SysmlPackage.DEFINITION__OWNED_INTERFACE:
                return getOwnedInterface();
            case SysmlPackage.DEFINITION__OWNED_ITEM:
                return getOwnedItem();
            case SysmlPackage.DEFINITION__OWNED_METADATA:
                return getOwnedMetadata();
            case SysmlPackage.DEFINITION__OWNED_OCCURRENCE:
                return getOwnedOccurrence();
            case SysmlPackage.DEFINITION__OWNED_PART:
                return getOwnedPart();
            case SysmlPackage.DEFINITION__OWNED_PORT:
                return getOwnedPort();
            case SysmlPackage.DEFINITION__OWNED_REFERENCE:
                return getOwnedReference();
            case SysmlPackage.DEFINITION__OWNED_RENDERING:
                return getOwnedRendering();
            case SysmlPackage.DEFINITION__OWNED_REQUIREMENT:
                return getOwnedRequirement();
            case SysmlPackage.DEFINITION__OWNED_STATE:
                return getOwnedState();
            case SysmlPackage.DEFINITION__OWNED_TRANSITION:
                return getOwnedTransition();
            case SysmlPackage.DEFINITION__OWNED_USAGE:
                return getOwnedUsage();
            case SysmlPackage.DEFINITION__OWNED_USE_CASE:
                return getOwnedUseCase();
            case SysmlPackage.DEFINITION__OWNED_VERIFICATION_CASE:
                return getOwnedVerificationCase();
            case SysmlPackage.DEFINITION__OWNED_VIEW:
                return getOwnedView();
            case SysmlPackage.DEFINITION__OWNED_VIEWPOINT:
                return getOwnedViewpoint();
            case SysmlPackage.DEFINITION__USAGE:
                return getUsage();
            case SysmlPackage.DEFINITION__VARIANT:
                return getVariant();
            case SysmlPackage.DEFINITION__VARIANT_MEMBERSHIP:
                return getVariantMembership();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case SysmlPackage.DEFINITION__IS_VARIATION:
                setIsVariation((Boolean)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case SysmlPackage.DEFINITION__IS_VARIATION:
                setIsVariation(IS_VARIATION_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case SysmlPackage.DEFINITION__IS_VARIATION:
                return isVariation != IS_VARIATION_EDEFAULT;
            case SysmlPackage.DEFINITION__DIRECTED_USAGE:
                return !getDirectedUsage().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_ACTION:
                return !getOwnedAction().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_ALLOCATION:
                return !getOwnedAllocation().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_ANALYSIS_CASE:
                return !getOwnedAnalysisCase().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_ATTRIBUTE:
                return !getOwnedAttribute().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_CALCULATION:
                return !getOwnedCalculation().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_CASE:
                return !getOwnedCase().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_CONCERN:
                return !getOwnedConcern().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_CONNECTION:
                return !getOwnedConnection().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_CONSTRAINT:
                return !getOwnedConstraint().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_ENUMERATION:
                return !getOwnedEnumeration().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_FLOW:
                return !getOwnedFlow().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_INTERFACE:
                return !getOwnedInterface().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_ITEM:
                return !getOwnedItem().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_METADATA:
                return !getOwnedMetadata().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_OCCURRENCE:
                return !getOwnedOccurrence().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_PART:
                return !getOwnedPart().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_PORT:
                return !getOwnedPort().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_REFERENCE:
                return !getOwnedReference().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_RENDERING:
                return !getOwnedRendering().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_REQUIREMENT:
                return !getOwnedRequirement().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_STATE:
                return !getOwnedState().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_TRANSITION:
                return !getOwnedTransition().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_USAGE:
                return !getOwnedUsage().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_USE_CASE:
                return !getOwnedUseCase().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_VERIFICATION_CASE:
                return !getOwnedVerificationCase().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_VIEW:
                return !getOwnedView().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_VIEWPOINT:
                return !getOwnedViewpoint().isEmpty();
            case SysmlPackage.DEFINITION__USAGE:
                return !getUsage().isEmpty();
            case SysmlPackage.DEFINITION__VARIANT:
                return !getVariant().isEmpty();
            case SysmlPackage.DEFINITION__VARIANT_MEMBERSHIP:
                return !getVariantMembership().isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (isVariation: ");
        result.append(isVariation);
        result.append(')');
        return result.toString();
    }

} //DefinitionImpl
