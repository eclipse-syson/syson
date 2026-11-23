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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.syson.sysml.Feature;
import org.eclipse.syson.sysml.FeatureMembership;
import org.eclipse.syson.sysml.RequirementConstraintMembership;
import org.eclipse.syson.sysml.StateSubactionMembership;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.TransitionFeatureMembership;
import org.eclipse.syson.sysml.Type;

/**
 * Memberships attached during a single feature-chain operation, in insertion order.
 *
 * @author Arthur Daussy
 */
final class FeatureMembershipChanges {

    /**
     * Memberships created by this operation, recorded before attachment and kept in insertion order.
     * Rollback traverses this list in reverse to detach nested additions before their owners.
     */
    private final List<FeatureMembership> created = new ArrayList<>();

    /**
     * Creates an empty tracker for memberships attached during one feature-chain redefinition operation.
     */
    // SysON requires public or private constructors, including on package-private classes.
    @SuppressWarnings("checkstyle:RedundantModifier")
    public FeatureMembershipChanges() {
    }

    /**
     * Attaches a newly created local feature through a membership with the source's role and records that membership
     * for rollback. The membership is tracked before insertion so a failed insertion can also be cleaned up.
     *
     * @param owner
     *            the type that will contain the new membership
     * @param local
     *            the newly created, detached redefining feature to attach
     * @param source
     *            the redefined feature whose owning membership supplies the local membership's metaclass and role
     * @param insertion
     *            zero-based index in the owner's owned relationships, between zero and the list size inclusive
     */
    void attach(Type owner, Feature local, Feature source, int insertion) {
        FeatureMembership membership = this.createRedefinitionMembership(source.getOwningFeatureMembership());
        membership.getOwnedRelatedElement().add(local);
        this.created.add(membership);
        owner.getOwnedRelationship().add(insertion, membership);
    }

    /**
     * Detaches the memberships recorded by this operation that are still contained by a type.
     * Reverse insertion order removes nested additions before their newly created owners.
     * Removing a membership also detaches the subtree containing its local feature and owned relationships.
     * This cleanup relies on callers using the tracker for newly created features and memberships.
     */
    void rollback() {
        for (FeatureMembership membership : this.created.reversed()) {
            if (membership.eContainer() instanceof Type owner) {
                owner.getOwnedRelationship().remove(membership);
            }
        }
    }

    /**
     * Preserve the membership role so SysML derived properties such as entryAction, requiredConstraint and
     * effectAction can find the local feature. Visibility is preserved so a protected source remains protected locally.
     * Preserving the role and visibility is service policy.
     *
     * @param source
     *            the source membership, or null to create an ordinary feature membership with default visibility
     * @return a detached membership with the source's category, role and visibility when a source is supplied
     */
    private FeatureMembership createRedefinitionMembership(FeatureMembership source) {
        FeatureMembership local;
        if (source == null) {
            local = SysmlFactory.eINSTANCE.createFeatureMembership();
        } else {
            local = (FeatureMembership) SysmlFactory.eINSTANCE.create(source.eClass());
            local.setVisibility(source.getVisibility());
        }
        if (source instanceof StateSubactionMembership sourceSubaction) {
            // SysML 2.1 Beta 2, 8.3.18.4: kind distinguishes entry, do and exit actions; retaining the role is service policy.
            ((StateSubactionMembership) local).setKind(sourceSubaction.getKind());
        } else if (source instanceof RequirementConstraintMembership sourceConstraint) {
            // SysML 2.1 Beta 2, 8.3.21.7: kind distinguishes assumed and required constraints; retaining the role is service policy.
            ((RequirementConstraintMembership) local).setKind(sourceConstraint.getKind());
        } else if (source instanceof TransitionFeatureMembership sourceTransition) {
            // SysML 2.1 Beta 2, 8.3.18.8: kind distinguishes transition triggers, guards and effects; retaining the role is service policy.
            ((TransitionFeatureMembership) local).setKind(sourceTransition.getKind());
        }
        return local;
    }
}
