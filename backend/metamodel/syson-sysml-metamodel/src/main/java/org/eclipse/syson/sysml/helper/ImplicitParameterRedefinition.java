/*******************************************************************************
 * Copyright (c) 2026 Obeo.
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
package org.eclipse.syson.sysml.helper;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.syson.sysml.Behavior;
import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureDirectionKind;
import org.eclipse.syson.sysml.InvocationExpression;
import org.eclipse.syson.sysml.Redefinition;
import org.eclipse.syson.sysml.ReturnParameterMembership;
import org.eclipse.syson.sysml.Specialization;
import org.eclipse.syson.sysml.Step;
import org.eclipse.syson.sysml.Type;
import org.eclipse.syson.sysml.VisibilityKind;

/**
 * Computes positional parameter inheritance and implicit redefinitions.
 *
 * @author Arthur Daussy
 */
public final class ImplicitParameterRedefinition {

    /** A result redefines the parents' results independently of ordinary parameter positions. */
    private static final int RESULT = Integer.MAX_VALUE;

    private final Feature feature;

    /**
     * Creates a computation for a feature whose implicit parameter redefinition targets are requested.
     *
     * @param feature
     *            the locally declared feature to check for positional redefinitions
     */
    public ImplicitParameterRedefinition(Feature feature) {
        this.feature = feature;
    }

    /**
     * Computes the inherited features that still need an implicit redefinition from this parameter.
     * KerML checkFeatureParameterRedefinition matches ordinary parameters by declaration position.
     * KerML checkFeatureResultRedefinition matches return parameters separately.
     * Existing mappings to a matching position can be completed. A mapping entirely outside that
     * position or an explicit ordinary invocation argument takes precedence over inference.
     * The target's direction in the local owner's context must match the local direction or be {@code inout}.
     *
     * @param specializations
     *            specializations already collected for the local feature, or an empty list if none have been collected
     * @return the missing targets, in supertype traversal order and without duplicates
     */
    public List<Feature> findMissingRedefinitionTargets(List<Specialization> specializations) {
        Type owner = this.feature.getOwningType();
        if (this.feature.getDirection() == null || !(owner instanceof Behavior || owner instanceof Step)) {
            return List.of();
        }
        List<Redefinition> existing = Stream.concat(this.feature.getOwnedRelationship().stream(), specializations.stream())
                .filter(Redefinition.class::isInstance)
                .map(Redefinition.class::cast)
                .toList();
        if (owner instanceof InvocationExpression && !(this.feature.getOwningFeatureMembership() instanceof ReturnParameterMembership)
                && existing.stream()
                        .anyMatch(redefinition -> !redefinition.isIsImplied())) {
            return List.of();
        }
        int position = this.getOwnedParameterPosition(this.feature);
        if (position < 0) {
            return List.of();
        }
        List<Feature> targets = this.findInheritedParameterTargets(owner, position);
        List<Feature> existingTargets = existing.stream()
                .map(Redefinition::getRedefinedFeature)
                .toList();
        // A partial or reordered declaration may explicitly map a different position. Preserve that mapping.
        if (!existingTargets.isEmpty() && existingTargets.stream()
                .noneMatch(targets::contains)) {
            return List.of();
        }
        List<Feature> inferred = new ArrayList<>();
        for (Feature target : targets) {
            if (!existingTargets.contains(target) && !this.hasParameterSupertype(target.getOwningType(), owner)) {
                FeatureDirectionKind required = owner.directionOf(target);
                if (required == this.feature.getDirection() || required == FeatureDirectionKind.INOUT) {
                    inferred.add(target);
                }
            }
        }
        return inferred;
    }

    /**
     * Finds the inheritable sources of one parameter position in the direct parents of a destination type.
     * Private memberships and replaced prefixes are handled by the same lookup used for implicit redefinitions.
     * This query reads the model without creating parameters or relationships and does not filter target directions.
     *
     * @param owner
     *            the destination type whose inherited positions are requested
     * @param position
     *            the zero-based ordinary position, or Integer.MAX_VALUE to request return parameters separately
     * @return the matching sources in supertype traversal order, without duplicates, or an empty list
     */
    public List<Feature> findInheritedParameterTargets(Type owner, int position) {
        // A cyclic traversal can produce branch-dependent partial results. Reuse lookups only in an acyclic hierarchy.
        LookupCache cache = new LookupCache(!this.hasSpecializationCycle(owner, new HashSet<>(), new HashSet<>()), new HashMap<>());
        return this.findDirectParameterSupertypes(owner).stream()
                .flatMap(parent -> this.findParametersAtPosition(parent, position, 0, new HashSet<>(Set.of(owner)), cache).stream())
                .distinct()
                .toList();
    }

    /**
     * Resolves a position after the cycle guard and cache lookup in {@link #findParametersAtPosition}.
     *
     * @param parent
     *            the supertype being searched
     * @param position
     *            offset among eligible ordinary parameters, or {@link #RESULT}
     * @param positionsToSkip
     *            leading positions occupied by the subtype before filtering visibility
     * @param visitedTypesOnBranch
     *            types on the current branch, including the parent
     * @param cache
     *            completed lookups for this invocation
     * @return the matching inheritable parameters, or an empty list
     */
    private List<Feature> computeParametersAtPosition(Type parent, int position, int positionsToSkip, Set<Type> visitedTypesOnBranch, LookupCache cache) {
        List<Feature> declaredFeatures = parent.getOwnedFeature();
        if (position == RESULT) {
            List<Feature> results = declaredFeatures.stream()
                    .filter(feature -> feature.getOwningFeatureMembership() instanceof ReturnParameterMembership)
                    .toList();
            if (!results.isEmpty()) {
                return results.stream()
                        .filter(result -> this.isInheritableParameter(result))
                        .toList();
            }
            return this.findInheritedParametersAtPosition(parent, RESULT, 0, declaredFeatures, visitedTypesOnBranch, cache);
        }
        List<Feature> declaredParameters = declaredFeatures.stream()
                .filter(this::isOrdinaryDirectedParameter)
                .toList();
        List<Feature> visibleParameters = declaredParameters.stream()
                .skip(positionsToSkip)
                .filter(this::isInheritableParameter)
                .toList();
        if (position < visibleParameters.size()) {
            return List.of(visibleParameters.get(position));
        }
        // The requested position lies beyond the eligible local declarations.
        int positionInInheritedSuffix = position - visibleParameters.size();
        // If this type declares too few parameters to consume the skipped prefix, carry the remainder upwards.
        int positionsToSkipInAncestors = Math.max(0, positionsToSkip - declaredParameters.size());
        positionInInheritedSuffix += positionsToSkipInAncestors;
        return this.findInheritedParametersAtPosition(parent, positionInInheritedSuffix, declaredParameters.size(), declaredFeatures, visitedTypesOnBranch, cache);
    }

    /**
     * Returns the distinct direct {@link Behavior} and {@link Step} supertypes that supply parameter candidates.
     * For a conjugated type, {@code supertypes(false)} supplies the original type.
     * For other types, it supplies the targets of both explicit and implied specializations.
     *
     * @param type
     *            the type whose immediate supertypes are being inspected
     * @return the direct supertypes to search for parameter candidates
     */
    private List<Type> findDirectParameterSupertypes(Type type) {
        return type.supertypes(false).stream()
                .filter(parent -> parent instanceof Behavior || parent instanceof Step)
                .distinct()
                .toList();
    }

    /**
     * Searches each parent after the ordinary positions already occupied by local declarations.
     * Explicit redefinitions may also hide a target at another position.
     *
     * @param type
     *            the type whose remaining parameters are sought in its direct supertypes
     * @param position
     *            zero-based offset in the inherited suffix, or {@link #RESULT} to look for a return parameter
     * @param declaredParameterCount
     *            number of ordinary parameter positions already occupied by declarations of {@code type}; zero for a result lookup
     * @param declaredFeatures
     *            features declared by {@code type}, used to exclude candidates they explicitly redefine
     * @param visitedTypesOnBranch
     *            types on the current specialization path, including {@code type}
     * @param cache
     *            completed lookups shared by all branches of this invocation
     */
    private List<Feature> findInheritedParametersAtPosition(Type type, int position, int declaredParameterCount, List<Feature> declaredFeatures,
            Set<Type> visitedTypesOnBranch, LookupCache cache) {
        return this.findDirectParameterSupertypes(type).stream()
                .flatMap(parent -> this.findParametersAtPosition(parent, position, declaredParameterCount, visitedTypesOnBranch, cache).stream())
                .filter(candidate -> declaredFeatures.stream()
                        .noneMatch(local -> this.redefinesExplicitly(local, candidate, new HashSet<>())))
                .distinct()
                .toList();
    }

    /**
     * Finds one position in a supertype's inheritable signature, after skipping positions occupied by the subtype.
     * Private declarations are excluded at every inheritance boundary, including the direct parent.
     * This follows the inheritable-target requirement of KerML 1.0, 7.3.4.5. TODO clarify its interaction
     * with checkFeatureParameterRedefinition, whose OCL selects owned parameters without a visibility filter.
     * For example, if B declares two private inputs replacing A::p1 and A::p2, an empty C specializing B
     * inherits A::p3 as its first input. Searching C's first position reaches A with two positions to skip.
     * The current branch excludes cycles without blocking other branches of a diamond.
     *
     * @param parent
     *            the supertype whose signature is being searched
     * @param position
     *            zero-based offset among eligible ordinary parameters after skipping the replaced prefix, or {@link #RESULT}
     * @param positionsToSkip
     *            number of leading positions already occupied by the subtype, counted before filtering visibility
     * @param visitedTypesOnBranch
     *            types already on this branch; the search temporarily adds {@code parent} while visiting its ancestors
     * @param cache
     *            completed lookups shared by all branches of this invocation, including empty results
     */
    private List<Feature> findParametersAtPosition(Type parent, int position, int positionsToSkip,
            Set<Type> visitedTypesOnBranch, LookupCache cache) {
        if (!visitedTypesOnBranch.add(parent)) {
            return List.of();
        }
        try {
            ParameterLookup lookup = new ParameterLookup(parent, position, positionsToSkip);
            List<Feature> targets = cache.completedLookups().get(lookup);
            if (targets == null) {
                targets = this.computeParametersAtPosition(parent, position, positionsToSkip, visitedTypesOnBranch, cache);
                if (cache.enabled()) {
                    cache.completedLookups().put(lookup, targets);
                }
            }
            return targets;
        } finally {
            visitedTypesOnBranch.remove(parent);
        }
    }

    /**
     * Computes a parameter's position from the ordinary parameter declarations of its owning type.
     * Using the declaring type keeps that position stable when the parameter is inherited through several branches.
     *
     * @param parameter
     *            the parameter whose declaration position is needed
     * @return the zero-based declaration position, {@link #RESULT} for a return parameter, or {@code -1}
     *         if the owning type's ordinary parameter list does not contain the feature
     */
    private int getOwnedParameterPosition(Feature parameter) {
        if (parameter.getOwningFeatureMembership() instanceof ReturnParameterMembership) {
            return RESULT;
        }
        return parameter.getOwningType().getOwnedFeature().stream()
                .filter(this::isOrdinaryDirectedParameter)
                .toList()
                .indexOf(parameter);
    }

    /**
     * Checks for a return path from the candidate's declaring type to the local owner. In a specialization cycle
     * both owners can declare a match before positional lookup sees a repeated type. Inferring those links would
     * make anonymous parameters obtain their names from each other.
     *
     * @param candidateOwner
     *            the declaring type of the proposed redefinition target, where the search starts
     * @param localOwner
     *            the declaring type of the redefining parameter; reaching it would close the specialization cycle
     */
    private boolean hasParameterSupertype(Type candidateOwner, Type localOwner) {
        Deque<Type> remaining = new ArrayDeque<>();
        remaining.add(candidateOwner);
        Set<Type> visited = new HashSet<>();
        while (!remaining.isEmpty()) {
            Type current = remaining.removeFirst();
            if (current == localOwner) {
                return true;
            }
            if (visited.add(current)) {
                remaining.addAll(this.findDirectParameterSupertypes(current));
            }
        }
        return false;
    }

    /**
     * Detects cycles before enabling memoization. A completed lookup in a cyclic hierarchy can depend on the
     * current branch's excluded types and therefore cannot be reused using only its positional arguments.
     *
     * @param type
     *            the type whose parameter supertypes are inspected
     * @param visited
     *            types already explored during this hierarchy scan
     * @param branch
     *            types on the current scan branch
     * @return whether any reachable parameter supertype closes a cycle
     */
    private boolean hasSpecializationCycle(Type type, Set<Type> visited, Set<Type> branch) {
        if (branch.contains(type)) {
            return true;
        }
        if (!visited.add(type)) {
            return false;
        }
        branch.add(type);
        boolean cyclic = this.findDirectParameterSupertypes(type).stream()
                .anyMatch(parent -> this.hasSpecializationCycle(parent, visited, branch));
        branch.remove(type);
        return cyclic;
    }

    private boolean isInheritableParameter(Feature parameter) {
        return parameter.getOwningFeatureMembership().getVisibility() != VisibilityKind.PRIVATE;
    }

    /**
     * Checks whether a feature occupies a position in the ordinary parameter list.
     *
     * @param feature
     *            the feature to check
     * @return whether the feature has a direction and its membership is not a return parameter membership
     */
    private boolean isOrdinaryDirectedParameter(Feature feature) {
        return feature.getDirection() != null && !(feature.getOwningFeatureMembership() instanceof ReturnParameterMembership);
    }

    /**
     * An explicit mapping at a different position can hide an inherited candidate.
     * Read stored relationships to avoid re-entering implicit parameter lookup.
     *
     * @param local
     *            the feature whose stored redefinitions are traversed
     * @param candidate
     *            the inherited feature sought as a direct or transitive redefinition target
     * @param visited
     *            features already examined for this candidate, shared across branches of this redefinition search
     */
    private boolean redefinesExplicitly(Feature local, Feature candidate, Set<Feature> visited) {
        return visited.add(local) && local.getOwnedRelationship().stream()
                .filter(Redefinition.class::isInstance)
                .map(Redefinition.class::cast)
                .map(Redefinition::getRedefinedFeature)
                .anyMatch(target -> target == candidate || target != null && this.redefinesExplicitly(target, candidate, visited));
    }

    /** Results belong to one query, so subsequent model edits cannot leave stale targets. */
    private record LookupCache(boolean enabled, Map<ParameterLookup, List<Feature>> completedLookups) {
    }

    /** Different skipped prefixes can select different targets at the same visible position. */
    private record ParameterLookup(Type type, int position, int positionsToSkip) {
    }

}
