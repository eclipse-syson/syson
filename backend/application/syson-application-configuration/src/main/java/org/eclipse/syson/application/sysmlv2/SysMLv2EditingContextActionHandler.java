/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.sirius.components.collaborative.api.ChangeKind;
import org.eclipse.sirius.components.collaborative.api.IEditingContextActionHandler;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.emf.migration.api.IMigrationParticipant;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.representations.Failure;
import org.eclipse.sirius.components.representations.IStatus;
import org.eclipse.sirius.components.representations.Success;
import org.eclipse.syson.application.sysmlv2.api.IDefaultSysMLv2ResourceProvider;
import org.springframework.stereotype.Service;

/**
 * Handler used to perform an action on the editingContext.
 *
 * @author arichard
 */
@Service
public class SysMLv2EditingContextActionHandler implements IEditingContextActionHandler {

    private static final List<String> HANDLED_ACTIONS = List.of(SysMLv2StereotypeProvider.EMPTY_SYSML_ID, SysMLv2StereotypeProvider.EMPTY_SYSML_LIBRARY_ID);

    private final IDefaultSysMLv2ResourceProvider defaultSysMLv2ResourceProvider;

    public SysMLv2EditingContextActionHandler(List<IMigrationParticipant> migrationParticipants, IDefaultSysMLv2ResourceProvider defaultSysMLv2ResourceProvider) {
        this.defaultSysMLv2ResourceProvider = Objects.requireNonNull(defaultSysMLv2ResourceProvider);
    }

    @Override
    public boolean canHandle(IEditingContext editingContext, String actionId) {
        return HANDLED_ACTIONS.contains(actionId);
    }

    @Override
    public IStatus handle(IEditingContext editingContext, String actionId) {
        return Optional.of(editingContext)
                .filter(IEMFEditingContext.class::isInstance)
                .map(IEMFEditingContext.class::cast)
                .map(IEMFEditingContext::getDomain)
                .map(AdapterFactoryEditingDomain::getResourceSet)
                .map(resourceSet -> this.performActionOnResourceSet(resourceSet, actionId))
                .orElse(new Failure("Something went wrong while handling this action."));
    }

    private IStatus performActionOnResourceSet(ResourceSet resourceSet, String actionId) {
        return switch (actionId) {
            case SysMLv2StereotypeProvider.EMPTY_SYSML_ID -> this.createResourceAndReturnSuccess(resourceSet, this::createEmptySysMLResource);
            case SysMLv2StereotypeProvider.EMPTY_SYSML_LIBRARY_ID -> this.createResourceAndReturnSuccess(resourceSet, this::createEmptySysMLLibraryResource);
            default -> new Failure("Unknown action.");
        };
    }

    private IStatus createResourceAndReturnSuccess(ResourceSet resourceSet, Consumer<ResourceSet> createResource) {
        createResource.accept(resourceSet);
        return new Success(ChangeKind.SEMANTIC_CHANGE, Map.of());
    }

    private void createEmptySysMLResource(ResourceSet resourceSet) {
        var resource = this.defaultSysMLv2ResourceProvider.getDefaultSysMLv2Resource(UUID.randomUUID(), SysMLv2StereotypeProvider.EMPTY_SYSML_LABEL + ".sysml");
        resourceSet.getResources().add(resource);
    }

    private void createEmptySysMLLibraryResource(ResourceSet resourceSet) {
        var resource = this.defaultSysMLv2ResourceProvider.getDefaultSysMLv2LibraryResource(UUID.randomUUID(), SysMLv2StereotypeProvider.EMPTY_SYSML_LIBRARY_LABEL + ".sysml");
        resourceSet.getResources().add(resource);
    }
}
