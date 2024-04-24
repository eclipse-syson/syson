/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
package org.eclipse.syson.diagram.statetransition.view.services;

import java.util.Objects;
import java.util.function.BinaryOperator;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.emf.common.util.EList;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.syson.diagram.common.view.services.ViewLabelService;
import org.eclipse.syson.diagram.statetransition.view.StateTransitionViewDiagramDescriptionProvider;
import org.eclipse.syson.services.DiagramDirectEditListener;
import org.eclipse.syson.services.LabelService;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.services.grammars.DirectEditLexer;
import org.eclipse.syson.services.grammars.DirectEditParser;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.Succession;
import org.eclipse.syson.sysml.TransitionFeatureKind;
import org.eclipse.syson.sysml.TransitionUsage;

/**
 * Edge-related Java services used by the {@link StateTransitionViewDiagramDescriptionProvider}. This is ongoing work
 * and requires additional developments.
 *
 * @author adieumegard
 */
public class StateTransitionViewEdgeService {
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

    private ViewLabelService viewLabelService;

    private IFeedbackMessageService feedbackMessageService;

    private UtilService utilService;

    public StateTransitionViewEdgeService(IFeedbackMessageService feedbackMessageService) {
        this.feedbackMessageService = Objects.requireNonNull(feedbackMessageService);
        this.viewLabelService = new ViewLabelService(feedbackMessageService);
        this.utilService = new UtilService();
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

    /**
     * Edit the TransitionUsage based on the provided textual content {@code newLabel}.
     *
     * @param element
     *            The {@link TransitionUsage} to edit
     * @param newLabel
     *            The user provided label
     * @return the given {@link TransitionUsage}.
     */
    public Element editTransitionEdgeLabel(TransitionUsage element, String newLabel) {
        DirectEditLexer lexer = new DirectEditLexer(CharStreams.fromString(newLabel));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DirectEditParser parser = new DirectEditParser(tokens);
        ParseTree tree = parser.transitionExpression();
        ParseTreeWalker walker = new ParseTreeWalker();
        String[] options = new String[] { LabelService.REDEFINITION_OFF, LabelService.SUBSETTING_OFF, LabelService.VALUE_OFF, LabelService.TYPING_OFF };
        DiagramDirectEditListener listener = new DiagramDirectEditListener(element, this.feedbackMessageService, options);
        walker.walk(listener, tree);

        // cleanup deleted elements on empty expression
        TransitionFeatureKind.VALUES.stream()
            .filter(val -> !listener.getVisitedTransitionFeatures().get(val))
            .forEach(val -> this.utilService.removeTransitionFeaturesOfSpecificKind(element, val));

        return element;
    }

    /**
     * Set a new source {@link ActionUsage} for the given {@link TransitionUsage}. Used by
     * {@code TransitionEdgeDescriptionProvider.createSourceReconnectTool()}
     *
     * @param transition
     *            the given {@link TransitionUsage}.
     * @param newSource
     *            the new target {@link ActionUsage}.
     * @return the given {@link TransitionUsage}.
     */
    public TransitionUsage setTransitionSource(TransitionUsage transition, ActionUsage newSource) {
        ActionUsage currentSource = transition.getSource();
        // Update transition source
        transition.getOwnedRelationship().stream()
                .filter(FeatureMembership.class::isInstance)
                .map(FeatureMembership.class::cast)
                .findFirst()
                .ifPresent(mem -> mem.setMemberElement(newSource));
        // Update succession source
        Succession succession = transition.getSuccession();
        succession.getSource().replaceAll(e -> {
            if (e.equals(currentSource)) {
                return newSource;
            }
            return e;
        });
        return transition;
    }

    /**
     * Set a new target {@link ActionUsage} for the given {@link TransitionUsage}. Used by
     * {@code TransitionEdgeDescriptionProvider.createTargetReconnectTool()}
     *
     * @param transition
     *            the given {@link TransitionUsage}.
     * @param newTarget
     *            the new target {@link ActionUsage}.
     * @return the given {@link TransitionUsage}.
     */
    public TransitionUsage setTransitionTarget(TransitionUsage transition, ActionUsage newTarget) {
        ActionUsage currentTarget = transition.getTarget();
        // Update succession target
        Succession succession = transition.getSuccession();
        succession.getTarget().replaceAll(e -> {
            if (e.equals(currentTarget)) {
                return newTarget;
            }
            return e;
        });
        return transition;
    }

    private String getEffectActionsDefaultDirectEditLabel(EList<ActionUsage> effectActions) {
        String effectLabel;
        effectLabel = this.getElementsDefaultInitialDirectEditLabel(effectActions, (a, b) -> {
            return a + EFFECT_ACTION_SEPARATOR + b;
        });
        return effectLabel;
    }

    private String getElementsDefaultInitialDirectEditLabel(EList<? extends Element> elements, BinaryOperator<String> reduceOperator) {
        return elements.stream()
                .map(action -> {
                    if (action instanceof AcceptActionUsage aau) {
                        return this.viewLabelService.getDefaultInitialDirectEditLabel(aau.getPayloadParameter());
                    }
                    return this.viewLabelService.getDefaultInitialDirectEditLabel(action);
                })
                .reduce(reduceOperator)
                .orElse("");
    }

    private String getGuardExpressionsDefaultDirectEditLabel(EList<Expression> guardExpressions) {
        String guardLabel;
        guardLabel = this.getElementsDefaultInitialDirectEditLabel(guardExpressions, (a, b) -> {
            return a + GUARD_EXPRESSION_SEPARATOR + b;
        });
        return guardLabel;
    }

    private String getTriggerActionsDefaultDirectEditLabel(EList<AcceptActionUsage> triggerActions) {
        String triggerLabel;
        triggerLabel = this.getElementsDefaultInitialDirectEditLabel(triggerActions, (a, b) -> {
            return a + TRIGGER_ACTION_SEPARATOR + b;
        });
        return triggerLabel;
    }
}
