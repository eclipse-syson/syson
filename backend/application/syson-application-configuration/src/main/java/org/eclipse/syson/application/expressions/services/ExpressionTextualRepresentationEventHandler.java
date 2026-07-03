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
import org.eclipse.syson.application.expressions.dto.FeatureValueExpressionPropertiesPayload;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.Function;
import org.eclipse.syson.sysml.Succession;
import org.eclipse.syson.sysml.TransitionUsage;
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
        FeatureValueExpressionPropertiesPayload featureValueProperties = null;
        if (input instanceof ExpressionTextualRepresentationInput expressionTextualRepresentationInput) {
            String elementId = expressionTextualRepresentationInput.elementId();
            Optional<Element> optionalElement = this.getElement(editingContext, elementId);
            Optional<Expression> optionalExpression = optionalElement.flatMap(this::getExpression);
            if (optionalExpression.isPresent()) {
                textualRepresentation = this.metamodelQueryElementService.getExpressionTextualRepresentation(optionalExpression.get());
            }
            featureValueProperties = this.getFeatureValueProperties(optionalElement, optionalExpression);
        }
        payloadSink.tryEmitValue(new ExpressionTextualRepresentationPayload(input.id(), textualRepresentation, featureValueProperties));
    }

    /**
     * Gets the target {@link Element} designated by the provided identifier.
     *
     * @param editingContext
     *            the editing context.
     * @param elementId
     *            the identifier of the target element.
     * @return the target {@link Element}, if it exists.
     */
    private Optional<Element> getElement(IEditingContext editingContext, String elementId) {
        return this.objectSearchService.getObject(editingContext, elementId)
                .filter(Element.class::isInstance)
                .map(Element.class::cast);
    }

    /**
     * Finds the {@link Expression} element to consider given the provided {@code elementId}.
     *
     * @param element
     *            either an actual {@link Expression} element, or the parent {@link Element} of a single
     *            {@code Expression}.
     * @return the directly of indirectly designated {@link Expression}.
     */
    private Optional<Expression> getExpression(Element element) {
        Optional<Expression> result = Optional.empty();
        if (element instanceof Expression expression && this.metamodelQueryElementService.isTopLevelExpression(expression)) {
            result = Optional.of(expression);
        } else {
            result = this.metamodelQueryElementService.findSingleExpressionDefinition(element);
        }
        return result;
    }

    /**
     * Gets the {@link FeatureValueExpressionPropertiesPayload} relevant for the expression editor context.
     *
     * @param optionalElement
     *            the selected element, if any.
     * @param optionalExpression
     *            the effective expression, if any.
     * @return the relevant {@link FeatureValueExpressionPropertiesPayload}, if any.
     */
    private FeatureValueExpressionPropertiesPayload getFeatureValueProperties(Optional<Element> optionalElement, Optional<Expression> optionalExpression) {
        FeatureValueExpressionPropertiesPayload result = null;
        if (optionalElement.isPresent()) {
            Element element = optionalElement.get();
            Optional<FeatureValue> optionalFeatureValue = Optional.empty();
            if (element instanceof Expression expression) {
                optionalFeatureValue = this.getOwningFeatureValue(expression);
            } else if (element instanceof FeatureValue featureValue) {
                optionalFeatureValue = Optional.of(featureValue);
            } else if (this.supportsFeatureValueProperties(element)) {
                optionalFeatureValue = this.getOwnedFeatureValue(element, optionalExpression);
            }
            if (optionalFeatureValue.isPresent()) {
                FeatureValue featureValue = optionalFeatureValue.get();
                result = new FeatureValueExpressionPropertiesPayload(featureValue.isIsDefault(), featureValue.isIsInitial());
            } else if (optionalElement.filter(this::supportsFeatureValueProperties).isPresent()) {
                result = new FeatureValueExpressionPropertiesPayload(false, false);
            }
        }
        return result;
    }

    /**
     * Gets the {@link FeatureValue} directly owned by the selected feature-like element or by the resolved expression.
     *
     * @param element
     *            the selected element.
     * @param optionalExpression
     *            the effective expression, if any.
     * @return the relevant {@link FeatureValue}, if any.
     */
    private Optional<FeatureValue> getOwnedFeatureValue(Element element, Optional<Expression> optionalExpression) {
        Optional<FeatureValue> result = optionalExpression.flatMap(this::getOwningFeatureValue);
        if (result.isEmpty() && element instanceof Feature feature) {
            result = feature.getOwnedRelationship().stream()
                    .filter(FeatureValue.class::isInstance)
                    .map(FeatureValue.class::cast)
                    .findFirst();
        }
        return result;
    }

    /**
     * Gets the owning {@link FeatureValue} of the provided expression, if it is one.
     *
     * @param expression
     *            the expression whose owner should be inspected.
     * @return the owning {@link FeatureValue}, if any.
     */
    private Optional<FeatureValue> getOwningFeatureValue(Expression expression) {
        return Optional.ofNullable(expression.getOwningRelationship())
                .filter(FeatureValue.class::isInstance)
                .map(FeatureValue.class::cast);
    }

    /**
     * Checks whether the selected element would own its expression through a {@link FeatureValue}, which is the only
     * case where the expression editor should expose feature value properties in create mode.
     *
     * @param element
     *            the selected element.
     * @return {@code true} if feature value properties should be exposed for a new expression on this element.
     */
    private boolean supportsFeatureValueProperties(Element element) {
        boolean result = false;
        if (element instanceof TransitionUsage || element instanceof Succession) {
            result = false;
        } else if (element instanceof Function || element instanceof Expression) {
            result = false;
        } else {
            result = element instanceof Feature;
        }
        return result;
    }
}
