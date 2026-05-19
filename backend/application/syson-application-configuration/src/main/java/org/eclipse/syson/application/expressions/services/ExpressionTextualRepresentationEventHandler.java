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
import org.eclipse.sirius.components.collaborative.api.IEditingContextEventHandler;
import org.eclipse.sirius.components.core.api.IEditingContext;
import org.eclipse.sirius.components.core.api.IInput;
import org.eclipse.sirius.components.core.api.IObjectSearchService;
import org.eclipse.sirius.components.core.api.IPayload;
import org.eclipse.syson.application.expressions.dto.ExpressionTextualRepresentationInput;
import org.eclipse.syson.application.expressions.dto.ExpressionTextualRepresentationPayload;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Sinks;

/**
 * Event handler for the {@code expressionTextualRepresentation} query field on EditingContext.
 *
 * @author pcdavid
 */
@Service
public class ExpressionTextualRepresentationEventHandler implements IEditingContextEventHandler {

    private final IObjectSearchService objectSearchService;

    private final MetamodelQueryElementService metamodelQueryElementService;

    public ExpressionTextualRepresentationEventHandler(IObjectSearchService objectSearchService) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.metamodelQueryElementService = new MetamodelQueryElementService();
    }

    @Override
    public boolean canHandle(IEditingContext editingContext, IInput input) {
        return input instanceof ExpressionTextualRepresentationInput;
    }

    @Override
    public void handle(Sinks.One<IPayload> payloadSink, Sinks.Many<ChangeDescription> changeDescriptionSink, IEditingContext editingContext, IInput input) {
        String textualRepresentation = "";
        if (input instanceof ExpressionTextualRepresentationInput expressionTextualRepresentationInput) {
            Optional<Expression> optionalExpression = this.objectSearchService.getObject(editingContext, expressionTextualRepresentationInput.elementId())
                    .filter(Expression.class::isInstance)
                    .map(Expression.class::cast)
                    .filter(this.metamodelQueryElementService::isTopLevelExpression);
            if (optionalExpression.isPresent()) {
                textualRepresentation = this.metamodelQueryElementService.getExpressionTextualRepresentation(optionalExpression.get());
            }
        }
        payloadSink.tryEmitValue(new ExpressionTextualRepresentationPayload(input.id(), textualRepresentation));
    }
}
