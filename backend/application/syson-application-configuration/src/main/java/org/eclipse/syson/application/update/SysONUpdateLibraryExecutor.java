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
package org.eclipse.syson.application.update;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil.UnresolvedProxyCrossReferencer;
import org.eclipse.sirius.components.collaborative.api.ChangeKind;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.events.ICause;
import org.eclipse.sirius.components.representations.Failure;
import org.eclipse.sirius.components.representations.IStatus;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.sirius.components.representations.Success;
import org.eclipse.sirius.web.application.UUIDParser;
import org.eclipse.sirius.web.application.editingcontext.EditingContext;
import org.eclipse.sirius.web.application.editingcontext.services.api.IEditingContextDependencyLoader;
import org.eclipse.sirius.web.application.editingcontext.services.api.IEditingContextPersistenceFilter;
import org.eclipse.sirius.web.application.library.dto.UpdateLibraryInput;
import org.eclipse.sirius.web.application.library.services.LibraryMetadataAdapter;
import org.eclipse.sirius.web.application.studio.services.library.api.IUpdateLibraryExecutor;
import org.eclipse.sirius.web.domain.boundedcontexts.library.Library;
import org.eclipse.sirius.web.domain.boundedcontexts.library.services.api.ILibrarySearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.SemanticData;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.SemanticDataDependency;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.services.api.ISemanticDataSearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.services.api.ISemanticDataUpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;

/**
 * Used to execute a library update.
 *
 * @author gdaniel
 */
@Service
// This class should be removed once https://github.com/eclipse-sirius/sirius-web/issues/5400 is fixed.
@Primary
public class SysONUpdateLibraryExecutor implements IUpdateLibraryExecutor {

    public static final String REMOVED_PROXIES_PARAMETER_KEY = "removed_proxies_parameter_key";

    private final Logger logger = LoggerFactory.getLogger(SysONUpdateLibraryExecutor.class);

    private final ILibrarySearchService librarySearchService;

    private final ISemanticDataSearchService semanticDataSearchService;

    private final ISemanticDataUpdateService semanticDataUpdateService;

    private final IEditingContextDependencyLoader editingContextDependencyLoader;

    private final List<IEditingContextPersistenceFilter> editingContextPersistenceFilters;

    public SysONUpdateLibraryExecutor(ILibrarySearchService librarySearchService, ISemanticDataSearchService semanticDataSearchService, ISemanticDataUpdateService semanticDataUpdateService,
            IEditingContextDependencyLoader editingContextDependencyLoader, List<IEditingContextPersistenceFilter> editingContextPersistenceFilters) {
        this.librarySearchService = Objects.requireNonNull(librarySearchService);
        this.semanticDataSearchService = Objects.requireNonNull(semanticDataSearchService);
        this.semanticDataUpdateService = Objects.requireNonNull(semanticDataUpdateService);
        this.editingContextDependencyLoader = Objects.requireNonNull(editingContextDependencyLoader);
        this.editingContextPersistenceFilters = Objects.requireNonNull(editingContextPersistenceFilters);
    }

    @Override
    public IStatus updateLibrary(ICause cause, IEditingContext editingContext, UUID libraryId) {
        IStatus result = new Failure(List.of());
        if (editingContext instanceof EditingContext siriusWebEditingContext) {
            Optional<Library> optionalNewLibrary = this.librarySearchService.findById(libraryId);
            if (optionalNewLibrary.isPresent()) {
                Library newLibrary = optionalNewLibrary.get();

                // Find the version of the library already in the dependencies.
                Optional<Library> optionalOldLibrary = this.getLibraryDependencyWithDifferentVersion(siriusWebEditingContext, newLibrary);
                if (optionalOldLibrary.isPresent()) {
                    Library oldLibrary = optionalOldLibrary.get();
                    List<Resource> oldLibraryResources = this.getAllResourcesFromLibraryRecursiveDependencies(siriusWebEditingContext, oldLibrary);

                    for (Resource oldLibraryResource : oldLibraryResources) {
                        oldLibraryResource.unload();
                        siriusWebEditingContext.getDomain().getResourceSet().getResources().remove(oldLibraryResource);
                    }

                    if (cause instanceof UpdateLibraryInput) {
                        // Do not update the semantic data bounded context if we are not performing an actual library
                        // update (this is for example the case when performing an impact analysis).
                        this.addLibraryDependency(cause, siriusWebEditingContext, newLibrary);
                        this.removeLibraryDependency(cause, siriusWebEditingContext, oldLibrary);
                    }

                    List<SemanticData> newDependencies = new ArrayList<>();
                    this.semanticDataSearchService.findById(newLibrary.getSemanticData().getId())
                            .ifPresent(newLibrarySemanticData -> {
                                newDependencies.add(newLibrarySemanticData);
                                newDependencies.addAll(this.semanticDataSearchService.findAllDependenciesRecursivelyById(newLibrarySemanticData.getId()));
                            });

                    this.editingContextDependencyLoader.loadDependencies(siriusWebEditingContext, newDependencies);

                    long start = System.nanoTime();
                    Map<EObject, Collection<Setting>> removedProxies = this.removeUnresolvedProxies(siriusWebEditingContext.getDomain().getResourceSet());
                    Duration timeToRemoveProxies = Duration.ofNanos(System.nanoTime() - start);
                    this.logger.trace("Removed proxies in {}ms", timeToRemoveProxies.toMillis());

                    result = new Success(ChangeKind.SEMANTIC_CHANGE, Map.of(REMOVED_PROXIES_PARAMETER_KEY, removedProxies),
                            List.of(new Message("Library " + oldLibrary.getName() + " updated to version " + newLibrary.getVersion(), MessageLevel.SUCCESS)));
                } else {
                    result = new Failure(List.of(new Message("Cannot update Library " + newLibrary.getName() + ": the library is not a direct dependency", MessageLevel.ERROR)));
                }
            }
        }
        return result;
    }

    private Optional<Library> getLibraryDependencyWithDifferentVersion(EditingContext editingContext, Library library) {
        return new UUIDParser().parse(editingContext.getId())
                .flatMap(this.semanticDataSearchService::findById)
                .map(SemanticData::getDependencies)
                .orElse(List.of())
                .stream()
                .map(SemanticDataDependency::dependencySemanticDataId)
                .map(this.librarySearchService::findBySemanticData)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(libraryDependency -> Objects.equals(libraryDependency.getNamespace(), library.getNamespace())
                        && Objects.equals(libraryDependency.getName(), library.getName())
                        && !Objects.equals(libraryDependency.getVersion(), library.getVersion()))
                .findFirst();
    }

    private List<Resource> getAllResourcesFromLibraryRecursiveDependencies(EditingContext editingContext, Library library) {
        Set<Library> libraries = new LinkedHashSet<>();
        libraries.add(library);
        this.semanticDataSearchService.findAllDependenciesRecursivelyById(library.getSemanticData().getId()).stream()
                .map(SemanticData::getId)
                .map(AggregateReference::<SemanticData, UUID> to)
                .map(this.librarySearchService::findBySemanticData)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(libraries::add);
        List<Resource> libraryResources = new ArrayList<>();
        for (Resource resource : editingContext.getDomain().getResourceSet().getResources()) {
            resource.eAdapters().stream()
                    .filter(LibraryMetadataAdapter.class::isInstance)
                    .map(LibraryMetadataAdapter.class::cast)
                    .findFirst()
                    .ifPresent(libraryMetadata -> {
                        if (libraries.stream().anyMatch(libraryDependency -> Objects.equals(libraryMetadata.getNamespace(), libraryDependency.getNamespace())
                                && Objects.equals(libraryMetadata.getName(), libraryDependency.getName())
                                && Objects.equals(libraryMetadata.getVersion(), libraryDependency.getVersion()))) {
                            libraryResources.add(resource);
                        }
                    });
        }
        return libraryResources;
    }

    private void addLibraryDependency(ICause cause, EditingContext editingContext, Library library) {
        Optional<SemanticData> optionalSemanticData = new UUIDParser().parse(editingContext.getId())
                .flatMap(this.semanticDataSearchService::findById);
        if (optionalSemanticData.isPresent()) {
            if (optionalSemanticData.get().getDependencies().stream().anyMatch(dependency -> dependency.dependencySemanticDataId().equals(library.getSemanticData()))) {
                this.logger.warn("Cannot add the dependency to library " + library.getNamespace() + ":" + library.getName() + ":" + library.getVersion() + ": the dependency already exists");
            } else {
                this.semanticDataUpdateService.addDependencies(cause, AggregateReference.to(optionalSemanticData.get().getId()), List.of(library.getSemanticData()));
            }
        }
    }

    private void removeLibraryDependency(ICause cause, EditingContext editingContext, Library library) {
        new UUIDParser().parse(editingContext.getId())
                .flatMap(this.semanticDataSearchService::findById)
                .ifPresent(semanticData -> {
                    this.semanticDataUpdateService.removeDependencies(cause, AggregateReference.to(semanticData.getId()), List.of(library.getSemanticData()));
                });
    }

    /**
     * Removes proxies from non-library resources in the provided {@code resourceSet}.
     *
     * @param resourceSet
     *            the resource set
     * @return a {@link Map} containing the unresolved proxies from non-library resources in the provided
     *         {@code resourceSet}
     */
    private Map<EObject, Collection<Setting>> removeUnresolvedProxies(ResourceSet resourceSet) {
        List<Resource> nonLibraryResources = resourceSet.getResources().stream()
                .filter(resource -> this.editingContextPersistenceFilters.stream().allMatch(editingContextPersistenceFilter -> editingContextPersistenceFilter.shouldPersist(resource)))
                .toList();
        // Only look for proxies in Resources that are persisted in the project's semantic data
        Map<EObject, Collection<Setting>> unresolvedProxies = UnresolvedProxyCrossReferencer.find(nonLibraryResources);
        unresolvedProxies.forEach((proxyObject, settings) -> {
            for (Setting setting : settings) {
                if (!setting.getEStructuralFeature().isDerived()) {
                    if (setting.getEStructuralFeature().isMany()) {
                        List<?> value = (List<?>) setting.get(false);
                        value.remove(proxyObject);
                    } else {
                        setting.unset();
                    }
                }
            }
        });
        return unresolvedProxies;

    }
}

