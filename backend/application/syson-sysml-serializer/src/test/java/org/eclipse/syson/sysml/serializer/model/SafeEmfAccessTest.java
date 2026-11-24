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
package org.eclipse.syson.sysml.serializer.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;

import org.eclipse.syson.sysml.serializer.SerializerRoundTripTestSupport;
import org.junit.jupiter.api.Test;
import org.omg.sysml.lang.sysml.Element;
import org.omg.sysml.lang.sysml.Namespace;
import org.omg.sysml.lang.sysml.Relationship;
import org.omg.sysml.lang.sysml.SysMLPackage;
import org.omg.sysml.logic.SysMLLogicStandaloneSetup;

/**
 * Tests the non-resolving, side-effect free reads of {@link SafeEmfAccess}.
 *
 * @author <a href="mailto:cedric.brun@obeo.fr">Cedric Brun</a>
 */
class SafeEmfAccessTest {

    private static final String OWNED_MEMBER_FEATURES = "edited/owned-member-features.sysml";

    static {
        SysMLLogicStandaloneSetup.doSetup();
    }

    /**
     * A stored containment is read directly, even when derived references subset it, such as
     * {@code Relationship::ownedRelatedElement} under {@code OwningMembership::ownedMemberElement}.
     */
    @Test
    void readsStoredContainmentsWithDerivedSubsets() throws IOException {
        final Element definition = member(SerializerRoundTripTestSupport.parseTestCase(OWNED_MEMBER_FEATURES).model(), 0);
        final Relationship membership = definition.getOwnedRelationship().get(0);

        assertEquals(List.of(member(definition, 0)),
                SafeEmfAccess.unresolvedValues(membership, SysMLPackage.eINSTANCE.getRelationship_OwnedRelatedElement()));
    }

    /**
     * A derived reference is read from the containment it reaches through several redefinitions or subsets, keeping its
     * own type, so a comment is not an owned member feature: {@code FeatureMembership::ownedMemberFeature} redefines
     * {@code OwningMembership::ownedMemberElement}, which subsets {@code Relationship::ownedRelatedElement}.
     */
    @Test
    void readsDerivedReferencesThroughIntermediateRedefinitions() throws IOException {
        final Namespace root = SerializerRoundTripTestSupport.parseTestCase(OWNED_MEMBER_FEATURES).model();
        final Element definition = member(root, 0);
        final Element action = member(root, 1);

        assertEquals(List.of(member(definition, 0)), SafeEmfAccess.unresolvedValues(definition.getOwnedRelationship().get(0),
                SysMLPackage.eINSTANCE.getFeatureMembership_OwnedMemberFeature()));
        assertEquals(List.of(member(action, 0)), SafeEmfAccess.unresolvedValues(action.getOwnedRelationship().get(0),
                SysMLPackage.eINSTANCE.getParameterMembership_OwnedMemberParameter()));
        assertEquals(List.of(), SafeEmfAccess.unresolvedValues(definition.getOwnedRelationship().get(1),
                SysMLPackage.eINSTANCE.getFeatureMembership_OwnedMemberFeature()));
    }

    /** Returns the element owned through the owned relationship at {@code index} of {@code owner}. */
    private static Element member(final Element owner, final int index) {
        return owner.getOwnedRelationship().get(index).getOwnedRelatedElement().get(0);
    }
}
