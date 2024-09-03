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
package org.eclipse.syson.services;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.syson.services.grammars.DirectEditLexer;
import org.eclipse.syson.services.grammars.DirectEditListener;
import org.eclipse.syson.services.grammars.DirectEditParser;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.ConstraintUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Documentation;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureChainExpression;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.FeatureReferenceExpression;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.LiteralBoolean;
import org.eclipse.syson.sysml.LiteralExpression;
import org.eclipse.syson.sysml.LiteralInfinity;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.LiteralRational;
import org.eclipse.syson.sysml.LiteralString;
import org.eclipse.syson.sysml.MultiplicityRange;
import org.eclipse.syson.sysml.OperatorExpression;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.VariantMembership;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.sysml.helper.LabelConstants;
import org.eclipse.syson.sysml.util.ElementUtil;

/**
 * Label-related Java services used by SysON representations.
 *
 * @author arichard
 */
public class LabelService {

    public static final String MULTIPLICITY_OFF = "MULTIPLICITY_OFF";

    public static final String NAME_OFF = "NAME_OFF";

    public static final String REDEFINITION_OFF = "REDEFINITION_OFF";

    public static final String SUBSETTING_OFF = "SUBSETTING_OFF";

    public static final String TYPING_OFF = "TYPING_OFF";

    public static final String VALUE_OFF = "VALUE_OFF";

    public static final String TRANSITION_EXPRESSION_OFF = "TRANSITION_EXPRESSION_OFF";

    private final IFeedbackMessageService feedbackMessageService;

    public LabelService(IFeedbackMessageService feedbackMessageService) {
        this.feedbackMessageService = Objects.requireNonNull(feedbackMessageService);
    }

    public IFeedbackMessageService getFeedbackMessageService() {
        return this.feedbackMessageService;
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
     * Apply the direct edit result (i.e. the newLabel) to the given {@link Element}.
     *
     * @param element
     *            the given {@link Element}.
     * @param newLabel
     *            the new value to apply.
     * @return the given {@link Element}.
     */
    public Element directEdit(Element element, String newLabel) {
        return this.directEdit(element, newLabel, (String[]) null);
    }

    /**
     * Apply the direct edit result (i.e. the newLabel) to the given {@link Element}, with the provided {@code options}.
     * <p>
     * This method is typically used to enable direct edit with some restrictions (e.g. de-activate the ability to edit
     * the name of an element via direct edit). See {@link #directEdit(Element, String)} to perform a direct edit with
     * default options.
     * </p>
     *
     * @param element
     *            the given {@link Element}
     * @param newLabel
     *            the new value to apply
     * @param options
     *            the options of the direct edit
     * @return the given {@link Element}
     */
    public Element directEdit(Element element, String newLabel, String... options) {
        return this.directEdit(element, newLabel, false, options);
    }

    /**
     * Apply the direct edit result (i.e. the newLabel) to the given graphical node {@link Element}.
     *
     * @param element
     *            the given {@link Element}.
     * @param newLabel
     *            the new value to apply.
     * @return the given {@link Element}.
     */
    public Element directEditNode(Element element, String newLabel) {
        return this.directEdit(element, newLabel, false, (String[]) null);
    }

    /**
     * Apply the direct edit result (i.e. the newLabel) to the given graphical list item {@link Element}.
     *
     * @param element
     *            the given {@link Element}.
     * @param newLabel
     *            the new value to apply.
     * @return the given {@link Element}.
     */
    public Element directEditListItem(Element element, String newLabel) {
        return this.directEdit(element, newLabel, true, (String[]) null);
    }

    /**
     * Apply the direct edit result (i.e. the newLabel) to the given {@link Element} without changing the name of the
     * element itself.
     *
     * @param element
     *            the given {@link Element}.
     * @param newLabel
     *            the new value to apply.
     * @return the given {@link Element}.
     */
    public Element directEditNameOff(Element element, String newLabel) {
        return this.directEdit(element, newLabel, LabelService.NAME_OFF);
    }

    /**
     * Apply the direct edit result (i.e. the newLabel) to the given {@link Element}.
     *
     * @param element
     *            the given {@link Element}.
     * @param newLabel
     *            the new value to apply.
     * @return the given {@link Element}.
     */
    public Element directEdit(Element element, String newLabel, boolean isCompartmentItem, String... options) {
        DirectEditLexer lexer = new DirectEditLexer(CharStreams.fromString(newLabel));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DirectEditParser parser = new DirectEditParser(tokens);
        ParseTree tree;
        if (element instanceof ConstraintUsage && element.getOwningMembership() instanceof RequirementConstraintMembership && isCompartmentItem) {
            // Use the constraint expression parser only if the element is a constraint owned by a requirement, other
            // constraints (including requirements) are parsed as regular elements.
            tree = parser.constraintExpression();
        } else if (isCompartmentItem) {
            tree = parser.listItemExpression();
        } else {
            tree = parser.nodeExpression();
        }
        ParseTreeWalker walker = new ParseTreeWalker();
        DirectEditListener listener = new DiagramDirectEditListener(element, this.getFeedbackMessageService(), options);
        walker.walk(listener, tree);
        return element;
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
        builder.append(element.getDeclaredName());
        builder.append(this.getMultiplicityLabel(element));
        builder.append(this.getTypingLabel(element));
        builder.append(this.getRedefinitionLabel(element));
        builder.append(this.getSubsettingLabel(element));
        builder.append(this.getSubclassificationLabel(element));
        if (element instanceof Usage usage) {
            builder.append(this.getValueLabel(usage));
        }
        return builder.toString();
    }

    /**
     * Get the value to display when a direct edit has been called on the given {@link Documentation}.
     *
     * @param usage
     *            the given {@link Documentation}.
     * @return the value to display.
     */
    public String getDefaultInitialDirectEditLabel(Documentation documentation) {
        return documentation.getBody();
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
                firstBound = this.getValue(bounds.get(0));
            } else if (bounds.size() == 2) {
                firstBound = this.getValue(bounds.get(0));
                secondBound = this.getValue(bounds.get(1));
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
     * Return the label of the prefix part of the given {@link Usage}.
     *
     * @param usage
     *            the given {@link Usage}.
     * @return the label of the prefix part of the given {@link Usage} if there is one, an empty string otherwise.
     */
    public String getBasicNamePrefix(Element element) {
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
        if (usage.isIsReadOnly()) {
            label.append(LabelConstants.READ_ONLY + LabelConstants.SPACE);
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
     * Return the label of the typing part of the given {@link Element}.
     *
     * @param usage
     *            the given {@link Element}.
     * @return the label of the typing part of the given {@link Element} if there is one, an empty string otherwise.
     */
    protected String getTypingLabel(Element element) {
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

    /**
     * Return the label of the subclassification of the given {@link Element}.
     *
     * @param usage
     *            the given {@link Element}.
     * @return the label of the subclassification of the given {@link Element} if there is one, an empty string
     *         otherwise.
     */
    protected String getSubclassificationLabel(Element element) {
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

    /**
     * Return the label of the subsetting of the given {@link Element}.
     *
     * @param usage
     *            the given {@link Element}.
     * @return the label of the subsetting of the given {@link Element} if there is one, an empty string otherwise.
     */
    protected String getSubsettingLabel(Element element) {
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

    /**
     * Return the label of the redefinition of the given {@link Element}.
     *
     * @param element
     *            the given {@link Element}.
     * @return the label of the redefinition of the given {@link Element} if there is one, an empty string otherwise.
     */
    protected String getRedefinitionLabel(Element element) {
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

    /**
     * Return the label of the value part of the given {@link Usage}.
     *
     * @param usage
     *            the given {@link Usage}.
     * @return the label of the value part of the given {@link Usage} if there is one, an empty string otherwise.
     */
    protected String getValueLabel(Usage usage) {
        StringBuilder label = new StringBuilder();
        var featureValue = usage.getOwnedRelationship().stream()
                .filter(FeatureValue.class::isInstance)
                .map(FeatureValue.class::cast)
                .findFirst();
        if (featureValue.isPresent()) {
            var expression = featureValue.get().getValue();
            String valueAsString = null;
            if (expression != null) {
                valueAsString = this.getValue(expression);
            }
            label
                    .append(LabelConstants.SPACE)
                    .append(LabelConstants.EQUAL)
                    .append(LabelConstants.SPACE)
                    .append(valueAsString);
        }
        return label.toString();
    }

    /**
     * Get the value of the given {@link Expression} as a string.
     *
     * @param expression
     *            the given {@link Expression}.
     * @return the value of the given {@link Expression} as a string.
     */
    protected String getValue(Expression expression) {
        String value = null;
        if (expression instanceof LiteralInteger literal) {
            value = String.valueOf(literal.getValue());
        } else if (expression instanceof LiteralRational literal) {
            value = String.valueOf(literal.getValue());
        } else if (expression instanceof LiteralBoolean literal) {
            value = String.valueOf(literal.isValue());
        } else if (expression instanceof LiteralString literal) {
            value = "\"" + String.valueOf(literal.getValue()) + "\"";
        } else if (expression instanceof LiteralInfinity) {
            value = "*";
        } else if (expression instanceof OperatorExpression operatorExpression) {
            value = this.getValue(operatorExpression);
        } else if (expression instanceof FeatureReferenceExpression featureReferenceExpression) {
            value = this.getValue(featureReferenceExpression);
        }
        return value;
    }

    private String getValue(OperatorExpression operatorExpression) {
        String value = null;
        if (operatorExpression instanceof FeatureChainExpression featureChainExpression) {
            value = this.getValue(featureChainExpression);
        } else {
            if (Objects.equals(operatorExpression.getOperator(), LabelConstants.OPEN_BRACKET)) {
                value = this.getValue(operatorExpression.getArgument().get(0))
                        + LabelConstants.SPACE
                        + LabelConstants.OPEN_BRACKET
                        + this.getValue(operatorExpression.getArgument().get(1))
                        + LabelConstants.CLOSE_BRACKET;
            } else if (List.of("<=", ">=", "<", ">", "==").contains(operatorExpression.getOperator())) {
                value = this.getValue(operatorExpression.getArgument().get(0))
                        + LabelConstants.SPACE
                        + operatorExpression.getOperator()
                        + LabelConstants.SPACE
                        + this.getValue(operatorExpression.getArgument().get(1));
            }
        }
        return value;
    }

    private String getValue(FeatureChainExpression featureChainExpression) {
        StringBuilder value = new StringBuilder();
        if (!featureChainExpression.getArgument().isEmpty()) {
            value.append(this.getValue(featureChainExpression.getArgument().get(0)));
            value.append(".");
        }
        Feature targetFeature = featureChainExpression.getTargetFeature();
        if (targetFeature.getChainingFeature().isEmpty()) {
            value.append(targetFeature.getName());
        } else {
            value.append(targetFeature.getChainingFeature().stream()
                    .map(Feature::getName)
                    .collect(Collectors.joining(".")));
        }
        return value.toString();
    }

    private String getValue(FeatureReferenceExpression featureReferenceExpression) {
        String value = null;
        boolean isInMeasurementExpression = EMFUtils.getAncestors(OperatorExpression.class, featureReferenceExpression,
                ancestor -> ancestor instanceof OperatorExpression operatorExpression && Objects.equals(operatorExpression.getOperator(), "[")).size() > 0;
        if (isInMeasurementExpression) {
            // We use short name for measurements if it exists
            value = featureReferenceExpression.getReferent().getShortName();
            if (value == null || value.isBlank()) {
                // If the short name is not set we use the regular name
                value = featureReferenceExpression.getReferent().getName();
            }
        } else {
            value = featureReferenceExpression.getReferent().getName();
        }
        return value;
    }

    protected String getDeclaredNameLabel(Element element) {
        var label = element.getName();
        if (ElementUtil.isFromStandardLibrary(element)) {
            label = element.getQualifiedName();
        }
        return label;

    }
}
