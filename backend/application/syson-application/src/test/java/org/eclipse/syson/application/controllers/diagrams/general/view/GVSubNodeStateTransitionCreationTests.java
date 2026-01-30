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

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
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
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckNodeInCompartment;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.controllers.utils.TestNameGenerator;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.diagram.common.view.nodes.StatesCompartmentNodeDescriptionProvider;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.NodeCreationTestsService;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.PerformActionUsage;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.StateSubactionKind;
import org.eclipse.syson.sysml.StateSubactionMembership;
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

import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Tests the creation of the "StateTransition" sub nodes section in the General View diagram.
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

    @Autowired
    private IIdentityService identityService;

    private NodeCreationTestsService creationTestsService;

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

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
                        Arguments.of(SysmlPackage.eINSTANCE.getDocumentation(), DOC_COMPARTMENT, SysmlPackage.eINSTANCE.getElement_Documentation(), "New Documentation", 1, 0),
                        /*
                         * 8 new nodes = 1 compartment item node for new exhibit state + 1 rectangular child node and its 6
                         * compartments
                         */
                        Arguments.of(SysmlPackage.eINSTANCE.getExhibitStateUsage(), EXHIBIT_STATES_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedState(), "New Exhibit State", 9, 1),
                        /*
                         * 8 new nodes = 1 compartment item node for new exhibit state + 1 rectangular child node and its 6
                         * compartments
                         */
                        Arguments.of(SysmlPackage.eINSTANCE.getExhibitStateUsage(), EXHIBIT_STATES_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedState(), "New Exhibit Parallel State", 9, 1),
                        /*
                         * 15 new nodes = 1 compartment item node for new state + 1 rectangular node in the
                         * "state transition compartment" and its 6 compartments + 1 rectangular child node and its 6
                         * compartments
                         */
                        Arguments.of(SysmlPackage.eINSTANCE.getStateUsage(), STATES_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedState(), "New State", 16, 1),
                        /*
                         * 15 new nodes = 1 compartment item node for new state + 1 rectangular node in the
                         * "state transition compartment" and its 6 compartments + 1 rectangular child node and its 6
                         * compartments
                         */
                        Arguments.of(SysmlPackage.eINSTANCE.getStateUsage(), STATES_COMPARTMENT, SysmlPackage.eINSTANCE.getUsage_NestedState(), "New Parallel State", 16, 1))
                .map(TestNameGenerator::namedArguments);
    }

    private static Stream<Arguments> stateDefinitionChildNodeParameters() {
        return Stream.of(
                        Arguments.of(SysmlPackage.eINSTANCE.getDocumentation(), DOC_COMPARTMENT, SysmlPackage.eINSTANCE.getElement_Documentation(), "New Documentation", 1, 0),
                        /*
                         * 8 new nodes = 1 compartment item node for new exhibit state + 1 rectangular child node and its 6
                         * compartments
                         */
                        Arguments.of(SysmlPackage.eINSTANCE.getStateUsage(), EXHIBIT_STATES_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedState(), "New Exhibit State", 9, 1),
                        /*
                         * 8 new nodes = 1 compartment item node for new exhibit state + 1 rectangular child node and its 6
                         * compartments
                         */
                        Arguments.of(SysmlPackage.eINSTANCE.getStateUsage(), EXHIBIT_STATES_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedState(), "New Exhibit Parallel State", 9, 1),
                        /*
                         * 15 new nodes = 1 compartment item node for new state + 1 rectangular node in the
                         * "state transition compartment" and its 6 compartments + 1 rectangular child node and its 6
                         * compartments
                         */
                        Arguments.of(SysmlPackage.eINSTANCE.getStateUsage(), STATES_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedState(), "New State", 16, 1),
                        /*
                         * 15 new nodes = 1 compartment item node for new state + 1 rectangular node in the
                         * "state transition compartment" and its 6 compartments + 1 rectangular child node and its 6
                         * compartments
                         */
                        Arguments.of(SysmlPackage.eINSTANCE.getStateUsage(), STATES_COMPARTMENT, SysmlPackage.eINSTANCE.getDefinition_OwnedState(), "New Parallel State", 16, 1))
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
        this.semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID);
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("stateSubactionsParameters")
    public void createStateUsageSubactionNode(StateSubactionKind kind) {
        String toolName = "New " + StringUtils.capitalize(kind.getName()) + " Action";

        this.createStateSubactionNode(kind, SysmlPackage.eINSTANCE.getStateUsage(), "state", toolName);
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("stateSubactionsParameters")
    public void createStateUsageSubactionWithReferencedActionNode(StateSubactionKind kind) {
        String toolName = "New " + StringUtils.capitalize(kind.getName()) + " Action with referenced Action";
        var params = List.of(new ToolVariable("selectedObject", GeneralViewWithTopNodesTestProjectData.SemanticIds.ACTION_USAGE_ID, ToolVariableType.OBJECT_ID));

        this.createStateSubactionWithReferencedActionNode(kind, SysmlPackage.eINSTANCE.getStateUsage(), "state", toolName, params);
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("stateSubactionsParameters")
    public void createStateDefinitionSubactionNode(StateSubactionKind kind) {
        String toolName = "New " + StringUtils.capitalize(kind.getName()) + " Action";

        this.createStateSubactionNode(kind, SysmlPackage.eINSTANCE.getStateDefinition(), "StateDefinition", toolName);
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest
    @MethodSource("stateSubactionsParameters")
    public void createStateDefinitionSubactionWithReferencedActionNode(StateSubactionKind kind) {
        String toolName = "New " + StringUtils.capitalize(kind.getName()) + " Action with referenced Action";
        var params = List.of(new ToolVariable("selectedObject", GeneralViewWithTopNodesTestProjectData.SemanticIds.ACTION_USAGE_ID, ToolVariableType.OBJECT_ID));

        this.createStateSubactionWithReferencedActionNode(kind, SysmlPackage.eINSTANCE.getStateDefinition(), "StateDefinition", toolName, params);
    }

    private void createStateSubactionNode(StateSubactionKind kind, EClass parentEClass, String parentLabel, String toolName) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, toolName);

        String[] subActionId = new String[1];
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    // the new subaction is created in two compartments (actions and perform actions)
                    .hasNewNodeCount(2)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram);
            var node = this.diagramComparator.newNodes(initialDiagram, newDiagram).get(0);
            subActionId[0] = node.getTargetObjectId();
        });

        Runnable semanticCheck = this.semanticCheckerService.checkElement(PerformActionUsage.class, () -> subActionId[0], subaction -> {
            // new subaction should be owned by a StateSubactionMembership with the correct kind
            var parentMembership = subaction.eContainer();
            assertThat(parentMembership).isInstanceOf(StateSubactionMembership.class);
            var stateSubactionMembership = (StateSubactionMembership) parentMembership;
            assertThat(stateSubactionMembership.getKind()).isEqualTo(kind);
            // subaction has no owned membership
            assertThat(subaction.getOwnedRelationship()).hasSize(0);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    private void createStateSubactionWithReferencedActionNode(StateSubactionKind kind, EClass parentEClass, String parentLabel, String toolName, List<@NotNull ToolVariable> params) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, toolName, params);

        String[] subActionId = new String[1];
        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    // the new subaction is created in two compartments (actions and perform actions)
                    .hasNewNodeCount(2)
                    .hasNewEdgeCount(0)
                    .check(initialDiagram, newDiagram);
            var node = this.diagramComparator.newNodes(initialDiagram, newDiagram).get(0);
            subActionId[0] = node.getTargetObjectId();
        });

        Runnable semanticCheck = this.semanticCheckerService.checkElement(PerformActionUsage.class, () -> subActionId[0], subaction -> {
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
    @MethodSource("stateUsageChildNodeParameters")
    public void createStateUsageChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, String creationToolName, int expectedNumberOfNewNodes,
            int expectedNumberOfNewEdges) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getStateUsage();
        String parentLabel = "state";
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, creationToolName);

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(expectedNumberOfNewNodes)
                    .hasNewEdgeCount(expectedNumberOfNewEdges)
                    .check(initialDiagram, newDiagram);
            String listStatesNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            if (STATES_COMPARTMENT.equals(compartmentName)) {
                listStatesNodeDescription += StatesCompartmentNodeDescriptionProvider.STATES_NAME;
            } else if (EXHIBIT_STATES_COMPARTMENT.equals(compartmentName)) {
                listStatesNodeDescription += StatesCompartmentNodeDescriptionProvider.EXHIBIT_STATES_NAME;
            }
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(listStatesNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        });

        Runnable semanticCheck = () -> { };

        // Actions are not semantically owned by parent
        if (!SysmlPackage.eINSTANCE.getActionUsage().equals(childEClass)) {
            semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
        }

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
    @MethodSource("stateDefinitionChildNodeParameters")
    public void createStateDefinitionChildNodes(EClass childEClass, String compartmentName, EReference containmentReference, String creationToolName, int expectedNumberOfNewNodes,
            int expectedNumberOfNewEdges) {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        EClass parentEClass = SysmlPackage.eINSTANCE.getStateDefinition();
        String parentLabel = "StateDefinition";
        Runnable createNodeRunnable = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, parentEClass, parentLabel, creationToolName);

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(expectedNumberOfNewNodes)
                    .hasNewEdgeCount(expectedNumberOfNewEdges)
                    .check(initialDiagram, newDiagram);
            String listStatesNodeDescription = this.descriptionNameGenerator.getCompartmentItemName(parentEClass, containmentReference);
            if (STATES_COMPARTMENT.equals(compartmentName)) {
                listStatesNodeDescription += StatesCompartmentNodeDescriptionProvider.STATES_NAME;
            } else if (EXHIBIT_STATES_COMPARTMENT.equals(compartmentName)) {
                listStatesNodeDescription += StatesCompartmentNodeDescriptionProvider.EXHIBIT_STATES_NAME;
            }
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withParentLabel(parentLabel)
                    .withCompartmentName(compartmentName)
                    .hasNodeDescriptionName(listStatesNodeDescription)
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        });

        Runnable semanticCheck = () -> { };

        // Actions are not semantically owned by parent
        if (!SysmlPackage.eINSTANCE.getActionUsage().equals(childEClass)) {
            semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass));
        }

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
