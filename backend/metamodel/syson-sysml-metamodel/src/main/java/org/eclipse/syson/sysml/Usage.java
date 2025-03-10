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
package org.eclipse.syson.sysml;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Usage</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.Usage#isIsReference <em>Is Reference</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#isIsVariation <em>Is Variation</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getDefinition <em>Definition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getDirectedUsage <em>Directed Usage</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getNestedAction <em>Nested Action</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getNestedAllocation <em>Nested Allocation</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getNestedAnalysisCase <em>Nested Analysis Case</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getNestedAttribute <em>Nested Attribute</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getNestedCalculation <em>Nested Calculation</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getNestedCase <em>Nested Case</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getNestedConcern <em>Nested Concern</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getNestedConnection <em>Nested Connection</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getNestedConstraint <em>Nested Constraint</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getNestedEnumeration <em>Nested Enumeration</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getNestedFlow <em>Nested Flow</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getNestedInterface <em>Nested Interface</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getNestedItem <em>Nested Item</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getNestedMetadata <em>Nested Metadata</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getNestedOccurrence <em>Nested Occurrence</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getNestedPart <em>Nested Part</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getNestedPort <em>Nested Port</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getNestedReference <em>Nested Reference</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getNestedRendering <em>Nested Rendering</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getNestedRequirement <em>Nested Requirement</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getNestedState <em>Nested State</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getNestedTransition <em>Nested Transition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getNestedUsage <em>Nested Usage</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getNestedUseCase <em>Nested Use Case</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getNestedVerificationCase <em>Nested Verification Case</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getNestedView <em>Nested View</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getNestedViewpoint <em>Nested Viewpoint</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getOwningDefinition <em>Owning Definition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getOwningUsage <em>Owning Usage</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getUsage <em>Usage</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getVariant <em>Variant</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Usage#getVariantMembership <em>Variant Membership</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getUsage()
 * @model
 * @generated
 */
public interface Usage extends Feature {
    /**
     * Returns the value of the '<em><b>Definition</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Classifier}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Definition</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_Definition()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="redefines"
     * @generated
     */
    EList<Classifier> getDefinition();

    /**
     * Returns the value of the '<em><b>Directed Usage</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Usage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Directed Usage</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_DirectedUsage()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<Usage> getDirectedUsage();

    /**
     * Returns the value of the '<em><b>Is Reference</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Is Reference</em>' attribute.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_IsReference()
     * @model required="true" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     * @generated
     */
    boolean isIsReference();

    /**
     * Returns the value of the '<em><b>Is Variation</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Is Variation</em>' attribute.
     * @see #setIsVariation(boolean)
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_IsVariation()
     * @model required="true" ordered="false"
     * @generated
     */
    boolean isIsVariation();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Usage#isIsVariation <em>Is Variation</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Is Variation</em>' attribute.
     * @see #isIsVariation()
     * @generated
     */
    void setIsVariation(boolean value);

    /**
     * Returns the value of the '<em><b>Nested Action</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.ActionUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nested Action</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_NestedAction()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<ActionUsage> getNestedAction();

    /**
     * Returns the value of the '<em><b>Nested Allocation</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.AllocationUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nested Allocation</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_NestedAllocation()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<AllocationUsage> getNestedAllocation();

    /**
     * Returns the value of the '<em><b>Nested Analysis Case</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.AnalysisCaseUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nested Analysis Case</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_NestedAnalysisCase()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<AnalysisCaseUsage> getNestedAnalysisCase();

    /**
     * Returns the value of the '<em><b>Nested Attribute</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.AttributeUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nested Attribute</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_NestedAttribute()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<AttributeUsage> getNestedAttribute();

    /**
     * Returns the value of the '<em><b>Nested Calculation</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.CalculationUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nested Calculation</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_NestedCalculation()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<CalculationUsage> getNestedCalculation();

    /**
     * Returns the value of the '<em><b>Nested Case</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.CaseUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nested Case</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_NestedCase()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<CaseUsage> getNestedCase();

    /**
     * Returns the value of the '<em><b>Nested Concern</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.ConcernUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nested Concern</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_NestedConcern()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false" annotation="subsets"
     * @generated
     */
    EList<ConcernUsage> getNestedConcern();

    /**
     * Returns the value of the '<em><b>Nested Connection</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.ConnectorAsUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nested Connection</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_NestedConnection()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<ConnectorAsUsage> getNestedConnection();

    /**
     * Returns the value of the '<em><b>Nested Constraint</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.ConstraintUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nested Constraint</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_NestedConstraint()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<ConstraintUsage> getNestedConstraint();

    /**
     * Returns the value of the '<em><b>Nested Enumeration</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.EnumerationUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nested Enumeration</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_NestedEnumeration()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<EnumerationUsage> getNestedEnumeration();

    /**
     * Returns the value of the '<em><b>Nested Flow</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.FlowConnectionUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nested Flow</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_NestedFlow()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false" annotation="subsets"
     * @generated
     */
    EList<FlowConnectionUsage> getNestedFlow();

    /**
     * Returns the value of the '<em><b>Nested Interface</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.InterfaceUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nested Interface</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_NestedInterface()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<InterfaceUsage> getNestedInterface();

    /**
     * Returns the value of the '<em><b>Nested Item</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.ItemUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nested Item</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_NestedItem()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<ItemUsage> getNestedItem();

    /**
     * Returns the value of the '<em><b>Nested Metadata</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.MetadataUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nested Metadata</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_NestedMetadata()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<MetadataUsage> getNestedMetadata();

    /**
     * Returns the value of the '<em><b>Nested Occurrence</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.OccurrenceUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nested Occurrence</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_NestedOccurrence()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<OccurrenceUsage> getNestedOccurrence();

    /**
     * Returns the value of the '<em><b>Nested Part</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.PartUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nested Part</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_NestedPart()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<PartUsage> getNestedPart();

    /**
     * Returns the value of the '<em><b>Nested Port</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.PortUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nested Port</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_NestedPort()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<PortUsage> getNestedPort();

    /**
     * Returns the value of the '<em><b>Nested Reference</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.ReferenceUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nested Reference</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_NestedReference()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<ReferenceUsage> getNestedReference();

    /**
     * Returns the value of the '<em><b>Nested Rendering</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.RenderingUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nested Rendering</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_NestedRendering()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<RenderingUsage> getNestedRendering();

    /**
     * Returns the value of the '<em><b>Nested Requirement</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.RequirementUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nested Requirement</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_NestedRequirement()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<RequirementUsage> getNestedRequirement();

    /**
     * Returns the value of the '<em><b>Nested State</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.StateUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nested State</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_NestedState()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<StateUsage> getNestedState();

    /**
     * Returns the value of the '<em><b>Nested Transition</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.TransitionUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nested Transition</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_NestedTransition()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false" annotation="subsets"
     * @generated
     */
    EList<TransitionUsage> getNestedTransition();

    /**
     * Returns the value of the '<em><b>Nested Usage</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Usage}. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.Usage#getOwningUsage <em>Owning Usage</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Nested Usage</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_NestedUsage()
     * @see org.eclipse.syson.sysml.Usage#getOwningUsage
     * @model opposite="owningUsage" transient="true" changeable="false" volatile="true" derived="true"
     *        annotation="subsets"
     * @generated
     */
    EList<Usage> getNestedUsage();

    /**
     * Returns the value of the '<em><b>Nested Use Case</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.UseCaseUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nested Use Case</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_NestedUseCase()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<UseCaseUsage> getNestedUseCase();

    /**
     * Returns the value of the '<em><b>Nested Verification Case</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.VerificationCaseUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nested Verification Case</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_NestedVerificationCase()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<VerificationCaseUsage> getNestedVerificationCase();

    /**
     * Returns the value of the '<em><b>Nested View</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.ViewUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nested View</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_NestedView()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<ViewUsage> getNestedView();

    /**
     * Returns the value of the '<em><b>Nested Viewpoint</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.ViewpointUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Nested Viewpoint</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_NestedViewpoint()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<ViewpointUsage> getNestedViewpoint();

    /**
     * Returns the value of the '<em><b>Owning Definition</b></em>' reference. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.Definition#getOwnedUsage <em>Owned Usage</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Owning Definition</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_OwningDefinition()
     * @see org.eclipse.syson.sysml.Definition#getOwnedUsage
     * @model opposite="ownedUsage" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     *        annotation="subsets"
     * @generated
     */
    Definition getOwningDefinition();

    /**
     * Returns the value of the '<em><b>Owning Usage</b></em>' reference. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.Usage#getNestedUsage <em>Nested Usage</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Owning Usage</em>' reference.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_OwningUsage()
     * @see org.eclipse.syson.sysml.Usage#getNestedUsage
     * @model opposite="nestedUsage" transient="true" changeable="false" volatile="true" derived="true" ordered="false"
     *        annotation="subsets"
     * @generated
     */
    Usage getOwningUsage();

    /**
     * Returns the value of the '<em><b>Usage</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Usage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Usage</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_Usage()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<Usage> getUsage();

    /**
     * Returns the value of the '<em><b>Variant</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Usage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Variant</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_Variant()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false" annotation="subsets"
     * @generated
     */
    EList<Usage> getVariant();

    /**
     * Returns the value of the '<em><b>Variant Membership</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.VariantMembership}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Variant Membership</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getUsage_VariantMembership()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false" annotation="subsets"
     * @generated
     */
    EList<VariantMembership> getVariantMembership();

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model required="true" ordered="false"
     * @generated
     */
    Feature referencedFeatureTarget();

} // Usage
