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
package org.eclipse.syson.application.expressions.dto;

import java.util.Objects;
import java.util.UUID;

import org.eclipse.sirius.components.core.api.IPayload;

/**
 * Payload for the {@code expressionTextualRepresentation} query field on EditingContext.
 *
 * @author pcdavid
 */
public record ExpressionTextualRepresentationPayload(UUID id, String textualRepresentation) implements IPayload {
    public ExpressionTextualRepresentationPayload {
        Objects.requireNonNull(id);
    }
}
