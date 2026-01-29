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
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.GVSimpleNestedAndTreeElementsTestProjectData;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.helper.LabelConstants;
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
 * Tests about visibility on action tree nodes and action nested nodes in the General View diagram.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVNestedAndTreeVisibilityTests extends AbstractIntegrationTests {

    private static final String ATTRIBUTES = "attributes";

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
                GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID,
                GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a diagram with actionA and actionB linked by a composition edge, WHEN the action flow compartment is revelead on actionA, THEN actionB is hidden on the diagram background")
    @GivenSysONServer({ GVSimpleNestedAndTreeElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void testDisplayActionFlowCompartmentOnActionAShouldHideActionB() {
        var flux = this.givenSubscriptionToDiagram();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var actionB = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_B_ID).getNode();
            assertThat(actionB).isNotNull();
            assertThat(actionB.getState().equals(ViewModifier.Normal)).isTrue();

            var actionFlowCompartment = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_A_ID)
                    .childNodeWithLabel("action flow")
                    .getNode();
            assertThat(actionFlowCompartment).isNotNull();
            assertThat(actionFlowCompartment.getState().equals(ViewModifier.Hidden)).isTrue();

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(1);
            var edgeAB = new DiagramNavigator(diag).edgeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.A_B_EDGE_ID).getEdge();
            assertThat(edgeAB).isNotNull();
            assertThat(edgeAB.getState().equals(ViewModifier.Normal)).isTrue();
        });

        Runnable revealActionFlowCompartment = () -> {
            var input = new HideDiagramElementInput(UUID.randomUUID(), GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID, GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.DIAGRAM_ID,
                    Set.of(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_A_ACTION_FLOW_COMPARTMENT), false);
            var result = this.hideDiagramElementMutationRunner.run(input);
            String typename = JsonPath.read(result.data(), "$.data.hideDiagramElement.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var actionB = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_B_ID).getNode();
            assertThat(actionB).isNotNull();
            assertThat(actionB.getState().equals(ViewModifier.Hidden)).isTrue();

            var actionFlowCompartment = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_A_ID)
                    .childNodeWithLabel("action flow")
                    .getNode();
            assertThat(actionFlowCompartment).isNotNull();
            assertThat(actionFlowCompartment.getState().equals(ViewModifier.Normal)).isTrue();

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(1);
            var edgeAB = new DiagramNavigator(diag).edgeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.A_B_EDGE_ID).getEdge();
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
    @GivenSysONServer({ GVSimpleNestedAndTreeElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void testDisplayActionsCompartmentOnActionAShouldHideActionB() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var newActionToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()), "New Action");
        assertThat(newActionToolId).as("The tool 'New Action' should exist on the ActionUsage").isNotNull();

        var newItemInOutToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()), "New Item Inout");
        assertThat(newActionToolId).as("The tool 'New Item Inout' should exist on the ActionUsage").isNotNull();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var actionB = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_B_ID).getNode();
            assertThat(actionB).isNotNull();
            assertThat(actionB.getState().equals(ViewModifier.Normal)).isTrue();

            var actionsCompartment = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_A_ID)
                    .childNodeWithLabel("actions")
                    .getNode();
            assertThat(actionsCompartment).isNotNull();
            assertThat(actionsCompartment.getState().equals(ViewModifier.Hidden)).isTrue();

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(1);
            var edgeAB = new DiagramNavigator(diag).edgeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.A_B_EDGE_ID).getEdge();
            assertThat(edgeAB).isNotNull();
            assertThat(edgeAB.getState().equals(ViewModifier.Normal)).isTrue();
        });

        Runnable revealActionsCompartment = () -> {
            var input = new HideDiagramElementInput(UUID.randomUUID(), GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID, GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.DIAGRAM_ID,
                    Set.of(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_A_ACTIONS_COMPARTMENT), false);
            var result = this.hideDiagramElementMutationRunner.run(input);
            String typename = JsonPath.read(result.data(), "$.data.hideDiagramElement.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var actionB = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_B_ID).getNode();
            assertThat(actionB).isNotNull();
            assertThat(actionB.getState().equals(ViewModifier.Hidden)).isTrue();

            var actionsCompartment = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_A_ID)
                    .childNodeWithLabel("actions")
                    .getNode();
            assertThat(actionsCompartment).isNotNull();
            assertThat(actionsCompartment.getState().equals(ViewModifier.Normal)).isTrue();

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(1);
            var edgeAB = new DiagramNavigator(diag).edgeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.A_B_EDGE_ID).getEdge();
            assertThat(edgeAB).isNotNull();
            assertThat(edgeAB.getState().equals(ViewModifier.Hidden)).isTrue();
        });

        // then a 'New Action' on actionA will add an ActionUsage only in the actionA actions compartment, and not on
        // the diagram background (to be precise, it will be created on the diagram background but hidden)
        Runnable newAction = () -> this.toolTester.invokeTool(GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID, GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.DIAGRAM_ID,
                GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_A_ID, newActionToolId,
                List.of());

        Consumer<Object> updatedDiagramContentConsumerAfterNewAction = assertRefreshedDiagramThat(diag -> {
            assertThat(diag.getNodes().stream().filter(n -> n.getState().equals(ViewModifier.Normal)).toList()).hasSize(3);
            assertThat(diag.getEdges().stream().filter(e -> e.getState().equals(ViewModifier.Normal)).toList()).hasSize(0);

            var actionsCompartmentNavigator = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_A_ID)
                    .childNodeWithLabel("actions");
            var actionsCompartment = actionsCompartmentNavigator.getNode();
            assertThat(actionsCompartment).isNotNull();
            assertThat(actionsCompartment.getState().equals(ViewModifier.Normal)).isTrue();
            assertThat(actionsCompartment.getChildNodes().size()).isEqualTo(2);
        });

        // then a 'New Item' on actionA will add an ItemUsage only as a border node, and not on the diagram background
        Runnable newItem = () -> this.toolTester.invokeTool(GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID, GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.DIAGRAM_ID,
                GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_A_ID, newItemInOutToolId,
                List.of());

        Consumer<Object> updatedDiagramContentConsumerAfterNewItem = assertRefreshedDiagramThat(diag -> {
            assertThat(diag.getNodes().stream().filter(n -> n.getState().equals(ViewModifier.Normal)).toList()).hasSize(3);
            assertThat(diag.getEdges().stream().filter(e -> e.getState().equals(ViewModifier.Normal)).toList()).hasSize(0);

            var actionANode = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_A_ID).getNode();
            var itemBorderNode = actionANode.getBorderNodes().stream().filter(bn -> "item1Inout".equals(bn.getOutsideLabels().get(0).text())).findFirst();

            assertThat(itemBorderNode).isNotEmpty();
            assertThat(itemBorderNode.get().getState().equals(ViewModifier.Normal)).isTrue();
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(revealActionsCompartment)
                .consumeNextWith(updatedDiagramContentConsumer)
                .then(newAction)
                .consumeNextWith(updatedDiagramContentConsumerAfterNewAction)
                .then(newItem)
                .consumeNextWith(updatedDiagramContentConsumerAfterNewItem)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a diagram with actionA and actionB linked by a composition edge, WHEN the items compartment is revelead on actionA, THEN actionB is still visible on the diagram background")
    @GivenSysONServer({ GVSimpleNestedAndTreeElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void testDisplayItemsCompartmentOnActionAShouldDoNothingOnActionB() {
        var flux = this.givenSubscriptionToDiagram();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var actionB = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_B_ID).getNode();
            assertThat(actionB).isNotNull();
            assertThat(actionB.getState().equals(ViewModifier.Normal)).isTrue();

            var itemsCompartment = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_A_ID)
                    .childNodeWithLabel("items")
                    .getNode();
            assertThat(itemsCompartment).isNotNull();
            assertThat(itemsCompartment.getState().equals(ViewModifier.Hidden)).isTrue();

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(1);
            var edgeAB = new DiagramNavigator(diag).edgeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.A_B_EDGE_ID).getEdge();
            assertThat(edgeAB).isNotNull();
            assertThat(edgeAB.getState().equals(ViewModifier.Normal)).isTrue();
        });

        Runnable revealItemsCompartment = () -> {
            var input = new HideDiagramElementInput(UUID.randomUUID(), GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID, GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.DIAGRAM_ID,
                    Set.of(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_A_ITEMS_COMPARTMENT), false);
            var result = this.hideDiagramElementMutationRunner.run(input);
            String typename = JsonPath.read(result.data(), "$.data.hideDiagramElement.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var actionB = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_B_ID).getNode();
            assertThat(actionB).isNotNull();
            assertThat(actionB.getState().equals(ViewModifier.Normal)).isTrue();

            var itemsCompartment = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_A_ID)
                    .childNodeWithLabel("actions")
                    .getNode();
            assertThat(itemsCompartment).isNotNull();
            assertThat(itemsCompartment.getState().equals(ViewModifier.Hidden)).isTrue();

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(1);
            var edgeAB = new DiagramNavigator(diag).edgeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.A_B_EDGE_ID).getEdge();
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
    @GivenSysONServer({ GVSimpleNestedAndTreeElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void testShowContentAsNested() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var showContentAsNestedToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()), "Show content as Nested");
        assertThat(showContentAsNestedToolId).as("The tool 'Show Content as Nested' should exist on ActionUsage").isNotNull();

        var showContentAsTreeToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()), "Show content as Tree");
        assertThat(showContentAsTreeToolId).as("The tool 'Show Content as Tree' should exist on ActionUsage").isNotNull();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var actionA = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_A_ID)
                    .getNode();
            assertThat(actionA).isNotNull();
            assertThat(actionA.getState().equals(ViewModifier.Normal)).isTrue();
            assertThat(actionA.getChildNodes().stream().allMatch(cn -> cn.getState().equals(ViewModifier.Hidden))).isTrue();

            var actionB = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_B_ID).getNode();
            assertThat(actionB).isNotNull();
            assertThat(actionB.getState().equals(ViewModifier.Normal)).isTrue();
            assertThat(actionB.getChildNodes().stream().allMatch(cn -> cn.getState().equals(ViewModifier.Hidden))).isTrue();

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(1);
            var edgeAB = new DiagramNavigator(diag).edgeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.A_B_EDGE_ID).getEdge();
            assertThat(edgeAB).isNotNull();
            assertThat(edgeAB.getState().equals(ViewModifier.Normal)).isTrue();
        });

        Runnable showContentAsNestedTool = () -> this.toolTester.invokeTool(GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID, GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.DIAGRAM_ID,
                GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_A_ID, showContentAsNestedToolId,
                List.of());

        Consumer<Object> updatedDiagramContentConsumer1 = assertRefreshedDiagramThat(diag -> {
            var actionA = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_A_ID)
                    .getNode();
            assertThat(actionA).isNotNull();
            assertThat(actionA.getState().equals(ViewModifier.Normal)).isTrue();
            assertThat(actionA.getChildNodes().stream().anyMatch(cn -> cn.getState().equals(ViewModifier.Normal))).isTrue();

            var actionB = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_B_ID).getNode();
            assertThat(actionB).isNotNull();
            assertThat(actionB.getState().equals(ViewModifier.Hidden)).isTrue();

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(1);
            var edgeAB = new DiagramNavigator(diag).edgeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.A_B_EDGE_ID).getEdge();
            assertThat(edgeAB).isNotNull();
            assertThat(edgeAB.getState().equals(ViewModifier.Hidden)).isTrue();
        });

        Runnable showContentAsTreeTool = () -> this.toolTester.invokeTool(GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID, GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.DIAGRAM_ID,
                GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_A_ID, showContentAsTreeToolId,
                List.of());

        Consumer<Object> updatedDiagramContentConsumer2 = assertRefreshedDiagramThat(diag -> {
            var actionA = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_A_ID)
                    .getNode();
            assertThat(actionA).isNotNull();
            assertThat(actionA.getState().equals(ViewModifier.Normal)).isTrue();
            assertThat(actionA.getChildNodes().stream().allMatch(cn -> cn.getState().equals(ViewModifier.Hidden))).isTrue();

            var actionB = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_B_ID).getNode();
            assertThat(actionB).isNotNull();
            assertThat(actionB.getState().equals(ViewModifier.Normal)).isTrue();

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(1);
            var edgeAB = new DiagramNavigator(diag).edgeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.A_B_EDGE_ID).getEdge();
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
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a GV diagram with a PartDefinition, WHEN an attribute is created on the PartDefinition, THEN the attribute is only visible in its attributes compartment")
    @GivenSysONServer({ GVSimpleNestedAndTreeElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void testCreateAttributeOnPartDef() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var newAttributeToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartDefinition()), "New Attribute");
        assertThat(newAttributeToolId).as("The tool 'New Attribute' should exist on PartDefinition").isNotNull();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var partDefA = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PARTDEF_A_ID).getNode();
            assertThat(partDefA).isNotNull();
            assertThat(partDefA.getState().equals(ViewModifier.Normal)).isTrue();

            var attributesCompartment = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PARTDEF_A_ID)
                    .childNodeWithLabel(ATTRIBUTES)
                    .getNode();
            assertThat(attributesCompartment).isNotNull();
            assertThat(attributesCompartment.getState().equals(ViewModifier.Hidden)).isTrue();
            assertThat(attributesCompartment.getChildNodes()).hasSize(0);
        });

        Runnable newAttributeTool = () -> this.toolTester.invokeTool(GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID,
                GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.DIAGRAM_ID,
                GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PARTDEF_A_ID, newAttributeToolId,
                List.of());

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var partDefA = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PARTDEF_A_ID).getNode();
            assertThat(partDefA).isNotNull();
            assertThat(partDefA.getState().equals(ViewModifier.Normal)).isTrue();

            var attributesCompartment = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PARTDEF_A_ID)
                    .childNodeWithLabel(ATTRIBUTES)
                    .getNode();
            assertThat(attributesCompartment).isNotNull();
            assertThat(attributesCompartment.getState().equals(ViewModifier.Normal)).isTrue();
            assertThat(attributesCompartment.getChildNodes()).hasSize(1);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(newAttributeTool)
                .consumeNextWith(updatedDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a GV diagram with a PartDefinition, WHEN an item is created on the PartDefinition, THEN the item is visible on the diagram background")
    @GivenSysONServer({ GVSimpleNestedAndTreeElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void testCreateItemOnPartDef() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var newItemToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartDefinition()), "New Item");
        assertThat(newItemToolId).as("The tool 'New Item' should exist on PartDefinition").isNotNull();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var partDefA = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PARTDEF_A_ID).getNode();
            assertThat(partDefA).isNotNull();
            assertThat(partDefA.getState().equals(ViewModifier.Normal)).isTrue();

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(1);
        });

        Runnable newItemTool = () -> this.toolTester.invokeTool(GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID,
                GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.DIAGRAM_ID,
                GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PARTDEF_A_ID, newItemToolId,
                List.of());

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var partDefA = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PARTDEF_A_ID).getNode();
            assertThat(partDefA).isNotNull();
            assertThat(partDefA.getState().equals(ViewModifier.Normal)).isTrue();

            var item1 = new DiagramNavigator(diag).nodeWithLabel(LabelConstants.OPEN_QUOTE + "item" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "item1").getNode();
            assertThat(item1).isNotNull();
            assertThat(item1.getState().equals(ViewModifier.Normal)).isTrue();

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(2);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(newItemTool)
                .consumeNextWith(updatedDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a GV diagram with a PartDefinition, WHEN a port is created on the PartDefinition, THEN the port is only visible as border node on the PartDefinition")
    @GivenSysONServer({ GVSimpleNestedAndTreeElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void testCreatePortOnPartDef() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var newPortoolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartDefinition()), "New Port");
        assertThat(newPortoolId).as("The tool 'New Port' should exist on PartDefinition").isNotNull();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var partDefA = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PARTDEF_A_ID).getNode();
            assertThat(partDefA).isNotNull();
            assertThat(partDefA.getState().equals(ViewModifier.Normal)).isTrue();
            assertThat(partDefA.getBorderNodes()).hasSize(0);

            var portsCompartment = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PARTDEF_A_ID)
                    .childNodeWithLabel("ports")
                    .getNode();
            assertThat(portsCompartment).isNotNull();
            assertThat(portsCompartment.getState().equals(ViewModifier.Hidden)).isTrue();
            assertThat(portsCompartment.getChildNodes()).hasSize(0);

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(1);
        });

        Runnable newPortTool = () -> this.toolTester.invokeTool(GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID,
                GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.DIAGRAM_ID,
                GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PARTDEF_A_ID, newPortoolId,
                List.of());

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var partDefA = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PARTDEF_A_ID).getNode();
            assertThat(partDefA).isNotNull();
            assertThat(partDefA.getState().equals(ViewModifier.Normal)).isTrue();
            assertThat(partDefA.getBorderNodes()).hasSize(1);

            var portsCompartment = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PARTDEF_A_ID)
                    .childNodeWithLabel("ports")
                    .getNode();
            assertThat(portsCompartment).isNotNull();
            assertThat(portsCompartment.getState().equals(ViewModifier.Hidden)).isTrue();
            assertThat(portsCompartment.getChildNodes()).hasSize(1);

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(2);

            var port1 = new DiagramNavigator(diag).nodeWithLabel(LabelConstants.OPEN_QUOTE + "port" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "port1").getNode();
            assertThat(port1).isNotNull();
            assertThat(port1.getState().equals(ViewModifier.Hidden)).isTrue();
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(newPortTool)
                .consumeNextWith(updatedDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a GV diagram with a PartUsage, WHEN an attribute is created on the PartUsage, THEN the attribute is only visible in its attributes compartment")
    @GivenSysONServer({ GVSimpleNestedAndTreeElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void testCreateAttributeOnPart() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var newAttributeToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()), "New Attribute");
        assertThat(newAttributeToolId).as("The tool 'New Attribute' should exist on PartUsage").isNotNull();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var partA = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PART_A_ID).getNode();
            assertThat(partA).isNotNull();
            assertThat(partA.getState().equals(ViewModifier.Normal)).isTrue();

            var attributesCompartment = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PART_A_ID)
                    .childNodeWithLabel(ATTRIBUTES)
                    .getNode();
            assertThat(attributesCompartment).isNotNull();
            assertThat(attributesCompartment.getState().equals(ViewModifier.Hidden)).isTrue();
            assertThat(attributesCompartment.getChildNodes()).hasSize(0);
        });

        Runnable newAttributeTool = () -> this.toolTester.invokeTool(GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID,
                GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.DIAGRAM_ID,
                GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PART_A_ID, newAttributeToolId,
                List.of());

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var partA = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PART_A_ID).getNode();
            assertThat(partA).isNotNull();
            assertThat(partA.getState().equals(ViewModifier.Normal)).isTrue();

            var attributesCompartment = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PART_A_ID)
                    .childNodeWithLabel(ATTRIBUTES)
                    .getNode();
            assertThat(attributesCompartment).isNotNull();
            assertThat(attributesCompartment.getState().equals(ViewModifier.Normal)).isTrue();
            assertThat(attributesCompartment.getChildNodes()).hasSize(1);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(newAttributeTool)
                .consumeNextWith(updatedDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a GV diagram with a PartUsage, WHEN an item is created on the PartUsage, THEN the item is visible on the diagram background")
    @GivenSysONServer({ GVSimpleNestedAndTreeElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void testCreateItemOnPart() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var newItemToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()), "New Item");
        assertThat(newItemToolId).as("The tool 'New Item' should exist on PartUsage").isNotNull();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var partA = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PART_A_ID).getNode();
            assertThat(partA).isNotNull();
            assertThat(partA.getState().equals(ViewModifier.Normal)).isTrue();

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(1);
        });

        Runnable newItemTool = () -> this.toolTester.invokeTool(GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID,
                GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.DIAGRAM_ID,
                GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PART_A_ID, newItemToolId,
                List.of());

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var partA = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PART_A_ID).getNode();
            assertThat(partA).isNotNull();
            assertThat(partA.getState().equals(ViewModifier.Normal)).isTrue();

            var item1 = new DiagramNavigator(diag).nodeWithLabel(LabelConstants.OPEN_QUOTE + "item" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "item1").getNode();
            assertThat(item1).isNotNull();
            assertThat(item1.getState().equals(ViewModifier.Normal)).isTrue();

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(2);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(newItemTool)
                .consumeNextWith(updatedDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a GV diagram with a PartUsage, WHEN a port is created on the PartUsage, THEN the port is only visible as border node on the PartUsage")
    @GivenSysONServer({ GVSimpleNestedAndTreeElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void testCreatePortOnPart() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var newPortoolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()), "New Port");
        assertThat(newPortoolId).as("The tool 'New Port' should exist on PartUsage").isNotNull();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var partA = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PART_A_ID).getNode();
            assertThat(partA).isNotNull();
            assertThat(partA.getState().equals(ViewModifier.Normal)).isTrue();
            assertThat(partA.getBorderNodes()).hasSize(0);

            var portsCompartment = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PART_A_ID)
                    .childNodeWithLabel("ports")
                    .getNode();
            assertThat(portsCompartment).isNotNull();
            assertThat(portsCompartment.getState().equals(ViewModifier.Hidden)).isTrue();
            assertThat(portsCompartment.getChildNodes()).hasSize(0);

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(1);
        });

        Runnable newPortTool = () -> this.toolTester.invokeTool(GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID,
                GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.DIAGRAM_ID,
                GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PART_A_ID, newPortoolId,
                List.of());

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var partA = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PART_A_ID).getNode();
            assertThat(partA).isNotNull();
            assertThat(partA.getState().equals(ViewModifier.Normal)).isTrue();
            assertThat(partA.getBorderNodes()).hasSize(1);

            var portsCompartment = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.PART_A_ID)
                    .childNodeWithLabel("ports")
                    .getNode();
            assertThat(portsCompartment).isNotNull();
            assertThat(portsCompartment.getState().equals(ViewModifier.Hidden)).isTrue();
            assertThat(portsCompartment.getChildNodes()).hasSize(1);

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(2);

            var port1 = new DiagramNavigator(diag).nodeWithLabel(LabelConstants.OPEN_QUOTE + "port" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "port1").getNode();
            assertThat(port1).isNotNull();
            assertThat(port1.getState().equals(ViewModifier.Hidden)).isTrue();
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(newPortTool)
                .consumeNextWith(updatedDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a GV diagram with a ActionUsage, WHEN an attribute is created on the ActionUsage, THEN the attribute is only visible in its attributes compartment")
    @GivenSysONServer({ GVSimpleNestedAndTreeElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void testCreateAttributeOnAction() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var newAttributeToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()), "New Attribute");
        assertThat(newAttributeToolId).as("The tool 'New Attribute' should exist on ActionUsage").isNotNull();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var actionA = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_A_ID).getNode();
            assertThat(actionA).isNotNull();
            assertThat(actionA.getState().equals(ViewModifier.Normal)).isTrue();

            var attributesCompartment = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_A_ID)
                    .childNodeWithLabel(ATTRIBUTES)
                    .getNode();
            assertThat(attributesCompartment).isNotNull();
            assertThat(attributesCompartment.getState().equals(ViewModifier.Hidden)).isTrue();
            assertThat(attributesCompartment.getChildNodes()).hasSize(0);
        });

        Runnable newAttributeTool = () -> this.toolTester.invokeTool(GVSimpleNestedAndTreeElementsTestProjectData.EDITING_CONTEXT_ID,
                GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.DIAGRAM_ID,
                GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_A_ID, newAttributeToolId,
                List.of());

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var actionA = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_A_ID).getNode();
            assertThat(actionA).isNotNull();
            assertThat(actionA.getState().equals(ViewModifier.Normal)).isTrue();

            var attributesCompartment = new DiagramNavigator(diag).nodeWithId(GVSimpleNestedAndTreeElementsTestProjectData.GraphicalIds.ACTION_A_ID)
                    .childNodeWithLabel(ATTRIBUTES)
                    .getNode();
            assertThat(attributesCompartment).isNotNull();
            assertThat(attributesCompartment.getState().equals(ViewModifier.Normal)).isTrue();
            assertThat(attributesCompartment.getChildNodes()).hasSize(1);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(newAttributeTool)
                .consumeNextWith(updatedDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

}
