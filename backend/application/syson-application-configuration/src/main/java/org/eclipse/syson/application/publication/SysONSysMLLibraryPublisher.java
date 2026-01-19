/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.components.core.api.ErrorPayload;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.events.ICause;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.sirius.web.application.editingcontext.services.DocumentData;
import org.eclipse.sirius.web.application.editingcontext.services.EPackageEntry;
import org.eclipse.sirius.web.application.editingcontext.services.api.IResourceToDocumentService;
import org.eclipse.sirius.web.application.library.services.LibraryMetadataAdapter;
import org.eclipse.sirius.web.application.studio.services.library.api.DependencyGraph;
import org.eclipse.sirius.web.domain.boundedcontexts.library.Library;
import org.eclipse.sirius.web.domain.boundedcontexts.library.services.api.ILibrarySearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.Document;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.SemanticData;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.services.api.ISemanticDataCreationService;
import org.eclipse.sirius.web.domain.services.IResult;
import org.eclipse.sirius.web.domain.services.Success;
import org.eclipse.syson.application.omnibox.api.IPredicateCanEditingContextPublishSysMLProject;
import org.eclipse.syson.application.publication.api.ISysMLLibraryPublisher;
import org.eclipse.syson.application.publication.api.ISysONLibraryDependencyCollector;
import org.eclipse.syson.application.services.SysONResourceService;
import org.eclipse.syson.services.api.ISysONResourceService;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;

/**
 * SysON implementation of {@link ISysMLLibraryPublisher}.
 * <p>
 * It publishes as one library all the {@link Resource resources} of the given {@link ResourceSet} matching the
 * following criteria:
 * <ul>
 * <li>The {@link Resource} is not from a library
 * ({@link SysONResourceService#isFromReferencedLibrary(IEditingContext, Resource) published} or
 * {@link ElementUtil#isStandardLibraryResource(Resource) standard})</li>
 * <li>The {@link Resource} is {@link SysONResourceService#isSysML(Resource) identified as a SysML resource}</li>
 * </ul>
 * <b>Note:</b> Attempting to publish a library that already exists (exact same {@code namespace}, {@code name} and
 * {@code version}) will result in an error.
 *
 * @author flatombe
 */
@Service
public class SysONSysMLLibraryPublisher implements ISysMLLibraryPublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysONSysMLLibraryPublisher.class);

    private final ILibrarySearchService librarySearchService;

    private final IPredicateCanEditingContextPublishSysMLProject predicateCanEditingContextPublishSysMLProject;

    private final IResourceToDocumentService resourceToDocumentService;

    private final ISemanticDataCreationService semanticDataCreationService;

    private final ISysONLibraryDependencyCollector sysONLibraryDependencyCollector;

    private final ISysONResourceService sysONResourceService;

    public SysONSysMLLibraryPublisher(final ILibrarySearchService librarySearchService, final IPredicateCanEditingContextPublishSysMLProject predicateCanEditingContextPublishSysMLProject,
            final IResourceToDocumentService resourceToDocumentService, final ISemanticDataCreationService semanticDataCreationService,
            final ISysONLibraryDependencyCollector sysonLibraryDependencyCollector, final ISysONResourceService sysONResourceService) {
        this.librarySearchService = Objects.requireNonNull(librarySearchService);
        this.predicateCanEditingContextPublishSysMLProject = Objects.requireNonNull(predicateCanEditingContextPublishSysMLProject);
        this.resourceToDocumentService = Objects.requireNonNull(resourceToDocumentService);
        this.semanticDataCreationService = Objects.requireNonNull(semanticDataCreationService);
        this.sysONLibraryDependencyCollector = Objects.requireNonNull(sysonLibraryDependencyCollector);
        this.sysONResourceService = Objects.requireNonNull(sysONResourceService);
    }

    @Override
    public IPayload publish(final ICause cause, final IEditingContext libraryAuthoringEditingContext, final String libraryNamespace, final String libraryName,
            final String libraryVersion, final String libraryDescription) {
        final IPayload result;

        if (!this.predicateCanEditingContextPublishSysMLProject.test(libraryAuthoringEditingContext.getId())) {
            result = new ErrorPayload(cause.id(),
                    List.of(new Message("Cannot publish SysML library from editing context '%s'.".formatted(libraryAuthoringEditingContext.getId()), MessageLevel.ERROR)));
        } else if (this.librarySearchService.findByNamespaceAndNameAndVersion(libraryNamespace, libraryName, libraryVersion).isPresent()) {
            // The Sirius Web lifecycle for published libraries relies on their immutability, so we want to prevent the
            // publication from happening.
            result = new ErrorPayload(cause.id(),
                    List.of(new Message("Library '%s:%s@%s' already exists.".formatted(libraryNamespace, libraryName, libraryVersion), MessageLevel.ERROR)));
        } else if (!(libraryAuthoringEditingContext instanceof IEMFEditingContext)) {
            result = new ErrorPayload(cause.id(),
                    List.of(new Message(
                            "Editing context '%s' is of an unsupported type: %s".formatted(libraryAuthoringEditingContext.getId(), libraryAuthoringEditingContext.getClass().getCanonicalName()),
                            MessageLevel.ERROR)));
        } else {
            result = this.doPublish(cause, (IEMFEditingContext) libraryAuthoringEditingContext, libraryNamespace, libraryName, libraryVersion, libraryDescription);
        }
        return result;
    }

    /**
     * Publishes all the proper SysML contents of an editing context as a single library with the specified identity
     * (namespace, name and version) and description.
     *
     * @param cause
     *            the (non-{@code null}) originating {@link ICause}.
     * @param emfEditingContext
     *            the (non-{@code null}) {@link IEMFEditingContext} that authors the library.
     * @param libraryNamespace
     *            the (non-{@code null}) desired {@link Library#getNamespace() namespace} for the library to publish.
     * @param libraryName
     *            the (non-{@code null}) desired {@link Library#getName() name} for the library to publish.
     * @param libraryVersion
     *            the (non-{@code null}) desired {@link Library#getVersion() version} for the library to publish.
     * @param libraryDescription
     *            the (non-{@code null}) desired {@link Library#getDescription() description} for the library to
     *            publish.
     * @return the (non-{@code null}) resulting {@link IPayload}.
     */
    protected IPayload doPublish(final ICause cause, final IEMFEditingContext emfEditingContext, final String libraryNamespace, final String libraryName,
            final String libraryVersion, final String libraryDescription) {
        final Set<Resource> resourcesToPublish = this.getResourcesToPublish(emfEditingContext);
        final DependencyGraph<Resource> dependencyGraph = this.sysONLibraryDependencyCollector.collectDependencies(emfEditingContext.getDomain().getResourceSet());

        final List<AggregateReference<SemanticData, UUID>> dependencies = this.getDependencies(dependencyGraph, resourcesToPublish);

        final Optional<SemanticData> maybePublishedLibrarySemanticData = this.publishAsLibrary(cause, resourcesToPublish, libraryNamespace, libraryName, libraryVersion, libraryDescription,
                dependencies);
        // After this transaction is done, SysONLibraryPublicationListener reacts by also creating the
        // associated Library metadata.

        return maybePublishedLibrarySemanticData
                .map(publishedLibrarySemanticData -> (IPayload) new SuccessPayload(cause.id(),
                        List.of(new Message("Successfully published library '%s:%s@%s'.".formatted(libraryNamespace, libraryName, libraryVersion), MessageLevel.SUCCESS))))
                .orElseGet(
                        () -> new ErrorPayload(cause.id(),
                                List.of(new Message("Failed to publish library '%s:%s@%s'.".formatted(libraryNamespace, libraryName, libraryVersion), MessageLevel.ERROR))));
    }

    protected List<AggregateReference<SemanticData, UUID>> getDependencies(final DependencyGraph<Resource> dependencyGraph, final Set<Resource> resourcesToPublish) {
        final List<AggregateReference<SemanticData, UUID>> dependencies = new ArrayList<>();

        for (final Resource resourceToPublish : resourcesToPublish) {
            for (final Resource dependencyCandidate : dependencyGraph.getDependencies(resourceToPublish)) {
                // This Resource is a "dependency" in the EMF sense of the word.
                // It may be a proper Resource of the ResourceSet, or a Resource present in the ResourceSet because it
                // originally belongs to a published library (a dependency in the Sirius Web sense of the word).
                // Since this implementation publishes all proper Resources of the ResourceSet into a single library, we
                // only need to look for the Resources which come from the published libraries we have in dependencies.
                this.getLibraryMetadata(dependencyCandidate).ifPresent(libraryMetadataAdapter -> {
                    this.librarySearchService.findByNamespaceAndNameAndVersion(
                            libraryMetadataAdapter.getNamespace(),
                            libraryMetadataAdapter.getName(),
                            libraryMetadataAdapter.getVersion())
                            .map(Library::getSemanticData)
                            .ifPresentOrElse(dependencies::add,
                                    () -> LOGGER.warn("Cannot retrieve contents of library '%s:%s@%s'"
                                            .formatted(libraryMetadataAdapter.getNamespace(), libraryMetadataAdapter.getName(),
                                                    libraryMetadataAdapter.getVersion())));
                });
            }
        }
        return dependencies.stream()
                .distinct()
                .toList();
    }

    protected Optional<SemanticData> publishAsLibrary(final ICause parentCause, final Collection<Resource> resources, final String libraryNamespace, final String libraryName,
            final String libraryVersion, final String libraryDescription, final List<AggregateReference<SemanticData, UUID>> dependencies) {
        // Remove the imported flag on the resource: the resource is now a library, and its read-only/read-write nature
        // should be determined by its import kind (reference or copy), and not whether it was imported from a textual
        // SysML file in the first place.
        resources.forEach(resource -> ElementUtil.setIsImported(resource, false));

        final ICause cause = new SysONPublishedLibrarySemanticDataCreationRequested(parentCause, libraryNamespace, libraryName, libraryVersion, libraryDescription);
        return this.createSemanticData(cause, resources, dependencies);
    }

    protected Optional<SemanticData> createSemanticData(final ICause cause, final Collection<Resource> resources, final List<AggregateReference<SemanticData, UUID>> dependencies) {
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

        final IResult<SemanticData> creationResult = this.semanticDataCreationService.create(cause, documents, domains, dependencies);
        if (creationResult instanceof Success<SemanticData> creationSuccess) {
            return Optional.ofNullable(creationSuccess.data());
        } else {
            return Optional.empty();
        }
    }

    protected Set<Resource> getResourcesToPublish(final IEMFEditingContext emfEditingContext) {
        return emfEditingContext.getDomain().getResourceSet().getResources().stream()
                .filter(Predicate.not(ElementUtil::isStandardLibraryResource))
                .filter(resource -> !this.sysONResourceService.isFromReferencedLibrary(emfEditingContext, resource))
                // Only the ".sysml" resources can be published.
                .filter(this.sysONResourceService::isSysML)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    protected Optional<LibraryMetadataAdapter> getLibraryMetadata(final Resource resource) {
        return resource.eAdapters().stream()
                .filter(LibraryMetadataAdapter.class::isInstance)
                .map(LibraryMetadataAdapter.class::cast)
                .findFirst();
    }
}
