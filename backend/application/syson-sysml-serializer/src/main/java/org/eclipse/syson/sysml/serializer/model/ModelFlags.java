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

import java.util.Objects;

import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Definition;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Expression;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.Invariant;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.OccurrenceDefinition;
import org.omg.sysml.lang.sysml.OccurrenceUsage;
import org.omg.sysml.lang.sysml.ReturnParameterMembership;
import org.omg.sysml.lang.sysml.StateDefinition;
import org.omg.sysml.lang.sysml.StateUsage;
import org.omg.sysml.lang.sysml.Type;
import org.omg.sysml.lang.sysml.Usage;
import org.omg.sysml.lang.sysml.VisibilityKind;

/**
 * Guarded reads of the model attributes that become declaration keywords and prefixes: visibility, {@code abstract},
 * {@code variation}, {@code ordered}/{@code nonunique}, {@code derived}, {@code constant}, {@code end},
 * {@code individual}, portion kind, direction and membership names.
 * <p>
 * Look here when a keyword is emitted or dropped although the model attribute says otherwise; the order of the
 * keywords is decided by the renderers. Flag reads are guarded: a failing derived getter yields the attribute's
 * default instead of aborting serialization.
 * </p>
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class ModelFlags {

    private static final String EXPRESSION_METACLASS_SUFFIX = "Expression";

    private static final String LITERAL_METACLASS_PREFIX = "Literal";

    private final boolean preserveParsedSyntax;

    private final ParserAdapterQueries adapters;

    /**
     * Creates the flag queries.
     *
     * @param preserveParsedSyntax
     *            {@code true} in round-trip mode, where a return direction omitted in the source stays omitted
     * @param adapters
     *            reads the parser markers that record such omissions
     */
    public ModelFlags(final boolean preserveParsedSyntax, final ParserAdapterQueries adapters) {
        this.preserveParsedSyntax = preserveParsedSyntax;
        this.adapters = Objects.requireNonNull(adapters);
    }

    /** Tells whether a membership has {@code private} visibility, which must be written explicitly. */
    public boolean isPrivate(final Membership membership) {
        return membership != null && membership.getVisibility() == VisibilityKind.PRIVATE;
    }

    /** Tells whether a membership has {@code protected} visibility, which must be written explicitly. */
    public boolean isProtected(final Membership membership) {
        return membership != null && membership.getVisibility() == VisibilityKind.PROTECTED;
    }

    /** Tells whether a feature needs the {@code nonunique} multiplicity property; non-features are never non-unique. */
    public boolean isNonUnique(final Element element) {
        return element instanceof final Feature feature && !SafeEmfAccess.get(feature::isUnique, true);
    }

    /** Tells whether a feature needs the {@code derived} prefix. */
    public boolean isDerived(final Element element) {
        return element instanceof final Feature feature && SafeEmfAccess.get(feature::isDerived, false);
    }

    /** Tells whether a feature needs the {@code constant} prefix. */
    public boolean isConstant(final Element element) {
        return element instanceof final Feature feature && SafeEmfAccess.get(feature::isConstant, false);
    }

    /** Tells whether a feature needs the {@code end} prefix. */
    public boolean isEnd(final Element element) {
        return element instanceof final Feature feature && SafeEmfAccess.get(feature::isEnd, false);
    }

    /** Tells whether a feature is composite; non-composite usages may need an explicit {@code ref} prefix. */
    public boolean isComposite(final Element element) {
        return element instanceof final Feature feature && SafeEmfAccess.get(feature::isComposite, false);
    }

    /** Tells whether a retained explicit ref prefix still agrees with the feature's composite flag. */
    public boolean hasExplicitRefPrefix(final Element element) {
        return this.adapters.hasTextAdapter(element, ITextAdapter.EXPLICIT_REF_PREFIX) && !this.isComposite(element);
    }

    /** Tells whether a type needs the {@code abstract} prefix. */
    public boolean isAbstract(final Element element) {
        return element instanceof final Type type && SafeEmfAccess.get(type::isAbstract, false);
    }

    /** Tells whether a definition or usage needs the {@code variation} prefix. */
    public boolean isVariation(final Element element) {
        boolean variation = false;
        if (element instanceof final Definition definition) {
            variation = SafeEmfAccess.get(definition::isVariation, false);
        } else if (element instanceof final Usage usage) {
            variation = SafeEmfAccess.get(usage::isVariation, false);
        }
        return variation;
    }

    /** Tells whether an invariant is negated, which is written {@code inv false}. */
    public boolean isNegated(final Element element) {
        return element instanceof final Invariant invariant && SafeEmfAccess.get(invariant::isNegated, false);
    }

    /** Tells whether a feature needs the {@code ordered} multiplicity property. */
    public boolean isOrdered(final Element element) {
        return element instanceof final Feature feature && SafeEmfAccess.get(feature::isOrdered, false);
    }

    /** Tells whether a state definition or usage needs the {@code parallel} keyword. */
    public boolean isParallel(final Element element) {
        boolean parallel = false;
        if (element instanceof final StateDefinition definition) {
            parallel = SafeEmfAccess.get(definition::isParallel, false);
        } else if (element instanceof final StateUsage usage) {
            parallel = SafeEmfAccess.get(usage::isParallel, false);
        }
        return parallel;
    }

    /**
     * Tells whether an expression is a syntax expression (operator, invocation, literal, ...) rather than a semantic
     * usage that merely implements {@link Expression}, such as a calculation usage, which keeps its declaration syntax.
     * The metaclass naming convention is the boundary between both families.
     */
    public boolean isExpressionSyntaxElement(final Expression expression) {
        if (expression == null) {
            return false;
        }
        final String metaclassName = expression.eClass().getName();
        return metaclassName.endsWith(EXPRESSION_METACLASS_SUFFIX) || metaclassName.startsWith(LITERAL_METACLASS_PREFIX);
    }

    /** Tells whether an occurrence definition or usage needs the {@code individual} prefix. */
    public boolean isIndividual(final Element element) {
        boolean individual = false;
        if (element instanceof final OccurrenceDefinition definition) {
            individual = SafeEmfAccess.get(definition::isIndividual, false);
        } else if (element instanceof final OccurrenceUsage usage) {
            individual = SafeEmfAccess.get(usage::isIndividual, false);
        }
        return individual;
    }

    /** Returns the {@code snapshot}/{@code timeslice} keyword of an occurrence usage, or an empty string. */
    public String portionKind(final Element element) {
        String portionKind = "";
        if (element instanceof final OccurrenceUsage usage && usage.getPortionKind() != null) {
            portionKind = usage.getPortionKind().getName();
        }
        return portionKind;
    }

    /**
     * Returns the direction keyword of a feature ({@code in}, {@code out}, {@code inout}), or an empty string. In
     * round-trip mode, an {@code out} that the source left implicit on a return parameter stays omitted.
     */
    public String direction(final Element element) {
        String direction = "";
        if (element instanceof final Feature feature) {
            direction = SafeEmfAccess.get(() -> this.writtenDirection(feature), "");
        }
        return direction;
    }

    /** Returns the derived member name of a membership (its alias name for aliases), or an empty string. */
    public String memberName(final Membership membership) {
        return Objects.toString(StoredModel.memberName(membership), "");
    }

    /** Returns the derived member short name of a membership ({@code <id>} of an alias), or an empty string. */
    public String memberShortName(final Membership membership) {
        return Objects.toString(StoredModel.memberShortName(membership), "");
    }

    /**
     * Returns the direction keyword written for a feature. A return parameter written without direction gets
     * {@code out} from structural completion, recorded by the parser's omission marker; that {@code out} is left
     * implicit only while the feature still is a return parameter, so a feature moved elsewhere keeps its direction.
     */
    private String writtenDirection(final Feature feature) {
        String direction = "";
        if (feature.getDirection() != null) {
            direction = feature.getDirection().getName();
        }
        if (this.preserveParsedSyntax && SysMLTokens.OUT_KEYWORD.equals(direction)
                && this.adapters.hasTextAdapter(feature, ITextAdapter.RETURN_DIRECTION_OMITTED)
                && feature.eContainer() instanceof ReturnParameterMembership) {
            direction = "";
        }
        return direction;
    }
}
