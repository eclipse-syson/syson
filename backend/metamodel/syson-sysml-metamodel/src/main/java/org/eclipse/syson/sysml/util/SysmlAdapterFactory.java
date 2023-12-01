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
package org.eclipse.syson.sysml.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

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
import org.eclipse.syson.sysml.Predicate;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.RenderingDefinition;
import org.eclipse.syson.sysml.RenderingUsage;
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
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TextualRepresentation;
import org.eclipse.syson.sysml.TransitionFeatureMembership;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.TriggerInvocationExpression;
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
import org.eclipse.syson.sysml.WhileLoopActionUsage;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.syson.sysml.SysmlPackage
 * @generated
 */
public class SysmlAdapterFactory extends AdapterFactoryImpl {
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static SysmlPackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SysmlAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = SysmlPackage.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object.
     * <!-- begin-user-doc -->
     * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
     * <!-- end-user-doc -->
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    @Override
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject)object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch that delegates to the <code>createXXX</code> methods.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected SysmlSwitch<Adapter> modelSwitch =
        new SysmlSwitch<Adapter>() {
            @Override
            public Adapter caseAcceptActionUsage(AcceptActionUsage object) {
                return createAcceptActionUsageAdapter();
            }
            @Override
            public Adapter caseActionDefinition(ActionDefinition object) {
                return createActionDefinitionAdapter();
            }
            @Override
            public Adapter caseActionUsage(ActionUsage object) {
                return createActionUsageAdapter();
            }
            @Override
            public Adapter caseActorMembership(ActorMembership object) {
                return createActorMembershipAdapter();
            }
            @Override
            public Adapter caseAllocationDefinition(AllocationDefinition object) {
                return createAllocationDefinitionAdapter();
            }
            @Override
            public Adapter caseAllocationUsage(AllocationUsage object) {
                return createAllocationUsageAdapter();
            }
            @Override
            public Adapter caseAnalysisCaseDefinition(AnalysisCaseDefinition object) {
                return createAnalysisCaseDefinitionAdapter();
            }
            @Override
            public Adapter caseAnalysisCaseUsage(AnalysisCaseUsage object) {
                return createAnalysisCaseUsageAdapter();
            }
            @Override
            public Adapter caseAnnotatingElement(AnnotatingElement object) {
                return createAnnotatingElementAdapter();
            }
            @Override
            public Adapter caseAnnotation(Annotation object) {
                return createAnnotationAdapter();
            }
            @Override
            public Adapter caseAssertConstraintUsage(AssertConstraintUsage object) {
                return createAssertConstraintUsageAdapter();
            }
            @Override
            public Adapter caseAssignmentActionUsage(AssignmentActionUsage object) {
                return createAssignmentActionUsageAdapter();
            }
            @Override
            public Adapter caseAssociation(Association object) {
                return createAssociationAdapter();
            }
            @Override
            public Adapter caseAssociationStructure(AssociationStructure object) {
                return createAssociationStructureAdapter();
            }
            @Override
            public Adapter caseAttributeDefinition(AttributeDefinition object) {
                return createAttributeDefinitionAdapter();
            }
            @Override
            public Adapter caseAttributeUsage(AttributeUsage object) {
                return createAttributeUsageAdapter();
            }
            @Override
            public Adapter caseBehavior(Behavior object) {
                return createBehaviorAdapter();
            }
            @Override
            public Adapter caseBindingConnector(BindingConnector object) {
                return createBindingConnectorAdapter();
            }
            @Override
            public Adapter caseBindingConnectorAsUsage(BindingConnectorAsUsage object) {
                return createBindingConnectorAsUsageAdapter();
            }
            @Override
            public Adapter caseBooleanExpression(BooleanExpression object) {
                return createBooleanExpressionAdapter();
            }
            @Override
            public Adapter caseCalculationDefinition(CalculationDefinition object) {
                return createCalculationDefinitionAdapter();
            }
            @Override
            public Adapter caseCalculationUsage(CalculationUsage object) {
                return createCalculationUsageAdapter();
            }
            @Override
            public Adapter caseCaseDefinition(CaseDefinition object) {
                return createCaseDefinitionAdapter();
            }
            @Override
            public Adapter caseCaseUsage(CaseUsage object) {
                return createCaseUsageAdapter();
            }
            @Override
            public Adapter caseClass(org.eclipse.syson.sysml.Class object) {
                return createClassAdapter();
            }
            @Override
            public Adapter caseClassifier(Classifier object) {
                return createClassifierAdapter();
            }
            @Override
            public Adapter caseCollectExpression(CollectExpression object) {
                return createCollectExpressionAdapter();
            }
            @Override
            public Adapter caseComment(Comment object) {
                return createCommentAdapter();
            }
            @Override
            public Adapter caseConcernDefinition(ConcernDefinition object) {
                return createConcernDefinitionAdapter();
            }
            @Override
            public Adapter caseConcernUsage(ConcernUsage object) {
                return createConcernUsageAdapter();
            }
            @Override
            public Adapter caseConjugatedPortDefinition(ConjugatedPortDefinition object) {
                return createConjugatedPortDefinitionAdapter();
            }
            @Override
            public Adapter caseConjugatedPortTyping(ConjugatedPortTyping object) {
                return createConjugatedPortTypingAdapter();
            }
            @Override
            public Adapter caseConjugation(Conjugation object) {
                return createConjugationAdapter();
            }
            @Override
            public Adapter caseConnectionDefinition(ConnectionDefinition object) {
                return createConnectionDefinitionAdapter();
            }
            @Override
            public Adapter caseConnectionUsage(ConnectionUsage object) {
                return createConnectionUsageAdapter();
            }
            @Override
            public Adapter caseConnector(Connector object) {
                return createConnectorAdapter();
            }
            @Override
            public Adapter caseConnectorAsUsage(ConnectorAsUsage object) {
                return createConnectorAsUsageAdapter();
            }
            @Override
            public Adapter caseConstraintDefinition(ConstraintDefinition object) {
                return createConstraintDefinitionAdapter();
            }
            @Override
            public Adapter caseConstraintUsage(ConstraintUsage object) {
                return createConstraintUsageAdapter();
            }
            @Override
            public Adapter caseControlNode(ControlNode object) {
                return createControlNodeAdapter();
            }
            @Override
            public Adapter caseDataType(DataType object) {
                return createDataTypeAdapter();
            }
            @Override
            public Adapter caseDecisionNode(DecisionNode object) {
                return createDecisionNodeAdapter();
            }
            @Override
            public Adapter caseDefinition(Definition object) {
                return createDefinitionAdapter();
            }
            @Override
            public Adapter caseDependency(Dependency object) {
                return createDependencyAdapter();
            }
            @Override
            public Adapter caseDifferencing(Differencing object) {
                return createDifferencingAdapter();
            }
            @Override
            public Adapter caseDisjoining(Disjoining object) {
                return createDisjoiningAdapter();
            }
            @Override
            public Adapter caseDocumentation(Documentation object) {
                return createDocumentationAdapter();
            }
            @Override
            public Adapter caseElement(Element object) {
                return createElementAdapter();
            }
            @Override
            public Adapter caseElementFilterMembership(ElementFilterMembership object) {
                return createElementFilterMembershipAdapter();
            }
            @Override
            public Adapter caseEndFeatureMembership(EndFeatureMembership object) {
                return createEndFeatureMembershipAdapter();
            }
            @Override
            public Adapter caseEnumerationDefinition(EnumerationDefinition object) {
                return createEnumerationDefinitionAdapter();
            }
            @Override
            public Adapter caseEnumerationUsage(EnumerationUsage object) {
                return createEnumerationUsageAdapter();
            }
            @Override
            public Adapter caseEventOccurrenceUsage(EventOccurrenceUsage object) {
                return createEventOccurrenceUsageAdapter();
            }
            @Override
            public Adapter caseExhibitStateUsage(ExhibitStateUsage object) {
                return createExhibitStateUsageAdapter();
            }
            @Override
            public Adapter caseExpose(Expose object) {
                return createExposeAdapter();
            }
            @Override
            public Adapter caseExpression(Expression object) {
                return createExpressionAdapter();
            }
            @Override
            public Adapter caseFeature(Feature object) {
                return createFeatureAdapter();
            }
            @Override
            public Adapter caseFeatureChainExpression(FeatureChainExpression object) {
                return createFeatureChainExpressionAdapter();
            }
            @Override
            public Adapter caseFeatureChaining(FeatureChaining object) {
                return createFeatureChainingAdapter();
            }
            @Override
            public Adapter caseFeatureInverting(FeatureInverting object) {
                return createFeatureInvertingAdapter();
            }
            @Override
            public Adapter caseFeatureMembership(FeatureMembership object) {
                return createFeatureMembershipAdapter();
            }
            @Override
            public Adapter caseFeatureReferenceExpression(FeatureReferenceExpression object) {
                return createFeatureReferenceExpressionAdapter();
            }
            @Override
            public Adapter caseFeatureTyping(FeatureTyping object) {
                return createFeatureTypingAdapter();
            }
            @Override
            public Adapter caseFeatureValue(FeatureValue object) {
                return createFeatureValueAdapter();
            }
            @Override
            public Adapter caseFeaturing(Featuring object) {
                return createFeaturingAdapter();
            }
            @Override
            public Adapter caseFlowConnectionDefinition(FlowConnectionDefinition object) {
                return createFlowConnectionDefinitionAdapter();
            }
            @Override
            public Adapter caseFlowConnectionUsage(FlowConnectionUsage object) {
                return createFlowConnectionUsageAdapter();
            }
            @Override
            public Adapter caseForkNode(ForkNode object) {
                return createForkNodeAdapter();
            }
            @Override
            public Adapter caseForLoopActionUsage(ForLoopActionUsage object) {
                return createForLoopActionUsageAdapter();
            }
            @Override
            public Adapter caseFramedConcernMembership(FramedConcernMembership object) {
                return createFramedConcernMembershipAdapter();
            }
            @Override
            public Adapter caseFunction(Function object) {
                return createFunctionAdapter();
            }
            @Override
            public Adapter caseIfActionUsage(IfActionUsage object) {
                return createIfActionUsageAdapter();
            }
            @Override
            public Adapter caseImport(Import object) {
                return createImportAdapter();
            }
            @Override
            public Adapter caseIncludeUseCaseUsage(IncludeUseCaseUsage object) {
                return createIncludeUseCaseUsageAdapter();
            }
            @Override
            public Adapter caseInteraction(Interaction object) {
                return createInteractionAdapter();
            }
            @Override
            public Adapter caseInterfaceDefinition(InterfaceDefinition object) {
                return createInterfaceDefinitionAdapter();
            }
            @Override
            public Adapter caseInterfaceUsage(InterfaceUsage object) {
                return createInterfaceUsageAdapter();
            }
            @Override
            public Adapter caseIntersecting(Intersecting object) {
                return createIntersectingAdapter();
            }
            @Override
            public Adapter caseInvariant(Invariant object) {
                return createInvariantAdapter();
            }
            @Override
            public Adapter caseInvocationExpression(InvocationExpression object) {
                return createInvocationExpressionAdapter();
            }
            @Override
            public Adapter caseItemDefinition(ItemDefinition object) {
                return createItemDefinitionAdapter();
            }
            @Override
            public Adapter caseItemFeature(ItemFeature object) {
                return createItemFeatureAdapter();
            }
            @Override
            public Adapter caseItemFlow(ItemFlow object) {
                return createItemFlowAdapter();
            }
            @Override
            public Adapter caseItemFlowEnd(ItemFlowEnd object) {
                return createItemFlowEndAdapter();
            }
            @Override
            public Adapter caseItemUsage(ItemUsage object) {
                return createItemUsageAdapter();
            }
            @Override
            public Adapter caseJoinNode(JoinNode object) {
                return createJoinNodeAdapter();
            }
            @Override
            public Adapter caseLibraryPackage(LibraryPackage object) {
                return createLibraryPackageAdapter();
            }
            @Override
            public Adapter caseLifeClass(LifeClass object) {
                return createLifeClassAdapter();
            }
            @Override
            public Adapter caseLiteralBoolean(LiteralBoolean object) {
                return createLiteralBooleanAdapter();
            }
            @Override
            public Adapter caseLiteralExpression(LiteralExpression object) {
                return createLiteralExpressionAdapter();
            }
            @Override
            public Adapter caseLiteralInfinity(LiteralInfinity object) {
                return createLiteralInfinityAdapter();
            }
            @Override
            public Adapter caseLiteralInteger(LiteralInteger object) {
                return createLiteralIntegerAdapter();
            }
            @Override
            public Adapter caseLiteralRational(LiteralRational object) {
                return createLiteralRationalAdapter();
            }
            @Override
            public Adapter caseLiteralString(LiteralString object) {
                return createLiteralStringAdapter();
            }
            @Override
            public Adapter caseLoopActionUsage(LoopActionUsage object) {
                return createLoopActionUsageAdapter();
            }
            @Override
            public Adapter caseMembership(Membership object) {
                return createMembershipAdapter();
            }
            @Override
            public Adapter caseMembershipExpose(MembershipExpose object) {
                return createMembershipExposeAdapter();
            }
            @Override
            public Adapter caseMembershipImport(MembershipImport object) {
                return createMembershipImportAdapter();
            }
            @Override
            public Adapter caseMergeNode(MergeNode object) {
                return createMergeNodeAdapter();
            }
            @Override
            public Adapter caseMetaclass(Metaclass object) {
                return createMetaclassAdapter();
            }
            @Override
            public Adapter caseMetadataAccessExpression(MetadataAccessExpression object) {
                return createMetadataAccessExpressionAdapter();
            }
            @Override
            public Adapter caseMetadataDefinition(MetadataDefinition object) {
                return createMetadataDefinitionAdapter();
            }
            @Override
            public Adapter caseMetadataFeature(MetadataFeature object) {
                return createMetadataFeatureAdapter();
            }
            @Override
            public Adapter caseMetadataUsage(MetadataUsage object) {
                return createMetadataUsageAdapter();
            }
            @Override
            public Adapter caseMultiplicity(Multiplicity object) {
                return createMultiplicityAdapter();
            }
            @Override
            public Adapter caseMultiplicityRange(MultiplicityRange object) {
                return createMultiplicityRangeAdapter();
            }
            @Override
            public Adapter caseNamespace(Namespace object) {
                return createNamespaceAdapter();
            }
            @Override
            public Adapter caseNamespaceExpose(NamespaceExpose object) {
                return createNamespaceExposeAdapter();
            }
            @Override
            public Adapter caseNamespaceImport(NamespaceImport object) {
                return createNamespaceImportAdapter();
            }
            @Override
            public Adapter caseNullExpression(NullExpression object) {
                return createNullExpressionAdapter();
            }
            @Override
            public Adapter caseObjectiveMembership(ObjectiveMembership object) {
                return createObjectiveMembershipAdapter();
            }
            @Override
            public Adapter caseOccurrenceDefinition(OccurrenceDefinition object) {
                return createOccurrenceDefinitionAdapter();
            }
            @Override
            public Adapter caseOccurrenceUsage(OccurrenceUsage object) {
                return createOccurrenceUsageAdapter();
            }
            @Override
            public Adapter caseOperatorExpression(OperatorExpression object) {
                return createOperatorExpressionAdapter();
            }
            @Override
            public Adapter caseOwningMembership(OwningMembership object) {
                return createOwningMembershipAdapter();
            }
            @Override
            public Adapter casePackage(org.eclipse.syson.sysml.Package object) {
                return createPackageAdapter();
            }
            @Override
            public Adapter caseParameterMembership(ParameterMembership object) {
                return createParameterMembershipAdapter();
            }
            @Override
            public Adapter casePartDefinition(PartDefinition object) {
                return createPartDefinitionAdapter();
            }
            @Override
            public Adapter casePartUsage(PartUsage object) {
                return createPartUsageAdapter();
            }
            @Override
            public Adapter casePerformActionUsage(PerformActionUsage object) {
                return createPerformActionUsageAdapter();
            }
            @Override
            public Adapter casePortConjugation(PortConjugation object) {
                return createPortConjugationAdapter();
            }
            @Override
            public Adapter casePortDefinition(PortDefinition object) {
                return createPortDefinitionAdapter();
            }
            @Override
            public Adapter casePortUsage(PortUsage object) {
                return createPortUsageAdapter();
            }
            @Override
            public Adapter casePredicate(Predicate object) {
                return createPredicateAdapter();
            }
            @Override
            public Adapter caseRedefinition(Redefinition object) {
                return createRedefinitionAdapter();
            }
            @Override
            public Adapter caseReferenceSubsetting(ReferenceSubsetting object) {
                return createReferenceSubsettingAdapter();
            }
            @Override
            public Adapter caseReferenceUsage(ReferenceUsage object) {
                return createReferenceUsageAdapter();
            }
            @Override
            public Adapter caseRelationship(Relationship object) {
                return createRelationshipAdapter();
            }
            @Override
            public Adapter caseRenderingDefinition(RenderingDefinition object) {
                return createRenderingDefinitionAdapter();
            }
            @Override
            public Adapter caseRenderingUsage(RenderingUsage object) {
                return createRenderingUsageAdapter();
            }
            @Override
            public Adapter caseRequirementConstraintMembership(RequirementConstraintMembership object) {
                return createRequirementConstraintMembershipAdapter();
            }
            @Override
            public Adapter caseRequirementDefinition(RequirementDefinition object) {
                return createRequirementDefinitionAdapter();
            }
            @Override
            public Adapter caseRequirementUsage(RequirementUsage object) {
                return createRequirementUsageAdapter();
            }
            @Override
            public Adapter caseRequirementVerificationMembership(RequirementVerificationMembership object) {
                return createRequirementVerificationMembershipAdapter();
            }
            @Override
            public Adapter caseResultExpressionMembership(ResultExpressionMembership object) {
                return createResultExpressionMembershipAdapter();
            }
            @Override
            public Adapter caseReturnParameterMembership(ReturnParameterMembership object) {
                return createReturnParameterMembershipAdapter();
            }
            @Override
            public Adapter caseSatisfyRequirementUsage(SatisfyRequirementUsage object) {
                return createSatisfyRequirementUsageAdapter();
            }
            @Override
            public Adapter caseSelectExpression(SelectExpression object) {
                return createSelectExpressionAdapter();
            }
            @Override
            public Adapter caseSendActionUsage(SendActionUsage object) {
                return createSendActionUsageAdapter();
            }
            @Override
            public Adapter caseSpecialization(Specialization object) {
                return createSpecializationAdapter();
            }
            @Override
            public Adapter caseStakeholderMembership(StakeholderMembership object) {
                return createStakeholderMembershipAdapter();
            }
            @Override
            public Adapter caseStateDefinition(StateDefinition object) {
                return createStateDefinitionAdapter();
            }
            @Override
            public Adapter caseStateSubactionMembership(StateSubactionMembership object) {
                return createStateSubactionMembershipAdapter();
            }
            @Override
            public Adapter caseStateUsage(StateUsage object) {
                return createStateUsageAdapter();
            }
            @Override
            public Adapter caseStep(Step object) {
                return createStepAdapter();
            }
            @Override
            public Adapter caseStructure(Structure object) {
                return createStructureAdapter();
            }
            @Override
            public Adapter caseSubclassification(Subclassification object) {
                return createSubclassificationAdapter();
            }
            @Override
            public Adapter caseSubjectMembership(SubjectMembership object) {
                return createSubjectMembershipAdapter();
            }
            @Override
            public Adapter caseSubsetting(Subsetting object) {
                return createSubsettingAdapter();
            }
            @Override
            public Adapter caseSuccession(Succession object) {
                return createSuccessionAdapter();
            }
            @Override
            public Adapter caseSuccessionAsUsage(SuccessionAsUsage object) {
                return createSuccessionAsUsageAdapter();
            }
            @Override
            public Adapter caseSuccessionFlowConnectionUsage(SuccessionFlowConnectionUsage object) {
                return createSuccessionFlowConnectionUsageAdapter();
            }
            @Override
            public Adapter caseSuccessionItemFlow(SuccessionItemFlow object) {
                return createSuccessionItemFlowAdapter();
            }
            @Override
            public Adapter caseTextualRepresentation(TextualRepresentation object) {
                return createTextualRepresentationAdapter();
            }
            @Override
            public Adapter caseTransitionFeatureMembership(TransitionFeatureMembership object) {
                return createTransitionFeatureMembershipAdapter();
            }
            @Override
            public Adapter caseTransitionUsage(TransitionUsage object) {
                return createTransitionUsageAdapter();
            }
            @Override
            public Adapter caseTriggerInvocationExpression(TriggerInvocationExpression object) {
                return createTriggerInvocationExpressionAdapter();
            }
            @Override
            public Adapter caseType(Type object) {
                return createTypeAdapter();
            }
            @Override
            public Adapter caseTypeFeaturing(TypeFeaturing object) {
                return createTypeFeaturingAdapter();
            }
            @Override
            public Adapter caseUnioning(Unioning object) {
                return createUnioningAdapter();
            }
            @Override
            public Adapter caseUsage(Usage object) {
                return createUsageAdapter();
            }
            @Override
            public Adapter caseUseCaseDefinition(UseCaseDefinition object) {
                return createUseCaseDefinitionAdapter();
            }
            @Override
            public Adapter caseUseCaseUsage(UseCaseUsage object) {
                return createUseCaseUsageAdapter();
            }
            @Override
            public Adapter caseVariantMembership(VariantMembership object) {
                return createVariantMembershipAdapter();
            }
            @Override
            public Adapter caseVerificationCaseDefinition(VerificationCaseDefinition object) {
                return createVerificationCaseDefinitionAdapter();
            }
            @Override
            public Adapter caseVerificationCaseUsage(VerificationCaseUsage object) {
                return createVerificationCaseUsageAdapter();
            }
            @Override
            public Adapter caseViewDefinition(ViewDefinition object) {
                return createViewDefinitionAdapter();
            }
            @Override
            public Adapter caseViewpointDefinition(ViewpointDefinition object) {
                return createViewpointDefinitionAdapter();
            }
            @Override
            public Adapter caseViewpointUsage(ViewpointUsage object) {
                return createViewpointUsageAdapter();
            }
            @Override
            public Adapter caseViewRenderingMembership(ViewRenderingMembership object) {
                return createViewRenderingMembershipAdapter();
            }
            @Override
            public Adapter caseViewUsage(ViewUsage object) {
                return createViewUsageAdapter();
            }
            @Override
            public Adapter caseWhileLoopActionUsage(WhileLoopActionUsage object) {
                return createWhileLoopActionUsageAdapter();
            }
            @Override
            public Adapter defaultCase(EObject object) {
                return createEObjectAdapter();
            }
        };

    /**
     * Creates an adapter for the <code>target</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param target the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    @Override
    public Adapter createAdapter(Notifier target) {
        return modelSwitch.doSwitch((EObject)target);
    }


    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.AcceptActionUsage <em>Accept Action Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.AcceptActionUsage
     * @generated
     */
    public Adapter createAcceptActionUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ActionDefinition <em>Action Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ActionDefinition
     * @generated
     */
    public Adapter createActionDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ActionUsage <em>Action Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ActionUsage
     * @generated
     */
    public Adapter createActionUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ActorMembership <em>Actor Membership</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ActorMembership
     * @generated
     */
    public Adapter createActorMembershipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.AllocationDefinition <em>Allocation Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.AllocationDefinition
     * @generated
     */
    public Adapter createAllocationDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.AllocationUsage <em>Allocation Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.AllocationUsage
     * @generated
     */
    public Adapter createAllocationUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.AnalysisCaseDefinition <em>Analysis Case Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.AnalysisCaseDefinition
     * @generated
     */
    public Adapter createAnalysisCaseDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.AnalysisCaseUsage <em>Analysis Case Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.AnalysisCaseUsage
     * @generated
     */
    public Adapter createAnalysisCaseUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.AnnotatingElement <em>Annotating Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.AnnotatingElement
     * @generated
     */
    public Adapter createAnnotatingElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Annotation <em>Annotation</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Annotation
     * @generated
     */
    public Adapter createAnnotationAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.AssertConstraintUsage <em>Assert Constraint Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.AssertConstraintUsage
     * @generated
     */
    public Adapter createAssertConstraintUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.AssignmentActionUsage <em>Assignment Action Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.AssignmentActionUsage
     * @generated
     */
    public Adapter createAssignmentActionUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Association <em>Association</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Association
     * @generated
     */
    public Adapter createAssociationAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.AssociationStructure <em>Association Structure</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.AssociationStructure
     * @generated
     */
    public Adapter createAssociationStructureAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.AttributeDefinition <em>Attribute Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.AttributeDefinition
     * @generated
     */
    public Adapter createAttributeDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.AttributeUsage <em>Attribute Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.AttributeUsage
     * @generated
     */
    public Adapter createAttributeUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Behavior <em>Behavior</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Behavior
     * @generated
     */
    public Adapter createBehaviorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.BindingConnector <em>Binding Connector</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.BindingConnector
     * @generated
     */
    public Adapter createBindingConnectorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.BindingConnectorAsUsage <em>Binding Connector As Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.BindingConnectorAsUsage
     * @generated
     */
    public Adapter createBindingConnectorAsUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.BooleanExpression <em>Boolean Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.BooleanExpression
     * @generated
     */
    public Adapter createBooleanExpressionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.CalculationDefinition <em>Calculation Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.CalculationDefinition
     * @generated
     */
    public Adapter createCalculationDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.CalculationUsage <em>Calculation Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.CalculationUsage
     * @generated
     */
    public Adapter createCalculationUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.CaseDefinition <em>Case Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.CaseDefinition
     * @generated
     */
    public Adapter createCaseDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.CaseUsage <em>Case Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.CaseUsage
     * @generated
     */
    public Adapter createCaseUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Class <em>Class</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Class
     * @generated
     */
    public Adapter createClassAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Classifier <em>Classifier</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Classifier
     * @generated
     */
    public Adapter createClassifierAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.CollectExpression <em>Collect Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.CollectExpression
     * @generated
     */
    public Adapter createCollectExpressionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Comment <em>Comment</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Comment
     * @generated
     */
    public Adapter createCommentAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ConcernDefinition <em>Concern Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ConcernDefinition
     * @generated
     */
    public Adapter createConcernDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ConcernUsage <em>Concern Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ConcernUsage
     * @generated
     */
    public Adapter createConcernUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ConjugatedPortDefinition <em>Conjugated Port Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ConjugatedPortDefinition
     * @generated
     */
    public Adapter createConjugatedPortDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ConjugatedPortTyping <em>Conjugated Port Typing</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ConjugatedPortTyping
     * @generated
     */
    public Adapter createConjugatedPortTypingAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Conjugation <em>Conjugation</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Conjugation
     * @generated
     */
    public Adapter createConjugationAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ConnectionDefinition <em>Connection Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ConnectionDefinition
     * @generated
     */
    public Adapter createConnectionDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ConnectionUsage <em>Connection Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ConnectionUsage
     * @generated
     */
    public Adapter createConnectionUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Connector <em>Connector</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Connector
     * @generated
     */
    public Adapter createConnectorAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ConnectorAsUsage <em>Connector As Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ConnectorAsUsage
     * @generated
     */
    public Adapter createConnectorAsUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ConstraintDefinition <em>Constraint Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ConstraintDefinition
     * @generated
     */
    public Adapter createConstraintDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ConstraintUsage <em>Constraint Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ConstraintUsage
     * @generated
     */
    public Adapter createConstraintUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ControlNode <em>Control Node</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ControlNode
     * @generated
     */
    public Adapter createControlNodeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.DataType <em>Data Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.DataType
     * @generated
     */
    public Adapter createDataTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.DecisionNode <em>Decision Node</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.DecisionNode
     * @generated
     */
    public Adapter createDecisionNodeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Definition <em>Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Definition
     * @generated
     */
    public Adapter createDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Dependency <em>Dependency</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Dependency
     * @generated
     */
    public Adapter createDependencyAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Differencing <em>Differencing</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Differencing
     * @generated
     */
    public Adapter createDifferencingAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Disjoining <em>Disjoining</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Disjoining
     * @generated
     */
    public Adapter createDisjoiningAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Documentation <em>Documentation</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Documentation
     * @generated
     */
    public Adapter createDocumentationAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Element <em>Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Element
     * @generated
     */
    public Adapter createElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ElementFilterMembership <em>Element Filter Membership</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ElementFilterMembership
     * @generated
     */
    public Adapter createElementFilterMembershipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.EndFeatureMembership <em>End Feature Membership</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.EndFeatureMembership
     * @generated
     */
    public Adapter createEndFeatureMembershipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.EnumerationDefinition <em>Enumeration Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.EnumerationDefinition
     * @generated
     */
    public Adapter createEnumerationDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.EnumerationUsage <em>Enumeration Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.EnumerationUsage
     * @generated
     */
    public Adapter createEnumerationUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.EventOccurrenceUsage <em>Event Occurrence Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.EventOccurrenceUsage
     * @generated
     */
    public Adapter createEventOccurrenceUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ExhibitStateUsage <em>Exhibit State Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ExhibitStateUsage
     * @generated
     */
    public Adapter createExhibitStateUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Expose <em>Expose</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Expose
     * @generated
     */
    public Adapter createExposeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Expression <em>Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Expression
     * @generated
     */
    public Adapter createExpressionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Feature <em>Feature</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Feature
     * @generated
     */
    public Adapter createFeatureAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.FeatureChainExpression <em>Feature Chain Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.FeatureChainExpression
     * @generated
     */
    public Adapter createFeatureChainExpressionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.FeatureChaining <em>Feature Chaining</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.FeatureChaining
     * @generated
     */
    public Adapter createFeatureChainingAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.FeatureInverting <em>Feature Inverting</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.FeatureInverting
     * @generated
     */
    public Adapter createFeatureInvertingAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.FeatureMembership <em>Feature Membership</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.FeatureMembership
     * @generated
     */
    public Adapter createFeatureMembershipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.FeatureReferenceExpression <em>Feature Reference Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.FeatureReferenceExpression
     * @generated
     */
    public Adapter createFeatureReferenceExpressionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.FeatureTyping <em>Feature Typing</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.FeatureTyping
     * @generated
     */
    public Adapter createFeatureTypingAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.FeatureValue <em>Feature Value</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.FeatureValue
     * @generated
     */
    public Adapter createFeatureValueAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Featuring <em>Featuring</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Featuring
     * @generated
     */
    public Adapter createFeaturingAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.FlowConnectionDefinition <em>Flow Connection Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.FlowConnectionDefinition
     * @generated
     */
    public Adapter createFlowConnectionDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.FlowConnectionUsage <em>Flow Connection Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.FlowConnectionUsage
     * @generated
     */
    public Adapter createFlowConnectionUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ForkNode <em>Fork Node</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ForkNode
     * @generated
     */
    public Adapter createForkNodeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ForLoopActionUsage <em>For Loop Action Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ForLoopActionUsage
     * @generated
     */
    public Adapter createForLoopActionUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.FramedConcernMembership <em>Framed Concern Membership</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.FramedConcernMembership
     * @generated
     */
    public Adapter createFramedConcernMembershipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Function <em>Function</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Function
     * @generated
     */
    public Adapter createFunctionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.IfActionUsage <em>If Action Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.IfActionUsage
     * @generated
     */
    public Adapter createIfActionUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Import <em>Import</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Import
     * @generated
     */
    public Adapter createImportAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.IncludeUseCaseUsage <em>Include Use Case Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.IncludeUseCaseUsage
     * @generated
     */
    public Adapter createIncludeUseCaseUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Interaction <em>Interaction</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Interaction
     * @generated
     */
    public Adapter createInteractionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.InterfaceDefinition <em>Interface Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.InterfaceDefinition
     * @generated
     */
    public Adapter createInterfaceDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.InterfaceUsage <em>Interface Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.InterfaceUsage
     * @generated
     */
    public Adapter createInterfaceUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Intersecting <em>Intersecting</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Intersecting
     * @generated
     */
    public Adapter createIntersectingAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Invariant <em>Invariant</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Invariant
     * @generated
     */
    public Adapter createInvariantAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.InvocationExpression <em>Invocation Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.InvocationExpression
     * @generated
     */
    public Adapter createInvocationExpressionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ItemDefinition <em>Item Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ItemDefinition
     * @generated
     */
    public Adapter createItemDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ItemFeature <em>Item Feature</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ItemFeature
     * @generated
     */
    public Adapter createItemFeatureAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ItemFlow <em>Item Flow</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ItemFlow
     * @generated
     */
    public Adapter createItemFlowAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ItemFlowEnd <em>Item Flow End</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ItemFlowEnd
     * @generated
     */
    public Adapter createItemFlowEndAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ItemUsage <em>Item Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ItemUsage
     * @generated
     */
    public Adapter createItemUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.JoinNode <em>Join Node</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.JoinNode
     * @generated
     */
    public Adapter createJoinNodeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.LibraryPackage <em>Library Package</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.LibraryPackage
     * @generated
     */
    public Adapter createLibraryPackageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.LifeClass <em>Life Class</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.LifeClass
     * @generated
     */
    public Adapter createLifeClassAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.LiteralBoolean <em>Literal Boolean</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.LiteralBoolean
     * @generated
     */
    public Adapter createLiteralBooleanAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.LiteralExpression <em>Literal Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.LiteralExpression
     * @generated
     */
    public Adapter createLiteralExpressionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.LiteralInfinity <em>Literal Infinity</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.LiteralInfinity
     * @generated
     */
    public Adapter createLiteralInfinityAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.LiteralInteger <em>Literal Integer</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.LiteralInteger
     * @generated
     */
    public Adapter createLiteralIntegerAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.LiteralRational <em>Literal Rational</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.LiteralRational
     * @generated
     */
    public Adapter createLiteralRationalAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.LiteralString <em>Literal String</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.LiteralString
     * @generated
     */
    public Adapter createLiteralStringAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.LoopActionUsage <em>Loop Action Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.LoopActionUsage
     * @generated
     */
    public Adapter createLoopActionUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Membership <em>Membership</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Membership
     * @generated
     */
    public Adapter createMembershipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.MembershipExpose <em>Membership Expose</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.MembershipExpose
     * @generated
     */
    public Adapter createMembershipExposeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.MembershipImport <em>Membership Import</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.MembershipImport
     * @generated
     */
    public Adapter createMembershipImportAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.MergeNode <em>Merge Node</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.MergeNode
     * @generated
     */
    public Adapter createMergeNodeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Metaclass <em>Metaclass</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Metaclass
     * @generated
     */
    public Adapter createMetaclassAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.MetadataAccessExpression <em>Metadata Access Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.MetadataAccessExpression
     * @generated
     */
    public Adapter createMetadataAccessExpressionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.MetadataDefinition <em>Metadata Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.MetadataDefinition
     * @generated
     */
    public Adapter createMetadataDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.MetadataFeature <em>Metadata Feature</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.MetadataFeature
     * @generated
     */
    public Adapter createMetadataFeatureAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.MetadataUsage <em>Metadata Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.MetadataUsage
     * @generated
     */
    public Adapter createMetadataUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Multiplicity <em>Multiplicity</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Multiplicity
     * @generated
     */
    public Adapter createMultiplicityAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.MultiplicityRange <em>Multiplicity Range</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.MultiplicityRange
     * @generated
     */
    public Adapter createMultiplicityRangeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Namespace <em>Namespace</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Namespace
     * @generated
     */
    public Adapter createNamespaceAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.NamespaceExpose <em>Namespace Expose</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.NamespaceExpose
     * @generated
     */
    public Adapter createNamespaceExposeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.NamespaceImport <em>Namespace Import</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.NamespaceImport
     * @generated
     */
    public Adapter createNamespaceImportAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.NullExpression <em>Null Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.NullExpression
     * @generated
     */
    public Adapter createNullExpressionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ObjectiveMembership <em>Objective Membership</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ObjectiveMembership
     * @generated
     */
    public Adapter createObjectiveMembershipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.OccurrenceDefinition <em>Occurrence Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.OccurrenceDefinition
     * @generated
     */
    public Adapter createOccurrenceDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.OccurrenceUsage <em>Occurrence Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.OccurrenceUsage
     * @generated
     */
    public Adapter createOccurrenceUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.OperatorExpression <em>Operator Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.OperatorExpression
     * @generated
     */
    public Adapter createOperatorExpressionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.OwningMembership <em>Owning Membership</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.OwningMembership
     * @generated
     */
    public Adapter createOwningMembershipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Package <em>Package</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Package
     * @generated
     */
    public Adapter createPackageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ParameterMembership <em>Parameter Membership</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ParameterMembership
     * @generated
     */
    public Adapter createParameterMembershipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.PartDefinition <em>Part Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.PartDefinition
     * @generated
     */
    public Adapter createPartDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.PartUsage <em>Part Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.PartUsage
     * @generated
     */
    public Adapter createPartUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.PerformActionUsage <em>Perform Action Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.PerformActionUsage
     * @generated
     */
    public Adapter createPerformActionUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.PortConjugation <em>Port Conjugation</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.PortConjugation
     * @generated
     */
    public Adapter createPortConjugationAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.PortDefinition <em>Port Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.PortDefinition
     * @generated
     */
    public Adapter createPortDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.PortUsage <em>Port Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.PortUsage
     * @generated
     */
    public Adapter createPortUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Predicate <em>Predicate</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Predicate
     * @generated
     */
    public Adapter createPredicateAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Redefinition <em>Redefinition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Redefinition
     * @generated
     */
    public Adapter createRedefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ReferenceSubsetting <em>Reference Subsetting</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ReferenceSubsetting
     * @generated
     */
    public Adapter createReferenceSubsettingAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ReferenceUsage <em>Reference Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ReferenceUsage
     * @generated
     */
    public Adapter createReferenceUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Relationship <em>Relationship</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Relationship
     * @generated
     */
    public Adapter createRelationshipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.RenderingDefinition <em>Rendering Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.RenderingDefinition
     * @generated
     */
    public Adapter createRenderingDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.RenderingUsage <em>Rendering Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.RenderingUsage
     * @generated
     */
    public Adapter createRenderingUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.RequirementConstraintMembership <em>Requirement Constraint Membership</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.RequirementConstraintMembership
     * @generated
     */
    public Adapter createRequirementConstraintMembershipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.RequirementDefinition <em>Requirement Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.RequirementDefinition
     * @generated
     */
    public Adapter createRequirementDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.RequirementUsage <em>Requirement Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.RequirementUsage
     * @generated
     */
    public Adapter createRequirementUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.RequirementVerificationMembership <em>Requirement Verification Membership</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.RequirementVerificationMembership
     * @generated
     */
    public Adapter createRequirementVerificationMembershipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ResultExpressionMembership <em>Result Expression Membership</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ResultExpressionMembership
     * @generated
     */
    public Adapter createResultExpressionMembershipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ReturnParameterMembership <em>Return Parameter Membership</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ReturnParameterMembership
     * @generated
     */
    public Adapter createReturnParameterMembershipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.SatisfyRequirementUsage <em>Satisfy Requirement Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.SatisfyRequirementUsage
     * @generated
     */
    public Adapter createSatisfyRequirementUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.SelectExpression <em>Select Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.SelectExpression
     * @generated
     */
    public Adapter createSelectExpressionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.SendActionUsage <em>Send Action Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.SendActionUsage
     * @generated
     */
    public Adapter createSendActionUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Specialization <em>Specialization</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Specialization
     * @generated
     */
    public Adapter createSpecializationAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.StakeholderMembership <em>Stakeholder Membership</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.StakeholderMembership
     * @generated
     */
    public Adapter createStakeholderMembershipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.StateDefinition <em>State Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.StateDefinition
     * @generated
     */
    public Adapter createStateDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.StateSubactionMembership <em>State Subaction Membership</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.StateSubactionMembership
     * @generated
     */
    public Adapter createStateSubactionMembershipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.StateUsage <em>State Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.StateUsage
     * @generated
     */
    public Adapter createStateUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Step <em>Step</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Step
     * @generated
     */
    public Adapter createStepAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Structure <em>Structure</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Structure
     * @generated
     */
    public Adapter createStructureAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Subclassification <em>Subclassification</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Subclassification
     * @generated
     */
    public Adapter createSubclassificationAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.SubjectMembership <em>Subject Membership</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.SubjectMembership
     * @generated
     */
    public Adapter createSubjectMembershipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Subsetting <em>Subsetting</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Subsetting
     * @generated
     */
    public Adapter createSubsettingAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Succession <em>Succession</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Succession
     * @generated
     */
    public Adapter createSuccessionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.SuccessionAsUsage <em>Succession As Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.SuccessionAsUsage
     * @generated
     */
    public Adapter createSuccessionAsUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.SuccessionFlowConnectionUsage <em>Succession Flow Connection Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.SuccessionFlowConnectionUsage
     * @generated
     */
    public Adapter createSuccessionFlowConnectionUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.SuccessionItemFlow <em>Succession Item Flow</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.SuccessionItemFlow
     * @generated
     */
    public Adapter createSuccessionItemFlowAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.TextualRepresentation <em>Textual Representation</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.TextualRepresentation
     * @generated
     */
    public Adapter createTextualRepresentationAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.TransitionFeatureMembership <em>Transition Feature Membership</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.TransitionFeatureMembership
     * @generated
     */
    public Adapter createTransitionFeatureMembershipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.TransitionUsage <em>Transition Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.TransitionUsage
     * @generated
     */
    public Adapter createTransitionUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.TriggerInvocationExpression <em>Trigger Invocation Expression</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.TriggerInvocationExpression
     * @generated
     */
    public Adapter createTriggerInvocationExpressionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Type <em>Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Type
     * @generated
     */
    public Adapter createTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.TypeFeaturing <em>Type Featuring</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.TypeFeaturing
     * @generated
     */
    public Adapter createTypeFeaturingAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Unioning <em>Unioning</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Unioning
     * @generated
     */
    public Adapter createUnioningAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.Usage <em>Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.Usage
     * @generated
     */
    public Adapter createUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.UseCaseDefinition <em>Use Case Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.UseCaseDefinition
     * @generated
     */
    public Adapter createUseCaseDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.UseCaseUsage <em>Use Case Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.UseCaseUsage
     * @generated
     */
    public Adapter createUseCaseUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.VariantMembership <em>Variant Membership</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.VariantMembership
     * @generated
     */
    public Adapter createVariantMembershipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.VerificationCaseDefinition <em>Verification Case Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.VerificationCaseDefinition
     * @generated
     */
    public Adapter createVerificationCaseDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.VerificationCaseUsage <em>Verification Case Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.VerificationCaseUsage
     * @generated
     */
    public Adapter createVerificationCaseUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ViewDefinition <em>View Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ViewDefinition
     * @generated
     */
    public Adapter createViewDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ViewpointDefinition <em>Viewpoint Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ViewpointDefinition
     * @generated
     */
    public Adapter createViewpointDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ViewpointUsage <em>Viewpoint Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ViewpointUsage
     * @generated
     */
    public Adapter createViewpointUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ViewRenderingMembership <em>View Rendering Membership</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ViewRenderingMembership
     * @generated
     */
    public Adapter createViewRenderingMembershipAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.ViewUsage <em>View Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.ViewUsage
     * @generated
     */
    public Adapter createViewUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.syson.sysml.WhileLoopActionUsage <em>While Loop Action Usage</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.syson.sysml.WhileLoopActionUsage
     * @generated
     */
    public Adapter createWhileLoopActionUsageAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for the default case.
     * <!-- begin-user-doc -->
     * This default implementation returns null.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter() {
        return null;
    }

} //SysmlAdapterFactory
