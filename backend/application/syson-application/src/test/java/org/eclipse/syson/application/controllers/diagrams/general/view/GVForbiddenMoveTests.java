/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
package org.eclipse.syson.application.controllers.diagrams.general.view;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DropNodesInput;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.events.ReconnectEdgeKind;
import org.eclipse.sirius.components.diagrams.layoutdata.Position;
import org.eclipse.sirius.components.graphql.tests.api.GraphQLResult;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.testers.DropNodesWithMessageMutationRunner;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeReconnectionTester;
import org.eclipse.syson.application.data.GVWithReadOnlyNodesProjectData;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Tests the feedback messages returned after forbidden move or edge reconnection resulting in a move operation.
 *
 * @author Arthur Daussy
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVForbiddenMoveTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private EdgeReconnectionTester edgeReconnectionTester;

    @Autowired
    private DropNodesWithMessageMutationRunner dropRunner;

    @Autowired
    private DiagramComparator diagramComparator;

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN composite edge, WHEN when reconnecting to a read only source, THEN no new edge is created and a feedback message is reported")
    @GivenSysONServer({ GVWithReadOnlyNodesProjectData.SCRIPT_PATH })
    @Test
    public void reconnectCompositeEdgeSourceToReadOnlyObject() {
        var flux = this.givenSubscriptionToDiagram(GVWithReadOnlyNodesProjectData.EDITING_CONTEXT_ID);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable reconnectEdgeRunnable = this.buildReconnectRunnable(
                GVWithReadOnlyNodesProjectData.GraphicalIds.PART_ID,
                ReconnectEdgeKind.SOURCE,
                diagram,
                List.of(new Message("Unable to move part2 in Part: Unable to move a Element to a read only Element", MessageLevel.WARNING)));

        Consumer<Object> newEdgeConsumer = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(0)
                    .check(diagram.get(), newDiagram, true);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(reconnectEdgeRunnable)
                .consumeNextWith(newEdgeConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));

    }

    @DisplayName("GIVEN a part and a read only package, WHEN when dropping the part on the read only package, THEN the drop should be prevented and a feedback message should be sent")
    @GivenSysONServer({ GVWithReadOnlyNodesProjectData.SCRIPT_PATH })
    @Test
    public void moveElementToReadOnlyElement() {
        var flux = this.givenSubscriptionToDiagram(GVWithReadOnlyNodesProjectData.EDITING_CONTEXT_ID);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable dropPartOnPartsRunnable = () -> {
            GraphQLResult response = this.dropRunner.run(new DropNodesInput(UUID.randomUUID(),
                    GVWithReadOnlyNodesProjectData.EDITING_CONTEXT_ID,
                    GVWithReadOnlyNodesProjectData.GraphicalIds.DIAGRAM_ID,
                    List.of(GVWithReadOnlyNodesProjectData.GraphicalIds.PART1_ID),
                    GVWithReadOnlyNodesProjectData.GraphicalIds.PARTS_PKG_ID,
                    List.of(new Position(0, 0))));

            assertThat(this.readDropNodesMessages(response.data()))
                    .isEqualTo(List.of(new Message("Unable to move part1 in Parts: Unable to move a Element to a read only Element", MessageLevel.WARNING)));
        };
        Consumer<Object> newEdgeConsumer = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(0)
                    .hasNewNodeCount(0)
                    .check(diagram.get(), newDiagram, true);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(dropPartOnPartsRunnable)
                .consumeNextWith(newEdgeConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));

    }

    private List<Message> readDropNodesMessages(String responseData) {
        Configuration conf = Configuration.builder()
                .jsonProvider(new JacksonJsonProvider())
                .mappingProvider(new JacksonMappingProvider())
                .build();
        DocumentContext ctx = JsonPath.using(conf).parse(responseData);
        return ctx.read("$.data.dropNodes.messages", new TypeRef<List<Message>>() {
        });
    }

    private Runnable buildReconnectRunnable(String newTarget, ReconnectEdgeKind reconnectionKind, AtomicReference<Diagram> diagram, List<Message> expectedMessages) {
        return () ->
                assertThat(
                        this.edgeReconnectionTester.reconnectEdge(GVWithReadOnlyNodesProjectData.EDITING_CONTEXT_ID,
                                diagram,
                                GVWithReadOnlyNodesProjectData.GraphicalIds.NESTED_USAGE_EDGE,
                                newTarget,
                                reconnectionKind)).isEqualTo(expectedMessages);
    }

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram(String editingContextId) {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(), editingContextId, GVWithReadOnlyNodesProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }
}
