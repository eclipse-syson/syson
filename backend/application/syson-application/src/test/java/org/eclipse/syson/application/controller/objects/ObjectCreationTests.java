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
package org.eclipse.syson.application.controller.objects;

import static org.assertj.core.api.Assertions.assertThat;

import com.jayway.jsonpath.JsonPath;

import java.util.Optional;
import java.util.UUID;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.components.collaborative.dto.CreateChildInput;
import org.eclipse.sirius.components.collaborative.dto.CreateChildSuccessPayload;
import org.eclipse.sirius.components.collaborative.dto.CreateRootObjectInput;
import org.eclipse.sirius.components.collaborative.dto.CreateRootObjectSuccessPayload;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionSuccessPayload;
import org.eclipse.sirius.web.application.UUIDParser;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.services.api.IRepresentationMetadataSearchService;
import org.eclipse.sirius.web.tests.graphql.CreateChildMutationRunner;
import org.eclipse.sirius.web.tests.graphql.CreateRootObjectMutationRunner;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.GeneralViewEmptyTestProjectData;
import org.eclipse.syson.application.services.SysMLv2EditService;
import org.eclipse.syson.services.SemanticRunnableFactory;
import org.eclipse.syson.services.api.ISysONResourceService;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.util.ElementUtil;
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
 * Tests the object creations.
 *
 * @author gdaniel
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ObjectCreationTests extends AbstractIntegrationTests {

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private CreateRootObjectMutationRunner createRootObjectMutationRunner;

    @Autowired
    private CreateChildMutationRunner createChildMutationRunner;

    @Autowired
    private SemanticRunnableFactory semanticRunnableFactory;

    @Autowired
    private ISysONResourceService sysONResourceService;

    @Autowired
    private IRepresentationMetadataSearchService representationMetadataSearchService;

    @BeforeEach
    public void setUp() {
        this.givenInitialServerState.initialize();
    }

    @Test
    @DisplayName("GIVEN an imported document, WHEN a root object is created, THEN it is created properly and the document is not imported anymore")
    @Sql(scripts = { GeneralViewEmptyTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenImportedDocumentWhenRootObjectIsCreatedThenItIsCreatedProperlyAndDocumentIsNotImported() {
        this.semanticRunnableFactory.createRunnable(GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                (editingContext, executeEditingContextFunctionInput) -> {
                    Optional<Resource> optResource = this.getResource(editingContext, GeneralViewEmptyTestProjectData.SemanticIds.MODEL_ID);
                    assertThat(optResource).isPresent();
                    Resource resource = optResource.get();
                    ElementUtil.setIsImported(resource, true);
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                }).run();
        TestTransaction.flagForCommit();
        TestTransaction.end();

        var input = new CreateRootObjectInput(
                UUID.randomUUID(),
                GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                UUID.fromString(GeneralViewEmptyTestProjectData.SemanticIds.MODEL_ID),
                SysmlPackage.eNS_URI,
                "SysMLv2EditService-Package");

        var result = this.createRootObjectMutationRunner.run(input);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        String typename = JsonPath.read(result.data(), "$.data.createRootObject.__typename");
        assertThat(typename).isEqualTo(CreateRootObjectSuccessPayload.class.getSimpleName());

        String objectId = JsonPath.read(result.data(), "$.data.createRootObject.object.id");
        assertThat(objectId).isNotBlank();

        String objectLabel = JsonPath.read(result.data(), "$.data.createRootObject.object.label");
        assertThat(objectLabel).isNotBlank();

        String objectKind = JsonPath.read(result.data(), "$.data.createRootObject.object.kind");
        assertThat(objectKind).isEqualTo("siriusComponents://semantic?domain=sysml&entity=Package");

        this.semanticRunnableFactory.createRunnable(GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                (editingContext, executeEditingContextFunctionInput) -> {
                    Optional<Resource> optResource = this.getResource(editingContext, GeneralViewEmptyTestProjectData.SemanticIds.MODEL_ID);
                    assertThat(optResource).isPresent();
                    Resource resource = optResource.get();
                    assertThat(this.sysONResourceService.isImported(editingContext, resource)).isFalse();
                    return new ExecuteEditingContextFunctionSuccessPayload(executeEditingContextFunctionInput.id(), true);
                }).run();
    }

    @DisplayName("GIVEN an empty SysML Project, WHEN a ViewUsage is created under the root Package, THEN a General View diagram is also created on this ViewUsage")
    @Sql(scripts = { GeneralViewEmptyTestProjectData.SCRIPT_PATH }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    @Test
    public void createGeneralViewDiagramAtTheSameTimeAsViewUsage() {
        Optional<UUID> optionalSemanticData = new UUIDParser().parse(GeneralViewEmptyTestProjectData.EDITING_CONTEXT);
        assertThat(optionalSemanticData).isPresent();
        var representationMetadatas = this.representationMetadataSearchService.findAllRepresentationMetadataBySemanticData(AggregateReference.to(optionalSemanticData.get()));
        assertThat(representationMetadatas).hasSize(1);
        assertThat(representationMetadatas.get(0)).extracting("label").isEqualTo("General View");

        var input = new CreateChildInput(
                UUID.randomUUID(),
                GeneralViewEmptyTestProjectData.EDITING_CONTEXT,
                GeneralViewEmptyTestProjectData.SemanticIds.PACKAGE_1_ID,
                SysMLv2EditService.ID_PREFIX + "ViewUsage");
        var result = this.createChildMutationRunner.run(input);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        String typename = JsonPath.read(result.data(), "$.data.createChild.__typename");
        assertThat(typename).isEqualTo(CreateChildSuccessPayload.class.getSimpleName());

        String objectId = JsonPath.read(result.data(), "$.data.createChild.object.id");
        assertThat(objectId).isNotBlank();

        String objectLabel = JsonPath.read(result.data(), "$.data.createChild.object.label");
        // a ViewUsage already exists in the GeneralViewEmpty project, so the new one is the second one
        assertThat(objectLabel).isEqualTo("view2");

        String objectKind = JsonPath.read(result.data(), "$.data.createChild.object.kind");
        assertThat(objectKind).isEqualTo("siriusComponents://semantic?domain=sysml&entity=ViewUsage");

        representationMetadatas = this.representationMetadataSearchService.findAllRepresentationMetadataBySemanticData(AggregateReference.to(optionalSemanticData.get()));
        assertThat(representationMetadatas).hasSize(2)
                .anySatisfy(mdt -> mdt.getLabel().equals("General View"))
                .anySatisfy(mdt -> mdt.getLabel().equals("view2"));

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
            URI uri = new JSONResourceFactory().createResourceURI(GeneralViewEmptyTestProjectData.SemanticIds.MODEL_ID);
            result = resourceSet.getResources().stream()
                    .filter(r -> r.getURI().equals(uri))
                    .findFirst();
        }
        return result;

    }
}
