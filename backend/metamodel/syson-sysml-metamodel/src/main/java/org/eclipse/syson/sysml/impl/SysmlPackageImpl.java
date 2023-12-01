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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.ActorMembership;
import org.eclipse.syson.sysml.AllocationDefinition;
import org.eclipse.syson.sysml.AllocationUsage;
import org.eclipse.syson.sysml.AnalysisCaseDefinition;
import org.eclipse.syson.sysml.AnalysisCaseUsage;
import org.eclipse.syson.sysml.AnnotatingElement;
import org.eclipse.syson.sysml.Annotation;
import org.eclipse.syson.sysml.AssertConstraintUsage;
import org.eclipse.syson.sysml.AssignmentActionUsage;
import org.eclipse.syson.sysml.Association;
import org.eclipse.syson.sysml.AssociationStructure;
import org.eclipse.syson.sysml.AttributeDefinition;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Behavior;
import org.eclipse.syson.sysml.BindingConnector;
import org.eclipse.syson.sysml.BindingConnectorAsUsage;
import org.eclipse.syson.sysml.BooleanExpression;
import org.eclipse.syson.sysml.CalculationDefinition;
import org.eclipse.syson.sysml.CalculationUsage;
import org.eclipse.syson.sysml.CaseDefinition;
import org.eclipse.syson.sysml.CaseUsage;
import org.eclipse.syson.sysml.Classifier;
import org.eclipse.syson.sysml.CollectExpression;
import org.eclipse.syson.sysml.Comment;
import org.eclipse.syson.sysml.ConcernDefinition;
import org.eclipse.syson.sysml.ConcernUsage;
import org.eclipse.syson.sysml.ConjugatedPortDefinition;
import org.eclipse.syson.sysml.ConjugatedPortTyping;
import org.eclipse.syson.sysml.Conjugation;
import org.eclipse.syson.sysml.ConnectionDefinition;
import org.eclipse.syson.sysml.ConnectionUsage;
import org.eclipse.syson.sysml.Connector;
import org.eclipse.syson.sysml.ConnectorAsUsage;
import org.eclipse.syson.sysml.ConstraintDefinition;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.ControlNode;
import org.eclipse.syson.sysml.DataType;
import org.eclipse.syson.sysml.DecisionNode;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Dependency;
import org.eclipse.syson.sysml.Differencing;
import org.eclipse.syson.sysml.Disjoining;
import org.eclipse.syson.sysml.Documentation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.ElementFilterMembership;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.EnumerationDefinition;
import org.eclipse.syson.sysml.EnumerationUsage;
import org.eclipse.syson.sysml.EventOccurrenceUsage;
import org.eclipse.syson.sysml.ExhibitStateUsage;
import org.eclipse.syson.sysml.Expose;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureChainExpression;
import org.eclipse.syson.sysml.FeatureChaining;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.FeatureInverting;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureReferenceExpression;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.Featuring;
import org.eclipse.syson.sysml.FlowConnectionDefinition;
import org.eclipse.syson.sysml.FlowConnectionUsage;
import org.eclipse.syson.sysml.ForLoopActionUsage;
import org.eclipse.syson.sysml.ForkNode;
import org.eclipse.syson.sysml.FramedConcernMembership;
import org.eclipse.syson.sysml.Function;
import org.eclipse.syson.sysml.IfActionUsage;
import org.eclipse.syson.sysml.Import;
import org.eclipse.syson.sysml.IncludeUseCaseUsage;
import org.eclipse.syson.sysml.Interaction;
import org.eclipse.syson.sysml.InterfaceDefinition;
import org.eclipse.syson.sysml.InterfaceUsage;
import org.eclipse.syson.sysml.Intersecting;
import org.eclipse.syson.sysml.Invariant;
import org.eclipse.syson.sysml.InvocationExpression;
import org.eclipse.syson.sysml.ItemDefinition;
import org.eclipse.syson.sysml.ItemFeature;
import org.eclipse.syson.sysml.ItemFlow;
import org.eclipse.syson.sysml.ItemFlowEnd;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.JoinNode;
import org.eclipse.syson.sysml.LibraryPackage;
import org.eclipse.syson.sysml.LifeClass;
import org.eclipse.syson.sysml.LiteralBoolean;
import org.eclipse.syson.sysml.LiteralExpression;
import org.eclipse.syson.sysml.LiteralInfinity;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.LiteralRational;
import org.eclipse.syson.sysml.LiteralString;
import org.eclipse.syson.sysml.LoopActionUsage;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.MembershipExpose;
import org.eclipse.syson.sysml.MembershipImport;
import org.eclipse.syson.sysml.MergeNode;
import org.eclipse.syson.sysml.Metaclass;
import org.eclipse.syson.sysml.MetadataAccessExpression;
import org.eclipse.syson.sysml.MetadataDefinition;
import org.eclipse.syson.sysml.MetadataFeature;
import org.eclipse.syson.sysml.MetadataUsage;
import org.eclipse.syson.sysml.Multiplicity;
import org.eclipse.syson.sysml.MultiplicityRange;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.NamespaceExpose;
import org.eclipse.syson.sysml.NamespaceImport;
import org.eclipse.syson.sysml.NullExpression;
import org.eclipse.syson.sysml.ObjectiveMembership;
import org.eclipse.syson.sysml.OccurrenceDefinition;
import org.eclipse.syson.sysml.OccurrenceUsage;
import org.eclipse.syson.sysml.OperatorExpression;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PerformActionUsage;
import org.eclipse.syson.sysml.PortConjugation;
import org.eclipse.syson.sysml.PortDefinition;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.PortionKind;
import org.eclipse.syson.sysml.Predicate;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.RenderingDefinition;
import org.eclipse.syson.sysml.RenderingUsage;
import org.eclipse.syson.sysml.RequirementConstraintKind;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.RequirementDefinition;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.RequirementVerificationMembership;
import org.eclipse.syson.sysml.ResultExpressionMembership;
import org.eclipse.syson.sysml.ReturnParameterMembership;
import org.eclipse.syson.sysml.SatisfyRequirementUsage;
import org.eclipse.syson.sysml.SelectExpression;
import org.eclipse.syson.sysml.SendActionUsage;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.StakeholderMembership;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.StateSubactionKind;
import org.eclipse.syson.sysml.StateSubactionMembership;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.Step;
import org.eclipse.syson.sysml.Structure;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.SubjectMembership;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.Succession;
import org.eclipse.syson.sysml.SuccessionAsUsage;
import org.eclipse.syson.sysml.SuccessionFlowConnectionUsage;
import org.eclipse.syson.sysml.SuccessionItemFlow;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TextualRepresentation;
import org.eclipse.syson.sysml.TransitionFeatureKind;
import org.eclipse.syson.sysml.TransitionFeatureMembership;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.TriggerInvocationExpression;
import org.eclipse.syson.sysml.TriggerKind;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.TypeFeaturing;
import org.eclipse.syson.sysml.Unioning;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.UseCaseDefinition;
import org.eclipse.syson.sysml.UseCaseUsage;
import org.eclipse.syson.sysml.VariantMembership;
import org.eclipse.syson.sysml.VerificationCaseDefinition;
import org.eclipse.syson.sysml.VerificationCaseUsage;
import org.eclipse.syson.sysml.ViewDefinition;
import org.eclipse.syson.sysml.ViewRenderingMembership;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.ViewpointDefinition;
import org.eclipse.syson.sysml.ViewpointUsage;
import org.eclipse.syson.sysml.VisibilityKind;
import org.eclipse.syson.sysml.WhileLoopActionUsage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SysmlPackageImpl extends EPackageImpl implements SysmlPackage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass acceptActionUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass actionDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass actionUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass actorMembershipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass allocationDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass allocationUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass analysisCaseDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass analysisCaseUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass annotatingElementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass annotationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass assertConstraintUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass assignmentActionUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass associationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass associationStructureEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass attributeDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass attributeUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass behaviorEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass bindingConnectorEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass bindingConnectorAsUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass booleanExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass calculationDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass calculationUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass caseDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass caseUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass classEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass classifierEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass collectExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass commentEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass concernDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass concernUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass conjugatedPortDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass conjugatedPortTypingEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass conjugationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass connectionDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass connectionUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass connectorEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass connectorAsUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass constraintDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass constraintUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass controlNodeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dataTypeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass decisionNodeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass definitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dependencyEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass differencingEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass disjoiningEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass documentationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass elementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass elementFilterMembershipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass endFeatureMembershipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass enumerationDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass enumerationUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass eventOccurrenceUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass exhibitStateUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass exposeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass expressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass featureEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass featureChainExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass featureChainingEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass featureInvertingEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass featureMembershipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass featureReferenceExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass featureTypingEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass featureValueEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass featuringEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass flowConnectionDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass flowConnectionUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass forkNodeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass forLoopActionUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass framedConcernMembershipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass functionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass ifActionUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass importEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass includeUseCaseUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass interactionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass interfaceDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass interfaceUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass intersectingEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass invariantEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass invocationExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass itemDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass itemFeatureEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass itemFlowEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass itemFlowEndEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass itemUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass joinNodeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass libraryPackageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass lifeClassEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass literalBooleanEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass literalExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass literalInfinityEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass literalIntegerEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass literalRationalEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass literalStringEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass loopActionUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass membershipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass membershipExposeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass membershipImportEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass mergeNodeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass metaclassEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass metadataAccessExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass metadataDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass metadataFeatureEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass metadataUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass multiplicityEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass multiplicityRangeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass namespaceEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass namespaceExposeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass namespaceImportEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass nullExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass objectiveMembershipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass occurrenceDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass occurrenceUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass operatorExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass owningMembershipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass packageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass parameterMembershipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass partDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass partUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass performActionUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass portConjugationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass portDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass portUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass predicateEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass redefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass referenceSubsettingEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass referenceUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass relationshipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass renderingDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass renderingUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass requirementConstraintMembershipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass requirementDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass requirementUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass requirementVerificationMembershipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass resultExpressionMembershipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass returnParameterMembershipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass satisfyRequirementUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass selectExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass sendActionUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass specializationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass stakeholderMembershipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass stateDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass stateSubactionMembershipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass stateUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass stepEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass structureEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass subclassificationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass subjectMembershipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass subsettingEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass successionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass successionAsUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass successionFlowConnectionUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass successionItemFlowEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass textualRepresentationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass transitionFeatureMembershipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass transitionUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass triggerInvocationExpressionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass typeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass typeFeaturingEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass unioningEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass usageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass useCaseDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass useCaseUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass variantMembershipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass verificationCaseDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass verificationCaseUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass viewDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass viewpointDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass viewpointUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass viewRenderingMembershipEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass viewUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass whileLoopActionUsageEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum featureDirectionKindEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum portionKindEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum requirementConstraintKindEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum stateSubactionKindEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum transitionFeatureKindEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum triggerKindEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum visibilityKindEEnum = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
     * package URI value.
     * <p>Note: the correct way to create the package is via the static
     * factory method {@link #init init()}, which also performs
     * initialization of the package, or returns the registered package,
     * if one already exists.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see org.eclipse.syson.sysml.SysmlPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private SysmlPackageImpl() {
        super(eNS_URI, SysmlFactory.eINSTANCE);
    }
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
     *
     * <p>This method is used to initialize {@link SysmlPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static SysmlPackage init() {
        if (isInited) return (SysmlPackage)EPackage.Registry.INSTANCE.getEPackage(SysmlPackage.eNS_URI);

        // Obtain or create and register package
        Object registeredSysmlPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
        SysmlPackageImpl theSysmlPackage = registeredSysmlPackage instanceof SysmlPackageImpl ? (SysmlPackageImpl)registeredSysmlPackage : new SysmlPackageImpl();

        isInited = true;

        // Create package meta-data objects
        theSysmlPackage.createPackageContents();

        // Initialize created meta-data
        theSysmlPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theSysmlPackage.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(SysmlPackage.eNS_URI, theSysmlPackage);
        return theSysmlPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAcceptActionUsage() {
        return acceptActionUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAcceptActionUsage_PayloadArgument() {
        return (EReference)acceptActionUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAcceptActionUsage_PayloadParameter() {
        return (EReference)acceptActionUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAcceptActionUsage_ReceiverArgument() {
        return (EReference)acceptActionUsageEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getAcceptActionUsage__IsTriggerAction() {
        return acceptActionUsageEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getActionDefinition() {
        return actionDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getActionDefinition_Action() {
        return (EReference)actionDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getActionUsage() {
        return actionUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getActionUsage_ActionDefinition() {
        return (EReference)actionUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getActionUsage__Argument__int() {
        return actionUsageEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getActionUsage__InputParameter__int() {
        return actionUsageEClass.getEOperations().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getActionUsage__InputParameters() {
        return actionUsageEClass.getEOperations().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getActionUsage__IsSubactionUsage() {
        return actionUsageEClass.getEOperations().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getActorMembership() {
        return actorMembershipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getActorMembership_OwnedActorParameter() {
        return (EReference)actorMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAllocationDefinition() {
        return allocationDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAllocationDefinition_Allocation() {
        return (EReference)allocationDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAllocationUsage() {
        return allocationUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAllocationUsage_AllocationDefinition() {
        return (EReference)allocationUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAnalysisCaseDefinition() {
        return analysisCaseDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAnalysisCaseDefinition_AnalysisAction() {
        return (EReference)analysisCaseDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAnalysisCaseDefinition_ResultExpression() {
        return (EReference)analysisCaseDefinitionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAnalysisCaseUsage() {
        return analysisCaseUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAnalysisCaseUsage_AnalysisAction() {
        return (EReference)analysisCaseUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAnalysisCaseUsage_AnalysisCaseDefinition() {
        return (EReference)analysisCaseUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAnalysisCaseUsage_ResultExpression() {
        return (EReference)analysisCaseUsageEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAnnotatingElement() {
        return annotatingElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAnnotatingElement_AnnotatedElement() {
        return (EReference)annotatingElementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAnnotatingElement_Annotation() {
        return (EReference)annotatingElementEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAnnotation() {
        return annotationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAnnotation_AnnotatedElement() {
        return (EReference)annotationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAnnotation_AnnotatingElement() {
        return (EReference)annotationEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAnnotation_OwningAnnotatedElement() {
        return (EReference)annotationEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAssertConstraintUsage() {
        return assertConstraintUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAssertConstraintUsage_AssertedConstraint() {
        return (EReference)assertConstraintUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAssignmentActionUsage() {
        return assignmentActionUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAssignmentActionUsage_Referent() {
        return (EReference)assignmentActionUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAssignmentActionUsage_TargetArgument() {
        return (EReference)assignmentActionUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAssignmentActionUsage_ValueExpression() {
        return (EReference)assignmentActionUsageEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAssociation() {
        return associationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAssociation_AssociationEnd() {
        return (EReference)associationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAssociation_RelatedType() {
        return (EReference)associationEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAssociation_SourceType() {
        return (EReference)associationEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAssociation_TargetType() {
        return (EReference)associationEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAssociationStructure() {
        return associationStructureEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAttributeDefinition() {
        return attributeDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getAttributeUsage() {
        return attributeUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getAttributeUsage_AttributeDefinition() {
        return (EReference)attributeUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBehavior() {
        return behaviorEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getBehavior_Parameter() {
        return (EReference)behaviorEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getBehavior_Step() {
        return (EReference)behaviorEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBindingConnector() {
        return bindingConnectorEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBindingConnectorAsUsage() {
        return bindingConnectorAsUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getBooleanExpression() {
        return booleanExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getBooleanExpression_Predicate() {
        return (EReference)booleanExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getCalculationDefinition() {
        return calculationDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getCalculationDefinition_Calculation() {
        return (EReference)calculationDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getCalculationUsage() {
        return calculationUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getCalculationUsage_CalculationDefinition() {
        return (EReference)calculationUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getCaseDefinition() {
        return caseDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getCaseDefinition_ActorParameter() {
        return (EReference)caseDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getCaseDefinition_ObjectiveRequirement() {
        return (EReference)caseDefinitionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getCaseDefinition_SubjectParameter() {
        return (EReference)caseDefinitionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getCaseUsage() {
        return caseUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getCaseUsage_ActorParameter() {
        return (EReference)caseUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getCaseUsage_CaseDefinition() {
        return (EReference)caseUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getCaseUsage_ObjectiveRequirement() {
        return (EReference)caseUsageEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getCaseUsage_SubjectParameter() {
        return (EReference)caseUsageEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getClass_() {
        return classEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getClassifier() {
        return classifierEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getClassifier_OwnedSubclassification() {
        return (EReference)classifierEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getCollectExpression() {
        return collectExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getComment() {
        return commentEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getComment_Body() {
        return (EAttribute)commentEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getComment_Locale() {
        return (EAttribute)commentEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getConcernDefinition() {
        return concernDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getConcernUsage() {
        return concernUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getConcernUsage_ConcernDefinition() {
        return (EReference)concernUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getConjugatedPortDefinition() {
        return conjugatedPortDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getConjugatedPortDefinition_OriginalPortDefinition() {
        return (EReference)conjugatedPortDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getConjugatedPortDefinition_OwnedPortConjugator() {
        return (EReference)conjugatedPortDefinitionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getConjugatedPortTyping() {
        return conjugatedPortTypingEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getConjugatedPortTyping_ConjugatedPortDefinition() {
        return (EReference)conjugatedPortTypingEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getConjugatedPortTyping_PortDefinition() {
        return (EReference)conjugatedPortTypingEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getConjugation() {
        return conjugationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getConjugation_ConjugatedType() {
        return (EReference)conjugationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getConjugation_OriginalType() {
        return (EReference)conjugationEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getConjugation_OwningType() {
        return (EReference)conjugationEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getConnectionDefinition() {
        return connectionDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getConnectionDefinition_ConnectionEnd() {
        return (EReference)connectionDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getConnectionUsage() {
        return connectionUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getConnectionUsage_ConnectionDefinition() {
        return (EReference)connectionUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getConnector() {
        return connectorEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getConnector_Association() {
        return (EReference)connectorEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getConnector_ConnectorEnd() {
        return (EReference)connectorEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getConnector_IsDirected() {
        return (EAttribute)connectorEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getConnector_RelatedFeature() {
        return (EReference)connectorEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getConnector_SourceFeature() {
        return (EReference)connectorEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getConnector_TargetFeature() {
        return (EReference)connectorEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getConnectorAsUsage() {
        return connectorAsUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getConstraintDefinition() {
        return constraintDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getConstraintUsage() {
        return constraintUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getConstraintUsage_ConstraintDefinition() {
        return (EReference)constraintUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getControlNode() {
        return controlNodeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getControlNode__MultiplicityHasBounds__Multiplicity_int_int() {
        return controlNodeEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDataType() {
        return dataTypeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDecisionNode() {
        return decisionNodeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDefinition() {
        return definitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_DirectedUsage() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getDefinition_IsVariation() {
        return (EAttribute)definitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedAction() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedAllocation() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedAnalysisCase() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedAttribute() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedCalculation() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedCase() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedConcern() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedConnection() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedConstraint() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedEnumeration() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedFlow() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedInterface() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedItem() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedMetadata() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedOccurrence() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedPart() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedPort() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedReference() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedRendering() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedRequirement() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(21);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedState() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(22);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedTransition() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(23);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedUsage() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(24);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedUseCase() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(25);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedVerificationCase() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(26);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedView() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(27);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_OwnedViewpoint() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(28);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_Usage() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(29);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_Variant() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(30);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDefinition_VariantMembership() {
        return (EReference)definitionEClass.getEStructuralFeatures().get(31);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDependency() {
        return dependencyEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDependency_Client() {
        return (EReference)dependencyEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDependency_Supplier() {
        return (EReference)dependencyEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDifferencing() {
        return differencingEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDifferencing_DifferencingType() {
        return (EReference)differencingEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDifferencing_TypeDifferenced() {
        return (EReference)differencingEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDisjoining() {
        return disjoiningEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDisjoining_DisjoiningType() {
        return (EReference)disjoiningEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDisjoining_OwningType() {
        return (EReference)disjoiningEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDisjoining_TypeDisjoined() {
        return (EReference)disjoiningEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getDocumentation() {
        return documentationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getDocumentation_DocumentedElement() {
        return (EReference)documentationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getElement() {
        return elementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getElement_AliasIds() {
        return (EAttribute)elementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getElement_DeclaredName() {
        return (EAttribute)elementEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getElement_DeclaredShortName() {
        return (EAttribute)elementEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getElement_Documentation() {
        return (EReference)elementEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getElement_ElementId() {
        return (EAttribute)elementEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getElement_IsImpliedIncluded() {
        return (EAttribute)elementEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getElement_IsLibraryElement() {
        return (EAttribute)elementEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getElement_Name() {
        return (EAttribute)elementEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getElement_OwnedAnnotation() {
        return (EReference)elementEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getElement_OwnedElement() {
        return (EReference)elementEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getElement_OwnedRelationship() {
        return (EReference)elementEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getElement_Owner() {
        return (EReference)elementEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getElement_OwningMembership() {
        return (EReference)elementEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getElement_OwningNamespace() {
        return (EReference)elementEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getElement_OwningRelationship() {
        return (EReference)elementEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getElement_QualifiedName() {
        return (EAttribute)elementEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getElement_ShortName() {
        return (EAttribute)elementEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getElement_TextualRepresentation() {
        return (EReference)elementEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getElement__EffectiveName() {
        return elementEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getElement__EffectiveShortName() {
        return elementEClass.getEOperations().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getElement__EscapedName() {
        return elementEClass.getEOperations().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getElement__LibraryNamespace() {
        return elementEClass.getEOperations().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getElementFilterMembership() {
        return elementFilterMembershipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getElementFilterMembership_Condition() {
        return (EReference)elementFilterMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getEndFeatureMembership() {
        return endFeatureMembershipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getEnumerationDefinition() {
        return enumerationDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getEnumerationDefinition_EnumeratedValue() {
        return (EReference)enumerationDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getEnumerationUsage() {
        return enumerationUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getEnumerationUsage_EnumerationDefinition() {
        return (EReference)enumerationUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getEventOccurrenceUsage() {
        return eventOccurrenceUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getEventOccurrenceUsage_EventOccurrence() {
        return (EReference)eventOccurrenceUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getExhibitStateUsage() {
        return exhibitStateUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getExhibitStateUsage_ExhibitedState() {
        return (EReference)exhibitStateUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getExpose() {
        return exposeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getExpression() {
        return expressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getExpression_Function() {
        return (EReference)expressionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getExpression_IsModelLevelEvaluable() {
        return (EAttribute)expressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getExpression_Result() {
        return (EReference)expressionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getExpression__CheckCondition__Element() {
        return expressionEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getExpression__Evaluate__Element() {
        return expressionEClass.getEOperations().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getExpression__ModelLevelEvaluable__EList() {
        return expressionEClass.getEOperations().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFeature() {
        return featureEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeature_ChainingFeature() {
        return (EReference)featureEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getFeature_Direction() {
        return (EAttribute)featureEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeature_EndOwningType() {
        return (EReference)featureEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeature_FeaturingType() {
        return (EReference)featureEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getFeature_IsComposite() {
        return (EAttribute)featureEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getFeature_IsDerived() {
        return (EAttribute)featureEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getFeature_IsEnd() {
        return (EAttribute)featureEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getFeature_IsNonunique() {
        return (EAttribute)featureEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getFeature_IsOrdered() {
        return (EAttribute)featureEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getFeature_IsPortion() {
        return (EAttribute)featureEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getFeature_IsReadOnly() {
        return (EAttribute)featureEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getFeature_IsUnique() {
        return (EAttribute)featureEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeature_OwnedFeatureChaining() {
        return (EReference)featureEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeature_OwnedFeatureInverting() {
        return (EReference)featureEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeature_OwnedRedefinition() {
        return (EReference)featureEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeature_OwnedReferenceSubsetting() {
        return (EReference)featureEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeature_OwnedSubsetting() {
        return (EReference)featureEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeature_OwnedTypeFeaturing() {
        return (EReference)featureEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeature_OwnedTyping() {
        return (EReference)featureEClass.getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeature_OwningFeatureMembership() {
        return (EReference)featureEClass.getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeature_OwningType() {
        return (EReference)featureEClass.getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeature_Type() {
        return (EReference)featureEClass.getEStructuralFeatures().get(21);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getFeature__DirectionFor__Type() {
        return featureEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getFeature__IsFeaturedWithin__Type() {
        return featureEClass.getEOperations().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getFeature__NamingFeature() {
        return featureEClass.getEOperations().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getFeature__Redefines__Feature() {
        return featureEClass.getEOperations().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getFeature__RedefinesFromLibrary__String() {
        return featureEClass.getEOperations().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getFeature__SubsetsChain__Feature_Feature() {
        return featureEClass.getEOperations().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFeatureChainExpression() {
        return featureChainExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeatureChainExpression_TargetFeature() {
        return (EReference)featureChainExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getFeatureChainExpression__SourceTargetFeature() {
        return featureChainExpressionEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFeatureChaining() {
        return featureChainingEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeatureChaining_ChainingFeature() {
        return (EReference)featureChainingEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeatureChaining_FeatureChained() {
        return (EReference)featureChainingEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFeatureInverting() {
        return featureInvertingEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeatureInverting_FeatureInverted() {
        return (EReference)featureInvertingEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeatureInverting_InvertingFeature() {
        return (EReference)featureInvertingEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeatureInverting_OwningFeature() {
        return (EReference)featureInvertingEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFeatureMembership() {
        return featureMembershipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeatureMembership_OwnedMemberFeature() {
        return (EReference)featureMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeatureMembership_OwningType() {
        return (EReference)featureMembershipEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFeatureReferenceExpression() {
        return featureReferenceExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeatureReferenceExpression_Referent() {
        return (EReference)featureReferenceExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFeatureTyping() {
        return featureTypingEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeatureTyping_OwningFeature() {
        return (EReference)featureTypingEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeatureTyping_Type() {
        return (EReference)featureTypingEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeatureTyping_TypedFeature() {
        return (EReference)featureTypingEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFeatureValue() {
        return featureValueEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeatureValue_FeatureWithValue() {
        return (EReference)featureValueEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getFeatureValue_IsDefault() {
        return (EAttribute)featureValueEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getFeatureValue_IsInitial() {
        return (EAttribute)featureValueEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeatureValue_Value() {
        return (EReference)featureValueEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFeaturing() {
        return featuringEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeaturing_Feature() {
        return (EReference)featuringEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFeaturing_Type() {
        return (EReference)featuringEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFlowConnectionDefinition() {
        return flowConnectionDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFlowConnectionUsage() {
        return flowConnectionUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFlowConnectionUsage_FlowConnectionDefinition() {
        return (EReference)flowConnectionUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getForkNode() {
        return forkNodeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getForLoopActionUsage() {
        return forLoopActionUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getForLoopActionUsage_LoopVariable() {
        return (EReference)forLoopActionUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getForLoopActionUsage_SeqArgument() {
        return (EReference)forLoopActionUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFramedConcernMembership() {
        return framedConcernMembershipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFramedConcernMembership_OwnedConcern() {
        return (EReference)framedConcernMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFramedConcernMembership_ReferencedConcern() {
        return (EReference)framedConcernMembershipEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getFunction() {
        return functionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFunction_Expression() {
        return (EReference)functionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getFunction_IsModelLevelEvaluable() {
        return (EAttribute)functionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getFunction_Result() {
        return (EReference)functionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getIfActionUsage() {
        return ifActionUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getIfActionUsage_ElseAction() {
        return (EReference)ifActionUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getIfActionUsage_IfArgument() {
        return (EReference)ifActionUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getIfActionUsage_ThenAction() {
        return (EReference)ifActionUsageEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getImport() {
        return importEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getImport_ImportedElement() {
        return (EReference)importEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getImport_ImportOwningNamespace() {
        return (EReference)importEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getImport_IsImportAll() {
        return (EAttribute)importEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getImport_IsRecursive() {
        return (EAttribute)importEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getImport_Visibility() {
        return (EAttribute)importEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getImport__ImportedMemberships__EList() {
        return importEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getIncludeUseCaseUsage() {
        return includeUseCaseUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getIncludeUseCaseUsage_UseCaseIncluded() {
        return (EReference)includeUseCaseUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getInteraction() {
        return interactionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getInterfaceDefinition() {
        return interfaceDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getInterfaceDefinition_InterfaceEnd() {
        return (EReference)interfaceDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getInterfaceUsage() {
        return interfaceUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getInterfaceUsage_InterfaceDefinition() {
        return (EReference)interfaceUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getIntersecting() {
        return intersectingEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getIntersecting_IntersectingType() {
        return (EReference)intersectingEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getIntersecting_TypeIntersected() {
        return (EReference)intersectingEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getInvariant() {
        return invariantEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getInvariant_IsNegated() {
        return (EAttribute)invariantEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getInvocationExpression() {
        return invocationExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getInvocationExpression_Argument() {
        return (EReference)invocationExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getItemDefinition() {
        return itemDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getItemFeature() {
        return itemFeatureEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getItemFlow() {
        return itemFlowEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getItemFlow_Interaction() {
        return (EReference)itemFlowEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getItemFlow_ItemFeature() {
        return (EReference)itemFlowEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getItemFlow_ItemFlowEnd() {
        return (EReference)itemFlowEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getItemFlow_ItemType() {
        return (EReference)itemFlowEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getItemFlow_SourceOutputFeature() {
        return (EReference)itemFlowEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getItemFlow_TargetInputFeature() {
        return (EReference)itemFlowEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getItemFlowEnd() {
        return itemFlowEndEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getItemUsage() {
        return itemUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getItemUsage_ItemDefinition() {
        return (EReference)itemUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getJoinNode() {
        return joinNodeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getLibraryPackage() {
        return libraryPackageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getLibraryPackage_IsStandard() {
        return (EAttribute)libraryPackageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getLifeClass() {
        return lifeClassEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getLiteralBoolean() {
        return literalBooleanEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getLiteralBoolean_Value() {
        return (EAttribute)literalBooleanEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getLiteralExpression() {
        return literalExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getLiteralInfinity() {
        return literalInfinityEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getLiteralInteger() {
        return literalIntegerEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getLiteralInteger_Value() {
        return (EAttribute)literalIntegerEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getLiteralRational() {
        return literalRationalEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getLiteralRational_Value() {
        return (EAttribute)literalRationalEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getLiteralString() {
        return literalStringEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getLiteralString_Value() {
        return (EAttribute)literalStringEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getLoopActionUsage() {
        return loopActionUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getLoopActionUsage_BodyAction() {
        return (EReference)loopActionUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getMembership() {
        return membershipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getMembership_MemberElement() {
        return (EReference)membershipEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getMembership_MemberElementId() {
        return (EAttribute)membershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getMembership_MemberName() {
        return (EAttribute)membershipEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getMembership_MembershipOwningNamespace() {
        return (EReference)membershipEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getMembership_MemberShortName() {
        return (EAttribute)membershipEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getMembership_Visibility() {
        return (EAttribute)membershipEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getMembership__IsDistinguishableFrom__Membership() {
        return membershipEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getMembershipExpose() {
        return membershipExposeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getMembershipImport() {
        return membershipImportEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getMembershipImport_ImportedMembership() {
        return (EReference)membershipImportEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getMergeNode() {
        return mergeNodeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getMetaclass() {
        return metaclassEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getMetadataAccessExpression() {
        return metadataAccessExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getMetadataAccessExpression_ReferencedElement() {
        return (EReference)metadataAccessExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getMetadataAccessExpression__MetaclassFeature() {
        return metadataAccessExpressionEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getMetadataDefinition() {
        return metadataDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getMetadataFeature() {
        return metadataFeatureEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getMetadataFeature_Metaclass() {
        return (EReference)metadataFeatureEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getMetadataFeature__EvaluateFeature__Feature() {
        return metadataFeatureEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getMetadataFeature__IsSemantic() {
        return metadataFeatureEClass.getEOperations().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getMetadataFeature__IsSyntactic() {
        return metadataFeatureEClass.getEOperations().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getMetadataFeature__SyntaxElement() {
        return metadataFeatureEClass.getEOperations().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getMetadataUsage() {
        return metadataUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getMetadataUsage_MetadataDefinition() {
        return (EReference)metadataUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getMultiplicity() {
        return multiplicityEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getMultiplicityRange() {
        return multiplicityRangeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getMultiplicityRange_Bound() {
        return (EReference)multiplicityRangeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getMultiplicityRange_LowerBound() {
        return (EReference)multiplicityRangeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getMultiplicityRange_UpperBound() {
        return (EReference)multiplicityRangeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getMultiplicityRange__HasBounds__int_int() {
        return multiplicityRangeEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getMultiplicityRange__ValueOf__Expression() {
        return multiplicityRangeEClass.getEOperations().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getNamespace() {
        return namespaceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getNamespace_ImportedMembership() {
        return (EReference)namespaceEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getNamespace_Member() {
        return (EReference)namespaceEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getNamespace_Membership() {
        return (EReference)namespaceEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getNamespace_OwnedImport() {
        return (EReference)namespaceEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getNamespace_OwnedMember() {
        return (EReference)namespaceEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getNamespace_OwnedMembership() {
        return (EReference)namespaceEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getNamespace__ImportedMemberships__EList() {
        return namespaceEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getNamespace__NamesOf__Element() {
        return namespaceEClass.getEOperations().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getNamespace__QualificationOf__String() {
        return namespaceEClass.getEOperations().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getNamespace__Resolve__String() {
        return namespaceEClass.getEOperations().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getNamespace__ResolveGlobal__String() {
        return namespaceEClass.getEOperations().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getNamespace__ResolveLocal__String() {
        return namespaceEClass.getEOperations().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getNamespace__ResolveVisible__String() {
        return namespaceEClass.getEOperations().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getNamespace__UnqualifiedNameOf__String() {
        return namespaceEClass.getEOperations().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getNamespace__VisibilityOf__Membership() {
        return namespaceEClass.getEOperations().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getNamespace__VisibleMemberships__EList_boolean_boolean() {
        return namespaceEClass.getEOperations().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getNamespaceExpose() {
        return namespaceExposeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getNamespaceImport() {
        return namespaceImportEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getNamespaceImport_ImportedNamespace() {
        return (EReference)namespaceImportEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getNullExpression() {
        return nullExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getObjectiveMembership() {
        return objectiveMembershipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getObjectiveMembership_OwnedObjectiveRequirement() {
        return (EReference)objectiveMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getOccurrenceDefinition() {
        return occurrenceDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getOccurrenceDefinition_IsIndividual() {
        return (EAttribute)occurrenceDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getOccurrenceDefinition_LifeClass() {
        return (EReference)occurrenceDefinitionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getOccurrenceUsage() {
        return occurrenceUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getOccurrenceUsage_IndividualDefinition() {
        return (EReference)occurrenceUsageEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getOccurrenceUsage_IsIndividual() {
        return (EAttribute)occurrenceUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getOccurrenceUsage_OccurrenceDefinition() {
        return (EReference)occurrenceUsageEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getOccurrenceUsage_PortionKind() {
        return (EAttribute)occurrenceUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getOperatorExpression() {
        return operatorExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getOperatorExpression_Operand() {
        return (EReference)operatorExpressionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getOperatorExpression_Operator() {
        return (EAttribute)operatorExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getOwningMembership() {
        return owningMembershipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getOwningMembership_OwnedMemberElement() {
        return (EReference)owningMembershipEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getOwningMembership_OwnedMemberElementId() {
        return (EAttribute)owningMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getOwningMembership_OwnedMemberName() {
        return (EAttribute)owningMembershipEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getOwningMembership_OwnedMemberShortName() {
        return (EAttribute)owningMembershipEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getPackage() {
        return packageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getPackage_FilterCondition() {
        return (EReference)packageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getPackage__IncludeAsMember__Element() {
        return packageEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getParameterMembership() {
        return parameterMembershipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getParameterMembership_OwnedMemberParameter() {
        return (EReference)parameterMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getPartDefinition() {
        return partDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getPartUsage() {
        return partUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getPartUsage_PartDefinition() {
        return (EReference)partUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getPerformActionUsage() {
        return performActionUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getPerformActionUsage_PerformedAction() {
        return (EReference)performActionUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getPortConjugation() {
        return portConjugationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getPortConjugation_ConjugatedPortDefinition() {
        return (EReference)portConjugationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getPortConjugation_OriginalPortDefinition() {
        return (EReference)portConjugationEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getPortDefinition() {
        return portDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getPortDefinition_ConjugatedPortDefinition() {
        return (EReference)portDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getPortUsage() {
        return portUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getPortUsage_PortDefinition() {
        return (EReference)portUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getPredicate() {
        return predicateEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getRedefinition() {
        return redefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getRedefinition_RedefinedFeature() {
        return (EReference)redefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getRedefinition_RedefiningFeature() {
        return (EReference)redefinitionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getReferenceSubsetting() {
        return referenceSubsettingEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getReferenceSubsetting_ReferencedFeature() {
        return (EReference)referenceSubsettingEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getReferenceSubsetting_ReferencingFeature() {
        return (EReference)referenceSubsettingEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getReferenceUsage() {
        return referenceUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getRelationship() {
        return relationshipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getRelationship_IsImplied() {
        return (EAttribute)relationshipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getRelationship_OwnedRelatedElement() {
        return (EReference)relationshipEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getRelationship_OwningRelatedElement() {
        return (EReference)relationshipEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getRelationship_RelatedElement() {
        return (EReference)relationshipEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getRelationship_Source() {
        return (EReference)relationshipEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getRelationship_Target() {
        return (EReference)relationshipEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getRenderingDefinition() {
        return renderingDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getRenderingDefinition_Rendering() {
        return (EReference)renderingDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getRenderingUsage() {
        return renderingUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getRenderingUsage_RenderingDefinition() {
        return (EReference)renderingUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getRequirementConstraintMembership() {
        return requirementConstraintMembershipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getRequirementConstraintMembership_Kind() {
        return (EAttribute)requirementConstraintMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getRequirementConstraintMembership_OwnedConstraint() {
        return (EReference)requirementConstraintMembershipEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getRequirementConstraintMembership_ReferencedConstraint() {
        return (EReference)requirementConstraintMembershipEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getRequirementDefinition() {
        return requirementDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getRequirementDefinition_ActorParameter() {
        return (EReference)requirementDefinitionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getRequirementDefinition_AssumedConstraint() {
        return (EReference)requirementDefinitionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getRequirementDefinition_FramedConcern() {
        return (EReference)requirementDefinitionEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getRequirementDefinition_ReqId() {
        return (EAttribute)requirementDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getRequirementDefinition_RequiredConstraint() {
        return (EReference)requirementDefinitionEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getRequirementDefinition_StakeholderParameter() {
        return (EReference)requirementDefinitionEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getRequirementDefinition_SubjectParameter() {
        return (EReference)requirementDefinitionEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getRequirementDefinition_Text() {
        return (EAttribute)requirementDefinitionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getRequirementUsage() {
        return requirementUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getRequirementUsage_ActorParameter() {
        return (EReference)requirementUsageEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getRequirementUsage_AssumedConstraint() {
        return (EReference)requirementUsageEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getRequirementUsage_FramedConcern() {
        return (EReference)requirementUsageEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getRequirementUsage_ReqId() {
        return (EAttribute)requirementUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getRequirementUsage_RequiredConstraint() {
        return (EReference)requirementUsageEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getRequirementUsage_RequirementDefinition() {
        return (EReference)requirementUsageEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getRequirementUsage_StakeholderParameter() {
        return (EReference)requirementUsageEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getRequirementUsage_SubjectParameter() {
        return (EReference)requirementUsageEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getRequirementUsage_Text() {
        return (EAttribute)requirementUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getRequirementVerificationMembership() {
        return requirementVerificationMembershipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getRequirementVerificationMembership_OwnedRequirement() {
        return (EReference)requirementVerificationMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getRequirementVerificationMembership_VerifiedRequirement() {
        return (EReference)requirementVerificationMembershipEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getResultExpressionMembership() {
        return resultExpressionMembershipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getResultExpressionMembership_OwnedResultExpression() {
        return (EReference)resultExpressionMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getReturnParameterMembership() {
        return returnParameterMembershipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSatisfyRequirementUsage() {
        return satisfyRequirementUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSatisfyRequirementUsage_SatisfiedRequirement() {
        return (EReference)satisfyRequirementUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSatisfyRequirementUsage_SatisfyingFeature() {
        return (EReference)satisfyRequirementUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSelectExpression() {
        return selectExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSendActionUsage() {
        return sendActionUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSendActionUsage_PayloadArgument() {
        return (EReference)sendActionUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSendActionUsage_ReceiverArgument() {
        return (EReference)sendActionUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSendActionUsage_SenderArgument() {
        return (EReference)sendActionUsageEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSpecialization() {
        return specializationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSpecialization_General() {
        return (EReference)specializationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSpecialization_OwningType() {
        return (EReference)specializationEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSpecialization_Specific() {
        return (EReference)specializationEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getStakeholderMembership() {
        return stakeholderMembershipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getStakeholderMembership_OwnedStakeholderParameter() {
        return (EReference)stakeholderMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getStateDefinition() {
        return stateDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getStateDefinition_DoAction() {
        return (EReference)stateDefinitionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getStateDefinition_EntryAction() {
        return (EReference)stateDefinitionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getStateDefinition_ExitAction() {
        return (EReference)stateDefinitionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getStateDefinition_IsParallel() {
        return (EAttribute)stateDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getStateDefinition_State() {
        return (EReference)stateDefinitionEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getStateSubactionMembership() {
        return stateSubactionMembershipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getStateSubactionMembership_Action() {
        return (EReference)stateSubactionMembershipEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getStateSubactionMembership_Kind() {
        return (EAttribute)stateSubactionMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getStateUsage() {
        return stateUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getStateUsage_DoAction() {
        return (EReference)stateUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getStateUsage_EntryAction() {
        return (EReference)stateUsageEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getStateUsage_ExitAction() {
        return (EReference)stateUsageEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getStateUsage_IsParallel() {
        return (EAttribute)stateUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getStateUsage_StateDefinition() {
        return (EReference)stateUsageEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getStateUsage__IsSubstateUsage__boolean() {
        return stateUsageEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getStep() {
        return stepEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getStep_Behavior() {
        return (EReference)stepEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getStep_Parameter() {
        return (EReference)stepEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getStructure() {
        return structureEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSubclassification() {
        return subclassificationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSubclassification_OwningClassifier() {
        return (EReference)subclassificationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSubclassification_Subclassifier() {
        return (EReference)subclassificationEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSubclassification_Superclassifier() {
        return (EReference)subclassificationEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSubjectMembership() {
        return subjectMembershipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSubjectMembership_OwnedSubjectParameter() {
        return (EReference)subjectMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSubsetting() {
        return subsettingEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSubsetting_OwningFeature() {
        return (EReference)subsettingEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSubsetting_SubsettedFeature() {
        return (EReference)subsettingEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSubsetting_SubsettingFeature() {
        return (EReference)subsettingEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSuccession() {
        return successionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSuccession_EffectStep() {
        return (EReference)successionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSuccession_GuardExpression() {
        return (EReference)successionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSuccession_TransitionStep() {
        return (EReference)successionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getSuccession_TriggerStep() {
        return (EReference)successionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSuccessionAsUsage() {
        return successionAsUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSuccessionFlowConnectionUsage() {
        return successionFlowConnectionUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getSuccessionItemFlow() {
        return successionItemFlowEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTextualRepresentation() {
        return textualRepresentationEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getTextualRepresentation_Body() {
        return (EAttribute)textualRepresentationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getTextualRepresentation_Language() {
        return (EAttribute)textualRepresentationEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getTextualRepresentation_RepresentedElement() {
        return (EReference)textualRepresentationEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTransitionFeatureMembership() {
        return transitionFeatureMembershipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getTransitionFeatureMembership_Kind() {
        return (EAttribute)transitionFeatureMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getTransitionFeatureMembership_TransitionFeature() {
        return (EReference)transitionFeatureMembershipEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTransitionUsage() {
        return transitionUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getTransitionUsage_EffectAction() {
        return (EReference)transitionUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getTransitionUsage_GuardExpression() {
        return (EReference)transitionUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getTransitionUsage_Source() {
        return (EReference)transitionUsageEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getTransitionUsage_Succession() {
        return (EReference)transitionUsageEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getTransitionUsage_Target() {
        return (EReference)transitionUsageEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getTransitionUsage_TriggerAction() {
        return (EReference)transitionUsageEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getTransitionUsage__TriggerPayloadParameter() {
        return transitionUsageEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTriggerInvocationExpression() {
        return triggerInvocationExpressionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getTriggerInvocationExpression_Kind() {
        return (EAttribute)triggerInvocationExpressionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getType() {
        return typeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getType_DifferencingType() {
        return (EReference)typeEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getType_DirectedFeature() {
        return (EReference)typeEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getType_EndFeature() {
        return (EReference)typeEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getType_Feature() {
        return (EReference)typeEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getType_FeatureMembership() {
        return (EReference)typeEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getType_InheritedFeature() {
        return (EReference)typeEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getType_InheritedMembership() {
        return (EReference)typeEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getType_Input() {
        return (EReference)typeEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getType_IntersectingType() {
        return (EReference)typeEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getType_IsAbstract() {
        return (EAttribute)typeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getType_IsConjugated() {
        return (EAttribute)typeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getType_IsSufficient() {
        return (EAttribute)typeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getType_Multiplicity() {
        return (EReference)typeEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getType_Output() {
        return (EReference)typeEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getType_OwnedConjugator() {
        return (EReference)typeEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getType_OwnedDifferencing() {
        return (EReference)typeEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getType_OwnedDisjoining() {
        return (EReference)typeEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getType_OwnedEndFeature() {
        return (EReference)typeEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getType_OwnedFeature() {
        return (EReference)typeEClass.getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getType_OwnedFeatureMembership() {
        return (EReference)typeEClass.getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getType_OwnedIntersecting() {
        return (EReference)typeEClass.getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getType_OwnedSpecialization() {
        return (EReference)typeEClass.getEStructuralFeatures().get(21);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getType_OwnedUnioning() {
        return (EReference)typeEClass.getEStructuralFeatures().get(22);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getType_UnioningType() {
        return (EReference)typeEClass.getEStructuralFeatures().get(23);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getType__AllSupertypes() {
        return typeEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getType__DirectionOf__Feature() {
        return typeEClass.getEOperations().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getType__InheritedMemberships__EList() {
        return typeEClass.getEOperations().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getType__Specializes__Type() {
        return typeEClass.getEOperations().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getType__SpecializesFromLibrary__String() {
        return typeEClass.getEOperations().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getTypeFeaturing() {
        return typeFeaturingEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getTypeFeaturing_FeatureOfType() {
        return (EReference)typeFeaturingEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getTypeFeaturing_FeaturingType() {
        return (EReference)typeFeaturingEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getTypeFeaturing_OwningFeatureOfType() {
        return (EReference)typeFeaturingEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getUnioning() {
        return unioningEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUnioning_TypeUnioned() {
        return (EReference)unioningEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUnioning_UnioningType() {
        return (EReference)unioningEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getUsage() {
        return usageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_Definition() {
        return (EReference)usageEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_DirectedUsage() {
        return (EReference)usageEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getUsage_IsReference() {
        return (EAttribute)usageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EAttribute getUsage_IsVariation() {
        return (EAttribute)usageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_NestedAction() {
        return (EReference)usageEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_NestedAllocation() {
        return (EReference)usageEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_NestedAnalysisCase() {
        return (EReference)usageEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_NestedAttribute() {
        return (EReference)usageEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_NestedCalculation() {
        return (EReference)usageEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_NestedCase() {
        return (EReference)usageEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_NestedConcern() {
        return (EReference)usageEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_NestedConnection() {
        return (EReference)usageEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_NestedConstraint() {
        return (EReference)usageEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_NestedEnumeration() {
        return (EReference)usageEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_NestedFlow() {
        return (EReference)usageEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_NestedInterface() {
        return (EReference)usageEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_NestedItem() {
        return (EReference)usageEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_NestedMetadata() {
        return (EReference)usageEClass.getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_NestedOccurrence() {
        return (EReference)usageEClass.getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_NestedPart() {
        return (EReference)usageEClass.getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_NestedPort() {
        return (EReference)usageEClass.getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_NestedReference() {
        return (EReference)usageEClass.getEStructuralFeatures().get(21);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_NestedRendering() {
        return (EReference)usageEClass.getEStructuralFeatures().get(22);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_NestedRequirement() {
        return (EReference)usageEClass.getEStructuralFeatures().get(23);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_NestedState() {
        return (EReference)usageEClass.getEStructuralFeatures().get(24);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_NestedTransition() {
        return (EReference)usageEClass.getEStructuralFeatures().get(25);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_NestedUsage() {
        return (EReference)usageEClass.getEStructuralFeatures().get(26);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_NestedUseCase() {
        return (EReference)usageEClass.getEStructuralFeatures().get(27);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_NestedVerificationCase() {
        return (EReference)usageEClass.getEStructuralFeatures().get(28);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_NestedView() {
        return (EReference)usageEClass.getEStructuralFeatures().get(29);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_NestedViewpoint() {
        return (EReference)usageEClass.getEStructuralFeatures().get(30);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_OwningDefinition() {
        return (EReference)usageEClass.getEStructuralFeatures().get(31);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_OwningUsage() {
        return (EReference)usageEClass.getEStructuralFeatures().get(32);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_Usage() {
        return (EReference)usageEClass.getEStructuralFeatures().get(33);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_Variant() {
        return (EReference)usageEClass.getEStructuralFeatures().get(34);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUsage_VariantMembership() {
        return (EReference)usageEClass.getEStructuralFeatures().get(35);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getUseCaseDefinition() {
        return useCaseDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUseCaseDefinition_IncludedUseCase() {
        return (EReference)useCaseDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getUseCaseUsage() {
        return useCaseUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUseCaseUsage_IncludedUseCase() {
        return (EReference)useCaseUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getUseCaseUsage_UseCaseDefinition() {
        return (EReference)useCaseUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getVariantMembership() {
        return variantMembershipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getVariantMembership_OwnedVariantUsage() {
        return (EReference)variantMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getVerificationCaseDefinition() {
        return verificationCaseDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getVerificationCaseDefinition_VerifiedRequirement() {
        return (EReference)verificationCaseDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getVerificationCaseUsage() {
        return verificationCaseUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getVerificationCaseUsage_VerificationCaseDefinition() {
        return (EReference)verificationCaseUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getVerificationCaseUsage_VerifiedRequirement() {
        return (EReference)verificationCaseUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getViewDefinition() {
        return viewDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getViewDefinition_SatisfiedViewpoint() {
        return (EReference)viewDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getViewDefinition_View() {
        return (EReference)viewDefinitionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getViewDefinition_ViewCondition() {
        return (EReference)viewDefinitionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getViewDefinition_ViewRendering() {
        return (EReference)viewDefinitionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getViewpointDefinition() {
        return viewpointDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getViewpointDefinition_ViewpointStakeholder() {
        return (EReference)viewpointDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getViewpointUsage() {
        return viewpointUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getViewpointUsage_ViewpointDefinition() {
        return (EReference)viewpointUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getViewpointUsage_ViewpointStakeholder() {
        return (EReference)viewpointUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getViewRenderingMembership() {
        return viewRenderingMembershipEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getViewRenderingMembership_OwnedRendering() {
        return (EReference)viewRenderingMembershipEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getViewRenderingMembership_ReferencedRendering() {
        return (EReference)viewRenderingMembershipEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getViewUsage() {
        return viewUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getViewUsage_ExposedElement() {
        return (EReference)viewUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getViewUsage_SatisfiedViewpoint() {
        return (EReference)viewUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getViewUsage_ViewCondition() {
        return (EReference)viewUsageEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getViewUsage_ViewDefinition() {
        return (EReference)viewUsageEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getViewUsage_ViewRendering() {
        return (EReference)viewUsageEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EOperation getViewUsage__IncludeAsExposed__Element() {
        return viewUsageEClass.getEOperations().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EClass getWhileLoopActionUsage() {
        return whileLoopActionUsageEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getWhileLoopActionUsage_UntilArgument() {
        return (EReference)whileLoopActionUsageEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EReference getWhileLoopActionUsage_WhileArgument() {
        return (EReference)whileLoopActionUsageEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EEnum getFeatureDirectionKind() {
        return featureDirectionKindEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EEnum getPortionKind() {
        return portionKindEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EEnum getRequirementConstraintKind() {
        return requirementConstraintKindEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EEnum getStateSubactionKind() {
        return stateSubactionKindEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EEnum getTransitionFeatureKind() {
        return transitionFeatureKindEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EEnum getTriggerKind() {
        return triggerKindEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EEnum getVisibilityKind() {
        return visibilityKindEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public SysmlFactory getSysmlFactory() {
        return (SysmlFactory)getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package.  This method is
     * guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void createPackageContents() {
        if (isCreated) return;
        isCreated = true;

        // Create classes and their features
        acceptActionUsageEClass = createEClass(ACCEPT_ACTION_USAGE);
        createEReference(acceptActionUsageEClass, ACCEPT_ACTION_USAGE__PAYLOAD_ARGUMENT);
        createEReference(acceptActionUsageEClass, ACCEPT_ACTION_USAGE__PAYLOAD_PARAMETER);
        createEReference(acceptActionUsageEClass, ACCEPT_ACTION_USAGE__RECEIVER_ARGUMENT);
        createEOperation(acceptActionUsageEClass, ACCEPT_ACTION_USAGE___IS_TRIGGER_ACTION);

        actionDefinitionEClass = createEClass(ACTION_DEFINITION);
        createEReference(actionDefinitionEClass, ACTION_DEFINITION__ACTION);

        actionUsageEClass = createEClass(ACTION_USAGE);
        createEReference(actionUsageEClass, ACTION_USAGE__ACTION_DEFINITION);
        createEOperation(actionUsageEClass, ACTION_USAGE___ARGUMENT__INT);
        createEOperation(actionUsageEClass, ACTION_USAGE___INPUT_PARAMETER__INT);
        createEOperation(actionUsageEClass, ACTION_USAGE___INPUT_PARAMETERS);
        createEOperation(actionUsageEClass, ACTION_USAGE___IS_SUBACTION_USAGE);

        actorMembershipEClass = createEClass(ACTOR_MEMBERSHIP);
        createEReference(actorMembershipEClass, ACTOR_MEMBERSHIP__OWNED_ACTOR_PARAMETER);

        allocationDefinitionEClass = createEClass(ALLOCATION_DEFINITION);
        createEReference(allocationDefinitionEClass, ALLOCATION_DEFINITION__ALLOCATION);

        allocationUsageEClass = createEClass(ALLOCATION_USAGE);
        createEReference(allocationUsageEClass, ALLOCATION_USAGE__ALLOCATION_DEFINITION);

        analysisCaseDefinitionEClass = createEClass(ANALYSIS_CASE_DEFINITION);
        createEReference(analysisCaseDefinitionEClass, ANALYSIS_CASE_DEFINITION__ANALYSIS_ACTION);
        createEReference(analysisCaseDefinitionEClass, ANALYSIS_CASE_DEFINITION__RESULT_EXPRESSION);

        analysisCaseUsageEClass = createEClass(ANALYSIS_CASE_USAGE);
        createEReference(analysisCaseUsageEClass, ANALYSIS_CASE_USAGE__ANALYSIS_ACTION);
        createEReference(analysisCaseUsageEClass, ANALYSIS_CASE_USAGE__ANALYSIS_CASE_DEFINITION);
        createEReference(analysisCaseUsageEClass, ANALYSIS_CASE_USAGE__RESULT_EXPRESSION);

        annotatingElementEClass = createEClass(ANNOTATING_ELEMENT);
        createEReference(annotatingElementEClass, ANNOTATING_ELEMENT__ANNOTATED_ELEMENT);
        createEReference(annotatingElementEClass, ANNOTATING_ELEMENT__ANNOTATION);

        annotationEClass = createEClass(ANNOTATION);
        createEReference(annotationEClass, ANNOTATION__ANNOTATED_ELEMENT);
        createEReference(annotationEClass, ANNOTATION__ANNOTATING_ELEMENT);
        createEReference(annotationEClass, ANNOTATION__OWNING_ANNOTATED_ELEMENT);

        assertConstraintUsageEClass = createEClass(ASSERT_CONSTRAINT_USAGE);
        createEReference(assertConstraintUsageEClass, ASSERT_CONSTRAINT_USAGE__ASSERTED_CONSTRAINT);

        assignmentActionUsageEClass = createEClass(ASSIGNMENT_ACTION_USAGE);
        createEReference(assignmentActionUsageEClass, ASSIGNMENT_ACTION_USAGE__REFERENT);
        createEReference(assignmentActionUsageEClass, ASSIGNMENT_ACTION_USAGE__TARGET_ARGUMENT);
        createEReference(assignmentActionUsageEClass, ASSIGNMENT_ACTION_USAGE__VALUE_EXPRESSION);

        associationEClass = createEClass(ASSOCIATION);
        createEReference(associationEClass, ASSOCIATION__ASSOCIATION_END);
        createEReference(associationEClass, ASSOCIATION__RELATED_TYPE);
        createEReference(associationEClass, ASSOCIATION__SOURCE_TYPE);
        createEReference(associationEClass, ASSOCIATION__TARGET_TYPE);

        associationStructureEClass = createEClass(ASSOCIATION_STRUCTURE);

        attributeDefinitionEClass = createEClass(ATTRIBUTE_DEFINITION);

        attributeUsageEClass = createEClass(ATTRIBUTE_USAGE);
        createEReference(attributeUsageEClass, ATTRIBUTE_USAGE__ATTRIBUTE_DEFINITION);

        behaviorEClass = createEClass(BEHAVIOR);
        createEReference(behaviorEClass, BEHAVIOR__PARAMETER);
        createEReference(behaviorEClass, BEHAVIOR__STEP);

        bindingConnectorEClass = createEClass(BINDING_CONNECTOR);

        bindingConnectorAsUsageEClass = createEClass(BINDING_CONNECTOR_AS_USAGE);

        booleanExpressionEClass = createEClass(BOOLEAN_EXPRESSION);
        createEReference(booleanExpressionEClass, BOOLEAN_EXPRESSION__PREDICATE);

        calculationDefinitionEClass = createEClass(CALCULATION_DEFINITION);
        createEReference(calculationDefinitionEClass, CALCULATION_DEFINITION__CALCULATION);

        calculationUsageEClass = createEClass(CALCULATION_USAGE);
        createEReference(calculationUsageEClass, CALCULATION_USAGE__CALCULATION_DEFINITION);

        caseDefinitionEClass = createEClass(CASE_DEFINITION);
        createEReference(caseDefinitionEClass, CASE_DEFINITION__ACTOR_PARAMETER);
        createEReference(caseDefinitionEClass, CASE_DEFINITION__OBJECTIVE_REQUIREMENT);
        createEReference(caseDefinitionEClass, CASE_DEFINITION__SUBJECT_PARAMETER);

        caseUsageEClass = createEClass(CASE_USAGE);
        createEReference(caseUsageEClass, CASE_USAGE__ACTOR_PARAMETER);
        createEReference(caseUsageEClass, CASE_USAGE__CASE_DEFINITION);
        createEReference(caseUsageEClass, CASE_USAGE__OBJECTIVE_REQUIREMENT);
        createEReference(caseUsageEClass, CASE_USAGE__SUBJECT_PARAMETER);

        classEClass = createEClass(CLASS);

        classifierEClass = createEClass(CLASSIFIER);
        createEReference(classifierEClass, CLASSIFIER__OWNED_SUBCLASSIFICATION);

        collectExpressionEClass = createEClass(COLLECT_EXPRESSION);

        commentEClass = createEClass(COMMENT);
        createEAttribute(commentEClass, COMMENT__BODY);
        createEAttribute(commentEClass, COMMENT__LOCALE);

        concernDefinitionEClass = createEClass(CONCERN_DEFINITION);

        concernUsageEClass = createEClass(CONCERN_USAGE);
        createEReference(concernUsageEClass, CONCERN_USAGE__CONCERN_DEFINITION);

        conjugatedPortDefinitionEClass = createEClass(CONJUGATED_PORT_DEFINITION);
        createEReference(conjugatedPortDefinitionEClass, CONJUGATED_PORT_DEFINITION__ORIGINAL_PORT_DEFINITION);
        createEReference(conjugatedPortDefinitionEClass, CONJUGATED_PORT_DEFINITION__OWNED_PORT_CONJUGATOR);

        conjugatedPortTypingEClass = createEClass(CONJUGATED_PORT_TYPING);
        createEReference(conjugatedPortTypingEClass, CONJUGATED_PORT_TYPING__CONJUGATED_PORT_DEFINITION);
        createEReference(conjugatedPortTypingEClass, CONJUGATED_PORT_TYPING__PORT_DEFINITION);

        conjugationEClass = createEClass(CONJUGATION);
        createEReference(conjugationEClass, CONJUGATION__CONJUGATED_TYPE);
        createEReference(conjugationEClass, CONJUGATION__ORIGINAL_TYPE);
        createEReference(conjugationEClass, CONJUGATION__OWNING_TYPE);

        connectionDefinitionEClass = createEClass(CONNECTION_DEFINITION);
        createEReference(connectionDefinitionEClass, CONNECTION_DEFINITION__CONNECTION_END);

        connectionUsageEClass = createEClass(CONNECTION_USAGE);
        createEReference(connectionUsageEClass, CONNECTION_USAGE__CONNECTION_DEFINITION);

        connectorEClass = createEClass(CONNECTOR);
        createEAttribute(connectorEClass, CONNECTOR__IS_DIRECTED);
        createEReference(connectorEClass, CONNECTOR__ASSOCIATION);
        createEReference(connectorEClass, CONNECTOR__CONNECTOR_END);
        createEReference(connectorEClass, CONNECTOR__RELATED_FEATURE);
        createEReference(connectorEClass, CONNECTOR__SOURCE_FEATURE);
        createEReference(connectorEClass, CONNECTOR__TARGET_FEATURE);

        connectorAsUsageEClass = createEClass(CONNECTOR_AS_USAGE);

        constraintDefinitionEClass = createEClass(CONSTRAINT_DEFINITION);

        constraintUsageEClass = createEClass(CONSTRAINT_USAGE);
        createEReference(constraintUsageEClass, CONSTRAINT_USAGE__CONSTRAINT_DEFINITION);

        controlNodeEClass = createEClass(CONTROL_NODE);
        createEOperation(controlNodeEClass, CONTROL_NODE___MULTIPLICITY_HAS_BOUNDS__MULTIPLICITY_INT_INT);

        dataTypeEClass = createEClass(DATA_TYPE);

        decisionNodeEClass = createEClass(DECISION_NODE);

        definitionEClass = createEClass(DEFINITION);
        createEAttribute(definitionEClass, DEFINITION__IS_VARIATION);
        createEReference(definitionEClass, DEFINITION__DIRECTED_USAGE);
        createEReference(definitionEClass, DEFINITION__OWNED_ACTION);
        createEReference(definitionEClass, DEFINITION__OWNED_ALLOCATION);
        createEReference(definitionEClass, DEFINITION__OWNED_ANALYSIS_CASE);
        createEReference(definitionEClass, DEFINITION__OWNED_ATTRIBUTE);
        createEReference(definitionEClass, DEFINITION__OWNED_CALCULATION);
        createEReference(definitionEClass, DEFINITION__OWNED_CASE);
        createEReference(definitionEClass, DEFINITION__OWNED_CONCERN);
        createEReference(definitionEClass, DEFINITION__OWNED_CONNECTION);
        createEReference(definitionEClass, DEFINITION__OWNED_CONSTRAINT);
        createEReference(definitionEClass, DEFINITION__OWNED_ENUMERATION);
        createEReference(definitionEClass, DEFINITION__OWNED_FLOW);
        createEReference(definitionEClass, DEFINITION__OWNED_INTERFACE);
        createEReference(definitionEClass, DEFINITION__OWNED_ITEM);
        createEReference(definitionEClass, DEFINITION__OWNED_METADATA);
        createEReference(definitionEClass, DEFINITION__OWNED_OCCURRENCE);
        createEReference(definitionEClass, DEFINITION__OWNED_PART);
        createEReference(definitionEClass, DEFINITION__OWNED_PORT);
        createEReference(definitionEClass, DEFINITION__OWNED_REFERENCE);
        createEReference(definitionEClass, DEFINITION__OWNED_RENDERING);
        createEReference(definitionEClass, DEFINITION__OWNED_REQUIREMENT);
        createEReference(definitionEClass, DEFINITION__OWNED_STATE);
        createEReference(definitionEClass, DEFINITION__OWNED_TRANSITION);
        createEReference(definitionEClass, DEFINITION__OWNED_USAGE);
        createEReference(definitionEClass, DEFINITION__OWNED_USE_CASE);
        createEReference(definitionEClass, DEFINITION__OWNED_VERIFICATION_CASE);
        createEReference(definitionEClass, DEFINITION__OWNED_VIEW);
        createEReference(definitionEClass, DEFINITION__OWNED_VIEWPOINT);
        createEReference(definitionEClass, DEFINITION__USAGE);
        createEReference(definitionEClass, DEFINITION__VARIANT);
        createEReference(definitionEClass, DEFINITION__VARIANT_MEMBERSHIP);

        dependencyEClass = createEClass(DEPENDENCY);
        createEReference(dependencyEClass, DEPENDENCY__CLIENT);
        createEReference(dependencyEClass, DEPENDENCY__SUPPLIER);

        differencingEClass = createEClass(DIFFERENCING);
        createEReference(differencingEClass, DIFFERENCING__DIFFERENCING_TYPE);
        createEReference(differencingEClass, DIFFERENCING__TYPE_DIFFERENCED);

        disjoiningEClass = createEClass(DISJOINING);
        createEReference(disjoiningEClass, DISJOINING__DISJOINING_TYPE);
        createEReference(disjoiningEClass, DISJOINING__OWNING_TYPE);
        createEReference(disjoiningEClass, DISJOINING__TYPE_DISJOINED);

        documentationEClass = createEClass(DOCUMENTATION);
        createEReference(documentationEClass, DOCUMENTATION__DOCUMENTED_ELEMENT);

        elementEClass = createEClass(ELEMENT);
        createEAttribute(elementEClass, ELEMENT__ALIAS_IDS);
        createEAttribute(elementEClass, ELEMENT__DECLARED_NAME);
        createEAttribute(elementEClass, ELEMENT__DECLARED_SHORT_NAME);
        createEAttribute(elementEClass, ELEMENT__ELEMENT_ID);
        createEAttribute(elementEClass, ELEMENT__IS_IMPLIED_INCLUDED);
        createEAttribute(elementEClass, ELEMENT__IS_LIBRARY_ELEMENT);
        createEAttribute(elementEClass, ELEMENT__NAME);
        createEAttribute(elementEClass, ELEMENT__QUALIFIED_NAME);
        createEAttribute(elementEClass, ELEMENT__SHORT_NAME);
        createEReference(elementEClass, ELEMENT__DOCUMENTATION);
        createEReference(elementEClass, ELEMENT__OWNED_ANNOTATION);
        createEReference(elementEClass, ELEMENT__OWNED_ELEMENT);
        createEReference(elementEClass, ELEMENT__OWNED_RELATIONSHIP);
        createEReference(elementEClass, ELEMENT__OWNER);
        createEReference(elementEClass, ELEMENT__OWNING_MEMBERSHIP);
        createEReference(elementEClass, ELEMENT__OWNING_NAMESPACE);
        createEReference(elementEClass, ELEMENT__OWNING_RELATIONSHIP);
        createEReference(elementEClass, ELEMENT__TEXTUAL_REPRESENTATION);
        createEOperation(elementEClass, ELEMENT___EFFECTIVE_NAME);
        createEOperation(elementEClass, ELEMENT___EFFECTIVE_SHORT_NAME);
        createEOperation(elementEClass, ELEMENT___ESCAPED_NAME);
        createEOperation(elementEClass, ELEMENT___LIBRARY_NAMESPACE);

        elementFilterMembershipEClass = createEClass(ELEMENT_FILTER_MEMBERSHIP);
        createEReference(elementFilterMembershipEClass, ELEMENT_FILTER_MEMBERSHIP__CONDITION);

        endFeatureMembershipEClass = createEClass(END_FEATURE_MEMBERSHIP);

        enumerationDefinitionEClass = createEClass(ENUMERATION_DEFINITION);
        createEReference(enumerationDefinitionEClass, ENUMERATION_DEFINITION__ENUMERATED_VALUE);

        enumerationUsageEClass = createEClass(ENUMERATION_USAGE);
        createEReference(enumerationUsageEClass, ENUMERATION_USAGE__ENUMERATION_DEFINITION);

        eventOccurrenceUsageEClass = createEClass(EVENT_OCCURRENCE_USAGE);
        createEReference(eventOccurrenceUsageEClass, EVENT_OCCURRENCE_USAGE__EVENT_OCCURRENCE);

        exhibitStateUsageEClass = createEClass(EXHIBIT_STATE_USAGE);
        createEReference(exhibitStateUsageEClass, EXHIBIT_STATE_USAGE__EXHIBITED_STATE);

        exposeEClass = createEClass(EXPOSE);

        expressionEClass = createEClass(EXPRESSION);
        createEAttribute(expressionEClass, EXPRESSION__IS_MODEL_LEVEL_EVALUABLE);
        createEReference(expressionEClass, EXPRESSION__FUNCTION);
        createEReference(expressionEClass, EXPRESSION__RESULT);
        createEOperation(expressionEClass, EXPRESSION___CHECK_CONDITION__ELEMENT);
        createEOperation(expressionEClass, EXPRESSION___EVALUATE__ELEMENT);
        createEOperation(expressionEClass, EXPRESSION___MODEL_LEVEL_EVALUABLE__ELIST);

        featureEClass = createEClass(FEATURE);
        createEAttribute(featureEClass, FEATURE__DIRECTION);
        createEAttribute(featureEClass, FEATURE__IS_COMPOSITE);
        createEAttribute(featureEClass, FEATURE__IS_DERIVED);
        createEAttribute(featureEClass, FEATURE__IS_END);
        createEAttribute(featureEClass, FEATURE__IS_NONUNIQUE);
        createEAttribute(featureEClass, FEATURE__IS_ORDERED);
        createEAttribute(featureEClass, FEATURE__IS_PORTION);
        createEAttribute(featureEClass, FEATURE__IS_READ_ONLY);
        createEAttribute(featureEClass, FEATURE__IS_UNIQUE);
        createEReference(featureEClass, FEATURE__CHAINING_FEATURE);
        createEReference(featureEClass, FEATURE__END_OWNING_TYPE);
        createEReference(featureEClass, FEATURE__FEATURING_TYPE);
        createEReference(featureEClass, FEATURE__OWNED_FEATURE_CHAINING);
        createEReference(featureEClass, FEATURE__OWNED_FEATURE_INVERTING);
        createEReference(featureEClass, FEATURE__OWNED_REDEFINITION);
        createEReference(featureEClass, FEATURE__OWNED_REFERENCE_SUBSETTING);
        createEReference(featureEClass, FEATURE__OWNED_SUBSETTING);
        createEReference(featureEClass, FEATURE__OWNED_TYPE_FEATURING);
        createEReference(featureEClass, FEATURE__OWNED_TYPING);
        createEReference(featureEClass, FEATURE__OWNING_FEATURE_MEMBERSHIP);
        createEReference(featureEClass, FEATURE__OWNING_TYPE);
        createEReference(featureEClass, FEATURE__TYPE);
        createEOperation(featureEClass, FEATURE___DIRECTION_FOR__TYPE);
        createEOperation(featureEClass, FEATURE___IS_FEATURED_WITHIN__TYPE);
        createEOperation(featureEClass, FEATURE___NAMING_FEATURE);
        createEOperation(featureEClass, FEATURE___REDEFINES__FEATURE);
        createEOperation(featureEClass, FEATURE___REDEFINES_FROM_LIBRARY__STRING);
        createEOperation(featureEClass, FEATURE___SUBSETS_CHAIN__FEATURE_FEATURE);

        featureChainExpressionEClass = createEClass(FEATURE_CHAIN_EXPRESSION);
        createEReference(featureChainExpressionEClass, FEATURE_CHAIN_EXPRESSION__TARGET_FEATURE);
        createEOperation(featureChainExpressionEClass, FEATURE_CHAIN_EXPRESSION___SOURCE_TARGET_FEATURE);

        featureChainingEClass = createEClass(FEATURE_CHAINING);
        createEReference(featureChainingEClass, FEATURE_CHAINING__CHAINING_FEATURE);
        createEReference(featureChainingEClass, FEATURE_CHAINING__FEATURE_CHAINED);

        featureInvertingEClass = createEClass(FEATURE_INVERTING);
        createEReference(featureInvertingEClass, FEATURE_INVERTING__FEATURE_INVERTED);
        createEReference(featureInvertingEClass, FEATURE_INVERTING__INVERTING_FEATURE);
        createEReference(featureInvertingEClass, FEATURE_INVERTING__OWNING_FEATURE);

        featureMembershipEClass = createEClass(FEATURE_MEMBERSHIP);
        createEReference(featureMembershipEClass, FEATURE_MEMBERSHIP__OWNED_MEMBER_FEATURE);
        createEReference(featureMembershipEClass, FEATURE_MEMBERSHIP__OWNING_TYPE);

        featureReferenceExpressionEClass = createEClass(FEATURE_REFERENCE_EXPRESSION);
        createEReference(featureReferenceExpressionEClass, FEATURE_REFERENCE_EXPRESSION__REFERENT);

        featureTypingEClass = createEClass(FEATURE_TYPING);
        createEReference(featureTypingEClass, FEATURE_TYPING__OWNING_FEATURE);
        createEReference(featureTypingEClass, FEATURE_TYPING__TYPE);
        createEReference(featureTypingEClass, FEATURE_TYPING__TYPED_FEATURE);

        featureValueEClass = createEClass(FEATURE_VALUE);
        createEAttribute(featureValueEClass, FEATURE_VALUE__IS_DEFAULT);
        createEAttribute(featureValueEClass, FEATURE_VALUE__IS_INITIAL);
        createEReference(featureValueEClass, FEATURE_VALUE__FEATURE_WITH_VALUE);
        createEReference(featureValueEClass, FEATURE_VALUE__VALUE);

        featuringEClass = createEClass(FEATURING);
        createEReference(featuringEClass, FEATURING__FEATURE);
        createEReference(featuringEClass, FEATURING__TYPE);

        flowConnectionDefinitionEClass = createEClass(FLOW_CONNECTION_DEFINITION);

        flowConnectionUsageEClass = createEClass(FLOW_CONNECTION_USAGE);
        createEReference(flowConnectionUsageEClass, FLOW_CONNECTION_USAGE__FLOW_CONNECTION_DEFINITION);

        forkNodeEClass = createEClass(FORK_NODE);

        forLoopActionUsageEClass = createEClass(FOR_LOOP_ACTION_USAGE);
        createEReference(forLoopActionUsageEClass, FOR_LOOP_ACTION_USAGE__LOOP_VARIABLE);
        createEReference(forLoopActionUsageEClass, FOR_LOOP_ACTION_USAGE__SEQ_ARGUMENT);

        framedConcernMembershipEClass = createEClass(FRAMED_CONCERN_MEMBERSHIP);
        createEReference(framedConcernMembershipEClass, FRAMED_CONCERN_MEMBERSHIP__OWNED_CONCERN);
        createEReference(framedConcernMembershipEClass, FRAMED_CONCERN_MEMBERSHIP__REFERENCED_CONCERN);

        functionEClass = createEClass(FUNCTION);
        createEAttribute(functionEClass, FUNCTION__IS_MODEL_LEVEL_EVALUABLE);
        createEReference(functionEClass, FUNCTION__EXPRESSION);
        createEReference(functionEClass, FUNCTION__RESULT);

        ifActionUsageEClass = createEClass(IF_ACTION_USAGE);
        createEReference(ifActionUsageEClass, IF_ACTION_USAGE__ELSE_ACTION);
        createEReference(ifActionUsageEClass, IF_ACTION_USAGE__IF_ARGUMENT);
        createEReference(ifActionUsageEClass, IF_ACTION_USAGE__THEN_ACTION);

        importEClass = createEClass(IMPORT);
        createEAttribute(importEClass, IMPORT__IS_IMPORT_ALL);
        createEAttribute(importEClass, IMPORT__IS_RECURSIVE);
        createEAttribute(importEClass, IMPORT__VISIBILITY);
        createEReference(importEClass, IMPORT__IMPORTED_ELEMENT);
        createEReference(importEClass, IMPORT__IMPORT_OWNING_NAMESPACE);
        createEOperation(importEClass, IMPORT___IMPORTED_MEMBERSHIPS__ELIST);

        includeUseCaseUsageEClass = createEClass(INCLUDE_USE_CASE_USAGE);
        createEReference(includeUseCaseUsageEClass, INCLUDE_USE_CASE_USAGE__USE_CASE_INCLUDED);

        interactionEClass = createEClass(INTERACTION);

        interfaceDefinitionEClass = createEClass(INTERFACE_DEFINITION);
        createEReference(interfaceDefinitionEClass, INTERFACE_DEFINITION__INTERFACE_END);

        interfaceUsageEClass = createEClass(INTERFACE_USAGE);
        createEReference(interfaceUsageEClass, INTERFACE_USAGE__INTERFACE_DEFINITION);

        intersectingEClass = createEClass(INTERSECTING);
        createEReference(intersectingEClass, INTERSECTING__INTERSECTING_TYPE);
        createEReference(intersectingEClass, INTERSECTING__TYPE_INTERSECTED);

        invariantEClass = createEClass(INVARIANT);
        createEAttribute(invariantEClass, INVARIANT__IS_NEGATED);

        invocationExpressionEClass = createEClass(INVOCATION_EXPRESSION);
        createEReference(invocationExpressionEClass, INVOCATION_EXPRESSION__ARGUMENT);

        itemDefinitionEClass = createEClass(ITEM_DEFINITION);

        itemFeatureEClass = createEClass(ITEM_FEATURE);

        itemFlowEClass = createEClass(ITEM_FLOW);
        createEReference(itemFlowEClass, ITEM_FLOW__INTERACTION);
        createEReference(itemFlowEClass, ITEM_FLOW__ITEM_FEATURE);
        createEReference(itemFlowEClass, ITEM_FLOW__ITEM_FLOW_END);
        createEReference(itemFlowEClass, ITEM_FLOW__ITEM_TYPE);
        createEReference(itemFlowEClass, ITEM_FLOW__SOURCE_OUTPUT_FEATURE);
        createEReference(itemFlowEClass, ITEM_FLOW__TARGET_INPUT_FEATURE);

        itemFlowEndEClass = createEClass(ITEM_FLOW_END);

        itemUsageEClass = createEClass(ITEM_USAGE);
        createEReference(itemUsageEClass, ITEM_USAGE__ITEM_DEFINITION);

        joinNodeEClass = createEClass(JOIN_NODE);

        libraryPackageEClass = createEClass(LIBRARY_PACKAGE);
        createEAttribute(libraryPackageEClass, LIBRARY_PACKAGE__IS_STANDARD);

        lifeClassEClass = createEClass(LIFE_CLASS);

        literalBooleanEClass = createEClass(LITERAL_BOOLEAN);
        createEAttribute(literalBooleanEClass, LITERAL_BOOLEAN__VALUE);

        literalExpressionEClass = createEClass(LITERAL_EXPRESSION);

        literalInfinityEClass = createEClass(LITERAL_INFINITY);

        literalIntegerEClass = createEClass(LITERAL_INTEGER);
        createEAttribute(literalIntegerEClass, LITERAL_INTEGER__VALUE);

        literalRationalEClass = createEClass(LITERAL_RATIONAL);
        createEAttribute(literalRationalEClass, LITERAL_RATIONAL__VALUE);

        literalStringEClass = createEClass(LITERAL_STRING);
        createEAttribute(literalStringEClass, LITERAL_STRING__VALUE);

        loopActionUsageEClass = createEClass(LOOP_ACTION_USAGE);
        createEReference(loopActionUsageEClass, LOOP_ACTION_USAGE__BODY_ACTION);

        membershipEClass = createEClass(MEMBERSHIP);
        createEAttribute(membershipEClass, MEMBERSHIP__MEMBER_ELEMENT_ID);
        createEAttribute(membershipEClass, MEMBERSHIP__MEMBER_NAME);
        createEAttribute(membershipEClass, MEMBERSHIP__MEMBER_SHORT_NAME);
        createEAttribute(membershipEClass, MEMBERSHIP__VISIBILITY);
        createEReference(membershipEClass, MEMBERSHIP__MEMBER_ELEMENT);
        createEReference(membershipEClass, MEMBERSHIP__MEMBERSHIP_OWNING_NAMESPACE);
        createEOperation(membershipEClass, MEMBERSHIP___IS_DISTINGUISHABLE_FROM__MEMBERSHIP);

        membershipExposeEClass = createEClass(MEMBERSHIP_EXPOSE);

        membershipImportEClass = createEClass(MEMBERSHIP_IMPORT);
        createEReference(membershipImportEClass, MEMBERSHIP_IMPORT__IMPORTED_MEMBERSHIP);

        mergeNodeEClass = createEClass(MERGE_NODE);

        metaclassEClass = createEClass(METACLASS);

        metadataAccessExpressionEClass = createEClass(METADATA_ACCESS_EXPRESSION);
        createEReference(metadataAccessExpressionEClass, METADATA_ACCESS_EXPRESSION__REFERENCED_ELEMENT);
        createEOperation(metadataAccessExpressionEClass, METADATA_ACCESS_EXPRESSION___METACLASS_FEATURE);

        metadataDefinitionEClass = createEClass(METADATA_DEFINITION);

        metadataFeatureEClass = createEClass(METADATA_FEATURE);
        createEReference(metadataFeatureEClass, METADATA_FEATURE__METACLASS);
        createEOperation(metadataFeatureEClass, METADATA_FEATURE___EVALUATE_FEATURE__FEATURE);
        createEOperation(metadataFeatureEClass, METADATA_FEATURE___IS_SEMANTIC);
        createEOperation(metadataFeatureEClass, METADATA_FEATURE___IS_SYNTACTIC);
        createEOperation(metadataFeatureEClass, METADATA_FEATURE___SYNTAX_ELEMENT);

        metadataUsageEClass = createEClass(METADATA_USAGE);
        createEReference(metadataUsageEClass, METADATA_USAGE__METADATA_DEFINITION);

        multiplicityEClass = createEClass(MULTIPLICITY);

        multiplicityRangeEClass = createEClass(MULTIPLICITY_RANGE);
        createEReference(multiplicityRangeEClass, MULTIPLICITY_RANGE__BOUND);
        createEReference(multiplicityRangeEClass, MULTIPLICITY_RANGE__LOWER_BOUND);
        createEReference(multiplicityRangeEClass, MULTIPLICITY_RANGE__UPPER_BOUND);
        createEOperation(multiplicityRangeEClass, MULTIPLICITY_RANGE___HAS_BOUNDS__INT_INT);
        createEOperation(multiplicityRangeEClass, MULTIPLICITY_RANGE___VALUE_OF__EXPRESSION);

        namespaceEClass = createEClass(NAMESPACE);
        createEReference(namespaceEClass, NAMESPACE__IMPORTED_MEMBERSHIP);
        createEReference(namespaceEClass, NAMESPACE__MEMBER);
        createEReference(namespaceEClass, NAMESPACE__MEMBERSHIP);
        createEReference(namespaceEClass, NAMESPACE__OWNED_IMPORT);
        createEReference(namespaceEClass, NAMESPACE__OWNED_MEMBER);
        createEReference(namespaceEClass, NAMESPACE__OWNED_MEMBERSHIP);
        createEOperation(namespaceEClass, NAMESPACE___IMPORTED_MEMBERSHIPS__ELIST);
        createEOperation(namespaceEClass, NAMESPACE___NAMES_OF__ELEMENT);
        createEOperation(namespaceEClass, NAMESPACE___QUALIFICATION_OF__STRING);
        createEOperation(namespaceEClass, NAMESPACE___RESOLVE__STRING);
        createEOperation(namespaceEClass, NAMESPACE___RESOLVE_GLOBAL__STRING);
        createEOperation(namespaceEClass, NAMESPACE___RESOLVE_LOCAL__STRING);
        createEOperation(namespaceEClass, NAMESPACE___RESOLVE_VISIBLE__STRING);
        createEOperation(namespaceEClass, NAMESPACE___UNQUALIFIED_NAME_OF__STRING);
        createEOperation(namespaceEClass, NAMESPACE___VISIBILITY_OF__MEMBERSHIP);
        createEOperation(namespaceEClass, NAMESPACE___VISIBLE_MEMBERSHIPS__ELIST_BOOLEAN_BOOLEAN);

        namespaceExposeEClass = createEClass(NAMESPACE_EXPOSE);

        namespaceImportEClass = createEClass(NAMESPACE_IMPORT);
        createEReference(namespaceImportEClass, NAMESPACE_IMPORT__IMPORTED_NAMESPACE);

        nullExpressionEClass = createEClass(NULL_EXPRESSION);

        objectiveMembershipEClass = createEClass(OBJECTIVE_MEMBERSHIP);
        createEReference(objectiveMembershipEClass, OBJECTIVE_MEMBERSHIP__OWNED_OBJECTIVE_REQUIREMENT);

        occurrenceDefinitionEClass = createEClass(OCCURRENCE_DEFINITION);
        createEAttribute(occurrenceDefinitionEClass, OCCURRENCE_DEFINITION__IS_INDIVIDUAL);
        createEReference(occurrenceDefinitionEClass, OCCURRENCE_DEFINITION__LIFE_CLASS);

        occurrenceUsageEClass = createEClass(OCCURRENCE_USAGE);
        createEAttribute(occurrenceUsageEClass, OCCURRENCE_USAGE__IS_INDIVIDUAL);
        createEAttribute(occurrenceUsageEClass, OCCURRENCE_USAGE__PORTION_KIND);
        createEReference(occurrenceUsageEClass, OCCURRENCE_USAGE__INDIVIDUAL_DEFINITION);
        createEReference(occurrenceUsageEClass, OCCURRENCE_USAGE__OCCURRENCE_DEFINITION);

        operatorExpressionEClass = createEClass(OPERATOR_EXPRESSION);
        createEAttribute(operatorExpressionEClass, OPERATOR_EXPRESSION__OPERATOR);
        createEReference(operatorExpressionEClass, OPERATOR_EXPRESSION__OPERAND);

        owningMembershipEClass = createEClass(OWNING_MEMBERSHIP);
        createEAttribute(owningMembershipEClass, OWNING_MEMBERSHIP__OWNED_MEMBER_ELEMENT_ID);
        createEAttribute(owningMembershipEClass, OWNING_MEMBERSHIP__OWNED_MEMBER_NAME);
        createEAttribute(owningMembershipEClass, OWNING_MEMBERSHIP__OWNED_MEMBER_SHORT_NAME);
        createEReference(owningMembershipEClass, OWNING_MEMBERSHIP__OWNED_MEMBER_ELEMENT);

        packageEClass = createEClass(PACKAGE);
        createEReference(packageEClass, PACKAGE__FILTER_CONDITION);
        createEOperation(packageEClass, PACKAGE___INCLUDE_AS_MEMBER__ELEMENT);

        parameterMembershipEClass = createEClass(PARAMETER_MEMBERSHIP);
        createEReference(parameterMembershipEClass, PARAMETER_MEMBERSHIP__OWNED_MEMBER_PARAMETER);

        partDefinitionEClass = createEClass(PART_DEFINITION);

        partUsageEClass = createEClass(PART_USAGE);
        createEReference(partUsageEClass, PART_USAGE__PART_DEFINITION);

        performActionUsageEClass = createEClass(PERFORM_ACTION_USAGE);
        createEReference(performActionUsageEClass, PERFORM_ACTION_USAGE__PERFORMED_ACTION);

        portConjugationEClass = createEClass(PORT_CONJUGATION);
        createEReference(portConjugationEClass, PORT_CONJUGATION__CONJUGATED_PORT_DEFINITION);
        createEReference(portConjugationEClass, PORT_CONJUGATION__ORIGINAL_PORT_DEFINITION);

        portDefinitionEClass = createEClass(PORT_DEFINITION);
        createEReference(portDefinitionEClass, PORT_DEFINITION__CONJUGATED_PORT_DEFINITION);

        portUsageEClass = createEClass(PORT_USAGE);
        createEReference(portUsageEClass, PORT_USAGE__PORT_DEFINITION);

        predicateEClass = createEClass(PREDICATE);

        redefinitionEClass = createEClass(REDEFINITION);
        createEReference(redefinitionEClass, REDEFINITION__REDEFINED_FEATURE);
        createEReference(redefinitionEClass, REDEFINITION__REDEFINING_FEATURE);

        referenceSubsettingEClass = createEClass(REFERENCE_SUBSETTING);
        createEReference(referenceSubsettingEClass, REFERENCE_SUBSETTING__REFERENCED_FEATURE);
        createEReference(referenceSubsettingEClass, REFERENCE_SUBSETTING__REFERENCING_FEATURE);

        referenceUsageEClass = createEClass(REFERENCE_USAGE);

        relationshipEClass = createEClass(RELATIONSHIP);
        createEAttribute(relationshipEClass, RELATIONSHIP__IS_IMPLIED);
        createEReference(relationshipEClass, RELATIONSHIP__OWNED_RELATED_ELEMENT);
        createEReference(relationshipEClass, RELATIONSHIP__OWNING_RELATED_ELEMENT);
        createEReference(relationshipEClass, RELATIONSHIP__RELATED_ELEMENT);
        createEReference(relationshipEClass, RELATIONSHIP__SOURCE);
        createEReference(relationshipEClass, RELATIONSHIP__TARGET);

        renderingDefinitionEClass = createEClass(RENDERING_DEFINITION);
        createEReference(renderingDefinitionEClass, RENDERING_DEFINITION__RENDERING);

        renderingUsageEClass = createEClass(RENDERING_USAGE);
        createEReference(renderingUsageEClass, RENDERING_USAGE__RENDERING_DEFINITION);

        requirementConstraintMembershipEClass = createEClass(REQUIREMENT_CONSTRAINT_MEMBERSHIP);
        createEAttribute(requirementConstraintMembershipEClass, REQUIREMENT_CONSTRAINT_MEMBERSHIP__KIND);
        createEReference(requirementConstraintMembershipEClass, REQUIREMENT_CONSTRAINT_MEMBERSHIP__OWNED_CONSTRAINT);
        createEReference(requirementConstraintMembershipEClass, REQUIREMENT_CONSTRAINT_MEMBERSHIP__REFERENCED_CONSTRAINT);

        requirementDefinitionEClass = createEClass(REQUIREMENT_DEFINITION);
        createEAttribute(requirementDefinitionEClass, REQUIREMENT_DEFINITION__REQ_ID);
        createEAttribute(requirementDefinitionEClass, REQUIREMENT_DEFINITION__TEXT);
        createEReference(requirementDefinitionEClass, REQUIREMENT_DEFINITION__ACTOR_PARAMETER);
        createEReference(requirementDefinitionEClass, REQUIREMENT_DEFINITION__ASSUMED_CONSTRAINT);
        createEReference(requirementDefinitionEClass, REQUIREMENT_DEFINITION__FRAMED_CONCERN);
        createEReference(requirementDefinitionEClass, REQUIREMENT_DEFINITION__REQUIRED_CONSTRAINT);
        createEReference(requirementDefinitionEClass, REQUIREMENT_DEFINITION__STAKEHOLDER_PARAMETER);
        createEReference(requirementDefinitionEClass, REQUIREMENT_DEFINITION__SUBJECT_PARAMETER);

        requirementUsageEClass = createEClass(REQUIREMENT_USAGE);
        createEAttribute(requirementUsageEClass, REQUIREMENT_USAGE__REQ_ID);
        createEAttribute(requirementUsageEClass, REQUIREMENT_USAGE__TEXT);
        createEReference(requirementUsageEClass, REQUIREMENT_USAGE__ACTOR_PARAMETER);
        createEReference(requirementUsageEClass, REQUIREMENT_USAGE__ASSUMED_CONSTRAINT);
        createEReference(requirementUsageEClass, REQUIREMENT_USAGE__FRAMED_CONCERN);
        createEReference(requirementUsageEClass, REQUIREMENT_USAGE__REQUIRED_CONSTRAINT);
        createEReference(requirementUsageEClass, REQUIREMENT_USAGE__REQUIREMENT_DEFINITION);
        createEReference(requirementUsageEClass, REQUIREMENT_USAGE__STAKEHOLDER_PARAMETER);
        createEReference(requirementUsageEClass, REQUIREMENT_USAGE__SUBJECT_PARAMETER);

        requirementVerificationMembershipEClass = createEClass(REQUIREMENT_VERIFICATION_MEMBERSHIP);
        createEReference(requirementVerificationMembershipEClass, REQUIREMENT_VERIFICATION_MEMBERSHIP__OWNED_REQUIREMENT);
        createEReference(requirementVerificationMembershipEClass, REQUIREMENT_VERIFICATION_MEMBERSHIP__VERIFIED_REQUIREMENT);

        resultExpressionMembershipEClass = createEClass(RESULT_EXPRESSION_MEMBERSHIP);
        createEReference(resultExpressionMembershipEClass, RESULT_EXPRESSION_MEMBERSHIP__OWNED_RESULT_EXPRESSION);

        returnParameterMembershipEClass = createEClass(RETURN_PARAMETER_MEMBERSHIP);

        satisfyRequirementUsageEClass = createEClass(SATISFY_REQUIREMENT_USAGE);
        createEReference(satisfyRequirementUsageEClass, SATISFY_REQUIREMENT_USAGE__SATISFIED_REQUIREMENT);
        createEReference(satisfyRequirementUsageEClass, SATISFY_REQUIREMENT_USAGE__SATISFYING_FEATURE);

        selectExpressionEClass = createEClass(SELECT_EXPRESSION);

        sendActionUsageEClass = createEClass(SEND_ACTION_USAGE);
        createEReference(sendActionUsageEClass, SEND_ACTION_USAGE__PAYLOAD_ARGUMENT);
        createEReference(sendActionUsageEClass, SEND_ACTION_USAGE__RECEIVER_ARGUMENT);
        createEReference(sendActionUsageEClass, SEND_ACTION_USAGE__SENDER_ARGUMENT);

        specializationEClass = createEClass(SPECIALIZATION);
        createEReference(specializationEClass, SPECIALIZATION__GENERAL);
        createEReference(specializationEClass, SPECIALIZATION__OWNING_TYPE);
        createEReference(specializationEClass, SPECIALIZATION__SPECIFIC);

        stakeholderMembershipEClass = createEClass(STAKEHOLDER_MEMBERSHIP);
        createEReference(stakeholderMembershipEClass, STAKEHOLDER_MEMBERSHIP__OWNED_STAKEHOLDER_PARAMETER);

        stateDefinitionEClass = createEClass(STATE_DEFINITION);
        createEAttribute(stateDefinitionEClass, STATE_DEFINITION__IS_PARALLEL);
        createEReference(stateDefinitionEClass, STATE_DEFINITION__DO_ACTION);
        createEReference(stateDefinitionEClass, STATE_DEFINITION__ENTRY_ACTION);
        createEReference(stateDefinitionEClass, STATE_DEFINITION__EXIT_ACTION);
        createEReference(stateDefinitionEClass, STATE_DEFINITION__STATE);

        stateSubactionMembershipEClass = createEClass(STATE_SUBACTION_MEMBERSHIP);
        createEAttribute(stateSubactionMembershipEClass, STATE_SUBACTION_MEMBERSHIP__KIND);
        createEReference(stateSubactionMembershipEClass, STATE_SUBACTION_MEMBERSHIP__ACTION);

        stateUsageEClass = createEClass(STATE_USAGE);
        createEAttribute(stateUsageEClass, STATE_USAGE__IS_PARALLEL);
        createEReference(stateUsageEClass, STATE_USAGE__DO_ACTION);
        createEReference(stateUsageEClass, STATE_USAGE__ENTRY_ACTION);
        createEReference(stateUsageEClass, STATE_USAGE__EXIT_ACTION);
        createEReference(stateUsageEClass, STATE_USAGE__STATE_DEFINITION);
        createEOperation(stateUsageEClass, STATE_USAGE___IS_SUBSTATE_USAGE__BOOLEAN);

        stepEClass = createEClass(STEP);
        createEReference(stepEClass, STEP__BEHAVIOR);
        createEReference(stepEClass, STEP__PARAMETER);

        structureEClass = createEClass(STRUCTURE);

        subclassificationEClass = createEClass(SUBCLASSIFICATION);
        createEReference(subclassificationEClass, SUBCLASSIFICATION__OWNING_CLASSIFIER);
        createEReference(subclassificationEClass, SUBCLASSIFICATION__SUBCLASSIFIER);
        createEReference(subclassificationEClass, SUBCLASSIFICATION__SUPERCLASSIFIER);

        subjectMembershipEClass = createEClass(SUBJECT_MEMBERSHIP);
        createEReference(subjectMembershipEClass, SUBJECT_MEMBERSHIP__OWNED_SUBJECT_PARAMETER);

        subsettingEClass = createEClass(SUBSETTING);
        createEReference(subsettingEClass, SUBSETTING__OWNING_FEATURE);
        createEReference(subsettingEClass, SUBSETTING__SUBSETTED_FEATURE);
        createEReference(subsettingEClass, SUBSETTING__SUBSETTING_FEATURE);

        successionEClass = createEClass(SUCCESSION);
        createEReference(successionEClass, SUCCESSION__EFFECT_STEP);
        createEReference(successionEClass, SUCCESSION__GUARD_EXPRESSION);
        createEReference(successionEClass, SUCCESSION__TRANSITION_STEP);
        createEReference(successionEClass, SUCCESSION__TRIGGER_STEP);

        successionAsUsageEClass = createEClass(SUCCESSION_AS_USAGE);

        successionFlowConnectionUsageEClass = createEClass(SUCCESSION_FLOW_CONNECTION_USAGE);

        successionItemFlowEClass = createEClass(SUCCESSION_ITEM_FLOW);

        textualRepresentationEClass = createEClass(TEXTUAL_REPRESENTATION);
        createEAttribute(textualRepresentationEClass, TEXTUAL_REPRESENTATION__BODY);
        createEAttribute(textualRepresentationEClass, TEXTUAL_REPRESENTATION__LANGUAGE);
        createEReference(textualRepresentationEClass, TEXTUAL_REPRESENTATION__REPRESENTED_ELEMENT);

        transitionFeatureMembershipEClass = createEClass(TRANSITION_FEATURE_MEMBERSHIP);
        createEAttribute(transitionFeatureMembershipEClass, TRANSITION_FEATURE_MEMBERSHIP__KIND);
        createEReference(transitionFeatureMembershipEClass, TRANSITION_FEATURE_MEMBERSHIP__TRANSITION_FEATURE);

        transitionUsageEClass = createEClass(TRANSITION_USAGE);
        createEReference(transitionUsageEClass, TRANSITION_USAGE__EFFECT_ACTION);
        createEReference(transitionUsageEClass, TRANSITION_USAGE__GUARD_EXPRESSION);
        createEReference(transitionUsageEClass, TRANSITION_USAGE__SOURCE);
        createEReference(transitionUsageEClass, TRANSITION_USAGE__SUCCESSION);
        createEReference(transitionUsageEClass, TRANSITION_USAGE__TARGET);
        createEReference(transitionUsageEClass, TRANSITION_USAGE__TRIGGER_ACTION);
        createEOperation(transitionUsageEClass, TRANSITION_USAGE___TRIGGER_PAYLOAD_PARAMETER);

        triggerInvocationExpressionEClass = createEClass(TRIGGER_INVOCATION_EXPRESSION);
        createEAttribute(triggerInvocationExpressionEClass, TRIGGER_INVOCATION_EXPRESSION__KIND);

        typeEClass = createEClass(TYPE);
        createEAttribute(typeEClass, TYPE__IS_ABSTRACT);
        createEAttribute(typeEClass, TYPE__IS_CONJUGATED);
        createEAttribute(typeEClass, TYPE__IS_SUFFICIENT);
        createEReference(typeEClass, TYPE__DIFFERENCING_TYPE);
        createEReference(typeEClass, TYPE__DIRECTED_FEATURE);
        createEReference(typeEClass, TYPE__END_FEATURE);
        createEReference(typeEClass, TYPE__FEATURE);
        createEReference(typeEClass, TYPE__FEATURE_MEMBERSHIP);
        createEReference(typeEClass, TYPE__INHERITED_FEATURE);
        createEReference(typeEClass, TYPE__INHERITED_MEMBERSHIP);
        createEReference(typeEClass, TYPE__INPUT);
        createEReference(typeEClass, TYPE__INTERSECTING_TYPE);
        createEReference(typeEClass, TYPE__MULTIPLICITY);
        createEReference(typeEClass, TYPE__OUTPUT);
        createEReference(typeEClass, TYPE__OWNED_CONJUGATOR);
        createEReference(typeEClass, TYPE__OWNED_DIFFERENCING);
        createEReference(typeEClass, TYPE__OWNED_DISJOINING);
        createEReference(typeEClass, TYPE__OWNED_END_FEATURE);
        createEReference(typeEClass, TYPE__OWNED_FEATURE);
        createEReference(typeEClass, TYPE__OWNED_FEATURE_MEMBERSHIP);
        createEReference(typeEClass, TYPE__OWNED_INTERSECTING);
        createEReference(typeEClass, TYPE__OWNED_SPECIALIZATION);
        createEReference(typeEClass, TYPE__OWNED_UNIONING);
        createEReference(typeEClass, TYPE__UNIONING_TYPE);
        createEOperation(typeEClass, TYPE___ALL_SUPERTYPES);
        createEOperation(typeEClass, TYPE___DIRECTION_OF__FEATURE);
        createEOperation(typeEClass, TYPE___INHERITED_MEMBERSHIPS__ELIST);
        createEOperation(typeEClass, TYPE___SPECIALIZES__TYPE);
        createEOperation(typeEClass, TYPE___SPECIALIZES_FROM_LIBRARY__STRING);

        typeFeaturingEClass = createEClass(TYPE_FEATURING);
        createEReference(typeFeaturingEClass, TYPE_FEATURING__FEATURE_OF_TYPE);
        createEReference(typeFeaturingEClass, TYPE_FEATURING__FEATURING_TYPE);
        createEReference(typeFeaturingEClass, TYPE_FEATURING__OWNING_FEATURE_OF_TYPE);

        unioningEClass = createEClass(UNIONING);
        createEReference(unioningEClass, UNIONING__TYPE_UNIONED);
        createEReference(unioningEClass, UNIONING__UNIONING_TYPE);

        usageEClass = createEClass(USAGE);
        createEAttribute(usageEClass, USAGE__IS_REFERENCE);
        createEAttribute(usageEClass, USAGE__IS_VARIATION);
        createEReference(usageEClass, USAGE__DEFINITION);
        createEReference(usageEClass, USAGE__DIRECTED_USAGE);
        createEReference(usageEClass, USAGE__NESTED_ACTION);
        createEReference(usageEClass, USAGE__NESTED_ALLOCATION);
        createEReference(usageEClass, USAGE__NESTED_ANALYSIS_CASE);
        createEReference(usageEClass, USAGE__NESTED_ATTRIBUTE);
        createEReference(usageEClass, USAGE__NESTED_CALCULATION);
        createEReference(usageEClass, USAGE__NESTED_CASE);
        createEReference(usageEClass, USAGE__NESTED_CONCERN);
        createEReference(usageEClass, USAGE__NESTED_CONNECTION);
        createEReference(usageEClass, USAGE__NESTED_CONSTRAINT);
        createEReference(usageEClass, USAGE__NESTED_ENUMERATION);
        createEReference(usageEClass, USAGE__NESTED_FLOW);
        createEReference(usageEClass, USAGE__NESTED_INTERFACE);
        createEReference(usageEClass, USAGE__NESTED_ITEM);
        createEReference(usageEClass, USAGE__NESTED_METADATA);
        createEReference(usageEClass, USAGE__NESTED_OCCURRENCE);
        createEReference(usageEClass, USAGE__NESTED_PART);
        createEReference(usageEClass, USAGE__NESTED_PORT);
        createEReference(usageEClass, USAGE__NESTED_REFERENCE);
        createEReference(usageEClass, USAGE__NESTED_RENDERING);
        createEReference(usageEClass, USAGE__NESTED_REQUIREMENT);
        createEReference(usageEClass, USAGE__NESTED_STATE);
        createEReference(usageEClass, USAGE__NESTED_TRANSITION);
        createEReference(usageEClass, USAGE__NESTED_USAGE);
        createEReference(usageEClass, USAGE__NESTED_USE_CASE);
        createEReference(usageEClass, USAGE__NESTED_VERIFICATION_CASE);
        createEReference(usageEClass, USAGE__NESTED_VIEW);
        createEReference(usageEClass, USAGE__NESTED_VIEWPOINT);
        createEReference(usageEClass, USAGE__OWNING_DEFINITION);
        createEReference(usageEClass, USAGE__OWNING_USAGE);
        createEReference(usageEClass, USAGE__USAGE);
        createEReference(usageEClass, USAGE__VARIANT);
        createEReference(usageEClass, USAGE__VARIANT_MEMBERSHIP);

        useCaseDefinitionEClass = createEClass(USE_CASE_DEFINITION);
        createEReference(useCaseDefinitionEClass, USE_CASE_DEFINITION__INCLUDED_USE_CASE);

        useCaseUsageEClass = createEClass(USE_CASE_USAGE);
        createEReference(useCaseUsageEClass, USE_CASE_USAGE__INCLUDED_USE_CASE);
        createEReference(useCaseUsageEClass, USE_CASE_USAGE__USE_CASE_DEFINITION);

        variantMembershipEClass = createEClass(VARIANT_MEMBERSHIP);
        createEReference(variantMembershipEClass, VARIANT_MEMBERSHIP__OWNED_VARIANT_USAGE);

        verificationCaseDefinitionEClass = createEClass(VERIFICATION_CASE_DEFINITION);
        createEReference(verificationCaseDefinitionEClass, VERIFICATION_CASE_DEFINITION__VERIFIED_REQUIREMENT);

        verificationCaseUsageEClass = createEClass(VERIFICATION_CASE_USAGE);
        createEReference(verificationCaseUsageEClass, VERIFICATION_CASE_USAGE__VERIFICATION_CASE_DEFINITION);
        createEReference(verificationCaseUsageEClass, VERIFICATION_CASE_USAGE__VERIFIED_REQUIREMENT);

        viewDefinitionEClass = createEClass(VIEW_DEFINITION);
        createEReference(viewDefinitionEClass, VIEW_DEFINITION__SATISFIED_VIEWPOINT);
        createEReference(viewDefinitionEClass, VIEW_DEFINITION__VIEW);
        createEReference(viewDefinitionEClass, VIEW_DEFINITION__VIEW_CONDITION);
        createEReference(viewDefinitionEClass, VIEW_DEFINITION__VIEW_RENDERING);

        viewpointDefinitionEClass = createEClass(VIEWPOINT_DEFINITION);
        createEReference(viewpointDefinitionEClass, VIEWPOINT_DEFINITION__VIEWPOINT_STAKEHOLDER);

        viewpointUsageEClass = createEClass(VIEWPOINT_USAGE);
        createEReference(viewpointUsageEClass, VIEWPOINT_USAGE__VIEWPOINT_DEFINITION);
        createEReference(viewpointUsageEClass, VIEWPOINT_USAGE__VIEWPOINT_STAKEHOLDER);

        viewRenderingMembershipEClass = createEClass(VIEW_RENDERING_MEMBERSHIP);
        createEReference(viewRenderingMembershipEClass, VIEW_RENDERING_MEMBERSHIP__OWNED_RENDERING);
        createEReference(viewRenderingMembershipEClass, VIEW_RENDERING_MEMBERSHIP__REFERENCED_RENDERING);

        viewUsageEClass = createEClass(VIEW_USAGE);
        createEReference(viewUsageEClass, VIEW_USAGE__EXPOSED_ELEMENT);
        createEReference(viewUsageEClass, VIEW_USAGE__SATISFIED_VIEWPOINT);
        createEReference(viewUsageEClass, VIEW_USAGE__VIEW_CONDITION);
        createEReference(viewUsageEClass, VIEW_USAGE__VIEW_DEFINITION);
        createEReference(viewUsageEClass, VIEW_USAGE__VIEW_RENDERING);
        createEOperation(viewUsageEClass, VIEW_USAGE___INCLUDE_AS_EXPOSED__ELEMENT);

        whileLoopActionUsageEClass = createEClass(WHILE_LOOP_ACTION_USAGE);
        createEReference(whileLoopActionUsageEClass, WHILE_LOOP_ACTION_USAGE__UNTIL_ARGUMENT);
        createEReference(whileLoopActionUsageEClass, WHILE_LOOP_ACTION_USAGE__WHILE_ARGUMENT);

        // Create enums
        featureDirectionKindEEnum = createEEnum(FEATURE_DIRECTION_KIND);
        portionKindEEnum = createEEnum(PORTION_KIND);
        requirementConstraintKindEEnum = createEEnum(REQUIREMENT_CONSTRAINT_KIND);
        stateSubactionKindEEnum = createEEnum(STATE_SUBACTION_KIND);
        transitionFeatureKindEEnum = createEEnum(TRANSITION_FEATURE_KIND);
        triggerKindEEnum = createEEnum(TRIGGER_KIND);
        visibilityKindEEnum = createEEnum(VISIBILITY_KIND);
    }
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model.  This
     * method is guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void initializePackageContents() {
        if (isInitialized) return;
        isInitialized = true;

        // Initialize package
        setName(eNAME);
        setNsPrefix(eNS_PREFIX);
        setNsURI(eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        acceptActionUsageEClass.getESuperTypes().add(this.getActionUsage());
        actionDefinitionEClass.getESuperTypes().add(this.getOccurrenceDefinition());
        actionDefinitionEClass.getESuperTypes().add(this.getBehavior());
        actionUsageEClass.getESuperTypes().add(this.getOccurrenceUsage());
        actionUsageEClass.getESuperTypes().add(this.getStep());
        actorMembershipEClass.getESuperTypes().add(this.getParameterMembership());
        allocationDefinitionEClass.getESuperTypes().add(this.getConnectionDefinition());
        allocationUsageEClass.getESuperTypes().add(this.getConnectionUsage());
        analysisCaseDefinitionEClass.getESuperTypes().add(this.getCaseDefinition());
        analysisCaseUsageEClass.getESuperTypes().add(this.getCaseUsage());
        annotatingElementEClass.getESuperTypes().add(this.getElement());
        annotationEClass.getESuperTypes().add(this.getRelationship());
        assertConstraintUsageEClass.getESuperTypes().add(this.getConstraintUsage());
        assertConstraintUsageEClass.getESuperTypes().add(this.getInvariant());
        assignmentActionUsageEClass.getESuperTypes().add(this.getActionUsage());
        associationEClass.getESuperTypes().add(this.getClassifier());
        associationEClass.getESuperTypes().add(this.getRelationship());
        associationStructureEClass.getESuperTypes().add(this.getAssociation());
        associationStructureEClass.getESuperTypes().add(this.getStructure());
        attributeDefinitionEClass.getESuperTypes().add(this.getDefinition());
        attributeDefinitionEClass.getESuperTypes().add(this.getDataType());
        attributeUsageEClass.getESuperTypes().add(this.getUsage());
        behaviorEClass.getESuperTypes().add(this.getClass_());
        bindingConnectorEClass.getESuperTypes().add(this.getConnector());
        bindingConnectorAsUsageEClass.getESuperTypes().add(this.getConnectorAsUsage());
        bindingConnectorAsUsageEClass.getESuperTypes().add(this.getBindingConnector());
        booleanExpressionEClass.getESuperTypes().add(this.getExpression());
        calculationDefinitionEClass.getESuperTypes().add(this.getActionDefinition());
        calculationDefinitionEClass.getESuperTypes().add(this.getFunction());
        calculationUsageEClass.getESuperTypes().add(this.getActionUsage());
        calculationUsageEClass.getESuperTypes().add(this.getExpression());
        caseDefinitionEClass.getESuperTypes().add(this.getCalculationDefinition());
        caseUsageEClass.getESuperTypes().add(this.getCalculationUsage());
        classEClass.getESuperTypes().add(this.getClassifier());
        classifierEClass.getESuperTypes().add(this.getType());
        collectExpressionEClass.getESuperTypes().add(this.getOperatorExpression());
        commentEClass.getESuperTypes().add(this.getAnnotatingElement());
        concernDefinitionEClass.getESuperTypes().add(this.getRequirementDefinition());
        concernUsageEClass.getESuperTypes().add(this.getRequirementUsage());
        conjugatedPortDefinitionEClass.getESuperTypes().add(this.getPortDefinition());
        conjugatedPortTypingEClass.getESuperTypes().add(this.getFeatureTyping());
        conjugationEClass.getESuperTypes().add(this.getRelationship());
        connectionDefinitionEClass.getESuperTypes().add(this.getPartDefinition());
        connectionDefinitionEClass.getESuperTypes().add(this.getAssociationStructure());
        connectionUsageEClass.getESuperTypes().add(this.getConnectorAsUsage());
        connectionUsageEClass.getESuperTypes().add(this.getPartUsage());
        connectorEClass.getESuperTypes().add(this.getFeature());
        connectorEClass.getESuperTypes().add(this.getRelationship());
        connectorAsUsageEClass.getESuperTypes().add(this.getUsage());
        connectorAsUsageEClass.getESuperTypes().add(this.getConnector());
        constraintDefinitionEClass.getESuperTypes().add(this.getOccurrenceDefinition());
        constraintDefinitionEClass.getESuperTypes().add(this.getPredicate());
        constraintUsageEClass.getESuperTypes().add(this.getOccurrenceUsage());
        constraintUsageEClass.getESuperTypes().add(this.getBooleanExpression());
        controlNodeEClass.getESuperTypes().add(this.getActionUsage());
        dataTypeEClass.getESuperTypes().add(this.getClassifier());
        decisionNodeEClass.getESuperTypes().add(this.getControlNode());
        definitionEClass.getESuperTypes().add(this.getClassifier());
        dependencyEClass.getESuperTypes().add(this.getRelationship());
        differencingEClass.getESuperTypes().add(this.getRelationship());
        disjoiningEClass.getESuperTypes().add(this.getRelationship());
        documentationEClass.getESuperTypes().add(this.getComment());
        elementFilterMembershipEClass.getESuperTypes().add(this.getOwningMembership());
        endFeatureMembershipEClass.getESuperTypes().add(this.getFeatureMembership());
        enumerationDefinitionEClass.getESuperTypes().add(this.getAttributeDefinition());
        enumerationUsageEClass.getESuperTypes().add(this.getAttributeUsage());
        eventOccurrenceUsageEClass.getESuperTypes().add(this.getOccurrenceUsage());
        exhibitStateUsageEClass.getESuperTypes().add(this.getStateUsage());
        exhibitStateUsageEClass.getESuperTypes().add(this.getPerformActionUsage());
        exposeEClass.getESuperTypes().add(this.getImport());
        expressionEClass.getESuperTypes().add(this.getStep());
        featureEClass.getESuperTypes().add(this.getType());
        featureChainExpressionEClass.getESuperTypes().add(this.getOperatorExpression());
        featureChainingEClass.getESuperTypes().add(this.getRelationship());
        featureInvertingEClass.getESuperTypes().add(this.getRelationship());
        featureMembershipEClass.getESuperTypes().add(this.getOwningMembership());
        featureMembershipEClass.getESuperTypes().add(this.getFeaturing());
        featureReferenceExpressionEClass.getESuperTypes().add(this.getExpression());
        featureTypingEClass.getESuperTypes().add(this.getSpecialization());
        featureValueEClass.getESuperTypes().add(this.getOwningMembership());
        featuringEClass.getESuperTypes().add(this.getRelationship());
        flowConnectionDefinitionEClass.getESuperTypes().add(this.getConnectionDefinition());
        flowConnectionDefinitionEClass.getESuperTypes().add(this.getActionDefinition());
        flowConnectionDefinitionEClass.getESuperTypes().add(this.getInteraction());
        flowConnectionUsageEClass.getESuperTypes().add(this.getConnectionUsage());
        flowConnectionUsageEClass.getESuperTypes().add(this.getActionUsage());
        flowConnectionUsageEClass.getESuperTypes().add(this.getItemFlow());
        forkNodeEClass.getESuperTypes().add(this.getControlNode());
        forLoopActionUsageEClass.getESuperTypes().add(this.getLoopActionUsage());
        framedConcernMembershipEClass.getESuperTypes().add(this.getRequirementConstraintMembership());
        functionEClass.getESuperTypes().add(this.getBehavior());
        ifActionUsageEClass.getESuperTypes().add(this.getActionUsage());
        importEClass.getESuperTypes().add(this.getRelationship());
        includeUseCaseUsageEClass.getESuperTypes().add(this.getUseCaseUsage());
        includeUseCaseUsageEClass.getESuperTypes().add(this.getPerformActionUsage());
        interactionEClass.getESuperTypes().add(this.getAssociation());
        interactionEClass.getESuperTypes().add(this.getBehavior());
        interfaceDefinitionEClass.getESuperTypes().add(this.getConnectionDefinition());
        interfaceUsageEClass.getESuperTypes().add(this.getConnectionUsage());
        intersectingEClass.getESuperTypes().add(this.getRelationship());
        invariantEClass.getESuperTypes().add(this.getBooleanExpression());
        invocationExpressionEClass.getESuperTypes().add(this.getExpression());
        itemDefinitionEClass.getESuperTypes().add(this.getOccurrenceDefinition());
        itemDefinitionEClass.getESuperTypes().add(this.getStructure());
        itemFeatureEClass.getESuperTypes().add(this.getFeature());
        itemFlowEClass.getESuperTypes().add(this.getConnector());
        itemFlowEClass.getESuperTypes().add(this.getStep());
        itemFlowEndEClass.getESuperTypes().add(this.getFeature());
        itemUsageEClass.getESuperTypes().add(this.getOccurrenceUsage());
        joinNodeEClass.getESuperTypes().add(this.getControlNode());
        libraryPackageEClass.getESuperTypes().add(this.getPackage());
        lifeClassEClass.getESuperTypes().add(this.getClass_());
        literalBooleanEClass.getESuperTypes().add(this.getLiteralExpression());
        literalExpressionEClass.getESuperTypes().add(this.getExpression());
        literalInfinityEClass.getESuperTypes().add(this.getLiteralExpression());
        literalIntegerEClass.getESuperTypes().add(this.getLiteralExpression());
        literalRationalEClass.getESuperTypes().add(this.getLiteralExpression());
        literalStringEClass.getESuperTypes().add(this.getLiteralExpression());
        loopActionUsageEClass.getESuperTypes().add(this.getActionUsage());
        membershipEClass.getESuperTypes().add(this.getRelationship());
        membershipExposeEClass.getESuperTypes().add(this.getMembershipImport());
        membershipExposeEClass.getESuperTypes().add(this.getExpose());
        membershipImportEClass.getESuperTypes().add(this.getImport());
        mergeNodeEClass.getESuperTypes().add(this.getControlNode());
        metaclassEClass.getESuperTypes().add(this.getStructure());
        metadataAccessExpressionEClass.getESuperTypes().add(this.getExpression());
        metadataDefinitionEClass.getESuperTypes().add(this.getItemDefinition());
        metadataDefinitionEClass.getESuperTypes().add(this.getMetaclass());
        metadataFeatureEClass.getESuperTypes().add(this.getFeature());
        metadataFeatureEClass.getESuperTypes().add(this.getAnnotatingElement());
        metadataUsageEClass.getESuperTypes().add(this.getItemUsage());
        metadataUsageEClass.getESuperTypes().add(this.getMetadataFeature());
        multiplicityEClass.getESuperTypes().add(this.getFeature());
        multiplicityRangeEClass.getESuperTypes().add(this.getMultiplicity());
        namespaceEClass.getESuperTypes().add(this.getElement());
        namespaceExposeEClass.getESuperTypes().add(this.getNamespaceImport());
        namespaceExposeEClass.getESuperTypes().add(this.getExpose());
        namespaceImportEClass.getESuperTypes().add(this.getImport());
        nullExpressionEClass.getESuperTypes().add(this.getExpression());
        objectiveMembershipEClass.getESuperTypes().add(this.getFeatureMembership());
        occurrenceDefinitionEClass.getESuperTypes().add(this.getDefinition());
        occurrenceDefinitionEClass.getESuperTypes().add(this.getClass_());
        occurrenceUsageEClass.getESuperTypes().add(this.getUsage());
        operatorExpressionEClass.getESuperTypes().add(this.getInvocationExpression());
        owningMembershipEClass.getESuperTypes().add(this.getMembership());
        packageEClass.getESuperTypes().add(this.getNamespace());
        parameterMembershipEClass.getESuperTypes().add(this.getFeatureMembership());
        partDefinitionEClass.getESuperTypes().add(this.getItemDefinition());
        partUsageEClass.getESuperTypes().add(this.getItemUsage());
        performActionUsageEClass.getESuperTypes().add(this.getActionUsage());
        performActionUsageEClass.getESuperTypes().add(this.getEventOccurrenceUsage());
        portConjugationEClass.getESuperTypes().add(this.getConjugation());
        portDefinitionEClass.getESuperTypes().add(this.getOccurrenceDefinition());
        portDefinitionEClass.getESuperTypes().add(this.getStructure());
        portUsageEClass.getESuperTypes().add(this.getOccurrenceUsage());
        predicateEClass.getESuperTypes().add(this.getFunction());
        redefinitionEClass.getESuperTypes().add(this.getSubsetting());
        referenceSubsettingEClass.getESuperTypes().add(this.getSubsetting());
        referenceUsageEClass.getESuperTypes().add(this.getUsage());
        relationshipEClass.getESuperTypes().add(this.getElement());
        renderingDefinitionEClass.getESuperTypes().add(this.getPartDefinition());
        renderingUsageEClass.getESuperTypes().add(this.getPartUsage());
        requirementConstraintMembershipEClass.getESuperTypes().add(this.getFeatureMembership());
        requirementDefinitionEClass.getESuperTypes().add(this.getConstraintDefinition());
        requirementUsageEClass.getESuperTypes().add(this.getConstraintUsage());
        requirementVerificationMembershipEClass.getESuperTypes().add(this.getRequirementConstraintMembership());
        resultExpressionMembershipEClass.getESuperTypes().add(this.getFeatureMembership());
        returnParameterMembershipEClass.getESuperTypes().add(this.getParameterMembership());
        satisfyRequirementUsageEClass.getESuperTypes().add(this.getRequirementUsage());
        satisfyRequirementUsageEClass.getESuperTypes().add(this.getAssertConstraintUsage());
        selectExpressionEClass.getESuperTypes().add(this.getOperatorExpression());
        sendActionUsageEClass.getESuperTypes().add(this.getActionUsage());
        specializationEClass.getESuperTypes().add(this.getRelationship());
        stakeholderMembershipEClass.getESuperTypes().add(this.getParameterMembership());
        stateDefinitionEClass.getESuperTypes().add(this.getActionDefinition());
        stateSubactionMembershipEClass.getESuperTypes().add(this.getFeatureMembership());
        stateUsageEClass.getESuperTypes().add(this.getActionUsage());
        stepEClass.getESuperTypes().add(this.getFeature());
        structureEClass.getESuperTypes().add(this.getClass_());
        subclassificationEClass.getESuperTypes().add(this.getSpecialization());
        subjectMembershipEClass.getESuperTypes().add(this.getParameterMembership());
        subsettingEClass.getESuperTypes().add(this.getSpecialization());
        successionEClass.getESuperTypes().add(this.getConnector());
        successionAsUsageEClass.getESuperTypes().add(this.getConnectorAsUsage());
        successionAsUsageEClass.getESuperTypes().add(this.getSuccession());
        successionFlowConnectionUsageEClass.getESuperTypes().add(this.getFlowConnectionUsage());
        successionFlowConnectionUsageEClass.getESuperTypes().add(this.getSuccessionItemFlow());
        successionItemFlowEClass.getESuperTypes().add(this.getItemFlow());
        successionItemFlowEClass.getESuperTypes().add(this.getSuccession());
        textualRepresentationEClass.getESuperTypes().add(this.getAnnotatingElement());
        transitionFeatureMembershipEClass.getESuperTypes().add(this.getFeatureMembership());
        transitionUsageEClass.getESuperTypes().add(this.getActionUsage());
        triggerInvocationExpressionEClass.getESuperTypes().add(this.getInvocationExpression());
        typeEClass.getESuperTypes().add(this.getNamespace());
        typeFeaturingEClass.getESuperTypes().add(this.getFeaturing());
        unioningEClass.getESuperTypes().add(this.getRelationship());
        usageEClass.getESuperTypes().add(this.getFeature());
        useCaseDefinitionEClass.getESuperTypes().add(this.getCaseDefinition());
        useCaseUsageEClass.getESuperTypes().add(this.getCaseUsage());
        variantMembershipEClass.getESuperTypes().add(this.getOwningMembership());
        verificationCaseDefinitionEClass.getESuperTypes().add(this.getCaseDefinition());
        verificationCaseUsageEClass.getESuperTypes().add(this.getCaseUsage());
        viewDefinitionEClass.getESuperTypes().add(this.getPartDefinition());
        viewpointDefinitionEClass.getESuperTypes().add(this.getRequirementDefinition());
        viewpointUsageEClass.getESuperTypes().add(this.getRequirementUsage());
        viewRenderingMembershipEClass.getESuperTypes().add(this.getFeatureMembership());
        viewUsageEClass.getESuperTypes().add(this.getPartUsage());
        whileLoopActionUsageEClass.getESuperTypes().add(this.getLoopActionUsage());

        // Initialize classes, features, and operations; add parameters
        initEClass(acceptActionUsageEClass, AcceptActionUsage.class, "AcceptActionUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAcceptActionUsage_PayloadArgument(), this.getExpression(), null, "payloadArgument", null, 0, 1, AcceptActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getAcceptActionUsage_PayloadParameter(), this.getReferenceUsage(), null, "payloadParameter", null, 1, 1, AcceptActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getAcceptActionUsage_ReceiverArgument(), this.getExpression(), null, "receiverArgument", null, 0, 1, AcceptActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEOperation(getAcceptActionUsage__IsTriggerAction(), ecorePackage.getEBoolean(), "isTriggerAction", 1, 1, IS_UNIQUE, !IS_ORDERED);

        initEClass(actionDefinitionEClass, ActionDefinition.class, "ActionDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getActionDefinition_Action(), this.getActionUsage(), null, "action", null, 0, -1, ActionDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(actionUsageEClass, ActionUsage.class, "ActionUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getActionUsage_ActionDefinition(), this.getBehavior(), null, "actionDefinition", null, 0, -1, ActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        EOperation op = initEOperation(getActionUsage__Argument__int(), this.getExpression(), "argument", 0, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, ecorePackage.getEInt(), "i", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = initEOperation(getActionUsage__InputParameter__int(), this.getFeature(), "inputParameter", 0, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, ecorePackage.getEInt(), "i", 1, 1, IS_UNIQUE, !IS_ORDERED);

        initEOperation(getActionUsage__InputParameters(), this.getFeature(), "inputParameters", 0, -1, IS_UNIQUE, !IS_ORDERED);

        initEOperation(getActionUsage__IsSubactionUsage(), ecorePackage.getEBoolean(), "isSubactionUsage", 1, 1, IS_UNIQUE, !IS_ORDERED);

        initEClass(actorMembershipEClass, ActorMembership.class, "ActorMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getActorMembership_OwnedActorParameter(), this.getPartUsage(), null, "ownedActorParameter", null, 1, 1, ActorMembership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(allocationDefinitionEClass, AllocationDefinition.class, "AllocationDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAllocationDefinition_Allocation(), this.getAllocationUsage(), null, "allocation", null, 0, -1, AllocationDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(allocationUsageEClass, AllocationUsage.class, "AllocationUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAllocationUsage_AllocationDefinition(), this.getAllocationDefinition(), null, "allocationDefinition", null, 0, -1, AllocationUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(analysisCaseDefinitionEClass, AnalysisCaseDefinition.class, "AnalysisCaseDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAnalysisCaseDefinition_AnalysisAction(), this.getActionUsage(), null, "analysisAction", null, 0, -1, AnalysisCaseDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getAnalysisCaseDefinition_ResultExpression(), this.getExpression(), null, "resultExpression", null, 0, 1, AnalysisCaseDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(analysisCaseUsageEClass, AnalysisCaseUsage.class, "AnalysisCaseUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAnalysisCaseUsage_AnalysisAction(), this.getActionUsage(), null, "analysisAction", null, 0, -1, AnalysisCaseUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getAnalysisCaseUsage_AnalysisCaseDefinition(), this.getAnalysisCaseDefinition(), null, "analysisCaseDefinition", null, 0, 1, AnalysisCaseUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getAnalysisCaseUsage_ResultExpression(), this.getExpression(), null, "resultExpression", null, 0, 1, AnalysisCaseUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(annotatingElementEClass, AnnotatingElement.class, "AnnotatingElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAnnotatingElement_AnnotatedElement(), this.getElement(), null, "annotatedElement", null, 1, -1, AnnotatingElement.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getAnnotatingElement_Annotation(), this.getAnnotation(), this.getAnnotation_AnnotatingElement(), "annotation", null, 0, -1, AnnotatingElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(annotationEClass, Annotation.class, "Annotation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAnnotation_AnnotatedElement(), this.getElement(), null, "annotatedElement", null, 1, 1, Annotation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getAnnotation_AnnotatingElement(), this.getAnnotatingElement(), this.getAnnotatingElement_Annotation(), "annotatingElement", null, 1, 1, Annotation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getAnnotation_OwningAnnotatedElement(), this.getElement(), this.getElement_OwnedAnnotation(), "owningAnnotatedElement", null, 0, 1, Annotation.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(assertConstraintUsageEClass, AssertConstraintUsage.class, "AssertConstraintUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAssertConstraintUsage_AssertedConstraint(), this.getConstraintUsage(), null, "assertedConstraint", null, 1, 1, AssertConstraintUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(assignmentActionUsageEClass, AssignmentActionUsage.class, "AssignmentActionUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAssignmentActionUsage_Referent(), this.getFeature(), null, "referent", null, 1, 1, AssignmentActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getAssignmentActionUsage_TargetArgument(), this.getExpression(), null, "targetArgument", null, 1, 1, AssignmentActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getAssignmentActionUsage_ValueExpression(), this.getExpression(), null, "valueExpression", null, 1, 1, AssignmentActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(associationEClass, Association.class, "Association", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAssociation_AssociationEnd(), this.getFeature(), null, "associationEnd", null, 0, -1, Association.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getAssociation_RelatedType(), this.getType(), null, "relatedType", null, 0, -1, Association.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getAssociation_SourceType(), this.getType(), null, "sourceType", null, 0, 1, Association.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getAssociation_TargetType(), this.getType(), null, "targetType", null, 0, -1, Association.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(associationStructureEClass, AssociationStructure.class, "AssociationStructure", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(attributeDefinitionEClass, AttributeDefinition.class, "AttributeDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(attributeUsageEClass, AttributeUsage.class, "AttributeUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getAttributeUsage_AttributeDefinition(), this.getDataType(), null, "attributeDefinition", null, 0, -1, AttributeUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(behaviorEClass, Behavior.class, "Behavior", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getBehavior_Parameter(), this.getFeature(), null, "parameter", null, 0, -1, Behavior.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getBehavior_Step(), this.getStep(), null, "step", null, 0, -1, Behavior.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(bindingConnectorEClass, BindingConnector.class, "BindingConnector", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(bindingConnectorAsUsageEClass, BindingConnectorAsUsage.class, "BindingConnectorAsUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(booleanExpressionEClass, BooleanExpression.class, "BooleanExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getBooleanExpression_Predicate(), this.getPredicate(), null, "predicate", null, 0, 1, BooleanExpression.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(calculationDefinitionEClass, CalculationDefinition.class, "CalculationDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCalculationDefinition_Calculation(), this.getCalculationUsage(), null, "calculation", null, 0, -1, CalculationDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(calculationUsageEClass, CalculationUsage.class, "CalculationUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCalculationUsage_CalculationDefinition(), this.getFunction(), null, "calculationDefinition", null, 0, 1, CalculationUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(caseDefinitionEClass, CaseDefinition.class, "CaseDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCaseDefinition_ActorParameter(), this.getPartUsage(), null, "actorParameter", null, 0, -1, CaseDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getCaseDefinition_ObjectiveRequirement(), this.getRequirementUsage(), null, "objectiveRequirement", null, 0, 1, CaseDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getCaseDefinition_SubjectParameter(), this.getUsage(), null, "subjectParameter", null, 1, 1, CaseDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(caseUsageEClass, CaseUsage.class, "CaseUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getCaseUsage_ActorParameter(), this.getPartUsage(), null, "actorParameter", null, 0, -1, CaseUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getCaseUsage_CaseDefinition(), this.getCaseDefinition(), null, "caseDefinition", null, 0, 1, CaseUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getCaseUsage_ObjectiveRequirement(), this.getRequirementUsage(), null, "objectiveRequirement", null, 0, 1, CaseUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getCaseUsage_SubjectParameter(), this.getUsage(), null, "subjectParameter", null, 1, 1, CaseUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(classEClass, org.eclipse.syson.sysml.Class.class, "Class", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(classifierEClass, Classifier.class, "Classifier", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getClassifier_OwnedSubclassification(), this.getSubclassification(), this.getSubclassification_OwningClassifier(), "ownedSubclassification", null, 0, -1, Classifier.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(collectExpressionEClass, CollectExpression.class, "CollectExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(commentEClass, Comment.class, "Comment", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getComment_Body(), ecorePackage.getEString(), "body", null, 1, 1, Comment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEAttribute(getComment_Locale(), ecorePackage.getEString(), "locale", null, 0, 1, Comment.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        initEClass(concernDefinitionEClass, ConcernDefinition.class, "ConcernDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(concernUsageEClass, ConcernUsage.class, "ConcernUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getConcernUsage_ConcernDefinition(), this.getConcernDefinition(), null, "concernDefinition", null, 0, 1, ConcernUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(conjugatedPortDefinitionEClass, ConjugatedPortDefinition.class, "ConjugatedPortDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getConjugatedPortDefinition_OriginalPortDefinition(), this.getPortDefinition(), this.getPortDefinition_ConjugatedPortDefinition(), "originalPortDefinition", null, 1, 1, ConjugatedPortDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getConjugatedPortDefinition_OwnedPortConjugator(), this.getPortConjugation(), this.getPortConjugation_ConjugatedPortDefinition(), "ownedPortConjugator", null, 1, 1, ConjugatedPortDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(conjugatedPortTypingEClass, ConjugatedPortTyping.class, "ConjugatedPortTyping", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getConjugatedPortTyping_ConjugatedPortDefinition(), this.getConjugatedPortDefinition(), null, "conjugatedPortDefinition", null, 1, 1, ConjugatedPortTyping.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getConjugatedPortTyping_PortDefinition(), this.getPortDefinition(), null, "portDefinition", null, 1, 1, ConjugatedPortTyping.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(conjugationEClass, Conjugation.class, "Conjugation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getConjugation_ConjugatedType(), this.getType(), null, "conjugatedType", null, 1, 1, Conjugation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getConjugation_OriginalType(), this.getType(), null, "originalType", null, 1, 1, Conjugation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getConjugation_OwningType(), this.getType(), this.getType_OwnedConjugator(), "owningType", null, 0, 1, Conjugation.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(connectionDefinitionEClass, ConnectionDefinition.class, "ConnectionDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getConnectionDefinition_ConnectionEnd(), this.getUsage(), null, "connectionEnd", null, 0, -1, ConnectionDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(connectionUsageEClass, ConnectionUsage.class, "ConnectionUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getConnectionUsage_ConnectionDefinition(), this.getAssociationStructure(), null, "connectionDefinition", null, 0, -1, ConnectionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(connectorEClass, Connector.class, "Connector", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getConnector_IsDirected(), ecorePackage.getEBoolean(), "isDirected", "false", 1, 1, Connector.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getConnector_Association(), this.getAssociation(), null, "association", null, 0, -1, Connector.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getConnector_ConnectorEnd(), this.getFeature(), null, "connectorEnd", null, 0, -1, Connector.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getConnector_RelatedFeature(), this.getFeature(), null, "relatedFeature", null, 0, -1, Connector.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getConnector_SourceFeature(), this.getFeature(), null, "sourceFeature", null, 0, 1, Connector.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getConnector_TargetFeature(), this.getFeature(), null, "targetFeature", null, 0, -1, Connector.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(connectorAsUsageEClass, ConnectorAsUsage.class, "ConnectorAsUsage", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(constraintDefinitionEClass, ConstraintDefinition.class, "ConstraintDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(constraintUsageEClass, ConstraintUsage.class, "ConstraintUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getConstraintUsage_ConstraintDefinition(), this.getPredicate(), null, "constraintDefinition", null, 0, 1, ConstraintUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(controlNodeEClass, ControlNode.class, "ControlNode", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        op = initEOperation(getControlNode__MultiplicityHasBounds__Multiplicity_int_int(), ecorePackage.getEBoolean(), "multiplicityHasBounds", 1, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, this.getMultiplicity(), "mult", 1, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, ecorePackage.getEInt(), "lower", 1, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, ecorePackage.getEInt(), "upper", 1, 1, IS_UNIQUE, !IS_ORDERED);

        initEClass(dataTypeEClass, DataType.class, "DataType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(decisionNodeEClass, DecisionNode.class, "DecisionNode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(definitionEClass, Definition.class, "Definition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDefinition_IsVariation(), ecorePackage.getEBoolean(), "isVariation", null, 1, 1, Definition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getDefinition_DirectedUsage(), this.getUsage(), null, "directedUsage", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDefinition_OwnedAction(), this.getActionUsage(), null, "ownedAction", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDefinition_OwnedAllocation(), this.getAllocationUsage(), null, "ownedAllocation", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDefinition_OwnedAnalysisCase(), this.getAnalysisCaseUsage(), null, "ownedAnalysisCase", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDefinition_OwnedAttribute(), this.getAttributeUsage(), null, "ownedAttribute", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDefinition_OwnedCalculation(), this.getCalculationUsage(), null, "ownedCalculation", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDefinition_OwnedCase(), this.getCaseUsage(), null, "ownedCase", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDefinition_OwnedConcern(), this.getConcernUsage(), null, "ownedConcern", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getDefinition_OwnedConnection(), this.getConnectorAsUsage(), null, "ownedConnection", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDefinition_OwnedConstraint(), this.getConstraintUsage(), null, "ownedConstraint", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDefinition_OwnedEnumeration(), this.getEnumerationUsage(), null, "ownedEnumeration", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDefinition_OwnedFlow(), this.getFlowConnectionUsage(), null, "ownedFlow", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getDefinition_OwnedInterface(), this.getInterfaceUsage(), null, "ownedInterface", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDefinition_OwnedItem(), this.getItemUsage(), null, "ownedItem", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDefinition_OwnedMetadata(), this.getMetadataUsage(), null, "ownedMetadata", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDefinition_OwnedOccurrence(), this.getOccurrenceUsage(), null, "ownedOccurrence", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDefinition_OwnedPart(), this.getPartUsage(), null, "ownedPart", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDefinition_OwnedPort(), this.getPortUsage(), null, "ownedPort", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDefinition_OwnedReference(), this.getReferenceUsage(), null, "ownedReference", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDefinition_OwnedRendering(), this.getRenderingUsage(), null, "ownedRendering", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDefinition_OwnedRequirement(), this.getRequirementUsage(), null, "ownedRequirement", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDefinition_OwnedState(), this.getStateUsage(), null, "ownedState", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDefinition_OwnedTransition(), this.getTransitionUsage(), null, "ownedTransition", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getDefinition_OwnedUsage(), this.getUsage(), this.getUsage_OwningDefinition(), "ownedUsage", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDefinition_OwnedUseCase(), this.getUseCaseUsage(), null, "ownedUseCase", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDefinition_OwnedVerificationCase(), this.getVerificationCaseUsage(), null, "ownedVerificationCase", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDefinition_OwnedView(), this.getViewUsage(), null, "ownedView", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDefinition_OwnedViewpoint(), this.getViewpointUsage(), null, "ownedViewpoint", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDefinition_Usage(), this.getUsage(), null, "usage", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getDefinition_Variant(), this.getUsage(), null, "variant", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getDefinition_VariantMembership(), this.getVariantMembership(), null, "variantMembership", null, 0, -1, Definition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(dependencyEClass, Dependency.class, "Dependency", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDependency_Client(), this.getElement(), null, "client", null, 1, -1, Dependency.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDependency_Supplier(), this.getElement(), null, "supplier", null, 1, -1, Dependency.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(differencingEClass, Differencing.class, "Differencing", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDifferencing_DifferencingType(), this.getType(), null, "differencingType", null, 1, 1, Differencing.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getDifferencing_TypeDifferenced(), this.getType(), this.getType_OwnedDifferencing(), "typeDifferenced", null, 1, 1, Differencing.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(disjoiningEClass, Disjoining.class, "Disjoining", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDisjoining_DisjoiningType(), this.getType(), null, "disjoiningType", null, 1, 1, Disjoining.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getDisjoining_OwningType(), this.getType(), this.getType_OwnedDisjoining(), "owningType", null, 0, 1, Disjoining.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getDisjoining_TypeDisjoined(), this.getType(), null, "typeDisjoined", null, 1, 1, Disjoining.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        initEClass(documentationEClass, Documentation.class, "Documentation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getDocumentation_DocumentedElement(), this.getElement(), this.getElement_Documentation(), "documentedElement", null, 1, 1, Documentation.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(elementEClass, Element.class, "Element", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getElement_AliasIds(), ecorePackage.getEString(), "aliasIds", null, 0, -1, Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getElement_DeclaredName(), ecorePackage.getEString(), "declaredName", null, 0, 1, Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEAttribute(getElement_DeclaredShortName(), ecorePackage.getEString(), "declaredShortName", null, 0, 1, Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEAttribute(getElement_ElementId(), ecorePackage.getEString(), "elementId", null, 1, 1, Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEAttribute(getElement_IsImpliedIncluded(), ecorePackage.getEBoolean(), "isImpliedIncluded", "false", 1, 1, Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEAttribute(getElement_IsLibraryElement(), ecorePackage.getEBoolean(), "isLibraryElement", null, 1, 1, Element.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEAttribute(getElement_Name(), ecorePackage.getEString(), "name", null, 0, 1, Element.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEAttribute(getElement_QualifiedName(), ecorePackage.getEString(), "qualifiedName", null, 0, 1, Element.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEAttribute(getElement_ShortName(), ecorePackage.getEString(), "shortName", null, 0, 1, Element.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getElement_Documentation(), this.getDocumentation(), this.getDocumentation_DocumentedElement(), "documentation", null, 0, -1, Element.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getElement_OwnedAnnotation(), this.getAnnotation(), this.getAnnotation_OwningAnnotatedElement(), "ownedAnnotation", null, 0, -1, Element.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getElement_OwnedElement(), this.getElement(), this.getElement_Owner(), "ownedElement", null, 0, -1, Element.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getElement_OwnedRelationship(), this.getRelationship(), this.getRelationship_OwningRelatedElement(), "ownedRelationship", null, 0, -1, Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getElement_Owner(), this.getElement(), this.getElement_OwnedElement(), "owner", null, 0, 1, Element.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getElement_OwningMembership(), this.getOwningMembership(), this.getOwningMembership_OwnedMemberElement(), "owningMembership", null, 0, 1, Element.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getElement_OwningNamespace(), this.getNamespace(), this.getNamespace_OwnedMember(), "owningNamespace", null, 0, 1, Element.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getElement_OwningRelationship(), this.getRelationship(), this.getRelationship_OwnedRelatedElement(), "owningRelationship", null, 0, 1, Element.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getElement_TextualRepresentation(), this.getTextualRepresentation(), this.getTextualRepresentation_RepresentedElement(), "textualRepresentation", null, 0, -1, Element.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEOperation(getElement__EffectiveName(), ecorePackage.getEString(), "effectiveName", 0, 1, IS_UNIQUE, !IS_ORDERED);

        initEOperation(getElement__EffectiveShortName(), ecorePackage.getEString(), "effectiveShortName", 0, 1, IS_UNIQUE, !IS_ORDERED);

        initEOperation(getElement__EscapedName(), ecorePackage.getEString(), "escapedName", 0, 1, IS_UNIQUE, !IS_ORDERED);

        initEOperation(getElement__LibraryNamespace(), this.getNamespace(), "libraryNamespace", 0, 1, IS_UNIQUE, !IS_ORDERED);

        initEClass(elementFilterMembershipEClass, ElementFilterMembership.class, "ElementFilterMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getElementFilterMembership_Condition(), this.getExpression(), null, "condition", null, 1, 1, ElementFilterMembership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(endFeatureMembershipEClass, EndFeatureMembership.class, "EndFeatureMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(enumerationDefinitionEClass, EnumerationDefinition.class, "EnumerationDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getEnumerationDefinition_EnumeratedValue(), this.getEnumerationUsage(), null, "enumeratedValue", null, 0, -1, EnumerationDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(enumerationUsageEClass, EnumerationUsage.class, "EnumerationUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getEnumerationUsage_EnumerationDefinition(), this.getEnumerationDefinition(), null, "enumerationDefinition", null, 1, 1, EnumerationUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(eventOccurrenceUsageEClass, EventOccurrenceUsage.class, "EventOccurrenceUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getEventOccurrenceUsage_EventOccurrence(), this.getOccurrenceUsage(), null, "eventOccurrence", null, 1, 1, EventOccurrenceUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(exhibitStateUsageEClass, ExhibitStateUsage.class, "ExhibitStateUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getExhibitStateUsage_ExhibitedState(), this.getStateUsage(), null, "exhibitedState", null, 1, 1, ExhibitStateUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(exposeEClass, Expose.class, "Expose", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(expressionEClass, Expression.class, "Expression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getExpression_IsModelLevelEvaluable(), ecorePackage.getEBoolean(), "isModelLevelEvaluable", null, 1, 1, Expression.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getExpression_Function(), this.getFunction(), null, "function", null, 0, 1, Expression.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getExpression_Result(), this.getFeature(), null, "result", null, 1, 1, Expression.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        op = initEOperation(getExpression__CheckCondition__Element(), ecorePackage.getEBoolean(), "checkCondition", 1, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, this.getElement(), "target", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = initEOperation(getExpression__Evaluate__Element(), this.getElement(), "evaluate", 0, -1, !IS_UNIQUE, IS_ORDERED);
        addEParameter(op, this.getElement(), "target", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = initEOperation(getExpression__ModelLevelEvaluable__EList(), ecorePackage.getEBoolean(), "modelLevelEvaluable", 1, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, this.getFeature(), "visited", 0, -1, IS_UNIQUE, !IS_ORDERED);

        initEClass(featureEClass, Feature.class, "Feature", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getFeature_Direction(), this.getFeatureDirectionKind(), "direction", null, 0, 1, Feature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEAttribute(getFeature_IsComposite(), ecorePackage.getEBoolean(), "isComposite", "false", 1, 1, Feature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEAttribute(getFeature_IsDerived(), ecorePackage.getEBoolean(), "isDerived", "false", 1, 1, Feature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEAttribute(getFeature_IsEnd(), ecorePackage.getEBoolean(), "isEnd", "false", 1, 1, Feature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEAttribute(getFeature_IsNonunique(), ecorePackage.getEBoolean(), "isNonunique", "false", 1, 1, Feature.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEAttribute(getFeature_IsOrdered(), ecorePackage.getEBoolean(), "isOrdered", "false", 1, 1, Feature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEAttribute(getFeature_IsPortion(), ecorePackage.getEBoolean(), "isPortion", "false", 1, 1, Feature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEAttribute(getFeature_IsReadOnly(), ecorePackage.getEBoolean(), "isReadOnly", "false", 1, 1, Feature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEAttribute(getFeature_IsUnique(), ecorePackage.getEBoolean(), "isUnique", "true", 1, 1, Feature.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getFeature_ChainingFeature(), this.getFeature(), null, "chainingFeature", null, 0, -1, Feature.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getFeature_EndOwningType(), this.getType(), this.getType_OwnedEndFeature(), "endOwningType", null, 0, 1, Feature.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getFeature_FeaturingType(), this.getType(), null, "featuringType", null, 0, -1, Feature.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getFeature_OwnedFeatureChaining(), this.getFeatureChaining(), this.getFeatureChaining_FeatureChained(), "ownedFeatureChaining", null, 0, -1, Feature.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getFeature_OwnedFeatureInverting(), this.getFeatureInverting(), this.getFeatureInverting_OwningFeature(), "ownedFeatureInverting", null, 0, -1, Feature.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getFeature_OwnedRedefinition(), this.getRedefinition(), null, "ownedRedefinition", null, 0, -1, Feature.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getFeature_OwnedReferenceSubsetting(), this.getReferenceSubsetting(), this.getReferenceSubsetting_ReferencingFeature(), "ownedReferenceSubsetting", null, 0, 1, Feature.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getFeature_OwnedSubsetting(), this.getSubsetting(), this.getSubsetting_OwningFeature(), "ownedSubsetting", null, 0, -1, Feature.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getFeature_OwnedTypeFeaturing(), this.getTypeFeaturing(), this.getTypeFeaturing_OwningFeatureOfType(), "ownedTypeFeaturing", null, 0, -1, Feature.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getFeature_OwnedTyping(), this.getFeatureTyping(), this.getFeatureTyping_OwningFeature(), "ownedTyping", null, 0, -1, Feature.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getFeature_OwningFeatureMembership(), this.getFeatureMembership(), this.getFeatureMembership_OwnedMemberFeature(), "owningFeatureMembership", null, 0, 1, Feature.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getFeature_OwningType(), this.getType(), this.getType_OwnedFeature(), "owningType", null, 0, 1, Feature.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getFeature_Type(), this.getType(), null, "type", null, 0, -1, Feature.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        op = initEOperation(getFeature__DirectionFor__Type(), this.getFeatureDirectionKind(), "directionFor", 0, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, this.getType(), "type", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = initEOperation(getFeature__IsFeaturedWithin__Type(), ecorePackage.getEBoolean(), "isFeaturedWithin", 1, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, this.getType(), "type", 0, 1, IS_UNIQUE, !IS_ORDERED);

        initEOperation(getFeature__NamingFeature(), this.getFeature(), "namingFeature", 0, 1, IS_UNIQUE, !IS_ORDERED);

        op = initEOperation(getFeature__Redefines__Feature(), ecorePackage.getEBoolean(), "redefines", 1, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, this.getFeature(), "redefinedFeature", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = initEOperation(getFeature__RedefinesFromLibrary__String(), ecorePackage.getEBoolean(), "redefinesFromLibrary", 1, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, ecorePackage.getEString(), "libraryFeatureName", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = initEOperation(getFeature__SubsetsChain__Feature_Feature(), ecorePackage.getEBoolean(), "subsetsChain", 1, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, this.getFeature(), "first", 1, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, this.getFeature(), "second", 1, 1, IS_UNIQUE, !IS_ORDERED);

        initEClass(featureChainExpressionEClass, FeatureChainExpression.class, "FeatureChainExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getFeatureChainExpression_TargetFeature(), this.getFeature(), null, "targetFeature", null, 1, 1, FeatureChainExpression.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEOperation(getFeatureChainExpression__SourceTargetFeature(), this.getFeature(), "sourceTargetFeature", 0, 1, IS_UNIQUE, !IS_ORDERED);

        initEClass(featureChainingEClass, FeatureChaining.class, "FeatureChaining", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getFeatureChaining_ChainingFeature(), this.getFeature(), null, "chainingFeature", null, 1, 1, FeatureChaining.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getFeatureChaining_FeatureChained(), this.getFeature(), this.getFeature_OwnedFeatureChaining(), "featureChained", null, 1, 1, FeatureChaining.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(featureInvertingEClass, FeatureInverting.class, "FeatureInverting", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getFeatureInverting_FeatureInverted(), this.getFeature(), null, "featureInverted", null, 1, 1, FeatureInverting.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getFeatureInverting_InvertingFeature(), this.getFeature(), null, "invertingFeature", null, 1, 1, FeatureInverting.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getFeatureInverting_OwningFeature(), this.getFeature(), this.getFeature_OwnedFeatureInverting(), "owningFeature", null, 0, 1, FeatureInverting.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(featureMembershipEClass, FeatureMembership.class, "FeatureMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getFeatureMembership_OwnedMemberFeature(), this.getFeature(), this.getFeature_OwningFeatureMembership(), "ownedMemberFeature", null, 1, 1, FeatureMembership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getFeatureMembership_OwningType(), this.getType(), this.getType_OwnedFeatureMembership(), "owningType", null, 1, 1, FeatureMembership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(featureReferenceExpressionEClass, FeatureReferenceExpression.class, "FeatureReferenceExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getFeatureReferenceExpression_Referent(), this.getFeature(), null, "referent", null, 1, 1, FeatureReferenceExpression.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(featureTypingEClass, FeatureTyping.class, "FeatureTyping", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getFeatureTyping_OwningFeature(), this.getFeature(), this.getFeature_OwnedTyping(), "owningFeature", null, 0, 1, FeatureTyping.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getFeatureTyping_Type(), this.getType(), null, "type", null, 1, 1, FeatureTyping.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getFeatureTyping_TypedFeature(), this.getFeature(), null, "typedFeature", null, 1, 1, FeatureTyping.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        initEClass(featureValueEClass, FeatureValue.class, "FeatureValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getFeatureValue_IsDefault(), ecorePackage.getEBoolean(), "isDefault", "false", 1, 1, FeatureValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEAttribute(getFeatureValue_IsInitial(), ecorePackage.getEBoolean(), "isInitial", "false", 1, 1, FeatureValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getFeatureValue_FeatureWithValue(), this.getFeature(), null, "featureWithValue", null, 1, 1, FeatureValue.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getFeatureValue_Value(), this.getExpression(), null, "value", null, 1, 1, FeatureValue.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(featuringEClass, Featuring.class, "Featuring", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getFeaturing_Feature(), this.getFeature(), null, "feature", null, 1, 1, Featuring.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getFeaturing_Type(), this.getType(), null, "type", null, 1, 1, Featuring.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        initEClass(flowConnectionDefinitionEClass, FlowConnectionDefinition.class, "FlowConnectionDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(flowConnectionUsageEClass, FlowConnectionUsage.class, "FlowConnectionUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getFlowConnectionUsage_FlowConnectionDefinition(), this.getInteraction(), null, "flowConnectionDefinition", null, 0, -1, FlowConnectionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(forkNodeEClass, ForkNode.class, "ForkNode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(forLoopActionUsageEClass, ForLoopActionUsage.class, "ForLoopActionUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getForLoopActionUsage_LoopVariable(), this.getReferenceUsage(), null, "loopVariable", null, 1, 1, ForLoopActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getForLoopActionUsage_SeqArgument(), this.getExpression(), null, "seqArgument", null, 1, 1, ForLoopActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(framedConcernMembershipEClass, FramedConcernMembership.class, "FramedConcernMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getFramedConcernMembership_OwnedConcern(), this.getConcernUsage(), null, "ownedConcern", null, 1, 1, FramedConcernMembership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getFramedConcernMembership_ReferencedConcern(), this.getConcernUsage(), null, "referencedConcern", null, 1, 1, FramedConcernMembership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(functionEClass, Function.class, "Function", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getFunction_IsModelLevelEvaluable(), ecorePackage.getEBoolean(), "isModelLevelEvaluable", null, 1, 1, Function.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getFunction_Expression(), this.getExpression(), null, "expression", null, 0, -1, Function.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getFunction_Result(), this.getFeature(), null, "result", null, 1, 1, Function.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(ifActionUsageEClass, IfActionUsage.class, "IfActionUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getIfActionUsage_ElseAction(), this.getActionUsage(), null, "elseAction", null, 0, 1, IfActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getIfActionUsage_IfArgument(), this.getExpression(), null, "ifArgument", null, 1, 1, IfActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getIfActionUsage_ThenAction(), this.getActionUsage(), null, "thenAction", null, 1, 1, IfActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(importEClass, Import.class, "Import", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getImport_IsImportAll(), ecorePackage.getEBoolean(), "isImportAll", "false", 1, 1, Import.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEAttribute(getImport_IsRecursive(), ecorePackage.getEBoolean(), "isRecursive", "false", 1, 1, Import.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEAttribute(getImport_Visibility(), this.getVisibilityKind(), "visibility", "public", 1, 1, Import.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getImport_ImportedElement(), this.getElement(), null, "importedElement", null, 1, 1, Import.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getImport_ImportOwningNamespace(), this.getNamespace(), this.getNamespace_OwnedImport(), "importOwningNamespace", null, 1, 1, Import.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        op = initEOperation(getImport__ImportedMemberships__EList(), this.getMembership(), "importedMemberships", 0, -1, IS_UNIQUE, IS_ORDERED);
        addEParameter(op, this.getNamespace(), "excluded", 0, -1, IS_UNIQUE, !IS_ORDERED);

        initEClass(includeUseCaseUsageEClass, IncludeUseCaseUsage.class, "IncludeUseCaseUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getIncludeUseCaseUsage_UseCaseIncluded(), this.getUseCaseUsage(), null, "useCaseIncluded", null, 1, 1, IncludeUseCaseUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(interactionEClass, Interaction.class, "Interaction", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(interfaceDefinitionEClass, InterfaceDefinition.class, "InterfaceDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getInterfaceDefinition_InterfaceEnd(), this.getPortUsage(), null, "interfaceEnd", null, 0, -1, InterfaceDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(interfaceUsageEClass, InterfaceUsage.class, "InterfaceUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getInterfaceUsage_InterfaceDefinition(), this.getInterfaceDefinition(), null, "interfaceDefinition", null, 0, -1, InterfaceUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(intersectingEClass, Intersecting.class, "Intersecting", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getIntersecting_IntersectingType(), this.getType(), null, "intersectingType", null, 1, 1, Intersecting.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getIntersecting_TypeIntersected(), this.getType(), this.getType_OwnedIntersecting(), "typeIntersected", null, 1, 1, Intersecting.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(invariantEClass, Invariant.class, "Invariant", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getInvariant_IsNegated(), ecorePackage.getEBoolean(), "isNegated", "false", 1, 1, Invariant.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        initEClass(invocationExpressionEClass, InvocationExpression.class, "InvocationExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getInvocationExpression_Argument(), this.getExpression(), null, "argument", null, 0, -1, InvocationExpression.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(itemDefinitionEClass, ItemDefinition.class, "ItemDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(itemFeatureEClass, ItemFeature.class, "ItemFeature", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(itemFlowEClass, ItemFlow.class, "ItemFlow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getItemFlow_Interaction(), this.getInteraction(), null, "interaction", null, 0, -1, ItemFlow.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getItemFlow_ItemFeature(), this.getItemFeature(), null, "itemFeature", null, 0, 1, ItemFlow.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getItemFlow_ItemFlowEnd(), this.getItemFlowEnd(), null, "itemFlowEnd", null, 0, 2, ItemFlow.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getItemFlow_ItemType(), this.getClassifier(), null, "itemType", null, 0, -1, ItemFlow.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getItemFlow_SourceOutputFeature(), this.getFeature(), null, "sourceOutputFeature", null, 0, 1, ItemFlow.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getItemFlow_TargetInputFeature(), this.getFeature(), null, "targetInputFeature", null, 0, 1, ItemFlow.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(itemFlowEndEClass, ItemFlowEnd.class, "ItemFlowEnd", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(itemUsageEClass, ItemUsage.class, "ItemUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getItemUsage_ItemDefinition(), this.getStructure(), null, "itemDefinition", null, 0, -1, ItemUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(joinNodeEClass, JoinNode.class, "JoinNode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(libraryPackageEClass, LibraryPackage.class, "LibraryPackage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getLibraryPackage_IsStandard(), ecorePackage.getEBoolean(), "isStandard", "false", 1, 1, LibraryPackage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        initEClass(lifeClassEClass, LifeClass.class, "LifeClass", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(literalBooleanEClass, LiteralBoolean.class, "LiteralBoolean", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getLiteralBoolean_Value(), ecorePackage.getEBoolean(), "value", null, 1, 1, LiteralBoolean.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        initEClass(literalExpressionEClass, LiteralExpression.class, "LiteralExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(literalInfinityEClass, LiteralInfinity.class, "LiteralInfinity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(literalIntegerEClass, LiteralInteger.class, "LiteralInteger", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getLiteralInteger_Value(), ecorePackage.getEInt(), "value", null, 1, 1, LiteralInteger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        initEClass(literalRationalEClass, LiteralRational.class, "LiteralRational", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getLiteralRational_Value(), ecorePackage.getEDouble(), "value", null, 1, 1, LiteralRational.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        initEClass(literalStringEClass, LiteralString.class, "LiteralString", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getLiteralString_Value(), ecorePackage.getEString(), "value", null, 1, 1, LiteralString.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        initEClass(loopActionUsageEClass, LoopActionUsage.class, "LoopActionUsage", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getLoopActionUsage_BodyAction(), this.getActionUsage(), null, "bodyAction", null, 1, 1, LoopActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(membershipEClass, Membership.class, "Membership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getMembership_MemberElementId(), ecorePackage.getEString(), "memberElementId", null, 1, 1, Membership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEAttribute(getMembership_MemberName(), ecorePackage.getEString(), "memberName", null, 0, 1, Membership.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEAttribute(getMembership_MemberShortName(), ecorePackage.getEString(), "memberShortName", null, 0, 1, Membership.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEAttribute(getMembership_Visibility(), this.getVisibilityKind(), "visibility", "public", 1, 1, Membership.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getMembership_MemberElement(), this.getElement(), null, "memberElement", null, 1, 1, Membership.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getMembership_MembershipOwningNamespace(), this.getNamespace(), this.getNamespace_OwnedMembership(), "membershipOwningNamespace", null, 1, 1, Membership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        op = initEOperation(getMembership__IsDistinguishableFrom__Membership(), ecorePackage.getEBoolean(), "isDistinguishableFrom", 1, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, this.getMembership(), "other", 1, 1, IS_UNIQUE, !IS_ORDERED);

        initEClass(membershipExposeEClass, MembershipExpose.class, "MembershipExpose", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(membershipImportEClass, MembershipImport.class, "MembershipImport", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getMembershipImport_ImportedMembership(), this.getMembership(), null, "importedMembership", null, 1, 1, MembershipImport.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        initEClass(mergeNodeEClass, MergeNode.class, "MergeNode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(metaclassEClass, Metaclass.class, "Metaclass", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(metadataAccessExpressionEClass, MetadataAccessExpression.class, "MetadataAccessExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getMetadataAccessExpression_ReferencedElement(), this.getElement(), null, "referencedElement", null, 1, 1, MetadataAccessExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        initEOperation(getMetadataAccessExpression__MetaclassFeature(), this.getMetadataFeature(), "metaclassFeature", 1, 1, IS_UNIQUE, !IS_ORDERED);

        initEClass(metadataDefinitionEClass, MetadataDefinition.class, "MetadataDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(metadataFeatureEClass, MetadataFeature.class, "MetadataFeature", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getMetadataFeature_Metaclass(), this.getMetaclass(), null, "metaclass", null, 0, 1, MetadataFeature.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        op = initEOperation(getMetadataFeature__EvaluateFeature__Feature(), this.getElement(), "evaluateFeature", 0, -1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, this.getFeature(), "baseFeature", 1, 1, IS_UNIQUE, !IS_ORDERED);

        initEOperation(getMetadataFeature__IsSemantic(), ecorePackage.getEBoolean(), "isSemantic", 1, 1, IS_UNIQUE, !IS_ORDERED);

        initEOperation(getMetadataFeature__IsSyntactic(), ecorePackage.getEBoolean(), "isSyntactic", 1, 1, IS_UNIQUE, !IS_ORDERED);

        initEOperation(getMetadataFeature__SyntaxElement(), this.getElement(), "syntaxElement", 0, 1, IS_UNIQUE, !IS_ORDERED);

        initEClass(metadataUsageEClass, MetadataUsage.class, "MetadataUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getMetadataUsage_MetadataDefinition(), this.getMetaclass(), null, "metadataDefinition", null, 0, 1, MetadataUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(multiplicityEClass, Multiplicity.class, "Multiplicity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(multiplicityRangeEClass, MultiplicityRange.class, "MultiplicityRange", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getMultiplicityRange_Bound(), this.getExpression(), null, "bound", null, 1, 2, MultiplicityRange.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getMultiplicityRange_LowerBound(), this.getExpression(), null, "lowerBound", null, 0, 1, MultiplicityRange.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getMultiplicityRange_UpperBound(), this.getExpression(), null, "upperBound", null, 1, 1, MultiplicityRange.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        op = initEOperation(getMultiplicityRange__HasBounds__int_int(), ecorePackage.getEBoolean(), "hasBounds", 1, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, ecorePackage.getEInt(), "lower", 1, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, ecorePackage.getEInt(), "upper", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = initEOperation(getMultiplicityRange__ValueOf__Expression(), ecorePackage.getEInt(), "valueOf", 0, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, this.getExpression(), "bound", 0, 1, IS_UNIQUE, !IS_ORDERED);

        initEClass(namespaceEClass, Namespace.class, "Namespace", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getNamespace_ImportedMembership(), this.getMembership(), null, "importedMembership", null, 0, -1, Namespace.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getNamespace_Member(), this.getElement(), null, "member", null, 0, -1, Namespace.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getNamespace_Membership(), this.getMembership(), null, "membership", null, 0, -1, Namespace.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getNamespace_OwnedImport(), this.getImport(), this.getImport_ImportOwningNamespace(), "ownedImport", null, 0, -1, Namespace.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getNamespace_OwnedMember(), this.getElement(), this.getElement_OwningNamespace(), "ownedMember", null, 0, -1, Namespace.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getNamespace_OwnedMembership(), this.getMembership(), this.getMembership_MembershipOwningNamespace(), "ownedMembership", null, 0, -1, Namespace.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        op = initEOperation(getNamespace__ImportedMemberships__EList(), this.getMembership(), "importedMemberships", 0, -1, IS_UNIQUE, IS_ORDERED);
        addEParameter(op, this.getNamespace(), "excluded", 0, -1, IS_UNIQUE, !IS_ORDERED);

        op = initEOperation(getNamespace__NamesOf__Element(), ecorePackage.getEString(), "namesOf", 0, -1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, this.getElement(), "element", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = initEOperation(getNamespace__QualificationOf__String(), ecorePackage.getEString(), "qualificationOf", 0, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, ecorePackage.getEString(), "qualifiedName", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = initEOperation(getNamespace__Resolve__String(), this.getMembership(), "resolve", 0, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, ecorePackage.getEString(), "qualifiedName", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = initEOperation(getNamespace__ResolveGlobal__String(), this.getMembership(), "resolveGlobal", 0, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, ecorePackage.getEString(), "qualifiedName", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = initEOperation(getNamespace__ResolveLocal__String(), this.getMembership(), "resolveLocal", 0, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, ecorePackage.getEString(), "name", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = initEOperation(getNamespace__ResolveVisible__String(), this.getMembership(), "resolveVisible", 0, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, ecorePackage.getEString(), "name", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = initEOperation(getNamespace__UnqualifiedNameOf__String(), ecorePackage.getEString(), "unqualifiedNameOf", 1, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, ecorePackage.getEString(), "qualifiedName", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = initEOperation(getNamespace__VisibilityOf__Membership(), this.getVisibilityKind(), "visibilityOf", 1, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, this.getMembership(), "mem", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = initEOperation(getNamespace__VisibleMemberships__EList_boolean_boolean(), this.getMembership(), "visibleMemberships", 0, -1, IS_UNIQUE, IS_ORDERED);
        addEParameter(op, this.getNamespace(), "excluded", 0, -1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, ecorePackage.getEBoolean(), "isRecursive", 1, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, ecorePackage.getEBoolean(), "includeAll", 1, 1, IS_UNIQUE, !IS_ORDERED);

        initEClass(namespaceExposeEClass, NamespaceExpose.class, "NamespaceExpose", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(namespaceImportEClass, NamespaceImport.class, "NamespaceImport", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getNamespaceImport_ImportedNamespace(), this.getNamespace(), null, "importedNamespace", null, 1, 1, NamespaceImport.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        initEClass(nullExpressionEClass, NullExpression.class, "NullExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(objectiveMembershipEClass, ObjectiveMembership.class, "ObjectiveMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getObjectiveMembership_OwnedObjectiveRequirement(), this.getRequirementUsage(), null, "ownedObjectiveRequirement", null, 1, 1, ObjectiveMembership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(occurrenceDefinitionEClass, OccurrenceDefinition.class, "OccurrenceDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getOccurrenceDefinition_IsIndividual(), ecorePackage.getEBoolean(), "isIndividual", "false", 1, 1, OccurrenceDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getOccurrenceDefinition_LifeClass(), this.getLifeClass(), null, "lifeClass", null, 0, 1, OccurrenceDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(occurrenceUsageEClass, OccurrenceUsage.class, "OccurrenceUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getOccurrenceUsage_IsIndividual(), ecorePackage.getEBoolean(), "isIndividual", "false", 1, 1, OccurrenceUsage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEAttribute(getOccurrenceUsage_PortionKind(), this.getPortionKind(), "portionKind", null, 0, 1, OccurrenceUsage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getOccurrenceUsage_IndividualDefinition(), this.getOccurrenceDefinition(), null, "individualDefinition", null, 0, 1, OccurrenceUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getOccurrenceUsage_OccurrenceDefinition(), this.getClass_(), null, "occurrenceDefinition", null, 0, -1, OccurrenceUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(operatorExpressionEClass, OperatorExpression.class, "OperatorExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getOperatorExpression_Operator(), ecorePackage.getEString(), "operator", null, 1, 1, OperatorExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getOperatorExpression_Operand(), this.getExpression(), null, "operand", null, 0, -1, OperatorExpression.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(owningMembershipEClass, OwningMembership.class, "OwningMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getOwningMembership_OwnedMemberElementId(), ecorePackage.getEString(), "ownedMemberElementId", null, 1, 1, OwningMembership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEAttribute(getOwningMembership_OwnedMemberName(), ecorePackage.getEString(), "ownedMemberName", null, 0, 1, OwningMembership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEAttribute(getOwningMembership_OwnedMemberShortName(), ecorePackage.getEString(), "ownedMemberShortName", null, 0, 1, OwningMembership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getOwningMembership_OwnedMemberElement(), this.getElement(), this.getElement_OwningMembership(), "ownedMemberElement", null, 1, 1, OwningMembership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(packageEClass, org.eclipse.syson.sysml.Package.class, "Package", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getPackage_FilterCondition(), this.getExpression(), null, "filterCondition", null, 0, -1, org.eclipse.syson.sysml.Package.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        op = initEOperation(getPackage__IncludeAsMember__Element(), ecorePackage.getEBoolean(), "includeAsMember", 1, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, this.getElement(), "element", 1, 1, IS_UNIQUE, !IS_ORDERED);

        initEClass(parameterMembershipEClass, ParameterMembership.class, "ParameterMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getParameterMembership_OwnedMemberParameter(), this.getFeature(), null, "ownedMemberParameter", null, 1, 1, ParameterMembership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(partDefinitionEClass, PartDefinition.class, "PartDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(partUsageEClass, PartUsage.class, "PartUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getPartUsage_PartDefinition(), this.getPartDefinition(), null, "partDefinition", null, 0, -1, PartUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(performActionUsageEClass, PerformActionUsage.class, "PerformActionUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getPerformActionUsage_PerformedAction(), this.getActionUsage(), null, "performedAction", null, 1, 1, PerformActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(portConjugationEClass, PortConjugation.class, "PortConjugation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getPortConjugation_ConjugatedPortDefinition(), this.getConjugatedPortDefinition(), this.getConjugatedPortDefinition_OwnedPortConjugator(), "conjugatedPortDefinition", null, 1, 1, PortConjugation.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getPortConjugation_OriginalPortDefinition(), this.getPortDefinition(), null, "originalPortDefinition", null, 1, 1, PortConjugation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        initEClass(portDefinitionEClass, PortDefinition.class, "PortDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getPortDefinition_ConjugatedPortDefinition(), this.getConjugatedPortDefinition(), this.getConjugatedPortDefinition_OriginalPortDefinition(), "conjugatedPortDefinition", null, 0, 1, PortDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(portUsageEClass, PortUsage.class, "PortUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getPortUsage_PortDefinition(), this.getPortDefinition(), null, "portDefinition", null, 0, -1, PortUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(predicateEClass, Predicate.class, "Predicate", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(redefinitionEClass, Redefinition.class, "Redefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getRedefinition_RedefinedFeature(), this.getFeature(), null, "redefinedFeature", null, 1, 1, Redefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getRedefinition_RedefiningFeature(), this.getFeature(), null, "redefiningFeature", null, 1, 1, Redefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        initEClass(referenceSubsettingEClass, ReferenceSubsetting.class, "ReferenceSubsetting", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getReferenceSubsetting_ReferencedFeature(), this.getFeature(), null, "referencedFeature", null, 1, 1, ReferenceSubsetting.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getReferenceSubsetting_ReferencingFeature(), this.getFeature(), this.getFeature_OwnedReferenceSubsetting(), "referencingFeature", null, 1, 1, ReferenceSubsetting.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(referenceUsageEClass, ReferenceUsage.class, "ReferenceUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(relationshipEClass, Relationship.class, "Relationship", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getRelationship_IsImplied(), ecorePackage.getEBoolean(), "isImplied", "false", 1, 1, Relationship.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getRelationship_OwnedRelatedElement(), this.getElement(), this.getElement_OwningRelationship(), "ownedRelatedElement", null, 0, -1, Relationship.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getRelationship_OwningRelatedElement(), this.getElement(), this.getElement_OwnedRelationship(), "owningRelatedElement", null, 0, 1, Relationship.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getRelationship_RelatedElement(), this.getElement(), null, "relatedElement", null, 0, -1, Relationship.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getRelationship_Source(), this.getElement(), null, "source", null, 0, -1, Relationship.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getRelationship_Target(), this.getElement(), null, "target", null, 0, -1, Relationship.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(renderingDefinitionEClass, RenderingDefinition.class, "RenderingDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getRenderingDefinition_Rendering(), this.getRenderingUsage(), null, "rendering", null, 0, -1, RenderingDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(renderingUsageEClass, RenderingUsage.class, "RenderingUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getRenderingUsage_RenderingDefinition(), this.getRenderingDefinition(), null, "renderingDefinition", null, 0, 1, RenderingUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(requirementConstraintMembershipEClass, RequirementConstraintMembership.class, "RequirementConstraintMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getRequirementConstraintMembership_Kind(), this.getRequirementConstraintKind(), "kind", null, 1, 1, RequirementConstraintMembership.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getRequirementConstraintMembership_OwnedConstraint(), this.getConstraintUsage(), null, "ownedConstraint", null, 1, 1, RequirementConstraintMembership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getRequirementConstraintMembership_ReferencedConstraint(), this.getConstraintUsage(), null, "referencedConstraint", null, 1, 1, RequirementConstraintMembership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(requirementDefinitionEClass, RequirementDefinition.class, "RequirementDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getRequirementDefinition_ReqId(), ecorePackage.getEString(), "reqId", null, 0, 1, RequirementDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEAttribute(getRequirementDefinition_Text(), ecorePackage.getEString(), "text", null, 0, -1, RequirementDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getRequirementDefinition_ActorParameter(), this.getPartUsage(), null, "actorParameter", null, 0, -1, RequirementDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getRequirementDefinition_AssumedConstraint(), this.getConstraintUsage(), null, "assumedConstraint", null, 0, -1, RequirementDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getRequirementDefinition_FramedConcern(), this.getConcernUsage(), null, "framedConcern", null, 0, -1, RequirementDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getRequirementDefinition_RequiredConstraint(), this.getConstraintUsage(), null, "requiredConstraint", null, 0, -1, RequirementDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getRequirementDefinition_StakeholderParameter(), this.getPartUsage(), null, "stakeholderParameter", null, 0, -1, RequirementDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getRequirementDefinition_SubjectParameter(), this.getUsage(), null, "subjectParameter", null, 1, 1, RequirementDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(requirementUsageEClass, RequirementUsage.class, "RequirementUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getRequirementUsage_ReqId(), ecorePackage.getEString(), "reqId", null, 0, 1, RequirementUsage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEAttribute(getRequirementUsage_Text(), ecorePackage.getEString(), "text", null, 0, -1, RequirementUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getRequirementUsage_ActorParameter(), this.getPartUsage(), null, "actorParameter", null, 0, -1, RequirementUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getRequirementUsage_AssumedConstraint(), this.getConstraintUsage(), null, "assumedConstraint", null, 0, -1, RequirementUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getRequirementUsage_FramedConcern(), this.getConcernUsage(), null, "framedConcern", null, 0, -1, RequirementUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getRequirementUsage_RequiredConstraint(), this.getConstraintUsage(), null, "requiredConstraint", null, 0, -1, RequirementUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getRequirementUsage_RequirementDefinition(), this.getRequirementDefinition(), null, "requirementDefinition", null, 0, 1, RequirementUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getRequirementUsage_StakeholderParameter(), this.getPartUsage(), null, "stakeholderParameter", null, 0, -1, RequirementUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getRequirementUsage_SubjectParameter(), this.getUsage(), null, "subjectParameter", null, 1, 1, RequirementUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(requirementVerificationMembershipEClass, RequirementVerificationMembership.class, "RequirementVerificationMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getRequirementVerificationMembership_OwnedRequirement(), this.getRequirementUsage(), null, "ownedRequirement", null, 1, 1, RequirementVerificationMembership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getRequirementVerificationMembership_VerifiedRequirement(), this.getRequirementUsage(), null, "verifiedRequirement", null, 1, 1, RequirementVerificationMembership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(resultExpressionMembershipEClass, ResultExpressionMembership.class, "ResultExpressionMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getResultExpressionMembership_OwnedResultExpression(), this.getExpression(), null, "ownedResultExpression", null, 1, 1, ResultExpressionMembership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(returnParameterMembershipEClass, ReturnParameterMembership.class, "ReturnParameterMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(satisfyRequirementUsageEClass, SatisfyRequirementUsage.class, "SatisfyRequirementUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSatisfyRequirementUsage_SatisfiedRequirement(), this.getRequirementUsage(), null, "satisfiedRequirement", null, 1, 1, SatisfyRequirementUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getSatisfyRequirementUsage_SatisfyingFeature(), this.getFeature(), null, "satisfyingFeature", null, 1, 1, SatisfyRequirementUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(selectExpressionEClass, SelectExpression.class, "SelectExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(sendActionUsageEClass, SendActionUsage.class, "SendActionUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSendActionUsage_PayloadArgument(), this.getExpression(), null, "payloadArgument", null, 1, 1, SendActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getSendActionUsage_ReceiverArgument(), this.getExpression(), null, "receiverArgument", null, 0, 1, SendActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getSendActionUsage_SenderArgument(), this.getExpression(), null, "senderArgument", null, 0, 1, SendActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(specializationEClass, Specialization.class, "Specialization", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSpecialization_General(), this.getType(), null, "general", null, 1, 1, Specialization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getSpecialization_OwningType(), this.getType(), this.getType_OwnedSpecialization(), "owningType", null, 0, 1, Specialization.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getSpecialization_Specific(), this.getType(), null, "specific", null, 1, 1, Specialization.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        initEClass(stakeholderMembershipEClass, StakeholderMembership.class, "StakeholderMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getStakeholderMembership_OwnedStakeholderParameter(), this.getPartUsage(), null, "ownedStakeholderParameter", null, 1, 1, StakeholderMembership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(stateDefinitionEClass, StateDefinition.class, "StateDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getStateDefinition_IsParallel(), ecorePackage.getEBoolean(), "isParallel", "false", 1, 1, StateDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getStateDefinition_DoAction(), this.getActionUsage(), null, "doAction", null, 0, 1, StateDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getStateDefinition_EntryAction(), this.getActionUsage(), null, "entryAction", null, 0, 1, StateDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getStateDefinition_ExitAction(), this.getActionUsage(), null, "exitAction", null, 0, 1, StateDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getStateDefinition_State(), this.getStateUsage(), null, "state", null, 0, -1, StateDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(stateSubactionMembershipEClass, StateSubactionMembership.class, "StateSubactionMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getStateSubactionMembership_Kind(), this.getStateSubactionKind(), "kind", null, 1, 1, StateSubactionMembership.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getStateSubactionMembership_Action(), this.getActionUsage(), null, "action", null, 1, 1, StateSubactionMembership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(stateUsageEClass, StateUsage.class, "StateUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getStateUsage_IsParallel(), ecorePackage.getEBoolean(), "isParallel", "false", 1, 1, StateUsage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getStateUsage_DoAction(), this.getActionUsage(), null, "doAction", null, 0, 1, StateUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getStateUsage_EntryAction(), this.getActionUsage(), null, "entryAction", null, 0, 1, StateUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getStateUsage_ExitAction(), this.getActionUsage(), null, "exitAction", null, 0, 1, StateUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getStateUsage_StateDefinition(), this.getBehavior(), null, "stateDefinition", null, 0, -1, StateUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        op = initEOperation(getStateUsage__IsSubstateUsage__boolean(), ecorePackage.getEBoolean(), "isSubstateUsage", 1, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, ecorePackage.getEBoolean(), "isParallel", 1, 1, IS_UNIQUE, !IS_ORDERED);

        initEClass(stepEClass, Step.class, "Step", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getStep_Behavior(), this.getBehavior(), null, "behavior", null, 0, -1, Step.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getStep_Parameter(), this.getFeature(), null, "parameter", null, 0, -1, Step.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(structureEClass, Structure.class, "Structure", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(subclassificationEClass, Subclassification.class, "Subclassification", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSubclassification_OwningClassifier(), this.getClassifier(), this.getClassifier_OwnedSubclassification(), "owningClassifier", null, 0, 1, Subclassification.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getSubclassification_Subclassifier(), this.getClassifier(), null, "subclassifier", null, 1, 1, Subclassification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getSubclassification_Superclassifier(), this.getClassifier(), null, "superclassifier", null, 1, 1, Subclassification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        initEClass(subjectMembershipEClass, SubjectMembership.class, "SubjectMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSubjectMembership_OwnedSubjectParameter(), this.getUsage(), null, "ownedSubjectParameter", null, 1, 1, SubjectMembership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(subsettingEClass, Subsetting.class, "Subsetting", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSubsetting_OwningFeature(), this.getFeature(), this.getFeature_OwnedSubsetting(), "owningFeature", null, 1, 1, Subsetting.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getSubsetting_SubsettedFeature(), this.getFeature(), null, "subsettedFeature", null, 1, 1, Subsetting.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getSubsetting_SubsettingFeature(), this.getFeature(), null, "subsettingFeature", null, 1, 1, Subsetting.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        initEClass(successionEClass, Succession.class, "Succession", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSuccession_EffectStep(), this.getStep(), null, "effectStep", null, 0, -1, Succession.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getSuccession_GuardExpression(), this.getExpression(), null, "guardExpression", null, 0, -1, Succession.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getSuccession_TransitionStep(), this.getStep(), null, "transitionStep", null, 0, 1, Succession.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getSuccession_TriggerStep(), this.getStep(), null, "triggerStep", null, 0, -1, Succession.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(successionAsUsageEClass, SuccessionAsUsage.class, "SuccessionAsUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(successionFlowConnectionUsageEClass, SuccessionFlowConnectionUsage.class, "SuccessionFlowConnectionUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(successionItemFlowEClass, SuccessionItemFlow.class, "SuccessionItemFlow", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(textualRepresentationEClass, TextualRepresentation.class, "TextualRepresentation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTextualRepresentation_Body(), ecorePackage.getEString(), "body", null, 1, 1, TextualRepresentation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEAttribute(getTextualRepresentation_Language(), ecorePackage.getEString(), "language", null, 1, 1, TextualRepresentation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getTextualRepresentation_RepresentedElement(), this.getElement(), this.getElement_TextualRepresentation(), "representedElement", null, 1, 1, TextualRepresentation.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(transitionFeatureMembershipEClass, TransitionFeatureMembership.class, "TransitionFeatureMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTransitionFeatureMembership_Kind(), this.getTransitionFeatureKind(), "kind", null, 1, 1, TransitionFeatureMembership.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getTransitionFeatureMembership_TransitionFeature(), this.getStep(), null, "transitionFeature", null, 1, 1, TransitionFeatureMembership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(transitionUsageEClass, TransitionUsage.class, "TransitionUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTransitionUsage_EffectAction(), this.getActionUsage(), null, "effectAction", null, 0, -1, TransitionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getTransitionUsage_GuardExpression(), this.getExpression(), null, "guardExpression", null, 0, -1, TransitionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getTransitionUsage_Source(), this.getActionUsage(), null, "source", null, 1, 1, TransitionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getTransitionUsage_Succession(), this.getSuccession(), null, "succession", null, 1, 1, TransitionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getTransitionUsage_Target(), this.getActionUsage(), null, "target", null, 1, 1, TransitionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getTransitionUsage_TriggerAction(), this.getAcceptActionUsage(), null, "triggerAction", null, 0, -1, TransitionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEOperation(getTransitionUsage__TriggerPayloadParameter(), this.getReferenceUsage(), "triggerPayloadParameter", 0, 1, IS_UNIQUE, !IS_ORDERED);

        initEClass(triggerInvocationExpressionEClass, TriggerInvocationExpression.class, "TriggerInvocationExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getTriggerInvocationExpression_Kind(), this.getTriggerKind(), "kind", null, 1, 1, TriggerInvocationExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        initEClass(typeEClass, Type.class, "Type", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getType_IsAbstract(), ecorePackage.getEBoolean(), "isAbstract", "false", 1, 1, Type.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEAttribute(getType_IsConjugated(), ecorePackage.getEBoolean(), "isConjugated", null, 1, 1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEAttribute(getType_IsSufficient(), ecorePackage.getEBoolean(), "isSufficient", "false", 1, 1, Type.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getType_DifferencingType(), this.getType(), null, "differencingType", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getType_DirectedFeature(), this.getFeature(), null, "directedFeature", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getType_EndFeature(), this.getFeature(), null, "endFeature", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getType_Feature(), this.getFeature(), null, "feature", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getType_FeatureMembership(), this.getFeatureMembership(), null, "featureMembership", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getType_InheritedFeature(), this.getFeature(), null, "inheritedFeature", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getType_InheritedMembership(), this.getMembership(), null, "inheritedMembership", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getType_Input(), this.getFeature(), null, "input", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getType_IntersectingType(), this.getType(), null, "intersectingType", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getType_Multiplicity(), this.getMultiplicity(), null, "multiplicity", null, 0, 1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getType_Output(), this.getFeature(), null, "output", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getType_OwnedConjugator(), this.getConjugation(), this.getConjugation_OwningType(), "ownedConjugator", null, 0, 1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getType_OwnedDifferencing(), this.getDifferencing(), this.getDifferencing_TypeDifferenced(), "ownedDifferencing", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getType_OwnedDisjoining(), this.getDisjoining(), this.getDisjoining_OwningType(), "ownedDisjoining", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getType_OwnedEndFeature(), this.getFeature(), this.getFeature_EndOwningType(), "ownedEndFeature", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getType_OwnedFeature(), this.getFeature(), this.getFeature_OwningType(), "ownedFeature", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getType_OwnedFeatureMembership(), this.getFeatureMembership(), this.getFeatureMembership_OwningType(), "ownedFeatureMembership", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getType_OwnedIntersecting(), this.getIntersecting(), this.getIntersecting_TypeIntersected(), "ownedIntersecting", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getType_OwnedSpecialization(), this.getSpecialization(), this.getSpecialization_OwningType(), "ownedSpecialization", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getType_OwnedUnioning(), this.getUnioning(), this.getUnioning_TypeUnioned(), "ownedUnioning", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getType_UnioningType(), this.getType(), null, "unioningType", null, 0, -1, Type.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEOperation(getType__AllSupertypes(), this.getType(), "allSupertypes", 0, -1, IS_UNIQUE, !IS_ORDERED);

        op = initEOperation(getType__DirectionOf__Feature(), this.getFeatureDirectionKind(), "directionOf", 0, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, this.getFeature(), "feature", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = initEOperation(getType__InheritedMemberships__EList(), this.getMembership(), "inheritedMemberships", 0, -1, IS_UNIQUE, IS_ORDERED);
        addEParameter(op, this.getType(), "excluded", 0, -1, IS_UNIQUE, !IS_ORDERED);

        op = initEOperation(getType__Specializes__Type(), ecorePackage.getEBoolean(), "specializes", 1, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, this.getType(), "supertype", 1, 1, IS_UNIQUE, !IS_ORDERED);

        op = initEOperation(getType__SpecializesFromLibrary__String(), ecorePackage.getEBoolean(), "specializesFromLibrary", 1, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, ecorePackage.getEString(), "libraryTypeName", 1, 1, IS_UNIQUE, !IS_ORDERED);

        initEClass(typeFeaturingEClass, TypeFeaturing.class, "TypeFeaturing", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getTypeFeaturing_FeatureOfType(), this.getFeature(), null, "featureOfType", null, 1, 1, TypeFeaturing.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getTypeFeaturing_FeaturingType(), this.getType(), null, "featuringType", null, 1, 1, TypeFeaturing.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getTypeFeaturing_OwningFeatureOfType(), this.getFeature(), this.getFeature_OwnedTypeFeaturing(), "owningFeatureOfType", null, 0, 1, TypeFeaturing.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(unioningEClass, Unioning.class, "Unioning", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getUnioning_TypeUnioned(), this.getType(), this.getType_OwnedUnioning(), "typeUnioned", null, 1, 1, Unioning.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getUnioning_UnioningType(), this.getType(), null, "unioningType", null, 1, 1, Unioning.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

        initEClass(usageEClass, Usage.class, "Usage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getUsage_IsReference(), ecorePackage.getEBoolean(), "isReference", null, 1, 1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEAttribute(getUsage_IsVariation(), ecorePackage.getEBoolean(), "isVariation", null, 1, 1, Usage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
        initEReference(getUsage_Definition(), this.getClassifier(), null, "definition", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUsage_DirectedUsage(), this.getUsage(), null, "directedUsage", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUsage_NestedAction(), this.getActionUsage(), null, "nestedAction", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUsage_NestedAllocation(), this.getAllocationUsage(), null, "nestedAllocation", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUsage_NestedAnalysisCase(), this.getAnalysisCaseUsage(), null, "nestedAnalysisCase", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUsage_NestedAttribute(), this.getAttributeUsage(), null, "nestedAttribute", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUsage_NestedCalculation(), this.getCalculationUsage(), null, "nestedCalculation", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUsage_NestedCase(), this.getCaseUsage(), null, "nestedCase", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUsage_NestedConcern(), this.getConcernUsage(), null, "nestedConcern", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getUsage_NestedConnection(), this.getConnectorAsUsage(), null, "nestedConnection", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUsage_NestedConstraint(), this.getConstraintUsage(), null, "nestedConstraint", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUsage_NestedEnumeration(), this.getEnumerationUsage(), null, "nestedEnumeration", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUsage_NestedFlow(), this.getFlowConnectionUsage(), null, "nestedFlow", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getUsage_NestedInterface(), this.getInterfaceUsage(), null, "nestedInterface", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUsage_NestedItem(), this.getItemUsage(), null, "nestedItem", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUsage_NestedMetadata(), this.getMetadataUsage(), null, "nestedMetadata", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUsage_NestedOccurrence(), this.getOccurrenceUsage(), null, "nestedOccurrence", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUsage_NestedPart(), this.getPartUsage(), null, "nestedPart", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUsage_NestedPort(), this.getPortUsage(), null, "nestedPort", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUsage_NestedReference(), this.getReferenceUsage(), null, "nestedReference", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUsage_NestedRendering(), this.getRenderingUsage(), null, "nestedRendering", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUsage_NestedRequirement(), this.getRequirementUsage(), null, "nestedRequirement", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUsage_NestedState(), this.getStateUsage(), null, "nestedState", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUsage_NestedTransition(), this.getTransitionUsage(), null, "nestedTransition", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getUsage_NestedUsage(), this.getUsage(), this.getUsage_OwningUsage(), "nestedUsage", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUsage_NestedUseCase(), this.getUseCaseUsage(), null, "nestedUseCase", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUsage_NestedVerificationCase(), this.getVerificationCaseUsage(), null, "nestedVerificationCase", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUsage_NestedView(), this.getViewUsage(), null, "nestedView", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUsage_NestedViewpoint(), this.getViewpointUsage(), null, "nestedViewpoint", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUsage_OwningDefinition(), this.getDefinition(), this.getDefinition_OwnedUsage(), "owningDefinition", null, 0, 1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getUsage_OwningUsage(), this.getUsage(), this.getUsage_NestedUsage(), "owningUsage", null, 0, 1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getUsage_Usage(), this.getUsage(), null, "usage", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUsage_Variant(), this.getUsage(), null, "variant", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getUsage_VariantMembership(), this.getVariantMembership(), null, "variantMembership", null, 0, -1, Usage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(useCaseDefinitionEClass, UseCaseDefinition.class, "UseCaseDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getUseCaseDefinition_IncludedUseCase(), this.getUseCaseUsage(), null, "includedUseCase", null, 0, -1, UseCaseDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(useCaseUsageEClass, UseCaseUsage.class, "UseCaseUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getUseCaseUsage_IncludedUseCase(), this.getUseCaseUsage(), null, "includedUseCase", null, 0, -1, UseCaseUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getUseCaseUsage_UseCaseDefinition(), this.getUseCaseDefinition(), null, "useCaseDefinition", null, 0, 1, UseCaseUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(variantMembershipEClass, VariantMembership.class, "VariantMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getVariantMembership_OwnedVariantUsage(), this.getUsage(), null, "ownedVariantUsage", null, 1, 1, VariantMembership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(verificationCaseDefinitionEClass, VerificationCaseDefinition.class, "VerificationCaseDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getVerificationCaseDefinition_VerifiedRequirement(), this.getRequirementUsage(), null, "verifiedRequirement", null, 0, -1, VerificationCaseDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(verificationCaseUsageEClass, VerificationCaseUsage.class, "VerificationCaseUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getVerificationCaseUsage_VerificationCaseDefinition(), this.getVerificationCaseDefinition(), null, "verificationCaseDefinition", null, 0, 1, VerificationCaseUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getVerificationCaseUsage_VerifiedRequirement(), this.getRequirementUsage(), null, "verifiedRequirement", null, 0, -1, VerificationCaseUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(viewDefinitionEClass, ViewDefinition.class, "ViewDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getViewDefinition_SatisfiedViewpoint(), this.getViewpointUsage(), null, "satisfiedViewpoint", null, 0, -1, ViewDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getViewDefinition_View(), this.getViewUsage(), null, "view", null, 0, -1, ViewDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getViewDefinition_ViewCondition(), this.getExpression(), null, "viewCondition", null, 0, -1, ViewDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getViewDefinition_ViewRendering(), this.getRenderingUsage(), null, "viewRendering", null, 0, 1, ViewDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(viewpointDefinitionEClass, ViewpointDefinition.class, "ViewpointDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getViewpointDefinition_ViewpointStakeholder(), this.getPartUsage(), null, "viewpointStakeholder", null, 0, -1, ViewpointDefinition.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(viewpointUsageEClass, ViewpointUsage.class, "ViewpointUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getViewpointUsage_ViewpointDefinition(), this.getViewpointDefinition(), null, "viewpointDefinition", null, 0, 1, ViewpointUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getViewpointUsage_ViewpointStakeholder(), this.getPartUsage(), null, "viewpointStakeholder", null, 0, -1, ViewpointUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        initEClass(viewRenderingMembershipEClass, ViewRenderingMembership.class, "ViewRenderingMembership", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getViewRenderingMembership_OwnedRendering(), this.getRenderingUsage(), null, "ownedRendering", null, 1, 1, ViewRenderingMembership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getViewRenderingMembership_ReferencedRendering(), this.getRenderingUsage(), null, "referencedRendering", null, 1, 1, ViewRenderingMembership.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        initEClass(viewUsageEClass, ViewUsage.class, "ViewUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getViewUsage_ExposedElement(), this.getElement(), null, "exposedElement", null, 0, -1, ViewUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getViewUsage_SatisfiedViewpoint(), this.getViewpointUsage(), null, "satisfiedViewpoint", null, 0, -1, ViewUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getViewUsage_ViewCondition(), this.getExpression(), null, "viewCondition", null, 0, -1, ViewUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
        initEReference(getViewUsage_ViewDefinition(), this.getViewDefinition(), null, "viewDefinition", null, 0, 1, ViewUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getViewUsage_ViewRendering(), this.getRenderingUsage(), null, "viewRendering", null, 0, 1, ViewUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        op = initEOperation(getViewUsage__IncludeAsExposed__Element(), ecorePackage.getEBoolean(), "includeAsExposed", 1, 1, IS_UNIQUE, !IS_ORDERED);
        addEParameter(op, this.getElement(), "element", 1, 1, IS_UNIQUE, !IS_ORDERED);

        initEClass(whileLoopActionUsageEClass, WhileLoopActionUsage.class, "WhileLoopActionUsage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getWhileLoopActionUsage_UntilArgument(), this.getExpression(), null, "untilArgument", null, 0, 1, WhileLoopActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);
        initEReference(getWhileLoopActionUsage_WhileArgument(), this.getExpression(), null, "whileArgument", null, 1, 1, WhileLoopActionUsage.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, !IS_ORDERED);

        // Initialize enums and add enum literals
        initEEnum(featureDirectionKindEEnum, FeatureDirectionKind.class, "FeatureDirectionKind");
        addEEnumLiteral(featureDirectionKindEEnum, FeatureDirectionKind.IN);
        addEEnumLiteral(featureDirectionKindEEnum, FeatureDirectionKind.INOUT);
        addEEnumLiteral(featureDirectionKindEEnum, FeatureDirectionKind.OUT);

        initEEnum(portionKindEEnum, PortionKind.class, "PortionKind");
        addEEnumLiteral(portionKindEEnum, PortionKind.SNAPSHOT);
        addEEnumLiteral(portionKindEEnum, PortionKind.TIMESLICE);

        initEEnum(requirementConstraintKindEEnum, RequirementConstraintKind.class, "RequirementConstraintKind");
        addEEnumLiteral(requirementConstraintKindEEnum, RequirementConstraintKind.ASSUMPTION);
        addEEnumLiteral(requirementConstraintKindEEnum, RequirementConstraintKind.REQUIREMENT);

        initEEnum(stateSubactionKindEEnum, StateSubactionKind.class, "StateSubactionKind");
        addEEnumLiteral(stateSubactionKindEEnum, StateSubactionKind.DO);
        addEEnumLiteral(stateSubactionKindEEnum, StateSubactionKind.ENTRY);
        addEEnumLiteral(stateSubactionKindEEnum, StateSubactionKind.EXIT);

        initEEnum(transitionFeatureKindEEnum, TransitionFeatureKind.class, "TransitionFeatureKind");
        addEEnumLiteral(transitionFeatureKindEEnum, TransitionFeatureKind.EFFECT);
        addEEnumLiteral(transitionFeatureKindEEnum, TransitionFeatureKind.GUARD);
        addEEnumLiteral(transitionFeatureKindEEnum, TransitionFeatureKind.TRIGGER);

        initEEnum(triggerKindEEnum, TriggerKind.class, "TriggerKind");
        addEEnumLiteral(triggerKindEEnum, TriggerKind.AFTER);
        addEEnumLiteral(triggerKindEEnum, TriggerKind.AT);
        addEEnumLiteral(triggerKindEEnum, TriggerKind.WHEN);

        initEEnum(visibilityKindEEnum, VisibilityKind.class, "VisibilityKind");
        addEEnumLiteral(visibilityKindEEnum, VisibilityKind.PRIVATE);
        addEEnumLiteral(visibilityKindEEnum, VisibilityKind.PROTECTED);
        addEEnumLiteral(visibilityKindEEnum, VisibilityKind.PUBLIC);

        // Create resource
        createResource(eNS_URI);
    }

} //SysmlPackageImpl
