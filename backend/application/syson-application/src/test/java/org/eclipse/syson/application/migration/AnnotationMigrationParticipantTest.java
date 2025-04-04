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
import org.eclipse.sirius.web.tests.services.api.IGivenCommittedTransaction;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.sysml.AnnotatingElement;
import org.eclipse.syson.sysml.Annotation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

/**
 * Tests for all migration participant related to Annotation elements.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AnnotationMigrationParticipantTest extends AbstractIntegrationTests {

    private static final String ANNOTATION_ID = "13567c66-16d5-4a86-92d5-191af9d2a84a";

    private static final String ANNOTATING_ELEMENT_ID = "150bf3b9-bd26-404d-98ae-f2487b3104f3";

    private static final String EDITING_CONTEXT_ID = "45343945-1cb8-456b-b396-b02df4ca6da1";

    @Autowired
    private IGivenCommittedTransaction givenCommittedTransaction;

    @Autowired
    private IEditingContextSearchService editingContextSearchService;

    @Autowired
    private IObjectService objectService;

    @Test
    @DisplayName("Given a project with an old SysML model, when the model is loaded, AnnotationAnnotatingElementMigrationParticipant migrates the model correctly meaning it does not deserialize source feature")
    @Sql(scripts = { "/scripts/database-content/syson-test-database-annotation.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void annotationSourceMigrationParticpantTest() {
        this.givenCommittedTransaction.commit();
        var optionalEditingContext = this.editingContextSearchService.findById(EDITING_CONTEXT_ID.toString());
        assertThat(optionalEditingContext).isPresent();
        this.testIsMigrationSuccessful(optionalEditingContext.get());
    }

    private void testIsMigrationSuccessful(IEditingContext editingContext) {
        Annotation annotation = (Annotation) this.objectService.getObject(editingContext, ANNOTATION_ID).get();
        AnnotatingElement annotatingElement = (AnnotatingElement) this.objectService.getObject(editingContext, ANNOTATING_ELEMENT_ID).get();

        AnnotatingElement annotationAnnotatingElement = annotation.getAnnotatingElement();
        EList<Annotation> annotatingElementAnnotation = annotatingElement.getAnnotation();

        assertThat(annotatingElementAnnotation).hasSize(1);
        assertThat(annotatingElementAnnotation).contains(annotation);

        assertThat(annotationAnnotatingElement).isEqualTo(annotatingElement);
    }
}
