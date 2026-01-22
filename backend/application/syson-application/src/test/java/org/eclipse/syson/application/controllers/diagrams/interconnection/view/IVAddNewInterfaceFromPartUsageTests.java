/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.DiagramCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.IDiagramChecker;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.InterconnectionViewWithTopNodesTestProjectData;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Tests the invocation of the "New Interface" tool from a Part Usage in the Interconnection View diagram.
 *
 * @author Jerome Gout
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IVAddNewInterfaceFromPartUsageTests extends AbstractIntegrationTests {

    private static final int PART_USAGE_COMPARTMENT_COUNT = 14;

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private ToolTester toolTester;

    @Autowired
    private DiagramComparator diagramComparator;

    private final SDVDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                InterconnectionViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                InterconnectionViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        return flux;
    }

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a SysML Project, WHEN New Interface tool of first level element is requested on a PartUsage, THEN a new PartUsage and an Interface edge are created")
    @Sql(scripts = { InterconnectionViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void givenASysMLProjectWhenNewInterfaceToolIsRequestedOnAPartUsageThenANewPartUsageAndAInterfaceEdgeAreCreated() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(InterconnectionViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var diagramCheckerService = new DiagramCheckerService(this.diagramComparator, this.descriptionNameGenerator);

        String creationToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()), "New Interface");
        assertThat(creationToolId).as("The tool 'New Interface' should exist on a PartUsage").isNotNull();

        var diagram = new AtomicReference<Diagram>();

        var diagramId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            diagram.set(diag);
            diagramId.set(diag.getId());
        });

        Runnable newTool = () -> this.toolTester.invokeTool(InterconnectionViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagramId.get(),
                InterconnectionViewWithTopNodesTestProjectData.GraphicalIds.PART_1_ID, creationToolId,
                List.of());

        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    // We should have:
                    // - 1 more node for the new PartUsage, with all its compartments
                    // - 2 more nodes for ports on each part usage
                    // - 1 more node (list item) in the parent's "parts" compartment for the new part
                    // - 1 more edge (the new interface edge)
                    .hasNewNodeCount(1 + PART_USAGE_COMPARTMENT_COUNT + 2 + 1)
                    .hasNewEdgeCount(1)
                    .hasNewBorderNodeCount(2)
                    .check(initialDiagram, newDiagram);
        };

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(newTool)
                .consumeNextWith(diagramCheckerService.checkDiagram(diagramChecker, diagram))
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a SysML Project, WHEN New Interface tool of nested element is requested on a PartUsage, THEN a new PartUsage and an Interface edge are created")
    @Sql(scripts = { InterconnectionViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void givenASysMLProjectWhenNewInterfaceToolOfNestedElementIsRequestedOnAPartUsageThenANewPartUsageAndAnInterfaceEdgeAreCreated() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(InterconnectionViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var diagramCheckerService = new DiagramCheckerService(this.diagramComparator, this.descriptionNameGenerator);

        String newPartToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()), "New Part");
        assertThat(newPartToolId).as("The tool 'New Part' should exist on a first level PartUsage").isNotNull();

        String newInterfaceId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()), "New Interface");
        assertThat(newInterfaceId).as("The tool 'New Interface' should exist on a nested PartUsage").isNotNull();

        var diagram = new AtomicReference<Diagram>();
        var diagramId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            diagram.set(diag);
            diagramId.set(diag.getId());
        });

        Runnable renamedNode = () -> this.toolTester.renameNode(InterconnectionViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagram, "part1", "firstLevelPart");

        var diagramAfterRenaming = new AtomicReference<Diagram>();

        Consumer<Object> diagramAfterRenamingConsumer = assertRefreshedDiagramThat(diag -> {
            diagramAfterRenaming.set(diag);
        });

        Runnable newPart = () -> this.toolTester.invokeTool(InterconnectionViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagramAfterRenaming.get().getId(),
                InterconnectionViewWithTopNodesTestProjectData.GraphicalIds.PART_1_ID, newPartToolId, List.of());

        var diagramAfterNestedPartUsageCreation = new AtomicReference<Diagram>();

        var nestedPartNodeId = new AtomicReference<String>();

        Consumer<Object> diagramAfterNestedPartUsageCreationConsumer = assertRefreshedDiagramThat(diag -> {
            diagramAfterNestedPartUsageCreation.set(diag);
            var nestedPartNode = new DiagramNavigator(diag).nodeWithId(InterconnectionViewWithTopNodesTestProjectData.GraphicalIds.PART_1_ID)
                    .childNodeWithLabel("interconnection")
                    .childNodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "part1")
                    .getNode();
            assertThat(nestedPartNode).isNotNull();
            nestedPartNodeId.set(nestedPartNode.getId());
        });

        Runnable newInterface = () -> this.toolTester.invokeTool(InterconnectionViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagramAfterNestedPartUsageCreation.get().getId(),
                nestedPartNodeId.get(), newInterfaceId, List.of());

        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    // We should have:
                    // - 1 more node for the new PartUsage, with all its compartments
                    // - 2 more nodes for ports on each part usage
                    // - 2 more node (list item) in the parent's "parts" compartment for the new part
                    // - 1 more edge (the new interface edge)
                    .hasNewNodeCount(1 + PART_USAGE_COMPARTMENT_COUNT + 2 + 1)
                    .hasNewEdgeCount(1)
                    .hasNewBorderNodeCount(2)
                    .check(initialDiagram, newDiagram);
        };

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(renamedNode)
                .consumeNextWith(diagramAfterRenamingConsumer)
                .then(newPart)
                .consumeNextWith(diagramAfterNestedPartUsageCreationConsumer)
                .then(newInterface)
                .consumeNextWith(diagramCheckerService.checkDiagram(diagramChecker, diagramAfterNestedPartUsageCreation))
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
