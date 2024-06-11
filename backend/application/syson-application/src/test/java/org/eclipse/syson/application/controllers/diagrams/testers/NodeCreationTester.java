/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.sirius.components.collaborative.diagrams.dto.InvokeSingleClickOnDiagramElementToolInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.InvokeSingleClickOnDiagramElementToolSuccessPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
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
    private InvokeSingleClickOnDiagramElementToolMutationRunner invokeSingleClickOnDiagramElementToolMutationRunner;

    public void createNodeOnDiagram(String projectId, AtomicReference<Diagram> diagram, String toolId) {
        this.createNode(projectId, diagram, null, toolId);
    }

    public void createNode(String projectId, AtomicReference<Diagram> diagram, String parentNodeTargetObjectLabel, String toolId) {
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
                null);
        var createElementResult = this.invokeSingleClickOnDiagramElementToolMutationRunner.run(createElementInput);
        String typename = JsonPath.read(createElementResult, "$.data.invokeSingleClickOnDiagramElementTool.__typename");
        assertThat(typename).isEqualTo(InvokeSingleClickOnDiagramElementToolSuccessPayload.class.getSimpleName());
    }

}
