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
package org.eclipse.syson.sysml.serializer.rendering.connections;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.SerializationContext;
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
import org.omg.sysml.lang.sysml.FeatureTyping;
import org.omg.sysml.lang.sysml.FlowUsage;
import org.omg.sysml.lang.sysml.Multiplicity;
import org.omg.sysml.lang.sysml.ParameterMembership;
import org.omg.sysml.lang.sysml.PayloadFeature;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.Specialization;
import org.omg.sysml.lang.sysml.SuccessionFlowUsage;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Writes flow and message usages: {@code flow f of p : P from a.x to b.y { ... }},
 * {@code succession flow of P from a to b;} and {@code message m of S from a to b;}. The payload is stored as a
 * {@code PayloadFeature} in the body; it is written inline after {@code of} and left out of the body.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class FlowRenderer {

    private static final String SOURCE_REFERENCE = "source";

    private static final String TARGET_REFERENCE = "target";

    private final DeclarationRenderer declarations;

    private final ReferenceResolver referenceResolver;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ModelFlags flags;

    private final ParserAdapterQueries adapters;

    private final MultiplicityText multiplicities;

    private final SpecializationTargetNames specializationTargets;

    /**
     * Creates the renderer.
     *
     * @param declarations
     *            writes keywords, specializations, values, bodies and statement ends
     * @param referenceResolver
     *            resolves declared names and endpoint paths
     * @param referenceWriter
     *            writes references with traceability metadata
     * @param context
     *            the serialization context
     */
    public FlowRenderer(final DeclarationRenderer declarations,
            final ReferenceResolver referenceResolver,
            final ExpressionAndReferenceWriter referenceWriter,
            final SerializationContext context) {
        this.declarations = declarations;
        this.referenceResolver = referenceResolver;
        this.referenceWriter = referenceWriter;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.flags = context.modelQueries().flags();
        this.adapters = context.modelQueries().adapters();
        this.multiplicities = context.modelQueries().multiplicities();
        this.specializationTargets = context.modelQueries().specializationTargets();
    }

    /**
     * Writes a flow or succession flow (grammar {@code flowUsage}/{@code successionFlowUsage}): declaration,
     * {@code of payload}, {@code [from] source to target}, then body or terminator. The payload and the ends are each
     * optional. Callers check {@link #canSerializeFlowUsage(FlowUsage)} first.
     *
     * @param flowUsage
     *            the flow usage
     */
    public void serializeFlowUsage(final FlowUsage flowUsage) {
        final TextOutput output = this.context.output();
        final String name = this.referenceResolver.declaredName(flowUsage);
        this.declarations.serializeOccurrenceUsagePrefix(flowUsage);
        this.declarations.serializeUserKeywords(flowUsage);
        output.token(this.flowKeyword(flowUsage));
        this.declarations.renderOptionalIdentification(flowUsage, name);
        this.declarations.serializeSpecializations(flowUsage, true);
        final Optional<Element> payload = this.declaredPayload(flowUsage);
        payload.ifPresent(this::writePayload);
        final List<Element> ends = this.navigation.flowEnds(flowUsage);
        if (ends.size() == 2) {
            output.space();
            if (payload.isPresent() || this.requiresExplicitFlowFrom(flowUsage, name)) {
                output.token(SysMLTokens.FROM_KEYWORD).space();
            }
            this.writeSourceToTarget(output, ends);
        }
        this.writeFlowBody(flowUsage, ends);
    }

    /** Returns {@code succession flow} for a succession flow usage, {@code flow} otherwise. */
    private String flowKeyword(final FlowUsage flowUsage) {
        if (flowUsage instanceof SuccessionFlowUsage) {
            return SysMLTokens.SUCCESSION_FLOW;
        }
        return SysMLTokens.FLOW_KEYWORD;
    }

    /**
     * Writes the body without the ends and payload (already written inline) but with the trailing comments, or
     * {@code ;} when nothing remains.
     */
    private void writeFlowBody(final FlowUsage flowUsage, final List<Element> ends) {
        final List<Relationship> body = this.navigation.bodyRelationships(flowUsage).stream()
                .filter(relationship -> this.navigation.ownedRelatedElements(relationship).stream()
                        .noneMatch(related -> ends.contains(related) || related instanceof PayloadFeature))
                .toList();
        this.declarations.serializeSelectedBodyOrStatementEnd(flowUsage, body);
    }

    /**
     * Tells whether {@code from} must precede the source: the first {@code flowDeclaration} alternative is used as soon
     * as any declaration token is written (identification, multiplicity, specialization or, as the caller checks,
     * payload), or the source wrote {@code from}.
     */
    private boolean requiresExplicitFlowFrom(final FlowUsage flowUsage, final String name) {
        final boolean declaresIdentification = !name.isBlank()
                || !this.context.nameRenderer().declaredShortName(flowUsage).isBlank();
        final boolean declaresSpecialization = this.navigation.ownedRelationships(flowUsage).stream()
                .anyMatch(relationship -> relationship instanceof Specialization && !relationship.isImplied())
                || !this.multiplicities.multiplicityText(flowUsage, this.referenceWriter::expressionText).isBlank();
        return declaresIdentification || declaresSpecialization
                || this.adapters.hasTextAdapter(flowUsage, ITextAdapter.FLOW_FROM);
    }

    /**
     * Tells whether the flow needs the flow declaration grammar, which the generic declaration cannot write: it has two
     * ends, or a payload and no end.
     *
     * @param flowUsage
     *            the flow usage
     * @return {@code true} when {@link #serializeFlowUsage(FlowUsage)} applies
     */
    public boolean canSerializeFlowUsage(final FlowUsage flowUsage) {
        final int endCount = this.navigation.flowEnds(flowUsage).size();
        return endCount == 2 || endCount == 0 && this.declaredPayload(flowUsage).isPresent();
    }

    /** Tells whether the flow is the root of a standalone flow or succession-flow fragment. */
    private boolean isFlowParserEntry(final FlowUsage flowUsage) {
        return SourceSyntax.isRootParserEntry(this.context.options(), flowUsage, ParserFragments.FLOW_USAGE)
                || SourceSyntax.isRootParserEntry(this.context.options(), flowUsage,
                        ParserFragments.SUCCESSION_FLOW_USAGE);
    }

    /**
     * Writes a message (grammar {@code message}) as {@code message [name] [of payload] [from] a to b;}. A message
     * without payload or ends keeps its multiplicity and its value. Message ends declared in the body are kept there.
     * Callers check {@link #canSerializeMessageUsage(FlowUsage)} first.
     *
     * @param flowUsage
     *            the message, represented as a flow usage
     */
    public void serializeMessageUsage(final FlowUsage flowUsage) {
        this.declarations.serializeOccurrenceUsagePrefix(flowUsage);
        if (SourceSyntax.isRootParserEntry(this.context.options(), flowUsage, ParserFragments.MESSAGE)) {
            this.declarations.serializeUserKeywords(flowUsage);
        }
        this.context.output().token(SysMLTokens.MESSAGE_KEYWORD);
        final String name = this.referenceResolver.declaredName(flowUsage);
        this.declarations.renderOptionalName(name);
        final Optional<Element> payload = this.declaredPayload(flowUsage);
        this.declarations.serializeSpecializations(flowUsage, payload.isEmpty());
        final List<Element> ends = this.navigation.messageEnds(flowUsage);
        payload.ifPresent(this::writePayload);
        if (payload.isEmpty() && ends.isEmpty()) {
            this.declarations.serializeFeatureValues(flowUsage);
        }
        this.writeMessageEnds(flowUsage, ends, name.isBlank() && payload.isEmpty());
        this.writeFlowBody(flowUsage, ends);
    }

    /**
     * Writes {@code from a to b} when the message has two ends; {@code from} is dropped for a bare message with only
     * parameter members, as {@code message a to b} is spelled.
     */
    private void writeMessageEnds(final FlowUsage flowUsage, final List<Element> ends, final boolean undeclared) {
        if (ends.size() != 2) {
            return;
        }
        final TextOutput output = this.context.output().space();
        final boolean bareMessage = undeclared
                && this.navigation.ownedRelationships(flowUsage).stream()
                        .allMatch(ParameterMembership.class::isInstance);
        if (!bareMessage) {
            output.token(SysMLTokens.FROM_KEYWORD).space();
        }
        this.writeSourceToTarget(output, ends);
    }

    /**
     * Tells whether a flow usage must be written as a message: the source wrote {@code message}, it is a standalone
     * message fragment, it has two message ends, or it has the shape of a message without flow ends (see
     * {@link #hasImplicitMessageShape(FlowUsage)}). Flow fragments are never messages.
     *
     * @param flowUsage
     *            the flow usage
     * @return {@code true} when {@link #serializeMessageUsage(FlowUsage)} applies
     */
    public boolean canSerializeMessageUsage(final FlowUsage flowUsage) {
        if (this.isFlowParserEntry(flowUsage) || !this.navigation.flowEnds(flowUsage).isEmpty()) {
            return false;
        }
        return this.adapters.hasTextAdapter(flowUsage, ITextAdapter.MESSAGE_USAGE)
                || SourceSyntax.isRootParserEntry(this.context.options(), flowUsage, ParserFragments.MESSAGE)
                || this.navigation.messageEnds(flowUsage).size() == 2
                || this.hasImplicitMessageShape(flowUsage);
    }

    /**
     * Tells whether a flow without flow ends looks like a message: two source/target ends declared in the body, or a
     * use case message redefining and referencing a feature. A payload is no hint: {@code flow of P;} declares one too.
     */
    private boolean hasImplicitMessageShape(final FlowUsage flowUsage) {
        return this.navigation.flowEnds(flowUsage).isEmpty()
                && (this.messageBodyEnds(flowUsage).size() == 2 || this.isReferencedUseCaseMessage(flowUsage));
    }

    /** Tells whether the flow is owned by a use case and both redefines and references a feature. */
    private boolean isReferencedUseCaseMessage(final FlowUsage flowUsage) {
        return this.navigation.isOwnedByMetaclass(flowUsage, SysMLPackage.eINSTANCE.getUseCaseUsage())
                && !this.redefinedFeatureNames(flowUsage).isEmpty()
                && !this.specializationTargets.referencedFeatureNames(flowUsage,
                        this.context.danglingReferenceIndex(), this.context.nameRenderer()::referenceName).isEmpty();
    }

    /** Returns the body features that bind the message {@code source} or {@code target} with a value. */
    private List<Element> messageBodyEnds(final FlowUsage flowUsage) {
        return this.bodyElements(flowUsage).filter(this::isMessageBodyEnd).toList();
    }

    /** Tells whether the element redefines {@code source} or {@code target} and has a value. */
    private boolean isMessageBodyEnd(final Element element) {
        return this.redefinedFeatureNames(element).stream()
                .anyMatch(name -> SOURCE_REFERENCE.equals(name) || TARGET_REFERENCE.equals(name))
                && !this.navigation.featureValues(element).isEmpty();
    }

    /**
     * Returns the payload the flow declares after {@code of}. A payload feature without an identification or an
     * explicit relationship has nothing to write.
     */
    private Optional<Element> declaredPayload(final FlowUsage flowUsage) {
        return this.bodyElements(flowUsage)
                .filter(PayloadFeature.class::isInstance)
                .filter(payload -> this.isIdentified(payload) || !this.explicitRelationships(payload).isEmpty())
                .findFirst();
    }

    /**
     * Writes {@code of payload} (grammar {@code payload}). A payload is a feature declaration, so its identification,
     * specializations, multiplicity and value are written as for any other feature and none of them is lost. Only an
     * unnamed payload that is merely typed has its own short form, see {@link #typeOnlyPayloadText(Element)}.
     */
    private void writePayload(final Element payload) {
        final TextOutput output = this.context.output().space().token(SysMLTokens.OF_KEYWORD);
        final String typeOnlyPayload = this.typeOnlyPayloadText(payload);
        if (typeOnlyPayload.isBlank()) {
            this.declarations.renderOptionalIdentification(payload, this.referenceResolver.declaredName(payload));
            this.declarations.serializeSuffixModifiers(payload);
            if (!this.isIdentified(payload) && this.multiplicities.declaresMultiplicityFirst(payload)) {
                // The multiplicity is otherwise attached to the token it follows, as in "p[1]".
                output.space();
            }
            this.declarations.serializeSpecializations(payload, true);
            this.declarations.serializeFeatureValues(payload);
        } else {
            output.space().referenceText(typeOnlyPayload,
                    () -> this.referenceWriter.referenceOccurrences(payload, typeOnlyPayload));
        }
    }

    /**
     * Returns the short form {@code T}, {@code T [m]} or {@code [m] T} of an unidentified payload that declares nothing
     * but one type and possibly a multiplicity, in their source order; empty for any other payload. The type has no
     * {@code :} there, which the grammar accepts for this form only; a source that wrote the typing clause
     * ({@code of : T}) keeps it.
     */
    private String typeOnlyPayloadText(final Element payload) {
        final List<Relationship> relationships = this.explicitRelationships(payload);
        final List<String> types = this.featureTypeNames(payload);
        final boolean modified = this.isIdentified(payload) || this.flags.isOrdered(payload)
                || this.flags.isNonUnique(payload)
                || this.adapters.hasTextAdapter(payload, ITextAdapter.PAYLOAD_TYPING_CLAUSE);
        final boolean typeOnly = !modified && types.size() == 1 && relationships.stream()
                .allMatch(relationship -> relationship instanceof FeatureTyping || this.ownsMultiplicity(relationship));
        String text = "";
        if (typeOnly) {
            final String type = this.referenceResolver.referenceName(types.get(0));
            final String multiplicity = this.multiplicities.multiplicityText(payload,
                    this.referenceWriter::expressionText);
            if (this.multiplicities.declaresMultiplicityFirst(payload)) {
                text = multiplicity + SysMLTokens.SPACE + type;
            } else {
                text = (type + SysMLTokens.SPACE + multiplicity).trim();
            }
        }
        return text;
    }

    private boolean isIdentified(final Element element) {
        return !this.referenceResolver.declaredName(element).isBlank()
                || !this.context.nameRenderer().declaredShortName(element).isBlank();
    }

    private List<Relationship> explicitRelationships(final Element element) {
        return this.navigation.ownedRelationships(element).stream()
                .filter(relationship -> !relationship.isImplied())
                .toList();
    }

    private boolean ownsMultiplicity(final Relationship relationship) {
        return this.navigation.ownedRelatedElements(relationship).stream().anyMatch(Multiplicity.class::isInstance);
    }

    /** Writes {@code source to target} from the two flow or message ends. */
    private void writeSourceToTarget(final TextOutput output, final List<Element> ends) {
        this.writeEndpointReference(output, this.referenceResolver.endpointPath(ends.get(0)), ends.get(0));
        output.space().token(SysMLTokens.TO_KEYWORD).space();
        this.writeEndpointReference(output, this.referenceResolver.endpointPath(ends.get(1)), ends.get(1));
    }

    /** Writes a reference traced to {@code contextElement}; writes nothing for a blank reference. */
    private void writeEndpointReference(final TextOutput output, final String reference,
            final Element contextElement) {
        if (!reference.isBlank()) {
            output.referenceText(reference, () -> this.referenceWriter.referenceOccurrences(contextElement, reference));
        }
    }

    private Stream<Element> bodyElements(final FlowUsage flowUsage) {
        return this.navigation.bodyRelationships(flowUsage).stream()
                .flatMap(relationship -> this.navigation.ownedRelatedElements(relationship).stream());
    }

    private List<String> featureTypeNames(final Element element) {
        return this.specializationTargets.featureTypeNames(element, this.context.danglingReferenceIndex(),
                this.context.nameRenderer()::referenceName);
    }

    private List<String> redefinedFeatureNames(final Element element) {
        return this.specializationTargets.redefinedFeatureNames(element, this.context.danglingReferenceIndex(),
                this.context.nameRenderer()::referenceName);
    }
}
