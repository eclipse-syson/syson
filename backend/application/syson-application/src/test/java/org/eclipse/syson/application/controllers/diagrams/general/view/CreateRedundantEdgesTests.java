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
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.InvokeSingleClickOnTwoDiagramElementsToolInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.InvokeSingleClickOnTwoDiagramElementsToolSuccessPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.tests.graphql.InvokeSingleClickOnTwoDiagramElementsToolMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.GeneralViewEmptyTestProjectData;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Test that trying to create redundant edges between nodes provides a message instead.
 *
 * @author pcdavid
 */
@Transactional
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateRedundantEdgesTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private ToolTester toolTester;

    @Autowired
    private InvokeSingleClickOnTwoDiagramElementsToolMutationRunner singleClickOnTwoDiagramElementsToolMutationRunner;

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                GeneralViewEmptyTestProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        return flux;
    }

    @DisplayName("GIVEN a SysML Project, WHEN the New Feature Typing tool is requested on a PartUsage which already has a feature typing towards the same target, THEN no redundant feature typing is created but an informative message is returned to the end user")
    @Sql(scripts = { GeneralViewEmptyTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void testAttemptToCreateRedundantFeatureTyping() {
        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        String newPartDefToolId = diagramDescriptionIdProvider.getDiagramCreationToolId("New Part Definition");
        String newPartToolId = diagramDescriptionIdProvider.getDiagramCreationToolId("New Part");

        IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();
        String featureTypingToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()), "New Feature Typing");
        String featureTypingEdgeDescriptionId = diagramDescriptionIdProvider.getEdgeDescriptionId(descriptionNameGenerator.getEdgeName(SysmlPackage.eINSTANCE.getFeatureTyping()));

        var diagramId = new AtomicReference<String>();
        var partDefId = new AtomicReference<String>();
        var partId = new AtomicReference<String>();

        var flux = this.givenSubscriptionToDiagram();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            diagramId.set(diagram.getId());
            assertThat(diagram.getNodes()).hasSize(1); // The "Add your first element" pseudo-node
            assertThat(diagram.getEdges()).isEmpty();
        });

        Runnable createPartDef = () -> this.toolTester.invokeTool(GeneralViewEmptyTestProjectData.EDITING_CONTEXT, diagramId.get(), diagramId.get(), newPartDefToolId, List.of());

        Consumer<Object> updateAfterPartDefCreated = assertRefreshedDiagramThat(diagram -> {
            var partDefNode = this.findItem(diagram, "part def", "PartDefinition1");
            partDefId.set(partDefNode.getId());
            assertThat(diagram.getNodes()).hasSize(1);
            assertThat(diagram.getEdges()).isEmpty();
        });

        Runnable createPart = () -> this.toolTester.invokeTool(GeneralViewEmptyTestProjectData.EDITING_CONTEXT, diagramId.get(), diagramId.get(), newPartToolId, List.of());

        Consumer<Object> updateAfterPartCreated = assertRefreshedDiagramThat(diagram -> {
            var partNode = this.findItem(diagram, "part", "part1");
            partId.set(partNode.getId());
            assertThat(diagram.getNodes()).hasSize(2);
            assertThat(diagram.getEdges()).isEmpty();
        });

        Runnable createInitialFeatureTyping = () -> {
            var result = this.createEdge(diagramId.get(), partId.get(), partDefId.get(), featureTypingToolId);
            String typename = JsonPath.read(result, "$.data.invokeSingleClickOnTwoDiagramElementsTool.__typename");
            assertThat(typename).isEqualTo(InvokeSingleClickOnTwoDiagramElementsToolSuccessPayload.class.getSimpleName());
        };

        Consumer<Object> featureTypingCreated = assertRefreshedDiagramThat(diagram -> {
            var edge = new DiagramNavigator(diagram).edgeWithEdgeDescriptionId(featureTypingEdgeDescriptionId).getEdge();
            assertThat(edge).isNotNull();
            assertThat(diagram.getNodes()).hasSize(2);
            assertThat(diagram.getEdges()).hasSize(1);
        });

        Runnable createRedundantFeatureTyping = () -> {
            var result = this.createEdge(diagramId.get(), partId.get(), partDefId.get(), featureTypingToolId);
            String typename = JsonPath.read(result, "$.data.invokeSingleClickOnTwoDiagramElementsTool.__typename");
            assertThat(typename).isEqualTo(InvokeSingleClickOnTwoDiagramElementsToolSuccessPayload.class.getSimpleName());
            List<String> messages = JsonPath.read(result, "$.data.invokeSingleClickOnTwoDiagramElementsTool.messages[*].body");
            assertThat(messages).hasSameElementsAs(List.of("A feature typing already exists between these elements."));
        };

        Consumer<Object> noDuplicateFeatureTypingCreated = assertRefreshedDiagramThat(diagram -> {
            assertThat(diagram.getNodes()).hasSize(2);
            assertThat(diagram.getEdges()).hasSize(1);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createPartDef)
                .consumeNextWith(updateAfterPartDefCreated)
                .then(createPart)
                .consumeNextWith(updateAfterPartCreated)
                .then(createInitialFeatureTyping)
                .consumeNextWith(featureTypingCreated)
                .then(createRedundantFeatureTyping)
                .consumeNextWith(noDuplicateFeatureTypingCreated)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    private Node findItem(Diagram diagram, String type, String label) {
        return new DiagramNavigator(diagram)
                .nodeWithLabel(LabelConstants.OPEN_QUOTE + type + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + label)
                .getNode();
    }

    private String createEdge(String diagramId, String sourceNodeId, String targetNodeId, String toolId) {
        var input = new InvokeSingleClickOnTwoDiagramElementsToolInput(
                UUID.randomUUID(),
                GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                diagramId,
                sourceNodeId,
                targetNodeId,
                0,
                0,
                0,
                0,
                toolId,
                List.of());
        return this.singleClickOnTwoDiagramElementsToolMutationRunner.run(input);
    }
}
