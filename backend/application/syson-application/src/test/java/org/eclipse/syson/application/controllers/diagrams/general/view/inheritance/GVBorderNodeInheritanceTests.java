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

package org.eclipse.syson.application.controllers.diagrams.general.view.inheritance;

import java.util.stream.Stream;

import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.tests.api.GivenSysONServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * Tests the inheritance of nested elements represented as border nodes through specializations.
 * <p>
 *      The goal of this class is to test the inheritance of nested elements represented
 *      as border nodes in graphical nodes, when the owned element is specialized
 *      by another element.
 *      The super element contains a nested element viewed as a border node.
 *      A sub-element is created empty, and an inheritance specialization is established between
 *      the super and sub-elements.
 *      The test is composed of 4 parts:
 *     <ol>
 *         <li>
 *             Creation of the super element on GV diagram background.
 *         <li>
 *             Creation of the nested element inside the super element.
 *         <li>
 *             Creation of the sub element on GV diagram background.
 *         <li>
 *             Creation of the specialization edge between both super and sub elements.
 *     </ol>
 *     At the end, the border node of the inherited nested element should be found inside the sub element graphical node with the correct name.
 * </p>
 * <p>
 *     For instance:
 *     <ol>
 *         <li>
 *             Create a first ActionDefinition
 *         <li>
 *             Create a nested parameter inside this ActionDefinition
 *         <li>
 *             Create a second ActionDefinition
 *         <li>
 *             Create a Subclassification in between those ActionDefinition
 *     </ol>
 *     Check that the nested parameter border node is properly inherited in the second ActionDefinition.
 * </p>
 * To build a test case, we need to provide one test argument for each of the 4 parts described above.
 * <ul>
 *     <li>
 *         Super and sub elements are described using {@link ElementTestArgument}.
 *     <li>
 *         Nested element is described using {@link NestedElementTestArgument}.
 *     <li>
 *         Specialization is described using {@link SpecializationTestArgument}.
 * </ul>
 *
 * @author Jerome Gout
 */
@Transactional
@GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVBorderNodeInheritanceTests extends AbstractIntegrationTests {

    private static final ElementTestArgument ACTION_DEFINITION_ARGUMENT = new ElementTestArgument(SysmlPackage.eINSTANCE.getActionDefinition(), "New Action Definition");

    private static final ElementTestArgument ACTION_USAGE_ARGUMENT = new ElementTestArgument(SysmlPackage.eINSTANCE.getActionUsage(), "New Action");

    private static final SpecializationTestArgument SUBCLASSIFICATION_ARGUMENT = new SpecializationTestArgument("New Subclassification");

    private static final SpecializationTestArgument REFERENCE_SUBSETTING_ARGUMENT = new SpecializationTestArgument("New Reference Subsetting");

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private GraphicalInheritanceTestRunner testExecutor;

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }


    @DisplayName("GIVEN a base Element with a nested Element as a border node, WHEN another Element is specializing the base Element, THEN the sub Element nested Element is inherited from the base Element as a border node")
    @ParameterizedTest(name = "Check that {1} compartment of {0} is inherited in {2} via {3}")
    @MethodSource("parametersBorderNodeInheritanceArguments")
    public void checkBorderNodeItemInheritance(ElementTestArgument superElement, NestedElementTestArgument nestedElement, ElementTestArgument subElement, SpecializationTestArgument specialization) {
        this.testExecutor
                .superElementEClass(superElement.eClass())
                .superElementCreationToolName(superElement.creationToolName())
                .superElementExpectedBorderNodes(superElement.expectedBorderNodes())
                .superElementExpectedNodes(superElement.expectedNodes())
                .nestedElementCreationToolName(nestedElement.creationToolName())
                .nestedElementReferencedElementNodeId(nestedElement.referencedNodeId())
                .compartmentName(nestedElement.compartmentName())
                .nestedElementExpectedName(nestedElement.expectedName())
                .nestedElementExpectedBorderNodes(nestedElement.expectedBorderNodes())
                .nestedElementExpectedNodes(nestedElement.expectedNodes())
                .nestedElementExpectedEdges(nestedElement.expectedEdges())
                .subElementEClass(subElement.eClass())
                .subElementCreationToolName(subElement.creationToolName())
                .subElementExpectedBorderNodes(subElement.expectedBorderNodes())
                .subElementExpectedNodes(subElement.expectedNodes())
                .subElementExpectedEdges(subElement.expectedEdges())
                .specializationCreationToolName(specialization.creationToolName())
                .specializationExpectedBorderNodes(specialization.expectedBorderNodes())
                .specializationExpectedNodes(specialization.expectedNodes())
                .specializationExpectedEdges(specialization.expectedEdges())
                .run();
    }

    private static Stream<Arguments> parametersBorderNodeInheritanceArguments() {
        return Stream.of(
                Arguments.of(ACTION_DEFINITION_ARGUMENT,
                        new NestedElementTestArgument("New Parameter In", "parameters", "in ref parameter1", "", 1, 1, 0),
                        ACTION_DEFINITION_ARGUMENT, SUBCLASSIFICATION_ARGUMENT.withExtraBorderNodes(1)),
                Arguments.of(ACTION_USAGE_ARGUMENT,
                        new NestedElementTestArgument("New Parameter Out", "parameters", "out ref parameter1", "", 1, 1, 0),
                        ACTION_USAGE_ARGUMENT, REFERENCE_SUBSETTING_ARGUMENT.withExtraBorderNodes(1))
        );
    }
}
