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

import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Expression;
import org.omg.sysml.lang.sysml.Multiplicity;
import org.omg.sysml.lang.sysml.MultiplicityRange;
import org.omg.sysml.lang.sysml.ReferenceUsage;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Builds the bracketed multiplicity text of an element, following the {@code multiplicityRange} grammar rule:
 * {@code [n]}, {@code [l..u]} or {@code [*]}.
 * <p>
 * Look here when a multiplicity is missing, collapsed or expanded wrongly (e.g. {@code [0..*]} versus {@code [*]},
 * {@code [1]} versus {@code [1..1]}), or found on the wrong element. Bound expressions are rendered by the caller's
 * expression renderer.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class MultiplicityText {

    private static final String ZERO_LOWER_BOUND = "0";

    private final ModelNavigation navigation;

    /**
     * Creates the multiplicity text builder.
     *
     * @param navigation
     *            finds the multiplicity owned by an element and the bounds owned by a range
     */
    public MultiplicityText(final ModelNavigation navigation) {
        this.navigation = Objects.requireNonNull(navigation);
    }

    /**
     * Returns the multiplicity text of an element, or of the range itself when given one. An element without its own
     * multiplicity may borrow the one of an anonymous cross feature owned through a plain owning membership.
     *
     * @param element
     *            a multiplicity range or an element that owns one
     * @param expressionText
     *            renders a bound expression
     * @return the bracketed text, or an empty string when there is no upper bound
     */
    public String multiplicityText(final Element element, final Function<Expression, String> expressionText) {
        Multiplicity multiplicity;
        if (element instanceof final MultiplicityRange range) {
            multiplicity = range;
        } else {
            multiplicity = this.ownedMultiplicity(element);
        }
        String text = "";
        if (multiplicity instanceof final MultiplicityRange range) {
            text = this.multiplicityRangeText(range, expressionText);
        }
        return text;
    }

    /**
     * Returns the multiplicity of the cross feature owned through a plain owning membership, as specified by
     * {@code ownedCrossFeatureMember} and {@code ownedCrossMultiplicityMember}. Body members and their descendants
     * cannot supply it. Null gives an empty string.
     */
    public String crossFeatureMultiplicityText(final Element element, final Function<Expression, String> expressionText) {
        return this.navigation.ownedRelationships(element).stream()
                .filter(relationship -> this.navigation.isMetaclass(relationship, SysMLPackage.eINSTANCE.getOwningMembership()))
                .flatMap(relationship -> this.navigation.ownedRelatedElements(relationship).stream())
                .map(feature -> this.multiplicityText(feature, expressionText))
                .filter(text -> !text.isBlank()).findFirst().orElse("");
    }

    /**
     * Tells whether the element declares its multiplicity before its specializations, as in the payload {@code [1] T},
     * so that the source order of the two can be written back.
     */
    public boolean declaresMultiplicityFirst(final Element element) {
        return this.navigation.ownedRelationships(element).stream()
                .filter(relationship -> !relationship.isImplied())
                .findFirst()
                .filter(first -> this.navigation.ownedRelatedElements(first).stream().anyMatch(Multiplicity.class::isInstance))
                .isPresent();
    }

    /**
     * Returns the element's own multiplicity, else that of its anonymous inline cross feature. A body member cannot
     * supply the enclosing declaration's multiplicity. Both are looked for in a single pass over owned relationships.
     */
    private Multiplicity ownedMultiplicity(final Element element) {
        Multiplicity anonymousReferenceMultiplicity = null;
        for (final Relationship relationship : this.navigation.ownedRelationships(element)) {
            for (final Element related : this.navigation.ownedRelatedElements(relationship)) {
                if (related instanceof final Multiplicity multiplicity) {
                    return multiplicity;
                }
                if (anonymousReferenceMultiplicity == null
                        && this.navigation.isMetaclass(relationship, SysMLPackage.eINSTANCE.getOwningMembership())
                        && this.isAnonymousReferenceUsage(related)) {
                    anonymousReferenceMultiplicity = this.navigation.firstOwnedRelatedElement(related, Multiplicity.class);
                }
            }
        }
        return anonymousReferenceMultiplicity;
    }

    private boolean isAnonymousReferenceUsage(final Element element) {
        return element instanceof ReferenceUsage && (element.getDeclaredName() == null || element.getDeclaredName().isBlank());
    }

    /**
     * Renders a range from its owned bound expressions, which reflect the source, before the derived bound getters. A
     * single owned {@code *} bound with derived lower {@code 0} stays {@code [*]}; a single bound, or a missing lower
     * one, is written alone. Two owned bounds are always written {@code [l..u]}, even when equal.
     */
    private String multiplicityRangeText(final MultiplicityRange range, final Function<Expression, String> expressionText) {
        final List<Expression> ownedBounds = this.ownedBounds(range);
        Expression lower = SafeEmfAccess.get(range::getLowerBound, null);
        if (ownedBounds.size() > 1) {
            lower = ownedBounds.get(0);
        }
        Expression upper = SafeEmfAccess.get(range::getUpperBound, null);
        if (!ownedBounds.isEmpty()) {
            upper = ownedBounds.get(ownedBounds.size() - 1);
        }
        return this.bracketedRange(ownedBounds.size(), this.boundText(lower, expressionText), this.boundText(upper, expressionText));
    }

    private String bracketedRange(final int ownedBoundCount, final String lowerText, final String upperText) {
        String text;
        if (upperText.isBlank()) {
            text = "";
        } else if (ownedBoundCount == 1 && ZERO_LOWER_BOUND.equals(lowerText) && SysMLTokens.INFINITY_LITERAL.equals(upperText)) {
            text = SysMLTokens.UNBOUNDED_MULTIPLICITY;
        } else if (lowerText.isBlank() || ownedBoundCount <= 1 && lowerText.equals(upperText)) {
            text = SysMLTokens.OPEN_BRACKET + upperText + SysMLTokens.CLOSE_BRACKET;
        } else {
            text = SysMLTokens.OPEN_BRACKET + lowerText + SysMLTokens.RANGE_OPERATOR + upperText + SysMLTokens.CLOSE_BRACKET;
        }
        return text;
    }

    private String boundText(final Expression bound, final Function<Expression, String> expressionText) {
        String text = "";
        if (bound != null) {
            text = expressionText.apply(bound);
        }
        return text;
    }

    private List<Expression> ownedBounds(final MultiplicityRange range) {
        return this.navigation.ownedRelationships(range).stream()
                .flatMap(relationship -> this.navigation.ownedRelatedElements(relationship).stream())
                .filter(Expression.class::isInstance)
                .map(Expression.class::cast)
                .toList();
    }
}
