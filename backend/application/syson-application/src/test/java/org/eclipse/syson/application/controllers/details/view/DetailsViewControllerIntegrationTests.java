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
package org.eclipse.syson.application.controllers.details.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.forms.dto.FormRefreshedEventPayload;
import org.eclipse.sirius.components.forms.LabelWidget;
import org.eclipse.sirius.components.forms.Textfield;
import org.eclipse.sirius.components.forms.tests.navigation.FormNavigator;
import org.eclipse.sirius.web.application.views.details.dto.DetailsEventInput;
import org.eclipse.sirius.web.tests.graphql.DetailsEventSubscriptionRunner;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.sirius.web.tests.services.representation.RepresentationIdBuilder;
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

import graphql.execution.DataFetcherResult;
import reactor.test.StepVerifier;

/**
 * Integration tests of the details view.
 *
 * @author gdaniel
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DetailsViewControllerIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private DetailsEventSubscriptionRunner detailsEventSubscriptionRunner;

    @Autowired
    private RepresentationIdBuilder representationIdBuilder;

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @Test
    @DisplayName("Given a PartUsage, when we subscribe to its properties events, then the form is sent")
    @Sql(scripts = { SimpleProjectElementsTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenAPartUsageWhenWeSubscribeToItsPropertiesEventThenTheFormIsSent() {
        var detailsRepresentationId = this.representationIdBuilder.buildDetailsRepresentationId(List.of(SimpleProjectElementsTestProjectData.SemanticIds.PART_ID));
        var input = new DetailsEventInput(UUID.randomUUID(), SimpleProjectElementsTestProjectData.EDITING_CONTEXT_ID, detailsRepresentationId);
        var flux = this.detailsEventSubscriptionRunner.run(input);

        Consumer<Object> formContentConsumer = object -> Optional.of(object)
                .filter(DataFetcherResult.class::isInstance)
                .map(DataFetcherResult.class::cast)
                .map(DataFetcherResult::getData)
                .filter(FormRefreshedEventPayload.class::isInstance)
                .map(FormRefreshedEventPayload.class::cast)
                .map(FormRefreshedEventPayload::form)
                .ifPresentOrElse(form -> {
                    assertThat(form.getPages())
                            .hasSize(2)
                            .satisfiesExactly(page1 -> assertThat(page1.getLabel()).isEqualTo("Core"),
                                    page2 -> assertThat(page2.getLabel()).isEqualTo("Advanced"));
                    var coreGroupNavigator = new FormNavigator(form).page("Core").group("Part Properties");
                    var declaredNameTextfield = coreGroupNavigator.findWidget("Declared Name", Textfield.class);
                    assertThat(declaredNameTextfield.getValue()).isEqualTo("p");
                    var qualifiedNameLabel = coreGroupNavigator.findWidget("Qualified Name", LabelWidget.class);
                    assertThat(qualifiedNameLabel.getValue()).isEqualTo("Package1::p");
                    var advancedGroupNavigator = new FormNavigator(form).page("Advanced").group("Part Properties");
                    var nameLabel = advancedGroupNavigator.findWidget("Name", LabelWidget.class);
                    assertThat(nameLabel.getValue()).isEqualTo("p");
                }, () -> fail("Missing form"));

        StepVerifier.create(flux)
                .consumeNextWith(formContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));

    }

}
