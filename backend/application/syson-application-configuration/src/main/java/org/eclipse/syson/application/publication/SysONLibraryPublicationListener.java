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

import org.eclipse.sirius.web.domain.boundedcontexts.library.Library;
import org.eclipse.sirius.web.domain.boundedcontexts.library.services.api.ILibraryCreationService;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.SemanticData;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.events.SemanticDataCreatedEvent;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Listener that reacts when the {@link SemanticData} of a published library gets created.
 *
 * @author flatombe
 */
@Service
public class SysONLibraryPublicationListener {

    private final ILibraryCreationService libraryCreationService;

    public SysONLibraryPublicationListener(final ILibraryCreationService libraryCreationService) {
        this.libraryCreationService = Objects.requireNonNull(libraryCreationService);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void onSemanticDataCreatedEvent(final SemanticDataCreatedEvent semanticDataCreatedEvent) {
        if (semanticDataCreatedEvent.causedBy() instanceof SysONPublishedLibrarySemanticDataCreationRequested request) {
            final SemanticData createdSemanticData = semanticDataCreatedEvent.semanticData();

            final Library createdLibrary = Library.newLibrary()
                    .namespace(request.libraryNamespace())
                    .name(request.libraryName())
                    .version(request.libraryVersion())
                    .description(request.libraryDescription())
                    .semanticData(AggregateReference.to(createdSemanticData.getId()))
                    .build(semanticDataCreatedEvent);
            this.libraryCreationService.createLibrary(createdLibrary);
        }
    }
}
