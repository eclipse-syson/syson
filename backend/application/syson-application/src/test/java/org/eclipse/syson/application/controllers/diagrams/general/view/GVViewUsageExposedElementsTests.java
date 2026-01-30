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
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.api.ChangeDescription;
import org.eclipse.sirius.components.collaborative.api.ChangeKind;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramLayoutDataInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.LayoutDiagramInput;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IInput;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.tests.graphql.LayoutDiagramMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionInput;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionRunner;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.ViewUsageExposedElementsTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.sysml.helper.LabelConstants;
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
 * Tests the synchronization between the diagram nodes and the ViewUsage#exposedElements reference.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVViewUsageExposedElementsTests extends AbstractIntegrationTests {

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
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private ExecuteEditingContextFunctionRunner executeEditingContextFunctionRunner;

    @Autowired
    private DiagramComparator diagramComparator;

    @Autowired
    private LayoutDiagramMutationRunner layoutDiagramMutationRunner;

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(), ViewUsageExposedElementsTestProjectData.EDITING_CONTEXT_ID, ViewUsageExposedElementsTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a GV diagram on a ViewUsage, WHEN New Part tool is executed, THEN a the ViewUsage#exposedElements is updated with the new Part")
    @GivenSysONServer({ ViewUsageExposedElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void newPartToolShouldUpdateExposedElements() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(ViewUsageExposedElementsTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        String creationToolId = diagramDescriptionIdProvider.getDiagramCreationToolId(this.descriptionNameGenerator.getCreationToolName(SysmlPackage.eINSTANCE.getPartUsage()));
        assertThat(creationToolId).as("The tool 'New Part' should exist on the diagram").isNotNull();

        Runnable invokeToolCheck = () -> this.toolTester.invokeTool(ViewUsageExposedElementsTestProjectData.EDITING_CONTEXT_ID, diagram, creationToolId);

        Runnable semanticChecker = this.semanticRunnableFactory.createRunnable(ViewUsageExposedElementsTestProjectData.EDITING_CONTEXT_ID,
                (editingContext, executeEditingContextFunctionInput) -> {
                    Object viewUsageObject = this.objectSearchService.getObject(editingContext, ViewUsageExposedElementsTestProjectData.SemanticIds.VIEW_USAGE_GV_ELEMENT_ID).orElse(null);
                    assertThat(viewUsageObject).isInstanceOf(ViewUsage.class);
                    ViewUsage viewUsage = (ViewUsage) viewUsageObject;

                    assertEquals(1, viewUsage.getExposedElement().size());
                    assertThat(viewUsage.getExposedElement().get(0)).isInstanceOf(PartUsage.class);
                    assertThat(viewUsage.getExposedElement().get(0)).extracting(Element::getDeclaredName).isEqualTo("part2");

                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(invokeToolCheck)
                .consumeNextWith(payload -> { })
                .then(semanticChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a GV diagram on a ViewUsage, WHEN Add existing element(s) tool is executed, THEN the ViewUsage#exposedElements is updated with partA")
    @GivenSysONServer({ ViewUsageExposedElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void addExistingElementsToolShouldUpdateExposedElements() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(ViewUsageExposedElementsTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        String creationToolId = diagramDescriptionIdProvider.getDiagramCreationToolId("Add existing elements");
        assertThat(creationToolId).as("The tool 'Add existing elements' should exist on the diagram").isNotNull();
        Runnable invokeToolCheck = () -> this.toolTester.invokeTool(ViewUsageExposedElementsTestProjectData.EDITING_CONTEXT_ID, diagram, creationToolId);

        Runnable semanticChecker = this.semanticRunnableFactory.createRunnable(ViewUsageExposedElementsTestProjectData.EDITING_CONTEXT_ID,
                (editingContext, executeEditingContextFunctionInput) -> {
                    Object viewUsageObject = this.objectSearchService.getObject(editingContext, ViewUsageExposedElementsTestProjectData.SemanticIds.VIEW_USAGE_GV_ELEMENT_ID).orElse(null);
                    assertThat(viewUsageObject).isInstanceOf(ViewUsage.class);
                    ViewUsage viewUsage = (ViewUsage) viewUsageObject;

                    assertEquals(1, viewUsage.getExposedElement().size());
                    assertThat(viewUsage.getExposedElement().get(0)).isInstanceOf(PartUsage.class);
                    assertThat(viewUsage.getExposedElement().get(0)).extracting(Element::getElementId).isEqualTo(ViewUsageExposedElementsTestProjectData.SemanticIds.PART_A_ELEMENT_ID);

                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(invokeToolCheck)
                .consumeNextWith(payload -> { })
                .then(semanticChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a GV diagram on a ViewUsage, WHEN Add existing element(s) (recursive) tool is executed, THEN the ViewUsage#exposedElements is updated with partA and partB")
    @GivenSysONServer({ ViewUsageExposedElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void addExistingElementsRecursivelyToolShouldUpdateExposedElements() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(ViewUsageExposedElementsTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        String creationToolId = diagramDescriptionIdProvider.getDiagramCreationToolId("Add existing elements (recursive)");
        assertThat(creationToolId).as("The tool 'Add existing elements (recursive)' should exist on the diagram").isNotNull();
        Runnable invokeToolCheck = () -> this.toolTester.invokeTool(ViewUsageExposedElementsTestProjectData.EDITING_CONTEXT_ID, diagram, creationToolId);

        Runnable semanticChecker = this.semanticRunnableFactory.createRunnable(ViewUsageExposedElementsTestProjectData.EDITING_CONTEXT_ID,
                (editingContext, executeEditingContextFunctionInput) -> {
                    Object viewUsageObject = this.objectSearchService.getObject(editingContext, ViewUsageExposedElementsTestProjectData.SemanticIds.VIEW_USAGE_GV_ELEMENT_ID).orElse(null);
                    assertThat(viewUsageObject).isInstanceOf(ViewUsage.class);
                    ViewUsage viewUsage = (ViewUsage) viewUsageObject;

                    assertEquals(2, viewUsage.getExposedElement().size());
                    assertThat(viewUsage.getExposedElement().get(0)).isInstanceOf(PartUsage.class);
                    assertThat(viewUsage.getExposedElement().get(0)).extracting(Element::getElementId).isEqualTo(ViewUsageExposedElementsTestProjectData.SemanticIds.PART_A_ELEMENT_ID);
                    assertThat(viewUsage.getExposedElement().get(1)).isInstanceOf(PartUsage.class);
                    assertThat(viewUsage.getExposedElement().get(1)).extracting(Element::getElementId).isEqualTo(ViewUsageExposedElementsTestProjectData.SemanticIds.PART_B_ELEMENT_ID);

                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(invokeToolCheck)
                .consumeNextWith(payload -> { })
                .then(semanticChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a GV diagram on a ViewUsage, WHEN the ViewUsage#exposedElements is updated with partA, THEN the GV diagram is updated with a new partA node.")
    @GivenSysONServer({ ViewUsageExposedElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void updateExposedElementsShouldUpdateTheDiagram() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        BiFunction<IEditingContext, IInput, IPayload> function = (editingContext, executeEditingContextFunctionInput) -> {
            Object viewUsageObject = this.objectSearchService.getObject(editingContext, ViewUsageExposedElementsTestProjectData.SemanticIds.VIEW_USAGE_GV_ELEMENT_ID).orElse(null);
            assertThat(viewUsageObject).isInstanceOf(ViewUsage.class);
            ViewUsage viewUsage = (ViewUsage) viewUsageObject;
            Object partAObject = this.objectSearchService.getObject(editingContext, ViewUsageExposedElementsTestProjectData.SemanticIds.PART_A_ELEMENT_ID).orElse(null);
            assertThat(partAObject).isInstanceOf(PartUsage.class);
            PartUsage partA = (PartUsage) partAObject;
            var membershipExpose = SysmlFactory.eINSTANCE.createMembershipExpose();
            membershipExpose.setImportedMembership(partA.getOwningMembership());
            viewUsage.getOwnedRelationship().add(membershipExpose);
            return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
        };

        Runnable modifyExposedElements = () -> {
            UUID functionInputId = UUID.randomUUID();
            ChangeDescription changeDescription = new ChangeDescription(ChangeKind.SEMANTIC_CHANGE, ViewUsageExposedElementsTestProjectData.EDITING_CONTEXT_ID, () -> functionInputId);
            var modifyExposedElementsPayload = this.executeEditingContextFunctionRunner
                    .execute(new ExecuteEditingContextFunctionInput(functionInputId, ViewUsageExposedElementsTestProjectData.EDITING_CONTEXT_ID, function, changeDescription))
                    .block();
            assertThat(modifyExposedElementsPayload).isInstanceOf(ExecuteEditingContextFunctionSuccessPayload.class);
        };

        var currentRevisionId = new AtomicReference<UUID>();
        Consumer<Object> updatedDiagramContentConsumer = payload -> Optional.of(payload)
                .filter(DiagramRefreshedEventPayload.class::isInstance)
                .map(DiagramRefreshedEventPayload.class::cast)
                .map(diagramPayload -> {
                    currentRevisionId.set(diagramPayload.id());
                    return diagramPayload.diagram();
                });

        Runnable newDiagramLayout = () -> {
            var layoutData = new DiagramLayoutDataInput(List.of(), List.of(), List.of());
            var layoutInput = new LayoutDiagramInput(currentRevisionId.get(), ViewUsageExposedElementsTestProjectData.EDITING_CONTEXT_ID, diagram.get().getId(),
                    DiagramRefreshedEventPayload.CAUSE_REFRESH, layoutData);
            this.layoutDiagramMutationRunner.run(layoutInput);
        };

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(13) // One node and 12 compartments
                    .check(initialDiagram, newDiagram);
            Node newNode = this.diagramComparator.newNodes(initialDiagram, newDiagram).get(0);
            assertEquals(ViewUsageExposedElementsTestProjectData.SemanticIds.PART_A_SIRIUS_ID, newNode.getTargetObjectId());
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(modifyExposedElements)
                .consumeNextWith(updatedDiagramContentConsumer)
                .then(newDiagramLayout)
                .consumeNextWith(diagramCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a GV diagram on a ViewUsage, WHEN an element is created inside a Package, THEN the element should only be visible inside the Package")
    @GivenSysONServer({ ViewUsageExposedElementsTestProjectData.SCRIPT_PATH })
    @Test
    public void exposePackageChildShouldDisplayChildOnlyinPAckage() {
        var flux = this.givenSubscriptionToDiagram();

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(ViewUsageExposedElementsTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        var newPackageToolId = diagramDescriptionIdProvider.getDiagramCreationToolId("New Package");
        assertThat(newPackageToolId).as("The tool 'New Package' should exist on the diagram").isNotNull();
        var newInterfaceToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPackage()), "New Interface");
        assertThat(newInterfaceToolId).as("The tool 'New Interface' should exist on the Package").isNotNull();

        Runnable newPackageTool = () -> this.toolTester.invokeTool(ViewUsageExposedElementsTestProjectData.EDITING_CONTEXT_ID, diagram, newPackageToolId);

        var packageNodeId = new AtomicReference<String>();

        Consumer<Object> updatedDiagramWithPackage = assertRefreshedDiagramThat(diag -> {
            int diagramRootNodesCount = diag.getNodes().size();
            assertThat(diagramRootNodesCount).isEqualTo(1);
            var packageNode = new DiagramNavigator(diag).nodeWithLabel("Package1").getNode();
            assertThat(packageNode).isNotNull();
            assertThat(packageNode.getChildNodes()).hasSize(0);
            packageNodeId.set(packageNode.getId());
        });

        Runnable newInterfaceTool = () -> this.toolTester.invokeTool(ViewUsageExposedElementsTestProjectData.EDITING_CONTEXT_ID, diagram.get().getId(), packageNodeId.get(), newInterfaceToolId,
                List.of());

        Consumer<Object> updatedDiagramWithInterface = assertRefreshedDiagramThat(diag -> {
            int diagramRootNodesCount = diag.getNodes().size();
            assertThat(diagramRootNodesCount).isEqualTo(1);
            var packageNode = new DiagramNavigator(diag).nodeWithLabel("Package1").getNode();
            assertThat(packageNode.getChildNodes()).hasSize(1);
            var interfaceNode = new DiagramNavigator(diag).nodeWithLabel("Package1")
                    .childNodeWithLabel(LabelConstants.OPEN_QUOTE + "interface" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "interface1").getNode();
            assertThat(interfaceNode).isNotNull();
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(newPackageTool)
                .consumeNextWith(updatedDiagramWithPackage)
                .then(newInterfaceTool)
                .consumeNextWith(updatedDiagramWithInterface)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
