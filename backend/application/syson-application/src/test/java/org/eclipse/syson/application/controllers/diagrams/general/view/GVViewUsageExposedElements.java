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
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controllers.diagrams.testers.NodeCreationTester;
import org.eclipse.syson.application.data.ViewUsageExposedElementsTestProjectData;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.ViewUsage;
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
 * Tests the synchronization between the diagram nodes and the ViewUsage#exposedElements reference.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVViewUsageExposedElements extends AbstractIntegrationTests {

    private final IDescriptionNameGenerator descriptionNameGenerator = new GVDescriptionNameGenerator();

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
    private NodeCreationTester nodeCreationTester;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private IObjectSearchService objectSearchService;

    private Step<DiagramRefreshedEventPayload> verifier;

    private AtomicReference<Diagram> diagram;

    private DiagramDescription diagramDescription;

    private DiagramDescriptionIdProvider diagramDescriptionIdProvider;

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                ViewUsageExposedElementsTestProjectData.EDITING_CONTEXT_ID,
                ViewUsageExposedElementsTestProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.diagram = this.givenDiagram.getDiagram(this.verifier);
        this.diagramDescription = this.givenDiagramDescription.getDiagramDescription(ViewUsageExposedElementsTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        this.diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(this.diagramDescription, this.diagramIdProvider);
    }

    @AfterEach
    public void tearDown() {
        if (this.verifier != null) {
            this.verifier.thenCancel()
                    .verify(Duration.ofSeconds(10));
        }
    }

    @DisplayName("GIVEN a GV diagram on a ViewUsage, WHEN New Part tool is executed, THEN a the ViewUsage#exposedElements is updated with the new Part")
    @Sql(scripts = { ViewUsageExposedElementsTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void newPartToolShouldUpdateExposedElements() {
        String creationToolId = this.diagramDescriptionIdProvider.getDiagramCreationToolId(this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getPartUsage()));
        assertThat(creationToolId).as("The tool 'New Part' should exist on the diagram").isNotNull();
        this.verifier.then(() -> this.nodeCreationTester.createNodeOnDiagram(ViewUsageExposedElementsTestProjectData.EDITING_CONTEXT_ID,
                this.diagram,
                creationToolId));

        this.verifier.consumeNextWith(payload -> Optional.of(payload));

        Runnable semanticChecker = this.semanticRunnableFactory.createRunnable(ViewUsageExposedElementsTestProjectData.EDITING_CONTEXT_ID,
                (editingContext, executeEditingContextFunctionInput) -> {
                    Object viewUsageObject = this.objectSearchService.getObject(editingContext, ViewUsageExposedElementsTestProjectData.SemanticIds.VIEW_USAGE_GV_ID).orElse(null);
                    assertThat(viewUsageObject).isInstanceOf(ViewUsage.class);
                    ViewUsage viewUsage = (ViewUsage) viewUsageObject;

                    assertEquals(1, viewUsage.getExposedElement().size());
                    assertThat(viewUsage.getExposedElement().get(0)).isInstanceOf(PartUsage.class);
                    assertThat(viewUsage.getExposedElement().get(0)).extracting(Element::getDeclaredName).isEqualTo("part2");

                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        this.verifier.then(semanticChecker);
    }

    @DisplayName("GIVEN a GV diagram on a ViewUsage, WHEN Add existing element(s) tool is executed, THEN a the ViewUsage#exposedElements is updated with partA")
    @Sql(scripts = { ViewUsageExposedElementsTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void addExistingElementsToolShouldUpdateExposedElements() {
        String creationToolId = this.diagramDescriptionIdProvider.getDiagramCreationToolId("Add existing elements");
        assertThat(creationToolId).as("The tool 'Add existing elements' should exist on the diagram").isNotNull();
        this.verifier.then(() -> this.nodeCreationTester.createNodeOnDiagram(ViewUsageExposedElementsTestProjectData.EDITING_CONTEXT_ID,
                this.diagram,
                creationToolId));

        this.verifier.consumeNextWith(payload -> Optional.of(payload));

        Runnable semanticChecker = this.semanticRunnableFactory.createRunnable(ViewUsageExposedElementsTestProjectData.EDITING_CONTEXT_ID,
                (editingContext, executeEditingContextFunctionInput) -> {
                    Object viewUsageObject = this.objectSearchService.getObject(editingContext, ViewUsageExposedElementsTestProjectData.SemanticIds.VIEW_USAGE_GV_ID).orElse(null);
                    assertThat(viewUsageObject).isInstanceOf(ViewUsage.class);
                    ViewUsage viewUsage = (ViewUsage) viewUsageObject;

                    assertEquals(1, viewUsage.getExposedElement().size());
                    assertThat(viewUsage.getExposedElement().get(0)).isInstanceOf(PartUsage.class);
                    assertThat(viewUsage.getExposedElement().get(0)).extracting(Element::getElementId).isEqualTo(ViewUsageExposedElementsTestProjectData.SemanticIds.PART_A_ID);

                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        this.verifier.then(semanticChecker);
    }

    @DisplayName("GIVEN a GV diagram on a ViewUsage, WHEN Add existing element(s) (recursive) tool is executed, THEN a the ViewUsage#exposedElements is updated with partA and partB")
    @Sql(scripts = { ViewUsageExposedElementsTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void addExistingElementsRecursivelyToolShouldUpdateExposedElements() {
        String creationToolId = this.diagramDescriptionIdProvider.getDiagramCreationToolId("Add existing elements (recursive)");
        assertThat(creationToolId).as("The tool 'Add existing elements (recursive)' should exist on the diagram").isNotNull();
        this.verifier.then(() -> this.nodeCreationTester.createNodeOnDiagram(ViewUsageExposedElementsTestProjectData.EDITING_CONTEXT_ID,
                this.diagram,
                creationToolId));

        this.verifier.consumeNextWith(payload -> Optional.of(payload));

        Runnable semanticChecker = this.semanticRunnableFactory.createRunnable(ViewUsageExposedElementsTestProjectData.EDITING_CONTEXT_ID,
                (editingContext, executeEditingContextFunctionInput) -> {
                    Object viewUsageObject = this.objectSearchService.getObject(editingContext, ViewUsageExposedElementsTestProjectData.SemanticIds.VIEW_USAGE_GV_ID).orElse(null);
                    assertThat(viewUsageObject).isInstanceOf(ViewUsage.class);
                    ViewUsage viewUsage = (ViewUsage) viewUsageObject;

                    assertEquals(2, viewUsage.getExposedElement().size());
                    assertThat(viewUsage.getExposedElement().get(0)).isInstanceOf(PartUsage.class);
                    assertThat(viewUsage.getExposedElement().get(0)).extracting(Element::getElementId).isEqualTo(ViewUsageExposedElementsTestProjectData.SemanticIds.PART_A_ID);
                    assertThat(viewUsage.getExposedElement().get(1)).isInstanceOf(PartUsage.class);
                    assertThat(viewUsage.getExposedElement().get(1)).extracting(Element::getElementId).isEqualTo(ViewUsageExposedElementsTestProjectData.SemanticIds.PART_B_ID);

                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        this.verifier.then(semanticChecker);
    }
}
