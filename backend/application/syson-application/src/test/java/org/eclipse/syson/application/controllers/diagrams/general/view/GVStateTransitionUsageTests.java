/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.GeneralViewStateTransitionUsageProjectData;
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
 * Tests related to TransitionUsage between states in the General View Diagram.
 *
 * @author Arthur Daussy
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVStateTransitionUsageTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Sql(scripts = { GeneralViewStateTransitionUsageProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    @DisplayName("GIVEN a General View with multiplicity range, WHEN the diagram is render, THEN the edge center label as an empty text")
    public void checkMultiplicityLabel() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewStateTransitionUsageProjectData.EDITING_CONTEXT_ID,
                GeneralViewStateTransitionUsageProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);

        Consumer<DiagramRefreshedEventPayload> initialDiagramContentConsumer = payload -> Optional.of(payload)
                .map(DiagramRefreshedEventPayload::diagram)
                .ifPresentOrElse(diagram -> {
                    /**
                     * 3 compositions edges. 3 successions/transitions edges on the diagram background. 3
                     * successions/transitions edges on the state transition compartment.
                     */
                    assertThat(diagram.getEdges()).hasSize(9);
                    // We want to test the edges on the diagram background.
                    Edge offOnEdge = diagram.getEdges().stream()
                            .filter(e -> GeneralViewStateTransitionUsageProjectData.GraphicalIds.OFF_ON_ID.equals(e.getId()))
                            .findFirst()
                            .get();
                    assertThat(offOnEdge.getCenterLabel().text()).isEqualTo("TurnOn via commPort");
                    assertThat(offOnEdge.getSourceId()).isEqualTo(GeneralViewStateTransitionUsageProjectData.GraphicalIds.OFF_ID);
                    assertThat(offOnEdge.getTargetId()).isEqualTo(GeneralViewStateTransitionUsageProjectData.GraphicalIds.ON_ID);
                    Edge onOffEdge = diagram.getEdges().stream()
                            .filter(e -> GeneralViewStateTransitionUsageProjectData.GraphicalIds.ON_OFF_ID.equals(e.getId()))
                            .findFirst()
                            .get();
                    assertThat(onOffEdge.getCenterLabel().text()).isEqualTo("after 5 [min] via ");
                    assertThat(onOffEdge.getSourceId()).isEqualTo(GeneralViewStateTransitionUsageProjectData.GraphicalIds.ON_ID);
                    assertThat(onOffEdge.getTargetId()).isEqualTo(GeneralViewStateTransitionUsageProjectData.GraphicalIds.OFF_ID);
                    Edge onIdleEdge = diagram.getEdges().stream()
                            .filter(e -> GeneralViewStateTransitionUsageProjectData.GraphicalIds.ON_IDLE_ID.equals(e.getId()))
                            .findFirst()
                            .get();
                    assertThat(onIdleEdge.getCenterLabel().text()).isEqualTo("[x > 0]");
                    assertThat(onIdleEdge.getSourceId()).isEqualTo(GeneralViewStateTransitionUsageProjectData.GraphicalIds.ON_ID);
                    assertThat(onIdleEdge.getTargetId()).isEqualTo(GeneralViewStateTransitionUsageProjectData.GraphicalIds.IDLE_ID);
                }, () -> fail("Missing diagram"));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

}
