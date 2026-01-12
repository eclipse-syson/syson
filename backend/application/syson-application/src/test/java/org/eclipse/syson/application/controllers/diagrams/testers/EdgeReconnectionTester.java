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

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.sirius.components.collaborative.diagrams.dto.ReconnectEdgeInput;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.events.ReconnectEdgeKind;
import org.eclipse.sirius.components.diagrams.tests.graphql.ReconnectEdgeMutationRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Tester of edge reconnection actions.
 *
 * @author Arthur Daussy
 */
@Service
public class EdgeReconnectionTester {

    @Autowired
    private ReconnectEdgeMutationRunner reconnectEdgeMutationRunner;

    public void reconnectEdge(String projectId, AtomicReference<Diagram> diagram, String edgeId, String newEdgeEnd, ReconnectEdgeKind reconnectEdgeKind) {
        var reconnectEdgeInput = new ReconnectEdgeInput(
                UUID.randomUUID(),
                projectId,
                diagram.get().getId(),
                edgeId,
                newEdgeEnd,
                reconnectEdgeKind);
        var createEdgeResult = this.reconnectEdgeMutationRunner.run(reconnectEdgeInput);
        var typename = JsonPath.read(createEdgeResult.data(), "$.data.reconnectEdge.__typename");
        assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
    }

}
