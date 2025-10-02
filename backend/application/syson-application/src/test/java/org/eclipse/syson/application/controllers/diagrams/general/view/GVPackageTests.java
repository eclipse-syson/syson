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
 * Tests the nodes inside the a PAckage nodes in the General View diagram.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVPackageTests extends AbstractIntegrationTests {

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

    @DisplayName("GIVEN a diagram with a Package node, WHEN a Part node and a Sub-Part node are created, THEN the Part node and the Sub-Part node are visible inside the Package, on the same level, with an edge between them")
    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void testCreateSubPartInPackage() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var newPartToolId = diagramDescriptionIdProvider.getNodeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage()), "New Part");
        assertThat(newPartToolId).as("The tool 'New Part' should exist on the Package").isNotNull();

        var newSubPartToolId = diagramDescriptionIdProvider.getNodeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()), "New Part");
        assertThat(newPartToolId).as("The tool 'New Part' should exist on the PartUsage").isNotNull();

        var diagramId = new AtomicReference<String>();
        var packageNodeId = new AtomicReference<String>();
        var partNodeId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            diagramId.set(diag.getId());

            var packageNode = new DiagramNavigator(diag).nodeWithLabel("Package").getNode();
            packageNodeId.set(packageNode.getId());

            assertThat(packageNode.getChildNodes()).hasSize(0);

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(3);

        });

        Runnable newPartUsageTool = () -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagramId.get(), packageNodeId.get(), newPartToolId,
                List.of());

        Consumer<Object> updatedDiagramContentConsumerAfterNewPart = assertRefreshedDiagramThat(diag -> {
            var packageNode = new DiagramNavigator(diag).nodeWithLabel("Package").getNode();

            var partNode = new DiagramNavigator(diag).nodeWithLabel("Package").childNodeWithLabel("\u00ABpart\u00BB\npart1").getNode();
            partNodeId.set(partNode.getId());

            assertThat(packageNode.getChildNodes()).hasSize(1);
            assertThat(packageNode.getChildNodes().get(0)).isEqualTo(partNode);

            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(3);

        });

        Runnable newSubPartUsageTool = () -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagramId.get(), partNodeId.get(), newSubPartToolId,
                List.of());

        Consumer<Object> updatedDiagramContentConsumerAfterNewSubPart = assertRefreshedDiagramThat(diag -> {
            var packageNode = new DiagramNavigator(diag).nodeWithLabel("Package").getNode();

            assertThat(packageNode.getChildNodes()).hasSize(2);

            // a new edge exists between the Part and the sub-Part inside the Package node
            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(4);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(newPartUsageTool)
                .consumeNextWith(updatedDiagramContentConsumerAfterNewPart)
                .then(newSubPartUsageTool)
                .consumeNextWith(updatedDiagramContentConsumerAfterNewSubPart)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
