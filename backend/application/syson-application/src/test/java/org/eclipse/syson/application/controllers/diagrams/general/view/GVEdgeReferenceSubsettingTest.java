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

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeCreationTester;
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
 * ReferenceSubsetting-Edge related tests the in the General View diagram.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVEdgeReferenceSubsettingTest extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private EdgeCreationTester edgeTester;

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

    @DisplayName("GIVEN a diagram with two Usage nodes, WHEN a ReferenceSubsetting edge is created between them, THEN the edge is visisble on the diagram")
    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void testCreateReferenceSubsettingEdge() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var newReferenceSubsettingToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()),
                "New Reference Subsetting");
        assertThat(newReferenceSubsettingToolId).as("The tool 'New Reference Subsetting' should exist on the ActionUsage").isNotNull();

        var diagram = new AtomicReference<Diagram>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            diagram.set(diag);
            assertThat(new DiagramNavigator(diag).findDiagramEdgeCount()).isEqualTo(3);
        });

        Runnable newReferenceSubsettingTool = () -> this.edgeTester.createEdgeUsingNodeId(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagram,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_USAGE_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.STATE_USAGE_ID, newReferenceSubsettingToolId);

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diag -> {
            var diagramNavigator = new DiagramNavigator(diag);
            assertThat(diagramNavigator.findDiagramEdgeCount()).isEqualTo(4);
            var edgeDescriptionId = diagramDescriptionIdProvider.getEdgeDescriptionId(this.descriptionNameGenerator.getEdgeName(SysmlPackage.eINSTANCE.getReferenceSubsetting()));
            var referenceSubsettingEdge = diagramNavigator.edgeWithEdgeDescriptionId(edgeDescriptionId).getEdge();
            assertThat(referenceSubsettingEdge).isNotNull();
            assertThat(referenceSubsettingEdge.getSourceId()).isEqualTo(GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_USAGE_ID);
            assertThat(referenceSubsettingEdge.getTargetId()).isEqualTo(GeneralViewWithTopNodesTestProjectData.GraphicalIds.STATE_USAGE_ID);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(newReferenceSubsettingTool)
                .consumeNextWith(updatedDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
