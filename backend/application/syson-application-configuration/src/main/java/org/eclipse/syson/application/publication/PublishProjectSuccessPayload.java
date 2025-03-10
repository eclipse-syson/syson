/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.application.publication;

import java.util.Objects;
import java.util.UUID;

import org.eclipse.sirius.components.core.api.IPayload;

/**
 * {@link IPayload} that represents that a SysML Project has been successfully published as a library.
 *
 * @author flatombe
 */
public record PublishProjectSuccessPayload(UUID id) implements IPayload {
    public PublishProjectSuccessPayload {
        Objects.requireNonNull(id);
    }
}
