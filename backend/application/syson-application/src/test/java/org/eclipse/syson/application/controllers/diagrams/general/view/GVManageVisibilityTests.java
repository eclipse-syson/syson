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
package org.eclipse.syson.application.controllers.diagrams.general.view;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.managevisibility.InvokeManageVisibilityActionInput;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.diagrams.tests.graphql.GetManageVisibilityActionsQueryRunner;
import org.eclipse.sirius.components.diagrams.tests.graphql.InvokeManageVisibilityActionMutationRunner;
import org.eclipse.sirius.web.application.nodeaction.managevisibility.ManageVisibilityHideAllAction;
import org.eclipse.sirius.web.application.nodeaction.managevisibility.ManageVisibilityRevealAllAction;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.data.GeneralViewManageVisibilityTestsProjectData;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.services.nodeactions.managevisibility.ManageVisibilityRevealValuedContentAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Tests the manage visibility node action.
 *
 * @author mcharfadi
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVManageVisibilityTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private GetManageVisibilityActionsQueryRunner getActionsQueryRunner;

    @Autowired
    private InvokeManageVisibilityActionMutationRunner invokeActionMutationRunner;

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToGeneralViewDiagram() {
        var diagramEventInput = new DiagramEventInput(
                UUID.randomUUID(),
                GeneralViewManageVisibilityTestsProjectData.EDITING_CONTEXT_ID,
                GeneralViewManageVisibilityTestsProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @GivenSysONServer({ GeneralViewManageVisibilityTestsProjectData.SCRIPT_PATH })
    @DisplayName("GIVEN a graphical node with some children hidden and some revealed, WHEN invoking the show valued content only, THEN only the children that have children are visible")
    @Test
    public void invokeShowOnlyValuedContent() {
        var flux = this.givenSubscriptionToGeneralViewDiagram();
        var nodeId = new AtomicReference<String>();
        Consumer<DiagramRefreshedEventPayload> initialDiagramContentConsumer = payload -> Optional.of(payload)
                .ifPresentOrElse(diagramRefreshedEventPayload -> {
                    var diagram = diagramRefreshedEventPayload.diagram();
                    assertThat(diagram.getNodes()).hasSize(1);
                    nodeId.set(diagram.getNodes().get(0).getId());
                    assertThat(diagram.getNodes().get(0).getChildNodes()).hasSize(6);
                    var children = diagram.getNodes().get(0).getChildNodes();
                    assertThat(children.stream().filter(node -> node.getState().equals(ViewModifier.Hidden))).hasSize(6);
                    assertThat(children.stream().filter(node -> node.getState().equals(ViewModifier.Normal))).hasSize(0);
                }, () -> fail("Missing diagram"));

        Runnable getActions = () -> {
            Map<String, Object> variables = Map.of(
                    "editingContextId", GeneralViewManageVisibilityTestsProjectData.EDITING_CONTEXT_ID,
                    "diagramId", GeneralViewManageVisibilityTestsProjectData.GraphicalIds.DIAGRAM_ID,
                    "diagramElementId", nodeId.get()
            );
            var result = this.getActionsQueryRunner.run(variables);
            List<String> actionsIds = JsonPath.read(result.data(), "$.data.viewer.editingContext.representation.description.manageVisibilityActions[*].id");
            List<String> actionsLabels = JsonPath.read(result.data(), "$.data.viewer.editingContext.representation.description.manageVisibilityActions[*].label");

            assertThat(actionsIds)
                    .isNotEmpty()
                    .contains(ManageVisibilityRevealAllAction.ACTION_ID)
                    .contains(ManageVisibilityHideAllAction.ACTION_ID)
                    .contains(ManageVisibilityRevealValuedContentAction.ACTION_ID);

            assertThat(actionsLabels)
                    .isNotEmpty()
                    .contains("Hide all")
                    .contains("Reveal all")
                    .contains("Reveal valued content only");
        };

        Runnable invokeRevealValuedContentAction = () -> {
            var input = new InvokeManageVisibilityActionInput(UUID.randomUUID(), GeneralViewManageVisibilityTestsProjectData.EDITING_CONTEXT_ID, GeneralViewManageVisibilityTestsProjectData.GraphicalIds.DIAGRAM_ID, nodeId.get(), ManageVisibilityRevealValuedContentAction.ACTION_ID);
            var result = this.invokeActionMutationRunner.run(input);
            String typename = JsonPath.read(result.data(), "$.data.invokeManageVisibilityAction.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<DiagramRefreshedEventPayload> updatedAfterRevealDiagramContentMatcher = payload -> Optional.of(payload)
                .ifPresentOrElse(diagramRefreshedEventPayload -> {
                    var diagram = diagramRefreshedEventPayload.diagram();
                    assertThat(diagram.getNodes()).hasSize(1);
                    nodeId.set(diagram.getNodes().get(0).getId());
                    assertThat(diagram.getNodes().get(0).getChildNodes()).hasSize(6);
                    var children = diagram.getNodes().get(0).getChildNodes();
                    assertThat(children.stream().filter(node -> node.getState().equals(ViewModifier.Hidden))).hasSize(5);
                    assertThat(children.stream().filter(node -> node.getState().equals(ViewModifier.Normal))).hasSize(1);
                    assertThat(children.stream().filter(node -> node.getState().equals(ViewModifier.Faded))).hasSize(0);
                }, () -> fail("Missing diagram"));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(getActions)
                .then(invokeRevealValuedContentAction)
                .consumeNextWith(updatedAfterRevealDiagramContentMatcher)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
