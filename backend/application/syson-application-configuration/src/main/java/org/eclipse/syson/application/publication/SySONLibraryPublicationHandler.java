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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.components.core.api.ErrorPayload;
import org.eclipse.sirius.components.core.api.IEditingContextSearchService;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.components.emf.services.JSONResourceFactory;
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
import org.eclipse.sirius.web.domain.boundedcontexts.library.Library;
import org.eclipse.sirius.web.domain.boundedcontexts.library.services.api.ILibrarySearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.project.Project;
import org.eclipse.sirius.web.domain.boundedcontexts.project.services.api.IProjectSearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.projectsemanticdata.services.api.IProjectSemanticDataSearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.Document;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.SemanticData;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.services.api.ISemanticDataCreationService;
import org.eclipse.sirius.web.domain.services.IResult;
import org.eclipse.sirius.web.domain.services.Success;
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
public class SySONLibraryPublicationHandler implements ILibraryPublicationHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SySONLibraryPublicationHandler.class);

    private final IEditingContextSearchService editingContextSearchService;

    private final ISemanticDataCreationService semanticDataCreationService;

    private final IResourceToDocumentService resourceToDocumentService;

    private final IProjectSemanticDataSearchService projectSemanticDataSearchService;

    private final IProjectSearchService projectSearchService;

    private final ILibrarySearchService librarySearchService;

    public SySONLibraryPublicationHandler(final IEditingContextSearchService editingContextSearchService,
            final ISemanticDataCreationService semanticDataCreationService,
            final IResourceToDocumentService resourceToDocumentService,
            final IProjectSemanticDataSearchService projectSemanticDataSearchService, final IProjectSearchService projectSearchService,
            final ILibrarySearchService librarySearchService) {
        this.projectSearchService = Objects.requireNonNull(projectSearchService);
        this.editingContextSearchService = Objects.requireNonNull(editingContextSearchService);
        this.semanticDataCreationService = Objects.requireNonNull(semanticDataCreationService);
        this.resourceToDocumentService = Objects.requireNonNull(resourceToDocumentService);
        this.projectSemanticDataSearchService = Objects.requireNonNull(projectSemanticDataSearchService);
        this.librarySearchService = Objects.requireNonNull(librarySearchService);
    }

    @Override
    public boolean canHandle(final PublishLibrariesInput input) {
        return Objects.equals(input.publicationKind(), "Project_SysML_AllProperContents");
    }

    @Override
    public IPayload handle(final PublishLibrariesInput input) {
        return this.projectSearchService.findById(input.projectId())
                .map(project -> this.projectSemanticDataSearchService.findByProjectId(AggregateReference.to(input.projectId()))
                        .flatMap(projectSemanticData -> this.editingContextSearchService.findById(projectSemanticData.getSemanticData().getId().toString()))
                        .filter(IEMFEditingContext.class::isInstance)
                        .map(IEMFEditingContext.class::cast)
                        .map(editingContext -> this.handle(input, project, editingContext.getDomain().getResourceSet()))
                        .orElseGet(() -> new ErrorPayload(input.id(),
                                List.of(new Message("Could not find the editing context of project '%s'.".formatted(project.getName()), MessageLevel.ERROR)))))
                .orElseGet(() -> new ErrorPayload(input.id(), List.of(new Message("Could not find project with ID '%s'.".formatted(input.projectId()), MessageLevel.ERROR))));
    }

    protected IPayload handle(final PublishLibrariesInput input, final Project project, final ResourceSet resourceSet) {
        final IPayload result;

        final String libraryNamespace = project.getId();
        final String libraryName = project.getName();
        final String libraryVersion = input.version();
        final Map<Resource, List<EObject>> properSysMLRootContents = this.getProperSysMLRootContents(resourceSet);

        if (!properSysMLRootContents.isEmpty()) {
            if (!this.librarySearchService.existsByNamespaceAndNameAndVersion(libraryNamespace, libraryName, libraryVersion)) {

                // Create a ResourceSet with resources containing copies of the SysML contents to publish.
                final ResourceSet publishableResourceSet = new ResourceSetImpl();
                final EcoreUtil.Copier ecoreCopier = new EcoreUtil.Copier();
                final List<Resource> standaloneResources = properSysMLRootContents.entrySet().stream().map(entry -> {
                    final String resourceName = this.getResourceName(entry.getKey());
                    // Important: the library upgrade process will use the resource name as identity, so it should not
                    // include the library version.
                    final String libraryResourceId = "%s:%s:%s".formatted(libraryNamespace, libraryName, resourceName);
                    final Resource standaloneResource = this.createResource(this.getResourceName(entry.getKey()), libraryResourceId, ecoreCopier.copyAll(entry.getValue()));
                    return standaloneResource;
                }).toList();
                publishableResourceSet.getResources().addAll(standaloneResources);
                ecoreCopier.copyReferences();

                // Identify which external resources are actually referenced by the SysML contents.
                final List<Resource> externalResourcesReferencedBySysMLContents = this.findAllExternalResourcesReferencedBy(publishableResourceSet);

                // Find the semantic data dependencies that the published library will need to reference.
                final List<AggregateReference<SemanticData, UUID>> dependencies = this.getDependenciesToPublishedLibraries(publishableResourceSet).stream()
                        .filter(dependency -> externalResourcesReferencedBySysMLContents.contains(dependency))
                        .map(dependency -> {
                            final LibraryMetadataAdapter libraryMetadata = this.getLibraryMetadata(dependency).get();
                            final Optional<Library> maybeLibrary = this.librarySearchService.findByNamespaceAndNameAndVersion(libraryMetadata.getNamespace(), libraryMetadata.getName(),
                                    libraryMetadata.getVersion());
                            return maybeLibrary.map(Library::getId).orElseGet(() -> {
                                LOGGER.warn(
                                        "In project '%s', resource '%s' (URI: '%s') has library metadata, but the matching actual Library could not be retrieved.".formatted(project.getName(),
                                                this.getResourceName(dependency), dependency.getURI().toString()));
                                return null;
                            });
                        })
                        .filter(Objects::nonNull)
                        .distinct()
                        .map(AggregateReference::<SemanticData, UUID> to)
                        .toList();

                // Publish the library by creating its SemanticData.
                final Optional<SemanticData> maybePublishedLibrarySemanticData = this.publishAsLibrary(input, publishableResourceSet, libraryName, libraryVersion, dependencies);
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
                // There is already a Library from our namespace with that name and version.
                result = new ErrorPayload(input.id(),
                        List.of(new Message("Library '%s' (version '%s') already exists in namespace '%s'.".formatted(libraryName, libraryVersion, libraryNamespace), MessageLevel.ERROR)));
            }
        } else {
            // There are no proper SysML contents to publish.
            result = new ErrorPayload(input.id(),
                    List.of(new Message("There are no SysML contents in project '%s' to publish as library.".formatted(project.getName()), MessageLevel.ERROR)));
        }

        return result;
    }

    private Resource createResource(final String resourceName, final String resourceId, final Collection<EObject> contents) {
        final JSONResourceFactory jsonResourceFactory = new JSONResourceFactory();
        final URI uri = jsonResourceFactory.createResourceURI(UUID.nameUUIDFromBytes(resourceId.getBytes()).toString());
        final Resource isolatedResource = jsonResourceFactory.createResource(uri);
        isolatedResource.eAdapters().add(new ResourceMetadataAdapter(resourceName));
        isolatedResource.getContents().addAll(contents);
        return isolatedResource;
    }

    protected Optional<SemanticData> publishAsLibrary(final ICause parentCause, final ResourceSet resourceSet, final String name, final String version,
            final List<AggregateReference<SemanticData, UUID>> dependencies) {
        final ICause cause = new SysONPublishedLibrarySemanticDataCreationRequested(parentCause, name);
        return this.createSemanticData(cause, resourceSet.getResources(), dependencies);
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

    protected Map<Resource, List<EObject>> getProperSysMLRootContents(final ResourceSet resourceSet) {
        Objects.requireNonNull(resourceSet);

        final List<Resource> dependencies = this.getDependenciesToPublishedLibraries(resourceSet);
        final List<Resource> candidateResources = resourceSet.getResources().stream()
                .filter(resource -> !dependencies.contains(resource))
                .toList();

        final Map<Resource, List<EObject>> properSysMLRootContentsByTheirContainingResource = new LinkedHashMap<>();
        candidateResources.stream().forEach(resource -> {
            properSysMLRootContentsByTheirContainingResource.put(resource,
                    resource.getContents().stream()
                            .filter(this::isSysmlContent)
                            .toList());
        });
        return properSysMLRootContentsByTheirContainingResource;
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

    protected List<Resource> findAllExternalResourcesReferencedBy(final ResourceSet resourceSet) {
        final Set<EObject> referencedExternalElements = EcoreUtil.ExternalCrossReferencer.find(resourceSet).keySet();
        final List<Resource> referencedExternalResources = referencedExternalElements.stream()
                .map(EObject::eResource)
                .distinct()
                .toList();
        return referencedExternalResources;
    }
}
