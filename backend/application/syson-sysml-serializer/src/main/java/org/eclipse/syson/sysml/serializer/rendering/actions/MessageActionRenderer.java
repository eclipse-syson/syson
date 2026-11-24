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
package org.eclipse.syson.sysml.serializer.rendering.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.syson.sysml.serializer.ElementDispatch;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.model.ElementSyntaxQueries;
import org.eclipse.syson.sysml.serializer.model.ModelFlags;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.model.MultiplicityText;
import org.eclipse.syson.sysml.serializer.model.ParserAdapterQueries;
import org.eclipse.syson.sysml.serializer.model.SpecializationTargetNames;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.rendering.declarations.DeclarationRenderer;
import org.eclipse.syson.sysml.serializer.syntax.ParserFragments;
import org.eclipse.syson.sysml.serializer.syntax.SourceSyntax;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Expression;
import org.omg.sysml.lang.sysml.FeatureValue;
import org.omg.sysml.lang.sysml.ReferenceUsage;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.lang.sysml.TriggerInvocationExpression;

/**
 * Writes {@code accept} and {@code send} actions, both as statements ({@code accept s : Signal via p;},
 * {@code send s via a to b;}) and as inline texts used for transition triggers and effects. Payload, sender and
 * receiver are read from the action's parameters in order: payload first, receiver last, and for a send with three
 * parameters the sender in between.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
final class MessageActionRenderer {

    private final DeclarationRenderer declarations;

    private final ElementDispatch dispatch;

    private final ReferenceResolver referenceResolver;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final ElementSyntaxQueries syntax;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ModelFlags flags;

    private final MultiplicityText multiplicities;

    private final ParserAdapterQueries adapters;

    private final SpecializationTargetNames specializationTargets;

    private final ActionExpressionSupport expressionSupport;

    private final ActionNodePrefixRenderer prefixRenderer;

    private final ParameterActionBodyWriter bodyWriter;

    /**
     * Creates the renderer.
     *
     * @param declarations
     *            writes statement ends and comments
     * @param dispatch
     *            dispatches body relationships of a send action
     * @param referenceResolver
     *            resolves declared and referenced names
     * @param referenceWriter
     *            writes expressions and references with traceability metadata
     * @param syntax
     *            syntax queries on elements
     * @param context
     *            the serialization context
     * @param expressionSupport
     *            shared helper for expression-valued parameters
     */
    MessageActionRenderer(final DeclarationRenderer declarations,
            final ElementDispatch dispatch,
            final ReferenceResolver referenceResolver,
            final ExpressionAndReferenceWriter referenceWriter,
            final ElementSyntaxQueries syntax,
            final SerializationContext context,
            final ActionExpressionSupport expressionSupport) {
        this.declarations = declarations;
        this.dispatch = dispatch;
        this.referenceResolver = referenceResolver;
        this.referenceWriter = referenceWriter;
        this.syntax = syntax;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.flags = context.modelQueries().flags();
        this.multiplicities = context.modelQueries().multiplicities();
        this.adapters = context.modelQueries().adapters();
        this.specializationTargets = context.modelQueries().specializationTargets();
        this.expressionSupport = expressionSupport;
        this.prefixRenderer = new ActionNodePrefixRenderer(declarations, referenceResolver, context);
        this.bodyWriter = new ParameterActionBodyWriter(declarations, syntax, context);
    }

    /**
     * Writes an accept action statement: {@code [action [name]] accept payload [via receiver];}. A named or explicitly
     * keyworded node keeps its {@code action} prefix. In a standalone trigger-action fragment the {@code accept}
     * keyword and the terminator are omitted, because the fragment grammar starts at the payload.
     *
     * @param acceptActionUsage
     *            the accept action usage
     */
    void serializeAccept(final Element acceptActionUsage) {
        final TextOutput output = this.context.output();
        final boolean triggerActionFragment = SourceSyntax.isRootParserEntry(this.context.options(),
                acceptActionUsage, ParserFragments.TRIGGER_ACTION);
        final AcceptActionFacts facts = this.acceptActionFacts(acceptActionUsage);
        this.writeActionNodeDeclaration(acceptActionUsage);
        if (!triggerActionFragment) {
            output.token(SysMLTokens.ACCEPT_KEYWORD);
        }
        if (!facts.payload().isBlank()) {
            this.expressionSupport.writeParameterText(output.space(), facts.payload(), facts.payloadExpression(),
                    acceptActionUsage);
        }
        if (!facts.receiver().isBlank()) {
            output.space().token(SysMLTokens.VIA_KEYWORD).space();
            this.expressionSupport.writeParameterText(output, facts.receiver(),
                    facts.receiverExpression(), acceptActionUsage);
        }
        if (triggerActionFragment) {
            output.newline();
        } else {
            this.bodyWriter.writeBodyOrStatementEnd(acceptActionUsage);
        }
    }

    /**
     * Writes the {@code ActionNodePrefix} of an accept or send statement: its {@code OccurrenceUsagePrefix} and its
     * {@code action} usage declaration when it has one.
     */
    private void writeActionNodeDeclaration(final Element actionNode) {
        this.prefixRenderer.writeActionNodePrefix(actionNode);
    }

    /**
     * Writes a send action statement: {@code [action [name]] send payload [via sender to receiver | via|to receiver]},
     * followed by its body when it has explicit members, otherwise by the statement end. With two parameters the
     * receiver is the {@code via} endpoint.
     *
     * @param sendActionUsage
     *            the send action usage
     */
    void serializeSend(final Element sendActionUsage) {
        final TextOutput output = this.context.output();
        final SendActionFacts facts = this.sendActionFacts(sendActionUsage);
        this.writeActionNodeDeclaration(sendActionUsage);
        output.token(SysMLTokens.SEND_KEYWORD).space();
        this.expressionSupport.writeParameterText(output, facts.payload(),
                facts.payloadExpression(), sendActionUsage);
        if (!facts.receiver().isBlank()) {
            if (!facts.sender().isBlank()) {
                output.space().token(SysMLTokens.VIA_KEYWORD).space();
                this.expressionSupport.writeParameterText(output, facts.sender(),
                        facts.senderExpression(), sendActionUsage);
                output.space().token(SysMLTokens.TO_KEYWORD).space();
                this.expressionSupport.writeParameterText(output, facts.receiver(),
                        facts.receiverExpression(), sendActionUsage);
            } else {
                output.space().token(facts.receiverKeyword()).space();
                this.expressionSupport.writeParameterText(output, facts.receiver(),
                        facts.receiverExpression(), sendActionUsage);
            }
        }
        if (this.hasSendBody(sendActionUsage)) {
            this.serializeSendBody(sendActionUsage);
            output.newline();
        } else {
            this.declarations.serializeStatementEnd(sendActionUsage);
        }
    }

    /**
     * Returns the inline accept text used as a transition trigger: {@code accept payload [via receiver]}. No
     * terminator.
     *
     * @param acceptActionUsage
     *            the accept action usage
     * @return the accept text
     */
    String acceptActionText(final Element acceptActionUsage) {
        final AcceptActionFacts facts = this.acceptActionFacts(acceptActionUsage);
        final String payload = facts.payload();
        if (facts.receiver().isBlank()) {
            return SysMLTokens.ACCEPT_PREFIX + payload;
        }
        return SysMLTokens.ACCEPT_PREFIX + payload + SysMLTokens.VIA_INFIX
                + facts.receiver();
    }

    /**
     * Returns the inline send text used as a transition effect, with the same {@code action [name]} prefix and via/to
     * forms as {@link #serializeSend(Element)}. No terminator and no body.
     *
     * @param sendActionUsage
     *            the send action usage
     * @return the send text
     */
    String sendActionText(final Element sendActionUsage) {
        final SendActionFacts facts = this.sendActionFacts(sendActionUsage);
        final String receiver = facts.receiver();
        String text;
        if (receiver.isBlank()) {
            text = SysMLTokens.SEND_PREFIX + facts.payload();
        } else if (!facts.sender().isBlank()) {
            text = SysMLTokens.SEND_PREFIX + facts.payload()
                    + SysMLTokens.VIA_INFIX + facts.sender()
                    + SysMLTokens.TO_INFIX + receiver;
        } else {
            text = SysMLTokens.SEND_PREFIX + facts.payload() + SysMLTokens.SPACE
                    + facts.receiverKeyword() + SysMLTokens.SPACE + receiver;
        }
        return this.prefixRenderer.inlineActionNodeDeclarationText(sendActionUsage) + text;
    }

    /**
     * Tells whether a send action has user-written body members (imports, or named, directed or typed features) that
     * need a {@code { ... }} block. Generated and elided anonymous reference members do not count.
     *
     * @param sendActionUsage
     *            the send action usage
     * @return {@code true} when a body block must be written
     */
    boolean hasSendBody(final Element sendActionUsage) {
        return this.navigation.bodyRelationships(sendActionUsage).stream()
                .anyMatch(this::isSendBodyRelationship);
    }

    /** Tells whether a body relationship of a send action is user-written content rather than parameter plumbing. */
    private boolean isSendBodyRelationship(final Relationship relationship) {
        return this.navigation.isMetaclass(relationship, SysMLPackage.eINSTANCE.getImport())
                || this.navigation.isMetaclass(relationship, SysMLPackage.eINSTANCE.getMembershipImport())
                || this.navigation.ownedRelatedElements(relationship).stream()
                        .anyMatch(this::isExplicitSendBodyElement);
    }

    /**
     * Tells whether a send body member is one the user wrote: not generated, not an elided anonymous reference, and
     * carrying a name, direction or type.
     */
    private boolean isExplicitSendBodyElement(final Element element) {
        final boolean anonymousReference = element instanceof ReferenceUsage
                && this.referenceResolver.declaredName(element).isBlank();
        if (this.syntax.isAnonymousGeneratedElement(element)
                || anonymousReference && this.context.options()
                        .implicitReferenceKeywordElision()) {
            return false;
        }
        return !this.referenceResolver.declaredName(element).isBlank()
                || !this.flags.direction(element).isBlank()
                || !this.specializationTargets
                        .featureTypeNames(element,
                                this.context.danglingReferenceIndex(),
                                this.context.nameRenderer()::referenceName)
                        .isEmpty();
    }

    /**
     * Writes the {@code { ... }} body of a send action with only its user-written members (see
     * {@link #hasSendBody(Element)}) and trailing comments. The caller writes the text before it and the newline after.
     *
     * @param sendActionUsage
     *            the send action usage
     */
    void serializeSendBody(final Element sendActionUsage) {
        final TextOutput output = this.context.output();
        output.space().beginBlock();
        for (final Relationship relationship : this.navigation
                .bodyRelationships(sendActionUsage)) {
            if (this.isSendBodyRelationship(relationship)) {
                this.dispatch.serializeRelationship(relationship);
            }
        }
        this.declarations.serializeTrailingComments(sendActionUsage);
        output.endBlock();
    }

    /** Collects the payload text and receiver of an accept action with their source expressions for traceability. */
    private AcceptActionFacts acceptActionFacts(final Element action) {
        final List<Element> parameters = this.navigation.parameterElements(action);
        return new AcceptActionFacts(this.acceptPayloadText(parameters),
                this.expressionSupport.parameterExpression(parameters, 0),
                this.receiverText(parameters),
                this.expressionSupport.parameterExpression(parameters,
                        parameters.size() - 1));
    }

    /**
     * Returns the accept payload text, matching the payload forms of the grammar: a {@code new} instance expression,
     * the payload declaration ({@code identification? PayloadFeatureSpecializationPart?}) followed by its value when it
     * has one (see {@link #payloadValueText(Element, String)}), a value part alone, or finally the path of the payload
     * feature. Empty when the action has no parameter.
     */
    private String acceptPayloadText(final List<Element> parameters) {
        if (parameters.isEmpty()) {
            return "";
        }
        final Element payload = parameters.get(0);
        final String value = this.expressionSupport
                .expressionValueText(payload);
        String declaration = "";
        if (!value.startsWith(SysMLTokens.NEW_PREFIX)) {
            declaration = this.payloadDeclarationText(payload, !value.isBlank());
        }
        final String payloadText;
        if (declaration.isBlank() && value.isBlank()) {
            payloadText = this.referenceResolver.endpointPath(payload);
        } else if (value.isBlank()) {
            payloadText = declaration;
        } else if (value.startsWith(SysMLTokens.NEW_PREFIX)) {
            payloadText = value;
        } else if (declaration.isBlank()) {
            payloadText = this.payloadValueText(payload, value);
        } else {
            payloadText = declaration + SysMLTokens.SPACE + this.payloadValueText(payload, value);
        }
        return payloadText;
    }

    /**
     * Returns the payload name and its {@code PayloadFeatureSpecializationPart}: typing, subsetting, redefinition,
     * multiplicity and ordering modifiers, so that none of them is lost. The multiplicity is written last, or right
     * after the name when the source declared it first ({@code accept [1] Signal}).
     */
    private String payloadDeclarationText(final Element payload, final boolean hasValue) {
        final List<String> clauses = new ArrayList<>();
        final String name = this.referenceResolver.declaredName(payload);
        if (!name.isBlank()) {
            clauses.add(name);
        }
        final int positionAfterName = clauses.size();
        clauses.addAll(this.payloadSpecializationClauses(payload, name.isBlank() && !hasValue));
        final String multiplicity = this.multiplicities.multiplicityText(payload, this.referenceWriter::expressionText);
        if (!multiplicity.isBlank() && !clauses.isEmpty()) {
            int position = clauses.size();
            if (this.multiplicities.declaresMultiplicityFirst(payload)) {
                position = positionAfterName;
            }
            clauses.add(position, multiplicity);
        }
        this.addOrderingModifiers(payload, clauses, positionAfterName, !multiplicity.isBlank());
        return String.join(SysMLTokens.SPACE, clauses);
    }

    /**
     * Returns the typing, subsetting and redefinition clauses of a payload. A payload that only has a typing keeps the
     * bare type form ({@code accept Signal}), which the grammar reads as the typing, when {@code mayBeBareType} is
     * set: before a value or after a name a bare name would be read as the payload name. The typing also keeps its
     * {@code :} before an ordering modifier and when the source wrote it ({@code accept : Signal}).
     */
    private List<String> payloadSpecializationClauses(final Element payload, final boolean mayBeBareType) {
        final List<String> types = this.specializationTargets.featureTypeNames(payload,
                this.context.danglingReferenceIndex(), this.context.nameRenderer()::referenceName);
        final List<String> subsettedFeatures = this.specializationTargets.subsettedFeatureNames(payload,
                this.context.danglingReferenceIndex(), this.context.nameRenderer()::referenceName);
        final List<String> redefinedFeatures = this.specializationTargets.redefinedFeatureNames(payload,
                this.context.danglingReferenceIndex(), this.context.nameRenderer()::referenceName);
        final List<String> clauses = new ArrayList<>();
        if (!types.isEmpty()) {
            final boolean typingClause = this.flags.isOrdered(payload) || this.flags.isNonUnique(payload)
                    || this.adapters.hasTextAdapter(payload, ITextAdapter.PAYLOAD_TYPING_CLAUSE);
            final boolean bareType = mayBeBareType && subsettedFeatures.isEmpty() && redefinedFeatures.isEmpty()
                    && !typingClause;
            clauses.add(this.payloadTypingText(types, bareType));
        }
        if (!subsettedFeatures.isEmpty()) {
            clauses.add(SysMLTokens.SUBSETTING_OPERATOR + SysMLTokens.SPACE
                    + this.referenceResolver.referenceNames(subsettedFeatures));
        }
        if (!redefinedFeatures.isEmpty()) {
            clauses.add(this.declarations.redefinitionText(redefinedFeatures));
        }
        return clauses;
    }

    /**
     * Adds the {@code ordered} and {@code nonunique} modifiers of the payload where the source wrote them: an early
     * {@code nonunique} right after the name, otherwise both after the other clauses, as {@code ordered nonunique}
     * when the parser marked that order. As for any feature, {@code nonunique} is late when the payload has a
     * multiplicity or the parser marked it.
     */
    private void addOrderingModifiers(final Element payload, final List<String> clauses, final int earlyPosition,
            final boolean hasMultiplicity) {
        final boolean nonUnique = this.flags.isNonUnique(payload);
        final boolean late = hasMultiplicity || this.adapters.hasTextAdapter(payload, ITextAdapter.LATE_NONUNIQUE);
        final boolean afterOrdered = this.adapters.hasTextAdapter(payload, ITextAdapter.NONUNIQUE_AFTER_ORDERED);
        if (nonUnique && !late) {
            clauses.add(earlyPosition, SysMLTokens.NONUNIQUE_KEYWORD);
        } else if (nonUnique && !afterOrdered) {
            clauses.add(SysMLTokens.NONUNIQUE_KEYWORD);
        }
        if (this.flags.isOrdered(payload)) {
            clauses.add(SysMLTokens.ORDERED_KEYWORD);
        }
        if (nonUnique && late && afterOrdered) {
            clauses.add(SysMLTokens.NONUNIQUE_KEYWORD);
        }
    }

    /** Returns the payload typing as {@code : T}, or as the bare type {@code T} when {@code bareType} is set. */
    private String payloadTypingText(final List<String> types, final boolean bareType) {
        final String typeNames = this.referenceResolver.referenceNames(types);
        if (bareType) {
            return typeNames;
        }
        return SysMLTokens.TYPE_SEPARATOR_PREFIX + typeNames;
    }

    /**
     * Returns the payload value as the grammar spells it: a {@code TriggerValuePart} ({@code at t}, {@code after d},
     * {@code when c}) carries its own keyword, whereas a {@code ValuePart} needs its feature value operator
     * ({@code = v}, {@code := v}, {@code default = v}); without it the value would not parse after a name, or would be
     * read as the payload typing when the payload is unnamed ({@code accept = v} is not {@code accept v}).
     */
    private String payloadValueText(final Element payload, final String value) {
        final FeatureValue featureValue = this.navigation.featureValues(payload).stream().findFirst().orElse(null);
        if (featureValue == null || this.navigation.featureValueExpression(featureValue) instanceof TriggerInvocationExpression) {
            return value;
        }
        return this.declarations.featureValueOperator(featureValue) + SysMLTokens.SPACE + value;
    }

    /**
     * Returns the receiver value text, carried by the last parameter; empty when there are fewer than two parameters.
     */
    private String receiverText(final List<Element> parameters) {
        if (parameters.size() < 2) {
            return "";
        }
        return this.expressionSupport.expressionValueText(
                parameters.get(parameters.size() - 1));
    }

    /**
     * Collects the payload, sender and receiver texts of a send action with their source expressions, and the keyword
     * introducing the receiver ({@code via} when only two parameters exist, {@code to} otherwise).
     */
    private SendActionFacts sendActionFacts(final Element sendActionUsage) {
        final List<Element> parameters = this.navigation
                .parameterElements(sendActionUsage);
        final String sender = this.sendSenderText(parameters);
        final String receiver = this.receiverText(parameters);
        // Two parameters encode a via endpoint; a separate sender requires a third parameter.
        String receiverKeyword = SysMLTokens.TO_KEYWORD;
        if (parameters.size() == 2) {
            receiverKeyword = SysMLTokens.VIA_KEYWORD;
        }
        return new SendActionFacts(this.sendPayloadText(parameters),
                this.expressionSupport.parameterExpression(parameters, 0),
                sender, this.expressionSupport.parameterExpression(parameters,
                        parameters.size() - 2),
                receiver, this.expressionSupport.parameterExpression(parameters,
                        parameters.size() - 1),
                receiverKeyword);
    }

    /**
     * Returns the payload value text, or the path of the payload feature when it has no value; empty without
     * parameters.
     */
    private String sendPayloadText(final List<Element> parameters) {
        if (parameters.isEmpty()) {
            return "";
        }
        final String value = this.expressionSupport
                .expressionValueText(parameters.get(0));
        String payload = value;
        if (payload.isBlank()) {
            payload = this.referenceResolver.endpointPath(parameters.get(0));
        }
        return payload;
    }

    /** Returns the sender ({@code via}) value text, carried by the second-to-last of at least three parameters. */
    private String sendSenderText(final List<Element> parameters) {
        if (parameters.size() < 3) {
            return "";
        }
        return this.expressionSupport.expressionValueText(
                parameters.get(parameters.size() - 2));
    }

    /** Texts and source expressions of an accept action's payload and receiver. */
    private record AcceptActionFacts(String payload,
            Expression payloadExpression, String receiver,
            Expression receiverExpression) {
    }

    /** Texts and source expressions of a send action's payload, sender and receiver, with the receiver keyword. */
    private record SendActionFacts(String payload,
            Expression payloadExpression, String sender,
            Expression senderExpression, String receiver,
            Expression receiverExpression, String receiverKeyword) {
    }
}
