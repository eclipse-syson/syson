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

import org.omg.sysml.lang.sysml.FeatureMembership;
import org.omg.sysml.lang.sysml.Multiplicity;
import org.omg.sysml.lang.sysml.OwningMembership;
import org.omg.sysml.lang.sysml.Redefinition;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.SysMLFactory;
import org.omg.sysml.lang.sysml.Type;
import org.omg.sysml.lang.sysml.Usage;

/**
 * Completes an implicit usage multiplicity.
 * <p>
 * This mirrors the multiplicity preparation done by the pilot implementation parser post-processors (ST6RI-947). Both
 * rules only depend on local properties and on the containment graph: explicit owned redefinitions prevent the
 * default except for ends. Subsettings and implied redefinitions do not; their targets are never inspected. The completion can therefore
 * run as soon as parsing has produced a stable containment graph.
 * </p>
 *
 * @author Arthur Daussy
 */
public final class UsageMultiplicityCompleter {

    private UsageMultiplicityCompleter() {
        // Utility class.
    }

    /**
     * Completes the end-only multiplicity used by generic usages and connection usages.
     *
     * @param usage
     *            the {@link Usage} to complete
     */
    public static void completeEndMultiplicity(Usage usage) {
        if (usage.isEnd() && !hasOwnedMultiplicity(usage)) {
            addMultiplicity(usage);
        }
    }

    /**
     * Completes the default multiplicity used by attribute, item and port usages: an end usage or a usage owned by a
     * {@link Type} through a FeatureMembership without an explicit owned redefinition receives a default multiplicity.
     *
     * @param usage
     *            the {@link Usage} to complete
     */
    public static void completeDefaultMultiplicity(Usage usage) {
        // Follow containment instead of the resolving, derived owningType reference.
        // TODO: Update as necessary after final resolution of SYSML21-185, as in Pilot.
        boolean shouldAddMultiplicity = usage.isEnd() || usage.eContainer() instanceof FeatureMembership membership
                && membership.eContainer() instanceof Type
                && usage.getOwnedRelationship().stream().noneMatch(relationship -> relationship instanceof Redefinition && !relationship.isImplied());
        if (shouldAddMultiplicity && !hasOwnedMultiplicity(usage)) {
            addMultiplicity(usage);
        }
    }

    /**
     * Adds an implicit multiplicity to the given {@link Usage}.
     *
     * @param usage
     *            the {@link Usage}
     */
    private static void addMultiplicity(Usage usage) {
        Multiplicity multiplicity = SysMLFactory.eINSTANCE.createMultiplicity();
        OwningMembership membership = SysMLFactory.eINSTANCE.createOwningMembership();
        membership.setOwnedMemberElement(multiplicity);
        usage.getOwnedRelationship().add(membership);
    }

    /**
     * Tells if the given {@link Type} owns a multiplicity.
     *
     * @param type
     *            the {@link Type}
     * @return if <code>true</code> the given {@link Type} owns a multiplicity, <code>false</code> otherwise
     */
    private static boolean hasOwnedMultiplicity(Type type) {
        return type.getOwnedRelationship().stream().map(Relationship::getOwnedRelatedElement)
                .flatMap(List::stream).anyMatch(Multiplicity.class::isInstance);
    }
}
