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

import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.ElementDispatch;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.model.ElementSyntaxQueries;
import org.eclipse.syson.sysml.serializer.model.ModelFlags;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.model.ModelReference;
import org.eclipse.syson.sysml.serializer.model.MultiplicityText;
import org.eclipse.syson.sysml.serializer.model.NestedTargetSearch;
import org.eclipse.syson.sysml.serializer.model.ParserAdapterQueries;
import org.eclipse.syson.sysml.serializer.model.ReferenceTargets;
import org.eclipse.syson.sysml.serializer.model.SpecializationTargetNames;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.syntax.ParserFragments;
import org.eclipse.syson.sysml.serializer.syntax.SourceSyntax;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Comment;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Import;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.Redefinition;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Renders metadata usages and metadata field bodies.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class MetadataRenderer {

    private static final String TYPE_REFERENCE = "type";

    private final DeclarationRenderer declarations;

    private final ElementDispatch dispatch;

    private final ReferenceResolver referenceResolver;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final ElementSyntaxQueries syntax;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ModelFlags flags;

    private final ParserAdapterQueries adapters;

    private final MultiplicityText multiplicities;

    private final ReferenceTargets referenceTargets;

    private final SpecializationTargetNames specializationTargets;

    private final NestedTargetSearch nestedTargets;

    /**
     * Creates rendering for metadata usages and their fields, sharing declaration syntax and recursive dispatch.
     *
     * @param declarations
     *            writes shared prefixes, feature clauses and bodies
     * @param dispatch
     *            recursively renders metadata body elements through the session
     * @param referenceResolver
     *            supplies metadata names, types and annotated-element spellings
     * @param referenceWriter
     *            emits metadata references and field values with their target metadata
     * @param syntax
     *            identifies optional metadata bodies and printable field declarations
     * @param context
     *            supplies the run's output, options and parser-aware model queries
     */
    public MetadataRenderer(final DeclarationRenderer declarations,
            final ElementDispatch dispatch,
            final ReferenceResolver referenceResolver,
            final ExpressionAndReferenceWriter referenceWriter,
            final ElementSyntaxQueries syntax,
            final SerializationContext context) {
        this.declarations = declarations;
        this.dispatch = dispatch;
        this.referenceResolver = referenceResolver;
        this.referenceWriter = referenceWriter;
        this.syntax = syntax;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.flags = context.modelQueries().flags();
        this.adapters = context.modelQueries().adapters();
        this.multiplicities = context.modelQueries().multiplicities();
        this.referenceTargets = context.modelQueries().referenceTargets();
        this.specializationTargets = context.modelQueries().specializationTargets();
        this.nestedTargets = context.modelQueries().nestedTargets();
    }

    /**
     * Tells whether a reference usage is a metadata body field whose name is its redefined feature ({@code name = value;}):
     * an anonymous usage with such a redefinition, serialized from the metadata body parser rules or as a detached
     * root.
     */
    public boolean canSerializeMetadataBodyUsage(final Element element) {
        return this.navigation.isMetaclass(element,
                SysMLPackage.eINSTANCE.getReferenceUsage())
                && this.hasAnonymousMetadataBodyShape(element);
    }

    /**
     * Requires an unnamed usage with a redefined prefix name. Outside the metadata body parser rules, only a root usage
     * whose sole redefinition is that prefix qualifies.
     */
    private boolean hasAnonymousMetadataBodyShape(final Element element) {
        if (!this.referenceResolver.declaredName(element).isBlank()
                || this.metadataBodyUsagePrefix(element).isBlank()) {
            return false;
        }
        final boolean parserEntry = SourceSyntax.isRootParserEntry(this.context.options(), element,
                ParserFragments.METADATA_BODY_USAGE)
                || SourceSyntax.isParserEntryChild(this.context.options(), element,
                        ParserFragments.METADATA_BODY_USAGE_MEMBER);
        return parserEntry || element.eContainer() == null
                && this.metadataBodyUsageRedefinitionsAfterPrefix(element).isEmpty();
    }

    /**
     * Writes a metadata body field: the redefined feature as the name, remaining specializations, multiplicity and
     * modifiers, the value, then {@code ;}. A metadata body field has no body of its own.
     */
    public void serializeMetadataBodyUsage(final Element element) {
        final TextOutput output = this.context.output();
        if (this.flags.hasExplicitRefPrefix(element)) {
            output.token(SysMLTokens.REF_KEYWORD).space();
        }
        final String prefix = this.metadataBodyUsagePrefix(element);
        this.referenceWriter.writeChainedFeatureReferences(output, prefix, element);
        this.writeMetadataBodyUsageSpecializationsAndValue(element,
                this.metadataBodyUsageRedefinitionsAfterPrefix(element));
        output.token(SysMLTokens.STATEMENT_TERMINATOR).newline();
    }

    /**
     * Writes what follows the field name of a {@code metadataBodyUsage}: the {@code featureSpecializationPart} (the redefinitions
     * not already written as the name, then the other clauses, typing, multiplicity and modifiers) and the
     * {@code valuePart} with its source operator ({@code =}, {@code :=}, {@code default}).
     */
    private void writeMetadataBodyUsageSpecializationsAndValue(final Element element,
            final List<String> redefinedFeatures) {
        final TextOutput output = this.context.output();
        final List<String> referencedFeatures = this.specializationTargets
                .referencedFeatureNames(element,
                        this.context.danglingReferenceIndex(),
                        this.context.nameRenderer()::referenceName);
        this.declarations.serializePrefixedSpecializations(element,
                redefinedFeatures, referencedFeatures);
        final String multiplicity = this.multiplicities.multiplicityText(element,
                this.referenceWriter::expressionText);
        if (!multiplicity.isBlank()) {
            this.referenceWriter.writeReferenceText(output, multiplicity, element);
        }
        if (this.flags.isOrdered(element)) {
            output.space().token(SysMLTokens.ORDERED_KEYWORD);
        }
        this.declarations.serializeLateNonUnique(element);
        this.declarations.serializeFeatureValues(element);
    }

    /**
     * Writes a metadata usage in the spelling the source used, <code>@[identification :] Type</code> or
     * {@code metadata [identification :] Type}, then the {@code about} clause and a body of field values. The body is written only when it has content or was braced in the source;
     * otherwise the usage ends with {@code ;}.
     */
    public void serializeMetadataUsage(final Element metadataUsage) {
        final TextOutput output = this.context.output();
        final String metadataType = this.specializationTargets
                .featureTypeNames(metadataUsage,
                        this.context.danglingReferenceIndex(),
                        this.context.nameRenderer()::referenceName)
                .stream().findFirst().orElse("");
        this.declarations.serializeUserKeywords(metadataUsage);
        if (this.adapters.hasTextAdapter(metadataUsage,
                ITextAdapter.METADATA_AT_SIGN)) {
            output.token(SysMLTokens.METADATA_USAGE_PREFIX);
        } else {
            output.token(SysMLTokens.METADATA_KEYWORD).space();
        }
        this.writeMetadataUsageIdentification(metadataUsage);
        this.referenceWriter.writeRelationshipReferences(output,
                this.referenceResolver.referenceName(metadataType), metadataUsage,
                SysMLPackage.eINSTANCE.getFeatureTyping(), TYPE_REFERENCE);
        this.writeAboutClause(metadataUsage);
        final List<Relationship> bodyRelationships = this.navigation
                .bodyRelationships(metadataUsage);
        if (!this.adapters.hasTextAdapter(metadataUsage, ITextAdapter.BODY_BLOCK)
                && bodyRelationships.stream()
                .noneMatch(this::hasSerializableMetadataBodyContent)) {
            output.token(SysMLTokens.STATEMENT_TERMINATOR).newline();
            return;
        }
        output.space().beginBlock();
        this.serializeMetadataBodyContents(metadataUsage);
        output.endBlock().newline();
    }

    /**
     * Writes the optional {@code identification (':' | 'typed' 'by')} part of {@code metadataUsageDeclaration}: short name and
     * name followed by {@code :}, or a bare {@code :} when the source wrote one without identification.
     */
    private void writeMetadataUsageIdentification(final Element metadataUsage) {
        final TextOutput output = this.context.output();
        final String shortName = this.context.nameRenderer().declaredShortName(metadataUsage);
        if (!shortName.isBlank()) {
            output.token(SysMLTokens.SHORT_NAME_START)
                    .token(this.referenceResolver.renderedShortName(shortName))
                    .token(SysMLTokens.SHORT_NAME_END).space();
        }
        final String name = this.referenceResolver.declaredName(metadataUsage);
        if (!name.isBlank()) {
            output.token(name).space();
        }
        if (!shortName.isBlank() || !name.isBlank()
                || this.adapters.hasTextAdapter(metadataUsage, ITextAdapter.METADATA_COLON)) {
            output.token(SysMLTokens.TYPE_SEPARATOR).space();
        }
    }

    /**
     * Writes {@code about a, b} from the metadata's annotations, using the parser spellings so unresolved targets are kept.
     */
    private void writeAboutClause(final Element metadataUsage) {
        final TextOutput output = this.context.output();
        final List<ModelReference> annotatedElements = this
                .metadataAnnotatedElements(metadataUsage);
        if (!annotatedElements.isEmpty()) {
            output.space().token(SysMLTokens.ABOUT_KEYWORD).space();
            for (int index = 0; index < annotatedElements.size(); index++) {
                if (index > 0) {
                    output.token(SysMLTokens.COMMA).space();
                }
                final ModelReference target = annotatedElements
                        .get(index);
                this.referenceWriter.writeReferencePath(output,
                        this.referenceResolver.referenceName(target.name()),
                        target.element());
            }
        }
    }

    /**
     * Tells whether a body relationship produces text: feature memberships only when a field has a name, other
     * memberships, imports and comments always.
     */
    private boolean hasSerializableMetadataBodyContent(
            final Relationship relationship) {
        if (this.navigation.isMetaclass(relationship,
                SysMLPackage.eINSTANCE.getFeatureMembership())) {
            return this.navigation.ownedRelatedElements(relationship).stream()
                    .map(this::metadataFieldPlan)
                    .anyMatch(MetadataFieldRenderingPlan::hasText);
        }
        return relationship instanceof Import || relationship instanceof Membership
                || this.navigation.ownedRelatedElements(relationship).stream()
                .anyMatch(Comment.class::isInstance);
    }

    /** Writes metadata fields in their compact {@code name = value;} form and dispatches other members normally. */
    private void serializeMetadataBodyContents(final Element metadataUsage) {
        for (final Relationship relationship : this.navigation
                .bodyRelationships(metadataUsage)) {
            if (this.navigation.isMetaclass(relationship,
                    SysMLPackage.eINSTANCE.getFeatureMembership())) {
                this.navigation.ownedRelatedElements(relationship).stream()
                        .map(this::metadataFieldPlan)
                        .filter(MetadataFieldRenderingPlan::hasText)
                        .forEach(this::serializeMetadataField);
            } else if (this.hasSerializableMetadataBodyContent(relationship)) {
                this.dispatch.serializeRelationship(relationship);
            }
        }
    }

    /**
     * Writes one metadata field following {@code metadataBodyUsage}: the redefined name, the specialization part, the value
     * with its source operator, then <code>{ ... }</code> when it has nested fields or {@code ;}. Normalized output uses
     * the same form: a generic declaration would group the redefinitions as <code>:>> a, b</code>, which
     * {@code metadataBodyUsage} rejects.
     */
    private void serializeMetadataField(final MetadataFieldRenderingPlan plan) {
        final TextOutput output = this.context.output();
        final Element field = plan.field();
        this.declarations.serializeLeadingComments(field);
        if (this.flags.hasExplicitRefPrefix(field)) {
            output.token(SysMLTokens.REF_KEYWORD).space();
        }
        this.referenceWriter.writeReferenceText(output, plan.name(), field);
        this.writeMetadataBodyUsageSpecializationsAndValue(field, plan.redefinitionsAfterName());
        if (!plan.hasBody()) {
            output.token(SysMLTokens.STATEMENT_TERMINATOR).newline();
            return;
        }
        output.space().beginBlock();
        this.serializeMetadataBodyContents(field);
        output.endBlock().newline();
    }

    /**
     * Returns the {@code about} targets of the owned annotations, with the same target selection as comments: the
     * installed annotated elements, keeping a parser spelling while it still designates its target, and the parser
     * spellings of unresolved targets.
     */
    private List<ModelReference> metadataAnnotatedElements(
            final Element metadataUsage) {
        return this.navigation.ownedRelationships(metadataUsage).stream()
                .filter(relationship -> this.navigation.isMetaclass(relationship,
                        SysMLPackage.eINSTANCE.getAnnotation()))
                .flatMap(annotation -> this.referenceTargets.referenceTargets(annotation,
                        SysMLPackage.eINSTANCE.getAnnotation_AnnotatedElement().getName(),
                        this.context.danglingReferenceIndex(),
                        this.context.nameRenderer()::referenceName).stream())
                .toList();
    }

    /** Returns the redefined feature that serves as the field name of a metadata body usage, or an empty string. */
    private String metadataBodyUsagePrefix(final Element element) {
        return this.nestedTargets.firstChainedFeatureName(element, Redefinition.class,
                this.context.danglingReferenceIndex());
    }

    /** Returns the redefinitions other than the one written as the field name, which still need a clause. */
    private List<String> metadataBodyUsageRedefinitionsAfterPrefix(
            final Element element) {
        final String prefix = this.metadataBodyUsagePrefix(element);
        return this.specializationTargets
                .redefinedFeatureNames(element,
                        this.context.danglingReferenceIndex(),
                        this.context.nameRenderer()::referenceName)
                .stream().filter(feature -> !feature.equals(prefix)).toList();
    }

    /**
     * Collects a field's name, the redefinitions written after it and whether it has a body before choosing its {@code ;} or
     * block form. Only the first redefinition can be the name: {@code metadataBodyUsage} takes a single {@code ownedRedefinition}
     * there, the others belong to its {@code featureSpecializationPart} (<code>:>> a :>> b</code>, never <code>:>> a, b</code>).
     */
    private MetadataFieldRenderingPlan metadataFieldPlan(final Element field) {
        final List<String> redefinedFeatures = this.specializationTargets.redefinedFeatureNames(
                field, this.context.danglingReferenceIndex(),
                this.context.nameRenderer()::referenceName);
        List<String> redefinitionsAfterName = List.of();
        if (redefinedFeatures.size() > 1) {
            redefinitionsAfterName = redefinedFeatures.subList(1, redefinedFeatures.size());
        }
        return new MetadataFieldRenderingPlan(field, this.metadataFieldName(field, redefinedFeatures),
                redefinitionsAfterName, this.syntax.hasSerializableBody(field));
    }

    /**
     * Returns the field name: the first redefined feature with the source {@code :>>} or {@code redefines} spelling, or the
     * declared name when the field redefines nothing.
     */
    private String metadataFieldName(final Element field, final List<String> redefinedFeatures) {
        if (redefinedFeatures.isEmpty()) {
            return this.referenceResolver.declaredName(field);
        }
        final String name = this.referenceResolver.referenceName(redefinedFeatures.getFirst());
        String operator = "";
        if (this.adapters.hasTextAdapter(field, ITextAdapter.REDEFINES_SYMBOL)) {
            operator = SysMLTokens.REDEFINITION_OPERATOR + SysMLTokens.SPACE;
        } else if (this.adapters.hasTextAdapter(field, ITextAdapter.REDEFINES)) {
            operator = SysMLTokens.REDEFINES_PREFIX;
        }
        return operator + name;
    }

    /** Field name, redefinitions written after it and body presence collected before choosing {@code ;} or block syntax. */
    private record MetadataFieldRenderingPlan(Element field, String name, List<String> redefinitionsAfterName, boolean hasBody) {

        private boolean hasText() {
            return !this.name.isBlank();
        }
    }

}
