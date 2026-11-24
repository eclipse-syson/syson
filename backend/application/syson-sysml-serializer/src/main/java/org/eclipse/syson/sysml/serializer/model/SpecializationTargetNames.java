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

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.ConjugatedPortTyping;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.FeatureTyping;
import org.omg.sysml.lang.sysml.Redefinition;
import org.omg.sysml.lang.sysml.ReferenceSubsetting;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.Subclassification;
import org.omg.sysml.lang.sysml.Subsetting;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Builds the target names written in specialization clauses: typing ({@code :}), subclassification and subsetting
 * ({@code :>}), redefinition ({@code :>>}), reference subsetting ({@code ::>}) and cross subsetting ({@code =>}).
 * <p>
 * Look here when a specialization target is missing, duplicated, wrongly grouped, or spelled differently from the
 * source, including conjugated port typings (<code>&#126;P</code>) and targets written as feature chains ({@code :>> a.b}).
 * Each relationship kind fixes which EMF references are consulted and whether model targets or parser spellings win;
 * the text of a single reference is chosen by {@link ReferenceTargets}. Clause placement is decided in the
 * {@code specialization} package, not here.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class SpecializationTargetNames {

    private final ModelNavigation navigation;

    private final ReferenceTargets referenceTargets;

    private final NestedTargetSearch nestedTargets;

    /**
     * Creates the specialization target queries.
     *
     * @param navigation
     *            containment navigation
     * @param referenceTargets
     *            chooses the text of each target reference
     * @param nestedTargets
     *            supplies the text of targets written as feature chains
     */
    public SpecializationTargetNames(final ModelNavigation navigation, final ReferenceTargets referenceTargets,
            final NestedTargetSearch nestedTargets) {
        this.navigation = Objects.requireNonNull(navigation);
        this.referenceTargets = Objects.requireNonNull(referenceTargets);
        this.nestedTargets = Objects.requireNonNull(nestedTargets);
    }

    /**
     * Returns the targets of the explicit typings of an element ({@code : T1, T2} or {@code defined by}), in model
     * order, skipping typings that produce no text.
     */
    public List<String> featureTypeNames(final Element element, final DanglingReferenceIndex danglingReferences,
            final Function<Element, String> resolvedName) {
        return this.relationshipNames(element, FeatureTyping.class::isInstance,
                relationship -> this.featureTypeName((FeatureTyping) relationship, danglingReferences, resolvedName));
    }

    /**
     * Returns the target of one typing. A conjugated port typing is written <code>&#126;P</code>: its port definition is named
     * first, then the plain type, so that the conjugation marker is never lost or doubled. A typing by a feature chain
     * ({@code : a.b}) owns that chain instead of referencing a type and is named by its dotted text.
     */
    public String featureTypeName(final FeatureTyping relationship, final DanglingReferenceIndex danglingReferences,
            final Function<Element, String> resolvedName) {
        String name = "";
        if (relationship instanceof ConjugatedPortTyping) {
            name = this.referenceTargets.referenceName(relationship, danglingReferences, resolvedName, EmfReferenceNames.PORT_DEFINITION,
                    EmfReferenceNames.CONJUGATED_PORT_DEFINITION, EmfReferenceNames.TYPE, EmfReferenceNames.GENERAL);
            if (!name.isBlank() && !name.startsWith(SysMLTokens.CONJUGATION_OPERATOR)) {
                name = SysMLTokens.CONJUGATION_OPERATOR + name;
            }
        }
        if (name.isBlank()) {
            name = this.referenceOrChainedFeatureName(relationship, danglingReferences, resolvedName, EmfReferenceNames.TYPE,
                    EmfReferenceNames.GENERAL);
        }
        return name;
    }

    /** Returns the subclassification targets of a classifier ({@code :>} / {@code specializes}), in model order. */
    public List<String> supertypeNames(final Element element, final DanglingReferenceIndex danglingReferences,
            final Function<Element, String> resolvedName) {
        return this.relationshipNames(element, Subclassification.class::isInstance,
                relationship -> this.supertypeName(relationship, danglingReferences, resolvedName));
    }

    /** Returns the target of one subclassification (its superclassifier, else its general type). */
    public String supertypeName(final Relationship relationship, final DanglingReferenceIndex danglingReferences,
            final Function<Element, String> resolvedName) {
        return this.referenceTargets.referenceName(relationship, danglingReferences, resolvedName, EmfReferenceNames.SUPERCLASSIFIER,
                EmfReferenceNames.GENERAL);
    }

    /**
     * Returns the targets of the plain subsettings of a feature ({@code :>} / {@code subsets}), flattened. Reference,
     * cross subsettings and redefinitions are excluded because each has its own clause.
     */
    public List<String> subsettedFeatureNames(final Element element, final DanglingReferenceIndex danglingReferences,
            final Function<Element, String> resolvedName) {
        return this.relationshipNameGroups(element, this::isPlainSubsetting,
                relationship -> this.subsettedFeatureNamesForRelationship(relationship, danglingReferences, resolvedName))
                .stream()
                .flatMap(List::stream)
                .filter(name -> !name.isBlank())
                .toList();
    }

    /**
     * Returns the targets of one subsetting. Installed model targets win (subsetted feature, then general type); a
     * fully unresolved subsetting falls back to parser spellings, then to a fallback name or its owned feature chain.
     */
    public List<String> subsettedFeatureNamesForRelationship(final Relationship relationship, final DanglingReferenceIndex danglingReferences,
            final Function<Element, String> resolvedName) {
        List<ModelReference> modelTargets = this.referenceTargets.referenceTargets(relationship, EmfReferenceNames.SUBSETTED_FEATURE,
                danglingReferences, resolvedName);
        if (modelTargets.isEmpty()) {
            modelTargets = this.referenceTargets.referenceTargets(relationship, EmfReferenceNames.GENERAL, danglingReferences, resolvedName);
        }
        final List<String> parsedNames = this.parsedNames(relationship, danglingReferences, EmfReferenceNames.SUBSETTED_FEATURE,
                EmfReferenceNames.GENERAL);
        List<String> names;
        if (!modelTargets.isEmpty()) {
            names = modelTargets.stream()
                    .map(target -> this.referenceTargets.renderedReferenceName(relationship, target.name()))
                    .toList();
        } else if (!parsedNames.isEmpty()) {
            names = this.renderedNames(relationship, parsedNames);
        } else {
            names = this.singleName(this.referenceOrChainedFeatureName(relationship, danglingReferences, resolvedName,
                    EmfReferenceNames.SUBSETTED_FEATURE, EmfReferenceNames.GENERAL));
        }
        return names;
    }

    /** Returns the redefinition targets of a feature ({@code :>>} / {@code redefines}), flattened. */
    public List<String> redefinedFeatureNames(final Element element, final DanglingReferenceIndex danglingReferences,
            final Function<Element, String> resolvedName) {
        return this.redefinedFeatureNameGroups(element, danglingReferences, resolvedName).stream()
                .flatMap(List::stream)
                .filter(name -> !name.isBlank())
                .toList();
    }

    /** Returns the redefinition targets grouped by relationship, so that source grouping can be preserved. */
    public List<List<String>> redefinedFeatureNameGroups(final Element element, final DanglingReferenceIndex danglingReferences,
            final Function<Element, String> resolvedName) {
        return this.relationshipNameGroups(element, Redefinition.class::isInstance,
                relationship -> this.redefinedFeatureNamesForRelationship(relationship, danglingReferences, resolvedName));
    }

    /**
     * Returns the targets of one redefinition. Unlike subsetting, parser spellings win here, which keeps a redefined
     * feature written with the inherited name used in the source, as long as they still designate the installed
     * redefined features; otherwise the model name or owned feature chain.
     */
    public List<String> redefinedFeatureNamesForRelationship(final Relationship relationship, final DanglingReferenceIndex danglingReferences,
            final Function<Element, String> resolvedName) {
        final List<String> parsedNames = this.parsedNames(relationship, danglingReferences, EmfReferenceNames.REDEFINED_FEATURE,
                EmfReferenceNames.SUBSETTED_FEATURE);
        List<String> names;
        if (!parsedNames.isEmpty()
                && this.referenceTargets.parsedRedefinedNamesDesignateTargets(relationship, parsedNames)) {
            names = this.renderedNames(relationship, parsedNames);
        } else {
            names = this.singleName(this.referenceOrChainedFeatureName(relationship, danglingReferences, resolvedName,
                    EmfReferenceNames.REDEFINED_FEATURE, EmfReferenceNames.SUBSETTED_FEATURE));
        }
        return names;
    }

    /** Returns the reference subsetting targets of a feature ({@code ::>} / {@code references}). */
    public List<String> referencedFeatureNames(final Element element, final DanglingReferenceIndex danglingReferences,
            final Function<Element, String> resolvedName) {
        return this.relationshipNames(element, ReferenceSubsetting.class::isInstance,
                relationship -> this.referencedFeatureName(relationship, danglingReferences, resolvedName));
    }

    /** Returns the target of one reference subsetting, or the text of its owned feature chain. */
    public String referencedFeatureName(final Relationship relationship, final DanglingReferenceIndex danglingReferences,
            final Function<Element, String> resolvedName) {
        return this.referenceOrChainedFeatureName(relationship, danglingReferences, resolvedName, EmfReferenceNames.REFERENCED_FEATURE,
                EmfReferenceNames.SUBSETTED_FEATURE);
    }

    /** Returns the cross subsetting targets of a feature ({@code =>} / {@code crosses}). */
    public List<String> crossedFeatureNames(final Element element, final DanglingReferenceIndex danglingReferences,
            final Function<Element, String> resolvedName) {
        return this.relationshipNames(element, this::isCrossSubsetting,
                relationship -> this.crossedFeatureName(relationship, danglingReferences, resolvedName));
    }

    /** Returns the target of one cross subsetting, or the text of its owned feature chain. */
    public String crossedFeatureName(final Relationship relationship, final DanglingReferenceIndex danglingReferences,
            final Function<Element, String> resolvedName) {
        return this.referenceOrChainedFeatureName(relationship, danglingReferences, resolvedName, EmfReferenceNames.CROSSED_FEATURE,
                EmfReferenceNames.SUBSETTED_FEATURE);
    }

    /**
     * Returns the {@code ::>} targets of the exact reference subsettings owned directly by an element, as used by
     * connector ends and similar shorthand forms. A proxy target is named by its URI leaf; a resolved target keeps the
     * parser spelling only while that spelling still designates it.
     */
    public List<String> directReferenceSubsettingTargetNames(final Element element, final DanglingReferenceIndex danglingReferences,
            final Function<Element, String> resolvedName) {
        return this.relationshipNames(element, this::isExactReferenceSubsetting,
                relationship -> this.directReferenceSubsettingTargetName(relationship, danglingReferences, resolvedName));
    }

    private boolean isPlainSubsetting(final Relationship relationship) {
        return relationship instanceof Subsetting
                && !(relationship instanceof ReferenceSubsetting)
                && !(relationship instanceof Redefinition)
                && !this.isCrossSubsetting(relationship);
    }

    private boolean isCrossSubsetting(final Relationship relationship) {
        return this.navigation.isMetaclass(relationship, SysMLPackage.eINSTANCE.getCrossSubsetting());
    }

    private boolean isExactReferenceSubsetting(final Relationship relationship) {
        return this.navigation.isMetaclass(relationship, SysMLPackage.eINSTANCE.getReferenceSubsetting());
    }

    /** Returns the parser spellings of the first of the given references that the parser left unresolved. */
    private List<String> parsedNames(final Relationship relationship, final DanglingReferenceIndex danglingReferences, final String referenceName,
            final String fallbackReferenceName) {
        List<String> parsedNames = danglingReferences.targets(relationship, referenceName);
        if (parsedNames.isEmpty()) {
            parsedNames = danglingReferences.targets(relationship, fallbackReferenceName);
        }
        return parsedNames;
    }

    private List<String> renderedNames(final Relationship relationship, final List<String> names) {
        return names.stream()
                .map(name -> this.referenceTargets.renderedReferenceName(relationship, name))
                .filter(name -> !name.isBlank())
                .toList();
    }

    private List<String> singleName(final String name) {
        List<String> names = List.of();
        if (!name.isBlank()) {
            names = List.of(name);
        }
        return names;
    }

    /** Names the target of one reference subsetting, using the proxy leaf when the target is unresolved. */
    private String directReferenceSubsettingTargetName(final Relationship relationship, final DanglingReferenceIndex danglingReferences,
            final Function<Element, String> resolvedName) {
        String name = this.referencedFeatureName(relationship, danglingReferences, resolvedName);
        if (relationship instanceof final ReferenceSubsetting referenceSubsetting) {
            final Element referencedFeature = StoredModel.value(referenceSubsetting, SysMLPackage.eINSTANCE.getReferenceSubsetting_ReferencedFeature());
            if (referencedFeature instanceof final InternalEObject proxy && proxy.eIsProxy() && name.isBlank()) {
                name = this.referenceTargets.proxyLeafName(proxy);
            } else if (referencedFeature != null) {
                name = this.preferredReferenceName(relationship, name, referencedFeature, resolvedName.apply(referencedFeature));
            }
        }
        return name;
    }

    /** Keeps the parsed spelling of a referenced feature when it still designates the target; else the resolved name. */
    private String preferredReferenceName(final Element context, final String parsed, final Element target, final String resolved) {
        String name = resolved;
        if (!parsed.isBlank()
                && this.referenceTargets.shouldUseParsedReferenceName(context, EmfReferenceNames.REFERENCED_FEATURE, parsed, target, resolved)) {
            name = parsed;
        }
        return name;
    }

    /** Returns the reference target text, falling back to the dotted text of the first owned feature chain. */
    private String referenceOrChainedFeatureName(final Relationship relationship, final DanglingReferenceIndex danglingReferences,
            final Function<Element, String> resolvedName, final String... referenceNames) {
        String name = this.referenceTargets.referenceName(relationship, danglingReferences, resolvedName, referenceNames);
        if (name.isBlank()) {
            name = this.nestedTargets.ownedFeatureChainText(relationship, danglingReferences);
        }
        return name;
    }

    /** Names each explicit relationship accepted by the filter, in model order, skipping those that produce no text. */
    private List<String> relationshipNames(final Element element, final Predicate<Relationship> relationshipFilter,
            final Function<Relationship, String> nameResolver) {
        return this.navigation.ownedRelationships(element).stream()
                .filter(relationshipFilter)
                .map(nameResolver)
                .filter(name -> !name.isBlank())
                .toList();
    }

    /** Groups the target names of each explicit relationship accepted by the filter, skipping empty groups. */
    private List<List<String>> relationshipNameGroups(final Element element, final Predicate<Relationship> relationshipFilter,
            final Function<Relationship, List<String>> namesResolver) {
        return this.navigation.ownedRelationships(element).stream()
                .filter(relationshipFilter)
                .map(namesResolver)
                .filter(names -> !names.isEmpty())
                .toList();
    }
}
