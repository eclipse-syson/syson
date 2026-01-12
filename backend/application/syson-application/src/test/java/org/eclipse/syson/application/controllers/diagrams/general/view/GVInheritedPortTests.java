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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.InvokeSingleClickOnTwoDiagramElementsToolInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.InvokeSingleClickOnTwoDiagramElementsToolSuccessPayload;
import org.eclipse.sirius.components.diagrams.tests.graphql.ConnectorToolsQueryRunner;
import org.eclipse.sirius.components.diagrams.tests.graphql.InvokeSingleClickOnTwoDiagramElementsToolMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.graphql.PaletteQueryRunner;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controllers.diagrams.graphql.ShowDiagramsInheritedMembersMutationRunner;
import org.eclipse.syson.application.data.GeneralViewInheritedPortTestProjectData;
import org.eclipse.syson.diagram.common.view.services.dto.ShowDiagramsInheritedMembersInput;
import org.eclipse.syson.diagram.common.view.services.dto.ShowDiagramsInheritedMembersSuccessPayload;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Tests the display of inherited ports inside the General View diagram.
 *
 * @author frouene
 */
@Transactional
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVInheritedPortTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private ConnectorToolsQueryRunner connectorToolsQueryRunner;

    @Autowired
    private InvokeSingleClickOnTwoDiagramElementsToolMutationRunner invokeSingleClickOnTwoDiagramElementsToolMutationRunner;

    @Autowired
    private ShowDiagramsInheritedMembersMutationRunner showDiagramsInheritedMembersMutationRunner;

    @Autowired
    private PaletteQueryRunner paletteQueryRunner;

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewInheritedPortTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewInheritedPortTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a diagram with some inherited port, WHEN show inherited members filter is uncheck, THEN inherited ports are not displayed")
    @Sql(scripts = { GeneralViewInheritedPortTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void checkInheritedPortsVisibility() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            diagramId.set(diagram.getId());
            var part2Node = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\npart2").getNode();
            assertThat(part2Node.getBorderNodes()).hasSize(1);
            assertThat(part2Node.getBorderNodes()).allMatch(node -> node.getOutsideLabels().get(0).text().equals("port1"));
            var v1Node = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\nv1 : Vehicle").getNode();
            assertThat(v1Node.getBorderNodes()).hasSize(1);
            assertThat(v1Node.getBorderNodes()).allMatch(node -> node.getOutsideLabels().get(0).text().equals("^fuelInPort : FuelPort"));
        });

        Runnable uncheckShowInheritedMembersFilter = () -> {
            var input = new ShowDiagramsInheritedMembersInput(
                    UUID.randomUUID(),
                    GeneralViewInheritedPortTestProjectData.EDITING_CONTEXT_ID,
                    diagramId.get(),
                    false);
            var result = this.showDiagramsInheritedMembersMutationRunner.run(input);
            String typename = JsonPath.read(result.data(), "$.data.showDiagramsInheritedMembers.__typename");
            assertThat(typename).isEqualTo(ShowDiagramsInheritedMembersSuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentConsumerAfterUncheckInheritedVisibilityChange = assertRefreshedDiagramThat(diagram -> {
            var part2Node = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\npart2").getNode();
            assertThat(part2Node.getBorderNodes()).hasSize(1);
            var v1Node = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\nv1 : Vehicle").getNode();
            assertThat(v1Node.getBorderNodes()).hasSize(0);
        });

        Runnable checkShowInheritedMembersFilter = () -> {
            var input = new ShowDiagramsInheritedMembersInput(
                    UUID.randomUUID(),
                    GeneralViewInheritedPortTestProjectData.EDITING_CONTEXT_ID,
                    diagramId.get(),
                    true);
            var result = this.showDiagramsInheritedMembersMutationRunner.run(input);
            String typename = JsonPath.read(result.data(), "$.data.showDiagramsInheritedMembers.__typename");
            assertThat(typename).isEqualTo(ShowDiagramsInheritedMembersSuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentConsumerAfterCheckInheritedVisibilityChange = assertRefreshedDiagramThat(diagram -> {
            var part2Node = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\npart2").getNode();
            assertThat(part2Node.getBorderNodes()).hasSize(1);
            var v1Node = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\nv1 : Vehicle").getNode();
            assertThat(v1Node.getBorderNodes()).hasSize(1);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(uncheckShowInheritedMembersFilter)
                .consumeNextWith(updatedDiagramContentConsumerAfterUncheckInheritedVisibilityChange)
                .then(checkShowInheritedMembersFilter)
                .consumeNextWith(updatedDiagramContentConsumerAfterCheckInheritedVisibilityChange)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a diagram with some inherited port, WHEN an edge tool is invoke from inherited port, THEN inherited port is redefined")
    @Sql(scripts = { GeneralViewInheritedPortTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @ValueSource(strings = { "New Binding Connector As Usage (bind)", "New Interface (connect)", "New Flow (flow)" })
    public void checkInheritedPortSourceRedefinition(String parameterizedValue) {
        var flux = this.givenSubscriptionToDiagram();
        var diagramId = new AtomicReference<String>();
        var port1Id = new AtomicReference<String>();
        var inheritedPortId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            diagramId.set(diagram.getId());
            var part2Node = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\npart2").getNode();
            assertThat(part2Node.getBorderNodes()).hasSize(1);
            assertThat(part2Node.getBorderNodes()).allMatch(node -> node.getOutsideLabels().get(0).text().equals("port1"));
            port1Id.set(part2Node.getBorderNodes().get(0).getId());
            var v1Node = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\nv1 : Vehicle").getNode();
            assertThat(v1Node.getBorderNodes()).hasSize(1);
            assertThat(v1Node.getBorderNodes()).allMatch(node -> node.getOutsideLabels().get(0).text().equals("^fuelInPort : FuelPort"));
            inheritedPortId.set(v1Node.getBorderNodes().get(0).getId());
            assertThat(diagram.getEdges()).hasSize(2);
        });

        Runnable triggerEdgeTool = () -> {
            Map<String, Object> variables = Map.of(
                    "editingContextId", GeneralViewInheritedPortTestProjectData.EDITING_CONTEXT_ID,
                    "representationId", diagramId.get(),
                    "sourceDiagramElementId", inheritedPortId.get(),
                    "targetDiagramElementId", port1Id.get()
            );
            var connectorToolsResult = this.connectorToolsQueryRunner.run(variables);
            List<String> ids = JsonPath.read(connectorToolsResult.data(), String.format("$.data.viewer.editingContext.representation.description.connectorTools[?(@.label=='Redefine Port And %s')].id",
                    parameterizedValue));
            String toolId = ids.get(0);

            var createEdgeInput = new InvokeSingleClickOnTwoDiagramElementsToolInput(
                    UUID.randomUUID(),
                    GeneralViewInheritedPortTestProjectData.EDITING_CONTEXT_ID,
                    diagramId.get(),
                    inheritedPortId.get(),
                    port1Id.get(),
                    0,
                    0,
                    0,
                    0,
                    toolId,
                    new ArrayList<>());
            var createEdgeResult = this.invokeSingleClickOnTwoDiagramElementsToolMutationRunner.run(createEdgeInput);
            String typename = JsonPath.read(createEdgeResult.data(), "$.data.invokeSingleClickOnTwoDiagramElementsTool.__typename");
            assertThat(typename).isEqualTo(InvokeSingleClickOnTwoDiagramElementsToolSuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentConsumerAfterEdgeTool = assertRefreshedDiagramThat(diagram -> {
            var part2Node = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\npart2").getNode();
            assertThat(part2Node.getBorderNodes()).hasSize(1);
            var v1Node = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\nv1 : Vehicle").getNode();
            assertThat(v1Node.getBorderNodes()).hasSize(1);
            assertThat(v1Node.getBorderNodes()).allMatch(node -> node.getOutsideLabels().get(0).text().equals(" :>> fuelInPort"));
            assertThat(diagram.getEdges()).hasSize(3);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(triggerEdgeTool)
                .consumeNextWith(updatedDiagramContentConsumerAfterEdgeTool)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a diagram with some inherited port, WHEN an edge tool is invoke targeting an inherited port, THEN inherited port is redefined")
    @Sql(scripts = { GeneralViewInheritedPortTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @ValueSource(strings = { "New Binding Connector As Usage (bind)", "New Interface (connect)", "New Flow (flow)" })
    public void checkInheritedPortTargetRedefinition(String parameterizedValue) {
        var flux = this.givenSubscriptionToDiagram();
        var diagramId = new AtomicReference<String>();
        var port1Id = new AtomicReference<String>();
        var inheritedPortId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            diagramId.set(diagram.getId());
            var part2Node = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\npart2").getNode();
            assertThat(part2Node.getBorderNodes()).hasSize(1);
            assertThat(part2Node.getBorderNodes()).allMatch(node -> node.getOutsideLabels().get(0).text().equals("port1"));
            port1Id.set(part2Node.getBorderNodes().get(0).getId());
            var v1Node = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\nv1 : Vehicle").getNode();
            assertThat(v1Node.getBorderNodes()).hasSize(1);
            assertThat(v1Node.getBorderNodes()).allMatch(node -> node.getOutsideLabels().get(0).text().equals("^fuelInPort : FuelPort"));
            inheritedPortId.set(v1Node.getBorderNodes().get(0).getId());
            assertThat(diagram.getEdges()).hasSize(2);
        });

        Runnable triggerEdgeTool = () -> {
            Map<String, Object> variables = Map.of(
                    "editingContextId", GeneralViewInheritedPortTestProjectData.EDITING_CONTEXT_ID,
                    "representationId", diagramId.get(),
                    "sourceDiagramElementId", port1Id.get(),
                    "targetDiagramElementId", inheritedPortId.get()
            );
            var connectorToolsResult = this.connectorToolsQueryRunner.run(variables);
            List<String> ids = JsonPath.read(connectorToolsResult.data(), String.format("$.data.viewer.editingContext.representation.description.connectorTools[?(@.label=='Redefine Port And %s')].id",
                    parameterizedValue));
            String toolId = ids.get(0);

            var createEdgeInput = new InvokeSingleClickOnTwoDiagramElementsToolInput(
                    UUID.randomUUID(),
                    GeneralViewInheritedPortTestProjectData.EDITING_CONTEXT_ID,
                    diagramId.get(),
                    port1Id.get(),
                    inheritedPortId.get(),
                    0,
                    0,
                    0,
                    0,
                    toolId,
                    new ArrayList<>());
            var createEdgeResult = this.invokeSingleClickOnTwoDiagramElementsToolMutationRunner.run(createEdgeInput);
            String typename = JsonPath.read(createEdgeResult.data(), "$.data.invokeSingleClickOnTwoDiagramElementsTool.__typename");
            assertThat(typename).isEqualTo(InvokeSingleClickOnTwoDiagramElementsToolSuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentConsumerAfterEdgeTool = assertRefreshedDiagramThat(diagram -> {
            var part2Node = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\npart2").getNode();
            assertThat(part2Node.getBorderNodes()).hasSize(1);
            var v1Node = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\nv1 : Vehicle").getNode();
            assertThat(v1Node.getBorderNodes()).hasSize(1);
            assertThat(v1Node.getBorderNodes()).allMatch(node -> node.getOutsideLabels().get(0).text().equals(" :>> fuelInPort"));
            assertThat(diagram.getEdges()).hasSize(3);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(triggerEdgeTool)
                .consumeNextWith(updatedDiagramContentConsumerAfterEdgeTool)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a diagram with some inherited port, WHEN an edge tool is invoked from an inherited port and targeting an inherited port, THEN both inherited ports are redefined")
    @Sql(scripts = { GeneralViewInheritedPortTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @ValueSource(strings = { "New Binding Connector As Usage (bind)", "New Interface (connect)", "New Flow (flow)" })
    public void checkInheritedPortAsSourceAndTargetRedefinition(String parameterizedValue) {
        var flux = this.givenSubscriptionToDiagram();
        var diagramId = new AtomicReference<String>();
        var inheritedPortV1Id = new AtomicReference<String>();
        var inheritedPortV2Id = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            diagramId.set(diagram.getId());
            var v1Node = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\nv1 : Vehicle").getNode();
            assertThat(v1Node.getBorderNodes()).hasSize(1);
            assertThat(v1Node.getBorderNodes()).allMatch(node -> node.getOutsideLabels().get(0).text().equals("^fuelInPort : FuelPort"));
            inheritedPortV1Id.set(v1Node.getBorderNodes().get(0).getId());
            var v2Node = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\nv2 : Vehicle").getNode();
            assertThat(v2Node.getBorderNodes()).hasSize(1);
            assertThat(v2Node.getBorderNodes()).allMatch(node -> node.getOutsideLabels().get(0).text().equals("^fuelInPort : FuelPort"));
            inheritedPortV2Id.set(v2Node.getBorderNodes().get(0).getId());
            assertThat(diagram.getEdges()).hasSize(2);
        });

        Runnable triggerEdgeTool = () -> {
            Map<String, Object> variables = Map.of(
                    "editingContextId", GeneralViewInheritedPortTestProjectData.EDITING_CONTEXT_ID,
                    "representationId", diagramId.get(),
                    "sourceDiagramElementId", inheritedPortV1Id.get(),
                    "targetDiagramElementId", inheritedPortV2Id.get()
            );
            var connectorToolsResult = this.connectorToolsQueryRunner.run(variables);
            List<String> ids = JsonPath.read(connectorToolsResult.data(),
                    String.format("$.data.viewer.editingContext.representation.description.connectorTools[?(@.label=='Redefine Ports And %s')].id",
                    parameterizedValue));
            String toolId = ids.get(0);

            var createEdgeInput = new InvokeSingleClickOnTwoDiagramElementsToolInput(
                    UUID.randomUUID(),
                    GeneralViewInheritedPortTestProjectData.EDITING_CONTEXT_ID,
                    diagramId.get(),
                    inheritedPortV1Id.get(),
                    inheritedPortV2Id.get(),
                    0,
                    0,
                    0,
                    0,
                    toolId,
                    new ArrayList<>());
            var createEdgeResult = this.invokeSingleClickOnTwoDiagramElementsToolMutationRunner.run(createEdgeInput);
            String typename = JsonPath.read(createEdgeResult.data(), "$.data.invokeSingleClickOnTwoDiagramElementsTool.__typename");
            assertThat(typename).isEqualTo(InvokeSingleClickOnTwoDiagramElementsToolSuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentConsumerAfterEdgeTool = assertRefreshedDiagramThat(diagram -> {
            var v1Node = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\nv1 : Vehicle").getNode();
            assertThat(v1Node.getBorderNodes()).hasSize(1);
            assertThat(v1Node.getBorderNodes()).allMatch(node -> node.getOutsideLabels().get(0).text().equals(" :>> fuelInPort"));
            var v2Node = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\nv2 : Vehicle").getNode();
            assertThat(v2Node.getBorderNodes()).hasSize(1);
            assertThat(v2Node.getBorderNodes()).allMatch(node -> node.getOutsideLabels().get(0).text().equals(" :>> fuelInPort"));
            assertThat(diagram.getEdges()).hasSize(3);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(triggerEdgeTool)
                .consumeNextWith(updatedDiagramContentConsumerAfterEdgeTool)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a diagram with some inherited port, WHEN palette is retrieved, THEN delete and rename tools should not be available")
    @Sql(scripts = { GeneralViewInheritedPortTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void checkInheritedPortPalette() {
        var flux = this.givenSubscriptionToDiagram();
        var diagramId = new AtomicReference<String>();
        var inheritedPortId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            diagramId.set(diagram.getId());
            var v1Node = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + "\nv1 : Vehicle").getNode();
            assertThat(v1Node.getBorderNodes()).hasSize(1);
            assertThat(v1Node.getBorderNodes()).allMatch(node -> node.getOutsideLabels().get(0).text().equals("^fuelInPort : FuelPort"));
            inheritedPortId.set(v1Node.getBorderNodes().get(0).getId());
        });

        Runnable triggerEdgeTool = () -> {
            Map<String, Object> variables = Map.of(
                    "editingContextId", GeneralViewInheritedPortTestProjectData.EDITING_CONTEXT_ID,
                    "representationId", diagramId.get(),
                    "diagramElementIds", List.of(inheritedPortId.get())
            );
            var result = this.paletteQueryRunner.run(variables);
            List<String> quickAccessToolIds = JsonPath.read(result.data(), "$.data.viewer.editingContext.representation.description.palette.quickAccessTools[*].id");
            assertThat(quickAccessToolIds).doesNotContain("semantic-delete", "edit");
            List<String> editToolSectionIds = JsonPath.read(result.data(), "$.data.viewer.editingContext.representation.description.palette.paletteEntries[?(@.id=='edit-section')].tools[*].id");
            assertThat(editToolSectionIds).doesNotContain("semantic-delete", "edit");
        };

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(triggerEdgeTool)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
