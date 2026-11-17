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

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.components.emf.services.api.IEMFEditingContext;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.Function;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.ResultExpressionMembership;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.TransitionFeatureKind;
import org.eclipse.syson.sysml.TransitionFeatureMembership;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.dto.ExpressionPropertiesInput;
import org.eclipse.syson.sysml.dto.FeatureValueExpressionPropertiesInput;
import org.eclipse.syson.sysml.services.api.ExpressionCreationResult;
import org.eclipse.syson.sysml.services.api.ISysMLExpressionEditor;
import org.springframework.stereotype.Service;

/**
 * Java parser and linker based implementation of {@link ISysMLExpressionEditor}.
 *
 * @author pcdavid
 */
@Service
public class SysMLExpressionEditor implements ISysMLExpressionEditor {

    private final SysMLParsingService parsingService;

    /**
     * Creates an expression editor.
     *
     * @param parsingService the shared Java parsing pipeline
     */
    public SysMLExpressionEditor(SysMLParsingService parsingService) {
        this.parsingService = Objects.requireNonNull(parsingService);
    }

    @Override
    public ExpressionCreationResult createExpression(IEMFEditingContext emfEditingContext, Element parentElement, String expressionText, ExpressionPropertiesInput properties) {
        return this.createOrEditExpression(emfEditingContext, parentElement, Optional.empty(), expressionText, properties);
    }

    @Override
    public ExpressionCreationResult editExpression(IEMFEditingContext emfEditingContext, Element parentElement, Expression expression, String expressionText,
            ExpressionPropertiesInput properties) {
        return this.createOrEditExpression(emfEditingContext, parentElement, Optional.of(expression), expressionText, properties);
    }

    /**
     * Create a new expression from the given text and add it inside the specified parent element, optionally replacing
     * an existing expression (when called in "edit existing expression" mode). Required SI imports are committed only
     * after parsing and linking succeed without warnings or errors.
     *
     * @param emfEditingContext
     *            the editing context.
     * @param parentElement
     *            the parent element in which to create the new expression.
     * @param expressionToReplace
     *            an optional existing expression inside the parent element. If provided, the newly created expression
     *            will <em>replace</em> this one instead of just being added.
     * @param expressionText
     *            the textual representation of the expression to create.
     * @param properties
     *            the optional typed properties to apply after the expression is created or replaced.
     * @return the result of creating a new expression. It can either contain the actual expression created (on success)
     *         or a list of messages on failure.
     */
    private ExpressionCreationResult createOrEditExpression(IEMFEditingContext emfEditingContext, Element parentElement, Optional<Expression> expressionToReplace, String expressionText,
            ExpressionPropertiesInput properties) {
        List<Element> newObjects = List.of();
        List<Message> messages = new ArrayList<>();

        var wrappedText = this.wrapPlainExpression(parentElement, expressionText);
        var inputStream = new ByteArrayInputStream(wrappedText.getBytes(StandardCharsets.UTF_8));
        var result = this.parsingService.parseAndLink(emfEditingContext.getDomain().getResourceSet(), parentElement, inputStream);
        messages.addAll(result.messages());
        if (result.root() != null) {
            newObjects = this.unwrap(result.elements());
        }
        boolean success = !this.containsErrors(messages);
        var optionalResult = this.getNewExpression(newObjects);

        if (success && optionalResult.isPresent()) {
            for (Element element : newObjects) {
                parentElement.getOwnedRelationship().add((Relationship) element);
            }
            if (expressionToReplace.isPresent()) {
                this.replace(expressionToReplace.get(), optionalResult.get());
            }
            this.applyProperties(optionalResult.get(), properties);
            result.applyImports();
            return new ExpressionCreationResult(optionalResult.get(), messages);
        } else {
            return new ExpressionCreationResult(null, messages);
        }
    }

    /**
     * Applies supported properties to the owner of the created or edited expression.
     *
     * @param expression
     *            the created or edited expression.
     * @param properties
     *            the optional properties to apply.
     */
    private void applyProperties(Expression expression, ExpressionPropertiesInput properties) {
        if (properties != null && properties.featureValue() != null) {
            this.getOwningFeatureValue(expression).ifPresent(featureValue -> this.applyFeatureValueProperties(featureValue, properties.featureValue()));
        }
    }

    /**
     * Gets the owning {@link FeatureValue} of the provided expression, if any.
     *
     * @param expression
     *            the expression whose owner should be inspected.
     * @return the owning {@link FeatureValue}, if one exists.
     */
    private Optional<FeatureValue> getOwningFeatureValue(Expression expression) {
        return Optional.ofNullable(expression.getOwningRelationship())
                .filter(FeatureValue.class::isInstance)
                .map(FeatureValue.class::cast);
    }

    /**
     * Applies optional {@link FeatureValue} properties while preserving existing values for omitted fields.
     *
     * @param featureValue
     *            the feature value to update.
     * @param properties
     *            the optional properties to apply.
     */
    private void applyFeatureValueProperties(FeatureValue featureValue, FeatureValueExpressionPropertiesInput properties) {
        if (properties.isDefault() != null) {
            featureValue.setIsDefault(properties.isDefault());
        }
        if (properties.isInitial() != null) {
            featureValue.setIsInitial(properties.isInitial());
        }
    }

    /**
     * Determines if the messages we received from the parser and converter indicate an error. We treat warnings as
     * errors here.
     *
     * @param messages
     *            a list of messages.
     * @return whether the list contains any error or warning.
     */
    private boolean containsErrors(List<Message> messages) {
        return messages.stream().anyMatch(message -> message.level().equals(MessageLevel.WARNING) || message.level().equals(MessageLevel.ERROR));
    }

    /**
     * Finds the actual {@link Expression} element we're interested in in the list of elements returned by the
     * Java parsing pipeline.
     *
     * @param newObjects
     *            the list of elements added to the model as returned by the converter.
     * @return the actual {@link Expression} element we're interested in if we could find it.
     */
    private Optional<Expression> getNewExpression(List<Element> newObjects) {
        if (!newObjects.isEmpty() && newObjects.get(0) instanceof Relationship relationship && !relationship.getOwnedRelatedElement().isEmpty()
                && relationship.getOwnedRelatedElement().get(0) instanceof Expression newExpression) {
            return Optional.of(newExpression);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Replaces an existing {@link Expression} with a new one. We only replace the expression element itself, keeping
     * the parent {@link Relationship}.
     *
     * @param oldExpression
     *            the old expression to replace (it will be removed from the model entirely).
     * @param newExpression
     *            the new expression to replace it with.
     */
    private void replace(Expression oldExpression, Expression newExpression) {
        // Detach the old expression and its owning relationship from the model
        if (newExpression.getOwningRelationship() != null) {
            EcoreUtil.remove(newExpression.getOwningRelationship());
        }
        // Put the new expression in place of the old one in the same owning relationship
        if (oldExpression.getOwningRelationship() != null) {
            EList<Element> relatedElements = oldExpression.getOwningRelationship().getOwnedRelatedElement();
            int index = relatedElements.indexOf(oldExpression);
            relatedElements.add(index, newExpression);
            relatedElements.remove(oldExpression);
        }
    }

    /**
     * Wraps the plain expression entered by the user into a top-level construct of the appropriate kind to force the
     * parser to interpret it as an expression.
     *
     * @param parentElement the contextual parent
     * @param plainExpression the expression text
     * @return the expression in a parseable contextual wrapper
     */
    private String wrapPlainExpression(Element parentElement, String plainExpression) {
        UUID uniqueName = UUID.randomUUID();
        String template = "";
        if (parentElement instanceof TransitionUsage) {
            // Will produce a more complex structure inside which we'll find the actual
            // TransitionFeatureMembership[kind=GUARD] ->
            // Expression. See extractGuardExpression().
            template = """
                    state <'%s'> {
                      entry action init;
                      state off;
                      transition first init if %s then off;
                    }
                    """;
        } else if (parentElement instanceof Function || parentElement instanceof Expression) {
            // Will produce ConstraintDefinition -> ResultExpressionMembership -> Expression
            template = "constraint def <'%s'> { %s }";
        } else if (parentElement instanceof Feature) {
            // Will produce AttributeUsage -> FeatureValue -> Expression
            template = "attribute <'%s'> = %s;";
        }
        return String.format(template, uniqueName, plainExpression);
    }

    /**
     * Extracts the expression and its parent relationship once they have been parsed and linked. This is the reverse of
     * {@link #wrapPlainExpression(Element, String)}. Completion can add other relationships to the wrapper, so its
     * expression ownership is selected by type instead of list position.
     *
     * @param roots the converted wrapper elements
     * @return the expression's owning relationship
     */
    private List<Element> unwrap(List<Element> roots) {
        Element result = null;
        var parentElement = roots.stream().findFirst().orElse(null);
        if (parentElement instanceof StateUsage wrappingState) {
            result = this.extractGuard(wrappingState);
        } else if (parentElement instanceof Function || parentElement instanceof Expression) {
            result = parentElement.getOwnedRelationship().stream().filter(ResultExpressionMembership.class::isInstance).findFirst().orElse(null);
        } else if (parentElement instanceof Feature) {
            result = parentElement.getOwnedRelationship().stream().filter(FeatureValue.class::isInstance).findFirst().orElse(null);
        }

        return Optional.ofNullable(result).stream().toList();
    }

    /**
     * Extracts the guard from the parsed wrapper without depending on the positions of implicit members.
     *
     * @param wrappingState
     *            the parsed version of the {@code "state ... { }"} template created in
     *            {@link #wrapPlainExpression(Element, String)}.
     * @return the {@link TransitionFeatureMembership guard} which contains the expression parsed.
     */
    private Element extractGuard(StateUsage wrappingState) {
        return wrappingState.getOwnedElement().stream()
                .filter(TransitionUsage.class::isInstance)
                .map(TransitionUsage.class::cast)
                .flatMap(transition -> transition.getOwnedRelationship().stream())
                .filter(TransitionFeatureMembership.class::isInstance)
                .map(TransitionFeatureMembership.class::cast)
                .filter(tfm -> tfm.getKind() == TransitionFeatureKind.GUARD)
                .findFirst()
                .orElse(null);
    }

}
