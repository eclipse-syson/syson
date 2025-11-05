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

import org.eclipse.sirius.components.collaborative.api.IRepresentationMetadataPersistenceService;
import org.eclipse.sirius.components.collaborative.api.IRepresentationPersistenceService;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramCreationService;
import org.eclipse.sirius.components.core.api.IRepresentationDescriptionSearchService;
import org.springframework.stereotype.Service;

/**
 * A wrapper around services used by {@link SysMLv2EditService}.
 *
 * @author flatombe
 */
@Service
record SysMLv2EditServiceExtraServices(IDiagramCreationService diagramCreationService, IRepresentationDescriptionSearchService representationDescriptionSearchService,
        IRepresentationMetadataPersistenceService representationMetadataPersistenceService, IRepresentationPersistenceService representationPersistenceService) {

    public SysMLv2EditServiceExtraServices {
        Objects.requireNonNull(diagramCreationService);
        Objects.requireNonNull(representationDescriptionSearchService);
        Objects.requireNonNull(representationMetadataPersistenceService);
        Objects.requireNonNull(representationPersistenceService);
    }
}
