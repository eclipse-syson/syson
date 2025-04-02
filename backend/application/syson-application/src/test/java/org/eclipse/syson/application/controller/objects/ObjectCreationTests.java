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
package org.eclipse.syson.application.controller.objects;

import static org.assertj.core.api.Assertions.assertThat;

import com.jayway.jsonpath.JsonPath;

import java.util.Optional;
import java.util.UUID;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.components.collaborative.dto.CreateRootObjectInput;
import org.eclipse.sirius.components.collaborative.dto.CreateRootObjectSuccessPayload;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.web.tests.graphql.CreateRootObjectMutationRunner;
import org.eclipse.sirius.web.tests.services.api.IGivenCommittedTransaction;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.SysONTestsProperties;
import org.eclipse.syson.application.data.SysMLv2Identifiers;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.api.ISysONResourceService;
import org.eclipse.syson.sysml.SysmlPackage;
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
 * Tests the object creations.
 *
 * @author gdaniel
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = { SysONTestsProperties.NO_DEFAULT_LIBRARIES_PROPERTY })
public class ObjectCreationTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IGivenCommittedTransaction givenCommittedTransaction;

    @Autowired
    private CreateRootObjectMutationRunner createRootObjectMutationRunner;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private ISysONResourceService sysONResourceService;

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    @Test
    @DisplayName("Given an imported document, when a root object is created, then it is created properly and the document is not imported anymore")
    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenImportedDocumentWhenRootObjectIsCreatedThenItIsCreatedProperlyAndDocumentIsNotImported() {
        this.givenCommittedTransaction.commit();

        this.semanticRunnableFactory.createRunnable(SysMLv2Identifiers.GENERAL_VIEW_EMPTY_EDITING_CONTEXT_ID,
                (editingContext, executeEditingContextFunctionInput) -> {
                    Optional<Resource> optResource = this.getResource(editingContext, SysMLv2Identifiers.GENERAL_VIEW_EMPTY_MODEL);
                    assertThat(optResource).isPresent();
                    Resource resource = optResource.get();
                    ElementUtil.setIsImported(resource, true);
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                }).run();

        var input = new CreateRootObjectInput(
                UUID.randomUUID(),
                SysMLv2Identifiers.GENERAL_VIEW_EMPTY_EDITING_CONTEXT_ID,
                UUID.fromString(SysMLv2Identifiers.GENERAL_VIEW_EMPTY_MODEL),
                SysmlPackage.eNS_URI,
                "SysMLv2EditService-Package");

        var result = this.createRootObjectMutationRunner.run(input);

        String typename = JsonPath.read(result, "$.data.createRootObject.__typename");
        assertThat(typename).isEqualTo(CreateRootObjectSuccessPayload.class.getSimpleName());

        String objectId = JsonPath.read(result, "$.data.createRootObject.object.id");
        assertThat(objectId).isNotBlank();

        String objectLabel = JsonPath.read(result, "$.data.createRootObject.object.label");
        assertThat(objectLabel).isNotBlank();

        String objectKind = JsonPath.read(result, "$.data.createRootObject.object.kind");
        assertThat(objectKind).isEqualTo("siriusComponents://semantic?domain=sysml&entity=Package");

        this.semanticRunnableFactory.createRunnable(SysMLv2Identifiers.GENERAL_VIEW_EMPTY_EDITING_CONTEXT_ID,
                (editingContext, executeEditingContextFunctionInput) -> {
                    Optional<Resource> optResource = this.getResource(editingContext, SysMLv2Identifiers.GENERAL_VIEW_EMPTY_MODEL);
                    assertThat(optResource).isPresent();
                    Resource resource = optResource.get();
                    assertThat(this.sysONResourceService.isImported(resource)).isFalse();
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                }).run();
    }

    private Optional<Resource> getResource(IEditingContext editingContext, String resourceId) {
        Optional<Resource> result = Optional.empty();
        var optionalEditingDomain = Optional.of(editingContext)
                .filter(IEMFEditingContext.class::isInstance)
                .map(IEMFEditingContext.class::cast)
                .map(IEMFEditingContext::getDomain);

        if (optionalEditingDomain.isPresent()) {
            var editingDomain = optionalEditingDomain.get();
            ResourceSet resourceSet = editingDomain.getResourceSet();
            URI uri = new JSONResourceFactory().createResourceURI(SysMLv2Identifiers.GENERAL_VIEW_EMPTY_MODEL);
            result = resourceSet.getResources().stream()
                    .filter(r -> r.getURI().equals(uri))
                    .findFirst();
        }
        return result;

    }
}
