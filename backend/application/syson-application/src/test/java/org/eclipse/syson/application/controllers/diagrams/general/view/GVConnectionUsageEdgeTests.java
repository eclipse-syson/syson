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
import org.eclipse.sirius.components.diagrams.Label;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.diagrams.events.ReconnectEdgeKind;
import org.eclipse.sirius.components.diagrams.tests.assertions.DiagramAssertions;
import org.eclipse.sirius.components.diagrams.tests.graphql.EditLabelMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.ConnectorCheckerBuilder;
import org.eclipse.syson.application.controllers.diagrams.testers.DirectEditTester;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeCreationTester;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeReconnectionTester;
import org.eclipse.syson.application.data.EdgeConnectionUsageTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.ConnectionUsage;
import org.eclipse.syson.sysml.SysmlPackage;
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
 * Tests the edition of {@link ConnectionUsage} as edges in the general view.
 *
 * @author Arthur Daussy
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVConnectionUsageEdgeTests extends AbstractIntegrationTests {

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

    private DirectEditTester directEditTester;

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private SemanticCheckerService semanticCheckerService;

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(), EdgeConnectionUsageTestProjectData.EDITING_CONTEXT_ID, EdgeConnectionUsageTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        this.semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, EdgeConnectionUsageTestProjectData.EDITING_CONTEXT_ID,
                EdgeConnectionUsageTestProjectData.SemanticIds.PACKAGE1_ID);
        this.directEditTester = new DirectEditTester(this.editLabelMutationRunner, EdgeConnectionUsageTestProjectData.EDITING_CONTEXT_ID);
    }

    @DisplayName("GIVEN two PartUsages in a package, WHEN using an Edge tool to create a ConnectionUsage in between, THEN a ConnectionUsage edge is created")
    @GivenSysONServer({ EdgeConnectionUsageTestProjectData.SCRIPT_PATH })
    @Test
    public void createConnectionUsageOnParts() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(EdgeConnectionUsageTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        Runnable createEdgeRunnable = this.buildCreateEdgeRunnable(
                diagramDescriptionIdProvider,
                diagram,
                this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()),
                EdgeConnectionUsageTestProjectData.GraphicalIds.PART1_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.PART2_ID);

        AtomicReference<String> newEdge = new AtomicReference<>();
        Consumer<Object> newEdgeConsumer = this.assertNewEdgeThat(
                EdgeConnectionUsageTestProjectData.GraphicalIds.PART1_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.PART2_ID,
                "connection1",
                diagram,
                newEdge::set);

        Runnable checker = this.createCheckerBuilder()
                .setExpectedSourceSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.PART1_ID)
                .setExpectedTargetSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.PART2_ID)
                .setExpectedSemanticContainer(EdgeConnectionUsageTestProjectData.SemanticIds.PACKAGE1_ID)
                .setExpectedSourceReference(EdgeConnectionUsageTestProjectData.SemanticIds.PART1_ID)
                .setExpectedTargetReference(EdgeConnectionUsageTestProjectData.SemanticIds.PART2_ID)
                .build(newEdge);

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createEdgeRunnable)
                .consumeNextWith(newEdgeConsumer)
                .then(checker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN two PartUsages contained in a common context, WHEN using an Edge tool to create a ConnectionUsage in between, THEN a ConnectionUsage edge is created")
    @GivenSysONServer({ EdgeConnectionUsageTestProjectData.SCRIPT_PATH })
    @Test
    public void createConnectionUsageOnPartsWithCommonContext() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(EdgeConnectionUsageTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        Runnable createEdgeRunnable = this.buildCreateEdgeRunnable(
                diagramDescriptionIdProvider,
                diagram,
                this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()),
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_PART1_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_PART2_ID);

        AtomicReference<String> newEdge = new AtomicReference<>();
        Consumer<Object> newEdgeConsumer = this.assertNewEdgeThat(
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_PART1_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_PART2_ID,
                "connection2",
                diagram,
                newEdge::set);

        Runnable checker = this.createCheckerBuilder()
                .setExpectedSourceSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART1_ID)
                .setExpectedTargetSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART2_ID)
                .setExpectedSemanticContainer(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ID)
                .setExpectedSourceReference(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART1_ID)
                .setExpectedTargetReference(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART2_ID)
                .build(newEdge);

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createEdgeRunnable)
                .consumeNextWith(newEdgeConsumer)
                .then(checker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }


    @DisplayName("GIVEN two ItemUsage contained in a common context PartUsage, WHEN using an Edge tool to create a ConnectionUsage in between, THEN a ConnectionUsage edge is created with valid feature chain")
    @GivenSysONServer({ EdgeConnectionUsageTestProjectData.SCRIPT_PATH })
    @Test
    public void createConnectionUsageOnItemUsageWithCommonContext() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(EdgeConnectionUsageTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        Runnable createEdgeRunnable = this.buildCreateEdgeRunnable(
                diagramDescriptionIdProvider,
                diagram,
                this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getItemUsage()),
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_ITEM1_IN_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_ITEM2_OUT_ID);

        AtomicReference<String> newEdge = new AtomicReference<>();
        Consumer<Object> newEdgeConsumer = this.assertNewEdgeThat(
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_ITEM1_IN_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_ITEM2_OUT_ID,
                "connection2",
                diagram,
                newEdge::set);

        Runnable checker = this.createCheckerBuilder()
                .setExpectedSourceSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ITEM1IN_ID)
                .setExpectedTargetSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ITEM2_ID)
                .setExpectedSemanticContainer(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ID)
                .setExpectedSourceFeatureChain(List.of(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART1_ID, EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ITEM1IN_ID))
                .setExpectedTargetFeatureChain(List.of(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART2_ID, EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ITEM2_ID))
                .build(newEdge);

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createEdgeRunnable)
                .consumeNextWith(newEdgeConsumer)
                .then(checker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN two PortUsage contained in a common context PartUsage, WHEN using an Edge tool to create a ConnectionUsage in between, THEN a ConnectionUsage edge is created with valid feature chain")
    @GivenSysONServer({ EdgeConnectionUsageTestProjectData.SCRIPT_PATH })
    @Test
    public void createConnectionUsageOnPortUsageUsageWithCommonContext() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(EdgeConnectionUsageTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        Runnable createEdgeRunnable = this.buildCreateEdgeRunnable(
                diagramDescriptionIdProvider,
                diagram,
                this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort()),
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_PORT11_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_PORT22_ID);

        AtomicReference<String> newEdge = new AtomicReference<>();
        Consumer<Object> newEdgeConsumer = this.assertNewEdgeThat(
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_PORT11_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_PORT22_ID,
                "connection2",
                diagram,
                newEdge::set);

        Runnable checker = this.createCheckerBuilder()
                .setExpectedSourceSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.PORT11_ID)
                .setExpectedTargetSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.PORT22_ID)
                .setExpectedSemanticContainer(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ID)
                .setExpectedSourceFeatureChain(List.of(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART1_ID, EdgeConnectionUsageTestProjectData.SemanticIds.PORT11_ID))
                .setExpectedTargetFeatureChain(List.of(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART2_ID, EdgeConnectionUsageTestProjectData.SemanticIds.PORT22_ID))
                .build(newEdge);

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createEdgeRunnable)
                .consumeNextWith(newEdgeConsumer)
                .then(checker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN two inherited PortUsage contained in a common context PartUsage, WHEN using an Edge tool to create a ConnectionUsage in between, THEN a ConnectionUsage edge is created with valid feature chain")
    @GivenSysONServer({ EdgeConnectionUsageTestProjectData.SCRIPT_PATH })
    @Test
    public void createConnectionUsageOnInheritedPortUsageUsageWithCommonContext() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(EdgeConnectionUsageTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        Runnable createEdgeRunnable = this.buildCreateEdgeRunnable(
                diagramDescriptionIdProvider,
                diagram,
                this.descriptionNameGenerator.getInheritedBorderNodeName(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort()),
                EdgeConnectionUsageTestProjectData.GraphicalIds.INHERITED_PD1_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.INHERITED_PD2_ID);

        AtomicReference<String> newEdge = new AtomicReference<>();
        Consumer<Object> newEdgeConsumer = this.assertNewEdgeThat(
                EdgeConnectionUsageTestProjectData.GraphicalIds.INHERITED_PD1_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.INHERITED_PD2_ID,
                "connection2",
                diagram,
                newEdge::set);

        Runnable checker = this.createCheckerBuilder()
                .setExpectedSourceSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.PD1_ID)
                .setExpectedTargetSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.PD2_ID)
                .setExpectedSemanticContainer(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ID)
                .setExpectedSourceFeatureChain(List.of(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART1_ID, EdgeConnectionUsageTestProjectData.SemanticIds.PD1_ID))
                .setExpectedTargetFeatureChain(List.of(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART2_ID, EdgeConnectionUsageTestProjectData.SemanticIds.PD2_ID))
                .build(newEdge);

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createEdgeRunnable)
                .consumeNextWith(newEdgeConsumer)
                .then(checker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a ConnectionUsage, WHEN reconnecting the source to a new source within the same context, THEN a ConnectionUsage updated but the container is not changed")
    @GivenSysONServer({ EdgeConnectionUsageTestProjectData.SCRIPT_PATH })
    @Test
    public void reconnectSourceSameContext() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable reconnectEdgeRunnable = this.buildReconnectRunnable(
                EdgeConnectionUsageTestProjectData.GraphicalIds.CONNECTION_0_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_PART1_ID,
                ReconnectEdgeKind.SOURCE,
                diagram);

        AtomicReference<String> newEdge = new AtomicReference<>();
        Consumer<Object> newEdgeConsumer = this.assertReconnectThat(
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_PART1_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_ITEM2_OUT_ID,
                diagram,
                newEdge::set);

        Runnable checker = this.createCheckerBuilder()
                .setExpectedSourceSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART1_ID)
                .setExpectedTargetSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ITEM2_ID)
                .setExpectedSemanticContainer(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ID)
                .setExpectedSourceReference(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART1_ID)
                .setExpectedTargetFeatureChain(List.of(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART2_ID, EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ITEM2_ID))
                .build(newEdge);

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(reconnectEdgeRunnable)
                .consumeNextWith(newEdgeConsumer)
                .then(checker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a ConnectionUsage, WHEN reconnecting the source to a new source within a new context, THEN a ConnectionUsage updated and the container is changed")
    @GivenSysONServer({ EdgeConnectionUsageTestProjectData.SCRIPT_PATH })
    @Test
    public void reconnectSourceChangeContext() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable reconnectEdgeRunnable = this.buildReconnectRunnable(
                EdgeConnectionUsageTestProjectData.GraphicalIds.CONNECTION_0_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.PORT1_ID,
                ReconnectEdgeKind.SOURCE,
                diagram);

        AtomicReference<String> newEdge = new AtomicReference<>();
        Consumer<Object> newEdgeConsumer = this.assertReconnectThat(
                EdgeConnectionUsageTestProjectData.GraphicalIds.PORT1_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_ITEM2_OUT_ID,
                diagram,
                newEdge::set);

        Runnable checker = this.createCheckerBuilder()
                .setExpectedSourceSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.PORT1_ID)
                .setExpectedTargetSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ITEM2_ID)
                .setExpectedSemanticContainer(EdgeConnectionUsageTestProjectData.SemanticIds.PACKAGE1_ID)
                .setExpectedSourceReference(EdgeConnectionUsageTestProjectData.SemanticIds.PORT1_ID)
                .setExpectedTargetReference(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ITEM2_ID)
                .build(newEdge);

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(reconnectEdgeRunnable)
                .consumeNextWith(newEdgeConsumer)
                .then(checker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a ConnectionUsage, WHEN reconnecting the target to a new target within the same context, THEN a ConnectionUsage is updated but the container is not changed")
    @GivenSysONServer({ EdgeConnectionUsageTestProjectData.SCRIPT_PATH })
    @Test
    public void reconnectTargetSameContext() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable reconnectEdgeRunnable = this.buildReconnectRunnable(
                EdgeConnectionUsageTestProjectData.GraphicalIds.CONNECTION_0_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_PART2_ID,
                ReconnectEdgeKind.TARGET,
                diagram);

        AtomicReference<String> newEdge = new AtomicReference<>();
        Consumer<Object> newEdgeConsumer = this.assertReconnectThat(
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_ITEM1_IN_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_PART2_ID,
                diagram,
                newEdge::set);

        Runnable checker = this.createCheckerBuilder()
                .setExpectedSourceSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ITEM1IN_ID)
                .setExpectedTargetSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART2_ID)
                .setExpectedSemanticContainer(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ID)
                .setExpectedSourceFeatureChain(List.of(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART1_ID, EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ITEM1IN_ID))
                .setExpectedTargetReference(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_PART2_ID)
                .build(newEdge);

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(reconnectEdgeRunnable)
                .consumeNextWith(newEdgeConsumer)
                .then(checker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a ConnectionUsage, WHEN reconnecting the target to a new target in a new context , THEN a ConnectionUsage is updated and the container is changed")
    @GivenSysONServer({ EdgeConnectionUsageTestProjectData.SCRIPT_PATH })
    @Test
    public void reconnectTargetChangeContext() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable reconnectEdgeRunnable = this.buildReconnectRunnable(
                EdgeConnectionUsageTestProjectData.GraphicalIds.CONNECTION_0_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.PORT2_ID,
                ReconnectEdgeKind.TARGET,
                diagram);

        AtomicReference<String> newEdge = new AtomicReference<>();
        Consumer<Object> newEdgeConsumer = this.assertReconnectThat(
                EdgeConnectionUsageTestProjectData.GraphicalIds.SYSTEM_ITEM1_IN_ID,
                EdgeConnectionUsageTestProjectData.GraphicalIds.PORT2_ID,
                diagram,
                newEdge::set);

        Runnable checker = this.createCheckerBuilder()
                .setExpectedSourceSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ITEM1IN_ID)
                .setExpectedTargetSemanticId(EdgeConnectionUsageTestProjectData.SemanticIds.PORT2_ID)
                .setExpectedSemanticContainer(EdgeConnectionUsageTestProjectData.SemanticIds.PACKAGE1_ID)
                .setExpectedSourceReference(EdgeConnectionUsageTestProjectData.SemanticIds.SYSTEM_ITEM1IN_ID)
                .setExpectedTargetReference(EdgeConnectionUsageTestProjectData.SemanticIds.PORT2_ID)
                .build(newEdge);

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(reconnectEdgeRunnable)
                .consumeNextWith(newEdgeConsumer)
                .then(checker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a ConnectionUsage, WHEN direct editing its centered label, THEN type of the usage is set")
    @GivenSysONServer({ EdgeConnectionUsageTestProjectData.SCRIPT_PATH })
    @Test
    public void directEditFeatureTyping() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable requestDirectEdit = this.directEditTester.directEditCenteredEdgeLabel(diagram,
                EdgeConnectionUsageTestProjectData.GraphicalIds.CONNECTION_0_ID,
                "connection0 : ConnectionDefinition1");

        Consumer<Object> diagramConsumer = assertRefreshedDiagramThat(newDiagram -> {
            DiagramNavigator diagramNavigator = new DiagramNavigator(newDiagram);

            Label newLabel = diagramNavigator.edgeWithId(EdgeConnectionUsageTestProjectData.GraphicalIds.CONNECTION_0_ID).getEdge().getCenterLabel();
            assertThat(newLabel.text()).isEqualTo("connection0 : ConnectionDefinition1");
        });

        Runnable runnableChecker = this.semanticCheckerService.checkElement(ConnectionUsage.class, () -> EdgeConnectionUsageTestProjectData.SemanticIds.CONNECTION_0_ID, connectionUsage -> {
            assertThat(connectionUsage.getType()).hasSize(2)
                    .allMatch(type -> "ConnectionDefinition1".equals(type.getName()) || "BinaryLinkObject".equals(type.getName()));
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(requestDirectEdit)
                .consumeNextWith(diagramConsumer)
                .then(runnableChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    private Runnable buildCreateEdgeRunnable(DiagramDescriptionIdProvider diagramDescriptionIdProvider, AtomicReference<Diagram> diagram, String sourceNodeDescriptionName, String sourceNodeId, String targetNodeId) {
        String creationToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(
                sourceNodeDescriptionName,
                "New Connection");
        return () -> this.edgeCreationTester.createEdgeUsingNodeId(EdgeConnectionUsageTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                sourceNodeId,
                targetNodeId,
                creationToolId);
    }

    private Consumer<Object> assertNewEdgeThat(String expectedSourceGraphicalId, String expectedTargetGraphicalId, String expectedLabel, AtomicReference<Diagram> diagram, Consumer<String> newEdgeConsumer) {
        return assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(0)
                    .hasNewEdgeCount(1)
                    .check(diagram.get(), newDiagram, true);
            List<Edge> newEdges = this.diagramComparator.newEdges(diagram.get(), newDiagram);
            List<Edge> newVisibleEdges = newEdges.stream().filter(edge -> edge.getState() != ViewModifier.Hidden).toList();
            assertThat(newVisibleEdges).hasSize(1).first(EDGE)
                    .hasSourceId(expectedSourceGraphicalId)
                    .hasTargetId(expectedTargetGraphicalId)
                    .extracting(Edge::getStyle, EDGE_STYLE)
                    .hasSourceArrow(ArrowStyle.None)
                    .hasTargetArrow(ArrowStyle.None);
            assertThat(newVisibleEdges).hasSize(1).first(EDGE)
                    .extracting(e -> e.getCenterLabel().text()).isEqualTo(expectedLabel);
            newEdgeConsumer.accept(newEdges.get(0).getTargetObjectId());
        });
    }

    private ConnectorCheckerBuilder<ConnectionUsage> createCheckerBuilder() {
        return new ConnectorCheckerBuilder<>(this.identityService, ConnectionUsage.class, this.semanticCheckerService);
    }

    private Runnable buildReconnectRunnable(String edgeId, String newTarget, ReconnectEdgeKind reconnectionKind, AtomicReference<Diagram> diagram) {
        return () -> this.edgeReconnectionTester.reconnectEdge(EdgeConnectionUsageTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                edgeId,
                newTarget,
                reconnectionKind);
    }

    private Consumer<Object> assertReconnectThat(String expectedSourceGrapicalId, String expectedTargetGraphicalId, AtomicReference<Diagram> diagram, Consumer<String> newEdgeConsumer) {
        return assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(diagram.get(), newDiagram, true);

            Edge newEdge = this.diagramComparator.newEdges(diagram.get(), newDiagram).get(0);
            newEdgeConsumer.accept(newEdge.getTargetObjectId());
            DiagramAssertions.assertThat(newEdge)
                    .hasSourceId(expectedSourceGrapicalId)
                    .hasTargetId(expectedTargetGraphicalId)
                    .extracting(Edge::getStyle, EDGE_STYLE)
                    .hasSourceArrow(ArrowStyle.None)
                    .hasTargetArrow(ArrowStyle.None);
        });
    }
}
