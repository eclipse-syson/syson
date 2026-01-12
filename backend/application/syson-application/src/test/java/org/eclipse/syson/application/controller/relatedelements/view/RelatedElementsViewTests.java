/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
package org.eclipse.syson.application.controller.relatedelements.view;

import static org.eclipse.sirius.components.forms.tests.assertions.FormAssertions.assertThat;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import org.eclipse.sirius.components.collaborative.forms.dto.FormRefreshedEventPayload;
import org.eclipse.sirius.components.forms.Form;
import org.eclipse.sirius.components.forms.GroupDisplayMode;
import org.eclipse.sirius.components.forms.tests.navigation.FormNavigator;
import org.eclipse.sirius.web.application.views.relatedelements.dto.RelatedElementsEventInput;
import org.eclipse.sirius.web.tests.graphql.RelatedElementsEventSubscriptionRunner;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.sirius.web.tests.services.representation.RepresentationIdBuilder;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.SysONTestsProperties;
import org.eclipse.syson.application.data.InterconnectionViewEmptyTestProjectData;
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
 * Tests the Related Elements View.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = { SysONTestsProperties.NO_DEFAULT_LIBRARIES_PROPERTY })
public class RelatedElementsViewTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private RelatedElementsEventSubscriptionRunner relatedElementsEventSubscriptionRunner;

    @Autowired
    private RepresentationIdBuilder representationIdBuilder;

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("GIVEN an empty SysML Project, WHEN the Related Elements View is requested, THEN no error is returned")
    @Sql(scripts = { InterconnectionViewEmptyTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void getRelatedElementsView() {
        var relatedElementRepresentationId = this.representationIdBuilder.buildRelatedElementsRepresentationId(List.of(InterconnectionViewEmptyTestProjectData.SemanticIds.PACKAGE_1_ID.toString()));
        var input = new RelatedElementsEventInput(UUID.randomUUID(), InterconnectionViewEmptyTestProjectData.EDITING_CONTEXT_ID, relatedElementRepresentationId);
        var flux = this.relatedElementsEventSubscriptionRunner.run(input)
                .flux()
                .filter(FormRefreshedEventPayload.class::isInstance);

        Predicate<Form> formPredicate = form -> {
            var groupNavigator = new FormNavigator(form).page("partDiagram").group("Related Elements");
            assertThat(groupNavigator.getGroup()).hasDisplayMode(GroupDisplayMode.TOGGLEABLE_AREAS);
            return true;
        };

        Predicate<Object> formContentMatcher = object -> Optional.of(object)
                .filter(FormRefreshedEventPayload.class::isInstance)
                .map(FormRefreshedEventPayload.class::cast)
                .map(FormRefreshedEventPayload::form)
                .filter(formPredicate)
                .isPresent();

        StepVerifier.create(flux)
                .expectNextMatches(formContentMatcher)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
