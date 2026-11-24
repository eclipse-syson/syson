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
package org.eclipse.syson.sysml.serializer.syntax;

/**
 * Parser entry-point names used when serializing grammar fragments.
 * <p>
 * A model may be the result of parsing a single grammar rule rather than a whole file (see
 * {@link org.eclipse.syson.sysml.serializer.SerializationOptions#parserEntryPoint()}). The fragment root then has no
 * container, and several rules can build the same metaclass with different syntax, so renderers compare the
 * configured entry point with these names to restore the rule's exact form (terminator, keyword, implicit parts).
 * </p>
 * <p>
 * Each value is the name of the corresponding {@code SysMLModelParser} method, i.e. {@code "parse"} followed by the
 * SysML v2 grammar rule name; the constant is the rule name in upper snake case. A {@code *_MEMBER} entry parses the
 * owning membership of the rule without the suffix: its root is the relationship and the interesting element is its
 * direct child (see {@link SourceSyntax#isParserEntryChild}). Constants are kept in alphabetical order. Add one only
 * when a renderer needs to recognize that rule; the value must match the parser method name exactly.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class ParserFragments {

    public static final String ACCEPT_NODE = "parseAcceptNode";

    public static final String ACTION_BODY_PARAMETER = "parseActionBodyParameter";

    public static final String ACTION_NODE = "parseActionNode";

    public static final String ACTION_TARGET_SUCCESSION = "parseActionTargetSuccession";

    public static final String ACTOR_USAGE = "parseActorUsage";

    public static final String ALIAS_MEMBER = "parseAliasMember";

    public static final String ARGUMENT = "parseArgument";

    public static final String ARGUMENT_EXPRESSION = "parseArgumentExpression";

    public static final String ARGUMENT_EXPRESSION_MEMBER = "parseArgumentExpressionMember";

    public static final String ARGUMENT_MEMBER = "parseArgumentMember";

    public static final String ASSIGNMENT_TARGET_MEMBER = "parseAssignmentTargetMember";

    public static final String BINDING_CONNECTOR_AS_USAGE = "parseBindingConnectorAsUsage";

    public static final String BODY_EXPRESSION = "parseBodyExpression";

    public static final String BODY_PARAMETER = "parseBodyParameter";

    public static final String BODY_PARAMETER_MEMBER = "parseBodyParameterMember";

    public static final String CONNECTOR_END = "parseConnectorEnd";

    public static final String CONNECTOR_END_MEMBER = "parseConnectorEndMember";

    public static final String CONSTRUCTOR_RESULT = "parseConstructorResult";

    public static final String CONSTRUCTOR_RESULT_MEMBER = "parseConstructorResultMember";

    public static final String DEFAULT_INTERFACE_END = "parseDefaultInterfaceEnd";

    public static final String DEFAULT_TARGET_SUCCESSION = "parseDefaultTargetSuccession";

    public static final String EFFECT_BEHAVIOR_MEMBER = "parseEffectBehaviorMember";

    public static final String EFFECT_BEHAVIOR_USAGE = "parseEffectBehaviorUsage";

    /**
     * Entry point for the right operand of a short-circuit {@code and}: a feature reference expression wrapping an
     * equality expression, so the operand is evaluated lazily.
     */
    public static final String ENUMERATED_VALUE = "parseEnumeratedValue";

    public static final String ENUMERATION_USAGE_MEMBER = "parseEnumerationUsageMember";

    public static final String EQUALITY_EXPRESSION_REFERENCE = "parseEqualityExpressionReference";

    public static final String EVENT_OCCURRENCE_USAGE = "parseEventOccurrenceUsage";

    public static final String EXHIBIT_STATE_USAGE = "parseExhibitStateUsage";

    public static final String EXPRESSION_BODY = "parseExpressionBody";

    public static final String EXPRESSION_BODY_MEMBER = "parseExpressionBodyMember";

    /** Entry point for the {@code a.b.} prefix of a feature chain, which builds a {@code Feature} without a name. */
    public static final String FEATURE_CHAIN_PREFIX = "parseFeatureChainPrefix";

    public static final String FEATURE_VALUE = "parseFeatureValue";

    public static final String FILTER_PACKAGE = "parseFilterPackage";

    public static final String FILTER_PACKAGE_IMPORT = "parseFilterPackageImport";

    public static final String FILTER_PACKAGE_MEMBER = "parseFilterPackageMember";

    public static final String FILTER_PACKAGE_MEMBERSHIP_IMPORT = "parseFilterPackageMembershipImport";

    public static final String FILTER_PACKAGE_NAMESPACE_IMPORT = "parseFilterPackageNamespaceImport";

    public static final String FLOW_END = "parseFlowEnd";

    public static final String FLOW_END_MEMBER = "parseFlowEndMember";

    public static final String FLOW_END_SUBSETTING = "parseFlowEndSubsetting";

    public static final String FLOW_FEATURE = "parseFlowFeature";

    public static final String FLOW_FEATURE_MEMBER = "parseFlowFeatureMember";

    public static final String FLOW_USAGE = "parseFlowUsage";

    public static final String FOR_VARIABLE_DECLARATION = "parseForVariableDeclaration";

    public static final String FOR_VARIABLE_DECLARATION_MEMBER = "parseForVariableDeclarationMember";

    public static final String GUARDED_SUCCESSION = "parseGuardedSuccession";

    public static final String GUARDED_SUCCESSION_MEMBER = "parseGuardedSuccessionMember";

    public static final String GUARDED_TARGET_SUCCESSION = "parseGuardedTargetSuccession";

    public static final String GUARD_EXPRESSION_MEMBER = "parseGuardExpressionMember";

    public static final String INCLUDE_USE_CASE_USAGE = "parseIncludeUseCaseUsage";

    public static final String INDIVIDUAL_USAGE = "parseIndividualUsage";

    public static final String INITIAL_NODE_MEMBER = "parseInitialNodeMember";

    public static final String INTERFACE_END = "parseInterfaceEnd";

    public static final String INTERFACE_END_MEMBER = "parseInterfaceEndMember";

    public static final String INTERFACE_NON_OCCURRENCE_USAGE_ELEMENT = "parseInterfaceNonOccurrenceUsageElement";

    public static final String INTERFACE_NON_OCCURRENCE_USAGE_MEMBER = "parseInterfaceNonOccurrenceUsageMember";

    public static final String INTERFACE_OCCURRENCE_USAGE_ELEMENT = "parseInterfaceOccurrenceUsageElement";

    public static final String LIBRARY_PACKAGE = "parseLibraryPackage";

    public static final String MESSAGE = "parseMessage";

    public static final String MESSAGE_EVENT = "parseMessageEvent";

    public static final String MESSAGE_EVENT_MEMBER = "parseMessageEventMember";

    public static final String METADATA_BODY_USAGE = "parseMetadataBodyUsage";

    public static final String METADATA_BODY_USAGE_MEMBER = "parseMetadataBodyUsageMember";

    public static final String METADATA_REFERENCE = "parseMetadataReference";

    public static final String MULTIPLICITY_RANGE = "parseMultiplicityRange";

    public static final String MULTIPLICITY_SOURCE_END = "parseMultiplicitySourceEnd";

    public static final String MULTIPLICITY_SOURCE_END_MEMBER = "parseMultiplicitySourceEndMember";

    public static final String NAMED_ARGUMENT = "parseNamedArgument";

    public static final String NAMED_ARGUMENT_MEMBER = "parseNamedArgumentMember";

    public static final String NODE_PARAMETER = "parseNodeParameter";

    public static final String NODE_PARAMETER_MEMBER = "parseNodeParameterMember";

    public static final String OBJECTIVE_REQUIREMENT_USAGE = "parseObjectiveRequirementUsage";

    public static final String OCCURRENCE_DEFINITION = "parseOccurrenceDefinition";

    public static final String OWNED_CROSS_FEATURE_MEMBER = "parseOwnedCrossFeatureMember";

    public static final String OWNED_CROSS_MULTIPLICITY = "parseOwnedCrossMultiplicity";

    public static final String OWNED_CROSS_MULTIPLICITY_MEMBER = "parseOwnedCrossMultiplicityMember";

    public static final String OWNED_FEATURE_CHAINING = "parseOwnedFeatureChaining";

    public static final String OWNED_MULTIPLICITY = "parseOwnedMultiplicity";

    public static final String OWNED_SUBCLASSIFICATION = "parseOwnedSubclassification";

    /** Entry point of the {@code Package} rule, named {@code package_} in the grammar ({@code package} is reserved). */
    public static final String PACKAGE = "parsePackage_";

    public static final String PAYLOAD_FEATURE = "parsePayloadFeature";

    public static final String PAYLOAD_FEATURE_MEMBER = "parsePayloadFeatureMember";

    public static final String PAYLOAD_PARAMETER = "parsePayloadParameter";

    public static final String PAYLOAD_PARAMETER_MEMBER = "parsePayloadParameterMember";

    public static final String PERFORMED_ACTION_USAGE = "parsePerformedActionUsage";

    public static final String PORTION_USAGE = "parsePortionUsage";

    public static final String PREFIX_METADATA_MEMBER = "parsePrefixMetadataMember";

    public static final String PREFIX_METADATA_USAGE = "parsePrefixMetadataUsage";

    public static final String REFERENCE_USAGE = "parseReferenceUsage";

    public static final String REQUIREMENT_USAGE = "parseRequirementUsage";

    public static final String REQUIREMENT_VERIFICATION_USAGE = "parseRequirementVerificationUsage";

    /** Entry point for a complete document: its root is the unnamed namespace wrapping the file's members. */
    public static final String ROOT_NAMESPACE = "parseRootNamespace";

    public static final String SATISFACTION_PARAMETER = "parseSatisfactionParameter";

    public static final String SATISFACTION_SUBJECT_MEMBER = "parseSatisfactionSubjectMember";

    public static final String SATISFY_REQUIREMENT_USAGE = "parseSatisfyRequirementUsage";

    public static final String SEQUENCE_EXPRESSION = "parseSequenceExpression";

    public static final String STAKEHOLDER_USAGE = "parseStakeholderUsage";

    public static final String STATE_ACTION_USAGE = "parseStateActionUsage";

    public static final String SUBJECT_USAGE = "parseSubjectUsage";

    public static final String SUCCESSION_FLOW_USAGE = "parseSuccessionFlowUsage";

    public static final String TARGET_EXPRESSION = "parseTargetExpression";

    public static final String TARGET_PARAMETER = "parseTargetParameter";

    public static final String TARGET_SUCCESSION = "parseTargetSuccession";

    public static final String TARGET_TRANSITION_USAGE = "parseTargetTransitionUsage";

    public static final String TARGET_TRANSITION_USAGE_MEMBER = "parseTargetTransitionUsageMember";

    public static final String TERMINATE_NODE = "parseTerminateNode";

    public static final String TRANSITION_SUCCESSION = "parseTransitionSuccession";

    public static final String TRANSITION_SUCCESSION_MEMBER = "parseTransitionSuccessionMember";

    public static final String TRIGGER_ACTION = "parseTriggerAction";

    public static final String TRIGGER_ACTION_MEMBER = "parseTriggerActionMember";

    public static final String TYPE_REFERENCE = "parseTypeReference";

    public static final String TYPE_REFERENCE_MEMBER = "parseTypeReferenceMember";

    /** Entry point for the target type of a cast ({@code as} / {@code meta}), owned as a return-parameter member. */
    public static final String TYPE_RESULT_MEMBER = "parseTypeResultMember";

    public static final String VARIANT_REFERENCE = "parseVariantReference";

    public static final String VARIANT_USAGE_ELEMENT = "parseVariantUsageElement";

    private ParserFragments() {
    }
}
