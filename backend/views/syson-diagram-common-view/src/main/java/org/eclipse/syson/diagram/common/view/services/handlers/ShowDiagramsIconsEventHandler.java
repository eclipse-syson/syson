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
package org.eclipse.syson.diagram.common.view.services.handlers;

import java.util.Objects;

import org.eclipse.sirius.components.collaborative.api.ChangeDescription;
import org.eclipse.sirius.components.collaborative.api.ChangeKind;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramChangeKind;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramEventHandler;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramInput;
import org.eclipse.sirius.components.collaborative.diagrams.messages.ICollaborativeDiagramMessageService;
import org.eclipse.sirius.components.core.api.ErrorPayload;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.syson.diagram.common.view.services.ShowDiagramsIconsService;
import org.eclipse.syson.diagram.common.view.services.dto.ShowDiagramsIconsInput;
import org.eclipse.syson.diagram.common.view.services.dto.ShowDiagramsIconsSuccessPayload;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Sinks.Many;
import reactor.core.publisher.Sinks.One;

/**
 * Handle show diagrams icons event.
 *
 * @author arichard
 */
@Service
public class ShowDiagramsIconsEventHandler implements IDiagramEventHandler {

    private final ICollaborativeDiagramMessageService messageService;

    private final ShowDiagramsIconsService showDiagramsIconsService;

    public ShowDiagramsIconsEventHandler(ICollaborativeDiagramMessageService messageService, ShowDiagramsIconsService showDiagramsIconsService) {
        this.messageService = Objects.requireNonNull(messageService);
        this.showDiagramsIconsService = Objects.requireNonNull(showDiagramsIconsService);
    }

    @Override
    public boolean canHandle(IDiagramInput diagramInput) {
        return diagramInput instanceof ShowDiagramsIconsInput;
    }

    @Override
    public void handle(One<IPayload> payloadSink, Many<ChangeDescription> changeDescriptionSink, IEditingContext editingContext, IDiagramContext diagramContext, IDiagramInput diagramInput) {
        if (diagramInput instanceof ShowDiagramsIconsInput showDiagramsIconsInput) {
            this.showDiagramsIconsService.setShowIcons(showDiagramsIconsInput.show());
            IPayload payload = new ShowDiagramsIconsSuccessPayload(diagramInput.id(), showDiagramsIconsInput.show());
            ChangeDescription changeDescription = new ChangeDescription(DiagramChangeKind.DIAGRAM_ELEMENT_VISIBILITY_CHANGE, diagramInput.representationId(), diagramInput);
            payloadSink.tryEmitValue(payload);
            changeDescriptionSink.tryEmitNext(changeDescription);
        } else {
            String message = this.messageService.invalidInput(diagramInput.getClass().getSimpleName(), ShowDiagramsIconsInput.class.getSimpleName());
            IPayload payload = new ErrorPayload(diagramInput.id(), message);
            ChangeDescription changeDescription = new ChangeDescription(ChangeKind.NOTHING, diagramInput.representationId(), diagramInput);
            payloadSink.tryEmitValue(payload);
            changeDescriptionSink.tryEmitNext(changeDescription);
        }
    }
}
