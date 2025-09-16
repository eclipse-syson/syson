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
import org.eclipse.syson.application.controllers.diagrams.checkers.DiagramCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.IDiagramChecker;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeCreationTester;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeReconnectionTester;
import org.eclipse.syson.application.data.IncludeUseCaseUsageProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.IncludeUseCaseUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.helper.LabelConstants;
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
                IncludeUseCaseUsageProjectData.EDITING_CONTEXT_ID,
                IncludeUseCaseUsageProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.diagram = this.givenDiagram.getDiagram(this.verifier);
        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(IncludeUseCaseUsageProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        this.diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);
        this.diagramCheckerService = new DiagramCheckerService(this.diagramComparator, this.descriptionNameGenerator);
        this.semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, IncludeUseCaseUsageProjectData.EDITING_CONTEXT_ID,
                IncludeUseCaseUsageProjectData.SemanticIds.PACKAGE_1_ID);
    }

    @AfterEach
    public void tearDown() {
        if (this.verifier != null) {
            this.verifier.thenCancel()
                    .verify(Duration.ofSeconds(10));
        }
    }

    @DisplayName("GIVEN two UseCaseUsages, WHEN creating an IncludeUseCaseUsage between them, THEN an edge should be displayed to represent that new element as an edge")
    @Sql(scripts = { IncludeUseCaseUsageProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void checkIncludeUsageActionCreation() {
        String creationToolId = this.diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getUseCaseUsage()),
                "New Include Use Case");
        this.verifier.then(() -> this.edgeCreationTester.createEdgeUsingNodeId(IncludeUseCaseUsageProjectData.EDITING_CONTEXT_ID,
                this.diagram,
                IncludeUseCaseUsageProjectData.GraphicalIds.INCLUDING_USE_CASE_ID,
                IncludeUseCaseUsageProjectData.GraphicalIds.INCLUDED_USE_CASE_2_ID,
                creationToolId));

        String[] newInclude = new String[1];
        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
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
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, IncludeUseCaseUsage.class, () -> newInclude[0], includeUsage -> {
            assertThat(this.identityService.getId(includeUsage.getUseCaseIncluded()))
                    .isEqualTo(IncludeUseCaseUsageProjectData.SemanticIds.INCLUDED_USE_CASE_2_ID);
            assertThat(this.identityService.getId(includeUsage.getOwningUsage()))
                    .isEqualTo(IncludeUseCaseUsageProjectData.SemanticIds.INCLUDING_USE_CASE_ID);
        });
    }

    @Test
    @DisplayName("GIVEN an IncludeUseCaseUsage, WHEN reconnecting the target, THEN the new target of the IncludeUseCaseUsage is correct")
    @Sql(scripts = { IncludeUseCaseUsageProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void reconnectIncludeUsaceCaseUsageTarget() {
        this.verifier.then(() -> this.edgeReconnectionTester.reconnectEdge(IncludeUseCaseUsageProjectData.EDITING_CONTEXT_ID,
                this.diagram,
                IncludeUseCaseUsageProjectData.GraphicalIds.INCLUDE_USE_CASE_USAGE_ID,
                IncludeUseCaseUsageProjectData.GraphicalIds.INCLUDED_USE_CASE_2_ID,
                ReconnectEdgeKind.TARGET));

        IDiagramChecker diagramCheckerTarget = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);

            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            assertThat(newEdge).hasSourceId(IncludeUseCaseUsageProjectData.GraphicalIds.INCLUDING_USE_CASE_ID);
            assertThat(newEdge).hasTargetId(IncludeUseCaseUsageProjectData.GraphicalIds.INCLUDED_USE_CASE_2_ID);
        };

        this.diagramCheckerService.checkDiagram(diagramCheckerTarget, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, IncludeUseCaseUsage.class, () -> IncludeUseCaseUsageProjectData.SemanticIds.INCLUDE_USE_CASE_USAGE_ID, includeUsage -> {
            assertThat(this.identityService.getId(includeUsage.getUseCaseIncluded()))
                    .isEqualTo(IncludeUseCaseUsageProjectData.SemanticIds.INCLUDED_USE_CASE_2_ID);
            assertThat(this.identityService.getId(includeUsage.getOwningUsage()))
                    .isEqualTo(IncludeUseCaseUsageProjectData.SemanticIds.INCLUDING_USE_CASE_ID);
        });
    }

    @Test
    @DisplayName("GIVEN an IncludeUseCaseUsage, WHEN reconnecting the source, THEN the new source of the IncludeUseCaseUsage is correct")
    @Sql(scripts = { IncludeUseCaseUsageProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void reconnectIncludeUseCaseUsageSource() {
        this.verifier.then(() -> this.edgeReconnectionTester.reconnectEdge(IncludeUseCaseUsageProjectData.EDITING_CONTEXT_ID,
                this.diagram,
                IncludeUseCaseUsageProjectData.GraphicalIds.INCLUDE_USE_CASE_USAGE_ID,
                IncludeUseCaseUsageProjectData.GraphicalIds.INCLUDED_USE_CASE_2_ID,
                ReconnectEdgeKind.SOURCE));

        IDiagramChecker diagramCheckerTarget = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1) // New element in the Perform action compartment
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);

            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            assertThat(newEdge).hasSourceId(IncludeUseCaseUsageProjectData.GraphicalIds.INCLUDED_USE_CASE_2_ID);
            assertThat(newEdge).hasTargetId(IncludeUseCaseUsageProjectData.GraphicalIds.INCLUDED_USE_CASE_ID);
        };

        this.diagramCheckerService.checkDiagram(diagramCheckerTarget, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, IncludeUseCaseUsage.class, () -> IncludeUseCaseUsageProjectData.SemanticIds.INCLUDE_USE_CASE_USAGE_ID, includeUsage -> {
            assertThat(this.identityService.getId(includeUsage.getUseCaseIncluded()))
                    .isEqualTo(IncludeUseCaseUsageProjectData.SemanticIds.INCLUDED_USE_CASE_ID);
            assertThat(this.identityService.getId(includeUsage.getOwningUsage()))
                    .isEqualTo(IncludeUseCaseUsageProjectData.SemanticIds.INCLUDED_USE_CASE_2_ID);
        });
    }

}
