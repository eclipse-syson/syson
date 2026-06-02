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
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;
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

    private final MetamodelQueryElementService metamodelQueryElementService;

    private final Counter counter;

    public EditExpressionEventHandler(IObjectSearchService objectSearchService, IIdentityService identityService, ICollaborativeMessageService messageService, ISysMLExpressionEditor expressionEditor,
            MeterRegistry meterRegistry) {
        this.objectSearchService = Objects.requireNonNull(objectSearchService);
        this.identityService = Objects.requireNonNull(identityService);
        this.messageService = Objects.requireNonNull(messageService);
        this.expressionEditor = Objects.requireNonNull(expressionEditor);
        this.metamodelQueryElementService = new MetamodelQueryElementService();
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
            var optionalParent = this.getExpressionParent(emfEditingContext, editExpressionInput.elementId());
            var optionalExpression = this.getExpression(emfEditingContext, editExpressionInput.elementId());

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

    /**
     * Finds the {@link Expression} element to consider given the provided {@code elementId}.
     *
     * @param editingContext
     *            the editing context.
     * @param elementId
     *            either to id of an actual {@link Expression} element, or of the parent {@link Element} of a single
     *            {@code Expression}.
     * @return the directly of indirectly designated {@link Expression}.
     */
    private Optional<Expression> getExpression(IEditingContext editingContext, String elementId) {
        Optional<Expression> result = Optional.empty();
        Optional<Element> optionalElement = this.objectSearchService.getObject(editingContext, elementId)
                .filter(Element.class::isInstance)
                .map(Element.class::cast);
        if (optionalElement.isPresent()) {
            Element element = optionalElement.get();
            if (element instanceof Expression expression && this.metamodelQueryElementService.isTopLevelExpression(expression)) {
                result = optionalElement.map(Expression.class::cast);
            } else {
                result = this.metamodelQueryElementService.findSingleExpressionDefinition(element);
            }
        }
        return result;
    }

    /**
     * Finds the {@link Expression} element to consider given the provided {@code elementId}.
     *
     * @param editingContext
     *            the editing context.
     * @param elementId
     *            either to id of an actual {@link Expression} element, or of the parent {@link Element} of a single
     *            {@code Expression}.
     * @return the directly of indirectly designated {@link Expression}.
     */
    private Optional<Element> getExpressionParent(IEditingContext editingContext, String elementId) {
        Optional<Element> result = Optional.empty();

        Optional<Element> optionalElement = this.objectSearchService.getObject(editingContext, elementId)
                .filter(Element.class::isInstance)
                .map(Element.class::cast);
        if (optionalElement.isPresent()) {
            Element element = optionalElement.get();
            if (element instanceof Expression expression && this.metamodelQueryElementService.isTopLevelExpression(expression)) {
                result = optionalElement.map(Element::getOwner);
            } else {
                result = optionalElement;
            }
        }
        return result;
    }
}
