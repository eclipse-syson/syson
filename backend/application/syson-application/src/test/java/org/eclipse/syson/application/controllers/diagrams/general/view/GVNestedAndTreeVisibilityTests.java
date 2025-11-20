/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.HideDiagramElementInput;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.diagrams.tests.graphql.HideDiagramElementMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.GVSimpleNestedActionTestProjectData;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Tests about visibility on action tree nodes and action nested nodes in the General View diagram.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVNestedAndTreeVisibilityTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private HideDiagramElementMutationRunner hideDiagramElementMutationRunner;

    @Autowired
    private ToolTester toolTester;

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GVSimpleNestedActionTestProjectData.EDITING_CONTEXT_ID,
                GVSimpleNestedActionTestProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        return flux;
    }

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a diagram with actionA and actionB linked by a composition edge, WHEN the action flow compartment is revelead on actionA, THEN actionB is hidden on the diagram background")
    @Sql(scripts = { GVSimpleNestedActionTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void testDisplayActionFlowCompartmentOnActionAShouldHideActionB() {
        var flux = this.givenSubscriptionToDiagram();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var actionB = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedActionTestProjectData.GraphicalIds.ACTION_B_ID).getNode();
            assertThat(actionB).isNotNull();
            assertThat(actionB.getState().equals(ViewModifier.Normal)).isTrue();

            var actionFlowCompartment = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedActionTestProjectData.GraphicalIds.ACTION_A_ID)
                    .childNodeWithLabel("action flow")
                    .getNode();
            assertThat(actionFlowCompartment).isNotNull();
            assertThat(actionFlowCompartment.getState().equals(ViewModifier.Hidden)).isTrue();

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(1);
            var edgeAB = new DiagramNavigator(diag).edgeWithId(GVSimpleNestedActionTestProjectData.GraphicalIds.A_B_EDGE_ID).getEdge();
            assertThat(edgeAB).isNotNull();
            assertThat(edgeAB.getState().equals(ViewModifier.Normal)).isTrue();
        });

        Runnable revealActionFlowCompartment = () -> {
            var input = new HideDiagramElementInput(UUID.randomUUID(), GVSimpleNestedActionTestProjectData.EDITING_CONTEXT_ID, GVSimpleNestedActionTestProjectData.GraphicalIds.DIAGRAM_ID,
                    Set.of(GVSimpleNestedActionTestProjectData.GraphicalIds.ACTION_A_ACTION_FLOW_COMPARTMENT), false);
            var result = this.hideDiagramElementMutationRunner.run(input);
            String typename = JsonPath.read(result, "$.data.hideDiagramElement.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var actionB = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedActionTestProjectData.GraphicalIds.ACTION_B_ID).getNode();
            assertThat(actionB).isNotNull();
            assertThat(actionB.getState().equals(ViewModifier.Hidden)).isTrue();

            var actionFlowCompartment = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedActionTestProjectData.GraphicalIds.ACTION_A_ID)
                    .childNodeWithLabel("action flow")
                    .getNode();
            assertThat(actionFlowCompartment).isNotNull();
            assertThat(actionFlowCompartment.getState().equals(ViewModifier.Normal)).isTrue();

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(1);
            var edgeAB = new DiagramNavigator(diag).edgeWithId(GVSimpleNestedActionTestProjectData.GraphicalIds.A_B_EDGE_ID).getEdge();
            assertThat(edgeAB).isNotNull();
            assertThat(edgeAB.getState().equals(ViewModifier.Hidden)).isTrue();
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(revealActionFlowCompartment)
                .consumeNextWith(updatedDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a diagram with actionA and actionB linked by a composition edge, WHEN the actions compartment is revelead on actionA, THEN actionB is hidden on the diagram background")
    @Sql(scripts = { GVSimpleNestedActionTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void testDisplayActionsCompartmentOnActionAShouldHideActionB() {
        var flux = this.givenSubscriptionToDiagram();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var actionB = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedActionTestProjectData.GraphicalIds.ACTION_B_ID).getNode();
            assertThat(actionB).isNotNull();
            assertThat(actionB.getState().equals(ViewModifier.Normal)).isTrue();

            var actionsCompartment = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedActionTestProjectData.GraphicalIds.ACTION_A_ID)
                    .childNodeWithLabel("actions")
                    .getNode();
            assertThat(actionsCompartment).isNotNull();
            assertThat(actionsCompartment.getState().equals(ViewModifier.Hidden)).isTrue();

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(1);
            var edgeAB = new DiagramNavigator(diag).edgeWithId(GVSimpleNestedActionTestProjectData.GraphicalIds.A_B_EDGE_ID).getEdge();
            assertThat(edgeAB).isNotNull();
            assertThat(edgeAB.getState().equals(ViewModifier.Normal)).isTrue();
        });

        Runnable revealActionsCompartment = () -> {
            var input = new HideDiagramElementInput(UUID.randomUUID(), GVSimpleNestedActionTestProjectData.EDITING_CONTEXT_ID, GVSimpleNestedActionTestProjectData.GraphicalIds.DIAGRAM_ID,
                    Set.of(GVSimpleNestedActionTestProjectData.GraphicalIds.ACTION_A_ACTIONS_COMPARTMENT), false);
            var result = this.hideDiagramElementMutationRunner.run(input);
            String typename = JsonPath.read(result, "$.data.hideDiagramElement.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var actionB = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedActionTestProjectData.GraphicalIds.ACTION_B_ID).getNode();
            assertThat(actionB).isNotNull();
            assertThat(actionB.getState().equals(ViewModifier.Hidden)).isTrue();

            var actionsCompartment = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedActionTestProjectData.GraphicalIds.ACTION_A_ID)
                    .childNodeWithLabel("actions")
                    .getNode();
            assertThat(actionsCompartment).isNotNull();
            assertThat(actionsCompartment.getState().equals(ViewModifier.Normal)).isTrue();

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(1);
            var edgeAB = new DiagramNavigator(diag).edgeWithId(GVSimpleNestedActionTestProjectData.GraphicalIds.A_B_EDGE_ID).getEdge();
            assertThat(edgeAB).isNotNull();
            assertThat(edgeAB.getState().equals(ViewModifier.Hidden)).isTrue();
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(revealActionsCompartment)
                .consumeNextWith(updatedDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a diagram with actionA and actionB linked by a composition edge, WHEN the items compartment is revelead on actionA, THEN actionB is still visible on the diagram background")
    @Sql(scripts = { GVSimpleNestedActionTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void testDisplayItemsCompartmentOnActionAShouldDoNothingOnActionB() {
        var flux = this.givenSubscriptionToDiagram();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var actionB = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedActionTestProjectData.GraphicalIds.ACTION_B_ID).getNode();
            assertThat(actionB).isNotNull();
            assertThat(actionB.getState().equals(ViewModifier.Normal)).isTrue();

            var itemsCompartment = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedActionTestProjectData.GraphicalIds.ACTION_A_ID)
                    .childNodeWithLabel("items")
                    .getNode();
            assertThat(itemsCompartment).isNotNull();
            assertThat(itemsCompartment.getState().equals(ViewModifier.Hidden)).isTrue();

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(1);
            var edgeAB = new DiagramNavigator(diag).edgeWithId(GVSimpleNestedActionTestProjectData.GraphicalIds.A_B_EDGE_ID).getEdge();
            assertThat(edgeAB).isNotNull();
            assertThat(edgeAB.getState().equals(ViewModifier.Normal)).isTrue();
        });

        Runnable revealItemsCompartment = () -> {
            var input = new HideDiagramElementInput(UUID.randomUUID(), GVSimpleNestedActionTestProjectData.EDITING_CONTEXT_ID, GVSimpleNestedActionTestProjectData.GraphicalIds.DIAGRAM_ID,
                    Set.of(GVSimpleNestedActionTestProjectData.GraphicalIds.ACTION_A_ITEMS_COMPARTMENT), false);
            var result = this.hideDiagramElementMutationRunner.run(input);
            String typename = JsonPath.read(result, "$.data.hideDiagramElement.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var actionB = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedActionTestProjectData.GraphicalIds.ACTION_B_ID).getNode();
            assertThat(actionB).isNotNull();
            assertThat(actionB.getState().equals(ViewModifier.Normal)).isTrue();

            var itemsCompartment = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedActionTestProjectData.GraphicalIds.ACTION_A_ID)
                    .childNodeWithLabel("actions")
                    .getNode();
            assertThat(itemsCompartment).isNotNull();
            assertThat(itemsCompartment.getState().equals(ViewModifier.Hidden)).isTrue();

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(1);
            var edgeAB = new DiagramNavigator(diag).edgeWithId(GVSimpleNestedActionTestProjectData.GraphicalIds.A_B_EDGE_ID).getEdge();
            assertThat(edgeAB).isNotNull();
            assertThat(edgeAB.getState().equals(ViewModifier.Normal)).isTrue();
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(revealItemsCompartment)
                .consumeNextWith(updatedDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a diagram with actionA and actionB linked by a composition edge, WHEN the Show content as Nested tool is executed, THEN actionB is hidden and all actionA compartments containing actionB are revealed")
    @Sql(scripts = { GVSimpleNestedActionTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void testShowContentAsNested() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GVSimpleNestedActionTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var showContentAsNestedToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()), "Show content as Nested");
        assertThat(showContentAsNestedToolId).as("The tool 'Show Content as Nested' should exist on ActionUsage").isNotNull();

        var showContentAsTreeToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()), "Show content as Tree");
        assertThat(showContentAsTreeToolId).as("The tool 'Show Content as Tree' should exist on ActionUsage").isNotNull();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var actionA = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedActionTestProjectData.GraphicalIds.ACTION_A_ID)
                    .getNode();
            assertThat(actionA).isNotNull();
            assertThat(actionA.getState().equals(ViewModifier.Normal)).isTrue();
            assertThat(actionA.getChildNodes().stream().allMatch(cn -> cn.getState().equals(ViewModifier.Hidden))).isTrue();

            var actionB = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedActionTestProjectData.GraphicalIds.ACTION_B_ID).getNode();
            assertThat(actionB).isNotNull();
            assertThat(actionB.getState().equals(ViewModifier.Normal)).isTrue();
            assertThat(actionB.getChildNodes().stream().allMatch(cn -> cn.getState().equals(ViewModifier.Hidden))).isTrue();

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(1);
            var edgeAB = new DiagramNavigator(diag).edgeWithId(GVSimpleNestedActionTestProjectData.GraphicalIds.A_B_EDGE_ID).getEdge();
            assertThat(edgeAB).isNotNull();
            assertThat(edgeAB.getState().equals(ViewModifier.Normal)).isTrue();
        });

        Runnable showContentAsNestedTool = () -> this.toolTester.invokeTool(GVSimpleNestedActionTestProjectData.EDITING_CONTEXT_ID, GVSimpleNestedActionTestProjectData.GraphicalIds.DIAGRAM_ID,
                GVSimpleNestedActionTestProjectData.GraphicalIds.ACTION_A_ID, showContentAsNestedToolId,
                List.of());

        Consumer<Object> updatedDiagramContentConsumer1 = assertRefreshedDiagramThat(diag -> {
            var actionA = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedActionTestProjectData.GraphicalIds.ACTION_A_ID)
                    .getNode();
            assertThat(actionA).isNotNull();
            assertThat(actionA.getState().equals(ViewModifier.Normal)).isTrue();
            assertThat(actionA.getChildNodes().stream().anyMatch(cn -> cn.getState().equals(ViewModifier.Normal))).isTrue();

            var actionB = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedActionTestProjectData.GraphicalIds.ACTION_B_ID).getNode();
            assertThat(actionB).isNotNull();
            assertThat(actionB.getState().equals(ViewModifier.Hidden)).isTrue();

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(1);
            var edgeAB = new DiagramNavigator(diag).edgeWithId(GVSimpleNestedActionTestProjectData.GraphicalIds.A_B_EDGE_ID).getEdge();
            assertThat(edgeAB).isNotNull();
            assertThat(edgeAB.getState().equals(ViewModifier.Hidden)).isTrue();
        });

        Runnable showContentAsTreeTool = () -> this.toolTester.invokeTool(GVSimpleNestedActionTestProjectData.EDITING_CONTEXT_ID, GVSimpleNestedActionTestProjectData.GraphicalIds.DIAGRAM_ID,
                GVSimpleNestedActionTestProjectData.GraphicalIds.ACTION_A_ID, showContentAsTreeToolId,
                List.of());

        Consumer<Object> updatedDiagramContentConsumer2 = assertRefreshedDiagramThat(diag -> {
            var actionA = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedActionTestProjectData.GraphicalIds.ACTION_A_ID)
                    .getNode();
            assertThat(actionA).isNotNull();
            assertThat(actionA.getState().equals(ViewModifier.Normal)).isTrue();
            assertThat(actionA.getChildNodes().stream().allMatch(cn -> cn.getState().equals(ViewModifier.Hidden))).isTrue();

            var actionB = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedActionTestProjectData.GraphicalIds.ACTION_B_ID).getNode();
            assertThat(actionB).isNotNull();
            assertThat(actionB.getState().equals(ViewModifier.Normal)).isTrue();

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(1);
            var edgeAB = new DiagramNavigator(diag).edgeWithId(GVSimpleNestedActionTestProjectData.GraphicalIds.A_B_EDGE_ID).getEdge();
            assertThat(edgeAB).isNotNull();
            assertThat(edgeAB.getState().equals(ViewModifier.Normal)).isTrue();
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(showContentAsNestedTool)
                .consumeNextWith(updatedDiagramContentConsumer1)
                .then(showContentAsTreeTool)
                .consumeNextWith(updatedDiagramContentConsumer2)
                .thenCancel()
                .verify(Duration.ofSeconds(100));
    }
}
