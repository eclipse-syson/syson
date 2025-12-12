/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
package org.eclipse.syson.diagram.common.view.services.handlers;

import java.util.Objects;

import org.eclipse.sirius.components.collaborative.api.ChangeDescription;
import org.eclipse.sirius.components.collaborative.api.ChangeKind;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramChangeKind;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramEventHandler;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramInput;
import org.eclipse.sirius.components.collaborative.diagrams.messages.ICollaborativeDiagramMessageService;
import org.eclipse.sirius.components.core.api.ErrorPayload;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.syson.diagram.common.view.services.ShowDiagramsInheritedMembersService;
import org.eclipse.syson.diagram.common.view.services.dto.ShowDiagramsInheritedMembersFromStandardLibrariesInput;
import org.eclipse.syson.diagram.common.view.services.dto.ShowDiagramsInheritedMembersFromStandardLibrariesSuccessPayload;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Sinks.Many;
import reactor.core.publisher.Sinks.One;

/**
 * Handle show diagrams inherited members from standard libraries event.
 *
 * @author arichard
 */
@Service
public class ShowDiagramsInheritedMembersFromStandardLibrariesEventHandler implements IDiagramEventHandler {

    private final ICollaborativeDiagramMessageService messageService;

    private final ShowDiagramsInheritedMembersService showDiagramsInheritedMembersService;

    public ShowDiagramsInheritedMembersFromStandardLibrariesEventHandler(ICollaborativeDiagramMessageService messageService, ShowDiagramsInheritedMembersService showDiagramsInheritedMembersService) {
        this.messageService = Objects.requireNonNull(messageService);
        this.showDiagramsInheritedMembersService = Objects.requireNonNull(showDiagramsInheritedMembersService);
    }

    @Override
    public boolean canHandle(IEditingContext editingContext, IDiagramInput diagramInput) {
        return diagramInput instanceof ShowDiagramsInheritedMembersFromStandardLibrariesInput;
    }

    @Override
    public void handle(One<IPayload> payloadSink, Many<ChangeDescription> changeDescriptionSink, IEditingContext editingContext, DiagramContext diagramContext, IDiagramInput diagramInput) {
        if (diagramInput instanceof ShowDiagramsInheritedMembersFromStandardLibrariesInput showDiagramsInheritedMembersFromStandardLibrariesInput) {
            this.showDiagramsInheritedMembersService.setShowInheritedMembersFromStandardLibraries(showDiagramsInheritedMembersFromStandardLibrariesInput.show());
            IPayload payload = new ShowDiagramsInheritedMembersFromStandardLibrariesSuccessPayload(diagramInput.id(), showDiagramsInheritedMembersFromStandardLibrariesInput.show());
            ChangeDescription changeDescription = new ChangeDescription(DiagramChangeKind.DIAGRAM_ELEMENT_VISIBILITY_CHANGE, diagramInput.representationId(), diagramInput);
            payloadSink.tryEmitValue(payload);
            changeDescriptionSink.tryEmitNext(changeDescription);
        } else {
            String message = this.messageService.invalidInput(diagramInput.getClass().getSimpleName(), ShowDiagramsInheritedMembersFromStandardLibrariesInput.class.getSimpleName());
            IPayload payload = new ErrorPayload(diagramInput.id(), message);
            ChangeDescription changeDescription = new ChangeDescription(ChangeKind.NOTHING, diagramInput.representationId(), diagramInput);
            payloadSink.tryEmitValue(payload);
            changeDescriptionSink.tryEmitNext(changeDescription);
        }
    }
}
