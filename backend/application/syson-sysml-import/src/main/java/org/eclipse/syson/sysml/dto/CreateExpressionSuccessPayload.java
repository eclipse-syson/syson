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

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.representations.Message;

/**
 * The result returned by the {@code createExpression} mutation on success.
 *
 * @author pcdavid
 */
public record CreateExpressionSuccessPayload(UUID id, String newExpressionId, List<Message> messages) implements IPayload {
    public CreateExpressionSuccessPayload {
        Objects.requireNonNull(id);
        Objects.requireNonNull(newExpressionId);
        Objects.requireNonNull(messages);
    }
}
