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
import org.eclipse.sirius.components.diagrams.tests.graphql.EditLabelMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.graphql.InitialDirectEditElementLabelQueryRunner;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controller.editingContext.checkers.ISemanticChecker;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.DiagramCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.IDiagramChecker;
import org.eclipse.syson.application.controllers.diagrams.testers.DirectEditInitialLabelTester;
import org.eclipse.syson.application.controllers.diagrams.testers.DirectEditTester;
import org.eclipse.syson.application.controllers.diagrams.testers.NodeCreationTester;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesIdentifiers;
import org.eclipse.syson.application.data.SysMLv2Identifiers;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.NodeCreationTestsService;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TextualRepresentation;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
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
 * Tests several actions on AnnotatingElement of the General View diagram.
 *
 * @author Arthur Daussy
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVAnnotatingElementTests extends AbstractIntegrationTests {

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
    private EditLabelMutationRunner editLabelMutationRunner;

    @Autowired
    private NodeCreationTester nodeCreationTester;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private DiagramComparator diagramComparator;

    @Autowired
    private InitialDirectEditElementLabelQueryRunner initialDirectEditElementLabelQueryRunner;

    private DiagramDescriptionIdProvider diagramDescriptionIdProvider;

    private Step<DiagramRefreshedEventPayload> verifier;

    private AtomicReference<Diagram> diagram;

    private DiagramDescription diagramDescription;

    private DiagramCheckerService diagramCheckerService;

    private SemanticCheckerService semanticCheckerService;

    private DirectEditInitialLabelTester directEditInitialLabelTester;

    private NodeCreationTestsService creationTestsService;

    private final IDescriptionNameGenerator descriptionNameGenerator = new GVDescriptionNameGenerator();

    private DirectEditTester directEditTester;

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewWithTopNodesIdentifiers.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesIdentifiers.Diagram.ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.diagram = this.givenDiagram.getDiagram(this.verifier);
        this.diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesIdentifiers.EDITING_CONTEXT_ID,
                SysMLv2Identifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        this.diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(this.diagramDescription, this.diagramIdProvider);
        this.diagramCheckerService = new DiagramCheckerService(this.diagramComparator, this.descriptionNameGenerator);
        this.semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewWithTopNodesIdentifiers.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesIdentifiers.Semantic.PACKAGE_1);
        this.creationTestsService = new NodeCreationTestsService(this.nodeCreationTester, this.descriptionNameGenerator, GeneralViewWithTopNodesIdentifiers.EDITING_CONTEXT_ID);
        this.directEditInitialLabelTester = new DirectEditInitialLabelTester(this.initialDirectEditElementLabelQueryRunner, GeneralViewWithTopNodesIdentifiers.EDITING_CONTEXT_ID);
        this.directEditTester = new DirectEditTester(this.editLabelMutationRunner, GeneralViewWithTopNodesIdentifiers.EDITING_CONTEXT_ID);
    }

    @AfterEach
    public void tearDown() {
        if (this.verifier != null) {
            this.verifier.thenCancel()
                    .verify(Duration.ofSeconds(10));
        }
    }

    @DisplayName("Given a Part Definition, when using the 'New TextualRepresentation' tool, then a new node should be created with an edge connecting it to the PartDefinition")
    @Test
    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void createTextualRepresentation() {
        String parentLabel = "part";
        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, SysmlPackage.eINSTANCE.getPartUsage(), parentLabel,
                this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getTextualRepresentation()));

        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);

            Node newNode = this.diagramComparator.newNodes(initialDiagram, newDiagram).get(0);
            assertThat(newNode.getTargetObjectKind()).isEqualTo("siriusComponents://semantic?domain=sysml&entity=TextualRepresentation");

            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            assertThat(newEdge.getSourceId()).isEqualTo(newNode.getId());
            assertThat(newEdge.getTargetId()).isEqualTo(GeneralViewWithTopNodesIdentifiers.Diagram.PART_USAGE);
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);

        ISemanticChecker semanticChecker = (editingContext) -> {
            Object semanticRootObject = this.objectSearchService.getObject(editingContext, GeneralViewWithTopNodesIdentifiers.Semantic.PART_USAGE).orElse(null);
            assertThat(semanticRootObject).isInstanceOf(PartUsage.class);
            Element semanticRootElement = (Element) semanticRootObject;
            List<TextualRepresentation> textualRepresentations = EMFUtils.allContainedObjectOfType(semanticRootElement, TextualRepresentation.class)
                    .toList();
            assertThat(textualRepresentations).hasSize(1).allMatch(t -> t.getBody().equals("add textual representation here"));
        };

        this.semanticCheckerService.checkEditingContext(semanticChecker, this.verifier);
    }

    @DisplayName("Given a TextualRepresentation, when using the 'Direct Edit' tool, then the body of the textual representation should be updated")
    @Test
    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void directEditTextualRepresentation() {
        this.directEditInitialLabelTester.checkDirectEditInitialLabelOnNode(this.verifier, this.diagram, GeneralViewWithTopNodesIdentifiers.Diagram.PART_DEFINITION_TEXTUAL_REP,
                "add textual representation here");

        this.directEditTester.checkDirectEdit(this.verifier, this.diagram, GeneralViewWithTopNodesIdentifiers.Diagram.PART_DEFINITION_TEXTUAL_REP, "Some textual representation", "«rep»\n"
                + "\n"
                + "add textual representation here");

        ISemanticChecker semanticChecker = (editingContext) -> {
            Object element = this.objectSearchService.getObject(editingContext, GeneralViewWithTopNodesIdentifiers.Semantic.PART_DEFINITION_TEXTUAL_REP).orElse(null);
            assertThat(element).isInstanceOf(TextualRepresentation.class);
            TextualRepresentation textualRepresentation = (TextualRepresentation) element;
            assertThat(textualRepresentation.getBody()).isEqualTo("Some textual representation");
        };

        this.semanticCheckerService.checkEditingContext(semanticChecker, this.verifier);
    }
}
