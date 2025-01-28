/*******************************************************************************
 * Copyright (c) 2024, 2025 Obeo.
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
import org.eclipse.syson.sysml.Package;
import org.eclipse.syson.sysml.PartDefinition;
import org.eclipse.syson.sysml.PartUsage;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.util.ModelBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link MembershipImpl} related tests.
 *
 * @author arichard
 */
public class MembershipImplTest {

    private ModelBuilder builder;

    private PartUsage p1;

    private PartUsage p2;

    private PartUsage p3;

    private PartUsage p4;

    /**
     * <pre>
     * .
     *  package Pkg1 {
     *    part def Def1 {
     *      part p1 : Def1;
     *      part p2 : Def1;
     *      part p1 : Def2;
     *      part p1 : Def1;
     *    }
     *
     *    part def Def2;
     *  }
     * </pre>
     */
    @BeforeEach
    public void setUp() {
        this.builder = new ModelBuilder();
        Package pkg1 = this.builder.createWithName(Package.class, "Pkg1");
        PartDefinition def1 = this.builder.createInWithName(PartDefinition.class, pkg1, "Def1");
        PartDefinition def2 = this.builder.createInWithName(PartDefinition.class, pkg1, "Def2");
        this.p1 = this.builder.createInWithName(PartUsage.class, def1, "p1");
        this.builder.setType(this.p1, def1);
        this.p2 = this.builder.createInWithName(PartUsage.class, def1, "p2");
        this.builder.setType(this.p2, def1);
        this.p3 = this.builder.createInWithName(PartUsage.class, def1, "p1");
        this.builder.setType(this.p3, def2);
        this.p4 = this.builder.createInWithName(PartUsage.class, def1, "p1");
        this.builder.setType(this.p4, def1);
    }

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

    @DisplayName("Given two memberships containg different name and different type, when isDistinguishableFrom is called, then the result is true")
    @Test
    public void isDistinguishableFromTest1() {
        var lhs = this.p1.getOwningMembership();
        var rhs = this.p2.getOwningMembership();
        assertTrue(lhs.isDistinguishableFrom(rhs));
    }

    @DisplayName("Given two memberships containg different name but same type, when isDistinguishableFrom is called, then the result is true")
    @Test
    public void isDistinguishableFromTest2() {
        var lhs = this.p1.getOwningMembership();
        var rhs = this.p2.getOwningMembership();
        assertTrue(lhs.isDistinguishableFrom(rhs));
    }

    @DisplayName("Given two memberships containg same name but different type, when isDistinguishableFrom is called, then the result is false")
    @Test
    public void isDistinguishableFromTest3() {
        var lhs = this.p1.getOwningMembership();
        var rhs = this.p3.getOwningMembership();
        assertFalse(lhs.isDistinguishableFrom(rhs));
    }

    @DisplayName("Given two memberships containg same name and same type, when isDistinguishableFrom is called, then the result is false")
    @Test
    public void isDistinguishableFromTest4() {
        var lhs = this.p1.getOwningMembership();
        var rhs = this.p4.getOwningMembership();
        assertFalse(lhs.isDistinguishableFrom(rhs));
    }

    @DisplayName("Given same memberships, when isDistinguishableFrom is called, then the result is false")
    @Test
    public void isDistinguishableFromTest5() {
        var lhs = this.p1.getOwningMembership();
        assertFalse(lhs.isDistinguishableFrom(lhs));
    }
}
