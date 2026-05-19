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
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.dto.EditExpressionInput;
import org.eclipse.syson.sysml.dto.EditExpressionSuccessPayload;
import org.eclipse.syson.sysml.services.api.ISysMLExpressionEditor;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import reactor.core.publisher.Sinks;

/**
 * Event handler for the {@code editExpression} mutation.
 *
 * @author pcdavid
 */
@Service
public class EditExpressionEventHandler implements IEditingContextEventHandler {

    private final IObjectSearchService objectSearchService;

    private final IIdentityService identityService;

    private final ICollaborativeMessageService messageService;

    private final ISysMLExpressionEditor expressionEditor;

    private final Counter counter;

    public EditExpressionEventHandler(IObjectSearchService objectSearchService, IIdentityService identityService, ICollaborativeMessageService messageService, ISysMLExpressionEditor expressionEditor,
            MeterRegistry meterRegistry) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.identityService = Objects.requireNonNull(identityService);
        this.messageService = Objects.requireNonNull(messageService);
        this.expressionEditor = Objects.requireNonNull(expressionEditor);
        this.counter = Counter.builder(Monitoring.EVENT_HANDLER)
                .tag(Monitoring.NAME, this.getClass().getSimpleName())
                .register(meterRegistry);
    }

    @Override
    public boolean canHandle(IEditingContext editingContext, IInput input) {
        return editingContext instanceof IEMFEditingContext && input instanceof EditExpressionInput;
    }

    @Override
    public void handle(Sinks.One<IPayload> payloadSink, Sinks.Many<ChangeDescription> changeDescriptionSink, IEditingContext editingContext, IInput input) {
        this.counter.increment();
        IPayload payload;
        ChangeDescription changeDescription = new ChangeDescription(ChangeKind.NOTHING, editingContext.getId(), input);

        if (input instanceof EditExpressionInput editExpressionInput && editingContext instanceof IEMFEditingContext emfEditingContext) {
            Optional<Expression> optionalExpression = this.objectSearchService.getObject(editingContext, editExpressionInput.expressionElementId())
                    .filter(Expression.class::isInstance)
                    .map(Expression.class::cast);
            Optional<Element> optionalParent = Optional.empty();
            if (optionalExpression.isPresent()) {
                optionalParent = Optional.ofNullable(optionalExpression.get().getOwner());
            }

            if (optionalParent.isPresent() && optionalExpression.isPresent()) {
                var result = this.expressionEditor.editExpression(emfEditingContext, optionalParent.get(), optionalExpression.get(), editExpressionInput.newExpressionText());
                if (result.createdExpression() != null) {
                    var newExpression = result.createdExpression();
                    var newExpressionId = this.identityService.getId(newExpression);

                    changeDescription = new ChangeDescription(ChangeKind.SEMANTIC_CHANGE, editingContext.getId(), input);
                    payload = new EditExpressionSuccessPayload(input.id(), newExpressionId, result.messages());
                } else {
                    payload = new ErrorPayload(input.id(), result.messages());
                }
            } else {
                payload = new ErrorPayload(input.id(), this.messageService.notFound());
            }
        } else {
            payload = new ErrorPayload(input.id(), this.messageService.invalidInput(EditExpressionInput.class.getName(), input.getClass().getName()));
        }

        payloadSink.tryEmitValue(payload);
        changeDescriptionSink.tryEmitNext(changeDescription);
    }
}
