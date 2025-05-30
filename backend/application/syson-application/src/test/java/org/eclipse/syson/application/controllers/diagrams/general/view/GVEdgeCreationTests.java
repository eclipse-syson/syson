/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramInstanceOfAssertFactories.EDGE;
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramInstanceOfAssertFactories.EDGE_STYLE;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.ArrowStyle;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.SysONTestsProperties;
import org.eclipse.syson.application.controller.editingContext.checkers.ISemanticChecker;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.DiagramCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.IDiagramChecker;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeCreationTester;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;
import reactor.test.StepVerifier.Step;

/**
 * Tests the creation of edges in the General View Diagram.
 *
 * @author gdaniel
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = { SysONTestsProperties.NO_DEFAULT_LIBRARIES_PROPERTY })
public class GVEdgeCreationTests extends AbstractIntegrationTests {

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
    private EdgeCreationTester edgeCreationTester;

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

    private final IDescriptionNameGenerator descriptionNameGenerator = new GVDescriptionNameGenerator();

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.diagram = this.givenDiagram.getDiagram(this.verifier);
        this.diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        this.diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(this.diagramDescription, this.diagramIdProvider);
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

    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void createAddAsNestedEdge() {
        String creationToolId = this.diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()), "Add as nested Action");
        this.verifier.then(() -> this.edgeCreationTester.createEdge(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                this.diagram,
                "part",
                "action",
                creationToolId));

        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    // 1 new node has been created in the "actions" compartment of the part.
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);
            DiagramNavigator diagramNavigator = new DiagramNavigator(newDiagram);
            String sourceId = diagramNavigator.nodeWithTargetObjectLabel("part").getNode().getId();
            // Get the target with its description instead of its label: there are new two elements with the label
            // "action" on the diagram (one on the diagram itself, and one in the "actions" compartment of the part
            // element).
            Optional<String> optionalTargetId = diagramNavigator.findAllNodes().stream()
                    .filter(node -> Objects.equals(node.getTargetObjectLabel(), "action"))
                    .filter(node -> Objects.equals(node.getDescriptionId(),
                            this.diagramDescriptionIdProvider.getNodeDescriptionId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()))))
                    .map(Node::getId)
                    .findFirst();
            assertThat(optionalTargetId).isPresent();
            List<Edge> newEdges = this.diagramComparator.newEdges(initialDiagram, newDiagram);
            assertThat(newEdges).hasSize(1).first(EDGE)
                    .hasSourceId(sourceId)
                    .hasTargetId(optionalTargetId.get())
                    .extracting(Edge::getStyle, EDGE_STYLE)
                    .hasSourceArrow(ArrowStyle.FillDiamond);
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);

        ISemanticChecker semanticChecker = this.semanticCheckerService.getElementInParentSemanticChecker("part", SysmlPackage.eINSTANCE.getNamespace_OwnedMember(),
                SysmlPackage.eINSTANCE.getActionUsage());

        this.semanticCheckerService.checkEditingContext(semanticChecker, this.verifier);
    }
}
