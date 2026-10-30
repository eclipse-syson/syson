/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
package org.eclipse.syson.standard.diagrams.view.services;

import java.util.Objects;

import org.eclipse.sirius.web.projects.domain.events.ProjectCreatedEvent;
import org.eclipse.sirius.web.projects.stylecustomizations.domain.services.api.IProjectStyleCustomizationCreationService;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Enables the SysON theme by default for newly created projects.
 *
 * @author gcoutable
 */
@Service
public class SDVThemeInitializer {

    private final IProjectStyleCustomizationCreationService projectStyleCustomizationCreationService;

    public SDVThemeInitializer(IProjectStyleCustomizationCreationService projectStyleCustomizationCreationService) {
        this.projectStyleCustomizationCreationService = Objects.requireNonNull(projectStyleCustomizationCreationService);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void onProjectCreatedEvent(ProjectCreatedEvent projectCreatedEvent) {
        this.projectStyleCustomizationCreationService.createProjectStyleCustomization(projectCreatedEvent, AggregateReference.to(projectCreatedEvent.project().getId()), SysONStyleCustomizationDescriptionProvider.SYSON_STYLE_CUSTOMIZATION_DESCRIPTION_ID);
    }
}
