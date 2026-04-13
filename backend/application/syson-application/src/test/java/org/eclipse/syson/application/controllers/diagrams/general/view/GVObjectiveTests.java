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
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.assertj.core.api.Assertions;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramEventInput;
import org.eclipse.sirius.components.collaborative.diagrams.dto.DiagramRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.view.emf.diagram.IDiagramIdProvider;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controller.editingcontext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckDiagramElementCount;
import org.eclipse.syson.application.controllers.diagrams.checkers.CheckNodeInCompartment;
import org.eclipse.syson.application.controllers.diagrams.testers.ToolTester;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.diagrams.DiagramComparator;
import org.eclipse.syson.services.diagrams.DiagramDescriptionIdProvider;
import org.eclipse.syson.services.diagrams.NodeCreationTestsService;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramDescription;
import org.eclipse.syson.services.diagrams.api.IGivenDiagramSubscription;
import org.eclipse.syson.standard.diagrams.view.SDVDescriptionNameGenerator;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
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
 * Tests the creation of "Objective" nodes in the General View diagram.
 *
 * @author gcoutable
 */
@Transactional
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GVObjectiveTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenDiagramSubscription givenDiagramSubscription;

    @Autowired
    private IGivenDiagramDescription givenDiagramDescription;

    @Autowired
    private IDiagramIdProvider diagramIdProvider;

    @Autowired
    private ToolTester nodeCreationTester;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private DiagramComparator diagramComparator;

    private NodeCreationTestsService creationTestsService;

    private SemanticCheckerService semanticCheckerService;

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
        this.semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID);
    }

    @DisplayName("GIVEN a CaseDefinition, WHEN creating an objective selecting a RequirementUsage, THEN the objective requirement subsetted by the RequirementUsage is created in the CaseDefinition")
    @Test
    public void testCreateNewObjectiveWithSubsetInCaseDefinition() {
        this.createNewObjectiveWithSubsettingIn(SysmlPackage.eINSTANCE.getCaseDefinition(), GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_DEFINITION_ID, "CaseDefinition");
    }

    @DisplayName("GIVEN a Case, WHEN creating an objective selecting a RequirementUsage, THEN the objective requirement subsetted by the RequirementUsage is created in the Case")
    @Test
    public void testCreateNewObjectiveWithSubsetInCaseUsage() {
        this.createNewObjectiveWithSubsettingIn(SysmlPackage.eINSTANCE.getCaseUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_USAGE_ID, "case");
    }

    @DisplayName("GIVEN a UseCaseDefinition, WHEN creating an objective selecting a RequirementUsage, THEN the objective requirement subsetted by the RequirementUsage is created in the UseCaseDefinition")
    @Test
    public void testCreateNewObjectiveWithSubsetInUseCaseDefinition() {
        this.createNewObjectiveWithSubsettingIn(SysmlPackage.eINSTANCE.getUseCaseDefinition(), GeneralViewWithTopNodesTestProjectData.SemanticIds.USE_CASE_DEFINITION_ID, "UseCaseDefinition");
    }

    @DisplayName("GIVEN a UseCase, WHEN creating an objective selecting a RequirementUsage, THEN the objective requirement subsetted by the RequirementUsage is created in the UseCase")
    @Test
    public void testCreateNewObjectiveWithSubsetInUseCaseUsage() {
        this.createNewObjectiveWithSubsettingIn(SysmlPackage.eINSTANCE.getUseCaseUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.USE_CASE_USAGE_ID, "useCase");
    }

    @DisplayName("GIVEN a CaseDefinition, WHEN creating an objective selecting a RequirementDefinition, THEN the objective requirement typed by the RequirementDefinition is created in the CaseDefinition")
    @Test
    public void testCreateNewObjectiveWithFeatureTypingInCaseDefinition() {
        this.createNewObjectiveWithFeatureTypingIn(SysmlPackage.eINSTANCE.getCaseDefinition(), GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_DEFINITION_ID, "CaseDefinition");
    }

    @DisplayName("GIVEN a CaseUsage, WHEN creating an objective selecting a RequirementDefinition, THEN the objective requirement typed by the RequirementDefinition is created in the CaseUsage")
    @Test
    public void testCreateNewObjectiveWithFeatureTypingInCaseUsage() {
        this.createNewObjectiveWithFeatureTypingIn(SysmlPackage.eINSTANCE.getCaseUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_USAGE_ID, "case");
    }

    @DisplayName("GIVEN a UseCaseDefinition, WHEN creating an objective selecting a RequirementDefinition, THEN the objective requirement typed by the RequirementDefinition is created in the UseCaseDefinition")
    @Test
    public void testCreateNewObjectiveWithFeatureTypingInUseCaseDefinition() {
        this.createNewObjectiveWithFeatureTypingIn(SysmlPackage.eINSTANCE.getUseCaseDefinition(), GeneralViewWithTopNodesTestProjectData.SemanticIds.USE_CASE_DEFINITION_ID, "UseCaseDefinition");
    }

    @DisplayName("GIVEN a UseCaseUsage, WHEN creating an objective selecting a RequirementDefinition, THEN the objective requirement typed by the RequirementDefinition is created in the UseCaseUsage")
    @Test
    public void testCreateNewObjectiveWithFeatureTypingInUseCaseUsage() {
        this.createNewObjectiveWithFeatureTypingIn(SysmlPackage.eINSTANCE.getUseCaseUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.USE_CASE_USAGE_ID, "useCase");
    }

    @DisplayName("GIVEN a CaseDefinition, WHEN creating an objective without selection, THEN the objective requirement without specialization is created in the CaseDefinition")
    @Test
    public void testCreateStandaloneNewObjectiveInCaseDefinition() {
        this.createStandaloneNewObjectiveIn(SysmlPackage.eINSTANCE.getCaseDefinition(), GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_DEFINITION_ID, "CaseDefinition");
    }

    @DisplayName("GIVEN a CaseUsage, WHEN creating an objective without selection, THEN the objective requirement without specialization is created in the CaseUsage")
    @Test
    public void testCreateStandaloneNewObjectiveInCaseUsage() {
        this.createStandaloneNewObjectiveIn(SysmlPackage.eINSTANCE.getCaseUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.CASE_USAGE_ID, "case");
    }

    @DisplayName("GIVEN a UseCaseDefinition, WHEN creating an objective without selection, THEN the objective requirement without specialization is created in the UseCaseDefinition")
    @Test
    public void testCreateStandaloneNewObjectiveInUseCaseDefinition() {
        this.createStandaloneNewObjectiveIn(SysmlPackage.eINSTANCE.getUseCaseDefinition(), GeneralViewWithTopNodesTestProjectData.SemanticIds.USE_CASE_DEFINITION_ID, "UseCaseDefinition");
    }

    @DisplayName("GIVEN a UseCaseUsage, WHEN creating an objective without selection, THEN the objective requirement without specialization is created in the UseCaseUsage")
    @Test
    public void testCreateStandaloneNewObjectiveInUseCaseUsage() {
        this.createStandaloneNewObjectiveIn(SysmlPackage.eINSTANCE.getUseCaseUsage(), GeneralViewWithTopNodesTestProjectData.SemanticIds.USE_CASE_USAGE_ID, "useCase");
    }

    public void createNewObjectiveWithSubsettingIn(EClass eClassWithObjectiveRequirement, String targetObjectId, String parentLabel) {
        var flux = this.givenSubscriptionToDiagram();
        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();

        EClass childEClass = SysmlPackage.eINSTANCE.getRequirementUsage();
        var objectiveCreationToolName = "New Objective";
        var containmentReference = eClassWithObjectiveRequirement.getEAllReferences().stream()
                .filter(eReference -> eReference.getName().equals("objectiveRequirement") && eReference.getEType() == SysmlPackage.eINSTANCE.getRequirementUsage()).findFirst()
                .orElseGet(() -> Assertions.fail("No fitting EReference could be found in '%s'.".formatted(eClassWithObjectiveRequirement.getName())));
        var existingElementId = GeneralViewWithTopNodesTestProjectData.SemanticIds.REQUIREMENT_USAGE_ID;

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable createNodeRunnable = this.creationTestsService.createNodeWithSelectionDialogWithSingleSelection(diagramDescriptionIdProvider, diagram, eClassWithObjectiveRequirement, targetObjectId, objectiveCreationToolName, existingElementId);

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(2)
                    .check(initialDiagram, newDiagram, true);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withTargetObjectId(targetObjectId)
                    .withCompartmentName("objective")
                    .hasNodeDescriptionName(this.descriptionNameGenerator.getCompartmentItemName(containmentReference.getEContainingClass(), containmentReference))
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        });

        Consumer<Object> additionalCheck = referencedObject -> {
            assertThat(referencedObject).isInstanceOf(RequirementUsage.class)
                    .asInstanceOf(type(RequirementUsage.class))
                    .satisfies(requirement -> {
                        EList<Subsetting> objectiveRequirementSubsets = requirement.getOwnedSubsetting();
                        assertThat(objectiveRequirementSubsets).isNotEmpty();
                        assertThat(objectiveRequirementSubsets.getFirst().getSubsettedFeature().getName()).isEqualTo("requirement");
                    });
        };

        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass, additionalCheck));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    public void createNewObjectiveWithFeatureTypingIn(EClass eClassWithObjectiveRequirement, String targetObjectId, String parentLabel) {
        var flux = this.givenSubscriptionToDiagram();
        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();

        EClass childEClass = SysmlPackage.eINSTANCE.getRequirementUsage();
        var objectiveCreationToolName = "New Objective";
        var containmentReference = eClassWithObjectiveRequirement.getEAllReferences().stream()
                .filter(eReference -> eReference.getName().equals("objectiveRequirement") && eReference.getEType() == SysmlPackage.eINSTANCE.getRequirementUsage()).findFirst()
                .orElseGet(() -> Assertions.fail("No fitting EReference could be found in '%s'.".formatted(eClassWithObjectiveRequirement.getName())));
        var existingElementId = GeneralViewWithTopNodesTestProjectData.SemanticIds.REQUIREMENT_DEFINITION_ID;

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable createNodeRunnable = this.creationTestsService.createNodeWithSelectionDialogWithSingleSelection(diagramDescriptionIdProvider, diagram, eClassWithObjectiveRequirement, targetObjectId, objectiveCreationToolName, existingElementId);

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(2)
                    .check(initialDiagram, newDiagram, true);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withTargetObjectId(targetObjectId)
                    .withCompartmentName("objective")
                    .hasNodeDescriptionName(this.descriptionNameGenerator.getCompartmentItemName(containmentReference.getEContainingClass(), containmentReference))
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        });

        Consumer<Object> additionalCheck = referencedObject -> {
            assertThat(referencedObject).isInstanceOf(RequirementUsage.class)
                    .asInstanceOf(type(RequirementUsage.class))
                    .satisfies(requirement -> {
                        var types = requirement.getType();
                        assertThat(types).isNotEmpty();
                        assertThat(types.getFirst().getName()).isEqualTo("RequirementDefinition");
                    });
        };

        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass, additionalCheck));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    public void createStandaloneNewObjectiveIn(EClass eClassWithObjectiveRequirement, String targetObjectId, String parentLabel) {
        var flux = this.givenSubscriptionToDiagram();
        var diagramDescription = this.givenDiagramDescription.getDiagramDescription(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID);
        var diagramDescriptionIdProvider = new DiagramDescriptionIdProvider(diagramDescription, this.diagramIdProvider);

        AtomicReference<Diagram> diagram = new AtomicReference<>();

        EClass childEClass = SysmlPackage.eINSTANCE.getRequirementUsage();
        var objectiveCreationToolName = "New Objective";
        var containmentReference = eClassWithObjectiveRequirement.getEAllReferences().stream()
                .filter(eReference -> eReference.getName().equals("objectiveRequirement") && eReference.getEType() == SysmlPackage.eINSTANCE.getRequirementUsage()).findFirst()
                .orElseGet(() -> Assertions.fail("No fitting EReference could be found in '%s'.".formatted(eClassWithObjectiveRequirement.getName())));

        Consumer<Object> initialDiagramContentConsumer = assertRefreshedDiagramThat(diagram::set);

        Runnable createNodeRunnable = this.creationTestsService.createNodeWithSelectionDialogWithoutSelectionProvided(diagramDescriptionIdProvider, diagram, eClassWithObjectiveRequirement, targetObjectId, objectiveCreationToolName);

        Consumer<Object> diagramCheck = assertRefreshedDiagramThat(newDiagram -> {
            var initialDiagram = diagram.get();
            new CheckDiagramElementCount(this.diagramComparator)
                    .hasNewNodeCount(1)
                    .hasNewEdgeCount(1)
                    .check(initialDiagram, newDiagram, true);
            new CheckNodeInCompartment(diagramDescriptionIdProvider, this.diagramComparator)
                    .withTargetObjectId(targetObjectId)
                    .withCompartmentName("objective")
                    .hasNodeDescriptionName(this.descriptionNameGenerator.getCompartmentItemName(containmentReference.getEContainingClass(), containmentReference))
                    .hasCompartmentCount(0)
                    .check(initialDiagram, newDiagram);
        });

        Consumer<Object> additionalCheck = referencedObject -> {
            assertThat(referencedObject).isInstanceOf(RequirementUsage.class)
                    .asInstanceOf(type(RequirementUsage.class))
                    .satisfies(requirement -> {
                        assertThat(requirement.getOwnedSpecialization()).allMatch(Specialization::isIsImplied);
                        EList<Type> types = requirement.getType();
                        assertThat(types)
                                .isNotEmpty()
                                .allMatch(Element::isIsLibraryElement);
                    });
        };

        Runnable semanticCheck = this.semanticCheckerService.checkEditingContext(this.semanticCheckerService.getElementInParentSemanticChecker(parentLabel, containmentReference, childEClass, additionalCheck));

        StepVerifier.create(flux)
                .consumeNextWith(initialDiagramContentConsumer)
                .then(createNodeRunnable)
                .consumeNextWith(diagramCheck)
                .then(semanticCheck)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
