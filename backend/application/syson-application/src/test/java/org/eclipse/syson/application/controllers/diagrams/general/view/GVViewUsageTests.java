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
import static org.eclipse.sirius.components.trees.tests.TreeEventPayloadConsumer.assertRefreshedTreeThat;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.trees.api.TreeFilter;
import org.eclipse.sirius.components.core.api.IEditingContextSearchService;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.trees.TreeItem;
import org.eclipse.sirius.components.trees.description.TreeDescription;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.application.views.explorer.ExplorerEventInput;
import org.eclipse.sirius.web.tests.services.api.IGivenCommittedTransaction;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.sirius.web.tests.services.explorer.ExplorerEventSubscriptionRunner;
import org.eclipse.sirius.web.tests.services.representation.RepresentationIdBuilder;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controller.explorer.testers.ExpandAllTreeItemTester;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckChildNode;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.controllers.utils.TestNameGenerator;
import org.eclipse.syson.application.data.GeneralViewViewTestProjectData;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.eclipse.syson.tree.explorer.view.SysONTreeFilterProvider;
import org.eclipse.syson.tree.explorer.view.SysONTreeViewDescriptionProvider;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;

/**
 * Tests related to View in the General View Diagram.
 *
 * @author frouene
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVViewUsageTests extends AbstractIntegrationTests {

    private static final String VIEW_USAGE_NODE_LABEL = LabelConstants.OPEN_QUOTE + "view" + LabelConstants.CLOSE_QUOTE + " view1 : StandardViewDefinitions::GeneralView";

    private static final String PART_USAGE_NODE_LABEL = LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "part1";

    private static final String ACTION_USAGE_NODE_LABEL = LabelConstants.OPEN_QUOTE + "action" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "action1";

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenCommittedTransaction givenCommittedTransaction;

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
    private ToolTester toolTester;

    @Autowired
    private ExplorerEventSubscriptionRunner explorerEventSubscriptionRunner;

    @Autowired
    private RepresentationIdBuilder representationIdBuilder;

    @Autowired
    private SysONTreeViewDescriptionProvider sysonTreeViewDescriptionProvider;

    @Autowired
    private SysONTreeFilterProvider sysonTreeFilterProvider;

    @Autowired
    private ExpandAllTreeItemTester expandAllTreeItemTester;

    @Autowired
    private IRepresentationDescriptionSearchService representationDescriptionSearchService;

    @Autowired
    private IEditingContextSearchService editingContextSearchService;

    private DiagramDescriptionIdProvider diagramDescriptionIdProvider;

    private StepVerifier.Step<DiagramRefreshedEventPayload> verifier;

    private AtomicReference<Diagram> diagram;

    private DiagramDescription diagramDescription;

    private String sysONExplorerTreeDescriptionId;

    private List<String> defaultFilters;

    private static Stream<Arguments> childNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAcceptActionUsage(), 2),
                Arguments.of(SysmlPackage.eINSTANCE.getActionDefinition(), 6),
                Arguments.of(SysmlPackage.eINSTANCE.getActionUsage(), 7),
                Arguments.of(SysmlPackage.eINSTANCE.getAllocationDefinition(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getAllocationUsage(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getAssignmentActionUsage(), 1),
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeDefinition(), 2),
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getConcernDefinition(), 8),
                Arguments.of(SysmlPackage.eINSTANCE.getConcernUsage(), 8),
                Arguments.of(SysmlPackage.eINSTANCE.getConnectionDefinition(), 1),
                Arguments.of(SysmlPackage.eINSTANCE.getConstraintDefinition(), 2),
                Arguments.of(SysmlPackage.eINSTANCE.getConstraintUsage(), 4),
                Arguments.of(SysmlPackage.eINSTANCE.getEnumerationDefinition(), 2),
                Arguments.of(SysmlPackage.eINSTANCE.getExhibitStateUsage(), 6),
                Arguments.of(SysmlPackage.eINSTANCE.getInterfaceDefinition(), 5),
                Arguments.of(SysmlPackage.eINSTANCE.getInterfaceUsage(), 4),
                Arguments.of(SysmlPackage.eINSTANCE.getItemDefinition(), 2),
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), 4),
                Arguments.of(SysmlPackage.eINSTANCE.getMetadataDefinition(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getPackage(), 0),
                Arguments.of(SysmlPackage.eINSTANCE.getOccurrenceDefinition(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getOccurrenceUsage(), 2),
                Arguments.of(SysmlPackage.eINSTANCE.getPartDefinition(), 10),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), 11),
                Arguments.of(SysmlPackage.eINSTANCE.getPortDefinition(), 5),
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), 5),
                Arguments.of(SysmlPackage.eINSTANCE.getRequirementDefinition(), 8),
                Arguments.of(SysmlPackage.eINSTANCE.getRequirementUsage(), 8),
                Arguments.of(SysmlPackage.eINSTANCE.getUseCaseDefinition(), 5),
                Arguments.of(SysmlPackage.eINSTANCE.getUseCaseUsage(), 7),
                Arguments.of(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), 8),
                Arguments.of(SysmlPackage.eINSTANCE.getStateDefinition(), 6),
                Arguments.of(SysmlPackage.eINSTANCE.getStateUsage(), 6),
                Arguments.of(SysmlPackage.eINSTANCE.getViewUsage(), 0)
        ).map(TestNameGenerator::namedArguments);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewViewTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewViewTestProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.diagram = this.givenDiagram.getDiagram(this.verifier);
        this.diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewViewTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        this.diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(this.diagramDescription, this.diagramIdProvider);

        this.sysONExplorerTreeDescriptionId = this.sysonTreeViewDescriptionProvider.getDescriptionId();
        var optionalEditingContext = this.editingContextSearchService.findById(GeneralViewViewTestProjectData.EDITING_CONTEXT_ID);
        TreeDescription treeDescription = optionalEditingContext
                .flatMap(editingContext -> this.representationDescriptionSearchService.findById(editingContext, this.sysONExplorerTreeDescriptionId))
                .filter(TreeDescription.class::isInstance)
                .map(TreeDescription.class::cast)
                .orElse(null);
        this.defaultFilters = this.sysonTreeFilterProvider.get(null, treeDescription).stream()
                .filter(TreeFilter::defaultState)
                .map(TreeFilter::id)
                .toList();
    }

    @AfterEach
    public void tearDown() {
        if (this.verifier != null) {
            this.verifier.thenCancel()
                    .verify(Duration.ofSeconds(10));
        }
    }

    @Sql(scripts = { GeneralViewViewTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("childNodeParameters")
    @DisplayName("GIVEN a General View with ViewUsage node, WHEN child nodes are created, THEN nodes are added to the diagram")
    public void checkViewUsageChildNodeCreation(EClass eClass, int compartmentCount) {
        String creationToolId = this.diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getViewUsage()),
                this.descriptionNameGenerator.getCreationToolName(eClass));

        this.givenCommittedTransaction.commit();

        Runnable invokeTool = () -> this.nodeCreationTester.invokeTool(GeneralViewViewTestProjectData.EDITING_CONTEXT_ID,
                this.diagram.get().getId(),
                GeneralViewViewTestProjectData.GraphicalIds.VIEW_USAGE_ID,
                creationToolId, List.of());

        Consumer<Object> updatedDiagramConsumer = assertRefreshedDiagramThat(newDiagram -> {
            int createdNodesExpectedCount = 1 + compartmentCount;
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(0)
                    .hasNewNodeCount(createdNodesExpectedCount)
                    .check(this.diagram.get(), newDiagram);

            String newNodeDescriptionName = this.descriptionNameGenerator.getNodeName(eClass);

            new CheckChildNode(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentNodeId(GeneralViewViewTestProjectData.GraphicalIds.VIEW_USAGE_ID)
                    .hasNodeDescriptionName(newNodeDescriptionName)
                    .hasCompartmentCount(compartmentCount)
                    .check(this.diagram.get(), newDiagram);
        });

        this.verifier
                .then(invokeTool)
                .consumeNextWith(updatedDiagramConsumer);
    }

    @DisplayName("GIVEN a General View with ViewUsage node, WHEN sub-child nodes are created in the ViewUsage node, THEN nodes are added in the ViewUsage node")
    @Sql(scripts = { GeneralViewViewTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void checkViewUsageSubChildNodeCreation() {
        var partOnViewUsageToolId = this.diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getViewUsage()),
                this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getPartUsage()));
        var actionOnPartToolId = this.diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()),
                this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getActionUsage()));

        var partNodeId = new AtomicReference<String>();

        this.givenCommittedTransaction.commit();

        Runnable newPartOnViewUsage = () -> this.toolTester.invokeTool(GeneralViewViewTestProjectData.EDITING_CONTEXT_ID, GeneralViewViewTestProjectData.GraphicalIds.DIAGRAM_ID,
                GeneralViewViewTestProjectData.GraphicalIds.VIEW_USAGE_ID,
                partOnViewUsageToolId,
                List.of());

        Consumer<Object> updatedDiagramAfterNewPart = assertRefreshedDiagramThat(diag -> {
            var viewUsageNode = new DiagramNavigator(diag).nodeWithLabel(VIEW_USAGE_NODE_LABEL).getNode();

            var partNode = new DiagramNavigator(diag).nodeWithLabel(VIEW_USAGE_NODE_LABEL)
                    .childNodeWithLabel(PART_USAGE_NODE_LABEL)
                    .getNode();
            partNodeId.set(partNode.getId());

            assertThat(viewUsageNode.getChildNodes()).hasSize(1);
            assertThat(viewUsageNode.getChildNodes().get(0)).isEqualTo(partNode);
        });

        Runnable newActionOnPart = () -> this.toolTester.invokeTool(GeneralViewViewTestProjectData.EDITING_CONTEXT_ID, GeneralViewViewTestProjectData.GraphicalIds.DIAGRAM_ID, partNodeId.get(),
                actionOnPartToolId,
                List.of());

        Consumer<Object> updatedDiagramAfterNewSubPart = assertRefreshedDiagramThat(diag -> {
            var viewUsageNode = new DiagramNavigator(diag).nodeWithLabel(VIEW_USAGE_NODE_LABEL).getNode();

            var partNode = new DiagramNavigator(diag).nodeWithLabel(VIEW_USAGE_NODE_LABEL)
                    .childNodeWithLabel(PART_USAGE_NODE_LABEL)
                    .getNode();
            partNodeId.set(partNode.getId());

            var actionNode = new DiagramNavigator(diag).nodeWithLabel(VIEW_USAGE_NODE_LABEL)
                    .childNodeWithLabel(ACTION_USAGE_NODE_LABEL)
                    .getNode();

            assertThat(viewUsageNode.getChildNodes()).hasSize(2);
            assertThat(viewUsageNode.getChildNodes().contains(partNode)).isTrue();
            assertThat(viewUsageNode.getChildNodes().contains(actionNode)).isTrue();
        });

        this.verifier
                .then(newPartOnViewUsage)
                .consumeNextWith(updatedDiagramAfterNewPart)
                .then(newActionOnPart)
                .consumeNextWith(updatedDiagramAfterNewSubPart);
    }

    @DisplayName("GIVEN a diagram, WHEN a ViewUsage is created, THEN the Explorer contains the new ViewUSage and a diagram associated")
    @Sql(scripts = { GeneralViewViewTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void testCreateViewUsage() {
        var viewUsageToolId = this.diagramDescriptionIdProvider.getDiagramCreationToolId(this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getViewUsage()));

        List<String> expandedIds = new ArrayList<>();
        expandedIds.add(GeneralViewViewTestProjectData.SemanticIds.DOCUMENT_ID);
        expandedIds.add(GeneralViewViewTestProjectData.SemanticIds.PACKAGE_1_ID);

        var explorerRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.sysONExplorerTreeDescriptionId, expandedIds, this.defaultFilters);
        var input = new ExplorerEventInput(UUID.randomUUID(), GeneralViewViewTestProjectData.EDITING_CONTEXT_ID, explorerRepresentationId);
        var flux = this.explorerEventSubscriptionRunner.run(input);
        this.givenCommittedTransaction.commit();

        var initialExplorerContentConsumer = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            TreeItem sysmlv2Model = tree.getChildren().get(0);
            assertThat(sysmlv2Model.getLabel().toString()).isEqualTo("GeneralView-View.sysml");
            assertThat(sysmlv2Model.getChildren()).hasSize(1);
            TreeItem pkg1 = sysmlv2Model.getChildren().get(0);
            assertThat(pkg1.getLabel().toString()).isEqualTo("Package 1");

            assertThat(pkg1.getChildren()).hasSize(2);
            TreeItem view1 = pkg1.getChildren().get(0);
            assertThat(view1.getLabel().toString()).isEqualTo("view1 [GeneralView]");
            TreeItem gv = pkg1.getChildren().get(1);
            assertThat(gv.getLabel().toString()).isEqualTo("General View [GeneralView]");
        });

        Runnable newViewUsageOnDiagram = () -> this.toolTester.invokeTool(GeneralViewViewTestProjectData.EDITING_CONTEXT_ID, GeneralViewViewTestProjectData.GraphicalIds.DIAGRAM_ID,
                GeneralViewViewTestProjectData.GraphicalIds.DIAGRAM_ID, viewUsageToolId, List.of());

        AtomicReference<String> treeId = new AtomicReference<>();
        AtomicReference<String> view3Id = new AtomicReference<>();

        var updatedExplorerContentConsumer = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            treeId.set(tree.getId());

            TreeItem sysmlv2Model = tree.getChildren().get(0);
            assertThat(sysmlv2Model.getLabel().toString()).isEqualTo("GeneralView-View.sysml");
            assertThat(sysmlv2Model.getChildren()).hasSize(1);
            TreeItem pkg1 = sysmlv2Model.getChildren().get(0);
            assertThat(pkg1.getLabel().toString()).isEqualTo("Package 1");

            assertThat(pkg1.getChildren()).hasSize(3);
            TreeItem view1 = pkg1.getChildren().get(0);
            assertThat(view1.getLabel().toString()).isEqualTo("view1 [GeneralView]");
            TreeItem gv = pkg1.getChildren().get(1);
            assertThat(gv.getLabel().toString()).isEqualTo("General View [GeneralView]");
            TreeItem view3 = pkg1.getChildren().get(2);
            assertThat(view3.getLabel().toString()).isEqualTo("view3 [GeneralView]");
            assertThat(view3.getChildren()).hasSize(0);
            view3Id.set(view3.getId());
        });

        Runnable expandView3 = () -> expandedIds.addAll(this.expandAllTreeItemTester.expandTreeItem(GeneralViewViewTestProjectData.EDITING_CONTEXT_ID, treeId.get(), view3Id.get()));

        StepVerifier.create(flux)
                .consumeNextWith(initialExplorerContentConsumer)
                .then(newViewUsageOnDiagram)
                .consumeNextWith(updatedExplorerContentConsumer)
                .then(expandView3)
                .thenCancel()
                .verify(Duration.ofSeconds(10));

        var updatedExplorerRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.sysONExplorerTreeDescriptionId, expandedIds, this.defaultFilters);
        var updatedInput = new ExplorerEventInput(UUID.randomUUID(), GeneralViewViewTestProjectData.EDITING_CONTEXT_ID, updatedExplorerRepresentationId);
        var updatedFlux = this.explorerEventSubscriptionRunner.run(updatedInput);
        this.givenCommittedTransaction.commit();

        var updatedExplorerContentConsumerAfterExpand = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            TreeItem sysmlv2Model = tree.getChildren().get(0);
            assertThat(sysmlv2Model.getLabel().toString()).isEqualTo("GeneralView-View.sysml");
            assertThat(sysmlv2Model.getChildren()).hasSize(1);
            TreeItem pkg1 = sysmlv2Model.getChildren().get(0);
            assertThat(pkg1.getLabel().toString()).isEqualTo("Package 1");

            assertThat(pkg1.getChildren()).hasSize(3);
            TreeItem view1 = pkg1.getChildren().get(0);
            assertThat(view1.getLabel().toString()).isEqualTo("view1 [GeneralView]");
            TreeItem gv = pkg1.getChildren().get(1);
            assertThat(gv.getLabel().toString()).isEqualTo("General View [GeneralView]");
            TreeItem view3 = pkg1.getChildren().get(2);
            assertThat(view3.getLabel().toString()).isEqualTo("view3 [GeneralView]");
            assertThat(view3.getChildren()).hasSize(2);
            TreeItem view3Diagram = view3.getChildren().get(0);
            assertThat(view3Diagram.getLabel().toString()).isEqualTo("view3");
        });

        StepVerifier.create(updatedFlux)
                .consumeNextWith(updatedExplorerContentConsumerAfterExpand)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
