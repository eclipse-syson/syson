/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
package org.eclipse.syson.application.libraries.imports;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.components.collaborative.validation.dto.ValidationEventInput;
import org.eclipse.sirius.components.collaborative.validation.dto.ValidationRefreshedEventPayload;
import org.eclipse.sirius.components.core.api.IEditingContextSearchService;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.web.application.library.dto.ImportLibrariesInput;
import org.eclipse.sirius.web.application.library.services.LibraryMetadataAdapter;
import org.eclipse.sirius.web.domain.boundedcontexts.library.Library;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.SemanticData;
import org.eclipse.sirius.web.tests.graphql.ValidationEventSubscriptionRunner;
import org.eclipse.syson.application.libraries.SysONLibraryImportTestServer;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import reactor.test.StepVerifier;

/**
 * {@link SysONLibraryImportTests} implementation for library import-by-reference.
 *
 * @author flatombe
 */
@Transactional(propagation = Propagation.NEVER)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(OutputCaptureExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class SysONLibraryImportByReferenceTests extends SysONLibraryImportTests {

    @Autowired
    private IEditingContextSearchService editingContextSearchService;

    @Autowired
    private ValidationEventSubscriptionRunner validationEventSubscriptionRunner;

    @Override
    @BeforeEach
    public void initializeServerState() {
        super.initializeServerState();
    }

    @Test
    @DisplayName("The metadata of the imported library have not changed")
    @SysONLibraryImportTestServer
    public void testLibraryMetadataHaveNotChanged(CapturedOutput capturedOutput) {
        this.importLibraryV1();
        final Library myLibraryV1 = this.loadMyLibraryV1();
        assertThat(myLibraryV1.getLastModifiedOn()).isEqualTo(this.myLibraryV1LastModifiedInstantBefore);
    }

    @Test
    @DisplayName("The semantic data of the imported Library have not changed")
    @SysONLibraryImportTestServer
    public void testLibrarySemanticDataHaveNotChanged(CapturedOutput capturedOutput) {
        this.importLibraryV1();
        final SemanticData myLibraryV1SemanticData = this.loadMyLibraryV1SemanticData();
        assertThat(myLibraryV1SemanticData.getLastModifiedOn()).isEqualTo(this.myLibraryV1SemanticDataLastModifiedInstantBefore);
    }

    @Test
    @DisplayName("The dependencies of the project importing the library have changed")
    @SysONLibraryImportTestServer
    public void testImportingProjectDependenciesHaveChanged(CapturedOutput capturedOutput) {
        this.importLibraryV1();
        final SemanticData projectSemanticData = this.loadProjectSemanticData();
        assertThat(projectSemanticData.getDependencies()).hasSize(1);
        assertThat(projectSemanticData.getDependencies().get(0).dependencySemanticDataId().getId().toString()).isEqualTo(this.loadMyLibraryV1SemanticData().getId().toString());
    }

    @Test
    @DisplayName("The project importing the libraries now has a local copy of the library documents")
    @SysONLibraryImportTestServer
    public void testImportingProjectSemanticDataHaveChanged(CapturedOutput capturedOutput) {
        this.importLibraryV1();
        final ResourceSet projectResourceSet = ((IEMFEditingContext) this.projectEditingContext).getDomain().getResourceSet();
        final SemanticData myLibraryV1SemanticData = this.loadMyLibraryV1SemanticData();
        final List<Resource> libraryResources = ((IEMFEditingContext) this.editingContextSearchService.findById(myLibraryV1SemanticData.getId().toString())
                .orElseThrow())
                        .getDomain()
                        .getResourceSet()
                        .getResources();
        // The importing project already had a resource before the import.
        assertThat(projectResourceSet.getResources()).hasSize(libraryResources.size() + 1);

        // Now we want to compare the new contents in our project with the contents of the library (disregarding the
        // default libraries).
        final List<Resource> projectResourcesCopiedFromLibrary = projectResourceSet.getResources().stream()
                .skip(projectResourceSet.getResources().size() - myLibraryV1SemanticData.getDocuments().size())
                .toList();
        final List<Resource> libraryCopiedResources = libraryResources.stream()
                .skip(libraryResources.size() - myLibraryV1SemanticData.getDocuments().size())
                .toList();

        final Library myLibraryV1 = this.loadMyLibraryV1();
        // With an import-by-reference, the LibraryMetadataAdapter is supposed to be set up on the local Resources.
        projectResourcesCopiedFromLibrary.forEach(resource -> {
            final List<LibraryMetadataAdapter> libraryMetadataAdapters = resource.eAdapters().stream()
                    .filter(LibraryMetadataAdapter.class::isInstance).map(LibraryMetadataAdapter.class::cast).toList();
            assertThat(libraryMetadataAdapters).hasSize(1);
            final LibraryMetadataAdapter libraryMetadataAdapter = libraryMetadataAdapters.get(0);
            assertThat(libraryMetadataAdapter.getName()).isEqualTo(myLibraryV1.getName());
            assertThat(libraryMetadataAdapter.getNamespace()).isEqualTo(myLibraryV1.getNamespace());
            assertThat(libraryMetadataAdapter.getVersion()).isEqualTo(myLibraryV1.getVersion());
        });

        for (int i = 0; i < projectResourcesCopiedFromLibrary.size(); i++) {
            final Resource projectResourceCopiedFromLibrary = projectResourcesCopiedFromLibrary.get(i);
            final Resource libraryCopiedResource = libraryCopiedResources.get(i);

            // Even across local copies, the resource name should be the same.
            assertThat(this.getResourceName(projectResourceCopiedFromLibrary))
                    .isEqualTo(this.getResourceName(libraryCopiedResource));

            // Only comparing root elements should be enough.
            assertThat(projectResourceCopiedFromLibrary.getContents())
                    .usingElementComparator((left, right) -> {
                        if (EcoreUtil.equals(left, right)) {
                            return 0;
                        } else {
                            return -1;
                        }
                    })
                    .hasSameElementsAs(libraryCopiedResource.getContents());
        }
    }

    @Test
    @DisplayName("The validation view of the project displays the same amount of validation rules after the import")
    @SysONLibraryImportTestServer
    public void testValidationAfterImport() {
        ValidationEventInput validationEventInput = new ValidationEventInput(UUID.randomUUID(), this.projectEditingContext.getId(), "validation://");
        var validationFlux = this.validationEventSubscriptionRunner.run(validationEventInput).flux();

        AtomicReference<Integer> diagnosticCount = new AtomicReference<>(0);

        Consumer<Object> validationContentConsumer = payload -> Optional.of(payload)
                .filter(ValidationRefreshedEventPayload.class::isInstance)
                .map(ValidationRefreshedEventPayload.class::cast)
                .map(ValidationRefreshedEventPayload::validation)
                .ifPresentOrElse(validation -> {
                    assertThat(validation).isNotNull();
                    diagnosticCount.set(validation.getDiagnostics().size());
                }, () -> fail("Missing validation"));

        Runnable importLibrary = () -> {
            this.importLibraryV1();
        };

        Consumer<Object> validationUpdatedContentConsumer = payload -> Optional.of(payload)
                .filter(ValidationRefreshedEventPayload.class::isInstance)
                .map(ValidationRefreshedEventPayload.class::cast)
                .map(ValidationRefreshedEventPayload::validation)
                .ifPresentOrElse(validation -> {
                    assertThat(validation).isNotNull();
                    assertThat(validation.getDiagnostics().size()).isEqualTo(diagnosticCount.get());
                }, () -> fail("Missing validation"));

        StepVerifier.create(validationFlux)
                .consumeNextWith(validationContentConsumer)
                .then(importLibrary)
                .consumeNextWith(validationUpdatedContentConsumer)
                .thenCancel()
                .verify(Duration.ofSeconds(10));

    }

    private void importLibraryV1() {
        final ImportLibrariesInput importLibrariesInput = new ImportLibrariesInput(
                UUID.randomUUID(),
                super.projectEditingContext.getId(),
                "import",
                List.of(this.loadMyLibraryV1().getId().toString()));
        this.projectEditingContextEventProcessor.handle(importLibrariesInput);
    }
}
