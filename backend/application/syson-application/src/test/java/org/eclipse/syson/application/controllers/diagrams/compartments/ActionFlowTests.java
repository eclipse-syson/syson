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
package org.eclipse.syson.application.controllers.diagrams.compartments;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramAssertions.assertThat;

import java.time.Duration;
import java.util.List;
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
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeCreationTester;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeReconnectionTester;
import org.eclipse.syson.application.data.ActionFlowCompartmentTestProjectData;
import org.eclipse.syson.diagram.common.view.nodes.StartActionNodeDescriptionProvider;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.SuccessionAsUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Tests several actions on the Action Flow compartment on an Interconnection View diagram.
 *
 * @author Arthur Daussy
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ActionFlowTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

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

    private SemanticCheckerService semanticCheckerService;

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(), ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID, ActionFlowCompartmentTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        this.semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                ActionFlowCompartmentTestProjectData.SemanticIds.ROOT_ACTION_USAGE_ID);
    }

    @DisplayName("GIVEN two ActionUsages nested in each other, WHEN using the 'New succession' tool between them, THEN a SuccessionAsUsage should not be created and a message should be displayed to the user")
    @GivenSysONServer({ ActionFlowCompartmentTestProjectData.SCRIPT_PATH })
    @Test
    public void createSuccesionBetweenNestedActions() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var creationToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()), "New Succession");

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable creationToolRunnable = () -> this.edgeCreationTester.runSingleClickOnTwoDiagramElementsTool(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                ActionFlowCompartmentTestProjectData.GraphicalIds.ROOT_ACTION_USAGE,
                ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION1_ID,
                creationToolId,
                List.of("Can't create cross container SuccessionAsUsage"));

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(0)
                    .hasNewEdgeCount(0)
                    .check(diagram.get(), newDiagram);
        });

        Runnable semanticCheck = this.semanticCheckerService.checkElement(ActionUsage.class, () -> ActionFlowCompartmentTestProjectData.SemanticIds.ROOT_ACTION_USAGE_ID, rootActionUsage -> {
            assertThat(EMFUtils.allContainedObjectOfType(rootActionUsage, SuccessionAsUsage.class).count()).isEqualTo(1);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(creationToolRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN two ActionUsages nested in each other, WHEN using the 'New Transition' tool between them, THEN a TransitionUsage should not be created and a message should be displayed to the user")
    @GivenSysONServer({ ActionFlowCompartmentTestProjectData.SCRIPT_PATH })
    @Test
    public void createTransitionBetweenNestedActions() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var creationToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()), "New Transition");

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable creationToolRunnable = () -> this.edgeCreationTester.runSingleClickOnTwoDiagramElementsTool(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                ActionFlowCompartmentTestProjectData.GraphicalIds.ROOT_ACTION_USAGE,
                ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION1_ID,
                creationToolId,
                List.of("Can't create cross container TransitionUsage"));

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(0)
                    .hasNewEdgeCount(0)
                    .check(diagram.get(), newDiagram);
        });

        Runnable semanticCheck = this.semanticCheckerService.checkElement(ActionUsage.class, () -> ActionFlowCompartmentTestProjectData.SemanticIds.ROOT_ACTION_USAGE_ID, rootActionUsage -> {
            assertThat(EMFUtils.allContainedObjectOfType(rootActionUsage, SuccessionAsUsage.class).count()).isEqualTo(1);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(creationToolRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN two ActionUsage, WHEN using the 'New Transition' tool between them, THEN a TransitionUsage is created between the two actions")
    @GivenSysONServer({ ActionFlowCompartmentTestProjectData.SCRIPT_PATH })
    @Test
    public void createTransitionUsageBetweenSubActions() {
        var flux = this.givenSubscriptionToDiagram();


        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var creationToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()), "New Transition");

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable creationToolRunnable = () -> this.edgeCreationTester.createEdgeUsingNodeId(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION1_ID,
                ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION2_ID,
                creationToolId);

        AtomicReference<String> newTransitionUsageId = new AtomicReference<>();
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(0)
                    .hasNewEdgeCount(1)
                    .check(diagram.get(), newDiagram);
            Edge newEdge = this.diagramComparator.newEdges(diagram.get(), newDiagram).get(0);
            newTransitionUsageId.set(newEdge.getTargetObjectId());
            assertThat(newEdge).hasSourceId(ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION1_ID);
            assertThat(newEdge).hasTargetId(ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION2_ID);
            assertThat(newEdge.getStyle()).hasTargetArrow(ArrowStyle.InputArrow);
        });

        Runnable semanticCheck = this.semanticCheckerService.checkElement(TransitionUsage.class, () -> newTransitionUsageId.get(), transitionUsage -> {
            assertThat(this.identityService.getId(transitionUsage.getSource())).isEqualTo(ActionFlowCompartmentTestProjectData.SemanticIds.SUB_ACTION1_ID);
            assertThat(this.identityService.getId(transitionUsage.getTarget())).isEqualTo(ActionFlowCompartmentTestProjectData.SemanticIds.SUB_ACTION2_ID);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(creationToolRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a TransitionUsage, WHEN reconnecting the target, THEN the new target of the TransitionUsage is correct")
    @GivenSysONServer({ ActionFlowCompartmentTestProjectData.SCRIPT_PATH })
    @Test
    public void reconnectTransitionUsageTarget() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable reconnectRunnable = () -> this.edgeReconnectionTester.reconnectEdge(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                ActionFlowCompartmentTestProjectData.GraphicalIds.TRANSITION_A2_A3_ID,
                ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION1_ID,
                ReconnectEdgeKind.TARGET);

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(diagram.get(), newDiagram);

            Edge newEdge = this.diagramComparator.newEdges(diagram.get(), newDiagram).get(0);
            assertThat(newEdge).hasSourceId(ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION2_ID);
            assertThat(newEdge).hasTargetId(ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION1_ID);
            assertThat(newEdge.getStyle()).hasTargetArrow(ArrowStyle.InputArrow);
        });

        Runnable semanticCheck = this.semanticCheckerService.checkElement(TransitionUsage.class, () -> ActionFlowCompartmentTestProjectData.SemanticIds.TRANSITION_A2_A3_ID, transitionUsage -> {
            assertThat(this.identityService.getId(transitionUsage.getSource())).isEqualTo(ActionFlowCompartmentTestProjectData.SemanticIds.SUB_ACTION2_ID);
            assertThat(this.identityService.getId(transitionUsage.getTarget())).isEqualTo(ActionFlowCompartmentTestProjectData.SemanticIds.SUB_ACTION1_ID);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(reconnectRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a TransitionUsage, WHEN reconnecting the target, THEN the reconnection to a TransitionUsage should be forbidden")
    @GivenSysONServer({ ActionFlowCompartmentTestProjectData.SCRIPT_PATH })
    @Test
    public void reconnectTransitionUsageTargetOnTransitionForbidden() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable reconnectRunnable = () -> this.edgeReconnectionTester.reconnectEdge(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                ActionFlowCompartmentTestProjectData.GraphicalIds.TRANSITION_A2_A3_ID,
                ActionFlowCompartmentTestProjectData.GraphicalIds.TRANSITION_A2_A3_ID,
                ReconnectEdgeKind.TARGET);

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(0)
                    .check(diagram.get(), newDiagram);

            Edge existingEdge = newDiagram.getEdges().stream().filter(e -> e.getId().equals(ActionFlowCompartmentTestProjectData.GraphicalIds.TRANSITION_A2_A3_ID)).findFirst().get();
            assertThat(existingEdge).hasSourceId(ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION2_ID);
            assertThat(existingEdge).hasTargetId(ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION3_ID);
            assertThat(existingEdge.getStyle()).hasTargetArrow(ArrowStyle.InputArrow);
        });

        Runnable semanticCheck = this.semanticCheckerService.checkElement(TransitionUsage.class, () -> ActionFlowCompartmentTestProjectData.SemanticIds.TRANSITION_A2_A3_ID, transitionUsage -> {
            assertThat(this.identityService.getId(transitionUsage.getSource())).isEqualTo(ActionFlowCompartmentTestProjectData.SemanticIds.SUB_ACTION2_ID);
            assertThat(this.identityService.getId(transitionUsage.getTarget())).isEqualTo(ActionFlowCompartmentTestProjectData.SemanticIds.SUB_ACTION3_ID);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(reconnectRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a TransitionUsage,WHEN reconnecting the source, THEN the new source of the TransitionUsage is correct")
    @GivenSysONServer({ ActionFlowCompartmentTestProjectData.SCRIPT_PATH })
    @Test
    public void reconnectTransitionUsageSource() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable reconnectRunnable = () -> this.edgeReconnectionTester.reconnectEdge(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                ActionFlowCompartmentTestProjectData.GraphicalIds.TRANSITION_A2_A3_ID,
                ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION1_ID,
                ReconnectEdgeKind.SOURCE);

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(diagram.get(), newDiagram);

            Edge newEdge = this.diagramComparator.newEdges(diagram.get(), newDiagram).get(0);
            assertThat(newEdge).hasSourceId(ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION1_ID);
            assertThat(newEdge).hasTargetId(ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION3_ID);
            assertThat(newEdge.getStyle()).hasTargetArrow(ArrowStyle.InputArrow);
        });

        Runnable semanticCheck = this.semanticCheckerService.checkElement(TransitionUsage.class, () -> ActionFlowCompartmentTestProjectData.SemanticIds.TRANSITION_A2_A3_ID, transitionUsage -> {
            assertThat(this.identityService.getId(transitionUsage.getSource())).isEqualTo(ActionFlowCompartmentTestProjectData.SemanticIds.SUB_ACTION1_ID);
            assertThat(this.identityService.getId(transitionUsage.getTarget())).isEqualTo(ActionFlowCompartmentTestProjectData.SemanticIds.SUB_ACTION3_ID);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(reconnectRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN two ActionUsage, WHEN using the 'New Succession' tool between them, THEN a SuccessionAsUsage is created between the two actions")
    @GivenSysONServer({ ActionFlowCompartmentTestProjectData.SCRIPT_PATH })
    @Test
    public void createSuccessionBetweenSubActions() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var creationToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()), "New Succession");

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable creationToolRunnable = () -> this.edgeCreationTester.createEdgeUsingNodeId(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION1_ID,
                ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION2_ID,
                creationToolId);

        AtomicReference<String> newSuccessionId = new AtomicReference<>();
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(diagram.get(), newDiagram);
            Edge newEdge = this.diagramComparator.newEdges(diagram.get(), newDiagram).get(0);
            newSuccessionId.set(newEdge.getTargetObjectId());
            assertThat(newEdge).hasSourceId(ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION1_ID);
            assertThat(newEdge).hasTargetId(ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION2_ID);
            assertThat(newEdge.getStyle()).hasTargetArrow(ArrowStyle.InputArrow);
        });

        Runnable semanticCheck = this.semanticCheckerService.checkElement(SuccessionAsUsage.class, () -> newSuccessionId.get(), successionAsUsage -> {
            assertThat(this.identityService.getId(successionAsUsage.getSourceFeature())).isEqualTo(ActionFlowCompartmentTestProjectData.SemanticIds.SUB_ACTION1_ID);
            assertThat(successionAsUsage.getTargetFeature()).hasSize(1)
                    .allMatch(targetFeature -> ActionFlowCompartmentTestProjectData.SemanticIds.SUB_ACTION2_ID.equals(this.identityService.getId(targetFeature)));
            assertThat(EMFUtils.allContainedObjectOfType(successionAsUsage, ReferenceUsage.class).toList()).allMatch(refUsage -> refUsage.getAliasIds().isEmpty());
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(creationToolRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN SuccessionAsUsage from 2 ActionUsages, WHEN reconnecting the target to the 'done' usage, THEN the SuccessionAsUsage should point to the 'done' action")
    @GivenSysONServer({ ActionFlowCompartmentTestProjectData.SCRIPT_PATH })
    @Test
    public void reconnectSuccessionToDone() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable reconnectRunnable = () -> this.edgeReconnectionTester.reconnectEdge(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                ActionFlowCompartmentTestProjectData.GraphicalIds.SUCCESSION_A1_A2_ID,
                ActionFlowCompartmentTestProjectData.GraphicalIds.DONE_ID,
                ReconnectEdgeKind.TARGET);

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(diagram.get(), newDiagram);

            Edge newEdge = this.diagramComparator.newEdges(diagram.get(), newDiagram).get(0);
            assertThat(newEdge).hasSourceId(ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION1_ID);
            assertThat(newEdge).hasTargetId(ActionFlowCompartmentTestProjectData.GraphicalIds.DONE_ID);
            assertThat(newEdge.getStyle()).hasTargetArrow(ArrowStyle.InputArrow);
        });

        Runnable semanticCheck = this.semanticCheckerService.checkElement(SuccessionAsUsage.class, () -> ActionFlowCompartmentTestProjectData.SemanticIds.SUCCESSION_AS_USAGE_ID, successionAsUsage -> {
            assertThat(this.identityService.getId(successionAsUsage.getSourceFeature())).isEqualTo(ActionFlowCompartmentTestProjectData.SemanticIds.SUB_ACTION1_ID);
            assertThat(successionAsUsage.getTargetFeature()).hasSize(1)
                    .allMatch(targetFeature -> new UtilService().isStandardDoneAction(targetFeature));
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(reconnectRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a start action and an ActionUsage, WHEN using the 'New Succession' tool between them, THEN a SuccessionAsUsage is created between the 'start' and the selected ActionUsage")
    @GivenSysONServer({ ActionFlowCompartmentTestProjectData.SCRIPT_PATH })
    @Test
    public void createSuccessionFromStart() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var creationToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(StartActionNodeDescriptionProvider.START_ACTION_NAME),
                "New Succession");

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable creationToolRunnable = () -> this.edgeCreationTester.createEdgeUsingNodeId(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                ActionFlowCompartmentTestProjectData.GraphicalIds.START_ID,
                ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION2_ID,
                creationToolId);

        AtomicReference<String> newSuccessionId = new AtomicReference<>();
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(diagram.get(), newDiagram);
            Edge newEdge = this.diagramComparator.newEdges(diagram.get(), newDiagram).get(0);
            newSuccessionId.set(newEdge.getTargetObjectId());
            assertThat(newEdge).hasSourceId(ActionFlowCompartmentTestProjectData.GraphicalIds.START_ID);
            assertThat(newEdge).hasTargetId(ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION2_ID);
            assertThat(newEdge.getStyle()).hasTargetArrow(ArrowStyle.InputArrow);
        });

        Runnable semanticCheck = this.semanticCheckerService.checkElement(SuccessionAsUsage.class, () -> newSuccessionId.get(), successionAsUsage -> {
            assertThat(successionAsUsage.getSourceFeature()).matches(sourceFeature -> new UtilService().isStandardStartAction(sourceFeature));
            assertThat(successionAsUsage.getTargetFeature()).hasSize(1)
                    .allMatch(targetFeature -> ActionFlowCompartmentTestProjectData.SemanticIds.SUB_ACTION2_ID.equals(this.identityService.getId(targetFeature)));
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(creationToolRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a start action and a done action, WHEN using the 'New Succession' tool between them, THEN a SuccessionAsUsage is created between the 'start' membership to the 'done' membership")
    @GivenSysONServer({ ActionFlowCompartmentTestProjectData.SCRIPT_PATH })
    @Test
    public void createSuccessionFromStartToDone() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var creationToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(StartActionNodeDescriptionProvider.START_ACTION_NAME),
                "New Succession");

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable creationToolRunnable = () -> this.edgeCreationTester.createEdgeUsingNodeId(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                ActionFlowCompartmentTestProjectData.GraphicalIds.START_ID,
                ActionFlowCompartmentTestProjectData.GraphicalIds.DONE_ID,
                creationToolId);

        AtomicReference<String> newSuccessionId = new AtomicReference<>();
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(diagram.get(), newDiagram);
            Edge newEdge = this.diagramComparator.newEdges(diagram.get(), newDiagram).get(0);
            newSuccessionId.set(newEdge.getTargetObjectId());
            assertThat(newEdge).hasSourceId(ActionFlowCompartmentTestProjectData.GraphicalIds.START_ID);
            assertThat(newEdge).hasTargetId(ActionFlowCompartmentTestProjectData.GraphicalIds.DONE_ID);
            assertThat(newEdge.getStyle()).hasTargetArrow(ArrowStyle.InputArrow);
        });

        Runnable semanticCheck = this.semanticCheckerService.checkElement(SuccessionAsUsage.class, () -> newSuccessionId.get(), successionAsUsage -> {
            assertThat(successionAsUsage.getSourceFeature()).matches(sourceFeature -> new UtilService().isStandardStartAction(sourceFeature));
            assertThat(successionAsUsage.getTargetFeature()).hasSize(1)
                    .allMatch(targetFeature -> new UtilService().isStandardDoneAction(targetFeature));
            assertThat(EMFUtils.allContainedObjectOfType(successionAsUsage, ReferenceUsage.class))
                    .hasSize(2)
                    .allMatch(refUsage -> refUsage.getAliasIds().isEmpty());
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(creationToolRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a sub action node and a done action, WHEN using the 'New Transition' tool between them, THEN a TransitionUsage is created between the sub action to the 'done' element. The new TransitionUsage is stored in the container action")
    @GivenSysONServer({ ActionFlowCompartmentTestProjectData.SCRIPT_PATH })
    @Test
    public void createTransitionUsageToDone() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var creationToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()), "New Transition");

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable creationToolRunnable = () -> this.edgeCreationTester.createEdgeUsingNodeId(ActionFlowCompartmentTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION1_ID,
                ActionFlowCompartmentTestProjectData.GraphicalIds.DONE_ID,
                creationToolId);

        AtomicReference<String> newTransitionId = new AtomicReference<>();
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(0)
                    .hasNewEdgeCount(1)
                    .check(diagram.get(), newDiagram);
            Edge newEdge = this.diagramComparator.newEdges(diagram.get(), newDiagram).get(0);
            newTransitionId.set(newEdge.getTargetObjectId());
            assertThat(newEdge).hasSourceId(ActionFlowCompartmentTestProjectData.GraphicalIds.SUB_ACTION1_ID);
            assertThat(newEdge).hasTargetId(ActionFlowCompartmentTestProjectData.GraphicalIds.DONE_ID);
            assertThat(newEdge.getStyle()).hasTargetArrow(ArrowStyle.InputArrow);
        });

        Runnable semanticCheck = this.semanticCheckerService.checkElement(TransitionUsage.class, () -> newTransitionId.get(), transitionUsage -> {
            assertThat(transitionUsage.getSource().getName()).isEqualTo("subAction1");
            assertThat(transitionUsage.getTarget()).matches(targetFeature -> new UtilService().isStandardDoneAction(targetFeature));
            assertThat(transitionUsage.getOwningType().getName()).matches("RootAction"::equals);
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
