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
package org.eclipse.syson.application.controllers.diagrams.interconnection.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.ArrowStyle;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.diagrams.events.ReconnectEdgeKind;
import org.eclipse.sirius.components.diagrams.tests.assertions.DiagramAssertions;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeReconnectionTester;
import org.eclipse.syson.application.data.InterconnectionViewFlowConnectionTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.sysml.FlowUsage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Tests around Flow edges from a Part Usage in the Interconnection View diagram.
 *
 * @author frouene
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IVFlowFromPartUsageTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private DiagramComparator diagramComparator;

    @Autowired
    private EdgeReconnectionTester edgeReconnectionTester;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private IIdentityService identityService;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    private SemanticCheckerService semanticCheckerService;

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(), InterconnectionViewFlowConnectionTestProjectData.EDITING_CONTEXT_ID,
                InterconnectionViewFlowConnectionTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        this.semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, InterconnectionViewFlowConnectionTestProjectData.EDITING_CONTEXT_ID,
                InterconnectionViewFlowConnectionTestProjectData.SemanticIds.PACKAGE_1_ID);
    }

    @DisplayName("GIVEN a FlowUsage, WHEN reconnecting the target, THEN the new target of the FlowUsage is correct")
    @GivenSysONServer({ InterconnectionViewFlowConnectionTestProjectData.SCRIPT_PATH })
    @Test
    public void reconnectFlowUsageTarget() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable creationToolRunnable = () -> this.edgeReconnectionTester.reconnectEdge(InterconnectionViewFlowConnectionTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                InterconnectionViewFlowConnectionTestProjectData.GraphicalIds.FLOW_CONNECTION_P1_P2_ID,
                InterconnectionViewFlowConnectionTestProjectData.GraphicalIds.PORT_PART_3_ID,
                ReconnectEdgeKind.TARGET);

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);

            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            DiagramAssertions.assertThat(newEdge).hasSourceId(InterconnectionViewFlowConnectionTestProjectData.GraphicalIds.PORT_PART_1_ID);
            DiagramAssertions.assertThat(newEdge).hasTargetId(InterconnectionViewFlowConnectionTestProjectData.GraphicalIds.PORT_PART_3_ID);
            DiagramAssertions.assertThat(newEdge.getStyle()).hasTargetArrow(ArrowStyle.InputFillClosedArrow);
        });

        Runnable semanticCheck = this.semanticCheckerService.checkElement(FlowUsage.class, () -> InterconnectionViewFlowConnectionTestProjectData.SemanticIds.FLOW_CONNECTION_P1_P2_ID, flowUsage -> {
            assertThat(this.identityService.getId(flowUsage.getSourceOutputFeature().getOwnedRedefinition().get(0).getRedefinedFeature()))
                    .isEqualTo(InterconnectionViewFlowConnectionTestProjectData.SemanticIds.PORT_PART_1_ID);
            assertThat(this.identityService.getId(flowUsage.getTargetInputFeature().getOwnedRedefinition().get(0).getRedefinedFeature()))
                    .isEqualTo(InterconnectionViewFlowConnectionTestProjectData.SemanticIds.PORT_PART_3_ID);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(creationToolRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a FlowUsage, WHEN reconnecting the source, THEN the new source of the FlowUsage is correct")
    @GivenSysONServer({ InterconnectionViewFlowConnectionTestProjectData.SCRIPT_PATH })
    @Test
    public void reconnectFlowUsageSource() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable creationToolRunnable = () -> this.edgeReconnectionTester.reconnectEdge(InterconnectionViewFlowConnectionTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                InterconnectionViewFlowConnectionTestProjectData.GraphicalIds.FLOW_CONNECTION_P1_P2_ID,
                InterconnectionViewFlowConnectionTestProjectData.GraphicalIds.PORT_PART_3_ID,
                ReconnectEdgeKind.SOURCE);

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);

            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            DiagramAssertions.assertThat(newEdge).hasSourceId(InterconnectionViewFlowConnectionTestProjectData.GraphicalIds.PORT_PART_3_ID);
            DiagramAssertions.assertThat(newEdge).hasTargetId(InterconnectionViewFlowConnectionTestProjectData.GraphicalIds.PORT_PART_2_ID);
            DiagramAssertions.assertThat(newEdge.getStyle()).hasTargetArrow(ArrowStyle.InputFillClosedArrow);
        });

        Runnable semanticCheck = this.semanticCheckerService.checkElement(FlowUsage.class, () -> InterconnectionViewFlowConnectionTestProjectData.SemanticIds.FLOW_CONNECTION_P1_P2_ID, flowUsage -> {
            assertThat(this.identityService.getId(flowUsage.getSourceOutputFeature().getOwnedRedefinition().get(0).getRedefinedFeature()))
                    .isEqualTo(InterconnectionViewFlowConnectionTestProjectData.SemanticIds.PORT_PART_3_ID);
            assertThat(this.identityService.getId(flowUsage.getTargetInputFeature().getOwnedRedefinition().get(0).getRedefinedFeature()))
                    .isEqualTo(InterconnectionViewFlowConnectionTestProjectData.SemanticIds.PORT_PART_2_ID);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(creationToolRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
