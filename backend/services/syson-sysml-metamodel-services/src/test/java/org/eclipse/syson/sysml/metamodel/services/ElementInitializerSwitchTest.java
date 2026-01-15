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
package org.eclipse.syson.sysml.metamodel.services;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.VisibilityKind;
import org.eclipse.syson.sysml.util.ElementUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * ElementInitializerSwitch-related tests.
 *
 * @author arichard
 */
public class ElementInitializerSwitchTest {

    private static final String ROOT = "root";

    private ResourceSet rSet;

    private Resource resource;

    private ElementInitializerSwitch elementInitializerSwitch;

    private ElementUtil elementUtil;

    @BeforeEach
    public void setUp() {
        this.rSet = new ResourceSetImpl();
        // Make sure the resources we manipulate use a CrossReferenceAdapter.
        this.rSet.eAdapters().add(new ECrossReferenceAdapter());
        this.resource = new XMIResourceImpl();
        this.rSet.getResources().add(this.resource);
        this.elementInitializerSwitch = new ElementInitializerSwitch();
        this.elementUtil = new ElementUtil();

    }

    @DisplayName("GIVEN an Actor, WHEN it is initialized, THEN it's name contains the count of the same kind of elements")
    @Test
    public void testActorDefaultName() {
        var root = SysmlFactory.eINSTANCE.createPackage();
        this.resource.getContents().add(root);
        root.setDeclaredName(ROOT);
        var p1 = SysmlFactory.eINSTANCE.createPartUsage();
        this.addInParent(p1, root);
        var a1 = SysmlFactory.eINSTANCE.createPartUsage();
        this.addInParent(a1, p1, SysmlPackage.eINSTANCE.getActorMembership());
        var initializedA1 = this.elementInitializerSwitch.doSwitch(a1);
        assertThat(initializedA1.getDeclaredName()).isEqualTo("actor1");
        var a2 = SysmlFactory.eINSTANCE.createPartUsage();
        this.addInParent(a2, p1, SysmlPackage.eINSTANCE.getActorMembership());
        var initializedA2 = this.elementInitializerSwitch.doSwitch(a2);
        assertThat(initializedA2.getDeclaredName()).isEqualTo("actor2");
    }

    @DisplayName("GIVEN a EnumerationDefinition, WHEN it is initialized, THEN it's name contains the count of the same kind of elements")
    @Test
    public void testEnumerationDefinitionDefaultName() {
        var root = SysmlFactory.eINSTANCE.createPackage();
        this.resource.getContents().add(root);
        root.setDeclaredName(ROOT);
        var ed1 = SysmlFactory.eINSTANCE.createEnumerationDefinition();
        this.addInParent(ed1, root);
        var initializedED1 = this.elementInitializerSwitch.doSwitch(ed1);
        assertThat(initializedED1.getDeclaredName()).isEqualTo("EnumerationDefinition1");
        var ed2 = SysmlFactory.eINSTANCE.createEnumerationDefinition();
        this.addInParent(ed2, root);
        var initializedED2 = this.elementInitializerSwitch.doSwitch(ed2);
        assertThat(initializedED2.getDeclaredName()).isEqualTo("EnumerationDefinition2");
    }

    @DisplayName("GIVEN an Expose, WHEN it is initialized, THEN it's visibility is protected and isImportAll is true")
    @Test
    public void testExposeVisibility() {
        var root = SysmlFactory.eINSTANCE.createPackage();
        this.resource.getContents().add(root);
        root.setDeclaredName(ROOT);
        var vu1 = SysmlFactory.eINSTANCE.createViewUsage();
        this.addInParent(vu1, root);
        var e1 = SysmlFactory.eINSTANCE.createMembershipExpose();
        this.addInParent(e1, root);
        this.elementInitializerSwitch.doSwitch(e1);
        assertThat(e1.getVisibility()).isEqualTo(VisibilityKind.PROTECTED);
        assertThat(e1.isIsImportAll()).isTrue();

    }

    @DisplayName("GIVEN a Package, WHEN it is initialized, THEN it's name contains the count of the same kind of elements")
    @Test
    public void testPackageDefaultName() {
        var root = SysmlFactory.eINSTANCE.createPackage();
        this.resource.getContents().add(root);
        root.setDeclaredName(ROOT);
        var p1 = SysmlFactory.eINSTANCE.createPackage();
        this.addInParent(p1, root);
        var initializedP1 = this.elementInitializerSwitch.doSwitch(p1);
        assertThat(initializedP1.getDeclaredName()).isEqualTo("Package1");
        var p2 = SysmlFactory.eINSTANCE.createPackage();
        this.addInParent(p2, root);
        var initializedP2 = this.elementInitializerSwitch.doSwitch(p2);
        assertThat(initializedP2.getDeclaredName()).isEqualTo("Package2");
    }

    @DisplayName("GIVEN a PartDefinition, WHEN it is initialized, THEN it's name contains the count of the same kind of elements")
    @Test
    public void testPartDefinitionDefaultName() {
        var root = SysmlFactory.eINSTANCE.createPackage();
        this.resource.getContents().add(root);
        root.setDeclaredName(ROOT);
        var p1 = SysmlFactory.eINSTANCE.createPartDefinition();
        this.addInParent(p1, root);
        var initializedP1 = this.elementInitializerSwitch.doSwitch(p1);
        assertThat(initializedP1.getDeclaredName()).isEqualTo("PartDefinition1");
        var p2 = SysmlFactory.eINSTANCE.createPartDefinition();
        this.addInParent(p2, root);
        var initializedP2 = this.elementInitializerSwitch.doSwitch(p2);
        assertThat(initializedP2.getDeclaredName()).isEqualTo("PartDefinition2");
    }

    @DisplayName("GIVEN a PartUsage, WHEN it is initialized, THEN it's name contains the count of the same kind of elements")
    @Test
    public void testPartUsageDefaultName() {
        var root = SysmlFactory.eINSTANCE.createPackage();
        this.resource.getContents().add(root);
        root.setDeclaredName(ROOT);
        var p1 = SysmlFactory.eINSTANCE.createPartUsage();
        this.addInParent(p1, root);
        var initializedP1 = this.elementInitializerSwitch.doSwitch(p1);
        assertThat(initializedP1.getDeclaredName()).isEqualTo("part1");
        var p2 = SysmlFactory.eINSTANCE.createPartUsage();
        this.addInParent(p2, root);
        var initializedP2 = this.elementInitializerSwitch.doSwitch(p2);
        assertThat(initializedP2.getDeclaredName()).isEqualTo("part2");
    }

    @DisplayName("GIVEN a Stakeholder, WHEN it is initialized, THEN it's name contains the count of the same kind of elements")
    @Test
    public void testStakeholderDefaultName() {
        var root = SysmlFactory.eINSTANCE.createPackage();
        this.resource.getContents().add(root);
        root.setDeclaredName(ROOT);
        var p1 = SysmlFactory.eINSTANCE.createPartUsage();
        this.addInParent(p1, root);
        var s1 = SysmlFactory.eINSTANCE.createPartUsage();
        this.addInParent(s1, p1, SysmlPackage.eINSTANCE.getStakeholderMembership());
        var initializedS1 = this.elementInitializerSwitch.doSwitch(s1);
        assertThat(initializedS1.getDeclaredName()).isEqualTo("stakeholder1");
        var s2 = SysmlFactory.eINSTANCE.createPartUsage();
        this.addInParent(s2, p1, SysmlPackage.eINSTANCE.getStakeholderMembership());
        var initializedS2 = this.elementInitializerSwitch.doSwitch(s2);
        assertThat(initializedS2.getDeclaredName()).isEqualTo("stakeholder2");
    }

    private void addInParent(Element element, Element parent) {
        OwningMembership owningMembership = SysmlFactory.eINSTANCE.createOwningMembership();
        parent.getOwnedRelationship().add(owningMembership);
        owningMembership.getOwnedRelatedElement().add(element);
    }

    private void addInParent(Element element, Element parent, EClass membershipKind) {
        var membership = SysmlFactory.eINSTANCE.create(membershipKind);
        assertThat(membership).isInstanceOf(Membership.class);
        parent.getOwnedRelationship().add((Relationship) membership);
        ((Relationship) membership).getOwnedRelatedElement().add(element);
    }
}
