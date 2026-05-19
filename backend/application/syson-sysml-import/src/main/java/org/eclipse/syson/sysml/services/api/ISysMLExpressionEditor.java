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
package org.eclipse.syson.sysml.services.api;

import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.LiteralBoolean;
import org.eclipse.syson.sysml.OperatorExpression;
import org.eclipse.syson.sysml.Relationship;

/**
 * Use to create of edit a SySMLv2 {@link Expression} in a given context from a given text fragment.
 *
 * @author pcdavid
 */
public interface ISysMLExpressionEditor {
    /**
     * Create a {@link Expression expression} inside the specified parent element from a textual representation.
     *
     * @param emfEditingContext
     *            the editing context.
     * @param parentElement
     *            the parent element in which the new expression will be added. The concrete {@link Relationship
     *            relationship} into which the expression is added will depend on the parent's type.
     * @param expressionText
     *            the plain text representation of the expression to create.
     * @return the result of creating a new expression. It can either contain the actual expression created (on success)
     *         or a list of messages on failure.
     */
    ExpressionCreationResult createExpression(IEMFEditingContext emfEditingContext, Element parentElement, String expressionText);

    /**
     * "Edit" an existing {@link Expression expression} inside the specified parent element from a textual
     * representation. The actual type of the new expression, once parse, may differ from the type of the existing one:
     * if we have a {@link LiteralBoolean} {@code "true"} and we want the change to {@code "value < limit"} which will
     * be parsed as an {@link OperatorExpression}, editing an expression is actually <em>replacing</em> the previous
     * {@link Element} with a new one, which will have a different identity.
     *
     * @param emfEditingContext
     *            the editing context.
     * @param parentElement
     *            the parent element of the expression to edit. The concrete {@link Relationship relationship} into
     *            which the expression is added will depend on the parent's type.
     * @param expressionText
     *            the plain text representation of the expression to create as a replacement.
     * @return the result of editing the expression. It can either contain the actual new expression created as a
     *         replacement (on success) or a list of messages on failure.
     */
    ExpressionCreationResult editExpression(IEMFEditingContext emfEditingContext, Element parentElement, Expression expression, String expressionText);
}
