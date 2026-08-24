/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
package org.eclipse.syson.application.controllers.details.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.components.trees.tests.TreeEventPayloadConsumer.assertRefreshedTreeThat;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.browser.dto.ModelBrowserEventInput;
import org.eclipse.sirius.components.trees.TreeItem;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.sirius.web.tests.services.modelbrowser.ModelBrowserEventSubscriptionRunner;
import org.eclipse.sirius.web.tests.services.representation.RepresentationIdBuilder;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.SimpleProjectElementsTestProjectData;
import org.eclipse.syson.tests.api.GivenSysONServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;

/**
 * Integration tests of the Reference Widget model-browser tree.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReferenceWidgetModelBrowserTreeDescriptionTest extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private ModelBrowserEventSubscriptionRunner modelBrowserEventSubscriptionRunner;

    @Autowired
    private RepresentationIdBuilder representationIdBuilder;

    @BeforeEach
    void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    /**
     * Verifies that Reference Widget model browsers flatten root namespaces and memberships.
     */
    @Test
    @GivenSysONServer({ SimpleProjectElementsTestProjectData.SCRIPT_PATH })
    @DisplayName("GIVEN a Reference Widget model browser, WHEN it is displayed, THEN root namespaces and memberships are hidden")
    void referenceWidgetModelBrowserHidesRootNamespacesAndMemberships() {
        var representationId = this.representationIdBuilder.buildModelBrowserRepresentationId(
                "reference",
                "siriusComponents://semantic?domain=sysml&entity=PartUsage",
                "siriusComponents://semantic?domain=sysml&entity=PartDefinition",
                SimpleProjectElementsTestProjectData.SemanticIds.PART_ID,
                "reference-widget",
                false,
                List.of(
                        SimpleProjectElementsTestProjectData.DOCUMENT_ID,
                        SimpleProjectElementsTestProjectData.SemanticIds.PACKAGE_1_ID,
                        SimpleProjectElementsTestProjectData.SemanticIds.PACKAGE2_ID));
        var input = new ModelBrowserEventInput(UUID.randomUUID(), SimpleProjectElementsTestProjectData.EDITING_CONTEXT_ID, representationId);
        var flux = this.modelBrowserEventSubscriptionRunner.run(input).flux();

        Consumer<Object> treeContentConsumer = assertRefreshedTreeThat(tree -> {
            TreeItem resourceItem = tree.getChildren().stream()
                    .filter(treeItem -> treeItem.getId().equals(SimpleProjectElementsTestProjectData.DOCUMENT_ID))
                    .findFirst()
                    .orElseThrow();
            assertThat(resourceItem.getChildren().stream().map(TreeItem::getId))
                    .containsExactly(
                            SimpleProjectElementsTestProjectData.SemanticIds.PACKAGE_1_ID,
                            SimpleProjectElementsTestProjectData.SemanticIds.PACKAGE2_ID)
                    .doesNotContain(SimpleProjectElementsTestProjectData.SemanticIds.ROOT_NAMESPACE);

            TreeItem package1Item = resourceItem.getChildren().get(0);
            assertThat(package1Item.getChildren().stream().map(TreeItem::getId))
                    .contains(SimpleProjectElementsTestProjectData.SemanticIds.PART_ID);

            TreeItem package2Item = resourceItem.getChildren().get(1);
            assertThat(package2Item.getChildren()).singleElement().satisfies(partDefinitionItem -> {
                assertThat(partDefinitionItem.getId()).isEqualTo(SimpleProjectElementsTestProjectData.SemanticIds.PART_DEF_ID);
                assertThat(partDefinitionItem.isSelectable()).isTrue();
            });
        });

        StepVerifier.create(flux)
                .consumeNextWith(treeContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
