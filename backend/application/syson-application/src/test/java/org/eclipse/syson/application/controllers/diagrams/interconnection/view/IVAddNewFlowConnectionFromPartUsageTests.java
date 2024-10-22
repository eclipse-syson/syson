/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.DiagramCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.IDiagramChecker;
import org.eclipse.syson.application.controllers.diagrams.testers.NodeCreationTester;
import org.eclipse.syson.application.data.SysMLv2Identifiers;
import org.eclipse.syson.diagram.interconnection.view.IVDescriptionNameGenerator;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.sysml.SysmlPackage;
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
 * Tests the invocation of the "new Flow Connection" tool from a Part Usage in the Interconnection View diagram.
 *
 * @author Jerome Gout
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IVAddNewFlowConnectionFromPartUsageTests extends AbstractIntegrationTests {

    private static final int PART_USAGE_COMPARTMENT_COUNT = 3;

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
    private NodeCreationTester nodeCreationTester;

    @Autowired
    private DiagramComparator diagramComparator;

    private DiagramDescriptionIdProvider diagramDescriptionIdProvider;

    private DiagramCheckerService diagramCheckerService;

    private Step<DiagramRefreshedEventPayload> verifier;

    private AtomicReference<Diagram> diagram;

    private final IVDescriptionNameGenerator descriptionNameGenerator = new IVDescriptionNameGenerator();

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                SysMLv2Identifiers.INTERCONNECTION_VIEW_WITH_TOP_NODES_PROJECT,
                SysMLv2Identifiers.INTERCONNECTION_VIEW_WITH_TOP_NODES_DIAGRAM);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.diagram = this.givenDiagram.getDiagram(this.verifier);
        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(SysMLv2Identifiers.INTERCONNECTION_VIEW_WITH_TOP_NODES_PROJECT,
                SysMLv2Identifiers.INTERCONNECTION_VIEW_DIAGRAM_DESCRIPTION_ID);
        this.diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);
        this.diagramCheckerService = new DiagramCheckerService(this.diagramComparator, this.descriptionNameGenerator);
    }

    @AfterEach
    public void tearDown() {
        if (this.verifier != null) {
            this.verifier.thenCancel()
                    .verify(Duration.ofSeconds(10));
        }
    }

    @DisplayName("Given a SysML Project, when New Flow Connection tool of first level element is requested on a PartUsage, then a new PartUsage and a Flow Connection edge are created")
    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void givenASysMLProjectWhenNewFlowConnectionToolOfFirstLevelElementIsRequestedOnAPartUsageThenANewPartUsageAndAFlowConnectionEdgeAreCreated() {
        String creationToolId = this.diagramDescriptionIdProvider.getNodeCreationToolId(this.descriptionNameGenerator.getFirstLevelNodeName(SysmlPackage.eINSTANCE.getPartUsage()), "New Flow Connection");
        assertThat(creationToolId).as("The tool 'New Flow Connection' should exist on a PartUsage").isNotNull();
        this.verifier.then(() -> this.nodeCreationTester.createNode(SysMLv2Identifiers.INTERCONNECTION_VIEW_WITH_TOP_NODES_PROJECT,
                this.diagram,
                "part1",
                creationToolId));

        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    // we should have 1 more node for the new PartUsage, 3 more nodes for its compartments and
                    // 2 more nodes for ports on each part usage.
                    // we should have 1 more edge (the new flow connection edge)
                    .hasNewNodeCount(1 + PART_USAGE_COMPARTMENT_COUNT + 2)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);
    }

    @DisplayName("Given a SysML Project, when New Flow Connection tool of nested element is requested on a PartUsage, then a new PartUsage and a Flow Connection edge are created")
    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void givenASysMLProjectWhenNewFlowConnectionToolOfNestedElementIsRequestedOnAPartUsageThenANewPartUsageAndAFlowConnectionEdgeAreCreated() {
        String creationPartToolId = this.diagramDescriptionIdProvider.getNodeCreationToolId(this.descriptionNameGenerator.getFirstLevelNodeName(SysmlPackage.eINSTANCE.getPartUsage()), "New Part");
        assertThat(creationPartToolId).as("The tool 'New Part' should exist on a first level PartUsage").isNotNull();
        this.verifier.then(() -> this.nodeCreationTester.createNode(SysMLv2Identifiers.INTERCONNECTION_VIEW_WITH_TOP_NODES_PROJECT,
                this.diagram,
                "part1",
                creationPartToolId));

        var diagramAfterNestedPartUsageCreation = this.givenDiagram.getDiagram(this.verifier);

        String creationToolId = this.diagramDescriptionIdProvider.getNodeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()), "New Flow Connection");
        assertThat(creationToolId).as("The tool 'New Flow Connection' should exist on a nested PartUsage").isNotNull();
        this.verifier.then(() -> this.nodeCreationTester.createNode(SysMLv2Identifiers.INTERCONNECTION_VIEW_WITH_TOP_NODES_PROJECT,
                diagramAfterNestedPartUsageCreation,
                "part",
                creationToolId));

        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    // we should have 1 more node for the new PartUsage, 3 more nodes for its compartments and
                    // 2 more nodes for ports on each part usage.
                    // we should have 1 more edge (the new flow connection edge)
                    .hasNewNodeCount(1 + PART_USAGE_COMPARTMENT_COUNT + 2)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, diagramAfterNestedPartUsageCreation, this.verifier);
    }
}
