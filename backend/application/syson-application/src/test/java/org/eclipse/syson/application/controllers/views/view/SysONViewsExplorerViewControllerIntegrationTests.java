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
package org.eclipse.syson.application.controllers.views.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.components.trees.tests.TreeEventPayloadConsumer.assertRefreshedTreeThat;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import org.eclipse.sirius.components.diagrams.Diagram;
import org.eclipse.sirius.components.tables.Table;
import org.eclipse.sirius.web.application.views.viewsexplorer.ViewsExplorerEventInput;
import org.eclipse.sirius.web.application.views.viewsexplorer.services.ViewsExplorerTreeDescriptionProvider;
import org.eclipse.sirius.web.tests.graphql.ViewsExplorerEventSubscriptionRunner;
import org.eclipse.sirius.web.tests.services.representation.RepresentationIdBuilder;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.GivenSysONServer;
import org.eclipse.syson.application.data.InterconnectionViewEmptyTestProjectData;
import org.eclipse.syson.application.data.RequirementsTableTestProjectData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;

/**
 * Integration tests of the Views view.
 *
 * @author frouene
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SysONViewsExplorerViewControllerIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    private ViewsExplorerEventSubscriptionRunner viewsExplorerEventSubscriptionRunner;

    @DisplayName("GIVEN a project with a table, WHEN we subscribe to views events, THEN then the tree contains the table representation leaf")
    @GivenSysONServer({ RequirementsTableTestProjectData.SCRIPT_PATH })
    @Test
    public void viewsWithTableRepresentation() {
        var representationId = new RepresentationIdBuilder().buildViewsExplorerViewRepresentationId(
                List.of(Table.KIND));
        var defaultExplorerInput = new ViewsExplorerEventInput(UUID.randomUUID(), RequirementsTableTestProjectData.EDITING_CONTEXT_ID, representationId);
        var defaultFlux = this.viewsExplorerEventSubscriptionRunner.run(defaultExplorerInput).flux();

        Consumer<Object> initialDefaultViewsContentConsumer = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            assertThat(tree.getDescriptionId()).isEqualTo(ViewsExplorerTreeDescriptionProvider.DESCRIPTION_ID);
            assertThat(tree.getChildren()).hasSize(1);
            assertThat(tree.getChildren()).allSatisfy(treeItem -> assertThat(treeItem.getChildren()).hasSize(1));
            assertThat(tree.getChildren().get(0).getChildren()).allSatisfy(treeItem -> assertThat(treeItem.getLabel().toString()).isEqualTo("REQUIREMENTS TABLE VIEW (1)"));
        });

        StepVerifier.create(defaultFlux)
                .consumeNextWith(initialDefaultViewsContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @DisplayName("GIVEN a project with an interconnection view, WHEN we subscribe to views events, THEN then the tree contains the interconnection view representation leaf")
    @GivenSysONServer({ InterconnectionViewEmptyTestProjectData.SCRIPT_PATH })
    @Test
    public void viewsWithInterconnectionViewRepresentation() {
        var representationId = new RepresentationIdBuilder().buildViewsExplorerViewRepresentationId(
                List.of(Diagram.KIND));
        var defaultExplorerInput = new ViewsExplorerEventInput(UUID.randomUUID(), InterconnectionViewEmptyTestProjectData.EDITING_CONTEXT_ID, representationId);
        var defaultFlux = this.viewsExplorerEventSubscriptionRunner.run(defaultExplorerInput).flux();

        Consumer<Object> initialDefaultViewsContentConsumer = assertRefreshedTreeThat(tree -> {
            assertThat(tree).isNotNull();
            assertThat(tree.getDescriptionId()).isEqualTo(ViewsExplorerTreeDescriptionProvider.DESCRIPTION_ID);
            assertThat(tree.getChildren()).hasSize(1);
            assertThat(tree.getChildren()).allSatisfy(treeItem -> assertThat(treeItem.getChildren()).hasSize(1));
            assertThat(tree.getChildren().get(0).getChildren()).allSatisfy(treeItem -> assertThat(treeItem.getLabel().toString()).isEqualTo("INTERCONNECTION VIEW (1)"));
        });

        StepVerifier.create(defaultFlux)
                .consumeNextWith(initialDefaultViewsContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
