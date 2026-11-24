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

import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.model.SpecializationTargetNames;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.rendering.declarations.DeclarationRenderer;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Expression;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Writes the control-structure actions {@code while}/{@code loop ... until}, {@code if ... else} and
 * {@code for ... in}. Their condition, sequence and body are stored as parameters of the action; this class maps them
 * back to the positions of the textual grammar.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
final class ControlActionRenderer {

    private static final String TYPE_REFERENCE = "type";

    private final DeclarationRenderer declarations;

    private final ReferenceResolver referenceResolver;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final SpecializationTargetNames specializationTargets;

    private final ActionExpressionSupport expressionSupport;

    private final ActionNodePrefixRenderer prefixRenderer;

    /**
     * Creates the renderer.
     *
     * @param declarations
     *            writes namespace bodies, comments, prefix modifiers and specializations
     * @param referenceResolver
     *            resolves declared and referenced names
     * @param referenceWriter
     *            writes expressions and references with traceability metadata
     * @param context
     *            the serialization context
     * @param expressionSupport
     *            shared helper for expression-valued parameters
     * @param prefixRenderer
     *            writes action node prefixes and body parameter declarations
     */
    ControlActionRenderer(final DeclarationRenderer declarations,
            final ReferenceResolver referenceResolver,
            final ExpressionAndReferenceWriter referenceWriter,
            final SerializationContext context,
            final ActionExpressionSupport expressionSupport,
            final ActionNodePrefixRenderer prefixRenderer) {
        this.declarations = declarations;
        this.referenceResolver = referenceResolver;
        this.referenceWriter = referenceWriter;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.specializationTargets = context.modelQueries().specializationTargets();
        this.expressionSupport = expressionSupport;
        this.prefixRenderer = prefixRenderer;
    }

    /**
     * Writes {@code [action name] while cond { ... }} or {@code loop { ... }}, followed by {@code until cond;} when an
     * until expression exists.
     *
     * @param whileLoopActionUsage
     *            the while loop action usage
     */
    void serializeWhileLoop(final Element whileLoopActionUsage) {
        final TextOutput output = this.context.output();
        final WhileLoopPlan plan = this.whileLoopPlan(whileLoopActionUsage);
        this.writeActionNodePrefix(whileLoopActionUsage);
        if (!plan.conditionText().isBlank()) {
            output.token(SysMLTokens.WHILE_KEYWORD).space();
            this.expressionSupport.writeParameterText(output, plan.conditionText(),
                    plan.conditionExpression());
        } else {
            output.token(SysMLTokens.LOOP_KEYWORD);
        }
        this.serializeWhileLoopBody(whileLoopActionUsage, plan);
        if (!plan.untilText().isBlank()) {
            output.token(SysMLTokens.UNTIL_KEYWORD).space();
            this.expressionSupport.writeParameterText(output, plan.untilText(),
                    plan.untilExpression());
            output.token(SysMLTokens.STATEMENT_TERMINATOR).newline();
        }
    }

    /**
     * Reads the loop parameters: the first parameter when it is an expression is the while condition, the first action
     * parameter is the body, and the last expression parameter after the first slot is the until condition. The slots
     * are told apart by position, not by text, because the until condition may repeat the while condition.
     */
    private WhileLoopPlan whileLoopPlan(final Element whileLoopActionUsage) {
        final List<Element> parameters = this.navigation
                .parameterElements(whileLoopActionUsage);
        Expression conditionExpression = null;
        if (!parameters.isEmpty()
                && parameters.get(0) instanceof final Expression expression) {
            conditionExpression = expression;
        }
        final String condition = this.referenceWriter.expressionText(conditionExpression);
        final Element bodyAction = this.firstActionParameter(parameters);
        final Expression untilExpression = parameters.stream().skip(1)
                .filter(Expression.class::isInstance)
                .map(Expression.class::cast).reduce((first, second) -> second)
                .orElse(null);
        return new WhileLoopPlan(conditionExpression, condition, bodyAction,
                untilExpression, this.referenceWriter.expressionText(untilExpression));
    }

    /** Writes the loop body: the body action parameter, or the loop's own members when there is no body action. */
    private void serializeWhileLoopBody(final Element whileLoopActionUsage,
            final WhileLoopPlan plan) {
        final TextOutput output = this.context.output();
        if (plan.bodyAction() == null) {
            if (!plan.conditionText().isBlank()) {
                output.space();
            }
            this.serializeActionBodyParameter(whileLoopActionUsage);
            return;
        }
        output.space();
        this.serializeActionBodyParameter(plan.bodyAction());
    }

    /**
     * Writes {@code if guard { ... }} with an optional {@code else { ... }} or chained {@code else if}. The guard is
     * the first expression parameter, the then-branch the first action parameter and the else-branch the second one.
     *
     * @param ifActionUsage
     *            the if action usage
     */
    void serializeIf(final Element ifActionUsage) {
        final TextOutput output = this.context.output();
        final List<Element> parameters = this.navigation.parameterElements(ifActionUsage);
        final List<Expression> expressions = parameters.stream()
                .filter(Expression.class::isInstance).map(Expression.class::cast).toList();
        final String guard = expressions.stream().map(this.referenceWriter::expressionText)
                .filter(text -> !text.isBlank()).findFirst().orElse("");
        final Expression guardExpression = expressions.stream().findFirst().orElse(null);
        this.writeActionNodePrefix(ifActionUsage);
        output.token(SysMLTokens.IF_KEYWORD).space();
        this.expressionSupport.writeParameterText(output, guard, guardExpression);
        output.space();
        final List<Element> branches = parameters.stream()
                .filter(element -> this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getActionUsage())
                        || this.isIfAction(element))
                .toList();
        Element thenBranch = ifActionUsage;
        if (!branches.isEmpty()) {
            thenBranch = branches.get(0);
        }
        this.serializeActionBodyParameter(thenBranch);
        if (branches.size() > 1) {
            output.space().token(SysMLTokens.ELSE_KEYWORD).space();
            final Element elseBranch = branches.get(1);
            if (this.isIfAction(elseBranch)) {
                this.serializeIf(elseBranch);
            } else {
                this.serializeActionBodyParameter(elseBranch);
            }
        }
    }

    /**
     * Writes the {@code ActionNodePrefix} of a control node or a {@code terminate}.
     *
     * @param controlNode
     *            the action node whose prefix is written
     */
    void writeActionNodePrefix(final Element controlNode) {
        this.prefixRenderer.writeActionNodePrefix(controlNode);
    }

    private boolean isIfAction(final Element element) {
        return this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getIfActionUsage());
    }

    /** Returns the first parameter that is a plain action, which holds the body block of a loop, or {@code null}. */
    private Element firstActionParameter(final List<Element> parameters) {
        return parameters.stream()
                .filter(element -> this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getActionUsage()))
                .findFirst().orElse(null);
    }

    /**
     * Writes {@code for v : T in seq { ... }}. The loop variable is the first named owned feature and the sequence the
     * first parameter value; the body is the body action's block, or the loop's own members without body action.
     *
     * @param forLoopActionUsage
     *            the for loop action usage
     */
    void serializeForLoop(final Element forLoopActionUsage) {
        final TextOutput output = this.context.output();
        final Element variable = this.navigation
                .featureMembershipElements(forLoopActionUsage).stream()
                .filter(element -> !this.referenceResolver.declaredName(element)
                        .isBlank())
                .findFirst().orElse(null);
        final String sequence = this.expressionSupport
                .firstParameterExpressionText(forLoopActionUsage);
        this.writeActionNodePrefix(forLoopActionUsage);
        output.token(SysMLTokens.FOR_KEYWORD).space()
                .token(this.referenceResolver.declaredName(variable));
        this.writeForLoopVariableType(output, variable);
        output.space().token(SysMLTokens.IN_KEYWORD).space();
        this.referenceWriter.writeReferenceText(output, sequence, forLoopActionUsage)
                .space();
        Element bodyOwner = this.firstActionParameter(this.navigation.parameterElements(forLoopActionUsage));
        if (bodyOwner == null) {
            bodyOwner = forLoopActionUsage;
        }
        this.serializeActionBodyParameter(bodyOwner);
    }

    /** Writes the {@code : T} typing of the loop variable when it is typed. */
    private void writeForLoopVariableType(final TextOutput output,
            final Element variable) {
        if (variable == null) {
            return;
        }
        final List<String> types = this.specializationTargets.featureTypeNames(variable,
                this.context.danglingReferenceIndex(),
                this.context.nameRenderer()::referenceName);
        if (!types.isEmpty()) {
            output.space().token(SysMLTokens.TYPE_SEPARATOR).space();
            this.referenceWriter.writeRelationshipReferences(output,
                    this.referenceResolver.referenceNames(types), variable,
                    SysMLPackage.eINSTANCE.getFeatureTyping(), TYPE_REFERENCE);
        }
    }

    /**
     * Writes an {@code ActionBodyParameter}: the optional {@code action} usage declaration of the body action, then its
     * {@code { ... }} block with its members and trailing comments, then a newline. When the control node itself owns
     * the body, its declaration belongs to the node prefix and is not repeated.
     */
    private void serializeActionBodyParameter(final Element bodyOwner) {
        final TextOutput output = this.context.output();
        if (this.navigation.isMetaclass(bodyOwner, SysMLPackage.eINSTANCE.getActionUsage())
                && this.prefixRenderer.hasActionUsageDeclaration(bodyOwner)) {
            this.prefixRenderer.writeActionUsageDeclaration(bodyOwner);
            output.space();
        }
        output.beginBlock();
        this.declarations.serializeNamespaceBody(bodyOwner);
        this.declarations.serializeTrailingComments(bodyOwner);
        output.endBlock().newline();
    }

    /** Condition, body and until parts of a while loop, with the expressions kept for traceability. */
    private record WhileLoopPlan(Expression conditionExpression,
            String conditionText, Element bodyAction, Expression untilExpression,
            String untilText) {
    }
}
