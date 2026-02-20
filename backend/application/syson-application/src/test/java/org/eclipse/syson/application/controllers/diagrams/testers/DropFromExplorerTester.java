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

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DropOnDiagramInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DropOnDiagramSuccessPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.graphql.tests.api.GraphQLResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.test.StepVerifier;

/**
 * Drops an element from the explorer on a diagram.
 * <p>
 * This class should be used as part of a subscription verification (see
 * {@link StepVerifier#create(org.reactivestreams.Publisher)}).
 * </p>
 *
 * @author gdaniel
 */
@Service
public class DropFromExplorerTester {

    @Autowired
    private DropOnDiagramWithMessagesMutationRunner dropOnDiagramMutationRunner;

    public void dropFromExplorerOnDiagram(String projectId, AtomicReference<Diagram> diagram, String semanticElementIdToDnd) {
        this.dropFromExplorer(projectId, diagram, null, semanticElementIdToDnd);
    }

    public void dropFromExplorerOnDiagramElement(String projectId, AtomicReference<Diagram> diagram, String semanticElementIdToDnd, String targetNodeId) {
        this.dropFromExplorer(projectId, diagram, targetNodeId, semanticElementIdToDnd);
    }

    public GraphQLResult dropFromExplorer(String projectId, AtomicReference<Diagram> diagram, String targetNodeId, String semanticElementId) {
        DiagramNavigator diagramNavigator = new DiagramNavigator(diagram.get());
        final String targetId;
        if (targetNodeId == null) {
            targetId = diagram.get().getId();
        } else {
            targetId = diagramNavigator.nodeWithId(targetNodeId).getNode().getId();
        }
        var dropOnDiagramInput = new DropOnDiagramInput(
                UUID.randomUUID(),
                projectId,
                diagram.get().getId(),
                targetId,
                List.of(semanticElementId),
                0,
                0);
        var dropOnDiagramResult = this.dropOnDiagramMutationRunner.run(dropOnDiagramInput);
        var typename = JsonPath.read(dropOnDiagramResult.data(), "$.data.dropOnDiagram.__typename");
        assertThat(typename).isEqualTo(DropOnDiagramSuccessPayload.class.getSimpleName());
        return dropOnDiagramResult;
    }
}
