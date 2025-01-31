/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.application.controllers.projects;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.SysMLv2Identifiers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests of the project data versioning REST controller.
 *
 * @author arichard
 */
@Transactional
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProjectDataVersioningRestControllerIntegrationTests extends AbstractIntegrationTests {

    private static final String INVALID_PROJECT = "55555555-5555-5555-5555-555555555555";

    private static final String SIMPLE_PROJECT_PART = "a4f51a38-bfeb-4e0d-a870-55f8fe90405e";

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @LocalServerPort
    private String port;

    private String getHTTPBaseUrl() {
        return "http://localhost:" + this.port;
    }

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @Test
    @DisplayName("Given the SysON REST API, when we ask for all changes, then all changes should be returned")
    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenSysONRestAPIWhenWeAskForAllChangesThenAllChangesShouldBeReturned() {
        var webTestClient = WebTestClient.bindToServer()
                .baseUrl(this.getHTTPBaseUrl())
                .build();

        String expectedJSON = null;
        try {
            var classPathResource = new ClassPathResource("simple_project_changes.json");
            expectedJSON = FileUtils.readFileToString(classPathResource.getFile(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            fail(e);
        }

        var uri = String.format("/api/rest/projects/%s/commits/%s/changes", SysMLv2Identifiers.SIMPLE_PROJECT, SysMLv2Identifiers.SIMPLE_PROJECT);
        webTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .json(expectedJSON);
    }

    @Test
    @DisplayName("Given the SysON REST API, when we ask for all changes in an unknown project, then it should return an error")
    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenSysONRestAPIWhenWeAskForAllChangesInUnknownProjectThenItShouldReturnAnError() {
        var webTestClient = WebTestClient.bindToServer()
                .baseUrl(this.getHTTPBaseUrl())
                .build();

        var uri = String.format("/api/rest/projects/%s/commits/%s/changes", INVALID_PROJECT, INVALID_PROJECT);
        webTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    @DisplayName("Given the SysON REST API, when we ask for changes of a specific element, then those changes should be returned")
    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenSysONRestAPIWhenWeAskForChangesOfASpecificElementThenThoseChangesShouldBeReturned() {
        var webTestClient = WebTestClient.bindToServer()
                .baseUrl(this.getHTTPBaseUrl())
                .build();

        String expectedJSON = null;
        try {
            var classPathResource = new ClassPathResource("part_changes.json");
            expectedJSON = FileUtils.readFileToString(classPathResource.getFile(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            fail(e);
        }

        var computedChangeId = UUID.nameUUIDFromBytes((SysMLv2Identifiers.SIMPLE_PROJECT + SIMPLE_PROJECT_PART).getBytes()).toString();
        var uri = String.format("/api/rest/projects/%s/commits/%s/changes/%s", SysMLv2Identifiers.SIMPLE_PROJECT, SysMLv2Identifiers.SIMPLE_PROJECT, computedChangeId);
        webTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .json(expectedJSON);
    }

    @Test
    @DisplayName("Given the SysON REST API, when we ask for specific changes in an unknown project, then it should return an error")
    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenSysONRestAPIWhenWeAskForSpecificChangesInUnknownProjectThenItShouldReturnAnError() {
        var webTestClient = WebTestClient.bindToServer()
                .baseUrl(this.getHTTPBaseUrl())
                .build();

        var computedChangeId = UUID.nameUUIDFromBytes((INVALID_PROJECT + SIMPLE_PROJECT_PART).getBytes()).toString();
        var uri = String.format("/api/rest/projects/%s/commits/%s/changes/%s", INVALID_PROJECT, INVALID_PROJECT, computedChangeId);
        webTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}
