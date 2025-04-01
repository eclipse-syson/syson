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
import static org.junit.jupiter.api.Assertions.fail;

import com.jayway.jsonpath.JsonPath;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.EditLabelInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.EditLabelSuccessPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.InsideLabel;
import org.eclipse.sirius.components.diagrams.tests.graphql.EditLabelMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;

import reactor.test.StepVerifier.Step;

/**
 * Tester that checks the execution of a DirectEdit tool.
 *
 * @author Arthur Daussy
 */
public class DirectEditTester {

    private final EditLabelMutationRunner editLabelMutationRunner;

    private final String editingContextId;

    public DirectEditTester(EditLabelMutationRunner editLabelMutationRunner, String editingContextId) {
        this.editLabelMutationRunner = Objects.requireNonNull(editLabelMutationRunner);
        this.editingContextId = Objects.requireNonNull(editingContextId);
    }

    public void checkDirectEdit(Step<DiagramRefreshedEventPayload> verifier, AtomicReference<Diagram> diagram, String mainNodeId, String inputLabel, String expectedNodeLabel) {
        Runnable requestDirectEdit = () -> {

            DiagramNavigator diagramNavigator = new DiagramNavigator(diagram.get());
            String labelId = diagramNavigator.nodeWithId(mainNodeId).getNode().getInsideLabel().getId();

            EditLabelInput input = new EditLabelInput(UUID.randomUUID(), this.editingContextId, diagram.get().getId(), labelId, inputLabel);
            var result = this.editLabelMutationRunner.run(input);

            String typename = JsonPath.read(result, "$.data.editLabel.__typename");
            assertThat(typename).isEqualTo(EditLabelSuccessPayload.class.getSimpleName());
        };

        Consumer<DiagramRefreshedEventPayload> diagramConsumer = payload -> Optional.of(payload)
                .map(DiagramRefreshedEventPayload::diagram)
                .ifPresentOrElse(newDiagram -> {
                    DiagramNavigator diagramNavigator = new DiagramNavigator(diagram.get());
                    InsideLabel newLabel = diagramNavigator.nodeWithId(mainNodeId).getNode().getInsideLabel();
                    assertThat(newLabel.getText()).isEqualTo(expectedNodeLabel);
                }, () -> fail("Missing diagram"));

        verifier.then(requestDirectEdit);
        verifier.consumeNextWith(diagramConsumer);
    }
}
