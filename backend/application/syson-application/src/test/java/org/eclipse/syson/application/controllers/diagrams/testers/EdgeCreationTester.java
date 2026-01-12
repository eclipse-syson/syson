/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
package org.eclipse.syson.application.controllers.diagrams.testers;

import static org.assertj.core.api.Assertions.assertThat;

import com.jayway.jsonpath.JsonPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.sirius.components.collaborative.diagrams.dto.InvokeSingleClickOnTwoDiagramElementsToolInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.InvokeSingleClickOnTwoDiagramElementsToolSuccessPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.tests.graphql.InvokeSingleClickOnTwoDiagramElementsToolMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.test.StepVerifier;

/**
 * Creates an edge on a diagram.
 * <p>
 * This class should be used as part of a subscription verification (see
 * {@link StepVerifier#create(org.reactivestreams.Publisher)}).
 * </p>
 *
 * @author gdaniel
 */
@Service
public class EdgeCreationTester {

    @Autowired
    private InvokeSingleClickOnTwoDiagramElementsToolMutationRunner invokeSingleClickOnTwoDiagramElementsToolMutationRunner;

    public void createEdge(String projectId, AtomicReference<Diagram> diagram, String sourceNodeTargetObjectLabel, String targetNodeTargetObjectLabel, String toolId) {
        DiagramNavigator diagramNavigator = new DiagramNavigator(diagram.get());
        String sourceId = diagramNavigator.nodeWithTargetObjectLabel(sourceNodeTargetObjectLabel).getNode().getId();
        String targetId = diagramNavigator.nodeWithTargetObjectLabel(targetNodeTargetObjectLabel).getNode().getId();
        this.createEdgeUsingNodeId(projectId, diagram, sourceId, targetId, toolId);
    }

    public void createEdgeUsingNodeId(String projectId, AtomicReference<Diagram> diagram, String sourceNodeId, String targetNodeId, String toolId) {
        this.createEdgeUsingNodeId(projectId, diagram.get().getId(), sourceNodeId, targetNodeId, toolId, Optional.empty());
    }

    public void createEdgeUsingNodeId(String projectId, String diagramId, String sourceNodeId, String targetNodeId, String toolId) {
        this.createEdgeUsingNodeId(projectId, diagramId, sourceNodeId, targetNodeId, toolId, Optional.empty());
    }

    /**
     * Runs a SingleClickOnTwoDiagramElementsTool.
     *
     * @param editingContextId
     *            the editing context id
     * @param diagram
     *            the current diagram
     * @param sourceNodeId
     *            the source node id
     * @param targetNodeId
     *            the target node id
     * @param toolId
     *            the tool id
     * @param expectedMessages
     *            a list of expected messages shown to the user
     */
    public void runSingleClickOnTwoDiagramElementsTool(String editingContextId, AtomicReference<Diagram> diagram, String sourceNodeId, String targetNodeId, String toolId, List<String> expectedMessages) {
        this.createEdgeUsingNodeId(editingContextId, diagram.get().getId(), sourceNodeId, targetNodeId, toolId, Optional.of(expectedMessages));
    }

    private void createEdgeUsingNodeId(String editingContextId, String diagramId, String sourceNodeId, String targetNodeId, String toolId, Optional<List<String>> expectedMessages) {
        var createEdgeInput = new InvokeSingleClickOnTwoDiagramElementsToolInput(
                UUID.randomUUID(),
                editingContextId,
                diagramId,
                sourceNodeId,
                targetNodeId,
                0,
                0,
                0,
                0,
                toolId,
                new ArrayList<>());
        var createEdgeResult = this.invokeSingleClickOnTwoDiagramElementsToolMutationRunner.run(createEdgeInput);
        String typename = JsonPath.read(createEdgeResult.data(), "$.data.invokeSingleClickOnTwoDiagramElementsTool.__typename");
        assertThat(typename).isEqualTo(InvokeSingleClickOnTwoDiagramElementsToolSuccessPayload.class.getSimpleName());

        // If some messages have been provided then checks them
        if (expectedMessages.isPresent()) {
            List<String> expectedMsg = expectedMessages.get();
            for (int i = 0; i < expectedMsg.size(); i++) {
                String expMg = expectedMsg.get(i);
                String messageText = JsonPath.read(createEdgeResult.data(), "$.data.invokeSingleClickOnTwoDiagramElementsTool.messages[" + i + "].body");
                assertThat(messageText).isEqualTo(expMg);
            }

        }
    }

}
