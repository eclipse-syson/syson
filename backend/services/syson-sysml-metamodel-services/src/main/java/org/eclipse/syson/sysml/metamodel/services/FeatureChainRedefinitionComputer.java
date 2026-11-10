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

import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.StateSubactionMembership;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.TransitionFeatureMembership;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;

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
                        pending.add(new PendingFeature(owner, local, source.getOwningFeatureMembership()));
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
                var membership = this.createRedefinitionMembership(feature.sourceMembership());
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
        // shortcut: parameters are rejected until ordered parameter redefinition is implemented in its dedicated lot.
        if (source.getDirection() != null || source.getOwningFeatureMembership() instanceof ParameterMembership) {
            error = MessageFormat.format(
                    "Cannot redefine segment {0} {1} in context {2}: redefining a parameter is not supported yet because the required parameter order must be preserved.",
                    position, this.describeRedefinitionElement(source), this.describeRedefinitionElement(context));
        } else if (!(source instanceof Usage)) {
            error = MessageFormat.format(
                    "Cannot redefine segment {0} {1} in context {2}: redefining a {3} is not supported. Select a non-directed SysML usage.",
                    position, this.describeRedefinitionElement(source), this.describeRedefinitionElement(context), source.eClass().getName());
        }
        return error;
    }

    private FeatureMembership createRedefinitionMembership(FeatureMembership source) {
        // Preserve the source's semantic membership role, including ends (KerML 1.1 Beta 2, 8.3.3.3.3), without copying its contents.
        FeatureMembership local;
        if (source == null) {
            local = SysmlFactory.eINSTANCE.createFeatureMembership();
        } else {
            local = (FeatureMembership) SysmlFactory.eINSTANCE.create(source.eClass());
        }
        if (source instanceof StateSubactionMembership subaction) {
            // SysML 2.1 Beta 2, 8.3.18.4: kind distinguishes entry, do and exit actions; retaining the role is service policy.
            ((StateSubactionMembership) local).setKind(subaction.getKind());
        } else if (source instanceof RequirementConstraintMembership constraint) {
            // SysML 2.1 Beta 2, 8.3.21.7: kind distinguishes assumed and required constraints; retaining the role is service policy.
            ((RequirementConstraintMembership) local).setKind(constraint.getKind());
        } else if (source instanceof TransitionFeatureMembership transition) {
            // SysML 2.1 Beta 2, 8.3.18.8: kind distinguishes transition triggers, guards and effects; retaining the role is service policy.
            ((TransitionFeatureMembership) local).setKind(transition.getKind());
        }
        return local;
    }

    /**
     * Defers containment changes until every segment has been resolved against the prospective local chain.
     *
     * @author Arthur Daussy
     */
    private record PendingFeature(Type owner, Feature feature, FeatureMembership sourceMembership) {
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
