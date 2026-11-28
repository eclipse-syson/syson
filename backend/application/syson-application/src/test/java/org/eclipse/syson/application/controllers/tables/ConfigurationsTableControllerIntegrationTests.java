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
package org.eclipse.syson.application.controllers.tables;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.components.tables.tests.TableEventPayloadConsumer.assertRefreshedTableThat;

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.sirius.components.collaborative.dto.CreateRepresentationInput;
import org.eclipse.sirius.components.graphql.tests.RepresentationDescriptionsQueryRunner;
import org.eclipse.sirius.components.graphql.tests.api.GraphQLResult;
import org.eclipse.sirius.components.tables.Line;
import org.eclipse.sirius.web.tests.services.api.IGivenCreatedTableSubscription;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.ModelConfigurationTestProjectData;
import org.eclipse.syson.tests.api.GivenSysONServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Creates a Configurations Table for {@code Example::part1}. This occurrence inherits {@code PD1}
 * and already selects variant {@code var1} through local shorthand redefinitions.
 * Checks the sorted configurable paths, excluding intermediate parts and fixed attributes.
 *
 * @author Jerome Gout
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConfigurationsTableControllerIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private RepresentationDescriptionsQueryRunner representationDescriptionsQueryRunner;

    @Autowired
    private IGivenCreatedTableSubscription givenCreatedTableSubscription;

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    @Test
    @GivenSysONServer({ ModelConfigurationTestProjectData.SCRIPT_PATH })
    @DisplayName("GIVEN part1, WHEN creating a configuration table, THEN its configurable paths are displayed without columns")
    public void createConfigurationTable() {
        // GIVEN a part and its dynamically retrieved configurations table description
        String editingContextId = ModelConfigurationTestProjectData.EDITING_CONTEXT_ID;
        GraphQLResult descriptions = this.representationDescriptionsQueryRunner.run(Map.of(
                "editingContextId", editingContextId, "objectId", ModelConfigurationTestProjectData.PART_1_ID));
        assertThat(descriptions.errors()).isEmpty();
        List<String> availableLabels = JsonPath.read(descriptions.data(),
                "$.data.viewer.editingContext.representationDescriptions.edges[*].node.label");
        List<String> tableDescriptionIds = JsonPath.read(descriptions.data(),
                "$.data.viewer.editingContext.representationDescriptions.edges"
                        + "[?(@.node.label == 'Configurations Table View')].node.id");
        assertThat(tableDescriptionIds)
                .as("Configurations table description; available labels: %s", availableLabels)
                .hasSize(1);

        // WHEN creating the table and subscribing to its events
        CreateRepresentationInput input = new CreateRepresentationInput(UUID.randomUUID(), editingContextId, tableDescriptionIds.getFirst(),
                ModelConfigurationTestProjectData.PART_1_ID, "Part1 configurations");
        Flux<Object> flux = this.givenCreatedTableSubscription.createAndSubscribe(input).flux();

        // THEN the sorted configurable paths are displayed without columns or cells
        StepVerifier.create(flux)
                .consumeNextWith(assertRefreshedTableThat(table -> {
                    assertThat(table.getColumns()).isEmpty();
                    assertThat(table.getLines()).extracting(Line::getHeaderLabel).containsExactly(
                            "^attr1_1", "^partPD2.^attr2_1", "partPD3.^attr3_1", "partPD3.variantPart",
                            "partPD3.variantPart.attrVar1", "partPD3.variantPart.^commonAttr");
                    assertThat(table.getLines()).allSatisfy(line -> assertThat(line.getCells()).isEmpty());
                }))
                .thenCancel()
                .verify(Duration.ofSeconds(20));
    }
}
