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
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.model.ModelFlags;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.model.NestedTargetSearch;
import org.eclipse.syson.sysml.serializer.model.ParserAdapterQueries;
import org.eclipse.syson.sysml.serializer.model.SpecializationTargetNames;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.syntax.CommentRenderer;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Comment;
import org.omg.sysml.lang.sysml.Documentation;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Expression;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureReferenceExpression;
import org.omg.sysml.lang.sysml.InvocationExpression;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.ParameterMembership;
import org.omg.sysml.lang.sysml.ReferenceUsage;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.ResultExpressionMembership;
import org.omg.sysml.lang.sysml.ReturnParameterMembership;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.lang.sysml.VisibilityKind;

/**
 * Renders the invocation forms of {@code KerMLExpressions.g4}: {@code new T(...)} constructors, {@code f(...)} calls,
 * {@code x->f(...)} invocations and their body arguments ({@code x->f {in v; ...}}), whose body text is shared with the
 * {@code .{...}} / {@code .?{...}} shorthands.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
final class InvocationExpressionRenderer {

    private final ExpressionRenderer expressionRenderer;

    private final ReferenceResolver referenceResolver;

    private final CommentRenderer comments;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ModelFlags flags;

    private final ParserAdapterQueries adapters;

    private final SpecializationTargetNames specializationTargets;

    private final NestedTargetSearch nestedTargets;

    private final OperatorOperandResolver operandResolver;

    private final Function<List<Relationship>, String> relationshipsText;

    /**
     * Creates an invocation renderer for one run.
     *
     * @param expressionRenderer
     *            renders receivers, arguments and lambda results
     * @param referenceResolver
     *            renders function, type and feature names
     * @param comments
     *            renders comments kept inside lambda bodies
     * @param context
     *            the run's context
     * @param operandResolver
     *            reads argument values from parameter features
     * @param relationshipsText
     *            renders the members of a full body expression
     */
    InvocationExpressionRenderer(final ExpressionRenderer expressionRenderer,
            final ReferenceResolver referenceResolver,
            final CommentRenderer comments,
            final SerializationContext context,
            final OperatorOperandResolver operandResolver, final Function<List<Relationship>, String> relationshipsText) {
        this.expressionRenderer = expressionRenderer;
        this.referenceResolver = referenceResolver;
        this.comments = comments;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.flags = context.modelQueries().flags();
        this.adapters = context.modelQueries().adapters();
        this.specializationTargets = context.modelQueries().specializationTargets();
        this.nestedTargets = context.modelQueries().nestedTargets();
        this.operandResolver = operandResolver;
        this.relationshipsText = relationshipsText;
    }

    /**
     * Writes {@code new T(args)}. Named arguments ({@code p = v}, from redefining features) take precedence over
     * positional operands; a type held by a feature chain is written as a dotted path.
     *
     * @param expression
     *            the constructor expression
     * @return the rendered constructor
     */
    String renderConstructor(final Expression expression) {
        String type = this.referenceResolver.firstDirectRelationshipTarget(expression,
                SysMLPackage.eINSTANCE.getMembership());
        if (type.isBlank()) {
            type = String.join(SysMLTokens.PATH_SEPARATOR,
                    this.nestedTargets.chainedFeatureNames(expression,
                            this.context.danglingReferenceIndex()));
        }
        type = this.expressionRenderer.referenceLeadingCommentText(expression) + type;
        final List<String> namedArguments = this
                .constructorNamedArguments(expression);
        if (!namedArguments.isEmpty()) {
            return SysMLTokens.NEW_PREFIX + type + SysMLTokens.OPEN_PARENTHESIS
                    + String.join(SysMLTokens.ARGUMENT_SEPARATOR,
                            namedArguments)
                    + SysMLTokens.CLOSE_PARENTHESIS;
        }
        final List<Expression> operands = this.navigation.operands(expression);
        final List<String> arguments = operands.stream()
                .map(this.expressionRenderer::expressionText)
                .filter(text -> !text.isBlank()).toList();
        return SysMLTokens.NEW_PREFIX + type + SysMLTokens.OPEN_PARENTHESIS
                + String.join(SysMLTokens.ARGUMENT_SEPARATOR, arguments)
                + SysMLTokens.CLOSE_PARENTHESIS;
    }

    /**
     * Writes a function call: {@code f(args)}, {@code receiver->f(args)}, or {@code receiver->f {lambda}} when the
     * second operand is a body expression. Trailing comments captured inside the argument list are kept there.
     *
     * @param expression
     *            the invocation expression
     * @return the rendered invocation
     */
    String renderInvocation(final Expression expression) {
        String function = this.referenceResolver.firstDirectRelationshipTarget(
                expression, SysMLPackage.eINSTANCE.getMembership());
        if (function.isBlank()) {
            final List<String> chainedFeatures = this.nestedTargets
                    .chainedFeatureNames(expression,
                            this.context.danglingReferenceIndex());
            if (chainedFeatures.size() > 1) {
                function = String.join(SysMLTokens.PATH_SEPARATOR,
                        chainedFeatures);
            }
        }
        return this.renderInvocation(expression, function);
    }

    /**
     * Writes a call whose function is already named, so that the forms the parser builds without a function
     * membership, such as the {@code x->f(1, 2)} of an assignment target typed by {@code f}, keep their receiver and
     * every positional or named argument.
     *
     * @param expression
     *            the invocation expression, receiver first
     * @param functionName
     *            the reference text of the invoked function
     * @return the rendered invocation
     */
    String renderInvocation(final Expression expression, final String functionName) {
        final List<Expression> operands = this.navigation.operands(expression);
        String receiver = "";
        if (!operands.isEmpty()) {
            receiver = this.expressionRenderer.expressionText(operands.get(0));
        }
        final String trailingComments = this.expressionRenderer
                .directTrailingCommentText(expression);
        final String function = this.expressionRenderer.referenceLeadingCommentText(expression) + functionName;
        String text = this.lambdaInvocationText(expression, operands, receiver,
                function);
        if (text.isBlank()) {
            text = this.regularInvocationText(expression, operands, receiver,
                    function, trailingComments);
        }
        return text;
    }

    /** Writes {@code receiver->f {in x; body}}, or an empty string when the second operand is not a lambda. */
    private String lambdaInvocationText(final Expression expression,
            final List<Expression> operands, final String receiver,
            final String function) {
        if (operands.size() != 2 || receiver.isBlank() || this.hasFunctionMembershipBeforeArguments(expression)) {
            return "";
        }
        String text = "";
        final String lambda = this.invocationLambdaText(expression);
        if (!lambda.isBlank()) {
            String body = SysMLTokens.OPEN_BRACE_PREFIX + lambda + SysMLTokens.CLOSE_BRACE_SUFFIX;
            if (this.adapters.hasTextAdapter(operands.get(1), ITextAdapter.PARENTHESIZED_EXPRESSION)) {
                body = ExpressionPathSupport.parenthesized(body);
            }
            if (this.adapters.hasTextAdapter(expression, ITextAdapter.PARENTHESIZED_LAMBDA_ARGUMENT)
                    || this.lambdaUsesArgumentListSyntax(expression)) {
                body = ExpressionPathSupport.parenthesized(body);
            }
            text = this.navigationReceiver(operands.get(0), receiver)
                    + SysMLTokens.NAVIGATION_OPERATOR + function + body;
        }
        return text;
    }

    /**
     * Tells whether the lambda argument was passed inside an argument list ({@code ->f({...})}) rather than as a bare
     * body ({@code ->f {...}}). The parser marks the bare form with a private parameter membership.
     *
     * @param expression
     *            the invocation expression
     * @return {@code true} if the lambda must be written between parentheses
     */
    boolean lambdaUsesArgumentListSyntax(final Expression expression) {
        final List<Expression> operands = this.navigation.operands(expression);
        if (operands.size() < 2) {
            return false;
        }
        EObject container = operands.get(1);
        while (container != null && !(container instanceof ParameterMembership)) {
            container = container.eContainer();
        }
        return container instanceof ParameterMembership membership
                && membership.getVisibility() != VisibilityKind.PRIVATE;
    }

    /**
     * Writes a call without lambda. Operand zero is the {@code ->} receiver unless the function is referenced by a
     * leading membership, in which case it is an ordinary argument. A single argument bound to a private parameter
     * membership was written without parentheses ({@code x->f arg}) and is written the same way.
     */
    private String regularInvocationText(final Expression expression,
            final List<Expression> operands, final String receiver,
            final String function, final String trailingComments) {
        final List<String> namedArguments = this
                .invocationNamedArguments(expression);
        final boolean hasReceiver = !this.hasFunctionMembershipBeforeArguments(expression);
        String prefix = function;
        if (hasReceiver && !receiver.isBlank()) {
            prefix = this.navigationReceiver(operands.get(0), receiver) + SysMLTokens.NAVIGATION_OPERATOR + function;
        }
        List<String> arguments;
        if (!namedArguments.isEmpty()) {
            arguments = namedArguments;
        } else {
            int receiverCount = 0;
            if (hasReceiver) {
                receiverCount = 1;
            }
            arguments = operands.stream().skip(receiverCount)
                    .map(this.expressionRenderer::expressionText)
                    .filter(text -> !text.isBlank()).toList();
        }
        arguments = this.appendTrailingArgumentComments(expression, arguments);
        if (arguments.size() == 1 && !receiver.isBlank() && this.hasPrivateArgumentMembership(expression)) {
            return prefix + SysMLTokens.SPACE + arguments.get(0)
                    + trailingComments;
        }
        return prefix + SysMLTokens.OPEN_PARENTHESIS
                + String.join(SysMLTokens.ARGUMENT_SEPARATOR, arguments)
                + trailingComments + SysMLTokens.CLOSE_PARENTHESIS;
    }

    /** Tells whether an argument after the receiver is bound by the private parameter membership of {@code x->f arg}. */
    private boolean hasPrivateArgumentMembership(final Expression expression) {
        return this.navigation.ownedRelationships(expression).stream()
                .filter(ParameterMembership.class::isInstance)
                .skip(1)
                .map(ParameterMembership.class::cast)
                .anyMatch(membership -> membership.getVisibility() == VisibilityKind.PRIVATE);
    }

    /**
     * Appends the comments written after each argument, aligned by parameter position, without mutating
     * {@code arguments}; leaves the list unchanged if the counts do not align.
     */
    private List<String> appendTrailingArgumentComments(final Expression expression,
            final List<String> arguments) {
        int receiverParameterCount = 0;
        if (!this.hasFunctionMembershipBeforeArguments(expression)) {
            receiverParameterCount = 1;
        }
        final List<Feature> argumentFeatures = this.navigation.parameterFeatures(expression)
                .stream().skip(receiverParameterCount).toList();
        if (argumentFeatures.size() != arguments.size()) {
            return arguments;
        }
        final List<String> argumentsWithComments = new ArrayList<>(arguments.size());
        for (int index = 0; index < arguments.size(); index++) {
            argumentsWithComments.add(arguments.get(index) + this.expressionRenderer
                    .directTrailingCommentText(argumentFeatures.get(index)));
        }
        return argumentsWithComments;
    }

    private List<String> invocationNamedArguments(
            final Expression expression) {
        return this.navigation.parameterFeatures(expression).stream()
                .map(this::namedArgumentText).filter(text -> !text.isBlank())
                .toList();
    }

    /** Returns the named arguments owned by the constructor's result feature, excluding nested argument values. */
    private List<String> constructorNamedArguments(
            final Expression expression) {
        return this.navigation.ownedRelationships(expression).stream()
                .filter(ReturnParameterMembership.class::isInstance)
                .flatMap(relationship -> this.navigation.ownedRelatedElements(relationship).stream())
                .flatMap(result -> this.navigation.parameterFeatures(result).stream())
                .map(this::namedArgumentText)
                .filter(text -> !text.isBlank()).toList();
    }

    /** Writes {@code redefined = value}, or an empty string when the feature is not a named argument. */
    private String namedArgumentText(final Feature feature) {
        final List<String> redefinitions = this.specializationTargets.redefinedFeatureNames(
                feature, this.context.danglingReferenceIndex(),
                this.context.nameRenderer()::referenceName);
        final Expression value = this.operandResolver.operand(feature);
        if (redefinitions.isEmpty() || value == null) {
            return "";
        }
        return this.referenceResolver.referenceName(redefinitions.get(0))
                + SysMLTokens.EQUALS_OPERATOR
                + this.expressionRenderer.expressionText(value);
    }

    /**
     * Groups an operator-expression receiver of {@code ->}, which would otherwise only apply to its last operand.
     */
    private String navigationReceiver(final Expression receiverExpression, final String receiver) {
        if (OperatorPrecedence.mustParenthesizePostfixSource(receiverExpression, receiver)) {
            return ExpressionPathSupport.parenthesized(receiver);
        }
        return receiver;
    }

    /**
     * Tells whether the invoked function is referenced by the first owned relationship (a membership or a feature
     * chain), meaning the call was written {@code f(a, b)} rather than {@code a->f(b)}.
     */
    private boolean hasFunctionMembershipBeforeArguments(
            final Expression expression) {
        final List<Relationship> relationships = this.navigation.ownedRelationships(expression);
        if (relationships.isEmpty()) {
            return false;
        }
        final Relationship firstRelationship = relationships.getFirst();
        return this.navigation.isMetaclass(firstRelationship,
                SysMLPackage.eINSTANCE.getMembership())
                || this.isOwnedFeatureChainFunctionMembership(
                        firstRelationship);
    }

    private boolean isOwnedFeatureChainFunctionMembership(
            final Relationship relationship) {
        return this.navigation.isMetaclass(relationship,
                SysMLPackage.eINSTANCE.getOwningMembership())
                && this.navigation.ownedRelatedElements(relationship).stream()
                        .anyMatch(element -> this.navigation.ownedRelationships(element)
                                .stream()
                                .anyMatch(nestedRelationship -> this.navigation
                                        .isMetaclass(nestedRelationship,
                                                SysMLPackage.eINSTANCE
                                                        .getFeatureChaining())));
    }

    /**
     * Writes the inside of a lambda body: documentation, all declared parameters ({@code in x : T;}), private local
     * declarations of a quantifier, then the result expression.
     *
     * @param expression
     *            the invocation, or the lambda expression itself
     * @return the body text without braces, or an empty string when there is no named parameter or no result
     */
    String invocationLambdaText(final Element expression) {
        final Element lambda = this.lambdaArgument(expression);
        if (this.hasMembersOutsideCompactForm(lambda)) {
            return this.relationshipsText.apply(this.navigation.bodyRelationships(lambda));
        }
        return this.compactLambdaText(lambda);
    }

    /** Writes named parameters and the result of a compact lambda body. */
    private String compactLambdaText(final Element lambda) {
        final List<Element> parameters = this.navigation.ownedRelationships(lambda).stream()
                .filter(relationship -> relationship instanceof Membership membership
                        && membership.getVisibility() != VisibilityKind.PRIVATE
                        && !(membership instanceof ResultExpressionMembership))
                .flatMap(relationship -> this.navigation.ownedRelatedElements(relationship).stream())
                .filter(Feature.class::isInstance)
                .filter(parameter -> !this.referenceResolver.declaredName(parameter).isBlank())
                .toList();
        final Expression result = this.navigation.resultExpression(lambda);
        if (parameters.isEmpty() || result == null) {
            return "";
        }
        final String resultText = this.expressionRenderer.expressionText(result);
        String text = "";
        if (!resultText.isBlank()) {
            text = this.lambdaDocumentationText(lambda, parameters)
                    + parameters.stream().map(this::lambdaVariableText).collect(Collectors.joining())
                    + this.quantifierLocalDeclarations(lambda, parameters, result)
                    + resultText;
        }
        return text;
    }

    /**
     * Tells whether a body expression has members that the compact {@code in x : T; result} text cannot write: a member
     * other than a parameter or a comment (an attribute, a binding connector...), a return parameter, or no result
     * expression. Its members are then written with the element renderers. Other arguments, such as feature chains,
     * are never body expressions.
     */
    private boolean hasMembersOutsideCompactForm(final Element lambda) {
        if (!this.navigation.isMetaclass(lambda, SysMLPackage.eINSTANCE.getExpression())) {
            return false;
        }
        final List<Relationship> members = this.navigation.bodyRelationships(lambda).stream()
                .filter(relationship -> !(relationship instanceof ResultExpressionMembership))
                .toList();
        return !members.isEmpty() && (this.navigation.resultExpression(lambda) == null
                || members.stream().anyMatch(relationship -> relationship instanceof ReturnParameterMembership
                        || this.navigation.ownedRelatedElements(relationship).stream()
                                .anyMatch(member -> !(member instanceof ReferenceUsage) && !(member instanceof Comment))));
    }

    /** Writes the lambda parameter declaration with its direction, explicit {@code ref} and inline body comments. */
    private String lambdaVariableText(final Element variable) {
        final String variableName = this.referenceResolver.declaredName(variable);
        final String type = this.specializationTargets.featureTypeNames(variable,
                this.context.danglingReferenceIndex(), this.context.nameRenderer()::referenceName)
                .stream().findFirst().map(this.referenceResolver::referenceName).orElse("");
        String typeText = "";
        if (!type.isBlank()) {
            typeText = SysMLTokens.TYPE_SEPARATOR_INFIX + type;
        }
        final List<String> subsettedFeatures = this.specializationTargets.subsettedFeatureNames(variable,
                this.context.danglingReferenceIndex(), this.context.nameRenderer()::referenceName);
        if (!subsettedFeatures.isEmpty()) {
            typeText += SysMLTokens.SUBSETTING_INFIX + this.referenceResolver.referenceNames(subsettedFeatures);
        }
        String prefix = this.flags.direction(variable);
        if (!prefix.isBlank()) {
            prefix += SysMLTokens.SPACE;
        }
        if (this.adapters.hasTextAdapter(variable,
                ITextAdapter.EXPLICIT_REF_PREFIX)) {
            prefix += SysMLTokens.REF_PREFIX;
        }
        final String variableBody = this.comments.inlineBodyComments(variable);
        String terminator = SysMLTokens.SPACE;
        if (variableBody.isBlank()) {
            terminator = SysMLTokens.STATEMENT_SEPARATOR;
        }
        return prefix + variableName + typeText + variableBody + terminator;
    }

    /** Collects the documentation comments of the lambda subtree, outside its parameters' own subtrees. */
    private String lambdaDocumentationText(final Element element,
            final List<Element> excludedSubtrees) {
        if (element == null || excludedSubtrees.contains(element)) {
            return "";
        }
        final StringBuilder builder = new StringBuilder();
        for (final Relationship relationship : this.navigation.ownedRelationships(element)) {
            for (final Element related : this.navigation
                    .ownedRelatedElements(relationship)) {
                if (excludedSubtrees.contains(related)) {
                    continue;
                }
                if (related instanceof final Documentation documentation) {
                    builder.append(this.comments.commentText(documentation));
                } else {
                    builder.append(this.lambdaDocumentationText(related,
                            excludedSubtrees));
                }
            }
        }
        return builder.toString();
    }

    /** Returns the second operand of an invocation, or the element itself when it is already the lambda. */
    private Element lambdaArgument(final Element expression) {
        Element argument = expression;
        if (expression instanceof InvocationExpression) {
            final List<Expression> operands = this.navigation.operands(expression);
            if (operands.size() > 1) {
                argument = operands.get(1);
            }
        }
        if (argument instanceof FeatureReferenceExpression) {
            final Expression body = this.navigation.firstOwnedExpression(argument);
            if (body != null) {
                return body;
            }
        }
        return argument;
    }

    /**
     * Finds the first named feature in preorder, including the supplied element, or returns null when absent. Used to
     * find the declared variable of a lambda.
     *
     * @param element
     *            the subtree root, or {@code null}
     * @return the named feature, or {@code null}
     */
    Element firstNamedFeature(final Element element) {
        Element namedFeature = null;
        if (element instanceof final Feature feature
                && !this.referenceResolver.declaredName(feature).isBlank()) {
            namedFeature = feature;
        } else if (element != null) {
            namedFeature = this.firstNamedFeatureInRelationships(element);
        }
        return namedFeature;
    }

    private Element firstNamedFeatureInRelationships(final Element element) {
        for (final Relationship relationship : this.navigation.ownedRelationships(element)) {
            for (final Element related : this.navigation.ownedRelatedElements(relationship)) {
                final Element namedFeature = this.firstNamedFeature(related);
                if (namedFeature != null) {
                    return namedFeature;
                }
            }
        }
        return null;
    }

    /**
     * Writes the private features a quantifier body declares besides its variable (those with a value or bindings),
     * which the grammar accepts before the result expression.
     */
    private String quantifierLocalDeclarations(
            final Element quantifiedExpression, final List<Element> parameters,
            final Expression predicate) {
        final ArrayList<Element> declarations = new ArrayList<>();
        this.collectQuantifierLocalDeclarations(quantifiedExpression, predicate,
                parameters, declarations);
        return declarations.stream().map(this::quantifierLocalDeclaration)
                .filter(text -> !text.isBlank())
                .collect(Collectors.joining());
    }

    private void collectQuantifierLocalDeclarations(final Element element,
            final Expression predicate, final List<Element> parameters,
            final List<Element> declarations) {
        for (final Relationship relationship : this.navigation.ownedRelationships(element)) {
            for (final Element related : this.navigation
                    .ownedRelatedElements(relationship)) {
                if (related == predicate) {
                    continue;
                }
                if (related instanceof Feature
                        && !parameters.contains(related)
                        && (!this.navigation.featureValues(related).isEmpty()
                                || !this.quantifierLocalBody(related)
                                        .isBlank())) {
                    declarations.add(related);
                }
                this.collectQuantifierLocalDeclarations(related, predicate,
                        parameters, declarations);
            }
        }
    }

    /** Writes {@code private [attribute] name : T = value;} or the same with a binding body. */
    private String quantifierLocalDeclaration(final Element feature) {
        final String name = this.referenceResolver.declaredName(feature);
        if (name.isBlank()) {
            return "";
        }
        final List<String> types = this.specializationTargets.featureTypeNames(feature,
                this.context.danglingReferenceIndex(),
                this.context.nameRenderer()::referenceName);
        String type = "";
        if (!types.isEmpty()) {
            type = SysMLTokens.TYPE_SEPARATOR_PREFIX
                    + this.referenceResolver.referenceNames(types);
        }
        final String value = this.firstFeatureValueText(feature);
        String assignment = "";
        if (!value.isBlank()) {
            assignment = SysMLTokens.EQUALS_INFIX + value;
        }
        String keyword = "";
        if (this.navigation.isMetaclass(feature,
                SysMLPackage.eINSTANCE.getAttributeUsage())) {
            keyword = SysMLTokens.ATTRIBUTE_PREFIX;
        }
        final String body = this.quantifierLocalBody(feature);
        String terminator = "";
        if (body.isBlank()) {
            terminator = SysMLTokens.STATEMENT_TERMINATOR;
        }
        return SysMLTokens.PRIVATE_PREFIX + keyword + name + type + assignment
                + body + terminator + SysMLTokens.SPACE;
    }

    /** Writes the {@code { :>> f = v; }} body of a local declaration, or an empty string when it has none. */
    private String quantifierLocalBody(final Element feature) {
        final List<String> bindings = this.navigation
                .featureMembershipElements(feature).stream()
                .map(this::quantifierLocalBinding)
                .filter(text -> !text.isBlank()).toList();
        if (bindings.isEmpty()) {
            return "";
        }
        return SysMLTokens.OPEN_BRACE_INFIX
                + String.join(SysMLTokens.SPACE, bindings)
                + SysMLTokens.CLOSE_BRACE_SUFFIX;
    }

    /** Writes one {@code :>> f = v;} or {@code f = v;} binding of a local declaration body. */
    private String quantifierLocalBinding(final Element feature) {
        final List<String> redefinedFeatures = this.specializationTargets.redefinedFeatureNames(
                feature, this.context.danglingReferenceIndex(),
                this.context.nameRenderer()::referenceName);
        final String name = this.referenceResolver.declaredName(feature);
        String target = name;
        if (!redefinedFeatures.isEmpty()) {
            target = SysMLTokens.REDEFINITION_PREFIX
                    + this.referenceResolver.referenceNames(redefinedFeatures);
        }
        final String value = this.firstFeatureValueText(feature);
        if (target.isBlank() || value.isBlank()) {
            return "";
        }
        return target + SysMLTokens.EQUALS_INFIX + value
                + SysMLTokens.STATEMENT_TERMINATOR;
    }

    private String firstFeatureValueText(final Element feature) {
        return this.navigation.featureValues(feature).stream()
                .map(this.navigation::featureValueExpression)
                .map(this.expressionRenderer::expressionText)
                .filter(text -> !text.isBlank())
                .findFirst().orElse("");
    }
}
