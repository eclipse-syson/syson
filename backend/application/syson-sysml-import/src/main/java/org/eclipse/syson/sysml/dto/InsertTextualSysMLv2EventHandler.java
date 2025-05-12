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
package org.eclipse.syson.sysml.dto;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Objects;

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
import org.eclipse.syson.sysml.ASTTransformer;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.SysmlToAst;
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

    private final ICollaborativeMessageService messageService;

    private final SysmlToAst sysmlToAst;

    private final Counter counter;

    public InsertTextualSysMLv2EventHandler(IObjectSearchService objectSearchService, ICollaborativeMessageService messageService,
            SysmlToAst sysmlToAst, MeterRegistry meterRegistry) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.messageService = Objects.requireNonNull(messageService);
        this.sysmlToAst = Objects.requireNonNull(sysmlToAst);
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
            var parentObjectId = insertTextualInput.objectId();
            var parentElement = this.getParentElement(parentObjectId, emfEditingContext);
            if (parentElement != null) {
                var tranformer = new ASTTransformer();
                var newObjects = this.convert(insertTextualInput, emfEditingContext, tranformer, parentElement);
                if (!newObjects.isEmpty()) {
                    messages = tranformer.getTransformationMessages();
                    payload = new SuccessPayload(input.id(), messages);
                    changeDescription = new ChangeDescription(ChangeKind.SEMANTIC_CHANGE, editingContext.getId(), input);
                } else {
                    if (!tranformer.getTransformationMessages().isEmpty()) {
                        messages = tranformer.getTransformationMessages();
                    } else {
                        messages = List.of(new Message("Unable to convert the input into valid SysMLv2", MessageLevel.ERROR));
                    }
                }
            }
        }

        if (payload == null) {
            payload = new ErrorPayload(input.id(), messages);
        }

        payloadSink.tryEmitValue(payload);
        changeDescriptionSink.tryEmitNext(changeDescription);
    }

    private Element getParentElement(String parentObjectId, IEMFEditingContext emfEditingContext) {
        var parentObject = this.objectSearchService.getObject(emfEditingContext, parentObjectId);
        if (parentObject.isPresent()) {
            var object = parentObject.get();
            if (object instanceof Element parentElement) {
                return parentElement;
            }
        }
        return null;
    }

    private List<Element> convert(InsertTextualSysMLv2Input insertTextualInput, IEMFEditingContext emfEditingContext, ASTTransformer tranformer, Element parentElement) {
        var textualContent = insertTextualInput.textualContent();
        var resourceSet = emfEditingContext.getDomain().getResourceSet();
        var inputStream = new ByteArrayInputStream(textualContent.getBytes());
        var astStream = this.sysmlToAst.convert(inputStream, ".sysml");
        var newObjects = tranformer.convertToElements(astStream, resourceSet, parentElement);
        return newObjects;
    }
}
