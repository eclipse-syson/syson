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
package org.eclipse.syson.sysml.linker.additionalmembers;

import java.util.List;

import org.eclipse.emf.ecore.InternalEObject;
import org.omg.sysml.lang.sysml.Behavior;
import org.omg.sysml.lang.sysml.Connector;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Feature;
import org.omg.sysml.lang.sysml.FlowUsage;
import org.omg.sysml.lang.sysml.Membership;
import org.omg.sysml.lang.sysml.ParameterMembership;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.Step;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.lang.sysml.TransitionUsage;
import org.omg.sysml.lang.sysml.Type;

/**
 * Completes the source membership of a transition usage.
 * <p>
 * This rule only depends on the stable containment order of memberships in the transition's owning type. It can run
 * after parsing and before linking. It must run before {@link TransitionLinkCompleter}, which consumes the derived
 * transition source.
 * </p>
 *
 * @author Arthur Daussy
 */
public final class TransitionSourceCompleter {

    private TransitionSourceCompleter() {
        // Utility class.
    }

    /**
     * Completes the source membership if it is absent or empty.
     */
    public static void completeSource(TransitionUsage transition) {
        List<Membership> memberships = ownedMemberships(transition);
        Feature source = getPreviousFeature(transition);
        if (memberships.isEmpty() || memberships.get(0) instanceof ParameterMembership) {
            Membership membership = SysMLFactory.eINSTANCE.createMembership();
            membership.setMemberElement(source);
            transition.getOwnedRelationship().add(0, membership);
        } else if (memberships.get(0).eGet(SysMLPackage.Literals.MEMBERSHIP__MEMBER_ELEMENT, false) == null) {
            memberships.get(0).setMemberElement(source);
        }
    }

    private static List<Membership> ownedMemberships(TransitionUsage transition) {
        return transition.getOwnedRelationship().stream().filter(Membership.class::isInstance)
                .map(Membership.class::cast).toList();
    }

    /** Gets the {@link Feature} preceding a transition. */
    /**
     * @param transition
     *            the {@link TransitionUsage}
     * @return the {@link Feature} preceding a transition
     */
    private static Feature getPreviousFeature(TransitionUsage transition) {
        Type owner = getOwningType(transition);
        Feature previousFeature = null;
        if (owner != null) {
            Relationship owningRelationship = transition.getOwningRelationship();
            List<Relationship> relationships = owner.getOwnedRelationship();
            for (int index = relationships.indexOf(owningRelationship) - 1; index >= 0 && previousFeature == null; index--) {
                Relationship relationship = relationships.get(index);
                if (relationship instanceof Membership membership) {
                    Object member = membership.eGet(SysMLPackage.Literals.MEMBERSHIP__MEMBER_ELEMENT, false);
                    if (member instanceof Feature previous && isValidPreviousFeature(previous)) {
                        previousFeature = previous;
                    }
                }
            }
        }
        return previousFeature;
    }

    /**
     * Tells if a feature can precede a transition.
     * 
     * @param feature
     *            the {@link Feature}
     * @return <code>true</code> if a feature can precede a transition, <code>false</code> otherwise
     */
    private static boolean isValidPreviousFeature(Feature feature) {
        boolean valid = !isProxy(feature);
        if (valid) {
            valid = !isParameter(feature);
        }
        if (valid) {
            valid = !(feature instanceof TransitionUsage);
        }
        if (valid && feature instanceof Connector) {
            valid = isMessageConnection(feature);
        }
        return valid;
    }

    /**
     * Gets the type that owns the given {@link Feature}.
     * 
     * @param feature
     *            the {@link Feature}
     * @return the type that owns the given {@link Feature}
     */
    private static Type getOwningType(Feature feature) {
        Relationship owningRelationship = feature.getOwningRelationship();
        Element owner = null;
        if (owningRelationship != null) {
            owner = owningRelationship.getOwningRelatedElement();
        }
        Type owningType = null;
        if (owner instanceof Type type) {
            owningType = type;
        }
        return owningType;
    }

    /**
     * Tells if the given {@link Feature} is a parameter.
     * 
     * @param feature
     *            the {@link Feature}
     * @return <code>true</code> if the given {@link Feature} is a parameter, <code>false</code> otherwise
     */
    private static boolean isParameter(Feature feature) {
        Type owningType = getOwningType(feature);
        return (owningType instanceof Behavior || owningType instanceof Step) && feature.getDirection() != null;
    }

    /**
     * Tells if the given {@link Feature} is a message connection.
     * 
     * @param feature
     *            the {@link Feature}
     * @return <code>true</code> if the given {@link Feature} is a message connection, <code>false</code> otherwise
     */
    private static boolean isMessageConnection(Feature feature) {
        return feature instanceof FlowUsage && feature.getOwnedFeature().stream().noneMatch(Feature::isEnd);
    }

    /**
     * Tells if the given {@link Feature} is an unresolved proxy.
     * 
     * @param feature
     *            the {@link Feature}
     * @return <code>true</code> if the given {@link Feature} is an unresolved proxy, <code>false</code> otherwise
     */
    private static boolean isProxy(Feature feature) {
        return feature instanceof InternalEObject internal && internal.eIsProxy();
    }
}
