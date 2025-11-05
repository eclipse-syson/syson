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
package org.eclipse.syson.application.services;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.emf.ResourceMetadataAdapter;
import org.eclipse.sirius.web.application.UUIDParser;
import org.eclipse.sirius.web.application.library.services.LibraryMetadataAdapter;
import org.eclipse.sirius.web.domain.boundedcontexts.library.Library;
import org.eclipse.sirius.web.domain.boundedcontexts.library.services.api.ILibrarySearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.SemanticData;
import org.eclipse.syson.services.api.ISysONResourceService;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;

/**
 * The default SysON implementation for {@link ISysONResourceService}.
 *
 * @author flatombe
 */
@Service
public class SysONResourceService implements ISysONResourceService {

    private final ILibrarySearchService librarySearchService;

    public SysONResourceService(ILibrarySearchService librarySearchService) {
        this.librarySearchService = Objects.requireNonNull(librarySearchService);
    }

    @Override
    public boolean isSysML(Resource resource) {
        final boolean isSysMLResource = resource.eAdapters()
                .stream()
                .filter(ResourceMetadataAdapter.class::isInstance)
                .map(ResourceMetadataAdapter.class::cast)
                .findFirst()
                .map(ResourceMetadataAdapter::getName)
                .filter(name -> name.toLowerCase().endsWith(".sysml"))
                .isPresent();
        return isSysMLResource;
    }

    @Override
    public boolean isImported(final IEditingContext editingContext, final Resource resource) {
        Objects.requireNonNull(resource);

        return ElementUtil.isImported(resource) ||
                this.isFromReferencedLibrary(editingContext, resource);
    }

    @Override
    public boolean isFromReferencedLibrary(final IEditingContext editingContext, final Resource resource) {
        Objects.requireNonNull(editingContext);
        Objects.requireNonNull(resource);

        boolean result = false;

        Optional<Library> optionalEditingContextLibrary = new UUIDParser().parse(editingContext.getId())
                .map(AggregateReference::<SemanticData, UUID> to)
                .flatMap(this.librarySearchService::findBySemanticData);
        if (optionalEditingContextLibrary.isPresent()) {
            // The library is not from a referenced library if it is the library defined in the editing context.
            // This happens when visualizing a library: the content of the library is considered as a library by Sirius
            // Web but it isn't imported from anywhere, it is defined in the current editing context.
            Library editingContextLibrary = optionalEditingContextLibrary.get();
            result = resource.eAdapters().stream()
                    .filter(LibraryMetadataAdapter.class::isInstance)
                    .map(LibraryMetadataAdapter.class::cast)
                    .findFirst()
                    .map(libraryAdapter -> !this.isLibrary(libraryAdapter, editingContextLibrary))
                    .orElse(false);
        } else {
            result = resource.eAdapters().stream()
                    .anyMatch(LibraryMetadataAdapter.class::isInstance);
        }

        return result;
    }

    private boolean isLibrary(LibraryMetadataAdapter libraryMetadataAdapter, Library library) {
        return Objects.equals(libraryMetadataAdapter.getNamespace(), library.getNamespace())
                && Objects.equals(libraryMetadataAdapter.getName(), library.getName())
                && Objects.equals(libraryMetadataAdapter.getVersion(), library.getVersion());
    }

}
