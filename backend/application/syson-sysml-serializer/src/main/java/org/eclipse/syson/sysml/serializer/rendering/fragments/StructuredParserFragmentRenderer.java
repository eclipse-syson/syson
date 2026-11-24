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
package org.eclipse.syson.sysml.serializer.rendering.fragments;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.ElementDispatch;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.SerializationOptions;
import org.eclipse.syson.sysml.serializer.model.ModelFlags;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.model.MultiplicityText;
import org.eclipse.syson.sysml.serializer.model.NestedTargetSearch;
import org.eclipse.syson.sysml.serializer.model.SpecializationTargetNames;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.rendering.actions.TransitionRenderer;
import org.eclipse.syson.sysml.serializer.rendering.connections.ConnectorEndRenderer;
import org.eclipse.syson.sysml.serializer.rendering.declarations.DeclarationRenderer;
import org.eclipse.syson.sysml.serializer.syntax.ParserFragments;
import org.eclipse.syson.sysml.serializer.syntax.SourceSyntax;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Expression;
import org.omg.sysml.lang.sysml.FeatureTyping;
import org.omg.sysml.lang.sysml.FeatureValue;
import org.omg.sysml.lang.sysml.Multiplicity;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Renders parser-entry-point fragments that do not have a standalone model renderer. These fragments are intentionally
 * kept ahead of generic renderers because their parser entry point is part of their syntax contract.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class StructuredParserFragmentRenderer {

    private final TransitionRenderer transitions;

    private final DeclarationRenderer declarations;

    private final ElementDispatch dispatch;

    private final ReferenceResolver referenceResolver;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ModelFlags flags;

    private final MultiplicityText multiplicities;

    private final SpecializationTargetNames specializationTargets;

    private final NestedTargetSearch nestedTargets;

    private final ConnectorEndRenderer connectorEndRenderer;

    /**
     * Creates rendering for parser-rule fragments with nested structure, such as transitions and connector ends.
     *
     * @param transitions
     *            writes transition and effect syntax inside fragments
     * @param declarations
     *            writes shared declaration clauses and bodies
     * @param dispatch
     *            recursively renders nested elements and relationships through the session
     * @param referenceResolver
     *            supplies fragment names and reference spellings
     * @param referenceWriter
     *            emits fragment references and expressions with their target metadata
     * @param context
     *            supplies the parser entry point, output and model queries for this run
     * @param connectorEndRenderer
     *            writes connector-end names, multiplicities and reference clauses
     */
    public StructuredParserFragmentRenderer(final TransitionRenderer transitions,
            final DeclarationRenderer declarations,
            final ElementDispatch dispatch,
            final ReferenceResolver referenceResolver,
            final ExpressionAndReferenceWriter referenceWriter,
            final SerializationContext context,
            final ConnectorEndRenderer connectorEndRenderer) {
        this.transitions = transitions;
        this.declarations = declarations;
        this.dispatch = dispatch;
        this.referenceResolver = referenceResolver;
        this.referenceWriter = referenceWriter;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.flags = context.modelQueries().flags();
        this.multiplicities = context.modelQueries().multiplicities();
        this.specializationTargets = context.modelQueries().specializationTargets();
        this.nestedTargets = context.modelQueries().nestedTargets();
        this.connectorEndRenderer = connectorEndRenderer;
    }

    /**
     * Matches the root of a {@code MessageEvent} / {@code MessageEventMember} parse, whose text is only the event's feature chain.
     */
    public boolean canSerializeMessageEvent(final Element element) {
        final boolean event = SourceSyntax.isRootParserEntry(this.context.options(), element,
                ParserFragments.MESSAGE_EVENT)
                && this.navigation.isMetaclass(element,
                        SysMLPackage.eINSTANCE.getEventOccurrenceUsage());
        final boolean member = SourceSyntax.isRootParserEntry(this.context.options(), element,
                ParserFragments.MESSAGE_EVENT_MEMBER)
                && this.navigation.isMetaclass(element,
                        SysMLPackage.eINSTANCE.getParameterMembership());
        return event || member;
    }

    /**
     * Writes the event's feature chain (for example {@code a.b}) on its own line, unwrapping the member rule's membership.
     */
    public void serializeMessageEvent(final Element element) {
        Element event = element;
        if (element instanceof final Relationship relationship) {
            event = this.navigation.ownedRelatedElements(relationship).stream()
                    .findFirst().orElse(null);
        }
        String path = "";
        if (event != null) {
            path = this.referenceResolver.directReferencedElementTarget(event);
        }
        final TextOutput output = this.context.output();
        this.referenceWriter.writeReferenceText(output, path, event);
        output.newline();
    }

    /**
     * Matches a port or reference usage parsed as a default interface end, or as an end through the interface
     * occurrence usage rule, where the end has no {@code port}/{@code ref} keyword.
     */
    public boolean canSerializeDefaultInterfaceEnd(final Element element) {
        final boolean matchingEntry = SourceSyntax.isRootParserEntry(this.context.options(), element,
                ParserFragments.DEFAULT_INTERFACE_END)
                || SourceSyntax.isRootParserEntry(this.context.options(), element,
                        ParserFragments.INTERFACE_OCCURRENCE_USAGE_ELEMENT)
                        && this.flags.isEnd(element);
        final boolean matchingType = this.navigation.isMetaclass(element,
                SysMLPackage.eINSTANCE.getPortUsage())
                || this.navigation.isMetaclass(element,
                        SysMLPackage.eINSTANCE.getReferenceUsage());
        return matchingEntry && matchingType;
    }

    /** Writes {@code [end] name specializations [= value]} without the usage keyword, then {@code ;} or a statement body. */
    public void serializeDefaultInterfaceEnd(final Element element) {
        final TextOutput output = this.context.output();
        if (this.flags.isEnd(element)) {
            output.token(SysMLTokens.END_KEYWORD).space();
        }
        final String name = this.referenceResolver.declaredName(element);
        if (!name.isBlank()) {
            output.token(name);
        }
        this.declarations.serializeSpecializations(element, true);
        this.declarations.serializeFeatureValues(element);
        this.declarations.serializeStatementEnd(element);
    }

    /** Matches the detached feature value produced by a {@code FeatureValue} parse. */
    public boolean canSerializeFeatureValue(final Element element) {
        return SourceSyntax.isRootParserEntry(this.context.options(), element,
                ParserFragments.FEATURE_VALUE)
                && this.navigation.isMetaclass(element,
                        SysMLPackage.eINSTANCE.getFeatureValue());
    }

    /**
     * Writes {@code operator expression} for a detached feature value, using {@code ()} when the expression is missing so the
     * result stays parseable.
     */
    public void serializeFeatureValue(final Element element) {
        final FeatureValue value = (FeatureValue) element;
        final Expression valueExpression = this.navigation
                .featureValueExpression(value);
        String expressionText = "";
        if (valueExpression != null) {
            expressionText = this.referenceWriter.expressionText(valueExpression);
        }
        if (expressionText.isBlank()) {
            expressionText = SysMLTokens.EMPTY_ARGUMENT_LIST;
        }
        final TextOutput output = this.context.output();
        output.token(this.declarations.featureValueOperator(value)).space();
        this.referenceWriter.writeReferenceText(output, expressionText, value);
        output.newline();
    }

    /** Matches the root feature reference expression of a {@code BodyExpression} parse, which wraps an expression body. */
    public boolean canSerializeBodyExpression(final Element element) {
        return SourceSyntax.isRootParserEntry(this.context.options(), element,
                ParserFragments.BODY_EXPRESSION)
                && this.navigation.isMetaclass(element,
                        SysMLPackage.eINSTANCE
                                .getFeatureReferenceExpression());
    }

    /** Writes <code>{ ... }</code> with the members of the expression wrapped by the feature reference. */
    public void serializeBodyExpression(final Element element) {
        final Element body = this.navigation.firstOwnedExpression(element);
        final TextOutput output = this.context.output();
        output.beginBlock();
        if (body != null) {
            this.navigation.bodyRelationships(body)
                    .forEach(this.dispatch::serializeRelationship);
        }
        output.endBlock().newline();
    }

    /**
     * Matches the root expression of an {@code ExpressionBody} parse, or the expression owned by the root membership of an
     * {@code ExpressionBodyMember} parse.
     */
    public boolean canSerializeExpressionBody(final Element element) {
        final boolean entry = SourceSyntax.isRootParserEntry(this.context.options(), element,
                ParserFragments.EXPRESSION_BODY)
                || SourceSyntax.isParserEntryChild(this.context.options(), element,
                        ParserFragments.EXPRESSION_BODY_MEMBER);
        return entry && this.navigation.isMetaclass(element,
                SysMLPackage.eINSTANCE.getExpression());
    }

    /** Writes the expression's members in braces, as the <code>{ ... }</code> expression body rule spells them. */
    public void serializeExpressionBody(final Element element) {
        final TextOutput output = this.context.output();
        output.beginBlock();
        this.navigation.bodyRelationships(element)
                .forEach(this.dispatch::serializeRelationship);
        output.endBlock().newline();
    }

    /** Matches the root transition feature membership of an {@code EffectBehaviorMember} parse. */
    public boolean canSerializeEffectBehavior(final Element element) {
        return SourceSyntax.isRootParserEntry(this.context.options(), element,
                ParserFragments.EFFECT_BEHAVIOR_MEMBER)
                && this.navigation.isMetaclass(element,
                        SysMLPackage.eINSTANCE
                                .getTransitionFeatureMembership());
    }

    /**
     * Writes {@code do} followed by the owned effect action in the form a transition gives it (perform, send, accept or
     * assignment), without terminator since the {@code EffectBehaviorMember} grammar has none. Writes only {@code do}
     * when the membership owns nothing.
     */
    public void serializeEffectBehavior(final Element element) {
        this.context.output().token(SysMLTokens.DO_KEYWORD).space();
        this.navigation.ownedRelatedElements((Relationship) element).stream()
                .findFirst().ifPresent(this.transitions::serializeEffectBehaviorUsage);
        this.context.output().newline();
    }

    /** Matches an unnamed, valueless payload parameter or payload feature parse root whose text is a feature chain. */
    public boolean canSerializePayload(final Element element) {
        final boolean identified = !this.referenceResolver.declaredName(element).isBlank()
                || !this.context.nameRenderer().declaredShortName(element).isBlank();
        final boolean modified = this.flags.isOrdered(element) || this.flags.isNonUnique(element)
                || this.context.modelQueries().adapters().hasTextAdapter(element, ITextAdapter.PAYLOAD_TYPING_CLAUSE);
        if (!this.isPayloadParserFragment(element) || identified || modified || !this.navigation.featureValues(element).isEmpty()) {
            return false;
        }
        final List<Relationship> relationships = this.navigation.ownedRelationships(element);
        final long typings = relationships.stream().filter(FeatureTyping.class::isInstance).count();
        return typings == 1 && relationships.stream().allMatch(relationship -> relationship instanceof FeatureTyping
                || this.navigation.ownedRelatedElements(relationship).stream().anyMatch(Multiplicity.class::isInstance))
                && !this.payloadChainType(element).isBlank();
    }

    /** Returns only the anonymous chain owned by the payload's typing, excluding other specializations. */
    private String payloadChainType(final Element element) {
        return this.nestedTargets.firstChainedFeatureName(element, FeatureTyping.class, this.context.danglingReferenceIndex());
    }

    /**
     * Tells whether {@code element} is the root of one of the four payload parser rules: a reference usage for payload
     * parameters, a payload feature for payload features, owned by the root membership for the member rules.
     */
    private boolean isPayloadParserFragment(final Element element) {
        final SerializationOptions options = this.context.options();
        final boolean payloadParameter = SourceSyntax.isRootParserEntry(options, element,
                ParserFragments.PAYLOAD_PARAMETER)
                || SourceSyntax.isParserEntryChild(options, element, ParserFragments.PAYLOAD_PARAMETER_MEMBER);
        final boolean payloadFeature = SourceSyntax.isRootParserEntry(options, element,
                ParserFragments.PAYLOAD_FEATURE)
                || SourceSyntax.isParserEntryChild(options, element, ParserFragments.PAYLOAD_FEATURE_MEMBER);
        return payloadParameter && this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getReferenceUsage())
                || payloadFeature && this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getPayloadFeature());
    }

    /** Writes the payload chain and its multiplicity, in the order the source used ({@code [1] a.b} or {@code a.b[1]}). */
    public void serializePayload(final Element element) {
        final String target = this.payloadChainType(element);
        String multiplicity = this.multiplicities.multiplicityText(element,
                this.referenceWriter::expressionText);
        if (multiplicity.isBlank()) {
            multiplicity = this.multiplicities.crossFeatureMultiplicityText(element,
                    this.referenceWriter::expressionText);
        }
        final TextOutput output = this.context.output();
        if (this.isPayloadMultiplicityBeforeTarget(element)) {
            this.referenceWriter.writeReferenceText(output, multiplicity, element)
                    .space();
            this.referenceWriter.writeReferenceText(output, target, element);
        } else {
            this.referenceWriter.writeReferenceText(output, target, element);
            this.referenceWriter.writeReferenceText(output, multiplicity, element);
        }
        output.newline();
    }

    /**
     * Tells whether the multiplicity was written before the payload target, based on the order of owned relationships.
     */
    private boolean isPayloadMultiplicityBeforeTarget(final Element element) {
        int multiplicityIndex = -1;
        int typingIndex = -1;
        final List<Relationship> relationships = this.navigation.ownedRelationships(element);
        for (int i = 0; i < relationships.size(); i++) {
            final Relationship relationship = relationships.get(i);
            if (multiplicityIndex < 0
                    && this.navigation.ownedRelatedElements(relationship).stream()
                            .anyMatch(related -> this.navigation.isMetaclass(related,
                                    SysMLPackage.eINSTANCE.getMultiplicity())
                                    || this.navigation.isMetaclass(related,
                                            SysMLPackage.eINSTANCE
                                                    .getMultiplicityRange()))) {
                multiplicityIndex = i;
            }
            if (typingIndex < 0 && this.navigation.isMetaclass(relationship,
                    SysMLPackage.eINSTANCE.getFeatureTyping())) {
                typingIndex = i;
            }
        }
        // Without a typing relationship, captured multiplicity still precedes the payload target.
        return multiplicityIndex >= 0
                && (typingIndex < 0 || multiplicityIndex < typingIndex);
    }

    /**
     * Matches the root feature of a {@code ConstructorResult} parse, or the root return parameter membership of a
     * {@code ConstructorResultMember} parse.
     */
    public boolean canSerializeConstructorResult(final Element element) {
        final boolean directResult = SourceSyntax.isRootParserEntry(this.context.options(), element,
                ParserFragments.CONSTRUCTOR_RESULT)
                && this.navigation.isMetaclass(element,
                        SysMLPackage.eINSTANCE.getFeature());
        final boolean resultMember = SourceSyntax.isRootParserEntry(this.context.options(), element,
                ParserFragments.CONSTRUCTOR_RESULT_MEMBER)
                && this.navigation.isMetaclass(element,
                        SysMLPackage.eINSTANCE.getReturnParameterMembership());
        return directResult || resultMember;
    }

    /** Writes the constructor argument list {@code (a, name = b)} from the parameter memberships of the result feature. */
    public void serializeConstructorResult(final Element element) {
        Element argumentContainer = element;
        if (element instanceof final Relationship relationship) {
            argumentContainer = this.navigation.ownedRelatedElements(relationship)
                    .stream().findFirst().orElse(null);
        }
        List<Element> arguments = List.of();
        if (argumentContainer != null) {
            arguments = this.navigation.bodyRelationships(argumentContainer).stream()
                    .filter(relationship -> this.navigation.isMetaclass(relationship,
                            SysMLPackage.eINSTANCE.getParameterMembership()))
                    .flatMap(relationship -> this.navigation
                            .ownedRelatedElements(relationship).stream())
                    .toList();
        }
        final String text = arguments.stream()
                .map(this::constructorArgumentText)
                .collect(Collectors
                        .joining(SysMLTokens.ARGUMENT_SEPARATOR));
        final TextOutput output = this.context.output()
                .token(SysMLTokens.OPEN_PARENTHESIS);
        this.referenceWriter.writeReferenceText(output, text, element)
                .token(SysMLTokens.CLOSE_PARENTHESIS)
                .newline();
    }

    /**
     * Returns one argument as {@code value} or, when the argument redefines a parameter, {@code name = value}; a missing value
     * becomes {@code ()}.
     */
    private String constructorArgumentText(final Element argument) {
        final String valueText = this.navigation.featureValues(argument).stream()
                .map(this.navigation::featureValueExpression)
                .map(this.referenceWriter::expressionText)
                .filter(text -> !text.isBlank()).findFirst()
                .orElse(SysMLTokens.EMPTY_ARGUMENT_LIST);
        final String name = this.specializationTargets
                .redefinedFeatureNames(argument,
                        this.context.danglingReferenceIndex(),
                        this.context.nameRenderer()::referenceName)
                .stream().findFirst().orElse("");
        if (name.isBlank()) {
            return valueText;
        }
        return name + SysMLTokens.EQUALS_INFIX + valueText;
    }

    /**
     * Matches the root port or reference usage of an interface or connector end parse, directly or through its member
     * rule.
     */
    public boolean canSerializeInterfaceEnd(final Element element) {
        final SerializationOptions options = this.context.options();
        final boolean rootEnd = SourceSyntax.isRootParserEntry(options, element, ParserFragments.INTERFACE_END)
                || SourceSyntax.isRootParserEntry(options, element, ParserFragments.CONNECTOR_END);
        final boolean memberEnd = SourceSyntax.isParserEntryChild(options, element, ParserFragments.INTERFACE_END_MEMBER)
                || SourceSyntax.isParserEntryChild(options, element, ParserFragments.CONNECTOR_END_MEMBER);
        final boolean endElement = this.navigation.isMetaclass(element,
                SysMLPackage.eINSTANCE.getPortUsage())
                || this.navigation.isMetaclass(element,
                        SysMLPackage.eINSTANCE.getReferenceUsage());
        return endElement && (rootEnd || memberEnd);
    }

    /** Writes an interface or connector end with the shared end syntax, on its own line. */
    public void serializeInterfaceEnd(final Element element) {
        this.connectorEndRenderer.writeInterfaceEnd(this.context.output(),
                element);
        this.context.output().newline();
    }


}
