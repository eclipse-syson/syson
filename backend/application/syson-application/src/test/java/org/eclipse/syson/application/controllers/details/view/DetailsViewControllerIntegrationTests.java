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
package org.eclipse.syson.application.controllers.details.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.components.forms.tests.FormEventPayloadConsumer.assertRefreshedFormThat;

import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.forms.dto.FormRefreshedEventPayload;
import org.eclipse.sirius.components.forms.Checkbox;
import org.eclipse.sirius.components.forms.LabelWidget;
import org.eclipse.sirius.components.forms.Textfield;
import org.eclipse.sirius.components.forms.tests.graphql.HelpTextQueryRunner;
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
    private HelpTextQueryRunner helpTextQueryRunner;

    @Autowired
    private RepresentationIdBuilder representationIdBuilder;

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @Test
    @DisplayName("GIVEN a PartUsage, WHEN we subscribe to its properties events, THEN the form is sent")
    @Sql(scripts = { SimpleProjectElementsTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenAPartUsageWhenWeSubscribeToItsPropertiesEventThenTheFormIsSent() {
        var detailsRepresentationId = this.representationIdBuilder.buildDetailsRepresentationId(List.of(SimpleProjectElementsTestProjectData.SemanticIds.PART_ID));
        var input = new DetailsEventInput(UUID.randomUUID(), SimpleProjectElementsTestProjectData.EDITING_CONTEXT_ID, detailsRepresentationId);
        var flux = this.detailsEventSubscriptionRunner.run(input)
                .flux()
                .filter(FormRefreshedEventPayload.class::isInstance);

        Consumer<Object> formContentConsumer = assertRefreshedFormThat(form -> {
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
        });

        StepVerifier.create(flux)
                .consumeNextWith(formContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));

    }

    @Test
    @DisplayName("GIVEN a PartUsage, WHEN we request the help text, THEN the help text is send")
    @Sql(scripts = { SimpleProjectElementsTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenAPartUsageWhenWeRequestHelpTextThenHelpTextIsSend() {
        var detailsRepresentationId = this.representationIdBuilder.buildDetailsRepresentationId(List.of(SimpleProjectElementsTestProjectData.SemanticIds.PART_ID));
        var input = new DetailsEventInput(UUID.randomUUID(), SimpleProjectElementsTestProjectData.EDITING_CONTEXT_ID, detailsRepresentationId);
        var flux = this.detailsEventSubscriptionRunner.run(input)
                .flux()
                .filter(FormRefreshedEventPayload.class::isInstance);

        var isCompositeWidgetId = new AtomicReference<String>();
        var isReferenceWidgetId = new AtomicReference<String>();
        var isPortionWidgetId = new AtomicReference<String>();
        Consumer<Object> formContentConsumer = assertRefreshedFormThat(form -> {
            var isCompositeWidget = new FormNavigator(form).page("Advanced").group("Part Properties").findWidget("Is Composite", Checkbox.class);
            var isReferenceWidget = new FormNavigator(form).page("Advanced").group("Part Properties").findWidget("Is Reference", Checkbox.class);
            var isPortionWidget = new FormNavigator(form).page("Advanced").group("Part Properties").findWidget("Is Portion", Checkbox.class);
            assertThat(isCompositeWidget).isNotNull();
            assertThat(isReferenceWidget).isNotNull();
            isCompositeWidgetId.set(isCompositeWidget.getId());
            isReferenceWidgetId.set(isReferenceWidget.getId());
            isPortionWidgetId.set(isPortionWidget.getId());
        });

        Runnable requestIsCompositeHelpText = () -> {
            Map<String, Object> variables = Map.of(
                    "editingContextId", SimpleProjectElementsTestProjectData.EDITING_CONTEXT_ID,
                    "representationId", detailsRepresentationId,
                    "widgetId", isCompositeWidgetId.get()
            );
            var result = this.helpTextQueryRunner.run(variables);

            String helpText = JsonPath.read(result.data(), "$.data.viewer.editingContext.representation.description.helpText");
            assertThat(helpText).isEqualTo("Opposite value of isReference");
        };

        Runnable requestIsReferenceHelpText = () -> {
            Map<String, Object> variables = Map.of(
                    "editingContextId", SimpleProjectElementsTestProjectData.EDITING_CONTEXT_ID,
                    "representationId", detailsRepresentationId,
                    "widgetId", isReferenceWidgetId.get()
            );
            var result = this.helpTextQueryRunner.run(variables);

            String helpText = JsonPath.read(result.data(), "$.data.viewer.editingContext.representation.description.helpText");
            assertThat(helpText).isEqualTo("Opposite value of isComposite (cannot be edited, edit isComposite instead)");
        };
        Runnable requestIsPortionHelpText = () -> {
            Map<String, Object> variables = Map.of(
                    "editingContextId", SimpleProjectElementsTestProjectData.EDITING_CONTEXT_ID,
                    "representationId", detailsRepresentationId,
                    "widgetId", isPortionWidgetId.get()
            );
            var result = this.helpTextQueryRunner.run(variables);

            String helpText = JsonPath.read(result.data(), "$.data.viewer.editingContext.representation.description.helpText");
            assertThat(helpText).isEmpty();
        };

        StepVerifier.create(flux)
                .consumeNextWith(formContentConsumer)
                .then(requestIsCompositeHelpText)
                .then(requestIsReferenceHelpText)
                .then(requestIsPortionHelpText)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
