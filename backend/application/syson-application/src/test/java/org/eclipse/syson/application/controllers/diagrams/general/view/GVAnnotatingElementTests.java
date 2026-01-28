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

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.EditLabelInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.EditLabelSuccessPayload;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.diagrams.InsideLabel;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.tests.graphql.EditLabelMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.graphql.InitialDirectEditElementLabelQueryRunner;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controller.editingContext.checkers.ISemanticChecker;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TextualRepresentation;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

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
    private ToolTester nodeCreationTester;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private DiagramComparator diagramComparator;

    @Autowired
    private InitialDirectEditElementLabelQueryRunner initialDirectEditElementLabelQueryRunner;

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(), GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a Part Definition, WHEN using the 'New TextualRepresentation' tool, THEN a new node should be created with an edge connecting it to the PartDefinition")
    @Test
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    public void createTextualRepresentation() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        String parentLabel = "part";

        String creationToolId = diagramDescriptionIdProvider.getNodeToolId(
                this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()),
                this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getTextualRepresentation())
        );

        Runnable invokeCreationToolRunnable = () -> this.nodeCreationTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                parentLabel,
                creationToolId,
                List.of()
        );

        Consumer<Object> diagramCheckerConsumer = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);

            Node newNode = this.diagramComparator.newNodes(initialDiagram, newDiagram).get(0);
            assertThat(newNode.getTargetObjectKind()).isEqualTo("siriusComponents://semantic?domain=sysml&entity=TextualRepresentation");

            Edge newEdge = this.diagramComparator.newEdges(initialDiagram, newDiagram).get(0);
            assertThat(newEdge.getSourceId()).isEqualTo(newNode.getId());
            assertThat(newEdge.getTargetId()).isEqualTo(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_USAGE_ID);
        });

        ISemanticChecker semanticChecker = (editingContext) -> {
            Object semanticRootObject = this.objectSearchService.getObject(editingContext, GeneralViewWithTopNodesTestProjectData.SemanticIds.PART_USAGE_ID).orElse(null);
            assertThat(semanticRootObject).isInstanceOf(PartUsage.class);
            Element semanticRootElement = (Element) semanticRootObject;
            List<TextualRepresentation> textualRepresentations = EMFUtils.allContainedObjectOfType(semanticRootElement, TextualRepresentation.class)
                    .toList();
            assertThat(textualRepresentations).hasSize(1).allMatch(t -> t.getBody().equals("add textual representation here"));
        };

        Runnable editingContextCheckerRunnable = this.semanticRunnableFactory.createRunnable(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                (editingContext, executeEditingContextFunctionInput) -> {
                    semanticChecker.check(editingContext);
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(invokeCreationToolRunnable)
                .consumeNextWith(diagramCheckerConsumer)
                .then(editingContextCheckerRunnable)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a TextualRepresentation, WHEN using the 'Direct Edit' tool, THEN the body of the textual representation should be updated")
    @Test
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    public void directEditTextualRepresentation() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable checkDirectEditInitialLabelOnNodeRunnable = () -> {
            DiagramNavigator diagramNavigator = new DiagramNavigator(diagram.get());
            String labelId = diagramNavigator.nodeWithId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_DEFINITION_TEXTUAL_REP_ID).getNode().getInsideLabel().getId();

            Map<String, Object> variables = Map.of(
                    "editingContextId", GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                    "diagramId", diagram.get().getId(),
                    "labelId", labelId);
            var initialDirectEditElementLabelResult = this.initialDirectEditElementLabelQueryRunner.run(variables);

            String initialDirectEditElementLabel = JsonPath.read(initialDirectEditElementLabelResult.data(), "$.data.viewer.editingContext.representation.description.initialDirectEditElementLabel");
            assertThat(initialDirectEditElementLabel).isEqualTo("add textual representation here");
        };

        Runnable requestDirectEdit = () -> {
            DiagramNavigator diagramNavigator = new DiagramNavigator(diagram.get());
            String labelId = diagramNavigator.nodeWithId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_DEFINITION_TEXTUAL_REP_ID).getNode().getInsideLabel().getId();

            EditLabelInput input = new EditLabelInput(UUID.randomUUID(), GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagram.get().getId(), labelId, "Some textual representation");
            var result = this.editLabelMutationRunner.run(input);

            String typename = JsonPath.read(result.data(), "$.data.editLabel.__typename");
            assertThat(typename).isEqualTo(EditLabelSuccessPayload.class.getSimpleName());
        };

        Consumer<Object> checker = assertRefreshedDiagramThat(newDiagram -> {
            DiagramNavigator diagramNavigator = new DiagramNavigator(newDiagram);

            InsideLabel newLabel = diagramNavigator.nodeWithId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_DEFINITION_TEXTUAL_REP_ID).getNode().getInsideLabel();
            assertThat(newLabel.getText()).isEqualTo("""
                    «rep»
                    
                    Some textual representation""");
        });

        Runnable runnableChecker = this.semanticRunnableFactory.createRunnable(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                (editingContext, executeEditingContextFunctionInput) -> {
                    Object element = this.objectSearchService.getObject(editingContext, GeneralViewWithTopNodesTestProjectData.SemanticIds.PART_DEFINITION_TEXTUAL_REP_ID).orElse(null);
                    assertThat(element).isInstanceOf(TextualRepresentation.class);
                    TextualRepresentation textualRepresentation = (TextualRepresentation) element;
                    Assertions.assertNotNull(textualRepresentation);
                    assertThat(textualRepresentation.getBody()).isEqualTo("Some textual representation");
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(checkDirectEditInitialLabelOnNodeRunnable)
                .then(requestDirectEdit)
                .consumeNextWith(checker)
                .then(runnableChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
