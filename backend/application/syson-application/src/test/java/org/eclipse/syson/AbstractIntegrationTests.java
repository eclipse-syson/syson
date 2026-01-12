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
package org.eclipse.syson;

import org.eclipse.sirius.web.infrastructure.configuration.persistence.JDBCConfiguration;
import org.eclipse.sirius.web.starter.SiriusWebStarterConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

/**
 * Superclass of all the integration tests used to setup the test environment.
 *
 * @author sbegaudeau
 */
@SpringJUnitConfig(classes = { SiriusWebStarterConfiguration.class, JDBCConfiguration.class, IntegrationTestConfiguration.class })
public abstract class AbstractIntegrationTests {
    public static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER;

    public static final ElasticsearchContainer ELASTICSEARCH_CONTAINER;


    static {
        POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:latest").withReuse(true);
        POSTGRESQL_CONTAINER.start();
        ELASTICSEARCH_CONTAINER = new ElasticsearchContainer("elasticsearch:9.2.1")
                .withEnv("xpack.security.transport.ssl.enabled", "false")
                .withEnv("xpack.security.http.ssl.enabled", "false")
                .withReuse(true);
        ELASTICSEARCH_CONTAINER.start();
    }

    @DynamicPropertySource
    public static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
    }
}
