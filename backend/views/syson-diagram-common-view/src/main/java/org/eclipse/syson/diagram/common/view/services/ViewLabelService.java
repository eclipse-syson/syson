/*******************************************************************************
 * Copyright (c) 2023, 2025 Obeo.
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
package org.eclipse.syson.diagram.common.view.services;

import static java.util.stream.Collectors.joining;

import java.util.Objects;
import java.util.function.BinaryOperator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.syson.services.LabelService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Comment;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Dependency;
import org.eclipse.syson.sysml.Documentation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.StateSubactionMembership;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.eclipse.syson.sysml.textual.SysMLElementSerializer;
import org.eclipse.syson.sysml.textual.utils.Appender;
import org.eclipse.syson.sysml.textual.utils.NameDeresolver;

/**
 * Label-related Java services used in diagrams.
 *
 * @author arichard
 */
public class ViewLabelService extends LabelService {

    private static final String SPACE = " ";

    /**
     * The default separator used when printing a set of Trigger Actions for a TransitionUsage label
     */
    private static final String TRIGGER_ACTION_SEPARATOR = " | ";

    /**
     * The default separator used when printing a set of Guard Expressions for a TransitionUsage label
     */
    private static final String GUARD_EXPRESSION_SEPARATOR = "& ";

    /**
     * The default separator used when printing a set of Effect Action for a TransitionUsage label
     */
    private static final String EFFECT_ACTION_SEPARATOR = ", ";

    private final UtilService utilService;

    private final ShowDiagramsIconsService showDiagramsIconsService;

    public ViewLabelService(IFeedbackMessageService feedbackMessageService, ShowDiagramsIconsService showDiagramsIconsService) {
        super(feedbackMessageService);
        this.utilService = new UtilService();
        this.showDiagramsIconsService = Objects.requireNonNull(showDiagramsIconsService);
    }

    /**
     * Get the value of the showIcon property of the ShowDiagramsIconsService.
     *
     * @param object
     *            The current object.
     * @return the value of the showIcon property of the ShowDiagramsIconsService.
     */
    public boolean showIcon(Object object) {
        return this.showDiagramsIconsService.getShowIcons();
    }

    /**
     * Return the label for the given {@link Usage} when displayed as a compartment item.
     *
     * @param usage
     *            the given {@link Usage}.
     * @return the label for the given {@link Usage}.
     */
    public String getCompartmentItemLabel(Usage usage) {
        StringBuilder label = new StringBuilder();
        if (usage instanceof ConstraintUsage constraintUsage
                && usage.getOwningMembership() instanceof RequirementConstraintMembership) {
            // Use the constraint-specific rendering only if the element is a constraint owned by a requirement. Other
            // constraints (including requirements) are rendered as regular elements.
            label.append(this.getCompartmentItemLabel(constraintUsage));
        } else {
            label.append(this.getUsageListItemPrefix(usage));
            label.append(this.getIdentificationLabel(usage));
            label.append(this.getMultiplicityLabel(usage));
            label.append(this.getTypingLabel(usage));
            label.append(this.getRedefinitionLabel(usage));
            label.append(this.getSubsettingLabel(usage));
            label.append(this.getValueLabel(usage));
        }
        return label.toString();
    }

    /**
     * The default label for edges.
     *
     * @param element
     *            the element to get the edge label from
     * @return the edge label
     */
    public String getEdgeLabel(Element element) {
        StringBuilder label = new StringBuilder();
        label.append(this.getIdentificationLabel(element));
        label.append(this.getTypingLabel(element));
        return label.toString();
    }

    /**
     * Returns the label for the given {@code dependency}.
     *
     * @param dependency
     *            the dependency to get the edge label from
     * @return the edge label
     */
    public String getDependencyLabel(Dependency dependency) {
        return this.getIdentificationLabel(dependency);
    }

    /**
     * Returns the label for the given {@link ConstraintUsage} when displayed as compartment item.
     *
     * @param constraintUsage
     *            the given {@link ConstraintUsage}
     * @return the label for the given {@link ConstraintUsage}
     */
    private String getCompartmentItemLabel(ConstraintUsage constraintUsage) {
        StringBuilder label = new StringBuilder();
        if (!constraintUsage.getOwnedMember().isEmpty() && constraintUsage.getOwnedMember().get(0) instanceof Expression expression) {
            label.append(this.getValue(expression));
        } else {
            // The constraint doesn't have an expression, we use its name as default label.
            label.append(this.getIdentificationLabel(constraintUsage));
        }
        return label.toString();
    }

    /**
     * Return the label for the given {@link Documentation} when displayed as a compartment item.
     *
     * @param documentation
     *            the given {@link Documentation}.
     * @return the label for the given {@link Documentation}.
     */
    public String getCompartmentItemLabel(Documentation documentation) {
        StringBuilder label = new StringBuilder();
        String declaredName = documentation.getDeclaredName();
        if (declaredName != null && !declaredName.isBlank()) {
            label.append(declaredName);
            label.append(LabelConstants.CR);
        }
        String body = documentation.getBody();
        if (body != null) {
            label.append(body);
        }
        return label.toString();
    }

    /**
     * Return the label for the given {@link Usage} prefixed with additional content.
     *
     * @param usage
     *            the given {@link Usage}.
     * @return the label for the given {@link Usage}.
     */
    public String getPrefixedCompartmentItemUsageLabel(Usage usage) {
        StringBuilder label = new StringBuilder();
        if (usage instanceof ActionUsage && usage.eContainer() instanceof StateSubactionMembership ssm) {
            label.append(ssm.getKind() + SPACE);
        }
        label.append(this.getCompartmentItemLabel(usage));
        return label.toString();
    }

    /**
     * Get the value to display when a direct edit has been called on the given item list {@link Usage}.
     *
     * @param usage
     *            the given {@link Usage}.
     * @return the value to display.
     */
    public String getInitialDirectEditListItemLabel(Usage usage) {
        String result;
        if (usage instanceof ConstraintUsage constraintUsage &&
                usage.getOwningMembership() instanceof RequirementConstraintMembership) {
            result = this.getInitialDirectEditListItemLabel(constraintUsage);
        } else {
            result = this.getCompartmentItemLabel(usage);
        }
        return result;
    }

    private String getInitialDirectEditListItemLabel(ConstraintUsage constraintUsage) {
        String result;
        if (!constraintUsage.getOwnedMember().isEmpty() && constraintUsage.getOwnedMember().get(0) instanceof Expression expression) {
            result = this.getValue(expression);
        } else {
            // The constraint doesn't have an expression, we set an initial empty string for the direct edit.
            result = "";
        }
        return result;
    }

    /**
     * Get the value to display when a direct edit has been called on the given {@link Documentation}.
     *
     * @param documentation
     *            the given {@link Documentation}.
     * @return the value to display.
     */
    public String getInitialDirectEditListItemLabel(Documentation documentation) {
        return documentation.getBody();
    }

    /**
     * Get the value to display when a direct edit has been called on the given {@link Comment}.
     *
     * @param comment
     *            the given {@link comment}.
     * @return the value to display.
     */
    public String getInitialDirectEditListItemLabel(Comment comment) {
        return comment.getBody();
    }

    public String getMultiplicityRangeInitialDirectEditLabel(Element element) {
        return this.getMultiplicityLabel(element);
    }

    public Element editMultiplicityRangeCenterLabel(Element element, String newLabel) {
        return this.directEdit(element, newLabel, LabelService.NAME_OFF, LabelService.REDEFINITION_OFF, LabelService.SUBSETTING_OFF, LabelService.TYPING_OFF, LabelService.VALUE_OFF);
    }

    public Element editEdgeCenterLabel(Element element, String newLabel) {
        return this.directEdit(element, newLabel, LabelService.REDEFINITION_OFF, LabelService.SUBSETTING_OFF, LabelService.VALUE_OFF);
    }

    /**
     * Computes the label for a {@link TransitionUsage}.
     *
     * @param transition
     *            The {@link TransitionUsage} to compute the label for
     * @param displayGuard
     *            holds <code>true</code> to display the guard
     */
    public String getTransitionLabel(TransitionUsage transition, boolean displayGuard) {
        // trigger-expression '/' ActionUsage
        Appender appender = new Appender(SPACE, SPACE);

        this.handleTransitionTriggerExpression(transition, displayGuard, appender);

        EList<ActionUsage> effectActions = transition.getEffectAction();
        if (!effectActions.isEmpty()) {
            String effectLabel = this.getEffectActionsDefaultDirectEditLabel(effectActions);
            if (!effectLabel.isBlank()) {
                appender.appendWithSpaceIfNeeded("/ " + effectLabel);
            }
        }
        return appender.toString();
    }

    /**
     * Computes the label for a {@link TransitionUsage}.
     *
     * @param transition
     *            The {@link TransitionUsage} to compute the label for
     */
    public String getTransitionLabel(TransitionUsage transition) {
        return this.getTransitionLabel(transition, true);
    }

    /**
     * Return the label for the given {@link Usage} represented as a border node.
     *
     * @param usage
     *         the given {@link Usage}.
     * @return the label for the given {@link Usage}.
     */
    public String getBorderNodeUsageLabel(Usage usage) {
        StringBuilder label = new StringBuilder();
        label
                .append(usage.getDeclaredName())
                .append(this.getTypingLabel(usage))
                .append(this.getRedefinitionLabel(usage))
                .append(this.getSubsettingLabel(usage));
        return label.toString();
    }

    private String getElementsDefaultInitialDirectEditLabel(EList<? extends Element> elements, BinaryOperator<String> reduceOperator) {
        return elements.stream()
                .map(action -> {
                    if (action instanceof AcceptActionUsage aau && aau.getPayloadParameter() != null) {
                        return this.getDefaultInitialDirectEditLabel(aau.getPayloadParameter());
                    }
                    return this.getDefaultInitialDirectEditLabel(action);
                })
                .reduce(reduceOperator)
                .orElse("");
    }

    private String getEffectActionsDefaultDirectEditLabel(EList<ActionUsage> effectActions) {
        String effectLabel;
        effectLabel = this.getElementsDefaultInitialDirectEditLabel(effectActions, (a, b) -> {
            return a + EFFECT_ACTION_SEPARATOR + b;
        });
        return effectLabel;
    }

    private void handleTransitionTriggerExpression(TransitionUsage transition, boolean displayGuard, Appender appender) {
        this.handleAcceptParameterPart(transition, appender);
        if (displayGuard) {
            this.handleGuardExpression(transition, appender);
        }
    }

    private void handleGuardExpression(TransitionUsage transition, Appender appender) {
        EList<Expression> guardExpressions = transition.getGuardExpression();
        if (!guardExpressions.isEmpty()) {
            SysMLElementSerializer sysmlSerializer = new SysMLElementSerializer(SPACE, SPACE, new NameDeresolver(), null);
            String textGuardExpression = guardExpressions.stream().map(sysmlSerializer::doSwitch)
                    .filter(Objects::nonNull)
                    .collect(joining(GUARD_EXPRESSION_SEPARATOR));
            appender.appendWithSpaceIfNeeded("[").append(textGuardExpression).append("]");
        }
    }

    private void handleAcceptParameterPart(TransitionUsage transition, Appender appender) {
        EList<AcceptActionUsage> triggerActions = transition.getTriggerAction();
        if (!triggerActions.isEmpty()) {
            SysMLElementSerializer sysmlSerializer = new SysMLElementSerializer(SPACE, SPACE, new NameDeresolver(), null);
            String textGuardExpression = triggerActions.stream().map(sysmlSerializer::getAcceptParameterPart)
                    .filter(Objects::nonNull)
                    .collect(joining(TRIGGER_ACTION_SEPARATOR));
            appender.append(textGuardExpression);
        }
    }
}
