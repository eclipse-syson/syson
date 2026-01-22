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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariable;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariableType;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.GeneralViewEmptyTestProjectData;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Test the creation of a do action with referenced action in the General View diagram..
 *
 * @author pcdavid
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVStateDoActionWithReferencedActionTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private ToolTester toolTester;

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                GeneralViewEmptyTestProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        return flux;
    }

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a General View with a state and action, WHEN invoking do action with referenced action, THEN the PerformActionUsage is visible in the state")
    @GivenSysONServer({ GeneralViewEmptyTestProjectData.SCRIPT_PATH })
    @Test
    public void checkPerformActionRevealInState() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);

        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);
        var newStateToolId = diagramDescriptionIdProvider.getDiagramCreationToolId("New State");
        assertThat(newStateToolId).as("The tool 'New State' should exist on the diagram").isNotNull();
        var newActionToolId = diagramDescriptionIdProvider.getDiagramCreationToolId("New Action");
        assertThat(newActionToolId).as("The tool 'New Action' should exist on the diagram").isNotNull();
        var newDoActionWithReferencedActionToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getStateUsage()),
                "New Do Action with referenced Action");
        assertThat(newDoActionWithReferencedActionToolId).as("The tool 'New Do Action with referenced Action' should exist on State").isNotNull();

        var diagramReference = new AtomicReference<Diagram>(null);
        var stateNodeId = new AtomicReference<String>(null);
        var actionId = new AtomicReference<String>(null);

        Consumer<DiagramRefreshedEventPayload> initialDiagramContentConsumer = payload -> Optional.of(payload)
                .map(DiagramRefreshedEventPayload::diagram)
                .ifPresentOrElse(diagram -> {
                    diagramReference.set(diagram);
                }, () -> fail("Missing diagram"));

        Runnable createState = () -> this.toolTester.invokeTool(GeneralViewEmptyTestProjectData.EDITING_CONTEXT, diagramReference, newStateToolId);

        Consumer<DiagramRefreshedEventPayload> diagramWithStateConsumer = payload -> Optional.of(payload)
                .map(DiagramRefreshedEventPayload::diagram)
                .ifPresentOrElse(diagram -> {
                    assertThat(diagram.getNodes()).hasSize(1);
                    diagram.getNodes().stream()
                            .filter(node -> node.getTargetObjectLabel().equals("state1"))
                            .findFirst()
                            .ifPresentOrElse(node -> stateNodeId.set(node.getId()), () -> fail("Node 'state1' not found"));
                }, () -> fail("Missing diagram"));

        Runnable createAction = () -> this.toolTester.invokeTool(GeneralViewEmptyTestProjectData.EDITING_CONTEXT, diagramReference, newActionToolId);

        Consumer<DiagramRefreshedEventPayload> diagramWithStateAndActionConsumer = payload -> Optional.of(payload)
                .map(DiagramRefreshedEventPayload::diagram)
                .ifPresentOrElse(diagram -> {
                    assertThat(diagram.getNodes()).hasSize(2);
                    diagram.getNodes().stream()
                            .filter(node -> node.getTargetObjectLabel().equals("action1"))
                            .findFirst()
                            .ifPresentOrElse(node -> actionId.set(node.getTargetObjectId()), () -> fail("Node 'action1' not found"));
                }, () -> fail("Missing diagram"));

        Runnable createDoActionWithReference = () -> {
            this.toolTester.invokeTool(GeneralViewEmptyTestProjectData.EDITING_CONTEXT, diagramReference.get().getId(), stateNodeId.get(), newDoActionWithReferencedActionToolId,
                    List.of(new ToolVariable("selectedObject", actionId.get(), ToolVariableType.OBJECT_ID)));
        };

        Consumer<DiagramRefreshedEventPayload> diagramWithDoActionConsumer = payload -> Optional.of(payload)
                .map(DiagramRefreshedEventPayload::diagram)
                .ifPresentOrElse(diagram -> {
                    assertThat(diagram.getNodes()).hasSize(2);
                    assertThat(diagram.getEdges()).isEmpty();
                    DiagramNavigator navigator = new DiagramNavigator(diagram);
                    Collection<Node> visibleNodes = navigator.findAllNodes().stream().filter(node -> node.getState() == ViewModifier.Normal).toList();
                    assertThat(visibleNodes.stream().filter(node -> node.getInsideLabel().getText().equals("perform actions"))).hasSize(1).allMatch(node -> node.getChildNodes().size() == 1);
                    assertThat(visibleNodes.stream().filter(node -> node.getInsideLabel().getText().equals("ref do ::> action1"))).hasSize(1);
                }, () -> fail("Missing diagram"));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createState)
                .consumeNextWith(diagramWithStateConsumer)
                .then(createAction)
                .consumeNextWith(diagramWithStateAndActionConsumer)
                .then(createDoActionWithReference)
                .consumeNextWith(diagramWithDoActionConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
