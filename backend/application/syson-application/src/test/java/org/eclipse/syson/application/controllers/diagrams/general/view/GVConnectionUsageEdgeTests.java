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
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.diagrams.events.ReconnectEdgeKind;
import org.eclipse.sirius.components.diagrams.tests.assertions.DiagramAssertions;
import org.eclipse.sirius.components.diagrams.tests.graphql.EditLabelMutationRunner;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.ConnectorAsUsageChecker;
import org.eclipse.syson.application.controllers.diagrams.checkers.DiagramCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.IDiagramChecker;
import org.eclipse.syson.application.controllers.diagrams.testers.DirectEditTester;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeCreationTester;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeReconnectionTester;
import org.eclipse.syson.application.data.EdgeConnectionUsageTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.ConnectionUsage;
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
 * Tests the edition of {@link ConnectionUsage} as edges in the general view.
 *
 * @author Arthur Daussy
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVConnectionUsageEdgeTests extends AbstractIntegrationTests {

    private static final String EDITING_CONTEXT_ID = EdgeConnectionUsageTestProjectData.EDITING_CONTEXT_ID;

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
    private DiagramComparator diagramComparator;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private EdgeCreationTester edgeCreationTester;

    @Autowired
    private IIdentityService identityService;

    @Autowired
    private EdgeReconnectionTester edgeReconnectionTester;

    @Autowired
    private EditLabelMutationRunner editLabelMutationRunner;

    private DiagramDescriptionIdProvider diagramDescriptionIdProvider;

    private StepVerifier.Step<DiagramRefreshedEventPayload> verifier;

    private AtomicReference<Diagram> diagram;

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private DiagramCheckerService diagramCheckerService;

    private SemanticCheckerService semanticCheckerService;

    private DirectEditTester directEditTester;

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                EDITING_CONTEXT_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.diagram = this.givenDiagram.getDiagram(this.verifier);
        DiagramDescription diagramDescription = this.givenDiagramDescription.getDiagramDescription(EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        this.diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);
        this.diagramCheckerService = new DiagramCheckerService(this.diagramComparator, this.descriptionNameGenerator);
        this.semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, EDITING_CONTEXT_ID,
                EdgeConnectionUsageTestProjectData.SemanticIds.PACKAGE1_ID);
        this.directEditTester = new DirectEditTester(this.editLabelMutationRunner, EdgeConnectionUsageTestProjectData.EDITING_CONTEXT_ID);

    }

    @AfterEach
    public void tearDown() {
        if (this.verifier != null) {
            this.verifier.thenCancel()
                    .verify(Duration.ofSeconds(10));
        }
    }

    @DisplayName("Given two PartUsages in a package, WHEN using an Edge tool to create a ConnectionUsage in between, THEN a ConnectionUsage edge is created")
    @Test
    @Sql(scripts = { EdgeConnectionUsageTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void createConnectionUsageOnParts() {
        AtomicReference<String> newEdge = this.createEdge(
                this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()),
                EdgeConnectionUsageTestProjectData.GraphicalIds.PART1_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.PART2_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.PART1_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.PART2_ID,
                "connection1");

        this.createChecker()
                .setExpectedSourceSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.PART1_ID)
                .setExpectedTargetSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.PART2_ID)
                .setExpectedSemanticContainer(EdgeConnectionUsageTestProjectData.SemanticIds.PACKAGE1_ID)
                .setExpectedSourceReference(EdgeConnectionUsageTestProjectData.SemanticIds.PART1_ID)
                .setExpectedTargetReference(EdgeConnectionUsageTestProjectData.SemanticIds.PART2_ID)
                .run(this.verifier, newEdge);
    }

    @DisplayName("Given two PartUsages contained in a common context, WHEN using an Edge tool to create a ConnectionUsage in between, THEN a ConnectionUsage edge is created")
    @Test
    @Sql(scripts = { EdgeConnectionUsageTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void createConnectionUsageOnPartsWithCommonContext() {
        AtomicReference<String> newEdge = this.createEdge(
                this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()),
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_PART1_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_PART2_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_PART1_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_PART2_ID,
                "connection2");

        this.createChecker()
                .setExpectedSourceSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART1_ID)
                .setExpectedTargetSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART2_ID)
                .setExpectedSemanticContainer(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ID)
                .setExpectedSourceReference(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART1_ID)
                .setExpectedTargetReference(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART2_ID)
                .run(this.verifier, newEdge);
    }


    @DisplayName("Given two ItemUsage contained in a common context PartUsage, WHEN using an Edge tool to create a ConnectionUsage in between, THEN a ConnectionUsage edge is created with valid " +
            "feature chain")
    @Test
    @Sql(scripts = { EdgeConnectionUsageTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void createConnectionUsageOnItemUsageWithCommonContext() {
        AtomicReference<String> newEdge = this.createEdge(
                this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getItemUsage()),
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_ITEM1_IN_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_ITEM2_OUT_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_ITEM1_IN_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_ITEM2_OUT_ID,
                "connection2");

        this.createChecker()
                .setExpectedSourceSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ITEM1IN_ID)
                .setExpectedTargetSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ITEM2_ID)
                .setExpectedSemanticContainer(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ID)
                .setExpectedSourceFeatureChain(List.of(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART1_ID, EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ITEM1IN_ID))
                .setExpectedTargetFeatureChain(List.of(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART2_ID, EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ITEM2_ID))
                .run(this.verifier, newEdge);
    }

    @DisplayName("Given two PortUsage contained in a common context PartUsage, WHEN using an Edge tool to create a ConnectionUsage in between, THEN a ConnectionUsage edge is created with valid " +
            "feature chain")
    @Test
    @Sql(scripts = { EdgeConnectionUsageTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void createConnectionUsageOnPortUsageUsageWithCommonContext() {
        AtomicReference<String> newEdge = this.createEdge(
                this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort()),
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_PORT11_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_PORT22_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_PORT11_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_PORT22_ID,
                "connection2");

        this.createChecker()
                .setExpectedSourceSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.PORT11_ID)
                .setExpectedTargetSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.PORT22_ID)
                .setExpectedSemanticContainer(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ID)
                .setExpectedSourceFeatureChain(List.of(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART1_ID, EdgeConnectionUsageTestProjectData.SemanticIds.PORT11_ID))
                .setExpectedTargetFeatureChain(List.of(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART2_ID, EdgeConnectionUsageTestProjectData.SemanticIds.PORT22_ID))
                .run(this.verifier, newEdge);
    }

    @DisplayName("Given two inherited PortUsage contained in a common context PartUsage, WHEN using an Edge tool to create a ConnectionUsage in between, THEN a ConnectionUsage edge is created with " +
            "valid " +
            "feature chain")
    @Test
    @Sql(scripts = { EdgeConnectionUsageTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void createConnectionUsageOnInheritedPortUsageUsageWithCommonContext() {
        AtomicReference<String> newEdge = this.createEdge(
                this.descriptionNameGenerator.getInheritedBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort()),
                EdgeConnectionUsageTestProjectData.GraphicalIds.INHERITED_PD1_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.INHERITED_PD2_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.INHERITED_PD1_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.INHERITED_PD2_ID,
                "connection2");

        this.createChecker()
                .setExpectedSourceSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.PD1_ID)
                .setExpectedTargetSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.PD2_ID)
                .setExpectedSemanticContainer(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ID)
                .setExpectedSourceFeatureChain(List.of(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART1_ID, EdgeConnectionUsageTestProjectData.SemanticIds.PD1_ID))
                .setExpectedTargetFeatureChain(List.of(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART2_ID, EdgeConnectionUsageTestProjectData.SemanticIds.PD2_ID))
                .run(this.verifier, newEdge);
    }

    @DisplayName("Given a ConnectionUsage, WHEN reconnecting the source to a new source within the same context , THEN a ConnectionUsage updated but the container is not changed")
    @Test
    @Sql(scripts = { EdgeConnectionUsageTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void reconnectSourceSameContext() {
        var newEdge = this.reconnect(EdgeConnectionUsageTestProjectData.GraphicalIds.CONNECTION_0_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_PART1_ID, ReconnectEdgeKind.SOURCE,
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_PART1_ID, EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_ITEM2_OUT_ID);

        this.createChecker()
                .setExpectedSourceSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART1_ID)
                .setExpectedTargetSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ITEM2_ID)
                .setExpectedSemanticContainer(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ID)
                .setExpectedSourceReference(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART1_ID)
                .setExpectedTargetFeatureChain(List.of(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART2_ID, EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ITEM2_ID))
                .run(this.verifier, newEdge);
    }

    @DisplayName("Given a ConnectionUsage, WHEN reconnecting the source to a new source within a new context, THEN a ConnectionUsage updated and the container is changed")
    @Test
    @Sql(scripts = { EdgeConnectionUsageTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void reconnectSourceChangeContext() {
        var newEdge = this.reconnect(EdgeConnectionUsageTestProjectData.GraphicalIds.CONNECTION_0_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.PORT1_ID, ReconnectEdgeKind.SOURCE,
                EdgeConnectionUsageTestProjectData.GraphicalIds.PORT1_ID, EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_ITEM2_OUT_ID);

        this.createChecker()
                .setExpectedSourceSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.PORT1_ID)
                .setExpectedTargetSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ITEM2_ID)
                .setExpectedSemanticContainer(EdgeConnectionUsageTestProjectData.SemanticIds.PACKAGE1_ID)
                .setExpectedSourceReference(EdgeConnectionUsageTestProjectData.SemanticIds.PORT1_ID)
                .setExpectedTargetReference(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ITEM2_ID)
                .run(this.verifier, newEdge);
    }

    @DisplayName("Given a ConnectionUsage, WHEN reconnecting the target to a new target within the same context, THEN a ConnectionUsage is updated but the container is not changed")
    @Test
    @Sql(scripts = { EdgeConnectionUsageTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void reconnectTargetSameContext() {
        var newEdge = this.reconnect(EdgeConnectionUsageTestProjectData.GraphicalIds.CONNECTION_0_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_PART2_ID, ReconnectEdgeKind.TARGET,
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_ITEM1_IN_ID, EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_PART2_ID);

        this.createChecker()
                .setExpectedSourceSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ITEM1IN_ID)
                .setExpectedTargetSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART2_ID)
                .setExpectedSemanticContainer(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ID)
                .setExpectedSourceFeatureChain(List.of(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART1_ID, EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ITEM1IN_ID))
                .setExpectedTargetReference(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART2_ID)
                .run(this.verifier, newEdge);
    }

    @DisplayName("Given a ConnectionUsage, WHEN reconnecting the target to a new target in a new context , THEN a ConnectionUsage is updated and the container is changed")
    @Test
    @Sql(scripts = { EdgeConnectionUsageTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void reconnectTargetChangeContext() {
        var newEdge = this.reconnect(EdgeConnectionUsageTestProjectData.GraphicalIds.CONNECTION_0_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.PORT2_ID, ReconnectEdgeKind.TARGET,
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_ITEM1_IN_ID, EdgeConnectionUsageTestProjectData.GraphicalIds.PORT2_ID);

        this.createChecker()
                .setExpectedSourceSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ITEM1IN_ID)
                .setExpectedTargetSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.PORT2_ID)
                .setExpectedSemanticContainer(EdgeConnectionUsageTestProjectData.SemanticIds.PACKAGE1_ID)
                .setExpectedSourceReference(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ITEM1IN_ID)
                .setExpectedTargetReference(EdgeConnectionUsageTestProjectData.SemanticIds.PORT2_ID)
                .run(this.verifier, newEdge);
    }

    @DisplayName("Given a ConnectionUsage, WHEN direct editing its centered label, THEN type of the usage is set")
    @Test
    @Sql(scripts = { EdgeConnectionUsageTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void directEditFeatureTyping() {
        this.directEditTester.checkDirectEditCenteredEdgeLabel(this.verifier, this.diagram, EdgeConnectionUsageTestProjectData.GraphicalIds.CONNECTION_0_ID, "connection0 : ConnectionDefinition1", "connection0 : ConnectionDefinition1");

        this.semanticCheckerService.checkElement(this.verifier, ConnectionUsage.class, () -> EdgeConnectionUsageTestProjectData.SemanticIds.CONNECTION_0_ID, connectionUsage -> {
            assertThat(connectionUsage.getType()).hasSize(2)
                    .allMatch(type -> "ConnectionDefinition1".equals(type.getName()) || "BinaryLinkObject".equals(type.getName()));
        });

    }

    private AtomicReference<String> createEdge(String sourceNodeDescriptionName, String sourceNodeId, String targetNodeId, String expectedSourceGraphicalId, String expectedTargetGraphicalId,
            String expectedLabel) {
        String creationToolId = this.diagramDescriptionIdProvider.getEdgeCreationToolId(
                sourceNodeDescriptionName,
                "New Connection");


        this.verifier.then(() -> this.edgeCreationTester.createEdgeUsingNodeId(EDITING_CONTEXT_ID,
                this.diagram,
                sourceNodeId,
                targetNodeId,
                creationToolId));

        var result = new AtomicReference<String>();

        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(0)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram, true);
            List<Edge> newEdges = this.diagramComparator.newEdges(initialDiagram, newDiagram);
            List<Edge> newVisibleEdges = newEdges.stream().filter(edge -> edge.getState() != ViewModifier.Hidden).toList();
            assertThat(newVisibleEdges).hasSize(1).first(EDGE)
                    .hasSourceId(expectedSourceGraphicalId)
                    .hasTargetId(expectedTargetGraphicalId)
                    .extracting(Edge::getStyle, EDGE_STYLE)
                    .hasSourceArrow(ArrowStyle.None)
                    .hasTargetArrow(ArrowStyle.None);
            assertThat(newVisibleEdges).hasSize(1).first(EDGE)
                    .extracting(e -> e.getCenterLabel().text()).isEqualTo(expectedLabel);
            result.set(newEdges.get(0).getTargetObjectId());
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);
        return result;
    }

    private ConnectorAsUsageChecker<ConnectionUsage> createChecker() {
        return new ConnectorAsUsageChecker<>(this.identityService, this.semanticCheckerService, ConnectionUsage.class);
    }

    private AtomicReference<String> reconnect(String edgeId, String newTarget, ReconnectEdgeKind reconnectionKind, String expectedSourceGraplicalId, String expectedTargetGraplicalId) {

        this.verifier.then(() -> this.edgeReconnectionTester.reconnectEdge(EDITING_CONTEXT_ID,
                this.diagram,
                edgeId,
                newTarget,
                reconnectionKind));

        var result = new AtomicReference<String>();
        IDiagramChecker diagramCheckerTarget = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram, true);

            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            result.set(newEdge.getTargetObjectId());
            DiagramAssertions.assertThat(newEdge)
                    .hasSourceId(expectedSourceGraplicalId)
                    .hasTargetId(expectedTargetGraplicalId)
                    .extracting(Edge::getStyle, EDGE_STYLE)
                    .hasSourceArrow(ArrowStyle.None)
                    .hasTargetArrow(ArrowStyle.None);
        };

        this.diagramCheckerService.checkDiagram(diagramCheckerTarget, this.diagram, this.verifier);

        return result;
    }

}
