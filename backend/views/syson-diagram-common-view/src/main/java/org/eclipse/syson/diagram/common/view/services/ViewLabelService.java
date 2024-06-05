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

import java.util.function.BinaryOperator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.syson.services.LabelService;
import org.eclipse.syson.sysml.AcceptActionUsage;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.Dependency;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.StateSubactionMembership;
import org.eclipse.syson.sysml.Succession;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.helper.LabelConstants;

/**
 * Label-related Java services used in diagrams.
 *
 * @author arichard
 */
public class ViewLabelService extends LabelService {

    /**
     * The default separator used when printing a set of Guard Expressions for a TransitionUsage label
     */
    private static final String GUARD_EXPRESSION_SEPARATOR = "& ";

    public ViewLabelService(IFeedbackMessageService feedbackMessageService) {
        super(feedbackMessageService);
    }

    /**
     * Return the label for the given {@link Usage}.
     *
     * @param usage
     *            the given {@link Usage}.
     * @return the label for the given {@link Usage}.
     */
    public String getCompartmentItemUsageLabel(Usage usage) {
        StringBuilder label = new StringBuilder();
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
        label.append(this.getCompartmentItemUsageLabel(usage));
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
     * Get the value to display when a direct edit has been called on the given {@link Usage}.
     *
     * @param usage
     *            the given {@link Usage}.
     * @return the value to display.
     */
    public String getUsageInitialDirectEditLabel(Usage usage) {
        return this.getCompartmentItemUsageLabel(usage);
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

    private String getGuardExpressionsDefaultDirectEditLabel(EList<Expression> guardExpressions) {
        var guardLabel = this.getElementsDefaultInitialDirectEditLabel(guardExpressions, (a, b) -> {
            return a + GUARD_EXPRESSION_SEPARATOR + b;
        });
        return guardLabel;
    }
}
