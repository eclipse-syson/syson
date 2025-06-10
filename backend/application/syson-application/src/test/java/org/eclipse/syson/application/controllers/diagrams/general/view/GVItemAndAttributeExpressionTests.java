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

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.tests.graphql.EditLabelMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.graphql.InitialDirectEditElementLabelQueryRunner;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controllers.diagrams.testers.DirectEditInitialLabelTester;
import org.eclipse.syson.application.controllers.diagrams.testers.DirectEditTester;
import org.eclipse.syson.application.data.GeneralViewItemAndAttributeProjectData;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
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
    private IGivenDiagramReference givenDiagram;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private InitialDirectEditElementLabelQueryRunner initialDirectEditElementLabelQueryRunner;

    @Autowired
    private EditLabelMutationRunner editLabelMutationRunner;

    private Step<DiagramRefreshedEventPayload> verifier;

    private AtomicReference<Diagram> diagram;

    private DirectEditInitialLabelTester directEditInitialLabelTester;

    private DirectEditTester directEditTester;

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.diagram = this.givenDiagram.getDiagram(this.verifier);
        this.directEditInitialLabelTester = new DirectEditInitialLabelTester(this.initialDirectEditElementLabelQueryRunner, GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID);
        this.directEditTester = new DirectEditTester(this.editLabelMutationRunner, GeneralViewItemAndAttributeProjectData.EDITING_CONTEXT_ID);
    }

    @AfterEach
    public void tearDown() {
        if (this.verifier != null) {
            this.verifier.thenCancel()
                    .verify(Duration.ofSeconds(10));
        }
    }

    @DisplayName("GIVEN a attribute, WHEN using a nested BinaryOperatorExpressions, THEN the FeatureValue is correctly set")
    @Test
    @Sql(scripts = { GeneralViewItemAndAttributeProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
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
        // Simple test
        this.directEditInitialLabelTester.checkDirectEditInitialLabelOnNode(this.verifier, this.diagram, GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_1_X1_ID,
                "x1");

        this.directEditTester.checkDirectEditInsideLabel(this.verifier, this.diagram, GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_1_X1_ID,
                "x1 = x2 + x3 + 4.5",
                "x1 = x2 + x3 + 4.5");

        // Using qualified name but keep pointing to attributes in P1_1
        this.directEditInitialLabelTester.checkDirectEditInitialLabelOnNode(this.verifier, this.diagram, GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_1_X1_ID,
                "x1 = x2 + x3 + 4.5");

        this.directEditTester.checkDirectEditInsideLabel(this.verifier, this.diagram, GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_1_X1_ID,
                "x1 = RootPackage::p1::p1_1::x3 - RootPackage::p1::p1_1::x2 - 4.5",
                "x1 = x3 - x2 - 4.5"); // We use simple name for label

        // Now change the target to attributes defined in P1
        this.directEditInitialLabelTester.checkDirectEditInitialLabelOnNode(this.verifier, this.diagram, GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_1_X1_ID,
                "x1 = x3 - x2 - 4.5");

        this.directEditTester.checkDirectEditInsideLabel(this.verifier, this.diagram, GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_1_X1_ID,
                "x1 = RootPackage::p1::x3 / RootPackage::p1::x3 * 10",
                "x1 = x3 / x3 * 10"); // We use simple name for label

        // Here the direct edit input need to explicitly give the qualified name of x3 since the default x3 is the one
        // located in p1_1 whereas we are targeting RootPackage::p1::x3
        this.directEditInitialLabelTester.checkDirectEditInitialLabelOnNode(this.verifier, this.diagram, GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_1_X1_ID,
                "x1 = p1::x3 / p1::x3 * 10");

    }

    @DisplayName("GIVEN a ItemUsage, WHEN using FeatureChainExpression to set the value, THEN the FeatureValue is correctly set")
    @Test
    @Sql(scripts = { GeneralViewItemAndAttributeProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void itemFeatureChain() {
        // Simple test on the bordered node representing a2_1
        this.directEditInitialLabelTester.checkDirectEditInitialLabelOnBorderedNode(this.verifier, this.diagram,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.A2_1_BORDERED_NODE_ID,
                "a2_1");

        this.directEditTester.checkDirectEditOutsideLabel(this.verifier, this.diagram,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.A2_1_BORDERED_NODE_ID,
                "a2_1 = a1.a1_2",
                "a2_1"); // Bordered not do not display the feature value

        // But the item in the compartment does
        this.directEditInitialLabelTester.checkDirectEditInitialLabelOnNode(this.verifier, this.diagram,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.A2_1_ICON_AND_LABEL_ID,
                "in a2_1 = a1.a1_2");

        // Simple test on the icon and label node representing a2_1
        this.directEditTester.checkDirectEditInsideLabel(this.verifier, this.diagram,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.A2_1_ICON_AND_LABEL_ID,
                "in a2_1 = a1.a1_3.i2_1.i3_1",
                "in a2_1 = a1.a1_3.i2_1.i3_1");
    }

    @DisplayName("GIVEN an AttributeUsage, WHEN using BracketExpression to set the value, THEN the FeatureValue is correctly set")
    @Test
    @Sql(scripts = { GeneralViewItemAndAttributeProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void attributeWithBranketExpression() {
        this.directEditInitialLabelTester.checkDirectEditInitialLabelOnNode(this.verifier, this.diagram,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_X1_ID,
                "x1");

        // Inside P1 there is no import so we need qualified name
        this.directEditTester.checkDirectEditInsideLabel(this.verifier, this.diagram,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_X1_ID,
                "a2_1 = 45 [SI::kg]",
                "a2_1 = 45 [kg]"); // Use short name for displaying bracket expression

        this.directEditInitialLabelTester.checkDirectEditInitialLabelOnNode(this.verifier, this.diagram,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_X1_ID,
                "a2_1 = 45 [kilogram]"); // We still need the qualified name or full name while editing

        // Inside P1_1 there is an import of S1::* so no need to use the qualified name
        // Test using qualified name
        this.directEditInitialLabelTester.checkDirectEditInitialLabelOnNode(this.verifier, this.diagram,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_1_X1_ID,
                "x1");

        // Inside P1 there is no import so we need qualified name
        this.directEditTester.checkDirectEditInsideLabel(this.verifier, this.diagram,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_1_X1_ID,
                "a2_1 = 45 [kg]",
                "a2_1 = 45 [kg]");

        this.directEditInitialLabelTester.checkDirectEditInitialLabelOnNode(this.verifier, this.diagram,
                GeneralViewItemAndAttributeProjectData.GraphicalIds.P1_1_X1_ID,
                "a2_1 = 45 [kilogram]");
    }
}
