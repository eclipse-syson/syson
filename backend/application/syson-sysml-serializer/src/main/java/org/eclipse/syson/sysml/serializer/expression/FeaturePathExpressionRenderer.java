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
package org.eclipse.syson.sysml.serializer.expression;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.model.NestedTargetSearch;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Expression;
import org.omg.sysml.lang.sysml.FeatureChainExpression;
import org.omg.sysml.lang.sysml.FeatureReferenceExpression;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Renders feature-reference expressions and {@code source.feature} feature chains as dotted or qualified paths.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
final class FeaturePathExpressionRenderer {

    private final ExpressionRenderer expressionRenderer;

    private final ReferenceResolver referenceResolver;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final NestedTargetSearch nestedTargets;

    private final ExpressionReferenceCollector referenceCollector;

    /**
     * Creates a path renderer sharing the run's expression renderer and reference collector.
     *
     * @param expressionRenderer
     *            renders chain sources and nested expressions
     * @param referenceResolver
     *            renders reference names
     * @param context
     *            the run's context
     * @param referenceCollector
     *            updates parser spellings made stale by resolved references
     */
    FeaturePathExpressionRenderer(final ExpressionRenderer expressionRenderer,
            final ReferenceResolver referenceResolver,
            final SerializationContext context,
            final ExpressionReferenceCollector referenceCollector) {
        this.expressionRenderer = expressionRenderer;
        this.referenceResolver = referenceResolver;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.nestedTargets = context.modelQueries().nestedTargets();
        this.referenceCollector = referenceCollector;
    }

    /**
     * Writes the feature a feature reference expression refers to, with stale parser spellings updated. When no path
     * can be named, the expression is a parser wrapper around another expression, which is rendered instead.
     *
     * @param referenceExpression
     *            the expression
     * @return the rendered reference, possibly empty
     */
    String renderFeatureReference(
            final FeatureReferenceExpression referenceExpression) {
        final Expression wrapped = this.navigation.firstOwnedExpression(referenceExpression);
        String text;
        if (wrapped != null && !this.navigation.isMetaclass(wrapped, SysMLPackage.eINSTANCE.getExpression())) {
            text = this.expressionRenderer.expressionText(wrapped);
        } else {
            text = this.referenceCollector.updatedText(
                    referenceExpression,
                    this.referenceResolver.endpointPath(referenceExpression));
            if (text.isBlank()) {
                text = this.renderNestedReferenceExpression(referenceExpression);
            } else {
                text = this.expressionRenderer.referenceLeadingCommentText(referenceExpression) + text;
            }
        }
        return text;
    }

    private String renderNestedReferenceExpression(
            final FeatureReferenceExpression referenceExpression) {
        final Expression wrapped = this.navigation.firstOwnedExpression(referenceExpression);
        String text = "";
        if (wrapped != null && wrapped != referenceExpression) {
            text = this.expressionRenderer.expressionText(wrapped);
        }
        return text;
    }

    /**
     * Writes {@code source.feature...}. The source is grouped when needed ({@code (x as T).f}, {@code (a + b).f}) and
     * a constructor value replaces it. The chained names come from the model, then from parser spellings, without
     * repeating names the source already spells.
     *
     * @param chainExpression
     *            the feature chain expression
     * @return the rendered path
     */
    String renderFeatureChain(final FeatureChainExpression chainExpression) {
        final List<Expression> operands = this.navigation.operands(chainExpression);
        String source = "";
        String constructorSource = "";
        if (!operands.isEmpty()) {
            source = this.expressionRenderer.expressionText(operands.get(0),
                    "", false);
            constructorSource = this.expressionRenderer
                    .constructorFeatureValueSource(operands.get(0));
        }
        if (!constructorSource.isBlank()) {
            source = constructorSource;
        } else if (!operands.isEmpty() && this.mustGroupChainSource(operands.get(0), source)) {
            source = ExpressionPathSupport.parenthesized(source);
        }

        List<String> chainedFeatureNames = this.nestedTargets.chainedFeatureNames(
                chainExpression, this.context.danglingReferenceIndex());
        final String directInvocationPath = this.renderDirectInvocationPath(
                chainExpression, operands, source, chainedFeatureNames);
        if (directInvocationPath.isBlank() && chainedFeatureNames.isEmpty()) {
            final String member = this.nestedTargets.chainMemberSegment(chainExpression, this.context.danglingReferenceIndex());
            if (!member.isBlank()) {
                chainedFeatureNames = List.of(member);
            }
        }

        return this.featureChainText(source, constructorSource,
                chainedFeatureNames, directInvocationPath, this.expressionRenderer.referenceLeadingCommentText(chainExpression));
    }

    /**
     * Tells whether the source of a feature chain must be grouped. Besides prefix and infix forms, a source that is
     * itself a feature chain expression needs parentheses: grammar rule {@code primaryExpression} allows one
     * {@code '.' featureChainMember} per postfix step and folds {@code x.ab.c} into a single chain {@code ab.c} on
     * {@code x}, so only {@code (x.ab).c} keeps {@code x.ab} as the source.
     */
    private boolean mustGroupChainSource(final Expression source, final String sourceText) {
        return OperatorPrecedence.mustParenthesizePostfixSource(source, sourceText)
                || source instanceof FeatureChainExpression
                        && !ExpressionPathSupport.isEnclosedInParentheses(sourceText);
    }

    /** Writes {@code f(x).g} when the source is a plain invocation and the chained feature is held by a membership. */
    private String renderDirectInvocationPath(
            final FeatureChainExpression chainExpression,
            final List<Expression> operands, final String source,
            final List<String> chainedFeatureNames) {
        String path = "";
        if (chainedFeatureNames.isEmpty() && !operands.isEmpty()
                && this.navigation.isMetaclass(operands.get(0),
                        SysMLPackage.eINSTANCE.getInvocationExpression())) {
            final String chainedFeature = this.nestedTargets.chainMemberSegment(chainExpression,
                    this.context.danglingReferenceIndex());
            if (!source.isBlank() && !chainedFeature.isBlank()) {
                path = source + SysMLTokens.PATH_SEPARATOR
                        + this.expressionRenderer.referenceLeadingCommentText(chainExpression)
                        + this.referenceResolver.referenceName(chainedFeature);
            }
        }
        return path;
    }

    private String featureChainText(final String source,
            final String constructorSource,
            final List<String> chainedFeatureNames,
            final String directInvocationPath, final String leadingComments) {
        String text;
        if (!directInvocationPath.isBlank()) {
            text = directInvocationPath;
        } else if (!constructorSource.isBlank()) {
            text = source + SysMLTokens.PATH_SEPARATOR
                    + leadingComments
                    + chainedFeatureNames.stream()
                            .map(this.referenceResolver::referenceName)
                            .collect(Collectors
                                    .joining(SysMLTokens.PATH_SEPARATOR));
        } else {
            text = this.joinFeaturePath(source, chainedFeatureNames, leadingComments);
        }
        return text;
    }

    /**
     * Joins rendered path parts, preserving every navigation step even when adjacent names are equal.
     *
     * @param source
     *            the rendered chain source, possibly blank
     * @param chainedFeatureNames
     *            the raw names that follow it
     * @param leadingComments
     *            comments written before the chained names
     * @return the dotted path
     */
    String joinFeaturePath(final String source,
            final List<String> chainedFeatureNames, final String leadingComments) {
        final StringJoiner renderedPath = new StringJoiner(
                SysMLTokens.PATH_SEPARATOR);
        if (!source.isBlank()) {
            renderedPath.add(source);
        }
        String comments = leadingComments;
        for (final String name : chainedFeatureNames) {
            if (name.isBlank()) {
                continue;
            }
            renderedPath.add(comments + this.renderFeaturePathPart(name));
            comments = "";
        }
        return renderedPath.toString();
    }

    private String renderFeaturePathPart(final String pathPart) {
        if (this.isGroupedCastFeaturePath(pathPart)) {
            return pathPart;
        }
        return this.referenceResolver.referenceName(pathPart);
    }

    /** Tells whether the text starts with a parenthesized {@code as} cast, which must be kept verbatim. */
    private boolean isGroupedCastFeaturePath(final String source) {
        final int closingParenthesis = ExpressionPathSupport.matchingClosingParenthesis(source, 0);
        if (!source.startsWith(SysMLTokens.OPEN_PARENTHESIS) || closingParenthesis <= 0) {
            return false;
        }
        final String grouped = source.substring(1, closingParenthesis);
        return grouped.startsWith(SysMLTokens.AS_PREFIX) || grouped.contains(SysMLTokens.AS_INFIX);
    }
}
