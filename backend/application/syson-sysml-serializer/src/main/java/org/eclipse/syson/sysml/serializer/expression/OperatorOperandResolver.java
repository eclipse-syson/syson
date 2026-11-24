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

import org.eclipse.syson.sysml.serializer.SerializationContext;
import org.eclipse.syson.sysml.serializer.model.ModelNavigation;
import org.omg.sysml.lang.sysml.Expression;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureReferenceExpression;
import org.omg.sysml.lang.sysml.InvocationExpression;
import org.omg.sysml.lang.sysml.SysMLPackage;

/**
 * Reads the operands of operator and invocation expressions from their parameter values, where the parser stores
 * them, unwrapping the feature reference expressions that {@code *ExpressionReference} grammar rules insert.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
final class OperatorOperandResolver {

    private final ModelNavigation navigation;

    /**
     * Creates a resolver using the run's model navigation.
     *
     * @param context
     *            the run's context
     */
    OperatorOperandResolver(final SerializationContext context) {
        this.navigation = context.modelQueries().navigation();
    }

    /**
     * Returns the operand expressions of an operator, read from the values of its parameters rather than from its
     * arguments, because that is where the parser stores them. Returns a mutable copy in parameter order, omitting
     * parameters without a resolved operand expression.
     *
     * @param expression
     *            the operator expression
     * @return the operands
     */
    List<Expression> operands(final InvocationExpression expression) {
        final List<Expression> resolvedOperands = new ArrayList<>();
        for (final Feature parameter : this.navigation.parameterFeatures(expression)) {
            final Expression operand = this.operand(parameter);
            if (operand != null) {
                resolvedOperands.add(operand);
            }
        }
        return resolvedOperands;
    }

    /**
     * Returns the expression bound to a parameter or argument feature, unwrapping the feature reference expressions
     * that expression-reference grammar rules insert around an operand.
     *
     * @param feature
     *            the parameter or argument feature
     * @return the operand, or {@code null} when the feature has no value expression
     */
    Expression operand(final Feature feature) {
        Expression operandExpression = this.navigation.featureValues(feature).stream()
                .map(this.navigation::featureValueExpression)
                .filter(Objects::nonNull).findFirst().orElse(null);
        // A plain Expression is a body expression, not another operand wrapper, so unwrapping stops there.
        while (operandExpression instanceof FeatureReferenceExpression) {
            final Expression wrappedExpression = this.navigation.firstOwnedExpression(operandExpression);
            if (wrappedExpression == null
                    || wrappedExpression.eClass() == SysMLPackage.eINSTANCE.getExpression()) {
                break;
            }
            operandExpression = wrappedExpression;
        }
        return operandExpression;
    }
}
