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
package org.eclipse.syson.application.publication;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.sirius.web.application.UUIDParser;
import org.eclipse.sirius.web.application.library.dto.PublishLibrariesInput;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.RepresentationContent;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.RepresentationMetadata;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.services.RepresentationCompositeIdProvider;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.services.api.IRepresentationContentCreationService;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.services.api.IRepresentationContentSearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.services.api.IRepresentationMetadataCreationService;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.services.api.IRepresentationMetadataSearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.SemanticData;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.events.SemanticDataCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Creates the representations of a library once its semantic data are published.
 *
 * @author gdaniel
 */
@Service
public class SysONLibraryRepresentationPublicationListener {

    private final IRepresentationMetadataSearchService representationMetadataSearchService;

    private final IRepresentationMetadataCreationService representationMetadataCreationService;

    private final IRepresentationContentSearchService representationContentSearchService;

    private final IRepresentationContentCreationService representationContentCreationService;

    private final Logger logger = LoggerFactory.getLogger(SysONLibraryRepresentationPublicationListener.class);

    public SysONLibraryRepresentationPublicationListener(IRepresentationMetadataSearchService representationMetadataSearchService,
            IRepresentationMetadataCreationService representationMetadataCreationService, IRepresentationContentSearchService representationContentSearchService,
            IRepresentationContentCreationService representationContentCreationService) {
        this.representationMetadataSearchService = Objects.requireNonNull(representationMetadataSearchService);
        this.representationMetadataCreationService = Objects.requireNonNull(representationMetadataCreationService);
        this.representationContentSearchService = Objects.requireNonNull(representationContentSearchService);
        this.representationContentCreationService = Objects.requireNonNull(representationContentCreationService);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void onSemanticDataCreatedEvent(final SemanticDataCreatedEvent semanticDataCreatedEvent) {
        if (semanticDataCreatedEvent.causedBy() instanceof SysONPublishedLibrarySemanticDataCreationRequested request
                && request.causedBy() instanceof PublishLibrariesInput publishLibrariesInput) {

            Optional<AggregateReference<SemanticData, UUID>> optionalSemanticDataId = new UUIDParser().parse(publishLibrariesInput.editingContextId())
                    .map(AggregateReference::to);

            if (optionalSemanticDataId.isPresent()) {

                SemanticData librarySemanticData = semanticDataCreatedEvent.semanticData();
                List<RepresentationMetadata> representationMetadataOnSemanticData = this.representationMetadataSearchService.findAllRepresentationMetadataBySemanticData(optionalSemanticDataId.get());

                for (RepresentationMetadata representationMetadata : representationMetadataOnSemanticData) {

                    Optional<RepresentationContent> optionalRepresentationContent = this.representationContentSearchService.findContentById(optionalSemanticDataId.get(),
                            AggregateReference.to(representationMetadata.getRepresentationMetadataId()));

                    if (optionalRepresentationContent.isPresent()) {

                        var duplicatedRepresentationId = UUID.randomUUID();
                        var id = new RepresentationCompositeIdProvider().getId(librarySemanticData.getId(), duplicatedRepresentationId);
                        var duplicatedRepresentationMetadata = RepresentationMetadata.newRepresentationMetadata(id)
                                .representationMetadataId(duplicatedRepresentationId)
                                .semanticData(AggregateReference.to(librarySemanticData.getId()))
                                .targetObjectId(representationMetadata.getTargetObjectId()) // same target object id (ids don't change in libraries)
                                .descriptionId(representationMetadata.getDescriptionId())
                                .label(representationMetadata.getLabel())
                                .kind(representationMetadata.getKind())
                                .documentation(representationMetadata.getDocumentation())
                                .iconURLs(representationMetadata.getIconURLs())
                                .build(semanticDataCreatedEvent);
                        this.representationMetadataCreationService.create(duplicatedRepresentationMetadata);

                        var duplicatedContent = optionalRepresentationContent.get().getContent()
                                .replace(representationMetadata.getRepresentationMetadataId().toString(), duplicatedRepresentationId.toString());
                        this.representationContentCreationService.create(
                                semanticDataCreatedEvent,
                                duplicatedRepresentationMetadata.getSemanticData(),
                                AggregateReference.to(duplicatedRepresentationMetadata.getRepresentationMetadataId()),
                                duplicatedContent,
                                optionalRepresentationContent.get().getLastMigrationPerformed(),
                                optionalRepresentationContent.get().getMigrationVersion()
                        );
                    } else {
                        this.logger.warn("Cannot find representation content with id {}", representationMetadata.getRepresentationMetadataId());
                    }
                }
            } else {
                this.logger.warn("Cannot find semantic data with id {}", publishLibrariesInput.editingContextId());
            }
        }
    }
}
