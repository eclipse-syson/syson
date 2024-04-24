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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.syson.diagram.common.view.services.ViewLabelService;
import org.eclipse.syson.diagram.statetransition.view.StateTransitionViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.Step;
import org.eclipse.syson.sysml.Succession;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.TransitionFeatureKind;
import org.eclipse.syson.sysml.TransitionFeatureMembership;
import org.eclipse.syson.sysml.TransitionUsage;

/**
 * Edge-related Java services used by the {@link StateTransitionViewDiagramDescriptionProvider}.
 * This is ongoing work and requires additional developments.
 *
 * @author adieumegard
 */
public class StateTransitionViewEdgeService {
    
    /**
     * The default separator used when printing a set of Trigger Actions for a TransitionUsage label
     */
    private static final String TRIGGER_ACTION_SEPARATOR = "| ";

    /**
     * The default separator used when printing a set of Guard Expressions for a TransitionUsage label 
     */
    private static final String GUARD_EXPRESSION_SEPARATOR = "& ";

    /**
     * The default separator used when printing a set of Effect Action for a TransitionUsage label
     */
    private static final String EFFECT_ACTION_SEPARATOR = ", ";
    
    private ViewLabelService viewLabelService;

    public StateTransitionViewEdgeService(IFeedbackMessageService feedbackMessageService) {
        this.viewLabelService = new ViewLabelService(feedbackMessageService);
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
        
        if (triggerActions != null) {
            triggerLabel = getTriggerActionsDefaultDirectEditLabel(triggerActions);
            if (!triggerLabel.isBlank()) {
                resultLabel += triggerLabel;
            }
        }
        if (guardExpressions != null) {
            guardLabel = getGuardExpressionsDefaultDirectEditLabel(guardExpressions);
            if (!guardLabel.isBlank()) {
                if (!resultLabel.isBlank()) {
                    resultLabel += " ";
                }
                resultLabel += "[" + guardLabel + "]";
            }
        }
        if (effectActions != null) {
            effectLabel = getEffectActionsDefaultDirectEditLabel(effectActions);
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
     * Edits the trigger, guard and effect of a transition based on the label value.
     * TransitionUsage label is of the form
     * <pre>
     * {@code Trigger ('['Guard']')? '/' Effect} 
     * or
     * {@code AcceptParameterPart (guard-expression)? '/' ActionUsage}
     * </pre>
     * 
     * @param transition
     *            The {@code TransitionUsage} to edit
     * @param newLabel
     *            The string label based on which edit is done
     * @return the given {@link TransitionUsage}.
     */
    public TransitionUsage performLabelEdit(TransitionUsage transition, String newLabel) {
        if (newLabel.length() > 0) {
            String newTriggerValue = "";
            String newGuardValue = "";
            String newEffectValue = "";
            // Add newLabel parsing
            performTriggerEdit(transition, newTriggerValue);
            performGuardEdit(transition, newGuardValue);
            performEffectEdit(transition, newEffectValue);
        }
        return transition;
    }
    
    /**
     * Set a new source {@link ActionUsage} for the given {@link TransitionUsage}.
     * Used by {@code TransitionEdgeDescriptionProvider.createSourceReconnectTool()}
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
     * Set a new target {@link ActionUsage} for the given {@link TransitionUsage}.
     * Used by {@code TransitionEdgeDescriptionProvider.createTargetReconnectTool()}
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

    private void addTransitionFeature(TransitionUsage transition, TransitionFeatureKind kind, Step au) {
        TransitionFeatureMembership tfMembership = SysmlFactory.eINSTANCE.createTransitionFeatureMembership();
        tfMembership.setKind(kind);
        transition.getOwnedFeatureMembership().add(tfMembership);
        tfMembership.getTransitionFeature();
        
        FeatureMembership fm = SysmlFactory.eINSTANCE.createFeatureMembership();
        tfMembership.getOwnedRelationship().add(fm);
        fm.getOwnedRelatedElement().add(au);
    }

    private void createAcceptActionUsageFromString(TransitionUsage transition, String newTriggerValue) {
        List<String> triggerValues = Arrays.asList(newTriggerValue.split(TRIGGER_ACTION_SEPARATOR.trim()));
        
        // Get referred ActionUsage
        List<AcceptActionUsage> actionUsages = new ArrayList<>();
        triggerValues.stream().map(triggerValue -> {
            return getVisibleAcceptActionUsageFromName(transition, triggerValue);
        }).map(actionUsages::add);
        
        // Set TransitionUsage transitionFeature
        actionUsages.stream().forEach(au -> {
            addTransitionFeature(transition, TransitionFeatureKind.TRIGGER, au);
        });
    }

    private void createActionUsageFromString(TransitionUsage transition, String newEffectValue) {
        List<String> effectValues = Arrays.asList(newEffectValue.split(EFFECT_ACTION_SEPARATOR.trim()));
        
        // Get referred ActionUsage
        List<ActionUsage> actionUsages = new ArrayList<>();
        effectValues.stream().map(effectValue -> {
            return getVisibleActionUsageFromName(transition, effectValue);
        }).map(actionUsages::add);
        
        // Set TransitionUsage transitionFeature
        actionUsages.stream().forEach(au -> {
            addTransitionFeature(transition, TransitionFeatureKind.EFFECT, au);
        });
    }

    private void createExpressionFromString(TransitionUsage transition, String newGuardValue) {
        List<String> guardValues = Arrays.asList(newGuardValue.split(GUARD_EXPRESSION_SEPARATOR.trim()));
        
        // Get referred Expressions
        List<Expression> expressions = new ArrayList<>();
        guardValues.stream().map(guardValue -> {
            return getExpressionFromString(transition, guardValue);
        }).map(expressions::add);
        
        // Set TransitionUsage transitionFeature
        expressions.stream().forEach(au -> {
            addTransitionFeature(transition, TransitionFeatureKind.GUARD, au);
        });
    }

    private String getEffectActionsDefaultDirectEditLabel(EList<ActionUsage> effectActions) {
        String effectLabel;
        effectLabel = getElementsDefaultInitialDirectEditLabel(effectActions, (a, b) -> {
            return a + EFFECT_ACTION_SEPARATOR + b;
        });
        return effectLabel;
    }

    private String getElementsDefaultInitialDirectEditLabel(EList<? extends Element> elements, BinaryOperator<String> reduceOperator) {
        return elements.stream()
                .map(action -> {
                    return viewLabelService.getDefaultInitialDirectEditLabel(action);
                })
                .reduce(reduceOperator)
                .orElse("");
    }

    private Expression getExpressionFromString(TransitionUsage transition, String guardValue) {
        return null;
    }

    private String getGuardExpressionsDefaultDirectEditLabel(EList<Expression> guardExpressions) {
        String guardLabel;
        guardLabel = getElementsDefaultInitialDirectEditLabel(guardExpressions, (a, b) -> {
            return a + GUARD_EXPRESSION_SEPARATOR + b;
        });
        return guardLabel;
    }

    private String getTriggerActionsDefaultDirectEditLabel(EList<AcceptActionUsage> triggerActions) {
        String triggerLabel;
        triggerLabel = getElementsDefaultInitialDirectEditLabel(triggerActions, (a, b) -> {
            return a + TRIGGER_ACTION_SEPARATOR + b;
        });
        return triggerLabel;
    }

    private AcceptActionUsage getVisibleAcceptActionUsageFromName(TransitionUsage transition, String triggerValue) {
        return null;
    }

    private ActionUsage getVisibleActionUsageFromName(TransitionUsage transition, String effectValue) {
        return null;
    }

    /**
     * Edits the effect of a transition based on the label value.
     * 
     * @param transition
     *            The {@code TransitionUsage} to edit
     * @param newEffectValue
     *            The string label based on which edit is done
     * @return the given {@link TransitionUsage}.
     */
    private TransitionUsage performEffectEdit(TransitionUsage transition, String newEffectValue) {
        EList<ActionUsage> effectAction = transition.getEffectAction();
        // Check if modification needed
        if (effectAction != null && !effectAction.isEmpty()) {
            String currentLabel = getEffectActionsDefaultDirectEditLabel(effectAction);
            if (currentLabel.equals(newEffectValue)) {
                return transition;
            }
        }
        
        // Unset all effects
        removeTransitionFeaturesOfSpecificKind(transition, TransitionFeatureKind.EFFECT);
        
        // Create effect(s)
        if (!newEffectValue.isBlank()) {
            createActionUsageFromString(transition, newEffectValue);
        }
        return transition;
    }

    /**
     * Edits the guard of a transition based on the label value.
     * 
     * @param transition
     *            The {@code TransitionUsage} to edit
     * @param newGuardValue
     *            The string label based on which edit is done
     * @return the given {@link TransitionUsage}.
     */
    private TransitionUsage performGuardEdit(TransitionUsage transition, String newGuardValue) {
        EList<Expression> expressions = transition.getGuardExpression();
        // Check if modification needed
        if (expressions != null && !expressions.isEmpty()) {
            String currentLabel = getGuardExpressionsDefaultDirectEditLabel(expressions);
            if (currentLabel.equals(newGuardValue)) {
                return transition;
            }
        }
        
        // Unset all guards
        removeTransitionFeaturesOfSpecificKind(transition, TransitionFeatureKind.GUARD);
        
        // Create guard(s)
        if (!newGuardValue.isBlank()) {
            createExpressionFromString(transition, newGuardValue);
        }
        return transition;
    }
    
    /**
     * Edits the trigger of a transition based on the label value.
     * 
     * @param transition
     *            The {@code TransitionUsage} to edit
     * @param newTriggerValue
     *            The string label based on which edit is done
     * @return the given {@link TransitionUsage}.
     */
    private TransitionUsage performTriggerEdit(TransitionUsage transition, String newTriggerValue) {
        EList<AcceptActionUsage> triggerAction = transition.getTriggerAction();
        // Check if modification needed
        if (triggerAction != null && !triggerAction.isEmpty()) {
            String currentTriggerLabel = getTriggerActionsDefaultDirectEditLabel(triggerAction);
            if (currentTriggerLabel.equals(newTriggerValue)) {
                return transition;
            }
        }
        
        // Unset all trigger
        removeTransitionFeaturesOfSpecificKind(transition, TransitionFeatureKind.TRIGGER);

        // Create trigger(s)
        if (!newTriggerValue.isBlank()) {
            createAcceptActionUsageFromString(transition, newTriggerValue);
        }
        return transition;
    }

    private void removeTransitionFeaturesOfSpecificKind(TransitionUsage transition, TransitionFeatureKind kind) {
        List<TransitionFeatureMembership> elementsToDelete = transition.getOwnedFeatureMembership().stream()
            .filter(TransitionFeatureMembership.class::isInstance)
            .map(TransitionFeatureMembership.class::cast)
            .filter(tfm -> tfm.getKind().equals(kind))
            .toList();
        EcoreUtil.removeAll(elementsToDelete);
    }
}

