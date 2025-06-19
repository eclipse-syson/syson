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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Definition</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.syson.sysml.Definition#isIsVariation <em>Is Variation</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getDirectedUsage <em>Directed Usage</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getOwnedAction <em>Owned Action</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getOwnedAllocation <em>Owned Allocation</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getOwnedAnalysisCase <em>Owned Analysis Case</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getOwnedAttribute <em>Owned Attribute</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getOwnedCalculation <em>Owned Calculation</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getOwnedCase <em>Owned Case</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getOwnedConcern <em>Owned Concern</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getOwnedConnection <em>Owned Connection</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getOwnedConstraint <em>Owned Constraint</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getOwnedEnumeration <em>Owned Enumeration</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getOwnedFlow <em>Owned Flow</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getOwnedInterface <em>Owned Interface</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getOwnedItem <em>Owned Item</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getOwnedMetadata <em>Owned Metadata</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getOwnedOccurrence <em>Owned Occurrence</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getOwnedPart <em>Owned Part</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getOwnedPort <em>Owned Port</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getOwnedReference <em>Owned Reference</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getOwnedRendering <em>Owned Rendering</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getOwnedRequirement <em>Owned Requirement</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getOwnedState <em>Owned State</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getOwnedTransition <em>Owned Transition</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getOwnedUsage <em>Owned Usage</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getOwnedUseCase <em>Owned Use Case</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getOwnedVerificationCase <em>Owned Verification Case</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getOwnedView <em>Owned View</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getOwnedViewpoint <em>Owned Viewpoint</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getUsage <em>Usage</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getVariant <em>Variant</em>}</li>
 * <li>{@link org.eclipse.syson.sysml.Definition#getVariantMembership <em>Variant Membership</em>}</li>
 * </ul>
 *
 * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition()
 * @model
 * @generated
 */
public interface Definition extends Classifier {
    /**
     * Returns the value of the '<em><b>Directed Usage</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Usage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Directed Usage</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_DirectedUsage()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<Usage> getDirectedUsage();

    /**
     * Returns the value of the '<em><b>Is Variation</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Is Variation</em>' attribute.
     * @see #setIsVariation(boolean)
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_IsVariation()
     * @model required="true" ordered="false"
     * @generated
     */
    boolean isIsVariation();

    /**
     * Sets the value of the '{@link org.eclipse.syson.sysml.Definition#isIsVariation <em>Is Variation</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Is Variation</em>' attribute.
     * @see #isIsVariation()
     * @generated
     */
    void setIsVariation(boolean value);

    /**
     * Returns the value of the '<em><b>Owned Action</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.ActionUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Action</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_OwnedAction()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<ActionUsage> getOwnedAction();

    /**
     * Returns the value of the '<em><b>Owned Allocation</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.AllocationUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Allocation</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_OwnedAllocation()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<AllocationUsage> getOwnedAllocation();

    /**
     * Returns the value of the '<em><b>Owned Analysis Case</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.AnalysisCaseUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Analysis Case</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_OwnedAnalysisCase()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<AnalysisCaseUsage> getOwnedAnalysisCase();

    /**
     * Returns the value of the '<em><b>Owned Attribute</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.AttributeUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Attribute</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_OwnedAttribute()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<AttributeUsage> getOwnedAttribute();

    /**
     * Returns the value of the '<em><b>Owned Calculation</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.CalculationUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Calculation</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_OwnedCalculation()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<CalculationUsage> getOwnedCalculation();

    /**
     * Returns the value of the '<em><b>Owned Case</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.CaseUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Case</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_OwnedCase()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<CaseUsage> getOwnedCase();

    /**
     * Returns the value of the '<em><b>Owned Concern</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.ConcernUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Concern</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_OwnedConcern()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false" annotation="subsets"
     * @generated
     */
    EList<ConcernUsage> getOwnedConcern();

    /**
     * Returns the value of the '<em><b>Owned Connection</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.ConnectorAsUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Connection</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_OwnedConnection()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<ConnectorAsUsage> getOwnedConnection();

    /**
     * Returns the value of the '<em><b>Owned Constraint</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.ConstraintUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Constraint</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_OwnedConstraint()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<ConstraintUsage> getOwnedConstraint();

    /**
     * Returns the value of the '<em><b>Owned Enumeration</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.EnumerationUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Enumeration</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_OwnedEnumeration()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<EnumerationUsage> getOwnedEnumeration();

    /**
     * Returns the value of the '<em><b>Owned Flow</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.FlowUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Flow</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_OwnedFlow()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false" annotation="subsets"
     * @generated
     */
    EList<FlowUsage> getOwnedFlow();

    /**
     * Returns the value of the '<em><b>Owned Interface</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.InterfaceUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Interface</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_OwnedInterface()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<InterfaceUsage> getOwnedInterface();

    /**
     * Returns the value of the '<em><b>Owned Item</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.ItemUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Item</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_OwnedItem()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<ItemUsage> getOwnedItem();

    /**
     * Returns the value of the '<em><b>Owned Metadata</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.MetadataUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Metadata</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_OwnedMetadata()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<MetadataUsage> getOwnedMetadata();

    /**
     * Returns the value of the '<em><b>Owned Occurrence</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.OccurrenceUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Occurrence</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_OwnedOccurrence()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<OccurrenceUsage> getOwnedOccurrence();

    /**
     * Returns the value of the '<em><b>Owned Part</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.PartUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Part</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_OwnedPart()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<PartUsage> getOwnedPart();

    /**
     * Returns the value of the '<em><b>Owned Port</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.PortUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Port</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_OwnedPort()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<PortUsage> getOwnedPort();

    /**
     * Returns the value of the '<em><b>Owned Reference</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.ReferenceUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Reference</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_OwnedReference()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<ReferenceUsage> getOwnedReference();

    /**
     * Returns the value of the '<em><b>Owned Rendering</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.RenderingUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Rendering</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_OwnedRendering()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<RenderingUsage> getOwnedRendering();

    /**
     * Returns the value of the '<em><b>Owned Requirement</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.RequirementUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Requirement</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_OwnedRequirement()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<RequirementUsage> getOwnedRequirement();

    /**
     * Returns the value of the '<em><b>Owned State</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.StateUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned State</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_OwnedState()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<StateUsage> getOwnedState();

    /**
     * Returns the value of the '<em><b>Owned Transition</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.TransitionUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Transition</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_OwnedTransition()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false" annotation="subsets"
     * @generated
     */
    EList<TransitionUsage> getOwnedTransition();

    /**
     * Returns the value of the '<em><b>Owned Usage</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Usage}. It is bidirectional and its opposite is
     * '{@link org.eclipse.syson.sysml.Usage#getOwningDefinition <em>Owning Definition</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Usage</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_OwnedUsage()
     * @see org.eclipse.syson.sysml.Usage#getOwningDefinition
     * @model opposite="owningDefinition" transient="true" changeable="false" volatile="true" derived="true"
     *        annotation="subsets"
     * @generated
     */
    EList<Usage> getOwnedUsage();

    /**
     * Returns the value of the '<em><b>Owned Use Case</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.UseCaseUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Use Case</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_OwnedUseCase()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<UseCaseUsage> getOwnedUseCase();

    /**
     * Returns the value of the '<em><b>Owned Verification Case</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.VerificationCaseUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Verification Case</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_OwnedVerificationCase()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<VerificationCaseUsage> getOwnedVerificationCase();

    /**
     * Returns the value of the '<em><b>Owned View</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.ViewUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned View</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_OwnedView()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<ViewUsage> getOwnedView();

    /**
     * Returns the value of the '<em><b>Owned Viewpoint</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.ViewpointUsage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Owned Viewpoint</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_OwnedViewpoint()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<ViewpointUsage> getOwnedViewpoint();

    /**
     * Returns the value of the '<em><b>Usage</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Usage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Usage</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_Usage()
     * @model transient="true" changeable="false" volatile="true" derived="true" annotation="subsets"
     * @generated
     */
    EList<Usage> getUsage();

    /**
     * Returns the value of the '<em><b>Variant</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.Usage}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Variant</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_Variant()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false" annotation="subsets"
     * @generated
     */
    EList<Usage> getVariant();

    /**
     * Returns the value of the '<em><b>Variant Membership</b></em>' reference list. The list contents are of type
     * {@link org.eclipse.syson.sysml.VariantMembership}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Variant Membership</em>' reference list.
     * @see org.eclipse.syson.sysml.SysmlPackage#getDefinition_VariantMembership()
     * @model transient="true" changeable="false" volatile="true" derived="true" ordered="false" annotation="subsets"
     * @generated
     */
    EList<VariantMembership> getVariantMembership();

} // Definition
