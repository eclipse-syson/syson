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

import org.eclipse.syson.diagram.general.view.GeneralViewDiagramDescriptionProvider;
import org.eclipse.syson.services.LabelService;
import org.eclipse.syson.sysml.Dependency;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.FeatureValue;
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
}
