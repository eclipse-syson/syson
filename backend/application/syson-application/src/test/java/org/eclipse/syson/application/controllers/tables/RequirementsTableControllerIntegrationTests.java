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
import org.eclipse.sirius.components.collaborative.tables.dto.InvokeToolMenuEntryInput;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.tables.TextareaCell;
import org.eclipse.sirius.components.tables.TextfieldCell;
import org.eclipse.sirius.components.tables.tests.graphql.InvokeRowContextMenuEntryMutationRunner;
import org.eclipse.sirius.components.tables.tests.graphql.InvokeToolMenuEntryMutationRunner;
import org.eclipse.sirius.components.tables.tests.graphql.RowContextMenuQueryRunner;
import org.eclipse.sirius.components.tables.tests.graphql.TableEventSubscriptionRunner;
import org.eclipse.sirius.components.tables.tests.graphql.ToolMenuEntriesQueryRunner;
import org.eclipse.sirius.web.tests.services.api.IGivenCreatedTableSubscription;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.sirius.web.tests.services.representation.RepresentationIdBuilder;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.controller.editingcontext.checkers.ISemanticChecker;
import org.eclipse.syson.application.controller.editingcontext.checkers.SemanticCheckerService;
import org.eclipse.syson.application.data.RequirementsTableTestProjectData;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.table.requirements.view.RTVTableToolMenuEntriesProvider;
import org.eclipse.syson.util.SysONRepresentationDescriptionIdentifiers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.transaction.TestTransaction;
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

    public static final String REQUIREMENT_2_LABEL = "requirement2";
    public static final String REQUIREMENT_1_LABEL = "requirement1";

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenCreatedTableSubscription givenCreatedTableSubscription;

    @Autowired
    private InvokeToolMenuEntryMutationRunner invokeToolMenuEntryMutationRunner;

    @Autowired
    private RowContextMenuQueryRunner rowContextMenuQueryRunner;

    @Autowired
    private InvokeRowContextMenuEntryMutationRunner invokeRowContextMenuEntryMutationRunner;

    @Autowired
    private TableEventSubscriptionRunner tableEventSubscriptionRunner;

    @Autowired
    private ToolMenuEntriesQueryRunner toolMenuEntriesQueryRunner;

    @Autowired
    private RepresentationIdBuilder representationIdBuilder;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private IObjectSearchService objectSearchService;

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
        return this.givenCreatedTableSubscription.createAndSubscribe(input).flux();
    }

    private Flux<Object> givenSubscriptionToExistingViewTableRepresentation() {
        var tableEventInput = new TableEventInput(UUID.randomUUID(), RequirementsTableTestProjectData.EDITING_CONTEXT_ID, RequirementsTableTestProjectData.GraphicalIds.TABLE_ID);
        var flux = this.tableEventSubscriptionRunner.run(tableEventInput).flux();

        TestTransaction.flagForCommit();
        TestTransaction.end();

        return flux;
    }

    @DisplayName("GIVEN a new requirements view table description, WHEN a subscription is created, THEN the table is render")
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

    @DisplayName("GIVEN a existing requirements view table description, WHEN a subscription is created, THEN the table is render")
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

    @DisplayName("GIVEN a requirements view table, WHEN the create requirement table action invoked, THEN the create requirement table action is correctly executed")
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
            var invokeToolMenuEntryInput = new InvokeToolMenuEntryInput(
                    UUID.randomUUID(),
                    RequirementsTableTestProjectData.EDITING_CONTEXT_ID,
                    tableId.get(),
                    tableId.get(),
                    RTVTableToolMenuEntriesProvider.ADD_REQUIREMENT_TABLE_TOOL_ENTRY);

            var result = this.invokeToolMenuEntryMutationRunner.run(invokeToolMenuEntryInput);
            String typename = JsonPath.read(result.data(), "$.data.invokeToolMenuEntry.__typename");
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

    @DisplayName("GIVEN a requirements view table, WHEN the expose requirements table action invoked, THEN the expose requirements table action is correctly executed")
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
            var invokeToolMenuEntryInput = new InvokeToolMenuEntryInput(
                    UUID.randomUUID(),
                    RequirementsTableTestProjectData.EDITING_CONTEXT_ID,
                    tableId.get(),
                    tableId.get(),
                    RTVTableToolMenuEntriesProvider.IMPORT_EXISTING_REQUIREMENTS_TABLE_TOOL_ENTRY);

            var result = this.invokeToolMenuEntryMutationRunner.run(invokeToolMenuEntryInput);
            String typename = JsonPath.read(result.data(), "$.data.invokeToolMenuEntry.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedTableContentConsumer = assertRefreshedTableThat(table -> {
            assertThat(table).isNotNull();
            assertThat(table.getLines()).hasSize(2);
            assertThat(((TextfieldCell) table.getLines().get(0).getCells().get(0)).getValue()).isEqualTo(REQUIREMENT_1_LABEL);
            assertThat(((TextfieldCell) table.getLines().get(0).getCells().get(1)).getValue()).isEqualTo("ReqR1");
            assertThat(((TextareaCell) table.getLines().get(0).getCells().get(2)).getValue()).isEqualTo("doc R1");
            assertThat(((TextfieldCell) table.getLines().get(1).getCells().get(0)).getValue()).isEqualTo(REQUIREMENT_2_LABEL);
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

    @DisplayName("GIVEN a requirements view table, WHEN the delete from table row action invoked, THEN the delete from table row action is correctly executed")
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
            var invokeToolMenuEntryInput = new InvokeToolMenuEntryInput(
                    UUID.randomUUID(),
                    RequirementsTableTestProjectData.EDITING_CONTEXT_ID,
                    tableId.get(),
                    tableId.get(),
                    RTVTableToolMenuEntriesProvider.IMPORT_EXISTING_REQUIREMENTS_TABLE_TOOL_ENTRY);

            var result = this.invokeToolMenuEntryMutationRunner.run(invokeToolMenuEntryInput);
            String typename = JsonPath.read(result.data(), "$.data.invokeToolMenuEntry.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedTableContentConsumerFirst = assertRefreshedTableThat(table -> {
            assertThat(table).isNotNull();
            assertThat(table.getLines()).hasSize(2);
            assertThat(((TextfieldCell) table.getLines().get(0).getCells().get(0)).getValue()).isEqualTo(REQUIREMENT_1_LABEL);
            assertThat(((TextfieldCell) table.getLines().get(1).getCells().get(0)).getValue()).isEqualTo(REQUIREMENT_2_LABEL);
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
            List<String> actionLabels = JsonPath.read(result.data(), "$.data.viewer.editingContext.representation.description.rowContextMenuEntries[*].label");
            assertThat(actionLabels).isNotEmpty().hasSize(3);
            assertThat(actionLabels.get(0)).isEqualTo("Delete from model");
            assertThat(actionLabels.get(1)).isEqualTo("Delete from table");
            assertThat(actionLabels.get(2)).isEqualTo("New Nested Requirement");

            List<String> actionIds = JsonPath.read(result.data(), "$.data.viewer.editingContext.representation.description.rowContextMenuEntries[*].id");
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
            String typename = JsonPath.read(result.data(), "$.data.invokeRowContextMenuEntry.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedTableContentConsumerSecond = assertRefreshedTableThat(table -> {
            assertThat(table).isNotNull();
            assertThat(table.getLines()).hasSize(1);
            assertThat(((TextfieldCell) table.getLines().get(0).getCells().get(0)).getValue()).isEqualTo(REQUIREMENT_2_LABEL);
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

    @DisplayName("GIVEN a requirements view table, WHEN the delete from model row action invoked, THEN the delete from model row action is correctly executed")
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
            var invokeToolMenuEntryInput = new InvokeToolMenuEntryInput(
                    UUID.randomUUID(),
                    RequirementsTableTestProjectData.EDITING_CONTEXT_ID,
                    tableId.get(),
                    tableId.get(),
                    RTVTableToolMenuEntriesProvider.IMPORT_EXISTING_REQUIREMENTS_TABLE_TOOL_ENTRY);

            var result = this.invokeToolMenuEntryMutationRunner.run(invokeToolMenuEntryInput);
            String typename = JsonPath.read(result.data(), "$.data.invokeToolMenuEntry.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedTableContentConsumerFirst = assertRefreshedTableThat(table -> {
            assertThat(table).isNotNull();
            assertThat(table.getLines()).hasSize(2);
            assertThat(((TextfieldCell) table.getLines().get(0).getCells().get(0)).getValue()).isEqualTo(REQUIREMENT_1_LABEL);
            assertThat(((TextfieldCell) table.getLines().get(1).getCells().get(0)).getValue()).isEqualTo(REQUIREMENT_2_LABEL);
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
            List<String> actionLabels = JsonPath.read(result.data(), "$.data.viewer.editingContext.representation.description.rowContextMenuEntries[*].label");
            assertThat(actionLabels).isNotEmpty().hasSize(3);
            assertThat(actionLabels.get(0)).isEqualTo("Delete from model");
            assertThat(actionLabels.get(1)).isEqualTo("Delete from table");
            assertThat(actionLabels.get(2)).isEqualTo("New Nested Requirement");

            List<String> actionIds = JsonPath.read(result.data(), "$.data.viewer.editingContext.representation.description.rowContextMenuEntries[*].id");
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
            String typename = JsonPath.read(result.data(), "$.data.invokeRowContextMenuEntry.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedTableContentConsumerSecond = assertRefreshedTableThat(table -> {
            assertThat(table).isNotNull();
            assertThat(table.getLines()).hasSize(1);
            assertThat(((TextfieldCell) table.getLines().get(0).getCells().get(0)).getValue()).isEqualTo(REQUIREMENT_2_LABEL);
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

    @Test
    @GivenSysONServer({ RequirementsTableTestProjectData.SCRIPT_PATH })
    @DisplayName("GIVEN a requirements view table, WHEN the new nested requirement row action invoked, THEN the new nested requirement row action creates a nested Requirement below the given Requirement")
    public void testNewNestedRequirement() {
        var flux = this.givenSubscriptionToExistingViewTableRepresentation();
        var tableId = new AtomicReference<String>();
        var rowId = new AtomicReference<UUID>();

        Consumer<Object> tableContentConsumer = assertRefreshedTableThat(table -> {
            assertThat(table).isNotNull();
            assertThat(table.getLines()).hasSize(0);
            tableId.set(table.getId());
        });

        Runnable exposeRequirementsTask = () -> {
            var invokeToolMenuEntryInput = new InvokeToolMenuEntryInput(
                    UUID.randomUUID(),
                    RequirementsTableTestProjectData.EDITING_CONTEXT_ID,
                    tableId.get(),
                    tableId.get(),
                    RTVTableToolMenuEntriesProvider.IMPORT_EXISTING_REQUIREMENTS_TABLE_TOOL_ENTRY);

            var result = this.invokeToolMenuEntryMutationRunner.run(invokeToolMenuEntryInput);
            String typename = JsonPath.read(result.data(), "$.data.invokeToolMenuEntry.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedTableContentConsumerFirst = assertRefreshedTableThat(table -> {
            assertThat(table).isNotNull();
            assertThat(table.getLines()).hasSize(2);
            assertThat(table.getLines().get(0).isHasChildren()).isFalse();
            assertThat(table.getLines().get(1).isHasChildren()).isFalse();
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
            List<String> actionLabels = JsonPath.read(result.data(), "$.data.viewer.editingContext.representation.description.rowContextMenuEntries[*].label");
            assertThat(actionLabels).isNotEmpty().hasSize(3);
            assertThat(actionLabels.get(0)).isEqualTo("Delete from model");
            assertThat(actionLabels.get(1)).isEqualTo("Delete from table");
            assertThat(actionLabels.get(2)).isEqualTo("New Nested Requirement");

            List<String> actionIds = JsonPath.read(result.data(), "$.data.viewer.editingContext.representation.description.rowContextMenuEntries[*].id");
            actionId.set(actionIds.get(2));
        };

        Runnable invokeNewNestedRequirementAction = () -> {
            var invokeRowContextMenuEntryInput = new InvokeRowContextMenuEntryInput(
                    UUID.randomUUID(),
                    RequirementsTableTestProjectData.EDITING_CONTEXT_ID,
                    tableId.get(),
                    tableId.get(),
                    rowId.get(),
                    actionId.get());

            var result = this.invokeRowContextMenuEntryMutationRunner.run(invokeRowContextMenuEntryInput);
            String typename = JsonPath.read(result.data(), "$.data.invokeRowContextMenuEntry.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedTableContentConsumerSecond = assertRefreshedTableThat(table -> {
            assertThat(table).isNotNull();
            assertThat(table.getLines()).hasSize(2);
            assertThat(table.getLines().get(0).isHasChildren()).isTrue(); // now this requirement has a child
            assertThat(table.getLines().get(1).isHasChildren()).isFalse();
        });

        SemanticCheckerService semanticCheckerService = new SemanticCheckerService(this.semanticRunnableFactory, this.objectSearchService, RequirementsTableTestProjectData.EDITING_CONTEXT_ID, RequirementsTableTestProjectData.SemanticIds.REQUIREMENT_1_ID);
        ISemanticChecker semanticChecker = semanticCheckerService.getElementInParentSemanticChecker(REQUIREMENT_1_LABEL, SysmlPackage.eINSTANCE.getNamespace_OwnedMember(), SysmlPackage.eINSTANCE.getRequirementUsage());

        Runnable editingContextChecker = semanticCheckerService.checkEditingContext(semanticChecker);

        StepVerifier.create(flux)
                .consumeNextWith(tableContentConsumer)
                .then(exposeRequirementsTask)
                .consumeNextWith(updatedTableContentConsumerFirst)
                .then(getContextMenuEntriesTask)
                .then(invokeNewNestedRequirementAction)
                .consumeNextWith(updatedTableContentConsumerSecond)
                .then(editingContextChecker)
                .thenCancel()
                .verify(Duration.ofSeconds(10));

        this.expandRequirement1Test(tableId);
    }

    private void expandRequirement1Test(AtomicReference<String> tableId) {
        String representationId = this.representationIdBuilder.buildTableRepresentationId(tableId.get(), null, "NEXT", 10, List.of(RequirementsTableTestProjectData.SemanticIds.REQUIREMENT_1_ID), List.of(), List.of());
        var tableEventInput = new TableEventInput(UUID.randomUUID(), RequirementsTableTestProjectData.EDITING_CONTEXT_ID, representationId);
        var expandedFlux = this.tableEventSubscriptionRunner.run(tableEventInput).flux();

        TestTransaction.start();

        Consumer<Object> expandedTableContentConsumer = assertRefreshedTableThat(table -> {
            assertThat(table).isNotNull();
            assertThat(table.getLines()).hasSize(3);
            assertThat(table.getLines().get(0).getDepthLevel()).isEqualTo(0);
            assertThat(table.getLines().get(1).getDepthLevel()).isEqualTo(1);
            assertThat(table.getLines().get(2).getDepthLevel()).isEqualTo(0);
        });

        StepVerifier.create(expandedFlux)
                .consumeNextWith(expandedTableContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @GivenSysONServer({ RequirementsTableTestProjectData.SCRIPT_PATH })
    @DisplayName("GIVEN a requirements view table, WHEN tool menu entries query is triggered, THEN all related tools are returned")
    public void testToolMenuEntriesQuery() {

        Map<String, Object> variables = Map.of(
                "editingContextId", RequirementsTableTestProjectData.EDITING_CONTEXT_ID,
                "representationId", RequirementsTableTestProjectData.GraphicalIds.TABLE_ID,
                "tableId", RequirementsTableTestProjectData.GraphicalIds.TABLE_ID
        );
        var result = this.toolMenuEntriesQueryRunner.run(variables);

        List<String> toolMenuEntriesId = JsonPath.read(result.data(), "$.data.viewer.editingContext.representation.description.toolMenuEntries[*].id");
        assertThat(toolMenuEntriesId).containsExactlyInAnyOrder("add-requirement-table-tool-entry", "import-existing-requirements-table-tool-entry");
    }
}
