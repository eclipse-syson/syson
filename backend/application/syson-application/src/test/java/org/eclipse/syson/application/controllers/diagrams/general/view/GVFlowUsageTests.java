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
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramAssertions.assertThat;
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramInstanceOfAssertFactories.EDGE_STYLE;

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
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenCommittedTransaction;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.SysONTestsProperties;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.DiagramCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.IDiagramChecker;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeCreationTester;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeReconnectionTester;
import org.eclipse.syson.application.data.GeneralViewFlowConnectionItemUsagesProjectData;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.sysml.BindingConnectorAsUsage;
import org.eclipse.syson.sysml.FlowUsage;
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
 * Tests on {@link FlowUsage} on the General View Diagram.
 *
 * @author Arthur Daussy
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = { SysONTestsProperties.NO_DEFAULT_LIBRARIES_PROPERTY })
public class GVFlowUsageTests extends AbstractIntegrationTests {

    private final IDescriptionNameGenerator descriptionNameGenerator = new GVDescriptionNameGenerator();

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenCommittedTransaction givenCommittedTransaction;

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
    private EdgeReconnectionTester edgeReconnectionTester;

    @Autowired
    private IIdentityService identityService;

    private DiagramDescriptionIdProvider diagramDescriptionIdProvider;

    private DiagramCheckerService diagramCheckerService;

    private StepVerifier.Step<DiagramRefreshedEventPayload> verifier;

    private SemanticCheckerService semanticCheckerService;

    private AtomicReference<Diagram> diagram;

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewFlowConnectionItemUsagesProjectData.EDITING_CONTEXT_ID,
                GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.diagram = this.givenDiagram.getDiagram(this.verifier);
        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewFlowConnectionItemUsagesProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        this.diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);
        this.diagramCheckerService = new DiagramCheckerService(this.diagramComparator, this.descriptionNameGenerator);
        this.semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewFlowConnectionItemUsagesProjectData.EDITING_CONTEXT_ID,
                GeneralViewFlowConnectionItemUsagesProjectData.SemanticIds.PACKAGE_1_ID);
    }

    @AfterEach
    public void tearDown() {
        if (this.verifier != null) {
            this.verifier.thenCancel()
                    .verify(Duration.ofSeconds(10));
        }
    }

    @DisplayName("GIVEN a SysML Project with ItemUsages on ActionUsage, WHEN creating a FlowUsage between them, THEN an edge should be displayed to represent that new flow")
    @Sql(scripts = { GeneralViewFlowConnectionItemUsagesProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void checkFlowConnectionCreation() {
        this.givenCommittedTransaction.commit();

        String creationToolId = this.diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage()),
                "New Flow (flow)");
        this.verifier.then(() -> this.edgeCreationTester.createEdgeUsingNodeId(GeneralViewFlowConnectionItemUsagesProjectData.EDITING_CONTEXT_ID,
                this.diagram,
                GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_2_OUT_ITEM_ID,
                GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_3_IN_ITEM_ID,
                creationToolId));

        String[] newFlow = new String[1];
        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);
            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            newFlow[0] = newEdge.getTargetObjectId();
            assertThat(newEdge).hasSourceId(GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_2_OUT_ITEM_ID);
            assertThat(newEdge).hasTargetId(GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_3_IN_ITEM_ID);
            assertThat(newEdge.getStyle()).hasTargetArrow(ArrowStyle.InputFillClosedArrow);
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, FlowUsage.class, () -> newFlow[0], flow -> {
            assertThat(this.identityService.getId(flow.getSourceOutputFeature().getOwnedRedefinition().get(0).getRedefinedFeature()))
                    .isEqualTo(GeneralViewFlowConnectionItemUsagesProjectData.SemanticIds.ACTION_USAGE_2_OUT_ITEM_ID);
            assertThat(this.identityService.getId(flow.getTargetInputFeature().getOwnedRedefinition().get(0).getRedefinedFeature()))
                    .isEqualTo(GeneralViewFlowConnectionItemUsagesProjectData.SemanticIds.ACTION_USAGE_3_IN_ITEM_ID);
            assertThat(this.identityService.getId(flow.getSourceFeature())).isEqualTo(GeneralViewFlowConnectionItemUsagesProjectData.SemanticIds.ACTION_USAGE_2_ID);
            assertThat(this.identityService.getId(flow.getTargetFeature().get(0))).isEqualTo(GeneralViewFlowConnectionItemUsagesProjectData.SemanticIds.ACTION_USAGE_3_ID);
        });
    }

    @DisplayName("GIVEN a SysML Project with ItemUsages on ActionUsage, WHEN creating a BindingConnectorAsUsage between them, THEN an edge should be displayed to represent that new binding")
    @Sql(scripts = { GeneralViewFlowConnectionItemUsagesProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void checkItemUsageBindingConnectorAsUsage() {
        this.givenCommittedTransaction.commit();

        String creationToolId = this.diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage()),
                "New Binding Connector As Usage (bind)");
        this.verifier.then(() -> this.edgeCreationTester.createEdgeUsingNodeId(GeneralViewFlowConnectionItemUsagesProjectData.EDITING_CONTEXT_ID,
                this.diagram,
                GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_2_OUT_ITEM_ID,
                GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_3_IN_ITEM_ID,
                creationToolId));

        String[] newBinding = new String[1];
        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);
            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            newBinding[0] = newEdge.getTargetObjectId();
            assertThat(newEdge)
                    .hasSourceId(GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_2_OUT_ITEM_ID)
                    .hasTargetId(GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_3_IN_ITEM_ID)
                    .extracting(Edge::getStyle, EDGE_STYLE)
                    .hasSourceArrow(ArrowStyle.None)
                    .hasTargetArrow(ArrowStyle.None);
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, BindingConnectorAsUsage.class, () -> newBinding[0], binding -> {
            assertThat(this.identityService.getId(binding.getSourceFeature()))
                    .isEqualTo(GeneralViewFlowConnectionItemUsagesProjectData.SemanticIds.ACTION_USAGE_2_OUT_ITEM_ID);
            assertThat(this.identityService.getId(binding.getTargetFeature().get(0).getFeatureTarget()))
                    .isEqualTo(GeneralViewFlowConnectionItemUsagesProjectData.SemanticIds.ACTION_USAGE_3_IN_ITEM_ID);
        });
    }

    @Test
    @DisplayName("GIVEN a FlowUsage, WHEN reconnecting the target, THEN the new target of the FlowUsage is correct")
    @Sql(scripts = { GeneralViewFlowConnectionItemUsagesProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void reconnectFlowUsageTarget() {
        this.givenCommittedTransaction.commit();

        this.verifier.then(() -> this.edgeReconnectionTester.reconnectEdge(GeneralViewFlowConnectionItemUsagesProjectData.EDITING_CONTEXT_ID,
                this.diagram,
                GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.FLOW_CONNECTION_ID,
                GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_3_IN_ITEM_ID,
                ReconnectEdgeKind.TARGET));

        IDiagramChecker diagramCheckerTarget = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);

            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            DiagramAssertions.assertThat(newEdge).hasSourceId(GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_1_OUT_ITEM_ID);
            DiagramAssertions.assertThat(newEdge).hasTargetId(GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_3_IN_ITEM_ID);
            DiagramAssertions.assertThat(newEdge.getStyle()).hasTargetArrow(ArrowStyle.InputFillClosedArrow);
        };

        this.diagramCheckerService.checkDiagram(diagramCheckerTarget, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, FlowUsage.class, () -> GeneralViewFlowConnectionItemUsagesProjectData.SemanticIds.FLOW_CONNECTION_ID, flowUsage -> {
            assertThat(this.identityService.getId(flowUsage.getSourceOutputFeature().getOwnedRedefinition().get(0).getRedefinedFeature()))
                    .isEqualTo(GeneralViewFlowConnectionItemUsagesProjectData.SemanticIds.ACTION_USAGE_1_OUT_ITEM_ID);
            assertThat(this.identityService.getId(flowUsage.getTargetInputFeature().getOwnedRedefinition().get(0).getRedefinedFeature()))
                    .isEqualTo(GeneralViewFlowConnectionItemUsagesProjectData.SemanticIds.ACTION_USAGE_3_IN_ITEM_ID);
            assertThat(this.identityService.getId(flowUsage.getSourceFeature())).isEqualTo(GeneralViewFlowConnectionItemUsagesProjectData.SemanticIds.ACTION_USAGE_1_ID);
            assertThat(this.identityService.getId(flowUsage.getTargetFeature().get(0))).isEqualTo(GeneralViewFlowConnectionItemUsagesProjectData.SemanticIds.ACTION_USAGE_3_ID);
        });
    }

    @Test
    @DisplayName("GIVEN a FlowUsage, WHEN reconnecting the source, THEN the new source of the FlowUsage is correct")
    @Sql(scripts = { GeneralViewFlowConnectionItemUsagesProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void reconnectFlowUsageSource() {
        this.givenCommittedTransaction.commit();

        this.verifier.then(() -> this.edgeReconnectionTester.reconnectEdge(GeneralViewFlowConnectionItemUsagesProjectData.EDITING_CONTEXT_ID,
                this.diagram,
                GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.FLOW_CONNECTION_ID,
                GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_3_OUT_ITEM_ID,
                ReconnectEdgeKind.SOURCE));

        IDiagramChecker diagramCheckerTarget = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);

            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            DiagramAssertions.assertThat(newEdge).hasSourceId(GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_3_OUT_ITEM_ID);
            DiagramAssertions.assertThat(newEdge).hasTargetId(GeneralViewFlowConnectionItemUsagesProjectData.GraphicalIds.ACTION_USAGE_2_IN_ITEM_ID);
            DiagramAssertions.assertThat(newEdge.getStyle()).hasTargetArrow(ArrowStyle.InputFillClosedArrow);
        };

        this.diagramCheckerService.checkDiagram(diagramCheckerTarget, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, FlowUsage.class, () -> GeneralViewFlowConnectionItemUsagesProjectData.SemanticIds.FLOW_CONNECTION_ID,
                flowUsage -> {
                    assertThat(this.identityService.getId(flowUsage.getSourceOutputFeature().getOwnedRedefinition().get(0).getRedefinedFeature()))
                            .isEqualTo(GeneralViewFlowConnectionItemUsagesProjectData.SemanticIds.ACTION_USAGE_3_OUT_ITEM_ID);
                    assertThat(this.identityService.getId(flowUsage.getTargetInputFeature().getOwnedRedefinition().get(0).getRedefinedFeature()))
                            .isEqualTo(GeneralViewFlowConnectionItemUsagesProjectData.SemanticIds.ACTION_USAGE_2_IN_ITEM_ID);
                    assertThat(this.identityService.getId(flowUsage.getSourceFeature())).isEqualTo(GeneralViewFlowConnectionItemUsagesProjectData.SemanticIds.ACTION_USAGE_3_ID);
                    assertThat(this.identityService.getId(flowUsage.getTargetFeature().get(0))).isEqualTo(GeneralViewFlowConnectionItemUsagesProjectData.SemanticIds.ACTION_USAGE_2_ID);
                });
    }

}
