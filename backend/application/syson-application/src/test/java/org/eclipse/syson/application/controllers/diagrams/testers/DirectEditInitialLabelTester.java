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

    /**
     * Checks the initial direct edit label on a bordered node's outside label.
     *
     * @param verifier
     *            the verifier to chain the check to
     * @param diagram
     *            the diagram reference
     * @param mainNodeId
     *            the id of the node holding the outside label
     * @param expectedLabel
     *            the expected initial label
     * @deprecated use {@link #checkDirectEditInitialLabelOnBorderedNode(AtomicReference, String, String)} instead.
     */
    @Deprecated
    public void checkDirectEditInitialLabelOnBorderedNode(Step<DiagramRefreshedEventPayload> verifier, AtomicReference<Diagram> diagram, String mainNodeId, String expectedLabel) {
        this.checkDirectEditInitialLabel(verifier, diagram, () -> {
            DiagramNavigator diagramNavigator = new DiagramNavigator(diagram.get());
            return diagramNavigator.nodeWithId(mainNodeId).getNode().getOutsideLabels().get(0).id();
        }, expectedLabel);
    }

    /**
     * Checks the initial direct edit label on a node's inside label.
     *
     * @param verifier
     *            the verifier to chain the check to
     * @param diagram
     *            the diagram reference
     * @param mainNodeId
     *            the id of the node holding the inside label
     * @param expectedLabel
     *            the expected initial label
     * @deprecated use {@link #checkDirectEditInitialLabelOnNode(AtomicReference, String, String)} instead.
     */
    @Deprecated
    public void checkDirectEditInitialLabelOnNode(Step<DiagramRefreshedEventPayload> verifier, AtomicReference<Diagram> diagram, String mainNodeId, String expectedLabel) {
        this.checkDirectEditInitialLabel(verifier, diagram, () -> {
            DiagramNavigator diagramNavigator = new DiagramNavigator(diagram.get());
            return diagramNavigator.nodeWithId(mainNodeId).getNode().getInsideLabel().getId();
        }, expectedLabel);
    }

    /**
     * Checks the initial direct edit label using a custom label ID supplier.
     *
     * @param verifier
     *            the verifier to chain the check to
     * @param diagram
     *            the diagram reference
     * @param labelId
     *            a supplier that provides the label ID
     * @param expectedLabel
     *            the expected initial label
     * @deprecated use {@link #checkDirectEditInitialLabel(AtomicReference, Supplier, String)} instead.
     */
    @Deprecated
    public void checkDirectEditInitialLabel(Step<DiagramRefreshedEventPayload> verifier, AtomicReference<Diagram> diagram, Supplier<String> labelId, String expectedLabel) {
        verifier.then(this.createInitialLabelCheckRunnable(diagram, labelId, expectedLabel));
    }

    /**
     * Creates a runnable that checks the initial direct edit label on a bordered node's outside label.
     *
     * @param diagram
     *            the diagram reference
     * @param mainNodeId
     *            the id of the node holding the outside label
     * @param expectedLabel
     *            the expected initial label
     * @return a runnable that performs the initial label check
     */
    public Runnable checkDirectEditInitialLabelOnBorderedNode(AtomicReference<Diagram> diagram, String mainNodeId, String expectedLabel) {
        return this.checkDirectEditInitialLabel(diagram, () -> {
            DiagramNavigator diagramNavigator = new DiagramNavigator(diagram.get());
            return diagramNavigator.nodeWithId(mainNodeId).getNode().getOutsideLabels().get(0).id();
        }, expectedLabel);
    }

    /**
     * Creates a runnable that checks the initial direct edit label on a node's inside label.
     *
     * @param diagram
     *            the diagram reference
     * @param mainNodeId
     *            the id of the node holding the inside label
     * @param expectedLabel
     *            the expected initial label
     * @return a runnable that performs the initial label check
     */
    public Runnable checkDirectEditInitialLabelOnNode(AtomicReference<Diagram> diagram, String mainNodeId, String expectedLabel) {
        return this.checkDirectEditInitialLabel(diagram, () -> {
            DiagramNavigator diagramNavigator = new DiagramNavigator(diagram.get());
            return diagramNavigator.nodeWithId(mainNodeId).getNode().getInsideLabel().getId();
        }, expectedLabel);
    }

    /**
     * Creates a runnable that checks the initial direct edit label using a custom label ID supplier.
     *
     * @param diagram
     *            the diagram reference
     * @param labelId
     *            a supplier that provides the label ID
     * @param expectedLabel
     *            the expected initial label
     * @return a runnable that performs the initial label check
     */
    public Runnable checkDirectEditInitialLabel(AtomicReference<Diagram> diagram, Supplier<String> labelId, String expectedLabel) {
        return this.createInitialLabelCheckRunnable(diagram, labelId, expectedLabel);
    }

    private Runnable createInitialLabelCheckRunnable(AtomicReference<Diagram> diagram, Supplier<String> labelId, String expectedLabel) {
        return () -> {
            Map<String, Object> variables = Map.of(
                    "editingContextId", this.editingContextId,
                    "diagramId", diagram.get().getId(),
                    "labelId", labelId.get());
            var initialDirectEditElementLabelResult = this.initialDirectEditElementLabelQueryRunner.run(variables);

            String initialDirectEditElementLabel = JsonPath.read(initialDirectEditElementLabelResult.data(), "$.data.viewer.editingContext.representation.description.initialDirectEditElementLabel");
            assertThat(initialDirectEditElementLabel).isEqualTo(expectedLabel);
        };
    }
}
