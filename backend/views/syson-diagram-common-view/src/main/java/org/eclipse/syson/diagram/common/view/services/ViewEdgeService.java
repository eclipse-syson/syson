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
import org.eclipse.sirius.components.diagrams.elements.NodeElementProps;
import org.eclipse.sirius.components.diagrams.renderer.DiagramRenderingCache;
import org.eclipse.sirius.components.representations.Message;
import org.eclipse.sirius.components.representations.MessageLevel;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.ActionUsage;
import org.eclipse.syson.sysml.AllocationUsage;
import org.eclipse.syson.sysml.BindingConnectorAsUsage;
import org.eclipse.syson.sysml.ConnectorAsUsage;
import org.eclipse.syson.sysml.Definition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureChaining;
import org.eclipse.syson.sysml.InterfaceUsage;
import org.eclipse.syson.sysml.ItemUsage;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.PortUsage;
import org.eclipse.syson.sysml.ReferenceSubsetting;
import org.eclipse.syson.sysml.ReferenceUsage;
import org.eclipse.syson.sysml.StateUsage;
import org.eclipse.syson.sysml.Succession;
import org.eclipse.syson.sysml.SuccessionAsUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.TransitionUsage;
import org.eclipse.syson.sysml.Usage;
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
     *         the {@link EObject} stored in a {@link ResourceSet}
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
     *         the source element
     * @param target
     *         the target element
     * @param cache
     *         the DiagramRenderingCache
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

    /**
     * Get all reachable {@link AllocationUsage} in the {@link ResourceSet} of given {@link EObject}.
     *
     * @param eObject
     *         the {@link EObject} stored in a {@link ResourceSet}
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
        return succession.getSourceFeature();
    }

    public Element getTarget(SuccessionAsUsage succession) {
        return succession.getTargetFeature().stream().findFirst().orElse(null);
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
     *         the source end to modify
     * @param newTargetFeature
     *         the new target feature
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
     *         the given {@link TransitionUsage}.
     * @param newSource
     *         the new target {@link ActionUsage}.
     * @return the given {@link TransitionUsage}.
     */
    public TransitionUsage reconnectSourceTransitionEdge(TransitionUsage transition, ActionUsage newSource) {
        if (!(newSource instanceof ActionUsage) || !this.checkTransitionEdgeTarget(newSource, transition.getTarget())) {
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
     * Set a new target {@link ActionUsage} for the given {@link TransitionUsage}. Used by
     * {@code TransitionEdgeDescriptionProvider.createTargetReconnectTool()}
     *
     * @param transition
     *         the given {@link TransitionUsage}.
     * @param newTarget
     *         the new target {@link ActionUsage}.
     * @return the given {@link TransitionUsage}.
     */
    public TransitionUsage reconnectTargetTransitionEdge(TransitionUsage transition, ActionUsage newTarget) {
        if (!(newTarget instanceof ActionUsage) || !this.checkTransitionEdgeTarget(transition.getSource(), newTarget)) {
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
     *         The source of the transition
     * @param target
     *         The target of the transition
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
     *         a transition
     * @return <code>true</code> if targeting a {@link StateUsage}.
     */
    public boolean isTransitionUsageForState(TransitionUsage transition) {
        return transition.getTarget() instanceof StateUsage;
    }

    /**
     * Return the source {@link Usage} of the given {@link ConnectorAsUsage} (e.g. a {@link BindingConnectorAsUsage}, an
     * {@link InterfaceUsage}).
     *
     * @param bind
     *         the given {@link ConnectorAsUsage}.
     * @return the source {@link Usage} if found, <code>null</code> otherwise.
     */
    public Usage getSourcePort(ConnectorAsUsage bind) {
        Usage sourcePort = null;
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
                if (feature instanceof PortUsage || feature instanceof ItemUsage) {
                    sourcePort = (Usage) feature;
                } else if (!feature.getOwnedFeatureChaining().isEmpty()) {
                    EList<FeatureChaining> ownedFeatureChaining = feature.getOwnedFeatureChaining();
                    FeatureChaining lastFeatureChaining = ownedFeatureChaining.get(Math.max(0, ownedFeatureChaining.size() - 1));
                    Feature chainingFeature = lastFeatureChaining.getChainingFeature();
                    if (chainingFeature instanceof PortUsage || chainingFeature instanceof ItemUsage) {
                        sourcePort = (Usage) chainingFeature;
                    }
                }
            }
        }
        return sourcePort;
    }

    /**
     * Return the target {@link Usage} of the given {@link ConnectorAsUsage} (e.g. a {@link BindingConnectorAsUsage}, an
     * {@link InterfaceUsage}).
     *
     * @param bind
     *         the given {@link ConnectorAsUsage}.
     * @return the target {@link Usage} if found, <code>null</code> otherwise.
     */
    public Usage getTargetPort(ConnectorAsUsage bind) {
        Usage targetPort = null;
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
                if (feature instanceof PortUsage || feature instanceof ItemUsage) {
                    targetPort = (Usage) feature;
                } else if (!feature.getOwnedFeatureChaining().isEmpty()) {
                    EList<FeatureChaining> ownedFeatureChaining = feature.getOwnedFeatureChaining();
                    FeatureChaining lastFeatureChaining = ownedFeatureChaining.get(Math.max(0, ownedFeatureChaining.size() - 1));
                    Feature chainingFeature = lastFeatureChaining.getChainingFeature();
                    if (chainingFeature instanceof PortUsage || chainingFeature instanceof ItemUsage) {
                        targetPort = (Usage) feature;
                    }
                }
            }
        }
        return targetPort;
    }

    /**
     * Set a new source {@link Element} for the given {@link ConnectorAsUsage}. Also move the given connector
     * into the parent (i.e. should be a PartUsage) of the new source.
     *
     * @param bind
     *         the given {@link ConnectorAsUsage}.
     * @param newSource
     *         the new target {@link Element}.
     * @return the given {@link ConnectorAsUsage}.
     */
    public ConnectorAsUsage setSourcePort(ConnectorAsUsage bind, Element newSource) {
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
                            // move the given connector into the parent of the new source
                            Namespace owningNamespace = newSourcePort.getOwningNamespace();
                            if (owningNamespace instanceof Usage || owningNamespace instanceof Definition) {
                                owningNamespace.getOwnedRelationship().add(bind.getOwningRelationship());
                            }
                        });
            }
        } else {
            this.feedbackMessageService.addFeedbackMessage(new Message("The source of the ConnectorAsUsage can only be connected to a PortUsage", MessageLevel.WARNING));
        }
        return bind;
    }

    /**
     * Set a new target {@link Element} for the given {@link ConnectorAsUsage}.
     *
     * @param bind
     *         the given {@link ConnectorAsUsage}.
     * @param newTarget
     *         the new target {@link Element}.
     * @return the given {@link ConnectorAsUsage}.
     */
    public ConnectorAsUsage setTargetPort(ConnectorAsUsage bind, Element newTarget) {
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
            this.feedbackMessageService.addFeedbackMessage(new Message("The target of the ConnectorAsUsage can only be connected to a PortUsage", MessageLevel.WARNING));
        }
        return bind;
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
