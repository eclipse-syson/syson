/*******************************************************************************
 * Copyright (c) 2023, 2024 Obeo.
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

import java.util.Objects;
import java.util.function.BinaryOperator;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.emf.common.util.EList;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.syson.services.DiagramDirectEditListener;
import org.eclipse.syson.services.LabelService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.services.grammars.DirectEditLexer;
import org.eclipse.syson.services.grammars.DirectEditParser;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Documentation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.StateSubactionMembership;
import org.eclipse.syson.sysml.Succession;
import org.eclipse.syson.sysml.TransitionFeatureKind;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.helper.LabelConstants;

/**
 * Label-related Java services used in diagrams.
 *
 * @author arichard
 */
public class ViewLabelService extends LabelService {
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
            label.append(this.getUsagePrefix(usage));
            String declaredName = usage.getDeclaredName();
            if (declaredName != null) {
                label.append(declaredName);
            }
            label
                    .append(this.getMultiplicityLabel(usage))
                    .append(this.getTypingLabel(usage))
                    .append(this.getRedefinitionLabel(usage))
                    .append(this.getSubsettingLabel(usage))
                    .append(this.getValueLabel(usage));
        }
        return label.toString();
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
            label.append(constraintUsage.getDeclaredName());
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
        if (usage instanceof ActionUsage au && usage.eContainer() instanceof StateSubactionMembership ssm) {
            label.append(ssm.getKind() + " ");
        }
        label.append(this.getCompartmentItemLabel(usage));
        return label.toString();
    }

    /**
     * Get the value to display when a direct edit has been called on the given {@link Usage}.
     *
     * @param usage
     *            the given {@link Usage}.
     * @return the value to display.
     */
    public String getInitialDirectEditLabel(Usage usage) {
        String result;
        if (usage instanceof ConstraintUsage constraintUsage &&
                usage.getOwningMembership() instanceof RequirementConstraintMembership) {
            result = this.getInitialDirectEditLabel(constraintUsage);
        } else {
            result = this.getCompartmentItemLabel(usage);
        }
        return result;
    }

    private String getInitialDirectEditLabel(ConstraintUsage constraintUsage) {
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
     * @param usage
     *            the given {@link Documentation}.
     * @return the value to display.
     */
    public String getInitialDirectEditLabel(Documentation documentation) {
        return documentation.getBody();
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
     * Edit the TransitionUsage based on the provided textual content {@code newLabel}.
     *
     * @param element
     *            The {@link TransitionUsage} to edit
     * @param newLabel
     *            The user provided label
     * @return the given {@link TransitionUsage}.
     */
    public Element directEditTransitionEdgeLabel(TransitionUsage element, String newLabel) {
        DirectEditLexer lexer = new DirectEditLexer(CharStreams.fromString(newLabel));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DirectEditParser parser = new DirectEditParser(tokens);
        ParseTree tree = parser.transitionExpression();
        ParseTreeWalker walker = new ParseTreeWalker();
        String[] options = new String[] { LabelService.REDEFINITION_OFF, LabelService.SUBSETTING_OFF, LabelService.VALUE_OFF, LabelService.TYPING_OFF };
        DiagramDirectEditListener listener = new DiagramDirectEditListener(element, this.getFeedbackMessageService(), options);
        walker.walk(listener, tree);

        // cleanup deleted elements on empty expression
        TransitionFeatureKind.VALUES.stream()
                .filter(val -> !listener.getVisitedTransitionFeatures().get(val))
                .forEach(val -> this.utilService.removeTransitionFeaturesOfSpecificKind(element, val));

        return element;
    }

    /**
     * Succession edge label getter service.
     *
     * @param succession
     *            The succession edge
     * @return the label of the given {@link Succession}
     */
    public String getSuccessionLabel(Succession succession) {
        StringBuilder resultLabel = new StringBuilder();
        var guardExpressions = succession.getGuardExpression();
        if (!guardExpressions.isEmpty()) {
            String guardLabel = this.getGuardExpressionsDefaultDirectEditLabel(guardExpressions);
            if (!guardLabel.isBlank()) {
                resultLabel.append(LabelConstants.OPEN_BRACKET)
                        .append(guardLabel)
                        .append(LabelConstants.CLOSE_BRACKET);
            }
        }
        return resultLabel.toString();
    }

    /**
     * Compute the label for a {@link TransitionUsage}.
     *
     * @param transition
     *            The {@link TransitionUsage} to compute the label for
     */
    public String getTransitionLabel(TransitionUsage transition) {
        String triggerLabel = "";
        String guardLabel = "";
        String effectLabel = "";
        String resultLabel = "";

        EList<AcceptActionUsage> triggerActions = transition.getTriggerAction();
        EList<Expression> guardExpressions = transition.getGuardExpression();
        EList<ActionUsage> effectActions = transition.getEffectAction();

        if (!triggerActions.isEmpty()) {
            triggerLabel = this.getTriggerActionsDefaultDirectEditLabel(triggerActions);
            if (!triggerLabel.isBlank()) {
                resultLabel += triggerLabel;
            }
        }
        if (!guardExpressions.isEmpty()) {
            guardLabel = this.getGuardExpressionsDefaultDirectEditLabel(guardExpressions);
            if (!guardLabel.isBlank()) {
                if (!resultLabel.isBlank()) {
                    resultLabel += " ";
                }
                resultLabel += "[" + guardLabel + "]";
            }
        }
        if (!effectActions.isEmpty()) {
            effectLabel = this.getEffectActionsDefaultDirectEditLabel(effectActions);
            if (!effectLabel.isBlank()) {
                if (!resultLabel.isBlank()) {
                    resultLabel += " ";
                }
                resultLabel += "/ " + effectLabel;
            }
        }
        return resultLabel;
    }

    private String getElementsDefaultInitialDirectEditLabel(EList<? extends Element> elements, BinaryOperator<String> reduceOperator) {
        return elements.stream()
                .map(action -> {
                    if (action instanceof AcceptActionUsage aau) {
                        return this.getDefaultInitialDirectEditLabel(aau.getPayloadParameter());
                    }
                    return this.getDefaultInitialDirectEditLabel(action);
                })
                .reduce(reduceOperator)
                .orElse("");
    }

    private String getTriggerActionsDefaultDirectEditLabel(EList<AcceptActionUsage> triggerActions) {
        String triggerLabel;
        triggerLabel = this.getElementsDefaultInitialDirectEditLabel(triggerActions, (a, b) -> {
            return a + TRIGGER_ACTION_SEPARATOR + b;
        });
        return triggerLabel;
    }

    private String getGuardExpressionsDefaultDirectEditLabel(EList<Expression> guardExpressions) {
        var guardLabel = this.getElementsDefaultInitialDirectEditLabel(guardExpressions, (a, b) -> {
            return a + GUARD_EXPRESSION_SEPARATOR + b;
        });
        return guardLabel;
    }

    private String getEffectActionsDefaultDirectEditLabel(EList<ActionUsage> effectActions) {
        String effectLabel;
        effectLabel = this.getElementsDefaultInitialDirectEditLabel(effectActions, (a, b) -> {
            return a + EFFECT_ACTION_SEPARATOR + b;
        });
        return effectLabel;
    }
}
