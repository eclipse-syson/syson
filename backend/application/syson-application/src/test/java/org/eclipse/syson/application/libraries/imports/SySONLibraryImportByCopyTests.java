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
package org.eclipse.syson.application.libraries.imports;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.components.core.api.IEditingContextSearchService;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.web.application.library.dto.ImportLibrariesInput;
import org.eclipse.sirius.web.application.library.services.LibraryMetadataAdapter;
import org.eclipse.sirius.web.domain.boundedcontexts.library.Library;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.SemanticData;
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

/**
 * {@link SySONLibraryImportTests} implementation for library import-by-copy.
 *
 * @author flatombe
 */
@Transactional(propagation = Propagation.NEVER)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(OutputCaptureExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class SySONLibraryImportByCopyTests extends SySONLibraryImportTests {

    @Autowired
    private IEditingContextSearchService editingContextSearchService;

    @Override
    @BeforeEach
    public void initializeServerState() {
        super.initializeServerState();

        final ImportLibrariesInput importLibrariesInput = new ImportLibrariesInput(
                UUID.randomUUID(),
                super.projectEditingContext.getId(),
                "copy",
                List.of(this.loadMyLibraryV1().getId().toString()));
        this.projectEditingContextEventProcessor.handle(importLibrariesInput);
    }

    @Test
    @DisplayName("The metadata of the imported library have not changed")
    @SysONLibraryImportTestServer
    public void testLibraryMetadataHaveNotChanged(CapturedOutput capturedOutput) {
        final Library myLibraryV1 = this.loadMyLibraryV1();
        assertThat(myLibraryV1.getLastModifiedOn()).isEqualTo(this.myLibraryV1LastModifiedInstantBefore);
    }

    @Test
    @DisplayName("The semantic data of the imported Library have not changed")
    @SysONLibraryImportTestServer
    public void testLibrarySemanticDataHaveNotChanged(CapturedOutput capturedOutput) {
        final SemanticData myLibraryV1SemanticData = this.loadMyLibraryV1SemanticData();
        assertThat(myLibraryV1SemanticData.getLastModifiedOn()).isEqualTo(this.myLibraryV1SemanticDataLastModifiedInstantBefore);
    }

    @Test
    @DisplayName("The dependencies of the project importing the library have not changed")
    @SysONLibraryImportTestServer
    public void testImportingProjectDependenciesHaveNotChanged(CapturedOutput capturedOutput) {
        final SemanticData projectSemanticData = this.loadProjectSemanticData();
        assertThat(projectSemanticData.getDependencies()).hasSize(0);
    }

    @Test
    @DisplayName("The project importing the libraries now has a copy of the library documents")
    @SysONLibraryImportTestServer
    public void testImportingProjectSemanticDataHaveChanged(CapturedOutput capturedOutput) {
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

        // With an import-by-copy, the LibraryMetadataAdapter is not supposed to be set up.
        projectResourcesCopiedFromLibrary.forEach(resource -> assertThat(resource.eAdapters().stream()
                .noneMatch(LibraryMetadataAdapter.class::isInstance)));

        for (int i = 0; i < projectResourcesCopiedFromLibrary.size(); i++) {
            final Resource projectResourceCopiedFromLibrary = projectResourcesCopiedFromLibrary.get(i);
            final Resource libraryCopiedResource = libraryCopiedResources.get(i);

            // Even across copies, the resource name should be the same.
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
}
