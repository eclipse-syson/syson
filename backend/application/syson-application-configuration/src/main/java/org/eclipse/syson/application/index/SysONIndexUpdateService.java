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
import org.eclipse.sirius.web.application.index.services.api.IIndexEntry;
import org.eclipse.sirius.web.application.index.services.api.IIndexEntryProvider;
import org.eclipse.sirius.web.application.studio.services.api.IStudioCapableEditingContextPredicate;
import org.eclipse.sirius.web.infrastructure.elasticsearch.services.DefaultIndexCreationService;
import org.eclipse.sirius.web.infrastructure.elasticsearch.services.api.IIndexCreationService;
import org.eclipse.sirius.web.infrastructure.elasticsearch.services.api.IIndexDeletionService;
import org.eclipse.sirius.web.infrastructure.elasticsearch.services.api.IIndexUpdateServiceDelegate;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._helpers.bulk.BulkIngester;

/**
 * A service to index the content of SysML model.
 *
 * <p>
 * This service is called by Sirius Web when changes are detected in an editing context, which may require to update the associated indices. This implementation ensures that all the resources in the
 * editing context are indexed, excepted SysML and KerML standard libraries for performance reasons.
 * </p>
 * <p>
 * Note that this service does not accept {@code editingContext} instances that contain Studio data. For these {@code editingContext}, the default implementation of
 * {@link org.eclipse.sirius.web.infrastructure.elasticsearch.services.api.IDefaultIndexUpdateService} is used instead.
 * </p>
 *
 *
 * @author gdaniel
 */
@Service
public class SysONIndexUpdateService implements IIndexUpdateServiceDelegate {

    private final IStudioCapableEditingContextPredicate studioCapableEditingContextPredicate;

    private final IIndexCreationService indexCreationService;

    private final IIndexDeletionService indexDeletionService;

    private final IIndexEntryProvider indexEntryProvider;

    private final Optional<ElasticsearchClient> optionalElasticSearchClient;

    private final Logger logger = LoggerFactory.getLogger(SysONIndexUpdateService.class);

    public SysONIndexUpdateService(IStudioCapableEditingContextPredicate studioCapableEditingContextPredicate, IIndexCreationService indexCreationService, IIndexDeletionService indexDeletionService,
            IIndexEntryProvider indexEntryProvider, Optional<ElasticsearchClient> optionalElasticSearchClient) {
        this.studioCapableEditingContextPredicate = Objects.requireNonNull(studioCapableEditingContextPredicate);
        this.indexCreationService = Objects.requireNonNull(indexCreationService);
        this.indexDeletionService = Objects.requireNonNull(indexDeletionService);
        this.indexEntryProvider = Objects.requireNonNull(indexEntryProvider);
        this.optionalElasticSearchClient = Objects.requireNonNull(optionalElasticSearchClient);
    }

    @Override
    public boolean canHandle(IEditingContext editingContext) {
        return !this.studioCapableEditingContextPredicate.test(editingContext.getId());
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
        /*
         * In some cases (particularly when a new project is created), this line may throw an exception indicating that the index does not exist. This exception doesn't crash the application, it just
         * prevents an update of the index, which is not critical (it will be updated later when the semantic data gets updated).
         * This issue is linked to https://github.com/eclipse-sirius/sirius-web/issues/6044, and will disappear once it is fixed in Sirius Web.
         */
        this.indexDeletionService.deleteIndex(editingContext.getId());
        this.indexCreationService.createIndex(editingContext.getId());
    }
}
