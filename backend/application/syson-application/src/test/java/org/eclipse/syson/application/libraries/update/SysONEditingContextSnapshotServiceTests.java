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
package org.eclipse.syson.application.libraries.update;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IEditingContextSearchService;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.web.application.editingcontext.services.api.IEditingContextSnapshot;
import org.eclipse.sirius.web.application.editingcontext.services.api.IEditingContextSnapshotService;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.SimpleProjectElementsTestProjectData;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests of the editing context snapshot service.
 *
 * @author gdaniel
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SysONEditingContextSnapshotServiceTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IEditingContextSnapshotService editingContextSnapshotService;

    @Autowired
    private IEditingContextSearchService editingContextSearchService;

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @Test
    @DisplayName("GIVEN an editing context, WHEN a snapshot is created and restored, THEN the resources in the editing context are unchanged")
    @Sql(scripts = { SimpleProjectElementsTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenEditingContextWhenSnapshotIsCreatedAndRestoredThenTheResourcesInTheEditingContextAreUnchanged() {
        Optional<IEditingContext> optionalEditingContext = this.editingContextSearchService.findById(SimpleProjectElementsTestProjectData.EDITING_CONTEXT_ID);
        assertThat(optionalEditingContext).isPresent()
                .get()
                .isInstanceOf(IEMFEditingContext.class);
        IEMFEditingContext siriusWebEditingContext = (IEMFEditingContext) optionalEditingContext.get();
        List<Resource> resources = siriusWebEditingContext.getDomain().getResourceSet().getResources();
        int initialNumberOfResources = resources.size();
        long initialNumberOfStandardLibraryResources = resources.stream().filter(ElementUtil::isStandardLibraryResource).count();

        Optional<IEditingContextSnapshot> optionalSnapshot = this.editingContextSnapshotService.createSnapshot(siriusWebEditingContext);
        assertThat(optionalSnapshot).isPresent();
        this.editingContextSnapshotService.restoreSnapshot(siriusWebEditingContext, optionalSnapshot.get());

        resources = siriusWebEditingContext.getDomain().getResourceSet().getResources();
        assertThat(resources.size()).isEqualTo(initialNumberOfResources);
        long updatedNumberOfStandardLibraryResources = resources.stream().filter(ElementUtil::isStandardLibraryResource).count();
        assertThat(updatedNumberOfStandardLibraryResources).isEqualTo(initialNumberOfStandardLibraryResources);
    }

}
