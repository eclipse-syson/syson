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

import org.eclipse.emf.common.util.EList;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.tests.assertions.DiagramInstanceOfAssertFactories;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.DiagramCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.IDiagramChecker;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
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
import reactor.test.StepVerifier.Step;

/**
 * Tests the creation of ItemUsage parameters on {@link ActionDefinition} and {@link ActionUsage} in the General View
 * Diagram .
 *
 * @author Arthur Daussy
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVEdgeItemUsageTests extends AbstractIntegrationTests {

    private static final String ITEM1 = "item1";

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
    private ToolTester nodeCreationTester;

    @Autowired
    private DiagramComparator diagramComparator;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    private DiagramDescriptionIdProvider diagramDescriptionIdProvider;

    private DiagramCheckerService diagramCheckerService;

    private Step<DiagramRefreshedEventPayload> verifier;

    private SemanticCheckerService semanticCheckerService;

    private AtomicReference<Diagram> diagram;

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.diagram = this.givenDiagram.getDiagram(this.verifier);
        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        this.diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);
        this.diagramCheckerService = new DiagramCheckerService(this.diagramComparator, this.descriptionNameGenerator);
        this.semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID);
    }

    @AfterEach
    public void tearDown() {
        if (this.verifier != null) {
            this.verifier.thenCancel()
                    .verify(Duration.ofSeconds(10));
        }
    }

    @DisplayName("GIVEN a SysML Project with an Action Usage, WHEN creating ItemUsage with direction in, THEN they should be displayed as bordered nodeand list item in the nestedItems compartment")
    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void checkActionUsageInParameter() {
        this.checkItemParameterOnActionUsage("In");

    }

    @DisplayName("GIVEN a SysML Project with an Action Usage, WHEN creating ItemUsage with direction out, THEN they should be displayed as BorderedNode and list item in the nestedItems compartment")
    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void checkActionUsageOutParameter() {
        this.checkItemParameterOnActionUsage("Out");

    }

    @DisplayName("GIVEN a SysML Project with an Action Usage, WHEN creating ItemUsage with direction inout, THEN they should be displayed as bordered node and list item in the nestedItems compartment")
    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void checkActionUsageInOutParameter() {
        this.checkItemParameterOnActionUsage("Inout");
    }

    @DisplayName("GIVEN a SysML Project with an Action Definition, WHEN creating ItemUsage with direction in, THEN they should be displayed as bordered node and list item in the nestedItems compartment")
    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void checkActionDefinitionInParameter() {
        this.checkItemParameterOnActionDefinition("In");
    }

    @DisplayName("GIVEN a SysML Project with an Action Definition, WHEN creating ItemUsage with direction out, THEN they should be displayed as bordered node and list item in the nestedItems compartment")
    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void checkActionDefinitionOutParameter() {
        this.checkItemParameterOnActionDefinition("Out");
    }

    @DisplayName("GIVEN a SysML Project with an Action Definition, WHEN creating ItemUsage with direction Inout, THEN they should be displayed as bordered node and list item in the nestedItems compartment")
    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void checkActionDefinitionInoutParameter() {
        this.checkItemParameterOnActionDefinition("Inout");
    }

    private void checkItemParameterOnActionUsage(String kind) {
        String creationToolId = this.diagramDescriptionIdProvider.getNodeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()), "New Item " + kind);
        this.verifier.then(() -> this.nodeCreationTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, this.diagram, "action", creationToolId));

        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            // One bordered node
            // One border + one root node for Tree Composition (with 4 compartments) + new item in the items
            // compartment
            // One new Composite Edge
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewBorderNodeCount(1)
                    .hasNewNodeCount(7)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);

            List<Node> newNodes = this.diagramComparator.newNodes(initialDiagram, newDiagram);

            // First node is the root element
            Node rootItem = newNodes.get(0);
            // Check for root node node
            assertThat(rootItem)
                    .isNotBorderNode()
                    .hasTargetObjectLabel(ITEM1 + kind)
                    .hasType("node:rectangle")
                    .hasTargetObjectKind("siriusComponents://semantic?domain=sysml&entity=ItemUsage");
            // Skip all containedCompartement
            List<Node> otherNewNodes = newNodes.stream().filter(n -> n != rootItem && !rootItem.getChildNodes().contains(n)).toList();

            // Check for nested item list node
            assertThat(otherNewNodes).hasSize(2)
                    .filteredOn(Node::isBorderNode)
                    .first(DiagramInstanceOfAssertFactories.NODE)
                    .hasType("node:image")
                    .hasTargetObjectLabel(ITEM1 + kind)
                    .hasTargetObjectKind("siriusComponents://semantic?domain=sysml&entity=ItemUsage");
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, ActionUsage.class, () -> GeneralViewWithTopNodesTestProjectData.SemanticIds.ACTION_USAGE_ID, actionUsage -> {
            EList<Feature> parameters = actionUsage.getParameter();
            assertThat(parameters).hasSize(1).allMatch(p -> (ITEM1 + kind).equals(p.getDeclaredName()) && p.getDirection() == FeatureDirectionKind.get(kind.toLowerCase()));
        });
    }

    private void checkItemParameterOnActionDefinition(String kind) {
        String creationToolId = this.diagramDescriptionIdProvider.getNodeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionDefinition()), "New Item " + kind);
        this.verifier.then(() -> this.nodeCreationTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, this.diagram, "ActionDefinition", creationToolId));

        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            // One bordered node
            // One border + one root node for Tree Composition (with 4 compartments) + new item in the items
            // compartment
            // One new Composite Edge
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewBorderNodeCount(1)
                    .hasNewNodeCount(7)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);

            List<Node> newNodes = this.diagramComparator.newNodes(initialDiagram, newDiagram);

            // First node is the root element
            Node rootItem = newNodes.get(0);
            // Check for root node node
            assertThat(rootItem)
                    .isNotBorderNode()
                    .hasTargetObjectLabel(ITEM1)
                    .hasType("node:rectangle")
                    .hasTargetObjectKind("siriusComponents://semantic?domain=sysml&entity=ItemUsage");
            // Skip all containedCompartement
            List<Node> otherNewNodes = newNodes.stream().filter(n -> n != rootItem && !rootItem.getChildNodes().contains(n)).toList();

            // Check for nested item list node
            assertThat(otherNewNodes).hasSize(2)
                    .filteredOn(Node::isBorderNode)
                    .first(DiagramInstanceOfAssertFactories.NODE)
                    .hasType("node:image")
                    .hasTargetObjectLabel(ITEM1)
                    .hasTargetObjectKind("siriusComponents://semantic?domain=sysml&entity=ItemUsage");
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, ActionDefinition.class, () -> GeneralViewWithTopNodesTestProjectData.SemanticIds.ACTION_DEFINITION_ID, actionUsage -> {
            EList<Feature> parameters = actionUsage.getParameter();
            assertThat(parameters).hasSize(1).allMatch(p -> "item1".equals(p.getDeclaredName()) && p.getDirection() == FeatureDirectionKind.get(kind.toLowerCase()));
        });
    }
}
