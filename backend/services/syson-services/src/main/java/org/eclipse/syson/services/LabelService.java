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

import java.util.Objects;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.syson.services.grammars.DirectEditLexer;
import org.eclipse.syson.services.grammars.DirectEditListener;
import org.eclipse.syson.services.grammars.DirectEditParser;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.LiteralBoolean;
import org.eclipse.syson.sysml.LiteralExpression;
import org.eclipse.syson.sysml.LiteralInfinity;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.LiteralRational;
import org.eclipse.syson.sysml.LiteralString;
import org.eclipse.syson.sysml.MultiplicityRange;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.Subclassification;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.Usage;
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
    public Element directEdit(Element element, String newLabel, String... options) {
        DirectEditLexer lexer = new DirectEditLexer(CharStreams.fromString(newLabel));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DirectEditParser parser = new DirectEditParser(tokens);
        ParseTree tree = parser.expression();
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
        if (element instanceof Usage usage && usage.isIsReference()) {
            builder.append(LabelConstants.REF);
            builder.append(LabelConstants.SPACE);
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
     * Return the label of the multiplicity part of the given {@link Element}.
     *
     * @param usage
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
            label.append(LabelConstants.CLOSE_BRACKET);
        }
        return label.toString();
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
            var literalExpression = featureValue.get().getValue();
            String valueAsString = null;
            if (literalExpression != null) {
                valueAsString = this.getValue(literalExpression);
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
     * @param literalExpression
     *            the given {@link Expression}.
     * @return the value of the given {@link Expression} as a string.
     */
    protected String getValue(Expression literalExpression) {
        String value = null;
        if (literalExpression instanceof LiteralInteger literal) {
            value = String.valueOf(literal.getValue());
        } else if (literalExpression instanceof LiteralRational literal) {
            value = String.valueOf(literal.getValue());
        } else if (literalExpression instanceof LiteralBoolean literal) {
            value = String.valueOf(literal.isValue());
        } else if (literalExpression instanceof LiteralString literal) {
            value = "\"" + String.valueOf(literal.getValue()) + "\"";
        } else if (literalExpression instanceof LiteralInfinity) {
            value = "*";
        }
        return value;
    }

    protected String getDeclaredNameLabel(Element element) {
        var label = element.getDeclaredName();
        if (ElementUtil.isFromStandardLibrary(element)) {
            label = element.getQualifiedName();
        }
        return label;

    }
}
