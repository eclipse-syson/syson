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
package org.eclipse.syson.sysml.serializer.rendering.declarations;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.ElementDispatch;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.SerializationMode;
import org.eclipse.syson.sysml.serializer.expression.ExpressionRenderer;
import org.eclipse.syson.sysml.serializer.model.ElementSyntaxQueries;
import org.eclipse.syson.sysml.serializer.model.ModelFlags;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.specialization.SpecializationRenderer;
import org.eclipse.syson.sysml.serializer.syntax.CommentRenderer;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.EnumerationDefinition;
import org.omg.sysml.lang.sysml.FeatureValue;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.Specialization;

/**
 * Renders the grammar shared by declarations: names, modifiers, specialization clauses, values and bodies.
 * Domain renderers decide which parts to emit and in what order; nested elements re-enter dispatch.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class DeclarationRenderer {

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ModelFlags flags;

    private final ReferenceResolver referenceResolver;

    private final ElementSyntaxQueries syntax;

    private final ElementDispatch dispatch;

    private final CommentRenderer comments;

    private final ElementBodyRenderer bodies;

    private final FeatureValueRenderer values;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final SpecializationRenderer specializations;

    /**
     * Creates the shared declaration renderer and its body, value and specialization collaborators for one run.
     *
     * @param context
     *            supplies the run's output, options and model queries
     * @param referenceResolver
     *            supplies declaration names and reference spellings
     * @param expressions
     *            renders feature values and expressions used in declaration clauses
     * @param referenceWriter
     *            writes those expressions and references with their target metadata
     * @param syntax
     *            decides which optional clauses and body forms have printable syntax
     * @param dispatch
     *            recursively renders declaration members through the session
     * @param comments
     *            writes documentation and source notes in declaration bodies
     */
    public DeclarationRenderer(final SerializationContext context, final ReferenceResolver referenceResolver,
            final ExpressionRenderer expressions, final ExpressionAndReferenceWriter referenceWriter,
            final ElementSyntaxQueries syntax, final ElementDispatch dispatch, final CommentRenderer comments) {
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.flags = context.modelQueries().flags();
        this.referenceResolver = referenceResolver;
        this.syntax = syntax;
        this.dispatch = dispatch;
        this.comments = comments;
        this.bodies = new ElementBodyRenderer(syntax, this.comments, dispatch, context);
        this.values = new FeatureValueRenderer(expressions, referenceWriter, syntax, context);
        this.referenceWriter = referenceWriter;
        this.specializations = new SpecializationRenderer(referenceResolver, expressions, referenceWriter, syntax, context);
    }

    /** Writes non-default visibility, retaining an explicitly written public modifier. */
    public void serializeMembershipVisibility(final Relationship relationship) {
        if (relationship instanceof final Membership membership
                && (this.context.modelQueries().adapters().hasTextAdapter(membership,
                        ITextAdapter.EXPLICIT_PUBLIC_VISIBILITY)
                        || this.flags.isPrivate(membership)
                        || this.flags.isProtected(membership))) {
            this.context.output().token(membership.getVisibility().getName()).space();
        }
    }

    /**
     * Writes a space-separated name only when one is declared, so anonymous declarations keep no dangling separator.
     */
    public void renderOptionalName(final String name) {
        if (!name.isBlank()) {
            this.context.output().space().token(name);
        }
    }

    /**
     * Tells whether a usage has content that only the {@code usageDeclaration} of its grammar rule can carry: a name, a
     * short name, a multiplicity or an explicit specialization. Connector shorthands such as {@code connect a to b} have
     * no declaration, so such content requires the declaration form.
     */
    public boolean hasUsageDeclaration(final Element usage) {
        return !this.referenceResolver.declaredName(usage).isBlank()
                || !this.context.nameRenderer().declaredShortName(usage).isBlank()
                || !this.context.modelQueries().multiplicities()
                        .multiplicityText(usage, this.referenceWriter::expressionText).isBlank()
                || this.navigation.ownedRelationships(usage).stream()
                        .anyMatch(relationship -> relationship instanceof Specialization && !relationship.isImplied());
    }

    /**
     * Writes the grammar {@code identification} after a keyword already written: the optional {@code <shortName>} then
     * the optional name, each preceded by a space, so shorthand declarations such as {@code connection <s> c} keep their
     * short name.
     */
    public void renderOptionalIdentification(final Element element, final String name) {
        final String shortName = this.context.nameRenderer().declaredShortName(element);
        if (!shortName.isBlank()) {
            this.context.output().space().token(SysMLTokens.SHORT_NAME_START)
                    .token(this.referenceResolver.renderedShortName(shortName))
                    .token(SysMLTokens.SHORT_NAME_END);
        }
        this.renderOptionalName(name);
    }

    /**
     * Ends a declaration with either a <code>{ ... }</code> body or {@code ;}. A body is chosen when the element has serializable
     * members, a recorded source block, or {@code parallel}; parser fragments whose grammar rule has no terminator end with a
     * line break only. Always ends the output line.
     */
    public void renderBodyOrStatementEnd(final Element element) {
        this.bodies.renderBodyOrStatementEnd(element);
    }

    /**
     * Writes the braced body of an element (preceded by {@code parallel} when set) with all its body relationships. Does not
     * end the line, so callers can append text after the closing brace.
     */
    public void serializeBody(final Element element) {
        this.bodies.serializeBody(element);
    }

    /**
     * Ends a statement-like declaration (one whose grammar has no member body, only comments and metadata) with {@code ;} or
     * a comments-and-metadata body.
     */
    public void serializeStatementEnd(final Element element) {
        this.serializeStatementEnd(element, false);
    }

    /**
     * Ends a statement-like declaration; when {@code includeFeatureBindings} is set, explicitly declared nested
     * features (such as {@code in x = ...;} bindings of an exhibit or perform) are also kept in the body.
     */
    public void serializeStatementEnd(final Element element,
            final boolean includeFeatureBindings) {
        this.bodies.serializeStatementEnd(element,
                includeFeatureBindings);
    }

    /**
     * Writes a braced body that omits plain {@code Feature} members, which forms such as connectors and actions own as
     * generated parameters or ends rather than as user-written members.
     */
    public void serializeBodyExcludingImplicitFeatures(final Element element) {
        this.serializeBodyExcludingImplicitFeatures(element, List.of());
    }

    /**
     * Same as {@link #serializeBodyExcludingImplicitFeatures(Element)}, additionally omitting members the caller
     * already rendered in the declaration header.
     */
    public void serializeBodyExcludingImplicitFeatures(final Element element,
            final List<Element> additionalSkippedElements) {
        this.serializeBodyExcludingImplicitFeatures(element,
                additionalSkippedElements, List.of());
    }

    /**
     * Same as {@link #serializeBodyExcludingImplicitFeatures(Element, List)}, additionally omitting relationships of
     * the given metaclasses. Skipped relationships keep their index in the sibling list passed to dispatch.
     */
    public void serializeBodyExcludingImplicitFeatures(final Element element,
            final List<Element> additionalSkippedElements,
            final List<EClass> skippedRelationshipClasses) {
        this.bodies.serializeBodyExcludingImplicitFeatures(element,
                additionalSkippedElements, skippedRelationshipClasses);
    }

    /**
     * Ends a declaration with the body members chosen by the caller and the trailing comments, or with {@code ;} when
     * there is neither member, comment nor recorded body block.
     */
    public void serializeSelectedBodyOrStatementEnd(final Element element, final List<Relationship> relationships) {
        this.bodies.serializeSelectedBodyOrStatementEnd(element, relationships);
    }

    /** Writes the members of an element without braces, as for the parser's unnamed root namespace. */
    public void serializeNamespaceBody(final Element element) {
        this.bodies.serializeNamespaceBody(element);
    }

    /**
     * Writes the leading notes of a membership once, then each non-generated element it owns as
     * {@code [visibility] keyword declaration}, with a membership keyword such as {@code variant} or {@code verify}.
     * Implied memberships produce no text.
     */
    public void serializePrefixedMembership(final String prefix,
            final Relationship membership) {
        if (membership.isImplied()) {
            return;
        }
        this.comments.serializeLeadingComments(membership);
        for (final Element related : this.navigation
                .ownedRelatedElements(membership)) {
            if (!this.syntax.isAnonymousGeneratedElement(related)) {
                this.serializeMembershipVisibility(membership);
                this.context.output().token(prefix).space();
                this.dispatch.serializeElement(related);
            }
        }
    }

    /**
     * Writes the {@code #Metadata} user-defined keywords that precede a declaration's own keyword. Only metadata promoted
     * from the declaration prefix is written here; other metadata stays in the body.
     */
    public void serializeUserKeywords(final Element element) {
        for (final Relationship relationship : this.syntax
                .promotedUserKeywordRelationships(element)) {
            this.comments.serializeLeadingComments(relationship);
            for (final Element related : this.navigation
                    .ownedRelatedElements(relationship)) {
                if (this.syntax.isPromotedUserKeywordMetadata(related)) {
                    final String metadataType = this.syntax.metadataTypeName(related);
                    if (!metadataType.isBlank()) {
                        this.context.output()
                                .token(SysMLTokens.METADATA_KEYWORD_PREFIX);
                        this.referenceWriter.writeReferenceText(this.context.output(),
                                this.referenceResolver.referenceName(metadataType),
                                related).space();
                    }
                }
            }
        }
    }

    /**
     * Writes a feature introduced by a membership keyword (for example {@code subject s : T;} or <code>objective { ... }</code>): short
     * name, name, specializations, values, then body or terminator. An empty prefix writes the declaration alone.
     */
    public void serializeMembershipFeature(final String prefix,
            final Element element) {
        this.comments.serializeLeadingComments(element);
        final TextOutput output = this.context.output().token(prefix);
        final String shortName = this.context.nameRenderer()
                .declaredShortName(element);
        if (!shortName.isBlank()) {
            output.space().token(SysMLTokens.SHORT_NAME_START)
                    .token(this.referenceResolver.renderedShortName(shortName))
                    .token(SysMLTokens.SHORT_NAME_END);
        }
        final String name = this.referenceResolver.declaredName(element);
        if (!name.isBlank()) {
            output.space().token(name);
        }
        this.serializeSpecializations(element, true);
        this.serializeFeatureValues(element);
        this.renderBodyOrStatementEnd(element);
    }

    /**
     * Writes the typing, subsetting, redefinition and reference clauses of a declaration, in the order chosen by the
     * specialization planner, optionally with the multiplicity.
     */
    public void serializeSpecializations(final Element element,
            final boolean includeMultiplicity) {
        this.specializations
                .serializeSpecializations(element, includeMultiplicity);
    }

    /**
     * Variant of {@link #serializeSpecializations(Element, boolean)} that can omit {@code ::>} reference clauses, for
     * declarations whose syntax renders the referenced feature elsewhere.
     */
    public void serializeSpecializations(final Element element,
            final boolean includeMultiplicity,
            final boolean includeReferenceSpecializations) {
        this.specializations
                .serializeSpecializations(element, includeMultiplicity,
                        includeReferenceSpecializations);
    }

    /**
     * Writes the specializations of a declaration whose prefix already consumed part of them (for example metadata
     * bodies), emitting only the first non-empty clause family and then typing.
     */
    public void serializePrefixedSpecializations(final Element element,
            final List<String> redefinedFeatures, final List<String> referencedFeatures) {
        this.specializations
                .serializePrefixedSpecializations(element, redefinedFeatures,
                        referencedFeatures);
    }

    /**
     * Returns the {@code :>> a :>> b} text for already-resolved redefined names, for callers that assemble a declaration as a
     * string; returns an empty string for an empty list.
     */
    public String redefinitionText(final List<String> redefinedFeatures) {
        return this.specializations
                .redefinitionText(redefinedFeatures);
    }

    /**
     * Writes the redefinition clause of {@code element} with reference traces, keeping the source keyword and grouping
     * in round-trip mode.
     */
    public void writeRedefinition(final TextOutput output, final Element element,
            final List<String> redefinedFeatures) {
        this.specializations.writeRedefinition(
                output, element, redefinedFeatures);
    }

    /** Writes the {@code = value}, {@code := value} or {@code default} clauses of a declaration, in model order. */
    public void serializeFeatureValues(final Element element) {
        this.values.serializeFeatureValues(element);
    }

    /**
     * Returns the operator introducing a feature value, preferring the parser's recorded spelling in round-trip mode.
     */
    public String featureValueOperator(final FeatureValue value) {
        return this.values.featureValueOperator(value);
    }

    /**
     * Writes the {@code derived}, {@code abstract}, {@code variation} and {@code constant} prefix modifiers in the order of
     * the grammar rule {@code refPrefix}. Enumeration definitions are excluded from {@code abstract} and {@code variation}
     * because their {@code enum def} syntax has no slot for them. The grammar accepts {@code abstract} or
     * {@code variation}, never both: an edited element carrying both flags is written {@code variation} alone, since a
     * variation is abstract by definition.
     */
    public void serializePrefixModifiers(final Element element) {
        final TextOutput output = this.context.output();
        if (this.flags.isDerived(element)) {
            output.token(SysMLTokens.DERIVED_KEYWORD).space();
        }
        final boolean variation = this.flags.isVariation(element);
        if (this.flags.isAbstract(element) && !variation
                && !(element instanceof EnumerationDefinition)) {
            output.token(SysMLTokens.ABSTRACT_KEYWORD).space();
        }
        if (variation && !(element instanceof EnumerationDefinition)) {
            output.token(SysMLTokens.VARIATION_KEYWORD).space();
        }
        if (this.flags.isConstant(element)) {
            output.token(SysMLTokens.CONSTANT_KEYWORD).space();
        }
    }

    /**
     * Writes the {@code occurrenceUsagePrefix} keywords that precede the user keywords and the usage keyword of
     * connector-like and include declarations: direction, prefix modifiers, {@code ref}, {@code individual} and the
     * portion kind. {@code ref} is written when the source had it or, in normalized output, for a non-composite usage
     * that is neither {@code abstract} nor {@code variation}, so the non-composite flag stays explicit.
     */
    public void serializeOccurrenceUsagePrefix(final Element usage) {
        final TextOutput output = this.context.output();
        final String direction = this.flags.direction(usage);
        if (!direction.isBlank()) {
            output.token(direction).space();
        }
        this.serializePrefixModifiers(usage);
        if (this.shouldWriteRefPrefix(usage)) {
            output.token(SysMLTokens.REF_KEYWORD).space();
        }
        if (this.flags.isIndividual(usage)) {
            output.token(SysMLTokens.INDIVIDUAL_KEYWORD).space();
        }
        final String portionKind = this.flags.portionKind(usage);
        if (!portionKind.isBlank()) {
            output.token(portionKind).space();
        }
    }

    private boolean shouldWriteRefPrefix(final Element usage) {
        final boolean normalizedNonComposite = this.context.options().mode() == SerializationMode.NORMALIZED
                && !this.flags.isComposite(usage)
                && !this.flags.isAbstract(usage)
                && !this.flags.isVariation(usage);
        return normalizedNonComposite
                || this.flags.hasExplicitRefPrefix(usage);
    }

    /**
     * Writes {@code nonunique} in its early position, used only when the declaration has no multiplicity and the source did
     * not place the keyword late; see {@link #serializeLateNonUnique(Element)}.
     */
    public void serializeSuffixModifiers(final Element element) {
        this.specializations.serializeSuffixModifiers(element);
    }

    /**
     * Writes a {@code nonunique} that follows the multiplicity, either because the source put it there or because a
     * multiplicity is present (the grammar then requires that position).
     */
    public void serializeLateNonUnique(final Element element) {
        this.specializations.serializeLateNonUnique(element);
    }

    /** Writes the comments the parser attached before {@code object}. */
    public void serializeLeadingComments(final EObject object) {
        this.comments.serializeLeadingComments(object);
    }

    /** Writes the comments the parser recorded as trailing {@code object}, such as comments after its last member. */
    public void serializeTrailingComments(final EObject object) {
        this.comments.serializeTrailingComments(object);
    }
}
