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

import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariable;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariableType;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckBorderNode;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckNodeInCompartment;
import org.eclipse.syson.application.controllers.diagrams.checkers.DiagramCheckerService;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.controllers.utils.TestNameGenerator;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.diagram.common.view.nodes.SatisfyRequirementCompartmentItemNodeDescription;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.NodeCreationTestsService;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

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

    private static final String PARTS_COMPARTMENT = "parts";

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

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

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private NodeCreationTestsService creationTestsService;

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

    private static Stream<Arguments> connectionDefinitionChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ATTRIBUTES_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute()),
                Arguments.of(SysmlPackage.eINSTANCE.getDocumentation(), DOC_COMPARTMENT, SysmlPackage.eINSTANCE.getElement_Documentation()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> connectionDefinitionSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), PARTS_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedPart(), 11, 2))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> allocationDefinitionSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), PARTS_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedPart(), 11, 2))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> interfaceDefinitionSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), PARTS_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedPart(), 11, 2))
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
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPart(), 11),
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
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeDefinition(), ownedMember, 2, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ownedMember, 3, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getAllocationDefinition(), ownedMember, 4, 2, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getAllocationUsage(), ownedMember, 3, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getActionDefinition(), ownedMember, 6, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getAcceptActionUsage(), ownedMember, 2, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getActionUsage(), ownedMember, 7, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getAssignmentActionUsage(), ownedMember, 1, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getConcernDefinition(), ownedMember, 8, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getConcernUsage(), ownedMember, 8, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getConstraintDefinition(), ownedMember, 2, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getConstraintUsage(), ownedMember, 4, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getEnumerationDefinition(), ownedMember, 2, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getInterfaceDefinition(), ownedMember, 7, 6, 2),
                Arguments.of(SysmlPackage.eINSTANCE.getInterfaceUsage(), ownedMember, 4, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getItemDefinition(), ownedMember, 2, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), ownedMember, 4, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getMetadataDefinition(), ownedMember, 3, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getOccurrenceDefinition(), ownedMember, 3, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getOccurrenceUsage(), ownedMember, 2, 0, 0),
                // A package doesn't have a compartment: it is handled as a custom node
                Arguments.of(SysmlPackage.eINSTANCE.getPackage(), ownedMember, 0, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getPartDefinition(), ownedMember, 11, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), ownedMember, 11, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getPortDefinition(), ownedMember, 5, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), ownedMember, 5, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getRequirementDefinition(), ownedMember, 8, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getRequirementUsage(), ownedMember, 8, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getUseCaseDefinition(), ownedMember, 5, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getUseCaseUsage(), ownedMember, 7, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), ownedMember, 8, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getStateDefinition(), ownedMember, 6, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getStateUsage(), ownedMember, 6, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getNamespaceImport(), SysmlPackage.eINSTANCE.getNamespace_OwnedImport(), 0,
                        0, 0)
        ).map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> partDefinitionChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ATTRIBUTES_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute()),
                Arguments.of(SysmlPackage.eINSTANCE.getDocumentation(), DOC_COMPARTMENT, SysmlPackage.eINSTANCE.getElement_Documentation()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> partDefinitionSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedItem()),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedPart()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> partDefinitionSiblingAndChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), "satisfy requirements", SysmlPackage.eINSTANCE.getDefinition_OwnedRequirement(), 10, 1,
                        SatisfyRequirementCompartmentItemNodeDescription.COMPARTMENT_ITEM_NAME))
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
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getUsage_NestedItem(), 4))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> partUsageSiblingAndChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), "parts", SysmlPackage.eINSTANCE.getUsage_NestedPart(), 13, 1, ""),
                Arguments.of(SysmlPackage.eINSTANCE.getSatisfyRequirementUsage(), "satisfy requirements", SysmlPackage.eINSTANCE.getUsage_NestedRequirement(), 10, 2,
                        SatisfyRequirementCompartmentItemNodeDescription.COMPARTMENT_ITEM_NAME))
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

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        this.creationTestsService = new NodeCreationTestsService(this.nodeCreationTester, this.descriptionNameGenerator, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID);
        this.diagramCheckerService = new DiagramCheckerService(this.diagramComparator, this.descriptionNameGenerator);
        this.semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID);
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("attributeDefinitionChildNodeParameters")
    public void createAttributeDefinitionSubNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        EClass parentEClass = SysmlPackage.eINSTANCE.getAttributeDefinition();
        String parentLabel = "AttributeDefinition";

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);

        Consumer<Object> diagramChecker = this.diagramCheckerService.compartmentNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, parentLabel, parentEClass, containmentReference,
                compartmentName, true);

        Runnable semanticChecker = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramChecker)
                .then(semanticChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("attributeUsageChildNodeParameters")
    public void createAttributeUsageSubNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        EClass parentEClass = SysmlPackage.eINSTANCE.getAttributeUsage();
        String parentLabel = "attribute";

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);

        Consumer<Object> diagramChecker = this.diagramCheckerService.compartmentNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, parentLabel, parentEClass, containmentReference,
                compartmentName, true);

        Runnable semanticChecker = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramChecker)
                .then(semanticChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("enumerationDefinitionChildNodeParameters")
    public void createEnumerationDefinitionSubNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        EClass parentEClass = SysmlPackage.eINSTANCE.getEnumerationDefinition();
        String parentLabel = "EnumerationDefinition";

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);

        Consumer<Object> diagramChecker = this.diagramCheckerService.compartmentNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, parentLabel, parentEClass, containmentReference,
                compartmentName, false);

        Runnable semanticChecker = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramChecker)
                .then(semanticChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("connectionDefinitionChildNodeParameters")
    public void createConnectionDefinitionSubNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        EClass parentEClass = SysmlPackage.eINSTANCE.getConnectionDefinition();
        String parentLabel = "ConnectionDefinition";

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);

        Consumer<Object> diagramChecker = this.diagramCheckerService.compartmentNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, parentLabel, parentEClass, containmentReference,
                compartmentName, true);

        Runnable semanticChecker = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramChecker)
                .then(semanticChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("connectionDefinitionSiblingNodeParameters")
    public void createConnectionDefinitionSiblingNodes(EClass childEClass, String compartmentName, EReference containmentReference, int compartmentCount, int newNodesCount) {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        EClass parentEClass = SysmlPackage.eINSTANCE.getConnectionDefinition();
        String parentLabel = "ConnectionDefinition";

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);

        Consumer<Object> diagramChecker = this.diagramCheckerService.siblingNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, childEClass, compartmentCount, newNodesCount);

        Runnable semanticChecker = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel,
                containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramChecker)
                .then(semanticChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("allocationDefinitionSiblingNodeParameters")
    public void creatAllocationDefinitionSiblingNodes(EClass childEClass, String compartmentName, EReference containmentReference, int compartmentCount, int newNodesCount) {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        EClass parentEClass = SysmlPackage.eINSTANCE.getAllocationDefinition();
        String parentLabel = "AllocationDefinition";

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);

        Consumer<Object> diagramChecker = this.diagramCheckerService.siblingNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, childEClass, compartmentCount, newNodesCount);

        Runnable semanticChecker = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel,
                containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramChecker)
                .then(semanticChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("interfaceDefinitionSiblingNodeParameters")
    public void creatInterfaceDefinitionSiblingNodes(EClass childEClass, String compartmentName, EReference containmentReference, int compartmentCount, int newNodesCount) {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        EClass parentEClass = SysmlPackage.eINSTANCE.getInterfaceDefinition();
        String parentLabel = "InterfaceDefinition";

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);

        Consumer<Object> diagramChecker = this.diagramCheckerService.siblingNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, childEClass, compartmentCount, newNodesCount);

        Runnable semanticChecker = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel,
                containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramChecker)
                .then(semanticChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("itemDefinitionSiblingNodeParameters")
    public void createItemDefinitionSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        EClass parentEClass = SysmlPackage.eINSTANCE.getItemDefinition();
        String parentLabel = "ItemDefinition";

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);

        Consumer<Object> diagramChecker = this.diagramCheckerService.siblingNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, childEClass, compartmentCount);

        Runnable semanticChecker = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramChecker)
                .then(semanticChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("itemDefinitionChildNodeParameters")
    public void createItemDefinitionSubNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        EClass parentEClass = SysmlPackage.eINSTANCE.getItemDefinition();
        String parentLabel = "ItemDefinition";

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);

        Consumer<Object> diagramChecker = this.diagramCheckerService.compartmentNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, parentLabel, parentEClass, containmentReference,
                compartmentName, true);

        Runnable semanticChecker = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramChecker)
                .then(semanticChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("itemUsageSiblingNodeParameters")
    public void createItemUsageSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        EClass parentEClass = SysmlPackage.eINSTANCE.getItemUsage();
        String parentLabel = "item";

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);

        Consumer<Object> diagramChecker = this.diagramCheckerService.siblingNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, childEClass, compartmentCount);

        Runnable semanticChecker = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramChecker)
                .then(semanticChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("itemUsageChildNodeParameters")
    public void createItemUsageChildNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        EClass parentEClass = SysmlPackage.eINSTANCE.getItemUsage();
        String parentLabel = "item";

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);

        Consumer<Object> diagramChecker = this.diagramCheckerService.compartmentNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, parentLabel, parentEClass, containmentReference,
                compartmentName, true);

        Runnable semanticChecker = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramChecker)
                .then(semanticChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("itemUsageBorderAndChildNodeParameters")
    public void createItemUsageBorderAndChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, EClass borderNodeType) {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        EClass parentEClass = SysmlPackage.eINSTANCE.getItemUsage();
        String parentLabel = "item";

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);

        Consumer<Object> diagramChecker = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewBorderNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(diagram.get(), newDiagram, true);
            String compartmentNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(compartmentNodeDescription)
                    .hasCompartmentCount(0)
                    .check(diagram.get(), newDiagram);
            String borderNodeDescription = this.descriptionNameGenerator.getBorderNodeName(borderNodeType, containmentReference);
            new CheckBorderNode(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .hasBorderNodeDescriptionName(borderNodeDescription)
                    .check(diagram.get(), newDiagram);
        });

        Runnable semanticChecker = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramChecker)
                .then(semanticChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("packageChildNodeParameters")
    public void createPackageChildNodes(EClass childEClass, EReference containmentReference, int compartmentCount, int additionalNodesCount, int newBorderNodes) {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        EClass parentEClass = SysmlPackage.eINSTANCE.getPackage();
        String parentLabel = "Package";
        List<ToolVariable> variables = List.of();
        if (SysmlPackage.eINSTANCE.getNamespaceImport().equals(childEClass)) {
            variables = List.of(new ToolVariable("selectedObject", GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID, ToolVariableType.OBJECT_ID));
        }

        List<ToolVariable> finalVariables = variables;
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass, finalVariables);

        Consumer<Object> diagramChecker = this.diagramCheckerService.childNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, parentLabel, childEClass, compartmentCount,
                1 + compartmentCount + additionalNodesCount, newBorderNodes);

        Runnable semanticChecker = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramChecker)
                .then(semanticChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("partDefinitionChildNodeParameters")
    public void createPartDefinitionChildNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        EClass parentEClass = SysmlPackage.eINSTANCE.getPartDefinition();
        String parentLabel = "PartDefinition";

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);

        Consumer<Object> diagramChecker = this.diagramCheckerService.compartmentNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, parentLabel, parentEClass, containmentReference,
                compartmentName, true);

        Runnable semanticChecker = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramChecker)
                .then(semanticChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("partDefinitionSiblingNodeParameters")
    public void createPartDefinitionSiblingNodes(EClass childEClass, EReference containmentReference) {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        EClass parentEClass = SysmlPackage.eINSTANCE.getPartDefinition();
        String parentLabel = "PartDefinition";

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);

        Consumer<Object> diagramChecker = assertRefreshedDiagramThat(newDiagram -> {
            int createdNodesExpectedCount = 1;
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(createdNodesExpectedCount)
                    .hasNewEdgeCount(1)
                    .check(diagram.get(), newDiagram, true);
        });

        Runnable semanticChecker = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramChecker)
                .then(semanticChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("partDefinitionSiblingAndChildNodeParameters")
    public void createPartDefinitionSiblingAndChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, int expectedNumberOfNewNodes,
            int expectedNumberOfNewEdges, String compartmentNodeDecriptionNameSuffix) {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        EClass parentEClass = SysmlPackage.eINSTANCE.getPartDefinition();
        String parentLabel = "PartDefinition";

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);

        Consumer<Object> diagramChecker = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(expectedNumberOfNewNodes)
                    .hasNewEdgeCount(expectedNumberOfNewEdges)
                    .check(diagram.get(), newDiagram);
            String compartmentItemNodeDescriptionName = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference) + compartmentNodeDecriptionNameSuffix;
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(compartmentItemNodeDescriptionName)
                    .hasCompartmentCount(0)
                    .check(diagram.get(), newDiagram);
        });

        Runnable semanticChecker = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramChecker)
                .then(semanticChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("partUsageChildNodeParameters")
    public void createPartUsageChildNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        EClass parentEClass = SysmlPackage.eINSTANCE.getPartUsage();
        String parentLabel = "part";

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);

        Consumer<Object> diagramChecker = this.diagramCheckerService.compartmentNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, parentLabel, parentEClass, containmentReference,
                compartmentName, true);

        Runnable semanticChecker = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramChecker)
                .then(semanticChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("partUsageSiblingNodeParameters")
    public void createPartUsageSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        EClass parentEClass = SysmlPackage.eINSTANCE.getPartUsage();
        String parentLabel = "part";

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);

        Consumer<Object> diagramChecker = this.diagramCheckerService.siblingNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, childEClass, compartmentCount);

        Runnable semanticChecker = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramChecker)
                .then(semanticChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("partUsageSiblingAndChildNodeParameters")
    public void createPartUsageSiblingAndChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, int expectedNumberOfNewNodes,
            int expectedNumberOfNewEdges, String compartmentNodeDecriptionNameSuffix) {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        EClass parentEClass = SysmlPackage.eINSTANCE.getPartUsage();
        String parentLabel = "part";

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);

        Consumer<Object> diagramChecker = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(expectedNumberOfNewNodes)
                    .hasNewEdgeCount(expectedNumberOfNewEdges)
                    .check(diagram.get(), newDiagram);
            String compartmentItemNodeDescriptionName = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference) + compartmentNodeDecriptionNameSuffix;
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(compartmentItemNodeDescriptionName)
                    .hasCompartmentCount(0)
                    .check(diagram.get(), newDiagram);
        });

        Runnable semanticChecker = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramChecker)
                .then(semanticChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("directedPortsInPartDefinitionChildNodeParameters")
    public void createDirectedPortsInPartDefinitionChildNodeParameters(EClass childEClass, String compartmentName, EReference containmentReference, String creationToolName) {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        EClass parentEClass = SysmlPackage.eINSTANCE.getPartDefinition();
        String parentLabel = "PartDefinition";

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, creationToolName);

        Consumer<Object> diagramChecker = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewBorderNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(diagram.get(), newDiagram, true);
            String compartmentNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(compartmentNodeDescription)
                    .hasCompartmentCount(0)
                    .check(diagram.get(), newDiagram);
            String borderNodeDescription = this.descriptionNameGenerator.getBorderNodeName(childEClass, containmentReference);
            new CheckBorderNode(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .hasBorderNodeDescriptionName(borderNodeDescription)
                    .check(diagram.get(), newDiagram);
        });

        Runnable semanticChecker = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramChecker)
                .then(semanticChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
