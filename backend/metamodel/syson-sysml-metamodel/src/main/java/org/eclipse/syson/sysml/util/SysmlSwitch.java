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
package org.eclipse.syson.sysml.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

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
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.syson.sysml.SysmlPackage
 * @generated
 */
public class SysmlSwitch<T> extends Switch<T> {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static SysmlPackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SysmlSwitch() {
        if (modelPackage == null) {
            modelPackage = SysmlPackage.eINSTANCE;
        }
    }

    /**
     * Checks whether this is a switch for the given package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param ePackage the package in question.
     * @return whether this is a switch for the given package.
     * @generated
     */
    @Override
    protected boolean isSwitchFor(EPackage ePackage) {
        return ePackage == modelPackage;
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    @Override
    protected T doSwitch(int classifierID, EObject theEObject) {
        switch (classifierID) {
            case SysmlPackage.ACCEPT_ACTION_USAGE: {
                AcceptActionUsage acceptActionUsage = (AcceptActionUsage)theEObject;
                T result = caseAcceptActionUsage(acceptActionUsage);
                if (result == null) result = caseActionUsage(acceptActionUsage);
                if (result == null) result = caseOccurrenceUsage(acceptActionUsage);
                if (result == null) result = caseStep(acceptActionUsage);
                if (result == null) result = caseUsage(acceptActionUsage);
                if (result == null) result = caseFeature(acceptActionUsage);
                if (result == null) result = caseType(acceptActionUsage);
                if (result == null) result = caseNamespace(acceptActionUsage);
                if (result == null) result = caseElement(acceptActionUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ACTION_DEFINITION: {
                ActionDefinition actionDefinition = (ActionDefinition)theEObject;
                T result = caseActionDefinition(actionDefinition);
                if (result == null) result = caseOccurrenceDefinition(actionDefinition);
                if (result == null) result = caseBehavior(actionDefinition);
                if (result == null) result = caseDefinition(actionDefinition);
                if (result == null) result = caseClass(actionDefinition);
                if (result == null) result = caseClassifier(actionDefinition);
                if (result == null) result = caseType(actionDefinition);
                if (result == null) result = caseNamespace(actionDefinition);
                if (result == null) result = caseElement(actionDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ACTION_USAGE: {
                ActionUsage actionUsage = (ActionUsage)theEObject;
                T result = caseActionUsage(actionUsage);
                if (result == null) result = caseOccurrenceUsage(actionUsage);
                if (result == null) result = caseStep(actionUsage);
                if (result == null) result = caseUsage(actionUsage);
                if (result == null) result = caseFeature(actionUsage);
                if (result == null) result = caseType(actionUsage);
                if (result == null) result = caseNamespace(actionUsage);
                if (result == null) result = caseElement(actionUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ACTOR_MEMBERSHIP: {
                ActorMembership actorMembership = (ActorMembership)theEObject;
                T result = caseActorMembership(actorMembership);
                if (result == null) result = caseParameterMembership(actorMembership);
                if (result == null) result = caseFeatureMembership(actorMembership);
                if (result == null) result = caseOwningMembership(actorMembership);
                if (result == null) result = caseFeaturing(actorMembership);
                if (result == null) result = caseMembership(actorMembership);
                if (result == null) result = caseRelationship(actorMembership);
                if (result == null) result = caseElement(actorMembership);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ALLOCATION_DEFINITION: {
                AllocationDefinition allocationDefinition = (AllocationDefinition)theEObject;
                T result = caseAllocationDefinition(allocationDefinition);
                if (result == null) result = caseConnectionDefinition(allocationDefinition);
                if (result == null) result = casePartDefinition(allocationDefinition);
                if (result == null) result = caseAssociationStructure(allocationDefinition);
                if (result == null) result = caseItemDefinition(allocationDefinition);
                if (result == null) result = caseAssociation(allocationDefinition);
                if (result == null) result = caseOccurrenceDefinition(allocationDefinition);
                if (result == null) result = caseStructure(allocationDefinition);
                if (result == null) result = caseRelationship(allocationDefinition);
                if (result == null) result = caseDefinition(allocationDefinition);
                if (result == null) result = caseClass(allocationDefinition);
                if (result == null) result = caseClassifier(allocationDefinition);
                if (result == null) result = caseType(allocationDefinition);
                if (result == null) result = caseNamespace(allocationDefinition);
                if (result == null) result = caseElement(allocationDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ALLOCATION_USAGE: {
                AllocationUsage allocationUsage = (AllocationUsage)theEObject;
                T result = caseAllocationUsage(allocationUsage);
                if (result == null) result = caseConnectionUsage(allocationUsage);
                if (result == null) result = caseConnectorAsUsage(allocationUsage);
                if (result == null) result = casePartUsage(allocationUsage);
                if (result == null) result = caseConnector(allocationUsage);
                if (result == null) result = caseItemUsage(allocationUsage);
                if (result == null) result = caseFeature(allocationUsage);
                if (result == null) result = caseRelationship(allocationUsage);
                if (result == null) result = caseOccurrenceUsage(allocationUsage);
                if (result == null) result = caseUsage(allocationUsage);
                if (result == null) result = caseType(allocationUsage);
                if (result == null) result = caseNamespace(allocationUsage);
                if (result == null) result = caseElement(allocationUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ANALYSIS_CASE_DEFINITION: {
                AnalysisCaseDefinition analysisCaseDefinition = (AnalysisCaseDefinition)theEObject;
                T result = caseAnalysisCaseDefinition(analysisCaseDefinition);
                if (result == null) result = caseCaseDefinition(analysisCaseDefinition);
                if (result == null) result = caseCalculationDefinition(analysisCaseDefinition);
                if (result == null) result = caseActionDefinition(analysisCaseDefinition);
                if (result == null) result = caseFunction(analysisCaseDefinition);
                if (result == null) result = caseOccurrenceDefinition(analysisCaseDefinition);
                if (result == null) result = caseBehavior(analysisCaseDefinition);
                if (result == null) result = caseDefinition(analysisCaseDefinition);
                if (result == null) result = caseClass(analysisCaseDefinition);
                if (result == null) result = caseClassifier(analysisCaseDefinition);
                if (result == null) result = caseType(analysisCaseDefinition);
                if (result == null) result = caseNamespace(analysisCaseDefinition);
                if (result == null) result = caseElement(analysisCaseDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ANALYSIS_CASE_USAGE: {
                AnalysisCaseUsage analysisCaseUsage = (AnalysisCaseUsage)theEObject;
                T result = caseAnalysisCaseUsage(analysisCaseUsage);
                if (result == null) result = caseCaseUsage(analysisCaseUsage);
                if (result == null) result = caseCalculationUsage(analysisCaseUsage);
                if (result == null) result = caseActionUsage(analysisCaseUsage);
                if (result == null) result = caseExpression(analysisCaseUsage);
                if (result == null) result = caseOccurrenceUsage(analysisCaseUsage);
                if (result == null) result = caseStep(analysisCaseUsage);
                if (result == null) result = caseUsage(analysisCaseUsage);
                if (result == null) result = caseFeature(analysisCaseUsage);
                if (result == null) result = caseType(analysisCaseUsage);
                if (result == null) result = caseNamespace(analysisCaseUsage);
                if (result == null) result = caseElement(analysisCaseUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ANNOTATING_ELEMENT: {
                AnnotatingElement annotatingElement = (AnnotatingElement)theEObject;
                T result = caseAnnotatingElement(annotatingElement);
                if (result == null) result = caseElement(annotatingElement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ANNOTATION: {
                Annotation annotation = (Annotation)theEObject;
                T result = caseAnnotation(annotation);
                if (result == null) result = caseRelationship(annotation);
                if (result == null) result = caseElement(annotation);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ASSERT_CONSTRAINT_USAGE: {
                AssertConstraintUsage assertConstraintUsage = (AssertConstraintUsage)theEObject;
                T result = caseAssertConstraintUsage(assertConstraintUsage);
                if (result == null) result = caseConstraintUsage(assertConstraintUsage);
                if (result == null) result = caseInvariant(assertConstraintUsage);
                if (result == null) result = caseOccurrenceUsage(assertConstraintUsage);
                if (result == null) result = caseBooleanExpression(assertConstraintUsage);
                if (result == null) result = caseUsage(assertConstraintUsage);
                if (result == null) result = caseExpression(assertConstraintUsage);
                if (result == null) result = caseStep(assertConstraintUsage);
                if (result == null) result = caseFeature(assertConstraintUsage);
                if (result == null) result = caseType(assertConstraintUsage);
                if (result == null) result = caseNamespace(assertConstraintUsage);
                if (result == null) result = caseElement(assertConstraintUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ASSIGNMENT_ACTION_USAGE: {
                AssignmentActionUsage assignmentActionUsage = (AssignmentActionUsage)theEObject;
                T result = caseAssignmentActionUsage(assignmentActionUsage);
                if (result == null) result = caseActionUsage(assignmentActionUsage);
                if (result == null) result = caseOccurrenceUsage(assignmentActionUsage);
                if (result == null) result = caseStep(assignmentActionUsage);
                if (result == null) result = caseUsage(assignmentActionUsage);
                if (result == null) result = caseFeature(assignmentActionUsage);
                if (result == null) result = caseType(assignmentActionUsage);
                if (result == null) result = caseNamespace(assignmentActionUsage);
                if (result == null) result = caseElement(assignmentActionUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ASSOCIATION: {
                Association association = (Association)theEObject;
                T result = caseAssociation(association);
                if (result == null) result = caseClassifier(association);
                if (result == null) result = caseRelationship(association);
                if (result == null) result = caseType(association);
                if (result == null) result = caseNamespace(association);
                if (result == null) result = caseElement(association);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ASSOCIATION_STRUCTURE: {
                AssociationStructure associationStructure = (AssociationStructure)theEObject;
                T result = caseAssociationStructure(associationStructure);
                if (result == null) result = caseAssociation(associationStructure);
                if (result == null) result = caseStructure(associationStructure);
                if (result == null) result = caseRelationship(associationStructure);
                if (result == null) result = caseClass(associationStructure);
                if (result == null) result = caseClassifier(associationStructure);
                if (result == null) result = caseType(associationStructure);
                if (result == null) result = caseNamespace(associationStructure);
                if (result == null) result = caseElement(associationStructure);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ATTRIBUTE_DEFINITION: {
                AttributeDefinition attributeDefinition = (AttributeDefinition)theEObject;
                T result = caseAttributeDefinition(attributeDefinition);
                if (result == null) result = caseDefinition(attributeDefinition);
                if (result == null) result = caseDataType(attributeDefinition);
                if (result == null) result = caseClassifier(attributeDefinition);
                if (result == null) result = caseType(attributeDefinition);
                if (result == null) result = caseNamespace(attributeDefinition);
                if (result == null) result = caseElement(attributeDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ATTRIBUTE_USAGE: {
                AttributeUsage attributeUsage = (AttributeUsage)theEObject;
                T result = caseAttributeUsage(attributeUsage);
                if (result == null) result = caseUsage(attributeUsage);
                if (result == null) result = caseFeature(attributeUsage);
                if (result == null) result = caseType(attributeUsage);
                if (result == null) result = caseNamespace(attributeUsage);
                if (result == null) result = caseElement(attributeUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.BEHAVIOR: {
                Behavior behavior = (Behavior)theEObject;
                T result = caseBehavior(behavior);
                if (result == null) result = caseClass(behavior);
                if (result == null) result = caseClassifier(behavior);
                if (result == null) result = caseType(behavior);
                if (result == null) result = caseNamespace(behavior);
                if (result == null) result = caseElement(behavior);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.BINDING_CONNECTOR: {
                BindingConnector bindingConnector = (BindingConnector)theEObject;
                T result = caseBindingConnector(bindingConnector);
                if (result == null) result = caseConnector(bindingConnector);
                if (result == null) result = caseFeature(bindingConnector);
                if (result == null) result = caseRelationship(bindingConnector);
                if (result == null) result = caseType(bindingConnector);
                if (result == null) result = caseNamespace(bindingConnector);
                if (result == null) result = caseElement(bindingConnector);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.BINDING_CONNECTOR_AS_USAGE: {
                BindingConnectorAsUsage bindingConnectorAsUsage = (BindingConnectorAsUsage)theEObject;
                T result = caseBindingConnectorAsUsage(bindingConnectorAsUsage);
                if (result == null) result = caseConnectorAsUsage(bindingConnectorAsUsage);
                if (result == null) result = caseBindingConnector(bindingConnectorAsUsage);
                if (result == null) result = caseUsage(bindingConnectorAsUsage);
                if (result == null) result = caseConnector(bindingConnectorAsUsage);
                if (result == null) result = caseFeature(bindingConnectorAsUsage);
                if (result == null) result = caseRelationship(bindingConnectorAsUsage);
                if (result == null) result = caseType(bindingConnectorAsUsage);
                if (result == null) result = caseNamespace(bindingConnectorAsUsage);
                if (result == null) result = caseElement(bindingConnectorAsUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.BOOLEAN_EXPRESSION: {
                BooleanExpression booleanExpression = (BooleanExpression)theEObject;
                T result = caseBooleanExpression(booleanExpression);
                if (result == null) result = caseExpression(booleanExpression);
                if (result == null) result = caseStep(booleanExpression);
                if (result == null) result = caseFeature(booleanExpression);
                if (result == null) result = caseType(booleanExpression);
                if (result == null) result = caseNamespace(booleanExpression);
                if (result == null) result = caseElement(booleanExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CALCULATION_DEFINITION: {
                CalculationDefinition calculationDefinition = (CalculationDefinition)theEObject;
                T result = caseCalculationDefinition(calculationDefinition);
                if (result == null) result = caseActionDefinition(calculationDefinition);
                if (result == null) result = caseFunction(calculationDefinition);
                if (result == null) result = caseOccurrenceDefinition(calculationDefinition);
                if (result == null) result = caseBehavior(calculationDefinition);
                if (result == null) result = caseDefinition(calculationDefinition);
                if (result == null) result = caseClass(calculationDefinition);
                if (result == null) result = caseClassifier(calculationDefinition);
                if (result == null) result = caseType(calculationDefinition);
                if (result == null) result = caseNamespace(calculationDefinition);
                if (result == null) result = caseElement(calculationDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CALCULATION_USAGE: {
                CalculationUsage calculationUsage = (CalculationUsage)theEObject;
                T result = caseCalculationUsage(calculationUsage);
                if (result == null) result = caseActionUsage(calculationUsage);
                if (result == null) result = caseExpression(calculationUsage);
                if (result == null) result = caseOccurrenceUsage(calculationUsage);
                if (result == null) result = caseStep(calculationUsage);
                if (result == null) result = caseUsage(calculationUsage);
                if (result == null) result = caseFeature(calculationUsage);
                if (result == null) result = caseType(calculationUsage);
                if (result == null) result = caseNamespace(calculationUsage);
                if (result == null) result = caseElement(calculationUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CASE_DEFINITION: {
                CaseDefinition caseDefinition = (CaseDefinition)theEObject;
                T result = caseCaseDefinition(caseDefinition);
                if (result == null) result = caseCalculationDefinition(caseDefinition);
                if (result == null) result = caseActionDefinition(caseDefinition);
                if (result == null) result = caseFunction(caseDefinition);
                if (result == null) result = caseOccurrenceDefinition(caseDefinition);
                if (result == null) result = caseBehavior(caseDefinition);
                if (result == null) result = caseDefinition(caseDefinition);
                if (result == null) result = caseClass(caseDefinition);
                if (result == null) result = caseClassifier(caseDefinition);
                if (result == null) result = caseType(caseDefinition);
                if (result == null) result = caseNamespace(caseDefinition);
                if (result == null) result = caseElement(caseDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CASE_USAGE: {
                CaseUsage caseUsage = (CaseUsage)theEObject;
                T result = caseCaseUsage(caseUsage);
                if (result == null) result = caseCalculationUsage(caseUsage);
                if (result == null) result = caseActionUsage(caseUsage);
                if (result == null) result = caseExpression(caseUsage);
                if (result == null) result = caseOccurrenceUsage(caseUsage);
                if (result == null) result = caseStep(caseUsage);
                if (result == null) result = caseUsage(caseUsage);
                if (result == null) result = caseFeature(caseUsage);
                if (result == null) result = caseType(caseUsage);
                if (result == null) result = caseNamespace(caseUsage);
                if (result == null) result = caseElement(caseUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CLASS: {
                org.eclipse.syson.sysml.Class class_ = (org.eclipse.syson.sysml.Class)theEObject;
                T result = caseClass(class_);
                if (result == null) result = caseClassifier(class_);
                if (result == null) result = caseType(class_);
                if (result == null) result = caseNamespace(class_);
                if (result == null) result = caseElement(class_);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CLASSIFIER: {
                Classifier classifier = (Classifier)theEObject;
                T result = caseClassifier(classifier);
                if (result == null) result = caseType(classifier);
                if (result == null) result = caseNamespace(classifier);
                if (result == null) result = caseElement(classifier);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.COLLECT_EXPRESSION: {
                CollectExpression collectExpression = (CollectExpression)theEObject;
                T result = caseCollectExpression(collectExpression);
                if (result == null) result = caseOperatorExpression(collectExpression);
                if (result == null) result = caseInvocationExpression(collectExpression);
                if (result == null) result = caseExpression(collectExpression);
                if (result == null) result = caseStep(collectExpression);
                if (result == null) result = caseFeature(collectExpression);
                if (result == null) result = caseType(collectExpression);
                if (result == null) result = caseNamespace(collectExpression);
                if (result == null) result = caseElement(collectExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.COMMENT: {
                Comment comment = (Comment)theEObject;
                T result = caseComment(comment);
                if (result == null) result = caseAnnotatingElement(comment);
                if (result == null) result = caseElement(comment);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CONCERN_DEFINITION: {
                ConcernDefinition concernDefinition = (ConcernDefinition)theEObject;
                T result = caseConcernDefinition(concernDefinition);
                if (result == null) result = caseRequirementDefinition(concernDefinition);
                if (result == null) result = caseConstraintDefinition(concernDefinition);
                if (result == null) result = caseOccurrenceDefinition(concernDefinition);
                if (result == null) result = casePredicate(concernDefinition);
                if (result == null) result = caseDefinition(concernDefinition);
                if (result == null) result = caseFunction(concernDefinition);
                if (result == null) result = caseClassifier(concernDefinition);
                if (result == null) result = caseBehavior(concernDefinition);
                if (result == null) result = caseType(concernDefinition);
                if (result == null) result = caseClass(concernDefinition);
                if (result == null) result = caseNamespace(concernDefinition);
                if (result == null) result = caseElement(concernDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CONCERN_USAGE: {
                ConcernUsage concernUsage = (ConcernUsage)theEObject;
                T result = caseConcernUsage(concernUsage);
                if (result == null) result = caseRequirementUsage(concernUsage);
                if (result == null) result = caseConstraintUsage(concernUsage);
                if (result == null) result = caseOccurrenceUsage(concernUsage);
                if (result == null) result = caseBooleanExpression(concernUsage);
                if (result == null) result = caseUsage(concernUsage);
                if (result == null) result = caseExpression(concernUsage);
                if (result == null) result = caseStep(concernUsage);
                if (result == null) result = caseFeature(concernUsage);
                if (result == null) result = caseType(concernUsage);
                if (result == null) result = caseNamespace(concernUsage);
                if (result == null) result = caseElement(concernUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CONJUGATED_PORT_DEFINITION: {
                ConjugatedPortDefinition conjugatedPortDefinition = (ConjugatedPortDefinition)theEObject;
                T result = caseConjugatedPortDefinition(conjugatedPortDefinition);
                if (result == null) result = casePortDefinition(conjugatedPortDefinition);
                if (result == null) result = caseOccurrenceDefinition(conjugatedPortDefinition);
                if (result == null) result = caseStructure(conjugatedPortDefinition);
                if (result == null) result = caseDefinition(conjugatedPortDefinition);
                if (result == null) result = caseClass(conjugatedPortDefinition);
                if (result == null) result = caseClassifier(conjugatedPortDefinition);
                if (result == null) result = caseType(conjugatedPortDefinition);
                if (result == null) result = caseNamespace(conjugatedPortDefinition);
                if (result == null) result = caseElement(conjugatedPortDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CONJUGATED_PORT_TYPING: {
                ConjugatedPortTyping conjugatedPortTyping = (ConjugatedPortTyping)theEObject;
                T result = caseConjugatedPortTyping(conjugatedPortTyping);
                if (result == null) result = caseFeatureTyping(conjugatedPortTyping);
                if (result == null) result = caseSpecialization(conjugatedPortTyping);
                if (result == null) result = caseRelationship(conjugatedPortTyping);
                if (result == null) result = caseElement(conjugatedPortTyping);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CONJUGATION: {
                Conjugation conjugation = (Conjugation)theEObject;
                T result = caseConjugation(conjugation);
                if (result == null) result = caseRelationship(conjugation);
                if (result == null) result = caseElement(conjugation);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CONNECTION_DEFINITION: {
                ConnectionDefinition connectionDefinition = (ConnectionDefinition)theEObject;
                T result = caseConnectionDefinition(connectionDefinition);
                if (result == null) result = casePartDefinition(connectionDefinition);
                if (result == null) result = caseAssociationStructure(connectionDefinition);
                if (result == null) result = caseItemDefinition(connectionDefinition);
                if (result == null) result = caseAssociation(connectionDefinition);
                if (result == null) result = caseOccurrenceDefinition(connectionDefinition);
                if (result == null) result = caseStructure(connectionDefinition);
                if (result == null) result = caseRelationship(connectionDefinition);
                if (result == null) result = caseDefinition(connectionDefinition);
                if (result == null) result = caseClass(connectionDefinition);
                if (result == null) result = caseClassifier(connectionDefinition);
                if (result == null) result = caseType(connectionDefinition);
                if (result == null) result = caseNamespace(connectionDefinition);
                if (result == null) result = caseElement(connectionDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CONNECTION_USAGE: {
                ConnectionUsage connectionUsage = (ConnectionUsage)theEObject;
                T result = caseConnectionUsage(connectionUsage);
                if (result == null) result = caseConnectorAsUsage(connectionUsage);
                if (result == null) result = casePartUsage(connectionUsage);
                if (result == null) result = caseConnector(connectionUsage);
                if (result == null) result = caseItemUsage(connectionUsage);
                if (result == null) result = caseFeature(connectionUsage);
                if (result == null) result = caseRelationship(connectionUsage);
                if (result == null) result = caseOccurrenceUsage(connectionUsage);
                if (result == null) result = caseUsage(connectionUsage);
                if (result == null) result = caseType(connectionUsage);
                if (result == null) result = caseNamespace(connectionUsage);
                if (result == null) result = caseElement(connectionUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CONNECTOR: {
                Connector connector = (Connector)theEObject;
                T result = caseConnector(connector);
                if (result == null) result = caseFeature(connector);
                if (result == null) result = caseRelationship(connector);
                if (result == null) result = caseType(connector);
                if (result == null) result = caseNamespace(connector);
                if (result == null) result = caseElement(connector);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CONNECTOR_AS_USAGE: {
                ConnectorAsUsage connectorAsUsage = (ConnectorAsUsage)theEObject;
                T result = caseConnectorAsUsage(connectorAsUsage);
                if (result == null) result = caseUsage(connectorAsUsage);
                if (result == null) result = caseConnector(connectorAsUsage);
                if (result == null) result = caseFeature(connectorAsUsage);
                if (result == null) result = caseRelationship(connectorAsUsage);
                if (result == null) result = caseType(connectorAsUsage);
                if (result == null) result = caseNamespace(connectorAsUsage);
                if (result == null) result = caseElement(connectorAsUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CONSTRAINT_DEFINITION: {
                ConstraintDefinition constraintDefinition = (ConstraintDefinition)theEObject;
                T result = caseConstraintDefinition(constraintDefinition);
                if (result == null) result = caseOccurrenceDefinition(constraintDefinition);
                if (result == null) result = casePredicate(constraintDefinition);
                if (result == null) result = caseDefinition(constraintDefinition);
                if (result == null) result = caseFunction(constraintDefinition);
                if (result == null) result = caseClassifier(constraintDefinition);
                if (result == null) result = caseBehavior(constraintDefinition);
                if (result == null) result = caseType(constraintDefinition);
                if (result == null) result = caseClass(constraintDefinition);
                if (result == null) result = caseNamespace(constraintDefinition);
                if (result == null) result = caseElement(constraintDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CONSTRAINT_USAGE: {
                ConstraintUsage constraintUsage = (ConstraintUsage)theEObject;
                T result = caseConstraintUsage(constraintUsage);
                if (result == null) result = caseOccurrenceUsage(constraintUsage);
                if (result == null) result = caseBooleanExpression(constraintUsage);
                if (result == null) result = caseUsage(constraintUsage);
                if (result == null) result = caseExpression(constraintUsage);
                if (result == null) result = caseStep(constraintUsage);
                if (result == null) result = caseFeature(constraintUsage);
                if (result == null) result = caseType(constraintUsage);
                if (result == null) result = caseNamespace(constraintUsage);
                if (result == null) result = caseElement(constraintUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.CONTROL_NODE: {
                ControlNode controlNode = (ControlNode)theEObject;
                T result = caseControlNode(controlNode);
                if (result == null) result = caseActionUsage(controlNode);
                if (result == null) result = caseOccurrenceUsage(controlNode);
                if (result == null) result = caseStep(controlNode);
                if (result == null) result = caseUsage(controlNode);
                if (result == null) result = caseFeature(controlNode);
                if (result == null) result = caseType(controlNode);
                if (result == null) result = caseNamespace(controlNode);
                if (result == null) result = caseElement(controlNode);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.DATA_TYPE: {
                DataType dataType = (DataType)theEObject;
                T result = caseDataType(dataType);
                if (result == null) result = caseClassifier(dataType);
                if (result == null) result = caseType(dataType);
                if (result == null) result = caseNamespace(dataType);
                if (result == null) result = caseElement(dataType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.DECISION_NODE: {
                DecisionNode decisionNode = (DecisionNode)theEObject;
                T result = caseDecisionNode(decisionNode);
                if (result == null) result = caseControlNode(decisionNode);
                if (result == null) result = caseActionUsage(decisionNode);
                if (result == null) result = caseOccurrenceUsage(decisionNode);
                if (result == null) result = caseStep(decisionNode);
                if (result == null) result = caseUsage(decisionNode);
                if (result == null) result = caseFeature(decisionNode);
                if (result == null) result = caseType(decisionNode);
                if (result == null) result = caseNamespace(decisionNode);
                if (result == null) result = caseElement(decisionNode);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.DEFINITION: {
                Definition definition = (Definition)theEObject;
                T result = caseDefinition(definition);
                if (result == null) result = caseClassifier(definition);
                if (result == null) result = caseType(definition);
                if (result == null) result = caseNamespace(definition);
                if (result == null) result = caseElement(definition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.DEPENDENCY: {
                Dependency dependency = (Dependency)theEObject;
                T result = caseDependency(dependency);
                if (result == null) result = caseRelationship(dependency);
                if (result == null) result = caseElement(dependency);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.DIFFERENCING: {
                Differencing differencing = (Differencing)theEObject;
                T result = caseDifferencing(differencing);
                if (result == null) result = caseRelationship(differencing);
                if (result == null) result = caseElement(differencing);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.DISJOINING: {
                Disjoining disjoining = (Disjoining)theEObject;
                T result = caseDisjoining(disjoining);
                if (result == null) result = caseRelationship(disjoining);
                if (result == null) result = caseElement(disjoining);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.DOCUMENTATION: {
                Documentation documentation = (Documentation)theEObject;
                T result = caseDocumentation(documentation);
                if (result == null) result = caseComment(documentation);
                if (result == null) result = caseAnnotatingElement(documentation);
                if (result == null) result = caseElement(documentation);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ELEMENT: {
                Element element = (Element)theEObject;
                T result = caseElement(element);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ELEMENT_FILTER_MEMBERSHIP: {
                ElementFilterMembership elementFilterMembership = (ElementFilterMembership)theEObject;
                T result = caseElementFilterMembership(elementFilterMembership);
                if (result == null) result = caseOwningMembership(elementFilterMembership);
                if (result == null) result = caseMembership(elementFilterMembership);
                if (result == null) result = caseRelationship(elementFilterMembership);
                if (result == null) result = caseElement(elementFilterMembership);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.END_FEATURE_MEMBERSHIP: {
                EndFeatureMembership endFeatureMembership = (EndFeatureMembership)theEObject;
                T result = caseEndFeatureMembership(endFeatureMembership);
                if (result == null) result = caseFeatureMembership(endFeatureMembership);
                if (result == null) result = caseOwningMembership(endFeatureMembership);
                if (result == null) result = caseFeaturing(endFeatureMembership);
                if (result == null) result = caseMembership(endFeatureMembership);
                if (result == null) result = caseRelationship(endFeatureMembership);
                if (result == null) result = caseElement(endFeatureMembership);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ENUMERATION_DEFINITION: {
                EnumerationDefinition enumerationDefinition = (EnumerationDefinition)theEObject;
                T result = caseEnumerationDefinition(enumerationDefinition);
                if (result == null) result = caseAttributeDefinition(enumerationDefinition);
                if (result == null) result = caseDefinition(enumerationDefinition);
                if (result == null) result = caseDataType(enumerationDefinition);
                if (result == null) result = caseClassifier(enumerationDefinition);
                if (result == null) result = caseType(enumerationDefinition);
                if (result == null) result = caseNamespace(enumerationDefinition);
                if (result == null) result = caseElement(enumerationDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ENUMERATION_USAGE: {
                EnumerationUsage enumerationUsage = (EnumerationUsage)theEObject;
                T result = caseEnumerationUsage(enumerationUsage);
                if (result == null) result = caseAttributeUsage(enumerationUsage);
                if (result == null) result = caseUsage(enumerationUsage);
                if (result == null) result = caseFeature(enumerationUsage);
                if (result == null) result = caseType(enumerationUsage);
                if (result == null) result = caseNamespace(enumerationUsage);
                if (result == null) result = caseElement(enumerationUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.EVENT_OCCURRENCE_USAGE: {
                EventOccurrenceUsage eventOccurrenceUsage = (EventOccurrenceUsage)theEObject;
                T result = caseEventOccurrenceUsage(eventOccurrenceUsage);
                if (result == null) result = caseOccurrenceUsage(eventOccurrenceUsage);
                if (result == null) result = caseUsage(eventOccurrenceUsage);
                if (result == null) result = caseFeature(eventOccurrenceUsage);
                if (result == null) result = caseType(eventOccurrenceUsage);
                if (result == null) result = caseNamespace(eventOccurrenceUsage);
                if (result == null) result = caseElement(eventOccurrenceUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.EXHIBIT_STATE_USAGE: {
                ExhibitStateUsage exhibitStateUsage = (ExhibitStateUsage)theEObject;
                T result = caseExhibitStateUsage(exhibitStateUsage);
                if (result == null) result = caseStateUsage(exhibitStateUsage);
                if (result == null) result = casePerformActionUsage(exhibitStateUsage);
                if (result == null) result = caseActionUsage(exhibitStateUsage);
                if (result == null) result = caseEventOccurrenceUsage(exhibitStateUsage);
                if (result == null) result = caseOccurrenceUsage(exhibitStateUsage);
                if (result == null) result = caseStep(exhibitStateUsage);
                if (result == null) result = caseUsage(exhibitStateUsage);
                if (result == null) result = caseFeature(exhibitStateUsage);
                if (result == null) result = caseType(exhibitStateUsage);
                if (result == null) result = caseNamespace(exhibitStateUsage);
                if (result == null) result = caseElement(exhibitStateUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.EXPOSE: {
                Expose expose = (Expose)theEObject;
                T result = caseExpose(expose);
                if (result == null) result = caseImport(expose);
                if (result == null) result = caseRelationship(expose);
                if (result == null) result = caseElement(expose);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.EXPRESSION: {
                Expression expression = (Expression)theEObject;
                T result = caseExpression(expression);
                if (result == null) result = caseStep(expression);
                if (result == null) result = caseFeature(expression);
                if (result == null) result = caseType(expression);
                if (result == null) result = caseNamespace(expression);
                if (result == null) result = caseElement(expression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FEATURE: {
                Feature feature = (Feature)theEObject;
                T result = caseFeature(feature);
                if (result == null) result = caseType(feature);
                if (result == null) result = caseNamespace(feature);
                if (result == null) result = caseElement(feature);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FEATURE_CHAIN_EXPRESSION: {
                FeatureChainExpression featureChainExpression = (FeatureChainExpression)theEObject;
                T result = caseFeatureChainExpression(featureChainExpression);
                if (result == null) result = caseOperatorExpression(featureChainExpression);
                if (result == null) result = caseInvocationExpression(featureChainExpression);
                if (result == null) result = caseExpression(featureChainExpression);
                if (result == null) result = caseStep(featureChainExpression);
                if (result == null) result = caseFeature(featureChainExpression);
                if (result == null) result = caseType(featureChainExpression);
                if (result == null) result = caseNamespace(featureChainExpression);
                if (result == null) result = caseElement(featureChainExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FEATURE_CHAINING: {
                FeatureChaining featureChaining = (FeatureChaining)theEObject;
                T result = caseFeatureChaining(featureChaining);
                if (result == null) result = caseRelationship(featureChaining);
                if (result == null) result = caseElement(featureChaining);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FEATURE_INVERTING: {
                FeatureInverting featureInverting = (FeatureInverting)theEObject;
                T result = caseFeatureInverting(featureInverting);
                if (result == null) result = caseRelationship(featureInverting);
                if (result == null) result = caseElement(featureInverting);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FEATURE_MEMBERSHIP: {
                FeatureMembership featureMembership = (FeatureMembership)theEObject;
                T result = caseFeatureMembership(featureMembership);
                if (result == null) result = caseOwningMembership(featureMembership);
                if (result == null) result = caseFeaturing(featureMembership);
                if (result == null) result = caseMembership(featureMembership);
                if (result == null) result = caseRelationship(featureMembership);
                if (result == null) result = caseElement(featureMembership);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FEATURE_REFERENCE_EXPRESSION: {
                FeatureReferenceExpression featureReferenceExpression = (FeatureReferenceExpression)theEObject;
                T result = caseFeatureReferenceExpression(featureReferenceExpression);
                if (result == null) result = caseExpression(featureReferenceExpression);
                if (result == null) result = caseStep(featureReferenceExpression);
                if (result == null) result = caseFeature(featureReferenceExpression);
                if (result == null) result = caseType(featureReferenceExpression);
                if (result == null) result = caseNamespace(featureReferenceExpression);
                if (result == null) result = caseElement(featureReferenceExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FEATURE_TYPING: {
                FeatureTyping featureTyping = (FeatureTyping)theEObject;
                T result = caseFeatureTyping(featureTyping);
                if (result == null) result = caseSpecialization(featureTyping);
                if (result == null) result = caseRelationship(featureTyping);
                if (result == null) result = caseElement(featureTyping);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FEATURE_VALUE: {
                FeatureValue featureValue = (FeatureValue)theEObject;
                T result = caseFeatureValue(featureValue);
                if (result == null) result = caseOwningMembership(featureValue);
                if (result == null) result = caseMembership(featureValue);
                if (result == null) result = caseRelationship(featureValue);
                if (result == null) result = caseElement(featureValue);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FEATURING: {
                Featuring featuring = (Featuring)theEObject;
                T result = caseFeaturing(featuring);
                if (result == null) result = caseRelationship(featuring);
                if (result == null) result = caseElement(featuring);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FLOW_CONNECTION_DEFINITION: {
                FlowConnectionDefinition flowConnectionDefinition = (FlowConnectionDefinition)theEObject;
                T result = caseFlowConnectionDefinition(flowConnectionDefinition);
                if (result == null) result = caseConnectionDefinition(flowConnectionDefinition);
                if (result == null) result = caseActionDefinition(flowConnectionDefinition);
                if (result == null) result = caseInteraction(flowConnectionDefinition);
                if (result == null) result = casePartDefinition(flowConnectionDefinition);
                if (result == null) result = caseAssociationStructure(flowConnectionDefinition);
                if (result == null) result = caseBehavior(flowConnectionDefinition);
                if (result == null) result = caseItemDefinition(flowConnectionDefinition);
                if (result == null) result = caseAssociation(flowConnectionDefinition);
                if (result == null) result = caseOccurrenceDefinition(flowConnectionDefinition);
                if (result == null) result = caseStructure(flowConnectionDefinition);
                if (result == null) result = caseRelationship(flowConnectionDefinition);
                if (result == null) result = caseDefinition(flowConnectionDefinition);
                if (result == null) result = caseClass(flowConnectionDefinition);
                if (result == null) result = caseClassifier(flowConnectionDefinition);
                if (result == null) result = caseType(flowConnectionDefinition);
                if (result == null) result = caseNamespace(flowConnectionDefinition);
                if (result == null) result = caseElement(flowConnectionDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FLOW_CONNECTION_USAGE: {
                FlowConnectionUsage flowConnectionUsage = (FlowConnectionUsage)theEObject;
                T result = caseFlowConnectionUsage(flowConnectionUsage);
                if (result == null) result = caseConnectionUsage(flowConnectionUsage);
                if (result == null) result = caseActionUsage(flowConnectionUsage);
                if (result == null) result = caseItemFlow(flowConnectionUsage);
                if (result == null) result = caseConnectorAsUsage(flowConnectionUsage);
                if (result == null) result = casePartUsage(flowConnectionUsage);
                if (result == null) result = caseStep(flowConnectionUsage);
                if (result == null) result = caseConnector(flowConnectionUsage);
                if (result == null) result = caseItemUsage(flowConnectionUsage);
                if (result == null) result = caseFeature(flowConnectionUsage);
                if (result == null) result = caseRelationship(flowConnectionUsage);
                if (result == null) result = caseOccurrenceUsage(flowConnectionUsage);
                if (result == null) result = caseUsage(flowConnectionUsage);
                if (result == null) result = caseType(flowConnectionUsage);
                if (result == null) result = caseNamespace(flowConnectionUsage);
                if (result == null) result = caseElement(flowConnectionUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FORK_NODE: {
                ForkNode forkNode = (ForkNode)theEObject;
                T result = caseForkNode(forkNode);
                if (result == null) result = caseControlNode(forkNode);
                if (result == null) result = caseActionUsage(forkNode);
                if (result == null) result = caseOccurrenceUsage(forkNode);
                if (result == null) result = caseStep(forkNode);
                if (result == null) result = caseUsage(forkNode);
                if (result == null) result = caseFeature(forkNode);
                if (result == null) result = caseType(forkNode);
                if (result == null) result = caseNamespace(forkNode);
                if (result == null) result = caseElement(forkNode);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FOR_LOOP_ACTION_USAGE: {
                ForLoopActionUsage forLoopActionUsage = (ForLoopActionUsage)theEObject;
                T result = caseForLoopActionUsage(forLoopActionUsage);
                if (result == null) result = caseLoopActionUsage(forLoopActionUsage);
                if (result == null) result = caseActionUsage(forLoopActionUsage);
                if (result == null) result = caseOccurrenceUsage(forLoopActionUsage);
                if (result == null) result = caseStep(forLoopActionUsage);
                if (result == null) result = caseUsage(forLoopActionUsage);
                if (result == null) result = caseFeature(forLoopActionUsage);
                if (result == null) result = caseType(forLoopActionUsage);
                if (result == null) result = caseNamespace(forLoopActionUsage);
                if (result == null) result = caseElement(forLoopActionUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FRAMED_CONCERN_MEMBERSHIP: {
                FramedConcernMembership framedConcernMembership = (FramedConcernMembership)theEObject;
                T result = caseFramedConcernMembership(framedConcernMembership);
                if (result == null) result = caseRequirementConstraintMembership(framedConcernMembership);
                if (result == null) result = caseFeatureMembership(framedConcernMembership);
                if (result == null) result = caseOwningMembership(framedConcernMembership);
                if (result == null) result = caseFeaturing(framedConcernMembership);
                if (result == null) result = caseMembership(framedConcernMembership);
                if (result == null) result = caseRelationship(framedConcernMembership);
                if (result == null) result = caseElement(framedConcernMembership);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.FUNCTION: {
                Function function = (Function)theEObject;
                T result = caseFunction(function);
                if (result == null) result = caseBehavior(function);
                if (result == null) result = caseClass(function);
                if (result == null) result = caseClassifier(function);
                if (result == null) result = caseType(function);
                if (result == null) result = caseNamespace(function);
                if (result == null) result = caseElement(function);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.IF_ACTION_USAGE: {
                IfActionUsage ifActionUsage = (IfActionUsage)theEObject;
                T result = caseIfActionUsage(ifActionUsage);
                if (result == null) result = caseActionUsage(ifActionUsage);
                if (result == null) result = caseOccurrenceUsage(ifActionUsage);
                if (result == null) result = caseStep(ifActionUsage);
                if (result == null) result = caseUsage(ifActionUsage);
                if (result == null) result = caseFeature(ifActionUsage);
                if (result == null) result = caseType(ifActionUsage);
                if (result == null) result = caseNamespace(ifActionUsage);
                if (result == null) result = caseElement(ifActionUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.IMPORT: {
                Import import_ = (Import)theEObject;
                T result = caseImport(import_);
                if (result == null) result = caseRelationship(import_);
                if (result == null) result = caseElement(import_);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.INCLUDE_USE_CASE_USAGE: {
                IncludeUseCaseUsage includeUseCaseUsage = (IncludeUseCaseUsage)theEObject;
                T result = caseIncludeUseCaseUsage(includeUseCaseUsage);
                if (result == null) result = caseUseCaseUsage(includeUseCaseUsage);
                if (result == null) result = casePerformActionUsage(includeUseCaseUsage);
                if (result == null) result = caseCaseUsage(includeUseCaseUsage);
                if (result == null) result = caseEventOccurrenceUsage(includeUseCaseUsage);
                if (result == null) result = caseCalculationUsage(includeUseCaseUsage);
                if (result == null) result = caseActionUsage(includeUseCaseUsage);
                if (result == null) result = caseExpression(includeUseCaseUsage);
                if (result == null) result = caseOccurrenceUsage(includeUseCaseUsage);
                if (result == null) result = caseStep(includeUseCaseUsage);
                if (result == null) result = caseUsage(includeUseCaseUsage);
                if (result == null) result = caseFeature(includeUseCaseUsage);
                if (result == null) result = caseType(includeUseCaseUsage);
                if (result == null) result = caseNamespace(includeUseCaseUsage);
                if (result == null) result = caseElement(includeUseCaseUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.INTERACTION: {
                Interaction interaction = (Interaction)theEObject;
                T result = caseInteraction(interaction);
                if (result == null) result = caseAssociation(interaction);
                if (result == null) result = caseBehavior(interaction);
                if (result == null) result = caseRelationship(interaction);
                if (result == null) result = caseClass(interaction);
                if (result == null) result = caseClassifier(interaction);
                if (result == null) result = caseType(interaction);
                if (result == null) result = caseNamespace(interaction);
                if (result == null) result = caseElement(interaction);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.INTERFACE_DEFINITION: {
                InterfaceDefinition interfaceDefinition = (InterfaceDefinition)theEObject;
                T result = caseInterfaceDefinition(interfaceDefinition);
                if (result == null) result = caseConnectionDefinition(interfaceDefinition);
                if (result == null) result = casePartDefinition(interfaceDefinition);
                if (result == null) result = caseAssociationStructure(interfaceDefinition);
                if (result == null) result = caseItemDefinition(interfaceDefinition);
                if (result == null) result = caseAssociation(interfaceDefinition);
                if (result == null) result = caseOccurrenceDefinition(interfaceDefinition);
                if (result == null) result = caseStructure(interfaceDefinition);
                if (result == null) result = caseRelationship(interfaceDefinition);
                if (result == null) result = caseDefinition(interfaceDefinition);
                if (result == null) result = caseClass(interfaceDefinition);
                if (result == null) result = caseClassifier(interfaceDefinition);
                if (result == null) result = caseType(interfaceDefinition);
                if (result == null) result = caseNamespace(interfaceDefinition);
                if (result == null) result = caseElement(interfaceDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.INTERFACE_USAGE: {
                InterfaceUsage interfaceUsage = (InterfaceUsage)theEObject;
                T result = caseInterfaceUsage(interfaceUsage);
                if (result == null) result = caseConnectionUsage(interfaceUsage);
                if (result == null) result = caseConnectorAsUsage(interfaceUsage);
                if (result == null) result = casePartUsage(interfaceUsage);
                if (result == null) result = caseConnector(interfaceUsage);
                if (result == null) result = caseItemUsage(interfaceUsage);
                if (result == null) result = caseFeature(interfaceUsage);
                if (result == null) result = caseRelationship(interfaceUsage);
                if (result == null) result = caseOccurrenceUsage(interfaceUsage);
                if (result == null) result = caseUsage(interfaceUsage);
                if (result == null) result = caseType(interfaceUsage);
                if (result == null) result = caseNamespace(interfaceUsage);
                if (result == null) result = caseElement(interfaceUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.INTERSECTING: {
                Intersecting intersecting = (Intersecting)theEObject;
                T result = caseIntersecting(intersecting);
                if (result == null) result = caseRelationship(intersecting);
                if (result == null) result = caseElement(intersecting);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.INVARIANT: {
                Invariant invariant = (Invariant)theEObject;
                T result = caseInvariant(invariant);
                if (result == null) result = caseBooleanExpression(invariant);
                if (result == null) result = caseExpression(invariant);
                if (result == null) result = caseStep(invariant);
                if (result == null) result = caseFeature(invariant);
                if (result == null) result = caseType(invariant);
                if (result == null) result = caseNamespace(invariant);
                if (result == null) result = caseElement(invariant);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.INVOCATION_EXPRESSION: {
                InvocationExpression invocationExpression = (InvocationExpression)theEObject;
                T result = caseInvocationExpression(invocationExpression);
                if (result == null) result = caseExpression(invocationExpression);
                if (result == null) result = caseStep(invocationExpression);
                if (result == null) result = caseFeature(invocationExpression);
                if (result == null) result = caseType(invocationExpression);
                if (result == null) result = caseNamespace(invocationExpression);
                if (result == null) result = caseElement(invocationExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ITEM_DEFINITION: {
                ItemDefinition itemDefinition = (ItemDefinition)theEObject;
                T result = caseItemDefinition(itemDefinition);
                if (result == null) result = caseOccurrenceDefinition(itemDefinition);
                if (result == null) result = caseStructure(itemDefinition);
                if (result == null) result = caseDefinition(itemDefinition);
                if (result == null) result = caseClass(itemDefinition);
                if (result == null) result = caseClassifier(itemDefinition);
                if (result == null) result = caseType(itemDefinition);
                if (result == null) result = caseNamespace(itemDefinition);
                if (result == null) result = caseElement(itemDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ITEM_FEATURE: {
                ItemFeature itemFeature = (ItemFeature)theEObject;
                T result = caseItemFeature(itemFeature);
                if (result == null) result = caseFeature(itemFeature);
                if (result == null) result = caseType(itemFeature);
                if (result == null) result = caseNamespace(itemFeature);
                if (result == null) result = caseElement(itemFeature);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ITEM_FLOW: {
                ItemFlow itemFlow = (ItemFlow)theEObject;
                T result = caseItemFlow(itemFlow);
                if (result == null) result = caseConnector(itemFlow);
                if (result == null) result = caseStep(itemFlow);
                if (result == null) result = caseFeature(itemFlow);
                if (result == null) result = caseRelationship(itemFlow);
                if (result == null) result = caseType(itemFlow);
                if (result == null) result = caseNamespace(itemFlow);
                if (result == null) result = caseElement(itemFlow);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ITEM_FLOW_END: {
                ItemFlowEnd itemFlowEnd = (ItemFlowEnd)theEObject;
                T result = caseItemFlowEnd(itemFlowEnd);
                if (result == null) result = caseFeature(itemFlowEnd);
                if (result == null) result = caseType(itemFlowEnd);
                if (result == null) result = caseNamespace(itemFlowEnd);
                if (result == null) result = caseElement(itemFlowEnd);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.ITEM_USAGE: {
                ItemUsage itemUsage = (ItemUsage)theEObject;
                T result = caseItemUsage(itemUsage);
                if (result == null) result = caseOccurrenceUsage(itemUsage);
                if (result == null) result = caseUsage(itemUsage);
                if (result == null) result = caseFeature(itemUsage);
                if (result == null) result = caseType(itemUsage);
                if (result == null) result = caseNamespace(itemUsage);
                if (result == null) result = caseElement(itemUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.JOIN_NODE: {
                JoinNode joinNode = (JoinNode)theEObject;
                T result = caseJoinNode(joinNode);
                if (result == null) result = caseControlNode(joinNode);
                if (result == null) result = caseActionUsage(joinNode);
                if (result == null) result = caseOccurrenceUsage(joinNode);
                if (result == null) result = caseStep(joinNode);
                if (result == null) result = caseUsage(joinNode);
                if (result == null) result = caseFeature(joinNode);
                if (result == null) result = caseType(joinNode);
                if (result == null) result = caseNamespace(joinNode);
                if (result == null) result = caseElement(joinNode);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.LIBRARY_PACKAGE: {
                LibraryPackage libraryPackage = (LibraryPackage)theEObject;
                T result = caseLibraryPackage(libraryPackage);
                if (result == null) result = casePackage(libraryPackage);
                if (result == null) result = caseNamespace(libraryPackage);
                if (result == null) result = caseElement(libraryPackage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.LIFE_CLASS: {
                LifeClass lifeClass = (LifeClass)theEObject;
                T result = caseLifeClass(lifeClass);
                if (result == null) result = caseClass(lifeClass);
                if (result == null) result = caseClassifier(lifeClass);
                if (result == null) result = caseType(lifeClass);
                if (result == null) result = caseNamespace(lifeClass);
                if (result == null) result = caseElement(lifeClass);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.LITERAL_BOOLEAN: {
                LiteralBoolean literalBoolean = (LiteralBoolean)theEObject;
                T result = caseLiteralBoolean(literalBoolean);
                if (result == null) result = caseLiteralExpression(literalBoolean);
                if (result == null) result = caseExpression(literalBoolean);
                if (result == null) result = caseStep(literalBoolean);
                if (result == null) result = caseFeature(literalBoolean);
                if (result == null) result = caseType(literalBoolean);
                if (result == null) result = caseNamespace(literalBoolean);
                if (result == null) result = caseElement(literalBoolean);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.LITERAL_EXPRESSION: {
                LiteralExpression literalExpression = (LiteralExpression)theEObject;
                T result = caseLiteralExpression(literalExpression);
                if (result == null) result = caseExpression(literalExpression);
                if (result == null) result = caseStep(literalExpression);
                if (result == null) result = caseFeature(literalExpression);
                if (result == null) result = caseType(literalExpression);
                if (result == null) result = caseNamespace(literalExpression);
                if (result == null) result = caseElement(literalExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.LITERAL_INFINITY: {
                LiteralInfinity literalInfinity = (LiteralInfinity)theEObject;
                T result = caseLiteralInfinity(literalInfinity);
                if (result == null) result = caseLiteralExpression(literalInfinity);
                if (result == null) result = caseExpression(literalInfinity);
                if (result == null) result = caseStep(literalInfinity);
                if (result == null) result = caseFeature(literalInfinity);
                if (result == null) result = caseType(literalInfinity);
                if (result == null) result = caseNamespace(literalInfinity);
                if (result == null) result = caseElement(literalInfinity);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.LITERAL_INTEGER: {
                LiteralInteger literalInteger = (LiteralInteger)theEObject;
                T result = caseLiteralInteger(literalInteger);
                if (result == null) result = caseLiteralExpression(literalInteger);
                if (result == null) result = caseExpression(literalInteger);
                if (result == null) result = caseStep(literalInteger);
                if (result == null) result = caseFeature(literalInteger);
                if (result == null) result = caseType(literalInteger);
                if (result == null) result = caseNamespace(literalInteger);
                if (result == null) result = caseElement(literalInteger);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.LITERAL_RATIONAL: {
                LiteralRational literalRational = (LiteralRational)theEObject;
                T result = caseLiteralRational(literalRational);
                if (result == null) result = caseLiteralExpression(literalRational);
                if (result == null) result = caseExpression(literalRational);
                if (result == null) result = caseStep(literalRational);
                if (result == null) result = caseFeature(literalRational);
                if (result == null) result = caseType(literalRational);
                if (result == null) result = caseNamespace(literalRational);
                if (result == null) result = caseElement(literalRational);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.LITERAL_STRING: {
                LiteralString literalString = (LiteralString)theEObject;
                T result = caseLiteralString(literalString);
                if (result == null) result = caseLiteralExpression(literalString);
                if (result == null) result = caseExpression(literalString);
                if (result == null) result = caseStep(literalString);
                if (result == null) result = caseFeature(literalString);
                if (result == null) result = caseType(literalString);
                if (result == null) result = caseNamespace(literalString);
                if (result == null) result = caseElement(literalString);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.LOOP_ACTION_USAGE: {
                LoopActionUsage loopActionUsage = (LoopActionUsage)theEObject;
                T result = caseLoopActionUsage(loopActionUsage);
                if (result == null) result = caseActionUsage(loopActionUsage);
                if (result == null) result = caseOccurrenceUsage(loopActionUsage);
                if (result == null) result = caseStep(loopActionUsage);
                if (result == null) result = caseUsage(loopActionUsage);
                if (result == null) result = caseFeature(loopActionUsage);
                if (result == null) result = caseType(loopActionUsage);
                if (result == null) result = caseNamespace(loopActionUsage);
                if (result == null) result = caseElement(loopActionUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.MEMBERSHIP: {
                Membership membership = (Membership)theEObject;
                T result = caseMembership(membership);
                if (result == null) result = caseRelationship(membership);
                if (result == null) result = caseElement(membership);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.MEMBERSHIP_EXPOSE: {
                MembershipExpose membershipExpose = (MembershipExpose)theEObject;
                T result = caseMembershipExpose(membershipExpose);
                if (result == null) result = caseMembershipImport(membershipExpose);
                if (result == null) result = caseExpose(membershipExpose);
                if (result == null) result = caseImport(membershipExpose);
                if (result == null) result = caseRelationship(membershipExpose);
                if (result == null) result = caseElement(membershipExpose);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.MEMBERSHIP_IMPORT: {
                MembershipImport membershipImport = (MembershipImport)theEObject;
                T result = caseMembershipImport(membershipImport);
                if (result == null) result = caseImport(membershipImport);
                if (result == null) result = caseRelationship(membershipImport);
                if (result == null) result = caseElement(membershipImport);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.MERGE_NODE: {
                MergeNode mergeNode = (MergeNode)theEObject;
                T result = caseMergeNode(mergeNode);
                if (result == null) result = caseControlNode(mergeNode);
                if (result == null) result = caseActionUsage(mergeNode);
                if (result == null) result = caseOccurrenceUsage(mergeNode);
                if (result == null) result = caseStep(mergeNode);
                if (result == null) result = caseUsage(mergeNode);
                if (result == null) result = caseFeature(mergeNode);
                if (result == null) result = caseType(mergeNode);
                if (result == null) result = caseNamespace(mergeNode);
                if (result == null) result = caseElement(mergeNode);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.METACLASS: {
                Metaclass metaclass = (Metaclass)theEObject;
                T result = caseMetaclass(metaclass);
                if (result == null) result = caseStructure(metaclass);
                if (result == null) result = caseClass(metaclass);
                if (result == null) result = caseClassifier(metaclass);
                if (result == null) result = caseType(metaclass);
                if (result == null) result = caseNamespace(metaclass);
                if (result == null) result = caseElement(metaclass);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.METADATA_ACCESS_EXPRESSION: {
                MetadataAccessExpression metadataAccessExpression = (MetadataAccessExpression)theEObject;
                T result = caseMetadataAccessExpression(metadataAccessExpression);
                if (result == null) result = caseExpression(metadataAccessExpression);
                if (result == null) result = caseStep(metadataAccessExpression);
                if (result == null) result = caseFeature(metadataAccessExpression);
                if (result == null) result = caseType(metadataAccessExpression);
                if (result == null) result = caseNamespace(metadataAccessExpression);
                if (result == null) result = caseElement(metadataAccessExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.METADATA_DEFINITION: {
                MetadataDefinition metadataDefinition = (MetadataDefinition)theEObject;
                T result = caseMetadataDefinition(metadataDefinition);
                if (result == null) result = caseItemDefinition(metadataDefinition);
                if (result == null) result = caseMetaclass(metadataDefinition);
                if (result == null) result = caseOccurrenceDefinition(metadataDefinition);
                if (result == null) result = caseStructure(metadataDefinition);
                if (result == null) result = caseDefinition(metadataDefinition);
                if (result == null) result = caseClass(metadataDefinition);
                if (result == null) result = caseClassifier(metadataDefinition);
                if (result == null) result = caseType(metadataDefinition);
                if (result == null) result = caseNamespace(metadataDefinition);
                if (result == null) result = caseElement(metadataDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.METADATA_FEATURE: {
                MetadataFeature metadataFeature = (MetadataFeature)theEObject;
                T result = caseMetadataFeature(metadataFeature);
                if (result == null) result = caseFeature(metadataFeature);
                if (result == null) result = caseAnnotatingElement(metadataFeature);
                if (result == null) result = caseType(metadataFeature);
                if (result == null) result = caseNamespace(metadataFeature);
                if (result == null) result = caseElement(metadataFeature);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.METADATA_USAGE: {
                MetadataUsage metadataUsage = (MetadataUsage)theEObject;
                T result = caseMetadataUsage(metadataUsage);
                if (result == null) result = caseItemUsage(metadataUsage);
                if (result == null) result = caseMetadataFeature(metadataUsage);
                if (result == null) result = caseOccurrenceUsage(metadataUsage);
                if (result == null) result = caseAnnotatingElement(metadataUsage);
                if (result == null) result = caseUsage(metadataUsage);
                if (result == null) result = caseFeature(metadataUsage);
                if (result == null) result = caseType(metadataUsage);
                if (result == null) result = caseNamespace(metadataUsage);
                if (result == null) result = caseElement(metadataUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.MULTIPLICITY: {
                Multiplicity multiplicity = (Multiplicity)theEObject;
                T result = caseMultiplicity(multiplicity);
                if (result == null) result = caseFeature(multiplicity);
                if (result == null) result = caseType(multiplicity);
                if (result == null) result = caseNamespace(multiplicity);
                if (result == null) result = caseElement(multiplicity);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.MULTIPLICITY_RANGE: {
                MultiplicityRange multiplicityRange = (MultiplicityRange)theEObject;
                T result = caseMultiplicityRange(multiplicityRange);
                if (result == null) result = caseMultiplicity(multiplicityRange);
                if (result == null) result = caseFeature(multiplicityRange);
                if (result == null) result = caseType(multiplicityRange);
                if (result == null) result = caseNamespace(multiplicityRange);
                if (result == null) result = caseElement(multiplicityRange);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.NAMESPACE: {
                Namespace namespace = (Namespace)theEObject;
                T result = caseNamespace(namespace);
                if (result == null) result = caseElement(namespace);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.NAMESPACE_EXPOSE: {
                NamespaceExpose namespaceExpose = (NamespaceExpose)theEObject;
                T result = caseNamespaceExpose(namespaceExpose);
                if (result == null) result = caseNamespaceImport(namespaceExpose);
                if (result == null) result = caseExpose(namespaceExpose);
                if (result == null) result = caseImport(namespaceExpose);
                if (result == null) result = caseRelationship(namespaceExpose);
                if (result == null) result = caseElement(namespaceExpose);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.NAMESPACE_IMPORT: {
                NamespaceImport namespaceImport = (NamespaceImport)theEObject;
                T result = caseNamespaceImport(namespaceImport);
                if (result == null) result = caseImport(namespaceImport);
                if (result == null) result = caseRelationship(namespaceImport);
                if (result == null) result = caseElement(namespaceImport);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.NULL_EXPRESSION: {
                NullExpression nullExpression = (NullExpression)theEObject;
                T result = caseNullExpression(nullExpression);
                if (result == null) result = caseExpression(nullExpression);
                if (result == null) result = caseStep(nullExpression);
                if (result == null) result = caseFeature(nullExpression);
                if (result == null) result = caseType(nullExpression);
                if (result == null) result = caseNamespace(nullExpression);
                if (result == null) result = caseElement(nullExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.OBJECTIVE_MEMBERSHIP: {
                ObjectiveMembership objectiveMembership = (ObjectiveMembership)theEObject;
                T result = caseObjectiveMembership(objectiveMembership);
                if (result == null) result = caseFeatureMembership(objectiveMembership);
                if (result == null) result = caseOwningMembership(objectiveMembership);
                if (result == null) result = caseFeaturing(objectiveMembership);
                if (result == null) result = caseMembership(objectiveMembership);
                if (result == null) result = caseRelationship(objectiveMembership);
                if (result == null) result = caseElement(objectiveMembership);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.OCCURRENCE_DEFINITION: {
                OccurrenceDefinition occurrenceDefinition = (OccurrenceDefinition)theEObject;
                T result = caseOccurrenceDefinition(occurrenceDefinition);
                if (result == null) result = caseDefinition(occurrenceDefinition);
                if (result == null) result = caseClass(occurrenceDefinition);
                if (result == null) result = caseClassifier(occurrenceDefinition);
                if (result == null) result = caseType(occurrenceDefinition);
                if (result == null) result = caseNamespace(occurrenceDefinition);
                if (result == null) result = caseElement(occurrenceDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.OCCURRENCE_USAGE: {
                OccurrenceUsage occurrenceUsage = (OccurrenceUsage)theEObject;
                T result = caseOccurrenceUsage(occurrenceUsage);
                if (result == null) result = caseUsage(occurrenceUsage);
                if (result == null) result = caseFeature(occurrenceUsage);
                if (result == null) result = caseType(occurrenceUsage);
                if (result == null) result = caseNamespace(occurrenceUsage);
                if (result == null) result = caseElement(occurrenceUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.OPERATOR_EXPRESSION: {
                OperatorExpression operatorExpression = (OperatorExpression)theEObject;
                T result = caseOperatorExpression(operatorExpression);
                if (result == null) result = caseInvocationExpression(operatorExpression);
                if (result == null) result = caseExpression(operatorExpression);
                if (result == null) result = caseStep(operatorExpression);
                if (result == null) result = caseFeature(operatorExpression);
                if (result == null) result = caseType(operatorExpression);
                if (result == null) result = caseNamespace(operatorExpression);
                if (result == null) result = caseElement(operatorExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.OWNING_MEMBERSHIP: {
                OwningMembership owningMembership = (OwningMembership)theEObject;
                T result = caseOwningMembership(owningMembership);
                if (result == null) result = caseMembership(owningMembership);
                if (result == null) result = caseRelationship(owningMembership);
                if (result == null) result = caseElement(owningMembership);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.PACKAGE: {
                org.eclipse.syson.sysml.Package package_ = (org.eclipse.syson.sysml.Package)theEObject;
                T result = casePackage(package_);
                if (result == null) result = caseNamespace(package_);
                if (result == null) result = caseElement(package_);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.PARAMETER_MEMBERSHIP: {
                ParameterMembership parameterMembership = (ParameterMembership)theEObject;
                T result = caseParameterMembership(parameterMembership);
                if (result == null) result = caseFeatureMembership(parameterMembership);
                if (result == null) result = caseOwningMembership(parameterMembership);
                if (result == null) result = caseFeaturing(parameterMembership);
                if (result == null) result = caseMembership(parameterMembership);
                if (result == null) result = caseRelationship(parameterMembership);
                if (result == null) result = caseElement(parameterMembership);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.PART_DEFINITION: {
                PartDefinition partDefinition = (PartDefinition)theEObject;
                T result = casePartDefinition(partDefinition);
                if (result == null) result = caseItemDefinition(partDefinition);
                if (result == null) result = caseOccurrenceDefinition(partDefinition);
                if (result == null) result = caseStructure(partDefinition);
                if (result == null) result = caseDefinition(partDefinition);
                if (result == null) result = caseClass(partDefinition);
                if (result == null) result = caseClassifier(partDefinition);
                if (result == null) result = caseType(partDefinition);
                if (result == null) result = caseNamespace(partDefinition);
                if (result == null) result = caseElement(partDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.PART_USAGE: {
                PartUsage partUsage = (PartUsage)theEObject;
                T result = casePartUsage(partUsage);
                if (result == null) result = caseItemUsage(partUsage);
                if (result == null) result = caseOccurrenceUsage(partUsage);
                if (result == null) result = caseUsage(partUsage);
                if (result == null) result = caseFeature(partUsage);
                if (result == null) result = caseType(partUsage);
                if (result == null) result = caseNamespace(partUsage);
                if (result == null) result = caseElement(partUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.PERFORM_ACTION_USAGE: {
                PerformActionUsage performActionUsage = (PerformActionUsage)theEObject;
                T result = casePerformActionUsage(performActionUsage);
                if (result == null) result = caseActionUsage(performActionUsage);
                if (result == null) result = caseEventOccurrenceUsage(performActionUsage);
                if (result == null) result = caseOccurrenceUsage(performActionUsage);
                if (result == null) result = caseStep(performActionUsage);
                if (result == null) result = caseUsage(performActionUsage);
                if (result == null) result = caseFeature(performActionUsage);
                if (result == null) result = caseType(performActionUsage);
                if (result == null) result = caseNamespace(performActionUsage);
                if (result == null) result = caseElement(performActionUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.PORT_CONJUGATION: {
                PortConjugation portConjugation = (PortConjugation)theEObject;
                T result = casePortConjugation(portConjugation);
                if (result == null) result = caseConjugation(portConjugation);
                if (result == null) result = caseRelationship(portConjugation);
                if (result == null) result = caseElement(portConjugation);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.PORT_DEFINITION: {
                PortDefinition portDefinition = (PortDefinition)theEObject;
                T result = casePortDefinition(portDefinition);
                if (result == null) result = caseOccurrenceDefinition(portDefinition);
                if (result == null) result = caseStructure(portDefinition);
                if (result == null) result = caseDefinition(portDefinition);
                if (result == null) result = caseClass(portDefinition);
                if (result == null) result = caseClassifier(portDefinition);
                if (result == null) result = caseType(portDefinition);
                if (result == null) result = caseNamespace(portDefinition);
                if (result == null) result = caseElement(portDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.PORT_USAGE: {
                PortUsage portUsage = (PortUsage)theEObject;
                T result = casePortUsage(portUsage);
                if (result == null) result = caseOccurrenceUsage(portUsage);
                if (result == null) result = caseUsage(portUsage);
                if (result == null) result = caseFeature(portUsage);
                if (result == null) result = caseType(portUsage);
                if (result == null) result = caseNamespace(portUsage);
                if (result == null) result = caseElement(portUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.PREDICATE: {
                Predicate predicate = (Predicate)theEObject;
                T result = casePredicate(predicate);
                if (result == null) result = caseFunction(predicate);
                if (result == null) result = caseBehavior(predicate);
                if (result == null) result = caseClass(predicate);
                if (result == null) result = caseClassifier(predicate);
                if (result == null) result = caseType(predicate);
                if (result == null) result = caseNamespace(predicate);
                if (result == null) result = caseElement(predicate);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.REDEFINITION: {
                Redefinition redefinition = (Redefinition)theEObject;
                T result = caseRedefinition(redefinition);
                if (result == null) result = caseSubsetting(redefinition);
                if (result == null) result = caseSpecialization(redefinition);
                if (result == null) result = caseRelationship(redefinition);
                if (result == null) result = caseElement(redefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.REFERENCE_SUBSETTING: {
                ReferenceSubsetting referenceSubsetting = (ReferenceSubsetting)theEObject;
                T result = caseReferenceSubsetting(referenceSubsetting);
                if (result == null) result = caseSubsetting(referenceSubsetting);
                if (result == null) result = caseSpecialization(referenceSubsetting);
                if (result == null) result = caseRelationship(referenceSubsetting);
                if (result == null) result = caseElement(referenceSubsetting);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.REFERENCE_USAGE: {
                ReferenceUsage referenceUsage = (ReferenceUsage)theEObject;
                T result = caseReferenceUsage(referenceUsage);
                if (result == null) result = caseUsage(referenceUsage);
                if (result == null) result = caseFeature(referenceUsage);
                if (result == null) result = caseType(referenceUsage);
                if (result == null) result = caseNamespace(referenceUsage);
                if (result == null) result = caseElement(referenceUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.RELATIONSHIP: {
                Relationship relationship = (Relationship)theEObject;
                T result = caseRelationship(relationship);
                if (result == null) result = caseElement(relationship);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.RENDERING_DEFINITION: {
                RenderingDefinition renderingDefinition = (RenderingDefinition)theEObject;
                T result = caseRenderingDefinition(renderingDefinition);
                if (result == null) result = casePartDefinition(renderingDefinition);
                if (result == null) result = caseItemDefinition(renderingDefinition);
                if (result == null) result = caseOccurrenceDefinition(renderingDefinition);
                if (result == null) result = caseStructure(renderingDefinition);
                if (result == null) result = caseDefinition(renderingDefinition);
                if (result == null) result = caseClass(renderingDefinition);
                if (result == null) result = caseClassifier(renderingDefinition);
                if (result == null) result = caseType(renderingDefinition);
                if (result == null) result = caseNamespace(renderingDefinition);
                if (result == null) result = caseElement(renderingDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.RENDERING_USAGE: {
                RenderingUsage renderingUsage = (RenderingUsage)theEObject;
                T result = caseRenderingUsage(renderingUsage);
                if (result == null) result = casePartUsage(renderingUsage);
                if (result == null) result = caseItemUsage(renderingUsage);
                if (result == null) result = caseOccurrenceUsage(renderingUsage);
                if (result == null) result = caseUsage(renderingUsage);
                if (result == null) result = caseFeature(renderingUsage);
                if (result == null) result = caseType(renderingUsage);
                if (result == null) result = caseNamespace(renderingUsage);
                if (result == null) result = caseElement(renderingUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.REQUIREMENT_CONSTRAINT_MEMBERSHIP: {
                RequirementConstraintMembership requirementConstraintMembership = (RequirementConstraintMembership)theEObject;
                T result = caseRequirementConstraintMembership(requirementConstraintMembership);
                if (result == null) result = caseFeatureMembership(requirementConstraintMembership);
                if (result == null) result = caseOwningMembership(requirementConstraintMembership);
                if (result == null) result = caseFeaturing(requirementConstraintMembership);
                if (result == null) result = caseMembership(requirementConstraintMembership);
                if (result == null) result = caseRelationship(requirementConstraintMembership);
                if (result == null) result = caseElement(requirementConstraintMembership);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.REQUIREMENT_DEFINITION: {
                RequirementDefinition requirementDefinition = (RequirementDefinition)theEObject;
                T result = caseRequirementDefinition(requirementDefinition);
                if (result == null) result = caseConstraintDefinition(requirementDefinition);
                if (result == null) result = caseOccurrenceDefinition(requirementDefinition);
                if (result == null) result = casePredicate(requirementDefinition);
                if (result == null) result = caseDefinition(requirementDefinition);
                if (result == null) result = caseFunction(requirementDefinition);
                if (result == null) result = caseClassifier(requirementDefinition);
                if (result == null) result = caseBehavior(requirementDefinition);
                if (result == null) result = caseType(requirementDefinition);
                if (result == null) result = caseClass(requirementDefinition);
                if (result == null) result = caseNamespace(requirementDefinition);
                if (result == null) result = caseElement(requirementDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.REQUIREMENT_USAGE: {
                RequirementUsage requirementUsage = (RequirementUsage)theEObject;
                T result = caseRequirementUsage(requirementUsage);
                if (result == null) result = caseConstraintUsage(requirementUsage);
                if (result == null) result = caseOccurrenceUsage(requirementUsage);
                if (result == null) result = caseBooleanExpression(requirementUsage);
                if (result == null) result = caseUsage(requirementUsage);
                if (result == null) result = caseExpression(requirementUsage);
                if (result == null) result = caseStep(requirementUsage);
                if (result == null) result = caseFeature(requirementUsage);
                if (result == null) result = caseType(requirementUsage);
                if (result == null) result = caseNamespace(requirementUsage);
                if (result == null) result = caseElement(requirementUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.REQUIREMENT_VERIFICATION_MEMBERSHIP: {
                RequirementVerificationMembership requirementVerificationMembership = (RequirementVerificationMembership)theEObject;
                T result = caseRequirementVerificationMembership(requirementVerificationMembership);
                if (result == null) result = caseRequirementConstraintMembership(requirementVerificationMembership);
                if (result == null) result = caseFeatureMembership(requirementVerificationMembership);
                if (result == null) result = caseOwningMembership(requirementVerificationMembership);
                if (result == null) result = caseFeaturing(requirementVerificationMembership);
                if (result == null) result = caseMembership(requirementVerificationMembership);
                if (result == null) result = caseRelationship(requirementVerificationMembership);
                if (result == null) result = caseElement(requirementVerificationMembership);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.RESULT_EXPRESSION_MEMBERSHIP: {
                ResultExpressionMembership resultExpressionMembership = (ResultExpressionMembership)theEObject;
                T result = caseResultExpressionMembership(resultExpressionMembership);
                if (result == null) result = caseFeatureMembership(resultExpressionMembership);
                if (result == null) result = caseOwningMembership(resultExpressionMembership);
                if (result == null) result = caseFeaturing(resultExpressionMembership);
                if (result == null) result = caseMembership(resultExpressionMembership);
                if (result == null) result = caseRelationship(resultExpressionMembership);
                if (result == null) result = caseElement(resultExpressionMembership);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.RETURN_PARAMETER_MEMBERSHIP: {
                ReturnParameterMembership returnParameterMembership = (ReturnParameterMembership)theEObject;
                T result = caseReturnParameterMembership(returnParameterMembership);
                if (result == null) result = caseParameterMembership(returnParameterMembership);
                if (result == null) result = caseFeatureMembership(returnParameterMembership);
                if (result == null) result = caseOwningMembership(returnParameterMembership);
                if (result == null) result = caseFeaturing(returnParameterMembership);
                if (result == null) result = caseMembership(returnParameterMembership);
                if (result == null) result = caseRelationship(returnParameterMembership);
                if (result == null) result = caseElement(returnParameterMembership);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.SATISFY_REQUIREMENT_USAGE: {
                SatisfyRequirementUsage satisfyRequirementUsage = (SatisfyRequirementUsage)theEObject;
                T result = caseSatisfyRequirementUsage(satisfyRequirementUsage);
                if (result == null) result = caseRequirementUsage(satisfyRequirementUsage);
                if (result == null) result = caseAssertConstraintUsage(satisfyRequirementUsage);
                if (result == null) result = caseConstraintUsage(satisfyRequirementUsage);
                if (result == null) result = caseInvariant(satisfyRequirementUsage);
                if (result == null) result = caseOccurrenceUsage(satisfyRequirementUsage);
                if (result == null) result = caseBooleanExpression(satisfyRequirementUsage);
                if (result == null) result = caseUsage(satisfyRequirementUsage);
                if (result == null) result = caseExpression(satisfyRequirementUsage);
                if (result == null) result = caseStep(satisfyRequirementUsage);
                if (result == null) result = caseFeature(satisfyRequirementUsage);
                if (result == null) result = caseType(satisfyRequirementUsage);
                if (result == null) result = caseNamespace(satisfyRequirementUsage);
                if (result == null) result = caseElement(satisfyRequirementUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.SELECT_EXPRESSION: {
                SelectExpression selectExpression = (SelectExpression)theEObject;
                T result = caseSelectExpression(selectExpression);
                if (result == null) result = caseOperatorExpression(selectExpression);
                if (result == null) result = caseInvocationExpression(selectExpression);
                if (result == null) result = caseExpression(selectExpression);
                if (result == null) result = caseStep(selectExpression);
                if (result == null) result = caseFeature(selectExpression);
                if (result == null) result = caseType(selectExpression);
                if (result == null) result = caseNamespace(selectExpression);
                if (result == null) result = caseElement(selectExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.SEND_ACTION_USAGE: {
                SendActionUsage sendActionUsage = (SendActionUsage)theEObject;
                T result = caseSendActionUsage(sendActionUsage);
                if (result == null) result = caseActionUsage(sendActionUsage);
                if (result == null) result = caseOccurrenceUsage(sendActionUsage);
                if (result == null) result = caseStep(sendActionUsage);
                if (result == null) result = caseUsage(sendActionUsage);
                if (result == null) result = caseFeature(sendActionUsage);
                if (result == null) result = caseType(sendActionUsage);
                if (result == null) result = caseNamespace(sendActionUsage);
                if (result == null) result = caseElement(sendActionUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.SPECIALIZATION: {
                Specialization specialization = (Specialization)theEObject;
                T result = caseSpecialization(specialization);
                if (result == null) result = caseRelationship(specialization);
                if (result == null) result = caseElement(specialization);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.STAKEHOLDER_MEMBERSHIP: {
                StakeholderMembership stakeholderMembership = (StakeholderMembership)theEObject;
                T result = caseStakeholderMembership(stakeholderMembership);
                if (result == null) result = caseParameterMembership(stakeholderMembership);
                if (result == null) result = caseFeatureMembership(stakeholderMembership);
                if (result == null) result = caseOwningMembership(stakeholderMembership);
                if (result == null) result = caseFeaturing(stakeholderMembership);
                if (result == null) result = caseMembership(stakeholderMembership);
                if (result == null) result = caseRelationship(stakeholderMembership);
                if (result == null) result = caseElement(stakeholderMembership);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.STATE_DEFINITION: {
                StateDefinition stateDefinition = (StateDefinition)theEObject;
                T result = caseStateDefinition(stateDefinition);
                if (result == null) result = caseActionDefinition(stateDefinition);
                if (result == null) result = caseOccurrenceDefinition(stateDefinition);
                if (result == null) result = caseBehavior(stateDefinition);
                if (result == null) result = caseDefinition(stateDefinition);
                if (result == null) result = caseClass(stateDefinition);
                if (result == null) result = caseClassifier(stateDefinition);
                if (result == null) result = caseType(stateDefinition);
                if (result == null) result = caseNamespace(stateDefinition);
                if (result == null) result = caseElement(stateDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.STATE_SUBACTION_MEMBERSHIP: {
                StateSubactionMembership stateSubactionMembership = (StateSubactionMembership)theEObject;
                T result = caseStateSubactionMembership(stateSubactionMembership);
                if (result == null) result = caseFeatureMembership(stateSubactionMembership);
                if (result == null) result = caseOwningMembership(stateSubactionMembership);
                if (result == null) result = caseFeaturing(stateSubactionMembership);
                if (result == null) result = caseMembership(stateSubactionMembership);
                if (result == null) result = caseRelationship(stateSubactionMembership);
                if (result == null) result = caseElement(stateSubactionMembership);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.STATE_USAGE: {
                StateUsage stateUsage = (StateUsage)theEObject;
                T result = caseStateUsage(stateUsage);
                if (result == null) result = caseActionUsage(stateUsage);
                if (result == null) result = caseOccurrenceUsage(stateUsage);
                if (result == null) result = caseStep(stateUsage);
                if (result == null) result = caseUsage(stateUsage);
                if (result == null) result = caseFeature(stateUsage);
                if (result == null) result = caseType(stateUsage);
                if (result == null) result = caseNamespace(stateUsage);
                if (result == null) result = caseElement(stateUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.STEP: {
                Step step = (Step)theEObject;
                T result = caseStep(step);
                if (result == null) result = caseFeature(step);
                if (result == null) result = caseType(step);
                if (result == null) result = caseNamespace(step);
                if (result == null) result = caseElement(step);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.STRUCTURE: {
                Structure structure = (Structure)theEObject;
                T result = caseStructure(structure);
                if (result == null) result = caseClass(structure);
                if (result == null) result = caseClassifier(structure);
                if (result == null) result = caseType(structure);
                if (result == null) result = caseNamespace(structure);
                if (result == null) result = caseElement(structure);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.SUBCLASSIFICATION: {
                Subclassification subclassification = (Subclassification)theEObject;
                T result = caseSubclassification(subclassification);
                if (result == null) result = caseSpecialization(subclassification);
                if (result == null) result = caseRelationship(subclassification);
                if (result == null) result = caseElement(subclassification);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.SUBJECT_MEMBERSHIP: {
                SubjectMembership subjectMembership = (SubjectMembership)theEObject;
                T result = caseSubjectMembership(subjectMembership);
                if (result == null) result = caseParameterMembership(subjectMembership);
                if (result == null) result = caseFeatureMembership(subjectMembership);
                if (result == null) result = caseOwningMembership(subjectMembership);
                if (result == null) result = caseFeaturing(subjectMembership);
                if (result == null) result = caseMembership(subjectMembership);
                if (result == null) result = caseRelationship(subjectMembership);
                if (result == null) result = caseElement(subjectMembership);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.SUBSETTING: {
                Subsetting subsetting = (Subsetting)theEObject;
                T result = caseSubsetting(subsetting);
                if (result == null) result = caseSpecialization(subsetting);
                if (result == null) result = caseRelationship(subsetting);
                if (result == null) result = caseElement(subsetting);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.SUCCESSION: {
                Succession succession = (Succession)theEObject;
                T result = caseSuccession(succession);
                if (result == null) result = caseConnector(succession);
                if (result == null) result = caseFeature(succession);
                if (result == null) result = caseRelationship(succession);
                if (result == null) result = caseType(succession);
                if (result == null) result = caseNamespace(succession);
                if (result == null) result = caseElement(succession);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.SUCCESSION_AS_USAGE: {
                SuccessionAsUsage successionAsUsage = (SuccessionAsUsage)theEObject;
                T result = caseSuccessionAsUsage(successionAsUsage);
                if (result == null) result = caseConnectorAsUsage(successionAsUsage);
                if (result == null) result = caseSuccession(successionAsUsage);
                if (result == null) result = caseUsage(successionAsUsage);
                if (result == null) result = caseConnector(successionAsUsage);
                if (result == null) result = caseFeature(successionAsUsage);
                if (result == null) result = caseRelationship(successionAsUsage);
                if (result == null) result = caseType(successionAsUsage);
                if (result == null) result = caseNamespace(successionAsUsage);
                if (result == null) result = caseElement(successionAsUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.SUCCESSION_FLOW_CONNECTION_USAGE: {
                SuccessionFlowConnectionUsage successionFlowConnectionUsage = (SuccessionFlowConnectionUsage)theEObject;
                T result = caseSuccessionFlowConnectionUsage(successionFlowConnectionUsage);
                if (result == null) result = caseFlowConnectionUsage(successionFlowConnectionUsage);
                if (result == null) result = caseSuccessionItemFlow(successionFlowConnectionUsage);
                if (result == null) result = caseConnectionUsage(successionFlowConnectionUsage);
                if (result == null) result = caseActionUsage(successionFlowConnectionUsage);
                if (result == null) result = caseItemFlow(successionFlowConnectionUsage);
                if (result == null) result = caseSuccession(successionFlowConnectionUsage);
                if (result == null) result = caseConnectorAsUsage(successionFlowConnectionUsage);
                if (result == null) result = casePartUsage(successionFlowConnectionUsage);
                if (result == null) result = caseStep(successionFlowConnectionUsage);
                if (result == null) result = caseConnector(successionFlowConnectionUsage);
                if (result == null) result = caseItemUsage(successionFlowConnectionUsage);
                if (result == null) result = caseFeature(successionFlowConnectionUsage);
                if (result == null) result = caseRelationship(successionFlowConnectionUsage);
                if (result == null) result = caseOccurrenceUsage(successionFlowConnectionUsage);
                if (result == null) result = caseUsage(successionFlowConnectionUsage);
                if (result == null) result = caseType(successionFlowConnectionUsage);
                if (result == null) result = caseNamespace(successionFlowConnectionUsage);
                if (result == null) result = caseElement(successionFlowConnectionUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.SUCCESSION_ITEM_FLOW: {
                SuccessionItemFlow successionItemFlow = (SuccessionItemFlow)theEObject;
                T result = caseSuccessionItemFlow(successionItemFlow);
                if (result == null) result = caseItemFlow(successionItemFlow);
                if (result == null) result = caseSuccession(successionItemFlow);
                if (result == null) result = caseConnector(successionItemFlow);
                if (result == null) result = caseStep(successionItemFlow);
                if (result == null) result = caseFeature(successionItemFlow);
                if (result == null) result = caseRelationship(successionItemFlow);
                if (result == null) result = caseType(successionItemFlow);
                if (result == null) result = caseNamespace(successionItemFlow);
                if (result == null) result = caseElement(successionItemFlow);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.TEXTUAL_REPRESENTATION: {
                TextualRepresentation textualRepresentation = (TextualRepresentation)theEObject;
                T result = caseTextualRepresentation(textualRepresentation);
                if (result == null) result = caseAnnotatingElement(textualRepresentation);
                if (result == null) result = caseElement(textualRepresentation);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.TRANSITION_FEATURE_MEMBERSHIP: {
                TransitionFeatureMembership transitionFeatureMembership = (TransitionFeatureMembership)theEObject;
                T result = caseTransitionFeatureMembership(transitionFeatureMembership);
                if (result == null) result = caseFeatureMembership(transitionFeatureMembership);
                if (result == null) result = caseOwningMembership(transitionFeatureMembership);
                if (result == null) result = caseFeaturing(transitionFeatureMembership);
                if (result == null) result = caseMembership(transitionFeatureMembership);
                if (result == null) result = caseRelationship(transitionFeatureMembership);
                if (result == null) result = caseElement(transitionFeatureMembership);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.TRANSITION_USAGE: {
                TransitionUsage transitionUsage = (TransitionUsage)theEObject;
                T result = caseTransitionUsage(transitionUsage);
                if (result == null) result = caseActionUsage(transitionUsage);
                if (result == null) result = caseOccurrenceUsage(transitionUsage);
                if (result == null) result = caseStep(transitionUsage);
                if (result == null) result = caseUsage(transitionUsage);
                if (result == null) result = caseFeature(transitionUsage);
                if (result == null) result = caseType(transitionUsage);
                if (result == null) result = caseNamespace(transitionUsage);
                if (result == null) result = caseElement(transitionUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.TRIGGER_INVOCATION_EXPRESSION: {
                TriggerInvocationExpression triggerInvocationExpression = (TriggerInvocationExpression)theEObject;
                T result = caseTriggerInvocationExpression(triggerInvocationExpression);
                if (result == null) result = caseInvocationExpression(triggerInvocationExpression);
                if (result == null) result = caseExpression(triggerInvocationExpression);
                if (result == null) result = caseStep(triggerInvocationExpression);
                if (result == null) result = caseFeature(triggerInvocationExpression);
                if (result == null) result = caseType(triggerInvocationExpression);
                if (result == null) result = caseNamespace(triggerInvocationExpression);
                if (result == null) result = caseElement(triggerInvocationExpression);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.TYPE: {
                Type type = (Type)theEObject;
                T result = caseType(type);
                if (result == null) result = caseNamespace(type);
                if (result == null) result = caseElement(type);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.TYPE_FEATURING: {
                TypeFeaturing typeFeaturing = (TypeFeaturing)theEObject;
                T result = caseTypeFeaturing(typeFeaturing);
                if (result == null) result = caseFeaturing(typeFeaturing);
                if (result == null) result = caseRelationship(typeFeaturing);
                if (result == null) result = caseElement(typeFeaturing);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.UNIONING: {
                Unioning unioning = (Unioning)theEObject;
                T result = caseUnioning(unioning);
                if (result == null) result = caseRelationship(unioning);
                if (result == null) result = caseElement(unioning);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.USAGE: {
                Usage usage = (Usage)theEObject;
                T result = caseUsage(usage);
                if (result == null) result = caseFeature(usage);
                if (result == null) result = caseType(usage);
                if (result == null) result = caseNamespace(usage);
                if (result == null) result = caseElement(usage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.USE_CASE_DEFINITION: {
                UseCaseDefinition useCaseDefinition = (UseCaseDefinition)theEObject;
                T result = caseUseCaseDefinition(useCaseDefinition);
                if (result == null) result = caseCaseDefinition(useCaseDefinition);
                if (result == null) result = caseCalculationDefinition(useCaseDefinition);
                if (result == null) result = caseActionDefinition(useCaseDefinition);
                if (result == null) result = caseFunction(useCaseDefinition);
                if (result == null) result = caseOccurrenceDefinition(useCaseDefinition);
                if (result == null) result = caseBehavior(useCaseDefinition);
                if (result == null) result = caseDefinition(useCaseDefinition);
                if (result == null) result = caseClass(useCaseDefinition);
                if (result == null) result = caseClassifier(useCaseDefinition);
                if (result == null) result = caseType(useCaseDefinition);
                if (result == null) result = caseNamespace(useCaseDefinition);
                if (result == null) result = caseElement(useCaseDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.USE_CASE_USAGE: {
                UseCaseUsage useCaseUsage = (UseCaseUsage)theEObject;
                T result = caseUseCaseUsage(useCaseUsage);
                if (result == null) result = caseCaseUsage(useCaseUsage);
                if (result == null) result = caseCalculationUsage(useCaseUsage);
                if (result == null) result = caseActionUsage(useCaseUsage);
                if (result == null) result = caseExpression(useCaseUsage);
                if (result == null) result = caseOccurrenceUsage(useCaseUsage);
                if (result == null) result = caseStep(useCaseUsage);
                if (result == null) result = caseUsage(useCaseUsage);
                if (result == null) result = caseFeature(useCaseUsage);
                if (result == null) result = caseType(useCaseUsage);
                if (result == null) result = caseNamespace(useCaseUsage);
                if (result == null) result = caseElement(useCaseUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.VARIANT_MEMBERSHIP: {
                VariantMembership variantMembership = (VariantMembership)theEObject;
                T result = caseVariantMembership(variantMembership);
                if (result == null) result = caseOwningMembership(variantMembership);
                if (result == null) result = caseMembership(variantMembership);
                if (result == null) result = caseRelationship(variantMembership);
                if (result == null) result = caseElement(variantMembership);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.VERIFICATION_CASE_DEFINITION: {
                VerificationCaseDefinition verificationCaseDefinition = (VerificationCaseDefinition)theEObject;
                T result = caseVerificationCaseDefinition(verificationCaseDefinition);
                if (result == null) result = caseCaseDefinition(verificationCaseDefinition);
                if (result == null) result = caseCalculationDefinition(verificationCaseDefinition);
                if (result == null) result = caseActionDefinition(verificationCaseDefinition);
                if (result == null) result = caseFunction(verificationCaseDefinition);
                if (result == null) result = caseOccurrenceDefinition(verificationCaseDefinition);
                if (result == null) result = caseBehavior(verificationCaseDefinition);
                if (result == null) result = caseDefinition(verificationCaseDefinition);
                if (result == null) result = caseClass(verificationCaseDefinition);
                if (result == null) result = caseClassifier(verificationCaseDefinition);
                if (result == null) result = caseType(verificationCaseDefinition);
                if (result == null) result = caseNamespace(verificationCaseDefinition);
                if (result == null) result = caseElement(verificationCaseDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.VERIFICATION_CASE_USAGE: {
                VerificationCaseUsage verificationCaseUsage = (VerificationCaseUsage)theEObject;
                T result = caseVerificationCaseUsage(verificationCaseUsage);
                if (result == null) result = caseCaseUsage(verificationCaseUsage);
                if (result == null) result = caseCalculationUsage(verificationCaseUsage);
                if (result == null) result = caseActionUsage(verificationCaseUsage);
                if (result == null) result = caseExpression(verificationCaseUsage);
                if (result == null) result = caseOccurrenceUsage(verificationCaseUsage);
                if (result == null) result = caseStep(verificationCaseUsage);
                if (result == null) result = caseUsage(verificationCaseUsage);
                if (result == null) result = caseFeature(verificationCaseUsage);
                if (result == null) result = caseType(verificationCaseUsage);
                if (result == null) result = caseNamespace(verificationCaseUsage);
                if (result == null) result = caseElement(verificationCaseUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.VIEW_DEFINITION: {
                ViewDefinition viewDefinition = (ViewDefinition)theEObject;
                T result = caseViewDefinition(viewDefinition);
                if (result == null) result = casePartDefinition(viewDefinition);
                if (result == null) result = caseItemDefinition(viewDefinition);
                if (result == null) result = caseOccurrenceDefinition(viewDefinition);
                if (result == null) result = caseStructure(viewDefinition);
                if (result == null) result = caseDefinition(viewDefinition);
                if (result == null) result = caseClass(viewDefinition);
                if (result == null) result = caseClassifier(viewDefinition);
                if (result == null) result = caseType(viewDefinition);
                if (result == null) result = caseNamespace(viewDefinition);
                if (result == null) result = caseElement(viewDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.VIEWPOINT_DEFINITION: {
                ViewpointDefinition viewpointDefinition = (ViewpointDefinition)theEObject;
                T result = caseViewpointDefinition(viewpointDefinition);
                if (result == null) result = caseRequirementDefinition(viewpointDefinition);
                if (result == null) result = caseConstraintDefinition(viewpointDefinition);
                if (result == null) result = caseOccurrenceDefinition(viewpointDefinition);
                if (result == null) result = casePredicate(viewpointDefinition);
                if (result == null) result = caseDefinition(viewpointDefinition);
                if (result == null) result = caseFunction(viewpointDefinition);
                if (result == null) result = caseClassifier(viewpointDefinition);
                if (result == null) result = caseBehavior(viewpointDefinition);
                if (result == null) result = caseType(viewpointDefinition);
                if (result == null) result = caseClass(viewpointDefinition);
                if (result == null) result = caseNamespace(viewpointDefinition);
                if (result == null) result = caseElement(viewpointDefinition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.VIEWPOINT_USAGE: {
                ViewpointUsage viewpointUsage = (ViewpointUsage)theEObject;
                T result = caseViewpointUsage(viewpointUsage);
                if (result == null) result = caseRequirementUsage(viewpointUsage);
                if (result == null) result = caseConstraintUsage(viewpointUsage);
                if (result == null) result = caseOccurrenceUsage(viewpointUsage);
                if (result == null) result = caseBooleanExpression(viewpointUsage);
                if (result == null) result = caseUsage(viewpointUsage);
                if (result == null) result = caseExpression(viewpointUsage);
                if (result == null) result = caseStep(viewpointUsage);
                if (result == null) result = caseFeature(viewpointUsage);
                if (result == null) result = caseType(viewpointUsage);
                if (result == null) result = caseNamespace(viewpointUsage);
                if (result == null) result = caseElement(viewpointUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.VIEW_RENDERING_MEMBERSHIP: {
                ViewRenderingMembership viewRenderingMembership = (ViewRenderingMembership)theEObject;
                T result = caseViewRenderingMembership(viewRenderingMembership);
                if (result == null) result = caseFeatureMembership(viewRenderingMembership);
                if (result == null) result = caseOwningMembership(viewRenderingMembership);
                if (result == null) result = caseFeaturing(viewRenderingMembership);
                if (result == null) result = caseMembership(viewRenderingMembership);
                if (result == null) result = caseRelationship(viewRenderingMembership);
                if (result == null) result = caseElement(viewRenderingMembership);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.VIEW_USAGE: {
                ViewUsage viewUsage = (ViewUsage)theEObject;
                T result = caseViewUsage(viewUsage);
                if (result == null) result = casePartUsage(viewUsage);
                if (result == null) result = caseItemUsage(viewUsage);
                if (result == null) result = caseOccurrenceUsage(viewUsage);
                if (result == null) result = caseUsage(viewUsage);
                if (result == null) result = caseFeature(viewUsage);
                if (result == null) result = caseType(viewUsage);
                if (result == null) result = caseNamespace(viewUsage);
                if (result == null) result = caseElement(viewUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case SysmlPackage.WHILE_LOOP_ACTION_USAGE: {
                WhileLoopActionUsage whileLoopActionUsage = (WhileLoopActionUsage)theEObject;
                T result = caseWhileLoopActionUsage(whileLoopActionUsage);
                if (result == null) result = caseLoopActionUsage(whileLoopActionUsage);
                if (result == null) result = caseActionUsage(whileLoopActionUsage);
                if (result == null) result = caseOccurrenceUsage(whileLoopActionUsage);
                if (result == null) result = caseStep(whileLoopActionUsage);
                if (result == null) result = caseUsage(whileLoopActionUsage);
                if (result == null) result = caseFeature(whileLoopActionUsage);
                if (result == null) result = caseType(whileLoopActionUsage);
                if (result == null) result = caseNamespace(whileLoopActionUsage);
                if (result == null) result = caseElement(whileLoopActionUsage);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Accept Action Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Accept Action Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAcceptActionUsage(AcceptActionUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Action Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Action Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseActionDefinition(ActionDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Action Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Action Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseActionUsage(ActionUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Actor Membership</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Actor Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseActorMembership(ActorMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Allocation Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Allocation Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAllocationDefinition(AllocationDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Allocation Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Allocation Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAllocationUsage(AllocationUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Analysis Case Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Analysis Case Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAnalysisCaseDefinition(AnalysisCaseDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Analysis Case Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Analysis Case Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAnalysisCaseUsage(AnalysisCaseUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Annotating Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Annotating Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAnnotatingElement(AnnotatingElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Annotation</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Annotation</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAnnotation(Annotation object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Assert Constraint Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Assert Constraint Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAssertConstraintUsage(AssertConstraintUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Assignment Action Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Assignment Action Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAssignmentActionUsage(AssignmentActionUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Association</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Association</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAssociation(Association object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Association Structure</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Association Structure</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAssociationStructure(AssociationStructure object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Attribute Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Attribute Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAttributeDefinition(AttributeDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Attribute Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Attribute Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAttributeUsage(AttributeUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Behavior</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Behavior</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBehavior(Behavior object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Binding Connector</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Binding Connector</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBindingConnector(BindingConnector object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Binding Connector As Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Binding Connector As Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBindingConnectorAsUsage(BindingConnectorAsUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Boolean Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Boolean Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseBooleanExpression(BooleanExpression object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Calculation Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Calculation Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCalculationDefinition(CalculationDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Calculation Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Calculation Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCalculationUsage(CalculationUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Case Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Case Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCaseDefinition(CaseDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Case Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Case Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCaseUsage(CaseUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Class</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Class</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseClass(org.eclipse.syson.sysml.Class object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Classifier</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Classifier</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseClassifier(Classifier object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Collect Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Collect Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCollectExpression(CollectExpression object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Comment</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Comment</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseComment(Comment object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Concern Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Concern Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConcernDefinition(ConcernDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Concern Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Concern Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConcernUsage(ConcernUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Conjugated Port Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Conjugated Port Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConjugatedPortDefinition(ConjugatedPortDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Conjugated Port Typing</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Conjugated Port Typing</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConjugatedPortTyping(ConjugatedPortTyping object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Conjugation</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Conjugation</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConjugation(Conjugation object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Connection Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Connection Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConnectionDefinition(ConnectionDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Connection Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Connection Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConnectionUsage(ConnectionUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Connector</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Connector</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConnector(Connector object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Connector As Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Connector As Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConnectorAsUsage(ConnectorAsUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Constraint Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Constraint Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConstraintDefinition(ConstraintDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Constraint Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Constraint Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseConstraintUsage(ConstraintUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Control Node</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Control Node</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseControlNode(ControlNode object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Data Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Data Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDataType(DataType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Decision Node</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Decision Node</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDecisionNode(DecisionNode object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDefinition(Definition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Dependency</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Dependency</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDependency(Dependency object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Differencing</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Differencing</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDifferencing(Differencing object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Disjoining</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Disjoining</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDisjoining(Disjoining object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Documentation</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Documentation</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDocumentation(Documentation object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseElement(Element object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Element Filter Membership</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Element Filter Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseElementFilterMembership(ElementFilterMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>End Feature Membership</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>End Feature Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEndFeatureMembership(EndFeatureMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Enumeration Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Enumeration Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEnumerationDefinition(EnumerationDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Enumeration Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Enumeration Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEnumerationUsage(EnumerationUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Event Occurrence Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Event Occurrence Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEventOccurrenceUsage(EventOccurrenceUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Exhibit State Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Exhibit State Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseExhibitStateUsage(ExhibitStateUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Expose</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Expose</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseExpose(Expose object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseExpression(Expression object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Feature</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Feature</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeature(Feature object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Feature Chain Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Feature Chain Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeatureChainExpression(FeatureChainExpression object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Feature Chaining</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Feature Chaining</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeatureChaining(FeatureChaining object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Feature Inverting</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Feature Inverting</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeatureInverting(FeatureInverting object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Feature Membership</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Feature Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeatureMembership(FeatureMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Feature Reference Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Feature Reference Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeatureReferenceExpression(FeatureReferenceExpression object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Feature Typing</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Feature Typing</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeatureTyping(FeatureTyping object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Feature Value</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Feature Value</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeatureValue(FeatureValue object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Featuring</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Featuring</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFeaturing(Featuring object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Flow Connection Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Flow Connection Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFlowConnectionDefinition(FlowConnectionDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Flow Connection Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Flow Connection Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFlowConnectionUsage(FlowConnectionUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Fork Node</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Fork Node</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseForkNode(ForkNode object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>For Loop Action Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>For Loop Action Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseForLoopActionUsage(ForLoopActionUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Framed Concern Membership</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Framed Concern Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFramedConcernMembership(FramedConcernMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Function</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Function</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseFunction(Function object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>If Action Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>If Action Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIfActionUsage(IfActionUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Import</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Import</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseImport(Import object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Include Use Case Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Include Use Case Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIncludeUseCaseUsage(IncludeUseCaseUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Interaction</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Interaction</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInteraction(Interaction object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Interface Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Interface Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInterfaceDefinition(InterfaceDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Interface Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Interface Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInterfaceUsage(InterfaceUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Intersecting</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Intersecting</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIntersecting(Intersecting object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Invariant</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Invariant</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInvariant(Invariant object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Invocation Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Invocation Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInvocationExpression(InvocationExpression object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Item Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Item Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseItemDefinition(ItemDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Item Feature</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Item Feature</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseItemFeature(ItemFeature object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Item Flow</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Item Flow</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseItemFlow(ItemFlow object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Item Flow End</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Item Flow End</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseItemFlowEnd(ItemFlowEnd object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Item Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Item Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseItemUsage(ItemUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Join Node</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Join Node</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseJoinNode(JoinNode object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Library Package</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Library Package</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLibraryPackage(LibraryPackage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Life Class</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Life Class</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLifeClass(LifeClass object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Literal Boolean</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Literal Boolean</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLiteralBoolean(LiteralBoolean object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Literal Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Literal Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLiteralExpression(LiteralExpression object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Literal Infinity</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Literal Infinity</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLiteralInfinity(LiteralInfinity object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Literal Integer</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Literal Integer</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLiteralInteger(LiteralInteger object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Literal Rational</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Literal Rational</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLiteralRational(LiteralRational object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Literal String</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Literal String</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLiteralString(LiteralString object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Loop Action Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Loop Action Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLoopActionUsage(LoopActionUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Membership</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMembership(Membership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Membership Expose</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Membership Expose</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMembershipExpose(MembershipExpose object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Membership Import</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Membership Import</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMembershipImport(MembershipImport object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Merge Node</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Merge Node</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMergeNode(MergeNode object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Metaclass</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Metaclass</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMetaclass(Metaclass object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Metadata Access Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Metadata Access Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMetadataAccessExpression(MetadataAccessExpression object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Metadata Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Metadata Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMetadataDefinition(MetadataDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Metadata Feature</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Metadata Feature</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMetadataFeature(MetadataFeature object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Metadata Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Metadata Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMetadataUsage(MetadataUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Multiplicity</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Multiplicity</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMultiplicity(Multiplicity object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Multiplicity Range</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Multiplicity Range</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseMultiplicityRange(MultiplicityRange object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Namespace</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Namespace</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNamespace(Namespace object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Namespace Expose</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Namespace Expose</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNamespaceExpose(NamespaceExpose object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Namespace Import</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Namespace Import</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNamespaceImport(NamespaceImport object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Null Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Null Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNullExpression(NullExpression object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Objective Membership</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Objective Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseObjectiveMembership(ObjectiveMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Occurrence Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Occurrence Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOccurrenceDefinition(OccurrenceDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Occurrence Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Occurrence Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOccurrenceUsage(OccurrenceUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Operator Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Operator Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOperatorExpression(OperatorExpression object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Owning Membership</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Owning Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOwningMembership(OwningMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Package</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Package</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePackage(org.eclipse.syson.sysml.Package object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Parameter Membership</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Parameter Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseParameterMembership(ParameterMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Part Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Part Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePartDefinition(PartDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Part Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Part Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePartUsage(PartUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Perform Action Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Perform Action Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePerformActionUsage(PerformActionUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Port Conjugation</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Port Conjugation</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePortConjugation(PortConjugation object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Port Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Port Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePortDefinition(PortDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Port Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Port Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePortUsage(PortUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Predicate</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Predicate</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePredicate(Predicate object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Redefinition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Redefinition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRedefinition(Redefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Reference Subsetting</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Reference Subsetting</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseReferenceSubsetting(ReferenceSubsetting object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Reference Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Reference Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseReferenceUsage(ReferenceUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Relationship</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Relationship</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRelationship(Relationship object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Rendering Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Rendering Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRenderingDefinition(RenderingDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Rendering Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Rendering Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRenderingUsage(RenderingUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Requirement Constraint Membership</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Requirement Constraint Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRequirementConstraintMembership(RequirementConstraintMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Requirement Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Requirement Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRequirementDefinition(RequirementDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Requirement Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Requirement Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRequirementUsage(RequirementUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Requirement Verification Membership</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Requirement Verification Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseRequirementVerificationMembership(RequirementVerificationMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Result Expression Membership</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Result Expression Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseResultExpressionMembership(ResultExpressionMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Return Parameter Membership</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Return Parameter Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseReturnParameterMembership(ReturnParameterMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Satisfy Requirement Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Satisfy Requirement Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSatisfyRequirementUsage(SatisfyRequirementUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Select Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Select Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSelectExpression(SelectExpression object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Send Action Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Send Action Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSendActionUsage(SendActionUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Specialization</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Specialization</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSpecialization(Specialization object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Stakeholder Membership</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Stakeholder Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStakeholderMembership(StakeholderMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>State Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>State Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStateDefinition(StateDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>State Subaction Membership</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>State Subaction Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStateSubactionMembership(StateSubactionMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>State Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>State Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStateUsage(StateUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Step</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Step</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStep(Step object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Structure</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Structure</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStructure(Structure object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Subclassification</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Subclassification</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSubclassification(Subclassification object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Subject Membership</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Subject Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSubjectMembership(SubjectMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Subsetting</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Subsetting</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSubsetting(Subsetting object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Succession</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Succession</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSuccession(Succession object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Succession As Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Succession As Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSuccessionAsUsage(SuccessionAsUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Succession Flow Connection Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Succession Flow Connection Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSuccessionFlowConnectionUsage(SuccessionFlowConnectionUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Succession Item Flow</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Succession Item Flow</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseSuccessionItemFlow(SuccessionItemFlow object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Textual Representation</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Textual Representation</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTextualRepresentation(TextualRepresentation object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Transition Feature Membership</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Transition Feature Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTransitionFeatureMembership(TransitionFeatureMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Transition Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Transition Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTransitionUsage(TransitionUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Trigger Invocation Expression</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Trigger Invocation Expression</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTriggerInvocationExpression(TriggerInvocationExpression object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseType(Type object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Type Featuring</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Type Featuring</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseTypeFeaturing(TypeFeaturing object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Unioning</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Unioning</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUnioning(Unioning object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUsage(Usage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Use Case Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Use Case Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUseCaseDefinition(UseCaseDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Use Case Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Use Case Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUseCaseUsage(UseCaseUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Variant Membership</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Variant Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseVariantMembership(VariantMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Verification Case Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Verification Case Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseVerificationCaseDefinition(VerificationCaseDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Verification Case Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Verification Case Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseVerificationCaseUsage(VerificationCaseUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>View Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>View Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseViewDefinition(ViewDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Viewpoint Definition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Viewpoint Definition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseViewpointDefinition(ViewpointDefinition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Viewpoint Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Viewpoint Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseViewpointUsage(ViewpointUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>View Rendering Membership</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>View Rendering Membership</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseViewRenderingMembership(ViewRenderingMembership object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>View Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>View Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseViewUsage(ViewUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>While Loop Action Usage</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>While Loop Action Usage</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseWhileLoopActionUsage(WhileLoopActionUsage object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch, but this is the last case anyway.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject)
     * @generated
     */
    @Override
    public T defaultCase(EObject object) {
        return null;
    }

} //SysmlSwitch
