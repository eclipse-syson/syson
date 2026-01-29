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
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramAssertions.assertThat;

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
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.SysONTestsProperties;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeCreationTester;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeReconnectionTester;
import org.eclipse.syson.application.data.IncludeUseCaseUsageProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.IncludeUseCaseUsage;
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
 * Tests on {@link IncludeUseCaseUsage} on the General View Diagram .
 *
 * @author Arthur Daussy
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = { SysONTestsProperties.NO_DEFAULT_LIBRARIES_PROPERTY })
public class GVIncludeUseCaseUsageTests extends AbstractIntegrationTests {

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

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

    private SemanticCheckerService semanticCheckerService;

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(), IncludeUseCaseUsageProjectData.EDITING_CONTEXT_ID, IncludeUseCaseUsageProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        this.semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, IncludeUseCaseUsageProjectData.EDITING_CONTEXT_ID,
                IncludeUseCaseUsageProjectData.SemanticIds.PACKAGE_1_ID);
    }

    @DisplayName("GIVEN two UseCaseUsages, WHEN creating an IncludeUseCaseUsage between them, THEN an edge should be displayed to represent that new element as an edge")
    @GivenSysONServer({ IncludeUseCaseUsageProjectData.SCRIPT_PATH })
    @Test
    public void checkIncludeUsageActionCreation() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(IncludeUseCaseUsageProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        String creationToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getUseCaseUsage()),
                "New Include Use Case");
        Runnable creationToolRunnable = () -> this.edgeCreationTester.createEdgeUsingNodeId(IncludeUseCaseUsageProjectData.EDITING_CONTEXT_ID,
                diagram,
                IncludeUseCaseUsageProjectData.GraphicalIds.INCLUDING_USE_CASE_ID,
                IncludeUseCaseUsageProjectData.GraphicalIds.INCLUDED_USE_CASE_2_ID,
                creationToolId);

        String[] newInclude = new String[1];
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1) // New element in the Perform action compartment
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);
            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            newInclude[0] = newEdge.getTargetObjectId();
            assertThat(newEdge).hasSourceId(IncludeUseCaseUsageProjectData.GraphicalIds.INCLUDING_USE_CASE_ID);
            assertThat(newEdge).hasTargetId(IncludeUseCaseUsageProjectData.GraphicalIds.INCLUDED_USE_CASE_2_ID);
            assertThat(newEdge.getStyle()).hasTargetArrow(ArrowStyle.InputFillClosedArrow);
            assertThat(newEdge.getCenterLabel().text()).isEqualTo(LabelConstants.OPEN_QUOTE + "include" + LabelConstants.CLOSE_QUOTE);
        });

        Runnable semanticCheck = this.semanticCheckerService.checkElement(IncludeUseCaseUsage.class, () -> newInclude[0], includeUsage -> {
            assertThat(this.identityService.getId(includeUsage.getUseCaseIncluded()))
                    .isEqualTo(IncludeUseCaseUsageProjectData.SemanticIds.INCLUDED_USE_CASE_2_ID);
            assertThat(this.identityService.getId(includeUsage.getOwningUsage()))
                    .isEqualTo(IncludeUseCaseUsageProjectData.SemanticIds.INCLUDING_USE_CASE_ID);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(creationToolRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("GIVEN an IncludeUseCaseUsage, WHEN reconnecting the target, THEN the new target of the IncludeUseCaseUsage is correct")
    @GivenSysONServer({ IncludeUseCaseUsageProjectData.SCRIPT_PATH })

    public void reconnectIncludeUsaceCaseUsageTarget() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable reconnectEdgeRunnable = () -> this.edgeReconnectionTester.reconnectEdge(IncludeUseCaseUsageProjectData.EDITING_CONTEXT_ID,
                diagram,
                IncludeUseCaseUsageProjectData.GraphicalIds.INCLUDE_USE_CASE_USAGE_ID,
                IncludeUseCaseUsageProjectData.GraphicalIds.INCLUDED_USE_CASE_2_ID,
                ReconnectEdgeKind.TARGET);

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);

            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            assertThat(newEdge).hasSourceId(IncludeUseCaseUsageProjectData.GraphicalIds.INCLUDING_USE_CASE_ID);
            assertThat(newEdge).hasTargetId(IncludeUseCaseUsageProjectData.GraphicalIds.INCLUDED_USE_CASE_2_ID);
        });

        Runnable semanticCheck = this.semanticCheckerService.checkElement(IncludeUseCaseUsage.class, () -> IncludeUseCaseUsageProjectData.SemanticIds.INCLUDE_USE_CASE_USAGE_ID, includeUsage -> {
            assertThat(this.identityService.getId(includeUsage.getUseCaseIncluded()))
                    .isEqualTo(IncludeUseCaseUsageProjectData.SemanticIds.INCLUDED_USE_CASE_2_ID);
            assertThat(this.identityService.getId(includeUsage.getOwningUsage()))
                    .isEqualTo(IncludeUseCaseUsageProjectData.SemanticIds.INCLUDING_USE_CASE_ID);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(reconnectEdgeRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("GIVEN an IncludeUseCaseUsage, WHEN reconnecting the source, THEN the new source of the IncludeUseCaseUsage is correct")
    @GivenSysONServer({ IncludeUseCaseUsageProjectData.SCRIPT_PATH })
    public void reconnectIncludeUseCaseUsageSource() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable reconnectEdgeRunnable = () -> this.edgeReconnectionTester.reconnectEdge(IncludeUseCaseUsageProjectData.EDITING_CONTEXT_ID,
                diagram,
                IncludeUseCaseUsageProjectData.GraphicalIds.INCLUDE_USE_CASE_USAGE_ID,
                IncludeUseCaseUsageProjectData.GraphicalIds.INCLUDED_USE_CASE_2_ID,
                ReconnectEdgeKind.SOURCE);

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1) // New element in the Perform action compartment
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);

            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            assertThat(newEdge).hasSourceId(IncludeUseCaseUsageProjectData.GraphicalIds.INCLUDED_USE_CASE_2_ID);
            assertThat(newEdge).hasTargetId(IncludeUseCaseUsageProjectData.GraphicalIds.INCLUDED_USE_CASE_ID);
        });

        Runnable semanticCheck = this.semanticCheckerService.checkElement(IncludeUseCaseUsage.class, () -> IncludeUseCaseUsageProjectData.SemanticIds.INCLUDE_USE_CASE_USAGE_ID, includeUsage -> {
            assertThat(this.identityService.getId(includeUsage.getUseCaseIncluded()))
                    .isEqualTo(IncludeUseCaseUsageProjectData.SemanticIds.INCLUDED_USE_CASE_ID);
            assertThat(this.identityService.getId(includeUsage.getOwningUsage()))
                    .isEqualTo(IncludeUseCaseUsageProjectData.SemanticIds.INCLUDED_USE_CASE_2_ID);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(reconnectEdgeRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

}
