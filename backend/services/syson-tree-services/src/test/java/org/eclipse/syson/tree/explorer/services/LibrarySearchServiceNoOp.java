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
package org.eclipse.syson.tree.explorer.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.sirius.web.domain.boundedcontexts.library.Library;
import org.eclipse.sirius.web.domain.boundedcontexts.library.services.api.ILibrarySearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.SemanticData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

/**
 * Mock implementation of {@link ILibrarySearchService} which does nothing.
 *
 * @author gdaniel
 */
public class LibrarySearchServiceNoOp implements ILibrarySearchService {

    @Override
    public Page<Library> findAll(Pageable pageable) {
        return new PageImpl<>(List.of());
    }

    @Override
    public boolean existsByNamespaceAndNameAndVersion(String namespace, String name, String version) {
        return false;
    }

    @Override
    public Optional<Library> findByNamespaceAndNameAndVersion(String namespace, String name, String version) {
        return Optional.empty();
    }

    @Override
    public Optional<Library> findBySemanticData(AggregateReference<SemanticData, UUID> semanticData) {
        return Optional.empty();
    }

    @Override
    public Page<Library> findAllByNamespaceAndName(String namespace, String name, Pageable pageable) {
        return new PageImpl<>(List.of());
    }

    @Override
    public List<Library> findAllById(Iterable<UUID> ids) {
        return List.of();
    }

    @Override
    public Optional<Library> findById(UUID id) {
        return Optional.empty();
    }

}
