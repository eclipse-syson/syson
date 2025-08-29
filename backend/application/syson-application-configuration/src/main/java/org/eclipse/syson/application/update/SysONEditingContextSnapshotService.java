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

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.web.application.editingcontext.EditingContext;
import org.eclipse.sirius.web.application.editingcontext.services.DocumentData;
import org.eclipse.sirius.web.application.editingcontext.services.EditingContextSnapshot;
import org.eclipse.sirius.web.application.editingcontext.services.api.IEditingContextSnapshot;
import org.eclipse.sirius.web.application.editingcontext.services.api.IEditingContextSnapshotService;
import org.eclipse.sirius.web.application.editingcontext.services.api.IResourceLoader;
import org.eclipse.sirius.web.application.editingcontext.services.api.IResourceToDocumentService;
import org.eclipse.sirius.web.application.library.services.LibraryMetadataAdapter;
import org.eclipse.syson.application.configuration.SysMLEditingContextProcessor;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Creates and restores snapshots of SysON editing contexts.
 *
 * @author gdaniel
 */
@Service
// This class should be removed once https://github.com/eclipse-sirius/sirius-web/issues/5408 is fixed.
@Primary
public class SysONEditingContextSnapshotService implements IEditingContextSnapshotService {

    private final IResourceToDocumentService resourceToDocumentService;

    private final IResourceLoader resourceLoader;

    private final SysMLEditingContextProcessor sysmlEditingContextProcessor;

    public SysONEditingContextSnapshotService(IResourceToDocumentService resourceToDocumentService, IResourceLoader resourceLoader, SysMLEditingContextProcessor sysmlEditingContextProcessor) {
        this.resourceToDocumentService = Objects.requireNonNull(resourceToDocumentService);
        this.resourceLoader = Objects.requireNonNull(resourceLoader);
        this.sysmlEditingContextProcessor = Objects.requireNonNull(sysmlEditingContextProcessor);
    }

    @Override
    public Optional<IEditingContextSnapshot> createSnapshot(IEditingContext editingContext) {
        Optional<IEditingContextSnapshot> result = Optional.empty();
        if (editingContext instanceof EditingContext siriusWebEditingContext) {
            ResourceSet resourceSet = siriusWebEditingContext.getDomain().getResourceSet();

            var libraryAdapterResourcesMap = resourceSet.getResources().stream()
                    .filter(resource -> resource.eAdapters().stream()
                            .anyMatch(LibraryMetadataAdapter.class::isInstance))
                    .collect(Collectors.toMap(
                            Resource::getURI,
                            resource -> resource.eAdapters().stream()
                                    .filter(LibraryMetadataAdapter.class::isInstance)
                                    .findFirst()
                                    .orElseThrow(() -> new IllegalStateException("LibraryMetadataAdapter not found"))));

            List<DocumentData> snapshotDocuments = resourceSet.getResources().stream()
                    .filter(resource -> !ElementUtil.isStandardLibraryResource(resource))
                    .map(resource -> this.resourceToDocumentService.toDocument(resource, false))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();

            result = Optional.of(new EditingContextSnapshot(snapshotDocuments, libraryAdapterResourcesMap));
        }
        return result;
    }

    @Override
    public void restoreSnapshot(IEditingContext editingContext, IEditingContextSnapshot snapshot) {
        if (editingContext instanceof EditingContext siriusWebEditingContext && snapshot instanceof EditingContextSnapshot siriusWebSnapshot) {
            ResourceSet resourceSet = siriusWebEditingContext.getDomain().getResourceSet();
            resourceSet.getResources().clear();

            this.sysmlEditingContextProcessor.preProcess(siriusWebEditingContext);

            for (var documentSnapshot : siriusWebSnapshot.documents()) {
                var optionalResource = this.resourceLoader.toResource(resourceSet, documentSnapshot.document().getId().toString(), documentSnapshot.document().getName(),
                        documentSnapshot.document().getContent(), false);
                optionalResource.filter(resource -> siriusWebSnapshot.libraryAdapterResourcesMap().containsKey(resource.getURI()))
                        .ifPresent(resource -> resource.eAdapters().add(siriusWebSnapshot.libraryAdapterResourcesMap().get(resource.getURI())));
            }
        }
    }

}
