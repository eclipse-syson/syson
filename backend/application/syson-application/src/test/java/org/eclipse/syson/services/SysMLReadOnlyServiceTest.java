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
package org.eclipse.syson.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IEditingContextSearchService;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.web.tests.services.api.IGivenCommittedTransaction;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.SimpleProjectElementsTestProjectData;
import org.eclipse.syson.services.api.ISysMLReadOnlyService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test for {@link ISysMLReadOnlyService}.
 *
 * @author Arthur Daussy
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SysMLReadOnlyServiceTest extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenCommittedTransaction givenCommittedTransaction;

    @Autowired
    private ISysMLReadOnlyService readOnlyService;

    @Autowired
    private IEditingContextSearchService editingContextSearchService;

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("Given a simple SysML project, when we ask if an element is read only, then elements stored in standard libraries should be read-only whereas elements stored in other resources should be considered as editable")
    @Sql(scripts = { SimpleProjectElementsTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void checkEditableElements() {

        this.givenCommittedTransaction.commit();

        Optional<IEditingContext> ed = this.editingContextSearchService.findById(SimpleProjectElementsTestProjectData.EDITING_CONTEXT_ID);

        assertThat(ed).isPresent();

        URI mainResourceURI = new JSONResourceFactory().createResourceURI(SimpleProjectElementsTestProjectData.DOCUMENT_ID);

        for (Resource r : ((IEMFEditingContext) ed.get()).getDomain().getResourceSet().getResources()) {
            boolean expectedReadOnly = !mainResourceURI.equals(r.getURI());
            final String message = this.buildErrorMessage(expectedReadOnly);
            List<Element> elements = EMFUtils.allContainedObjectOfType(r, Element.class).toList();
            for (Element element : elements) {
                assertThat(this.readOnlyService.isReadOnly(element)).as(message, element.getElementId(), r.getURI()).isEqualTo(expectedReadOnly);
            }
        }
    }

    private String buildErrorMessage(boolean expectedReadOnly) {
        final String message;
        if (expectedReadOnly) {
            message = "Expecting %s in %s to %s to be read only";
        } else {
            message = "Expecting %s in %s to %s to be editable";
        }
        return message;
    }

}
