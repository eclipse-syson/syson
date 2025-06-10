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
package org.eclipse.syson.application.controllers.diagrams.testers;

import static org.assertj.core.api.Assertions.assertThat;

import com.jayway.jsonpath.JsonPath;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.tests.graphql.InitialDirectEditElementLabelQueryRunner;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;

import reactor.test.StepVerifier.Step;

/**
 * Tester that checks the initial label used as input of the DirectEdit tool.
 *
 * @author Arthur Daussy
 */
public class DirectEditInitialLabelTester {

    private final InitialDirectEditElementLabelQueryRunner initialDirectEditElementLabelQueryRunner;

    private final String editingContextId;

    public DirectEditInitialLabelTester(InitialDirectEditElementLabelQueryRunner initialDirectEditElementLabelQueryRunner, String editingContextId) {
        this.initialDirectEditElementLabelQueryRunner = Objects.requireNonNull(initialDirectEditElementLabelQueryRunner);
        this.editingContextId = Objects.requireNonNull(editingContextId);
    }
    
    public void checkDirectEditInitialLabelOnBorderedNode(Step<DiagramRefreshedEventPayload> verifier, AtomicReference<Diagram> diagram, String mainNodeId, String expectedLabel) {
        this.checkDirectEditInitialLabel(verifier, diagram, () -> {
            DiagramNavigator diagramNavigator = new DiagramNavigator(diagram.get());
            return diagramNavigator.nodeWithId(mainNodeId).getNode().getOutsideLabels().get(0).id();
        }, expectedLabel);
    }

    public void checkDirectEditInitialLabelOnNode(Step<DiagramRefreshedEventPayload> verifier, AtomicReference<Diagram> diagram, String mainNodeId, String expectedLabel) {
        this.checkDirectEditInitialLabel(verifier, diagram, () -> {
            DiagramNavigator diagramNavigator = new DiagramNavigator(diagram.get());
            return diagramNavigator.nodeWithId(mainNodeId).getNode().getInsideLabel().getId();
        }, expectedLabel);
    }

    public void checkDirectEditInitialLabel(Step<DiagramRefreshedEventPayload> verifier, AtomicReference<Diagram> diagram, Supplier<String> labelId, String expectedLabel) {
        Runnable requestInitialLabel = () -> {
            Map<String, Object> variables = Map.of(
                    "editingContextId", this.editingContextId,
                    "diagramId", diagram.get().getId(),
                    "labelId", labelId.get());
            var initialDirectEditElementLabelResult = this.initialDirectEditElementLabelQueryRunner.run(variables);

            String initialDirectEditElementLabel = JsonPath.read(initialDirectEditElementLabelResult, "$.data.viewer.editingContext.representation.description.initialDirectEditElementLabel");
            assertThat(initialDirectEditElementLabel).isEqualTo(expectedLabel);
        };

        verifier.then(requestInitialLabel);
    }
}
