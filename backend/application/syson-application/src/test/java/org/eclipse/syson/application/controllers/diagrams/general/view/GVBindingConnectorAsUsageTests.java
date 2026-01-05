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
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramAssertions.assertThat;
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramInstanceOfAssertFactories.EDGE;
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramInstanceOfAssertFactories.EDGE_STYLE;

import java.time.Duration;
import java.util.List;
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
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.SysONTestsProperties;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.ConnectorAsUsageChecker;
import org.eclipse.syson.application.controllers.diagrams.checkers.DiagramCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.IDiagramChecker;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeCreationTester;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeReconnectionTester;
import org.eclipse.syson.application.data.GeneralViewBindingConnectorProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.BindingConnectorAsUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
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
 * Tests the edition of {@link BindingConnectorAsUsage}s on General View.
 *
 * @author Arthur Daussy
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = { SysONTestsProperties.NO_DEFAULT_LIBRARIES_PROPERTY })
public class GVBindingConnectorAsUsageTests extends AbstractIntegrationTests {

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramReference givenDiagram;

    @Autowired
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private EdgeCreationTester edgeCreationTester;

    @Autowired
    private DiagramComparator diagramComparator;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private IIdentityService identityService;

    @Autowired
    private EdgeReconnectionTester edgeReconnectionTester;

    private DiagramDescriptionIdProvider diagramDescriptionIdProvider;

    private DiagramCheckerService diagramCheckerService;

    private StepVerifier.Step<DiagramRefreshedEventPayload> verifier;

    private SemanticCheckerService semanticCheckerService;

    private AtomicReference<Diagram> diagram;

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewBindingConnectorProjectData.EDITING_CONTEXT_ID,
                GeneralViewBindingConnectorProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.diagram = this.givenDiagram.getDiagram(this.verifier);
        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewBindingConnectorProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        this.diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);
        this.diagramCheckerService = new DiagramCheckerService(this.diagramComparator, this.descriptionNameGenerator);
        this.semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewBindingConnectorProjectData.EDITING_CONTEXT_ID,
                GeneralViewBindingConnectorProjectData.SemanticIds.PACKAGE_1_ID);
    }

    @AfterEach
    public void tearDown() {
        if (this.verifier != null) {
            this.verifier.thenCancel()
                    .verify(Duration.ofSeconds(10));
        }
    }

    @DisplayName("GIVEN a ActionUsage with nested item (1 level), WHEN creating a BindingConnectorAsUsage, THEN the binding should be created in the common container (A0) and a feature chain should be created for non direct features in target")
    @Sql(scripts = { GeneralViewBindingConnectorProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void bindingNestedLevel1() {

        AtomicReference<String> newEdge = this.createEdge(GeneralViewBindingConnectorProjectData.GraphicalIds.I0_ID,
                GeneralViewBindingConnectorProjectData.GraphicalIds.I1_ID,
                GeneralViewBindingConnectorProjectData.GraphicalIds.I0_ID,
                GeneralViewBindingConnectorProjectData.GraphicalIds.I1_ID);

        // I0 -> I1
        this.createChecker()
                .setExpectedSourceSemanticId(GeneralViewBindingConnectorProjectData.SemanticIds.I0_ID)
                .setExpectedTargetSemanticId(GeneralViewBindingConnectorProjectData.SemanticIds.I1_ID)
                .setExpectedSemanticContainer(GeneralViewBindingConnectorProjectData.SemanticIds.A0_ID)
                .setExpectedSourceReference(GeneralViewBindingConnectorProjectData.SemanticIds.I0_ID)
                .setExpectedTargetFeatureChain(List.of(GeneralViewBindingConnectorProjectData.SemanticIds.A1_ID, GeneralViewBindingConnectorProjectData.SemanticIds.I1_ID))
                .run(this.verifier, newEdge);

    }

    @DisplayName("GIVEN a ActionUsage with nested item (1 level), WHEN creating a BindingConnectorAsUsage, THEN the binding should be created in the common container (A0) and a feature chain should be created for non direct features in source")
    @Sql(scripts = { GeneralViewBindingConnectorProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void bindingNestedLevel1Reversed() {

        AtomicReference<String> newEdge = this.createEdge(GeneralViewBindingConnectorProjectData.GraphicalIds.I1_ID,
                GeneralViewBindingConnectorProjectData.GraphicalIds.I0_ID,
                GeneralViewBindingConnectorProjectData.GraphicalIds.I1_ID,
                GeneralViewBindingConnectorProjectData.GraphicalIds.I0_ID);

        // I1 -> I0
        this.createChecker()
                .setExpectedSourceSemanticId(GeneralViewBindingConnectorProjectData.SemanticIds.I1_ID)
                .setExpectedTargetSemanticId(GeneralViewBindingConnectorProjectData.SemanticIds.I0_ID)
                .setExpectedSemanticContainer(GeneralViewBindingConnectorProjectData.SemanticIds.A0_ID)
                .setExpectedTargetReference(GeneralViewBindingConnectorProjectData.SemanticIds.I0_ID)
                .setExpectedSourceFeatureChain(List.of(GeneralViewBindingConnectorProjectData.SemanticIds.A1_ID, GeneralViewBindingConnectorProjectData.SemanticIds.I1_ID))
                .run(this.verifier, newEdge);

    }

    @DisplayName("GIVEN a ActionUsage with nested item (2 level), WHEN creating a BindingConnectorAsUsage, THEN the binding should be created in the common container (A0) and a feature chain should be created for non direct features in source")
    @Sql(scripts = { GeneralViewBindingConnectorProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void bindingNestedLevel2() {

        AtomicReference<String> newEdge = this.createEdge(GeneralViewBindingConnectorProjectData.GraphicalIds.I0_ID,
                GeneralViewBindingConnectorProjectData.GraphicalIds.I11_ID,
                GeneralViewBindingConnectorProjectData.GraphicalIds.I0_ID,
                GeneralViewBindingConnectorProjectData.GraphicalIds.I11_ID);
        // I0 -> I11
        this.createChecker()
                .setExpectedSourceSemanticId(GeneralViewBindingConnectorProjectData.SemanticIds.I0_ID)
                .setExpectedTargetSemanticId(GeneralViewBindingConnectorProjectData.SemanticIds.I11_ID)
                .setExpectedSemanticContainer(GeneralViewBindingConnectorProjectData.SemanticIds.A0_ID)
                .setExpectedSourceReference(GeneralViewBindingConnectorProjectData.SemanticIds.I0_ID)
                .setExpectedTargetFeatureChain(List.of(GeneralViewBindingConnectorProjectData.SemanticIds.A1_ID, GeneralViewBindingConnectorProjectData.SemanticIds.A11_ID,
                        GeneralViewBindingConnectorProjectData.SemanticIds.I11_ID))
                .run(this.verifier, newEdge);
    }

    @DisplayName("GIVEN a ActionUsage owning sibling items, WHEN creating a BindingConnectorAsUsage, THEN the binding should be created the common container (A11) and feature should be directly referenced")
    @Sql(scripts = { GeneralViewBindingConnectorProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void bindingSameLevel() {

        AtomicReference<String> newEdge = this.createEdge(GeneralViewBindingConnectorProjectData.GraphicalIds.I11_ID,
                GeneralViewBindingConnectorProjectData.GraphicalIds.I11TEST_ID,
                GeneralViewBindingConnectorProjectData.GraphicalIds.I11_ID,
                GeneralViewBindingConnectorProjectData.GraphicalIds.I11TEST_ID);

        // I11 -> I11Test
        this.createChecker()
                .setExpectedSourceSemanticId(GeneralViewBindingConnectorProjectData.SemanticIds.I11_ID)
                .setExpectedTargetSemanticId(GeneralViewBindingConnectorProjectData.SemanticIds.I11TEST_ID)
                .setExpectedSemanticContainer(GeneralViewBindingConnectorProjectData.SemanticIds.A11_ID)
                .setExpectedSourceReference(GeneralViewBindingConnectorProjectData.SemanticIds.I11_ID)
                .setExpectedTargetReference(GeneralViewBindingConnectorProjectData.SemanticIds.I11TEST_ID)
                .run(this.verifier, newEdge);
    }

    @DisplayName("GIVEN a ActionUsage with existing binding, WHEN reconnecting target, THEN binding should be moved to new the common container (A1) and the source/target should be recomputed")
    @Sql(scripts = { GeneralViewBindingConnectorProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void reconnectTargetSameLevel() {

        var newEdge = this.reconnect(GeneralViewBindingConnectorProjectData.GraphicalIds.BINDING_EDGE_ID, GeneralViewBindingConnectorProjectData.GraphicalIds.I2_ID, ReconnectEdgeKind.TARGET,
                GeneralViewBindingConnectorProjectData.GraphicalIds.I1_ID, GeneralViewBindingConnectorProjectData.GraphicalIds.I2_ID);
        this.createChecker()
                .setExpectedSourceSemanticId(GeneralViewBindingConnectorProjectData.SemanticIds.I1_ID)
                .setExpectedTargetSemanticId(GeneralViewBindingConnectorProjectData.SemanticIds.I2_ID)
                // Semantic container should change from A0 to A1
                .setExpectedSemanticContainer(GeneralViewBindingConnectorProjectData.SemanticIds.A1_ID)
                .setExpectedSourceReference(GeneralViewBindingConnectorProjectData.SemanticIds.I1_ID)
                // No more feature chain required
                .setExpectedTargetReference(GeneralViewBindingConnectorProjectData.SemanticIds.I2_ID)
                .run(this.verifier, newEdge);
    }

    private ConnectorAsUsageChecker<BindingConnectorAsUsage> createChecker() {
        return new ConnectorAsUsageChecker<>(this.identityService, this.semanticCheckerService, BindingConnectorAsUsage.class);
    }

    private AtomicReference<String> reconnect(String edgeId, String newTarget, ReconnectEdgeKind reconnectionKind, String expectedSourceGraplicalId, String expectedTargetGraplicalId) {

        this.verifier.then(() -> this.edgeReconnectionTester.reconnectEdge(GeneralViewBindingConnectorProjectData.EDITING_CONTEXT_ID,
                this.diagram,
                edgeId,
                newTarget,
                reconnectionKind));

        var result = new AtomicReference<String>();
        IDiagramChecker diagramCheckerTarget = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);

            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            result.set(newEdge.getTargetObjectId());
            assertThat(newEdge)
                    .hasSourceId(expectedSourceGraplicalId)
                    .hasTargetId(expectedTargetGraplicalId)
                    .extracting(Edge::getStyle, EDGE_STYLE)
                    .hasSourceArrow(ArrowStyle.None)
                    .hasTargetArrow(ArrowStyle.None);
            assertThat(newEdge)
                    .extracting(e -> e.getCenterLabel().text()).isEqualTo("=");
        };

        this.diagramCheckerService.checkDiagram(diagramCheckerTarget, this.diagram, this.verifier);

        return result;
    }

    private AtomicReference<String> createEdge(String sourceNodeId, String targetNodeId, String expectedSourceGraphicalId, String expectedTargetGraphicalId) {
        String creationToolId = this.diagramDescriptionIdProvider.getEdgeCreationToolId(
                this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getBehavior_Parameter()),
                "New Binding Connector As Usage (bind)");
        this.verifier.then(() -> this.edgeCreationTester.createEdgeUsingNodeId(GeneralViewBindingConnectorProjectData.EDITING_CONTEXT_ID,
                this.diagram,
                sourceNodeId,
                targetNodeId,
                creationToolId));

        var result = new AtomicReference<String>();

        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(0)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);
            List<Edge> newEdges = this.diagramComparator.newEdges(initialDiagram, newDiagram);
            assertThat(newEdges).hasSize(1).first(EDGE)
                    .hasSourceId(expectedSourceGraphicalId)
                    .hasTargetId(expectedTargetGraphicalId)
                    .extracting(Edge::getStyle, EDGE_STYLE)
                    .hasSourceArrow(ArrowStyle.None)
                    .hasTargetArrow(ArrowStyle.None);
            assertThat(newEdges).hasSize(1).first(EDGE)
                    .extracting(e -> e.getCenterLabel().text()).isEqualTo("=");
            result.set(newEdges.get(0).getTargetObjectId());
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);
        return result;
    }

}
