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

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Edge;
import org.eclipse.sirius.components.diagrams.InsideLabel;
import org.eclipse.sirius.components.diagrams.OutsideLabel;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.diagrams.tests.graphql.EditLabelMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.graphql.InitialDirectEditElementLabelQueryRunner;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controllers.diagrams.testers.DeleteToolRunner;
import org.eclipse.syson.application.controllers.diagrams.testers.DeleteToolTester;
import org.eclipse.syson.application.controllers.diagrams.testers.DirectEditInitialLabelTester;
import org.eclipse.syson.application.controllers.diagrams.testers.DirectEditTester;
import org.eclipse.syson.application.data.GeneralViewItemAndAttributeProjectData;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Test labels and direct edit on FeatureValue with Expression using project "GeneralView-ItemAndAttributesModelTest".
 *
 * @author Arthur Daussy
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVItemAndAttributeExpressionTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private InitialDirectEditElementLabelQueryRunner initialDirectEditElementLabelQueryRunner;

    @Autowired
    private EditLabelMutationRunner editLabelMutationRunner;

    @Autowired
    private DiagramComparator diagramComparator;

    @Autowired
    private DeleteToolRunner deleteFromDiagramRunner;

    private DirectEditInitialLabelTester directEditInitialLabelTester;

    private DirectEditTester directEditTester;

    private DeleteToolTester deleteFromDiagramTester;

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(), GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID, GeneralViewItemAndAttributeProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        this.directEditInitialLabelTester = new DirectEditInitialLabelTester(this.initialDirectEditElementLabelQueryRunner, GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID);
        this.directEditTester = new DirectEditTester(this.editLabelMutationRunner, GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID);
        this.deleteFromDiagramTester = new DeleteToolTester(this.deleteFromDiagramRunner, GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.DIAGRAM_ID);
    }

    @DisplayName("GIVEN a attribute, WHEN using a nested BinaryOperatorExpressions, THEN the FeatureValue is correctly set")
    @Test
    @GivenSysONServer({ GeneralViewItemAndAttributeProjectData.SCRIPT_PATH })
    public void nestedBinaryOperatorExpressionWithNameOverlapping() {
        /**
         * We are playing with this part of the model
         *
         * <pre>
            package RootPackage {
               ...
                part p1 {
                    attribute x1;
                    attribute x2;
                    attribute x3;

                    part p1_1 {
                        attribute x1;
                        attribute x2;
                        attribute x3;
                    }
                }
            }
         * </pre>
         */

        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        // Simple test
        Runnable directEditInitialLabelCheck = this.directEditInitialLabelTester.checkDirectEditInitialLabelOnNode(diagram, GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_1_X1_ID,
                "x1");

        Runnable directEditInsideLabelCheck = this.directEditTester.directEditInsideLabel(diagram, GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_1_X1_ID, "x1 = x2 + x3 + 4.5");
        Consumer<Object> directEditInsideLabelDiagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            DiagramNavigator diagramNavigator = new DiagramNavigator(newDiagram);

            InsideLabel newLabel = diagramNavigator.nodeWithId(GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_1_X1_ID).getNode().getInsideLabel();
            assertThat(newLabel.getText()).isEqualTo("x1 = x2 + x3 + 4.5");
        });

        // Using qualified name but keep pointing to attributes in P1_1
        Runnable directEditInitialLabelCheck2 = this.directEditInitialLabelTester.checkDirectEditInitialLabelOnNode(diagram, GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_1_X1_ID,
                "x1 = x2 + x3 + 4.5");

        Runnable directEditInsideLabelDiagramCheck2 = this.directEditTester.directEditInsideLabel(diagram, GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_1_X1_ID,
                "x1 = RootPackage::p1::p1_1::x3 - RootPackage::p1::p1_1::x2 - 4.5"); // We use simple name for label
        Consumer<Object> directEditInsideLabelDiagramCheck3 = assertRefreshedDiagramThat(newDiagram -> {
            DiagramNavigator diagramNavigator = new DiagramNavigator(newDiagram);

            InsideLabel newLabel = diagramNavigator.nodeWithId(GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_1_X1_ID).getNode().getInsideLabel();
            assertThat(newLabel.getText()).isEqualTo("x1 = x3 - x2 - 4.5");
        });

        // Now change the target to attributes defined in P1
        Runnable directEditInitialLabelCheck3 = this.directEditInitialLabelTester.checkDirectEditInitialLabelOnNode(diagram, GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_1_X1_ID,
                "x1 = x3 - x2 - 4.5");

        Runnable directEditInsideLabelCheck4 = this.directEditTester.directEditInsideLabel(diagram, GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_1_X1_ID,
                "x1 = RootPackage::p1::x3 / RootPackage::p1::x3 * 10");
        Consumer<Object> directEditInsideLabelDiagramCheck4 = assertRefreshedDiagramThat(newDiagram -> {
            DiagramNavigator diagramNavigator = new DiagramNavigator(newDiagram);

            InsideLabel newLabel = diagramNavigator.nodeWithId(GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_1_X1_ID).getNode().getInsideLabel();
            assertThat(newLabel.getText()).isEqualTo("x1 = x3 / x3 * 10"); // We use simple name for label
        });

        // Here the direct edit input needs to explicitly give the qualified name of x3 since the default x3 is the one
        // located in p1_1 whereas we are targeting RootPackage::p1::x3
        Runnable directEditInitialLabelCheck5 = this.directEditInitialLabelTester.checkDirectEditInitialLabelOnNode(diagram, GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_1_X1_ID,
                "x1 = RootPackage::p1::x3 / RootPackage::p1::x3 * 10");

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(directEditInitialLabelCheck)
                .then(directEditInsideLabelCheck)
                .consumeNextWith(directEditInsideLabelDiagramCheck)
                .then(directEditInitialLabelCheck2)
                .then(directEditInsideLabelDiagramCheck2)
                .consumeNextWith(directEditInsideLabelDiagramCheck3)
                .then(directEditInitialLabelCheck3)
                .then(directEditInsideLabelCheck4)
                .consumeNextWith(directEditInsideLabelDiagramCheck4)
                .then(directEditInitialLabelCheck5)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a ItemUsage, WHEN using FeatureChainExpression to set the value, THEN the FeatureValue is correctly set")
    @Test
    @GivenSysONServer({ GeneralViewItemAndAttributeProjectData.SCRIPT_PATH })
    public void payloadFeatureChain() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        // Simple test on the bordered node representing a2_1
        Runnable directEditInitialLabelCheck1 = this.directEditInitialLabelTester.checkDirectEditInitialLabelOnBorderedNode(diagram,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.A2_1_BORDERED_NODE_ID,
                "a2_1");

        Runnable directEditOutsideLabelCheck1 = this.directEditTester.directEditOutsideLabel(diagram,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.A2_1_BORDERED_NODE_ID,
                "a2_1 = a1.a1_2");
        Consumer<Object> directEditOutsideLabelDiagramCheck1 = assertRefreshedDiagramThat(newDiagram -> {
            DiagramNavigator diagramNavigator = new DiagramNavigator(newDiagram);
            OutsideLabel newLabel = diagramNavigator.nodeWithId(GeneralViewItemAndAttributeProjectData.GraphicalIds.A2_1_BORDERED_NODE_ID).getNode().getOutsideLabels().get(0);
            // Bordered node do not display the feature value in their label
            assertThat(newLabel.text()).isEqualTo("a2_1");
        });

        // But the item in the compartment does
        Runnable directEditInitialLabelCheck2 = this.directEditInitialLabelTester.checkDirectEditInitialLabelOnNode(diagram,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.A2_1_ICON_AND_LABEL_ID,
                "in a2_1 = a1.a1_2");

        // Simple test on the icon and label node representing a2_1
        Runnable directEditInsideLabelCheck2 = this.directEditTester.directEditInsideLabel(diagram,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.A2_1_ICON_AND_LABEL_ID,
                "in a2_1 = a1.a1_3.i2_1.i3_1");
        Consumer<Object> directEditInsideLabelDiagramCheck2 = assertRefreshedDiagramThat(newDiagram -> {
            DiagramNavigator diagramNavigator = new DiagramNavigator(newDiagram);
            InsideLabel newLabel = diagramNavigator.nodeWithId(GeneralViewItemAndAttributeProjectData.GraphicalIds.A2_1_ICON_AND_LABEL_ID).getNode().getInsideLabel();
            assertThat(newLabel.getText()).isEqualTo("in a2_1 = a1.a1_3.i2_1.i3_1");
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(directEditInitialLabelCheck1)
                .then(directEditOutsideLabelCheck1)
                .consumeNextWith(directEditOutsideLabelDiagramCheck1)
                .then(directEditInitialLabelCheck2)
                .then(directEditInsideLabelCheck2)
                .consumeNextWith(directEditInsideLabelDiagramCheck2)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN an AttributeUsage, WHEN using BracketExpression to set the value, THEN the FeatureValue is correctly set")
    @Test
    @GivenSysONServer({ GeneralViewItemAndAttributeProjectData.SCRIPT_PATH })
    public void attributeWithBranketExpression() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable directEditInitialLabelCheck1 = this.directEditInitialLabelTester.checkDirectEditInitialLabelOnNode(diagram,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_X1_ID,
                "x1");

        // Inside P1 there is no import so we need qualified name
        Runnable directEditInsideLabelCheck1 = this.directEditTester.directEditInsideLabel(diagram,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_X1_ID,
                "a2_1 = 45 [SI::kg]");
        Consumer<Object> directEditInsideLabelDiagramCheck1 = assertRefreshedDiagramThat(newDiagram -> {
            DiagramNavigator diagramNavigator = new DiagramNavigator(newDiagram);
            InsideLabel newLabel = diagramNavigator.nodeWithId(GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_X1_ID).getNode().getInsideLabel();
            assertThat(newLabel.getText()).isEqualTo("a2_1 = 45 [kg]"); // Use short name for displaying bracket expression
        });

        Runnable directEditInitialLabelCheck2 = this.directEditInitialLabelTester.checkDirectEditInitialLabelOnNode(diagram,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_X1_ID,
                "a2_1 = 45 [kg]");

        // Inside P1_1 there is an import of S1::* so no need to use the qualified name
        // Test using qualified name
        Runnable directEditInitialLabelCheck3 = this.directEditInitialLabelTester.checkDirectEditInitialLabelOnNode(diagram,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_1_X1_ID,
                "x1");

        // Inside P1 there is no import so we need qualified name
        Runnable directEditInsideLabelCheck3 = this.directEditTester.directEditInsideLabel(diagram,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_1_X1_ID,
                "a2_1 = 45 [kg]");
        Consumer<Object> directEditInsideLabelDiagramCheck3 = assertRefreshedDiagramThat(newDiagram -> {
            DiagramNavigator diagramNavigator = new DiagramNavigator(newDiagram);
            InsideLabel newLabel = diagramNavigator.nodeWithId(GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_1_X1_ID).getNode().getInsideLabel();
            assertThat(newLabel.getText()).isEqualTo("a2_1 = 45 [kg]");
        });

        Runnable directEditInitialLabelCheck4 = this.directEditInitialLabelTester.checkDirectEditInitialLabelOnNode(diagram,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_1_X1_ID,
                "a2_1 = 45 [kg]");

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(directEditInitialLabelCheck1)
                .then(directEditInsideLabelCheck1)
                .consumeNextWith(directEditInsideLabelDiagramCheck1)
                .then(directEditInitialLabelCheck2)
                .then(directEditInitialLabelCheck3)
                .then(directEditInsideLabelCheck3)
                .consumeNextWith(directEditInsideLabelDiagramCheck3)
                .then(directEditInitialLabelCheck4)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN an ItemUsage, WHEN direct editing with a value referencing another ItemUsage, THEN an edge should connect the ItemUsage")
    @Test
    @GivenSysONServer({ GeneralViewItemAndAttributeProjectData.SCRIPT_PATH })
    public void payloadFeatureChainBindingEdge() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        // Create an edge using direct edit
        Runnable directEditInsideLabelCheck1 = this.directEditTester.directEditInsideLabel(diagram,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.A2_1_ICON_AND_LABEL_ID,
                "in a2_1 = a1.a1_1");
        Consumer<Object> diagramCheck1 = this.buildEdgeChecker(diagram, GeneralViewItemAndAttributeProjectData.GraphicalIds.A2_1_ICON_AND_LABEL_ID, "in a2_1 = a1.a1_1",
                GeneralViewItemAndAttributeProjectData.GraphicalIds.A2_1_BORDERED_NODE_ID, GeneralViewItemAndAttributeProjectData.GraphicalIds.A1_1_BORDERED_NODE_ID);

        // Change the edge to a new target
        Runnable directEditInsideLabelCheck2 = this.directEditTester.directEditInsideLabel(diagram,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.A2_1_ICON_AND_LABEL_ID,
                "in a2_1 = a1.a1_2");
        Consumer<Object> diagramCheck2 = this.buildEdgeChecker(diagram, GeneralViewItemAndAttributeProjectData.GraphicalIds.A2_1_ICON_AND_LABEL_ID, "in a2_1 = a1.a1_2",
                GeneralViewItemAndAttributeProjectData.GraphicalIds.A2_1_BORDERED_NODE_ID, GeneralViewItemAndAttributeProjectData.GraphicalIds.A1_2_BORDERED_NODE_ID);

        // Remove edge
        Runnable directEditInsideLabelCheck3 = this.directEditTester.directEditInsideLabel(diagram,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.A2_1_ICON_AND_LABEL_ID,
                "in a2_1 =");
        Consumer<Object> diagramCheck3 = this.buildNoEdgeStartingFromChecker(diagram, GeneralViewItemAndAttributeProjectData.GraphicalIds.A2_1_ICON_AND_LABEL_ID, "in a2_1",
                GeneralViewItemAndAttributeProjectData.GraphicalIds.A2_1_BORDERED_NODE_ID);

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(directEditInsideLabelCheck1)
                .consumeNextWith(diagramCheck1)
                .then(directEditInsideLabelCheck2)
                .consumeNextWith(diagramCheck2)
                .then(directEditInsideLabelCheck3)
                .consumeNextWith(diagramCheck3)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN an ItemUsage, WHEN deleting an edge representing a FeatureValue, THEN the FeatureValue should be deleted and the label of the ItemUsage should be updated updated")
    @Test
    @GivenSysONServer({ GeneralViewItemAndAttributeProjectData.SCRIPT_PATH })
    public void deleteFeatureValueEdge() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable deleteTool = this.deleteFromDiagramTester.checkDeleteTool(List.of(), List.of(GeneralViewItemAndAttributeProjectData.GraphicalIds.FEATURE_VALUE_A2_2_TO_A1_4_EDGE));

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            // Check label no more FeatureValue (the = part is gone)
            DiagramNavigator diagramNavigator = new DiagramNavigator(newDiagram);
            InsideLabel newLabel = diagramNavigator.nodeWithId(GeneralViewItemAndAttributeProjectData.GraphicalIds.A2_2_ICON_AND_LABEL_ID).getNode().getInsideLabel();
            assertThat(newLabel.getText()).isEqualTo("out a2_2");

            // No more edge starting from a2_2
            assertThat(newDiagram.getEdges()).noneMatch(s -> GeneralViewItemAndAttributeProjectData.GraphicalIds.A2_2_BORDERED_NODE_ID.equals(s.getSourceId()));
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(deleteTool)
                .consumeNextWith(diagramCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    private Consumer<Object> buildEdgeChecker(AtomicReference<Diagram> diagram, String nodeToCheckForLabel, String expectedLabel, String sourceNodeId, String targetNodeId) {
        return assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            // Check label
            DiagramNavigator diagramNavigator = new DiagramNavigator(newDiagram);
            InsideLabel newLabel = diagramNavigator.nodeWithId(nodeToCheckForLabel).getNode().getInsideLabel();
            assertThat(newLabel.getText()).isEqualTo(expectedLabel);

            // Check new edges
            List<Edge> newEdges = this.diagramComparator.newEdges(initialDiagram, newDiagram).stream().filter(e -> ViewModifier.Normal.equals(e.getState())).toList();
            assertThat(newEdges).hasSize(1)
                    .first()
                    .satisfies(edge -> {
                        assertThat(edge.getSourceId()).as("Should start from A2_1").isEqualTo(sourceNodeId);
                    }, edge -> {
                        assertThat(edge.getTargetId()).as("Should end to A1_1 or A1_2 depending on the test").isEqualTo(targetNodeId);
                    });
        });
    }

    private Consumer<Object> buildNoEdgeStartingFromChecker(AtomicReference<Diagram> diagram, String nodeToCheckForLabel, String expectedLabel, String sourceNodeId) {
        return assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            // Check label
            DiagramNavigator diagramNavigator = new DiagramNavigator(newDiagram);
            InsideLabel newLabel = diagramNavigator.nodeWithId(nodeToCheckForLabel).getNode().getInsideLabel();
            assertThat(newLabel.getText()).isEqualTo(expectedLabel);

            // Check there is starting from the given source
            List<Edge> newEdges = this.diagramComparator.newEdges(initialDiagram, newDiagram);

            assertThat(newDiagram.getEdges()).noneMatch(s -> sourceNodeId.equals(s.getSourceId()));
            assertThat(newEdges).hasSize(0);
        });
    }
}
