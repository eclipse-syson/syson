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
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.syson.sysml.parser.adapter.ITextAdapter;
import org.eclipse.syson.sysml.parser.adapter.ParsedComment;
import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.SerializationMode;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.eclipse.syson.sysml.serializer.model.ParserAdapterQueries;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.syntax.CommentRenderer;
import org.eclipse.syson.sysml.serializer.syntax.ParserFragments;
import org.eclipse.syson.sysml.serializer.syntax.SourceSyntax;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.ConstructorExpression;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Expression;
import org.omg.sysml.lang.sysml.FeatureChainExpression;
import org.omg.sysml.lang.sysml.FeatureReferenceExpression;
import org.omg.sysml.lang.sysml.InvocationExpression;
import org.omg.sysml.lang.sysml.MetadataAccessExpression;
import org.omg.sysml.lang.sysml.OperatorExpression;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.lang.sysml.TriggerInvocationExpression;

/**
 * Dispatches expression syntax to literal, path, invocation, and operator renderers.
 * Operator rendering owns precedence; this coordinator preserves comment and reference context.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
public final class ExpressionRenderer {

    private static final String METADATA_ACCESS_SUFFIX = ".metadata";

    private final ReferenceResolver referenceResolver;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final ParserAdapterQueries adapters;

    private final ExpressionReferenceCollector referenceCollector;

    private final LiteralExpressionRenderer literalRenderer;

    private final FeaturePathExpressionRenderer featurePathRenderer;

    private final OperatorOperandResolver operatorOperandResolver;

    private final InvocationExpressionRenderer invocationRenderer;

    private final OperatorExpressionRenderer operatorRenderer;

    /**
     * Creates the expression renderer of one run and the form-specific renderers it dispatches to.
     *
     * @param referenceResolver
     *            renders reference names and paths
     * @param commentRenderer
     *            renders comments kept inside expressions
     * @param context
     *            the run's context
     * @param relationshipsText
     *            renders the members of a full body expression
     */
    public ExpressionRenderer(final ReferenceResolver referenceResolver,
            final CommentRenderer commentRenderer,
            final SerializationContext context, final Function<List<Relationship>, String> relationshipsText) {
        this.referenceResolver = referenceResolver;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.adapters = context.modelQueries().adapters();
        this.referenceCollector = new ExpressionReferenceCollector(this::expressionText, referenceResolver, context);
        this.literalRenderer = new LiteralExpressionRenderer(this.adapters,
                context.options().mode() == SerializationMode.ROUND_TRIP);
        this.featurePathRenderer = new FeaturePathExpressionRenderer(this, referenceResolver, context, this.referenceCollector);
        this.operatorOperandResolver = new OperatorOperandResolver(context);
        this.invocationRenderer = new InvocationExpressionRenderer(this, referenceResolver, commentRenderer, context, this.operatorOperandResolver, relationshipsText);
        this.operatorRenderer = new OperatorExpressionRenderer(this, referenceResolver, this.invocationRenderer, this.operatorOperandResolver, context);
    }

    /**
     * Renders a top-level expression with its comments; a null expression produces
     * empty text. A leading line comment is terminated so the expression text cannot be swallowed by it.
     *
     * @param expression
     *            the expression, or {@code null}
     * @return the rendered text
     */
    public String expressionText(final Expression expression) {
        return this.expressionText(expression, "", false);
    }

    /**
     * Renders the expression bound as the source of an assignment target ({@code assign source.feature := value},
     * grammar {@code targetExpression}). Only a base expression or a postfix operation fits there, so a prefix or infix
     * form is grouped, and so is a feature chain: ungrouped, {@code assign a.b.x} binds {@code a} and assigns
     * {@code b.x}.
     *
     * @param source
     *            the bound expression, or {@code null}
     * @return the rendered text, grouped when the grammar requires it
     */
    public String assignmentTargetSourceText(final Expression source) {
        final String text = this.expressionText(source);
        final boolean ungroupedFeatureChain = source instanceof FeatureChainExpression
                && !ExpressionPathSupport.isEnclosedInParentheses(text);
        if (ungroupedFeatureChain || OperatorPrecedence.mustParenthesizePostfixSource(source, text)) {
            return ExpressionPathSupport.parenthesized(text);
        }
        return text;
    }

    /**
     * Renders an operand and its own comments in its parent's precedence context. Parentheses written
     * in the source are restored unless the text is already fully enclosed by one pair, and an empty {@code ()}
     * keeps only its comments.
     *
     * @param expression
     *            the operand, or {@code null}
     * @param parentOperator
     *            the enclosing operator, or an empty string at top level
     * @param rightOperand
     *            whether the operand is on the right of {@code parentOperator}, which decides associativity parentheses
     * @return the rendered text
     */
    public String expressionText(final Expression expression,
            final String parentOperator, final boolean rightOperand) {
        return this.expressionLeadingCommentText(expression)
                + this.expressionTextWithoutLeadingComments(expression, parentOperator, rightOperand);
    }

    /**
     * Renders a top-level expression without the comments attached before it, for an expression written as an element
     * whose dispatch already wrote them, such as the result expression of a calculation body.
     *
     * @param expression
     *            the expression, or {@code null}
     * @return the rendered text
     */
    public String standaloneExpressionText(final Expression expression) {
        return this.expressionTextWithoutLeadingComments(expression, "", false);
    }

    /** Renders an expression in its parent's precedence context, with its source parentheses but without its leading comments. */
    private String expressionTextWithoutLeadingComments(final Expression expression,
            final String parentOperator, final boolean rightOperand) {
        String text;
        if (expression == null) {
            text = "";
        } else if (this.adapters.hasTextAdapter(expression,
                ITextAdapter.EMPTY_PARENTHESIZED_EXPRESSION)) {
            text = this.directTrailingCommentText(expression);
        } else if (this.literalRenderer.supports(expression)) {
            text = this.literalRenderer.render(expression);
        } else {
            text = this.structuredExpressionText(expression, parentOperator,
                    rightOperand);
        }
        if (expression != null
                && this.adapters.hasTextAdapter(expression, ITextAdapter.PARENTHESIZED_EXPRESSION)
                && !ExpressionPathSupport.isEnclosedInParentheses(text)) {
            text = ExpressionPathSupport.parenthesized(text);
        }
        return text;
    }

    private String structuredExpressionText(final Expression expression,
            final String parentOperator, final boolean rightOperand) {
        String text;
        if (expression instanceof final FeatureReferenceExpression reference) {
            text = this.featureReferenceText(reference);
        } else if (expression instanceof final FeatureChainExpression chain) {
            text = this.featurePathRenderer.renderFeatureChain(chain);
        } else if (expression instanceof final TriggerInvocationExpression trigger) {
            text = this.renderTriggerInvocationExpression(trigger);
        } else if (expression instanceof ConstructorExpression) {
            text = this.invocationRenderer.renderConstructor(expression);
        } else if (expression instanceof MetadataAccessExpression) {
            text = this.renderMetadataAccessExpression(expression);
        } else if (expression instanceof final OperatorExpression operator) {
            text = this.operatorRenderer.render(operator, parentOperator,
                    rightOperand);
        } else if (expression instanceof InvocationExpression) {
            text = this.invocationRenderer.renderInvocation(expression);
        } else {
            text = this.renderFallbackExpression(expression);
        }
        return text;
    }

    /** Writes a feature reference: the whole body of a standalone body expression, otherwise the referenced path. */
    private String featureReferenceText(final FeatureReferenceExpression reference) {
        if (this.expressionBody(reference) instanceof final Expression body) {
            return this.bodyExpressionText(body);
        }
        return this.featurePathRenderer.renderFeatureReference(reference);
    }

    /**
     * Returns the body of a standalone body expression ({@code baseExpression: bodyExpression}), which the parser
     * builds as a feature reference owning a plain {@code Expression} with a result by feature membership; a function
     * reference ({@code x->f g}) owns a plain {@code Expression} without result. Returns {@code null} for any other
     * expression.
     */
    private Element expressionBody(final FeatureReferenceExpression reference) {
        return this.navigation.featureMembershipElements(reference).stream()
                .filter(element -> this.navigation.isMetaclass(element, SysMLPackage.eINSTANCE.getExpression()))
                .filter(element -> this.navigation.resultExpression(element) != null)
                .findFirst().orElse(null);
    }

    /** Writes a body expression with its braces and parameters, {@code { in v; result }}, not only its result. */
    private String bodyExpressionText(final Expression body) {
        final String content = this.operatorRenderer.bodyExpressionText(body);
        if (content.isBlank()) {
            return "";
        }
        return SysMLTokens.OPEN_BRACE_PREFIX + content + SysMLTokens.CLOSE_BRACE_SUFFIX;
    }

    /**
     * Returns the references of an expression subtree that are visible in its rendered text.
     *
     * @param expression
     *            the expression
     * @return an immutable list in text order, possibly empty
     */
    public List<TextOutput.Reference> referenceOccurrences(
            final Expression expression) {
        return this.referenceCollector.collect(expression);
    }

    /**
     * Returns the references of {@code element}'s subtree that are visible in caller-provided text.
     *
     * @param element
     *            the element whose subtree owns the references
     * @param text
     *            the rendered text
     * @return an immutable list in text order, possibly empty
     */
    public List<TextOutput.Reference> referenceOccurrences(final Element element,
            final String text) {
        return this.referenceCollector.collect(element, text);
    }

    /**
     * Like {@link #referenceOccurrences(Element, String)}, also linking {@code fallbackTarget}, as for a reference
     * path whose target is known only to the caller.
     *
     * @param element
     *            the element whose subtree owns the references
     * @param text
     *            the rendered text
     * @param fallbackTarget
     *            the target to link as well, or {@code null}
     * @return an immutable list in text order, possibly empty
     */
    public List<TextOutput.Reference> referenceOccurrences(final Element element,
            final String text, final EObject fallbackTarget) {
        return this.referenceCollector.collect(element, text, fallbackTarget);
    }

    /**
     * Replaces parser spellings that no longer name their target, because a dangling reference was resolved to an
     * element with another name after parsing. Text is returned unchanged when nothing was resolved.
     *
     * @param element
     *            the element whose subtree owns the references
     * @param text
     *            text containing parser spellings
     * @return the updated text
     */
    public String updatedReferenceText(final Element element, final String text) {
        return this.referenceCollector.updatedText(element, text);
    }

    /**
     * Keeps the candidates that actually occur in {@code text}; see
     * {@link ExpressionReferenceCollector#visibleOccurrences(String, List)}.
     *
     * @param text
     *            the rendered text
     * @param candidates
     *            the candidate references
     * @return an immutable list in text order
     */
    public List<TextOutput.Reference> visibleReferenceOccurrences(final String text,
            final List<TextOutput.Reference> candidates) {
        return this.referenceCollector.visibleOccurrences(text, candidates);
    }

    /**
     * Splits one reference spelling into segment occurrences, linking the segments to the target and, where the
     * ownership chain matches, to its owning namespaces.
     *
     * @param text
     *            the reference spelling
     * @param target
     *            the referenced object, or {@code null}
     * @return one occurrence per segment
     */
    public List<TextOutput.Reference> atomicReferenceOccurrences(final String text,
            final EObject target) {
        return this.referenceCollector.atomicReferenceOccurrences(text,
                target);
    }

    /**
     * Like {@link #atomicReferenceOccurrences(String, EObject)}, but first asks the caller's reference path resolver
     * for the target of every segment of the reference held by {@code referenceContext}, so alias paths link exactly.
     *
     * @param text
     *            the reference spelling
     * @param target
     *            the terminal target, or {@code null}
     * @param referenceContext
     *            the object holding the reference
     * @param reference
     *            the EMF reference holding {@code target}
     * @return one occurrence per segment
     */
    public List<TextOutput.Reference> atomicReferenceOccurrences(final String text,
            final EObject target, final EObject referenceContext,
            final EReference reference) {
        return this.referenceCollector.atomicReferenceOccurrences(text,
                target, referenceContext, reference);
    }

    /**
     * Writes {@code at}, {@code after} or {@code when} triggers from their argument value, unwrapping the
     * feature-reference wrapper of a change condition without descending into the condition itself.
     */
    private String renderTriggerInvocationExpression(
            final TriggerInvocationExpression expression) {
        final var triggerKind = expression.getKind();
        String triggerKeyword = "";
        if (triggerKind != null) {
            triggerKeyword = triggerKind.toString();
        }
        final List<Expression> operands = this.operatorOperandResolver.operands(expression);
        Expression argumentExpression = null;
        if (!operands.isEmpty()) {
            argumentExpression = operands.getFirst();
        }
        final String argumentText = this.expressionText(argumentExpression);
        if (triggerKeyword.isBlank()) {
            return argumentText;
        }
        return triggerKeyword + SysMLTokens.SPACE + argumentText;
    }

    /**
     * Writes {@code x.metadata}. The suffix is omitted where the grammar implies it: operands of {@code @} and
     * {@code @@}, and a standalone metadata-reference fragment.
     */
    private String renderMetadataAccessExpression(final Expression expression) {
        final String target = this.referenceResolver.firstDirectRelationshipTarget(
                expression, SysMLPackage.eINSTANCE.getMembership());
        if (target.isBlank() || this.isImplicitMetadataReference(expression)
                || SourceSyntax.isRootParserEntry(this.context.options(), expression,
                        ParserFragments.METADATA_REFERENCE)) {
            return target;
        }
        return target + METADATA_ACCESS_SUFFIX;
    }

    private boolean isImplicitMetadataReference(final Expression expression) {
        EObject parent = expression.eContainer();
        while (parent != null && !(parent instanceof Expression)) {
            parent = parent.eContainer();
        }
        if (parent instanceof final OperatorExpression operator) {
            return SysMLTokens.META_OPERATOR.equals(operator.getOperator())
                    || SysMLTokens.METACLASS_OPERATOR
                            .equals(operator.getOperator());
        }
        return false;
    }

    /** Renders a function-reference expression by its own typing, or as empty text when it has none. */
    private String renderFallbackExpression(final Expression expression) {
        return this.referenceResolver.firstDirectRelationshipTarget(expression, SysMLPackage.eINSTANCE.getFeatureTyping());
    }

    /**
     * Returns the text of a constructor expression ({@code new T(...)}) given directly or as the value of one of the
     * expression's feature values; used when such a value is the source of a feature chain.
     *
     * @param expression
     *            the expression to inspect
     * @return the constructor text, or an empty string when there is none
     */
    public String constructorFeatureValueSource(final Expression expression) {
        if (expression instanceof ConstructorExpression) {
            return this.expressionText(expression);
        }
        return this.navigation.featureValues(expression).stream()
                .map(this.navigation::featureValueExpression)
                .filter(ConstructorExpression.class::isInstance)
                .map(this::expressionText).filter(text -> !text.isBlank())
                .findFirst().orElse("");
    }

    /**
     * Returns the comments the parser attached after {@code object} inside an expression, concatenated; line comments
     * are terminated so following text stays outside the comment.
     *
     * @param object
     *            the expression part
     * @return the comment text, or an empty string
     */
    public String directTrailingCommentText(final EObject object) {
        final List<ParsedComment> comments = this.adapters
                .trailingComments(object);
        if (comments.isEmpty()) {
            return "";
        }
        return comments.stream().map(ParsedComment::text)
                .map(this::expressionCommentText)
                .collect(Collectors.joining());
    }

    /**
     * Returns all comments attached directly before {@code object}, in source order. Nested expressions render their
     * own comments, so they must not be lifted before the containing expression.
     *
     * @param object
     *            the expression part
     * @return the comment text, or an empty string
     */
    public String expressionLeadingCommentText(final EObject object) {
        return this.adapters.leadingComments(object).stream()
                .map(ParsedComment::text)
                .map(this::expressionCommentText)
                .collect(Collectors.joining());
    }

    /** Returns comments before the direct reference membership, at the point where its target name is rendered. */
    String referenceLeadingCommentText(final Element expression) {
        return this.navigation.ownedRelationships(expression).stream()
                .filter(relationship -> this.navigation.isMetaclass(relationship, SysMLPackage.eINSTANCE.getMembership())
                        || this.navigation.isMetaclass(relationship, SysMLPackage.eINSTANCE.getOwningMembership()))
                .map(this::expressionLeadingCommentText)
                .collect(Collectors.joining());
    }

    private String expressionCommentText(final String comment) {
        if (comment.startsWith(SysMLTokens.LINE_COMMENT_PREFIX)
                && !comment.endsWith(SysMLTokens.LINE_FEED)
                && !comment.endsWith(SysMLTokens.CARRIAGE_RETURN)) {
            return comment + System.lineSeparator();
        }
        return comment;
    }
}
