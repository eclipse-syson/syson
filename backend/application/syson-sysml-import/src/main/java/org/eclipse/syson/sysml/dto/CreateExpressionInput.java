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
package org.eclipse.syson.sysml.dto;

import java.util.UUID;

import org.eclipse.sirius.components.core.api.IInput;

/**
 * The input object of the createExpression operation.
 *
 * @param id
 *            the mutation identifier.
 * @param editingContextId
 *            the editing context identifier.
 * @param parentElementId
 *            the identifier of the element in which the expression should be created.
 * @param expressionText
 *            the textual representation of the expression to create.
 * @param properties
 *            the optional typed properties to apply once the expression is created successfully.
 * @author pcdavid
 */
public record CreateExpressionInput(UUID id, String editingContextId, String parentElementId, String expressionText, ExpressionPropertiesInput properties) implements IInput {
    /**
     * Creates an input without optional properties.
     *
     * @param id
     *            the mutation identifier.
     * @param editingContextId
     *            the editing context identifier.
     * @param parentElementId
     *            the identifier of the element in which the expression should be created.
     * @param expressionText
     *            the textual representation of the expression to create.
     */
    public CreateExpressionInput(UUID id, String editingContextId, String parentElementId, String expressionText) {
        this(id, editingContextId, parentElementId, expressionText, null);
    }
}
