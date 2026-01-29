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
package org.eclipse.syson.application.controllers.diagrams.general.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.diagrams.ArrowStyle;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.data.GeneraViewRelationshipTestProjectData;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;

/**
 * Tests related to relationship in the General View Diagram.
 *
 * @author frouene
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVRelationshipTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @GivenSysONServer({ GeneraViewRelationshipTestProjectData.SCRIPT_PATH })
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    @DisplayName("GIVEN a General View with multiplicity range, WHEN the diagram is render, THEN the edge center label as an empty text")
    public void checkMultiplicityLabel() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneraViewRelationshipTestProjectData.EDITING_CONTEXT_ID,
                GeneraViewRelationshipTestProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            assertThat(diagram.getNodes()).hasSize(4);
            assertThat(diagram.getEdges()).hasSize(2);
            assertThat(diagram.getEdges().get(0).getCenterLabel()).isNotNull();
            assertThat(diagram.getEdges().get(0).getCenterLabel().text()).isBlank();
            assertThat(diagram.getNodes()).satisfiesOnlyOnce(node -> assertThat(node.getInsideLabel().getText()).contains("[2]"));
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneraViewRelationshipTestProjectData.SCRIPT_PATH })
    @Test
    @DisplayName("GIVEN a General View with an OccurrenceDefinition owning a OccurrenceUsage, WHEN the diagram is rendered, THEN an edge should be diplayed between the usage and the definition")
    public void checkOccurenceDefinitionNestedOccurrencesEdge() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneraViewRelationshipTestProjectData.EDITING_CONTEXT_ID,
                GeneraViewRelationshipTestProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            Optional<Edge> nestedOcccurenceEdge = diagram.getEdges().stream().filter(
                    edge -> GeneraViewRelationshipTestProjectData.GraphicalIds.OCC_1_ID.equals(edge.getTargetId()) && GeneraViewRelationshipTestProjectData.GraphicalIds.OCC_DEF_1_ID.equals(edge.getSourceId()))
                    .findFirst();
            assertThat(nestedOcccurenceEdge.isPresent()).isTrue();
            assertThat(nestedOcccurenceEdge.get().getStyle().getSourceArrow() == ArrowStyle.FillDiamond);

        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

}
