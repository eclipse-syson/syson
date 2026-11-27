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
package org.eclipse.syson.sysml.serializer.model;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.expression.ExpressionRenderer;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.syntax.ParserFragments;
import org.eclipse.syson.sysml.serializer.syntax.SourceSyntax;
import org.omg.sysml.lang.sysml.Annotation;
import org.omg.sysml.lang.sysml.AssignmentActionUsage;
import org.omg.sysml.lang.sysml.Comment;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureTyping;
import org.omg.sysml.lang.sysml.FlowEnd;
import org.omg.sysml.lang.sysml.Import;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.PortUsage;
import org.omg.sysml.lang.sysml.Redefinition;
import org.omg.sysml.lang.sysml.ReferenceUsage;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.Subclassification;
import org.omg.sysml.lang.sysml.SubjectMembership;
import org.omg.sysml.lang.sysml.Subsetting;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Decides which declarations, bodies and metadata have explicit SysML syntax.
 * <p>
 * The model contains elements the textual notation never writes (implicit conjugations, multiplicity helpers,
 * anonymous references created by shorthand syntax) and metadata that may be written either as a {@code #Keyword}
 * prefix or as a body member. Renderers ask these queries before choosing between {@code ;} and a {@code { ... }}
 * body, or before writing a member, so the same decision is applied everywhere. Mutually recursive decisions share the
 * per-serialization generated-element cache, which assumes the model is not modified during the run.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class ElementSyntaxQueries {

    private static final String CONJUGATED_METACLASS_PREFIX = "Conjugated";

    private static final String MULTIPLICITY_METACLASS_PREFIX = "Multiplicity";

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ModelFlags flags;

    private final ParserAdapterQueries adapters;

    private final MultiplicityText multiplicities;

    private final SpecializationTargetNames specializationTargets;

    private final ReferenceResolver referenceResolver;

    private final ExpressionRenderer expressions;

    // Equal names or structure do not make two model objects share a generated-element decision.
    private final Map<Element, Boolean> anonymousGeneratedElements = new IdentityHashMap<>();

    /**
     * Creates the syntax queries of one serialization run.
     *
     * @param context
     *            the run context, supplying the model queries, dangling references and name renderer
     * @param referenceResolver
     *            supplies declared names and reference targets as the renderers write them
     * @param expressions
     *            renders multiplicity bounds, whose text decides whether a feature declares a multiplicity
     */
    public ElementSyntaxQueries(final SerializationContext context, final ReferenceResolver referenceResolver,
            final ExpressionRenderer expressions) {
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.flags = context.modelQueries().flags();
        this.adapters = context.modelQueries().adapters();
        this.multiplicities = context.modelQueries().multiplicities();
        this.specializationTargets = context.modelQueries().specializationTargets();
        this.referenceResolver = referenceResolver;
        this.expressions = expressions;
    }

    /** Same as {@link #hasStatementBody(Element, boolean)} without feature bindings. */
    public boolean hasStatementBody(final Element element) {
        return this.hasStatementBody(element, false);
    }

    /**
     * Tells whether a declaration whose grammar admits only a restricted body (comments, body metadata and, when
     * requested, explicit feature bindings) needs a {@code { ... }} block instead of {@code ;}. A body block recorded
     * by the parser is kept even when empty.
     */
    public boolean hasStatementBody(final Element element, final boolean includeFeatureBindings) {
        return this.hasRecordedBodyOrTrailingComments(element)
                || this.bodyElements(element).anyMatch(related -> related instanceof Comment
                        || this.isStatementBodyMetadata(related)
                        || includeFeatureBindings && this.isExplicitFeatureBinding(related));
    }

    /**
     * Tells whether a feature in a restricted body carries user-written syntax (a name, direction, value or
     * specialization) and must therefore be rendered, as opposed to a feature generated from the declaration header.
     */
    public boolean isExplicitFeatureBinding(final Element element) {
        return element instanceof Feature
                && !this.isPromotedUserKeywordMetadata(element)
                && this.hasExplicitFeatureDeclaration(element);
    }

    /**
     * Tells whether an element needs a {@code { ... }} body: the source had one, trailing comments must be kept, or at
     * least one body member has explicit syntax (generated elements, nested end references and promoted keyword
     * metadata do not count).
     */
    public boolean hasSerializableBody(final Element element) {
        return this.hasRecordedBodyOrTrailingComments(element)
                || this.navigation.ownedRelationships(element).stream()
                        .anyMatch(relationship -> this.isSerializableBodyRelationship(element, relationship));
    }

    /**
     * Tells whether an action-like declaration (action, requirement, constraint, transition effect) ends with a block
     * rather than {@code ;}. A standalone action-body-parameter fragment always has one; parallel states and recorded
     * body blocks keep theirs, except on end features.
     */
    public boolean shouldSerializeBlockBody(final Element element) {
        final boolean explicitBlock = SourceSyntax.isRootParserEntry(this.context.options(), element, ParserFragments.ACTION_BODY_PARAMETER)
                || this.hasSerializableBody(element);
        final boolean nonEndBlock = !this.flags.isEnd(element)
                && (this.adapters.hasTextAdapter(element, ITextAdapter.BODY_BLOCK) || this.flags.isParallel(element));
        return explicitBlock || nonEndBlock;
    }

    /** Same as {@link #hasExplicitActionBodyContent(Element, List)} without skipped elements. */
    public boolean hasExplicitActionBodyContent(final Element element) {
        return this.hasExplicitActionBodyContent(element, List.of());
    }

    /**
     * Tells whether an action, connection or allocation body contains user-written members other than plain features
     * and comments, so that the body block must be emitted. Explicit relationships that own no element, such as imports
     * and aliases, count as members.
     *
     * @param skippedElements
     *            elements already rendered in the declaration header (such as connection ends), which do not count
     */
    public boolean hasExplicitActionBodyContent(final Element element, final List<Element> skippedElements) {
        return this.navigation.bodyRelationships(element).stream()
                .filter(relationship -> !this.isDeclarationTargetMembership(element, relationship))
                .anyMatch(relationship -> relationship instanceof Import
                        || !relationship.isImplied() && this.navigation.ownedRelatedElements(relationship).isEmpty()
                        || this.navigation.ownedRelatedElements(relationship).stream()
                                .filter(related -> !skippedElements.contains(related))
                                .anyMatch(this::isExplicitActionBodyElement));
    }

    /**
     * Tells whether {@code relationship} is the target of {@code assign target := value} (grammar
     * {@code featureChainMember}): the first relationship of the assignment whose metaclass is exactly
     * {@code Membership}. The assignment declaration writes it, so it is never a body member even though it owns no
     * element, like the imports and aliases that are.
     */
    public boolean isDeclarationTargetMembership(final Element owner, final Relationship relationship) {
        return owner instanceof AssignmentActionUsage
                && this.navigation.ownedRelationships(owner).stream()
                        .filter(candidate -> candidate.eClass() == SysMLPackage.eINSTANCE.getMembership())
                        .findFirst()
                        .filter(relationship::equals)
                        .isPresent();
    }

    /**
     * Tells whether {@code related} is the reference usage nested in an end feature. The end declaration renders its
     * name, multiplicity and uniqueness inline, so it must not also be written as a body member.
     */
    public boolean isNestedEndReference(final Element owner, final Element related) {
        if (!this.flags.isEnd(owner) || !(related instanceof ReferenceUsage)) {
            return false;
        }
        final Relationship membership = related.getOwningRelationship();
        return this.navigation.isMetaclass(membership, SysMLPackage.eINSTANCE.getOwningMembership())
                && membership.getOwningRelatedElement() == owner;
    }

    /** Tells whether an end feature owns a nested reference usage that its declaration renders inline. */
    public boolean hasNestedEndReference(final Element element) {
        return this.flags.isEnd(element) && this.bodyElements(element).anyMatch(related -> this.isNestedEndReference(element, related));
    }

    /** Tells whether an element is metadata written as a body member ({@code @M;}) rather than as a keyword prefix. */
    public boolean isStatementBodyMetadata(final Element element) {
        return this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getMetadataUsage())
                && !this.isPromotedUserKeywordMetadata(element);
    }

    /**
     * Returns the {@code #Keyword} metadata type names that prefix the element's declaration, in model order. Some
     * renderers use a non-empty result to force an otherwise implicit keyword, since the grammar requires one after a
     * user keyword.
     */
    public List<String> userKeywords(final Element element) {
        return this.promotedUserKeywordRelationships(element).stream()
                .flatMap(relationship -> this.navigation.ownedRelatedElements(relationship).stream())
                .filter(this::isPromotedUserKeywordMetadata)
                .map(this::metadataTypeName)
                .filter(Predicate.not(String::isBlank))
                .toList();
    }

    /**
     * Returns the body relationships whose metadata is written as a {@code #Keyword} declaration prefix, in model
     * order, so the declaration renderer can emit them (with their leading comments) before the declaration keyword.
     */
    public List<Relationship> promotedUserKeywordRelationships(final Element element) {
        // Runs for every declaration, and most own no metadata: plain loops avoid a stream per relationship.
        List<Relationship> promoted = List.of();
        for (final Relationship relationship : this.navigation.ownedRelationships(element)) {
            if (this.ownsPromotedUserKeywordMetadata(relationship) && this.navigation.isBodyRelationship(relationship)) {
                if (promoted.isEmpty()) {
                    promoted = new ArrayList<>();
                }
                promoted.add(relationship);
            }
        }
        return promoted;
    }

    private boolean ownsPromotedUserKeywordMetadata(final Relationship relationship) {
        for (final Element related : this.navigation.ownedRelatedElements(relationship)) {
            if (this.isPromotedUserKeywordMetadata(related)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tells whether an unnamed, bodiless metadata usage was written as a {@code #Keyword} prefix of its owner
     * ({@code usageExtensionKeyword} / {@code definitionExtensionKeyword}) rather than as {@code @M;} in the body. The
     * parser markers decide when present; otherwise the metadata is considered a prefix when it leads the owner's
     * members, or precedes the owner's typing/subsetting/redefinition.
     */
    public boolean isPromotedUserKeywordMetadata(final Element element) {
        boolean promoted = false;
        if (this.isUserKeywordMetadataCandidate(element)) {
            promoted = this.adapters.hasTextAdapter(element, ITextAdapter.PREFIX_METADATA)
                    || !this.adapters.hasTextAdapter(element, ITextAdapter.METADATA_AT_SIGN) && this.isInDeclarationPrefixPosition(element);
        }
        return promoted;
    }

    /** Returns the reference name of the metadata's first type, or an empty string when it is untyped. */
    public String metadataTypeName(final Element metadataUsage) {
        return this.specializationTargets.featureTypeNames(metadataUsage, this.context.danglingReferenceIndex(), this.referenceName())
                .stream()
                .findFirst()
                .orElse("");
    }

    /**
     * Tells whether an element is an unnamed helper produced by the parser or the model (conjugated port
     * definitions, multiplicity ranges, shorthand reference usages without their own typing, specialization or
     * value) and has no syntax of its own. The answer is cached for the run.
     */
    public boolean isAnonymousGeneratedElement(final Element element) {
        return this.anonymousGeneratedElements.computeIfAbsent(element, this::computeAnonymousGeneratedElement);
    }

    /**
     * Tells whether an end declaration repeats its usage keyword ({@code end port p}, {@code end ref r}). Port ends
     * follow the parser marker, defaulting to the keyword for models without text adapters; a bodiless reference end
     * outside a flow omits it because the bare end syntax already declares a reference usage.
     */
    public boolean shouldRenderEndFeatureKeyword(final Element element) {
        if (element instanceof PortUsage) {
            return this.adapters.hasTextAdapter(element, ITextAdapter.END_FEATURE_KEYWORD) || !SourceSyntax.hasTextAdapter(element);
        }
        return element instanceof FlowEnd
                || this.navigation.isOwnedByMetaclass(element, SysMLPackage.eINSTANCE.getFlowDefinition())
                || this.hasSerializableBody(element)
                || !(element instanceof ReferenceUsage);
    }

    /**
     * Returns the reference subsetting target of the membership's owned member, including an anonymous feature chain.
     * Body members cannot contribute a target or path segment.
     */
    public String referencedMembershipTarget(final Relationship membership) {
        return this.navigation.ownedRelatedElements(membership).stream()
                .map(this.referenceResolver::directReferencedElementTarget)
                .filter(target -> !target.isBlank()).findFirst().orElse("");
    }

    /** Tells whether a feature is named {@code name} or redefines a feature rendered with exactly that name. */
    public boolean isRedefinedFeature(final Element element, final String name) {
        return name.equals(this.referenceResolver.declaredName(element))
                || this.specializationTargets.redefinedFeatureNames(element, this.context.danglingReferenceIndex(), this.referenceName())
                        .contains(name);
    }

    private boolean hasRecordedBodyOrTrailingComments(final Element element) {
        return this.adapters.hasTextAdapter(element, ITextAdapter.BODY_BLOCK) || !this.adapters.trailingComments(element).isEmpty();
    }

    private Stream<Element> bodyElements(final Element element) {
        return this.navigation.bodyRelationships(element).stream()
                .flatMap(relationship -> this.navigation.ownedRelatedElements(relationship).stream());
    }

    private boolean hasExplicitFeatureDeclaration(final Element element) {
        return !this.referenceResolver.declaredName(element).isBlank()
                || !this.flags.direction(element).isBlank()
                || this.hasNonMultiplicityStructuralSuffix(element)
                || !this.multiplicities.multiplicityText(element, this.expressions::expressionText).isBlank();
    }

    /**
     * A body relationship counts when it is an import, an alias or a subject membership, a variant membership with
     * explicit syntax, or when it owns a member with explicit syntax.
     */
    private boolean isSerializableBodyRelationship(final Element owner, final Relationship relationship) {
        if (!this.navigation.isBodyRelationship(relationship)) {
            return false;
        }
        return this.isImportOrAlias(relationship)
                || relationship instanceof SubjectMembership
                || this.isSerializableVariantMembership(relationship)
                || this.navigation.ownedRelatedElements(relationship).stream().anyMatch(related -> this.isSerializableBodyElement(owner, related));
    }

    /**
     * Tells whether a relationship is an alias {@code alias X for Y}: a plain membership, which owns no element,
     * declaring its own name or short name. Header memberships such as an assignment target or a feature chain
     * segment name none.
     */
    private boolean isAlias(final Relationship relationship) {
        if (!this.navigation.isMetaclass(relationship, SysMLPackage.eINSTANCE.getMembership())) {
            return false;
        }
        final Membership membership = (Membership) relationship;
        return isNonBlank(membership.getMemberName()) || isNonBlank(membership.getMemberShortName());
    }

    private boolean isImportOrAlias(final Relationship relationship) {
        return relationship instanceof Import || this.isAlias(relationship);
    }

    private boolean isSerializableBodyElement(final Element owner, final Element related) {
        return this.hasOwnSyntax(related)
                && !this.isNestedEndReference(owner, related)
                && !this.isPromotedUserKeywordMetadata(related);
    }

    /**
     * A variant membership counts as body content when it names a referenced variant, or when it owns a variant with
     * explicit syntax.
     */
    private boolean isSerializableVariantMembership(final Relationship relationship) {
        if (!this.navigation.isMetaclass(relationship, SysMLPackage.eINSTANCE.getVariantMembership())) {
            return false;
        }
        return !this.referencedMembershipTarget(relationship).isBlank()
                || this.navigation.ownedRelatedElements(relationship).stream().anyMatch(this::hasOwnSyntax);
    }

    private boolean hasOwnSyntax(final Element element) {
        return !this.isAnonymousGeneratedElement(element) || this.isSerializableAnonymousReferenceUsage(element);
    }

    /**
     * An unnamed reference usage normally comes from shorthand syntax, but it still has to be written when it
     * designates something: a reference-subsetting target, an endpoint path, or its own body.
     */
    private boolean isSerializableAnonymousReferenceUsage(final Element element) {
        if (!this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getReferenceUsage())) {
            return false;
        }
        return !this.referenceResolver.firstDirectRelationshipTarget(element, SysMLPackage.eINSTANCE.getReferenceSubsetting()).isBlank()
                || !this.referenceResolver.endpointPath(element).isBlank()
                || this.hasSerializableBody(element);
    }

    /**
     * Tells whether a body element was written by the user: not a generated helper, which includes an unnamed reference
     * usage that designates something, such as the extra end <code>end ref ::> extra;</code> of a connector body.
     */
    private boolean isExplicitActionBodyElement(final Element element) {
        final boolean explicitModelElement = this.hasOwnSyntax(element)
                && !this.isPromotedUserKeywordMetadata(element)
                && !this.isStatementBodyMetadata(element);
        return explicitModelElement
                && !(element instanceof Comment)
                && !this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getFeature());
    }

    /**
     * Tells whether a metadata usage could be written as a {@code #M} prefix keyword, which only spells its type:
     * it must have no name, short name, {@code about} target or body, since the prefix form cannot keep them.
     */
    private boolean isUserKeywordMetadataCandidate(final Element element) {
        return this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getMetadataUsage())
                && this.isUnidentified(element)
                && !this.hasExplicitAnnotation(element)
                && !this.hasSerializableBody(element);
    }

    /** Tells whether an element has neither a declared name nor a short name. */
    private boolean isUnidentified(final Element element) {
        return this.referenceResolver.declaredName(element).isBlank() && !isNonBlank(StoredModel.shortName(element));
    }

    /** Tells whether an element owns an annotation written in the source, such as the {@code about} of a metadata. */
    private boolean hasExplicitAnnotation(final Element element) {
        return this.navigation.ownedRelationships(element).stream()
                .anyMatch(relationship -> relationship instanceof Annotation && !relationship.isImplied());
    }

    /** Infers a {@code #Keyword} prefix from the position of the metadata membership among its owner's relationships. */
    private boolean isInDeclarationPrefixPosition(final Element metadataUsage) {
        return metadataUsage.eContainer() instanceof final Relationship membership
                && membership.eContainer() instanceof final Element owner
                && (this.isLeadingUserKeywordMetadata(owner, membership) || this.isMetadataBeforeStructuralSuffix(owner, membership));
    }

    /**
     * Prefix metadata ({@code usageExtensionKeyword}, {@code definitionExtensionKeyword}) is parsed before any other
     * relationship, so a candidate is leading only when every relationship before its membership is itself a keyword
     * candidate. Any other relationship, including a typing or specialization whose target is not owned, a comment or
     * an import, means the metadata was written in the body.
     */
    private boolean isLeadingUserKeywordMetadata(final Element owner, final Relationship metadataMembership) {
        return this.navigation.ownedRelationships(owner).stream()
                .filter(relationship -> relationship == metadataMembership || !this.ownsOnlyKeywordCandidates(relationship))
                .findFirst()
                .map(relationship -> relationship == metadataMembership)
                .orElse(false);
    }

    /** Tells whether a relationship owns elements and all of them may be written as {@code #Keyword} prefixes. */
    private boolean ownsOnlyKeywordCandidates(final Relationship relationship) {
        final List<Element> members = this.navigation.ownedRelatedElements(relationship);
        return !members.isEmpty() && members.stream().allMatch(this::isUserKeywordMetadataCandidate);
    }

    /**
     * Body members follow the declaration's typing/subsetting/redefinition relationships, so metadata owned before
     * the first of them can only come from the declaration prefix.
     */
    private boolean isMetadataBeforeStructuralSuffix(final Element owner, final Relationship metadataMembership) {
        final List<Relationship> relationships = this.navigation.ownedRelationships(owner);
        final int metadataIndex = relationships.indexOf(metadataMembership);
        final int firstSuffixIndex = IntStream.range(0, relationships.size())
                .filter(index -> this.isStructuralSuffix(relationships.get(index)))
                .findFirst()
                .orElse(-1);
        return metadataIndex >= 0 && metadataIndex < firstSuffixIndex;
    }

    private boolean isStructuralSuffix(final Relationship relationship) {
        return relationship instanceof FeatureTyping || relationship instanceof Subsetting || relationship instanceof Redefinition;
    }

    private boolean computeAnonymousGeneratedElement(final Element element) {
        if (!this.referenceResolver.declaredName(element).isBlank()
                || element instanceof ReferenceUsage && this.flags.isEnd(element) && this.hasSerializableBody(element)) {
            return false;
        }
        // Conjugated and multiplicity helper elements form generated families whose
        // concrete metaclasses intentionally follow these naming conventions.
        final String metaclassName = element.eClass().getName();
        return metaclassName.startsWith(CONJUGATED_METACLASS_PREFIX)
                || metaclassName.startsWith(MULTIPLICITY_METACLASS_PREFIX)
                || element instanceof ReferenceUsage && !this.hasExplicitReferenceDeclaration(element);
    }

    /**
     * Tells whether a reference usage needs its {@code ref} keyword to be parsed as a declaration: it has no name,
     * short name, multiplicity, specialization or value, so only a direction or a body would remain, as in
     * {@code in ref;} or {@code ref { doc ... }}, and neither can start a usage declaration on its own.
     *
     * @param element
     *            the element to write
     * @return {@code true} for a reference usage whose declaration would be empty without {@code ref}
     */
    public boolean needsRefKeywordToDeclare(final Element element) {
        return this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getReferenceUsage())
                && this.referenceResolver.declaredName(element).isBlank()
                && !isNonBlank(StoredModel.shortName(element))
                && !this.declaresFeatureClauses(element);
    }

    /** Tells whether a feature declares a typing, specialization, value or multiplicity clause. */
    private boolean declaresFeatureClauses(final Element element) {
        return this.hasNonMultiplicityStructuralSuffix(element)
                || !this.multiplicities.multiplicityText(element, this.expressions::expressionText).isBlank();
    }

    private static boolean isNonBlank(final String text) {
        return text != null && !text.isBlank();
    }

    /**
     * Tells whether an unnamed reference usage was written by the user, as in {@code ref [2];}, {@code in ref;},
     * {@code ref <r>;} or {@code ref { doc ... }}: it declares a direction, multiplicity, specialization or value, a
     * short name, or a body. The reference usages the parser generates for shorthand syntax declare none of these,
     * except a body for a shorthand that names its target by reference subsetting, as {@code variant q { ... }}.
     */
    private boolean hasExplicitReferenceDeclaration(final Element element) {
        return this.hasExplicitFeatureDeclaration(element)
                || isNonBlank(StoredModel.shortName(element))
                || this.hasBodyWithoutReferencedTarget(element);
    }

    /** Tells whether a reference usage has a body and does not name its target by reference subsetting. */
    private boolean hasBodyWithoutReferencedTarget(final Element element) {
        return this.referenceResolver.firstDirectRelationshipTarget(element, SysMLPackage.eINSTANCE.getReferenceSubsetting()).isBlank()
                && this.hasSerializableBody(element);
    }

    /** Tells whether a feature declares typing, specialization, subsetting, redefinition or a value. */
    private boolean hasNonMultiplicityStructuralSuffix(final Element element) {
        // A value settles the answer cheaply, as for the common `:>> x = v;`, before any target name is rendered; and
        // without a typing, subclassification or subsetting, no target name can exist.
        return !this.navigation.featureValues(element).isEmpty()
                || this.navigation.ownedRelationships(element).stream().anyMatch(ElementSyntaxQueries::isSpecialization)
                        && this.hasSpecializationTargetName(element);
    }

    /** Tells whether a typing, subclassification, subsetting or redefinition of the element names a target. */
    private boolean hasSpecializationTargetName(final Element element) {
        final DanglingReferenceIndex danglingReferences = this.context.danglingReferenceIndex();
        final Function<Element, String> referenceName = this.referenceName();
        return !this.specializationTargets.featureTypeNames(element, danglingReferences, referenceName).isEmpty()
                || !this.specializationTargets.supertypeNames(element, danglingReferences, referenceName).isEmpty()
                || !this.specializationTargets.subsettedFeatureNames(element, danglingReferences, referenceName).isEmpty()
                || !this.specializationTargets.redefinedFeatureNames(element, danglingReferences, referenceName).isEmpty();
    }

    /** Matches the relationships that {@link #hasNonMultiplicityStructuralSuffix(Element)} names: typings, subclassifications and subsettings (including redefinitions). */
    private static boolean isSpecialization(final Relationship relationship) {
        return relationship instanceof FeatureTyping || relationship instanceof Subclassification || relationship instanceof Subsetting;
    }

    private Function<Element, String> referenceName() {
        return this.context.nameRenderer()::referenceName;
    }
}
