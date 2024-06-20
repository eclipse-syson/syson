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
package org.eclipse.syson.sysml.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.syson.sysml.AttributeDefinition;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.junit.jupiter.api.Test;

/**
 * {@link MembershipImpl} related tests.
 *
 * @author arichard
 */
public class MembershipImplTest {

    @Test
    public void testSource() {
        Namespace namespace = SysmlFactory.eINSTANCE.createNamespace();
        List<Element> mySources = new BasicEList<>();
        mySources.add(namespace);
        Membership membership = SysmlFactory.eINSTANCE.createMembership();
        assertTrue(membership.getSource().isEmpty());
        assertNull(membership.getMembershipOwningNamespace());
        membership.eSet(SysmlPackage.eINSTANCE.getRelationship_Source(), mySources);
        // membershipOwningNamespace is derived so it must remain null when an element is added to source
        assertTrue(membership.getSource().isEmpty());
        assertNull(membership.getMembershipOwningNamespace());
    }

    @Test
    public void testTarget() {
        AttributeDefinition attDef = SysmlFactory.eINSTANCE.createAttributeDefinition();
        List<Element> myTargets = new BasicEList<>();
        myTargets.add(attDef);
        Membership membership = SysmlFactory.eINSTANCE.createMembership();
        assertTrue(membership.getTarget().isEmpty());
        assertNull(membership.getMemberElement());
        membership.eSet(SysmlPackage.eINSTANCE.getRelationship_Target(), myTargets);
        assertFalse(membership.getTarget().isEmpty());
        assertNotNull(membership.getMemberElement());
    }
}
