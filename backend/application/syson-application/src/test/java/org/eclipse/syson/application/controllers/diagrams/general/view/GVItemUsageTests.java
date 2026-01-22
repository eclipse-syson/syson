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
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
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
 * ItemUsage related-tests in the General View diagram.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVItemUsageTests extends AbstractIntegrationTests {

    private static final String PACKAGE = "Package";

    private static final String PACKAGE1 = "Package1";

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

    @DisplayName("GIVEN a diagram with an ActionUsage node, WHEN an ItemUsage is created from this node, THEN the new ItemUsage is visible as sibling node")
    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void testCreateItemOnActionUsage() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var newItemUsageOnActionUsageToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()), "New Item");
        assertThat(newItemUsageOnActionUsageToolId).as("The tool 'New Item' should exist on the ActionUsage").isNotNull();

        var diagramId = new AtomicReference<String>();
        var actionUsageNodeId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            diagramId.set(diag.getId());
            var actionUsageNode = new DiagramNavigator(diag)
                    .nodeWithLabel(LabelConstants.OPEN_QUOTE + "ref action" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "action").getNode();
            actionUsageNodeId.set(actionUsageNode.getId());
        });

        Runnable newItemUsageOnActionUsage = () -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagramId.get(), actionUsageNodeId.get(),
                newItemUsageOnActionUsageToolId, List.of());

        Consumer<Object> updatedDiagramContentConsumerAfterNewItemOnAction = assertRefreshedDiagramThat(diag -> {
            var itemNodeUsage = new DiagramNavigator(diag)
                    .nodeWithLabel(LabelConstants.OPEN_QUOTE + "item" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "item1").getNode();
            assertThat(itemNodeUsage).isNotNull();

            var newEdgeTargetNode = new DiagramNavigator(diag).edgeWithTargetObjectId(GeneralViewWithTopNodesTestProjectData.SemanticIds.ACTION_USAGE_ID).targetNode().getNode();
            assertEquals(newEdgeTargetNode, itemNodeUsage);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(newItemUsageOnActionUsage)
                .consumeNextWith(updatedDiagramContentConsumerAfterNewItemOnAction)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a diagram with an ActionDefinition node, WHEN an ItemUsage is created from this node, THEN the new ItemUsage is visible as sibling node")
    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void testCreateItemOnActionDefinition() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var newItemUsageOnActionDefinitionToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionDefinition()), "New Item");
        assertThat(newItemUsageOnActionDefinitionToolId).as("The tool 'New Item' should exist on the ActionDefinition").isNotNull();

        var diagramId = new AtomicReference<String>();
        var actionDefNodeId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            diagramId.set(diag.getId());
            var actionDefNode = new DiagramNavigator(diag)
                    .nodeWithLabel(LabelConstants.OPEN_QUOTE + "action def" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "ActionDefinition :> UseCaseDefinition").getNode();
            actionDefNodeId.set(actionDefNode.getId());
        });

        Runnable newItemUsageOnActionDefinition = () -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagramId.get(), actionDefNodeId.get(),
                newItemUsageOnActionDefinitionToolId, List.of());

        Consumer<Object> updatedDiagramContentConsumerAfterNewItemOnActionDef = assertRefreshedDiagramThat(diag -> {
            var itemNodeUsage = new DiagramNavigator(diag)
                    .nodeWithLabel(LabelConstants.OPEN_QUOTE + "item" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "item1").getNode();
            assertThat(itemNodeUsage).isNotNull();

            var newEdgeTargetNode = new DiagramNavigator(diag).edgeWithTargetObjectId(GeneralViewWithTopNodesTestProjectData.SemanticIds.ACTION_DEFINITION_ID).targetNode().getNode();
            assertEquals(newEdgeTargetNode, itemNodeUsage);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(newItemUsageOnActionDefinition)
                .consumeNextWith(updatedDiagramContentConsumerAfterNewItemOnActionDef)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
