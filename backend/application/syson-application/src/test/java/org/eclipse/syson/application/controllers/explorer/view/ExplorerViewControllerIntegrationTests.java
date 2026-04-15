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
package org.eclipse.syson.application.controllers.explorer.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.components.trees.tests.TreeEventPayloadConsumer.assertRefreshedTreeThat;

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.components.collaborative.dto.CreateChildInput;
import org.eclipse.sirius.components.collaborative.dto.CreateChildSuccessPayload;
import org.eclipse.sirius.components.core.api.ErrorPayload;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionInput;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.components.graphql.tests.RepresentationDescriptionsQueryRunner;
import org.eclipse.sirius.components.graphql.tests.api.IExecuteEditingContextFunctionRunner;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.sirius.components.trees.Tree;
import org.eclipse.sirius.components.trees.TreeItem;
import org.eclipse.sirius.components.trees.tests.graphql.InitialDirectEditTreeItemLabelQueryRunner;
import org.eclipse.sirius.components.trees.tests.graphql.TreeFiltersQueryRunner;
import org.eclipse.sirius.web.application.views.explorer.ExplorerEventInput;
import org.eclipse.sirius.web.tests.graphql.CreateChildMutationRunner;
import org.eclipse.sirius.web.tests.graphql.TreeItemContextMenuQueryRunner;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.sirius.web.tests.services.explorer.ExplorerEventSubscriptionRunner;
import org.eclipse.sirius.web.tests.services.representation.RepresentationIdBuilder;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.data.ExplorerViewDirectEditTestProjectData;
import org.eclipse.syson.application.data.GeneralViewEmptyTestProjectData;
import org.eclipse.syson.application.data.ProjectWithLibraryDependencyContainingLibraryPackageTestProjectData;
import org.eclipse.syson.application.data.WithUserLibrariesTestProjectData;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.LibraryPackage;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.tree.explorer.filters.SysONTreeFilterConstants;
import org.eclipse.syson.tree.explorer.fragments.LibrariesDirectory;
import org.eclipse.syson.tree.explorer.fragments.UserLibrariesDirectory;
import org.eclipse.syson.tree.explorer.view.SysONTreeViewDescriptionProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;

/**
 * Integration tests of the Explorer view.
 *
 * @author arichard
 */
@Transactional
@SuppressWarnings("checkstyle:MultipleStringLiterals")
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

    private final TreeItemMatcher req1RU = new TreeItemMatcher(
            tree -> tree.getChildren().get(0).getChildren().get(1).getChildren().get(1),
            treeItem -> treeItem.getLabel().toString().equals("<Req1> requirement1"));

    private final TreeItemMatcher req2RU = new TreeItemMatcher(
            tree -> tree.getChildren().get(0).getChildren().get(1).getChildren().get(2),
            treeItem -> treeItem.getLabel().toString().equals("<Req2> "));

    private final TreeItemMatcher userLibraryPackageRW = new TreeItemMatcher(
            tree -> this.findTreeItem(tree, WithUserLibrariesTestProjectData.SemanticIds.RW_USER_LIBRARY_PACKAGE_ID).orElseThrow(),
            treeItem -> treeItem.getLabel().toString().equals("Package2"));

    private final TreeItemMatcher userLibraryPackageRO = new TreeItemMatcher(
            tree -> this.findTreeItem(tree, WithUserLibrariesTestProjectData.SemanticIds.RO_USER_LIBRARY_PACKAGE_ID).orElseThrow(),
            treeItem -> treeItem.getLabel().toString().equals("Package3"));

    private final TreeItemMatcher view1GVRepresentation = new TreeItemMatcher(
            tree -> tree.getChildren().get(0).getChildren().get(0).getChildren().get(2).getChildren().get(0),
            treeItem -> treeItem.getLabel().toString().equals("view1"));

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IIdentityService identityService;

    @Autowired
    private ExplorerEventSubscriptionRunner treeEventSubscriptionRunner;

    @Autowired
    private InitialDirectEditTreeItemLabelQueryRunner initialDirectEditTreeItemLabelQueryRunner;

    @Autowired
    private RepresentationDescriptionsQueryRunner representationDescriptionsQueryRunner;

    @Autowired
    private RepresentationIdBuilder representationIdBuilder;

    @Autowired
    private SysONTreeViewDescriptionProvider sysONTreeViewDescriptionProvider;

    @Autowired
    private TreeFiltersQueryRunner treeFiltersQueryRunner;

    @Autowired
    private CreateChildMutationRunner createChildMutationRunner;

    @Autowired
    private TreeItemContextMenuQueryRunner treeItemContextMenuQueryRunner;

    @Autowired
    private IExecuteEditingContextFunctionRunner executeEditingContextFunctionRunner;

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN a Package, WHEN the New Representation menu is invoked, THEN InterconnectionView is a possible candidate")
    @GivenSysONServer({ GeneralViewEmptyTestProjectData.SCRIPT_PATH })
    @Test
    public void canCreateInterconnectionViewOnPackage() {
        Map<String, Object> variables = Map.of(
                "editingContextId", GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                "objectId", GeneralViewEmptyTestProjectData.SemanticIds.PACKAGE_1_ID);

        var result = this.representationDescriptionsQueryRunner.run(variables);

        // Needed because GeneralViewEmptyTestProject performs a migration of the diagram
        TestTransaction.flagForCommit();
        TestTransaction.end();

        boolean hasPreviousPage = JsonPath.read(result.data(), "$.data.viewer.editingContext.representationDescriptions.pageInfo.hasPreviousPage");
        assertThat(hasPreviousPage).isFalse();

        boolean hasNextPage = JsonPath.read(result.data(), "$.data.viewer.editingContext.representationDescriptions.pageInfo.hasNextPage");
        assertThat(hasNextPage).isFalse();

        String startCursor = JsonPath.read(result.data(), "$.data.viewer.editingContext.representationDescriptions.pageInfo.startCursor");
        assertThat(startCursor).isNotBlank();

        String endCursor = JsonPath.read(result.data(), "$.data.viewer.editingContext.representationDescriptions.pageInfo.endCursor");
        assertThat(endCursor).isNotBlank();

        int count = JsonPath.read(result.data(), "$.data.viewer.editingContext.representationDescriptions.pageInfo.count");
        assertThat(count).isEqualTo(5);

        List<String> representationLabels = JsonPath.read(result.data(), "$.data.viewer.editingContext.representationDescriptions.edges[*].node.label");
        assertThat(representationLabels).hasSize(5);
        assertThat(representationLabels).contains("General View", "Action Flow View", "Interconnection View", "Requirements Table View", "State Transition View");
    }

    @DisplayName("GIVEN the SysON Explorer View, WHEN we direct edit a ViewUsage typed with a standard diagram, THEN the type of ViewUsage is not part of the initial value of the direct edit")
    @GivenSysONServer({ ExplorerViewDirectEditTestProjectData.SCRIPT_PATH })
    @Test
    public void testDirectEditOnViewUsage() {
        var expandedIds = this.getAllTreeItemIds(ExplorerViewDirectEditTestProjectData.EDITING_CONTEXT_ID);
        var activatedFilters = List.of(SysONTreeFilterConstants.HIDE_ROOT_NAMESPACES_ID, SysONTreeFilterConstants.HIDE_MEMBERSHIPS_TREE_ITEM_FILTER_ID);
        var treeRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.sysONTreeViewDescriptionProvider.getDescriptionId(), expandedIds, activatedFilters);

        var treeEventInput = new ExplorerEventInput(UUID.randomUUID(), ExplorerViewDirectEditTestProjectData.EDITING_CONTEXT_ID, treeRepresentationId);
        var treeFlux = this.treeEventSubscriptionRunner.run(treeEventInput).flux();

        var hasProjectContent = this.getTreeRefreshedEventPayloadMatcher(List.of(this.view1GV, this.view2AFV, this.view3STV, this.view4IV, this.view1GVRepresentation));

        StepVerifier.create(treeFlux)
                .consumeNextWith(hasProjectContent)
                .then(this.triggerDirectEditTreeItemLabel(ExplorerViewDirectEditTestProjectData.EDITING_CONTEXT_ID, treeRepresentationId, UUID.fromString(ExplorerViewDirectEditTestProjectData.SemanticIds.VIEW_1_GV_ID), "view1"))
                .then(this.triggerDirectEditTreeItemLabel(ExplorerViewDirectEditTestProjectData.EDITING_CONTEXT_ID, treeRepresentationId,
                        UUID.fromString(ExplorerViewDirectEditTestProjectData.SemanticIds.VIEW_2_AFV_ID), "view2"))
                .then(this.triggerDirectEditTreeItemLabel(ExplorerViewDirectEditTestProjectData.EDITING_CONTEXT_ID, treeRepresentationId,
                        UUID.fromString(ExplorerViewDirectEditTestProjectData.SemanticIds.VIEW_3_STV_ID), "view3"))
                .then(this.triggerDirectEditTreeItemLabel(ExplorerViewDirectEditTestProjectData.EDITING_CONTEXT_ID, treeRepresentationId,
                        UUID.fromString(ExplorerViewDirectEditTestProjectData.SemanticIds.VIEW_4_IV_ID), "view4"))
                .then(this.triggerDirectEditTreeItemLabel(ExplorerViewDirectEditTestProjectData.EDITING_CONTEXT_ID, treeRepresentationId,
                        UUID.fromString(ExplorerViewDirectEditTestProjectData.GraphicalIds.VIEW_1_DIAGRAM_ID), "view1"))
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN the SysON Explorer View, WHEN we direct edit an Element with a shortName, THEN the shortName is not part of the initial value of the direct edit")
    @GivenSysONServer({ ExplorerViewDirectEditTestProjectData.SCRIPT_PATH })
    @Test
    public void testDirectEditOnElementWithShortName() {
        var expandedIds = this.getAllTreeItemIds(ExplorerViewDirectEditTestProjectData.EDITING_CONTEXT_ID);
        var activatedFilters = List.of(SysONTreeFilterConstants.HIDE_ROOT_NAMESPACES_ID, SysONTreeFilterConstants.HIDE_MEMBERSHIPS_TREE_ITEM_FILTER_ID);
        var treeRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.sysONTreeViewDescriptionProvider.getDescriptionId(), expandedIds, activatedFilters);

        var treeEventInput = new ExplorerEventInput(UUID.randomUUID(), ExplorerViewDirectEditTestProjectData.EDITING_CONTEXT_ID, treeRepresentationId);
        var treeFlux = this.treeEventSubscriptionRunner.run(treeEventInput).flux();

        var hasProjectContent = this.getTreeRefreshedEventPayloadMatcher(List.of(this.req1RU, this.req2RU));

        StepVerifier.create(treeFlux)
                .consumeNextWith(hasProjectContent)
                .then(this.triggerDirectEditTreeItemLabel(ExplorerViewDirectEditTestProjectData.EDITING_CONTEXT_ID, treeRepresentationId,
                        UUID.fromString(ExplorerViewDirectEditTestProjectData.SemanticIds.REQ1_RU_ID), "requirement1"))
                .then(this.triggerDirectEditTreeItemLabel(ExplorerViewDirectEditTestProjectData.EDITING_CONTEXT_ID, treeRepresentationId,
                        UUID.fromString(ExplorerViewDirectEditTestProjectData.SemanticIds.REQ2_RU_ID), ""))
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN the SysON Explorer View, WHEN we try to create a new element in an imported read-write user library, THEN a new element is added to the user library")
    @GivenSysONServer({ WithUserLibrariesTestProjectData.SCRIPT_PATH })
    @Test
    public void testImportedReadWriteUserLibraryEdition() {
        var expandedIds = this.getAllTreeItemIds(WithUserLibrariesTestProjectData.EDITING_CONTEXT_ID);
        expandedIds.add(LibrariesDirectory.LIBRARIES_DIRECTORY_ID);
        expandedIds.add(UserLibrariesDirectory.USER_LIBRARIES_DIRECTORY_ID);
        var activatedFilters = List.of(SysONTreeFilterConstants.HIDE_ROOT_NAMESPACES_ID, SysONTreeFilterConstants.HIDE_MEMBERSHIPS_TREE_ITEM_FILTER_ID);
        var treeRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.sysONTreeViewDescriptionProvider.getDescriptionId(), expandedIds, activatedFilters);

        var treeEventInput = new ExplorerEventInput(UUID.randomUUID(), WithUserLibrariesTestProjectData.EDITING_CONTEXT_ID, treeRepresentationId);
        var treeFlux = this.treeEventSubscriptionRunner.run(treeEventInput).flux();

        var hasProjectContent = this.getTreeRefreshedEventPayloadMatcher(List.of(this.userLibraryPackageRW));

        Runnable getContextualMenuRunnable = () -> {
            Map<String, Object> variables = Map.of(
                    "editingContextId", WithUserLibrariesTestProjectData.EDITING_CONTEXT_ID,
                    "representationId", treeRepresentationId,
                    "treeItemId", WithUserLibrariesTestProjectData.SemanticIds.RW_USER_LIBRARY_PACKAGE_ID);
            var result = this.treeItemContextMenuQueryRunner.run(variables);

            List<String> contextualMenuIds = JsonPath.read(result.data(), "$.data.viewer.editingContext.representation.description.contextMenu[*].id");
            assertThat(contextualMenuIds).contains("new-object", "expand-all");
        };

        Runnable createChildRunnable = () -> {
            var input = new CreateChildInput(UUID.randomUUID(), WithUserLibrariesTestProjectData.EDITING_CONTEXT_ID, WithUserLibrariesTestProjectData.SemanticIds.RW_USER_LIBRARY_PACKAGE_ID, "SysMLv2EditService-AcceptActionUsage");

            var result = this.createChildMutationRunner.run(input);

            String typename = JsonPath.read(result.data(), "$.data.createChild.__typename");
            assertThat(typename).isEqualTo(CreateChildSuccessPayload.class.getSimpleName());
        };

        Consumer<Object> updatedExplorerView = assertRefreshedTreeThat(tree -> {
            var package2TreeItem = this.findTreeItem(tree, WithUserLibrariesTestProjectData.SemanticIds.RW_USER_LIBRARY_PACKAGE_ID).orElseThrow();
            assertThat(package2TreeItem.getChildren()).size().isEqualTo(2);
            assertThat(package2TreeItem.getChildren().get(1).getKind()).contains("AcceptActionUsage");
        });

        StepVerifier.create(treeFlux)
                .consumeNextWith(hasProjectContent)
                .then(getContextualMenuRunnable)
                .then(createChildRunnable)
                .consumeNextWith(updatedExplorerView)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN the SysON Explorer View, WHEN we request the contextual menu of a tree item in an imported read-only user library, THEN the contextual menu does not contain entry to mutate the library")
    @GivenSysONServer({ WithUserLibrariesTestProjectData.SCRIPT_PATH })
    @Test
    public void testImportedReadOnlyUserLibraryEdition() {
        var expandedIds = this.getAllTreeItemIds(WithUserLibrariesTestProjectData.EDITING_CONTEXT_ID);
        expandedIds.add(LibrariesDirectory.LIBRARIES_DIRECTORY_ID);
        expandedIds.add(UserLibrariesDirectory.USER_LIBRARIES_DIRECTORY_ID);
        var activatedFilters = List.of(SysONTreeFilterConstants.HIDE_ROOT_NAMESPACES_ID, SysONTreeFilterConstants.HIDE_MEMBERSHIPS_TREE_ITEM_FILTER_ID);
        var treeRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.sysONTreeViewDescriptionProvider.getDescriptionId(), expandedIds, activatedFilters);

        var treeEventInput = new ExplorerEventInput(UUID.randomUUID(), WithUserLibrariesTestProjectData.EDITING_CONTEXT_ID, treeRepresentationId);
        var treeFlux = this.treeEventSubscriptionRunner.run(treeEventInput).flux();

        var hasProjectContent = this.getTreeRefreshedEventPayloadMatcher(List.of(this.userLibraryPackageRO));

        Runnable getContextualMenuRunnable = () -> {
            Map<String, Object> variables = Map.of(
                    "editingContextId", WithUserLibrariesTestProjectData.EDITING_CONTEXT_ID,
                    "representationId", treeRepresentationId,
                    "treeItemId", WithUserLibrariesTestProjectData.SemanticIds.RO_USER_LIBRARY_PACKAGE_ID);
            var result = this.treeItemContextMenuQueryRunner.run(variables);

            List<String> contextualMenuIds = JsonPath.read(result.data(), "$.data.viewer.editingContext.representation.description.contextMenu[*].id");
            assertThat(contextualMenuIds).doesNotContain("new-object", "new-representation", "newObjectsFromText", "duplicate-object").contains("expand-all");
        };

        StepVerifier.create(treeFlux)
                .consumeNextWith(hasProjectContent)
                .then(getContextualMenuRunnable)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN the SysON Explorer View, WHEN hide expose element filter is active, THEN the expose element are not return as tree item")
    @GivenSysONServer({ ExplorerViewDirectEditTestProjectData.SCRIPT_PATH })
    @Test
    public void testHideExposeElementFilter() {
        var expandedIds = this.getAllTreeItemIds(ExplorerViewDirectEditTestProjectData.EDITING_CONTEXT_ID);
        var activatedFilters = List.of(SysONTreeFilterConstants.HIDE_ROOT_NAMESPACES_ID, SysONTreeFilterConstants.HIDE_MEMBERSHIPS_TREE_ITEM_FILTER_ID, SysONTreeFilterConstants.HIDE_EXPOSE_ELEMENTS_TREE_ITEM_FILTER_ID);
        var treeRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.sysONTreeViewDescriptionProvider.getDescriptionId(), expandedIds, activatedFilters);

        var treeEventInput = new ExplorerEventInput(UUID.randomUUID(), ExplorerViewDirectEditTestProjectData.EDITING_CONTEXT_ID, treeRepresentationId);
        var treeFlux = this.treeEventSubscriptionRunner.run(treeEventInput).flux();

        var hasNoExposeElement = assertRefreshedTreeThat(tree -> assertThat(tree.getChildren()).allSatisfy(this::assertNoExposeChildren));

        StepVerifier.create(treeFlux)
                .consumeNextWith(hasNoExposeElement)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN the SysON Explorer View, WHEN hide user libraries filter is active, THEN no user libraries are return as tree item")
    @GivenSysONServer({ ProjectWithLibraryDependencyContainingLibraryPackageTestProjectData.SCRIPT_PATH })
    @Test
    public void testHideUserLibrariesFilter() {
        var expandedIds = List.of(UUID.nameUUIDFromBytes("SysON_Libraries_Directory".getBytes()).toString());
        var activatedFilters = List.of(SysONTreeFilterConstants.HIDE_USER_LIBRARIES_TREE_FILTER_ID);
        var treeRepresentationId = this.representationIdBuilder.buildExplorerRepresentationId(this.sysONTreeViewDescriptionProvider.getDescriptionId(), expandedIds, activatedFilters);

        var treeEventInput = new ExplorerEventInput(UUID.randomUUID(), ProjectWithLibraryDependencyContainingLibraryPackageTestProjectData.EDITING_CONTEXT, treeRepresentationId);
        var treeFlux = this.treeEventSubscriptionRunner.run(treeEventInput).flux();

        var hasNoExposeElement = assertRefreshedTreeThat(tree -> assertThat(tree.getChildren()).allSatisfy(this::assertNoUserLibraryChildren));

        StepVerifier.create(treeFlux)
                .consumeNextWith(hasNoExposeElement)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN the SysON Explorer View, WHEN querying the filters, THEN the syson filters are returned")
    @GivenSysONServer({ ExplorerViewDirectEditTestProjectData.SCRIPT_PATH })
    @Test
    public void testSysONFiltersOnSysONExplorer() {
        Map<String, Object> variables = Map.of(
                "editingContextId", ExplorerViewDirectEditTestProjectData.EDITING_CONTEXT_ID,
                "representationDescriptionId", this.sysONTreeViewDescriptionProvider.getDescriptionId()
        );
        var result = this.treeFiltersQueryRunner.run(variables);

        List<String> treeFilterIds = JsonPath.read(result.data(), "$.data.viewer.editingContext.representationDescription.filters[*].id");
        assertThat(treeFilterIds).containsExactlyInAnyOrder(SysONTreeFilterConstants.HIDE_MEMBERSHIPS_TREE_ITEM_FILTER_ID,
                SysONTreeFilterConstants.HIDE_KERML_STANDARD_LIBRARIES_TREE_FILTER_ID,
                SysONTreeFilterConstants.HIDE_SYSML_STANDARD_LIBRARIES_TREE_FILTER_ID,
                SysONTreeFilterConstants.HIDE_USER_LIBRARIES_TREE_FILTER_ID,
                SysONTreeFilterConstants.HIDE_ROOT_NAMESPACES_ID,
                SysONTreeFilterConstants.HIDE_EXPOSE_ELEMENTS_TREE_ITEM_FILTER_ID);
    }

    @DisplayName("GIVEN the Sirius Explorer View, WHEN querying the filters, THEN no syson filters are returned")
    @GivenSysONServer({ ExplorerViewDirectEditTestProjectData.SCRIPT_PATH })
    @Test
    public void testSysONFiltersOnSiriusExplorer() {
        Map<String, Object> variables = Map.of(
                "editingContextId", ExplorerViewDirectEditTestProjectData.EDITING_CONTEXT_ID,
                "representationDescriptionId", "explorer_tree_description"
        );
        var result = this.treeFiltersQueryRunner.run(variables);

        List<String> treeFilterIds = JsonPath.read(result.data(), "$.data.viewer.editingContext.representationDescription.filters[*].id");
        assertThat(treeFilterIds).isEmpty();
    }

    private List<String> getAllTreeItemIds(String editingContextId) {
        ExecuteEditingContextFunctionInput executeEditingContextFunctionInput = new ExecuteEditingContextFunctionInput(UUID.randomUUID(), editingContextId, (editingContext, input) -> {
            if (editingContext instanceof IEMFEditingContext emfEditingContext) {
                List<String> expandedIds = new ArrayList<>();
                ResourceSet resourceSet = emfEditingContext.getDomain().getResourceSet();
                List<Resource> resources = resourceSet.getResources().stream().filter(r -> !this.isStandardLibrary(r)).toList();
                for (Resource resource : resources) {
                    expandedIds.add(this.identityService.getId(resource));
                    resource.getAllContents().forEachRemaining(notifier -> {
                        if (notifier instanceof EObject) {
                            expandedIds.add(this.identityService.getId(notifier));
                        }
                    });
                }
                return new ExecuteEditingContextFunctionSuccessPayload(input.id(), expandedIds);
            } else {
                return new ErrorPayload(input.id(), List.of(new Message("Invalid editing context", MessageLevel.ERROR)));
            }
        });

        var payload = this.executeEditingContextFunctionRunner.execute(executeEditingContextFunctionInput).block();
        assertThat(payload).isInstanceOf(ExecuteEditingContextFunctionSuccessPayload.class);
        return (List<String>) ((ExecuteEditingContextFunctionSuccessPayload) payload).result();
    }

    private boolean isStandardLibrary(Resource resource) {
        var standardLib = resource.getContents().stream()
                .filter(Namespace.class::isInstance)
                .map(Namespace.class::cast)
                .filter(this::isRootNamespace)
                .flatMap(namespace -> namespace.getOwnedElement().stream())
                .filter(LibraryPackage.class::isInstance)
                .map(LibraryPackage.class::cast)
                .filter(libraryPackage -> libraryPackage.isIsStandard())
                .findFirst();
        return standardLib.isPresent();
    }

    private boolean isRootNamespace(Element element) {
        return element.eClass() == SysmlPackage.eINSTANCE.getNamespace()
                && element.getOwner() == null
                && element.getName() == null;
    }

    private Consumer<Object> getTreeRefreshedEventPayloadMatcher(List<TreeItemMatcher> treeItemMatchers) {
        return assertRefreshedTreeThat(tree -> {
            assertThat(treeItemMatchers).allMatch(treeItemMatcher -> {
                var treeItem = treeItemMatcher.treeItemFinder.apply(tree);
                return treeItemMatcher.treeItemPredicate.test(treeItem);
            });
        });
    }

    private Optional<TreeItem> findTreeItem(Tree tree, String treeItemId) {
        return tree.getChildren().stream()
                .map(child -> this.findTreeItem(child, treeItemId))
                .flatMap(Optional::stream)
                .findFirst();
    }

    private Optional<TreeItem> findTreeItem(TreeItem treeItem, String treeItemId) {
        if (treeItemId.equals(treeItem.getId())) {
            return Optional.of(treeItem);
        }
        return treeItem.getChildren().stream()
                .map(child -> this.findTreeItem(child, treeItemId))
                .flatMap(Optional::stream)
                .findFirst();
    }

    private Runnable triggerDirectEditTreeItemLabel(String editingContextId, String treeId, UUID treeItemId, String expectedLabel) {
        return () -> {
            Map<String, Object> variables = Map.of(
                    "editingContextId", editingContextId,
                    "representationId", treeId,
                    "treeItemId", treeItemId);
            var result = this.initialDirectEditTreeItemLabelQueryRunner.run(variables);

            String initialDirectEditLabel = JsonPath.read(result.data(), "$.data.viewer.editingContext.representation.description.initialDirectEditTreeItemLabel");
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

    private void assertNoUserLibraryChildren(TreeItem item) {
        if (item == null) {
            return;
        }
        assertThat(item.getLabel().toString()).isNotEqualTo("User libraries");
        List<TreeItem> children = item.getChildren();
        if (children != null) {
            for (TreeItem child : children) {
                this.assertNoUserLibraryChildren(child);
            }
        }
    }
}
