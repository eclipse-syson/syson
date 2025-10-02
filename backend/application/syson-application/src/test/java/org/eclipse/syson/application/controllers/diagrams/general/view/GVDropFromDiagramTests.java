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
import org.eclipse.sirius.components.collaborative.diagrams.dto.DropNodeInput;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.diagrams.tests.graphql.DropNodeMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
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
 * Tests the drag and drop of nodes inside the General View diagram.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVDropFromDiagramTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private DropNodeMutationRunner dropNodeMutationRunner;

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        return flux;
    }

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a diagram with some nodes, WHEN a node is dropped in another one, THEN the diagram is updated")
    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void dropPartFromDiagramToPackageThenFromPackageToPart() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramId = new AtomicReference<String>();
        var packageNodeId = new AtomicReference<String>();
        var partNodeId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            diagramId.set(diagram.getId());

            var packageNode = new DiagramNavigator(diagram).nodeWithLabel("Package").getNode();
            packageNodeId.set(packageNode.getId());

            var partNode = new DiagramNavigator(diagram).nodeWithLabel("\u00ABref part\u00BB\npart").getNode();
            partNodeId.set(partNode.getId());

            assertThat(packageNode.getChildNodes()).hasSize(0);
        });

        Runnable dropPartNodeFromDiagramToPackage = () -> {
            var input = new DropNodeInput(
                    UUID.randomUUID(),
                    GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID.toString(),
                    diagramId.get(),
                    partNodeId.get(),
                    packageNodeId.get(),
                    0,
                    0);
            var result = this.dropNodeMutationRunner.run(input);
            String typename = JsonPath.read(result, "$.data.dropNode.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentConsumerAfterFirstDrop = assertRefreshedDiagramThat(diagram -> {
            var packageNode = new DiagramNavigator(diagram).nodeWithLabel("Package").getNode();

            var partNode = new DiagramNavigator(diagram).nodeWithLabel("\u00ABref part\u00BB\npart").getNode();
            partNodeId.set(partNode.getId());

            assertThat(packageNode.getChildNodes()).hasSize(1);
            assertThat(packageNode.getChildNodes().get(0)).isEqualTo(partNode);
        });

        Runnable dropPartNodeFromPackageToDiagram = () -> {
            var input = new DropNodeInput(
                    UUID.randomUUID(),
                    GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID.toString(),
                    diagramId.get(),
                    partNodeId.get(),
                    diagramId.get(),
                    0,
                    0);
            var result = this.dropNodeMutationRunner.run(input);
            String typename = JsonPath.read(result, "$.data.dropNode.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentConsumerAfterSecondDrop = assertRefreshedDiagramThat(diagram -> {
            var packageNode = new DiagramNavigator(diagram).nodeWithLabel("Package").getNode();

            var partNode = new DiagramNavigator(diagram).nodeWithLabel("\u00ABref part\u00BB\npart").getNode();

            assertThat(packageNode.getChildNodes()).hasSize(0);
            assertThat(partNode).isNotNull();
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(dropPartNodeFromDiagramToPackage)
                .consumeNextWith(updatedDiagramContentConsumerAfterFirstDrop)
                .then(dropPartNodeFromPackageToDiagram)
                .consumeNextWith(updatedDiagramContentConsumerAfterSecondDrop)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
