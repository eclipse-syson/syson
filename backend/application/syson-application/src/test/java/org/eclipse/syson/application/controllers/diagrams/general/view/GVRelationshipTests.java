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
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.SysMLv2Identifiers;
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


    @Sql(scripts = {"/scripts/syson-test-database.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/scripts/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    @DisplayName("Given a General View with multiplicity range, when the diagram is render, then the edge center label as an empty text")
    public void checkMultiplicityLabel() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                SysMLv2Identifiers.GENERAL_VIEW_RELATIONSHIP_EDITING_CONTEXT_ID,
                SysMLv2Identifiers.GENERAL_VIEW_RELATIONSHIP_DIAGRAM);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);

        Consumer<DiagramRefreshedEventPayload> initialDiagramContentConsumer = payload -> Optional.of(payload)
                .map(DiagramRefreshedEventPayload::diagram)
                .ifPresentOrElse(diagram -> {
                    assertThat(diagram.getNodes()).hasSize(2);
                    assertThat(diagram.getEdges()).hasSize(1);
                    assertThat(diagram.getEdges().get(0).getCenterLabel()).isNotNull();
                    assertThat(diagram.getEdges().get(0).getCenterLabel().getText()).isBlank();
                    assertThat(diagram.getNodes()).satisfiesOnlyOnce(node -> assertThat(node.getInsideLabel().getText()).contains("[2]"));

                }, () -> fail("Missing diagram"));


        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

}
