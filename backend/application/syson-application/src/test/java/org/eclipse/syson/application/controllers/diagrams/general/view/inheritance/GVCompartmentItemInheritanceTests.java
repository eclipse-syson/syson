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
 * Tests the inheritance of compartment items through specializations.
 * <p>
 *      The goal of this class is to test the inheritance of nested elements represented as
 *      compartment items in graphical nodes, when the owned element is specialized
 *      by another element.
 *      The super element contains a nested element inside a compartment. A sub-element
 *      is created empty, and an inheritance specialization is established between
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
 *     At the end, the inherited nested element should be found inside the compartment of the sub element with the correct name.
 * </p>
 * <p>
 *     For instance:
 *     <ol>
 *         <li>
 *             Create a first ActionDefinition
 *         <li>
 *             Create a nested ActionUsage inside this ActionDefinition
 *         <li>
 *             Create a second ActionDefinition
 *         <li>
 *             Create a Subclassification in between those ActionDefinition
 *     </ol>
 *     Check that the nested action is properly inherited in the second ActionDefinition
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
public class GVCompartmentItemInheritanceTests extends AbstractIntegrationTests {

    private static final ElementTestArgument ACTION_DEFINITION_ARGUMENT = new ElementTestArgument(SysmlPackage.eINSTANCE.getActionDefinition(), "New Action Definition");

    private static final ElementTestArgument ACTION_USAGE_ARGUMENT = new ElementTestArgument(SysmlPackage.eINSTANCE.getActionUsage(), "New Action");

    private static final ElementTestArgument PART_USAGE_ARGUMENT = new ElementTestArgument(SysmlPackage.eINSTANCE.getPartUsage(), "New Part");

    private static final ElementTestArgument PART_DEFINITION_ARGUMENT = new ElementTestArgument(SysmlPackage.eINSTANCE.getPartDefinition(), "New Part Definition");

    private static final ElementTestArgument STATE_USAGE_ARGUMENT = new ElementTestArgument(SysmlPackage.eINSTANCE.getStateUsage(), "New State");

    private static final ElementTestArgument STATE_DEFINITION_ARGUMENT = new ElementTestArgument(SysmlPackage.eINSTANCE.getStateDefinition(), "New State Definition");

    private static final ElementTestArgument PORT_USAGE_ARGUMENT = new ElementTestArgument(SysmlPackage.eINSTANCE.getPortUsage(), "New Port");

    private static final ElementTestArgument PORT_DEFINITION_ARGUMENT = new ElementTestArgument(SysmlPackage.eINSTANCE.getPortDefinition(), "New Port Definition");

    private static final ElementTestArgument REQUIREMENT_USAGE_ARGUMENT = new ElementTestArgument(SysmlPackage.eINSTANCE.getRequirementUsage(), "New Requirement");

    private static final ElementTestArgument REQUIREMENT_DEFINITION_ARGUMENT = new ElementTestArgument(SysmlPackage.eINSTANCE.getRequirementDefinition(), "New Requirement Definition");

    private static final ElementTestArgument SATISFY_REQUIREMENT_USAGE_ARGUMENT = new ElementTestArgument(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), "New Satisfy Requirement");

    private static final ElementTestArgument CONCERN_USAGE_ARGUMENT = new ElementTestArgument(SysmlPackage.eINSTANCE.getConcernUsage(), "New Concern");

    private static final ElementTestArgument CONCERN_DEFINITION_ARGUMENT = new ElementTestArgument(SysmlPackage.eINSTANCE.getConcernDefinition(), "New Concern Definition");

    private static final ElementTestArgument CASE_USAGE_ARGUMENT = new ElementTestArgument(SysmlPackage.eINSTANCE.getCaseUsage(), "New Case");

    private static final ElementTestArgument CASE_DEFINITION_ARGUMENT = new ElementTestArgument(SysmlPackage.eINSTANCE.getCaseDefinition(), "New Case Definition");

    private static final SpecializationTestArgument SUBCLASSIFICATION_ARGUMENT = new SpecializationTestArgument("New Subclassification");

    private static final SpecializationTestArgument FEATURE_TYPING_ARGUMENT = new SpecializationTestArgument("New Feature Typing");

    private static final SpecializationTestArgument SUBSETTING_ARGUMENT = new SpecializationTestArgument("New Subsetting");

    private static final SpecializationTestArgument REFERENCE_SUBSETTING_ARGUMENT = new SpecializationTestArgument("New Reference Subsetting");

    private static final SpecializationTestArgument REDEFINITION_ARGUMENT = new SpecializationTestArgument("New Redefinition");

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private GraphicalInheritanceTestRunner testExecutor;

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a base Element with a nested Element, WHEN another Element is specializing the base Element, THEN the sub Element nested Element is inherited from the base Element")
    @ParameterizedTest(name = "Check that {1} compartment of {0} is inherited in {2} via {3}")
    @MethodSource({ "actionsCompartmentItemInheritanceArguments",
                    "itemsCompartmentItemInheritanceArguments",
                    "statesCompartmentItemInheritanceArguments",
                    "exhibitStatesCompartmentItemInheritanceArguments",
                    "performActionsCompartmentItemInheritanceArguments",
                    "assumeConstraintsCompartmentItemInheritanceArguments",
                    "requireConstraintsCompartmentItemInheritanceArguments",
                    "framesCompartmentItemInheritanceArguments",
                    "satisfyRequirementsCompartmentItemInheritanceArguments",
                    "stakeholdersCompartmentItemInheritanceArguments",
                    "objectiveCompartmentItemInheritanceArguments",
                    "actorsCompartmentItemInheritanceArguments",
                    "subjectCompartmentItemInheritanceArguments" })
    public void checkCompartmentItemInheritance(ElementTestArgument superElement, NestedElementTestArgument nestedElement, ElementTestArgument subElement, SpecializationTestArgument specialization) {
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

    private static Stream<Arguments> actionsCompartmentItemInheritanceArguments() {
        NestedElementTestArgument nestedAction = new NestedElementTestArgument("New Action", "actions", "action1");
        return Stream.of(
                Arguments.of(ACTION_DEFINITION_ARGUMENT, nestedAction, ACTION_USAGE_ARGUMENT, FEATURE_TYPING_ARGUMENT),
                Arguments.of(ACTION_DEFINITION_ARGUMENT, nestedAction, ACTION_DEFINITION_ARGUMENT, SUBCLASSIFICATION_ARGUMENT),
                Arguments.of(ACTION_USAGE_ARGUMENT, nestedAction, ACTION_USAGE_ARGUMENT, SUBSETTING_ARGUMENT),
                Arguments.of(ACTION_USAGE_ARGUMENT, nestedAction, ACTION_USAGE_ARGUMENT, REFERENCE_SUBSETTING_ARGUMENT),
                Arguments.of(ACTION_USAGE_ARGUMENT, nestedAction, ACTION_USAGE_ARGUMENT, REDEFINITION_ARGUMENT)
        );
    }

    private static Stream<Arguments> itemsCompartmentItemInheritanceArguments() {
        NestedElementTestArgument nestedItem = new NestedElementTestArgument("New Item", "items", "item1");
        NestedElementTestArgument nestedItemWithBorderNode = new NestedElementTestArgument("New Item", "items", "item1", "", 1, 1, 0);
        return Stream.of(
                Arguments.of(ACTION_DEFINITION_ARGUMENT, nestedItem, ACTION_USAGE_ARGUMENT, FEATURE_TYPING_ARGUMENT),
                Arguments.of(ACTION_DEFINITION_ARGUMENT, nestedItem, ACTION_DEFINITION_ARGUMENT, SUBCLASSIFICATION_ARGUMENT),
                Arguments.of(ACTION_USAGE_ARGUMENT, nestedItem, ACTION_USAGE_ARGUMENT, SUBSETTING_ARGUMENT),
                Arguments.of(ACTION_USAGE_ARGUMENT, nestedItem, ACTION_USAGE_ARGUMENT, REFERENCE_SUBSETTING_ARGUMENT),
                Arguments.of(ACTION_USAGE_ARGUMENT, nestedItem, ACTION_USAGE_ARGUMENT, REDEFINITION_ARGUMENT),
                Arguments.of(PORT_USAGE_ARGUMENT, nestedItemWithBorderNode, PORT_USAGE_ARGUMENT, SUBSETTING_ARGUMENT),
                Arguments.of(PORT_USAGE_ARGUMENT, nestedItemWithBorderNode, PORT_USAGE_ARGUMENT, REFERENCE_SUBSETTING_ARGUMENT),
                Arguments.of(PORT_DEFINITION_ARGUMENT, nestedItemWithBorderNode, PORT_DEFINITION_ARGUMENT, SUBCLASSIFICATION_ARGUMENT)
        );
    }

    private static Stream<Arguments> statesCompartmentItemInheritanceArguments() {
        NestedElementTestArgument nestedState = new NestedElementTestArgument("New State", "states", "state1");
        return Stream.of(
                Arguments.of(PART_DEFINITION_ARGUMENT, nestedState, PART_DEFINITION_ARGUMENT, SUBCLASSIFICATION_ARGUMENT),
                Arguments.of(PART_DEFINITION_ARGUMENT, nestedState, PART_USAGE_ARGUMENT, FEATURE_TYPING_ARGUMENT),
                Arguments.of(PART_USAGE_ARGUMENT, nestedState, PART_USAGE_ARGUMENT, REDEFINITION_ARGUMENT)
        );
    }

    private static Stream<Arguments> exhibitStatesCompartmentItemInheritanceArguments() {
        NestedElementTestArgument nestedExhibitState = new NestedElementTestArgument("New Exhibit State", "exhibit states", "ref  ::> state", GeneralViewWithTopNodesTestProjectData.SemanticIds.STATE_USAGE_ID, 0, 1, 2);
        return Stream.of(
                Arguments.of(PART_DEFINITION_ARGUMENT, nestedExhibitState, PART_DEFINITION_ARGUMENT, SUBCLASSIFICATION_ARGUMENT),
                Arguments.of(STATE_DEFINITION_ARGUMENT, nestedExhibitState, STATE_USAGE_ARGUMENT, FEATURE_TYPING_ARGUMENT),
                Arguments.of(STATE_USAGE_ARGUMENT, nestedExhibitState, STATE_USAGE_ARGUMENT, REDEFINITION_ARGUMENT)
        );
    }

    private static Stream<Arguments> performActionsCompartmentItemInheritanceArguments() {
        return Stream.of(
                Arguments.of(ACTION_DEFINITION_ARGUMENT, new NestedElementTestArgument("New Perform Action", "perform actions", "ref "), ACTION_DEFINITION_ARGUMENT, SUBCLASSIFICATION_ARGUMENT),
                Arguments.of(STATE_DEFINITION_ARGUMENT, new NestedElementTestArgument("New Do Action", "perform actions", "ref do ::> action", GeneralViewWithTopNodesTestProjectData.SemanticIds.ACTION_USAGE_ID, 0, 1, 0),
                        STATE_USAGE_ARGUMENT, FEATURE_TYPING_ARGUMENT),
                Arguments.of(STATE_USAGE_ARGUMENT, new NestedElementTestArgument("New Entry Action", "perform actions", "ref entry ::> action", GeneralViewWithTopNodesTestProjectData.SemanticIds.ACTION_USAGE_ID, 0, 1, 0),
                        PART_USAGE_ARGUMENT, REFERENCE_SUBSETTING_ARGUMENT)
        );
    }

    private static Stream<Arguments> assumeConstraintsCompartmentItemInheritanceArguments() {
        NestedElementTestArgument nestedAssumeConstraint = new NestedElementTestArgument("New Assume constraint", "assume constraints", "constraint1");
        return Stream.of(
                Arguments.of(REQUIREMENT_DEFINITION_ARGUMENT, nestedAssumeConstraint, REQUIREMENT_DEFINITION_ARGUMENT, SUBCLASSIFICATION_ARGUMENT),
                Arguments.of(REQUIREMENT_DEFINITION_ARGUMENT, nestedAssumeConstraint, REQUIREMENT_USAGE_ARGUMENT, FEATURE_TYPING_ARGUMENT),
                Arguments.of(REQUIREMENT_USAGE_ARGUMENT, nestedAssumeConstraint, REQUIREMENT_USAGE_ARGUMENT, REDEFINITION_ARGUMENT),
                Arguments.of(REQUIREMENT_USAGE_ARGUMENT, nestedAssumeConstraint, REQUIREMENT_USAGE_ARGUMENT, SUBSETTING_ARGUMENT),
                Arguments.of(REQUIREMENT_USAGE_ARGUMENT, nestedAssumeConstraint, REQUIREMENT_USAGE_ARGUMENT, REFERENCE_SUBSETTING_ARGUMENT),
                Arguments.of(SATISFY_REQUIREMENT_USAGE_ARGUMENT, nestedAssumeConstraint, SATISFY_REQUIREMENT_USAGE_ARGUMENT, REDEFINITION_ARGUMENT),
                Arguments.of(SATISFY_REQUIREMENT_USAGE_ARGUMENT, nestedAssumeConstraint, SATISFY_REQUIREMENT_USAGE_ARGUMENT, SUBSETTING_ARGUMENT),
                Arguments.of(SATISFY_REQUIREMENT_USAGE_ARGUMENT, nestedAssumeConstraint, SATISFY_REQUIREMENT_USAGE_ARGUMENT, REFERENCE_SUBSETTING_ARGUMENT),
                Arguments.of(CONCERN_DEFINITION_ARGUMENT, nestedAssumeConstraint, CONCERN_DEFINITION_ARGUMENT, SUBCLASSIFICATION_ARGUMENT),
                Arguments.of(CONCERN_DEFINITION_ARGUMENT, nestedAssumeConstraint, CONCERN_USAGE_ARGUMENT, FEATURE_TYPING_ARGUMENT),
                Arguments.of(CONCERN_USAGE_ARGUMENT, nestedAssumeConstraint, CONCERN_USAGE_ARGUMENT, REDEFINITION_ARGUMENT),
                Arguments.of(CONCERN_USAGE_ARGUMENT, nestedAssumeConstraint, CONCERN_USAGE_ARGUMENT, SUBSETTING_ARGUMENT),
                Arguments.of(CONCERN_USAGE_ARGUMENT, nestedAssumeConstraint, CONCERN_USAGE_ARGUMENT, REFERENCE_SUBSETTING_ARGUMENT)
        );
    }

    private static Stream<Arguments> requireConstraintsCompartmentItemInheritanceArguments() {
        NestedElementTestArgument nestedRequireConstraint = new NestedElementTestArgument("New Require constraint", "require constraints", "constraint1");
        return Stream.of(
                Arguments.of(REQUIREMENT_DEFINITION_ARGUMENT, nestedRequireConstraint, REQUIREMENT_DEFINITION_ARGUMENT, SUBCLASSIFICATION_ARGUMENT),
                Arguments.of(REQUIREMENT_DEFINITION_ARGUMENT, nestedRequireConstraint, REQUIREMENT_USAGE_ARGUMENT, FEATURE_TYPING_ARGUMENT),
                Arguments.of(REQUIREMENT_USAGE_ARGUMENT, nestedRequireConstraint, REQUIREMENT_USAGE_ARGUMENT, REDEFINITION_ARGUMENT),
                Arguments.of(REQUIREMENT_USAGE_ARGUMENT, nestedRequireConstraint, REQUIREMENT_USAGE_ARGUMENT, SUBSETTING_ARGUMENT),
                Arguments.of(REQUIREMENT_USAGE_ARGUMENT, nestedRequireConstraint, REQUIREMENT_USAGE_ARGUMENT, REFERENCE_SUBSETTING_ARGUMENT),
                Arguments.of(SATISFY_REQUIREMENT_USAGE_ARGUMENT, nestedRequireConstraint, SATISFY_REQUIREMENT_USAGE_ARGUMENT, REDEFINITION_ARGUMENT),
                Arguments.of(SATISFY_REQUIREMENT_USAGE_ARGUMENT, nestedRequireConstraint, SATISFY_REQUIREMENT_USAGE_ARGUMENT, SUBSETTING_ARGUMENT),
                Arguments.of(SATISFY_REQUIREMENT_USAGE_ARGUMENT, nestedRequireConstraint, SATISFY_REQUIREMENT_USAGE_ARGUMENT, REFERENCE_SUBSETTING_ARGUMENT),
                Arguments.of(CONCERN_DEFINITION_ARGUMENT, nestedRequireConstraint, CONCERN_DEFINITION_ARGUMENT, SUBCLASSIFICATION_ARGUMENT),
                Arguments.of(CONCERN_DEFINITION_ARGUMENT, nestedRequireConstraint, CONCERN_USAGE_ARGUMENT, FEATURE_TYPING_ARGUMENT),
                Arguments.of(CONCERN_USAGE_ARGUMENT, nestedRequireConstraint, CONCERN_USAGE_ARGUMENT, REDEFINITION_ARGUMENT),
                Arguments.of(CONCERN_USAGE_ARGUMENT, nestedRequireConstraint, CONCERN_USAGE_ARGUMENT, SUBSETTING_ARGUMENT),
                Arguments.of(CONCERN_USAGE_ARGUMENT, nestedRequireConstraint, CONCERN_USAGE_ARGUMENT, REFERENCE_SUBSETTING_ARGUMENT)
        );
    }

    private static Stream<Arguments> framesCompartmentItemInheritanceArguments() {
        NestedElementTestArgument nestedFramedConcern = new NestedElementTestArgument("New Framed Concern", "frames", "concern1");
        return Stream.of(
                Arguments.of(REQUIREMENT_DEFINITION_ARGUMENT, nestedFramedConcern, REQUIREMENT_DEFINITION_ARGUMENT, SUBCLASSIFICATION_ARGUMENT),
                Arguments.of(REQUIREMENT_DEFINITION_ARGUMENT, nestedFramedConcern, REQUIREMENT_USAGE_ARGUMENT, FEATURE_TYPING_ARGUMENT),
                Arguments.of(REQUIREMENT_USAGE_ARGUMENT, nestedFramedConcern, REQUIREMENT_USAGE_ARGUMENT, REDEFINITION_ARGUMENT),
                Arguments.of(REQUIREMENT_USAGE_ARGUMENT, nestedFramedConcern, REQUIREMENT_USAGE_ARGUMENT, SUBSETTING_ARGUMENT),
                Arguments.of(REQUIREMENT_USAGE_ARGUMENT, nestedFramedConcern, REQUIREMENT_USAGE_ARGUMENT, REFERENCE_SUBSETTING_ARGUMENT)
        );
    }

    private static Stream<Arguments> satisfyRequirementsCompartmentItemInheritanceArguments() {
        NestedElementTestArgument nestedSatisfyRequirement = new NestedElementTestArgument("New Satisfy Requirement", "satisfy requirements", "satisfyRequirement1");
        NestedElementTestArgument nestedSatisfyRequirementWithSatisfyEdge =  new NestedElementTestArgument("New Satisfy Requirement", "satisfy requirements", "satisfyRequirement1", "", 0, 1, 2);
        return Stream.of(
                Arguments.of(PART_DEFINITION_ARGUMENT, nestedSatisfyRequirement, PART_DEFINITION_ARGUMENT, SUBCLASSIFICATION_ARGUMENT),
                Arguments.of(PART_DEFINITION_ARGUMENT, nestedSatisfyRequirement, PART_USAGE_ARGUMENT, FEATURE_TYPING_ARGUMENT),
                Arguments.of(PART_USAGE_ARGUMENT, nestedSatisfyRequirementWithSatisfyEdge, PART_USAGE_ARGUMENT, REDEFINITION_ARGUMENT),
                Arguments.of(PART_USAGE_ARGUMENT, nestedSatisfyRequirementWithSatisfyEdge, PART_USAGE_ARGUMENT, SUBSETTING_ARGUMENT),
                Arguments.of(PART_USAGE_ARGUMENT, nestedSatisfyRequirementWithSatisfyEdge, PART_USAGE_ARGUMENT, REFERENCE_SUBSETTING_ARGUMENT)
        );
    }

    private static Stream<Arguments> stakeholdersCompartmentItemInheritanceArguments() {
        NestedElementTestArgument nestedStakeholder = new NestedElementTestArgument("New Stakeholder", "stakeholders", "stakeholder1");
        return Stream.of(
                Arguments.of(REQUIREMENT_DEFINITION_ARGUMENT, nestedStakeholder, REQUIREMENT_DEFINITION_ARGUMENT, SUBCLASSIFICATION_ARGUMENT),
                Arguments.of(REQUIREMENT_DEFINITION_ARGUMENT, nestedStakeholder, REQUIREMENT_USAGE_ARGUMENT, FEATURE_TYPING_ARGUMENT),
                Arguments.of(REQUIREMENT_USAGE_ARGUMENT, nestedStakeholder, REQUIREMENT_USAGE_ARGUMENT, REDEFINITION_ARGUMENT),
                Arguments.of(REQUIREMENT_USAGE_ARGUMENT, nestedStakeholder, REQUIREMENT_USAGE_ARGUMENT, SUBSETTING_ARGUMENT),
                Arguments.of(REQUIREMENT_USAGE_ARGUMENT, nestedStakeholder, REQUIREMENT_USAGE_ARGUMENT, REFERENCE_SUBSETTING_ARGUMENT),
                Arguments.of(SATISFY_REQUIREMENT_USAGE_ARGUMENT, nestedStakeholder, SATISFY_REQUIREMENT_USAGE_ARGUMENT, REDEFINITION_ARGUMENT),
                Arguments.of(SATISFY_REQUIREMENT_USAGE_ARGUMENT, nestedStakeholder, SATISFY_REQUIREMENT_USAGE_ARGUMENT, SUBSETTING_ARGUMENT),
                Arguments.of(SATISFY_REQUIREMENT_USAGE_ARGUMENT, nestedStakeholder, SATISFY_REQUIREMENT_USAGE_ARGUMENT, REFERENCE_SUBSETTING_ARGUMENT),
                Arguments.of(CONCERN_DEFINITION_ARGUMENT, nestedStakeholder, CONCERN_DEFINITION_ARGUMENT, SUBCLASSIFICATION_ARGUMENT),
                Arguments.of(CONCERN_DEFINITION_ARGUMENT, nestedStakeholder, CONCERN_USAGE_ARGUMENT, FEATURE_TYPING_ARGUMENT),
                Arguments.of(CONCERN_USAGE_ARGUMENT, nestedStakeholder, CONCERN_USAGE_ARGUMENT, REDEFINITION_ARGUMENT),
                Arguments.of(CONCERN_USAGE_ARGUMENT, nestedStakeholder, CONCERN_USAGE_ARGUMENT, SUBSETTING_ARGUMENT),
                Arguments.of(CONCERN_USAGE_ARGUMENT, nestedStakeholder, CONCERN_USAGE_ARGUMENT, REFERENCE_SUBSETTING_ARGUMENT)
        );
    }

    private static Stream<Arguments> objectiveCompartmentItemInheritanceArguments() {
        NestedElementTestArgument nestedObjective = new NestedElementTestArgument("New Objective", "objective", "requirement1");
        return Stream.of(
                Arguments.of(CASE_DEFINITION_ARGUMENT, nestedObjective, CASE_DEFINITION_ARGUMENT, SUBCLASSIFICATION_ARGUMENT),
                Arguments.of(CASE_DEFINITION_ARGUMENT, nestedObjective, CASE_USAGE_ARGUMENT, FEATURE_TYPING_ARGUMENT),
                Arguments.of(CASE_USAGE_ARGUMENT, nestedObjective, CASE_USAGE_ARGUMENT, REDEFINITION_ARGUMENT),
                Arguments.of(CASE_USAGE_ARGUMENT, nestedObjective, CASE_USAGE_ARGUMENT, SUBSETTING_ARGUMENT),
                Arguments.of(CASE_USAGE_ARGUMENT, nestedObjective, CASE_USAGE_ARGUMENT, REFERENCE_SUBSETTING_ARGUMENT)
        );
    }

    private static Stream<Arguments> actorsCompartmentItemInheritanceArguments() {
        NestedElementTestArgument nestedActor = new NestedElementTestArgument("New Actor", "actors", "actor1");
        return Stream.of(
                Arguments.of(REQUIREMENT_DEFINITION_ARGUMENT, nestedActor, REQUIREMENT_DEFINITION_ARGUMENT, SUBCLASSIFICATION_ARGUMENT.withExtraEdges(1)),
                Arguments.of(REQUIREMENT_DEFINITION_ARGUMENT, nestedActor, REQUIREMENT_USAGE_ARGUMENT, FEATURE_TYPING_ARGUMENT.withExtraEdges(1)),
                Arguments.of(REQUIREMENT_USAGE_ARGUMENT, nestedActor, REQUIREMENT_USAGE_ARGUMENT, REDEFINITION_ARGUMENT.withExtraEdges(1)),
                Arguments.of(REQUIREMENT_USAGE_ARGUMENT, nestedActor, REQUIREMENT_USAGE_ARGUMENT, SUBSETTING_ARGUMENT.withExtraEdges(1)),
                Arguments.of(REQUIREMENT_USAGE_ARGUMENT, nestedActor, REQUIREMENT_USAGE_ARGUMENT, REFERENCE_SUBSETTING_ARGUMENT.withExtraEdges(1)),
                Arguments.of(CASE_DEFINITION_ARGUMENT, nestedActor, CASE_DEFINITION_ARGUMENT, SUBCLASSIFICATION_ARGUMENT.withExtraEdges(1)),
                Arguments.of(CASE_DEFINITION_ARGUMENT, nestedActor, CASE_USAGE_ARGUMENT, FEATURE_TYPING_ARGUMENT.withExtraEdges(1)),
                Arguments.of(CASE_USAGE_ARGUMENT, nestedActor, CASE_USAGE_ARGUMENT, REDEFINITION_ARGUMENT.withExtraEdges(1)),
                Arguments.of(CASE_USAGE_ARGUMENT, nestedActor, CASE_USAGE_ARGUMENT, SUBSETTING_ARGUMENT.withExtraEdges(1)),
                Arguments.of(CASE_USAGE_ARGUMENT, nestedActor, CASE_USAGE_ARGUMENT, REFERENCE_SUBSETTING_ARGUMENT.withExtraEdges(1)),
                Arguments.of(SATISFY_REQUIREMENT_USAGE_ARGUMENT, nestedActor, SATISFY_REQUIREMENT_USAGE_ARGUMENT, REDEFINITION_ARGUMENT.withExtraEdges(1)),
                Arguments.of(SATISFY_REQUIREMENT_USAGE_ARGUMENT, nestedActor, SATISFY_REQUIREMENT_USAGE_ARGUMENT, SUBSETTING_ARGUMENT.withExtraEdges(1)),
                Arguments.of(SATISFY_REQUIREMENT_USAGE_ARGUMENT, nestedActor, SATISFY_REQUIREMENT_USAGE_ARGUMENT, REFERENCE_SUBSETTING_ARGUMENT.withExtraEdges(1)),
                Arguments.of(CONCERN_DEFINITION_ARGUMENT, nestedActor, CONCERN_DEFINITION_ARGUMENT, SUBCLASSIFICATION_ARGUMENT.withExtraEdges(1)),
                Arguments.of(CONCERN_DEFINITION_ARGUMENT, nestedActor, CONCERN_USAGE_ARGUMENT, FEATURE_TYPING_ARGUMENT.withExtraEdges(1)),
                Arguments.of(CONCERN_USAGE_ARGUMENT, nestedActor, CONCERN_USAGE_ARGUMENT, REDEFINITION_ARGUMENT.withExtraEdges(1)),
                Arguments.of(CONCERN_USAGE_ARGUMENT, nestedActor, CONCERN_USAGE_ARGUMENT, SUBSETTING_ARGUMENT.withExtraEdges(1)),
                Arguments.of(CONCERN_USAGE_ARGUMENT, nestedActor, CONCERN_USAGE_ARGUMENT, REFERENCE_SUBSETTING_ARGUMENT.withExtraEdges(1))
        );
    }

    private static Stream<Arguments> subjectCompartmentItemInheritanceArguments() {
        NestedElementTestArgument nestedSubjectWithoutEdge = new NestedElementTestArgument("New Subject", "subject", "ref subject", "", 0, 1, 0);
        return Stream.of(
                Arguments.of(REQUIREMENT_DEFINITION_ARGUMENT, nestedSubjectWithoutEdge, REQUIREMENT_DEFINITION_ARGUMENT, SUBCLASSIFICATION_ARGUMENT),
                Arguments.of(REQUIREMENT_DEFINITION_ARGUMENT, nestedSubjectWithoutEdge, REQUIREMENT_USAGE_ARGUMENT, FEATURE_TYPING_ARGUMENT),
                Arguments.of(REQUIREMENT_USAGE_ARGUMENT, nestedSubjectWithoutEdge, REQUIREMENT_USAGE_ARGUMENT, REDEFINITION_ARGUMENT),
                Arguments.of(REQUIREMENT_USAGE_ARGUMENT, nestedSubjectWithoutEdge, REQUIREMENT_USAGE_ARGUMENT, SUBSETTING_ARGUMENT),
                Arguments.of(REQUIREMENT_USAGE_ARGUMENT, nestedSubjectWithoutEdge, REQUIREMENT_USAGE_ARGUMENT, REFERENCE_SUBSETTING_ARGUMENT),
                Arguments.of(SATISFY_REQUIREMENT_USAGE_ARGUMENT, nestedSubjectWithoutEdge, SATISFY_REQUIREMENT_USAGE_ARGUMENT, REDEFINITION_ARGUMENT),
                Arguments.of(SATISFY_REQUIREMENT_USAGE_ARGUMENT, nestedSubjectWithoutEdge, SATISFY_REQUIREMENT_USAGE_ARGUMENT, SUBSETTING_ARGUMENT),
                Arguments.of(SATISFY_REQUIREMENT_USAGE_ARGUMENT, nestedSubjectWithoutEdge, SATISFY_REQUIREMENT_USAGE_ARGUMENT, REFERENCE_SUBSETTING_ARGUMENT),
                Arguments.of(CONCERN_DEFINITION_ARGUMENT, nestedSubjectWithoutEdge, CONCERN_DEFINITION_ARGUMENT, SUBCLASSIFICATION_ARGUMENT),
                Arguments.of(CONCERN_DEFINITION_ARGUMENT, nestedSubjectWithoutEdge, CONCERN_USAGE_ARGUMENT, FEATURE_TYPING_ARGUMENT),
                Arguments.of(CONCERN_USAGE_ARGUMENT, nestedSubjectWithoutEdge, CONCERN_USAGE_ARGUMENT, REDEFINITION_ARGUMENT),
                Arguments.of(CONCERN_USAGE_ARGUMENT, nestedSubjectWithoutEdge, CONCERN_USAGE_ARGUMENT, SUBSETTING_ARGUMENT),
                Arguments.of(CONCERN_USAGE_ARGUMENT, nestedSubjectWithoutEdge, CONCERN_USAGE_ARGUMENT, REFERENCE_SUBSETTING_ARGUMENT),
                Arguments.of(CASE_DEFINITION_ARGUMENT, nestedSubjectWithoutEdge, CASE_DEFINITION_ARGUMENT, SUBCLASSIFICATION_ARGUMENT),
                Arguments.of(CASE_DEFINITION_ARGUMENT, nestedSubjectWithoutEdge, CASE_USAGE_ARGUMENT, FEATURE_TYPING_ARGUMENT),
                Arguments.of(CASE_USAGE_ARGUMENT, nestedSubjectWithoutEdge, CASE_USAGE_ARGUMENT, REDEFINITION_ARGUMENT),
                Arguments.of(CASE_USAGE_ARGUMENT, nestedSubjectWithoutEdge, CASE_USAGE_ARGUMENT, SUBSETTING_ARGUMENT),
                Arguments.of(CASE_USAGE_ARGUMENT, nestedSubjectWithoutEdge, CASE_USAGE_ARGUMENT, REFERENCE_SUBSETTING_ARGUMENT)
        );
    }
}
