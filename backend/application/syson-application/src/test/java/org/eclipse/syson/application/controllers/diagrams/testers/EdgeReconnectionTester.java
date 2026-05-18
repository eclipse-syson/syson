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

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.sirius.components.collaborative.diagrams.dto.ReconnectEdgeInput;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.events.ReconnectEdgeKind;
import org.eclipse.sirius.components.diagrams.tests.graphql.ReconnectEdgeMutationRunner;
import org.eclipse.sirius.components.representations.Message;
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

    public List<Message> reconnectEdge(String projectId, AtomicReference<Diagram> diagram, String edgeId, String newEdgeEnd, ReconnectEdgeKind reconnectEdgeKind) {
        var reconnectEdgeInput = new ReconnectEdgeInput(
                UUID.randomUUID(),
                projectId,
                diagram.get().getId(),
                edgeId,
                newEdgeEnd,
                reconnectEdgeKind,
                0,
                0);
        var createEdgeResult = this.reconnectEdgeMutationRunner.run(reconnectEdgeInput);
        var typename = JsonPath.read(createEdgeResult.data(), "$.data.reconnectEdge.__typename");
        assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());

        // Return feedback messages
        Configuration conf = Configuration.builder()
                .jsonProvider(new JacksonJsonProvider())
                .mappingProvider(new JacksonMappingProvider())
                .build();
        DocumentContext ctx = JsonPath.using(conf).parse(createEdgeResult.data());
        return ctx.read("$.data.reconnectEdge.messages", new TypeRef<List<Message>>() {
        });

    }

}
