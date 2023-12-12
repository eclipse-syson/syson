/*******************************************************************************
 * Copyright (c) 2023 Obeo.
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
package org.eclipse.syson.diagram.general.view.services;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.diagram.general.view.directedit.GeneralViewDirectEditListener;
import org.eclipse.syson.diagram.general.view.directedit.grammars.DirectEditLexer;
import org.eclipse.syson.diagram.general.view.directedit.grammars.DirectEditListener;
import org.eclipse.syson.diagram.general.view.directedit.grammars.DirectEditParser;
import org.eclipse.syson.services.LabelService;
import org.eclipse.syson.sysml.Dependency;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.LiteralBoolean;
import org.eclipse.syson.sysml.LiteralInteger;
import org.eclipse.syson.sysml.LiteralRational;
import org.eclipse.syson.sysml.LiteralString;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.Subsetting;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.util.LabelConstants;

/**
 * Label-related Java services used by the {@link GeneralViewDiagramDescriptionProvider}.
 *
 * @author arichard
 */
public class GeneralViewLabelService extends LabelService {

    /**
     * Return the label for the given {@link Usage}.
     *
     * @param usage
     *            the given {@link Usage}.
     * @return the label for the given {@link Usage}.
     */
    public String getCompartmentItemUsageLabel(Usage usage) {
        StringBuilder label = new StringBuilder();
        label
            .append(usage.getDeclaredName())
            .append(this.getTypingLabel(usage))
            .append(this.getRedefinitionLabel(usage))
            .append(this.getSubsettingLabel(usage))
            .append(this.getValueLabel(usage));
        return label.toString();
    }

    /**
     * Return the label of the value part of the given {@link Usage}.
     *
     * @param usage
     *            the given {@link Usage}.
     * @return the label of the value part of the given {@link Usage} if there is one, an empty string otherwise.
     */
    private String getValueLabel(Usage usage) {
        StringBuilder label = new StringBuilder();
        var featureValue = usage.getOwnedRelationship().stream()
                .filter(FeatureValue.class::isInstance)
                .map(FeatureValue.class::cast)
                .findFirst();
        if (featureValue.isPresent()) {
            var literalExpression = featureValue.get().getValue();
            String valueAsString = null;
            if (literalExpression != null) {
                valueAsString = getValue(literalExpression);
            }
            label
                .append(LabelConstants.SPACE)
                .append(LabelConstants.EQUAL)
                .append(LabelConstants.SPACE)
                .append(valueAsString);
        }
        return label.toString();
    }

    private String getValue(Expression literalExpression) {
        String value = null;
        if (literalExpression instanceof LiteralInteger literal) {
            value = String.valueOf(literal.getValue());
        } else if (literalExpression instanceof LiteralRational literal) {
            value = String.valueOf(literal.getValue());
        } else if (literalExpression instanceof LiteralBoolean literal) {
            value = String.valueOf(literal.isValue());
        } else if (literalExpression instanceof LiteralString literal) {
            value = String.valueOf(literal.getValue());
        }
        return value;
    }

    /**
     * Return the label of the typing part of the given {@link Element}.
     *
     * @param usage
     *            the given {@link Element}.
     * @return the label of the typing part of the given {@link Element} if there is one, an empty string otherwise.
     */
    private String getTypingLabel(Element element) {
        StringBuilder label = new StringBuilder();
        var featureTyping = element.getOwnedRelationship().stream()
                .filter(FeatureTyping.class::isInstance)
                .map(FeatureTyping.class::cast)
                .findFirst();
        if (featureTyping.isPresent()) {
            var type = featureTyping.get().getType();
            String typeName = null;
            if (type != null) {
                typeName = type.getDeclaredName();
            }
            label
                .append(LabelConstants.SPACE)
                .append(LabelConstants.COLON)
                .append(LabelConstants.SPACE)
                .append(typeName);
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
    private String getSubsettingLabel(Element element) {
        StringBuilder label = new StringBuilder();
        var subsetting = element.getOwnedRelationship().stream()
                .filter(r -> r instanceof Subsetting && !(r instanceof Redefinition))
                .map(Subsetting.class::cast)
                .findFirst();
        if (subsetting.isPresent()) {
            var subsettedFeature = subsetting.get().getSubsettedFeature();
            String subsettedFeatureName = null;
            if (subsettedFeature != null) {
                subsettedFeatureName = subsettedFeature.getDeclaredName();
            }
            label
                .append(LabelConstants.SPACE)
                .append(LabelConstants.SUBSETTING)
                .append(LabelConstants.SPACE)
                .append(subsettedFeatureName);
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
    private String getRedefinitionLabel(Element element) {
        StringBuilder label = new StringBuilder();
        var redefinition = element.getOwnedRelationship().stream()
                .filter(Redefinition.class::isInstance)
                .map(Redefinition.class::cast)
                .findFirst();
        if (redefinition.isPresent()) {
            var redefinedFeature = redefinition.get().getRedefinedFeature();
            if (redefinedFeature != null) {
                label
                    .append(LabelConstants.SPACE)
                    .append(LabelConstants.REDEFINITION)
                    .append(LabelConstants.SPACE)
                    .append(redefinedFeature.getDeclaredName());
            }
        }
        return label.toString();
    }

    /**
     * Return the edge label for the given {@link Dependency}.
     *
     * @param dependency
     *            the given {@link Dependency}.
     * @return the edge label for the given {@link Dependency}.
     */
    public String getDependencyEdgeLabel(Dependency dependency) {
        return LabelConstants.OPEN_QUOTE + "dependency" + LabelConstants.CLOSE_QUOTE + LabelConstants.CR + dependency.getDeclaredName();
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
        DirectEditLexer lexer = new DirectEditLexer(CharStreams.fromString(newLabel));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DirectEditParser parser = new DirectEditParser(tokens);
        ParseTree tree = parser.expression();
        ParseTreeWalker walker = new ParseTreeWalker();
        DirectEditListener listener = new GeneralViewDirectEditListener(element);
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
        builder.append(element.getDeclaredName());
        builder.append(this.getTypingLabel(element));
        builder.append(this.getRedefinitionLabel(element));
        builder.append(this.getSubsettingLabel(element));
        return builder.toString();
    }

    /**
     * Get the value to display when a direct edit has been called on the given {@link Usage}.
     *
     * @param usage
     *            the given {@link Usage}.
     * @return the value to display.
     */
    public String getUsageInitialDirectEditLabel(Usage usage) {
        return this.getCompartmentItemUsageLabel(usage);
    }
}
