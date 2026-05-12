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

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DropNodesInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.HideDiagramElementInput;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.layoutdata.Position;
import org.eclipse.sirius.components.diagrams.tests.graphql.DropNodesMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.graphql.HideDiagramElementMutationRunner;
import org.eclipse.sirius.components.diagrams.tests.navigation.DiagramNavigator;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.NodeCreationTestsService;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.SysmlPackage;
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
 * Tests the drag and drop of nodes inside the General View diagram.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVDropFromDiagramTests extends AbstractIntegrationTests {

    private static final String SUBJECT = "subject";

    private static final String OBJECTIVE = "objective";

    private static final String DATA_DROP_NODES_TYPENAME = "$.data.dropNodes.__typename";

    private final IDescriptionNameGenerator descriptionNameGenerator = new SDVDescriptionNameGenerator();

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private DropNodesMutationRunner dropNodesMutationRunner;

    @Autowired
    private HideDiagramElementMutationRunner hideDiagramElementMutationRunner;

    @Autowired
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private ToolTester toolTester;

    private Flux<DiagramRefreshedEventPayload> givenSubscriptionToDiagram() {
        var diagramEventInput = new DiagramEventInput(UUID.randomUUID(),
                GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID);
        return this.givenDiagramSubscription.subscribe(diagramEventInput);
    }

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a diagram with some nodes, WHEN a node is dropped in another one, THEN the diagram is updated")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void dropPartFromDiagramToPackageThenFromPackageToPart() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramId = new AtomicReference<String>();
        var packageNodeId = new AtomicReference<String>();
        var partNodeId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            diagramId.set(diagram.getId());

            var packageNode = new DiagramNavigator(diagram).nodeWithLabel("Package").getNode();
            packageNodeId.set(packageNode.getId());

            var partNode = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "ref part" + LabelConstants.CLOSE_QUOTE + "\npart").getNode();
            partNodeId.set(partNode.getId());

            assertThat(packageNode.getChildNodes()).hasSize(0);
        });

        Runnable dropPartNodeFromDiagramToPackage = () -> {
            var input = new DropNodesInput(
                    UUID.randomUUID(),
                    GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID.toString(),
                    diagramId.get(),
                    List.of(partNodeId.get()),
                    packageNodeId.get(),
                    List.of(new Position(0, 0)));
            var result = this.dropNodesMutationRunner.run(input);
            String typename = JsonPath.read(result.data(), DATA_DROP_NODES_TYPENAME);
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentConsumerAfterFirstDrop = assertRefreshedDiagramThat(diagram -> {
            var packageNode = new DiagramNavigator(diagram).nodeWithLabel("Package").getNode();

            var partNode = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "ref part" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "part").getNode();
            partNodeId.set(partNode.getId());

            assertThat(packageNode.getChildNodes()).hasSize(1);
            assertThat(packageNode.getChildNodes().get(0)).isEqualTo(partNode);
        });

        Runnable dropPartNodeFromPackageToDiagram = () -> {
            var input = new DropNodesInput(
                    UUID.randomUUID(),
                    GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID.toString(),
                    diagramId.get(),
                    List.of(partNodeId.get()),
                    diagramId.get(),
                    List.of(new Position(0, 0)));
            var result = this.dropNodesMutationRunner.run(input);
            String typename = JsonPath.read(result.data(), DATA_DROP_NODES_TYPENAME);
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentConsumerAfterSecondDrop = assertRefreshedDiagramThat(diagram -> {
            var packageNode = new DiagramNavigator(diagram).nodeWithLabel("Package").getNode();

            var partNode = new DiagramNavigator(diagram).nodeWithLabel(LabelConstants.OPEN_QUOTE + "ref part" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + "part").getNode();

            assertThat(packageNode.getChildNodes()).hasSize(0);
            assertThat(partNode).isNotNull();
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(dropPartNodeFromDiagramToPackage)
                .consumeNextWith(updatedDiagramContentConsumerAfterFirstDrop)
                .then(dropPartNodeFromPackageToDiagram)
                .consumeNextWith(updatedDiagramContentConsumerAfterSecondDrop)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a diagram with top nodes, WHEN multiple nodes are dropped in a package and back to the diagram, THEN all of them are moved")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void dropMultipleNodesFromDiagramToPackageThenBackToDiagram() {
        var flux = this.givenSubscriptionToDiagram();

        var diagramId = new AtomicReference<String>();
        var packageNodeId = new AtomicReference<String>();
        var partNodeId = new AtomicReference<String>();
        var actionNodeId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            diagramId.set(diagram.getId());

            var diagramNavigator = new DiagramNavigator(diagram);
            var packageNode = diagramNavigator.nodeWithTargetObjectId(GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_ID).getNode();
            packageNodeId.set(packageNode.getId());

            partNodeId.set(diagramNavigator.nodeWithTargetObjectId(GeneralViewWithTopNodesTestProjectData.SemanticIds.PART_USAGE_ID).getNode().getId());
            actionNodeId.set(diagramNavigator.nodeWithTargetObjectId(GeneralViewWithTopNodesTestProjectData.SemanticIds.ACTION_USAGE_ID).getNode().getId());

            assertThat(packageNode.getChildNodes()).isEmpty();
        });

        Runnable dropNodesFromDiagramToPackage = () -> {
            var input = new DropNodesInput(
                    UUID.randomUUID(),
                    GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID.toString(),
                    diagramId.get(),
                    List.of(partNodeId.get(), actionNodeId.get()),
                    packageNodeId.get(),
                    List.of(new Position(0, 0), new Position(25, 25)));
            var result = this.dropNodesMutationRunner.run(input);
            String typename = JsonPath.read(result.data(), DATA_DROP_NODES_TYPENAME);
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentConsumerAfterFirstDrop = assertRefreshedDiagramThat(diagram -> {
            var packageNode = new DiagramNavigator(diagram).nodeWithTargetObjectId(GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_ID).getNode();

            assertThat(packageNode.getChildNodes()).hasSize(2);
            assertThat(packageNode.getChildNodes())
                    .extracting(org.eclipse.sirius.components.diagrams.Node::getTargetObjectId)
                    .containsExactlyInAnyOrder(GeneralViewWithTopNodesTestProjectData.SemanticIds.PART_USAGE_ID, GeneralViewWithTopNodesTestProjectData.SemanticIds.ACTION_USAGE_ID);

            packageNode.getChildNodes().stream()
                    .filter(child -> GeneralViewWithTopNodesTestProjectData.SemanticIds.PART_USAGE_ID.equals(child.getTargetObjectId()))
                    .findFirst()
                    .ifPresent(child -> partNodeId.set(child.getId()));
            packageNode.getChildNodes().stream()
                    .filter(child -> GeneralViewWithTopNodesTestProjectData.SemanticIds.ACTION_USAGE_ID.equals(child.getTargetObjectId()))
                    .findFirst()
                    .ifPresent(child -> actionNodeId.set(child.getId()));
        });

        Runnable dropNodesFromPackageToDiagram = () -> {
            var input = new DropNodesInput(
                    UUID.randomUUID(),
                    GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID.toString(),
                    diagramId.get(),
                    List.of(partNodeId.get(), actionNodeId.get()),
                    diagramId.get(),
                    List.of(new Position(0, 0), new Position(25, 25)));
            var result = this.dropNodesMutationRunner.run(input);
            String typename = JsonPath.read(result.data(), DATA_DROP_NODES_TYPENAME);
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentConsumerAfterSecondDrop = assertRefreshedDiagramThat(diagram -> {
            var diagramNavigator = new DiagramNavigator(diagram);
            var packageNode = diagramNavigator.nodeWithTargetObjectId(GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_ID).getNode();

            assertThat(packageNode.getChildNodes()).isEmpty();
            assertThat(diagramNavigator.nodeWithTargetObjectId(GeneralViewWithTopNodesTestProjectData.SemanticIds.PART_USAGE_ID).getNode()).isNotNull();
            assertThat(diagramNavigator.nodeWithTargetObjectId(GeneralViewWithTopNodesTestProjectData.SemanticIds.ACTION_USAGE_ID).getNode()).isNotNull();
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(dropNodesFromDiagramToPackage)
                .consumeNextWith(updatedDiagramContentConsumerAfterFirstDrop)
                .then(dropNodesFromPackageToDiagram)
                .consumeNextWith(updatedDiagramContentConsumerAfterSecondDrop)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN subject compartment items, WHEN they are dropped on subject compartments, THEN drop succeeds for case and requirement subject providers")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void dropSubjectCompartmentItemsOnSubjectCompartments() {
        var flux = this.givenSubscriptionToDiagram();
        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);
        var creationTestsService = new NodeCreationTestsService(this.toolTester, this.descriptionNameGenerator, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID);

        var diagramRef = new AtomicReference<Diagram>();
        var caseUsageSubjectCompartmentNodeId = new AtomicReference<String>();
        var useCaseUsageSubjectCompartmentNodeId = new AtomicReference<String>();
        var caseDefinitionSubjectCompartmentNodeId = new AtomicReference<String>();
        var caseUsageSubjectNodeId = new AtomicReference<String>();
        var useCaseUsageSubjectNodeId = new AtomicReference<String>();
        var caseUsageSubjectTargetObjectId = new AtomicReference<String>();
        var useCaseUsageSubjectTargetObjectId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(currentDiagram -> {
            diagramRef.set(currentDiagram);
            caseUsageSubjectCompartmentNodeId.set(this.getCompartmentNodeId(currentDiagram, GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_USAGE_ID, SUBJECT));
            useCaseUsageSubjectCompartmentNodeId.set(this.getCompartmentNodeId(currentDiagram, GeneralViewWithTopNodesTestProjectData.SemanticIds.USE_CASE_USAGE_ID, SUBJECT));
            caseDefinitionSubjectCompartmentNodeId.set(this.getCompartmentNodeId(currentDiagram, GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_DEFINITION_ID, SUBJECT));
            assertThat(caseUsageSubjectCompartmentNodeId.get()).isNotBlank();
            assertThat(useCaseUsageSubjectCompartmentNodeId.get()).isNotBlank();
            assertThat(caseDefinitionSubjectCompartmentNodeId.get()).isNotBlank();
        });

        Runnable createSubjectOnCaseUsage = creationTestsService.createNodeWithSelectionDialogWithoutSelectionProvided(diagramDescriptionIdProvider, diagramRef, SysmlPackage.eINSTANCE.getCaseUsage(),
                GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_USAGE_ID, "New Subject");

        Runnable createSubjectOnUseCaseUsage = creationTestsService.createNodeWithSelectionDialogWithoutSelectionProvided(diagramDescriptionIdProvider, diagramRef,
                SysmlPackage.eINSTANCE.getUseCaseUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.USE_CASE_USAGE_ID, "New Subject");

        Runnable revealCaseDefinitionSubjectCompartment = () -> {
            var input = new HideDiagramElementInput(
                    UUID.randomUUID(),
                    GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                    GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID,
                    Set.of(caseDefinitionSubjectCompartmentNodeId.get()),
                    false);
            var result = this.hideDiagramElementMutationRunner.run(input);
            String typename = JsonPath.read(result.data(), "$.data.hideDiagramElement.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentAfterCaseUsageCreation = assertRefreshedDiagramThat(diagram -> {
            caseUsageSubjectNodeId.set(this.getCompartmentItemNodeId(diagram, GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_USAGE_ID, SUBJECT));
            caseUsageSubjectTargetObjectId.set(this.getCompartmentItemTargetObjectId(diagram, GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_USAGE_ID, SUBJECT));
            assertThat(caseUsageSubjectNodeId.get()).isNotBlank();
            assertThat(caseUsageSubjectTargetObjectId.get()).isNotBlank();
        });

        Consumer<Object> updatedDiagramContentAfterUseCaseUsageCreation = assertRefreshedDiagramThat(diagram -> {
            useCaseUsageSubjectNodeId.set(this.getCompartmentItemNodeId(diagram, GeneralViewWithTopNodesTestProjectData.SemanticIds.USE_CASE_USAGE_ID, SUBJECT));
            useCaseUsageSubjectTargetObjectId.set(this.getCompartmentItemTargetObjectId(diagram, GeneralViewWithTopNodesTestProjectData.SemanticIds.USE_CASE_USAGE_ID, SUBJECT));
            assertThat(useCaseUsageSubjectNodeId.get()).isNotBlank();
            assertThat(useCaseUsageSubjectTargetObjectId.get()).isNotBlank();
        });

        Consumer<Object> updatedDiagramContentAfterCaseDefinitionReveal = assertRefreshedDiagramThat(diagram -> {
            var caseDefinitionSubjectCompartment = this.getCompartmentNode(diagram, GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_DEFINITION_ID, SUBJECT);
            assertThat(caseDefinitionSubjectCompartment.getState()).isNotEqualTo(org.eclipse.sirius.components.diagrams.ViewModifier.Hidden);
            assertThat(caseDefinitionSubjectCompartment.getChildNodes()).isEmpty();
        });

        Runnable dropCompartmentItem = () -> {
            var input = new DropNodesInput(UUID.randomUUID(), GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID.toString(), GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID,
                    List.of(caseUsageSubjectNodeId.get(), useCaseUsageSubjectNodeId.get()), caseDefinitionSubjectCompartmentNodeId.get(),
                    List.of(new Position(0, 0), new Position(25, 25)));
            var result = this.dropNodesMutationRunner.run(input);
            String typename = JsonPath.read(result.data(), DATA_DROP_NODES_TYPENAME);
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            var caseUsageSubjectCompartment = this.getCompartmentNode(diagram, GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_USAGE_ID, SUBJECT);
            var useCaseUsageSubjectCompartment = this.getCompartmentNode(diagram, GeneralViewWithTopNodesTestProjectData.SemanticIds.USE_CASE_USAGE_ID, SUBJECT);
            var caseDefinitionSubjectCompartment = this.getCompartmentNode(diagram, GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_DEFINITION_ID, SUBJECT);

            assertThat(caseDefinitionSubjectCompartment.getChildNodes()).hasSize(1);
            assertThat(caseDefinitionSubjectCompartment.getChildNodes().getFirst().getTargetObjectId()).isIn(caseUsageSubjectTargetObjectId.get(), useCaseUsageSubjectTargetObjectId.get());
            assertThat(caseUsageSubjectCompartment.getChildNodes().size() + useCaseUsageSubjectCompartment.getChildNodes().size()).isEqualTo(1);
            assertThat(caseUsageSubjectCompartment.getChildNodes().stream().map(org.eclipse.sirius.components.diagrams.Node::getTargetObjectId))
                    .doesNotContain(caseDefinitionSubjectCompartment.getChildNodes().getFirst().getTargetObjectId());
            assertThat(useCaseUsageSubjectCompartment.getChildNodes().stream().map(org.eclipse.sirius.components.diagrams.Node::getTargetObjectId))
                    .doesNotContain(caseDefinitionSubjectCompartment.getChildNodes().getFirst().getTargetObjectId());
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createSubjectOnCaseUsage)
                .consumeNextWith(updatedDiagramContentAfterCaseUsageCreation)
                .then(createSubjectOnUseCaseUsage)
                .consumeNextWith(updatedDiagramContentAfterUseCaseUsageCreation)
                .then(revealCaseDefinitionSubjectCompartment)
                .consumeNextWith(updatedDiagramContentAfterCaseDefinitionReveal)
                .then(dropCompartmentItem)
                .consumeNextWith(updatedDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN objective requirement compartment items, WHEN they are dropped on objective compartments, THEN drop succeeds for case definition and case usage objective providers")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void dropObjectiveCompartmentItemsOnObjectiveCompartments() {
        var flux = this.givenSubscriptionToDiagram();
        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);
        var creationTestsService = new NodeCreationTestsService(this.toolTester, this.descriptionNameGenerator, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID);

        var diagramRef = new AtomicReference<Diagram>();
        var caseUsageObjectiveCompartmentNodeId = new AtomicReference<String>();
        var useCaseUsageObjectiveCompartmentNodeId = new AtomicReference<String>();
        var caseDefinitionObjectiveCompartmentNodeId = new AtomicReference<String>();
        var caseUsageObjectiveNodeId = new AtomicReference<String>();
        var useCaseUsageObjectiveNodeId = new AtomicReference<String>();
        var caseUsageObjectiveTargetObjectId = new AtomicReference<String>();
        var useCaseUsageObjectiveTargetObjectId = new AtomicReference<String>();

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(currentDiagram -> {
            diagramRef.set(currentDiagram);
            caseUsageObjectiveCompartmentNodeId.set(this.getCompartmentNodeId(currentDiagram, GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_USAGE_ID, OBJECTIVE));
            useCaseUsageObjectiveCompartmentNodeId.set(this.getCompartmentNodeId(currentDiagram, GeneralViewWithTopNodesTestProjectData.SemanticIds.USE_CASE_USAGE_ID, OBJECTIVE));
            caseDefinitionObjectiveCompartmentNodeId.set(this.getCompartmentNodeId(currentDiagram, GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_DEFINITION_ID, OBJECTIVE));
            assertThat(caseUsageObjectiveCompartmentNodeId.get()).isNotBlank();
            assertThat(useCaseUsageObjectiveCompartmentNodeId.get()).isNotBlank();
            assertThat(caseDefinitionObjectiveCompartmentNodeId.get()).isNotBlank();
        });

        Runnable createObjectiveOnCaseUsage = creationTestsService.createNodeWithSelectionDialogWithoutSelectionProvided(diagramDescriptionIdProvider, diagramRef,
                SysmlPackage.eINSTANCE.getCaseUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_USAGE_ID, "New Objective");

        Runnable createObjectiveOnUseCaseUsage = creationTestsService.createNodeWithSelectionDialogWithoutSelectionProvided(diagramDescriptionIdProvider, diagramRef,
                SysmlPackage.eINSTANCE.getUseCaseUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.USE_CASE_USAGE_ID, "New Objective");

        Consumer<Object> updatedDiagramContentAfterCaseUsageCreation = assertRefreshedDiagramThat(diagram -> {
            caseUsageObjectiveNodeId.set(this.getCompartmentItemNodeId(diagram, GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_USAGE_ID, OBJECTIVE));
            caseUsageObjectiveTargetObjectId.set(this.getCompartmentItemTargetObjectId(diagram, GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_USAGE_ID, OBJECTIVE));
            assertThat(caseUsageObjectiveNodeId.get()).isNotBlank();
            assertThat(caseUsageObjectiveTargetObjectId.get()).isNotBlank();
        });

        Consumer<Object> updatedDiagramContentAfterUseCaseUsageCreation = assertRefreshedDiagramThat(diagram -> {
            useCaseUsageObjectiveNodeId.set(this.getCompartmentItemNodeId(diagram, GeneralViewWithTopNodesTestProjectData.SemanticIds.USE_CASE_USAGE_ID, OBJECTIVE));
            useCaseUsageObjectiveTargetObjectId.set(this.getCompartmentItemTargetObjectId(diagram, GeneralViewWithTopNodesTestProjectData.SemanticIds.USE_CASE_USAGE_ID, OBJECTIVE));
            assertThat(useCaseUsageObjectiveNodeId.get()).isNotBlank();
            assertThat(useCaseUsageObjectiveTargetObjectId.get()).isNotBlank();
        });

        Runnable revealCaseDefinitionObjectiveCompartment = () -> {
            var input = new HideDiagramElementInput(
                    UUID.randomUUID(),
                    GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                    GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID,
                    Set.of(caseDefinitionObjectiveCompartmentNodeId.get()),
                    false);
            var result = this.hideDiagramElementMutationRunner.run(input);
            String typename = JsonPath.read(result.data(), "$.data.hideDiagramElement.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentAfterCaseDefinitionReveal = assertRefreshedDiagramThat(diagram -> {
            var caseDefinitionObjectiveCompartment = this.getCompartmentNode(diagram, GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_DEFINITION_ID, OBJECTIVE);
            assertThat(caseDefinitionObjectiveCompartment.getState()).isNotEqualTo(org.eclipse.sirius.components.diagrams.ViewModifier.Hidden);
            assertThat(caseDefinitionObjectiveCompartment.getChildNodes()).isEmpty();
        });

        Runnable dropCompartmentItem = () -> {
            var input = new DropNodesInput(UUID.randomUUID(), GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID.toString(), GeneralViewWithTopNodesTestProjectData.GraphicalIds.DIAGRAM_ID,
                    List.of(caseUsageObjectiveNodeId.get(), useCaseUsageObjectiveNodeId.get()), caseDefinitionObjectiveCompartmentNodeId.get(),
                    List.of(new Position(0, 0), new Position(25, 25)));
            var result = this.dropNodesMutationRunner.run(input);
            String typename = JsonPath.read(result.data(), DATA_DROP_NODES_TYPENAME);
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedDiagramContentConsumer = assertRefreshedDiagramThat(diagram -> {
            var caseUsageObjectiveCompartment = this.getCompartmentNode(diagram, GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_USAGE_ID, OBJECTIVE);
            var useCaseUsageObjectiveCompartment = this.getCompartmentNode(diagram, GeneralViewWithTopNodesTestProjectData.SemanticIds.USE_CASE_USAGE_ID, OBJECTIVE);
            var caseDefinitionObjectiveCompartment = this.getCompartmentNode(diagram, GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_DEFINITION_ID, OBJECTIVE);

            assertThat(caseDefinitionObjectiveCompartment.getChildNodes()).hasSize(1);
            assertThat(caseDefinitionObjectiveCompartment.getChildNodes().getFirst().getTargetObjectId()).isIn(caseUsageObjectiveTargetObjectId.get(), useCaseUsageObjectiveTargetObjectId.get());
            assertThat(caseUsageObjectiveCompartment.getChildNodes().size() + useCaseUsageObjectiveCompartment.getChildNodes().size()).isEqualTo(1);
            assertThat(caseUsageObjectiveCompartment.getChildNodes().stream().map(org.eclipse.sirius.components.diagrams.Node::getTargetObjectId))
                    .doesNotContain(caseDefinitionObjectiveCompartment.getChildNodes().getFirst().getTargetObjectId());
            assertThat(useCaseUsageObjectiveCompartment.getChildNodes().stream().map(org.eclipse.sirius.components.diagrams.Node::getTargetObjectId))
                    .doesNotContain(caseDefinitionObjectiveCompartment.getChildNodes().getFirst().getTargetObjectId());
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createObjectiveOnCaseUsage)
                .consumeNextWith(updatedDiagramContentAfterCaseUsageCreation)
                .then(createObjectiveOnUseCaseUsage)
                .consumeNextWith(updatedDiagramContentAfterUseCaseUsageCreation)
                .then(revealCaseDefinitionObjectiveCompartment)
                .consumeNextWith(updatedDiagramContentAfterCaseDefinitionReveal)
                .then(dropCompartmentItem)
                .consumeNextWith(updatedDiagramContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    private org.eclipse.sirius.components.diagrams.Node getCompartmentNode(org.eclipse.sirius.components.diagrams.Diagram diagram, String targetObjectId, String compartmentLabel) {
        return new DiagramNavigator(diagram).nodeWithTargetObjectId(targetObjectId).childNodeWithLabel(compartmentLabel).getNode();
    }

    private String getCompartmentNodeId(org.eclipse.sirius.components.diagrams.Diagram diagram, String targetObjectId, String compartmentLabel) {
        return this.getCompartmentNode(diagram, targetObjectId, compartmentLabel).getId();
    }

    private String getCompartmentItemNodeId(org.eclipse.sirius.components.diagrams.Diagram diagram, String targetObjectId, String compartmentLabel) {
        return this.getCompartmentNode(diagram, targetObjectId, compartmentLabel).getChildNodes().stream()
                .findFirst()
                .map(org.eclipse.sirius.components.diagrams.Node::getId)
                .orElse("");
    }

    private String getCompartmentItemTargetObjectId(org.eclipse.sirius.components.diagrams.Diagram diagram, String targetObjectId, String compartmentLabel) {
        return this.getCompartmentNode(diagram, targetObjectId, compartmentLabel).getChildNodes().stream()
                .findFirst()
                .map(org.eclipse.sirius.components.diagrams.Node::getTargetObjectId)
                .orElse("");
    }
}
