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
package org.eclipse.syson.tree.explorer.view.handlers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.trees.dto.DropTreeItemInput;
import org.eclipse.sirius.components.collaborative.trees.dto.TreeRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.ErrorPayload;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.trees.tests.graphql.DropTreeItemMutationRunner;
import org.eclipse.sirius.web.application.views.explorer.ExplorerEventInput;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.sirius.web.tests.services.explorer.ExplorerEventSubscriptionRunner;
import org.eclipse.sirius.web.tests.services.representation.RepresentationIdBuilder;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.SysMLv2Identifiers;
import org.eclipse.syson.tree.explorer.view.SysONTreeViewDescriptionProvider;
import org.eclipse.syson.tree.explorer.view.filters.SysONTreeFilterProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import graphql.execution.DataFetcherResult;
import reactor.test.StepVerifier;

/**
 * Integration tests for Explorer DnD.
 *
 * @author Arthur Daussy
 */
@Transactional
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DropTreeItemHandlerTest extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private ExplorerEventSubscriptionRunner treeEventSubscriptionRunner;

    @Autowired
    private DropTreeItemMutationRunner dropTreeItemMutationRunner;

    @Autowired
    private RepresentationIdBuilder representationIdBuilder;

    @Autowired
    private SysONTreeViewDescriptionProvider treeProvider;

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("Given the simple project, when drag an dropping a part definition on a package, then the part definition should be moved under the Package.")
    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void checkDnDPartDefinitionOnPackage() {

        var expandedIds = List.of(
                SysMLv2Identifiers.SIMPLE_PROJECT_PACKAGE1.toString(),
                SysMLv2Identifiers.SIMPLE_PROJECT_PACKAGE2.toString(),
                SysMLv2Identifiers.SIMPLE_PROJECT.toString(),
                SysMLv2Identifiers.SIMPLE_PROJECT_DOCUMENT.toString());

        var explorerRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.treeProvider.getDescriptionId(), expandedIds,
                List.of(SysONTreeFilterProvider.HIDE_MEMBERSHIPS_TREE_ITEM_FILTER_ID, SysONTreeFilterProvider.HIDE_ROOT_NAMESPACES_ID));
        var input = new ExplorerEventInput(UUID.randomUUID(), SysMLv2Identifiers.SIMPLE_PROJECT.toString(), explorerRepresentationId);
        var flux = this.treeEventSubscriptionRunner.run(input);

        Consumer<Object> initialTreeContentConsumer = object -> Optional.of(object)
                .filter(DataFetcherResult.class::isInstance)
                .map(DataFetcherResult.class::cast)
                .map(DataFetcherResult::getData)
                .filter(TreeRefreshedEventPayload.class::isInstance)
                .map(TreeRefreshedEventPayload.class::cast)
                .map(TreeRefreshedEventPayload::tree)
                .ifPresentOrElse(tree -> {
                    assertThat(tree).isNotNull();
                    assertThat(tree.getChildren().get(0).getChildren().get(0).getChildren()).hasSize(2);
                    assertThat(tree.getChildren().get(0).getChildren().get(0).getChildren()).anyMatch(treeItem -> treeItem.getId()
                            .equals(SysMLv2Identifiers.SIMPLE_PROJECT_PART.toString()));
                    assertThat(tree.getChildren().get(0).getChildren().get(1).getChildren()).hasSize(1);
                    assertThat(tree.getChildren().get(0).getChildren().get(1).getChildren()).anyMatch(treeItem -> treeItem.getId()
                            .equals(SysMLv2Identifiers.SIMPLE_PROJECT_PART_DEF.toString()));
                }, () -> fail("Missing tree"));

        Runnable dropItemMutation = () -> {
            DropTreeItemInput dropTreeItemInput = new DropTreeItemInput(
                    UUID.randomUUID(), SysMLv2Identifiers.SIMPLE_PROJECT.toString(),
                    explorerRepresentationId,
                    List.of(SysMLv2Identifiers.SIMPLE_PROJECT_PART_DEF.toString()),
                    SysMLv2Identifiers.SIMPLE_PROJECT_PACKAGE1.toString(),
                    -1);
            var result = this.dropTreeItemMutationRunner.run(dropTreeItemInput);
            String typename = JsonPath.read(result, "$.data.dropTreeItem.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());

            TestTransaction.flagForCommit();
            TestTransaction.end();
            TestTransaction.start();
        };

        Consumer<Object> updateTreeContentConsumer = object -> Optional.of(object)
                .filter(DataFetcherResult.class::isInstance)
                .map(DataFetcherResult.class::cast)
                .map(DataFetcherResult::getData)
                .filter(TreeRefreshedEventPayload.class::isInstance)
                .map(TreeRefreshedEventPayload.class::cast)
                .map(TreeRefreshedEventPayload::tree)
                .ifPresentOrElse(tree -> {
                    assertThat(tree).isNotNull();
                    assertThat(tree.getChildren().get(0).getChildren().get(0).getChildren()).hasSize(3);
                    assertThat(tree.getChildren().get(0).getChildren().get(0).getChildren()).anyMatch(treeItem -> treeItem.getId()
                            .equals(SysMLv2Identifiers.SIMPLE_PROJECT_PART_DEF.toString()));
                }, () -> fail("Missing tree"));

        StepVerifier.create(flux)
                .consumeNextWith(initialTreeContentConsumer)
                .then(dropItemMutation)
                .consumeNextWith(updateTreeContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));

    }

    @DisplayName("Given a SySML project, when drag an dropping an element into one of its descendant, then the selected element should not be moved")
    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void checkForbiddenDropOnDescendant() {

        var expandedIds = List.of(
                SysMLv2Identifiers.SIMPLE_PROJECT_PACKAGE1.toString(),
                SysMLv2Identifiers.SIMPLE_PROJECT_PACKAGE2.toString(),
                SysMLv2Identifiers.SIMPLE_PROJECT.toString(),
                SysMLv2Identifiers.SIMPLE_PROJECT_DOCUMENT.toString());

        var explorerRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.treeProvider.getDescriptionId(), expandedIds,
                List.of(SysONTreeFilterProvider.HIDE_MEMBERSHIPS_TREE_ITEM_FILTER_ID, SysONTreeFilterProvider.HIDE_ROOT_NAMESPACES_ID));
        var input = new ExplorerEventInput(UUID.randomUUID(), SysMLv2Identifiers.SIMPLE_PROJECT.toString(), explorerRepresentationId);
        var flux = this.treeEventSubscriptionRunner.run(input);

        Consumer<Object> initialTreeContentConsumer = object -> Optional.of(object)
                .filter(DataFetcherResult.class::isInstance)
                .map(DataFetcherResult.class::cast)
                .map(DataFetcherResult::getData)
                .filter(TreeRefreshedEventPayload.class::isInstance)
                .map(TreeRefreshedEventPayload.class::cast)
                .map(TreeRefreshedEventPayload::tree)
                .ifPresentOrElse(tree -> {
                    assertThat(tree).isNotNull();
                    assertThat(tree.getChildren().get(0).getChildren().get(0).getChildren()).hasSize(2);
                    assertThat(tree.getChildren().get(0).getChildren().get(0).getChildren()).anyMatch(treeItem -> treeItem.getId()
                            .equals(SysMLv2Identifiers.SIMPLE_PROJECT_PART.toString()));
                    assertThat(tree.getChildren().get(0).getChildren().get(1).getChildren()).hasSize(1);
                    assertThat(tree.getChildren().get(0).getChildren().get(1).getChildren()).anyMatch(treeItem -> treeItem.getId()
                            .equals(SysMLv2Identifiers.SIMPLE_PROJECT_PART_DEF.toString()));
                }, () -> fail("Missing tree"));

        Runnable dropItemMutation = () -> {
            DropTreeItemInput dropTreeItemInput = new DropTreeItemInput(
                    UUID.randomUUID(), SysMLv2Identifiers.SIMPLE_PROJECT.toString(),
                    explorerRepresentationId,
                    List.of(SysMLv2Identifiers.SIMPLE_PROJECT_PACKAGE1.toString()),
                    SysMLv2Identifiers.SIMPLE_PROJECT_PART.toString(),
                    -1);
            var result = this.dropTreeItemMutationRunner.run(dropTreeItemInput);
            String typename = JsonPath.read(result, "$.data.dropTreeItem.__typename");
            assertThat(typename).isEqualTo(ErrorPayload.class.getSimpleName());

            TestTransaction.flagForCommit();
            TestTransaction.end();
            TestTransaction.start();
        };


        StepVerifier.create(flux)
                .consumeNextWith(initialTreeContentConsumer)
                .then(dropItemMutation)
                .thenCancel()
                .verify(Duration.ofSeconds(10));

    }

}
