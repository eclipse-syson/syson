/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramAssertions.assertThat;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariable;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariableType;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.view.diagram.DiagramDescription;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controller.editingContext.checkers.ISemanticChecker;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckNodeInCompartment;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckNodeOnDiagram;
import org.eclipse.syson.application.controllers.diagrams.checkers.DiagramCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.IDiagramChecker;
import org.eclipse.syson.application.controllers.diagrams.testers.NodeCreationTester;
import org.eclipse.syson.application.controllers.utils.TestNameGenerator;
import org.eclipse.syson.application.data.SysMLv2Identifiers;
import org.eclipse.syson.diagram.common.view.nodes.DecisionActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.ForkActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.JoinActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.MergeActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.general.view.GVDescriptionNameGenerator;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.NodeCreationTestsService;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramReference;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
 * Tests the creation of "ActionFlow" sub nodes section in the General View diagram.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVSubNodeActionFlowCreationTests extends AbstractIntegrationTests {

    private static final String ACTIONS_COMPARTMENT = "actions";

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

    private static Stream<Arguments> acceptActionUsagePayloadParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getPartDefinition()),
                Arguments.of(SysmlPackage.eINSTANCE.getItemDefinition()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> actionUsageSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPart(), 7),
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAttribute(), 3),
                Arguments.of(SysmlPackage.eINSTANCE.getPortUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPort(), 3))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> actionUsageListNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), "items", SysmlPackage.eINSTANCE.getUsage_NestedItem()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> actionUsageListAndFreeFormNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAcceptActionUsage(), ACTIONS_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedAction(), 1),
                Arguments.of(SysmlPackage.eINSTANCE.getActionUsage(), ACTIONS_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedAction(), 4),
                Arguments.of(SysmlPackage.eINSTANCE.getDecisionNode(), ACTIONS_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedAction(), 0),
                Arguments.of(SysmlPackage.eINSTANCE.getForkNode(), ACTIONS_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedAction(), 0),
                Arguments.of(SysmlPackage.eINSTANCE.getJoinNode(), ACTIONS_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedAction(), 0),
                Arguments.of(SysmlPackage.eINSTANCE.getMergeNode(), ACTIONS_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedAction(), 0))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> actionDefinitionSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedItem(), 3))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> actionDefinitionListAndFreeFormNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAcceptActionUsage(), ACTIONS_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedAction(), 1),
                Arguments.of(SysmlPackage.eINSTANCE.getActionUsage(), ACTIONS_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedAction(), 4),
                Arguments.of(SysmlPackage.eINSTANCE.getDecisionNode(), ACTIONS_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedAction(), 0),
                Arguments.of(SysmlPackage.eINSTANCE.getForkNode(), ACTIONS_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedAction(), 0),
                Arguments.of(SysmlPackage.eINSTANCE.getJoinNode(), ACTIONS_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedAction(), 0),
                Arguments.of(SysmlPackage.eINSTANCE.getMergeNode(), ACTIONS_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedAction(), 0))
                .map(TestNameGenerator::namedArguments);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                SysMLv2Identifiers.GENERAL_VIEW_WITH_TOP_NODES_PROJECT,
                SysMLv2Identifiers.GENERAL_VIEW_WITH_TOP_NODES_DIAGRAM);
        var flux = this.givenDiagramSubscription.subscribe(diagramEventInput);
        this.verifier = StepVerifier.create(flux);
        this.diagram = this.givenDiagram.getDiagram(this.verifier);
        this.diagramDescription = this.givenDiagramDescription.getDiagramDescription(SysMLv2Identifiers.GENERAL_VIEW_WITH_TOP_NODES_PROJECT,
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
    @MethodSource("acceptActionUsagePayloadParameters")
    public void createAcceptActionUsagePayload(EClass eClass) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getAcceptActionUsage();
        String parentLabel = "acceptAction";

        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel,
                this.descriptionNameGenerator.getCreationToolName("New {0} as Payload", eClass));

        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(0)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram);

            DiagramNavigator diagramNavigator = new DiagramNavigator(newDiagram);
            Node updatedNode = diagramNavigator.nodeWithTargetObjectLabel(parentLabel).getNode();
            assertThat(updatedNode).as("The updated node should exist").isNotNull();
            assertThat(updatedNode.getInsideLabel()).as("The updated node label should exist").isNotNull();
            assertThat(updatedNode.getInsideLabel().getText())
                    .contains(LabelConstants.OPEN_QUOTE + "accept" + LabelConstants.CLOSE_QUOTE)
                    .contains("payload: acceptActionPayloadType");
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);

        ISemanticChecker semanticChecker = (editingContext) -> {
            Object semanticRootObject = this.objectService.getObject(editingContext, SysMLv2Identifiers.GENERAL_VIEW_WITH_TOP_NODES_DIAGRAM_OBJECT).orElse(null);
            assertThat(semanticRootObject).isInstanceOf(Element.class);
            Element semanticRootElement = (Element) semanticRootObject;
            Optional<Element> optParentElement = EMFUtils.allContainedObjectOfType(semanticRootElement, Element.class)
                    .filter(element -> Objects.equals(element.getName(), parentLabel))
                    .findFirst();
            assertThat(optParentElement).isPresent();
            AcceptActionUsage acceptActionUsage = (AcceptActionUsage) optParentElement.get();
            var referenceUsage = acceptActionUsage.getPayloadParameter();
            var relationship = referenceUsage.getOwnedRelationship().get(0);
            assertThat(relationship).isInstanceOf(FeatureTyping.class);
            FeatureTyping featureTyping = (FeatureTyping) relationship;
            assertThat(eClass.isInstance(featureTyping.getType()));
            assertThat(featureTyping.getType().getName()).isEqualTo("acceptActionPayloadType");
        };

        this.semanticCheckerService.checkEditingContext(semanticChecker, this.verifier);
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void createAcceptActionUsageReceiver() {
        EClass parentEClass = SysmlPackage.eINSTANCE.getAcceptActionUsage();
        String parentLabel = "acceptAction";
        EClass eClass = SysmlPackage.eINSTANCE.getPortUsage();
        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel,
                this.descriptionNameGenerator.getCreationToolName("New {0} as Receiver", eClass));

        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(0)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram);

            DiagramNavigator diagramNavigator = new DiagramNavigator(newDiagram);
            Node updatedNode = diagramNavigator.nodeWithTargetObjectLabel(parentLabel).getNode();
            assertThat(updatedNode).as("The updated node should exist").isNotNull();
            assertThat(updatedNode.getInsideLabel()).as("The updated node label should exist").isNotNull();
            assertThat(updatedNode.getInsideLabel().getText())
                    .contains(LabelConstants.OPEN_QUOTE + "via" + LabelConstants.CLOSE_QUOTE)
                    .contains("acceptAction's receiver");
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);

        ISemanticChecker semanticChecker = (editingContext) -> {
            Object semanticRootObject = this.objectService.getObject(editingContext, SysMLv2Identifiers.GENERAL_VIEW_WITH_TOP_NODES_DIAGRAM_OBJECT).orElse(null);
            assertThat(semanticRootObject).isInstanceOf(Element.class);
            Element semanticRootElement = (Element) semanticRootObject;
            Optional<Element> optParentElement = EMFUtils.allContainedObjectOfType(semanticRootElement, Element.class)
                    .filter(element -> Objects.equals(element.getName(), parentLabel))
                    .findFirst();
            assertThat(optParentElement).isPresent();
            AcceptActionUsage acceptActionUsage = (AcceptActionUsage) optParentElement.get();
            var receiverExpression = acceptActionUsage.getReceiverArgument();
            var relationship = receiverExpression.getOwnedRelationship().get(0);
            assertThat(relationship).isInstanceOf(Membership.class);
            Membership membership = (Membership) relationship;
            assertThat(eClass.isInstance(membership.getMemberElement())).isTrue();
            assertThat(membership.getMemberElement().getName()).isEqualTo("acceptAction's receiver");
        };

        this.semanticCheckerService.checkEditingContext(semanticChecker, this.verifier);
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("actionUsageSiblingNodeParameters")
    public void createActionUsageSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getActionUsage();
        String parentLabel = "action";
        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel, childEClass);
        final IDiagramChecker diagramChecker;
        if (SysmlPackage.eINSTANCE.getPartUsage().equals(childEClass)) {
            diagramChecker = (initialDiagram, newDiagram) -> {
                int createdNodesExpectedCount = 2 + compartmentCount;
                new CheckDiagramElementCount(this.diagramComparator)
                        .hasNewNodeCount(createdNodesExpectedCount)
                        .hasNewEdgeCount(1)
                        .check(initialDiagram, newDiagram);

                String newNodeDescriptionName = this.descriptionNameGenerator.getNodeName(childEClass);
                new CheckNodeOnDiagram(this.diagramDescriptionIdProvider, this.diagramComparator)
                        .hasNodeDescriptionName(newNodeDescriptionName)
                        .hasCompartmentCount(compartmentCount)
                        .check(initialDiagram, newDiagram);

                String compartmentNodeDescriptionName = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, SysmlPackage.eINSTANCE.getUsage_NestedItem());
                new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                        .withParentLabel(parentLabel)
                        .withCompartmentName("items")
                        .hasNodeDescriptionName(compartmentNodeDescriptionName)
                        .check(initialDiagram, newDiagram);
            };
        } else {
            diagramChecker = this.diagramCheckerService.getSiblingNodeGraphicalChecker(this.diagram, this.diagramDescriptionIdProvider, childEClass, compartmentCount);
        }
        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);
        this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass), this.verifier);
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("actionUsageListNodeParameters")
    public void createActionUsageListChildNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getActionUsage();
        String parentLabel = "action";
        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel, childEClass);
        this.diagramCheckerService.checkDiagram(
                this.diagramCheckerService.getCompartmentNodeGraphicalChecker(this.diagram, this.diagramDescriptionIdProvider, parentLabel, parentEClass, containmentReference, compartmentName),
                this.diagram, this.verifier);
        this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass), this.verifier);
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("actionUsageListAndFreeFormNodeParameters")
    public void createActionUsageListAndFreeFormChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getActionUsage();
        String parentLabel = "action";

        String creationToolName = this.descriptionNameGenerator.getCreationToolName(childEClass);
        if (creationToolName.endsWith(" Node")) {
            creationToolName = creationToolName.substring(0, creationToolName.length() - 5);
        }

        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel, creationToolName);
        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            int createdNodesExpectedCount = 2 + compartmentCount;
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(createdNodesExpectedCount)
                    .check(initialDiagram, newDiagram);
            String listNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(listNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
            String nodeName = this.getActionFlowNodeName(childEClass);
            String freeFormNodeDescription = this.descriptionNameGenerator.getNodeName(nodeName);
            new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName("action flow")
                    .hasNodeDescriptionName(freeFormNodeDescription)
                    .hasCompartmentCount(compartmentCount)
                    .check(initialDiagram, newDiagram);
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);
        this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass), this.verifier);
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void createReferencingPerformActionUsageInActionUsage() {
        EClass parentEClass = SysmlPackage.eINSTANCE.getActionUsage();
        EClass childEClass = SysmlPackage.eINSTANCE.getPerformActionUsage();
        String parentLabel = "action";
        String creationToolName = "New Perfom";
        EReference containmentReference = SysmlPackage.eINSTANCE.getUsage_NestedAction();
        List<ToolVariable> variables = new ArrayList<>();
        variables.add(new ToolVariable("selectedObject", "e00ffabe-b4e8-40ab-b7b8-5a21dcc4c141", ToolVariableType.OBJECT_ID));

        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel, creationToolName, variables);

        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            int createdNodesExpectedCount = 6;
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(createdNodesExpectedCount)
                    .check(initialDiagram, newDiagram);
            String listNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(ACTIONS_COMPARTMENT)
                    .hasNodeDescriptionName(listNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
            String freeFormNodeDescription = this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPerformActionUsage());
            new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName("action flow")
                    .hasNodeDescriptionName(freeFormNodeDescription)
                    .hasCompartmentCount(4)
                    .check(initialDiagram, newDiagram);
        };
        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);
        this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass), this.verifier);
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void createPerformActionUsageInActionUsage() {
        EClass parentEClass = SysmlPackage.eINSTANCE.getActionUsage();
        EClass childEClass = SysmlPackage.eINSTANCE.getPerformActionUsage();
        String parentLabel = "action";
        String creationToolName = "New Perfom action";
        EReference containmentReference = SysmlPackage.eINSTANCE.getUsage_NestedAction();

        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel, creationToolName);
        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            int createdNodesExpectedCount = 6;
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(createdNodesExpectedCount)
                    .check(initialDiagram, newDiagram);
            String listNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(ACTIONS_COMPARTMENT)
                    .hasNodeDescriptionName(listNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
            String freeFormNodeDescription = this.descriptionNameGenerator.getNodeName(childEClass);
            new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName("action flow")
                    .hasNodeDescriptionName(freeFormNodeDescription)
                    .hasCompartmentCount(4)
                    .check(initialDiagram, newDiagram);
        };
        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);
        this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass), this.verifier);
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("actionDefinitionSiblingNodeParameters")
    public void createActionDefinitionSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getActionDefinition();
        String parentLabel = "ActionDefinition";
        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel, childEClass);
        this.diagramCheckerService.checkDiagram(this.diagramCheckerService.getSiblingNodeGraphicalChecker(this.diagram, this.diagramDescriptionIdProvider, childEClass, compartmentCount), this.diagram,
                this.verifier);
        this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass), this.verifier);
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @ParameterizedTest
    @MethodSource("actionDefinitionListAndFreeFormNodeParameters")
    public void createActionDefinitionListAndFreeFormChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, int compartmentCount) {
        EClass parentEClass = SysmlPackage.eINSTANCE.getActionDefinition();
        String parentLabel = "ActionDefinition";

        String creationToolName = this.descriptionNameGenerator.getCreationToolName(childEClass);
        if (creationToolName.endsWith(" Node")) {
            creationToolName = creationToolName.substring(0, creationToolName.length() - 5);
        }

        this.creationTestsService.createNode(this.verifier, this.diagramDescriptionIdProvider, this.diagram, parentEClass, parentLabel, creationToolName);

        IDiagramChecker diagramChecker = (initialDiagram, newDiagram) -> {
            int createdNodesExpectedCount = 2 + compartmentCount;
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(createdNodesExpectedCount)
                    .check(initialDiagram, newDiagram);
            String listNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(listNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
            String nodeName = this.getActionFlowNodeName(childEClass);
            String freeFormNodeDescription = this.descriptionNameGenerator.getNodeName(nodeName);
            new CheckNodeInCompartment(this.diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName("action flow")
                    .hasNodeDescriptionName(freeFormNodeDescription)
                    .hasCompartmentCount(compartmentCount)
                    .check(initialDiagram, newDiagram);
        };

        this.diagramCheckerService.checkDiagram(diagramChecker, this.diagram, this.verifier);
        this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass), this.verifier);
    }

    private String getActionFlowNodeName(EClass eClass) {
        assertThat(eClass).isNotNull();
        String result = eClass.getName();
        if (SysmlPackage.eINSTANCE.getDecisionNode().equals(eClass)) {
            result = DecisionActionNodeDescriptionProvider.DECISION_ACTION_NAME;
        } else if (SysmlPackage.eINSTANCE.getForkNode().equals(eClass)) {
            result = ForkActionNodeDescriptionProvider.FORK_ACTION_NAME;
        } else if (SysmlPackage.eINSTANCE.getJoinNode().equals(eClass)) {
            result = JoinActionNodeDescriptionProvider.JOIN_ACTION_NAME;
        } else if (SysmlPackage.eINSTANCE.getMergeNode().equals(eClass)) {
            result = MergeActionNodeDescriptionProvider.MERGE_ACTION_NAME;
        }
        return result;
    }
}
