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
 * The input object of the {@code editExpression} mutation.
 *
 * @param id
 *            the mutation identifier.
 * @param editingContextId
 *            the editing context identifier.
 * @param elementId
 *            the identifier of the expression itself or of the parent element that owns a single expression.
 * @param newExpressionText
 *            the textual representation of the replacement expression.
 * @param properties
 *            the optional typed properties to apply once the expression is edited successfully.
 * @author pcdavid
 */
public record EditExpressionInput(UUID id, String editingContextId, String elementId, String newExpressionText, ExpressionPropertiesInput properties) implements IInput {
    /**
     * Creates an input without optional properties.
     *
     * @param id
     *            the mutation identifier.
     * @param editingContextId
     *            the editing context identifier.
     * @param elementId
     *            the identifier of the expression itself or of the parent element that owns a single expression.
     * @param newExpressionText
     *            the textual representation of the replacement expression.
     */
    public EditExpressionInput(UUID id, String editingContextId, String elementId, String newExpressionText) {
        this(id, editingContextId, elementId, newExpressionText, null);
    }
}
