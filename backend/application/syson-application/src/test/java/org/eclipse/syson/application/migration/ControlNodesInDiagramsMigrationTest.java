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

package org.eclipse.syson.application.migration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controller.editingcontext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.data.ControlNodesInDiagramsMigrationTestProjectData;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Test that old diagrams (GV and AFV) with control nodes (Decision, Fork, Join, Merge) open with the same visual content as before.
 * Due to the addition of new mappings for control nodes at the diagram level, when opening existing diagrams containing control nodes,
 * we want the top-level control nodes to remain hidden, as they were before.
 *
 * @author Jerome Gout
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControlNodesInDiagramsMigrationTest extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    private SemanticCheckerService semanticCheckerService;

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToGVDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(), ControlNodesInDiagramsMigrationTestProjectData.EDITING_CONTEXT_ID, ControlNodesInDiagramsMigrationTestProjectData.GraphicalIds.GV_DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToAFVDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(), ControlNodesInDiagramsMigrationTestProjectData.EDITING_CONTEXT_ID, ControlNodesInDiagramsMigrationTestProjectData.GraphicalIds.AFV_DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a project with a GV diagram containing control nodes, WHEN the model is loaded, THEN new extra control nodes are hidden")
    @GivenSysONServer({ ControlNodesInDiagramsMigrationTestProjectData.SCRIPT_PATH })
    @Test
    public void givenAProjectWithAGVDiagramContainingControlNodesWhenTheModelIsLoadedThenNewExtraControlNodesAreHidden() {
        var flux = this.givenSubscriptionToGVDiagram();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            // the diagram background contains 6 nodes, a visible root action, a sub action which is hidden and 4 hidden control nodes
            var rootNodes = diagram.getNodes();
            assertEquals(6, rootNodes.size());
            var rootActionNode = rootNodes.stream()
                    .filter(node -> ControlNodesInDiagramsMigrationTestProjectData.SemanticIds.ROOT_ACT_ID.equals(node.getTargetObjectId()))
                    .findFirst();
            var subActionNode = rootNodes.stream()
                    .filter(node -> ControlNodesInDiagramsMigrationTestProjectData.SemanticIds.SUB_ACT_ID.equals(node.getTargetObjectId()))
                    .findFirst();
            var subDecisionNode = rootNodes.stream()
                    .filter(node -> ControlNodesInDiagramsMigrationTestProjectData.SemanticIds.DECISION_NODE_DEC_ID.equals(node.getTargetObjectId()))
                    .findFirst();
            var subForkNode = rootNodes.stream()
                    .filter(node -> ControlNodesInDiagramsMigrationTestProjectData.SemanticIds.FORK_NODE_FOR_ID.equals(node.getTargetObjectId()))
                    .findFirst();
            var subJoinNode = rootNodes.stream()
                    .filter(node -> ControlNodesInDiagramsMigrationTestProjectData.SemanticIds.JOIN_NODE_JOI_ID.equals(node.getTargetObjectId()))
                    .findFirst();
            var subMergeNode = rootNodes.stream()
                    .filter(node -> ControlNodesInDiagramsMigrationTestProjectData.SemanticIds.MERGE_NODE_MER_ID.equals(node.getTargetObjectId()))
                    .findFirst();
            assertThat(rootActionNode).isPresent();
            assertThat(rootActionNode.get().getState()).isEqualTo(ViewModifier.Normal);
            assertThat(subActionNode).isPresent();
            assertThat(subActionNode.get().getState()).isEqualTo(ViewModifier.Hidden);
            assertThat(subDecisionNode).isPresent();
            assertThat(subDecisionNode.get().getState()).isEqualTo(ViewModifier.Hidden);
            assertThat(subForkNode).isPresent();
            assertThat(subForkNode.get().getState()).isEqualTo(ViewModifier.Hidden);
            assertThat(subJoinNode).isPresent();
            assertThat(subJoinNode.get().getState()).isEqualTo(ViewModifier.Hidden);
            assertThat(subMergeNode).isPresent();
            assertThat(subMergeNode.get().getState()).isEqualTo(ViewModifier.Hidden);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));

    }

    @DisplayName("GIVEN a project with a AFV diagram containing control nodes, WHEN the model is loaded, THEN new extra control nodes are hidden")
    @GivenSysONServer({ ControlNodesInDiagramsMigrationTestProjectData.SCRIPT_PATH })
    @Test
    public void givenAProjectWithAAFVDiagramContainingControlNodesWhenTheModelIsLoadedThenNewExtraControlNodesAreHidden() {
        var flux = this.givenSubscriptionToAFVDiagram();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            // the AFV diagram background contains 5 nodes, a visible root action and 4 control nodes which must be hidden
            var rootNodes = diagram.getNodes();
            assertEquals(5, rootNodes.size());
            var avfActionNode = rootNodes.stream()
                    .filter(node -> ControlNodesInDiagramsMigrationTestProjectData.SemanticIds.AVF_ACTION_ACT_ID.equals(node.getTargetObjectId()))
                    .findFirst();
            var afvDecisionNode = rootNodes.stream()
                    .filter(node -> ControlNodesInDiagramsMigrationTestProjectData.SemanticIds.AFV_DECISION_DEC_ID.equals(node.getTargetObjectId()))
                    .findFirst();
            var afvForkNode = rootNodes.stream()
                    .filter(node -> ControlNodesInDiagramsMigrationTestProjectData.SemanticIds.AFV_FORK_FOR_ID.equals(node.getTargetObjectId()))
                    .findFirst();
            var afvJoinNode = rootNodes.stream()
                    .filter(node -> ControlNodesInDiagramsMigrationTestProjectData.SemanticIds.AFV_JOIN_JOI_ID.equals(node.getTargetObjectId()))
                    .findFirst();
            var afvMergeNode = rootNodes.stream()
                    .filter(node -> ControlNodesInDiagramsMigrationTestProjectData.SemanticIds.AFV_MERGE_MER_ID.equals(node.getTargetObjectId()))
                    .findFirst();
            assertThat(avfActionNode).isPresent();
            assertThat(avfActionNode.get().getState()).isEqualTo(ViewModifier.Normal);
            assertThat(afvDecisionNode).isPresent();
            assertThat(afvDecisionNode.get().getState()).isEqualTo(ViewModifier.Hidden);
            assertThat(afvForkNode).isPresent();
            assertThat(afvForkNode.get().getState()).isEqualTo(ViewModifier.Hidden);
            assertThat(afvJoinNode).isPresent();
            assertThat(afvJoinNode.get().getState()).isEqualTo(ViewModifier.Hidden);
            assertThat(afvMergeNode).isPresent();
            assertThat(afvMergeNode.get().getState()).isEqualTo(ViewModifier.Hidden);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));

    }

}
