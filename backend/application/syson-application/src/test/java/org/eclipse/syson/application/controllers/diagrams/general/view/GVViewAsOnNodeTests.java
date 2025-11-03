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
import org.eclipse.syson.application.data.ViewAsOnNodeTestProjectData;
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
 * Tests the ViewAs > XXXX tool on graphical nodes in the General View diagram.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVViewAsOnNodeTests extends AbstractIntegrationTests {

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
                ViewAsOnNodeTestProjectData.EDITING_CONTEXT_ID,
                ViewAsOnNodeTestProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        return flux;
    }

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a GV diagram with 3 PartUsages (linked by composition, partA, partB, partC), WHEN the 'View as > Interconnection View' tool is applied on partB, THEN a new ViewUsage (typed with IV) is created and visible in the GV and contain partB and partC.")
    @Sql(scripts = { ViewAsOnNodeTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void testViewAsIVOnPartUsage() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(ViewAsOnNodeTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var viewAsInterconnectionViewToolId = diagramDescriptionIdProvider.getNodeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()),
                "Interconnection View");
        assertThat(viewAsInterconnectionViewToolId).as("The tool 'View as > Interconnection View' should exist on partB").isNotNull();

        var diagramId = new AtomicReference<String>();
        var partBNodeId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            diagramId.set(diag.getId());
            assertThat(diag.getNodes()).hasSize(3);
            assertThat(diag.getEdges()).hasSize(2);

            var partBNode = new DiagramNavigator(diag).nodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "pB").getNode();
            partBNodeId.set(partBNode.getId());
        });

        Runnable viewAsInterconnectionViewTool = () -> this.toolTester.invokeTool(ViewAsOnNodeTestProjectData.EDITING_CONTEXT_ID, diagramId.get(), partBNodeId.get(), viewAsInterconnectionViewToolId,
                List.of());

        Consumer<Object> updatedDiagramContentConsumerAfterToolExecution = assertRefreshedDiagramThat(diag -> {
            assertThat(diag.getNodes()).hasSize(2);
            assertThat(diag.getEdges()).hasSize(1);

            var view2NodeNavigator = new DiagramNavigator(diag)
                    .nodeWithLabel(LabelConstants.OPEN_QUOTE + "view" + LabelConstants.CLOSE_QUOTE + " view2 : StandardViewDefinitions::InterconnectionView");
            var newViewUsageNode = view2NodeNavigator.getNode();
            assertThat(newViewUsageNode).isNotNull();

            var partBNodeNavigator = view2NodeNavigator.childNodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "pB");
            var partBNode = partBNodeNavigator.getNode();
            assertThat(partBNode).isNotNull();

            var partCNodeNavigator = partBNodeNavigator.childNodeWithLabel("interconnection")
                    .childNodeWithLabel(LabelConstants.OPEN_QUOTE + "part" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "pC");
            var partCNode = partCNodeNavigator.getNode();
            assertThat(partCNode).isNotNull();
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(viewAsInterconnectionViewTool)
                .consumeNextWith(updatedDiagramContentConsumerAfterToolExecution)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
