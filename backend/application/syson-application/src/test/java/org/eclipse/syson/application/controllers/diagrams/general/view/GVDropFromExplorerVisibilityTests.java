/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
import org.eclipse.sirius.components.collaborative.diagrams.dto.DropOnDiagramInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DropOnDiagramSuccessPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.InvokeSingleClickOnDiagramElementToolInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.InvokeSingleClickOnDiagramElementToolSuccessPayload;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.diagrams.tests.graphql.DropOnDiagramMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.graphql.InvokeSingleClickOnDiagramElementToolMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.graphql.PaletteQueryRunner;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.SysONTestsProperties;
import org.eclipse.syson.application.data.GeneralViewItemAndAttributeProjectData;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;

/**
 * Tests the visibility of dropped elements from the explorer on the General View diagram.
 *
 * @author mcharfadi
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = { SysONTestsProperties.NO_DEFAULT_LIBRARIES_PROPERTY })
public class GVDropFromExplorerVisibilityTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private DropOnDiagramMutationRunner dropOnDiagramMutationRunner;

    @Autowired
    private PaletteQueryRunner paletteQueryRunner;

    @Autowired
    private InvokeSingleClickOnDiagramElementToolMutationRunner invokeSingleClickOnDiagramElementToolMutationRunner;

    @DisplayName("GIVEN a diagram, WHEN we drop a PartUsage with no empty compartments from the Explorer view, THEN the PartUsage is displayed on the diagram with its compartments hidden")
    @Sql(scripts = { GeneralViewItemAndAttributeProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void dropPartFromTheExplorer() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.DIAGRAM_ID);

        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);

        var diagramId = new AtomicReference<String>();
        var removeFromDiagramToolId = new AtomicReference<String>();
        var diagramTargetId = new AtomicReference<String>();
        var partNodeId = new AtomicReference<String>();
        var partNodeSemanticId = new AtomicReference<String>();

        Consumer<Object> diagramContentConsumerBeforeDrop = assertRefreshedDiagramThat(diagram -> {
            assertThat(diagram.getNodes()).hasSize(3);
            diagramTargetId.set(diagram.getTargetObjectId());
            diagramId.set(diagram.getId());
            var partNode = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "p1").getNode();
            assertThat(partNode.getChildNodes().stream().filter(node -> node.getModifiers().contains(ViewModifier.Hidden))).hasSize(9);
            partNodeSemanticId.set(partNode.getTargetObjectId());
            partNodeId.set(partNode.getId());
        });

        Runnable getRemoveFromDiagramTool = () -> {
            Map<String, Object> variables = Map.of(
                    "editingContextId", GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID,
                    "representationId", diagramId.get(),
                    "diagramElementIds", List.of(partNodeId.get())
            );
            var result = this.paletteQueryRunner.run(variables);

            List<String> labels = JsonPath.read(result.data(), "$.data.viewer.editingContext.representation.description.palette.quickAccessTools[*].label");
            assertThat(labels).hasSize(8);
            assertThat(labels.get(6)).isEqualTo("Delete from Diagram");
            List<String> ids = JsonPath.read(result.data(), "$.data.viewer.editingContext.representation.description.palette.quickAccessTools[*].id");
            removeFromDiagramToolId.set(ids.get(6));
        };

        // Remove the node from the diagram
        Runnable executeRemoveFromDiagramTool = () -> {
            var input = new InvokeSingleClickOnDiagramElementToolInput(UUID.randomUUID(), GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID, diagramId.get(), List.of(partNodeId.get()), removeFromDiagramToolId.get(), 0, 0, List.of());
            var result = this.invokeSingleClickOnDiagramElementToolMutationRunner.run(input);
            String typename = JsonPath.read(result.data(), "$.data.invokeSingleClickOnDiagramElementTool.__typename");
            assertThat(typename).isEqualTo(InvokeSingleClickOnDiagramElementToolSuccessPayload.class.getSimpleName());
        };

        Consumer<Object> diagramContentConsumerAfterRemove = assertRefreshedDiagramThat(diagram -> {
            assertThat(diagram.getNodes()).hasSize(2);
        });

        // Drop from the explorer
        Runnable executeDropPartOnDiagram = () -> {
            var dropOnDiagramInput = new DropOnDiagramInput(UUID.randomUUID(), GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID, diagramId.get(),
                    diagramTargetId.get(), List.of(partNodeSemanticId.get()), 0, 0);
            var dropOnDiagramResult = this.dropOnDiagramMutationRunner.run(dropOnDiagramInput);
            var typename = JsonPath.read(dropOnDiagramResult.data(), "$.data.dropOnDiagram.__typename");
            assertThat(typename).isEqualTo(DropOnDiagramSuccessPayload.class.getSimpleName());
        };

        Consumer<Object> diagramContentConsumerAfterDrop = assertRefreshedDiagramThat(diagram -> {
            assertThat(diagram.getNodes()).hasSize(3);
            diagramTargetId.set(diagram.getTargetObjectId());
            diagramId.set(diagram.getId());
            var partNode = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "p1").getNode();
            assertThat(partNode.getChildNodes().stream().filter(node -> node.getModifiers().contains(ViewModifier.Hidden))).hasSize(11);
            partNodeSemanticId.set(partNode.getTargetObjectId());
            partNodeId.set(partNode.getId());
        });

        StepVerifier.create(flux)
                .consumeNextWith(diagramContentConsumerBeforeDrop)
                .then(getRemoveFromDiagramTool)
                .then(executeRemoveFromDiagramTool)
                .consumeNextWith(diagramContentConsumerAfterRemove)
                .then(executeDropPartOnDiagram)
                .consumeNextWith(diagramContentConsumerAfterDrop)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
