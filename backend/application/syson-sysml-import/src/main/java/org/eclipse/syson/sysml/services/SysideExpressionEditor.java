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
import org.eclipse.syson.sysml.ASTTransformer;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.Function;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.SysmlToAst;
import org.eclipse.syson.sysml.TransitionFeatureKind;
import org.eclipse.syson.sysml.TransitionFeatureMembership;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.metamodel.services.textual.utils.Status;
import org.eclipse.syson.sysml.services.api.ExpressionCreationResult;
import org.eclipse.syson.sysml.services.api.ISysMLExpressionEditor;
import org.springframework.stereotype.Service;

/**
 * syside-based implementation of {@link ISysMLExpressionEditor}.
 *
 * @author pcdavid
 */
@Service
public class SysideExpressionEditor implements ISysMLExpressionEditor {

    private final SysmlToAst sysmlToAst;

    public SysideExpressionEditor(SysmlToAst sysmlToAst) {
        this.sysmlToAst = Objects.requireNonNull(sysmlToAst);
    }

    @Override
    public ExpressionCreationResult createExpression(IEMFEditingContext emfEditingContext, Element parentElement, String expressionText) {
        return this.createOrEditExpression(emfEditingContext, parentElement, Optional.empty(), expressionText);
    }

    @Override
    public ExpressionCreationResult editExpression(IEMFEditingContext emfEditingContext, Element parentElement, Expression expression, String expressionText) {
        return this.createOrEditExpression(emfEditingContext, parentElement, Optional.of(expression), expressionText);
    }

    /**
     * Create a new expression from the given text and add it inside the specified parent element, optionally replacing
     * an existing expression (when called in "edit existing expression" mode).
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
     * @return the result of creating a new expression. It can either contain the actual expression created (on success)
     *         or a list of messages on failure.
     */
    private ExpressionCreationResult createOrEditExpression(IEMFEditingContext emfEditingContext, Element parentElement, Optional<Expression> expressionToReplace, String expressionText) {
        List<Element> newObjects = List.of();
        List<Message> messages = new ArrayList<>();

        // Needed to force the syside parser to treat the input text as an Expression.
        var wrappedText = this.wrapPlainExpression(parentElement, expressionText);

        // Phase 1: parse the raw text into a JSON AST using syside-cli
        var inputStream = new ByteArrayInputStream(wrappedText.getBytes());
        var astParsingResult = this.sysmlToAst.convert(inputStream, ".sysml");
        messages.addAll(this.toMessages(astParsingResult.reports()));

        // Phase 2: "parse" the resulting JSON AST into actual SySMLv2 Elements and integrate them into
        // parentElement
        if (astParsingResult.ast().isPresent()) {
            var transformer = new ASTTransformer();
            newObjects = transformer.convertToElements(astParsingResult.ast().get(), parentElement, this::unwrap, this::containsErrors);
            messages.addAll(transformer.getTransformationMessages());
        }
        boolean success = !this.containsErrors(messages);
        var optionalResult = this.getNewExpression(newObjects);

        if (success && optionalResult.isPresent()) {
            if (expressionToReplace.isPresent()) {
                this.replace(expressionToReplace.get(), optionalResult.get());
            }
            return new ExpressionCreationResult(optionalResult.get(), messages);
        } else {
            return new ExpressionCreationResult(null, messages);
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
     * {@link SysmlToAst converter}.
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
     * syside parser to actually interpret it as an Expression.
     *
     * @param parentElement
     * @param plainExpression
     * @return
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
     * {@link #wrapPlainExpression(Element, String)}.
     *
     * @param roots
     * @return
     */
    private List<Element> unwrap(List<Element> roots) {
        Element result = null;
        var parentElement = roots.stream().findFirst().orElse(null);
        if (parentElement instanceof StateUsage wrappingState) {
            result = this.extractGuard(wrappingState);
        } else if (parentElement instanceof Function || parentElement instanceof Expression) {
            result = parentElement.getOwnedRelationship().stream().map(Element.class::cast).findFirst().orElse(null);
        } else if (parentElement instanceof Feature) {
            result = parentElement.getOwnedRelationship().stream().map(Element.class::cast).findFirst().orElse(null);
        }

        return Optional.ofNullable(result).stream().toList();
    }

    /**
     * Extracts the guard (TransitionFeatureMembership and its Expression) from the parsed version of the template
     * created in {@link #wrapPlainExpression(Element, String)}.
     *
     * @param wrappingState
     *            the parsed version of the {@code "state ... { }"} template created in
     *            {@link #wrapPlainExpression(Element, String)}.
     * @return the {@link TransitionFeatureMembership guard} which contains the expression parsed.
     */
    private Element extractGuard(StateUsage wrappingState) {
        Element result;
        result = wrappingState.getOwnedRelationship().get(2)
                .getOwnedRelatedElement().get(0)
                .getOwnedRelationship().stream()
                .filter(TransitionFeatureMembership.class::isInstance)
                .map(TransitionFeatureMembership.class::cast)
                .filter(tfm -> tfm.getKind() == TransitionFeatureKind.GUARD)
                .findFirst()
                .orElse(null);
        return result;
    }

    private List<Message> toMessages(List<Status> reports) {
        return reports.stream().map((Status status) -> {
            return switch (status.severity()) {
                case INFO -> new Message(status.message(), MessageLevel.INFO);
                case WARNING -> new Message(status.message(), MessageLevel.WARNING);
                case ERROR -> new Message(status.message(), MessageLevel.ERROR);
                default -> null;
            };
        }).filter(Objects::nonNull).toList();
    }

}
