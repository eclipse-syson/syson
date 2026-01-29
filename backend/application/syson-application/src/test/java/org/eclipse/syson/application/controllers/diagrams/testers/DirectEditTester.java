/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
import java.util.function.Function;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.EditLabelInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.EditLabelSuccessPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.InsideLabel;
import org.eclipse.sirius.components.diagrams.Label;
import org.eclipse.sirius.components.diagrams.OutsideLabel;
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

    /**
     * Run a direct edit and check the resulting label for an {@link InsideLabel}.
     *
     * @param verifier
     *            the verifier
     * @param diagram
     *            the diagram
     * @param mainNodeId
     *            the id of the node holding the inside label
     * @param inputLabel
     *            the input label to be used for the direct edit
     * @param expectedInsideLabel
     *            the expected inside label
     * @deprecated use {@link #directEditInsideLabel(AtomicReference, String, String)} instead.
     */
    @Deprecated
    public void checkDirectEditInsideLabel(Step<DiagramRefreshedEventPayload> verifier, AtomicReference<Diagram> diagram, String mainNodeId, String inputLabel, String expectedInsideLabel) {
        Consumer<DiagramRefreshedEventPayload> checker = payload -> Optional.of(payload)
                .map(DiagramRefreshedEventPayload::diagram)
                .ifPresentOrElse(newDiagram -> {
                    DiagramNavigator diagramNavigator = new DiagramNavigator(newDiagram);

                    InsideLabel newLabel = diagramNavigator.nodeWithId(mainNodeId).getNode().getInsideLabel();
                    assertThat(newLabel.getText()).isEqualTo(expectedInsideLabel);
                }, () -> fail("Missing diagram"));

        this.checkDirectEdit(verifier, diagram, inputLabel, d -> this.getInsideLabelId(mainNodeId, d), checker);
    }

    /**
     * Run a direct edit and check the resulting label for an {@link OutsideLabel} (only check the first one available).
     *
     * @param verifier
     *            the verifier
     * @param diagram
     *            the diagram
     * @param mainNodeId
     *            the id of the node holding the outside label
     * @param inputLabel
     *            the input label to be used for the direct edit
     * @param expectedOutsideLabel
     *            the expected outside label
     * @deprecated use {@link #directEditOutsideLabel(AtomicReference, String, String)} instead.
     */
    @Deprecated
    public void checkDirectEditOutsideLabel(Step<DiagramRefreshedEventPayload> verifier, AtomicReference<Diagram> diagram, String mainNodeId, String inputLabel, String expectedOutsideLabel) {
        Consumer<DiagramRefreshedEventPayload> checker = payload -> Optional.of(payload)
                .map(DiagramRefreshedEventPayload::diagram)
                .ifPresentOrElse(newDiagram -> {
                    DiagramNavigator diagramNavigator = new DiagramNavigator(newDiagram);

                    OutsideLabel newLabel = diagramNavigator.nodeWithId(mainNodeId).getNode().getOutsideLabels().get(0);
                    assertThat(newLabel.text()).isEqualTo(expectedOutsideLabel);
                }, () -> fail("Missing diagram"));

        this.checkDirectEdit(verifier, diagram, inputLabel, d -> this.getOutsideLabelId(mainNodeId, d), checker);
    }

    /**
     * Runs a direct edit and checks the resulting label for an edge centered label.
     *
     * @param verifier
     *            the verifier
     * @param diagram
     *            the diagram
     * @param edgeId
     *            the id of the edge holding the centered label
     * @param inputLabel
     *            the input label to be used for the direct edit
     * @param expectedCenteredLabel
     *            the expected centered label
     * @deprecated use {@link #directEditCenteredEdgeLabel(AtomicReference, String, String)} instead.
     */
    @Deprecated
    public void checkDirectEditCenteredEdgeLabel(Step<DiagramRefreshedEventPayload> verifier, AtomicReference<Diagram> diagram, String edgeId, String inputLabel,
            String expectedCenteredLabel) {
        Consumer<DiagramRefreshedEventPayload> checker = payload -> Optional.of(payload)
                .map(DiagramRefreshedEventPayload::diagram)
                .ifPresentOrElse(newDiagram -> {
                    DiagramNavigator diagramNavigator = new DiagramNavigator(newDiagram);

                    Label newLabel = diagramNavigator.edgeWithId(edgeId).getEdge().getCenterLabel();
                    assertThat(newLabel.text()).isEqualTo(expectedCenteredLabel);
                }, () -> fail("Missing diagram"));
        this.checkDirectEdit(verifier, diagram, inputLabel, d -> this.getCenteredLabelId(edgeId, d), checker);
    }

    /**
     * Run a direct edit and check the resulting label for an {@link InsideLabel} (only check the first one available).
     *
     * @param verifier
     *            the verifier
     * @param diagram
     *            the diagram
     * @param mainNodeId
     *            the id of the node holding the inside label
     * @param inputLabel
     *            the input label to be used for the direct edit
     * @param checker
     *            a check to be done after the diagram re-render
     * @deprecated use {@link #directEditInsideLabel(AtomicReference, String, String)} instead.
     */
    @Deprecated
    public void checkDirectEditInsideLabel(Step<DiagramRefreshedEventPayload> verifier, AtomicReference<Diagram> diagram, String mainNodeId, String inputLabel,
            Consumer<DiagramRefreshedEventPayload> checker) {
        this.checkDirectEdit(verifier, diagram, inputLabel, d -> this.getInsideLabelId(mainNodeId, d), checker);
    }

    /**
     * Run a direct edit and check the resulting label for an {@link OutsideLabel} (only check the first one available).
     *
     * @param verifier
     *            the verifier
     * @param diagram
     *            the diagram
     * @param mainNodeId
     *            the id of the node holding the outside label
     * @param inputLabel
     *            the input label to be used for the direct edit
     * @param checker
     *            a check to be done after the diagram re-render
     * @deprecated use {@link #directEditOutsideLabel(AtomicReference, String, String)} instead.
     */
    @Deprecated
    public void checkDirectEditOutsideLabel(Step<DiagramRefreshedEventPayload> verifier, AtomicReference<Diagram> diagram, String mainNodeId, String inputLabel,
            Consumer<DiagramRefreshedEventPayload> checker) {
        this.checkDirectEdit(verifier, diagram, inputLabel, d -> this.getOutsideLabelId(mainNodeId, d), checker);
    }

    /**
     * Creates a runnable that executes a direct edit on an {@link InsideLabel}.
     *
     * @param diagram
     *            the diagram reference
     * @param mainNodeId
     *            the id of the node holding the inside label
     * @param inputLabel
     *            the input label to be used for the direct edit
     * @return a runnable that performs the direct edit mutation
     */
    public Runnable directEditInsideLabel(AtomicReference<Diagram> diagram, String mainNodeId, String inputLabel) {
        return this.createDirectEditRunnable(diagram, inputLabel, d -> this.getInsideLabelId(mainNodeId, d));
    }

    /**
     * Creates a runnable that executes a direct edit on an {@link OutsideLabel}.
     *
     * @param diagram
     *            the diagram reference
     * @param mainNodeId
     *            the id of the node holding the outside label
     * @param inputLabel
     *            the input label to be used for the direct edit
     * @return a runnable that performs the direct edit mutation
     */
    public Runnable directEditOutsideLabel(AtomicReference<Diagram> diagram, String mainNodeId, String inputLabel) {
        return this.createDirectEditRunnable(diagram, inputLabel, d -> this.getOutsideLabelId(mainNodeId, d));
    }

    /**
     * Creates a runnable that executes a direct edit on an edge centered label.
     *
     * @param diagram
     *            the diagram reference
     * @param edgeId
     *            the id of the edge holding the centered label
     * @param inputLabel
     *            the input label to be used for the direct edit
     * @return a runnable that performs the direct edit mutation
     */
    public Runnable directEditCenteredEdgeLabel(AtomicReference<Diagram> diagram, String edgeId, String inputLabel) {
        return this.createDirectEditRunnable(diagram, inputLabel, d -> this.getCenteredLabelId(edgeId, d));
    }

    private Runnable createDirectEditRunnable(AtomicReference<Diagram> diagram, String inputLabel, Function<Diagram, String> labelIdProvider) {
        return () -> {
            final String labelId = labelIdProvider.apply(diagram.get());
            EditLabelInput input = new EditLabelInput(UUID.randomUUID(), this.editingContextId, diagram.get().getId(), labelId, inputLabel);
            var result = this.editLabelMutationRunner.run(input);
            String typename = JsonPath.read(result.data(), "$.data.editLabel.__typename");
            assertThat(typename).isEqualTo(EditLabelSuccessPayload.class.getSimpleName());
        };
    }

    /**
     * Executes a direct edit mutation and chains the result verification to the provided verifier.
     *
     * @param verifier
     *            the {@link Step} verifier to chain the direct edit execution to
     * @param diagram
     *            the diagram reference containing the label to edit
     * @param inputLabel
     *            the new label value to set via direct edit
     * @param labelIdProvider
     *            a function that retrieves the label ID from the diagram
     * @param diagramConsumer
     *            a consumer to verify the diagram after the direct edit completes
     * @deprecated use {@link #createDirectEditRunnable(AtomicReference, String, Function)} instead.
     */
    @Deprecated
    private void checkDirectEdit(Step<DiagramRefreshedEventPayload> verifier, AtomicReference<Diagram> diagram, String inputLabel, Function<Diagram, String> labelIdProvider,
            Consumer<DiagramRefreshedEventPayload> diagramConsumer) {
        Runnable requestDirectEdit = () -> {

            final String labelId = labelIdProvider.apply(diagram.get());

            EditLabelInput input = new EditLabelInput(UUID.randomUUID(), this.editingContextId, diagram.get().getId(), labelId, inputLabel);
            var result = this.editLabelMutationRunner.run(input);

            String typename = JsonPath.read(result.data(), "$.data.editLabel.__typename");
            assertThat(typename).isEqualTo(EditLabelSuccessPayload.class.getSimpleName());
        };

        verifier.then(requestDirectEdit);
        verifier.consumeNextWith(diagramConsumer);
    }

    private String getInsideLabelId(String mainNodeId, Diagram d) {
        DiagramNavigator diagramNavigator = new DiagramNavigator(d);
        return diagramNavigator.nodeWithId(mainNodeId).getNode().getInsideLabel().getId();
    }

    private String getOutsideLabelId(String mainNodeId, Diagram d) {
        DiagramNavigator diagramNavigator = new DiagramNavigator(d);
        return diagramNavigator.nodeWithId(mainNodeId).getNode().getOutsideLabels().get(0).id();
    }

    private String getCenteredLabelId(String edgeId, Diagram d) {
        DiagramNavigator diagramNavigator = new DiagramNavigator(d);
        return diagramNavigator.edgeWithId(edgeId).getEdge().getCenterLabel().id();
    }

}
