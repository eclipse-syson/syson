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

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.stream.Streams;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.components.collaborative.api.IEditingContextEventProcessor;
import org.eclipse.sirius.components.collaborative.editingcontext.EditingContextEventProcessorFactory;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IEditingContextSearchService;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.web.application.library.dto.UpdateLibraryInput;
import org.eclipse.sirius.web.application.library.services.LibraryMetadataAdapter;
import org.eclipse.sirius.web.domain.boundedcontexts.library.Library;
import org.eclipse.sirius.web.domain.boundedcontexts.library.services.api.ILibrarySearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.project.Project;
import org.eclipse.sirius.web.domain.boundedcontexts.project.services.api.IProjectSearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.projectsemanticdata.ProjectSemanticData;
import org.eclipse.sirius.web.domain.boundedcontexts.projectsemanticdata.services.api.IProjectSemanticDataSearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.SemanticData;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.services.api.ISemanticDataSearchService;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.libraries.SysONLibraryImportTestServer;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link AbstractIntegrationTests} implementation for updating the version of a published library used as dependency.
 *
 * @author flatombe
 */
@Transactional(propagation = Propagation.NEVER)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(OutputCaptureExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class SysONLibraryUpdateTests extends AbstractIntegrationTests {

    protected IEditingContext projectEditingContext;

    protected IEditingContextEventProcessor projectEditingContextEventProcessor;

    protected Instant myLibraryV1LastModifiedOnBefore;

    protected Instant myLibraryV2LastModifiedOnBefore;

    protected Instant myLibraryV1SemanticDataLastModifiedOnBefore;

    protected Instant myLibraryV2SemanticDataLastModifiedOnBefore;

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IProjectSearchService projectSearchService;

    @Autowired
    private ISemanticDataSearchService semanticDataSearchService;

    @Autowired
    private IEditingContextSearchService editingContextSearchService;

    @Autowired
    private ILibrarySearchService librarySearchService;

    @Autowired
    private IProjectSemanticDataSearchService projectSemanticDataSearchService;

    @Autowired
    private EditingContextEventProcessorFactory factory;

    @BeforeEach
    public void initializeServerState() {
        this.givenInitialServerState.initialize();

        // Sanity checks about our libraries.
        final SemanticData myLibraryV1SemanticData = this.loadMyLibraryV1SemanticData();
        assertThat(myLibraryV1SemanticData.getDependencies()).isEmpty();
        assertThat(myLibraryV1SemanticData.getDocuments()).hasSize(3);
        final SemanticData myLibraryV2SemanticData = this.loadMyLibraryV2SemanticData();
        assertThat(myLibraryV2SemanticData.getDependencies()).isEmpty();
        assertThat(myLibraryV2SemanticData.getDocuments()).hasSize(2);

        // Sanity checks about our project that already references MyLibraryV1
        final SemanticData projectSemanticData = this.loadProjectSemanticData();
        assertThat(projectSemanticData.getDependencies()).hasSize(1);
        assertThat(projectSemanticData.getDependencies().get(0).dependencySemanticDataId().getId()).isEqualTo(myLibraryV1SemanticData.getId());
        this.projectEditingContext = this.editingContextSearchService.findById(projectSemanticData.getId().toString()).orElseThrow();
        final ResourceSet projectResourceSet = ((IEMFEditingContext) this.projectEditingContext).getDomain().getResourceSet();
        final List<Resource> resourcesFromLibraries = projectResourceSet.getResources().stream().filter(resource -> resource.eAdapters().stream().anyMatch(LibraryMetadataAdapter.class::isInstance))
                .toList();
        assertThat(projectSemanticData.getDocuments()).hasSize(1);
        assertThat(resourcesFromLibraries).hasSize(3);
        final Library myLibraryV1 = this.loadMyLibraryV1();
        resourcesFromLibraries.forEach(resource -> {
            final List<LibraryMetadataAdapter> libraryMetadataAdapters = resource.eAdapters().stream()
                    .filter(LibraryMetadataAdapter.class::isInstance).map(LibraryMetadataAdapter.class::cast).toList();
            assertThat(libraryMetadataAdapters).hasSize(1);
            final LibraryMetadataAdapter libraryMetadataAdapter = libraryMetadataAdapters.get(0);
            assertThat(libraryMetadataAdapter.getName()).isEqualTo(myLibraryV1.getName());
            assertThat(libraryMetadataAdapter.getNamespace()).isEqualTo(myLibraryV1.getNamespace());
            assertThat(libraryMetadataAdapter.getVersion()).isEqualTo(myLibraryV1.getVersion());
        });
        final Resource mainResource = projectResourceSet.getResources().stream().filter(resource -> this.getResourceName(resource).equals("ProjectUsingMyLibraryV1")).findFirst().get();
        final AttributeUsage attribute1 = Streams.of(mainResource.getAllContents()).filter(AttributeUsage.class::isInstance).map(AttributeUsage.class::cast)
                .filter(attribute -> attribute.getDeclaredName().equals("attribute1")).findFirst().get();
        assertThat(attribute1.getType()).hasSize(1);
        final Type attribute1Type = attribute1.getType().get(0);
        assertThat(attribute1Type.getDeclaredName()).isEqualTo("AttributeDefinition1");
        assertThat(attribute1Type.eResource().getResourceSet()).isEqualTo(projectResourceSet);
        assertThat(this.getResourceName(attribute1Type.eResource())).isEqualTo("LibraryResource1");
        final AttributeUsage attribute2 = Streams.of(mainResource.getAllContents()).filter(AttributeUsage.class::isInstance).map(AttributeUsage.class::cast)
                .filter(attribute -> attribute.getDeclaredName().equals("attribute2")).findFirst().get();
        assertThat(attribute2.getType()).hasSize(1);
        final Type attribute2Type = attribute2.getType().get(0);
        assertThat(attribute2Type.getDeclaredName()).isEqualTo("AttributeDefinition2");
        assertThat(attribute2Type.eResource().getResourceSet()).isEqualTo(projectResourceSet);
        assertThat(this.getResourceName(attribute2Type.eResource())).isEqualTo("LibraryResource2");
        final AttributeUsage attribute3 = Streams.of(mainResource.getAllContents()).filter(AttributeUsage.class::isInstance).map(AttributeUsage.class::cast)
                .filter(attribute -> attribute.getDeclaredName().equals("attribute3")).findFirst().get();
        assertThat(attribute3.getType()).hasSize(1);
        final Type attribute3Type = attribute3.getType().get(0);
        assertThat(attribute3Type.getDeclaredName()).isEqualTo("AttributeDefinition3");
        assertThat(attribute3Type.eResource().getResourceSet()).isEqualTo(projectResourceSet);
        assertThat(this.getResourceName(attribute3Type.eResource())).isEqualTo("LibraryResource3");

        this.projectEditingContextEventProcessor = this.factory.createEditingContextEventProcessor(this.projectEditingContext);

        final Library myLibraryV2 = this.loadMyLibraryV2();
        final UpdateLibraryInput updateLibraryInput = new UpdateLibraryInput(UUID.randomUUID(), this.projectEditingContext.getId(), myLibraryV2.getId());
        this.projectEditingContextEventProcessor.handle(updateLibraryInput);

        this.myLibraryV1LastModifiedOnBefore = myLibraryV1.getLastModifiedOn();
        this.myLibraryV2LastModifiedOnBefore = myLibraryV2.getLastModifiedOn();
        this.myLibraryV1SemanticDataLastModifiedOnBefore = myLibraryV1SemanticData.getLastModifiedOn();
        this.myLibraryV2SemanticDataLastModifiedOnBefore = myLibraryV2SemanticData.getLastModifiedOn();
    }

    @Test
    @DisplayName("The metadatas and contents of the libraries have not been modified")
    @SysONLibraryImportTestServer
    public void testLibrariesHaveNotBeenModified() {
        assertThat(this.loadMyLibraryV1().getLastModifiedOn()).isEqualTo(this.myLibraryV1LastModifiedOnBefore);
        assertThat(this.loadMyLibraryV2().getLastModifiedOn()).isEqualTo(this.myLibraryV2LastModifiedOnBefore);
        assertThat(this.loadMyLibraryV1SemanticData().getLastModifiedOn()).isEqualTo(this.myLibraryV1SemanticDataLastModifiedOnBefore);
        assertThat(this.loadMyLibraryV2SemanticData().getLastModifiedOn()).isEqualTo(this.myLibraryV2SemanticDataLastModifiedOnBefore);
    }

    @Test
    @DisplayName("The dependencies of the project have been updated to reflect the library version update")
    @SysONLibraryImportTestServer
    public void testProjectDependenciesHaveBeenUpdated() {
        final SemanticData projectSemanticData = this.loadProjectSemanticData();
        assertThat(projectSemanticData.getDependencies()).hasSize(1);
        assertThat(projectSemanticData.getDependencies().get(0).dependencySemanticDataId().getId()).isEqualTo(this.loadMyLibraryV2().getSemanticData().getId());
    }

    @Test
    @DisplayName("The contents of the project have been updated to reflect the library version update")
    @SysONLibraryImportTestServer
    public void testProjectContentsHaveBeenUpdated() {
        final ResourceSet projectResourceSet = ((IEMFEditingContext) this.projectEditingContext).getDomain().getResourceSet();

        // LibraryResource1 no longer exists.
        final List<Resource> resourcesFromLibraries = projectResourceSet.getResources().stream().filter(resource -> resource.eAdapters().stream().anyMatch(LibraryMetadataAdapter.class::isInstance))
                .toList();
        assertThat(resourcesFromLibraries).hasSize(2);

        // Check that the metadata have been updated.
        final Library myLibraryV2 = this.loadMyLibraryV2();
        resourcesFromLibraries.forEach(resource -> {
            final List<LibraryMetadataAdapter> libraryMetadataAdapters = resource.eAdapters().stream()
                    .filter(LibraryMetadataAdapter.class::isInstance).map(LibraryMetadataAdapter.class::cast).toList();
            assertThat(libraryMetadataAdapters).hasSize(1);
            final LibraryMetadataAdapter libraryMetadataAdapter = libraryMetadataAdapters.get(0);
            assertThat(libraryMetadataAdapter.getName()).isEqualTo(myLibraryV2.getName());
            assertThat(libraryMetadataAdapter.getNamespace()).isEqualTo(myLibraryV2.getNamespace());
            assertThat(libraryMetadataAdapter.getVersion()).isEqualTo(myLibraryV2.getVersion());
        });

        final Resource mainResource = projectResourceSet.getResources().stream().filter(resource -> this.getResourceName(resource).equals("ProjectUsingMyLibraryV1")).findFirst().get();

        // LibraryResource1 no longer exists, and AttributeDefinition1 has been renamed and moved to LibrayResource3
        // So the type should be null.
        final AttributeUsage attribute1 = Streams.of(mainResource.getAllContents()).filter(AttributeUsage.class::isInstance).map(AttributeUsage.class::cast)
                .filter(attribute -> attribute.getDeclaredName().equals("attribute1")).findFirst().get();
        assertThat(attribute1.getType()).hasSize(1);
        final Type attribute1Type = attribute1.getType().get(0);
        assertThat(attribute1Type).isNull();

        // LibraryResource2 is unchanged, so our assumptions should still hold.
        final AttributeUsage attribute2 = Streams.of(mainResource.getAllContents()).filter(AttributeUsage.class::isInstance).map(AttributeUsage.class::cast)
                .filter(attribute -> attribute.getDeclaredName().equals("attribute2")).findFirst().get();
        assertThat(attribute2.getType()).hasSize(1);
        final Type attribute2Type = attribute2.getType().get(0);
        assertThat(attribute2Type.getDeclaredName()).isEqualTo("AttributeDefinition2");
        assertThat(attribute2Type.eResource().getResourceSet()).isEqualTo(projectResourceSet);
        assertThat(this.getResourceName(attribute2Type.eResource())).isEqualTo("LibraryResource2");

        // AttributeDefinition3 has been removed, so the type should be null now.
        final AttributeUsage attribute3 = Streams.of(mainResource.getAllContents()).filter(AttributeUsage.class::isInstance).map(AttributeUsage.class::cast)
                .filter(attribute -> attribute.getDeclaredName().equals("attribute3")).findFirst().get();
        assertThat(attribute3.getType()).hasSize(1);
        final Type attribute3Type = attribute3.getType().get(0);
        assertThat(attribute3Type).isNull();
    }

    protected Library loadMyLibraryV1() {
        return this.librarySearchService
                .findByNamespaceAndNameAndVersion(
                        this.loadProjectByName("MyLibrary").getId(),
                        "MyLibrary",
                        "v1")
                .orElseThrow();
    }

    protected SemanticData loadMyLibraryV1SemanticData() {
        return this.semanticDataSearchService.findById(this.loadMyLibraryV1().getSemanticData().getId()).orElseThrow();
    }

    protected Library loadMyLibraryV2() {
        return this.librarySearchService
                .findByNamespaceAndNameAndVersion(
                        this.loadProjectByName("MyLibrary").getId(),
                        "MyLibrary",
                        "v2")
                .orElseThrow();
    }

    protected SemanticData loadMyLibraryV2SemanticData() {
        return this.semanticDataSearchService.findById(this.loadMyLibraryV2().getSemanticData().getId()).orElseThrow();
    }

    protected SemanticData loadProjectSemanticData() {
        final Project project = this.loadProjectByName("ProjectUsingMyLibraryV1");
        final ProjectSemanticData projectSemanticData = this.projectSemanticDataSearchService.findByProjectId(AggregateReference.to(project.getId())).orElseThrow();
        return this.semanticDataSearchService.findById(projectSemanticData.getSemanticData().getId()).orElseThrow();
    }

    protected String getResourceName(final Resource resource) {
        return resource.eAdapters().stream()
                .filter(ResourceMetadataAdapter.class::isInstance)
                .map(ResourceMetadataAdapter.class::cast)
                .findFirst()
                .get()
                .getName();
    }

    private Project loadProjectByName(final String projectName) {
        final List<Project> candidates = this.projectSearchService.findAll(ScrollPosition.keyset(), 10, Map.of()).stream()
                .filter(project -> project.getName().equals(projectName))
                .toList();
        if (candidates.size() != 1) {
            return null;
        } else {
            return candidates.get(0);
        }
    }
}
