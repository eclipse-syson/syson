/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
package org.eclipse.syson.sysml.parser.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Aggregates tests for the SysML parser.
 *
 * @author <a href="mailto:yvan.lussaud@obeo.fr">Yvan Lussaud</a>
 */
@RunWith(Suite.class)
@SuiteClasses(value = { AcceptNodeTests.class, ActionBodyParameterMemberTests.class, ActionBodyParameterTests.class, ActionDefinitionTests.class, ActionNodeMemberTests.class, ActionNodeTests.class,
                        ActionTargetSuccessionTests.class, ActionUsageTests.class, ActorMemberTests.class, ActorUsageTests.class, AdditiveExpressionTests.class, AliasMemberTests.class,
                        AllocationDefinitionTests.class, AllocationUsageTests.class, AnalysisCaseDefinitionTests.class, AnalysisCaseUsageTests.class, AndExpressionTests.class,
                        AnnotatingElementTests.class,
                        AnnotatingMemberTests.class, AnnotationTests.class, ArgumentExpressionMemberTests.class, ArgumentExpressionTests.class, ArgumentExpressionValueTests.class,
                        ArgumentMemberTests.class,
                        ArgumentTests.class, ArgumentValueTests.class, AssertConstraintUsageTests.class, AssignmentNodeTests.class, AssignmentTargetMemberTests.class, AttributeDefinitionTests.class,
                        AttributeUsageTests.class, BaseExpressionTests.class, BehaviorUsageElementTests.class, BehaviorUsageMemberTests.class, BindingConnectorAsUsageTests.class,
                        BodyExpressionTests.class,
                        BodyParameterMemberTests.class, BodyParameterTests.class, CalculationDefinitionTests.class, CalculationUsageTests.class, CaseDefinitionTests.class, CaseUsageTests.class,
                        ClassificationExpressionTests.class, CommentTests.class, ConcernDefinitionTests.class, ConcernUsageTests.class, ConditionalExpressionTests.class,
                        ConjugatedPortDefinitionMemberTests.class,
                        ConjugatedPortDefinitionTests.class, ConjugatedPortTypingTests.class, ConnectionDefinitionTests.class, ConnectionUsageTests.class, ConnectorEndMemberTests.class,
                        ConnectorEndTests.class,
                        ConstraintDefinitionTests.class, ConstraintUsageTests.class, ConstructorExpressionTests.class, ConstructorResultMemberTests.class, ConstructorResultTests.class,
                        ControlNodeTests.class,
                        DecisionNodeTests.class, DefaultInterfaceEndTests.class, DefaultReferenceUsageTests.class, DefaultTargetSuccessionTests.class, DefinitionElementTests.class,
                        DefinitionMemberTests.class,
                        DependencyTests.class, DoActionMemberTests.class, DocumentationTests.class, EffectBehaviorMemberTests.class, EffectBehaviorUsageTests.class, ElementFilterMemberTests.class,
                        ElementReferenceMemberTests.class, EmptyActionUsageTests.class, EmptyFeatureTests.class, EmptyMultiplicityMemberTests.class, EmptyMultiplicityTests.class,
                        EmptyParameterMemberTests.class,
                        EmptySourceEndMemberTests.class, EmptySourceEndTests.class, EmptySuccessionMemberTests.class, EmptySuccessionTests.class, EmptyTargetEndMemberTests.class,
                        EmptyTargetEndTests.class,
                        EmptyUsageTests.class, EntryActionMemberTests.class, EntryTransitionMemberTests.class, EnumeratedValueTests.class, EnumerationDefinitionTests.class,
                        EnumerationUsageMemberTests.class,
                        EnumerationUsageTests.class, EqualityExpressionMemberTests.class, EqualityExpressionReferenceTests.class, EqualityExpressionTests.class, EventOccurrenceUsageTests.class,
                        ExhibitStateUsageTests.class, ExitActionMemberTests.class, ExponentiationExpressionTests.class, ExposeTests.class, ExpressionBodyMemberTests.class, ExpressionBodyTests.class,
                        ExpressionParameterMemberTests.class, ExtendedDefinitionTests.class, ExtendedUsageTests.class, ExtentExpressionTests.class, FeatureBindingTests.class,
                        FeatureChainMemberTests.class,
                        FeatureChainPrefixTests.class, FeatureReferenceExpressionTests.class, FeatureReferenceMemberTests.class, FeatureTypingTests.class, FeatureValueTests.class,
                        FilterPackageImportTests.class,
                        FilterPackageMembershipImportTests.class, FilterPackageMemberTests.class, FilterPackageNamespaceImportTests.class, FilterPackageTests.class, FlowDefinitionTests.class,
                        FlowEndMemberTests.class, FlowEndSubsettingTests.class, FlowEndTests.class, FlowFeatureMemberTests.class, FlowFeatureTests.class, FlowRedefinitionTests.class,
                        FlowUsageTests.class,
                        ForkNodeTests.class, ForLoopNodeTests.class, ForVariableDeclarationMemberTests.class, ForVariableDeclarationTests.class, FramedConcernMemberTests.class,
                        FramedConcernUsageTests.class,
                        FunctionReferenceExpressionTests.class, FunctionReferenceMemberTests.class, FunctionReferenceTests.class, GuardedSuccessionMemberTests.class, GuardedSuccessionTests.class,
                        GuardedTargetSuccessionTests.class, GuardExpressionMemberTests.class, IfNodeParameterMemberTests.class, IfNodeTests.class, ImpliesExpressionMemberTests.class,
                        ImpliesExpressionReferenceTests.class, ImpliesExpressionTests.class, ImportTests.class, IncludeUseCaseUsageTests.class, IndividualDefinitionTests.class,
                        IndividualUsageTests.class,
                        InitialNodeMemberTests.class, InstantiatedTypeMemberTests.class, InterfaceDefinitionTests.class, InterfaceEndMemberTests.class, InterfaceEndTests.class,
                        InterfaceNonOccurrenceUsageElementTests.class, InterfaceNonOccurrenceUsageMemberTests.class, InterfaceOccurrenceUsageElementTests.class,
                        InterfaceOccurrenceUsageMemberTests.class,
                        InterfaceUsageTests.class, InvocationExpressionTests.class, ItemDefinitionTests.class, ItemUsageTests.class, JoinNodeTests.class, LibraryPackageTests.class,
                        LiteralBooleanTests.class,
                        LiteralExpressionTests.class, LiteralInfinityTests.class, LiteralIntegerTests.class, LiteralRealTests.class, LiteralStringTests.class, NumericLiteralFormatAdapterTests.class,
                        MembershipExposeTests.class, MembershipImportTests.class,
                        MergeNodeTests.class, MessageEventMemberTests.class, MessageEventTests.class, MessageTests.class, MetadataAccessExpressionTests.class, MetadataBodyUsageMemberTests.class,
                        MetadataBodyUsageTests.class, MetadataDefinitionTests.class, MetadataReferenceTests.class, MetadataTypingTests.class, MetadataUsageTests.class,
                        MultiplicativeExpressionTests.class,
                        MultiplicityExpressionMemberTests.class, MultiplicityRangeTests.class, MultiplicitySourceEndMemberTests.class, MultiplicitySourceEndTests.class, NamedArgumentMemberTests.class,
                        NamedArgumentTests.class, NamespaceExposeTests.class, NamespaceImportTests.class, NodeParameterMemberTests.class, NodeParameterTests.class,
                        NonOccurrenceUsageElementTests.class,
                        NonOccurrenceUsageMemberTests.class, NullCoalescingExpressionTests.class, NullExpressionTests.class, ObjectiveMemberTests.class, ObjectiveRequirementUsageTests.class,
                        OccurrenceDefinitionTests.class, OccurrenceUsageElementTests.class, OccurrenceUsageMemberTests.class, OccurrenceUsageTests.class, OrExpressionMemberTests.class,
                        OrExpressionReferenceTests.class, OrExpressionTests.class, OwnedAnnotationTests.class, OwnedCrossFeatureMemberTests.class, OwnedCrossFeatureTests.class,
                        OwnedCrossMultiplicityMemberTests.class, OwnedCrossMultiplicityTests.class, OwnedCrossSubsettingTests.class, OwnedExpressionMemberTests.class,
                        OwnedExpressionReferenceTests.class,
                        OwnedExpressionTests.class, OwnedFeatureChainingTests.class, OwnedFeatureChainTests.class, OwnedFeatureTypingTests.class, OwnedMultiplicityTests.class,
                        OwnedRedefinitionTests.class,
                        OwnedReferenceSubsettingTests.class, OwnedSubclassificationTests.class, OwnedSubsettingTests.class, PackageMemberTests.class, PackageTests.class,
                        ParameterRedefinitionTests.class,
                        PartDefinitionTests.class, PartUsageTests.class, PayloadFeatureMemberTests.class, PayloadFeatureTests.class, PayloadParameterMemberTests.class, PayloadParameterTests.class,
                        PerformActionUsageTests.class, PerformedActionUsageTests.class, PortConjugationTests.class, PortDefinitionTests.class, PortionUsageTests.class, PortUsageTests.class,
                        PrefixMetadataAnnotationTests.class, PrefixMetadataMemberTests.class, PrefixMetadataUsageTests.class, PrimaryExpressionTests.class, RangeExpressionTests.class,
                        ReferenceTypingTests.class,
                        ReferenceUsageTests.class, RelationalExpressionTests.class, RenderingDefinitionTests.class, RenderingUsageTests.class, RequirementConstraintMemberTests.class,
                        RequirementConstraintUsageTests.class, RequirementDefinitionTests.class, RequirementUsageTests.class, RequirementVerificationMemberTests.class,
                        RequirementVerificationUsageTests.class,
                        ResultExpressionMemberTests.class, ReturnParameterMemberTests.class, RootNamespaceRecoveryTests.class, RootNamespaceTests.class, SatisfactionFeatureValueTests.class, SatisfactionParameterTests.class,
                        SatisfactionReferenceExpressionTests.class, SatisfactionSubjectMemberTests.class, SatisfyRequirementUsageTests.class, SelfReferenceExpressionTests.class,
                        SelfReferenceMemberTests.class,
                        SendNodeTests.class, SequenceExpressionTests.class, StakeholderMemberTests.class, StakeholderUsageTests.class, StateActionUsageTests.class, StateDefinitionTests.class,
                        StateUsageTests.class,
                        SysMLParsingFactoryTests.class, ReturnParameterDirectionTests.class, SourceSyntaxPreservationTests.class,
                        StructureUsageElementTests.class, StructureUsageMemberTests.class, SubjectMemberTests.class, SubjectUsageTests.class, SuccessionAsUsageTests.class,
                        SuccessionFlowUsageTests.class,
                        TargetAccessedFeatureMemberTests.class, TargetBindingTests.class, TargetExpressionTests.class, TargetFeatureMemberTests.class, TargetFeatureTests.class,
                        TargetParameterTests.class,
                        TargetSuccessionMemberTests.class, TargetSuccessionTests.class, TargetTransitionUsageMemberTests.class, TargetTransitionUsageTests.class, TerminateNodeTests.class,
                        TextualRepresentationTests.class, TransitionSourceMemberTests.class, TransitionSuccessionMemberTests.class, TransitionSuccessionTests.class, TransitionUsageMemberTests.class,
                        TransitionUsageTests.class, TriggerActionMemberTests.class, TriggerActionTests.class, TriggerExpressionTests.class, TriggerFeatureValueTests.class,
                        TypeReferenceMemberTests.class,
                        TypeReferenceTests.class, TypeResultMemberTests.class, UnaryExpressionTests.class, UsageElementTests.class, UseCaseDefinitionTests.class, UseCaseUsageTests.class,
                        VariantReferenceTests.class,
                        VariantUsageElementTests.class, VariantUsageMemberTests.class, VerificationCaseDefinitionTests.class, VerificationCaseUsageTests.class, ViewDefinitionTests.class,
                        ViewpointDefinitionTests.class, ViewpointUsageTests.class, ViewRenderingMemberTests.class, ViewRenderingUsageTests.class, ViewUsageTests.class, WhileLoopNodeTests.class,
                        XorExpressionMemberTests.class, XorExpressionReferenceTests.class, XorExpressionTests.class, })
public class AllSysMLTests {

}
