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

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.ArrowStyle;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Edge;
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
 * Tests the creation of edges in the General View Diagram.
 *
 * @author gdaniel
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVEdgeCreationTests extends AbstractIntegrationTests {

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

    private SemanticCheckerService semanticCheckerService;

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(), GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        this.semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID);
    }

    @DisplayName("GIVEN a General View with a PartUsage and an ActionUsage, WHEN linking the PartUsage and the Action with Add as nested Action edge tool, THEN the ActionUsage is now a child of the PartUsage and there is a composition edge between them.")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void createAddAsNestedEdge() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        String creationToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()), "Add as nested Action");
        Runnable creationToolRunnable = () -> this.edgeCreationTester.createEdge(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                "part",
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
            var sourceId = diagramNavigator.nodeWithTargetObjectLabel("part").getNode().getId();
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

        ISemanticChecker semanticChecker = this.semanticCheckerService.getElementInParentSemanticChecker("part", SysmlPackage.eINSTANCE.getNamespace_OwnedMember(),
                SysmlPackage.eINSTANCE.getActionUsage());

        Runnable editingContextChecker = this.semanticCheckerService.checkEditingContext(semanticChecker);

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(creationToolRunnable)
                .consumeNextWith(diagramChecker)
                .then(editingContextChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
