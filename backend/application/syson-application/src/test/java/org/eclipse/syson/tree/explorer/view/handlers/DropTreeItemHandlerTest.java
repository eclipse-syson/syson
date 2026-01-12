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
import org.eclipse.syson.SysONTestsProperties;
import org.eclipse.syson.application.data.SimpleProjectElementsTestProjectData;
import org.eclipse.syson.tree.explorer.filters.SysONTreeFilterConstants;
import org.eclipse.syson.tree.explorer.view.SysONTreeViewDescriptionProvider;
import org.junit.jupiter.api.BeforeEach;
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
 * Integration tests for Explorer DnD.
 *
 * @author Arthur Daussy
 */
@Transactional
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = { SysONTestsProperties.NO_DEFAULT_LIBRARIES_PROPERTY })
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

    @DisplayName("GIVEN a SySML project, WHEN drag an dropping a PartDefinition on a Package, THEN the PartDefinition should be moved under the Package.")
    @Sql(scripts = { SimpleProjectElementsTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void checkDnDPartDefinitionOnPackage() {

        var expandedIds = List.of(
                SimpleProjectElementsTestProjectData.SemanticIds.PACKAGE_1_ID,
                SimpleProjectElementsTestProjectData.SemanticIds.PACKAGE2_ID,
                SimpleProjectElementsTestProjectData.EDITING_CONTEXT_ID,
                SimpleProjectElementsTestProjectData.DOCUMENT_ID.toString());

        var explorerRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.treeProvider.getDescriptionId(), expandedIds,
                List.of(SysONTreeFilterConstants.HIDE_MEMBERSHIPS_TREE_ITEM_FILTER_ID, SysONTreeFilterConstants.HIDE_ROOT_NAMESPACES_ID));
        var input = new ExplorerEventInput(UUID.randomUUID(), SimpleProjectElementsTestProjectData.EDITING_CONTEXT_ID, explorerRepresentationId);
        var flux = this.treeEventSubscriptionRunner.run(input).flux();

        Consumer<Object> initialTreeContentConsumer = object -> Optional.of(object)
                .filter(TreeRefreshedEventPayload.class::isInstance)
                .map(TreeRefreshedEventPayload.class::cast)
                .map(TreeRefreshedEventPayload::tree)
                .ifPresentOrElse(tree -> {
                    assertThat(tree).isNotNull();
                    assertThat(tree.getChildren().get(0).getChildren().get(0).getChildren()).hasSize(2);
                    assertThat(tree.getChildren().get(0).getChildren().get(0).getChildren()).anyMatch(treeItem -> treeItem.getId()
                            .equals(SimpleProjectElementsTestProjectData.SemanticIds.PART_ID));
                    assertThat(tree.getChildren().get(0).getChildren().get(1).getChildren()).hasSize(1);
                    assertThat(tree.getChildren().get(0).getChildren().get(1).getChildren()).anyMatch(treeItem -> treeItem.getId()
                            .equals(SimpleProjectElementsTestProjectData.SemanticIds.PART_DEF_ID));
                }, () -> fail("Missing tree"));

        Runnable dropItemMutation = () -> {
            DropTreeItemInput dropTreeItemInput = new DropTreeItemInput(
                    UUID.randomUUID(), SimpleProjectElementsTestProjectData.EDITING_CONTEXT_ID,
                    explorerRepresentationId,
                    List.of(SimpleProjectElementsTestProjectData.SemanticIds.PART_DEF_ID),
                    SimpleProjectElementsTestProjectData.SemanticIds.PACKAGE_1_ID,
                    -1);
            var result = this.dropTreeItemMutationRunner.run(dropTreeItemInput);
            String typename = JsonPath.read(result.data(), "$.data.dropTreeItem.__typename");
            assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());

            TestTransaction.flagForCommit();
            TestTransaction.end();
            TestTransaction.start();
        };

        Consumer<Object> updateTreeContentConsumer = object -> Optional.of(object)
                .filter(TreeRefreshedEventPayload.class::isInstance)
                .map(TreeRefreshedEventPayload.class::cast)
                .map(TreeRefreshedEventPayload::tree)
                .ifPresentOrElse(tree -> {
                    assertThat(tree).isNotNull();
                    assertThat(tree.getChildren().get(0).getChildren().get(0).getChildren()).hasSize(3);
                    assertThat(tree.getChildren().get(0).getChildren().get(0).getChildren()).anyMatch(treeItem -> treeItem.getId()
                            .equals(SimpleProjectElementsTestProjectData.SemanticIds.PART_DEF_ID));
                }, () -> fail("Missing tree"));

        StepVerifier.create(flux)
                .consumeNextWith(initialTreeContentConsumer)
                .then(dropItemMutation)
                .consumeNextWith(updateTreeContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));

    }

    @DisplayName("GIVEN a SySML project, WHEN drag an dropping an Element into one of its descendant, THEN the selected Element should not be moved")
    @Sql(scripts = { SimpleProjectElementsTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void checkForbiddenDropOnDescendant() {

        var expandedIds = List.of(
                SimpleProjectElementsTestProjectData.SemanticIds.PACKAGE_1_ID,
                SimpleProjectElementsTestProjectData.SemanticIds.PACKAGE2_ID,
                SimpleProjectElementsTestProjectData.EDITING_CONTEXT_ID,
                SimpleProjectElementsTestProjectData.DOCUMENT_ID.toString());

        var explorerRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.treeProvider.getDescriptionId(), expandedIds,
                List.of(SysONTreeFilterConstants.HIDE_MEMBERSHIPS_TREE_ITEM_FILTER_ID, SysONTreeFilterConstants.HIDE_ROOT_NAMESPACES_ID));
        var input = new ExplorerEventInput(UUID.randomUUID(), SimpleProjectElementsTestProjectData.EDITING_CONTEXT_ID, explorerRepresentationId);
        var flux = this.treeEventSubscriptionRunner.run(input).flux();

        Consumer<Object> initialTreeContentConsumer = object -> Optional.of(object)
                .filter(TreeRefreshedEventPayload.class::isInstance)
                .map(TreeRefreshedEventPayload.class::cast)
                .map(TreeRefreshedEventPayload::tree)
                .ifPresentOrElse(tree -> {
                    assertThat(tree).isNotNull();
                    assertThat(tree.getChildren().get(0).getChildren().get(0).getChildren()).hasSize(2);
                    assertThat(tree.getChildren().get(0).getChildren().get(0).getChildren()).anyMatch(treeItem -> treeItem.getId()
                            .equals(SimpleProjectElementsTestProjectData.SemanticIds.PART_ID));
                    assertThat(tree.getChildren().get(0).getChildren().get(1).getChildren()).hasSize(1);
                    assertThat(tree.getChildren().get(0).getChildren().get(1).getChildren()).anyMatch(treeItem -> treeItem.getId()
                            .equals(SimpleProjectElementsTestProjectData.SemanticIds.PART_DEF_ID));
                }, () -> fail("Missing tree"));

        Runnable dropItemMutation = () -> {
            DropTreeItemInput dropTreeItemInput = new DropTreeItemInput(
                    UUID.randomUUID(), SimpleProjectElementsTestProjectData.EDITING_CONTEXT_ID,
                    explorerRepresentationId,
                    List.of(SimpleProjectElementsTestProjectData.SemanticIds.PACKAGE_1_ID),
                    SimpleProjectElementsTestProjectData.SemanticIds.PART_ID,
                    -1);
            var result = this.dropTreeItemMutationRunner.run(dropTreeItemInput);
            String typename = JsonPath.read(result.data(), "$.data.dropTreeItem.__typename");
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
