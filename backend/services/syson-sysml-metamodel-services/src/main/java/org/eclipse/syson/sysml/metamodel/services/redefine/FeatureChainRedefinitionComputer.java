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
package org.eclipse.syson.sysml.metamodel.services.redefine;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.syson.sysml.Behavior;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.ParameterMembership;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.Step;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.metamodel.services.MetamodelMutationElementService;

/**
 * Resolves and materializes a feature chain, undoing new memberships if any segment fails.
 *
 * @author Arthur Daussy
 */
public final class FeatureChainRedefinitionComputer {

    private final MetamodelMutationElementService metamodelMutationElementService;

    /**
     * Creates a computation that uses the initiating service to initialize local redefinitions.
     *
     * @param metamodelMutationElementService
     *            the non-null service that started the operation
     */
    public FeatureChainRedefinitionComputer(MetamodelMutationElementService metamodelMutationElementService) {
        this.metamodelMutationElementService = Objects.requireNonNull(metamodelMutationElementService);
    }

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
        FeatureChainRedefinitionResult result = this.validateRedefinitionChain(context, chain);
        if (result == null) {
            result = this.materializeFeatureChain(context, List.copyOf(chain));
        }
        return result;
    }

    /**
     * Creates a detached anonymous feature of the source's exact metaclass with its explicit Redefinition relationship.
     */
    private Feature createRedefiningFeature(Feature source) {
        Feature local = (Feature) SysmlFactory.eINSTANCE.create(source.eClass());
        Redefinition redefinition = SysmlFactory.eINSTANCE.createRedefinition();
        redefinition.setRedefiningFeature(local);
        redefinition.setRedefinedFeature(source);
        local.getOwnedRelationship().add(redefinition);
        return local;
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

    /**
     * Finds replacements by identity or semantic redefinition, never by name, without modifying existing features.
     */
    private List<Feature> findLocalRedefinitions(Type owner, Feature source) {
        return owner.getOwnedFeature().stream().filter(feature -> feature.allRedefinedFeatures().contains(source)).toList();
    }

    /**
     * Distinguishes behavior and step parameters from other directed usages, at the current local chain context.
     */
    private boolean isParameterSelection(Type owner, Feature source) {
        return (owner instanceof Behavior || owner instanceof Step)
                && (source.getDirection() != null || source.getOwningFeatureMembership() instanceof ParameterMembership);
    }

    /**
     * Records each new membership so a failure can undo the additions in reverse containment order.
     */
    private FeatureChainRedefinitionResult materializeFeatureChain(Type context, List<Feature> chain) {
        FeatureMembershipChanges created = new FeatureMembershipChanges();
        boolean success = false;
        try {
            FeatureChainRedefinitionResult result = this.redefineSegments(context, chain, created);
            success = result.isSuccess();
            return result;
        } finally {
            if (!success) {
                created.rollback();
            }
        }
    }

    /**
     * Validates a segment, then reuses its unique local replacement or creates a new one if the source is accessible.
     * Existing local features take precedence over inherited lookup, including selections made before a previous mutation.
     */
    private SegmentResult redefineSegment(Type owner, Feature source, FeatureMembershipChanges created) {
        String error = this.validateRedefinitionSegment(source);
        if (error == null && this.isParameterSelection(owner, source)) {
            return new ParameterRedefinitionComputer(owner, source, created).redefine();
        }
        Feature local = null;
        if (error == null) {
            List<Feature> localFeatures = this.findLocalRedefinitions(owner, source);
            if (localFeatures.size() > 1) {
                error = MessageFormat.format(
                        "Multiple local features redefine this feature: {0}. Resolve the conflicting redefinitions before trying again.",
                        localFeatures.stream().map(this::describeRedefinitionElement).toList());
            } else if (!localFeatures.isEmpty()) {
                local = localFeatures.getFirst();
            } else if (owner.getFeatureMembership().stream().noneMatch(membership -> membership.getOwnedMemberFeature() == source)) {
                error = "the feature is not accessible from this context. Select an accessible feature chain again.";
            } else {
                Feature redefiningFeature = this.createRedefiningFeature(source);
                RedefinitionStatus status = this.metamodelMutationElementService.initializeRedefiningFeature(redefiningFeature, source, owner);
                if (status.isSuccess()) {
                    created.attach(owner, redefiningFeature, source, owner.getOwnedRelationship().size());
                    local = redefiningFeature;
                } else {
                    error = status.message();
                }
            }
        }
        return new SegmentResult(local, error);
    }

    /**
     * Resolves segments against successive local owners, reusing existing features and attaching missing ones.
     * Adds the selected segment and root context to a failure cause before returning it to the caller.
     * Stops at the first failure; the returned chain contains only the selected segments, excluding additional parameters.
     */
    private FeatureChainRedefinitionResult redefineSegments(Type context, List<Feature> chain, FeatureMembershipChanges created) {
        List<Feature> localChain = new ArrayList<Feature>();
        Type owner = context;
        for (int index = 0; index < chain.size(); index++) {
            Feature source = chain.get(index);
            SegmentResult segment = this.redefineSegment(owner, source, created);
            if (segment.error() != null) {
                String message = MessageFormat.format("Cannot redefine segment {0} {1} in context {2}: {3}", index + 1,
                        this.describeRedefinitionElement(source), this.describeRedefinitionElement(context), segment.error());
                return FeatureChainRedefinitionResult.failure(message, context, index, source);
            }
            localChain.add(segment.feature());
            owner = segment.feature();
        }
        return FeatureChainRedefinitionResult.success(localChain);
    }

    /**
     * Validates the complete request before mutation and identifies a missing segment by its original index.
     *
     * @param context
     *            the requested root context, possibly null
     * @param chain
     *            the original selection, possibly null or containing null segments
     * @return the failed result with available location information, or null for a valid request
     */
    private FeatureChainRedefinitionResult validateRedefinitionChain(Type context, List<Feature> chain) {
        if (context == null) {
            return FeatureChainRedefinitionResult.failure("No configuration context was provided. Select a context before redefining a feature.");
        }
        String contextLabel = this.describeRedefinitionElement(context);
        FeatureChainRedefinitionResult failure = null;
        if (chain == null) {
            String message = MessageFormat.format("Cannot redefine a feature in context {0}: No feature chain was provided. Select a feature to redefine.", contextLabel);
            failure = FeatureChainRedefinitionResult.failure(message, context, null, null);
        } else if (chain.isEmpty()) {
            String message = MessageFormat.format("Cannot redefine a feature in context {0}: the selected feature chain is empty. Select a feature to redefine.", contextLabel);
            failure = FeatureChainRedefinitionResult.failure(message, context, null, null);
        } else {
            for (int index = 0; index < chain.size() && failure == null; index++) {
                if (chain.get(index) == null) {
                    String message = MessageFormat.format("Cannot redefine a feature in context {0}: segment {1} is missing. Select the feature chain again.", contextLabel, index + 1);
                    failure = FeatureChainRedefinitionResult.failure(message, context, index, null);
                }
            }
        }
        return failure;
    }

    /**
     * Checks whether the selected feature category and parameter configuration are supported at this chain position.
     */
    private String validateRedefinitionSegment(Feature source) {
        String error = null;
        if (source.getOwningFeatureMembership() instanceof ParameterMembership && source.getDirection() == null) {
            error = "the parameter has no direction. Specify its direction before redefining it.";
        } else if (!(source instanceof Usage)) {
            error = MessageFormat.format(
                    "redefining a {0} is not supported. Select a SysML usage.", source.eClass().getName());
        }
        return error;
    }
}
