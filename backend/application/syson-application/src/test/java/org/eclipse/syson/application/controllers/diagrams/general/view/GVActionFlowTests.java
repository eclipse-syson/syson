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
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.DiagramCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.IDiagramChecker;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeCreationTester;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeReconnectionTester;
import org.eclipse.syson.application.data.ActionFlowCompartmentTestProjectData;
import org.eclipse.syson.application.data.SysONRepresentationDescripionIdentifiers;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.SuccessionAsUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
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
import reactor.test.StepVerifier.Step;

/**
 * Tests several actions on the Action Flow compartment of the General View diagram.
 *
 * @author Arthur Daussy
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVActionFlowTests extends AbstractIntegrationTests {

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
    private IObjectSearchService objectSearchService;

    @Autowired
    private IIdentityService identityService;

    @Autowired
    private EdgeCreationTester edgeCreationTester;

    @Autowired
    private EdgeReconnectionTester edgeReconnectionTester;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private DiagramComparator diagramComparator;

    private DiagramDescriptionIdProvider diagramDescriptionIdProvider;

    private Step<DiagramRefreshedEventPayload> verifier;

    private AtomicReference<Diagram> diagram;

    private DiagramDescription diagramDescription;

    private DiagramCheckerService diagramCheckerService;

    private SemanticCheckerService semanticCheckerService;

    private final IDescriptionNameGenerator descriptionNameGenerator = new GVDescriptionNameGenerator();

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                ActionFlowCompartmentTestProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.diagram = this.givenDiagram.getDiagram(this.verifier);
        this.diagramDescription = this.givenDiagramDescription.getDiagramDescription(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescripionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        this.diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(this.diagramDescription, this.diagramIdProvider);
        this.diagramCheckerService = new DiagramCheckerService(this.diagramComparator, this.descriptionNameGenerator);
        this.semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                ActionFlowCompartmentTestProjectData.SemanticIds.ROOT_ACTION_USAGE);
    }

    @AfterEach
    public void tearDown() {
        if (this.verifier != null) {
            this.verifier.thenCancel()
                    .verify(Duration.ofSeconds(10));
        }
    }

    @Sql(scripts = { ActionFlowCompartmentTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    @DisplayName("Given two ActionUsages nested in each other,"
            + "when using the 'New succession' tool between them, "
            + "then a SuccessionAsUsage should not be created and a message should be displayed to the user")
    public void creatSuccesionBetweenNestedActions() {
        String creationToolId = this.diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()), "New Succession");
        this.verifier.then(() -> this.edgeCreationTester.runSingleClickOnTwoDiagramElementsTool(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                this.diagram,
                ActionFlowCompartmentTestProjectData.GraphicalIds.ROOT_ACTION_USAGE,
                ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION1_ID,
                creationToolId,
                List.of("Can't create cross container SuccessionAsUsage")));

        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(0)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram);
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, ActionUsage.class, () -> ActionFlowCompartmentTestProjectData.SemanticIds.ROOT_ACTION_USAGE, rootActionUsage -> {
            assertThat(EMFUtils.allContainedObjectOfType(rootActionUsage, SuccessionAsUsage.class).count()).isEqualTo(1);
        });
    }

    @Sql(scripts = { ActionFlowCompartmentTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    @DisplayName("Given two ActionUsages nested in each other,"
            + "when using the 'New Transition' tool between them, "
            + "then a TransitionUsage should not be created and a message should be displayed to the user")
    public void creatTransitionBetweenNestedActions() {
        String creationToolId = this.diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()), "New Transition");
        this.verifier.then(() -> this.edgeCreationTester.runSingleClickOnTwoDiagramElementsTool(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                this.diagram,
                ActionFlowCompartmentTestProjectData.GraphicalIds.ROOT_ACTION_USAGE,
                ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION1_ID,
                creationToolId,
                List.of("Can't create cross container TransitionUsage")));

        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(0)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram);
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, ActionUsage.class, () -> ActionFlowCompartmentTestProjectData.SemanticIds.ROOT_ACTION_USAGE, rootActionUsage -> {
            assertThat(EMFUtils.allContainedObjectOfType(rootActionUsage, SuccessionAsUsage.class).count()).isEqualTo(1);
        });
    }

    @Sql(scripts = { ActionFlowCompartmentTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    @DisplayName("Given two ActionUsage,"
            + "when using the 'New Transition' tool between them, "
            + "then a TransitionUsage is created between the two actions")
    public void createTransitionUsageBetweenSubActions() {
        String creationToolId = this.diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()), "New Transition");
        this.verifier.then(() -> this.edgeCreationTester.createEdgeUsingNodeId(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                this.diagram,
                ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION1_ID,
                ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION2_ID,
                creationToolId));

        String[] newTransitionUsageId = new String[1];
        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1) // The TransitionUsage is displayed in the Action compartment
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);
            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            newTransitionUsageId[0] = newEdge.getTargetObjectId();
            assertThat(newEdge).hasSourceId(ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION1_ID);
            assertThat(newEdge).hasTargetId(ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION2_ID);
            assertThat(newEdge.getStyle()).hasTargetArrow(ArrowStyle.InputArrow);
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, TransitionUsage.class, () -> newTransitionUsageId[0], transitionUsage -> {
            assertThat(this.identityService.getId(transitionUsage.getSource())).isEqualTo(ActionFlowCompartmentTestProjectData.SemanticIds.SUB_ACTION1_ID);
            assertThat(this.identityService.getId(transitionUsage.getTarget())).isEqualTo(ActionFlowCompartmentTestProjectData.SemanticIds.SUB_ACTION2_ID);
        });
    }

    @Sql(scripts = { ActionFlowCompartmentTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    @DisplayName("Given a TransitionUsage,"
            + "when reconnecting the target, "
            + "then the new target of the TransitionUsage is correct")
    public void reconnectTransitionUsageTarget() {
        this.verifier.then(() -> this.edgeReconnectionTester.reconnectEdge(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                this.diagram,
                ActionFlowCompartmentTestProjectData.GraphicalIds.TRANSITION_A2_A3_ID,
                ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION1_ID,
                ReconnectEdgeKind.TARGET));

        IDiagramChecker diagramCheckerTarget = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);

            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            assertThat(newEdge).hasSourceId(ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION2_ID);
            assertThat(newEdge).hasTargetId(ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION1_ID);
            assertThat(newEdge.getStyle()).hasTargetArrow(ArrowStyle.InputArrow);
        };

        this.diagramCheckerService.checkDiagram(diagramCheckerTarget, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, TransitionUsage.class, () -> ActionFlowCompartmentTestProjectData.SemanticIds.TRANSITION_A2_A3_ID, transitionUsage -> {
            assertThat(this.identityService.getId(transitionUsage.getSource())).isEqualTo(ActionFlowCompartmentTestProjectData.SemanticIds.SUB_ACTION2_ID);
            assertThat(this.identityService.getId(transitionUsage.getTarget())).isEqualTo(ActionFlowCompartmentTestProjectData.SemanticIds.SUB_ACTION1_ID);
        });
    }

    @Sql(scripts = { ActionFlowCompartmentTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    @DisplayName("Given a TransitionUsage,"
            + "when reconnecting the source, "
            + "then the new source of the TransitionUsage is correct")
    public void reconnectTransitionUsageSource() {
        this.verifier.then(() -> this.edgeReconnectionTester.reconnectEdge(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                this.diagram,
                ActionFlowCompartmentTestProjectData.GraphicalIds.TRANSITION_A2_A3_ID,
                ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION1_ID,
                ReconnectEdgeKind.SOURCE));

        IDiagramChecker diagramCheckerSource = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);

            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            assertThat(newEdge).hasSourceId(ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION1_ID);
            assertThat(newEdge).hasTargetId(ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION3_ID);
            assertThat(newEdge.getStyle()).hasTargetArrow(ArrowStyle.InputArrow);
        };

        this.diagramCheckerService.checkDiagram(diagramCheckerSource, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, TransitionUsage.class, () -> ActionFlowCompartmentTestProjectData.SemanticIds.TRANSITION_A2_A3_ID, transitionUsage -> {
            assertThat(this.identityService.getId(transitionUsage.getSource())).isEqualTo(ActionFlowCompartmentTestProjectData.SemanticIds.SUB_ACTION1_ID);
            assertThat(this.identityService.getId(transitionUsage.getTarget())).isEqualTo(ActionFlowCompartmentTestProjectData.SemanticIds.SUB_ACTION3_ID);
        });
    }

    @Sql(scripts = { ActionFlowCompartmentTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    @DisplayName("Given two ActionUsage,"
            + "when using the 'New Succession' tool between them, "
            + "then a SuccessionAsUsage is created between the two actions")
    public void createSuccessionBetweenSubActions() {
        String creationToolId = this.diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()), "New Succession");
        this.verifier.then(() -> this.edgeCreationTester.createEdgeUsingNodeId(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                this.diagram,
                ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION1_ID,
                ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION2_ID,
                creationToolId));

        String[] newSuccessionId = new String[1];
        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);
            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            newSuccessionId[0] = newEdge.getTargetObjectId();
            assertThat(newEdge).hasSourceId(ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION1_ID);
            assertThat(newEdge).hasTargetId(ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION2_ID);
            assertThat(newEdge.getStyle()).hasTargetArrow(ArrowStyle.InputArrow);
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, SuccessionAsUsage.class, () -> newSuccessionId[0], successionAsUsage -> {
            assertThat(this.identityService.getId(successionAsUsage.getSourceFeature())).isEqualTo(ActionFlowCompartmentTestProjectData.SemanticIds.SUB_ACTION1_ID);
            assertThat(successionAsUsage.getTargetFeature()).hasSize(1)
                    .allMatch(targetFeature -> ActionFlowCompartmentTestProjectData.SemanticIds.SUB_ACTION2_ID.equals(this.identityService.getId(targetFeature)));
            assertThat(EMFUtils.allContainedObjectOfType(successionAsUsage, ReferenceUsage.class).toList()).allMatch(refUsage -> refUsage.getAliasIds().isEmpty());
        });
    }

    @Sql(scripts = { ActionFlowCompartmentTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    @DisplayName("Given SuccessionAsUsage from 2 ActionUsages,"
            + "when reconnecting the target to the 'done' usage, "
            + "then a SuccessionAsUsage should point to the 'done' action")
    public void reconnectSuccessionToDone() {
        this.verifier.then(() -> this.edgeReconnectionTester.reconnectEdge(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                this.diagram,
                ActionFlowCompartmentTestProjectData.GraphicalIds.SUCCESSION_A1_A2_ID,
                ActionFlowCompartmentTestProjectData.GraphicalIds.DONE_ID,
                ReconnectEdgeKind.TARGET));

        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);

            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            assertThat(newEdge).hasSourceId(ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION1_ID);
            assertThat(newEdge).hasTargetId(ActionFlowCompartmentTestProjectData.GraphicalIds.DONE_ID);
            assertThat(newEdge.getStyle()).hasTargetArrow(ArrowStyle.InputArrow);
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, SuccessionAsUsage.class, () -> ActionFlowCompartmentTestProjectData.SemanticIds.SUCCESSION_AS_USAGE_ID, successionAsUsage -> {
            assertThat(this.identityService.getId(successionAsUsage.getSourceFeature())).isEqualTo(ActionFlowCompartmentTestProjectData.SemanticIds.SUB_ACTION1_ID);
            assertThat(successionAsUsage.getTargetFeature()).hasSize(1)
                    .allMatch(targetFeature -> new UtilService().isStandardDoneAction(targetFeature));
        });
    }

    @Sql(scripts = { ActionFlowCompartmentTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    @DisplayName("Given a start action and an ActionUsage,"
            + "when using the 'New Succession' tool between them, "
            + "then a SuccessionAsUsage is created between the 'start' and the selected ActionUsage")
    public void createSuccessionFromStart() {
        String creationToolId = this.diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()), "New Succession");
        this.verifier.then(() -> this.edgeCreationTester.createEdgeUsingNodeId(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                this.diagram,
                ActionFlowCompartmentTestProjectData.GraphicalIds.START_ID,
                ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION2_ID,
                creationToolId));

        String[] newSuccessionId = new String[1];
        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);
            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            newSuccessionId[0] = newEdge.getTargetObjectId();
            assertThat(newEdge).hasSourceId(ActionFlowCompartmentTestProjectData.GraphicalIds.START_ID);
            assertThat(newEdge).hasTargetId(ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION2_ID);
            assertThat(newEdge.getStyle()).hasTargetArrow(ArrowStyle.InputArrow);
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, SuccessionAsUsage.class, () -> newSuccessionId[0], successionAsUsage -> {
            assertThat(successionAsUsage.getSourceFeature()).matches(sourceFeature -> new UtilService().isStandardStartAction(sourceFeature));
            assertThat(successionAsUsage.getTargetFeature()).hasSize(1)
                    .allMatch(targetFeature -> ActionFlowCompartmentTestProjectData.SemanticIds.SUB_ACTION2_ID.equals(this.identityService.getId(targetFeature)));
        });
    }

    @Sql(scripts = { ActionFlowCompartmentTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    @DisplayName("Given a start action and  ActionUsage,"
            + "when using the 'New Succession' tool between them, "
            + "then a SuccessionAsUsage is created between the 'start' membership to the 'done' membership")
    public void createSuccessionFromStartToDone() {
        String creationToolId = this.diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()), "New Succession");
        this.verifier.then(() -> this.edgeCreationTester.createEdgeUsingNodeId(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                this.diagram,
                ActionFlowCompartmentTestProjectData.GraphicalIds.START_ID,
                ActionFlowCompartmentTestProjectData.GraphicalIds.DONE_ID,
                creationToolId));

        String[] newSuccessionId = new String[1];
        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);
            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            newSuccessionId[0] = newEdge.getTargetObjectId();
            assertThat(newEdge).hasSourceId(ActionFlowCompartmentTestProjectData.GraphicalIds.START_ID);
            assertThat(newEdge).hasTargetId(ActionFlowCompartmentTestProjectData.GraphicalIds.DONE_ID);
            assertThat(newEdge.getStyle()).hasTargetArrow(ArrowStyle.InputArrow);
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, SuccessionAsUsage.class, () -> newSuccessionId[0], successionAsUsage -> {
            assertThat(successionAsUsage.getSourceFeature()).matches(sourceFeature -> new UtilService().isStandardStartAction(sourceFeature));
            assertThat(successionAsUsage.getTargetFeature()).hasSize(1)
                    .allMatch(targetFeature -> new UtilService().isStandardDoneAction(targetFeature));
            assertThat(EMFUtils.allContainedObjectOfType(successionAsUsage, ReferenceUsage.class))
                    .hasSize(2)
                    .allMatch(refUsage -> refUsage.getAliasIds().isEmpty());
        });
    }

    @Sql(scripts = { ActionFlowCompartmentTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    @DisplayName("Given a sub action node and a done action,"
            + "when using the 'New Transition' tool between them, "
            + "then a TransitionUsage is created between the decisionNode to the 'done' element. The new TransitionUsage is store in the container action")
    public void createTransitionUsageToDone() {
        String creationToolId = this.diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()), "New Transition");
        this.verifier.then(() -> this.edgeCreationTester.createEdgeUsingNodeId(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                this.diagram,
                ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION1_ID,
                ActionFlowCompartmentTestProjectData.GraphicalIds.DONE_ID,
                creationToolId));

        String[] newSuccessionId = new String[1];
        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1) // TransitionUsage are also added to the previous compartment as node
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);
            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            newSuccessionId[0] = newEdge.getTargetObjectId();
            assertThat(newEdge).hasSourceId(ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION1_ID);
            assertThat(newEdge).hasTargetId(ActionFlowCompartmentTestProjectData.GraphicalIds.DONE_ID);
            assertThat(newEdge.getStyle()).hasTargetArrow(ArrowStyle.InputArrow);
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, TransitionUsage.class, () -> newSuccessionId[0], transitionUsage -> {
            assertThat(transitionUsage.getSource().getName()).isEqualTo("subAction1");
            assertThat(transitionUsage.getTarget()).matches(targetFeature -> new UtilService().isStandardDoneAction(targetFeature));
            assertThat(transitionUsage.getOwningType().getName()).matches(parentName -> "RootAction".equals(parentName));
        });
    }
}
