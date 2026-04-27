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

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.OutsideLabel;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.view.diagram.NodeTool;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controller.editingcontext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.GeneralViewItemAndAttributeProjectData;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.StateDefinition;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Test for duplication of nodes in GV.
 *
 * @author Arthur Daussy
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVDuplicateNodeTest extends AbstractIntegrationTests {

    private static final String A1_1_COPY = "a1_1-copy";

    private static final String A1_3_COPY = "a1_3-copy";

    private static final String ACTION_COPY = "action-copy";

    private static final String PART_COPY = "part-copy";

    private static final String PART_1_COPY = "part1-copy";

    private static final String STATE_DEFINITION_COPY = "StateDefinition-copy";

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

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

    private SemanticCheckerService semanticCheckerService;

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(), GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID, GeneralViewItemAndAttributeProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram(String editingContextId, String diagramId) {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(), editingContextId, diagramId);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        this.semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID,
                GeneralViewItemAndAttributeProjectData.SemanticIds.ROOTPACKAGE_ID);
    }

    @DisplayName("GIVEN a General View diagram, WHEN duplicating a Part Usage node with attributes, THEN the semantic element is duplicated with its content and its representation")
    @GivenSysONServer({ GeneralViewItemAndAttributeProjectData.SCRIPT_PATH })
    @Test
    public void checkTopUsageNodeDuplication() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);


        var duplicateToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()), "Duplicate Element");
        Runnable duplicateToolRunnable = () -> this.toolTester.invokeTool(GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID,
                diagram.get().getId(),
                GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_ID,
                duplicateToolId,
                List.of()
        );

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
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
        });

        Runnable semanticCheck = this.semanticCheckerService.checkElement(Package.class, () -> GeneralViewItemAndAttributeProjectData.SemanticIds.ROOTPACKAGE_ID, rootPackage -> {
            assertThat(rootPackage.getOwnedElement().stream()
                    .filter(PartUsage.class::isInstance)
                    .filter(e -> "p1-copy".equals(e.getName()))
                    .toList()
            ).hasSize(1);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(duplicateToolRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a General View diagram, WHEN inspecting duplicate tools, THEN they provide the Ctrl+D keybinding")
    @GivenSysONServer({ GeneralViewItemAndAttributeProjectData.SCRIPT_PATH })
    @Test
    public void checkDuplicateToolsKeyBinding() {
        TestTransaction.flagForCommit();
        TestTransaction.end();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);

        var duplicateElementTool = EMFUtils.allContainedObjectOfType(diagramDescription, NodeTool.class)
                .filter(nodeTool -> "Duplicate Element".equals(nodeTool.getName()))
                .findFirst();
        assertThat(duplicateElementTool).isPresent();
        this.assertCtrlDKeyBinding(duplicateElementTool.get());

        var duplicateElementsGroupTool = EMFUtils.allContainedObjectOfType(diagramDescription.getGroupPalette(), NodeTool.class)
                .filter(nodeTool -> "Duplicate Elements".equals(nodeTool.getName()))
                .findFirst();
        assertThat(duplicateElementsGroupTool).isPresent();
        this.assertCtrlDKeyBinding(duplicateElementsGroupTool.get());
    }

    @DisplayName("GIVEN a General View diagram, WHEN duplicating an ItemUsage bordered node, THEN the semantic element is duplicated with its representation")
    @GivenSysONServer({ GeneralViewItemAndAttributeProjectData.SCRIPT_PATH })
    @Test
    public void checkBorderedNodeUsageNodeDuplication() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var duplicateToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getBehavior_Parameter()), "Duplicate Element");
        Runnable duplicateToolRunnable = () -> this.toolTester.invokeTool(GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID,
                diagram.get().getId(),
                GeneralViewItemAndAttributeProjectData.GraphicalIds.A1_3_BORDERED_NODE_ID,
                duplicateToolId,
                List.of()
        );

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            Optional<Node> rootNewNode = newDiagram.getNodes().stream()
                    .filter(n -> this.containsLabel(n, A1_3_COPY))
                    .findFirst();

            assertThat(rootNewNode).isPresent();
            Node a13RootNode = rootNewNode.get();
            assertThat(rootNewNode.get()).as("Newly created root node should be hidden")
                    .matches(node -> node.getState() == ViewModifier.Hidden);

            assertThat(this.diagramComparator.newNodes(initialDiagram, newDiagram).stream().filter(node -> this.containsLabel(node, A1_3_COPY)).toList())
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
        });

        Runnable semanticCheck = this.semanticCheckerService.checkElement(ActionUsage.class, () -> GeneralViewItemAndAttributeProjectData.SemanticIds.A1_ID, actionUsage -> {
            assertThat(actionUsage.getOwnedElement().stream()
                    .filter(ItemUsage.class::isInstance)
                    .filter(e -> A1_3_COPY.equals(e.getName()))
                    .toList()
            ).hasSize(1);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(duplicateToolRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }


    @DisplayName("GIVEN a General View diagram, WHEN duplicating a Item Usage in the 'items' compartment, THEN the semantic element is duplicated with its representation")
    @Test
    @GivenSysONServer({ GeneralViewItemAndAttributeProjectData.SCRIPT_PATH })
    public void checkCompartmentItemUsageNodeDuplication() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var duplicateToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getBorderNodeName(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getBehavior_Parameter()), "Duplicate Element");
        Runnable duplicateToolRunnable = () -> this.toolTester.invokeTool(GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID,
                diagram.get().getId(),
                GeneralViewItemAndAttributeProjectData.GraphicalIds.A1_1_BORDERED_NODE_ID,
                duplicateToolId,
                List.of()
        );

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            Optional<Node> rootNewNode = newDiagram.getNodes().stream()
                    .filter(n -> this.containsLabel(n, A1_1_COPY))
                    .findFirst();

            /*
             * In app the node is hidden but not in test...
             */
            assertThat(rootNewNode).isPresent();
            Node a21RootNode = rootNewNode.get();
            assertThat(a21RootNode).as("Newly created root node should be hidden")
                    .matches(node -> node.getState() == ViewModifier.Hidden);


            assertThat(this.diagramComparator.newNodes(initialDiagram, newDiagram).stream().filter(node -> this.containsLabel(node, A1_1_COPY)).toList())
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
        });

        Runnable semanticCheck = this.semanticCheckerService.checkElement(ActionUsage.class, () -> GeneralViewItemAndAttributeProjectData.SemanticIds.A1_ID, actionUsage -> {
            assertThat(actionUsage.getOwnedElement().stream()
                    .filter(ItemUsage.class::isInstance)
                    .filter(e -> A1_1_COPY.equals(e.getName()))
                    .toList()
            ).hasSize(1);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(duplicateToolRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a General View diagram, WHEN duplicating two top container nodes, THEN both semantic elements and representations are duplicated")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void checkMultiSelectionContainerNodeDuplication() {
        var flux = this.givenSubscriptionToDiagram(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        var topNodesSemanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var duplicateToolId = diagramDescriptionIdProvider.getGroupNodeToolId("Duplicate Elements");
        Runnable duplicateToolRunnable = () -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                diagram.get().getId(),
                List.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_USAGE_ID),
                duplicateToolId,
                List.of()
        );

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();

            this.assertDuplicatedContainerNodeRepresentation(initialDiagram, newDiagram, PART_COPY);
            this.assertDuplicatedContainerNodeRepresentation(initialDiagram, newDiagram, ACTION_COPY);

            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(20)
                    .hasNewBorderNodeCount(0)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram);
        });

        Runnable semanticCheckPart = topNodesSemanticCheckerService.checkElement(Package.class, () -> GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID, rootPackage -> {
            assertThat(rootPackage.getOwnedElement().stream()
                    .filter(PartUsage.class::isInstance)
                    .filter(e -> PART_COPY.equals(e.getName()))
                    .toList()
            ).hasSize(1);
        });

        Runnable semanticCheckAction = topNodesSemanticCheckerService.checkElement(Package.class, () -> GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID, rootPackage -> {
            assertThat(rootPackage.getOwnedElement().stream()
                    .filter(ActionUsage.class::isInstance)
                    .filter(e -> ACTION_COPY.equals(e.getName()))
                    .toList()
            ).hasSize(1);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(duplicateToolRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheckPart)
                .then(semanticCheckAction)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a General View diagram, WHEN a part is created in a Package and duplicated with a StateDefinition, THEN both semantic elements and representations are duplicated")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void checkMultiSelectionDifferentContainerNodeDuplication() {
        var flux = this.givenSubscriptionToDiagram(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        var topNodesSemanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID);
        var nestedPackageSemanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_ID);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        AtomicReference<String> packageNodeId = new AtomicReference<>();
        AtomicReference<String> stateDefinitionNodeId = new AtomicReference<>();
        AtomicReference<String> createdPartNodeId = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var newPartToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage()), "New Part");
        var duplicateToolId = diagramDescriptionIdProvider.getGroupNodeToolId("Duplicate Elements");

        Runnable createPartToolRunnable = () -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                diagram.get().getId(),
                packageNodeId.get(),
                newPartToolId,
                List.of()
        );

        Consumer<Object> diagramCheckAfterCreate = assertRefreshedDiagramThat(newDiagram -> {
            diagram.set(newDiagram);

            var packageNode = new DiagramNavigator(newDiagram).nodeWithTargetObjectId(GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_ID).getNode();
            assertThat(packageNode.getChildNodes()).hasSize(1);

            var createdPartNode = packageNode.getChildNodes().get(0);
            createdPartNodeId.set(createdPartNode.getId());

            assertThat(this.containsLabel(createdPartNode, LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "part1")).isTrue();
        });

        Runnable duplicateToolRunnable = () -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                diagram.get().getId(),
                List.of(createdPartNodeId.get(), stateDefinitionNodeId.get()),
                duplicateToolId,
                List.of()
        );

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();

            this.assertDuplicatedContainerNodeRepresentation(initialDiagram, newDiagram, PART_1_COPY);
            this.assertDuplicatedContainerNodeRepresentation(initialDiagram, newDiagram, STATE_DEFINITION_COPY);

            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(19)
                    .hasNewBorderNodeCount(0)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram);
        });

        Runnable semanticCheckCreatedPartCopy = nestedPackageSemanticCheckerService.checkElement(Package.class, () -> GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_ID, nestedPackage -> {
            assertThat(nestedPackage.getOwnedElement().stream()
                    .filter(PartUsage.class::isInstance)
                    .filter(e -> PART_1_COPY.equals(e.getName()))
                    .toList()
            ).hasSize(1);
        });

        Runnable semanticCheckStateDefinitionCopy = topNodesSemanticCheckerService.checkElement(Package.class, () -> GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID, rootPackage -> {
            assertThat(rootPackage.getOwnedElement().stream()
                    .filter(StateDefinition.class::isInstance)
                    .filter(e -> STATE_DEFINITION_COPY.equals(e.getName()))
                    .toList()
            ).hasSize(1);
        });

        StepVerifier.create(flux)
                .consumeNextWith(diag -> {
                    initialDiagramContentConsumer.accept(diag);
                    var initialDiagram = diag.diagram();
                    packageNodeId.set(new DiagramNavigator(initialDiagram).nodeWithTargetObjectId(GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_ID).getNode().getId());
                    stateDefinitionNodeId.set(new DiagramNavigator(initialDiagram).nodeWithTargetObjectId(GeneralViewWithTopNodesTestProjectData.SemanticIds.STATE_DEFINITION_ID).getNode().getId());
                })
                .then(createPartToolRunnable)
                .consumeNextWith(diagramCheckAfterCreate)
                .then(duplicateToolRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheckCreatedPartCopy)
                .then(semanticCheckStateDefinitionCopy)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    private void assertDuplicatedContainerNodeRepresentation(Diagram initialDiagram, Diagram newDiagram, String label) {
        assertThat(this.diagramComparator.newNodes(initialDiagram, newDiagram).stream().filter(node -> this.containsLabel(node, label)).toList())
                .as("Contains graphical container node for %s".formatted(label))
                .isNotEmpty()
                .allMatch(node -> !node.isBorderNode());
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

    private void assertCtrlDKeyBinding(NodeTool nodeTool) {
        assertThat(nodeTool.getKeyBindings())
                .singleElement()
                .satisfies(keyBinding -> {
                    assertThat(keyBinding.isCtrl()).isTrue();
                    assertThat(keyBinding.isAlt()).isFalse();
                    assertThat(keyBinding.isMeta()).isFalse();
                    assertThat(keyBinding.getKey()).isEqualTo("d");
                });
    }

}
