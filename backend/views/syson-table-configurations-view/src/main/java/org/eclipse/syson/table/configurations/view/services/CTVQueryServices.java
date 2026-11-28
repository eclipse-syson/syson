/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
package org.eclipse.syson.table.configurations.view.services;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.eclipse.syson.services.ContextualFeatureChain;
import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.metamodel.services.MetamodelQueryElementService;
import org.eclipse.syson.sysml.metamodel.services.NonStandardLibraryFeaturePredicate;
import org.eclipse.syson.sysml.metamodel.services.RequiresRedefinitionFeatureChainPredicate;

/**
 * Query services for the Configurations Table View.
 *
 * @author Jerome Gout
 */
public class CTVQueryServices {

    private final MetamodelQueryElementService metamodelQueryElementService = new MetamodelQueryElementService();

    /**
     * Collects configurable attributes and variations, including their shorthand redefinitions. Fixed local attribute
     * overrides remain visible even when {@link #isConfigurableFeature(Feature)} returns {@code false}; inherited fixed
     * attributes and ordinary parts are excluded without pruning their descendants.
     *
     * @param type
     *            the context from which feature chains are accessed
     * @return the feature chains sorted by escaped segment names, without inheritance markers
     */
    public List<ContextualFeatureChain> getConfigurableFeatureChains(Type type) {
        RequiresRedefinitionFeatureChainPredicate requiresRedefinition = new RequiresRedefinitionFeatureChainPredicate();
        return this.metamodelQueryElementService.streamFeatureChains(type, new NonStandardLibraryFeaturePredicate(),
                (context, chain) -> this.isConfigurableFeature(chain.getLast())
                        || this.isAttribute(chain.getLast()) && !chain.getLast().getOwnedRedefinition().isEmpty()
                        && !requiresRedefinition.test(context, chain))
                .map(features -> new ContextualFeatureChain(type, features))
                .sorted(Comparator.comparing(chain -> chain.features().stream().map(this::getFeatureName).collect(Collectors.joining("."))))
                .toList();
    }

    /**
     * Builds a dotted path using escaped names, falling back to short names or {@code <unnamed>}. Each segment requiring
     * redefinition in its accessing context is prefixed with {@code ^}; the context advances after each segment.
     *
     * @param chain
     *            the contextual feature chain to label
     * @return the path with per-segment inheritance markers
     */
    public String getFeatureChainLabel(ContextualFeatureChain chain) {
        RequiresRedefinitionFeatureChainPredicate requiresRedefinition = new RequiresRedefinitionFeatureChainPredicate();
        Type context = chain.context();
        StringJoiner name = new StringJoiner(".");
        for (Feature feature : chain.features()) {
            String segmentName = this.getFeatureName(feature);
            if (requiresRedefinition.test(context, List.of(feature))) {
                segmentName = '^' + segmentName;
            }
            name.add(segmentName);
            context = feature;
        }
        return name.toString();
    }

    /**
     * Determines whether a feature is an attribute without a value or with a default value, or a variation of any usage
     * kind. Redefined features supply the effective kind and value for shorthand redefinitions. Unlike row selection,
     * this predicate excludes fixed local attribute overrides.
     *
     * @param feature
     *            the feature whose configurability is evaluated
     * @return whether the feature is configurable
     */
    public boolean isConfigurableFeature(Feature feature) {
        // A declaration such as ':>> parameter;' creates a ReferenceUsage even when parameter is an attribute or a variation.
        // Check the redefined features as well to recognize these configurable parameters.
        return this.isAttribute(feature) && this.getEffectiveFeatureValue(feature)
                .map(FeatureValue::isIsDefault)
                .orElse(true)
                || this.isVariation(feature);
    }

    private Optional<FeatureValue> getEffectiveFeatureValue(Feature feature) {
        return feature.allRedefinedFeatures().stream()
                .flatMap(candidate -> candidate.getOwnedRelationship().stream())
                .filter(FeatureValue.class::isInstance)
                .map(FeatureValue.class::cast)
                .findFirst();
    }

    private String getFeatureName(Feature feature) {
        return Optional.ofNullable(feature.escapedName()).filter(name -> !name.isEmpty()).orElse("<unnamed>");
    }

    private boolean isAttribute(Feature feature) {
        return feature.allRedefinedFeatures().stream().anyMatch(AttributeUsage.class::isInstance);
    }

    private boolean isVariation(Feature feature) {
        return feature.allRedefinedFeatures().stream().anyMatch(candidate -> candidate instanceof Usage usage && usage.isIsVariation());
    }

}
