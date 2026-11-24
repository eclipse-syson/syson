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
import org.eclipse.syson.sysml.serializer.model.NestedTargetSearch;
import org.eclipse.syson.sysml.serializer.model.SafeEmfAccess;
import org.eclipse.syson.sysml.serializer.output.ExpressionAndReferenceWriter;
import org.eclipse.syson.sysml.serializer.output.TextOutput;
import org.eclipse.syson.sysml.serializer.references.ReferenceResolver;
import org.eclipse.syson.sysml.serializer.tokens.SysMLTokens;
import org.omg.sysml.lang.sysml.AssignmentActionUsage;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Expression;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.NullExpression;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Writes assignment actions, as statements ({@code assign x.y := v;}) and as inline texts for transition effects and
 * state subactions. The model splits the target into a target argument (the occurrence, {@code x}) and a referent
 * membership (the assigned feature, {@code y}); this class rebuilds the path the user wrote from both.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
final class AssignmentActionRenderer {

    private final ReferenceResolver referenceResolver;

    private final ExpressionAndReferenceWriter referenceWriter;

    private final SerializationContext context;

    private final ModelNavigation navigation;

    private final NestedTargetSearch nestedTargets;

    private final ActionExpressionSupport expressionSupport;

    private final ActionNodePrefixRenderer prefixRenderer;

    private final ParameterActionBodyWriter bodyWriter;

    /**
     * Creates the renderer.
     *
     * @param referenceResolver
     *            resolves declared and referenced names
     * @param referenceWriter
     *            writes expressions and references with traceability metadata
     * @param context
     *            the serialization context
     * @param expressionSupport
     *            shared helper for expression-valued parameters
     * @param prefixRenderer
     *            writes the action node prefix of statements
     * @param bodyWriter
     *            writes the statement body or end
     */
    AssignmentActionRenderer(final ReferenceResolver referenceResolver,
            final ExpressionAndReferenceWriter referenceWriter,
            final SerializationContext context,
            final ActionExpressionSupport expressionSupport,
            final ActionNodePrefixRenderer prefixRenderer,
            final ParameterActionBodyWriter bodyWriter) {
        this.referenceResolver = referenceResolver;
        this.referenceWriter = referenceWriter;
        this.context = context;
        this.navigation = context.modelQueries().navigation();
        this.nestedTargets = context.modelQueries().nestedTargets();
        this.expressionSupport = expressionSupport;
        this.prefixRenderer = prefixRenderer;
        this.bodyWriter = bodyWriter;
    }

    /**
     * Writes {@code [prefix] [action name] assign target := value;}, with the {@code ActionNodePrefix} of the node. When no target path can be rebuilt, the resolved referent
     * name is used so the statement stays parseable; {@code null} values are written as the {@code null} literal.
     *
     * @param assignmentActionUsage
     *            the assignment action
     */
    void serializeAssignment(
            final AssignmentActionUsage assignmentActionUsage) {
        final TextOutput output = this.context.output();
        this.prefixRenderer.writeActionNodePrefix(assignmentActionUsage);
        output.token(SysMLTokens.ASSIGN_KEYWORD).space();
        String target = this.assignmentTargetName(assignmentActionUsage);
        if (target.isBlank()) {
            final Feature referent = SafeEmfAccess.get(assignmentActionUsage::getReferent, null);
            target = this.context.nameRenderer().referenceName(referent);
        }
        this.referenceWriter.writeReferenceText(output, target, assignmentActionUsage)
                .space().token(SysMLTokens.ASSIGNMENT_OPERATOR).space();
        final Expression value = this.assignmentValue(assignmentActionUsage);
        this.expressionSupport.writeParameterText(output, this.assignmentValueText(value),
                value);
        this.bodyWriter.writeBodyOrStatementEnd(assignmentActionUsage);
    }

    /**
     * Returns the inline {@code assign target := value} text, without action declaration, statement terminator or
     * newline. Unlike {@link #serializeAssignment(AssignmentActionUsage)}, there is no fallback to the referent name.
     *
     * @param assignmentActionUsage
     *            the assignment action
     * @return the inline assignment text
     */
    String assignmentText(
            final AssignmentActionUsage assignmentActionUsage) {
        final String target = this.assignmentTargetName(assignmentActionUsage);
        return SysMLTokens.ASSIGN_PREFIX + target + SysMLTokens.ASSIGNMENT_INFIX
                + this.assignmentValueText(
                        this.assignmentValue(assignmentActionUsage));
    }

    /**
     * Rebuilds the target as the grammar splits it, {@code [source .] feature}: the source is the expression bound to
     * the target parameter, the feature is the member the action assigns. Each part is read from its own place in the
     * model and never compared with the other, so that {@code assign charge.charge := 0} keeps both segments.
     */
    private String assignmentTargetName(final AssignmentActionUsage assignmentActionUsage) {
        final String source = this.targetArgumentSource(assignmentActionUsage);
        final String feature = this.assignedFeatureName(assignmentActionUsage);
        if (source.isBlank() || feature.isBlank()) {
            return source + feature;
        }
        return source + SysMLTokens.PATH_SEPARATOR + feature;
    }

    /** Returns the assigned feature: the member referenced by name, else the feature chain the action owns. */
    private String assignedFeatureName(final AssignmentActionUsage assignmentActionUsage) {
        final String member = this.referenceResolver.firstDirectRelationshipTarget(assignmentActionUsage,
                SysMLPackage.eINSTANCE.getMembership());
        if (!member.isBlank()) {
            return member;
        }
        return this.nestedTargets.ownedFeatureChainName(assignmentActionUsage, this.context.danglingReferenceIndex());
    }

    /** Returns the source text of the first parameter (the target argument), or an empty string without parameters. */
    private String targetArgumentSource(final AssignmentActionUsage assignmentActionUsage) {
        final List<Element> parameters = this.navigation.parameterElements(assignmentActionUsage);
        if (parameters.isEmpty()) {
            return "";
        }
        return this.parameterSourceText(parameters.get(0));
    }

    /**
     * Returns the text of the expression bound to the target parameter, written as an expression so that its own
     * syntax, such as the parentheses of {@code (a).x}, is kept. Only when the model holds no expression text is the
     * parser spelling of an unresolved target used, as a reference name.
     */
    private String parameterSourceText(final Element parameter) {
        String source = this.referenceWriter.assignmentTargetSourceText(this.navigation.firstOwnedExpression(parameter));
        if (source.isBlank()) {
            source = this.referenceResolver.referenceName(this.nestedTargets.firstDanglingTarget(parameter,
                    this.context.danglingReferenceIndex()));
        }
        return source;
    }

    /**
     * Returns the assigned value: the expression of the second parameter, else the last operand, else {@code null} for
     * an incomplete model-built assignment. A missing value is written as empty text rather than as an invented
     * literal, so the output shows the gap instead of reparsing into a different model.
     */
    private Expression assignmentValue(
            final AssignmentActionUsage assignmentActionUsage) {
        final List<Element> parameters = this.navigation
                .parameterElements(assignmentActionUsage);
        Expression value = null;
        if (parameters.size() > 1) {
            value = this.navigation.firstOwnedExpression(parameters.get(1));
        }
        final List<Expression> operands = this.navigation
                .operands(assignmentActionUsage);
        if (value == null && !operands.isEmpty()) {
            value = operands.get(operands.size() - 1);
        }
        return value;
    }

    /** Returns the value text, writing a {@code NullExpression} as the {@code null} literal rather than {@code ()}. */
    private String assignmentValueText(final Expression value) {
        if (value instanceof NullExpression) {
            return SysMLTokens.NULL_LITERAL;
        }
        return this.referenceWriter.expressionText(value);
    }

}
