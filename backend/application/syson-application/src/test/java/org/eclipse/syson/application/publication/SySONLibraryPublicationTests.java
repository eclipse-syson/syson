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
package org.eclipse.syson.application.publication;

import static org.assertj.core.api.Assertions.assertThat;

import com.jayway.jsonpath.JsonPath;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.components.core.api.ErrorPayload;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IEditingContextSearchService;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.web.application.library.dto.PublishLibrariesInput;
import org.eclipse.sirius.web.domain.boundedcontexts.library.Library;
import org.eclipse.sirius.web.domain.boundedcontexts.library.services.api.ILibrarySearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.Document;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.SemanticData;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.services.api.ISemanticDataSearchService;
import org.eclipse.sirius.web.tests.graphql.PublishLibrariesMutationRunner;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.SysMLv2Identifiers;
import org.eclipse.syson.sysml.SysmlPackage;
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
 * Integration tests for the publication of the SysML contents of a project as a library.
 *
 * @see SySONLibraryPublicationHandler
 * @see SysONLibraryPublicationListener
 * @author flatombe
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SySONLibraryPublicationTests extends AbstractIntegrationTests {

    private static final String PUBLICATION_KIND = "Project_SysML_AllProperContents";

    private static final String LIBRARY_VERSION = "v1";

    private static final String LIBRARY_DESCRIPTION = "A description.";

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private ISemanticDataSearchService semanticDataSearchService;

    @Autowired
    private IEditingContextSearchService editingContextSearchService;

    @Autowired
    private ILibrarySearchService librarySearchService;

    @Autowired
    private PublishLibrariesMutationRunner publishLibrariesMutationRunner;

    @BeforeEach
    public void initializeServerState() {
        this.givenInitialServerState.initialize();
    }

    @Test
    @DisplayName("Given a project, when the library is published, then the library exists and its metadatas are correct")
    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenProjectWhenLibraryIsPublishedThenLibraryExistsAndHasCorrectMetadatas() {
        var input = new PublishLibrariesInput(UUID.randomUUID(), SysMLv2Identifiers.SIMPLE_PROJECT, PUBLICATION_KIND, LIBRARY_VERSION, LIBRARY_DESCRIPTION);
        var result = this.publishLibrariesMutationRunner.run(input);
        String typename = JsonPath.read(result, "$.data.publishLibraries.__typename");
        assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        assertThat(this.librarySearchService.findByNamespaceAndNameAndVersion(SysMLv2Identifiers.SIMPLE_PROJECT, SysMLv2Identifiers.SIMPLE_PROJECT_NAME, LIBRARY_VERSION))
                .isPresent()
                .hasValueSatisfying(library -> assertThat(library.getName()).isEqualTo(SysMLv2Identifiers.SIMPLE_PROJECT_NAME))
                .hasValueSatisfying(library -> assertThat(library.getVersion()).isEqualTo(LIBRARY_VERSION))
                .hasValueSatisfying(library -> assertThat(library.getDescription()).isEqualTo(LIBRARY_DESCRIPTION));
    }

    @Test
    @DisplayName("Given a project, when the library is published twice with the same version, then the second publication fails and the library is not updated")
    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenProjectWhenLibraryIsPublishedTwiceThenTheSecondPublicationFailsAndTheLibraryIsNotUpdated() {
        var input1 = new PublishLibrariesInput(UUID.randomUUID(), SysMLv2Identifiers.SIMPLE_PROJECT, PUBLICATION_KIND, LIBRARY_VERSION, LIBRARY_DESCRIPTION);
        var result1 = this.publishLibrariesMutationRunner.run(input1);
        String typename1 = JsonPath.read(result1, "$.data.publishLibraries.__typename");
        assertThat(typename1).isEqualTo(SuccessPayload.class.getSimpleName());
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        Optional<Library> optionalLibrary = this.librarySearchService.findByNamespaceAndNameAndVersion(SysMLv2Identifiers.SIMPLE_PROJECT, SysMLv2Identifiers.SIMPLE_PROJECT_NAME, LIBRARY_VERSION);
        assertThat(optionalLibrary).isPresent();
        Instant initialLastModifiedOn = optionalLibrary.get().getLastModifiedOn();

        // The uniqueness of a library is ensured based on its namespace (project ID), name and version so we may tweak
        // the description.
        var input2 = new PublishLibrariesInput(UUID.randomUUID(), SysMLv2Identifiers.SIMPLE_PROJECT, PUBLICATION_KIND, LIBRARY_VERSION, LIBRARY_DESCRIPTION + "_2");
        var result2 = this.publishLibrariesMutationRunner.run(input2);
        String typename2 = JsonPath.read(result2, "$.data.publishLibraries.__typename");
        assertThat(typename2).isEqualTo(ErrorPayload.class.getSimpleName());
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        Optional<Library> optionalUpdatedLibrary = this.librarySearchService.findByNamespaceAndNameAndVersion(SysMLv2Identifiers.SIMPLE_PROJECT, SysMLv2Identifiers.SIMPLE_PROJECT_NAME,
                LIBRARY_VERSION);
        // Check that the published library has not been updated.
        assertThat(optionalUpdatedLibrary)
                .isPresent()
                .hasValueSatisfying(library -> assertThat(library.getLastModifiedOn()).isEqualTo(initialLastModifiedOn));
    }

    @Test
    @DisplayName("Given a project, when the library is published, then the content of the library matches the content of the project")
    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenProjectWhenLibraryIsPublishedThenContentOfLibraryMatchesContentOfProject() {
        var input = new PublishLibrariesInput(UUID.randomUUID(), SysMLv2Identifiers.SIMPLE_PROJECT, PUBLICATION_KIND, LIBRARY_VERSION, LIBRARY_DESCRIPTION);
        var result = this.publishLibrariesMutationRunner.run(input);
        String typename = JsonPath.read(result, "$.data.publishLibraries.__typename");
        assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        final Optional<IEditingContext> maybeLibraryEditingContext = this.librarySearchService
                .findByNamespaceAndNameAndVersion(SysMLv2Identifiers.SIMPLE_PROJECT, SysMLv2Identifiers.SIMPLE_PROJECT_NAME, LIBRARY_VERSION)
                .map(Library::getSemanticData)
                .map(AggregateReference::getId)
                .map(UUID::toString)
                .flatMap(this.editingContextSearchService::findById);
        assertThat(maybeLibraryEditingContext).isPresent().get().isInstanceOf(IEMFEditingContext.class);
        final ResourceSet libraryResourceSet = ((IEMFEditingContext) maybeLibraryEditingContext.get()).getDomain().getResourceSet();

        final Optional<IEditingContext> maybeSimpleProjectEditingContext = this.editingContextSearchService.findById(SysMLv2Identifiers.SIMPLE_PROJECT_EDITING_CONTEXT_ID);
        assertThat(maybeSimpleProjectEditingContext).isPresent().get().isInstanceOf(IEMFEditingContext.class);
        final ResourceSet simpleProjectResourceSet = ((IEMFEditingContext) maybeSimpleProjectEditingContext.get()).getDomain().getResourceSet();

        final Function<Resource, List<EObject>> functionResourceToSysMLRootContents = resource -> resource.getContents().stream()
                .filter(rootEObject -> rootEObject.eClass().getEPackage() == SysmlPackage.eINSTANCE)
                .toList();
        final Predicate<Resource> predicateIsSysMLResource = resource -> !functionResourceToSysMLRootContents.apply(resource)
                .isEmpty();
        final Function<Resource, String> functionResourceToResourceName = resource -> resource.eAdapters().stream()
                .filter(ResourceMetadataAdapter.class::isInstance)
                .map(ResourceMetadataAdapter.class::cast)
                .findFirst()
                .get()
                .getName();

        // We want to compare both contents but we cannot rely on IDs which have changed upon publication.
        // Let us just check for number of resources and their names, and the sizes of their SysML contents.

        final List<Resource> simpleProjectSysMLResources = simpleProjectResourceSet.getResources().stream()
                .filter(predicateIsSysMLResource)
                .toList();
        final List<Resource> librarySysMLResources = libraryResourceSet.getResources().stream()
                .filter(predicateIsSysMLResource)
                .toList();
        assertThat(librarySysMLResources.size()).isEqualTo(simpleProjectSysMLResources.size());

        for (int i = 0; i < librarySysMLResources.size(); i++) {
            final Resource librarySysMLResource = librarySysMLResources.get(i);
            final Resource simpleProjectSysMLResource = simpleProjectSysMLResources.get(i);

            assertThat(functionResourceToResourceName.apply(librarySysMLResource))
                    .isEqualTo(functionResourceToResourceName.apply(simpleProjectSysMLResource));

            assertThat(functionResourceToSysMLRootContents.apply(librarySysMLResource))
                    .usingElementComparator((left, right) -> {
                        if (EcoreUtil.equals(left, right)) {
                            return 0;
                        } else {
                            return -1;
                        }
                    })
                    .hasSameElementsAs(functionResourceToSysMLRootContents.apply(simpleProjectSysMLResource));
        }
    }

    @Test
    @DisplayName("Given a project with a resource flagged as imported, when the library is published, then the library does not contain the imported flag.")
    @Sql(scripts = { "/scripts/SysMLv2-ImportedProject/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void givenProjectWithImportedResourceWhenLibraryIsPublishedThenLibraryDoesNotContainTheImportedFlag() {
        var input = new PublishLibrariesInput(UUID.randomUUID(), SysMLv2Identifiers.IMPORTED_PROJECT, PUBLICATION_KIND, "1.0.0", "");
        var result = this.publishLibrariesMutationRunner.run(input);
        String typename = JsonPath.read(result, "$.data.publishLibraries.__typename");
        assertThat(typename).isEqualTo(SuccessPayload.class.getSimpleName());
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();

        Optional<SemanticData> semanticData = this.librarySearchService.findByNamespaceAndNameAndVersion(SysMLv2Identifiers.IMPORTED_PROJECT, SysMLv2Identifiers.IMPORTED_PROJECT_NAME, "1.0.0")
                    .map(Library::getSemanticData)
                    .map(AggregateReference::getId)
                    .flatMap(this.semanticDataSearchService::findById);

        assertThat(semanticData).isPresent();
        Set<Document> documents = semanticData.get().getDocuments();
        assertThat(documents)
                .hasSize(1)
                .allSatisfy(document -> assertThat(document.getContent()).doesNotContain("\"source\":\"org.eclipse.syson.sysml.imported\""));
    }

}
