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
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
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
import org.eclipse.syson.application.controllers.utils.TestNameGenerator;
import org.eclipse.syson.application.data.GeneralViewEmptyTestProjectData;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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

    private static Stream<Arguments> redundantEdgesArguments() {
        return Stream.of(
                // Part --(feature typing)--> Part Def
                Arguments.of(
                        SysmlPackage.eINSTANCE.getPartUsage(), LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "part1",
                        SysmlPackage.eINSTANCE.getPartDefinition(), LabelConstants.OPEN_QUOTE + "part def" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "PartDefinition1",
                        SysmlPackage.eINSTANCE.getFeatureTyping(), "A feature typing already exists between these elements."),
                // Attribute --(subsetting)--> Attribute
                Arguments.of(
                        SysmlPackage.eINSTANCE.getAttributeUsage(), LabelConstants.OPEN_QUOTE + "attribute" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "attribute1",
                        SysmlPackage.eINSTANCE.getAttributeUsage(), LabelConstants.OPEN_QUOTE + "attribute" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "attribute2",
                        SysmlPackage.eINSTANCE.getSubsetting(), "A subsetting already exists between these elements."),
                // Attribute --(redefinition)--> Attribute
                Arguments.of(
                        SysmlPackage.eINSTANCE.getAttributeUsage(), LabelConstants.OPEN_QUOTE + "attribute" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "attribute1",
                        SysmlPackage.eINSTANCE.getAttributeUsage(), LabelConstants.OPEN_QUOTE + "attribute" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "attribute2",
                        SysmlPackage.eINSTANCE.getRedefinition(), "A redefinition already exists between these elements."),
                // Part Def --(subclassification)--> Part Def
                Arguments.of(
                        SysmlPackage.eINSTANCE.getPartDefinition(), LabelConstants.OPEN_QUOTE + "part def" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "PartDefinition1",
                        SysmlPackage.eINSTANCE.getPartDefinition(), LabelConstants.OPEN_QUOTE + "part def" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "PartDefinition2",
                        SysmlPackage.eINSTANCE.getSubclassification(), "A subclassification already exists between these elements.")
        ).map(TestNameGenerator::namedArguments);
    }

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                GeneralViewEmptyTestProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        return flux;
    }

    @DisplayName("GIVEN a SysML Project, WHEN an edge tool is requested on a pair of node that already have the corresponding relation, THEN no redundant edge is created but an informative message is returned to the end user")
    @Sql(scripts = { GeneralViewEmptyTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("redundantEdgesArguments")
    public void testAttemptToCreateRedundantFeatureTyping(EClass sourceNodeClass, String sourceNodeLabel, EClass targetNodeClass, String targetNodeLabel, EClass edgeType, String expectedInfoMessage) {
        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);
        var nameGenerator = new SDVDescriptionNameGenerator();

        String newSourceNodeToolId = diagramDescriptionIdProvider.getDiagramCreationToolId(nameGenerator.getCreationToolName(sourceNodeClass));
        String newTargetNodeToolId = diagramDescriptionIdProvider.getDiagramCreationToolId(nameGenerator.getCreationToolName(targetNodeClass));
        String newEdgeToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(nameGenerator.getNodeName(sourceNodeClass),
                nameGenerator.getCreationToolName(edgeType));
        String edgeDescriptionId = diagramDescriptionIdProvider.getEdgeDescriptionId(nameGenerator.getEdgeName(edgeType));

        var diagramId = new AtomicReference<String>();
        var sourceNodeId = new AtomicReference<String>();
        var targetNodeId = new AtomicReference<String>();

        var flux = this.givenSubscriptionToDiagram();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            diagramId.set(diagram.getId());
            assertThat(diagram.getNodes()).hasSize(1); // The "Add your first element" pseudo-node
            assertThat(diagram.getEdges()).isEmpty();
        });

        Runnable createSourceNode = () -> this.toolTester.invokeTool(GeneralViewEmptyTestProjectData.EDITING_CONTEXT, diagramId.get(), diagramId.get(), newSourceNodeToolId, List.of());

        Consumer<Object> updateAfterSourceNodeCreated = assertRefreshedDiagramThat(diagram -> {
            var node = this.findItem(diagram, sourceNodeLabel);
            sourceNodeId.set(node.getId());
            assertThat(diagram.getNodes()).hasSize(1);
            assertThat(diagram.getEdges()).isEmpty();
        });

        Runnable createTargetNode = () -> this.toolTester.invokeTool(GeneralViewEmptyTestProjectData.EDITING_CONTEXT, diagramId.get(), diagramId.get(), newTargetNodeToolId, List.of());

        Consumer<Object> updateAfterSecondNodeCreated = assertRefreshedDiagramThat(diagram -> {
            var node = this.findItem(diagram, targetNodeLabel);
            targetNodeId.set(node.getId());
            assertThat(diagram.getNodes()).hasSize(2);
            assertThat(diagram.getEdges()).isEmpty();
        });

        Runnable createInitialEdge = () -> {
            var result = this.createEdge(diagramId.get(), sourceNodeId.get(), targetNodeId.get(), newEdgeToolId);
            String typename = JsonPath.read(result, "$.data.invokeSingleClickOnTwoDiagramElementsTool.__typename");
            assertThat(typename).isEqualTo(InvokeSingleClickOnTwoDiagramElementsToolSuccessPayload.class.getSimpleName());
        };

        Consumer<Object> edgeCreated = assertRefreshedDiagramThat(diagram -> {
            var edge = new DiagramNavigator(diagram).edgeWithEdgeDescriptionId(edgeDescriptionId).getEdge();
            assertThat(edge).isNotNull();
            assertThat(diagram.getNodes()).hasSize(2);
            assertThat(diagram.getEdges()).hasSize(1);
        });

        Runnable createRedundantEdge = () -> {
            var result = this.createEdge(diagramId.get(), sourceNodeId.get(), targetNodeId.get(), newEdgeToolId);
            String typename = JsonPath.read(result, "$.data.invokeSingleClickOnTwoDiagramElementsTool.__typename");
            assertThat(typename).isEqualTo(InvokeSingleClickOnTwoDiagramElementsToolSuccessPayload.class.getSimpleName());
            List<String> messages = JsonPath.read(result, "$.data.invokeSingleClickOnTwoDiagramElementsTool.messages[*].body");
            assertThat(messages).hasSameElementsAs(List.of(expectedInfoMessage));
        };

        Consumer<Object> noDuplicateEdgeCreated = assertRefreshedDiagramThat(diagram -> {
            assertThat(diagram.getNodes()).hasSize(2);
            assertThat(diagram.getEdges()).hasSize(1);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createSourceNode)
                .consumeNextWith(updateAfterSourceNodeCreated)
                .then(createTargetNode)
                .consumeNextWith(updateAfterSecondNodeCreated)
                .then(createInitialEdge)
                .consumeNextWith(edgeCreated)
                .then(createRedundantEdge)
                .consumeNextWith(noDuplicateEdgeCreated)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    private Node findItem(Diagram diagram, String label) {
        return new DiagramNavigator(diagram).nodeWithLabel(label).getNode();
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
