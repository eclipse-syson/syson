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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.serializer.model.ParserAdapterQueries;
import org.eclipse.syson.sysml.serializer.syntax.SysMLTextSyntax;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Expression;
import org.omg.sysml.lang.sysml.LiteralBoolean;
import org.omg.sysml.lang.sysml.LiteralInfinity;
import org.omg.sysml.lang.sysml.LiteralInteger;
import org.omg.sysml.lang.sysml.LiteralRational;
import org.omg.sysml.lang.sysml.LiteralString;
import org.omg.sysml.lang.sysml.NullExpression;

/**
 * Renders the {@code literalExpression} and {@code nullExpression} forms: booleans, strings, integers, reals,
 * {@code *} and {@code null}.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
final class LiteralExpressionRenderer {

    private static final String NULL_METACLASS_NAME = "LiteralNull";

    /** A real literal above {@link Double#MAX_VALUE}, which {@link Double#valueOf(String)} reads as positive infinity. */
    private static final String OVERFLOWING_REAL = "1.0E309";

    private static final String ZERO_REAL = "0.0";

    private final ParserAdapterQueries adapters;

    private final boolean preserveNumericFormat;

    /**
     * Creates a literal renderer.
     *
     * @param adapters
     *            access to spellings recorded by the parser
     * @param preserveNumericFormat
     *            whether numbers keep their source spelling ({@code 1e3}, {@code 0.50}); enabled in round-trip mode
     */
    LiteralExpressionRenderer(final ParserAdapterQueries adapters,
            final boolean preserveNumericFormat) {
        this.adapters = adapters;
        this.preserveNumericFormat = preserveNumericFormat;
    }

    /**
     * Tells whether the expression is a literal this renderer writes, including {@code null} from older models.
     *
     * @param expression
     *            the expression, not {@code null}
     * @return {@code true} for literal and null expressions
     */
    boolean supports(final Expression expression) {
        final boolean numericLiteral = expression instanceof LiteralInteger
                || expression instanceof LiteralInfinity
                || expression instanceof LiteralRational;
        final boolean scalarLiteral = expression instanceof LiteralBoolean
                || expression instanceof LiteralString
                || expression instanceof NullExpression;
        return numericLiteral || scalarLiteral
                || this.isLegacyLiteralNull(expression);
    }

    /**
     * Writes a literal. Strings always keep their source spelling when known, since escapes cannot be recovered from
     * the value; numbers keep it only when numeric format is preserved.
     *
     * @param expression
     *            an expression accepted by {@link #supports(Expression)}
     * @return the literal text; the SysML {@code null} literal for null expressions
     */
    String render(final Expression expression) {
        String text = SysMLTokens.NULL_LITERAL;
        if (expression instanceof final LiteralInteger literal) {
            text = this.renderInteger(literal);
        } else if (expression instanceof LiteralInfinity) {
            text = SysMLTokens.INFINITY_LITERAL;
        } else if (expression instanceof final LiteralRational literal) {
            text = this.renderRational(literal);
        } else if (expression instanceof final LiteralBoolean literal) {
            text = Boolean.toString(literal.isValue());
        } else if (expression instanceof final LiteralString literal) {
            text = this.renderString(literal);
        }
        return text;
    }

    private String renderString(final LiteralString literalString) {
        final String sourceSpelling = this.adapters.stringLiteralFormat(literalString);
        if (sourceSpelling.isBlank()) {
            return SysMLTextSyntax.quote(literalString.getValue());
        }
        return sourceSpelling;
    }

    private String renderInteger(final LiteralInteger literalInteger) {
        // Its positive magnitude cannot be parsed as an integer; both operands of this difference can.
        if (literalInteger.getValue() == Integer.MIN_VALUE) {
            return "(-2147483647 - 1)";
        }
        return this.numericText(literalInteger, Integer.toString(literalInteger.getValue()));
    }

    private String renderRational(final LiteralRational literalRational) {
        return this.numericText(literalRational, realValueText(literalRational.getValue()));
    }

    /**
     * Writes a real value so that it reparses to the same value. {@link Double#toString(double)} is a valid
     * {@code realValue} for finite numbers, but gives {@code Infinity} and {@code NaN} for the others, which would
     * reparse as names. An infinite value is written as a real literal that overflows to infinity when read back
     * ({@code 1.0E309}, negated for negative infinity, like any negative number); NaN has no literal and is written
     * as the grouped division {@code (0.0 / 0.0)}, whose value is NaN.
     */
    private static String realValueText(final double value) {
        final String text;
        if (Double.isNaN(value)) {
            text = SysMLTokens.OPEN_PARENTHESIS + ZERO_REAL + SysMLTokens.SPACE + SysMLTokens.DIVISION_OPERATOR
                    + SysMLTokens.SPACE + ZERO_REAL + SysMLTokens.CLOSE_PARENTHESIS;
        } else if (value == Double.POSITIVE_INFINITY) {
            text = OVERFLOWING_REAL;
        } else if (value == Double.NEGATIVE_INFINITY) {
            text = SysMLTokens.MINUS_OPERATOR + OVERFLOWING_REAL;
        } else {
            text = Double.toString(value);
        }
        return text;
    }

    /** Returns the source spelling of a number when numeric format is preserved and known, else its value text. */
    private String numericText(final EObject literal, final String valueText) {
        String sourceSpelling = "";
        if (this.preserveNumericFormat) {
            sourceSpelling = this.adapters.numericLiteralFormat(literal);
        }
        if (sourceSpelling.isBlank()) {
            return valueText;
        }
        return sourceSpelling;
    }

    private boolean isLegacyLiteralNull(final Expression expression) {
        // LiteralNull belonged to an older metamodel and has no generated Java type.
        return NULL_METACLASS_NAME.equals(expression.eClass().getName());
    }
}
