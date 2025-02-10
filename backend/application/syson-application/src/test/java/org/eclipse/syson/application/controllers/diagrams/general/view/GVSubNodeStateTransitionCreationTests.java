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

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckNodeInCompartment;
import org.eclipse.syson.application.controllers.diagrams.checkers.DiagramCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.IDiagramChecker;
import org.eclipse.syson.application.controllers.diagrams.testers.NodeCreationTester;
import org.eclipse.syson.application.controllers.utils.TestNameGenerator;
import org.eclipse.syson.application.data.SysMLv2Identifiers;
import org.eclipse.syson.diagram.common.view.nodes.StatesCompartmentNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.NodeCreationTestsService;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
    private IObjectService objectService;

    @Autowired
    private NodeCreationTester nodeCreationTester;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private DiagramComparator diagramComparator;

    private DiagramDescriptionIdProvider diagramDescriptionIdProvider;

    private Step<DiagramRefreshedEventPayload> verifier;

    private AtomicReference<Diagram> diagram;

    private DiagramDescription diagramDescription;

    private NodeCreationTestsService creationTestsService;

    private final IDescriptionNameGenerator descriptionNameGenerator = new GVDescriptionNameGenerator();

    private DiagramCheckerService diagramCheckerService;

    private SemanticCheckerService semanticCheckerService;

    private static Stream<Arguments> stateUsageSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getActionUsage(), SysmlPackage.eINSTANCE.getStateUsage_EntryAction(), "New Entry Action", 4),
                Arguments.of(SysmlPackage.eINSTANCE.getActionUsage(), SysmlPackage.eINSTANCE.getStateUsage_DoAction(), "New Do Action", 4),
                Arguments.of(SysmlPackage.eINSTANCE.getActionUsage(), SysmlPackage.eINSTANCE.getStateUsage_ExitAction(), "New Exit Action", 4))
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

    private static Stream<Arguments> stateDefinitionSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getActionUsage(), SysmlPackage.eINSTANCE.getStateDefinition_EntryAction(), "New Entry Action", 4),
                Arguments.of(SysmlPackage.eINSTANCE.getActionUsage(), SysmlPackage.eINSTANCE.getStateDefinition_DoAction(), "New Do Action", 4),
                Arguments.of(SysmlPackage.eINSTANCE.getActionUsage(), SysmlPackage.eINSTANCE.getStateDefinition_ExitAction(), "New Exit Action", 4))
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
                SysMLv2Identifiers.GENERAL_VIEW_WITH_TOP_NODES_EDITING_CONTEXT_ID,
                SysMLv2Identifiers.GENERAL_VIEW_WITH_TOP_NODES_DIAGRAM);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.diagram = this.givenDiagram.getDiagram(this.verifier);
        this.diagramDescription = this.givenDiagramDescription.getDiagramDescription(SysMLv2Identifiers.GENERAL_VIEW_WITH_TOP_NODES_EDITING_CONTEXT_ID,
                SysMLv2Identifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        this.diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(this.diagramDescription, this.diagramIdProvider);
        this.creationTestsService = new NodeCreationTestsService(this.nodeCreationTester, this.descriptionNameGenerator);
        this.diagramCheckerService = new DiagramCheckerService(this.diagramComparator, this.descriptionNameGenerator);
        this.semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectService);
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
    @ParameterizedTest
    @MethodSource("stateUsageSiblingNodeParameters")
    public void createStateUsageSiblingNodes(EClass childEClass, EReference containmentReference, String creationToolName, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getStateUsage();
        String parentLabel = "state";

        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel, creationToolName);
        // the action is created inside a list compartment and outside as a sibling node
        this.diagramCheckerService.checkDiagram(this.diagramCheckerService.getSiblingNodeGraphicalChecker(this.diagram, this.diagramDescriptionIdProvider, childEClass, compartmentCount, 2),
                this.diagram, this.verifier);
        this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass), this.verifier);
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("stateDefinitionSiblingNodeParameters")
    public void createStateDefinitionSiblingNodes(EClass childEClass, EReference containmentReference, String creationToolName, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getStateDefinition();
        String parentLabel = "StateDefinition";

        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel, creationToolName);
        // the action is created inside a list compartment and outside as a sibling node
        this.diagramCheckerService.checkDiagram(this.diagramCheckerService.getSiblingNodeGraphicalChecker(this.diagram, this.diagramDescriptionIdProvider, childEClass, compartmentCount, 2),
                this.diagram, this.verifier);
        this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass), this.verifier);
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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
