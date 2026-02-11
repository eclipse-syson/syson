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
package org.eclipse.syson;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

/**
 * Superclass of all the integration tests used to setup the test environment with SysON's Elasticsearch integration.
 *
 * @author gdaniel
 */
public class AbstractIntegrationTestWithElasticsearch extends AbstractIntegrationTests {

    public static final ElasticsearchContainer ELASTICSEARCH_CONTAINER;

    static {
        ELASTICSEARCH_CONTAINER = new ElasticsearchContainer("elasticsearch:9.2.1")
                .withEnv("xpack.security.transport.ssl.enabled", "false")
                .withEnv("xpack.security.http.ssl.enabled", "false")
                .withReuse(true);
        ELASTICSEARCH_CONTAINER.start();
    }

    @DynamicPropertySource
    public static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.elasticsearch.uris", AbstractIntegrationTestWithElasticsearch.ELASTICSEARCH_CONTAINER::getHttpHostAddress);
        registry.add("spring.elasticsearch.username", () -> "elastic");
        registry.add("spring.elasticsearch.password", () -> ElasticsearchContainer.ELASTICSEARCH_DEFAULT_PASSWORD);
    }
}
