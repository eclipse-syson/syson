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
package org.eclipse.syson.application.index;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.web.application.UUIDParser;
import org.eclipse.sirius.web.application.index.services.api.IIndexEntry;
import org.eclipse.sirius.web.application.index.services.api.IIndexEntryProvider;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.services.api.ISemanticDataSearchService;
import org.eclipse.sirius.web.infrastructure.elasticsearch.services.DefaultIndexCreationService;
import org.eclipse.sirius.web.infrastructure.elasticsearch.services.api.IIndexCreationService;
import org.eclipse.sirius.web.infrastructure.elasticsearch.services.api.IIndexDeletionService;
import org.eclipse.sirius.web.infrastructure.elasticsearch.services.api.IIndexUpdateServiceDelegate;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._helpers.bulk.BulkIngester;

/**
 * TODO
 *
 * @author gdaniel
 */
@Service
public class SysONIndexUpdateService implements IIndexUpdateServiceDelegate {

    private final ISemanticDataSearchService semanticDataSearchService;

    private final IIndexCreationService indexCreationService;

    private final IIndexDeletionService indexDeletionService;

    private final IIndexEntryProvider indexEntryProvider;

    private final Optional<ElasticsearchClient> optionalElasticSearchClient;

    private final Logger logger = LoggerFactory.getLogger(SysONIndexUpdateService.class);

    public SysONIndexUpdateService(ISemanticDataSearchService semanticDataSearchService, IIndexCreationService indexCreationService, IIndexDeletionService indexDeletionService,
            IIndexEntryProvider indexEntryProvider, Optional<ElasticsearchClient> optionalElasticSearchClient) {
        this.semanticDataSearchService = Objects.requireNonNull(semanticDataSearchService);
        this.indexCreationService = Objects.requireNonNull(indexCreationService);
        this.indexDeletionService = Objects.requireNonNull(indexDeletionService);
        this.indexEntryProvider = Objects.requireNonNull(indexEntryProvider);
        this.optionalElasticSearchClient = Objects.requireNonNull(optionalElasticSearchClient);
    }

    @Override
    public boolean canHandle(IEditingContext editingContext) {
        return new UUIDParser().parse(editingContext.getId())
                .map(id -> this.semanticDataSearchService.isUsingDomains(id, List.of(SysmlPackage.eNS_URI)))
                .orElse(false);
    }

    @Override
    public void updateIndex(IEditingContext editingContext) {
        long start = System.nanoTime();
        if (this.optionalElasticSearchClient.isPresent()) {
            if (editingContext instanceof IEMFEditingContext emfEditingContext) {
                this.clearIndex(editingContext);
                BulkIngester<Void> bulkIngester = BulkIngester.of(bulkIngesterBuilder -> bulkIngesterBuilder
                        .client(this.optionalElasticSearchClient.get())
                );
                // We don't want to index SysML/KerML standard libraries: they are duplicated in each project and pollutes the search.
                List<Resource> resourcesToIndex = emfEditingContext.getDomain().getResourceSet().getResources().stream()
                        .filter(resource -> !ElementUtil.isStandardLibraryResource(resource))
                        .toList();
                for (Resource resourceToIndex : resourcesToIndex) {
                    StreamSupport.stream(Spliterators.spliteratorUnknownSize(resourceToIndex.getAllContents(), Spliterator.ORDERED), false)
                            .forEach(eObject -> {
                                Optional<IIndexEntry> optionalIndexEntry = this.indexEntryProvider.getIndexEntry(editingContext, eObject);
                                if (optionalIndexEntry.isPresent()) {
                                    IIndexEntry indexEntry = optionalIndexEntry.get();
                                    bulkIngester.add(bulkOperation -> bulkOperation
                                            .index(indexOperation -> indexOperation
                                                    .index(DefaultIndexCreationService.EDITING_CONTEXT_INDEX_NAME_PREFIX + editingContext.getId())
                                                    .id(indexEntry.id())
                                                    .document(indexEntry)
                                            )
                                    );
                                }
                            });
                }
                bulkIngester.close();
            }
        }
        Duration timeToIndexEditingContext = Duration.ofNanos(System.nanoTime() - start);
        this.logger.trace("Indexed editing context {} in {}ms", editingContext.getId(), timeToIndexEditingContext.toMillis());
    }

    private void clearIndex(IEditingContext editingContext) {
        this.indexDeletionService.deleteIndex(editingContext.getId());
        this.indexCreationService.createIndex(editingContext.getId());
    }
}
