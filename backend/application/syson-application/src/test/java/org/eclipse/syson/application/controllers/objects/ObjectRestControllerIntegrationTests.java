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
package org.eclipse.syson.application.controllers.objects;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.SimpleProjectElementsTestProjectData;
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
 * Integration tests of the object REST controller.
 *
 * @author arichard
 */
@Transactional
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ObjectRestControllerIntegrationTests extends AbstractIntegrationTests {

    private static final String INVALID_PROJECT = "55555555-5555-5555-5555-555555555555";

    private static final String SIMPLE_PROJECT_PACKAGE1 = "d51791b8-6666-46e3-8c60-c975e1f3e490";

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
    @DisplayName("Given the SysON REST API, when we ask for all elements, then all elements should be returned")
    @Sql(scripts = { SimpleProjectElementsTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenSysONRestAPIWhenWeAskForAllElementsThenAllElementsShouldBeReturned() {
        var webTestClient = WebTestClient.bindToServer()
                .baseUrl(this.getHTTPBaseUrl())
                .build();

        String expectedJSON = null;
        try {
            var classPathResource = new ClassPathResource("simple_project.json");
            expectedJSON = FileUtils.readFileToString(classPathResource.getFile(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            fail(e);
        }

        var uri = String.format("/api/rest/projects/%s/commits/%s/elements", SimpleProjectElementsTestProjectData.PROJECT_ID, SimpleProjectElementsTestProjectData.PROJECT_ID);
        webTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .json(expectedJSON);
    }

    @Test
    @DisplayName("Given the SysON REST API, when we ask for all elements in an unknown project, then it should return an error")
    @Sql(scripts = { SimpleProjectElementsTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenSysONRestAPIWhenWeAskForAllElementsInUnknownProjectThenItShouldReturnAnError() {
        var webTestClient = WebTestClient.bindToServer()
                .baseUrl(this.getHTTPBaseUrl())
                .build();

        var uri = String.format("/api/rest/projects/%s/commits/%s/elements", INVALID_PROJECT, INVALID_PROJECT);
        webTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    @DisplayName("Given the SysON REST API, when we ask for a specific element, then it should return the element")
    @Sql(scripts = { SimpleProjectElementsTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenSysONRestAPIWhenWeAskForSpecificElementThenItShouldReturnTheElement() {
        var webTestClient = WebTestClient.bindToServer()
                .baseUrl(this.getHTTPBaseUrl())
                .build();

        String expectedJSON = null;
        try {
            var classPathResource = new ClassPathResource("package1.json");
            expectedJSON = FileUtils.readFileToString(classPathResource.getFile(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            fail(e);
        }

        var uri = String.format("/api/rest/projects/%s/commits/%s/elements/%s", SimpleProjectElementsTestProjectData.PROJECT_ID, SimpleProjectElementsTestProjectData.PROJECT_ID, SIMPLE_PROJECT_PACKAGE1);
        webTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .json(expectedJSON);
    }

    @Test
    @DisplayName("Given the SysON REST API, when we ask for a specific element in an unknown project, then it should return an error")
    @Sql(scripts = { SimpleProjectElementsTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenSysONRestAPIWhenWeAskForSpecificElementInUnknownProjectThenItShouldReturnAnError() {
        var webTestClient = WebTestClient.bindToServer()
                .baseUrl(this.getHTTPBaseUrl())
                .build();

        var uri = String.format("/api/rest/projects/%s/commits/%s/elements/%s", INVALID_PROJECT, INVALID_PROJECT, SIMPLE_PROJECT_PACKAGE1);
        webTestClient.get()
                .uri(uri.toString())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Given the SysON REST API, when we ask for the relationships of an element, then it should return the relationships")
    @Sql(scripts = { SimpleProjectElementsTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenSysONRestAPIWhenWeAskForRelationshipsOfAnElementThenItShouldReturnTheRelationships() {
        var webTestClient = WebTestClient.bindToServer()
                .baseUrl(this.getHTTPBaseUrl())
                .build();

        String expectedJSON = null;
        try {
            var classPathResource = new ClassPathResource("part_relationships.json");
            expectedJSON = FileUtils.readFileToString(classPathResource.getFile(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            fail(e);
        }

        var uri = String.format("/api/rest/projects/%s/commits/%s/elements/%s/relationships", SimpleProjectElementsTestProjectData.PROJECT_ID, SimpleProjectElementsTestProjectData.PROJECT_ID,
                SIMPLE_PROJECT_PART);
        webTestClient.get()
                .uri(uri.toString())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .json(expectedJSON);
    }

    @Test
    @DisplayName("Given the SysON REST API, when we ask for the relationships of an element in an unknown project, then it should return an error")
    @Sql(scripts = { SimpleProjectElementsTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenSysONRestAPIWhenWeAskForRelationshipsOfAnElementInAnUnknownProjectThenItShouldReturnAnError() {
        var webTestClient = WebTestClient.bindToServer()
                .baseUrl(this.getHTTPBaseUrl())
                .build();

        var uri = String.format("/api/rest/projects/%s/commits/%s/elements/%s/relationships", INVALID_PROJECT, INVALID_PROJECT, SIMPLE_PROJECT_PACKAGE1);
        webTestClient.get()
                .uri(uri.toString())
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    @DisplayName("Given the SysON REST API, when we ask for the root elements, then it should return the root elements")
    @Sql(scripts = { SimpleProjectElementsTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenSysONRestAPIWhenWeAskForTheRootElementsThenItShouldReturnTheRootElements() {
        var webTestClient = WebTestClient.bindToServer()
                .baseUrl(this.getHTTPBaseUrl())
                .build();

        String expectedJSON = null;
        try {
            var classPathResource = new ClassPathResource("simple_project_roots.json");
            expectedJSON = FileUtils.readFileToString(classPathResource.getFile(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            fail(e);
        }

        var uri = String.format("/api/rest/projects/%s/commits/%s/roots", SimpleProjectElementsTestProjectData.PROJECT_ID, SimpleProjectElementsTestProjectData.PROJECT_ID);
        webTestClient.get()
                .uri(uri.toString())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .json(expectedJSON);
    }

    @Test
    @DisplayName("Given the SysON REST API, when we ask for the root elements of an unknown project, then it should return an error")
    @Sql(scripts = { SimpleProjectElementsTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenSysONRestAPIWhenWeAskForTheRootElementsOfUnknownProjectThenItShouldReturnAnError() {
        var webTestClient = WebTestClient.bindToServer()
                .baseUrl(this.getHTTPBaseUrl())
                .build();

        var uri = String.format("/api/rest/projects/%s/commits/%s/roots", INVALID_PROJECT, INVALID_PROJECT);
        webTestClient.get()
                .uri(uri.toString())
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}