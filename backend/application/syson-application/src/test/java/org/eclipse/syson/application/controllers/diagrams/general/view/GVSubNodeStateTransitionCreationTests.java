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

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariable;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariableType;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.SysONTestsProperties;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckNodeInCompartment;
import org.eclipse.syson.application.controllers.diagrams.checkers.DiagramCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.IDiagramChecker;
import org.eclipse.syson.application.controllers.diagrams.testers.NodeCreationTester;
import org.eclipse.syson.application.controllers.utils.TestNameGenerator;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.application.data.SysONRepresentationDescriptionIdentifiers;
import org.eclipse.syson.diagram.common.view.nodes.StatesCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.NodeCreationTestsService;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.PerformActionUsage;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.StateSubactionKind;
import org.eclipse.syson.sysml.StateSubactionMembership;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;
import reactor.test.StepVerifier.Step;

/**
 * Tests the creation of "StateTransition" sub nodes section in the General View diagram.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = { SysONTestsProperties.NO_DEFAULT_LIBRARIES_PROPERTY })
public class GVSubNodeStateTransitionCreationTests extends AbstractIntegrationTests {

    private static final String EXHIBIT_STATES_COMPARTMENT = "exhibit states";

    private static final String STATES_COMPARTMENT = "states";

    private static final String DOC_COMPARTMENT = "doc";

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
    private IObjectSearchService objectSearchService;

    @Autowired
    private NodeCreationTester nodeCreationTester;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private DiagramComparator diagramComparator;

    @Autowired
    private IIdentityService identityService;

    private DiagramDescriptionIdProvider diagramDescriptionIdProvider;

    private Step<DiagramRefreshedEventPayload> verifier;

    private AtomicReference<Diagram> diagram;

    private DiagramDescription diagramDescription;

    private NodeCreationTestsService creationTestsService;

    private final IDescriptionNameGenerator descriptionNameGenerator = new GVDescriptionNameGenerator();

    private DiagramCheckerService diagramCheckerService;

    private SemanticCheckerService semanticCheckerService;

    private static Stream<Arguments> stateSubactionsParameters() {
        return Stream.of(
                Arguments.of(StateSubactionKind.ENTRY),
                Arguments.of(StateSubactionKind.DO),
                Arguments.of(StateSubactionKind.EXIT))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> stateUsageChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getDocumentation(), DOC_COMPARTMENT, SysmlPackage.eINSTANCE.getElement_Documentation(), "New Documentation"),
                Arguments.of(SysmlPackage.eINSTANCE.getStateUsage(), STATES_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedState(), "New State"),
                Arguments.of(SysmlPackage.eINSTANCE.getStateUsage(), STATES_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedState(), "New Parallel State"),
                Arguments.of(SysmlPackage.eINSTANCE.getStateUsage(), EXHIBIT_STATES_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedState(), "New Exhibit State"),
                Arguments.of(SysmlPackage.eINSTANCE.getStateUsage(), EXHIBIT_STATES_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedState(), "New Exhibit Parallel State"))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> stateDefinitionChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getDocumentation(), DOC_COMPARTMENT, SysmlPackage.eINSTANCE.getElement_Documentation(), "New Documentation"),
                Arguments.of(SysmlPackage.eINSTANCE.getStateUsage(), STATES_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedState(), "New State"),
                Arguments.of(SysmlPackage.eINSTANCE.getStateUsage(), STATES_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedState(), "New Parallel State"),
                Arguments.of(SysmlPackage.eINSTANCE.getStateUsage(), EXHIBIT_STATES_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedState(), "New Exhibit State"),
                Arguments.of(SysmlPackage.eINSTANCE.getStateUsage(), EXHIBIT_STATES_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedState(), "New Exhibit Parallel State"))
                .map(TestNameGenerator::namedArguments);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.diagram = this.givenDiagram.getDiagram(this.verifier);
        this.diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        this.diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(this.diagramDescription, this.diagramIdProvider);
        this.creationTestsService = new NodeCreationTestsService(this.nodeCreationTester, this.descriptionNameGenerator, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID);
        this.diagramCheckerService = new DiagramCheckerService(this.diagramComparator, this.descriptionNameGenerator);
        this.semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID);
    }

    @AfterEach
    public void tearDown() {
        if (this.verifier != null) {
            this.verifier.thenCancel()
                    .verify(Duration.ofSeconds(10));
        }
    }

    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("stateSubactionsParameters")
    public void createStateUsageSubactionNode(StateSubactionKind kind) {
        String toolName = "New " + StringUtils.capitalize(kind.getName()) + " Action";

        this.createStateSubactionNode(kind, SysmlPackage.eINSTANCE.getStateUsage(), "state", toolName);
    }

    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("stateSubactionsParameters")
    public void createStateUsageSubactionWithReferencedActionNode(StateSubactionKind kind) {
        String toolName = "New " + StringUtils.capitalize(kind.getName()) + " Action with referenced Action";
        var params = List.of(new ToolVariable("selectedObject", GeneralViewWithTopNodesTestProjectData.SemanticIds.ACTION_USAGE_ID , ToolVariableType.OBJECT_ID));

        this.createStateSubactionWithReferencedActionNode(kind, SysmlPackage.eINSTANCE.getStateUsage(), "state", toolName, params);
    }

    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("stateSubactionsParameters")
    public void createStateDefinitionSubactionNode(StateSubactionKind kind) {
        String toolName = "New " + StringUtils.capitalize(kind.getName()) + " Action";

        this.createStateSubactionNode(kind, SysmlPackage.eINSTANCE.getStateDefinition(), "StateDefinition", toolName);
    }

    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("stateSubactionsParameters")
    public void createStateDefinitionSubactionWithReferencedActionNode(StateSubactionKind kind) {
        String toolName = "New " + StringUtils.capitalize(kind.getName()) + " Action with referenced Action";
        var params = List.of(new ToolVariable("selectedObject", GeneralViewWithTopNodesTestProjectData.SemanticIds.ACTION_USAGE_ID , ToolVariableType.OBJECT_ID));

        this.createStateSubactionWithReferencedActionNode(kind, SysmlPackage.eINSTANCE.getStateDefinition(), "StateDefinition", toolName, params);
    }

    private void createStateSubactionNode(StateSubactionKind kind, EClass parentEClass, String parentLabel, String toolName) {
        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel, toolName);

        String[] subActionId = new String[1];
        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    // only the new subaction should be created
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram);
            var node = this.diagramComparator.newNodes(initialDiagram, newDiagram).get(0);
            subActionId[0] = node.getTargetObjectId();
        };
        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, PerformActionUsage.class, () -> subActionId[0], subaction -> {
            // new subaction should be owned by a StateSubactionMembership with the correct kind
            var parentMembership = subaction.eContainer();
            assertThat(parentMembership).isInstanceOf(StateSubactionMembership.class);
            var stateSubactionMembership = (StateSubactionMembership) parentMembership;
            assertThat(stateSubactionMembership.getKind()).isEqualTo(kind);
            // subaction has no owned membership
            assertThat(subaction.getOwnedRelationship()).hasSize(0);
        });
    }

    private void createStateSubactionWithReferencedActionNode(StateSubactionKind kind, EClass parentEClass, String parentLabel, String toolName, List<@NotNull ToolVariable> params) {
        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel, toolName, params);

        String[] subActionId = new String[1];
        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    // only the new subaction should be created
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram);
            var node = this.diagramComparator.newNodes(initialDiagram, newDiagram).get(0);
            subActionId[0] = node.getTargetObjectId();
        };
        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);

        this.semanticCheckerService.checkElement(this.verifier, PerformActionUsage.class, () -> subActionId[0], subaction -> {
            var membership = subaction.eContainer();
            assertThat(membership).isInstanceOf(StateSubactionMembership.class);
            var stateSubactionMembership = (StateSubactionMembership) membership;
            assertThat(stateSubactionMembership.getKind()).isEqualTo(kind);
            // check that the new sub action contains the reference subsetting to the existing action
            var memberships = subaction.getOwnedRelationship();
            assertThat(memberships.get(0)).isInstanceOf(ReferenceSubsetting.class);
            var referenceSubsetting = (ReferenceSubsetting) memberships.get(0);
            assertThat(referenceSubsetting.getReferencedFeature()).isInstanceOf(ActionUsage.class);
            assertThat(this.identityService.getId(referenceSubsetting.getReferencedFeature())).isEqualTo(GeneralViewWithTopNodesTestProjectData.SemanticIds.ACTION_USAGE_ID);
        });
    }

    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("stateUsageChildNodeParameters")
    public void createStateUsageChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, String creationToolName) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getStateUsage();
        String parentLabel = "state";
        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel, creationToolName);
        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            int expectedNodeCount = 1;
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(expectedNodeCount)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram);
            String listStatesNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            if (STATES_COMPARTMENT.equals(compartmentName)) {
                listStatesNodeDescription += StatesCompartmentNodeDescriptionProvider.STATES_NAME;
            } else if (EXHIBIT_STATES_COMPARTMENT.equals(compartmentName)) {
                listStatesNodeDescription += StatesCompartmentNodeDescriptionProvider.EXHIBIT_STATES_NAME;
            }
            new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(listStatesNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        };
        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);
        // Actions are not semantically owned by parent
        if (!SysmlPackage.eINSTANCE.getActionUsage().equals(childEClass)) {
            this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass), this.verifier);
        }
    }

    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("stateDefinitionChildNodeParameters")
    public void createStateDefinitionChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, String creationToolName) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getStateDefinition();
        String parentLabel = "StateDefinition";
        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel, creationToolName);
        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            int expectedNodeCount = 1;
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(expectedNodeCount)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram);
            String listStatesNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            if (STATES_COMPARTMENT.equals(compartmentName)) {
                listStatesNodeDescription += StatesCompartmentNodeDescriptionProvider.STATES_NAME;
            } else if (EXHIBIT_STATES_COMPARTMENT.equals(compartmentName)) {
                listStatesNodeDescription += StatesCompartmentNodeDescriptionProvider.EXHIBIT_STATES_NAME;
            }
            new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(listStatesNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        };
        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);
        // Actions are not semantically owned by parent
        if (!SysmlPackage.eINSTANCE.getActionUsage().equals(childEClass)) {
            this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass), this.verifier);
        }
    }
}
