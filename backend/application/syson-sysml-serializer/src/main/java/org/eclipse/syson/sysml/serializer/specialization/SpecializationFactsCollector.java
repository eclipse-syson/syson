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
package org.eclipse.syson.sysml.serializer.specialization;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.expression.ExpressionRenderer;
import org.eclipse.syson.sysml.serializer.model.DanglingReferenceIndex;
import org.eclipse.syson.sysml.serializer.model.ElementSyntaxQueries;
import org.eclipse.syson.sysml.serializer.model.ModelFlags;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.model.MultiplicityText;
import org.eclipse.syson.sysml.serializer.model.SpecializationTargetNames;
import org.eclipse.syson.sysml.serializer.specialization.SpecializationFacts.ClauseKind;
import org.eclipse.syson.sysml.serializer.specialization.SpecializationFacts.SourceItem;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Definition;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.FeatureTyping;
import org.omg.sysml.lang.sysml.Redefinition;
import org.omg.sysml.lang.sysml.ReferenceSubsetting;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.Subclassification;
import org.omg.sysml.lang.sysml.Subsetting;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Builds the {@link SpecializationFacts} of an element: its specialization targets and multiplicity as source-ordered
 * items, and the multiplicity text to emit after deciding whether another construct already renders it.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
final class SpecializationFactsCollector {

    private final ExpressionRenderer expressions;

    private final ElementSyntaxQueries syntax;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ModelFlags flags;

    private final MultiplicityText multiplicities;

    private final SpecializationTargetNames specializationTargets;

    /**
     * Creates a collector reading model and parser data through the given context.
     *
     * @param expressions
     *            renders multiplicity bounds
     * @param syntax
     *            syntax queries used to detect bodies
     * @param context
     *            the serialization context
     */
    SpecializationFactsCollector(final ExpressionRenderer expressions,
            final ElementSyntaxQueries syntax,
            final SerializationContext context) {
        this.expressions = expressions;
        this.syntax = syntax;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.flags = context.modelQueries().flags();
        this.multiplicities = context.modelQueries().multiplicities();
        this.specializationTargets = context.modelQueries().specializationTargets();
    }

    /**
     * Collects the specialization facts of an element from its direct, non-implied owned relationships, in source
     * order. Implied relationships are skipped so that only what the user wrote is exported. Target names that cannot
     * be resolved and have no parser spelling are dropped. A multiplicity that no direct relationship owns gets a
     * trailing item.
     *
     * @param element
     *            the element whose declaration is being written
     * @return facts with immutable lists; never {@code null}
     */
    SpecializationFacts collect(final Element element) {
        final List<SourceItem> items = new ArrayList<>();
        element.getOwnedRelationship().stream()
                .filter(relationship -> !relationship.isImplied())
                .map(this::sourceItem)
                .filter(Objects::nonNull)
                .forEach(items::add);
        final boolean ownedMultiplicity = items.stream().anyMatch(item -> item.kind() == ClauseKind.MULTIPLICITY);
        final String rawMultiplicity = this.multiplicities.multiplicityText(element,
                this.expressions::expressionText);
        final String multiplicity = this.emittedMultiplicity(element, rawMultiplicity,
                ownedMultiplicity);
        if (!ownedMultiplicity && !multiplicity.isBlank()) {
            items.add(new SourceItem(ClauseKind.MULTIPLICITY, null, List.of()));
        }
        return new SpecializationFacts(List.copyOf(items), rawMultiplicity, multiplicity);
    }

    /**
     * Returns the multiplicity to emit in the specialization part, or an empty string when it is implied by the
     * context or written elsewhere (see {@link #shouldSuppressMultiplicity(Element, String)}), including a
     * multiplicity that no direct relationship owns while the element has a body that serializes it.
     */
    private String emittedMultiplicity(final Element element, final String rawMultiplicity, final boolean ownedMultiplicity) {
        if (rawMultiplicity.isBlank()
                || this.shouldSuppressMultiplicity(element, rawMultiplicity)
                || !ownedMultiplicity && this.syntax.hasSerializableBody(element)) {
            return "";
        }
        return rawMultiplicity;
    }

    /**
     * Classifies one explicit relationship as a specialization item with its rendered target names, or returns
     * {@code null} for a relationship that is not part of the specialization part or whose targets have no text.
     */
    private SourceItem sourceItem(final Relationship relationship) {
        final DanglingReferenceIndex danglingReferences = this.context.danglingReferenceIndex();
        final Function<Element, String> resolvedName = this.context.nameRenderer()::referenceName;
        final SourceItem item = switch (relationship) {
            case FeatureTyping typing -> item(ClauseKind.TYPING, typing,
                    List.of(this.specializationTargets.featureTypeName(typing, danglingReferences, resolvedName)));
            case Redefinition redefinition -> item(ClauseKind.REDEFINITION, redefinition,
                    this.specializationTargets.redefinedFeatureNamesForRelationship(redefinition, danglingReferences,
                            resolvedName));
            case ReferenceSubsetting reference -> item(ClauseKind.REFERENCE_SUBSETTING, reference,
                    List.of(this.specializationTargets.referencedFeatureName(reference, danglingReferences,
                            resolvedName)));
            case Subsetting subsetting when isCrossSubsetting(subsetting) -> item(ClauseKind.CROSS_SUBSETTING,
                    subsetting,
                    List.of(this.specializationTargets.crossedFeatureName(subsetting, danglingReferences,
                            resolvedName)));
            case Subsetting subsetting -> item(ClauseKind.SUBSETTING, subsetting,
                    this.specializationTargets.subsettedFeatureNamesForRelationship(subsetting, danglingReferences,
                            resolvedName));
            case Subclassification subclassification -> item(ClauseKind.SUBCLASSIFICATION, subclassification,
                    List.of(this.specializationTargets.supertypeName(subclassification, danglingReferences,
                            resolvedName)));
            default -> null;
        };
        if (item == null && this.ownsMultiplicity(relationship)) {
            return new SourceItem(ClauseKind.MULTIPLICITY, relationship, List.of());
        }
        return item;
    }

    /** Builds an item from its non-blank target names, or returns {@code null} when none is left. */
    private static SourceItem item(final ClauseKind kind, final Relationship relationship, final List<String> names) {
        final List<String> nonBlankNames = names.stream().filter(name -> !name.isBlank()).toList();
        if (nonBlankNames.isEmpty()) {
            return null;
        }
        return new SourceItem(kind, relationship, nonBlankNames);
    }

    /** Tells whether the subsetting is exactly a {@code CrossSubsetting}, written {@code =>} rather than {@code :>}. */
    private static boolean isCrossSubsetting(final Subsetting subsetting) {
        return subsetting.eClass() == SysMLPackage.eINSTANCE.getCrossSubsetting();
    }

    /**
     * Tells whether the relationship owns the element's multiplicity, which marks the multiplicity's source position.
     */
    private boolean ownsMultiplicity(final Relationship relationship) {
        for (final Element element : this.navigation.ownedRelatedElements(relationship)) {
            if (this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getMultiplicity())
                    || this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getMultiplicityRange())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tells whether the multiplicity is implied or rendered elsewhere: definitions and actions owned by a perform
     * action never write one; a calculation omits {@code [1]}; a port whose directed features share the multiplicity
     * does not repeat it. Array dimensions and subject multiplicities do not replace their owner's multiplicity.
     */
    private boolean shouldSuppressMultiplicity(final Element element, final String multiplicity) {
        final boolean suppressMultiplicity;
        if (element instanceof Definition || this.isPerformedAction(element)) {
            suppressMultiplicity = true;
        } else if (this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getCalculationUsage())) {
            suppressMultiplicity = SysMLTokens.UNIT_MULTIPLICITY.equals(multiplicity);
        } else {
            suppressMultiplicity = this.hasDirectedBodyFeatureWithSameMultiplicity(element, multiplicity);
        }
        return suppressMultiplicity;
    }

    /** Tells whether the element is an action usage owned by a {@code perform} action. */
    private boolean isPerformedAction(final Element element) {
        return this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getActionUsage())
                && this.navigation.isOwnedByMetaclass(element, SysMLPackage.eINSTANCE.getPerformActionUsage());
    }

    /** Tells whether the element is a port usage with a directed feature carrying the same multiplicity text. */
    private boolean hasDirectedBodyFeatureWithSameMultiplicity(final Element element, final String multiplicity) {
        return this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getPortUsage())
                && this.navigation.featureMembershipElements(element).stream()
                        .anyMatch(feature -> !this.flags.direction(feature).isBlank()
                                && multiplicity.equals(this.multiplicities.multiplicityText(feature,
                                        this.expressions::expressionText)));
    }
}
