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

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Tests the control nodes (fork, merge, decision, join) inside the General View diagram.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVControlNodeTests extends AbstractIntegrationTests {

    private static final String ACTION_LABEL = LabelConstants.OPEN_QUOTE + "ref action" + LabelConstants.CLOSE_QUOTE + "\naction";

    private static final String ACTION_FLOW_LABEL = "action flow";

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
                GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        return flux;
    }

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a diagram with an ActionUsage, WHEN a DecisionNode is created, THEN the diagram shows the action flow compartment and the DecisionNode inside the ActionUsage")
    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void createDecisionNode() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var newDecisionNodeToolId = diagramDescriptionIdProvider.getNodeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()), "New Decision");
        assertThat(newDecisionNodeToolId).as("The tool 'New Decision' should exist on ActionUsage").isNotNull();

        var diagramId = new AtomicReference<String>();
        var actionNodeId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            diagramId.set(diagram.getId());

            var actionNode = new DiagramNavigator(diagram).nodeWithLabel(ACTION_LABEL).getNode();
            assertThat(actionNode).isNotNull();
            actionNodeId.set(actionNode.getId());

            var actionFlowCompartmentNode = new DiagramNavigator(diagram).nodeWithLabel(ACTION_LABEL).childNodeWithLabel(ACTION_FLOW_LABEL).getNode();
            assertThat(actionFlowCompartmentNode.getState()).isEqualTo(ViewModifier.Hidden);
        });

        Runnable newDecisionTool = () -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagramId.get(), actionNodeId.get(), newDecisionNodeToolId,
                List.of());

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            var actionFlowCompartmentNode = new DiagramNavigator(diagram).nodeWithLabel(ACTION_LABEL).childNodeWithLabel(ACTION_FLOW_LABEL).getNode();
            assertThat(actionFlowCompartmentNode.getState()).isEqualTo(ViewModifier.Normal);
            List<Node> childNodes = actionFlowCompartmentNode.getChildNodes();
            assertThat(childNodes).hasSize(1);
            assertThat(childNodes.get(0).getOutsideLabels()).isNotNull();
            assertThat(childNodes.get(0).getOutsideLabels().get(0).text()).isEqualTo("decisionNode1");
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(newDecisionTool)
                .consumeNextWith(updatedDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a diagram with an ActionUsage, WHEN a ForkNode is created, THEN the diagram shows the action flow compartment and the ForkNode inside the ActionUsage")
    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void createForkNode() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var newForkNodeToolId = diagramDescriptionIdProvider.getNodeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()), "New Fork");
        assertThat(newForkNodeToolId).as("The tool 'New Fork' should exist on ActionUsage").isNotNull();

        var diagramId = new AtomicReference<String>();
        var actionNodeId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            diagramId.set(diagram.getId());

            var actionNode = new DiagramNavigator(diagram).nodeWithLabel(ACTION_LABEL).getNode();
            assertThat(actionNode).isNotNull();
            actionNodeId.set(actionNode.getId());

            var actionFlowCompartmentNode = new DiagramNavigator(diagram).nodeWithLabel(ACTION_LABEL).childNodeWithLabel(ACTION_FLOW_LABEL).getNode();
            assertThat(actionFlowCompartmentNode.getState()).isEqualTo(ViewModifier.Hidden);
        });

        Runnable newForkTool = () -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagramId.get(), actionNodeId.get(), newForkNodeToolId,
                List.of());

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            var actionFlowCompartmentNode = new DiagramNavigator(diagram).nodeWithLabel(ACTION_LABEL).childNodeWithLabel(ACTION_FLOW_LABEL).getNode();
            assertThat(actionFlowCompartmentNode.getState()).isEqualTo(ViewModifier.Normal);
            List<Node> childNodes = actionFlowCompartmentNode.getChildNodes();
            assertThat(childNodes).hasSize(1);
            assertThat(childNodes.get(0).getOutsideLabels()).isNotNull();
            assertThat(childNodes.get(0).getOutsideLabels().get(0).text()).isEqualTo("forkNode1");
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(newForkTool)
                .consumeNextWith(updatedDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a diagram with an ActionUsage, WHEN a JoinNode is created, THEN the diagram shows the action flow compartment and the JoinNode inside the ActionUsage")
    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void createJoinNode() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var newJoinNodeToolId = diagramDescriptionIdProvider.getNodeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()), "New Join");
        assertThat(newJoinNodeToolId).as("The tool 'New Join' should exist on ActionUsage").isNotNull();

        var diagramId = new AtomicReference<String>();
        var actionNodeId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            diagramId.set(diagram.getId());

            var actionNode = new DiagramNavigator(diagram).nodeWithLabel(ACTION_LABEL).getNode();
            assertThat(actionNode).isNotNull();
            actionNodeId.set(actionNode.getId());

            var actionFlowCompartmentNode = new DiagramNavigator(diagram).nodeWithLabel(ACTION_LABEL).childNodeWithLabel(ACTION_FLOW_LABEL).getNode();
            assertThat(actionFlowCompartmentNode.getState()).isEqualTo(ViewModifier.Hidden);
        });

        Runnable newJoinTool = () -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagramId.get(), actionNodeId.get(), newJoinNodeToolId,
                List.of());

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            var actionFlowCompartmentNode = new DiagramNavigator(diagram).nodeWithLabel(ACTION_LABEL).childNodeWithLabel(ACTION_FLOW_LABEL).getNode();
            assertThat(actionFlowCompartmentNode.getState()).isEqualTo(ViewModifier.Normal);
            List<Node> childNodes = actionFlowCompartmentNode.getChildNodes();
            assertThat(childNodes).hasSize(1);
            assertThat(childNodes.get(0).getOutsideLabels()).isNotNull();
            assertThat(childNodes.get(0).getOutsideLabels().get(0).text()).isEqualTo("joinNode1");
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(newJoinTool)
                .consumeNextWith(updatedDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a diagram with an ActionUsage, WHEN a MergeNode is created, THEN the diagram shows the action flow compartment and the MergeNode inside the ActionUsage")
    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void createMergeNode() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var newMergeNodeToolId = diagramDescriptionIdProvider.getNodeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()), "New Merge");
        assertThat(newMergeNodeToolId).as("The tool 'New Merge' should exist on ActionUsage").isNotNull();

        var diagramId = new AtomicReference<String>();
        var actionNodeId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            diagramId.set(diagram.getId());

            var actionNode = new DiagramNavigator(diagram).nodeWithLabel(ACTION_LABEL).getNode();
            assertThat(actionNode).isNotNull();
            actionNodeId.set(actionNode.getId());

            var actionFlowCompartmentNode = new DiagramNavigator(diagram).nodeWithLabel(ACTION_LABEL).childNodeWithLabel(ACTION_FLOW_LABEL).getNode();
            assertThat(actionFlowCompartmentNode.getState()).isEqualTo(ViewModifier.Hidden);
        });

        Runnable newMergeTool = () -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagramId.get(), actionNodeId.get(), newMergeNodeToolId,
                List.of());

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            var actionFlowCompartmentNode = new DiagramNavigator(diagram).nodeWithLabel(ACTION_LABEL).childNodeWithLabel(ACTION_FLOW_LABEL).getNode();
            assertThat(actionFlowCompartmentNode.getState()).isEqualTo(ViewModifier.Normal);
            List<Node> childNodes = actionFlowCompartmentNode.getChildNodes();
            assertThat(childNodes).hasSize(1);
            assertThat(childNodes.get(0).getOutsideLabels()).isNotNull();
            assertThat(childNodes.get(0).getOutsideLabels().get(0).text()).isEqualTo("mergeNode1");
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(newMergeTool)
                .consumeNextWith(updatedDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
