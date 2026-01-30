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

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DeleteFromDiagramInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DeleteFromDiagramSuccessPayload;

/**
 * Execute a "Delete" tool (either delete from model or delete from diagram).
 *
 * @author Arthur Daussy
 */
public class DeleteToolTester {

    private final DeleteToolRunner runner;

    private final String editingContextId;

    private final String representationId;

    public DeleteToolTester(DeleteToolRunner runner, String editingContextId, String representationId) {
        this.runner = Objects.requireNonNull(runner);
        this.editingContextId = Objects.requireNonNull(editingContextId);
        this.representationId = Objects.requireNonNull(representationId);
    }

    public Runnable checkDeleteTool(List<String> nodeIds, List<String> edgeIds) {
        return () -> {

            DeleteFromDiagramInput input = new DeleteFromDiagramInput(UUID.randomUUID(), this.editingContextId, this.representationId, nodeIds, edgeIds);
            var result = this.runner.run(input);

            String typename = JsonPath.read(result.data(), "$.data.deleteFromDiagram.__typename");
            assertThat(typename).isEqualTo(DeleteFromDiagramSuccessPayload.class.getSimpleName());
        };
    }

}
