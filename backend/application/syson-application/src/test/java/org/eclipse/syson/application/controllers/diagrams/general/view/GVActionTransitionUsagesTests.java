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
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controllers.diagrams.testers.NodeCreationTester;
import org.eclipse.syson.application.data.GeneralViewActionTransitionUsagesProjectData;
import org.eclipse.syson.diagram.common.view.services.NodeFinder;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;
import reactor.test.StepVerifier.Step;

/**
 * Tests TransitionUsage on the General View diagram.
 *
 * @author Arthur Daussy
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVActionTransitionUsagesTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramReference givenDiagram;

    @Autowired
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private NodeCreationTester nodeCreationTester;

    private DiagramDescriptionIdProvider diagramDescriptionIdProvider;

    private Step<DiagramRefreshedEventPayload> verifier;

    private AtomicReference<Diagram> diagram;

    private DiagramDescription diagramDescription;

    private final IDescriptionNameGenerator descriptionNameGenerator = new GVDescriptionNameGenerator();

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewActionTransitionUsagesProjectData.EDITING_CONTEXT_ID,
                GeneralViewActionTransitionUsagesProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.diagram = this.givenDiagram.getDiagram(this.verifier);
        this.diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewActionTransitionUsagesProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        this.diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(this.diagramDescription, this.diagramIdProvider);
    }

    @AfterEach
    public void tearDown() {
        if (this.verifier != null) {
            this.verifier.thenCancel()
                    .verify(Duration.ofSeconds(10));
        }
    }

    @Sql(scripts = { GeneralViewActionTransitionUsagesProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @DisplayName("Given a model with TransitionUsage ending on 'start' or 'done' ActionUsage, when adding existing nested element on the parent of the TransitionUsage, then the 'start' and 'done' node should be added to the ActionFlow compartment.")
    @Test
    public void addExistingElementsOnDiagram() {
        String creationToolId = this.diagramDescriptionIdProvider.getNodeCreationToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getActionUsage()),
                "Add existing nested elements");
        assertThat(creationToolId).as("The tool 'Add existing elements' should exist on Action Usage").isNotNull();
        this.verifier.then(() -> this.nodeCreationTester.createNode(GeneralViewActionTransitionUsagesProjectData.EDITING_CONTEXT_ID, this.diagram, "a0", creationToolId));

        Consumer<DiagramRefreshedEventPayload> updatedDiagramConsumer = payload -> Optional.of(payload)
                .map(DiagramRefreshedEventPayload::diagram)
                .ifPresentOrElse(newDiagram -> {
                    Node a0ActionFlowCmp = new NodeFinder(newDiagram)
                            .getOneNodeMatching(n -> GeneralViewActionTransitionUsagesProjectData.GraphicalIds.A0_ACTION_FLOW_CMP_ID.equals(n.getId()))
                            .get();
                    assertThat(a0ActionFlowCmp.getChildNodes()).as("5 nodes should be visible on this action flow compartiment").hasSize(5);
                    assertThat(a0ActionFlowCmp.getChildNodes())
                            .as("Should contain a start node")
                            .anyMatch(n -> Objects.equals(n.getTargetObjectId(), GeneralViewActionTransitionUsagesProjectData.SemanticIds.START_ACTION_USAGE_ID))
                            .as("Should contain a done node")
                            .anyMatch(n -> Objects.equals(n.getTargetObjectId(), GeneralViewActionTransitionUsagesProjectData.SemanticIds.DONE_ACTION_USAGE_ID))
                            .as("Should contain a1")
                            .anyMatch(n -> Objects.equals(n.getTargetObjectId(), GeneralViewActionTransitionUsagesProjectData.SemanticIds.A1_ID))
                            .as("Should contain a2")
                            .anyMatch(n -> Objects.equals(n.getTargetObjectId(), GeneralViewActionTransitionUsagesProjectData.SemanticIds.A2_ID))
                            .as("Should contain a3")
                            .anyMatch(n -> Objects.equals(n.getTargetObjectId(), GeneralViewActionTransitionUsagesProjectData.SemanticIds.A3_ID));

                    // Should contain only 3 edges
                    assertThat(newDiagram.getEdges()).as("Should contain only 3 edges (Start -> a1 (once), a1 -> Done , Start -> a4 (once))").hasSize(3);

                }, () -> fail("Missing diagram"));
        this.verifier.consumeNextWith(updatedDiagramConsumer);

    }

}
