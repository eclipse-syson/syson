/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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

package org.eclipse.syson.application.sysmlv2;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.emf.migration.api.IMigrationParticipant;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.web.application.document.dto.DocumentDTO;
import org.eclipse.sirius.web.application.document.services.api.IStereotypeHandler;
import org.eclipse.sirius.web.application.views.explorer.services.ExplorerDescriptionProvider;
import org.eclipse.syson.application.sysmlv2.api.IDefaultSysMLv2ResourceProvider;
import org.springframework.stereotype.Service;

/**
 * Used to create document from a SysMLV2 stereotype.
 *
 * @author gcoutable
 */
@Service
public class SysMLv2StereotypeHandler implements IStereotypeHandler {

    private final IDefaultSysMLv2ResourceProvider defaultSysMLv2ResourceProvider;

    public SysMLv2StereotypeHandler(List<IMigrationParticipant> migrationParticipants, IDefaultSysMLv2ResourceProvider defaultSysMLv2ResourceProvider) {
        this.defaultSysMLv2ResourceProvider = Objects.requireNonNull(defaultSysMLv2ResourceProvider);
    }

    @Override
    public boolean canHandle(IEditingContext editingContext, String stereotypeId) {
        return SysMLv2StereotypeProvider.EMPTY_SYSML_ID.equals(stereotypeId) || SysMLv2StereotypeProvider.EMPTY_ID.equals(stereotypeId);
    }

    @Override
    public Optional<DocumentDTO> handle(IEditingContext editingContext, String stereotypeId, String name) {
        if (editingContext instanceof IEMFEditingContext emfEditingContext) {
            return switch (stereotypeId) {
                case SysMLv2StereotypeProvider.EMPTY_SYSML_ID -> this.createEmptySysMLV2Document(emfEditingContext, name);
                case SysMLv2StereotypeProvider.EMPTY_ID -> this.createEmptyDocument(emfEditingContext, name);
                default -> Optional.empty();
            };
        }
        return Optional.empty();
    }

    private Optional<DocumentDTO> createEmptyDocument(IEMFEditingContext emfEditingContext, String name) {
        var documentId = UUID.randomUUID();
        var resource = this.defaultSysMLv2ResourceProvider.getEmptyResource(documentId, name);
        emfEditingContext.getDomain().getResourceSet().getResources().add(resource);

        return Optional.of(new DocumentDTO(documentId, name, ExplorerDescriptionProvider.DOCUMENT_KIND));
    }

    private Optional<DocumentDTO> createEmptySysMLV2Document(IEMFEditingContext emfEditingContext, String name) {
        var documentId = UUID.randomUUID();
        var resource = this.defaultSysMLv2ResourceProvider.getDefaultSysMLv2Resource(documentId, name);
        emfEditingContext.getDomain().getResourceSet().getResources().add(resource);

        return Optional.of(new DocumentDTO(documentId, name, ExplorerDescriptionProvider.DOCUMENT_KIND));
    }
}
