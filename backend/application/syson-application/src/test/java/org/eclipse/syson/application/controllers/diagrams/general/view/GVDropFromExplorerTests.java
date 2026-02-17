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
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.SysONTestsProperties;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckNodeOnDiagram;
import org.eclipse.syson.application.controllers.diagrams.testers.DropFromExplorerTester;
import org.eclipse.syson.application.data.GeneralViewAddExistingElementsTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.standard.diagrams.view.nodes.GeneralViewEmptyDiagramNodeDescriptionProvider;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = { SysONTestsProperties.NO_DEFAULT_LIBRARIES_PROPERTY })
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

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewAddExistingElementsTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewAddExistingElementsTestProjectData.GraphicalIds.DIAGRAM_ID);
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
            assertThat(diagram.get().getNodes()).hasSize(1);
            Node emptyDiagramNode = diagram.get().getNodes().get(0);
            String emptyDiagramNodeDescriptionId = diagramDescriptionIdProvider.getNodeDescriptionId(GeneralViewEmptyDiagramNodeDescriptionProvider.NAME);
            // Ensure the existing node is the "empty diagram" one.
            assertThat(emptyDiagramNode).hasDescriptionId(emptyDiagramNodeDescriptionId);
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

    @GivenSysONServer({ GeneralViewAddExistingElementsTestProjectData.SCRIPT_PATH })
    @Test
    @DisplayName("GIVEN an Element with no declared name but having a declared short name, WHEN drag and dropping this element on a diagram, THEN graphical node should be created")
    public void dropFromExplorerShortNameOnlyOnEmptyDiagram() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewAddExistingElementsTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        Runnable dropFromExplorerRunnable = () -> {
            assertThat(diagram.get().getNodes()).hasSize(1);
            Node emptyDiagramNode = diagram.get().getNodes().get(0);
            String emptyDiagramNodeDescriptionId = diagramDescriptionIdProvider.getNodeDescriptionId(GeneralViewEmptyDiagramNodeDescriptionProvider.NAME);
            // Ensure the existing node is the "empty diagram" one.
            assertThat(emptyDiagramNode).hasDescriptionId(emptyDiagramNodeDescriptionId);
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
    public void dropFromExplorerOnEmptyDiagramNode() {
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
            assertThat(diagram.get().getNodes()).hasSize(1);
            Node emptyDiagramNode = diagram.get().getNodes().get(0);
            String emptyDiagramNodeDescriptionId = diagramDescriptionIdProvider.getNodeDescriptionId(GeneralViewEmptyDiagramNodeDescriptionProvider.NAME);
            // Ensure the existing node is the "empty diagram" one.
            assertThat(emptyDiagramNode).hasDescriptionId(emptyDiagramNodeDescriptionId);
            this.dropFromExplorerTester.dropFromExplorer(GeneralViewAddExistingElementsTestProjectData.EDITING_CONTEXT_ID, diagram, emptyDiagramNode.getId(), semanticElementId.get());
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
        });

        StepVerifier.create(flux)
                .then(semanticChecker)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(dropFromExplorerRunnable)
                .consumeNextWith(updatedDiagramConsumer)
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

        StepVerifier.create(flux)
                .then(initialState)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(() -> {
                    var result = this.dropFromExplorerTester.dropFromExplorer(GeneralViewAddExistingElementsTestProjectData.EDITING_CONTEXT_ID, diagram,
                            null, semanticElementId.get());
                    List<Object> messages = JsonPath.read(result.data(), "$.data.dropOnDiagram.messages[*]");
                    assertThat(messages).isEmpty();
                })
                .consumeNextWith(payload -> { })
                .then(hasBeenExposed)
                .then(() -> {
                    var result = this.dropFromExplorerTester.dropFromExplorer(GeneralViewAddExistingElementsTestProjectData.EDITING_CONTEXT_ID, diagram,
                            null, semanticElementId.get());
                    List<Object> messages = JsonPath.read(result.data(), "$.data.dropOnDiagram.messages[*]");
                    assertThat(messages).as("We should receive at least one message when dropping an already visible element").hasSizeGreaterThanOrEqualTo(1);
                    String messageBody = JsonPath.read(result.data(), "$.data.dropOnDiagram.messages[0].body");
                    assertThat(messageBody).isEqualTo("The element part1 is already visible in its parent General View");
                    String messageLevel = JsonPath.read(result.data(), "$.data.dropOnDiagram.messages[0].level");
                    assertThat(messageLevel).isEqualTo(MessageLevel.WARNING.toString());
                })
                .consumeNextWith(payload -> { })
                .then(hasNotBeenExposedAgain)
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
