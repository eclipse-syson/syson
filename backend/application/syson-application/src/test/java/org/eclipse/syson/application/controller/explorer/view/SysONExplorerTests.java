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
import static org.eclipse.sirius.components.trees.tests.TreeEventPayloadConsumer.assertRefreshedTreeThat;

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.components.collaborative.trees.api.TreeFilter;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionInput;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.components.graphql.tests.api.IExecuteEditingContextFunctionRunner;
import org.eclipse.sirius.components.trees.TreeItem;
import org.eclipse.sirius.web.application.views.explorer.ExplorerEventInput;
import org.eclipse.sirius.web.application.views.explorer.services.ExplorerDescriptionProvider;
import org.eclipse.sirius.web.tests.graphql.ExplorerDescriptionsQueryRunner;
import org.eclipse.sirius.web.tests.services.api.IGivenCommittedTransaction;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.sirius.web.tests.services.explorer.ExplorerEventSubscriptionRunner;
import org.eclipse.sirius.web.tests.services.representation.RepresentationIdBuilder;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.controller.explorer.testers.ExpandAllTreeItemTester;
import org.eclipse.syson.application.controller.explorer.testers.TreeItemContextMenuTester;
import org.eclipse.syson.application.controller.explorer.testers.TreePathTester;
import org.eclipse.syson.application.data.GeneralViewEmptyTestProjectData;
import org.eclipse.syson.application.data.SysonStudioTestProjectData;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Package;
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

import reactor.test.StepVerifier;

/**
 * Tests the SysON Explorer View.
 *
 * @author gdaniel
 */
@Transactional
@SuppressWarnings("checkstyle:MultipleStringLiterals")
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
    private IExecuteEditingContextFunctionRunner executeEditingContextFunctionRunner;

    @Autowired
    private RepresentationIdBuilder representationIdBuilder;

    @Autowired
    private SysONTreeViewDescriptionProvider sysonTreeViewDescriptionProvider;

    @Autowired
    private IObjectSearchService objectSearchService;

    @Autowired
    private IIdentityService identityService;

    @Autowired
    private ExpandAllTreeItemTester expandAllTreeItemTester;

    @Autowired
    private TreeItemContextMenuTester treeItemContextMenuTester;

    @Autowired
    private TreePathTester treePathTester;

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
        Map<String, Object> explorerVariables = Map.of(
                "editingContextId", GeneralViewEmptyTestProjectData.EDITING_CONTEXT);
        var explorerResult = this.explorerDescriptionsQueryRunner.run(explorerVariables);
        this.givenCommittedTransaction.commit();
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
        this.givenCommittedTransaction.commit();

        AtomicReference<String> sysmlModelTreeItemId = new AtomicReference<>();
        AtomicReference<String> librariesDirectoryTreeItemId = new AtomicReference<>();
        AtomicReference<String> treeId = new AtomicReference<>();
        List<String> expandedTreeItemIds = new ArrayList<>();

        var initialExplorerContentConsumer = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            treeId.set(tree.getId());
            assertThat(tree.getChildren()).hasSize(2);
            TreeItem sysmlv2Model = tree.getChildren().get(0);
            assertThat(sysmlv2Model.getLabel().toString()).isEqualTo("SysMLv2");
            sysmlModelTreeItemId.set(sysmlv2Model.getId());
            assertThat(sysmlv2Model.isDeletable()).isTrue();
            assertThat(sysmlv2Model.isEditable()).isTrue();
            assertThat(sysmlv2Model.isSelectable()).isTrue();
            assertThat(sysmlv2Model.isHasChildren()).isTrue();
            TreeItem librariesDirectory = tree.getChildren().get(1);
            assertThat(librariesDirectory.getLabel().toString()).isEqualTo("Libraries");
            librariesDirectoryTreeItemId.set(librariesDirectory.getId());
            assertThat(librariesDirectory.isDeletable()).isFalse();
            assertThat(librariesDirectory.isEditable()).isFalse();
            assertThat(librariesDirectory.isSelectable()).isTrue();
            assertThat(librariesDirectory.isHasChildren()).isTrue();
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialExplorerContentConsumer)
                .then(() -> expandedTreeItemIds.addAll(this.expandAllTreeItemTester.expandTreeItem(GeneralViewEmptyTestProjectData.EDITING_CONTEXT, treeId.get(), sysmlModelTreeItemId.get())))
                .then(() -> expandedTreeItemIds
                        .addAll(this.expandAllTreeItemTester.expandTreeItem(GeneralViewEmptyTestProjectData.EDITING_CONTEXT, treeId.get(), librariesDirectoryTreeItemId.get())))
                .thenCancel()
                .verify(Duration.ofSeconds(10));

        var updatedExplorerRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.treeDescriptionId, expandedTreeItemIds, this.defaultFilters);
        var updatedInput = new ExplorerEventInput(UUID.randomUUID(), GeneralViewEmptyTestProjectData.EDITING_CONTEXT, updatedExplorerRepresentationId);
        var updatedFlux = this.explorerEventSubscriptionRunner.run(updatedInput);

        var updatedExplorerContentConsumer = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            assertThat(tree.getChildren()).hasSize(2);
            TreeItem sysmlv2Model = tree.getChildren().get(0);
            assertThat(sysmlv2Model.getLabel().toString()).isEqualTo("SysMLv2");
            assertThat(sysmlv2Model.isHasChildren()).isTrue();
            assertThat(sysmlv2Model.getChildren()).hasSize(1);
            assertThat(sysmlv2Model.getChildren().get(0).getLabel().toString()).isEqualTo("Package 1");
            TreeItem librariesDirectory = tree.getChildren().get(1);
            assertThat(librariesDirectory.getLabel().toString()).isEqualTo("Libraries");
            assertThat(librariesDirectory.isHasChildren()).isTrue();
            assertThat(librariesDirectory.getChildren()).hasSize(2);
            TreeItem kermlDirectory = librariesDirectory.getChildren().get(0);
            assertThat(kermlDirectory.getLabel().toString()).isEqualTo("KerML");
            assertThat(kermlDirectory.isDeletable()).isFalse();
            assertThat(kermlDirectory.isEditable()).isFalse();
            assertThat(kermlDirectory.isSelectable()).isTrue();
            assertThat(kermlDirectory.isHasChildren()).isTrue();
            TreeItem sysmlDirectory = librariesDirectory.getChildren().get(1);
            assertThat(sysmlDirectory.getLabel().toString()).isEqualTo("SysML");
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

    @DisplayName("GIVEN an empty SysML Project, WHEN the explorer is displayed with KerML and SysML libraries expanded, THEN the library models are visible")
    @Sql(scripts = { GeneralViewEmptyTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void getExplorerContentWithKerMLAndSysMLExpanded() {
        String librariesTreeItemId = UUID.nameUUIDFromBytes("SysON_Libraries_Directory".getBytes()).toString();
        String sysmlLibrariesTreeItemId = UUID.nameUUIDFromBytes("SysON_SysML_Directory".getBytes()).toString();
        String kermlLibrariesTreeItemId = UUID.nameUUIDFromBytes("SysON_KerML_Directory".getBytes()).toString();
        var explorerRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.treeDescriptionId,
                List.of(librariesTreeItemId, sysmlLibrariesTreeItemId, kermlLibrariesTreeItemId), this.defaultFilters);
        var input = new ExplorerEventInput(UUID.randomUUID(), GeneralViewEmptyTestProjectData.EDITING_CONTEXT, explorerRepresentationId);
        var flux = this.explorerEventSubscriptionRunner.run(input);
        this.givenCommittedTransaction.commit();

        var initialExplorerContentConsumer = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            assertThat(tree.getChildren()).hasSize(2);
            TreeItem librariesDirectory = tree.getChildren().get(1);
            assertThat(librariesDirectory.getLabel().toString()).isEqualTo("Libraries");
            assertThat(librariesDirectory.isDeletable()).isFalse();
            assertThat(librariesDirectory.isEditable()).isFalse();
            assertThat(librariesDirectory.isSelectable()).isTrue();
            assertThat(librariesDirectory.isHasChildren()).isTrue();
            assertThat(librariesDirectory.getChildren()).hasSize(2);
            TreeItem kermlDirectory = librariesDirectory.getChildren().get(0);
            assertThat(kermlDirectory.getLabel().toString()).isEqualTo("KerML");
            assertThat(kermlDirectory.isDeletable()).isFalse();
            assertThat(kermlDirectory.isEditable()).isFalse();
            assertThat(kermlDirectory.isSelectable()).isTrue();
            assertThat(kermlDirectory.isHasChildren()).isTrue();
            assertThat(kermlDirectory.getChildren()).allMatch(children -> !children.isDeletable() && !children.isEditable());
            TreeItem sysmlDirectory = librariesDirectory.getChildren().get(1);
            assertThat(sysmlDirectory.getLabel().toString()).isEqualTo("SysML");
            assertThat(sysmlDirectory.isDeletable()).isFalse();
            assertThat(sysmlDirectory.isEditable()).isFalse();
            assertThat(sysmlDirectory.isSelectable()).isTrue();
            assertThat(sysmlDirectory.isHasChildren()).isTrue();
            assertThat(sysmlDirectory.getChildren()).allMatch(children -> !children.isDeletable() && !children.isEditable());
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialExplorerContentConsumer)
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
        this.givenCommittedTransaction.commit();

        var initialExplorerContentConsumer = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            assertThat(tree.getChildren()).hasSize(2);
            TreeItem sysmlv2Model = tree.getChildren().get(0);
            assertThat(sysmlv2Model.getLabel().toString()).isEqualTo("SysMLv2");
            assertThat(sysmlv2Model.isDeletable()).isTrue();
            assertThat(sysmlv2Model.isEditable()).isTrue();
            assertThat(sysmlv2Model.isSelectable()).isTrue();
            assertThat(sysmlv2Model.isHasChildren()).isTrue();
            assertThat(sysmlv2Model.isExpanded());
            assertThat(sysmlv2Model.getChildren()).hasSize(1);
            TreeItem namespaceItem = sysmlv2Model.getChildren().get(0);
            assertThat(namespaceItem.getLabel().toString()).isEqualTo("Namespace");
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
        this.givenCommittedTransaction.commit();

        AtomicReference<String> sysmlModelTreeItemId = new AtomicReference<>();
        AtomicReference<String> librariesDirectoryTreeItemId = new AtomicReference<>();
        AtomicReference<String> kermlDirectoryTreeItemId = new AtomicReference<>();
        AtomicReference<String> sysmlDirectoryTreeItemId = new AtomicReference<>();

        var initialExplorerContentConsumer = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            assertThat(tree.getChildren()).hasSize(2);
            TreeItem sysmlv2Model = tree.getChildren().get(0);
            assertThat(sysmlv2Model.getLabel().toString()).isEqualTo("SysMLv2");
            sysmlModelTreeItemId.set(sysmlv2Model.getId());
            assertThat(sysmlv2Model.isHasChildren()).isTrue();
            TreeItem librariesDirectory = tree.getChildren().get(1);
            assertThat(librariesDirectory.getLabel().toString()).isEqualTo("Libraries");
            librariesDirectoryTreeItemId.set(librariesDirectory.getId());
            assertThat(librariesDirectory.isHasChildren()).isTrue();
            assertThat(librariesDirectory.getChildren()).hasSize(2);
            TreeItem kermlDirectory = librariesDirectory.getChildren().get(0);
            assertThat(kermlDirectory.getLabel().toString()).isEqualTo("KerML");
            kermlDirectoryTreeItemId.set(kermlDirectory.getId());
            assertThat(kermlDirectory.isHasChildren()).isTrue();
            TreeItem sysmlDirectory = librariesDirectory.getChildren().get(1);
            assertThat(sysmlDirectory.getLabel().toString()).isEqualTo("SysML");
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

    @DisplayName("GIVEN an empty SysML Project, WHEN the tree path is queried, THEN the returned tree path should take into accounts the Explorer filters.")
    @Sql(scripts = { GeneralViewEmptyTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void treePathQueryApplyExplorerFilters() {
        List<String> filters = List.of(SysONTreeFilterProvider.HIDE_MEMBERSHIPS_TREE_ITEM_FILTER_ID, SysONTreeFilterProvider.HIDE_ROOT_NAMESPACES_ID);
        var explorerRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.treeDescriptionId,
                List.of(GeneralViewEmptyTestProjectData.SemanticIds.MODEL_ID, GeneralViewEmptyTestProjectData.SemanticIds.PACKAGE_1_ID), filters);
        var input = new ExplorerEventInput(UUID.randomUUID(), GeneralViewEmptyTestProjectData.EDITING_CONTEXT, explorerRepresentationId);
        var flux = this.explorerEventSubscriptionRunner.run(input);
        this.givenCommittedTransaction.commit();

        AtomicReference<String> treeId = new AtomicReference<>();
        AtomicReference<String> sysmlv2DocumentTreeItemId = new AtomicReference<>();
        AtomicReference<String> package1TreeItemId = new AtomicReference<>();

        var initialExplorerContentConsumer = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            treeId.set(tree.getId());
            assertThat(tree.getChildren()).hasSize(2);
            TreeItem sysmlv2DocumentTreeItem = tree.getChildren().get(0);
            assertThat(sysmlv2DocumentTreeItem.getLabel().toString()).isEqualTo("SysMLv2");
            sysmlv2DocumentTreeItemId.set(sysmlv2DocumentTreeItem.getId());
            assertThat(sysmlv2DocumentTreeItem.isHasChildren()).isTrue();
            TreeItem package1TreeItem = sysmlv2DocumentTreeItem.getChildren().get(0);
            assertThat(package1TreeItem.getLabel().toString()).isEqualTo("Package 1");
            package1TreeItemId.set(package1TreeItem.getId());
        });

        StepVerifier.create(flux)
                .consumeNextWith(initialExplorerContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));

        // the list of tree item Ids to expand when a GetTreePath query is called on Package 1 with "Hide Memberships"
        // and "Hide Root Namespaces" filters active.
        List<String> treeItemIdsToExpand = this.treePathTester.getTreeItemIdsToExpand(GeneralViewEmptyTestProjectData.EDITING_CONTEXT, treeId.get(), List.of(package1TreeItemId.get()));
        assertThat(treeItemIdsToExpand).hasSize(1);
        assertThat(treeItemIdsToExpand).contains(sysmlv2DocumentTreeItemId.get());
    }

    /**
     * Tests that the {@code treePath} query behaves as expected in Sirius Web's default explorer.
     * <p>
     * Downstream applications integrating Sirius Web's default explorer should work as expected. The default behavior
     * of Sirius Web is to <b>not</b> filter tree path entries based on filters. We should ensure SysON does not
     * interfere with this behavior.
     * </p>
     */
    @DisplayName("GIVEN an empty SysML Project, WHEN the tree path is queried in the Sirius Web default Explorer, THEN the returned tree path should not take into accounts the Explorer filters.")
    @Sql(scripts = { GeneralViewEmptyTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void treePathQueryInSiriusWebDefaultExplorerDoesNotApplyExplorerFilters() {
        List<String> filters = List.of(SysONTreeFilterProvider.HIDE_MEMBERSHIPS_TREE_ITEM_FILTER_ID, SysONTreeFilterProvider.HIDE_ROOT_NAMESPACES_ID);
        var explorerRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(ExplorerDescriptionProvider.DESCRIPTION_ID, List.of(GeneralViewEmptyTestProjectData.SemanticIds.MODEL_ID, GeneralViewEmptyTestProjectData.SemanticIds.PACKAGE_1_ID), filters);

        var input = new ExplorerEventInput(UUID.randomUUID(), GeneralViewEmptyTestProjectData.EDITING_CONTEXT, explorerRepresentationId);
        var flux = this.explorerEventSubscriptionRunner.run(input);
        this.givenCommittedTransaction.commit();

        AtomicReference<String> treeId = new AtomicReference<>();
        AtomicReference<String> sysmlv2DocumentTreeItemId = new AtomicReference<>();
        AtomicReference<String> package1TreeItemId = new AtomicReference<>();

        var initialExplorerContentConsumer = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            treeId.set(tree.getId());
            // SysML and KerML libraries are at the root of the tree in the Sirius Web explorer.
            assertThat(tree.getChildren()).hasSize(95);
            Optional<TreeItem> optionalSysmlv2DocumentTreeItem = tree.getChildren().stream()
                    .filter(treeItem -> Objects.equals(treeItem.getLabel().toString(), "SysMLv2"))
                    .findFirst();
            assertThat(optionalSysmlv2DocumentTreeItem).isPresent();
            TreeItem sysmlv2DocumentTreeItem = optionalSysmlv2DocumentTreeItem.get();
            sysmlv2DocumentTreeItemId.set(sysmlv2DocumentTreeItem.getId());
            assertThat(sysmlv2DocumentTreeItem.isHasChildren()).isTrue();
            TreeItem package1TreeItem = sysmlv2DocumentTreeItem.getChildren().get(0);
            assertThat(package1TreeItem.getLabel().toString()).isEqualTo("Package 1");
            package1TreeItemId.set(package1TreeItem.getId());
        });

        // Retrieve the Ids of the root Namespace and the OwningMembership containing the package, they should be part
        // of the tree path.
        AtomicReference<String> rootNamespaceId = new AtomicReference<>();
        AtomicReference<String> owningMembershipId = new AtomicReference<>();

        Runnable getNamespaceAndMembershipIds = () -> {
            var getNamespaceAndMembershipIdsInput = new ExecuteEditingContextFunctionInput(UUID.randomUUID(), GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                    (editingContext, executeEditingContextFunctionInput) -> {
                        Optional<Object> optionalPackage1 = this.objectSearchService.getObject(editingContext, package1TreeItemId.get());
                        assertThat(optionalPackage1).isPresent().get().isInstanceOf(Package.class);
                        Package package1 = (Package) optionalPackage1.get();
                        EObject owningMembership = package1.eContainer();
                        assertThat(owningMembership).isInstanceOf(OwningMembership.class);
                        owningMembershipId.set(this.identityService.getId(owningMembership));
                        EObject rootNamespace = owningMembership.eContainer();
                        assertThat(rootNamespace).isInstanceOf(Namespace.class);
                        rootNamespaceId.set(this.identityService.getId(rootNamespace));
                        return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                    });
            var payload = this.executeEditingContextFunctionRunner.execute(getNamespaceAndMembershipIdsInput).block();
            assertThat(payload).isInstanceOf(ExecuteEditingContextFunctionSuccessPayload.class);
        };

        StepVerifier.create(flux)
                .consumeNextWith(initialExplorerContentConsumer)
                .then(getNamespaceAndMembershipIds)
                .thenCancel()
                .verify(Duration.ofSeconds(10));

        // The list of tree item Ids to expand when a GetTreePath query is called on Package 1 (since filters are
        // ignored by Sirius Web this list contains all the ancestors of Package 1, including the ones filtered out by
        // the explorer).
        List<String> treeItemIdsToExpand = this.treePathTester.getTreeItemIdsToExpand(GeneralViewEmptyTestProjectData.EDITING_CONTEXT, treeId.get(), List.of(package1TreeItemId.get()));
        assertThat(treeItemIdsToExpand).hasSize(3);
        assertThat(treeItemIdsToExpand).containsExactly(owningMembershipId.get(), rootNamespaceId.get(), sysmlv2DocumentTreeItemId.get());
    }
}
