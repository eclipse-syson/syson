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

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.components.collaborative.api.IEditingContextEventProcessor;
import org.eclipse.sirius.components.collaborative.editingcontext.EditingContextEventProcessorFactory;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IEditingContextSearchService;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.graphql.tests.ExecuteEditingContextFunctionInput;
import org.eclipse.sirius.components.graphql.tests.api.IExecuteEditingContextFunctionRunner;
import org.eclipse.sirius.web.domain.boundedcontexts.library.Library;
import org.eclipse.sirius.web.domain.boundedcontexts.library.services.api.ILibrarySearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.projectsemanticdata.ProjectSemanticData;
import org.eclipse.sirius.web.domain.boundedcontexts.projectsemanticdata.services.api.IProjectSemanticDataSearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.SemanticData;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.services.api.ISemanticDataSearchService;
import org.eclipse.sirius.web.tests.services.api.IGivenInitialServerState;
import org.eclipse.syson.AbstractIntegrationTests;
import org.eclipse.syson.application.data.ProjectWithLibraryDependencyTestProjectData;
import org.eclipse.syson.application.data.ProjectWithoutLibraryDependencyTestProjectData;
import org.eclipse.syson.sysml.metamodel.util.ElementUtil;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

/**
 * Partial {@link AbstractIntegrationTests} implementation for testing the import of a published library into a project.
 *
 * @see SysONLibraryImportByCopyTests
 * @see SysONLibraryImportByReferenceTests
 * @author flatombe
 */
public abstract class SysONLibraryImportTests extends AbstractIntegrationTests {

    protected String projectEditingContextId;

    protected Instant myLibraryV1LastModifiedInstantBefore;

    protected Instant myLibraryV1SemanticDataLastModifiedInstantBefore;

    @Autowired
    private IGivenInitialServerState givenInitialServerState;

    @Autowired
    private ISemanticDataSearchService semanticDataSearchService;

    @Autowired
    private ILibrarySearchService librarySearchService;

    @Autowired
    private IProjectSemanticDataSearchService projectSemanticDataSearchService;

    @Autowired
    private IEditingContextSearchService editingContextSearchService;

    @Autowired
    private IExecuteEditingContextFunctionRunner executeEditingContextFunctionRunner;

    @Autowired
    private EditingContextEventProcessorFactory editingContextEventProcessorFactory;

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

        this.projectEditingContextId = projectSemanticData.getId().toString();

        this.myLibraryV1LastModifiedInstantBefore = this.loadMyLibraryV1().getLastModifiedOn();
        this.myLibraryV1SemanticDataLastModifiedInstantBefore = myLibraryV1SemanticData.getLastModifiedOn();
    }

    protected Library loadMyLibraryV1() {
        return this.librarySearchService
                .findByNamespaceAndNameAndVersion(
                        ProjectWithLibraryDependencyTestProjectData.LIBRARY_PROJECT_ID,
                        "MyLibrary",
                        "v1")
                .orElseThrow();
    }

    protected SemanticData loadMyLibraryV1SemanticData() {
        return this.semanticDataSearchService.findById(this.loadMyLibraryV1().getSemanticData().getId()).orElseThrow();
    }

    protected SemanticData loadProjectSemanticData() {
        final ProjectSemanticData projectSemanticData = this.projectSemanticDataSearchService
                .findByProjectId(AggregateReference.to(ProjectWithoutLibraryDependencyTestProjectData.PROJECT_ID))
                .orElseThrow();
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

    /**
     * Loads the resources of a published library editing context.
     * <p>
     * Published libraries are read-only, so the editing-context dispatcher rejects the test function runner for their
     * semantic data. Loading this context through the search service is therefore required to inspect its resources.
     * </p>
     *
     * @param librarySemanticDataId
     *            the semantic data identifier of the published library
     * @return the library resources
     */
    protected List<Resource> loadPublishedLibraryResources(final String librarySemanticDataId) {
        return ((IEMFEditingContext) this.editingContextSearchService.findById(librarySemanticDataId).orElseThrow())
                .getDomain()
                .getResourceSet()
                .getResources();
    }

    /**
     * Executes a function using an editing context initialized with its standard libraries.
     *
     * @param editingContextId
     *            the identifier of the editing context to use
     * @param consumer
     *            the function to execute
     */
    protected void executeInEditingContext(final String editingContextId, final Consumer<IEMFEditingContext> consumer) {
        var input = new ExecuteEditingContextFunctionInput(UUID.randomUUID(), editingContextId, (editingContext, executeInput) -> {
            assertThat(editingContext)
                    .as("Editing context %s must be an EMF editing context", editingContextId)
                    .isInstanceOf(IEMFEditingContext.class);
            IEMFEditingContext emfEditingContext = (IEMFEditingContext) editingContext;
            assertThat(emfEditingContext.getDomain().getResourceSet().getResources())
                    .as("Editing context %s must contain standard libraries", editingContextId)
                    .anyMatch(ElementUtil::isStandardLibraryResource);
            consumer.accept(emfEditingContext);
            return new SuccessPayload(executeInput.id());
        });

        IPayload payload = this.executeEditingContextFunctionRunner.execute(input).block();
        assertThat(payload).isInstanceOf(SuccessPayload.class);
    }

    /**
     * Creates an event processor for an editing context supplied by the editing-context function runner.
     *
     * @param editingContext
     *            the editing context to handle events
     * @return an event processor for the editing context
     */
    protected IEditingContextEventProcessor createEditingContextEventProcessor(final IEditingContext editingContext) {
        return this.editingContextEventProcessorFactory.createEditingContextEventProcessor(editingContext);
    }

}
