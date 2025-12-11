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
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariable;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariableType;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckBorderNode;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckNodeInCompartment;
import org.eclipse.syson.application.controllers.diagrams.checkers.DiagramCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.IDiagramChecker;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.controllers.utils.TestNameGenerator;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.NodeCreationTestsService;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
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
 * Tests the creation of "Structure" sub nodes section in the General View diagram.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVSubNodeStructureCreationTests extends AbstractIntegrationTests {

    private static final String ATTRIBUTES_COMPARTMENT = "attributes";

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
    private ToolTester nodeCreationTester;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private DiagramComparator diagramComparator;

    private DiagramDescriptionIdProvider diagramDescriptionIdProvider;

    private Step<DiagramRefreshedEventPayload> verifier;

    private AtomicReference<Diagram> diagram;

    private DiagramDescription diagramDescription;

    private NodeCreationTestsService creationTestsService;

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private DiagramCheckerService diagramCheckerService;

    private SemanticCheckerService semanticCheckerService;

    private static Stream<Arguments> attributeDefinitionChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ATTRIBUTES_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute()),
                Arguments.of(SysmlPackage.eINSTANCE.getDocumentation(), DOC_COMPARTMENT, SysmlPackage.eINSTANCE.getElement_Documentation()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> attributeUsageChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ATTRIBUTES_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedAttribute()),
                Arguments.of(SysmlPackage.eINSTANCE.getReferenceUsage(), "references", SysmlPackage.eINSTANCE.getUsage_NestedReference()),
                Arguments.of(SysmlPackage.eINSTANCE.getDocumentation(), DOC_COMPARTMENT, SysmlPackage.eINSTANCE.getElement_Documentation()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> enumerationDefinitionChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getEnumerationUsage(), "enumerations", SysmlPackage.eINSTANCE.getEnumerationDefinition_EnumeratedValue()),
                Arguments.of(SysmlPackage.eINSTANCE.getDocumentation(), DOC_COMPARTMENT, SysmlPackage.eINSTANCE.getElement_Documentation()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> itemDefinitionSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedUsage(), 4))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> itemDefinitionChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ATTRIBUTES_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute()),
                Arguments.of(SysmlPackage.eINSTANCE.getDocumentation(), DOC_COMPARTMENT, SysmlPackage.eINSTANCE.getElement_Documentation()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> itemUsageSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getUsage_NestedItem(), 4),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPart(), 10),
                Arguments.of(SysmlPackage.eINSTANCE.getComment(), SysmlPackage.eINSTANCE.getElement_OwnedElement(), 0))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> itemUsageChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ATTRIBUTES_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedAttribute()),
                Arguments.of(SysmlPackage.eINSTANCE.getReferenceUsage(), "references", SysmlPackage.eINSTANCE.getUsage_NestedReference()),
                Arguments.of(SysmlPackage.eINSTANCE.getDocumentation(), DOC_COMPARTMENT, SysmlPackage.eINSTANCE.getElement_Documentation()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> itemUsageBorderAndChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), "ports", SysmlPackage.eINSTANCE.getUsage_NestedPort(), SysmlPackage.eINSTANCE.getPortUsage()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> packageChildNodeParameters() {
        EReference ownedMember = SysmlPackage.eINSTANCE.getNamespace_OwnedMember();
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeDefinition(), ownedMember, 2),
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ownedMember, 3),
                Arguments.of(SysmlPackage.eINSTANCE.getAllocationDefinition(), ownedMember, 3),
                Arguments.of(SysmlPackage.eINSTANCE.getAllocationUsage(), ownedMember, 3),
                Arguments.of(SysmlPackage.eINSTANCE.getActionDefinition(), ownedMember, 6),
                Arguments.of(SysmlPackage.eINSTANCE.getAcceptActionUsage(), ownedMember, 2),
                Arguments.of(SysmlPackage.eINSTANCE.getActionUsage(), ownedMember, 7),
                Arguments.of(SysmlPackage.eINSTANCE.getAssignmentActionUsage(), ownedMember, 1),
                Arguments.of(SysmlPackage.eINSTANCE.getConcernDefinition(), ownedMember, 8),
                Arguments.of(SysmlPackage.eINSTANCE.getConcernUsage(), ownedMember, 8),
                Arguments.of(SysmlPackage.eINSTANCE.getConstraintDefinition(), ownedMember, 2),
                Arguments.of(SysmlPackage.eINSTANCE.getConstraintUsage(), ownedMember, 4),
                Arguments.of(SysmlPackage.eINSTANCE.getEnumerationDefinition(), ownedMember, 2),
                Arguments.of(SysmlPackage.eINSTANCE.getInterfaceDefinition(), ownedMember, 5),
                Arguments.of(SysmlPackage.eINSTANCE.getInterfaceUsage(), ownedMember, 4),
                Arguments.of(SysmlPackage.eINSTANCE.getItemDefinition(), ownedMember, 2),
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), ownedMember, 4),
                Arguments.of(SysmlPackage.eINSTANCE.getMetadataDefinition(), ownedMember, 3),
                Arguments.of(SysmlPackage.eINSTANCE.getOccurrenceDefinition(), ownedMember, 3),
                Arguments.of(SysmlPackage.eINSTANCE.getOccurrenceUsage(), ownedMember, 2),
                // A package doesn't have a compartment: it is handled as a custom node
                Arguments.of(SysmlPackage.eINSTANCE.getPackage(), ownedMember, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getPartDefinition(), ownedMember, 9),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), ownedMember, 10),
                Arguments.of(SysmlPackage.eINSTANCE.getPortDefinition(), ownedMember, 5),
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), ownedMember, 4),
                Arguments.of(SysmlPackage.eINSTANCE.getRequirementDefinition(), ownedMember, 8),
                Arguments.of(SysmlPackage.eINSTANCE.getRequirementUsage(), ownedMember, 8),
                Arguments.of(SysmlPackage.eINSTANCE.getUseCaseDefinition(), ownedMember, 5),
                Arguments.of(SysmlPackage.eINSTANCE.getUseCaseUsage(), ownedMember, 7),
                Arguments.of(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), ownedMember, 8),
                Arguments.of(SysmlPackage.eINSTANCE.getStateDefinition(), ownedMember, 6),
                Arguments.of(SysmlPackage.eINSTANCE.getStateUsage(), ownedMember, 6),
                Arguments.of(SysmlPackage.eINSTANCE.getNamespaceImport(), SysmlPackage.eINSTANCE.getNamespace_OwnedImport(), 0))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> partDefinitionChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ATTRIBUTES_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute()),
                Arguments.of(SysmlPackage.eINSTANCE.getDocumentation(), DOC_COMPARTMENT, SysmlPackage.eINSTANCE.getElement_Documentation()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> partDefinitionSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedItem(), 4),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedPart(), 10),
                Arguments.of(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedRequirement(), 8))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> partUsageChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ATTRIBUTES_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedAttribute()),
                Arguments.of(SysmlPackage.eINSTANCE.getDocumentation(), DOC_COMPARTMENT, SysmlPackage.eINSTANCE.getElement_Documentation()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> partUsageSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getUsage_NestedItem(), 4),
                Arguments.of(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), SysmlPackage.eINSTANCE.getUsage_NestedRequirement(), 8))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> partUsageSiblingAndChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), "parts", SysmlPackage.eINSTANCE.getUsage_NestedPart(), 12, 1))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> directedPortsInPartDefinitionChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), "ports", SysmlPackage.eINSTANCE.getDefinition_OwnedPort(), "New Port"),
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), "ports", SysmlPackage.eINSTANCE.getDefinition_OwnedPort(), "New Port In"),
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), "ports", SysmlPackage.eINSTANCE.getDefinition_OwnedPort(), "New Port Inout"),
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), "ports", SysmlPackage.eINSTANCE.getDefinition_OwnedPort(), "New Port Out"))
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

    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("attributeDefinitionChildNodeParameters")
    public void createAttributeDefinitionSubNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getAttributeDefinition();
        String parentLabel = "AttributeDefinition";
        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel,
                childEClass);
        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram, true);

            String newNodeDescriptionName = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(newNodeDescriptionName)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        };
        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);
        this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass),
                this.verifier);
    }

    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("attributeUsageChildNodeParameters")
    public void createAttributeUsageSubNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getAttributeUsage();
        String parentLabel = "attribute";
        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel,
                childEClass);
        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram, true);

            String newNodeDescriptionName = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(newNodeDescriptionName)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        };
        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);
        this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass),
                this.verifier);
    }

    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("enumerationDefinitionChildNodeParameters")
    public void createEnumerationDefinitionSubNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getEnumerationDefinition();
        String parentLabel = "EnumerationDefinition";
        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel, childEClass);
        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram, true);

            String newNodeDescriptionName = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(newNodeDescriptionName)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        };
        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);
        this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass), this.verifier);
    }

    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("itemDefinitionSiblingNodeParameters")
    public void createItemDefinitionSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getItemDefinition();
        String parentLabel = "ItemDefinition";
        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel, childEClass);
        this.diagramCheckerService.checkDiagram(this.diagramCheckerService.getSiblingNodeGraphicalChecker(this.diagram, this.diagramDescriptionIdProvider, childEClass, compartmentCount),
                this.diagram, this.verifier);
        this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass), this.verifier);
    }

    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("itemDefinitionChildNodeParameters")
    public void createItemDefinitionSubNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getItemDefinition();
        String parentLabel = "ItemDefinition";
        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel, childEClass);
        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram, true);

            String newNodeDescriptionName = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(newNodeDescriptionName)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        };
        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);
        this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass), this.verifier);
    }

    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("itemUsageSiblingNodeParameters")
    public void createItemUsageSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getItemUsage();
        String parentLabel = "item";
        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel, childEClass);
        this.diagramCheckerService.checkDiagram(this.diagramCheckerService.getSiblingNodeGraphicalChecker(this.diagram, this.diagramDescriptionIdProvider, childEClass, compartmentCount),
                this.diagram, this.verifier);
        this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass), this.verifier);
    }

    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("itemUsageChildNodeParameters")
    public void createItemUsageChildNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getItemUsage();
        String parentLabel = "item";
        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel, childEClass);
        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram, true);

            String newNodeDescriptionName = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(newNodeDescriptionName)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        };
        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);
        this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass), this.verifier);
    }

    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("itemUsageBorderAndChildNodeParameters")
    public void createItemUsageBorderAndChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, EClass borderNodeType) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getItemUsage();
        String parentLabel = "item";
        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel, childEClass);
        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewBorderNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram, true);
            String compartmentNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(compartmentNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
            String borderNodeDescription = this.descriptionNameGenerator.getBorderNodeName(borderNodeType, containmentReference);
            new CheckBorderNode(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .hasBorderNodeDescriptionName(borderNodeDescription)
                    .check(initialDiagram, newDiagram);
        };
        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);
        this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass), this.verifier);
    }

    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("packageChildNodeParameters")
    public void createPackageChildNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getPackage();
        String parentLabel = "Package";
        List<ToolVariable> variables = List.of();
        if (SysmlPackage.eINSTANCE.getNamespaceImport().equals(childEClass)) {
            variables = List.of(new ToolVariable("selectedObject", GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID, ToolVariableType.OBJECT_ID));
        }
        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel, childEClass, variables);
        this.diagramCheckerService.checkDiagram(
                this.diagramCheckerService.getChildNodeGraphicalChecker(this.diagram, this.diagramDescriptionIdProvider, parentLabel, childEClass, compartmentCount), this.diagram,
                this.verifier);
        this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass), this.verifier);
    }

    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("partDefinitionChildNodeParameters")
    public void createPartDefinitionChildNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getPartDefinition();
        String parentLabel = "PartDefinition";
        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel, childEClass);
        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram, true);

            String newNodeDescriptionName = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(newNodeDescriptionName)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        };
        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);
        this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass), this.verifier);
    }

    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("partDefinitionSiblingNodeParameters")
    public void createPartDefinitionSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getPartDefinition();
        String parentLabel = "PartDefinition";
        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel, childEClass);
        this.diagramCheckerService.checkDiagram(this.diagramCheckerService.getSiblingNodeGraphicalChecker(this.diagram, this.diagramDescriptionIdProvider, childEClass, compartmentCount),
                this.diagram, this.verifier);
        this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass), this.verifier);
    }

    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("partUsageChildNodeParameters")
    public void createPartUsageChildNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getPartUsage();
        String parentLabel = "part";
        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel, childEClass);
        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram, true);

            String newNodeDescriptionName = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(newNodeDescriptionName)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        };
        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);
        this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass), this.verifier);
    }

    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("partUsageSiblingNodeParameters")
    public void createPartUsageSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getPartUsage();
        String parentLabel = "part";
        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel, childEClass);
        this.diagramCheckerService.checkDiagram(this.diagramCheckerService.getSiblingNodeGraphicalChecker(this.diagram, this.diagramDescriptionIdProvider, childEClass, compartmentCount),
                this.diagram, this.verifier);
        this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass), this.verifier);
    }

    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("partUsageSiblingAndChildNodeParameters")
    public void createPartUsageSiblingAndChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, int expectedNumberOfNewNodes,
            int expectedNumberOfNewEdges) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getPartUsage();
        String parentLabel = "part";
        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel, childEClass);
        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(expectedNumberOfNewNodes)
                    .hasNewEdgeCount(expectedNumberOfNewEdges)
                    .check(initialDiagram, newDiagram);
            String listStatesNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(listStatesNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        };
        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);
        this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass), this.verifier);
    }

    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("directedPortsInPartDefinitionChildNodeParameters")
    public void createDirectedPortsInPartDefinitionChildNodeParameters(EClass childEClass, String compartmentName, EReference containmentReference, String creationToolName) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getPartDefinition();
        String parentLabel = "PartDefinition";
        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel, creationToolName);
        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewBorderNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram, true);
            String compartmentNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(compartmentNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
            String borderNodeDescription = this.descriptionNameGenerator.getBorderNodeName(childEClass, containmentReference);
            new CheckBorderNode(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .hasBorderNodeDescriptionName(borderNodeDescription)
                    .check(initialDiagram, newDiagram);
        };
        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);
        this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass), this.verifier);
    }
}
