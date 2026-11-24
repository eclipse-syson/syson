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
package org.eclipse.syson.sysml.serializer.rendering.actions;

import java.util.List;

import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Expression;

/**
 * Shared helper of the action renderers for parameters whose value is an expression (assignment values, send payloads,
 * loop bounds, terminate targets). It finds the expression behind a parameter and writes its text so that HTML output
 * keeps the link to that expression whenever the text is the expression's own rendering.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
final class ActionExpressionSupport {

    private final ExpressionAndReferenceWriter referenceWriter;

    private final ModelNavigation navigation;

    /**
     * Creates the helper.
     *
     * @param referenceWriter
     *            writes expressions and reference text with traceability metadata
     * @param navigation
     *            containment navigation used to reach feature values
     */
    ActionExpressionSupport(final ExpressionAndReferenceWriter referenceWriter,
            final ModelNavigation navigation) {
        this.referenceWriter = referenceWriter;
        this.navigation = navigation;
    }

    /**
     * Returns the expression carried by a parameter: the parameter itself when it is an expression, otherwise the
     * expression of its first feature value.
     *
     * @param parameters
     *            the action's parameters, in order
     * @param index
     *            the parameter position
     * @return an existing model expression, or {@code null} for an out-of-range index or a parameter without value
     */
    Expression parameterExpression(final List<Element> parameters,
            final int index) {
        if (index < 0 || index >= parameters.size()) {
            return null;
        }
        final Expression expression;
        if (parameters.get(index) instanceof final Expression parameterExpression) {
            expression = parameterExpression;
        } else {
            expression = this.navigation.featureValues(parameters.get(index)).stream()
                    .map(this.navigation::featureValueExpression).findFirst()
                    .orElse(null);
        }
        return expression;
    }

    /**
     * Writes {@code text} traced to {@code expression} when there is one, otherwise as plain text.
     *
     * @param output
     *            the output to write to
     * @param text
     *            the text to write
     * @param expression
     *            the expression the text was produced from, or {@code null}
     */
    void writeParameterText(final TextOutput output, final String text,
            final Expression expression) {
        this.writeParameterText(output, text, expression, null);
    }

    /**
     * Writes {@code text} with the most precise traceability available. When the text is exactly the expression's own
     * rendering, it is written as that expression (so HTML output links each nested reference); when it was rewritten
     * by the caller, it is traced to the expression as a whole; without an expression it is traced to
     * {@code contextElement}, or written untraced.
     *
     * @param output
     *            the output to write to
     * @param text
     *            the text to write
     * @param expression
     *            the expression the text was produced from, or {@code null}
     * @param contextElement
     *            the fallback element for traceability, or {@code null}
     */
    void writeParameterText(final TextOutput output, final String text,
            final Expression expression, final Element contextElement) {
        if (expression != null
                && text.equals(this.referenceWriter.expressionText(expression))) {
            this.referenceWriter.writeExpression(output, expression, text);
        } else if (expression != null) {
            this.referenceWriter.writeReferenceText(output, text, expression);
        } else if (contextElement != null) {
            this.referenceWriter.writeReferenceText(output, text, contextElement);
        } else {
            output.token(text);
        }
    }

    /**
     * Returns the text of the first parameter of {@code element} that has a non-blank value expression, as used for the
     * {@code for} loop sequence or {@code terminate} occurrence.
     *
     * @param element
     *            the action whose parameters are searched
     * @return the expression text, or an empty string when no parameter has a value
     */
    String firstParameterExpressionText(final Element element) {
        return this.navigation.parameterElements(element).stream()
                .map(this::expressionValueText).filter(text -> !text.isBlank())
                .findFirst().orElse("");
    }

    /**
     * Returns the text of the first non-blank feature value expression of {@code element}, as in {@code send x via p}.
     *
     * @param element
     *            the feature whose value is read
     * @return the expression text, or an empty string when it has no value
     */
    String expressionValueText(final Element element) {
        return this.navigation.featureValues(element).stream()
                .map(this.navigation::featureValueExpression)
                .map(this.referenceWriter::expressionText)
                .filter(text -> !text.isBlank()).findFirst().orElse("");
    }
}
