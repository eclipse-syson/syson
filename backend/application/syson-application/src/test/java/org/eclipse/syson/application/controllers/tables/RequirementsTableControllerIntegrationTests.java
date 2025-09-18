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
package org.eclipse.syson.application.controllers.tables;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.components.tables.tests.TableEventPayloadConsumer.assertRefreshedTableThat;

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.dto.CreateRepresentationInput;
import org.eclipse.sirius.components.collaborative.tables.TableEventInput;
import org.eclipse.sirius.components.collaborative.tables.dto.InvokeRowContextMenuEntryInput;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.tables.TextareaCell;
import org.eclipse.sirius.components.tables.TextfieldCell;
import org.eclipse.sirius.components.tables.tests.graphql.InvokeRowContextMenuEntryMutationRunner;
import org.eclipse.sirius.components.tables.tests.graphql.RowContextMenuQueryRunner;
import org.eclipse.sirius.components.tables.tests.graphql.TableEventSubscriptionRunner;
import org.eclipse.sirius.web.tests.services.api.IGivenCommittedTransaction;
import org.eclipse.sirius.web.tests.services.api.IGivenCreatedTableSubscription;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controllers.diagrams.graphql.CreateRequirementMutationRunner;
import org.eclipse.syson.application.controllers.diagrams.graphql.ExposeRequirementsMutationRunner;
import org.eclipse.syson.application.data.RequirementsTableTestProjectData;
import org.eclipse.syson.table.requirements.view.dto.CreateRequirementInput;
import org.eclipse.syson.table.requirements.view.dto.ExposeRequirementsInput;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Integration tests of the Requirements view table description.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RequirementsTableControllerIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenCommittedTransaction givenCommittedTransaction;

    @Autowired
    private IGivenCreatedTableSubscription givenCreatedTableSubscription;

    @Autowired
    private CreateRequirementMutationRunner createRequirementMutationRunner;

    @Autowired
    private ExposeRequirementsMutationRunner exposeRequirementsMutationRunner;

    @Autowired
    private RowContextMenuQueryRunner rowContextMenuQueryRunner;

    @Autowired
    private InvokeRowContextMenuEntryMutationRunner invokeRowContextMenuEntryMutationRunner;

    @Autowired
    private TableEventSubscriptionRunner tableEventSubscriptionRunner;

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    private Flux<Object> givenSubscriptionToNewViewTableRepresentation() {
        var input = new CreateRepresentationInput(
                UUID.randomUUID(),
                RequirementsTableTestProjectData.EDITING_CONTEXT_ID,
                SysONRepresentationDescriptionIdentifiers.REQUIREMENTS_TABLE_VIEW_DESCRIPTION_ID,
                RequirementsTableTestProjectData.SemanticIds.VIEW_1_ELEMENT_ID,
                "NewRequirementsTableView");
        return this.givenCreatedTableSubscription.createAndSubscribe(input);
    }

    private Flux<Object> givenSubscriptionToExistingViewTableRepresentation() {
        this.givenCommittedTransaction.commit();

        var tableEventInput = new TableEventInput(UUID.randomUUID(), RequirementsTableTestProjectData.EDITING_CONTEXT_ID, RequirementsTableTestProjectData.GraphicalIds.TABLE_ID);
        var flux = this.tableEventSubscriptionRunner.run(tableEventInput);

        this.givenCommittedTransaction.commit();

        return flux;
    }

    @DisplayName("Given a new requirements view table description, when a subscription is created, then the table is render")
    @Sql(scripts = { RequirementsTableTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void givenNewRequirementsViewTableDescriptionWhenSubscriptionIsCreatedThenTableIsRender() {
        var flux = this.givenSubscriptionToNewViewTableRepresentation();

        Consumer<Object> tableContentConsumer = assertRefreshedTableThat(table -> {
            assertThat(table).isNotNull();

            assertThat(table.getColumns()).hasSize(3);
            assertThat(table.getColumns().get(0).getHeaderLabel()).isEqualTo("DeclaredName");
            assertThat(table.getColumns().get(0).getHeaderIndexLabel()).isEqualTo("");
            assertThat(table.getColumns().get(1).getHeaderLabel()).isEqualTo("ReqId");
            assertThat(table.getColumns().get(1).getHeaderIndexLabel()).isEqualTo("");
            assertThat(table.getColumns().get(2).getHeaderLabel()).isEqualTo("Documentation");
            assertThat(table.getColumns().get(2).getHeaderIndexLabel()).isEqualTo("");

            assertThat(table.getLines()).hasSize(0);
        });

        StepVerifier.create(flux)
                .consumeNextWith(tableContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("Given a existing requirements view table description, when a subscription is created, then the table is render")
    @Sql(scripts = { RequirementsTableTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void givenRequirementsViewTableDescriptionWhenSubscriptionIsCreatedThenTableIsRender() {
        var flux = this.givenSubscriptionToExistingViewTableRepresentation();

        Consumer<Object> tableContentConsumer = assertRefreshedTableThat(table -> {
            assertThat(table).isNotNull();

            assertThat(table.getColumns()).hasSize(3);
            assertThat(table.getColumns().get(0).getHeaderLabel()).isEqualTo("DeclaredName");
            assertThat(table.getColumns().get(0).getHeaderIndexLabel()).isEqualTo("");
            assertThat(table.getColumns().get(1).getHeaderLabel()).isEqualTo("ReqId");
            assertThat(table.getColumns().get(1).getHeaderIndexLabel()).isEqualTo("");
            assertThat(table.getColumns().get(2).getHeaderLabel()).isEqualTo("Documentation");
            assertThat(table.getColumns().get(2).getHeaderIndexLabel()).isEqualTo("");

            assertThat(table.getLines()).hasSize(0);
        });

        StepVerifier.create(flux)
                .consumeNextWith(tableContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("Given a requirements view table, when the create requirement table action invoked, then the create requirement table action is correctly executed")
    @Sql(scripts = { RequirementsTableTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void testCreateRequirementTableAction() {
        var flux = this.givenSubscriptionToNewViewTableRepresentation();

        var tableId = new AtomicReference<String>();
        Consumer<Object> tableContentConsumer = assertRefreshedTableThat(table -> {
            assertThat(table).isNotNull();
            assertThat(table.getLines()).hasSize(0);
            tableId.set(table.getId());
        });

        Runnable createRequirementTask = () -> {
            var createRequirementInput = new CreateRequirementInput(
                    UUID.randomUUID(),
                    RequirementsTableTestProjectData.EDITING_CONTEXT_ID,
                    tableId.get());

            var result = this.createRequirementMutationRunner.run(createRequirementInput);
            String typename = JsonPath.read(result, "$.data.createRequirement.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedTableContentConsumer = assertRefreshedTableThat(table -> {
            assertThat(table).isNotNull();
            assertThat(table.getLines()).hasSize(1);
            assertThat(((TextfieldCell) table.getLines().get(0).getCells().get(0)).getValue()).isEqualTo("requirement3");
        });

        StepVerifier.create(flux)
                .consumeNextWith(tableContentConsumer)
                .then(createRequirementTask)
                .consumeNextWith(updatedTableContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("Given a requirements view table, when the expose requirements table action invoked, then the expose requirements table action is correctly executed")
    @Sql(scripts = { RequirementsTableTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void testExposeRequirementsTableAction() {
        var flux = this.givenSubscriptionToExistingViewTableRepresentation();

        var tableId = new AtomicReference<String>();
        Consumer<Object> tableContentConsumer = assertRefreshedTableThat(table -> {
            assertThat(table).isNotNull();
            assertThat(table.getLines()).hasSize(0);
            tableId.set(table.getId());
        });

        Runnable exposeRequirementsTask = () -> {
            var createRequirementInput = new ExposeRequirementsInput(
                    UUID.randomUUID(),
                    RequirementsTableTestProjectData.EDITING_CONTEXT_ID,
                    tableId.get());

            var result = this.exposeRequirementsMutationRunner.run(createRequirementInput);
            String typename = JsonPath.read(result, "$.data.exposeRequirements.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedTableContentConsumer = assertRefreshedTableThat(table -> {
            assertThat(table).isNotNull();
            assertThat(table.getLines()).hasSize(2);
            assertThat(((TextfieldCell) table.getLines().get(0).getCells().get(0)).getValue()).isEqualTo("requirement1");
            assertThat(((TextfieldCell) table.getLines().get(0).getCells().get(1)).getValue()).isEqualTo("ReqR1");
            assertThat(((TextareaCell) table.getLines().get(0).getCells().get(2)).getValue()).isEqualTo("doc R1");
            assertThat(((TextfieldCell) table.getLines().get(1).getCells().get(0)).getValue()).isEqualTo("requirement2");
            assertThat(((TextfieldCell) table.getLines().get(1).getCells().get(1)).getValue()).isEqualTo("ReqR2");
            assertThat(((TextareaCell) table.getLines().get(1).getCells().get(2)).getValue()).isEqualTo("doc R2");
        });

        StepVerifier.create(flux)
                .consumeNextWith(tableContentConsumer)
                .then(exposeRequirementsTask)
                .consumeNextWith(updatedTableContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("Given a requirements view table, when the delete from table row action invoked, then the delete from table row action is correctly executed")
    @Sql(scripts = { RequirementsTableTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void testDeleteFromTable() {
        var flux = this.givenSubscriptionToExistingViewTableRepresentation();

        var tableId = new AtomicReference<String>();
        var rowId = new AtomicReference<UUID>();

        Consumer<Object> tableContentConsumer = assertRefreshedTableThat(table -> {
            assertThat(table).isNotNull();
            assertThat(table.getLines()).hasSize(0);
            tableId.set(table.getId());
        });

        Runnable exposeRequirementsTask = () -> {
            var createRequirementInput = new ExposeRequirementsInput(
                    UUID.randomUUID(),
                    RequirementsTableTestProjectData.EDITING_CONTEXT_ID,
                    tableId.get());

            var result = this.exposeRequirementsMutationRunner.run(createRequirementInput);
            String typename = JsonPath.read(result, "$.data.exposeRequirements.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedTableContentConsumerFirst = assertRefreshedTableThat(table -> {
            assertThat(table).isNotNull();
            assertThat(table.getLines()).hasSize(2);
            assertThat(((TextfieldCell) table.getLines().get(0).getCells().get(0)).getValue()).isEqualTo("requirement1");
            assertThat(((TextfieldCell) table.getLines().get(1).getCells().get(0)).getValue()).isEqualTo("requirement2");
            rowId.set(table.getLines().get(0).getId());
        });

        var actionId = new AtomicReference<String>();
        Runnable getContextMenuEntriesTask = () -> {
            Map<String, Object> variables = Map.of(
                    "editingContextId", RequirementsTableTestProjectData.EDITING_CONTEXT_ID,
                    "representationId", tableId.get(),
                    "tableId", tableId.get(),
                    "rowId", rowId.get().toString());

            var result = this.rowContextMenuQueryRunner.run(variables);
            List<String> actionLabels = JsonPath.read(result, "$.data.viewer.editingContext.representation.description.rowContextMenuEntries[*].label");
            assertThat(actionLabels).isNotEmpty().hasSize(2);
            assertThat(actionLabels.get(0)).isEqualTo("Delete from model");
            assertThat(actionLabels.get(1)).isEqualTo("Delete from table");

            List<String> actionIds = JsonPath.read(result, "$.data.viewer.editingContext.representation.description.rowContextMenuEntries[*].id");
            actionId.set(actionIds.get(1));
        };

        Runnable invokeDeleteFromTableAction = () -> {
            var invokeRowContextMenuEntryInput = new InvokeRowContextMenuEntryInput(
                    UUID.randomUUID(),
                    RequirementsTableTestProjectData.EDITING_CONTEXT_ID,
                    tableId.get(),
                    tableId.get(),
                    rowId.get(),
                    actionId.get());

            var result = this.invokeRowContextMenuEntryMutationRunner.run(invokeRowContextMenuEntryInput);
            String typename = JsonPath.read(result, "$.data.invokeRowContextMenuEntry.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedTableContentConsumerSecond = assertRefreshedTableThat(table -> {
            assertThat(table).isNotNull();
            assertThat(table.getLines()).hasSize(1);
            assertThat(((TextfieldCell) table.getLines().get(0).getCells().get(0)).getValue()).isEqualTo("requirement2");
        });

        StepVerifier.create(flux)
                .consumeNextWith(tableContentConsumer)
                .then(exposeRequirementsTask)
                .consumeNextWith(updatedTableContentConsumerFirst)
                .then(getContextMenuEntriesTask)
                .then(invokeDeleteFromTableAction)
                .consumeNextWith(updatedTableContentConsumerSecond)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("Given a requirements view table, when the delete from model row action invoked, then the delete from model row action is correctly executed")
    @Sql(scripts = { RequirementsTableTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void testDeleteFromModel() {
        var flux = this.givenSubscriptionToExistingViewTableRepresentation();

        var tableId = new AtomicReference<String>();
        var rowId = new AtomicReference<UUID>();

        Consumer<Object> tableContentConsumer = assertRefreshedTableThat(table -> {
            assertThat(table).isNotNull();
            assertThat(table.getLines()).hasSize(0);
            tableId.set(table.getId());
        });

        Runnable exposeRequirementsTask = () -> {
            var createRequirementInput = new ExposeRequirementsInput(
                    UUID.randomUUID(),
                    RequirementsTableTestProjectData.EDITING_CONTEXT_ID,
                    tableId.get());

            var result = this.exposeRequirementsMutationRunner.run(createRequirementInput);
            String typename = JsonPath.read(result, "$.data.exposeRequirements.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedTableContentConsumerFirst = assertRefreshedTableThat(table -> {
            assertThat(table).isNotNull();
            assertThat(table.getLines()).hasSize(2);
            assertThat(((TextfieldCell) table.getLines().get(0).getCells().get(0)).getValue()).isEqualTo("requirement1");
            assertThat(((TextfieldCell) table.getLines().get(1).getCells().get(0)).getValue()).isEqualTo("requirement2");
            rowId.set(table.getLines().get(0).getId());
        });

        var actionId = new AtomicReference<String>();
        Runnable getContextMenuEntriesTask = () -> {
            Map<String, Object> variables = Map.of(
                    "editingContextId", RequirementsTableTestProjectData.EDITING_CONTEXT_ID,
                    "representationId", tableId.get(),
                    "tableId", tableId.get(),
                    "rowId", rowId.get().toString());

            var result = this.rowContextMenuQueryRunner.run(variables);
            List<String> actionLabels = JsonPath.read(result, "$.data.viewer.editingContext.representation.description.rowContextMenuEntries[*].label");
            assertThat(actionLabels).isNotEmpty().hasSize(2);
            assertThat(actionLabels.get(0)).isEqualTo("Delete from model");

            List<String> actionIds = JsonPath.read(result, "$.data.viewer.editingContext.representation.description.rowContextMenuEntries[*].id");
            actionId.set(actionIds.get(0));
        };

        Runnable invokeDeleteFromModelAction = () -> {
            var invokeRowContextMenuEntryInput = new InvokeRowContextMenuEntryInput(
                    UUID.randomUUID(),
                    RequirementsTableTestProjectData.EDITING_CONTEXT_ID,
                    tableId.get(),
                    tableId.get(),
                    rowId.get(),
                    actionId.get());

            var result = this.invokeRowContextMenuEntryMutationRunner.run(invokeRowContextMenuEntryInput);
            String typename = JsonPath.read(result, "$.data.invokeRowContextMenuEntry.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedTableContentConsumerSecond = assertRefreshedTableThat(table -> {
            assertThat(table).isNotNull();
            assertThat(table.getLines()).hasSize(1);
            assertThat(((TextfieldCell) table.getLines().get(0).getCells().get(0)).getValue()).isEqualTo("requirement2");
        });

        StepVerifier.create(flux)
                .consumeNextWith(tableContentConsumer)
                .then(exposeRequirementsTask)
                .consumeNextWith(updatedTableContentConsumerFirst)
                .then(getContextMenuEntriesTask)
                .then(invokeDeleteFromModelAction)
                .consumeNextWith(updatedTableContentConsumerSecond)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
