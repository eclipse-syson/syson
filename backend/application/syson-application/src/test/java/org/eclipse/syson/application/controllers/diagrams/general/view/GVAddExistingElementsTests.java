/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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

import java.text.MessageFormat;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.ImageNodeStyle;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controllers.diagrams.testers.NodeCreationTester;
import org.eclipse.syson.application.data.GeneralViewAddExistingElementsIdentifiers;
import org.eclipse.syson.application.data.SysMLv2Identifiers;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;
import reactor.test.StepVerifier.Step;

/**
 * Tests the invocation of the "addExistingElements" tool in the General View diagram.
 *
 * @author gdaniel
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVAddExistingElementsTests extends AbstractIntegrationTests {

    private static final String PACKAGE1 = "Package1";

    private static final String ACTION1 = "action1";

    private static final String ACTION2 = "action2";

    private static final String ACTION3 = "action3";

    private static final String PART1 = "part1";

    private static final String PART2 = "part2";

    private static final String ATTRIBUTE_DEFINITION = "AttributeDefinition1";

    private static final String NODE_SHOULD_BE_ON_DIAGRAM_MESSAGE = "Node {0} should exist on the diagram";

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

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewAddExistingElementsIdentifiers.EDITING_CONTEXT_ID,
                GeneralViewAddExistingElementsIdentifiers.Diagram.ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.diagram = this.givenDiagram.getDiagram(this.verifier);
        this.diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewAddExistingElementsIdentifiers.EDITING_CONTEXT_ID,
                SysMLv2Identifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        this.diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(this.diagramDescription, this.diagramIdProvider);
    }

    @AfterEach
    public void tearDown() {
        if (this.verifier != null) {
            this.verifier.thenCancel()
                    .verify(Duration.ofSeconds(10));
        }
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void addExistingElementsOnDiagram() {
        String creationToolId = this.diagramDescriptionIdProvider.getDiagramCreationToolId("Add existing elements");
        assertThat(creationToolId).as("The tool 'Add existing elements' should exist on the diagram").isNotNull();
        this.verifier.then(() -> this.nodeCreationTester.createNodeOnDiagram(GeneralViewAddExistingElementsIdentifiers.EDITING_CONTEXT_ID, this.diagram, creationToolId));

        Consumer<DiagramRefreshedEventPayload> updatedDiagramConsumer = payload -> Optional.of(payload)
                .map(DiagramRefreshedEventPayload::diagram)
                .ifPresentOrElse(newDiagram -> {
                    assertThat(newDiagram.getNodes()).as("3 nodes should be visible on the diagram").hasSize(3);
                    assertThat(newDiagram.getNodes())
                            .as(MessageFormat.format(NODE_SHOULD_BE_ON_DIAGRAM_MESSAGE, PACKAGE1))
                            .anyMatch(n -> Objects.equals(n.getTargetObjectLabel(), PACKAGE1))
                            .as(MessageFormat.format(NODE_SHOULD_BE_ON_DIAGRAM_MESSAGE, ACTION1))
                            .anyMatch(n -> Objects.equals(n.getTargetObjectLabel(), ACTION1))
                            .as(MessageFormat.format(NODE_SHOULD_BE_ON_DIAGRAM_MESSAGE, PART1))
                            .anyMatch(n -> Objects.equals(n.getTargetObjectLabel(), PART1));
                }, () -> fail("Missing diagram"));
        this.verifier.consumeNextWith(updatedDiagramConsumer);

    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void addExistingElementsRecursiveOnDiagram() {
        String creationToolId = this.diagramDescriptionIdProvider.getDiagramCreationToolId("Add existing elements (recursive)");
        assertThat(creationToolId).as("The tool 'Add existing elements (recursive)' should exist on the diagram").isNotNull();
        this.verifier.then(() -> this.nodeCreationTester.createNodeOnDiagram(GeneralViewAddExistingElementsIdentifiers.EDITING_CONTEXT_ID, this.diagram, creationToolId));

        Consumer<DiagramRefreshedEventPayload> updatedDiagramConsumer = payload -> Optional.of(payload)
                .map(DiagramRefreshedEventPayload::diagram)
                .ifPresentOrElse(newDiagram -> {
                    assertThat(newDiagram.getNodes()).as("4 nodes should be visible on the diagram").hasSize(4);
                    assertThat(newDiagram.getEdges())
                            .as("2 edges should be visible on the diagram")
                            .hasSize(2)
                            .as("The diagrm should contain a composite edge between part2 and part1")
                            .anyMatch(edge -> edge.getTargetObjectLabel().equals(PART1))
                            .as("The diagrm should contain a SuccessionAsUsage edge between start and action2")
                            .anyMatch(edge -> edge.getTargetObjectId().equals(GeneralViewAddExistingElementsIdentifiers.Semantic.SUCCESSION_START_ACTION_2));
                    assertThat(newDiagram.getNodes())
                            .as(MessageFormat.format(NODE_SHOULD_BE_ON_DIAGRAM_MESSAGE, PACKAGE1))
                            .anyMatch(n -> Objects.equals(n.getTargetObjectLabel(), PACKAGE1))
                            .as(MessageFormat.format(NODE_SHOULD_BE_ON_DIAGRAM_MESSAGE, ACTION1))
                            .anyMatch(n -> Objects.equals(n.getTargetObjectLabel(), ACTION1))
                            .as(MessageFormat.format(NODE_SHOULD_BE_ON_DIAGRAM_MESSAGE, PART1))
                            .anyMatch(n -> Objects.equals(n.getTargetObjectLabel(), PART1))
                            .as(MessageFormat.format(NODE_SHOULD_BE_ON_DIAGRAM_MESSAGE, PART2))
                            .anyMatch(n -> Objects.equals(n.getTargetObjectLabel(), PART2));

                    var optPackageNode = newDiagram.getNodes().stream()
                            .filter(n -> Objects.equals(n.getTargetObjectLabel(), PACKAGE1))
                            .findFirst();
                    assertThat(optPackageNode).isPresent();
                    assertThat(optPackageNode.get().getChildNodes())
                        .as("Node " + PACKAGE1 + " should contain 1 child")
                        .hasSize(1)
                        .as("Node " + ATTRIBUTE_DEFINITION + " should exist inside " + PACKAGE1)
                        .anyMatch(n -> Objects.equals(n.getTargetObjectLabel(), ATTRIBUTE_DEFINITION));

                    var optAction1Node = newDiagram.getNodes().stream()
                            .filter(n -> Objects.equals(n.getTargetObjectLabel(), ACTION1))
                            .findFirst();
                    assertThat(optAction1Node).isPresent();
                    var action1Node = optAction1Node.get();
                    var action1ActionsCompartment = this.getCompartment(action1Node, "actions");
                    assertThat(action1ActionsCompartment)
                            .as(ACTION1 + " should contain an actions compartment")
                            .isPresent();
                    assertThat(action1ActionsCompartment.get().getChildNodes())
                            .as(ACTION1 + " actions compartment should contain 1 child")
                            .hasSize(1)
                            .as(ACTION1 + " actions compartment should contain " + ACTION2)
                            .anyMatch(n -> Objects.equals(n.getTargetObjectLabel(), ACTION2));

                    var action1ActionFlowCompartment = this.getCompartment(action1Node, "action flow");
                    assertThat(action1ActionFlowCompartment)
                            .as(ACTION1 + " should contain an action flow compartment")
                            .isPresent();
                    assertThat(action1ActionFlowCompartment.get().getChildNodes())
                            .as(ACTION1 + " action flow compartment should contain 2 child")
                            .hasSize(2)
                            .as(ACTION1 + " action flow compartment should contain " + ACTION2)
                            .anyMatch(n -> Objects.equals(n.getTargetObjectLabel(), ACTION2))
                            .as(ACTION1 + " action flow compartment should contain a start node")
                            .anyMatch(n -> n.getStyle() instanceof ImageNodeStyle imageStyle && Objects.equals(imageStyle.getImageURL(), "images/start_action.svg")
                                    && Objects.equals("", n.getTargetObjectLabel()));

                    var optAction2Node = action1ActionFlowCompartment.get().getChildNodes().stream()
                            .filter(n -> Objects.equals(n.getTargetObjectLabel(), ACTION2))
                            .findFirst();
                    assertThat(optAction2Node).isPresent();
                    var action2Node = optAction2Node.get();
                    var action2ActionsCompartment = this.getCompartment(action2Node, "actions");
                    assertThat(action2ActionsCompartment)
                            .as(ACTION2 + " should contain an actions compartment")
                            .isPresent();
                    assertThat(action2ActionsCompartment.get().getChildNodes())
                            .as(ACTION2 + " actions compartment should contain 1 child")
                            .hasSize(1)
                            .as(ACTION2 + " actions compartment should contain " + ACTION3)
                            .anyMatch(n -> Objects.equals(n.getTargetObjectLabel(), ACTION3));

                    var action2ActionFlowCompartment = this.getCompartment(action2Node, "action flow");
                    assertThat(action2ActionFlowCompartment)
                            .as(ACTION2 + " should contain an action flow compartment")
                            .isPresent();
                    assertThat(action2ActionFlowCompartment.get().getChildNodes())
                            .as(ACTION2 + " action flow compartment should contain 1 child")
                            .hasSize(1)
                            .as(ACTION2 + " action flow compartment should contain " + ACTION3)
                            .anyMatch(n -> Objects.equals(n.getTargetObjectLabel(), ACTION3));

                }, () -> fail("Missing diagram"));
        this.verifier.consumeNextWith(updatedDiagramConsumer);
    }

    private Optional<Node> getCompartment(Node node, String compartmentName) {
        return node.getChildNodes().stream()
                .filter(n -> Objects.equals(n.getInsideLabel().getText(), compartmentName))
                .findFirst();
    }

}
