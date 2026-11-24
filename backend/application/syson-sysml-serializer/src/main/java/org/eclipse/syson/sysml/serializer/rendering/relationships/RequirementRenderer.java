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
package org.eclipse.syson.sysml.serializer.rendering.relationships;

import java.util.List;
import java.util.stream.Stream;

import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.ElementDispatch;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.SerializationMode;
import org.eclipse.syson.sysml.serializer.model.ElementSyntaxQueries;
import org.eclipse.syson.sysml.serializer.model.ModelFlags;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.model.NestedTargetSearch;
import org.eclipse.syson.sysml.serializer.model.ParserAdapterQueries;
import org.eclipse.syson.sysml.serializer.model.ReferenceTargets;
import org.eclipse.syson.sysml.serializer.model.SpecializationTargetNames;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.rendering.declarations.DeclarationRenderer;
import org.eclipse.syson.sysml.serializer.syntax.ParserFragments;
import org.eclipse.syson.sysml.serializer.syntax.SourceSyntax;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.ConcernUsage;
import org.omg.sysml.lang.sysml.ConstraintUsage;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.ReferenceSubsetting;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.RequirementConstraintKind;
import org.omg.sysml.lang.sysml.RequirementConstraintMembership;
import org.omg.sysml.lang.sysml.SatisfyRequirementUsage;
import org.omg.sysml.lang.sysml.SubjectMembership;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Writes the requirement constructs that have statement-level syntax: requirement and constraint references
 * ({@code requirement ::> r;}, {@code assert c;}), {@code verify}, {@code require}/{@code assume} members and
 * {@code satisfy ... by ...}.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class RequirementRenderer {

    private final DeclarationRenderer declarations;

    private final ElementDispatch dispatch;

    private final ReferenceResolver referenceResolver;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final ElementSyntaxQueries syntax;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ModelFlags flags;

    private final ParserAdapterQueries adapters;

    private final ReferenceTargets referenceTargets;

    private final SpecializationTargetNames specializationTargets;

    private final NestedTargetSearch nestedTargets;

    /**
     * Creates the renderer.
     *
     * @param declarations
     *            writes modifiers, specializations, values and bodies
     * @param dispatch
     *            writes the body members of a satisfy usage
     * @param referenceResolver
     *            resolves names and referenced targets
     * @param referenceWriter
     *            writes references with traceability metadata
     * @param syntax
     *            detects user keywords and explicit bodies
     * @param context
     *            the serialization context
     */
    public RequirementRenderer(final DeclarationRenderer declarations,
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
        this.referenceTargets = context.modelQueries().referenceTargets();
        this.specializationTargets = context.modelQueries().specializationTargets();
        this.nestedTargets = context.modelQueries().nestedTargets();
    }

    /**
     * Matches an unnamed requirement usage that only references another requirement ({@code requirement ::> r;}),
     * outside the requirement parser fragments, which have their own declaration form.
     *
     * @param element
     *            the candidate element
     * @return {@code true} when {@link #serializeRequirementReferenceUsage(Element)} applies
     */
    public boolean canSerializeRequirementReferenceUsage(final Element element) {
        return this.isOutsideRequirementParserEntries(element)
                && this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getRequirementUsage())
                && !this.referenceResolver.hasIdentification(element)
                && !this.referenceResolver.directReferencedElementTarget(element).isBlank();
    }

    /** Excludes requirement usages serialized as the root of a requirement, objective or verification fragment. */
    private boolean isOutsideRequirementParserEntries(final Element element) {
        return !SourceSyntax.isRootParserEntry(this.context.options(), element,
                ParserFragments.OBJECTIVE_REQUIREMENT_USAGE)
                && !SourceSyntax.isRootParserEntry(this.context.options(), element, ParserFragments.REQUIREMENT_USAGE)
                && !SourceSyntax.isRootParserEntry(this.context.options(), element,
                        ParserFragments.REQUIREMENT_VERIFICATION_USAGE);
    }

    /**
     * Writes {@code requirement ::> target} (or {@code references}, as spelled in the source) with a body or
     * {@code ;}.
     *
     * @param element
     *            a requirement usage accepted by {@link #canSerializeRequirementReferenceUsage(Element)}
     */
    public void serializeRequirementReferenceUsage(final Element element) {
        String referenceOperator = SysMLTokens.REFERENCE_SUBSETTING_OPERATOR;
        if (this.adapters.hasTextAdapter(element, ITextAdapter.REFERENCES)) {
            referenceOperator = SysMLTokens.REFERENCES_KEYWORD;
        }
        final TextOutput output = this.context.output().token(SysMLTokens.REQUIREMENT_KEYWORD)
                .space().token(referenceOperator).space();
        this.writeReference(output, this.referenceResolver.directReferencedElementTarget(element),
                this.referenceResolver.directReferencedElementTargetElement(element), element);
        if (this.syntax.hasSerializableBody(element)) {
            this.declarations.serializeBody(element);
            output.newline();
        } else {
            output.token(SysMLTokens.STATEMENT_TERMINATOR).newline();
        }
    }

    /**
     * Matches an unnamed constraint or concern usage whose first reference subsetting gives its written name, as in
     * requirement bodies where {@code c;} or {@code c.x;} refers to a feature of the context rather than declaring a
     * new one.
     *
     * @param element
     *            the candidate element
     * @return {@code true} when {@link #serializeContextualRequirementUsage(Element)} applies
     */
    public boolean canSerializeContextualRequirementUsage(final Element element) {
        return (element instanceof ConstraintUsage || element instanceof ConcernUsage)
                && this.referenceResolver.declaredName(element).isBlank()
                && !this.contextualReferenceChain(element).isBlank();
    }

    /**
     * Writes the contextual reference form: the referenced feature chain as the name, remaining specializations and
     * values, then a body or {@code ;}.
     *
     * @param element
     *            a usage accepted by {@link #canSerializeContextualRequirementUsage(Element)}
     */
    public void serializeContextualRequirementUsage(final Element element) {
        final String referenceChain = this.contextualReferenceChain(element);
        this.referenceWriter.writeChainedFeatureReferences(this.context.output(), referenceChain, element);
        final List<String> remainingReferencedFeatures = this.specializationTargets
                .referencedFeatureNames(element, this.context.danglingReferenceIndex(),
                        this.context.nameRenderer()::referenceName)
                .stream()
                .filter(feature -> !feature.equals(referenceChain))
                .toList();
        this.declarations.serializePrefixedSpecializations(element, this.redefinedFeatureNames(element),
                remainingReferencedFeatures);
        this.declarations.serializeFeatureValues(element);
        this.declarations.renderBodyOrStatementEnd(element);
    }

    /** Returns the referenced feature chain used as the written name, or an empty string. */
    private String contextualReferenceChain(final Element element) {
        return this.nestedTargets.firstChainedFeatureName(element, ReferenceSubsetting.class,
                this.context.danglingReferenceIndex());
    }

    /**
     * Matches an unnamed, untyped assert constraint usage with a reference subsetting target, a single feature or a
     * feature chain ({@code assert a.b;}). It must win over the contextual reference form, which has no {@code assert}.
     *
     * @param element
     *            the candidate element
     * @return {@code true} when {@link #serializeReferencedAssertConstraint(Element)} applies
     */
    public boolean canSerializeReferencedAssertConstraint(final Element element) {
        return this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getAssertConstraintUsage())
                && this.referenceResolver.declaredName(element).isBlank()
                && this.featureTypeNames(element).isEmpty()
                && !this.referenceResolver.directReferencedElementTarget(element).isBlank();
    }

    /**
     * Writes {@code assert [not] target} for an unnamed, untyped assert constraint that references an existing
     * constraint, with its remaining specializations and a body or {@code ;}.
     *
     * @param element
     *            an assert constraint accepted by {@link #canSerializeReferencedAssertConstraint(Element)}
     */
    public void serializeReferencedAssertConstraint(final Element element) {
        this.declarations.serializePrefixModifiers(element);
        this.declarations.serializeUserKeywords(element);
        final TextOutput output = this.context.output().token(SysMLTokens.ASSERT_KEYWORD).space();
        if (this.flags.isNegated(element)) {
            output.token(SysMLTokens.NOT_OPERATOR).space();
        }
        this.writeReference(output, this.referenceResolver.directReferencedElementTarget(element),
                this.referenceResolver.directReferencedElementTargetElement(element), element);
        this.declarations.serializeSpecializations(element, true, false);
        this.writeBlockBodyOrTerminator(element);
    }

    /**
     * Writes a {@code verify} member (grammar {@code requirementVerificationMember}): {@code verify target ...} when the
     * verified requirement references an existing one, otherwise {@code verify} followed by the requirement
     * declaration.
     *
     * @param membership
     *            the requirement verification membership
     */
    void serializeRequirementVerificationMembership(final Relationship membership) {
        final Element referencingMember = this.firstReferencingMember(this.navigation.ownedRelatedElements(membership));
        if (referencingMember == null) {
            this.declarations.serializePrefixedMembership(SysMLTokens.VERIFY_KEYWORD, membership);
        } else {
            this.serializeMembershipPrefix(membership);
            this.serializeReferencingMember(SysMLTokens.VERIFY_KEYWORD, referencingMember);
        }
    }

    /**
     * Writes a {@code require} or {@code assume} constraint member (grammar {@code requirementConstraintMember}), in
     * the first form that applies: an inline constraint declaration, a reference to an existing constraint, or the
     * keyword followed by the dispatched declaration.
     *
     * @param membership
     *            the requirement constraint membership
     */
    void serializeRequirementConstraintMembership(final RequirementConstraintMembership membership) {
        String keyword = SysMLTokens.REQUIRE_KEYWORD;
        if (membership.getKind() == RequirementConstraintKind.ASSUMPTION) {
            keyword = SysMLTokens.ASSUME_KEYWORD;
        }
        final List<Element> relatedElements = this.navigation.ownedRelatedElements(membership);
        final Element inlineConstraint = relatedElements.stream()
                .filter(this::canSerializeInlineRequirementConstraint)
                .findFirst().orElse(null);
        if (inlineConstraint != null) {
            this.serializeMembershipPrefix(membership);
            this.serializeInlineRequirementConstraint(keyword, inlineConstraint);
            return;
        }
        final Element referencingMember = this.firstReferencingMember(relatedElements);
        if (referencingMember == null) {
            this.declarations.serializePrefixedMembership(keyword, membership);
        } else {
            this.serializeMembershipPrefix(membership);
            this.serializeReferencingMember(keyword, referencingMember);
        }
    }

    /**
     * Writes the leading notes of a membership, then its visibility, so a note never separates the visibility from
     * the member keyword it qualifies.
     */
    private void serializeMembershipPrefix(final Relationship membership) {
        this.declarations.serializeLeadingComments(membership);
        this.declarations.serializeMembershipVisibility(membership);
    }

    /** Returns the first member that references an existing element ({@code keyword target}), or null. */
    private Element firstReferencingMember(final List<Element> members) {
        return members.stream()
                .filter(member -> !this.referenceResolver.directReferencedElementTarget(member).isBlank())
                .findFirst().orElse(null);
    }

    /**
     * Writes {@code keyword target ...} for a member that references an existing element, with its remaining
     * specializations and a body or {@code ;}.
     */
    private void serializeReferencingMember(final String keyword, final Element member) {
        final TextOutput output = this.context.output().token(keyword).space();
        this.writeReference(output, this.referenceResolver.directReferencedElementTarget(member),
                this.referenceResolver.directReferencedElementTargetElement(member), member);
        this.declarations.serializeSpecializations(member, true, false);
        this.declarations.renderBodyOrStatementEnd(member);
    }

    /**
     * Matches a constraint usage that declares something of its own (name, user keyword, typing or redefinition) and
     * does not merely reference another constraint.
     */
    private boolean canSerializeInlineRequirementConstraint(final Element element) {
        return this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getConstraintUsage())
                && (this.referenceResolver.directReferencedElementTarget(element).isBlank() || this.referenceResolver.hasIdentification(element))
                && this.hasExplicitInlineConstraintDeclaration(element);
    }

    private boolean hasExplicitInlineConstraintDeclaration(final Element element) {
        return this.referenceResolver.hasIdentification(element)
                || !this.syntax.userKeywords(element).isEmpty()
                || this.hasTypingOrRedefinition(element);
    }

    /**
     * Writes {@code require [constraint] name ...} with a body or {@code ;}, writing the {@code constraint} keyword only
     * when the source had it or the grammar needs it.
     */
    private void serializeInlineRequirementConstraint(final String keyword, final Element constraint) {
        this.context.output().token(keyword).space();
        this.declarations.serializeUserKeywords(constraint);
        final String name = this.referenceResolver.declaredName(constraint);
        if (this.mustRenderRequirementConstraintKeyword(constraint)) {
            this.context.output().token(SysMLTokens.CONSTRAINT_KEYWORD);
        }
        this.declarations.renderOptionalIdentification(constraint, name);
        this.declarations.serializeSpecializations(constraint, true);
        this.writeBlockBodyOrTerminator(constraint);
    }

    /**
     * Tells whether {@code constraint} is written after {@code require}/{@code assume}: when the source had it, when a
     * model-built named constraint has no user keyword, or when a typed or redefining constraint has no user keyword to
     * start the declaration.
     */
    private boolean mustRenderRequirementConstraintKeyword(final Element constraint) {
        final boolean explicitKeyword = this.adapters.hasTextAdapter(constraint,
                ITextAdapter.REQUIREMENT_CONSTRAINT_KEYWORD)
                || this.requiresKeywordForIdentification(constraint);
        return explicitKeyword
                || this.hasTypingOrRedefinition(constraint) && this.syntax.userKeywords(constraint).isEmpty();
    }

    private boolean requiresKeywordForIdentification(final Element constraint) {
        return this.referenceResolver.hasIdentification(constraint) && this.syntax.userKeywords(constraint).isEmpty();
    }

    private boolean hasTypingOrRedefinition(final Element element) {
        return !this.featureTypeNames(element).isEmpty() || !this.redefinedFeatureNames(element).isEmpty();
    }

    /**
     * Writes a satisfy requirement usage (grammar {@code satisfyRequirementUsage}) as
     * {@code [assert] [not] satisfy target [by subject]} with a body or {@code ;}. The satisfied requirement is written
     * as a bare reference, or as a {@code requirement name : Type} declaration when the usage is named or typed. The
     * subject used in the {@code by} clause is omitted from the body.
     *
     * @param satisfyRequirementUsage
     *            the satisfy requirement usage
     */
    public void serializeSatisfyRequirement(final SatisfyRequirementUsage satisfyRequirementUsage) {
        this.writeSatisfyRequirementPrefix(satisfyRequirementUsage);
        final SatisfyPlan plan = this.satisfyPlan(satisfyRequirementUsage);
        if (this.adapters.hasTextAdapter(satisfyRequirementUsage, ITextAdapter.DECLARATION_KEYWORD_OMITTED)
                && !this.referenceResolver.hasIdentification(satisfyRequirementUsage)) {
            this.writeBareSatisfiedRequirement(satisfyRequirementUsage, plan);
        } else {
            this.writeSatisfiedRequirement(satisfyRequirementUsage, plan);
        }
        this.declarations.serializeFeatureValues(satisfyRequirementUsage);
        if (!plan.subjectPath().isBlank()) {
            final TextOutput output = this.context.output().space().token(SysMLTokens.BY_KEYWORD).space();
            this.referenceWriter.writeReferenceText(output, plan.subjectPath(), plan.subjectElement());
        }
        if (plan.hasBody()) {
            this.serializeSatisfyBody(satisfyRequirementUsage, plan);
            this.context.output().newline();
        } else {
            this.context.output().token(SysMLTokens.STATEMENT_TERMINATOR).newline();
        }
    }

    /**
     * Writes the prefix modifiers, the {@code #M} user keywords and {@code satisfy}. Normalized output writes {@code ref}
     * for non-composite usages and {@code assert} for composite ones; round-trip output writes {@code assert} only when
     * the source had it.
     */
    private void writeSatisfyRequirementPrefix(final SatisfyRequirementUsage satisfyRequirementUsage) {
        final TextOutput output = this.context.output();
        this.declarations.serializePrefixModifiers(satisfyRequirementUsage);
        final boolean normalized = this.context.options().mode() == SerializationMode.NORMALIZED;
        final boolean composite = this.flags.isComposite(satisfyRequirementUsage);
        if (normalized && !composite) {
            output.token(SysMLTokens.REF_KEYWORD).space();
        }
        this.declarations.serializeUserKeywords(satisfyRequirementUsage);
        if (normalized && composite
                || this.adapters.hasTextAdapter(satisfyRequirementUsage, ITextAdapter.SATISFY_ASSERT)) {
            output.token(SysMLTokens.ASSERT_KEYWORD).space();
        }
        if (this.flags.isNegated(satisfyRequirementUsage)) {
            output.token(SysMLTokens.NOT_OPERATOR).space();
        }
        output.token(SysMLTokens.SATISFY_KEYWORD);
    }

    /**
     * Chooses the satisfied requirement (typing, or reference subsetting when untyped or written bare), the parser
     * specialization text of a satisfy fragment, the {@code by} subject, and whether a body is needed. Metadata written
     * as {@code #M} user keywords belongs to the prefix, not to the body.
     */
    private SatisfyPlan satisfyPlan(final SatisfyRequirementUsage satisfyRequirementUsage) {
        final String typedRequirement = this.featureTypeNames(satisfyRequirementUsage).stream()
                .findFirst().map(this.referenceResolver::referenceName).orElse("");
        final String referencedRequirement = this.specializationTargets
                .directReferenceSubsettingTargetNames(satisfyRequirementUsage, this.context.danglingReferenceIndex(),
                        this.context.nameRenderer()::referenceName)
                .stream().findFirst().orElse("");
        final boolean useReferencedRequirement = typedRequirement.isBlank()
                || !referencedRequirement.isBlank() && this.adapters.hasTextAdapter(satisfyRequirementUsage,
                        ITextAdapter.DECLARATION_KEYWORD_OMITTED);
        String target = typedRequirement;
        Element targetElement = this.referenceTargets.firstDirectRelationshipTargetElement(satisfyRequirementUsage,
                SysMLPackage.eINSTANCE.getFeatureTyping());
        if (useReferencedRequirement) {
            target = referencedRequirement;
            targetElement = this.referenceResolver.firstDirectRelationshipTargetElement(satisfyRequirementUsage,
                    SysMLPackage.eINSTANCE.getReferenceSubsetting());
        }
        String parserSpecialization = "";
        if (SourceSyntax.isRootParserEntry(this.context.options(), satisfyRequirementUsage,
                ParserFragments.SATISFY_REQUIREMENT_USAGE)) {
            parserSpecialization = this.parserSatisfySpecialization(satisfyRequirementUsage);
        }
        if (!parserSpecialization.isBlank()) {
            target = parserSpecialization.substring(parserSpecialization.indexOf(' ') + 1);
        }
        final List<Relationship> userKeywords = this.syntax.promotedUserKeywordRelationships(satisfyRequirementUsage);
        final List<Relationship> bodyRelationships = this.navigation.bodyRelationships(satisfyRequirementUsage)
                .stream().filter(relationship -> !userKeywords.contains(relationship)).toList();
        final Relationship subjectMembership = this.satisfactionSubjectMembership(bodyRelationships);
        final Element subjectElement = this.satisfactionSubjectElement(subjectMembership);
        String subjectPath = "";
        if (subjectElement != null) {
            subjectPath = this.referenceResolver.endpointPath(subjectElement);
        }
        final boolean hasBody = this.adapters.hasTextAdapter(satisfyRequirementUsage, ITextAdapter.BODY_BLOCK)
                || bodyRelationships.stream().anyMatch(relationship -> relationship != subjectMembership);
        return new SatisfyPlan(target, targetElement, this.referenceResolver.declaredName(satisfyRequirementUsage),
                typedRequirement, parserSpecialization, bodyRelationships, subjectMembership, subjectElement,
                subjectPath, hasBody);
    }

    /**
     * Returns the first specialization of a satisfy parser fragment, in crosses / redefines / references / subsets
     * priority, as {@code operator target}, or an empty string.
     */
    private String parserSatisfySpecialization(final SatisfyRequirementUsage usage) {
        return Stream.of(
                specializationText(SysMLTokens.CROSSING_OPERATOR, this.specializationTargets.crossedFeatureNames(
                        usage, this.context.danglingReferenceIndex(), this.context.nameRenderer()::referenceName)),
                specializationText(SysMLTokens.REDEFINITION_OPERATOR, this.redefinedFeatureNames(usage)),
                specializationText(SysMLTokens.REFERENCE_SUBSETTING_OPERATOR,
                        this.specializationTargets.referencedFeatureNames(usage,
                                this.context.danglingReferenceIndex(), this.context.nameRenderer()::referenceName)),
                specializationText(SysMLTokens.SUBSETTING_OPERATOR, this.specializationTargets.subsettedFeatureNames(
                        usage, this.context.danglingReferenceIndex(), this.context.nameRenderer()::referenceName)))
                .filter(text -> !text.isBlank())
                .findFirst().orElse("");
    }

    private static String specializationText(final String operator, final List<String> targets) {
        return targets.stream().findFirst().map(target -> operator + SysMLTokens.SPACE + target).orElse("");
    }

    /**
     * Writes the members of the satisfy body, leaving out the subject already written in the {@code by} clause, and
     * its trailing comments; the closing brace does not end the line.
     */
    private void serializeSatisfyBody(final SatisfyRequirementUsage satisfyRequirementUsage, final SatisfyPlan plan) {
        final TextOutput output = this.context.output();
        output.space().beginBlock();
        plan.bodyRelationships().stream()
                .filter(relationship -> relationship != plan.subjectMembership())
                .forEach(this.dispatch::serializeRelationship);
        this.declarations.serializeTrailingComments(satisfyRequirementUsage);
        output.endBlock();
    }

    /**
     * Returns the subject membership written as the {@code by} clause: one whose untyped subject has an endpoint path,
     * or null.
     */
    private Relationship satisfactionSubjectMembership(final List<Relationship> bodyRelationships) {
        return bodyRelationships.stream()
                .filter(SubjectMembership.class::isInstance)
                .filter(relationship -> this.navigation.ownedRelatedElements(relationship).stream()
                        .anyMatch(element -> !this.referenceResolver.endpointPath(element).isBlank()
                                && this.featureTypeNames(element).isEmpty()))
                .findFirst().orElse(null);
    }

    /** Returns the first member of the {@code by} subject membership that has an endpoint path, or null. */
    private Element satisfactionSubjectElement(final Relationship subjectMembership) {
        if (subjectMembership == null) {
            return null;
        }
        return this.navigation.ownedRelatedElements(subjectMembership).stream()
                .filter(element -> !this.referenceResolver.endpointPath(element).isBlank())
                .findFirst().orElse(null);
    }

    /**
     * Writes the satisfied requirement after {@code satisfy}: a bare reference, or a {@code requirement [name]}
     * declaration with its specializations. Specializations are skipped when the target is the {@code by} subject
     * itself, and a satisfy fragment reuses its parser specialization text.
     */
    private void writeSatisfiedRequirement(final SatisfyRequirementUsage satisfyRequirementUsage,
            final SatisfyPlan plan) {
        final TextOutput output = this.context.output().space();
        final boolean bareReference = !plan.target().isBlank() && !this.referenceResolver.hasIdentification(satisfyRequirementUsage)
                && plan.typedRequirement().isBlank();
        if (bareReference) {
            this.writeReference(output, this.referenceResolver.referenceName(plan.target()), plan.targetElement(),
                    satisfyRequirementUsage);
            return;
        }
        output.token(SysMLTokens.REQUIREMENT_KEYWORD);
        this.declarations.renderOptionalIdentification(satisfyRequirementUsage, plan.name());
        if (plan.parserSpecialization().isBlank()) {
            this.declarations.serializeSpecializations(satisfyRequirementUsage, true);
        } else {
            this.referenceWriter.writeReferenceText(output.space(), plan.parserSpecialization(),
                    satisfyRequirementUsage);
        }
    }

    /**
     * Writes the target of a satisfy whose {@code requirement} keyword the source omitted, followed by its
     * non-reference specializations.
     */
    private void writeBareSatisfiedRequirement(final SatisfyRequirementUsage usage, final SatisfyPlan plan) {
        final TextOutput output = this.context.output().space();
        this.writeReference(output, this.referenceResolver.referenceName(plan.target()), plan.targetElement(), usage);
        this.declarations.serializeSpecializations(usage, true, false);
    }

    /**
     * Writes a reference as a navigable path when the target is resolved, otherwise as text attached to the context
     * element so unresolved spellings are still traced.
     */
    private TextOutput writeReference(final TextOutput output, final String text, final Element target,
            final Element referenceContext) {
        if (target != null) {
            return this.referenceWriter.writeReferencePath(output, text, target);
        }
        return this.referenceWriter.writeReferenceText(output, text, referenceContext);
    }

    /** Ends a statement with the element's explicit block body, or with {@code ;}. */
    private void writeBlockBodyOrTerminator(final Element element) {
        final TextOutput output = this.context.output();
        if (this.syntax.shouldSerializeBlockBody(element)) {
            this.declarations.serializeBody(element);
            output.newline();
        } else {
            output.token(SysMLTokens.STATEMENT_TERMINATOR).newline();
        }
    }

    private List<String> featureTypeNames(final Element element) {
        return this.specializationTargets.featureTypeNames(element, this.context.danglingReferenceIndex(),
                this.context.nameRenderer()::referenceName);
    }

    private List<String> redefinedFeatureNames(final Element element) {
        return this.specializationTargets.redefinedFeatureNames(element, this.context.danglingReferenceIndex(),
                this.context.nameRenderer()::referenceName);
    }

    /**
     * The decisions taken once for a satisfy requirement usage.
     *
     * @param target
     *            the written satisfied requirement, or empty for a declaration without target
     * @param targetElement
     *            the model target of {@code target}, or {@code null} when unresolved
     * @param name
     *            the declared name of the satisfy usage
     * @param typedRequirement
     *            the first type name, or empty
     * @param parserSpecialization
     *            the {@code operator target} text of a satisfy parser fragment, or empty
     * @param bodyRelationships
     *            the body relationships
     * @param subjectMembership
     *            the subject membership written as the {@code by} clause, or {@code null}
     * @param subjectElement
     *            the subject written in the {@code by} clause, or {@code null}
     * @param subjectPath
     *            the written {@code by} subject, or empty
     * @param hasBody
     *            whether a braced body is written
     */
    private record SatisfyPlan(String target, Element targetElement, String name, String typedRequirement,
            String parserSpecialization, List<Relationship> bodyRelationships, Relationship subjectMembership,
            Element subjectElement, String subjectPath, boolean hasBody) {
    }
}
