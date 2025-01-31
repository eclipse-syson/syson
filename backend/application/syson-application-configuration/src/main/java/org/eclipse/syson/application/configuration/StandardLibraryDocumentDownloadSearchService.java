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
package org.eclipse.syson.application.configuration;

import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.components.core.api.IEditingContextSearchService;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.web.application.document.services.api.IDocumentDownloadResourceSearchService;
import org.springframework.stereotype.Service;

/**
 * Used to provide a valid resource when searching for document download resource.
 *
 * @author arichard
 */
@Service
public class StandardLibraryDocumentDownloadSearchService implements IDocumentDownloadResourceSearchService {

    private final IEditingContextSearchService editingContextSearchService;

    public StandardLibraryDocumentDownloadSearchService(IEditingContextSearchService editingContextSearchService) {
        this.editingContextSearchService = Objects.requireNonNull(editingContextSearchService);
    }

    @Override
    public Optional<Resource> findResource(String editingContextId, String documentId) {
        return this.editingContextSearchService.findById(editingContextId)
                .filter(IEMFEditingContext.class::isInstance)
                .map(IEMFEditingContext.class::cast)
                .flatMap(editingContext -> {
                    return editingContext.getDomain().getResourceSet().getResources().stream()
                            .filter(resource -> resource.getURI().toString().contains(documentId))
                            .findFirst();
                });
    }
}
