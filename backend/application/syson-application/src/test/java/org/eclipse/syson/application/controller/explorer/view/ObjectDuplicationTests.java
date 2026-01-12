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
package org.eclipse.syson.application.controller.explorer.view;

import static org.assertj.core.api.Assertions.assertThat;

import com.jayway.jsonpath.JsonPath;

import java.util.List;
import java.util.UUID;

import org.eclipse.sirius.web.application.views.explorer.dto.DuplicateObjectInput;
import org.eclipse.sirius.web.application.views.explorer.dto.DuplicateObjectSuccessPayload;
import org.eclipse.sirius.web.application.views.query.dto.EvaluateExpressionInput;
import org.eclipse.sirius.web.tests.graphql.DuplicateObjectMutationRunner;
import org.eclipse.sirius.web.tests.graphql.EvaluateExpressionMutationRunner;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
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

/**
 * Test class for object duplication tests.
 *
 * @author Arthur Daussy
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ObjectDuplicationTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private DuplicateObjectMutationRunner duplicateObjectMutationRunner;

    @Autowired
    private EvaluateExpressionMutationRunner evaluateExpressionMutationRunner;

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @Test
    @DisplayName("GIVEN a model with a part, WHEN duplicating this part, THEN the duplicated part is copied in the given container with an intermediate membership")
    @Sql(scripts = { SimpleProjectElementsTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode =
            SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void checkSimpleDuplication() {
        var duplicateInput = new DuplicateObjectInput(UUID.randomUUID(),
                SimpleProjectElementsTestProjectData.EDITING_CONTEXT_ID,
                SimpleProjectElementsTestProjectData.SemanticIds.PART_ID,
                SimpleProjectElementsTestProjectData.SemanticIds.PACKAGE_1_ID,
                "membership:OwningMembership", true, true, false);
        var result = this.duplicateObjectMutationRunner.run(duplicateInput);

        String typename = JsonPath.read(result.data(), "$.data.duplicateObject.__typename");
        assertThat(typename).isEqualTo(DuplicateObjectSuccessPayload.class.getSimpleName());

        String objectId = JsonPath.read(result.data(), "$.data.duplicateObject.object.id");
        assertThat(objectId).isNotBlank().isNotEqualTo(SimpleProjectElementsTestProjectData.SemanticIds.PART_ID);
        String label = JsonPath.read(result.data(), "$.data.duplicateObject.object.label");
        assertThat(label).isEqualTo("p-copy");

        var queryResult = this.evaluateExpressionMutationRunner.run(
                new EvaluateExpressionInput(UUID.randomUUID(),
                        SimpleProjectElementsTestProjectData.EDITING_CONTEXT_ID,
                        "aql:self.eContainer().eClass().name", List.of(objectId)));

        String stringResult = JsonPath.read(queryResult.data(), "$.data.evaluateExpression.result.stringValue");

        assertThat(stringResult).isEqualTo("OwningMembership");

        queryResult = this.evaluateExpressionMutationRunner.run(
                new EvaluateExpressionInput(UUID.randomUUID(),
                        SimpleProjectElementsTestProjectData.EDITING_CONTEXT_ID,
                        "aql:self.eContainer().eContainer().name",
                        List.of(objectId)));

        stringResult = JsonPath.read(queryResult.data(), "$.data.evaluateExpression.result.stringValue");
        assertThat(stringResult).isEqualTo("Package1");
    }
}
