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

import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

/**
 * Test configuration to register Elasticsearch properties for tests that require Elasticsearch.
 * <p>
 * Use {@link SysONTestsProperties#ELASTICSEARCH_PROPERTY} in the test configuration to enable Elasticsearch.
 * </p>
 *
 * @author gdaniel
 */
@Configuration
@Conditional(OnElasticsearchEnabledTests.class)
public class ElasticsearchDynamicPropertyRegistrar implements DynamicPropertyRegistrar {

    @Override
    public void accept(DynamicPropertyRegistry registry) {
        registry.add("spring.elasticsearch.uris", AbstractIntegrationTests.ELASTICSEARCH_CONTAINER::getHttpHostAddress);
        registry.add("spring.elasticsearch.username", () -> "elastic");
        registry.add("spring.elasticsearch.password", () -> ElasticsearchContainer.ELASTICSEARCH_DEFAULT_PASSWORD);
    }
}
