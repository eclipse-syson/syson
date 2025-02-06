/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.application.controller.explorer.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.eclipse.sirius.components.collaborative.trees.api.TreeFilter;
import org.eclipse.sirius.components.collaborative.trees.dto.TreeRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.labels.StyledStringFragment;
import org.eclipse.sirius.components.trees.Tree;
import org.eclipse.sirius.components.trees.TreeItem;
import org.eclipse.sirius.web.application.views.explorer.ExplorerEventInput;
import org.eclipse.sirius.web.application.views.explorer.services.ExplorerDescriptionProvider;
import org.eclipse.sirius.web.tests.graphql.ExplorerDescriptionsQueryRunner;
import org.eclipse.sirius.web.tests.services.api.IGivenCommittedTransaction;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.sirius.web.tests.services.explorer.ExplorerEventSubscriptionRunner;
import org.eclipse.sirius.web.tests.services.representation.RepresentationIdBuilder;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controller.explorer.testers.ExpandTreeItemTester;
import org.eclipse.syson.application.controller.explorer.testers.TreeItemContextMenuTester;
import org.eclipse.syson.application.data.GeneralViewEmptyTestProjectData;
import org.eclipse.syson.application.data.SysonStudioTestProjectData;
import org.eclipse.syson.tree.explorer.view.SysONExplorerTreeDescriptionProvider;
import org.eclipse.syson.tree.explorer.view.SysONTreeViewDescriptionProvider;
import org.eclipse.syson.tree.explorer.view.filters.SysONTreeFilterProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import graphql.execution.DataFetcherResult;
import reactor.test.StepVerifier;

/**
 * Tests the SysON Explorer View.
 *
 * @author gdaniel
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SysONExplorerTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenCommittedTransaction givenCommittedTransaction;

    @Autowired
    private ExplorerEventSubscriptionRunner explorerEventSubscriptionRunner;

    @Autowired
    private ExplorerDescriptionsQueryRunner explorerDescriptionsQueryRunner;

    @Autowired
    private RepresentationIdBuilder representationIdBuilder;

    @Autowired
    private SysONTreeViewDescriptionProvider sysonTreeViewDescriptionProvider;

    @Autowired
    private ExpandTreeItemTester expandTreeItemTester;

    @Autowired
    private TreeItemContextMenuTester treeItemContextMenuTester;

    @Autowired
    private SysONTreeFilterProvider sysonTreeFilterProvider;

    private List<String> defaultFilters;

    private String treeDescriptionId;

    @BeforeEach
    public void beforeEach() {
        this.treeDescriptionId = this.sysonTreeViewDescriptionProvider.getDescriptionId();
        this.givenInitialServerState.initialize();
        this.defaultFilters = this.sysonTreeFilterProvider.get(null, null, null).stream()
                .filter(TreeFilter::defaultState)
                .map(TreeFilter::id)
                .toList();
    }

    @DisplayName("GIVEN an empty SysML Project, WHEN the available explorers are requested, THEN the SysON explorer is returned")
    @Sql(scripts = { GeneralViewEmptyTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void getAvailableExplorersForSysMLv2Project() {
        this.givenCommittedTransaction.commit();

        Map<String, Object> explorerVariables = Map.of(
                "editingContextId", GeneralViewEmptyTestProjectData.EDITING_CONTEXT);
        var explorerResult = this.explorerDescriptionsQueryRunner.run(explorerVariables);
        List<String> explorerIds = JsonPath.read(explorerResult, "$.data.viewer.editingContext.explorerDescriptions[*].id");
        assertThat(explorerIds).hasSize(1);
        assertThat(explorerIds).contains(this.treeDescriptionId);
    }

    @DisplayName("GIVEN a SysON Studio Project, WHEN the available explorers are requested, THEN the Sirius Web explorer is returned")
    @Sql(scripts = { SysonStudioTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void getAvailableExplorersForStudioProject() {
        this.givenCommittedTransaction.commit();

        Map<String, Object> explorerVariables = Map.of(
                "editingContextId", SysonStudioTestProjectData.EDITING_CONTEXT_ID);
        var explorerResult = this.explorerDescriptionsQueryRunner.run(explorerVariables);
        List<String> explorerIds = JsonPath.read(explorerResult, "$.data.viewer.editingContext.explorerDescriptions[*].id");
        assertThat(explorerIds).hasSize(1);
        assertThat(explorerIds).contains(ExplorerDescriptionProvider.DESCRIPTION_ID);
    }

    @DisplayName("GIVEN an empty SysML Project, WHEN the explorer is displayed, THEN the libraries are visible and the root namespace and memberships are not visible")
    @Sql(scripts = { GeneralViewEmptyTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void getExplorerContentWithDefaultFilters() {
        var explorerRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.treeDescriptionId, List.of(), this.defaultFilters);
        var input = new ExplorerEventInput(UUID.randomUUID(), GeneralViewEmptyTestProjectData.EDITING_CONTEXT, explorerRepresentationId);
        var flux = this.explorerEventSubscriptionRunner.run(input);

        AtomicReference<String> sysmlModelTreeItemId = new AtomicReference<>();
        AtomicReference<String> librariesDirectoryTreeItemId = new AtomicReference<>();
        AtomicReference<String> treeId = new AtomicReference<>();
        List<String> expandedTreeItemIds = new ArrayList<>();

        var initialExplorerContentConsumer = this.getTreeSubscriptionConsumer(tree -> {
            assertThat(tree).isNotNull();
            treeId.set(tree.getId());
            assertThat(tree.getChildren()).hasSize(2);
            TreeItem sysmlv2Model = tree.getChildren().get(0);
            this.assertThatTreeItemHasLabel(sysmlv2Model, "SysMLv2");
            sysmlModelTreeItemId.set(sysmlv2Model.getId());
            assertThat(sysmlv2Model.isDeletable()).isTrue();
            assertThat(sysmlv2Model.isEditable()).isTrue();
            assertThat(sysmlv2Model.isSelectable()).isTrue();
            assertThat(sysmlv2Model.isHasChildren()).isTrue();
            TreeItem librariesDirectory = tree.getChildren().get(1);
            this.assertThatTreeItemHasLabel(librariesDirectory, "Libraries");
            librariesDirectoryTreeItemId.set(librariesDirectory.getId());
            assertThat(librariesDirectory.isDeletable()).isFalse();
            assertThat(librariesDirectory.isEditable()).isFalse();
            assertThat(librariesDirectory.isSelectable()).isTrue();
            assertThat(librariesDirectory.isHasChildren()).isTrue();
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialExplorerContentConsumer)
                .then(() -> expandedTreeItemIds.addAll(this.expandTreeItemTester.expandTreeItem(GeneralViewEmptyTestProjectData.EDITING_CONTEXT, treeId.get(), sysmlModelTreeItemId.get())))
                .then(() -> expandedTreeItemIds
                        .addAll(this.expandTreeItemTester.expandTreeItem(GeneralViewEmptyTestProjectData.EDITING_CONTEXT, treeId.get(), librariesDirectoryTreeItemId.get())))
                .thenCancel()
                .verify(Duration.ofSeconds(10));

        var updatedExplorerRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.treeDescriptionId, expandedTreeItemIds, this.defaultFilters);
        var updatedInput = new ExplorerEventInput(UUID.randomUUID(), GeneralViewEmptyTestProjectData.EDITING_CONTEXT, updatedExplorerRepresentationId);
        var updatedFlux = this.explorerEventSubscriptionRunner.run(updatedInput);

        var updatedExplorerContentConsumer = this.getTreeSubscriptionConsumer(tree -> {
            assertThat(tree).isNotNull();
            assertThat(tree.getChildren()).hasSize(2);
            TreeItem sysmlv2Model = tree.getChildren().get(0);
            this.assertThatTreeItemHasLabel(sysmlv2Model, "SysMLv2");
            assertThat(sysmlv2Model.isHasChildren()).isTrue();
            assertThat(sysmlv2Model.getChildren()).hasSize(1);
            this.assertThatTreeItemHasLabel(sysmlv2Model.getChildren().get(0), "Package 1");
            TreeItem librariesDirectory = tree.getChildren().get(1);
            this.assertThatTreeItemHasLabel(librariesDirectory, "Libraries");
            assertThat(librariesDirectory.isHasChildren()).isTrue();
            assertThat(librariesDirectory.getChildren()).hasSize(2);
            TreeItem kermlDirectory = librariesDirectory.getChildren().get(0);
            this.assertThatTreeItemHasLabel(kermlDirectory, "KerML");
            assertThat(kermlDirectory.isDeletable()).isFalse();
            assertThat(kermlDirectory.isEditable()).isFalse();
            assertThat(kermlDirectory.isSelectable()).isTrue();
            assertThat(kermlDirectory.isHasChildren()).isTrue();
            TreeItem sysmlDirectory = librariesDirectory.getChildren().get(1);
            this.assertThatTreeItemHasLabel(sysmlDirectory, "SysML");
            assertThat(sysmlDirectory.isDeletable()).isFalse();
            assertThat(sysmlDirectory.isEditable()).isFalse();
            assertThat(sysmlDirectory.isSelectable()).isTrue();
            assertThat(sysmlDirectory.isHasChildren()).isTrue();
        });

        StepVerifier.create(updatedFlux)
                .consumeNextWith(updatedExplorerContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));

    }

    @DisplayName("GIVEN an empty SysML Project, WHEN the explorer is displayed with its root model expanded and the hide memberships and hide KerML libraries filters, THEN the root model is visible and is expanded")
    @Sql(scripts = { GeneralViewEmptyTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void getRootContentWithHideMembershipsAndHideKerMLStandardLibraries() {
        List<String> filters = List.of(SysONTreeFilterProvider.HIDE_MEMBERSHIPS_TREE_ITEM_FILTER_ID, SysONTreeFilterProvider.HIDE_KERML_STANDARD_LIBRARIES_TREE_FILTER_ID);
        var explorerRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.treeDescriptionId, List.of(GeneralViewEmptyTestProjectData.SemanticIds.MODEL_ID), filters);
        var input = new ExplorerEventInput(UUID.randomUUID(), GeneralViewEmptyTestProjectData.EDITING_CONTEXT, explorerRepresentationId);
        var flux = this.explorerEventSubscriptionRunner.run(input);

        var initialExplorerContentConsumer = this.getTreeSubscriptionConsumer(tree -> {
            assertThat(tree).isNotNull();
            assertThat(tree.getChildren()).hasSize(2);
            TreeItem sysmlv2Model = tree.getChildren().get(0);
            this.assertThatTreeItemHasLabel(sysmlv2Model, "SysMLv2");
            assertThat(sysmlv2Model.isDeletable()).isTrue();
            assertThat(sysmlv2Model.isEditable()).isTrue();
            assertThat(sysmlv2Model.isSelectable()).isTrue();
            assertThat(sysmlv2Model.isHasChildren()).isTrue();
            assertThat(sysmlv2Model.isExpanded());
            assertThat(sysmlv2Model.getChildren()).hasSize(1);
            TreeItem namespaceItem = sysmlv2Model.getChildren().get(0);
            this.assertThatTreeItemHasLabel(namespaceItem, "Namespace");
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialExplorerContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN an empty SysML Project, WHEN context menu is queried, THEN the menu is returned")
    @Sql(scripts = { GeneralViewEmptyTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void getContextMenuOfModelAndLibraryDirectories() {
        // Expand the Libraries directory when building the explorer, we want to check the context menu of elements
        // under it.
        var explorerRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.treeDescriptionId,
                List.of(UUID.nameUUIDFromBytes("SysON_Libraries_Directory".getBytes()).toString()), this.defaultFilters);
        var input = new ExplorerEventInput(UUID.randomUUID(), GeneralViewEmptyTestProjectData.EDITING_CONTEXT, explorerRepresentationId);
        var flux = this.explorerEventSubscriptionRunner.run(input);

        AtomicReference<String> sysmlModelTreeItemId = new AtomicReference<>();
        AtomicReference<String> librariesDirectoryTreeItemId = new AtomicReference<>();
        AtomicReference<String> kermlDirectoryTreeItemId = new AtomicReference<>();
        AtomicReference<String> sysmlDirectoryTreeItemId = new AtomicReference<>();

        var initialExplorerContentConsumer = this.getTreeSubscriptionConsumer(tree -> {
            assertThat(tree).isNotNull();
            assertThat(tree.getChildren()).hasSize(2);
            TreeItem sysmlv2Model = tree.getChildren().get(0);
            this.assertThatTreeItemHasLabel(sysmlv2Model, "SysMLv2");
            sysmlModelTreeItemId.set(sysmlv2Model.getId());
            assertThat(sysmlv2Model.isHasChildren()).isTrue();
            TreeItem librariesDirectory = tree.getChildren().get(1);
            this.assertThatTreeItemHasLabel(librariesDirectory, "Libraries");
            librariesDirectoryTreeItemId.set(librariesDirectory.getId());
            assertThat(librariesDirectory.isHasChildren()).isTrue();
            assertThat(librariesDirectory.getChildren()).hasSize(2);
            TreeItem kermlDirectory = librariesDirectory.getChildren().get(0);
            this.assertThatTreeItemHasLabel(kermlDirectory, "KerML");
            kermlDirectoryTreeItemId.set(kermlDirectory.getId());
            assertThat(kermlDirectory.isHasChildren()).isTrue();
            TreeItem sysmlDirectory = librariesDirectory.getChildren().get(1);
            this.assertThatTreeItemHasLabel(sysmlDirectory, "SysML");
            sysmlDirectoryTreeItemId.set(sysmlDirectory.getId());
            assertThat(sysmlDirectory.isHasChildren()).isTrue();
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialExplorerContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));

        List<String> sysmlModelContextMenu = this.treeItemContextMenuTester.getContextMenuEntries(GeneralViewEmptyTestProjectData.EDITING_CONTEXT, explorerRepresentationId,
                sysmlModelTreeItemId.get());
        // The expected size is 1: there is only one entry contributed from the backend, the other entries (new object,
        // download, etc) are contributed from the frontend.
        assertThat(sysmlModelContextMenu).hasSize(1).anyMatch(entry -> Objects.equals(entry, SysONExplorerTreeDescriptionProvider.EXPAND_ALL_MENU_ENTRY_CONTRIBUTION_ID));
        List<String> librariesDirectoryContextMenu = this.treeItemContextMenuTester.getContextMenuEntries(GeneralViewEmptyTestProjectData.EDITING_CONTEXT, explorerRepresentationId,
                librariesDirectoryTreeItemId.get());
        assertThat(librariesDirectoryContextMenu).isEmpty();
        List<String> kermlDirectoryContextMenu = this.treeItemContextMenuTester.getContextMenuEntries(GeneralViewEmptyTestProjectData.EDITING_CONTEXT, explorerRepresentationId,
                kermlDirectoryTreeItemId.get());
        assertThat(kermlDirectoryContextMenu).isEmpty();
        List<String> sysmlDirectoryContextMenu = this.treeItemContextMenuTester.getContextMenuEntries(GeneralViewEmptyTestProjectData.EDITING_CONTEXT, explorerRepresentationId,
                sysmlDirectoryTreeItemId.get());
        assertThat(sysmlDirectoryContextMenu).isEmpty();
    }

    private void assertThatTreeItemHasLabel(TreeItem treeItem, String label) {
        assertThat(treeItem.getLabel().styledStringFragments().stream().map(StyledStringFragment::text).collect(Collectors.joining())).isEqualTo(label);
    }

    private Consumer<Object> getTreeSubscriptionConsumer(Consumer<Tree> treeConsumer) {
        return object -> Optional.of(object)
                .filter(DataFetcherResult.class::isInstance)
                .map(DataFetcherResult.class::cast)
                .map(DataFetcherResult::getData)
                .filter(TreeRefreshedEventPayload.class::isInstance)
                .map(TreeRefreshedEventPayload.class::cast)
                .map(TreeRefreshedEventPayload::tree)
                .ifPresentOrElse(treeConsumer, () -> fail("Missing tree"));
    }
}
