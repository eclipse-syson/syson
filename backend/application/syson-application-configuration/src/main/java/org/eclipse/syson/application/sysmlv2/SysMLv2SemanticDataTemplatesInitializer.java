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

import java.util.Objects;
import java.util.UUID;

import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IEditingContextPersistenceService;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.events.ICause;
import org.eclipse.sirius.web.application.project.services.api.ISemanticDataInitializer;
import org.eclipse.syson.application.sysmlv2.api.IDefaultSysMLv2ResourceProvider;
import org.springframework.stereotype.Service;

/**
 * Provides SysMLv2-specific project templates initializers.
 *
 * @author arichard
 */
@Service
public class SysMLv2SemanticDataTemplatesInitializer implements ISemanticDataInitializer {

    private static final String SYSMLV2_DOCUMENT_NAME = "SysMLv2.sysml";

    private static final String SYSMLV2_LIBRARY_DOCUMENT_NAME = "SysMLv2-Library.sysml";

    private static final String BATMOBILE_DOCUMENT_NAME = "Batmobile.sysml";

    private final IEditingContextPersistenceService editingContextPersistenceService;

    private final IDefaultSysMLv2ResourceProvider defaultSysMLv2ResourceProvider;

    public SysMLv2SemanticDataTemplatesInitializer(IEditingContextPersistenceService editingContextPersistenceService, IDefaultSysMLv2ResourceProvider defaultSysMLv2ResourceProvider) {
        this.editingContextPersistenceService = Objects.requireNonNull(editingContextPersistenceService);
        this.defaultSysMLv2ResourceProvider = Objects.requireNonNull(defaultSysMLv2ResourceProvider);
    }

    @Override
    public boolean canHandle(String templateId) {
        return SysMLv2ProjectTemplatesProvider.SYSMLV2_TEMPLATE_ID.equals(templateId) || SysMLv2ProjectTemplatesProvider.SYSMLV2_LIBRARY_TEMPLATE_ID.equals(templateId)
                || SysMLv2ProjectTemplatesProvider.BATMOBILE_TEMPLATE_ID.equals(templateId);
    }

    @Override
    public void handle(ICause cause, IEditingContext editingContext, String projectTemplateId) {
        if (SysMLv2ProjectTemplatesProvider.SYSMLV2_TEMPLATE_ID.equals(projectTemplateId) && editingContext instanceof IEMFEditingContext emfEditingContext) {
            this.initializeSysMLv2Project(cause, emfEditingContext);
        } else if (SysMLv2ProjectTemplatesProvider.SYSMLV2_LIBRARY_TEMPLATE_ID.equals(projectTemplateId) && editingContext instanceof IEMFEditingContext emfEditingContext) {
            this.initializeSysMLv2LibraryProject(cause, emfEditingContext);
        } else if (SysMLv2ProjectTemplatesProvider.BATMOBILE_TEMPLATE_ID.equals(projectTemplateId) && editingContext instanceof IEMFEditingContext emfEditingContext) {
            this.initializeBatmobileProject(cause, emfEditingContext);
        }
    }

    private void initializeSysMLv2Project(ICause cause, IEMFEditingContext emfEditingContext) {
        var resourceSet = emfEditingContext.getDomain().getResourceSet();
        var resource = this.defaultSysMLv2ResourceProvider.getDefaultSysMLv2Resource(UUID.randomUUID(), SYSMLV2_DOCUMENT_NAME);
        resourceSet.getResources().add(resource);
        this.editingContextPersistenceService.persist(new SysMLv2TemplatesInitialization(UUID.randomUUID(), emfEditingContext, resource, cause), emfEditingContext);
    }

    private void initializeSysMLv2LibraryProject(ICause cause, IEMFEditingContext emfEditingContext) {
        var resourceSet = emfEditingContext.getDomain().getResourceSet();
        var resource = this.defaultSysMLv2ResourceProvider.getDefaultSysMLv2LibraryResource(UUID.randomUUID(), SYSMLV2_LIBRARY_DOCUMENT_NAME);
        resourceSet.getResources().add(resource);
        this.editingContextPersistenceService.persist(new SysMLv2TemplatesInitialization(UUID.randomUUID(), emfEditingContext, resource, cause), emfEditingContext);
    }

    private void initializeBatmobileProject(ICause cause, IEMFEditingContext emfEditingContext) {
        var resourceSet = emfEditingContext.getDomain().getResourceSet();
        var resource = this.defaultSysMLv2ResourceProvider.getEmptyResource(UUID.randomUUID(), BATMOBILE_DOCUMENT_NAME);
        resourceSet.getResources().add(resource);
        // Load after adding the resource to the resourceSet, to be sure that references will be resolved.
        this.defaultSysMLv2ResourceProvider.loadBatmobileResource(resource);
        this.editingContextPersistenceService.persist(new SysMLv2TemplatesInitialization(UUID.randomUUID(), emfEditingContext, resource, cause), emfEditingContext);
    }
}
