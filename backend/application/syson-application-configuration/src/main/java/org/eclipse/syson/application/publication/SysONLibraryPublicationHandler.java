/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
package org.eclipse.syson.application.publication;

import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.core.api.ErrorPayload;
import org.eclipse.sirius.components.core.api.IEditingContextSearchService;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.sirius.web.application.library.dto.PublishLibrariesInput;
import org.eclipse.sirius.web.application.library.services.api.ILibraryApplicationService;
import org.eclipse.sirius.web.application.library.services.api.ILibraryPublicationHandler;
import org.eclipse.sirius.web.application.project.services.api.IProjectEditingContextService;
import org.eclipse.sirius.web.domain.boundedcontexts.project.services.api.IProjectSearchService;
import org.eclipse.syson.application.publication.api.ISysMLLibraryPublisher;
import org.springframework.stereotype.Service;

/**
 * {@link ILibraryPublicationHandler} for publishing libraries in SysON.
 *
 * @see ILibraryApplicationService
 * @see SysONLibraryPublicationListener
 * @author flatombe
 */
@Service
public class SysONLibraryPublicationHandler implements ILibraryPublicationHandler {


    private final IEditingContextSearchService editingContextSearchService;

    private final IProjectEditingContextService projectEditingContextService;

    private final IProjectSearchService projectSearchService;

    private final ISysMLLibraryPublisher sysONSysMLLibraryPublisher;

    public SysONLibraryPublicationHandler(final IEditingContextSearchService editingContextSearchService,
            final IProjectEditingContextService projectEditingContextService,
            final IProjectSearchService projectSearchService,
            final ISysMLLibraryPublisher sysONSysMLLibraryPublisher) {
        this.editingContextSearchService = Objects.requireNonNull(editingContextSearchService);
        this.projectEditingContextService = Objects.requireNonNull(projectEditingContextService);
        this.projectSearchService = Objects.requireNonNull(projectSearchService);
        this.sysONSysMLLibraryPublisher = Objects.requireNonNull(sysONSysMLLibraryPublisher);
    }

    @Override
    public boolean canHandle(final PublishLibrariesInput input) {
        return Objects.equals(input.publicationKind(), "Project_SysML_AllProperContents");
    }

    @Override
    public IPayload handle(final PublishLibrariesInput input) {

        IPayload payload = null;
        var editingContextId = input.editingContextId();
        var optionalProjectId = this.projectEditingContextService.getProjectId(editingContextId);
        if (optionalProjectId.isPresent()) {
            var projectId = optionalProjectId.get();
            var optionalProject = this.projectSearchService.findById(projectId);
            var optionalEditingContext = this.editingContextSearchService.findById(editingContextId)
                    .filter(IEMFEditingContext.class::isInstance)
                    .map(IEMFEditingContext.class::cast);

            if (optionalProject.isPresent() && optionalEditingContext.isPresent()) {
                payload = this.sysONSysMLLibraryPublisher.publish(
                        input,
                        optionalEditingContext.get(),
                        projectId,
                        optionalProject.get().getName(),
                        input.version(),
                        input.description());
            } else {
                payload = new ErrorPayload(input.id(), List.of(new Message("Could not find project with following editingContextId '%s'.".formatted(editingContextId), MessageLevel.ERROR)));
            }
        } else {
            payload = new ErrorPayload(input.id(), List.of(new Message("Could not find project with following editingContextId '%s'.".formatted(editingContextId), MessageLevel.ERROR)));
        }
        return payload;
    }
}
