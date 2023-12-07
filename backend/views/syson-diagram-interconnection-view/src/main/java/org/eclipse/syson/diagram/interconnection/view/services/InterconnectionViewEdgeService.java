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
package org.eclipse.syson.diagram.interconnection.view.services;

import java.util.Optional;

import org.eclipse.emf.common.util.EList;
import org.eclipse.syson.diagram.interconnection.view.InterconnectionViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.BindingConnectorAsUsage;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureChaining;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.ReferenceSubsetting;

/**
 * Edge-related Java services used by the {@link InterconnectionViewDiagramDescriptionProvider}.
 *
 * @author arichard
 */
public class InterconnectionViewEdgeService {

    /**
     * Return the source {@link PortUsage} of the given {@link BindingConnectorAsUsage}.
     *
     * @param bind
     *            the given {@link BindingConnectorAsUsage}.
     * @return the source {@link PortUsage} if found, <code>null</code> otherwise.
     */
    public PortUsage getSourcePort(BindingConnectorAsUsage bind) {
        PortUsage sourcePort = null;
        Optional<EndFeatureMembership> endFeatureMembership = bind.getOwnedFeatureMembership().stream()
            .filter(EndFeatureMembership.class::isInstance)
            .map(EndFeatureMembership.class::cast)
            .findFirst();
        if (endFeatureMembership.isPresent()) {
            Optional<Feature> referencedFeature = endFeatureMembership.get().getOwnedRelatedElement().stream()
                .filter(Feature.class::isInstance)
                .map(Feature.class::cast)
                .findFirst()
                .map(Feature::getOwnedReferenceSubsetting)
                .map(ReferenceSubsetting::getReferencedFeature);
            if (referencedFeature.isPresent()) {
                Feature feature = referencedFeature.get();
                if (feature instanceof PortUsage portUsage) {
                    sourcePort = portUsage;
                } else {
                    EList<FeatureChaining> ownedFeatureChaining = feature.getOwnedFeatureChaining();
                    FeatureChaining lastFeatureChaining = ownedFeatureChaining.get(Math.max(0, ownedFeatureChaining.size() - 1));
                    Feature chainingFeature = lastFeatureChaining.getChainingFeature();
                    if (chainingFeature instanceof PortUsage portUsage) {
                        sourcePort = portUsage;
                    }
                }
            }
        }
        return sourcePort;
    }

    /**
     * Return the source {@link PortUsage} of the given {@link BindingConnectorAsUsage}.
     *
     * @param bind
     *            the given {@link BindingConnectorAsUsage}.
     * @return the source {@link PortUsage} if found, <code>null</code> otherwise.
     */
    public PortUsage getTargetPort(BindingConnectorAsUsage bind) {
        PortUsage targetPort = null;
        Optional<EndFeatureMembership> endFeatureMembership = bind.getOwnedFeatureMembership().stream()
            .filter(EndFeatureMembership.class::isInstance)
            .map(EndFeatureMembership.class::cast)
            .reduce((first, second) -> second);
        if (endFeatureMembership.isPresent()) {
            Optional<Feature> referencedFeature = endFeatureMembership.get().getOwnedRelatedElement().stream()
                .filter(Feature.class::isInstance)
                .map(Feature.class::cast)
                .findFirst()
                .map(Feature::getOwnedReferenceSubsetting)
                .map(ReferenceSubsetting::getReferencedFeature);
            if (referencedFeature.isPresent()) {
                Feature feature = referencedFeature.get();
                if (feature instanceof PortUsage portUsage) {
                    targetPort = portUsage;
                } else {
                    EList<FeatureChaining> ownedFeatureChaining = feature.getOwnedFeatureChaining();
                    FeatureChaining lastFeatureChaining = ownedFeatureChaining.get(Math.max(0, ownedFeatureChaining.size() - 1));
                    Feature chainingFeature = lastFeatureChaining.getChainingFeature();
                    if (chainingFeature instanceof PortUsage portUsage) {
                        targetPort = portUsage;
                    }
                }
            }
        }
        return targetPort;
    }
}
