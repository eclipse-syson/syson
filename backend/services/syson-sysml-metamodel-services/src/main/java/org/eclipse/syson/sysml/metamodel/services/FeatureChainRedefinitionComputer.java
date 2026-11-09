/*******************************************************************************
 * Copyright (c) 2025, 2026 Obeo.
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
package org.eclipse.syson.sysml.metamodel.services;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.syson.sysml.AttributeUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.Type;

/**
 * Resolves and materializes a feature chain, deferring containment changes until every segment is valid.
 *
 * @author Arthur Daussy
 */
public class FeatureChainRedefinitionComputer {

    /**
     * Performs the operation described by {@link MetamodelMutationElementService#redefineFeatureChain}.
     *
     * @param context
     *            the context to configure
     * @param chain
     *            the selected relative feature chain
     * @return the local chain on success, or user-facing failure messages
     */
    public FeatureChainRedefinitionResult compute(Type context, List<Feature> chain) {
        String error = this.validateRedefinitionChain(context, chain);
        FeatureChainRedefinitionResult result;
        if (error != null) {
            result = FeatureChainRedefinitionResult.failure(error);
        } else {
            result = this.materializeFeatureChain(context, List.copyOf(chain));
        }
        return result;
    }

    private FeatureChainRedefinitionResult materializeFeatureChain(Type context, List<Feature> chain) {
        var mutationService = new MetamodelMutationElementService();
        var localChain = new ArrayList<Feature>();
        var pending = new ArrayList<PendingFeature>();
        Type owner = context;
        String error = null;
        for (int index = 0; index < chain.size() && error == null; index++) {
            var source = chain.get(index);
            error = this.validateRedefinitionSegment(context, source, index + 1);
            if (error == null) {
                var localFeatures = owner.getOwnedFeature().stream()
                        .filter(feature -> feature == source || feature.redefines(source))
                        .toList();
                if (localFeatures.size() > 1) {
                    error = MessageFormat.format(
                            "Cannot redefine segment {0} {1} in context {2}: Multiple local features redefine this feature: {3}. Resolve the conflicting redefinitions before trying again.",
                            index + 1, this.describeRedefinitionElement(source), this.describeRedefinitionElement(context),
                            localFeatures.stream().map(this::describeRedefinitionElement).toList());
                } else if (!localFeatures.isEmpty()) {
                    var local = localFeatures.getFirst();
                    localChain.add(local);
                    owner = local;
                } else if (owner.getFeatureMembership().stream().noneMatch(membership -> membership.getOwnedMemberFeature() == source)) {
                    error = MessageFormat.format(
                            "Cannot redefine segment {0} {1} in context {2}: the feature is not accessible from this context. Select an accessible feature chain again.",
                            index + 1, this.describeRedefinitionElement(source), this.describeRedefinitionElement(context));
                } else {
                    var local = (Feature) SysmlFactory.eINSTANCE.create(source.eClass());
                    var redefinition = SysmlFactory.eINSTANCE.createRedefinition();
                    redefinition.setRedefiningFeature(local);
                    redefinition.setRedefinedFeature(source);
                    local.getOwnedRelationship().add(redefinition);
                    var status = mutationService.initializeRedefiningFeature(local, source, owner);
                    if (status.isSuccess()) {
                        pending.add(new PendingFeature(owner, local));
                        localChain.add(local);
                        owner = local;
                    } else {
                        error = MessageFormat.format("Cannot redefine segment {0} {1} in context {2}: {3}", index + 1,
                                this.describeRedefinitionElement(source), this.describeRedefinitionElement(context), status.message());
                    }
                }
            }
        }
        FeatureChainRedefinitionResult result;
        if (error == null) {
            pending.forEach(feature -> {
                var membership = SysmlFactory.eINSTANCE.createFeatureMembership();
                membership.getOwnedRelatedElement().add(feature.feature());
                feature.owner().getOwnedRelationship().add(membership);
            });
            result = FeatureChainRedefinitionResult.success(localChain);
        } else {
            result = FeatureChainRedefinitionResult.failure(error);
        }
        return result;
    }

    private String validateRedefinitionChain(Type context, List<Feature> chain) {
        if (context == null) {
            return "No configuration context was provided. Select a context before redefining a feature.";
        }
        String error = null;
        String contextLabel = this.describeRedefinitionElement(context);
        if (chain == null) {
            error = MessageFormat.format("Cannot redefine a feature in context {0}: No feature chain was provided. Select a feature to redefine.", contextLabel);
        } else if (chain.isEmpty()) {
            error = MessageFormat.format("Cannot redefine a feature in context {0}: the selected feature chain is empty. Select a feature to redefine.", contextLabel);
        } else {
            for (int index = 0; index < chain.size() && error == null; index++) {
                if (chain.get(index) == null) {
                    error = MessageFormat.format("Cannot redefine a feature in context {0}: segment {1} is missing. Select the feature chain again.", contextLabel, index + 1);
                }
            }
        }
        return error;
    }

    private String validateRedefinitionSegment(Type context, Feature source, int position) {
        String error = null;
        // shortcut: parameters and other feature categories are rejected until their dedicated implementation lots.
        if (source.getDirection() != null) {
            error = MessageFormat.format(
                    "Cannot redefine segment {0} {1} in context {2}: redefining a parameter is not supported yet because the required parameter order must be preserved.",
                    position, this.describeRedefinitionElement(source), this.describeRedefinitionElement(context));
        } else if (!(source instanceof AttributeUsage || source instanceof PartUsage)) {
            error = MessageFormat.format(
                    "Cannot redefine segment {0} {1} in context {2}: redefining a {3} is not supported yet. Only non-directed parts and attributes can currently be redefined.",
                    position, this.describeRedefinitionElement(source), this.describeRedefinitionElement(context), source.eClass().getName());
        }
        return error;
    }

    /**
     * Defers containment changes until every segment has been resolved against the prospective local chain.
     *
     * @author Arthur Daussy
     */
    private record PendingFeature(Type owner, Feature feature) {
    }

    private String describeRedefinitionElement(Element element) {
        String name = element.getQualifiedName();
        String label;
        if (name == null || name.isBlank()) {
            label = MessageFormat.format("unnamed {0} (id: {1})", element.eClass().getName(), element.getElementId());
        } else {
            label = MessageFormat.format("''{0}''", name);
        }
        return label;
    }

}
