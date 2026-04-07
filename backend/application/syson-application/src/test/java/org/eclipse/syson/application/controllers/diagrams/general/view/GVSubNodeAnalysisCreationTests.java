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
import static org.assertj.core.api.InstanceOfAssertFactories.type;
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controller.editingcontext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckBorderNode;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckNodeInCompartment;
import org.eclipse.syson.application.controllers.diagrams.checkers.DiagramCheckerService;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.controllers.utils.TestNameGenerator;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.NodeCreationTestsService;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Tests the creation of "Analysis" sub nodes section in the General View diagram.
 *
 * @author arichard
 */
@Transactional
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVSubNodeAnalysisCreationTests extends AbstractIntegrationTests {

    private static final String CASE = "case";

    private static final String USE_CASE = "useCase";

    private static final String DOC_COMPARTMENT = "doc";

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

    private NodeCreationTestsService creationTestsService;

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private DiagramCheckerService diagramCheckerService;

    private SemanticCheckerService semanticCheckerService;

    private static Stream<Arguments> caseUsageSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getUsage_NestedItem(), 4),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPart(), 11))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> caseUsageChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getDocumentation(), DOC_COMPARTMENT, SysmlPackage.eINSTANCE.getElement_Documentation()),
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), "attributes", SysmlPackage.eINSTANCE.getUsage_NestedAttribute()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> caseUsageBorderAndChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), "ports", SysmlPackage.eINSTANCE.getUsage_NestedPort(), SysmlPackage.eINSTANCE.getPortUsage()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> caseDefinitionChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getDocumentation(), DOC_COMPARTMENT, SysmlPackage.eINSTANCE.getElement_Documentation()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> useCaseUsageSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getUsage_NestedItem(), 4),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPart(), 11))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> useCaseUsageChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getDocumentation(), DOC_COMPARTMENT, SysmlPackage.eINSTANCE.getElement_Documentation()),
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), "attributes", SysmlPackage.eINSTANCE.getUsage_NestedAttribute()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> useCaseUsageBorderAndChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), "ports", SysmlPackage.eINSTANCE.getUsage_NestedPort(), SysmlPackage.eINSTANCE.getPortUsage()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> useCaseDefinitionChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getDocumentation(), DOC_COMPARTMENT, SysmlPackage.eINSTANCE.getElement_Documentation()))
                .map(TestNameGenerator::namedArguments);
    }

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(), GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
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
    @MethodSource("caseUsageSiblingNodeParameters")
    public void createCaseUsageSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getCaseUsage();
        String targetObjectId = GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_USAGE_ID;
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, targetObjectId, childEClass);
        Consumer<Object> diagramCheck = this.diagramCheckerService.siblingNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, childEClass, compartmentCount);
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(CASE, containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("caseUsageChildNodeParameters")
    public void createCaseUsageChildNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getCaseUsage();
        String targetObjectId = GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_USAGE_ID;
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, targetObjectId, childEClass);
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram, true);
            String listStatesNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withTargetObjectId(targetObjectId)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(listStatesNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        });
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(CASE, containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("caseUsageBorderAndChildNodeParameters")
    public void createCaseUsageBorderAndChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, EClass borderNodeType) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getCaseUsage();
        String targetObjectId = GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_USAGE_ID;
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, targetObjectId, childEClass);
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewBorderNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram, true);
            String compartmentNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withTargetObjectId(targetObjectId)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(compartmentNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
            String borderNodeDescription = this.descriptionNameGenerator.getBorderNodeName(borderNodeType, containmentReference);
            new CheckBorderNode(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentTargetObjectId(targetObjectId)
                    .hasBorderNodeDescriptionName(borderNodeDescription)
                    .check(initialDiagram, newDiagram);
        });
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(CASE, containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("caseDefinitionChildNodeParameters")
    public void createCaseDefinitionChildNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getCaseDefinition();
        String targetObjectId = GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_DEFINITION_ID;
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, targetObjectId, childEClass);
        Consumer<Object> diagramCheck = this.diagramCheckerService.compartmentNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, targetObjectId, parentEClass, containmentReference, compartmentName, false);
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker("CaseDefinition", containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("useCaseUsageSiblingNodeParameters")
    public void createUseCaseUsageSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getUseCaseUsage();
        String targetObjectId = GeneralViewWithTopNodesTestProjectData.SemanticIds.USE_CASE_USAGE_ID;
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, targetObjectId, childEClass);
        Consumer<Object> diagramCheck = this.diagramCheckerService.siblingNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, childEClass, compartmentCount);
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(USE_CASE, containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("useCaseUsageChildNodeParameters")
    public void createUseCaseUsageChildNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getUseCaseUsage();
        String targetObjectId = GeneralViewWithTopNodesTestProjectData.SemanticIds.USE_CASE_USAGE_ID;
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, targetObjectId, childEClass);
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram, true);
            String listStatesNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withTargetObjectId(targetObjectId)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(listStatesNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        });
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(USE_CASE, containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("useCaseUsageBorderAndChildNodeParameters")
    public void createUseCaseUsageBorderAndChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, EClass borderNodeType) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getUseCaseUsage();
        String targetObjectId = GeneralViewWithTopNodesTestProjectData.SemanticIds.USE_CASE_USAGE_ID;
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, targetObjectId, childEClass);
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewBorderNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram, true);
            String compartmentNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withTargetObjectId(targetObjectId)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(compartmentNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
            String borderNodeDescription = this.descriptionNameGenerator.getBorderNodeName(borderNodeType, containmentReference);
            new CheckBorderNode(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentTargetObjectId(targetObjectId)
                    .hasBorderNodeDescriptionName(borderNodeDescription)
                    .check(initialDiagram, newDiagram);
        });
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(USE_CASE, containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("useCaseDefinitionChildNodeParameters")
    public void createUseCaseDefinitionChildNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getUseCaseDefinition();
        String targetObjectId = GeneralViewWithTopNodesTestProjectData.SemanticIds.USE_CASE_DEFINITION_ID;
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, targetObjectId, childEClass);
        Consumer<Object> diagramCheck = this.diagramCheckerService.compartmentNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, targetObjectId, parentEClass, containmentReference, compartmentName, false);
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker("UseCaseDefinition", containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a Case, WHEN creating a new Subject selecting a Part, THEN the Subject subsetted by the Part is created in the Case")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void createNewUsageSubjectInCaseUsage() {
        this.createSubjectWithSubsettingInCaseUsage(SysmlPackage.eINSTANCE.getCaseUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_USAGE_ID, CASE);
    }

    @DisplayName("GIVEN a UseCase, WHEN creating a new Subject selecting a Part, THEN the Subject subsetted by the Part is created in the UseCase")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void createNewUsageSubjectInUseCaseUsage() {
        this.createSubjectWithSubsettingInCaseUsage(SysmlPackage.eINSTANCE.getUseCaseUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.USE_CASE_USAGE_ID, USE_CASE);
    }

    @DisplayName("GIVEN a Case, WHEN creating a new Subject selecting a PartDefinition, THEN the Subject typed by the PartDefinition is created in the Case")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void createNewDefinitionSubjectInCaseUsage() {
        this.createSubjectWithFeatureTypingInCaseUsage(SysmlPackage.eINSTANCE.getCaseUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_USAGE_ID, CASE);
    }

    @DisplayName("GIVEN a UseCase, WHEN creating a new Subject selecting a PartDefinition, THEN the Subject typed by the PartDefinition is created in the UseCase")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void createNewDefinitionSubjectInUseCaseUsage() {
        this.createSubjectWithFeatureTypingInCaseUsage(SysmlPackage.eINSTANCE.getUseCaseUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.USE_CASE_USAGE_ID, USE_CASE);
    }

    @DisplayName("GIVEN a Case, WHEN creating a new Subject without selection, THEN the Subject without specialization is created in the Case")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void createNewSubjectWithoutSpecializationInCaseUsage() {
        this.createSubjectWithoutSpecializationInCaseUsage(SysmlPackage.eINSTANCE.getCaseUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_USAGE_ID, CASE);
    }

    @DisplayName("GIVEN a UseCase, WHEN creating a new Subject without selection, THEN the Subject without specialization is created in the UseCase")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void createNewSubjectWithoutSpecializationInUseCaseUsage() {
        this.createSubjectWithoutSpecializationInCaseUsage(SysmlPackage.eINSTANCE.getUseCaseUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.USE_CASE_USAGE_ID, USE_CASE);
    }

    @DisplayName("GIVEN a Case, WHEN creating a new Actor selecting a Part, THEN the Actor subsetted by the Part is created in the Case")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void createNewActorWithSubsettingInCaseUsage() {
        this.createActorWithSubsettingInCaseUsage(SysmlPackage.eINSTANCE.getCaseUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_USAGE_ID, CASE);
    }

    @DisplayName("GIVEN a UseCase, WHEN creating a new Actor selecting a Part, THEN the Actor subsetted by the Part is created in the UseCase")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void createNewActorWithSubsettingInUseCaseUsage() {
        this.createActorWithSubsettingInCaseUsage(SysmlPackage.eINSTANCE.getUseCaseUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.USE_CASE_USAGE_ID, USE_CASE);
    }

    @DisplayName("GIVEN a Case, WHEN creating a new Actor selecting a PartDefinition, THEN the Actor typed by the PartDefinition is created in the Case")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void createNewActorWithFeatureTypingInCaseUsage() {
        this.createActorWithFeatureTypingInCaseUsage(SysmlPackage.eINSTANCE.getCaseUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_USAGE_ID, CASE);
    }

    @DisplayName("GIVEN a UseCase, WHEN creating a new Actor selecting a PartDefinition, THEN the Actor typed by the PartDefinition is created in the UseCase")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void createNewActorWithFeatureTypingInUseCaseUsage() {
        this.createActorWithFeatureTypingInCaseUsage(SysmlPackage.eINSTANCE.getUseCaseUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.USE_CASE_USAGE_ID, USE_CASE);
    }

    @DisplayName("GIVEN a Case, WHEN creating a new Actor without selection, THEN the Actor without specialization is created in the Case")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void createNewActorWithoutSpecializationInCaseUsage() {
        this.createActorWithoutSpecializationInCaseUsage(SysmlPackage.eINSTANCE.getCaseUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_USAGE_ID, CASE);
    }

    @DisplayName("GIVEN a UseCase, WHEN creating a new Actor without selection, THEN the Actor without specialization is created in the UseCase")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void createNewActorWithoutSpecializationInUseCaseUsage() {
        this.createActorWithoutSpecializationInCaseUsage(SysmlPackage.eINSTANCE.getUseCaseUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.USE_CASE_USAGE_ID, USE_CASE);
    }

    private void createSubjectWithSubsettingInCaseUsage(EClass caseUsageSubclass, String targetObjectId, String parentLabel) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass childEClass = SysmlPackage.eINSTANCE.getReferenceUsage();
        String creationToolName = "New Subject";
        EReference containmentReference = SysmlPackage.eINSTANCE.getCaseUsage_SubjectParameter();

        Runnable createNodeRunnable = this.creationTestsService.createNodeWithSelectionDialogWithSingleSelection(diagramDescriptionIdProvider, diagram, caseUsageSubclass, targetObjectId, creationToolName, GeneralViewWithTopNodesTestProjectData.SemanticIds.PART_USAGE_ID);

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            int createdNodesExpectedCount = 2;
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(createdNodesExpectedCount)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);
            // Only the node inside the compartment is visible
            // The "sibling" node is hidden
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram, true);
            String listNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(SysmlPackage.eINSTANCE.getCaseUsage(), containmentReference);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withTargetObjectId(targetObjectId)
                    .withCompartmentName("subject")
                    .hasNodeDescriptionName(listNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        });

        Consumer<Object> additionalCheck = referencedObject -> {
            assertThat(referencedObject)
                    .isInstanceOf(ReferenceUsage.class)
                    .asInstanceOf(type(ReferenceUsage.class))
                    .satisfies(referenceUsage -> {
                        EList<Subsetting> subjectSubsets = referenceUsage.getOwnedSubsetting();
                        assertThat(subjectSubsets).isNotEmpty();
                        assertThat(subjectSubsets.get(0).getSubsettedFeature().getName()).isEqualTo("part");
                    });
        };

        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass, additionalCheck));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    private void createSubjectWithFeatureTypingInCaseUsage(EClass caseUsageSubclass, String targetObjectId, String parentLabel) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass childEClass = SysmlPackage.eINSTANCE.getReferenceUsage();
        String creationToolName = "New Subject";
        EReference containmentReference = SysmlPackage.eINSTANCE.getCaseUsage_SubjectParameter();

        Runnable createNodeRunnable = this.creationTestsService.createNodeWithSelectionDialogWithSingleSelection(diagramDescriptionIdProvider, diagram, caseUsageSubclass, targetObjectId, creationToolName, GeneralViewWithTopNodesTestProjectData.SemanticIds.PART_DEFINITION_ID);

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            int createdNodesExpectedCount = 2;
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(createdNodesExpectedCount)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);
            // Only the node inside the compartment is visible
            // The "sibling" node is hidden
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram, true);
            String listNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(SysmlPackage.eINSTANCE.getCaseUsage(), containmentReference);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withTargetObjectId(targetObjectId)
                    .withCompartmentName("subject")
                    .hasNodeDescriptionName(listNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        });

        Consumer<Object> additionalCheck = referencedObject -> {
            assertThat(referencedObject)
                    .isInstanceOf(ReferenceUsage.class)
                    .asInstanceOf(type(ReferenceUsage.class))
                    .satisfies(referenceUsage -> {
                        EList<Type> types = referenceUsage.getType();
                        assertThat(types).isNotEmpty();
                        assertThat(types.get(0).getName()).isEqualTo("PartDefinition");
                    });
        };

        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass, additionalCheck));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    private void createSubjectWithoutSpecializationInCaseUsage(EClass caseUsageSubclass, String targetObjectId, String parentLabel) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass childEClass = SysmlPackage.eINSTANCE.getReferenceUsage();
        String creationToolName = "New Subject";
        EReference containmentReference = SysmlPackage.eINSTANCE.getCaseUsage_SubjectParameter();

        Runnable createNodeRunnable = this.creationTestsService.createNodeWithSelectionDialogWithoutSelectionProvided(diagramDescriptionIdProvider, diagram, caseUsageSubclass, targetObjectId, creationToolName);

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            int createdNodesExpectedCount = 2;
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(createdNodesExpectedCount)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);
            // Only the node inside the compartment is visible
            // The "sibling" node is hidden
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram, true);
            String listNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(SysmlPackage.eINSTANCE.getCaseUsage(), containmentReference);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withTargetObjectId(targetObjectId)
                    .withCompartmentName("subject")
                    .hasNodeDescriptionName(listNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        });

        Consumer<Object> additionalCheck = referencedObject -> {
            assertThat(referencedObject)
                    .isInstanceOf(ReferenceUsage.class)
                    .asInstanceOf(type(ReferenceUsage.class))
                    .satisfies(referenceUsage -> {
                        assertThat(referenceUsage.getOwnedSpecialization()).allMatch(Specialization::isIsImplied);
                        assertThat(referenceUsage.getType()).isEmpty();
                    });
        };

        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass, additionalCheck));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    private void createActorWithSubsettingInCaseUsage(EClass caseUsageSubclass, String targetObjectId, String parentLabel) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass childEClass = SysmlPackage.eINSTANCE.getPartUsage();
        String creationToolName = "New Actor";
        EReference containmentReference = SysmlPackage.eINSTANCE.getCaseUsage_ActorParameter();
        String existingPartId = GeneralViewWithTopNodesTestProjectData.SemanticIds.PART_USAGE_ID;

        Runnable createNodeRunnable = this.creationTestsService.createNodeWithSelectionDialogWithSingleSelection(diagramDescriptionIdProvider, diagram, caseUsageSubclass, targetObjectId, creationToolName, existingPartId);

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            int createdNodesExpectedCount = 1;
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(createdNodesExpectedCount)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram, true);
        });

        Consumer<Object> additionalCheck = referencedObject -> {
            assertThat(referencedObject).isInstanceOf(List.class)
                    .asInstanceOf(type(List.class))
                    .satisfies(actors -> {
                        assertThat((List<?>) actors).size().isEqualTo(1);
                        assertThat(actors.getFirst())
                                .isInstanceOf(PartUsage.class)
                                .asInstanceOf(type(PartUsage.class))
                                .satisfies(actor -> {
                                    EList<Subsetting> subjectSubsets = actor.getOwnedSubsetting();
                                    assertThat(subjectSubsets).isNotEmpty();
                                    assertThat(subjectSubsets.get(0).getSubsettedFeature().getName()).isEqualTo("part");
                                });
                    });
        };

        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass, additionalCheck));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    private void createActorWithFeatureTypingInCaseUsage(EClass caseUsageSubclass, String targetObjectId, String parentLabel) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass childEClass = SysmlPackage.eINSTANCE.getPartUsage();
        String creationToolName = "New Actor";
        EReference containmentReference = SysmlPackage.eINSTANCE.getCaseUsage_ActorParameter();
        String existingPartDefId = GeneralViewWithTopNodesTestProjectData.SemanticIds.PART_DEFINITION_ID;

        Runnable createNodeRunnable = this.creationTestsService.createNodeWithSelectionDialogWithSingleSelection(diagramDescriptionIdProvider, diagram, caseUsageSubclass, targetObjectId, creationToolName, existingPartDefId);

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            int createdNodesExpectedCount = 1;
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(createdNodesExpectedCount)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram, true);
        });

        Consumer<Object> additionalCheck = referencedObject -> {
            assertThat(referencedObject).isInstanceOf(List.class)
                    .asInstanceOf(type(List.class))
                    .satisfies(actors -> {
                        assertThat((List<?>) actors).size().isEqualTo(1);
                        assertThat(actors.getFirst())
                                .isInstanceOf(PartUsage.class)
                                .asInstanceOf(type(PartUsage.class))
                                .satisfies(actor -> {
                                    EList<Type> types = actor.getType();
                                    assertThat(types).isNotEmpty();
                                    assertThat(types.get(0).getName()).isEqualTo("PartDefinition");
                                });
                    });
        };

        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass, additionalCheck));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    private void createActorWithoutSpecializationInCaseUsage(EClass caseUsageSubclass, String targetObjectId, String parentLabel) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass childEClass = SysmlPackage.eINSTANCE.getPartUsage();
        String creationToolName = "New Actor";
        EReference containmentReference = SysmlPackage.eINSTANCE.getCaseUsage_ActorParameter();

        Runnable createNodeRunnable = this.creationTestsService.createNodeWithSelectionDialogWithoutSelectionProvided(diagramDescriptionIdProvider, diagram, caseUsageSubclass, targetObjectId, creationToolName);

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            int createdNodesExpectedCount = 1;
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(createdNodesExpectedCount)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram, true);
        });

        Consumer<Object> additionalCheck = referencedObject -> {
            assertThat(referencedObject).isInstanceOf(List.class)
                    .asInstanceOf(type(List.class))
                    .satisfies(actors -> {
                        assertThat((List<?>) actors).size().isEqualTo(1);
                        assertThat(actors.getFirst())
                                .isInstanceOf(PartUsage.class)
                                .asInstanceOf(type(PartUsage.class))
                                .satisfies(actor -> {
                                    assertThat(actor.getOwnedSpecialization()).allMatch(Specialization::isIsImplied);
                                    EList<Type> types = actor.getType();
                                    assertThat(types)
                                            .isNotEmpty()
                                            .allMatch(Element::isIsLibraryElement);
                                });
                    });
        };

        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass, additionalCheck));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
