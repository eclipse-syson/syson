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
package org.eclipse.syson.application.controllers.diagrams;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ResetNodeAppearanceInput;
import org.eclipse.sirius.components.diagrams.LineStyle;
import org.eclipse.sirius.components.diagrams.tests.graphql.ResetNodeAppearanceMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controllers.diagrams.graphql.EditSysMLViewFrameNodeAppearanceMutationRunner;
import org.eclipse.syson.application.data.AllCustomNodesProjectData;
import org.eclipse.syson.application.nodes.SysMLViewFrameNodeStyle;
import org.eclipse.syson.application.nodes.dto.EditSysMLViewFrameNodeAppearanceInput;
import org.eclipse.syson.application.nodes.dto.SysMLViewFrameNodeAppearanceInput;
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
 * Tests for SysMLViewFrame node appearance edition.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EditSysMLViewFrameNodeAppearanceControllerTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private EditSysMLViewFrameNodeAppearanceMutationRunner editSysMLViewFrameNodeAppearanceMutationRunner;

    @Autowired
    private ResetNodeAppearanceMutationRunner resetNodeAppearanceMutationRunner;

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    private Flux<DiagramRefreshedEventPayload> givenDiagramSubscription() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                AllCustomNodesProjectData.EDITING_CONTEXT_ID,
                AllCustomNodesProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        return flux;
    }

    @DisplayName("GIVEN a diagram, WHEN we edit all its appearance and reset changes, THEN the diagram is properly updated")
    @Sql(scripts = { AllCustomNodesProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void givenDiagramWhenWeEditAllItsAppearanceAndResetChangesThenTheDiagramIsProperlyUpdated() {
        var flux = this.givenDiagramSubscription();
        var diagramId = new AtomicReference<String>();
        var siriusWebApplicationNodeId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            diagramId.set(diagram.getId());
            assertThat(diagram.getNodes())
                    .filteredOn(node -> node.getInsideLabel() != null && node.getInsideLabel().getText().equals(AllCustomNodesProjectData.Labels.VIEW_FRAME))
                    .hasSize(1)
                    .allMatch(node -> node.getStyle() instanceof SysMLViewFrameNodeStyle)
                    .extracting(node -> (SysMLViewFrameNodeStyle) node.getStyle())
                    .allMatch(sysMLViewFrameNodeStyle -> "#ffffff".equals(sysMLViewFrameNodeStyle.getBackground()))
                    .allMatch(sysMLViewFrameNodeStyle -> "#000000".equals(sysMLViewFrameNodeStyle.getBorderColor()))
                    .allMatch(sysMLViewFrameNodeStyle -> sysMLViewFrameNodeStyle.getBorderSize() == 1)
                    .allMatch(sysMLViewFrameNodeStyle -> LineStyle.Solid.equals(sysMLViewFrameNodeStyle.getBorderStyle()))
                    .allMatch(sysMLViewFrameNodeStyle -> sysMLViewFrameNodeStyle.getBorderRadius() == 10);

            var siriusWebApplicationNode = new DiagramNavigator(diagram).nodeWithLabel(AllCustomNodesProjectData.Labels.VIEW_FRAME).getNode();
            siriusWebApplicationNodeId.set(siriusWebApplicationNode.getId());
        });

        Runnable setNodeCustomAppearance = () -> {
            var appearanceInput = new SysMLViewFrameNodeAppearanceInput("red", "blue", 5, LineStyle.Dash, 5);

            var input = new EditSysMLViewFrameNodeAppearanceInput(
                    UUID.randomUUID(),
                    AllCustomNodesProjectData.EDITING_CONTEXT_ID.toString(),
                    diagramId.get(),
                    siriusWebApplicationNodeId.get(),
                    appearanceInput);

            this.editSysMLViewFrameNodeAppearanceMutationRunner.run(input);
        };

        Consumer<Object> updatedAfterCustomAppearanceDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            assertThat(diagram.getNodes())
                    .filteredOn(node -> node.getInsideLabel() != null && node.getInsideLabel().getText().equals(AllCustomNodesProjectData.Labels.VIEW_FRAME))
                    .hasSize(1)
                    .allMatch(node -> node.getStyle() instanceof SysMLViewFrameNodeStyle)
                    .extracting(node -> (SysMLViewFrameNodeStyle) node.getStyle())
                    .allMatch(sysMLViewFrameNodeStyle -> "red".equals(sysMLViewFrameNodeStyle.getBackground()))
                    .allMatch(sysMLViewFrameNodeStyle -> "blue".equals(sysMLViewFrameNodeStyle.getBorderColor()))
                    .allMatch(sysMLViewFrameNodeStyle -> sysMLViewFrameNodeStyle.getBorderSize() == 5)
                    .allMatch(sysMLViewFrameNodeStyle -> LineStyle.Dash.equals(sysMLViewFrameNodeStyle.getBorderStyle()))
                    .allMatch(sysMLViewFrameNodeStyle -> sysMLViewFrameNodeStyle.getBorderRadius() == 5);
        });

        Runnable resetNodeCustomAppearance = () -> {
            var input = new ResetNodeAppearanceInput(
                    UUID.randomUUID(),
                    AllCustomNodesProjectData.EDITING_CONTEXT_ID.toString(),
                    diagramId.get(),
                    siriusWebApplicationNodeId.get(),
                    List.of("BACKGROUND", "BORDER_COLOR", "BORDER_RADIUS", "BORDER_SIZE", "BORDER_STYLE"));

            this.resetNodeAppearanceMutationRunner.run(input);
        };

        Consumer<Object> updatedAfterResetCustomAppearanceDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            assertThat(diagram.getNodes())
                    .filteredOn(node -> node.getInsideLabel() != null && node.getInsideLabel().getText().equals(AllCustomNodesProjectData.Labels.VIEW_FRAME))
                    .hasSize(1)
                    .allMatch(node -> node.getStyle() instanceof SysMLViewFrameNodeStyle)
                    .extracting(node -> (SysMLViewFrameNodeStyle) node.getStyle())
                    .allMatch(sysMLViewFrameNodeStyle -> "#ffffff".equals(sysMLViewFrameNodeStyle.getBackground()))
                    .allMatch(sysMLViewFrameNodeStyle -> "#000000".equals(sysMLViewFrameNodeStyle.getBorderColor()))
                    .allMatch(sysMLViewFrameNodeStyle -> sysMLViewFrameNodeStyle.getBorderSize() == 1)
                    .allMatch(sysMLViewFrameNodeStyle -> LineStyle.Solid.equals(sysMLViewFrameNodeStyle.getBorderStyle()))
                    .allMatch(sysMLViewFrameNodeStyle -> sysMLViewFrameNodeStyle.getBorderRadius() == 10);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(setNodeCustomAppearance)
                .consumeNextWith(updatedAfterCustomAppearanceDiagramContentConsumer)
                .then(resetNodeCustomAppearance)
                .consumeNextWith(updatedAfterResetCustomAppearanceDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a diagram, WHEN we edit two of its appearance and reset one change, THEN only one property is reset")
    @Sql(scripts = { AllCustomNodesProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void givenDiagramWhenWeEditTwoOfItsAppearanceAndResetOneChangeThenOnlyOnePropertyIsReset() {
        var flux = this.givenDiagramSubscription();
        var diagramId = new AtomicReference<String>();
        var siriusWebApplicationNodeId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            diagramId.set(diagram.getId());
            assertThat(diagram.getNodes())
                    .filteredOn(node -> node.getInsideLabel() != null && node.getInsideLabel().getText().equals(AllCustomNodesProjectData.Labels.VIEW_FRAME))
                    .hasSize(1)
                    .allMatch(node -> node.getStyle() instanceof SysMLViewFrameNodeStyle)
                    .extracting(node -> (SysMLViewFrameNodeStyle) node.getStyle())
                    .allMatch(sysMLViewFrameNodeStyle -> "#ffffff".equals(sysMLViewFrameNodeStyle.getBackground()))
                    .allMatch(sysMLViewFrameNodeStyle -> sysMLViewFrameNodeStyle.getBorderSize() == 1);

            var siriusWebApplicationNode = new DiagramNavigator(diagram).nodeWithLabel(AllCustomNodesProjectData.Labels.VIEW_FRAME).getNode();
            siriusWebApplicationNodeId.set(siriusWebApplicationNode.getId());
        });

        Runnable setNodeCustomAppearance = () -> {
            var appearanceInput = new SysMLViewFrameNodeAppearanceInput("red", null, 5, null, 5);

            var input = new EditSysMLViewFrameNodeAppearanceInput(
                    UUID.randomUUID(),
                    AllCustomNodesProjectData.EDITING_CONTEXT_ID.toString(),
                    diagramId.get(),
                    siriusWebApplicationNodeId.get(),
                    appearanceInput);

            this.editSysMLViewFrameNodeAppearanceMutationRunner.run(input);
        };

        Consumer<Object> updatedAfterCustomAppearanceDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            assertThat(diagram.getNodes())
                    .filteredOn(node -> node.getInsideLabel() != null && node.getInsideLabel().getText().equals(AllCustomNodesProjectData.Labels.VIEW_FRAME))
                    .hasSize(1)
                    .allMatch(node -> node.getStyle() instanceof SysMLViewFrameNodeStyle)
                    .extracting(node -> (SysMLViewFrameNodeStyle) node.getStyle())
                    .allMatch(sysMLViewFrameNodeStyle -> "red".equals(sysMLViewFrameNodeStyle.getBackground()))
                    .allMatch(sysMLViewFrameNodeStyle -> sysMLViewFrameNodeStyle.getBorderSize() == 5);
        });

        Runnable resetNodeCustomAppearance = () -> {
            var input = new ResetNodeAppearanceInput(
                    UUID.randomUUID(),
                    AllCustomNodesProjectData.EDITING_CONTEXT_ID.toString(),
                    diagramId.get(),
                    siriusWebApplicationNodeId.get(),
                    List.of("BORDER_SIZE"));

            this.resetNodeAppearanceMutationRunner.run(input);
        };

        Consumer<Object> updatedAfterResetCustomAppearanceDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            assertThat(diagram.getNodes())
                    .filteredOn(node -> node.getInsideLabel() != null && node.getInsideLabel().getText().equals(AllCustomNodesProjectData.Labels.VIEW_FRAME))
                    .hasSize(1)
                    .allMatch(node -> node.getStyle() instanceof SysMLViewFrameNodeStyle)
                    .extracting(node -> (SysMLViewFrameNodeStyle) node.getStyle())
                    .allMatch(sysMLViewFrameNodeStyle -> "red".equals(sysMLViewFrameNodeStyle.getBackground()))
                    .allMatch(sysMLViewFrameNodeStyle -> sysMLViewFrameNodeStyle.getBorderSize() == 1);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(setNodeCustomAppearance)
                .consumeNextWith(updatedAfterCustomAppearanceDiagramContentConsumer)
                .then(resetNodeCustomAppearance)
                .consumeNextWith(updatedAfterResetCustomAppearanceDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

}