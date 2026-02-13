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
package org.eclipse.syson.application.controllers.search;

import static org.assertj.core.api.Assertions.assertThat;

import com.jayway.jsonpath.JsonPath;

import java.util.List;
import java.util.Map;

import org.eclipse.sirius.web.application.views.search.dto.SearchResult;
import org.eclipse.sirius.web.application.views.search.dto.SearchSuccessPayload;
import org.eclipse.sirius.web.tests.graphql.SearchQueryRunner;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.ProjectWithLibraryDependencyContainingLibraryPackageTestProjectData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests of the search controllers.
 *
 * @author gdaniel
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SearchIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    private SearchQueryRunner searchQueryRunner;

    @Test
    @DisplayName("GIVEN a SysML project, WHEN we execute a search including user libraries, THEN all the matching semantic elements are returned")
    @Sql(scripts = { ProjectWithLibraryDependencyContainingLibraryPackageTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenSysMLProjectWhenWeExecuteSearchIncludingUserLibrariesThenAllMatchingElementsAreReturned() {
        List<String> matches = this.search(ProjectWithLibraryDependencyContainingLibraryPackageTestProjectData.EDITING_CONTEXT, "Library.sysml", false, false, false, false, /* searchInLibraries */ false);
        assertThat(matches).isEmpty();
        matches = this.search(ProjectWithLibraryDependencyContainingLibraryPackageTestProjectData.EDITING_CONTEXT, "Library.sysml", false, false, false, false, /* searchInLibraries */ true);
        assertThat(matches).containsExactlyInAnyOrder("Library.sysml");
    }

    @Test
    @DisplayName("GIVEN a SysML project, WHEN we execute a search including standard libraries, THEN all the matching semantic elements are returned")
    @Sql(scripts = { ProjectWithLibraryDependencyContainingLibraryPackageTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenSysMLProjectWhenWeExecuteSearchIncludingStandardLibrariesThenAllMatchingElementsAreReturned() {
        List<String> matches = this.search(ProjectWithLibraryDependencyContainingLibraryPackageTestProjectData.EDITING_CONTEXT, "parts", true, true, false, false, /* searchInLibraries */ false);
        assertThat(matches).isEmpty();
        matches = this.search(ProjectWithLibraryDependencyContainingLibraryPackageTestProjectData.EDITING_CONTEXT, "parts", true, true, false, false, /* searchInLibraries */ true);
        assertThat(matches).containsExactly("parts");
    }

    private List<String> search(String editingContextId, String text, boolean matchCase, boolean matchWholeWord, boolean useRegularExpressions, boolean searchInAttributes, boolean searchInLibraries) {
        // The SearchQuery object must be passed as a plain Map here
        var queryMap = Map.of(
                "text", text,
                "matchCase", matchCase,
                "matchWholeWord", matchWholeWord,
                "useRegularExpression", useRegularExpressions,
                "searchInAttributes", searchInAttributes,
                "searchInLibraries", searchInLibraries
        );
        Map<String, Object> variables = Map.of(
                "editingContextId", editingContextId,
                "query", queryMap
        );
        var result = this.searchQueryRunner.run(variables);

        String payloadTypename = JsonPath.read(result.data(), "$.data.viewer.editingContext.search.__typename");
        assertThat(payloadTypename).isEqualTo(SearchSuccessPayload.class.getSimpleName());

        String resultTypename = JsonPath.read(result.data(), "$.data.viewer.editingContext.search.result.__typename");
        assertThat(resultTypename).isEqualTo(SearchResult.class.getSimpleName());

        return JsonPath.read(result.data(), "$.data.viewer.editingContext.search.result.matches[*].label");
    }
}
