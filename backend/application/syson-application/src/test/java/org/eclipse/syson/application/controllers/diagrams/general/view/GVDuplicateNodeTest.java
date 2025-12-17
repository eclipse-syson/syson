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

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.OutsideLabel;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.DiagramCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.IDiagramChecker;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.GeneralViewItemAndAttributeProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartUsage;
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
 * Test for duplication of nodes in GV.
 *
 * @author Arthur Daussy
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVDuplicateNodeTest extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramReference givenDiagram;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private DiagramComparator diagramComparator;

    @Autowired
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private ToolTester toolTester;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private IObjectSearchService objectSearchService;

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private StepVerifier.Step<DiagramRefreshedEventPayload> verifier;

    private AtomicReference<Diagram> diagram;

    private DiagramDescriptionIdProvider diagramDescriptionIdProvider;

    private DiagramCheckerService diagramCheckerService;

    private SemanticCheckerService semanticCheckerService;

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.diagram = this.givenDiagram.getDiagram(this.verifier);
        DiagramDescription diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        this.diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);
        this.diagramCheckerService = new DiagramCheckerService(this.diagramComparator, this.descriptionNameGenerator);
        this.semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID,
                GeneralViewItemAndAttributeProjectData.SemanticIds.ROOTPACKAGE_ID);
    }

    @AfterEach
    public void tearDown() {
        if (this.verifier != null) {
            this.verifier.thenCancel()
                    .verify(Duration.ofSeconds(10));
        }
    }

    @DisplayName("GIVEN a General View diagram, WHEN duplicating a Part Usage node with attributes, THEN the semantic element is duplicated with its content and its representation")
    @Test
    @Sql(scripts = { GeneralViewItemAndAttributeProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void checkTopUsageNodeDuplication() {
        var duplicateToolId = this.diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()), "Duplicate Element");
        this.verifier.then(() -> this.toolTester.invokeTool(GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID,
                this.diagram.get().getId(),
                GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_ID,
                duplicateToolId,
                List.of()
        ));

        IDiagramChecker diagramCheck = (initialDiagram, newDiagram) -> {
            assertThat(this.diagramComparator.newNodes(initialDiagram, newDiagram))
                    .as("Contains node named p1-copy")
                    .matches(nodes -> nodes.stream().anyMatch(n -> "p1-copy".equals(n.getTargetObjectLabel())))
                    .as("Contains attribute x1")
                    .matches(nodes -> nodes.stream().anyMatch(n -> "x1".equals(n.getTargetObjectLabel())))
                    .as("Contains attribute x2")
                    .matches(nodes -> nodes.stream().anyMatch(n -> "x2".equals(n.getTargetObjectLabel())))
                    .as("Contains attribute x3")
                    .matches(nodes -> nodes.stream().anyMatch(n -> "x3".equals(n.getTargetObjectLabel())));
            new CheckDiagramElementCount(this.diagramComparator)
                    // we should have 2 more nodes the port container and the port border node
                    .hasNewNodeCount(16) // 1 main node // 12 compartments // 3 attribute // 1 part p1_1
                    .hasNewBorderNodeCount(0)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram);
        };
        this.diagramCheckerService.checkDiagram(diagramCheck, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, Package.class, () -> GeneralViewItemAndAttributeProjectData.SemanticIds.ROOTPACKAGE_ID, rootPackage -> {
            assertThat(rootPackage.getOwnedElement().stream()
                    .filter(PartUsage.class::isInstance)
                    .filter(e -> "p1-copy".equals(e.getName()))
                    .toList()
            ).hasSize(1);
        });
    }

    @DisplayName("GIVEN a General View diagram, WHEN duplicating an ItemUsage bordered node, THEN the semantic element is duplicated with its representation")
    @Test
    @Sql(scripts = { GeneralViewItemAndAttributeProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void checkBorderedNodeUsageNodeDuplication() {
        var duplicateToolId = this.diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getBehavior_Parameter()), "Duplicate Element");
        this.verifier.then(() -> this.toolTester.invokeTool(GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID,
                this.diagram.get().getId(),
                GeneralViewItemAndAttributeProjectData.GraphicalIds.A1_3_BORDERED_NODE_ID,
                duplicateToolId,
                List.of()
        ));

        IDiagramChecker diagramCheck = (initialDiagram, newDiagram) -> {
            Optional<Node> rootNewNode = newDiagram.getNodes().stream()
                    .filter(n -> this.containsLabel(n, "a1_3-copy"))
                    .findFirst();

            assertThat(rootNewNode).isPresent();
            Node a13RootNode = rootNewNode.get();
            assertThat(rootNewNode.get()).as("Newly created root node should be hidden")
                    .matches(node -> node.getState() == ViewModifier.Hidden);

            assertThat(this.diagramComparator.newNodes(initialDiagram, newDiagram).stream().filter(node -> this.containsLabel(node, "a1_3-copy")).toList())
                    .as("Contains 3 nodes named a1_3-copy (A root node, A borderedNode and item compartment")
                    .hasSize(3)
                    .contains(a13RootNode)
                    .anyMatch(node -> node != a13RootNode && node.isBorderNode()) // Check the bordered node
                    .anyMatch(node -> node != a13RootNode && !node.isBorderNode()); // Check the item in the compartment


            new CheckDiagramElementCount(this.diagramComparator)
                    // we should have 2 more nodes the port container and the port border node
                    .hasNewNodeCount(7) // One compartment item node, one bordered node and one root node with 4 compartments
                    .hasNewBorderNodeCount(1)
                    .hasNewEdgeCount(1) // Composition link between root node and container of a1_3
                    .check(initialDiagram, newDiagram);
        };
        this.diagramCheckerService.checkDiagram(diagramCheck, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, ActionUsage.class, () -> GeneralViewItemAndAttributeProjectData.SemanticIds.A1_ID, actionUsage -> {
            assertThat(actionUsage.getOwnedElement().stream()
                    .filter(ItemUsage.class::isInstance)
                    .filter(e -> "a1_3-copy".equals(e.getName()))
                    .toList()
            ).hasSize(1);
        });
    }


    @DisplayName("GIVEN a General View diagram, WHEN duplicating a Item Usage in the 'items' compartment, THEN the semantic element is duplicated with its representation")
    @Test
    @Sql(scripts = { GeneralViewItemAndAttributeProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void checkCompartmentItemUsageNodeDuplication() {
        var duplicateToolId = this.diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getBehavior_Parameter()), "Duplicate Element");
        this.verifier.then(() -> this.toolTester.invokeTool(GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID,
                this.diagram.get().getId(),
                GeneralViewItemAndAttributeProjectData.GraphicalIds.A1_1_BORDERED_NODE_ID,
                duplicateToolId,
                List.of()
        ));

        IDiagramChecker diagramCheck = (initialDiagram, newDiagram) -> {
            Optional<Node> rootNewNode = newDiagram.getNodes().stream()
                    .filter(n -> this.containsLabel(n, "a1_1-copy"))
                    .findFirst();

            /*
             * In app the node is hidden but not in test...
             */
            assertThat(rootNewNode).isPresent();
            Node a21RootNode = rootNewNode.get();
            assertThat(a21RootNode).as("Newly created root node should be hidden")
                    .matches(node -> node.getState() == ViewModifier.Hidden);


            assertThat(this.diagramComparator.newNodes(initialDiagram, newDiagram).stream().filter(node -> this.containsLabel(node, "a1_1-copy")).toList())
                    .as("Contains 3 nodes named a1_1-copy (A root node, A borderedNode and item compartment")
                    .hasSize(3)
                    .contains(a21RootNode)
                    .anyMatch(node -> node != a21RootNode && node.isBorderNode()) // Check the bordered node
                    .anyMatch(node -> node != a21RootNode && !node.isBorderNode()); // Check the item in the compartment


            new CheckDiagramElementCount(this.diagramComparator)
                    // we should have 2 more nodes the port container and the port border node
                    .hasNewNodeCount(7) // One compartment item node, one bordered node and one root node with 4 compartments
                    .hasNewBorderNodeCount(1)
                    .hasNewEdgeCount(1) // Composition link between root node and container of a1_3
                    .check(initialDiagram, newDiagram);
        };
        this.diagramCheckerService.checkDiagram(diagramCheck, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, ActionUsage.class, () -> GeneralViewItemAndAttributeProjectData.SemanticIds.A1_ID, actionUsage -> {
            assertThat(actionUsage.getOwnedElement().stream()
                    .filter(ItemUsage.class::isInstance)
                    .filter(e -> "a1_1-copy".equals(e.getName()))
                    .toList()
            ).hasSize(1);
        });
    }

    private boolean containsLabel(Node node, String label) {
        boolean match = false;
        if (node.getInsideLabel() != null && node.getInsideLabel().getText() != null) {
            match = node.getInsideLabel().getText().contains(label);
        }
        if (!match) {
            match = node.getOutsideLabels().stream().map(OutsideLabel::text).anyMatch(s -> s.contains(label));
        }
        return match;
    }

}
