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
package org.eclipse.syson.application.controllers.diagrams.interconnection.view;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

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
import org.eclipse.syson.SysONTestsProperties;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.DiagramCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.IDiagramChecker;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeReconnectionTester;
import org.eclipse.syson.application.data.InterconnectionViewTestProjectData;
import org.eclipse.syson.diagram.interconnection.view.IVDescriptionNameGenerator;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.sysml.FlowConnectionUsage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;

/**
 * Tests around Flow Connection edges from a Part Usage in the Interconnection View diagram.
 *
 * @author frouene
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = { SysONTestsProperties.NO_DEFAULT_LIBRARIES_PROPERTY })
public class IVFlowConnectionFromPartUsageTests extends AbstractIntegrationTests {

    private final IVDescriptionNameGenerator descriptionNameGenerator = new IVDescriptionNameGenerator();

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramReference givenDiagram;

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

    private DiagramCheckerService diagramCheckerService;

    private StepVerifier.Step<DiagramRefreshedEventPayload> verifier;

    private AtomicReference<Diagram> diagram;

    private SemanticCheckerService semanticCheckerService;

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                InterconnectionViewTestProjectData.EDITING_CONTEXT_ID,
                InterconnectionViewTestProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.diagram = this.givenDiagram.getDiagram(this.verifier);
        this.diagramCheckerService = new DiagramCheckerService(this.diagramComparator, this.descriptionNameGenerator);
        this.semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, InterconnectionViewTestProjectData.EDITING_CONTEXT_ID, null);
    }

    @AfterEach
    public void tearDown() {
        if (this.verifier != null) {
            this.verifier.thenCancel()
                    .verify(Duration.ofSeconds(10));
        }
    }

    @Test
    @DisplayName("Given a FlowConnectionUsage, when reconnecting the target, then the new target of the FlowConnectionUsage is correct")
    @Sql(scripts = { InterconnectionViewTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void reconnectFlowConnectionUsageTarget() {
        this.verifier.then(() -> this.edgeReconnectionTester.reconnectEdge(InterconnectionViewTestProjectData.EDITING_CONTEXT_ID,
                this.diagram,
                InterconnectionViewTestProjectData.GraphicalIds.FLOW_CONNECTION_P1_P2_ID,
                InterconnectionViewTestProjectData.GraphicalIds.PORT_PART_3_ID,
                ReconnectEdgeKind.TARGET));

        IDiagramChecker diagramCheckerTarget = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);

            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            DiagramAssertions.assertThat(newEdge).hasSourceId(InterconnectionViewTestProjectData.GraphicalIds.PORT_PART_1_ID);
            DiagramAssertions.assertThat(newEdge).hasTargetId(InterconnectionViewTestProjectData.GraphicalIds.PORT_PART_3_ID);
            DiagramAssertions.assertThat(newEdge.getStyle()).hasTargetArrow(ArrowStyle.InputFillClosedArrow);
        };

        this.diagramCheckerService.checkDiagram(diagramCheckerTarget, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, FlowConnectionUsage.class, () -> InterconnectionViewTestProjectData.SemanticIds.FLOW_CONNECTION_P1_P2_ID, flowConnectionUsage -> {
            assertThat(this.identityService.getId(flowConnectionUsage.getSource().get(0))).isEqualTo(InterconnectionViewTestProjectData.SemanticIds.PORT_PART_1_ID);
            assertThat(this.identityService.getId(flowConnectionUsage.getTarget().get(0))).isEqualTo(InterconnectionViewTestProjectData.SemanticIds.PORT_PART_3_ID);
        });
    }

    @Test
    @DisplayName("Given a FlowConnectionUsage, when reconnecting the source, then the new source of the FlowConnectionUsage is correct")
    @Sql(scripts = { InterconnectionViewTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void reconnectFlowConnectionUsageSource() {
        this.verifier.then(() -> this.edgeReconnectionTester.reconnectEdge(InterconnectionViewTestProjectData.EDITING_CONTEXT_ID,
                this.diagram,
                InterconnectionViewTestProjectData.GraphicalIds.FLOW_CONNECTION_P1_P2_ID,
                InterconnectionViewTestProjectData.GraphicalIds.PORT_PART_3_ID,
                ReconnectEdgeKind.SOURCE));

        IDiagramChecker diagramCheckerTarget = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);

            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            DiagramAssertions.assertThat(newEdge).hasSourceId(InterconnectionViewTestProjectData.GraphicalIds.PORT_PART_3_ID);
            DiagramAssertions.assertThat(newEdge).hasTargetId(InterconnectionViewTestProjectData.GraphicalIds.PORT_PART_2_ID);
            DiagramAssertions.assertThat(newEdge.getStyle()).hasTargetArrow(ArrowStyle.InputFillClosedArrow);
        };

        this.diagramCheckerService.checkDiagram(diagramCheckerTarget, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, FlowConnectionUsage.class, () -> InterconnectionViewTestProjectData.SemanticIds.FLOW_CONNECTION_P1_P2_ID, flowConnectionUsage -> {
            assertThat(this.identityService.getId(flowConnectionUsage.getSource().get(0))).isEqualTo(InterconnectionViewTestProjectData.SemanticIds.PORT_PART_3_ID);
            assertThat(this.identityService.getId(flowConnectionUsage.getTarget().get(0))).isEqualTo(InterconnectionViewTestProjectData.SemanticIds.PORT_PART_2_ID);
        });
    }
}
