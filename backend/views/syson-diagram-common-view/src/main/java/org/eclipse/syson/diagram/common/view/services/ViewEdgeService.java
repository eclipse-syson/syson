/*******************************************************************************
 * Copyright (c) 2024, 2026 Obeo.
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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.components.core.api.IFeedbackMessageService;
import org.eclipse.sirius.components.diagrams.elements.NodeElementProps;
import org.eclipse.sirius.components.diagrams.renderer.DiagramRenderingCache;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.AllocationUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.Expression;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureChainExpression;
import org.eclipse.syson.sysml.FeatureChaining;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureReferenceExpression;
import org.eclipse.syson.sysml.FeatureValue;
import org.eclipse.syson.sysml.FlowUsage;
import org.eclipse.syson.sysml.IncludeUseCaseUsage;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.Succession;
import org.eclipse.syson.sysml.SuccessionAsUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.UseCaseUsage;
import org.eclipse.syson.sysml.metamodel.services.MetamodelMutationElementService;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Edge-related Java services used by several diagrams.
 *
 * @author Jerome Gout
 */
public class ViewEdgeService {

    protected final IFeedbackMessageService feedbackMessageService;

    private final UtilService utilService;

    private final MetamodelMutationElementService metamodelMutationElementService;

    public ViewEdgeService(IFeedbackMessageService feedbackMessageService) {
        this.metamodelMutationElementService = new MetamodelMutationElementService();
        this.utilService = new UtilService();
        this.feedbackMessageService = Objects.requireNonNull(feedbackMessageService);
    }

    /**
     * Get all reachable allocate edge in the {@link ResourceSet} of given {@link EObject}.
     *
     * @param eObject
     *            the {@link EObject} stored in a {@link ResourceSet}
     * @return a list of allocate edge objects
     */
    public List<AllocationUsage> getAllReachableAllocateEdges(EObject eObject) {
        String type = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getAllocationUsage());
        var allAllocationUsages = this.utilService.getAllReachable(eObject, type);
        return allAllocationUsages.stream()
                .filter(AllocationUsage.class::isInstance)
                .map(AllocationUsage.class::cast)
                .filter(au -> this.isAnAllocateEdge(au))
                .toList();
    }

    /**
     * Check if the given source and target {@link org.eclipse.sirius.components.representations.Element} or contained
     * by the same graphical container.
     *
     * @param source
     *            the source element
     * @param target
     *            the target element
     * @param cache
     *            the DiagramRenderingCache
     * @return <code>true</code> if contained by the same container
     */
    public boolean isInSameGraphicalContainer(org.eclipse.sirius.components.representations.Element source, org.eclipse.sirius.components.representations.Element target, DiagramRenderingCache cache) {
        if (source.getProps() instanceof NodeElementProps sourceNodeProps
                && target.getProps() instanceof NodeElementProps targetNodeProps) {
            String sourceParentId = this.getParentId(cache, sourceNodeProps);
            String targetParentId = this.getParentId(cache, targetNodeProps);
            return (sourceParentId == null && targetParentId == null) // Root nodes
                    || (sourceParentId != null && sourceParentId.equals(targetParentId)); // Sub nodes
        }
        return true;
    }

    public Element getSourceAllocateEdge(AllocationUsage allocationUsage) {
        var features = this.getFeatures(allocationUsage);
        if (features.size() == 2) {
            return this.getFeatureElement(features.get(0));
        }
        return null;
    }

    public Element getTargetAllocateEdge(AllocationUsage allocationUsage) {
        var features = this.getFeatures(allocationUsage);
        if (features.size() == 2) {
            return this.getFeatureElement(features.get(1));
        }
        return null;
    }

    public Element reconnectSourceAllocateEdge(AllocationUsage self, Element newSource) {
        if (newSource instanceof Usage usage) {
            var features = this.getFeatures(self);
            if (features.size() == 2) {
                var reference = features.get(0).getOwnedReferenceSubsetting();
                if (reference != null) {
                    reference.setReferencedFeature(usage);
                }
            }
        }
        return self;
    }

    public Element reconnectTargetAllocateEdge(AllocationUsage self, Element newTarget) {
        if (newTarget instanceof Usage usage) {
            var features = this.getFeatures(self);
            if (features.size() == 2) {
                var reference = features.get(1).getOwnedReferenceSubsetting();
                if (reference != null) {
                    reference.setReferencedFeature(usage);
                }
            }
        }
        return self;
    }

    private boolean isAnAllocateEdge(AllocationUsage allocationUsage) {
        // an allocate edge is an AllocationUsage that contains 2 EndFeaturesMembership
        return this.getFeatures(allocationUsage).size() == 2;
    }

    private List<Feature> getFeatures(AllocationUsage allocationUsage) {
        var features = allocationUsage.getOwnedFeatureMembership().stream()
                .filter(EndFeatureMembership.class::isInstance)
                .map(EndFeatureMembership.class::cast)
                .flatMap(efm -> efm.getOwnedRelatedElement().stream())
                .filter(Feature.class::isInstance)
                .map(Feature.class::cast)
                .toList();
        return features;
    }

    private Element getFeatureElement(Feature feature) {
        var reference = feature.getOwnedReferenceSubsetting();
        if (reference != null) {
            return this.resolveFeatureElement(reference.getReferencedFeature());
        }
        return null;
    }

    private Usage resolveFeatureElement(Feature feature) {
        Usage result = null;
        if (feature instanceof Usage usage) {
            result = usage;
        } else if (feature != null) {
            // need to be resolved, retrieve the last feature chaining 's target
            Optional<FeatureChaining> lastChaining = feature.getOwnedRelationship().stream()
                    .filter(FeatureChaining.class::isInstance)
                    .map(FeatureChaining.class::cast)
                    .reduce((acc, element) -> element);
            if (lastChaining.isPresent()) {
                result = lastChaining.get().getTarget().stream()
                        .filter(Usage.class::isInstance)
                        .map(Usage.class::cast)
                        .findFirst()
                        .orElse(null);
            }
        }
        return result;
    }

    public Element reconnectSourceSuccessionEdge(SuccessionAsUsage succession, Element oldSource, Element newSource) {
        EList<Feature> ends = succession.getConnectorEnd();
        if (!ends.isEmpty()) {
            Feature sourceEnd = ends.get(0);
            this.setConnectorEndFeature(sourceEnd, newSource);
        }
        return succession;
    }

    public Element reconnectTargetSuccessionEdge(SuccessionAsUsage succession, Element oldTarget, Element newTarget) {
        EList<Feature> ends = succession.getConnectorEnd();
        if (ends.size() > 1) {
            Feature sourceEnd = ends.get(1);
            this.setConnectorEndFeature(sourceEnd, newTarget);
        }
        return succession;
    }

    /**
     * Redefines the target feature of the given source end.
     *
     * @param sourceEnd
     *            the source end to modify
     * @param newTargetFeature
     *            the new target feature
     */
    private void setConnectorEndFeature(Feature sourceEnd, Element newTargetFeature) {
        if (sourceEnd instanceof ReferenceUsage refUsage) {
            ReferenceSubsetting referenceSubsetting = sourceEnd.getOwnedReferenceSubsetting();
            if (referenceSubsetting == null || referenceSubsetting.isIsImplied()) {
                // Needs to create a new one because this one is inherited
                referenceSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
                referenceSubsetting.setSubsettingFeature(refUsage);
            }
            this.getAction(newTargetFeature).ifPresent(referenceSubsetting::setReferencedFeature);
        }
    }

    /**
     * Set a new source {@link ActionUsage} for the given {@link TransitionUsage}. Used by
     * {@code TransitionEdgeDescriptionProvider.createSourceReconnectTool()}
     *
     * @param transition
     *            the given {@link TransitionUsage}.
     * @param newSource
     *            the new target {@link ActionUsage}.
     * @return the given {@link TransitionUsage}.
     */
    public TransitionUsage reconnectSourceTransitionEdge(TransitionUsage transition, ActionUsage newSource) {
        if (!(newSource instanceof ActionUsage) || newSource instanceof TransitionUsage || !this.checkTransitionEdgeTarget(newSource, transition.getTarget())) {
            this.feedbackMessageService.addFeedbackMessage(new Message("Invalid new source for transition", MessageLevel.WARNING));
            return transition;
        }
        // Update transition source
        transition.getOwnedMembership().stream()
                .filter(Membership.class::isInstance)
                .map(membership -> membership)
                .findFirst()
                .ifPresent(mem -> mem.setMemberElement(newSource));
        // Update succession source
        Succession succession = transition.getSuccession();
        succession.getFeatureMembership().stream()
                .filter(EndFeatureMembership.class::isInstance)
                .map(EndFeatureMembership.class::cast)
                .findFirst()
                .ifPresent(endFeat -> {
                    endFeat.getOwnedRelatedElement().stream()
                            .findFirst()
                            .ifPresent(feat -> feat.getOwnedRelationship().stream()
                                    .filter(ReferenceSubsetting.class::isInstance)
                                    .map(ReferenceSubsetting.class::cast)
                                    .findFirst()
                                    .ifPresent(refSub -> refSub.setReferencedFeature(newSource)));
                });
        return transition;
    }

    /**
     * Reconnects the source of an {@link IncludeUseCaseUsage}.
     *
     * @param includeUseCaseUsage
     *            the include to reconnect
     * @param newSource
     *            the new source
     * @return the {@link IncludeUseCaseUsage}.
     */
    public IncludeUseCaseUsage reconnectSourceIncludeUseCaseUsage(IncludeUseCaseUsage includeUseCaseUsage, UseCaseUsage newSource) {
        OwningMembership owningMembership = includeUseCaseUsage.getOwningMembership();
        newSource.getOwnedRelationship().add(owningMembership);
        return includeUseCaseUsage;
    }
    /**
     * Reconnects the target of an {@link IncludeUseCaseUsage}.
     *
     * @param includeUseCaseUsage
     *            the include to reconnect
     * @param newTarget
     *            the target source
     * @return the {@link IncludeUseCaseUsage}.
     */
    public IncludeUseCaseUsage reconnectTargetIncludeUseCaseUsage(IncludeUseCaseUsage includeUseCaseUsage, UseCaseUsage newTarget) {

        ReferenceSubsetting refSubSetting = includeUseCaseUsage.getOwnedReferenceSubsetting();
        if (refSubSetting != null) {
            refSubSetting.setReferencedFeature(newTarget);
        }
        return includeUseCaseUsage;
    }

    /**
     * Set a new target {@link ActionUsage} for the given {@link TransitionUsage}. Used by
     * {@code TransitionEdgeDescriptionProvider.createTargetReconnectTool()}
     *
     * @param transition
     *            the given {@link TransitionUsage}.
     * @param newTarget
     *            the new target {@link ActionUsage}.
     * @return the given {@link TransitionUsage}.
     */
    public TransitionUsage reconnectTargetTransitionEdge(TransitionUsage transition, ActionUsage newTarget) {
        if (!(newTarget instanceof ActionUsage) || newTarget instanceof TransitionUsage || !this.checkTransitionEdgeTarget(transition.getSource(), newTarget)) {
            this.feedbackMessageService.addFeedbackMessage(new Message("Invalid new target for transition", MessageLevel.WARNING));
            return transition;
        }
        // Update succession target
        Succession succession = transition.getSuccession();
        List<EndFeatureMembership> succFeatMemberships = succession.getFeatureMembership().stream()
                .filter(EndFeatureMembership.class::isInstance)
                .map(EndFeatureMembership.class::cast)
                .toList();
        if (succFeatMemberships.size() > 1) {
            succFeatMemberships.get(1).getOwnedRelatedElement().stream()
                    .findFirst()
                    .ifPresent(feat -> feat.getOwnedRelationship().stream()
                            .filter(ReferenceSubsetting.class::isInstance)
                            .map(ReferenceSubsetting.class::cast)
                            .findFirst()
                            .ifPresent(refSub -> refSub.setReferencedFeature(newTarget)));
        }
        return transition;
    }

    /**
     * TransitionUsage edge target checking. Used as a precondition expression for the TransitionEdgeDescriptionProvider
     * but does not seems to be used to filter the {@link TransitionUsage} creation.
     *
     * @param source
     *            The source of the transition
     * @param target
     *            The target of the transition
     * @return
     */
    private boolean checkTransitionEdgeTarget(Element source, Element target) {
        boolean sameParent = false;
        if (source instanceof ActionUsage sourceAction && target instanceof ActionUsage targetAction) {
            Element sourceParentElement = sourceAction.getOwner();
            Element targetParentElement = targetAction.getOwner();
            sameParent = sourceParentElement == targetParentElement;
        }
        boolean parentIsParallel = this.utilService.isParallelState(source.getOwner());
        return sameParent && !parentIsParallel;
    }

    /**
     * Checks that if a given transition is targeting a {@link StateUsage}.
     *
     * @param transition
     *            a transition
     * @return <code>true</code> if targeting a {@link StateUsage}.
     */
    public boolean isTransitionUsageForState(TransitionUsage transition) {
        return transition.getTarget() instanceof StateUsage;
    }

    /**
     * Unwrap the given {@link Feature} to its referenced element. If the feature is a {@link ReferenceUsage} return the
     * reference feature.
     *
     * @param input
     *            the input {@link Feature}
     * @return a {@link Feature} or <code>null</code>
     */
    public Feature unwrapFeature(Feature input) {
        if (input instanceof ReferenceUsage) {
            return input.getOwnedRedefinition().stream()
                    .map(Redefinition::getRedefinedFeature)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        } else {
            return input;
        }
    }

    /**
     * Get the target of a {@link FeatureValue} edge.
     *
     * @param featureValue
     *            a {@link FeatureValue}
     * @return a target or <code>null</code>.
     */
    public Feature getFeatureValueTarget(FeatureValue featureValue) {
        Expression value = featureValue.getValue();
        Feature result = null;
        if (!featureValue.isIsDefault()) {
            // Only handle FeatureChainExpression and FeatureReferenceExpression for the moment
            if (value instanceof FeatureChainExpression featureChainExpression) {
                Feature targetFeature = featureChainExpression.getTargetFeature();
                if (targetFeature != null) {
                    result = targetFeature.getFeatureTarget();
                }
            } else if (value instanceof FeatureReferenceExpression featureRef) {
                result = featureRef.getReferent();
            }
        }
        return result;
    }

    /**
     * Reconnects the source of a {@link FlowUsage}.
     *
     * @param flow
     *            a flow
     * @param newSource
     *            the new source
     * @return the flow itself
     */
    // @technical-debt At some point this implementation should be aligned with org.eclipse.syson.diagram.services.DiagramMutationElementService.reconnectSource
    public FlowUsage reconnectSource(FlowUsage flow, Feature newSource) {
        EList<FeatureMembership> featureMemberships = flow.getFeatureMembership();
        if (!featureMemberships.isEmpty()) {
            FeatureMembership toDelete = featureMemberships.get(0);
            int index = flow.getOwnedRelationship().indexOf(toDelete);
            flow.getOwnedRelationship().remove(index);
            flow.getOwnedRelationship().add(index, this.metamodelMutationElementService.createFlowConnectionEnd(newSource));
        }
        return flow;
    }

    /**
     * Reconnects the target of a {@link FlowUsage}.
     *
     * @param flow
     *            a flow
     * @param newTarget
     *            the new target
     * @return the flow itself
     */
    // @technical-debt At some point this implementation should be aligned with org.eclipse.syson.diagram.services.DiagramMutationElementService.reconnectTarget
    public FlowUsage reconnectTarget(FlowUsage flow, Feature newTarget) {
        EList<FeatureMembership> featureMemberships = flow.getFeatureMembership();
        if (featureMemberships.size() >= 2) {
            FeatureMembership toDelete = featureMemberships.get(1);
            int index = flow.getOwnedRelationship().indexOf(toDelete);
            flow.getOwnedRelationship().remove(index);
            flow.getOwnedRelationship().add(index, this.metamodelMutationElementService.createFlowConnectionEnd(newTarget));
        }
        return flow;
    }

    private Optional<ActionUsage> getAction(Element element) {
        Optional<ActionUsage> result = Optional.empty();
        if (element instanceof Membership membership) {
            // this is a standard start or done action, the actual action is inside the memberElement feature.
            if (membership.getMemberElement() instanceof ActionUsage au) {
                result = Optional.of(au);
            }
        } else if (element instanceof ActionUsage au) {
            result = Optional.of(au);
        }
        return result;
    }

    private String getParentId(DiagramRenderingCache cache, NodeElementProps sourceNodeProps) {
        return cache.getParent(sourceNodeProps.getId())
                .map(org.eclipse.sirius.components.representations.Element::getProps)
                .map(props -> {
                    if (props instanceof NodeElementProps nodeProps) {
                        return nodeProps.getId();
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .orElse(null);
    }
}
