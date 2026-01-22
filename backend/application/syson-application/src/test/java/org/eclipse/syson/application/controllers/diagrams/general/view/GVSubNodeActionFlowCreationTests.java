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
import static org.eclipse.sirius.components.diagrams.tests.assertions.DiagramAssertions.assertThat;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
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
import org.eclipse.syson.diagram.common.view.nodes.DecisionActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.ForkActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.JoinActionNodeDescriptionProvider;
import org.eclipse.syson.diagram.common.view.nodes.MergeActionNodeDescriptionProvider;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.NodeCreationTestsService;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.helper.LabelConstants;
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
 * Tests the creation of "ActionFlow" sub nodes section in the General View diagram.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVSubNodeActionFlowCreationTests extends AbstractIntegrationTests {

    private static final String PARAMETERS = "parameters";

    private static final String ACTION = "action";

    private static final String ACTIONS_COMPARTMENT = "actions";

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

    private static Stream<Arguments> acceptActionUsagePayloadParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getPartDefinition()),
                Arguments.of(SysmlPackage.eINSTANCE.getItemDefinition()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> actionUsageSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), SysmlPackage.eINSTANCE.getUsage_NestedPart(), 11))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> actionUsageListNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getAttributeUsage(), "attributes", SysmlPackage.eINSTANCE.getUsage_NestedAttribute()))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> actionUsageBorderAndChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getReferenceUsage(), PARAMETERS, SysmlPackage.eINSTANCE.getUsage_NestedReference(), SysmlPackage.eINSTANCE.getReferenceUsage(),
                        "New Parameter In"),
                Arguments.of(SysmlPackage.eINSTANCE.getReferenceUsage(), PARAMETERS, SysmlPackage.eINSTANCE.getUsage_NestedReference(), SysmlPackage.eINSTANCE.getReferenceUsage(),
                        "New Parameter Inout"),
                Arguments.of(SysmlPackage.eINSTANCE.getReferenceUsage(), PARAMETERS, SysmlPackage.eINSTANCE.getUsage_NestedReference(), SysmlPackage.eINSTANCE.getReferenceUsage(),
                        "New Parameter Out"))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> actionDefinitionBorderAndChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getReferenceUsage(), PARAMETERS, SysmlPackage.eINSTANCE.getDefinition_OwnedReference(), SysmlPackage.eINSTANCE.getReferenceUsage(),
                        "New Parameter In"),
                Arguments.of(SysmlPackage.eINSTANCE.getReferenceUsage(), PARAMETERS, SysmlPackage.eINSTANCE.getDefinition_OwnedReference(), SysmlPackage.eINSTANCE.getReferenceUsage(),
                        "New Parameter Inout"),
                Arguments.of(SysmlPackage.eINSTANCE.getReferenceUsage(), PARAMETERS, SysmlPackage.eINSTANCE.getDefinition_OwnedReference(), SysmlPackage.eINSTANCE.getReferenceUsage(),
                        "New Parameter Out"))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> actionUsageFreeFormNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getDecisionNode(), SysmlPackage.eINSTANCE.getUsage_NestedAction(), 0, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getForkNode(), SysmlPackage.eINSTANCE.getUsage_NestedAction(), 0, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getJoinNode(), SysmlPackage.eINSTANCE.getUsage_NestedAction(), 0, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getMergeNode(), SysmlPackage.eINSTANCE.getUsage_NestedAction(), 0, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getAcceptActionUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAction(), 5, 2, 1))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> actionUsageSiblingAndChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getActionUsage(), ACTIONS_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedAction(), 17, 1),
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), "items", SysmlPackage.eINSTANCE.getUsage_NestedItem(), 6, 1))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> actionDefinitionSiblingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedItem(), 4))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> actionDefinitionFreeFormNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getDecisionNode(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction(), 0, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getForkNode(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction(), 0, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getJoinNode(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction(), 0, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getMergeNode(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction(), 0, 0, 0),
                Arguments.of(SysmlPackage.eINSTANCE.getAcceptActionUsage(), SysmlPackage.eINSTANCE.getDefinition_OwnedAction(), 5, 2, 1))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> actionDefinitionSiblingAndChildNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getActionUsage(), ACTIONS_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedAction(), 17, 1))
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
    @MethodSource("acceptActionUsagePayloadParameters")
    public void createAcceptActionUsagePayload(EClass eClass) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getAcceptActionUsage();
        String parentLabel = "acceptAction";

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel,
                this.descriptionNameGenerator.getCreationToolName("New {0} as Payload", eClass));

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(0)
                    .hasNewEdgeCount(0)
                    .check(diagram.get(), newDiagram);

            DiagramNavigator diagramNavigator = new DiagramNavigator(newDiagram);
            Node updatedNode = diagramNavigator.nodeWithTargetObjectLabel(parentLabel).getNode();
            assertThat(updatedNode).as("The updated node should exist").isNotNull();
            assertThat(updatedNode.getInsideLabel()).as("The updated node label should exist").isNotNull();
            assertThat(updatedNode.getInsideLabel().getText())
                    .contains(LabelConstants.OPEN_QUOTE + "accept" + LabelConstants.CLOSE_QUOTE)
                    .contains("payload: acceptActionPayloadType");
        });

        ISemanticChecker semanticChecker = (editingContext) -> {
            Object semanticRootObject = this.objectSearchService.getObject(editingContext, GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID).orElse(null);
            assertThat(semanticRootObject).isInstanceOf(Element.class);
            Element semanticRootElement = (Element) semanticRootObject;
            Optional<Element> optParentElement = EMFUtils.allContainedObjectOfType(semanticRootElement, Element.class)
                    .filter(element -> Objects.equals(element.getName(), parentLabel))
                    .findFirst();
            assertThat(optParentElement).isPresent();
            assertThat(optParentElement.get()).isInstanceOf(AcceptActionUsage.class);
            AcceptActionUsage acceptActionUsage = (AcceptActionUsage) optParentElement.get();
            var referenceUsage = acceptActionUsage.getPayloadParameter();
            var relationship = referenceUsage.getOwnedRelationship().get(0);
            assertThat(relationship).isInstanceOf(FeatureTyping.class);
            FeatureTyping featureTyping = (FeatureTyping) relationship;
            assertThat(eClass.isInstance(featureTyping.getType()));
            assertThat(featureTyping.getType().getName()).isEqualTo("acceptActionPayloadType");
        };

        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(semanticChecker);

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
    public void createAcceptActionUsageReceiver() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getAcceptActionUsage();
        String parentLabel = "acceptAction";
        EClass eClass = SysmlPackage.eINSTANCE.getPortUsage();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel,
                this.descriptionNameGenerator.getCreationToolName("New {0} as Receiver", eClass));

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(0)
                    .hasNewEdgeCount(0)
                    .check(diagram.get(), newDiagram);

            DiagramNavigator diagramNavigator = new DiagramNavigator(newDiagram);
            Node updatedNode = diagramNavigator.nodeWithTargetObjectLabel(parentLabel).getNode();
            assertThat(updatedNode).as("The updated node should exist").isNotNull();
            assertThat(updatedNode.getInsideLabel()).as("The updated node label should exist").isNotNull();
            assertThat(updatedNode.getInsideLabel().getText())
                    .contains(LabelConstants.OPEN_QUOTE + "via" + LabelConstants.CLOSE_QUOTE)
                    .contains("acceptAction's receiver");
        });

        ISemanticChecker semanticChecker = (editingContext) -> {
            Object semanticRootObject = this.objectSearchService.getObject(editingContext, GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID).orElse(null);
            assertThat(semanticRootObject).isInstanceOf(Element.class);
            Element semanticRootElement = (Element) semanticRootObject;
            Optional<Element> optParentElement = EMFUtils.allContainedObjectOfType(semanticRootElement, Element.class)
                    .filter(element -> Objects.equals(element.getName(), parentLabel))
                    .findFirst();
            assertThat(optParentElement).isPresent();
            assertThat(optParentElement.get()).isInstanceOf(AcceptActionUsage.class);
            AcceptActionUsage acceptActionUsage = (AcceptActionUsage) optParentElement.get();
            var receiverExpression = acceptActionUsage.getReceiverArgument();
            var relationship = receiverExpression.getOwnedRelationship().get(0);
            assertThat(relationship).isInstanceOf(Membership.class);
            Membership membership = (Membership) relationship;
            assertThat(eClass.isInstance(membership.getMemberElement())).isTrue();
            assertThat(membership.getMemberElement().getName()).isEqualTo("acceptAction's receiver");
        };

        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(semanticChecker);

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
    @MethodSource("actionUsageSiblingNodeParameters")
    public void createActionUsageSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getActionUsage();
        String parentLabel = ACTION;

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);

        Consumer<Object> diagramCheck = this.diagramCheckerService.siblingNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, childEClass, compartmentCount);

        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(100));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("actionUsageListNodeParameters")
    public void createActionUsageListChildNodes(EClass childEClass, String compartmentName, EReference containmentReference) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getActionUsage();
        String parentLabel = ACTION;
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
    @MethodSource("actionUsageBorderAndChildNodeParameters")
    public void createActionUsageBorderAndChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, EClass borderNodeType, String toolName) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getActionUsage();
        String parentLabel = ACTION;
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, toolName);
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(2)
                    .hasNewBorderNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram);
            String compartmentNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(compartmentNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
            String borderNodeDescription = this.descriptionNameGenerator.getBorderNodeName(borderNodeType);
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
    @MethodSource("actionDefinitionBorderAndChildNodeParameters")
    public void createActionDefinitionBorderAndChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, EClass borderNodeType, String toolName) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getActionDefinition();
        String parentLabel = "ActionDefinition";
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, toolName);
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(2)
                    .hasNewBorderNodeCount(1)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram);
            String compartmentNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(compartmentNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
            String borderNodeDescription = this.descriptionNameGenerator.getBorderNodeName(borderNodeType);
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
    @MethodSource("actionUsageFreeFormNodeParameters")
    public void createActionUsageFreeFormChildNodes(EClass childEClass, EReference containmentReference, int compartmentCount, int compartmentCountInNewChild, int expectedEdgeCount) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getActionUsage();
        String parentLabel = ACTION;

        String creationToolName = this.descriptionNameGenerator.getCreationToolName(childEClass);
        if (creationToolName.endsWith(" Node")) {
            creationToolName = creationToolName.substring(0, creationToolName.length() - 5);
        }

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, creationToolName);
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            int createdNodesExpectedCount = 1 + compartmentCount;
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(createdNodesExpectedCount)
                    .hasNewEdgeCount(expectedEdgeCount)
                    .check(initialDiagram, newDiagram);
            String nodeName = this.getActionFlowNodeName(childEClass);
            String freeFormNodeDescription = this.descriptionNameGenerator.getNodeName(nodeName);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName("action flow")
                    .hasNodeDescriptionName(freeFormNodeDescription)
                    .hasCompartmentCount(compartmentCountInNewChild)
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
    @MethodSource("actionUsageSiblingAndChildNodeParameters")
    public void createActionUsageSiblingAndChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, int expectedNumberOfNewNodes,
            int expectedNumberOfNewEdges) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getActionUsage();
        String parentLabel = ACTION;

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
    @Test
    public void createReferencingPerformActionUsageInActionUsage() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getActionUsage();
        EClass childEClass = SysmlPackage.eINSTANCE.getPerformActionUsage();
        String parentLabel = ACTION;
        String creationToolName = "New Perform";
        EReference containmentReference = SysmlPackage.eINSTANCE.getUsage_NestedAction();
        List<ToolVariable> variables = new ArrayList<>();
        variables.add(new ToolVariable("selectedObject", GeneralViewWithTopNodesTestProjectData.SemanticIds.ACTION_USAGE_ID, ToolVariableType.OBJECT_ID));

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, creationToolName, variables);

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(3) // 1 visible new PerformActionUsage node + 1 list items (in 'perform actions'
                                        // compartments, now visible) + 1 node in 'action flow' compartment
                    .hasNewEdgeCount(3) // 1 visible composition edge and 2 visible ReferenceSubsetting edge (one for
                                        // the top-level node, one for the node in "action flow" compartment
                    .check(initialDiagram, newDiagram, true);
            String freeFormNodeDescription = this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPerformActionUsage());
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName("action flow")
                    .hasNodeDescriptionName(freeFormNodeDescription)
                    .hasCompartmentCount(5)
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
    public void createPerformActionUsageInActionUsage() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getActionUsage();
        EClass childEClass = SysmlPackage.eINSTANCE.getPerformActionUsage();
        String parentLabel = ACTION;
        String creationToolName = "New Perform action";
        EReference containmentReference = SysmlPackage.eINSTANCE.getUsage_NestedAction();

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, creationToolName);
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            int createdNodesExpectedCount = 13;
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(createdNodesExpectedCount)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram);
            String freeFormNodeDescription = this.descriptionNameGenerator.getNodeName(childEClass);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName("action flow")
                    .hasNodeDescriptionName(freeFormNodeDescription)
                    .hasCompartmentCount(5)
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
    @MethodSource("actionDefinitionSiblingNodeParameters")
    public void createActionDefinitionSiblingNodes(EClass childEClass, EReference containmentReference, int compartmentCount) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getActionDefinition();
        String parentLabel = "ActionDefinition";
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, childEClass);
        Consumer<Object> diagramCheck = this.diagramCheckerService.siblingNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, childEClass, compartmentCount, 2);
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
    @MethodSource("actionDefinitionFreeFormNodeParameters")
    public void createActionDefinitionFreeFormChildNodes(EClass childEClass, EReference containmentReference, int compartmentCount, int compartmentCountInNewChild, int expectedEdgeCount) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getActionDefinition();
        String parentLabel = "ActionDefinition";

        String creationToolName = this.descriptionNameGenerator.getCreationToolName(childEClass);
        if (creationToolName.endsWith(" Node")) {
            creationToolName = creationToolName.substring(0, creationToolName.length() - 5);
        }

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, creationToolName);

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            int createdNodesExpectedCount = 1 + compartmentCount;
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(createdNodesExpectedCount)
                    .hasNewEdgeCount(expectedEdgeCount)
                    .check(initialDiagram, newDiagram);
            String nodeName = this.getActionFlowNodeName(childEClass);
            String freeFormNodeDescription = this.descriptionNameGenerator.getNodeName(nodeName);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName("action flow")
                    .hasNodeDescriptionName(freeFormNodeDescription)
                    .hasCompartmentCount(compartmentCountInNewChild)
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
    @MethodSource("actionDefinitionSiblingAndChildNodeParameters")
    public void createActionDefinitionSiblingAndChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, int expectedNumberOfNewNodes,
            int expectedNumberOfNewEdges) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getActionUsage();
        String parentLabel = ACTION;

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
