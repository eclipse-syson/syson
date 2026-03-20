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

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controller.editingcontext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckNodeInCompartment;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.InterconnectionViewPartDefinitionCompartmentRevealTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.helper.EMFUtils;
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
 * Tests the compartment reveal behavior of a PartDefinition in an Interconnection View diagram.
 *
 * @author gcoutable
 */
@Transactional
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PartDefinitionCompartmentRevealTests extends AbstractIntegrationTests {

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
    private DiagramComparator diagramComparator;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private ToolTester toolTester;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram(String diagramId) {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(), InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.EDITING_CONTEXT_ID, diagramId);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN an IV diagram with a PartDefinition without any compartment revealed, WHEN using the tool to create a PartUsage from the PartDefinition, THEN only the interconnection compartment is visible and it contains the created PartUsage")
    @GivenSysONServer({ InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SCRIPT_PATH })
    @Test
    public void testWithoutCompartmentDisplayed() {
        SemanticCheckerService semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.EDITING_CONTEXT_ID,
                InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SemanticIds.PART_DEFINITION_ROOT_ELEMENT_ID);
        var flux = this.givenSubscriptionToDiagram(InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.GraphicalIds.NO_COMPARTMENT_DISPLAYED_DIAGRAM_ID);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(initialDiagram -> {
            var initialPartDefinition = initialDiagram.getNodes()
                    .stream()
                    .filter(node -> InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SemanticIds.PART_DEFINITION_ROOT_ELEMENT_ID.equals(node.getTargetObjectId()))
                    .findFirst();

            assertThat(initialPartDefinition).isPresent();
            assertThat(initialPartDefinition.get().getChildNodes())
                    .allMatch(node -> node.getState().equals(ViewModifier.Hidden));

            diagram.set(initialDiagram);
        });

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);
        var parentEClass = SysmlPackage.eINSTANCE.getPartDefinition();
        String creationToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(parentEClass), "New Part");

        Runnable createPartUsage = () -> this.toolTester.invokeTool(InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SemanticIds.PART_DEFINITION_ROOT_ELEMENT_ID,
                creationToolId);

        Consumer<Object> checkCompartments = assertRefreshedDiagramThat(diagramRefreshed -> {
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withTargetObjectId(InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SemanticIds.PART_DEFINITION_ROOT_ELEMENT_ID)
                    .withCompartmentName("interconnection")
                    .hasCompartmentCount(12)
                    .hasNodeDescriptionName(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()))
                    .isRevealed()
                    .check(diagram.get(), diagramRefreshed);

            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withTargetObjectId(InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SemanticIds.PART_DEFINITION_ROOT_ELEMENT_ID)
                    .withCompartmentName("parts")
                    .hasCompartmentCount(0)
                    .hasNodeDescriptionName(this.descriptionNameGenerator.getCompartmentItemName(parentEClass, SysmlPackage.eINSTANCE.getDefinition_OwnedPart()))
                    .isHidden()
                    .check(diagram.get(), diagramRefreshed);
        });

        Runnable semanticCheck = semanticCheckerService.checkElement(PartDefinition.class, () -> InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SemanticIds.PART_DEFINITION_ROOT_ELEMENT_ID, partDefinition -> {
            assertThat(EMFUtils.allContainedObjectOfType(partDefinition, PartUsage.class).count()).isEqualTo(1);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createPartUsage)
                .consumeNextWith(checkCompartments)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN an IV diagram with a PartDefinition with the 'interconnection' compartment revealed, WHEN using the tool to create a PartUsage from the PartDefinition, THEN only the interconnection compartment is visible and it contains the created PartUsage")
    @GivenSysONServer({ InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SCRIPT_PATH })
    @Test
    public void testWithInterconnectionCompartmentDisplayed() {
        SemanticCheckerService semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.EDITING_CONTEXT_ID,
                InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SemanticIds.PART_DEFINITION_ROOT_ELEMENT_ID);
        var flux = this.givenSubscriptionToDiagram(InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.GraphicalIds.INTERCONNECTION_COMPARTMENT_DISPLAYED_DIAGRAM_ID);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(initialDiagram -> {
            var initialPartDefinition = initialDiagram.getNodes()
                    .stream()
                    .filter(node -> InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SemanticIds.PART_DEFINITION_ROOT_ELEMENT_ID.equals(node.getTargetObjectId()))
                    .findFirst();

            assertThat(initialPartDefinition).isPresent();
            assertThat(initialPartDefinition.get().getChildNodes())
                    .allMatch(node -> {
                        if ("interconnection".equals(node.getInsideLabel().getText())) {
                            return node.getState().equals(ViewModifier.Normal);
                        } else {
                            return node.getState().equals(ViewModifier.Hidden);
                        }
                    });

            diagram.set(initialDiagram);
        });

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);
        var parentEClass = SysmlPackage.eINSTANCE.getPartDefinition();
        String creationToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartDefinition()), "New Part");

        Runnable createPartUsage = () -> this.toolTester.invokeTool(InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SemanticIds.PART_DEFINITION_ROOT_ELEMENT_ID,
                creationToolId);

        Consumer<Object> checkCompartments = assertRefreshedDiagramThat(diagramRefreshed -> {
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withTargetObjectId(InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SemanticIds.PART_DEFINITION_ROOT_ELEMENT_ID)
                    .withCompartmentName("interconnection")
                    .hasCompartmentCount(12)
                    .hasNodeDescriptionName(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()))
                    .isRevealed()
                    .check(diagram.get(), diagramRefreshed);

            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withTargetObjectId(InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SemanticIds.PART_DEFINITION_ROOT_ELEMENT_ID)
                    .withCompartmentName("parts")
                    .hasCompartmentCount(0)
                    .hasNodeDescriptionName(this.descriptionNameGenerator.getCompartmentItemName(parentEClass, SysmlPackage.eINSTANCE.getDefinition_OwnedPart()))
                    .isHidden()
                    .check(diagram.get(), diagramRefreshed);
        });

        Runnable semanticCheck = semanticCheckerService.checkElement(PartDefinition.class, () -> InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SemanticIds.PART_DEFINITION_ROOT_ELEMENT_ID, partDefinition -> {
            assertThat(EMFUtils.allContainedObjectOfType(partDefinition, PartUsage.class).count()).isEqualTo(1);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createPartUsage)
                .consumeNextWith(checkCompartments)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN an IV diagram with a PartDefinition with the 'parts' compartment revealed, WHEN using the tool to create a PartUsage from the PartDefinition, THEN only the 'parts' compartment is visible and it contains the created PartUsage")
    @GivenSysONServer({ InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SCRIPT_PATH })
    @Test
    public void testWithPartsCompartmentDisplayed() {
        SemanticCheckerService semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.EDITING_CONTEXT_ID,
                InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SemanticIds.PART_DEFINITION_ROOT_ELEMENT_ID);
        var flux = this.givenSubscriptionToDiagram(InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.GraphicalIds.PARTS_COMPARTMENT_DISPLAYED_DIAGRAM_ID);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(initialDiagram -> {
            var initialPartDefinition = initialDiagram.getNodes()
                    .stream()
                    .filter(node -> InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SemanticIds.PART_DEFINITION_ROOT_ELEMENT_ID.equals(node.getTargetObjectId()))
                    .findFirst();

            assertThat(initialPartDefinition).isPresent();
            assertThat(initialPartDefinition.get().getChildNodes())
                    .allMatch(node -> {
                        if ("parts".equals(node.getInsideLabel().getText())) {
                            return node.getState().equals(ViewModifier.Normal);
                        } else {
                            return node.getState().equals(ViewModifier.Hidden);
                        }
                    });

            diagram.set(initialDiagram);
        });

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);
        var parentEClass = SysmlPackage.eINSTANCE.getPartDefinition();
        String creationToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartDefinition()), "New Part");

        Runnable createPartUsage = () -> this.toolTester.invokeTool(InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SemanticIds.PART_DEFINITION_ROOT_ELEMENT_ID,
                creationToolId);

        Consumer<Object> checkCompartments = assertRefreshedDiagramThat(diagramRefreshed -> {
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withTargetObjectId(InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SemanticIds.PART_DEFINITION_ROOT_ELEMENT_ID)
                    .withCompartmentName("interconnection")
                    .hasCompartmentCount(12)
                    .hasNodeDescriptionName(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()))
                    .isHidden()
                    .check(diagram.get(), diagramRefreshed);

            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withTargetObjectId(InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SemanticIds.PART_DEFINITION_ROOT_ELEMENT_ID)
                    .withCompartmentName("parts")
                    .hasCompartmentCount(0)
                    .hasNodeDescriptionName(this.descriptionNameGenerator.getCompartmentItemName(parentEClass, SysmlPackage.eINSTANCE.getDefinition_OwnedPart()))
                    .isRevealed()
                    .check(diagram.get(), diagramRefreshed);
        });

        Runnable semanticCheck = semanticCheckerService.checkElement(PartDefinition.class, () -> InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SemanticIds.PART_DEFINITION_ROOT_ELEMENT_ID, partDefinition -> {
            assertThat(EMFUtils.allContainedObjectOfType(partDefinition, PartUsage.class).count()).isEqualTo(1);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createPartUsage)
                .consumeNextWith(checkCompartments)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN an IV diagram with a PartDefinition with both the 'parts' and 'interconnection' compartments revealed, WHEN using the tool to create a PartUsage from the PartDefinition, THEN both compartments are visible and they both contains the created PartUsage")
    @GivenSysONServer({ InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SCRIPT_PATH })
    @Test
    public void testWithInterconnectionAndPartsCompartmentsDisplayed() {
        SemanticCheckerService semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.EDITING_CONTEXT_ID,
                InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SemanticIds.PART_DEFINITION_ROOT_ELEMENT_ID);
        var flux = this.givenSubscriptionToDiagram(InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.GraphicalIds.INTERCONNECTION_AND_PARTS_COMPARTMENTS_DISPLAYED_DIAGRAM_ID);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(initialDiagram -> {
            var initialPartDefinition = initialDiagram.getNodes()
                    .stream()
                    .filter(node -> InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SemanticIds.PART_DEFINITION_ROOT_ELEMENT_ID.equals(node.getTargetObjectId()))
                    .findFirst();

            assertThat(initialPartDefinition).isPresent();
            assertThat(initialPartDefinition.get().getChildNodes())
                    .allMatch(node -> {
                        var compartmentLabel = node.getInsideLabel().getText();
                        if ("parts".equals(compartmentLabel) || "interconnection".equals(compartmentLabel)) {
                            return node.getState().equals(ViewModifier.Normal);
                        } else {
                            return node.getState().equals(ViewModifier.Hidden);
                        }
                    });

            diagram.set(initialDiagram);
        });

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);
        var parentEClass = SysmlPackage.eINSTANCE.getPartDefinition();
        String creationToolId = diagramDescriptionIdProvider.getNodeToolId(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartDefinition()), "New Part");

        Runnable createPartUsage = () -> this.toolTester.invokeTool(InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.EDITING_CONTEXT_ID,
                diagram,
                InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SemanticIds.PART_DEFINITION_ROOT_ELEMENT_ID,
                creationToolId);

        Consumer<Object> checkCompartments = assertRefreshedDiagramThat(diagramRefreshed -> {
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withTargetObjectId(InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SemanticIds.PART_DEFINITION_ROOT_ELEMENT_ID)
                    .withCompartmentName("interconnection")
                    .hasCompartmentCount(12)
                    .hasNodeDescriptionName(this.descriptionNameGenerator.getNodeName(SysmlPackage.eINSTANCE.getPartUsage()))
                    .isRevealed()
                    .check(diagram.get(), diagramRefreshed);

            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withTargetObjectId(InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SemanticIds.PART_DEFINITION_ROOT_ELEMENT_ID)
                    .withCompartmentName("parts")
                    .hasCompartmentCount(0)
                    .hasNodeDescriptionName(this.descriptionNameGenerator.getCompartmentItemName(parentEClass, SysmlPackage.eINSTANCE.getDefinition_OwnedPart()))
                    .isRevealed()
                    .check(diagram.get(), diagramRefreshed);
        });

        Runnable semanticCheck = semanticCheckerService.checkElement(PartDefinition.class, () -> InterconnectionViewPartDefinitionCompartmentRevealTestProjectData.SemanticIds.PART_DEFINITION_ROOT_ELEMENT_ID, partDefinition -> {
            assertThat(EMFUtils.allContainedObjectOfType(partDefinition, PartUsage.class).count()).isEqualTo(1);
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createPartUsage)
                .consumeNextWith(checkCompartments)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
