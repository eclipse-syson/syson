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
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.GeneralViewEdgeOnEdgeTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.NodeCreationTestsService;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.Comment;
import org.eclipse.syson.sysml.Dependency;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.helper.EMFUtils;
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
 * Edge on edge related tests on General View.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVEdgeOnEdgeTests extends AbstractIntegrationTests {

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
    private ToolTester nodeCreationTester;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private DiagramComparator diagramComparator;

    private SemanticCheckerService semanticCheckerService;

    private NodeCreationTestsService creationTestsService;

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(), GeneralViewEdgeOnEdgeTestProjectData.EDITING_CONTEXT_ID, GeneralViewEdgeOnEdgeTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        this.semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewEdgeOnEdgeTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewEdgeOnEdgeTestProjectData.SemanticIds.PACKAGE_1_ID);
        this.creationTestsService = new NodeCreationTestsService(this.nodeCreationTester, this.descriptionNameGenerator, GeneralViewEdgeOnEdgeTestProjectData.EDITING_CONTEXT_ID);
    }

    @DisplayName("GIVEN a Dependency graphical edge, WHEN using the 'New Comment' tool, THEN a new node should be created with an edge connecting it to the Dependency edge")
    @Test
    @GivenSysONServer({ GeneralViewEdgeOnEdgeTestProjectData.SCRIPT_PATH })
    public void createCommentOnDependency() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewEdgeOnEdgeTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        String selectedEdgeLabel = "dependency1";
        Runnable createNodeRunnable = this.creationTestsService.createNodeOnEdge(diagramDescriptionIdProvider, diagram, SysmlPackage.eINSTANCE.getDependency(), selectedEdgeLabel,
                this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getComment()));

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);

            Node newNode = this.diagramComparator.newNodes(initialDiagram, newDiagram).get(0);
            assertThat(newNode.getTargetObjectKind()).isEqualTo("siriusComponents://semantic?domain=sysml&entity=Comment");

            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            assertThat(newEdge.getSourceId()).isEqualTo(newNode.getId());
            assertThat(newEdge.getTargetId()).isEqualTo(GeneralViewEdgeOnEdgeTestProjectData.GraphicalIds.DEPENDENCY_1_ID);
        });

        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(editingContext -> {
            Object dependency1Object = this.objectSearchService.getObject(editingContext, GeneralViewEdgeOnEdgeTestProjectData.SemanticIds.DEPENDENCY_1_ID).orElse(null);
            assertThat(dependency1Object).isInstanceOf(Dependency.class);
            Element dependency1 = (Element) dependency1Object;
            List<Comment> comments = EMFUtils.allContainedObjectOfType(dependency1, Comment.class).toList();
            assertThat(comments).hasSize(1).allMatch(t -> t.getBody().equals("add comment here"));
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a TransitionUsage graphical edge, WHEN using the 'New Comment' tool, THEN a new node should be created with an edge connecting it to the TransitionUsage edge")
    @Test
    @GivenSysONServer({ GeneralViewEdgeOnEdgeTestProjectData.SCRIPT_PATH })
    public void createCommentOnTransitionUsage() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewEdgeOnEdgeTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        String selectedEdgeLabel = "transition1";
        Runnable createNodeRunnable = this.creationTestsService.createNodeOnEdge(diagramDescriptionIdProvider, diagram, SysmlPackage.eINSTANCE.getTransitionUsage(), selectedEdgeLabel,
                this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getComment()));

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);

            Node newNode = this.diagramComparator.newNodes(initialDiagram, newDiagram).get(0);
            assertThat(newNode.getTargetObjectKind()).isEqualTo("siriusComponents://semantic?domain=sysml&entity=Comment");

            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            assertThat(newEdge.getSourceId()).isEqualTo(newNode.getId());
            assertThat(newEdge.getTargetId()).isEqualTo(GeneralViewEdgeOnEdgeTestProjectData.GraphicalIds.TRANSITION_USAGE_1_ID);
        });

        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(editingContext -> {
            Object transition1Object = this.objectSearchService.getObject(editingContext, GeneralViewEdgeOnEdgeTestProjectData.SemanticIds.TRANSITION_USAGE_1_ID).orElse(null);
            assertThat(transition1Object).isInstanceOf(TransitionUsage.class);
            Element transition1 = (Element) transition1Object;
            List<Comment> comments = EMFUtils.allContainedObjectOfType(transition1, Comment.class).toList();
            assertThat(comments).hasSize(1).allMatch(t -> t.getBody().equals("add comment here"));
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
