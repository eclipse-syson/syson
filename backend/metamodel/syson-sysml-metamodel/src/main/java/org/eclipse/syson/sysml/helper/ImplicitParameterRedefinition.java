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

import java.util.ArrayList;
import java.util.List;
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

/**
 * Derives the implicit positional redefinitions of one parameter.
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
     *            the potential redefining parameter
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
     *            specializations already collected by the caller, considered together with the feature's owned relationships
     * @return the missing targets, in supertype traversal order and without duplicates
     */
    public List<Feature> findMissingRedefinitionTargets(List<Specialization> specializations) {
        Type owner = this.feature.getOwningType();
        if (this.feature.getDirection() == null || !(owner instanceof Behavior || owner instanceof Step)) {
            return List.of();
        }
        List<Redefinition> existing = Stream.concat(this.feature.getOwnedRelationship().stream(), specializations.stream())
                .filter(Redefinition.class::isInstance)
                .map(Redefinition.class::cast).toList();
        if (owner instanceof InvocationExpression && !(this.feature.getOwningFeatureMembership() instanceof ReturnParameterMembership)
                && existing.stream().anyMatch(redefinition -> !redefinition.isIsImplied())) {
            return List.of();
        }
        int position = this.getOwnedParameterPosition(this.feature);
        if (position < 0) {
            return List.of();
        }
        List<Feature> targets = this.findDirectParameterSupertypes(owner).stream()
                .flatMap(parent -> this.findParametersAtPosition(parent, position).stream()).distinct().toList();
        List<Feature> existingTargets = existing.stream().map(Redefinition::getRedefinedFeature).toList();
        // A partial or reordered declaration may explicitly map a different position. Preserve that mapping.
        if (!existingTargets.isEmpty() && existingTargets.stream().noneMatch(targets::contains)) {
            return List.of();
        }
        List<Feature> inferred = new ArrayList<>();
        for (Feature target : targets) {
            if (!existingTargets.contains(target)) {
                FeatureDirectionKind required = owner.directionOf(target);
                if (required == this.feature.getDirection() || required == FeatureDirectionKind.INOUT) {
                    inferred.add(target);
                }
            }
        }
        return inferred;
    }

    /**
     * Returns the distinct direct {@link Behavior} and {@link Step} supertypes that supply parameter candidates.
     * For a conjugated type, {@code supertypes(false)} supplies the original type.
     * For other types, it supplies the targets of both explicit and implied specializations.
     *
     * @param type
     *            the owning type of the local, redefining parameter
     * @return the direct supertypes to search for parameter candidates
     */
    private List<Type> findDirectParameterSupertypes(Type type) {
        return type.supertypes(false).stream().filter(parent -> parent instanceof Behavior || parent instanceof Step).distinct().toList();
    }

    /**
     * Finds the targets for one parameter position in a direct supertype.
     * The supertype's own declaration at that position takes precedence. If it has no such declaration,
     * search its effective parameters by their position in their declaring type. This can find several
     * candidates at the same position when the supertype inherits through multiple branches.
     * Reading owned features first avoids resolving the inherited signature for a declared position.
     *
     * @param parent
     *            the direct supertype to search
     * @param position
     *            the zero-based ordinary parameter position, or {@link #RESULT} for a return parameter
     * @return the matching targets in the supertype's parameter order
     */
    private List<Feature> findParametersAtPosition(Type parent, int position) {
        if (position == RESULT) {
            List<Feature> results = parent.getOwnedFeature().stream().filter(feature -> feature.getOwningFeatureMembership() instanceof ReturnParameterMembership).toList();
            if (!results.isEmpty()) {
                return results;
            }
        } else {
            List<Feature> owned = parent.getOwnedFeature().stream().filter(this::isOrdinaryDirectedParameter).toList();
            if (position < owned.size()) {
                return List.of(owned.get(position));
            }
        }
        return this.getEffectiveParameters(parent).stream().filter(parameter -> this.getOwnedParameterPosition(parameter) == position).toList();
    }

    /**
     * Reads the metamodel's effective parameter list, including inherited parameters that remain after redefinition.
     *
     * @param type
     *            the behavior or step whose parameters are needed
     * @return the effective parameters in metamodel order
     */
    private List<Feature> getEffectiveParameters(Type type) {
        if (type instanceof Behavior behavior) {
            return behavior.getParameter();
        }
        return ((Step) type).getParameter();
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
        return parameter.getOwningType().getOwnedFeature().stream().filter(this::isOrdinaryDirectedParameter).toList().indexOf(parameter);
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
}
