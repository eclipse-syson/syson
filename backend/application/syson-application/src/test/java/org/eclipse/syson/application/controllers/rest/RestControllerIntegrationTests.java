/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
package org.eclipse.syson.application.controllers.rest;

import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.SysONTestsProperties;
import org.eclipse.syson.application.data.SimpleProjectElementsTestProjectData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests of REST controller.
 *
 * @author arichard
 */
@Transactional
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = { SysONTestsProperties.NO_DEFAULT_LIBRARIES_PROPERTY })
public class RestControllerIntegrationTests extends AbstractIntegrationTests {

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

    @DisplayName("GIVEN the SysON REST API, WHEN we ask for several queries involving dates, THEN all queries should return correctly")
    @GivenSysONServer({ SimpleProjectElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void givenSysONRestAPIWhenWeAskForSeveralQueriesInvolvingDatesThenAllQueriesShouldReturnCorrectly() {
        var webTestClient = WebTestClient.bindToServer()
                .baseUrl(this.getHTTPBaseUrl())
                .build();

        var uri = String.format("/api/rest/projects");
        webTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .isOk();

        uri = String.format("/api/rest/projects/%s/commits/%s/elements", SimpleProjectElementsTestProjectData.PROJECT_ID, SimpleProjectElementsTestProjectData.PROJECT_ID);
        webTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .isOk();

        uri = String.format("/api/rest/projects");
        webTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @DisplayName("GIVEN the SysON REST API, WHEN we ask for projects, THEN created dates should be serialized as ISO-8601 strings")
    @GivenSysONServer({ SimpleProjectElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void givenSysONRestAPIWhenWeAskForProjectsThenCreatedDatesShouldBeSerializedAsISO8601Strings() {
        var webTestClient = WebTestClient.bindToServer()
                .baseUrl(this.getHTTPBaseUrl())
                .build();

        var uri = String.format("/api/rest/projects");
        webTestClient.get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[0].created").isEqualTo("1970-01-01T00:00:00Z");
    }
}
