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
package org.eclipse.syson.application.publication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.components.core.api.ErrorPayload;
import org.eclipse.sirius.components.core.api.IEditingContextSearchService;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.events.ICause;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.sirius.web.application.editingcontext.services.DocumentData;
import org.eclipse.sirius.web.application.editingcontext.services.EPackageEntry;
import org.eclipse.sirius.web.application.editingcontext.services.api.IResourceToDocumentService;
import org.eclipse.sirius.web.application.library.dto.PublishLibrariesInput;
import org.eclipse.sirius.web.application.library.services.LibraryMetadataAdapter;
import org.eclipse.sirius.web.application.library.services.api.ILibraryPublicationHandler;
import org.eclipse.sirius.web.application.project.services.api.IProjectEditingContextService;
import org.eclipse.sirius.web.application.studio.services.library.api.DependencyGraph;
import org.eclipse.sirius.web.domain.boundedcontexts.library.Library;
import org.eclipse.sirius.web.domain.boundedcontexts.library.services.api.ILibrarySearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.project.Project;
import org.eclipse.sirius.web.domain.boundedcontexts.project.services.api.IProjectSearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.Document;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.SemanticData;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.services.api.ISemanticDataCreationService;
import org.eclipse.sirius.web.domain.services.IResult;
import org.eclipse.sirius.web.domain.services.Success;
import org.eclipse.syson.application.publication.api.ISysONLibraryDependencyCollector;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;

/**
 * {@link ILibraryPublicationHandler} for publishing libraries in SysON.
 *
 * @see SysONLibraryPublicationListener
 * @author flatombe
 */
@Service
public class SysONLibraryPublicationHandler implements ILibraryPublicationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysONLibraryPublicationHandler.class);

    private final IEditingContextSearchService editingContextSearchService;

    private final IProjectEditingContextService projectEditingContextService;

    private final ISemanticDataCreationService semanticDataCreationService;

    private final IResourceToDocumentService resourceToDocumentService;

    private final IProjectSearchService projectSearchService;

    private final ILibrarySearchService librarySearchService;

    private final ISysONLibraryDependencyCollector sysonLibraryDependencyCollector;

    public SysONLibraryPublicationHandler(final IEditingContextSearchService editingContextSearchService,
            final IProjectEditingContextService projectEditingContextService,
            final ISemanticDataCreationService semanticDataCreationService,
            final IResourceToDocumentService resourceToDocumentService,
            final IProjectSearchService projectSearchService,
            final ILibrarySearchService librarySearchService,
            final ISysONLibraryDependencyCollector sysonLibraryDependencyCollector) {
        this.projectSearchService = Objects.requireNonNull(projectSearchService);
        this.projectEditingContextService = Objects.requireNonNull(projectEditingContextService);
        this.editingContextSearchService = Objects.requireNonNull(editingContextSearchService);
        this.semanticDataCreationService = Objects.requireNonNull(semanticDataCreationService);
        this.resourceToDocumentService = Objects.requireNonNull(resourceToDocumentService);
        this.librarySearchService = Objects.requireNonNull(librarySearchService);
        this.sysonLibraryDependencyCollector = Objects.requireNonNull(sysonLibraryDependencyCollector);
    }

    @Override
    public boolean canHandle(final PublishLibrariesInput input) {
        return Objects.equals(input.publicationKind(), "Project_SysML_AllProperContents");
    }

    @Override
    public IPayload handle(final PublishLibrariesInput input) {
        IPayload payload = null;
        var editingContextId = input.editingContextId();
        var optionalProjectId = this.projectEditingContextService.getProjectId(editingContextId);
        if (optionalProjectId.isPresent()) {
            var projectId = optionalProjectId.get();
            var optionalProject = this.projectSearchService.findById(projectId);
            var optionalEditingContext = this.editingContextSearchService.findById(editingContextId)
                    .filter(IEMFEditingContext.class::isInstance)
                    .map(IEMFEditingContext.class::cast);

            if (optionalProject.isPresent() && optionalEditingContext.isPresent()) {
                payload = this.handle(input, optionalProject.get(), optionalEditingContext.get().getDomain().getResourceSet());
            } else {
                payload = new ErrorPayload(input.id(), List.of(new Message("Could not find project with following editingContextId '%s'.".formatted(editingContextId), MessageLevel.ERROR)));
            }
        } else {
            payload = new ErrorPayload(input.id(), List.of(new Message("Could not find project with following editingContextId '%s'.".formatted(editingContextId), MessageLevel.ERROR)));
        }
        return payload;
    }

    protected IPayload handle(final PublishLibrariesInput input, final Project project, final ResourceSet resourceSet) {
        final IPayload result;

        final String libraryName = project.getName();
        final String libraryVersion = input.version();

        if (this.librarySearchService.findByNamespaceAndNameAndVersion(project.getId(), libraryName, libraryVersion).isPresent()) {
            // There is already a Library from our namespace with that name and version.
            result = new ErrorPayload(input.id(),
                    List.of(new Message("Library '%s' (version '%s') already exists in namespace '%s'.".formatted(libraryName, libraryVersion, project.getId()), MessageLevel.ERROR)));
        } else {
            final Set<Resource> resourcesToPublish = this.getProperSysMLRootContents(resourceSet);
            if (!resourcesToPublish.isEmpty()) {
                DependencyGraph<Resource> dependencyGraph = this.sysonLibraryDependencyCollector.collectDependencies(resourceSet);

                List<AggregateReference<SemanticData, UUID>> dependencies = this.getDependencies(dependencyGraph, resourcesToPublish);

                final Optional<SemanticData> maybePublishedLibrarySemanticData = this.publishAsLibrary(input, resourcesToPublish, libraryName, libraryVersion, dependencies);
                // After this transaction is done, SysONLibraryPublicationListener reacts by also creating the
                // associated Library metadata.

                result = maybePublishedLibrarySemanticData
                        .map(publishedLibrary -> (IPayload) new SuccessPayload(input.id(),
                                List.of(new Message(
                                        "Successfully published the SysML contents of project '%s' as version '%s' of library '%s'.".formatted(project.getName(),
                                                libraryVersion,
                                                libraryName),
                                        MessageLevel.SUCCESS))))
                        .orElseGet(
                                () -> new ErrorPayload(input.id(),
                                        List.of(new Message("Failed to publish the SysML contents of project '%s' as a library.".formatted(project.getName(), project.getId()),
                                                MessageLevel.ERROR))));
            } else {
                result = new ErrorPayload(input.id(),
                        List.of(new Message("There are no SysML contents in project '%s' to publish as library.".formatted(project.getName()), MessageLevel.ERROR)));
            }
        }
        return result;
    }

    private List<AggregateReference<SemanticData, UUID>> getDependencies(DependencyGraph<Resource> dependencyGraph, final Set<Resource> resourcesToPublish) {
        List<AggregateReference<SemanticData, UUID>> dependencies = new ArrayList<>();

        for (Resource resourceToPublish : resourcesToPublish) {
            for (Resource dependencyCandidate : dependencyGraph.getDependencies(resourceToPublish)) {
                Optional<LibraryMetadataAdapter> optionalLibraryMetadata = dependencyCandidate.eAdapters().stream()
                        .filter(LibraryMetadataAdapter.class::isInstance)
                        .map(LibraryMetadataAdapter.class::cast)
                        .findFirst();
                if (optionalLibraryMetadata.isPresent()) {
                    LibraryMetadataAdapter libraryMetadataAdapter = optionalLibraryMetadata.get();
                    this.librarySearchService.findByNamespaceAndNameAndVersion(libraryMetadataAdapter.getNamespace(), libraryMetadataAdapter.getName(), libraryMetadataAdapter.getVersion())
                            .map(Library::getSemanticData)
                            .ifPresentOrElse(dependencies::add, () -> LOGGER.warn("Cannot retrieve library {}:{}:{}", libraryMetadataAdapter.getNamespace(), libraryMetadataAdapter.getName(),
                                    libraryMetadataAdapter.getVersion()));
                }
                // Ignore the resource if it isn't a library: all non-library resources are published in a single
                // library in SysON.
            }
        }
        return dependencies.stream()
                .distinct()
                .toList();
    }

    protected Optional<SemanticData> publishAsLibrary(final ICause parentCause, final Set<Resource> resources, final String name, final String version,
            final List<AggregateReference<SemanticData, UUID>> dependencies) {
        final ICause cause = new SysONPublishedLibrarySemanticDataCreationRequested(parentCause, name);
        // Remove the imported flag on the resource: the resource is now a library, and its read-only/read-write nature
        // should be determined by its import kind (reference or copy), and not whether it was imported from a textual
        // SysML file in the first place.
        resources.forEach(resource -> ElementUtil.setIsImported(resource, false));
        return this.createSemanticData(cause, resources, dependencies);
    }

    protected Optional<SemanticData> createSemanticData(final ICause event, final Collection<Resource> resources, final List<AggregateReference<SemanticData, UUID>> dependencies) {
        final List<DocumentData> documentDatas = resources.stream()
                .map(resource -> this.resourceToDocumentService.toDocument(resource, false))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        final List<Document> documents = documentDatas.stream()
                .map(DocumentData::document)
                .toList();
        final List<String> domains = documentDatas.stream()
                .map(DocumentData::ePackageEntries)
                .flatMap(List::stream)
                .map(EPackageEntry::nsURI)
                .distinct()
                .toList();

        final IResult<SemanticData> creationResult = this.semanticDataCreationService.create(event, documents, domains, dependencies);
        if (creationResult instanceof Success<SemanticData> creationSuccess) {
            return Optional.ofNullable(creationSuccess.data());
        } else {
            return Optional.empty();
        }
    }

    protected Set<Resource> getProperSysMLRootContents(final ResourceSet resourceSet) {
        Objects.requireNonNull(resourceSet);

        final List<Resource> dependencies = this.getDependenciesToPublishedLibraries(resourceSet);
        return resourceSet.getResources().stream()
                .filter(resource -> !dependencies.contains(resource))
                .collect(Collectors.toSet());
    }

    protected List<Resource> getDependenciesToPublishedLibraries(ResourceSet resourceSet) {
        return resourceSet.getResources().stream()
                .filter(resource -> this.getLibraryMetadata(resource).isPresent()
                        || resource.getURI().scheme().equals(ElementUtil.KERML_LIBRARY_SCHEME)
                        || resource.getURI().scheme().equals(ElementUtil.SYSML_LIBRARY_SCHEME))
                .toList();
    }

    protected Optional<LibraryMetadataAdapter> getLibraryMetadata(final Resource resource) {
        return resource.eAdapters().stream()
                .filter(LibraryMetadataAdapter.class::isInstance)
                .map(LibraryMetadataAdapter.class::cast)
                .findFirst();
    }

    protected boolean isSysmlContent(final EObject rootEObject) {
        return rootEObject.eClass().getEPackage() == SysmlPackage.eINSTANCE;
    }

    protected String getResourceName(final Resource resource) {
        return resource.eAdapters().stream()
                .filter(ResourceMetadataAdapter.class::isInstance)
                .map(ResourceMetadataAdapter.class::cast)
                .map(ResourceMetadataAdapter::getName)
                .findFirst()
                .orElse(null);
    }
}
