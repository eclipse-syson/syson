/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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

import org.eclipse.sirius.components.collaborative.diagrams.dto.EditLabelInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.EditLabelSuccessPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.InvokeSingleClickOnDiagramElementToolInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.InvokeSingleClickOnDiagramElementToolSuccessPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariable;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.tests.graphql.EditLabelMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.graphql.InvokeSingleClickOnDiagramElementToolMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.test.StepVerifier;

/**
 * Service class to test tool invocation.
 * <p>
 * This class should be used as part of a subscription verification (see
 * {@link StepVerifier#create(org.reactivestreams.Publisher)}).
 * </p>
 *
 * @author gdaniel
 */
@Service
public class ToolTester {

    @Autowired
    private EditLabelMutationRunner editLabelMutationRunner;

    @Autowired
    private InvokeSingleClickOnDiagramElementToolMutationRunner invokeSingleClickOnDiagramElementToolMutationRunner;

    public void invokeTool(String editingContextId, AtomicReference<Diagram> diagram, String toolId) {
        this.invokeTool(editingContextId, diagram, null, toolId);
    }

    public void invokeTool(String editingContextId, AtomicReference<Diagram> diagram, String selectedNodeTargetObjectLabel, String toolId) {
        this.invokeTool(editingContextId, diagram, selectedNodeTargetObjectLabel, toolId, List.of());
    }

    public void invokeTool(String editingContextId, AtomicReference<Diagram> diagram, String selectedNodeTargetObjectLabel, String toolId, List<ToolVariable> variables) {
        String diagramElementId = diagram.get().getId();
        if (selectedNodeTargetObjectLabel != null) {
            DiagramNavigator diagramNavigator = new DiagramNavigator(diagram.get());
            diagramElementId = diagramNavigator.nodeWithTargetObjectLabel(selectedNodeTargetObjectLabel).getNode().getId();
        }
        this.invokeTool(editingContextId, diagram.get().getId(), diagramElementId, toolId, variables);
    }

    public void invokeTool(String editingContextId, String diagramId, String diagramElementId, String toolId, List<ToolVariable> variables) {
        var createElementInput = new InvokeSingleClickOnDiagramElementToolInput(
                UUID.randomUUID(),
                editingContextId,
                diagramId,
                diagramElementId,
                toolId,
                0,
                0,
                variables);
        var createElementResult = this.invokeSingleClickOnDiagramElementToolMutationRunner.run(createElementInput);
        String typename = JsonPath.read(createElementResult, "$.data.invokeSingleClickOnDiagramElementTool.__typename");
        assertThat(typename).isEqualTo(InvokeSingleClickOnDiagramElementToolSuccessPayload.class.getSimpleName());
    }

    public void createNodeOnEdge(String editingContextId, AtomicReference<Diagram> diagram, String selectedEdgeTargetObjectLabel, String toolId) {
        this.createNodeOnEdge(editingContextId, diagram, selectedEdgeTargetObjectLabel, toolId, List.of());
    }

    public void createNodeOnEdge(String editingContextId, AtomicReference<Diagram> diagram, String selectedEdgeTargetObjectLabel, String toolId, List<ToolVariable> variables) {
        String diagramElementId = diagram.get().getId();
        if (selectedEdgeTargetObjectLabel != null) {
            DiagramNavigator diagramNavigator = new DiagramNavigator(diagram.get());
            diagramElementId = diagramNavigator.edgeWithTargetObjectLabel(selectedEdgeTargetObjectLabel).getEdge().getId();
        }
        var createElementInput = new InvokeSingleClickOnDiagramElementToolInput(
                UUID.randomUUID(),
                editingContextId,
                diagram.get().getId(),
                diagramElementId,
                toolId,
                0,
                0,
                variables);
        var createElementResult = this.invokeSingleClickOnDiagramElementToolMutationRunner.run(createElementInput);
        String typename = JsonPath.read(createElementResult, "$.data.invokeSingleClickOnDiagramElementTool.__typename");
        assertThat(typename).isEqualTo(InvokeSingleClickOnDiagramElementToolSuccessPayload.class.getSimpleName());
    }

    public void renameRootNode(String editingContextId, AtomicReference<Diagram> diagram, String nodeName, String newName) {
        Optional<Node> optionalNode = diagram.get().getNodes().stream().filter(n -> n.getTargetObjectLabel().equals(nodeName)).findFirst();

        assertThat(optionalNode).as("the node " + nodeName + " is not present in the diagram").isNotEmpty();

        var input = new EditLabelInput(UUID.randomUUID(), editingContextId, diagram.get().getId(), optionalNode.get().getInsideLabel().getId(), newName);
        var invokeSingleClickOnDiagramElementToolResult = this.editLabelMutationRunner.run(input);

        String invokeSingleClickOnDiagramElementToolResultTypename = JsonPath.read(invokeSingleClickOnDiagramElementToolResult, "$.data.editLabel.__typename");
        assertThat(invokeSingleClickOnDiagramElementToolResultTypename).isEqualTo(EditLabelSuccessPayload.class.getSimpleName());
    }

    public void renameNode(String editingContextId, AtomicReference<Diagram> diagram, String nodeName, String newName) {
        List<Node> nodes = new ArrayList<>();
        List<Node> rootNodes = diagram.get().getNodes();
        nodes.addAll(rootNodes);
        for (Node node : rootNodes) {
            List<Node> subNodes = node.getChildNodes();
            nodes.addAll(subNodes);
            for (Node subNode : subNodes) {
                nodes.addAll(subNode.getChildNodes());
            }
        }
        Optional<Node> optionalNode = nodes.stream().filter(n -> n.getTargetObjectLabel().equals(nodeName)).findFirst();

        assertThat(optionalNode).as("the node " + nodeName + " is not present in the diagram").isNotEmpty();

        var input = new EditLabelInput(UUID.randomUUID(), editingContextId, diagram.get().getId(), optionalNode.get().getInsideLabel().getId(), newName);
        var invokeSingleClickOnDiagramElementToolResult = this.editLabelMutationRunner.run(input);

        String invokeSingleClickOnDiagramElementToolResultTypename = JsonPath.read(invokeSingleClickOnDiagramElementToolResult, "$.data.editLabel.__typename");
        assertThat(invokeSingleClickOnDiagramElementToolResultTypename).isEqualTo(EditLabelSuccessPayload.class.getSimpleName());
    }
}
