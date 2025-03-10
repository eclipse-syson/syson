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

import java.time.Instant;
import java.util.List;
import java.util.Optional;
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
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.web.application.library.dto.PublishLibrariesInput;
import org.eclipse.sirius.web.domain.boundedcontexts.library.Library;
import org.eclipse.sirius.web.domain.boundedcontexts.library.services.api.ILibrarySearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.project.Project;
import org.eclipse.sirius.web.domain.boundedcontexts.project.services.api.IProjectSearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.SemanticData;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.services.api.ISemanticDataSearchService;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.SysMLv2Identifiers;
import org.eclipse.syson.sysml.SysmlPackage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the publication of the SysML contents of a project as a library.
 *
 * @see SySONLibraryPublicationHandler
 * @see SysONLibraryPublicationListener
 * @author flatombe
 */
@Transactional(propagation = Propagation.NEVER)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(OutputCaptureExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class SySONLibraryPublicationTests extends AbstractIntegrationTests {

    private static final String PUBLISHED_PROJECT_ID = SysMLv2Identifiers.SIMPLE_PROJECT;

    private static final String PUBLISHED_PROJECT_EDITING_CONTEXT_ID = SysMLv2Identifiers.SIMPLE_PROJECT_EDITING_CONTEXT_ID;

    private static final String PUBLICATION_KIND = "Project_SysML_AllProperContents";

    private static final String LIBRARY_VERSION = "v1";

    private static final String LIBRARY_DESCRIPTION = "A description.";

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private SySONLibraryPublicationHandler sySONLibraryPublicationHandler;

    @Autowired
    private IProjectSearchService projectSearchService;

    @Autowired
    private ISemanticDataSearchService semanticDataSearchService;

    @Autowired
    private IEditingContextSearchService editingContextSearchService;

    @Autowired
    private ILibrarySearchService librarySearchService;

    @BeforeEach
    public void initializeServerState() {
        this.givenInitialServerState.initialize();

        final Project projectBeforePublication = this.projectSearchService.findById(PUBLISHED_PROJECT_ID).orElseThrow();

        // Publish a project as a library and do some sanity checks.
        final PublishLibrariesInput input = new PublishLibrariesInput(UUID.randomUUID(), PUBLISHED_PROJECT_ID, PUBLICATION_KIND, LIBRARY_VERSION, LIBRARY_DESCRIPTION);
        assertThat(this.sySONLibraryPublicationHandler.canHandle(input));
        final IPayload publicationResult = this.sySONLibraryPublicationHandler.handle(input);
        assertThat(publicationResult).isInstanceOf(SuccessPayload.class);

        final Project projectAfterPublication = this.projectSearchService.findById(PUBLISHED_PROJECT_ID).orElseThrow();
        assertThat(projectBeforePublication.getLastModifiedOn()).isEqualTo(projectAfterPublication.getLastModifiedOn());
    }

    @Test
    @DisplayName("The published library exists and its metadatas are correct")
    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void testPublishedLibraryExistsAndMetadatasAreCorrect(CapturedOutput capturedOutput) {
        final Project project = this.projectSearchService.findById(PUBLISHED_PROJECT_ID).orElseThrow();
        assertThat(this.librarySearchService.findByNamespaceAndNameAndVersion(PUBLISHED_PROJECT_ID, project.getName(), LIBRARY_VERSION))
                .isPresent()
                .hasValueSatisfying(library -> library.getName().equals(project.getName()))
                .hasValueSatisfying(library -> library.getVersion().equals(LIBRARY_VERSION))
                .hasValueSatisfying(library -> library.getDescription().equals(LIBRARY_DESCRIPTION));
    }

    @Test
    @DisplayName("It is not possible to overwrite an existing library")
    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void testCannotOverwriteExistingLibrary(CapturedOutput capturedOutput) {
        final Project project = this.projectSearchService.findById(PUBLISHED_PROJECT_ID).orElseThrow();
        final Optional<Library> maybeLibrary = this.librarySearchService.findByNamespaceAndNameAndVersion(PUBLISHED_PROJECT_ID, project.getName(), LIBRARY_VERSION);
        assertThat(maybeLibrary).isPresent();

        final Instant libraryLastModifiedBeforeOurTry = maybeLibrary.get().getLastModifiedOn();

        // The uniqueness of a library is ensured based on its namespace (project ID), name and version so we may tweak
        // the description.
        final PublishLibrariesInput input = new PublishLibrariesInput(UUID.randomUUID(), PUBLISHED_PROJECT_ID, PUBLICATION_KIND, maybeLibrary.get().getVersion(),
                maybeLibrary.get().getDescription() + "_2");
        final IPayload publicationResult = this.sySONLibraryPublicationHandler.handle(input);
        assertThat(publicationResult).isInstanceOf(ErrorPayload.class);

        final Instant libraryLastModifiedAfterOurTry = this.librarySearchService.findByNamespaceAndNameAndVersion(PUBLISHED_PROJECT_ID, project.getName(), LIBRARY_VERSION).get().getLastModifiedOn();

        assertThat(libraryLastModifiedBeforeOurTry).isEqualTo(libraryLastModifiedAfterOurTry);
    }

    @Test
    @DisplayName("The contents of the library are similar to the SysML contents of the project that was published")
    @Sql(scripts = { "/scripts/syson-test-database.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = { "/scripts/cleanup.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void testLibraryContents(CapturedOutput capturedOutput) {
        final Project project = this.projectSearchService.findById(PUBLISHED_PROJECT_ID).orElseThrow();
        final Optional<Library> maybeLibrary = this.librarySearchService.findByNamespaceAndNameAndVersion(PUBLISHED_PROJECT_ID, project.getName(), LIBRARY_VERSION);
        assertThat(maybeLibrary).isPresent();

        final Optional<SemanticData> maybeLibrarySemanticData = this.semanticDataSearchService.findById(maybeLibrary.get().getSemanticData().getId());
        assertThat(maybeLibrarySemanticData).isPresent();

        final Optional<IEditingContext> maybeLibraryEditingContext = this.editingContextSearchService.findById(maybeLibrarySemanticData.get().getId().toString());
        assertThat(maybeLibraryEditingContext).isPresent().get().isInstanceOf(IEMFEditingContext.class);

        final ResourceSet libraryResourceSet = ((IEMFEditingContext) maybeLibraryEditingContext.get()).getDomain().getResourceSet();

        final Optional<IEditingContext> maybeSimpleProjectEditingContext = this.editingContextSearchService.findById(PUBLISHED_PROJECT_EDITING_CONTEXT_ID);
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
                        // return Integer.compare(Iterators.size(left.eAllContents()),
                        // Iterators.size(right.eAllContents()));
                        if (EcoreUtil.equals(left, right)) {
                            return 0;
                        } else {
                            return -1;
                        }
                    })
                    .hasSameElementsAs(functionResourceToSysMLRootContents.apply(simpleProjectSysMLResource));
        }
    }

}
