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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.FeatureTyping;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.helper.EMFUtils;

/**
 * Object in charge of computing a chain of feature to access a {@link Feature} from one {@link Type}.
 *
 * @author Arthur Daussy
 */
public class FeatureChainComputer {

    /**
     * Find the shortest path from the given source to the given feature.
     *
     * @param source
     *            a Type
     * @param target
     *            a Feature
     * @return an {@link Optional} list of feature (including the target)
     */
    public Optional<List<Feature>> computeShortestPath(Type source, Feature target) {
        List<List<Feature>> chains = this.computeChains(source, target, new HashSet<>());
        return chains.stream().min(Comparator.comparing(Collection::size));
    }

    /**
     * Compute all feature paths from the given source to the given feature.
     *
     * @param source
     *            a {@link Type}
     * @param target
     *            a Feature.
     * @return list of all path
     */
    public List<List<Feature>> computeAllPath(Type source, Feature target) {
        return this.computeChains(source, target, new HashSet<>());
    }

    private List<List<Feature>> computeChains(Type source, Feature target, Set<Feature> encounteredFeature0) {
        List<List<Feature>> result = new ArrayList<>();
        Set<Feature> encounteredFeature = new HashSet<>(encounteredFeature0);
        encounteredFeature.add(target);

        if (target != null) {

            if (target.getOwningType() == null) {
                // The target is a feature directly available in the root namespace
                List<Feature> features = new ArrayList<>();
                features.add(target);
                result.add(features);
            }
            EList<Feature> sourceFeatures = source.getFeature();

            for (Feature accessingFeature : this.computeAccessingFeature(target, encounteredFeature)) {
                if (accessingFeature == source) {
                    List<Feature> features = new ArrayList<>();
                    features.add(target);
                    result.add(features);
                } else if (sourceFeatures.contains(accessingFeature)) {
                    List<Feature> features = new ArrayList<>();
                    features.add(accessingFeature);
                    features.add(target);
                    result.add(features);
                } else {
                    List<List<Feature>> subchains = this.computeChains(source, accessingFeature, encounteredFeature);
                    for (List<Feature> subChainfeature : subchains) {
                        if (!subChainfeature.isEmpty()) {
                            List<Feature> features = new ArrayList<>(subChainfeature);
                            features.add(target);
                            result.add(features);
                        }
                    }
                }
            }

        }
        return result;
    }

    private List<Feature> computeAccessingFeature(Feature target, Set<Feature> encounteredFeature) {

        Collection<Setting> inverseReferences = EMFUtils.getInverse(target);

        List<Feature> accessingFeatures = new ArrayList<>();
        for (Setting s : inverseReferences) {
            List<Feature> accessFeatures = this.collectTargettingFeatures(s);
            for (var af : accessFeatures) {
                if (!encounteredFeature.contains(af) && !accessingFeatures.contains(af)) {
                    accessingFeatures.add(af);
                }
            }
        }

        return accessingFeatures;
    }

    private List<Feature> collectTargettingFeatures(Setting inverseReference) {
        EObject referenceSource = inverseReference.getEObject();
        List<Feature> accessFeatures = new ArrayList<>();
        if (referenceSource instanceof FeatureTyping featureTyping && inverseReference.getEStructuralFeature() == SysmlPackage.eINSTANCE.getFeatureTyping_Type()) {
            accessFeatures.add(featureTyping.getOwningFeature());
        } else if (referenceSource instanceof FeatureMembership featureMembership && inverseReference.getEStructuralFeature() == SysmlPackage.eINSTANCE.getRelationship_OwnedRelatedElement()) {
            Type owningType = featureMembership.getOwningType();
            this.collectFeatureFromType(owningType, accessFeatures);
        }
        return accessFeatures;
    }

    private void collectFeatureFromType(Type owningType, List<Feature> accessFeatures) {
        if (owningType != null) {
            // Get all specialization of this type
            this.collectTypingFeatures(accessFeatures, owningType);
        }
    }

    private void collectTypingFeatures(List<Feature> accessFeatures, Type owningType) {
        List<Type> typeToSearch = new ArrayList<>();
        typeToSearch.add(owningType);
        this.collectAllSubTypes(owningType, typeToSearch);
        for (Type type : typeToSearch) {
            if (type instanceof Feature feature) {
                accessFeatures.add(feature);
            }

        }
    }

    private void collectAllSubTypes(Type superType, List<Type> result) {
        List<Type> specifics = this.computeSpecific(superType);
        for (Type specific : specifics) {
            if (!result.contains(specific)) {
                result.add(specific);
                this.collectAllSubTypes(specific, result);
            }
        }

    }

    private List<Type> computeSpecific(Type superType) {
        return EMFUtils.getInverse(superType)
                .stream()
                .filter(s -> s.getEObject() instanceof Specialization)
                .map(s -> ((Specialization) s.getEObject()).getSpecific())
                .toList();
    }



}
