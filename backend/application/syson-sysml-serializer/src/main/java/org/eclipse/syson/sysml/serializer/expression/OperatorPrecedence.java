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

import java.util.Map;
import java.util.Set;

import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Expression;
import org.omg.sysml.lang.sysml.LiteralInteger;
import org.omg.sysml.lang.sysml.LiteralRational;
import org.omg.sysml.lang.sysml.OperatorExpression;

/**
 * Encodes the operator precedence and associativity of {@code KerMLExpressions.g4} and decides where operands must be
 * grouped so that the rendered text parses back into the same operator tree.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
final class OperatorPrecedence {

    private static final int NULL_COALESCING = -5;

    private static final int IMPLIES = -4;

    private static final int OR = -3;

    private static final int XOR = -2;

    private static final int AND = -1;

    private static final int EQUALITY = 0;

    private static final int CLASSIFICATION = 1;

    private static final int RELATIONAL = 2;

    private static final int RANGE = 3;

    private static final int ADDITIVE = 4;

    private static final int MULTIPLICATIVE = 5;

    private static final int EXPONENTIATION = 6;

    private static final int UNARY_NOT = 7;

    // Levels written "operand (operator operand)?" in the grammar: an operand of the same level is grouped on both sides.
    private static final Set<Integer> NON_ASSOCIATIVE_LEVELS = Set.of(CLASSIFICATION, RANGE);

    // Grammar rule nesting, loosest first; higher values bind more tightly. Binary '+' and '-' share their token with
    // the unary forms, which OperatorExpressionRenderer handles separately.
    private static final Map<String, Integer> PRECEDENCE = Map.ofEntries(
            Map.entry(SysMLTokens.NULL_COALESCING_OPERATOR, NULL_COALESCING),
            Map.entry(SysMLTokens.IMPLIES_OPERATOR, IMPLIES),
            Map.entry(SysMLTokens.OR_OPERATOR, OR),
            Map.entry(SysMLTokens.PIPE_OPERATOR, OR),
            Map.entry(SysMLTokens.XOR_OPERATOR, XOR),
            Map.entry(SysMLTokens.AND_OPERATOR, AND),
            Map.entry(SysMLTokens.AMPERSAND_OPERATOR, AND),
            Map.entry(SysMLTokens.EQUALITY_OPERATOR, EQUALITY),
            Map.entry(SysMLTokens.INEQUALITY_OPERATOR, EQUALITY),
            Map.entry(SysMLTokens.IDENTITY_OPERATOR, EQUALITY),
            Map.entry(SysMLTokens.NON_IDENTITY_OPERATOR, EQUALITY),
            Map.entry(SysMLTokens.IS_TYPE_OPERATOR, CLASSIFICATION),
            Map.entry(SysMLTokens.HAS_TYPE_OPERATOR, CLASSIFICATION),
            Map.entry(SysMLTokens.CLASSIFICATION_TEST_OPERATOR, CLASSIFICATION),
            Map.entry(SysMLTokens.METACLASS_OPERATOR, CLASSIFICATION),
            Map.entry(SysMLTokens.AS_KEYWORD, CLASSIFICATION),
            Map.entry(SysMLTokens.META_OPERATOR, CLASSIFICATION),
            Map.entry(SysMLTokens.LESS_THAN_OPERATOR, RELATIONAL),
            Map.entry(SysMLTokens.GREATER_THAN_OPERATOR, RELATIONAL),
            Map.entry(SysMLTokens.LESS_OR_EQUAL_OPERATOR, RELATIONAL),
            Map.entry(SysMLTokens.GREATER_OR_EQUAL_OPERATOR, RELATIONAL),
            Map.entry(SysMLTokens.RANGE_OPERATOR, RANGE),
            Map.entry(SysMLTokens.PLUS_OPERATOR, ADDITIVE),
            Map.entry(SysMLTokens.MINUS_OPERATOR, ADDITIVE),
            Map.entry(SysMLTokens.MULTIPLICATION_OPERATOR, MULTIPLICATIVE),
            Map.entry(SysMLTokens.DIVISION_OPERATOR, MULTIPLICATIVE),
            Map.entry(SysMLTokens.REMAINDER_OPERATOR, MULTIPLICATIVE),
            Map.entry(SysMLTokens.POWER_OPERATOR, EXPONENTIATION),
            Map.entry(SysMLTokens.ALTERNATE_POWER_OPERATOR, EXPONENTIATION),
            Map.entry(SysMLTokens.NOT_OPERATOR, UNARY_NOT),
            Map.entry(SysMLTokens.CONJUGATION_OPERATOR, UNARY_NOT));

    private OperatorPrecedence() {
        // Prevent instantiation.
    }

    /**
     * Tells whether an operand rendered with {@code operator} needs parentheses inside {@code parentOperator}: it binds
     * more loosely, or binds equally on a side that the parent's level does not group without parentheses (the right
     * of a left-associative level, the left of {@code ^}, either side of the non-associative classification and range
     * levels). Unknown operators rank with equality.
     *
     * @param operator
     *            the operand's operator
     * @param parentOperator
     *            the enclosing operator, blank at top level
     * @param rightOperand
     *            whether the operand is on the parent's right
     * @return whether the operand must be parenthesized
     */
    static boolean mustParenthesize(final String operator,
            final String parentOperator, final boolean rightOperand) {
        if (parentOperator.isBlank()) {
            return false;
        }
        final int operandPrecedence = precedence(operator);
        final int parentPrecedence = precedence(parentOperator);
        return operandPrecedence < parentPrecedence
                || operandPrecedence == parentPrecedence
                        && isGroupedAtEqualPrecedence(parentOperator, rightOperand);
    }

    /**
     * Tells whether the source of a postfix operation ({@code .} feature chain, {@code #(...)} index, {@code ->}
     * invocation, {@code .{...}} collect or {@code .?{...}} select) must be grouped. Grammar rule
     * {@code primaryExpression} only accepts a base expression or another postfix operation there, so every prefix or
     * infix form is grouped, classification, {@code all} and conditionals included: {@code (a + b).c} is not
     * {@code a + b.c}, and {@code (all T).x} is not {@code all T.x}.
     *
     * @param source
     *            the source expression
     * @param sourceText
     *            its rendered text, which may already be grouped by a parser adapter
     * @return whether the caller must add parentheses around the source text
     */
    static boolean mustParenthesizePostfixSource(final Expression source, final String sourceText) {
        return (isNegativeLiteral(source)
                || source instanceof final OperatorExpression operatorExpression
                        && isPrefixOrInfixForm(operatorExpression.getOperator()))
                && !ExpressionPathSupport.isEnclosedInParentheses(sourceText);
    }

    /**
     * Tells whether an operator is written before or between its operands, so binds more loosely than a postfix form.
     * An operator expression without operator, such as an invocation, is neither.
     */
    private static boolean isPrefixOrInfixForm(final String operator) {
        return operator != null && (PRECEDENCE.containsKey(operator)
                || SysMLTokens.ALL_KEYWORD.equals(operator)
                || SysMLTokens.IF_KEYWORD.equals(operator));
    }

    /**
     * Tells whether the operand of a prefix unary operator ({@code not}, {@code -}, {@code +}, <code>&#126;</code>)
     * must be grouped. Grammar rule {@code unaryExpression: unaryOperator extentExpression} only accepts an extent or
     * postfix expression there, so every ranked operator is grouped, another unary operator included:
     * <code>&#126;(a * b)</code> is not <code>&#126;a * b</code>, and {@code not not a} does not parse.
     *
     * @param operand
     *            the operand expression
     * @param operandText
     *            its rendered text
     * @return whether the caller must add parentheses around the operand text
     */
    static boolean mustParenthesizeUnaryOperand(final Expression operand, final String operandText) {
        return (isNegativeLiteral(operand) || operand instanceof final OperatorExpression operator && isGroupedUnderUnaryOperator(operator))
                && !ExpressionPathSupport.isEnclosedInParentheses(operandText);
    }

    /** Tells whether an operator expression ranks below the unary operators, or is a conditional, which ranks below all. */
    private static boolean isGroupedUnderUnaryOperator(final OperatorExpression operator) {
        return PRECEDENCE.containsKey(operator.getOperator()) || isConditional(operator);
    }

    /**
     * Tells whether an expression is a conditional {@code if c ? a else b}, which grammar rule
     * {@code conditionalExpression} places above every operator: wherever an operand is expected, it must be grouped.
     */
    private static boolean isConditional(final OperatorExpression expression) {
        return SysMLTokens.IF_KEYWORD.equals(expression.getOperator());
    }

    /** Negative stored numbers render as unary expressions, including negative zero and negative real infinity. */
    private static boolean isNegativeLiteral(final Expression expression) {
        return expression instanceof final LiteralInteger integer && integer.getValue() < 0
                || expression instanceof final LiteralRational rational && Double.compare(rational.getValue(), 0.0) < 0;
    }

    /**
     * Tells whether the source of a classification operator such as {@code as} must be grouped because it binds more
     * loosely than the classification itself: {@code (a == b) as Boolean} is not {@code a == (b as Boolean)}, and
     * {@code (if c ? a else b) as T} is not a cast of the {@code else} branch only.
     *
     * @param source
     *            the source expression
     * @param sourceText
     *            its rendered text, which may already be grouped by a parser adapter
     * @param classificationOperator
     *            the classification operator applied to the source
     * @return whether the caller must add parentheses around the source text
     */
    static boolean mustParenthesizeClassificationSource(final Expression source, final String sourceText,
            final String classificationOperator) {
        return source instanceof final OperatorExpression operatorExpression
                && bindsMoreLooselyThanClassification(operatorExpression, classificationOperator)
                && !ExpressionPathSupport.isEnclosedInParentheses(sourceText);
    }

    /** Tells whether a classification source is a conditional or a ranked operator binding more loosely. */
    private static boolean bindsMoreLooselyThanClassification(final OperatorExpression source, final String classificationOperator) {
        return isConditional(source)
                || PRECEDENCE.containsKey(source.getOperator()) && mustParenthesize(source.getOperator(), classificationOperator, false);
    }

    /**
     * Tells whether an operand of the parent's own level is grouped: on both sides of a non-associative level, on the
     * left of right-associative exponentiation ({@code a ^ b ^ c}), and on the right of every other level.
     */
    private static boolean isGroupedAtEqualPrecedence(final String parentOperator, final boolean rightOperand) {
        if (NON_ASSOCIATIVE_LEVELS.contains(precedence(parentOperator))) {
            return true;
        }
        final boolean rightAssociative = SysMLTokens.POWER_OPERATOR.equals(parentOperator)
                || SysMLTokens.ALTERNATE_POWER_OPERATOR.equals(parentOperator);
        return rightAssociative != rightOperand;
    }

    private static int precedence(final String operator) {
        return PRECEDENCE.getOrDefault(operator, EQUALITY);
    }
}
