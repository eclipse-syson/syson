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

import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.RepresentationMetadata;
import org.eclipse.sirius.web.domain.boundedcontexts.representationdata.services.api.IRepresentationMetadataSearchService;
import org.eclipse.sirius.web.domain.boundedcontexts.semanticdata.SemanticData;
import org.eclipse.sirius.web.domain.pagination.Window;
import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

/**
 * Mock implementation of {@link IRepresentationMetadataSearchService}.
 * 
 * @author dvojtise
 * @author flatombe
 */
public class IRepresentationMetadataSearchServiceNoOp implements IRepresentationMetadataSearchService {

    @Override
    public Optional<AggregateReference<SemanticData, UUID>> findSemanticDataByRepresentationId(UUID representationId) {
        return Optional.empty();
    }

    @Override
    public Optional<RepresentationMetadata> findMetadataById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<RepresentationMetadata> findAllRepresentationMetadataBySemanticDataAndTargetObjectId(AggregateReference<SemanticData, UUID> semanticData, String targetObjectId) {
        return null;
    }

    @Override
    public List<RepresentationMetadata> findAllRepresentationMetadataBySemanticData(AggregateReference<SemanticData, UUID> semanticData) {
        return null;
    }

    @Override
    public Window<RepresentationMetadata> findAllRepresentationMetadataBySemanticData(AggregateReference<SemanticData, UUID> semanticData, KeysetScrollPosition position, int limit) {
        return null;
    }

    @Override
    public boolean existsByIdAndKind(UUID id, List<String> kinds) {
        return false;
    }

    @Override
    public boolean existsById(UUID id) {
        return false;
    }

    @Override
    public boolean existAnyRepresentationMetadataForSemanticDataAndTargetObjectId(AggregateReference<SemanticData, UUID> semanticData, String targetObjectId) {
        return false;
    }
}
