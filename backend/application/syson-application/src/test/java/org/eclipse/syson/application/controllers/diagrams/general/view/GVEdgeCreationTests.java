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

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.ArrowStyle;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.diagrams.Label;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controller.editingContext.checkers.ISemanticChecker;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeCreationTester;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.helper.LabelConstants;
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

    private static final String PART_LABEL = "part";

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

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private static Stream<Arguments> addAttributeUsageAsNestedOfEdgeSourceParameters() {
        return Stream.of(
                Arguments.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.ITEM_DEFINITION_ID, SysmlPackage.eINSTANCE.getItemDefinition(), "ItemDefinition", 1, ArrowStyle.FillDiamond),
                Arguments.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_DEFINITION_ID, SysmlPackage.eINSTANCE.getPartDefinition(), "PartDefinition", 1, ArrowStyle.FillDiamond),
                Arguments.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID, SysmlPackage.eINSTANCE.getPartUsage(), PART_LABEL, 1, ArrowStyle.Diamond),
                Arguments.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_USAGE_ID, SysmlPackage.eINSTANCE.getActionUsage(), "action", 1, ArrowStyle.Diamond),
                Arguments.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.STATE_USAGE_ID, SysmlPackage.eINSTANCE.getStateUsage(), "state", 0, ArrowStyle.Diamond)
        );
    }

    private static Stream<Arguments> makeAttributeUsageBecomingNestedOfEdgeTargetParameters() {
        return Stream.of(
                Arguments.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.ITEM_DEFINITION_ID, "ItemDefinition", 1, ArrowStyle.FillDiamond),
                Arguments.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_DEFINITION_ID, "PartDefinition", 1, ArrowStyle.FillDiamond),
                Arguments.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID, PART_LABEL, 1, ArrowStyle.Diamond),
                Arguments.of(GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_USAGE_ID, "action", 1, ArrowStyle.Diamond),
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

    @DisplayName("GIVEN a SysML Project, WHEN the edge tool 'Add as nested Attribute' is applied between a Definition/Usage graphical node and an AttributeUsage graphical node, THEN an edge is created between the Definition/Usage graphical node and an AttributeUsage graphical node")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("addAttributeUsageAsNestedOfEdgeSourceParameters")
    public void addAttributeUsageAsNestedOfEdgeSource(String edgeSourceId, EClass parentClass, String parentLabel, int newNodeCount, ArrowStyle arrowStyle) {
        SemanticCheckerService semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID);
        var flux = this.givenSubscriptionToDiagram(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        String edgeTargetId = GeneralViewWithTopNodesTestProjectData.GraphicalIds.ATTRIBUTE_USAGE_ID;

        String creationToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(parentClass), "Add as nested Attribute");
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

    @DisplayName("GIVEN a General View with a PartUsage and an ActionUsage, WHEN linking the PartUsage and the Action with Add as nested Action edge tool, THEN the ActionUsage is now a child of the PartUsage and there is a composition edge between them.")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void createAddAsNestedEdge() {
        SemanticCheckerService semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID);
        var flux = this.givenSubscriptionToDiagram(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        String creationToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()), "Add as nested Action");
        Runnable creationToolRunnable = () -> this.edgeCreationTester.createEdge(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                PART_LABEL,
                "action",
                creationToolId);

        Consumer<Object> diagramChecker = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();

            new CheckDiagramElementCount(this.diagramComparator)
                    // 1 new node has been created in the "actions" compartment of the part.
                    // 1 new node has been created as nested tree node + 5 compartments
                    .hasNewNodeCount(9)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);
            DiagramNavigator diagramNavigator = new DiagramNavigator(newDiagram);
            var sourceId = diagramNavigator.nodeWithId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID).getNode().getId();
            // Get the target with its description instead of its label: there are two new elements with the label
            // "action" on the diagram (one on the diagram itself, and one in the "actions" compartment of the part
            // element).
            var nodeDescriptionId = diagramDescriptionIdProvider.getNodeDescriptionId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()));
            Optional<String> optionalTargetId = diagramNavigator.findAllNodes().stream()
                    .filter(node -> Objects.equals(node.getTargetObjectLabel(), "action"))
                    .filter(node -> Objects.equals(node.getDescriptionId(), nodeDescriptionId))
                    .filter(node -> Objects.equals(node.getState(), ViewModifier.Normal))
                    .map(Node::getId)
                    .findFirst();
            assertThat(optionalTargetId).isPresent();
            List<Edge> newEdges = this.diagramComparator.newEdges(initialDiagram, newDiagram);
            assertThat(newEdges)
                    .hasSize(1)
                    .first(EDGE)
                    .hasSourceId(sourceId)
                    .hasTargetId(optionalTargetId.get())
                    .extracting(Edge::getStyle, EDGE_STYLE)
                    .hasSourceArrow(ArrowStyle.FillDiamond);
        });

        ISemanticChecker semanticChecker = semanticCheckerService.getElementInParentSemanticChecker(PART_LABEL, SysmlPackage.eINSTANCE.getNamespace_OwnedMember(),
                SysmlPackage.eINSTANCE.getActionUsage());

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

        String satisfyEdgeToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()), "New Satisfy Requirement");
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

        ISemanticChecker semanticChecker = semanticCheckerService.getElementInParentSemanticChecker(PART_LABEL, SysmlPackage.eINSTANCE.getNamespace_OwnedMember(),
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
}
