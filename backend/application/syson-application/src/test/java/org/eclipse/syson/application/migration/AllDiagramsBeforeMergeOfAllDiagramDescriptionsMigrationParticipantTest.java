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
package org.eclipse.syson.application.migration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.UUID;
import java.util.function.BiFunction;

import org.eclipse.sirius.components.collaborative.api.IRepresentationSearchService;
import org.eclipse.sirius.components.collaborative.dto.EditingContextEventInput;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IInput;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.graphql.tests.EditingContextEventSubscriptionRunner;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionInput;
import org.eclipse.sirius.components.graphql.tests.api.IExecuteEditingContextFunctionRunner;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.AllDiagramsBeforeMergeOfAllDiagramDescriptionsTestProjectData;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.ViewUsage;
import org.eclipse.syson.util.StandardDiagramsConstants;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;

/**
 * Tests for the migration participant allowing to update the diagramDescription property of exiting diagrams. Indeed,
 * InterconnectionViewDiagramDescription, ActionFlowViewDiagramDescription and StateTransitionViewdiagramDescription
 * have been deleted. The goal here is to retrieve all DiagramDescriptions that don't exists anymore and change them to
 * GeneralViewDiagramDescription.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AllDiagramsBeforeMergeOfAllDiagramDescriptionsMigrationParticipantTest extends AbstractIntegrationTests {

    @Autowired
    private EditingContextEventSubscriptionRunner editingContextEventSubscriptionRunner;

    @Autowired
    private IExecuteEditingContextFunctionRunner executeEditingContextFunctionRunner;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private IRepresentationSearchService representationSearchService;

    @Test
    @DisplayName("GIVEN a model with a all kind of diagrams, WHEN the model is loaded, THEN all diagrams are updated with the GeneralViewDiagramDescription Id")
    @Sql(scripts = { AllDiagramsBeforeMergeOfAllDiagramDescriptionsTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void migrationParticpantTest() {
        TestTransaction.flagForCommit();
        TestTransaction.end();
        var editingContextEventInput = new EditingContextEventInput(UUID.randomUUID(), AllDiagramsBeforeMergeOfAllDiagramDescriptionsTestProjectData.EDITING_CONTEXT_ID.toString());
        var flux = this.editingContextEventSubscriptionRunner.run(editingContextEventInput).flux();
        TestTransaction.flagForCommit();
        TestTransaction.end();

        BiFunction<IEditingContext, IInput, IPayload> checkFunction = (editingContext, executeEditingContextFunctionInput) -> {
            assertThat(this.testIsMigrationSuccessful(editingContext));
            return new SuccessPayload(executeEditingContextFunctionInput.id());
        };

        Runnable checkMigration = () -> {
            var checkInitialEditingContextInput = new ExecuteEditingContextFunctionInput(UUID.randomUUID(), AllDiagramsBeforeMergeOfAllDiagramDescriptionsTestProjectData.EDITING_CONTEXT_ID.toString(),
                    checkFunction);
            var payload = this.executeEditingContextFunctionRunner.execute(checkInitialEditingContextInput).block();
            assertThat(payload).isInstanceOf(SuccessPayload.class);
        };

        StepVerifier.create(flux)
                .then(checkMigration)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    private boolean testIsMigrationSuccessful(IEditingContext editingContext) {
        Package pkg1 = (Package) this.objectSearchService.getObject(editingContext, AllDiagramsBeforeMergeOfAllDiagramDescriptionsTestProjectData.SemanticIds.PACKAGE_1_ELEMENT_ID).get();

        var members = pkg1.getMember();
        assertTrue(members.stream().anyMatch(ViewUsage.class::isInstance));
        assertEquals(3, members.stream().filter(ViewUsage.class::isInstance).count());

        var optionalGVViewUsage = members.stream().filter(ViewUsage.class::isInstance).map(ViewUsage.class::cast)
                .filter(vu -> StandardDiagramsConstants.GV_QN.equals(vu.getType().get(0).getQualifiedName())).findFirst();
        assertTrue(optionalGVViewUsage.isPresent());

        var optionalGVDiagram = this.representationSearchService.findById(editingContext, AllDiagramsBeforeMergeOfAllDiagramDescriptionsTestProjectData.GraphicalIds.GV_DIAGRAM_ID, Diagram.class);
        assertTrue(optionalGVDiagram.isPresent());

        var gvDiagram = optionalGVDiagram.get();
        var gvDescriptionId = gvDiagram.getDescriptionId();
        assertEquals(SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID, gvDescriptionId);

        var optionalAFVViewUsage = members.stream().filter(ViewUsage.class::isInstance).map(ViewUsage.class::cast)
                .filter(vu -> StandardDiagramsConstants.AFV_QN.equals(vu.getType().get(0).getQualifiedName())).findFirst();
        assertTrue(optionalAFVViewUsage.isPresent());

        var optionalAFVDiagram = this.representationSearchService.findById(editingContext, AllDiagramsBeforeMergeOfAllDiagramDescriptionsTestProjectData.GraphicalIds.AFV_DIAGRAM_ID, Diagram.class);
        assertTrue(optionalAFVDiagram.isPresent());

        var afvDiagram = optionalAFVDiagram.get();
        var afvDescriptionId = afvDiagram.getDescriptionId();
        assertEquals(SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID, afvDescriptionId);

        var optionalSTVViewUsage = members.stream().filter(ViewUsage.class::isInstance).map(ViewUsage.class::cast)
                .filter(vu -> StandardDiagramsConstants.STV_QN.equals(vu.getType().get(0).getQualifiedName())).findFirst();
        assertTrue(optionalSTVViewUsage.isPresent());

        var optionalSTVDiagram = this.representationSearchService.findById(editingContext, AllDiagramsBeforeMergeOfAllDiagramDescriptionsTestProjectData.GraphicalIds.STV_DIAGRAM_ID, Diagram.class);
        assertTrue(optionalSTVDiagram.isPresent());

        var stvDiagram = optionalSTVDiagram.get();
        var stvDescriptionId = stvDiagram.getDescriptionId();
        assertEquals(SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID, stvDescriptionId);

        PartUsage part1 = (PartUsage) this.objectSearchService.getObject(editingContext, AllDiagramsBeforeMergeOfAllDiagramDescriptionsTestProjectData.SemanticIds.PART_1_ELEMENT_ID).get();

        members = part1.getMember();
        assertTrue(members.stream().anyMatch(ViewUsage.class::isInstance));
        assertEquals(1, members.stream().filter(ViewUsage.class::isInstance).count());

        var optionalIVViewUsage = members.stream().filter(ViewUsage.class::isInstance).map(ViewUsage.class::cast)
                .filter(vu -> StandardDiagramsConstants.IV_QN.equals(vu.getType().get(0).getQualifiedName())).findFirst();
        assertTrue(optionalIVViewUsage.isPresent());

        var optionalIVDiagram = this.representationSearchService.findById(editingContext, AllDiagramsBeforeMergeOfAllDiagramDescriptionsTestProjectData.GraphicalIds.IV_DIAGRAM_ID, Diagram.class);
        assertTrue(optionalIVDiagram.isPresent());

        var ivDiagram = optionalIVDiagram.get();
        var ivDescriptionId = ivDiagram.getDescriptionId();
        assertEquals(SysONRepresentationDescriptionIdentifiers.GENERAL_VIEW_DIAGRAM_DESCRIPTION_ID, ivDescriptionId);

        return true;
    }
}
