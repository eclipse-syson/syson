/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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
package org.eclipse.syson.sysml.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.sirius.components.collaborative.api.ChangeDescription;
import org.eclipse.sirius.components.collaborative.api.ChangeKind;
import org.eclipse.sirius.components.collaborative.api.IEditingContextEventHandler;
import org.eclipse.sirius.components.collaborative.api.Monitoring;
import org.eclipse.sirius.components.collaborative.messages.ICollaborativeMessageService;
import org.eclipse.sirius.components.core.api.ErrorPayload;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IInput;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.dto.InsertTextualSysMLv2Input;
import org.eclipse.syson.sysml.services.api.ISysMLTextImporter;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import reactor.core.publisher.Sinks.Many;
import reactor.core.publisher.Sinks.One;

/**
 * Event handler for InsertTextualSysMLv2 mutation.
 *
 * @author arichard
 */
@Service
public class InsertTextualSysMLv2EventHandler implements IEditingContextEventHandler {

    private final IObjectSearchService objectSearchService;

    private final ISysMLTextImporter sysmlTextImporter;

    private final ICollaborativeMessageService messageService;

    private final Counter counter;

    public InsertTextualSysMLv2EventHandler(IObjectSearchService objectSearchService, ISysMLTextImporter sysmlTextImporter, ICollaborativeMessageService messageService, MeterRegistry meterRegistry) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.sysmlTextImporter = Objects.requireNonNull(sysmlTextImporter);
        this.messageService = Objects.requireNonNull(messageService);
        this.counter = Counter.builder(Monitoring.EVENT_HANDLER)
                .tag(Monitoring.NAME, this.getClass().getSimpleName())
                .register(meterRegistry);
    }

    @Override
    public boolean canHandle(IEditingContext editingContext, IInput input) {
        return input instanceof InsertTextualSysMLv2Input;
    }

    @Override
    public void handle(One<IPayload> payloadSink, Many<ChangeDescription> changeDescriptionSink, IEditingContext editingContext, IInput input) {
        this.counter.increment();

        List<Message> messages = List.of(new Message(this.messageService.invalidInput(input.getClass().getSimpleName(), InsertTextualSysMLv2Input.class.getSimpleName()),
                MessageLevel.ERROR));
        ChangeDescription changeDescription = new ChangeDescription(ChangeKind.NOTHING, editingContext.getId(), input);

        IPayload payload = null;

        if (input instanceof InsertTextualSysMLv2Input insertTextualInput && editingContext instanceof IEMFEditingContext emfEditingContext) {
            messages = new ArrayList<>();
            var optionalParentElement = this.resolveElement(emfEditingContext, insertTextualInput.objectId());
            if (optionalParentElement.isPresent()) {
                var parent = optionalParentElement.get();
                var textualContent = insertTextualInput.textualContent();

                var newObjects = this.sysmlTextImporter.importSysMLText(emfEditingContext, parent, textualContent, messages);
                if (!newObjects.isEmpty()) {
                    payload = new SuccessPayload(input.id(), messages);
                    changeDescription = new ChangeDescription(ChangeKind.SEMANTIC_CHANGE, editingContext.getId(), input);
                } else {
                    messages.add(new Message("Unable to convert the input into valid SysMLv2", MessageLevel.ERROR));
                }
            }
        }

        if (payload == null) {
            payload = new ErrorPayload(input.id(), messages);
        }

        payloadSink.tryEmitValue(payload);
        changeDescriptionSink.tryEmitNext(changeDescription);
    }

    private Optional<Element> resolveElement(IEMFEditingContext emfEditingContext, String objectId) {
        return this.objectSearchService.getObject(emfEditingContext, objectId)
                .filter(Element.class::isInstance)
                .map(Element.class::cast);
    }

}
