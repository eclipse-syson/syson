/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.AllocationUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureChaining;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.Succession;
import org.eclipse.syson.sysml.SuccessionAsUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.sysml.helper.EMFUtils;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Edge-related Java services used by several diagrams.
 *
 * @author Jerome Gout
 */
public class ViewEdgeService {


    protected final IFeedbackMessageService feedbackMessageService;

    private final UtilService utilService;

    public ViewEdgeService(IFeedbackMessageService feedbackMessageService) {
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
     * Get all reachable {@link AllocationUsage} in the {@link ResourceSet} of given {@link EObject}.
     *
     * @param eObject
     *            the {@link EObject} stored in a {@link ResourceSet}
     * @return a list of {@link AllocationUsage} objects
     */
    public List<AllocationUsage> getAllReachableAllocationUsages(EObject eObject) {
        String type = SysMLMetamodelHelper.buildQualifiedName(SysmlPackage.eINSTANCE.getAllocationUsage());
        var allAllocationUsages = this.utilService.getAllReachable(eObject, type);
        return allAllocationUsages.stream()
                .filter(AllocationUsage.class::isInstance)
                .map(AllocationUsage.class::cast)
                .filter(au -> !this.isAnAllocateEdge(au))
                .toList();
    }

    public Element getSource(SuccessionAsUsage succession) {
        Feature sourceFeature = succession.getSourceFeature();
        String membershipAliasId = null;

        EList<Feature> ends = succession.getConnectorEnd();
        if (!ends.isEmpty()) {
            Feature firstEnd = ends.get(0);
            if (firstEnd instanceof ReferenceUsage refUsage) {
                EList<String> aliasId = refUsage.getAliasIds();
                if (!aliasId.isEmpty()) {
                    membershipAliasId = aliasId.get(0);
                }
            }
        }

        if (membershipAliasId != null && sourceFeature != null) {
            // Check for referencing membership
            Membership membership = this.getReferencingMembershipWithId(sourceFeature, membershipAliasId);
            if (membership != null) {
                return membership;
            }
        }
        return sourceFeature;
    }

    public Element getTarget(SuccessionAsUsage succession) {
        Feature targetFeature = null;
        EList<Feature> targetFeatures = succession.getTargetFeature();
        if (!targetFeatures.isEmpty()) {

            targetFeature = targetFeatures.get(0);
            String membershipAliasId = null;
            EList<Feature> ends = succession.getConnectorEnd();
            if (ends.size() > 1) {
                Feature firstEnd = ends.get(1);
                if (firstEnd instanceof ReferenceUsage refUsage) {
                    EList<String> aliasId = refUsage.getAliasIds();
                    if (!aliasId.isEmpty()) {
                        membershipAliasId = aliasId.get(0);
                    }
                }
            }

            if (membershipAliasId != null) {
                // Check for referencing membership
                Membership membeship = this.getReferencingMembershipWithId(targetFeature, membershipAliasId);
                if (membeship != null) {
                    return membeship;
                }
            }
        }
        return targetFeature;
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

    /**
     * Gets all memberships referencing the given feature with the specified id
     *
     * @param sourceFeature
     *            the source feature
     * @param membershipId
     *            the id of the membership
     * @return membership or <code>null</code> if not found
     */
    private Membership getReferencingMembershipWithId(Feature sourceFeature, String membershipId) {
        return EMFUtils.getInverse(sourceFeature, SysmlPackage.eINSTANCE.getMembership_MemberElement()).stream()
                .filter(setting -> setting.getEObject() instanceof Membership membership && membershipId.equals(membership.getElementId()))
                .map(setting -> (Membership) setting.getEObject())
                .findFirst()
                .orElse(null);
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
            sourceEnd.getAliasIds().clear();

            ReferenceSubsetting referenceSubsetting = sourceEnd.getOwnedReferenceSubsetting();
            if (referenceSubsetting == null || referenceSubsetting.isIsImplied()) {
                // Needs to create a new one because this one is inherited
                referenceSubsetting = SysmlFactory.eINSTANCE.createReferenceSubsetting();
                referenceSubsetting.setSubsettingFeature(refUsage);
            }
            this.getAction(newTargetFeature).ifPresent(referenceSubsetting::setReferencedFeature);
            // In case the newSource if a membership (e.g for 'start' and 'done') keep the membership id to identify
            // the graphical source of the edge
            if (newTargetFeature instanceof Membership sourceMembership) {
                refUsage.getAliasIds().add(sourceMembership.getElementId());
            }
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
        if (!(newSource instanceof StateUsage) || !this.checkTransitionEdgeTarget(newSource, transition.getTarget())) {
            this.feedbackMessageService.addFeedbackMessage(new Message("Invalid new source for transition", MessageLevel.WARNING));
            return transition;
        }
        // Update transition source
        transition.getOwnedMembership().stream()
                .filter(Membership.class::isInstance)
                .map(Membership.class::cast)
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
        if (!(newTarget instanceof StateUsage) || !this.checkTransitionEdgeTarget(transition.getSource(), newTarget)) {
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
}
