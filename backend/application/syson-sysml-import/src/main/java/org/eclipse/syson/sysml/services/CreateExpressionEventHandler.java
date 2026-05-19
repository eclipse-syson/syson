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
package org.eclipse.syson.sysml.services;

import java.util.Objects;
import java.util.Optional;

import org.eclipse.sirius.components.collaborative.api.ChangeDescription;
import org.eclipse.sirius.components.collaborative.api.ChangeKind;
import org.eclipse.sirius.components.collaborative.api.IEditingContextEventHandler;
import org.eclipse.sirius.components.collaborative.api.Monitoring;
import org.eclipse.sirius.components.collaborative.messages.ICollaborativeMessageService;
import org.eclipse.sirius.components.core.api.ErrorPayload;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IIdentityService;
import org.eclipse.sirius.components.core.api.IInput;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.dto.CreateExpressionInput;
import org.eclipse.syson.sysml.dto.CreateExpressionSuccessPayload;
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;
import org.eclipse.syson.sysml.services.api.ISysMLExpressionEditor;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import reactor.core.publisher.Sinks.Many;
import reactor.core.publisher.Sinks.One;

/**
 * Handler used to execute the {@code createExpression} operation.
 *
 * @author pcdavid
 */
@Service
public class CreateExpressionEventHandler implements IEditingContextEventHandler {

    private final IObjectSearchService objectSearchService;

    private final IIdentityService identityService;

    private final ISysMLExpressionEditor expressionEditor;

    private final MetamodelQueryElementService metamodelQueryElementService;

    private final ICollaborativeMessageService messageService;

    private final Counter counter;

    public CreateExpressionEventHandler(IObjectSearchService objectSearchService, IIdentityService identityService, ISysMLExpressionEditor expressionEditor,
            ICollaborativeMessageService messageService, MeterRegistry meterRegistry) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.identityService = Objects.requireNonNull(identityService);
        this.expressionEditor = Objects.requireNonNull(expressionEditor);
        this.metamodelQueryElementService = new MetamodelQueryElementService();
        this.messageService = Objects.requireNonNull(messageService);
        this.counter = Counter.builder(Monitoring.EVENT_HANDLER)
                .tag(Monitoring.NAME, this.getClass().getSimpleName())
                .register(meterRegistry);
    }

    @Override
    public boolean canHandle(IEditingContext editingContext, IInput input) {
        return editingContext instanceof IEMFEditingContext && input instanceof CreateExpressionInput;
    }

    @Override
    public void handle(One<IPayload> payloadSink, Many<ChangeDescription> changeDescriptionSink, IEditingContext editingContext, IInput input) {
        this.counter.increment();
        IPayload payload;
        ChangeDescription changeDescription = new ChangeDescription(ChangeKind.NOTHING, editingContext.getId(), input);

        if (input instanceof CreateExpressionInput createExpressionInput && editingContext instanceof IEMFEditingContext emfEditingContext) {
            Optional<Element> optionalParentElement = this.objectSearchService.getObject(editingContext, createExpressionInput.parentElementId())
                    .filter(Element.class::isInstance)
                    .map(Element.class::cast);
            if (optionalParentElement.isPresent()) {
                if (this.metamodelQueryElementService.hasSingleExpressionDefinition(optionalParentElement.get())) {
                    payload = new ErrorPayload(input.id(), "The parent element already has an expression");
                } else {
                    var result = this.expressionEditor.createExpression(emfEditingContext, optionalParentElement.get(), createExpressionInput.expressionText());
                    if (result.createdExpression() != null) {
                        var createdExpression = result.createdExpression();
                        var createdExpressionId = this.identityService.getId(createdExpression);
                        changeDescription = new ChangeDescription(ChangeKind.SEMANTIC_CHANGE, editingContext.getId(), input);
                        payload = new CreateExpressionSuccessPayload(input.id(), createdExpressionId, result.messages());
                    } else {
                        payload = new ErrorPayload(input.id(), result.messages());
                    }
                }
            } else {
                payload = new ErrorPayload(input.id(), this.messageService.notFound());
            }
        } else {
            payload = new ErrorPayload(input.id(), this.messageService.invalidInput(CreateExpressionInput.class.getName(), input.getClass().getName()));
        }

        payloadSink.tryEmitValue(payload);
        changeDescriptionSink.tryEmitNext(changeDescription);
    }
}
