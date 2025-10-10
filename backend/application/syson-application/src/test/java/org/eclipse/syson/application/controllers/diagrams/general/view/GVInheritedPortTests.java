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
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controllers.diagrams.graphql.ShowDiagramsInheritedMembersMutationRunner;
import org.eclipse.syson.application.data.GeneralViewInheritedPortTestProjectData;
import org.eclipse.syson.diagram.common.view.services.dto.ShowDiagramsInheritedMembersInput;
import org.eclipse.syson.diagram.common.view.services.dto.ShowDiagramsInheritedMembersSuccessPayload;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Tests the display of inherited ports inside the General View diagram.
 *
 * @author frouene
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVInheritedPortTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private ShowDiagramsInheritedMembersMutationRunner showDiagramsInheritedMembersMutationRunner;

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewInheritedPortTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewInheritedPortTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a diagram with some inherited port, WHEN show inherited members filter is uncheck, THEN inherited ports are not displayed")
    @Sql(scripts = { GeneralViewInheritedPortTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void checkInheritedPortsVisibility() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            diagramId.set(diagram.getId());
            var part2Node = new DiagramNavigator(diagram).nodeWithLabel("«part»\npart2").getNode();
            assertThat(part2Node.getBorderNodes()).hasSize(1);
            assertThat(part2Node.getBorderNodes()).allMatch(node -> node.getOutsideLabels().get(0).text().equals("port1"));
            var v1Node = new DiagramNavigator(diagram).nodeWithLabel("«part»\nv1 : Vehicle").getNode();
            assertThat(v1Node.getBorderNodes()).hasSize(1);
            assertThat(v1Node.getBorderNodes()).allMatch(node -> node.getOutsideLabels().get(0).text().equals("^fuelInPort : FuelPort"));
        });

        Runnable uncheckShowInheritedMembersFilter = () -> {
            var input = new ShowDiagramsInheritedMembersInput(
                    UUID.randomUUID(),
                    GeneralViewInheritedPortTestProjectData.EDITING_CONTEXT_ID,
                    diagramId.get(),
                    false);
            var result = this.showDiagramsInheritedMembersMutationRunner.run(input);
            String typename = JsonPath.read(result, "$.data.showDiagramsInheritedMembers.__typename");
            assertThat(typename).isEqualTo(ShowDiagramsInheritedMembersSuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentConsumerAfterInheritedVisibilityChange = assertRefreshedDiagramThat(diagram -> {
            var part2Node = new DiagramNavigator(diagram).nodeWithLabel("«part»\npart2").getNode();
            assertThat(part2Node.getBorderNodes()).hasSize(1);
            var v1Node = new DiagramNavigator(diagram).nodeWithLabel("«part»\nv1 : Vehicle").getNode();
            assertThat(v1Node.getBorderNodes()).hasSize(0);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(uncheckShowInheritedMembersFilter)
                .consumeNextWith(updatedDiagramContentConsumerAfterInheritedVisibilityChange)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
