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
package org.eclipse.syson.application.controllers.diagrams.general.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramInstanceOfAssertFactories.EDGE;
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramInstanceOfAssertFactories.EDGE_STYLE;

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.InvokeSingleClickOnTwoDiagramElementsToolInput;
import org.eclipse.sirius.components.core.api.ErrorPayload;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.ArrowStyle;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.diagrams.Label;
import org.eclipse.sirius.components.diagrams.tests.graphql.ConnectorPaletteQueryRunner;
import org.eclipse.sirius.components.diagrams.tests.graphql.InvokeSingleClickOnTwoDiagramElementsToolMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.view.diagram.NodeDescription;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controller.editingcontext.checkers.ISemanticChecker;
import org.eclipse.syson.application.controller.editingcontext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeCreationTester;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.SuccessionAsUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.metamodel.helper.EMFUtils;
import org.eclipse.syson.sysml.metamodel.helper.LabelConstants;
import org.eclipse.syson.tests.api.GivenSysONServer;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Tests the creation of edges in the General View Diagram.
 *
 * @author gdaniel
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVEdgeCreationTests extends AbstractIntegrationTests {

    private static final String ACTION_LABEL = "action";

    private static final String PART_LABEL = "part";

    private static final String NEW_SUBCLASSIFICATION_TOOL_LABEL = "New Subclassification";

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private EdgeCreationTester edgeCreationTester;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private DiagramComparator diagramComparator;

    @Autowired
    private InvokeSingleClickOnTwoDiagramElementsToolMutationRunner invokeSingleClickOnTwoDiagramElementsToolMutationRunner;

    @Autowired
    private ConnectorPaletteQueryRunner connectorPaletteQueryRunner;

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private static Stream<Arguments> addAttributeUsageAsNestedOfEdgeSourceParameters() {
        return Stream.of(
                Arguments.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.ITEM_DEFINITION_ID, SysmlPackage.eINSTANCE.getItemDefinition(), "ItemDefinition", 1, ArrowStyle.FillDiamond, "Add target as owned Attribute"),
                Arguments.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_DEFINITION_ID, SysmlPackage.eINSTANCE.getPartDefinition(), "PartDefinition", 1, ArrowStyle.FillDiamond, "Add target as owned Attribute"),
                Arguments.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID, SysmlPackage.eINSTANCE.getPartUsage(), PART_LABEL, 1, ArrowStyle.Diamond, "Add target as nested Attribute"),
                Arguments.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_USAGE_ID, SysmlPackage.eINSTANCE.getActionUsage(), ACTION_LABEL, 1, ArrowStyle.Diamond, "Add target as nested Attribute"),
                Arguments.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.STATE_USAGE_ID, SysmlPackage.eINSTANCE.getStateUsage(), "state", 0, ArrowStyle.Diamond, "Add target as nested Attribute")
        );
    }

    private static Stream<Arguments> makeAttributeUsageBecomingNestedOfEdgeTargetParameters() {
        return Stream.of(
                Arguments.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.ITEM_DEFINITION_ID, "ItemDefinition", 1, ArrowStyle.FillDiamond),
                Arguments.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_DEFINITION_ID, "PartDefinition", 1, ArrowStyle.FillDiamond),
                Arguments.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID, PART_LABEL, 1, ArrowStyle.Diamond),
                Arguments.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_USAGE_ID, ACTION_LABEL, 1, ArrowStyle.Diamond),
                Arguments.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.STATE_USAGE_ID, "state", 0, ArrowStyle.Diamond)
        );
    }

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram(String editingContextId, String diagramId) {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(), editingContextId, diagramId);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a General View diagram description, WHEN inspecting the ConcernUsage edge tools, THEN the nested composition tool is correctly labelled")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void concernUsageHasCorrectlyLabelledNestedCompositionEdgeTool() {
        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var concernUsageNodeDescriptionName = this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getConcernUsage());

        var concernUsageNodeDescription = EMFUtils.allContainedObjectOfType(diagramDescription, NodeDescription.class)
                .filter(nodeDescription -> concernUsageNodeDescriptionName.equals(nodeDescription.getName()))
                .findFirst();

        assertThat(concernUsageNodeDescription).isPresent();
        assertThat(concernUsageNodeDescription.get().getPalette().getEdgeTools())
                .extracting(edgeTool -> edgeTool.getName())
                .contains("Become nested Concern");
    }

    @DisplayName("GIVEN a Usage source and a Usage target, WHEN retrieving the connector palette, THEN the nested tools name their target and source respectively")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void connectorPaletteProvidesTargetSpecificNestedUsageTools() {
        var flux = this.givenSubscriptionToDiagram(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);

        StepVerifier.create(flux)
                .consumeNextWith(assertRefreshedDiagramThat(diagram -> {
                    var paletteEntries = this.getConnectorPaletteLabels(diagram.getId(), GeneralViewWithTopNodesTestProjectData.GraphicalIds.ATTRIBUTE_USAGE_ID,
                            GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID);
                    assertThat(paletteEntries).contains("Add target as nested Part", "Become nested Attribute");
                    assertThat(paletteEntries).filteredOn(label -> label.startsWith("Add target as nested ")).containsExactly("Add target as nested Part");

                    var actionTargetPaletteEntries = this.getConnectorPaletteLabels(diagram.getId(), GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID,
                            GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_USAGE_ID);
                    assertThat(actionTargetPaletteEntries).contains("Add target as nested Action", "Become nested Part");
                    assertThat(actionTargetPaletteEntries).filteredOn(label -> label.startsWith("Add target as nested ")).containsExactly("Add target as nested Action");

                    var allocationTargetPaletteEntries = this.getConnectorPaletteLabels(diagram.getId(), GeneralViewWithTopNodesTestProjectData.GraphicalIds.ALLOCATION_DEFINITION_ID,
                            GeneralViewWithTopNodesTestProjectData.GraphicalIds.ALLOCATION_USAGE_ID);
                    assertThat(allocationTargetPaletteEntries).contains("Add target as owned Allocation");
                    assertThat(allocationTargetPaletteEntries).filteredOn(label -> label.startsWith("Add target as owned ")).containsExactly("Add target as owned Allocation");

                    var caseTargetPaletteEntries = this.getConnectorPaletteLabels(diagram.getId(), GeneralViewWithTopNodesTestProjectData.GraphicalIds.ALLOCATION_DEFINITION_ID,
                            GeneralViewWithTopNodesTestProjectData.GraphicalIds.CASE_USAGE_ID);
                    assertThat(caseTargetPaletteEntries).contains("Add target as owned Case");
                    assertThat(caseTargetPaletteEntries).filteredOn(label -> label.startsWith("Add target as owned ")).containsExactly("Add target as owned Case");

                    var definitionTargetPaletteEntries = this.getConnectorPaletteLabels(diagram.getId(), GeneralViewWithTopNodesTestProjectData.GraphicalIds.ATTRIBUTE_USAGE_ID,
                            GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_DEFINITION_ID);
                    assertThat(definitionTargetPaletteEntries).noneMatch(label -> label.startsWith("Add target as nested "));
                    assertThat(definitionTargetPaletteEntries).noneMatch(label -> label.startsWith("Add target as owned "));
                }))
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    /**
     * Retrieves the labels available in the connector palette for two graphical nodes.
     *
     * @param diagramId
     *            the diagram identifier
     * @param sourceDiagramElementId
     *            the source graphical node identifier
     * @param targetDiagramElementId
     *            the target graphical node identifier
     * @return the available connector-palette labels
     */
    private List<String> getConnectorPaletteLabels(String diagramId, String sourceDiagramElementId, String targetDiagramElementId) {
        Map<String, Object> variables = Map.of(
                "editingContextId", GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                "representationId", diagramId,
                "sourceDiagramElementId", sourceDiagramElementId,
                "targetDiagramElementId", targetDiagramElementId);
        var connectorPaletteResult = this.connectorPaletteQueryRunner.run(variables);
        return JsonPath.read(connectorPaletteResult.data(), "$.data.viewer.editingContext.representation.description.connectorPalette.paletteEntries[*].label");
    }

    @DisplayName("GIVEN a SysML Project, WHEN an Add target as nested edge tool is applied between a Definition/Usage graphical node and an AttributeUsage graphical node, THEN an edge is created between the Definition/Usage graphical node and an AttributeUsage graphical node")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("addAttributeUsageAsNestedOfEdgeSourceParameters")
    public void addAttributeUsageAsNestedOfEdgeSource(String edgeSourceId, EClass parentClass, String parentLabel, int newNodeCount, ArrowStyle arrowStyle, String toolName) {
        SemanticCheckerService semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID);
        var flux = this.givenSubscriptionToDiagram(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        String edgeTargetId = GeneralViewWithTopNodesTestProjectData.GraphicalIds.ATTRIBUTE_USAGE_ID;

        String creationToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(parentClass), toolName);
        Runnable creationToolRunnable = () -> this.edgeCreationTester.createEdgeUsingNodeId(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                edgeSourceId,
                edgeTargetId,
                creationToolId);

        Consumer<Object> diagramChecker = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();

            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(newNodeCount)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);

            List<Edge> newEdges = this.diagramComparator.newEdges(initialDiagram, newDiagram);
            assertThat(newEdges)
                    .hasSize(1)
                    .first(EDGE)
                    .hasSourceId(edgeSourceId)
                    .hasTargetId(edgeTargetId)
                    .extracting(Edge::getStyle, EDGE_STYLE)
                    .hasSourceArrow(arrowStyle);
        });

        ISemanticChecker semanticChecker = semanticCheckerService.getElementInParentSemanticChecker(parentLabel, SysmlPackage.eINSTANCE.getElement_OwnedElement(),
                SysmlPackage.eINSTANCE.getAttributeUsage());

        Runnable editingContextChecker = semanticCheckerService.checkEditingContext(semanticChecker);

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(creationToolRunnable)
                .consumeNextWith(diagramChecker)
                .then(editingContextChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a SysML Project, WHEN the edge tool 'Become nested Attribute' is applied between an AttributeUsage graphical node and a Definition/Usage graphical node, THEN an edge is created between the Definition/Usage graphical node and the AttributeUsage graphical node")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("makeAttributeUsageBecomingNestedOfEdgeTargetParameters")
    public void makeAttributeUsageBecomingNestedOfEdgeTarget(String edgeTargetId, String parentLabel, int newNodeCount, ArrowStyle arrowStyle) {
        SemanticCheckerService semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID);
        var flux = this.givenSubscriptionToDiagram(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        String edgeSourceId = GeneralViewWithTopNodesTestProjectData.GraphicalIds.ATTRIBUTE_USAGE_ID;

        String creationToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getAttributeUsage()), "Become nested Attribute");
        Runnable creationToolRunnable = () -> this.edgeCreationTester.createEdgeUsingNodeId(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                edgeSourceId,
                edgeTargetId,
                creationToolId);

        Consumer<Object> diagramChecker = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();

            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(newNodeCount)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);

            List<Edge> newEdges = this.diagramComparator.newEdges(initialDiagram, newDiagram);
            assertThat(newEdges)
                    .hasSize(1)
                    .first(EDGE)
                    .hasSourceId(edgeTargetId)
                    .hasTargetId(edgeSourceId)
                    .extracting(Edge::getStyle, EDGE_STYLE)
                    .hasSourceArrow(arrowStyle);
        });

        ISemanticChecker semanticChecker = semanticCheckerService.getElementInParentSemanticChecker(parentLabel, SysmlPackage.eINSTANCE.getElement_OwnedElement(),
                SysmlPackage.eINSTANCE.getAttributeUsage());

        Runnable editingContextChecker = semanticCheckerService.checkEditingContext(semanticChecker);

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(creationToolRunnable)
                .consumeNextWith(diagramChecker)
                .then(editingContextChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a General View with a PartUsage and a RequirementUsage, WHEN linking the PartUsage and the RequirementUsage with Satisfy tool, THEN a SatisfyRequirementUsage is created and there is an edge between the PartUsage and the RequirementUsage.")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void createSatisfyEdge() {
        SemanticCheckerService semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID);
        var flux = this.givenSubscriptionToDiagram(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var satisfyEdgeToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()), "New Satisfy Requirement");

        Runnable creationToolRunnable = () -> this.edgeCreationTester.createEdge(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                PART_LABEL,
                "requirement",
                satisfyEdgeToolId);

        Consumer<Object> diagramChecker = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();

            new CheckDiagramElementCount(this.diagramComparator)
                    // 1 new node has been created in the "satisfy requirements" compartment of the part.
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);
            DiagramNavigator diagramNavigator = new DiagramNavigator(newDiagram);
            var sourceId = diagramNavigator.nodeWithId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID).getNode().getId();
            var targetId = diagramNavigator.nodeWithId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.REQUIREMENT_USAGE_ID).getNode().getId();
            List<Edge> newEdges = this.diagramComparator.newEdges(initialDiagram, newDiagram);
            assertThat(newEdges)
                    .hasSize(1)
                    .first(EDGE)
                    .hasSourceId(sourceId)
                    .hasTargetId(targetId)
                    .extracting(Edge::getCenterLabel)
                    .extracting(Label::text)
                    .hasToString(LabelConstants.OPEN_QUOTE + LabelConstants.SATISFY + LabelConstants.CLOSE_QUOTE);
        });

        var semanticChecker = semanticCheckerService.getElementInParentSemanticChecker(PART_LABEL, SysmlPackage.eINSTANCE.getNamespace_OwnedMember(),
                SysmlPackage.eINSTANCE.getSatisfyRequirementUsage());

        Runnable editingContextChecker = semanticCheckerService.checkEditingContext(semanticChecker);

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(creationToolRunnable)
                .consumeNextWith(diagramChecker)
                .then(editingContextChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a General View with a PartUsage and an ActionUsage, WHEN linking the PartUsage and the ActionUsage with Satisfy tool, THEN it should not be possible")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void doNotCreateSatisfyEdge() {
        var flux = this.givenSubscriptionToDiagram(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var satisfyEdgeToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()), "New Satisfy Requirement");

        Runnable creationToolRunnable = () -> this.edgeCreationTester.createEdge(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                PART_LABEL,
                "requirement",
                satisfyEdgeToolId);

        var createEdgeInput = new InvokeSingleClickOnTwoDiagramElementsToolInput(
                UUID.randomUUID(),
                GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_USAGE_ID,
                0,
                0,
                0,
                0,
                satisfyEdgeToolId,
                new ArrayList<>());
        var createEdgeResult = this.invokeSingleClickOnTwoDiagramElementsToolMutationRunner.run(createEdgeInput);
        String typename = JsonPath.read(createEdgeResult.data(), "$.data.invokeSingleClickOnTwoDiagramElementsTool.__typename");
        assertThat(typename).isEqualTo(ErrorPayload.class.getSimpleName());

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(creationToolRunnable)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a ViewUsage-backed General View with top-level ActionUsages, WHEN using the New Succession tool on diagram background nodes, THEN the created SuccessionAsUsage is owned by the container element of the ViewUsage")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void createBackgroundSuccessionOwnedByViewUsageContainer() {
        SemanticCheckerService semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID);
        var flux = this.givenSubscriptionToDiagram(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);
        var creationToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()), "New Succession");

        Runnable creationToolRunnable = () -> this.edgeCreationTester.createEdgeUsingNodeId(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_USAGE_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.STATE_USAGE_ID,
                creationToolId);

        AtomicReference<String> newSuccessionId = new AtomicReference<>();
        Consumer<Object> diagramChecker = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(0)
                    .hasNewEdgeCount(1)
                    .check(diagram.get(), newDiagram);
            Edge newEdge = this.diagramComparator.newEdges(diagram.get(), newDiagram).get(0);
            newSuccessionId.set(newEdge.getTargetObjectId());
            assertThat(newEdge.getSourceId()).isEqualTo(GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_USAGE_ID);
            assertThat(newEdge.getTargetId()).isEqualTo(GeneralViewWithTopNodesTestProjectData.GraphicalIds.STATE_USAGE_ID);
        });

        Runnable semanticCheck = semanticCheckerService.checkElement(SuccessionAsUsage.class, newSuccessionId::get, successionAsUsage -> {
            assertThat(successionAsUsage.getOwner()).isNotInstanceOf(ViewUsage.class);
            assertThat(successionAsUsage.getOwner().getName()).isEqualTo("Package 1");
            assertThat(successionAsUsage.getSourceFeature().getName()).isEqualTo(ACTION_LABEL);
            assertThat(successionAsUsage.getTargetFeature()).hasSize(1)
                    .allMatch(targetFeature -> "state".equals(targetFeature.getName()));
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(creationToolRunnable)
                .consumeNextWith(diagramChecker)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a ViewUsage-backed General View with top-level ActionUsages, WHEN using the New Transition tool on diagram background nodes, THEN the created TransitionUsage is owned by the container element of the ViewUsage")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void createBackgroundTransitionOwnedByViewUsageContainer() {
        SemanticCheckerService semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID);
        var flux = this.givenSubscriptionToDiagram(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);
        var creationToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()), "New Transition");

        Runnable creationToolRunnable = () -> this.edgeCreationTester.createEdgeUsingNodeId(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_USAGE_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.STATE_USAGE_ID,
                creationToolId);

        AtomicReference<String> newTransitionId = new AtomicReference<>();
        Consumer<Object> diagramChecker = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(0)
                    .hasNewEdgeCount(1)
                    .check(diagram.get(), newDiagram);
            Edge newEdge = this.diagramComparator.newEdges(diagram.get(), newDiagram).get(0);
            newTransitionId.set(newEdge.getTargetObjectId());
            assertThat(newEdge.getSourceId()).isEqualTo(GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_USAGE_ID);
            assertThat(newEdge.getTargetId()).isEqualTo(GeneralViewWithTopNodesTestProjectData.GraphicalIds.STATE_USAGE_ID);
        });

        Runnable semanticCheck = semanticCheckerService.checkElement(TransitionUsage.class, newTransitionId::get, transitionUsage -> {
            assertThat(transitionUsage.getOwner()).isNotInstanceOf(ViewUsage.class);
            assertThat(transitionUsage.getOwner().getName()).isEqualTo("Package 1");
            assertThat(transitionUsage.getSource().getName()).isEqualTo(ACTION_LABEL);
            assertThat(transitionUsage.getTarget().getName()).isEqualTo("state");
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(creationToolRunnable)
                .consumeNextWith(diagramChecker)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN an Element source and an Element target, WHEN retrieving the connector palette, THEN the New Subclassification tool appears accordingly")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void connectorPaletteProvidesSubclassificationTool() {
        var flux = this.givenSubscriptionToDiagram(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        StepVerifier.create(flux)
                .consumeNextWith(assertRefreshedDiagramThat(diagram -> {
                    var actionDefinitionToActionUsagePaletteEntries = this.getConnectorPaletteLabels(diagram.getId(),
                            GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_DEFINITION_ID,
                            GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_USAGE_ID);
                    assertThat(actionDefinitionToActionUsagePaletteEntries).noneMatch(label -> label.startsWith(NEW_SUBCLASSIFICATION_TOOL_LABEL));
                    var actionDefinitionToActionDefinitionPaletteEntries = this.getConnectorPaletteLabels(diagram.getId(),
                            GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_DEFINITION_ID,
                            GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_DEFINITION_ID);
                    assertThat(actionDefinitionToActionDefinitionPaletteEntries).contains(NEW_SUBCLASSIFICATION_TOOL_LABEL);
                    var enumerationDefinitionToEnumerationDefinitionPaletteEntries =  this.getConnectorPaletteLabels(diagram.getId(),
                            GeneralViewWithTopNodesTestProjectData.GraphicalIds.ENUMERATION_DEFINITION_ID,
                            GeneralViewWithTopNodesTestProjectData.GraphicalIds.ENUMERATION_DEFINITION_ID);
                    assertThat(enumerationDefinitionToEnumerationDefinitionPaletteEntries).noneMatch(label -> label.startsWith(NEW_SUBCLASSIFICATION_TOOL_LABEL));
                }))
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
