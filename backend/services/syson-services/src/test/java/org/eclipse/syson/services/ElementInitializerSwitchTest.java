/*******************************************************************************
 * Copyright (c) 2025 Obeo.
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
package org.eclipse.syson.services;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.sirius.components.emf.services.EditingContextCrossReferenceAdapter;
import org.eclipse.sirius.emfjson.resource.JsonResourceFactoryImpl;
import org.eclipse.syson.sysml.Element;
import org.eclipse.syson.sysml.Membership;
import org.eclipse.syson.sysml.OwningMembership;
import org.eclipse.syson.sysml.Relationship;
import org.eclipse.syson.sysml.SysmlFactory;
import org.eclipse.syson.sysml.SysmlPackage;
import org.eclipse.syson.sysml.ViewDefinition;
import org.eclipse.syson.sysml.ViewUsage;
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
        // Make sure the resources we manipulate use the Sirius Web CrossReferenceAdapter.
        // This is particularly important when manipulating memberships, where the cross-referencer may produce
        // unexpected behaviors that cannot be observed in a resource without it.
        this.rSet.eAdapters().add(new EditingContextCrossReferenceAdapter());
        this.resource = new JsonResourceFactoryImpl().createResource(URI.createURI("ElementInitializerSwitchTest"));
        this.rSet.getResources().add(this.resource);
        this.elementInitializerSwitch = new ElementInitializerSwitch();
        this.elementUtil = new ElementUtil();

    }

    @DisplayName("Given an Actor, when it is initialized, then it's name contains the count of the same kind of elements")
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

    @DisplayName("Given a EnumerationDefinition, when it is initialized, then it's name contains the count of the same kind of elements")
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

    @DisplayName("Given a Package, when it is initialized, then it's name contains the count of the same kind of elements")
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

    @DisplayName("Given a PartDefinition, when it is initialized, then it's name contains the count of the same kind of elements")
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

    @DisplayName("Given a PartUsage, when it is initialized, then it's name contains the count of the same kind of elements")
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

    @DisplayName("Given a Stakeholder, when it is initialized, then it's name contains the count of the same kind of elements")
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

    @DisplayName("Given a ViewUsage, when it is initialized, then it's typed by default with the GeneralView ViewDefinition")
    @Test
    public void testViewUsageDefaultType() {
        var root = SysmlFactory.eINSTANCE.createPackage();
        this.resource.getContents().add(root);
        root.setDeclaredName(ROOT);
        var view1 = SysmlFactory.eINSTANCE.createViewUsage();
        this.addInParent(view1, root);
        var initializedView1 = this.elementInitializerSwitch.doSwitch(view1);
        assertThat(initializedView1).isInstanceOf(ViewUsage.class);
        var generalViewViewDef = this.elementUtil.findByNameAndType(view1, "StandardViewDefinitions::GeneralView", ViewDefinition.class);
        assertThat(((ViewUsage) initializedView1).getType()).contains(generalViewViewDef);

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
