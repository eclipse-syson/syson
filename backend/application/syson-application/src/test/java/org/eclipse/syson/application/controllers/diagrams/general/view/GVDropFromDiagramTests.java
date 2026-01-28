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

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DropNodesInput;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.diagrams.layoutdata.Position;
import org.eclipse.sirius.components.diagrams.tests.graphql.DropNodesMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    private DropNodesMutationRunner dropNodesMutationRunner;

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a diagram with some nodes, WHEN a node is dropped in another one, THEN the diagram is updated")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
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

            var partNode = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "ref part" + LabelConstants.CLOSE_QUOTE + "\npart").getNode();
            partNodeId.set(partNode.getId());

            assertThat(packageNode.getChildNodes()).hasSize(0);
        });

        Runnable dropPartNodeFromDiagramToPackage = () -> {
            var input = new DropNodesInput(
                    UUID.randomUUID(),
                    GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID.toString(),
                    diagramId.get(),
                    List.of(partNodeId.get()),
                    packageNodeId.get(),
                    List.of(new Position(0, 0)));
            var result = this.dropNodesMutationRunner.run(input);
            String typename = JsonPath.read(result.data(), "$.data.dropNodes.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentConsumerAfterFirstDrop = assertRefreshedDiagramThat(diagram -> {
            var packageNode = new DiagramNavigator(diagram).nodeWithLabel("Package").getNode();

            var partNode = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "ref part" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "part").getNode();
            partNodeId.set(partNode.getId());

            assertThat(packageNode.getChildNodes()).hasSize(1);
            assertThat(packageNode.getChildNodes().get(0)).isEqualTo(partNode);
        });

        Runnable dropPartNodeFromPackageToDiagram = () -> {
            var input = new DropNodesInput(
                    UUID.randomUUID(),
                    GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID.toString(),
                    diagramId.get(),
                    List.of(partNodeId.get()),
                    diagramId.get(),
                    List.of(new Position(0, 0)));
            var result = this.dropNodesMutationRunner.run(input);
            String typename = JsonPath.read(result.data(), "$.data.dropNodes.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentConsumerAfterSecondDrop = assertRefreshedDiagramThat(diagram -> {
            var packageNode = new DiagramNavigator(diagram).nodeWithLabel("Package").getNode();

            var partNode = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "ref part" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "part").getNode();

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
