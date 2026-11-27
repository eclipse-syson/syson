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

import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.SerializationMode;
import org.eclipse.syson.sysml.serializer.model.ElementSyntaxQueries;
import org.eclipse.syson.sysml.serializer.model.ModelFlags;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.model.MultiplicityText;
import org.eclipse.syson.sysml.serializer.model.ParserAdapterQueries;
import org.eclipse.syson.sysml.serializer.model.SpecializationTargetNames;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.syntax.ParserFragments;
import org.eclipse.syson.sysml.serializer.syntax.SourceSyntax;
import org.eclipse.syson.sysml.serializer.syntax.SysMLTextSyntax;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.AssertConstraintUsage;
import org.omg.sysml.lang.sysml.Definition;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.EnumerationUsage;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Decides and emits declarations for SysML definitions and usages, including {@code PartDefinition} and
 * {@code PartUsage}, and for declaration-like nodes.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class DefinitionAndUsageRenderer {

    private static final String DEFINITION_METACLASS_SUFFIX = "Definition";

    private static final String USAGE_METACLASS_SUFFIX = "Usage";

    private static final Set<EClass> KEYWORD_USAGE_TYPES = Set.of(
            SysMLPackage.eINSTANCE.getActionUsage(),
            SysMLPackage.eINSTANCE.getItemUsage(),
            SysMLPackage.eINSTANCE.getOccurrenceUsage(),
            SysMLPackage.eINSTANCE.getPartUsage(),
            SysMLPackage.eINSTANCE.getPortUsage(),
            SysMLPackage.eINSTANCE.getRequirementUsage(),
            SysMLPackage.eINSTANCE.getAnalysisCaseUsage(),
            SysMLPackage.eINSTANCE.getCalculationUsage(),
            SysMLPackage.eINSTANCE.getCaseUsage(),
            SysMLPackage.eINSTANCE.getConcernUsage(),
            SysMLPackage.eINSTANCE.getConstraintUsage(),
            SysMLPackage.eINSTANCE.getRenderingUsage(),
            SysMLPackage.eINSTANCE.getStateUsage(),
            SysMLPackage.eINSTANCE.getUseCaseUsage(),
            SysMLPackage.eINSTANCE.getVerificationCaseUsage(),
            SysMLPackage.eINSTANCE.getViewUsage(),
            SysMLPackage.eINSTANCE.getViewpointUsage());

    /** Fragment rules whose own keyword ({@code actor}, {@code stakeholder}, {@code subject}, portion) precedes user keywords. */
    private static final Set<String> KEYWORD_BEFORE_USER_KEYWORDS_FRAGMENTS = Set.of(
            ParserFragments.ACTOR_USAGE, ParserFragments.STAKEHOLDER_USAGE,
            ParserFragments.SUBJECT_USAGE, ParserFragments.PORTION_USAGE);

    /** Fragment rules whose reference usage requires an explicit {@code ref}. */
    private static final Set<String> EXPLICIT_REF_ROOT_FRAGMENTS = Set.of(
            ParserFragments.REFERENCE_USAGE,
            ParserFragments.INTERFACE_NON_OCCURRENCE_USAGE_ELEMENT,
            ParserFragments.VARIANT_USAGE_ELEMENT);

    private final DeclarationRenderer declarations;

    private final ReferenceResolver referenceResolver;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final ElementSyntaxQueries syntax;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ModelFlags flags;

    private final ParserAdapterQueries adapters;

    private final MultiplicityText multiplicities;

    private final SpecializationTargetNames specializationTargets;

    /**
     * Creates rendering for definition and usage declarations using the current run's syntax and reference policies.
     *
     * @param declarations
     *            writes shared prefixes, feature clauses and bodies
     * @param referenceResolver
     *            supplies declared names and reference spellings
     * @param referenceWriter
     *            emits declaration references with their model-target metadata
     * @param syntax
     *            distinguishes explicit declarations and bodies from generated model elements
     * @param context
     *            supplies the run's output, options and model queries
     */
    public DefinitionAndUsageRenderer(final DeclarationRenderer declarations,
            final ReferenceResolver referenceResolver,
            final ExpressionAndReferenceWriter referenceWriter,
            final ElementSyntaxQueries syntax,
            final SerializationContext context) {
        this.declarations = declarations;
        this.referenceResolver = referenceResolver;
        this.referenceWriter = referenceWriter;
        this.syntax = syntax;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.flags = context.modelQueries().flags();
        this.adapters = context.modelQueries().adapters();
        this.multiplicities = context.modelQueries().multiplicities();
        this.specializationTargets = context.modelQueries().specializationTargets();
    }

    /**
     * Writes the generic declaration of a definition, usage or other declaration-like element: direction and modifiers,
     * {@code ref}/{@code individual}/user keywords, the metaclass keyword, names, specializations, values, then body or {@code ;}. This
     * is the fallback for every element without a dedicated renderer, and the forced form used by {@code
     * ElementDispatch.serializeDeclaration}. End features additionally get the cross-feature part (<code>end x [1] part p
     * ...</code>). A bare reference recorded by the parser (keyword omitted) is written as its target.
     */
    public void serializeDeclaration(final Element element) {
        final TextOutput output = this.context.output();
        final boolean isEnd = this.flags.isEnd(element);
        final DeclarationPrefix prefix = this.writeDeclarationPrefixes(element,
                output, this.writeModifiersAndResolveKeyword(element, output), isEnd);
        final String keyword = prefix.keyword();
        if (!keyword.isBlank()) {
            output.token(keyword);
        }
        final EndDeclarationPlan endPlan = this.writeEndDeclaration(element, output,
                isEnd, prefix.refPrefix());
        this.writeDeclarationNames(element, output, keyword);
        this.declarations.serializeSuffixModifiers(element);
        final boolean bareReference = this.hasOmittedDeclarationKeyword(element);
        if (bareReference) {
            this.referenceWriter.writeReferenceText(output.space(),
                    this.referenceResolver.directReferencedElementTarget(element),
                    element);
        }
        final boolean endMultiplicitySpecialization = endPlan
                .hasDirectMultiplicity()
                && (endPlan.ownMultiplicityAsTypeSuffix()
                        || !endPlan.nestedMultiplicity().isBlank());
        this.declarations.serializeSpecializations(element,
                !isEnd || endMultiplicitySpecialization, !bareReference);
        if (isEnd && endPlan.ownMultiplicityAsTypeSuffix()
                && endPlan.ownMultiplicity().isBlank()
                && !endPlan.nestedMultiplicity().isBlank()) {
            this.referenceWriter.writeReferenceText(output, endPlan.nestedMultiplicity(),
                    element);
        }
        this.declarations.serializeFeatureValues(element);
        this.declarations.renderBodyOrStatementEnd(element);
    }

    /**
     * Writes direction and prefix modifiers, except for an end feature, and returns the keyword the declaration should use: the metaclass keyword,
     * adjusted for {@code assert not}, portion kinds ({@code snapshot}, {@code timeslice}), {@code end}, and parser fragments that imply or
     * omit it.
     */
    private String writeModifiersAndResolveKeyword(final Element element,
            final TextOutput output) {
        String keyword = this.metaclassKeyword(element);
        if (element instanceof AssertConstraintUsage
                && this.flags.isNegated(element)) {
            keyword = SysMLTokens.ASSERT_NOT_CONSTRAINT;
        }
        if (!this.flags.isEnd(element)) {
            this.writeDirectionAndPrefixModifiers(element, output);
        }
        final String portionKind = this.flags.portionKind(element);
        if (!portionKind.isBlank()) {
            keyword = this.portionDeclarationKeyword(portionKind, keyword);
        }
        if (this.flags.isEnd(element)) {
            keyword = SysMLTokens.END_KEYWORD;
        }
        return this.parserDeclarationKeyword(element, keyword);
    }

    /**
     * Writes the direction and the {@code derived}/{@code abstract}/{@code variation}/{@code constant} modifiers of
     * {@code refPrefix}. An end feature writes them after {@code end}, the only place where {@code defaultReferenceUsage}
     * accepts them.
     */
    private void writeDirectionAndPrefixModifiers(final Element element,
            final TextOutput output) {
        final String direction = this.flags.direction(element);
        if (!direction.isBlank()) {
            output.token(direction).space();
        }
        this.declarations.serializePrefixModifiers(element);
    }

    /**
     * Combines the portion kind with the metaclass keyword; for plain occurrences the portion kind alone is the
     * keyword.
     */
    private String portionDeclarationKeyword(final String portionKind,
            final String keyword) {
        String declarationKeyword = portionKind;
        if (!keyword.isBlank()
                && !SysMLTokens.OCCURRENCE_KEYWORD.equals(keyword)) {
            declarationKeyword = portionKind + SysMLTokens.SPACE + keyword;
        }
        return declarationKeyword;
    }

    /**
     * Adjusts the keyword when the element comes from a grammar rule that supplies or omits it (objectives,
     * anonymous state actions, {@code actor}, {@code stakeholder}, {@code subject}). Assert constraints keep
     * {@code assert} even when the declaration keyword is omitted. Default interface ends never get here:
     * {@code StructuredParserFragmentRenderer} claims the port usages that rule builds.
     */
    private String parserDeclarationKeyword(final Element element,
            final String defaultKeyword) {
        String keyword = defaultKeyword;
        final boolean omitsKeyword = this.isAnonymousStateAction(element)
                || this.isObjectiveRequirementUsage(element)
                || this.hasOmittedDeclarationKeyword(element);
        if (omitsKeyword) {
            keyword = this.omittedDeclarationKeyword(element);
        } else if (this.isParserPartUsage(element,
                ParserFragments.ACTOR_USAGE)) {
            keyword = SysMLTokens.ACTOR_KEYWORD;
        } else if (this.isParserPartUsage(element,
                ParserFragments.STAKEHOLDER_USAGE)) {
            keyword = SysMLTokens.STAKEHOLDER_KEYWORD;
        } else if (this.isSubjectUsage(element)) {
            keyword = SysMLTokens.SUBJECT_KEYWORD;
        }
        return keyword;
    }

    /** An omitted keyword remains valid for a reference or an extension keyword that introduces the declaration. */
    private boolean hasOmittedDeclarationKeyword(final Element element) {
        return this.adapters.hasTextAdapter(element, ITextAdapter.DECLARATION_KEYWORD_OMITTED)
                && (!this.referenceResolver.hasIdentification(element) || !this.syntax.userKeywords(element).isEmpty());
    }

    /** Returns the keyword left when the declaration keyword is omitted: only assert constraints keep {@code assert [not]}. */
    private String omittedDeclarationKeyword(final Element element) {
        if (!(element instanceof AssertConstraintUsage)) {
            return "";
        }
        String keyword = SysMLTokens.ASSERT_KEYWORD;
        if (this.flags.isNegated(element)) {
            keyword = SysMLTokens.ASSERT_NOT;
        }
        return keyword;
    }

    /** Matches the requirement usage root of an {@code ObjectiveRequirementUsage} parse, whose rule supplies no keyword. */
    private boolean isObjectiveRequirementUsage(final Element element) {
        return SourceSyntax.isRootParserEntry(this.context.options(), element,
                ParserFragments.OBJECTIVE_REQUIREMENT_USAGE)
                && this.navigation.isMetaclass(element,
                        SysMLPackage.eINSTANCE.getRequirementUsage());
    }

    /** Matches a part usage root of the given fragment rule, which spells the part with its own keyword. */
    private boolean isParserPartUsage(final Element element,
            final String parserFragment) {
        return SourceSyntax.isRootParserEntry(this.context.options(), element, parserFragment)
                && this.navigation.isMetaclass(element,
                        SysMLPackage.eINSTANCE.getPartUsage());
    }

    /** Matches a part or reference usage root of a {@code SubjectUsage} parse, written with {@code subject}. */
    private boolean isSubjectUsage(final Element element) {
        return SourceSyntax.isRootParserEntry(this.context.options(), element,
                ParserFragments.SUBJECT_USAGE)
                && (this.navigation.isMetaclass(element,
                        SysMLPackage.eINSTANCE.getPartUsage())
                        || this.navigation.isMetaclass(element,
                                SysMLPackage.eINSTANCE.getReferenceUsage()));
    }

    /**
     * Matches an {@code entry}/{@code do}/{@code exit} action fragment that has no name or body, whose grammar writes no {@code action}
     * keyword.
     */
    private boolean isAnonymousStateAction(final Element element) {
        return SourceSyntax.isRootParserEntry(this.context.options(), element,
                ParserFragments.STATE_ACTION_USAGE)
                && this.navigation.isMetaclass(element,
                        SysMLPackage.eINSTANCE.getActionUsage())
                && this.referenceResolver.declaredName(element).isBlank()
                && !this.syntax.hasSerializableBody(element);
    }

    /**
     * Writes the prefix keywords in the order the grammar accepts: {@code individual} (when the parser rule supplies it),
     * {@code ref}, {@code individual}, the portion kind and usage user keywords ({@code occurrenceUsagePrefix}), then definition
     * user keywords. Fragment rules whose keyword precedes user keywords ({@code actor}, {@code stakeholder}, {@code subject},
     * portions) write it here. Returns the keyword still to write, which is empty when already consumed.
     */
    private DeclarationPrefix writeDeclarationPrefixes(final Element element,
            final TextOutput output, final String keyword, final boolean isEnd) {
        String declarationKeyword = keyword;
        final boolean parserKeywordBeforeUserKeywords = this
                .hasParserKeywordBeforeUserKeywords(element);
        final boolean parserIndividualUsage = this
                .isParserIndividualUsage(element);
        declarationKeyword = this.writeParserIndividualPrefix(element, output,
                declarationKeyword, parserIndividualUsage);
        final boolean refPrefix = this.shouldRenderRefPrefix(element,
                declarationKeyword);
        if (refPrefix && !isEnd) {
            output.token(SysMLTokens.REF_KEYWORD).space();
        }
        declarationKeyword = this.writeIndividualPrefix(element, output,
                declarationKeyword, parserIndividualUsage);
        if (this.hasRegularUsageUserKeywords(element, parserIndividualUsage, isEnd,
                parserKeywordBeforeUserKeywords)) {
            declarationKeyword = this.writePortionKind(element, output, declarationKeyword);
            this.declarations.serializeUserKeywords(element);
        }
        if (element instanceof Definition) {
            this.declarations.serializeUserKeywords(element);
        }
        if (this.shouldOmitEnumerationUsageKeyword(element)) {
            declarationKeyword = "";
        }
        declarationKeyword = this.writeParserKeywordBeforeUserKeywords(element,
                output, declarationKeyword, parserKeywordBeforeUserKeywords);
        return new DeclarationPrefix(declarationKeyword, refPrefix);
    }

    /**
     * For the {@code IndividualUsage} parser rule, writes {@code individual} followed by user keywords; that rule has no metaclass
     * keyword, so the returned keyword is empty.
     */
    private String writeParserIndividualPrefix(final Element element,
            final TextOutput output, final String keyword,
            final boolean parserIndividualUsage) {
        if (!parserIndividualUsage) {
            return keyword;
        }
        output.token(SysMLTokens.INDIVIDUAL_KEYWORD).space();
        this.declarations.serializeUserKeywords(element);
        return "";
    }

    /**
     * Tells whether the user keywords of a usage go at their regular place, after {@code ref}, {@code individual} and the
     * portion kind. Ends, definitions and fragment rules that place user keywords elsewhere write them at their own
     * position.
     */
    private boolean hasRegularUsageUserKeywords(final Element element,
            final boolean parserIndividualUsage, final boolean isEnd,
            final boolean parserKeywordBeforeUserKeywords) {
        return !(element instanceof Definition) && !parserIndividualUsage && !isEnd
                && !parserKeywordBeforeUserKeywords;
    }

    /**
     * Writes the portion kind ({@code snapshot}, {@code timeslice}) merged at the start of the keyword, so that user keywords can
     * follow it as {@code occurrenceUsagePrefix} requires, and returns the rest of the keyword. A keyword that does not start
     * with the portion kind, because a parser fragment replaced it, is returned unchanged.
     */
    private String writePortionKind(final Element element,
            final TextOutput output, final String keyword) {
        final String portionKind = this.flags.portionKind(element);
        if (portionKind.isBlank()
                || !keyword.equals(portionKind) && !keyword.startsWith(portionKind + SysMLTokens.SPACE)) {
            return keyword;
        }
        output.token(portionKind).space();
        return keyword.substring(portionKind.length()).strip();
    }

    /**
     * Writes {@code individual} for individual definitions and usages and returns the keyword that follows it, which may be
     * shortened (see {@link #individualDeclarationKeyword(Element, String)}).
     */
    private String writeIndividualPrefix(final Element element,
            final TextOutput output, final String keyword,
            final boolean parserIndividualUsage) {
        if (!this.flags.isIndividual(element) || parserIndividualUsage) {
            return keyword;
        }
        output.token(SysMLTokens.INDIVIDUAL_KEYWORD).space();
        return this.individualDeclarationKeyword(element, keyword);
    }

    /**
     * For parser rules whose keyword precedes user keywords ({@code actor #K a}), writes the keyword and then the user
     * keywords; returns an empty keyword since it is already written.
     */
    private String writeParserKeywordBeforeUserKeywords(final Element element,
            final TextOutput output, final String keyword,
            final boolean parserKeywordBeforeUserKeywords) {
        if (!parserKeywordBeforeUserKeywords || keyword.isBlank()) {
            return keyword;
        }
        output.token(keyword).space();
        this.declarations.serializeUserKeywords(element);
        return "";
    }

    /** Matches an individual usage root of an {@code IndividualUsage} parse, which writes {@code individual} without keyword. */
    private boolean isParserIndividualUsage(final Element element) {
        return SourceSyntax.isRootParserEntry(this.context.options(), element,
                ParserFragments.INDIVIDUAL_USAGE)
                && this.flags.isIndividual(element);
    }

    /** Matches an enumeration usage written without {@code enum}, as an enumerated value in an {@code enum def} body. */
    private boolean shouldOmitEnumerationUsageKeyword(final Element element) {
        if (!(element instanceof EnumerationUsage)) {
            return false;
        }
        final Relationship membership = element.getOwningRelationship();
        final boolean enumerationMember = membership != null
                && this.navigation.isMetaclass(membership.getOwningRelatedElement(), SysMLPackage.eINSTANCE.getEnumerationDefinition());
        final boolean enumerationContext = enumerationMember
                || SourceSyntax.isRootParserEntry(this.context.options(), element, ParserFragments.ENUMERATED_VALUE)
                || SourceSyntax.isParserEntryChild(this.context.options(), element, ParserFragments.ENUMERATION_USAGE_MEMBER);
        return enumerationContext && this.adapters.hasTextAdapter(element, ITextAdapter.ENUM_KEYWORD_OMITTED);
    }

    /** Matches the root of a fragment rule listed in {@link #KEYWORD_BEFORE_USER_KEYWORDS_FRAGMENTS}. */
    private boolean hasParserKeywordBeforeUserKeywords(final Element element) {
        return element.eContainer() == null && KEYWORD_BEFORE_USER_KEYWORDS_FRAGMENTS
                .contains(this.context.options().parserEntryPoint());
    }

    /**
     * Returns the keyword after {@code individual}: {@code individual def} instead of {@code individual occurrence def}, and <code>individual
     * a</code> instead of {@code individual occurrence a}, when the source used the compact form, the usage is anonymous, or legacy
     * normalized output is requested (normalized without implicit reference keyword elision).
     */
    private String individualDeclarationKeyword(final Element element,
            final String keyword) {
        String individualKeyword = keyword;
        final boolean historicalNormalized = this.context.options()
                .mode() == SerializationMode.NORMALIZED
                && !this.context.options().implicitReferenceKeywordElision();
        final boolean compactDefinition = historicalNormalized
                || this.adapters.hasTextAdapter(element,
                        ITextAdapter.COMPACT_INDIVIDUAL_DEFINITION);
        if (SysMLTokens.OCCURRENCE_DEFINITION.equals(keyword)
                && compactDefinition
                && !SourceSyntax.isRootParserEntry(this.context.options(), element,
                        ParserFragments.OCCURRENCE_DEFINITION)) {
            individualKeyword = SysMLTokens.DEF_KEYWORD;
        }
        final boolean compactUsage = historicalNormalized
                || this.adapters.hasTextAdapter(element,
                        ITextAdapter.COMPACT_INDIVIDUAL_USAGE)
                || this.referenceResolver.declaredName(element).isBlank();
        if (SysMLTokens.OCCURRENCE_KEYWORD.equals(keyword) && compactUsage) {
            individualKeyword = "";
        }
        return individualKeyword;
    }

    /**
     * Writes the part of an end feature between {@code end} and its name: the cross feature (nested reference name and
     * multiplicity), {@code ordered}/{@code nonunique}, the metaclass keyword when required, and {@code ref}. Returns an empty plan for
     * non-end elements.
     */
    private EndDeclarationPlan writeEndDeclaration(final Element element,
            final TextOutput output, final boolean isEnd, final boolean refPrefix) {
        if (!isEnd) {
            return new EndDeclarationPlan("", "", "", false, false);
        }
        final EndDeclarationPlan plan = this.endDeclarationPlan(element);
        output.space();
        this.writeDirectionAndPrefixModifiers(element, output);
        this.writeEndReferenceAndMultiplicity(element, output, plan);
        this.writeEndModifiers(element, output);
        if (refPrefix) {
            output.space().token(SysMLTokens.REF_KEYWORD);
        }
        return plan;
    }

    /**
     * Collects where the multiplicity of an end belongs: on the nested cross feature, before the name, or as a type
     * suffix in the specialization part.
     */
    private EndDeclarationPlan endDeclarationPlan(final Element element) {
        final boolean hasNestedReference = this.syntax.hasNestedEndReference(element);
        String nestedName = "";
        String nestedMultiplicity = "";
        if (hasNestedReference) {
            nestedName = this.nestedEndReferenceName(element);
            nestedMultiplicity = this.multiplicities.crossFeatureMultiplicityText(element,
                    this.referenceWriter::expressionText);
        }
        final String ownMultiplicity = this.multiplicities.multiplicityText(element,
                this.referenceWriter::expressionText);
        final boolean hasDirectMultiplicity = this
                .hasDirectEndMultiplicity(element);
        final boolean ownMultiplicityAsTypeSuffix = hasDirectMultiplicity
                && !hasNestedReference
                && this.mustRenderEndMultiplicityAsTypeSuffix(element,
                        ownMultiplicity, nestedMultiplicity);
        return new EndDeclarationPlan(nestedName, nestedMultiplicity,
                ownMultiplicity, ownMultiplicityAsTypeSuffix,
                hasDirectMultiplicity);
    }

    /** Tells whether the end owns its multiplicity directly rather than through its cross feature. */
    private boolean hasDirectEndMultiplicity(final Element element) {
        return this.navigation.ownedRelationships(element).stream()
                .flatMap(relationship -> this.navigation
                        .ownedRelatedElements(relationship).stream())
                .anyMatch(related -> this.navigation.isMetaclass(related,
                        SysMLPackage.eINSTANCE.getMultiplicity())
                        || this.navigation.isMetaclass(related,
                                SysMLPackage.eINSTANCE.getMultiplicityRange()));
    }

    /** Writes the cross feature of an end: its nested reference name, then the multiplicity that precedes the end name. */
    private void writeEndReferenceAndMultiplicity(final Element element,
            final TextOutput output, final EndDeclarationPlan plan) {
        if (!plan.nestedName().isBlank()) {
            this.referenceWriter.writeReferenceText(output.space(), plan.nestedName(),
                    element);
        }
        final String multiplicity = this.endMultiplicityToWrite(plan);
        if (!multiplicity.isBlank()) {
            this.referenceWriter.writeReferenceText(output.space(), multiplicity,
                    element);
        }
    }

    /** Returns the multiplicity written in the end prefix: none when it is a type suffix, else nested before own. */
    private String endMultiplicityToWrite(final EndDeclarationPlan plan) {
        if (plan.ownMultiplicityAsTypeSuffix()) {
            return "";
        }
        String multiplicity = plan.nestedMultiplicity();
        if (multiplicity.isBlank()) {
            multiplicity = plan.ownMultiplicity();
        }
        return multiplicity;
    }

    /**
     * Writes end modifiers after the cross feature, then the user keywords and the end feature keyword, as in {@code usagePrefix
     * : endUsagePrefix usageExtensionKeyword*}. {@code ordered} is written here only for the interface occurrence usage
     * parser rule; {@code nonunique} comes from the cross feature.
     */
    private void writeEndModifiers(final Element element,
            final TextOutput output) {
        if (this.flags.isOrdered(element)
                && SourceSyntax.isRootParserEntry(this.context.options(), element,
                        ParserFragments.INTERFACE_OCCURRENCE_USAGE_ELEMENT)) {
            output.space().token(SysMLTokens.ORDERED_KEYWORD);
        }
        if (this.hasNonUniqueNestedEndReference(element)) {
            output.space().token(SysMLTokens.NONUNIQUE_KEYWORD);
        }
        output.space();
        this.declarations.serializeUserKeywords(element);
        final String endFeatureKeyword = this.metaclassKeyword(element);
        if (!endFeatureKeyword.isBlank()
                && this.syntax.shouldRenderEndFeatureKeyword(element)) {
            output.space().token(endFeatureKeyword);
        }
    }

    /** Writes {@code <short name>} and name. */
    private void writeDeclarationNames(final Element element,
            final TextOutput output, final String keyword) {
        final String shortName = this.context.nameRenderer()
                .declaredShortName(element);
        this.writeDeclarationShortName(output, keyword, shortName);
        this.writeDeclarationName(output, keyword, shortName,
                this.referenceResolver.declaredName(element));
    }

    private void writeDeclarationShortName(final TextOutput output,
            final String keyword, final String shortName) {
        if (!shortName.isBlank()) {
            if (!keyword.isBlank()) {
                output.space();
            }
            output.token(SysMLTokens.SHORT_NAME_START)
                    .token(this.referenceResolver.renderedShortName(shortName))
                    .token(SysMLTokens.SHORT_NAME_END);
        }
    }

    private void writeDeclarationName(final TextOutput output,
            final String keyword, final String shortName, final String name) {
        if (!name.isBlank()) {
            if (!keyword.isBlank() || !shortName.isBlank()) {
                output.space();
            }
            output.token(name);
        }
    }

    /**
     * Returns the declaration keyword of the element's metaclass from the keyword table, or derived from the metaclass
     * name ({@code FooDefinition} gives {@code foo def}, {@code FooUsage} gives {@code foo}). Returns an empty string for metaclasses without a
     * keyword, such as plain features.
     */
    private String metaclassKeyword(final Element element) {
        final String className = element.eClass().getName();
        String keyword = SysMLTextSyntax.DECLARATION_KEYWORDS.get(className);
        if (keyword == null
                && className.endsWith(DEFINITION_METACLASS_SUFFIX)) {
            keyword = decapitalize(className.substring(0,
                    className.length() - DEFINITION_METACLASS_SUFFIX.length()))
                    + SysMLTokens.DEF_SUFFIX;
        } else if (keyword == null
                && className.endsWith(USAGE_METACLASS_SUFFIX)) {
            keyword = decapitalize(className.substring(0,
                    className.length() - USAGE_METACLASS_SUFFIX.length()));
        }
        if (keyword == null) {
            keyword = "";
        }
        return keyword;
    }

    private static String decapitalize(final String text) {
        if (text.isEmpty()) {
            return "";
        }
        return Character.toLowerCase(text.charAt(0)) + text.substring(1);
    }

    /** The keyword still to write after the prefixes, and whether {@code ref} was decided for the declaration. */
    private record DeclarationPrefix(String keyword, boolean refPrefix) {
    }

    /** Values collected before rendering an end declaration. */
    private record EndDeclarationPlan(String nestedName,
            String nestedMultiplicity, String ownMultiplicity,
            boolean ownMultiplicityAsTypeSuffix,
            boolean hasDirectMultiplicity) {
    }

    /**
     * Decides whether to write {@code ref}. The source choice wins; otherwise {@code ref} is required for reference usages in
     * interface bodies and for non-composite ends with a nested multiplicity, and inferred for non-composite usages
     * whose keyword would otherwise mean composite. In normalized mode, a root reference usage only gets {@code ref} from
     * the source choice.
     */
    private boolean shouldRenderRefPrefix(final Element element, final String keyword) {
        final boolean explicitRefPrefix = this.flags.hasExplicitRefPrefix(element)
                || this.isRootParserEntryReferenceUsage(element);
        final boolean normalizedRootRule = this.context.options().mode() == SerializationMode.NORMALIZED
                && this.isRootParserRuleReferenceUsage(element);
        // Short-circuit order: the costly model rules are only evaluated when the source choice does not decide.
        return explicitRefPrefix || !normalizedRootRule && this.requiresRefPrefixFromModel(element, keyword);
    }

    /** Applies the model rules that require or infer {@code ref} when the source did not record the choice. */
    private boolean requiresRefPrefixFromModel(final Element element, final String keyword) {
        return this.mustRenderReferenceUsageKeywordInInterfaceBody(element)
                || this.mustRenderRefPrefixForEndReferenceUsageWithoutSyntaxAdapter(element)
                || this.mustRenderRefPrefixToDeclare(element)
                || this.shouldInferNonCompositeRefPrefix(element, keyword);
    }

    /** Requires {@code ref} on a reference usage, other than an end, whose declaration would otherwise be empty. */
    private boolean mustRenderRefPrefixToDeclare(final Element element) {
        return !this.flags.isEnd(element) && this.syntax.needsRefKeywordToDeclare(element);
    }

    /** Infers {@code ref} from the current composite flag, including edits to elements that retain parser markers. */
    private boolean shouldInferNonCompositeRefPrefix(final Element element, final String keyword) {
        return !this.flags.isEnd(element)
                && (this.isNonCompositeKeywordUsage(element, keyword)
                        || this.mustRenderRefPrefixForNonCompositeGenericUsage(element));
    }

    /** Matches a reference usage serialized as a parser fragment whose rule requires an explicit {@code ref}. */
    private boolean isRootParserEntryReferenceUsage(final Element element) {
        final boolean parserReference = element.eContainer() == null
                && EXPLICIT_REF_ROOT_FRAGMENTS.contains(this.context.options().parserEntryPoint())
                || SourceSyntax.isParserEntryChild(this.context.options(), element,
                        ParserFragments.INTERFACE_NON_OCCURRENCE_USAGE_MEMBER);
        return parserReference && this.navigation.isMetaclass(element,
                SysMLPackage.eINSTANCE.getReferenceUsage());
    }

    /** Matches a non-end reference usage that is the parse root or owned by the root membership of a parser rule. */
    private boolean isRootParserRuleReferenceUsage(final Element element) {
        boolean rootReference = this.navigation.isMetaclass(element,
                SysMLPackage.eINSTANCE.getReferenceUsage())
                && !this.flags.isEnd(element);
        if (rootReference && element.eContainer() != null) {
            rootReference = element
                    .eContainer() instanceof final Relationship relationship
                    && relationship.eContainer() == null;
        }
        return rootReference;
    }

    /**
     * Requires {@code ref} on a model-built non-composite reference end with a nested multiplicity, so that the
     * non-composite flag survives re-parsing.
     */
    private boolean mustRenderRefPrefixForEndReferenceUsageWithoutSyntaxAdapter(
            final Element element) {
        final boolean unadaptedReferenceEnd = !SourceSyntax.hasTextAdapter(element)
                && this.flags.isEnd(element)
                && this.navigation.isMetaclass(element,
                        SysMLPackage.eINSTANCE.getReferenceUsage())
                && !this.flags.isComposite(element);
        return unadaptedReferenceEnd && !this.multiplicities
                .crossFeatureMultiplicityText(element, this.referenceWriter::expressionText)
                .isBlank();
    }

    /**
     * Requires {@code ref} on a non-composite plain usage introduced by user keywords, since {@code #K u} alone declares a
     * composite.
     */
    private boolean mustRenderRefPrefixForNonCompositeGenericUsage(
            final Element element) {
        return this.navigation.isMetaclass(element,
                SysMLPackage.eINSTANCE.getUsage())
                && !this.flags.isComposite(element)
                && !this.syntax.userKeywords(element).isEmpty();
    }

    /**
     * Requires {@code ref} on reference usages owned by interfaces, whose body grammar has no keyword-less reference
     * usage.
     */
    private boolean mustRenderReferenceUsageKeywordInInterfaceBody(
            final Element element) {
        return this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getReferenceUsage())
                && this.navigation.isOwnedByMetaclass(element, SysMLPackage.eINSTANCE.getInterfaceDefinition(),
                        SysMLPackage.eINSTANCE.getInterfaceUsage());
    }

    /**
     * Matches a non-composite usage whose metaclass keyword ({@code part}, {@code action}, ...) declares a composite by default.
     */
    private boolean isNonCompositeKeywordUsage(final Element element,
            final String keyword) {
        return !keyword.isBlank() && KEYWORD_USAGE_TYPES.contains(element.eClass())
                && !this.flags.isComposite(element);
    }

    /**
     * Tells whether an end's own multiplicity must follow its type ({@code end p : P [1]}) rather than precede the name.
     * Applies when no nested multiplicity exists and the end is typed, has no body, or was written with {@code crosses}.
     */
    private boolean mustRenderEndMultiplicityAsTypeSuffix(final Element element,
            final String ownEndMultiplicity,
            final String nestedEndMultiplicity) {
        boolean suffix = !ownEndMultiplicity.isBlank()
                && nestedEndMultiplicity.isBlank();
        if (suffix) {
            suffix = this.adapters.hasTextAdapter(element, ITextAdapter.CROSSES)
                    || !this.syntax.hasSerializableBody(element);
            suffix = suffix || !this.specializationTargets
                    .featureTypeNames(element,
                            this.context.danglingReferenceIndex(),
                            this.context.nameRenderer()::referenceName)
                    .isEmpty();
        }
        return suffix;
    }

    /**
     * Returns the name of the first named reference usage nested in an end (its cross feature), or an empty string.
     */
    private String nestedEndReferenceName(final Element element) {
        String name = "";
        if (this.syntax.hasNestedEndReference(element)) {
            name = this.navigation.bodyRelationships(element).stream()
                    .flatMap(relationship -> this.navigation
                            .ownedRelatedElements(relationship).stream())
                    .filter(related -> this.syntax.isNestedEndReference(element, related))
                    .map(this.referenceResolver::declaredName)
                    .filter(candidate -> !candidate.isBlank()).findFirst()
                    .orElse("");
        }
        return name;
    }

    /**
     * Tells whether the end's cross feature is {@code nonunique}, which is written in the end prefix rather than on the
     * nested usage.
     */
    private boolean hasNonUniqueNestedEndReference(final Element element) {
        boolean nonUnique = false;
        if (this.syntax.hasNestedEndReference(element)) {
            nonUnique = this.navigation.bodyRelationships(element).stream()
                    .flatMap(relationship -> this.navigation
                            .ownedRelatedElements(relationship).stream())
                    .filter(related -> this.syntax.isNestedEndReference(element, related))
                    .anyMatch(this.flags::isNonUnique);
        }
        return nonUnique;
    }
}
