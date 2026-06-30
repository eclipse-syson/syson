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
import java.util.Objects;
import java.util.Optional;
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
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
        new ElementSpecializationInheritanceTestRunner()
                .baseElementToInheritFromEClass(SysmlPackage.eINSTANCE.getActionDefinition())
                .baseElementToInheritFromNodeId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_DEFINITION_ID)
                .elementToInheritCreationToolName("New Parameter In")
                .elementToInheritExpectedListItemLabelText("in ref parameter1")
                .alsoRepresentedByBorderNode()
                .compartmentName("parameters")
                .elementThatInheritFromBaseElementCreationToolName("New Action Definition")
                .elementThatInheritFromBaseElementEClass(SysmlPackage.eINSTANCE.getActionDefinition())
                .specializationToolName("New Subclassification")
                .run();
    }

    @DisplayName("GIVEN a base ActionUsage with behavior parameter, WHEN another ActionUsage is subsetting by reference the base ActionUsage, THEN the base ActionUsage behavior parameters are inherited by the other ActionUsage")
    @Test
    public void checkActionUsageParametersInheritanceWithReferenceSubsetting() {
        new ElementSpecializationInheritanceTestRunner()
                .baseElementToInheritFromEClass(SysmlPackage.eINSTANCE.getActionUsage())
                .baseElementToInheritFromNodeId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_USAGE_ID)
                .elementToInheritCreationToolName("New Parameter Out")
                .elementToInheritExpectedListItemLabelText("out ref parameter1")
                .alsoRepresentedByBorderNode()
                .compartmentName("parameters")
                .elementThatInheritFromBaseElementCreationToolName("New Action")
                .elementThatInheritFromBaseElementEClass(SysmlPackage.eINSTANCE.getActionUsage())
                .specializationToolName("New Reference Subsetting")
                .run();
    }

    @DisplayName("GIVEN an ActionDefinition with an action, WHEN using the ActionDefinition as a feature type of an ActionUsage, THEN the ActionUsage actions are inherited from the ActionDefinition")
    @Test
    public void checkActionDefinitionActionsInheritanceWithFeatureTyping() {
        new ElementSpecializationInheritanceTestRunner()
                .baseElementToInheritFromEClass(SysmlPackage.eINSTANCE.getActionDefinition())
                .baseElementToInheritFromNodeId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_DEFINITION_ID)
                .elementToInheritCreationToolName("New Action")
                .withEdgeExpected()
                .elementToInheritExpectedListItemLabelText("action1")
                .compartmentName("actions")
                .elementThatInheritFromBaseElementCreationToolName("New Action")
                .elementThatInheritFromBaseElementEClass(SysmlPackage.eINSTANCE.getActionUsage())
                .specializationToolName("New Feature Typing")
                .run();
    }

    @DisplayName("GIVEN a base ActionUsage with an action, WHEN subsetting the base ActionUsage with another ActionUsage, THEN the ActionUsage actions are inherited from the base ActionUsage")
    @Test
    public void checkActionUsageActionsInheritanceWithSubsetting() {
        new ElementSpecializationInheritanceTestRunner()
                .baseElementToInheritFromEClass(SysmlPackage.eINSTANCE.getActionUsage())
                .baseElementToInheritFromNodeId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_USAGE_ID)
                .elementToInheritCreationToolName("New Action")
                .withEdgeExpected()
                .elementToInheritExpectedListItemLabelText("action1")
                .compartmentName("actions")
                .elementThatInheritFromBaseElementCreationToolName("New Action")
                .elementThatInheritFromBaseElementEClass(SysmlPackage.eINSTANCE.getActionUsage())
                .specializationToolName("New Subsetting")
                .run();
    }

    @DisplayName("GIVEN a base ActionUsage with an item, WHEN subsetting the base ActionUsage with another ActionUsage, THEN the ActionUsage items are inherited from the base ActionUsage")
    @Test
    public void checkActionUsageItemsInheritanceWithSubsetting() {
        new ElementSpecializationInheritanceTestRunner()
                .baseElementToInheritFromEClass(SysmlPackage.eINSTANCE.getActionUsage())
                .baseElementToInheritFromNodeId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_USAGE_ID)
                .elementToInheritCreationToolName("New Item")
                .withEdgeExpected()
                .elementToInheritExpectedListItemLabelText("item1")
                .compartmentName("items")
                .elementThatInheritFromBaseElementCreationToolName("New Action")
                .elementThatInheritFromBaseElementEClass(SysmlPackage.eINSTANCE.getActionUsage())
                .specializationToolName("New Subsetting")
                .run();
    }

    @DisplayName("GIVEN an ActionUsage with an action, WHEN a StateUsage is subsetting by reference the ActionUsage, THEN the ActionUsage actions are inherited by the StateUsage")
    @Test
    public void checkActionUsageActionsInheritanceWithReferenceSubsetting() {
        new ElementSpecializationInheritanceTestRunner()
                .baseElementToInheritFromEClass(SysmlPackage.eINSTANCE.getActionUsage())
                .baseElementToInheritFromNodeId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.ACTION_USAGE_ID)
                .elementToInheritCreationToolName("New Action")
                .withEdgeExpected()
                .elementToInheritExpectedListItemLabelText("action1")
                .compartmentName("actions")
                .elementThatInheritFromBaseElementCreationToolName("New State")
                .elementThatInheritFromBaseElementEClass(SysmlPackage.eINSTANCE.getStateUsage())
                .specializationToolName("New Reference Subsetting")
                .run();
    }

    @DisplayName("GIVEN a base PartDefinition with a state, WHEN another PartDefinition is subclassing the base PartDefinition, THEN the base PartDefinition states are inherited by the other PartDefinition")
    @Test
    public void checkParDefinitionStatesInheritanceWithSubclassification() {
        new ElementSpecializationInheritanceTestRunner()
                .baseElementToInheritFromEClass(SysmlPackage.eINSTANCE.getPartDefinition())
                .baseElementToInheritFromNodeId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_DEFINITION_ID)
                .elementToInheritCreationToolName("New State")
                .withEdgeExpected()
                .elementToInheritExpectedListItemLabelText("state1")
                .compartmentName("states")
                .elementThatInheritFromBaseElementCreationToolName("New Part Definition")
                .elementThatInheritFromBaseElementEClass(SysmlPackage.eINSTANCE.getPartDefinition())
                .specializationToolName("New Subclassification")
                .run();
    }

    @DisplayName("GIVEN a StateDefinition with a state, WHEN a StateUsage is typed by the StateDefinition, THEN the StateDefinition states are inherited by the StateUsage")
    @Test
    public void checkStateDefinitionStatesInheritanceWithFeatureTyping() {
        new ElementSpecializationInheritanceTestRunner()
                .baseElementToInheritFromEClass(SysmlPackage.eINSTANCE.getStateDefinition())
                .baseElementToInheritFromNodeId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.STATE_DEFINITION_ID)
                .elementToInheritCreationToolName("New State")
                .withEdgeExpected()
                .elementToInheritExpectedListItemLabelText("state1")
                .compartmentName("states")
                .elementThatInheritFromBaseElementCreationToolName("New State")
                .elementThatInheritFromBaseElementEClass(SysmlPackage.eINSTANCE.getStateUsage())
                .specializationToolName("New Feature Typing")
                .run();
    }

    @DisplayName("GIVEN a base StateUsage with a state, WHEN another StateUsage redefines the base StateUsage, THEN the base StateUsage states are inherited by the redefining StateUsage")
    @Test
    public void checkStateUsageStatesInheritanceWithRedefinition() {
        new ElementSpecializationInheritanceTestRunner()
                .baseElementToInheritFromEClass(SysmlPackage.eINSTANCE.getStateUsage())
                .baseElementToInheritFromNodeId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.STATE_USAGE_ID)
                .elementToInheritCreationToolName("New State")
                .withEdgeExpected()
                .elementToInheritExpectedListItemLabelText("state1")
                .compartmentName("states")
                .elementThatInheritFromBaseElementCreationToolName("New State")
                .elementThatInheritFromBaseElementEClass(SysmlPackage.eINSTANCE.getStateUsage())
                .specializationToolName("New Redefinition")
                .run();
    }

    @DisplayName("GIVEN a base PartDefinition with an exhibit state, WHEN another PartDefinition is subclassing the base PartDefinition, THEN the base PartDefinition exhibit states are inherited by the other PartDefinition")
    @Test
    public void checkPartDefinitionExhibitStatesInheritanceWithSubclassification() {
        new ElementSpecializationInheritanceTestRunner()
                .baseElementToInheritFromEClass(SysmlPackage.eINSTANCE.getPartDefinition())
                .baseElementToInheritFromNodeId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.PART_DEFINITION_ID)
                .elementToInheritCreationToolName("New Exhibit State")
                .withEdgeExpected()
                .withSelectedElementId(GeneralViewWithTopNodesTestProjectData.SemanticIds.STATE_USAGE_ID)
                .elementToInheritExpectedListItemLabelText("ref  ::> state")
                .compartmentName("exhibit states")
                .elementThatInheritFromBaseElementCreationToolName("New Part Definition")
                .elementThatInheritFromBaseElementEClass(SysmlPackage.eINSTANCE.getPartDefinition())
                .specializationToolName("New Subclassification")
                .run();
    }

    @DisplayName("GIVEN a StateDefinition with an exhibit state, WHEN a StateUsage is typed by the StateDefinition, THEN the StateDefinition exhibit states are inherited by the StateUsage")
    @Test
    public void checkStateDefinitionExhibitStatesInheritanceWithFeatureTyping() {
        new ElementSpecializationInheritanceTestRunner()
                .baseElementToInheritFromEClass(SysmlPackage.eINSTANCE.getStateDefinition())
                .baseElementToInheritFromNodeId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.STATE_DEFINITION_ID)
                .elementToInheritCreationToolName("New Exhibit State")
                .withEdgeExpected()
                .withSelectedElementId(GeneralViewWithTopNodesTestProjectData.SemanticIds.STATE_USAGE_ID)
                .elementToInheritExpectedListItemLabelText("ref  ::> state")
                .compartmentName("exhibit states")
                .elementThatInheritFromBaseElementCreationToolName("New State")
                .elementThatInheritFromBaseElementEClass(SysmlPackage.eINSTANCE.getStateUsage())
                .specializationToolName("New Feature Typing")
                .run();
    }

    @DisplayName("GIVEN a base StateUsage with an exhibit state, WHEN another StateUsage redefines the base StateUsage, THEN the base StateUsage exhibit states are inherited by the redefining StateUsage")
    @Test
    public void checkStateUsageExhibitStatesInheritanceWithRedefinition() {
        new ElementSpecializationInheritanceTestRunner()
                .baseElementToInheritFromEClass(SysmlPackage.eINSTANCE.getStateUsage())
                .baseElementToInheritFromNodeId(GeneralViewWithTopNodesTestProjectData.GraphicalIds.STATE_USAGE_ID)
                .elementToInheritCreationToolName("New Exhibit State")
                .withEdgeExpected()
                .withSelectedElementId(GeneralViewWithTopNodesTestProjectData.SemanticIds.STATE_USAGE_ID)
                .elementToInheritExpectedListItemLabelText("ref  ::> state")
                .compartmentName("exhibit states")
                .elementThatInheritFromBaseElementCreationToolName("New State")
                .elementThatInheritFromBaseElementEClass(SysmlPackage.eINSTANCE.getStateUsage())
                .specializationToolName("New Redefinition")
                .run();
    }

    /**
     * This test runner verifies that creating a specializing relationship create inherited elements.
     *
     * It executes the following steps then verifies the inherited elements.
     * <ol>
     *     <li>Applies the tool {@code elementToInheritCreationToolName} with the optionally provided {@code selectedElementId} on the base element with the <em>EClass</em> {@code baseElementToInheritFromEClass}, with the <em>target object ID</em> {@code baseElementToInheritFromTargetObjectId}, represented by the graphical node <em>identified by</em> {@code baseElementToInheritFromNodeId}</li>
     *     <li>Applies the node tool {@code elementThatInheritFromBaseElementCreationToolName} to create a node on the diagram background</li>
     *     <li>Applies the tool {@code specializationToolName} to create a specialization relationship between the created node and the base element</li>
     * </ol>
     */
    @SuppressWarnings("checkstyle:HiddenField")
    private final class ElementSpecializationInheritanceTestRunner {

        /**
         * The <em>EClass</em> of the element on which the {@code elementToInheritCreationToolName} tool will be applied.
         */
        private EClass baseElementToInheritFromEClass;

        /**
         * Whether applying the {@code elementToInheritCreationToolName} tool on {@code baseElementToInheritFromNodeId} make new edges to render.
         * <p>
         *     We expect edges when the tool creates an element without revealing the list compartment holding the created element.
         *     That means the new element is created on the background linked with a containment edge.
         * </p>
         * <p>
         *     Another edge is created when the element is created on the background and a non-empty selection is provided.
         *     In that case, a specialization edge is also rendered.
         * </p>
         */
        private boolean withEdgeExpected;

        /**
         * The selected element required for the creation tool applied on the base element.
         */
        private Optional<String> selectedElementId = Optional.empty();

        /**
         * The graphical <em>node ID</em> that represents the element on which the {@code elementToInheritCreationToolName} tool will be applied.
         */
        private String baseElementToInheritFromNodeId;

        /**
         * The tool name to apply on the base element.
         */
        private String elementToInheritCreationToolName;

        /**
         * The list item label of the element created by the {@code elementToInheritCreationToolName} tool.
         */
        private String elementToInheritExpectedListItemLabelText;

        /**
         * If element created by the {@code elementToInheritCreationToolName} tool is also represented by a graphical border node.
         */
        private boolean alsoRepresentedByBorderNode;

        /**
         * The compartment name in which the element created by the {@code elementToInheritCreationToolName} tool is created.
         */
        private String compartmentName;

        /**
         * The tool name used to create an element that will inherit from the base element.
         */
        private String elementThatInheritFromBaseElementCreationToolName;

        /**
         * The <em>EClass</em> of the element created by the {@code elementThatInheritFromBaseElementCreationToolName} tool.
         */
        private EClass elementThatInheritFromBaseElementEClass;

        /**
         * The tool name used to create the specialization relationship between the element created by the {@code elementToInheritCreationToolName} tool, and the base element.
         */
        private String specializationToolName;

        public ElementSpecializationInheritanceTestRunner baseElementToInheritFromEClass(EClass baseElementToInheritFromEClass) {
            this.baseElementToInheritFromEClass = Objects.requireNonNull(baseElementToInheritFromEClass);
            return this;
        }

        public ElementSpecializationInheritanceTestRunner withEdgeExpected() {
            this.withEdgeExpected = true;
            return this;
        }

        public ElementSpecializationInheritanceTestRunner withSelectedElementId(String selectedElementId) {
            this.selectedElementId = Optional.of(selectedElementId);
            return this;
        }

        public ElementSpecializationInheritanceTestRunner baseElementToInheritFromNodeId(String baseElementToInheritFromNodeId) {
            this.baseElementToInheritFromNodeId = Objects.requireNonNull(baseElementToInheritFromNodeId);
            return this;
        }

        public ElementSpecializationInheritanceTestRunner elementToInheritCreationToolName(String elementToInheritCreationToolName) {
            this.elementToInheritCreationToolName = Objects.requireNonNull(elementToInheritCreationToolName);
            return this;
        }

        public ElementSpecializationInheritanceTestRunner elementToInheritExpectedListItemLabelText(String elementToInheritExpectedListItemLabelText) {
            this.elementToInheritExpectedListItemLabelText = Objects.requireNonNull(elementToInheritExpectedListItemLabelText);
            return this;
        }

        public ElementSpecializationInheritanceTestRunner alsoRepresentedByBorderNode() {
            this.alsoRepresentedByBorderNode = true;
            return this;
        }

        public ElementSpecializationInheritanceTestRunner compartmentName(String compartmentName) {
            this.compartmentName = Objects.requireNonNull(compartmentName);
            return this;
        }

        public ElementSpecializationInheritanceTestRunner elementThatInheritFromBaseElementCreationToolName(String elementThatInheritFromBaseElementCreationToolName) {
            this.elementThatInheritFromBaseElementCreationToolName = Objects.requireNonNull(elementThatInheritFromBaseElementCreationToolName);
            return this;
        }

        public ElementSpecializationInheritanceTestRunner elementThatInheritFromBaseElementEClass(EClass elementThatInheritFromBaseElementEClass) {
            this.elementThatInheritFromBaseElementEClass = Objects.requireNonNull(elementThatInheritFromBaseElementEClass);
            return this;
        }

        public ElementSpecializationInheritanceTestRunner specializationToolName(String specializationToolName) {
            this.specializationToolName = Objects.requireNonNull(specializationToolName);
            return this;
        }

        private void isReady() {
            Objects.requireNonNull(this.baseElementToInheritFromEClass);
            Objects.requireNonNull(this.baseElementToInheritFromNodeId);
            Objects.requireNonNull(this.elementToInheritCreationToolName);
            Objects.requireNonNull(this.elementToInheritExpectedListItemLabelText);
            Objects.requireNonNull(this.compartmentName);
            Objects.requireNonNull(this.elementThatInheritFromBaseElementCreationToolName);
            Objects.requireNonNull(this.elementThatInheritFromBaseElementEClass);
            Objects.requireNonNull(this.specializationToolName);
        }

        public void run() {
            this.isReady();

            var flux = GVCompartmentItemInheritanceTests.this.givenSubscriptionToDiagram();
            var diagramDescription = GVCompartmentItemInheritanceTests.this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                    SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
            var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, GVCompartmentItemInheritanceTests.this.diagramIdProvider);

            AtomicReference<Diagram> diagram = new AtomicReference<>();
            Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

            List<ToolVariable> toolVariables = new ArrayList<>();
            selectedElementId.ifPresent(s -> toolVariables.add(new ToolVariable("selectedObject", s, ToolVariableType.OBJECT_ID)));

            // Create an element in the base element to inherit
            String createActionToolId = diagramDescriptionIdProvider.getNodeToolId(GVCompartmentItemInheritanceTests.this.descriptionNameGenerator.getNodeName(this.baseElementToInheritFromEClass), this.elementToInheritCreationToolName);
            Runnable newCreationTool = () -> GVCompartmentItemInheritanceTests.this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagram.get().getId(), this.baseElementToInheritFromNodeId, createActionToolId, toolVariables);
            Consumer<Object> createdActionDiagramConsumer = assertRefreshedDiagramThat(newDiagram -> {
                int expectedBorderNodeCount = 0;
                if (this.alsoRepresentedByBorderNode) {
                    expectedBorderNodeCount = 1;
                }
                int expectEdgeCount = 0;
                if (withEdgeExpected) {
                    expectEdgeCount = 1;
                    if (this.selectedElementId.isPresent() && !this.selectedElementId.get().isBlank()) {
                        expectEdgeCount = 2;
                    }
                }
                new CheckDiagramElementCount(GVCompartmentItemInheritanceTests.this.diagramComparator)
                        .hasNewBorderNodeCount(expectedBorderNodeCount) // The element is created and sometimes can be represented as a border node
                        .hasNewNodeCount(1) // The element is created and represented as a list item
                        .hasNewEdgeCount(expectEdgeCount)
                        .check(diagram.get(), newDiagram, true);

                var actionsCompartment = new DiagramNavigator(newDiagram)
                        .nodeWithId(this.baseElementToInheritFromNodeId)
                        .childNodeWithLabel(this.compartmentName)
                        .getNode();
                assertThat(actionsCompartment.getChildNodes()).hasSize(1);
                assertThat(actionsCompartment.getChildNodes().getFirst().getInsideLabel().getText()).isEqualTo(this.elementToInheritExpectedListItemLabelText); // The created element has the expected name
                diagram.set(newDiagram);
            });


            // Create a new element that will inherit from the base element
            AtomicReference<String> newActionActionUsageNodeId = new AtomicReference<>();
            String createActionUsageToolId = diagramDescriptionIdProvider.getDiagramCreationToolId(this.elementThatInheritFromBaseElementCreationToolName);
            Runnable createActionDefinitionRunnable = () -> GVCompartmentItemInheritanceTests.this.toolTester.invokeTool(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagram, null, createActionUsageToolId);
            Consumer<Object> createdActionDefinitionDiagramConsumer = assertRefreshedDiagramThat(newDiagram -> {
                new CheckDiagramElementCount(GVCompartmentItemInheritanceTests.this.diagramComparator)
                        .hasNewBorderNodeCount(0)
                        .hasNewNodeCount(1) // The new element is created
                        .hasNewEdgeCount(0)
                        .check(diagram.get(), newDiagram, true);
                var newNodes = GVCompartmentItemInheritanceTests.this.diagramComparator.newNodes(diagram.get(), newDiagram);
                newActionActionUsageNodeId.set(newNodes.getFirst().getId());
                diagram.set(newDiagram);
            });


            // Create the specialization between the newly created element and the base element
            String createFeatureTypeToolId = diagramDescriptionIdProvider.getEdgeCreationToolId(GVCompartmentItemInheritanceTests.this.descriptionNameGenerator.getNodeName(this.elementThatInheritFromBaseElementEClass), this.specializationToolName);
            Runnable createFeatureTyping = () -> GVCompartmentItemInheritanceTests.this.edgeCreationTester.createEdgeUsingNodeId(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, diagram, newActionActionUsageNodeId.get(), this.baseElementToInheritFromNodeId, createFeatureTypeToolId);

            // Check new created element inherits from the base element, and thus, contains a list item with '^' in its label
            Consumer<Object> createFeatureTypeDiagramConsumer = assertRefreshedDiagramThat(newDiagram -> {
                new CheckDiagramElementCount(GVCompartmentItemInheritanceTests.this.diagramComparator)
                        .hasNewBorderNodeCount(0)
                        .hasNewNodeCount(1) // The list item inherited from the base element
                        .hasNewEdgeCount(1) // The specialization edge
                        .check(diagram.get(), newDiagram);

                var parameterCompartment = new DiagramNavigator(newDiagram)
                        .nodeWithId(newActionActionUsageNodeId.get())
                        .childNodeWithLabel(this.compartmentName)
                        .getNode();
                assertThat(parameterCompartment.getChildNodes()).hasSize(1);
                assertThat(parameterCompartment.getChildNodes().getFirst().getInsideLabel().getText()).isEqualTo("^" + this.elementToInheritExpectedListItemLabelText); // The inheriting element has the same name prefixed with '^'
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
}
