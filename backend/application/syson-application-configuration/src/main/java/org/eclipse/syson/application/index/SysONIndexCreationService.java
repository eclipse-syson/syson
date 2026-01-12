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

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.sirius.web.application.index.services.api.IIndexEntry;
import org.eclipse.sirius.web.application.studio.services.api.IStudioCapableEditingContextPredicate;
import org.eclipse.sirius.web.infrastructure.elasticsearch.services.DefaultIndexCreationService;
import org.eclipse.sirius.web.infrastructure.elasticsearch.services.api.IIndexCreationServiceDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.mapping.DynamicTemplate;
import co.elastic.clients.util.NamedValue;

/**
 * Creates indices to store SysML models.
 *
 * <p>
 * This service creates and configures the index associated to each editing context. The created index has a name matching the pattern {@code editing-context-<Editing Context ID>}.
 * This service is automatically called by Sirius Web when an editing context is created (e.g. when a project is created).
 * </p>
 *
 * @author gdaniel
 */
@Service
public class SysONIndexCreationService implements IIndexCreationServiceDelegate {

    private final IStudioCapableEditingContextPredicate studioCapableEditingContextPredicate;

    private final Optional<ElasticsearchClient> optionalElasticSearchClient;

    private final Logger logger = LoggerFactory.getLogger(SysONIndexCreationService.class);

    public SysONIndexCreationService(IStudioCapableEditingContextPredicate studioCapableEditingContextPredicate, Optional<ElasticsearchClient> optionalElasticSearchClient) {
        this.studioCapableEditingContextPredicate = Objects.requireNonNull(studioCapableEditingContextPredicate);
        this.optionalElasticSearchClient = Objects.requireNonNull(optionalElasticSearchClient);
    }

    @Override
    public boolean canHandle(String editingContextId) {
        return !this.studioCapableEditingContextPredicate.test(editingContextId);
    }

    @Override
    public boolean createIndex(String editingContextId) {
        boolean indexCreated = false;
        if (this.optionalElasticSearchClient.isPresent()) {
            ElasticsearchClient elasticSearchClient = this.optionalElasticSearchClient.get();
            try {
                if (!elasticSearchClient.indices().exists(existsRequest -> existsRequest.index(DefaultIndexCreationService.EDITING_CONTEXT_INDEX_NAME_PREFIX + editingContextId)).value()) {
                    elasticSearchClient.indices().create(createIndexRequest -> createIndexRequest
                            .index(DefaultIndexCreationService.EDITING_CONTEXT_INDEX_NAME_PREFIX + editingContextId)
                            .settings(settingsBuilder -> settingsBuilder.mode("lookup"))
                            .mappings(mappingsBuilder -> mappingsBuilder
                                    // Do not index nestedIndexEntryType field, this is technical information we don't want to search for.
                                    // Note that we need to use a dynamic template here because multiple fields may exist containing "@nestedIndexEntryType",
                                    // and we cannot use wildcards in field names when defining properties.
                                    .dynamicTemplates(NamedValue.of("nestedIndexEntryTypeMapping", DynamicTemplate.of(dynamicTemplateBuilder ->
                                            dynamicTemplateBuilder.matchMappingType("string")
                                                    .match("*" + INestedIndexEntry.NESTED_INDEX_ENTRY_TYPE_FIELD)
                                                    .mapping(mappingBuilder -> mappingBuilder
                                                            .text(textBuilder -> textBuilder.index(false)))
                                    )))
                                    // Do not index iconURLs, editingContextId, or entryType, this is technical information we don't want to search for.
                                    .properties(IIndexEntry.ICON_URLS_FIELD, propertyBuilder ->
                                            propertyBuilder.text(textPropertyBuilder ->
                                                    textPropertyBuilder.index(false)))
                                    .properties(IIndexEntry.EDITING_CONTEXT_ID_FIELD, propertyBuilder ->
                                            propertyBuilder.text(textPropertyBuilder ->
                                                    textPropertyBuilder.index(false)))
                                    .properties(IIndexEntry.INDEX_ENTRY_TYPE_FIELD, propertyBuilder ->
                                            propertyBuilder.text(textPropertyBuilder ->
                                                    textPropertyBuilder.index(false)))));
                    indexCreated = true;
                }
            } catch (IOException | ElasticsearchException exception) {
                this.logger.warn("An error occurred while creating the index", exception);
            }
        }
        return indexCreated;
    }
}
