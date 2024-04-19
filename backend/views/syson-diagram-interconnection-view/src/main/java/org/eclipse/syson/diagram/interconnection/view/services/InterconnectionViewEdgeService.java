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
package org.eclipse.syson.diagram.interconnection.view.services;

import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.common.util.EList;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.diagram.common.view.services.ViewEdgeService;
import org.eclipse.syson.diagram.interconnection.view.InterconnectionViewDiagramDescriptionProvider;
import org.eclipse.syson.sysml.BindingConnectorAsUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureChaining;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.Usage;

/**
 * Edge-related Java services used by the {@link InterconnectionViewDiagramDescriptionProvider}.
 *
 * @author arichard
 */
public class InterconnectionViewEdgeService extends ViewEdgeService {

    private final IFeedbackMessageService feedbackMessageService;

    public InterconnectionViewEdgeService(IFeedbackMessageService feedbackMessageService) {
        this.feedbackMessageService = Objects.requireNonNull(feedbackMessageService);
    }

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
     * Return the target {@link PortUsage} of the given {@link BindingConnectorAsUsage}.
     *
     * @param bind
     *            the given {@link BindingConnectorAsUsage}.
     * @return the target {@link PortUsage} if found, <code>null</code> otherwise.
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

    /**
     * Set a new source {@link Element} for the given {@link BindingConnectorAsUsage}. Also move the given connector
     * into the parent (i.e. should be a PartUsage) of the new source.
     *
     * @param bind
     *            the given {@link BindingConnectorAsUsage}.
     * @param newSource
     *            the new target {@link Element}.
     * @return the given {@link BindingConnectorAsUsage}.
     */
    public BindingConnectorAsUsage setSourcePort(BindingConnectorAsUsage bind, Element newSource) {
        if (newSource instanceof PortUsage newSourcePort) {
            Optional<EndFeatureMembership> endFeatureMembership = bind.getOwnedFeatureMembership().stream()
                    .filter(EndFeatureMembership.class::isInstance)
                    .map(EndFeatureMembership.class::cast)
                    .findFirst();
            if (endFeatureMembership.isPresent()) {
                endFeatureMembership.get().getOwnedRelatedElement().stream()
                        .filter(Feature.class::isInstance)
                        .map(Feature.class::cast)
                        .findFirst()
                        .map(Feature::getOwnedReferenceSubsetting)
                        .ifPresent(refSub -> {
                            refSub.setReferencedFeature(newSourcePort);
                            // move the given connector into the parent (i.e. should be a PartUsage) of the new source
                            Usage owningUsage = newSourcePort.getOwningUsage();
                            if (owningUsage instanceof PartUsage partUsage) {
                                partUsage.getOwnedRelationship().add(bind.getOwningRelationship());
                            }
                        });

            }
        } else {
            this.feedbackMessageService.addFeedbackMessage(new Message("The source of the BindingConnectorAsUsage can only be connected to a PortUsage", MessageLevel.WARNING));
        }
        return bind;
    }

    /**
     * Set a new target {@link Element} for the given {@link BindingConnectorAsUsage}.
     *
     * @param bind
     *            the given {@link BindingConnectorAsUsage}.
     * @param newTarget
     *            the new target {@link Element}.
     * @return the given {@link BindingConnectorAsUsage}.
     */
    public BindingConnectorAsUsage setTargetPort(BindingConnectorAsUsage bind, Element newTarget) {
        if (newTarget instanceof PortUsage newTargetPort) {
            Optional<EndFeatureMembership> endFeatureMembership = bind.getOwnedFeatureMembership().stream()
                    .filter(EndFeatureMembership.class::isInstance)
                    .map(EndFeatureMembership.class::cast)
                    .reduce((first, second) -> second);
            if (endFeatureMembership.isPresent()) {
                endFeatureMembership.get().getOwnedRelatedElement().stream()
                        .filter(Feature.class::isInstance)
                        .map(Feature.class::cast)
                        .findFirst()
                        .map(Feature::getOwnedReferenceSubsetting)
                        .ifPresent(refSub -> refSub.setReferencedFeature(newTargetPort));
            }
        } else {
            this.feedbackMessageService.addFeedbackMessage(new Message("The target of the BindingConnectorAsUsage can only be connected to a PortUsage", MessageLevel.WARNING));
        }
        return bind;
    }
}
