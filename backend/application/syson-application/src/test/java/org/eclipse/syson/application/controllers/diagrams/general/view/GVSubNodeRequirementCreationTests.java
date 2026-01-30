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
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.eclipse.emf.common.util.EList;
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
import org.eclipse.syson.application.controller.editingContext.checkers.ISemanticChecker;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
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
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.BeforeEach;
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
 * Tests the creation of the "Requirement" sub nodes section in the General View diagram.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVSubNodeRequirementCreationTests extends AbstractIntegrationTests {

    private static final String ATTRIBUTES_COMPARTMENT = "attributes";

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

    private static Stream<Arguments> concernUsageSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getUsage_NestedItem(), 4),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPart(), 11),
                Arguments.of(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getUsage_NestedRequirement(), 8))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> concernUsageChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ATTRIBUTES_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), null),
                Arguments.of(SysmlPackage.eINSTANCE.getDocumentation(), DOC_COMPARTMENT, SysmlPackage.eINSTANCE.getElement_Documentation(), null))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> concernUsageSiblingAndChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getConstraintUsage(), "assume constraints", SysmlPackage.eINSTANCE.getRequirementUsage_AssumedConstraint(), "New Assume constraint", 6, 1),
                Arguments.of(SysmlPackage.eINSTANCE.getConstraintUsage(), "require constraints", SysmlPackage.eINSTANCE.getRequirementUsage_RequiredConstraint(), "New Require constraint", 6, 1))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> concernUsageBorderAndChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), "ports", SysmlPackage.eINSTANCE.getUsage_NestedPort(), SysmlPackage.eINSTANCE.getPortUsage()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> concernDefinitionChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ATTRIBUTES_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute(), null),
                Arguments.of(SysmlPackage.eINSTANCE.getDocumentation(), DOC_COMPARTMENT, SysmlPackage.eINSTANCE.getElement_Documentation(), null))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> concernDefinitionSiblingAndChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getRequirementUsage(), "requirements", SysmlPackage.eINSTANCE.getDefinition_OwnedRequirement(), null, 10, 1),
                Arguments.of(SysmlPackage.eINSTANCE.getConstraintUsage(), "assume constraints", SysmlPackage.eINSTANCE.getRequirementDefinition_AssumedConstraint(), "New Assume constraint", 6, 1),
                Arguments.of(SysmlPackage.eINSTANCE.getConstraintUsage(), "require constraints", SysmlPackage.eINSTANCE.getRequirementDefinition_RequiredConstraint(), "New Require constraint", 6, 1))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> constraintUsageSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getUsage_NestedItem(), 4),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPart(), 11))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> constraintUsageChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ATTRIBUTES_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedAttribute()),
                Arguments.of(SysmlPackage.eINSTANCE.getDocumentation(), DOC_COMPARTMENT, SysmlPackage.eINSTANCE.getElement_Documentation()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> constraintUsageSiblingAndChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getConstraintUsage(), "constraints", SysmlPackage.eINSTANCE.getUsage_NestedConstraint(), 6, 1))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> constraintUsageBorderAndChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), "ports", SysmlPackage.eINSTANCE.getUsage_NestedPort(), SysmlPackage.eINSTANCE.getPortUsage()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> constraintDefinitionSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedUsage(), 11))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> constraintDefinitionChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getDocumentation(), DOC_COMPARTMENT, SysmlPackage.eINSTANCE.getElement_Documentation(), 1))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> constraintDefinitionSiblingAndChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getConstraintUsage(), "constraints", SysmlPackage.eINSTANCE.getDefinition_OwnedConstraint(), 6, 1))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> requirementUsageSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getUsage_NestedItem(), 4),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPart(), 11),
                Arguments.of(SysmlPackage.eINSTANCE.getRequirementUsage(), SysmlPackage.eINSTANCE.getUsage_NestedRequirement(), 8))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> requirementUsageChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ATTRIBUTES_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedAttribute()),
                Arguments.of(SysmlPackage.eINSTANCE.getDocumentation(), DOC_COMPARTMENT, SysmlPackage.eINSTANCE.getElement_Documentation()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> requirementUsageBorderAndChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), "ports", SysmlPackage.eINSTANCE.getUsage_NestedPort(), SysmlPackage.eINSTANCE.getPortUsage()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> requirementDefinitionChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), ATTRIBUTES_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedAttribute()),
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
    @MethodSource("concernUsageSiblingNodeParameters")
    public void createConcernUsageSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getConcernUsage();
        String parentLabel = "concern";
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);
        Consumer<Object> diagramCheck = this.diagramCheckerService.siblingNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, childEClass, compartmentCount);
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

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
    @MethodSource("concernUsageChildNodeParameters")
    public void createConcernUsageChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, String creationToolNameParameter) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getConcernUsage();
        String parentLabel = "concern";

        final String creationToolName;
        if (creationToolNameParameter != null) {
            creationToolName = creationToolNameParameter;
        } else {
            creationToolName = this.descriptionNameGenerator.getCreationToolName(childEClass);
        }

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, creationToolName);
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram, true);
            String listStatesNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(listStatesNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        });
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

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
    @MethodSource("concernUsageSiblingAndChildNodeParameters")
    public void createConcernUsageSiblingAndChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, String creationToolNameParameter, int expectedNumberOfNewNodes,
            int expectedNumberOfNewEdges) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getConcernUsage();
        String parentLabel = "concern";

        final String creationToolName;
        if (creationToolNameParameter != null) {
            creationToolName = creationToolNameParameter;
        } else {
            creationToolName = this.descriptionNameGenerator.getCreationToolName(childEClass);
        }

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, creationToolName);
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(expectedNumberOfNewNodes)
                    .hasNewEdgeCount(expectedNumberOfNewEdges)
                    .check(initialDiagram, newDiagram);
            String listStatesNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(listStatesNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        });
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

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
    @MethodSource("concernUsageBorderAndChildNodeParameters")
    public void createConcernUsageBorderAndChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, EClass borderNodeType) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getConcernUsage();
        String parentLabel = "concern";
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewBorderNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram, true);
            String compartmentNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(compartmentNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
            String borderNodeDescription = this.descriptionNameGenerator.getBorderNodeName(borderNodeType, containmentReference);
            new CheckBorderNode(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .hasBorderNodeDescriptionName(borderNodeDescription)
                    .check(initialDiagram, newDiagram);
        });
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

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
    @MethodSource("concernDefinitionSiblingAndChildNodeParameters")
    public void createConcernDefinitionSiblingAndChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, String creationToolNameParameter, int expectedNumberOfNewNodes,
            int expectedNumberOfNewEdges) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getConcernDefinition();
        String parentLabel = "ConcernDefinition";

        final String creationToolName;
        if (creationToolNameParameter != null) {
            creationToolName = creationToolNameParameter;
        } else {
            creationToolName = this.descriptionNameGenerator.getCreationToolName(childEClass);
        }

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, creationToolName);
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(expectedNumberOfNewNodes)
                    .hasNewEdgeCount(expectedNumberOfNewEdges)
                    .check(initialDiagram, newDiagram);
            String listStatesNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(listStatesNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        });
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

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
    @MethodSource("concernDefinitionChildNodeParameters")
    public void createConcernDefinitionChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, String creationToolNameParameter) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getConcernDefinition();
        String parentLabel = "ConcernDefinition";

        final String creationToolName;
        if (creationToolNameParameter != null) {
            creationToolName = creationToolNameParameter;
        } else {
            creationToolName = this.descriptionNameGenerator.getCreationToolName(childEClass);
        }

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, creationToolName);
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram, true);
            String listStatesNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(listStatesNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        });
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

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
    @MethodSource("constraintUsageSiblingNodeParameters")
    public void createConstraintUsageSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getConstraintUsage();
        String parentLabel = "constraint";
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);
        Consumer<Object> diagramCheck = this.diagramCheckerService.siblingNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, childEClass, compartmentCount);
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

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
    @MethodSource("constraintUsageChildNodeParameters")
    public void createConstraintUsageChildNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getConstraintUsage();
        String parentLabel = "constraint";
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram, true);
            String listStatesNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(listStatesNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        });
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

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
    @MethodSource("constraintUsageSiblingAndChildNodeParameters")
    public void createConstraintUsageSiblingAndChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, int expectedNumberOfNewNodes,
            int expectedNumberOfNewEdges) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getConstraintUsage();
        String parentLabel = "constraint";
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(expectedNumberOfNewNodes)
                    .hasNewEdgeCount(expectedNumberOfNewEdges)
                    .check(initialDiagram, newDiagram);
            String listStatesNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(listStatesNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        });
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

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
    @MethodSource("constraintUsageBorderAndChildNodeParameters")
    public void createConstraintUsageBorderAndChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, EClass borderNodeType) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getConstraintUsage();
        String parentLabel = "constraint";
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewBorderNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram, true);
            String compartmentNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(compartmentNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
            String borderNodeDescription = this.descriptionNameGenerator.getBorderNodeName(borderNodeType, containmentReference);
            new CheckBorderNode(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .hasBorderNodeDescriptionName(borderNodeDescription)
                    .check(initialDiagram, newDiagram);
        });
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

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
    @MethodSource("constraintDefinitionSiblingNodeParameters")
    public void createConstraintDefinitionSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getConstraintDefinition();
        String parentLabel = "ConstraintDefinition";
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);
        Consumer<Object> diagramCheck = this.diagramCheckerService.siblingNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, childEClass, compartmentCount);
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

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
    @MethodSource("constraintDefinitionChildNodeParameters")
    public void createConstraintDefinitionChildNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getConstraintDefinition();
        String parentLabel = "ConstraintDefinition";
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);
        Consumer<Object> diagramCheck = this.diagramCheckerService.compartmentNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, parentLabel, parentEClass, containmentReference,
                compartmentName);
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

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
    @MethodSource("constraintDefinitionSiblingAndChildNodeParameters")
    public void createConstraintDefinitionSiblingAndChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, int expectedNumberOfNewNodes,
            int expectedNumberOfNewEdges) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getConstraintDefinition();
        String parentLabel = "ConstraintDefinition";
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(expectedNumberOfNewNodes)
                    .hasNewEdgeCount(expectedNumberOfNewEdges)
                    .check(initialDiagram, newDiagram);
            String listStatesNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(listStatesNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        });
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

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
    @MethodSource("requirementUsageSiblingNodeParameters")
    public void createRequirementUsageSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getRequirementUsage();
        String parentLabel = "requirement";
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);
        Consumer<Object> diagramCheck = this.diagramCheckerService.siblingNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, childEClass, compartmentCount);
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

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
    @MethodSource("requirementUsageChildNodeParameters")
    public void createSatisfyRequirementUsageChildNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getSatisfyRequirementUsage();
        String parentLabel = "satisfy requirement";
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram, true);
            String listStatesNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(listStatesNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        });
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

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
    @MethodSource("requirementUsageBorderAndChildNodeParameters")
    public void createSatisfyRequirementUsageBorderAndChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, EClass borderNodeType) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getSatisfyRequirementUsage();
        String parentLabel = "satisfy requirement";
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewBorderNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram, true);
            String compartmentNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(compartmentNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
            String borderNodeDescription = this.descriptionNameGenerator.getBorderNodeName(borderNodeType, containmentReference);
            new CheckBorderNode(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .hasBorderNodeDescriptionName(borderNodeDescription)
                    .check(initialDiagram, newDiagram);
        });
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

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
    @MethodSource("requirementUsageSiblingNodeParameters")
    public void createSatisfyRequirementUsageSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getSatisfyRequirementUsage();
        String parentLabel = "satisfy requirement";
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);
        Consumer<Object> diagramCheck = this.diagramCheckerService.siblingNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, childEClass, compartmentCount);
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

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
    @MethodSource("requirementUsageChildNodeParameters")
    public void createRequirementUsageChildNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getRequirementUsage();
        String parentLabel = "requirement";
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram, true);
            String listStatesNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(listStatesNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        });
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

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
    @MethodSource("requirementUsageBorderAndChildNodeParameters")
    public void createRequirementUsageBorderAndChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, EClass borderNodeType) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getRequirementUsage();
        String parentLabel = "requirement";
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewBorderNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram, true);
            String compartmentNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(compartmentNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
            String borderNodeDescription = this.descriptionNameGenerator.getBorderNodeName(borderNodeType, containmentReference);
            new CheckBorderNode(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .hasBorderNodeDescriptionName(borderNodeDescription)
                    .check(initialDiagram, newDiagram);
        });
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

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
    @MethodSource("requirementDefinitionChildNodeParameters")
    public void createRequirementDefinitionChildNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getRequirementDefinition();
        String parentLabel = "RequirementDefinition";
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram, true);
            String listStatesNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(listStatesNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        });
        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void createNewStakeholderInConcernDefinition() {
        this.createNewStakeholderIn(SysmlPackage.eINSTANCE.getConcernDefinition(), "ConcernDefinition");
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void createNewStakeholderInConcernUsage() {
        this.createNewStakeholderIn(SysmlPackage.eINSTANCE.getConcernUsage(), "concern");
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void createNewStakeholderInRequirementDefinition() {
        this.createNewStakeholderIn(SysmlPackage.eINSTANCE.getRequirementDefinition(), "RequirementDefinition");
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void createNewStakeholderInRequirementUsage() {
        this.createNewStakeholderIn(SysmlPackage.eINSTANCE.getRequirementUsage(), "requirement");
    }

    private void createNewStakeholderIn(EClass eClassWithStakeholderParameter, String parentNodeLabel) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        final EReference stakeholderParameterEReference = eClassWithStakeholderParameter.getEAllReferences().stream()
                .filter(eReference -> eReference.getName().equals("stakeholderParameter") && eReference.getEType() == SysmlPackage.eINSTANCE.getPartUsage()).findFirst()
                .orElseGet(() -> Assertions.fail("No fitting EReference could be found in '%s'.".formatted(eClassWithStakeholderParameter.getName())));

        final String stakeholderCreationToolName = "New Stakeholder";
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, eClassWithStakeholderParameter, parentNodeLabel, stakeholderCreationToolName,
                Stream.of(new ToolVariable("selectedObject", /* PartUsage 'part' */ "2c5fe5a5-18fe-40f4-ab66-a2d91ab7df6a", ToolVariableType.OBJECT_ID)).toList());

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .check(initialDiagram, newDiagram);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentNodeLabel)
                    .withCompartmentName("stakeholders")
                    .hasNodeDescriptionName(this.descriptionNameGenerator.getCompartmentItemName(eClassWithStakeholderParameter, stakeholderParameterEReference))
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        });

        final ISemanticChecker semanticChecker = (editingContext) -> {
            final Element semanticRootElement = this.objectSearchService.getObject(editingContext, GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID).filter(Element.class::isInstance)
                    .map(Element.class::cast).orElseGet(() -> Assertions.fail("Could not find the expected root semantic object."));
            final List<PartUsage> allStakeholderPartUsages = EMFUtils.allContainedObjectOfType(semanticRootElement, PartUsage.class)
                    .filter(element -> Objects.equals(element.getName(), "stakeholder1")).toList();
            assertEquals(1, allStakeholderPartUsages.size());

            final PartUsage stakeholderPartUsage = allStakeholderPartUsages.get(0);
            final EList<Subsetting> subsettings = stakeholderPartUsage.getOwnedSubsetting();
            assertEquals(1, subsettings.size());
            assertThat(subsettings.get(0).getSubsettedFeature().getName()).isEqualTo("part");
        };

        Runnable semanticCheck1 = this.semanticCheckerService.checkEditingContext(
                this.semanticCheckerService.getElementInParentSemanticChecker(parentNodeLabel, stakeholderParameterEReference, SysmlPackage.eINSTANCE.getPartUsage()));
        Runnable semanticCheck2 = this.semanticCheckerService.checkEditingContext(semanticChecker);

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck1)
                .then(semanticCheck2)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
