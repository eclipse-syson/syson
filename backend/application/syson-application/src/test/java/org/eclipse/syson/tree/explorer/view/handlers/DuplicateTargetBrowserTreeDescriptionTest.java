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
import static org.eclipse.sirius.components.trees.tests.TreeEventPayloadConsumer.assertRefreshedTreeThat;

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.browser.dto.ModelBrowserEventInput;
import org.eclipse.sirius.components.trees.TreeItem;
import org.eclipse.sirius.components.trees.tests.graphql.ExpandAllTreePathQueryRunner;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.sirius.web.tests.services.modelbrowser.ModelBrowserEventSubscriptionRunner;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.SimpleProjectElementsTestProjectData;
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
 * Test the browser used to select the future owner of a duplication.
 *
 * @author Arthur Daussy
 */
@Transactional
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DuplicateTargetBrowserTreeDescriptionTest extends AbstractIntegrationTests {

    private static final String TREE_ID = "modelBrowser://container?ownerKind=siriusComponents%3A%2F%2Fsemantic%3Fdomain%3Dsysml%26entity%3DPartUsage&targetType=siriusComponents%3A%2F%2Fsemantic" +
            "%3Fdomain%3Dsysml%26entity%3DPartUsage&ownerId=a4f51a38-bfeb-4e0d-a870-55f8fe90405e&descriptionId=duplicate-target-browser&isContainment=false&expandedIds=[${expanded-ids}]";

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private ModelBrowserEventSubscriptionRunner treeEventSubscriptionRunner;

    @Autowired
    private ExpandAllTreePathQueryRunner expandAllTreePathQueryRunner;

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @Test
    @DisplayName("GIVEN a SysML model with a part, WHEN duplicating this part, THEN a valid browser should be displayed")
    @Sql(scripts = { SimpleProjectElementsTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode =
            SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void checkDuplicateTargetBrowserTree() {
        var representationId = TREE_ID.replace("${expanded-ids}",
                SimpleProjectElementsTestProjectData.DOCUMENT_ID + ","
                        + SimpleProjectElementsTestProjectData.SemanticIds.ROOT_NAMESPACE + ","
                        + SimpleProjectElementsTestProjectData.SemanticIds.PACKAGE_1_ID);
        var input = new ModelBrowserEventInput(UUID.randomUUID(), SimpleProjectElementsTestProjectData.EDITING_CONTEXT_ID, representationId);
        var flux = this.treeEventSubscriptionRunner.run(input).flux();


        String[] treeId = new String[] { null };

        Consumer<Object> initialTreeContentConsumer = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            assertThat(tree.getChildren()).hasSize(1);
            treeId[0] = tree.getId();

            TreeItem resourceItem = tree.getChildren().get(0);

            assertThat(resourceItem.getLabel().toString()).isEqualTo("SysMLv2.sysml");
            assertThat(resourceItem.isHasChildren()).isEqualTo(true);
            assertThat(resourceItem.isEditable()).isEqualTo(false);
            assertThat(resourceItem.isSelectable()).isEqualTo(false);
            assertThat(resourceItem.isDeletable()).isEqualTo(false);
            assertThat(resourceItem.getId()).isEqualTo(SimpleProjectElementsTestProjectData.DOCUMENT_ID);
            assertThat(resourceItem.getChildren()).hasSize(1);

            TreeItem rootNamespaceItem = resourceItem.getChildren().get(0);
            assertThat(rootNamespaceItem.getLabel().toString()).isEqualTo("Namespace");
            assertThat(rootNamespaceItem.isHasChildren()).isEqualTo(true);
            assertThat(rootNamespaceItem.isEditable()).isEqualTo(false);
            assertThat(rootNamespaceItem.isSelectable()).isEqualTo(true);
            assertThat(rootNamespaceItem.isDeletable()).isEqualTo(false);
            assertThat(rootNamespaceItem.getChildren()).hasSize(2);
            assertThat(rootNamespaceItem.getId()).isEqualTo(SimpleProjectElementsTestProjectData.SemanticIds.ROOT_NAMESPACE);

            TreeItem package1Item = rootNamespaceItem.getChildren().get(0);
            assertThat(package1Item.getLabel().toString()).isEqualTo("Package1");
            assertThat(package1Item.isHasChildren()).isEqualTo(true);
            assertThat(package1Item.isEditable()).isEqualTo(false);
            assertThat(package1Item.isSelectable()).isEqualTo(true);
            assertThat(package1Item.isDeletable()).isEqualTo(false);
            assertThat(package1Item.getChildren()).hasSize(1);
            assertThat(package1Item.getId()).isEqualTo(SimpleProjectElementsTestProjectData.SemanticIds.PACKAGE_1_ID);

            TreeItem partItem = package1Item.getChildren().get(0);
            assertThat(partItem.getLabel().toString()).isEqualTo("p");
            assertThat(partItem.isHasChildren()).isEqualTo(false);
            assertThat(partItem.isEditable()).isEqualTo(false);
            assertThat(partItem.isSelectable()).isEqualTo(true);
            assertThat(partItem.isDeletable()).isEqualTo(false);
            assertThat(partItem.getId()).isEqualTo(SimpleProjectElementsTestProjectData.SemanticIds.PART_ID);
        });

        Runnable getTreePath = () -> {
            Map<String, Object> variables = Map.of(
                    "editingContextId", SimpleProjectElementsTestProjectData.EDITING_CONTEXT_ID,
                    "treeId", treeId[0],
                    "treeItemId", SimpleProjectElementsTestProjectData.SemanticIds.PACKAGE2_ID
            );

            var result = this.expandAllTreePathQueryRunner.run(variables);
            List<String> treeItemIdsToExpand = JsonPath.read(result.data(), "$.data.viewer.editingContext.expandAllTreePath.treeItemIdsToExpand");
            assertThat(treeItemIdsToExpand).hasSize(2);
        };

        StepVerifier.create(flux)
                .consumeNextWith(initialTreeContentConsumer)
                .then(getTreePath)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

}
