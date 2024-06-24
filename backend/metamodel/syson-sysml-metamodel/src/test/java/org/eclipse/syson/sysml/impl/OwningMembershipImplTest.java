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

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Namespace;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.junit.jupiter.api.Test;

/**
 * {@link OwningMembershipImpl} related tests.
 *
 * @author arichard
 */
public class OwningMembershipImplTest {

    @Test
    public void testSource() {
        Namespace namespace = SysmlFactory.eINSTANCE.createNamespace();
        List<Element> mySources = new BasicEList<>();
        mySources.add(namespace);
        OwningMembership owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
        assertTrue(owningMembership.getSource().isEmpty());
        assertNull(owningMembership.getMembershipOwningNamespace());
        Object eGet = owningMembership.eGet(SysmlPackage.eINSTANCE.getRelationship_Source());
        // membershipOwningNamespace is derived so it must remain null when an element is added to source
        assertTrue(owningMembership.getSource().isEmpty());
        assertNull(owningMembership.getMembershipOwningNamespace());
    }
}
