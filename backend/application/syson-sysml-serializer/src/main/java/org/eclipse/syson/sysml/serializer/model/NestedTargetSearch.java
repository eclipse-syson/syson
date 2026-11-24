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
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.syson.sysml.serializer.names.NameEscaper;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureChainExpression;
import org.omg.sysml.lang.sysml.FeatureChaining;
import org.omg.sysml.lang.sysml.FeatureReferenceExpression;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Searches the owned subtree of an element for reference text that is not stored on the element itself: parser
 * spellings of unresolved references, proxy names, feature-chain segments ({@code a.b.c}), assignment targets and the
 * targets of anonymous nested reference usages.
 * <p>
 * Look here when a shorthand syntax (assignment, feature chain, connector or succession end, performed action,
 * {@code ::>} target of a nested usage) loses or garbles its target, or when an unresolved reference disappears from
 * the output. Parser spellings from {@link DanglingReferenceIndex} are generally preferred so that partially resolved
 * models round-trip; the text of a single reference is chosen by {@link ReferenceTargets}.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class NestedTargetSearch {

    /** The relationships that carry the target of an exact reference usage, in lookup order. */
    private static final List<EClass> REFERENCE_USAGE_RELATIONSHIPS = List.of(SysMLPackage.eINSTANCE.getReferenceSubsetting(),
            SysMLPackage.eINSTANCE.getRedefinition(), SysMLPackage.eINSTANCE.getSubsetting());

    private final ModelNavigation navigation;

    private final ReferenceTargets referenceTargets;

    /**
     * Creates the subtree searches.
     *
     * @param navigation
     *            containment navigation
     * @param referenceTargets
     *            chooses the text of each reference met in the subtree
     */
    public NestedTargetSearch(final ModelNavigation navigation, final ReferenceTargets referenceTargets) {
        this.navigation = Objects.requireNonNull(navigation);
        this.referenceTargets = Objects.requireNonNull(referenceTargets);
    }

    /**
     * Returns the first reference text found in the owned subtree of an element, for shorthand syntaxes whose target is
     * stored on a nested element. An unresolved proxy leaf comes first, then a target renamed since parsing, then parser
     * spellings of the element and of its relationships, depth first.
     */
    public String firstDanglingTarget(final Element element, final DanglingReferenceIndex danglingReferences) {
        if (element == null) {
            return "";
        }
        String target = this.referenceTargets.firstProxyReferenceName(element);
        if (target.isBlank()) {
            target = this.renamedRelationshipTarget(element, danglingReferences);
        }
        if (target.isBlank()) {
            target = danglingReferences.targets(element).stream().findFirst().orElse("");
        }
        if (target.isBlank()) {
            target = this.navigation.ownedRelationships(element).stream()
                    .map(relationship -> this.firstDanglingRelationshipTarget(relationship, danglingReferences))
                    .filter(candidate -> !candidate.isBlank())
                    .findFirst()
                    .orElse("");
        }
        return target;
    }

    /**
     * Returns the segments that a feature chain expression appends to its source ({@code src.a.b}): the parser spelling
     * of each chaining feature, else a fallback target name.
     */
    public List<String> chainedFeatureNames(final Element expression, final DanglingReferenceIndex danglingReferences) {
        return this.navigation.ownedRelationships(expression).stream()
                .filter(relationship -> this.navigation.isMetaclass(relationship, SysMLPackage.eINSTANCE.getOwningMembership()))
                .flatMap(relationship -> this.navigation.ownedRelatedElements(relationship).stream())
                .filter(Feature.class::isInstance).map(Feature.class::cast)
                .flatMap(this::featureChainings)
                .map(chaining -> this.chainingSegmentName(chaining, danglingReferences))
                .filter(name -> !name.isBlank())
                .toList();
    }

    /**
     * Returns the segments of the feature chain owned directly by this feature. Values, bodies and nested chains are
     * excluded; null gives an empty list. This is the shape of grammar rule {@code ownedFeatureChain}.
     */
    public List<String> directFeatureChainNames(final Element element, final DanglingReferenceIndex danglingReferences) {
        return this.navigation.ownedRelationships(element).stream()
                .filter(FeatureChaining.class::isInstance).map(FeatureChaining.class::cast)
                .map(chaining -> this.referenceTargets.renderedReferenceName(chaining, this.chainingSegmentName(chaining, danglingReferences)))
                .filter(name -> !name.isBlank()).toList();
    }

    /**
     * Returns the feature a feature chain expression navigates to when it holds it through a plain membership, as in
     * {@code src.f}: the parser spelling while it still names the member, else the member's current name, with source
     * quoting; empty when there is no such membership.
     */
    public String chainMemberSegment(final FeatureChainExpression expression, final DanglingReferenceIndex danglingReferences) {
        return this.navigation.ownedRelationships(expression).stream()
                .filter(relationship -> this.navigation.isMetaclass(relationship, SysMLPackage.eINSTANCE.getMembership()))
                .map(membership -> this.referenceTargets.renderedReferenceName(membership,
                        this.referenceTargets.chainSegmentName(membership, EmfReferenceNames.MEMBER_ELEMENT, danglingReferences, true)))
                .filter(name -> !name.isBlank())
                .findFirst()
                .orElse("");
    }

    /**
     * Returns the chaining-feature segments of every feature chaining in the owned subtree, depth first and with source
     * quoting, to write a chained target such as {@code a.b.c} held by an anonymous chained feature.
     */
    public List<String> chainedFeatureNamesInSubtree(final Element element, final DanglingReferenceIndex danglingReferences) {
        if (element == null) {
            return List.of();
        }
        final List<String> names = new ArrayList<>();
        this.collectChainedFeatureNames(element, danglingReferences, names);
        return List.copyOf(names);
    }

    /**
     * Returns the dotted chain owned by the first relationship of the given type that has one, e.g. the target of
     * {@code :>> a.b} where the redefinition owns an anonymous chained feature.
     */
    public String firstChainedFeatureName(final Element element, final Class<? extends Relationship> relationshipType,
            final DanglingReferenceIndex danglingReferences) {
        return this.navigation.ownedRelationships(element).stream()
                .filter(relationshipType::isInstance)
                .map(relationship -> this.ownedFeatureChainText(relationship, danglingReferences))
                .filter(name -> !name.isBlank())
                .findFirst()
                .orElse("");
    }

    /**
     * Returns the chaining-feature segments of the owned subtree with their model targets, so that each segment of a
     * chain can be traced or linked to the element it designates.
     */
    public List<ModelReference> chainedFeatureReferencesInSubtree(final Element element, final DanglingReferenceIndex danglingReferences,
            final Function<Element, String> resolvedName) {
        if (element == null) {
            return List.of();
        }
        final List<ModelReference> references = new ArrayList<>();
        this.collectChainedFeatureReferences(element, danglingReferences, resolvedName, references);
        return List.copyOf(references);
    }

    /**
     * Returns the target text of a feature reference expression: the member element of its membership as chosen by
     * {@link ReferenceTargets}, else the parser spelling or fallback name, with source quoting.
     */
    public String featureReferenceName(final FeatureReferenceExpression expression, final DanglingReferenceIndex danglingReferences,
            final Function<Element, String> resolvedName) {
        final Membership membership = this.navigation.ownedRelationships(expression).stream()
                .filter(Membership.class::isInstance)
                .map(Membership.class::cast)
                .findFirst()
                .orElse(null);
        String name = this.referenceTargets.referenceTargets(membership, EmfReferenceNames.MEMBER_ELEMENT, danglingReferences, resolvedName)
                .stream()
                .findFirst()
                .map(ModelReference::name)
                .orElse("");
        if (name.isBlank()) {
            name = this.memberReferenceName(membership, danglingReferences);
        }
        return this.referenceTargets.renderedReferenceName(membership, name);
    }

    /**
     * Returns the target of the first nested exact reference usage, trying its reference subsetting, redefinition and
     * subsetting before its dangling targets. Shorthand ends store their target on such an anonymous usage, written as
     * the segment after the end's path ({@code a.c}).
     */
    public String nestedReferenceTarget(final Element element, final DanglingReferenceIndex danglingReferences) {
        return this.navigation.firstNestedText(element, related -> this.referenceUsageSegment(related, danglingReferences));
    }

    /**
     * Returns the target of a nested reference usage as a path segment, or an empty string for any other element. The
     * parser spelling is kept while it still designates the target; otherwise the target was renamed or retargeted and
     * is written by its own name, the feature navigated from the preceding path, rather than by a qualified name inside
     * the feature path.
     */
    private String referenceUsageSegment(final Element element, final DanglingReferenceIndex danglingReferences) {
        if (!this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getReferenceUsage())) {
            return "";
        }
        final String target = this.referenceUsageTarget(element, danglingReferences);
        final Element installedTarget = REFERENCE_USAGE_RELATIONSHIPS.stream()
                .map(relationshipClass -> this.referenceTargets.firstDirectRelationshipTargetElement(element, relationshipClass))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
        String segment = target;
        final String installedName = StoredModel.name(installedTarget);
        if (installedName != null && !this.isParsedSpelling(element, target, danglingReferences)) {
            segment = new NameEscaper().escapeDeclaredName(installedName);
        }
        return segment;
    }

    /** Tells whether the text is the rendered parser spelling of one of the element's owned relationships. */
    private boolean isParsedSpelling(final Element element, final String text, final DanglingReferenceIndex danglingReferences) {
        return this.navigation.ownedRelationships(element).stream()
                .anyMatch(relationship -> danglingReferences.targets(relationship).stream()
                        .anyMatch(spelling -> text.equals(this.referenceTargets.renderedReferenceName(relationship, spelling))));
    }

    /**
     * Returns the dotted text of the feature chain owned by a relationship, such as the anonymous chained feature of
     * {@code :>> a.b}, or an empty string.
     */
    String ownedFeatureChainText(final Relationship relationship, final DanglingReferenceIndex danglingReferences) {
        return this.navigation.ownedRelatedElements(relationship).stream()
                .map(related -> String.join(SysMLTokens.PATH_SEPARATOR, this.directFeatureChainNames(related, danglingReferences)))
                .filter(text -> !text.isBlank())
                .findFirst()
                .orElse("");
    }

    /**
     * Returns the dotted, quoted feature chain an element owns through a plain owning membership, such as the assigned
     * feature of {@code assign source.a.b := value}, or an empty string.
     */
    public String ownedFeatureChainName(final Element element, final DanglingReferenceIndex danglingReferences) {
        return this.navigation.ownedRelationships(element).stream()
                .filter(relationship -> this.navigation.isMetaclass(relationship, SysMLPackage.eINSTANCE.getOwningMembership()))
                .flatMap(relationship -> this.navigation.ownedRelatedElements(relationship).stream())
                .filter(Feature.class::isInstance)
                .map(Feature.class::cast)
                .flatMap(this::featureChainings)
                .map(chaining -> this.referenceTargets.renderedReferenceName(chaining, this.chainingSegmentName(chaining, danglingReferences)))
                .filter(name -> !name.isBlank())
                .collect(Collectors.joining(SysMLTokens.PATH_SEPARATOR));
    }

    /** Returns the spelling of one chaining feature; see {@link ReferenceTargets#chainSegmentName}. */
    private String chainingSegmentName(final FeatureChaining chaining, final DanglingReferenceIndex danglingReferences) {
        return this.referenceTargets.chainSegmentName(chaining, EmfReferenceNames.CHAINING_FEATURE, danglingReferences,
                this.followsPath(chaining));
    }

    /**
     * Tells whether a chaining feature is navigated from a preceding segment: it is not the first chaining of its
     * chained feature, or that feature is the member a feature chain expression appends to its source ({@code src.a.b}).
     */
    private boolean followsPath(final FeatureChaining chaining) {
        final Element chainedFeature = chaining.getOwningRelatedElement();
        if (chainedFeature == null) {
            return false;
        }
        final boolean firstChaining = this.navigation.ownedRelationships(chainedFeature).stream()
                .filter(FeatureChaining.class::isInstance)
                .findFirst()
                .orElse(null) == chaining;
        final Relationship membership = chainedFeature.getOwningRelationship();
        return !firstChaining || membership != null
                && this.navigation.isMetaclass(membership, SysMLPackage.eINSTANCE.getOwningMembership())
                && membership.getOwningRelatedElement() instanceof FeatureChainExpression;
    }

    private Stream<FeatureChaining> featureChainings(final Feature feature) {
        return this.navigation.ownedRelationships(feature).stream()
                .filter(FeatureChaining.class::isInstance)
                .map(FeatureChaining.class::cast);
    }

    /** Returns the target of an exact reference usage: its first named specialization target, else a dangling target. */
    private String referenceUsageTarget(final Element element, final DanglingReferenceIndex danglingReferences) {
        return REFERENCE_USAGE_RELATIONSHIPS.stream()
                .map(relationshipClass -> this.referenceTargets.firstDirectRelationshipTarget(element, relationshipClass, danglingReferences))
                .filter(target -> !target.isBlank())
                .findFirst()
                .orElseGet(() -> this.firstDanglingTarget(element, danglingReferences));
    }

    private void collectChainedFeatureReferences(final Element element, final DanglingReferenceIndex danglingReferences,
            final Function<Element, String> resolvedName, final List<ModelReference> references) {
        for (final Relationship relationship : this.navigation.ownedRelationships(element)) {
            if (relationship instanceof FeatureChaining) {
                references.addAll(this.referenceTargets.referenceTargets(relationship, EmfReferenceNames.CHAINING_FEATURE, danglingReferences, resolvedName));
            }
            for (final Element related : this.navigation.ownedRelatedElements(relationship)) {
                this.collectChainedFeatureReferences(related, danglingReferences, resolvedName, references);
            }
        }
    }

    private void collectChainedFeatureNames(final Element element, final DanglingReferenceIndex danglingReferences, final List<String> names) {
        for (final Relationship relationship : this.navigation.ownedRelationships(element)) {
            if (relationship instanceof final FeatureChaining chaining) {
                final String name = this.chainingSegmentName(chaining, danglingReferences);
                if (!name.isBlank()) {
                    names.add(this.referenceTargets.renderedReferenceName(relationship, name));
                }
            }
            for (final Element related : this.navigation.ownedRelatedElements(relationship)) {
                this.collectChainedFeatureNames(related, danglingReferences, names);
            }
        }
    }

    private String memberReferenceName(final Membership membership, final DanglingReferenceIndex danglingReferences) {
        String name = this.referenceTargets.firstReferenceTargetName(membership, EmfReferenceNames.MEMBER_ELEMENT);
        if (name.isBlank()) {
            name = this.referenceTargets.aliasTargetName(membership, danglingReferences);
        }
        return this.referenceTargets.renderedReferenceName(membership, name);
    }

    /** Searches a relationship, then its owned related elements, for the first proxy name or parser spelling. */
    private String firstDanglingRelationshipTarget(final Relationship relationship, final DanglingReferenceIndex danglingReferences) {
        String target = this.referenceTargets.firstProxyReferenceName(relationship);
        if (target.isBlank()) {
            target = this.referenceTargets.renderedReferenceName(relationship,
                    this.referenceTargets.currentParsedTargetName(relationship, danglingReferences));
        }
        if (target.isBlank()) {
            target = this.firstDanglingTarget(relationship, danglingReferences);
        }
        if (target.isBlank()) {
            target = this.navigation.ownedRelatedElements(relationship).stream()
                    .map(related -> this.firstDanglingTarget(related, danglingReferences))
                    .filter(candidate -> !candidate.isBlank())
                    .findFirst()
                    .orElse("");
        }
        return target;
    }

    /**
     * Returns the current model name of a feature reference (or of the typing of an untyped expression) whose target was
     * renamed after parsing, i.e. whose installed target leaf no longer matches the parser spelling.
     */
    private String renamedRelationshipTarget(final Element element, final DanglingReferenceIndex danglingReferences) {
        final boolean featureReference = element instanceof FeatureReferenceExpression;
        final boolean untypedExpression = this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getExpression());
        return this.navigation.ownedRelationships(element).stream()
                .filter(relationship -> featureReference
                        || untypedExpression && this.navigation.isMetaclass(relationship, SysMLPackage.eINSTANCE.getFeatureTyping()))
                .map(relationship -> this.renamedTarget(relationship, danglingReferences))
                .filter(target -> !target.isBlank())
                .findFirst()
                .orElse("");
    }

    private String renamedTarget(final Relationship relationship, final DanglingReferenceIndex danglingReferences) {
        final String parsedName = danglingReferences.targets(relationship).stream().findFirst().orElse("");
        String renamedTarget = "";
        if (!parsedName.isBlank()) {
            final String modelName = this.referenceTargets.firstExternalModelReferenceName(relationship);
            if (!modelName.isBlank() && !this.referenceTargets.referenceLeaf(modelName).equals(this.referenceTargets.referenceLeaf(parsedName))) {
                renamedTarget = this.referenceTargets.renderedReferenceName(relationship, modelName);
            }
        }
        return renamedTarget;
    }
}
