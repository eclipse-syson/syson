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

package org.eclipse.syson.application.controllers.diagrams.actionflow.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckNodeOnDiagram;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.ActionFlowViewInsideActionUsageEmptyTestProjectData;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Tests the creation of control nodes (Decision, Fork, Join and Merge) as well as Start and Done actions in a Action Flow view diagram background when it is contained by an ActionUsage.
 *
 * @author Jerome Gout
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AFVAddTopControlNodeActionTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private ToolTester toolTester;

    @Autowired
    private DiagramComparator diagramComparator;

    private final SDVDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private static Stream<Arguments> topControlNodeParameters() {
        return Stream.of(
                Arguments.of("New Start Action", "StartAction"),
                Arguments.of("New Done Action", "DoneAction"),
                Arguments.of("New Decision", SysmlPackage.eINSTANCE.getDecisionNode().getName()),
                Arguments.of("New Fork", SysmlPackage.eINSTANCE.getForkNode().getName()),
                Arguments.of("New Join", SysmlPackage.eINSTANCE.getJoinNode().getName()),
                Arguments.of("New Merge", SysmlPackage.eINSTANCE.getMergeNode().getName())
        );
    }

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                ActionFlowViewInsideActionUsageEmptyTestProjectData.EDITING_CONTEXT_ID,
                ActionFlowViewInsideActionUsageEmptyTestProjectData.GraphicalIds.AFV_IN_ACTION_DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN an ActionFlow View diagram, WHEN control node creation tool is requested on digram background, THEN a new control node is created")
    @GivenSysONServer({ ActionFlowViewInsideActionUsageEmptyTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("topControlNodeParameters")
    public void givenAnAFVDiagramWhenControlNodeCreationToolIsRequestedOnDiagramBackgroundThenANewControlNodeIsCreated(String toolLabel, String nodeName) {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(ActionFlowViewInsideActionUsageEmptyTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        String creationToolId = diagramDescriptionIdProvider.getDiagramCreationToolId(toolLabel);
        assertThat(creationToolId).as("The tool '" + toolLabel + "' should exist on diagram background").isNotNull();

        var diagram = new AtomicReference<Diagram>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable createNodeRunnable = () -> {
            this.toolTester.invokeTool(ActionFlowViewInsideActionUsageEmptyTestProjectData.EDITING_CONTEXT_ID, diagram, null, creationToolId);
        };

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(0)
                    .hasNewNodeCount(1)
                    .check(initialDiagram, newDiagram);

            new CheckNodeOnDiagram(diagramDescriptionIdProvider, this.diagramComparator)
                    .hasNodeDescriptionName(this.descriptionNameGenerator.getNodeName(nodeName))
                    .check(initialDiagram, newDiagram);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
