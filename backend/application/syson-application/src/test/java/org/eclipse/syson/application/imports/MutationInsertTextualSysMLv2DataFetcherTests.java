/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.application.imports;

import static org.assertj.core.api.Assertions.assertThat;

import com.jayway.jsonpath.JsonPath;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IInput;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionInput;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionRunner;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.web.tests.services.api.IGivenCommittedTransaction;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.SysMLv2Identifiers;
import org.eclipse.syson.sysml.datafetchers.MutationInsertTextualSysMLv2DataFetcher;
import org.eclipse.syson.sysml.dto.InsertTextualSysMLv2Input;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;

/**
 * Integration test for {@link MutationInsertTextualSysMLv2DataFetcher}.
 *
 * @author Arthur Daussy
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MutationInsertTextualSysMLv2DataFetcherTests extends AbstractIntegrationTests {

    private static final String SIMPLE_PROJECT_PACKAGE1 = "d51791b8-6666-46e3-8c60-c975e1f3e490";

    @Autowired
    private IGivenCommittedTransaction givenCommittedTransaction;

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private ExecuteEditingContextFunctionRunner executeEditingContextFunctionRunner;

    @Autowired
    private MutationInsertTextualSysMLv2DataRunner queryRunner;

    @Autowired
    private IObjectSearchService objectSearchService;

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void testCreationFromText() {
        this.givenCommittedTransaction.commit();
        var input = new InsertTextualSysMLv2Input(UUID.randomUUID(), SysMLv2Identifiers.SIMPLE_PROJECT_EDITING_CONTEXT_ID, SIMPLE_PROJECT_PACKAGE1, """
                package importedPackage;
                """);
        var result = this.queryRunner.run(input);
        String typename = JsonPath.read(result, "$.data.insertTextualSysMLv2.__typename");

        assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());

        BiFunction<IEditingContext, IInput, IPayload> function = (editingContext, executeEditingContextFunctionInput) -> {
            return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), this.objectSearchService.getObject(editingContext, SIMPLE_PROJECT_PACKAGE1).get());
        };
        var mono = this.executeEditingContextFunctionRunner.execute(new ExecuteEditingContextFunctionInput(UUID.randomUUID(), SysMLv2Identifiers.SIMPLE_PROJECT_EDITING_CONTEXT_ID, function));

        Predicate<IPayload> predicate = payload -> Optional.of(payload)
                .filter(ExecuteEditingContextFunctionSuccessPayload.class::isInstance)
                .map(ExecuteEditingContextFunctionSuccessPayload.class::cast)
                .map(ExecuteEditingContextFunctionSuccessPayload::result)
                .filter(org.eclipse.syson.sysml.Package.class::isInstance)
                .map(org.eclipse.syson.sysml.Package.class::cast)
                .map(p -> p.getOwnedElement().stream().anyMatch(e -> e instanceof org.eclipse.syson.sysml.Package pack && "importedPackage".equals(pack.getName())))
                .orElse(false);

        StepVerifier.create(mono)
                .expectNextMatches(predicate)
                .thenCancel()
                .verify();
    }
}
