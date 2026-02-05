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
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramInstanceOfAssertFactories.EDGE;
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramInstanceOfAssertFactories.EDGE_STYLE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.diagrams.ArrowStyle;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.diagrams.Label;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.diagrams.events.ReconnectEdgeKind;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeCreationTester;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeReconnectionTester;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.SatisfyRequirementUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.helper.LabelConstants;
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
 * {@link SatisfyRequirementUsage} related test in General View.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVSatisfyRequirementTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private DiagramComparator diagramComparator;

    @Autowired
    private ToolTester toolTester;

    @Autowired
    private EdgeCreationTester edgeCreationTester;

    @Autowired
    private EdgeReconnectionTester edgeReconnectionTester;

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(), GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a SatisfyRequirement edge, WHEN reconnecting the source to a new valid source, THEN the edge is connected to the new source")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void reconnectSatisfyEdgeSource() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var edgeCreationToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(
                this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()),
                "New Satisfy Requirement");

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        AtomicReference<String> satisfyEdgeId = new AtomicReference<>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable createEdgeRunnable = () -> this.edgeCreationTester.createEdgeUsingNodeId(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.REQUIREMENT_USAGE_ID,
                edgeCreationToolId);

        Consumer<Object> diagramContentConsumerAfterNewEdge = assertRefreshedDiagramThat(newDiagram -> {
            var newEdges = this.diagramComparator.newEdges(diagram.get(), newDiagram);
            var newVisibleEdges = newEdges.stream().filter(edge -> edge.getState() != ViewModifier.Hidden).toList();

            assertThat(newVisibleEdges).hasSize(1).first(EDGE)
                    .hasSourceId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID)
                    .hasTargetId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.REQUIREMENT_USAGE_ID)
                    .extracting(Edge::getCenterLabel)
                    .extracting(Label::text)
                    .hasToString(LabelConstants.OPEN_QUOTE + LabelConstants.SATISFY + LabelConstants.CLOSE_QUOTE);

            assertThat(newVisibleEdges).hasSize(1).first(EDGE)
                    .extracting(Edge::getStyle, EDGE_STYLE)
                    .hasSourceArrow(ArrowStyle.None)
                    .hasTargetArrow(ArrowStyle.InputArrow);

            satisfyEdgeId.set(newVisibleEdges.get(0).getId());
        });

        Runnable reconnectEdgeRunnable = () -> this.edgeReconnectionTester.reconnectEdge(
                GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                satisfyEdgeId.get(),
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_USAGE_ID,
                ReconnectEdgeKind.SOURCE);

        Consumer<Object> diagramContentConsumerAfterReconnect = assertRefreshedDiagramThat(newDiagram -> {
            var diagramNavigator = new DiagramNavigator(newDiagram);
            var sourceNodeId = diagramNavigator.edgeWithLabel(LabelConstants.OPEN_QUOTE + LabelConstants.SATISFY + LabelConstants.CLOSE_QUOTE).sourceNode().getNode().getId();
            assertEquals(GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_USAGE_ID, sourceNodeId);
            var targetNodeId = diagramNavigator.edgeWithLabel(LabelConstants.OPEN_QUOTE + LabelConstants.SATISFY + LabelConstants.CLOSE_QUOTE).targetNode().getNode().getId();
            assertEquals(GeneralViewWithTopNodesTestProjectData.GraphicalIds.REQUIREMENT_USAGE_ID, targetNodeId);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createEdgeRunnable)
                .consumeNextWith(diagramContentConsumerAfterNewEdge)
                .then(reconnectEdgeRunnable)
                .consumeNextWith(diagramContentConsumerAfterReconnect)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a SatisfyRequirement edge, WHEN reconnecting the source to a new invalid source, THEN the edge is not connected to the new source")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void reconnectSatisfyEdgeToWrongSource() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var edgeCreationToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(
                this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()),
                "New Satisfy Requirement");

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        AtomicReference<String> satisfyEdgeId = new AtomicReference<>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable createEdgeRunnable = () -> this.edgeCreationTester.createEdgeUsingNodeId(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.REQUIREMENT_USAGE_ID,
                edgeCreationToolId);

        Consumer<Object> diagramContentConsumerAfterNewEdge = assertRefreshedDiagramThat(newDiagram -> {
            var newEdges = this.diagramComparator.newEdges(diagram.get(), newDiagram);
            var newVisibleEdges = newEdges.stream().filter(edge -> edge.getState() != ViewModifier.Hidden).toList();

            assertThat(newVisibleEdges).hasSize(1).first(EDGE)
                    .hasSourceId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID)
                    .hasTargetId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.REQUIREMENT_USAGE_ID)
                    .extracting(Edge::getCenterLabel)
                    .extracting(Label::text)
                    .hasToString(LabelConstants.OPEN_QUOTE + LabelConstants.SATISFY + LabelConstants.CLOSE_QUOTE);

            assertThat(newVisibleEdges).hasSize(1).first(EDGE)
                    .extracting(Edge::getStyle, EDGE_STYLE)
                    .hasSourceArrow(ArrowStyle.None)
                    .hasTargetArrow(ArrowStyle.InputArrow);

            satisfyEdgeId.set(newVisibleEdges.get(0).getId());
        });

        Runnable reconnectEdgeRunnable = () -> this.edgeReconnectionTester.reconnectEdge(
                GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                satisfyEdgeId.get(),
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_DEFINITION_ID,
                ReconnectEdgeKind.SOURCE);

        Consumer<Object> diagramContentConsumerAfterReconnect = assertRefreshedDiagramThat(newDiagram -> {
            var diagramNavigator = new DiagramNavigator(newDiagram);
            var sourceNodeId = diagramNavigator.edgeWithLabel(LabelConstants.OPEN_QUOTE + LabelConstants.SATISFY + LabelConstants.CLOSE_QUOTE).sourceNode().getNode().getId();
            assertEquals(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID, sourceNodeId);
            var targetNodeId = diagramNavigator.edgeWithLabel(LabelConstants.OPEN_QUOTE + LabelConstants.SATISFY + LabelConstants.CLOSE_QUOTE).targetNode().getNode().getId();
            assertEquals(GeneralViewWithTopNodesTestProjectData.GraphicalIds.REQUIREMENT_USAGE_ID, targetNodeId);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createEdgeRunnable)
                .consumeNextWith(diagramContentConsumerAfterNewEdge)
                .then(reconnectEdgeRunnable)
                .consumeNextWith(diagramContentConsumerAfterReconnect)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a SatisfyRequirement edge, WHEN reconnecting the target to a new valid target, THEN the edge is connected to the new target")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void reconnectSatisfyEdgeTarget() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var edgeCreationToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(
                this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()),
                "New Satisfy Requirement");

        var newRequirementUsageToolId = diagramDescriptionIdProvider.getDiagramCreationToolId(this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getRequirementUsage()));

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        AtomicReference<String> satisfyEdgeId = new AtomicReference<>();
        AtomicReference<String> secondRequirementId = new AtomicReference<>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable createEdgeRunnable = () -> this.edgeCreationTester.createEdgeUsingNodeId(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.REQUIREMENT_USAGE_ID,
                edgeCreationToolId);

        Consumer<Object> diagramContentConsumerAfterNewEdge = assertRefreshedDiagramThat(newDiagram -> {
            var newEdges = this.diagramComparator.newEdges(diagram.get(), newDiagram);
            var newVisibleEdges = newEdges.stream().filter(edge -> edge.getState() != ViewModifier.Hidden).toList();

            assertThat(newVisibleEdges).hasSize(1).first(EDGE)
                    .hasSourceId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID)
                    .hasTargetId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.REQUIREMENT_USAGE_ID)
                    .extracting(Edge::getCenterLabel)
                    .extracting(Label::text)
                    .hasToString(LabelConstants.OPEN_QUOTE + LabelConstants.SATISFY + LabelConstants.CLOSE_QUOTE);

            assertThat(newVisibleEdges).hasSize(1).first(EDGE)
                    .extracting(Edge::getStyle, EDGE_STYLE)
                    .hasSourceArrow(ArrowStyle.None)
                    .hasTargetArrow(ArrowStyle.InputArrow);

            satisfyEdgeId.set(newVisibleEdges.get(0).getId());
        });

        Runnable createSecondRequirement = () -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagram, newRequirementUsageToolId);

        Consumer<Object> diagramContentConsumerAfterNewReq = assertRefreshedDiagramThat(newDiagram -> {
            var newNodes = this.diagramComparator.newNodes(diagram.get(), newDiagram);
            var newVisibleNodes = newNodes.stream().filter(edge -> edge.getState() != ViewModifier.Hidden).toList();
            secondRequirementId.set(newVisibleNodes.get(0).getId());
        });

        Runnable reconnectEdgeRunnable = () -> this.edgeReconnectionTester.reconnectEdge(
                GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                satisfyEdgeId.get(),
                secondRequirementId.get(),
                ReconnectEdgeKind.TARGET);

        Consumer<Object> diagramContentConsumerAfterReconnect = assertRefreshedDiagramThat(newDiagram -> {
            var diagramNavigator = new DiagramNavigator(newDiagram);
            var sourceNodeId = diagramNavigator.edgeWithLabel(LabelConstants.OPEN_QUOTE + LabelConstants.SATISFY + LabelConstants.CLOSE_QUOTE).sourceNode().getNode().getId();
            assertEquals(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID, sourceNodeId);
            var targetNodeId = diagramNavigator.edgeWithLabel(LabelConstants.OPEN_QUOTE + LabelConstants.SATISFY + LabelConstants.CLOSE_QUOTE).targetNode().getNode().getId();
            assertEquals(secondRequirementId.get(), targetNodeId);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createEdgeRunnable)
                .consumeNextWith(diagramContentConsumerAfterNewEdge)
                .then(createSecondRequirement)
                .consumeNextWith(diagramContentConsumerAfterNewReq)
                .then(reconnectEdgeRunnable)
                .consumeNextWith(diagramContentConsumerAfterReconnect)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a SatisfyRequirement edge, WHEN reconnecting the target to a new invalid target, THEN the edge is not connected to the new target")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void reconnectSatisfyEdgeWrongTarget() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var edgeCreationToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(
                this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()),
                "New Satisfy Requirement");

        var newRequirementUsageToolId = diagramDescriptionIdProvider.getDiagramCreationToolId(this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getRequirementUsage()));

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        AtomicReference<String> satisfyEdgeId = new AtomicReference<>();
        AtomicReference<String> secondRequirementId = new AtomicReference<>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable createEdgeRunnable = () -> this.edgeCreationTester.createEdgeUsingNodeId(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.REQUIREMENT_USAGE_ID,
                edgeCreationToolId);

        Consumer<Object> diagramContentConsumerAfterNewEdge = assertRefreshedDiagramThat(newDiagram -> {
            var newEdges = this.diagramComparator.newEdges(diagram.get(), newDiagram);
            var newVisibleEdges = newEdges.stream().filter(edge -> edge.getState() != ViewModifier.Hidden).toList();

            assertThat(newVisibleEdges).hasSize(1).first(EDGE)
                    .hasSourceId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID)
                    .hasTargetId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.REQUIREMENT_USAGE_ID)
                    .extracting(Edge::getCenterLabel)
                    .extracting(Label::text)
                    .hasToString(LabelConstants.OPEN_QUOTE + LabelConstants.SATISFY + LabelConstants.CLOSE_QUOTE);

            assertThat(newVisibleEdges).hasSize(1).first(EDGE)
                    .extracting(Edge::getStyle, EDGE_STYLE)
                    .hasSourceArrow(ArrowStyle.None)
                    .hasTargetArrow(ArrowStyle.InputArrow);

            satisfyEdgeId.set(newVisibleEdges.get(0).getId());
        });

        Runnable createSecondRequirement = () -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagram, newRequirementUsageToolId);

        Consumer<Object> diagramContentConsumerAfterNewReq = assertRefreshedDiagramThat(newDiagram -> {
            var newNodes = this.diagramComparator.newNodes(diagram.get(), newDiagram);
            var newVisibleNodes = newNodes.stream().filter(edge -> edge.getState() != ViewModifier.Hidden).toList();
            secondRequirementId.set(newVisibleNodes.get(0).getId());
        });

        Runnable reconnectEdgeRunnable = () -> this.edgeReconnectionTester.reconnectEdge(
                GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                satisfyEdgeId.get(),
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.ITEM_DEFINITION_ID,
                ReconnectEdgeKind.TARGET);

        Consumer<Object> diagramContentConsumerAfterReconnect = assertRefreshedDiagramThat(newDiagram -> {
            var diagramNavigator = new DiagramNavigator(newDiagram);
            var sourceNodeId = diagramNavigator.edgeWithLabel(LabelConstants.OPEN_QUOTE + LabelConstants.SATISFY + LabelConstants.CLOSE_QUOTE).sourceNode().getNode().getId();
            assertEquals(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID, sourceNodeId);
            var targetNodeId = diagramNavigator.edgeWithLabel(LabelConstants.OPEN_QUOTE + LabelConstants.SATISFY + LabelConstants.CLOSE_QUOTE).targetNode().getNode().getId();
            assertEquals(GeneralViewWithTopNodesTestProjectData.GraphicalIds.REQUIREMENT_USAGE_ID, targetNodeId);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createEdgeRunnable)
                .consumeNextWith(diagramContentConsumerAfterNewEdge)
                .then(createSecondRequirement)
                .consumeNextWith(diagramContentConsumerAfterNewReq)
                .then(reconnectEdgeRunnable)
                .consumeNextWith(diagramContentConsumerAfterReconnect)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
