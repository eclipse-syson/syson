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
package org.eclipse.syson.sysml.linker;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.omg.sysml.lang.sysml.AcceptActionUsage;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FeatureDirectionKind;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.ParameterMembership;
import org.omg.sysml.lang.sysml.Redefinition;
import org.omg.sysml.lang.sysml.ReferenceUsage;
import org.omg.sysml.lang.sysml.TransitionFeatureKind;
import org.omg.sysml.lang.sysml.TransitionFeatureMembership;
import org.omg.sysml.lang.sysml.TransitionUsage;
import org.omg.sysml.lang.sysml.Type;

/**
 * Navigates a SysML model exclusively through API that do rely on a lazy proxy resolution. It is basically safe to navigate trough containment reference or derived feature only relying on
 * containment feature or attribute.
 * <p>
 * Avoiding derived SysML navigation properties is important while linking because reading those properties may require
 * references that have not been resolved yet.
 * </p>
 *
 * @author Arthur Daussy
 */
public class SysMLProxySafeNavigator {

    /**
     * Gets the trigger payload corresponding to a transition's second input parameter, using containment only.
     * SysML 8.3.6.3 uses this feature as the naming feature of the transition payload, and
     * 8.4.14.3 requires the transition payload to subset the trigger payload chain.
     *
     * @param feature
     *            the possible transition payload
     * @return the trigger payload, or <code>null</code> if the feature is not a transition payload with a trigger
     */
    public Feature getTransitionTriggerPayload(Feature feature) {
        if (feature instanceof ReferenceUsage && feature.eContainer() instanceof ParameterMembership membership
                && membership.eContainer() instanceof TransitionUsage transition) {
            List<Feature> inputs = this.getOwnedInputParameters(transition);
            if (inputs.size() > 1 && inputs.get(1) == feature) {
                for (var relationship : List.copyOf(transition.getOwnedRelationship())) {
                    if (relationship instanceof TransitionFeatureMembership triggerMembership && triggerMembership.getKind() == TransitionFeatureKind.TRIGGER
                            && triggerMembership.getOwnedMemberFeature() instanceof AcceptActionUsage trigger) {
                        return this.getOwnedInputParameters(trigger).stream().findFirst().orElse(null);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Gets the input and input-output parameters owned by the given {@link Namespace}, in containment order.
     * Only features contained through {@link ParameterMembership} relationships are considered; parameters with no
     * direction or an output-only direction are excluded.
     *
     * @param namespace
     *            the namespace whose owned parameters are inspected
     * @return the owned input and input-output parameters, in containment order
     */
    private List<Feature> getOwnedInputParameters(Namespace namespace) {
        return namespace.getOwnedRelationship().stream()
                .filter(ParameterMembership.class::isInstance)
                .map(ParameterMembership.class::cast)
                .map(ParameterMembership::getOwnedMemberParameter)
                .filter(feature -> feature != null && feature.getDirection() != null && feature.getDirection() != FeatureDirectionKind.OUT)
                .toList();
    }

    /**
     * Gets the owning {@link Type} of the {@link Feature} that owns the given {@link Redefinition}.
     * <p>
     * This method deliberately follows EMF containment only. In particular, it does not call
     * {@link Redefinition#getOwningFeature()} or {@link Feature#getOwningType()}, because those derived references may
     * depend on references that have not been linked yet.
     * </p>
     *
     * @param redefinition
     *            the {@link Redefinition}
     *
     * @return the owning {@link Type}, or <code>null</code> when the expected containment chain is incomplete
     */
    public Type getRedefinitionOwningType(Redefinition redefinition) {
        Feature redefiningFeature = this.getContaining(Feature.class, redefinition.eContainer());
        Type result = null;
        if (redefiningFeature != null) {
            result = this.getContaining(Type.class, redefiningFeature.eContainer());
        }
        return result;
    }

    /**
     * Gets the first object of the requested type while walking from the given object towards the containment root.
     *
     * @param <T>
     *            the expected type
     * @param cls
     *            the class representing the expected type
     * @param eObj
     *            the object at which to start the search; it is included in the search
     *
     * @return the first object assignable to the requested type, or <code>null</code> if the containment chain contains
     *         none
     */
    public <T> T getContaining(Class<T> cls, EObject eObj) {
        EObject current = eObj;
        while (current != null) {
            if (cls.isInstance(current)) {
                break;
            }
            current = current.eContainer();
        }

        return (T) current;
    }

    /**
     * Gets the last object of the requested type encountered in a depth-first traversal of the given object's
     * descendants.
     * <p>
     * The given object itself is not included in the search.
     * </p>
     *
     * @param <T>
     *            the expected type
     * @param cls
     *            the class representing the expected type
     * @param eObj
     *            the object whose descendants are searched
     *
     * @return the last descendant assignable to the requested type, or <code>null</code> if no descendant matches
     */
    public <T> T getContained(Class<T> cls, EObject eObj) {
        T res = null;

        Iterator<EObject> it = eObj.eAllContents();
        while (it.hasNext()) {
            EObject current = it.next();
            if (cls.isInstance(current)) {
                res = (T) current;
            }
        }

        return res;
    }
}
