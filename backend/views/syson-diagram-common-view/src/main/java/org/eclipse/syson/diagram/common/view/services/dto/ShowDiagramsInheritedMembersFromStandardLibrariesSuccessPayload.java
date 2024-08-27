/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.syson.diagram.common.view.services.dto;

import java.util.Objects;
import java.util.UUID;

import org.eclipse.sirius.components.core.api.IPayload;

/**
 * The payload of the show diagrams inherited members from standard libraries mutation.
 *
 * @author arichard
 */
public record ShowDiagramsInheritedMembersFromStandardLibrariesSuccessPayload(UUID id, boolean show) implements IPayload {

    public ShowDiagramsInheritedMembersFromStandardLibrariesSuccessPayload {
        Objects.requireNonNull(id);
    }
}
