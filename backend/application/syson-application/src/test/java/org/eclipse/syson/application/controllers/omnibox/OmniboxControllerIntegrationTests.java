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
package org.eclipse.syson.application.controllers.omnibox;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.jayway.jsonpath.JsonPath;

import java.util.List;
import java.util.Map;

import org.eclipse.sirius.components.collaborative.omnibox.OmniboxSearchCommandProvider;
import org.eclipse.sirius.components.core.api.IEditingContextSearchService;
import org.eclipse.sirius.components.graphql.tests.OmniboxCommandsQueryRunner;
import org.eclipse.sirius.web.application.studio.services.StudioImportLibraryCommandProvider;
import org.eclipse.sirius.web.application.studio.services.StudioPublicationCommandProvider;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.SysONTestsProperties;
import org.eclipse.syson.application.data.SimpleProjectElementsTestProjectData;
import org.eclipse.syson.application.data.SysonStudioTestProjectData;
import org.eclipse.syson.application.omnibox.SysONOmniboxCommandProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests of the omnibox controllers.
 *
 * @author gdaniel
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = { SysONTestsProperties.NO_DEFAULT_LIBRARIES_PROPERTY })
public class OmniboxControllerIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    private OmniboxCommandsQueryRunner omniboxCommandsQueryRunner;

    @MockitoSpyBean
    private IEditingContextSearchService editingContextSearchService;

    @Test
    @DisplayName("GIVEN a SysML project, WHEN the commands are queried, THEN regular and SysML commands are returned")
    @Sql(scripts = { SimpleProjectElementsTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenSysMLProjectWhenCommandsAreQueriedThenRegularAndSysMLCommandsAreReturned() {
        Map<String, Object> omniboxCommandsQueryVariables = Map.of(
                "editingContextId", SimpleProjectElementsTestProjectData.EDITING_CONTEXT_ID,
                "selectedObjectIds", List.of(),
                "query", ""
        );
        var omniboxCommandsQueryResult = this.omniboxCommandsQueryRunner.run(omniboxCommandsQueryVariables);
        List<String> allCommandIds = JsonPath.read(omniboxCommandsQueryResult, "$.data.viewer.omniboxCommands.edges[*].node.id");
        assertThat(allCommandIds).hasSize(3).contains(OmniboxSearchCommandProvider.SEARCH_COMMAND_ID, SysONOmniboxCommandProvider.PUBLISH_SYSML_PROJECT_COMMAND_ID,
                SysONOmniboxCommandProvider.IMPORT_PUBLISHED_LIBRARY_COMMAND_ID);
        // Ensure the test never requested the editing context from IEditingContextSearchService.
        // Loading the editing context is time consuming since all the standard libraries need to be copied into it.
        verify(this.editingContextSearchService, never()).findById(SimpleProjectElementsTestProjectData.EDITING_CONTEXT_ID);
    }

    @Test
    @DisplayName("GIVEN a Studio project, WHEN the commands are queried, THEN regular and studio commands are returned")
    @Sql(scripts = { SysonStudioTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenStudioProjectWhenCommandsAreQueriedThenRegularAndStudioCommandsAreReturned() {
        Map<String, Object> omniboxCommandsQueryVariables = Map.of(
                "editingContextId", SysonStudioTestProjectData.EDITING_CONTEXT_ID,
                "selectedObjectIds", List.of(),
                "query", "");
        var omniboxCommandsQueryResult = this.omniboxCommandsQueryRunner.run(omniboxCommandsQueryVariables);
        List<String> allCommandIds = JsonPath.read(omniboxCommandsQueryResult, "$.data.viewer.omniboxCommands.edges[*].node.id");
        assertThat(allCommandIds).hasSize(4)
                .contains(OmniboxSearchCommandProvider.SEARCH_COMMAND_ID,
                        StudioPublicationCommandProvider.PUBLISH_STUDIO_COMMAND_ID,
                        StudioImportLibraryCommandProvider.IMPORT_LIBRARY_COMMAND_ID,
                        // It is possible to import SysML libraries in non-SysML projects, but it is not possible to
                        // publish SysML libraries in non-SysML projects.
                        SysONOmniboxCommandProvider.IMPORT_PUBLISHED_LIBRARY_COMMAND_ID);
        verify(this.editingContextSearchService, never()).findById(SimpleProjectElementsTestProjectData.EDITING_CONTEXT_ID);
    }
}
