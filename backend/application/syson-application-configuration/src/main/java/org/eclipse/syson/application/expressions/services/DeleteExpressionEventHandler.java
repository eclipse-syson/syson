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
package org.eclipse.syson.application.expressions.services;

import java.util.Objects;
import java.util.Optional;

import org.eclipse.sirius.components.collaborative.api.ChangeDescription;
import org.eclipse.sirius.components.collaborative.api.ChangeKind;
import org.eclipse.sirius.components.collaborative.api.IEditingContextEventHandler;
import org.eclipse.sirius.components.collaborative.messages.ICollaborativeMessageService;
import org.eclipse.sirius.components.core.api.ErrorPayload;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IInput;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.sirius.components.core.api.SuccessPayload;
import org.eclipse.syson.application.expressions.dto.DeleteExpressionInput;
import org.eclipse.syson.services.DeleteService;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Sinks;

/**
 * Event handler for the {@code deleteExpression} mutation.
 *
 * @author pcdavid
 */
@Service
public class DeleteExpressionEventHandler implements IEditingContextEventHandler {

    private final IObjectSearchService objectSearchService;

    private final MetamodelQueryElementService metamodelQueryElementService;

    private final ICollaborativeMessageService messageService;

    private final DeleteService deleteService;

    public DeleteExpressionEventHandler(IObjectSearchService objectSearchService, ICollaborativeMessageService messageService) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.metamodelQueryElementService = new MetamodelQueryElementService();
        this.messageService = Objects.requireNonNull(messageService);
        this.deleteService = new DeleteService();
    }

    @Override
    public boolean canHandle(IEditingContext editingContext, IInput input) {
        return input instanceof DeleteExpressionInput;
    }

    @Override
    public void handle(Sinks.One<IPayload> payloadSink, Sinks.Many<ChangeDescription> changeDescriptionSink, IEditingContext editingContext, IInput input) {
        IPayload payload;
        if (input instanceof DeleteExpressionInput deleteExpressionInput) {
            Optional<Expression> optionalExpression = this.objectSearchService.getObject(editingContext, deleteExpressionInput.parentElementId())
                    .filter(Element.class::isInstance)
                    .map(Element.class::cast)
                    .flatMap(element -> this.metamodelQueryElementService.findSingleExpressionDefinition(element));
            if (optionalExpression.isPresent()) {
                this.deleteService.deleteFromModel(optionalExpression.get());
                var changeDescription = new ChangeDescription(ChangeKind.SEMANTIC_CHANGE, editingContext.getId(), input);
                changeDescriptionSink.tryEmitNext(changeDescription);
                payload = new SuccessPayload(input.id());
            } else {
                payload = new ErrorPayload(input.id(), this.messageService.notFound());
            }
        } else {
            payload = new ErrorPayload(input.id(), this.messageService.invalidInput(DeleteExpressionInput.class.getName(), input.getClass().getName()));
        }
        payloadSink.tryEmitValue(payload);
    }
}
