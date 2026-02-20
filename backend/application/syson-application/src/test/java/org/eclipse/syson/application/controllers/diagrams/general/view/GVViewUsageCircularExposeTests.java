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
package org.eclipse.syson.application.controllers.diagrams.general.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionInput;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.components.graphql.tests.api.IExecuteEditingContextFunctionRunner;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controllers.diagrams.testers.DropFromExplorerTester;
import org.eclipse.syson.application.data.GVViewUsageCircularExposeTestProjectData;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.sysml.ViewUsage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Tests the expose of ViewUsage inside in the General View diagram.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVViewUsageCircularExposeTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private DropFromExplorerTester dropFromExplorerTester;

    @Autowired
    private IExecuteEditingContextFunctionRunner executeEditingContextFunctionRunner;

    @Autowired
    private IObjectSearchService objectSearchService;

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GVViewUsageCircularExposeTestProjectData.EDITING_CONTEXT_ID,
                GVViewUsageCircularExposeTestProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        return flux;
    }

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a diagram with ViewUsage nodes, WHEN the ViewUsage corresponding to the diagram is d&d into the diagram, THEN it is not exposed.")
    @GivenSysONServer({ GVViewUsageCircularExposeTestProjectData.SCRIPT_PATH })
    @Test
    public void testDropViewUsageOnDiagram() {
        var flux = this.givenSubscriptionToDiagram();

        var diagram = new AtomicReference<Diagram>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            diagram.set(diag);
            assertThat(new DiagramNavigator(diag).findDiagramNodeCount()).isEqualTo(2);
        });

        Runnable dropFromExplorerRunnable = () -> {
            this.dropFromExplorerTester.dropFromExplorerOnDiagram(GVViewUsageCircularExposeTestProjectData.EDITING_CONTEXT_ID, diagram,
                    GVViewUsageCircularExposeTestProjectData.SemanticIds.VIEW_USAGE_VIEW1_ID);
        };

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            assertThat(new DiagramNavigator(diag).findDiagramNodeCount()).isEqualTo(2);
        });

        Runnable checkSemanticData = () -> {
            var input = new ExecuteEditingContextFunctionInput(UUID.randomUUID(), GVViewUsageCircularExposeTestProjectData.EDITING_CONTEXT_ID,
                    (editingContext, executeEditingContextFunctionInput) -> {
                        Optional<Object> optViewUsage1 = this.objectSearchService.getObject(editingContext, GVViewUsageCircularExposeTestProjectData.SemanticIds.VIEW_USAGE_VIEW1_ID);
                        assertThat(optViewUsage1).isPresent().get().isInstanceOf(ViewUsage.class);
                        ViewUsage viewUsage1 = (ViewUsage) optViewUsage1.get();
                        assertThat(viewUsage1.getExposedElement()).hasSize(1);
                        assertThat(viewUsage1.getExposedElement()).doesNotContain(viewUsage1);
                        return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                    });
            var payload = this.executeEditingContextFunctionRunner.execute(input).block();
            assertThat(payload).isInstanceOf(ExecuteEditingContextFunctionSuccessPayload.class);
        };

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(dropFromExplorerRunnable)
                .consumeNextWith(updatedDiagramContentConsumer)
                .then(checkSemanticData)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a diagram with ViewUsage nodes, WHEN the ViewUsage corresponding to the diagram is d&d into a ViewUsage node, THEN it is not exposed.")
    @GivenSysONServer({ GVViewUsageCircularExposeTestProjectData.SCRIPT_PATH })
    @Test
    public void testDropViewUsageOnViewUsageNode() {
        var flux = this.givenSubscriptionToDiagram();

        var diagram = new AtomicReference<Diagram>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            diagram.set(diag);
            assertThat(new DiagramNavigator(diag).findDiagramNodeCount()).isEqualTo(2);
        });

        Runnable dropFromExplorerOnTopNodeRunnable = () -> {
            this.dropFromExplorerTester.dropFromExplorerOnDiagramElement(GVViewUsageCircularExposeTestProjectData.EDITING_CONTEXT_ID, diagram,
                    GVViewUsageCircularExposeTestProjectData.SemanticIds.VIEW_USAGE_VIEW1_ID, GVViewUsageCircularExposeTestProjectData.GraphicalIds.VIEW_USAGE_VIEW2_ID);
        };

        Consumer<Object> updatedDiagramContentConsumerAfterDropOnTopNode = assertRefreshedDiagramThat(diag -> {
            assertThat(new DiagramNavigator(diag).findDiagramNodeCount()).isEqualTo(2);
        });

        Runnable dropFromExplorerOnNestedNodeRunnable = () -> {
            this.dropFromExplorerTester.dropFromExplorerOnDiagramElement(GVViewUsageCircularExposeTestProjectData.EDITING_CONTEXT_ID, diagram,
                    GVViewUsageCircularExposeTestProjectData.SemanticIds.VIEW_USAGE_VIEW1_ID, GVViewUsageCircularExposeTestProjectData.GraphicalIds.VIEW_USAGE_VIEW3_ID);
        };

        Consumer<Object> updatedDiagramContentConsumerAfterDropOnNestedNode = assertRefreshedDiagramThat(diag -> {
            assertThat(new DiagramNavigator(diag).findDiagramNodeCount()).isEqualTo(2);
        });

        Runnable checkSemanticData = () -> {
            var input = new ExecuteEditingContextFunctionInput(UUID.randomUUID(), GVViewUsageCircularExposeTestProjectData.EDITING_CONTEXT_ID,
                    (editingContext, executeEditingContextFunctionInput) -> {
                        Optional<Object> optViewUsage1 = this.objectSearchService.getObject(editingContext, GVViewUsageCircularExposeTestProjectData.SemanticIds.VIEW_USAGE_VIEW1_ID);
                        assertThat(optViewUsage1).isPresent().get().isInstanceOf(ViewUsage.class);
                        ViewUsage viewUsage1 = (ViewUsage) optViewUsage1.get();
                        assertThat(viewUsage1.getExposedElement()).hasSize(1);
                        assertThat(viewUsage1.getExposedElement()).doesNotContain(viewUsage1);
                        return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                    });
            var payload = this.executeEditingContextFunctionRunner.execute(input).block();
            assertThat(payload).isInstanceOf(ExecuteEditingContextFunctionSuccessPayload.class);
        };

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(dropFromExplorerOnTopNodeRunnable)
                .consumeNextWith(updatedDiagramContentConsumerAfterDropOnTopNode)
                .then(dropFromExplorerOnNestedNodeRunnable)
                .consumeNextWith(updatedDiagramContentConsumerAfterDropOnNestedNode)
                .then(checkSemanticData)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
