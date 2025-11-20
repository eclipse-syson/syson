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
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariable;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariableType;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.SysONTestsProperties;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.DiagramCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.IDiagramChecker;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.ExhibitStateUsage;
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
 * Tests the invocation of the "New Exhibit State with referenced State" tool from a Part Usage and Part Definition in the General View diagram.
 *
 * @author Jerome Gout
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = { SysONTestsProperties.NO_DEFAULT_LIBRARIES_PROPERTY })
public class GVAddNewExhibitStateWithReferencedStateFromPartsTests extends AbstractIntegrationTests {

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
    private ToolTester nodeCreationTester;

    @Autowired
    private DiagramComparator diagramComparator;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private IIdentityService identityService;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    private DiagramDescriptionIdProvider diagramDescriptionIdProvider;

    private DiagramCheckerService diagramCheckerService;

    private StepVerifier.Step<DiagramRefreshedEventPayload> verifier;

    private AtomicReference<Diagram> diagram;

    private SemanticCheckerService semanticCheckerService;

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.diagram = this.givenDiagram.getDiagram(this.verifier);
        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        this.diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);
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

    @DisplayName("GIVEN a SysML Project, WHEN New Exhibit State with referenced State tool is requested on a PartUsage, THEN a new ExhibitStateUsage node is created")
    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void testApplyNewExhibitStateWithReferencedStateToolFromPartUsage() {
        String creationToolId = this.diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()), "New Exhibit State with referenced State");
        assertThat(creationToolId).as("The tool 'New Exhibit State with referenced State' should exist on a PartUsage").isNotNull();
        this.verifier.then(() -> this.nodeCreationTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                this.diagram,
                "part",
                creationToolId,
                List.of(new ToolVariable("selectedObject", GeneralViewWithTopNodesTestProjectData.SemanticIds.STATE_USAGE_ID , ToolVariableType.OBJECT_ID))));

        String[] newExhibitStateId = new String[1];
        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    // we should have 1 more node (the new ExhibitStateUsage)
                    .hasNewNodeCount(1)
                    .check(initialDiagram, newDiagram);
            var node = this.diagramComparator.newNodes(initialDiagram, newDiagram).get(0);
            newExhibitStateId[0] = node.getTargetObjectId();
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, ExhibitStateUsage.class, () -> newExhibitStateId[0], exhibitStateUsage -> {
            assertThat(this.identityService.getId(exhibitStateUsage.getExhibitedState())).isEqualTo(GeneralViewWithTopNodesTestProjectData.SemanticIds.STATE_USAGE_ID);
        });
    }

    @DisplayName("GIVEN a SysML Project, WHEN New Exhibit State with referenced State tool is requested on a PartDefinition, THEN a new ExhibitStateUsage node is created")
    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void testApplyNewExhibitStateWithReferencedStateToolFromPartDefinition() {
        String creationToolId = this.diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartDefinition()), "New Exhibit State with referenced State");
        assertThat(creationToolId).as("The tool 'New Exhibit State with referenced State' should exist on a PartDefinition").isNotNull();
        this.verifier.then(() -> this.nodeCreationTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                this.diagram,
                "part",
                creationToolId,
                List.of(new ToolVariable("selectedObject", GeneralViewWithTopNodesTestProjectData.SemanticIds.STATE_USAGE_ID , ToolVariableType.OBJECT_ID))));

        String[] newExhibitStateId = new String[1];
        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    // we should have 1 more node (the new ExhibitStateUsage)
                    .hasNewNodeCount(1)
                    .check(initialDiagram, newDiagram);
            var node = this.diagramComparator.newNodes(initialDiagram, newDiagram).get(0);
            newExhibitStateId[0] = node.getTargetObjectId();
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, ExhibitStateUsage.class, () -> newExhibitStateId[0], exhibitStateUsage -> {
            assertThat(this.identityService.getId(exhibitStateUsage.getExhibitedState())).isEqualTo(GeneralViewWithTopNodesTestProjectData.SemanticIds.STATE_USAGE_ID);
        });
    }
}