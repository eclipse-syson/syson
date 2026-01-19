/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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

import org.eclipse.sirius.components.events.ICause;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.SemanticData;

/**
 * {@link ICause} indicating that the creation of a {@link SemanticData} representing a SysML Project published as a
 * library has been requested.
 *
 * @author flatombe
 */
public record SysONPublishedLibrarySemanticDataCreationRequested(
        UUID id,
        ICause causedBy,
        String libraryNamespace,
        String libraryName,
        String libraryVersion,
        String libraryDescription) implements ICause {

    public SysONPublishedLibrarySemanticDataCreationRequested(final ICause cause, final String libraryNamespace, final String libraryName, final String libraryVersion,
            final String libraryDescription) {
        this(UUID.randomUUID(), cause, libraryNamespace, libraryName, libraryVersion, libraryDescription);
    }

    public SysONPublishedLibrarySemanticDataCreationRequested {
        Objects.requireNonNull(id);
        Objects.requireNonNull(causedBy);
        Objects.requireNonNull(libraryNamespace);
        Objects.requireNonNull(libraryName);
        Objects.requireNonNull(libraryVersion);
        Objects.requireNonNull(libraryDescription);
    }

}
