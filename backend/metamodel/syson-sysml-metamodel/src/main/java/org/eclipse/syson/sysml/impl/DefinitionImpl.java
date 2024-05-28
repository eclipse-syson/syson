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
import org.eclipse.syson.sysml.Membership;
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
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.UseCaseUsage;
import org.eclipse.syson.sysml.VariantMembership;
import org.eclipse.syson.sysml.VerificationCaseUsage;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.ViewpointUsage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Definition</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#isIsVariation <em>Is Variation</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getDirectedUsage <em>Directed Usage</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedAction <em>Owned Action</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedAllocation <em>Owned Allocation</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedAnalysisCase <em>Owned Analysis Case</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedAttribute <em>Owned Attribute</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedCalculation <em>Owned Calculation</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedCase <em>Owned Case</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedConcern <em>Owned Concern</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedConnection <em>Owned Connection</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedConstraint <em>Owned Constraint</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedEnumeration <em>Owned Enumeration</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedFlow <em>Owned Flow</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedInterface <em>Owned Interface</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedItem <em>Owned Item</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedMetadata <em>Owned Metadata</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedOccurrence <em>Owned Occurrence</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedPart <em>Owned Part</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedPort <em>Owned Port</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedReference <em>Owned Reference</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedRendering <em>Owned Rendering</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedRequirement <em>Owned Requirement</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedState <em>Owned State</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedTransition <em>Owned Transition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedUsage <em>Owned Usage</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedUseCase <em>Owned Use Case</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedVerificationCase <em>Owned Verification
 * Case</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedView <em>Owned View</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getOwnedViewpoint <em>Owned Viewpoint</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getUsage <em>Usage</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getVariant <em>Variant</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.impl.DefinitionImpl#getVariantMembership <em>Variant Membership</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DefinitionImpl extends ClassifierImpl implements Definition {
    /**
     * The default value of the '{@link #isIsVariation() <em>Is Variation</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsVariation()
     * @generated
     * @ordered
     */
    protected static final boolean IS_VARIATION_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isIsVariation() <em>Is Variation</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #isIsVariation()
     * @generated
     * @ordered
     */
    protected boolean isVariation = IS_VARIATION_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected DefinitionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return SysmlPackage.eINSTANCE.getDefinition();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Usage> getDirectedUsage() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_DirectedUsage(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean isIsVariation() {
        return this.isVariation;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIsVariation(boolean newIsVariation) {
        boolean oldIsVariation = this.isVariation;
        this.isVariation = newIsVariation;
        if (this.eNotificationRequired())
            this.eNotify(new ENotificationImpl(this, Notification.SET, SysmlPackage.DEFINITION__IS_VARIATION, oldIsVariation, this.isVariation));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<ActionUsage> getOwnedAction() {
        List<ActionUsage> actions = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .flatMap(fm -> fm.getOwnedRelatedElement().stream())
                .filter(ActionUsage.class::isInstance)
                .map(ActionUsage.class::cast)
                .forEach(actions::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedAction(), actions.size(), actions.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<AllocationUsage> getOwnedAllocation() {
        List<AllocationUsage> allocations = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .flatMap(fm -> fm.getOwnedRelatedElement().stream())
                .filter(AllocationUsage.class::isInstance)
                .map(AllocationUsage.class::cast)
                .forEach(allocations::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedAllocation(), allocations.size(), allocations.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<AnalysisCaseUsage> getOwnedAnalysisCase() {
        List<AnalysisCaseUsage> analysisCases = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .flatMap(fm -> fm.getOwnedRelatedElement().stream())
                .filter(AnalysisCaseUsage.class::isInstance)
                .map(AnalysisCaseUsage.class::cast)
                .forEach(analysisCases::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedAnalysisCase(), analysisCases.size(), analysisCases.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
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
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<CalculationUsage> getOwnedCalculation() {
        List<CalculationUsage> calculations = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .flatMap(fm -> fm.getOwnedRelatedElement().stream())
                .filter(CalculationUsage.class::isInstance)
                .map(CalculationUsage.class::cast)
                .forEach(calculations::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedCalculation(), calculations.size(), calculations.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<CaseUsage> getOwnedCase() {
        List<CaseUsage> cases = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .flatMap(fm -> fm.getOwnedRelatedElement().stream())
                .filter(CaseUsage.class::isInstance)
                .map(CaseUsage.class::cast)
                .forEach(cases::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedCase(), cases.size(), cases.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<ConcernUsage> getOwnedConcern() {
        List<ConcernUsage> concerns = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .flatMap(fm -> fm.getOwnedRelatedElement().stream())
                .filter(ConcernUsage.class::isInstance)
                .map(ConcernUsage.class::cast)
                .forEach(concerns::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedConcern(), concerns.size(), concerns.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<ConnectorAsUsage> getOwnedConnection() {
        List<ConnectorAsUsage> connections = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .flatMap(fm -> fm.getOwnedRelatedElement().stream())
                .filter(ConnectorAsUsage.class::isInstance)
                .map(ConnectorAsUsage.class::cast)
                .forEach(connections::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedConnection(), connections.size(), connections.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<ConstraintUsage> getOwnedConstraint() {
        List<ConstraintUsage> constraints = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .flatMap(fm -> fm.getOwnedRelatedElement().stream())
                .filter(ConstraintUsage.class::isInstance)
                .map(ConstraintUsage.class::cast)
                .forEach(constraints::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedConstraint(), constraints.size(), constraints.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<EnumerationUsage> getOwnedEnumeration() {
        List<EnumerationUsage> enumerations = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .flatMap(fm -> fm.getOwnedRelatedElement().stream())
                .filter(EnumerationUsage.class::isInstance)
                .map(EnumerationUsage.class::cast)
                .forEach(enumerations::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedEnumeration(), enumerations.size(), enumerations.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<FlowConnectionUsage> getOwnedFlow() {
        List<FlowConnectionUsage> flows = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .flatMap(fm -> fm.getOwnedRelatedElement().stream())
                .filter(FlowConnectionUsage.class::isInstance)
                .map(FlowConnectionUsage.class::cast)
                .forEach(flows::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedFlow(), flows.size(), flows.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<InterfaceUsage> getOwnedInterface() {
        List<InterfaceUsage> interfaces = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .flatMap(fm -> fm.getOwnedRelatedElement().stream())
                .filter(InterfaceUsage.class::isInstance)
                .map(InterfaceUsage.class::cast)
                .forEach(interfaces::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedInterface(), interfaces.size(), interfaces.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
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
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<MetadataUsage> getOwnedMetadata() {
        List<MetadataUsage> metadatas = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .flatMap(fm -> fm.getOwnedRelatedElement().stream())
                .filter(MetadataUsage.class::isInstance)
                .map(MetadataUsage.class::cast)
                .forEach(metadatas::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedMetadata(), metadatas.size(), metadatas.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<OccurrenceUsage> getOwnedOccurrence() {
        List<OccurrenceUsage> occurences = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .flatMap(fm -> fm.getOwnedRelatedElement().stream())
                .filter(OccurrenceUsage.class::isInstance)
                .map(OccurrenceUsage.class::cast)
                .forEach(occurences::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedOccurrence(), occurences.size(), occurences.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
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
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
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
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<ReferenceUsage> getOwnedReference() {
        List<ReferenceUsage> references = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .flatMap(fm -> fm.getOwnedRelatedElement().stream())
                .filter(ReferenceUsage.class::isInstance)
                .map(ReferenceUsage.class::cast)
                .forEach(references::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedReference(), references.size(), references.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<RenderingUsage> getOwnedRendering() {
        List<RenderingUsage> renderings = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .flatMap(fm -> fm.getOwnedRelatedElement().stream())
                .filter(RenderingUsage.class::isInstance)
                .map(RenderingUsage.class::cast)
                .forEach(renderings::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedRendering(), renderings.size(), renderings.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<RequirementUsage> getOwnedRequirement() {
        List<RequirementUsage> requirements = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .flatMap(fm -> fm.getOwnedRelatedElement().stream())
                .filter(RequirementUsage.class::isInstance)
                .map(RequirementUsage.class::cast)
                .forEach(requirements::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedRequirement(), requirements.size(), requirements.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<StateUsage> getOwnedState() {
        List<StateUsage> states = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .flatMap(fm -> fm.getOwnedRelatedElement().stream())
                .filter(StateUsage.class::isInstance)
                .map(StateUsage.class::cast)
                .forEach(states::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedState(), states.size(), states.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<TransitionUsage> getOwnedTransition() {
        List<TransitionUsage> transitions = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .flatMap(fm -> fm.getOwnedRelatedElement().stream())
                .filter(TransitionUsage.class::isInstance)
                .map(TransitionUsage.class::cast)
                .forEach(transitions::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedTransition(), transitions.size(), transitions.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Usage> getOwnedUsage() {
        List<Usage> usages = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .flatMap(fm -> fm.getOwnedRelatedElement().stream())
                .filter(Usage.class::isInstance)
                .map(Usage.class::cast)
                .forEach(usages::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedUsage(), usages.size(), usages.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<UseCaseUsage> getOwnedUseCase() {
        List<UseCaseUsage> useCases = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .flatMap(fm -> fm.getOwnedRelatedElement().stream())
                .filter(UseCaseUsage.class::isInstance)
                .map(UseCaseUsage.class::cast)
                .forEach(useCases::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedUseCase(), useCases.size(), useCases.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<VerificationCaseUsage> getOwnedVerificationCase() {
        List<VerificationCaseUsage> verificationCases = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .flatMap(fm -> fm.getOwnedRelatedElement().stream())
                .filter(VerificationCaseUsage.class::isInstance)
                .map(VerificationCaseUsage.class::cast)
                .forEach(verificationCases::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedVerificationCase(), verificationCases.size(), verificationCases.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<ViewUsage> getOwnedView() {
        List<ViewUsage> views = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .flatMap(fm -> fm.getOwnedRelatedElement().stream())
                .filter(ViewUsage.class::isInstance)
                .map(ViewUsage.class::cast)
                .forEach(views::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedView(), views.size(), views.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<ViewpointUsage> getOwnedViewpoint() {
        List<ViewpointUsage> viewpoints = new ArrayList<>();
        this.getOwnedRelationship().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .flatMap(fm -> fm.getOwnedRelatedElement().stream())
                .filter(ViewpointUsage.class::isInstance)
                .map(ViewpointUsage.class::cast)
                .forEach(viewpoints::add);
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_OwnedViewpoint(), viewpoints.size(), viewpoints.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Usage> getUsage() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_Usage(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Usage> getVariant() {
        List<Usage> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_Variant(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<VariantMembership> getVariantMembership() {
        List<VariantMembership> data = new ArrayList<>();
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getDefinition_VariantMembership(), data.size(), data.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Membership> getInheritedMembership() {
        List<Membership> inheritedMemberships = new ArrayList<>();
        this.getOwnedSpecialization().forEach(spe -> {
            Type general = spe.getGeneral();
            if (general != null) {
                inheritedMemberships.addAll(general.getInheritedMembership());
                inheritedMemberships.addAll(general.getOwnedMembership());
            }
        });
        return new EcoreEList.UnmodifiableEList<>(this, SysmlPackage.eINSTANCE.getType_InheritedMembership(), inheritedMemberships.size(), inheritedMemberships.toArray());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case SysmlPackage.DEFINITION__IS_VARIATION:
                return this.isIsVariation();
            case SysmlPackage.DEFINITION__DIRECTED_USAGE:
                return this.getDirectedUsage();
            case SysmlPackage.DEFINITION__OWNED_ACTION:
                return this.getOwnedAction();
            case SysmlPackage.DEFINITION__OWNED_ALLOCATION:
                return this.getOwnedAllocation();
            case SysmlPackage.DEFINITION__OWNED_ANALYSIS_CASE:
                return this.getOwnedAnalysisCase();
            case SysmlPackage.DEFINITION__OWNED_ATTRIBUTE:
                return this.getOwnedAttribute();
            case SysmlPackage.DEFINITION__OWNED_CALCULATION:
                return this.getOwnedCalculation();
            case SysmlPackage.DEFINITION__OWNED_CASE:
                return this.getOwnedCase();
            case SysmlPackage.DEFINITION__OWNED_CONCERN:
                return this.getOwnedConcern();
            case SysmlPackage.DEFINITION__OWNED_CONNECTION:
                return this.getOwnedConnection();
            case SysmlPackage.DEFINITION__OWNED_CONSTRAINT:
                return this.getOwnedConstraint();
            case SysmlPackage.DEFINITION__OWNED_ENUMERATION:
                return this.getOwnedEnumeration();
            case SysmlPackage.DEFINITION__OWNED_FLOW:
                return this.getOwnedFlow();
            case SysmlPackage.DEFINITION__OWNED_INTERFACE:
                return this.getOwnedInterface();
            case SysmlPackage.DEFINITION__OWNED_ITEM:
                return this.getOwnedItem();
            case SysmlPackage.DEFINITION__OWNED_METADATA:
                return this.getOwnedMetadata();
            case SysmlPackage.DEFINITION__OWNED_OCCURRENCE:
                return this.getOwnedOccurrence();
            case SysmlPackage.DEFINITION__OWNED_PART:
                return this.getOwnedPart();
            case SysmlPackage.DEFINITION__OWNED_PORT:
                return this.getOwnedPort();
            case SysmlPackage.DEFINITION__OWNED_REFERENCE:
                return this.getOwnedReference();
            case SysmlPackage.DEFINITION__OWNED_RENDERING:
                return this.getOwnedRendering();
            case SysmlPackage.DEFINITION__OWNED_REQUIREMENT:
                return this.getOwnedRequirement();
            case SysmlPackage.DEFINITION__OWNED_STATE:
                return this.getOwnedState();
            case SysmlPackage.DEFINITION__OWNED_TRANSITION:
                return this.getOwnedTransition();
            case SysmlPackage.DEFINITION__OWNED_USAGE:
                return this.getOwnedUsage();
            case SysmlPackage.DEFINITION__OWNED_USE_CASE:
                return this.getOwnedUseCase();
            case SysmlPackage.DEFINITION__OWNED_VERIFICATION_CASE:
                return this.getOwnedVerificationCase();
            case SysmlPackage.DEFINITION__OWNED_VIEW:
                return this.getOwnedView();
            case SysmlPackage.DEFINITION__OWNED_VIEWPOINT:
                return this.getOwnedViewpoint();
            case SysmlPackage.DEFINITION__USAGE:
                return this.getUsage();
            case SysmlPackage.DEFINITION__VARIANT:
                return this.getVariant();
            case SysmlPackage.DEFINITION__VARIANT_MEMBERSHIP:
                return this.getVariantMembership();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case SysmlPackage.DEFINITION__IS_VARIATION:
                this.setIsVariation((Boolean) newValue);
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
            case SysmlPackage.DEFINITION__IS_VARIATION:
                this.setIsVariation(IS_VARIATION_EDEFAULT);
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
            case SysmlPackage.DEFINITION__IS_VARIATION:
                return this.isVariation != IS_VARIATION_EDEFAULT;
            case SysmlPackage.DEFINITION__DIRECTED_USAGE:
                return !this.getDirectedUsage().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_ACTION:
                return !this.getOwnedAction().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_ALLOCATION:
                return !this.getOwnedAllocation().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_ANALYSIS_CASE:
                return !this.getOwnedAnalysisCase().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_ATTRIBUTE:
                return !this.getOwnedAttribute().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_CALCULATION:
                return !this.getOwnedCalculation().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_CASE:
                return !this.getOwnedCase().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_CONCERN:
                return !this.getOwnedConcern().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_CONNECTION:
                return !this.getOwnedConnection().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_CONSTRAINT:
                return !this.getOwnedConstraint().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_ENUMERATION:
                return !this.getOwnedEnumeration().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_FLOW:
                return !this.getOwnedFlow().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_INTERFACE:
                return !this.getOwnedInterface().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_ITEM:
                return !this.getOwnedItem().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_METADATA:
                return !this.getOwnedMetadata().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_OCCURRENCE:
                return !this.getOwnedOccurrence().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_PART:
                return !this.getOwnedPart().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_PORT:
                return !this.getOwnedPort().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_REFERENCE:
                return !this.getOwnedReference().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_RENDERING:
                return !this.getOwnedRendering().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_REQUIREMENT:
                return !this.getOwnedRequirement().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_STATE:
                return !this.getOwnedState().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_TRANSITION:
                return !this.getOwnedTransition().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_USAGE:
                return !this.getOwnedUsage().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_USE_CASE:
                return !this.getOwnedUseCase().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_VERIFICATION_CASE:
                return !this.getOwnedVerificationCase().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_VIEW:
                return !this.getOwnedView().isEmpty();
            case SysmlPackage.DEFINITION__OWNED_VIEWPOINT:
                return !this.getOwnedViewpoint().isEmpty();
            case SysmlPackage.DEFINITION__USAGE:
                return !this.getUsage().isEmpty();
            case SysmlPackage.DEFINITION__VARIANT:
                return !this.getVariant().isEmpty();
            case SysmlPackage.DEFINITION__VARIANT_MEMBERSHIP:
                return !this.getVariantMembership().isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String toString() {
        if (this.eIsProxy())
            return super.toString();

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (isVariation: ");
        result.append(this.isVariation);
        result.append(')');
        return result.toString();
    }

} // DefinitionImpl
