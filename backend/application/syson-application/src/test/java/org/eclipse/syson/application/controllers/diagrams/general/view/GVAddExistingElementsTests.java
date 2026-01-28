/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.GeneralViewAddExistingElementsTestProjectData;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

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
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private ToolTester nodeCreationTester;

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewAddExistingElementsTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewAddExistingElementsTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }


    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    @GivenSysONServer({ GeneralViewAddExistingElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void addExistingElementsOnDiagram() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewAddExistingElementsTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        String creationToolId = diagramDescriptionIdProvider.getDiagramCreationToolId("Add existing elements");
        assertThat(creationToolId).as("The tool 'Add existing elements' should exist on the diagram").isNotNull();

        Runnable nodeCreationRunner = () -> this.nodeCreationTester.invokeTool(GeneralViewAddExistingElementsTestProjectData.EDITING_CONTEXT_ID, diagram, creationToolId);

        Consumer<Object> updatedDiagramConsumer = assertRefreshedDiagramThat(newDiagram -> {
            assertThat(newDiagram.getNodes()).as("3 nodes should be visible on the diagram").hasSize(4);
            assertThat(newDiagram.getNodes())
                    .as(MessageFormat.format(NODE_SHOULD_BE_ON_DIAGRAM_MESSAGE, PACKAGE1))
                    .anyMatch(n -> Objects.equals(n.getTargetObjectLabel(), PACKAGE1))
                    .as(MessageFormat.format(NODE_SHOULD_BE_ON_DIAGRAM_MESSAGE, ACTION1))
                    .anyMatch(n -> Objects.equals(n.getTargetObjectLabel(), ACTION1))
                    .as(MessageFormat.format(NODE_SHOULD_BE_ON_DIAGRAM_MESSAGE, PART1))
                    .anyMatch(n -> Objects.equals(n.getTargetObjectLabel(), PART1))
                    .as(MessageFormat.format(NODE_SHOULD_BE_ON_DIAGRAM_MESSAGE, "RequirementUsage"))
                    .anyMatch(n -> Objects.equals(n.getTargetObjectLabel(), "RequirementUsage"));
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(nodeCreationRunner)
                .consumeNextWith(updatedDiagramConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));

    }

    @GivenSysONServer({ GeneralViewAddExistingElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void addExistingElementsRecursiveOnDiagram() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewAddExistingElementsTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        String creationToolId = diagramDescriptionIdProvider.getDiagramCreationToolId("Add existing elements (recursive)");
        assertThat(creationToolId).as("The tool 'Add existing elements (recursive)' should exist on the diagram").isNotNull();

        Runnable nodeCreationRunner = () -> this.nodeCreationTester.invokeTool(GeneralViewAddExistingElementsTestProjectData.EDITING_CONTEXT_ID, diagram, creationToolId);

        Consumer<Object> updatedDiagramConsumer = assertRefreshedDiagramThat(newDiagram -> {
            assertThat(newDiagram.getNodes()).as("6 nodes should be visible on the diagram").hasSize(7);
            assertThat(newDiagram.getEdges().stream().filter(e -> ViewModifier.Normal.equals(e.getState())).toList())
                    .as("3 edges should be visible on the diagram")
                    .hasSize(3)
                    .as("The diagram should contain a composite edge between part2 and part1")
                    .anyMatch(edge -> edge.getTargetObjectLabel().equals(PART1))
                    .as("The diagram should contain a composite edge between action1 and action2")
                    .anyMatch(edge -> edge.getTargetObjectLabel().equals(ACTION1))
                    .as("The diagram should contain a composite edge between action2 and action3")
                    .anyMatch(edge -> edge.getTargetObjectLabel().equals(ACTION2));
            assertThat(newDiagram.getNodes())
                    .as(MessageFormat.format(NODE_SHOULD_BE_ON_DIAGRAM_MESSAGE, PACKAGE1))
                    .anyMatch(n -> Objects.equals(n.getTargetObjectLabel(), PACKAGE1))
                    .as(MessageFormat.format(NODE_SHOULD_BE_ON_DIAGRAM_MESSAGE, ACTION1))
                    .anyMatch(n -> Objects.equals(n.getTargetObjectLabel(), ACTION1))
                    .as(MessageFormat.format(NODE_SHOULD_BE_ON_DIAGRAM_MESSAGE, PART1))
                    .anyMatch(n -> Objects.equals(n.getTargetObjectLabel(), PART1))
                    .as(MessageFormat.format(NODE_SHOULD_BE_ON_DIAGRAM_MESSAGE, PART2))
                    .anyMatch(n -> Objects.equals(n.getTargetObjectLabel(), PART2))
                    .as(MessageFormat.format(NODE_SHOULD_BE_ON_DIAGRAM_MESSAGE, "RequirementUsage"))
                    .anyMatch(n -> Objects.equals(n.getTargetObjectLabel(), "RequirementUsage"));

            this.checkPackageNode(newDiagram);

            // @technical-debt enable this part when start node will be synchronized
            // .as(ACTION1 + " action flow compartment should contain a start node")
            // .anyMatch(n -> n.getStyle() instanceof ImageNodeStyle imageStyle &&
            // Objects.equals(imageStyle.getImageURL(), "images/start_action.svg")
            // && Objects.equals("start", n.getTargetObjectLabel()));

            this.checkAction2(newDiagram);

            this.checkRequirementUsage(newDiagram);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(nodeCreationRunner)
                .consumeNextWith(updatedDiagramConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    private void checkPackageNode(Diagram newDiagram) {
        var optPackageNode = newDiagram.getNodes().stream()
                .filter(n -> Objects.equals(n.getTargetObjectLabel(), PACKAGE1))
                .findFirst();
        assertThat(optPackageNode).isPresent();
        assertThat(optPackageNode.get().getChildNodes())
                .as("Node " + PACKAGE1 + " should contain 1 child")
                .hasSize(1)
                .as("Node " + ATTRIBUTE_DEFINITION + " should exist inside " + PACKAGE1)
                .anyMatch(n -> Objects.equals(n.getTargetObjectLabel(), ATTRIBUTE_DEFINITION));
    }

    private void checkAction2(Diagram newDiagram) {
        var action1ActionFlowCompartment = this.checkAction1(newDiagram);
        var optAction2Node = action1ActionFlowCompartment.getChildNodes().stream()
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
    }

    private Node checkAction1(Diagram newDiagram) {
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
                .as(ACTION1 + " action flow compartment should contain 2 children")
                .hasSize(1) // @technical-debt should be 2 when start node will be synchronized
                .as(ACTION1 + " action flow compartment should contain " + ACTION2)
                .anyMatch(n -> Objects.equals(n.getTargetObjectLabel(), ACTION2));
        return action1ActionFlowCompartment.get();
    }

    private void checkRequirementUsage(Diagram newDiagram) {
        var optRequirementNode = newDiagram.getNodes().stream()
                .filter(n -> Objects.equals(n.getTargetObjectId(), GeneralViewAddExistingElementsTestProjectData.SemanticIds.SN_REQUIREMENT_ELEMENT_ID))
                .findFirst();

        assertThat(optRequirementNode).isPresent();
        assertThat(optRequirementNode.get().getChildNodes())
                .as("Node RequirementUsage should contain 6 children")
                .hasSize(8);

        var requirementDocCompartment = this.getCompartment(optRequirementNode.get(), "doc");
        assertThat(requirementDocCompartment).isPresent();
        assertThat(requirementDocCompartment.get())
                .as("The doc compartment should be visible")
                .matches(node -> !node.getModifiers().contains(ViewModifier.Hidden))
                .as("The doc compartment should contain a document item")
                .matches(node -> node.getChildNodes().size() == 1);
    }

    private Optional<Node> getCompartment(Node node, String compartmentName) {
        return node.getChildNodes().stream()
                .filter(n -> Objects.equals(n.getInsideLabel().getText(), compartmentName))
                .findFirst();
    }

}
