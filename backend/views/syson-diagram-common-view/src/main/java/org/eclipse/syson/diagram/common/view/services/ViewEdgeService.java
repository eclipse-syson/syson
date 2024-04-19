/*******************************************************************************
 * Copyright (c) 2024 Obeo.
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
import java.util.Optional;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.syson.services.UtilService;
import org.eclipse.syson.sysml.AllocationUsage;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.EndFeatureMembership;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureChaining;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Usage;
import org.eclipse.syson.util.SysMLMetamodelHelper;

/**
 * Edge-related Java services used by several diagrams.
 *
 * @author Jerome Gout
 */
public class ViewEdgeService {

    private final UtilService utilService;

    public ViewEdgeService() {
        this.utilService = new UtilService();
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

    private boolean isAnAllocateEdge(AllocationUsage allocationUsage) {
        // an allocate edge is an AllocationUsage that contains 2 EndFeaturesMembership
        return this.getFeatures(allocationUsage).size() == 2;
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
        } else {
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

    public Element reconnectTargetAlocateEdge(AllocationUsage self, Element newTarget) {
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
}
