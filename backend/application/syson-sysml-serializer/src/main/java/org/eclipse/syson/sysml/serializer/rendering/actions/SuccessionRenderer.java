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

import java.util.List;

import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.ElementDispatch;
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
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.SuccessionAsUsage;

/**
 * Writes succession-as-usage declarations: the shorthand {@code first a then b;}, the declared form
 * {@code succession s first [1] a then b;}, and the target-only forms used inside action bodies ({@code then b;}) and
 * by standalone parser fragments.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class SuccessionRenderer {

    private final DeclarationRenderer declarations;

    private final ElementDispatch dispatch;

    private final ReferenceResolver referenceResolver;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final ElementSyntaxQueries syntax;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ParserAdapterQueries adapters;

    private final MultiplicityText multiplicities;

    private final ModelFlags flags;

    private final SpecializationTargetNames specializationTargets;

    private final SuccessionEndpointRenderer endpointRenderer;

    /**
     * Creates the renderer.
     *
     * @param declarations
     *            writes specializations and statement ends
     * @param dispatch
     *            writes an inline target declaration after the succession
     * @param referenceResolver
     *            resolves declared and referenced names
     * @param referenceWriter
     *            writes reference text with traceability metadata
     * @param syntax
     *            tells whether the succession body has members beyond comments, metadata and feature bindings
     * @param context
     *            the serialization context
     * @param endpointRenderer
     *            writes succession endpoints
     */
    public SuccessionRenderer(final DeclarationRenderer declarations,
            final ElementDispatch dispatch,
            final ReferenceResolver referenceResolver,
            final ExpressionAndReferenceWriter referenceWriter,
            final ElementSyntaxQueries syntax,
            final SerializationContext context,
            final SuccessionEndpointRenderer endpointRenderer) {
        this.declarations = declarations;
        this.dispatch = dispatch;
        this.referenceResolver = referenceResolver;
        this.referenceWriter = referenceWriter;
        this.syntax = syntax;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.adapters = context.modelQueries().adapters();
        this.multiplicities = context.modelQueries().multiplicities();
        this.flags = context.modelQueries().flags();
        this.specializationTargets = context.modelQueries().specializationTargets();
        this.endpointRenderer = endpointRenderer;
    }

    /**
     * Writes a succession, choosing the form of the grammar rule it was parsed from: the transition-succession and
     * target-succession fragments only spell the target, any other succession uses the standard
     * {@code [succession ...] [first source] then target} form.
     *
     * @param successionAsUsage
     *            the succession
     */
    public void serializeSuccessionAsUsage(final SuccessionAsUsage successionAsUsage) {
        final List<Element> ends = this.navigation.referenceEnds(successionAsUsage);
        final boolean transitionSuccessionFragment = SourceSyntax.isRootParserEntry(this.context.options(), successionAsUsage,
                        ParserFragments.TRANSITION_SUCCESSION)
                || SourceSyntax.isParserEntryChild(this.context.options(), successionAsUsage,
                        ParserFragments.TRANSITION_SUCCESSION_MEMBER);
        if (transitionSuccessionFragment && ends.size() >= 2) {
            this.serializeParserTransitionSuccession(successionAsUsage,
                    ends.get(1));
            return;
        }
        if (SourceSyntax.isRootParserEntry(this.context.options(), successionAsUsage,
                ParserFragments.TARGET_SUCCESSION)) {
            this.serializeParserTargetSuccession(successionAsUsage, ends);
            return;
        }
        this.serializeStandardSuccession(successionAsUsage, ends);
    }

    /** Writes the transition-succession fragment form, which is only the target endpoint with its multiplicity. */
    private void serializeParserTransitionSuccession(
            final SuccessionAsUsage successionAsUsage, final Element target) {
        this.endpointRenderer.writeMultiplicity(target);
        this.context.output().space();
        this.endpointRenderer.write(target);
        this.declarations.serializeStatementEnd(successionAsUsage);
    }

    /** Writes the target-succession fragment form {@code [mult] then target;}. */
    private void serializeParserTargetSuccession(
            final SuccessionAsUsage successionAsUsage,
            final List<Element> ends) {
        String multiplicity = "";
        if (!ends.isEmpty()) {
            multiplicity = this.multiplicities.multiplicityText(ends.getFirst(), this.referenceWriter::expressionText);
        }
        if (!multiplicity.isBlank()) {
            this.referenceWriter.writeReferenceText(this.context.output(),
                    multiplicity, ends.getFirst()).space();
        }
        this.context.output().token(SysMLTokens.THEN_KEYWORD).space();
        if (ends.size() >= 2) {
            this.endpointRenderer.writeMultiplicity(ends.get(1));
            this.endpointRenderer.write(ends.get(1));
        }
        this.declarations.serializeStatementEnd(successionAsUsage);
    }

    /**
     * Writes the standard {@code SuccessionAsUsage} form: optional {@code succession} declaration, {@code first source},
     * then {@code then target}.
     */
    private void serializeStandardSuccession(
            final SuccessionAsUsage successionAsUsage,
            final List<Element> ends) {
        this.writeUsagePrefix(successionAsUsage);
        if (this.mustRenderDeclaration(successionAsUsage)) {
            this.writeDeclaration(successionAsUsage);
        }
        this.writeSource(ends);
        this.writeTarget(successionAsUsage, ends);
    }

    /**
     * Writes the {@code usagePrefix} of grammar rule {@code successionAsUsage}: direction, {@code derived},
     * {@code abstract} or {@code variation}, {@code constant}, {@code ref} and the {@code #Metadata} user keywords.
     * Unlike other usages, {@code ref} is written only when the source spelled it: a succession is non-composite with
     * or without it, so the keyword adds nothing to the model and normalized output omits it rather than writing
     * {@code ref first a then b} for every succession.
     */
    private void writeUsagePrefix(final SuccessionAsUsage successionAsUsage) {
        final TextOutput output = this.context.output();
        final String direction = this.flags.direction(successionAsUsage);
        if (!direction.isBlank()) {
            output.token(direction).space();
        }
        this.declarations.serializePrefixModifiers(successionAsUsage);
        if (this.flags.hasExplicitRefPrefix(successionAsUsage)) {
            output.token(SysMLTokens.REF_KEYWORD).space();
        }
        this.declarations.serializeUserKeywords(successionAsUsage);
    }

    /** Writes {@code succession [name] ...specializations }. */
    private void writeDeclaration(final SuccessionAsUsage successionAsUsage) {
        final TextOutput output = this.context.output();
        output.token(SysMLTokens.SUCCESSION_KEYWORD);
        final String name = this.referenceResolver.declaredName(successionAsUsage);
        if (!name.isBlank()) {
            output.space().token(name);
        }
        this.declarations.serializeSpecializations(successionAsUsage, true);
        output.space();
    }

    /** Writes {@code first [mult] source }, or nothing when the source is implicit (a {@code then b;} in a body). */
    private void writeSource(final List<Element> ends) {
        if (ends.isEmpty() || this.endpointRenderer.path(ends.get(0)).isBlank()) {
            return;
        }
        final TextOutput output = this.context.output();
        output.token(SysMLTokens.FIRST_KEYWORD).space();
        this.endpointRenderer.writeMultiplicity(ends.get(0));
        this.endpointRenderer.write(ends.get(0));
        output.space();
    }

    /**
     * Writes {@code then target;}. With reference keyword elision, a named target end is written by name and declared
     * right after the statement, and a target equal to the next sibling's name is written as that name, after its end
     * multiplicity. Without target,
     * only {@code then } is written: the next sibling is the inline target declaration and ends the statement.
     */
    private void writeTarget(final SuccessionAsUsage successionAsUsage,
            final List<Element> ends) {
        final TextOutput output = this.context.output();
        output.token(SysMLTokens.THEN_KEYWORD).space();
        String target = "";
        if (ends.size() >= 2) {
            target = this.endpointRenderer.path(ends.get(1));
        }
        if (target.isBlank()) {
            return;
        }
        final boolean inlineTargetDeclaration = this.isInlineTargetDeclaration(ends);
        if (inlineTargetDeclaration) {
            output.token(this.referenceResolver.declaredName(ends.get(1)));
        } else if (target.equals(this.nextSiblingTargetName(successionAsUsage))) {
            this.endpointRenderer.writeMultiplicity(ends.get(1));
            this.referenceWriter.writeReferenceText(output, target, successionAsUsage);
        } else {
            this.endpointRenderer.writeMultiplicity(ends.get(1));
            this.endpointRenderer.write(ends.get(1));
        }
        this.writeBodyOrStatementEnd(successionAsUsage, ends);
        if (inlineTargetDeclaration) {
            this.dispatch.serializeElement(ends.get(1));
        }
    }

    /**
     * Ends the succession with its {@code definitionBody}: the full body without the ends when it has members such as
     * imports, aliases or definitions, otherwise the restricted statement body (comments, metadata, feature bindings)
     * or {@code ;}.
     */
    private void writeBodyOrStatementEnd(final SuccessionAsUsage successionAsUsage, final List<Element> ends) {
        if (this.syntax.hasExplicitActionBodyContent(successionAsUsage, ends)) {
            this.declarations.serializeBodyExcludingImplicitFeatures(successionAsUsage, ends);
            this.context.output().newline();
        } else {
            this.declarations.serializeStatementEnd(successionAsUsage,
                    this.endpointRenderer.hasNonEndpointFeatureBindings(successionAsUsage, ends));
        }
    }

    /**
     * Tells whether the target end is a named feature to be declared inline after {@code then}, which is only spelled
     * so when reference keyword elision is enabled.
     */
    private boolean isInlineTargetDeclaration(final List<Element> ends) {
        return this.context.options().implicitReferenceKeywordElision()
                && ends.size() >= 2
                && !this.referenceResolver.declaredName(ends.get(1)).isBlank();
    }

    /**
     * Returns the first declared name of the member that follows the succession in its owner, when reference keyword
     * elision is enabled; such a target is written as a plain name so that it reads as the following declaration.
     */
    private String nextSiblingTargetName(
            final SuccessionAsUsage successionAsUsage) {
        String target = "";
        if (this.context.options().implicitReferenceKeywordElision()) {
            final Relationship owningRelationship = successionAsUsage
                    .getOwningRelationship();
            if (owningRelationship != null
                    && owningRelationship.getOwningRelatedElement() != null) {
                final List<Relationship> siblings = this.navigation
                        .ownedRelationships(
                                owningRelationship.getOwningRelatedElement());
                final int index = siblings.indexOf(owningRelationship);
                target = this.firstDeclaredNameInNextSibling(siblings, index);
            }
        }
        return target;
    }

    /** Returns the first non-blank declared name among the elements of the sibling after {@code index}, or empty. */
    private String firstDeclaredNameInNextSibling(
            final List<Relationship> siblings,
            final int index) {
        String target = "";
        if (index >= 0 && index + 1 < siblings.size()) {
            target = this.navigation.ownedRelatedElements(siblings.get(index + 1))
                    .stream().map(this.referenceResolver::declaredName)
                    .filter(name -> !name.isBlank()).findFirst().orElse("");
        }
        return target;
    }

    /**
     * Tells whether the {@code succession} declaration part must be written: when the source used the keyword or the
     * succession carries information the shorthand cannot hold (name, typing, multiplicity, specialization).
     */
    private boolean mustRenderDeclaration(final SuccessionAsUsage successionAsUsage) {
        return this.adapters.hasTextAdapter(successionAsUsage, ITextAdapter.SUCCESSION_KEYWORD)
                || !this.referenceResolver.declaredName(successionAsUsage).isBlank()
                || this.hasTypingOrMultiplicity(successionAsUsage)
                || this.hasSpecialization(successionAsUsage);
    }

    /** Tells whether the succession declares a multiplicity or a type, which only the declared form can spell. */
    private boolean hasTypingOrMultiplicity(final SuccessionAsUsage successionAsUsage) {
        return !this.multiplicities.multiplicityText(successionAsUsage, this.referenceWriter::expressionText).isBlank()
                || !this.specializationTargets.featureTypeNames(successionAsUsage,
                        this.context.danglingReferenceIndex(), this.context.nameRenderer()::referenceName).isEmpty();
    }

    /** Tells whether the succession redefines, subsets, references or cross-subsets a feature. */
    private boolean hasSpecialization(
            final SuccessionAsUsage successionAsUsage) {
        final boolean redefined = !this.specializationTargets.redefinedFeatureNames(
                successionAsUsage,
                this.context.danglingReferenceIndex(),
                this.context.nameRenderer()::referenceName).isEmpty();
        final boolean subsetted = !this.specializationTargets.subsettedFeatureNames(
                successionAsUsage,
                this.context.danglingReferenceIndex(),
                this.context.nameRenderer()::referenceName).isEmpty();
        final boolean referenced = !this.specializationTargets.referencedFeatureNames(
                successionAsUsage, this.context.danglingReferenceIndex(),
                this.context.nameRenderer()::referenceName).isEmpty();
        final boolean crossed = !this.specializationTargets.crossedFeatureNames(
                successionAsUsage, this.context.danglingReferenceIndex(),
                this.context.nameRenderer()::referenceName).isEmpty();
        return redefined || subsetted || referenced || crossed;
    }
}
