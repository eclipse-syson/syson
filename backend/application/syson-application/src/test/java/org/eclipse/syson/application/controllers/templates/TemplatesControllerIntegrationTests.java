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
package org.eclipse.syson.application.controllers.templates;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jayway.jsonpath.JsonPath;

import java.util.UUID;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.components.core.api.IEditingContextSearchService;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.web.application.project.dto.CreateProjectFromTemplateInput;
import org.eclipse.sirius.web.application.project.dto.CreateProjectFromTemplateSuccessPayload;
import org.eclipse.sirius.web.domain.boundedcontexts.projectsemanticdata.ProjectSemanticData;
import org.eclipse.sirius.web.domain.boundedcontexts.projectsemanticdata.services.api.IProjectSemanticDataSearchService;
import org.eclipse.sirius.web.tests.graphql.CreateProjectFromTemplateMutationRunner;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.SysONTestsProperties;
import org.eclipse.syson.application.sysmlv2.SysMLv2ProjectTemplatesProvider;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Namespace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests of the project templates controllers.
 *
 * @author arichard
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = { SysONTestsProperties.NO_DEFAULT_LIBRARIES_PROPERTY })
public class TemplatesControllerIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private CreateProjectFromTemplateMutationRunner createProjectFromTemplateMutationRunner;

    @Autowired
    private IEditingContextSearchService editingContextSearchService;

    @Autowired
    private IProjectSemanticDataSearchService projectSemanticDataSearchService;

    @BeforeEach
    public void beforeEach() {
        this.givenInitialServerState.initialize();
    }

    @DisplayName("Given the Batmobile project template, when the mutation is performed, then the project is created")
    @Test
    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenBatmobileProjectTemplateWhenMutationIsPerformedThenTheProjectIsCreated() {
        TestTransaction.flagForCommit();
        TestTransaction.end();

        var input = new CreateProjectFromTemplateInput(UUID.randomUUID(), SysMLv2ProjectTemplatesProvider.BATMOBILE_TEMPLATE_ID);
        var result = this.createProjectFromTemplateMutationRunner.run(input);

        String typename = JsonPath.read(result, "$.data.createProjectFromTemplate.__typename");
        assertThat(typename).isEqualTo(CreateProjectFromTemplateSuccessPayload.class.getSimpleName());

        String projectId = JsonPath.read(result, "$.data.createProjectFromTemplate.project.id");
        assertThat(projectId).isNotBlank();

        var optionalEditingContext = this.projectSemanticDataSearchService.findByProjectId(AggregateReference.to(projectId))
                .map(ProjectSemanticData::getSemanticData)
                .map(AggregateReference::getId)
                .map(UUID::toString)
                .flatMap(this.editingContextSearchService::findById);

        assertThat(optionalEditingContext).isPresent();

        var editingContext = optionalEditingContext.get();
        assertThat(editingContext).isInstanceOf(IEMFEditingContext.class);

        var emfEditingContext = (IEMFEditingContext) editingContext;
        var rootObject = this.getRooObject(emfEditingContext);
        assertNotNull(rootObject);
        assertEquals("Batmobile", rootObject.getDeclaredName());
    }

    @DisplayName("Given the SysMLv2 project template, when the mutation is performed, then the project is created")
    @Test
    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenSysMLv2ProjectTemplateWhenMutationIsPerformedThenTheProjectIsCreated() {
        TestTransaction.flagForCommit();
        TestTransaction.end();

        var input = new CreateProjectFromTemplateInput(UUID.randomUUID(), SysMLv2ProjectTemplatesProvider.SYSMLV2_TEMPLATE_ID);
        var result = this.createProjectFromTemplateMutationRunner.run(input);

        String typename = JsonPath.read(result, "$.data.createProjectFromTemplate.__typename");
        assertThat(typename).isEqualTo(CreateProjectFromTemplateSuccessPayload.class.getSimpleName());

        String projectId = JsonPath.read(result, "$.data.createProjectFromTemplate.project.id");
        assertThat(projectId).isNotBlank();

        var optionalEditingContext = this.projectSemanticDataSearchService.findByProjectId(AggregateReference.to(projectId))
                .map(ProjectSemanticData::getSemanticData)
                .map(AggregateReference::getId)
                .map(UUID::toString)
                .flatMap(this.editingContextSearchService::findById);

        assertThat(optionalEditingContext).isPresent();

        var editingContext = optionalEditingContext.get();
        assertThat(editingContext).isInstanceOf(IEMFEditingContext.class);

        var emfEditingContext = (IEMFEditingContext) editingContext;
        var rootObject = this.getRooObject(emfEditingContext);
        assertNotNull(rootObject);
        assertEquals("Package 1", rootObject.getDeclaredName());
    }

    @DisplayName("Given the SysMLv2-Library project template, when the mutation is performed, then the project is created")
    @Test
    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenSysMLv2LibraryProjectTemplateWhenMutationIsPerformedThenTheProjectIsCreated() {
        TestTransaction.flagForCommit();
        TestTransaction.end();

        var input = new CreateProjectFromTemplateInput(UUID.randomUUID(), SysMLv2ProjectTemplatesProvider.SYSMLV2_LIBRARY_TEMPLATE_ID);
        var result = this.createProjectFromTemplateMutationRunner.run(input);

        String typename = JsonPath.read(result, "$.data.createProjectFromTemplate.__typename");
        assertThat(typename).isEqualTo(CreateProjectFromTemplateSuccessPayload.class.getSimpleName());

        String projectId = JsonPath.read(result, "$.data.createProjectFromTemplate.project.id");
        assertThat(projectId).isNotBlank();

        var optionalEditingContext = this.projectSemanticDataSearchService.findByProjectId(AggregateReference.to(projectId))
                .map(ProjectSemanticData::getSemanticData)
                .map(AggregateReference::getId)
                .map(UUID::toString)
                .flatMap(this.editingContextSearchService::findById);

        assertThat(optionalEditingContext).isPresent();

        var editingContext = optionalEditingContext.get();
        assertThat(editingContext).isInstanceOf(IEMFEditingContext.class);

        var emfEditingContext = (IEMFEditingContext) editingContext;
        var rootObject = this.getRooObject(emfEditingContext);
        assertNotNull(rootObject);
        assertEquals("LibraryPackage1", rootObject.getDeclaredName());
    }

    private Element getRooObject(IEMFEditingContext emfEditingContext) {
        for (Resource resource : emfEditingContext.getDomain().getResourceSet().getResources()) {
            var contents = resource.getContents();
            for (EObject eObject : contents) {
                if (eObject instanceof Namespace implicitRootNamespace) {
                    return implicitRootNamespace.getOwnedMember().get(0);
                }
            }
        }
        return null;
    }
}
