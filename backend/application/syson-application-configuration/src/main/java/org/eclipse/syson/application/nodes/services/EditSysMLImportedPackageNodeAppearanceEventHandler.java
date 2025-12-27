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
package org.eclipse.syson.application.nodes.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.sirius.components.collaborative.api.ChangeDescription;
import org.eclipse.sirius.components.collaborative.api.ChangeKind;
import org.eclipse.sirius.components.collaborative.api.Monitoring;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramChangeKind;
import org.eclipse.sirius.components.collaborative.diagrams.DiagramContext;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramEventHandler;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramInput;
import org.eclipse.sirius.components.collaborative.diagrams.api.IDiagramQueryService;
import org.eclipse.sirius.components.collaborative.diagrams.messages.ICollaborativeDiagramMessageService;
import org.eclipse.sirius.components.core.api.ErrorPayload;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.diagrams.Node;
import org.eclipse.sirius.components.diagrams.events.appearance.EditAppearanceEvent;
import org.eclipse.sirius.components.diagrams.events.appearance.IAppearanceChange;
import org.eclipse.sirius.components.diagrams.events.appearance.NodeBackgroundAppearanceChange;
import org.eclipse.sirius.components.diagrams.events.appearance.NodeBorderColorAppearanceChange;
import org.eclipse.sirius.components.diagrams.events.appearance.NodeBorderSizeAppearanceChange;
import org.eclipse.sirius.components.diagrams.events.appearance.NodeBorderStyleAppearanceChange;
import org.eclipse.syson.application.nodes.dto.EditSysMLImportedPackageNodeAppearanceInput;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import reactor.core.publisher.Sinks;

/**
 * Handle diagram events related to editing a SysMLImportedPackage node's appearance.
 *
 * @author arichard
 */
@Service
public class EditSysMLImportedPackageNodeAppearanceEventHandler implements IDiagramEventHandler {

    private final ICollaborativeDiagramMessageService messageService;

    private final IDiagramQueryService diagramQueryService;

    private final Counter counter;

    public EditSysMLImportedPackageNodeAppearanceEventHandler(ICollaborativeDiagramMessageService messageService, IDiagramQueryService diagramQueryService, MeterRegistry meterRegistry) {
        this.messageService = Objects.requireNonNull(messageService);
        this.diagramQueryService = Objects.requireNonNull(diagramQueryService);
        this.counter = Counter.builder(Monitoring.EVENT_HANDLER)
                .tag(Monitoring.NAME, this.getClass().getSimpleName())
                .register(meterRegistry);
    }

    @Override
    public boolean canHandle(IEditingContext editingContext, IDiagramInput diagramInput) {
        return diagramInput instanceof EditSysMLImportedPackageNodeAppearanceInput;
    }

    @Override
    public void handle(Sinks.One<IPayload> payloadSink, Sinks.Many<ChangeDescription> changeDescriptionSink, IEditingContext editingContext, DiagramContext diagramContext,
            IDiagramInput diagramInput) {
        this.counter.increment();

        String message = this.messageService.invalidInput(diagramInput.getClass().getSimpleName(), EditSysMLImportedPackageNodeAppearanceInput.class.getSimpleName());
        IPayload payload = new ErrorPayload(diagramInput.id(), message);
        ChangeDescription changeDescription = new ChangeDescription(ChangeKind.NOTHING, diagramInput.representationId(), diagramInput);

        if (diagramInput instanceof EditSysMLImportedPackageNodeAppearanceInput editAppearanceInput) {
            List<String> nodeIds = editAppearanceInput.nodeIds();
            List<IAppearanceChange> appearanceChanges = new ArrayList<>();
            List<String> elementNotFoundIds = new ArrayList<>();

            for (String nodeId : nodeIds) {
                Optional<Node> optionalNode = this.diagramQueryService.findNodeById(diagramContext.diagram(), nodeId);
                if (optionalNode.isPresent()) {
                    Optional.ofNullable(editAppearanceInput.appearance().background()).ifPresent(background -> appearanceChanges.add(new NodeBackgroundAppearanceChange(nodeId, background)));
                    Optional.ofNullable(editAppearanceInput.appearance().borderColor()).ifPresent(borderColor -> appearanceChanges.add(new NodeBorderColorAppearanceChange(nodeId, borderColor)));
                    Optional.ofNullable(editAppearanceInput.appearance().borderSize()).ifPresent(borderSize -> appearanceChanges.add(new NodeBorderSizeAppearanceChange(nodeId, borderSize)));
                    Optional.ofNullable(editAppearanceInput.appearance().borderStyle()).ifPresent(borderStyle -> appearanceChanges.add(new NodeBorderStyleAppearanceChange(nodeId, borderStyle)));
                } else {
                    elementNotFoundIds.add(nodeId);
                }
            }
            if (!elementNotFoundIds.isEmpty()) {
                String nodeNotFoundMessage = this.messageService.nodeNotFound(String.join(" - ", elementNotFoundIds));
                payload = new ErrorPayload(diagramInput.id(), nodeNotFoundMessage);
            } else {
                diagramContext.diagramEvents().add(new EditAppearanceEvent(appearanceChanges));
                payload = new SuccessPayload(diagramInput.id());
                changeDescription = new ChangeDescription(DiagramChangeKind.DIAGRAM_APPEARANCE_CHANGE, diagramInput.representationId(), diagramInput);
            }
        }

        payloadSink.tryEmitValue(payload);
        changeDescriptionSink.tryEmitNext(changeDescription);
    }
}
