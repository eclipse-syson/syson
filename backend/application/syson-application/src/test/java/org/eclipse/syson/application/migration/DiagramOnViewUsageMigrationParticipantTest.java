/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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

import com.google.common.base.Objects;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;

import org.eclipse.sirius.components.collaborative.api.IRepresentationSearchService;
import org.eclipse.sirius.components.collaborative.dto.EditingContextEventInput;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IInput;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.ViewModifier;
import org.eclipse.sirius.components.graphql.tests.EditingContextEventSubscriptionRunner;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionInput;
import org.eclipse.sirius.components.graphql.tests.api.IExecuteEditingContextFunctionRunner;
import org.eclipse.sirius.web.tests.services.api.IGivenCommittedTransaction;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.DiagramOnViewUsageMigrationParticipantTestProjectData;
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.ViewUsage;
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
 * Tests for all migration participant related to moving existing diagrams associated to Elements that are not ViewUsage
 * to a newly created ViewUsage.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DiagramOnViewUsageMigrationParticipantTest extends AbstractIntegrationTests {

    @Autowired
    private IGivenCommittedTransaction givenCommittedTransaction;

    @Autowired
    private EditingContextEventSubscriptionRunner editingContextEventSubscriptionRunner;

    @Autowired
    private IExecuteEditingContextFunctionRunner executeEditingContextFunctionRunner;

    @Autowired
    private IObjectService objectService;

    @Autowired
    private IRepresentationSearchService representationSearchService;

    @Test
    @DisplayName("GIVEN a project with a diagram associated to a Package, WHEN the model is loaded, THEN a ViewUsage is created under the Package and the diagram is now associated to this new ViewUsage")
    @Sql(scripts = { DiagramOnViewUsageMigrationParticipantTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void migrationParticpantTest() {
        this.givenCommittedTransaction.commit();
        var editingContextEventInput = new EditingContextEventInput(UUID.randomUUID(), DiagramOnViewUsageMigrationParticipantTestProjectData.EDITING_CONTEXT_ID.toString());
        var flux = this.editingContextEventSubscriptionRunner.run(editingContextEventInput);
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        BiFunction<IEditingContext, IInput, IPayload> checkFunction = (editingContext, executeEditingContextFunctionInput) -> {
            assertThat(this.testIsMigrationSuccessful(editingContext));
            return new SuccessPayload(executeEditingContextFunctionInput.id());
        };

        Runnable checkMigration = () -> {
            var checkInitialEditingContextInput = new ExecuteEditingContextFunctionInput(UUID.randomUUID(), DiagramOnViewUsageMigrationParticipantTestProjectData.EDITING_CONTEXT_ID.toString(),
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
        Package pkg1 = (Package) this.objectService.getObject(editingContext, DiagramOnViewUsageMigrationParticipantTestProjectData.SemanticIds.PACKAGE_1_ID).get();

        var members = pkg1.getMember();
        assertTrue(members.stream().anyMatch(ViewUsage.class::isInstance));
        assertEquals(1, members.stream().filter(ViewUsage.class::isInstance).count());

        var optionalViewUsage = members.stream().filter(ViewUsage.class::isInstance).map(ViewUsage.class::cast).findFirst();
        assertTrue(optionalViewUsage.isPresent());

        var optionalDiagram = this.representationSearchService.findById(editingContext, DiagramOnViewUsageMigrationParticipantTestProjectData.GraphicalIds.DIAGRAM_ID, Diagram.class);
        assertTrue(optionalDiagram.isPresent());

        var diagram = optionalDiagram.get();
        var targetObjectId = diagram.getTargetObjectId();

        var viewUsage = optionalViewUsage.get();
        assertEquals(this.objectService.getId(viewUsage), targetObjectId);

        // diagram contains partA (PartUsage), partB (PartUsage), PartDefinition1 (PartDefinition) as root nodes (3)
        var rootNodes = diagram.getNodes();
        assertEquals(3, rootNodes.size());

        // partA graphical node contains documentation, states, flow states as compartments visible graphical nodes
        // (3)
        for (Node node : rootNodes) {
            if (Objects.equal("partA", node.getTargetObjectLabel())) {
                List<Node> compartmentNodes = node.getChildNodes().stream().filter(childNode -> Objects.equal(ViewModifier.Normal, childNode.getState())).toList();
                assertEquals(3, compartmentNodes.size());
                // each compartment graphical node contains one graphical node (3)
                for (Node compartmentNode : compartmentNodes) {
                    assertEquals(1, compartmentNode.getChildNodes().size());
                }
            }
        }

        // diagram contains one dependency graphical edge and one composition graphical edge
        assertEquals(2, diagram.getEdges().size());
        return true;
    }
}
