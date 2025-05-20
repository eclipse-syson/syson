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
package org.eclipse.syson.application.validation;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import org.eclipse.sirius.components.collaborative.validation.dto.ValidationEventInput;
import org.eclipse.sirius.components.collaborative.validation.dto.ValidationRefreshedEventPayload;
import org.eclipse.sirius.components.graphql.tests.api.IGraphQLRequestor;
import org.eclipse.sirius.web.tests.services.api.IGivenCommittedTransaction;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.sirius.web.tests.services.representation.RepresentationIdBuilder;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.GeneralViewWithTopNodesTestProjectData;
import org.eclipse.syson.application.data.SimpleProjectElementsTestProjectData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import graphql.execution.DataFetcherResult;
import reactor.test.StepVerifier;

/**
 * Validation View related tests.
 *
 * @author arichard
 */
@Transactional
@SuppressWarnings("checkstyle:MultipleStringLiterals")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(OutputCaptureExtension.class)
public class ValidationRulesTests extends AbstractIntegrationTests {

    private static final String GET_VALIDATION_EVENT_SUBSCRIPTION = """
            subscription validationEvent($input: ValidationEventInput!) {
              validationEvent(input: $input) {
                __typename
              }
            }
            """;

    @Autowired
    private IGivenCommittedTransaction givenCommittedTransaction;

    @Autowired
    private IGraphQLRequestor graphQLRequestor;

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private RepresentationIdBuilder representationIdBuilder;

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @Test
    @DisplayName("GIVEN a simple project, WHEN we subscribe to its validation events, THEN the validation data are sent and backend console has no AQL errors")
    @Sql(scripts = { SimpleProjectElementsTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenASimpleProjectWhenWeSubscribeToItsValidationEventsThenTheValidationDataAreSentAndBackendConsoleHasNoAQLErrors(CapturedOutput capturedOutput) {
        var input = new ValidationEventInput(UUID.randomUUID(), SimpleProjectElementsTestProjectData.EDITING_CONTEXT_ID, this.representationIdBuilder.buildValidationRepresentationId());
        var flux = this.graphQLRequestor.subscribe(GET_VALIDATION_EVENT_SUBSCRIPTION, input)
                .filter(DataFetcherResult.class::isInstance)
                .map(DataFetcherResult.class::cast)
                .map(DataFetcherResult::getData)
                .filter(ValidationRefreshedEventPayload.class::isInstance)
                .map(ValidationRefreshedEventPayload.class::cast);

        this.givenCommittedTransaction.commit();

        Consumer<ValidationRefreshedEventPayload> validationContentConsumer = payload -> Optional.of(payload)
                .map(ValidationRefreshedEventPayload::validation)
                .ifPresentOrElse(validation -> {
                    assertNotNull(validation);
                    assertFalse(capturedOutput.getOut().contains("AQLInterpreter"));
                    // Some constraints are not respected, should be 0 when all validation rules will be valid, all
                    // derived references will be implemented and all implicit specialization added.
                    assertTrue(validation.getDiagnostics().size() > 0);
                }, () -> fail("Missing validation"));

        StepVerifier.create(flux)
                .consumeNextWith(validationContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }

    @Test
    @DisplayName("GIVEN a project with many elements, WHEN we subscribe to its validation events, THEN the validation data are sent and backend console has no AQL errors")
    @Sql(scripts = { GeneralViewWithTopNodesTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenAProjectWithManyElementsWhenWeSubscribeToItsValidationEventsThenTheValidationDataAreSentAndBackendConsoleHasNoAQLErrors(CapturedOutput capturedOutput) {
        var input = new ValidationEventInput(UUID.randomUUID(), GeneralViewWithTopNodesTestProjectData.EDITING_CONTEXT_ID, this.representationIdBuilder.buildValidationRepresentationId());
        var flux = this.graphQLRequestor.subscribe(GET_VALIDATION_EVENT_SUBSCRIPTION, input)
                .filter(DataFetcherResult.class::isInstance)
                .map(DataFetcherResult.class::cast)
                .map(DataFetcherResult::getData)
                .filter(ValidationRefreshedEventPayload.class::isInstance)
                .map(ValidationRefreshedEventPayload.class::cast);

        this.givenCommittedTransaction.commit();

        Consumer<ValidationRefreshedEventPayload> validationContentConsumer = payload -> Optional.of(payload)
                .map(ValidationRefreshedEventPayload::validation)
                .ifPresentOrElse(validation -> {
                    assertNotNull(validation);
                    // It remains some AQL Errors in console, should be false when all validation rules will be valid
                    assertTrue(capturedOutput.getOut().contains("AQLInterpreter"));
                    // Some constraints are not respected, should be 0 when all validation rules will be valid, all
                    // derived references will be implemented and all implicit specialization added.
                    assertTrue(validation.getDiagnostics().size() > 0);
                }, () -> fail("Missing validation"));

        StepVerifier.create(flux)
                .consumeNextWith(validationContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));
    }
}
