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
package org.eclipse.syson.application.controllers.explorer.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.components.trees.tests.TreeEventPayloadConsumer.assertRefreshedTreeThat;

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.components.core.api.IEditingContextSearchService;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.trees.Tree;
import org.eclipse.sirius.components.trees.TreeItem;
import org.eclipse.sirius.components.trees.tests.graphql.InitialDirectEditTreeItemLabelQueryRunner;
import org.eclipse.sirius.web.application.views.explorer.ExplorerEventInput;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.sirius.web.tests.services.explorer.ExplorerEventSubscriptionRunner;
import org.eclipse.sirius.web.tests.services.representation.RepresentationIdBuilder;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.ExplorerViewDirectEditTestProjectData;
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
 * Integration tests of the Explorer view.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExplorerViewControllerIntegrationTests extends AbstractIntegrationTests {

    /**
     * Record used to contain both a way to find a tree item and some predicate to validate on said tree item in order
     * to simplify the design of some advanced tests.
     *
     * @author arichard
     */
    private record TreeItemMatcher(Function<Tree, TreeItem> treeItemFinder, Predicate<TreeItem> treeItemPredicate) {
    }

    private final TreeItemMatcher view1GV = new TreeItemMatcher(
            tree -> tree.getChildren().get(0).getChildren().get(0).getChildren().get(2),
            treeItem -> treeItem.getLabel().toString().equals("view1 [GeneralView]"));

    private final TreeItemMatcher view2AFV = new TreeItemMatcher(
            tree -> tree.getChildren().get(0).getChildren().get(0).getChildren().get(3),
            treeItem -> treeItem.getLabel().toString().equals("view2 [ActionFlowView]"));

    private final TreeItemMatcher view3STV = new TreeItemMatcher(
            tree -> tree.getChildren().get(0).getChildren().get(0).getChildren().get(4),
            treeItem -> treeItem.getLabel().toString().equals("view3 [StateTransitionView]"));

    private final TreeItemMatcher view4IV = new TreeItemMatcher(
            tree -> tree.getChildren().get(0).getChildren().get(0).getChildren().get(1).getChildren().get(1),
            treeItem -> treeItem.getLabel().toString().equals("view4 [InterconnectionView]"));

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IEditingContextSearchService editingContextSearchService;

    @Autowired
    private IIdentityService identityService;

    @Autowired
    private ExplorerEventSubscriptionRunner treeEventSubscriptionRunner;

    @Autowired
    private InitialDirectEditTreeItemLabelQueryRunner initialDirectEditTreeItemLabelQueryRunner;

    @Autowired
    private RepresentationIdBuilder representationIdBuilder;

    @Autowired
    private SysONTreeViewDescriptionProvider sysONTreeViewDescriptionProvider;

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN the SysON Explorer View, WHEN we direct edit a ViewUsage typed with a standard diagram, THEN the type of ViewUsage is not part of the initial value of the direct edit")
    @Sql(scripts = { ExplorerViewDirectEditTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void testDirectEditOnViewUsage() {
        var expandedIds = this.getAllTreeItemIds();
        var activatedFilters = List.of(SysONTreeFilterProvider.HIDE_ROOT_NAMESPACES_ID, SysONTreeFilterProvider.HIDE_MEMBERSHIPS_TREE_ITEM_FILTER_ID);
        var treeRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.sysONTreeViewDescriptionProvider.getDescriptionId(), expandedIds, activatedFilters);

        var treeEventInput = new ExplorerEventInput(UUID.randomUUID(), ExplorerViewDirectEditTestProjectData.EDITING_CONTEXT_ID, treeRepresentationId);
        var treeFlux = this.treeEventSubscriptionRunner.run(treeEventInput);

        var hasProjectContent = this.getTreeRefreshedEventPayloadMatcher(List.of(this.view1GV, this.view2AFV, this.view3STV, this.view4IV));

        StepVerifier.create(treeFlux)
                .consumeNextWith(hasProjectContent)
                .then(this.triggerDirectEditTreeItemLabel(ExplorerViewDirectEditTestProjectData.EDITING_CONTEXT_ID, treeRepresentationId, UUID.fromString(ExplorerViewDirectEditTestProjectData.SemanticIds.VIEW_1_GV_ID), "view1"))
                .then(this.triggerDirectEditTreeItemLabel(ExplorerViewDirectEditTestProjectData.EDITING_CONTEXT_ID, treeRepresentationId,
                        UUID.fromString(ExplorerViewDirectEditTestProjectData.SemanticIds.VIEW_2_AFV_ID), "view2"))
                .then(this.triggerDirectEditTreeItemLabel(ExplorerViewDirectEditTestProjectData.EDITING_CONTEXT_ID, treeRepresentationId,
                        UUID.fromString(ExplorerViewDirectEditTestProjectData.SemanticIds.VIEW_3_STV_ID), "view3"))
                .then(this.triggerDirectEditTreeItemLabel(ExplorerViewDirectEditTestProjectData.EDITING_CONTEXT_ID, treeRepresentationId,
                        UUID.fromString(ExplorerViewDirectEditTestProjectData.SemanticIds.VIEW_4_IV_ID), "view4"))
                .thenCancel()
                .verify(Duration.ofSeconds(100));

    }

    @DisplayName("GIVEN the SysON Explorer View, WHEN hide expose element filter is active, THEN the expose element are not return as tree item")
    @Sql(scripts = { ExplorerViewDirectEditTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void testHideExposeElementFilter() {
        var expandedIds = this.getAllTreeItemIds();
        var activatedFilters = List.of(SysONTreeFilterProvider.HIDE_ROOT_NAMESPACES_ID, SysONTreeFilterProvider.HIDE_MEMBERSHIPS_TREE_ITEM_FILTER_ID, SysONTreeFilterProvider.HIDE_EXPOSE_ELEMENTS_TREE_ITEM_FILTER_ID);
        var treeRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.sysONTreeViewDescriptionProvider.getDescriptionId(), expandedIds, activatedFilters);

        var treeEventInput = new ExplorerEventInput(UUID.randomUUID(), ExplorerViewDirectEditTestProjectData.EDITING_CONTEXT_ID, treeRepresentationId);
        var treeFlux = this.treeEventSubscriptionRunner.run(treeEventInput);

        var hasNoExposeElement = assertRefreshedTreeThat(tree -> assertThat(tree.getChildren()).allSatisfy(this::assertNoExposeChildren));

        StepVerifier.create(treeFlux)
                .consumeNextWith(hasNoExposeElement)
                .thenCancel()
                .verify(Duration.ofSeconds(100));

    }

    private List<String> getAllTreeItemIds() {
        var optionalEditingContext = this.editingContextSearchService.findById(ExplorerViewDirectEditTestProjectData.EDITING_CONTEXT_ID)
                .filter(IEMFEditingContext.class::isInstance)
                .map(IEMFEditingContext.class::cast);
        assertThat(optionalEditingContext).isPresent();

        var editingContext = optionalEditingContext.get();
        var expandedIds = new ArrayList<String>();
        editingContext.getDomain().getResourceSet().getAllContents().forEachRemaining(notifier -> {
            if (notifier instanceof Resource || notifier instanceof EObject) {
                expandedIds.add(this.identityService.getId(notifier));
            }
        });
        return expandedIds;
    }

    private Consumer<Object> getTreeRefreshedEventPayloadMatcher(List<TreeItemMatcher> treeItemMatchers) {
        return assertRefreshedTreeThat(tree -> {
            assertThat(treeItemMatchers).allMatch(treeItemMatcher -> {
                var treeItem = treeItemMatcher.treeItemFinder.apply(tree);
                return treeItemMatcher.treeItemPredicate.test(treeItem);
            });
        });
    }

    private Runnable triggerDirectEditTreeItemLabel(String editingContextId, String treeId, UUID treeItemId, String expectedLabel) {
        return () -> {
            Map<String, Object> variables = Map.of(
                    "editingContextId", editingContextId,
                    "representationId", treeId,
                    "treeItemId", treeItemId);
            var result = this.initialDirectEditTreeItemLabelQueryRunner.run(variables);

            String initialDirectEditLabel = JsonPath.read(result, "$.data.viewer.editingContext.representation.description.initialDirectEditTreeItemLabel");
            assertThat(initialDirectEditLabel).isEqualTo(expectedLabel);
        };
    }

    private void assertNoExposeChildren(TreeItem item) {
        if (item == null) {
            return;
        }
        assertThat(item.getKind()).doesNotEndWith("entity=MembershipExpose");
        List<TreeItem> children = item.getChildren();
        if (children != null) {
            for (TreeItem child : children) {
                this.assertNoExposeChildren(child);
            }
        }
    }
}
