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

import java.util.UUID;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.syson.sysml.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SysmlFactoryImpl extends EFactoryImpl implements SysmlFactory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static SysmlFactory init() {
        try {
            SysmlFactory theSysmlFactory = (SysmlFactory)EPackage.Registry.INSTANCE.getEFactory(SysmlPackage.eNS_URI);
            if (theSysmlFactory != null) {
                return theSysmlFactory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new SysmlFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SysmlFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case SysmlPackage.ACCEPT_ACTION_USAGE: return createAcceptActionUsage();
            case SysmlPackage.ACTION_DEFINITION: return createActionDefinition();
            case SysmlPackage.ACTION_USAGE: return createActionUsage();
            case SysmlPackage.ACTOR_MEMBERSHIP: return createActorMembership();
            case SysmlPackage.ALLOCATION_DEFINITION: return createAllocationDefinition();
            case SysmlPackage.ALLOCATION_USAGE: return createAllocationUsage();
            case SysmlPackage.ANALYSIS_CASE_DEFINITION: return createAnalysisCaseDefinition();
            case SysmlPackage.ANALYSIS_CASE_USAGE: return createAnalysisCaseUsage();
            case SysmlPackage.ANNOTATING_ELEMENT: return createAnnotatingElement();
            case SysmlPackage.ANNOTATION: return createAnnotation();
            case SysmlPackage.ASSERT_CONSTRAINT_USAGE: return createAssertConstraintUsage();
            case SysmlPackage.ASSIGNMENT_ACTION_USAGE: return createAssignmentActionUsage();
            case SysmlPackage.ASSOCIATION: return createAssociation();
            case SysmlPackage.ASSOCIATION_STRUCTURE: return createAssociationStructure();
            case SysmlPackage.ATTRIBUTE_DEFINITION: return createAttributeDefinition();
            case SysmlPackage.ATTRIBUTE_USAGE: return createAttributeUsage();
            case SysmlPackage.BEHAVIOR: return createBehavior();
            case SysmlPackage.BINDING_CONNECTOR: return createBindingConnector();
            case SysmlPackage.BINDING_CONNECTOR_AS_USAGE: return createBindingConnectorAsUsage();
            case SysmlPackage.BOOLEAN_EXPRESSION: return createBooleanExpression();
            case SysmlPackage.CALCULATION_DEFINITION: return createCalculationDefinition();
            case SysmlPackage.CALCULATION_USAGE: return createCalculationUsage();
            case SysmlPackage.CASE_DEFINITION: return createCaseDefinition();
            case SysmlPackage.CASE_USAGE: return createCaseUsage();
            case SysmlPackage.CLASS: return createClass();
            case SysmlPackage.CLASSIFIER: return createClassifier();
            case SysmlPackage.COLLECT_EXPRESSION: return createCollectExpression();
            case SysmlPackage.COMMENT: return createComment();
            case SysmlPackage.CONCERN_DEFINITION: return createConcernDefinition();
            case SysmlPackage.CONCERN_USAGE: return createConcernUsage();
            case SysmlPackage.CONJUGATED_PORT_DEFINITION: return createConjugatedPortDefinition();
            case SysmlPackage.CONJUGATED_PORT_TYPING: return createConjugatedPortTyping();
            case SysmlPackage.CONJUGATION: return createConjugation();
            case SysmlPackage.CONNECTION_DEFINITION: return createConnectionDefinition();
            case SysmlPackage.CONNECTION_USAGE: return createConnectionUsage();
            case SysmlPackage.CONNECTOR: return createConnector();
            case SysmlPackage.CONSTRAINT_DEFINITION: return createConstraintDefinition();
            case SysmlPackage.CONSTRAINT_USAGE: return createConstraintUsage();
            case SysmlPackage.DATA_TYPE: return createDataType();
            case SysmlPackage.DECISION_NODE: return createDecisionNode();
            case SysmlPackage.DEFINITION: return createDefinition();
            case SysmlPackage.DEPENDENCY: return createDependency();
            case SysmlPackage.DIFFERENCING: return createDifferencing();
            case SysmlPackage.DISJOINING: return createDisjoining();
            case SysmlPackage.DOCUMENTATION: return createDocumentation();
            case SysmlPackage.ELEMENT_FILTER_MEMBERSHIP: return createElementFilterMembership();
            case SysmlPackage.END_FEATURE_MEMBERSHIP: return createEndFeatureMembership();
            case SysmlPackage.ENUMERATION_DEFINITION: return createEnumerationDefinition();
            case SysmlPackage.ENUMERATION_USAGE: return createEnumerationUsage();
            case SysmlPackage.EVENT_OCCURRENCE_USAGE: return createEventOccurrenceUsage();
            case SysmlPackage.EXHIBIT_STATE_USAGE: return createExhibitStateUsage();
            case SysmlPackage.EXPRESSION: return createExpression();
            case SysmlPackage.FEATURE: return createFeature();
            case SysmlPackage.FEATURE_CHAIN_EXPRESSION: return createFeatureChainExpression();
            case SysmlPackage.FEATURE_CHAINING: return createFeatureChaining();
            case SysmlPackage.FEATURE_INVERTING: return createFeatureInverting();
            case SysmlPackage.FEATURE_MEMBERSHIP: return createFeatureMembership();
            case SysmlPackage.FEATURE_REFERENCE_EXPRESSION: return createFeatureReferenceExpression();
            case SysmlPackage.FEATURE_TYPING: return createFeatureTyping();
            case SysmlPackage.FEATURE_VALUE: return createFeatureValue();
            case SysmlPackage.FLOW_CONNECTION_DEFINITION: return createFlowConnectionDefinition();
            case SysmlPackage.FLOW_CONNECTION_USAGE: return createFlowConnectionUsage();
            case SysmlPackage.FORK_NODE: return createForkNode();
            case SysmlPackage.FOR_LOOP_ACTION_USAGE: return createForLoopActionUsage();
            case SysmlPackage.FRAMED_CONCERN_MEMBERSHIP: return createFramedConcernMembership();
            case SysmlPackage.FUNCTION: return createFunction();
            case SysmlPackage.IF_ACTION_USAGE: return createIfActionUsage();
            case SysmlPackage.INCLUDE_USE_CASE_USAGE: return createIncludeUseCaseUsage();
            case SysmlPackage.INTERACTION: return createInteraction();
            case SysmlPackage.INTERFACE_DEFINITION: return createInterfaceDefinition();
            case SysmlPackage.INTERFACE_USAGE: return createInterfaceUsage();
            case SysmlPackage.INTERSECTING: return createIntersecting();
            case SysmlPackage.INVARIANT: return createInvariant();
            case SysmlPackage.INVOCATION_EXPRESSION: return createInvocationExpression();
            case SysmlPackage.ITEM_DEFINITION: return createItemDefinition();
            case SysmlPackage.ITEM_FEATURE: return createItemFeature();
            case SysmlPackage.ITEM_FLOW: return createItemFlow();
            case SysmlPackage.ITEM_FLOW_END: return createItemFlowEnd();
            case SysmlPackage.ITEM_USAGE: return createItemUsage();
            case SysmlPackage.JOIN_NODE: return createJoinNode();
            case SysmlPackage.LIBRARY_PACKAGE: return createLibraryPackage();
            case SysmlPackage.LIFE_CLASS: return createLifeClass();
            case SysmlPackage.LITERAL_BOOLEAN: return createLiteralBoolean();
            case SysmlPackage.LITERAL_EXPRESSION: return createLiteralExpression();
            case SysmlPackage.LITERAL_INFINITY: return createLiteralInfinity();
            case SysmlPackage.LITERAL_INTEGER: return createLiteralInteger();
            case SysmlPackage.LITERAL_RATIONAL: return createLiteralRational();
            case SysmlPackage.LITERAL_STRING: return createLiteralString();
            case SysmlPackage.MEMBERSHIP: return createMembership();
            case SysmlPackage.MEMBERSHIP_EXPOSE: return createMembershipExpose();
            case SysmlPackage.MEMBERSHIP_IMPORT: return createMembershipImport();
            case SysmlPackage.MERGE_NODE: return createMergeNode();
            case SysmlPackage.METACLASS: return createMetaclass();
            case SysmlPackage.METADATA_ACCESS_EXPRESSION: return createMetadataAccessExpression();
            case SysmlPackage.METADATA_DEFINITION: return createMetadataDefinition();
            case SysmlPackage.METADATA_FEATURE: return createMetadataFeature();
            case SysmlPackage.METADATA_USAGE: return createMetadataUsage();
            case SysmlPackage.MULTIPLICITY: return createMultiplicity();
            case SysmlPackage.MULTIPLICITY_RANGE: return createMultiplicityRange();
            case SysmlPackage.NAMESPACE: return createNamespace();
            case SysmlPackage.NAMESPACE_EXPOSE: return createNamespaceExpose();
            case SysmlPackage.NAMESPACE_IMPORT: return createNamespaceImport();
            case SysmlPackage.NULL_EXPRESSION: return createNullExpression();
            case SysmlPackage.OBJECTIVE_MEMBERSHIP: return createObjectiveMembership();
            case SysmlPackage.OCCURRENCE_DEFINITION: return createOccurrenceDefinition();
            case SysmlPackage.OCCURRENCE_USAGE: return createOccurrenceUsage();
            case SysmlPackage.OPERATOR_EXPRESSION: return createOperatorExpression();
            case SysmlPackage.OWNING_MEMBERSHIP: return createOwningMembership();
            case SysmlPackage.PACKAGE: return createPackage();
            case SysmlPackage.PARAMETER_MEMBERSHIP: return createParameterMembership();
            case SysmlPackage.PART_DEFINITION: return createPartDefinition();
            case SysmlPackage.PART_USAGE: return createPartUsage();
            case SysmlPackage.PERFORM_ACTION_USAGE: return createPerformActionUsage();
            case SysmlPackage.PORT_CONJUGATION: return createPortConjugation();
            case SysmlPackage.PORT_DEFINITION: return createPortDefinition();
            case SysmlPackage.PORT_USAGE: return createPortUsage();
            case SysmlPackage.PREDICATE: return createPredicate();
            case SysmlPackage.REDEFINITION: return createRedefinition();
            case SysmlPackage.REFERENCE_SUBSETTING: return createReferenceSubsetting();
            case SysmlPackage.REFERENCE_USAGE: return createReferenceUsage();
            case SysmlPackage.RENDERING_DEFINITION: return createRenderingDefinition();
            case SysmlPackage.RENDERING_USAGE: return createRenderingUsage();
            case SysmlPackage.REQUIREMENT_CONSTRAINT_MEMBERSHIP: return createRequirementConstraintMembership();
            case SysmlPackage.REQUIREMENT_DEFINITION: return createRequirementDefinition();
            case SysmlPackage.REQUIREMENT_USAGE: return createRequirementUsage();
            case SysmlPackage.REQUIREMENT_VERIFICATION_MEMBERSHIP: return createRequirementVerificationMembership();
            case SysmlPackage.RESULT_EXPRESSION_MEMBERSHIP: return createResultExpressionMembership();
            case SysmlPackage.RETURN_PARAMETER_MEMBERSHIP: return createReturnParameterMembership();
            case SysmlPackage.SATISFY_REQUIREMENT_USAGE: return createSatisfyRequirementUsage();
            case SysmlPackage.SELECT_EXPRESSION: return createSelectExpression();
            case SysmlPackage.SEND_ACTION_USAGE: return createSendActionUsage();
            case SysmlPackage.SPECIALIZATION: return createSpecialization();
            case SysmlPackage.STAKEHOLDER_MEMBERSHIP: return createStakeholderMembership();
            case SysmlPackage.STATE_DEFINITION: return createStateDefinition();
            case SysmlPackage.STATE_SUBACTION_MEMBERSHIP: return createStateSubactionMembership();
            case SysmlPackage.STATE_USAGE: return createStateUsage();
            case SysmlPackage.STEP: return createStep();
            case SysmlPackage.STRUCTURE: return createStructure();
            case SysmlPackage.SUBCLASSIFICATION: return createSubclassification();
            case SysmlPackage.SUBJECT_MEMBERSHIP: return createSubjectMembership();
            case SysmlPackage.SUBSETTING: return createSubsetting();
            case SysmlPackage.SUCCESSION: return createSuccession();
            case SysmlPackage.SUCCESSION_AS_USAGE: return createSuccessionAsUsage();
            case SysmlPackage.SUCCESSION_FLOW_CONNECTION_USAGE: return createSuccessionFlowConnectionUsage();
            case SysmlPackage.SUCCESSION_ITEM_FLOW: return createSuccessionItemFlow();
            case SysmlPackage.TEXTUAL_REPRESENTATION: return createTextualRepresentation();
            case SysmlPackage.TRANSITION_FEATURE_MEMBERSHIP: return createTransitionFeatureMembership();
            case SysmlPackage.TRANSITION_USAGE: return createTransitionUsage();
            case SysmlPackage.TRIGGER_INVOCATION_EXPRESSION: return createTriggerInvocationExpression();
            case SysmlPackage.TYPE: return createType();
            case SysmlPackage.TYPE_FEATURING: return createTypeFeaturing();
            case SysmlPackage.UNIONING: return createUnioning();
            case SysmlPackage.USAGE: return createUsage();
            case SysmlPackage.USE_CASE_DEFINITION: return createUseCaseDefinition();
            case SysmlPackage.USE_CASE_USAGE: return createUseCaseUsage();
            case SysmlPackage.VARIANT_MEMBERSHIP: return createVariantMembership();
            case SysmlPackage.VERIFICATION_CASE_DEFINITION: return createVerificationCaseDefinition();
            case SysmlPackage.VERIFICATION_CASE_USAGE: return createVerificationCaseUsage();
            case SysmlPackage.VIEW_DEFINITION: return createViewDefinition();
            case SysmlPackage.VIEWPOINT_DEFINITION: return createViewpointDefinition();
            case SysmlPackage.VIEWPOINT_USAGE: return createViewpointUsage();
            case SysmlPackage.VIEW_RENDERING_MEMBERSHIP: return createViewRenderingMembership();
            case SysmlPackage.VIEW_USAGE: return createViewUsage();
            case SysmlPackage.WHILE_LOOP_ACTION_USAGE: return createWhileLoopActionUsage();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object createFromString(EDataType eDataType, String initialValue) {
        switch (eDataType.getClassifierID()) {
            case SysmlPackage.FEATURE_DIRECTION_KIND:
                return createFeatureDirectionKindFromString(eDataType, initialValue);
            case SysmlPackage.PORTION_KIND:
                return createPortionKindFromString(eDataType, initialValue);
            case SysmlPackage.REQUIREMENT_CONSTRAINT_KIND:
                return createRequirementConstraintKindFromString(eDataType, initialValue);
            case SysmlPackage.STATE_SUBACTION_KIND:
                return createStateSubactionKindFromString(eDataType, initialValue);
            case SysmlPackage.TRANSITION_FEATURE_KIND:
                return createTransitionFeatureKindFromString(eDataType, initialValue);
            case SysmlPackage.TRIGGER_KIND:
                return createTriggerKindFromString(eDataType, initialValue);
            case SysmlPackage.VISIBILITY_KIND:
                return createVisibilityKindFromString(eDataType, initialValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String convertToString(EDataType eDataType, Object instanceValue) {
        switch (eDataType.getClassifierID()) {
            case SysmlPackage.FEATURE_DIRECTION_KIND:
                return convertFeatureDirectionKindToString(eDataType, instanceValue);
            case SysmlPackage.PORTION_KIND:
                return convertPortionKindToString(eDataType, instanceValue);
            case SysmlPackage.REQUIREMENT_CONSTRAINT_KIND:
                return convertRequirementConstraintKindToString(eDataType, instanceValue);
            case SysmlPackage.STATE_SUBACTION_KIND:
                return convertStateSubactionKindToString(eDataType, instanceValue);
            case SysmlPackage.TRANSITION_FEATURE_KIND:
                return convertTransitionFeatureKindToString(eDataType, instanceValue);
            case SysmlPackage.TRIGGER_KIND:
                return convertTriggerKindToString(eDataType, instanceValue);
            case SysmlPackage.VISIBILITY_KIND:
                return convertVisibilityKindToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public AcceptActionUsage createAcceptActionUsage() {
        AcceptActionUsageImpl acceptActionUsage = new AcceptActionUsageImpl();
        acceptActionUsage.setElementId(UUID.randomUUID().toString());
        return acceptActionUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ActionDefinition createActionDefinition() {
        ActionDefinitionImpl actionDefinition = new ActionDefinitionImpl();
        actionDefinition.setElementId(UUID.randomUUID().toString());
        return actionDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ActionUsage createActionUsage() {
        ActionUsageImpl actionUsage = new ActionUsageImpl();
        actionUsage.setElementId(UUID.randomUUID().toString());
        return actionUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ActorMembership createActorMembership() {
        ActorMembershipImpl actorMembership = new ActorMembershipImpl();
        actorMembership.setElementId(UUID.randomUUID().toString());
        return actorMembership;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public AllocationDefinition createAllocationDefinition() {
        AllocationDefinitionImpl allocationDefinition = new AllocationDefinitionImpl();
        allocationDefinition.setElementId(UUID.randomUUID().toString());
        return allocationDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public AllocationUsage createAllocationUsage() {
        AllocationUsageImpl allocationUsage = new AllocationUsageImpl();
        allocationUsage.setElementId(UUID.randomUUID().toString());
        return allocationUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public AnalysisCaseDefinition createAnalysisCaseDefinition() {
        AnalysisCaseDefinitionImpl analysisCaseDefinition = new AnalysisCaseDefinitionImpl();
        analysisCaseDefinition.setElementId(UUID.randomUUID().toString());
        return analysisCaseDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public AnalysisCaseUsage createAnalysisCaseUsage() {
        AnalysisCaseUsageImpl analysisCaseUsage = new AnalysisCaseUsageImpl();
        analysisCaseUsage.setElementId(UUID.randomUUID().toString());
        return analysisCaseUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public AnnotatingElement createAnnotatingElement() {
        AnnotatingElementImpl annotatingElement = new AnnotatingElementImpl();
        annotatingElement.setElementId(UUID.randomUUID().toString());
        return annotatingElement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Annotation createAnnotation() {
        AnnotationImpl annotation = new AnnotationImpl();
        annotation.setElementId(UUID.randomUUID().toString());
        return annotation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public AssertConstraintUsage createAssertConstraintUsage() {
        AssertConstraintUsageImpl assertConstraintUsage = new AssertConstraintUsageImpl();
        assertConstraintUsage.setElementId(UUID.randomUUID().toString());
        return assertConstraintUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public AssignmentActionUsage createAssignmentActionUsage() {
        AssignmentActionUsageImpl assignmentActionUsage = new AssignmentActionUsageImpl();
        assignmentActionUsage.setElementId(UUID.randomUUID().toString());
        return assignmentActionUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Association createAssociation() {
        AssociationImpl association = new AssociationImpl();
        association.setElementId(UUID.randomUUID().toString());
        return association;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public AssociationStructure createAssociationStructure() {
        AssociationStructureImpl associationStructure = new AssociationStructureImpl();
        associationStructure.setElementId(UUID.randomUUID().toString());
        return associationStructure;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public AttributeDefinition createAttributeDefinition() {
        AttributeDefinitionImpl attributeDefinition = new AttributeDefinitionImpl();
        attributeDefinition.setElementId(UUID.randomUUID().toString());
        return attributeDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public AttributeUsage createAttributeUsage() {
        AttributeUsageImpl attributeUsage = new AttributeUsageImpl();
        attributeUsage.setElementId(UUID.randomUUID().toString());
        return attributeUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Behavior createBehavior() {
        BehaviorImpl behavior = new BehaviorImpl();
        behavior.setElementId(UUID.randomUUID().toString());
        return behavior;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public BindingConnector createBindingConnector() {
        BindingConnectorImpl bindingConnector = new BindingConnectorImpl();
        bindingConnector.setElementId(UUID.randomUUID().toString());
        return bindingConnector;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public BindingConnectorAsUsage createBindingConnectorAsUsage() {
        BindingConnectorAsUsageImpl bindingConnectorAsUsage = new BindingConnectorAsUsageImpl();
        bindingConnectorAsUsage.setElementId(UUID.randomUUID().toString());
        return bindingConnectorAsUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public BooleanExpression createBooleanExpression() {
        BooleanExpressionImpl booleanExpression = new BooleanExpressionImpl();
        booleanExpression.setElementId(UUID.randomUUID().toString());
        return booleanExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public CalculationDefinition createCalculationDefinition() {
        CalculationDefinitionImpl calculationDefinition = new CalculationDefinitionImpl();
        calculationDefinition.setElementId(UUID.randomUUID().toString());
        return calculationDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public CalculationUsage createCalculationUsage() {
        CalculationUsageImpl calculationUsage = new CalculationUsageImpl();
        calculationUsage.setElementId(UUID.randomUUID().toString());
        return calculationUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public CaseDefinition createCaseDefinition() {
        CaseDefinitionImpl caseDefinition = new CaseDefinitionImpl();
        caseDefinition.setElementId(UUID.randomUUID().toString());
        return caseDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public CaseUsage createCaseUsage() {
        CaseUsageImpl caseUsage = new CaseUsageImpl();
        caseUsage.setElementId(UUID.randomUUID().toString());
        return caseUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public org.eclipse.syson.sysml.Class createClass() {
        ClassImpl class_ = new ClassImpl();
        class_.setElementId(UUID.randomUUID().toString());
        return class_;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Classifier createClassifier() {
        ClassifierImpl classifier = new ClassifierImpl();
        classifier.setElementId(UUID.randomUUID().toString());
        return classifier;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public CollectExpression createCollectExpression() {
        CollectExpressionImpl collectExpression = new CollectExpressionImpl();
        collectExpression.setElementId(UUID.randomUUID().toString());
        return collectExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Comment createComment() {
        CommentImpl comment = new CommentImpl();
        comment.setElementId(UUID.randomUUID().toString());
        return comment;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ConcernDefinition createConcernDefinition() {
        ConcernDefinitionImpl concernDefinition = new ConcernDefinitionImpl();
        concernDefinition.setElementId(UUID.randomUUID().toString());
        return concernDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ConcernUsage createConcernUsage() {
        ConcernUsageImpl concernUsage = new ConcernUsageImpl();
        concernUsage.setElementId(UUID.randomUUID().toString());
        return concernUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ConjugatedPortDefinition createConjugatedPortDefinition() {
        ConjugatedPortDefinitionImpl conjugatedPortDefinition = new ConjugatedPortDefinitionImpl();
        conjugatedPortDefinition.setElementId(UUID.randomUUID().toString());
        return conjugatedPortDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ConjugatedPortTyping createConjugatedPortTyping() {
        ConjugatedPortTypingImpl conjugatedPortTyping = new ConjugatedPortTypingImpl();
        conjugatedPortTyping.setElementId(UUID.randomUUID().toString());
        return conjugatedPortTyping;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Conjugation createConjugation() {
        ConjugationImpl conjugation = new ConjugationImpl();
        conjugation.setElementId(UUID.randomUUID().toString());
        return conjugation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ConnectionDefinition createConnectionDefinition() {
        ConnectionDefinitionImpl connectionDefinition = new ConnectionDefinitionImpl();
        connectionDefinition.setElementId(UUID.randomUUID().toString());
        return connectionDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ConnectionUsage createConnectionUsage() {
        ConnectionUsageImpl connectionUsage = new ConnectionUsageImpl();
        connectionUsage.setElementId(UUID.randomUUID().toString());
        return connectionUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Connector createConnector() {
        ConnectorImpl connector = new ConnectorImpl();
        connector.setElementId(UUID.randomUUID().toString());
        return connector;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ConstraintDefinition createConstraintDefinition() {
        ConstraintDefinitionImpl constraintDefinition = new ConstraintDefinitionImpl();
        constraintDefinition.setElementId(UUID.randomUUID().toString());
        return constraintDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ConstraintUsage createConstraintUsage() {
        ConstraintUsageImpl constraintUsage = new ConstraintUsageImpl();
        constraintUsage.setElementId(UUID.randomUUID().toString());
        return constraintUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public DataType createDataType() {
        DataTypeImpl dataType = new DataTypeImpl();
        dataType.setElementId(UUID.randomUUID().toString());
        return dataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public DecisionNode createDecisionNode() {
        DecisionNodeImpl decisionNode = new DecisionNodeImpl();
        decisionNode.setElementId(UUID.randomUUID().toString());
        return decisionNode;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Definition createDefinition() {
        DefinitionImpl definition = new DefinitionImpl();
        definition.setElementId(UUID.randomUUID().toString());
        return definition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Dependency createDependency() {
        DependencyImpl dependency = new DependencyImpl();
        dependency.setElementId(UUID.randomUUID().toString());
        return dependency;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Differencing createDifferencing() {
        DifferencingImpl differencing = new DifferencingImpl();
        differencing.setElementId(UUID.randomUUID().toString());
        return differencing;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Disjoining createDisjoining() {
        DisjoiningImpl disjoining = new DisjoiningImpl();
        disjoining.setElementId(UUID.randomUUID().toString());
        return disjoining;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Documentation createDocumentation() {
        DocumentationImpl documentation = new DocumentationImpl();
        documentation.setElementId(UUID.randomUUID().toString());
        return documentation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ElementFilterMembership createElementFilterMembership() {
        ElementFilterMembershipImpl elementFilterMembership = new ElementFilterMembershipImpl();
        elementFilterMembership.setElementId(UUID.randomUUID().toString());
        return elementFilterMembership;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EndFeatureMembership createEndFeatureMembership() {
        EndFeatureMembershipImpl endFeatureMembership = new EndFeatureMembershipImpl();
        endFeatureMembership.setElementId(UUID.randomUUID().toString());
        return endFeatureMembership;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EnumerationDefinition createEnumerationDefinition() {
        EnumerationDefinitionImpl enumerationDefinition = new EnumerationDefinitionImpl();
        enumerationDefinition.setElementId(UUID.randomUUID().toString());
        return enumerationDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EnumerationUsage createEnumerationUsage() {
        EnumerationUsageImpl enumerationUsage = new EnumerationUsageImpl();
        enumerationUsage.setElementId(UUID.randomUUID().toString());
        return enumerationUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public EventOccurrenceUsage createEventOccurrenceUsage() {
        EventOccurrenceUsageImpl eventOccurrenceUsage = new EventOccurrenceUsageImpl();
        eventOccurrenceUsage.setElementId(UUID.randomUUID().toString());
        return eventOccurrenceUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ExhibitStateUsage createExhibitStateUsage() {
        ExhibitStateUsageImpl exhibitStateUsage = new ExhibitStateUsageImpl();
        exhibitStateUsage.setElementId(UUID.randomUUID().toString());
        return exhibitStateUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Expression createExpression() {
        ExpressionImpl expression = new ExpressionImpl();
        expression.setElementId(UUID.randomUUID().toString());
        return expression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Feature createFeature() {
        FeatureImpl feature = new FeatureImpl();
        feature.setElementId(UUID.randomUUID().toString());
        return feature;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public FeatureChainExpression createFeatureChainExpression() {
        FeatureChainExpressionImpl featureChainExpression = new FeatureChainExpressionImpl();
        featureChainExpression.setElementId(UUID.randomUUID().toString());
        return featureChainExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public FeatureChaining createFeatureChaining() {
        FeatureChainingImpl featureChaining = new FeatureChainingImpl();
        featureChaining.setElementId(UUID.randomUUID().toString());
        return featureChaining;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public FeatureInverting createFeatureInverting() {
        FeatureInvertingImpl featureInverting = new FeatureInvertingImpl();
        featureInverting.setElementId(UUID.randomUUID().toString());
        return featureInverting;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public FeatureMembership createFeatureMembership() {
        FeatureMembershipImpl featureMembership = new FeatureMembershipImpl();
        featureMembership.setElementId(UUID.randomUUID().toString());
        return featureMembership;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public FeatureReferenceExpression createFeatureReferenceExpression() {
        FeatureReferenceExpressionImpl featureReferenceExpression = new FeatureReferenceExpressionImpl();
        featureReferenceExpression.setElementId(UUID.randomUUID().toString());
        return featureReferenceExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public FeatureTyping createFeatureTyping() {
        FeatureTypingImpl featureTyping = new FeatureTypingImpl();
        featureTyping.setElementId(UUID.randomUUID().toString());
        return featureTyping;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public FeatureValue createFeatureValue() {
        FeatureValueImpl featureValue = new FeatureValueImpl();
        featureValue.setElementId(UUID.randomUUID().toString());
        return featureValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public FlowConnectionDefinition createFlowConnectionDefinition() {
        FlowConnectionDefinitionImpl flowConnectionDefinition = new FlowConnectionDefinitionImpl();
        flowConnectionDefinition.setElementId(UUID.randomUUID().toString());
        return flowConnectionDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public FlowConnectionUsage createFlowConnectionUsage() {
        FlowConnectionUsageImpl flowConnectionUsage = new FlowConnectionUsageImpl();
        flowConnectionUsage.setElementId(UUID.randomUUID().toString());
        return flowConnectionUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ForkNode createForkNode() {
        ForkNodeImpl forkNode = new ForkNodeImpl();
        forkNode.setElementId(UUID.randomUUID().toString());
        return forkNode;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ForLoopActionUsage createForLoopActionUsage() {
        ForLoopActionUsageImpl forLoopActionUsage = new ForLoopActionUsageImpl();
        forLoopActionUsage.setElementId(UUID.randomUUID().toString());
        return forLoopActionUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public FramedConcernMembership createFramedConcernMembership() {
        FramedConcernMembershipImpl framedConcernMembership = new FramedConcernMembershipImpl();
        framedConcernMembership.setElementId(UUID.randomUUID().toString());
        return framedConcernMembership;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Function createFunction() {
        FunctionImpl function = new FunctionImpl();
        function.setElementId(UUID.randomUUID().toString());
        return function;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public IfActionUsage createIfActionUsage() {
        IfActionUsageImpl ifActionUsage = new IfActionUsageImpl();
        ifActionUsage.setElementId(UUID.randomUUID().toString());
        return ifActionUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public IncludeUseCaseUsage createIncludeUseCaseUsage() {
        IncludeUseCaseUsageImpl includeUseCaseUsage = new IncludeUseCaseUsageImpl();
        includeUseCaseUsage.setElementId(UUID.randomUUID().toString());
        return includeUseCaseUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Interaction createInteraction() {
        InteractionImpl interaction = new InteractionImpl();
        interaction.setElementId(UUID.randomUUID().toString());
        return interaction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public InterfaceDefinition createInterfaceDefinition() {
        InterfaceDefinitionImpl interfaceDefinition = new InterfaceDefinitionImpl();
        interfaceDefinition.setElementId(UUID.randomUUID().toString());
        return interfaceDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public InterfaceUsage createInterfaceUsage() {
        InterfaceUsageImpl interfaceUsage = new InterfaceUsageImpl();
        interfaceUsage.setElementId(UUID.randomUUID().toString());
        return interfaceUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Intersecting createIntersecting() {
        IntersectingImpl intersecting = new IntersectingImpl();
        intersecting.setElementId(UUID.randomUUID().toString());
        return intersecting;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Invariant createInvariant() {
        InvariantImpl invariant = new InvariantImpl();
        invariant.setElementId(UUID.randomUUID().toString());
        return invariant;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public InvocationExpression createInvocationExpression() {
        InvocationExpressionImpl invocationExpression = new InvocationExpressionImpl();
        invocationExpression.setElementId(UUID.randomUUID().toString());
        return invocationExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ItemDefinition createItemDefinition() {
        ItemDefinitionImpl itemDefinition = new ItemDefinitionImpl();
        itemDefinition.setElementId(UUID.randomUUID().toString());
        return itemDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ItemFeature createItemFeature() {
        ItemFeatureImpl itemFeature = new ItemFeatureImpl();
        itemFeature.setElementId(UUID.randomUUID().toString());
        return itemFeature;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ItemFlow createItemFlow() {
        ItemFlowImpl itemFlow = new ItemFlowImpl();
        itemFlow.setElementId(UUID.randomUUID().toString());
        return itemFlow;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ItemFlowEnd createItemFlowEnd() {
        ItemFlowEndImpl itemFlowEnd = new ItemFlowEndImpl();
        itemFlowEnd.setElementId(UUID.randomUUID().toString());
        return itemFlowEnd;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ItemUsage createItemUsage() {
        ItemUsageImpl itemUsage = new ItemUsageImpl();
        itemUsage.setElementId(UUID.randomUUID().toString());
        return itemUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public JoinNode createJoinNode() {
        JoinNodeImpl joinNode = new JoinNodeImpl();
        joinNode.setElementId(UUID.randomUUID().toString());
        return joinNode;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public LibraryPackage createLibraryPackage() {
        LibraryPackageImpl libraryPackage = new LibraryPackageImpl();
        libraryPackage.setElementId(UUID.randomUUID().toString());
        return libraryPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public LifeClass createLifeClass() {
        LifeClassImpl lifeClass = new LifeClassImpl();
        lifeClass.setElementId(UUID.randomUUID().toString());
        return lifeClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public LiteralBoolean createLiteralBoolean() {
        LiteralBooleanImpl literalBoolean = new LiteralBooleanImpl();
        literalBoolean.setElementId(UUID.randomUUID().toString());
        return literalBoolean;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public LiteralExpression createLiteralExpression() {
        LiteralExpressionImpl literalExpression = new LiteralExpressionImpl();
        literalExpression.setElementId(UUID.randomUUID().toString());
        return literalExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public LiteralInfinity createLiteralInfinity() {
        LiteralInfinityImpl literalInfinity = new LiteralInfinityImpl();
        literalInfinity.setElementId(UUID.randomUUID().toString());
        return literalInfinity;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public LiteralInteger createLiteralInteger() {
        LiteralIntegerImpl literalInteger = new LiteralIntegerImpl();
        literalInteger.setElementId(UUID.randomUUID().toString());
        return literalInteger;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public LiteralRational createLiteralRational() {
        LiteralRationalImpl literalRational = new LiteralRationalImpl();
        literalRational.setElementId(UUID.randomUUID().toString());
        return literalRational;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public LiteralString createLiteralString() {
        LiteralStringImpl literalString = new LiteralStringImpl();
        literalString.setElementId(UUID.randomUUID().toString());
        return literalString;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Membership createMembership() {
        MembershipImpl membership = new MembershipImpl();
        membership.setElementId(UUID.randomUUID().toString());
        return membership;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public MembershipExpose createMembershipExpose() {
        MembershipExposeImpl membershipExpose = new MembershipExposeImpl();
        membershipExpose.setElementId(UUID.randomUUID().toString());
        return membershipExpose;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public MembershipImport createMembershipImport() {
        MembershipImportImpl membershipImport = new MembershipImportImpl();
        membershipImport.setElementId(UUID.randomUUID().toString());
        return membershipImport;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public MergeNode createMergeNode() {
        MergeNodeImpl mergeNode = new MergeNodeImpl();
        mergeNode.setElementId(UUID.randomUUID().toString());
        return mergeNode;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Metaclass createMetaclass() {
        MetaclassImpl metaclass = new MetaclassImpl();
        metaclass.setElementId(UUID.randomUUID().toString());
        return metaclass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public MetadataAccessExpression createMetadataAccessExpression() {
        MetadataAccessExpressionImpl metadataAccessExpression = new MetadataAccessExpressionImpl();
        metadataAccessExpression.setElementId(UUID.randomUUID().toString());
        return metadataAccessExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public MetadataDefinition createMetadataDefinition() {
        MetadataDefinitionImpl metadataDefinition = new MetadataDefinitionImpl();
        metadataDefinition.setElementId(UUID.randomUUID().toString());
        return metadataDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public MetadataFeature createMetadataFeature() {
        MetadataFeatureImpl metadataFeature = new MetadataFeatureImpl();
        metadataFeature.setElementId(UUID.randomUUID().toString());
        return metadataFeature;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public MetadataUsage createMetadataUsage() {
        MetadataUsageImpl metadataUsage = new MetadataUsageImpl();
        metadataUsage.setElementId(UUID.randomUUID().toString());
        return metadataUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Multiplicity createMultiplicity() {
        MultiplicityImpl multiplicity = new MultiplicityImpl();
        multiplicity.setElementId(UUID.randomUUID().toString());
        return multiplicity;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public MultiplicityRange createMultiplicityRange() {
        MultiplicityRangeImpl multiplicityRange = new MultiplicityRangeImpl();
        multiplicityRange.setElementId(UUID.randomUUID().toString());
        return multiplicityRange;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Namespace createNamespace() {
        NamespaceImpl namespace = new NamespaceImpl();
        namespace.setElementId(UUID.randomUUID().toString());
        return namespace;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public NamespaceExpose createNamespaceExpose() {
        NamespaceExposeImpl namespaceExpose = new NamespaceExposeImpl();
        namespaceExpose.setElementId(UUID.randomUUID().toString());
        return namespaceExpose;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public NamespaceImport createNamespaceImport() {
        NamespaceImportImpl namespaceImport = new NamespaceImportImpl();
        namespaceImport.setElementId(UUID.randomUUID().toString());
        return namespaceImport;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public NullExpression createNullExpression() {
        NullExpressionImpl nullExpression = new NullExpressionImpl();
        nullExpression.setElementId(UUID.randomUUID().toString());
        return nullExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ObjectiveMembership createObjectiveMembership() {
        ObjectiveMembershipImpl objectiveMembership = new ObjectiveMembershipImpl();
        objectiveMembership.setElementId(UUID.randomUUID().toString());
        return objectiveMembership;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public OccurrenceDefinition createOccurrenceDefinition() {
        OccurrenceDefinitionImpl occurrenceDefinition = new OccurrenceDefinitionImpl();
        occurrenceDefinition.setElementId(UUID.randomUUID().toString());
        return occurrenceDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public OccurrenceUsage createOccurrenceUsage() {
        OccurrenceUsageImpl occurrenceUsage = new OccurrenceUsageImpl();
        occurrenceUsage.setElementId(UUID.randomUUID().toString());
        return occurrenceUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public OperatorExpression createOperatorExpression() {
        OperatorExpressionImpl operatorExpression = new OperatorExpressionImpl();
        operatorExpression.setElementId(UUID.randomUUID().toString());
        return operatorExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public OwningMembership createOwningMembership() {
        OwningMembershipImpl owningMembership = new OwningMembershipImpl();
        owningMembership.setElementId(UUID.randomUUID().toString());
        return owningMembership;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public org.eclipse.syson.sysml.Package createPackage() {
        PackageImpl package_ = new PackageImpl();
        package_.setElementId(UUID.randomUUID().toString());
        return package_;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ParameterMembership createParameterMembership() {
        ParameterMembershipImpl parameterMembership = new ParameterMembershipImpl();
        parameterMembership.setElementId(UUID.randomUUID().toString());
        return parameterMembership;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public PartDefinition createPartDefinition() {
        PartDefinitionImpl partDefinition = new PartDefinitionImpl();
        partDefinition.setElementId(UUID.randomUUID().toString());
        return partDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public PartUsage createPartUsage() {
        PartUsageImpl partUsage = new PartUsageImpl();
        partUsage.setElementId(UUID.randomUUID().toString());
        return partUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public PerformActionUsage createPerformActionUsage() {
        PerformActionUsageImpl performActionUsage = new PerformActionUsageImpl();
        performActionUsage.setElementId(UUID.randomUUID().toString());
        return performActionUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public PortConjugation createPortConjugation() {
        PortConjugationImpl portConjugation = new PortConjugationImpl();
        portConjugation.setElementId(UUID.randomUUID().toString());
        return portConjugation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public PortDefinition createPortDefinition() {
        PortDefinitionImpl portDefinition = new PortDefinitionImpl();
        portDefinition.setElementId(UUID.randomUUID().toString());
        return portDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public PortUsage createPortUsage() {
        PortUsageImpl portUsage = new PortUsageImpl();
        portUsage.setElementId(UUID.randomUUID().toString());
        return portUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Predicate createPredicate() {
        PredicateImpl predicate = new PredicateImpl();
        predicate.setElementId(UUID.randomUUID().toString());
        return predicate;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Redefinition createRedefinition() {
        RedefinitionImpl redefinition = new RedefinitionImpl();
        redefinition.setElementId(UUID.randomUUID().toString());
        return redefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ReferenceSubsetting createReferenceSubsetting() {
        ReferenceSubsettingImpl referenceSubsetting = new ReferenceSubsettingImpl();
        referenceSubsetting.setElementId(UUID.randomUUID().toString());
        return referenceSubsetting;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ReferenceUsage createReferenceUsage() {
        ReferenceUsageImpl referenceUsage = new ReferenceUsageImpl();
        referenceUsage.setElementId(UUID.randomUUID().toString());
        return referenceUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public RenderingDefinition createRenderingDefinition() {
        RenderingDefinitionImpl renderingDefinition = new RenderingDefinitionImpl();
        renderingDefinition.setElementId(UUID.randomUUID().toString());
        return renderingDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public RenderingUsage createRenderingUsage() {
        RenderingUsageImpl renderingUsage = new RenderingUsageImpl();
        renderingUsage.setElementId(UUID.randomUUID().toString());
        return renderingUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public RequirementConstraintMembership createRequirementConstraintMembership() {
        RequirementConstraintMembershipImpl requirementConstraintMembership = new RequirementConstraintMembershipImpl();
        requirementConstraintMembership.setElementId(UUID.randomUUID().toString());
        return requirementConstraintMembership;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public RequirementDefinition createRequirementDefinition() {
        RequirementDefinitionImpl requirementDefinition = new RequirementDefinitionImpl();
        requirementDefinition.setElementId(UUID.randomUUID().toString());
        return requirementDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public RequirementUsage createRequirementUsage() {
        RequirementUsageImpl requirementUsage = new RequirementUsageImpl();
        requirementUsage.setElementId(UUID.randomUUID().toString());
        return requirementUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public RequirementVerificationMembership createRequirementVerificationMembership() {
        RequirementVerificationMembershipImpl requirementVerificationMembership = new RequirementVerificationMembershipImpl();
        requirementVerificationMembership.setElementId(UUID.randomUUID().toString());
        return requirementVerificationMembership;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ResultExpressionMembership createResultExpressionMembership() {
        ResultExpressionMembershipImpl resultExpressionMembership = new ResultExpressionMembershipImpl();
        resultExpressionMembership.setElementId(UUID.randomUUID().toString());
        return resultExpressionMembership;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ReturnParameterMembership createReturnParameterMembership() {
        ReturnParameterMembershipImpl returnParameterMembership = new ReturnParameterMembershipImpl();
        returnParameterMembership.setElementId(UUID.randomUUID().toString());
        return returnParameterMembership;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public SatisfyRequirementUsage createSatisfyRequirementUsage() {
        SatisfyRequirementUsageImpl satisfyRequirementUsage = new SatisfyRequirementUsageImpl();
        satisfyRequirementUsage.setElementId(UUID.randomUUID().toString());
        return satisfyRequirementUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public SelectExpression createSelectExpression() {
        SelectExpressionImpl selectExpression = new SelectExpressionImpl();
        selectExpression.setElementId(UUID.randomUUID().toString());
        return selectExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public SendActionUsage createSendActionUsage() {
        SendActionUsageImpl sendActionUsage = new SendActionUsageImpl();
        sendActionUsage.setElementId(UUID.randomUUID().toString());
        return sendActionUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Specialization createSpecialization() {
        SpecializationImpl specialization = new SpecializationImpl();
        specialization.setElementId(UUID.randomUUID().toString());
        return specialization;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public StakeholderMembership createStakeholderMembership() {
        StakeholderMembershipImpl stakeholderMembership = new StakeholderMembershipImpl();
        stakeholderMembership.setElementId(UUID.randomUUID().toString());
        return stakeholderMembership;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public StateDefinition createStateDefinition() {
        StateDefinitionImpl stateDefinition = new StateDefinitionImpl();
        stateDefinition.setElementId(UUID.randomUUID().toString());
        return stateDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public StateSubactionMembership createStateSubactionMembership() {
        StateSubactionMembershipImpl stateSubactionMembership = new StateSubactionMembershipImpl();
        stateSubactionMembership.setElementId(UUID.randomUUID().toString());
        return stateSubactionMembership;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public StateUsage createStateUsage() {
        StateUsageImpl stateUsage = new StateUsageImpl();
        stateUsage.setElementId(UUID.randomUUID().toString());
        return stateUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Step createStep() {
        StepImpl step = new StepImpl();
        step.setElementId(UUID.randomUUID().toString());
        return step;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Structure createStructure() {
        StructureImpl structure = new StructureImpl();
        structure.setElementId(UUID.randomUUID().toString());
        return structure;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Subclassification createSubclassification() {
        SubclassificationImpl subclassification = new SubclassificationImpl();
        subclassification.setElementId(UUID.randomUUID().toString());
        return subclassification;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public SubjectMembership createSubjectMembership() {
        SubjectMembershipImpl subjectMembership = new SubjectMembershipImpl();
        subjectMembership.setElementId(UUID.randomUUID().toString());
        return subjectMembership;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Subsetting createSubsetting() {
        SubsettingImpl subsetting = new SubsettingImpl();
        subsetting.setElementId(UUID.randomUUID().toString());
        return subsetting;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Succession createSuccession() {
        SuccessionImpl succession = new SuccessionImpl();
        succession.setElementId(UUID.randomUUID().toString());
        return succession;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public SuccessionAsUsage createSuccessionAsUsage() {
        SuccessionAsUsageImpl successionAsUsage = new SuccessionAsUsageImpl();
        successionAsUsage.setElementId(UUID.randomUUID().toString());
        return successionAsUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public SuccessionFlowConnectionUsage createSuccessionFlowConnectionUsage() {
        SuccessionFlowConnectionUsageImpl successionFlowConnectionUsage = new SuccessionFlowConnectionUsageImpl();
        successionFlowConnectionUsage.setElementId(UUID.randomUUID().toString());
        return successionFlowConnectionUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public SuccessionItemFlow createSuccessionItemFlow() {
        SuccessionItemFlowImpl successionItemFlow = new SuccessionItemFlowImpl();
        successionItemFlow.setElementId(UUID.randomUUID().toString());
        return successionItemFlow;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public TextualRepresentation createTextualRepresentation() {
        TextualRepresentationImpl textualRepresentation = new TextualRepresentationImpl();
        textualRepresentation.setElementId(UUID.randomUUID().toString());
        return textualRepresentation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public TransitionFeatureMembership createTransitionFeatureMembership() {
        TransitionFeatureMembershipImpl transitionFeatureMembership = new TransitionFeatureMembershipImpl();
        transitionFeatureMembership.setElementId(UUID.randomUUID().toString());
        return transitionFeatureMembership;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public TransitionUsage createTransitionUsage() {
        TransitionUsageImpl transitionUsage = new TransitionUsageImpl();
        transitionUsage.setElementId(UUID.randomUUID().toString());
        return transitionUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public TriggerInvocationExpression createTriggerInvocationExpression() {
        TriggerInvocationExpressionImpl triggerInvocationExpression = new TriggerInvocationExpressionImpl();
        triggerInvocationExpression.setElementId(UUID.randomUUID().toString());
        return triggerInvocationExpression;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Type createType() {
        TypeImpl type = new TypeImpl();
        type.setElementId(UUID.randomUUID().toString());
        return type;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public TypeFeaturing createTypeFeaturing() {
        TypeFeaturingImpl typeFeaturing = new TypeFeaturingImpl();
        typeFeaturing.setElementId(UUID.randomUUID().toString());
        return typeFeaturing;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Unioning createUnioning() {
        UnioningImpl unioning = new UnioningImpl();
        unioning.setElementId(UUID.randomUUID().toString());
        return unioning;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public Usage createUsage() {
        UsageImpl usage = new UsageImpl();
        usage.setElementId(UUID.randomUUID().toString());
        return usage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public UseCaseDefinition createUseCaseDefinition() {
        UseCaseDefinitionImpl useCaseDefinition = new UseCaseDefinitionImpl();
        useCaseDefinition.setElementId(UUID.randomUUID().toString());
        return useCaseDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public UseCaseUsage createUseCaseUsage() {
        UseCaseUsageImpl useCaseUsage = new UseCaseUsageImpl();
        useCaseUsage.setElementId(UUID.randomUUID().toString());
        return useCaseUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public VariantMembership createVariantMembership() {
        VariantMembershipImpl variantMembership = new VariantMembershipImpl();
        variantMembership.setElementId(UUID.randomUUID().toString());
        return variantMembership;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public VerificationCaseDefinition createVerificationCaseDefinition() {
        VerificationCaseDefinitionImpl verificationCaseDefinition = new VerificationCaseDefinitionImpl();
        verificationCaseDefinition.setElementId(UUID.randomUUID().toString());
        return verificationCaseDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public VerificationCaseUsage createVerificationCaseUsage() {
        VerificationCaseUsageImpl verificationCaseUsage = new VerificationCaseUsageImpl();
        verificationCaseUsage.setElementId(UUID.randomUUID().toString());
        return verificationCaseUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ViewDefinition createViewDefinition() {
        ViewDefinitionImpl viewDefinition = new ViewDefinitionImpl();
        viewDefinition.setElementId(UUID.randomUUID().toString());
        return viewDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ViewpointDefinition createViewpointDefinition() {
        ViewpointDefinitionImpl viewpointDefinition = new ViewpointDefinitionImpl();
        viewpointDefinition.setElementId(UUID.randomUUID().toString());
        return viewpointDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ViewpointUsage createViewpointUsage() {
        ViewpointUsageImpl viewpointUsage = new ViewpointUsageImpl();
        viewpointUsage.setElementId(UUID.randomUUID().toString());
        return viewpointUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ViewRenderingMembership createViewRenderingMembership() {
        ViewRenderingMembershipImpl viewRenderingMembership = new ViewRenderingMembershipImpl();
        viewRenderingMembership.setElementId(UUID.randomUUID().toString());
        return viewRenderingMembership;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public ViewUsage createViewUsage() {
        ViewUsageImpl viewUsage = new ViewUsageImpl();
        viewUsage.setElementId(UUID.randomUUID().toString());
        return viewUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated NOT
     */
    @Override
    public WhileLoopActionUsage createWhileLoopActionUsage() {
        WhileLoopActionUsageImpl whileLoopActionUsage = new WhileLoopActionUsageImpl();
        whileLoopActionUsage.setElementId(UUID.randomUUID().toString());
        return whileLoopActionUsage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public FeatureDirectionKind createFeatureDirectionKindFromString(EDataType eDataType, String initialValue) {
        FeatureDirectionKind result = FeatureDirectionKind.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertFeatureDirectionKindToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PortionKind createPortionKindFromString(EDataType eDataType, String initialValue) {
        PortionKind result = PortionKind.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertPortionKindToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RequirementConstraintKind createRequirementConstraintKindFromString(EDataType eDataType, String initialValue) {
        RequirementConstraintKind result = RequirementConstraintKind.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertRequirementConstraintKindToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StateSubactionKind createStateSubactionKindFromString(EDataType eDataType, String initialValue) {
        StateSubactionKind result = StateSubactionKind.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertStateSubactionKindToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TransitionFeatureKind createTransitionFeatureKindFromString(EDataType eDataType, String initialValue) {
        TransitionFeatureKind result = TransitionFeatureKind.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertTransitionFeatureKindToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public TriggerKind createTriggerKindFromString(EDataType eDataType, String initialValue) {
        TriggerKind result = TriggerKind.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertTriggerKindToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public VisibilityKind createVisibilityKindFromString(EDataType eDataType, String initialValue) {
        VisibilityKind result = VisibilityKind.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertVisibilityKindToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public SysmlPackage getSysmlPackage() {
        return (SysmlPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static SysmlPackage getPackage() {
        return SysmlPackage.eINSTANCE;
    }

} //SysmlFactoryImpl
