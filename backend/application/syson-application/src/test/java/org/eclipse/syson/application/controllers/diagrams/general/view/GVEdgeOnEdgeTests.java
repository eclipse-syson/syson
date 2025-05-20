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
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controller.editingContext.checkers.ISemanticChecker;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.DiagramCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.IDiagramChecker;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.GeneralViewEdgeOnEdgeTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.NodeCreationTestsService;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
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
    private IGivenDiagramReference givenDiagram;

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

    private DiagramDescriptionIdProvider diagramDescriptionIdProvider;

    private Step<DiagramRefreshedEventPayload> verifier;

    private AtomicReference<Diagram> diagram;

    private DiagramDescription diagramDescription;

    private DiagramCheckerService diagramCheckerService;

    private SemanticCheckerService semanticCheckerService;

    private NodeCreationTestsService creationTestsService;

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewEdgeOnEdgeTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewEdgeOnEdgeTestProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.diagram = this.givenDiagram.getDiagram(this.verifier);
        this.diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewEdgeOnEdgeTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        this.diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(this.diagramDescription, this.diagramIdProvider);
        this.diagramCheckerService = new DiagramCheckerService(this.diagramComparator, this.descriptionNameGenerator);
        this.semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewEdgeOnEdgeTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewEdgeOnEdgeTestProjectData.SemanticIds.PACKAGE_1_ID);
        this.creationTestsService = new NodeCreationTestsService(this.nodeCreationTester, this.descriptionNameGenerator, GeneralViewEdgeOnEdgeTestProjectData.EDITING_CONTEXT_ID);
    }

    @AfterEach
    public void tearDown() {
        if (this.verifier != null) {
            this.verifier.thenCancel()
                    .verify(Duration.ofSeconds(10));
        }
    }

    @DisplayName("GIVEN a Dependency graphical edge, WHEN using the 'New Comment' tool, THEN a new node should be created with an edge connecting it to the Dependency edge")
    @Test
    @Sql(scripts = { GeneralViewEdgeOnEdgeTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void createCommentOnDependency() {
        String selectedEdgeLabel = "dependency1";
        this.creationTestsService.createNodeOnEdge(this.verifier, this.diagramDescriptionIdProvider, this.diagram, SysmlPackage.eINSTANCE.getDependency(), selectedEdgeLabel,
                this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getComment()));

        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);

            Node newNode = this.diagramComparator.newNodes(initialDiagram, newDiagram).get(0);
            assertThat(newNode.getTargetObjectKind()).isEqualTo("siriusComponents://semantic?domain=sysml&entity=Comment");

            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            assertThat(newEdge.getSourceId()).isEqualTo(newNode.getId());
            assertThat(newEdge.getTargetId()).isEqualTo(GeneralViewEdgeOnEdgeTestProjectData.GraphicalIds.DEPENDENCY_1_ID);
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);

        ISemanticChecker semanticChecker = (editingContext) -> {
            Object dependency1Object = this.objectSearchService.getObject(editingContext, GeneralViewEdgeOnEdgeTestProjectData.SemanticIds.DEPENDENCY_1_ID).orElse(null);
            assertThat(dependency1Object).isInstanceOf(Dependency.class);
            Element dependency1 = (Element) dependency1Object;
            List<Comment> comments = EMFUtils.allContainedObjectOfType(dependency1, Comment.class).toList();
            assertThat(comments).hasSize(1).allMatch(t -> t.getBody().equals("add comment here"));
        };

        this.semanticCheckerService.checkEditingContext(semanticChecker, this.verifier);
    }

    @DisplayName("GIVEN a TransitionUsage graphical edge, WHEN using the 'New Comment' tool, THEN a new node should be created with an edge connecting it to the TransitionUsage edge")
    @Test
    @Sql(scripts = { GeneralViewEdgeOnEdgeTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void createCommentOnTransitionUsage() {
        String selectedEdgeLabel = "transition1";
        this.creationTestsService.createNodeOnEdge(this.verifier, this.diagramDescriptionIdProvider, this.diagram, SysmlPackage.eINSTANCE.getTransitionUsage(), selectedEdgeLabel,
                this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getComment()));

        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);

            Node newNode = this.diagramComparator.newNodes(initialDiagram, newDiagram).get(0);
            assertThat(newNode.getTargetObjectKind()).isEqualTo("siriusComponents://semantic?domain=sysml&entity=Comment");

            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            assertThat(newEdge.getSourceId()).isEqualTo(newNode.getId());
            assertThat(newEdge.getTargetId()).isEqualTo(GeneralViewEdgeOnEdgeTestProjectData.GraphicalIds.TRANSITION_USAGE_1_ID);
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);

        ISemanticChecker semanticChecker = (editingContext) -> {
            Object transition1Object = this.objectSearchService.getObject(editingContext, GeneralViewEdgeOnEdgeTestProjectData.SemanticIds.TRANSITION_USAGE_1_ID).orElse(null);
            assertThat(transition1Object).isInstanceOf(TransitionUsage.class);
            Element transition1 = (Element) transition1Object;
            List<Comment> comments = EMFUtils.allContainedObjectOfType(transition1, Comment.class).toList();
            assertThat(comments).hasSize(1).allMatch(t -> t.getBody().equals("add comment here"));
        };

        this.semanticCheckerService.checkEditingContext(semanticChecker, this.verifier);
    }
}
