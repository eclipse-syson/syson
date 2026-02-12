/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
import java.time.Duration;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.data.SimpleProjectElementsTestProjectData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
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

    @DisplayName("GIVEN the SysON REST API, WHEN we ask for all changes, THEN all changes should be returned")
    @GivenSysONServer({ SimpleProjectElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void givenSysONRestAPIWhenWeAskForAllChangesThenAllChangesShouldBeReturned() {
        var webTestClient = WebTestClient.bindToServer()
                .baseUrl(this.getHTTPBaseUrl())
                .responseTimeout(Duration.ofSeconds(30))
                .build();

        String expectedJSON = null;
        try {
            var classPathResource = new ClassPathResource("simple_project_changes.json");
            expectedJSON = FileUtils.readFileToString(classPathResource.getFile(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            fail(e);
        }

        var uri = String.format("/api/rest/projects/%s/commits/%s/changes", SimpleProjectElementsTestProjectData.PROJECT_ID, SimpleProjectElementsTestProjectData.PROJECT_ID);
        webTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .json(expectedJSON);
    }

    @DisplayName("GIVEN the SysON REST API, WHEN we ask for all changes in an unknown project, THEN it should return an error")
    @GivenSysONServer({ SimpleProjectElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void givenSysONRestAPIWhenWeAskForAllChangesInUnknownProjectThenItShouldReturnAnError() {
        var webTestClient = WebTestClient.bindToServer()
                .baseUrl(this.getHTTPBaseUrl())
                .responseTimeout(Duration.ofSeconds(30))
                .build();

        var uri = String.format("/api/rest/projects/%s/commits/%s/changes", INVALID_PROJECT, INVALID_PROJECT);
        webTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @DisplayName("GIVEN the SysON REST API, WHEN we ask for changes of a specific element, THEN those changes should be returned")
    @GivenSysONServer({ SimpleProjectElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void givenSysONRestAPIWhenWeAskForChangesOfASpecificElementThenThoseChangesShouldBeReturned() {
        var webTestClient = WebTestClient.bindToServer()
                .baseUrl(this.getHTTPBaseUrl())
                .responseTimeout(Duration.ofSeconds(30))
                .build();

        String expectedJSON = null;
        try {
            var classPathResource = new ClassPathResource("part_changes.json");
            expectedJSON = FileUtils.readFileToString(classPathResource.getFile(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            fail(e);
        }

        var computedChangeId = UUID.nameUUIDFromBytes((SimpleProjectElementsTestProjectData.PROJECT_ID + SimpleProjectElementsTestProjectData.SemanticIds.PART_ID).getBytes()).toString();
        var uri = String.format("/api/rest/projects/%s/commits/%s/changes/%s", SimpleProjectElementsTestProjectData.PROJECT_ID, SimpleProjectElementsTestProjectData.PROJECT_ID, computedChangeId);
        webTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .json(expectedJSON);
    }

    @DisplayName("GIVEN the SysON REST API, WHEN we ask for specific changes in an unknown project, THEN it should return an error")
    @GivenSysONServer({ SimpleProjectElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void givenSysONRestAPIWhenWeAskForSpecificChangesInUnknownProjectThenItShouldReturnAnError() {
        var webTestClient = WebTestClient.bindToServer()
                .baseUrl(this.getHTTPBaseUrl())
                .responseTimeout(Duration.ofSeconds(30))
                .build();

        var computedChangeId = UUID.nameUUIDFromBytes((INVALID_PROJECT + SimpleProjectElementsTestProjectData.SemanticIds.PART_ID).getBytes()).toString();
        var uri = String.format("/api/rest/projects/%s/commits/%s/changes/%s", INVALID_PROJECT, INVALID_PROJECT, computedChangeId);
        webTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}
