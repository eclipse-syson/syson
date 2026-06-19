/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariable;
import org.eclipse.sirius.components.collaborative.diagrams.dto.ToolVariableType;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.SysONTestsProperties;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.testers.EdgeCreationTester;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.ActionDefinition;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.metamodel.helper.LabelConstants;
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
 * Tests the {@link ActionDefinition} inherited features behavior with parameters compartment.
 *
 * @author fbarbin
 */
@Transactional
@GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
// CHECKSTYLE:OFF
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = { SysONTestsProperties.NO_DEFAULT_LIBRARIES_PROPERTY })
public class GVCompartmentItemInheritanceTests extends AbstractIntegrationTests {


    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private ToolTester toolTester;

    @Autowired
    private EdgeCreationTester edgeCreationTester;

    @Autowired
    private DiagramComparator diagramComparator;

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a SysML Project with an ActionDefinition that subclasses a UseCaseDefinition, WHEN creating a subject in the UseCaseDefinition, THEN it should not be displayed in the ActionDefinition parameters compartment")
    @Test
    public void checkActionDefinitionParameterFilter() {
        var flux = this.givenSubscriptionToDiagram();
        List<ToolVariable> variables = new ArrayList<>();
        variables.add(new ToolVariable("selectedObject", GeneralViewWithTopNodesTestProjectData.SemanticIds.PART_USAGE_ID, ToolVariableType.OBJECT_ID));

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        String creationToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getUseCaseDefinition()), "New Subject");

        AtomicReference<Diagram> diagram = new AtomicReference<>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable newCreationTool = () -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagram, GeneralViewWithTopNodesTestProjectData.SemanticIds.USE_CASE_DEFINITION_ID, creationToolId, variables);

        Consumer<Object> updatedDiagramConsumer = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewBorderNodeCount(0)
                    .hasNewNodeCount(1) // Only one subject in the UseCaseDefinition subjects compartment.
                    .hasNewEdgeCount(0)
                    .check(diagram.get(), newDiagram, true);

            var useCaseDefSubjectCompartment = new DiagramNavigator(newDiagram)
                    .nodeWithLabel(LabelConstants.OPEN_QUOTE + "use case def" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "UseCaseDefinition").childNodeWithLabel("subject").getNode();
            assertThat(useCaseDefSubjectCompartment.getChildNodes()).hasSize(1);

            var actionDefParametersCompartment = new DiagramNavigator(newDiagram)
                    .nodeWithLabel(LabelConstants.OPEN_QUOTE + "action def" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "ActionDefinition :> UseCaseDefinition").childNodeWithLabel("parameters")
                    .getNode();
            assertThat(actionDefParametersCompartment.getChildNodes()).hasSize(0);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(newCreationTool)
                .consumeNextWith(updatedDiagramConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN an ActionDefinition with behavior parameter, WHEN subclassing the ActionDefinition with another ActionDefinition, THEN the other ActionDefinition behavior parameters are inherited from the subclassed ActionDefinition")
    @Test
    public void checkActionDefinitionParametersInheritanceWithSubclassification() {
        this.checkListItemInBaseElementAreInheritedInSpecializingElement(
                SysmlPackage.eINSTANCE.getActionDefinition(),
                GeneralViewWithTopNodesTestProjectData.SemanticIds.ACTION_DEFINITION_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_DEFINITION_ID,
                "New Parameter In",
                "in ref parameter1",
                true,
                "parameters",
                "New Action Definition",
                SysmlPackage.eINSTANCE.getActionDefinition(),
                "New Subclassification"
        );
    }

    @DisplayName("GIVEN a base ActionUsage with behavior parameter, WHEN another ActionUsage is subsetting by reference the base ActionUsage, THEN the base ActionUsage behavior parameters are inherited by the other ActionUsage")
    @Test
    public void checkActionUsageParametersInheritanceWithReferenceSubsetting() {
        this.checkListItemInBaseElementAreInheritedInSpecializingElement(
                SysmlPackage.eINSTANCE.getActionUsage(),
                GeneralViewWithTopNodesTestProjectData.SemanticIds.ACTION_USAGE_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_USAGE_ID,
                "New Parameter Out",
                "out ref parameter1",
                true,
                "parameters",
                "New Action",
                SysmlPackage.eINSTANCE.getActionUsage(),
                "New Reference Subsetting"
        );
    }

    @DisplayName("GIVEN an ActionDefinition with an action, WHEN using the ActionDefinition as a feature type of an ActionUsage, THEN the ActionUsage actions are inherited from the ActionDefinition")
    @Test
    public void checkActionDefinitionActionsInheritanceWithFeatureTyping() {
        this.checkListItemInBaseElementAreInheritedInSpecializingElement(
                SysmlPackage.eINSTANCE.getActionDefinition(),
                GeneralViewWithTopNodesTestProjectData.SemanticIds.ACTION_DEFINITION_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_DEFINITION_ID,
                "New Action",
                "action1",
                false,
                "actions",
                "New Action",
                SysmlPackage.eINSTANCE.getActionUsage(),
                "New Feature Typing"
        );
    }

    @DisplayName("GIVEN a base ActionUsage with an action, WHEN subsetting the base ActionUsage with another ActionUsage, THEN the ActionUsage actions are inherited from the base ActionUsage")
    @Test
    public void checkActionUsageActionsInheritanceWithSubsetting() {
        this.checkListItemInBaseElementAreInheritedInSpecializingElement(
                SysmlPackage.eINSTANCE.getActionUsage(),
                GeneralViewWithTopNodesTestProjectData.SemanticIds.ACTION_USAGE_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_USAGE_ID,
                "New Action",
                "action1",
                false,
                "actions",
                "New Action",
                SysmlPackage.eINSTANCE.getActionUsage(),
                "New Subsetting"
        );
    }

    @DisplayName("GIVEN a base ActionUsage with an item, WHEN subsetting the base ActionUsage with another ActionUsage, THEN the ActionUsage items are inherited from the base ActionUsage")
    @Test
    public void checkActionUsageItemsInheritanceWithSubsetting() {
        this.checkListItemInBaseElementAreInheritedInSpecializingElement(
                SysmlPackage.eINSTANCE.getActionUsage(),
                GeneralViewWithTopNodesTestProjectData.SemanticIds.ACTION_USAGE_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_USAGE_ID,
                "New Item",
                "item1",
                false,
                "items",
                "New Action",
                SysmlPackage.eINSTANCE.getActionUsage(),
                "New Subsetting"
        );
    }

    @DisplayName("GIVEN an ActionUsage with an action, WHEN a StateUsage is subsetting by reference the ActionUsage, THEN the ActionUsage actions are inherited by the StateUsage")
    @Test
    public void checkActionUsageActionsInheritanceWithReferenceSubsetting() {
        this.checkListItemInBaseElementAreInheritedInSpecializingElement(
                SysmlPackage.eINSTANCE.getActionUsage(),
                GeneralViewWithTopNodesTestProjectData.SemanticIds.ACTION_USAGE_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_USAGE_ID,
                "New Action",
                "action1",
                false,
                "actions",
                "New State",
                SysmlPackage.eINSTANCE.getStateUsage(),
                "New Reference Subsetting"
        );
    }

    private void checkListItemInBaseElementAreInheritedInSpecializingElement(EClass baseElementToInheritFrom, String baseElementToInheritFromTargetObjectId, String baseElementToInheritFromNodeId, String elementToInheritCreationToolName, String elementToInheritExpectedListItemLabelText, boolean alsoRepresentedByBorderNode, String compartmentName, String elementThatInheritFromBaseElementCreationToolName, EClass elementThatInheritFromBaseElementEClass, String specializationToolName) {
        var flux = this.givenSubscriptionToDiagram();
        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        // Create an element in the base element to inherit
        String createActionToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(baseElementToInheritFrom), elementToInheritCreationToolName);
        Runnable newCreationTool = () -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagram, baseElementToInheritFromTargetObjectId, createActionToolId);
        Consumer<Object> createdActionDiagramConsumer = assertRefreshedDiagramThat(newDiagram -> {
            int expectedBorderNodeCount = 0;
            if (alsoRepresentedByBorderNode) {
                expectedBorderNodeCount = 1;
            }
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewBorderNodeCount(expectedBorderNodeCount) // The element is created and sometimes can be represented as a border node
                    .hasNewNodeCount(1) // The element is created and represented as a list item
                    .hasNewEdgeCount(0)
                    .check(diagram.get(), newDiagram, true);

            var actionsCompartment = new DiagramNavigator(newDiagram)
                    .nodeWithTargetObjectId(baseElementToInheritFromTargetObjectId)
                    .childNodeWithLabel(compartmentName)
                    .getNode();
            assertThat(actionsCompartment.getChildNodes()).hasSize(1);
            assertThat(actionsCompartment.getChildNodes().getFirst().getInsideLabel().getText()).isEqualTo(elementToInheritExpectedListItemLabelText); // The created element has the expected name
            diagram.set(newDiagram);
        });


        // Create an new element that will inherit from the base element
        AtomicReference<String> newActionActionUsageNodeId = new AtomicReference<>();
        String createActionUsageToolId = diagramDescriptionIdProvider.getDiagramCreationToolId(elementThatInheritFromBaseElementCreationToolName);
        Runnable createActionDefinitionRunnable = () -> this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagram, null, createActionUsageToolId);
        Consumer<Object> createdActionDefinitionDiagramConsumer = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewBorderNodeCount(0)
                    .hasNewNodeCount(1) // The new element is created
                    .hasNewEdgeCount(0)
                    .check(diagram.get(), newDiagram, true);
            var newNodes = this.diagramComparator.newNodes(diagram.get(), newDiagram);
            newActionActionUsageNodeId.set(newNodes.getFirst().getId());
            diagram.set(newDiagram);
        });


        // Create the specialization between the newly created element and the base element
        String createFeatureTypeToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(this.descriptionNameGenerator.getNodeName(elementThatInheritFromBaseElementEClass), specializationToolName);
        Runnable createFeatureTyping = () -> this.edgeCreationTester.createEdgeUsingNodeId(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagram, newActionActionUsageNodeId.get(), baseElementToInheritFromNodeId, createFeatureTypeToolId);

        // Check new created element inherits from the base element, and thus, contains a list item with '^' in its label
        Consumer<Object> createFeatureTypeDiagramConsumer = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewBorderNodeCount(0)
                    .hasNewNodeCount(1) // The list item inherited from the base element
                    .hasNewEdgeCount(1) // The specialization edge
                    .check(diagram.get(), newDiagram);

            var parameterCompartment = new DiagramNavigator(newDiagram)
                    .nodeWithId(newActionActionUsageNodeId.get())
                    .childNodeWithLabel(compartmentName)
                    .getNode();
            assertThat(parameterCompartment.getChildNodes()).hasSize(1);
            assertThat(parameterCompartment.getChildNodes().getFirst().getInsideLabel().getText()).isEqualTo("^" + elementToInheritExpectedListItemLabelText); // The inheriting element has the same name prefixed with '^'
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(newCreationTool)
                .consumeNextWith(createdActionDiagramConsumer)
                .then(createActionDefinitionRunnable)
                .consumeNextWith(createdActionDefinitionDiagramConsumer)
                .then(createFeatureTyping)
                .consumeNextWith(createFeatureTypeDiagramConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
// CHECKSTYLE:ON
