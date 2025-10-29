/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.diagram.services;

import static java.util.stream.Collectors.joining;

import java.util.Objects;
import java.util.function.BinaryOperator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.syson.diagram.services.api.IDiagramLabelService;
import org.eclipse.syson.diagram.services.utils.MultiLineLabelSwitch;
import org.eclipse.syson.services.SimpleNameDeresolver;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Comment;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Dependency;
import org.eclipse.syson.sysml.Documentation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.LiteralExpression;
import org.eclipse.syson.sysml.MultiplicityRange;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.StateSubactionMembership;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.TextualRepresentation;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.VariantMembership;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.eclipse.syson.sysml.textual.SysMLElementSerializer;
import org.eclipse.syson.sysml.textual.utils.Appender;
import org.eclipse.syson.sysml.textual.utils.FileNameDeresolver;
import org.eclipse.syson.sysml.textual.utils.INameDeresolver;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Label-related services doing queries in diagrams.
 *
 * @author arichard
 */
@Service
public class DiagramQueryLabelService implements IDiagramLabelService {

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

    private final Logger logger = LoggerFactory.getLogger(DiagramQueryLabelService.class);

    @Override
    public String getIdentificationLabel(Element element) {
        StringBuilder label = new StringBuilder();
        if (element instanceof ActionUsage && element.eContainer() instanceof StateSubactionMembership ssm) {
            label.append(ssm.getKind());
        }
        label.append(this.getShortNameLabel(element));
        String declaredName = element.getDeclaredName();
        if (declaredName != null) {
            label.append(declaredName);
        }
        return label.toString();
    }

    @Override
    public String getRedefinitionLabel(Element element) {
        StringBuilder label = new StringBuilder();
        var optRedefinition = element.getOwnedRelationship().stream()
                .filter(Redefinition.class::isInstance)
                .map(Redefinition.class::cast)
                .findFirst();
        if (optRedefinition.isPresent()) {
            Redefinition redefinition = optRedefinition.get();
            if (!redefinition.isIsImplied()) {
                var redefinedFeature = redefinition.getRedefinedFeature();
                if (redefinedFeature != null) {
                    label.append(LabelConstants.SPACE);
                    label.append(LabelConstants.REDEFINITION);
                    label.append(LabelConstants.SPACE);
                    label.append(this.getDeclaredNameLabel(redefinedFeature));
                }
            }
        }
        return label.toString();
    }

    @Override
    public String getSubclassificationLabel(Element element) {
        StringBuilder label = new StringBuilder();
        var optSubclassification = element.getOwnedRelationship().stream()
                .filter(Subclassification.class::isInstance)
                .map(Subclassification.class::cast)
                .findFirst();
        if (optSubclassification.isPresent()) {
            Subclassification subclassification = optSubclassification.get();
            if (!subclassification.isIsImplied()) {
                var superclassifier = subclassification.getSuperclassifier();
                if (superclassifier != null) {
                    label.append(LabelConstants.SPACE);
                    label.append(LabelConstants.SUBCLASSIFICATION);
                    label.append(LabelConstants.SPACE);
                    label.append(this.getDeclaredNameLabel(superclassifier));
                }
            }
        }
        return label.toString();
    }

    @Override
    public String getSubsettingLabel(Element element) {
        StringBuilder label = new StringBuilder();
        var optSubsetting = element.getOwnedRelationship().stream()
                .filter(r -> r instanceof Subsetting && !(r instanceof Redefinition))
                .map(Subsetting.class::cast)
                .findFirst();
        if (optSubsetting.isPresent()) {
            Subsetting subsetting = optSubsetting.get();
            if (!subsetting.isIsImplied()) {
                var subsettedFeature = subsetting.getSubsettedFeature();
                if (subsettedFeature != null) {
                    label.append(LabelConstants.SPACE);
                    label.append(LabelConstants.SUBSETTING);
                    label.append(LabelConstants.SPACE);
                    label.append(this.getDeclaredNameLabel(subsettedFeature));
                }
            }
        }
        return label.toString();
    }

    @Override
    public String getTypingLabel(Element element) {
        StringBuilder label = new StringBuilder();
        var optFeatureTyping = element.getOwnedRelationship().stream()
                .filter(FeatureTyping.class::isInstance)
                .map(FeatureTyping.class::cast)
                .findFirst();
        if (optFeatureTyping.isPresent()) {
            FeatureTyping featureTyping = optFeatureTyping.get();
            if (!featureTyping.isIsImplied()) {
                var type = featureTyping.getType();
                if (type != null) {
                    label.append(LabelConstants.SPACE);
                    label.append(LabelConstants.COLON);
                    label.append(LabelConstants.SPACE);
                    label.append(this.getDeclaredNameLabel(type));
                }
            }
        }
        return label.toString();
    }

    @Override
    public String getValueLabel(Usage usage) {
        return this.getValueStringRepresentation(usage, false);
    }

    @Override
    public String getSysmlTextualRepresentation(Element element, boolean resolvableName) {
        return this.buildSerializer(resolvableName).doSwitch(element);
    }

    /**
     * Return the container label for the given {@link Element}.
     *
     * @param element
     *            the given {@link Element}.
     * @return the container label for the given {@link Element}.
     */
    public String getContainerLabel(Element element) {
        return new MultiLineLabelSwitch(this).doSwitch(element);
    }

    /**
     * Return the label of the multiplicity part of the given {@link Element}.
     *
     * @param element
     *            the given {@link Element}.
     * @return the label of the multiplicity part of the given {@link Element} if there is one, an empty string
     *         otherwise.
     */
    public String getMultiplicityLabel(Element element) {
        return this.getMultiplicityStringRepresentation(element, false);
    }

    /**
     * Return the label of the multiplicity part of the given {@link Element}.
     *
     * @param element
     *            element the given {@link Element}.
     * @param directEditInput
     *            holds <code>true</code> if the label is used as a direct edit input
     * @return the label of the multiplicity part of the given {@link Element} if there is one, an empty string
     *         otherwise.
     */
    private String getMultiplicityStringRepresentation(Element element, boolean directEditInput) {
        StringBuilder label = new StringBuilder();
        var optMultiplicityRange = element.getOwnedRelationship().stream()
                .filter(OwningMembership.class::isInstance)
                .map(OwningMembership.class::cast)
                .flatMap(m -> m.getOwnedRelatedElement().stream())
                .filter(MultiplicityRange.class::isInstance)
                .map(MultiplicityRange.class::cast)
                .findFirst();
        label.append(LabelConstants.SPACE);
        if (optMultiplicityRange.isPresent()) {
            var range = optMultiplicityRange.get();
            String firstBound = null;
            String secondBound = null;
            var bounds = range.getOwnedRelationship().stream()
                    .filter(OwningMembership.class::isInstance)
                    .map(OwningMembership.class::cast)
                    .flatMap(m -> m.getOwnedRelatedElement().stream())
                    .filter(LiteralExpression.class::isInstance)
                    .map(LiteralExpression.class::cast)
                    .toList();
            if (bounds.size() == 1) {
                firstBound = this.getSysmlTextualRepresentation(bounds.get(0), directEditInput);
            } else if (bounds.size() == 2) {
                firstBound = this.getSysmlTextualRepresentation(bounds.get(0), directEditInput);
                secondBound = this.getSysmlTextualRepresentation(bounds.get(1), directEditInput);
            }
            label.append(LabelConstants.OPEN_BRACKET);
            if (firstBound != null) {
                label.append(firstBound);
            }
            if (secondBound != null) {
                label.append("..");
                label.append(secondBound);
            }
            label.append(LabelConstants.CLOSE_BRACKET + LabelConstants.SPACE);
        }
        if (element instanceof Feature feature) {
            if (feature.isIsOrdered()) {
                label.append(LabelConstants.ORDERED + LabelConstants.SPACE);
            }
            if (!feature.isIsUnique()) {
                label.append(LabelConstants.NON_UNIQUE + LabelConstants.SPACE);
            }
        }
        String labelAsString = label.toString().trim();
        if (!labelAsString.isEmpty()) {
            return LabelConstants.SPACE + labelAsString;
        } else {
            return labelAsString;
        }
    }

    /**
     * Get the value to display when a direct edit has been called on the given {@link Element}.
     *
     * @param element
     *            the given {@link Element}.
     * @return the value to display.
     */
    public String getDefaultInitialDirectEditLabel(Element element) {
        StringBuilder builder = new StringBuilder();
        if (element instanceof Usage usage) {
            builder.append(this.getBasicNamePrefix(usage));
        }
        builder.append(this.getIdentificationLabel(element));
        builder.append(this.getMultiplicityStringRepresentation(element, true));
        builder.append(this.getTypingLabel(element));
        builder.append(this.getRedefinitionLabel(element));
        builder.append(this.getSubsettingLabel(element));
        builder.append(this.getSubclassificationLabel(element));
        if (element instanceof Usage usage) {
            builder.append(this.getValueStringRepresentation(usage, true));
        }
        return builder.toString();
    }

    /**
     * Return the label of the value part of the given {@link Usage}.
     *
     * @param usage
     *            the given {@link Usage}.
     * @param directEditInput
     *            if the label is being used as direct edit input
     * @return the label of the value part of the given {@link Usage} if there is one, an empty string otherwise.
     */
    private String getValueStringRepresentation(Usage usage, boolean directEditInput) {
        StringBuilder label = new StringBuilder();
        var featureValue = usage.getOwnedRelationship().stream()
                .filter(FeatureValue.class::isInstance)
                .map(FeatureValue.class::cast)
                .findFirst();
        if (featureValue.isPresent()) {
            var expression = featureValue.get().getValue();
            String valueAsString = null;
            if (expression != null) {
                valueAsString = this.getSysmlTextualRepresentation(expression, directEditInput);
            }

            if (featureValue.get().isIsDefault()) {
                label
                        .append(LabelConstants.SPACE)
                        .append(LabelConstants.DEFAULT);
            }
            label
                    .append(LabelConstants.SPACE)
                    .append(this.getFeatureValueRelationshipSymbol(featureValue.get()))
                    .append(LabelConstants.SPACE)
                    .append(valueAsString);
        }
        return label.toString();
    }

    /**
     * Return the label of the prefix part of the given {@link Usage}.
     *
     * @param usage
     *            the given {@link Usage}.
     * @return the label of the prefix part of the given {@link Usage} if there is one, an empty string otherwise.
     */
    private String getBasicNamePrefix(Element element) {
        StringBuilder label = new StringBuilder();
        if (element instanceof Usage usage) {
            if (usage.isIsVariation()) {
                label.append(LabelConstants.VARIATION + LabelConstants.SPACE);
            }
        } else if (element instanceof Definition definition) {
            if (definition.isIsVariation()) {
                label.append(LabelConstants.VARIATION + LabelConstants.SPACE);
            }
        }
        EObject membership = element.getOwningMembership();
        if (membership != null) {
            EObject parent = membership.eContainer();
            boolean hasVariationParent = (parent instanceof Definition && ((Definition) parent).isIsVariation()) | (parent instanceof Usage && ((Usage) parent).isIsVariation());
            if (membership instanceof VariantMembership | hasVariationParent) {
                label.append(LabelConstants.VARIANT);
            }
        }
        if (element instanceof Type type && type.isIsAbstract()) {
            label.append(LabelConstants.ABSTRACT + LabelConstants.SPACE);
        }
        if (element instanceof Usage usage) {
            this.getReferenceUsagePrefix(usage, label);
        }
        return label.toString();
    }

    /**
     * Get the value to display when a direct edit has been called on the given {@link Comment}.
     *
     * @param comment
     *            the given {@link Comment}.
     * @return the value to display.
     */
    public String getDefaultInitialDirectEditLabel(Comment comment) {
        return comment.getBody();
    }

    /**
     * Get the value to display when a direct edit has been called on the given {@link TextualRepresentation}.
     *
     * @param textualRepresentation
     *            the given {@link Comment}.
     * @return the value to display.
     */
    public String getDefaultInitialDirectEditLabel(TextualRepresentation textualRepresentation) {
        return textualRepresentation.getBody();
    }


    /**
     * Return the label of the prefix part of the given {@link Usage}.
     *
     * @param usage
     *            the given {@link Usage}.
     * @return the label of the prefix part of the given {@link Usage} if there is one, an empty string otherwise.
     */
    public String getUsageListItemPrefix(Usage usage) {
        StringBuilder label = new StringBuilder();
        if (usage.getDirection() == FeatureDirectionKind.IN) {
            label.append(LabelConstants.IN + LabelConstants.SPACE);
        } else if (usage.getDirection() == FeatureDirectionKind.OUT) {
            label.append(LabelConstants.OUT + LabelConstants.SPACE);
        } else if (usage.getDirection() == FeatureDirectionKind.INOUT) {
            label.append(LabelConstants.INOUT + LabelConstants.SPACE);
        }
        if (usage.isIsAbstract() && !usage.isIsVariation()) {
            label.append(LabelConstants.ABSTRACT + LabelConstants.SPACE);
        }
        if (usage.isIsVariation()) {
            label.append(LabelConstants.VARIATION + LabelConstants.SPACE);
        }
        if (usage.isIsConstant()) {
            label.append(LabelConstants.CONSTANT + LabelConstants.SPACE);
        }
        if (usage.isIsDerived()) {
            label.append(LabelConstants.DERIVED + LabelConstants.SPACE);
        }
        if (usage.isIsEnd()) {
            label.append(LabelConstants.END + LabelConstants.SPACE);
        }
        this.getReferenceUsagePrefix(usage, label);
        return label.toString();
    }

    /**
     * Return the label for the given {@link Usage} when displayed as a compartment item.
     *
     * @param usage
     *            the given {@link Usage}.
     * @return the label for the given {@link Usage}.
     */
    public String getCompartmentItemLabel(Usage usage) {
        return this.getCompartmentItemStringRepresentation(usage, false);
    }

    private String getCompartmentItemStringRepresentation(Usage usage, boolean directEditInput) {
        StringBuilder label = new StringBuilder();
        if (usage instanceof ConstraintUsage constraintUsage
                && usage.getOwningMembership() instanceof RequirementConstraintMembership) {
            // Use the constraint-specific rendering only if the element is a constraint owned by a requirement. Other
            // constraints (including requirements) are rendered as regular elements.
            label.append(this.getCompartmentItemLabel(constraintUsage, directEditInput));
        } else {
            label.append(this.getUsageListItemPrefix(usage));
            label.append(this.getIdentificationLabel(usage));
            label.append(this.getMultiplicityStringRepresentation(usage, directEditInput));
            label.append(this.getTypingLabel(usage));
            label.append(this.getRedefinitionLabel(usage));
            label.append(this.getSubsettingLabel(usage));
            label.append(this.getValueStringRepresentation(usage, directEditInput));
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
    private String getCompartmentItemLabel(ConstraintUsage constraintUsage, boolean directEditInput) {
        StringBuilder label = new StringBuilder();
        if (constraintUsage == null) {
            label.append("");
        } else if (!constraintUsage.getOwnedMember().isEmpty() && constraintUsage.getOwnedMember().get(0) instanceof Expression expression) {
            label.append(this.getSysmlTextualRepresentation(expression, directEditInput));
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
            result = this.getInitialDirectEditListItemLabel(constraintUsage, true);
        } else {
            result = this.getCompartmentItemStringRepresentation(usage, true);
        }
        return result;
    }

    private String getInitialDirectEditListItemLabel(ConstraintUsage constraintUsage, boolean directEditInput) {
        String result;
        if (!constraintUsage.getOwnedMember().isEmpty() && constraintUsage.getOwnedMember().get(0) instanceof Expression expression) {
            result = this.getSysmlTextualRepresentation(expression, directEditInput);
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
     * Computes the label for a {@link TransitionUsage}.
     *
     * @param transition
     *            The {@link TransitionUsage} to compute the label for
     * @param displayGuard
     *            holds <code>true</code> to display the guard
     */
    private String getTransitionLabel(TransitionUsage transition, boolean displayGuard) {
        // trigger-expression '/' ActionUsage
        Appender appender = new Appender(LabelConstants.SPACE, LabelConstants.SPACE);

        this.handleTransitionTriggerExpression(transition, displayGuard, appender, false);

        EList<ActionUsage> effectActions = transition.getEffectAction();
        if (!effectActions.isEmpty()) {
            String effectLabel = this.getEffectActionsDefaultDirectEditLabel(effectActions);
            if (!effectLabel.isBlank()) {
                appender.appendWithSpaceIfNeeded("/ " + effectLabel);
            }
        }
        return appender.toString();
    }

    private void handleTransitionTriggerExpression(TransitionUsage transition, boolean displayGuard, Appender appender, boolean directEditInput) {
        this.handleAcceptParameterPart(transition, appender, directEditInput);
        if (displayGuard) {
            this.handleGuardExpression(transition, appender, directEditInput);
        }
    }

    private void handleGuardExpression(TransitionUsage transition, Appender appender, boolean directEditInput) {
        EList<Expression> guardExpressions = transition.getGuardExpression();
        if (!guardExpressions.isEmpty()) {
            String textGuardExpression = guardExpressions.stream().map(exp -> this.getSysmlTextualRepresentation(exp, directEditInput))
                    .filter(Objects::nonNull)
                    .collect(joining(GUARD_EXPRESSION_SEPARATOR));
            appender.appendWithSpaceIfNeeded("[").append(textGuardExpression).append("]");
        }
    }

    private void handleAcceptParameterPart(TransitionUsage transition, Appender appender, boolean directEditInput) {
        EList<AcceptActionUsage> triggerActions = transition.getTriggerAction();
        if (!triggerActions.isEmpty()) {
            SysMLElementSerializer sysmlSerializer = this.buildSerializer(directEditInput);
            String textGuardExpression = triggerActions.stream().map(sysmlSerializer::getAcceptParameterPart)
                    .filter(Objects::nonNull)
                    .collect(joining(TRIGGER_ACTION_SEPARATOR));
            appender.append(textGuardExpression);
        }
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
                    if (action instanceof AcceptActionUsage aau && aau.getPayloadParameter() != null) {
                        return this.getDefaultInitialDirectEditLabel(aau.getPayloadParameter());
                    }
                    return this.getDefaultInitialDirectEditLabel(action);
                })
                .reduce(reduceOperator)
                .orElse("");
    }

    /**
     * Return the label for the given {@link Usage} represented as a border node.
     *
     * @param usage
     *            the given {@link Usage}.
     * @return the label for the given {@link Usage}.
     */
    public String getBorderNodeUsageLabel(Usage usage) {
        StringBuilder label = new StringBuilder();
        String declaredName = usage.getDeclaredName();
        if (declaredName != null && !declaredName.isBlank()) {
            label.append(declaredName);
        }
        label
                .append(this.getTypingLabel(usage))
                .append(this.getRedefinitionLabel(usage))
                .append(this.getSubsettingLabel(usage));
        return label.toString();
    }

    private String getDeclaredNameLabel(Element element) {
        var label = element.getName();
        if (ElementUtil.isFromStandardLibrary(element)) {
            label = element.getQualifiedName();
        }
        return label;
    }

    /**
     * Returns the label for the short name of the given {@code element}.
     *
     * @param element
     *            the element to compute the short name label from
     * @return the label for the short name of the given {@code element}
     */
    private String getShortNameLabel(Element element) {
        StringBuilder label = new StringBuilder();
        String declaredShortName = element.getDeclaredShortName();
        if (declaredShortName != null && !declaredShortName.isBlank()) {
            label.append(LabelConstants.LESSER_THAN)
                    .append(declaredShortName)
                    .append(LabelConstants.GREATER_THAN)
                    .append(LabelConstants.SPACE);
        }
        return label.toString();
    }

    /**
     * Return the label of the reference prefix part of the given {@link Usage}.
     *
     * @param usage
     *            the given {@link Usage}.
     * @return the label of the reference prefix part of the given {@link Usage} if there is one, an empty string
     *         otherwise.
     */
    private void getReferenceUsagePrefix(Usage usage, StringBuilder label) {
        if (usage.isIsReference() && !(usage instanceof AttributeUsage)) {
            // AttributeUsage are always referential, so no need to add the ref keyword
            label.append(LabelConstants.REF + LabelConstants.SPACE);
        }
    }

    /**
     * Builds a SysMLSerializer.
     *
     * @param resolvableName
     *            holds <code>true</code> to compute resolvable names for references, otherwise simple name are used to
     *            reference an element.
     * @return a new {@link SysMLElementSerializer}.
     */
    private SysMLElementSerializer buildSerializer(boolean resolvableName) {
        final INameDeresolver nameDeresolver;
        if (resolvableName) {
            nameDeresolver = new FileNameDeresolver();
        } else {
            nameDeresolver = new SimpleNameDeresolver();
        }
        return new SysMLElementSerializer("\n", " ", nameDeresolver, s -> {
            this.logger.info(s.message());
        });
    }

    private String getFeatureValueRelationshipSymbol(FeatureValue featureValueMembership) {
        final String affectationSymbole;

        if (featureValueMembership.isIsInitial()) {
            affectationSymbole = LabelConstants.COLON_EQUAL;
        } else {
            affectationSymbole = LabelConstants.EQUAL;
        }
        return affectationSymbole;
    }
}
