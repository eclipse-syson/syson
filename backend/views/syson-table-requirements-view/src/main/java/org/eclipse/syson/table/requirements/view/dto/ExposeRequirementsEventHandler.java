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
package org.eclipse.syson.table.requirements.view.dto;

import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.components.collaborative.api.ChangeDescription;
import org.eclipse.sirius.components.collaborative.api.ChangeKind;
import org.eclipse.sirius.components.collaborative.api.IEditingContextEventHandler;
import org.eclipse.sirius.components.collaborative.api.IRepresentationSearchService;
import org.eclipse.sirius.components.collaborative.api.Monitoring;
import org.eclipse.sirius.components.collaborative.messages.ICollaborativeMessageService;
import org.eclipse.sirius.components.core.api.ErrorPayload;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IInput;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.sirius.components.tables.Table;
import org.eclipse.syson.sysml.RequirementUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.ViewUsage;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import reactor.core.publisher.Sinks.Many;
import reactor.core.publisher.Sinks.One;

/**
 * Event handler for ExposeRequirements mutation.
 *
 * @author arichard
 */
@Service
public class ExposeRequirementsEventHandler implements IEditingContextEventHandler {

    private final IObjectSearchService objectSearchService;

    private final IRepresentationSearchService representationSearchService;

    private final ICollaborativeMessageService messageService;

    private final Counter counter;

    public ExposeRequirementsEventHandler(IObjectSearchService objectSearchService, IRepresentationSearchService representationSearchService, ICollaborativeMessageService messageService,
            MeterRegistry meterRegistry) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.representationSearchService = Objects.requireNonNull(representationSearchService);
        this.messageService = Objects.requireNonNull(messageService);
        this.counter = Counter.builder(Monitoring.EVENT_HANDLER)
                .tag(Monitoring.NAME, this.getClass().getSimpleName())
                .register(meterRegistry);
    }

    @Override
    public boolean canHandle(IEditingContext editingContext, IInput input) {
        return input instanceof ExposeRequirementsInput;
    }

    @Override
    public void handle(One<IPayload> payloadSink, Many<ChangeDescription> changeDescriptionSink, IEditingContext editingContext, IInput input) {
        this.counter.increment();

        List<Message> messages = List.of(new Message(this.messageService.invalidInput(input.getClass().getSimpleName(), ExposeRequirementsInput.class.getSimpleName()),
                MessageLevel.ERROR));
        ChangeDescription changeDescription = new ChangeDescription(ChangeKind.NOTHING, editingContext.getId(), input);

        IPayload payload = null;

        if (input instanceof ExposeRequirementsInput exposeRequirementsInput) {
            var tableId = exposeRequirementsInput.tableId();
            var viewUsage = this.getViewUsage(editingContext, tableId);
            if (viewUsage != null) {
                this.exposeRequirements(editingContext, viewUsage);
                payload = new SuccessPayload(input.id(), messages);
                changeDescription = new ChangeDescription(ChangeKind.SEMANTIC_CHANGE, editingContext.getId(), input);
            }
        }

        if (payload == null) {
            payload = new ErrorPayload(input.id(), messages);
        }

        payloadSink.tryEmitValue(payload);
        changeDescriptionSink.tryEmitNext(changeDescription);
    }

    private ViewUsage getViewUsage(IEditingContext editingContext, String tableId) {
        var optTable = this.representationSearchService.findById(editingContext, tableId, Table.class);
        if (optTable.isPresent()) {
            var objectId = optTable.get().getTargetObjectId();
            var parentObject = this.objectSearchService.getObject(editingContext, objectId);
            if (parentObject.isPresent()) {
                var object = parentObject.get();
                if (object instanceof ViewUsage viewUsage) {
                    return viewUsage;
                }
            }
        }
        return null;
    }

    private void exposeRequirements(IEditingContext editingContext, ViewUsage viewUsage) {
        var owningNamespace = viewUsage.getOwningNamespace();
        owningNamespace.getOwnedMember().stream()
                .filter(RequirementUsage.class::isInstance)
                .map(RequirementUsage.class::cast)
                .forEach(req -> {
                    if (!viewUsage.getExposedElement().contains(req)) {
                        var membershipExpose = SysmlFactory.eINSTANCE.createMembershipExpose();
                        viewUsage.getOwnedRelationship().add(membershipExpose);
                        membershipExpose.setImportedMembership(req.getOwningMembership());
                    }
                });
    }
}
