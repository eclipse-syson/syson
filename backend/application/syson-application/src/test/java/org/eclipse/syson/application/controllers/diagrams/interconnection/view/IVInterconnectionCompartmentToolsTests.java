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
package org.eclipse.syson.application.controllers.diagrams.interconnection.view;

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
import org.eclipse.sirius.components.diagrams.tests.graphql.InvokeSingleClickOnDiagramElementToolExecutor;
import org.eclipse.sirius.components.diagrams.tests.graphql.PaletteQueryRunner;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.InterconnectionViewWithTopNodesTestProjectData;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
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
 * Tests the palette query and execution of tools of {@link org.eclipse.syson.diagram.common.view.nodes.InterconnectionCompartmentNodeDescriptionProvider}.
 *
 * @author mcharfadi
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IVInterconnectionCompartmentToolsTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private PaletteQueryRunner paletteQueryRunner;

    @Autowired
    private InvokeSingleClickOnDiagramElementToolExecutor invokeSingleClickOnDiagramElementToolExecutor;

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                InterconnectionViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                InterconnectionViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a diagram with an Interconnection node, WHEN the palette is requested, THEN the correct tools are available and can be executed")
    @Sql(scripts = { InterconnectionViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void testQueryPaletteAndExecuteTools() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramId = new AtomicReference<String>();
        var interconnectionCompartmentNodeId = new AtomicReference<String>();
        var newPartToolId = new AtomicReference<String>();
        var newActionToolId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            diagramId.set(diagram.getId());
            var interconnectionCompartmentNode = new DiagramNavigator(diagram).nodeWithLabel("interconnection").getNode();
            assertThat(interconnectionCompartmentNode.getChildNodes()).hasSize(2);
            interconnectionCompartmentNodeId.set(interconnectionCompartmentNode.getId());
        });

        Runnable requestPalette = () -> {
            Map<String, Object> variables = Map.of(
                    "editingContextId", InterconnectionViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                    "representationId", diagramId.get(),
                    "diagramElementIds", List.of(interconnectionCompartmentNodeId.get())
            );
            var result = this.paletteQueryRunner.run(variables);

            List<String> quickToolsLabels = JsonPath.read(result, "$.data.viewer.editingContext.representation.description.palette.quickAccessTools[*].label");
            assertThat(quickToolsLabels).hasSize(0);
            List<String> paletteEntriesLabels = JsonPath.read(result, "$.data.viewer.editingContext.representation.description.palette.paletteEntries[*].label");
            assertThat(paletteEntriesLabels).hasSize(4);
            assertThat(paletteEntriesLabels).containsSequence("Structure", "Show/Hide", "Related Elements", "Edit");
            List<String> paletteStructureSectionToolsLabels = JsonPath.read(result, "$.data.viewer.editingContext.representation.description.palette.paletteEntries[0].tools[*].label");
            assertThat(paletteStructureSectionToolsLabels).hasSize(2);
            assertThat(paletteStructureSectionToolsLabels).containsSequence("New Part", "New Action");
            List<String> paletteStructureSectionToolsIds = JsonPath.read(result, "$.data.viewer.editingContext.representation.description.palette.paletteEntries[0].tools[*].id");
            newPartToolId.set(paletteStructureSectionToolsIds.get(0));
            newActionToolId.set(paletteStructureSectionToolsIds.get(0));
        };

        Runnable createPartTool = () -> this.invokeSingleClickOnDiagramElementToolExecutor.execute(InterconnectionViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                diagramId.get(), List.of(interconnectionCompartmentNodeId.get()), newPartToolId.get(), 0, 0, List.of())
                .isSuccess();
        Runnable createActionTool = () -> this.invokeSingleClickOnDiagramElementToolExecutor.execute(InterconnectionViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                diagramId.get(), List.of(interconnectionCompartmentNodeId.get()), newActionToolId.get(), 0, 0, List.of())
                .isSuccess();

        Consumer<Object> afterCreatePartToolConsumer = assertRefreshedDiagramThat(diagram -> {
            var interconnectionCompartmentNode = new DiagramNavigator(diagram).nodeWithLabel("interconnection").getNode();
            assertThat(interconnectionCompartmentNode.getChildNodes()).hasSize(3);
        });

        Consumer<Object> afterCreateActionToolConsumer = assertRefreshedDiagramThat(diagram -> {
            var interconnectionCompartmentNode = new DiagramNavigator(diagram).nodeWithLabel("interconnection").getNode();
            assertThat(interconnectionCompartmentNode.getChildNodes()).hasSize(4);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(requestPalette)
                .then(createPartTool)
                .consumeNextWith(afterCreatePartToolConsumer)
                .then(createActionTool)
                .consumeNextWith(afterCreateActionToolConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

}
