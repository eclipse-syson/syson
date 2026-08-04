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
package org.eclipse.syson.application.controllers.omnibox;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import com.jayway.jsonpath.JsonPath;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.sirius.components.core.api.IEditingContextSearchService;
import org.eclipse.sirius.web.infrastructure.elasticsearch.services.api.IIndexCreationService;
import org.eclipse.sirius.web.infrastructure.elasticsearch.services.api.IIndexDeletionService;
import org.eclipse.sirius.web.infrastructure.elasticsearch.services.api.IIndexUpdateService;
import org.eclipse.sirius.web.tests.graphql.ProjectsOmniboxSearchQueryRunner;
import org.eclipse.syson.AbstractIntegrationTestWithElasticsearch;
import org.eclipse.syson.SysONTestsProperties;
import org.eclipse.syson.application.data.SimpleProjectElementsTestProjectData;
import org.eclipse.syson.tests.api.GivenSysONServer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.elastic.clients.elasticsearch.ElasticsearchClient;

/**
 * Integration tests of the projects omnibox controllers.
 *
 * @author gdaniel
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = { SysONTestsProperties.SYSON_TEST_ENABLED + "=" + SysONTestsProperties.NO_DEFAULT_LIBRARIES })
public class ProjectsOmniboxControllerIntegrationTests extends AbstractIntegrationTestWithElasticsearch {

    @Autowired
    private ProjectsOmniboxSearchQueryRunner projectsOmniboxSearchQueryRunner;

    @Autowired
    private IIndexUpdateService indexUpdateService;

    @Autowired
    private IIndexCreationService indexCreationService;

    @Autowired
    private IIndexDeletionService indexDeletionService;

    @Autowired
    private Optional<ElasticsearchClient> optionalElasticSearchClient;

    @Autowired
    private IEditingContextSearchService editingContextSearchService;

    /**
     * Verifies that creating an already existing index is handled as an idempotent operation.
     */
    @Test
    @DisplayName("GIVEN an existing index, WHEN it is created again, THEN the creation is ignored")
    public void givenExistingIndexWhenIndexIsCreatedAgainThenCreationIsIgnored() throws IOException {
        String editingContextId = UUID.randomUUID().toString();
        try {
            assertThat(this.indexCreationService.createIndex(editingContextId)).isTrue();
            assertThat(this.indexCreationService.createIndex(editingContextId)).isFalse();
            assertThat(this.optionalElasticSearchClient.orElseThrow().indices()
                    .exists(existsRequest -> existsRequest.index("editing-context-" + editingContextId)).value()).isTrue();
        } finally {
            this.indexDeletionService.deleteIndex(editingContextId);
        }
    }

    @DisplayName("GIVEN a query, WHEN the objects are searched in the projects omnibox, THEN the objects are returned")
    @GivenSysONServer({ SimpleProjectElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void givenQueryWhenObjectsAreSearchedInProjectsOmniboxThenObjectsAreReturned() {
        assertThat(this.optionalElasticSearchClient.isPresent());
        this.editingContextSearchService.findById(SimpleProjectElementsTestProjectData.EDITING_CONTEXT_ID).ifPresent(this.indexUpdateService::updateIndex);
        // Wait for Elasticsearch's refresh to ensure the indexed documents can be queried.
        this.optionalElasticSearchClient.ifPresent(elasticSearchClient -> {
            try {
                elasticSearchClient.indices().refresh();
            } catch (IOException exception) {
                fail(exception);
            }
        });

        Map<String, Object> emptyQueryVariables = Map.of(
                "query", ""
        );

        var emptyQueryResult = this.projectsOmniboxSearchQueryRunner.run(emptyQueryVariables);
        List<String> emptyQueryObjectLabels = JsonPath.read(emptyQueryResult.data(), "$.data.viewer.projectsOmniboxSearch.edges[*].node.label");
        assertThat(emptyQueryObjectLabels).isEmpty();

        Map<String, Object> filterQueryVariables = Map.of(
                "query", "name:Pack*"
        );

        var filterQueryResult = this.projectsOmniboxSearchQueryRunner.run(filterQueryVariables);
        List<String> filterQueryObjectLabels = JsonPath.read(filterQueryResult.data(), "$.data.viewer.projectsOmniboxSearch.edges[*].node.label");
        assertThat(filterQueryObjectLabels)
                .hasSize(2)
                .anySatisfy(label -> assertThat(label).contains("Package1", SimpleProjectElementsTestProjectData.PROJECT_NAME))
                .anySatisfy(label -> assertThat(label).contains("Package2", SimpleProjectElementsTestProjectData.PROJECT_NAME));

        Map<String, Object> complexQueryVariables = Map.of(
                "query", "@type:Part AND owner.name:Package1"
        );

        var complexQueryResult = this.projectsOmniboxSearchQueryRunner.run(complexQueryVariables);
        List<String> complexQueryObjectLabels = JsonPath.read(complexQueryResult.data(), "$.data.viewer.projectsOmniboxSearch.edges[*].node.label");
        assertThat(complexQueryObjectLabels).isNotEmpty()
                .anySatisfy(label -> assertThat(label).contains("p", SimpleProjectElementsTestProjectData.PROJECT_NAME));
    }
}
