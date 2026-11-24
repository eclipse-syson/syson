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

import java.util.stream.Collectors;

import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.parser.adapter.ParsedComment;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.SerializationMode;
import org.eclipse.syson.sysml.serializer.expression.ExpressionRenderer;
import org.eclipse.syson.sysml.serializer.model.ElementSyntaxQueries;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.model.ParserAdapterQueries;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.ConstructorExpression;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Expression;
import org.omg.sysml.lang.sysml.FeatureValue;
import org.omg.sysml.lang.sysml.NullExpression;
import org.omg.sysml.lang.sysml.TriggerInvocationExpression;

/**
 * Renders feature values, including their operator and expression wrapping.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class FeatureValueRenderer {

    private static final String ELEMENTS_FEATURE = "elements";

    private static final String OMITTED_EXPRESSION = "(//* ... */)";

    private final ExpressionRenderer expressions;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final ElementSyntaxQueries syntax;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ParserAdapterQueries adapters;

    /**
     * Creates rendering for feature-value operators and expressions, including grouping and incomplete values.
     *
     * @param expressions
     *            renders value expressions and trigger invocations
     * @param referenceWriter
     *            writes expression text with its reference occurrences
     * @param syntax
     *            identifies the expression and body forms permitted by the surrounding declaration
     * @param context
     *            supplies the run's output, options and parser-aware model queries
     */
    public FeatureValueRenderer(final ExpressionRenderer expressions,
            final ExpressionAndReferenceWriter referenceWriter,
            final ElementSyntaxQueries syntax,
            final SerializationContext context) {
        this.expressions = expressions;
        this.referenceWriter = referenceWriter;
        this.syntax = syntax;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.adapters = context.modelQueries().adapters();
    }

    /**
     * Writes the value clause of each feature value owned by {@code element}, in model order, each preceded by a space.
     */
    public void serializeFeatureValues(final Element element) {
        for (final FeatureValue value : this.navigation.featureValues(element)) {
            this.serializeFeatureValue(element, value);
        }
    }

    /**
     * Writes one value clause. Trigger invocations ({@code at}, {@code after}, {@code when}) carry their own keyword and are written
     * without an operator. A missing value expression still produces valid text: {@code := ()} for an assignment, otherwise
     * an omitted expression placeholder note, so the declaration stays parseable.
     */
    private void serializeFeatureValue(final Element element,
            final FeatureValue value) {
        final TextOutput output = this.context.output();
        final Expression valueExpression = this.navigation
                .featureValueExpression(value);
        if (valueExpression instanceof TriggerInvocationExpression) {
            final String trigger = this.expressions.expressionText(valueExpression);
            if (!trigger.isBlank()) {
                output.space();
                this.referenceWriter.writeExpression(output, valueExpression,
                        trigger);
            }
            return;
        }
        final String operator = this.featureValueOperator(value);
        String renderedExpression = "";
        if (valueExpression != null) {
            renderedExpression = this.expressions.expressionText(valueExpression);
        }
        String valueText = this.featureValueExpressionText(element, value,
                valueExpression, renderedExpression);
        if (valueText.isBlank()
                && SysMLTokens.ASSIGNMENT_OPERATOR.equals(operator)) {
            valueText = SysMLTokens.EMPTY_ARGUMENT_LIST;
        } else if (valueText.isBlank()) {
            valueText = OMITTED_EXPRESSION;
        }
        output.space().token(operator).space();
        if (valueExpression != null && valueText.equals(renderedExpression)) {
            this.referenceWriter.writeExpression(output, valueExpression,
                    renderedExpression);
        } else {
            this.referenceWriter.writeReferenceText(output, valueText, value);
        }
    }

    /**
     * Returns the value text with the wrapping the grammar or the source requires: {@code null} or {@code ()} for null expressions,
     * or parentheses when the source parenthesized the complete value. Expression bodies are rendered by the expression renderer.
     */
    private String featureValueExpressionText(final Element element,
            final FeatureValue value, final Expression expression,
            final String renderedExpression) {
        String text;
        if (expression instanceof NullExpression && this.adapters.hasTextAdapter(
                value, ITextAdapter.PARENTHESIZED_FEATURE_VALUE_EXPRESSION)) {
            final String comments = this.adapters.trailingComments(expression)
                    .stream().map(ParsedComment::text)
                    .collect(Collectors.joining());
            if (comments.isBlank()) {
                text = SysMLTokens.EMPTY_ARGUMENT_LIST;
            } else {
                text = SysMLTokens.OPEN_PARENTHESIS + comments
                        + SysMLTokens.CLOSE_PARENTHESIS;
            }
        } else if (expression instanceof NullExpression) {
            text = SysMLTokens.NULL_LITERAL;
        } else {
            text = renderedExpression;
            if (this.requiresParenthesizedFeatureValue(element, value, expression, text)) {
                text = SysMLTokens.OPEN_PARENTHESIS + text
                        + SysMLTokens.CLOSE_PARENTHESIS;
            }
        }
        return text;
    }

    /**
     * Keeps source parentheses around a value unless the text is already parenthesized. Constructor expressions keep
     * them only when the feature redefines {@code elements}.
     */
    private boolean requiresParenthesizedFeatureValue(final Element element,
            final FeatureValue value, final Expression expression,
            final String text) {
        if (!this.adapters.hasTextAdapter(value, ITextAdapter.PARENTHESIZED_FEATURE_VALUE_EXPRESSION)
                || expression == null || text.isBlank()
                || text.startsWith(SysMLTokens.OPEN_PARENTHESIS)) {
            return false;
        }
        return !(expression instanceof ConstructorExpression)
                || this.syntax.isRedefinedFeature(element, ELEMENTS_FEATURE);
    }

    /**
     * Returns the operator that introduces {@code value} ({@code =}, {@code :=}, {@code default}, {@code default =}, {@code default :=}). Round-trip
     * mode keeps the recorded {@code default} spelling; normalized mode derives it from the {@code isDefault} and {@code isInitial}
     * flags.
     */
    public String featureValueOperator(final FeatureValue value) {
        // The generic default marker also accompanies explicit = and :=; only the bare spelling is ambiguous.
        final boolean bareDefaultInSource = this.adapters.hasTextAdapter(value, ITextAdapter.FEATURE_VALUE_DEFAULT)
                && !this.adapters.hasTextAdapter(value, ITextAdapter.FEATURE_VALUE_DEFAULT_EQUALS)
                && !this.adapters.hasTextAdapter(value, ITextAdapter.FEATURE_VALUE_DEFAULT_INITIAL);
        if (this.context.options().mode() == SerializationMode.ROUND_TRIP && value.isDefault() && !value.isInitial() && bareDefaultInSource) {
            return SysMLTokens.DEFAULT_KEYWORD;
        }
        return this.normalizedFeatureValueOperator(value);
    }

    /** Derives the canonical operator from the model flags, using the explicit {@code default =} form for default values. */
    private String normalizedFeatureValueOperator(final FeatureValue value) {
        String operator = SysMLTokens.EQUALS_OPERATOR;
        if (value.isDefault() && value.isInitial()) {
            operator = SysMLTokens.DEFAULT_ASSIGNMENT;
        } else if (value.isDefault()) {
            operator = SysMLTokens.DEFAULT_EQUALS;
        } else if (value.isInitial()) {
            operator = SysMLTokens.ASSIGNMENT_OPERATOR;
        }
        return operator;
    }
}
