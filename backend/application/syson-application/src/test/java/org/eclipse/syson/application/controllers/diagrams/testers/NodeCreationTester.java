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
 * Creates a node on a diagram.
 * <p>
 * This class should be used as part of a subscription verification (see
 * {@link StepVerifier#create(org.reactivestreams.Publisher)}).
 * </p>
 *
 * @author gdaniel
 */
@Service
public class NodeCreationTester {

    @Autowired
    private EditLabelMutationRunner editLabelMutationRunner;

    @Autowired
    private InvokeSingleClickOnDiagramElementToolMutationRunner invokeSingleClickOnDiagramElementToolMutationRunner;

    public void createNodeOnDiagram(String projectId, AtomicReference<Diagram> diagram, String toolId) {
        this.createNode(projectId, diagram, null, toolId);
    }

    public void createNode(String projectId, AtomicReference<Diagram> diagram, String parentNodeTargetObjectLabel, String toolId) {
        this.createNode(projectId, diagram, parentNodeTargetObjectLabel, toolId, List.of());
    }

    public void createNode(String projectId, AtomicReference<Diagram> diagram, String parentNodeTargetObjectLabel, String toolId, List<ToolVariable> variables) {
        String parentId = diagram.get().getId();
        if (parentNodeTargetObjectLabel != null) {
            DiagramNavigator diagramNavigator = new DiagramNavigator(diagram.get());
            parentId = diagramNavigator.nodeWithTargetObjectLabel(parentNodeTargetObjectLabel).getNode().getId();
        }
        var createElementInput = new InvokeSingleClickOnDiagramElementToolInput(
                UUID.randomUUID(),
                projectId,
                diagram.get().getId(),
                parentId,
                toolId,
                0,
                0,
                variables);
        var createElementResult = this.invokeSingleClickOnDiagramElementToolMutationRunner.run(createElementInput);
        String typename = JsonPath.read(createElementResult, "$.data.invokeSingleClickOnDiagramElementTool.__typename");
        assertThat(typename).isEqualTo(InvokeSingleClickOnDiagramElementToolSuccessPayload.class.getSimpleName());
    }

    public void renameRootNode(String projectId, AtomicReference<Diagram> diagram, String nodeName, String newName) {
        Optional<Node> optionalNode = diagram.get().getNodes().stream().filter(n -> n.getTargetObjectLabel().equals(nodeName)).findFirst();

        assertThat(optionalNode).as("the node " + nodeName + " is not present in the diagram").isNotEmpty();

        var input = new EditLabelInput(UUID.randomUUID(), projectId, diagram.get().getId(), optionalNode.get().getInsideLabel().getId(), newName);
        var invokeSingleClickOnDiagramElementToolResult = this.editLabelMutationRunner.run(input);

        String invokeSingleClickOnDiagramElementToolResultTypename = JsonPath.read(invokeSingleClickOnDiagramElementToolResult, "$.data.editLabel.__typename");
        assertThat(invokeSingleClickOnDiagramElementToolResultTypename).isEqualTo(EditLabelSuccessPayload.class.getSimpleName());
    }

    public void renameNode(String projectId, AtomicReference<Diagram> diagram, String nodeName, String newName) {
        List<Node> nodes = new ArrayList<>();
        List<Node> rootNodes = diagram.get().getNodes();
        nodes.addAll(rootNodes);
        for (Node node : rootNodes) {
            nodes.addAll(node.getChildNodes());
        }
        Optional<Node> optionalNode = nodes.stream().filter(n -> n.getTargetObjectLabel().equals(nodeName)).findFirst();

        assertThat(optionalNode).as("the node " + nodeName + " is not present in the diagram").isNotEmpty();

        var input = new EditLabelInput(UUID.randomUUID(), projectId, diagram.get().getId(), optionalNode.get().getInsideLabel().getId(), newName);
        var invokeSingleClickOnDiagramElementToolResult = this.editLabelMutationRunner.run(input);

        String invokeSingleClickOnDiagramElementToolResultTypename = JsonPath.read(invokeSingleClickOnDiagramElementToolResult, "$.data.editLabel.__typename");
        assertThat(invokeSingleClickOnDiagramElementToolResultTypename).isEqualTo(EditLabelSuccessPayload.class.getSimpleName());
    }
}
