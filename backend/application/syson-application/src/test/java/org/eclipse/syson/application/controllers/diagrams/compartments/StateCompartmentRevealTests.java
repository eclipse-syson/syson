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
package org.eclipse.syson.application.controllers.diagrams.compartments;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.HideDiagramElementInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariable;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariableType;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.diagrams.tests.graphql.HideDiagramElementMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.diagrams.tests.navigation.NavigatorCache;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.NodeCreationTestsService;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.standard.diagrams.view.nodes.PerformActionsCompartmentNodeDescriptionProvider;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.util.IDescriptionNameGenerator;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Tests the compartment reveal behavior of a State.
 *
 * @author gcoutable
 */
@Transactional
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StateCompartmentRevealTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private HideDiagramElementMutationRunner hideDiagramElementMutationRunner;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private ToolTester nodeCreationTester;

    @Autowired
    private DiagramComparator diagramComparator;

    private NodeCreationTestsService creationTestsService;

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

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
    }

    @DisplayName("GIVEN a StateUsage graphical node in a state transition compartment, WHEN executing the tool to create a Do Action, THEN the perform action compartment is revealed on the StateUsage graphical node")
    @Test
    public void testRevealPerformActionOfStateInsideStateTransitionCompartment() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);
        var diagram = new AtomicReference<Diagram>();
        var stateTransitionCompartmentNodeId = new AtomicReference<String>();
        var newStateUsageGraphicalNode = new AtomicReference<Node>();

        var initialDiagramContentConsumer = assertRefreshedDiagramThat(initialDiagram -> {
            diagram.set(initialDiagram);
            var stateTransitionCompartment = new DiagramNavigator(initialDiagram)
                    .nodeWithId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.STATE_USAGE_ID)
                    .childNodeWithNodeDescriptionId(diagramDescriptionIdProvider
                            .getNodeDescriptionId(this.descriptionNameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState())))
                    .getNode();
            assertThat(stateTransitionCompartment.getState()).isEqualTo(ViewModifier.Hidden);
            stateTransitionCompartmentNodeId.set(stateTransitionCompartment.getId());
        });

        Runnable revealStateTransitionCompartment = () -> {
            // Reveal the "state transition" compartment of StateUsage graphical node
            var input = new HideDiagramElementInput(UUID.randomUUID(), GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagram.get().getId(), Set.of(stateTransitionCompartmentNodeId.get()), false);
            var result = this.hideDiagramElementMutationRunner.run(input);
            assertThat(result.errors()).isEmpty();

            var typename = JsonPath.read(result.data(), "$.data.hideDiagramElement.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        var updatedDiagramAfterCompartmentReveal = assertRefreshedDiagramThat(refreshedDiagram -> {
            var stateTransitionCompartment = new DiagramNavigator(refreshedDiagram)
                    .nodeWithId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.STATE_USAGE_ID)
                    .childNodeWithNodeDescriptionId(diagramDescriptionIdProvider
                            .getNodeDescriptionId(this.descriptionNameGenerator.getFreeFormCompartmentName(SysmlPackage.eINSTANCE.getStateUsage(), SysmlPackage.eINSTANCE.getUsage_NestedState())))
                    .getNode();
            assertThat(stateTransitionCompartment.getState()).isEqualTo(ViewModifier.Normal);
            // Check there is only one StateUsage graphical node in the diagram for now (because the diagram contains
            // one of many kind of SysML elements)
            var stateUsages = new NavigatorCache(refreshedDiagram).getNodeDescriptionIdToNodes().get(diagramDescriptionIdProvider.getNodeDescriptionId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getStateUsage())));
            assertThat(stateUsages).size().isEqualTo(1);
            diagram.set(refreshedDiagram);
        });

        // Create a state in the StateUsage which will create two graphical nodes, one hidden on the diagram background,
        // and another one visible in the StateUsage "state transition" compartment.
        Runnable createStateInStateTransition = this.creationTestsService.createNode(diagramDescriptionIdProvider, diagram, SysmlPackage.eINSTANCE.getStateUsage(),
                GeneralViewWithTopNodesTestProjectData.SemanticIds.STATE_USAGE_ID, SysmlPackage.eINSTANCE.getStateUsage());

        var updatedDiagramAfterStateCreation = assertRefreshedDiagramThat(refreshedDiagram -> {
            // We have created a StateUsage in a StateUsage, thus, there are two more StateUsage graphical nodes, one
            // hidden on the diagram background, and another one visible in the StateUsage "state transition"
            // compartment.
            var stateUsages = new NavigatorCache(refreshedDiagram).getNodeDescriptionIdToNodes().get(diagramDescriptionIdProvider.getNodeDescriptionId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getStateUsage())));
            assertThat(stateUsages).size().isEqualTo(3);
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .check(diagram.get(), refreshedDiagram, true);
            var newStateNode = new DiagramNavigator(refreshedDiagram)
                    .nodeWithId(stateTransitionCompartmentNodeId.get())
                    .childNodeWithNodeDescriptionId(diagramDescriptionIdProvider.getNodeDescriptionId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getStateUsage())))
                    .getNode();
            assertThat(newStateNode.getState()).isEqualTo(ViewModifier.Normal);
            diagram.set(refreshedDiagram);
            newStateUsageGraphicalNode.set(newStateNode);
        });

        Runnable createDoAction = () -> {
            // Create a Do Action in the StateUsage which should reveal the StateUsage "perform action" compartment
            var toolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getStateUsage()), "New Do Action");
            var variables = List.of(new ToolVariable("selectedObject", "", ToolVariableType.OBJECT_ID));
            this.nodeCreationTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagram.get().getId(), newStateUsageGraphicalNode.get().getId(), toolId, variables);
        };

        var updatedDiagramAfterDoActionCreated = assertRefreshedDiagramThat(refreshedDiagram -> {
            this.checkDiagramAfterDoActionCreated(diagramDescriptionIdProvider, newStateUsageGraphicalNode, refreshedDiagram);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(revealStateTransitionCompartment)
                .consumeNextWith(updatedDiagramAfterCompartmentReveal)
                .then(createStateInStateTransition)
                .consumeNextWith(updatedDiagramAfterStateCreation)
                .then(createDoAction)
                .consumeNextWith(updatedDiagramAfterDoActionCreated)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    private void checkDiagramAfterDoActionCreated(DiagramDescriptionIdProvider diagramDescriptionIdProvider, AtomicReference<Node> newStateUsageGraphicalNode, Diagram refreshedDiagram) {
        // Check that between the two new StateUsage graphical node, the compartment is revealed on the graphical node with ViewModifier.Normal state.
        var stateUsages = new NavigatorCache(refreshedDiagram).getNodeDescriptionIdToNodes().get(diagramDescriptionIdProvider.getNodeDescriptionId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getStateUsage()))).stream()
                .filter(node -> node.getTargetObjectId().equals(newStateUsageGraphicalNode.get().getTargetObjectId()))
                .toList();
        var performActionCompartmentDescriptionId = diagramDescriptionIdProvider.getNodeDescriptionId(this.descriptionNameGenerator.getCompartmentName(SysmlPackage.eINSTANCE.getActionUsage(), SysmlPackage.eINSTANCE.getUsage_NestedAction()) + PerformActionsCompartmentNodeDescriptionProvider.PERFORM_ACTIONS_COMPARTMENT_NAME);
        assertThat(stateUsages).allSatisfy(node -> {
            if (node.getState().equals(ViewModifier.Hidden)) {
                var matchingCompartment = node.getChildNodes().stream()
                        .filter(child -> child.getDescriptionId().equals(performActionCompartmentDescriptionId))
                        .toList();
                assertThat(matchingCompartment).isNotEmpty().allMatch(child -> child.getState().equals(ViewModifier.Hidden));
            } else {
                var matchingCompartment = node.getChildNodes().stream()
                        .filter(child -> child.getDescriptionId().equals(performActionCompartmentDescriptionId))
                        .toList();
                assertThat(matchingCompartment).isNotEmpty().allMatch(child -> child.getState().equals(ViewModifier.Normal));
            }
        });
    }
}
