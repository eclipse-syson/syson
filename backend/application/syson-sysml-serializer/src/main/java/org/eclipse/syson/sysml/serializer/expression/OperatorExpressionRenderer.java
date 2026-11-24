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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.model.ParserAdapterQueries;
import org.eclipse.syson.sysml.serializer.model.SpecializationTargetNames;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.syntax.ParserFragments;
import org.eclipse.syson.sysml.serializer.syntax.SourceSyntax;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Expression;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureChainExpression;
import org.omg.sysml.lang.sysml.FeatureReferenceExpression;
import org.omg.sysml.lang.sysml.NullExpression;
import org.omg.sysml.lang.sysml.OperatorExpression;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Renders the {@code OperatorExpression} forms of {@code KerMLExpressions.g4}: binary operators with their precedence
 * parentheses, prefix unary operators, conditional, classification and metadata operators, {@code all}, sequences,
 * {@code #(...)} indexing, the {@code [unit]} quantity form, and the {@code .{...}} / {@code .?{...}} collect and
 * select shorthands.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
final class OperatorExpressionRenderer {

    private final ExpressionRenderer expressionRenderer;

    private final ReferenceResolver referenceResolver;

    private final InvocationExpressionRenderer invocationRenderer;

    private final OperatorOperandResolver operandResolver;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ParserAdapterQueries adapters;

    private final SpecializationTargetNames specializationTargets;

    /**
     * Creates an operator renderer for one run.
     *
     * @param expressionRenderer
     *            renders operands in their precedence context
     * @param referenceResolver
     *            renders type and feature names
     * @param invocationRenderer
     *            renders lambda bodies shared with {@code ->} invocations
     * @param operandResolver
     *            reads operands from parameter values
     * @param context
     *            the run's context
     */
    OperatorExpressionRenderer(final ExpressionRenderer expressionRenderer,
            final ReferenceResolver referenceResolver,
            final InvocationExpressionRenderer invocationRenderer,
            final OperatorOperandResolver operandResolver,
            final SerializationContext context) {
        this.expressionRenderer = expressionRenderer;
        this.referenceResolver = referenceResolver;
        this.invocationRenderer = invocationRenderer;
        this.operandResolver = operandResolver;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.adapters = context.modelQueries().adapters();
        this.specializationTargets = context.modelQueries().specializationTargets();
    }

    /**
     * Writes an operator expression, adding the parentheses its position requires. Operators whose syntax is not
     * {@code a op b} (tuples, unary forms, casts, indexing, conditionals, classification, {@code all}, collect and
     * select bodies, parser-fragment shapes) are tried first; otherwise the binary form is used, and as a last resort
     * the bare operator or single operand.
     *
     * @param expression
     *            the operator expression
     * @param parentOperator
     *            the enclosing operator, or an empty string at top level
     * @param rightOperand
     *            whether the expression is the right operand of {@code parentOperator}
     * @return the rendered text
     */
    String render(final OperatorExpression expression, final String parentOperator, final boolean rightOperand) {
        final List<Expression> operands = this.operandResolver.operands(expression);
        String text = this.typedInvocationText(expression, operands);
        if (text.isBlank()) {
            text = this.operatorSpecificText(expression, operands, parentOperator, rightOperand);
        }
        if (text.isBlank()) {
            text = this.binaryOrFallbackOperatorText(expression, operands, parentOperator, rightOperand);
        }
        return text;
    }

    /** Selects the grammar form of operators that are not written {@code a op b}. */
    private String operatorSpecificText(final OperatorExpression expression,
            final List<Expression> operands, final String parentOperator, final boolean rightOperand) {
        final String operator = expression.getOperator();
        if (operator == null) {
            return "";
        }
        String text = this.groupedInParent(this.classificationOperatorText(expression, operands, operator), operator,
                parentOperator, rightOperand);
        if (text.isBlank()) {
            text = this.unaryOperatorText(operands, operator);
        }
        if (text.isBlank()) {
            text = this.compoundOperatorText(expression, operands, parentOperator, operator);
        }
        return text;
    }

    /** Classification, metadata and extent operators keep their type in a parameter typing, not in an operand. */
    private String classificationOperatorText(final OperatorExpression expression,
            final List<Expression> operands, final String operator) {
        return switch (operator) {
            case SysMLTokens.CLASSIFICATION_TEST_OPERATOR, SysMLTokens.METACLASS_OPERATOR -> this.metadataTestText(expression, operands, operator);
            case SysMLTokens.META_OPERATOR -> this.typedOperatorText(this.firstOperandText(operands), SysMLTokens.META_INFIX,
                    this.operatorReturnType(expression));
            case SysMLTokens.AS_KEYWORD -> this.castText(expression, operands);
            case SysMLTokens.IS_TYPE_OPERATOR, SysMLTokens.HAS_TYPE_OPERATOR -> this.classificationTestText(expression, operands, operator);
            case SysMLTokens.ALL_KEYWORD -> this.extentText(expression);
            default -> "";
        };
    }

    /**
     * Groups a classification or metadata operation that binds more loosely than its parent, as in
     * {@code (x as T) + 1}; the extent expression {@code all T} is a primary and is never grouped.
     */
    private String groupedInParent(final String text, final String operator, final String parentOperator,
            final boolean rightOperand) {
        if (text.isBlank() || SysMLTokens.ALL_KEYWORD.equals(operator)
                || !OperatorPrecedence.mustParenthesize(operator, parentOperator, rightOperand)
                || ExpressionPathSupport.isEnclosedInParentheses(text)) {
            return text;
        }
        return ExpressionPathSupport.parenthesized(text);
    }

    private String unaryOperatorText(final List<Expression> operands, final String operator) {
        if (operands.size() != 1) {
            return "";
        }
        return switch (operator) {
            case SysMLTokens.NOT_OPERATOR -> this.notText(operands.get(0));
            case SysMLTokens.PLUS_OPERATOR, SysMLTokens.MINUS_OPERATOR, SysMLTokens.CONJUGATION_OPERATOR ->
                this.prefixUnaryText(operator, operands.get(0));
            default -> "";
        };
    }

    private String compoundOperatorText(final OperatorExpression expression,
            final List<Expression> operands, final String parentOperator, final String operator) {
        return switch (operator) {
            case SysMLTokens.COMMA -> this.sequenceText(expression, operands);
            case SysMLTokens.INDEX_OPERATOR -> this.indexText(operands);
            case SysMLTokens.IF_KEYWORD -> this.conditionalText(operands, parentOperator);
            case SysMLTokens.COLLECT_OPERATOR, SysMLTokens.SELECT_OPERATOR -> this.collectOrSelectText(expression, operands, operator);
            default -> "";
        };
    }

    private String binaryOrFallbackOperatorText(final OperatorExpression expression,
            final List<Expression> operands, final String parentOperator, final boolean rightOperand) {
        String text = "";
        if (operands.size() == 2) {
            text = this.binaryText(expression, operands, parentOperator, rightOperand);
        }
        if (text.isBlank()) {
            final String operator = expression.getOperator();
            if (operator != null) {
                text = operator;
            } else if (operands.size() == 1) {
                text = this.firstOperandText(operands);
            }
        }
        return text;
    }

    /**
     * Writes the metadata tests {@code x @ T} / {@code @T} (implicit self) and {@code x @@ T}, whose type is held by a
     * parameter typing rather than by an operand.
     */
    private String metadataTestText(final OperatorExpression expression, final List<Expression> operands,
            final String operator) {
        final String metadataType = this.metadataAccessType(expression);
        final String source = this.classificationSourceText(operands, operator);
        final boolean quantity = SysMLTokens.CLASSIFICATION_TEST_OPERATOR.equals(operator);
        String text = "";
        if (!metadataType.isBlank() && !source.isBlank()) {
            String infix = SysMLTokens.METACLASS_INFIX;
            if (quantity) {
                infix = SysMLTokens.CLASSIFICATION_TEST_INFIX;
            }
            text = source + infix + metadataType;
        } else if (!metadataType.isBlank() && quantity) {
            text = SysMLTokens.CLASSIFICATION_TEST_OPERATOR + metadataType;
        }
        return text;
    }

    private String firstOperandText(final List<Expression> operands) {
        if (operands.isEmpty()) {
            return "";
        }
        return this.expressionRenderer.expressionText(operands.get(0));
    }

    /**
     * Writes the source of a classification or metadata test, grouped when it binds more loosely than the test:
     * {@code (a == b) istype T} is not {@code a == (b istype T)}. An implicit-self cast is the one classification the
     * grammar chains without grouping, as in {@code as T istype U}.
     */
    private String classificationSourceText(final List<Expression> operands, final String operator) {
        String source = this.firstOperandText(operands);
        if (!operands.isEmpty() && !this.isImplicitSelfCast(operands.get(0))
                && OperatorPrecedence.mustParenthesizeClassificationSource(operands.get(0), source, operator)) {
            source = ExpressionPathSupport.parenthesized(source);
        }
        return source;
    }

    private boolean isImplicitSelfCast(final Expression expression) {
        return expression instanceof final OperatorExpression cast
                && SysMLTokens.AS_KEYWORD.equals(cast.getOperator())
                && this.hasImplicitSelfOperand(this.operandResolver.operands(cast));
    }

    /**
     * Tells whether the only operand is the parser-generated self reference of a prefix form such as {@code istype T}
     * or {@code as T}, recognizable by its return parameter membership.
     */
    private boolean hasImplicitSelfOperand(final List<Expression> operands) {
        return operands.size() == 1
                && operands.get(0) instanceof FeatureReferenceExpression
                && this.navigation.ownedRelationships(operands.get(0)).stream()
                        .anyMatch(relationship -> this.navigation.isMetaclass(relationship,
                                SysMLPackage.eINSTANCE.getReturnParameterMembership()));
    }

    /**
     * Writes a sequence {@code (a, b, c)} built by nested comma operators. The outermost sequence of a standalone
     * sequence-expression fragment is written without enclosing parentheses; nested sequences keep theirs, as in
     * {@code 1, (2, 3)}. A trailing comma recorded by the parser is restored.
     */
    private String sequenceText(final OperatorExpression expression, final List<Expression> operands) {
        if (operands.isEmpty()) {
            return "";
        }
        String text;
        if (SourceSyntax.isRootParserEntry(this.context.options(), expression,
                ParserFragments.SEQUENCE_EXPRESSION)) {
            text = String.join(SysMLTokens.ARGUMENT_SEPARATOR, this.sequenceOperandElements(operands));
        } else {
            text = this.sequenceOperandElements(operands).stream()
                    .collect(Collectors.joining(SysMLTokens.ARGUMENT_SEPARATOR,
                            SysMLTokens.OPEN_PARENTHESIS, SysMLTokens.CLOSE_PARENTHESIS));
        }
        if (this.adapters.hasTextAdapter(expression, ITextAdapter.TRAILING_COMMA)) {
            final int closing = text.lastIndexOf(SysMLTokens.CLOSE_PARENTHESIS);
            if (closing >= 0) {
                text = text.substring(0, closing) + SysMLTokens.COMMA + text.substring(closing);
            }
        }
        return text;
    }

    /** Writes {@code not x}, grouping the operand when the source did or when it is not an extent or postfix form. */
    private String notText(final Expression operand) {
        String operandText = this.expressionRenderer.expressionText(operand, SysMLTokens.NOT_OPERATOR, true);
        final boolean groupedInSource = this.adapters.hasTextAdapter(operand, ITextAdapter.PARENTHESIZED_EXPRESSION)
                && !operandText.startsWith(SysMLTokens.OPEN_PARENTHESIS);
        if (groupedInSource || OperatorPrecedence.mustParenthesizeUnaryOperand(operand, operandText)) {
            operandText = ExpressionPathSupport.parenthesized(operandText);
        }
        return SysMLTokens.NOT_OPERATOR + SysMLTokens.SPACE + operandText;
    }

    /**
     * Writes {@code +x}, {@code -x} and <code>&#126;x</code>, grouping a composite operand. A unary expression binds
     * tighter than every binary operator, {@code ^} included, so it never needs grouping itself: {@code -(a * b) ^ c}
     * already reads as {@code (-(a * b)) ^ c}.
     */
    private String prefixUnaryText(final String operator, final Expression operand) {
        String operandText = this.expressionRenderer.expressionText(operand, operator, true);
        if (OperatorPrecedence.mustParenthesizeUnaryOperand(operand, operandText)) {
            operandText = ExpressionPathSupport.parenthesized(operandText);
        }
        return operator + operandText;
    }

    /** Writes {@code x#(i, j)}, grouping a prefix or infix source so the index applies to all of it. */
    private String indexText(final List<Expression> operands) {
        if (operands.size() != 2) {
            return "";
        }
        return this.postfixSourceText(operands.get(0)) + SysMLTokens.INDEX_START + this.indexArgumentsText(operands.get(1)) + SysMLTokens.CLOSE_PARENTHESIS;
    }

    /**
     * Writes the source of a postfix operation ({@code #(...)}, {@code .{...}}, {@code .?{...}}, {@code ->}), grouped
     * when it is a prefix or infix form that would otherwise lend only its last operand to the postfix operation.
     */
    private String postfixSourceText(final Expression source) {
        final String text = this.expressionRenderer.expressionText(source);
        if (OperatorPrecedence.mustParenthesizePostfixSource(source, text)) {
            return ExpressionPathSupport.parenthesized(text);
        }
        return text;
    }

    /** Writes index arguments as a flat list, unless the source grouped them as one expression. */
    private String indexArgumentsText(final Expression indices) {
        if (this.adapters.hasTextAdapter(indices, ITextAdapter.PARENTHESIZED_EXPRESSION)) {
            return this.expressionRenderer.expressionText(indices);
        }
        return String.join(SysMLTokens.ARGUMENT_SEPARATOR, this.sequenceElements(indices));
    }

    /**
     * Writes {@code x as T}, or {@code as T} for the implicit-self form; an unresolved source falls back to its
     * dangling parser spelling.
     */
    private String castText(final OperatorExpression expression, final List<Expression> operands) {
        String source;
        if (operands.isEmpty()) {
            source = "";
        } else {
            source = this.firstOperandText(operands);
            if (this.mustParenthesizeCastSource(operands.get(0), source)) {
                source = ExpressionPathSupport.parenthesized(source);
            }
        }
        final String type = this.operatorReturnType(expression);
        if (source.isBlank() && this.hasImplicitSelfOperand(operands) && !type.isBlank()) {
            return SysMLTokens.AS_PREFIX + type;
        }
        return this.typedOperatorText(source, SysMLTokens.AS_INFIX, type);
    }

    /**
     * Decides whether the source of {@code as} needs parentheses: either the parser recorded grouping that the
     * rendered text lost, or the source binds more loosely than the cast and would change meaning without them.
     */
    private boolean mustParenthesizeCastSource(final Expression source, final String sourceText) {
        final boolean sourceGroupedInParsedText = this.adapters.hasTextAdapter(source, ITextAdapter.PARENTHESIZED_EXPRESSION)
                && !sourceText.startsWith(SysMLTokens.OPEN_PARENTHESIS);
        return sourceGroupedInParsedText
                || OperatorPrecedence.mustParenthesizeClassificationSource(source, sourceText, SysMLTokens.AS_KEYWORD);
    }

    private String typedOperatorText(final String source, final String operatorInfix, final String type) {
        if (source.isBlank() || type.isBlank()) {
            return "";
        }
        return source + operatorInfix + type;
    }

    /**
     * Writes {@code if c ? a else b}; it is grouped inside any operator because it has the lowest precedence and
     * would otherwise absorb the following operands into its else branch.
     */
    private String conditionalText(final List<Expression> operands, final String parentOperator) {
        String text = "";
        if (operands.size() == 3) {
            text = SysMLTokens.IF_PREFIX
                    + this.conditionText(operands.get(0))
                    + SysMLTokens.CONDITIONAL_SEPARATOR_INFIX
                    + this.conditionalBranchText(operands.get(1))
                    + SysMLTokens.ELSE_INFIX
                    + this.conditionalBranchText(operands.get(2));
            if (!parentOperator.isBlank()) {
                text = ExpressionPathSupport.parenthesized(text);
            }
        }
        return text;
    }

    /**
     * Writes a conditional's condition, which the grammar reads as a {@code nullCoalescingExpression}: rendering it as
     * the left operand of {@code ??} groups a nested conditional, as in {@code if (if a ? b else c) ? d else e}.
     */
    private String conditionText(final Expression condition) {
        return this.expressionRenderer.expressionText(condition, SysMLTokens.NULL_COALESCING_OPERATOR, false);
    }

    /** Writes the extent expression {@code all T}, using the dangling spelling when the type is unresolved. */
    private String extentText(final OperatorExpression expression) {
        final String type = this.operatorReturnType(expression);
        if (type.isBlank()) {
            return "";
        }
        return SysMLTokens.ALL_PREFIX + this.referenceResolver.referenceName(type);
    }

    /**
     * Writes {@code a op b} with precedence and source parentheses, or the quantity form {@code value [unit]} for the
     * {@code [} operator.
     */
    private String binaryText(final OperatorExpression expression, final List<Expression> operands,
            final String parentOperator, final boolean rightOperand) {
        final String operator = expression.getOperator();
        if (SysMLTokens.OPEN_BRACKET.equals(operator)) {
            return this.quantityText(operands);
        }
        String text = this.operatorOperandText(operands.get(0), operator, false)
                + SysMLTokens.SPACE + operator + SysMLTokens.SPACE
                + this.operatorOperandText(operands.get(1), operator, true)
                + this.expressionRenderer.directTrailingCommentText(expression);
        final boolean explicitParentheses = this.adapters.hasTextAdapter(expression, ITextAdapter.PARENTHESIZED_EXPRESSION)
                && !text.startsWith(SysMLTokens.OPEN_PARENTHESIS);
        if (explicitParentheses || OperatorPrecedence.mustParenthesize(operator, parentOperator, rightOperand)) {
            text = ExpressionPathSupport.parenthesized(text);
        }
        return text;
    }

    /** Writes {@code value [unit]}, grouping any operator value except a chain, an index or another quantity. */
    private String quantityText(final List<Expression> operands) {
        final Expression value = operands.get(0);
        String valueText = this.expressionRenderer.expressionText(value);
        final boolean compositeValue = value instanceof final OperatorExpression nested
                && !(nested instanceof FeatureChainExpression)
                && !SysMLTokens.OPEN_BRACKET.equals(nested.getOperator())
                && !SysMLTokens.INDEX_OPERATOR.equals(nested.getOperator());
        if ((compositeValue || OperatorPrecedence.mustParenthesizePostfixSource(value, valueText))
                && !ExpressionPathSupport.isEnclosedInParentheses(valueText)) {
            valueText = ExpressionPathSupport.parenthesized(valueText);
        }
        return valueText + SysMLTokens.SPACE + SysMLTokens.OPEN_BRACKET
                + this.indexArgumentsText(operands.get(1)) + SysMLTokens.CLOSE_BRACKET;
    }

    /**
     * Writes the invocations the parser builds as an operator expression without operator, where the invoked function
     * is held as a feature typing: {@code x->f(args)} in a target expression, such as an assignment target, and
     * {@code x->f arg} with a type or body argument.
     */
    private String typedInvocationText(final OperatorExpression expression, final List<Expression> operands) {
        String text = "";
        if (this.isTypedTargetInvocation(expression, operands)) {
            final String function = this.firstFeatureTypeName(expression);
            if (!function.isBlank()) {
                text = this.invocationRenderer.renderInvocation(expression, this.referenceResolver.referenceName(function));
            }
        } else if (expression.getOperator() == null && operands.size() == 1) {
            final String function = this.firstFeatureTypeName(expression);
            final String argument = this.navigation.featureMembershipElements(expression).stream()
                    .filter(Expression.class::isInstance)
                    .map(element -> this.typedInvocationArgument((Expression) element))
                    .filter(value -> !value.isBlank()).findFirst().orElse("");
            if (!function.isBlank() && !argument.isBlank()) {
                text = this.postfixSourceText(operands.get(0)) + SysMLTokens.NAVIGATION_OPERATOR
                        + this.referenceResolver.referenceName(function) + SysMLTokens.SPACE + argument;
            }
        }
        return text;
    }

    /** Writes the argument of a typed invocation: its type name, or its body as {@code {expr}}. */
    private String typedInvocationArgument(final Expression argument) {
        final String type = this.firstFeatureTypeName(argument);
        final Expression body = this.navigation.resultExpression(argument);
        String text = "";
        if (!type.isBlank()) {
            text = this.referenceResolver.referenceName(type);
        } else if (body != null) {
            text = SysMLTokens.OPEN_BRACE_PREFIX + this.expressionRenderer.expressionText(body)
                    + SysMLTokens.CLOSE_BRACE_SUFFIX;
        }
        return text;
    }

    /**
     * Recognizes {@code x->f(args)} as the parser builds it in a target expression: an operator expression without
     * operator whose receiver and arguments are its operands. The structure is enough, whether the text is a whole
     * document or the standalone target-expression fragment.
     */
    private boolean isTypedTargetInvocation(final OperatorExpression expression, final List<Expression> operands) {
        final String operator = expression.getOperator();
        return this.navigation.isMetaclass(expression, SysMLPackage.eINSTANCE.getOperatorExpression())
                && (operator == null || operator.isBlank()) && operands.size() >= 2;
    }

    /** Writes the classification tests {@code x istype T} and {@code x hastype T}, or their implicit-self forms. */
    private String classificationTestText(final OperatorExpression expression, final List<Expression> operands,
            final String operator) {
        final String source = this.classificationSourceText(operands, operator);
        final String type = this.operatorTypes(expression)
                .map(this.referenceResolver::referenceName)
                .filter(name -> !name.isBlank())
                .findFirst().orElse("");
        String text = "";
        if (!type.isBlank() && !source.isBlank()) {
            text = source + SysMLTokens.SPACE + operator + SysMLTokens.SPACE + type;
        } else if (!type.isBlank() && this.hasImplicitSelfOperand(operands)) {
            text = operator + SysMLTokens.SPACE + type;
        }
        return text;
    }

    /** Writes the {@code x.{...}} collect and {@code x.?{...}} select shorthands. */
    private String collectOrSelectText(final OperatorExpression expression, final List<Expression> operands,
            final String operator) {
        if (operands.isEmpty()) {
            return "";
        }
        final String receiver = this.postfixSourceText(operands.get(0));
        final Element body = this.collectOrSelectBody(expression);
        String bodyText = "";
        if (body != null) {
            bodyText = this.bodyExpressionText(body);
        }
        String opening = SysMLTokens.FILTER_BODY_START;
        if (SysMLTokens.COLLECT_OPERATOR.equals(operator)) {
            opening = SysMLTokens.COLLECTION_BODY_START;
        }
        return receiver + opening + bodyText + SysMLTokens.CLOSE_BRACE;
    }

    /**
     * Returns the body expression of a collect or select, held directly as a feature member or as the value of the
     * second parameter; {@code null} when absent.
     */
    private Element collectOrSelectBody(final OperatorExpression expression) {
        final Element direct = this.navigation.featureMembershipElements(expression).stream()
                .filter(element -> this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getExpression()))
                .findFirst().orElse(null);
        if (direct != null) {
            return direct;
        }
        return this.navigation.parameterFeatures(expression).stream().skip(1)
                .map(feature -> this.navigation.featureValues(feature).stream()
                        .map(this.navigation::featureValueExpression)
                        .filter(Objects::nonNull)
                        .findFirst().orElse(null))
                .map(this.navigation::firstOwnedExpression)
                .filter(value -> this.navigation.isMetaclass(value, SysMLPackage.eINSTANCE.getExpression()))
                .findFirst().orElse(null);
    }

    /**
     * Writes the inside of a body expression, without braces: its parameter declaration and result, or only the
     * result when it declares no parameter.
     *
     * @param body
     *            the body expression, or the invocation holding it as a lambda argument
     * @return the body text, or an empty string when nothing can be written
     */
    String bodyExpressionText(final Element body) {
        String text = this.invocationRenderer.invocationLambdaText(body);
        if (text.isBlank()) {
            final Expression result = this.navigation.resultExpression(body);
            if (result != null) {
                text = this.typedBodyResultText(body, result);
            }
        }
        return text;
    }

    /**
     * Writes {@code in x :> T; result} when the body parameter is typed or subsets a feature, otherwise only the
     * result.
     */
    private String typedBodyResultText(final Element body, final Expression result) {
        final Element parameter = this.invocationRenderer.firstNamedFeature(body);
        final String resultText = this.expressionRenderer.expressionText(result);
        String text = resultText;
        if (parameter != null && !resultText.isBlank()) {
            final String parameterName = this.referenceResolver.declaredName(parameter);
            String type = this.firstFeatureTypeName(parameter);
            if (type.isBlank()) {
                type = this.specializationTargets.subsettedFeatureNames(parameter,
                        this.context.danglingReferenceIndex(), this.context.nameRenderer()::referenceName)
                        .stream().findFirst().orElse("");
            }
            if (!parameterName.isBlank() && !type.isBlank()) {
                text = SysMLTokens.IN_PREFIX + parameterName + SysMLTokens.SUBSETTING_INFIX
                        + this.referenceResolver.referenceName(type) + SysMLTokens.STATEMENT_SEPARATOR + resultText;
            }
        }
        return text;
    }

    /**
     * Writes a binary operand as the operand itself: a body expression such as {@code { in x; f(x) }} is kept whole,
     * never replaced by an expression nested in it.
     */
    private String operatorOperandText(final Expression operand, final String operator, final boolean rightOperand) {
        String text = SysMLTokens.NULL_LITERAL;
        if (!(operand instanceof NullExpression)) {
            text = this.regularOperatorOperandText(operand, operator, rightOperand);
        }
        return text;
    }

    /** Writes an operand with its own comments and the parentheses the source wrote. */
    private String regularOperatorOperandText(final Expression operand, final String operator,
            final boolean rightOperand) {
        String text = this.expressionRenderer.expressionText(operand, operator, rightOperand);
        final boolean explicitParentheses = this.adapters.hasTextAdapter(operand, ITextAdapter.PARENTHESIZED_EXPRESSION)
                && !ExpressionPathSupport.isEnclosedInParentheses(text);
        if (explicitParentheses) {
            text = ExpressionPathSupport.parenthesized(text);
        }
        return text;
    }

    /**
     * Renders a {@code ?}/{@code else} branch. A null branch is written as the {@code null} literal, which the parser
     * may represent either by the operand itself or by a nested null expression.
     */
    private String conditionalBranchText(final Expression operand) {
        if (operand instanceof NullExpression) {
            return SysMLTokens.NULL_LITERAL;
        }
        return this.expressionRenderer.expressionText(operand);
    }

    /** Returns the rendered type of the first typed parameter, which holds the metaclass of {@code @} / {@code @@}. */
    private String metadataAccessType(final OperatorExpression expression) {
        return this.firstRenderedFeatureType(this.navigation.parameterFeatures(expression).stream());
    }

    /** Returns the rendered type of the return parameter, which holds the target type of {@code as} / {@code meta}. */
    private String operatorReturnType(final OperatorExpression expression) {
        final Stream<Feature> returnParameters = this.navigation.ownedRelationships(expression).stream()
                .filter(relationship -> this.navigation.isMetaclass(relationship,
                        SysMLPackage.eINSTANCE.getReturnParameterMembership()))
                .flatMap(relationship -> this.navigation.ownedRelatedElements(relationship).stream())
                .filter(Feature.class::isInstance)
                .map(Feature.class::cast);
        return this.firstRenderedFeatureType(returnParameters);
    }

    private String firstRenderedFeatureType(final Stream<Feature> features) {
        return this.featureTypes(features)
                .map(this.referenceResolver::referenceName)
                .filter(name -> !name.isBlank())
                .findFirst().orElse("");
    }

    /** Returns the types of the parameters after the first, where classification operators keep their type. */
    private Stream<String> operatorTypes(final OperatorExpression expression) {
        return this.featureTypes(this.navigation.parameterFeatures(expression).stream().skip(1));
    }

    private Stream<String> featureTypes(final Stream<Feature> features) {
        return features.flatMap(feature -> this.featureTypeNames(feature).stream());
    }

    private String firstFeatureTypeName(final Element element) {
        return this.featureTypeNames(element).stream().findFirst().orElse("");
    }

    private List<String> featureTypeNames(final Element element) {
        return this.specializationTargets.featureTypeNames(element, this.context.danglingReferenceIndex(),
                this.context.nameRenderer()::referenceName);
    }

    /**
     * Returns the rendered elements of a sequence's operands, flattening the right-nested comma operators. An earlier
     * or explicitly parenthesized nested sequence is kept whole, since flattening it would lose its grouping.
     */
    private List<String> sequenceOperandElements(final List<Expression> operands) {
        final List<String> elements = new ArrayList<>();
        for (int index = 0; index < operands.size(); index++) {
            final Expression operand = operands.get(index);
            final boolean nestedSequence = operand instanceof final OperatorExpression operator
                    && SysMLTokens.COMMA.equals(operator.getOperator());
            if (nestedSequence && (index < operands.size() - 1
                    || this.adapters.hasTextAdapter(operand, ITextAdapter.PARENTHESIZED_EXPRESSION))) {
                elements.add(this.expressionRenderer.expressionText(operand));
            } else {
                elements.addAll(this.sequenceElements(operand));
            }
        }
        return elements;
    }

    /** Returns the rendered elements of a sequence, or the expression itself; comment-only elements are dropped. */
    private List<String> sequenceElements(final Expression expression) {
        final List<String> elements;
        if (expression instanceof final OperatorExpression operatorExpression
                && SysMLTokens.COMMA.equals(operatorExpression.getOperator())) {
            elements = this.sequenceOperandElements(this.operandResolver.operands(operatorExpression));
        } else {
            final String text = this.expressionRenderer.expressionText(expression);
            if (ExpressionPathSupport.isCommentOnlyText(text)) {
                elements = List.of();
            } else {
                elements = List.of(text);
            }
        }
        return elements;
    }
}
