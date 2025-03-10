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
package org.eclipse.syson.application.migration;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.emf.common.util.EList;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IEditingContextSearchService;
import org.eclipse.sirius.components.core.api.IObjectService;
import org.eclipse.sirius.web.application.editingcontext.EditingContext;
import org.eclipse.sirius.web.tests.services.api.IGivenCommittedTransaction;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SuccessionAsUsage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test for {@link SuccessionAsUsageMigrationParticipant}.
 *
 * @author Arthur Daussy
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SuccessionAsUsageMigrationParticipantTest extends AbstractIntegrationTests {

    private static final String SUCCESSION_AS_USAGE_ID = "acacc9f2-8764-49e6-b5ea-b96623988c7e";

    private static final String SUCCESSION_MIGRATION_PARTICIPANT_TEST_EDITING_CONTEXT_ID = "6c4f7d33-fb14-4d1d-89fa-019a5f19a8d0";

    @Autowired
    private IGivenCommittedTransaction givenCommittedTransaction;

    @Autowired
    private IEditingContextSearchService editingContextSearchService;

    @Autowired
    private IObjectService objectService;

    @Test
    @DisplayName("Given a project with an old SysML model, when the model is loaded, SuccessionAsUsageMigrationParticipant migrates the model correctly")
    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenAnOldModelMigrationParticipantCanBeContributedToUpdateTheModel() {
        this.givenCommittedTransaction.commit();
        var optionalEditingContext = this.editingContextSearchService.findById(SUCCESSION_MIGRATION_PARTICIPANT_TEST_EDITING_CONTEXT_ID.toString());
        assertThat(optionalEditingContext).isPresent();
        this.testIsMigrationSuccessful(optionalEditingContext.get());
    }

    private void testIsMigrationSuccessful(IEditingContext editingContext) {
        if (editingContext instanceof EditingContext) {

            SuccessionAsUsage successionAsUsage = (SuccessionAsUsage) this.objectService.getObject(editingContext, SUCCESSION_AS_USAGE_ID).get();

            // Check the source feature is the "start" UsageAction and not the membership
            EList<Element> sources = successionAsUsage.getSource();
            assertThat(sources).hasSize(1);
            assertThat(sources.get(0).getDeclaredName()).isEqualTo("start");
        }
    }
}
