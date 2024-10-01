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
        var createEdgeInput = new InvokeSingleClickOnTwoDiagramElementsToolInput(
                UUID.randomUUID(),
                projectId,
                diagram.get().getId(),
                sourceId,
                targetId,
                0,
                0,
                0,
                0,
                toolId);
        var createEdgeResult = this.invokeSingleClickOnTwoDiagramElementsToolMutationRunner.run(createEdgeInput);
        String typename = JsonPath.read(createEdgeResult, "$.data.invokeSingleClickOnTwoDiagramElementsTool.__typename");
        assertThat(typename).isEqualTo(InvokeSingleClickOnTwoDiagramElementsToolSuccessPayload.class.getSimpleName());
    }


}
