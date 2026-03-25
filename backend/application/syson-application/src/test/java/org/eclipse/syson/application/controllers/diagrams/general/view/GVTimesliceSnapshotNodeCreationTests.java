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
package org.eclipse.syson.application.controllers.diagrams.general.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;
import static org.eclipse.sirius.components.diagrams.tests.DiagramEventPayloadConsumer.assertRefreshedDiagramThat;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controller.editingcontext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.DiagramCheckerService;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.controllers.utils.TestNameGenerator;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.NodeCreationTestsService;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.OccurrenceUsage;
import org.eclipse.syson.sysml.PortionKind;
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

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Tests the creation of timeslice and snapshot nodes in the General View diagram.
 *
 * @author gcoutable
 */
@Transactional
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVTimesliceSnapshotNodeCreationTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private ToolTester nodeCreationTester;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private DiagramComparator diagramComparator;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private IIdentityService identityService;

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram(String editingContextId, String diagramId) {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(), editingContextId, diagramId);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    private static Stream<Arguments> portionKingNodeParameters() {
        return Stream.of(
                Arguments.of(SysmlPackage.eINSTANCE.getOccurrenceUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.OCCURRENCE_USAGE_ID, "occurrence", 2, 2, false, SysmlPackage.eINSTANCE.getOccurrenceUsage()),
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.ITEM_USAGE_ID, "item", 2, 1, false, SysmlPackage.eINSTANCE.getOccurrenceUsage()),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.PART_USAGE_ID, "part", 2, 1, false, SysmlPackage.eINSTANCE.getOccurrenceUsage()),
                Arguments.of(SysmlPackage.eINSTANCE.getOccurrenceUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.OCCURRENCE_USAGE_ID, "occurrence", 2, 2, true, SysmlPackage.eINSTANCE.getOccurrenceUsage()),
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.ITEM_USAGE_ID, "item", 2, 1, true, SysmlPackage.eINSTANCE.getOccurrenceUsage()),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.PART_USAGE_ID, "part", 2, 1, true, SysmlPackage.eINSTANCE.getOccurrenceUsage()),
                Arguments.of(SysmlPackage.eINSTANCE.getOccurrenceUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.OCCURRENCE_USAGE_ID, "occurrence", 11, 1, true, SysmlPackage.eINSTANCE.getPartUsage()),
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.ITEM_USAGE_ID, "item", 11, 1, true, SysmlPackage.eINSTANCE.getPartUsage()),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.PART_USAGE_ID, "part", 11, 2, true, SysmlPackage.eINSTANCE.getPartUsage()),
                Arguments.of(SysmlPackage.eINSTANCE.getOccurrenceUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.OCCURRENCE_USAGE_ID, "occurrence", 4, 1, true, SysmlPackage.eINSTANCE.getItemUsage()),
                Arguments.of(SysmlPackage.eINSTANCE.getItemUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.ITEM_USAGE_ID, "item", 4, 1, true, SysmlPackage.eINSTANCE.getItemUsage()),
                Arguments.of(SysmlPackage.eINSTANCE.getPartUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.PART_USAGE_ID, "part", 4, 1, true, SysmlPackage.eINSTANCE.getItemUsage())
        ).map(TestNameGenerator::namedArguments);
    }

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest()
    @MethodSource("portionKingNodeParameters")
    public void createTimesliceNodeOn(EClass parentEClass, String parentTargetObjectId, String parentLabel, int newCompartmentCount, int newNodeCount, boolean provideSelection, EClass childEClass) {
        var creationTestsService = new NodeCreationTestsService(this.nodeCreationTester, this.descriptionNameGenerator, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID);
        var semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID);
        var diagramCheckerService = new DiagramCheckerService(this.diagramComparator, this.descriptionNameGenerator);
        String selectedObject = "";
        if (provideSelection) {
            selectedObject = this.identityService.getId(childEClass);
        }

        var flux = this.givenSubscriptionToDiagram(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);


        Runnable createNodeRunnable = creationTestsService.createNodeWithSelectionDialog(diagramDescriptionIdProvider, diagram, parentEClass, parentTargetObjectId, "New Timeslice", selectedObject);
        // The created child EClass depends on the behavior of the selection dialog, here without selection
        Consumer<Object> diagramChecker = diagramCheckerService.siblingNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, childEClass, newCompartmentCount, newNodeCount);
        Consumer<Object> additionalSemanticCheck = object -> {
            if (object instanceof List<?> elements) {
                assertThat(elements).anySatisfy(element -> {
                    assertThat(element)
                            .asInstanceOf(type(OccurrenceUsage.class))
                            .satisfies(occurrenceUsage -> assertThat(occurrenceUsage.getPortionKind()).isEqualTo(PortionKind.TIMESLICE));
                });
            }
        };
        Runnable semanticCheck = semanticCheckerService.checkEditingContext(semanticCheckerService.getElementInParentSemanticChecker(parentLabel, SysmlPackage.eINSTANCE.getUsage_NestedUsage(), SysmlPackage.eINSTANCE.getOccurrenceUsage(), additionalSemanticCheck));
        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramChecker)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @ParameterizedTest()
    @MethodSource("portionKingNodeParameters")
    public void createSnapshotNodeOn(EClass parentEClass, String parentTargetObjectId, String parentLabel, int newCompartmentCount, int newNodeCount, boolean provideSelection, EClass childEClass) {
        var creationTestsService = new NodeCreationTestsService(this.nodeCreationTester, this.descriptionNameGenerator, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID);
        var semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID);
        var diagramCheckerService = new DiagramCheckerService(this.diagramComparator, this.descriptionNameGenerator);
        String selectedObject = "";
        if (provideSelection) {
            selectedObject = this.identityService.getId(childEClass);
        }

        var flux = this.givenSubscriptionToDiagram(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);

        AtomicReference<Diagram> diagram = new AtomicReference<>();
        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        Runnable createNodeRunnable = creationTestsService.createNodeWithSelectionDialog(diagramDescriptionIdProvider, diagram, parentEClass, parentTargetObjectId, "New Snapshot", selectedObject);
        // The created child EClass depends on the behavior of the selection dialog, here without selection
        Consumer<Object> diagramChecker = diagramCheckerService.siblingNodeGraphicalChecker(diagram, diagramDescriptionIdProvider, childEClass, newCompartmentCount, newNodeCount);
        Consumer<Object> additionalSemanticCheck = object -> {
            if (object instanceof List<?> elements) {
                assertThat(elements).anySatisfy(element -> {
                    assertThat(element)
                            .asInstanceOf(type(OccurrenceUsage.class))
                            .satisfies(occurrenceUsage -> assertThat(occurrenceUsage.getPortionKind()).isEqualTo(PortionKind.SNAPSHOT));
                });
            }
        };
        Runnable semanticCheck = semanticCheckerService.checkEditingContext(semanticCheckerService.getElementInParentSemanticChecker(parentLabel, SysmlPackage.eINSTANCE.getUsage_NestedUsage(), SysmlPackage.eINSTANCE.getOccurrenceUsage(), additionalSemanticCheck));
        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramChecker)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

}
