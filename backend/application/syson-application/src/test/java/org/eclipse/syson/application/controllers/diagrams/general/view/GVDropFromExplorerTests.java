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

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.InvokeSingleClickOnDiagramElementToolInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.InvokeSingleClickOnDiagramElementToolSuccessPayload;
import org.eclipse.sirius.components.collaborative.dto.CreateChildInput;
import org.eclipse.sirius.components.collaborative.dto.CreateChildSuccessPayload;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.diagrams.tests.graphql.InvokeSingleClickOnDiagramElementToolMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.graphql.PaletteQueryRunner;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.graphql.CreateChildMutationRunner;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckNodeOnDiagram;
import org.eclipse.syson.application.controllers.diagrams.testers.DropFromExplorerTester;
import org.eclipse.syson.application.data.GeneralViewAddExistingElementsTestProjectData;
import org.eclipse.syson.application.data.GeneralViewEmptyTestProjectData;
import org.eclipse.syson.application.services.SysMLv2EditService;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.ViewUsage;
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
 * Tests the drop of elements from the explorer on the General View diagram.
 *
 * @author gdaniel
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVDropFromExplorerTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private IIdentityService identityService;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private DropFromExplorerTester dropFromExplorerTester;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private DiagramComparator diagramComparator;

    @Autowired
    private CreateChildMutationRunner createChildMutationRunner;

    @Autowired
    private PaletteQueryRunner paletteQueryRunner;

    @Autowired
    private InvokeSingleClickOnDiagramElementToolMutationRunner invokeSingleClickOnDiagramElementToolMutationRunner;

    @Autowired
    private IFeedbackMessageService feedbackMessageService;

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();


    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewAddExistingElementsTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewAddExistingElementsTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToEmptyDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                GeneralViewEmptyTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }


    @GivenSysONServer({ GeneralViewAddExistingElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void dropFromExplorerOnEmptyDiagram() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<String> semanticElementId = new AtomicReference<>();
        Runnable semanticChecker = this.semanticRunnableFactory.createRunnable(GeneralViewAddExistingElementsTestProjectData.EDITING_CONTEXT_ID,
                (editingContext, executeEditingContextFunctionInput) -> {
                    String partUsageId = this.getSemanticElementWithTargetObjectLabel(editingContext, "part1");
                    semanticElementId.set(partUsageId);
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewAddExistingElementsTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        Runnable dropFromExplorerRunnable = () -> {
            assertThat(diagram.get().getNodes()).hasSize(0);
            var background = diagram.get().getStyle().getBackground();
            assertThat(background).isNotEqualTo("transparent");
            this.dropFromExplorerTester.dropFromExplorerOnDiagram(GeneralViewAddExistingElementsTestProjectData.EDITING_CONTEXT_ID, diagram, semanticElementId.get());
        };

        Consumer<Object> updatedDiagramConsumer = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                .hasNewEdgeCount(0)
                // 1 node for the PartUsage, 11 for its compartments, 1 for the list-item for part2 in its
                // "parts" compartment
                .hasNewNodeCount(13)
                .check(diagram.get(), newDiagram);
            new CheckNodeOnDiagram(diagramDescriptionIdProvider, this.diagramComparator)
                .hasNodeDescriptionName(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()))
                .hasTargetObjectLabel("part1")
                .hasTotalCompartmentCount(11)
                .check(diagram.get(), newDiagram);
            var background = newDiagram.getStyle().getBackground();
            assertThat(background).isEqualTo("transparent");
        });

        Runnable exposedElementsChecker = this.semanticRunnableFactory.createRunnable(GeneralViewAddExistingElementsTestProjectData.EDITING_CONTEXT_ID,
                (editingContext, executeEditingContextFunctionInput) -> {
                    ViewUsage generalViewViewUsage = this.objectSearchService.getObject(editingContext, GeneralViewAddExistingElementsTestProjectData.SemanticIds.GENERAL_VIEW_VIEW_USAGE_ID)
                            .filter(ViewUsage.class::isInstance)
                            .map(ViewUsage.class::cast)
                            .orElse(null);
                    assertThat(generalViewViewUsage).isNotNull();
                    assertThat(generalViewViewUsage.getExposedElement()).hasSize(1);
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        StepVerifier.create(flux)
                .then(semanticChecker)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(dropFromExplorerRunnable)
                .consumeNextWith(updatedDiagramConsumer)
                .then(exposedElementsChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN an Element with no declared name but having a declared short name, WHEN drag and dropping this element on a diagram, THEN graphical node should be created")
    @GivenSysONServer({ GeneralViewAddExistingElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void dropFromExplorerShortNameOnlyOnEmptyDiagram() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewAddExistingElementsTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        Runnable dropFromExplorerRunnable = () -> {
            assertThat(diagram.get().getNodes()).hasSize(0);
            this.dropFromExplorerTester.dropFromExplorerOnDiagram(GeneralViewAddExistingElementsTestProjectData.EDITING_CONTEXT_ID, diagram,
                    GeneralViewAddExistingElementsTestProjectData.SemanticIds.SN_REQUIREMENT_ELEMENT_ID);
        };

        Consumer<Object> updatedDiagramConsumer = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewEdgeCount(0)
                    // 1 node for the Requirement and 8 for its compartments and one for the documentation
                    .hasNewNodeCount(10)
                    .check(diagram.get(), newDiagram);
            new CheckNodeOnDiagram(diagramDescriptionIdProvider, this.diagramComparator)
                    .hasNodeDescriptionName(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getRequirementUsage()))
                    .hasTargetObjectLabel("RequirementUsage")
                    .hasTotalCompartmentCount(8)
                    .check(diagram.get(), newDiagram);
        });

        Runnable exposedElementsChecker = this.semanticRunnableFactory.createRunnable(GeneralViewAddExistingElementsTestProjectData.EDITING_CONTEXT_ID,
                (editingContext, executeEditingContextFunctionInput) -> {
                    ViewUsage generalViewViewUsage = this.objectSearchService.getObject(editingContext, GeneralViewAddExistingElementsTestProjectData.SemanticIds.GENERAL_VIEW_VIEW_USAGE_ID)
                            .filter(ViewUsage.class::isInstance)
                            .map(ViewUsage.class::cast)
                            .orElse(null);
                    assertThat(generalViewViewUsage).isNotNull();
                    assertThat(generalViewViewUsage.getExposedElement()).hasSize(1);
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(dropFromExplorerRunnable)
                .consumeNextWith(updatedDiagramConsumer)
                .then(exposedElementsChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));

    }

    @GivenSysONServer({ GeneralViewAddExistingElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void dropFromExplorerTwiceShouldNotExposeElementTwice() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<String> semanticElementId = new AtomicReference<>();
        Runnable initialState = this.semanticRunnableFactory.createRunnable(GeneralViewAddExistingElementsTestProjectData.EDITING_CONTEXT_ID,
                (editingContext, executeEditingContextFunctionInput) -> {
                    String partUsageId = this.getSemanticElementWithTargetObjectLabel(editingContext, "part1");
                    semanticElementId.set(partUsageId);
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        Runnable hasBeenExposed = this.semanticRunnableFactory.createRunnable(GeneralViewAddExistingElementsTestProjectData.EDITING_CONTEXT_ID,
                (editingContext, executeEditingContextFunctionInput) -> {
                    var generalViewViewUsage = this.objectSearchService.getObject(editingContext, GeneralViewAddExistingElementsTestProjectData.SemanticIds.GENERAL_VIEW_VIEW_USAGE_ID).orElse(null);
                    assertThat(generalViewViewUsage).isInstanceOf(ViewUsage.class);
                    var part1 = this.objectSearchService.getObject(editingContext, GeneralViewAddExistingElementsTestProjectData.SemanticIds.PART_1_ELEMENT_ID).orElse(null);
                    assertThat(part1).isInstanceOf(PartUsage.class);
                    assertThat(((ViewUsage) generalViewViewUsage).getExposedElement()).hasSize(1);
                    assertThat(((ViewUsage) generalViewViewUsage).getExposedElement()).contains((Element) part1);
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        Runnable hasNotBeenExposedAgain = this.semanticRunnableFactory.createRunnable(GeneralViewAddExistingElementsTestProjectData.EDITING_CONTEXT_ID,
                (editingContext, executeEditingContextFunctionInput) -> {
                    var generalViewViewUsage = this.objectSearchService.getObject(editingContext, GeneralViewAddExistingElementsTestProjectData.SemanticIds.GENERAL_VIEW_VIEW_USAGE_ID).orElse(null);
                    assertThat(generalViewViewUsage).isInstanceOf(ViewUsage.class);
                    var part1 = this.objectSearchService.getObject(editingContext, GeneralViewAddExistingElementsTestProjectData.SemanticIds.PART_1_ELEMENT_ID).orElse(null);
                    assertThat(part1).isInstanceOf(PartUsage.class);
                    assertThat(((ViewUsage) generalViewViewUsage).getExposedElement()).hasSize(1);
                    assertThat(((ViewUsage) generalViewViewUsage).getExposedElement()).contains((Element) part1);
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable dropPart1 = () -> {
            var result = this.dropFromExplorerTester.dropFromExplorer(GeneralViewAddExistingElementsTestProjectData.EDITING_CONTEXT_ID, diagram,
                    null, semanticElementId.get());
            List<Object> messages = JsonPath.read(result.data(), "$.data.dropOnDiagram.messages[*]");
            assertThat(messages).isEmpty();
        };

        Runnable dropAgainPart1 = () -> {
            var result = this.dropFromExplorerTester.dropFromExplorer(GeneralViewAddExistingElementsTestProjectData.EDITING_CONTEXT_ID, diagram,
                    null, semanticElementId.get());
            List<Object> messages = JsonPath.read(result.data(), "$.data.dropOnDiagram.messages[*]");
            assertThat(messages).as("We should receive at least one message when dropping an already visible element").hasSizeGreaterThanOrEqualTo(1);
            String messageBody = JsonPath.read(result.data(), "$.data.dropOnDiagram.messages[0].body");
            assertThat(messageBody).isEqualTo("The element part1 is already visible in its parent Package 1");
            String messageLevel = JsonPath.read(result.data(), "$.data.dropOnDiagram.messages[0].level");
            assertThat(messageLevel).isEqualTo(MessageLevel.WARNING.toString());
        };

        StepVerifier.create(flux)
                .then(initialState)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(dropPart1)
                .consumeNextWith(payload -> { })
                .then(hasBeenExposed)
                .then(dropAgainPart1)
                .consumeNextWith(payload -> { })
                .then(hasNotBeenExposedAgain)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a diagram WHEN dropping a semantic element from the explorer on various targets THEN the user gets the appropriate feedback message")
    @GivenSysONServer({ GeneralViewAddExistingElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void dropFromExplorerFeedback() {
        AtomicReference<Diagram> diagram = new AtomicReference<>();
        AtomicReference<String> packageNodeId = new AtomicReference<>();
        AtomicReference<String> attributeNodeId = new AtomicReference<>();
        AtomicReference<String> hideToolId = new AtomicReference<>();

        String package1Id = GeneralViewAddExistingElementsTestProjectData.SemanticIds.PACKAGE1_ID;
        String attributeDefinition1Id = GeneralViewAddExistingElementsTestProjectData.SemanticIds.ATTRIBUTE_DEFINITION_1_ID;

        var flux = this.givenSubscriptionToDiagram();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable dropPackageOnDiagramBackground = () -> this.dropFromExplorer(diagram, null, package1Id, Optional.empty());

        Consumer<Object> diagramWithPackageNodeConsumer = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator).hasNewNodeCount(1).check(diagram.get(), newDiagram);
            var packageNode = new DiagramNavigator(newDiagram).nodeWithTargetObjectId(package1Id).getNode();
            assertThat(packageNode).isNotNull();
            packageNodeId.set(packageNode.getId());
            diagram.set(newDiagram);
        });

        Runnable dropAttributeOnPackage = () -> this.dropFromExplorer(diagram, packageNodeId.get(), attributeDefinition1Id, Optional.empty());

        Consumer<Object> diagramWithAttributeNodeConsumer = assertRefreshedDiagramThat(newDiagram -> {
            // 3 new nodes: AttributeDefinition container and its 2 compartments
            new CheckDiagramElementCount(this.diagramComparator).hasNewNodeCount(3).check(diagram.get(), newDiagram);
            var attributeNode = new DiagramNavigator(newDiagram).nodeWithTargetObjectId(attributeDefinition1Id).getNode();
            assertThat(attributeNode).isNotNull().extracting(Node::getState).isEqualTo(ViewModifier.Normal);
            attributeNodeId.set(attributeNode.getId());
            diagram.set(newDiagram);
        });

        Runnable getHideTool = () -> hideToolId.set(this.getQuickToolIdByLabel(diagram.get().getId(), attributeNodeId.get(), "Hide"));

        Runnable dropAttributeOnDiagram = () -> this.dropFromExplorer(diagram,
                null, attributeDefinition1Id,
                Optional.of("The element AttributeDefinition1 is already visible in its parent Package1"));

        Consumer<Object> diagramNotChangeConsumer = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator).check(diagram.get(), newDiagram);
            diagram.set(newDiagram);
        });

        Runnable hideAttributeNode = () -> {
            var input = new InvokeSingleClickOnDiagramElementToolInput(UUID.randomUUID(), GeneralViewAddExistingElementsTestProjectData.EDITING_CONTEXT_ID, diagram.get().getId(),
                    List.of(attributeNodeId.get()), hideToolId.get(), 0, 0, List.of());
            var result = this.invokeSingleClickOnDiagramElementToolMutationRunner.run(input);
            String typename = JsonPath.read(result.data(), "$.data.invokeSingleClickOnDiagramElementTool.__typename");
            assertThat(typename).isEqualTo(InvokeSingleClickOnDiagramElementToolSuccessPayload.class.getSimpleName());
        };

        Consumer<Object> atributeNodeHiddenConsumer = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator).check(diagram.get(), newDiagram);
            var attributeNode = new DiagramNavigator(newDiagram).nodeWithTargetObjectId(attributeDefinition1Id).getNode();
            assertThat(attributeNode).isNotNull().extracting(Node::getState).isEqualTo(ViewModifier.Hidden);
            diagram.set(newDiagram);
        });

        Runnable dropAttributeOnDiagramNoMessage = () -> this.dropFromExplorer(diagram, null, attributeDefinition1Id, Optional.empty());

        Runnable dropAttributeOnPackageAgain = () -> this.dropFromExplorer(diagram, packageNodeId.get(), attributeDefinition1Id, Optional.empty());

        Consumer<Object> diagramWithAttributeNodeRevealedConsumer = assertRefreshedDiagramThat(newDiagram -> {
            new CheckDiagramElementCount(this.diagramComparator).check(diagram.get(), newDiagram);
            var attributeNode = new DiagramNavigator(newDiagram).nodeWithTargetObjectId(GeneralViewAddExistingElementsTestProjectData.SemanticIds.ATTRIBUTE_DEFINITION_1_ID).getNode();
            assertThat(attributeNode).isNotNull().extracting(Node::getState).isEqualTo(ViewModifier.Normal);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(dropPackageOnDiagramBackground)
                .consumeNextWith(diagramWithPackageNodeConsumer)
                .then(dropAttributeOnPackage)
                .consumeNextWith(diagramWithAttributeNodeConsumer)
                .then(getHideTool)
                .then(dropAttributeOnDiagram)
                .consumeNextWith(diagramNotChangeConsumer)
                .then(hideAttributeNode)
                .consumeNextWith(atributeNodeHiddenConsumer)
                .then(dropAttributeOnDiagramNoMessage)
                .consumeNextWith(diagramNotChangeConsumer)
                .then(dropAttributeOnPackageAgain)
                .consumeNextWith(diagramWithAttributeNodeRevealedConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    private void dropFromExplorer(AtomicReference<Diagram> diagram, String targetId, String elementId, Optional<String> expectedWarning) {
        // Workaround: clear the messages left by the previous tool's execution
        this.feedbackMessageService.getFeedbackMessages().clear();
        var result = this.dropFromExplorerTester.dropFromExplorer(GeneralViewAddExistingElementsTestProjectData.EDITING_CONTEXT_ID, diagram,
                targetId, elementId);
        List<Object> messages = JsonPath.read(result.data(), "$.data.dropOnDiagram.messages[*]");
        if (expectedWarning.isPresent()) {
            assertThat(messages).hasSize(1);
            String messageBody = JsonPath.read(result.data(), "$.data.dropOnDiagram.messages[0].body");
            assertThat(messageBody).isEqualTo(expectedWarning.get());
            String messageLevel = JsonPath.read(result.data(), "$.data.dropOnDiagram.messages[0].level");
            assertThat(messageLevel).isEqualTo(MessageLevel.WARNING.toString());
        } else {
            assertThat(messages).isEmpty();
        }
    }

    private String getQuickToolIdByLabel(String diagramId, String nodeId, String toolName) {
        Map<String, Object> variables = Map.of(
                "editingContextId", GeneralViewAddExistingElementsTestProjectData.EDITING_CONTEXT_ID,
                "representationId", diagramId,
                "diagramElementIds", List.of(nodeId));
        var result = this.paletteQueryRunner.run(variables);
        List<String> labels = JsonPath.read(result.data(), "$.data.viewer.editingContext.representation.description.palette.quickAccessTools[*].label");
        assertThat(labels).contains(toolName);
        int toolIndex = labels.indexOf(toolName);
        List<String> ids = JsonPath.read(result.data(), "$.data.viewer.editingContext.representation.description.palette.quickAccessTools[*].id");
        return ids.get(toolIndex);
    }

    @GivenSysONServer({ GeneralViewEmptyTestProjectData.SCRIPT_PATH })
    @Test
    public void dropLibraryPackageFromExplorerOnDiagram() {
        var input = new CreateChildInput(
                UUID.randomUUID(),
                GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                GeneralViewEmptyTestProjectData.SemanticIds.PACKAGE_1_ID,
                SysMLv2EditService.ID_PREFIX + "LibraryPackage");
        var result = this.createChildMutationRunner.run(input);

        String typename = JsonPath.read(result.data(), "$.data.createChild.__typename");
        assertThat(typename).isEqualTo(CreateChildSuccessPayload.class.getSimpleName());

        String objectId = JsonPath.read(result.data(), "$.data.createChild.object.id");
        assertThat(objectId).isNotBlank();

        String objectLabel = JsonPath.read(result.data(), "$.data.createChild.object.label");
        assertThat(objectLabel).isEqualTo("LibraryPackage1");

        var flux = this.givenSubscriptionToEmptyDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        Runnable dropFromExplorerRunnable = () -> {
            this.dropFromExplorerTester.dropFromExplorerOnDiagram(GeneralViewEmptyTestProjectData.EDITING_CONTEXT, diagram, objectId);
        };

        Consumer<Object> updatedDiagramConsumer = assertRefreshedDiagramThat(newDiagram -> {
            assertThat(newDiagram.getNodes()).hasSize(1);
            Node packageDiagramNode = newDiagram.getNodes().get(0);
            String packageNodeDescriptionId = diagramDescriptionIdProvider.getNodeDescriptionId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage()));
            assertThat(packageDiagramNode).hasDescriptionId(packageNodeDescriptionId);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(dropFromExplorerRunnable)
                .consumeNextWith(updatedDiagramConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    private String getSemanticElementWithTargetObjectLabel(IEditingContext editingContext, String targetObjectLabel) {
        Object semanticRootObject = this.objectSearchService.getObject(editingContext, GeneralViewAddExistingElementsTestProjectData.SemanticIds.PACKAGE_1_ID).orElse(null);
        assertThat(semanticRootObject).isInstanceOf(Package.class);
        Package rootPackage = (Package) semanticRootObject;
        Optional<Element> optPartUsage = rootPackage.getOwnedMember().stream().filter(m -> Objects.equals(m.getName(), targetObjectLabel)).findFirst();
        assertThat(optPartUsage).isPresent();
        String partUsageId = this.identityService.getId(optPartUsage.get());
        assertThat(partUsageId).isNotNull();
        return partUsageId;
    }
}
