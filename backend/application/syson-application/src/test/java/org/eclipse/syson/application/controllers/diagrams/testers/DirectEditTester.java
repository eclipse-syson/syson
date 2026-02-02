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

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.eclipse.sirius.components.collaborative.diagrams.dto.EditLabelInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.EditLabelSuccessPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.InsideLabel;
import org.eclipse.sirius.components.diagrams.OutsideLabel;
import org.eclipse.sirius.components.diagrams.tests.graphql.EditLabelMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;

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
