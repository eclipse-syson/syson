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
package org.eclipse.syson.metamodel.helper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.sirius.components.collaborative.dto.EditingContextEventInput;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.graphql.tests.EditingContextEventSubscriptionRunner;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controller.editingContext.checkers.ISemanticChecker;
import org.eclipse.syson.application.controller.editingContext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.OccurrenceUsage;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortionKind;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;

/**
 * Tests the implicit specializations of SysMLv2 elements. We need an integration test to benefit from the standard
 * libraries.
 *
 * @author arichard
 */
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ImplicitSpecializationsTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private EditingContextEventSubscriptionRunner editingContextEventSubscriptionRunner;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a PartUsage with or without Specialization (FeatureTyping, Subsetting or Redefinition), WHEN validation is executed, THEN the Part implicitly specializes 'Parts::parts' from the standard libraries.")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void testImplicitSpecializationOnPartUsage() {
        var semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID);
        var editingContextEventInput = new EditingContextEventInput(UUID.randomUUID(), GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID);
        var flux = this.editingContextEventSubscriptionRunner.run(editingContextEventInput).flux();

        ISemanticChecker semanticChecker = (editingContext) -> {
            Object semanticRootObject = this.objectSearchService.getObject(editingContext, GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID).orElse(null);
            assertThat(semanticRootObject).isInstanceOf(Element.class);
            Element semanticRootElement = (Element) semanticRootObject;
            Optional<PartUsage> optPartUsage = EMFUtils.allContainedObjectOfType(semanticRootElement, PartUsage.class)
                    .filter(element -> Objects.equals(element.getName(), "part"))
                    .findFirst();
            assertThat(optPartUsage).isPresent();
            PartUsage partUsage = optPartUsage.get();

            assertTrue(partUsage.specializesFromLibrary("Parts::parts"));
            assertTrue(partUsage.specializesFromLibrary("Parts::Part"));

            Optional<PartDefinition> optPartDef = EMFUtils.allContainedObjectOfType(semanticRootElement, PartDefinition.class)
                    .filter(element -> Objects.equals(element.getName(), "PartDefinition"))
                    .findFirst();
            assertThat(optPartDef).isPresent();
            PartDefinition partDef = optPartDef.get();

            var newFeatureTyping = SysmlFactory.eINSTANCE.createFeatureTyping();
            partUsage.getOwnedRelationship().add(newFeatureTyping);
            newFeatureTyping.setType(partDef);
            newFeatureTyping.setTypedFeature(partUsage);

            assertTrue(partUsage.specializesFromLibrary("Parts::parts"));
            assertTrue(partUsage.specializesFromLibrary("Parts::Part"));
            assertTrue(partDef.specializesFromLibrary("Parts::Part"));

            var newMembership = SysmlFactory.eINSTANCE.createOwningMembership();
            semanticRootElement.getOwnedRelationship().add(newMembership);
            var anotherPart = SysmlFactory.eINSTANCE.createPartUsage();
            newMembership.getOwnedRelatedElement().add(anotherPart);

            var newSubsetting = SysmlFactory.eINSTANCE.createSubsetting();
            partUsage.getOwnedRelationship().add(newSubsetting);
            newSubsetting.setSubsettedFeature(anotherPart);
            newSubsetting.setSubsettingFeature(partUsage);

            assertTrue(partUsage.specializesFromLibrary("Parts::parts"));
            assertTrue(partUsage.specializesFromLibrary("Parts::Part"));
            assertTrue(anotherPart.specializesFromLibrary("Parts::parts"));
            assertTrue(anotherPart.specializesFromLibrary("Parts::Part"));
        };

        StepVerifier.create(flux)
                .then(semanticCheckerService.checkEditingContext(semanticChecker))
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN an AcceptAction, WHEN the validation is executed, THEN the AcceptAction implicitly specializes 'Actions::acceptActions'")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void testImplicitSpecializationOnAcceptActionUsage() {
        var semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID);
        var editingContextEventInput = new EditingContextEventInput(UUID.randomUUID(), GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID);
        var flux = this.editingContextEventSubscriptionRunner.run(editingContextEventInput).flux();

        ISemanticChecker semanticChecker = (editingContext) -> {
            Object semanticRootObject = this.objectSearchService.getObject(editingContext, GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID).orElse(null);
            assertThat(semanticRootObject).isInstanceOf(Element.class);
            Element semanticRootElement = (Element) semanticRootObject;
            Optional<AcceptActionUsage> optAcceptActionUsage = EMFUtils.allContainedObjectOfType(semanticRootElement, AcceptActionUsage.class)
                    .filter(element -> Objects.equals(element.getName(), "acceptAction"))
                    .findFirst();
            assertThat(optAcceptActionUsage).isPresent();
            AcceptActionUsage acceptActionUsage = optAcceptActionUsage.get();

            assertTrue(acceptActionUsage.specializesFromLibrary("Actions::acceptActions"));
        };

        StepVerifier.create(flux)
                .then(semanticCheckerService.checkEditingContext(semanticChecker))
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a parameter of an ActionUsage that subsets another ActionUsage with parameters, WHEN the validation is executed, THEN the ActionUsgae implicitly redefines the corresponding parameter")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void testImplicitParameterRedefinitionOnItemUsage() {
        var semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID);
        var editingContextEventInput = new EditingContextEventInput(UUID.randomUUID(), GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID);
        var flux = this.editingContextEventSubscriptionRunner.run(editingContextEventInput).flux();

        ISemanticChecker semanticChecker = (editingContext) -> {
            Object semanticRootObject = this.objectSearchService.getObject(editingContext, GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID).orElse(null);
            assertThat(semanticRootObject).isInstanceOf(Element.class);
            Element semanticRootElement = (Element) semanticRootObject;
            Optional<ActionUsage> optionalParentActionWithParameter = EMFUtils.allContainedObjectOfType(semanticRootElement, ActionUsage.class)
                    .filter(actionUsage -> Objects.equals(actionUsage.getName(), "parentActionWithParameter"))
                    .findFirst();
            assertThat(optionalParentActionWithParameter).isPresent();
            ActionUsage parentActionWithParameter = optionalParentActionWithParameter.get();
            Optional<ActionUsage> optionalSubActionWithParameter = EMFUtils.allContainedObjectOfType(semanticRootElement, ActionUsage.class)
                    .filter(actionUsage -> Objects.equals(actionUsage.getName(), "subActionWithParameter"))
                    .findFirst();
            assertThat(optionalSubActionWithParameter).isPresent();
            ActionUsage subActionWithParameter = optionalSubActionWithParameter.get();
            assertThat(parentActionWithParameter.getParameter()).hasSize(1);
            assertThat(subActionWithParameter.getParameter()).hasSize(1);
            List<Specialization> subParameterSpecializations = subActionWithParameter.getParameter().get(0).getOwnedSpecialization();
            Optional<Redefinition> optionalRedefinition = subParameterSpecializations.stream()
                    .filter(Redefinition.class::isInstance)
                    .map(Redefinition.class::cast)
                    .findFirst();
            assertThat(optionalRedefinition).isPresent();
            assertThat(optionalRedefinition)
                    .get()
                    .extracting(Redefinition::getRedefinedFeature)
                    .isEqualTo(parentActionWithParameter.getParameter().get(0));
        };

        StepVerifier.create(flux)
                .then(semanticCheckerService.checkEditingContext(semanticChecker))
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a snapshot Occurrence, WHEN the validation is executed, THEN the Occurrence implicitly specializes 'Occurrences::Occurrence::snapshot'")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void testImplicitSnapshotSpecializationOnOccurrenceUsage() {
        var semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID);
        var editingContextEventInput = new EditingContextEventInput(UUID.randomUUID(), GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID);
        var flux = this.editingContextEventSubscriptionRunner.run(editingContextEventInput).flux();

        var initializeEditingContext = this.semanticRunnableFactory.createRunnable(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, (editingContext, executeEditingContextFunctionInput) -> {
            var optionalOccurrenceUsage = this.objectSearchService.getObject(editingContext, GeneralViewWithTopNodesTestProjectData.SemanticIds.OCCURRENCE_USAGE_ID)
                    .filter(OccurrenceUsage.class::isInstance)
                    .map(OccurrenceUsage.class::cast);
            assertThat(optionalOccurrenceUsage).isPresent();
            var occurrenceUsage = optionalOccurrenceUsage.get();
            assertTrue(occurrenceUsage.specializesFromLibrary("Occurrences::occurrences"));
            assertFalse(occurrenceUsage.specializesFromLibrary("Occurrences::Occurrence::snapshots"));
            occurrenceUsage.setPortionKind(PortionKind.SNAPSHOT);
            return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
        });

        ISemanticChecker semanticChecker = (editingContext) -> {
            var optionalOccurrenceUsage = this.objectSearchService.getObject(editingContext, GeneralViewWithTopNodesTestProjectData.SemanticIds.OCCURRENCE_USAGE_ID)
                    .filter(OccurrenceUsage.class::isInstance)
                    .map(OccurrenceUsage.class::cast);
            assertThat(optionalOccurrenceUsage).isPresent();
            var occurrenceUsage = optionalOccurrenceUsage.get();
            assertTrue(occurrenceUsage.specializesFromLibrary("Occurrences::occurrences"));
            assertTrue(occurrenceUsage.specializesFromLibrary("Occurrences::Occurrence::snapshots"));
        };

        StepVerifier.create(flux)
                .then(initializeEditingContext)
                .then(semanticCheckerService.checkEditingContext(semanticChecker))
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a timeSlice PartUsage subsettings a timeSlice OccurrenceUsage, WHEN the validation is executed, THEN both specialize 'Occurrences::Occurrence::timeSlices' from library, but only the OccurrenceUsage has the timeSlice owned specialization")
    @GivenSysONServer({ GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH })
    @Test
    public void testImplicitTimeSliceSpecializationOnPartUsageSubsettingOccurrenceUsageBothTimeSlices() {
        var semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID,
                GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID);
        var editingContextEventInput = new EditingContextEventInput(UUID.randomUUID(), GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID);
        var flux = this.editingContextEventSubscriptionRunner.run(editingContextEventInput).flux();

        var initializeEditingContext = this.semanticRunnableFactory.createRunnable(GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, (editingContext, executeEditingContextFunctionInput) -> {
            var optionalOccurrenceUsage = this.objectSearchService.getObject(editingContext, GeneralViewWithTopNodesTestProjectData.SemanticIds.OCCURRENCE_USAGE_ID)
                    .filter(OccurrenceUsage.class::isInstance)
                    .map(OccurrenceUsage.class::cast);
            assertThat(optionalOccurrenceUsage).isPresent();
            var occurrenceUsage = optionalOccurrenceUsage.get();
            assertTrue(occurrenceUsage.specializesFromLibrary("Occurrences::occurrences"));
            assertFalse(occurrenceUsage.specializesFromLibrary("Occurrences::Occurrence::timeSlices"));
            occurrenceUsage.setPortionKind(PortionKind.TIMESLICE);

            var optionalPartUsage = this.objectSearchService.getObject(editingContext, GeneralViewWithTopNodesTestProjectData.SemanticIds.PART_USAGE_ID)
                    .filter(PartUsage.class::isInstance)
                    .map(PartUsage.class::cast);
            assertThat(optionalPartUsage).isPresent();
            var partUsage = optionalPartUsage.get();
            assertTrue(partUsage.specializesFromLibrary("Parts::Part"));
            assertFalse(partUsage.specializesFromLibrary("Occurrences::Occurrence::timeSlices"));
            partUsage.setPortionKind(PortionKind.TIMESLICE);

            var newSubsetting = SysmlFactory.eINSTANCE.createSubsetting();
            partUsage.getOwnedRelationship().add(newSubsetting);
            newSubsetting.setSubsettedFeature(occurrenceUsage);
            newSubsetting.setSubsettingFeature(partUsage);

            return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
        });

        ISemanticChecker semanticChecker = (editingContext) -> {
            Element semanticRootObject = this.objectSearchService.getObject(editingContext, GeneralViewWithTopNodesTestProjectData.SemanticIds.PACKAGE_1_ID)
                    .filter(Element.class::isInstance)
                    .map(Element.class::cast)
                    .orElse(null);
            assertThat(semanticRootObject).isNotNull();
            var timeSliceFeature = new ElementUtil().findByNameAndType(semanticRootObject, "Occurrences::Occurrence::timeSlices", Feature.class);
            assertThat(timeSliceFeature).isNotNull();

            var optionalPartUsage = this.objectSearchService.getObject(editingContext, GeneralViewWithTopNodesTestProjectData.SemanticIds.PART_USAGE_ID)
                    .filter(PartUsage.class::isInstance)
                    .map(PartUsage.class::cast);
            assertThat(optionalPartUsage).isPresent();
            var partUsage = optionalPartUsage.get();
            assertTrue(partUsage.specializesFromLibrary("Occurrences::Occurrence::timeSlices"));
            var partUsageOwnedSpecialization = partUsage.getOwnedSpecialization();
            var partUsageTimeSliceSubSettingFeatureCount = partUsageOwnedSpecialization.stream()
                    .filter(Subsetting.class::isInstance)
                    .map(Subsetting.class::cast)
                    .filter(subsetting -> subsetting.getSubsettedFeature().equals(timeSliceFeature))
                    .count();
            assertThat(partUsageTimeSliceSubSettingFeatureCount).isEqualTo(0);

            var optionalOccurrenceUsage = this.objectSearchService.getObject(editingContext, GeneralViewWithTopNodesTestProjectData.SemanticIds.OCCURRENCE_USAGE_ID)
                    .filter(OccurrenceUsage.class::isInstance)
                    .map(OccurrenceUsage.class::cast);
            assertThat(optionalOccurrenceUsage).isPresent();
            var occurrenceUsage = optionalOccurrenceUsage.get();
            var occurrenceUsageOwnedSpecialization = occurrenceUsage.getOwnedSpecialization();
            assertTrue(partUsage.specializesFromLibrary("Occurrences::Occurrence::timeSlices"));
            var occurrenceUsageTimeSliceSubSettingFeatureCount = occurrenceUsageOwnedSpecialization.stream()
                    .filter(Subsetting.class::isInstance)
                    .map(Subsetting.class::cast)
                    .filter(subsetting -> subsetting.getSubsettedFeature().equals(timeSliceFeature))
                    .count();
            assertThat(occurrenceUsageTimeSliceSubSettingFeatureCount).isEqualTo(1);
        };

        StepVerifier.create(flux)
                .then(initializeEditingContext)
                .then(semanticCheckerService.checkEditingContext(semanticChecker))
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
