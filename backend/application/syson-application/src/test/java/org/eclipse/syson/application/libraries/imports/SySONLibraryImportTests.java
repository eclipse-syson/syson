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

import java.time.Instant;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.components.collaborative.api.IEditingContextEventProcessor;
import org.eclipse.sirius.components.collaborative.editingcontext.EditingContextEventProcessorFactory;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IEditingContextSearchService;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
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
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

/**
 * Partial {@link AbstractIntegrationTests} implementation for testing the import of a published library into a project.
 *
 * @see SySONLibraryImportByCopyTests
 * @see SySONLibraryImportByReferenceTests
 * @author flatombe
 */
public abstract class SySONLibraryImportTests extends AbstractIntegrationTests {

    protected IEditingContext projectEditingContext;

    protected IEditingContextEventProcessor projectEditingContextEventProcessor;

    protected Instant myLibraryV1LastModifiedInstantBefore;

    protected Instant myLibraryV1SemanticDataLastModifiedInstantBefore;

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private IProjectSearchService projectSearchService;

    @Autowired
    private ISemanticDataSearchService semanticDataSearchService;

    @Autowired
    private ILibrarySearchService librarySearchService;

    @Autowired
    private IEditingContextSearchService editingContextSearchService;

    @Autowired
    private IProjectSemanticDataSearchService projectSemanticDataSearchService;

    @Autowired
    private EditingContextEventProcessorFactory factory;

    @BeforeEach
    public void initializeServerState() {
        this.givenInitialServerState.initialize();

        // Sanity checks about the library we want to import
        final SemanticData myLibraryV1SemanticData = this.loadMyLibraryV1SemanticData();
        assertThat(myLibraryV1SemanticData.getDependencies()).isEmpty();
        assertThat(myLibraryV1SemanticData.getDocuments()).hasSize(3);

        // Sanity checks about the project that will import the library
        final SemanticData projectSemanticData = this.loadProjectSemanticData();
        assertThat(projectSemanticData.getDependencies().isEmpty());
        assertThat(projectSemanticData.getDocuments()).hasSize(1);

        this.projectEditingContext = this.editingContextSearchService.findById(projectSemanticData.getId().toString()).orElseThrow();
        this.projectEditingContextEventProcessor = this.factory.createEditingContextEventProcessor(this.projectEditingContext);

        this.myLibraryV1LastModifiedInstantBefore = this.loadMyLibraryV1().getLastModifiedOn();
        this.myLibraryV1SemanticDataLastModifiedInstantBefore = myLibraryV1SemanticData.getLastModifiedOn();
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

    protected SemanticData loadProjectSemanticData() {
        final Project project = this.loadProjectByName("ProjectUsingNoLibraries");
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
        final List<Project> candidates = this.projectSearchService.findAll(ScrollPosition.keyset(), 10).stream()
                .filter(project -> project.getName().equals(projectName))
                .toList();
        if (candidates.size() != 1) {
            return null;
        } else {
            return candidates.get(0);
        }
    }
}
